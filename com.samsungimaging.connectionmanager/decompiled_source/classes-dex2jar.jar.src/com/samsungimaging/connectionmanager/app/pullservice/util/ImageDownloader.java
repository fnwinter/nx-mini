package com.samsungimaging.connectionmanager.app.pullservice.util;

import android.os.Handler;
import com.samsungimaging.connectionmanager.util.Trace;
import java.util.Stack;

public class ImageDownloader {
  private Trace.Tag TAG = Trace.Tag.RVF;
  
  Downloader downloader = new Downloader();
  
  Handler handler;
  
  ImageDownloadQueue imageDownloadQueue = new ImageDownloadQueue();
  
  int messageID;
  
  public ImageDownloader(Handler paramHandler, int paramInt) {
    this.handler = paramHandler;
    this.messageID = paramInt;
  }
  
  public void downloadImage(String paramString) {
    String[] arrayOfString = paramString.split("/");
    null = new ImageItem(paramString, FileManager.createFileName(arrayOfString[arrayOfString.length - 1]));
    synchronized (this.imageDownloadQueue.imageItems) {
      this.imageDownloadQueue.imageItems.push(null);
      this.imageDownloadQueue.imageItems.notifyAll();
      if (this.downloader.getState() == Thread.State.NEW)
        this.downloader.start(); 
      return;
    } 
  }
  
  public String syncDownloadImage(String paramString) {
    String[] arrayOfString = paramString.split("/");
    ImageItem imageItem = new ImageItem(paramString, FileManager.createFileName(arrayOfString[arrayOfString.length - 1]));
    this.downloader.download(imageItem.url, imageItem.saveFileName);
    return imageItem.saveFileName;
  }
  
  class Downloader extends Thread {
    private void download(String param1String1, String param1String2) {
      // Byte code:
      //   0: aload_0
      //   1: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   4: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   7: new java/lang/StringBuilder
      //   10: dup
      //   11: ldc 'start download() srcPath : '
      //   13: invokespecial <init> : (Ljava/lang/String;)V
      //   16: aload_1
      //   17: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   20: ldc ' destPath : '
      //   22: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   25: aload_2
      //   26: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   29: invokevirtual toString : ()Ljava/lang/String;
      //   32: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   35: sipush #4096
      //   38: newarray byte
      //   40: astore #23
      //   42: aconst_null
      //   43: astore #21
      //   45: aconst_null
      //   46: astore #22
      //   48: aconst_null
      //   49: astore #7
      //   51: aconst_null
      //   52: astore #8
      //   54: aconst_null
      //   55: astore #16
      //   57: aconst_null
      //   58: astore #20
      //   60: aconst_null
      //   61: astore #9
      //   63: aconst_null
      //   64: astore #10
      //   66: aconst_null
      //   67: astore #6
      //   69: aconst_null
      //   70: astore #17
      //   72: aconst_null
      //   73: astore #18
      //   75: aconst_null
      //   76: astore #19
      //   78: aconst_null
      //   79: astore #15
      //   81: aconst_null
      //   82: astore #12
      //   84: aconst_null
      //   85: astore #13
      //   87: aconst_null
      //   88: astore #4
      //   90: aconst_null
      //   91: astore #14
      //   93: aconst_null
      //   94: astore #11
      //   96: new java/io/FileOutputStream
      //   99: dup
      //   100: aload_2
      //   101: invokespecial <init> : (Ljava/lang/String;)V
      //   104: astore #5
      //   106: aload #16
      //   108: astore #9
      //   110: aload #17
      //   112: astore #10
      //   114: aload #18
      //   116: astore #7
      //   118: aload #19
      //   120: astore #8
      //   122: new java/net/URL
      //   125: dup
      //   126: aload_1
      //   127: invokespecial <init> : (Ljava/lang/String;)V
      //   130: invokevirtual openStream : ()Ljava/io/InputStream;
      //   133: astore #4
      //   135: aload #4
      //   137: astore #9
      //   139: aload #4
      //   141: astore #10
      //   143: aload #4
      //   145: astore #7
      //   147: aload #4
      //   149: astore #8
      //   151: new java/io/BufferedInputStream
      //   154: dup
      //   155: aload #4
      //   157: sipush #8192
      //   160: invokespecial <init> : (Ljava/io/InputStream;I)V
      //   163: astore #6
      //   165: aload #6
      //   167: aload #23
      //   169: invokevirtual read : ([B)I
      //   172: istore_3
      //   173: iload_3
      //   174: ifge -> 566
      //   177: aload #5
      //   179: invokevirtual flush : ()V
      //   182: new android/media/ExifInterface
      //   185: dup
      //   186: aload_2
      //   187: invokespecial <init> : (Ljava/lang/String;)V
      //   190: astore #7
      //   192: aload_0
      //   193: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   196: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   199: new java/lang/StringBuilder
      //   202: dup
      //   203: ldc 'System date : '
      //   205: invokespecial <init> : (Ljava/lang/String;)V
      //   208: invokestatic getInstance : ()Ljava/util/Calendar;
      //   211: iconst_1
      //   212: invokevirtual get : (I)I
      //   215: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   218: ldc '-'
      //   220: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   223: invokestatic getInstance : ()Ljava/util/Calendar;
      //   226: iconst_2
      //   227: invokevirtual get : (I)I
      //   230: iconst_1
      //   231: iadd
      //   232: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   235: ldc '-'
      //   237: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   240: invokestatic getInstance : ()Ljava/util/Calendar;
      //   243: iconst_5
      //   244: invokevirtual get : (I)I
      //   247: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   250: ldc ' exif date : '
      //   252: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   255: aload #7
      //   257: ldc 'DateTime'
      //   259: invokevirtual getAttribute : (Ljava/lang/String;)Ljava/lang/String;
      //   262: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   265: invokevirtual toString : ()Ljava/lang/String;
      //   268: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   271: aload #6
      //   273: ifnull -> 1071
      //   276: aload #6
      //   278: invokevirtual close : ()V
      //   281: aload #4
      //   283: ifnull -> 291
      //   286: aload #4
      //   288: invokevirtual close : ()V
      //   291: aload #5
      //   293: ifnull -> 1068
      //   296: aload #5
      //   298: invokevirtual close : ()V
      //   301: aload_0
      //   302: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   305: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   308: new java/lang/StringBuilder
      //   311: dup
      //   312: ldc 'end download() srcPath : '
      //   314: invokespecial <init> : (Ljava/lang/String;)V
      //   317: aload_1
      //   318: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   321: ldc ' destPath : '
      //   323: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   326: aload_2
      //   327: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   330: invokevirtual toString : ()Ljava/lang/String;
      //   333: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   336: return
      //   337: astore #6
      //   339: aload #16
      //   341: astore #9
      //   343: aload #17
      //   345: astore #10
      //   347: aload #18
      //   349: astore #7
      //   351: aload #19
      //   353: astore #8
      //   355: aload_0
      //   356: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   359: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   362: ldc 'Occured Exception when openStream, try again URL openStream'
      //   364: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   367: aload #16
      //   369: astore #9
      //   371: aload #17
      //   373: astore #10
      //   375: aload #18
      //   377: astore #7
      //   379: aload #19
      //   381: astore #8
      //   383: aload #6
      //   385: invokevirtual printStackTrace : ()V
      //   388: aload #16
      //   390: astore #9
      //   392: aload #17
      //   394: astore #10
      //   396: aload #18
      //   398: astore #7
      //   400: aload #19
      //   402: astore #8
      //   404: new java/net/URL
      //   407: dup
      //   408: aload_1
      //   409: invokespecial <init> : (Ljava/lang/String;)V
      //   412: invokevirtual openStream : ()Ljava/io/InputStream;
      //   415: astore #4
      //   417: goto -> 135
      //   420: astore #4
      //   422: aload #16
      //   424: astore #9
      //   426: aload #17
      //   428: astore #10
      //   430: aload #18
      //   432: astore #7
      //   434: aload #19
      //   436: astore #8
      //   438: aload_0
      //   439: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   442: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   445: ldc 'Occured Exception again when openStream'
      //   447: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   450: aload #16
      //   452: astore #9
      //   454: aload #17
      //   456: astore #10
      //   458: aload #18
      //   460: astore #7
      //   462: aload #19
      //   464: astore #8
      //   466: aload #6
      //   468: invokevirtual printStackTrace : ()V
      //   471: aload #15
      //   473: astore #4
      //   475: goto -> 135
      //   478: astore #10
      //   480: aload #5
      //   482: astore #8
      //   484: aload #9
      //   486: astore #5
      //   488: aload #11
      //   490: astore #9
      //   492: aload #9
      //   494: astore #4
      //   496: aload #5
      //   498: astore #6
      //   500: aload #8
      //   502: astore #7
      //   504: aload_0
      //   505: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   508: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   511: ldc 'Occured FileNotFoundException when download'
      //   513: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   516: aload #9
      //   518: astore #4
      //   520: aload #5
      //   522: astore #6
      //   524: aload #8
      //   526: astore #7
      //   528: aload #10
      //   530: invokevirtual printStackTrace : ()V
      //   533: aload #9
      //   535: ifnull -> 543
      //   538: aload #9
      //   540: invokevirtual close : ()V
      //   543: aload #5
      //   545: ifnull -> 553
      //   548: aload #5
      //   550: invokevirtual close : ()V
      //   553: aload #8
      //   555: ifnull -> 301
      //   558: aload #8
      //   560: invokevirtual close : ()V
      //   563: goto -> 301
      //   566: aload #5
      //   568: aload #23
      //   570: iconst_0
      //   571: iload_3
      //   572: invokevirtual write : ([BII)V
      //   575: goto -> 165
      //   578: astore #10
      //   580: aload #5
      //   582: astore #8
      //   584: aload #6
      //   586: astore #9
      //   588: aload #4
      //   590: astore #5
      //   592: goto -> 492
      //   595: astore #4
      //   597: aload #4
      //   599: invokevirtual printStackTrace : ()V
      //   602: goto -> 543
      //   605: astore #4
      //   607: aload #4
      //   609: invokevirtual printStackTrace : ()V
      //   612: goto -> 553
      //   615: astore #4
      //   617: aload #4
      //   619: invokevirtual printStackTrace : ()V
      //   622: goto -> 563
      //   625: astore #10
      //   627: aload #21
      //   629: astore #8
      //   631: aload #9
      //   633: astore #5
      //   635: aload #12
      //   637: astore #9
      //   639: aload #9
      //   641: astore #4
      //   643: aload #5
      //   645: astore #6
      //   647: aload #8
      //   649: astore #7
      //   651: aload_0
      //   652: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   655: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   658: ldc 'Occured MalformedURLException when download'
      //   660: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   663: aload #9
      //   665: astore #4
      //   667: aload #5
      //   669: astore #6
      //   671: aload #8
      //   673: astore #7
      //   675: aload #10
      //   677: invokevirtual printStackTrace : ()V
      //   680: aload #9
      //   682: ifnull -> 690
      //   685: aload #9
      //   687: invokevirtual close : ()V
      //   690: aload #5
      //   692: ifnull -> 700
      //   695: aload #5
      //   697: invokevirtual close : ()V
      //   700: aload #8
      //   702: ifnull -> 301
      //   705: aload #8
      //   707: invokevirtual close : ()V
      //   710: goto -> 301
      //   713: astore #4
      //   715: aload #4
      //   717: invokevirtual printStackTrace : ()V
      //   720: goto -> 690
      //   723: astore #4
      //   725: aload #4
      //   727: invokevirtual printStackTrace : ()V
      //   730: goto -> 700
      //   733: astore #4
      //   735: aload #4
      //   737: invokevirtual printStackTrace : ()V
      //   740: goto -> 710
      //   743: astore #4
      //   745: aload #22
      //   747: astore #8
      //   749: aload #10
      //   751: astore #5
      //   753: aload #4
      //   755: astore #10
      //   757: aload #13
      //   759: astore #9
      //   761: aload #9
      //   763: astore #4
      //   765: aload #5
      //   767: astore #6
      //   769: aload #8
      //   771: astore #7
      //   773: aload_0
      //   774: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   777: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   780: ldc 'Occured IOException when download'
      //   782: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   785: aload #9
      //   787: astore #4
      //   789: aload #5
      //   791: astore #6
      //   793: aload #8
      //   795: astore #7
      //   797: aload #10
      //   799: invokevirtual printStackTrace : ()V
      //   802: aload #9
      //   804: ifnull -> 812
      //   807: aload #9
      //   809: invokevirtual close : ()V
      //   812: aload #5
      //   814: ifnull -> 822
      //   817: aload #5
      //   819: invokevirtual close : ()V
      //   822: aload #8
      //   824: ifnull -> 301
      //   827: aload #8
      //   829: invokevirtual close : ()V
      //   832: goto -> 301
      //   835: astore #4
      //   837: aload #4
      //   839: invokevirtual printStackTrace : ()V
      //   842: goto -> 812
      //   845: astore #4
      //   847: aload #4
      //   849: invokevirtual printStackTrace : ()V
      //   852: goto -> 822
      //   855: astore #4
      //   857: aload #4
      //   859: invokevirtual printStackTrace : ()V
      //   862: goto -> 832
      //   865: astore_2
      //   866: aload #4
      //   868: astore_1
      //   869: aload_1
      //   870: ifnull -> 877
      //   873: aload_1
      //   874: invokevirtual close : ()V
      //   877: aload #6
      //   879: ifnull -> 887
      //   882: aload #6
      //   884: invokevirtual close : ()V
      //   887: aload #7
      //   889: ifnull -> 897
      //   892: aload #7
      //   894: invokevirtual close : ()V
      //   897: aload_2
      //   898: athrow
      //   899: astore_1
      //   900: aload_1
      //   901: invokevirtual printStackTrace : ()V
      //   904: goto -> 877
      //   907: astore_1
      //   908: aload_1
      //   909: invokevirtual printStackTrace : ()V
      //   912: goto -> 887
      //   915: astore_1
      //   916: aload_1
      //   917: invokevirtual printStackTrace : ()V
      //   920: goto -> 897
      //   923: astore #6
      //   925: aload #6
      //   927: invokevirtual printStackTrace : ()V
      //   930: goto -> 281
      //   933: astore #4
      //   935: aload #4
      //   937: invokevirtual printStackTrace : ()V
      //   940: goto -> 291
      //   943: astore #4
      //   945: aload #4
      //   947: invokevirtual printStackTrace : ()V
      //   950: goto -> 301
      //   953: astore_2
      //   954: aload #14
      //   956: astore_1
      //   957: aload #10
      //   959: astore #6
      //   961: aload #5
      //   963: astore #7
      //   965: goto -> 869
      //   968: astore_2
      //   969: aload #6
      //   971: astore_1
      //   972: aload #4
      //   974: astore #6
      //   976: aload #5
      //   978: astore #7
      //   980: goto -> 869
      //   983: astore #10
      //   985: aload #5
      //   987: astore #8
      //   989: aload #13
      //   991: astore #9
      //   993: aload #7
      //   995: astore #5
      //   997: goto -> 761
      //   1000: astore #10
      //   1002: aload #5
      //   1004: astore #8
      //   1006: aload #6
      //   1008: astore #9
      //   1010: aload #4
      //   1012: astore #5
      //   1014: goto -> 761
      //   1017: astore #10
      //   1019: aload #5
      //   1021: astore #4
      //   1023: aload #12
      //   1025: astore #9
      //   1027: aload #8
      //   1029: astore #5
      //   1031: aload #4
      //   1033: astore #8
      //   1035: goto -> 639
      //   1038: astore #10
      //   1040: aload #5
      //   1042: astore #8
      //   1044: aload #6
      //   1046: astore #9
      //   1048: aload #4
      //   1050: astore #5
      //   1052: goto -> 639
      //   1055: astore #10
      //   1057: aload #11
      //   1059: astore #9
      //   1061: aload #20
      //   1063: astore #5
      //   1065: goto -> 492
      //   1068: goto -> 301
      //   1071: goto -> 281
      // Exception table:
      //   from	to	target	type
      //   96	106	1055	java/io/FileNotFoundException
      //   96	106	625	java/net/MalformedURLException
      //   96	106	743	java/io/IOException
      //   96	106	865	finally
      //   122	135	337	java/lang/Exception
      //   122	135	478	java/io/FileNotFoundException
      //   122	135	1017	java/net/MalformedURLException
      //   122	135	983	java/io/IOException
      //   122	135	953	finally
      //   151	165	478	java/io/FileNotFoundException
      //   151	165	1017	java/net/MalformedURLException
      //   151	165	983	java/io/IOException
      //   151	165	953	finally
      //   165	173	578	java/io/FileNotFoundException
      //   165	173	1038	java/net/MalformedURLException
      //   165	173	1000	java/io/IOException
      //   165	173	968	finally
      //   177	271	578	java/io/FileNotFoundException
      //   177	271	1038	java/net/MalformedURLException
      //   177	271	1000	java/io/IOException
      //   177	271	968	finally
      //   276	281	923	java/io/IOException
      //   286	291	933	java/io/IOException
      //   296	301	943	java/io/IOException
      //   355	367	478	java/io/FileNotFoundException
      //   355	367	1017	java/net/MalformedURLException
      //   355	367	983	java/io/IOException
      //   355	367	953	finally
      //   383	388	478	java/io/FileNotFoundException
      //   383	388	1017	java/net/MalformedURLException
      //   383	388	983	java/io/IOException
      //   383	388	953	finally
      //   404	417	420	java/lang/Exception
      //   404	417	478	java/io/FileNotFoundException
      //   404	417	1017	java/net/MalformedURLException
      //   404	417	983	java/io/IOException
      //   404	417	953	finally
      //   438	450	478	java/io/FileNotFoundException
      //   438	450	1017	java/net/MalformedURLException
      //   438	450	983	java/io/IOException
      //   438	450	953	finally
      //   466	471	478	java/io/FileNotFoundException
      //   466	471	1017	java/net/MalformedURLException
      //   466	471	983	java/io/IOException
      //   466	471	953	finally
      //   504	516	865	finally
      //   528	533	865	finally
      //   538	543	595	java/io/IOException
      //   548	553	605	java/io/IOException
      //   558	563	615	java/io/IOException
      //   566	575	578	java/io/FileNotFoundException
      //   566	575	1038	java/net/MalformedURLException
      //   566	575	1000	java/io/IOException
      //   566	575	968	finally
      //   651	663	865	finally
      //   675	680	865	finally
      //   685	690	713	java/io/IOException
      //   695	700	723	java/io/IOException
      //   705	710	733	java/io/IOException
      //   773	785	865	finally
      //   797	802	865	finally
      //   807	812	835	java/io/IOException
      //   817	822	845	java/io/IOException
      //   827	832	855	java/io/IOException
      //   873	877	899	java/io/IOException
      //   882	887	907	java/io/IOException
      //   892	897	915	java/io/IOException
    }
    
    public void run() {
      // Byte code:
      //   0: aload_0
      //   1: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   4: getfield imageDownloadQueue : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader$ImageDownloadQueue;
      //   7: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader$ImageDownloadQueue;)Ljava/util/Stack;
      //   10: invokevirtual size : ()I
      //   13: ifne -> 44
      //   16: aload_0
      //   17: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   20: getfield imageDownloadQueue : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader$ImageDownloadQueue;
      //   23: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader$ImageDownloadQueue;)Ljava/util/Stack;
      //   26: astore_1
      //   27: aload_1
      //   28: monitorenter
      //   29: aload_0
      //   30: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   33: getfield imageDownloadQueue : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader$ImageDownloadQueue;
      //   36: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader$ImageDownloadQueue;)Ljava/util/Stack;
      //   39: invokevirtual wait : ()V
      //   42: aload_1
      //   43: monitorexit
      //   44: aload_0
      //   45: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   48: getfield imageDownloadQueue : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader$ImageDownloadQueue;
      //   51: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader$ImageDownloadQueue;)Ljava/util/Stack;
      //   54: invokevirtual size : ()I
      //   57: ifeq -> 0
      //   60: aload_0
      //   61: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   64: getfield imageDownloadQueue : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader$ImageDownloadQueue;
      //   67: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader$ImageDownloadQueue;)Ljava/util/Stack;
      //   70: astore_1
      //   71: aload_1
      //   72: monitorenter
      //   73: aload_0
      //   74: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   77: getfield imageDownloadQueue : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader$ImageDownloadQueue;
      //   80: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader$ImageDownloadQueue;)Ljava/util/Stack;
      //   83: iconst_0
      //   84: invokevirtual remove : (I)Ljava/lang/Object;
      //   87: checkcast com/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader$ImageItem
      //   90: astore_2
      //   91: aload_1
      //   92: monitorexit
      //   93: aload_0
      //   94: aload_2
      //   95: getfield url : Ljava/lang/String;
      //   98: aload_2
      //   99: getfield saveFileName : Ljava/lang/String;
      //   102: invokespecial download : (Ljava/lang/String;Ljava/lang/String;)V
      //   105: new android/os/Message
      //   108: dup
      //   109: invokespecial <init> : ()V
      //   112: astore_1
      //   113: aload_1
      //   114: aload_0
      //   115: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   118: getfield messageID : I
      //   121: putfield what : I
      //   124: aload_1
      //   125: aload_2
      //   126: getfield saveFileName : Ljava/lang/String;
      //   129: putfield obj : Ljava/lang/Object;
      //   132: aload_0
      //   133: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   136: getfield handler : Landroid/os/Handler;
      //   139: aload_1
      //   140: invokevirtual sendMessage : (Landroid/os/Message;)Z
      //   143: pop
      //   144: goto -> 0
      //   147: astore_1
      //   148: aload_1
      //   149: invokevirtual printStackTrace : ()V
      //   152: return
      //   153: astore_2
      //   154: aload_1
      //   155: monitorexit
      //   156: aload_2
      //   157: athrow
      //   158: astore_2
      //   159: aload_1
      //   160: monitorexit
      //   161: aload_2
      //   162: athrow
      // Exception table:
      //   from	to	target	type
      //   0	29	147	java/lang/InterruptedException
      //   29	44	153	finally
      //   44	73	147	java/lang/InterruptedException
      //   73	93	158	finally
      //   93	144	147	java/lang/InterruptedException
      //   154	156	153	finally
      //   156	158	147	java/lang/InterruptedException
      //   159	161	158	finally
      //   161	163	147	java/lang/InterruptedException
    }
  }
  
  class ImageDownloadQueue {
    private Stack<ImageDownloader.ImageItem> imageItems = new Stack<ImageDownloader.ImageItem>();
  }
  
  private class ImageItem {
    public String saveFileName;
    
    public String url;
    
    public ImageItem(String param1String1, String param1String2) {
      this.url = param1String1;
      this.saveFileName = param1String2;
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservic\\util\ImageDownloader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */