package org.cybergarage.upnp.device;

public class USN {
  public static final String ROOTDEVICE = "upnp:rootdevice";
  
  public static final String getUDN(String paramString) {
    if (paramString == null)
      return ""; 
    int i = paramString.indexOf("::");
    return (i < 0) ? paramString.trim() : (new String(paramString.getBytes(), 0, i)).trim();
  }
  
  public static final boolean isRootDevice(String paramString) {
    return (paramString == null) ? false : paramString.endsWith("upnp:rootdevice");
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\device\USN.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */