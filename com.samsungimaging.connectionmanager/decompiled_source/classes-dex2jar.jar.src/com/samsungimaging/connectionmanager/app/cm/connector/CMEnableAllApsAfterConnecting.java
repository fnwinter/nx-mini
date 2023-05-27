package com.samsungimaging.connectionmanager.app.cm.connector;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;
import java.util.Iterator;
import java.util.List;

public class CMEnableAllApsAfterConnecting {
  public static void enalbleAllAps(Context paramContext) {
    WifiManager wifiManager = (WifiManager)paramContext.getSystemService("wifi");
    List list = wifiManager.getConfiguredNetworks();
    if (list != null) {
      Iterator iterator = list.iterator();
      while (true) {
        if (iterator.hasNext()) {
          wifiManager.enableNetwork(((WifiConfiguration)iterator.next()).networkId, false);
          continue;
        } 
        return;
      } 
    } 
  }
  
  public static void enalbleNOTCameraAps(Context paramContext) {
    WifiManager wifiManager = (WifiManager)paramContext.getSystemService("wifi");
    List list = wifiManager.getConfiguredNetworks();
    if (list != null) {
      Iterator<WifiConfiguration> iterator = list.iterator();
      while (true) {
        if (iterator.hasNext()) {
          WifiConfiguration wifiConfiguration = iterator.next();
          if (!CMUtil.supportDSCPrefix(wifiConfiguration.SSID))
            wifiManager.enableNetwork(wifiConfiguration.networkId, false); 
          continue;
        } 
        return;
      } 
    } 
  }
  
  public static void start(Context paramContext) {
    paramContext.startService(new Intent(paramContext, BackgroundService.class));
  }
  
  public static class BackgroundService extends Service {
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context param2Context, Intent param2Intent) {
          if ("android.net.wifi.STATE_CHANGE".equals(param2Intent.getAction()) && ((NetworkInfo)param2Intent.getParcelableExtra("networkInfo")).getDetailedState() == NetworkInfo.DetailedState.CONNECTED && CMService.IS_WIFI_CONNECTED && !CMEnableAllApsAfterConnecting.BackgroundService.this.mRedo) {
            CMEnableAllApsAfterConnecting.BackgroundService.this.mRedo = true;
            CMEnableAllApsAfterConnecting.enalbleAllAps(param2Context);
            CMEnableAllApsAfterConnecting.BackgroundService.this.stopSelf();
          } 
        }
      };
    
    private boolean mRedo;
    
    public IBinder onBind(Intent param1Intent) {
      return null;
    }
    
    public void onCreate() {
      super.onCreate();
      this.mRedo = false;
      IntentFilter intentFilter = new IntentFilter("android.net.wifi.STATE_CHANGE");
      registerReceiver(this.mReceiver, intentFilter);
    }
    
    public void onDestroy() {
      super.onDestroy();
      unregisterReceiver(this.mReceiver);
    }
  }
  
  class null extends BroadcastReceiver {
    public void onReceive(Context param1Context, Intent param1Intent) {
      if ("android.net.wifi.STATE_CHANGE".equals(param1Intent.getAction()) && ((NetworkInfo)param1Intent.getParcelableExtra("networkInfo")).getDetailedState() == NetworkInfo.DetailedState.CONNECTED && CMService.IS_WIFI_CONNECTED && !this.this$1.mRedo) {
        this.this$1.mRedo = true;
        CMEnableAllApsAfterConnecting.enalbleAllAps(param1Context);
        this.this$1.stopSelf();
      } 
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\connector\CMEnableAllApsAfterConnecting.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */