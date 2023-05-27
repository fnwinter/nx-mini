package org.cybergarage.upnp.ssdp;

import org.cybergarage.util.Debug;

public class SSDP {
  public static final String ADDRESS = "239.255.255.250";
  
  public static final int DEFAULT_MSEARCH_MX = 3;
  
  private static String IPV6_ADDRESS;
  
  public static final String IPV6_ADMINISTRATIVE_ADDRESS = "FF04::C";
  
  public static final String IPV6_GLOBAL_ADDRESS = "FF0E::C";
  
  public static final String IPV6_LINK_LOCAL_ADDRESS = "FF02::C";
  
  public static final String IPV6_SITE_LOCAL_ADDRESS = "FF05::C";
  
  public static final String IPV6_SUBNET_ADDRESS = "FF03::C";
  
  public static int PORT = 1900;
  
  public static final int RECV_MESSAGE_BUFSIZE = 1024;
  
  static {
    setIPv6Address("FF02::C");
  }
  
  public static final String getIPv6Address() {
    return IPV6_ADDRESS;
  }
  
  public static final int getLeaseTime(String paramString) {
    int i = 0;
    int j = paramString.indexOf("max-age");
    if (j >= 0) {
      int k = paramString.indexOf(',', j);
      i = k;
      if (k < 0)
        i = paramString.length(); 
      try {
        return Integer.parseInt(paramString.substring(paramString.indexOf("=", j) + 1, i).trim());
      } catch (Exception exception) {
        Debug.warning(exception);
        return 0;
      } 
    } 
    return i;
  }
  
  public static final int getSSDPPort() {
    return PORT;
  }
  
  public static final void setIPv6Address(String paramString) {
    IPV6_ADDRESS = paramString;
  }
  
  public static final void setSSDPPort(int paramInt) {
    PORT = paramInt;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ssdp\SSDP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */