package org.cybergarage.upnp.ssdp;

import java.net.InetAddress;
import java.util.Vector;
import org.cybergarage.upnp.device.SearchListener;

public class SSDPSearchSocketList extends Vector {
  private InetAddress[] binds = null;
  
  private String multicastIPv4 = "239.255.255.250";
  
  private String multicastIPv6 = SSDP.getIPv6Address();
  
  private int port = SSDP.PORT;
  
  public SSDPSearchSocketList() {}
  
  public SSDPSearchSocketList(InetAddress[] paramArrayOfInetAddress) {
    this.binds = paramArrayOfInetAddress;
  }
  
  public SSDPSearchSocketList(InetAddress[] paramArrayOfInetAddress, int paramInt, String paramString1, String paramString2) {
    this.binds = paramArrayOfInetAddress;
    this.port = paramInt;
    this.multicastIPv4 = paramString1;
    this.multicastIPv6 = paramString2;
  }
  
  public void addSearchListener(SearchListener paramSearchListener) {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      getSSDPSearchSocket(i).addSearchListener(paramSearchListener);
    } 
  }
  
  public void close() {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j) {
        clear();
        return;
      } 
      getSSDPSearchSocket(i).close();
    } 
  }
  
  public SSDPSearchSocket getSSDPSearchSocket(int paramInt) {
    return (SSDPSearchSocket)get(paramInt);
  }
  
  public boolean open() {
    // Byte code:
    //   0: aload_0
    //   1: getfield binds : [Ljava/net/InetAddress;
    //   4: astore #4
    //   6: aload #4
    //   8: ifnull -> 54
    //   11: aload #4
    //   13: arraylength
    //   14: anewarray java/lang/String
    //   17: astore_3
    //   18: iconst_0
    //   19: istore_1
    //   20: iload_1
    //   21: aload #4
    //   23: arraylength
    //   24: if_icmplt -> 37
    //   27: iconst_0
    //   28: istore_1
    //   29: iload_1
    //   30: aload_3
    //   31: arraylength
    //   32: if_icmplt -> 89
    //   35: iconst_1
    //   36: ireturn
    //   37: aload_3
    //   38: iload_1
    //   39: aload #4
    //   41: iload_1
    //   42: aaload
    //   43: invokevirtual getHostAddress : ()Ljava/lang/String;
    //   46: aastore
    //   47: iload_1
    //   48: iconst_1
    //   49: iadd
    //   50: istore_1
    //   51: goto -> 20
    //   54: invokestatic getNHostAddresses : ()I
    //   57: istore_2
    //   58: iload_2
    //   59: anewarray java/lang/String
    //   62: astore #4
    //   64: iconst_0
    //   65: istore_1
    //   66: aload #4
    //   68: astore_3
    //   69: iload_1
    //   70: iload_2
    //   71: if_icmpge -> 27
    //   74: aload #4
    //   76: iload_1
    //   77: iload_1
    //   78: invokestatic getHostAddress : (I)Ljava/lang/String;
    //   81: aastore
    //   82: iload_1
    //   83: iconst_1
    //   84: iadd
    //   85: istore_1
    //   86: goto -> 66
    //   89: aload_3
    //   90: iload_1
    //   91: aaload
    //   92: ifnull -> 131
    //   95: aload_3
    //   96: iload_1
    //   97: aaload
    //   98: invokestatic isIPv6Address : (Ljava/lang/String;)Z
    //   101: ifeq -> 138
    //   104: new org/cybergarage/upnp/ssdp/SSDPSearchSocket
    //   107: dup
    //   108: aload_3
    //   109: iload_1
    //   110: aaload
    //   111: aload_0
    //   112: getfield port : I
    //   115: aload_0
    //   116: getfield multicastIPv6 : Ljava/lang/String;
    //   119: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
    //   122: astore #4
    //   124: aload_0
    //   125: aload #4
    //   127: invokevirtual add : (Ljava/lang/Object;)Z
    //   130: pop
    //   131: iload_1
    //   132: iconst_1
    //   133: iadd
    //   134: istore_1
    //   135: goto -> 29
    //   138: new org/cybergarage/upnp/ssdp/SSDPSearchSocket
    //   141: dup
    //   142: aload_3
    //   143: iload_1
    //   144: aaload
    //   145: aload_0
    //   146: getfield port : I
    //   149: aload_0
    //   150: getfield multicastIPv4 : Ljava/lang/String;
    //   153: invokespecial <init> : (Ljava/lang/String;ILjava/lang/String;)V
    //   156: astore #4
    //   158: goto -> 124
  }
  
  public void start() {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      getSSDPSearchSocket(i).start();
    } 
  }
  
  public void stop() {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      getSSDPSearchSocket(i).stop();
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ssdp\SSDPSearchSocketList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */