package org.cybergarage.upnp;

import com.samsungimaging.connectionmanager.util.Trace;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import org.cybergarage.http.HTTP;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.upnp.device.ST;
import org.cybergarage.upnp.event.NotifyRequest;
import org.cybergarage.upnp.event.Subscriber;
import org.cybergarage.upnp.event.SubscriberList;
import org.cybergarage.upnp.ssdp.SSDPNotifyRequest;
import org.cybergarage.upnp.ssdp.SSDPNotifySocket;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.upnp.xml.ServiceData;
import org.cybergarage.util.Debug;
import org.cybergarage.util.Mutex;
import org.cybergarage.util.StringUtil;
import org.cybergarage.xml.Node;
import org.cybergarage.xml.ParserException;

public class Service {
  private static final String CONTROL_URL = "controlURL";
  
  public static final String ELEM_NAME = "service";
  
  private static final String EVENT_SUB_URL = "eventSubURL";
  
  public static final String MAJOR = "major";
  
  public static final String MAJOR_VALUE = "1";
  
  public static final String MINOR = "minor";
  
  public static final String MINOR_VALUE = "0";
  
  private static final String SCPDURL = "SCPDURL";
  
  public static final String SCPD_ROOTNODE = "scpd";
  
  public static final String SCPD_ROOTNODE_NS = "urn:schemas-upnp-org:service-1-0";
  
  private static final String SERVICE_ID = "serviceId";
  
  private static final String SERVICE_TYPE = "serviceType";
  
  public static final String SPEC_VERSION = "specVersion";
  
  private Trace.Tag TAG = Trace.Tag.CYBERGARAGE;
  
  private Mutex mutex = new Mutex();
  
  private Node serviceNode;
  
  private Object userData = null;
  
  public Service() {
    this(new Node("service"));
    Node node1 = new Node("specVersion");
    Node node2 = new Node("major");
    node2.setValue("1");
    node1.addNode(node2);
    node2 = new Node("minor");
    node2.setValue("0");
    node1.addNode(node2);
    node2 = new Node("scpd");
    node2.addAttribute("xmlns", "urn:schemas-upnp-org:service-1-0");
    node2.addNode(node1);
    getServiceData().setSCPDNode(node2);
  }
  
  public Service(Node paramNode) {
    this.serviceNode = paramNode;
  }
  
  private Node getDeviceNode() {
    Node node = getServiceNode().getParentNode();
    return (node == null) ? null : node.getParentNode();
  }
  
  private String getNotifyServiceTypeNT() {
    return getServiceType();
  }
  
  private String getNotifyServiceTypeUSN() {
    return String.valueOf(getDevice().getUDN()) + "::" + getServiceType();
  }
  
  private Node getRootNode() {
    return getServiceNode().getRootNode();
  }
  
  private Node getSCPDNode() {
    Node node1 = null;
    ServiceData serviceData = getServiceData();
    Node node2 = serviceData.getSCPDNode();
    if (node2 != null)
      return node2; 
    Device device = getDevice();
    if (device != null) {
      String str = getSCPDURL();
      try {
        Node node = getSCPDNode(new URL(device.getAbsoluteURL(str)));
        if (node != null) {
          serviceData.setSCPDNode(node);
          return node;
        } 
      } catch (Exception exception1) {}
      str = String.valueOf(device.getDescriptionFilePath()) + HTTP.toRelativeURL(str);
      try {
        getSCPDNode(new File(str));
        return null;
      } catch (Exception exception) {
        Debug.warning(exception);
        return null;
      } 
    } 
    return (Node)exception;
  }
  
  private Node getSCPDNode(File paramFile) throws ParserException {
    return UPnP.getXMLParser().parse(paramFile);
  }
  
  private Node getSCPDNode(URL paramURL) throws ParserException {
    return UPnP.getXMLParser().parse(paramURL, null, null);
  }
  
  private ServiceData getServiceData() {
    Node node = getServiceNode();
    ServiceData serviceData2 = (ServiceData)node.getUserData();
    ServiceData serviceData1 = serviceData2;
    if (serviceData2 == null) {
      serviceData1 = new ServiceData();
      node.setUserData(serviceData1);
      serviceData1.setNode(node);
    } 
    return serviceData1;
  }
  
  public static boolean isServiceNode(Node paramNode) {
    return "service".equals(paramNode.getName());
  }
  
  private boolean isURL(String paramString1, String paramString2) {
    boolean bool2 = true;
    if (paramString1 == null || paramString2 == null)
      return false; 
    boolean bool1 = bool2;
    if (!paramString2.equals(paramString1)) {
      bool1 = bool2;
      if (!paramString2.equals(HTTP.toRelativeURL(paramString1, false)))
        return false; 
    } 
    return bool1;
  }
  
  private boolean notify(Subscriber paramSubscriber, StateVariable paramStateVariable) {
    String str2 = paramStateVariable.getName();
    String str1 = paramStateVariable.getValue();
    String str3 = paramSubscriber.getDeliveryHost();
    int i = paramSubscriber.getDeliveryPort();
    NotifyRequest notifyRequest = new NotifyRequest();
    notifyRequest.setRequest(paramSubscriber, str2, str1);
    if (!notifyRequest.post(str3, i).isSuccessful())
      return false; 
    paramSubscriber.incrementNotifyCount();
    return true;
  }
  
  public void addAction(Action paramAction) {
    Iterator<E> iterator = paramAction.getArgumentList().iterator();
    while (true) {
      Node<Argument> node;
      if (!iterator.hasNext()) {
        Node node2 = getSCPDNode();
        Node<Argument> node1 = node2.getNode("actionList");
        node = node1;
        if (node1 == null) {
          node = new Node("actionList");
          node2.addNode(node);
        } 
        node.addNode(paramAction.getActionNode());
        return;
      } 
      ((Argument)node.next()).setService(this);
    } 
  }
  
  public void addStateVariable(StateVariable paramStateVariable) {
    Node node2 = getSCPDNode().getNode("serviceStateTable");
    Node node1 = node2;
    if (node2 == null) {
      node1 = new Node("serviceStateTable");
      getSCPDNode().addNode(node1);
    } 
    paramStateVariable.setServiceNode(getServiceNode());
    node1.addNode(paramStateVariable.getStateVariableNode());
  }
  
  public void addSubscriber(Subscriber paramSubscriber) {
    getSubscriberList().add(paramSubscriber);
  }
  
  public void announce(String paramString) {
    String str1 = getRootDevice().getLocationURL(paramString);
    String str2 = getNotifyServiceTypeNT();
    String str3 = getNotifyServiceTypeUSN();
    Device device = getDevice();
    SSDPNotifyRequest sSDPNotifyRequest = new SSDPNotifyRequest();
    sSDPNotifyRequest.setServer(UPnP.getServerName());
    sSDPNotifyRequest.setLeaseTime(device.getLeaseTime());
    sSDPNotifyRequest.setLocation(str1);
    sSDPNotifyRequest.setNTS("ssdp:alive");
    sSDPNotifyRequest.setNT(str2);
    sSDPNotifyRequest.setUSN(str3);
    SSDPNotifySocket sSDPNotifySocket = new SSDPNotifySocket(paramString);
    Device.notifyWait();
    sSDPNotifySocket.post(sSDPNotifyRequest);
  }
  
  public void byebye(String paramString) {
    String str1 = getNotifyServiceTypeNT();
    String str2 = getNotifyServiceTypeUSN();
    SSDPNotifyRequest sSDPNotifyRequest = new SSDPNotifyRequest();
    sSDPNotifyRequest.setNTS("ssdp:byebye");
    sSDPNotifyRequest.setNT(str1);
    sSDPNotifyRequest.setUSN(str2);
    SSDPNotifySocket sSDPNotifySocket = new SSDPNotifySocket(paramString);
    Device.notifyWait();
    sSDPNotifySocket.post(sSDPNotifyRequest);
  }
  
  public void clearSID() {
    setSID("");
    setTimeout(0L);
  }
  
  public Action getAction(String paramString) {
    ActionList actionList = getActionList();
    int j = actionList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return null; 
      Action action = actionList.getAction(i);
      String str = action.getName();
      if (str != null && str.equals(paramString))
        return action; 
    } 
  }
  
  public ActionList getActionList() {
    ActionList actionList = new ActionList();
    Node node = getSCPDNode();
    if (node != null) {
      node = node.getNode("actionList");
      if (node != null) {
        int j = node.getNNodes();
        int i = 0;
        while (true) {
          if (i < j) {
            Node node1 = node.getNode(i);
            if (Action.isActionNode(node1)) {
              Action action = new Action(this.serviceNode, node1);
              Trace.d(this.TAG, "action Name : " + action.getName());
              actionList.add((E)action);
            } 
            i++;
            continue;
          } 
          return actionList;
        } 
      } 
    } 
    return actionList;
  }
  
  public String getControlURL() {
    return getServiceNode().getNodeValue("controlURL");
  }
  
  public String getDescriptionURL() {
    return getServiceData().getDescriptionURL();
  }
  
  public Device getDevice() {
    return new Device(getRootNode(), getDeviceNode());
  }
  
  public String getEventSubURL() {
    return getServiceNode().getNodeValue("eventSubURL");
  }
  
  public Device getRootDevice() {
    return getDevice().getRootDevice();
  }
  
  public byte[] getSCPDData() {
    Node node = getSCPDNode();
    return (node == null) ? new byte[0] : (String.valueOf(String.valueOf(String.valueOf(new String()) + "<?xml version=\"1.0\" encoding=\"utf-8\"?>") + "\n") + node.toString()).getBytes();
  }
  
  public String getSCPDURL() {
    return getServiceNode().getNodeValue("SCPDURL");
  }
  
  public String getSID() {
    return getServiceData().getSID();
  }
  
  public String getServiceID() {
    return getServiceNode().getNodeValue("serviceId");
  }
  
  public Node getServiceNode() {
    return this.serviceNode;
  }
  
  public ServiceStateTable getServiceStateTable() {
    ServiceStateTable serviceStateTable = new ServiceStateTable();
    Node node = getSCPDNode().getNode("serviceStateTable");
    if (node != null) {
      Node node1 = getServiceNode();
      int j = node.getNNodes();
      int i = 0;
      while (true) {
        if (i < j) {
          Node node2 = node.getNode(i);
          if (StateVariable.isStateVariableNode(node2))
            serviceStateTable.add((E)new StateVariable(node1, node2)); 
          i++;
          continue;
        } 
        return serviceStateTable;
      } 
    } 
    return serviceStateTable;
  }
  
  public String getServiceType() {
    return getServiceNode().getNodeValue("serviceType");
  }
  
  public StateVariable getStateVariable(String paramString) {
    ServiceStateTable serviceStateTable = getServiceStateTable();
    int j = serviceStateTable.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return null; 
      StateVariable stateVariable = serviceStateTable.getStateVariable(i);
      String str = stateVariable.getName();
      if (str != null && str.equals(paramString))
        return stateVariable; 
    } 
  }
  
  public Subscriber getSubscriber(String paramString) {
    SubscriberList subscriberList = getSubscriberList();
    int j = subscriberList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return null; 
      Subscriber subscriber = subscriberList.getSubscriber(i);
      if (subscriber != null) {
        String str = subscriber.getSID();
        if (str != null && str.equals(paramString))
          return subscriber; 
      } 
    } 
  }
  
  public SubscriberList getSubscriberList() {
    return getServiceData().getSubscriberList();
  }
  
  public long getTimeout() {
    return getServiceData().getTimeout();
  }
  
  public Object getUserData() {
    return this.userData;
  }
  
  public boolean hasSID() {
    return StringUtil.hasData(getSID());
  }
  
  public boolean hasStateVariable(String paramString) {
    return (getStateVariable(paramString) != null);
  }
  
  public boolean isControlURL(String paramString) {
    return isURL(getControlURL(), paramString);
  }
  
  public boolean isEventSubURL(String paramString) {
    return isURL(getEventSubURL(), paramString);
  }
  
  public boolean isSCPDURL(String paramString) {
    return isURL(getSCPDURL(), paramString);
  }
  
  public boolean isService(String paramString) {
    if (paramString != null) {
      if (paramString.endsWith(getServiceType()))
        return true; 
      if (paramString.endsWith(getServiceID()))
        return true; 
    } 
    return false;
  }
  
  public boolean isSubscribed() {
    return hasSID();
  }
  
  public boolean loadSCPD(File paramFile) throws ParserException {
    Node node = UPnP.getXMLParser().parse(paramFile);
    if (node == null)
      return false; 
    getServiceData().setSCPDNode(node);
    return true;
  }
  
  public boolean loadSCPD(InputStream paramInputStream) throws ParserException {
    Node node = UPnP.getXMLParser().parse(paramInputStream);
    if (node == null)
      return false; 
    getServiceData().setSCPDNode(node);
    return true;
  }
  
  public boolean loadSCPD(String paramString) throws InvalidDescriptionException {
    try {
      Node node = UPnP.getXMLParser().parse(paramString);
      if (node == null)
        return false; 
      getServiceData().setSCPDNode(node);
      return true;
    } catch (ParserException parserException) {
      throw new InvalidDescriptionException(parserException);
    } 
  }
  
  public void lock() {
    this.mutex.lock();
  }
  
  public void notify(StateVariable paramStateVariable) {
    SubscriberList subscriberList = getSubscriberList();
    int j = subscriberList.size();
    Subscriber[] arrayOfSubscriber = new Subscriber[j];
    for (int i = 0;; i++) {
      Subscriber subscriber;
      if (i >= j)
        for (i = 0;; i++) {
          if (i >= j) {
            j = subscriberList.size();
            arrayOfSubscriber = new Subscriber[j];
            for (i = 0;; i++) {
              if (i >= j) {
                for (i = 0;; i++) {
                  if (i >= j)
                    return; 
                  subscriber = arrayOfSubscriber[i];
                  if (subscriber != null)
                    notify(subscriber, paramStateVariable); 
                } 
                break;
              } 
              arrayOfSubscriber[i] = subscriber.getSubscriber(i);
            } 
            break;
          } 
          Subscriber subscriber1 = arrayOfSubscriber[i];
          if (subscriber1 != null && subscriber1.isExpired())
            removeSubscriber(subscriber1); 
        }  
      arrayOfSubscriber[i] = subscriber.getSubscriber(i);
    } 
  }
  
  public void notifyAllStateVariables() {
    ServiceStateTable serviceStateTable = getServiceStateTable();
    int j = serviceStateTable.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      StateVariable stateVariable = serviceStateTable.getStateVariable(i);
      if (stateVariable.isSendEvents())
        notify(stateVariable); 
    } 
  }
  
  public void removeSubscriber(Subscriber paramSubscriber) {
    getSubscriberList().remove(paramSubscriber);
  }
  
  public boolean serviceSearchResponse(SSDPPacket paramSSDPPacket) {
    String str1 = paramSSDPPacket.getST();
    if (str1 == null)
      return false; 
    Device device = getDevice();
    String str3 = getNotifyServiceTypeNT();
    String str2 = getNotifyServiceTypeUSN();
    if (ST.isAllDevice(str1)) {
      device.postSearchResponse(paramSSDPPacket, str3, str2);
      return true;
    } 
    if (ST.isURNService(str1)) {
      str3 = getServiceType();
      if (str1.equals(str3))
        device.postSearchResponse(paramSSDPPacket, str3, str2); 
    } 
    return true;
  }
  
  public void setActionListener(ActionListener paramActionListener) {
    ActionList actionList = getActionList();
    int j = actionList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      actionList.getAction(i).setActionListener(paramActionListener);
    } 
  }
  
  public void setControlURL(String paramString) {
    getServiceNode().setNode("controlURL", paramString);
  }
  
  public void setDescriptionURL(String paramString) {
    getServiceData().setDescriptionURL(paramString);
  }
  
  public void setEventSubURL(String paramString) {
    getServiceNode().setNode("eventSubURL", paramString);
  }
  
  public void setQueryListener(QueryListener paramQueryListener) {
    ServiceStateTable serviceStateTable = getServiceStateTable();
    int j = serviceStateTable.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      serviceStateTable.getStateVariable(i).setQueryListener(paramQueryListener);
    } 
  }
  
  public void setSCPDURL(String paramString) {
    getServiceNode().setNode("SCPDURL", paramString);
  }
  
  public void setSID(String paramString) {
    getServiceData().setSID(paramString);
  }
  
  public void setServiceID(String paramString) {
    getServiceNode().setNode("serviceId", paramString);
  }
  
  public void setServiceType(String paramString) {
    getServiceNode().setNode("serviceType", paramString);
  }
  
  public void setTimeout(long paramLong) {
    getServiceData().setTimeout(paramLong);
  }
  
  public void setUserData(Object paramObject) {
    this.userData = paramObject;
  }
  
  public void unlock() {
    this.mutex.unlock();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\Service.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */