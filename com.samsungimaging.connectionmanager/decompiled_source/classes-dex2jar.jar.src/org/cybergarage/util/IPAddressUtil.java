package org.cybergarage.util;

public class IPAddressUtil {
  public static boolean isIPv4Address(String paramString) {
    return paramString.matches("([0-9]{1,3}\\.){3}([0-9]{1,3})");
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\util\IPAddressUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */