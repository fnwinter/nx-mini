package com.samsungimaging.connectionmanager.app.cm.connector;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import com.samsungimaging.connectionmanager.app.cm.common.CMConstants;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;
import com.samsungimaging.connectionmanager.util.Trace;
import java.util.ArrayList;
import java.util.List;

public class CMCameraAPConnector {
  private static CMCameraAPConnector s_obj = null;
  
  private ConnectionHandler mConnectionHandler = null;
  
  private Connector mConnector = null;
  
  private ConnectorSecurity mConnectorSecurity = null;
  
  Context mContext = null;
  
  private CharSequence[] mSecurityConnectionInfo = (CharSequence[])new String[4];
  
  private List<ScanResult> mSrs = null;
  
  private boolean mTryToConnect = false;
  
  WifiManager mWifiManager = null;
  
  public static CMCameraAPConnector getInstance() {
    // Byte code:
    //   0: ldc com/samsungimaging/connectionmanager/app/cm/connector/CMCameraAPConnector
    //   2: monitorenter
    //   3: getstatic com/samsungimaging/connectionmanager/app/cm/connector/CMCameraAPConnector.s_obj : Lcom/samsungimaging/connectionmanager/app/cm/connector/CMCameraAPConnector;
    //   6: ifnonnull -> 19
    //   9: new com/samsungimaging/connectionmanager/app/cm/connector/CMCameraAPConnector
    //   12: dup
    //   13: invokespecial <init> : ()V
    //   16: putstatic com/samsungimaging/connectionmanager/app/cm/connector/CMCameraAPConnector.s_obj : Lcom/samsungimaging/connectionmanager/app/cm/connector/CMCameraAPConnector;
    //   19: getstatic com/samsungimaging/connectionmanager/app/cm/connector/CMCameraAPConnector.s_obj : Lcom/samsungimaging/connectionmanager/app/cm/connector/CMCameraAPConnector;
    //   22: astore_0
    //   23: ldc com/samsungimaging/connectionmanager/app/cm/connector/CMCameraAPConnector
    //   25: monitorexit
    //   26: aload_0
    //   27: areturn
    //   28: astore_0
    //   29: ldc com/samsungimaging/connectionmanager/app/cm/connector/CMCameraAPConnector
    //   31: monitorexit
    //   32: aload_0
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   3	19	28	finally
    //   19	23	28	finally
  }
  
  public void cancel() {
    if (this.mConnector != null) {
      Trace.d(CMConstants.TAG_NAME, "CMCameraAPConnector, cancel(), mConnector is not null.");
      if (!this.mConnector.isCancelled())
        this.mConnector.cancel(true); 
      this.mConnector = null;
    } 
    if (this.mConnectorSecurity != null) {
      Trace.d(CMConstants.TAG_NAME, "CMCameraAPConnector, cancel(), mConnectorSecurity is not null.");
      if (!this.mConnectorSecurity.isCancelled())
        this.mConnectorSecurity.cancel(true); 
      this.mConnectorSecurity = null;
    } 
  }
  
  public void setSr(ScanResult paramScanResult, Context paramContext, WifiManager paramWifiManager) {
    if (this.mConnectionHandler == null)
      this.mConnectionHandler = ConnectionHandler.getInstance(paramContext); 
    if (this.mContext == null)
      this.mContext = paramContext; 
    if (this.mWifiManager == null)
      this.mWifiManager = paramWifiManager; 
    if (!CMService.IS_WIFI_CONNECTED) {
      if (this.mConnector == null) {
        Trace.d(CMConstants.TAG_NAME, "mConnector is null, new Connector()02!!!");
        if (this.mSrs == null)
          this.mSrs = new ArrayList<ScanResult>(); 
        this.mSrs.add(paramScanResult);
        this.mConnector = new Connector();
        this.mConnector.execute((Object[])new Void[] { null, null, null });
        return;
      } 
    } else {
      return;
    } 
    cancel();
    if (this.mConnector == null) {
      Trace.d(CMConstants.TAG_NAME, "mConnector is null, new Connector()03!!!");
      if (this.mSrs != null)
        this.mSrs.clear(); 
      this.mSrs.add(paramScanResult);
      this.mConnector = new Connector();
      this.mConnector.execute((Object[])new Void[] { null, null, null });
      return;
    } 
  }
  
  public void setSr(List<ScanResult> paramList, Context paramContext, WifiManager paramWifiManager) {
    if (this.mConnectionHandler == null)
      this.mConnectionHandler = ConnectionHandler.getInstance(paramContext); 
    if (this.mContext == null)
      this.mContext = paramContext; 
    if (this.mWifiManager == null)
      this.mWifiManager = paramWifiManager; 
    if (!CMService.IS_WIFI_CONNECTED) {
      if (this.mConnector == null) {
        Trace.d(CMConstants.TAG_NAME, "mConnector is null, new Connector()01!!!");
        this.mSrs = paramList;
        this.mConnector = new Connector();
        this.mConnector.execute((Object[])new Void[] { null, null, null });
        return;
      } 
    } else {
      return;
    } 
    Trace.d(CMConstants.TAG_NAME, "mConnector is running.");
  }
  
  public void setSr(CharSequence[] paramArrayOfCharSequence, Context paramContext, WifiManager paramWifiManager) {
    if (this.mContext == null)
      this.mContext = paramContext; 
    if (this.mWifiManager == null)
      this.mWifiManager = paramWifiManager; 
    if (!CMService.IS_WIFI_CONNECTED) {
      if (this.mConnectorSecurity == null) {
        Trace.d(CMConstants.TAG_NAME, "mConnectorSecurity is null, new ConnectorSecurity()01!!!");
        this.mSecurityConnectionInfo = paramArrayOfCharSequence;
        this.mConnectorSecurity = new ConnectorSecurity();
        this.mConnectorSecurity.execute((Object[])new Void[] { null, null, null });
        return;
      } 
    } else {
      return;
    } 
    cancel();
    if (this.mConnectorSecurity == null) {
      Trace.d(CMConstants.TAG_NAME, "mConnectorSecurity is null, new ConnectorSecurity()02!!!");
      this.mSecurityConnectionInfo = paramArrayOfCharSequence;
      this.mConnectorSecurity = new ConnectorSecurity();
      this.mConnectorSecurity.execute((Object[])new Void[] { null, null, null });
      return;
    } 
  }
  
  class Connector extends AsyncTask<Void, Void, Void> {
    protected Void doInBackground(Void... param1VarArgs) {
      List<ScanResult> list = CMCameraAPConnector.this.mSrs;
      if (CMCameraAPConnector.this.mConnectionHandler == null) {
        Trace.d(CMConstants.TAG_NAME, "CMCameraAPConnector : handler is null, return!");
        return null;
      } 
      if (list != null && list.size() > 0) {
        Trace.d(CMConstants.TAG_NAME, "Performance Check Point : Checking Scan Result List to Connect AP");
        Trace.d(CMConstants.TAG_NAME, "CMCameraAPConnector : doInBackground---- 1");
        int i = 0;
        while (true) {
          if (i < list.size()) {
            Trace.d(CMConstants.TAG_NAME, "CMCameraAPConnector : doInBackground---- 2");
            if (!CMService.IS_WIFI_CONNECTED) {
              Trace.d(CMConstants.TAG_NAME, "CMCameraAPConnector : doInBackground---- 3, try to connect to camear : " + ((ScanResult)list.get(i)).SSID);
              CMCameraAPConnector.this.mTryToConnect = CMUtil.connectToNewCameraSoftAP(CMCameraAPConnector.this.mContext, CMCameraAPConnector.this.mWifiManager, list.get(i), CMCameraAPConnector.this.mConnectionHandler);
              Trace.d(CMConstants.TAG_NAME, "CMCameraAPConnector : doInBackground---- 4, mTryToConnect = " + CMCameraAPConnector.this.mTryToConnect);
              SystemClock.sleep(3000L);
              if (isCancelled() || CMService.IS_WIFI_CONNECTED) {
                Trace.d(CMConstants.TAG_NAME, "CMCameraAPConnector : Canceled!!!, WIFI CONNECTED = " + CMService.IS_WIFI_CONNECTED);
                return null;
              } 
            } 
            i++;
            continue;
          } 
          return null;
        } 
      } 
      return null;
    }
    
    protected void onPostExecute(Void param1Void) {
      super.onPostExecute(param1Void);
      Trace.d(CMConstants.TAG_NAME, "CMCameraAPConnector, PostExecute(), mConnector set null.");
      CMCameraAPConnector.this.mSrs = null;
      CMCameraAPConnector.this.mConnector = null;
    }
  }
  
  class ConnectorSecurity extends AsyncTask<Void, Void, Void> {
    protected Void doInBackground(Void... param1VarArgs) {
      if (CMCameraAPConnector.this.mConnectionHandler == null) {
        Trace.d(CMConstants.TAG_NAME, "CMCameraAPConnector, ConnectorSecurity : handler is null, return!");
        return null;
      } 
      Trace.d(CMConstants.TAG_NAME, "CMCameraAPConnector, ConnectorSecurity : doInBackground---- 1, WIFI CONNECTED = " + CMService.IS_WIFI_CONNECTED);
      if (!CMService.IS_WIFI_CONNECTED) {
        Trace.d(CMConstants.TAG_NAME, "CMCameraAPConnector, ConnectorSecurity : doInBackground---- 2");
        CMCameraAPConnector.this.mTryToConnect = CMUtil.createConfigAndRequestConnection(CMCameraAPConnector.this.mContext, CMCameraAPConnector.this.mWifiManager, null, true, null, CMCameraAPConnector.this.mSecurityConnectionInfo);
        Trace.d(CMConstants.TAG_NAME, "CMCameraAPConnector, ConnectorSecurity : doInBackground---- 3, mTryToConnect = " + CMCameraAPConnector.this.mTryToConnect);
        SystemClock.sleep(3000L);
        return null;
      } 
      return null;
    }
    
    protected void onPostExecute(Void param1Void) {
      super.onPostExecute(param1Void);
      Trace.d(CMConstants.TAG_NAME, "CMCameraAPConnector, ConnectorSecurity, PostExecute(), mConnectorSecurity set null.");
      CMCameraAPConnector.this.mConnectorSecurity = null;
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\connector\CMCameraAPConnector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */