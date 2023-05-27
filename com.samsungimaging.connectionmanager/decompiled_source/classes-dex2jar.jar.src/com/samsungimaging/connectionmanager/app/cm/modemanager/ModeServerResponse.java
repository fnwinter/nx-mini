package com.samsungimaging.connectionmanager.app.cm.modemanager;

import com.samsungimaging.connectionmanager.app.cm.common.CMConstants;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;
import com.samsungimaging.connectionmanager.util.Trace;
import java.net.Socket;

public class ModeServerResponse extends Thread {
  private Socket mClient = null;
  
  public ModeServerResponse(Socket paramSocket) {
    Trace.d(CMConstants.TAG_NAME, "ModeServerResponse, construct.");
    this.mClient = paramSocket;
  }
  
  private int checkRequest(String paramString) {
    if (paramString.toLowerCase().contains("samsung mode server")) {
      if (paramString.toLowerCase().contains("<currentmode>mobilelink</currentmode>")) {
        boolean bool = true;
        Trace.d(CMConstants.TAG_NAME, "ModeServerResponse, Current ACTIVE_SERVICE is " + CMService.ACTIVE_SERVICE + ", return request Type is " + bool);
        return bool;
      } 
      if (paramString.toLowerCase().contains("<currentmode>rvf</currentmode>")) {
        byte b2 = 2;
        Trace.d(CMConstants.TAG_NAME, "ModeServerResponse, Current ACTIVE_SERVICE is " + CMService.ACTIVE_SERVICE + ", return request Type is " + b2);
        return b2;
      } 
      if (paramString.toLowerCase().contains("<currentmode>autoshare</currentmode>")) {
        byte b2 = 3;
        Trace.d(CMConstants.TAG_NAME, "ModeServerResponse, Current ACTIVE_SERVICE is " + CMService.ACTIVE_SERVICE + ", return request Type is " + b2);
        return b2;
      } 
      if (paramString.toLowerCase().contains("<currentmode>selectivepush</currentmode>")) {
        byte b2 = 4;
        Trace.d(CMConstants.TAG_NAME, "ModeServerResponse, Current ACTIVE_SERVICE is " + CMService.ACTIVE_SERVICE + ", return request Type is " + b2);
        return b2;
      } 
      if (paramString.toLowerCase().contains("<currentmode>groupshare</currentmode>")) {
        byte b2 = 5;
        Trace.d(CMConstants.TAG_NAME, "ModeServerResponse, Current ACTIVE_SERVICE is " + CMService.ACTIVE_SERVICE + ", return request Type is " + b2);
        return b2;
      } 
      if (paramString.toLowerCase().contains("<currentmode>idle</currentmode>")) {
        byte b2 = 7;
        Trace.d(CMConstants.TAG_NAME, "ModeServerResponse, Current ACTIVE_SERVICE is " + CMService.ACTIVE_SERVICE + ", return request Type is " + b2);
        return b2;
      } 
      if (paramString.toLowerCase().contains("<currentmode>mobilebackup</currentmode>")) {
        byte b2 = 6;
        Trace.d(CMConstants.TAG_NAME, "ModeServerResponse, Current ACTIVE_SERVICE is " + CMService.ACTIVE_SERVICE + ", return request Type is " + b2);
        return b2;
      } 
      if (paramString.toLowerCase().contains("byebye")) {
        byte b2 = 23;
        Trace.d(CMConstants.TAG_NAME, "ModeServerResponse, Current ACTIVE_SERVICE is " + CMService.ACTIVE_SERVICE + ", return request Type is " + b2);
        return b2;
      } 
      byte b1 = 20;
      Trace.d(CMConstants.TAG_NAME, "ModeServerResponse, Current ACTIVE_SERVICE is " + CMService.ACTIVE_SERVICE + ", return request Type is " + b1);
      return b1;
    } 
    if (paramString.toLowerCase().contains("byebye")) {
      byte b1 = 23;
      Trace.d(CMConstants.TAG_NAME, "ModeServerResponse, Current ACTIVE_SERVICE is " + CMService.ACTIVE_SERVICE + ", return request Type is " + b1);
      return b1;
    } 
    byte b = 20;
    Trace.d(CMConstants.TAG_NAME, "ModeServerResponse, Current ACTIVE_SERVICE is " + CMService.ACTIVE_SERVICE + ", return request Type is " + b);
    return b;
  }
  
  public void run() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mClient : Ljava/net/Socket;
    //   4: ifnonnull -> 8
    //   7: return
    //   8: aconst_null
    //   9: astore #8
    //   11: aconst_null
    //   12: astore #10
    //   14: aconst_null
    //   15: astore #9
    //   17: aconst_null
    //   18: astore #7
    //   20: aconst_null
    //   21: astore #11
    //   23: iconst_0
    //   24: istore_3
    //   25: iconst_0
    //   26: istore_2
    //   27: iconst_0
    //   28: istore #4
    //   30: iconst_0
    //   31: istore_1
    //   32: new java/io/BufferedInputStream
    //   35: dup
    //   36: aload_0
    //   37: getfield mClient : Ljava/net/Socket;
    //   40: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   43: invokespecial <init> : (Ljava/io/InputStream;)V
    //   46: astore #6
    //   48: ldc ''
    //   50: astore #7
    //   52: iload_1
    //   53: istore_2
    //   54: iload #4
    //   56: istore_3
    //   57: sipush #356
    //   60: newarray byte
    //   62: astore #11
    //   64: iload_1
    //   65: istore_2
    //   66: iload #4
    //   68: istore_3
    //   69: aload #6
    //   71: aload #11
    //   73: iconst_0
    //   74: aload #11
    //   76: arraylength
    //   77: invokevirtual read : ([BII)I
    //   80: istore #5
    //   82: iload #5
    //   84: iconst_m1
    //   85: if_icmpne -> 295
    //   88: iload_1
    //   89: istore_2
    //   90: iload #4
    //   92: istore_3
    //   93: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   96: ldc 'ModeServerResponse, Request Received.'
    //   98: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   101: iload_1
    //   102: istore_2
    //   103: iload #4
    //   105: istore_3
    //   106: aload_0
    //   107: aload #7
    //   109: invokespecial checkRequest : (Ljava/lang/String;)I
    //   112: istore_1
    //   113: iload_1
    //   114: istore_2
    //   115: iload_1
    //   116: istore_3
    //   117: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   120: ldc 'ModeServerResponse, preparing Response...'
    //   122: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   125: iload_1
    //   126: istore_2
    //   127: iload_1
    //   128: istore_3
    //   129: new java/lang/StringBuffer
    //   132: dup
    //   133: invokespecial <init> : ()V
    //   136: astore #8
    //   138: iload_1
    //   139: bipush #20
    //   141: if_icmpne -> 408
    //   144: iload_1
    //   145: istore_2
    //   146: iload_1
    //   147: istore_3
    //   148: aload #8
    //   150: ldc 'HTTP/1.1 400 Bad Request'
    //   152: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   155: ldc '\\r\\n'
    //   157: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   160: ldc 'Content-Length: 0'
    //   162: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   165: ldc '\\r\\n'
    //   167: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   170: ldc '\\r\\n'
    //   172: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   175: pop
    //   176: iload_1
    //   177: istore_2
    //   178: iload_1
    //   179: istore_3
    //   180: new java/io/BufferedOutputStream
    //   183: dup
    //   184: aload_0
    //   185: getfield mClient : Ljava/net/Socket;
    //   188: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   191: invokespecial <init> : (Ljava/io/OutputStream;)V
    //   194: astore #7
    //   196: aload #7
    //   198: aload #8
    //   200: invokevirtual toString : ()Ljava/lang/String;
    //   203: invokevirtual getBytes : ()[B
    //   206: invokevirtual write : ([B)V
    //   209: aload #7
    //   211: invokevirtual flush : ()V
    //   214: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   217: new java/lang/StringBuilder
    //   220: dup
    //   221: ldc 'ModeServerResponse, Response flush done!, response = '
    //   223: invokespecial <init> : (Ljava/lang/String;)V
    //   226: aload #8
    //   228: invokevirtual toString : ()Ljava/lang/String;
    //   231: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   234: invokevirtual toString : ()Ljava/lang/String;
    //   237: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   240: aload #6
    //   242: ifnull -> 250
    //   245: aload #6
    //   247: invokevirtual close : ()V
    //   250: aload #7
    //   252: ifnull -> 260
    //   255: aload #7
    //   257: invokevirtual close : ()V
    //   260: aload_0
    //   261: getfield mClient : Ljava/net/Socket;
    //   264: ifnull -> 274
    //   267: aload_0
    //   268: getfield mClient : Ljava/net/Socket;
    //   271: invokevirtual close : ()V
    //   274: iload_1
    //   275: bipush #23
    //   277: if_icmpne -> 906
    //   280: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer;
    //   283: invokevirtual performRunByebye : ()V
    //   286: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   289: ldc 'ModeServerResponse, DONE.'
    //   291: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   294: return
    //   295: iload_1
    //   296: istore_2
    //   297: iload #4
    //   299: istore_3
    //   300: new java/lang/StringBuilder
    //   303: dup
    //   304: aload #7
    //   306: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   309: invokespecial <init> : (Ljava/lang/String;)V
    //   312: new java/lang/String
    //   315: dup
    //   316: aload #11
    //   318: iconst_0
    //   319: iload #5
    //   321: invokespecial <init> : ([BII)V
    //   324: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   327: invokevirtual toString : ()Ljava/lang/String;
    //   330: astore #8
    //   332: iload_1
    //   333: istore_2
    //   334: iload #4
    //   336: istore_3
    //   337: aload #8
    //   339: ldc '\\r\\n\\r\\n'
    //   341: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   344: astore #12
    //   346: iload_1
    //   347: istore_2
    //   348: iload #4
    //   350: istore_3
    //   351: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   354: new java/lang/StringBuilder
    //   357: dup
    //   358: ldc 'ModeServerResponse, header parsing...str = '
    //   360: invokespecial <init> : (Ljava/lang/String;)V
    //   363: aload #8
    //   365: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   368: ldc ', header_array.length = '
    //   370: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   373: aload #12
    //   375: arraylength
    //   376: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   379: invokevirtual toString : ()Ljava/lang/String;
    //   382: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   385: aload #8
    //   387: astore #7
    //   389: iload_1
    //   390: istore_2
    //   391: iload #4
    //   393: istore_3
    //   394: aload #12
    //   396: arraylength
    //   397: iconst_2
    //   398: if_icmplt -> 64
    //   401: aload #8
    //   403: astore #7
    //   405: goto -> 88
    //   408: iload_1
    //   409: istore_2
    //   410: iload_1
    //   411: istore_3
    //   412: aload #8
    //   414: ldc 'HTTP/1.1 200 OK'
    //   416: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   419: ldc '\\r\\n'
    //   421: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   424: ldc 'Content-Length: 0'
    //   426: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   429: ldc '\\r\\n'
    //   431: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   434: ldc '\\r\\n'
    //   436: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   439: pop
    //   440: goto -> 176
    //   443: astore #10
    //   445: iload_2
    //   446: istore_1
    //   447: aload #6
    //   449: astore #7
    //   451: aload #9
    //   453: astore #8
    //   455: iload_1
    //   456: istore_2
    //   457: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   460: ldc 'ModeServerResponse, Exception01'
    //   462: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   465: aload #6
    //   467: astore #7
    //   469: aload #9
    //   471: astore #8
    //   473: iload_1
    //   474: istore_2
    //   475: aload #10
    //   477: invokevirtual printStackTrace : ()V
    //   480: aload #6
    //   482: ifnull -> 490
    //   485: aload #6
    //   487: invokevirtual close : ()V
    //   490: aload #9
    //   492: ifnull -> 500
    //   495: aload #9
    //   497: invokevirtual close : ()V
    //   500: aload_0
    //   501: getfield mClient : Ljava/net/Socket;
    //   504: ifnull -> 514
    //   507: aload_0
    //   508: getfield mClient : Ljava/net/Socket;
    //   511: invokevirtual close : ()V
    //   514: iload_1
    //   515: bipush #23
    //   517: if_icmpne -> 545
    //   520: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer;
    //   523: invokevirtual performRunByebye : ()V
    //   526: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   529: ldc 'ModeServerResponse, DONE.'
    //   531: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   534: return
    //   535: astore #6
    //   537: aload #6
    //   539: invokevirtual printStackTrace : ()V
    //   542: goto -> 514
    //   545: iload_1
    //   546: iconst_1
    //   547: if_icmpeq -> 576
    //   550: iload_1
    //   551: iconst_2
    //   552: if_icmpeq -> 576
    //   555: iload_1
    //   556: iconst_3
    //   557: if_icmpeq -> 576
    //   560: iload_1
    //   561: iconst_4
    //   562: if_icmpeq -> 576
    //   565: iload_1
    //   566: iconst_5
    //   567: if_icmpeq -> 576
    //   570: iload_1
    //   571: bipush #6
    //   573: if_icmpne -> 675
    //   576: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   579: invokevirtual isSubAppAlive : ()Z
    //   582: ifeq -> 665
    //   585: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   588: new java/lang/StringBuilder
    //   591: dup
    //   592: ldc 'ModeServerResponse, already sub service running..., ACTIVE_SERVICE = '
    //   594: invokespecial <init> : (Ljava/lang/String;)V
    //   597: getstatic com/samsungimaging/connectionmanager/app/cm/service/CMService.ACTIVE_SERVICE : I
    //   600: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   603: ldc ', requestType = '
    //   605: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   608: iload_1
    //   609: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   612: invokevirtual toString : ()Ljava/lang/String;
    //   615: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   618: iload_1
    //   619: getstatic com/samsungimaging/connectionmanager/app/cm/service/CMService.ACTIVE_SERVICE : I
    //   622: if_icmpeq -> 526
    //   625: iconst_0
    //   626: istore_2
    //   627: iload_2
    //   628: bipush #6
    //   630: if_icmpge -> 526
    //   633: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   636: invokevirtual isSubAppAlive : ()Z
    //   639: ifne -> 652
    //   642: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer;
    //   645: iload_1
    //   646: invokevirtual performRunSubApplication : (I)V
    //   649: goto -> 526
    //   652: ldc2_w 500
    //   655: invokestatic sleep : (J)V
    //   658: iload_2
    //   659: iconst_1
    //   660: iadd
    //   661: istore_2
    //   662: goto -> 627
    //   665: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer;
    //   668: iload_1
    //   669: invokevirtual performRunSubApplication : (I)V
    //   672: goto -> 526
    //   675: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer;
    //   678: iload_1
    //   679: invokevirtual performRunSubApplication : (I)V
    //   682: goto -> 526
    //   685: astore #6
    //   687: iload_2
    //   688: istore_1
    //   689: aload #7
    //   691: ifnull -> 699
    //   694: aload #7
    //   696: invokevirtual close : ()V
    //   699: aload #8
    //   701: ifnull -> 709
    //   704: aload #8
    //   706: invokevirtual close : ()V
    //   709: aload_0
    //   710: getfield mClient : Ljava/net/Socket;
    //   713: ifnull -> 723
    //   716: aload_0
    //   717: getfield mClient : Ljava/net/Socket;
    //   720: invokevirtual close : ()V
    //   723: iload_1
    //   724: bipush #23
    //   726: if_icmpne -> 756
    //   729: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer;
    //   732: invokevirtual performRunByebye : ()V
    //   735: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   738: ldc 'ModeServerResponse, DONE.'
    //   740: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   743: aload #6
    //   745: athrow
    //   746: astore #7
    //   748: aload #7
    //   750: invokevirtual printStackTrace : ()V
    //   753: goto -> 723
    //   756: iload_1
    //   757: iconst_1
    //   758: if_icmpeq -> 787
    //   761: iload_1
    //   762: iconst_2
    //   763: if_icmpeq -> 787
    //   766: iload_1
    //   767: iconst_3
    //   768: if_icmpeq -> 787
    //   771: iload_1
    //   772: iconst_4
    //   773: if_icmpeq -> 787
    //   776: iload_1
    //   777: iconst_5
    //   778: if_icmpeq -> 787
    //   781: iload_1
    //   782: bipush #6
    //   784: if_icmpne -> 886
    //   787: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   790: invokevirtual isSubAppAlive : ()Z
    //   793: ifeq -> 876
    //   796: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   799: new java/lang/StringBuilder
    //   802: dup
    //   803: ldc 'ModeServerResponse, already sub service running..., ACTIVE_SERVICE = '
    //   805: invokespecial <init> : (Ljava/lang/String;)V
    //   808: getstatic com/samsungimaging/connectionmanager/app/cm/service/CMService.ACTIVE_SERVICE : I
    //   811: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   814: ldc ', requestType = '
    //   816: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   819: iload_1
    //   820: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   823: invokevirtual toString : ()Ljava/lang/String;
    //   826: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   829: iload_1
    //   830: getstatic com/samsungimaging/connectionmanager/app/cm/service/CMService.ACTIVE_SERVICE : I
    //   833: if_icmpeq -> 735
    //   836: iconst_0
    //   837: istore_2
    //   838: iload_2
    //   839: bipush #6
    //   841: if_icmpge -> 735
    //   844: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   847: invokevirtual isSubAppAlive : ()Z
    //   850: ifne -> 863
    //   853: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer;
    //   856: iload_1
    //   857: invokevirtual performRunSubApplication : (I)V
    //   860: goto -> 735
    //   863: ldc2_w 500
    //   866: invokestatic sleep : (J)V
    //   869: iload_2
    //   870: iconst_1
    //   871: iadd
    //   872: istore_2
    //   873: goto -> 838
    //   876: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer;
    //   879: iload_1
    //   880: invokevirtual performRunSubApplication : (I)V
    //   883: goto -> 735
    //   886: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer;
    //   889: iload_1
    //   890: invokevirtual performRunSubApplication : (I)V
    //   893: goto -> 735
    //   896: astore #6
    //   898: aload #6
    //   900: invokevirtual printStackTrace : ()V
    //   903: goto -> 274
    //   906: iload_1
    //   907: iconst_1
    //   908: if_icmpeq -> 937
    //   911: iload_1
    //   912: iconst_2
    //   913: if_icmpeq -> 937
    //   916: iload_1
    //   917: iconst_3
    //   918: if_icmpeq -> 937
    //   921: iload_1
    //   922: iconst_4
    //   923: if_icmpeq -> 937
    //   926: iload_1
    //   927: iconst_5
    //   928: if_icmpeq -> 937
    //   931: iload_1
    //   932: bipush #6
    //   934: if_icmpne -> 1036
    //   937: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   940: invokevirtual isSubAppAlive : ()Z
    //   943: ifeq -> 1026
    //   946: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   949: new java/lang/StringBuilder
    //   952: dup
    //   953: ldc 'ModeServerResponse, already sub service running..., ACTIVE_SERVICE = '
    //   955: invokespecial <init> : (Ljava/lang/String;)V
    //   958: getstatic com/samsungimaging/connectionmanager/app/cm/service/CMService.ACTIVE_SERVICE : I
    //   961: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   964: ldc ', requestType = '
    //   966: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   969: iload_1
    //   970: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   973: invokevirtual toString : ()Ljava/lang/String;
    //   976: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   979: iload_1
    //   980: getstatic com/samsungimaging/connectionmanager/app/cm/service/CMService.ACTIVE_SERVICE : I
    //   983: if_icmpeq -> 286
    //   986: iconst_0
    //   987: istore_2
    //   988: iload_2
    //   989: bipush #6
    //   991: if_icmpge -> 286
    //   994: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   997: invokevirtual isSubAppAlive : ()Z
    //   1000: ifne -> 1013
    //   1003: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer;
    //   1006: iload_1
    //   1007: invokevirtual performRunSubApplication : (I)V
    //   1010: goto -> 286
    //   1013: ldc2_w 500
    //   1016: invokestatic sleep : (J)V
    //   1019: iload_2
    //   1020: iconst_1
    //   1021: iadd
    //   1022: istore_2
    //   1023: goto -> 988
    //   1026: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer;
    //   1029: iload_1
    //   1030: invokevirtual performRunSubApplication : (I)V
    //   1033: goto -> 286
    //   1036: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer;
    //   1039: iload_1
    //   1040: invokevirtual performRunSubApplication : (I)V
    //   1043: goto -> 286
    //   1046: astore #9
    //   1048: aload #6
    //   1050: astore #7
    //   1052: aload #10
    //   1054: astore #8
    //   1056: iload_3
    //   1057: istore_1
    //   1058: aload #9
    //   1060: astore #6
    //   1062: goto -> 689
    //   1065: astore #9
    //   1067: aload #7
    //   1069: astore #8
    //   1071: aload #6
    //   1073: astore #7
    //   1075: aload #9
    //   1077: astore #6
    //   1079: goto -> 689
    //   1082: astore #10
    //   1084: aload #11
    //   1086: astore #6
    //   1088: iload_3
    //   1089: istore_1
    //   1090: goto -> 447
    //   1093: astore #10
    //   1095: aload #7
    //   1097: astore #9
    //   1099: goto -> 447
    // Exception table:
    //   from	to	target	type
    //   32	48	1082	java/lang/Exception
    //   32	48	685	finally
    //   57	64	443	java/lang/Exception
    //   57	64	1046	finally
    //   69	82	443	java/lang/Exception
    //   69	82	1046	finally
    //   93	101	443	java/lang/Exception
    //   93	101	1046	finally
    //   106	113	443	java/lang/Exception
    //   106	113	1046	finally
    //   117	125	443	java/lang/Exception
    //   117	125	1046	finally
    //   129	138	443	java/lang/Exception
    //   129	138	1046	finally
    //   148	176	443	java/lang/Exception
    //   148	176	1046	finally
    //   180	196	443	java/lang/Exception
    //   180	196	1046	finally
    //   196	240	1093	java/lang/Exception
    //   196	240	1065	finally
    //   245	250	896	java/io/IOException
    //   255	260	896	java/io/IOException
    //   260	274	896	java/io/IOException
    //   300	332	443	java/lang/Exception
    //   300	332	1046	finally
    //   337	346	443	java/lang/Exception
    //   337	346	1046	finally
    //   351	385	443	java/lang/Exception
    //   351	385	1046	finally
    //   394	401	443	java/lang/Exception
    //   394	401	1046	finally
    //   412	440	443	java/lang/Exception
    //   412	440	1046	finally
    //   457	465	685	finally
    //   475	480	685	finally
    //   485	490	535	java/io/IOException
    //   495	500	535	java/io/IOException
    //   500	514	535	java/io/IOException
    //   694	699	746	java/io/IOException
    //   704	709	746	java/io/IOException
    //   709	723	746	java/io/IOException
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\modemanager\ModeServerResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */