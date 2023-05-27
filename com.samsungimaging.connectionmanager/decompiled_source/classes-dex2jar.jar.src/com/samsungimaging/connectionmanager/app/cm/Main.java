package com.samsungimaging.connectionmanager.app.cm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Process;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.samsungimaging.connectionmanager.app.BaseActivity;
import com.samsungimaging.connectionmanager.app.BaseGalleryActivity;
import com.samsungimaging.connectionmanager.app.cm.Interface.IModeClient;
import com.samsungimaging.connectionmanager.app.cm.common.CMConstants;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;
import com.samsungimaging.connectionmanager.app.cm.common.CMSharedPreferenceUtil;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.app.cm.connector.LastRequestedCameraInfo;
import com.samsungimaging.connectionmanager.app.cm.modemanager.CMModeManager;
import com.samsungimaging.connectionmanager.app.cm.modemanager.ModeServer;
import com.samsungimaging.connectionmanager.app.cm.notimanager.CMNotificationManager;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;
import com.samsungimaging.connectionmanager.dialog.CMTestModeDialog;
import com.samsungimaging.connectionmanager.dialog.CMTestModePassWordDialog;
import com.samsungimaging.connectionmanager.dialog.CustomDialog;
import com.samsungimaging.connectionmanager.dialog.CustomProgressDialog;
import com.samsungimaging.connectionmanager.dialog.InitGuideDialog;
import com.samsungimaging.connectionmanager.dialog.InitHelpDialog;
import com.samsungimaging.connectionmanager.dialog.IntroDialog;
import com.samsungimaging.connectionmanager.dialog.IntroNoticeDialog;
import com.samsungimaging.connectionmanager.dialog.ModeClientDialog;
import com.samsungimaging.connectionmanager.dialog.NotSupportedDialog;
import com.samsungimaging.connectionmanager.dialog.NowAppClosingProgressDialog;
import com.samsungimaging.connectionmanager.dialog.RefusalDialog;
import com.samsungimaging.connectionmanager.dialog.SearchAPDialog;
import com.samsungimaging.connectionmanager.dialog.VerifyingPoorLinkDialog;
import com.samsungimaging.connectionmanager.gallery.GalleryFragment;
import com.samsungimaging.connectionmanager.manager.DatabaseManager;
import com.samsungimaging.connectionmanager.util.Trace;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Main extends BaseGalleryActivity implements View.OnClickListener {
  private static final int DIALOG_ID_INIT_GUIDE = 1003;
  
  private static final int DIALOG_ID_INIT_HELP = 1004;
  
  private static final int DIALOG_ID_INTRO_GUIDE = 1002;
  
  private static final int DIALOG_ID_INTRO_NOTICE = 1010;
  
  private static final int DIALOG_ID_MODE_CLIENT = 1001;
  
  private static final int DIALOG_ID_NOT_SUPPORTED_CAMERA = 1007;
  
  private static final int DIALOG_ID_NOW_APP_CLOSING = 1008;
  
  private static final int DIALOG_ID_REFUSAL = 1006;
  
  private static final int DIALOG_ID_SEARCH_AP = 1000;
  
  public static final int DIALOG_ID_TEST_MODE = 1011;
  
  private static final int DIALOG_ID_TEST_MODE_PASSWORD = 1009;
  
  private static final int DIALOG_ID_VERIFYING_POOR_LINK = 1005;
  
  public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
  
  private static final String packageNameACM = "com.samsungimaging.connectionmanager";
  
  private static Main s_obj = null;
  
  final ArrayList<String> deniedPermissions = new ArrayList<String>();
  
  private Button mConnectButton = null;
  
  private TextView mDescription = null;
  
  private Thread mFinishThread = null;
  
  private IModeClient mIModeClientForSendingByebye = new IModeClient() {
      public void runByebye() {
        Trace.d(CMConstants.TAG_NAME, "CMService, ModeClient Sending byebye...response 200OK byebye.");
        CMUtil.disableConnectedCamera(CMService.mWifiManager);
        CMService.IS_WIFI_CONNECTED = false;
      }
      
      public void runSubApplication(int param1Int) {
        Trace.d(CMConstants.TAG_NAME, "CMService, ModeClient Sending byebye...response runSubApplication, msg = " + param1Int);
        CMUtil.disableConnectedCamera(CMService.mWifiManager);
        CMService.IS_WIFI_CONNECTED = false;
      }
    };
  
  private Handler mNFCTaggingTimerHandler = null;
  
  private BroadcastReceiver mNetworkReciever = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        Bundle bundle;
        int i;
        String str;
        if ("com.samsungimaging.connectionmanager.intent.FROM_CM".equals(param1Intent.getAction())) {
          Trace.d(CMConstants.TAG_NAME, "INTENT_FROM_CM~");
          String str1 = null;
          Context context = null;
          byte b = -1;
          param1Context = context;
          str = str1;
          i = b;
          if (param1Intent != null) {
            param1Context = context;
            str = str1;
            i = b;
            if (param1Intent.getExtras() != null) {
              str = param1Intent.getExtras().getString("EXTRA_KEY_FROM_CM");
              i = param1Intent.getExtras().getInt("EXTRA_KEY2_FROM_CM", -1);
              bundle = param1Intent.getExtras().getBundle("EXTRA_KEY3_FROM_CM");
              Trace.d(CMConstants.TAG_NAME, "extraInfo = " + str + ", extraInfo2 = " + i + ", bundle = " + bundle);
            } 
          } 
          if (str != null) {
            if (str.equals("WIFI_DISCONNECTED")) {
              Main.this.wifiDisconnected();
              return;
            } 
          } else {
            return;
          } 
        } else {
          return;
        } 
        if (str.equals("RUN_MODEMANAGER")) {
          Main.this.showModeManagerDialog();
          return;
        } 
        if (str.equals("SHOW_SEARCHAPDIALOG")) {
          Main.this.showWifiManagerDialog();
          return;
        } 
        if (str.equals("SHOW_HELPDIALOG")) {
          Main.this.showHelpDialog();
          return;
        } 
        if (str.equals("CHECK_FOR_INTERNET_SERVICE_IN_SETTINGS")) {
          Main.this.showVerifyingPoorLinkDialog();
          return;
        } 
        if (str.equals("CHECK_FOR_401_ERROR") || str.equals("CHECK_FOR_503_ERROR")) {
          Main.this.wifiDisconnected();
          Main.this.showRefusalDialog(str);
          return;
        } 
        if (str.equals("SHOW_NOT_SUPPORTED_DIALOG")) {
          Main.this.showNOTSupportedDialog();
          return;
        } 
        if (str.equals("CHECK_FOR_MODECLIENT_NOTRESPONSE_OR_CANCELED")) {
          Main.this.wifiDisconnected();
          Toast.makeText((Context)Main.this, "There is no response from the Camera. Please retry again", 0).show();
          return;
        } 
        if (str.equals("START_WIFI_SCAN_TIMER_FOR_MIMUTES")) {
          Trace.d(CMConstants.TAG_NAME, "EXTRA_VALUE_START_WIFI_SCAN_TIMER_FOR_MIMUTES, extraInfo2 = " + i);
          if (i > 0) {
            Main.this.startWifiScanTimer(i);
            return;
          } 
          Main.this.startWifiScanTimer(120000);
          return;
        } 
        if (str.equals("STOP_WIFI_SCAN_TIMER_FOR_MIMUTES")) {
          Trace.d(CMConstants.TAG_NAME, "EXTRA_VALUE_STOP_WIFI_SCAN_TIMER_FOR_MIMUTES");
          Main.this.stopWifiScanTimer();
          return;
        } 
        if (str.equals("SHOW_STATUS_BAR")) {
          Main.this.showStatusBar();
          return;
        } 
        if (str.equals("STOP_NFC_TAGGING_TIMER")) {
          Main.this.stopNFCTaggingTimer();
          return;
        } 
        if (str.equals("APP_CLOSE_SENDING_BYEBYE")) {
          Main.this.appcloseSendingByeBye();
          return;
        } 
        if (str.equals("NEED_PASSWORD")) {
          Trace.d(CMConstants.TAG_NAME, "EXTRA_VALUE_NEED_PASSWORD, CMService.IS_WIFI_CONNECTED = " + CMService.IS_WIFI_CONNECTED);
          if (!CMService.IS_WIFI_CONNECTED) {
            CustomDialog customDialog = (CustomDialog)Main.this.mDialogList.get(1000);
            if (customDialog != null && customDialog.isShowing())
              customDialog.dismiss(); 
            if (bundle != null) {
              String[] arrayOfString = (String[])bundle.getCharSequenceArray("data");
              Trace.d(CMConstants.TAG_NAME, "EXTRA_VALUE_NEED_PASSWORD, security_info[0] = " + arrayOfString[0]);
              Trace.d(CMConstants.TAG_NAME, "EXTRA_VALUE_NEED_PASSWORD, security_info[1] = " + arrayOfString[1]);
              Trace.d(CMConstants.TAG_NAME, "EXTRA_VALUE_NEED_PASSWORD, security_info[2] = " + arrayOfString[2]);
              ScanResult scanResult = LastRequestedCameraInfo.getInstance().getLastRequestedCameraInfo();
              if (scanResult == null) {
                Trace.d(CMConstants.TAG_NAME, "EXTRA_VALUE_NEED_PASSWORD, last_sr = " + scanResult);
              } else {
                Trace.d(CMConstants.TAG_NAME, "EXTRA_VALUE_NEED_PASSWORD, last_sr.SSID = " + scanResult.SSID);
              } 
              if (scanResult == null || (scanResult != null && scanResult.SSID.contains(arrayOfString[0]))) {
                CustomDialog customDialog1 = (CustomDialog)Main.this.mDialogList.get(6);
                if (customDialog1 == null || !customDialog1.isShowing()) {
                  Main.this.showDialog(6, bundle);
                  return;
                } 
                Trace.d(CMConstants.TAG_NAME, "EXTRA_VALUE_NEED_PASSWORD, password already showing!");
                return;
              } 
              Trace.d(CMConstants.TAG_NAME, "EXTRA_VALUE_NEED_PASSWORD, other SSID selected.");
              return;
            } 
          } 
        } 
      }
    };
  
  private boolean mSSID_Fixed_Mode_For_Test = false;
  
  private Handler mTimerHandler = null;
  
  private Handler mTimerHandlerForInitGuideDialog = null;
  
  private void changeMainDescription() {
    Trace.d(CMConstants.TAG_NAME, "changeMainDescription, WIFI CONNECTED = " + CMService.IS_WIFI_CONNECTED + ", AUTOSHARE_AUTOSEARCH = " + CMService.AUTOSHARE_AUTOSEARCH);
    if (CMService.IS_WIFI_CONNECTED) {
      this.mDescription.setText(2131362049);
      this.mConnectButton.setEnabled(false);
    } else if (CMService.AUTOSHARE_AUTOSEARCH) {
      this.mDescription.setText(2131362059);
      this.mConnectButton.setEnabled(true);
    } else {
      this.mDescription.setText(2131362047);
      this.mConnectButton.setEnabled(true);
    } 
    checkTestMode();
  }
  
  private void checkModeServerPort() {
    ModeServer.mServerPort = CMSharedPreferenceUtil.getInteger((Context)this, "com.samsungimaging.connectionmanager.MODESERVER_PORT", 7789);
    Trace.d(CMConstants.TAG_NAME, "checkModeServerPort, ModeServer.mServerPort : " + ModeServer.mServerPort);
  }
  
  private void checkRunTimePermission() {
    ArrayList<String> arrayList = new ArrayList();
    arrayList.add("android.permission.ACCESS_FINE_LOCATION");
    arrayList.add("android.permission.WRITE_EXTERNAL_STORAGE");
    arrayList.add("android.permission.READ_PHONE_STATE");
    if (ContextCompat.checkSelfPermission((Context)this, "android.permission.ACCESS_FINE_LOCATION") != 0)
      this.deniedPermissions.add("android.permission.ACCESS_FINE_LOCATION"); 
    if (ContextCompat.checkSelfPermission((Context)this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0)
      this.deniedPermissions.add("android.permission.WRITE_EXTERNAL_STORAGE"); 
    if (ContextCompat.checkSelfPermission((Context)this, "android.permission.READ_PHONE_STATE") != 0)
      this.deniedPermissions.add("android.permission.READ_PHONE_STATE"); 
    if (!arrayList.isEmpty()) {
      ActivityCompat.requestPermissions((Activity)this, arrayList.<String>toArray(new String[arrayList.size()]), 1);
      return;
    } 
    if (ContextCompat.checkSelfPermission((Context)this, "android.permission.READ_PHONE_STATE") == 0 && ContextCompat.checkSelfPermission((Context)this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 && ContextCompat.checkSelfPermission((Context)this, "android.permission.ACCESS_FINE_LOCATION") == 0) {
      callMethod();
      return;
    } 
  }
  
  private void cmStart() {
    CMNotificationManager.getInstance().init(getApplicationContext());
    showStatusBar();
    CMNotificationManager.getInstance().notifyStatusChange(false, null, getResources().getString(2131362058).toString(), 2131361840);
    startForegroundService();
  }
  
  private void createContent() {
    setContentView(2130903044);
    this.mGallery = (GalleryFragment)getFragmentManager().findFragmentById(2131558499);
    this.mGallery.setDiskCacheDir("thumbnail", "viewer");
    this.mGallery.setOnItemClickListener((GalleryFragment.OnItemClickListener)this);
    this.mGallery.setOnItemLongClickListener((GalleryFragment.OnItemLongClickListener)this);
    this.mGallery.setQuery(getQuery());
    this.mActionBar.setTitle(2131362050);
    this.mActionBar.show();
    this.mDescription = (TextView)findViewById(16908308);
    this.mDescription.setText(2131362047);
    this.mConnectButton = (Button)findViewById(16908313);
    this.mConnectButton.setText(2131362048);
    this.mConnectButton.setOnClickListener(this);
    registerLocalBroadcastReceiver();
    cmStart();
  }
  
  public static Main getInstance() {
    // Byte code:
    //   0: ldc com/samsungimaging/connectionmanager/app/cm/Main
    //   2: monitorenter
    //   3: getstatic com/samsungimaging/connectionmanager/app/cm/Main.s_obj : Lcom/samsungimaging/connectionmanager/app/cm/Main;
    //   6: astore_0
    //   7: ldc com/samsungimaging/connectionmanager/app/cm/Main
    //   9: monitorexit
    //   10: aload_0
    //   11: areturn
    //   12: astore_0
    //   13: ldc com/samsungimaging/connectionmanager/app/cm/Main
    //   15: monitorexit
    //   16: aload_0
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   3	7	12	finally
  }
  
  private NdefMessage[] getNdefMessages(Intent paramIntent) {
    Trace.d(CMConstants.TAG_NAME, "getNdefMessages()");
    NdefMessage[] arrayOfNdefMessage = null;
    String str = paramIntent.getAction();
    if ("android.nfc.action.TAG_DISCOVERED".equals(str) || "android.nfc.action.NDEF_DISCOVERED".equals(str)) {
      Parcelable[] arrayOfParcelable = paramIntent.getParcelableArrayExtra("android.nfc.extra.NDEF_MESSAGES");
      if (arrayOfParcelable != null) {
        arrayOfNdefMessage = new NdefMessage[arrayOfParcelable.length];
        int i = 0;
        while (true) {
          if (i < arrayOfParcelable.length) {
            arrayOfNdefMessage[i] = (NdefMessage)arrayOfParcelable[i];
            i++;
            continue;
          } 
          return arrayOfNdefMessage;
        } 
      } 
    } else {
      return arrayOfNdefMessage;
    } 
    byte[] arrayOfByte = new byte[0];
    return new NdefMessage[] { new NdefMessage(new NdefRecord[] { new NdefRecord((short)5, arrayOfByte, arrayOfByte, arrayOfByte) }) };
  }
  
  @SuppressLint({"NewApi"})
  public static Drawable getPermissionGroupDrawable(Context paramContext, String paramString) {
    try {
      PackageManager packageManager = paramContext.getPackageManager();
      if (packageManager == null)
        return null; 
      PermissionGroupInfo permissionGroupInfo = packageManager.getPermissionGroupInfo((packageManager.getPermissionInfo(paramString, 4096)).group, 4096);
      return paramContext.getResources().getDrawable(permissionGroupInfo.icon, null);
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {
      return null;
    } 
  }
  
  public static String getPermissionGroupString(Context paramContext, String paramString) {
    PackageManager.NameNotFoundException nameNotFoundException2 = null;
    try {
      PackageManager packageManager = paramContext.getPackageManager();
      if (packageManager == null)
        return null; 
      PermissionGroupInfo permissionGroupInfo = packageManager.getPermissionGroupInfo((packageManager.getPermissionInfo(paramString, 4096)).group, 4096);
      String str = paramContext.getResources().getString(permissionGroupInfo.labelRes);
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException1) {
      nameNotFoundException1 = nameNotFoundException2;
    } 
    return (String)nameNotFoundException1;
  }
  
  private void processTag(Intent paramIntent) {
    String str = paramIntent.getAction();
    Trace.d(CMConstants.TAG_NAME, "processTag(), act = " + str);
    if ("android.nfc.action.NDEF_DISCOVERED".equals(str)) {
      Trace.d(CMConstants.TAG_NAME, "processTag(), ACTION_NDEF_DISCOVERED");
      NdefMessage[] arrayOfNdefMessage = getNdefMessages(paramIntent);
      Trace.d(CMConstants.TAG_NAME, "processTag(), messages.length = " + arrayOfNdefMessage.length);
      int j = arrayOfNdefMessage.length;
      String[][] arrayOfString = new String[j][];
      int i = 0;
      label19: while (true) {
        String str1;
        WifiInfo wifiInfo;
        if (i >= j) {
          str1 = arrayOfString[0][0].trim().toUpperCase();
          DatabaseManager.putMaxCountForAP(CMService.mContext, str1);
          CMInfo.getInstance().setIsNFCLaunch(true, str1);
          wifiInfo = CMService.mWifiManager.getConnectionInfo();
          if (CMUtil.supportDSCPrefix(wifiInfo.getSSID()) && wifiInfo.getSSID().indexOf(str1) < 0 && CMService.getInstance().isSubAppAlive()) {
            Trace.d(CMConstants.TAG_NAME, "processTag(), current SSID : " + wifiInfo.getSSID() + ", new SSID : " + str1);
            CMSharedPreferenceUtil.put((Context)this, "pref_nfc_ssid", str1);
            CMUtil.finishSubServicew(CMService.mContext);
            CMUtil.disableConnectedCamera(CMService.mWifiManager);
            CMService.mWifiManager.disconnect();
            CMService.IS_WIFI_CONNECTED = false;
            CMService.ACTIVE_SERVICE = 0;
          } 
          return;
        } 
        int m = (str1[i].getRecords()).length;
        wifiInfo[i] = (WifiInfo)new String[m];
        for (int k = 0;; k++) {
          if (k >= m) {
            i++;
            continue label19;
          } 
          byte[] arrayOfByte = str1[i].getRecords()[k].getPayload();
          wifiInfo[i][k] = (WifiInfo)new String(arrayOfByte);
          Trace.d(CMConstants.TAG_NAME, "processTag(), payload_record_str[" + k + "] : " + wifiInfo[i][k]);
        } 
        break;
      } 
    } 
  }
  
  private void registerLocalBroadcastReceiver() {
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("com.samsungimaging.connectionmanager.intent.FROM_CM");
    registerReceiver(this.mNetworkReciever, intentFilter);
  }
  
  private void showHelpDialog() {
    showDialog(1004);
  }
  
  private void showInitDialog() {
    showDialog(1002);
    if (this.mTimerHandlerForInitGuideDialog == null)
      this.mTimerHandlerForInitGuideDialog = new Handler() {
          public void handleMessage(Message param1Message) {
            CustomDialog customDialog = (CustomDialog)Main.this.mDialogList.get(1002);
            if (customDialog != null && customDialog.isShowing())
              customDialog.dismiss(); 
            Main.this.createContent();
            Main.this.showInitIntroDialog();
          }
        }; 
    this.mTimerHandlerForInitGuideDialog.sendEmptyMessageDelayed(0, 500L);
  }
  
  private void showInitIntroDialog() {
    if (!this.mSettings.getIntroNoticeGuide()) {
      showDialog(1010);
      return;
    } 
    showInitNFCDialog();
  }
  
  private void showInitNFCDialog() {
    if (this.mNFCAdapter != null) {
      if (!this.mSettings.getNFCConnectionGuide()) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("data", true);
        showDialog(3, bundle);
        return;
      } 
      showWifiManagerDialog();
      return;
    } 
    boolean bool = "true".equalsIgnoreCase(CMSharedPreferenceUtil.getString(getApplicationContext(), "com.samsungimaging.connectionmanager.SP_KEY_SHOW_INIT_GUIDE_DAILOG", "true"));
    Trace.d(CMConstants.TAG_NAME, "showInitDialog = " + bool);
    if (bool) {
      showDialog(1003);
      return;
    } 
    showWifiManagerDialog();
  }
  
  private void showModeManagerDialog() {
    Trace.d(CMConstants.TAG_NAME, "showModeManagerDialog(), WIFI CONNECTED = " + CMService.IS_WIFI_CONNECTED + ", MODE CONNECTED = " + CMService.IS_MODE_CONNECTED);
    if (CMService.IS_WIFI_CONNECTED) {
      if (CMService.IS_MODE_CONNECTED) {
        Trace.d(CMConstants.TAG_NAME, "showModeManagerDialog(), wifi conneced and mode connected. NOT show ModeManager Dialog.");
        return;
      } 
    } else {
      return;
    } 
    CustomDialog customDialog1 = (CustomDialog)this.mDialogList.get(3);
    CustomDialog customDialog2 = (CustomDialog)this.mDialogList.get(1003);
    CustomDialog customDialog3 = (CustomDialog)this.mDialogList.get(1004);
    if ((customDialog1 != null && customDialog1.isShowing()) || (customDialog2 != null && customDialog2.isShowing()) || (customDialog3 != null && customDialog3.isShowing())) {
      Trace.d(CMConstants.TAG_NAME, "showModeManagerDialog, Init Guide Dialog is showing...wait");
      return;
    } 
    stopWifiScanTimer();
    CMService.getInstance().runModeServer();
    customDialog1 = (CustomDialog)this.mDialogList.get(1000);
    if (customDialog1 != null && customDialog1.isShowing())
      customDialog1.dismiss(); 
    customDialog1 = (CustomDialog)this.mDialogList.get(1001);
    if (customDialog1 == null || !customDialog1.isShowing()) {
      showDialog(1001);
      return;
    } 
    if (customDialog1 != null) {
      customDialog1.isShowing();
      return;
    } 
  }
  
  private void showNOTSupportedDialog() {
    showDialog(1007);
  }
  
  private void showRefusalDialog(String paramString) {
    Bundle bundle = new Bundle();
    bundle.putString("data", paramString);
    showDialog(1006, bundle);
  }
  
  private void showVerifyingPoorLinkDialog() {
    showDialog(1005);
  }
  
  private void showWifiManagerDialog() {
    Trace.d(CMConstants.TAG_NAME, "showWifiManagerDialog()");
    CMService.getInstance().checkCurrentWifiConnection();
    if (!CMService.IS_WIFI_CONNECTED) {
      if (!CMService.mWifiManager.isWifiEnabled())
        CMService.mWifiManager.setWifiEnabled(true); 
      showDialog(1000);
    } 
  }
  
  private void startForegroundService() {
    startService(new Intent(getApplicationContext(), CMService.class));
  }
  
  private void startNFCTaggingTimer() {
    if (this.mNFCTaggingTimerHandler == null) {
      this.mNFCTaggingTimerHandler = new Handler() {
          public void handleMessage(Message param1Message) {
            CMService.getInstance().NFCConnectionTryCancel();
          }
        };
      this.mNFCTaggingTimerHandler.sendEmptyMessageDelayed(0, 20000L);
      return;
    } 
    if (this.mNFCTaggingTimerHandler.hasMessages(0))
      this.mNFCTaggingTimerHandler.removeMessages(0); 
    this.mNFCTaggingTimerHandler.sendEmptyMessageDelayed(0, 20000L);
  }
  
  private void startWifiScanTimer(int paramInt) {
    CMService.getInstance().wifiScanStart();
    Trace.d(CMConstants.TAG_NAME, "startWifiScanTimer, WIFI CONNECTED = " + CMService.IS_WIFI_CONNECTED + ", time = " + paramInt);
    if (paramInt == 600000) {
      CMService.AUTOSHARE_AUTOSEARCH = true;
    } else {
      CMService.AUTOSHARE_AUTOSEARCH = false;
    } 
    changeMainDescription();
    if (this.mTimerHandler == null) {
      this.mTimerHandler = new Handler() {
          public void handleMessage(Message param1Message) {
            CMService.getInstance().wifiScanStop();
            CMService.AUTOSHARE_AUTOSEARCH = false;
            Main.this.changeMainDescription();
            CustomDialog customDialog = (CustomDialog)Main.this.mDialogList.get(1000);
            if (customDialog != null && customDialog.isShowing()) {
              Trace.d(CMConstants.TAG_NAME, "Main, startWifiScanTimer, background WIFI scan stop and Search dialog dismiss.");
              customDialog.dismiss();
            } 
          }
        };
      this.mTimerHandler.sendEmptyMessageDelayed(0, paramInt);
      return;
    } 
    if (this.mTimerHandler.hasMessages(0))
      this.mTimerHandler.removeMessages(0); 
    this.mTimerHandler.sendEmptyMessageDelayed(0, paramInt);
  }
  
  private void stopNFCTaggingTimer() {
    if (this.mNFCTaggingTimerHandler != null) {
      if (this.mNFCTaggingTimerHandler.hasMessages(0))
        this.mNFCTaggingTimerHandler.removeMessages(0); 
      this.mNFCTaggingTimerHandler = null;
    } 
  }
  
  private void stopWifiScanTimer() {
    CMService.getInstance().wifiScanStop();
    Trace.d(CMConstants.TAG_NAME, "stopWifiScanTimer, WIFI CONNECTED = " + CMService.IS_WIFI_CONNECTED);
    CMService.AUTOSHARE_AUTOSEARCH = false;
    changeMainDescription();
    if (this.mTimerHandler != null) {
      if (this.mTimerHandler.hasMessages(0))
        this.mTimerHandler.removeMessages(0); 
      this.mTimerHandler = null;
    } 
  }
  
  private void wifiDisconnected() {
    CMService.IS_MODE_CONNECTED = false;
    changeMainDescription();
    CMService.getInstance().stopModeServer();
    CustomDialog customDialog = (CustomDialog)this.mDialogList.get(1001);
    if (customDialog != null && customDialog.isShowing()) {
      Trace.d(CMConstants.TAG_NAME, "wifiDisconnected(), wifi disconnected...BUT ModeClientDialog is showing, dismiss.");
      CMModeManager.stopModeClient();
      CMService.IS_WIFI_CONNECTED = false;
      CMService.getInstance().NFCConnectionTryCancel();
      customDialog.dismiss();
    } 
    showStatusBar();
    CMNotificationManager.getInstance().notifyStatusChange(false, null, getResources().getString(2131362058).toString(), 2131361840);
  }
  
  public void appcloseSendingByeBye() {
    Trace.d(CMConstants.TAG_NAME, "appcloseSendingByeBye()");
    showDialog(1008);
    if (this.mFinishThread == null) {
      this.mFinishThread = new Thread(new Runnable() {
            public void run() {
              boolean bool = CMUtil.checkOldVersionSmartCameraApp(CMInfo.getInstance().getConnectedSSID());
              Trace.d(CMConstants.TAG_NAME, "appcloseSendingByeBye, old camera= " + bool + ", WIFI CONNECTED = " + CMService.IS_WIFI_CONNECTED);
              if (CMService.IS_WIFI_CONNECTED && !bool) {
                CMModeManager.runModeClient(Main.this.mIModeClientForSendingByebye, true);
                int i = 0;
                while (true) {
                  Trace.d(CMConstants.TAG_NAME, "appcloseSendingByeBye, run... count = " + i);
                  if (!CMService.IS_WIFI_CONNECTED) {
                    Trace.d(CMConstants.TAG_NAME, "appcloseSendingByeBye, run...break01");
                  } else if (i == 12) {
                    Trace.d(CMConstants.TAG_NAME, "appcloseSendingByeBye, run...break02");
                  } else {
                    i++;
                    SystemClock.sleep(500L);
                    continue;
                  } 
                  Main.this.dismissAllDialog();
                  CMModeManager.stopModeClient();
                  CMService.getInstance().appclose();
                  return;
                } 
              } 
              Main.this.dismissAllDialog();
              CMModeManager.stopModeClient();
              CMService.getInstance().appclose();
            }
          });
      this.mFinishThread.start();
    } 
  }
  
  public void callMethod() {
    CMService.mWifiManager = (WifiManager)getSystemService("wifi");
    CMService.mConnectivityManager = (ConnectivityManager)getSystemService("connectivity");
    CMService.mContext = getApplicationContext();
    Intent intent = getIntent();
    if (intent != null) {
      String str = intent.getAction();
      Trace.d(CMConstants.TAG_NAME, "onCreate, getAction : " + str);
      if ("android.nfc.action.NDEF_DISCOVERED".equals(str)) {
        Trace.d(CMConstants.TAG_NAME, "onCreate, NDEF_DISCOVERED");
        processTag(intent);
        createContent();
        showWifiManagerDialog();
        return;
      } 
    } else {
      return;
    } 
    showInitDialog();
  }
  
  public void checkTestMode() {
    if (CMUtil.isTestMode(getApplicationContext())) {
      String str2 = CMSharedPreferenceUtil.getString((Context)this, "com.samsungimaging.connectionmanager.TESTMODE_SSID", "");
      Trace.d(CMConstants.TAG_NAME, "checkTestMode(), ssid_temp = " + str2);
      String str1 = str2;
      if (str2.length() != 0) {
        str1 = str2;
        if (str2.equals("NONE"))
          str1 = ""; 
      } 
      String str3 = CMSharedPreferenceUtil.getString((Context)this, "com.samsungimaging.connectionmanager.TESTMODE_SSID2", "");
      Trace.d(CMConstants.TAG_NAME, "checkTestMode(), ssid_temp_2 = " + str3);
      str2 = str3;
      if (str3.length() != 0) {
        str2 = str3;
        if (str3.equals("NONE"))
          str2 = ""; 
      } 
      String str4 = CMSharedPreferenceUtil.getString((Context)this, "com.samsungimaging.connectionmanager.TESTMODE_SSID3", "");
      Trace.d(CMConstants.TAG_NAME, "checkTestMode(), ssid_temp_3 = " + str4);
      str3 = str4;
      if (str4.length() != 0) {
        str3 = str4;
        if (str4.equals("NONE"))
          str3 = ""; 
      } 
      ArrayList<String> arrayList = new ArrayList();
      arrayList.add(str1);
      arrayList.add(str2);
      arrayList.add(str3);
      str1 = "";
      int i = 0;
      while (true) {
        if (i >= arrayList.size()) {
          Trace.d(CMConstants.TAG_NAME, "checkTestMode(), title = " + str1);
          CMUtil.setdscPrefix_fortest((Context)this, arrayList);
          this.mDescription.setText(this.mDescription.getText() + " " + str1);
          return;
        } 
        str3 = arrayList.get(i);
        str2 = str1;
        if (str3.length() != 0) {
          str2 = str1;
          if (!str3.equals("NONE"))
            str2 = String.valueOf(str1) + "(" + str3 + ")"; 
        } 
        i++;
        str1 = str2;
      } 
    } 
  }
  
  public void dismissAllDialog() {
    if (this.mDialogList != null) {
      CustomDialog customDialog = (CustomDialog)this.mDialogList.get(1000);
      if (customDialog != null && customDialog.isShowing())
        customDialog.dismiss(); 
      customDialog = (CustomDialog)this.mDialogList.get(1001);
      if (customDialog != null && customDialog.isShowing())
        customDialog.dismiss(); 
      customDialog = (CustomDialog)this.mDialogList.get(1003);
      if (customDialog != null && customDialog.isShowing())
        customDialog.dismiss(); 
      CustomProgressDialog customProgressDialog = (CustomProgressDialog)this.mDialogList.get(1008);
      if (customProgressDialog != null && customProgressDialog.isShowing())
        customProgressDialog.dismiss(); 
    } 
  }
  
  public void killProcess() {
    try {
      (new Thread(new Runnable() {
            public void run() {
              while (true) {
                for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : am.getRunningAppProcesses()) {
                  if (runningAppProcessInfo.processName.equals((Main.this.getApplicationInfo()).processName)) {
                    runningAppProcessInfo.importance = 500;
                    Trace.d(CMConstants.TAG_NAME, "killBackgroundProcesses");
                    am.killBackgroundProcesses(Main.this.getPackageName());
                    continue;
                  } 
                  Thread.yield();
                } 
              } 
            }
          }"CM")).start();
      Trace.d(CMConstants.TAG_NAME, "killProcess");
      Process.killProcess(Process.myPid());
      return;
    } catch (Throwable throwable) {
      throwable.printStackTrace();
      return;
    } 
  }
  
  public void onBackPressed() {
    if (isBackPressAvailable()) {
      Trace.d(CMConstants.TAG_NAME, "onBackPressed(), ACTIVE_SERVICE = " + CMService.ACTIVE_SERVICE);
      if (!CMService.getInstance().isSubAppAlive())
        CMService.getInstance().finishSafe(); 
    } 
  }
  
  public void onClick(View paramView) {
    showWifiManagerDialog();
  }
  
  protected void onCreate(Bundle paramBundle) {
    CMSharedPreferenceUtil.put(getApplicationContext(), "com.samsungimaging.connectionmanager.TESTMODE", String.valueOf(this.mSSID_Fixed_Mode_For_Test));
    checkModeServerPort();
    super.onCreate(paramBundle);
    Trace.d(CMConstants.TAG_NAME, "onCreate");
    s_obj = this;
    if (Build.VERSION.SDK_INT >= 23) {
      checkRunTimePermission();
      return;
    } 
    callMethod();
  }
  
  protected Dialog onCreateDialog(int paramInt, Bundle paramBundle) {
    IntroNoticeDialog introNoticeDialog;
    CustomDialog customDialog;
    ArrayList<String> arrayList;
    RefusalDialog refusalDialog;
    ArrayList<Drawable> arrayList1;
    switch (paramInt) {
      default:
        return super.onCreateDialog(paramInt, paramBundle);
      case 1000:
        return (Dialog)new SearchAPDialog((Activity)this);
      case 1001:
        return (Dialog)new ModeClientDialog((Context)this);
      case 1002:
        return (Dialog)new IntroDialog((Context)this);
      case 1003:
        return (Dialog)new InitGuideDialog((Context)this);
      case 1004:
        return (Dialog)new InitHelpDialog((Context)this);
      case 1005:
        return (Dialog)new VerifyingPoorLinkDialog((Context)this);
      case 1010:
        introNoticeDialog = new IntroNoticeDialog((BaseActivity)this);
        ((CustomDialog)introNoticeDialog).setNeutralButton(2131361817, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                param1DialogInterface.dismiss();
                Main.this.showInitNFCDialog();
              }
            });
        return (Dialog)introNoticeDialog;
      case 1113:
        customDialog = new CustomDialog((Context)this);
        customDialog.setMessage(getString(2131362077, new Object[] { getString(2131361800) }));
        arrayList = new ArrayList();
        arrayList1 = new ArrayList();
        if (ContextCompat.checkSelfPermission((Context)this, "android.permission.ACCESS_FINE_LOCATION") != 0) {
          arrayList.add(getPermissionGroupString(getApplicationContext(), "android.permission.ACCESS_FINE_LOCATION"));
          arrayList1.add(getPermissionGroupDrawable(getApplicationContext(), "android.permission.ACCESS_FINE_LOCATION"));
        } 
        if (ContextCompat.checkSelfPermission((Context)this, "android.permission.READ_PHONE_STATE") != 0) {
          arrayList.add(getPermissionGroupString(getApplicationContext(), "android.permission.READ_PHONE_STATE"));
          arrayList1.add(getPermissionGroupDrawable(getApplicationContext(), "android.permission.READ_PHONE_STATE"));
        } 
        if (ContextCompat.checkSelfPermission((Context)this, "android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
          arrayList.add(getPermissionGroupString(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE"));
          arrayList1.add(getPermissionGroupDrawable(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE"));
        } 
        customDialog.setAdapter((ListAdapter)new CustomAdapter(this, arrayList, arrayList1));
        customDialog.setPositiveButton(getResources().getString(2131362076), new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                try {
                  Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                  intent.setData(Uri.parse("package:com.samsungimaging.connectionmanager"));
                  Main.this.startActivity(intent);
                  Main.this.dismissDialog(1113);
                  Main.this.finish();
                  return;
                } catch (ActivityNotFoundException activityNotFoundException) {
                  Intent intent = new Intent("android.settings.MANAGE_APPLICATIONS_SETTINGS");
                  Main.this.startActivity(intent);
                  Main.this.dismissDialog(1113);
                  return;
                } 
              }
            });
        customDialog.setNegativeButton(2131362075, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                Main.this.dismissDialog(1113);
                Main.this.finish();
              }
            });
        customDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                if (param1KeyEvent.getKeyCode() == 84)
                  return true; 
                if (param1KeyEvent.getKeyCode() == 4) {
                  Main.this.dismissDialog(1113);
                  Main.this.finish();
                  return true;
                } 
                return false;
              }
            });
        return (Dialog)customDialog;
      case 1006:
        refusalDialog = new RefusalDialog((Context)this);
        ((CustomDialog)refusalDialog).setTag(customDialog.getString("data"));
        ((CustomDialog)refusalDialog).setNeutralButton(2131361842, null);
        return (Dialog)refusalDialog;
      case 1007:
        return (Dialog)new NotSupportedDialog((Context)this);
      case 1008:
        return (Dialog)new NowAppClosingProgressDialog((Context)this);
      case 1009:
        return (Dialog)new CMTestModePassWordDialog((Context)this, (Activity)this);
      case 1011:
        break;
    } 
    return (Dialog)new CMTestModeDialog((Context)this, 16973837);
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu) {
    super.onCreateOptionsMenu(paramMenu);
    if (CMUtil.isTestMode(getApplicationContext()))
      paramMenu.findItem(2131558732).setVisible(true); 
    return true;
  }
  
  protected void onDestroy() {
    Trace.d(CMConstants.TAG_NAME, "onDestroy");
    super.onDestroy();
    CMNotificationManager.getInstance().cancelAllNoti();
    if (CMUtil.isForceClose((ActivityManager)getSystemService("activity")))
      SystemClock.sleep(3000L); 
    CMService.mWifiManager = (WifiManager)getSystemService("wifi");
    CMService.mWifiManager.disconnect();
    killProcess();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    return keyDown(paramInt, paramKeyEvent) ? true : super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    return keyUp(paramInt, paramKeyEvent) ? true : super.onKeyUp(paramInt, paramKeyEvent);
  }
  
  protected void onNewIntent(Intent paramIntent) {
    Trace.d(CMConstants.TAG_NAME, "onNewIntent, intent.getAction() = " + paramIntent.getAction());
    if ("android.nfc.action.NDEF_DISCOVERED".equals(paramIntent.getAction())) {
      processTag(paramIntent);
      showWifiManagerDialog();
      return;
    } 
    Trace.d(CMConstants.TAG_NAME, "onNewIntent, CMService.IS_WIFI_CONNECTED = " + CMService.IS_WIFI_CONNECTED + ", CMService.ACTIVE_SERVICE = " + CMService.ACTIVE_SERVICE);
    if (CMService.IS_WIFI_CONNECTED && CMService.getInstance().isSubAppAlive()) {
      showWifiManagerDialog();
      return;
    } 
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
    super.onOptionsItemSelected(paramMenuItem);
    switch (paramMenuItem.getItemId()) {
      default:
        return true;
      case 2131558732:
        break;
    } 
    showDialog(1009);
  }
  
  protected void onPause() {
    super.onPause();
  }
  
  @SuppressLint({"Override"})
  public void onRequestPermissionsResult(int paramInt, String[] paramArrayOfString, int[] paramArrayOfint) {
    switch (paramInt) {
      default:
        return;
      case 1:
        break;
    } 
    SharedPreferences sharedPreferences = getSharedPreferences("configuration", 0);
    boolean bool = sharedPreferences.getBoolean("firstTimeAccount", true);
    HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
    hashMap.put("android.permission.ACCESS_FINE_LOCATION", Integer.valueOf(0));
    hashMap.put("android.permission.WRITE_EXTERNAL_STORAGE", Integer.valueOf(0));
    hashMap.put("android.permission.READ_PHONE_STATE", Integer.valueOf(0));
    if (paramArrayOfint.length > 0) {
      for (paramInt = 0;; paramInt++) {
        SharedPreferences.Editor editor;
        if (paramInt >= paramArrayOfString.length) {
          if (((Integer)hashMap.get("android.permission.ACCESS_FINE_LOCATION")).intValue() == 0 && ((Integer)hashMap.get("android.permission.WRITE_EXTERNAL_STORAGE")).intValue() == 0 && ((Integer)hashMap.get("android.permission.READ_PHONE_STATE")).intValue() == 0) {
            editor = sharedPreferences.edit();
            editor.putBoolean("firstTimeAccount", true);
            editor.commit();
            callMethod();
            return;
          } 
          break;
        } 
        hashMap.put(editor[paramInt], Integer.valueOf(paramArrayOfint[paramInt]));
      } 
      if (!bool) {
        showDialog(1113);
        return;
      } 
      bool = false;
      Iterator<String> iterator = this.deniedPermissions.iterator();
      while (true) {
        if (iterator.hasNext()) {
          boolean bool1 = ActivityCompat.shouldShowRequestPermissionRationale((Activity)this, iterator.next());
          bool = bool1;
          if (bool1) {
            bool = bool1;
          } else {
            continue;
          } 
        } 
        if (!bool) {
          SharedPreferences.Editor editor = sharedPreferences.edit();
          editor.putBoolean("firstTimeAccount", false);
          editor.commit();
          finish();
          return;
        } 
        finish();
        return;
      } 
    } 
  }
  
  protected void onResume() {
    super.onResume();
    Trace.d(CMConstants.TAG_NAME, "onResume");
  }
  
  public void sendByebyeForLowBattery() {
    (new Thread(new Runnable() {
          public void run() {
            boolean bool = CMUtil.checkOldVersionSmartCameraApp(CMInfo.getInstance().getConnectedSSID());
            Trace.d(CMConstants.TAG_NAME, "sendByebyeForLowBattery, old camera= " + bool + ", WIFI CONNECTED = " + CMService.IS_WIFI_CONNECTED);
            if (CMService.IS_WIFI_CONNECTED && !bool) {
              CMModeManager.runModeClient(Main.this.mIModeClientForSendingByebye, true);
              int i = 0;
              while (true) {
                Trace.d(CMConstants.TAG_NAME, "sendByebyeForLowBattery, run... count = " + i);
                if (!CMService.IS_WIFI_CONNECTED) {
                  Trace.d(CMConstants.TAG_NAME, "sendByebyeForLowBattery, run...break01");
                } else if (i == 12) {
                  Trace.d(CMConstants.TAG_NAME, "sendByebyeForLowBattery, run...break02");
                } else {
                  i++;
                  SystemClock.sleep(500L);
                  continue;
                } 
                CMModeManager.stopModeClient();
                CMUtil.disableConnectedCamera(CMService.mWifiManager);
                CMNotificationManager.getInstance().notifyStatusChange(false, null, Main.this.getResources().getString(2131362058).toString(), 2131361840);
                CMService.IS_WIFI_CONNECTED = false;
                CMService.IS_MODE_CONNECTED = false;
                return;
              } 
            } 
            CMModeManager.stopModeClient();
            CMUtil.disableConnectedCamera(CMService.mWifiManager);
            CMNotificationManager.getInstance().notifyStatusChange(false, null, Main.this.getResources().getString(2131362058).toString(), 2131361840);
            CMService.IS_WIFI_CONNECTED = false;
            CMService.IS_MODE_CONNECTED = false;
          }
        })).start();
  }
  
  public class CustomAdapter extends BaseAdapter {
    Context context;
    
    List<Drawable> imageId;
    
    private LayoutInflater inflater = null;
    
    List<String> result;
    
    View rowView;
    
    public CustomAdapter(Main param1Main1, List<String> param1List, List<Drawable> param1List1) {
      this.result = param1List;
      this.context = (Context)param1Main1;
      this.imageId = param1List1;
      this.inflater = (LayoutInflater)this.context.getSystemService("layout_inflater");
    }
    
    public int getCount() {
      return this.result.size();
    }
    
    public Object getItem(int param1Int) {
      return Integer.valueOf(param1Int);
    }
    
    public long getItemId(int param1Int) {
      return param1Int;
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      if (param1View == null) {
        param1View = this.inflater.inflate(2130903108, null);
        ViewHolder viewHolder1 = new ViewHolder();
        viewHolder1.tv = (TextView)param1View.findViewById(2131558716);
        viewHolder1.img = (ImageView)param1View.findViewById(2131558715);
        param1View.setTag(viewHolder1);
        viewHolder1.tv.setText(this.result.get(param1Int));
        viewHolder1.img.setImageDrawable(this.imageId.get(param1Int));
        return param1View;
      } 
      ViewHolder viewHolder = (ViewHolder)param1View.getTag();
      viewHolder.tv.setText(this.result.get(param1Int));
      viewHolder.img.setImageDrawable(this.imageId.get(param1Int));
      return param1View;
    }
    
    public class ViewHolder {
      ImageView img;
      
      TextView tv;
    }
  }
  
  public class ViewHolder {
    ImageView img;
    
    TextView tv;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\Main.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */