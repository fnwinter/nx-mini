package org.cybergarage.upnp.ssdp;

import java.net.InetAddress;
import java.util.Vector;
import org.cybergarage.upnp.ControlPoint;

public class SSDPNotifySocketList extends Vector {
  private InetAddress[] binds = null;
  
  public SSDPNotifySocketList() {}
  
  public SSDPNotifySocketList(InetAddress[] paramArrayOfInetAddress) {
    this.binds = paramArrayOfInetAddress;
  }
  
  public void close() {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j) {
        clear();
        return;
      } 
      getSSDPNotifySocket(i).close();
    } 
  }
  
  public SSDPNotifySocket getSSDPNotifySocket(int paramInt) {
    return (SSDPNotifySocket)get(paramInt);
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
    //   92: ifnull -> 110
    //   95: aload_0
    //   96: new org/cybergarage/upnp/ssdp/SSDPNotifySocket
    //   99: dup
    //   100: aload_3
    //   101: iload_1
    //   102: aaload
    //   103: invokespecial <init> : (Ljava/lang/String;)V
    //   106: invokevirtual add : (Ljava/lang/Object;)Z
    //   109: pop
    //   110: iload_1
    //   111: iconst_1
    //   112: iadd
    //   113: istore_1
    //   114: goto -> 29
  }
  
  public void setControlPoint(ControlPoint paramControlPoint) {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      getSSDPNotifySocket(i).setControlPoint(paramControlPoint);
    } 
  }
  
  public void start() {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      getSSDPNotifySocket(i).start();
    } 
  }
  
  public void stop() {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      getSSDPNotifySocket(i).stop();
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ssdp\SSDPNotifySocketList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */