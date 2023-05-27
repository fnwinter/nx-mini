package com.samsungimaging.connectionmanager.app.pullservice;

public class FunctionManager {
  public static int getAFFrameMetricsType(String paramString1, String paramString2) {
    return (paramString2.startsWith("EK_") || paramString2.startsWith("GC_") || paramString2.startsWith("AP_SSC_GC") || paramString2.startsWith("SM_")) ? 0 : ((paramString1.equals("7x3") || paramString1.equals("7X3") || paramString2.contains("NX2000") || paramString2.contains("NX300M") || paramString2.contains("NX300") || paramString2.contains("NX30")) ? 3 : ((paramString2.contains("NX210") || paramString2.contains("NX20") || paramString2.contains("NX1000") || paramString2.contains("NX1100")) ? 2 : 1));
  }
  
  public static int getAFMode(String paramString) {
    return isGalaxyAP(paramString) ? 0 : 1;
  }
  
  public static int getPhotoSizePresentationType(String paramString) {
    boolean bool = true;
    if (isGalaxyAP(paramString))
      bool = false; 
    return bool;
  }
  
  private static boolean isGalaxyAP(String paramString) {
    return !(paramString == null || (!paramString.startsWith("EK_") && !paramString.startsWith("SM_") && !paramString.startsWith("GC_") && !paramString.startsWith("AP_SSC_GC")));
  }
  
  public static boolean isNotSupportedRawQualityAndContinuousShotAtTheSameTime(String paramString) {
    return !(paramString == null || (!paramString.contains("NX3000") && !paramString.contains("NX3300") && !paramString.contains("NX MINI")));
  }
  
  public static boolean isSupportedDownloadBYOriginalImageURL(String paramString) {
    boolean bool = false;
    if (!paramString.equals("")) {
      if (Double.parseDouble(paramString) >= 1.0D)
        return true; 
    } else {
      return bool;
    } 
    return false;
  }
  
  public static boolean isSupportedSmartPanel(String paramString1, String paramString2) {
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (paramString1.equals("CSC")) {
      bool1 = bool2;
      if (!paramString2.equals("")) {
        if (Double.parseDouble(paramString2) >= 1.0D)
          return true; 
      } else {
        return bool1;
      } 
    } else {
      return bool1;
    } 
    return false;
  }
  
  public static class PhotoSizePresentationType {
    public static final int GALAXY = 0;
    
    public static final int OTHERS = 1;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\FunctionManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */