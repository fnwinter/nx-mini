package org.cybergarage.upnp.control;

import org.cybergarage.http.HTTPRequest;
import org.cybergarage.soap.SOAPRequest;
import org.cybergarage.upnp.Service;

public class ControlRequest extends SOAPRequest {
  public ControlRequest() {}
  
  public ControlRequest(HTTPRequest paramHTTPRequest) {
    set(paramHTTPRequest);
  }
  
  public boolean isActionControl() {
    return !isQueryControl();
  }
  
  public boolean isQueryControl() {
    return isSOAPAction("urn:schemas-upnp-org:control-1-0#QueryStateVariable");
  }
  
  protected void setRequestHost(Service paramService) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual getControlURL : ()Ljava/lang/String;
    //   4: astore #4
    //   6: aload_1
    //   7: invokevirtual getRootDevice : ()Lorg/cybergarage/upnp/Device;
    //   10: invokevirtual getURLBase : ()Ljava/lang/String;
    //   13: astore #5
    //   15: aload #4
    //   17: astore_3
    //   18: aload #5
    //   20: ifnull -> 101
    //   23: aload #4
    //   25: astore_3
    //   26: aload #5
    //   28: invokevirtual length : ()I
    //   31: ifle -> 101
    //   34: new java/net/URL
    //   37: dup
    //   38: aload #5
    //   40: invokespecial <init> : (Ljava/lang/String;)V
    //   43: invokevirtual getPath : ()Ljava/lang/String;
    //   46: astore #5
    //   48: aload #5
    //   50: invokevirtual length : ()I
    //   53: istore_2
    //   54: aload #4
    //   56: astore_3
    //   57: iload_2
    //   58: ifle -> 101
    //   61: iconst_1
    //   62: iload_2
    //   63: if_icmplt -> 80
    //   66: aload #4
    //   68: astore_3
    //   69: aload #5
    //   71: iconst_0
    //   72: invokevirtual charAt : (I)C
    //   75: bipush #47
    //   77: if_icmpeq -> 101
    //   80: new java/lang/StringBuilder
    //   83: dup
    //   84: aload #5
    //   86: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   89: invokespecial <init> : (Ljava/lang/String;)V
    //   92: aload #4
    //   94: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   97: invokevirtual toString : ()Ljava/lang/String;
    //   100: astore_3
    //   101: aload_0
    //   102: aload_3
    //   103: iconst_1
    //   104: invokevirtual setURI : (Ljava/lang/String;Z)V
    //   107: ldc ''
    //   109: astore #4
    //   111: aload_3
    //   112: invokestatic isAbsoluteURL : (Ljava/lang/String;)Z
    //   115: ifeq -> 121
    //   118: aload_3
    //   119: astore #4
    //   121: aload #4
    //   123: ifnull -> 137
    //   126: aload #4
    //   128: astore_3
    //   129: aload #4
    //   131: invokevirtual length : ()I
    //   134: ifgt -> 145
    //   137: aload_1
    //   138: invokevirtual getRootDevice : ()Lorg/cybergarage/upnp/Device;
    //   141: invokevirtual getURLBase : ()Ljava/lang/String;
    //   144: astore_3
    //   145: aload_3
    //   146: ifnull -> 159
    //   149: aload_3
    //   150: astore #4
    //   152: aload_3
    //   153: invokevirtual length : ()I
    //   156: ifgt -> 168
    //   159: aload_1
    //   160: invokevirtual getRootDevice : ()Lorg/cybergarage/upnp/Device;
    //   163: invokevirtual getLocation : ()Ljava/lang/String;
    //   166: astore #4
    //   168: aload #4
    //   170: invokestatic getHost : (Ljava/lang/String;)Ljava/lang/String;
    //   173: astore_1
    //   174: aload #4
    //   176: invokestatic getPort : (Ljava/lang/String;)I
    //   179: istore_2
    //   180: aload_0
    //   181: aload_1
    //   182: iload_2
    //   183: invokevirtual setHost : (Ljava/lang/String;I)V
    //   186: aload_0
    //   187: aload_1
    //   188: invokevirtual setRequestHost : (Ljava/lang/String;)V
    //   191: aload_0
    //   192: iload_2
    //   193: invokevirtual setRequestPort : (I)V
    //   196: return
    //   197: astore_3
    //   198: aload #4
    //   200: astore_3
    //   201: goto -> 101
    // Exception table:
    //   from	to	target	type
    //   34	54	197	java/net/MalformedURLException
    //   69	80	197	java/net/MalformedURLException
    //   80	101	197	java/net/MalformedURLException
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\control\ControlRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */