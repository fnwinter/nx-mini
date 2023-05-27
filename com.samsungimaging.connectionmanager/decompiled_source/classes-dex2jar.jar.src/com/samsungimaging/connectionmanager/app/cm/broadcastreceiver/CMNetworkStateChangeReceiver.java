package com.samsungimaging.connectionmanager.app.cm.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import com.samsungimaging.connectionmanager.app.cm.common.CMConstants;
import com.samsungimaging.connectionmanager.app.cm.common.CMSharedPreferenceUtil;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;
import com.samsungimaging.connectionmanager.util.Trace;

public class CMNetworkStateChangeReceiver extends BroadcastReceiver {
  private String mNewbssid_supplicant_state_changed = "";
  
  private String mNewssid_network_state_changed = "";
  
  private String mNewssid_supplicant_state_changed = "";
  
  private String mNewstate_network_state_changed = "";
  
  private String mNewstate_supplicant_state_changed = "";
  
  private String mOldbssid_supplicant_state_changed = "";
  
  private String mOldssid_network_state_changed = "";
  
  private String mOldssid_supplicant_state_changed = "";
  
  private String mOldstate_network_state_changed = "";
  
  private String mOldstate_supplicant_state_changed = "";
  
  private int mRetryPasswordInputCount = 0;
  
  public void onReceive(Context paramContext, Intent paramIntent) {
    NetworkInfo.DetailedState detailedState;
    String str = paramIntent.getAction();
    Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, action = " + str);
    if (str.equals("android.intent.action.LOCALE_CHANGED")) {
      Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, LOCALE_CHANGED!!!");
      ((WifiManager)paramContext.getSystemService("wifi")).disconnect();
      CMService.getInstance().goToHome();
      return;
    } 
    if (!"true".equalsIgnoreCase(CMSharedPreferenceUtil.getString(paramContext, "com.samsungimaging.connectionmanager.SP_KEY_IS_RECEIVER_RUNNING", "true"))) {
      Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, return04");
      return;
    } 
    if ("android.net.wifi.supplicant.CONNECTION_CHANGE".equals(str)) {
      if (!paramIntent.getBooleanExtra("connected", false)) {
        Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver : finighing by wifi disable on low version android phone");
        CMUtil.sendBroadCastToMain(paramContext, "WIFI_DISCONNECTED");
        return;
      } 
      return;
    } 
    WifiManager wifiManager = (WifiManager)paramContext.getSystemService("wifi");
    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
    this.mNewssid_network_state_changed = wifiInfo.getSSID();
    this.mNewssid_supplicant_state_changed = this.mNewssid_network_state_changed;
    this.mNewbssid_supplicant_state_changed = wifiInfo.getBSSID();
    wifiInfo = null;
    NetworkInfo networkInfo = (NetworkInfo)paramIntent.getParcelableExtra("networkInfo");
    if (networkInfo != null) {
      detailedState = networkInfo.getDetailedState();
      this.mNewstate_network_state_changed = detailedState.toString();
    } 
    int i = Integer.parseInt(Build.VERSION.SDK);
    if ("android.net.conn.CONNECTIVITY_CHANGE".equals(str)) {
      String str1 = networkInfo.getTypeName();
      networkInfo.getSubtypeName();
      boolean bool = networkInfo.isAvailable();
      if ("WIFI".equals(str1) && !bool) {
        if (CMUtil.supportDSCPrefix(this.mNewssid_network_state_changed) && CMUtil.isManagedSSID(paramContext, this.mNewssid_network_state_changed)) {
          Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver----------------1");
        } else {
          Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver----------------2");
        } 
      } else if ("WIFI".equals(str1) && bool) {
        Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver----------------3");
      } 
    } 
    if ("android.net.wifi.STATE_CHANGE".equals(str)) {
      int j = wifiManager.getWifiState();
      Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, wifiOnOffState = " + j);
      if (j == 3) {
        Trace.d(CMConstants.TAG_NAME, "Performance Check Point : Detailed State = " + detailedState);
        if (detailedState == NetworkInfo.DetailedState.CONNECTED) {
          Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, mNewssid = " + this.mNewssid_network_state_changed);
          if (CMUtil.supportDSCPrefix(this.mNewssid_network_state_changed) && CMUtil.isManagedSSID(paramContext, this.mNewssid_network_state_changed)) {
            if (CMService.IS_WIFI_CONNECTED || CMService.getInstance().isSubAppAlive() || CMService.getInstance().isWifiScanStop()) {
              Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, return, IS_WIFI_CONNECTED01 = " + CMService.IS_WIFI_CONNECTED + ", ACTIVE_SERVICE = " + CMService.ACTIVE_SERVICE + ", isWifiScanStop = " + CMService.getInstance().isWifiScanStop());
              return;
            } 
            CMService.IS_WIFI_CONNECTED = true;
            Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, Connected to CamearAP....ok");
            CMService.getInstance().checkSubApplicationType();
          } else {
            Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, IS_WIFI_CONNECTED02 = " + CMService.IS_WIFI_CONNECTED);
            CMService.IS_WIFI_CONNECTED = false;
          } 
        } else {
          Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, apiLevel = " + i);
          Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, mOldstate_network_state_changed = " + this.mOldstate_network_state_changed + ", mNewstate_network_state_changed = " + this.mNewstate_network_state_changed);
          Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, mOldssid_network_state_changed = " + this.mOldssid_network_state_changed + ", mNewssid_network_state_changed = " + this.mNewssid_network_state_changed);
          if (this.mOldstate_network_state_changed.equals("VERIFYING_POOR_LINK") && this.mNewstate_network_state_changed.equals("DISCONNECTED")) {
            String str1 = Build.BRAND;
            String str2 = Build.MANUFACTURER;
            Trace.d(CMConstants.TAG_NAME, "brand = " + str1 + ", manufacturer = " + str2);
            if (!str1.toLowerCase().contains("samsung") && !str2.toLowerCase().contains("samsung") && ((CMUtil.supportDSCPrefix(this.mNewssid_network_state_changed) && CMUtil.isManagedSSID(paramContext, this.mNewssid_network_state_changed)) || (CMUtil.supportDSCPrefix(this.mOldssid_network_state_changed) && CMUtil.isManagedSSID(paramContext, this.mOldssid_network_state_changed))) && CMUtil.isCMTopActivity(paramContext))
              CMUtil.sendBroadCastToMain(paramContext, "CHECK_FOR_INTERNET_SERVICE_IN_SETTINGS"); 
          } else if ((this.mOldstate_network_state_changed.equals("CONNECTED") && this.mNewstate_network_state_changed.equals("DISCONNECTED")) || (this.mOldstate_network_state_changed.equals("CONNECTED") && this.mNewstate_network_state_changed.equals("DISCONNECTING")) || (this.mOldstate_network_state_changed.equals("CONNECTED") && this.mNewstate_network_state_changed.equals("SCANNING")) || (this.mOldstate_network_state_changed.equals("CAPTIVE_PORTAL_CHECK") && this.mNewstate_network_state_changed.equals("DISCONNECTED"))) {
            Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, check disconnected... IS_WIFI_CONNECTED = " + CMService.IS_WIFI_CONNECTED + ", mOldssid = " + this.mOldssid_network_state_changed);
            if (CMUtil.supportDSCPrefix(this.mOldssid_network_state_changed) && CMUtil.isManagedSSID(paramContext, this.mOldssid_network_state_changed)) {
              CMService.IS_WIFI_CONNECTED = false;
              CMUtil.sendBroadCastToMain(paramContext, "WIFI_DISCONNECTED");
            } 
          } else {
            Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, check disconnected... IS_WIFI_CONNECTED = " + CMService.IS_WIFI_CONNECTED + ", mNewstate = " + this.mNewstate_network_state_changed);
            if (CMService.IS_WIFI_CONNECTED && this.mNewstate_network_state_changed.equals("DISCONNECTED")) {
              CMService.IS_WIFI_CONNECTED = false;
              CMUtil.sendBroadCastToMain(paramContext, "WIFI_DISCONNECTED");
            } 
            CMService.IS_WIFI_CONNECTED = false;
          } 
        } 
        this.mOldssid_network_state_changed = this.mNewssid_network_state_changed;
        this.mOldstate_network_state_changed = detailedState.toString();
      } else {
        if (j == 0 || j == 1)
          CMService.IS_WIFI_CONNECTED = false; 
        Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, finished by wifi disable");
        CMUtil.sendBroadCastToMain(paramContext, "WIFI_DISCONNECTED");
      } 
    } 
    if (str.equals("android.net.wifi.supplicant.STATE_CHANGE")) {
      SupplicantState supplicantState = (SupplicantState)paramIntent.getParcelableExtra("newState");
      this.mNewstate_supplicant_state_changed = supplicantState.toString();
      Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, SUPPLICANT_STATE_CHANGED_ACTION, mOldssid_supplicant_state_changed = " + this.mOldssid_supplicant_state_changed + ", mNewssid_supplicant_state_changed = " + this.mNewssid_supplicant_state_changed);
      Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, SUPPLICANT_STATE_CHANGED_ACTION, mOldstate_supplicant_state_changed = " + this.mOldstate_supplicant_state_changed + ", mNewstate_supplicant_state_changed = " + this.mNewstate_supplicant_state_changed);
      switch (supplicantState) {
        default:
          this.mOldssid_supplicant_state_changed = this.mNewssid_supplicant_state_changed;
          this.mOldbssid_supplicant_state_changed = this.mNewbssid_supplicant_state_changed;
          this.mOldstate_supplicant_state_changed = this.mNewstate_supplicant_state_changed;
          return;
        case DISCONNECTED:
          break;
      } 
      if (CMUtil.supportDSCPrefix(this.mOldssid_supplicant_state_changed) && CMUtil.isManagedSSID(paramContext, this.mOldssid_supplicant_state_changed)) {
        if (this.mOldstate_supplicant_state_changed.equals(SupplicantState.FOUR_WAY_HANDSHAKE.toString())) {
          this.mRetryPasswordInputCount++;
          Trace.d(CMConstants.TAG_NAME, "CMNetworkStateChangeReceiver, SUPPLICANT_STATE_CHANGED_ACTION, password error!!!, mRetryPasswordInputCount = " + this.mRetryPasswordInputCount);
          if (this.mRetryPasswordInputCount >= 1) {
            CMUtil.removeNetworkConfig(paramContext, wifiManager, this.mOldssid_supplicant_state_changed, this.mOldbssid_supplicant_state_changed);
            this.mRetryPasswordInputCount = 0;
          } 
        } 
        this.mRetryPasswordInputCount = 0;
      } 
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\broadcastreceiver\CMNetworkStateChangeReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */