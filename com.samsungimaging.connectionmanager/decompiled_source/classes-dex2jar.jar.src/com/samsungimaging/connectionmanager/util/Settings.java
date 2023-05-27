package com.samsungimaging.connectionmanager.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {
  private static final String KEY_INTRO_GUIDE = "pref_intro_guide_";
  
  private static final String KEY_INTRO_NOTICE_GUIDE = "pref_intro_notice_guide_";
  
  private static final String KEY_NFC_CONNECTION_GUIDE = "pref_nfc_connection_guide";
  
  private Context mContext = null;
  
  private SharedPreferences mPreferences = null;
  
  public Settings(Context paramContext) {
    this.mContext = paramContext;
    this.mPreferences = PreferenceManager.getDefaultSharedPreferences(paramContext);
  }
  
  public boolean getIntroGuide() {
    return this.mPreferences.getBoolean("pref_intro_guide_" + this.mContext.toString(), false);
  }
  
  public boolean getIntroNoticeGuide() {
    return this.mPreferences.getBoolean("pref_intro_notice_guide_", false);
  }
  
  public boolean getNFCConnectionGuide() {
    return this.mPreferences.getBoolean("pref_nfc_connection_guide", false);
  }
  
  public void setIntroGuide(boolean paramBoolean) {
    if (getIntroGuide() != paramBoolean) {
      SharedPreferences.Editor editor = this.mPreferences.edit();
      editor.putBoolean("pref_intro_guide_" + this.mContext.toString(), paramBoolean);
      editor.commit();
    } 
  }
  
  public void setIntroNoticeGuide(boolean paramBoolean) {
    if (getIntroGuide() != paramBoolean) {
      SharedPreferences.Editor editor = this.mPreferences.edit();
      editor.putBoolean("pref_intro_notice_guide_", paramBoolean);
      editor.commit();
    } 
  }
  
  public void setNFCConnectionGuide(boolean paramBoolean) {
    if (getNFCConnectionGuide() != paramBoolean) {
      SharedPreferences.Editor editor = this.mPreferences.edit();
      editor.putBoolean("pref_nfc_connection_guide", paramBoolean);
      editor.commit();
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanage\\util\Settings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */