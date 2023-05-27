package com.samsungimaging.connectionmanager.app.pullservice.demo.ml;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;
import com.samsungimaging.connectionmanager.app.localgallery.LocalGallery;
import com.samsungimaging.connectionmanager.app.pullservice.PullService;
import com.samsungimaging.connectionmanager.app.pullservice.controller.DeviceController;
import com.samsungimaging.connectionmanager.app.pullservice.controller.UPNPController;
import com.samsungimaging.connectionmanager.app.pullservice.demo.ml.adapter.ContentArrayAdapter;
import com.samsungimaging.connectionmanager.app.pullservice.demo.ml.adapter.ExpandableListAdapter;
import com.samsungimaging.connectionmanager.app.pullservice.demo.ml.entity.GroupEntity;
import com.samsungimaging.connectionmanager.app.pullservice.demo.rvf.LiveShutter;
import com.samsungimaging.connectionmanager.app.pullservice.demo.util.CommonUtils;
import com.samsungimaging.connectionmanager.app.pullservice.dialog.CustomDialogFileSave;
import com.samsungimaging.connectionmanager.dialog.CustomDialog;
import com.samsungimaging.connectionmanager.dialog.CustomProgressDialog;
import com.samsungimaging.connectionmanager.util.ExToast;
import com.samsungimaging.connectionmanager.util.Trace;
import java.util.ArrayList;
import java.util.List;
import org.cybergarage.upnp.Device;

public class FileSharing extends PullService {
  public static boolean bClosing;
  
  private static boolean bConnect;
  
  private static boolean bFileReceiveDone;
  
  public static boolean bNoTelephonyDevice;
  
  public static boolean mCancel;
  
  private static ProgressDialog mCloseAppProgDialog;
  
  private static ProgressDialog mConnectAPProgDialog = null;
  
  public static List<Item> mContainerList;
  
  private static Button mCopyButton;
  
  private static Button mGallaryButton;
  
  public static List<GroupEntity> mGroupCollection;
  
  private static CustomProgressDialog mInitDataInfoProgDialog = null;
  
  private static Button mSelectButton;
  
  public static WifiManager mWifimanager;
  
  public boolean CopyModeState = false;
  
  private Trace.Tag TAG = Trace.Tag.ML;
  
  private LinearLayout action_bar;
  
  private boolean bCloseByFinishSafe = false;
  
  public boolean bConnectedMobileData = true;
  
  TextView bodyText;
  
  private boolean bpopupflag = false;
  
  Button btnOk;
  
  private CheckBox checkBox;
  
  Dialog detail_view_dialog = null;
  
  Dialog dialog;
  
  CustomDialog dialog1;
  
  private boolean isChecked = false;
  
  LinearLayout layout;
  
  private ContentArrayAdapter mContentArrayAdapter;
  
  private CustomDialogFileSave mCustomDialogFileSave = null;
  
  private Handler mDeviceControlHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        switch (param1Message.what) {
          default:
            return;
          case 85:
            FileSharing.this.doSendEmptyMessage(5);
            return;
          case 89:
            break;
        } 
        if (((Boolean)param1Message.obj).booleanValue()) {
          String str = FileSharing.this.mDeviceController.getAction(44).getArgumentValue("TotalNumber");
          Trace.d(FileSharing.this.TAG, "TotalNumber : " + str);
          if (!str.equals("0")) {
            FileSharing.this.saveFiles();
            return;
          } 
        } 
      }
    };
  
  private Handler mEventHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        Intent intent;
        Trace.d(FileSharing.this.TAG, "start handleMessage() : " + param1Message.what);
        switch (param1Message.what) {
          default:
            return;
          case 117:
            break;
        } 
        if (((String)param1Message.obj).equals("TransitionToRVF")) {
          intent = new Intent((Context)FileSharing.this, LiveShutter.class);
          FileSharing.this.startActivity(intent);
          FileSharing.this.finish();
          return;
        } 
        if (((String)((Message)intent).obj).equals("TransitionToML")) {
          String str = FileSharing.this.getIntent().getStringExtra("requestServiceChange");
          if (str != null && str.equals("changeToML"))
            FileSharing.this.dismissDialog(3001); 
          FileSharing.this.doDeviceConfiguration();
          return;
        } 
      }
    };
  
  ExToast mExToast = ExToast.getInstance();
  
  private ExpandableListAdapter mExpandableListAdapter;
  
  private ExpandableListView mExpandableListView;
  
  private GridView mGridView;
  
  Handler mHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        // Byte code:
        //   0: aload_0
        //   1: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   4: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   7: new java/lang/StringBuilder
        //   10: dup
        //   11: ldc '****MSGWHAT : '
        //   13: invokespecial <init> : (Ljava/lang/String;)V
        //   16: aload_1
        //   17: getfield what : I
        //   20: invokestatic valueOf : (I)Ljava/lang/String;
        //   23: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   26: invokevirtual toString : ()Ljava/lang/String;
        //   29: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   32: aload_1
        //   33: getfield what : I
        //   36: tableswitch default -> 240, 1 -> 569, 2 -> 240, 3 -> 240, 4 -> 1485, 5 -> 764, 6 -> 807, 7 -> 240, 8 -> 672, 9 -> 252, 10 -> 334, 11 -> 240, 12 -> 240, 13 -> 240, 14 -> 240, 15 -> 240, 16 -> 240, 17 -> 240, 18 -> 240, 19 -> 240, 20 -> 240, 21 -> 1221, 22 -> 1197, 23 -> 1281, 24 -> 240, 25 -> 240, 26 -> 240, 27 -> 240, 28 -> 240, 29 -> 240, 30 -> 240, 31 -> 240, 32 -> 1335, 33 -> 1245, 34 -> 240, 35 -> 240, 36 -> 1411, 37 -> 1571, 38 -> 1356, 39 -> 356, 40 -> 240, 41 -> 379, 42 -> 1592, 43 -> 240, 44 -> 241, 45 -> 1723, 46 -> 1744, 47 -> 1835
        //   240: return
        //   241: aload_0
        //   242: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   245: sipush #1013
        //   248: invokevirtual showDialog : (I)V
        //   251: return
        //   252: aload_1
        //   253: getfield obj : Ljava/lang/Object;
        //   256: checkcast java/lang/Integer
        //   259: invokevirtual intValue : ()I
        //   262: istore_2
        //   263: aload_0
        //   264: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   267: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   270: new java/lang/StringBuilder
        //   273: dup
        //   274: ldc 'Const.MsgId.AP_CHECK_COMPLETED nDectectCount='
        //   276: invokespecial <init> : (Ljava/lang/String;)V
        //   279: iload_2
        //   280: invokevirtual append : (I)Ljava/lang/StringBuilder;
        //   283: invokevirtual toString : ()Ljava/lang/String;
        //   286: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   289: iload_2
        //   290: iconst_2
        //   291: if_icmpge -> 327
        //   294: iconst_1
        //   295: putstatic com/samsungimaging/connectionmanager/app/pullservice/demo/ml/Const$Config.DUMMY_WIFI_CONNECT_TYPE : I
        //   298: aload_0
        //   299: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   302: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   305: new java/lang/StringBuilder
        //   308: dup
        //   309: ldc 'AP Running Mode = '
        //   311: invokespecial <init> : (Ljava/lang/String;)V
        //   314: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/ml/Const$Config.DUMMY_WIFI_CONNECT_TYPE : I
        //   317: invokevirtual append : (I)Ljava/lang/StringBuilder;
        //   320: invokevirtual toString : ()Ljava/lang/String;
        //   323: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   326: return
        //   327: iconst_3
        //   328: putstatic com/samsungimaging/connectionmanager/app/pullservice/demo/ml/Const$Config.DUMMY_WIFI_CONNECT_TYPE : I
        //   331: goto -> 298
        //   334: aload_0
        //   335: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   338: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   341: ldc 'Const.MsgId.AP_CHECK_FAILED'
        //   343: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   346: aload_0
        //   347: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   350: bipush #32
        //   352: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;I)V
        //   355: return
        //   356: aload_0
        //   357: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   360: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   363: ldc 'Const.MsgId.AP_CONNECTED_FAILED'
        //   365: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   368: aload_0
        //   369: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   372: sipush #1010
        //   375: invokevirtual showDialog : (I)V
        //   378: return
        //   379: aload_0
        //   380: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   383: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   386: ldc 'Const.MsgId.CONNGUIDE_DIALOG_OPEN'
        //   388: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   391: invokestatic access$5 : ()Lcom/samsungimaging/connectionmanager/dialog/CustomProgressDialog;
        //   394: ifnull -> 407
        //   397: invokestatic access$5 : ()Lcom/samsungimaging/connectionmanager/dialog/CustomProgressDialog;
        //   400: invokevirtual dismiss : ()V
        //   403: aconst_null
        //   404: invokestatic access$6 : (Lcom/samsungimaging/connectionmanager/dialog/CustomProgressDialog;)V
        //   407: aload_0
        //   408: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   411: invokestatic access$7 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)I
        //   414: iconst_1
        //   415: if_icmpne -> 469
        //   418: aload_0
        //   419: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   422: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Landroid/widget/GridView;
        //   425: aload_0
        //   426: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   429: invokestatic access$9 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ContentArrayAdapter;
        //   432: invokevirtual setAdapter : (Landroid/widget/ListAdapter;)V
        //   435: aload_0
        //   436: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   439: invokestatic access$9 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ContentArrayAdapter;
        //   442: invokevirtual notifyDataSetChanged : ()V
        //   445: aload_0
        //   446: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   449: invokevirtual getApplicationContext : ()Landroid/content/Context;
        //   452: invokestatic getDisplayConnGuide : (Landroid/content/Context;)Z
        //   455: ifeq -> 561
        //   458: aload_0
        //   459: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   462: sipush #1011
        //   465: invokevirtual showDialog : (I)V
        //   468: return
        //   469: aload_0
        //   470: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   473: invokestatic access$7 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)I
        //   476: iconst_2
        //   477: if_icmpne -> 445
        //   480: aload_0
        //   481: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   484: invokestatic access$10 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Landroid/widget/ExpandableListView;
        //   487: aload_0
        //   488: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   491: invokestatic access$11 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;
        //   494: invokevirtual setAdapter : (Landroid/widget/ExpandableListAdapter;)V
        //   497: aload_0
        //   498: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   501: invokestatic access$11 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;
        //   504: invokevirtual notifyDataSetChanged : ()V
        //   507: iconst_0
        //   508: istore_2
        //   509: iload_2
        //   510: aload_0
        //   511: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   514: invokestatic access$11 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;
        //   517: invokevirtual getGroupCount : ()I
        //   520: if_icmpge -> 445
        //   523: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing.mGroupCollection : Ljava/util/List;
        //   526: iload_2
        //   527: invokeinterface get : (I)Ljava/lang/Object;
        //   532: checkcast com/samsungimaging/connectionmanager/app/pullservice/demo/ml/entity/GroupEntity
        //   535: getfield groupExpandedStatus : I
        //   538: iconst_1
        //   539: if_icmpne -> 554
        //   542: aload_0
        //   543: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   546: invokestatic access$10 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Landroid/widget/ExpandableListView;
        //   549: iload_2
        //   550: invokevirtual expandGroup : (I)Z
        //   553: pop
        //   554: iload_2
        //   555: iconst_1
        //   556: iadd
        //   557: istore_2
        //   558: goto -> 509
        //   561: aload_0
        //   562: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   565: invokevirtual appClose : ()V
        //   568: return
        //   569: aload_0
        //   570: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   573: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   576: ldc 'Const.MsgId.AP_CONNECTING'
        //   578: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   581: aload_0
        //   582: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   585: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)V
        //   588: ldc '%s\\n\\n[%s] %s'
        //   590: iconst_3
        //   591: anewarray java/lang/Object
        //   594: dup
        //   595: iconst_0
        //   596: aload_0
        //   597: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   600: ldc 2131361893
        //   602: invokevirtual getString : (I)Ljava/lang/String;
        //   605: aastore
        //   606: dup
        //   607: iconst_1
        //   608: aload_0
        //   609: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   612: ldc 2131361919
        //   614: invokevirtual getString : (I)Ljava/lang/String;
        //   617: aastore
        //   618: dup
        //   619: iconst_2
        //   620: aload_0
        //   621: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   624: invokevirtual getApplicationContext : ()Landroid/content/Context;
        //   627: invokestatic getMacAddress : (Landroid/content/Context;)Ljava/lang/String;
        //   630: aastore
        //   631: invokestatic format : (Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
        //   634: astore_1
        //   635: aload_0
        //   636: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   639: aconst_null
        //   640: aload_1
        //   641: iconst_1
        //   642: iconst_1
        //   643: aconst_null
        //   644: invokestatic show : (Landroid/content/Context;Ljava/lang/CharSequence;Ljava/lang/CharSequence;ZZLandroid/content/DialogInterface$OnCancelListener;)Landroid/app/ProgressDialog;
        //   647: invokestatic access$13 : (Landroid/app/ProgressDialog;)V
        //   650: invokestatic access$14 : ()Landroid/app/ProgressDialog;
        //   653: iconst_0
        //   654: invokevirtual setCancelable : (Z)V
        //   657: invokestatic access$14 : ()Landroid/app/ProgressDialog;
        //   660: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing$3$1
        //   663: dup
        //   664: aload_0
        //   665: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing$3;)V
        //   668: invokevirtual setOnKeyListener : (Landroid/content/DialogInterface$OnKeyListener;)V
        //   671: return
        //   672: aload_0
        //   673: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   676: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   679: ldc 'Const.MsgId.CP_DEVICE_FOUND'
        //   681: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   684: invokestatic access$14 : ()Landroid/app/ProgressDialog;
        //   687: ifnull -> 700
        //   690: invokestatic access$14 : ()Landroid/app/ProgressDialog;
        //   693: invokevirtual dismiss : ()V
        //   696: aconst_null
        //   697: invokestatic access$13 : (Landroid/app/ProgressDialog;)V
        //   700: new com/samsungimaging/connectionmanager/dialog/CustomProgressDialog
        //   703: dup
        //   704: aload_0
        //   705: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   708: invokespecial <init> : (Landroid/content/Context;)V
        //   711: invokestatic access$6 : (Lcom/samsungimaging/connectionmanager/dialog/CustomProgressDialog;)V
        //   714: invokestatic access$5 : ()Lcom/samsungimaging/connectionmanager/dialog/CustomProgressDialog;
        //   717: iconst_0
        //   718: invokevirtual setCancelable : (Z)V
        //   721: invokestatic access$5 : ()Lcom/samsungimaging/connectionmanager/dialog/CustomProgressDialog;
        //   724: iconst_0
        //   725: invokevirtual setProgressStyle : (I)V
        //   728: invokestatic access$5 : ()Lcom/samsungimaging/connectionmanager/dialog/CustomProgressDialog;
        //   731: aload_0
        //   732: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   735: ldc 2131361899
        //   737: invokevirtual getString : (I)Ljava/lang/String;
        //   740: invokevirtual setMessage : (Ljava/lang/CharSequence;)V
        //   743: invokestatic access$5 : ()Lcom/samsungimaging/connectionmanager/dialog/CustomProgressDialog;
        //   746: invokevirtual show : ()V
        //   749: invokestatic access$5 : ()Lcom/samsungimaging/connectionmanager/dialog/CustomProgressDialog;
        //   752: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing$3$2
        //   755: dup
        //   756: aload_0
        //   757: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing$3;)V
        //   760: invokevirtual setOnKeyListener : (Landroid/content/DialogInterface$OnKeyListener;)V
        //   763: return
        //   764: aload_0
        //   765: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   768: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   771: ldc 'Const.MsgId.DEVICE_CONFIGURATION_COMPLETED'
        //   773: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   776: aload_0
        //   777: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   780: invokevirtual getApplicationContext : ()Landroid/content/Context;
        //   783: invokestatic getDisplayNotice : (Landroid/content/Context;)Z
        //   786: ifeq -> 799
        //   789: aload_0
        //   790: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   793: bipush #44
        //   795: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;I)V
        //   798: return
        //   799: aload_0
        //   800: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   803: invokestatic access$16 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)V
        //   806: return
        //   807: aload_0
        //   808: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   811: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   814: ldc_w 'Const.MsgId.CP_BROWSING_COMPLETED'
        //   817: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   820: invokestatic access$5 : ()Lcom/samsungimaging/connectionmanager/dialog/CustomProgressDialog;
        //   823: ifnull -> 848
        //   826: invokestatic access$5 : ()Lcom/samsungimaging/connectionmanager/dialog/CustomProgressDialog;
        //   829: invokevirtual dismiss : ()V
        //   832: invokestatic access$17 : ()Landroid/widget/Button;
        //   835: ldc_w '#ebebeb'
        //   838: invokestatic parseColor : (Ljava/lang/String;)I
        //   841: invokevirtual setTextColor : (I)V
        //   844: aconst_null
        //   845: invokestatic access$6 : (Lcom/samsungimaging/connectionmanager/dialog/CustomProgressDialog;)V
        //   848: aload_0
        //   849: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   852: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   855: ldc_w 'Const.MsgId.DISPLAY_BROWSE_RESULT'
        //   858: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   861: aload_0
        //   862: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   865: invokestatic access$7 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)I
        //   868: iconst_1
        //   869: if_icmpne -> 999
        //   872: aload_0
        //   873: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   876: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Landroid/widget/GridView;
        //   879: aload_0
        //   880: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   883: invokestatic access$9 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ContentArrayAdapter;
        //   886: invokevirtual setAdapter : (Landroid/widget/ListAdapter;)V
        //   889: aload_0
        //   890: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   893: invokestatic access$9 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ContentArrayAdapter;
        //   896: invokevirtual notifyDataSetChanged : ()V
        //   899: aload_0
        //   900: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   903: invokestatic access$19 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)V
        //   906: aload_0
        //   907: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   910: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   913: new java/lang/StringBuilder
        //   916: dup
        //   917: aload_0
        //   918: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   921: invokevirtual getApplicationContext : ()Landroid/content/Context;
        //   924: invokestatic getDisplayDetailGuide : (Landroid/content/Context;)Z
        //   927: invokestatic valueOf : (Z)Ljava/lang/String;
        //   930: invokespecial <init> : (Ljava/lang/String;)V
        //   933: ldc_w '    Utils.getDisplayDetailGuide(getApplicationContext())    '
        //   936: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   939: aload_0
        //   940: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   943: invokevirtual getApplicationContext : ()Landroid/content/Context;
        //   946: invokestatic getDisplayDetailGuide : (Landroid/content/Context;)Z
        //   949: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   952: invokevirtual toString : ()Ljava/lang/String;
        //   955: invokestatic i : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   958: aload_0
        //   959: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   962: invokevirtual getApplicationContext : ()Landroid/content/Context;
        //   965: invokestatic getDisplayDetailGuide : (Landroid/content/Context;)Z
        //   968: ifeq -> 1180
        //   971: aload_0
        //   972: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   975: sipush #1009
        //   978: invokevirtual showDialog : (I)V
        //   981: ldc_w 'Tag'
        //   984: ldc_w 'Select button enabled'
        //   987: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
        //   990: pop
        //   991: invokestatic access$17 : ()Landroid/widget/Button;
        //   994: iconst_1
        //   995: invokevirtual setEnabled : (Z)V
        //   998: return
        //   999: aload_0
        //   1000: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1003: invokestatic access$7 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)I
        //   1006: iconst_2
        //   1007: if_icmpne -> 899
        //   1010: aload_0
        //   1011: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1014: aload_0
        //   1015: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1018: invokevirtual getApplicationContext : ()Landroid/content/Context;
        //   1021: invokevirtual getResources : ()Landroid/content/res/Resources;
        //   1024: ldc_w 2131296257
        //   1027: invokevirtual getInteger : (I)I
        //   1030: invokevirtual setGroupCollection : (I)V
        //   1033: aload_0
        //   1034: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1037: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter
        //   1040: dup
        //   1041: aload_0
        //   1042: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1045: invokevirtual getApplicationContext : ()Landroid/content/Context;
        //   1048: aload_0
        //   1049: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1052: aload_0
        //   1053: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1056: invokestatic access$10 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Landroid/widget/ExpandableListView;
        //   1059: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing.mGroupCollection : Ljava/util/List;
        //   1062: aload_0
        //   1063: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1066: invokevirtual getApplicationContext : ()Landroid/content/Context;
        //   1069: invokevirtual getResources : ()Landroid/content/res/Resources;
        //   1072: ldc_w 2131296257
        //   1075: invokevirtual getInteger : (I)I
        //   1078: iconst_1
        //   1079: aload_0
        //   1080: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1083: getfield mHandler : Landroid/os/Handler;
        //   1086: aload_0
        //   1087: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1090: getfield mExToast : Lcom/samsungimaging/connectionmanager/util/ExToast;
        //   1093: invokespecial <init> : (Landroid/content/Context;Landroid/app/Activity;Landroid/widget/ExpandableListView;Ljava/util/List;IZLandroid/os/Handler;Lcom/samsungimaging/connectionmanager/util/ExToast;)V
        //   1096: invokestatic access$18 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;)V
        //   1099: aload_0
        //   1100: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1103: invokestatic access$10 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Landroid/widget/ExpandableListView;
        //   1106: aload_0
        //   1107: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1110: invokestatic access$11 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;
        //   1113: invokevirtual setAdapter : (Landroid/widget/ExpandableListAdapter;)V
        //   1116: aload_0
        //   1117: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1120: invokestatic access$11 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;
        //   1123: invokevirtual notifyDataSetChanged : ()V
        //   1126: iconst_0
        //   1127: istore_2
        //   1128: iload_2
        //   1129: aload_0
        //   1130: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1133: invokestatic access$11 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;
        //   1136: invokevirtual getGroupCount : ()I
        //   1139: if_icmpge -> 899
        //   1142: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing.mGroupCollection : Ljava/util/List;
        //   1145: iload_2
        //   1146: invokeinterface get : (I)Ljava/lang/Object;
        //   1151: checkcast com/samsungimaging/connectionmanager/app/pullservice/demo/ml/entity/GroupEntity
        //   1154: getfield groupExpandedStatus : I
        //   1157: iconst_1
        //   1158: if_icmpne -> 1173
        //   1161: aload_0
        //   1162: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1165: invokestatic access$10 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Landroid/widget/ExpandableListView;
        //   1168: iload_2
        //   1169: invokevirtual expandGroup : (I)Z
        //   1172: pop
        //   1173: iload_2
        //   1174: iconst_1
        //   1175: iadd
        //   1176: istore_2
        //   1177: goto -> 1128
        //   1180: aload_0
        //   1181: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1184: ldc_w 2131361894
        //   1187: iconst_1
        //   1188: invokestatic makeText : (Landroid/content/Context;II)Landroid/widget/Toast;
        //   1191: invokevirtual show : ()V
        //   1194: goto -> 981
        //   1197: aload_0
        //   1198: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1201: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1204: ldc_w 'Const.MsgId.APP_CLOSE_FOR_REMOVED_SD'
        //   1207: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1210: aload_0
        //   1211: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1214: sipush #1005
        //   1217: invokevirtual showDialog : (I)V
        //   1220: return
        //   1221: aload_0
        //   1222: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1225: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1228: ldc_w 'Const.MsgId.APP_CLOSE_FOR_MEMORY_FULL'
        //   1231: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1234: aload_0
        //   1235: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1238: sipush #1006
        //   1241: invokevirtual showDialog : (I)V
        //   1244: return
        //   1245: aload_0
        //   1246: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1249: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1252: ldc_w 'Const.MsgId.RUN_EXIT_BYEBYE'
        //   1255: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1258: aload_0
        //   1259: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1262: sipush #1003
        //   1265: invokevirtual showDialog : (I)V
        //   1268: aload_0
        //   1269: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1272: bipush #32
        //   1274: ldc2_w 1000
        //   1277: invokestatic access$15 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;IJ)V
        //   1280: return
        //   1281: aload_0
        //   1282: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1285: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1288: ldc_w 'Const.MsgId.APP_CLOSE_FOR_DSC_ERROR'
        //   1291: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1294: aload_0
        //   1295: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1298: invokestatic access$20 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Z
        //   1301: ifne -> 240
        //   1304: aload_0
        //   1305: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1308: iconst_1
        //   1309: invokestatic access$21 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;Z)V
        //   1312: aload_0
        //   1313: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1316: sipush #1002
        //   1319: invokevirtual showDialog : (I)V
        //   1322: aload_0
        //   1323: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1326: bipush #32
        //   1328: ldc2_w 1000
        //   1331: invokestatic access$15 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;IJ)V
        //   1334: return
        //   1335: aload_0
        //   1336: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1339: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1342: ldc_w 'Const.MsgId.EXITING'
        //   1345: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1348: aload_0
        //   1349: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1352: invokevirtual appClose : ()V
        //   1355: return
        //   1356: aload_0
        //   1357: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1360: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1363: ldc_w 'Const.MsgId.FINISH_SAVING_FILE'
        //   1366: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1369: aload_0
        //   1370: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1373: invokestatic access$22 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Landroid/app/AlertDialog;
        //   1376: ifnull -> 240
        //   1379: aload_0
        //   1380: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1383: invokestatic access$22 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Landroid/app/AlertDialog;
        //   1386: invokevirtual isShowing : ()Z
        //   1389: ifeq -> 240
        //   1392: aload_0
        //   1393: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1396: invokestatic access$22 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Landroid/app/AlertDialog;
        //   1399: invokevirtual dismiss : ()V
        //   1402: aload_0
        //   1403: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1406: aconst_null
        //   1407: invokestatic access$23 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;Landroid/app/AlertDialog;)V
        //   1410: return
        //   1411: aload_0
        //   1412: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1415: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1418: ldc_w 'Const.MsgId.CANCEL_SAVING'
        //   1421: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1424: aload_0
        //   1425: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1428: invokestatic access$22 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Landroid/app/AlertDialog;
        //   1431: ifnull -> 1465
        //   1434: aload_0
        //   1435: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1438: invokestatic access$22 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Landroid/app/AlertDialog;
        //   1441: invokevirtual isShowing : ()Z
        //   1444: ifeq -> 1465
        //   1447: aload_0
        //   1448: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1451: invokestatic access$22 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Landroid/app/AlertDialog;
        //   1454: invokevirtual dismiss : ()V
        //   1457: aload_0
        //   1458: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1461: aconst_null
        //   1462: invokestatic access$23 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;Landroid/app/AlertDialog;)V
        //   1465: iconst_1
        //   1466: putstatic com/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing.mCancel : Z
        //   1469: aload_0
        //   1470: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1473: iconst_0
        //   1474: putfield CopyModeState : Z
        //   1477: aload_0
        //   1478: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1481: invokevirtual updateSelectedState : ()V
        //   1484: return
        //   1485: aload_0
        //   1486: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1489: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1492: ldc_w 'Const.MsgId.SHOW_GALLERY_DIALOG'
        //   1495: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1498: aload_0
        //   1499: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1502: invokestatic access$22 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Landroid/app/AlertDialog;
        //   1505: ifnull -> 1539
        //   1508: aload_0
        //   1509: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1512: invokestatic access$22 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Landroid/app/AlertDialog;
        //   1515: invokevirtual isShowing : ()Z
        //   1518: ifeq -> 1539
        //   1521: aload_0
        //   1522: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1525: invokestatic access$22 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Landroid/app/AlertDialog;
        //   1528: invokevirtual dismiss : ()V
        //   1531: aload_0
        //   1532: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1535: aconst_null
        //   1536: invokestatic access$23 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;Landroid/app/AlertDialog;)V
        //   1539: aload_0
        //   1540: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1543: invokevirtual getApplicationContext : ()Landroid/content/Context;
        //   1546: ldc_w 'samsungimaging'
        //   1549: invokestatic getActivityRunning : (Landroid/content/Context;Ljava/lang/String;)Z
        //   1552: ifeq -> 1566
        //   1555: aload_0
        //   1556: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1559: sipush #1008
        //   1562: invokevirtual showDialog : (I)V
        //   1565: return
        //   1566: iconst_1
        //   1567: invokestatic access$24 : (Z)V
        //   1570: return
        //   1571: aload_0
        //   1572: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1575: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1578: ldc_w 'Const.MsgId.CHANGED_SELECTED_FILE_COUNT'
        //   1581: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1584: aload_0
        //   1585: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1588: invokestatic access$19 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)V
        //   1591: return
        //   1592: aload_1
        //   1593: getfield obj : Ljava/lang/Object;
        //   1596: checkcast com/samsungimaging/connectionmanager/app/pullservice/demo/ml/Item
        //   1599: astore_1
        //   1600: aload_1
        //   1601: ifnull -> 240
        //   1604: aload_1
        //   1605: invokevirtual getScreenRes : ()Ljava/lang/String;
        //   1608: ifnull -> 1621
        //   1611: aload_1
        //   1612: invokevirtual getScreenRes : ()Ljava/lang/String;
        //   1615: ldc_w ''
        //   1618: if_acmpne -> 1706
        //   1621: aload_1
        //   1622: invokevirtual getThumbRes : ()Ljava/lang/String;
        //   1625: astore_1
        //   1626: aload_1
        //   1627: ifnull -> 240
        //   1630: aload_1
        //   1631: invokevirtual hashCode : ()I
        //   1634: invokestatic valueOf : (I)Ljava/lang/String;
        //   1637: astore_3
        //   1638: new java/io/File
        //   1641: dup
        //   1642: invokestatic getThumbStorage : ()Ljava/lang/String;
        //   1645: aload_3
        //   1646: invokespecial <init> : (Ljava/lang/String;Ljava/lang/String;)V
        //   1649: invokevirtual exists : ()Z
        //   1652: ifeq -> 1714
        //   1655: new java/lang/StringBuilder
        //   1658: dup
        //   1659: invokestatic getThumbStorage : ()Ljava/lang/String;
        //   1662: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
        //   1665: invokespecial <init> : (Ljava/lang/String;)V
        //   1668: ldc_w '/'
        //   1671: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   1674: aload_3
        //   1675: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   1678: invokevirtual toString : ()Ljava/lang/String;
        //   1681: astore_1
        //   1682: aload_0
        //   1683: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1686: aload_1
        //   1687: invokestatic access$25 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;Ljava/lang/String;)Z
        //   1690: ifne -> 240
        //   1693: aload_0
        //   1694: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1697: getfield mExToast : Lcom/samsungimaging/connectionmanager/util/ExToast;
        //   1700: bipush #7
        //   1702: invokevirtual show : (I)V
        //   1705: return
        //   1706: aload_1
        //   1707: invokevirtual getScreenRes : ()Ljava/lang/String;
        //   1710: astore_1
        //   1711: goto -> 1626
        //   1714: aload_0
        //   1715: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1718: aload_1
        //   1719: invokestatic access$26 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;Ljava/lang/String;)V
        //   1722: return
        //   1723: aload_0
        //   1724: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1727: invokevirtual isFinishing : ()Z
        //   1730: ifne -> 240
        //   1733: aload_0
        //   1734: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1737: sipush #1014
        //   1740: invokevirtual showDialog : (I)V
        //   1743: return
        //   1744: aload_1
        //   1745: getfield obj : Ljava/lang/Object;
        //   1748: checkcast java/lang/String
        //   1751: astore_1
        //   1752: aload_0
        //   1753: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1756: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1759: new java/lang/StringBuilder
        //   1762: dup
        //   1763: ldc_w 'ml message : '
        //   1766: invokespecial <init> : (Ljava/lang/String;)V
        //   1769: aload_1
        //   1770: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   1773: invokevirtual toString : ()Ljava/lang/String;
        //   1776: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1779: aload_1
        //   1780: ldc_w 'HTTP_UNAUTHORIZED'
        //   1783: invokevirtual contains : (Ljava/lang/CharSequence;)Z
        //   1786: ifeq -> 1807
        //   1789: aload_0
        //   1790: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1793: aload_0
        //   1794: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1797: ldc_w 2131361829
        //   1800: invokevirtual getString : (I)Ljava/lang/String;
        //   1803: invokestatic access$27 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;Ljava/lang/String;)V
        //   1806: return
        //   1807: aload_1
        //   1808: ldc_w 'HTTP_UNAVAILABLE'
        //   1811: invokevirtual contains : (Ljava/lang/CharSequence;)Z
        //   1814: ifeq -> 240
        //   1817: aload_0
        //   1818: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1821: aload_0
        //   1822: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1825: ldc_w 2131361830
        //   1828: invokevirtual getString : (I)Ljava/lang/String;
        //   1831: invokestatic access$27 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;Ljava/lang/String;)V
        //   1834: return
        //   1835: aload_0
        //   1836: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing;
        //   1839: sipush #1015
        //   1842: invokevirtual showDialog : (I)V
        //   1845: return
      }
    };
  
  ImageLoader mImageLoader = null;
  
  private AlertDialog mProgressSave = null;
  
  public ScanResult mSelectedSr = null;
  
  private int mSortingMode = 2;
  
  private TextView mTextView;
  
  public WifiManager.WifiLock mWifiLock = null;
  
  private ImageButton ml_goto_rvf;
  
  TextView title;
  
  static {
    mCloseAppProgDialog = null;
    mWifimanager = null;
    mContainerList = new ArrayList<Item>();
    mGroupCollection = new ArrayList<GroupEntity>();
    mCancel = false;
    bConnect = false;
    bClosing = false;
    bNoTelephonyDevice = false;
    bFileReceiveDone = false;
  }
  
  private void connectionFailDialog(String paramString) {
    View view = getLayoutInflater().inflate(2130903110, null);
    Button button = (Button)view.findViewById(2131558721);
    ((TextView)view.findViewById(2131558719)).setText(paramString);
    TextView textView = (TextView)view.findViewById(2131558720);
    textView.setText(textView.getText() + CMInfo.getInstance().getConnectedSSID());
    (new AlertDialog.Builder((Context)this)).setView(view).setOnKeyListener(new DialogInterface.OnKeyListener() {
          public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
            if (param1KeyEvent.getKeyCode() == 84)
              return true; 
            if (param1KeyEvent.getKeyCode() == 4) {
              CMService.getInstance().beforefinish(0);
              return true;
            } 
            return false;
          }
        }).show();
    button.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            CMService.getInstance().beforefinish(0);
          }
        });
  }
  
  private void dismissProgressDialog() {
    if (mConnectAPProgDialog != null) {
      mConnectAPProgDialog.dismiss();
      mConnectAPProgDialog = null;
    } 
    if (mInitDataInfoProgDialog != null) {
      mInitDataInfoProgDialog.dismiss();
      mInitDataInfoProgDialog = null;
    } 
    if (mCloseAppProgDialog != null) {
      mCloseAppProgDialog.dismiss();
      mCloseAppProgDialog = null;
    } 
    if (this.mProgressSave != null) {
      this.mProgressSave.dismiss();
      this.mProgressSave = null;
    } 
  }
  
  private void doAfterUserWifiSetting() {
    if (mWifimanager.isWifiEnabled()) {
      if (Utils.supportDSCPrefix(this.connectedSsid)) {
        Trace.d(this.TAG, "1!!!!!!!!!!!!!");
        (new Thread(new Runnable() {
              public void run() {
                FileSharing.this.doSendMessage(FileSharing.this.mHandler.obtainMessage(1));
              }
            })).start();
        return;
      } 
      Trace.d(this.TAG, "2!!!!!!!!!!!!!");
      showDialog(1003);
      (new Thread(new Runnable() {
            public void run() {
              FileSharing.this.appClose();
            }
          })).start();
      return;
    } 
    Trace.d(this.TAG, "3!!!!!!!!!!!!!");
    showDialog(1003);
    (new Thread(new Runnable() {
          public void run() {
            FileSharing.this.appClose();
          }
        })).start();
  }
  
  private void doBrowsing() {
    Message message = new Message();
    message.what = 8;
    doSendMessageDelayed(message, 1L);
    (new Thread(new Runnable() {
          public void run() {
            Trace.d(FileSharing.this.TAG, "DISPLAY_BROWSE_RESULT in r");
            if (FileSharing.this.mDeviceController.getAction(41) != null) {
              Trace.d(FileSharing.this.TAG, "==============================");
              Trace.d(FileSharing.this.TAG, "ddddddfffffffffgggggggggg       zzzzzzz eeee");
              (new BrowseManager(FileSharing.this.mDeviceController.getAction(41))).doBrowse();
              Trace.d(FileSharing.this.TAG, "==============================");
            } else {
              Trace.d(FileSharing.this.TAG, "d f q w e r t y u i o p a s d f g h j k l 0 0 0 0 0 0 0 0 0 0");
            } 
            FileSharing.this.doSendEmptyMessage(6);
          }
        })).start();
  }
  
  private void doDeviceConfiguration() {
    Trace.d(this.TAG, "doDeviceConfiguration start");
    if (this.mDeviceController.getAction(42) != null) {
      doAction(42, (String)null);
      return;
    } 
    doSendEmptyMessage(5);
  }
  
  private void doSendEmptyMessage(int paramInt) {
    if (this.mHandler != null)
      this.mHandler.sendEmptyMessage(paramInt); 
  }
  
  private void doSendEmptyMessageDelayed(int paramInt, long paramLong) {
    if (this.mHandler != null)
      this.mHandler.sendEmptyMessageDelayed(paramInt, paramLong); 
  }
  
  private void doSendMessage(Message paramMessage) {
    if (this.mHandler != null)
      this.mHandler.sendMessage(paramMessage); 
  }
  
  private void doSendMessageDelayed(Message paramMessage, long paramLong) {
    if (this.mHandler != null)
      this.mHandler.sendMessageDelayed(paramMessage, paramLong); 
  }
  
  private void init() {
    this.mImageLoader = new ImageLoader(getApplicationContext(), (Activity)this, 2130837530, Utils.getThumbStorage());
    Utils.setUseragent(getApplicationContext());
    mContainerList.clear();
    mGroupCollection.clear();
    this.upnpController.setEventHandler(this.mEventHandler);
    this.mDeviceController.setHandler(this.mDeviceControlHandler);
  }
  
  private void onChangedSelectItemCount() {
    if (this.mSortingMode == 1) {
      this.mTextView.setText(String.valueOf(this.mContentArrayAdapter.getCheckedItemCount()) + "   " + getString(2131361909));
      if (this.mContentArrayAdapter.getCheckedItemCount() > 0) {
        setEnabledCopyButton(true);
      } else {
        setEnabledCopyButton(false);
      } 
      if (this.mContentArrayAdapter.getCheckedItemCount() >= 1000 || (this.mContentArrayAdapter.getCheckedItemCount() == this.mContentArrayAdapter.getCount() && this.mContentArrayAdapter.getCount() != 0)) {
        mSelectButton.setText(2131361906);
        return;
      } 
      mSelectButton.setText(2131361905);
      return;
    } 
    if (this.mSortingMode == 2) {
      if (this.mExpandableListAdapter.getCheckedItemCount() > 0) {
        this.mTextView.setText(String.valueOf(this.mExpandableListAdapter.getCheckedItemCount()) + "   " + getString(2131361909));
        setEnabledCopyButton(true);
      } else {
        this.mTextView.setText("");
        setEnabledCopyButton(false);
      } 
      if (this.mExpandableListAdapter.getCheckedItemCount() >= 1000 || (this.mExpandableListAdapter.getCheckedItemCount() == this.mExpandableListAdapter.getItemCount() && this.mExpandableListAdapter.getItemCount() != 0)) {
        mSelectButton.setEnabled(true);
        mSelectButton.setText(2131361906);
        mSelectButton.setTextColor(-1);
        return;
      } 
      mSelectButton.setEnabled(true);
      mSelectButton.setText(2131361905);
      mSelectButton.setTextColor(-1);
      return;
    } 
  }
  
  private void removeTempFiles(String paramString) {
    // Byte code:
    //   0: aload_0
    //   1: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   4: ldc_w 'removeTempFiles start'
    //   7: invokestatic e : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   10: aload_0
    //   11: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   14: new java/lang/StringBuilder
    //   17: dup
    //   18: ldc_w 'mPath is ::: '
    //   21: invokespecial <init> : (Ljava/lang/String;)V
    //   24: aload_1
    //   25: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   28: invokevirtual toString : ()Ljava/lang/String;
    //   31: invokestatic e : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   34: new java/io/File
    //   37: dup
    //   38: aload_1
    //   39: invokespecial <init> : (Ljava/lang/String;)V
    //   42: astore #4
    //   44: aload #4
    //   46: invokevirtual listFiles : ()[Ljava/io/File;
    //   49: astore #5
    //   51: aload #4
    //   53: invokevirtual isDirectory : ()Z
    //   56: ifeq -> 91
    //   59: aload_0
    //   60: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   63: new java/lang/StringBuilder
    //   66: dup
    //   67: ldc_w 'dir.listFiles() is :: '
    //   70: invokespecial <init> : (Ljava/lang/String;)V
    //   73: aload #5
    //   75: arraylength
    //   76: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   79: ldc_w '     11111'
    //   82: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: invokevirtual toString : ()Ljava/lang/String;
    //   88: invokestatic e : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   91: aload #4
    //   93: invokevirtual exists : ()Z
    //   96: ifeq -> 116
    //   99: aload #5
    //   101: arraylength
    //   102: ifeq -> 116
    //   105: aload #5
    //   107: arraylength
    //   108: istore_3
    //   109: iconst_0
    //   110: istore_2
    //   111: iload_2
    //   112: iload_3
    //   113: if_icmplt -> 148
    //   116: aload #4
    //   118: invokevirtual length : ()J
    //   121: lconst_0
    //   122: lcmp
    //   123: ifeq -> 147
    //   126: aload #4
    //   128: invokevirtual list : ()[Ljava/lang/String;
    //   131: astore #4
    //   133: aload #4
    //   135: ifnull -> 147
    //   138: iconst_0
    //   139: istore_2
    //   140: iload_2
    //   141: aload #4
    //   143: arraylength
    //   144: if_icmplt -> 187
    //   147: return
    //   148: aload #5
    //   150: iload_2
    //   151: aaload
    //   152: astore #6
    //   154: aload #6
    //   156: invokevirtual isDirectory : ()Z
    //   159: ifeq -> 178
    //   162: aload_0
    //   163: aload #6
    //   165: invokevirtual getAbsolutePath : ()Ljava/lang/String;
    //   168: invokespecial removeTempFiles : (Ljava/lang/String;)V
    //   171: iload_2
    //   172: iconst_1
    //   173: iadd
    //   174: istore_2
    //   175: goto -> 111
    //   178: aload #6
    //   180: invokevirtual delete : ()Z
    //   183: pop
    //   184: goto -> 171
    //   187: aload #4
    //   189: iload_2
    //   190: aaload
    //   191: astore #5
    //   193: new java/io/File
    //   196: dup
    //   197: new java/lang/StringBuilder
    //   200: dup
    //   201: aload_1
    //   202: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   205: invokespecial <init> : (Ljava/lang/String;)V
    //   208: ldc_w '/'
    //   211: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   214: aload #5
    //   216: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   219: invokevirtual toString : ()Ljava/lang/String;
    //   222: invokespecial <init> : (Ljava/lang/String;)V
    //   225: astore #5
    //   227: aload #5
    //   229: invokevirtual exists : ()Z
    //   232: ifeq -> 293
    //   235: aload_0
    //   236: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   239: new java/lang/StringBuilder
    //   242: dup
    //   243: ldc_w 'children size   :::   '
    //   246: invokespecial <init> : (Ljava/lang/String;)V
    //   249: aload #4
    //   251: arraylength
    //   252: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   255: invokevirtual toString : ()Ljava/lang/String;
    //   258: invokestatic e : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   261: aload #5
    //   263: invokevirtual delete : ()Z
    //   266: pop
    //   267: aload_0
    //   268: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   271: new java/lang/StringBuilder
    //   274: dup
    //   275: ldc_w 'children size   :::   '
    //   278: invokespecial <init> : (Ljava/lang/String;)V
    //   281: aload #4
    //   283: arraylength
    //   284: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   287: invokevirtual toString : ()Ljava/lang/String;
    //   290: invokestatic e : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   293: iload_2
    //   294: iconst_1
    //   295: iadd
    //   296: istore_2
    //   297: goto -> 140
  }
  
  private void saveFiles() {
    ArrayList<Item> arrayList = new ArrayList();
    int j = mContainerList.size();
    for (int i = 0;; i++) {
      if (i >= j) {
        this.mCustomDialogFileSave = new CustomDialogFileSave((Context)this, this.mHandler, this.mDeviceController.isResizeMode(), arrayList, this.mImageLoader);
        this.mCustomDialogFileSave.setNegativeButton(new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                FileSharing.this.doAction(44, "0");
                FileSharing.this.mCustomDialogFileSave.cancel(true);
              }
            });
        this.mCustomDialogFileSave.execute((Object[])new Void[0]);
        return;
      } 
      Item item = mContainerList.get(i);
      if (item.getItemState() == 2)
        arrayList.add(item); 
    } 
  }
  
  private void setContentView() {
    if (this.mSortingMode == 1) {
      setContentView(2130903079);
      this.mContentArrayAdapter = new ContentArrayAdapter((Context)this, (Activity)this, 2130903081, mContainerList, true, this.mHandler, this.mExToast);
      this.mGridView = (GridView)findViewById(2131558613);
    } else if (this.mSortingMode == 2) {
      setContentView(2130903078);
      this.mExpandableListView = (ExpandableListView)findViewById(2131558608);
    } 
    this.mTextView = (TextView)findViewById(2131558605);
    this.action_bar = (LinearLayout)findViewById(2131558606);
    this.ml_goto_rvf = (ImageButton)findViewById(2131558607);
    if (CMUtil.checkOldVersionSmartCameraApp(getConnectedSSID())) {
      this.action_bar.setVisibility(8);
    } else {
      this.ml_goto_rvf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              FileSharing.this.doAction(43, "changeToRVF");
              final Handler handler = new Handler();
              handler.postDelayed(new Runnable() {
                    public void run() {
                      if (CMService.ACTIVE_SERVICE == 1) {
                        FileSharing.null.access$0(FileSharing.null.this).showDialog(2011);
                        handler.postDelayed(new Runnable() {
                              public void run() {
                                ((WifiManager)CMService.mContext.getSystemService("wifi")).disconnect();
                              }
                            },  2000L);
                      } 
                    }
                  }3000L);
              FileSharing.this.showDialog(3001);
            }
          });
    } 
    mSelectButton = (Button)findViewById(2131558609);
    mSelectButton.setEnabled(false);
    mSelectButton.setTextColor(-7829368);
    mSelectButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Trace.d(FileSharing.this.TAG, "mSelectButton text : " + FileSharing.mSelectButton.getText().toString() + " string resource : " + FileSharing.this.getString(2131361905));
            if (FileSharing.mSelectButton.getText().toString().equals(FileSharing.this.getString(2131361905))) {
              FileSharing.this.selectAllItem();
              return;
            } 
            FileSharing.this.unSelectAllItem();
          }
        });
    mCopyButton = (Button)findViewById(2131558610);
    setEnabledCopyButton(false);
    mCopyButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Integer integer;
            Trace.d(FileSharing.this.TAG, "start mCopyButton.onClick() CopyModeState : " + FileSharing.this.CopyModeState);
            boolean bool = CMUtil.checkOldVersionSmartCameraApp(FileSharing.this.getConnectedSSID());
            if (!FileSharing.this.CopyModeState) {
              FileSharing.this.CopyModeState = true;
              if (bool) {
                FileSharing.this.saveFiles();
                return;
              } 
            } else {
              return;
            } 
            if (FileSharing.this.mSortingMode == 1) {
              integer = Integer.valueOf(FileSharing.this.mContentArrayAdapter.getCheckedItemCount());
            } else {
              integer = Integer.valueOf(FileSharing.this.mExpandableListAdapter.getCheckedItemCount());
            } 
            FileSharing.this.doAction(44, integer.toString());
          }
        });
    mGallaryButton = (Button)findViewById(2131558611);
    mGallaryButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent((Context)FileSharing.this, LocalGallery.class);
            intent.putExtra("extra_description_res_id", 2131362052);
            intent.putExtra("extra_back_button_string_res_id", 2131362062);
            FileSharing.this.startActivityForResult(intent, 100);
          }
        });
  }
  
  public static void setEnabledCopyButton(boolean paramBoolean) {
    if (mCopyButton != null) {
      if (paramBoolean) {
        mCopyButton.setEnabled(true);
        mCopyButton.setTextColor(Color.parseColor("#ebebeb"));
        return;
      } 
    } else {
      return;
    } 
    mCopyButton.setEnabled(false);
    mCopyButton.setTextColor(-7829368);
  }
  
  private boolean showDetailDialog(String paramString) {
    boolean bool = false;
    Bitmap bitmap = BitmapFactory.decodeFile(paramString);
    if (bitmap != null) {
      View view = ((LayoutInflater)getSystemService("layout_inflater")).inflate(2130903073, null);
      AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
      builder.setView(view);
      final AlertDialog dialog = builder.create();
      ImageView imageView = (ImageView)view.findViewById(2131558579);
      imageView.setImageBitmap(bitmap);
      imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              dialog.dismiss();
            }
          });
      alertDialog.show();
      bool = true;
    } 
    return bool;
  }
  
  private void showDetailDialogForURL(String paramString) {
    View view = ((LayoutInflater)getSystemService("layout_inflater")).inflate(2130903073, null);
    AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
    builder.setView(view);
    final AlertDialog dialog = builder.create();
    ImageView imageView = (ImageView)view.findViewById(2131558579);
    this.mImageLoader.setImage(paramString, (Activity)this, imageView, null);
    imageView.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            dialog.dismiss();
          }
        });
    alertDialog.show();
  }
  
  public void appClose() {
    Trace.d(this.TAG, "start appClose()");
    Trace.d(this.TAG, "newlo onDestroy() bConnect : " + bConnect);
    dismissProgressDialog();
    this.upnpController.disconnect();
    this.upnpController.stop();
    CMService.getInstance().beforefinish(0);
    finish();
  }
  
  public void byebye() {
    Trace.d(this.TAG, "------- Bye Bye ------------");
    if (!isFinishing()) {
      Message message = new Message();
      message.what = 33;
      doSendMessageDelayed(message, 10L);
    } 
  }
  
  public void closeService() {
    Trace.d(this.TAG, "start closeService()");
    this.mHandler = null;
    dismissProgressDialog();
    CMService.getInstance().beforefinish(0);
    finish();
    if (this.bCloseByFinishSafe)
      CMService.getInstance().finishSafe(); 
  }
  
  public void closedata() {
    if (bClosing)
      return; 
    bClosing = true;
    bConnect = false;
  }
  
  public void doAction(int paramInt, String paramString) {
    Trace.d(this.TAG, "start doAction() id : " + paramInt + " param : " + paramString);
    this.mDeviceController.doAction(paramInt, paramString);
  }
  
  public void finishSafe() {
    Trace.d(this.TAG, "start finishSafe");
    this.upnpController.stop();
    CMService.getInstance().beforefinish(0);
  }
  
  public void getParserException() {
    doSendEmptyMessage(46);
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    Trace.d(this.TAG, "onActivityResult() " + paramInt1 + ",    " + paramInt2);
    switch (paramInt1) {
      default:
        return;
      case 100:
        if (paramInt2 == -1) {
          if (paramIntent.getStringExtra("request_code").equals("goto_rvf")) {
            doAction(43, "changeToRVF");
            showDialog(3001);
            return;
          } 
          this.bCloseByFinishSafe = true;
          (new UnsubscribeTask(null)).execute((Object[])new Device[] { this.upnpController.getConnectedDevice() });
          return;
        } 
      case 101:
        break;
    } 
    doAfterUserWifiSetting();
  }
  
  public void onAlive() {
    bConnect = true;
    if (CMUtil.checkOldVersionSmartCameraApp(getConnectedSSID())) {
      doDeviceConfiguration();
      return;
    } 
    Trace.d(this.TAG, "intent : " + getIntent().getStringExtra("requestServiceChange"));
    String str = getIntent().getStringExtra("requestServiceChange");
    if (str != null && str.equals("changeToML")) {
      showDialog(3001);
      doAction(43, "changeToML");
      return;
    } 
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    if (this.checkBox != null)
      this.isChecked = this.checkBox.isChecked(); 
    switch (paramConfiguration.orientation) {
      default:
        CommonUtils.setSystemConfigurationChanged(getApplicationContext());
        refreshDialog();
        return;
      case 1:
      case 2:
        break;
    } 
    setContentView();
    if (this.mSortingMode == 1) {
      this.mGridView.setAdapter((ListAdapter)this.mContentArrayAdapter);
      this.mContentArrayAdapter.notifyDataSetChanged();
    } else if (this.mSortingMode == 2) {
      setGroupCollection(getApplicationContext().getResources().getInteger(2131296257));
      this.mExpandableListAdapter = new ExpandableListAdapter(getApplicationContext(), (Activity)this, this.mExpandableListView, mGroupCollection, getApplicationContext().getResources().getInteger(2131296257), true, this.mHandler, this.mExToast);
      this.mExpandableListView.setAdapter((ExpandableListAdapter)this.mExpandableListAdapter);
      this.mExpandableListAdapter.notifyDataSetChanged();
      int i = 0;
      while (true) {
        if (i < this.mExpandableListAdapter.getGroupCount()) {
          if (((GroupEntity)this.mExpandableListAdapter.getGroup(i)).groupExpandedStatus == 1)
            this.mExpandableListView.expandGroup(i); 
          i++;
          continue;
        } 
        onChangedSelectItemCount();
      } 
    } 
    onChangedSelectItemCount();
  }
  
  public void onCreate(Bundle paramBundle) {
    Trace.i(this.TAG, "Performance Check Point : start onCreate()");
    super.onCreate(paramBundle);
    if (!Utils.isMountedExternalStorage()) {
      showDialog(1005);
      return;
    } 
    if (Utils.isMemoryFull()) {
      showDialog(1006);
      return;
    } 
    CMService.ACTIVE_SERVICE = 1;
    init();
    if (this.upnpController.isConnected())
      onAlive(); 
    if (this.connectedSsid.contains("NX1000") || this.connectedSsid.contains("NX1100")) {
      this.mSortingMode = 1;
    } else {
      this.mSortingMode = 2;
    } 
    getWindow().addFlags(128);
    mWifimanager = (WifiManager)getSystemService("wifi");
    bNoTelephonyDevice = Utils.CheckTelephonyDevice(Build.MODEL);
    Trace.d(this.TAG, "model name : " + Build.MODEL + ",is No telephony device: " + bNoTelephonyDevice);
    this.mExToast.register((Context)this, 4, 2131361913, 0);
    this.mExToast.register((Context)this, 5, 2131361914, 0);
    this.mExToast.register((Context)this, 6, 2131361827, 0);
    this.mExToast.register((Context)this, 7, 2131361916, 0);
    this.mExToast.register((Context)this, 9, 2131361981, 0);
    setContentView();
    System.gc();
    removeTempFiles(Utils.getThumbStorage());
  }
  
  protected Dialog onCreateDialog(int paramInt) {
    float f;
    final CheckBox checkBox;
    View view;
    final CheckBox checkBox;
    final CheckBox checkbox;
    AlertDialog.Builder builder;
    Trace.i(this.TAG, "onCreateDialog()-111 id : " + paramInt);
    switch (paramInt) {
      default:
        return super.onCreateDialog(paramInt);
      case 1013:
        this.dialog = new Dialog((Context)this);
        this.dialog.requestWindowFeature(1);
        this.dialog.setContentView(2130903074);
        this.dialog.setCanceledOnTouchOutside(false);
        checkBox2 = (CheckBox)this.dialog.findViewById(2131558581);
        checkBox2.setButtonDrawable(2130838239);
        checkBox2.setChecked(this.isChecked);
        f = (getResources().getDisplayMetrics()).density;
        checkBox2.setPadding(checkBox2.getPaddingLeft() + (int)(10.0F * f + 0.5F), checkBox2.getPaddingTop(), checkBox2.getPaddingRight(), checkBox2.getPaddingBottom());
        this.title = (TextView)this.dialog.findViewById(2131558500);
        this.bodyText = (TextView)this.dialog.findViewById(2131558580);
        this.layout = (LinearLayout)this.dialog.findViewById(2131558523);
        this.layout.setVisibility(0);
        this.title.setText(2131361961);
        this.bodyText.setText(2131361902);
        this.btnOk = (Button)this.dialog.findViewById(2131558526);
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                checkBox.setChecked(param1Boolean);
              }
            });
        this.btnOk.setOnClickListener(new View.OnClickListener() {
              public void onClick(View param1View) {
                FileSharing.this.dialog.dismiss();
                if (checkBox.isChecked()) {
                  Trace.e(FileSharing.this.TAG, "notice is checked");
                  Utils.setDisplayNotice(FileSharing.this.getApplicationContext(), false);
                } 
                FileSharing.this.doBrowsing();
              }
            });
        this.dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                boolean bool = true;
                switch (param1Int) {
                  default:
                    bool = false;
                  case 84:
                    return bool;
                  case 4:
                    break;
                } 
                System.exit(1);
              }
            });
        return this.dialog;
      case 1011:
        view = ((LayoutInflater)getSystemService("layout_inflater")).inflate(2130903074, (ViewGroup)findViewById(2131558578));
        checkBox3 = (CheckBox)view.findViewById(2131558581);
        checkBox3.setVisibility(8);
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                checkbox.setChecked(param1Boolean);
              }
            });
        ((TextView)view.findViewById(2131558580)).setText(2131361810);
        builder = new AlertDialog.Builder((Context)this);
        builder.setTitle("");
        builder.setView(view);
        builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                param1DialogInterface.dismiss();
                FileSharing.this.appClose();
              }
            });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                switch (param1Int) {
                  default:
                    return false;
                  case 84:
                    return true;
                  case 4:
                    break;
                } 
                FileSharing.this.finish();
              }
            });
        return (Dialog)builder.create();
      case 1009:
        this.detail_view_dialog = new Dialog((Context)this);
        this.detail_view_dialog.requestWindowFeature(1);
        this.detail_view_dialog.setContentView(2130903075);
        this.detail_view_dialog.setCanceledOnTouchOutside(false);
        checkBox1 = (CheckBox)this.detail_view_dialog.findViewById(2131558585);
        checkBox1.setButtonDrawable(2130838239);
        checkBox1.setChecked(this.isChecked);
        f = (getResources().getDisplayMetrics()).density;
        checkBox1.setPadding(checkBox1.getPaddingLeft() + (int)(10.0F * f + 0.5F), checkBox1.getPaddingTop(), checkBox1.getPaddingRight(), checkBox1.getPaddingBottom());
        ((Button)this.detail_view_dialog.findViewById(2131558586)).setOnClickListener(new View.OnClickListener() {
              public void onClick(View param1View) {
                if (checkBox.isChecked())
                  Utils.setDisplayDetailGuide(FileSharing.this.getApplicationContext(), false); 
                FileSharing.this.dismissDialog(1009);
                Toast.makeText((Context)FileSharing.this, 2131361894, 1).show();
              }
            });
        return this.detail_view_dialog;
      case 1004:
        Trace.i(this.TAG, "MSGBOX_AP_FAIL");
        return (Dialog)(bClosing ? null : (new AlertDialog.Builder((Context)this)).setIcon(2130837529).setTitle(2131361821).setMessage(2131361895).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                Trace.d(FileSharing.this.TAG, "exit 66");
                FileSharing.this.showDialog(1003);
                FileSharing.this.doSendEmptyMessageDelayed(32, 1000L);
              }
            }).setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                Trace.i(FileSharing.this.TAG, "onKey-2()");
                return !((param1Int != 84 || param1KeyEvent.getRepeatCount() != 0) && (param1Int != 4 || param1KeyEvent.getRepeatCount() != 0));
              }
            }).create());
      case 1002:
        Trace.d(this.TAG, "MSGBOX_DSC_DOWN");
      case 1000:
        return (Dialog)(new AlertDialog.Builder((Context)this)).setIcon(2130837529).setTitle(2131361821).setMessage(2131361824).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                FileSharing.this.showDialog(1003);
              }
            }).setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                return !((param1Int != 84 || param1KeyEvent.getRepeatCount() != 0) && (param1Int != 4 || param1KeyEvent.getRepeatCount() != 0));
              }
            }).create();
      case 1003:
        Trace.i(this.TAG, "PROGRESS_BAR_DISPLAY_EXIT");
        dismissProgressDialog();
        mCloseAppProgDialog = new ProgressDialog((Context)this);
        mCloseAppProgDialog.setMessage(getString(2131361896));
        mCloseAppProgDialog.setIndeterminate(true);
        mCloseAppProgDialog.setProgressStyle(0);
        mCloseAppProgDialog.setCancelable(false);
        mCloseAppProgDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                Trace.i(FileSharing.this.TAG, "onKey-3()");
                return !(param1KeyEvent.getKeyCode() != 84 && param1KeyEvent.getKeyCode() != 4);
              }
            });
        return (Dialog)mCloseAppProgDialog;
      case 1008:
        this.dialog = new Dialog((Context)this);
        this.dialog.requestWindowFeature(1);
        this.dialog.setContentView(2130903050);
        this.dialog.setCancelable(false);
        this.title = (TextView)this.dialog.findViewById(2131558500);
        this.title.setText(2131361908);
        this.bodyText = (TextView)this.dialog.findViewById(2131558524);
        this.bodyText.setText(2131361903);
        this.btnOk = (Button)this.dialog.findViewById(2131558526);
        this.btnOk.setOnClickListener(new View.OnClickListener() {
              public void onClick(View param1View) {
                FileSharing.this.dialog.dismiss();
                FileSharing.this.updateSelectedState();
                FileSharing.this.CopyModeState = false;
              }
            });
        return this.dialog;
      case 1006:
        dismissProgressDialog();
        return (Dialog)(new AlertDialog.Builder((Context)this)).setIcon(2130837529).setTitle(2131361821).setMessage(2131361911).setCancelable(false).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
            }).create();
      case 1005:
        dismissProgressDialog();
        return (Dialog)(new AlertDialog.Builder((Context)this)).setIcon(2130837529).setTitle(2131361821).setMessage(2131361822).setCancelable(false).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                FileSharing.this.showDialog(1003);
                FileSharing.this.doSendEmptyMessageDelayed(32, 1000L);
              }
            }).create();
      case 1007:
        return (Dialog)(new AlertDialog.Builder((Context)this)).setIcon(2130837529).setTitle(2131361920).setMessage(2131361921).setCancelable(false).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
            }).create();
      case 1010:
        return (Dialog)(new AlertDialog.Builder((Context)this)).setIcon(2130837529).setTitle(2131361920).setMessage(2131361921).setCancelable(false).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                FileSharing.this.finish();
              }
            }).create();
      case 1014:
        return (Dialog)(new AlertDialog.Builder((Context)this)).setIcon(2130837529).setTitle(2131361920).setMessage(2131361933).setCancelable(false).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
            }).create();
      case 1015:
        break;
    } 
    CustomDialog customDialog = new CustomDialog((Context)this);
    customDialog.setIcon(2130837529);
    customDialog.setTitle(2131361920);
    customDialog.setMessage(2131362068);
    customDialog.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            FileSharing.this.dismissDialog(1015);
          }
        });
    return (Dialog)customDialog;
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu) {
    return super.onCreateOptionsMenu(paramMenu);
  }
  
  protected void onDestroy() {
    Trace.d(this.TAG, "start onDestroy()");
    sendBroadcast(new Intent("action_disconnect"));
    super.onDestroy();
    if (this.mCustomDialogFileSave != null)
      this.mCustomDialogFileSave.cancel(true); 
    if (CMUtil.isForceClose((ActivityManager)getSystemService("activity")))
      SystemClock.sleep(3000L); 
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    switch (paramInt) {
      default:
        return super.onKeyUp(paramInt, paramKeyEvent);
      case 84:
        break;
    } 
    return true;
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
    if (paramMenuItem.getTitle().equals("goto RVF")) {
      doAction(43, "changeToRVF");
      showDialog(3001);
    } 
    return super.onOptionsItemSelected(paramMenuItem);
  }
  
  protected void onPause() {
    super.onPause();
    Trace.i(this.TAG, "onPause()-111");
    getWindow().addFlags(128);
  }
  
  protected void onRestart() {
    super.onRestart();
    Trace.i(this.TAG, "onRestart()-111");
    getWindow().addFlags(128);
  }
  
  protected void onResume() {
    super.onResume();
    Log.d("Test00", "**** filesharing onresume");
    Trace.i(this.TAG, "onResume()-111");
    getWindow().addFlags(128);
    if (bFileReceiveDone) {
      showDialog(1008);
      bFileReceiveDone = false;
    } 
  }
  
  protected void onStart() {
    super.onStart();
    Trace.i(this.TAG, "onStart()-111");
  }
  
  protected void onStop() {
    super.onStop();
    Trace.i(this.TAG, "onStop()-111");
  }
  
  public void refreshDialog() {
    if (this.detail_view_dialog != null && this.detail_view_dialog.isShowing()) {
      this.detail_view_dialog.dismiss();
      this.detail_view_dialog = new Dialog((Context)this);
      this.detail_view_dialog.requestWindowFeature(1);
      this.detail_view_dialog.setContentView(2130903075);
      this.detail_view_dialog.setCanceledOnTouchOutside(false);
      this.checkBox = (CheckBox)this.detail_view_dialog.findViewById(2131558585);
      this.detail_view_dialog.setCanceledOnTouchOutside(false);
      this.checkBox.setButtonDrawable(2130838239);
      this.checkBox.setChecked(this.isChecked);
      float f = (getResources().getDisplayMetrics()).density;
      this.checkBox.setPadding(this.checkBox.getPaddingLeft() + (int)(10.0F * f + 0.5F), this.checkBox.getPaddingTop(), this.checkBox.getPaddingRight(), this.checkBox.getPaddingBottom());
      ((Button)this.detail_view_dialog.findViewById(2131558586)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              if (FileSharing.this.checkBox.isChecked()) {
                FileSharing.this.isChecked = true;
                Utils.setDisplayDetailGuide(FileSharing.this.getApplicationContext(), false);
              } 
              FileSharing.this.detail_view_dialog.dismiss();
              FileSharing.this.detail_view_dialog = null;
              Toast.makeText((Context)FileSharing.this, 2131361894, 1).show();
            }
          });
      this.detail_view_dialog.show();
    } 
  }
  
  public void selectAllItem() {
    // Byte code:
    //   0: aload_0
    //   1: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   4: ldc_w 'start selectAllItem()'
    //   7: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   10: aload_0
    //   11: getfield mSortingMode : I
    //   14: iconst_1
    //   15: if_icmpne -> 180
    //   18: sipush #1000
    //   21: aload_0
    //   22: getfield mContentArrayAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ContentArrayAdapter;
    //   25: invokevirtual getCheckedItemCount : ()I
    //   28: isub
    //   29: istore_2
    //   30: iconst_0
    //   31: istore_1
    //   32: iload_1
    //   33: aload_0
    //   34: getfield mContentArrayAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ContentArrayAdapter;
    //   37: invokevirtual getCount : ()I
    //   40: if_icmplt -> 84
    //   43: aload_0
    //   44: getfield mContentArrayAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ContentArrayAdapter;
    //   47: invokevirtual notifyDataSetChanged : ()V
    //   50: aload_0
    //   51: invokespecial onChangedSelectItemCount : ()V
    //   54: aload_0
    //   55: getfield mSortingMode : I
    //   58: iconst_1
    //   59: if_icmpne -> 581
    //   62: aload_0
    //   63: getfield mContentArrayAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ContentArrayAdapter;
    //   66: invokevirtual getCount : ()I
    //   69: sipush #1000
    //   72: if_icmple -> 83
    //   75: aload_0
    //   76: getfield mExToast : Lcom/samsungimaging/connectionmanager/util/ExToast;
    //   79: iconst_4
    //   80: invokevirtual show : (I)V
    //   83: return
    //   84: iload_2
    //   85: istore_3
    //   86: aload_0
    //   87: getfield mContentArrayAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ContentArrayAdapter;
    //   90: iload_1
    //   91: invokevirtual getItem : (I)Ljava/lang/Object;
    //   94: checkcast com/samsungimaging/connectionmanager/app/pullservice/demo/ml/Item
    //   97: invokevirtual getItemState : ()I
    //   100: iconst_1
    //   101: if_icmpne -> 171
    //   104: invokestatic getAvailableExternalMemorySize : ()J
    //   107: aload_0
    //   108: getfield mContentArrayAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ContentArrayAdapter;
    //   111: invokevirtual getCheckedItemSize : ()J
    //   114: aload_0
    //   115: getfield mContentArrayAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ContentArrayAdapter;
    //   118: iload_1
    //   119: invokevirtual getItem : (I)Ljava/lang/Object;
    //   122: checkcast com/samsungimaging/connectionmanager/app/pullservice/demo/ml/Item
    //   125: invokevirtual getSize : ()Ljava/lang/String;
    //   128: invokestatic parseLong : (Ljava/lang/String;)J
    //   131: ladd
    //   132: lcmp
    //   133: ifgt -> 146
    //   136: aload_0
    //   137: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   140: ldc_w 'Warning memory full 00'
    //   143: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   146: iload_2
    //   147: istore_3
    //   148: iload_2
    //   149: ifle -> 171
    //   152: iload_2
    //   153: iconst_1
    //   154: isub
    //   155: istore_3
    //   156: aload_0
    //   157: getfield mContentArrayAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ContentArrayAdapter;
    //   160: iload_1
    //   161: invokevirtual getItem : (I)Ljava/lang/Object;
    //   164: checkcast com/samsungimaging/connectionmanager/app/pullservice/demo/ml/Item
    //   167: iconst_2
    //   168: invokevirtual setItemState : (I)V
    //   171: iload_1
    //   172: iconst_1
    //   173: iadd
    //   174: istore_1
    //   175: iload_3
    //   176: istore_2
    //   177: goto -> 32
    //   180: aload_0
    //   181: getfield mSortingMode : I
    //   184: iconst_2
    //   185: if_icmpne -> 50
    //   188: sipush #1000
    //   191: aload_0
    //   192: getfield mExpandableListAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;
    //   195: invokevirtual getCheckedItemCount : ()I
    //   198: isub
    //   199: istore_3
    //   200: iconst_0
    //   201: istore_1
    //   202: iload_1
    //   203: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing.mGroupCollection : Ljava/util/List;
    //   206: invokeinterface size : ()I
    //   211: if_icmpge -> 50
    //   214: iload_3
    //   215: ifle -> 50
    //   218: aload_0
    //   219: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   222: new java/lang/StringBuilder
    //   225: dup
    //   226: ldc_w 'i : '
    //   229: invokespecial <init> : (Ljava/lang/String;)V
    //   232: iload_1
    //   233: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   236: invokevirtual toString : ()Ljava/lang/String;
    //   239: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   242: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing.mGroupCollection : Ljava/util/List;
    //   245: iload_1
    //   246: invokeinterface get : (I)Ljava/lang/Object;
    //   251: checkcast com/samsungimaging/connectionmanager/app/pullservice/demo/ml/entity/GroupEntity
    //   254: astore #6
    //   256: iconst_0
    //   257: istore_2
    //   258: iload_2
    //   259: aload #6
    //   261: getfield GroupItemCollection : Ljava/util/List;
    //   264: invokeinterface size : ()I
    //   269: if_icmplt -> 337
    //   272: aload_0
    //   273: getfield mExpandableListAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;
    //   276: iload_1
    //   277: invokevirtual getGroupUncheckedItemCount : (I)I
    //   280: ifne -> 330
    //   283: aload #6
    //   285: iconst_1
    //   286: putfield groupCheckedAllStatus : Z
    //   289: aload_0
    //   290: getfield mExpandableListAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;
    //   293: getfield mGroupMap : Ljava/util/HashMap;
    //   296: iload_1
    //   297: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   300: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   303: ifnull -> 330
    //   306: aload_0
    //   307: getfield mExpandableListAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;
    //   310: getfield mGroupMap : Ljava/util/HashMap;
    //   313: iload_1
    //   314: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   317: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   320: checkcast com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$GroupHolder
    //   323: getfield title : Landroid/widget/CheckBox;
    //   326: iconst_1
    //   327: invokevirtual setChecked : (Z)V
    //   330: iload_1
    //   331: iconst_1
    //   332: iadd
    //   333: istore_1
    //   334: goto -> 202
    //   337: aload_0
    //   338: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   341: new java/lang/StringBuilder
    //   344: dup
    //   345: ldc_w 'j : '
    //   348: invokespecial <init> : (Ljava/lang/String;)V
    //   351: iload_2
    //   352: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   355: invokevirtual toString : ()Ljava/lang/String;
    //   358: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   361: iload_3
    //   362: ifle -> 272
    //   365: aload #6
    //   367: getfield GroupItemCollection : Ljava/util/List;
    //   370: iload_2
    //   371: invokeinterface get : (I)Ljava/lang/Object;
    //   376: checkcast com/samsungimaging/connectionmanager/app/pullservice/demo/ml/entity/GroupEntity$GroupItemEntity
    //   379: astore #7
    //   381: iconst_0
    //   382: istore #4
    //   384: iload #4
    //   386: aload #7
    //   388: getfield ItemList : Ljava/util/ArrayList;
    //   391: invokevirtual size : ()I
    //   394: if_icmplt -> 404
    //   397: iload_2
    //   398: iconst_1
    //   399: iadd
    //   400: istore_2
    //   401: goto -> 258
    //   404: aload_0
    //   405: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   408: new java/lang/StringBuilder
    //   411: dup
    //   412: ldc_w 'k : '
    //   415: invokespecial <init> : (Ljava/lang/String;)V
    //   418: iload #4
    //   420: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   423: invokevirtual toString : ()Ljava/lang/String;
    //   426: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   429: aload #7
    //   431: getfield ItemList : Ljava/util/ArrayList;
    //   434: iload #4
    //   436: invokevirtual get : (I)Ljava/lang/Object;
    //   439: checkcast com/samsungimaging/connectionmanager/app/pullservice/demo/ml/Item
    //   442: astore #8
    //   444: iload_3
    //   445: istore #5
    //   447: aload #8
    //   449: invokevirtual getItemState : ()I
    //   452: iconst_1
    //   453: if_icmpne -> 569
    //   456: aload_0
    //   457: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   460: ldc_w 'selectAllItem step 1'
    //   463: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   466: aload_0
    //   467: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   470: ldc_w 'selectAllItem step 2'
    //   473: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   476: iload_3
    //   477: ifle -> 397
    //   480: aload_0
    //   481: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   484: ldc_w 'selectAllItem step 3'
    //   487: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   490: iload_3
    //   491: iconst_1
    //   492: isub
    //   493: istore_3
    //   494: aload #8
    //   496: iconst_2
    //   497: invokevirtual setItemState : (I)V
    //   500: iload_3
    //   501: istore #5
    //   503: aload_0
    //   504: getfield mExpandableListAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;
    //   507: getfield mChildMap : Ljava/util/HashMap;
    //   510: aload #8
    //   512: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   515: ifnull -> 569
    //   518: iload_3
    //   519: istore #5
    //   521: aload #8
    //   523: aload_0
    //   524: getfield mExpandableListAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;
    //   527: getfield mChildMap : Ljava/util/HashMap;
    //   530: aload #8
    //   532: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   535: checkcast android/widget/CheckBox
    //   538: invokevirtual getTag : ()Ljava/lang/Object;
    //   541: invokevirtual equals : (Ljava/lang/Object;)Z
    //   544: ifeq -> 569
    //   547: aload_0
    //   548: getfield mExpandableListAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;
    //   551: getfield mChildMap : Ljava/util/HashMap;
    //   554: aload #8
    //   556: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   559: checkcast android/widget/CheckBox
    //   562: iconst_1
    //   563: invokevirtual setChecked : (Z)V
    //   566: iload_3
    //   567: istore #5
    //   569: iload #4
    //   571: iconst_1
    //   572: iadd
    //   573: istore #4
    //   575: iload #5
    //   577: istore_3
    //   578: goto -> 384
    //   581: aload_0
    //   582: getfield mSortingMode : I
    //   585: iconst_2
    //   586: if_icmpne -> 83
    //   589: aload_0
    //   590: getfield mExpandableListAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;
    //   593: invokevirtual getItemCount : ()I
    //   596: sipush #1000
    //   599: if_icmple -> 83
    //   602: aload_0
    //   603: getfield mExToast : Lcom/samsungimaging/connectionmanager/util/ExToast;
    //   606: iconst_4
    //   607: invokevirtual show : (I)V
    //   610: return
  }
  
  public void setGroupCollection(int paramInt) {
    Trace.d(this.TAG, "start setGroupCollection() columnNum : " + paramInt);
    for (int i = 0;; i++) {
      if (i >= mGroupCollection.size()) {
        i = 0;
        label25: while (true) {
          if (i >= mContainerList.size())
            return; 
          Item item = mContainerList.get(i);
          int j = 0;
          while (true) {
            if (j < mGroupCollection.size()) {
              GroupEntity groupEntity1 = mGroupCollection.get(j);
              if (groupEntity1.Name.equals(item.getDate())) {
                if (((GroupEntity.GroupItemEntity)groupEntity1.GroupItemCollection.get(groupEntity1.GroupItemCollection.size() - 1)).ItemList.size() < paramInt) {
                  ((GroupEntity.GroupItemEntity)groupEntity1.GroupItemCollection.get(groupEntity1.GroupItemCollection.size() - 1)).ItemList.add(item);
                } else {
                  groupEntity1.getClass();
                  GroupEntity.GroupItemEntity groupItemEntity1 = new GroupEntity.GroupItemEntity(groupEntity1);
                  groupItemEntity1.ItemList = new ArrayList();
                  groupItemEntity1.ItemList.add(item);
                  groupEntity1.GroupItemCollection.add(groupItemEntity1);
                } 
              } else {
                j++;
                continue;
              } 
            } 
            if (j == mGroupCollection.size()) {
              GroupEntity groupEntity1 = new GroupEntity();
              groupEntity1.Name = item.getDate();
              groupEntity1.getClass();
              GroupEntity.GroupItemEntity groupItemEntity1 = new GroupEntity.GroupItemEntity(groupEntity1);
              groupItemEntity1.ItemList = new ArrayList();
              groupItemEntity1.ItemList.add(item);
              groupEntity1.GroupItemCollection.add(groupItemEntity1);
              mGroupCollection.add(groupEntity1);
            } 
            i++;
            continue label25;
          } 
          break;
        } 
        break;
      } 
      GroupEntity groupEntity = mGroupCollection.get(i);
      groupEntity.GroupItemCollection.clear();
      groupEntity.getClass();
      GroupEntity.GroupItemEntity groupItemEntity = new GroupEntity.GroupItemEntity(groupEntity);
      groupItemEntity.ItemList = new ArrayList();
      groupEntity.GroupItemCollection.add(groupItemEntity);
    } 
  }
  
  public void unSelectAllItem() {
    Trace.d(this.TAG, "start unSelectAllItem()");
    if (this.mSortingMode == 1) {
      int i = 0;
      while (true) {
        if (i >= this.mContentArrayAdapter.getCount()) {
          this.mContentArrayAdapter.notifyDataSetChanged();
        } else {
          if (((Item)this.mContentArrayAdapter.getItem(i)).getItemState() == 2)
            ((Item)this.mContentArrayAdapter.getItem(i)).setItemState(1); 
          i++;
          continue;
        } 
        onChangedSelectItemCount();
        return;
      } 
    } 
    if (this.mSortingMode == 2) {
      int i = 0;
      label38: while (true) {
        if (i < mGroupCollection.size()) {
          GroupEntity groupEntity = mGroupCollection.get(i);
          int j = 0;
          label36: while (true) {
            if (j >= groupEntity.GroupItemCollection.size()) {
              groupEntity.groupCheckedAllStatus = false;
              if (this.mExpandableListAdapter.mGroupMap.get(Integer.valueOf(i)) != null)
                ((ExpandableListAdapter.GroupHolder)this.mExpandableListAdapter.mGroupMap.get(Integer.valueOf(i))).title.setChecked(false); 
              i++;
              continue label38;
            } 
            GroupEntity.GroupItemEntity groupItemEntity = groupEntity.GroupItemCollection.get(j);
            for (int k = 0;; k++) {
              if (k >= groupItemEntity.ItemList.size()) {
                j++;
                continue label36;
              } 
              Item item = groupItemEntity.ItemList.get(k);
              if (item.getItemState() == 2) {
                item.setItemState(1);
                if (this.mExpandableListAdapter.mChildMap.get(item) != null)
                  ((CheckBox)this.mExpandableListAdapter.mChildMap.get(item)).setChecked(false); 
              } 
            } 
            break;
          } 
          break;
        } 
        onChangedSelectItemCount();
        return;
      } 
    } 
    onChangedSelectItemCount();
  }
  
  public void updateSelectedState() {
    if (this.mSortingMode == 1) {
      int i = 0;
      while (true) {
        if (i >= this.mContentArrayAdapter.getCount()) {
          this.mContentArrayAdapter.notifyDataSetChanged();
        } else {
          if (((Item)this.mContentArrayAdapter.getItem(i)).getItemState() == 3)
            ((Item)this.mContentArrayAdapter.getItem(i)).setItemState(1); 
          i++;
          continue;
        } 
        onChangedSelectItemCount();
        return;
      } 
    } 
    if (this.mSortingMode == 2) {
      int i = 0;
      label36: while (true) {
        if (i < mGroupCollection.size()) {
          GroupEntity groupEntity = mGroupCollection.get(i);
          int j = 0;
          label34: while (true) {
            if (j >= groupEntity.GroupItemCollection.size()) {
              i++;
              continue label36;
            } 
            GroupEntity.GroupItemEntity groupItemEntity = groupEntity.GroupItemCollection.get(j);
            for (int k = 0;; k++) {
              if (k >= groupItemEntity.ItemList.size()) {
                j++;
                continue label34;
              } 
              Item item = groupItemEntity.ItemList.get(k);
              if (item.getItemState() == 3) {
                item.setItemState(1);
                if (this.mExpandableListAdapter.mChildMap.get(item) != null) {
                  ((CheckBox)this.mExpandableListAdapter.mChildMap.get(item)).setChecked(false);
                  groupEntity.groupCheckedAllStatus = false;
                  if (this.mExpandableListAdapter.mGroupMap.get(Integer.valueOf(i)) != null)
                    ((ExpandableListAdapter.GroupHolder)this.mExpandableListAdapter.mGroupMap.get(Integer.valueOf(i))).title.setChecked(false); 
                } 
              } 
            } 
            break;
          } 
          break;
        } 
        onChangedSelectItemCount();
        return;
      } 
    } 
    onChangedSelectItemCount();
  }
  
  public class MyPhoneStateListener extends PhoneStateListener {
    public void onCallStateChanged(int param1Int, String param1String) {
      switch (param1Int) {
      
      } 
    }
  }
  
  private class SdcardStateChangedReceiver extends BroadcastReceiver {
    public void onReceive(Context param1Context, Intent param1Intent) {
      Trace.e(FileSharing.this.TAG, "Sdcard onReceive() -- " + param1Intent.getAction());
      if (param1Intent.getAction().equals("android.intent.action.MEDIA_REMOVED") || param1Intent.getAction().equals("android.intent.action.MEDIA_UNMOUNTED") || param1Intent.getAction().equals("android.intent.action.MEDIA_SHARED") || param1Intent.getAction().equals("android.intent.action.MEDIA_BAD_REMOVAL") || param1Intent.getAction().equals("android.intent.action.MEDIA_UNMOUNTABLE")) {
        Message message = new Message();
        message.what = 22;
        FileSharing.this.doSendMessageDelayed(message, 1000L);
      } 
    }
  }
  
  public class SortingMode {
    public static final int DATE = 2;
    
    public static final int FILE_NAME = 3;
    
    public static final int FILE_SIZE = 4;
    
    public static final int NORMAL = 1;
  }
  
  private class UnsubscribeTask extends AsyncTask<Device, Void, Void> {
    private UnsubscribeTask() {}
    
    protected Void doInBackground(Device... param1VarArgs) {
      Trace.d(FileSharing.this.TAG, "start UnsubscribeTask.doInBackground()");
      if (FileSharing.this.upnpController != null && FileSharing.this.connectedSsid != null && param1VarArgs[0] != null)
        FileSharing.this.upnpController.unsubscribe(param1VarArgs[0]); 
      return null;
    }
    
    protected void onPostExecute(Void param1Void) {
      Trace.d(FileSharing.this.TAG, "start UnsubscribeTask.onPostExecute()");
      FileSharing.this.appClose();
      if (FileSharing.this.bCloseByFinishSafe)
        CMService.getInstance().finishSafe(); 
    }
    
    protected void onPreExecute() {
      if (!FileSharing.this.isFinishing())
        FileSharing.this.showDialog(1003); 
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\ml\FileSharing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */