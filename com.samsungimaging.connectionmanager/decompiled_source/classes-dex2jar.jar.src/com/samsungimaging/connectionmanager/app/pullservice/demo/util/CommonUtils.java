package com.samsungimaging.connectionmanager.app.pullservice.demo.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import com.samsungimaging.connectionmanager.util.Trace;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtils {
  public static final int APP_CM = 0;
  
  public static final int APP_ML = 1;
  
  public static final int APP_NONE = 6;
  
  public static final int APP_RVF = 2;
  
  public static final int APP_S2L = 3;
  
  public static final int APP_SP = 4;
  
  static final int ERROR = -1;
  
  public static final int MODE_CLIENT = 5;
  
  private static Trace.Tag TAG;
  
  static String[] configString = new String[128];
  
  private static boolean isXlargeTablet = false;
  
  private static final int thumbHeight = 80;
  
  private static final int thumbWidth = 112;
  
  private static String userAgent;
  
  private static final int zoomHeight = 240;
  
  private static final int zoomWidth = 336;
  
  static {
    TAG = Trace.Tag.ML;
    userAgent = null;
  }
  
  public static String ByteToHex(byte[] paramArrayOfbyte) {
    String str = "";
    int i = 0;
    label16: while (true) {
      if (i >= paramArrayOfbyte.length)
        return str.toUpperCase(); 
      String str1 = Integer.toHexString(paramArrayOfbyte[i]);
      if (str1.length() >= 2) {
        String str2 = str1.substring(str1.length() - 2);
        continue;
      } 
      int j = 0;
      while (true) {
        String str2 = str1;
        if (j < 2 - str1.length()) {
          str1 = "0" + str1;
          j++;
          continue;
        } 
        str = String.valueOf(str) + " " + str2;
        i++;
        continue label16;
      } 
      break;
    } 
  }
  
  public static int calculateInSampleSize(BitmapFactory.Options paramOptions, int paramInt1, int paramInt2) {
    int j = paramOptions.outHeight;
    int k = paramOptions.outWidth;
    int i = 1;
    if (j > paramInt2 || k > paramInt1) {
      if (k > j)
        return Math.round(j / paramInt2); 
    } else {
      return i;
    } 
    return Math.round(k / paramInt1);
  }
  
  public static Bitmap decodeForZoom(String paramString) {
    if (paramString.toLowerCase().endsWith(".jpg")) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(paramString, options);
      options.inSampleSize = calculateInSampleSize(options, 336, 240);
      options.inJustDecodeBounds = false;
      return getRotateBitmap(BitmapFactory.decodeFile(paramString, options), getExifOrientation(paramString));
    } 
    return ThumbnailUtils.createVideoThumbnail(paramString, 1);
  }
  
  public static Bitmap decodeThumb(String paramString) {
    if (paramString.toLowerCase().endsWith(".jpg")) {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(paramString, options);
      options.inSampleSize = calculateInSampleSize(options, 112, 80);
      options.inJustDecodeBounds = false;
      Bitmap bitmap = BitmapFactory.decodeFile(paramString, options);
      Trace.d(TAG, "FREE MEMORY : " + (Runtime.getRuntime().freeMemory() / 1048576L) + "MB");
      return getRotateBitmap(bitmap, getExifOrientation(paramString));
    } 
    return ThumbnailUtils.createVideoThumbnail(paramString, 3);
  }
  
  public static boolean externalMemoryAvailable() {
    return Environment.getExternalStorageState().equals("mounted");
  }
  
  public static boolean getActivityRunning(Context paramContext, String paramString) {
    return ((ActivityManager.RunningTaskInfo)((ActivityManager)paramContext.getSystemService("activity")).getRunningTasks(100).get(0)).baseActivity.getPackageName().contains(paramString);
  }
  
  public static long getAvailableExternalMemorySize() {
    if (externalMemoryAvailable()) {
      StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
      long l = statFs.getBlockSize();
      return statFs.getAvailableBlocks() * l;
    } 
    return -1L;
  }
  
  public static long getAvailableInternalMemorySize() {
    StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
    long l = statFs.getBlockSize();
    return statFs.getAvailableBlocks() * l;
  }
  
  public static String getDefaultStorage(int paramInt) {
    String str = String.valueOf(Environment.getExternalStorageDirectory().getPath()) + "/DCIM/Camera/Samsung Smart Camera Application/";
    switch (paramInt) {
      default:
        return str;
      case 3:
        return String.valueOf(str) + "AutoShare/";
      case 4:
        return String.valueOf(str) + "MobileLink/";
      case 2:
        return String.valueOf(str) + "RemoteViewfinder/";
      case 1:
        break;
    } 
    return String.valueOf(str) + "MobileLink/";
  }
  
  public static boolean getDisplayConnGuide(Context paramContext) {
    return PreferenceManager.getDefaultSharedPreferences(paramContext).getBoolean("connguide", true);
  }
  
  public static boolean getDisplayDetailGuide(Context paramContext) {
    return PreferenceManager.getDefaultSharedPreferences(paramContext).getBoolean("guide", true);
  }
  
  public static boolean getDisplayNotice(Context paramContext) {
    return PreferenceManager.getDefaultSharedPreferences(paramContext).getBoolean("notice", true);
  }
  
  public static int getExifOrientation(String paramString) {
    IOException iOException2 = null;
    try {
      ExifInterface exifInterface = new ExifInterface(paramString);
    } catch (IOException iOException1) {
      iOException1.printStackTrace();
      iOException1 = iOException2;
    } 
    if (iOException1 != null) {
      int i = iOException1.getAttributeInt("Orientation", 1);
      if (i != 1) {
        switch (i) {
          default:
            return 0;
          case 6:
            return 90;
          case 3:
            return 180;
          case 8:
            return 270;
          case 2:
            return 280;
          case 4:
            return 290;
          case 5:
            return 300;
          case 7:
            break;
        } 
        return 310;
      } 
    } 
  }
  
  public static String getExtention(String paramString) {
    String str2 = "";
    String str1 = str2;
    if (paramString != null) {
      str1 = str2;
      if (paramString.lastIndexOf(".") != -1)
        str1 = paramString.substring(paramString.lastIndexOf(".") + 1, paramString.length()); 
    } 
    return str1;
  }
  
  public static String getLineNumber(Context paramContext) {
    String str2 = null;
    String str1 = str2;
    if (paramContext != null) {
      TelephonyManager telephonyManager = (TelephonyManager)paramContext.getSystemService("phone");
      str1 = str2;
      if (telephonyManager.getSimState() != 1)
        str1 = telephonyManager.getLine1Number(); 
    } 
    if (str1 != null) {
      String str = str1;
      return (str1.length() == 0) ? "none" : str;
    } 
    return "none";
  }
  
  public static String getMacAddress(Context paramContext) {
    String str2 = "";
    WifiInfo wifiInfo = ((WifiManager)paramContext.getSystemService("wifi")).getConnectionInfo();
    String str1 = str2;
    if (wifiInfo != null) {
      str1 = wifiInfo.getMacAddress();
      if (str1 == null)
        return "UNKNOWN"; 
    } 
    return str1;
  }
  
  public static Map<String, PackageInfo> getPackageMap(Context paramContext) {
    SortedMap<?, ?> sortedMap = Collections.synchronizedSortedMap(new TreeMap<Object, Object>());
    PackageManager packageManager = paramContext.getPackageManager();
    Iterator<ApplicationInfo> iterator = packageManager.getInstalledApplications(128).iterator();
    while (true) {
      if (!iterator.hasNext())
        return (Map)sortedMap; 
      ApplicationInfo applicationInfo = iterator.next();
      try {
        sortedMap.put(applicationInfo.packageName, packageManager.getPackageInfo(applicationInfo.packageName, 0));
      } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
        nameNotFoundException.printStackTrace();
      } 
    } 
  }
  
  public static PackageInfo getPackageSmartWiFiCM(Context paramContext) {
    Iterator<PackageInfo> iterator = getPackageMap(paramContext).values().iterator();
    while (true) {
      if (!iterator.hasNext())
        return null; 
      PackageInfo packageInfo = iterator.next();
      if (packageInfo.packageName.startsWith("com.skt.network.wificm."))
        return packageInfo; 
    } 
  }
  
  public static Bitmap getRotateBitmap(Bitmap paramBitmap, int paramInt) {
    if (paramInt < 280 && paramBitmap != null) {
      Matrix matrix = new Matrix();
      matrix.setRotate(paramInt, paramBitmap.getWidth() / 2.0F, paramBitmap.getHeight() / 2.0F);
      try {
        Bitmap bitmap2 = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), matrix, true);
        Bitmap bitmap1 = paramBitmap;
        if (paramBitmap != bitmap2) {
          paramBitmap.recycle();
          bitmap1 = bitmap2;
        } 
        return bitmap1;
      } catch (OutOfMemoryError null) {
        outOfMemoryError.printStackTrace();
        return paramBitmap;
      } 
    } 
    Bitmap bitmap = paramBitmap;
    if (paramInt >= 280) {
      bitmap = paramBitmap;
      if (paramBitmap != null) {
        Matrix matrix = new Matrix();
        if (paramInt == 280) {
          matrix.setScale(-1.0F, 1.0F, paramBitmap.getWidth() / 2.0F, paramBitmap.getHeight() / 2.0F);
        } else if (paramInt == 290) {
          matrix.setScale(1.0F, -1.0F, paramBitmap.getWidth() / 2.0F, paramBitmap.getHeight() / 2.0F);
        } else if (paramInt == 300) {
          matrix.setRotate(270.0F, paramBitmap.getWidth() / 2.0F, paramBitmap.getHeight() / 2.0F);
          matrix.postTranslate(-(paramBitmap.getWidth() - paramBitmap.getHeight()) / 2.0F, -(paramBitmap.getHeight() - paramBitmap.getWidth()) / 2.0F);
          matrix.preScale(1.0F, -1.0F);
        } else if (paramInt == 310) {
          matrix.setRotate(90.0F, paramBitmap.getWidth() / 2.0F, paramBitmap.getHeight() / 2.0F);
          matrix.postTranslate(-(paramBitmap.getWidth() - paramBitmap.getHeight()) / 2.0F, -(paramBitmap.getHeight() - paramBitmap.getWidth()) / 2.0F);
          matrix.preScale(1.0F, -1.0F);
        } 
        try {
          Bitmap bitmap2 = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), matrix, false);
          Bitmap bitmap1 = paramBitmap;
          if (paramBitmap != bitmap2) {
            paramBitmap.recycle();
            return bitmap2;
          } 
        } catch (OutOfMemoryError outOfMemoryError) {
          outOfMemoryError.printStackTrace();
          return paramBitmap;
        } 
      } 
    } 
    return (Bitmap)outOfMemoryError;
  }
  
  public static boolean getSystemConfigurationChanged(Context paramContext) {
    boolean bool2 = true;
    Configuration configuration = paramContext.getResources().getConfiguration();
    Trace.d(TAG, "getSystemConfigurationChanged configString[0] : " + configString[0]);
    Trace.d(TAG, "getSystemConfigurationChanged configString[1] : " + configString[1]);
    Trace.d(TAG, "getSystemConfigurationChanged config : " + configuration.toString());
    if (configString[0] != null && configuration.toString().equals(configString[0])) {
      boolean bool = false;
      Trace.d(TAG, "getSystemConfigurationChanged   config value is Not changed");
      return bool;
    } 
    boolean bool1 = bool2;
    if (configString[1] != null) {
      bool1 = bool2;
      if (configuration.toString().equals(configString[1])) {
        Trace.d(TAG, "getSystemConfigurationChanged   config value is Not changed");
        return false;
      } 
    } 
    return bool1;
  }
  
  public static long getTotalExternalMemorySize() {
    if (externalMemoryAvailable()) {
      StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
      long l = statFs.getBlockSize();
      return statFs.getBlockCount() * l;
    } 
    return -1L;
  }
  
  public static long getTotalInternalMemorySize() {
    StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
    long l = statFs.getBlockSize();
    return statFs.getBlockCount() * l;
  }
  
  public static String getUserAgent() {
    return userAgent;
  }
  
  public static String getUseragent(Context paramContext) {
    // Byte code:
    //   0: new java/lang/StringBuffer
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_3
    //   8: aconst_null
    //   9: astore_1
    //   10: aconst_null
    //   11: astore_2
    //   12: aload_0
    //   13: ifnull -> 81
    //   16: aload_0
    //   17: ldc_w 'phone'
    //   20: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   23: checkcast android/telephony/TelephonyManager
    //   26: astore_1
    //   27: aload_1
    //   28: invokevirtual getSimState : ()I
    //   31: iconst_1
    //   32: if_icmpeq -> 40
    //   35: aload_1
    //   36: invokevirtual getLine1Number : ()Ljava/lang/String;
    //   39: astore_2
    //   40: aload_2
    //   41: ifnull -> 53
    //   44: aload_2
    //   45: astore_1
    //   46: aload_2
    //   47: invokevirtual length : ()I
    //   50: ifne -> 81
    //   53: aload_0
    //   54: ldc_w 'wifi'
    //   57: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   60: checkcast android/net/wifi/WifiManager
    //   63: invokevirtual getConnectionInfo : ()Landroid/net/wifi/WifiInfo;
    //   66: astore_0
    //   67: aload_2
    //   68: astore_1
    //   69: aload_0
    //   70: ifnull -> 81
    //   73: aload_0
    //   74: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   77: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   80: astore_1
    //   81: aload_1
    //   82: ifnull -> 94
    //   85: aload_1
    //   86: astore_0
    //   87: aload_1
    //   88: invokevirtual length : ()I
    //   91: ifne -> 98
    //   94: ldc_w 'UNKNOWN'
    //   97: astore_0
    //   98: aload_3
    //   99: aload_0
    //   100: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   103: pop
    //   104: aload_3
    //   105: invokevirtual toString : ()Ljava/lang/String;
    //   108: areturn
  }
  
  public static String intToIp(int paramInt) {
    return String.valueOf(paramInt & 0xFF) + "." + (paramInt >> 8 & 0xFF) + "." + (paramInt >> 16 & 0xFF) + "." + (paramInt >> 24 & 0xFF);
  }
  
  public static boolean isICS() {
    return Build.MODEL.contains("Galaxy Nexus");
  }
  
  public static boolean isMemoryFull() {
    boolean bool = false;
    try {
      StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
      long l = statFs.getBlockSize();
      int i = statFs.getAvailableBlocks();
      if (i * l < 10000000L)
        bool = true; 
      return bool;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public static boolean isMountedExternalStorage() {
    boolean bool = true;
    String str = Environment.getExternalStorageState();
    if (str.equals("removed") || str.equals("shared") || str.equals("bad_removal") || str.equals("unmounted") || str.equals("checking") || str.equals("unmountable"))
      bool = false; 
    return bool;
  }
  
  public static boolean isTab101() {
    return Build.MODEL.contains("SHW-M380");
  }
  
  public static boolean isTopActivity(Context paramContext, String paramString) {
    if (paramContext != null && paramString != null) {
      List list = ((ActivityManager)paramContext.getSystemService("activity")).getRunningTasks(1);
      paramContext = null;
      Iterator<ActivityManager.RunningTaskInfo> iterator = list.iterator();
      while (true) {
        if (!iterator.hasNext())
          return !!((ActivityManager.RunningTaskInfo)paramContext).topActivity.getClassName().equals(paramString); 
        ActivityManager.RunningTaskInfo runningTaskInfo = iterator.next();
      } 
    } 
    return true;
  }
  
  public static boolean isXlargeTablet() {
    return isXlargeTablet;
  }
  
  public static void noop(long paramLong) {
    try {
      Thread.sleep(paramLong);
      Thread.yield();
      return;
    } catch (Throwable throwable) {
      return;
    } 
  }
  
  private static String rename(String paramString) {
    Matcher matcher = Pattern.compile("(\\-)(\\d*)+[.]").matcher(paramString);
    if (matcher.find()) {
      int i = Integer.parseInt(matcher.group().replace("-", "").replace(".", ""));
      return paramString.replaceAll("(\\-)(\\d*)+[.]", "-" + (i + 1) + ".");
    } 
    return String.valueOf(paramString.substring(0, paramString.lastIndexOf("."))) + "-0" + paramString.substring(paramString.lastIndexOf("."), paramString.length());
  }
  
  public static String renameFile(String paramString1, String paramString2) {
    File file2 = new File(paramString1, paramString2);
    String str = paramString2;
    for (File file1 = file2;; file1 = new File(paramString1, str)) {
      if (!file1.exists())
        return str; 
      str = rename(str);
    } 
  }
  
  public static void setDisplayConnGuide(Context paramContext, boolean paramBoolean) {
    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
    editor.putBoolean("connguide", paramBoolean);
    editor.commit();
  }
  
  public static void setDisplayDetailGuide(Context paramContext, boolean paramBoolean) {
    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
    editor.putBoolean("guide", paramBoolean);
    editor.commit();
  }
  
  public static void setDisplayNotice(Context paramContext, boolean paramBoolean) {
    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
    editor.putBoolean("notice", paramBoolean);
    editor.commit();
  }
  
  public static void setSystemConfigurationChanged(Context paramContext) {
    Configuration configuration = paramContext.getResources().getConfiguration();
    if (configuration.orientation == 1) {
      configString[0] = configuration.toString();
      Trace.d(TAG, "setSystemConfigurationChanged configString[0] : " + configString[0]);
      return;
    } 
    if (configuration.orientation == 2) {
      configString[1] = configuration.toString();
      Trace.d(TAG, "setSystemConfigurationChanged configString[1] : " + configString[1]);
      return;
    } 
  }
  
  public static void setUseragent(Context paramContext) {
    // Byte code:
    //   0: new java/lang/StringBuffer
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_3
    //   8: aload_3
    //   9: ldc_w 'SEC_RVF_ML_'
    //   12: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   15: pop
    //   16: aconst_null
    //   17: astore_1
    //   18: aconst_null
    //   19: astore_2
    //   20: aload_0
    //   21: ifnull -> 102
    //   24: aload_0
    //   25: ldc_w 'phone'
    //   28: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   31: checkcast android/telephony/TelephonyManager
    //   34: astore_1
    //   35: aload_1
    //   36: invokevirtual getSimState : ()I
    //   39: iconst_1
    //   40: if_icmpeq -> 48
    //   43: aload_1
    //   44: invokevirtual getLine1Number : ()Ljava/lang/String;
    //   47: astore_2
    //   48: aload_2
    //   49: ifnull -> 61
    //   52: aload_2
    //   53: astore_1
    //   54: aload_2
    //   55: invokevirtual length : ()I
    //   58: ifne -> 102
    //   61: aload_0
    //   62: ldc_w 'wifi'
    //   65: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   68: checkcast android/net/wifi/WifiManager
    //   71: invokevirtual getConnectionInfo : ()Landroid/net/wifi/WifiInfo;
    //   74: astore_0
    //   75: aload_2
    //   76: astore_1
    //   77: aload_0
    //   78: ifnull -> 102
    //   81: aload_0
    //   82: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   85: astore_0
    //   86: aload_2
    //   87: astore_1
    //   88: aload_0
    //   89: ifnull -> 102
    //   92: aload_0
    //   93: ldc_w ':'
    //   96: ldc ''
    //   98: invokevirtual replace : (Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
    //   101: astore_1
    //   102: aload_1
    //   103: ifnull -> 115
    //   106: aload_1
    //   107: astore_0
    //   108: aload_1
    //   109: invokevirtual length : ()I
    //   112: ifne -> 119
    //   115: ldc_w 'UNKNOWN'
    //   118: astore_0
    //   119: aload_3
    //   120: aload_0
    //   121: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   124: pop
    //   125: aload_3
    //   126: invokevirtual toString : ()Ljava/lang/String;
    //   129: putstatic com/samsungimaging/connectionmanager/app/pullservice/demo/util/CommonUtils.userAgent : Ljava/lang/String;
    //   132: return
  }
  
  public static void setXlargeTablet() {
    isXlargeTablet = true;
  }
  
  public static long waitFor(long paramLong, IFunc paramIFunc) {
    long l2 = System.currentTimeMillis();
    long l1 = 0L;
    while (true) {
      if (paramIFunc.func())
        return l1; 
      try {
        Thread.sleep(10L);
      } catch (Throwable throwable) {}
      Thread.yield();
      long l = System.currentTimeMillis() - l2;
      l1 = l;
      if (l > paramLong)
        return l; 
    } 
  }
  
  public static interface IFunc {
    boolean func();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\dem\\util\CommonUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */