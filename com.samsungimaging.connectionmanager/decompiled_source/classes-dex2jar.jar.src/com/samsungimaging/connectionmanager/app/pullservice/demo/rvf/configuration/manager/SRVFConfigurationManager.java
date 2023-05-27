package com.samsungimaging.connectionmanager.app.pullservice.demo.rvf.configuration.manager;

import android.content.Context;
import android.content.SharedPreferences;

public class SRVFConfigurationManager {
  private static SRVFConfigurationManager instanceOfSRVFConfigurationManager;
  
  private static Context mContext;
  
  private static SharedPreferences mPref;
  
  public static SRVFConfigurationManager getInstance(Context paramContext) {
    mContext = paramContext;
    mPref = mContext.getSharedPreferences("SRVF_PREFERENCE", 0);
    if (instanceOfSRVFConfigurationManager == null)
      instanceOfSRVFConfigurationManager = new SRVFConfigurationManager(); 
    return instanceOfSRVFConfigurationManager;
  }
  
  public String getLastSaveFileName() {
    return mContext.getSharedPreferences("SRVF_PREFERENCE", 0).getString("PREF_LAST_SAVE_FILE_NAME", "no_file");
  }
  
  public boolean isCheckedStatusOfNotice() {
    return mContext.getSharedPreferences("SRVF_PREFERENCE", 0).getBoolean("PREF_CHECKED_STATUS_OF_NOTICE", false);
  }
  
  public void setCheckedStatusOfNotice(boolean paramBoolean) {
    SharedPreferences.Editor editor = mContext.getSharedPreferences("SRVF_PREFERENCE", 0).edit();
    editor.putBoolean("PREF_CHECKED_STATUS_OF_NOTICE", paramBoolean);
    editor.commit();
  }
  
  public void setLastSaveFileName(String paramString) {
    SharedPreferences.Editor editor = mContext.getSharedPreferences("SRVF_PREFERENCE", 0).edit();
    editor.putString("PREF_LAST_SAVE_FILE_NAME", paramString);
    editor.commit();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\rvf\configuration\manager\SRVFConfigurationManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */