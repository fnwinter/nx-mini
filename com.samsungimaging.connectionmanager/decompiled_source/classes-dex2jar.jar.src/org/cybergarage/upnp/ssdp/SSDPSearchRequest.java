package org.cybergarage.upnp.ssdp;

import org.cybergarage.net.HostInterface;

public class SSDPSearchRequest extends SSDPRequest {
  public SSDPSearchRequest() {
    this("upnp:rootdevice");
  }
  
  public SSDPSearchRequest(String paramString) {
    this(paramString, 3);
  }
  
  public SSDPSearchRequest(String paramString, int paramInt) {
    setMethod("M-SEARCH");
    setURI("*");
    setHeader("ST", paramString);
    setHeader("MX", Integer.toString(paramInt));
    setHeader("MAN", "\"ssdp:discover\"");
  }
  
  public void setLocalAddress(String paramString) {
    String str = "239.255.255.250";
    if (HostInterface.isIPv6Address(paramString))
      str = SSDP.getIPv6Address(); 
    setHost(str, SSDP.PORT);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ssdp\SSDPSearchRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */