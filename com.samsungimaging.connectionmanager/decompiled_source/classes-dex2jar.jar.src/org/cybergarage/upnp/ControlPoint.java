package org.cybergarage.upnp;

import com.samsungimaging.connectionmanager.util.Trace;
import java.net.InetAddress;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.http.HTTPRequestListener;
import org.cybergarage.http.HTTPServerList;
import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.control.RenewSubscriber;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.cybergarage.upnp.device.Disposer;
import org.cybergarage.upnp.device.NotifyListener;
import org.cybergarage.upnp.device.SearchResponseListener;
import org.cybergarage.upnp.device.USN;
import org.cybergarage.upnp.event.EventListener;
import org.cybergarage.upnp.event.NotifyRequest;
import org.cybergarage.upnp.event.Property;
import org.cybergarage.upnp.event.PropertyList;
import org.cybergarage.upnp.event.SubscriptionRequest;
import org.cybergarage.upnp.event.SubscriptionResponse;
import org.cybergarage.upnp.ssdp.SSDPNotifySocketList;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.upnp.ssdp.SSDPSearchRequest;
import org.cybergarage.upnp.ssdp.SSDPSearchResponseSocketList;
import org.cybergarage.util.Debug;
import org.cybergarage.util.ListenerList;
import org.cybergarage.util.Mutex;
import org.cybergarage.xml.Node;
import org.cybergarage.xml.NodeList;
import org.cybergarage.xml.ParserException;
import org.cybergarage.xml.ParserExceptionListener;

public class ControlPoint implements HTTPRequestListener {
  private static final int DEFAULT_EVENTSUB_PORT = 8059;
  
  private static final String DEFAULT_EVENTSUB_URI = "/evetSub";
  
  private static final int DEFAULT_EXPIRED_DEVICE_MONITORING_INTERVAL = 60;
  
  private static final int DEFAULT_SSDP_PORT = 8009;
  
  private static final int DEFAULT_UNSUBSCRIBE_TIMEOUT = 30000;
  
  private static String mAccessMethod;
  
  private static String mUserAgent = null;
  
  private Trace.Tag TAG = Trace.Tag.CYBERGARAGE;
  
  private NodeList devNodeList = new NodeList();
  
  ListenerList deviceChangeListenerList = new ListenerList();
  
  private Disposer deviceDisposer;
  
  private ListenerList deviceNotifyListenerList = new ListenerList();
  
  private ListenerList deviceSearchResponseListenerList = new ListenerList();
  
  private ListenerList eventListenerList = new ListenerList();
  
  private String eventSubURI = "/evetSub";
  
  private long expiredDeviceMonitoringInterval;
  
  private int httpPort = 0;
  
  private HTTPServerList httpServerList = new HTTPServerList();
  
  private Mutex mutex = new Mutex();
  
  private boolean nmprMode;
  
  ListenerList parserExceptionListenerList = new ListenerList();
  
  private RenewSubscriber renewSubscriber;
  
  private int searchMx = 3;
  
  private SSDPNotifySocketList ssdpNotifySocketList;
  
  private int ssdpPort = 0;
  
  private SSDPSearchResponseSocketList ssdpSearchResponseSocketList;
  
  private String strParserExceptionCause = "";
  
  private Object userData = null;
  
  static {
    mAccessMethod = null;
  }
  
  public ControlPoint() {
    this(8009, 8059);
  }
  
  public ControlPoint(int paramInt1, int paramInt2) {
    this(paramInt1, paramInt2, null);
  }
  
  public ControlPoint(int paramInt1, int paramInt2, InetAddress[] paramArrayOfInetAddress) {
    this.ssdpNotifySocketList = new SSDPNotifySocketList(paramArrayOfInetAddress);
    this.ssdpSearchResponseSocketList = new SSDPSearchResponseSocketList(paramArrayOfInetAddress);
    setSSDPPort(paramInt1);
    setHTTPPort(paramInt2);
    setDeviceDisposer(null);
    setExpiredDeviceMonitoringInterval(60L);
    setRenewSubscriber(null);
    setNMPRMode(true);
    setRenewSubscriber(null);
  }
  
  private void addDevice(SSDPPacket paramSSDPPacket) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_1
    //   3: invokevirtual isRootDevice : ()Z
    //   6: ifeq -> 35
    //   9: aload_0
    //   10: getfield strParserExceptionCause : Ljava/lang/String;
    //   13: ldc 'HTTP_UNAUTHORIZED'
    //   15: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   18: ifne -> 35
    //   21: aload_0
    //   22: getfield strParserExceptionCause : Ljava/lang/String;
    //   25: ldc 'HTTP_UNAVAILABLE'
    //   27: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   30: istore_2
    //   31: iload_2
    //   32: ifeq -> 38
    //   35: aload_0
    //   36: monitorexit
    //   37: return
    //   38: aload_0
    //   39: aload_1
    //   40: invokevirtual getUSN : ()Ljava/lang/String;
    //   43: invokestatic getUDN : (Ljava/lang/String;)Ljava/lang/String;
    //   46: invokevirtual getDevice : (Ljava/lang/String;)Lorg/cybergarage/upnp/Device;
    //   49: astore_3
    //   50: aload_3
    //   51: ifnull -> 67
    //   54: aload_3
    //   55: aload_1
    //   56: invokevirtual setSSDPPacket : (Lorg/cybergarage/upnp/ssdp/SSDPPacket;)V
    //   59: goto -> 35
    //   62: astore_1
    //   63: aload_0
    //   64: monitorexit
    //   65: aload_1
    //   66: athrow
    //   67: aload_1
    //   68: invokevirtual getLocation : ()Ljava/lang/String;
    //   71: astore_3
    //   72: new java/net/URL
    //   75: dup
    //   76: aload_3
    //   77: invokespecial <init> : (Ljava/lang/String;)V
    //   80: astore_3
    //   81: invokestatic getXMLParser : ()Lorg/cybergarage/xml/Parser;
    //   84: aload_3
    //   85: getstatic org/cybergarage/upnp/ControlPoint.mUserAgent : Ljava/lang/String;
    //   88: getstatic org/cybergarage/upnp/ControlPoint.mAccessMethod : Ljava/lang/String;
    //   91: invokevirtual parse : (Ljava/net/URL;Ljava/lang/String;Ljava/lang/String;)Lorg/cybergarage/xml/Node;
    //   94: astore_3
    //   95: aload_0
    //   96: aload_3
    //   97: invokespecial getDevice : (Lorg/cybergarage/xml/Node;)Lorg/cybergarage/upnp/Device;
    //   100: astore #4
    //   102: aload #4
    //   104: ifnull -> 35
    //   107: aload #4
    //   109: aload_1
    //   110: invokevirtual setSSDPPacket : (Lorg/cybergarage/upnp/ssdp/SSDPPacket;)V
    //   113: aload_0
    //   114: aload_3
    //   115: invokespecial addDevice : (Lorg/cybergarage/xml/Node;)V
    //   118: aload_0
    //   119: aload #4
    //   121: invokevirtual performAddDeviceListener : (Lorg/cybergarage/upnp/Device;)V
    //   124: goto -> 35
    //   127: astore_3
    //   128: aload_1
    //   129: invokevirtual toString : ()Ljava/lang/String;
    //   132: invokestatic warning : (Ljava/lang/String;)V
    //   135: aload_3
    //   136: invokestatic warning : (Ljava/lang/Exception;)V
    //   139: goto -> 35
    //   142: astore_3
    //   143: aload_1
    //   144: invokevirtual toString : ()Ljava/lang/String;
    //   147: invokestatic warning : (Ljava/lang/String;)V
    //   150: aload_3
    //   151: invokestatic warning : (Ljava/lang/Exception;)V
    //   154: aload_0
    //   155: aload_3
    //   156: invokevirtual getMessage : ()Ljava/lang/String;
    //   159: putfield strParserExceptionCause : Ljava/lang/String;
    //   162: aload_0
    //   163: aload_3
    //   164: invokevirtual performParserExceptionListener : (Lorg/cybergarage/xml/ParserException;)V
    //   167: goto -> 35
    // Exception table:
    //   from	to	target	type
    //   2	31	62	finally
    //   38	50	62	finally
    //   54	59	62	finally
    //   67	72	62	finally
    //   72	102	127	java/net/MalformedURLException
    //   72	102	142	org/cybergarage/xml/ParserException
    //   72	102	62	finally
    //   107	124	127	java/net/MalformedURLException
    //   107	124	142	org/cybergarage/xml/ParserException
    //   107	124	62	finally
    //   128	139	62	finally
    //   143	167	62	finally
  }
  
  private void addDevice(Node paramNode) {
    this.devNodeList.add(paramNode);
  }
  
  private Device getDevice(Node paramNode) {
    if (paramNode != null) {
      Node node = paramNode.getNode("device");
      if (node != null)
        return new Device(paramNode, node); 
    } 
    return null;
  }
  
  private String getEventSubCallbackURL(String paramString) {
    return HostInterface.getHostURL(paramString, getHTTPPort(), getEventSubURI());
  }
  
  private HTTPServerList getHTTPServerList() {
    return this.httpServerList;
  }
  
  private SSDPNotifySocketList getSSDPNotifySocketList() {
    return this.ssdpNotifySocketList;
  }
  
  private SSDPSearchResponseSocketList getSSDPSearchResponseSocketList() {
    return this.ssdpSearchResponseSocketList;
  }
  
  private void removeDevice(SSDPPacket paramSSDPPacket) {
    if (!paramSSDPPacket.isByeBye())
      return; 
    removeDevice(USN.getUDN(paramSSDPPacket.getUSN()));
  }
  
  private void removeDevice(Node paramNode) {
    Device device = getDevice(paramNode);
    if (device != null && device.isRootDevice())
      performRemoveDeviceListener(device); 
    this.devNodeList.remove(paramNode);
  }
  
  public void addDeviceChangeListener(DeviceChangeListener paramDeviceChangeListener) {
    this.deviceChangeListenerList.add(paramDeviceChangeListener);
  }
  
  public void addEventListener(EventListener paramEventListener) {
    this.eventListenerList.add(paramEventListener);
  }
  
  public void addNotifyListener(NotifyListener paramNotifyListener) {
    this.deviceNotifyListenerList.add(paramNotifyListener);
  }
  
  public void addParserExceptionListener(ParserExceptionListener paramParserExceptionListener) {
    this.parserExceptionListenerList.add(paramParserExceptionListener);
  }
  
  public void addSearchResponseListener(SearchResponseListener paramSearchResponseListener) {
    this.deviceSearchResponseListenerList.add(paramSearchResponseListener);
  }
  
  public void finalize() {
    stop();
  }
  
  public Device getDevice(String paramString) {
    int j = this.devNodeList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return null; 
      Device device = getDevice(this.devNodeList.getNode(i));
      if (device != null) {
        Device device1 = device;
        if (!device.isDevice(paramString)) {
          device1 = device.getDevice(paramString);
          if (device1 != null)
            return device1; 
        } else {
          return device1;
        } 
      } 
    } 
  }
  
  public Disposer getDeviceDisposer() {
    return this.deviceDisposer;
  }
  
  public DeviceList getDeviceList() {
    DeviceList deviceList = new DeviceList();
    int j = this.devNodeList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return deviceList; 
      Device device = getDevice(this.devNodeList.getNode(i));
      if (device != null)
        deviceList.add((E)device); 
    } 
  }
  
  public String getEventSubURI() {
    return this.eventSubURI;
  }
  
  public long getExpiredDeviceMonitoringInterval() {
    return this.expiredDeviceMonitoringInterval;
  }
  
  public int getHTTPPort() {
    return this.httpPort;
  }
  
  public RenewSubscriber getRenewSubscriber() {
    return this.renewSubscriber;
  }
  
  public int getSSDPPort() {
    return this.ssdpPort;
  }
  
  public int getSearchMx() {
    return this.searchMx;
  }
  
  public Service getSubscriberService(String paramString) {
    DeviceList deviceList = getDeviceList();
    int j = deviceList.size();
    int i = 0;
    while (true) {
      if (i >= j)
        return null; 
      Service service2 = deviceList.getDevice(i).getSubscriberService(paramString);
      Service service1 = service2;
      if (service2 == null) {
        i++;
        continue;
      } 
      return service1;
    } 
  }
  
  public Object getUserData() {
    return this.userData;
  }
  
  public boolean hasDevice(String paramString) {
    return (getDevice(paramString) != null);
  }
  
  public void httpRequestRecieved(HTTPRequest paramHTTPRequest) {
    if (Debug.isOn())
      paramHTTPRequest.print(); 
    if (paramHTTPRequest.isNotifyRequest()) {
      NotifyRequest notifyRequest = new NotifyRequest(paramHTTPRequest);
      String str = notifyRequest.getSID();
      long l = notifyRequest.getSEQ();
      PropertyList propertyList = notifyRequest.getPropertyList();
      int j = propertyList.size();
      for (int i = 0;; i++) {
        if (i >= j) {
          paramHTTPRequest.returnOK();
          return;
        } 
        Property property = propertyList.getProperty(i);
        performEventListener(str, l, property.getName(), property.getValue());
      } 
    } 
    paramHTTPRequest.returnBadRequest();
  }
  
  public boolean isNMPRMode() {
    return this.nmprMode;
  }
  
  public boolean isSubscribed(Service paramService) {
    return (paramService == null) ? false : paramService.isSubscribed();
  }
  
  public void lock() {
    this.mutex.lock();
  }
  
  public void notifyReceived(SSDPPacket paramSSDPPacket) {
    Trace.d(this.TAG, "Performance Check Point : start notifyReceived() isRootDevice : " + paramSSDPPacket.isRootDevice());
    if (paramSSDPPacket.isRootDevice()) {
      Trace.d(this.TAG, "isAlive : " + paramSSDPPacket.isAlive());
      if (paramSSDPPacket.isAlive()) {
        addDevice(paramSSDPPacket);
      } else if (paramSSDPPacket.isByeBye()) {
        removeDevice(paramSSDPPacket);
      } 
    } 
    performNotifyListener(paramSSDPPacket);
  }
  
  public void performAddDeviceListener(Device paramDevice) {
    int j = this.deviceChangeListenerList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      ((DeviceChangeListener)this.deviceChangeListenerList.get(i)).deviceAdded(paramDevice);
    } 
  }
  
  public void performEventListener(String paramString1, long paramLong, String paramString2, String paramString3) {
    int j = this.eventListenerList.size();
    int i;
    for (i = 0;; i++) {
      if (i >= j)
        return; 
      ((EventListener)this.eventListenerList.get(i)).eventNotifyReceived(paramString1, paramLong, paramString2, paramString3);
    } 
  }
  
  public void performNotifyListener(SSDPPacket paramSSDPPacket) {
    int j = this.deviceNotifyListenerList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      NotifyListener notifyListener = (NotifyListener)this.deviceNotifyListenerList.get(i);
      try {
        notifyListener.deviceNotifyReceived(paramSSDPPacket);
      } catch (Exception exception) {
        Debug.warning("NotifyListener returned an error:", exception);
      } 
    } 
  }
  
  public void performParserExceptionListener(ParserException paramParserException) {
    int j = this.parserExceptionListenerList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      ((ParserExceptionListener)this.parserExceptionListenerList.get(i)).occuredParserException(paramParserException);
    } 
  }
  
  public void performRemoveDeviceListener(Device paramDevice) {
    int j = this.deviceChangeListenerList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      ((DeviceChangeListener)this.deviceChangeListenerList.get(i)).deviceRemoved(paramDevice);
    } 
  }
  
  public void performSearchResponseListener(SSDPPacket paramSSDPPacket) {
    int j = this.deviceSearchResponseListenerList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      SearchResponseListener searchResponseListener = (SearchResponseListener)this.deviceSearchResponseListenerList.get(i);
      try {
        searchResponseListener.deviceSearchResponseReceived(paramSSDPPacket);
      } catch (Exception exception) {
        Debug.warning("SearchResponseListener returned an error:", exception);
      } 
    } 
  }
  
  public void print() {
    DeviceList deviceList = getDeviceList();
    int j = deviceList.size();
    Debug.message("Device Num = " + j);
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      Device device = deviceList.getDevice(i);
      Debug.message("[" + i + "] " + device.getFriendlyName() + ", " + device.getLeaseTime() + ", " + device.getElapsedTime());
    } 
  }
  
  protected void removeDevice(String paramString) {
    removeDevice(getDevice(paramString));
  }
  
  protected void removeDevice(Device paramDevice) {
    if (paramDevice == null)
      return; 
    removeDevice(paramDevice.getRootNode());
  }
  
  public void removeDeviceChangeListener(DeviceChangeListener paramDeviceChangeListener) {
    this.deviceChangeListenerList.remove(paramDeviceChangeListener);
  }
  
  public void removeEventListener(EventListener paramEventListener) {
    this.eventListenerList.remove(paramEventListener);
  }
  
  public void removeExpiredDevices() {
    DeviceList deviceList = getDeviceList();
    int j = deviceList.size();
    Device[] arrayOfDevice = new Device[j];
    for (int i = 0;; i++) {
      if (i >= j) {
        for (i = 0;; i++) {
          if (i >= j)
            return; 
          if (arrayOfDevice[i].isExpired()) {
            Debug.message("Expired device = " + arrayOfDevice[i].getFriendlyName());
            removeDevice(arrayOfDevice[i]);
          } 
        } 
        break;
      } 
      arrayOfDevice[i] = deviceList.getDevice(i);
    } 
  }
  
  public void removeNotifyListener(NotifyListener paramNotifyListener) {
    this.deviceNotifyListenerList.remove(paramNotifyListener);
  }
  
  public void removeParserExceptionListener(ParserExceptionListener paramParserExceptionListener) {
    this.parserExceptionListenerList.remove(paramParserExceptionListener);
  }
  
  public void removeSearchResponseListener(SearchResponseListener paramSearchResponseListener) {
    this.deviceSearchResponseListenerList.remove(paramSearchResponseListener);
  }
  
  public void renewSubscriberService() {
    renewSubscriberService(-1L);
  }
  
  public void renewSubscriberService(long paramLong) {
    DeviceList deviceList = getDeviceList();
    int j = deviceList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      renewSubscriberService(deviceList.getDevice(i), paramLong);
    } 
  }
  
  public void renewSubscriberService(Device paramDevice, long paramLong) {
    ServiceList serviceList = paramDevice.getServiceList();
    int j = serviceList.size();
    int i;
    for (i = 0;; i++) {
      if (i >= j) {
        DeviceList deviceList = paramDevice.getDeviceList();
        j = deviceList.size();
        for (i = 0;; i++) {
          if (i >= j)
            return; 
          renewSubscriberService(deviceList.getDevice(i), paramLong);
        } 
        break;
      } 
      Service service = serviceList.getService(i);
      if (service.isSubscribed() && !subscribe(service, service.getSID(), paramLong))
        subscribe(service, paramLong); 
    } 
  }
  
  public void search() {
    search("upnp:rootdevice", 3);
  }
  
  public void search(String paramString) {
    search(paramString, 3);
  }
  
  public void search(String paramString, int paramInt) {
    SSDPSearchRequest sSDPSearchRequest = new SSDPSearchRequest(paramString, paramInt);
    getSSDPSearchResponseSocketList().post(sSDPSearchRequest);
  }
  
  public void searchResponseReceived(SSDPPacket paramSSDPPacket) {
    Trace.d(this.TAG, "Performance Check Point : start searchResponseReceived() isRootDevice : " + paramSSDPPacket.isRootDevice());
    if (paramSSDPPacket.isRootDevice())
      addDevice(paramSSDPPacket); 
    performSearchResponseListener(paramSSDPPacket);
  }
  
  public void setDeviceDisposer(Disposer paramDisposer) {
    this.deviceDisposer = paramDisposer;
  }
  
  public void setEventSubURI(String paramString) {
    this.eventSubURI = paramString;
  }
  
  public void setExpiredDeviceMonitoringInterval(long paramLong) {
    this.expiredDeviceMonitoringInterval = paramLong;
  }
  
  public void setHTTPPort(int paramInt) {
    this.httpPort = paramInt;
  }
  
  public void setNMPRMode(boolean paramBoolean) {
    this.nmprMode = paramBoolean;
  }
  
  public void setRenewSubscriber(RenewSubscriber paramRenewSubscriber) {
    this.renewSubscriber = paramRenewSubscriber;
  }
  
  public void setSSDPPort(int paramInt) {
    this.ssdpPort = paramInt;
  }
  
  public void setSearchMx(int paramInt) {
    this.searchMx = paramInt;
  }
  
  public void setUserData(Object paramObject) {
    this.userData = paramObject;
  }
  
  public boolean start() {
    return start("upnp:rootdevice", 3, null, null);
  }
  
  public boolean start(String paramString1, int paramInt, String paramString2, String paramString3) {
    // Byte code:
    //   0: aload_3
    //   1: putstatic org/cybergarage/upnp/ControlPoint.mUserAgent : Ljava/lang/String;
    //   4: aload #4
    //   6: putstatic org/cybergarage/upnp/ControlPoint.mAccessMethod : Ljava/lang/String;
    //   9: aload_0
    //   10: invokevirtual stop : ()Z
    //   13: pop
    //   14: iconst_0
    //   15: istore #6
    //   17: aload_0
    //   18: invokevirtual getHTTPPort : ()I
    //   21: istore #5
    //   23: aload_0
    //   24: invokespecial getHTTPServerList : ()Lorg/cybergarage/http/HTTPServerList;
    //   27: astore_3
    //   28: aload_3
    //   29: iload #5
    //   31: invokevirtual open : (I)Z
    //   34: ifeq -> 60
    //   37: aload_3
    //   38: aload_0
    //   39: invokevirtual addRequestListener : (Lorg/cybergarage/http/HTTPRequestListener;)V
    //   42: aload_3
    //   43: invokevirtual start : ()V
    //   46: aload_0
    //   47: invokespecial getSSDPNotifySocketList : ()Lorg/cybergarage/upnp/ssdp/SSDPNotifySocketList;
    //   50: astore_3
    //   51: aload_3
    //   52: invokevirtual open : ()Z
    //   55: ifne -> 92
    //   58: iconst_0
    //   59: ireturn
    //   60: iload #6
    //   62: iconst_1
    //   63: iadd
    //   64: istore #6
    //   66: bipush #100
    //   68: iload #6
    //   70: if_icmpge -> 75
    //   73: iconst_0
    //   74: ireturn
    //   75: aload_0
    //   76: iload #5
    //   78: iconst_1
    //   79: iadd
    //   80: invokevirtual setHTTPPort : (I)V
    //   83: aload_0
    //   84: invokevirtual getHTTPPort : ()I
    //   87: istore #5
    //   89: goto -> 28
    //   92: aload_3
    //   93: aload_0
    //   94: invokevirtual setControlPoint : (Lorg/cybergarage/upnp/ControlPoint;)V
    //   97: aload_3
    //   98: invokevirtual start : ()V
    //   101: aload_0
    //   102: invokevirtual getSSDPPort : ()I
    //   105: istore #5
    //   107: iconst_0
    //   108: istore #6
    //   110: aload_0
    //   111: invokespecial getSSDPSearchResponseSocketList : ()Lorg/cybergarage/upnp/ssdp/SSDPSearchResponseSocketList;
    //   114: astore_3
    //   115: aload_3
    //   116: iload #5
    //   118: invokevirtual open : (I)Z
    //   121: ifeq -> 184
    //   124: aload_3
    //   125: aload_0
    //   126: invokevirtual setControlPoint : (Lorg/cybergarage/upnp/ControlPoint;)V
    //   129: aload_3
    //   130: invokevirtual start : ()V
    //   133: aload_0
    //   134: aload_1
    //   135: iload_2
    //   136: invokevirtual search : (Ljava/lang/String;I)V
    //   139: new org/cybergarage/upnp/device/Disposer
    //   142: dup
    //   143: aload_0
    //   144: invokespecial <init> : (Lorg/cybergarage/upnp/ControlPoint;)V
    //   147: astore_1
    //   148: aload_0
    //   149: aload_1
    //   150: invokevirtual setDeviceDisposer : (Lorg/cybergarage/upnp/device/Disposer;)V
    //   153: aload_1
    //   154: invokevirtual start : ()V
    //   157: aload_0
    //   158: invokevirtual isNMPRMode : ()Z
    //   161: ifeq -> 182
    //   164: new org/cybergarage/upnp/control/RenewSubscriber
    //   167: dup
    //   168: aload_0
    //   169: invokespecial <init> : (Lorg/cybergarage/upnp/ControlPoint;)V
    //   172: astore_1
    //   173: aload_0
    //   174: aload_1
    //   175: invokevirtual setRenewSubscriber : (Lorg/cybergarage/upnp/control/RenewSubscriber;)V
    //   178: aload_1
    //   179: invokevirtual start : ()V
    //   182: iconst_1
    //   183: ireturn
    //   184: iload #6
    //   186: iconst_1
    //   187: iadd
    //   188: istore #6
    //   190: bipush #100
    //   192: iload #6
    //   194: if_icmpge -> 199
    //   197: iconst_0
    //   198: ireturn
    //   199: aload_0
    //   200: iload #5
    //   202: iconst_1
    //   203: iadd
    //   204: invokevirtual setSSDPPort : (I)V
    //   207: aload_0
    //   208: invokevirtual getSSDPPort : ()I
    //   211: istore #5
    //   213: goto -> 115
    //   216: astore_1
    //   217: iconst_0
    //   218: ireturn
    // Exception table:
    //   from	to	target	type
    //   129	133	216	org/cybergarage/upnp/SocketAddressException
  }
  
  public boolean start(String paramString1, String paramString2, String paramString3) {
    return start(paramString1, 3, paramString2, paramString3);
  }
  
  public boolean stop() {
    unsubscribe();
    SSDPNotifySocketList sSDPNotifySocketList = getSSDPNotifySocketList();
    sSDPNotifySocketList.stop();
    sSDPNotifySocketList.close();
    sSDPNotifySocketList.clear();
    SSDPSearchResponseSocketList sSDPSearchResponseSocketList = getSSDPSearchResponseSocketList();
    sSDPSearchResponseSocketList.stop();
    sSDPSearchResponseSocketList.close();
    sSDPSearchResponseSocketList.clear();
    HTTPServerList hTTPServerList = getHTTPServerList();
    hTTPServerList.stop();
    hTTPServerList.close();
    hTTPServerList.clear();
    Disposer disposer = getDeviceDisposer();
    if (disposer != null) {
      disposer.stop();
      setDeviceDisposer(null);
    } 
    RenewSubscriber renewSubscriber = getRenewSubscriber();
    if (renewSubscriber != null) {
      renewSubscriber.stop();
      setRenewSubscriber(null);
    } 
    return true;
  }
  
  public boolean subscribe(Service paramService) {
    return subscribe(paramService, -1L);
  }
  
  public boolean subscribe(Service paramService, long paramLong) {
    boolean bool = false;
    if (paramService.isSubscribed())
      return subscribe(paramService, paramService.getSID(), paramLong); 
    Device device = paramService.getRootDevice();
    if (device != null) {
      String str = device.getInterfaceAddress();
      SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
      subscriptionRequest.setSubscribeRequest(paramService, getEventSubCallbackURL(str), paramLong);
      SubscriptionResponse subscriptionResponse = subscriptionRequest.post();
      if (subscriptionResponse.isSuccessful()) {
        paramService.setSID(subscriptionResponse.getSID());
        paramService.setTimeout(subscriptionResponse.getTimeout());
        return true;
      } 
      paramService.clearSID();
      return false;
    } 
    return bool;
  }
  
  public boolean subscribe(Service paramService, String paramString) {
    return subscribe(paramService, paramString, -1L);
  }
  
  public boolean subscribe(Service paramService, String paramString, long paramLong) {
    SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
    subscriptionRequest.setRenewRequest(paramService, paramString, paramLong);
    if (Debug.isOn())
      subscriptionRequest.print(); 
    SubscriptionResponse subscriptionResponse = subscriptionRequest.post();
    if (Debug.isOn())
      subscriptionResponse.print(); 
    if (subscriptionResponse.isSuccessful()) {
      paramService.setSID(subscriptionResponse.getSID());
      paramService.setTimeout(subscriptionResponse.getTimeout());
      return true;
    } 
    paramService.clearSID();
    return false;
  }
  
  public void unlock() {
    this.mutex.unlock();
  }
  
  public void unsubscribe() {
    DeviceList deviceList = getDeviceList();
    int j = deviceList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      unsubscribe(deviceList.getDevice(i));
    } 
  }
  
  public void unsubscribe(Device paramDevice) {
    Trace.d(this.TAG, "start unsubscribe(device)");
    ServiceList serviceList = paramDevice.getServiceList();
    int j = serviceList.size();
    for (int i = 0;; i++) {
      if (i >= j) {
        DeviceList deviceList = paramDevice.getDeviceList();
        j = deviceList.size();
        for (i = 0;; i++) {
          if (i >= j)
            return; 
          unsubscribe(deviceList.getDevice(i));
        } 
        break;
      } 
      Service service = serviceList.getService(i);
      if (service.hasSID())
        unsubscribe(service); 
    } 
  }
  
  public boolean unsubscribe(Service paramService) {
    Trace.d(this.TAG, "start unsubscribe(service)");
    SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
    subscriptionRequest.setUnsubscribeRequest(paramService);
    if (subscriptionRequest.post(30000).isSuccessful()) {
      paramService.clearSID();
      return true;
    } 
    return false;
  }
  
  static {
    UPnP.initialize();
    UPnP.setEnable(9);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ControlPoint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */