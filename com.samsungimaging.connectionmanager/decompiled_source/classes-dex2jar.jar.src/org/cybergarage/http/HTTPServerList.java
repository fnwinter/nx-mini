package org.cybergarage.http;

import java.net.InetAddress;
import java.util.Vector;

public class HTTPServerList extends Vector {
  private InetAddress[] binds = null;
  
  private int port = 4004;
  
  public HTTPServerList() {}
  
  public HTTPServerList(InetAddress[] paramArrayOfInetAddress, int paramInt) {
    this.binds = paramArrayOfInetAddress;
    this.port = paramInt;
  }
  
  public void addRequestListener(HTTPRequestListener paramHTTPRequestListener) {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      getHTTPServer(i).addRequestListener(paramHTTPRequestListener);
    } 
  }
  
  public void close() {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      getHTTPServer(i).close();
    } 
  }
  
  public HTTPServer getHTTPServer(int paramInt) {
    return (HTTPServer)get(paramInt);
  }
  
  public int open() {
    // Byte code:
    //   0: aload_0
    //   1: getfield binds : [Ljava/net/InetAddress;
    //   4: astore #4
    //   6: aload #4
    //   8: ifnull -> 56
    //   11: aload #4
    //   13: arraylength
    //   14: anewarray java/lang/String
    //   17: astore_3
    //   18: iconst_0
    //   19: istore_1
    //   20: iload_1
    //   21: aload #4
    //   23: arraylength
    //   24: if_icmplt -> 39
    //   27: iconst_0
    //   28: istore_2
    //   29: iconst_0
    //   30: istore_1
    //   31: iload_1
    //   32: aload_3
    //   33: arraylength
    //   34: if_icmplt -> 91
    //   37: iload_2
    //   38: ireturn
    //   39: aload_3
    //   40: iload_1
    //   41: aload #4
    //   43: iload_1
    //   44: aaload
    //   45: invokevirtual getHostAddress : ()Ljava/lang/String;
    //   48: aastore
    //   49: iload_1
    //   50: iconst_1
    //   51: iadd
    //   52: istore_1
    //   53: goto -> 20
    //   56: invokestatic getNHostAddresses : ()I
    //   59: istore_2
    //   60: iload_2
    //   61: anewarray java/lang/String
    //   64: astore #4
    //   66: iconst_0
    //   67: istore_1
    //   68: aload #4
    //   70: astore_3
    //   71: iload_1
    //   72: iload_2
    //   73: if_icmpge -> 27
    //   76: aload #4
    //   78: iload_1
    //   79: iload_1
    //   80: invokestatic getHostAddress : (I)Ljava/lang/String;
    //   83: aastore
    //   84: iload_1
    //   85: iconst_1
    //   86: iadd
    //   87: istore_1
    //   88: goto -> 68
    //   91: new org/cybergarage/http/HTTPServer
    //   94: dup
    //   95: invokespecial <init> : ()V
    //   98: astore #4
    //   100: aload_3
    //   101: iload_1
    //   102: aaload
    //   103: ifnull -> 121
    //   106: aload #4
    //   108: aload_3
    //   109: iload_1
    //   110: aaload
    //   111: aload_0
    //   112: getfield port : I
    //   115: invokevirtual open : (Ljava/lang/String;I)Z
    //   118: ifne -> 136
    //   121: aload_0
    //   122: invokevirtual close : ()V
    //   125: aload_0
    //   126: invokevirtual clear : ()V
    //   129: iload_1
    //   130: iconst_1
    //   131: iadd
    //   132: istore_1
    //   133: goto -> 31
    //   136: aload_0
    //   137: aload #4
    //   139: invokevirtual add : (Ljava/lang/Object;)Z
    //   142: pop
    //   143: iload_2
    //   144: iconst_1
    //   145: iadd
    //   146: istore_2
    //   147: goto -> 129
  }
  
  public boolean open(int paramInt) {
    this.port = paramInt;
    return (open() != 0);
  }
  
  public void removeRequestListener(HTTPRequestListener paramHTTPRequestListener) {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      getHTTPServer(i).removeRequestListener(paramHTTPRequestListener);
    } 
  }
  
  public void start() {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      getHTTPServer(i).start();
    } 
  }
  
  public void stop() {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      getHTTPServer(i).stop();
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\http\HTTPServerList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */