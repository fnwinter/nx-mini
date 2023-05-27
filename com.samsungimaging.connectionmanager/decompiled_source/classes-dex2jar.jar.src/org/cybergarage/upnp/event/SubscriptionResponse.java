package org.cybergarage.upnp.event;

import org.cybergarage.http.HTTPResponse;
import org.cybergarage.upnp.UPnP;

public class SubscriptionResponse extends HTTPResponse {
  public SubscriptionResponse() {
    setServer(UPnP.getServerName());
  }
  
  public SubscriptionResponse(HTTPResponse paramHTTPResponse) {
    super(paramHTTPResponse);
  }
  
  public String getSID() {
    return Subscription.getSID(getHeaderValue("SID"));
  }
  
  public long getTimeout() {
    return Subscription.getTimeout(getHeaderValue("TIMEOUT"));
  }
  
  public void setErrorResponse(int paramInt) {
    setStatusCode(paramInt);
    setContentLength(0L);
  }
  
  public void setResponse(int paramInt) {
    setStatusCode(paramInt);
    setContentLength(0L);
  }
  
  public void setSID(String paramString) {
    setHeader("SID", Subscription.toSIDHeaderString(paramString));
  }
  
  public void setTimeout(long paramLong) {
    setHeader("TIMEOUT", Subscription.toTimeoutHeaderString(paramLong));
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\event\SubscriptionResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */