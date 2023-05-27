package org.cybergarage.net;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import org.cybergarage.util.Debug;

public class HostInterface {
  public static final int IPV4_BITMASK = 1;
  
  public static final int IPV6_BITMASK = 16;
  
  public static final int LOCAL_BITMASK = 256;
  
  public static boolean USE_LOOPBACK_ADDR = false;
  
  public static boolean USE_MULTICAST_ADDR;
  
  public static boolean USE_ONLY_IPV4_ADDR = false;
  
  public static boolean USE_ONLY_IPV6_ADDR = false;
  
  private static String ifAddress;
  
  static {
    USE_MULTICAST_ADDR = false;
    ifAddress = "";
  }
  
  public static final String getHostAddress(int paramInt) {
    if (hasAssignedInterface())
      return getInterface(); 
    int i = 0;
    try {
      Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
      label23: while (true) {
        boolean bool = enumeration.hasMoreElements();
        if (bool) {
          Enumeration<InetAddress> enumeration1 = ((NetworkInterface)enumeration.nextElement()).getInetAddresses();
          int j = i;
          while (true) {
            i = j;
            if (enumeration1.hasMoreElements()) {
              InetAddress inetAddress = enumeration1.nextElement();
              if (inetAddress.toString().contains("/192.") && isUsableAddress(inetAddress)) {
                if (j < paramInt) {
                  j++;
                  continue;
                } 
                return inetAddress.getHostAddress();
              } 
              continue;
            } 
            continue label23;
          } 
          break;
        } 
        return "";
      } 
    } catch (Exception exception) {}
    return "";
  }
  
  public static final String getHostURL(String paramString1, int paramInt, String paramString2) {
    String str = paramString1;
    if (isIPv6Address(paramString1))
      str = "[" + paramString1 + "]"; 
    return "http://" + str + ":" + Integer.toString(paramInt) + paramString2;
  }
  
  public static final String getIPv4Address() {
    int j = getNHostAddresses();
    int i = 0;
    while (true) {
      if (i >= j)
        return ""; 
      String str2 = getHostAddress(i);
      String str1 = str2;
      if (!isIPv4Address(str2)) {
        i++;
        continue;
      } 
      return str1;
    } 
  }
  
  public static final String getIPv6Address() {
    int j = getNHostAddresses();
    int i = 0;
    while (true) {
      if (i >= j)
        return ""; 
      String str2 = getHostAddress(i);
      String str1 = str2;
      if (!isIPv6Address(str2)) {
        i++;
        continue;
      } 
      return str1;
    } 
  }
  
  public static final InetAddress[] getInetAddress(int paramInt, String[] paramArrayOfString) {
    // Byte code:
    //   0: aload_1
    //   1: ifnull -> 86
    //   4: new java/util/Vector
    //   7: dup
    //   8: invokespecial <init> : ()V
    //   11: astore_3
    //   12: iconst_0
    //   13: istore_2
    //   14: iload_2
    //   15: aload_1
    //   16: arraylength
    //   17: if_icmplt -> 54
    //   20: aload_3
    //   21: invokevirtual elements : ()Ljava/util/Enumeration;
    //   24: astore_1
    //   25: new java/util/ArrayList
    //   28: dup
    //   29: invokespecial <init> : ()V
    //   32: astore_3
    //   33: aload_1
    //   34: invokeinterface hasMoreElements : ()Z
    //   39: ifne -> 96
    //   42: aload_3
    //   43: iconst_0
    //   44: anewarray java/net/InetAddress
    //   47: invokevirtual toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   50: checkcast [Ljava/net/InetAddress;
    //   53: areturn
    //   54: aload_1
    //   55: iload_2
    //   56: aaload
    //   57: invokestatic getByName : (Ljava/lang/String;)Ljava/net/NetworkInterface;
    //   60: astore #4
    //   62: aload #4
    //   64: ifnull -> 74
    //   67: aload_3
    //   68: aload #4
    //   70: invokevirtual add : (Ljava/lang/Object;)Z
    //   73: pop
    //   74: iload_2
    //   75: iconst_1
    //   76: iadd
    //   77: istore_2
    //   78: goto -> 14
    //   81: astore #4
    //   83: goto -> 74
    //   86: invokestatic getNetworkInterfaces : ()Ljava/util/Enumeration;
    //   89: astore_1
    //   90: goto -> 25
    //   93: astore_1
    //   94: aconst_null
    //   95: areturn
    //   96: aload_1
    //   97: invokeinterface nextElement : ()Ljava/lang/Object;
    //   102: checkcast java/net/NetworkInterface
    //   105: invokevirtual getInetAddresses : ()Ljava/util/Enumeration;
    //   108: astore #4
    //   110: aload #4
    //   112: invokeinterface hasMoreElements : ()Z
    //   117: ifeq -> 33
    //   120: aload #4
    //   122: invokeinterface nextElement : ()Ljava/lang/Object;
    //   127: checkcast java/net/InetAddress
    //   130: astore #5
    //   132: iload_0
    //   133: sipush #256
    //   136: iand
    //   137: ifne -> 148
    //   140: aload #5
    //   142: invokevirtual isLoopbackAddress : ()Z
    //   145: ifne -> 110
    //   148: iload_0
    //   149: iconst_1
    //   150: iand
    //   151: ifeq -> 172
    //   154: aload #5
    //   156: instanceof java/net/Inet4Address
    //   159: ifeq -> 172
    //   162: aload_3
    //   163: aload #5
    //   165: invokevirtual add : (Ljava/lang/Object;)Z
    //   168: pop
    //   169: goto -> 110
    //   172: iload_0
    //   173: bipush #16
    //   175: iand
    //   176: ifeq -> 110
    //   179: aload #5
    //   181: instanceof java/net/InetAddress
    //   184: ifeq -> 110
    //   187: aload_3
    //   188: aload #5
    //   190: invokevirtual add : (Ljava/lang/Object;)Z
    //   193: pop
    //   194: goto -> 110
    // Exception table:
    //   from	to	target	type
    //   54	62	81	java/net/SocketException
    //   86	90	93	java/net/SocketException
  }
  
  public static final String getInterface() {
    return ifAddress;
  }
  
  public static final int getNHostAddresses() {
    if (hasAssignedInterface())
      return 1; 
    int j = 0;
    int i = 0;
    try {
      Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
      label24: while (true) {
        int k = i;
        j = i;
        if (enumeration.hasMoreElements()) {
          j = i;
          Enumeration<InetAddress> enumeration1 = ((NetworkInterface)enumeration.nextElement()).getInetAddresses();
          k = i;
          while (true) {
            i = k;
            j = k;
            if (enumeration1.hasMoreElements()) {
              j = k;
              InetAddress inetAddress = enumeration1.nextElement();
              j = k;
              if (inetAddress.toString().contains("/192.")) {
                j = k;
                boolean bool = isUsableAddress(inetAddress);
                if (bool)
                  k++; 
              } 
              continue;
            } 
            continue label24;
          } 
          break;
        } 
        return k;
      } 
    } catch (Exception exception) {
      Debug.warning(exception);
      return j;
    } 
  }
  
  private static final boolean hasAssignedInterface() {
    return (ifAddress.length() > 0);
  }
  
  public static final boolean hasIPv4Addresses() {
    int j = getNHostAddresses();
    for (int i = 0;; i++) {
      if (i >= j)
        return false; 
      if (isIPv4Address(getHostAddress(i)))
        return true; 
    } 
  }
  
  public static final boolean hasIPv6Addresses() {
    int j = getNHostAddresses();
    for (int i = 0;; i++) {
      if (i >= j)
        return false; 
      if (isIPv6Address(getHostAddress(i)))
        return true; 
    } 
  }
  
  public static final boolean isIPv4Address(String paramString) {
    boolean bool = false;
    try {
      boolean bool1 = InetAddress.getByName(paramString) instanceof java.net.Inet4Address;
      if (bool1)
        bool = true; 
      return bool;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public static final boolean isIPv6Address(String paramString) {
    boolean bool = false;
    try {
      boolean bool1 = InetAddress.getByName(paramString) instanceof java.net.Inet6Address;
      if (bool1)
        bool = true; 
      return bool;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  private static final boolean isUsableAddress(InetAddress paramInetAddress) {
    return ((USE_LOOPBACK_ADDR || !paramInetAddress.isLoopbackAddress()) && (!USE_ONLY_IPV4_ADDR || !(paramInetAddress instanceof java.net.Inet6Address)) && (!USE_ONLY_IPV6_ADDR || !(paramInetAddress instanceof java.net.Inet4Address)) && (USE_MULTICAST_ADDR || !paramInetAddress.isMulticastAddress()));
  }
  
  public static final void setInterface(String paramString) {
    ifAddress = paramString;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\net\HostInterface.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */