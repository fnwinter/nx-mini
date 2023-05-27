package org.cybergarage.upnp.ssdp;

import java.net.InetAddress;
import java.util.Vector;
import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.SocketAddressException;

public class SSDPSearchResponseSocketList extends Vector {
  private InetAddress[] binds = null;
  
  public SSDPSearchResponseSocketList() {}
  
  public SSDPSearchResponseSocketList(InetAddress[] paramArrayOfInetAddress) {
    this.binds = paramArrayOfInetAddress;
  }
  
  public void close() {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j) {
        clear();
        return;
      } 
      getSSDPSearchResponseSocket(i).close();
    } 
  }
  
  public SSDPSearchResponseSocket getSSDPSearchResponseSocket(int paramInt) {
    return (SSDPSearchResponseSocket)get(paramInt);
  }
  
  public boolean open() {
    return open(SSDP.PORT);
  }
  
  public boolean open(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield binds : [Ljava/net/InetAddress;
    //   4: astore #5
    //   6: aload #5
    //   8: ifnull -> 59
    //   11: aload #5
    //   13: arraylength
    //   14: anewarray java/lang/String
    //   17: astore #4
    //   19: iconst_0
    //   20: istore_2
    //   21: iload_2
    //   22: aload #5
    //   24: arraylength
    //   25: if_icmplt -> 41
    //   28: iconst_0
    //   29: istore_2
    //   30: aload #4
    //   32: arraylength
    //   33: istore_3
    //   34: iload_2
    //   35: iload_3
    //   36: if_icmplt -> 95
    //   39: iconst_1
    //   40: ireturn
    //   41: aload #4
    //   43: iload_2
    //   44: aload #5
    //   46: iload_2
    //   47: aaload
    //   48: invokevirtual getHostAddress : ()Ljava/lang/String;
    //   51: aastore
    //   52: iload_2
    //   53: iconst_1
    //   54: iadd
    //   55: istore_2
    //   56: goto -> 21
    //   59: invokestatic getNHostAddresses : ()I
    //   62: istore_3
    //   63: iload_3
    //   64: anewarray java/lang/String
    //   67: astore #5
    //   69: iconst_0
    //   70: istore_2
    //   71: aload #5
    //   73: astore #4
    //   75: iload_2
    //   76: iload_3
    //   77: if_icmpge -> 28
    //   80: aload #5
    //   82: iload_2
    //   83: iload_2
    //   84: invokestatic getHostAddress : (I)Ljava/lang/String;
    //   87: aastore
    //   88: iload_2
    //   89: iconst_1
    //   90: iadd
    //   91: istore_2
    //   92: goto -> 71
    //   95: aload #4
    //   97: iload_2
    //   98: aaload
    //   99: invokestatic isIPv4Address : (Ljava/lang/String;)Z
    //   102: ifeq -> 160
    //   105: aload #4
    //   107: iload_2
    //   108: aaload
    //   109: astore #5
    //   111: iload_1
    //   112: iconst_1
    //   113: iadd
    //   114: istore_3
    //   115: aload_0
    //   116: new org/cybergarage/upnp/ssdp/SSDPSearchResponseSocket
    //   119: dup
    //   120: aload #5
    //   122: iload_1
    //   123: invokespecial <init> : (Ljava/lang/String;I)V
    //   126: invokevirtual add : (Ljava/lang/Object;)Z
    //   129: pop
    //   130: iload_3
    //   131: istore_1
    //   132: iload_2
    //   133: iconst_1
    //   134: iadd
    //   135: istore_2
    //   136: goto -> 30
    //   139: astore #4
    //   141: aload_0
    //   142: invokevirtual stop : ()V
    //   145: aload_0
    //   146: invokevirtual close : ()V
    //   149: aload_0
    //   150: invokevirtual clear : ()V
    //   153: iconst_0
    //   154: ireturn
    //   155: astore #4
    //   157: goto -> 141
    //   160: goto -> 132
    // Exception table:
    //   from	to	target	type
    //   30	34	139	java/lang/Exception
    //   95	105	139	java/lang/Exception
    //   115	130	155	java/lang/Exception
  }
  
  public boolean post(SSDPSearchRequest paramSSDPSearchRequest) {
    boolean bool = true;
    int j = size();
    int i = 0;
    while (true) {
      if (i >= j)
        return bool; 
      SSDPSearchResponseSocket sSDPSearchResponseSocket = getSSDPSearchResponseSocket(i);
      boolean bool1 = bool;
      if (sSDPSearchResponseSocket != null) {
        String str2 = sSDPSearchResponseSocket.getLocalAddress();
        paramSSDPSearchRequest.setLocalAddress(str2);
        String str1 = "239.255.255.250";
        if (HostInterface.isIPv6Address(str2))
          str1 = SSDP.getIPv6Address(); 
        bool1 = bool;
        if (!sSDPSearchResponseSocket.post(str1, SSDP.PORT, paramSSDPSearchRequest))
          bool1 = false; 
      } 
      i++;
      bool = bool1;
    } 
  }
  
  public void setControlPoint(ControlPoint paramControlPoint) {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      getSSDPSearchResponseSocket(i).setControlPoint(paramControlPoint);
    } 
  }
  
  public void start() throws SocketAddressException {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      getSSDPSearchResponseSocket(i).start();
    } 
  }
  
  public void stop() {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      getSSDPSearchResponseSocket(i).stop();
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ssdp\SSDPSearchResponseSocketList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */