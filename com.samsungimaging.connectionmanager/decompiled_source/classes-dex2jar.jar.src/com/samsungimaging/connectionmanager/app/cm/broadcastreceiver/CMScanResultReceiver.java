package com.samsungimaging.connectionmanager.app.cm.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CMScanResultReceiver extends BroadcastReceiver {
  public void onReceive(Context paramContext, Intent paramIntent) {
    // Byte code:
    //   0: ldc 'android.net.wifi.SCAN_RESULTS'
    //   2: aload_2
    //   3: invokevirtual getAction : ()Ljava/lang/String;
    //   6: invokevirtual equals : (Ljava/lang/Object;)Z
    //   9: ifeq -> 43
    //   12: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   15: ldc 'WifiManager.SCAN_RESULTS_AVAILABLE_ACTION!!!'
    //   17: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   20: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   23: ifnull -> 44
    //   26: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   29: invokevirtual isWifiScanStop : ()Z
    //   32: ifeq -> 44
    //   35: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   38: ldc 'CMScanResultReceiver, return01'
    //   40: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   43: return
    //   44: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   47: ifnonnull -> 59
    //   50: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   53: ldc 'CMScanResultReceiver, return02'
    //   55: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   58: return
    //   59: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   62: invokevirtual isSubAppAlive : ()Z
    //   65: ifeq -> 77
    //   68: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   71: ldc 'CMScanResultReceiver, return03'
    //   73: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   76: return
    //   77: ldc 'true'
    //   79: aload_1
    //   80: ldc 'com.samsungimaging.connectionmanager.SP_KEY_IS_RECEIVER_RUNNING'
    //   82: ldc 'true'
    //   84: invokestatic getString : (Landroid/content/Context;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   87: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   90: ifne -> 102
    //   93: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   96: ldc 'CMScanResultReceiver, return04'
    //   98: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   101: return
    //   102: aload_1
    //   103: ldc 'wifi'
    //   105: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   108: checkcast android/net/wifi/WifiManager
    //   111: astore #7
    //   113: aload_1
    //   114: ldc 'connectivity'
    //   116: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   119: checkcast android/net/ConnectivityManager
    //   122: astore #8
    //   124: aload #7
    //   126: invokevirtual getScanResults : ()Ljava/util/List;
    //   129: astore #9
    //   131: new java/util/ArrayList
    //   134: dup
    //   135: invokespecial <init> : ()V
    //   138: astore #6
    //   140: aload #6
    //   142: astore_2
    //   143: aload #9
    //   145: ifnull -> 168
    //   148: aload #6
    //   150: astore_2
    //   151: aload #9
    //   153: invokeinterface size : ()I
    //   158: ifle -> 168
    //   161: aload_1
    //   162: aload #9
    //   164: invokestatic selectCameraSoftAP : (Landroid/content/Context;Ljava/util/List;)Ljava/util/List;
    //   167: astore_2
    //   168: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   171: new java/lang/StringBuilder
    //   174: dup
    //   175: ldc 'CMScanResultReceiver : ~~~~~~~~~~~~~~~~~~~~~~~~~ scan count : '
    //   177: invokespecial <init> : (Ljava/lang/String;)V
    //   180: aload_2
    //   181: invokeinterface size : ()I
    //   186: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   189: invokevirtual toString : ()Ljava/lang/String;
    //   192: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   195: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   198: aload_2
    //   199: invokevirtual setScannedList : (Ljava/util/List;)V
    //   202: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   205: invokevirtual getScannedList : ()Ljava/util/List;
    //   208: astore #6
    //   210: aload #6
    //   212: ifnull -> 744
    //   215: aload #6
    //   217: invokeinterface size : ()I
    //   222: ifle -> 744
    //   225: aload_1
    //   226: invokestatic fetchAllForAP : (Landroid/content/Context;)Ljava/util/ArrayList;
    //   229: astore_2
    //   230: aload #6
    //   232: invokestatic sort : (Ljava/util/List;)Ljava/util/List;
    //   235: astore #6
    //   237: aload #7
    //   239: invokevirtual getConnectionInfo : ()Landroid/net/wifi/WifiInfo;
    //   242: astore #9
    //   244: aload #9
    //   246: invokevirtual getSupplicantState : ()Landroid/net/wifi/SupplicantState;
    //   249: invokestatic getDetailedStateOf : (Landroid/net/wifi/SupplicantState;)Landroid/net/NetworkInfo$DetailedState;
    //   252: astore #10
    //   254: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   257: new java/lang/StringBuilder
    //   260: dup
    //   261: ldc 'CMScanResultReceiver : ~~~~~~~~~~~~~~~~~~~~~~~~~ state : '
    //   263: invokespecial <init> : (Ljava/lang/String;)V
    //   266: aload #10
    //   268: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   271: ldc ', ssid : '
    //   273: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   276: aload #9
    //   278: invokevirtual getSSID : ()Ljava/lang/String;
    //   281: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   284: invokevirtual toString : ()Ljava/lang/String;
    //   287: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   290: aload #8
    //   292: invokevirtual getActiveNetworkInfo : ()Landroid/net/NetworkInfo;
    //   295: astore #8
    //   297: aload #8
    //   299: ifnull -> 435
    //   302: aload #8
    //   304: invokevirtual getDetailedState : ()Landroid/net/NetworkInfo$DetailedState;
    //   307: astore #8
    //   309: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   312: new java/lang/StringBuilder
    //   315: dup
    //   316: ldc 'CMScanResultReceiver : ~~~~~~~~~~~~~~~~~~~~~~~~~ state_connectivitymanager : '
    //   318: invokespecial <init> : (Ljava/lang/String;)V
    //   321: aload #8
    //   323: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   326: invokevirtual toString : ()Ljava/lang/String;
    //   329: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   332: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   335: new java/lang/StringBuilder
    //   338: dup
    //   339: ldc 'CMScanResultReceiver : ~~~~~~~~~~~~~~~~~~~~~~~~~ state_wifimanager : '
    //   341: invokespecial <init> : (Ljava/lang/String;)V
    //   344: aload #10
    //   346: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   349: invokevirtual toString : ()Ljava/lang/String;
    //   352: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   355: aload #8
    //   357: getstatic android/net/NetworkInfo$DetailedState.CONNECTED : Landroid/net/NetworkInfo$DetailedState;
    //   360: if_acmpne -> 435
    //   363: aload #10
    //   365: getstatic android/net/NetworkInfo$DetailedState.CONNECTED : Landroid/net/NetworkInfo$DetailedState;
    //   368: if_acmpne -> 435
    //   371: aload #9
    //   373: invokevirtual getSSID : ()Ljava/lang/String;
    //   376: invokestatic supportDSCPrefix : (Ljava/lang/String;)Z
    //   379: ifeq -> 429
    //   382: aload_1
    //   383: aload #9
    //   385: invokevirtual getSSID : ()Ljava/lang/String;
    //   388: invokestatic isManagedSSID : (Landroid/content/Context;Ljava/lang/String;)Z
    //   391: ifeq -> 429
    //   394: iconst_1
    //   395: istore #5
    //   397: iload #5
    //   399: putstatic com/samsungimaging/connectionmanager/app/cm/service/CMService.IS_WIFI_CONNECTED : Z
    //   402: getstatic com/samsungimaging/connectionmanager/app/cm/service/CMService.IS_WIFI_CONNECTED : Z
    //   405: ifeq -> 435
    //   408: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   411: ldc 'CMScanResultReceiver : CMMainActivity.IS_CONNECTED!!!'
    //   413: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   416: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   419: ifnull -> 43
    //   422: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   425: invokevirtual checkSubApplicationType : ()V
    //   428: return
    //   429: iconst_0
    //   430: istore #5
    //   432: goto -> 397
    //   435: aload_1
    //   436: aload #7
    //   438: invokestatic isAvailableConnect : (Landroid/content/Context;Landroid/net/wifi/WifiManager;)Z
    //   441: ifeq -> 43
    //   444: new java/util/ArrayList
    //   447: dup
    //   448: invokespecial <init> : ()V
    //   451: astore #8
    //   453: aload_2
    //   454: ifnull -> 474
    //   457: aload_2
    //   458: invokevirtual size : ()I
    //   461: ifle -> 474
    //   464: iconst_0
    //   465: istore_3
    //   466: iload_3
    //   467: aload_2
    //   468: invokevirtual size : ()I
    //   471: if_icmplt -> 585
    //   474: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   477: new java/lang/StringBuilder
    //   480: dup
    //   481: ldc 'CMScanResultReceiver, ConnectionTryCount = '
    //   483: invokespecial <init> : (Ljava/lang/String;)V
    //   486: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   489: getfield mConnectionTryCount : I
    //   492: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   495: invokevirtual toString : ()Ljava/lang/String;
    //   498: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   501: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/common/CMInfo;
    //   504: invokevirtual getIsNFCLaunchFlag : ()Z
    //   507: ifeq -> 517
    //   510: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   513: iconst_0
    //   514: putfield mConnectionTryCount : I
    //   517: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   520: getfield mConnectionTryCount : I
    //   523: iconst_3
    //   524: if_icmpne -> 541
    //   527: aload_1
    //   528: aload_1
    //   529: ldc 2131361834
    //   531: invokevirtual getString : (I)Ljava/lang/String;
    //   534: iconst_0
    //   535: invokestatic makeText : (Landroid/content/Context;Ljava/lang/CharSequence;I)Landroid/widget/Toast;
    //   538: invokevirtual show : ()V
    //   541: aload #8
    //   543: invokeinterface size : ()I
    //   548: ifle -> 43
    //   551: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   554: ldc 'CMScanResultReceiver : ~~~~~~~~~~~~~~~~~~~~~~~~~ call connector '
    //   556: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   559: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/connector/CMCameraAPConnector;
    //   562: aload #8
    //   564: aload_1
    //   565: aload #7
    //   567: invokevirtual setSr : (Ljava/util/List;Landroid/content/Context;Landroid/net/wifi/WifiManager;)V
    //   570: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   573: astore_1
    //   574: aload_1
    //   575: aload_1
    //   576: getfield mConnectionTryCount : I
    //   579: iconst_1
    //   580: iadd
    //   581: putfield mConnectionTryCount : I
    //   584: return
    //   585: iconst_0
    //   586: istore #4
    //   588: iload #4
    //   590: aload #6
    //   592: invokeinterface size : ()I
    //   597: if_icmplt -> 607
    //   600: iload_3
    //   601: iconst_1
    //   602: iadd
    //   603: istore_3
    //   604: goto -> 466
    //   607: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   610: new java/lang/StringBuilder
    //   613: dup
    //   614: ldc 'CMScanResultReceiver, managedAPs.get('
    //   616: invokespecial <init> : (Ljava/lang/String;)V
    //   619: iload_3
    //   620: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   623: ldc ').getCameraSSID() = '
    //   625: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   628: aload_2
    //   629: iload_3
    //   630: invokevirtual get : (I)Ljava/lang/Object;
    //   633: checkcast com/samsungimaging/connectionmanager/provider/DatabaseAP
    //   636: invokevirtual getSSID : ()Ljava/lang/String;
    //   639: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   642: ldc_w ', cameraSrs.get('
    //   645: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   648: iload #4
    //   650: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   653: ldc_w ').SSID = '
    //   656: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   659: aload #6
    //   661: iload #4
    //   663: invokeinterface get : (I)Ljava/lang/Object;
    //   668: checkcast android/net/wifi/ScanResult
    //   671: getfield SSID : Ljava/lang/String;
    //   674: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   677: invokevirtual toString : ()Ljava/lang/String;
    //   680: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   683: aload_2
    //   684: iload_3
    //   685: invokevirtual get : (I)Ljava/lang/Object;
    //   688: checkcast com/samsungimaging/connectionmanager/provider/DatabaseAP
    //   691: invokevirtual getSSID : ()Ljava/lang/String;
    //   694: aload #6
    //   696: iload #4
    //   698: invokeinterface get : (I)Ljava/lang/Object;
    //   703: checkcast android/net/wifi/ScanResult
    //   706: getfield SSID : Ljava/lang/String;
    //   709: invokevirtual equals : (Ljava/lang/Object;)Z
    //   712: ifeq -> 735
    //   715: aload #8
    //   717: aload #6
    //   719: iload #4
    //   721: invokeinterface get : (I)Ljava/lang/Object;
    //   726: checkcast android/net/wifi/ScanResult
    //   729: invokeinterface add : (Ljava/lang/Object;)Z
    //   734: pop
    //   735: iload #4
    //   737: iconst_1
    //   738: iadd
    //   739: istore #4
    //   741: goto -> 588
    //   744: aload #6
    //   746: ifnull -> 43
    //   749: aload #6
    //   751: invokeinterface size : ()I
    //   756: ifne -> 43
    //   759: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   762: ldc_w 'CMScanResultReceiver : ~~~~~~~~~~~~~~~~~~~~~~~~~ cameraSrs.size() is 0,  enalbleAllAps.'
    //   765: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   768: aload_1
    //   769: invokestatic enalbleNOTCameraAps : (Landroid/content/Context;)V
    //   772: return
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\broadcastreceiver\CMScanResultReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */