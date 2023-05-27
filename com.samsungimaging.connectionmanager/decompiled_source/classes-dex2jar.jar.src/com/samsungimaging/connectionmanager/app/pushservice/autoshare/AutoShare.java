package com.samsungimaging.connectionmanager.app.pushservice.autoshare;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.samsungimaging.connectionmanager.app.cm.Main;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;
import com.samsungimaging.connectionmanager.app.pushservice.common.CommonPushService;
import com.samsungimaging.connectionmanager.app.pushservice.common.CommonUtils;
import com.samsungimaging.connectionmanager.dialog.ConnectionWaitDialog;
import com.samsungimaging.connectionmanager.dialog.CustomProgressDialog;
import com.samsungimaging.connectionmanager.dialog.RefusalDialog;
import com.samsungimaging.connectionmanager.gallery.GalleryFragment;
import com.samsungimaging.connectionmanager.util.Trace;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class AutoShare extends CommonPushService {
  private static final int NOTI_RECEIVE_DONE = 1;
  
  private static final int NOTI_RECEIVE_PROGRESS = 2;
  
  private static final int SERVER_PORT = 1801;
  
  private static final Trace.Tag TAG = Trace.Tag.AS;
  
  private static boolean configChanged = false;
  
  private static int mResolutionSize;
  
  private static WifiManager mWifimanager;
  
  private static int save_cnt;
  
  private static int updateInterval = 0;
  
  private String Latitude = null;
  
  private String Longitude = null;
  
  private boolean bAcceptRun = false;
  
  private boolean bRcvThreadRun = false;
  
  private RemoteViews contentView;
  
  private RemoteViews contentView_prog;
  
  private Location currentLocation_gps = null;
  
  private Location currentLocation_network = null;
  
  private boolean dscConnected = false;
  
  private Queue<String> fileQueue = null;
  
  private int g_progress_percent = 0;
  
  private int image_file_idx = 0;
  
  private boolean isBackGround = false;
  
  private boolean isFileSaveError = false;
  
  private boolean isHeaderError = false;
  
  private boolean isHomeKeyPressed = false;
  
  LocationListener locationListener_gps = new LocationListener() {
      public void onLocationChanged(Location param1Location) {
        AutoShare.this.currentLocation_gps = param1Location;
        Trace.d(AutoShare.TAG, "locationListener_gps onLocationChanged currentLocation" + param1Location.toString());
      }
      
      public void onProviderDisabled(String param1String) {
        AutoShare.this.currentLocation_gps = null;
        Trace.d(AutoShare.TAG, "locationListener_gps onProviderDisabled provider : " + param1String);
      }
      
      public void onProviderEnabled(String param1String) {
        if (AutoShare.this.locationManager != null)
          AutoShare.this.currentLocation_gps = AutoShare.this.locationManager.getLastKnownLocation(param1String); 
        Trace.d(AutoShare.TAG, "locationListener_gps onProviderEnabled provider : " + param1String);
      }
      
      public void onStatusChanged(String param1String, int param1Int, Bundle param1Bundle) {
        Trace.d(AutoShare.TAG, "locationListener_gps onStatusChanged provider : " + param1String + ", status : " + param1Int);
      }
    };
  
  LocationListener locationListener_network = new LocationListener() {
      public void onLocationChanged(Location param1Location) {
        AutoShare.this.currentLocation_network = param1Location;
        Trace.d(AutoShare.TAG, "locationListener_network_gps onLocationChanged currentLocation" + param1Location.toString());
      }
      
      public void onProviderDisabled(String param1String) {
        AutoShare.this.currentLocation_network = null;
        Trace.d(AutoShare.TAG, "locationListener_network_gps onProviderDisabled provider : " + param1String);
      }
      
      public void onProviderEnabled(String param1String) {
        if (AutoShare.this.locationManager != null)
          AutoShare.this.currentLocation_network = AutoShare.this.locationManager.getLastKnownLocation(param1String); 
        Trace.d(AutoShare.TAG, "locationListener_network_gps onProviderEnabled provider : " + param1String);
      }
      
      public void onStatusChanged(String param1String, int param1Int, Bundle param1Bundle) {
        Trace.d(AutoShare.TAG, "locationListener_network_gps onStatusChanged provider : " + param1String + ", status : " + param1Int);
      }
    };
  
  private LocationManager locationManager = null;
  
  private String mAuthorization = null;
  
  private String mContentLength = null;
  
  private Context mContext = null;
  
  private String mCreateFilePath = null;
  
  private ConnectionWaitDialog mDialogCMWaitConnect = null;
  
  private String mGpsInfo = null;
  
  Handler mHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        Notification notification;
        String str;
        CustomProgressDialog customProgressDialog1;
        int i;
        CustomProgressDialog customProgressDialog2;
        switch (param1Message.what) {
          default:
            return;
          case 22:
            Trace.d(AutoShare.TAG, "Const.MsgId.APP_CLOSE_FOR_REMOVED_SD");
            AutoShare.this.showDialog(1003);
            return;
          case 3:
            Trace.d(AutoShare.TAG, "Const.MsgId.AP_CONNECTED");
            AutoShare.this.registerDisconnectReceiver();
            AutoShare.this.mWifiInfo = AutoShare.mWifimanager.getConnectionInfo();
            i = AutoShare.this.mWifiInfo.getIpAddress();
            Trace.d(AutoShare.TAG, "ip:" + i + " -> " + CommonUtils.intToIp(i));
            if (AutoShare.mWifimanager.isWifiEnabled() && !AutoShare.this.dscConnected) {
              Trace.d(AutoShare.TAG, "wifi was already connected");
              AutoShare.this.sscTask = new AutoShare.InitTask(null);
              AutoShare.this.sscTask.execute((Object[])new WifiInfo[] { AutoShare.access$4(this.this$0) });
              Trace.d(AutoShare.TAG, "InitTask called  1");
              return;
            } 
          case 53:
            Trace.d(AutoShare.TAG, "Const.MsgId.IMAGE_RCV_WAIT");
            return;
          case 51:
            Trace.d(AutoShare.TAG, "Const.MsgId.IMAGE_RCV_DONE");
            if (!AutoShare.this.fileQueue.isEmpty()) {
              AutoShare.this.mReceivedFilePath = AutoShare.this.fileQueue.poll();
              if (AutoShare.this.currentLocation_gps != null || AutoShare.this.currentLocation_network != null)
                try {
                  ExifInterface exifInterface = new ExifInterface(AutoShare.this.mReceivedFilePath);
                  if (exifInterface.getAttribute("GPSLatitude") == null) {
                    Location location;
                    if (AutoShare.this.currentLocation_gps != null) {
                      location = AutoShare.this.currentLocation_gps;
                    } else {
                      location = AutoShare.this.currentLocation_network;
                    } 
                    String str1 = AutoShare.this.setGpsValue(location);
                    exifInterface.setAttribute("GPSLatitudeRef", AutoShare.this.Latitude);
                    exifInterface.setAttribute("GPSLongitudeRef", AutoShare.this.Longitude);
                    exifInterface.setAttribute("GPSLatitude", AutoShare.this.deg_to_dms(location.getLatitude()));
                    exifInterface.setAttribute("GPSLongitude", AutoShare.this.deg_to_dms(location.getLongitude()));
                    Trace.d(AutoShare.TAG, "Location mGpsInfo : " + AutoShare.this.mGpsInfo + ", gpsValue : " + str1);
                    exifInterface.saveAttributes();
                  } 
                } catch (IOException iOException) {
                  iOException.printStackTrace();
                }  
              Trace.d(AutoShare.TAG, "IMAGE_RCV_DONE [" + AutoShare.save_cnt + "] change. path:" + AutoShare.this.mReceivedFilePath);
            } 
            AutoShare.save_cnt = AutoShare.save_cnt + 1;
            if (AutoShare.this.isBackGround) {
              notification = AutoShare.this.noti;
              notification.defaults |= 0x1;
              String.format(AutoShare.this.getResources().getString(2131361825), new Object[] { Integer.valueOf(AutoShare.access$18()) });
              AutoShare.this.nm.notify(1, AutoShare.this.noti);
              AutoShare.this.nm.cancel(2);
              return;
            } 
          case 52:
            AutoShare.this.bAcceptRun = false;
            for (i = 0;; i++) {
              if (i >= 5) {
                AutoShare.this.showDialog(1006);
                Trace.d(AutoShare.TAG, "wifi disconnected!!!!!, send message app close after 1000ms");
                AutoShare.this.mHandler.sendEmptyMessageDelayed(24, 1000L);
                return;
              } 
              AutoShare.this.rcv_on[i] = false;
            } 
          case 24:
            str = (String)((Message)notification).obj;
            Trace.d(AutoShare.TAG, "APP_CLOSED with  : " + str);
            AutoShare.this.finishSafe(str);
            return;
          case 63:
            customProgressDialog2 = (CustomProgressDialog)AutoShare.this.mDialogList.get(1000);
            if (customProgressDialog2 == null || !customProgressDialog2.isShowing()) {
              Trace.d(AutoShare.TAG, "Show mProgressDialog");
              AutoShare.this.showDialog(1000);
              ((CustomProgressDialog)AutoShare.this.mDialogList.get(1000)).setMax(((Message)str).arg1);
            } 
            AutoShare.this.fileQueue.offer(AutoShare.this.mCreateFilePath);
            Trace.d(AutoShare.TAG, "SSC Thumbnail [" + AutoShare.save_cnt + "] add. path:" + AutoShare.this.mCreateFilePath);
            return;
          case 62:
            customProgressDialog2 = (CustomProgressDialog)AutoShare.this.mDialogList.get(1000);
            if (customProgressDialog2 != null && customProgressDialog2.isShowing())
              customProgressDialog2.setProgress(((Message)str).arg2); 
            if (AutoShare.this.bDownloading()) {
              AutoShare.this.contentView_prog.setProgressBar(2131558665, 100, AutoShare.this.g_progress_percent, false);
              AutoShare.this.nm.notify(2, AutoShare.this.noti_prog);
              AutoShare.this.pre_progress_percent = AutoShare.this.g_progress_percent;
              return;
            } 
          case 64:
            AutoShare.this.mGallery.setState(GalleryFragment.State.NORMAL, new Object[0]);
            customProgressDialog2 = (CustomProgressDialog)AutoShare.this.mDialogList.get(1000);
            if (customProgressDialog2 != null) {
              customProgressDialog2.setProgress(((Message)str).arg1);
              customProgressDialog2.dismiss();
            } 
            if (AutoShare.this.bDownloading()) {
              AutoShare.this.contentView_prog.setProgressBar(2131558665, 100, 100, false);
              AutoShare.this.nm.notify(2, AutoShare.this.noti_prog);
              AutoShare.this.pre_progress_percent = AutoShare.this.g_progress_percent;
              return;
            } 
          case 36:
            Trace.d(AutoShare.TAG, "Const.MsgId.CANCEL_SAVING");
            customProgressDialog1 = (CustomProgressDialog)AutoShare.this.mDialogList.get(1000);
            if (customProgressDialog1 != null && customProgressDialog1.isShowing())
              customProgressDialog1.dismiss(); 
            Trace.d(AutoShare.TAG, "file Queue remove" + (String)AutoShare.this.fileQueue.poll());
            AutoShare.this.nm.cancel(1);
            AutoShare.this.nm.cancel(2);
            return;
          case 71:
            Trace.d(AutoShare.TAG, "Const.MsgId.BACK_BUTTON_INVALID");
            return;
          case 72:
            Toast.makeText(AutoShare.this.mContext, 2131361895, 0).show();
            Trace.d(AutoShare.TAG, "Const.MsgId.WIFI_CONNECTION_IS_BAD");
            AutoShare.this.mHandler.sendEmptyMessageDelayed(24, 3000L);
            return;
          case 73:
            bundle = new Bundle();
            bundle.putString("error", "CHECK_FOR_401_ERROR");
            AutoShare.this.showDialog(1007, bundle);
            return;
          case 74:
            break;
        } 
        Bundle bundle = new Bundle();
        bundle.putString("error", "CHECK_FOR_503_ERROR");
        AutoShare.this.showDialog(1007, bundle);
      }
    };
  
  private String mHostName = null;
  
  private int mPortNumber;
  
  private PowerManager mPowerManager = null;
  
  private RcvThread mRcvThread = null;
  
  private String mReceivedFilePath = null;
  
  private String mRequest = null;
  
  private PowerManager.WakeLock mWakeLock = null;
  
  private WifiInfo mWifiInfo;
  
  private WifiStateChangedReceiverForAppClose mWifiStateChangedReceiverForAppClose = null;
  
  private NotificationManager nm;
  
  private Notification noti;
  
  private Notification noti_prog;
  
  private PendingIntent pending;
  
  private PendingIntent pending_prog;
  
  private int pre_progress_percent = 0;
  
  private boolean[] rcv_on = new boolean[5];
  
  private InetAddress serverAddr = null;
  
  int socket_idx = 0;
  
  private GpsTask sscGpsTask = null;
  
  private InitTask sscTask = null;
  
  private DeInitTask sscTaskEx = null;
  
  private ServerSocket svSock = null;
  
  int svSockReceiveBufferSize = 0;
  
  private boolean wifi_stat_conn = false;
  
  static {
    mWifimanager = null;
    save_cnt = 0;
  }
  
  private boolean SSC_DeInit(WifiInfo paramWifiInfo) throws IOException {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: iconst_0
    //   3: istore_2
    //   4: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   7: ldc 'SSC_DeInit()'
    //   9: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   12: aload_1
    //   13: astore #4
    //   15: aload_1
    //   16: ifnonnull -> 27
    //   19: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.mWifimanager : Landroid/net/wifi/WifiManager;
    //   22: invokevirtual getConnectionInfo : ()Landroid/net/wifi/WifiInfo;
    //   25: astore #4
    //   27: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.mWifimanager : Landroid/net/wifi/WifiManager;
    //   30: invokevirtual isWifiEnabled : ()Z
    //   33: ifeq -> 385
    //   36: aload #4
    //   38: invokevirtual getIpAddress : ()I
    //   41: invokestatic intToIp : (I)Ljava/lang/String;
    //   44: astore_1
    //   45: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   48: new java/lang/StringBuilder
    //   51: dup
    //   52: ldc_w '-----------------------------localIp : '
    //   55: invokespecial <init> : (Ljava/lang/String;)V
    //   58: aload_1
    //   59: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   62: invokevirtual toString : ()Ljava/lang/String;
    //   65: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   68: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.mWifimanager : Landroid/net/wifi/WifiManager;
    //   71: invokevirtual getDhcpInfo : ()Landroid/net/DhcpInfo;
    //   74: getfield serverAddress : I
    //   77: invokestatic intToIp : (I)Ljava/lang/String;
    //   80: astore #7
    //   82: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   85: new java/lang/StringBuilder
    //   88: dup
    //   89: ldc_w '-----------------------------CameraIp : '
    //   92: invokespecial <init> : (Ljava/lang/String;)V
    //   95: aload #7
    //   97: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   100: invokevirtual toString : ()Ljava/lang/String;
    //   103: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   106: new java/lang/StringBuffer
    //   109: dup
    //   110: invokespecial <init> : ()V
    //   113: astore #5
    //   115: aload #5
    //   117: ldc_w 'S2L/1.0 ByeBye'
    //   120: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   123: ldc_w '\\r\\n'
    //   126: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   129: ldc_w 'Host: SAMSUNG-S2L'
    //   132: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   135: ldc_w '\\r\\n'
    //   138: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   141: ldc_w 'Content-Type: text/xml;charset=utf-8'
    //   144: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   147: ldc_w '\\r\\n'
    //   150: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   153: ldc_w 'User-Agent: APP-TYPE'
    //   156: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   159: ldc_w '\\r\\n'
    //   162: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   165: ldc_w 'Content-Length: 0'
    //   168: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   171: ldc_w '\\r\\n'
    //   174: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   177: ldc_w 'Connection: Close'
    //   180: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   183: ldc_w '\\r\\n\\r\\n'
    //   186: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   189: pop
    //   190: aconst_null
    //   191: astore #4
    //   193: aconst_null
    //   194: astore #6
    //   196: aload #4
    //   198: astore_1
    //   199: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   202: new java/lang/StringBuilder
    //   205: dup
    //   206: ldc_w 'CameraIp:'
    //   209: invokespecial <init> : (Ljava/lang/String;)V
    //   212: aload #7
    //   214: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   217: ldc_w ', CameraPort: '
    //   220: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   223: aload_0
    //   224: getfield mPortNumber : I
    //   227: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   230: invokevirtual toString : ()Ljava/lang/String;
    //   233: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   236: aload #4
    //   238: astore_1
    //   239: new java/net/Socket
    //   242: dup
    //   243: invokespecial <init> : ()V
    //   246: astore #8
    //   248: aload #4
    //   250: astore_1
    //   251: aload #8
    //   253: new java/net/InetSocketAddress
    //   256: dup
    //   257: aload #7
    //   259: aload_0
    //   260: getfield mPortNumber : I
    //   263: invokespecial <init> : (Ljava/lang/String;I)V
    //   266: sipush #3000
    //   269: invokevirtual connect : (Ljava/net/SocketAddress;I)V
    //   272: aload #4
    //   274: astore_1
    //   275: new java/io/BufferedOutputStream
    //   278: dup
    //   279: aload #8
    //   281: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   284: invokespecial <init> : (Ljava/io/OutputStream;)V
    //   287: astore #4
    //   289: iload_3
    //   290: istore_2
    //   291: aload #4
    //   293: aload #5
    //   295: invokevirtual toString : ()Ljava/lang/String;
    //   298: invokevirtual getBytes : ()[B
    //   301: invokevirtual write : ([B)V
    //   304: iload_3
    //   305: istore_2
    //   306: aload #4
    //   308: invokevirtual flush : ()V
    //   311: iload_3
    //   312: istore_2
    //   313: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   316: aload #5
    //   318: invokevirtual toString : ()Ljava/lang/String;
    //   321: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   324: aload #4
    //   326: ifnull -> 336
    //   329: iload_3
    //   330: istore_2
    //   331: aload #4
    //   333: invokevirtual close : ()V
    //   336: iload_3
    //   337: istore_2
    //   338: aload_0
    //   339: iconst_0
    //   340: putfield dscConnected : Z
    //   343: iconst_1
    //   344: istore_2
    //   345: iconst_1
    //   346: istore_3
    //   347: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   350: new java/lang/StringBuilder
    //   353: dup
    //   354: ldc_w 'SSC_DeInit() ended dscConnected : '
    //   357: invokespecial <init> : (Ljava/lang/String;)V
    //   360: aload_0
    //   361: getfield dscConnected : Z
    //   364: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   367: invokevirtual toString : ()Ljava/lang/String;
    //   370: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   373: aload #4
    //   375: ifnull -> 383
    //   378: aload #4
    //   380: invokevirtual close : ()V
    //   383: iload_3
    //   384: ireturn
    //   385: iconst_0
    //   386: ireturn
    //   387: astore #5
    //   389: aload #6
    //   391: astore #4
    //   393: aload #4
    //   395: astore_1
    //   396: aload #5
    //   398: invokevirtual printStackTrace : ()V
    //   401: iload_2
    //   402: istore_3
    //   403: aload #4
    //   405: ifnull -> 383
    //   408: aload #4
    //   410: invokevirtual close : ()V
    //   413: iload_2
    //   414: istore_3
    //   415: goto -> 383
    //   418: astore #4
    //   420: aload_1
    //   421: ifnull -> 428
    //   424: aload_1
    //   425: invokevirtual close : ()V
    //   428: aload #4
    //   430: athrow
    //   431: astore #5
    //   433: aload #4
    //   435: astore_1
    //   436: aload #5
    //   438: astore #4
    //   440: goto -> 420
    //   443: astore #5
    //   445: goto -> 393
    // Exception table:
    //   from	to	target	type
    //   199	236	387	java/lang/Exception
    //   199	236	418	finally
    //   239	248	387	java/lang/Exception
    //   239	248	418	finally
    //   251	272	387	java/lang/Exception
    //   251	272	418	finally
    //   275	289	387	java/lang/Exception
    //   275	289	418	finally
    //   291	304	443	java/lang/Exception
    //   291	304	431	finally
    //   306	311	443	java/lang/Exception
    //   306	311	431	finally
    //   313	324	443	java/lang/Exception
    //   313	324	431	finally
    //   331	336	443	java/lang/Exception
    //   331	336	431	finally
    //   338	343	443	java/lang/Exception
    //   338	343	431	finally
    //   347	373	443	java/lang/Exception
    //   347	373	431	finally
    //   396	401	418	finally
  }
  
  private void SSC_GpsSend() throws IOException {
    // Byte code:
    //   0: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.mWifimanager : Landroid/net/wifi/WifiManager;
    //   3: invokevirtual getDhcpInfo : ()Landroid/net/DhcpInfo;
    //   6: getfield serverAddress : I
    //   9: invokestatic intToIp : (I)Ljava/lang/String;
    //   12: astore #11
    //   14: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   17: ldc_w 'SSC_GpsSend()'
    //   20: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   23: new java/lang/StringBuffer
    //   26: dup
    //   27: invokespecial <init> : ()V
    //   30: astore #9
    //   32: aload #9
    //   34: ldc_w 'S2L/1.0 Request'
    //   37: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   40: ldc_w '\\r\\n'
    //   43: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   46: ldc_w 'Host-Gps : '
    //   49: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   52: aload_0
    //   53: invokespecial getGpsInfo : ()Ljava/lang/String;
    //   56: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   59: ldc_w '\\r\\n\\r\\n'
    //   62: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   65: pop
    //   66: aconst_null
    //   67: astore_2
    //   68: aconst_null
    //   69: astore #7
    //   71: aconst_null
    //   72: astore #8
    //   74: aconst_null
    //   75: astore #6
    //   77: aconst_null
    //   78: astore #5
    //   80: aload #8
    //   82: astore_3
    //   83: aload_2
    //   84: astore #4
    //   86: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   89: new java/lang/StringBuilder
    //   92: dup
    //   93: ldc_w 'Camera Connection Try... : ['
    //   96: invokespecial <init> : (Ljava/lang/String;)V
    //   99: aload #11
    //   101: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   104: ldc_w ':'
    //   107: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   110: aload_0
    //   111: getfield mPortNumber : I
    //   114: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   117: ldc_w ']'
    //   120: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   123: invokevirtual toString : ()Ljava/lang/String;
    //   126: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   129: aload #8
    //   131: astore_3
    //   132: aload_2
    //   133: astore #4
    //   135: new java/net/Socket
    //   138: dup
    //   139: invokespecial <init> : ()V
    //   142: astore #10
    //   144: aload #8
    //   146: astore_3
    //   147: aload_2
    //   148: astore #4
    //   150: aload #10
    //   152: new java/net/InetSocketAddress
    //   155: dup
    //   156: aload #11
    //   158: aload_0
    //   159: getfield mPortNumber : I
    //   162: invokespecial <init> : (Ljava/lang/String;I)V
    //   165: sipush #2000
    //   168: invokevirtual connect : (Ljava/net/SocketAddress;I)V
    //   171: aload #8
    //   173: astore_3
    //   174: aload_2
    //   175: astore #4
    //   177: new java/io/BufferedOutputStream
    //   180: dup
    //   181: aload #10
    //   183: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   186: invokespecial <init> : (Ljava/io/OutputStream;)V
    //   189: astore_2
    //   190: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   193: aload #9
    //   195: invokevirtual toString : ()Ljava/lang/String;
    //   198: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   201: aload_2
    //   202: aload #9
    //   204: invokevirtual toString : ()Ljava/lang/String;
    //   207: invokevirtual getBytes : ()[B
    //   210: invokevirtual write : ([B)V
    //   213: aload_2
    //   214: invokevirtual flush : ()V
    //   217: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   220: ldc_w 'Performance Check Point : Discovery Request.'
    //   223: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   226: new java/io/BufferedInputStream
    //   229: dup
    //   230: aload #10
    //   232: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   235: invokespecial <init> : (Ljava/io/InputStream;)V
    //   238: astore #4
    //   240: ldc_w ''
    //   243: astore_3
    //   244: sipush #256
    //   247: newarray byte
    //   249: astore #6
    //   251: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   254: ldc_w 'Camera Response Waiting...'
    //   257: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   260: aload #4
    //   262: aload #6
    //   264: iconst_0
    //   265: aload #6
    //   267: arraylength
    //   268: invokevirtual read : ([BII)I
    //   271: istore_1
    //   272: iload_1
    //   273: iconst_m1
    //   274: if_icmpne -> 315
    //   277: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   280: ldc_w 'Performance Check Point : SSC_GpsSend Camera Response Received.'
    //   283: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   286: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   289: aload_3
    //   290: invokevirtual toString : ()Ljava/lang/String;
    //   293: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   296: aload_2
    //   297: ifnull -> 304
    //   300: aload_2
    //   301: invokevirtual close : ()V
    //   304: aload #4
    //   306: ifnull -> 314
    //   309: aload #4
    //   311: invokevirtual close : ()V
    //   314: return
    //   315: new java/lang/StringBuilder
    //   318: dup
    //   319: aload_3
    //   320: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   323: invokespecial <init> : (Ljava/lang/String;)V
    //   326: new java/lang/String
    //   329: dup
    //   330: aload #6
    //   332: iconst_0
    //   333: iload_1
    //   334: invokespecial <init> : ([BII)V
    //   337: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   340: invokevirtual toString : ()Ljava/lang/String;
    //   343: astore #5
    //   345: aload #5
    //   347: ldc_w '\\n'
    //   350: invokevirtual indexOf : (Ljava/lang/String;)I
    //   353: istore_1
    //   354: aload #5
    //   356: astore_3
    //   357: iload_1
    //   358: iconst_m1
    //   359: if_icmpeq -> 260
    //   362: aload #5
    //   364: astore_3
    //   365: goto -> 277
    //   368: astore #6
    //   370: aload #7
    //   372: astore_2
    //   373: aload #5
    //   375: astore_3
    //   376: aload_2
    //   377: astore #4
    //   379: aload #6
    //   381: invokevirtual printStackTrace : ()V
    //   384: aload_2
    //   385: ifnull -> 392
    //   388: aload_2
    //   389: invokevirtual close : ()V
    //   392: aload #5
    //   394: ifnull -> 314
    //   397: aload #5
    //   399: invokevirtual close : ()V
    //   402: return
    //   403: astore_2
    //   404: aload #4
    //   406: ifnull -> 414
    //   409: aload #4
    //   411: invokevirtual close : ()V
    //   414: aload_3
    //   415: ifnull -> 422
    //   418: aload_3
    //   419: invokevirtual close : ()V
    //   422: aload_2
    //   423: athrow
    //   424: astore #5
    //   426: aload #6
    //   428: astore_3
    //   429: aload_2
    //   430: astore #4
    //   432: aload #5
    //   434: astore_2
    //   435: goto -> 404
    //   438: astore #5
    //   440: aload #4
    //   442: astore_3
    //   443: aload_2
    //   444: astore #4
    //   446: aload #5
    //   448: astore_2
    //   449: goto -> 404
    //   452: astore #6
    //   454: goto -> 373
    //   457: astore #6
    //   459: aload #4
    //   461: astore #5
    //   463: goto -> 373
    // Exception table:
    //   from	to	target	type
    //   86	129	368	java/lang/Exception
    //   86	129	403	finally
    //   135	144	368	java/lang/Exception
    //   135	144	403	finally
    //   150	171	368	java/lang/Exception
    //   150	171	403	finally
    //   177	190	368	java/lang/Exception
    //   177	190	403	finally
    //   190	240	452	java/lang/Exception
    //   190	240	424	finally
    //   244	260	457	java/lang/Exception
    //   244	260	438	finally
    //   260	272	457	java/lang/Exception
    //   260	272	438	finally
    //   277	296	457	java/lang/Exception
    //   277	296	438	finally
    //   315	354	457	java/lang/Exception
    //   315	354	438	finally
    //   379	384	403	finally
  }
  
  private boolean SSC_Init(int paramInt) throws IOException {
    // Byte code:
    //   0: iconst_0
    //   1: istore #5
    //   3: iconst_0
    //   4: istore #6
    //   6: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   9: ldc_w 'SSC_Init()'
    //   12: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   15: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.mWifimanager : Landroid/net/wifi/WifiManager;
    //   18: invokevirtual getConnectionInfo : ()Landroid/net/wifi/WifiInfo;
    //   21: astore #9
    //   23: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.mWifimanager : Landroid/net/wifi/WifiManager;
    //   26: invokevirtual isWifiEnabled : ()Z
    //   29: ifeq -> 971
    //   32: iconst_0
    //   33: istore_2
    //   34: iconst_0
    //   35: istore_3
    //   36: iload_3
    //   37: iconst_5
    //   38: if_icmplt -> 70
    //   41: iload_2
    //   42: istore #4
    //   44: iload #4
    //   46: ifne -> 96
    //   49: aload_0
    //   50: getfield mHandler : Landroid/os/Handler;
    //   53: bipush #72
    //   55: invokevirtual sendEmptyMessage : (I)Z
    //   58: pop
    //   59: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   62: ldc_w 'SSC_Init Local Ip Address is 0'
    //   65: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   68: iconst_1
    //   69: ireturn
    //   70: aload #9
    //   72: invokevirtual getIpAddress : ()I
    //   75: istore_2
    //   76: iload_2
    //   77: istore #4
    //   79: iload_2
    //   80: ifne -> 44
    //   83: ldc2_w 1000
    //   86: invokestatic sleep : (J)V
    //   89: iload_3
    //   90: iconst_1
    //   91: iadd
    //   92: istore_3
    //   93: goto -> 36
    //   96: iload #4
    //   98: invokestatic intToIp : (I)Ljava/lang/String;
    //   101: astore #16
    //   103: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   106: new java/lang/StringBuilder
    //   109: dup
    //   110: ldc_w '-----------------------------localIp : '
    //   113: invokespecial <init> : (Ljava/lang/String;)V
    //   116: aload #16
    //   118: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   121: invokevirtual toString : ()Ljava/lang/String;
    //   124: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   127: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.mWifimanager : Landroid/net/wifi/WifiManager;
    //   130: invokevirtual getDhcpInfo : ()Landroid/net/DhcpInfo;
    //   133: getfield serverAddress : I
    //   136: invokestatic intToIp : (I)Ljava/lang/String;
    //   139: astore #15
    //   141: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   144: new java/lang/StringBuilder
    //   147: dup
    //   148: ldc_w '-----------------------------CameraIp : '
    //   151: invokespecial <init> : (Ljava/lang/String;)V
    //   154: aload #15
    //   156: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   159: invokevirtual toString : ()Ljava/lang/String;
    //   162: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   165: aload #9
    //   167: invokevirtual getSSID : ()Ljava/lang/String;
    //   170: invokestatic checkOldVersionSmartCameraApp : (Ljava/lang/String;)Z
    //   173: istore #7
    //   175: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/common/CMInfo;
    //   178: invokevirtual getIsNFCLaunch : ()Z
    //   181: ifeq -> 973
    //   184: iload #7
    //   186: ifeq -> 973
    //   189: ldc_w 'nfc'
    //   192: astore #8
    //   194: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   197: new java/lang/StringBuilder
    //   200: dup
    //   201: ldc_w '[S2L]accessMethod : '
    //   204: invokespecial <init> : (Ljava/lang/String;)V
    //   207: aload #8
    //   209: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   212: invokevirtual toString : ()Ljava/lang/String;
    //   215: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   218: new java/lang/StringBuffer
    //   221: dup
    //   222: invokespecial <init> : ()V
    //   225: astore #17
    //   227: aload #17
    //   229: ldc_w 'S2L/1.0 Request'
    //   232: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   235: ldc_w '\\r\\n'
    //   238: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   241: ldc_w 'Host: SAMSUNG-S2L'
    //   244: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   247: ldc_w '\\r\\n'
    //   250: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   253: ldc_w 'Content-Type: text/xml;charset=utf-8'
    //   256: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   259: ldc_w '\\r\\n'
    //   262: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   265: ldc_w 'User-Agent: APP-TYPE'
    //   268: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   271: ldc_w '\\r\\n'
    //   274: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   277: ldc_w 'Content-Length: 0'
    //   280: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   283: ldc_w '\\r\\n'
    //   286: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   289: ldc_w 'HOST-Mac : '
    //   292: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   295: aload #9
    //   297: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   300: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   303: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   306: ldc_w '\\r\\n'
    //   309: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   312: ldc_w 'HOST-Address : '
    //   315: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   318: aload #16
    //   320: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   323: ldc_w '\\r\\n'
    //   326: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   329: ldc_w 'HOST-port : '
    //   332: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   335: sipush #1801
    //   338: invokevirtual append : (I)Ljava/lang/StringBuffer;
    //   341: ldc_w '\\r\\n'
    //   344: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   347: ldc_w 'HOST-PNumber : '
    //   350: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   353: aload_0
    //   354: invokevirtual getApplicationContext : ()Landroid/content/Context;
    //   357: invokestatic getLineNumber : (Landroid/content/Context;)Ljava/lang/String;
    //   360: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   363: ldc_w '\\r\\n'
    //   366: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   369: ldc_w 'Host-Gps : '
    //   372: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   375: aload_0
    //   376: invokespecial getGpsInfo : ()Ljava/lang/String;
    //   379: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   382: ldc_w '\\r\\n'
    //   385: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   388: ldc_w 'Access-Method : '
    //   391: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   394: aload #8
    //   396: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   399: ldc_w '\\r\\n\\r\\n'
    //   402: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   405: pop
    //   406: aconst_null
    //   407: astore #8
    //   409: aconst_null
    //   410: astore #13
    //   412: aconst_null
    //   413: astore #12
    //   415: aconst_null
    //   416: astore #14
    //   418: aconst_null
    //   419: astore #11
    //   421: aload #14
    //   423: astore #10
    //   425: aload #8
    //   427: astore #9
    //   429: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   432: new java/lang/StringBuilder
    //   435: dup
    //   436: ldc_w 'Camera Connection Try... : ['
    //   439: invokespecial <init> : (Ljava/lang/String;)V
    //   442: aload #15
    //   444: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   447: ldc_w ':'
    //   450: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   453: iload_1
    //   454: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   457: ldc_w ']'
    //   460: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   463: invokevirtual toString : ()Ljava/lang/String;
    //   466: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   469: aload #14
    //   471: astore #10
    //   473: aload #8
    //   475: astore #9
    //   477: new java/net/Socket
    //   480: dup
    //   481: invokespecial <init> : ()V
    //   484: astore #18
    //   486: aload #14
    //   488: astore #10
    //   490: aload #8
    //   492: astore #9
    //   494: aload #18
    //   496: new java/net/InetSocketAddress
    //   499: dup
    //   500: aload #15
    //   502: iload_1
    //   503: invokespecial <init> : (Ljava/lang/String;I)V
    //   506: sipush #2000
    //   509: invokevirtual connect : (Ljava/net/SocketAddress;I)V
    //   512: aload #14
    //   514: astore #10
    //   516: aload #8
    //   518: astore #9
    //   520: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   523: new java/lang/StringBuilder
    //   526: dup
    //   527: ldc_w 'Camera Connection Success... : ['
    //   530: invokespecial <init> : (Ljava/lang/String;)V
    //   533: aload #15
    //   535: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   538: ldc_w ':'
    //   541: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   544: iload_1
    //   545: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   548: ldc_w ']'
    //   551: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   554: invokevirtual toString : ()Ljava/lang/String;
    //   557: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   560: aload #14
    //   562: astore #10
    //   564: aload #8
    //   566: astore #9
    //   568: aload_0
    //   569: iload_1
    //   570: putfield mPortNumber : I
    //   573: aload #14
    //   575: astore #10
    //   577: aload #8
    //   579: astore #9
    //   581: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   584: new java/lang/StringBuilder
    //   587: dup
    //   588: ldc_w 'Connected Port Num : '
    //   591: invokespecial <init> : (Ljava/lang/String;)V
    //   594: aload_0
    //   595: getfield mPortNumber : I
    //   598: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   601: invokevirtual toString : ()Ljava/lang/String;
    //   604: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   607: aload #14
    //   609: astore #10
    //   611: aload #8
    //   613: astore #9
    //   615: new java/io/BufferedOutputStream
    //   618: dup
    //   619: aload #18
    //   621: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   624: invokespecial <init> : (Ljava/io/OutputStream;)V
    //   627: astore #8
    //   629: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   632: aload #17
    //   634: invokevirtual toString : ()Ljava/lang/String;
    //   637: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   640: aload #8
    //   642: aload #17
    //   644: invokevirtual toString : ()Ljava/lang/String;
    //   647: invokevirtual getBytes : ()[B
    //   650: invokevirtual write : ([B)V
    //   653: aload #8
    //   655: invokevirtual flush : ()V
    //   658: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   661: ldc_w 'Performance Check Point : Discovery Request.'
    //   664: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   667: new java/io/BufferedInputStream
    //   670: dup
    //   671: aload #18
    //   673: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   676: invokespecial <init> : (Ljava/io/InputStream;)V
    //   679: astore #10
    //   681: ldc_w ''
    //   684: astore #9
    //   686: iload #6
    //   688: istore #5
    //   690: sipush #256
    //   693: newarray byte
    //   695: astore #12
    //   697: iload #6
    //   699: istore #5
    //   701: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   704: ldc_w 'Camera Response Waiting...'
    //   707: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   710: iload #6
    //   712: istore #5
    //   714: aload #10
    //   716: aload #12
    //   718: iconst_0
    //   719: aload #12
    //   721: arraylength
    //   722: invokevirtual read : ([BII)I
    //   725: istore_2
    //   726: iload_2
    //   727: iconst_m1
    //   728: if_icmpne -> 981
    //   731: iload #6
    //   733: istore #5
    //   735: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   738: ldc_w 'Performance Check Point : Camera Response Received.'
    //   741: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   744: iload #6
    //   746: istore #5
    //   748: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   751: aload #9
    //   753: invokevirtual toString : ()Ljava/lang/String;
    //   756: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   759: aload #8
    //   761: ifnull -> 773
    //   764: iload #6
    //   766: istore #5
    //   768: aload #8
    //   770: invokevirtual close : ()V
    //   773: aload #10
    //   775: ifnull -> 787
    //   778: iload #6
    //   780: istore #5
    //   782: aload #10
    //   784: invokevirtual close : ()V
    //   787: iload #6
    //   789: istore #5
    //   791: aload #9
    //   793: ldc_w 'Result_OK'
    //   796: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   799: ifeq -> 1043
    //   802: iload #6
    //   804: istore #5
    //   806: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   809: ldc_w 'SSC_init Camera Accept'
    //   812: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   815: iload #6
    //   817: istore #5
    //   819: aload_0
    //   820: aload #16
    //   822: invokestatic getByName : (Ljava/lang/String;)Ljava/net/InetAddress;
    //   825: putfield serverAddr : Ljava/net/InetAddress;
    //   828: iload #6
    //   830: istore #5
    //   832: aload_0
    //   833: iconst_1
    //   834: putfield bAcceptRun : Z
    //   837: iload #6
    //   839: istore #5
    //   841: aload_0
    //   842: getfield bRcvThreadRun : Z
    //   845: ifne -> 881
    //   848: iload #6
    //   850: istore #5
    //   852: aload_0
    //   853: iconst_1
    //   854: putfield bRcvThreadRun : Z
    //   857: iload #6
    //   859: istore #5
    //   861: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   864: ldc_w 'start RcvThread'
    //   867: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   870: iload #6
    //   872: istore #5
    //   874: aload_0
    //   875: getfield mRcvThread : Lcom/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare$RcvThread;
    //   878: invokevirtual start : ()V
    //   881: iload #6
    //   883: istore #5
    //   885: aload_0
    //   886: getfield mDialogCMWaitConnect : Lcom/samsungimaging/connectionmanager/dialog/ConnectionWaitDialog;
    //   889: ifnull -> 903
    //   892: iload #6
    //   894: istore #5
    //   896: aload_0
    //   897: getfield mDialogCMWaitConnect : Lcom/samsungimaging/connectionmanager/dialog/ConnectionWaitDialog;
    //   900: invokevirtual dismiss : ()V
    //   903: iconst_1
    //   904: istore #7
    //   906: iconst_1
    //   907: istore #6
    //   909: iload #7
    //   911: istore #5
    //   913: aload_0
    //   914: iconst_1
    //   915: putfield dscConnected : Z
    //   918: iload #7
    //   920: istore #5
    //   922: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   925: new java/lang/StringBuilder
    //   928: dup
    //   929: ldc_w 'SSC_Init() ended dscConnected : '
    //   932: invokespecial <init> : (Ljava/lang/String;)V
    //   935: aload_0
    //   936: getfield dscConnected : Z
    //   939: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   942: invokevirtual toString : ()Ljava/lang/String;
    //   945: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   948: aload #8
    //   950: ifnull -> 958
    //   953: aload #8
    //   955: invokevirtual close : ()V
    //   958: aload #10
    //   960: ifnull -> 968
    //   963: aload #10
    //   965: invokevirtual close : ()V
    //   968: iload #6
    //   970: ireturn
    //   971: iconst_0
    //   972: ireturn
    //   973: ldc_w 'manual'
    //   976: astore #8
    //   978: goto -> 194
    //   981: iload #6
    //   983: istore #5
    //   985: new java/lang/StringBuilder
    //   988: dup
    //   989: aload #9
    //   991: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   994: invokespecial <init> : (Ljava/lang/String;)V
    //   997: new java/lang/String
    //   1000: dup
    //   1001: aload #12
    //   1003: iconst_0
    //   1004: iload_2
    //   1005: invokespecial <init> : ([BII)V
    //   1008: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1011: invokevirtual toString : ()Ljava/lang/String;
    //   1014: astore #11
    //   1016: aload #11
    //   1018: astore #9
    //   1020: iload #6
    //   1022: istore #5
    //   1024: aload #11
    //   1026: ldc_w '\\n'
    //   1029: invokevirtual indexOf : (Ljava/lang/String;)I
    //   1032: iconst_m1
    //   1033: if_icmpeq -> 710
    //   1036: aload #11
    //   1038: astore #9
    //   1040: goto -> 731
    //   1043: iload #6
    //   1045: istore #5
    //   1047: aload #9
    //   1049: ldc_w 'Result_Error'
    //   1052: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   1055: ifeq -> 1129
    //   1058: iload #6
    //   1060: istore #5
    //   1062: aload_0
    //   1063: getfield mDialogCMWaitConnect : Lcom/samsungimaging/connectionmanager/dialog/ConnectionWaitDialog;
    //   1066: ifnull -> 1080
    //   1069: iload #6
    //   1071: istore #5
    //   1073: aload_0
    //   1074: getfield mDialogCMWaitConnect : Lcom/samsungimaging/connectionmanager/dialog/ConnectionWaitDialog;
    //   1077: invokevirtual dismiss : ()V
    //   1080: iload #6
    //   1082: istore #5
    //   1084: aload_0
    //   1085: getfield mHandler : Landroid/os/Handler;
    //   1088: bipush #74
    //   1090: invokevirtual sendEmptyMessage : (I)Z
    //   1093: pop
    //   1094: iload #6
    //   1096: istore #5
    //   1098: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1101: ldc_w 'SSC_init Camera Not Accept - Result_Error'
    //   1104: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1107: aload #8
    //   1109: ifnull -> 1117
    //   1112: aload #8
    //   1114: invokevirtual close : ()V
    //   1117: aload #10
    //   1119: ifnull -> 1127
    //   1122: aload #10
    //   1124: invokevirtual close : ()V
    //   1127: iconst_1
    //   1128: ireturn
    //   1129: iload #6
    //   1131: istore #5
    //   1133: aload #9
    //   1135: ldc_w 'Result_Reject'
    //   1138: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   1141: ifeq -> 1294
    //   1144: iload #6
    //   1146: istore #5
    //   1148: aload_0
    //   1149: getfield mDialogCMWaitConnect : Lcom/samsungimaging/connectionmanager/dialog/ConnectionWaitDialog;
    //   1152: ifnull -> 1166
    //   1155: iload #6
    //   1157: istore #5
    //   1159: aload_0
    //   1160: getfield mDialogCMWaitConnect : Lcom/samsungimaging/connectionmanager/dialog/ConnectionWaitDialog;
    //   1163: invokevirtual dismiss : ()V
    //   1166: iload #6
    //   1168: istore #5
    //   1170: aload_0
    //   1171: getfield mHandler : Landroid/os/Handler;
    //   1174: bipush #73
    //   1176: invokevirtual sendEmptyMessage : (I)Z
    //   1179: pop
    //   1180: iload #6
    //   1182: istore #5
    //   1184: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1187: ldc_w 'SSC_init Camera Not Accept - Result_Reject'
    //   1190: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1193: goto -> 1107
    //   1196: astore #12
    //   1198: aload #10
    //   1200: astore #11
    //   1202: aload #11
    //   1204: astore #10
    //   1206: aload #8
    //   1208: astore #9
    //   1210: aload #12
    //   1212: invokevirtual printStackTrace : ()V
    //   1215: aload #11
    //   1217: astore #10
    //   1219: aload #8
    //   1221: astore #9
    //   1223: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1226: new java/lang/StringBuilder
    //   1229: dup
    //   1230: ldc_w 'Camera Connection Fail : ['
    //   1233: invokespecial <init> : (Ljava/lang/String;)V
    //   1236: aload #15
    //   1238: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1241: ldc_w ':'
    //   1244: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1247: iload_1
    //   1248: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1251: ldc_w ']'
    //   1254: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1257: invokevirtual toString : ()Ljava/lang/String;
    //   1260: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1263: aload #8
    //   1265: ifnull -> 1273
    //   1268: aload #8
    //   1270: invokevirtual close : ()V
    //   1273: iload #5
    //   1275: istore #6
    //   1277: aload #11
    //   1279: ifnull -> 968
    //   1282: aload #11
    //   1284: invokevirtual close : ()V
    //   1287: iload #5
    //   1289: istore #6
    //   1291: goto -> 968
    //   1294: iload #6
    //   1296: istore #5
    //   1298: aload_0
    //   1299: getfield mDialogCMWaitConnect : Lcom/samsungimaging/connectionmanager/dialog/ConnectionWaitDialog;
    //   1302: ifnull -> 1316
    //   1305: iload #6
    //   1307: istore #5
    //   1309: aload_0
    //   1310: getfield mDialogCMWaitConnect : Lcom/samsungimaging/connectionmanager/dialog/ConnectionWaitDialog;
    //   1313: invokevirtual dismiss : ()V
    //   1316: iload #6
    //   1318: istore #5
    //   1320: aload_0
    //   1321: getfield mHandler : Landroid/os/Handler;
    //   1324: bipush #73
    //   1326: invokevirtual sendEmptyMessage : (I)Z
    //   1329: pop
    //   1330: iload #6
    //   1332: istore #5
    //   1334: getstatic com/samsungimaging/connectionmanager/app/pushservice/autoshare/AutoShare.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1337: ldc_w 'SSC_init Camera Invalid Response'
    //   1340: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1343: goto -> 1107
    //   1346: astore #11
    //   1348: aload #8
    //   1350: astore #9
    //   1352: aload #11
    //   1354: astore #8
    //   1356: aload #9
    //   1358: ifnull -> 1366
    //   1361: aload #9
    //   1363: invokevirtual close : ()V
    //   1366: aload #10
    //   1368: ifnull -> 1376
    //   1371: aload #10
    //   1373: invokevirtual close : ()V
    //   1376: aload #8
    //   1378: athrow
    //   1379: astore #9
    //   1381: iload #6
    //   1383: istore #5
    //   1385: aload #9
    //   1387: invokevirtual printStackTrace : ()V
    //   1390: goto -> 828
    //   1393: astore #8
    //   1395: goto -> 1356
    //   1398: astore #11
    //   1400: aload #12
    //   1402: astore #10
    //   1404: aload #8
    //   1406: astore #9
    //   1408: aload #11
    //   1410: astore #8
    //   1412: goto -> 1356
    //   1415: astore #12
    //   1417: aload #13
    //   1419: astore #8
    //   1421: goto -> 1202
    //   1424: astore #12
    //   1426: goto -> 1202
    // Exception table:
    //   from	to	target	type
    //   429	469	1415	java/lang/Exception
    //   429	469	1393	finally
    //   477	486	1415	java/lang/Exception
    //   477	486	1393	finally
    //   494	512	1415	java/lang/Exception
    //   494	512	1393	finally
    //   520	560	1415	java/lang/Exception
    //   520	560	1393	finally
    //   568	573	1415	java/lang/Exception
    //   568	573	1393	finally
    //   581	607	1415	java/lang/Exception
    //   581	607	1393	finally
    //   615	629	1415	java/lang/Exception
    //   615	629	1393	finally
    //   629	681	1424	java/lang/Exception
    //   629	681	1398	finally
    //   690	697	1196	java/lang/Exception
    //   690	697	1346	finally
    //   701	710	1196	java/lang/Exception
    //   701	710	1346	finally
    //   714	726	1196	java/lang/Exception
    //   714	726	1346	finally
    //   735	744	1196	java/lang/Exception
    //   735	744	1346	finally
    //   748	759	1196	java/lang/Exception
    //   748	759	1346	finally
    //   768	773	1196	java/lang/Exception
    //   768	773	1346	finally
    //   782	787	1196	java/lang/Exception
    //   782	787	1346	finally
    //   791	802	1196	java/lang/Exception
    //   791	802	1346	finally
    //   806	815	1196	java/lang/Exception
    //   806	815	1346	finally
    //   819	828	1379	java/net/UnknownHostException
    //   819	828	1196	java/lang/Exception
    //   819	828	1346	finally
    //   832	837	1196	java/lang/Exception
    //   832	837	1346	finally
    //   841	848	1196	java/lang/Exception
    //   841	848	1346	finally
    //   852	857	1196	java/lang/Exception
    //   852	857	1346	finally
    //   861	870	1196	java/lang/Exception
    //   861	870	1346	finally
    //   874	881	1196	java/lang/Exception
    //   874	881	1346	finally
    //   885	892	1196	java/lang/Exception
    //   885	892	1346	finally
    //   896	903	1196	java/lang/Exception
    //   896	903	1346	finally
    //   913	918	1196	java/lang/Exception
    //   913	918	1346	finally
    //   922	948	1196	java/lang/Exception
    //   922	948	1346	finally
    //   985	1016	1196	java/lang/Exception
    //   985	1016	1346	finally
    //   1024	1036	1196	java/lang/Exception
    //   1024	1036	1346	finally
    //   1047	1058	1196	java/lang/Exception
    //   1047	1058	1346	finally
    //   1062	1069	1196	java/lang/Exception
    //   1062	1069	1346	finally
    //   1073	1080	1196	java/lang/Exception
    //   1073	1080	1346	finally
    //   1084	1094	1196	java/lang/Exception
    //   1084	1094	1346	finally
    //   1098	1107	1196	java/lang/Exception
    //   1098	1107	1346	finally
    //   1133	1144	1196	java/lang/Exception
    //   1133	1144	1346	finally
    //   1148	1155	1196	java/lang/Exception
    //   1148	1155	1346	finally
    //   1159	1166	1196	java/lang/Exception
    //   1159	1166	1346	finally
    //   1170	1180	1196	java/lang/Exception
    //   1170	1180	1346	finally
    //   1184	1193	1196	java/lang/Exception
    //   1184	1193	1346	finally
    //   1210	1215	1393	finally
    //   1223	1263	1393	finally
    //   1298	1305	1196	java/lang/Exception
    //   1298	1305	1346	finally
    //   1309	1316	1196	java/lang/Exception
    //   1309	1316	1346	finally
    //   1320	1330	1196	java/lang/Exception
    //   1320	1330	1346	finally
    //   1334	1343	1196	java/lang/Exception
    //   1334	1343	1346	finally
    //   1385	1390	1196	java/lang/Exception
    //   1385	1390	1346	finally
  }
  
  private boolean bDownloading() {
    boolean bool = false;
    if (!this.isBackGround || this.pre_progress_percent == this.g_progress_percent)
      return false; 
    int i = 0;
    while (true) {
      if (i < 5) {
        if (this.rcv_on[i])
          return true; 
        i++;
        continue;
      } 
      return bool;
    } 
  }
  
  private String deg_to_dms(double paramDouble) {
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
  
  private String getGpsInfo() {
    if (this.locationManager == null)
      this.locationManager = (LocationManager)getSystemService("location"); 
    this.currentLocation_gps = this.locationManager.getLastKnownLocation("gps");
    this.currentLocation_network = this.locationManager.getLastKnownLocation("network");
    if (this.currentLocation_gps != null || this.currentLocation_network != null) {
      String str1;
      if (this.currentLocation_gps != null) {
        str1 = setGpsValue(this.currentLocation_gps);
      } else {
        str1 = setGpsValue(this.currentLocation_network);
      } 
      this.mGpsInfo = str1;
      Trace.d(TAG, "getGpsInfo : " + str1);
      return str1;
    } 
    String str = "none";
    this.mGpsInfo = null;
    Trace.d(TAG, "getGpsInfo : " + str);
    return str;
  }
  
  public static int getResolutionSize() {
    return mResolutionSize;
  }
  
  private void makeDirectory() {
    File file = new File(CommonUtils.getDefaultStorage());
    if (!file.exists() && !file.isDirectory())
      file.mkdirs(); 
  }
  
  private void registerDisconnectReceiver() {
    this.mWifiStateChangedReceiverForAppClose = new WifiStateChangedReceiverForAppClose(null);
    IntentFilter intentFilter = new IntentFilter("android.net.wifi.STATE_CHANGE");
    intentFilter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE");
    registerReceiver(this.mWifiStateChangedReceiverForAppClose, intentFilter);
  }
  
  private double roundDouble(double paramDouble, int paramInt) {
    return Math.round(Math.pow(10.0D, paramInt) * paramDouble) / Math.pow(10.0D, paramInt);
  }
  
  private void setContentView() {
    if (!CommonUtils.isMountedExternalStorage()) {
      showDialog(1003);
      return;
    } 
    if (CommonUtils.isMemoryFull()) {
      showDialog(1004);
      return;
    } 
  }
  
  private String setGpsValue(Location paramLocation) {
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
      this.Latitude = str1;
      this.Longitude = str;
      return String.valueOf(this.Latitude) + i + "X" + this.Longitude + j;
    } 
    String str2 = "W";
    j *= -1;
    this.Latitude = str1;
    this.Longitude = str2;
    return String.valueOf(this.Latitude) + i + "X" + this.Longitude + j;
  }
  
  private void startGpsTracking() {
    this.locationManager = (LocationManager)getSystemService("location");
    TelephonyManager telephonyManager = (TelephonyManager)getSystemService("phone");
    this.locationManager.requestLocationUpdates("gps", 5000L, 10.0F, this.locationListener_gps);
    if (telephonyManager.getNetworkType() != 0)
      this.locationManager.requestLocationUpdates("network", 5000L, 10.0F, this.locationListener_network); 
  }
  
  private void waitPreviousDownloading() {
    for (int i = 10;; i--) {
      if (i <= 0 || !this.rcv_on[0])
        return; 
      SystemClock.sleep(100L);
    } 
  }
  
  public boolean barray_reset(byte[] paramArrayOfbyte) {
    for (int i = 0;; i++) {
      if (i >= paramArrayOfbyte.length)
        return true; 
      paramArrayOfbyte[i] = 0;
    } 
  }
  
  protected void exit() {
    finishSafe("Exit_Pressed");
    super.exit();
  }
  
  public void exitAfterSendByeBye(String paramString) {
    if (this.dscConnected) {
      this.sscTaskEx = new DeInitTask(null);
      this.sscTaskEx.execute((Object[])new WifiInfo[] { this.mWifiInfo });
      Trace.d(TAG, " ================== SSC ByeBye Message Send Complete. ==================");
    } 
    if (!this.fileQueue.isEmpty()) {
      this.mReceivedFilePath = this.fileQueue.poll();
      File file = new File(this.mReceivedFilePath);
      if (file.exists())
        file.delete(); 
      Trace.d(TAG, "exitAfterSendByeBye  exit during downloaing image file");
    } 
    Trace.d(TAG, "exitAfterSendByeBye, send message App close beacuseof : " + paramString);
    Message message = this.mHandler.obtainMessage(24, paramString);
    this.mHandler.sendMessageDelayed(message, 500L);
  }
  
  public void finishSafe(String paramString) {
    setService(CommonPushService.Service.NONE);
    this.bAcceptRun = false;
    this.bRcvThreadRun = false;
    if (this.mDialogCMWaitConnect != null) {
      this.mDialogCMWaitConnect.dismiss();
      this.mDialogCMWaitConnect = null;
    } 
    this.nm.cancel(1);
    this.nm.cancel(2);
    if (this.sscTask != null)
      this.sscTask.cancel(true); 
    if (this.sscTaskEx != null)
      this.sscTaskEx.cancel(true); 
    if (this.mRcvThread != null) {
      Trace.d(TAG, "finishSafe interrupt RcvThread");
      this.mRcvThread.quit();
      this.mRcvThread.interrupt();
      if (this.mRcvThread.isAlive())
        try {
          Trace.d(TAG, "finishSafe RcvThread Alive wait ");
          SystemClock.sleep(1000L);
        } catch (Exception exception) {
          exception.printStackTrace();
        }  
      this.mRcvThread = null;
    } 
    try {
      unregisterReceiver(this.mWifiStateChangedReceiverForAppClose);
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
    Trace.d(TAG, "AutoShare is finishing...");
    if ("ByeBye_StandBy".equals(paramString)) {
      Trace.i(TAG, "finishSafe set CMService restart search true");
      CMService.getInstance().beforefinish(1);
    } else if ("Low_Battery".equals(paramString)) {
      Trace.i(TAG, "finishSafe set CMService with Low Battery");
      CMService.getInstance().beforefinish(2);
    } else {
      Trace.i(TAG, "finishSafe set CMService restart search false");
      CMService.getInstance().beforefinish(0);
    } 
    finish();
  }
  
  public int getAvailSock() {
    Trace.d(TAG, "getAvailSock");
    int i = 0;
    while (true) {
      if (i >= 5)
        return -1; 
      int j = i;
      if (this.rcv_on[i]) {
        Trace.d(TAG, "rcv_on[" + i + "] is true");
        i++;
        continue;
      } 
      return j;
    } 
  }
  
  public byte[] make_resp(String paramString) {
    if (paramString == "0") {
      String str1 = "S2L/1.0 Result_OK\r\nHost: " + this.mHostName + "\r\n" + "Content-length: " + this.mContentLength + "\r\n" + "Authorization: " + this.mAuthorization + "\r\n" + "Sub-ErrorCode: " + paramString + "\r\n\r\n";
      Trace.d(TAG, "make_resp : " + paramString);
      return str1.getBytes();
    } 
    String str = "S2L/1.0 Result_Error\r\nHost: " + this.mHostName + "\r\n" + "Content-length: " + this.mContentLength + "\r\n" + "Authorization: " + this.mAuthorization + "\r\n" + "Sub-ErrorCode: " + paramString + "\r\n\r\n";
    Trace.d(TAG, "make_resp : " + paramString);
    return str.getBytes();
  }
  
  public void onBackPressed() {
    if (isBackPressAvailable())
      exitAfterSendByeBye("BackKey_Pressed"); 
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    Trace.d(TAG, "onConfigurationChanged() newConfig.orientation : " + paramConfiguration.orientation);
    super.onConfigurationChanged(paramConfiguration);
    switch (paramConfiguration.orientation) {
      default:
        return;
      case 1:
      case 2:
        break;
    } 
    Trace.d(TAG, "-=> onConfigurationChanged  orientation [1:Port,2:Land] = " + paramConfiguration.orientation);
    setContentView();
    CommonUtils.setSystemConfigurationChanged(getApplicationContext());
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setService(CommonPushService.Service.AUTOSHARE);
    Trace.d(TAG, "oncreate : AutoShare");
    mWifimanager = (WifiManager)getSystemService("wifi");
    setContentView();
    this.mContext = (Context)this;
    this.nm = (NotificationManager)getSystemService("notification");
    startGpsTracking();
    this.noti = new Notification(2130837556, "AutoShare", System.currentTimeMillis());
    Notification notification2 = this.noti;
    notification2.flags |= 0x10;
    Intent intent2 = new Intent(getApplicationContext(), Main.class);
    intent2.addFlags(872415232);
    this.contentView = new RemoteViews(getPackageName(), 2130903086);
    this.noti.contentView = this.contentView;
    this.pending = PendingIntent.getActivity(getApplicationContext(), 0, intent2, 134217728);
    this.noti.contentIntent = this.pending;
    this.noti_prog = new Notification(2130837555, "AutoShare", System.currentTimeMillis());
    Notification notification1 = this.noti_prog;
    notification1.flags |= 0x10;
    notification1 = this.noti_prog;
    notification1.flags |= 0x2;
    Intent intent1 = new Intent(getApplicationContext(), Main.class);
    intent1.addFlags(872415232);
    this.contentView_prog = new RemoteViews(getPackageName(), 2130903087);
    this.contentView_prog.setProgressBar(2131558665, 100, 0, false);
    this.noti_prog.contentView = this.contentView_prog;
    this.pending_prog = PendingIntent.getActivity(getApplicationContext(), 0, intent1, 134217728);
    this.noti_prog.contentIntent = this.pending_prog;
    if (configChanged) {
      Trace.d(TAG, "onCreate  configChanged is true");
      Toast.makeText(this.mContext, 2131361981, 0).show();
      configChanged = false;
      this.mHandler.sendEmptyMessageDelayed(24, 3000L);
      return;
    } 
    makeDirectory();
    for (int i = 0;; i++) {
      if (i >= 5) {
        if (this.mRcvThread == null) {
          Trace.d(TAG, "create RcvThread");
          this.mRcvThread = new RcvThread();
        } 
        if (!this.mSettings.getIntroGuide()) {
          showDialog(1002);
        } else {
          Toast.makeText(this.mContext, 2131361823, 0).show();
        } 
        this.fileQueue = new LinkedList<String>();
        this.mHandler.sendEmptyMessage(3);
        if (mWifimanager.isWifiEnabled() && !this.dscConnected)
          showDialog(1005); 
        this.mPowerManager = (PowerManager)this.mContext.getSystemService("power");
        this.mWakeLock = this.mPowerManager.newWakeLock(1, "SSC_WAKE_LOCK");
        return;
      } 
      this.rcv_on[i] = false;
    } 
  }
  
  protected Dialog onCreateDialog(int paramInt, Bundle paramBundle) {
    Trace.i(TAG, "onCreateDialog() id " + paramInt);
    switch (paramInt) {
      default:
        return super.onCreateDialog(paramInt, paramBundle);
      case 1005:
        if (this.mDialogCMWaitConnect == null) {
          this.mDialogCMWaitConnect = new ConnectionWaitDialog((Context)this);
          this.mDialogCMWaitConnect.setOnKeyListener(new DialogInterface.OnKeyListener() {
                public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                  switch (param1Int) {
                    default:
                      return false;
                    case 4:
                      break;
                  } 
                  Trace.d(AutoShare.TAG, "back key pressed, send app close message");
                  AutoShare.this.dscConnected = true;
                  AutoShare.this.exitAfterSendByeBye("Canceled");
                }
              });
          this.mDialogCMWaitConnect.setNeutralButton(17039360, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                  Trace.d(AutoShare.TAG, "Connection Wait Dialog is close, dscConnected is " + AutoShare.this.dscConnected);
                  AutoShare.this.dscConnected = true;
                  AutoShare.this.exitAfterSendByeBye("Canceled");
                }
              });
        } 
        return (Dialog)this.mDialogCMWaitConnect;
      case 1007:
        break;
    } 
    RefusalDialog refusalDialog = new RefusalDialog((Context)this);
    refusalDialog.setTag(paramBundle.getString("error"));
    refusalDialog.setNeutralButton(2131361842, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            Trace.d(AutoShare.TAG, "Camera service is rejected, closing...");
            AutoShare.this.mHandler.sendEmptyMessage(24);
            param1DialogInterface.dismiss();
          }
        });
    refusalDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
          public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
            if (param1KeyEvent.getKeyCode() == 4) {
              AutoShare.this.mHandler.sendEmptyMessage(24);
              param1DialogInterface.dismiss();
              return true;
            } 
            return false;
          }
        });
    return (Dialog)refusalDialog;
  }
  
  protected void onDestroy() {
    Trace.i(TAG, "onDestroy()");
    super.onDestroy();
    if (CMUtil.isForceClose((ActivityManager)getSystemService("activity")))
      SystemClock.sleep(3000L); 
  }
  
  protected void onPause() {
    super.onPause();
    this.isBackGround = true;
    Trace.d(TAG, "onPause isBackGround true");
    if (this.isHomeKeyPressed) {
      String.format(getResources().getString(2131361825), new Object[] { Integer.valueOf(save_cnt) });
      this.nm.notify(1, this.noti);
      this.isHomeKeyPressed = false;
    } 
  }
  
  protected void onResume() {
    super.onResume();
    Trace.d(TAG, "**** onresume");
    this.noti.defaults = 0;
    this.isBackGround = false;
    this.nm.cancel(1);
    this.nm.cancel(2);
    Trace.d(TAG, "onResume() ended dscConnected : " + this.dscConnected);
    Trace.d(TAG, "isBackGround false");
  }
  
  protected void onUserLeaveHint() {
    if (!isFinishing())
      this.isHomeKeyPressed = true; 
    Trace.d(TAG, "onUserLeaveHint(), isHomeKeyPressed : " + this.isHomeKeyPressed);
    super.onUserLeaveHint();
  }
  
  public String toString() {
    return "AutoShare";
  }
  
  private class DeInitTask extends AsyncTask<WifiInfo, Void, Boolean> {
    private DeInitTask() {}
    
    protected Boolean doInBackground(WifiInfo... param1VarArgs) {
      try {
        AutoShare.this.SSC_DeInit(AutoShare.this.mWifiInfo);
      } catch (IOException iOException) {
        iOException.printStackTrace();
      } 
      return null;
    }
    
    protected void onPostExecute(Boolean param1Boolean) {
      super.onPostExecute(param1Boolean);
    }
    
    protected void onPreExecute() {
      super.onPreExecute();
    }
  }
  
  private class GpsTask extends AsyncTask<Void, Void, Boolean> {
    protected Boolean doInBackground(Void... param1VarArgs) {
      Trace.d(AutoShare.TAG, "GpsTask doInBackground()");
      try {
        AutoShare.this.SSC_GpsSend();
      } catch (IOException iOException) {
        iOException.printStackTrace();
      } 
      return Boolean.valueOf(true);
    }
    
    protected void onPostExecute(Boolean param1Boolean) {
      Trace.d(AutoShare.TAG, "GpsTask onPostExecute()");
      super.onPostExecute(param1Boolean);
    }
    
    protected void onPreExecute() {
      Trace.d(AutoShare.TAG, "GpsTask onPreExecute()");
      super.onPreExecute();
    }
  }
  
  public class ImageDownloader {
    DataInputStream dis = null;
    
    DataOutputStream dos = null;
    
    FileOutputStream fos;
    
    InputStream inStream = null;
    
    String mFileName;
    
    File mReceivingFile = null;
    
    private TimerTask mTimer;
    
    OutputStream outStream = null;
    
    int sock_idx;
    
    protected Socket socket;
    
    private Timer timer = new Timer();
    
    public ImageDownloader(Socket param1Socket, int param1Int) {
      this.socket = param1Socket;
      this.sock_idx = param1Int;
      try {
        this.inStream = param1Socket.getInputStream();
        this.outStream = param1Socket.getOutputStream();
        return;
      } catch (IOException iOException) {
        iOException.printStackTrace();
        return;
      } 
    }
    
    private void start() {
      int i = 256;
      long l = 0L;
      try {
        byte[] arrayOfByte = new byte[102400];
        boolean bool = false;
        Trace.d(AutoShare.TAG, "ImageDownloader Start, bAcceptRun : " + AutoShare.this.bAcceptRun);
        boolean bool1 = AutoShare.this.bAcceptRun;
        if (!bool1)
          return; 
      } catch (Exception exception) {
        exception.printStackTrace();
        try {
          if (this.fos != null)
            this.fos.close(); 
        } catch (IOException iOException) {}
        if (this.mReceivingFile != null && this.mReceivingFile.exists()) {
          Trace.d(AutoShare.TAG, "ImageDownloader Cancel Saving File Delete : " + this.mReceivingFile.getName());
          this.mReceivingFile.delete();
        } 
        Trace.d(AutoShare.TAG, "ImageDownloader  Exception: " + exception.toString());
        try {
          this.dos.write(AutoShare.this.make_resp("2400"));
          Trace.d(AutoShare.TAG, "Performance Check Point : Socket Create Fail");
          this.socket.close();
        } catch (IOException iOException) {}
        AutoShare.this.mHandler.sendEmptyMessage(36);
        return;
      } finally {
        AutoShare.this.rcv_on[this.sock_idx] = false;
      } 
      this.socket.close();
      AutoShare.this.rcv_on[this.sock_idx] = false;
      throw SYNTHETIC_LOCAL_VARIABLE_17;
    }
    
    private void startTimer() {
      Trace.d(AutoShare.TAG, "startTimer");
      this.mTimer = new TimerTask() {
          public void run() {
            Trace.d(AutoShare.TAG, "Blocking....");
            (AutoShare.ImageDownloader.access$1(AutoShare.ImageDownloader.this)).rcv_on[AutoShare.ImageDownloader.this.sock_idx] = false;
            try {
              if (AutoShare.ImageDownloader.this.dis != null) {
                AutoShare.ImageDownloader.this.dis.close();
                AutoShare.ImageDownloader.this.dis = null;
              } 
              if (AutoShare.ImageDownloader.this.dos != null) {
                AutoShare.ImageDownloader.this.dos.close();
                AutoShare.ImageDownloader.this.dos = null;
              } 
              return;
            } catch (IOException iOException) {
              Trace.d(AutoShare.TAG, "IOException 03");
              iOException.printStackTrace();
              return;
            } 
          }
        };
      this.timer.schedule(this.mTimer, 1000L);
    }
    
    private void stopTimer() {
      Trace.d(AutoShare.TAG, "stopTimer");
      this.mTimer.cancel();
    }
    
    public String getFileName() {
      return this.mFileName;
    }
    
    public void setFileName(String param1String) {
      this.mFileName = param1String;
    }
  }
  
  class null extends TimerTask {
    public void run() {
      Trace.d(AutoShare.TAG, "Blocking....");
      (AutoShare.ImageDownloader.access$1(this.this$1)).rcv_on[this.this$1.sock_idx] = false;
      try {
        if (this.this$1.dis != null) {
          this.this$1.dis.close();
          this.this$1.dis = null;
        } 
        if (this.this$1.dos != null) {
          this.this$1.dos.close();
          this.this$1.dos = null;
        } 
        return;
      } catch (IOException iOException) {
        Trace.d(AutoShare.TAG, "IOException 03");
        iOException.printStackTrace();
        return;
      } 
    }
  }
  
  private class InitTask extends AsyncTask<WifiInfo, Void, Boolean> {
    private InitTask() {}
    
    protected Boolean doInBackground(WifiInfo... param1VarArgs) {
      Trace.d(AutoShare.TAG, "InitTask doInBackground()");
      int i = 801;
      while (true) {
        if (i < 804)
          try {
            boolean bool = AutoShare.this.SSC_Init(i);
            if (!bool) {
              i++;
              continue;
            } 
          } catch (IOException iOException) {
            iOException.printStackTrace();
          }  
        return Boolean.valueOf(true);
      } 
    }
    
    protected void onPostExecute(Boolean param1Boolean) {
      Trace.d(AutoShare.TAG, "InitTask onPostExecute()");
      if (AutoShare.this.mDialogCMWaitConnect != null) {
        AutoShare.this.mDialogCMWaitConnect.dismiss();
        AutoShare.this.mDialogCMWaitConnect = null;
      } 
      super.onPostExecute(param1Boolean);
    }
    
    protected void onPreExecute() {
      Trace.d(AutoShare.TAG, "InitTask onPreExecute()");
      super.onPreExecute();
    }
  }
  
  public class MyPhoneStateListener extends PhoneStateListener {
    public void onCallStateChanged(int param1Int, String param1String) {
      switch (param1Int) {
      
      } 
    }
  }
  
  public class RcvThread extends Thread {
    public void quit() {
      if (AutoShare.this.svSock != null)
        try {
          AutoShare.this.svSock.close();
          Trace.d(AutoShare.TAG, "Server Socket close");
          return;
        } catch (Exception exception) {
          exception.printStackTrace();
          return;
        }  
    }
    
    public void run() {
      try {
        Trace.i(AutoShare.TAG, "SSC RcvThread Run...");
        AutoShare.this.svSock = new ServerSocket(1801, 0, AutoShare.this.serverAddr);
        Trace.i(AutoShare.TAG, "SSC RcvThread Server Socket Created.");
        AutoShare.this.socket_idx = 0;
        while (true) {
          if (AutoShare.this.bAcceptRun) {
            AutoShare.this.socket_idx = AutoShare.this.getAvailSock();
            if (AutoShare.this.socket_idx != -1) {
              Trace.i(AutoShare.TAG, "SSC RcvThread Server Socket Waiting...");
              Socket socket = AutoShare.this.svSock.accept();
              socket.setSoTimeout(5000);
              Trace.i(AutoShare.TAG, "SSC RcvThread Server Socket Accept.");
              Trace.i(AutoShare.TAG, "socket_idx1: " + AutoShare.this.socket_idx);
              if (isInterrupted()) {
                Trace.d(AutoShare.TAG, "RcvThread interrupted");
              } else {
                AutoShare.this.rcv_on[AutoShare.this.socket_idx] = true;
                AutoShare.this.svSockReceiveBufferSize = AutoShare.this.svSock.getReceiveBufferSize();
                Trace.d(AutoShare.TAG, "svSock.getReceiveBufferSize() : " + AutoShare.this.svSock.getReceiveBufferSize());
                if (AutoShare.this.mWakeLock != null)
                  AutoShare.this.mWakeLock.acquire(); 
                AutoShare.ImageDownloader imageDownloader = new AutoShare.ImageDownloader(socket, AutoShare.this.socket_idx);
                AutoShare.this.waitPreviousDownloading();
                imageDownloader.start();
                if (AutoShare.this.mWakeLock != null && AutoShare.this.mWakeLock.isHeld())
                  AutoShare.this.mWakeLock.release(); 
                continue;
              } 
            } else {
              Trace.d(AutoShare.TAG, "No available socket");
              continue;
            } 
          } 
          AutoShare.this.svSock.close();
          Trace.d(AutoShare.TAG, "Server Socket close");
          try {
            AutoShare.this.svSock.close();
            Trace.d(AutoShare.TAG, "Server Socket close");
          } catch (Exception exception) {
            exception.printStackTrace();
          } 
          return;
        } 
      } catch (IOException iOException) {
        iOException.printStackTrace();
        try {
          AutoShare.this.svSock.close();
          Trace.d(AutoShare.TAG, "Server Socket close");
        } catch (Exception exception) {
          exception.printStackTrace();
        } 
        return;
      } finally {
        try {
          AutoShare.this.svSock.close();
          Trace.d(AutoShare.TAG, "Server Socket close");
        } catch (Exception exception) {
          exception.printStackTrace();
        } 
      } 
    }
  }
  
  private class WifiStateChangedReceiverForAppClose extends BroadcastReceiver {
    private WifiStateChangedReceiverForAppClose() {}
    
    public void onReceive(Context param1Context, Intent param1Intent) {
      if ("android.net.wifi.supplicant.CONNECTION_CHANGE".equals(param1Intent.getAction())) {
        if (!param1Intent.getBooleanExtra("connected", false) && AutoShare.this.wifi_stat_conn) {
          AutoShare.this.wifi_stat_conn = false;
          AutoShare.this.dscConnected = false;
          AutoShare.this.mHandler.sendEmptyMessage(52);
        } 
        return;
      } 
      if (param1Intent.getAction().equals("android.net.wifi.STATE_CHANGE")) {
        NetworkInfo.DetailedState detailedState = ((NetworkInfo)param1Intent.getParcelableExtra("networkInfo")).getDetailedState();
        Trace.d(AutoShare.TAG, "NetworkInfo.DetailedState = " + detailedState.name());
        switch (detailedState) {
          default:
            Trace.d(AutoShare.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - Unknown");
            return;
          case CONNECTING:
            AutoShare.this.wifi_stat_conn = true;
            Trace.d(AutoShare.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - CONNECTING");
            return;
          case OBTAINING_IPADDR:
            Trace.d(AutoShare.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - OBTAINING_IPADDR");
            return;
          case CONNECTED:
            AutoShare.this.wifi_stat_conn = true;
            Trace.d(AutoShare.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - CONNECTED");
            return;
          case FAILED:
            Trace.d(AutoShare.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - FAILED");
            return;
          case DISCONNECTING:
            Trace.d(AutoShare.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - DISCONNECTING");
            return;
          case DISCONNECTED:
            break;
        } 
        Trace.d(AutoShare.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - DISCONNECTED");
        Trace.d(AutoShare.TAG, "wifi_stat_conn : " + AutoShare.this.wifi_stat_conn);
        if (AutoShare.this.wifi_stat_conn) {
          AutoShare.this.wifi_stat_conn = false;
          AutoShare.this.dscConnected = false;
          AutoShare.this.mHandler.sendEmptyMessage(52);
          return;
        } 
      } 
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pushservice\autoshare\AutoShare.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */