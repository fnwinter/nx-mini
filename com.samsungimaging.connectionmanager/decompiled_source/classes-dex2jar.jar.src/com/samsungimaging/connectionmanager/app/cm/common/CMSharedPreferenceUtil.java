package com.samsungimaging.connectionmanager.app.cm.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class CMSharedPreferenceUtil {
  public static int getInteger(Context paramContext, String paramString, int paramInt) {
    return PreferenceManager.getDefaultSharedPreferences(paramContext).getInt(paramString, paramInt);
  }
  
  public static long getLong(Context paramContext, String paramString, long paramLong) {
    return PreferenceManager.getDefaultSharedPreferences(paramContext).getLong(paramString, paramLong);
  }
  
  public static String getString(Context paramContext, String paramString1, String paramString2) {
    return PreferenceManager.getDefaultSharedPreferences(paramContext).getString(paramString1, paramString2);
  }
  
  public static void put(Context paramContext, String paramString, int paramInt) {
    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
    editor.putInt(paramString, paramInt);
    editor.commit();
  }
  
  public static void put(Context paramContext, String paramString, long paramLong) {
    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
    editor.putLong(paramString, paramLong);
    editor.commit();
  }
  
  public static void put(Context paramContext, String paramString1, String paramString2) {
    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
    editor.putString(paramString1, paramString2);
    editor.commit();
  }
  
  public static void remove(Context paramContext, String paramString) {
    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
    editor.remove(paramString);
    editor.commit();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\common\CMSharedPreferenceUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */