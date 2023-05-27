package com.samsungimaging.connectionmanager.app.pullservice;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.app.BaseActivity;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;
import com.samsungimaging.connectionmanager.app.pullservice.controller.DeviceController;
import com.samsungimaging.connectionmanager.app.pullservice.controller.UPNPController;
import com.samsungimaging.connectionmanager.app.pullservice.util.Utils;
import com.samsungimaging.connectionmanager.dialog.ConnectionWaitDialog;
import com.samsungimaging.connectionmanager.dialog.CustomDialog;
import com.samsungimaging.connectionmanager.dialog.CustomProgressDialog;
import com.samsungimaging.connectionmanager.util.ImageCache;
import com.samsungimaging.connectionmanager.util.Trace;
import org.cybergarage.upnp.Device;

public abstract class PullService extends BaseActivity implements View.OnTouchListener {
  private BroadcastReceiver BroadcastInfoReceiver = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        NetworkInfo.DetailedState detailedState;
        Trace.d(PullService.this.TAG, "start onReceive() action : " + param1Intent.getAction());
        String str = param1Intent.getAction();
        if ("android.intent.action.BATTERY_CHANGED".equals(str)) {
          int i = param1Intent.getIntExtra("level", -1);
          if (i != -1 && i <= 4) {
            Trace.d(PullService.this.TAG, "Battery level : " + i);
            if (PullService.this.handler.hasMessages(2001))
              PullService.this.dismissDialog(2001); 
            PullService.this.upnpController.disconnect();
            PullService.this.showDialog(2003);
          } 
          return;
        } 
        if ("android.net.wifi.STATE_CHANGE".equals(str)) {
          detailedState = ((NetworkInfo)param1Intent.getParcelableExtra("networkInfo")).getDetailedState();
          switch (detailedState) {
            default:
              Trace.d(PullService.this.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - Unknown");
              return;
            case CONNECTING:
              Trace.d(PullService.this.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - CONNECTING");
              return;
            case OBTAINING_IPADDR:
              Trace.d(PullService.this.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - OBTAINING_IPADDR");
              return;
            case CONNECTED:
              Trace.d(PullService.this.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - CONNECTED");
              return;
            case FAILED:
              Trace.d(PullService.this.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - FAILED");
              return;
            case DISCONNECTING:
              Trace.d(PullService.this.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - DISCONNECTING");
              return;
            case DISCONNECTED:
              break;
          } 
          Trace.d(PullService.this.TAG, "WifiManager.NETWORK_STATE_CHANGED_ACTION - DISCONNECTED");
          PullService.this.showDialog(2006);
          PullService.this.handler.sendEmptyMessageDelayed(202, 500L);
          return;
        } 
        if ("android.net.wifi.supplicant.CONNECTION_CHANGE".equals(detailedState) && !param1Intent.getBooleanExtra("connected", false)) {
          PullService.this.showDialog(2006);
          PullService.this.handler.sendEmptyMessageDelayed(202, 500L);
          return;
        } 
      }
    };
  
  private Trace.Tag TAG = Trace.Tag.COMMON;
  
  private boolean bCloseByFinishSafe = false;
  
  protected String connectedSsid = null;
  
  private Handler handler;
  
  protected CustomDialog mConfirmDialog = null;
  
  protected DeviceController mDeviceController = null;
  
  private ImageCache mImageCacheThumbnail = null;
  
  private ImageCache mImageCacheViewer = null;
  
  private String prefixAgent = "SEC_RVF_ML_";
  
  protected UPNPController upnpController;
  
  private WifiManager wifiManager = null;
  
  private void closeDiskCache() {
    if (this.mImageCacheThumbnail != null) {
      this.mImageCacheThumbnail.closeCache();
      this.mImageCacheThumbnail = null;
    } 
    if (this.mImageCacheViewer != null) {
      this.mImageCacheViewer.closeCache();
      this.mImageCacheViewer = null;
    } 
  }
  
  private void init() {
    this.handler = new Handler() {
        public void handleMessage(Message param1Message) {
          String str;
          Bundle bundle;
          Trace.d(PullService.this.TAG, "start handleMessage() msg.what : " + param1Message.what);
          switch (param1Message.what) {
            default:
              return;
            case 100:
              PullService.this.handler.removeMessages(200);
              PullService.this.dismissDialog(2001);
              PullService.this.onAlive();
              return;
            case 101:
              PullService.this.onUnsubscribe();
              return;
            case 118:
              PullService.this.handler.removeMessages(200);
              PullService.this.dismissDialog(2001);
              str = (String)param1Message.obj;
              Trace.d(PullService.this.TAG, "rvf message : " + str);
              bundle = new Bundle();
              if (str.contains("HTTP_UNAUTHORIZED")) {
                bundle.putString("DIALOG_MESSAGE_KEY", PullService.this.getString(2131361829));
              } else if (str.contains("HTTP_UNAVAILABLE")) {
                bundle.putString("DIALOG_MESSAGE_KEY", PullService.this.getString(2131361830));
              } 
              PullService.this.showDialog(2004, bundle);
              return;
            case 200:
              PullService.this.showDialog(3000);
              PullService.this.handler.sendEmptyMessageDelayed(201, 500L);
              return;
            case 201:
              PullService.this.dismissDialog(3000);
              PullService.this.onUnsubscribe();
              return;
            case 202:
              break;
          } 
          PullService.this.onClose();
        }
      };
    this.wifiManager = (WifiManager)getApplicationContext().getSystemService("wifi");
    this.connectedSsid = CMInfo.getInstance().getConnectedSSID();
    this.upnpController = UPNPController.getInstance();
    this.upnpController.setDeviceNotifyEventHandler(this.handler);
    this.mDeviceController = this.upnpController.getDeviceController();
  }
  
  private void initDiskCache() {
    this.mImageCacheThumbnail = new ImageCache(getApplicationContext(), "thumbnail");
    this.mImageCacheViewer = new ImageCache(getApplicationContext(), "viewer");
    this.mImageCacheThumbnail.initDiskCache();
    this.mImageCacheViewer.initDiskCache();
  }
  
  private void registerBroadcastReceiver() {
    IntentFilter intentFilter = new IntentFilter("android.intent.action.BATTERY_CHANGED");
    intentFilter.addAction("android.net.wifi.STATE_CHANGE");
    intentFilter.addAction("android.net.wifi.supplicant.CONNECTION_CHANGE");
    registerReceiver(this.BroadcastInfoReceiver, intentFilter);
  }
  
  private void unregisterBroadcastReceiver() {
    try {
      unregisterReceiver(this.BroadcastInfoReceiver);
      return;
    } catch (Exception exception) {
      exception.printStackTrace();
      return;
    } 
  }
  
  public void CheckDirtyCache(String paramString) {
    if (this.mImageCacheThumbnail != null && this.mImageCacheThumbnail.hasSnapshotInDiskCache(paramString)) {
      Trace.i(this.TAG, "CheckDirtyCache remove Thumbnail Cache : " + paramString);
      this.mImageCacheThumbnail.remove(paramString);
    } 
    if (this.mImageCacheViewer != null && this.mImageCacheViewer.hasSnapshotInDiskCache(paramString)) {
      Trace.i(this.TAG, "CheckDirtyCache remove Viewer Cache : " + paramString);
      this.mImageCacheViewer.remove(paramString);
    } 
  }
  
  public abstract void closeService();
  
  protected String getConnectedSSID() {
    Trace.d(this.TAG, "start getConnectedSSID()");
    return this.connectedSsid;
  }
  
  public abstract void onAlive();
  
  public void onBackPressed() {
    Trace.d(this.TAG, "start PullService.onBackPressed()");
    if (isBackPressAvailable()) {
      unregisterBroadcastReceiver();
      onUnsubscribe();
    } 
  }
  
  protected void onClose() {
    Trace.d(this.TAG, "start onClose()");
    this.upnpController.disconnect();
    closeService();
    if (this.bCloseByFinishSafe) {
      CMService.getInstance().beforefinish(2);
      CMService.getInstance().finishSafe();
    } 
  }
  
  protected void onCreate(Bundle paramBundle) {
    Trace.d(this.TAG, "start PullService.onCreate()");
    super.onCreate(paramBundle);
    registerBroadcastReceiver();
    init();
    initDiskCache();
    int i = registerReceiver(this.BroadcastInfoReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra("level", -1);
    if (i != -1 && i <= 4) {
      showDialog(2003);
      return;
    } 
    if (!this.upnpController.isConnected()) {
      String str2;
      this.handler.sendEmptyMessageDelayed(200, 180000L);
      showDialog(2001);
      boolean bool = CMUtil.checkOldVersionSmartCameraApp(this.connectedSsid);
      i = 1900;
      String str1 = "SEC_RVF_ML_";
      if (bool) {
        String str;
        if (CMInfo.getInstance().getIsNFCLaunchFlag()) {
          str = "nfc";
        } else {
          str = "manual";
        } 
        if (CMService.ACTIVE_SERVICE == 2) {
          i = 1900;
          str1 = "SEC_RVF_";
          str2 = str;
        } else {
          str2 = str;
          if (CMService.ACTIVE_SERVICE == 1) {
            i = 1901;
            str1 = "SEC_DSC_";
            str2 = str;
          } 
        } 
      } else {
        str2 = "manual";
        i = 1900;
        str1 = "SEC_RVF_ML_";
      } 
      this.upnpController.connect(Utils.getAgent(getApplicationContext(), str1), str2, i);
      return;
    } 
  }
  
  protected Dialog onCreateDialog(int paramInt, Bundle paramBundle) {
    ConnectionWaitDialog connectionWaitDialog;
    CustomDialog customDialog3;
    TextView textView;
    CustomDialog customDialog2;
    CustomProgressDialog customProgressDialog;
    View view;
    Button button;
    Trace.d(this.TAG, "start onCreateDialog() id : " + paramInt);
    switch (paramInt) {
      default:
        return super.onCreateDialog(paramInt, paramBundle);
      case 2000:
        this.mConfirmDialog = new CustomDialog((Context)this);
        this.mConfirmDialog.setIcon(2130838178);
        this.mConfirmDialog.setTitle(2131361934);
        this.mConfirmDialog.setMessage(paramBundle.getString("DIALOG_MESSAGE_KEY"));
        this.mConfirmDialog.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                PullService.this.dismissDialog(2000);
              }
            });
        return (Dialog)this.mConfirmDialog;
      case 2001:
        connectionWaitDialog = new ConnectionWaitDialog((Context)this);
        connectionWaitDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                switch (param1Int) {
                  default:
                    return false;
                  case 4:
                    break;
                } 
                PullService.this.handler.removeMessages(200);
                PullService.this.onUnsubscribe();
                PullService.this.wifiManager.disconnect();
              }
            });
        connectionWaitDialog.setNeutralButton(2131361817, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                PullService.this.handler.removeMessages(200);
                PullService.this.onUnsubscribe();
                PullService.this.wifiManager.disconnect();
              }
            });
        return (Dialog)connectionWaitDialog;
      case 2002:
        customDialog3 = new CustomDialog((Context)this);
        customDialog3.setIcon(2130838178);
        customDialog3.setTitle(2131361821);
        customDialog3.setMessage(2131361939);
        customDialog3.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                PullService.this.onUnsubscribe();
              }
            });
        customDialog3.setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                switch (param1Int) {
                  default:
                    return false;
                  case 4:
                  case 84:
                    break;
                } 
                return true;
              }
            });
        return (Dialog)customDialog3;
      case 2003:
        customDialog3 = new CustomDialog((Context)this);
        customDialog3.setIcon(2130838178);
        customDialog3.setTitle(2131361821);
        customDialog3.setMessage(2131361936);
        customDialog3.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                PullService.this.bCloseByFinishSafe = true;
                PullService.this.onUnsubscribe();
              }
            });
        customDialog3.setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                switch (param1Int) {
                  default:
                    return false;
                  case 4:
                    break;
                } 
                PullService.this.bCloseByFinishSafe = true;
                PullService.this.onUnsubscribe();
              }
            });
        return (Dialog)customDialog3;
      case 2004:
        view = getLayoutInflater().inflate(2130903110, null);
        button = (Button)view.findViewById(2131558721);
        ((TextView)view.findViewById(2131558719)).setText(customDialog3.getString("DIALOG_MESSAGE_KEY"));
        textView = (TextView)view.findViewById(2131558720);
        textView.setText(textView.getText() + CMInfo.getInstance().getConnectedSSID());
        customDialog2 = new CustomDialog((Context)this);
        customDialog2.setView(view);
        customDialog2.setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                switch (param1Int) {
                  default:
                    return false;
                  case 4:
                    break;
                } 
                CMService.getInstance().beforefinish(0);
                return false;
              }
            });
        button.setOnClickListener(new View.OnClickListener() {
              public void onClick(View param1View) {
                CMService.getInstance().beforefinish(0);
              }
            });
        return (Dialog)customDialog2;
      case 2005:
        customDialog2 = new CustomDialog((Context)this);
        customDialog2.setIcon(2130837538);
        customDialog2.setTitle(2131361821);
        customDialog2.setMessage(2131361900);
        customDialog2.setPositiveButton(17039379, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                PullService.this.showDialog(3000);
                PullService.this.onUnsubscribe();
              }
            });
        customDialog2.setNegativeButton(17039369, null);
        customDialog2.setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                switch (param1Int) {
                  default:
                    return false;
                  case 4:
                    break;
                } 
                return true;
              }
            });
        return (Dialog)customDialog2;
      case 2006:
        customDialog2 = new CustomDialog((Context)this);
        customDialog2.setIcon(2130838178);
        customDialog2.setTitle(2131361821);
        customDialog2.setMessage(2131361824);
        customDialog2.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                PullService.this.onClose();
              }
            });
        customDialog2.setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                switch (param1Int) {
                  default:
                    return false;
                  case 4:
                    break;
                } 
                return true;
              }
            });
        return (Dialog)customDialog2;
      case 3000:
        customProgressDialog = new CustomProgressDialog((Context)this);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setProgressStyle(0);
        customProgressDialog.setMessage(getString(2131361956));
        return (Dialog)customProgressDialog;
      case 3001:
        customProgressDialog = new CustomProgressDialog((Context)this);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setProgressStyle(0);
        customProgressDialog.setMessage(getString(2131362064));
        return (Dialog)customProgressDialog;
      case 2011:
        break;
    } 
    CustomDialog customDialog1 = new CustomDialog((Context)this);
    customDialog1.setMessage(2131362074);
    customDialog1.setCancelable(false);
    customDialog1.setNeutralButton(17039370, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            PullService.this.dismissDialog(3001);
            PullService.this.dismissDialog(2011);
          }
        });
    return (Dialog)customDialog1;
  }
  
  protected void onDestroy() {
    Trace.d(this.TAG, "start PullService.onDestroy()");
    super.onDestroy();
    closeDiskCache();
    unregisterBroadcastReceiver();
  }
  
  protected void onPause() {
    Trace.d(this.TAG, "start PullService.onPause()");
    super.onPause();
  }
  
  protected void onResume() {
    Trace.d(this.TAG, "start PullService.onResume()");
    super.onResume();
  }
  
  protected void onStart() {
    Trace.d(this.TAG, "start PullService.onStart()");
    super.onStart();
  }
  
  protected void onStop() {
    Trace.d(this.TAG, "start PullService.onStop()");
    super.onStop();
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
    Trace.d(this.TAG, "start PullService.onTouch()");
    return false;
  }
  
  protected void onUnsubscribe() {
    Trace.d(this.TAG, "start onUnsubscribe()");
    this.upnpController.setDeviceNotifyEventHandler(null);
    (new UnsubscribeTask(null)).execute((Object[])new Device[] { this.upnpController.getConnectedDevice() });
  }
  
  private class UnsubscribeTask extends AsyncTask<Device, Void, Void> {
    private UnsubscribeTask() {}
    
    protected Void doInBackground(Device... param1VarArgs) {
      Trace.d(PullService.this.TAG, "start UnsubscribeTask.doInBackground() isConnected : " + PullService.this.upnpController.isConnected());
      if (!PullService.this.prefixAgent.equals("SEC_RVF_") && PullService.this.upnpController != null && PullService.this.upnpController.isConnected()) {
        PullService.this.upnpController.unsubscribe(param1VarArgs[0]);
        return null;
      } 
      PullService.this.handler.removeMessages(200);
      return null;
    }
    
    protected void onPostExecute(Void param1Void) {
      Trace.d(PullService.this.TAG, "start UnsubscribeTask.onPostExecute()");
      PullService.this.onClose();
      super.onPostExecute(param1Void);
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\PullService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */