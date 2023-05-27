package com.samsungimaging.connectionmanager.app.pullservice.demo.rvf;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;
import com.samsungimaging.connectionmanager.util.Trace;
import java.util.regex.Pattern;

public class Utils {
  private static String ACCESS_METHOD_MANUAL;
  
  private static String ACCESS_METHOD_NFC;
  
  public static Trace.Tag TAG = Trace.Tag.RVF;
  
  static String[] existStatusBarDeviceForTablet;
  
  static String[] noTelephonyDeviceList;
  
  private static String userAgent = null;
  
  static {
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
    existStatusBarDeviceForTablet = new String[] { "SHW-M480", "GT-N8000", "SHW-M380", "GT-P7510", "GT-P7100", "SCH-I925U" };
  }
  
  public static boolean CheckLayoutLarge(int paramInt1, int paramInt2, int paramInt3) {
    Trace.d(TAG, "CheckLayoutLarge x : " + paramInt1 + " y : " + paramInt2 + " dpi : " + paramInt3);
    return ((paramInt1 == 600 && paramInt2 == 1024) || (paramInt1 == 1024 && paramInt2 == 600 && paramInt3 == 160));
  }
  
  public static boolean CheckOptimusView(int paramInt1, int paramInt2) {
    return ((paramInt1 == 1024 && paramInt2 == 768) || (paramInt1 == 768 && paramInt2 == 1024));
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
  
  public static boolean CheckexistStatusBarDeviceForTablet(String paramString) {
    if (paramString != null) {
      String[] arrayOfString = existStatusBarDeviceForTablet;
      int j = arrayOfString.length;
      int i = 0;
      while (true) {
        if (i < j) {
          if (paramString.contains(arrayOfString[i]))
            return true; 
          i++;
          continue;
        } 
        return false;
      } 
    } 
    return false;
  }
  
  public static int MaxValue(int paramInt1, int paramInt2) {
    return (paramInt1 > paramInt2) ? paramInt1 : paramInt2;
  }
  
  public static int MinValue(int paramInt1, int paramInt2) {
    return (paramInt1 < paramInt2) ? paramInt1 : paramInt2;
  }
  
  public static String getAccessMethod() {
    String str = ACCESS_METHOD_MANUAL;
    return CMInfo.getInstance().getIsNFCLaunch() ? ACCESS_METHOD_NFC : ACCESS_METHOD_MANUAL;
  }
  
  public static String getDefaultStorage() {
    return isNexusOne() ? (String.valueOf(Environment.getExternalStorageDirectory().getPath()) + "/sdcard/Camera/Samsung Smart Camera Application/RemoteViewfinder") : (String.valueOf(Environment.getExternalStorageDirectory().getPath()) + "/DCIM/Camera/Samsung Smart Camera Application/RemoteViewfinder");
  }
  
  public static boolean getDisplayConnGuide(Context paramContext) {
    return PreferenceManager.getDefaultSharedPreferences(paramContext).getBoolean("connguide", true);
  }
  
  public static String getMacAddress(Context paramContext) {
    String str2 = "";
    WifiInfo wifiInfo = ((WifiManager)paramContext.getSystemService("wifi")).getConnectionInfo();
    String str1 = str2;
    if (wifiInfo != null) {
      String str = wifiInfo.getMacAddress();
      str1 = str2;
      if (str != null)
        str1 = str; 
    } 
    return str1;
  }
  
  public static String getUserAgent() {
    Trace.d(TAG, "getUserAgent - userAgent : " + userAgent);
    return userAgent;
  }
  
  public static boolean isNexusOne() {
    return Build.MODEL.equals("Nexus One");
  }
  
  public static boolean isNumeric(String paramString) {
    return Pattern.compile("[+-]?\\d+").matcher(paramString).matches();
  }
  
  public static boolean isSCHi909() {
    return Build.MODEL.equals("SCH-i909");
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
  
  public static void setDisplayConnGuide(Context paramContext, boolean paramBoolean) {
    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(paramContext).edit();
    editor.putBoolean("connguide", paramBoolean);
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
    //   23: ifnull -> 124
    //   26: aload_3
    //   27: astore_2
    //   28: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.bNoTelephonyDevice : Z
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
    //   70: ifne -> 124
    //   73: aload_0
    //   74: ldc_w 'wifi'
    //   77: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   80: checkcast android/net/wifi/WifiManager
    //   83: invokevirtual getConnectionInfo : ()Landroid/net/wifi/WifiInfo;
    //   86: astore_0
    //   87: aload_2
    //   88: astore_1
    //   89: aload_0
    //   90: ifnull -> 124
    //   93: aload_0
    //   94: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   97: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   100: astore_1
    //   101: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/Utils.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   104: new java/lang/StringBuilder
    //   107: dup
    //   108: ldc_w 'addPostfix : '
    //   111: invokespecial <init> : (Ljava/lang/String;)V
    //   114: aload_1
    //   115: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   118: invokevirtual toString : ()Ljava/lang/String;
    //   121: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   124: aload_1
    //   125: ifnull -> 137
    //   128: aload_1
    //   129: astore_0
    //   130: aload_1
    //   131: invokevirtual length : ()I
    //   134: ifne -> 141
    //   137: ldc_w 'UNKNOWN'
    //   140: astore_0
    //   141: aload #4
    //   143: aload_0
    //   144: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   147: pop
    //   148: aload #4
    //   150: invokevirtual toString : ()Ljava/lang/String;
    //   153: putstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/Utils.userAgent : Ljava/lang/String;
    //   156: return
  }
  
  public static int stringToRatioType(String paramString) {
    byte b = 2;
    return paramString.equals("16:9") ? 1 : (paramString.equals("4:3") ? 2 : (paramString.equals("3:2") ? 3 : (paramString.equals("1:1") ? 4 : b)));
  }
  
  public static String unitChange(LiveShutter paramLiveShutter, double paramDouble1, double paramDouble2, int paramInt) {
    return ((paramDouble1 == 5472.0D && paramDouble2 == 3648.0D) || (paramDouble1 == 3648.0D && paramDouble2 == 5472.0D)) ? "20M" : (((paramDouble1 == 5472.0D && paramDouble2 == 3080.0D) || (paramDouble1 == 3080.0D && paramDouble2 == 5472.0D)) ? ((paramInt == 0) ? "W16M" : "16.9M") : (((paramDouble1 == 4608.0D && paramDouble2 == 3456.0D) || (paramDouble1 == 3456.0D && paramDouble2 == 4608.0D)) ? "16M" : (((paramDouble1 == 4608.0D && paramDouble2 == 3072.0D) || (paramDouble1 == 3072.0D && paramDouble2 == 4608.0D)) ? "P14M" : (((paramDouble1 == 3648.0D && paramDouble2 == 3648.0D) || (paramDouble1 == 3648.0D && paramDouble2 == 3648.0D)) ? ((paramInt == 0) ? "13M" : "13.3M") : (((paramDouble1 == 4608.0D && paramDouble2 == 2592.0D) || (paramDouble1 == 2592.0D && paramDouble2 == 4608.0D)) ? "W12M" : (((paramDouble1 == 4320.0D && paramDouble2 == 2432.0D) || (paramDouble1 == 2432.0D && paramDouble2 == 4320.0D)) ? "W10M" : (((paramDouble1 == 3648.0D && paramDouble2 == 2736.0D) || (paramDouble1 == 2736.0D && paramDouble2 == 3648.0D)) ? "10M" : (((paramDouble1 == 3888.0D && paramDouble2 == 2592.0D) || (paramDouble1 == 2592.0D && paramDouble2 == 3888.0D)) ? ((paramInt == 0) ? "10M" : "10.1M") : (((paramDouble1 == 4000.0D && paramDouble2 == 2248.0D) || (paramDouble1 == 2248.0D && paramDouble2 == 4000.0D)) ? "9M" : (((paramDouble1 == 2832.0D && paramDouble2 == 2832.0D) || (paramDouble1 == 2832.0D && paramDouble2 == 2832.0D)) ? "8M" : (((paramDouble1 == 3712.0D && paramDouble2 == 2088.0D) || (paramDouble1 == 2088.0D && paramDouble2 == 3712.0D)) ? ((paramInt == 0) ? "W7M" : "7.8M") : (((paramDouble1 == 2640.0D && paramDouble2 == 2640.0D) || (paramDouble1 == 2640.0D && paramDouble2 == 2640.0D)) ? "7M" : (((paramDouble1 == 2976.0D && paramDouble2 == 1984.0D) || (paramDouble1 == 1984.0D && paramDouble2 == 2976.0D)) ? ((paramInt == 0) ? "5M" : "5.9M") : (((paramDouble1 == 2592.0D && paramDouble2 == 1944.0D) || (paramDouble1 == 1944.0D && paramDouble2 == 2592.0D)) ? "5M" : (((paramDouble1 == 2944.0D && paramDouble2 == 1656.0D) || (paramDouble1 == 1656.0D && paramDouble2 == 2944.0D)) ? ((paramInt == 0) ? "W4M" : "4.9M") : (((paramDouble1 == 2000.0D && paramDouble2 == 2000.0D) || (paramDouble1 == 2000.0D && paramDouble2 == 2000.0D)) ? "4M" : (((paramDouble1 == 1984.0D && paramDouble2 == 1488.0D) || (paramDouble1 == 1488.0D && paramDouble2 == 1984.0D)) ? "3M" : (((paramDouble1 == 1920.0D && paramDouble2 == 1080.0D) || (paramDouble1 == 1080.0D && paramDouble2 == 1920.0D)) ? ((paramInt == 0) ? "W2M" : "2.1M") : (((paramDouble1 == 1728.0D && paramDouble2 == 1152.0D) || (paramDouble1 == 1152.0D && paramDouble2 == 1728.0D)) ? "2M" : (((paramDouble1 == 1024.0D && paramDouble2 == 1024.0D) || (paramDouble1 == 1024.0D && paramDouble2 == 1024.0D)) ? ((paramInt == 0) ? "1M" : "1.1M") : (((paramDouble1 == 1024.0D && paramDouble2 == 768.0D) || (paramDouble1 == 768.0D && paramDouble2 == 1024.0D)) ? "1M" : "10M")))))))))))))))))))));
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


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\rvf\Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */