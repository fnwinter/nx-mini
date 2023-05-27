package com.samsungimaging.connectionmanager.app.pushservice.selectivepush;

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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class SelectivePush extends CommonPushService {
  private static final int NOTI_RECEIVE_DONE = 1;
  
  private static final int NOTI_RECEIVE_PROGRESS = 2;
  
  private static final int SERVER_PORT = 18100;
  
  private static final Trace.Tag TAG = Trace.Tag.SP;
  
  private static boolean configChanged = false;
  
  private static int mResolutionSize;
  
  private static WifiManager mWifimanager;
  
  private static int save_cnt;
  
  private static int updateInterval = 0;
  
  private boolean bAcceptRun = false;
  
  private boolean bRcvThreadRun = false;
  
  private boolean bReject = false;
  
  BufferedInputStream bis;
  
  BufferedOutputStream bos;
  
  Socket clientSocket = null;
  
  private RemoteViews contentView;
  
  private RemoteViews contentView_prog;
  
  private boolean dscConnected = false;
  
  private Queue<String> fileQueue = null;
  
  private int g_progress_percent = 0;
  
  private int image_file_idx = 0;
  
  private boolean isBackGround = false;
  
  private boolean isFileSaveError = false;
  
  private boolean isGetCommand = false;
  
  private boolean isHeaderError = false;
  
  private boolean isHomeKeyPressed = false;
  
  private int mCameraPort = 8100;
  
  private Context mContext = null;
  
  private String mCreateFilePath = null;
  
  private ConnectionWaitDialog mDialogCMWaitConnect = null;
  
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
            Trace.d(SelectivePush.TAG, "Const.MsgId.APP_CLOSE_FOR_REMOVED_SD");
            SelectivePush.this.showDialog(1003);
            return;
          case 3:
            Trace.d(SelectivePush.TAG, "Const.MsgId.AP_CONNECTED");
            SelectivePush.this.registerDisconnectReceiver();
            SelectivePush.this.mWifiInfo = SelectivePush.mWifimanager.getConnectionInfo();
            i = SelectivePush.this.mWifiInfo.getIpAddress();
            Trace.d(SelectivePush.TAG, "ip:" + i + " -> " + CommonUtils.intToIp(i));
            if (SelectivePush.mWifimanager.isWifiEnabled() && !SelectivePush.this.dscConnected) {
              Trace.d(SelectivePush.TAG, "wifi was already connected");
              SelectivePush.this.sscTask = new SelectivePush.InitTask(null);
              SelectivePush.this.sscTask.execute((Object[])new WifiInfo[] { SelectivePush.access$4(this.this$0) });
              Trace.d(SelectivePush.TAG, "InitTask called  1");
              return;
            } 
          case 53:
            Trace.d(SelectivePush.TAG, "Const.MsgId.IMAGE_RCV_WAIT");
            return;
          case 51:
            Trace.d(SelectivePush.TAG, "Const.MsgId.IMAGE_RCV_DONE");
            if (!SelectivePush.this.fileQueue.isEmpty()) {
              SelectivePush.this.mReceivedFilePath = SelectivePush.this.fileQueue.poll();
              Trace.d(SelectivePush.TAG, "SSC Thumbnail [" + SelectivePush.save_cnt + "] change. path:" + SelectivePush.this.mReceivedFilePath);
            } 
            SelectivePush.save_cnt = SelectivePush.save_cnt + 1;
            Trace.d(SelectivePush.TAG, "IMAGE_RCV_DONE [" + SelectivePush.save_cnt + "] change. path:" + SelectivePush.this.mReceivedFilePath);
            if (SelectivePush.this.isBackGround) {
              notification = SelectivePush.this.noti;
              notification.defaults |= 0x1;
              String.format(SelectivePush.this.getResources().getString(2131361825), new Object[] { Integer.valueOf(SelectivePush.access$10()) });
              SelectivePush.this.nm.notify(1, SelectivePush.this.noti);
              SelectivePush.this.nm.cancel(2);
              return;
            } 
          case 52:
            SelectivePush.this.bAcceptRun = false;
            for (i = 0;; i++) {
              if (i >= 5) {
                SelectivePush.this.showDialog(1006);
                Trace.d(SelectivePush.TAG, "wifi disconnected!!!!!, send message app close after 1000ms");
                SelectivePush.this.mHandler.sendEmptyMessageDelayed(24, 1000L);
                return;
              } 
              SelectivePush.this.rcv_on[i] = false;
            } 
          case 24:
            str = (String)((Message)notification).obj;
            Trace.d(SelectivePush.TAG, "APP_CLOSED with  : " + str);
            SelectivePush.this.finishSafe(str);
            return;
          case 63:
            customProgressDialog2 = (CustomProgressDialog)SelectivePush.this.mDialogList.get(1000);
            if ((customProgressDialog2 == null || !customProgressDialog2.isShowing()) && SelectivePush.this.getService() != CommonPushService.Service.MOBILEBACKUP) {
              Trace.d(SelectivePush.TAG, "Show mProgressDialog");
              SelectivePush.this.showDialog(1000);
              ((CustomProgressDialog)SelectivePush.this.mDialogList.get(1000)).setMax(((Message)str).arg1);
            } 
            SelectivePush.this.fileQueue.offer(SelectivePush.this.mCreateFilePath);
            Trace.d(SelectivePush.TAG, "SSC Thumbnail [" + SelectivePush.save_cnt + "] add. path:" + SelectivePush.this.mCreateFilePath);
            return;
          case 62:
            customProgressDialog2 = (CustomProgressDialog)SelectivePush.this.mDialogList.get(1000);
            if (customProgressDialog2 != null && customProgressDialog2.isShowing())
              customProgressDialog2.setProgress(((Message)str).arg2); 
            if (SelectivePush.this.bDownloading()) {
              SelectivePush.this.contentView_prog.setProgressBar(2131558665, 100, SelectivePush.this.g_progress_percent, false);
              SelectivePush.this.nm.notify(2, SelectivePush.this.noti_prog);
              SelectivePush.this.pre_progress_percent = SelectivePush.this.g_progress_percent;
              return;
            } 
          case 64:
            SelectivePush.this.mGallery.setState(GalleryFragment.State.NORMAL, new Object[0]);
            customProgressDialog2 = (CustomProgressDialog)SelectivePush.this.mDialogList.get(1000);
            if (customProgressDialog2 != null) {
              customProgressDialog2.setProgress(((Message)str).arg1);
              customProgressDialog2.dismiss();
            } 
            if (SelectivePush.this.bDownloading()) {
              SelectivePush.this.contentView_prog.setProgressBar(2131558665, 100, 100, false);
              SelectivePush.this.nm.notify(2, SelectivePush.this.noti_prog);
              SelectivePush.this.pre_progress_percent = SelectivePush.this.g_progress_percent;
              return;
            } 
          case 36:
            Trace.d(SelectivePush.TAG, "Const.MsgId.CANCEL_SAVING");
            customProgressDialog1 = (CustomProgressDialog)SelectivePush.this.mDialogList.get(1000);
            if (customProgressDialog1 != null && customProgressDialog1.isShowing())
              customProgressDialog1.dismiss(); 
            SelectivePush.this.nm.cancel(1);
            SelectivePush.this.nm.cancel(2);
            return;
          case 71:
            Trace.d(SelectivePush.TAG, "Const.MsgId.BACK_BUTTON_INVALID");
            return;
          case 72:
            Toast.makeText(SelectivePush.this.mContext, 2131361895, 0).show();
            Trace.d(SelectivePush.TAG, "Const.MsgId.WIFI_CONNECTION_IS_BAD");
            SelectivePush.this.mHandler.sendEmptyMessageDelayed(24, 3000L);
            return;
          case 73:
            bundle = new Bundle();
            bundle.putString("error", "CHECK_FOR_401_ERROR");
            SelectivePush.this.showDialog(1007, bundle);
            return;
          case 74:
            break;
        } 
        Bundle bundle = new Bundle();
        bundle.putString("error", "CHECK_FOR_503_ERROR");
        SelectivePush.this.showDialog(1007, bundle);
      }
    };
  
  private PowerManager mPowerManager = null;
  
  private RcvThread mRcvThread = null;
  
  private String mReceivedFilePath = null;
  
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
  
  private SdcardStateChangedReceiver sdcardStateChangedReceiver = new SdcardStateChangedReceiver(null);
  
  private InetAddress serverAddr = null;
  
  int socket_idx = 0;
  
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
    boolean bool2 = false;
    boolean bool1 = false;
    Trace.d(TAG, "SSC_DeInit()");
    WifiInfo wifiInfo = paramWifiInfo;
    if (paramWifiInfo == null)
      wifiInfo = mWifimanager.getConnectionInfo(); 
    if (mWifimanager.isWifiEnabled()) {
      String str1 = CommonUtils.intToIp(wifiInfo.getIpAddress());
      Trace.d(TAG, "-----------------------------localIp : " + str1);
      String str2 = CommonUtils.intToIp((mWifimanager.getDhcpInfo()).serverAddress);
      Trace.d(TAG, "-----------------------------CameraIp : " + str2);
      null = new StringBuffer();
      null.append("HEAD /sp/control HTTP/1.1").append("\r\n").append("Host: 192.168.104.1:8100").append("\r\n").append("User-Agent: SEC_SP_").append(wifiInfo.getMacAddress().toLowerCase()).append("\r\n").append("Data-Server : ").append(str1).append(":18100").append("\r\n").append("Data-Port : ").append("18100").append("\r\n").append("NTS : byebye").append("\r\n\r\n");
      boolean bool = bool2;
      try {
        Trace.d(TAG, "CameraIp:" + str2 + ", CameraPort : " + this.mCameraPort);
        bool = bool2;
        this.clientSocket = new Socket();
        bool = bool2;
        InetSocketAddress inetSocketAddress = new InetSocketAddress(str2, this.mCameraPort);
        bool = bool2;
        this.clientSocket.connect(inetSocketAddress, 3000);
        bool = bool2;
        this.bos = new BufferedOutputStream(this.clientSocket.getOutputStream());
        bool = bool2;
        this.bos.write(null.toString().getBytes());
        bool = bool2;
        this.bos.flush();
        bool = bool2;
        Trace.d(TAG, null.toString());
        bool = bool2;
        this.bis = new BufferedInputStream(this.clientSocket.getInputStream());
        String str = "";
        bool = bool2;
        byte[] arrayOfByte = new byte[256];
        while (true) {
          bool = bool2;
          int i = this.bis.read(arrayOfByte, 0, arrayOfByte.length);
          if (i != -1) {
            bool = bool2;
            String str3 = String.valueOf(str) + new String(arrayOfByte, 0, i);
            str = str3;
            bool = bool2;
            if (str3.indexOf("\n") != -1) {
              str = str3;
            } else {
              continue;
            } 
          } 
          bool = bool2;
          Trace.d(TAG, "Camera Response Received.");
          bool = bool2;
          Trace.d(TAG, str.toString());
          bool = bool2;
          if (str.contains("200 OK")) {
            bool = bool2;
            Trace.d(TAG, "Camera has Accept");
            bool = bool2;
            this.dscConnected = false;
            bool = true;
            bool1 = true;
            Trace.d(TAG, "SSC_DeInit() ended dscConnected : " + this.dscConnected);
            bool = bool1;
          } else {
            bool = bool2;
            if (str.contains("500 Internal Server Error")) {
              bool = bool2;
              Trace.d(TAG, "Camera has Not Accept");
              bool = bool1;
            } else {
              bool = bool2;
              Trace.d(TAG, "Camera No Responce");
              bool = bool1;
            } 
          } 
          this.dscConnected = false;
          bool = true;
          bool1 = true;
          Trace.d(TAG, "SSC_DeInit() ended dscConnected : " + this.dscConnected);
          return bool;
        } 
      } catch (Exception exception) {
        exception.printStackTrace();
        return bool;
      } finally {
        try {
          this.bos.close();
          this.bis.close();
          this.clientSocket.close();
        } catch (Exception exception) {
          exception.printStackTrace();
        } 
      } 
    } 
    return false;
  }
  
  private int SSC_Init(int paramInt) throws IOException {
    // Byte code:
    //   0: iconst_0
    //   1: istore #5
    //   3: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   6: ldc_w 'SSC_Init()'
    //   9: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   12: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.mWifimanager : Landroid/net/wifi/WifiManager;
    //   15: invokevirtual getConnectionInfo : ()Landroid/net/wifi/WifiInfo;
    //   18: astore #9
    //   20: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.mWifimanager : Landroid/net/wifi/WifiManager;
    //   23: invokevirtual isWifiEnabled : ()Z
    //   26: ifeq -> 817
    //   29: iconst_0
    //   30: istore_2
    //   31: iconst_0
    //   32: istore_3
    //   33: iload_3
    //   34: iconst_5
    //   35: if_icmplt -> 67
    //   38: iload_2
    //   39: istore #4
    //   41: iload #4
    //   43: ifne -> 93
    //   46: aload_0
    //   47: getfield mHandler : Landroid/os/Handler;
    //   50: bipush #72
    //   52: invokevirtual sendEmptyMessage : (I)Z
    //   55: pop
    //   56: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   59: ldc_w 'SSC_Init Local Ip Address is 0'
    //   62: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   65: iconst_1
    //   66: ireturn
    //   67: aload #9
    //   69: invokevirtual getIpAddress : ()I
    //   72: istore_2
    //   73: iload_2
    //   74: istore #4
    //   76: iload_2
    //   77: ifne -> 41
    //   80: ldc2_w 1000
    //   83: invokestatic sleep : (J)V
    //   86: iload_3
    //   87: iconst_1
    //   88: iadd
    //   89: istore_3
    //   90: goto -> 33
    //   93: iload #4
    //   95: invokestatic intToIp : (I)Ljava/lang/String;
    //   98: astore #8
    //   100: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   103: new java/lang/StringBuilder
    //   106: dup
    //   107: ldc '-----------------------------localIp : '
    //   109: invokespecial <init> : (Ljava/lang/String;)V
    //   112: aload #8
    //   114: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   117: invokevirtual toString : ()Ljava/lang/String;
    //   120: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   123: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.mWifimanager : Landroid/net/wifi/WifiManager;
    //   126: invokevirtual getDhcpInfo : ()Landroid/net/DhcpInfo;
    //   129: getfield serverAddress : I
    //   132: invokestatic intToIp : (I)Ljava/lang/String;
    //   135: astore #10
    //   137: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   140: new java/lang/StringBuilder
    //   143: dup
    //   144: ldc_w '-----------------------------mCameraIp : '
    //   147: invokespecial <init> : (Ljava/lang/String;)V
    //   150: aload #10
    //   152: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   155: invokevirtual toString : ()Ljava/lang/String;
    //   158: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   161: aload #9
    //   163: invokevirtual getSSID : ()Ljava/lang/String;
    //   166: invokestatic checkOldVersionSmartCameraApp : (Ljava/lang/String;)Z
    //   169: istore #6
    //   171: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/common/CMInfo;
    //   174: invokevirtual getIsNFCLaunch : ()Z
    //   177: ifeq -> 819
    //   180: iload #6
    //   182: ifeq -> 819
    //   185: ldc_w 'nfc'
    //   188: astore #7
    //   190: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   193: new java/lang/StringBuilder
    //   196: dup
    //   197: ldc_w '[SP]accessMethod : '
    //   200: invokespecial <init> : (Ljava/lang/String;)V
    //   203: aload #7
    //   205: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   208: invokevirtual toString : ()Ljava/lang/String;
    //   211: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   214: new java/lang/StringBuffer
    //   217: dup
    //   218: invokespecial <init> : ()V
    //   221: astore #11
    //   223: aload #11
    //   225: ldc_w 'HEAD /sp/control HTTP/1.1'
    //   228: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   231: ldc_w '\\r\\n'
    //   234: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   237: ldc_w 'Host: 192.168.104.1:8100'
    //   240: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   243: ldc_w '\\r\\n'
    //   246: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   249: ldc_w 'User-Agent: SEC_SP_'
    //   252: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   255: aload #9
    //   257: invokevirtual getMacAddress : ()Ljava/lang/String;
    //   260: invokevirtual toLowerCase : ()Ljava/lang/String;
    //   263: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   266: ldc_w '\\r\\n'
    //   269: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   272: ldc_w 'Data-Server : '
    //   275: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   278: aload #8
    //   280: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   283: ldc_w ':18100'
    //   286: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   289: ldc_w '\\r\\n'
    //   292: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   295: ldc_w 'Data-Port : '
    //   298: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   301: ldc_w '18100'
    //   304: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   307: ldc_w '\\r\\n'
    //   310: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   313: ldc_w 'NTS : alive'
    //   316: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   319: ldc_w '\\r\\n'
    //   322: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   325: ldc_w 'HOST-PNumber : '
    //   328: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   331: aload_0
    //   332: invokevirtual getApplicationContext : ()Landroid/content/Context;
    //   335: invokestatic getLineNumber : (Landroid/content/Context;)Ljava/lang/String;
    //   338: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   341: ldc_w '\\r\\n'
    //   344: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   347: ldc_w 'Access-Method : '
    //   350: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   353: aload #7
    //   355: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   358: ldc_w '\\r\\n\\r\\n'
    //   361: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   364: pop
    //   365: iload #5
    //   367: istore_2
    //   368: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   371: new java/lang/StringBuilder
    //   374: dup
    //   375: ldc_w 'mCameraIp:'
    //   378: invokespecial <init> : (Ljava/lang/String;)V
    //   381: aload #10
    //   383: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   386: ldc_w ', mCameraPort : '
    //   389: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   392: iload_1
    //   393: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   396: invokevirtual toString : ()Ljava/lang/String;
    //   399: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   402: iload #5
    //   404: istore_2
    //   405: aload_0
    //   406: new java/net/Socket
    //   409: dup
    //   410: invokespecial <init> : ()V
    //   413: putfield clientSocket : Ljava/net/Socket;
    //   416: iload #5
    //   418: istore_2
    //   419: new java/net/InetSocketAddress
    //   422: dup
    //   423: aload #10
    //   425: iload_1
    //   426: invokespecial <init> : (Ljava/lang/String;I)V
    //   429: astore #7
    //   431: iload #5
    //   433: istore_2
    //   434: aload_0
    //   435: getfield clientSocket : Ljava/net/Socket;
    //   438: aload #7
    //   440: sipush #2000
    //   443: invokevirtual connect : (Ljava/net/SocketAddress;I)V
    //   446: iload #5
    //   448: istore_2
    //   449: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   452: new java/lang/StringBuilder
    //   455: dup
    //   456: ldc_w 'Connected Port Num : '
    //   459: invokespecial <init> : (Ljava/lang/String;)V
    //   462: iload_1
    //   463: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   466: invokevirtual toString : ()Ljava/lang/String;
    //   469: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   472: iload #5
    //   474: istore_2
    //   475: aload_0
    //   476: iload_1
    //   477: putfield mCameraPort : I
    //   480: iload #5
    //   482: istore_2
    //   483: aload_0
    //   484: new java/io/BufferedOutputStream
    //   487: dup
    //   488: aload_0
    //   489: getfield clientSocket : Ljava/net/Socket;
    //   492: invokevirtual getOutputStream : ()Ljava/io/OutputStream;
    //   495: invokespecial <init> : (Ljava/io/OutputStream;)V
    //   498: putfield bos : Ljava/io/BufferedOutputStream;
    //   501: iload #5
    //   503: istore_2
    //   504: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   507: aload #11
    //   509: invokevirtual toString : ()Ljava/lang/String;
    //   512: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   515: iload #5
    //   517: istore_2
    //   518: aload_0
    //   519: getfield bos : Ljava/io/BufferedOutputStream;
    //   522: aload #11
    //   524: invokevirtual toString : ()Ljava/lang/String;
    //   527: invokevirtual getBytes : ()[B
    //   530: invokevirtual write : ([B)V
    //   533: iload #5
    //   535: istore_2
    //   536: aload_0
    //   537: getfield bos : Ljava/io/BufferedOutputStream;
    //   540: invokevirtual flush : ()V
    //   543: iload #5
    //   545: istore_2
    //   546: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   549: ldc_w 'Performance Check Point : Discovery Request.'
    //   552: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   555: iload #5
    //   557: istore_2
    //   558: aload_0
    //   559: new java/io/BufferedInputStream
    //   562: dup
    //   563: aload_0
    //   564: getfield clientSocket : Ljava/net/Socket;
    //   567: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   570: invokespecial <init> : (Ljava/io/InputStream;)V
    //   573: putfield bis : Ljava/io/BufferedInputStream;
    //   576: iload #5
    //   578: istore_2
    //   579: aload_0
    //   580: aload #8
    //   582: invokestatic getByName : (Ljava/lang/String;)Ljava/net/InetAddress;
    //   585: putfield serverAddr : Ljava/net/InetAddress;
    //   588: iload #5
    //   590: istore_2
    //   591: aload_0
    //   592: iconst_1
    //   593: putfield bAcceptRun : Z
    //   596: iload #5
    //   598: istore_2
    //   599: aload_0
    //   600: getfield bRcvThreadRun : Z
    //   603: ifne -> 636
    //   606: iload #5
    //   608: istore_2
    //   609: aload_0
    //   610: iconst_1
    //   611: putfield bRcvThreadRun : Z
    //   614: iload #5
    //   616: istore_2
    //   617: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   620: ldc_w 'start RcvThread'
    //   623: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   626: iload #5
    //   628: istore_2
    //   629: aload_0
    //   630: getfield mRcvThread : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush$RcvThread;
    //   633: invokevirtual start : ()V
    //   636: ldc_w ''
    //   639: astore #7
    //   641: iload #5
    //   643: istore_2
    //   644: sipush #256
    //   647: newarray byte
    //   649: astore #9
    //   651: iload #5
    //   653: istore_2
    //   654: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   657: ldc_w 'Camera Response Waiting...'
    //   660: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   663: iload #5
    //   665: istore_2
    //   666: aload_0
    //   667: getfield bis : Ljava/io/BufferedInputStream;
    //   670: aload #9
    //   672: iconst_0
    //   673: aload #9
    //   675: arraylength
    //   676: invokevirtual read : ([BII)I
    //   679: istore_1
    //   680: iload_1
    //   681: iconst_m1
    //   682: if_icmpne -> 885
    //   685: iload #5
    //   687: istore_2
    //   688: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   691: ldc_w 'Performance Check Point : Camera Response Received.'
    //   694: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   697: iload #5
    //   699: istore_2
    //   700: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   703: aload #7
    //   705: invokevirtual toString : ()Ljava/lang/String;
    //   708: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   711: iload #5
    //   713: istore_2
    //   714: aload #7
    //   716: ldc_w '200 OK'
    //   719: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   722: ifeq -> 945
    //   725: iload #5
    //   727: istore_2
    //   728: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   731: ldc_w 'Camera has Accept'
    //   734: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   737: iconst_1
    //   738: istore_3
    //   739: iconst_1
    //   740: istore_1
    //   741: iload_3
    //   742: istore_2
    //   743: aload_0
    //   744: iconst_1
    //   745: putfield dscConnected : Z
    //   748: iload_3
    //   749: istore_2
    //   750: aload_0
    //   751: getfield mDialogCMWaitConnect : Lcom/samsungimaging/connectionmanager/dialog/ConnectionWaitDialog;
    //   754: ifnull -> 766
    //   757: iload_3
    //   758: istore_2
    //   759: aload_0
    //   760: getfield mDialogCMWaitConnect : Lcom/samsungimaging/connectionmanager/dialog/ConnectionWaitDialog;
    //   763: invokevirtual dismiss : ()V
    //   766: iload_3
    //   767: istore_2
    //   768: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   771: new java/lang/StringBuilder
    //   774: dup
    //   775: ldc_w 'SSC_Init() ended dscConnected : '
    //   778: invokespecial <init> : (Ljava/lang/String;)V
    //   781: aload_0
    //   782: getfield dscConnected : Z
    //   785: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   788: invokevirtual toString : ()Ljava/lang/String;
    //   791: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   794: aload_0
    //   795: getfield bos : Ljava/io/BufferedOutputStream;
    //   798: invokevirtual close : ()V
    //   801: aload_0
    //   802: getfield bis : Ljava/io/BufferedInputStream;
    //   805: invokevirtual close : ()V
    //   808: aload_0
    //   809: getfield clientSocket : Ljava/net/Socket;
    //   812: invokevirtual close : ()V
    //   815: iload_1
    //   816: ireturn
    //   817: iconst_0
    //   818: ireturn
    //   819: ldc_w 'manual'
    //   822: astore #7
    //   824: goto -> 190
    //   827: astore #7
    //   829: iload #5
    //   831: istore_2
    //   832: aload #7
    //   834: invokevirtual printStackTrace : ()V
    //   837: goto -> 588
    //   840: astore #7
    //   842: aload #7
    //   844: invokevirtual printStackTrace : ()V
    //   847: aload_0
    //   848: getfield bos : Ljava/io/BufferedOutputStream;
    //   851: invokevirtual close : ()V
    //   854: aload_0
    //   855: getfield bis : Ljava/io/BufferedInputStream;
    //   858: invokevirtual close : ()V
    //   861: aload_0
    //   862: getfield clientSocket : Ljava/net/Socket;
    //   865: invokevirtual close : ()V
    //   868: iload_2
    //   869: istore_1
    //   870: goto -> 815
    //   873: astore #7
    //   875: aload #7
    //   877: invokevirtual printStackTrace : ()V
    //   880: iload_2
    //   881: istore_1
    //   882: goto -> 815
    //   885: iload #5
    //   887: istore_2
    //   888: new java/lang/StringBuilder
    //   891: dup
    //   892: aload #7
    //   894: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   897: invokespecial <init> : (Ljava/lang/String;)V
    //   900: new java/lang/String
    //   903: dup
    //   904: aload #9
    //   906: iconst_0
    //   907: iload_1
    //   908: invokespecial <init> : ([BII)V
    //   911: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   914: invokevirtual toString : ()Ljava/lang/String;
    //   917: astore #8
    //   919: aload #8
    //   921: astore #7
    //   923: iload #5
    //   925: istore_2
    //   926: aload #8
    //   928: ldc_w '\\n'
    //   931: invokevirtual indexOf : (Ljava/lang/String;)I
    //   934: iconst_m1
    //   935: if_icmpeq -> 663
    //   938: aload #8
    //   940: astore #7
    //   942: goto -> 685
    //   945: iload #5
    //   947: istore_2
    //   948: aload #7
    //   950: ldc_w '503 Service Unavailable'
    //   953: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   956: ifeq -> 1035
    //   959: iload #5
    //   961: istore_2
    //   962: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   965: ldc_w 'SSC_init Camera Not Accept - 503 Service Unavailable'
    //   968: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   971: iconst_m1
    //   972: istore_1
    //   973: iload_1
    //   974: istore_2
    //   975: aload_0
    //   976: getfield mHandler : Landroid/os/Handler;
    //   979: bipush #74
    //   981: invokevirtual sendEmptyMessage : (I)Z
    //   984: pop
    //   985: iload_1
    //   986: istore_2
    //   987: aload_0
    //   988: iconst_1
    //   989: putfield bReject : Z
    //   992: iload_1
    //   993: istore_2
    //   994: aload_0
    //   995: iconst_0
    //   996: putfield bAcceptRun : Z
    //   999: iload_1
    //   1000: istore_2
    //   1001: aload_0
    //   1002: iconst_0
    //   1003: putfield bRcvThreadRun : Z
    //   1006: goto -> 794
    //   1009: astore #7
    //   1011: aload_0
    //   1012: getfield bos : Ljava/io/BufferedOutputStream;
    //   1015: invokevirtual close : ()V
    //   1018: aload_0
    //   1019: getfield bis : Ljava/io/BufferedInputStream;
    //   1022: invokevirtual close : ()V
    //   1025: aload_0
    //   1026: getfield clientSocket : Ljava/net/Socket;
    //   1029: invokevirtual close : ()V
    //   1032: aload #7
    //   1034: athrow
    //   1035: iload #5
    //   1037: istore_2
    //   1038: aload #7
    //   1040: ldc_w '401 Unauthorized'
    //   1043: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   1046: ifeq -> 1099
    //   1049: iload #5
    //   1051: istore_2
    //   1052: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1055: ldc_w 'SSC_init Camera Not Accept - 401 Unauthorized'
    //   1058: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1061: iconst_m1
    //   1062: istore_1
    //   1063: iload_1
    //   1064: istore_2
    //   1065: aload_0
    //   1066: getfield mHandler : Landroid/os/Handler;
    //   1069: bipush #73
    //   1071: invokevirtual sendEmptyMessage : (I)Z
    //   1074: pop
    //   1075: iload_1
    //   1076: istore_2
    //   1077: aload_0
    //   1078: iconst_1
    //   1079: putfield bReject : Z
    //   1082: iload_1
    //   1083: istore_2
    //   1084: aload_0
    //   1085: iconst_0
    //   1086: putfield bAcceptRun : Z
    //   1089: iload_1
    //   1090: istore_2
    //   1091: aload_0
    //   1092: iconst_0
    //   1093: putfield bRcvThreadRun : Z
    //   1096: goto -> 794
    //   1099: iload #5
    //   1101: istore_2
    //   1102: getstatic com/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1105: ldc_w 'SSC_init Camera Invalid Response'
    //   1108: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1111: iconst_m1
    //   1112: istore_1
    //   1113: iload_1
    //   1114: istore_2
    //   1115: aload_0
    //   1116: iconst_m1
    //   1117: putfield mCameraPort : I
    //   1120: iload_1
    //   1121: istore_2
    //   1122: aload_0
    //   1123: iconst_1
    //   1124: putfield bReject : Z
    //   1127: iload_1
    //   1128: istore_2
    //   1129: aload_0
    //   1130: iconst_0
    //   1131: putfield bAcceptRun : Z
    //   1134: iload_1
    //   1135: istore_2
    //   1136: aload_0
    //   1137: iconst_0
    //   1138: putfield bRcvThreadRun : Z
    //   1141: goto -> 794
    //   1144: astore #8
    //   1146: aload #8
    //   1148: invokevirtual printStackTrace : ()V
    //   1151: goto -> 1032
    //   1154: astore #7
    //   1156: aload #7
    //   1158: invokevirtual printStackTrace : ()V
    //   1161: goto -> 815
    // Exception table:
    //   from	to	target	type
    //   368	402	840	java/lang/Exception
    //   368	402	1009	finally
    //   405	416	840	java/lang/Exception
    //   405	416	1009	finally
    //   419	431	840	java/lang/Exception
    //   419	431	1009	finally
    //   434	446	840	java/lang/Exception
    //   434	446	1009	finally
    //   449	472	840	java/lang/Exception
    //   449	472	1009	finally
    //   475	480	840	java/lang/Exception
    //   475	480	1009	finally
    //   483	501	840	java/lang/Exception
    //   483	501	1009	finally
    //   504	515	840	java/lang/Exception
    //   504	515	1009	finally
    //   518	533	840	java/lang/Exception
    //   518	533	1009	finally
    //   536	543	840	java/lang/Exception
    //   536	543	1009	finally
    //   546	555	840	java/lang/Exception
    //   546	555	1009	finally
    //   558	576	840	java/lang/Exception
    //   558	576	1009	finally
    //   579	588	827	java/net/UnknownHostException
    //   579	588	840	java/lang/Exception
    //   579	588	1009	finally
    //   591	596	840	java/lang/Exception
    //   591	596	1009	finally
    //   599	606	840	java/lang/Exception
    //   599	606	1009	finally
    //   609	614	840	java/lang/Exception
    //   609	614	1009	finally
    //   617	626	840	java/lang/Exception
    //   617	626	1009	finally
    //   629	636	840	java/lang/Exception
    //   629	636	1009	finally
    //   644	651	840	java/lang/Exception
    //   644	651	1009	finally
    //   654	663	840	java/lang/Exception
    //   654	663	1009	finally
    //   666	680	840	java/lang/Exception
    //   666	680	1009	finally
    //   688	697	840	java/lang/Exception
    //   688	697	1009	finally
    //   700	711	840	java/lang/Exception
    //   700	711	1009	finally
    //   714	725	840	java/lang/Exception
    //   714	725	1009	finally
    //   728	737	840	java/lang/Exception
    //   728	737	1009	finally
    //   743	748	840	java/lang/Exception
    //   743	748	1009	finally
    //   750	757	840	java/lang/Exception
    //   750	757	1009	finally
    //   759	766	840	java/lang/Exception
    //   759	766	1009	finally
    //   768	794	840	java/lang/Exception
    //   768	794	1009	finally
    //   794	815	1154	java/lang/Exception
    //   832	837	840	java/lang/Exception
    //   832	837	1009	finally
    //   842	847	1009	finally
    //   847	868	873	java/lang/Exception
    //   888	919	840	java/lang/Exception
    //   888	919	1009	finally
    //   926	938	840	java/lang/Exception
    //   926	938	1009	finally
    //   948	959	840	java/lang/Exception
    //   948	959	1009	finally
    //   962	971	840	java/lang/Exception
    //   962	971	1009	finally
    //   975	985	840	java/lang/Exception
    //   975	985	1009	finally
    //   987	992	840	java/lang/Exception
    //   987	992	1009	finally
    //   994	999	840	java/lang/Exception
    //   994	999	1009	finally
    //   1001	1006	840	java/lang/Exception
    //   1001	1006	1009	finally
    //   1011	1032	1144	java/lang/Exception
    //   1038	1049	840	java/lang/Exception
    //   1038	1049	1009	finally
    //   1052	1061	840	java/lang/Exception
    //   1052	1061	1009	finally
    //   1065	1075	840	java/lang/Exception
    //   1065	1075	1009	finally
    //   1077	1082	840	java/lang/Exception
    //   1077	1082	1009	finally
    //   1084	1089	840	java/lang/Exception
    //   1084	1089	1009	finally
    //   1091	1096	840	java/lang/Exception
    //   1091	1096	1009	finally
    //   1102	1111	840	java/lang/Exception
    //   1102	1111	1009	finally
    //   1115	1120	840	java/lang/Exception
    //   1115	1120	1009	finally
    //   1122	1127	840	java/lang/Exception
    //   1122	1127	1009	finally
    //   1129	1134	840	java/lang/Exception
    //   1129	1134	1009	finally
    //   1136	1141	840	java/lang/Exception
    //   1136	1141	1009	finally
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
  
  public static int getResolutionSize() {
    return mResolutionSize;
  }
  
  private void registerDisconnectReceiver() {
    this.mWifiStateChangedReceiverForAppClose = new WifiStateChangedReceiverForAppClose(null);
    IntentFilter intentFilter = new IntentFilter("android.net.wifi.STATE_CHANGE");
    intentFilter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE");
    registerReceiver(this.mWifiStateChangedReceiverForAppClose, intentFilter);
  }
  
  private void registerReceiver() {
    IntentFilter intentFilter = new IntentFilter("android.intent.action.MEDIA_REMOVED");
    intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
    intentFilter.addDataScheme("file");
    registerReceiver(this.sdcardStateChangedReceiver, intentFilter);
    MyPhoneStateListener myPhoneStateListener = new MyPhoneStateListener();
    ((TelephonyManager)getSystemService("phone")).listen(myPhoneStateListener, 32);
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
    this.isGetCommand = false;
    if (this.mRcvThread != null) {
      Trace.d(TAG, "finishSafe interrupt RcvThread");
      this.mRcvThread.quit();
      this.mRcvThread.interrupt();
      if (this.mRcvThread.isAlive())
        try {
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
    Trace.d(TAG, "Selective Push is finishing...");
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
      String str1 = "HTTP/1.1 200 OK\r\nContent-Length: 0\r\nSub-ErrorCode: " + paramString + "\r\n\r\n";
      Trace.d(TAG, "make_resp : " + paramString);
      return str1.getBytes();
    } 
    String str = "HTTP/1.1 500 Internal Server Error\r\nSub-ErrorCode: " + paramString + "\r\n\r\n";
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
    Trace.d(TAG, "onConfigurationChanged() newConfig.orientation : " + paramConfiguration.orientation);
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
    String str = getIntent().getStringExtra("selectivepush_service_type");
    if (str.equalsIgnoreCase("mobilebackup")) {
      setService(CommonPushService.Service.MOBILEBACKUP);
    } else {
      setService(CommonPushService.Service.SELECTIVEPUSH);
    } 
    Trace.d(TAG, "oncreate : " + str);
    mWifimanager = (WifiManager)getSystemService("wifi");
    setContentView();
    this.mContext = (Context)this;
    this.nm = (NotificationManager)getSystemService("notification");
    this.noti = new Notification(2130837556, "Mobile Link", System.currentTimeMillis());
    Notification notification2 = this.noti;
    notification2.flags |= 0x10;
    Intent intent2 = new Intent(getApplicationContext(), Main.class);
    intent2.addFlags(872415232);
    this.contentView = new RemoteViews(getPackageName(), 2130903086);
    this.noti.contentView = this.contentView;
    this.pending = PendingIntent.getActivity(getApplicationContext(), 0, intent2, 134217728);
    this.noti.contentIntent = this.pending;
    this.noti_prog = new Notification(2130837555, "Mobile Link", System.currentTimeMillis());
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
    File file = new File(CommonUtils.getDefaultStorage());
    if (!file.exists() && !file.isDirectory())
      file.mkdirs(); 
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
                  Trace.d(SelectivePush.TAG, "back key pressed, send app close message");
                  SelectivePush.this.mHandler.sendEmptyMessageDelayed(24, 500L);
                }
              });
          this.mDialogCMWaitConnect.setNeutralButton(17039360, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                  Trace.d(SelectivePush.TAG, "Connection Wait Dialog is close, dscConnected is " + SelectivePush.this.dscConnected);
                  SelectivePush.this.dscConnected = true;
                  SelectivePush.this.exitAfterSendByeBye("Canceled");
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
            Trace.d(SelectivePush.TAG, "Camera service is rejected, closing...");
            SelectivePush.this.mHandler.sendEmptyMessage(24);
            param1DialogInterface.dismiss();
          }
        });
    refusalDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
          public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
            if (param1KeyEvent.getKeyCode() == 4) {
              SelectivePush.this.mHandler.sendEmptyMessage(24);
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
    Trace.d(TAG, "isBackGround true");
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
    return "SelectivePush";
  }
  
  private class DeInitTask extends AsyncTask<WifiInfo, Void, Boolean> {
    private DeInitTask() {}
    
    protected Boolean doInBackground(WifiInfo... param1VarArgs) {
      try {
        SelectivePush.this.SSC_DeInit(SelectivePush.this.mWifiInfo);
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
      // Byte code:
      //   0: sipush #512
      //   3: istore_1
      //   4: lconst_0
      //   5: lstore #8
      //   7: ldc 102400
      //   9: newarray byte
      //   11: astore #21
      //   13: iconst_0
      //   14: istore_3
      //   15: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   18: new java/lang/StringBuilder
      //   21: dup
      //   22: ldc 'ImageDownloader Start, bAcceptRun : '
      //   24: invokespecial <init> : (Ljava/lang/String;)V
      //   27: aload_0
      //   28: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   31: invokestatic access$31 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)Z
      //   34: invokevirtual append : (Z)Ljava/lang/StringBuilder;
      //   37: invokevirtual toString : ()Ljava/lang/String;
      //   40: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   43: aload_0
      //   44: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   47: invokestatic access$31 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)Z
      //   50: istore #20
      //   52: iload #20
      //   54: ifne -> 71
      //   57: aload_0
      //   58: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   61: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   64: aload_0
      //   65: getfield sock_idx : I
      //   68: iconst_0
      //   69: bastore
      //   70: return
      //   71: aload_0
      //   72: new java/io/DataInputStream
      //   75: dup
      //   76: aload_0
      //   77: getfield inStream : Ljava/io/InputStream;
      //   80: invokespecial <init> : (Ljava/io/InputStream;)V
      //   83: putfield dis : Ljava/io/DataInputStream;
      //   86: aload_0
      //   87: new java/io/DataOutputStream
      //   90: dup
      //   91: aload_0
      //   92: getfield outStream : Ljava/io/OutputStream;
      //   95: invokespecial <init> : (Ljava/io/OutputStream;)V
      //   98: putfield dos : Ljava/io/DataOutputStream;
      //   101: iconst_0
      //   102: istore_2
      //   103: lconst_0
      //   104: lstore #10
      //   106: iconst_0
      //   107: istore #5
      //   109: aload_0
      //   110: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   113: iconst_0
      //   114: invokestatic access$34 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;Z)V
      //   117: aload_0
      //   118: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   121: iconst_0
      //   122: invokestatic access$35 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;Z)V
      //   125: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   128: new java/lang/StringBuilder
      //   131: dup
      //   132: ldc 'socket isConnected : '
      //   134: invokespecial <init> : (Ljava/lang/String;)V
      //   137: aload_0
      //   138: getfield socket : Ljava/net/Socket;
      //   141: invokevirtual isConnected : ()Z
      //   144: invokevirtual append : (Z)Ljava/lang/StringBuilder;
      //   147: invokevirtual toString : ()Ljava/lang/String;
      //   150: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   153: aload_0
      //   154: getfield dis : Ljava/io/DataInputStream;
      //   157: aload #21
      //   159: iconst_0
      //   160: iload_1
      //   161: invokevirtual read : ([BII)I
      //   164: istore #7
      //   166: lload #10
      //   168: lstore #12
      //   170: lload #8
      //   172: lstore #14
      //   174: iload #7
      //   176: iconst_m1
      //   177: if_icmpeq -> 198
      //   180: aload_0
      //   181: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   184: invokestatic access$31 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)Z
      //   187: ifne -> 267
      //   190: lload #8
      //   192: lstore #14
      //   194: lload #10
      //   196: lstore #12
      //   198: aload_0
      //   199: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   202: invokestatic access$42 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)Z
      //   205: ifeq -> 2374
      //   208: aload_0
      //   209: getfield socket : Ljava/net/Socket;
      //   212: invokevirtual close : ()V
      //   215: aload_0
      //   216: getfield socket : Ljava/net/Socket;
      //   219: invokevirtual isClosed : ()Z
      //   222: ifeq -> 2364
      //   225: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   228: ldc 'sNp : socket close'
      //   230: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   233: aload_0
      //   234: getfield socket : Ljava/net/Socket;
      //   237: invokevirtual close : ()V
      //   240: aload_0
      //   241: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   244: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   247: aload_0
      //   248: getfield sock_idx : I
      //   251: iconst_0
      //   252: bastore
      //   253: aload_0
      //   254: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   257: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   260: aload_0
      //   261: getfield sock_idx : I
      //   264: iconst_0
      //   265: bastore
      //   266: return
      //   267: iload_3
      //   268: ifne -> 1939
      //   271: iload_2
      //   272: iload #7
      //   274: iconst_4
      //   275: isub
      //   276: if_icmple -> 469
      //   279: lload #8
      //   281: lstore #14
      //   283: iload_1
      //   284: istore #4
      //   286: lload #10
      //   288: lstore #12
      //   290: iload_3
      //   291: istore #6
      //   293: lload #12
      //   295: lstore #18
      //   297: iload_2
      //   298: istore #7
      //   300: iload #4
      //   302: istore_1
      //   303: lload #14
      //   305: lstore #16
      //   307: iload_3
      //   308: ifne -> 2029
      //   311: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   314: ldc 'Can't find contents download header start'
      //   316: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   319: new java/lang/String
      //   322: dup
      //   323: aload #21
      //   325: iconst_0
      //   326: iload_2
      //   327: invokespecial <init> : ([BII)V
      //   330: astore #21
      //   332: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   335: new java/lang/StringBuilder
      //   338: dup
      //   339: ldc 'header : '
      //   341: invokespecial <init> : (Ljava/lang/String;)V
      //   344: aload #21
      //   346: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   349: invokevirtual toString : ()Ljava/lang/String;
      //   352: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   355: new com/samsungimaging/connectionmanager/app/pushservice/selectivepush/HeaderParser
      //   358: dup
      //   359: aload #21
      //   361: invokespecial <init> : (Ljava/lang/String;)V
      //   364: ldc 'Request'
      //   366: invokevirtual getSpecificValue : (Ljava/lang/String;)Ljava/lang/String;
      //   369: astore #21
      //   371: aload #21
      //   373: ldc 'ByeBye_Stand'
      //   375: invokevirtual contains : (Ljava/lang/CharSequence;)Z
      //   378: ifne -> 391
      //   381: aload #21
      //   383: ldc 'byebye_stand'
      //   385: invokevirtual contains : (Ljava/lang/CharSequence;)Z
      //   388: ifeq -> 1818
      //   391: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   394: ldc 'ImageDownloader Header Parsing ByeBye_StandBy, send message App close with standby'
      //   396: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   399: aload_0
      //   400: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   403: getfield mHandler : Landroid/os/Handler;
      //   406: bipush #24
      //   408: ldc 'ByeBye_StandBy'
      //   410: invokevirtual obtainMessage : (ILjava/lang/Object;)Landroid/os/Message;
      //   413: astore #21
      //   415: aload_0
      //   416: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   419: getfield mHandler : Landroid/os/Handler;
      //   422: aload #21
      //   424: invokevirtual sendMessage : (Landroid/os/Message;)Z
      //   427: pop
      //   428: aload_0
      //   429: getfield socket : Ljava/net/Socket;
      //   432: invokevirtual close : ()V
      //   435: aload_0
      //   436: getfield socket : Ljava/net/Socket;
      //   439: invokevirtual close : ()V
      //   442: aload_0
      //   443: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   446: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   449: aload_0
      //   450: getfield sock_idx : I
      //   453: iconst_0
      //   454: bastore
      //   455: aload_0
      //   456: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   459: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   462: aload_0
      //   463: getfield sock_idx : I
      //   466: iconst_0
      //   467: bastore
      //   468: return
      //   469: aload #21
      //   471: iload_2
      //   472: baload
      //   473: bipush #13
      //   475: if_icmpne -> 1811
      //   478: aload #21
      //   480: iload_2
      //   481: iconst_1
      //   482: iadd
      //   483: baload
      //   484: bipush #10
      //   486: if_icmpne -> 1811
      //   489: aload #21
      //   491: iload_2
      //   492: iconst_2
      //   493: iadd
      //   494: baload
      //   495: bipush #13
      //   497: if_icmpne -> 1811
      //   500: aload #21
      //   502: iload_2
      //   503: iconst_3
      //   504: iadd
      //   505: baload
      //   506: bipush #10
      //   508: if_icmpne -> 1811
      //   511: iconst_1
      //   512: istore #6
      //   514: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   517: ldc 'Performance Check Point : Header Received'
      //   519: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   522: new java/lang/String
      //   525: dup
      //   526: aload #21
      //   528: iconst_0
      //   529: iload_2
      //   530: invokespecial <init> : ([BII)V
      //   533: astore #22
      //   535: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   538: new java/lang/StringBuilder
      //   541: dup
      //   542: ldc 'header : '
      //   544: invokespecial <init> : (Ljava/lang/String;)V
      //   547: aload #22
      //   549: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   552: invokevirtual toString : ()Ljava/lang/String;
      //   555: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   558: new com/samsungimaging/connectionmanager/app/pushservice/selectivepush/HeaderParser
      //   561: dup
      //   562: aload #22
      //   564: invokespecial <init> : (Ljava/lang/String;)V
      //   567: astore #22
      //   569: iload #6
      //   571: istore_3
      //   572: lload #10
      //   574: lstore #12
      //   576: iload_1
      //   577: istore #4
      //   579: lload #8
      //   581: lstore #14
      //   583: aload #22
      //   585: invokevirtual getHeaderParsed : ()I
      //   588: iconst_2
      //   589: if_icmpeq -> 290
      //   592: aload #22
      //   594: invokevirtual getHeaderParsed : ()I
      //   597: iconst_3
      //   598: if_icmpne -> 791
      //   601: aload_0
      //   602: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   605: iconst_1
      //   606: invokestatic access$36 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;Z)V
      //   609: aload_0
      //   610: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   613: iconst_1
      //   614: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;Z)V
      //   617: iload #6
      //   619: istore_3
      //   620: lload #10
      //   622: lstore #12
      //   624: iload_1
      //   625: istore #4
      //   627: lload #8
      //   629: lstore #14
      //   631: goto -> 290
      //   634: astore #21
      //   636: aload #21
      //   638: invokevirtual printStackTrace : ()V
      //   641: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   644: new java/lang/StringBuilder
      //   647: dup
      //   648: ldc 'DownloadOnSDcard  FileNotFoundException: '
      //   650: invokespecial <init> : (Ljava/lang/String;)V
      //   653: aload #21
      //   655: invokevirtual toString : ()Ljava/lang/String;
      //   658: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   661: invokevirtual toString : ()Ljava/lang/String;
      //   664: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   667: aload_0
      //   668: getfield dos : Ljava/io/DataOutputStream;
      //   671: aload_0
      //   672: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   675: ldc '2500'
      //   677: invokevirtual make_resp : (Ljava/lang/String;)[B
      //   680: invokevirtual write : ([B)V
      //   683: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   686: ldc 'Performance Check Point : File Not Found'
      //   688: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   691: aload_0
      //   692: getfield mReceivingFile : Ljava/io/File;
      //   695: ifnull -> 744
      //   698: aload_0
      //   699: getfield mReceivingFile : Ljava/io/File;
      //   702: invokevirtual exists : ()Z
      //   705: ifeq -> 744
      //   708: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   711: new java/lang/StringBuilder
      //   714: dup
      //   715: ldc 'ImageDownloader Cancel Saving File Delete : '
      //   717: invokespecial <init> : (Ljava/lang/String;)V
      //   720: aload_0
      //   721: getfield mReceivingFile : Ljava/io/File;
      //   724: invokevirtual getName : ()Ljava/lang/String;
      //   727: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   730: invokevirtual toString : ()Ljava/lang/String;
      //   733: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   736: aload_0
      //   737: getfield mReceivingFile : Ljava/io/File;
      //   740: invokevirtual delete : ()Z
      //   743: pop
      //   744: aload_0
      //   745: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   748: getfield mHandler : Landroid/os/Handler;
      //   751: bipush #36
      //   753: invokevirtual sendEmptyMessage : (I)Z
      //   756: pop
      //   757: aload_0
      //   758: getfield socket : Ljava/net/Socket;
      //   761: invokevirtual close : ()V
      //   764: aload_0
      //   765: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   768: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   771: aload_0
      //   772: getfield sock_idx : I
      //   775: iconst_0
      //   776: bastore
      //   777: aload_0
      //   778: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   781: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   784: aload_0
      //   785: getfield sock_idx : I
      //   788: iconst_0
      //   789: bastore
      //   790: return
      //   791: aload_0
      //   792: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   795: invokestatic getDefaultStorage : ()Ljava/lang/String;
      //   798: aload #22
      //   800: invokevirtual getFileName : ()Ljava/lang/String;
      //   803: invokestatic renameFile : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
      //   806: invokestatic access$38 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;Ljava/lang/String;)V
      //   809: aload_0
      //   810: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   813: new java/lang/StringBuilder
      //   816: dup
      //   817: invokestatic getDefaultStorage : ()Ljava/lang/String;
      //   820: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
      //   823: invokespecial <init> : (Ljava/lang/String;)V
      //   826: aload_0
      //   827: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   830: invokestatic access$20 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)Ljava/lang/String;
      //   833: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   836: invokevirtual toString : ()Ljava/lang/String;
      //   839: invokestatic access$38 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;Ljava/lang/String;)V
      //   842: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   845: new java/lang/StringBuilder
      //   848: dup
      //   849: ldc_w 'Create File Path : '
      //   852: invokespecial <init> : (Ljava/lang/String;)V
      //   855: aload_0
      //   856: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   859: invokestatic access$20 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)Ljava/lang/String;
      //   862: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   865: invokevirtual toString : ()Ljava/lang/String;
      //   868: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   871: aload_0
      //   872: new java/io/File
      //   875: dup
      //   876: aload_0
      //   877: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   880: invokestatic access$20 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)Ljava/lang/String;
      //   883: invokespecial <init> : (Ljava/lang/String;)V
      //   886: putfield mReceivingFile : Ljava/io/File;
      //   889: aload_0
      //   890: new java/io/FileOutputStream
      //   893: dup
      //   894: aload_0
      //   895: getfield mReceivingFile : Ljava/io/File;
      //   898: invokespecial <init> : (Ljava/io/File;)V
      //   901: putfield fos : Ljava/io/FileOutputStream;
      //   904: aload #22
      //   906: invokevirtual getContentLength : ()Ljava/lang/String;
      //   909: invokestatic parseLong : (Ljava/lang/String;)J
      //   912: lstore #12
      //   914: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   917: new java/lang/StringBuilder
      //   920: dup
      //   921: ldc_w 'file_size : '
      //   924: invokespecial <init> : (Ljava/lang/String;)V
      //   927: lload #12
      //   929: invokevirtual append : (J)Ljava/lang/StringBuilder;
      //   932: invokevirtual toString : ()Ljava/lang/String;
      //   935: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   938: lload #12
      //   940: ldc2_w 1048576
      //   943: lcmp
      //   944: ifge -> 1427
      //   947: bipush #10
      //   949: invokestatic access$39 : (I)V
      //   952: sipush #200
      //   955: istore_1
      //   956: new java/lang/StringBuffer
      //   959: dup
      //   960: invokespecial <init> : ()V
      //   963: astore #22
      //   965: lload #12
      //   967: invokestatic getAvailableExternalMemorySize : ()J
      //   970: ldc2_w 10485760
      //   973: lsub
      //   974: lcmp
      //   975: ifle -> 1460
      //   978: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   981: ldc_w '##### header check error - not enough memory !!! #####'
      //   984: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   987: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   990: new java/lang/StringBuilder
      //   993: dup
      //   994: ldc_w 'receving size : '
      //   997: invokespecial <init> : (Ljava/lang/String;)V
      //   1000: lload #12
      //   1002: invokevirtual append : (J)Ljava/lang/StringBuilder;
      //   1005: ldc_w '      phone free size : '
      //   1008: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1011: invokestatic getAvailableExternalMemorySize : ()J
      //   1014: invokevirtual append : (J)Ljava/lang/StringBuilder;
      //   1017: invokevirtual toString : ()Ljava/lang/String;
      //   1020: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   1023: aload_0
      //   1024: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1027: iconst_1
      //   1028: invokestatic access$35 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;Z)V
      //   1031: aload_0
      //   1032: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1035: invokestatic access$27 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)Landroid/content/Context;
      //   1038: ldc_w 2131361827
      //   1041: iconst_0
      //   1042: invokestatic makeText : (Landroid/content/Context;II)Landroid/widget/Toast;
      //   1045: invokevirtual show : ()V
      //   1048: aload #22
      //   1050: ldc_w 'HTTP/1.1 500 Internal Server Error'
      //   1053: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
      //   1056: ldc_w '\\r\\n'
      //   1059: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
      //   1062: ldc_w 'Sub-ErrorCode: 2502'
      //   1065: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
      //   1068: ldc_w '\\r\\n\\r\\n'
      //   1071: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
      //   1074: pop
      //   1075: aload_0
      //   1076: getfield dos : Ljava/io/DataOutputStream;
      //   1079: aload #22
      //   1081: invokevirtual toString : ()Ljava/lang/String;
      //   1084: invokevirtual getBytes : ()[B
      //   1087: invokevirtual write : ([B)V
      //   1090: aload_0
      //   1091: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1094: iconst_1
      //   1095: invokestatic access$35 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;Z)V
      //   1098: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   1101: ldc_w 'Performance Check Point : HTTP/1.1 500 Internal Server Error'
      //   1104: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   1107: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   1110: ldc_w 'Sub-ErrorCode: 2502'
      //   1113: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   1116: iload #6
      //   1118: istore_3
      //   1119: iload_1
      //   1120: istore #4
      //   1122: lload #8
      //   1124: lstore #14
      //   1126: goto -> 290
      //   1129: astore #21
      //   1131: aload #21
      //   1133: invokevirtual printStackTrace : ()V
      //   1136: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   1139: new java/lang/StringBuilder
      //   1142: dup
      //   1143: ldc_w 'DownloadOnSDcard  IOException: '
      //   1146: invokespecial <init> : (Ljava/lang/String;)V
      //   1149: aload #21
      //   1151: invokevirtual toString : ()Ljava/lang/String;
      //   1154: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1157: invokevirtual toString : ()Ljava/lang/String;
      //   1160: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   1163: aload_0
      //   1164: getfield fos : Ljava/io/FileOutputStream;
      //   1167: ifnull -> 1177
      //   1170: aload_0
      //   1171: getfield fos : Ljava/io/FileOutputStream;
      //   1174: invokevirtual close : ()V
      //   1177: aload_0
      //   1178: getfield mReceivingFile : Ljava/io/File;
      //   1181: ifnull -> 1230
      //   1184: aload_0
      //   1185: getfield mReceivingFile : Ljava/io/File;
      //   1188: invokevirtual exists : ()Z
      //   1191: ifeq -> 1230
      //   1194: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   1197: new java/lang/StringBuilder
      //   1200: dup
      //   1201: ldc 'ImageDownloader Cancel Saving File Delete : '
      //   1203: invokespecial <init> : (Ljava/lang/String;)V
      //   1206: aload_0
      //   1207: getfield mReceivingFile : Ljava/io/File;
      //   1210: invokevirtual getName : ()Ljava/lang/String;
      //   1213: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1216: invokevirtual toString : ()Ljava/lang/String;
      //   1219: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   1222: aload_0
      //   1223: getfield mReceivingFile : Ljava/io/File;
      //   1226: invokevirtual delete : ()Z
      //   1229: pop
      //   1230: aload_0
      //   1231: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1234: getfield mHandler : Landroid/os/Handler;
      //   1237: bipush #36
      //   1239: invokevirtual sendEmptyMessage : (I)Z
      //   1242: pop
      //   1243: aload_0
      //   1244: getfield socket : Ljava/net/Socket;
      //   1247: invokevirtual close : ()V
      //   1250: aload_0
      //   1251: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1254: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   1257: aload_0
      //   1258: getfield sock_idx : I
      //   1261: iconst_0
      //   1262: bastore
      //   1263: goto -> 777
      //   1266: astore #21
      //   1268: aload #21
      //   1270: invokevirtual printStackTrace : ()V
      //   1273: aload_0
      //   1274: getfield fos : Ljava/io/FileOutputStream;
      //   1277: ifnull -> 1287
      //   1280: aload_0
      //   1281: getfield fos : Ljava/io/FileOutputStream;
      //   1284: invokevirtual close : ()V
      //   1287: aload_0
      //   1288: getfield mReceivingFile : Ljava/io/File;
      //   1291: ifnull -> 1340
      //   1294: aload_0
      //   1295: getfield mReceivingFile : Ljava/io/File;
      //   1298: invokevirtual exists : ()Z
      //   1301: ifeq -> 1340
      //   1304: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   1307: new java/lang/StringBuilder
      //   1310: dup
      //   1311: ldc 'ImageDownloader Cancel Saving File Delete : '
      //   1313: invokespecial <init> : (Ljava/lang/String;)V
      //   1316: aload_0
      //   1317: getfield mReceivingFile : Ljava/io/File;
      //   1320: invokevirtual getName : ()Ljava/lang/String;
      //   1323: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1326: invokevirtual toString : ()Ljava/lang/String;
      //   1329: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   1332: aload_0
      //   1333: getfield mReceivingFile : Ljava/io/File;
      //   1336: invokevirtual delete : ()Z
      //   1339: pop
      //   1340: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   1343: new java/lang/StringBuilder
      //   1346: dup
      //   1347: ldc_w 'ImageDownloader  Exception: '
      //   1350: invokespecial <init> : (Ljava/lang/String;)V
      //   1353: aload #21
      //   1355: invokevirtual toString : ()Ljava/lang/String;
      //   1358: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1361: invokevirtual toString : ()Ljava/lang/String;
      //   1364: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   1367: aload_0
      //   1368: getfield dos : Ljava/io/DataOutputStream;
      //   1371: aload_0
      //   1372: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1375: ldc_w '2400'
      //   1378: invokevirtual make_resp : (Ljava/lang/String;)[B
      //   1381: invokevirtual write : ([B)V
      //   1384: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   1387: ldc_w 'Performance Check Point : Socket Create Fail'
      //   1390: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   1393: aload_0
      //   1394: getfield socket : Ljava/net/Socket;
      //   1397: invokevirtual close : ()V
      //   1400: aload_0
      //   1401: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1404: getfield mHandler : Landroid/os/Handler;
      //   1407: bipush #36
      //   1409: invokevirtual sendEmptyMessage : (I)Z
      //   1412: pop
      //   1413: aload_0
      //   1414: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1417: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   1420: aload_0
      //   1421: getfield sock_idx : I
      //   1424: iconst_0
      //   1425: bastore
      //   1426: return
      //   1427: lload #12
      //   1429: ldc2_w 5242880
      //   1432: lcmp
      //   1433: ifge -> 1448
      //   1436: bipush #30
      //   1438: invokestatic access$39 : (I)V
      //   1441: sipush #1024
      //   1444: istore_1
      //   1445: goto -> 956
      //   1448: bipush #50
      //   1450: invokestatic access$39 : (I)V
      //   1453: aload #21
      //   1455: arraylength
      //   1456: istore_1
      //   1457: goto -> 956
      //   1460: aload #22
      //   1462: ldc_w 'HTTP/1.1 100 Continue'
      //   1465: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
      //   1468: ldc_w '\\r\\n\\r\\n'
      //   1471: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
      //   1474: pop
      //   1475: aload_0
      //   1476: getfield dos : Ljava/io/DataOutputStream;
      //   1479: aload #22
      //   1481: invokevirtual toString : ()Ljava/lang/String;
      //   1484: invokevirtual getBytes : ()[B
      //   1487: invokevirtual write : ([B)V
      //   1490: aload_0
      //   1491: getfield dos : Ljava/io/DataOutputStream;
      //   1494: invokevirtual flush : ()V
      //   1497: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   1500: ldc_w 'Performance Check Point : HTTP/1.1 100 Continue'
      //   1503: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   1506: aload_0
      //   1507: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1510: getfield mHandler : Landroid/os/Handler;
      //   1513: aload_0
      //   1514: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1517: getfield mHandler : Landroid/os/Handler;
      //   1520: bipush #63
      //   1522: lload #12
      //   1524: ldc2_w 1000
      //   1527: ldiv
      //   1528: l2i
      //   1529: iconst_0
      //   1530: invokevirtual obtainMessage : (III)Landroid/os/Message;
      //   1533: invokevirtual sendMessage : (Landroid/os/Message;)Z
      //   1536: pop
      //   1537: aload_0
      //   1538: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1541: getfield mHandler : Landroid/os/Handler;
      //   1544: aload_0
      //   1545: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1548: getfield mHandler : Landroid/os/Handler;
      //   1551: bipush #62
      //   1553: lload #12
      //   1555: ldc2_w 1000
      //   1558: ldiv
      //   1559: l2i
      //   1560: iconst_0
      //   1561: invokevirtual obtainMessage : (III)Landroid/os/Message;
      //   1564: invokevirtual sendMessage : (Landroid/os/Message;)Z
      //   1567: pop
      //   1568: ldc2_w 300
      //   1571: invokestatic sleep : (J)V
      //   1574: aload_0
      //   1575: getfield fos : Ljava/io/FileOutputStream;
      //   1578: aload #21
      //   1580: iload_2
      //   1581: iconst_4
      //   1582: iadd
      //   1583: iload #7
      //   1585: iload_2
      //   1586: isub
      //   1587: iconst_4
      //   1588: isub
      //   1589: invokevirtual write : ([BII)V
      //   1592: iload #7
      //   1594: iload_2
      //   1595: isub
      //   1596: iconst_4
      //   1597: isub
      //   1598: i2l
      //   1599: lstore #14
      //   1601: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   1604: new java/lang/StringBuilder
      //   1607: dup
      //   1608: ldc_w 'file size first : '
      //   1611: invokespecial <init> : (Ljava/lang/String;)V
      //   1614: lload #14
      //   1616: invokevirtual append : (J)Ljava/lang/StringBuilder;
      //   1619: invokevirtual toString : ()Ljava/lang/String;
      //   1622: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   1625: aload_0
      //   1626: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1629: getfield mHandler : Landroid/os/Handler;
      //   1632: aload_0
      //   1633: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1636: getfield mHandler : Landroid/os/Handler;
      //   1639: bipush #62
      //   1641: lload #12
      //   1643: ldc2_w 1000
      //   1646: ldiv
      //   1647: l2i
      //   1648: lload #14
      //   1650: ldc2_w 1000
      //   1653: ldiv
      //   1654: l2i
      //   1655: invokevirtual obtainMessage : (III)Landroid/os/Message;
      //   1658: invokevirtual sendMessage : (Landroid/os/Message;)Z
      //   1661: pop
      //   1662: iload #6
      //   1664: istore_3
      //   1665: iload_1
      //   1666: istore #4
      //   1668: goto -> 290
      //   1671: astore #21
      //   1673: aload #21
      //   1675: invokevirtual printStackTrace : ()V
      //   1678: aload_0
      //   1679: getfield dos : Ljava/io/DataOutputStream;
      //   1682: aload_0
      //   1683: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1686: ldc_w '2999'
      //   1689: invokevirtual make_resp : (Ljava/lang/String;)[B
      //   1692: invokevirtual write : ([B)V
      //   1695: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   1698: ldc_w 'Performance Check Point : Unknown Exception'
      //   1701: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   1704: aload_0
      //   1705: getfield mReceivingFile : Ljava/io/File;
      //   1708: ifnull -> 1757
      //   1711: aload_0
      //   1712: getfield mReceivingFile : Ljava/io/File;
      //   1715: invokevirtual exists : ()Z
      //   1718: ifeq -> 1757
      //   1721: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   1724: new java/lang/StringBuilder
      //   1727: dup
      //   1728: ldc 'ImageDownloader Cancel Saving File Delete : '
      //   1730: invokespecial <init> : (Ljava/lang/String;)V
      //   1733: aload_0
      //   1734: getfield mReceivingFile : Ljava/io/File;
      //   1737: invokevirtual getName : ()Ljava/lang/String;
      //   1740: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   1743: invokevirtual toString : ()Ljava/lang/String;
      //   1746: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   1749: aload_0
      //   1750: getfield mReceivingFile : Ljava/io/File;
      //   1753: invokevirtual delete : ()Z
      //   1756: pop
      //   1757: aload_0
      //   1758: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1761: getfield mHandler : Landroid/os/Handler;
      //   1764: bipush #36
      //   1766: invokevirtual sendEmptyMessage : (I)Z
      //   1769: pop
      //   1770: aload_0
      //   1771: getfield socket : Ljava/net/Socket;
      //   1774: invokevirtual close : ()V
      //   1777: aload_0
      //   1778: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1781: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   1784: aload_0
      //   1785: getfield sock_idx : I
      //   1788: iconst_0
      //   1789: bastore
      //   1790: goto -> 777
      //   1793: astore #21
      //   1795: aload_0
      //   1796: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1799: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   1802: aload_0
      //   1803: getfield sock_idx : I
      //   1806: iconst_0
      //   1807: bastore
      //   1808: aload #21
      //   1810: athrow
      //   1811: iload_2
      //   1812: iconst_1
      //   1813: iadd
      //   1814: istore_2
      //   1815: goto -> 271
      //   1818: aload #21
      //   1820: ldc_w 'Bye'
      //   1823: invokevirtual contains : (Ljava/lang/CharSequence;)Z
      //   1826: ifne -> 1840
      //   1829: aload #21
      //   1831: ldc_w 'bye'
      //   1834: invokevirtual contains : (Ljava/lang/CharSequence;)Z
      //   1837: ifeq -> 1903
      //   1840: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   1843: ldc_w 'ImageDownloader Header Parsing ByeBye, send message App close '
      //   1846: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   1849: aload_0
      //   1850: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1853: getfield mHandler : Landroid/os/Handler;
      //   1856: bipush #24
      //   1858: invokevirtual sendEmptyMessage : (I)Z
      //   1861: pop
      //   1862: aload_0
      //   1863: getfield socket : Ljava/net/Socket;
      //   1866: invokevirtual close : ()V
      //   1869: aload_0
      //   1870: getfield socket : Ljava/net/Socket;
      //   1873: invokevirtual close : ()V
      //   1876: aload_0
      //   1877: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1880: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   1883: aload_0
      //   1884: getfield sock_idx : I
      //   1887: iconst_0
      //   1888: bastore
      //   1889: aload_0
      //   1890: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1893: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   1896: aload_0
      //   1897: getfield sock_idx : I
      //   1900: iconst_0
      //   1901: bastore
      //   1902: return
      //   1903: aload_0
      //   1904: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1907: iconst_1
      //   1908: invokestatic access$34 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;Z)V
      //   1911: goto -> 198
      //   1914: astore #21
      //   1916: aload_0
      //   1917: getfield socket : Ljava/net/Socket;
      //   1920: invokevirtual close : ()V
      //   1923: aload_0
      //   1924: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1927: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   1930: aload_0
      //   1931: getfield sock_idx : I
      //   1934: iconst_0
      //   1935: bastore
      //   1936: aload #21
      //   1938: athrow
      //   1939: aload_0
      //   1940: getfield fos : Ljava/io/FileOutputStream;
      //   1943: aload #21
      //   1945: iconst_0
      //   1946: iload #7
      //   1948: invokevirtual write : ([BII)V
      //   1951: lload #8
      //   1953: iload #7
      //   1955: i2l
      //   1956: ladd
      //   1957: lstore #16
      //   1959: aload_0
      //   1960: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1963: ldc2_w 100
      //   1966: lload #16
      //   1968: lmul
      //   1969: lload #10
      //   1971: ldiv
      //   1972: l2i
      //   1973: invokestatic access$40 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;I)V
      //   1976: iload #5
      //   1978: invokestatic access$41 : ()I
      //   1981: irem
      //   1982: ifne -> 2833
      //   1985: aload_0
      //   1986: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   1989: getfield mHandler : Landroid/os/Handler;
      //   1992: bipush #62
      //   1994: lload #10
      //   1996: ldc2_w 1000
      //   1999: ldiv
      //   2000: l2i
      //   2001: lload #16
      //   2003: ldc2_w 1000
      //   2006: ldiv
      //   2007: l2i
      //   2008: invokevirtual obtainMessage : (III)Landroid/os/Message;
      //   2011: astore #22
      //   2013: aload_0
      //   2014: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2017: getfield mHandler : Landroid/os/Handler;
      //   2020: aload #22
      //   2022: invokevirtual sendMessage : (Landroid/os/Message;)Z
      //   2025: pop
      //   2026: goto -> 2833
      //   2029: aload_0
      //   2030: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2033: invokestatic access$42 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)Z
      //   2036: ifeq -> 2178
      //   2039: new java/lang/StringBuffer
      //   2042: dup
      //   2043: invokespecial <init> : ()V
      //   2046: astore #21
      //   2048: aload #21
      //   2050: ldc_w 'HTTP/1.1 200 OK'
      //   2053: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
      //   2056: ldc_w '\\r\\n'
      //   2059: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
      //   2062: ldc_w 'Content-Length: 0'
      //   2065: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
      //   2068: ldc_w '\\r\\n'
      //   2071: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
      //   2074: ldc_w 'Sub-ErrorCode: 0'
      //   2077: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
      //   2080: ldc_w '\\r\\n\\r\\n'
      //   2083: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuffer;
      //   2086: pop
      //   2087: aload_0
      //   2088: getfield dos : Ljava/io/DataOutputStream;
      //   2091: aload #21
      //   2093: invokevirtual toString : ()Ljava/lang/String;
      //   2096: invokevirtual getBytes : ()[B
      //   2099: invokevirtual write : ([B)V
      //   2102: aload_0
      //   2103: getfield dos : Ljava/io/DataOutputStream;
      //   2106: invokevirtual flush : ()V
      //   2109: aload_0
      //   2110: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2113: getfield mHandler : Landroid/os/Handler;
      //   2116: bipush #24
      //   2118: invokevirtual sendEmptyMessage : (I)Z
      //   2121: pop
      //   2122: lload #18
      //   2124: lstore #12
      //   2126: lload #16
      //   2128: lstore #14
      //   2130: goto -> 198
      //   2133: astore #21
      //   2135: aload #21
      //   2137: invokevirtual printStackTrace : ()V
      //   2140: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   2143: new java/lang/StringBuilder
      //   2146: dup
      //   2147: ldc_w 'sNp command = Getout  IOException: '
      //   2150: invokespecial <init> : (Ljava/lang/String;)V
      //   2153: aload #21
      //   2155: invokevirtual toString : ()Ljava/lang/String;
      //   2158: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   2161: invokevirtual toString : ()Ljava/lang/String;
      //   2164: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   2167: lload #18
      //   2169: lstore #12
      //   2171: lload #16
      //   2173: lstore #14
      //   2175: goto -> 198
      //   2178: iload #6
      //   2180: istore_3
      //   2181: lload #18
      //   2183: lstore #10
      //   2185: iload #7
      //   2187: istore_2
      //   2188: lload #16
      //   2190: lstore #8
      //   2192: lload #16
      //   2194: lload #18
      //   2196: lcmp
      //   2197: iflt -> 153
      //   2200: aload_0
      //   2201: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2204: getfield mHandler : Landroid/os/Handler;
      //   2207: aload_0
      //   2208: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2211: getfield mHandler : Landroid/os/Handler;
      //   2214: bipush #62
      //   2216: lload #18
      //   2218: ldc2_w 1000
      //   2221: ldiv
      //   2222: l2i
      //   2223: lload #16
      //   2225: ldc2_w 1000
      //   2228: ldiv
      //   2229: l2i
      //   2230: invokevirtual obtainMessage : (III)Landroid/os/Message;
      //   2233: invokevirtual sendMessage : (Landroid/os/Message;)Z
      //   2236: pop
      //   2237: ldc2_w 200
      //   2240: invokestatic sleep : (J)V
      //   2243: aload_0
      //   2244: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2247: getfield mHandler : Landroid/os/Handler;
      //   2250: aload_0
      //   2251: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2254: getfield mHandler : Landroid/os/Handler;
      //   2257: bipush #64
      //   2259: lload #18
      //   2261: ldc2_w 1000
      //   2264: ldiv
      //   2265: l2i
      //   2266: lload #16
      //   2268: ldc2_w 1000
      //   2271: ldiv
      //   2272: l2i
      //   2273: invokevirtual obtainMessage : (III)Landroid/os/Message;
      //   2276: invokevirtual sendMessage : (Landroid/os/Message;)Z
      //   2279: pop
      //   2280: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   2283: ldc_w 'Performance Check Point : file receive complete !!!'
      //   2286: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   2289: aload_0
      //   2290: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2293: astore #21
      //   2295: aload #21
      //   2297: aload #21
      //   2299: invokestatic access$43 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)I
      //   2302: iconst_1
      //   2303: iadd
      //   2304: invokestatic access$44 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;I)V
      //   2307: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   2310: new java/lang/StringBuilder
      //   2313: dup
      //   2314: ldc_w 'image file save num : '
      //   2317: invokespecial <init> : (Ljava/lang/String;)V
      //   2320: aload_0
      //   2321: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2324: invokestatic access$43 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)I
      //   2327: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   2330: invokevirtual toString : ()Ljava/lang/String;
      //   2333: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   2336: aload_0
      //   2337: getfield dos : Ljava/io/DataOutputStream;
      //   2340: aload_0
      //   2341: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2344: ldc_w '0'
      //   2347: invokevirtual make_resp : (Ljava/lang/String;)[B
      //   2350: invokevirtual write : ([B)V
      //   2353: lload #18
      //   2355: lstore #12
      //   2357: lload #16
      //   2359: lstore #14
      //   2361: goto -> 198
      //   2364: aload_0
      //   2365: getfield socket : Ljava/net/Socket;
      //   2368: invokevirtual close : ()V
      //   2371: goto -> 233
      //   2374: aload_0
      //   2375: getfield fos : Ljava/io/FileOutputStream;
      //   2378: invokevirtual close : ()V
      //   2381: lload #14
      //   2383: lload #12
      //   2385: lcmp
      //   2386: ifge -> 2397
      //   2389: aload_0
      //   2390: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2393: iconst_1
      //   2394: invokestatic access$35 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;Z)V
      //   2397: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   2400: new java/lang/StringBuilder
      //   2403: dup
      //   2404: ldc_w 'rcv_on['
      //   2407: invokespecial <init> : (Ljava/lang/String;)V
      //   2410: aload_0
      //   2411: getfield sock_idx : I
      //   2414: invokevirtual append : (I)Ljava/lang/StringBuilder;
      //   2417: ldc_w '] is false'
      //   2420: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   2423: invokevirtual toString : ()Ljava/lang/String;
      //   2426: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   2429: aload_0
      //   2430: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2433: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   2436: aload_0
      //   2437: getfield sock_idx : I
      //   2440: iconst_0
      //   2441: bastore
      //   2442: aload_0
      //   2443: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2446: invokestatic access$45 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)Z
      //   2449: ifeq -> 2585
      //   2452: aload_0
      //   2453: getfield dos : Ljava/io/DataOutputStream;
      //   2456: aload_0
      //   2457: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2460: ldc_w '2100'
      //   2463: invokevirtual make_resp : (Ljava/lang/String;)[B
      //   2466: invokevirtual write : ([B)V
      //   2469: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   2472: ldc_w 'Performance Check Point : Header Error'
      //   2475: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   2478: aload_0
      //   2479: getfield socket : Ljava/net/Socket;
      //   2482: invokevirtual close : ()V
      //   2485: aload_0
      //   2486: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2489: getfield mHandler : Landroid/os/Handler;
      //   2492: bipush #36
      //   2494: invokevirtual sendEmptyMessage : (I)Z
      //   2497: pop
      //   2498: aload_0
      //   2499: getfield mReceivingFile : Ljava/io/File;
      //   2502: ifnull -> 2551
      //   2505: aload_0
      //   2506: getfield mReceivingFile : Ljava/io/File;
      //   2509: invokevirtual exists : ()Z
      //   2512: ifeq -> 2551
      //   2515: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   2518: new java/lang/StringBuilder
      //   2521: dup
      //   2522: ldc 'ImageDownloader Cancel Saving File Delete : '
      //   2524: invokespecial <init> : (Ljava/lang/String;)V
      //   2527: aload_0
      //   2528: getfield mReceivingFile : Ljava/io/File;
      //   2531: invokevirtual getName : ()Ljava/lang/String;
      //   2534: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   2537: invokevirtual toString : ()Ljava/lang/String;
      //   2540: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   2543: aload_0
      //   2544: getfield mReceivingFile : Ljava/io/File;
      //   2547: invokevirtual delete : ()Z
      //   2550: pop
      //   2551: aload_0
      //   2552: getfield socket : Ljava/net/Socket;
      //   2555: invokevirtual close : ()V
      //   2558: aload_0
      //   2559: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2562: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   2565: aload_0
      //   2566: getfield sock_idx : I
      //   2569: iconst_0
      //   2570: bastore
      //   2571: aload_0
      //   2572: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2575: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   2578: aload_0
      //   2579: getfield sock_idx : I
      //   2582: iconst_0
      //   2583: bastore
      //   2584: return
      //   2585: aload_0
      //   2586: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2589: invokestatic access$46 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)Z
      //   2592: ifeq -> 2728
      //   2595: aload_0
      //   2596: getfield dos : Ljava/io/DataOutputStream;
      //   2599: aload_0
      //   2600: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2603: ldc_w '2502'
      //   2606: invokevirtual make_resp : (Ljava/lang/String;)[B
      //   2609: invokevirtual write : ([B)V
      //   2612: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   2615: ldc_w 'Performance Check Point : File Save Error'
      //   2618: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   2621: aload_0
      //   2622: getfield socket : Ljava/net/Socket;
      //   2625: invokevirtual close : ()V
      //   2628: aload_0
      //   2629: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2632: getfield mHandler : Landroid/os/Handler;
      //   2635: bipush #36
      //   2637: invokevirtual sendEmptyMessage : (I)Z
      //   2640: pop
      //   2641: aload_0
      //   2642: getfield mReceivingFile : Ljava/io/File;
      //   2645: ifnull -> 2694
      //   2648: aload_0
      //   2649: getfield mReceivingFile : Ljava/io/File;
      //   2652: invokevirtual exists : ()Z
      //   2655: ifeq -> 2694
      //   2658: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   2661: new java/lang/StringBuilder
      //   2664: dup
      //   2665: ldc 'ImageDownloader Cancel Saving File Delete : '
      //   2667: invokespecial <init> : (Ljava/lang/String;)V
      //   2670: aload_0
      //   2671: getfield mReceivingFile : Ljava/io/File;
      //   2674: invokevirtual getName : ()Ljava/lang/String;
      //   2677: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   2680: invokevirtual toString : ()Ljava/lang/String;
      //   2683: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   2686: aload_0
      //   2687: getfield mReceivingFile : Ljava/io/File;
      //   2690: invokevirtual delete : ()Z
      //   2693: pop
      //   2694: aload_0
      //   2695: getfield socket : Ljava/net/Socket;
      //   2698: invokevirtual close : ()V
      //   2701: aload_0
      //   2702: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2705: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   2708: aload_0
      //   2709: getfield sock_idx : I
      //   2712: iconst_0
      //   2713: bastore
      //   2714: aload_0
      //   2715: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2718: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   2721: aload_0
      //   2722: getfield sock_idx : I
      //   2725: iconst_0
      //   2726: bastore
      //   2727: return
      //   2728: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   2731: ldc_w 'receive complete!'
      //   2734: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   2737: aload_0
      //   2738: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2741: aload_0
      //   2742: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2745: invokestatic access$20 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)Ljava/lang/String;
      //   2748: invokevirtual updateMediaContent : (Ljava/lang/String;)V
      //   2751: aload_0
      //   2752: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2755: getfield mHandler : Landroid/os/Handler;
      //   2758: bipush #51
      //   2760: invokevirtual sendEmptyMessage : (I)Z
      //   2763: pop
      //   2764: aload_0
      //   2765: getfield socket : Ljava/net/Socket;
      //   2768: invokevirtual close : ()V
      //   2771: invokestatic access$0 : ()Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   2774: ldc_w 'file write complete'
      //   2777: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   2780: aload_0
      //   2781: getfield socket : Ljava/net/Socket;
      //   2784: invokevirtual close : ()V
      //   2787: aload_0
      //   2788: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;
      //   2791: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pushservice/selectivepush/SelectivePush;)[Z
      //   2794: aload_0
      //   2795: getfield sock_idx : I
      //   2798: iconst_0
      //   2799: bastore
      //   2800: goto -> 777
      //   2803: astore #21
      //   2805: aload #21
      //   2807: invokevirtual printStackTrace : ()V
      //   2810: goto -> 1177
      //   2813: astore #22
      //   2815: aload #22
      //   2817: invokevirtual printStackTrace : ()V
      //   2820: goto -> 1287
      //   2823: astore #21
      //   2825: aload #21
      //   2827: invokevirtual printStackTrace : ()V
      //   2830: goto -> 1400
      //   2833: iload #5
      //   2835: iconst_1
      //   2836: iadd
      //   2837: istore #5
      //   2839: iload_3
      //   2840: istore #6
      //   2842: lload #10
      //   2844: lstore #18
      //   2846: iload_2
      //   2847: istore #7
      //   2849: goto -> 2029
      // Exception table:
      //   from	to	target	type
      //   7	13	1266	java/lang/Exception
      //   7	13	1793	finally
      //   15	52	1266	java/lang/Exception
      //   15	52	1793	finally
      //   71	101	634	java/io/FileNotFoundException
      //   71	101	1129	java/io/IOException
      //   71	101	1671	java/lang/Exception
      //   71	101	1914	finally
      //   109	153	634	java/io/FileNotFoundException
      //   109	153	1129	java/io/IOException
      //   109	153	1671	java/lang/Exception
      //   109	153	1914	finally
      //   153	166	634	java/io/FileNotFoundException
      //   153	166	1129	java/io/IOException
      //   153	166	1671	java/lang/Exception
      //   153	166	1914	finally
      //   180	190	634	java/io/FileNotFoundException
      //   180	190	1129	java/io/IOException
      //   180	190	1671	java/lang/Exception
      //   180	190	1914	finally
      //   198	233	634	java/io/FileNotFoundException
      //   198	233	1129	java/io/IOException
      //   198	233	1671	java/lang/Exception
      //   198	233	1914	finally
      //   233	253	1266	java/lang/Exception
      //   233	253	1793	finally
      //   311	391	634	java/io/FileNotFoundException
      //   311	391	1129	java/io/IOException
      //   311	391	1671	java/lang/Exception
      //   311	391	1914	finally
      //   391	435	634	java/io/FileNotFoundException
      //   391	435	1129	java/io/IOException
      //   391	435	1671	java/lang/Exception
      //   391	435	1914	finally
      //   435	455	1266	java/lang/Exception
      //   435	455	1793	finally
      //   514	569	634	java/io/FileNotFoundException
      //   514	569	1129	java/io/IOException
      //   514	569	1671	java/lang/Exception
      //   514	569	1914	finally
      //   583	617	634	java/io/FileNotFoundException
      //   583	617	1129	java/io/IOException
      //   583	617	1671	java/lang/Exception
      //   583	617	1914	finally
      //   636	744	1914	finally
      //   744	757	1914	finally
      //   757	777	1266	java/lang/Exception
      //   757	777	1793	finally
      //   791	938	634	java/io/FileNotFoundException
      //   791	938	1129	java/io/IOException
      //   791	938	1671	java/lang/Exception
      //   791	938	1914	finally
      //   947	952	634	java/io/FileNotFoundException
      //   947	952	1129	java/io/IOException
      //   947	952	1671	java/lang/Exception
      //   947	952	1914	finally
      //   956	1116	634	java/io/FileNotFoundException
      //   956	1116	1129	java/io/IOException
      //   956	1116	1671	java/lang/Exception
      //   956	1116	1914	finally
      //   1131	1163	1914	finally
      //   1163	1177	2803	java/io/IOException
      //   1163	1177	1914	finally
      //   1177	1230	1914	finally
      //   1230	1243	1914	finally
      //   1243	1263	1266	java/lang/Exception
      //   1243	1263	1793	finally
      //   1268	1273	1793	finally
      //   1273	1287	2813	java/io/IOException
      //   1273	1287	1793	finally
      //   1287	1340	1793	finally
      //   1340	1367	1793	finally
      //   1367	1400	2823	java/io/IOException
      //   1367	1400	1793	finally
      //   1400	1413	1793	finally
      //   1436	1441	634	java/io/FileNotFoundException
      //   1436	1441	1129	java/io/IOException
      //   1436	1441	1671	java/lang/Exception
      //   1436	1441	1914	finally
      //   1448	1457	634	java/io/FileNotFoundException
      //   1448	1457	1129	java/io/IOException
      //   1448	1457	1671	java/lang/Exception
      //   1448	1457	1914	finally
      //   1460	1592	634	java/io/FileNotFoundException
      //   1460	1592	1129	java/io/IOException
      //   1460	1592	1671	java/lang/Exception
      //   1460	1592	1914	finally
      //   1601	1662	634	java/io/FileNotFoundException
      //   1601	1662	1129	java/io/IOException
      //   1601	1662	1671	java/lang/Exception
      //   1601	1662	1914	finally
      //   1673	1757	1914	finally
      //   1757	1770	1914	finally
      //   1770	1790	1266	java/lang/Exception
      //   1770	1790	1793	finally
      //   1818	1840	634	java/io/FileNotFoundException
      //   1818	1840	1129	java/io/IOException
      //   1818	1840	1671	java/lang/Exception
      //   1818	1840	1914	finally
      //   1840	1869	634	java/io/FileNotFoundException
      //   1840	1869	1129	java/io/IOException
      //   1840	1869	1671	java/lang/Exception
      //   1840	1869	1914	finally
      //   1869	1889	1266	java/lang/Exception
      //   1869	1889	1793	finally
      //   1903	1911	634	java/io/FileNotFoundException
      //   1903	1911	1129	java/io/IOException
      //   1903	1911	1671	java/lang/Exception
      //   1903	1911	1914	finally
      //   1916	1939	1266	java/lang/Exception
      //   1916	1939	1793	finally
      //   1939	1951	634	java/io/FileNotFoundException
      //   1939	1951	1129	java/io/IOException
      //   1939	1951	1671	java/lang/Exception
      //   1939	1951	1914	finally
      //   1959	2026	634	java/io/FileNotFoundException
      //   1959	2026	1129	java/io/IOException
      //   1959	2026	1671	java/lang/Exception
      //   1959	2026	1914	finally
      //   2029	2048	634	java/io/FileNotFoundException
      //   2029	2048	1129	java/io/IOException
      //   2029	2048	1671	java/lang/Exception
      //   2029	2048	1914	finally
      //   2048	2122	2133	java/io/IOException
      //   2048	2122	634	java/io/FileNotFoundException
      //   2048	2122	1671	java/lang/Exception
      //   2048	2122	1914	finally
      //   2135	2167	634	java/io/FileNotFoundException
      //   2135	2167	1129	java/io/IOException
      //   2135	2167	1671	java/lang/Exception
      //   2135	2167	1914	finally
      //   2200	2353	634	java/io/FileNotFoundException
      //   2200	2353	1129	java/io/IOException
      //   2200	2353	1671	java/lang/Exception
      //   2200	2353	1914	finally
      //   2364	2371	634	java/io/FileNotFoundException
      //   2364	2371	1129	java/io/IOException
      //   2364	2371	1671	java/lang/Exception
      //   2364	2371	1914	finally
      //   2374	2381	634	java/io/FileNotFoundException
      //   2374	2381	1129	java/io/IOException
      //   2374	2381	1671	java/lang/Exception
      //   2374	2381	1914	finally
      //   2389	2397	634	java/io/FileNotFoundException
      //   2389	2397	1129	java/io/IOException
      //   2389	2397	1671	java/lang/Exception
      //   2389	2397	1914	finally
      //   2397	2551	634	java/io/FileNotFoundException
      //   2397	2551	1129	java/io/IOException
      //   2397	2551	1671	java/lang/Exception
      //   2397	2551	1914	finally
      //   2551	2571	1266	java/lang/Exception
      //   2551	2571	1793	finally
      //   2585	2694	634	java/io/FileNotFoundException
      //   2585	2694	1129	java/io/IOException
      //   2585	2694	1671	java/lang/Exception
      //   2585	2694	1914	finally
      //   2694	2714	1266	java/lang/Exception
      //   2694	2714	1793	finally
      //   2728	2780	634	java/io/FileNotFoundException
      //   2728	2780	1129	java/io/IOException
      //   2728	2780	1671	java/lang/Exception
      //   2728	2780	1914	finally
      //   2780	2800	1266	java/lang/Exception
      //   2780	2800	1793	finally
      //   2805	2810	1914	finally
      //   2815	2820	1793	finally
      //   2825	2830	1793	finally
    }
    
    private void startTimer() {
      Trace.d(SelectivePush.TAG, "startTimer");
      this.mTimer = new TimerTask() {
          public void run() {
            Trace.d(SelectivePush.TAG, "Blocking....");
            (SelectivePush.ImageDownloader.access$1(SelectivePush.ImageDownloader.this)).rcv_on[SelectivePush.ImageDownloader.this.sock_idx] = false;
            try {
              if (SelectivePush.ImageDownloader.this.dis != null) {
                SelectivePush.ImageDownloader.this.dis.close();
                SelectivePush.ImageDownloader.this.dis = null;
              } 
              if (SelectivePush.ImageDownloader.this.dos != null) {
                SelectivePush.ImageDownloader.this.dos.close();
                SelectivePush.ImageDownloader.this.dos = null;
              } 
              return;
            } catch (IOException iOException) {
              Trace.d(SelectivePush.TAG, "IOException 03");
              iOException.printStackTrace();
              return;
            } 
          }
        };
      this.timer.schedule(this.mTimer, 1000L);
    }
    
    private void stopTimer() {
      Trace.d(SelectivePush.TAG, "stopTimer");
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
      Trace.d(SelectivePush.TAG, "Blocking....");
      (SelectivePush.ImageDownloader.access$1(this.this$1)).rcv_on[this.this$1.sock_idx] = false;
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
        Trace.d(SelectivePush.TAG, "IOException 03");
        iOException.printStackTrace();
        return;
      } 
    }
  }
  
  private class InitTask extends AsyncTask<WifiInfo, Void, Boolean> {
    private InitTask() {}
    
    protected Boolean doInBackground(WifiInfo... param1VarArgs) {
      Trace.d(SelectivePush.TAG, "InitTask doInBackground()");
      int i = 8100;
      while (true) {
        if (i < 8104)
          try {
            int j = SelectivePush.this.SSC_Init(i);
            if (j == 0) {
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
      Trace.d(SelectivePush.TAG, "InitTask onPostExecute()");
      if (SelectivePush.this.mCameraPort == -1) {
        SelectivePush.this.mHandler.sendEmptyMessage(73);
      } else if (SelectivePush.this.mDialogCMWaitConnect != null) {
        SelectivePush.this.mDialogCMWaitConnect.dismiss();
        SelectivePush.this.mDialogCMWaitConnect = null;
      } 
      super.onPostExecute(param1Boolean);
    }
    
    protected void onPreExecute() {
      Trace.d(SelectivePush.TAG, "InitTask onPreExecute()");
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
      if (SelectivePush.this.svSock != null)
        try {
          SelectivePush.this.svSock.close();
          Trace.d(SelectivePush.TAG, "Server Socket close");
          return;
        } catch (Exception exception) {
          exception.printStackTrace();
          return;
        }  
    }
    
    public void run() {
      try {
        Trace.i(SelectivePush.TAG, "SSC RcvThread Run...");
        SelectivePush.this.svSock = new ServerSocket(18100, 0, SelectivePush.this.serverAddr);
        Trace.i(SelectivePush.TAG, "SSC RcvThread Server Socket Created.");
        SelectivePush.this.socket_idx = 0;
        while (true) {
          if (SelectivePush.this.bAcceptRun) {
            SelectivePush.this.socket_idx = SelectivePush.this.getAvailSock();
            if (SelectivePush.this.socket_idx != -1) {
              Trace.i(SelectivePush.TAG, "SSC RcvThread Server Socket Waiting...");
              Socket socket = SelectivePush.this.svSock.accept();
              Trace.i(SelectivePush.TAG, "SSC RcvThread Server Socket Acept.");
              Trace.i(SelectivePush.TAG, "socket_idx1: " + SelectivePush.this.socket_idx);
              if (isInterrupted()) {
                Trace.d(SelectivePush.TAG, "RcvThread interrupted");
              } else {
                SelectivePush.this.rcv_on[SelectivePush.this.socket_idx] = true;
                SelectivePush.this.svSockReceiveBufferSize = SelectivePush.this.svSock.getReceiveBufferSize();
                Trace.d(SelectivePush.TAG, "svSock.getReceiveBufferSize() : " + SelectivePush.this.svSock.getReceiveBufferSize());
                if (SelectivePush.this.mWakeLock != null)
                  SelectivePush.this.mWakeLock.acquire(); 
                SelectivePush.ImageDownloader imageDownloader = new SelectivePush.ImageDownloader(socket, SelectivePush.this.socket_idx);
                SelectivePush.this.waitPreviousDownloading();
                imageDownloader.start();
                if (SelectivePush.this.mWakeLock != null && SelectivePush.this.mWakeLock.isHeld())
                  SelectivePush.this.mWakeLock.release(); 
                continue;
              } 
            } else {
              Trace.d(SelectivePush.TAG, "No available socket");
              continue;
            } 
          } 
          SelectivePush.this.svSock.close();
          Trace.d(SelectivePush.TAG, "Server Socket close");
          try {
            SelectivePush.this.svSock.close();
            Trace.d(SelectivePush.TAG, "Server Socket close");
          } catch (Exception exception) {
            exception.printStackTrace();
          } 
          return;
        } 
      } catch (IOException iOException) {
        iOException.printStackTrace();
        try {
          SelectivePush.this.svSock.close();
          Trace.d(SelectivePush.TAG, "Server Socket close");
        } catch (Exception exception) {
          exception.printStackTrace();
        } 
        return;
      } finally {
        try {
          SelectivePush.this.svSock.close();
          Trace.d(SelectivePush.TAG, "Server Socket close");
        } catch (Exception exception) {
          exception.printStackTrace();
        } 
      } 
    }
  }
  
  private class SdcardStateChangedReceiver extends BroadcastReceiver {
    private SdcardStateChangedReceiver() {}
    
    public void onReceive(Context param1Context, Intent param1Intent) {
      Trace.e(SelectivePush.TAG, "Sdcard onReceive() -- " + param1Intent.getAction());
      if (param1Intent.getAction().equals("android.intent.action.MEDIA_REMOVED") || param1Intent.getAction().equals("android.intent.action.MEDIA_UNMOUNTED") || param1Intent.getAction().equals("android.intent.action.MEDIA_SHARED") || param1Intent.getAction().equals("android.intent.action.MEDIA_BAD_REMOVAL") || param1Intent.getAction().equals("android.intent.action.MEDIA_UNMOUNTABLE")) {
        Message message = new Message();
        message.what = 22;
        SelectivePush.this.mHandler.sendMessageDelayed(message, 1000L);
      } 
    }
  }
  
  private class WifiStateChangedReceiverForAppClose extends BroadcastReceiver {
    private WifiStateChangedReceiverForAppClose() {}
    
    public void onReceive(Context param1Context, Intent param1Intent) {
      if ("android.net.wifi.supplicant.CONNECTION_CHANGE".equals(param1Intent.getAction())) {
        if (!param1Intent.getBooleanExtra("connected", false) && SelectivePush.this.wifi_stat_conn) {
          SelectivePush.this.wifi_stat_conn = false;
          SelectivePush.this.dscConnected = false;
          SelectivePush.this.mHandler.sendEmptyMessage(52);
        } 
        return;
      } 
      if (param1Intent.getAction().equals("android.net.wifi.STATE_CHANGE")) {
        NetworkInfo.DetailedState detailedState = ((NetworkInfo)param1Intent.getParcelableExtra("networkInfo")).getDetailedState();
        Trace.d(SelectivePush.TAG, "NetworkInfo.DetailedState = " + detailedState.name());
        switch (detailedState) {
          default:
            Trace.d(SelectivePush.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - Unknown");
            return;
          case CONNECTING:
            SelectivePush.this.wifi_stat_conn = true;
            Trace.d(SelectivePush.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - CONNECTING");
            return;
          case OBTAINING_IPADDR:
            Trace.d(SelectivePush.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - OBTAINING_IPADDR");
            return;
          case CONNECTED:
            SelectivePush.this.wifi_stat_conn = true;
            Trace.d(SelectivePush.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - CONNECTED");
            return;
          case FAILED:
            Trace.d(SelectivePush.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - FAILED");
            return;
          case DISCONNECTING:
            Trace.d(SelectivePush.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - DISCONNECTING");
            return;
          case DISCONNECTED:
            break;
        } 
        Trace.d(SelectivePush.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - DISCONNECTED");
        Trace.d(SelectivePush.TAG, "wifi_stat_conn : " + SelectivePush.this.wifi_stat_conn);
        if (SelectivePush.this.wifi_stat_conn) {
          SelectivePush.this.wifi_stat_conn = false;
          SelectivePush.this.dscConnected = false;
          SelectivePush.this.mHandler.sendEmptyMessage(52);
          return;
        } 
      } 
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pushservice\selectivepush\SelectivePush.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */