package com.samsungimaging.connectionmanager.app.cm.common;

import com.samsungimaging.connectionmanager.util.Trace;

public class CMInfo {
  public static final int NUM_OF_NFC_SCAN_RESULT_CHECK_MAX = 3;
  
  private static CMInfo instance_CMInfo = null;
  
  private String mConnectedSSID = "";
  
  private String mDescriptionURLForRVFML = null;
  
  private boolean mIsNFCLaunch = false;
  
  private int mNFCScanResultCheckCount = 0;
  
  private String mNFCTagSSID = null;
  
  private boolean mOldCamera = true;
  
  public static CMInfo getInstance() {
    // Byte code:
    //   0: ldc com/samsungimaging/connectionmanager/app/cm/common/CMInfo
    //   2: monitorenter
    //   3: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMInfo.instance_CMInfo : Lcom/samsungimaging/connectionmanager/app/cm/common/CMInfo;
    //   6: ifnonnull -> 19
    //   9: new com/samsungimaging/connectionmanager/app/cm/common/CMInfo
    //   12: dup
    //   13: invokespecial <init> : ()V
    //   16: putstatic com/samsungimaging/connectionmanager/app/cm/common/CMInfo.instance_CMInfo : Lcom/samsungimaging/connectionmanager/app/cm/common/CMInfo;
    //   19: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMInfo.instance_CMInfo : Lcom/samsungimaging/connectionmanager/app/cm/common/CMInfo;
    //   22: astore_0
    //   23: ldc com/samsungimaging/connectionmanager/app/cm/common/CMInfo
    //   25: monitorexit
    //   26: aload_0
    //   27: areturn
    //   28: astore_0
    //   29: ldc com/samsungimaging/connectionmanager/app/cm/common/CMInfo
    //   31: monitorexit
    //   32: aload_0
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   3	19	28	finally
    //   19	23	28	finally
  }
  
  public String getConnectedSSID() {
    Trace.d(CMConstants.TAG_NAME, "CMInfo.getConnectedSSID, mConnectedSSID = " + this.mConnectedSSID);
    return this.mConnectedSSID;
  }
  
  public String getDescriptionURL() {
    return this.mDescriptionURLForRVFML;
  }
  
  public boolean getIsNFCLaunch() {
    Trace.d(CMConstants.TAG_NAME, "CMInfo.getIsNFCLaunch, mIsNFCLaunch = " + this.mIsNFCLaunch + ", mNFCTagSSID = " + this.mNFCTagSSID);
    if (this.mConnectedSSID.equals(this.mNFCTagSSID) && this.mIsNFCLaunch) {
      Trace.d(CMConstants.TAG_NAME, "CMInfo.getIsNFCLaunch is true!");
      return true;
    } 
    Trace.d(CMConstants.TAG_NAME, "CMInfo.getIsNFCLaunch is false!");
    return false;
  }
  
  public boolean getIsNFCLaunchFlag() {
    Trace.d(CMConstants.TAG_NAME, "CMInfo.getIsNFCLaunchFlag, mIsNFCLaunch = " + this.mIsNFCLaunch);
    return this.mIsNFCLaunch;
  }
  
  public int getNFCScanResultCheckCount() {
    return this.mNFCScanResultCheckCount;
  }
  
  public String getNFCTagSSID() {
    return this.mNFCTagSSID;
  }
  
  public boolean isOldCamera() {
    return this.mOldCamera;
  }
  
  public void setConnectedSSID(String paramString) {
    this.mConnectedSSID = CMUtil.convertToNOTQuotedString(paramString);
    Trace.d(CMConstants.TAG_NAME, "CMInfo.setConnectedSSID, mConnectedSSID = " + this.mConnectedSSID);
  }
  
  public void setDescriptionURL(String paramString) {
    this.mDescriptionURLForRVFML = paramString;
  }
  
  public void setIsNFCLaunch(boolean paramBoolean, String paramString) {
    this.mIsNFCLaunch = paramBoolean;
    this.mNFCTagSSID = CMUtil.convertToNOTQuotedString(paramString);
    Trace.d(CMConstants.TAG_NAME, "CMInfo.setIsNFCLaunch, mIsNFCLaunch = " + this.mIsNFCLaunch + ", mNFCTagSSID = " + this.mNFCTagSSID);
    if (!this.mIsNFCLaunch)
      this.mNFCScanResultCheckCount = 0; 
  }
  
  public void setIsOldCamera(boolean paramBoolean) {
    this.mOldCamera = paramBoolean;
  }
  
  public void setNFCScanResultCheck(int paramInt) {
    this.mNFCScanResultCheckCount = paramInt;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\common\CMInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */