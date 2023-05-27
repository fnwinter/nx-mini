package org.cybergarage.upnp.event;

import org.cybergarage.upnp.UPnP;

public class Subscription {
  public static final String INFINITE_STRING = "infinite";
  
  public static final int INFINITE_VALUE = -1;
  
  public static final String SUBSCRIBE_METHOD = "SUBSCRIBE";
  
  public static final String TIMEOUT_HEADER = "Second-";
  
  public static final String UNSUBSCRIBE_METHOD = "UNSUBSCRIBE";
  
  public static final String UUID = "uuid:";
  
  public static final String XMLNS = "urn:schemas-upnp-org:event-1-0";
  
  public static final String createSID() {
    return UPnP.createUUID();
  }
  
  public static final String getSID(String paramString) {
    if (paramString == null)
      return ""; 
    String str = paramString;
    return paramString.startsWith("uuid:") ? paramString.substring("uuid:".length(), paramString.length()) : str;
  }
  
  public static final long getTimeout(String paramString) {
    int i = paramString.indexOf('-');
    try {
      return Long.parseLong(paramString.substring(i + 1, paramString.length()));
    } catch (Exception exception) {
      return -1L;
    } 
  }
  
  public static final String toSIDHeaderString(String paramString) {
    return "uuid:" + paramString;
  }
  
  public static final String toTimeoutHeaderString(long paramLong) {
    return (paramLong == -1L) ? "infinite" : ("Second-" + Long.toString(paramLong));
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\event\Subscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */