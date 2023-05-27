package org.cybergarage.upnp.device;

public class NTS {
  public static final String ALIVE = "ssdp:alive";
  
  public static final String BYEBYE = "ssdp:byebye";
  
  public static final String PROPCHANGE = "upnp:propchange";
  
  public static final boolean isAlive(String paramString) {
    return (paramString == null) ? false : paramString.startsWith("ssdp:alive");
  }
  
  public static final boolean isByeBye(String paramString) {
    return (paramString == null) ? false : paramString.startsWith("ssdp:byebye");
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\device\NTS.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */