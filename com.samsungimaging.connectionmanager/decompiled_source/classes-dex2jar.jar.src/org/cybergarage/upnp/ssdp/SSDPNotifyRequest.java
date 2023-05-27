package org.cybergarage.upnp.ssdp;

public class SSDPNotifyRequest extends SSDPRequest {
  public SSDPNotifyRequest() {
    setMethod("NOTIFY");
    setURI("*");
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ssdp\SSDPNotifyRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */