package com.samsungimaging.connectionmanager.app.cm.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import com.samsungimaging.connectionmanager.app.cm.Interface.IModeClient;
import com.samsungimaging.connectionmanager.app.cm.Interface.IModeServer;
import com.samsungimaging.connectionmanager.app.cm.Main;
import com.samsungimaging.connectionmanager.app.cm.broadcastreceiver.CMNetworkStateChangeReceiver;
import com.samsungimaging.connectionmanager.app.cm.broadcastreceiver.CMScanResultReceiver;
import com.samsungimaging.connectionmanager.app.cm.common.CMConstants;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;
import com.samsungimaging.connectionmanager.app.cm.common.CMSharedPreferenceUtil;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.app.cm.connector.CMCameraAPConnector;
import com.samsungimaging.connectionmanager.app.cm.connector.CMEnableAllApsAfterConnecting;
import com.samsungimaging.connectionmanager.app.cm.modemanager.CMModeManager;
import com.samsungimaging.connectionmanager.app.cm.notimanager.CMNotificationManager;
import com.samsungimaging.connectionmanager.app.localgallery.LocalGallery;
import com.samsungimaging.connectionmanager.app.pullservice.demo.ml.FileSharing;
import com.samsungimaging.connectionmanager.app.pullservice.demo.rvf.LiveShutter;
import com.samsungimaging.connectionmanager.app.pushservice.autoshare.AutoShare;
import com.samsungimaging.connectionmanager.app.pushservice.selectivepush.SelectivePush;
import com.samsungimaging.connectionmanager.util.MyLocalGallery;
import com.samsungimaging.connectionmanager.util.Trace;
import java.util.ArrayList;
import java.util.List;

public class CMService extends Service {
  public static int ACTIVE_SERVICE;
  
  public static boolean AUTOSHARE_AUTOSEARCH;
  
  public static boolean IS_MODE_CONNECTED;
  
  public static boolean IS_WIFI_CONNECTED;
  
  public static ConnectivityManager mConnectivityManager;
  
  public static Context mContext = null;
  
  public static IModeClient mIModeClientDummy;
  
  public static WifiManager mWifiManager = null;
  
  private static CMService s_obj;
  
  private boolean beforeFinishFlag = false;
  
  private ActivityManager mActivityManager = null;
  
  private CMNetworkStateChangeReceiver mCMNetworkStateChangeReceiver = null;
  
  private CMScanResultReceiver mCMScanResultReceiver = null;
  
  private List<ScanResult> mCameraSrs = new ArrayList<ScanResult>();
  
  public int mConnectionTryCount = 0;
  
  private IModeServer mIModeServer = new IModeServer() {
      public void runByebye() {
        Trace.d(CMConstants.TAG_NAME, "CMService, IModeServer, runByebye()");
        CMUtil.sendBroadCastToMain(CMService.mContext, "SHOW_STATUS_BAR");
        CMNotificationManager.getInstance().notifyStatusChange(false, null, CMService.this.getResources().getString(2131361837).toString(), 2131361840);
        CMService.this.finishSafe();
      }
      
      public void runSubApplication(int param1Int) {
        Trace.d(CMConstants.TAG_NAME, "CMService, IModeServer, runSubApplication() => " + param1Int);
        if (param1Int == 20) {
          Trace.d(CMConstants.TAG_NAME, "CMService, IModeServer, runSubApplication(), ClientÃªÂ°â‚¬ Ã¬â€¢Å’ Ã¬Ë†Ëœ Ã¬â€”â€ Ã«Å â€� Ã¬Å¡â€�Ã¬Â²Â­Ã¬ï¿½â€ž Ã­â€¢ËœÃ¬Ëœâ‚¬Ã¬Å ÂµÃ«â€¹Ë†Ã«â€¹Â¤.");
          return;
        } 
        if (param1Int == 7) {
          Trace.d(CMConstants.TAG_NAME, "CMService, IModeServer, runSubApplication(), ClientÃªÂ°â‚¬ IDLEÃ¬ï¿½â€ž Ã¬â€¹Â¤Ã­â€“â€°Ã­â€¢ËœÃ«ï¿½Â¼ÃªÂ³Â  Ã¬Å¡â€�Ã¬Â²Â­Ã­â€“Ë†Ã¬Å ÂµÃ«â€¹Ë†Ã«â€¹Â¤.");
          return;
        } 
        Trace.d(CMConstants.TAG_NAME, "CMService, IModeServer, runSubApplication(), ClientÃªÂ°â‚¬ " + param1Int + "Ã«Â¥Â¼ Ã¬â€¹Â¤Ã­â€“â€°Ã­â€¢ËœÃ«ï¿½Â¼ÃªÂ³Â  Ã¬Å¡â€�Ã¬Â²Â­Ã­â€“Ë†Ã¬Å ÂµÃ«â€¹Ë†Ã«â€¹Â¤.");
        CMService.this.startSubApplication(param1Int);
      }
    };
  
  private boolean mIsCMNetworkStateChangeReceiverRegistered = false;
  
  private boolean mIsCMScanResultReceiverRegistered = false;
  
  private WifiManager.WifiLock mWifiLock = null;
  
  private Handler mWifiScanHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        super.handleMessage(param1Message);
        Trace.d(CMConstants.TAG_NAME, "WifiScanHandler start, IS_CONNECTED = " + CMService.IS_WIFI_CONNECTED + ", mWifiScanStop = " + CMService.this.mWifiScanStop + ", ACTIVE_SERVICE = " + CMService.ACTIVE_SERVICE + ", IsNFCLaunchFlag() = " + CMInfo.getInstance().getIsNFCLaunchFlag());
        if (CMService.mWifiManager == null)
          CMService.mWifiManager = (WifiManager)CMService.this.getApplicationContext().getSystemService("wifi"); 
        if (!CMService.IS_WIFI_CONNECTED && !CMService.this.mWifiScanStop && CMUtil.isAvailableScan(CMService.mContext, CMService.mWifiManager)) {
          Trace.d(CMConstants.TAG_NAME, "startScan");
          CMService.mWifiManager.startScan();
        } 
        CMService.this.mWifiScanHandler.sendEmptyMessageDelayed(0, 4000L);
      }
    };
  
  private boolean mWifiScanStop = true;
  
  static {
    mConnectivityManager = null;
    IS_WIFI_CONNECTED = false;
    IS_MODE_CONNECTED = false;
    AUTOSHARE_AUTOSEARCH = false;
    ACTIVE_SERVICE = -1;
    s_obj = null;
    mIModeClientDummy = new IModeClient() {
        public void runByebye() {}
        
        public void runSubApplication(int param1Int) {}
      };
  }
  
  public static CMService getInstance() {
    // Byte code:
    //   0: ldc com/samsungimaging/connectionmanager/app/cm/service/CMService
    //   2: monitorenter
    //   3: getstatic com/samsungimaging/connectionmanager/app/cm/service/CMService.s_obj : Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   6: ifnonnull -> 27
    //   9: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   12: ldc 's_obj is null!'
    //   14: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   17: new com/samsungimaging/connectionmanager/app/cm/service/CMService
    //   20: dup
    //   21: invokespecial <init> : ()V
    //   24: putstatic com/samsungimaging/connectionmanager/app/cm/service/CMService.s_obj : Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   27: getstatic com/samsungimaging/connectionmanager/app/cm/service/CMService.s_obj : Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   30: astore_0
    //   31: ldc com/samsungimaging/connectionmanager/app/cm/service/CMService
    //   33: monitorexit
    //   34: aload_0
    //   35: areturn
    //   36: astore_0
    //   37: ldc com/samsungimaging/connectionmanager/app/cm/service/CMService
    //   39: monitorexit
    //   40: aload_0
    //   41: athrow
    // Exception table:
    //   from	to	target	type
    //   3	27	36	finally
    //   27	31	36	finally
  }
  
  private void handleCommand() {
    Trace.d(CMConstants.TAG_NAME, "CMService : handlecommand");
    CMSharedPreferenceUtil.put(getApplicationContext(), "com.samsungimaging.connectionmanager.SP_KEY_IS_RECEIVER_RUNNING", "true");
    ((ConnectivityManager)mContext.getSystemService("connectivity")).setNetworkPreference(1);
    this.mWifiLock = mWifiManager.createWifiLock(1, "CM_LOCK");
    this.mWifiLock.setReferenceCounted(true);
    this.mWifiLock.acquire();
    Trace.d(CMConstants.TAG_NAME, "CMService : WIFI_MODE_FULL");
    CMEnableAllApsAfterConnecting.enalbleAllAps(mContext);
    Trace.d(CMConstants.TAG_NAME, "CMService : is restareted.......................");
  }
  
  private boolean isWifiConneced() {
    String str;
    boolean bool2 = false;
    WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
    NetworkInfo.DetailedState detailedState = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
    NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
    Trace.d(CMConstants.TAG_NAME, "isWifiConneced, state : " + detailedState + ", ssid : " + wifiInfo.getSSID() + ", networkInfo = " + networkInfo);
    if (networkInfo != null) {
      NetworkInfo.DetailedState detailedState1 = networkInfo.getDetailedState();
      str = networkInfo.getTypeName();
      boolean bool3 = networkInfo.isAvailable();
      boolean bool = bool2;
      if (detailedState1 == NetworkInfo.DetailedState.CONNECTED) {
        bool = bool2;
        if ("WIFI".equals(str)) {
          bool = bool2;
          if (bool3)
            bool = true; 
        } 
      } 
      return bool;
    } 
    boolean bool1 = bool2;
    return (str == NetworkInfo.DetailedState.CONNECTED) ? true : bool1;
  }
  
  private void registerBroadcastReceiver() {
    if (this.mCMScanResultReceiver == null) {
      this.mCMScanResultReceiver = new CMScanResultReceiver();
      IntentFilter intentFilter = new IntentFilter();
      intentFilter.addAction("android.net.wifi.SCAN_RESULTS");
      registerReceiver((BroadcastReceiver)this.mCMScanResultReceiver, intentFilter);
      this.mIsCMScanResultReceiverRegistered = true;
    } 
    if (this.mCMNetworkStateChangeReceiver == null) {
      this.mCMNetworkStateChangeReceiver = new CMNetworkStateChangeReceiver();
      IntentFilter intentFilter = new IntentFilter();
      intentFilter.addAction("android.net.wifi.STATE_CHANGE");
      intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
      intentFilter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE");
      intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
      intentFilter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
      registerReceiver((BroadcastReceiver)this.mCMNetworkStateChangeReceiver, intentFilter);
      this.mIsCMNetworkStateChangeReceiverRegistered = true;
    } 
  }
  
  private void stopService(Context paramContext) {
    paramContext.stopService(new Intent(paramContext, CMService.class));
  }
  
  private void unRegisterBroadcastReceiver() {
    CMSharedPreferenceUtil.put(mContext, "com.samsungimaging.connectionmanager.SP_KEY_IS_RECEIVER_RUNNING", "false");
    int i = Integer.parseInt(Build.VERSION.SDK);
    Trace.d(CMConstants.TAG_NAME, "unRegisterBroadcastReceiver, apiLevel = " + i);
    if (i >= 7)
      try {
        if (this.mCMScanResultReceiver != null && this.mIsCMScanResultReceiverRegistered) {
          mContext.unregisterReceiver((BroadcastReceiver)this.mCMScanResultReceiver);
          this.mIsCMScanResultReceiverRegistered = false;
        } 
        if (this.mCMNetworkStateChangeReceiver != null && this.mIsCMNetworkStateChangeReceiverRegistered) {
          mContext.unregisterReceiver((BroadcastReceiver)this.mCMNetworkStateChangeReceiver);
          this.mIsCMNetworkStateChangeReceiverRegistered = false;
        } 
        return;
      } catch (IllegalArgumentException illegalArgumentException) {
        Trace.d(CMConstants.TAG_NAME, "CMService.appclose, unregisterReceiver, IllegalArgumentException!!!");
        return;
      }  
    if (this.mCMScanResultReceiver != null && this.mIsCMScanResultReceiverRegistered) {
      unregisterReceiver((BroadcastReceiver)this.mCMScanResultReceiver);
      this.mIsCMScanResultReceiverRegistered = false;
    } 
    if (this.mCMNetworkStateChangeReceiver != null && this.mIsCMNetworkStateChangeReceiverRegistered) {
      unregisterReceiver((BroadcastReceiver)this.mCMNetworkStateChangeReceiver);
      this.mIsCMNetworkStateChangeReceiverRegistered = false;
      return;
    } 
  }
  
  public void NFCConnectionTryCancel() {
    if (CMInfo.getInstance().getIsNFCLaunchFlag()) {
      CMSharedPreferenceUtil.remove(mContext, "pref_nfc_ssid");
      CMInfo.getInstance().setIsNFCLaunch(false, "");
    } 
  }
  
  public void appclose() {
    CMModeManager.stopModeServer();
    CMModeManager.stopModeClient();
    if (this.mWifiScanHandler != null && this.mWifiScanHandler.hasMessages(0))
      this.mWifiScanHandler.removeMessages(0); 
    String str = CMSharedPreferenceUtil.getString(mContext, "com.samsungimaging.connectionmanager.ATFIRST_WIFIENABLED", "");
    if (!str.equals("")) {
      if (str.equals("true")) {
        mWifiManager.setWifiEnabled(true);
      } else if (str.equals("false")) {
        mWifiManager.setWifiEnabled(false);
      } 
      CMSharedPreferenceUtil.put(mContext, "com.samsungimaging.connectionmanager.ATFIRST_WIFIENABLED", "");
    } 
    CMCameraAPConnector.getInstance().cancel();
    unRegisterBroadcastReceiver();
    CMEnableAllApsAfterConnecting.enalbleAllAps(mContext);
    CMUtil.downPriority(mWifiManager);
    CMUtil.disalbleCameraAps(mContext, null);
    CMSharedPreferenceUtil.remove(mContext, "pref_nfc_ssid");
    CMInfo.getInstance().setIsNFCLaunch(false, "");
    if (this.mWifiLock != null && this.mWifiLock.isHeld())
      this.mWifiLock.release(); 
    CMNotificationManager.getInstance().cancelAllNoti();
    stopService(mContext);
    Trace.d(CMConstants.TAG_NAME, "CMService.stopService!");
    Trace.d(CMConstants.TAG_NAME, "CMService : is finished.....");
    if (Main.getInstance() != null)
      Main.getInstance().finish(); 
    IS_WIFI_CONNECTED = false;
    IS_MODE_CONNECTED = false;
    ACTIVE_SERVICE = -1;
    s_obj = null;
  }
  
  public void beforefinish(int paramInt) {
    if (!this.beforeFinishFlag) {
      this.beforeFinishFlag = true;
      String str = mWifiManager.getConnectionInfo().getSSID();
      boolean bool1 = CMUtil.checkOldVersionSmartCameraApp(str);
      boolean bool2 = isWifiConneced();
      Trace.d(CMConstants.TAG_NAME, "CMService, beforefinish, isConnected = " + bool2 + ", type = " + paramInt);
      CMUtil.sendBroadCastToMain(mContext, "SHOW_STATUS_BAR");
      if (bool2) {
        if (bool1 || ACTIVE_SERVICE == 5) {
          CMUtil.disableConnectedCamera(mWifiManager);
          CMNotificationManager.getInstance().notifyStatusChange(false, null, getResources().getString(2131362058).toString(), 2131361840);
          IS_WIFI_CONNECTED = false;
          IS_MODE_CONNECTED = false;
        } else if (paramInt == 1) {
          CMNotificationManager.getInstance().notifyStatusChange(false, null, getResources().getString(2131362058).toString(), 2131361840);
          CMUtil.sendBroadCastToMain(mContext, "START_WIFI_SCAN_TIMER_FOR_MIMUTES", 600000);
        } else if (paramInt == 2) {
          Main.getInstance().sendByebyeForLowBattery();
        } else {
          CMNotificationManager.getInstance().notifyStatusChange(true, str, getResources().getString(2131361838).toString(), 2131361840);
        } 
      } else if (paramInt == 1) {
        CMNotificationManager.getInstance().notifyStatusChange(false, null, getResources().getString(2131362058).toString(), 2131361840);
        CMUtil.sendBroadCastToMain(mContext, "START_WIFI_SCAN_TIMER_FOR_MIMUTES", 600000);
      } else {
        IS_WIFI_CONNECTED = false;
        IS_MODE_CONNECTED = false;
        CMUtil.sendBroadCastToMain(mContext, "WIFI_DISCONNECTED");
        CMNotificationManager.getInstance().notifyStatusChange(false, null, getResources().getString(2131362058).toString(), 2131361840);
      } 
      ACTIVE_SERVICE = 0;
      CMSharedPreferenceUtil.remove(mContext, "pref_nfc_ssid");
      CMInfo.getInstance().setIsNFCLaunch(false, "");
      str = CMUtil.whatIsTopActivity(mContext);
      bool1 = CMUtil.isCMTopActivity(mContext);
      Trace.d(CMConstants.TAG_NAME, "CMService, beforefinish, top = " + str + ", istop = " + bool1);
      Intent intent = new Intent(mContext, Main.class);
      intent.addFlags(872415232);
      startActivity(intent);
    } 
  }
  
  public void checkCurrentWifiConnection() {
    boolean bool = false;
    WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
    if (isWifiConneced()) {
      boolean bool1 = bool;
      if (CMUtil.supportDSCPrefix(wifiInfo.getSSID())) {
        bool1 = bool;
        if (CMUtil.isManagedSSID(mContext, wifiInfo.getSSID()))
          bool1 = true; 
      } 
      IS_WIFI_CONNECTED = bool1;
      if (IS_WIFI_CONNECTED)
        checkSubApplicationType(); 
      return;
    } 
    IS_WIFI_CONNECTED = false;
  }
  
  public void checkSubApplicationType() {
    String str = mWifiManager.getConnectionInfo().getSSID();
    CMInfo.getInstance().setConnectedSSID(str);
    if (false) {
      if (!isSubAppAlive())
        CMUtil.sendBroadCastToMain(mContext, "RUN_MODEMANAGER"); 
      return;
    } 
    if (!isSubAppAlive()) {
      int i = CMUtil.getAppTypeFromCamera(mWifiManager);
      if (i == 8) {
        CMUtil.sendBroadCastToMain(mContext, "RUN_MODEMANAGER");
        return;
      } 
      Trace.d(CMConstants.TAG_NAME, "checkSubApplicationType, Build.MODEL = " + Build.MODEL);
      if (CMUtil.lateDHCPRefresh(Build.MODEL)) {
        Trace.d(CMConstants.TAG_NAME, "Performance Check Point : Start sleep for updating DHCP info");
        if (Build.MODEL.contains("LG-F240")) {
          DhcpInfo dhcpInfo = mWifiManager.getDhcpInfo();
          if (dhcpInfo.leaseDuration <= 60) {
            Trace.d(CMConstants.TAG_NAME, "DHCP Lease time is " + dhcpInfo.leaseDuration);
            mWifiManager.reassociate();
          } 
        } 
        SystemClock.sleep(8000L);
        Trace.d(CMConstants.TAG_NAME, "Performance Check Point : End sleep for updating DHCP info");
        i = CMUtil.getAppTypeFromCamera(mWifiManager);
      } 
      if (CMUtil.isGalaxySeriesPrefix(str)) {
        if (1 <= i && i <= 5) {
          startSubApplication(i);
          return;
        } 
        startSubApplication(2);
        return;
      } 
      startSubApplication(i);
      return;
    } 
    startSubApplication(ACTIVE_SERVICE);
  }
  
  public void finishSafe() {
    CMUtil.sendBroadCastToMain(mContext, "APP_CLOSE_SENDING_BYEBYE");
  }
  
  public void forceClosed() {
    CMModeManager.runModeClient(mIModeClientDummy, true);
    getInstance().appclose();
  }
  
  public WifiManager.WifiLock getCMWifiLock() {
    return this.mWifiLock;
  }
  
  public List<ScanResult> getScannedList() {
    return this.mCameraSrs;
  }
  
  public void goToHome() {
    Intent intent = new Intent("android.intent.action.MAIN");
    intent.addCategory("android.intent.category.HOME");
    intent.setFlags(268435456);
    startActivity(intent);
  }
  
  public boolean isSubAppAlive() {
    Trace.d(CMConstants.TAG_NAME, "CMService, isSubAppAlive, ACTIVE_SERVICE = " + ACTIVE_SERVICE);
    switch (ACTIVE_SERVICE) {
      default:
        return false;
      case 1:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
        break;
    } 
    return true;
  }
  
  public boolean isWifiScanStop() {
    return this.mWifiScanStop;
  }
  
  public IBinder onBind(Intent paramIntent) {
    return null;
  }
  
  public void onCreate() {
    super.onCreate();
    Trace.d(CMConstants.TAG_NAME, "CMService : onCreate");
    s_obj = this;
    mContext = getApplicationContext();
    this.mActivityManager = (ActivityManager)getSystemService("activity");
    if (mWifiManager == null)
      mWifiManager = (WifiManager)mContext.getSystemService("wifi"); 
    if (mConnectivityManager == null)
      mConnectivityManager = (ConnectivityManager)mContext.getSystemService("connectivity"); 
    if (CMSharedPreferenceUtil.getString(mContext, "com.samsungimaging.connectionmanager.ATFIRST_WIFIENABLED", "").equals(""))
      if (mWifiManager.getWifiState() == 3 || mWifiManager.getWifiState() == 2) {
        CMSharedPreferenceUtil.put(mContext, "com.samsungimaging.connectionmanager.ATFIRST_WIFIENABLED", "true");
      } else {
        CMSharedPreferenceUtil.put(mContext, "com.samsungimaging.connectionmanager.ATFIRST_WIFIENABLED", "false");
      }  
    try {
      if (!mWifiManager.isWifiEnabled())
        mWifiManager.setWifiEnabled(true); 
    } catch (Throwable throwable) {}
    if (!isSubAppAlive()) {
      handleCommand();
      Trace.d(CMConstants.TAG_NAME, "CMService : registerBroadcastReceiver");
      registerBroadcastReceiver();
      this.mWifiScanHandler.sendEmptyMessage(1);
    } 
  }
  
  public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
    super.onStartCommand(paramIntent, paramInt1, paramInt2);
    return 2;
  }
  
  public void runModeServer() {
    CMModeManager.runModeServer(this.mIModeServer);
  }
  
  public void setScannedList(List<ScanResult> paramList) {
    if (paramList != null)
      this.mCameraSrs = paramList; 
  }
  
  public void startSubApplication(int paramInt) {
    String str1;
    String str2 = mWifiManager.getConnectionInfo().getSSID();
    boolean bool = CMUtil.checkOldVersionSmartCameraApp(str2);
    Trace.d(CMConstants.TAG_NAME, "startSubApplication, connected ssid = " + str2 + ", old = " + bool);
    Intent intent = null;
    if (paramInt == 1) {
      ACTIVE_SERVICE = 1;
      if (MyLocalGallery.isActivityCreated()) {
        Log.d("local_exit", "local existed");
        intent = new Intent(mContext, LocalGallery.class);
      } else if (bool) {
        intent = new Intent(mContext, FileSharing.class);
      } else {
        intent = new Intent(mContext, FileSharing.class);
        intent.putExtra("default_url_for_ml_rvf", CMInfo.getInstance().getDescriptionURL());
      } 
      str1 = "ML";
    } else if (paramInt == 2) {
      ACTIVE_SERVICE = 2;
      if (MyLocalGallery.isActivityCreated()) {
        Log.d("local_exit", "local existed");
        intent = new Intent(mContext, LocalGallery.class);
      } else if (bool) {
        intent = new Intent(mContext, LiveShutter.class);
      } else {
        intent = new Intent(mContext, LiveShutter.class);
        intent.putExtra("default_url_for_ml_rvf", CMInfo.getInstance().getDescriptionURL());
      } 
      str1 = "RVF";
    } else if (paramInt == 3) {
      ACTIVE_SERVICE = 3;
      if (bool) {
        intent = new Intent(mContext, AutoShare.class);
      } else {
        intent = new Intent(mContext, AutoShare.class);
      } 
      str1 = "S2L";
    } else if (paramInt == 4) {
      ACTIVE_SERVICE = 4;
      if (bool) {
        intent = new Intent(mContext, SelectivePush.class);
      } else {
        intent = new Intent(mContext, SelectivePush.class);
      } 
      intent.putExtra("selectivepush_service_type", "selectivepush");
      str1 = "SP";
    } else if (paramInt == 5) {
      ACTIVE_SERVICE = 5;
      if (bool) {
        intent = new Intent(mContext, SelectivePush.class);
      } else {
        intent = new Intent(mContext, SelectivePush.class);
      } 
      intent.putExtra("selectivepush_service_type", "groupshare");
      str1 = "GS";
    } else if (paramInt == 6) {
      ACTIVE_SERVICE = 6;
      if (bool) {
        intent = new Intent(mContext, SelectivePush.class);
      } else {
        intent = new Intent(mContext, SelectivePush.class);
      } 
      intent.putExtra("selectivepush_service_type", "mobilebackup");
      str1 = "AUTOBACKUP";
    } else if (paramInt == 7) {
      str1 = "IDLE";
    } else {
      Trace.d(CMConstants.TAG_NAME, "startSubApplication error, Ã­ËœÂ¸Ã¬Â¶Å“Ã­â€¢Â  Ã­â€¢ËœÃ¬Å“â€žÃ¬â€¢Â±Ã¬ï¿½â€ž Ã­Å’ï¿½Ã«â€¹Â¨Ã­â€¢Â  Ã¬Ë†Ëœ Ã¬â€”â€ Ã¬Å ÂµÃ«â€¹Ë†Ã«â€¹Â¤.");
      str1 = "error";
      CMUtil.disableConnectedCamera(mWifiManager);
      IS_WIFI_CONNECTED = false;
    } 
    Trace.d(CMConstants.TAG_NAME, "Ã­â€¢ËœÃ¬Å“â€žÃ¬â€¢Â± Ã­â„¢â€¢Ã¬ï¿½Â¸ ÃªÂ²Â°ÃªÂ³Â¼, " + str1 + "Ã¬ï¿½Â´(ÃªÂ°â‚¬) Ã¬â€¹Â¤Ã­â€“â€°Ã«ï¿½Â©Ã«â€¹Ë†Ã«â€¹Â¤.");
    CMUtil.sendBroadCastToMain(mContext, "STOP_NFC_TAGGING_TIMER");
    if (intent != null && mWifiManager.isWifiEnabled()) {
      intent.addFlags(872415232);
      Trace.d(CMConstants.TAG_NAME, "Performance Check Point : Start App : " + paramInt);
      if ((paramInt == 3 || paramInt == 4 || paramInt == 5 || paramInt == 6) && getInstance() != null) {
        WifiManager.WifiLock wifiLock = getInstance().getCMWifiLock();
        if (wifiLock != null && wifiLock.isHeld())
          wifiLock.release(); 
        wifiLock = mWifiManager.createWifiLock(3, "CM_LOCK");
        wifiLock.setReferenceCounted(true);
        wifiLock.acquire();
        Trace.d(CMConstants.TAG_NAME, "startAppAfterConnectingAp : WIFI_MODE_FULL_HIGH_PERF");
      } 
      CMUtil.sendBroadCastToMain(mContext, "SHOW_STATUS_BAR");
      CMNotificationManager.getInstance().notifyStatusChange(true, str2, mContext.getResources().getString(2131361838).toString(), 2131361840);
      this.beforeFinishFlag = false;
      CMCameraAPConnector.getInstance().cancel();
      mContext.startActivity(intent);
      if (Main.getInstance() != null)
        Main.getInstance().dismissAllDialog(); 
    } 
  }
  
  public void stopModeServer() {
    CMModeManager.removeModeServerListener(this.mIModeServer);
    CMModeManager.stopModeServer();
  }
  
  public void wifiScanStart() {
    this.mWifiScanStop = false;
  }
  
  public void wifiScanStop() {
    this.mConnectionTryCount = 0;
    this.mWifiScanStop = true;
    CMCameraAPConnector.getInstance().cancel();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\service\CMService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */