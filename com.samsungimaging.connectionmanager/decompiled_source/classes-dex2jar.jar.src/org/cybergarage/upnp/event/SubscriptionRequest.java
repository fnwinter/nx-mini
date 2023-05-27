package org.cybergarage.upnp.event;

import org.cybergarage.http.HTTPRequest;
import org.cybergarage.upnp.Service;

public class SubscriptionRequest extends HTTPRequest {
  private static final String CALLBACK_END_WITH = ">";
  
  private static final String CALLBACK_START_WITH = "<";
  
  public SubscriptionRequest() {
    setContentLength(0L);
  }
  
  public SubscriptionRequest(HTTPRequest paramHTTPRequest) {
    this();
    set(paramHTTPRequest);
  }
  
  private void setService(Service paramService) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getEventSubURL : ()Ljava/lang/String;
    //   4: astore #5
    //   6: aload_0
    //   7: aload #5
    //   9: iconst_1
    //   10: invokevirtual setURI : (Ljava/lang/String;Z)V
    //   13: ldc ''
    //   15: astore #4
    //   17: aload_1
    //   18: invokevirtual getDevice : ()Lorg/cybergarage/upnp/Device;
    //   21: astore_3
    //   22: aload_3
    //   23: ifnull -> 32
    //   26: aload_3
    //   27: invokevirtual getURLBase : ()Ljava/lang/String;
    //   30: astore #4
    //   32: aload #4
    //   34: ifnull -> 48
    //   37: aload #4
    //   39: astore_3
    //   40: aload #4
    //   42: invokevirtual length : ()I
    //   45: ifgt -> 68
    //   48: aload_1
    //   49: invokevirtual getRootDevice : ()Lorg/cybergarage/upnp/Device;
    //   52: astore #6
    //   54: aload #4
    //   56: astore_3
    //   57: aload #6
    //   59: ifnull -> 68
    //   62: aload #6
    //   64: invokevirtual getURLBase : ()Ljava/lang/String;
    //   67: astore_3
    //   68: aload_3
    //   69: ifnull -> 82
    //   72: aload_3
    //   73: astore #4
    //   75: aload_3
    //   76: invokevirtual length : ()I
    //   79: ifgt -> 100
    //   82: aload_1
    //   83: invokevirtual getRootDevice : ()Lorg/cybergarage/upnp/Device;
    //   86: astore_1
    //   87: aload_3
    //   88: astore #4
    //   90: aload_1
    //   91: ifnull -> 100
    //   94: aload_1
    //   95: invokevirtual getLocation : ()Ljava/lang/String;
    //   98: astore #4
    //   100: aload #4
    //   102: ifnull -> 116
    //   105: aload #4
    //   107: astore_1
    //   108: aload #4
    //   110: invokevirtual length : ()I
    //   113: ifgt -> 130
    //   116: aload #4
    //   118: astore_1
    //   119: aload #5
    //   121: invokestatic isAbsoluteURL : (Ljava/lang/String;)Z
    //   124: ifeq -> 130
    //   127: aload #5
    //   129: astore_1
    //   130: aload_1
    //   131: invokestatic getHost : (Ljava/lang/String;)Ljava/lang/String;
    //   134: astore_3
    //   135: aload_1
    //   136: invokestatic getPort : (Ljava/lang/String;)I
    //   139: istore_2
    //   140: aload_0
    //   141: aload_3
    //   142: iload_2
    //   143: invokevirtual setHost : (Ljava/lang/String;I)V
    //   146: aload_0
    //   147: aload_3
    //   148: invokevirtual setRequestHost : (Ljava/lang/String;)V
    //   151: aload_0
    //   152: iload_2
    //   153: invokevirtual setRequestPort : (I)V
    //   156: return
  }
  
  public String getCallback() {
    return getStringHeaderValue("CALLBACK", "<", ">");
  }
  
  public String getNT() {
    return getHeaderValue("NT");
  }
  
  public String getSID() {
    String str2 = Subscription.getSID(getHeaderValue("SID"));
    String str1 = str2;
    if (str2 == null)
      str1 = ""; 
    return str1;
  }
  
  public long getTimeout() {
    return Subscription.getTimeout(getHeaderValue("TIMEOUT"));
  }
  
  public boolean hasCallback() {
    String str = getCallback();
    return (str != null && str.length() > 0);
  }
  
  public boolean hasNT() {
    String str = getNT();
    return (str != null && str.length() > 0);
  }
  
  public boolean hasSID() {
    String str = getSID();
    return (str != null && str.length() > 0);
  }
  
  public SubscriptionResponse post() {
    return new SubscriptionResponse(post(getRequestHost(), getRequestPort()));
  }
  
  public SubscriptionResponse post(int paramInt) {
    return new SubscriptionResponse(post(getRequestHost(), getRequestPort(), paramInt));
  }
  
  public void post(SubscriptionResponse paramSubscriptionResponse) {
    post(paramSubscriptionResponse);
  }
  
  public void setCallback(String paramString) {
    setStringHeader("CALLBACK", paramString, "<", ">");
  }
  
  public void setNT(String paramString) {
    setHeader("NT", paramString);
  }
  
  public void setRenewRequest(Service paramService, String paramString, long paramLong) {
    setMethod("SUBSCRIBE");
    setService(paramService);
    setSID(paramString);
    setTimeout(paramLong);
  }
  
  public void setSID(String paramString) {
    setHeader("SID", Subscription.toSIDHeaderString(paramString));
  }
  
  public void setSubscribeRequest(Service paramService, String paramString, long paramLong) {
    setMethod("SUBSCRIBE");
    setService(paramService);
    setCallback(paramString);
    setNT("upnp:event");
    setTimeout(paramLong);
  }
  
  public final void setTimeout(long paramLong) {
    setHeader("TIMEOUT", Subscription.toTimeoutHeaderString(paramLong));
  }
  
  public void setUnsubscribeRequest(Service paramService) {
    setMethod("UNSUBSCRIBE");
    setService(paramService);
    setSID(paramService.getSID());
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\event\SubscriptionRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */