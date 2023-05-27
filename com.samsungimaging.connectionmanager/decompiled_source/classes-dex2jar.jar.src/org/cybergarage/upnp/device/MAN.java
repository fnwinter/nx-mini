package org.cybergarage.upnp.device;

public class MAN {
  public static final String DISCOVER = "ssdp:discover";
  
  public static final boolean isDiscover(String paramString) {
    return (paramString == null) ? false : (paramString.equals("ssdp:discover") ? true : paramString.equals("\"ssdp:discover\""));
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\device\MAN.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */