package com.samsungimaging.connectionmanager.app.cm.modemanager;

import com.samsungimaging.connectionmanager.app.cm.Interface.IModeClient;
import com.samsungimaging.connectionmanager.app.cm.common.CMConstants;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.util.Trace;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ModeClient implements Runnable {
  private static ModeClient instance_ModeClient = null;
  
  private static ArrayList<IModeClient> mIModeClientList = new ArrayList<IModeClient>();
  
  private Socket mModeClientSocket = null;
  
  private String mNTS = "alive";
  
  private Thread mRunModeClient = null;
  
  private int mServerPort = 7788;
  
  private ModeClient(boolean paramBoolean) {
    Trace.d(CMConstants.TAG_NAME, "ModeClient, isbyebye = " + paramBoolean);
    if (paramBoolean) {
      this.mNTS = "byebye";
      return;
    } 
    this.mNTS = "alive";
  }
  
  private int checkResponse(String paramString) {
    boolean bool = false;
    byte b = 0;
    if (paramString.contains("HTTP/1.1 200 OK")) {
      Trace.d(CMConstants.TAG_NAME, "ModeClient, HTTP/1.1 200 OK");
      String str = CMUtil.parseDescriptionURLForRVFML(paramString);
      Trace.d(CMConstants.TAG_NAME, "ModeClient, checkResponse, default_url = " + str);
      CMInfo.getInstance().setDescriptionURL(str);
      if (paramString.toLowerCase().contains("mobilelink")) {
        b = 1;
      } else if (paramString.toLowerCase().contains("rvf")) {
        b = 2;
      } else if (paramString.toLowerCase().contains("autoshare")) {
        b = 3;
      } else if (paramString.toLowerCase().contains("selectivepush")) {
        b = 4;
      } else if (paramString.toLowerCase().contains("groupshare")) {
        b = 5;
      } else if (paramString.toLowerCase().contains("mobilebackup")) {
        b = 6;
      } else if (paramString.toLowerCase().contains("idle")) {
        b = 7;
      } else if (paramString.toLowerCase().contains("byebye")) {
        b = 23;
      } 
      if (paramString.contains("MVERSION")) {
        CMInfo.getInstance().setIsOldCamera(false);
        return b;
      } 
      CMInfo.getInstance().setIsOldCamera(true);
      return b;
    } 
    if (paramString.contains("HTTP/1.1 401"))
      return 21; 
    b = bool;
    return paramString.contains("HTTP/1.1 503") ? 22 : b;
  }
  
  private void clearModeClientListner() {
    mIModeClientList.clear();
  }
  
  public static ModeClient getInstance() {
    // Byte code:
    //   0: ldc com/samsungimaging/connectionmanager/app/cm/modemanager/ModeClient
    //   2: monitorenter
    //   3: getstatic com/samsungimaging/connectionmanager/app/cm/modemanager/ModeClient.instance_ModeClient : Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeClient;
    //   6: ifnonnull -> 20
    //   9: new com/samsungimaging/connectionmanager/app/cm/modemanager/ModeClient
    //   12: dup
    //   13: iconst_0
    //   14: invokespecial <init> : (Z)V
    //   17: putstatic com/samsungimaging/connectionmanager/app/cm/modemanager/ModeClient.instance_ModeClient : Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeClient;
    //   20: getstatic com/samsungimaging/connectionmanager/app/cm/modemanager/ModeClient.instance_ModeClient : Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeClient;
    //   23: astore_0
    //   24: ldc com/samsungimaging/connectionmanager/app/cm/modemanager/ModeClient
    //   26: monitorexit
    //   27: aload_0
    //   28: areturn
    //   29: astore_0
    //   30: ldc com/samsungimaging/connectionmanager/app/cm/modemanager/ModeClient
    //   32: monitorexit
    //   33: aload_0
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   3	20	29	finally
    //   20	24	29	finally
  }
  
  private void performRunByebye() {
    int j = mIModeClientList.size();
    Trace.d(CMConstants.TAG_NAME, "ModeClient, performRunByebye, listenerSize = " + j);
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      ((IModeClient)mIModeClientList.get(i)).runByebye();
    } 
  }
  
  private void performRunSubApplication(int paramInt) {
    int j = mIModeClientList.size();
    Trace.d(CMConstants.TAG_NAME, "ModeClient, performRunSubApplication, listenerSize = " + j);
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      ((IModeClient)mIModeClientList.get(i)).runSubApplication(paramInt);
    } 
  }
  
  public void addModeClientListener(IModeClient paramIModeClient) {
    Trace.d(CMConstants.TAG_NAME, "ModeClient, addModeClientListener -> " + paramIModeClient);
    int j = mIModeClientList.size();
    for (int i = 0;; i++) {
      if (i >= j) {
        Trace.d(CMConstants.TAG_NAME, "ModeClient, addModeClientListener, add done.");
        mIModeClientList.add(paramIModeClient);
        return;
      } 
      Trace.d(CMConstants.TAG_NAME, "ModeClient, addModeClientListener -> mIModeClientList.get(" + i + ") = " + mIModeClientList.get(i));
      if (paramIModeClient.equals(mIModeClientList.get(i))) {
        Trace.d(CMConstants.TAG_NAME, "ModeClient, addModeClientListener, already added.");
        return;
      } 
    } 
  }
  
  public void run() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: iconst_0
    //   3: istore_1
    //   4: iload_1
    //   5: bipush #16
    //   7: if_icmplt -> 11
    //   10: return
    //   11: iload_2
    //   12: istore_3
    //   13: getstatic com/samsungimaging/connectionmanager/app/cm/service/CMService.mWifiManager : Landroid/net/wifi/WifiManager;
    //   16: ifnull -> 1155
    //   19: getstatic com/samsungimaging/connectionmanager/app/cm/service/CMService.mWifiManager : Landroid/net/wifi/WifiManager;
    //   22: invokevirtual getConnectionInfo : ()Landroid/net/wifi/WifiInfo;
    //   25: astore #6
    //   27: aload #6
    //   29: invokevirtual getSSID : ()Ljava/lang/String;
    //   32: pop
    //   33: getstatic com/samsungimaging/connectionmanager/app/cm/service/CMService.mWifiManager : Landroid/net/wifi/WifiManager;
    //   36: invokevirtual getDhcpInfo : ()Landroid/net/DhcpInfo;
    //   39: astore #4
    //   41: aload_0
    //   42: getfield mServerPort : I
    //   45: istore_3
    //   46: aload #4
    //   48: getfield serverAddress : I
    //   51: invokestatic formatIpAddress : (I)Ljava/lang/String;
    //   54: astore #13
    //   56: aload #4
    //   58: getfield ipAddress : I
    //   61: invokestatic formatIpAddress : (I)Ljava/lang/String;
    //   64: astore #7
    //   66: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   69: new java/lang/StringBuilder
    //   72: dup
    //   73: ldc 'ModeClient, cameraIP = '
    //   75: invokespecial <init> : (Ljava/lang/String;)V
    //   78: aload #13
    //   80: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   83: invokevirtual toString : ()Ljava/lang/String;
    //   86: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   89: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   92: new java/lang/StringBuilder
    //   95: dup
    //   96: ldc 'ModeClient, phoneIP = '
    //   98: invokespecial <init> : (Ljava/lang/String;)V
    //   101: aload #7
    //   103: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   106: invokevirtual toString : ()Ljava/lang/String;
    //   109: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   112: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   115: new java/lang/StringBuilder
    //   118: dup
    //   119: ldc 'ModeClient, ModeServer.mServerPort = '
    //   121: invokespecial <init> : (Ljava/lang/String;)V
    //   124: getstatic com/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer.mServerPort : I
    //   127: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   130: invokevirtual toString : ()Ljava/lang/String;
    //   133: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   136: getstatic com/samsungimaging/connectionmanager/app/cm/service/CMService.mContext : Landroid/content/Context;
    //   139: invokestatic getLineNumber : (Landroid/content/Context;)Ljava/lang/String;
    //   142: astore #5
    //   144: aload #6
    //   146: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   149: invokevirtual toUpperCase : ()Ljava/lang/String;
    //   152: astore #4
    //   154: aload #5
    //   156: ldc 'none'
    //   158: invokevirtual equals : (Ljava/lang/Object;)Z
    //   161: ifne -> 168
    //   164: aload #5
    //   166: astore #4
    //   168: new java/lang/StringBuffer
    //   171: dup
    //   172: invokespecial <init> : ()V
    //   175: astore #14
    //   177: aload #14
    //   179: ldc_w 'HEAD /mode/control HTTP/1.1'
    //   182: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   185: ldc_w '\\r\\n'
    //   188: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   191: ldc_w 'User-Agent: SEC_MODE_'
    //   194: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   197: aload #4
    //   199: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   202: ldc_w '\\r\\n'
    //   205: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   208: ldc_w 'Connection: Close'
    //   211: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   214: ldc_w '\\r\\n'
    //   217: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   220: ldc_w 'NTS : '
    //   223: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   226: aload_0
    //   227: getfield mNTS : Ljava/lang/String;
    //   230: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   233: ldc_w '\\r\\n'
    //   236: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   239: ldc_w 'Content-Length: 0'
    //   242: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   245: ldc_w '\\r\\n'
    //   248: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   251: ldc_w 'HOST-Mac : '
    //   254: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   257: aload #6
    //   259: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   262: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   265: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   268: ldc_w '\\r\\n'
    //   271: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   274: ldc_w 'HOST-Address : '
    //   277: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   280: aload #7
    //   282: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   285: ldc_w '\\r\\n'
    //   288: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   291: ldc_w 'HOST-port : '
    //   294: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   297: iload_3
    //   298: invokevirtual append : (I)Ljava/lang/StringBuffer;
    //   301: ldc_w '\\r\\n'
    //   304: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   307: ldc_w 'HOST-PNumber : '
    //   310: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   313: aload #5
    //   315: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   318: ldc_w '\\r\\n'
    //   321: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   324: ldc_w 'Access-Method : '
    //   327: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   330: ldc_w 'manual'
    //   333: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   336: ldc_w '\\r\\n'
    //   339: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   342: ldc_w 'CALLBACK: <http://'
    //   345: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   348: aload #7
    //   350: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   353: ldc_w ':'
    //   356: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   359: getstatic com/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer.mServerPort : I
    //   362: invokevirtual append : (I)Ljava/lang/StringBuffer;
    //   365: ldc_w '/eventCallback>'
    //   368: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   371: ldc_w '\\r\\n'
    //   374: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   377: ldc_w '\\r\\n'
    //   380: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   383: pop
    //   384: aconst_null
    //   385: astore #4
    //   387: aconst_null
    //   388: astore #10
    //   390: aconst_null
    //   391: astore #11
    //   393: aconst_null
    //   394: astore #12
    //   396: aconst_null
    //   397: astore #9
    //   399: aconst_null
    //   400: astore #7
    //   402: aconst_null
    //   403: astore #8
    //   405: aload #12
    //   407: astore #5
    //   409: aload #4
    //   411: astore #6
    //   413: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   416: new java/lang/StringBuilder
    //   419: dup
    //   420: ldc_w 'ModeClient, Camera Connection Try... : ['
    //   423: invokespecial <init> : (Ljava/lang/String;)V
    //   426: aload #13
    //   428: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   431: ldc_w ':'
    //   434: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   437: iload_3
    //   438: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   441: ldc_w ']'
    //   444: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   447: invokevirtual toString : ()Ljava/lang/String;
    //   450: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   453: aload #12
    //   455: astore #5
    //   457: aload #4
    //   459: astore #6
    //   461: aload_0
    //   462: new java/net/Socket
    //   465: dup
    //   466: invokespecial <init> : ()V
    //   469: putfield mModeClientSocket : Ljava/net/Socket;
    //   472: aload #12
    //   474: astore #5
    //   476: aload #4
    //   478: astore #6
    //   480: new java/net/InetSocketAddress
    //   483: dup
    //   484: aload #13
    //   486: iload_3
    //   487: invokespecial <init> : (Ljava/lang/String;I)V
    //   490: astore #15
    //   492: aload #12
    //   494: astore #5
    //   496: aload #4
    //   498: astore #6
    //   500: aload_0
    //   501: getfield mModeClientSocket : Ljava/net/Socket;
    //   504: aload #15
    //   506: sipush #3000
    //   509: invokevirtual connect : (Ljava/net/SocketAddress;I)V
    //   512: aload #12
    //   514: astore #5
    //   516: aload #4
    //   518: astore #6
    //   520: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   523: new java/lang/StringBuilder
    //   526: dup
    //   527: ldc_w 'ModeClient, Camera Connection Success... : ['
    //   530: invokespecial <init> : (Ljava/lang/String;)V
    //   533: aload #13
    //   535: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   538: ldc_w ':'
    //   541: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   544: iload_3
    //   545: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   548: ldc_w ']'
    //   551: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   554: invokevirtual toString : ()Ljava/lang/String;
    //   557: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   560: aload #12
    //   562: astore #5
    //   564: aload #4
    //   566: astore #6
    //   568: new java/io/BufferedOutputStream
    //   571: dup
    //   572: aload_0
    //   573: getfield mModeClientSocket : Ljava/net/Socket;
    //   576: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   579: invokespecial <init> : (Ljava/io/OutputStream;)V
    //   582: astore #4
    //   584: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   587: new java/lang/StringBuilder
    //   590: dup
    //   591: ldc_w 'ModeClient, OutputStream = '
    //   594: invokespecial <init> : (Ljava/lang/String;)V
    //   597: aload #14
    //   599: invokevirtual toString : ()Ljava/lang/String;
    //   602: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   605: invokevirtual toString : ()Ljava/lang/String;
    //   608: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   611: aload #4
    //   613: aload #14
    //   615: invokevirtual toString : ()Ljava/lang/String;
    //   618: invokevirtual getBytes : ()[B
    //   621: invokevirtual write : ([B)V
    //   624: aload #4
    //   626: invokevirtual flush : ()V
    //   629: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   632: ldc_w 'ModeClient, Discovery Request.'
    //   635: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   638: new java/io/BufferedInputStream
    //   641: dup
    //   642: aload_0
    //   643: getfield mModeClientSocket : Ljava/net/Socket;
    //   646: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   649: invokespecial <init> : (Ljava/io/InputStream;)V
    //   652: astore #5
    //   654: ldc_w ''
    //   657: astore #6
    //   659: sipush #356
    //   662: newarray byte
    //   664: astore #8
    //   666: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   669: ldc_w 'ModeClient, Camera Response Waiting...'
    //   672: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   675: aload #5
    //   677: aload #8
    //   679: iconst_0
    //   680: aload #8
    //   682: arraylength
    //   683: invokevirtual read : ([BII)I
    //   686: istore_3
    //   687: iload_3
    //   688: iconst_m1
    //   689: if_icmpne -> 836
    //   692: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   695: ldc_w 'ModeClient, Camera Response Received.'
    //   698: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   701: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   704: new java/lang/StringBuilder
    //   707: dup
    //   708: ldc_w 'ModeClient, InputStream = '
    //   711: invokespecial <init> : (Ljava/lang/String;)V
    //   714: aload #6
    //   716: invokevirtual toString : ()Ljava/lang/String;
    //   719: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   722: invokevirtual toString : ()Ljava/lang/String;
    //   725: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   728: aload #4
    //   730: ifnull -> 738
    //   733: aload #4
    //   735: invokevirtual close : ()V
    //   738: aload #5
    //   740: ifnull -> 748
    //   743: aload #5
    //   745: invokevirtual close : ()V
    //   748: aload_0
    //   749: getfield mModeClientSocket : Ljava/net/Socket;
    //   752: ifnull -> 762
    //   755: aload_0
    //   756: getfield mModeClientSocket : Ljava/net/Socket;
    //   759: invokevirtual close : ()V
    //   762: aload_0
    //   763: aload #6
    //   765: invokespecial checkResponse : (Ljava/lang/String;)I
    //   768: istore_3
    //   769: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   772: ldc_w 'ModeClient, finally!'
    //   775: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   778: aload #4
    //   780: ifnull -> 788
    //   783: aload #4
    //   785: invokevirtual close : ()V
    //   788: aload #5
    //   790: ifnull -> 798
    //   793: aload #5
    //   795: invokevirtual close : ()V
    //   798: aload_0
    //   799: getfield mModeClientSocket : Ljava/net/Socket;
    //   802: ifnull -> 812
    //   805: aload_0
    //   806: getfield mModeClientSocket : Ljava/net/Socket;
    //   809: invokevirtual close : ()V
    //   812: iload_3
    //   813: ifle -> 1449
    //   816: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   819: ldc_w 'ModeClient, DONE.'
    //   822: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   825: iload_3
    //   826: bipush #23
    //   828: if_icmpne -> 1443
    //   831: aload_0
    //   832: invokespecial performRunByebye : ()V
    //   835: return
    //   836: new java/lang/StringBuilder
    //   839: dup
    //   840: aload #6
    //   842: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   845: invokespecial <init> : (Ljava/lang/String;)V
    //   848: new java/lang/String
    //   851: dup
    //   852: aload #8
    //   854: iconst_0
    //   855: iload_3
    //   856: invokespecial <init> : ([BII)V
    //   859: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   862: invokevirtual toString : ()Ljava/lang/String;
    //   865: astore #7
    //   867: aload #7
    //   869: ldc_w '\\n'
    //   872: invokevirtual indexOf : (Ljava/lang/String;)I
    //   875: istore_3
    //   876: aload #7
    //   878: astore #6
    //   880: iload_3
    //   881: iconst_m1
    //   882: if_icmpeq -> 675
    //   885: aload #7
    //   887: astore #6
    //   889: goto -> 692
    //   892: astore #4
    //   894: aload #11
    //   896: astore #4
    //   898: aload #8
    //   900: astore #7
    //   902: aload #7
    //   904: astore #5
    //   906: aload #4
    //   908: astore #6
    //   910: getstatic com/samsungimaging/connectionmanager/util/Trace$Tag.CM : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   913: ldc_w 'delay start'
    //   916: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   919: aload #7
    //   921: astore #5
    //   923: aload #4
    //   925: astore #6
    //   927: ldc2_w 500
    //   930: invokestatic sleep : (J)V
    //   933: aload #7
    //   935: astore #5
    //   937: aload #4
    //   939: astore #6
    //   941: getstatic com/samsungimaging/connectionmanager/util/Trace$Tag.CM : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   944: ldc_w 'delay end'
    //   947: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   950: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   953: ldc_w 'ModeClient, finally!'
    //   956: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   959: aload #4
    //   961: ifnull -> 969
    //   964: aload #4
    //   966: invokevirtual close : ()V
    //   969: aload #7
    //   971: ifnull -> 979
    //   974: aload #7
    //   976: invokevirtual close : ()V
    //   979: aload_0
    //   980: getfield mModeClientSocket : Ljava/net/Socket;
    //   983: ifnull -> 993
    //   986: aload_0
    //   987: getfield mModeClientSocket : Ljava/net/Socket;
    //   990: invokevirtual close : ()V
    //   993: iload_2
    //   994: ifle -> 1140
    //   997: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1000: ldc_w 'ModeClient, DONE.'
    //   1003: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1006: iload_2
    //   1007: bipush #23
    //   1009: if_icmpne -> 1134
    //   1012: aload_0
    //   1013: invokespecial performRunByebye : ()V
    //   1016: return
    //   1017: astore #8
    //   1019: aload #7
    //   1021: astore #5
    //   1023: aload #4
    //   1025: astore #6
    //   1027: aload #8
    //   1029: invokevirtual printStackTrace : ()V
    //   1032: goto -> 950
    //   1035: astore #4
    //   1037: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1040: ldc_w 'ModeClient, finally!'
    //   1043: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1046: aload #6
    //   1048: ifnull -> 1056
    //   1051: aload #6
    //   1053: invokevirtual close : ()V
    //   1056: aload #5
    //   1058: ifnull -> 1066
    //   1061: aload #5
    //   1063: invokevirtual close : ()V
    //   1066: aload_0
    //   1067: getfield mModeClientSocket : Ljava/net/Socket;
    //   1070: ifnull -> 1080
    //   1073: aload_0
    //   1074: getfield mModeClientSocket : Ljava/net/Socket;
    //   1077: invokevirtual close : ()V
    //   1080: iload_2
    //   1081: ifle -> 1385
    //   1084: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1087: ldc_w 'ModeClient, DONE.'
    //   1090: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1093: iload_2
    //   1094: bipush #23
    //   1096: if_icmpne -> 1379
    //   1099: aload_0
    //   1100: invokespecial performRunByebye : ()V
    //   1103: return
    //   1104: astore #4
    //   1106: aload #4
    //   1108: invokevirtual printStackTrace : ()V
    //   1111: goto -> 969
    //   1114: astore #4
    //   1116: aload #4
    //   1118: invokevirtual printStackTrace : ()V
    //   1121: goto -> 979
    //   1124: astore #4
    //   1126: aload #4
    //   1128: invokevirtual printStackTrace : ()V
    //   1131: goto -> 993
    //   1134: aload_0
    //   1135: iload_2
    //   1136: invokespecial performRunSubApplication : (I)V
    //   1139: return
    //   1140: iload_1
    //   1141: iconst_1
    //   1142: iadd
    //   1143: bipush #16
    //   1145: if_icmpne -> 1164
    //   1148: aload_0
    //   1149: iconst_0
    //   1150: invokespecial performRunSubApplication : (I)V
    //   1153: iload_2
    //   1154: istore_3
    //   1155: iload_1
    //   1156: iconst_1
    //   1157: iadd
    //   1158: istore_1
    //   1159: iload_3
    //   1160: istore_2
    //   1161: goto -> 4
    //   1164: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1167: ldc_w 'ModeClient, retry!'
    //   1170: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1173: iload_2
    //   1174: istore_3
    //   1175: goto -> 1155
    //   1178: astore #8
    //   1180: aload #10
    //   1182: astore #4
    //   1184: aload #7
    //   1186: astore #5
    //   1188: aload #4
    //   1190: astore #6
    //   1192: aload #8
    //   1194: invokevirtual printStackTrace : ()V
    //   1197: aload #7
    //   1199: astore #5
    //   1201: aload #4
    //   1203: astore #6
    //   1205: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1208: ldc_w 'ModeClient, Exception!'
    //   1211: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1214: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1217: ldc_w 'ModeClient, finally!'
    //   1220: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1223: aload #4
    //   1225: ifnull -> 1233
    //   1228: aload #4
    //   1230: invokevirtual close : ()V
    //   1233: aload #7
    //   1235: ifnull -> 1243
    //   1238: aload #7
    //   1240: invokevirtual close : ()V
    //   1243: aload_0
    //   1244: getfield mModeClientSocket : Ljava/net/Socket;
    //   1247: ifnull -> 1257
    //   1250: aload_0
    //   1251: getfield mModeClientSocket : Ljava/net/Socket;
    //   1254: invokevirtual close : ()V
    //   1257: iload_2
    //   1258: ifle -> 1317
    //   1261: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1264: ldc_w 'ModeClient, DONE.'
    //   1267: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1270: iload_2
    //   1271: bipush #23
    //   1273: if_icmpne -> 1311
    //   1276: aload_0
    //   1277: invokespecial performRunByebye : ()V
    //   1280: return
    //   1281: astore #4
    //   1283: aload #4
    //   1285: invokevirtual printStackTrace : ()V
    //   1288: goto -> 1233
    //   1291: astore #4
    //   1293: aload #4
    //   1295: invokevirtual printStackTrace : ()V
    //   1298: goto -> 1243
    //   1301: astore #4
    //   1303: aload #4
    //   1305: invokevirtual printStackTrace : ()V
    //   1308: goto -> 1257
    //   1311: aload_0
    //   1312: iload_2
    //   1313: invokespecial performRunSubApplication : (I)V
    //   1316: return
    //   1317: iload_1
    //   1318: iconst_1
    //   1319: iadd
    //   1320: bipush #16
    //   1322: if_icmpne -> 1335
    //   1325: aload_0
    //   1326: iconst_0
    //   1327: invokespecial performRunSubApplication : (I)V
    //   1330: iload_2
    //   1331: istore_3
    //   1332: goto -> 1155
    //   1335: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1338: ldc_w 'ModeClient, retry!'
    //   1341: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1344: iload_2
    //   1345: istore_3
    //   1346: goto -> 1155
    //   1349: astore #6
    //   1351: aload #6
    //   1353: invokevirtual printStackTrace : ()V
    //   1356: goto -> 1056
    //   1359: astore #5
    //   1361: aload #5
    //   1363: invokevirtual printStackTrace : ()V
    //   1366: goto -> 1066
    //   1369: astore #5
    //   1371: aload #5
    //   1373: invokevirtual printStackTrace : ()V
    //   1376: goto -> 1080
    //   1379: aload_0
    //   1380: iload_2
    //   1381: invokespecial performRunSubApplication : (I)V
    //   1384: return
    //   1385: iload_1
    //   1386: iconst_1
    //   1387: iadd
    //   1388: bipush #16
    //   1390: if_icmpne -> 1401
    //   1393: aload_0
    //   1394: iconst_0
    //   1395: invokespecial performRunSubApplication : (I)V
    //   1398: aload #4
    //   1400: athrow
    //   1401: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1404: ldc_w 'ModeClient, retry!'
    //   1407: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1410: goto -> 1398
    //   1413: astore #4
    //   1415: aload #4
    //   1417: invokevirtual printStackTrace : ()V
    //   1420: goto -> 788
    //   1423: astore #4
    //   1425: aload #4
    //   1427: invokevirtual printStackTrace : ()V
    //   1430: goto -> 798
    //   1433: astore #4
    //   1435: aload #4
    //   1437: invokevirtual printStackTrace : ()V
    //   1440: goto -> 812
    //   1443: aload_0
    //   1444: iload_3
    //   1445: invokespecial performRunSubApplication : (I)V
    //   1448: return
    //   1449: iload_1
    //   1450: iconst_1
    //   1451: iadd
    //   1452: bipush #16
    //   1454: if_icmpne -> 1465
    //   1457: aload_0
    //   1458: iconst_0
    //   1459: invokespecial performRunSubApplication : (I)V
    //   1462: goto -> 1155
    //   1465: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1468: ldc_w 'ModeClient, retry!'
    //   1471: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1474: goto -> 1155
    //   1477: astore #7
    //   1479: aload #9
    //   1481: astore #5
    //   1483: aload #4
    //   1485: astore #6
    //   1487: aload #7
    //   1489: astore #4
    //   1491: goto -> 1037
    //   1494: astore #7
    //   1496: aload #4
    //   1498: astore #6
    //   1500: aload #7
    //   1502: astore #4
    //   1504: goto -> 1037
    //   1507: astore #8
    //   1509: goto -> 1184
    //   1512: astore #8
    //   1514: aload #5
    //   1516: astore #7
    //   1518: goto -> 1184
    //   1521: astore #5
    //   1523: aload #8
    //   1525: astore #7
    //   1527: goto -> 902
    //   1530: astore #6
    //   1532: aload #5
    //   1534: astore #7
    //   1536: goto -> 902
    // Exception table:
    //   from	to	target	type
    //   413	453	892	java/net/ConnectException
    //   413	453	1178	java/lang/Exception
    //   413	453	1035	finally
    //   461	472	892	java/net/ConnectException
    //   461	472	1178	java/lang/Exception
    //   461	472	1035	finally
    //   480	492	892	java/net/ConnectException
    //   480	492	1178	java/lang/Exception
    //   480	492	1035	finally
    //   500	512	892	java/net/ConnectException
    //   500	512	1178	java/lang/Exception
    //   500	512	1035	finally
    //   520	560	892	java/net/ConnectException
    //   520	560	1178	java/lang/Exception
    //   520	560	1035	finally
    //   568	584	892	java/net/ConnectException
    //   568	584	1178	java/lang/Exception
    //   568	584	1035	finally
    //   584	654	1521	java/net/ConnectException
    //   584	654	1507	java/lang/Exception
    //   584	654	1477	finally
    //   659	675	1530	java/net/ConnectException
    //   659	675	1512	java/lang/Exception
    //   659	675	1494	finally
    //   675	687	1530	java/net/ConnectException
    //   675	687	1512	java/lang/Exception
    //   675	687	1494	finally
    //   692	728	1530	java/net/ConnectException
    //   692	728	1512	java/lang/Exception
    //   692	728	1494	finally
    //   733	738	1530	java/net/ConnectException
    //   733	738	1512	java/lang/Exception
    //   733	738	1494	finally
    //   743	748	1530	java/net/ConnectException
    //   743	748	1512	java/lang/Exception
    //   743	748	1494	finally
    //   748	762	1530	java/net/ConnectException
    //   748	762	1512	java/lang/Exception
    //   748	762	1494	finally
    //   762	769	1530	java/net/ConnectException
    //   762	769	1512	java/lang/Exception
    //   762	769	1494	finally
    //   783	788	1413	java/io/IOException
    //   793	798	1423	java/io/IOException
    //   805	812	1433	java/io/IOException
    //   836	876	1530	java/net/ConnectException
    //   836	876	1512	java/lang/Exception
    //   836	876	1494	finally
    //   910	919	1017	java/lang/InterruptedException
    //   910	919	1035	finally
    //   927	933	1017	java/lang/InterruptedException
    //   927	933	1035	finally
    //   941	950	1017	java/lang/InterruptedException
    //   941	950	1035	finally
    //   964	969	1104	java/io/IOException
    //   974	979	1114	java/io/IOException
    //   986	993	1124	java/io/IOException
    //   1027	1032	1035	finally
    //   1051	1056	1349	java/io/IOException
    //   1061	1066	1359	java/io/IOException
    //   1073	1080	1369	java/io/IOException
    //   1192	1197	1035	finally
    //   1205	1214	1035	finally
    //   1228	1233	1281	java/io/IOException
    //   1238	1243	1291	java/io/IOException
    //   1250	1257	1301	java/io/IOException
  }
  
  public void runModeClient(boolean paramBoolean) {
    Trace.d(CMConstants.TAG_NAME, "ModeClient, runModeClient(), start!!!, mRunModeClient = " + this.mRunModeClient);
    if (this.mRunModeClient == null) {
      Trace.d(CMConstants.TAG_NAME, "ModeClient, runModeClient(), start!!!");
      this.mRunModeClient = new Thread(new ModeClient(paramBoolean));
      this.mRunModeClient.start();
      return;
    } 
    Trace.d(CMConstants.TAG_NAME, "ModeClient, runModeClient(), already started!!!");
  }
  
  public void stopModeClient() {
    Trace.d(CMConstants.TAG_NAME, "ModeClient, stopModeClient()");
    clearModeClientListner();
    if (this.mModeClientSocket != null)
      try {
        this.mModeClientSocket.close();
        this.mModeClientSocket = null;
      } catch (IOException iOException) {
        iOException.printStackTrace();
        Trace.d(CMConstants.TAG_NAME, "ModeClient, stopModeClient(), mModeClientSocket exception!!!");
      }  
    Trace.d(CMConstants.TAG_NAME, "ModeClient, stopModeClient(), mRunModeClient = " + this.mRunModeClient);
    if (this.mRunModeClient != null) {
      Trace.d(CMConstants.TAG_NAME, "ModeClient, stopModeClient()");
      this.mRunModeClient.interrupt();
      this.mRunModeClient = null;
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\modemanager\ModeClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */