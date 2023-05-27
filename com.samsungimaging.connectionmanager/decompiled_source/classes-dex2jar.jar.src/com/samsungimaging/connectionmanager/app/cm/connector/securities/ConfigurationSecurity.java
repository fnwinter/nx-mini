package com.samsungimaging.connectionmanager.app.cm.connector.securities;

import android.net.wifi.ScanResult;
import android.util.Log;

public class ConfigurationSecurity {
  public static final String EAP = "EAP";
  
  public static final String OPEN = "OPEN";
  
  public static final String PSK = "PSK";
  
  public static final int SECURITY_EAP = 2;
  
  public static final int SECURITY_OPEN = 3;
  
  public static final int SECURITY_PSK = 1;
  
  public static final int SECURITY_WEP = 0;
  
  public static final String WEP = "WEP";
  
  public static int getSecurity(ScanResult paramScanResult) {
    Log.d("c_m", "ConfigurationSecurities, getSecurity, result.capabilities = " + paramScanResult.capabilities);
    return paramScanResult.capabilities.contains("WEP") ? 0 : (paramScanResult.capabilities.contains("PSK") ? 1 : (paramScanResult.capabilities.contains("EAP") ? 2 : 3));
  }
  
  public static String getSecurity2(ScanResult paramScanResult) {
    Log.d("c_m", "ConfigurationSecurities, getSecurity2, result.capabilities = " + paramScanResult.capabilities);
    return paramScanResult.capabilities.contains("WEP") ? "WEP" : (paramScanResult.capabilities.contains("PSK") ? "PSK" : (paramScanResult.capabilities.contains("EAP") ? "EAP" : "OPEN"));
  }
  
  public static boolean isOpenNetwork(ScanResult paramScanResult) {
    return String.valueOf(3).equals(String.valueOf(getSecurity(paramScanResult)));
  }
  
  public static boolean isOpenNetwork(String paramString) {
    return String.valueOf(3).equals(paramString);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\connector\securities\ConfigurationSecurity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */