package org.cybergarage.upnp.device;

public class NT {
  public static final String EVENT = "upnp:event";
  
  public static final String ROOTDEVICE = "upnp:rootdevice";
  
  public static final boolean isRootDevice(String paramString) {
    return (paramString == null) ? false : paramString.startsWith("upnp:rootdevice");
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\device\NT.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */