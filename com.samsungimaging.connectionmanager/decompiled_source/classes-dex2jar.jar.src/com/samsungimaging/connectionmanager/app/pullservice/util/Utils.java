package com.samsungimaging.connectionmanager.app.pullservice.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Environment;
import com.samsungimaging.connectionmanager.util.Trace;
import java.io.IOException;
import java.util.regex.Pattern;

public class Utils {
  private static Trace.Tag TAG = Trace.Tag.RVF;
  
  private static String deg_to_dms(double paramDouble) {
    if (paramDouble < 0.0D)
      return ""; 
    int m = (int)paramDouble;
    paramDouble = (paramDouble - m) * 60.0D;
    int j = (int)paramDouble;
    paramDouble = roundDouble((paramDouble - j) * 60.0D, 2);
    int i = j;
    if (paramDouble == 60.0D)
      i = j + 1; 
    int k = m;
    j = i;
    if (i == 60) {
      k = m + 1;
      j = 0;
    } 
    return String.format("%d/1,%d/1,%.02f/1", new Object[] { Integer.valueOf(k), Integer.valueOf(j), Double.valueOf(paramDouble) });
  }
  
  public static String getAgent(Context paramContext, String paramString) {
    // Byte code:
    //   0: new java/lang/StringBuffer
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: astore_3
    //   8: aload_3
    //   9: aload_1
    //   10: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   13: pop
    //   14: aconst_null
    //   15: astore_1
    //   16: aconst_null
    //   17: astore_2
    //   18: aload_0
    //   19: ifnull -> 85
    //   22: aload_0
    //   23: ldc 'phone'
    //   25: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   28: checkcast android/telephony/TelephonyManager
    //   31: astore_1
    //   32: aload_1
    //   33: invokevirtual getSimState : ()I
    //   36: iconst_1
    //   37: if_icmpeq -> 45
    //   40: aload_1
    //   41: invokevirtual getLine1Number : ()Ljava/lang/String;
    //   44: astore_2
    //   45: aload_2
    //   46: ifnull -> 58
    //   49: aload_2
    //   50: astore_1
    //   51: aload_2
    //   52: invokevirtual length : ()I
    //   55: ifne -> 85
    //   58: aload_0
    //   59: ldc 'wifi'
    //   61: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   64: checkcast android/net/wifi/WifiManager
    //   67: invokevirtual getConnectionInfo : ()Landroid/net/wifi/WifiInfo;
    //   70: astore_0
    //   71: aload_2
    //   72: astore_1
    //   73: aload_0
    //   74: ifnull -> 85
    //   77: aload_0
    //   78: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   81: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   84: astore_1
    //   85: aload_1
    //   86: ifnull -> 98
    //   89: aload_1
    //   90: astore_0
    //   91: aload_1
    //   92: invokevirtual length : ()I
    //   95: ifne -> 101
    //   98: ldc 'UNKNOWN'
    //   100: astore_0
    //   101: aload_3
    //   102: aload_0
    //   103: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   106: pop
    //   107: aload_3
    //   108: invokevirtual toString : ()Ljava/lang/String;
    //   111: areturn
  }
  
  public static String getDefaultStorage() {
    return String.valueOf(Environment.getExternalStorageDirectory().getPath()) + "/DCIM/Camera/Samsung Smart Camera Application/RemoteViewfinder";
  }
  
  private static String getGPSValue(Location paramLocation) {
    String str1;
    int i = (int)(paramLocation.getLatitude() * 3600.0D);
    int j = (int)(paramLocation.getLongitude() * 3600.0D);
    if (i >= 0) {
      str1 = "N";
    } else {
      str1 = "S";
      i *= -1;
    } 
    if (j >= 0) {
      String str = "E";
      return String.valueOf(str1) + i + "X" + str + j;
    } 
    String str2 = "W";
    j *= -1;
    return String.valueOf(str1) + i + "X" + str2 + j;
  }
  
  public static String getLocation(LocationManager paramLocationManager) {
    Location location2 = paramLocationManager.getLastKnownLocation("gps");
    Location location1 = paramLocationManager.getLastKnownLocation("network");
    return (location2 != null) ? getGPSValue(location2) : ((location1 != null) ? getGPSValue(location1) : "UNKNOWN");
  }
  
  public static boolean isInstalledPackage(String paramString, Context paramContext) {
    PackageManager packageManager = paramContext.getPackageManager();
    try {
      packageManager.getApplicationInfo(paramString, 128);
      return true;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      nameNotFoundException.printStackTrace();
      return false;
    } 
  }
  
  public static boolean isNumeric(String paramString) {
    return Pattern.compile("[+-]?\\d+").matcher(paramString).matches();
  }
  
  private static double roundDouble(double paramDouble, int paramInt) {
    return Math.round(Math.pow(10.0D, paramInt) * paramDouble) / Math.pow(10.0D, paramInt);
  }
  
  public static void setGPSInfoOfExif(Location paramLocation, String paramString) {
    String str1;
    String str2;
    Trace.d(TAG, "start setGPSInfoOfExif()");
    int i = (int)(paramLocation.getLatitude() * 3600.0D);
    int j = (int)(paramLocation.getLongitude() * 3600.0D);
    if (i >= 0) {
      str1 = "N";
    } else {
      str1 = "S";
    } 
    if (j >= 0) {
      str2 = "E";
    } else {
      str2 = "W";
    } 
    try {
      ExifInterface exifInterface = new ExifInterface(paramString);
      try {
        if (exifInterface.getAttribute("GPSLatitude") == null) {
          exifInterface.setAttribute("GPSLatitudeRef", str1);
          exifInterface.setAttribute("GPSLongitudeRef", str2);
          exifInterface.setAttribute("GPSLatitude", deg_to_dms(paramLocation.getLatitude()));
          exifInterface.setAttribute("GPSLongitude", deg_to_dms(paramLocation.getLongitude()));
          Trace.d(TAG, "exif.saveAttributes()");
          exifInterface.saveAttributes();
          return;
        } 
      } catch (IOException null) {}
    } catch (IOException iOException) {}
    iOException.printStackTrace();
  }
  
  public static int toRatioType(String paramString) {
    byte b = 2;
    return paramString.equals("16:9") ? 1 : (paramString.equals("4:3") ? 2 : (paramString.equals("3:2") ? 3 : (paramString.equals("1:1") ? 4 : b)));
  }
  
  public static String unitChange(double paramDouble1, double paramDouble2) {
    return ((paramDouble1 == 5472.0D && paramDouble2 == 3648.0D) || (paramDouble1 == 3648.0D && paramDouble2 == 5472.0D)) ? "20M" : (((paramDouble1 == 5472.0D && paramDouble2 == 3080.0D) || (paramDouble1 == 3080.0D && paramDouble2 == 5472.0D)) ? "W16M" : (((paramDouble1 == 4608.0D && paramDouble2 == 3456.0D) || (paramDouble1 == 3456.0D && paramDouble2 == 4608.0D)) ? "16M" : (((paramDouble1 == 4608.0D && paramDouble2 == 3072.0D) || (paramDouble1 == 3072.0D && paramDouble2 == 4608.0D)) ? "P14M" : (((paramDouble1 == 3648.0D && paramDouble2 == 3648.0D) || (paramDouble1 == 3648.0D && paramDouble2 == 3648.0D)) ? "13M" : (((paramDouble1 == 4608.0D && paramDouble2 == 2592.0D) || (paramDouble1 == 2592.0D && paramDouble2 == 4608.0D)) ? "W12M" : (((paramDouble1 == 4320.0D && paramDouble2 == 2432.0D) || (paramDouble1 == 2432.0D && paramDouble2 == 4320.0D)) ? "W10M" : (((paramDouble1 == 3648.0D && paramDouble2 == 2736.0D) || (paramDouble1 == 2736.0D && paramDouble2 == 3648.0D)) ? "10M" : (((paramDouble1 == 3888.0D && paramDouble2 == 2592.0D) || (paramDouble1 == 2592.0D && paramDouble2 == 3888.0D)) ? "10M" : (((paramDouble1 == 4000.0D && paramDouble2 == 2248.0D) || (paramDouble1 == 2248.0D && paramDouble2 == 4000.0D)) ? "W9M" : (((paramDouble1 == 2832.0D && paramDouble2 == 2832.0D) || (paramDouble1 == 2832.0D && paramDouble2 == 2832.0D)) ? "8M" : (((paramDouble1 == 3712.0D && paramDouble2 == 2088.0D) || (paramDouble1 == 2088.0D && paramDouble2 == 3712.0D)) ? "W7M" : (((paramDouble1 == 2640.0D && paramDouble2 == 2640.0D) || (paramDouble1 == 2640.0D && paramDouble2 == 2640.0D)) ? "7M" : (((paramDouble1 == 2976.0D && paramDouble2 == 1984.0D) || (paramDouble1 == 1984.0D && paramDouble2 == 2976.0D)) ? "5M" : (((paramDouble1 == 2592.0D && paramDouble2 == 1944.0D) || (paramDouble1 == 1944.0D && paramDouble2 == 2592.0D)) ? "5M" : (((paramDouble1 == 2944.0D && paramDouble2 == 1656.0D) || (paramDouble1 == 1656.0D && paramDouble2 == 2944.0D)) ? "W4M" : (((paramDouble1 == 2000.0D && paramDouble2 == 2000.0D) || (paramDouble1 == 2000.0D && paramDouble2 == 2000.0D)) ? "4M" : (((paramDouble1 == 1984.0D && paramDouble2 == 1488.0D) || (paramDouble1 == 1488.0D && paramDouble2 == 1984.0D)) ? "3M" : (((paramDouble1 == 1920.0D && paramDouble2 == 1080.0D) || (paramDouble1 == 1080.0D && paramDouble2 == 1920.0D)) ? "W2M" : (((paramDouble1 == 1728.0D && paramDouble2 == 1152.0D) || (paramDouble1 == 1152.0D && paramDouble2 == 1728.0D)) ? "2M" : (((paramDouble1 == 1024.0D && paramDouble2 == 1024.0D) || (paramDouble1 == 1024.0D && paramDouble2 == 1024.0D)) ? "1M" : (((paramDouble1 == 1024.0D && paramDouble2 == 768.0D) || (paramDouble1 == 768.0D && paramDouble2 == 1024.0D)) ? "1M" : "10M")))))))))))))))))))));
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservic\\util\Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */