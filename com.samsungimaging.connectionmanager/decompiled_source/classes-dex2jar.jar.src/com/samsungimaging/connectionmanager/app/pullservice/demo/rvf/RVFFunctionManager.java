package com.samsungimaging.connectionmanager.app.pullservice.demo.rvf;

import com.samsungimaging.connectionmanager.util.Trace;

public class RVFFunctionManager {
  private static Trace.Tag TAG = Trace.Tag.RVF;
  
  public static int getAFFrameMetricsType(String paramString1, String paramString2) {
    return (paramString2.startsWith("EK_") || paramString2.startsWith("GC_") || paramString2.startsWith("AP_SSC_GC") || paramString2.startsWith("SM_")) ? 0 : ((paramString1.equals("7x3") || paramString1.equals("7X3") || paramString2.contains("NX2000") || paramString2.contains("NX300M") || paramString2.contains("NX300") || paramString2.contains("NX30")) ? 3 : ((paramString2.contains("NX210") || paramString2.contains("NX20") || paramString2.contains("NX1000") || paramString2.contains("NX1100")) ? 2 : 1));
  }
  
  public static int getAFMode(String paramString) {
    return isGalaxyAP(paramString) ? 0 : 1;
  }
  
  public static int getDefaultCameraSaveType(String paramString) {
    return (isGalaxyAP(paramString) && !isGalaxySAP(paramString)) ? 1 : 0;
  }
  
  public static String getDefaultFlash(String paramString) {
    return isGalaxyAP(paramString) ? (isGalaxySAP(paramString) ? "AUTO" : "OFF") : ((paramString.contains("ST200") || paramString.contains("WB150")) ? "AUTO" : "off");
  }
  
  public static int getFlashMountType(String paramString) {
    return (isNXAP(paramString) && !isGalaxyAP(paramString)) ? 0 : 1;
  }
  
  public static int getFlashProtocolValueType(String paramString) {
    return 1;
  }
  
  private static int getLensMountType(String paramString) {
    return isNXAP(paramString) ? 0 : 1;
  }
  
  public static int getLiveViewResolutionType(String paramString) {
    return (isGalaxyAP(paramString) || paramString.contains("EX2F")) ? 1 : 2;
  }
  
  public static int getSaveGuideDialogType(String paramString1, String paramString2, String paramString3) {
    boolean bool2 = false;
    if (isGalaxyAP(paramString3))
      return isNXAP(paramString3) ? 2 : 3; 
    boolean bool1 = bool2;
    if (!paramString2.equals("")) {
      bool1 = bool2;
      if (Double.parseDouble(paramString2) >= 1.0D) {
        bool1 = bool2;
        if (!isODMAP(paramString3))
          return 1; 
      } 
    } 
    return bool1;
  }
  
  public static boolean isBigZoomMaxValue(String paramString) {
    return !(paramString == null || (!paramString.contains("ST200") && !paramString.contains("WB150")));
  }
  
  public static boolean isCancellableShotByAFFail(String paramString1, String paramString2, String paramString3) {
    return (paramString1.equals("CSC") && !paramString2.equals("")) ? ((Double.parseDouble(paramString2) >= 1.0D)) : (isNXAP(paramString3));
  }
  
  public static boolean isCancellableTimerShotByTouchLiveView(String paramString) {
    return !isGalaxyAP(paramString);
  }
  
  private static boolean isGalaxyAP(String paramString) {
    return !(paramString == null || (!paramString.startsWith("EK_") && !paramString.startsWith("SM_") && !paramString.startsWith("GC_") && !paramString.startsWith("AP_SSC_GC")));
  }
  
  private static boolean isGalaxySAP(String paramString) {
    return !(paramString == null || (!paramString.startsWith("GC_") && !paramString.startsWith("AP_SSC_GC")));
  }
  
  private static boolean isNXAP(String paramString) {
    return !(paramString == null || (!paramString.contains("NX300") && !paramString.contains("NX30") && !paramString.contains("NX3000") && !paramString.contains("NX2000") && !paramString.contains("NX20") && !paramString.contains("NX210") && !paramString.contains("NX1000") && !paramString.contains("NX1100") && !paramString.contains("F1") && !paramString.contains("NX MINI") && !paramString.contains("GN120")));
  }
  
  private static boolean isODMAP(String paramString) {
    return !(paramString == null || (!isGalaxyAP(paramString) && !paramString.contains("WB35") && !paramString.contains("WB50") && !paramString.contains("WB350") && !paramString.contains("WB1100") && !paramString.contains("WB2200")));
  }
  
  public static boolean isSupported5SecondsTimerShot(String paramString) {
    return isGalaxyAP(paramString);
  }
  
  public static boolean isSupportedDisplayZoomBar(String paramString) {
    return !(getLensMountType(paramString) == 0);
  }
  
  public static boolean isSupportedDoubleTimerShot(String paramString) {
    return (paramString != null) ? paramString.contains("GC200") : false;
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
  
  public static boolean isSupportedMoreButton(String paramString) {
    return !(paramString == null || (!paramString.contains("NX2000") && !paramString.contains("NX300M") && !paramString.contains("NX300") && !paramString.contains("NX30")));
  }
  
  public static boolean isSupportedShutterUpAct(String paramString) {
    return isGalaxyAP(paramString);
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
  
  public static boolean isSupportedZoomLongKey(String paramString1, String paramString2, String paramString3) {
    boolean bool = false;
    return !paramString3.equals("") ? ((Double.parseDouble(paramString3) >= 1.0D)) : ((paramString1.contains("NX300M") || paramString1.contains("NX300") || paramString1.contains("NX2000")) ? true : bool);
  }
  
  public static boolean isTouchableZoomButton(String paramString) {
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (paramString != null) {
      bool1 = bool2;
      if (!paramString.isEmpty()) {
        bool1 = bool2;
        if (!paramString.startsWith("GC_")) {
          bool1 = bool2;
          if (!paramString.startsWith("AP_SSC_GC")) {
            bool1 = bool2;
            if (!paramString.startsWith("SM_"))
              bool1 = true; 
          } 
        } 
      } 
    } 
    if (paramString != null)
      Trace.d(TAG, "isSupportZoomButton() mPreconnectedSsid : " + paramString + " isSupport : " + bool1); 
    return bool1;
  }
  
  public static boolean isTouchableZoomSeekBar(String paramString) {
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (paramString != null) {
      bool1 = bool2;
      if (!paramString.isEmpty()) {
        bool1 = bool2;
        if (isGalaxyAP(paramString))
          bool1 = true; 
      } 
    } 
    return bool1;
  }
  
  public static boolean isUnsupportedZoomableLens(String paramString) {
    return !(paramString == null || (!paramString.contains("NX1000") && !paramString.contains("NX1100")));
  }
  
  public static class AutoFocusFrameMetricsType {
    public static final int METRICS_1x1 = 0;
    
    public static final int METRICS_3x3 = 1;
    
    public static final int METRICS_3x5 = 2;
    
    public static final int METRICS_3x7 = 3;
  }
  
  public static class AutoFocusMode {
    public static final int MULTI = 1;
    
    public static final int SINGLE = 0;
  }
  
  public static class CameraSaveType {
    public static final int CAMERA_AND_PHONE = 0;
    
    public static final int CAMERA_ONLY = 1;
  }
  
  public static class LiveViewResolutionType {
    public static final int RESOLUTION_16x9 = 1;
    
    public static final int RESOLUTION_1x1 = 4;
    
    public static final int RESOLUTION_3x2 = 3;
    
    public static final int RESOLUTION_4x3 = 2;
    
    public static final int RESOLUTION_NONE = 0;
  }
  
  public static class MountType {
    public static final int DETACHABLE = 0;
    
    public static final int UNDETACHABLE = 1;
  }
  
  public static class ProtocolValueType {
    public static final int FIRST_ONLY_UPPER = 3;
    
    public static final int LOWER = 1;
    
    public static final int NUMERIC = 0;
    
    public static final int UPPER = 2;
  }
  
  public static class SaveGuideDialogType {
    public static final int CSC = 2;
    
    public static final int DSC = 3;
    
    public static final int NONE = 0;
    
    public static final int NOT_SUPPORTED_MOVIE_AND_RAW = 1;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\rvf\RVFFunctionManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */