package com.samsungimaging.connectionmanager.app.cm.common;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import com.samsungimaging.connectionmanager.app.cm.connector.ConnectionHandler;
import com.samsungimaging.connectionmanager.app.cm.connector.securities.ConfigurationSecurity;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;
import com.samsungimaging.connectionmanager.manager.DatabaseManager;
import com.samsungimaging.connectionmanager.util.Trace;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CMUtil {
  private static HashMap<String, Integer> CONNECT_TRYING_COUNT;
  
  private static ArrayList<String> NOTSupported_dscPrefixList;
  
  private static ArrayList<String> dscPrefixList = null;
  
  private static ArrayList<String> dscPrefixListForRenewal;
  
  private static ArrayList<String> galaxySeriesPrefixList;
  
  private static ArrayList<String> lateDHCPRefreshList;
  
  private static ArrayList<String> olddscPrefixList;
  
  private static ArrayList<String> useAllChooseDialogList;
  
  static {
    NOTSupported_dscPrefixList = null;
    useAllChooseDialogList = null;
    lateDHCPRefreshList = null;
    olddscPrefixList = null;
    galaxySeriesPrefixList = null;
    dscPrefixListForRenewal = null;
    CONNECT_TRYING_COUNT = new HashMap<String, Integer>();
    dscPrefixListForRenewal = new ArrayList<String>();
    dscPrefixListForRenewal.add("NX MINI");
    dscPrefixListForRenewal.add("NX3000");
    dscPrefixListForRenewal.add("NX3300");
    dscPrefixList = new ArrayList<String>();
    dscPrefixList.add("AP_SSC_");
    dscPrefixList.add("GC_");
    dscPrefixList.add("EK_");
    dscPrefixList.add("SM_");
    NOTSupported_dscPrefixList = new ArrayList<String>();
    NOTSupported_dscPrefixList.add("WB150");
    NOTSupported_dscPrefixList.add("DV300");
    NOTSupported_dscPrefixList.add("DV500");
    NOTSupported_dscPrefixList.add("WB300");
    NOTSupported_dscPrefixList.add("ST200");
    NOTSupported_dscPrefixList.add("WB850");
    NOTSupported_dscPrefixList.add("NX20");
    NOTSupported_dscPrefixList.add("NX210");
    NOTSupported_dscPrefixList.add("NX1000");
    NOTSupported_dscPrefixList.add("EX2");
    NOTSupported_dscPrefixList.add("MV900");
    NOTSupported_dscPrefixList.add("QF30");
    useAllChooseDialogList = new ArrayList<String>();
    useAllChooseDialogList.add("LG-F180");
    lateDHCPRefreshList = new ArrayList<String>();
    lateDHCPRefreshList.add("LG-F100");
    lateDHCPRefreshList.add("LG-F200");
    lateDHCPRefreshList.add("LG-F320");
    lateDHCPRefreshList.add("LG-F240");
    olddscPrefixList = new ArrayList<String>();
    olddscPrefixList.add("WB150");
    olddscPrefixList.add("DV300");
    olddscPrefixList.add("DV500");
    olddscPrefixList.add("WB300");
    olddscPrefixList.add("ST200");
    olddscPrefixList.add("WB850");
    olddscPrefixList.add("NX20");
    olddscPrefixList.add("NX210");
    olddscPrefixList.add("NX1000");
    olddscPrefixList.add("EX2");
    olddscPrefixList.add("MV900");
    olddscPrefixList.add("QF30");
    olddscPrefixList.add("GC_");
    galaxySeriesPrefixList = new ArrayList<String>();
    galaxySeriesPrefixList.add("EK_");
    galaxySeriesPrefixList.add("SM_");
    galaxySeriesPrefixList.add("GC_");
    galaxySeriesPrefixList.add("AP_SSC_GC");
  }
  
  public static boolean NOTsupportDSCPrefix(String paramString) {
    paramString = convertToNOTQuotedString(paramString);
    if (paramString != null) {
      Iterator<String> iterator = NOTSupported_dscPrefixList.iterator();
      while (true) {
        if (iterator.hasNext()) {
          if (paramString.startsWith(iterator.next()))
            return true; 
          continue;
        } 
        return false;
      } 
    } 
    return false;
  }
  
  public static void addToManagedAPList(Context paramContext, String paramString) {
    DatabaseManager.putForAP(paramContext, convertToNOTQuotedString(paramString));
  }
  
  private static int changePriorityAndSave(WifiManager paramWifiManager) {
    List<WifiConfiguration> list = paramWifiManager.getConfiguredNetworks();
    sortByPriority(list);
    for (int i = 0;; i++) {
      if (i >= list.size()) {
        paramWifiManager.saveConfiguration();
        return list.size();
      } 
      WifiConfiguration wifiConfiguration = list.get(i);
      wifiConfiguration.priority = i;
      paramWifiManager.updateNetwork(wifiConfiguration);
    } 
  }
  
  public static String checkCClass(WifiManager paramWifiManager) {
    String str2 = Formatter.formatIpAddress((paramWifiManager.getDhcpInfo()).serverAddress);
    String str1 = paramWifiManager.getConnectionInfo().getSSID();
    Trace.d(CMConstants.TAG_NAME, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! connected_ssid : " + str1);
    Trace.d(CMConstants.TAG_NAME, "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! camera_ip : " + str2);
    str1 = str2.substring(0, str2.lastIndexOf("."));
    return str1.substring(str1.lastIndexOf(".") + 1);
  }
  
  public static boolean checkOldVersionSmartCameraApp(String paramString) {
    boolean bool3 = true;
    boolean bool2 = true;
    paramString = convertToNOTQuotedString(paramString);
    boolean bool1 = bool3;
    if (paramString != null) {
      bool1 = bool3;
      if (paramString.contains("AP_SSC_")) {
        paramString = paramString.split("_")[2];
        Iterator<String> iterator = dscPrefixListForRenewal.iterator();
        while (true) {
          if (!iterator.hasNext()) {
            bool1 = bool2;
          } else if (paramString.contains(iterator.next())) {
            bool1 = false;
          } else {
            continue;
          } 
          if (!CMInfo.getInstance().isOldCamera())
            bool1 = false; 
          Trace.d(CMConstants.TAG_NAME, "checkOldVersionSmartCameraApp, old camera is " + bool1);
          return bool1;
        } 
      } 
    } 
    Trace.d(CMConstants.TAG_NAME, "checkOldVersionSmartCameraApp, old camera is " + bool1);
    return bool1;
  }
  
  public static boolean connectToNewCameraSoftAP(Context paramContext, WifiManager paramWifiManager, ScanResult paramScanResult, ConnectionHandler paramConnectionHandler) {
    // Byte code:
    //   0: ldc com/samsungimaging/connectionmanager/app/cm/common/CMUtil
    //   2: monitorenter
    //   3: aload_0
    //   4: aload_2
    //   5: getfield SSID : Ljava/lang/String;
    //   8: invokestatic isManagedSSID : (Landroid/content/Context;Ljava/lang/String;)Z
    //   11: ifne -> 32
    //   14: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   17: ldc_w 'connectToNewCameraSoftAP, return false01!!'
    //   20: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   23: iconst_0
    //   24: istore #5
    //   26: ldc com/samsungimaging/connectionmanager/app/cm/common/CMUtil
    //   28: monitorexit
    //   29: iload #5
    //   31: ireturn
    //   32: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   35: ldc_w 'Performance Check Point : Connecting to Camera Soft AP'
    //   38: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   41: aload_1
    //   42: aload_2
    //   43: getfield SSID : Ljava/lang/String;
    //   46: invokestatic isAvailableConnect : (Landroid/net/wifi/WifiManager;Ljava/lang/String;)Z
    //   49: ifne -> 67
    //   52: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   55: ldc_w 'connectToNewCameraSoftAP, return false02!!, aleady connecting state.'
    //   58: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   61: iconst_0
    //   62: istore #5
    //   64: goto -> 26
    //   67: iconst_0
    //   68: istore #5
    //   70: aload_1
    //   71: aload_2
    //   72: invokestatic getWifiConfigurationWithScanResult : (Landroid/net/wifi/WifiManager;Landroid/net/wifi/ScanResult;)Landroid/net/wifi/WifiConfiguration;
    //   75: astore #9
    //   77: aload_2
    //   78: invokestatic isOpenNetwork : (Landroid/net/wifi/ScanResult;)Z
    //   81: istore #6
    //   83: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   86: new java/lang/StringBuilder
    //   89: dup
    //   90: ldc_w 'connectToNewCameraSoftAP, open_network = '
    //   93: invokespecial <init> : (Ljava/lang/String;)V
    //   96: iload #6
    //   98: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   101: invokevirtual toString : ()Ljava/lang/String;
    //   104: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   107: iload #6
    //   109: ifne -> 229
    //   112: aload #9
    //   114: astore #8
    //   116: aload #9
    //   118: ifnull -> 181
    //   121: aload #9
    //   123: getfield preSharedKey : Ljava/lang/String;
    //   126: ifnull -> 156
    //   129: aload #9
    //   131: astore #8
    //   133: aload #9
    //   135: getfield preSharedKey : Ljava/lang/String;
    //   138: ifnull -> 181
    //   141: aload #9
    //   143: astore #8
    //   145: aload #9
    //   147: getfield preSharedKey : Ljava/lang/String;
    //   150: invokevirtual isEmpty : ()Z
    //   153: ifeq -> 181
    //   156: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   159: ldc_w 'connectToNewCameraSof4tAP, scan result is security, BUT config is open!'
    //   162: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   165: aload_0
    //   166: aload_1
    //   167: aload_2
    //   168: getfield SSID : Ljava/lang/String;
    //   171: aload_2
    //   172: getfield BSSID : Ljava/lang/String;
    //   175: invokestatic removeNetworkConfig : (Landroid/content/Context;Landroid/net/wifi/WifiManager;Ljava/lang/String;Ljava/lang/String;)V
    //   178: aconst_null
    //   179: astore #8
    //   181: aload #8
    //   183: ifnonnull -> 307
    //   186: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   189: new java/lang/StringBuilder
    //   192: dup
    //   193: ldc_w 'connectToNewCameraSoftAP, config not exist, open_network = '
    //   196: invokespecial <init> : (Ljava/lang/String;)V
    //   199: iload #6
    //   201: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   204: invokevirtual toString : ()Ljava/lang/String;
    //   207: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   210: iload #6
    //   212: ifeq -> 293
    //   215: aload_0
    //   216: aload_1
    //   217: aload_2
    //   218: iconst_1
    //   219: aconst_null
    //   220: aconst_null
    //   221: invokestatic createConfigAndRequestConnection : (Landroid/content/Context;Landroid/net/wifi/WifiManager;Landroid/net/wifi/ScanResult;ZLandroid/net/wifi/WifiConfiguration;[Ljava/lang/CharSequence;)Z
    //   224: istore #5
    //   226: goto -> 26
    //   229: aload #9
    //   231: astore #8
    //   233: aload #9
    //   235: ifnull -> 181
    //   238: aload #9
    //   240: astore #8
    //   242: aload #9
    //   244: getfield preSharedKey : Ljava/lang/String;
    //   247: ifnull -> 181
    //   250: aload #9
    //   252: astore #8
    //   254: aload #9
    //   256: getfield preSharedKey : Ljava/lang/String;
    //   259: invokevirtual isEmpty : ()Z
    //   262: ifne -> 181
    //   265: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   268: ldc_w 'connectToNewCameraSoftAP, scan result is open, BUT config is security!'
    //   271: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   274: aload_0
    //   275: aload_1
    //   276: aload_2
    //   277: getfield SSID : Ljava/lang/String;
    //   280: aload_2
    //   281: getfield BSSID : Ljava/lang/String;
    //   284: invokestatic removeNetworkConfig : (Landroid/content/Context;Landroid/net/wifi/WifiManager;Ljava/lang/String;Ljava/lang/String;)V
    //   287: aconst_null
    //   288: astore #8
    //   290: goto -> 181
    //   293: aload_2
    //   294: aload_3
    //   295: invokestatic sendMessageForPassword : (Landroid/net/wifi/ScanResult;Lcom/samsungimaging/connectionmanager/app/cm/connector/ConnectionHandler;)V
    //   298: goto -> 26
    //   301: astore_0
    //   302: ldc com/samsungimaging/connectionmanager/app/cm/common/CMUtil
    //   304: monitorexit
    //   305: aload_0
    //   306: athrow
    //   307: getstatic android/os/Build.BRAND : Ljava/lang/String;
    //   310: astore #9
    //   312: getstatic android/os/Build.MANUFACTURER : Ljava/lang/String;
    //   315: astore #10
    //   317: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   320: new java/lang/StringBuilder
    //   323: dup
    //   324: ldc_w 'connectToNewCameraSoftAP, brand = '
    //   327: invokespecial <init> : (Ljava/lang/String;)V
    //   330: aload #9
    //   332: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   335: ldc_w ', manufacturer = '
    //   338: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   341: aload #10
    //   343: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   346: invokevirtual toString : ()Ljava/lang/String;
    //   349: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   352: aload #9
    //   354: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   357: ldc_w 'samsung'
    //   360: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   363: ifne -> 384
    //   366: aload #10
    //   368: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   371: ldc_w 'samsung'
    //   374: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   377: istore #7
    //   379: iload #7
    //   381: ifeq -> 671
    //   384: iconst_1
    //   385: istore #4
    //   387: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   390: ldc_w 'connectToNewCameraSoftAP, check SpecificFlags check!!!'
    //   393: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   396: ldc android/net/wifi/WifiConfiguration
    //   398: ldc_w 'samsungSpecificFlags'
    //   401: invokevirtual getField : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   404: aload #8
    //   406: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   409: checkcast java/util/BitSet
    //   412: invokevirtual toString : ()Ljava/lang/String;
    //   415: astore #9
    //   417: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   420: new java/lang/StringBuilder
    //   423: dup
    //   424: ldc_w 'connectToNewCameraSoftAP, samsungSpecificFlag check!!! = '
    //   427: invokespecial <init> : (Ljava/lang/String;)V
    //   430: aload #9
    //   432: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   435: invokevirtual toString : ()Ljava/lang/String;
    //   438: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   441: aload #9
    //   443: ldc_w '0'
    //   446: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   449: ifeq -> 484
    //   452: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   455: ldc_w 'connectToNewCameraSoftAP, samsungSpecificFlag check!!!, aleady set!!!'
    //   458: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   461: iconst_1
    //   462: istore #4
    //   464: iload #4
    //   466: ifeq -> 556
    //   469: aload_0
    //   470: aload_1
    //   471: aload_2
    //   472: iconst_0
    //   473: aload #8
    //   475: aconst_null
    //   476: invokestatic createConfigAndRequestConnection : (Landroid/content/Context;Landroid/net/wifi/WifiManager;Landroid/net/wifi/ScanResult;ZLandroid/net/wifi/WifiConfiguration;[Ljava/lang/CharSequence;)Z
    //   479: istore #5
    //   481: goto -> 26
    //   484: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   487: ldc_w 'connectToNewCameraSoftAP, samsungSpecificFlag check!!!, not set!!!'
    //   490: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   493: iconst_0
    //   494: istore #4
    //   496: goto -> 464
    //   499: astore #9
    //   501: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   504: ldc_w 'connectToNewCameraSoftAP, check SpecificFlags check!!!, NoSuchFieldException'
    //   507: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   510: aload #9
    //   512: invokevirtual printStackTrace : ()V
    //   515: goto -> 464
    //   518: astore #9
    //   520: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   523: ldc_w 'connectToNewCameraSoftAP, check SpecificFlags check!!!, IllegalArgumentException'
    //   526: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   529: aload #9
    //   531: invokevirtual printStackTrace : ()V
    //   534: goto -> 464
    //   537: astore #9
    //   539: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   542: ldc_w 'connectToNewCameraSoftAP, check SpecificFlags check!!!, IllegalAccessException'
    //   545: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   548: aload #9
    //   550: invokevirtual printStackTrace : ()V
    //   553: goto -> 464
    //   556: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   559: ldc_w 'connectToNewCameraSoftAP, config aleady exist, try remove config'
    //   562: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   565: aload_1
    //   566: aload #8
    //   568: getfield networkId : I
    //   571: invokevirtual removeNetwork : (I)Z
    //   574: ifeq -> 659
    //   577: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   580: ldc_w 'connectToNewCameraSoftAP, config aleady exist, try remove config, success.'
    //   583: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   586: aload_1
    //   587: invokevirtual saveConfiguration : ()Z
    //   590: ifne -> 608
    //   593: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   596: ldc_w 'connectToNewCameraSoftAP, config aleady exist, try remove config, saveConfig failed.'
    //   599: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   602: iconst_0
    //   603: istore #5
    //   605: goto -> 26
    //   608: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   611: new java/lang/StringBuilder
    //   614: dup
    //   615: ldc_w 'connectToNewCameraSoftAP, config aleady exist, try remove config, saveConfig success, open_network = '
    //   618: invokespecial <init> : (Ljava/lang/String;)V
    //   621: iload #6
    //   623: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   626: invokevirtual toString : ()Ljava/lang/String;
    //   629: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   632: iload #6
    //   634: ifeq -> 651
    //   637: aload_0
    //   638: aload_1
    //   639: aload_2
    //   640: iconst_1
    //   641: aconst_null
    //   642: aconst_null
    //   643: invokestatic createConfigAndRequestConnection : (Landroid/content/Context;Landroid/net/wifi/WifiManager;Landroid/net/wifi/ScanResult;ZLandroid/net/wifi/WifiConfiguration;[Ljava/lang/CharSequence;)Z
    //   646: istore #5
    //   648: goto -> 26
    //   651: aload_2
    //   652: aload_3
    //   653: invokestatic sendMessageForPassword : (Landroid/net/wifi/ScanResult;Lcom/samsungimaging/connectionmanager/app/cm/connector/ConnectionHandler;)V
    //   656: goto -> 26
    //   659: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   662: ldc_w 'connectToNewCameraSoftAP, config aleady exist, try remove config, failed.'
    //   665: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   668: goto -> 26
    //   671: aload_0
    //   672: aload_1
    //   673: aload_2
    //   674: iconst_0
    //   675: aload #8
    //   677: aconst_null
    //   678: invokestatic createConfigAndRequestConnection : (Landroid/content/Context;Landroid/net/wifi/WifiManager;Landroid/net/wifi/ScanResult;ZLandroid/net/wifi/WifiConfiguration;[Ljava/lang/CharSequence;)Z
    //   681: istore #5
    //   683: goto -> 26
    // Exception table:
    //   from	to	target	type
    //   3	23	301	finally
    //   32	61	301	finally
    //   70	107	301	finally
    //   121	129	301	finally
    //   133	141	301	finally
    //   145	156	301	finally
    //   156	178	301	finally
    //   186	210	301	finally
    //   215	226	301	finally
    //   242	250	301	finally
    //   254	287	301	finally
    //   293	298	301	finally
    //   307	379	301	finally
    //   387	461	499	java/lang/NoSuchFieldException
    //   387	461	518	java/lang/IllegalArgumentException
    //   387	461	537	java/lang/IllegalAccessException
    //   387	461	301	finally
    //   469	481	301	finally
    //   484	493	499	java/lang/NoSuchFieldException
    //   484	493	518	java/lang/IllegalArgumentException
    //   484	493	537	java/lang/IllegalAccessException
    //   484	493	301	finally
    //   501	515	301	finally
    //   520	534	301	finally
    //   539	553	301	finally
    //   556	602	301	finally
    //   608	632	301	finally
    //   637	648	301	finally
    //   651	656	301	finally
    //   659	668	301	finally
    //   671	683	301	finally
  }
  
  public static String convertToNOTQuotedString(String paramString) {
    String str = paramString;
    if (paramString != null) {
      str = paramString;
      if (!paramString.equals("")) {
        Matcher matcher = Pattern.compile("\".*\"").matcher(paramString);
        str = paramString;
        if (matcher.find())
          str = matcher.group().replace("\"", ""); 
      } 
    } 
    return str;
  }
  
  private static String convertToQuotedString(String paramString) {
    if (TextUtils.isEmpty(paramString))
      return ""; 
    int i = paramString.length() - 1;
    if (i > 0 && paramString.charAt(0) == '"') {
      String str = paramString;
      return (paramString.charAt(i) != '"') ? ("\"" + paramString + "\"") : str;
    } 
    return "\"" + paramString + "\"";
  }
  
  public static boolean createConfigAndRequestConnection(Context paramContext, WifiManager paramWifiManager, ScanResult paramScanResult, boolean paramBoolean, WifiConfiguration paramWifiConfiguration, CharSequence[] paramArrayOfCharSequence) {
    // Byte code:
    //   0: ldc com/samsungimaging/connectionmanager/app/cm/common/CMUtil
    //   2: monitorenter
    //   3: aconst_null
    //   4: astore #11
    //   6: aconst_null
    //   7: astore_0
    //   8: aconst_null
    //   9: astore #10
    //   11: aconst_null
    //   12: astore #9
    //   14: aload #5
    //   16: ifnull -> 160
    //   19: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   22: new java/lang/StringBuilder
    //   25: dup
    //   26: ldc_w 'createConfigAndRequestConnection, makeconfig = '
    //   29: invokespecial <init> : (Ljava/lang/String;)V
    //   32: iload_3
    //   33: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   36: ldc_w ', SSID = '
    //   39: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   42: aload #5
    //   44: iconst_0
    //   45: aaload
    //   46: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   49: ldc_w ', BSSID = '
    //   52: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   55: aload #5
    //   57: iconst_1
    //   58: aaload
    //   59: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   62: ldc_w ', security = '
    //   65: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   68: aload #5
    //   70: iconst_2
    //   71: aaload
    //   72: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   75: invokevirtual toString : ()Ljava/lang/String;
    //   78: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   81: aload #5
    //   83: iconst_3
    //   84: aaload
    //   85: ifnull -> 582
    //   88: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   91: new java/lang/StringBuilder
    //   94: dup
    //   95: ldc_w 'createConfigAndRequestConnection, makeconfig = '
    //   98: invokespecial <init> : (Ljava/lang/String;)V
    //   101: iload_3
    //   102: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   105: ldc_w ', password = *'
    //   108: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   111: invokevirtual toString : ()Ljava/lang/String;
    //   114: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   117: aload #5
    //   119: iconst_0
    //   120: aaload
    //   121: invokeinterface toString : ()Ljava/lang/String;
    //   126: astore #11
    //   128: aload #5
    //   130: iconst_1
    //   131: aaload
    //   132: invokeinterface toString : ()Ljava/lang/String;
    //   137: astore_0
    //   138: aload #5
    //   140: iconst_2
    //   141: aaload
    //   142: invokeinterface toString : ()Ljava/lang/String;
    //   147: astore #10
    //   149: aload #5
    //   151: iconst_3
    //   152: aaload
    //   153: invokeinterface toString : ()Ljava/lang/String;
    //   158: astore #9
    //   160: iload_3
    //   161: ifeq -> 1006
    //   164: aload_1
    //   165: invokestatic getMaxPriority : (Landroid/net/wifi/WifiManager;)I
    //   168: iconst_1
    //   169: iadd
    //   170: istore #7
    //   172: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   175: new java/lang/StringBuilder
    //   178: dup
    //   179: ldc_w 'createConfigAndRequestConnection, priority setting, newPri01 = '
    //   182: invokespecial <init> : (Ljava/lang/String;)V
    //   185: iload #7
    //   187: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   190: invokevirtual toString : ()Ljava/lang/String;
    //   193: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   196: iload #7
    //   198: istore #6
    //   200: iload #7
    //   202: ldc_w 99999
    //   205: if_icmple -> 214
    //   208: aload_1
    //   209: invokestatic changePriorityAndSave : (Landroid/net/wifi/WifiManager;)I
    //   212: istore #6
    //   214: new android/net/wifi/WifiConfiguration
    //   217: dup
    //   218: invokespecial <init> : ()V
    //   221: astore #4
    //   223: aload #5
    //   225: ifnull -> 620
    //   228: aload #4
    //   230: aload #11
    //   232: invokestatic convertToQuotedString : (Ljava/lang/String;)Ljava/lang/String;
    //   235: putfield SSID : Ljava/lang/String;
    //   238: aload #4
    //   240: aload_0
    //   241: putfield BSSID : Ljava/lang/String;
    //   244: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   247: new java/lang/StringBuilder
    //   250: dup
    //   251: ldc_w 'createConfigAndRequestConnection, config.SSID = '
    //   254: invokespecial <init> : (Ljava/lang/String;)V
    //   257: aload #11
    //   259: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   262: ldc_w ', config.BSSID = '
    //   265: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   268: aload_0
    //   269: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   272: invokevirtual toString : ()Ljava/lang/String;
    //   275: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   278: aload #4
    //   280: iload #6
    //   282: putfield priority : I
    //   285: aload #4
    //   287: getfield allowedAuthAlgorithms : Ljava/util/BitSet;
    //   290: invokevirtual clear : ()V
    //   293: aload #4
    //   295: getfield allowedGroupCiphers : Ljava/util/BitSet;
    //   298: invokevirtual clear : ()V
    //   301: aload #4
    //   303: getfield allowedKeyManagement : Ljava/util/BitSet;
    //   306: invokevirtual clear : ()V
    //   309: aload #4
    //   311: getfield allowedPairwiseCiphers : Ljava/util/BitSet;
    //   314: invokevirtual clear : ()V
    //   317: aload #4
    //   319: getfield allowedProtocols : Ljava/util/BitSet;
    //   322: invokevirtual clear : ()V
    //   325: aload #9
    //   327: ifnonnull -> 683
    //   330: iconst_0
    //   331: istore #6
    //   333: aload #10
    //   335: ifnull -> 868
    //   338: aload #10
    //   340: ldc_w 'WEP'
    //   343: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   346: ifeq -> 728
    //   349: aload #4
    //   351: getfield allowedKeyManagement : Ljava/util/BitSet;
    //   354: iconst_0
    //   355: invokevirtual set : (I)V
    //   358: aload #4
    //   360: getfield allowedAuthAlgorithms : Ljava/util/BitSet;
    //   363: iconst_0
    //   364: invokevirtual set : (I)V
    //   367: aload #4
    //   369: getfield allowedAuthAlgorithms : Ljava/util/BitSet;
    //   372: iconst_1
    //   373: invokevirtual set : (I)V
    //   376: iload #6
    //   378: ifeq -> 422
    //   381: iload #6
    //   383: bipush #10
    //   385: if_icmpeq -> 402
    //   388: iload #6
    //   390: bipush #26
    //   392: if_icmpeq -> 402
    //   395: iload #6
    //   397: bipush #58
    //   399: if_icmpne -> 693
    //   402: aload #9
    //   404: ldc_w '[0-9A-Fa-f]*'
    //   407: invokevirtual matches : (Ljava/lang/String;)Z
    //   410: ifeq -> 693
    //   413: aload #4
    //   415: getfield wepKeys : [Ljava/lang/String;
    //   418: iconst_0
    //   419: aload #9
    //   421: aastore
    //   422: getstatic android/os/Build.BRAND : Ljava/lang/String;
    //   425: astore_0
    //   426: getstatic android/os/Build.MANUFACTURER : Ljava/lang/String;
    //   429: astore_2
    //   430: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   433: new java/lang/StringBuilder
    //   436: dup
    //   437: ldc_w 'createConfigAndRequestConnection, brand = '
    //   440: invokespecial <init> : (Ljava/lang/String;)V
    //   443: aload_0
    //   444: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   447: ldc_w ', manufacturer = '
    //   450: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   453: aload_2
    //   454: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   457: invokevirtual toString : ()Ljava/lang/String;
    //   460: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   463: aload_0
    //   464: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   467: ldc_w 'samsung'
    //   470: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   473: ifne -> 491
    //   476: aload_2
    //   477: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   480: ldc_w 'samsung'
    //   483: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   486: istore_3
    //   487: iload_3
    //   488: ifeq -> 545
    //   491: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   494: ldc_w 'createConfigAndRequestConnection, check SpecificFlags'
    //   497: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   500: ldc android/net/wifi/WifiConfiguration
    //   502: ldc_w 'samsungSpecificFlags'
    //   505: invokevirtual getField : (Ljava/lang/String;)Ljava/lang/reflect/Field;
    //   508: aload #4
    //   510: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   513: checkcast java/util/BitSet
    //   516: astore_0
    //   517: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   520: new java/lang/StringBuilder
    //   523: dup
    //   524: ldc_w 'createConfigAndRequestConnection, samsungSpecificFlag = '
    //   527: invokespecial <init> : (Ljava/lang/String;)V
    //   530: aload_0
    //   531: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   534: invokevirtual toString : ()Ljava/lang/String;
    //   537: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   540: aload_0
    //   541: iconst_0
    //   542: invokevirtual set : (I)V
    //   545: iconst_m1
    //   546: istore #6
    //   548: aload_1
    //   549: aload #4
    //   551: invokevirtual addNetwork : (Landroid/net/wifi/WifiConfiguration;)I
    //   554: istore #7
    //   556: iload #7
    //   558: istore #6
    //   560: iload #6
    //   562: iconst_m1
    //   563: if_icmpne -> 931
    //   566: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   569: ldc_w 'createConfigAndRequestConnection, return false01'
    //   572: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   575: iconst_0
    //   576: istore_3
    //   577: ldc com/samsungimaging/connectionmanager/app/cm/common/CMUtil
    //   579: monitorexit
    //   580: iload_3
    //   581: ireturn
    //   582: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   585: new java/lang/StringBuilder
    //   588: dup
    //   589: ldc_w 'createConfigAndRequestConnection, makeconfig = '
    //   592: invokespecial <init> : (Ljava/lang/String;)V
    //   595: iload_3
    //   596: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   599: ldc_w ', password = null'
    //   602: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   605: invokevirtual toString : ()Ljava/lang/String;
    //   608: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   611: goto -> 117
    //   614: astore_0
    //   615: ldc com/samsungimaging/connectionmanager/app/cm/common/CMUtil
    //   617: monitorexit
    //   618: aload_0
    //   619: athrow
    //   620: aload #4
    //   622: aload_2
    //   623: getfield SSID : Ljava/lang/String;
    //   626: invokestatic convertToQuotedString : (Ljava/lang/String;)Ljava/lang/String;
    //   629: putfield SSID : Ljava/lang/String;
    //   632: aload #4
    //   634: aload_2
    //   635: getfield BSSID : Ljava/lang/String;
    //   638: putfield BSSID : Ljava/lang/String;
    //   641: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   644: new java/lang/StringBuilder
    //   647: dup
    //   648: ldc_w 'createConfigAndRequestConnection, config.SSID = '
    //   651: invokespecial <init> : (Ljava/lang/String;)V
    //   654: aload_2
    //   655: getfield SSID : Ljava/lang/String;
    //   658: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   661: ldc_w ', config.BSSID = '
    //   664: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   667: aload_2
    //   668: getfield BSSID : Ljava/lang/String;
    //   671: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   674: invokevirtual toString : ()Ljava/lang/String;
    //   677: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   680: goto -> 278
    //   683: aload #9
    //   685: invokevirtual length : ()I
    //   688: istore #6
    //   690: goto -> 333
    //   693: aload #4
    //   695: getfield wepKeys : [Ljava/lang/String;
    //   698: iconst_0
    //   699: new java/lang/StringBuilder
    //   702: dup
    //   703: bipush #34
    //   705: invokestatic valueOf : (C)Ljava/lang/String;
    //   708: invokespecial <init> : (Ljava/lang/String;)V
    //   711: aload #9
    //   713: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   716: bipush #34
    //   718: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   721: invokevirtual toString : ()Ljava/lang/String;
    //   724: aastore
    //   725: goto -> 422
    //   728: aload #10
    //   730: ldc_w 'PSK'
    //   733: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   736: ifeq -> 824
    //   739: aload #4
    //   741: getfield allowedAuthAlgorithms : Ljava/util/BitSet;
    //   744: iconst_0
    //   745: invokevirtual set : (I)V
    //   748: aload #4
    //   750: getfield allowedProtocols : Ljava/util/BitSet;
    //   753: iconst_1
    //   754: invokevirtual set : (I)V
    //   757: aload #4
    //   759: getfield allowedKeyManagement : Ljava/util/BitSet;
    //   762: iconst_1
    //   763: invokevirtual set : (I)V
    //   766: aload #4
    //   768: getfield allowedPairwiseCiphers : Ljava/util/BitSet;
    //   771: iconst_2
    //   772: invokevirtual set : (I)V
    //   775: aload #4
    //   777: getfield allowedPairwiseCiphers : Ljava/util/BitSet;
    //   780: iconst_1
    //   781: invokevirtual set : (I)V
    //   784: aload #4
    //   786: getfield allowedGroupCiphers : Ljava/util/BitSet;
    //   789: iconst_3
    //   790: invokevirtual set : (I)V
    //   793: aload #4
    //   795: getfield allowedGroupCiphers : Ljava/util/BitSet;
    //   798: iconst_2
    //   799: invokevirtual set : (I)V
    //   802: aload #4
    //   804: ldc_w '"'
    //   807: aload #9
    //   809: invokevirtual concat : (Ljava/lang/String;)Ljava/lang/String;
    //   812: ldc_w '"'
    //   815: invokevirtual concat : (Ljava/lang/String;)Ljava/lang/String;
    //   818: putfield preSharedKey : Ljava/lang/String;
    //   821: goto -> 422
    //   824: aload #10
    //   826: ldc_w 'EAP'
    //   829: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   832: ifeq -> 856
    //   835: aload #4
    //   837: getfield allowedKeyManagement : Ljava/util/BitSet;
    //   840: iconst_2
    //   841: invokevirtual set : (I)V
    //   844: aload #4
    //   846: getfield allowedKeyManagement : Ljava/util/BitSet;
    //   849: iconst_3
    //   850: invokevirtual set : (I)V
    //   853: goto -> 422
    //   856: aload #4
    //   858: getfield allowedKeyManagement : Ljava/util/BitSet;
    //   861: iconst_0
    //   862: invokevirtual set : (I)V
    //   865: goto -> 422
    //   868: aload #4
    //   870: getfield allowedKeyManagement : Ljava/util/BitSet;
    //   873: iconst_0
    //   874: invokevirtual set : (I)V
    //   877: goto -> 422
    //   880: astore_0
    //   881: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   884: ldc_w 'createConfigAndRequestConnection, check SpecificFlags NoSuchFieldException'
    //   887: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   890: aload_0
    //   891: invokevirtual printStackTrace : ()V
    //   894: goto -> 545
    //   897: astore_0
    //   898: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   901: ldc_w 'createConfigAndRequestConnection, check SpecificFlags IllegalArgumentException'
    //   904: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   907: aload_0
    //   908: invokevirtual printStackTrace : ()V
    //   911: goto -> 545
    //   914: astore_0
    //   915: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   918: ldc_w 'createConfigAndRequestConnection, check SpecificFlags IllegalAccessException'
    //   921: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   924: aload_0
    //   925: invokevirtual printStackTrace : ()V
    //   928: goto -> 545
    //   931: aload_1
    //   932: invokevirtual saveConfiguration : ()Z
    //   935: ifne -> 952
    //   938: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   941: ldc_w 'createConfigAndRequestConnection, return false02'
    //   944: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   947: iconst_0
    //   948: istore_3
    //   949: goto -> 577
    //   952: aload_1
    //   953: aload #4
    //   955: invokestatic getWifiConfigurationWithWifiConfiguration : (Landroid/net/wifi/WifiManager;Landroid/net/wifi/WifiConfiguration;)Landroid/net/wifi/WifiConfiguration;
    //   958: ifnonnull -> 975
    //   961: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   964: ldc_w 'createConfigAndRequestConnection, return false03'
    //   967: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   970: iconst_0
    //   971: istore_3
    //   972: goto -> 577
    //   975: aload_1
    //   976: iload #6
    //   978: iconst_1
    //   979: invokevirtual enableNetwork : (IZ)Z
    //   982: ifeq -> 1259
    //   985: aload_1
    //   986: invokevirtual reconnect : ()Z
    //   989: ifne -> 1268
    //   992: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   995: ldc_w 'createConfigAndRequestConnection, return false04'
    //   998: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1001: iconst_0
    //   1002: istore_3
    //   1003: goto -> 577
    //   1006: aload #4
    //   1008: ifnull -> 1259
    //   1011: aload #4
    //   1013: getfield priority : I
    //   1016: istore #8
    //   1018: aload_1
    //   1019: invokestatic getMaxPriority : (Landroid/net/wifi/WifiManager;)I
    //   1022: iconst_1
    //   1023: iadd
    //   1024: istore #7
    //   1026: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1029: new java/lang/StringBuilder
    //   1032: dup
    //   1033: ldc_w 'createConfigAndRequestConnection, priority setting, newPri02 = '
    //   1036: invokespecial <init> : (Ljava/lang/String;)V
    //   1039: iload #7
    //   1041: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1044: invokevirtual toString : ()Ljava/lang/String;
    //   1047: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1050: iload #7
    //   1052: istore #6
    //   1054: aload #4
    //   1056: astore_0
    //   1057: iload #7
    //   1059: ldc_w 99999
    //   1062: if_icmple -> 1098
    //   1065: aload_1
    //   1066: invokestatic changePriorityAndSave : (Landroid/net/wifi/WifiManager;)I
    //   1069: istore #6
    //   1071: aload_1
    //   1072: aload #4
    //   1074: invokestatic getWifiConfigurationWithWifiConfiguration : (Landroid/net/wifi/WifiManager;Landroid/net/wifi/WifiConfiguration;)Landroid/net/wifi/WifiConfiguration;
    //   1077: astore_2
    //   1078: aload_2
    //   1079: astore_0
    //   1080: aload_2
    //   1081: ifnonnull -> 1098
    //   1084: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1087: ldc_w 'createConfigAndRequestConnection, priority setting, fail00'
    //   1090: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1093: iconst_0
    //   1094: istore_3
    //   1095: goto -> 577
    //   1098: aload_0
    //   1099: iload #6
    //   1101: putfield priority : I
    //   1104: aload_1
    //   1105: aload_0
    //   1106: invokevirtual updateNetwork : (Landroid/net/wifi/WifiConfiguration;)I
    //   1109: istore #6
    //   1111: iload #6
    //   1113: iconst_m1
    //   1114: if_icmpne -> 1131
    //   1117: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1120: ldc_w 'createConfigAndRequestConnection, priority setting, fail01'
    //   1123: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1126: iconst_0
    //   1127: istore_3
    //   1128: goto -> 577
    //   1131: aload_1
    //   1132: iload #6
    //   1134: iconst_0
    //   1135: invokevirtual enableNetwork : (IZ)Z
    //   1138: ifne -> 1161
    //   1141: aload_0
    //   1142: iload #8
    //   1144: putfield priority : I
    //   1147: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1150: ldc_w 'createConfigAndRequestConnection, priority setting, fail02'
    //   1153: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1156: iconst_0
    //   1157: istore_3
    //   1158: goto -> 577
    //   1161: aload_1
    //   1162: invokevirtual saveConfiguration : ()Z
    //   1165: ifne -> 1188
    //   1168: aload_0
    //   1169: iload #8
    //   1171: putfield priority : I
    //   1174: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1177: ldc_w 'createConfigAndRequestConnection, priority setting, fail03'
    //   1180: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1183: iconst_0
    //   1184: istore_3
    //   1185: goto -> 577
    //   1188: aload_1
    //   1189: aload_0
    //   1190: invokestatic getWifiConfigurationWithWifiConfiguration : (Landroid/net/wifi/WifiManager;Landroid/net/wifi/WifiConfiguration;)Landroid/net/wifi/WifiConfiguration;
    //   1193: astore_0
    //   1194: aload_0
    //   1195: ifnonnull -> 1212
    //   1198: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1201: ldc_w 'createConfigAndRequestConnection, priority setting, fail04'
    //   1204: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1207: iconst_0
    //   1208: istore_3
    //   1209: goto -> 577
    //   1212: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1215: ldc_w 'createConfigAndRequestConnection, not SupplicantState.COMPLETED!!!'
    //   1218: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1221: aload_1
    //   1222: aload_0
    //   1223: getfield networkId : I
    //   1226: iconst_1
    //   1227: invokevirtual enableNetwork : (IZ)Z
    //   1230: ifeq -> 1259
    //   1233: aload_1
    //   1234: invokevirtual reconnect : ()Z
    //   1237: ifne -> 1254
    //   1240: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1243: ldc_w 'createConfigAndRequestConnection, return false02'
    //   1246: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1249: iconst_0
    //   1250: istore_3
    //   1251: goto -> 577
    //   1254: iconst_1
    //   1255: istore_3
    //   1256: goto -> 577
    //   1259: iconst_0
    //   1260: istore_3
    //   1261: goto -> 577
    //   1264: astore_0
    //   1265: goto -> 560
    //   1268: iconst_1
    //   1269: istore_3
    //   1270: goto -> 577
    // Exception table:
    //   from	to	target	type
    //   19	81	614	finally
    //   88	117	614	finally
    //   117	160	614	finally
    //   164	196	614	finally
    //   208	214	614	finally
    //   214	223	614	finally
    //   228	278	614	finally
    //   278	325	614	finally
    //   338	376	614	finally
    //   402	422	614	finally
    //   422	487	614	finally
    //   491	545	880	java/lang/NoSuchFieldException
    //   491	545	897	java/lang/IllegalArgumentException
    //   491	545	914	java/lang/IllegalAccessException
    //   491	545	614	finally
    //   548	556	1264	java/lang/NullPointerException
    //   548	556	614	finally
    //   566	575	614	finally
    //   582	611	614	finally
    //   620	680	614	finally
    //   683	690	614	finally
    //   693	725	614	finally
    //   728	821	614	finally
    //   824	853	614	finally
    //   856	865	614	finally
    //   868	877	614	finally
    //   881	894	614	finally
    //   898	911	614	finally
    //   915	928	614	finally
    //   931	947	614	finally
    //   952	970	614	finally
    //   975	1001	614	finally
    //   1011	1050	614	finally
    //   1065	1078	614	finally
    //   1084	1093	614	finally
    //   1098	1111	614	finally
    //   1117	1126	614	finally
    //   1131	1156	614	finally
    //   1161	1183	614	finally
    //   1188	1194	614	finally
    //   1198	1207	614	finally
    //   1212	1249	614	finally
  }
  
  public static void disableConnectedCamera(WifiManager paramWifiManager) {
    if (paramWifiManager.isWifiEnabled() && paramWifiManager.getWifiState() != 0) {
      WifiInfo wifiInfo = paramWifiManager.getConnectionInfo();
      String str = wifiInfo.getSSID();
      if (str != null && !str.equals("") && supportDSCPrefix(str)) {
        downPriority(paramWifiManager);
        paramWifiManager.disableNetwork(wifiInfo.getNetworkId());
      } 
    } 
  }
  
  public static void disalbleCameraAps(Context paramContext, String paramString) {
    Iterator<WifiConfiguration> iterator;
    WifiConfiguration wifiConfiguration;
    WifiManager wifiManager = (WifiManager)paramContext.getSystemService("wifi");
    List list = wifiManager.getConfiguredNetworks();
    paramString = convertToNOTQuotedString(paramString);
    Trace.d(CMConstants.TAG_NAME, "disalbleCameraAps, ssid_temp = " + paramString);
    if (paramString == null) {
      if (list != null) {
        iterator = list.iterator();
        while (true) {
          if (iterator.hasNext()) {
            wifiConfiguration = iterator.next();
            if (supportDSCPrefix(wifiConfiguration.SSID)) {
              wifiManager.disableNetwork(wifiConfiguration.networkId);
              continue;
            } 
            wifiManager.enableNetwork(wifiConfiguration.networkId, false);
            continue;
          } 
          return;
        } 
      } 
      return;
    } 
    if (wifiConfiguration != null) {
      Iterator<WifiConfiguration> iterator1 = wifiConfiguration.iterator();
      while (true) {
        if (iterator1.hasNext()) {
          WifiConfiguration wifiConfiguration1 = iterator1.next();
          if (iterator.equals(convertToNOTQuotedString(wifiConfiguration1.SSID))) {
            Trace.d(CMConstants.TAG_NAME, "disalbleCameraAps, wifiConfiguration.SSID = " + wifiConfiguration1.SSID + ", wifiConfiguration.status = " + wifiConfiguration1.status);
            wifiManager.disableNetwork(wifiConfiguration1.networkId);
          } 
          continue;
        } 
        return;
      } 
    } 
  }
  
  public static void downPriority(WifiManager paramWifiManager) {
    WifiInfo wifiInfo = paramWifiManager.getConnectionInfo();
    List<WifiConfiguration> list = paramWifiManager.getConfiguredNetworks();
    Trace.d(CMConstants.TAG_NAME, "downPriority");
    if (list != null && wifiInfo != null) {
      Trace.d(CMConstants.TAG_NAME, "downPriority,  configurations.size() = " + list.size());
      int i = 0;
      while (true) {
        if (i < list.size()) {
          if (((WifiConfiguration)list.get(i)).SSID.equals(wifiInfo.getSSID()) || ((WifiConfiguration)list.get(i)).SSID.replace("\"", "").equals(wifiInfo.getSSID())) {
            Trace.d(CMConstants.TAG_NAME, "downPriority,  configurations.get(i).SSID = " + ((WifiConfiguration)list.get(i)).SSID + ", wifiInfo.getSSID() = " + wifiInfo.getSSID());
            ((WifiConfiguration)list.get(i)).priority = 0;
            paramWifiManager.updateNetwork(list.get(i));
            paramWifiManager.saveConfiguration();
            return;
          } 
          i++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public static void finishSubServicew(Context paramContext) {
    String str = whatIsTopActivity(paramContext);
    if (!str.equals("com.samsungimaging.connectionmanager.app.pullservice.MobileLink") && !str.equals("com.samsungimaging.connectionmanager.app.pullservice.RemoteViewFinder") && !str.equals("com.samsungimaging.connectionmanager.app.pushservice.autoshare.AutoShare"))
      str.equals("com.samsungimaging.connectionmanager.app.pushservice.selectivepush.SelectivePush"); 
  }
  
  public static int getAppTypeFromCamera(WifiManager paramWifiManager) {
    // Byte code:
    //   0: ldc com/samsungimaging/connectionmanager/app/cm/common/CMUtil
    //   2: monitorenter
    //   3: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   6: ldc_w 'getAppTypeFromCamera()'
    //   9: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   12: iconst_m1
    //   13: istore_1
    //   14: aload_0
    //   15: invokestatic checkCClass : (Landroid/net/wifi/WifiManager;)Ljava/lang/String;
    //   18: astore_0
    //   19: ldc_w '101'
    //   22: aload_0
    //   23: invokevirtual equals : (Ljava/lang/Object;)Z
    //   26: istore_2
    //   27: iload_2
    //   28: ifeq -> 38
    //   31: iconst_1
    //   32: istore_1
    //   33: ldc com/samsungimaging/connectionmanager/app/cm/common/CMUtil
    //   35: monitorexit
    //   36: iload_1
    //   37: ireturn
    //   38: ldc_w '102'
    //   41: aload_0
    //   42: invokevirtual equals : (Ljava/lang/Object;)Z
    //   45: ifeq -> 53
    //   48: iconst_2
    //   49: istore_1
    //   50: goto -> 33
    //   53: ldc_w '103'
    //   56: aload_0
    //   57: invokevirtual equals : (Ljava/lang/Object;)Z
    //   60: ifeq -> 68
    //   63: iconst_3
    //   64: istore_1
    //   65: goto -> 33
    //   68: ldc_w '104'
    //   71: aload_0
    //   72: invokevirtual equals : (Ljava/lang/Object;)Z
    //   75: ifeq -> 83
    //   78: iconst_4
    //   79: istore_1
    //   80: goto -> 33
    //   83: ldc_w '107'
    //   86: aload_0
    //   87: invokevirtual equals : (Ljava/lang/Object;)Z
    //   90: istore_2
    //   91: iload_2
    //   92: ifeq -> 33
    //   95: bipush #8
    //   97: istore_1
    //   98: goto -> 33
    //   101: astore_0
    //   102: aload_0
    //   103: invokevirtual printStackTrace : ()V
    //   106: goto -> 33
    //   109: astore_0
    //   110: ldc com/samsungimaging/connectionmanager/app/cm/common/CMUtil
    //   112: monitorexit
    //   113: aload_0
    //   114: athrow
    // Exception table:
    //   from	to	target	type
    //   3	12	109	finally
    //   14	27	101	java/lang/Exception
    //   14	27	109	finally
    //   38	48	101	java/lang/Exception
    //   38	48	109	finally
    //   53	63	101	java/lang/Exception
    //   53	63	109	finally
    //   68	78	101	java/lang/Exception
    //   68	78	109	finally
    //   83	91	101	java/lang/Exception
    //   83	91	109	finally
    //   102	106	109	finally
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
  
  private static int getMaxPriority(WifiManager paramWifiManager) {
    List list = paramWifiManager.getConfiguredNetworks();
    int i = 0;
    Iterator<WifiConfiguration> iterator = list.iterator();
    while (true) {
      if (!iterator.hasNext())
        return i; 
      WifiConfiguration wifiConfiguration = iterator.next();
      if (wifiConfiguration.priority > i)
        i = wifiConfiguration.priority; 
    } 
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
  
  public static WifiConfiguration getWifiConfigurationWithScanResult(WifiManager paramWifiManager, ScanResult paramScanResult) {
    String str2 = convertToQuotedString(paramScanResult.SSID);
    String str1 = paramScanResult.BSSID;
    List list = paramWifiManager.getConfiguredNetworks();
    if (str2 == null || str1 == null || list == null)
      return null; 
    Iterator<WifiConfiguration> iterator = list.iterator();
    while (true) {
      if (!iterator.hasNext())
        return null; 
      WifiConfiguration wifiConfiguration = iterator.next();
      if (wifiConfiguration.SSID != null && str2.equals(wifiConfiguration.SSID) && wifiConfiguration.SSID != null && str2.equals(wifiConfiguration.SSID)) {
        if (wifiConfiguration.BSSID == null) {
          Trace.d(CMConstants.TAG_NAME, "getWifiConfigurationWithScanResult, config.BSSID is null");
          return wifiConfiguration;
        } 
        if (str1.equals(wifiConfiguration.BSSID)) {
          Trace.d(CMConstants.TAG_NAME, "getWifiConfigurationWithScanResult, config.BSSID = " + wifiConfiguration.BSSID);
          return wifiConfiguration;
        } 
      } 
    } 
  }
  
  public static WifiConfiguration getWifiConfigurationWithScanResult(WifiManager paramWifiManager, String paramString1, String paramString2) {
    paramString1 = convertToQuotedString(paramString1);
    Iterator<WifiConfiguration> iterator = paramWifiManager.getConfiguredNetworks().iterator();
    while (true) {
      if (!iterator.hasNext())
        return null; 
      WifiConfiguration wifiConfiguration = iterator.next();
      if (wifiConfiguration.SSID != null && paramString1.equals(wifiConfiguration.SSID))
        return wifiConfiguration; 
    } 
  }
  
  private static WifiConfiguration getWifiConfigurationWithWifiConfiguration(WifiManager paramWifiManager, WifiConfiguration paramWifiConfiguration) {
    String str2 = paramWifiConfiguration.SSID;
    String str1 = paramWifiConfiguration.BSSID;
    List list = paramWifiManager.getConfiguredNetworks();
    if (str2 == null || list == null) {
      Trace.d(CMConstants.TAG_NAME, "getWifiConfigurationWithWifiConfiguration, return null01");
      return null;
    } 
    Iterator<WifiConfiguration> iterator = list.iterator();
    while (true) {
      if (!iterator.hasNext()) {
        Trace.d(CMConstants.TAG_NAME, "getWifiConfigurationWithWifiConfiguration, return null02");
        return null;
      } 
      WifiConfiguration wifiConfiguration = iterator.next();
      if (wifiConfiguration.SSID != null && str2.equals(wifiConfiguration.SSID)) {
        if (wifiConfiguration.BSSID == null) {
          Trace.d(CMConstants.TAG_NAME, "getWifiConfigurationWithWifiConfiguration, wifiConfiguration.BSSID is null");
          return wifiConfiguration;
        } 
        if (str1 != null && str1.equals(wifiConfiguration.BSSID)) {
          Trace.d(CMConstants.TAG_NAME, "getWifiConfigurationWithWifiConfiguration, wifiConfiguration.BSSID = " + wifiConfiguration.BSSID);
          return wifiConfiguration;
        } 
      } 
    } 
  }
  
  public static void goToHomeScreen(Context paramContext) {
    Intent intent = new Intent("android.intent.action.MAIN");
    intent.addCategory("android.intent.category.HOME");
    paramContext.startActivity(intent);
  }
  
  public static String intToIp(int paramInt) {
    return String.valueOf(paramInt & 0xFF) + "." + (paramInt >> 8 & 0xFF) + "." + (paramInt >> 16 & 0xFF) + "." + (paramInt >> 24 & 0xFF);
  }
  
  public static boolean isAvailableConnect(Context paramContext, WifiManager paramWifiManager) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: aload_1
    //   3: invokevirtual getConnectionInfo : ()Landroid/net/wifi/WifiInfo;
    //   6: astore #4
    //   8: aload #4
    //   10: invokevirtual getSSID : ()Ljava/lang/String;
    //   13: astore_1
    //   14: aload #4
    //   16: invokevirtual getSupplicantState : ()Landroid/net/wifi/SupplicantState;
    //   19: invokestatic getDetailedStateOf : (Landroid/net/wifi/SupplicantState;)Landroid/net/NetworkInfo$DetailedState;
    //   22: astore #4
    //   24: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   27: new java/lang/StringBuilder
    //   30: dup
    //   31: ldc_w 'isAvailableConnect01, ssid_wifiinfo = '
    //   34: invokespecial <init> : (Ljava/lang/String;)V
    //   37: aload_1
    //   38: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   41: invokevirtual toString : ()Ljava/lang/String;
    //   44: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   47: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   50: new java/lang/StringBuilder
    //   53: dup
    //   54: ldc_w 'isAvailableConnect01, state = '
    //   57: invokespecial <init> : (Ljava/lang/String;)V
    //   60: aload #4
    //   62: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   65: invokevirtual toString : ()Ljava/lang/String;
    //   68: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   71: getstatic android/net/NetworkInfo$DetailedState.CONNECTED : Landroid/net/NetworkInfo$DetailedState;
    //   74: aload #4
    //   76: if_acmpeq -> 113
    //   79: getstatic android/net/NetworkInfo$DetailedState.CONNECTING : Landroid/net/NetworkInfo$DetailedState;
    //   82: aload #4
    //   84: if_acmpeq -> 113
    //   87: getstatic android/net/NetworkInfo$DetailedState.IDLE : Landroid/net/NetworkInfo$DetailedState;
    //   90: aload #4
    //   92: if_acmpeq -> 113
    //   95: getstatic android/net/NetworkInfo$DetailedState.OBTAINING_IPADDR : Landroid/net/NetworkInfo$DetailedState;
    //   98: aload #4
    //   100: if_acmpeq -> 113
    //   103: iload_3
    //   104: istore_2
    //   105: getstatic android/net/NetworkInfo$DetailedState.AUTHENTICATING : Landroid/net/NetworkInfo$DetailedState;
    //   108: aload #4
    //   110: if_acmpne -> 134
    //   113: iload_3
    //   114: istore_2
    //   115: aload_1
    //   116: invokestatic supportDSCPrefix : (Ljava/lang/String;)Z
    //   119: ifeq -> 134
    //   122: iload_3
    //   123: istore_2
    //   124: aload_0
    //   125: aload_1
    //   126: invokestatic isManagedSSID : (Landroid/content/Context;Ljava/lang/String;)Z
    //   129: ifeq -> 134
    //   132: iconst_0
    //   133: istore_2
    //   134: iload_2
    //   135: ireturn
  }
  
  public static boolean isAvailableConnect(WifiManager paramWifiManager, String paramString) {
    WifiInfo wifiInfo = paramWifiManager.getConnectionInfo();
    String str = wifiInfo.getSSID();
    NetworkInfo.DetailedState detailedState = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
    Trace.d(CMConstants.TAG_NAME, "isAvailableConnect02, ssid_wifiinfo = " + str);
    Trace.d(CMConstants.TAG_NAME, "isAvailableConnect02, state = " + detailedState);
    Trace.d(CMConstants.TAG_NAME, "isAvailableConnect02, ssid = " + paramString);
    if (paramString.contains("NX1000") && str != null && convertToNOTQuotedString(str).equals("0x")) {
      Trace.d(CMConstants.TAG_NAME, "because of, SSID is " + str + ", then return false.");
      return false;
    } 
    if (NetworkInfo.DetailedState.CONNECTED == detailedState || NetworkInfo.DetailedState.CONNECTING == detailedState || NetworkInfo.DetailedState.IDLE == detailedState || NetworkInfo.DetailedState.OBTAINING_IPADDR == detailedState || NetworkInfo.DetailedState.AUTHENTICATING == detailedState) {
      if (str != null && convertToNOTQuotedString(str).equals(convertToNOTQuotedString(paramString))) {
        Trace.d(CMConstants.TAG_NAME, "because of, already connecting, then return false.");
        return false;
      } 
      return true;
    } 
    return true;
  }
  
  public static boolean isAvailableScan(Context paramContext, WifiManager paramWifiManager) {
    // Byte code:
    //   0: iconst_1
    //   1: istore_3
    //   2: aload_1
    //   3: invokevirtual getConnectionInfo : ()Landroid/net/wifi/WifiInfo;
    //   6: astore #4
    //   8: aload #4
    //   10: invokevirtual getSSID : ()Ljava/lang/String;
    //   13: astore_1
    //   14: aload #4
    //   16: invokevirtual getSupplicantState : ()Landroid/net/wifi/SupplicantState;
    //   19: invokestatic getDetailedStateOf : (Landroid/net/wifi/SupplicantState;)Landroid/net/NetworkInfo$DetailedState;
    //   22: astore #4
    //   24: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   27: new java/lang/StringBuilder
    //   30: dup
    //   31: ldc_w 'isAvailableScan, ssid_wifiinfo = '
    //   34: invokespecial <init> : (Ljava/lang/String;)V
    //   37: aload_1
    //   38: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   41: invokevirtual toString : ()Ljava/lang/String;
    //   44: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   47: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   50: new java/lang/StringBuilder
    //   53: dup
    //   54: ldc_w 'isAvailableScan, state = '
    //   57: invokespecial <init> : (Ljava/lang/String;)V
    //   60: aload #4
    //   62: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   65: invokevirtual toString : ()Ljava/lang/String;
    //   68: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   71: getstatic android/net/NetworkInfo$DetailedState.CONNECTED : Landroid/net/NetworkInfo$DetailedState;
    //   74: aload #4
    //   76: if_acmpeq -> 121
    //   79: getstatic android/net/NetworkInfo$DetailedState.CONNECTING : Landroid/net/NetworkInfo$DetailedState;
    //   82: aload #4
    //   84: if_acmpeq -> 121
    //   87: getstatic android/net/NetworkInfo$DetailedState.IDLE : Landroid/net/NetworkInfo$DetailedState;
    //   90: aload #4
    //   92: if_acmpeq -> 121
    //   95: getstatic android/net/NetworkInfo$DetailedState.OBTAINING_IPADDR : Landroid/net/NetworkInfo$DetailedState;
    //   98: aload #4
    //   100: if_acmpeq -> 121
    //   103: getstatic android/net/NetworkInfo$DetailedState.AUTHENTICATING : Landroid/net/NetworkInfo$DetailedState;
    //   106: aload #4
    //   108: if_acmpeq -> 121
    //   111: iload_3
    //   112: istore_2
    //   113: getstatic android/net/NetworkInfo$DetailedState.SCANNING : Landroid/net/NetworkInfo$DetailedState;
    //   116: aload #4
    //   118: if_acmpne -> 142
    //   121: iload_3
    //   122: istore_2
    //   123: aload_1
    //   124: invokestatic supportDSCPrefix : (Ljava/lang/String;)Z
    //   127: ifeq -> 142
    //   130: iload_3
    //   131: istore_2
    //   132: aload_0
    //   133: aload_1
    //   134: invokestatic isManagedSSID : (Landroid/content/Context;Ljava/lang/String;)Z
    //   137: ifeq -> 142
    //   140: iconst_0
    //   141: istore_2
    //   142: iload_2
    //   143: ireturn
  }
  
  public static boolean isCMTopActivity(Context paramContext) {
    boolean bool = false;
    if ("com.samsungimaging.connectionmanager".equals(((ActivityManager.RunningTaskInfo)((ActivityManager)paramContext.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getPackageName()))
      bool = true; 
    return bool;
  }
  
  public static boolean isEqualSSID(String paramString1, String paramString2) {
    return convertToNOTQuotedString(paramString1).equalsIgnoreCase(convertToNOTQuotedString(paramString2));
  }
  
  public static boolean isForceClose(ActivityManager paramActivityManager) {
    if (paramActivityManager != null) {
      List list = paramActivityManager.getRecentTasks(2147483647, 1);
      if (list != null) {
        byte b = 0;
        int i = 0;
        while (true) {
          if (i >= list.size()) {
            i = b;
          } else if (((ActivityManager.RecentTaskInfo)list.get(i)).baseIntent.getComponent().getPackageName().equals("com.samsungimaging.connectionmanager")) {
            i = 1;
          } else {
            i++;
            continue;
          } 
          if (i == 0) {
            Trace.d(CMConstants.TAG_NAME, "isForceClose, disappeared in recent task info...finish app.");
            CMService.getInstance().forceClosed();
            return true;
          } 
          Trace.d(CMConstants.TAG_NAME, "isForceClose, alive in recent task info.");
          return false;
        } 
      } 
    } 
    return false;
  }
  
  public static boolean isGalaxySeriesPrefix(String paramString) {
    paramString = convertToNOTQuotedString(paramString);
    if (paramString != null) {
      Iterator<String> iterator = galaxySeriesPrefixList.iterator();
      while (true) {
        if (iterator.hasNext()) {
          if (paramString.startsWith(iterator.next()))
            return true; 
          continue;
        } 
        return false;
      } 
    } 
    return false;
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
  
  public static boolean isManagedSSID(Context paramContext, String paramString) {
    return !(DatabaseManager.fetchForAP(paramContext, convertToNOTQuotedString(paramString)) == null);
  }
  
  private static boolean isOpenSecurity(WifiConfiguration paramWifiConfiguration) {
    boolean bool = false;
    if (paramWifiConfiguration.allowedKeyManagement.get(0))
      bool = true; 
    return bool;
  }
  
  public static boolean isTestMode(Context paramContext) {
    boolean bool = false;
    if ("true".equalsIgnoreCase(CMSharedPreferenceUtil.getString(paramContext, "com.samsungimaging.connectionmanager.TESTMODE", "false")))
      bool = true; 
    return bool;
  }
  
  public static boolean lateDHCPRefresh(String paramString) {
    if (paramString != null) {
      Iterator<String> iterator = lateDHCPRefreshList.iterator();
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
  
  public static String parseDescriptionURLForRVFML(String paramString) {
    String[] arrayOfString = paramString.split("\n");
    for (int i = 0;; i++) {
      if (i >= arrayOfString.length)
        continue; 
      Trace.d(CMConstants.TAG_NAME, "ModeClient, checkResponse, parseDescriptionURLForRVFML, input_array[i] = " + arrayOfString[i]);
      if (arrayOfString[i].contains("Default-URL:")) {
        arrayOfString = arrayOfString[i].split("Default-URL:");
        i = 0;
        while (true) {
          if (i < arrayOfString.length) {
            Trace.d(CMConstants.TAG_NAME, "ModeClient, checkResponse, parseDescriptionURLForRVFML, default_url_node[i] = " + arrayOfString[i]);
            if (arrayOfString[i].contains("http"))
              return arrayOfString[i]; 
            i++;
            continue;
          } 
          return null;
        } 
        break;
      } 
    } 
  }
  
  public static void removeNetworkConfig(Context paramContext, WifiManager paramWifiManager, String paramString1, String paramString2) {
    Trace.d(CMConstants.TAG_NAME, "removeNetworkConfig, ssid = " + paramString1);
    Trace.d(CMConstants.TAG_NAME, "removeNetworkConfig, bssid = " + paramString2);
    WifiConfiguration wifiConfiguration = getWifiConfigurationWithScanResult(paramWifiManager, paramString1, paramString2);
    if (wifiConfiguration != null) {
      if (paramWifiManager.removeNetwork(wifiConfiguration.networkId)) {
        if (!paramWifiManager.saveConfiguration())
          Trace.d(CMConstants.TAG_NAME, "removeNetworkConfig, remove config failed01."); 
        return;
      } 
      Trace.d(CMConstants.TAG_NAME, "removeNetworkConfig, remove config failed02.");
      return;
    } 
    Trace.d(CMConstants.TAG_NAME, "removeNetworkConfig, not exist config.");
  }
  
  public static List<ScanResult> selectCameraSoftAP(Context paramContext, List<ScanResult> paramList) {
    ScanResult scanResult;
    ArrayList<ScanResult> arrayList = new ArrayList();
    if ("true".equalsIgnoreCase(CMSharedPreferenceUtil.getString(paramContext, "com.samsungimaging.connectionmanager.TESTMODE", "false"))) {
      String str = CMSharedPreferenceUtil.getString(paramContext, "com.samsungimaging.connectionmanager.TESTMODE_SSID", "");
      Trace.d(CMConstants.TAG_NAME, "selectCameraSoftAP, ssid_temp = " + str);
      if (str.equals("")) {
        if (paramList != null && paramList.size() > 0) {
          Iterator<ScanResult> iterator = paramList.iterator();
          while (true) {
            if (iterator.hasNext()) {
              scanResult = iterator.next();
              if (supportDSCPrefix(scanResult.SSID) || NOTsupportDSCPrefix(scanResult.SSID))
                arrayList.add(scanResult); 
              continue;
            } 
            return arrayList;
          } 
        } 
        return arrayList;
      } 
      if (scanResult != null && scanResult.size() > 0) {
        Iterator<ScanResult> iterator = scanResult.iterator();
        while (true) {
          if (iterator.hasNext()) {
            scanResult = iterator.next();
            if (supportDSCPrefix(scanResult.SSID))
              arrayList.add(scanResult); 
            continue;
          } 
          return arrayList;
        } 
      } 
      return arrayList;
    } 
    if (scanResult != null && scanResult.size() > 0) {
      Iterator<ScanResult> iterator = scanResult.iterator();
      while (true) {
        if (iterator.hasNext()) {
          scanResult = iterator.next();
          if (supportDSCPrefix(scanResult.SSID) || NOTsupportDSCPrefix(scanResult.SSID))
            arrayList.add(scanResult); 
          continue;
        } 
        return arrayList;
      } 
    } 
    return arrayList;
  }
  
  public static void sendBroadCastToMain(Context paramContext, String paramString) {
    Trace.d(CMConstants.TAG_NAME, "sendBroadCastFromCM~~~ extraInfo = " + paramString);
    Intent intent = new Intent();
    if (paramString != null)
      intent.putExtra("EXTRA_KEY_FROM_CM", paramString); 
    intent.setAction("com.samsungimaging.connectionmanager.intent.FROM_CM");
    paramContext.sendBroadcast(intent);
  }
  
  public static void sendBroadCastToMain(Context paramContext, String paramString, int paramInt) {
    Trace.d(CMConstants.TAG_NAME, "sendBroadCastFromCM~~~");
    Intent intent = new Intent();
    if (paramString != null)
      intent.putExtra("EXTRA_KEY_FROM_CM", paramString); 
    intent.putExtra("EXTRA_KEY2_FROM_CM", paramInt);
    intent.setAction("com.samsungimaging.connectionmanager.intent.FROM_CM");
    paramContext.sendBroadcast(intent);
  }
  
  public static void sendBroadCastToMain(Context paramContext, String paramString, Bundle paramBundle) {
    Trace.d(CMConstants.TAG_NAME, "sendBroadCastFromCM~~~");
    Intent intent = new Intent();
    if (paramString != null) {
      intent.putExtra("EXTRA_KEY_FROM_CM", paramString);
      intent.putExtra("EXTRA_KEY3_FROM_CM", paramBundle);
    } 
    intent.setAction("com.samsungimaging.connectionmanager.intent.FROM_CM");
    paramContext.sendBroadcast(intent);
  }
  
  private static void sendMessageForPassword(ScanResult paramScanResult, ConnectionHandler paramConnectionHandler) {
    if (paramConnectionHandler != null) {
      String str1 = ConfigurationSecurity.getSecurity2(paramScanResult);
      String str2 = paramScanResult.SSID;
      String str3 = paramScanResult.BSSID;
      Trace.d(CMConstants.TAG_NAME, "sendMessageForPassword, scanResult.SSID = " + paramScanResult.SSID);
      Trace.d(CMConstants.TAG_NAME, "sendMessageForPassword, scanResult.BSSID = " + paramScanResult.BSSID);
      Trace.d(CMConstants.TAG_NAME, "sendMessageForPassword, scanResult.capabilities = " + paramScanResult.capabilities);
      Trace.d(CMConstants.TAG_NAME, "sendMessageForPassword, security_type = " + str1);
      Bundle bundle = new Bundle();
      bundle.putCharSequenceArray("data", (CharSequence[])new String[] { str2, str3, str1 });
      Message message = new Message();
      message.what = 100;
      message.obj = bundle;
      paramConnectionHandler.sendMessage(message);
    } 
  }
  
  public static void setdscPrefix_fortest(Context paramContext, String paramString) {
    Trace.d(CMConstants.TAG_NAME, "CMUtil.setdscPrefix, ssid = " + paramString);
    if ("true".equalsIgnoreCase(CMSharedPreferenceUtil.getString(paramContext, "com.samsungimaging.connectionmanager.TESTMODE", "false"))) {
      dscPrefixList.clear();
      if (paramString.equals("")) {
        dscPrefixList.add("AP_SSC_");
        dscPrefixList.add("EK_");
        dscPrefixList.add("SM_");
        dscPrefixList.add("GC_");
        return;
      } 
    } else {
      return;
    } 
    dscPrefixList.add(paramString);
  }
  
  public static void setdscPrefix_fortest(Context paramContext, List<String> paramList) {
    if ("true".equalsIgnoreCase(CMSharedPreferenceUtil.getString(paramContext, "com.samsungimaging.connectionmanager.TESTMODE", "false"))) {
      dscPrefixList.clear();
      for (int i = 0;; i++) {
        if (i >= paramList.size()) {
          if (dscPrefixList.size() == 0) {
            dscPrefixList.add("AP_SSC_");
            dscPrefixList.add("EK_");
            dscPrefixList.add("SM_");
            dscPrefixList.add("GC_");
          } 
          return;
        } 
        String str = paramList.get(i);
        if (!str.equals(""))
          dscPrefixList.add(str); 
      } 
    } 
  }
  
  public static List<ScanResult> sort(List<ScanResult> paramList) {
    Collections.sort(paramList, new Comparator<ScanResult>() {
          public int compare(ScanResult param1ScanResult1, ScanResult param1ScanResult2) {
            return Integer.signum(param1ScanResult2.level - param1ScanResult1.level);
          }
        });
    return paramList;
  }
  
  private static void sortByPriority(List<WifiConfiguration> paramList) {
    Collections.sort(paramList, new Comparator<WifiConfiguration>() {
          public int compare(WifiConfiguration param1WifiConfiguration1, WifiConfiguration param1WifiConfiguration2) {
            return param1WifiConfiguration1.priority - param1WifiConfiguration2.priority;
          }
        });
  }
  
  public static boolean supportAllChooseDialogModel(String paramString) {
    if (paramString != null) {
      Iterator<String> iterator = useAllChooseDialogList.iterator();
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
  
  public static boolean supportDSCPrefix(String paramString) {
    paramString = convertToNOTQuotedString(paramString);
    if (paramString != null) {
      Iterator<String> iterator = dscPrefixList.iterator();
      while (true) {
        if (iterator.hasNext()) {
          if (paramString.startsWith(iterator.next()))
            return true; 
          continue;
        } 
        return false;
      } 
    } 
    return false;
  }
  
  public static boolean supportOldDSCPrefix(String paramString) {
    paramString = convertToNOTQuotedString(paramString);
    if (paramString != null) {
      Iterator<String> iterator = olddscPrefixList.iterator();
      while (true) {
        if (iterator.hasNext()) {
          if (paramString.startsWith(iterator.next()))
            return true; 
          continue;
        } 
        return false;
      } 
    } 
    return false;
  }
  
  public static String whatIsTopActivity(Context paramContext) {
    String str = ((ActivityManager.RunningTaskInfo)((ActivityManager)paramContext.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getClassName();
    Trace.d(CMConstants.TAG_NAME, "whatIsTopActivity, topClassName : " + str);
    return str;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\common\CMUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */