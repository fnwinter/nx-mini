package org.cybergarage.upnp;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.util.Calendar;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.http.HTTPRequestListener;
import org.cybergarage.http.HTTPResponse;
import org.cybergarage.http.HTTPServerList;
import org.cybergarage.net.HostInterface;
import org.cybergarage.soap.SOAPResponse;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.ActionRequest;
import org.cybergarage.upnp.control.ActionResponse;
import org.cybergarage.upnp.control.ControlRequest;
import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.control.QueryRequest;
import org.cybergarage.upnp.device.Advertiser;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.upnp.device.SearchListener;
import org.cybergarage.upnp.event.Subscriber;
import org.cybergarage.upnp.event.Subscription;
import org.cybergarage.upnp.event.SubscriptionRequest;
import org.cybergarage.upnp.event.SubscriptionResponse;
import org.cybergarage.upnp.ssdp.SSDPNotifyRequest;
import org.cybergarage.upnp.ssdp.SSDPNotifySocket;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.upnp.ssdp.SSDPSearchResponse;
import org.cybergarage.upnp.ssdp.SSDPSearchResponseSocket;
import org.cybergarage.upnp.ssdp.SSDPSearchSocketList;
import org.cybergarage.upnp.xml.DeviceData;
import org.cybergarage.util.Debug;
import org.cybergarage.util.FileUtil;
import org.cybergarage.util.Mutex;
import org.cybergarage.util.TimerUtil;
import org.cybergarage.xml.Node;
import org.cybergarage.xml.ParserException;

public class Device implements HTTPRequestListener, SearchListener {
  public static final String DEFAULT_DESCRIPTION_URI = "/description.xml";
  
  public static final int DEFAULT_DISCOVERY_WAIT_TIME = 300;
  
  public static final int DEFAULT_LEASE_TIME = 1800;
  
  public static final int DEFAULT_STARTUP_WAIT_TIME = 1000;
  
  private static final String DEVICE_TYPE = "deviceType";
  
  public static final String ELEM_NAME = "device";
  
  private static final String FRIENDLY_NAME = "friendlyName";
  
  public static final int HTTP_DEFAULT_PORT = 4004;
  
  private static final String MANUFACTURE = "manufacturer";
  
  private static final String MANUFACTURE_URL = "manufacturerURL";
  
  private static final String MODEL_DESCRIPTION = "modelDescription";
  
  private static final String MODEL_NAME = "modelName";
  
  private static final String MODEL_NUMBER = "modelNumber";
  
  private static final String MODEL_URL = "modelURL";
  
  private static final String SERIAL_NUMBER = "serialNumber";
  
  private static final String UDN = "UDN";
  
  private static final String UPC = "UPC";
  
  public static final String UPNP_ROOTDEVICE = "upnp:rootdevice";
  
  private static final String URLBASE_NAME = "URLBase";
  
  private static Calendar cal = Calendar.getInstance();
  
  private static final String presentationURL = "presentationURL";
  
  private String devUUID;
  
  private Node deviceNode;
  
  private Mutex mutex = new Mutex();
  
  private Node rootNode;
  
  private Object userData = null;
  
  private boolean wirelessMode;
  
  public Device() {
    this(null, null);
  }
  
  public Device(File paramFile) throws InvalidDescriptionException {
    this(null, null);
    loadDescription(paramFile);
  }
  
  public Device(InputStream paramInputStream) throws InvalidDescriptionException {
    this(null, null);
    loadDescription(paramInputStream);
  }
  
  public Device(String paramString) throws InvalidDescriptionException {
    this(new File(paramString));
  }
  
  public Device(Node paramNode) {
    this(null, paramNode);
  }
  
  public Device(Node paramNode1, Node paramNode2) {
    this.rootNode = paramNode1;
    this.deviceNode = paramNode2;
    setUUID(UPnP.createUUID());
    setWirelessMode(false);
  }
  
  private void deviceActionControlRecieved(ActionRequest paramActionRequest, Service paramService) {
    if (Debug.isOn())
      paramActionRequest.print(); 
    Action action = paramService.getAction(paramActionRequest.getActionName());
    if (action == null) {
      invalidActionControlRecieved((ControlRequest)paramActionRequest);
      return;
    } 
    ArgumentList argumentList1 = action.getArgumentList();
    ArgumentList argumentList2 = paramActionRequest.getArgumentList();
    try {
      argumentList1.setReqArgs(argumentList2);
      if (!action.performActionListener(paramActionRequest)) {
        invalidActionControlRecieved((ControlRequest)paramActionRequest);
        return;
      } 
      return;
    } catch (IllegalArgumentException illegalArgumentException) {
      invalidArgumentsControlRecieved((ControlRequest)paramActionRequest);
      return;
    } 
  }
  
  private void deviceControlRequestRecieved(ControlRequest paramControlRequest, Service paramService) {
    if (paramControlRequest.isQueryControl()) {
      deviceQueryControlRecieved(new QueryRequest((HTTPRequest)paramControlRequest), paramService);
      return;
    } 
    deviceActionControlRecieved(new ActionRequest((HTTPRequest)paramControlRequest), paramService);
  }
  
  private void deviceEventNewSubscriptionRecieved(Service paramService, SubscriptionRequest paramSubscriptionRequest) {
    String str = paramSubscriptionRequest.getCallback();
    try {
      new URL(str);
      long l = paramSubscriptionRequest.getTimeout();
      String str1 = Subscription.createSID();
      Subscriber subscriber = new Subscriber();
      subscriber.setDeliveryURL(str);
      subscriber.setTimeOut(l);
      subscriber.setSID(str1);
      paramService.addSubscriber(subscriber);
      SubscriptionResponse subscriptionResponse = new SubscriptionResponse();
      subscriptionResponse.setStatusCode(200);
      subscriptionResponse.setSID(str1);
      subscriptionResponse.setTimeout(l);
      if (Debug.isOn())
        subscriptionResponse.print(); 
      paramSubscriptionRequest.post(subscriptionResponse);
      if (Debug.isOn())
        subscriptionResponse.print(); 
      paramService.notifyAllStateVariables();
      return;
    } catch (Exception exception) {
      upnpBadSubscriptionRecieved(paramSubscriptionRequest, 412);
      return;
    } 
  }
  
  private void deviceEventRenewSubscriptionRecieved(Service paramService, SubscriptionRequest paramSubscriptionRequest) {
    String str = paramSubscriptionRequest.getSID();
    Subscriber subscriber = paramService.getSubscriber(str);
    if (subscriber == null) {
      upnpBadSubscriptionRecieved(paramSubscriptionRequest, 412);
      return;
    } 
    long l = paramSubscriptionRequest.getTimeout();
    subscriber.setTimeOut(l);
    subscriber.renew();
    SubscriptionResponse subscriptionResponse = new SubscriptionResponse();
    subscriptionResponse.setStatusCode(200);
    subscriptionResponse.setSID(str);
    subscriptionResponse.setTimeout(l);
    paramSubscriptionRequest.post(subscriptionResponse);
    if (Debug.isOn()) {
      subscriptionResponse.print();
      return;
    } 
  }
  
  private void deviceEventSubscriptionRecieved(SubscriptionRequest paramSubscriptionRequest) {
    Service service = getServiceByEventSubURL(paramSubscriptionRequest.getURI());
    if (service == null) {
      paramSubscriptionRequest.returnBadRequest();
      return;
    } 
    if (!paramSubscriptionRequest.hasCallback() && !paramSubscriptionRequest.hasSID()) {
      upnpBadSubscriptionRecieved(paramSubscriptionRequest, 412);
      return;
    } 
    if (paramSubscriptionRequest.isUnsubscribeRequest()) {
      deviceEventUnsubscriptionRecieved(service, paramSubscriptionRequest);
      return;
    } 
    if (paramSubscriptionRequest.hasCallback()) {
      deviceEventNewSubscriptionRecieved(service, paramSubscriptionRequest);
      return;
    } 
    if (paramSubscriptionRequest.hasSID()) {
      deviceEventRenewSubscriptionRecieved(service, paramSubscriptionRequest);
      return;
    } 
    upnpBadSubscriptionRecieved(paramSubscriptionRequest, 412);
  }
  
  private void deviceEventUnsubscriptionRecieved(Service paramService, SubscriptionRequest paramSubscriptionRequest) {
    Subscriber subscriber = paramService.getSubscriber(paramSubscriptionRequest.getSID());
    if (subscriber == null) {
      upnpBadSubscriptionRecieved(paramSubscriptionRequest, 412);
      return;
    } 
    paramService.removeSubscriber(subscriber);
    SubscriptionResponse subscriptionResponse = new SubscriptionResponse();
    subscriptionResponse.setStatusCode(200);
    paramSubscriptionRequest.post(subscriptionResponse);
    if (Debug.isOn()) {
      subscriptionResponse.print();
      return;
    } 
  }
  
  private void deviceQueryControlRecieved(QueryRequest paramQueryRequest, Service paramService) {
    if (Debug.isOn())
      paramQueryRequest.print(); 
    String str = paramQueryRequest.getVarName();
    if (!paramService.hasStateVariable(str)) {
      invalidActionControlRecieved((ControlRequest)paramQueryRequest);
      return;
    } 
    if (!getStateVariable(str).performQueryListener(paramQueryRequest)) {
      invalidActionControlRecieved((ControlRequest)paramQueryRequest);
      return;
    } 
  }
  
  private Advertiser getAdvertiser() {
    return getDeviceData().getAdvertiser();
  }
  
  private byte[] getDescriptionData(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual isNMPRMode : ()Z
    //   6: ifne -> 14
    //   9: aload_0
    //   10: aload_1
    //   11: invokespecial updateURLBase : (Ljava/lang/String;)V
    //   14: aload_0
    //   15: invokevirtual getRootNode : ()Lorg/cybergarage/xml/Node;
    //   18: astore_1
    //   19: aload_1
    //   20: ifnonnull -> 31
    //   23: iconst_0
    //   24: newarray byte
    //   26: astore_1
    //   27: aload_0
    //   28: monitorexit
    //   29: aload_1
    //   30: areturn
    //   31: new java/lang/StringBuilder
    //   34: dup
    //   35: new java/lang/StringBuilder
    //   38: dup
    //   39: new java/lang/StringBuilder
    //   42: dup
    //   43: new java/lang/String
    //   46: dup
    //   47: invokespecial <init> : ()V
    //   50: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   53: invokespecial <init> : (Ljava/lang/String;)V
    //   56: ldc_w '<?xml version="1.0" encoding="utf-8"?>'
    //   59: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   62: invokevirtual toString : ()Ljava/lang/String;
    //   65: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   68: invokespecial <init> : (Ljava/lang/String;)V
    //   71: ldc_w '\\n'
    //   74: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   77: invokevirtual toString : ()Ljava/lang/String;
    //   80: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   83: invokespecial <init> : (Ljava/lang/String;)V
    //   86: aload_1
    //   87: invokevirtual toString : ()Ljava/lang/String;
    //   90: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   93: invokevirtual toString : ()Ljava/lang/String;
    //   96: invokevirtual getBytes : ()[B
    //   99: astore_1
    //   100: goto -> 27
    //   103: astore_1
    //   104: aload_0
    //   105: monitorexit
    //   106: aload_1
    //   107: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	103	finally
    //   14	19	103	finally
    //   23	27	103	finally
    //   31	100	103	finally
  }
  
  private String getDescriptionURI() {
    return getDeviceData().getDescriptionURI();
  }
  
  private DeviceData getDeviceData() {
    Node node = getDeviceNode();
    DeviceData deviceData2 = (DeviceData)node.getUserData();
    DeviceData deviceData1 = deviceData2;
    if (deviceData2 == null) {
      deviceData1 = new DeviceData();
      node.setUserData(deviceData1);
      deviceData1.setNode(node);
    } 
    return deviceData1;
  }
  
  private HTTPServerList getHTTPServerList() {
    return getDeviceData().getHTTPServerList();
  }
  
  private String getNotifyDeviceNT() {
    return !isRootDevice() ? getUDN() : "upnp:rootdevice";
  }
  
  private String getNotifyDeviceTypeNT() {
    return getDeviceType();
  }
  
  private String getNotifyDeviceTypeUSN() {
    return String.valueOf(getUDN()) + "::" + getDeviceType();
  }
  
  private String getNotifyDeviceUSN() {
    return !isRootDevice() ? getUDN() : (String.valueOf(getUDN()) + "::" + "upnp:rootdevice");
  }
  
  private SSDPSearchSocketList getSSDPSearchSocketList() {
    return getDeviceData().getSSDPSearchSocketList();
  }
  
  private String getUUID() {
    return this.devUUID;
  }
  
  private void httpGetRequestRecieved(HTTPRequest paramHTTPRequest) {
    byte[] arrayOfByte;
    String str = paramHTTPRequest.getURI();
    Debug.message("httpGetRequestRecieved = " + str);
    if (str == null) {
      paramHTTPRequest.returnBadRequest();
      return;
    } 
    if (isDescriptionURI(str)) {
      paramHTTPRequest.getLocalAddress();
      arrayOfByte = getDescriptionData(HostInterface.getInterface());
    } else {
      Device device = getDeviceByDescriptionURI(str);
      if (device != null) {
        arrayOfByte = device.getDescriptionData(paramHTTPRequest.getLocalAddress());
      } else {
        Service service = getServiceBySCPDURL(str);
        if (service != null) {
          arrayOfByte = service.getSCPDData();
        } else {
          paramHTTPRequest.returnBadRequest();
          return;
        } 
      } 
    } 
    HTTPResponse hTTPResponse = new HTTPResponse();
    if (FileUtil.isXMLFileName(str))
      hTTPResponse.setContentType("text/xml; charset=\"utf-8\""); 
    hTTPResponse.setStatusCode(200);
    hTTPResponse.setContent(arrayOfByte);
    paramHTTPRequest.post(hTTPResponse);
  }
  
  private void httpPostRequestRecieved(HTTPRequest paramHTTPRequest) {
    if (paramHTTPRequest.isSOAPAction()) {
      soapActionRecieved(paramHTTPRequest);
      return;
    } 
    paramHTTPRequest.returnBadRequest();
  }
  
  private boolean initializeLoadedDescription() {
    setDescriptionURI("/description.xml");
    setLeaseTime(1800);
    setHTTPPort(4004);
    if (!hasUDN())
      updateUDN(); 
    return true;
  }
  
  private void invalidActionControlRecieved(ControlRequest paramControlRequest) {
    ActionResponse actionResponse = new ActionResponse();
    actionResponse.setFaultResponse(401);
    paramControlRequest.post((HTTPResponse)actionResponse);
  }
  
  private void invalidArgumentsControlRecieved(ControlRequest paramControlRequest) {
    ActionResponse actionResponse = new ActionResponse();
    actionResponse.setFaultResponse(402);
    paramControlRequest.post((HTTPResponse)actionResponse);
  }
  
  private boolean isDescriptionURI(String paramString) {
    String str = getDescriptionURI();
    return (paramString == null || str == null) ? false : str.equals(paramString);
  }
  
  public static boolean isDeviceNode(Node paramNode) {
    return "device".equals(paramNode.getName());
  }
  
  public static final void notifyWait() {
    TimerUtil.waitRandom(300);
  }
  
  private void setAdvertiser(Advertiser paramAdvertiser) {
    getDeviceData().setAdvertiser(paramAdvertiser);
  }
  
  private void setDescriptionFile(File paramFile) {
    getDeviceData().setDescriptionFile(paramFile);
  }
  
  private void setDescriptionURI(String paramString) {
    getDeviceData().setDescriptionURI(paramString);
  }
  
  private void setURLBase(String paramString) {
    if (isRootDevice()) {
      Node node1 = getRootNode().getNode("URLBase");
      if (node1 != null) {
        node1.setValue(paramString);
        return;
      } 
    } else {
      return;
    } 
    Node node = new Node("URLBase");
    node.setValue(paramString);
    if (!getRootNode().hasNodes());
    getRootNode().insertNode(node, 1);
  }
  
  private void setUUID(String paramString) {
    this.devUUID = paramString;
  }
  
  private void soapActionRecieved(HTTPRequest paramHTTPRequest) {
    Service service = getServiceByControlURL(paramHTTPRequest.getURI());
    if (service != null) {
      deviceControlRequestRecieved((ControlRequest)new ActionRequest(paramHTTPRequest), service);
      return;
    } 
    soapBadActionRecieved(paramHTTPRequest);
  }
  
  private void soapBadActionRecieved(HTTPRequest paramHTTPRequest) {
    SOAPResponse sOAPResponse = new SOAPResponse();
    sOAPResponse.setStatusCode(400);
    paramHTTPRequest.post((HTTPResponse)sOAPResponse);
  }
  
  private boolean stop(boolean paramBoolean) {
    if (paramBoolean)
      byebye(); 
    HTTPServerList hTTPServerList = getHTTPServerList();
    hTTPServerList.stop();
    hTTPServerList.close();
    hTTPServerList.clear();
    SSDPSearchSocketList sSDPSearchSocketList = getSSDPSearchSocketList();
    sSDPSearchSocketList.stop();
    sSDPSearchSocketList.close();
    sSDPSearchSocketList.clear();
    Advertiser advertiser = getAdvertiser();
    if (advertiser != null) {
      advertiser.stop();
      setAdvertiser(null);
    } 
    return true;
  }
  
  private void updateUDN() {
    setUDN("uuid:" + getUUID());
  }
  
  private void updateURLBase(String paramString) {
    setURLBase(HostInterface.getHostURL(paramString, getHTTPPort(), ""));
  }
  
  private void upnpBadSubscriptionRecieved(SubscriptionRequest paramSubscriptionRequest, int paramInt) {
    SubscriptionResponse subscriptionResponse = new SubscriptionResponse();
    subscriptionResponse.setErrorResponse(paramInt);
    paramSubscriptionRequest.post(subscriptionResponse);
  }
  
  public void addDevice(Device paramDevice) {
    Node node2 = getDeviceNode().getNode("deviceList");
    Node node1 = node2;
    if (node2 == null) {
      node1 = new Node("deviceList");
      getDeviceNode().addNode(node1);
    } 
    node1.addNode(paramDevice.getDeviceNode());
    paramDevice.setRootNode(null);
    if (getRootNode() == null) {
      Node node3 = new Node("root");
      node3.setNameSpace("", "urn:schemas-upnp-org:device-1-0");
      node1 = new Node("specVersion");
      node2 = new Node("major");
      node2.setValue("1");
      Node node4 = new Node("minor");
      node4.setValue("0");
      node1.addNode(node2);
      node1.addNode(node4);
      node3.addNode(node1);
      setRootNode(node3);
    } 
  }
  
  public void addService(Service paramService) {
    Node node2 = getDeviceNode().getNode("serviceList");
    Node node1 = node2;
    if (node2 == null) {
      node1 = new Node("serviceList");
      getDeviceNode().addNode(node1);
    } 
    node1.addNode(paramService.getServiceNode());
  }
  
  public void announce() {
    // Byte code:
    //   0: invokestatic notifyWait : ()V
    //   3: aload_0
    //   4: invokespecial getDeviceData : ()Lorg/cybergarage/upnp/xml/DeviceData;
    //   7: invokevirtual getHTTPBindAddress : ()[Ljava/net/InetAddress;
    //   10: astore #5
    //   12: aload #5
    //   14: ifnull -> 62
    //   17: aload #5
    //   19: arraylength
    //   20: anewarray java/lang/String
    //   23: astore #4
    //   25: iconst_0
    //   26: istore_1
    //   27: iload_1
    //   28: aload #5
    //   30: arraylength
    //   31: if_icmplt -> 44
    //   34: iconst_0
    //   35: istore_1
    //   36: iload_1
    //   37: aload #4
    //   39: arraylength
    //   40: if_icmplt -> 98
    //   43: return
    //   44: aload #4
    //   46: iload_1
    //   47: aload #5
    //   49: iload_1
    //   50: aaload
    //   51: invokevirtual getHostAddress : ()Ljava/lang/String;
    //   54: aastore
    //   55: iload_1
    //   56: iconst_1
    //   57: iadd
    //   58: istore_1
    //   59: goto -> 27
    //   62: invokestatic getNHostAddresses : ()I
    //   65: istore_2
    //   66: iload_2
    //   67: anewarray java/lang/String
    //   70: astore #5
    //   72: iconst_0
    //   73: istore_1
    //   74: aload #5
    //   76: astore #4
    //   78: iload_1
    //   79: iload_2
    //   80: if_icmpge -> 34
    //   83: aload #5
    //   85: iload_1
    //   86: iload_1
    //   87: invokestatic getHostAddress : (I)Ljava/lang/String;
    //   90: aastore
    //   91: iload_1
    //   92: iconst_1
    //   93: iadd
    //   94: istore_1
    //   95: goto -> 74
    //   98: aload #4
    //   100: iload_1
    //   101: aaload
    //   102: ifnull -> 115
    //   105: aload #4
    //   107: iload_1
    //   108: aaload
    //   109: invokevirtual length : ()I
    //   112: ifne -> 122
    //   115: iload_1
    //   116: iconst_1
    //   117: iadd
    //   118: istore_1
    //   119: goto -> 36
    //   122: aload_0
    //   123: invokevirtual getSSDPAnnounceCount : ()I
    //   126: istore_3
    //   127: iconst_0
    //   128: istore_2
    //   129: iload_2
    //   130: iload_3
    //   131: if_icmpge -> 115
    //   134: aload_0
    //   135: aload #4
    //   137: iload_1
    //   138: aaload
    //   139: invokevirtual announce : (Ljava/lang/String;)V
    //   142: iload_2
    //   143: iconst_1
    //   144: iadd
    //   145: istore_2
    //   146: goto -> 129
  }
  
  public void announce(String paramString) {
    String str1 = getLocationURL(paramString);
    SSDPNotifySocket sSDPNotifySocket = new SSDPNotifySocket(paramString);
    SSDPNotifyRequest sSDPNotifyRequest = new SSDPNotifyRequest();
    sSDPNotifyRequest.setServer(UPnP.getServerName());
    sSDPNotifyRequest.setLeaseTime(getLeaseTime());
    sSDPNotifyRequest.setLocation(str1);
    sSDPNotifyRequest.setNTS("ssdp:alive");
    if (isRootDevice()) {
      str1 = getNotifyDeviceNT();
      String str = getNotifyDeviceUSN();
      sSDPNotifyRequest.setNT(str1);
      sSDPNotifyRequest.setUSN(str);
      sSDPNotifySocket.post(sSDPNotifyRequest);
      str1 = getUDN();
      sSDPNotifyRequest.setNT(str1);
      sSDPNotifyRequest.setUSN(str1);
      sSDPNotifySocket.post(sSDPNotifyRequest);
    } 
    str1 = getNotifyDeviceTypeNT();
    String str2 = getNotifyDeviceTypeUSN();
    sSDPNotifyRequest.setNT(str1);
    sSDPNotifyRequest.setUSN(str2);
    sSDPNotifySocket.post(sSDPNotifyRequest);
    sSDPNotifySocket.close();
    ServiceList serviceList = getServiceList();
    int j = serviceList.size();
    for (int i = 0;; i++) {
      DeviceList deviceList;
      if (i >= j) {
        deviceList = getDeviceList();
        j = deviceList.size();
        for (i = 0;; i++) {
          if (i >= j)
            return; 
          deviceList.getDevice(i).announce(paramString);
        } 
        break;
      } 
      deviceList.getService(i).announce(paramString);
    } 
  }
  
  public void byebye() {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial getDeviceData : ()Lorg/cybergarage/upnp/xml/DeviceData;
    //   4: invokevirtual getHTTPBindAddress : ()[Ljava/net/InetAddress;
    //   7: astore #5
    //   9: aload #5
    //   11: ifnull -> 59
    //   14: aload #5
    //   16: arraylength
    //   17: anewarray java/lang/String
    //   20: astore #4
    //   22: iconst_0
    //   23: istore_1
    //   24: iload_1
    //   25: aload #5
    //   27: arraylength
    //   28: if_icmplt -> 41
    //   31: iconst_0
    //   32: istore_1
    //   33: iload_1
    //   34: aload #4
    //   36: arraylength
    //   37: if_icmplt -> 95
    //   40: return
    //   41: aload #4
    //   43: iload_1
    //   44: aload #5
    //   46: iload_1
    //   47: aaload
    //   48: invokevirtual getHostAddress : ()Ljava/lang/String;
    //   51: aastore
    //   52: iload_1
    //   53: iconst_1
    //   54: iadd
    //   55: istore_1
    //   56: goto -> 24
    //   59: invokestatic getNHostAddresses : ()I
    //   62: istore_2
    //   63: iload_2
    //   64: anewarray java/lang/String
    //   67: astore #5
    //   69: iconst_0
    //   70: istore_1
    //   71: aload #5
    //   73: astore #4
    //   75: iload_1
    //   76: iload_2
    //   77: if_icmpge -> 31
    //   80: aload #5
    //   82: iload_1
    //   83: iload_1
    //   84: invokestatic getHostAddress : (I)Ljava/lang/String;
    //   87: aastore
    //   88: iload_1
    //   89: iconst_1
    //   90: iadd
    //   91: istore_1
    //   92: goto -> 71
    //   95: aload #4
    //   97: iload_1
    //   98: aaload
    //   99: ifnull -> 112
    //   102: aload #4
    //   104: iload_1
    //   105: aaload
    //   106: invokevirtual length : ()I
    //   109: ifgt -> 119
    //   112: iload_1
    //   113: iconst_1
    //   114: iadd
    //   115: istore_1
    //   116: goto -> 33
    //   119: aload_0
    //   120: invokevirtual getSSDPAnnounceCount : ()I
    //   123: istore_3
    //   124: iconst_0
    //   125: istore_2
    //   126: iload_2
    //   127: iload_3
    //   128: if_icmpge -> 112
    //   131: aload_0
    //   132: aload #4
    //   134: iload_1
    //   135: aaload
    //   136: invokevirtual byebye : (Ljava/lang/String;)V
    //   139: iload_2
    //   140: iconst_1
    //   141: iadd
    //   142: istore_2
    //   143: goto -> 126
  }
  
  public void byebye(String paramString) {
    SSDPNotifySocket sSDPNotifySocket = new SSDPNotifySocket(paramString);
    SSDPNotifyRequest sSDPNotifyRequest = new SSDPNotifyRequest();
    sSDPNotifyRequest.setNTS("ssdp:byebye");
    if (isRootDevice()) {
      String str3 = getNotifyDeviceNT();
      String str4 = getNotifyDeviceUSN();
      sSDPNotifyRequest.setNT(str3);
      sSDPNotifyRequest.setUSN(str4);
      sSDPNotifySocket.post(sSDPNotifyRequest);
    } 
    String str1 = getNotifyDeviceTypeNT();
    String str2 = getNotifyDeviceTypeUSN();
    sSDPNotifyRequest.setNT(str1);
    sSDPNotifyRequest.setUSN(str2);
    sSDPNotifySocket.post(sSDPNotifyRequest);
    sSDPNotifySocket.close();
    ServiceList serviceList = getServiceList();
    int j = serviceList.size();
    for (int i = 0;; i++) {
      DeviceList deviceList;
      if (i >= j) {
        deviceList = getDeviceList();
        j = deviceList.size();
        for (i = 0;; i++) {
          if (i >= j)
            return; 
          deviceList.getDevice(i).byebye(paramString);
        } 
        break;
      } 
      deviceList.getService(i).byebye(paramString);
    } 
  }
  
  public void deviceSearchReceived(SSDPPacket paramSSDPPacket) {
    deviceSearchResponse(paramSSDPPacket);
  }
  
  public void deviceSearchResponse(SSDPPacket paramSSDPPacket) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getST : ()Ljava/lang/String;
    //   4: astore #7
    //   6: aload #7
    //   8: ifnonnull -> 12
    //   11: return
    //   12: aload_0
    //   13: invokevirtual isRootDevice : ()Z
    //   16: istore #4
    //   18: aload_0
    //   19: invokevirtual getUDN : ()Ljava/lang/String;
    //   22: astore #6
    //   24: aload #6
    //   26: astore #5
    //   28: iload #4
    //   30: ifeq -> 56
    //   33: new java/lang/StringBuilder
    //   36: dup
    //   37: aload #6
    //   39: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   42: invokespecial <init> : (Ljava/lang/String;)V
    //   45: ldc_w '::upnp:rootdevice'
    //   48: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: invokevirtual toString : ()Ljava/lang/String;
    //   54: astore #5
    //   56: aload #7
    //   58: invokestatic isAllDevice : (Ljava/lang/String;)Z
    //   61: ifeq -> 161
    //   64: aload_0
    //   65: invokespecial getNotifyDeviceNT : ()Ljava/lang/String;
    //   68: astore #6
    //   70: iload #4
    //   72: ifeq -> 139
    //   75: iconst_3
    //   76: istore_2
    //   77: iconst_0
    //   78: istore_3
    //   79: iload_3
    //   80: iload_2
    //   81: if_icmplt -> 144
    //   84: aload_0
    //   85: invokevirtual getServiceList : ()Lorg/cybergarage/upnp/ServiceList;
    //   88: astore #5
    //   90: aload #5
    //   92: invokevirtual size : ()I
    //   95: istore_3
    //   96: iconst_0
    //   97: istore_2
    //   98: iload_2
    //   99: iload_3
    //   100: if_icmplt -> 287
    //   103: aload_0
    //   104: invokevirtual getDeviceList : ()Lorg/cybergarage/upnp/DeviceList;
    //   107: astore #5
    //   109: aload #5
    //   111: invokevirtual size : ()I
    //   114: istore_3
    //   115: iconst_0
    //   116: istore_2
    //   117: iload_2
    //   118: iload_3
    //   119: if_icmpge -> 11
    //   122: aload #5
    //   124: iload_2
    //   125: invokevirtual getDevice : (I)Lorg/cybergarage/upnp/Device;
    //   128: aload_1
    //   129: invokevirtual deviceSearchResponse : (Lorg/cybergarage/upnp/ssdp/SSDPPacket;)V
    //   132: iload_2
    //   133: iconst_1
    //   134: iadd
    //   135: istore_2
    //   136: goto -> 117
    //   139: iconst_2
    //   140: istore_2
    //   141: goto -> 77
    //   144: aload_0
    //   145: aload_1
    //   146: aload #6
    //   148: aload #5
    //   150: invokevirtual postSearchResponse : (Lorg/cybergarage/upnp/ssdp/SSDPPacket;Ljava/lang/String;Ljava/lang/String;)Z
    //   153: pop
    //   154: iload_3
    //   155: iconst_1
    //   156: iadd
    //   157: istore_3
    //   158: goto -> 79
    //   161: aload #7
    //   163: invokestatic isRootDevice : (Ljava/lang/String;)Z
    //   166: ifeq -> 187
    //   169: iload #4
    //   171: ifeq -> 84
    //   174: aload_0
    //   175: aload_1
    //   176: ldc 'upnp:rootdevice'
    //   178: aload #5
    //   180: invokevirtual postSearchResponse : (Lorg/cybergarage/upnp/ssdp/SSDPPacket;Ljava/lang/String;Ljava/lang/String;)Z
    //   183: pop
    //   184: goto -> 84
    //   187: aload #7
    //   189: invokestatic isUUIDDevice : (Ljava/lang/String;)Z
    //   192: ifeq -> 224
    //   195: aload_0
    //   196: invokevirtual getUDN : ()Ljava/lang/String;
    //   199: astore #6
    //   201: aload #7
    //   203: aload #6
    //   205: invokevirtual equals : (Ljava/lang/Object;)Z
    //   208: ifeq -> 84
    //   211: aload_0
    //   212: aload_1
    //   213: aload #6
    //   215: aload #5
    //   217: invokevirtual postSearchResponse : (Lorg/cybergarage/upnp/ssdp/SSDPPacket;Ljava/lang/String;Ljava/lang/String;)Z
    //   220: pop
    //   221: goto -> 84
    //   224: aload #7
    //   226: invokestatic isURNDevice : (Ljava/lang/String;)Z
    //   229: ifeq -> 84
    //   232: aload_0
    //   233: invokevirtual getDeviceType : ()Ljava/lang/String;
    //   236: astore #5
    //   238: aload #7
    //   240: aload #5
    //   242: invokevirtual equals : (Ljava/lang/Object;)Z
    //   245: ifeq -> 84
    //   248: aload_0
    //   249: aload_1
    //   250: aload #5
    //   252: new java/lang/StringBuilder
    //   255: dup
    //   256: aload_0
    //   257: invokevirtual getUDN : ()Ljava/lang/String;
    //   260: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   263: invokespecial <init> : (Ljava/lang/String;)V
    //   266: ldc_w '::'
    //   269: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   272: aload #5
    //   274: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   277: invokevirtual toString : ()Ljava/lang/String;
    //   280: invokevirtual postSearchResponse : (Lorg/cybergarage/upnp/ssdp/SSDPPacket;Ljava/lang/String;Ljava/lang/String;)Z
    //   283: pop
    //   284: goto -> 84
    //   287: aload #5
    //   289: iload_2
    //   290: invokevirtual getService : (I)Lorg/cybergarage/upnp/Service;
    //   293: aload_1
    //   294: invokevirtual serviceSearchResponse : (Lorg/cybergarage/upnp/ssdp/SSDPPacket;)Z
    //   297: pop
    //   298: iload_2
    //   299: iconst_1
    //   300: iadd
    //   301: istore_2
    //   302: goto -> 98
  }
  
  public String getAbsoluteURL(String paramString) {
    // Byte code:
    //   0: new java/net/URL
    //   3: dup
    //   4: aload_1
    //   5: invokespecial <init> : (Ljava/lang/String;)V
    //   8: invokevirtual toString : ()Ljava/lang/String;
    //   11: astore_2
    //   12: aload_2
    //   13: areturn
    //   14: astore_2
    //   15: aload_0
    //   16: invokevirtual getRootDevice : ()Lorg/cybergarage/upnp/Device;
    //   19: astore #4
    //   21: aload #4
    //   23: invokevirtual getURLBase : ()Ljava/lang/String;
    //   26: astore_3
    //   27: aload_3
    //   28: ifnull -> 40
    //   31: aload_3
    //   32: astore_2
    //   33: aload_3
    //   34: invokevirtual length : ()I
    //   37: ifgt -> 58
    //   40: aload #4
    //   42: invokevirtual getLocation : ()Ljava/lang/String;
    //   45: astore_2
    //   46: aload_2
    //   47: invokestatic getHost : (Ljava/lang/String;)Ljava/lang/String;
    //   50: aload_2
    //   51: invokestatic getPort : (Ljava/lang/String;)I
    //   54: invokestatic getRequestHostURL : (Ljava/lang/String;I)Ljava/lang/String;
    //   57: astore_2
    //   58: aload_1
    //   59: invokestatic toRelativeURL : (Ljava/lang/String;)Ljava/lang/String;
    //   62: astore_1
    //   63: new java/lang/StringBuilder
    //   66: dup
    //   67: aload_2
    //   68: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   71: invokespecial <init> : (Ljava/lang/String;)V
    //   74: aload_1
    //   75: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   78: invokevirtual toString : ()Ljava/lang/String;
    //   81: astore_3
    //   82: new java/net/URL
    //   85: dup
    //   86: aload_3
    //   87: invokespecial <init> : (Ljava/lang/String;)V
    //   90: invokevirtual toString : ()Ljava/lang/String;
    //   93: astore_3
    //   94: aload_3
    //   95: areturn
    //   96: astore_3
    //   97: aload_2
    //   98: aload_1
    //   99: invokestatic getAbsoluteURL : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   102: astore_1
    //   103: new java/net/URL
    //   106: dup
    //   107: aload_1
    //   108: invokespecial <init> : (Ljava/lang/String;)V
    //   111: invokevirtual toString : ()Ljava/lang/String;
    //   114: astore_1
    //   115: aload_1
    //   116: areturn
    //   117: astore_1
    //   118: ldc_w ''
    //   121: areturn
    // Exception table:
    //   from	to	target	type
    //   0	12	14	java/lang/Exception
    //   82	94	96	java/lang/Exception
    //   103	115	117	java/lang/Exception
  }
  
  public Action getAction(String paramString) {
    ServiceList serviceList = getServiceList();
    int j = serviceList.size();
    int i = 0;
    label24: while (true) {
      Action action;
      if (i >= j) {
        DeviceList deviceList = getDeviceList();
        int n = deviceList.size();
        i = 0;
        while (true) {
          if (i >= n)
            return null; 
          Action action1 = deviceList.getDevice(i).getAction(paramString);
          action = action1;
          if (action1 == null) {
            i++;
            continue;
          } 
          return action;
        } 
        break;
      } 
      ActionList actionList = action.getService(i).getActionList();
      int m = actionList.size();
      for (int k = 0;; k++) {
        if (k >= m) {
          i++;
          continue label24;
        } 
        Action action1 = actionList.getAction(k);
        String str = action1.getName();
        if (str != null && str.equals(paramString))
          return action1; 
      } 
      break;
    } 
  }
  
  public File getDescriptionFile() {
    return getDeviceData().getDescriptionFile();
  }
  
  public String getDescriptionFilePath() {
    File file = getDescriptionFile();
    return (file == null) ? "" : file.getAbsoluteFile().getParent();
  }
  
  public Device getDevice(String paramString) {
    DeviceList deviceList = getDeviceList();
    int j = deviceList.size();
    int i = 0;
    while (true) {
      if (i >= j)
        return null; 
      Device device2 = deviceList.getDevice(i);
      Device device1 = device2;
      if (!device2.isDevice(paramString)) {
        device1 = device2.getDevice(paramString);
        if (device1 != null)
          return device1; 
        i++;
        continue;
      } 
      return device1;
    } 
  }
  
  public Device getDeviceByDescriptionURI(String paramString) {
    DeviceList deviceList = getDeviceList();
    int j = deviceList.size();
    int i = 0;
    while (true) {
      if (i >= j)
        return null; 
      Device device2 = deviceList.getDevice(i);
      Device device1 = device2;
      if (!device2.isDescriptionURI(paramString)) {
        device1 = device2.getDeviceByDescriptionURI(paramString);
        if (device1 != null)
          return device1; 
        i++;
        continue;
      } 
      return device1;
    } 
  }
  
  public DeviceList getDeviceList() {
    DeviceList deviceList = new DeviceList();
    Node node = getDeviceNode().getNode("deviceList");
    if (node != null) {
      int j = node.getNNodes();
      int i = 0;
      while (true) {
        if (i < j) {
          Node node1 = node.getNode(i);
          if (isDeviceNode(node1))
            deviceList.add((E)new Device(node1)); 
          i++;
          continue;
        } 
        return deviceList;
      } 
    } 
    return deviceList;
  }
  
  public Node getDeviceNode() {
    return this.deviceNode;
  }
  
  public String getDeviceType() {
    return getDeviceNode().getNodeValue("deviceType");
  }
  
  public long getElapsedTime() {
    return (System.currentTimeMillis() - getTimeStamp()) / 1000L;
  }
  
  public String getFriendlyName() {
    return getDeviceNode().getNodeValue("friendlyName");
  }
  
  public InetAddress[] getHTTPBindAddress() {
    return getDeviceData().getHTTPBindAddress();
  }
  
  public int getHTTPPort() {
    return getDeviceData().getHTTPPort();
  }
  
  public Icon getIcon(int paramInt) {
    IconList iconList = getIconList();
    return (paramInt < 0 && iconList.size() - 1 < paramInt) ? null : iconList.getIcon(paramInt);
  }
  
  public IconList getIconList() {
    IconList iconList = new IconList();
    Node node = getDeviceNode().getNode("iconList");
    if (node != null) {
      int j = node.getNNodes();
      int i = 0;
      while (true) {
        if (i < j) {
          Node node1 = node.getNode(i);
          if (Icon.isIconNode(node1))
            iconList.add((E)new Icon(node1)); 
          i++;
          continue;
        } 
        return iconList;
      } 
    } 
    return iconList;
  }
  
  public String getInterfaceAddress() {
    SSDPPacket sSDPPacket = getSSDPPacket();
    return (sSDPPacket == null) ? "" : sSDPPacket.getLocalAddress();
  }
  
  public int getLeaseTime() {
    SSDPPacket sSDPPacket = getSSDPPacket();
    return (sSDPPacket != null) ? sSDPPacket.getLeaseTime() : getDeviceData().getLeaseTime();
  }
  
  public String getLocation() {
    SSDPPacket sSDPPacket = getSSDPPacket();
    return (sSDPPacket != null) ? sSDPPacket.getLocation() : getDeviceData().getLocation();
  }
  
  public String getLocationURL(String paramString) {
    return HostInterface.getHostURL(paramString, getHTTPPort(), getDescriptionURI());
  }
  
  public String getManufacture() {
    return getDeviceNode().getNodeValue("manufacturer");
  }
  
  public String getManufactureURL() {
    return getDeviceNode().getNodeValue("manufacturerURL");
  }
  
  public String getModelDescription() {
    return getDeviceNode().getNodeValue("modelDescription");
  }
  
  public String getModelName() {
    return getDeviceNode().getNodeValue("modelName");
  }
  
  public String getModelNumber() {
    return getDeviceNode().getNodeValue("modelNumber");
  }
  
  public String getModelURL() {
    return getDeviceNode().getNodeValue("modelURL");
  }
  
  public String getMulticastIPv4Address() {
    return getDeviceData().getMulticastIPv4Address();
  }
  
  public String getMulticastIPv6Address() {
    return getDeviceData().getMulticastIPv6Address();
  }
  
  public Device getParentDevice() {
    return isRootDevice() ? null : new Device(getDeviceNode().getParentNode().getParentNode());
  }
  
  public String getPresentationURL() {
    return getDeviceNode().getNodeValue("presentationURL");
  }
  
  public Device getRootDevice() {
    Node node = getRootNode();
    if (node != null) {
      Node node1 = node.getNode("device");
      if (node1 != null)
        return new Device(node, node1); 
    } 
    return null;
  }
  
  public Node getRootNode() {
    return (this.rootNode != null) ? this.rootNode : ((this.deviceNode == null) ? null : this.deviceNode.getRootNode());
  }
  
  public int getSSDPAnnounceCount() {
    return (isNMPRMode() && isWirelessMode()) ? 4 : 1;
  }
  
  public InetAddress[] getSSDPBindAddress() {
    return getDeviceData().getSSDPBindAddress();
  }
  
  public String getSSDPIPv4MulticastAddress() {
    return getDeviceData().getMulticastIPv4Address();
  }
  
  public void getSSDPIPv4MulticastAddress(String paramString) {
    getDeviceData().setMulticastIPv4Address(paramString);
  }
  
  public String getSSDPIPv6MulticastAddress() {
    return getDeviceData().getMulticastIPv6Address();
  }
  
  public void getSSDPIPv6MulticastAddress(String paramString) {
    getDeviceData().setMulticastIPv6Address(paramString);
  }
  
  public SSDPPacket getSSDPPacket() {
    return !isRootDevice() ? null : getDeviceData().getSSDPPacket();
  }
  
  public int getSSDPPort() {
    return getDeviceData().getSSDPPort();
  }
  
  public String getSerialNumber() {
    return getDeviceNode().getNodeValue("serialNumber");
  }
  
  public Service getService(String paramString) {
    ServiceList serviceList = getServiceList();
    int j = serviceList.size();
    int i = 0;
    while (true) {
      DeviceList deviceList;
      if (i >= j) {
        deviceList = getDeviceList();
        j = deviceList.size();
        i = 0;
        while (true) {
          if (i >= j)
            return null; 
          Service service4 = deviceList.getDevice(i).getService(paramString);
          Service service3 = service4;
          if (service4 == null) {
            i++;
            continue;
          } 
          return service3;
        } 
        break;
      } 
      Service service2 = deviceList.getService(i);
      Service service1 = service2;
      if (!service2.isService(paramString)) {
        i++;
        continue;
      } 
      continue;
    } 
  }
  
  public Service getServiceByControlURL(String paramString) {
    ServiceList serviceList = getServiceList();
    int j = serviceList.size();
    int i = 0;
    while (true) {
      DeviceList deviceList;
      if (i >= j) {
        deviceList = getDeviceList();
        j = deviceList.size();
        i = 0;
        while (true) {
          if (i >= j)
            return null; 
          Service service4 = deviceList.getDevice(i).getServiceByControlURL(paramString);
          Service service3 = service4;
          if (service4 == null) {
            i++;
            continue;
          } 
          return service3;
        } 
        break;
      } 
      Service service2 = deviceList.getService(i);
      Service service1 = service2;
      if (!service2.isControlURL(paramString)) {
        i++;
        continue;
      } 
      continue;
    } 
  }
  
  public Service getServiceByEventSubURL(String paramString) {
    ServiceList serviceList = getServiceList();
    int j = serviceList.size();
    int i = 0;
    while (true) {
      DeviceList deviceList;
      if (i >= j) {
        deviceList = getDeviceList();
        j = deviceList.size();
        i = 0;
        while (true) {
          if (i >= j)
            return null; 
          Service service4 = deviceList.getDevice(i).getServiceByEventSubURL(paramString);
          Service service3 = service4;
          if (service4 == null) {
            i++;
            continue;
          } 
          return service3;
        } 
        break;
      } 
      Service service2 = deviceList.getService(i);
      Service service1 = service2;
      if (!service2.isEventSubURL(paramString)) {
        i++;
        continue;
      } 
      continue;
    } 
  }
  
  public Service getServiceBySCPDURL(String paramString) {
    ServiceList serviceList = getServiceList();
    int j = serviceList.size();
    int i = 0;
    while (true) {
      DeviceList deviceList;
      if (i >= j) {
        deviceList = getDeviceList();
        j = deviceList.size();
        i = 0;
        while (true) {
          if (i >= j)
            return null; 
          Service service4 = deviceList.getDevice(i).getServiceBySCPDURL(paramString);
          Service service3 = service4;
          if (service4 == null) {
            i++;
            continue;
          } 
          return service3;
        } 
        break;
      } 
      Service service2 = deviceList.getService(i);
      Service service1 = service2;
      if (!service2.isSCPDURL(paramString)) {
        i++;
        continue;
      } 
      continue;
    } 
  }
  
  public ServiceList getServiceList() {
    ServiceList serviceList = new ServiceList();
    Node node = getDeviceNode().getNode("serviceList");
    if (node != null) {
      int j = node.getNNodes();
      int i = 0;
      while (true) {
        if (i < j) {
          Node node1 = node.getNode(i);
          if (Service.isServiceNode(node1))
            serviceList.add((E)new Service(node1)); 
          i++;
          continue;
        } 
        return serviceList;
      } 
    } 
    return serviceList;
  }
  
  public Icon getSmallestIcon() {
    Icon icon = null;
    IconList iconList = getIconList();
    int j = iconList.size();
    int i = 0;
    while (true) {
      Icon icon1;
      if (i >= j)
        return icon; 
      Icon icon2 = iconList.getIcon(i);
      if (icon == null) {
        icon1 = icon2;
      } else {
        icon1 = icon;
        if (icon2.getWidth() < icon.getWidth())
          icon1 = icon2; 
      } 
      i++;
      icon = icon1;
    } 
  }
  
  public StateVariable getStateVariable(String paramString) {
    return getStateVariable(null, paramString);
  }
  
  public StateVariable getStateVariable(String paramString1, String paramString2) {
    if (paramString1 == null && paramString2 == null)
      return null; 
    ServiceList serviceList = getServiceList();
    int j = serviceList.size();
    for (int i = 0;; i++) {
      StateVariable stateVariable;
      if (i >= j) {
        DeviceList deviceList = getDeviceList();
        j = deviceList.size();
        i = 0;
        while (true) {
          if (i >= j)
            return null; 
          StateVariable stateVariable1 = deviceList.getDevice(i).getStateVariable(paramString1, paramString2);
          stateVariable = stateVariable1;
          if (stateVariable1 == null) {
            i++;
            continue;
          } 
          return stateVariable;
        } 
        break;
      } 
      Service service = stateVariable.getService(i);
      if (paramString1 == null || service.getServiceType().equals(paramString1)) {
        StateVariable stateVariable1 = service.getStateVariable(paramString2);
        if (stateVariable1 != null)
          return stateVariable1; 
      } 
    } 
  }
  
  public Service getSubscriberService(String paramString) {
    ServiceList serviceList = getServiceList();
    int j = serviceList.size();
    int i = 0;
    while (true) {
      DeviceList deviceList;
      if (i >= j) {
        deviceList = getDeviceList();
        j = deviceList.size();
        i = 0;
        while (true) {
          if (i >= j)
            return null; 
          Service service4 = deviceList.getDevice(i).getSubscriberService(paramString);
          Service service3 = service4;
          if (service4 == null) {
            i++;
            continue;
          } 
          return service3;
        } 
        break;
      } 
      Service service2 = deviceList.getService(i);
      Service service1 = service2;
      if (!paramString.equals(service2.getSID())) {
        i++;
        continue;
      } 
      continue;
    } 
  }
  
  public long getTimeStamp() {
    SSDPPacket sSDPPacket = getSSDPPacket();
    return (sSDPPacket != null) ? sSDPPacket.getTimeStamp() : 0L;
  }
  
  public String getUDN() {
    return getDeviceNode().getNodeValue("UDN");
  }
  
  public String getUPC() {
    return getDeviceNode().getNodeValue("UPC");
  }
  
  public String getURLBase() {
    return isRootDevice() ? getRootNode().getNodeValue("URLBase") : "";
  }
  
  public Object getUserData() {
    return this.userData;
  }
  
  public boolean hasUDN() {
    String str = getUDN();
    return !(str == null || str.length() <= 0);
  }
  
  public void httpRequestRecieved(HTTPRequest paramHTTPRequest) {
    if (Debug.isOn())
      paramHTTPRequest.print(); 
    if (paramHTTPRequest.isGetRequest() || paramHTTPRequest.isHeadRequest()) {
      httpGetRequestRecieved(paramHTTPRequest);
      return;
    } 
    if (paramHTTPRequest.isPostRequest()) {
      httpPostRequestRecieved(paramHTTPRequest);
      return;
    } 
    if (paramHTTPRequest.isSubscribeRequest() || paramHTTPRequest.isUnsubscribeRequest()) {
      deviceEventSubscriptionRecieved(new SubscriptionRequest(paramHTTPRequest));
      return;
    } 
    paramHTTPRequest.returnBadRequest();
  }
  
  public boolean isDevice(String paramString) {
    if (paramString != null) {
      if (paramString.endsWith(getUDN()))
        return true; 
      if (paramString.equals(getFriendlyName()))
        return true; 
      if (paramString.endsWith(getDeviceType()))
        return true; 
    } 
    return false;
  }
  
  public boolean isDeviceType(String paramString) {
    return (paramString == null) ? false : paramString.equals(getDeviceType());
  }
  
  public boolean isExpired() {
    long l = getElapsedTime();
    return ((getLeaseTime() + 60) < l);
  }
  
  public boolean isNMPRMode() {
    Node node = getDeviceNode();
    return (node != null && node.getNode("INMPR03") != null);
  }
  
  public boolean isRootDevice() {
    return getRootNode().getNode("device").getNodeValue("UDN").equals(getUDN());
  }
  
  public boolean isRunning() {
    return (getAdvertiser() != null);
  }
  
  public boolean isWirelessMode() {
    return this.wirelessMode;
  }
  
  public boolean loadDescription(File paramFile) throws InvalidDescriptionException {
    try {
      this.rootNode = UPnP.getXMLParser().parse(paramFile);
      if (this.rootNode == null)
        throw new InvalidDescriptionException("Couldn't find a root node", paramFile); 
    } catch (ParserException parserException) {
      throw new InvalidDescriptionException(parserException);
    } 
    this.deviceNode = this.rootNode.getNode("device");
    if (this.deviceNode == null)
      throw new InvalidDescriptionException("Couldn't find a root device node", parserException); 
    if (!initializeLoadedDescription())
      return false; 
    setDescriptionFile((File)parserException);
    return true;
  }
  
  public boolean loadDescription(InputStream paramInputStream) throws InvalidDescriptionException {
    try {
      this.rootNode = UPnP.getXMLParser().parse(paramInputStream);
      if (this.rootNode == null)
        throw new InvalidDescriptionException("Couldn't find a root node"); 
    } catch (ParserException parserException) {
      throw new InvalidDescriptionException(parserException);
    } 
    this.deviceNode = this.rootNode.getNode("device");
    if (this.deviceNode == null)
      throw new InvalidDescriptionException("Couldn't find a root device node"); 
    if (!initializeLoadedDescription())
      return false; 
    setDescriptionFile(null);
    return true;
  }
  
  public boolean loadDescription(String paramString) throws InvalidDescriptionException {
    try {
      this.rootNode = UPnP.getXMLParser().parse(paramString);
      if (this.rootNode == null)
        throw new InvalidDescriptionException("Couldn't find a root node"); 
    } catch (ParserException parserException) {
      throw new InvalidDescriptionException(parserException);
    } 
    this.deviceNode = this.rootNode.getNode("device");
    if (this.deviceNode == null)
      throw new InvalidDescriptionException("Couldn't find a root device node"); 
    if (!initializeLoadedDescription())
      return false; 
    setDescriptionFile(null);
    return true;
  }
  
  public void lock() {
    this.mutex.lock();
  }
  
  public boolean postSearchResponse(SSDPPacket paramSSDPPacket, String paramString1, String paramString2) {
    String str1 = paramSSDPPacket.getLocalAddress();
    String str2 = getRootDevice().getLocationURL(str1);
    SSDPSearchResponse sSDPSearchResponse = new SSDPSearchResponse();
    sSDPSearchResponse.setLeaseTime(getLeaseTime());
    sSDPSearchResponse.setDate(cal);
    sSDPSearchResponse.setST(paramString1);
    sSDPSearchResponse.setUSN(paramString2);
    sSDPSearchResponse.setLocation(str2);
    sSDPSearchResponse.setMYNAME(getFriendlyName());
    TimerUtil.waitRandom(paramSSDPPacket.getMX() * 1000);
    paramString1 = paramSSDPPacket.getRemoteAddress();
    int j = paramSSDPPacket.getRemotePort();
    SSDPSearchResponseSocket sSDPSearchResponseSocket = new SSDPSearchResponseSocket();
    if (Debug.isOn())
      sSDPSearchResponse.print(); 
    int k = getSSDPAnnounceCount();
    int i;
    for (i = 0;; i++) {
      if (i >= k)
        return true; 
      sSDPSearchResponseSocket.post(paramString1, j, sSDPSearchResponse);
    } 
  }
  
  public void setActionListener(ActionListener paramActionListener) {
    ServiceList serviceList = getServiceList();
    int j = serviceList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      serviceList.getService(i).setActionListener(paramActionListener);
    } 
  }
  
  public void setActionListener(ActionListener paramActionListener, boolean paramBoolean) {
    setActionListener(paramActionListener);
    if (paramBoolean) {
      DeviceList deviceList = getDeviceList();
      int j = deviceList.size();
      int i = 0;
      while (true) {
        if (i < j) {
          deviceList.getDevice(i).setActionListener(paramActionListener, true);
          i++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public void setDeviceNode(Node paramNode) {
    this.deviceNode = paramNode;
  }
  
  public void setDeviceType(String paramString) {
    getDeviceNode().setNode("deviceType", paramString);
  }
  
  public void setFriendlyName(String paramString) {
    getDeviceNode().setNode("friendlyName", paramString);
  }
  
  public void setHTTPBindAddress(InetAddress[] paramArrayOfInetAddress) {
    getDeviceData().setHTTPBindAddress(paramArrayOfInetAddress);
  }
  
  public void setHTTPPort(int paramInt) {
    getDeviceData().setHTTPPort(paramInt);
  }
  
  public void setLeaseTime(int paramInt) {
    getDeviceData().setLeaseTime(paramInt);
    Advertiser advertiser = getAdvertiser();
    if (advertiser != null) {
      announce();
      advertiser.restart();
    } 
  }
  
  public void setLocation(String paramString) {
    getDeviceData().setLocation(paramString);
  }
  
  public void setManufacture(String paramString) {
    getDeviceNode().setNode("manufacturer", paramString);
  }
  
  public void setManufactureURL(String paramString) {
    getDeviceNode().setNode("manufacturerURL", paramString);
  }
  
  public void setModelDescription(String paramString) {
    getDeviceNode().setNode("modelDescription", paramString);
  }
  
  public void setModelName(String paramString) {
    getDeviceNode().setNode("modelName", paramString);
  }
  
  public void setModelNumber(String paramString) {
    getDeviceNode().setNode("modelNumber", paramString);
  }
  
  public void setModelURL(String paramString) {
    getDeviceNode().setNode("modelURL", paramString);
  }
  
  public void setMulticastIPv4Address(String paramString) {
    getDeviceData().setMulticastIPv4Address(paramString);
  }
  
  public void setMulticastIPv6Address(String paramString) {
    getDeviceData().setMulticastIPv6Address(paramString);
  }
  
  public void setNMPRMode(boolean paramBoolean) {
    Node node = getDeviceNode();
    if (node == null)
      return; 
    if (paramBoolean) {
      node.setNode("INMPR03", "1.0");
      node.removeNode("URLBase");
      return;
    } 
    node.removeNode("INMPR03");
  }
  
  public void setPresentationURL(String paramString) {
    getDeviceNode().setNode("presentationURL", paramString);
  }
  
  public void setQueryListener(QueryListener paramQueryListener) {
    ServiceList serviceList = getServiceList();
    int j = serviceList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      serviceList.getService(i).setQueryListener(paramQueryListener);
    } 
  }
  
  public void setQueryListener(QueryListener paramQueryListener, boolean paramBoolean) {
    setQueryListener(paramQueryListener);
    if (paramBoolean) {
      DeviceList deviceList = getDeviceList();
      int j = deviceList.size();
      int i = 0;
      while (true) {
        if (i < j) {
          deviceList.getDevice(i).setQueryListener(paramQueryListener, true);
          i++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public void setRootNode(Node paramNode) {
    this.rootNode = paramNode;
  }
  
  public void setSSDPBindAddress(InetAddress[] paramArrayOfInetAddress) {
    getDeviceData().setSSDPBindAddress(paramArrayOfInetAddress);
  }
  
  public void setSSDPPacket(SSDPPacket paramSSDPPacket) {
    getDeviceData().setSSDPPacket(paramSSDPPacket);
  }
  
  public void setSSDPPort(int paramInt) {
    getDeviceData().setSSDPPort(paramInt);
  }
  
  public void setSerialNumber(String paramString) {
    getDeviceNode().setNode("serialNumber", paramString);
  }
  
  public void setUDN(String paramString) {
    getDeviceNode().setNode("UDN", paramString);
  }
  
  public void setUPC(String paramString) {
    getDeviceNode().setNode("UPC", paramString);
  }
  
  public void setUserData(Object paramObject) {
    this.userData = paramObject;
  }
  
  public void setWirelessMode(boolean paramBoolean) {
    this.wirelessMode = paramBoolean;
  }
  
  public boolean start() {
    stop(true);
    int j = 0;
    int i = getHTTPPort();
    HTTPServerList hTTPServerList = getHTTPServerList();
    while (true) {
      if (hTTPServerList.open(i)) {
        hTTPServerList.addRequestListener(this);
        hTTPServerList.start();
        SSDPSearchSocketList sSDPSearchSocketList = getSSDPSearchSocketList();
        if (sSDPSearchSocketList.open()) {
          sSDPSearchSocketList.addSearchListener(this);
          sSDPSearchSocketList.start();
          announce();
          Advertiser advertiser = new Advertiser(this);
          setAdvertiser(advertiser);
          advertiser.start();
          return true;
        } 
        return false;
      } 
      if (100 >= ++j) {
        setHTTPPort(i + 1);
        i = getHTTPPort();
        continue;
      } 
      return false;
    } 
  }
  
  public boolean stop() {
    return stop(true);
  }
  
  public void unlock() {
    this.mutex.unlock();
  }
  
  static {
    UPnP.initialize();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\Device.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */