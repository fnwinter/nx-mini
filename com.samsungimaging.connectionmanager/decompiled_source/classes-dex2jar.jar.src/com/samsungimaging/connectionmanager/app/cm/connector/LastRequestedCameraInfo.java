package com.samsungimaging.connectionmanager.app.cm.connector;

import android.net.wifi.ScanResult;

public class LastRequestedCameraInfo {
  private static LastRequestedCameraInfo instance_LastRequestedCameraInfo = null;
  
  private String mSSID = null;
  
  private ScanResult mSr = null;
  
  public static LastRequestedCameraInfo getInstance() {
    // Byte code:
    //   0: ldc com/samsungimaging/connectionmanager/app/cm/connector/LastRequestedCameraInfo
    //   2: monitorenter
    //   3: getstatic com/samsungimaging/connectionmanager/app/cm/connector/LastRequestedCameraInfo.instance_LastRequestedCameraInfo : Lcom/samsungimaging/connectionmanager/app/cm/connector/LastRequestedCameraInfo;
    //   6: ifnonnull -> 19
    //   9: new com/samsungimaging/connectionmanager/app/cm/connector/LastRequestedCameraInfo
    //   12: dup
    //   13: invokespecial <init> : ()V
    //   16: putstatic com/samsungimaging/connectionmanager/app/cm/connector/LastRequestedCameraInfo.instance_LastRequestedCameraInfo : Lcom/samsungimaging/connectionmanager/app/cm/connector/LastRequestedCameraInfo;
    //   19: getstatic com/samsungimaging/connectionmanager/app/cm/connector/LastRequestedCameraInfo.instance_LastRequestedCameraInfo : Lcom/samsungimaging/connectionmanager/app/cm/connector/LastRequestedCameraInfo;
    //   22: astore_0
    //   23: ldc com/samsungimaging/connectionmanager/app/cm/connector/LastRequestedCameraInfo
    //   25: monitorexit
    //   26: aload_0
    //   27: areturn
    //   28: astore_0
    //   29: ldc com/samsungimaging/connectionmanager/app/cm/connector/LastRequestedCameraInfo
    //   31: monitorexit
    //   32: aload_0
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   3	19	28	finally
    //   19	23	28	finally
  }
  
  public ScanResult getLastRequestedCameraInfo() {
    return this.mSr;
  }
  
  public String getLastRequestedCameraSSID() {
    return this.mSSID;
  }
  
  public void setLastRequestedCameraInfo(ScanResult paramScanResult) {
    this.mSr = paramScanResult;
  }
  
  public void setLastRequestedCameraSSID(String paramString) {
    this.mSSID = paramString;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\connector\LastRequestedCameraInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */