package org.cybergarage.upnp.device;

public class ST {
  public static final String ALL_DEVICE = "ssdp:all";
  
  public static final String ROOT_DEVICE = "upnp:rootdevice";
  
  public static final String URN_DEVICE = "urn:schemas-upnp-org:device:";
  
  public static final String URN_SERVICE = "urn:schemas-upnp-org:service:";
  
  public static final String UUID_DEVICE = "uuid";
  
  public static final boolean isAllDevice(String paramString) {
    return (paramString == null) ? false : (paramString.equals("ssdp:all") ? true : paramString.equals("\"ssdp:all\""));
  }
  
  public static final boolean isRootDevice(String paramString) {
    return (paramString == null) ? false : (paramString.equals("upnp:rootdevice") ? true : paramString.equals("\"upnp:rootdevice\""));
  }
  
  public static final boolean isURNDevice(String paramString) {
    return (paramString == null) ? false : (paramString.startsWith("urn:schemas-upnp-org:device:") ? true : paramString.startsWith("\"urn:schemas-upnp-org:device:"));
  }
  
  public static final boolean isURNService(String paramString) {
    return (paramString == null) ? false : (paramString.startsWith("urn:schemas-upnp-org:service:") ? true : paramString.startsWith("\"urn:schemas-upnp-org:service:"));
  }
  
  public static final boolean isUUIDDevice(String paramString) {
    return (paramString == null) ? false : (paramString.startsWith("uuid") ? true : paramString.startsWith("\"uuid"));
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\device\ST.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */