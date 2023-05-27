package com.samsungimaging.connectionmanager.app.pullservice.demo.ml;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.preference.PreferenceManager;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
  private static String ACCESS_METHOD_MANUAL;
  
  private static String ACCESS_METHOD_NFC;
  
  private static ArrayList<String> dscPrefixList = null;
  
  static String[] noTelephonyDeviceList;
  
  private static String userAgent;
  
  static {
    dscPrefixList = new ArrayList<String>();
    dscPrefixList.add("WB150");
    dscPrefixList.add("DV300");
    dscPrefixList.add("DV500");
    dscPrefixList.add("WB300");
    dscPrefixList.add("ST200");
    dscPrefixList.add("WB850");
    dscPrefixList.add("NX20");
    dscPrefixList.add("NX210");
    dscPrefixList.add("NX1000");
    dscPrefixList.add("EX2");
    dscPrefixList.add("MV900");
    dscPrefixList.add("QF30");
    userAgent = null;
    ACCESS_METHOD_NFC = "nfc";
    ACCESS_METHOD_MANUAL = "manual";
    noTelephonyDeviceList = new String[] { 
        "GT-P7500", "SCH-I905", "SHW-M300W", "SHW-M380K", "SHW-M380S", "SHW-M380W", "P3", "SGH-T859", "SHW-M180K", "SMT-i9100", 
        "GT-P7300", "GT-P7310", "SGH-I957", "YP-G1", "YP-G70", "YP-GB1", "YP-GB70", "YP-GS1", "GT-B7510", "GT-B7510B", 
        "GT-B7510L", "GT-B5510", "GT-B5510B", "GT-B5510L", "GT-B5512", "SGH-T939", "GT-I5500", "GT-I5500B", "GT-I5500L", "GT-I5503", 
        "GT-B5512B", "GT-I5500M", "GT-I5503T", "GT-I5508", "GT-I5700L", "GT-I5800L", "GT-P1010", "GT-P1013", "GT-P6210", "GT-P6211", 
        "GT-P6810", "GT-P7300B", "GT-P7320", "GT-P7500D", "GT-P7500M", "GT-P7500R", "GT-P7501", "GT-P7503", "GT-P7511", "GT-S5360", 
        "GT-S5360B", "GT-S5360L", "GT-S5360T", "GT-S5363", "GT-S5368", "GT-S5369", "GT-S5570B", "GT-S5570I", "GT-S5570L", "GT-S5578", 
        "GT-S5670B", "GT-S5670L", "GT-S6102", "GT-I7500", "GT-S5670", "GT-S5570", "SPH-M900", "SPH-M580", "SC-01D", "SCH-I559", 
        "SCH-I815", "SCH-P739", "SCH-i509", "SCH-i559", "SGH-I957D", "SGH-I957M", "SGH-T499", "SGH-T499V", "SGH-T499Y", "SGH-T869", 
        "SHV-E140K", "SHV-E140L", "SHV-E140S", "SHW-M180W", "SHW-M305W", "SHW-M430W", "SPH-M580BST", "YP-G50", "GT-P7510" };
  }
  
  public static boolean CheckTelephonyDevice(String paramString) {
    if (paramString != null) {
      String[] arrayOfString = noTelephonyDeviceList;
      int j = arrayOfString.length;
      int i = 0;
      while (true) {
        if (i < j) {
          if (paramString.equals(arrayOfString[i]))
            return true; 
          i++;
          continue;
        } 
        return false;
      } 
    } 
    return false;
  }
  
  public static String formatSize(long paramLong) {
    return String.valueOf(String.format("%.2f", new Object[] { Double.valueOf(paramLong / 1024.0D / 1024.0D) })) + "MB";
  }
  
  public static String getAccessMethod() {
    String str = ACCESS_METHOD_MANUAL;
    return CMInfo.getInstance().getIsNFCLaunch() ? ACCESS_METHOD_NFC : ACCESS_METHOD_MANUAL;
  }
  
  public static long getAvailableExternalMemorySize() {
    if (isExternalMemoryAvailable()) {
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
  
  public static String getDefaultStorage() {
    return String.valueOf(Environment.getExternalStorageDirectory().getPath()) + "/DCIM/Camera/Samsung Smart Camera Application/MobileLink";
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
  
  public static String getThumbStorage() {
    return String.valueOf(getDefaultStorage()) + "/LazyList";
  }
  
  public static String getUserAgent() {
    return userAgent;
  }
  
  private static boolean isExternalMemoryAvailable() {
    return Environment.getExternalStorageState().equals("mounted");
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
  
  public static void setUseragent(Context paramContext) {
    // Byte code:
    //   0: new java/lang/StringBuffer
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore #4
    //   9: aload #4
    //   11: ldc_w 'SEC_RVF_ML_'
    //   14: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   17: pop
    //   18: aconst_null
    //   19: astore_1
    //   20: aconst_null
    //   21: astore_3
    //   22: aload_0
    //   23: ifnull -> 93
    //   26: aload_3
    //   27: astore_2
    //   28: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing.bNoTelephonyDevice : Z
    //   31: ifne -> 60
    //   34: aload_0
    //   35: ldc_w 'phone'
    //   38: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   41: checkcast android/telephony/TelephonyManager
    //   44: astore_1
    //   45: aload_3
    //   46: astore_2
    //   47: aload_1
    //   48: invokevirtual getSimState : ()I
    //   51: iconst_1
    //   52: if_icmpeq -> 60
    //   55: aload_1
    //   56: invokevirtual getLine1Number : ()Ljava/lang/String;
    //   59: astore_2
    //   60: aload_2
    //   61: ifnull -> 73
    //   64: aload_2
    //   65: astore_1
    //   66: aload_2
    //   67: invokevirtual length : ()I
    //   70: ifne -> 93
    //   73: aload_0
    //   74: ldc_w 'wifi'
    //   77: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   80: checkcast android/net/wifi/WifiManager
    //   83: invokevirtual getConnectionInfo : ()Landroid/net/wifi/WifiInfo;
    //   86: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   89: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   92: astore_1
    //   93: aload_1
    //   94: ifnull -> 106
    //   97: aload_1
    //   98: astore_0
    //   99: aload_1
    //   100: invokevirtual length : ()I
    //   103: ifne -> 110
    //   106: ldc_w 'UNKNOWN'
    //   109: astore_0
    //   110: aload #4
    //   112: aload_0
    //   113: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   116: pop
    //   117: aload #4
    //   119: invokevirtual toString : ()Ljava/lang/String;
    //   122: putstatic com/samsungimaging/connectionmanager/app/pullservice/demo/ml/Utils.userAgent : Ljava/lang/String;
    //   125: return
  }
  
  public static boolean supportDSCPrefix(String paramString) {
    if (paramString != null) {
      Iterator<String> iterator = dscPrefixList.iterator();
      while (true) {
        if (iterator.hasNext()) {
          if (paramString.contains(iterator.next()))
            return true; 
          continue;
        } 
        return false;
      } 
    } 
    return false;
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


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\ml\Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */