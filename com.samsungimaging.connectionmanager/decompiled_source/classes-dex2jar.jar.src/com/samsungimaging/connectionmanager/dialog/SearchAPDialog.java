package com.samsungimaging.connectionmanager.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.samsungimaging.connectionmanager.app.cm.common.CMConstants;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.app.cm.connector.CMCameraAPConnector;
import com.samsungimaging.connectionmanager.app.cm.connector.LastRequestedCameraInfo;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;
import com.samsungimaging.connectionmanager.manager.DatabaseManager;
import com.samsungimaging.connectionmanager.util.Trace;
import java.util.ArrayList;
import java.util.List;

public class SearchAPDialog extends CustomDialog implements AdapterView.OnItemClickListener {
  private Activity mActivity;
  
  private AnimationDrawable mAnimation = null;
  
  private List<ScanResult> mCameraSrs = new ArrayList<ScanResult>();
  
  private ConnectivityManager mConnectivityManager = null;
  
  private ListView mListView = null;
  
  private BaseAdapter mScannedListAdapter = new BaseAdapter() {
      public int getCount() {
        return (SearchAPDialog.this.mCameraSrs != null) ? SearchAPDialog.this.mCameraSrs.size() : 0;
      }
      
      public ScanResult getItem(int param1Int) {
        return SearchAPDialog.this.mCameraSrs.get(param1Int);
      }
      
      public long getItemId(int param1Int) {
        return param1Int;
      }
      
      public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
        final boolean isSupportDSC;
        boolean bool1 = false;
        View view = param1View;
        if (param1View == null)
          view = ((LayoutInflater)SearchAPDialog.this.getContext().getSystemService("layout_inflater")).inflate(2130903046, null); 
        final ScanResult scanResult = getItem(param1Int);
        if (CMUtil.NOTsupportDSCPrefix(scanResult.SSID)) {
          bool2 = false;
        } else {
          bool2 = true;
        } 
        boolean bool = SearchAPDialog.this.checkRequested(scanResult.SSID);
        TextView textView = (TextView)view.findViewById(2131558500);
        textView.setSelected(true);
        textView.setText(scanResult.SSID);
        textView.setEnabled(bool2);
        ImageView imageView = (ImageView)view.findViewById(2131558501);
        imageView.setImageResource(2130838219);
        if (bool2) {
          param1Int = bool1;
          imageView.setVisibility(param1Int);
          imageView.setSelected(bool);
          imageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View param2View) {
                  SearchAPDialog.null.access$1(SearchAPDialog.null.this).connect(isSupportDSC, scanResult);
                }
              });
          view.setTag(Boolean.valueOf(bool2));
          return view;
        } 
        param1Int = 8;
        imageView.setVisibility(param1Int);
        imageView.setSelected(bool);
        imageView.setOnClickListener(new View.OnClickListener() {
              public void onClick(View param2View) {
                SearchAPDialog.null.access$1(SearchAPDialog.null.this).connect(isSupportDSC, scanResult);
              }
            });
        view.setTag(Boolean.valueOf(bool2));
        return view;
      }
    };
  
  private Handler mScannedListRefreshHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        SearchAPDialog.this.refreshScannedListView();
        SearchAPDialog.this.mScannedListRefreshHandler.sendEmptyMessageDelayed(0, 2000L);
      }
    };
  
  private WifiManager mWifiManager = null;
  
  public SearchAPDialog(Activity paramActivity) {
    super((Context)paramActivity);
    this.mActivity = paramActivity;
  }
  
  private void beforeFinish() {
    Trace.d(CMConstants.TAG_NAME, "SearchAPDialog, beforeFinish");
    CMService.getInstance().NFCConnectionTryCancel();
  }
  
  private boolean checkRequested(String paramString) {
    boolean bool2 = false;
    ScanResult scanResult = LastRequestedCameraInfo.getInstance().getLastRequestedCameraInfo();
    String str = LastRequestedCameraInfo.getInstance().getLastRequestedCameraSSID();
    if (scanResult != null) {
      String str1 = (LastRequestedCameraInfo.getInstance().getLastRequestedCameraInfo()).SSID;
      boolean bool = bool2;
      if (str1 != null) {
        bool = bool2;
        if (!str1.isEmpty()) {
          bool = bool2;
          if (str1.contains(paramString))
            bool = true; 
        } 
      } 
      return bool;
    } 
    boolean bool1 = bool2;
    if (str != null) {
      bool1 = bool2;
      if (str.contains(paramString))
        return true; 
    } 
    return bool1;
  }
  
  private void connect(boolean paramBoolean, ScanResult paramScanResult) {
    if (paramBoolean) {
      LastRequestedCameraInfo.getInstance().setLastRequestedCameraInfo(paramScanResult);
      LastRequestedCameraInfo.getInstance().setLastRequestedCameraSSID(paramScanResult.SSID);
      this.mScannedListAdapter.notifyDataSetChanged();
      CMUtil.addToManagedAPList(getContext(), paramScanResult.SSID);
      WifiInfo wifiInfo = this.mWifiManager.getConnectionInfo();
      NetworkInfo networkInfo = this.mConnectivityManager.getActiveNetworkInfo();
      if (networkInfo != null) {
        NetworkInfo.DetailedState detailedState = networkInfo.getDetailedState();
        String str = networkInfo.getTypeName();
        paramBoolean = networkInfo.isAvailable();
        Trace.d(CMConstants.TAG_NAME, "onClick : ~~~~~~~~~~~~~~~~~~~~~~~~~ state_connectivitymanager : " + detailedState);
        Trace.d(CMConstants.TAG_NAME, "onClick : ~~~~~~~~~~~~~~~~~~~~~~~~~ typeName : " + str + ", available : " + paramBoolean);
        if (detailedState == NetworkInfo.DetailedState.CONNECTED) {
          if ("WIFI".equals(str) && paramBoolean) {
            if (CMUtil.supportDSCPrefix(wifiInfo.getSSID()) && CMUtil.isManagedSSID(getContext(), wifiInfo.getSSID()) && CMUtil.isEqualSSID(paramScanResult.SSID, wifiInfo.getSSID())) {
              paramBoolean = true;
            } else {
              paramBoolean = false;
            } 
            CMService.IS_WIFI_CONNECTED = paramBoolean;
            Trace.d(CMConstants.TAG_NAME, "onClick, IS_CONNECTED = " + CMService.IS_WIFI_CONNECTED);
            if (CMService.IS_WIFI_CONNECTED) {
              Trace.d(CMConstants.TAG_NAME, "already connected by wifi-manager!");
              if (CMService.getInstance() != null)
                CMService.getInstance().checkSubApplicationType(); 
              return;
            } 
            Trace.d(CMConstants.TAG_NAME, "test06");
            CMCameraAPConnector.getInstance().setSr(paramScanResult, getContext(), this.mWifiManager);
            return;
          } 
        } else {
          return;
        } 
        Trace.d(CMConstants.TAG_NAME, "test06-1");
        CMCameraAPConnector.getInstance().setSr(paramScanResult, getContext(), this.mWifiManager);
        return;
      } 
      Trace.d(CMConstants.TAG_NAME, "test07");
      CMCameraAPConnector.getInstance().setSr(paramScanResult, getContext(), this.mWifiManager);
      return;
    } 
    Trace.d(CMConstants.TAG_NAME, "NOTsupportDSCPrefix, onClick!!!");
    CMUtil.sendBroadCastToMain(getContext(), "SHOW_NOT_SUPPORTED_DIALOG");
  }
  
  private void initContent() {
    Trace.d(CMConstants.TAG_NAME, "SearchAPDialog, initContent");
    setView(2130903065);
    setNeutralButton(2131362067, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            SearchAPDialog.this.beforeFinish();
            SearchAPDialog.this.dismiss();
          }
        });
    this.mAnimation = (AnimationDrawable)((ImageView)this.mView.findViewById(2131558555)).getBackground();
  }
  
  private void initCustomContent() {
    this.mListView = (ListView)this.mView.findViewById(2131558534);
    this.mListView.setEmptyView(this.mView.findViewById(2131558565));
    this.mListView.setAdapter((ListAdapter)this.mScannedListAdapter);
    this.mListView.setOnItemClickListener(this);
  }
  
  public void onBackPressed() {
    super.onBackPressed();
    beforeFinish();
    dismiss();
  }
  
  protected void onCreate(Bundle paramBundle) {
    Trace.d(CMConstants.TAG_NAME, "SearchAPDialog, onCreate");
    this.mWifiManager = (WifiManager)getContext().getSystemService("wifi");
    this.mConnectivityManager = (ConnectivityManager)getContext().getSystemService("connectivity");
    initContent();
    super.onCreate(paramBundle);
    initCustomContent();
    if (CMUtil.isTestMode(getContext()))
      ((LinearLayout)findViewById(2131558538)).setVisibility(4); 
  }
  
  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
    connect(((Boolean)paramView.getTag()).booleanValue(), (ScanResult)this.mScannedListAdapter.getItem(paramInt));
  }
  
  protected void onStart() {
    Trace.d(CMConstants.TAG_NAME, "SearchAPDialog, onStart");
    this.mAnimation.start();
    if (this.mCameraSrs.size() > 0)
      this.mCameraSrs.clear(); 
    this.mScannedListRefreshHandler.sendEmptyMessage(1);
    initCustomContent();
    super.onStart();
    CMUtil.sendBroadCastToMain(getContext(), "START_WIFI_SCAN_TIMER_FOR_MIMUTES", 120000);
    if (DatabaseManager.getCountForAP(getContext()) > 0 && !CMInfo.getInstance().getIsNFCLaunchFlag())
      Toast.makeText(getContext(), getContext().getString(2131362040), 0).show(); 
  }
  
  protected void onStop() {
    Trace.d(CMConstants.TAG_NAME, "SearchAPDialog, onStop");
    LastRequestedCameraInfo.getInstance().setLastRequestedCameraInfo(null);
    LastRequestedCameraInfo.getInstance().setLastRequestedCameraSSID(null);
    this.mAnimation.stop();
    if (this.mScannedListRefreshHandler != null && this.mScannedListRefreshHandler.hasMessages(0))
      this.mScannedListRefreshHandler.removeMessages(0); 
    CMUtil.sendBroadCastToMain(getContext(), "STOP_WIFI_SCAN_TIMER_FOR_MIMUTES");
    super.onStop();
  }
  
  public void refreshScannedListView() {
    // Byte code:
    //   0: new java/util/ArrayList
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: pop
    //   8: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/service/CMService;
    //   11: invokevirtual getScannedList : ()Ljava/util/List;
    //   14: astore #7
    //   16: iconst_0
    //   17: istore #6
    //   19: iconst_0
    //   20: istore_2
    //   21: iload #6
    //   23: istore #5
    //   25: aload_0
    //   26: getfield mCameraSrs : Ljava/util/List;
    //   29: ifnull -> 63
    //   32: iload #6
    //   34: istore #5
    //   36: aload #7
    //   38: ifnull -> 63
    //   41: aload_0
    //   42: getfield mCameraSrs : Ljava/util/List;
    //   45: invokeinterface size : ()I
    //   50: aload #7
    //   52: invokeinterface size : ()I
    //   57: if_icmpeq -> 140
    //   60: iconst_1
    //   61: istore #5
    //   63: getstatic com/samsungimaging/connectionmanager/app/cm/common/CMConstants.TAG_NAME : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   66: new java/lang/StringBuilder
    //   69: dup
    //   70: ldc_w 'SearchAPDialog, refreshScannedListView, refresh = '
    //   73: invokespecial <init> : (Ljava/lang/String;)V
    //   76: iload #5
    //   78: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   81: ldc_w ', mCameraSrs.size() = '
    //   84: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   87: aload_0
    //   88: getfield mCameraSrs : Ljava/util/List;
    //   91: invokeinterface size : ()I
    //   96: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   99: ldc_w ', cameraSrs_temp.size() = '
    //   102: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   105: aload #7
    //   107: invokeinterface size : ()I
    //   112: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   115: invokevirtual toString : ()Ljava/lang/String;
    //   118: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   121: iload #5
    //   123: ifeq -> 139
    //   126: aload_0
    //   127: aload #7
    //   129: putfield mCameraSrs : Ljava/util/List;
    //   132: aload_0
    //   133: getfield mScannedListAdapter : Landroid/widget/BaseAdapter;
    //   136: invokevirtual notifyDataSetChanged : ()V
    //   139: return
    //   140: iconst_0
    //   141: istore_1
    //   142: iload_1
    //   143: aload #7
    //   145: invokeinterface size : ()I
    //   150: if_icmplt -> 170
    //   153: iload_2
    //   154: aload #7
    //   156: invokeinterface size : ()I
    //   161: if_icmpne -> 246
    //   164: iconst_0
    //   165: istore #5
    //   167: goto -> 63
    //   170: iconst_0
    //   171: istore_3
    //   172: iload_3
    //   173: aload_0
    //   174: getfield mCameraSrs : Ljava/util/List;
    //   177: invokeinterface size : ()I
    //   182: if_icmplt -> 192
    //   185: iload_1
    //   186: iconst_1
    //   187: iadd
    //   188: istore_1
    //   189: goto -> 142
    //   192: iload_2
    //   193: istore #4
    //   195: aload #7
    //   197: iload_1
    //   198: invokeinterface get : (I)Ljava/lang/Object;
    //   203: checkcast android/net/wifi/ScanResult
    //   206: getfield SSID : Ljava/lang/String;
    //   209: aload_0
    //   210: getfield mCameraSrs : Ljava/util/List;
    //   213: iload_3
    //   214: invokeinterface get : (I)Ljava/lang/Object;
    //   219: checkcast android/net/wifi/ScanResult
    //   222: getfield SSID : Ljava/lang/String;
    //   225: invokevirtual equals : (Ljava/lang/Object;)Z
    //   228: ifeq -> 236
    //   231: iload_2
    //   232: iconst_1
    //   233: iadd
    //   234: istore #4
    //   236: iload_3
    //   237: iconst_1
    //   238: iadd
    //   239: istore_3
    //   240: iload #4
    //   242: istore_2
    //   243: goto -> 172
    //   246: iconst_1
    //   247: istore #5
    //   249: goto -> 63
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\SearchAPDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */