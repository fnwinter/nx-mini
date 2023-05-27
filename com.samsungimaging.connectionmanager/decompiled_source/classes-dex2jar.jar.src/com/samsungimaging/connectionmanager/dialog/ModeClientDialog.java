package com.samsungimaging.connectionmanager.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.app.cm.Interface.IModeClient;
import com.samsungimaging.connectionmanager.app.cm.common.CMConstants;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.app.cm.modemanager.CMModeManager;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;
import com.samsungimaging.connectionmanager.util.Trace;

public class ModeClientDialog extends CustomDialog {
  private String mConnected_SSID = null;
  
  private IModeClient mIModeClient = new IModeClient() {
      public void runByebye() {}
      
      public void runSubApplication(int param1Int) {
        boolean bool;
        Trace.d(CMConstants.TAG_NAME, "ModeClientDialog, IModeClient, runSubApplication() => " + param1Int);
        switch (param1Int) {
          default:
            return;
          case 1:
          case 2:
          case 3:
          case 4:
          case 6:
          case 7:
            bool = CMUtil.checkOldVersionSmartCameraApp(ModeClientDialog.this.mConnected_SSID);
            Trace.d(CMConstants.TAG_NAME, "ModeClientDialog, IModeClient, ONLY old Camera, service execution supported by ModeClient. is OldCamera? = " + bool);
            if (bool) {
              CMService.getInstance().startSubApplication(param1Int);
              return;
            } 
            CMService.IS_MODE_CONNECTED = true;
            return;
          case 5:
            CMService.getInstance().startSubApplication(param1Int);
            return;
          case 21:
            ModeClientDialog.this.beforeFinish();
            ModeClientDialog.this.dismiss();
            CMUtil.sendBroadCastToMain(ModeClientDialog.this.getContext(), "CHECK_FOR_401_ERROR");
            return;
          case 22:
            ModeClientDialog.this.beforeFinish();
            ModeClientDialog.this.dismiss();
            CMUtil.sendBroadCastToMain(ModeClientDialog.this.getContext(), "CHECK_FOR_503_ERROR");
            return;
          case 0:
            break;
        } 
        ModeClientDialog.this.beforeFinish();
        ModeClientDialog.this.dismiss();
        CMUtil.sendBroadCastToMain(ModeClientDialog.this.getContext(), "CHECK_FOR_MODECLIENT_NOTRESPONSE_OR_CANCELED");
      }
    };
  
  private boolean mOldCameraDesc = false;
  
  private DialogInterface.OnClickListener mOnClickListener;
  
  private WifiManager mWifiManager = null;
  
  public ModeClientDialog(Context paramContext) {
    super(paramContext);
  }
  
  private void beforeFinish() {
    Trace.d(CMConstants.TAG_NAME, "ModeClientDialog, beforeFinish");
    CMModeManager.stopModeClient();
    CMUtil.disableConnectedCamera(this.mWifiManager);
    CMService.IS_WIFI_CONNECTED = false;
    CMService.IS_MODE_CONNECTED = false;
    CMService.getInstance().NFCConnectionTryCancel();
  }
  
  private void initContent() {
    Trace.d(CMConstants.TAG_NAME, "ModeClientDialog, initContent");
    setView(2130903059);
    this.mOnClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface param1DialogInterface, int param1Int) {
          ModeClientDialog.this.beforeFinish();
          ModeClientDialog.this.dismiss();
        }
      };
    setNeutralButton(2131361817, this.mOnClickListener);
  }
  
  public void onBackPressed() {
    beforeFinish();
    dismiss();
    super.onBackPressed();
  }
  
  protected void onCreate(Bundle paramBundle) {
    Trace.d(CMConstants.TAG_NAME, "ModeClientDialog, onCreate");
    this.mWifiManager = (WifiManager)getContext().getSystemService("wifi");
    initContent();
    super.onCreate(paramBundle);
    TextView textView = (TextView)findViewById(2131558554);
    textView.setText(textView.getText() + CMUtil.getUseragent(getContext()));
    if (CMUtil.isTestMode(getContext()))
      ((LinearLayout)findViewById(2131558538)).setVisibility(4); 
  }
  
  protected void onStart() {
    super.onStart();
    Trace.d(CMConstants.TAG_NAME, "ModeClientDialog, onStart");
    this.mConnected_SSID = this.mWifiManager.getConnectionInfo().getSSID();
    if (!"107".equals(CMUtil.checkCClass(this.mWifiManager)) || this.mConnected_SSID.contains("WB350"))
      this.mOldCameraDesc = true; 
    if (this.mOldCameraDesc) {
      if (CMInfo.getInstance().getIsNFCLaunch()) {
        ((TextView)findViewById(2131558552)).setText(2131361880);
      } else {
        ((TextView)findViewById(2131558552)).setText(2131361860);
      } 
    } else {
      ((TextView)findViewById(2131558552)).setText(2131361860);
    } 
    CMUtil.sendBroadCastToMain(getContext(), "STOP_WIFI_SCAN_TIMER_FOR_MIMUTES");
    CMModeManager.runModeClient(this.mIModeClient, false);
  }
  
  protected void onStop() {
    super.onStop();
    Trace.d(CMConstants.TAG_NAME, "ModeClientDialog, onStop");
    CMModeManager.stopModeClient();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\ModeClientDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */