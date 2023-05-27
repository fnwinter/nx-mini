package com.samsungimaging.connectionmanager.app.pullservice.datatype;

import com.samsungimaging.connectionmanager.util.Trace;
import java.util.ArrayList;
import java.util.Locale;

public class DSCCamera {
  private static Trace.Tag TAG = Trace.Tag.RVF;
  
  private boolean isConnected = false;
  
  private ArrayList<String> mAFAreaMenuItems = new ArrayList<String>();
  
  private ArrayList<DSCMenuItem> mAFAreaMenuItemsDim = new ArrayList<DSCMenuItem>();
  
  private String mAFAreaValue = "MULTI AF";
  
  private ArrayList<String> mAFModeMenuItems = new ArrayList<String>();
  
  private String mAFModeValue = "";
  
  private String mAFPriority = "0";
  
  private String mAFShotResult = "";
  
  private ArrayList<String> mApertureMenuItems = new ArrayList<String>();
  
  private String mApertureValue = "";
  
  private String mAvailShots = "";
  
  private String mCardStatusValue = "External";
  
  private String mCurrentFlashDisplay = "AUTO";
  
  private String mCurrentLedTimeMenuItem = "Off";
  
  private String mCurrentStreamQuality = "high";
  
  private String mCurrentStreamUrl = "";
  
  private String mDefaultFlash = "";
  
  private String mDefaultFocusState = "mf";
  
  private String mDefaultResolutionIndex = "0";
  
  private String mDefaultZoom = "0";
  
  private ArrayList<String> mDialModeMenuItems = new ArrayList<String>();
  
  private String mDialModeValue = "Auto";
  
  private ArrayList<String> mDriveMenuItems = new ArrayList<String>();
  
  private String mDriveValue = "Single";
  
  private ArrayList<String> mEVMenuItems = new ArrayList<String>();
  
  private String mEVValue = "";
  
  private ArrayList<String> mFileSaveMenuItems = new ArrayList<String>();
  
  private ArrayList<DSCMenuItem> mFileSaveMenuItemsDim = new ArrayList<DSCMenuItem>();
  
  private String mFileSaveValue = "Camera";
  
  private ArrayList<String> mFlashDisplayMenuItems = new ArrayList<String>();
  
  private ArrayList<String> mFlashMenuItems = new ArrayList<String>();
  
  private String mFlashStrobeStatus = "";
  
  private ArrayList<String> mISOMenuItems = new ArrayList<String>();
  
  private String mISOValue = "";
  
  private boolean mIsResizeMode = false;
  
  private boolean mIsTouchAFMovie = false;
  
  private ArrayList<String> mLedTimeMenuItems = new ArrayList<String>();
  
  private String mMaxZoom = "0";
  
  private ArrayList<String> mMeteringMenuItems = new ArrayList<String>();
  
  private String mMeteringValue = "";
  
  private String mMinZoom = "0";
  
  private String mMovieRecordTime = "0";
  
  private ArrayList<DSCMovieResolution> mMovieResolutionMenuItems = new ArrayList<DSCMovieResolution>();
  
  private String mMovieResolutionRatio = "";
  
  private String mMovieResolutionValue = "";
  
  private String mMultiAFMatrixSizeValue = "";
  
  private ArrayList<String> mQualityMenuItems = new ArrayList<String>();
  
  private String mQualityValue = "";
  
  private ArrayList<DSCResolution> mRatioOffsetMenuItems = new ArrayList<DSCResolution>();
  
  private DSCResolution mRatioOffsetValue = null;
  
  private String mRatioValue = "";
  
  private String mRemainRecTimeValue = "0";
  
  private ArrayList<DSCResolution> mResolutionMenuItems = new ArrayList<DSCResolution>();
  
  private ArrayList<DSCMenuItem> mResolutionMenuItemsDim = new ArrayList<DSCMenuItem>();
  
  private String mRotation = "";
  
  private String mSaveValue = "";
  
  private ArrayList<String> mShutterSpeedMenuItems = new ArrayList<String>();
  
  private String mShutterSpeedValue = "";
  
  private ArrayList<String> mStreamQualityMenuItems = new ArrayList<String>();
  
  private ArrayList<DSCMenuItem> mStreamUrlMenuItems = new ArrayList<DSCMenuItem>();
  
  private ArrayList<String> mTouchAFMenuItems = new ArrayList<String>();
  
  private ArrayList<DSCMenuItem> mTouchAFMenuItemsDim = new ArrayList<DSCMenuItem>();
  
  private DSCPosition mTouchAFPosition = new DSCPosition(50, 50);
  
  private String mTouchAFValue = "Touch AF";
  
  private String mVersion = "";
  
  private String mVersionPrefix = "";
  
  private String mVideoOutValue = "NTSC";
  
  private ArrayList<String> mWBMenuItems = new ArrayList<String>();
  
  private String mWBValue = "";
  
  public ArrayList<String> getAFAreaMenuItems() {
    return this.mAFAreaMenuItems;
  }
  
  public ArrayList<DSCMenuItem> getAFAreaMenuItemsDim() {
    return this.mAFAreaMenuItemsDim;
  }
  
  public String getAFAreaValue() {
    return this.mAFAreaValue;
  }
  
  public ArrayList<String> getAFModeMenuItems() {
    return this.mAFModeMenuItems;
  }
  
  public String getAFModeValue() {
    return this.mAFModeValue;
  }
  
  public String getAFPriority() {
    return this.mAFPriority;
  }
  
  public String getAFShotResult() {
    return this.mAFShotResult;
  }
  
  public ArrayList<String> getApertureMenuItems() {
    return this.mApertureMenuItems;
  }
  
  public String getApertureValue() {
    return this.mApertureValue;
  }
  
  public String getAvailShots() {
    return this.mAvailShots;
  }
  
  public String getCardStatusValue() {
    return this.mCardStatusValue;
  }
  
  public String getCurrentFlashDisplay() {
    return this.mCurrentFlashDisplay;
  }
  
  public int getCurrentFlashDisplayIndex() {
    for (int i = 0;; i++) {
      if (i >= getFlashDisplayMenuItems().size())
        return 0; 
      if (((String)getFlashDisplayMenuItems().get(i)).equals(getCurrentFlashDisplay()))
        return i; 
    } 
  }
  
  public String getCurrentLEDTimeMenuItem() {
    return this.mCurrentLedTimeMenuItem;
  }
  
  public String getCurrentStreamQuality() {
    return this.mCurrentStreamQuality;
  }
  
  public String getCurrentStreamUrl() {
    return this.mCurrentStreamUrl;
  }
  
  public String getDefaultFlash() {
    return this.mDefaultFlash;
  }
  
  public int getDefaultFlashIndex() {
    for (int i = 0;; i++) {
      if (i >= getFlashMenuItems().size())
        return 0; 
      if (((String)getFlashMenuItems().get(i)).toUpperCase(Locale.ENGLISH).equals(getDefaultFlash().toUpperCase(Locale.ENGLISH)))
        return i; 
    } 
  }
  
  public String getDefaultFocusState() {
    return this.mDefaultFocusState;
  }
  
  public String getDefaultResolutionIndex() {
    return this.mDefaultResolutionIndex;
  }
  
  public String getDefaultZoom() {
    return this.mDefaultZoom;
  }
  
  public ArrayList<String> getDialModeMenuItems() {
    return this.mDialModeMenuItems;
  }
  
  public String getDialModeValue() {
    return this.mDialModeValue;
  }
  
  public ArrayList<String> getDriveMenuItems() {
    return this.mDriveMenuItems;
  }
  
  public String getDriveValue() {
    Trace.d(TAG, "start getDriveValue() mDriveValue : " + this.mDriveValue);
    return this.mDriveValue;
  }
  
  public ArrayList<String> getEVMenuItems() {
    return this.mEVMenuItems;
  }
  
  public String getEVValue() {
    return this.mEVValue;
  }
  
  public ArrayList<String> getFileSaveMenuItems() {
    return this.mFileSaveMenuItems;
  }
  
  public ArrayList<DSCMenuItem> getFileSaveMenuItemsDim() {
    return this.mFileSaveMenuItemsDim;
  }
  
  public String getFileSaveValue() {
    return this.mFileSaveValue;
  }
  
  public ArrayList<String> getFlashDisplayMenuItems() {
    return this.mFlashDisplayMenuItems;
  }
  
  public ArrayList<String> getFlashMenuItems() {
    return this.mFlashMenuItems;
  }
  
  public String getFlashStrobeStatus() {
    return this.mFlashStrobeStatus;
  }
  
  public ArrayList<String> getISOMenuItems() {
    return this.mISOMenuItems;
  }
  
  public String getISOValue() {
    return this.mISOValue;
  }
  
  public ArrayList<String> getLedTimeMenuItems() {
    return this.mLedTimeMenuItems;
  }
  
  public String getMaxZoom() {
    return this.mMaxZoom;
  }
  
  public ArrayList<String> getMeteringMenuItems() {
    return this.mMeteringMenuItems;
  }
  
  public String getMeteringValue() {
    return this.mMeteringValue;
  }
  
  public String getMinZoom() {
    return this.mMinZoom;
  }
  
  public String getMovieRecordTime() {
    return this.mMovieRecordTime;
  }
  
  public ArrayList<DSCMovieResolution> getMovieResolutionMenuItems() {
    return this.mMovieResolutionMenuItems;
  }
  
  public String getMovieResolutionRatio() {
    return this.mMovieResolutionRatio;
  }
  
  public String getMovieResolutionValue() {
    return this.mMovieResolutionValue;
  }
  
  public String getMultiAFMatrixSizeValue() {
    return this.mMultiAFMatrixSizeValue;
  }
  
  public ArrayList<String> getQualityMenuItems() {
    return this.mQualityMenuItems;
  }
  
  public String getQualityValue() {
    return this.mQualityValue;
  }
  
  public ArrayList<DSCResolution> getRatioOffsetMenuItems() {
    return this.mRatioOffsetMenuItems;
  }
  
  public DSCResolution getRatioOffsetValue() {
    return this.mRatioOffsetValue;
  }
  
  public String getRatioValue() {
    return this.mRatioValue;
  }
  
  public String getRemainRecTimeValue() {
    return this.mRemainRecTimeValue;
  }
  
  public ArrayList<DSCResolution> getResolutionMenuItems() {
    return this.mResolutionMenuItems;
  }
  
  public ArrayList<DSCMenuItem> getResolutionMenuItemsDim() {
    return this.mResolutionMenuItemsDim;
  }
  
  public String getRotation() {
    return this.mRotation;
  }
  
  public String getSaveValue() {
    return this.mSaveValue;
  }
  
  public ArrayList<String> getShutterSpeedMenuItems() {
    return this.mShutterSpeedMenuItems;
  }
  
  public String getShutterSpeedValue() {
    return this.mShutterSpeedValue;
  }
  
  public ArrayList<String> getStreamQualityMenuItems() {
    return this.mStreamQualityMenuItems;
  }
  
  public ArrayList<DSCMenuItem> getStreamUrlMenuItems() {
    return this.mStreamUrlMenuItems;
  }
  
  public ArrayList<String> getTouchAFMenuItems() {
    return this.mTouchAFMenuItems;
  }
  
  public ArrayList<DSCMenuItem> getTouchAFMenuItemsDim() {
    return this.mTouchAFMenuItemsDim;
  }
  
  public DSCPosition getTouchAFPosition() {
    return this.mTouchAFPosition;
  }
  
  public String getTouchAFValue() {
    return this.mTouchAFValue;
  }
  
  public String getVersion() {
    return this.mVersion;
  }
  
  public String getVersionPrefix() {
    return this.mVersionPrefix;
  }
  
  public String getVideoOutValue() {
    return this.mVideoOutValue;
  }
  
  public ArrayList<String> getWBMenuItems() {
    return this.mWBMenuItems;
  }
  
  public String getWBValue() {
    return this.mWBValue;
  }
  
  public boolean isConnected() {
    return this.isConnected;
  }
  
  public boolean isResizeMode() {
    return this.mIsResizeMode;
  }
  
  public boolean isTouchAFMovie() {
    return this.mIsTouchAFMovie;
  }
  
  public void setAFAreaMenuItems(ArrayList<String> paramArrayList) {
    this.mAFAreaMenuItems = paramArrayList;
  }
  
  public void setAFAreaMenuItemsDim(ArrayList<DSCMenuItem> paramArrayList) {
    this.mAFAreaMenuItemsDim = paramArrayList;
  }
  
  public void setAFAreaValue(String paramString) {
    this.mAFAreaValue = paramString;
  }
  
  public void setAFModeMenuItems(ArrayList<String> paramArrayList) {
    this.mAFModeMenuItems = paramArrayList;
  }
  
  public void setAFModeValue(String paramString) {
    this.mAFModeValue = paramString;
  }
  
  public void setAFPriority(String paramString) {
    this.mAFPriority = paramString;
  }
  
  public void setAFShotResult(String paramString) {
    this.mAFShotResult = paramString;
  }
  
  public void setApertureMenuItems(ArrayList<String> paramArrayList) {
    this.mApertureMenuItems = paramArrayList;
  }
  
  public void setApertureValue(String paramString) {
    this.mApertureValue = paramString;
  }
  
  public void setAvailShots(String paramString) {
    this.mAvailShots = paramString;
  }
  
  public void setCardStatusValue(String paramString) {
    this.mCardStatusValue = paramString;
  }
  
  public void setConnected(boolean paramBoolean) {
    this.isConnected = paramBoolean;
  }
  
  public void setCurrentFlashDisplay(String paramString) {
    this.mCurrentFlashDisplay = paramString;
  }
  
  public void setCurrentLEDTimeMenuItem(String paramString) {
    this.mCurrentLedTimeMenuItem = paramString;
  }
  
  public void setCurrentStreamQuality(String paramString) {
    this.mCurrentStreamQuality = paramString;
  }
  
  protected void setCurrentStreamUrl(String paramString) {
    this.mCurrentStreamUrl = paramString;
  }
  
  public void setDefaultFlash(String paramString) {
    this.mDefaultFlash = paramString;
  }
  
  public void setDefaultFocusState(String paramString) {
    this.mDefaultFocusState = paramString;
  }
  
  public void setDefaultResolutionIndex(String paramString) {
    this.mDefaultResolutionIndex = paramString;
  }
  
  public void setDefaultZoom(String paramString) {
    this.mDefaultZoom = paramString;
  }
  
  public void setDialModeMenuItems(ArrayList<String> paramArrayList) {
    this.mDialModeMenuItems = paramArrayList;
  }
  
  public void setDialModeValue(String paramString) {
    this.mDialModeValue = paramString;
  }
  
  public void setDriveMenuItems(ArrayList<String> paramArrayList) {
    this.mDriveMenuItems = paramArrayList;
  }
  
  public void setDriveValue(String paramString) {
    Trace.d(TAG, "start setDriveValue() driveValue : " + paramString);
    this.mDriveValue = paramString;
  }
  
  public void setEVMenuItems(ArrayList<String> paramArrayList) {
    this.mEVMenuItems = paramArrayList;
  }
  
  public void setEVValue(String paramString) {
    this.mEVValue = paramString;
  }
  
  public void setFileSaveMenuItems(ArrayList<String> paramArrayList) {
    this.mFileSaveMenuItems = paramArrayList;
  }
  
  public void setFileSaveMenuItemsDim(ArrayList<DSCMenuItem> paramArrayList) {
    this.mFileSaveMenuItemsDim = paramArrayList;
  }
  
  public void setFileSaveValue(String paramString) {
    this.mFileSaveValue = paramString;
  }
  
  protected void setFlashDisplayMenuItems(ArrayList<String> paramArrayList) {
    this.mFlashDisplayMenuItems = paramArrayList;
  }
  
  protected void setFlashMenuItems(ArrayList<String> paramArrayList) {
    this.mFlashMenuItems = paramArrayList;
  }
  
  public void setFlashStrobeStatus(String paramString) {
    this.mFlashStrobeStatus = paramString;
  }
  
  public void setISOMenuItems(ArrayList<String> paramArrayList) {
    this.mISOMenuItems = paramArrayList;
  }
  
  public void setISOValue(String paramString) {
    this.mISOValue = paramString;
  }
  
  public void setLedTimeMenuItems(ArrayList<String> paramArrayList) {
    this.mLedTimeMenuItems = paramArrayList;
  }
  
  protected void setMaxZoom(String paramString) {
    this.mMaxZoom = paramString;
  }
  
  public void setMeteringMenuItems(ArrayList<String> paramArrayList) {
    this.mMeteringMenuItems = paramArrayList;
  }
  
  public void setMeteringValue(String paramString) {
    this.mMeteringValue = paramString;
  }
  
  public void setMinZoom(String paramString) {
    this.mMinZoom = paramString;
  }
  
  public void setMovieRecordTime(String paramString) {
    this.mMovieRecordTime = paramString;
  }
  
  public void setMovieResolutionMenuItems(ArrayList<DSCMovieResolution> paramArrayList) {
    this.mMovieResolutionMenuItems = paramArrayList;
  }
  
  public void setMovieResolutionRatio(String paramString) {
    this.mMovieResolutionRatio = paramString;
  }
  
  public void setMovieResolutionValue(String paramString) {
    this.mMovieResolutionValue = paramString;
  }
  
  public void setMultiAFMatrixSizeValue(String paramString) {
    this.mMultiAFMatrixSizeValue = paramString;
  }
  
  public void setQualityMenuItems(ArrayList<String> paramArrayList) {
    this.mQualityMenuItems = paramArrayList;
  }
  
  public void setQualityValue(String paramString) {
    this.mQualityValue = paramString;
  }
  
  public void setRatioOffsetMenuItems(ArrayList<DSCResolution> paramArrayList) {
    this.mRatioOffsetMenuItems = paramArrayList;
  }
  
  public void setRatioOffsetValue(DSCResolution paramDSCResolution) {
    this.mRatioOffsetValue = paramDSCResolution;
  }
  
  public void setRatioValue(String paramString) {
    this.mRatioValue = paramString;
  }
  
  public void setRemainRecTimeValue(String paramString) {
    this.mRemainRecTimeValue = paramString;
  }
  
  public void setResizeMode(boolean paramBoolean) {
    this.mIsResizeMode = paramBoolean;
  }
  
  protected void setResolutionMenuItems(ArrayList<DSCResolution> paramArrayList) {
    this.mResolutionMenuItems = paramArrayList;
  }
  
  public void setResolutionMenuItemsDim(ArrayList<DSCMenuItem> paramArrayList) {
    this.mResolutionMenuItemsDim = paramArrayList;
  }
  
  protected void setRotation(String paramString) {
    this.mRotation = paramString;
  }
  
  public void setSaveValue(String paramString) {
    this.mSaveValue = paramString;
  }
  
  public void setShutterSpeedMenuItems(ArrayList<String> paramArrayList) {
    this.mShutterSpeedMenuItems = paramArrayList;
  }
  
  public void setShutterSpeedValue(String paramString) {
    this.mShutterSpeedValue = paramString;
  }
  
  protected void setStreamQualityMenuItems(ArrayList<String> paramArrayList) {
    this.mStreamQualityMenuItems = paramArrayList;
  }
  
  protected void setStreamUrlMenuItems(ArrayList<DSCMenuItem> paramArrayList) {
    this.mStreamUrlMenuItems = paramArrayList;
  }
  
  public void setTouchAFMenuItems(ArrayList<String> paramArrayList) {
    this.mTouchAFMenuItems = paramArrayList;
  }
  
  public void setTouchAFMenuItemsDim(ArrayList<DSCMenuItem> paramArrayList) {
    this.mTouchAFMenuItemsDim = paramArrayList;
  }
  
  public void setTouchAFMovie(boolean paramBoolean) {
    this.mIsTouchAFMovie = paramBoolean;
  }
  
  public void setTouchAFPosition(DSCPosition paramDSCPosition) {
    this.mTouchAFPosition = paramDSCPosition;
  }
  
  public void setTouchAFValue(String paramString) {
    this.mTouchAFValue = paramString;
  }
  
  public void setVersion(String paramString) {
    this.mVersion = paramString;
  }
  
  public void setVersionPrefix(String paramString) {
    this.mVersionPrefix = paramString;
  }
  
  public void setVideoOutValue(String paramString) {
    this.mVideoOutValue = paramString;
  }
  
  public void setWBMenuItems(ArrayList<String> paramArrayList) {
    this.mWBMenuItems = paramArrayList;
  }
  
  public void setWBValue(String paramString) {
    this.mWBValue = paramString;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\datatype\DSCCamera.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */