package com.samsungimaging.connectionmanager.app.pullservice.demo.rvf;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.samsungimaging.asphodel.multimedia.FFmpegJNI;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;
import com.samsungimaging.connectionmanager.app.localgallery.LocalGallery;
import com.samsungimaging.connectionmanager.app.pullservice.FunctionManager;
import com.samsungimaging.connectionmanager.app.pullservice.PullService;
import com.samsungimaging.connectionmanager.app.pullservice.controller.DeviceController;
import com.samsungimaging.connectionmanager.app.pullservice.controller.UPNPController;
import com.samsungimaging.connectionmanager.app.pullservice.datatype.DSCMenuItem;
import com.samsungimaging.connectionmanager.app.pullservice.datatype.DSCMovieResolution;
import com.samsungimaging.connectionmanager.app.pullservice.datatype.DSCResolution;
import com.samsungimaging.connectionmanager.app.pullservice.demo.ml.FileSharing;
import com.samsungimaging.connectionmanager.app.pullservice.demo.rvf.common.Timer;
import com.samsungimaging.connectionmanager.app.pullservice.demo.rvf.configuration.manager.SRVFConfigurationManager;
import com.samsungimaging.connectionmanager.app.pullservice.demo.rvf.dialog.CustomDialogListMenu;
import com.samsungimaging.connectionmanager.app.pullservice.demo.rvf.dialog.CustomDialogWheelMenu;
import com.samsungimaging.connectionmanager.app.pullservice.demo.util.CommonUtils;
import com.samsungimaging.connectionmanager.app.pullservice.util.Graph;
import com.samsungimaging.connectionmanager.app.pullservice.util.ImageDownloader;
import com.samsungimaging.connectionmanager.dialog.CustomDialog;
import com.samsungimaging.connectionmanager.util.ExToast;
import com.samsungimaging.connectionmanager.util.Trace;
import com.samsungimaging.connectionmanager.widget.VerticalSeekBar;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;
import org.kankan.wheel.widget.OnWheelClickedListener;
import org.kankan.wheel.widget.OnWheelScrollListener;
import org.kankan.wheel.widget.WheelView;
import org.kankan.wheel.widget.adapters.ArrayWheelAdapter;
import org.kankan.wheel.widget.adapters.WheelViewAdapter;

public final class LiveShutter extends PullService implements View.OnClickListener, View.OnTouchListener, SurfaceHolder.Callback, OnWheelScrollListener, OnWheelClickedListener {
  private static Context Appcontext;
  
  public static Device DSCdevice;
  
  public static final int LENGTH_ENDLESS = 1;
  
  public static final int LENGTH_SHORT = 0;
  
  private static boolean bClosing;
  
  public static boolean bNoTelephonyDevice;
  
  private static boolean bVersionFail;
  
  static boolean bappclose;
  
  private static Bitmap bgbitmap;
  
  private static Bitmap bgbitmap1;
  
  private static Bitmap bmImg;
  
  static boolean bsaveflag;
  
  static boolean bshutter;
  
  private static char cBmpRotation;
  
  private static String cam_rotated_type;
  
  private static boolean codec_init;
  
  private static boolean configChanged;
  
  public static long connectionTimeout = 0L;
  
  static Handler dHandler;
  
  private static SurfaceHolder holder;
  
  private static boolean isButtonShow;
  
  private static boolean isShotProcessing;
  
  private static boolean mExistStatusBarDeviceForTablet;
  
  static Handler mHandler;
  
  private static double mLatitude;
  
  private static double mLongitude;
  
  static Handler msgHandler;
  
  private static int nCanvasX;
  
  private static int nCanvasY;
  
  private static int nDisplayFlag;
  
  private static int nHolderLongWidth;
  
  private static int nHolderShortHeight;
  
  private static int nHolderShortWidth;
  
  private static int nShutterDnUp;
  
  private static int orientation;
  
  private static RelativeLayout rvf_QuickSettingMenu;
  
  private static ImageView rvf_af3x3_00;
  
  private static ImageView rvf_af3x3_01;
  
  private static ImageView rvf_af3x3_02;
  
  private static ImageView rvf_af3x3_10;
  
  private static ImageView rvf_af3x3_11;
  
  private static ImageView rvf_af3x3_12;
  
  private static ImageView rvf_af3x3_20;
  
  private static ImageView rvf_af3x3_21;
  
  private static ImageView rvf_af3x3_22;
  
  private static ImageView rvf_af3x5_00;
  
  private static ImageView rvf_af3x5_01;
  
  private static ImageView rvf_af3x5_02;
  
  private static ImageView rvf_af3x5_03;
  
  private static ImageView rvf_af3x5_04;
  
  private static ImageView rvf_af3x5_10;
  
  private static ImageView rvf_af3x5_11;
  
  private static ImageView rvf_af3x5_12;
  
  private static ImageView rvf_af3x5_13;
  
  private static ImageView rvf_af3x5_14;
  
  private static ImageView rvf_af3x5_20;
  
  private static ImageView rvf_af3x5_21;
  
  private static ImageView rvf_af3x5_22;
  
  private static ImageView rvf_af3x5_23;
  
  private static ImageView rvf_af3x5_24;
  
  private static ImageView rvf_af3x7_00;
  
  private static ImageView rvf_af3x7_01;
  
  private static ImageView rvf_af3x7_02;
  
  private static ImageView rvf_af3x7_03;
  
  private static ImageView rvf_af3x7_04;
  
  private static ImageView rvf_af3x7_05;
  
  private static ImageView rvf_af3x7_06;
  
  private static ImageView rvf_af3x7_10;
  
  private static ImageView rvf_af3x7_11;
  
  private static ImageView rvf_af3x7_12;
  
  private static ImageView rvf_af3x7_13;
  
  private static ImageView rvf_af3x7_14;
  
  private static ImageView rvf_af3x7_15;
  
  private static ImageView rvf_af3x7_16;
  
  private static ImageView rvf_af3x7_20;
  
  private static ImageView rvf_af3x7_21;
  
  private static ImageView rvf_af3x7_22;
  
  private static ImageView rvf_af3x7_23;
  
  private static ImageView rvf_af3x7_24;
  
  private static ImageView rvf_af3x7_25;
  
  private static ImageView rvf_af3x7_26;
  
  private static RelativeLayout rvf_af_extend;
  
  private static ImageButton rvf_cameramore_button;
  
  private static ImageButton rvf_camerasave_button;
  
  private static ImageButton rvf_close_button;
  
  private static ImageButton rvf_flash_button;
  
  private static ImageView rvf_metering_spot_frame;
  
  private static ImageButton rvf_open_button;
  
  private static ImageButton rvf_photosize_button;
  
  private static ImageButton rvf_timer_button;
  
  private static TextView rvf_title;
  
  private static ImageView rvf_touchAutoFocus;
  
  private static RelativeLayout rvf_zoomMain;
  
  private static SeekBar rvf_zoomSeekBar;
  
  private static ImageButton rvf_zoomin_button;
  
  private static ImageButton rvf_zoomout_button;
  
  private static Bitmap thumbnail_bmp;
  
  private String[] AFData;
  
  private int AfFrameType = 0;
  
  private int REQUESTIMAGE = 1;
  
  private Trace.Tag TAG = Trace.Tag.RVF;
  
  private boolean bAFFAIL = false;
  
  private boolean bCloseByFinishSafe = false;
  
  private boolean bConnect = false;
  
  private boolean bNetworkGPS = false;
  
  private boolean bOnZoomProcess = false;
  
  private boolean bStart = false;
  
  private boolean bexitflag = false;
  
  private boolean bpopupflag = false;
  
  private boolean bshotrunflag = false;
  
  private boolean btimercount = false;
  
  private boolean btoast = false;
  
  private boolean btoastflag = false;
  
  private String cameratype = "ssdp:rotationD";
  
  Button closenotice;
  
  private Location currentLocation_gps = null;
  
  private Location currentLocation_network = null;
  
  private ProgressDialog customProgressBar = null;
  
  private FrameCounterTask frameCounter;
  
  private float gDensity = 0.0F;
  
  private int gDeviceHeight = 0;
  
  private int gDeviceWidth = 0;
  
  private int gHeightDips = 0;
  
  private int gWidthDips = 0;
  
  private boolean isByeBye = false;
  
  private boolean isExitByBackPressed = false;
  
  private boolean isInitializedCamera = false;
  
  private boolean isLongZoom = false;
  
  private boolean isOperationStateChange = false;
  
  private boolean isOptimusVu = false;
  
  private boolean isSettedLiveStream = false;
  
  LinearLayout layout;
  
  private LocationManager locationManager;
  
  private int mActionState = 0;
  
  private SRVFConfigurationManager mConfigurationManager;
  
  private AlertDialog mConnectionFail = null;
  
  private int mCurPopupWindowId = 0;
  
  private int mCurrentMainOptionMenuId = 0;
  
  private int mCurrentSubOptionMenuId = 0;
  
  private boolean mDestroyed = false;
  
  private Handler mDeviceControlHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        // Byte code:
        //   0: aload_0
        //   1: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   7: new java/lang/StringBuilder
        //   10: dup
        //   11: ldc 'apk version : '
        //   13: invokespecial <init> : (Ljava/lang/String;)V
        //   16: aload_0
        //   17: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   20: invokestatic access$53 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Ljava/lang/String;
        //   23: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   26: invokevirtual toString : ()Ljava/lang/String;
        //   29: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   32: aload_1
        //   33: getfield what : I
        //   36: tableswitch default -> 404, 0 -> 404, 1 -> 404, 2 -> 404, 3 -> 405, 4 -> 837, 5 -> 857, 6 -> 1194, 7 -> 1224, 8 -> 1371, 9 -> 1401, 10 -> 1535, 11 -> 1543, 12 -> 1593, 13 -> 1601, 14 -> 404, 15 -> 1662, 16 -> 404, 17 -> 1705, 18 -> 1825, 19 -> 1856, 20 -> 2273, 21 -> 2304, 22 -> 3248, 23 -> 3256, 24 -> 3342, 25 -> 3360, 26 -> 3713, 27 -> 3758, 28 -> 3915, 29 -> 3990, 30 -> 3915, 31 -> 3990, 32 -> 4665, 33 -> 4686, 34 -> 4874, 35 -> 4895, 36 -> 5562, 37 -> 5570, 38 -> 5733, 39 -> 5751, 40 -> 404, 41 -> 404, 42 -> 5828, 43 -> 5836, 44 -> 5896, 45 -> 5904, 46 -> 5964, 47 -> 5972, 48 -> 6032, 49 -> 6040, 50 -> 6100, 51 -> 6108, 52 -> 6194, 53 -> 6202, 54 -> 6354, 55 -> 6362, 56 -> 6438, 57 -> 6446, 58 -> 6623, 59 -> 6631, 60 -> 6727, 61 -> 6738, 62 -> 404, 63 -> 6790, 64 -> 6883, 65 -> 6891, 66 -> 404, 67 -> 7562, 68 -> 404, 69 -> 7803, 70 -> 404, 71 -> 7857, 72 -> 8080, 73 -> 8139, 74 -> 8230, 75 -> 8238, 76 -> 404, 77 -> 404, 78 -> 404, 79 -> 8376, 80 -> 404, 81 -> 8414, 82 -> 404, 83 -> 404, 84 -> 404, 85 -> 404, 86 -> 404, 87 -> 8459
        //   404: return
        //   405: aload_1
        //   406: getfield obj : Ljava/lang/Object;
        //   409: checkcast java/lang/Boolean
        //   412: invokevirtual booleanValue : ()Z
        //   415: ifeq -> 793
        //   418: aload_0
        //   419: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   422: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   425: invokevirtual getFileSaveValue : ()Ljava/lang/String;
        //   428: ldc ''
        //   430: invokevirtual equals : (Ljava/lang/Object;)Z
        //   433: ifne -> 471
        //   436: aload_0
        //   437: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   440: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   443: invokevirtual getFileSaveMenuItems : ()Ljava/util/ArrayList;
        //   446: invokevirtual size : ()I
        //   449: ifle -> 471
        //   452: iconst_0
        //   453: istore_2
        //   454: iload_2
        //   455: aload_0
        //   456: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   459: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   462: invokevirtual getFileSaveMenuItems : ()Ljava/util/ArrayList;
        //   465: invokevirtual size : ()I
        //   468: if_icmplt -> 685
        //   471: aload_0
        //   472: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   475: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   478: invokevirtual getRatioOffsetMenuItems : ()Ljava/util/ArrayList;
        //   481: invokevirtual size : ()I
        //   484: ifle -> 736
        //   487: aload_0
        //   488: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   491: iconst_2
        //   492: invokestatic access$54 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   495: aload_0
        //   496: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   499: aload_0
        //   500: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   503: invokevirtual getPhoneCamType : ()I
        //   506: invokestatic access$55 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   509: aload_0
        //   510: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   513: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   516: invokevirtual getAFAreaMenuItems : ()Ljava/util/ArrayList;
        //   519: invokevirtual size : ()I
        //   522: ifle -> 759
        //   525: aload_0
        //   526: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   529: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   532: invokevirtual getAFAreaValue : ()Ljava/lang/String;
        //   535: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   538: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   541: ldc 'SELECTION AF'
        //   543: invokevirtual equals : (Ljava/lang/Object;)Z
        //   546: ifeq -> 759
        //   549: invokestatic access$36 : ()Landroid/widget/ImageView;
        //   552: ldc 2130837575
        //   554: invokevirtual setImageResource : (I)V
        //   557: invokestatic access$36 : ()Landroid/widget/ImageView;
        //   560: iconst_0
        //   561: invokevirtual setVisibility : (I)V
        //   564: iconst_0
        //   565: putstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.bshutter : Z
        //   568: aload_0
        //   569: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   572: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   575: ldc 'MESSAGE : DISPLAY_BUTTON_SHOW'
        //   577: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   580: bipush #34
        //   582: invokestatic access$28 : (I)V
        //   585: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   588: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   591: dup
        //   592: aload_0
        //   593: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   596: invokestatic access$29 : ()I
        //   599: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   602: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   605: pop
        //   606: invokestatic access$56 : ()Landroid/widget/SeekBar;
        //   609: aload_0
        //   610: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   613: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   616: invokevirtual getMaxZoom : ()Ljava/lang/String;
        //   619: invokestatic parseInt : (Ljava/lang/String;)I
        //   622: aload_0
        //   623: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   626: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   629: invokevirtual getMinZoom : ()Ljava/lang/String;
        //   632: invokestatic parseInt : (Ljava/lang/String;)I
        //   635: isub
        //   636: invokevirtual setMax : (I)V
        //   639: aload_0
        //   640: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   643: invokestatic access$57 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   646: aload_0
        //   647: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   650: invokestatic access$31 : ()Landroid/widget/ImageButton;
        //   653: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   656: aload_0
        //   657: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   660: aload_0
        //   661: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   664: invokestatic access$30 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   667: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   670: aload_0
        //   671: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   674: invokestatic access$58 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   677: aload_0
        //   678: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   681: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   684: return
        //   685: aload_0
        //   686: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   689: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   692: invokevirtual getFileSaveMenuItems : ()Ljava/util/ArrayList;
        //   695: iload_2
        //   696: invokevirtual get : (I)Ljava/lang/Object;
        //   699: checkcast java/lang/String
        //   702: aload_0
        //   703: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   706: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   709: invokevirtual getFileSaveValue : ()Ljava/lang/String;
        //   712: invokevirtual equals : (Ljava/lang/Object;)Z
        //   715: ifeq -> 729
        //   718: aload_0
        //   719: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   722: iload_2
        //   723: invokestatic access$9 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   726: goto -> 471
        //   729: iload_2
        //   730: iconst_1
        //   731: iadd
        //   732: istore_2
        //   733: goto -> 454
        //   736: aload_0
        //   737: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   740: aload_0
        //   741: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   744: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   747: invokevirtual getRatioValue : ()Ljava/lang/String;
        //   750: invokestatic stringToRatioType : (Ljava/lang/String;)I
        //   753: invokestatic access$54 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   756: goto -> 495
        //   759: aload_0
        //   760: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   763: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   766: invokevirtual getAFAreaValue : ()Ljava/lang/String;
        //   769: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   772: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   775: ldc 'MULTI AF'
        //   777: invokevirtual equals : (Ljava/lang/Object;)Z
        //   780: ifeq -> 564
        //   783: invokestatic access$36 : ()Landroid/widget/ImageView;
        //   786: iconst_4
        //   787: invokevirtual setVisibility : (I)V
        //   790: goto -> 564
        //   793: aload_0
        //   794: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   797: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   800: ldc 'MESSAGE : DISPLAY_NETERRORMSG'
        //   802: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   805: bipush #10
        //   807: invokestatic access$28 : (I)V
        //   810: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   813: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   816: dup
        //   817: aload_0
        //   818: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   821: invokestatic access$29 : ()I
        //   824: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   827: ldc2_w 10
        //   830: invokevirtual postDelayed : (Ljava/lang/Runnable;J)Z
        //   833: pop
        //   834: goto -> 677
        //   837: aload_0
        //   838: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   841: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   844: ldc 'Performance Check Point : Set Resolution Action Start'
        //   846: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   849: aload_0
        //   850: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   853: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   856: return
        //   857: aload_1
        //   858: getfield obj : Ljava/lang/Object;
        //   861: checkcast java/lang/Boolean
        //   864: invokevirtual booleanValue : ()Z
        //   867: ifeq -> 1150
        //   870: aload_0
        //   871: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   874: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   877: aload_0
        //   878: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   881: invokestatic access$13 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)I
        //   884: invokestatic valueOf : (I)Ljava/lang/String;
        //   887: invokevirtual setDefaultResolutionIndex : (Ljava/lang/String;)V
        //   890: aload_0
        //   891: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   894: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   897: invokevirtual getVersionPrefix : ()Ljava/lang/String;
        //   900: aload_0
        //   901: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   904: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   907: invokevirtual getVersion : ()Ljava/lang/String;
        //   910: invokestatic isSupportedSmartPanel : (Ljava/lang/String;Ljava/lang/String;)Z
        //   913: ifeq -> 952
        //   916: aload_0
        //   917: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   920: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   923: iconst_2
        //   924: invokevirtual getAction : (I)Lorg/cybergarage/upnp/Action;
        //   927: invokevirtual getOutputArgumentList : ()Lorg/cybergarage/upnp/ArgumentList;
        //   930: astore_1
        //   931: aload_1
        //   932: invokevirtual size : ()I
        //   935: istore_3
        //   936: iconst_0
        //   937: istore_2
        //   938: iload_2
        //   939: iload_3
        //   940: if_icmplt -> 1041
        //   943: aload_0
        //   944: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   947: bipush #9
        //   949: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   952: aload_0
        //   953: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   956: aload_0
        //   957: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   960: invokestatic access$60 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   963: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   966: aload_0
        //   967: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   970: ldc_w 2131558681
        //   973: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   976: aload_0
        //   977: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   980: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   983: invokevirtual getVersionPrefix : ()Ljava/lang/String;
        //   986: aload_0
        //   987: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   990: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   993: invokevirtual getVersion : ()Ljava/lang/String;
        //   996: invokestatic isSupportedSmartPanel : (Ljava/lang/String;Ljava/lang/String;)Z
        //   999: ifne -> 1020
        //   1002: aload_0
        //   1003: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1006: iconst_2
        //   1007: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   1010: aload_0
        //   1011: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1014: invokestatic access$21 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$MainOptionMenuListAdapter;
        //   1017: invokevirtual notifyDataSetChanged : ()V
        //   1020: aload_0
        //   1021: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1024: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1027: aload_0
        //   1028: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1031: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1034: ldc_w 'Performance Check Point : Set Resolution Action End'
        //   1037: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1040: return
        //   1041: aload_1
        //   1042: iload_2
        //   1043: invokevirtual getArgument : (I)Lorg/cybergarage/upnp/Argument;
        //   1046: astore #7
        //   1048: aload #7
        //   1050: invokevirtual getName : ()Ljava/lang/String;
        //   1053: astore #6
        //   1055: aload #7
        //   1057: invokevirtual getValue : ()Ljava/lang/String;
        //   1060: astore #7
        //   1062: aload_0
        //   1063: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1066: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1069: new java/lang/StringBuilder
        //   1072: dup
        //   1073: ldc_w 'name : '
        //   1076: invokespecial <init> : (Ljava/lang/String;)V
        //   1079: aload #6
        //   1081: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   1084: ldc_w ' value : '
        //   1087: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   1090: aload #7
        //   1092: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   1095: invokevirtual toString : ()Ljava/lang/String;
        //   1098: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1101: aload #6
        //   1103: ldc_w 'RatioChangeStatus'
        //   1106: invokevirtual equals : (Ljava/lang/Object;)Z
        //   1109: ifeq -> 1143
        //   1112: aload #7
        //   1114: ldc_w '1'
        //   1117: invokevirtual equals : (Ljava/lang/Object;)Z
        //   1120: ifeq -> 1143
        //   1123: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$RestartCodecTask
        //   1126: dup
        //   1127: aload_0
        //   1128: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1131: aconst_null
        //   1132: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$RestartCodecTask;)V
        //   1135: iconst_0
        //   1136: anewarray java/lang/Integer
        //   1139: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
        //   1142: pop
        //   1143: iload_2
        //   1144: iconst_1
        //   1145: iadd
        //   1146: istore_2
        //   1147: goto -> 938
        //   1150: aload_0
        //   1151: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1154: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1157: ldc 'MESSAGE : DISPLAY_NETERRORMSG'
        //   1159: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1162: bipush #10
        //   1164: invokestatic access$28 : (I)V
        //   1167: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   1170: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   1173: dup
        //   1174: aload_0
        //   1175: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1178: invokestatic access$29 : ()I
        //   1181: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   1184: ldc2_w 10
        //   1187: invokevirtual postDelayed : (Ljava/lang/Runnable;J)Z
        //   1190: pop
        //   1191: goto -> 976
        //   1194: aload_0
        //   1195: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1198: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1201: ldc_w 'Performance Check Point : Zoom In Action Start'
        //   1204: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1207: aload_0
        //   1208: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1211: iconst_0
        //   1212: invokestatic access$61 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   1215: aload_0
        //   1216: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1219: iconst_0
        //   1220: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   1223: return
        //   1224: aload_1
        //   1225: getfield obj : Ljava/lang/Object;
        //   1228: checkcast java/lang/Boolean
        //   1231: invokevirtual booleanValue : ()Z
        //   1234: ifeq -> 1319
        //   1237: aload_0
        //   1238: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1241: invokestatic access$57 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1244: bipush #30
        //   1246: invokestatic access$28 : (I)V
        //   1249: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   1252: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   1255: dup
        //   1256: aload_0
        //   1257: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1260: invokestatic access$29 : ()I
        //   1263: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   1266: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   1269: pop
        //   1270: aload_0
        //   1271: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1274: invokestatic access$62 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   1277: ifeq -> 1341
        //   1280: aload_0
        //   1281: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1284: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1287: invokevirtual getDefaultZoom : ()Ljava/lang/String;
        //   1290: invokestatic parseInt : (Ljava/lang/String;)I
        //   1293: aload_0
        //   1294: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1297: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1300: invokevirtual getMaxZoom : ()Ljava/lang/String;
        //   1303: invokestatic parseInt : (Ljava/lang/String;)I
        //   1306: if_icmpge -> 1341
        //   1309: aload_0
        //   1310: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1313: iconst_3
        //   1314: ldc ''
        //   1316: invokevirtual doAction : (ILjava/lang/String;)V
        //   1319: aload_0
        //   1320: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1323: iconst_1
        //   1324: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   1327: aload_0
        //   1328: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1331: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1334: ldc_w 'Performance Check Point : Zoom in Action End'
        //   1337: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1340: return
        //   1341: aload_0
        //   1342: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1345: invokestatic access$63 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ProgressBar;
        //   1348: bipush #8
        //   1350: invokevirtual setVisibility : (I)V
        //   1353: aload_0
        //   1354: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1357: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1360: aload_0
        //   1361: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1364: iconst_1
        //   1365: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   1368: goto -> 1319
        //   1371: aload_0
        //   1372: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1375: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1378: ldc_w 'Performance Check Point : Zoom Out Action Start'
        //   1381: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1384: aload_0
        //   1385: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1388: iconst_0
        //   1389: invokestatic access$61 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   1392: aload_0
        //   1393: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1396: iconst_0
        //   1397: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   1400: return
        //   1401: aload_1
        //   1402: getfield obj : Ljava/lang/Object;
        //   1405: checkcast java/lang/Boolean
        //   1408: invokevirtual booleanValue : ()Z
        //   1411: ifeq -> 1483
        //   1414: aload_0
        //   1415: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1418: invokestatic access$57 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1421: bipush #30
        //   1423: invokestatic access$28 : (I)V
        //   1426: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   1429: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   1432: dup
        //   1433: aload_0
        //   1434: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1437: invokestatic access$29 : ()I
        //   1440: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   1443: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   1446: pop
        //   1447: aload_0
        //   1448: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1451: invokestatic access$62 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   1454: ifeq -> 1505
        //   1457: aload_0
        //   1458: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1461: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1464: invokevirtual getDefaultZoom : ()Ljava/lang/String;
        //   1467: invokestatic parseInt : (Ljava/lang/String;)I
        //   1470: ifle -> 1505
        //   1473: aload_0
        //   1474: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1477: iconst_4
        //   1478: ldc ''
        //   1480: invokevirtual doAction : (ILjava/lang/String;)V
        //   1483: aload_0
        //   1484: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1487: iconst_1
        //   1488: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   1491: aload_0
        //   1492: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1495: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1498: ldc_w 'Performance Check Point : Zoom Out Action End'
        //   1501: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1504: return
        //   1505: aload_0
        //   1506: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1509: invokestatic access$63 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ProgressBar;
        //   1512: bipush #8
        //   1514: invokevirtual setVisibility : (I)V
        //   1517: aload_0
        //   1518: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1521: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1524: aload_0
        //   1525: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1528: iconst_1
        //   1529: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   1532: goto -> 1483
        //   1535: aload_0
        //   1536: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1539: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1542: return
        //   1543: aload_1
        //   1544: getfield obj : Ljava/lang/Object;
        //   1547: checkcast java/lang/Boolean
        //   1550: invokevirtual booleanValue : ()Z
        //   1553: ifne -> 1585
        //   1556: bipush #10
        //   1558: invokestatic access$28 : (I)V
        //   1561: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   1564: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   1567: dup
        //   1568: aload_0
        //   1569: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1572: invokestatic access$29 : ()I
        //   1575: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   1578: ldc2_w 10
        //   1581: invokevirtual postDelayed : (Ljava/lang/Runnable;J)Z
        //   1584: pop
        //   1585: aload_0
        //   1586: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1589: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1592: return
        //   1593: aload_0
        //   1594: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1597: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1600: return
        //   1601: aload_1
        //   1602: getfield obj : Ljava/lang/Object;
        //   1605: checkcast java/lang/Boolean
        //   1608: invokevirtual booleanValue : ()Z
        //   1611: istore #4
        //   1613: aload_0
        //   1614: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1617: invokestatic access$64 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1620: iload #4
        //   1622: ifne -> 1654
        //   1625: bipush #10
        //   1627: invokestatic access$28 : (I)V
        //   1630: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   1633: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   1636: dup
        //   1637: aload_0
        //   1638: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1641: invokestatic access$29 : ()I
        //   1644: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   1647: ldc2_w 10
        //   1650: invokevirtual postDelayed : (Ljava/lang/Runnable;J)Z
        //   1653: pop
        //   1654: aload_0
        //   1655: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1658: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1661: return
        //   1662: aload_1
        //   1663: getfield obj : Ljava/lang/Object;
        //   1666: checkcast java/lang/Boolean
        //   1669: invokevirtual booleanValue : ()Z
        //   1672: ifne -> 404
        //   1675: bipush #10
        //   1677: invokestatic access$28 : (I)V
        //   1680: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   1683: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   1686: dup
        //   1687: aload_0
        //   1688: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1691: invokestatic access$29 : ()I
        //   1694: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   1697: ldc2_w 10
        //   1700: invokevirtual postDelayed : (Ljava/lang/Runnable;J)Z
        //   1703: pop
        //   1704: return
        //   1705: aload_1
        //   1706: getfield obj : Ljava/lang/Object;
        //   1709: checkcast java/lang/Boolean
        //   1712: invokevirtual booleanValue : ()Z
        //   1715: ifeq -> 1793
        //   1718: aload_0
        //   1719: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1722: invokestatic access$57 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1725: bipush #30
        //   1727: invokestatic access$28 : (I)V
        //   1730: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   1733: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   1736: dup
        //   1737: aload_0
        //   1738: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1741: invokestatic access$29 : ()I
        //   1744: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   1747: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   1750: pop
        //   1751: aload_0
        //   1752: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1755: invokestatic access$64 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1758: aload_0
        //   1759: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1762: iconst_1
        //   1763: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   1766: aload_0
        //   1767: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1770: invokestatic access$65 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1773: aload_0
        //   1774: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1777: invokestatic access$63 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ProgressBar;
        //   1780: bipush #8
        //   1782: invokevirtual setVisibility : (I)V
        //   1785: aload_0
        //   1786: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1789: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1792: return
        //   1793: bipush #10
        //   1795: invokestatic access$28 : (I)V
        //   1798: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   1801: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   1804: dup
        //   1805: aload_0
        //   1806: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1809: invokestatic access$29 : ()I
        //   1812: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   1815: ldc2_w 10
        //   1818: invokevirtual postDelayed : (Ljava/lang/Runnable;J)Z
        //   1821: pop
        //   1822: goto -> 1758
        //   1825: aload_0
        //   1826: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1829: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1832: ldc_w 'Performance Check Point : AF Action Start'
        //   1835: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1838: aload_0
        //   1839: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1842: invokestatic access$66 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   1845: ifeq -> 404
        //   1848: aload_0
        //   1849: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1852: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1855: return
        //   1856: aload_0
        //   1857: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1860: invokestatic access$66 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   1863: ifeq -> 2259
        //   1866: aload_1
        //   1867: getfield obj : Ljava/lang/Object;
        //   1870: checkcast java/lang/Boolean
        //   1873: invokevirtual booleanValue : ()Z
        //   1876: istore #4
        //   1878: aload_0
        //   1879: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1882: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1885: bipush #9
        //   1887: invokevirtual getAction : (I)Lorg/cybergarage/upnp/Action;
        //   1890: astore_1
        //   1891: aload_0
        //   1892: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1895: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1898: new java/lang/StringBuilder
        //   1901: dup
        //   1902: ldc_w 'result : '
        //   1905: invokespecial <init> : (Ljava/lang/String;)V
        //   1908: iload #4
        //   1910: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   1913: invokevirtual toString : ()Ljava/lang/String;
        //   1916: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   1919: iload #4
        //   1921: ifeq -> 2236
        //   1924: aload_1
        //   1925: invokevirtual getOutputArgumentList : ()Lorg/cybergarage/upnp/ArgumentList;
        //   1928: astore_1
        //   1929: aload_1
        //   1930: invokevirtual size : ()I
        //   1933: istore_3
        //   1934: iconst_0
        //   1935: istore_2
        //   1936: iload_2
        //   1937: iload_3
        //   1938: if_icmplt -> 2001
        //   1941: aload_0
        //   1942: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1945: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1948: invokevirtual getDriveValue : ()Ljava/lang/String;
        //   1951: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   1954: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   1957: ldc_w 'SINGLE'
        //   1960: invokevirtual equals : (Ljava/lang/Object;)Z
        //   1963: ifne -> 1980
        //   1966: invokestatic access$68 : ()I
        //   1969: iconst_1
        //   1970: if_icmpne -> 1980
        //   1973: aload_0
        //   1974: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1977: invokestatic access$70 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1980: aload_0
        //   1981: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1984: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1987: aload_0
        //   1988: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1991: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   1994: ldc_w 'Performance Check Point : AF Action End'
        //   1997: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   2000: return
        //   2001: aload_1
        //   2002: iload_2
        //   2003: invokevirtual getArgument : (I)Lorg/cybergarage/upnp/Argument;
        //   2006: astore #7
        //   2008: aload #7
        //   2010: invokevirtual getName : ()Ljava/lang/String;
        //   2013: astore #6
        //   2015: aload #7
        //   2017: invokevirtual getValue : ()Ljava/lang/String;
        //   2020: astore #7
        //   2022: aload_0
        //   2023: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2026: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   2029: new java/lang/StringBuilder
        //   2032: dup
        //   2033: ldc_w 'name : '
        //   2036: invokespecial <init> : (Ljava/lang/String;)V
        //   2039: aload #6
        //   2041: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   2044: ldc_w ' value : '
        //   2047: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   2050: aload #7
        //   2052: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   2055: invokevirtual toString : ()Ljava/lang/String;
        //   2058: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   2061: aload #6
        //   2063: ldc_w 'AF_MF'
        //   2066: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2069: ifne -> 2146
        //   2072: aload #6
        //   2074: ldc_w 'AFSTATUS'
        //   2077: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2080: ifeq -> 2146
        //   2083: aload #7
        //   2085: ldc_w '0'
        //   2088: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2091: ifeq -> 2153
        //   2094: aload_0
        //   2095: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2098: iconst_1
        //   2099: invokestatic access$67 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   2102: ldc2_w 500
        //   2105: invokestatic sleep : (J)V
        //   2108: invokestatic yield : ()V
        //   2111: invokestatic access$68 : ()I
        //   2114: ifne -> 2137
        //   2117: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   2120: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   2123: dup
        //   2124: aload_0
        //   2125: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2128: bipush #6
        //   2130: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2133: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   2136: pop
        //   2137: aload_0
        //   2138: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2141: bipush #10
        //   2143: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2146: iload_2
        //   2147: iconst_1
        //   2148: iadd
        //   2149: istore_2
        //   2150: goto -> 1936
        //   2153: aload #7
        //   2155: ldc_w '3'
        //   2158: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2161: ifeq -> 2175
        //   2164: aload_0
        //   2165: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2168: iconst_0
        //   2169: invokestatic access$67 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   2172: goto -> 2137
        //   2175: aload_0
        //   2176: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2179: iconst_0
        //   2180: invokestatic access$67 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   2183: aload_0
        //   2184: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2187: aload #7
        //   2189: ldc_w ','
        //   2192: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
        //   2195: invokestatic access$69 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;[Ljava/lang/String;)V
        //   2198: ldc2_w 500
        //   2201: invokestatic sleep : (J)V
        //   2204: invokestatic yield : ()V
        //   2207: invokestatic access$68 : ()I
        //   2210: ifne -> 2137
        //   2213: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   2216: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   2219: dup
        //   2220: aload_0
        //   2221: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2224: bipush #6
        //   2226: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2229: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   2232: pop
        //   2233: goto -> 2137
        //   2236: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   2239: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   2242: dup
        //   2243: aload_0
        //   2244: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2247: bipush #10
        //   2249: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2252: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   2255: pop
        //   2256: goto -> 1941
        //   2259: aload_0
        //   2260: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2263: bipush #13
        //   2265: ldc ''
        //   2267: invokevirtual doAction : (ILjava/lang/String;)V
        //   2270: goto -> 1980
        //   2273: aload_0
        //   2274: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2277: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   2280: ldc_w 'Performance Check Point : AF Action Start'
        //   2283: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   2286: aload_0
        //   2287: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2290: invokestatic access$66 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   2293: ifeq -> 404
        //   2296: aload_0
        //   2297: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2300: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   2303: return
        //   2304: aload_0
        //   2305: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2308: invokestatic access$66 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   2311: ifeq -> 3234
        //   2314: aload_1
        //   2315: getfield obj : Ljava/lang/Object;
        //   2318: checkcast java/lang/Boolean
        //   2321: invokevirtual booleanValue : ()Z
        //   2324: istore #4
        //   2326: aload_0
        //   2327: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2330: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   2333: bipush #10
        //   2335: invokevirtual getAction : (I)Lorg/cybergarage/upnp/Action;
        //   2338: astore_1
        //   2339: aload_0
        //   2340: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2343: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   2346: new java/lang/StringBuilder
        //   2349: dup
        //   2350: ldc_w 'result : '
        //   2353: invokespecial <init> : (Ljava/lang/String;)V
        //   2356: iload #4
        //   2358: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   2361: invokevirtual toString : ()Ljava/lang/String;
        //   2364: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   2367: iload #4
        //   2369: ifeq -> 3045
        //   2372: aload_1
        //   2373: invokevirtual getOutputArgumentList : ()Lorg/cybergarage/upnp/ArgumentList;
        //   2376: astore_1
        //   2377: aload_1
        //   2378: invokevirtual size : ()I
        //   2381: istore_3
        //   2382: iconst_0
        //   2383: istore_2
        //   2384: iload_2
        //   2385: iload_3
        //   2386: if_icmplt -> 2536
        //   2389: aload_0
        //   2390: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2393: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   2396: invokevirtual getDriveValue : ()Ljava/lang/String;
        //   2399: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   2402: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   2405: ldc_w 'SINGLE'
        //   2408: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2411: ifne -> 2515
        //   2414: aload_0
        //   2415: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2418: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   2421: invokevirtual getDriveValue : ()Ljava/lang/String;
        //   2424: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   2427: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   2430: ldc_w 'CONTINUOUS'
        //   2433: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2436: ifeq -> 3118
        //   2439: invokestatic access$68 : ()I
        //   2442: ifne -> 3068
        //   2445: aload_0
        //   2446: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2449: invokestatic access$72 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   2452: ifeq -> 2488
        //   2455: aload_0
        //   2456: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2459: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   2462: invokevirtual getVersionPrefix : ()Ljava/lang/String;
        //   2465: aload_0
        //   2466: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2469: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   2472: invokevirtual getVersion : ()Ljava/lang/String;
        //   2475: aload_0
        //   2476: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2479: invokestatic access$6 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Ljava/lang/String;
        //   2482: invokestatic isCancellableShotByAFFail : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z
        //   2485: ifne -> 3068
        //   2488: aload_0
        //   2489: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2492: iconst_5
        //   2493: invokestatic access$41 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2496: aload_0
        //   2497: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2500: iconst_3
        //   2501: invokestatic access$73 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2504: aload_0
        //   2505: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2508: bipush #30
        //   2510: ldc ''
        //   2512: invokevirtual doAction : (ILjava/lang/String;)V
        //   2515: aload_0
        //   2516: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2519: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   2522: aload_0
        //   2523: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2526: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   2529: ldc_w 'Performance Check Point : AF Action End'
        //   2532: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   2535: return
        //   2536: aload_1
        //   2537: iload_2
        //   2538: invokevirtual getArgument : (I)Lorg/cybergarage/upnp/Argument;
        //   2541: astore #7
        //   2543: aload #7
        //   2545: invokevirtual getName : ()Ljava/lang/String;
        //   2548: astore #6
        //   2550: aload #7
        //   2552: invokevirtual getValue : ()Ljava/lang/String;
        //   2555: astore #7
        //   2557: invokestatic access$68 : ()I
        //   2560: iconst_m1
        //   2561: if_icmpeq -> 2389
        //   2564: aload_0
        //   2565: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2568: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   2571: new java/lang/StringBuilder
        //   2574: dup
        //   2575: ldc_w 'name : '
        //   2578: invokespecial <init> : (Ljava/lang/String;)V
        //   2581: aload #6
        //   2583: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   2586: ldc_w ' value : '
        //   2589: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   2592: aload #7
        //   2594: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   2597: invokevirtual toString : ()Ljava/lang/String;
        //   2600: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   2603: aload #6
        //   2605: ldc_w 'AF_MF'
        //   2608: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2611: ifeq -> 2638
        //   2614: aload_0
        //   2615: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2618: iconst_0
        //   2619: invokestatic access$71 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2622: aload_0
        //   2623: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2626: bipush #10
        //   2628: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2631: iload_2
        //   2632: iconst_1
        //   2633: iadd
        //   2634: istore_2
        //   2635: goto -> 2384
        //   2638: aload #7
        //   2640: ldc_w '3'
        //   2643: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2646: ifeq -> 2673
        //   2649: aload_0
        //   2650: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2653: iconst_0
        //   2654: invokestatic access$71 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2657: aload_0
        //   2658: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2661: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   2664: ldc_w 'Manual Focus'
        //   2667: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   2670: goto -> 2622
        //   2673: aload #6
        //   2675: ldc_w 'AFSTATUS'
        //   2678: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2681: ifeq -> 2765
        //   2684: aload #7
        //   2686: ldc_w 'AFEXTEND_OK'
        //   2689: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2692: ifeq -> 2765
        //   2695: aload_0
        //   2696: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2699: iconst_4
        //   2700: invokestatic access$71 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2703: aload_0
        //   2704: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2707: iconst_0
        //   2708: invokestatic access$67 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   2711: aload_0
        //   2712: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2715: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   2718: invokevirtual getDriveValue : ()Ljava/lang/String;
        //   2721: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   2724: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   2727: ldc_w 'SINGLE'
        //   2730: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2733: ifne -> 2742
        //   2736: invokestatic access$68 : ()I
        //   2739: ifne -> 2622
        //   2742: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   2745: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   2748: dup
        //   2749: aload_0
        //   2750: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2753: bipush #6
        //   2755: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2758: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   2761: pop
        //   2762: goto -> 2622
        //   2765: aload #6
        //   2767: ldc_w 'AFSTATUS'
        //   2770: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2773: ifeq -> 2857
        //   2776: aload #7
        //   2778: ldc_w 'AFEXTEND_FAIL'
        //   2781: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2784: ifeq -> 2857
        //   2787: aload_0
        //   2788: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2791: iconst_4
        //   2792: invokestatic access$71 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2795: aload_0
        //   2796: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2799: iconst_1
        //   2800: invokestatic access$67 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   2803: aload_0
        //   2804: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2807: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   2810: invokevirtual getDriveValue : ()Ljava/lang/String;
        //   2813: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   2816: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   2819: ldc_w 'SINGLE'
        //   2822: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2825: ifne -> 2834
        //   2828: invokestatic access$68 : ()I
        //   2831: ifne -> 2622
        //   2834: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   2837: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   2840: dup
        //   2841: aload_0
        //   2842: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2845: bipush #6
        //   2847: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2850: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   2853: pop
        //   2854: goto -> 2622
        //   2857: aload #6
        //   2859: ldc_w 'AFSTATUS'
        //   2862: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2865: ifeq -> 2975
        //   2868: aload #7
        //   2870: ldc_w 'AFFAIL'
        //   2873: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2876: ifne -> 2975
        //   2879: aload #7
        //   2881: ldc_w '0'
        //   2884: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2887: ifne -> 2975
        //   2890: aload_0
        //   2891: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2894: iconst_1
        //   2895: invokestatic access$71 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2898: aload_0
        //   2899: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2902: iconst_0
        //   2903: invokestatic access$67 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   2906: aload_0
        //   2907: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2910: aload #7
        //   2912: ldc_w ','
        //   2915: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
        //   2918: invokestatic access$69 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;[Ljava/lang/String;)V
        //   2921: aload_0
        //   2922: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2925: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   2928: invokevirtual getDriveValue : ()Ljava/lang/String;
        //   2931: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   2934: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   2937: ldc_w 'SINGLE'
        //   2940: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2943: ifne -> 2952
        //   2946: invokestatic access$68 : ()I
        //   2949: ifne -> 2622
        //   2952: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   2955: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   2958: dup
        //   2959: aload_0
        //   2960: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2963: bipush #6
        //   2965: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2968: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   2971: pop
        //   2972: goto -> 2622
        //   2975: aload_0
        //   2976: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2979: iconst_1
        //   2980: invokestatic access$71 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2983: aload_0
        //   2984: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2987: iconst_1
        //   2988: invokestatic access$67 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   2991: aload_0
        //   2992: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2995: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   2998: invokevirtual getDriveValue : ()Ljava/lang/String;
        //   3001: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   3004: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   3007: ldc_w 'SINGLE'
        //   3010: invokevirtual equals : (Ljava/lang/Object;)Z
        //   3013: ifne -> 3022
        //   3016: invokestatic access$68 : ()I
        //   3019: ifne -> 2622
        //   3022: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   3025: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   3028: dup
        //   3029: aload_0
        //   3030: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3033: bipush #6
        //   3035: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   3038: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   3041: pop
        //   3042: goto -> 2622
        //   3045: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   3048: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   3051: dup
        //   3052: aload_0
        //   3053: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3056: bipush #10
        //   3058: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   3061: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   3064: pop
        //   3065: goto -> 2389
        //   3068: aload_0
        //   3069: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3072: iconst_0
        //   3073: invokestatic access$41 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   3076: aload_0
        //   3077: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3080: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   3083: ldc_w 'do action RELEASE_SELF_TIMER 08'
        //   3086: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   3089: aload_0
        //   3090: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3093: iconst_0
        //   3094: invokestatic access$73 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   3097: aload_0
        //   3098: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3101: invokestatic access$74 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   3104: aload_0
        //   3105: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3108: bipush #13
        //   3110: ldc ''
        //   3112: invokevirtual doAction : (ILjava/lang/String;)V
        //   3115: goto -> 2515
        //   3118: invokestatic access$68 : ()I
        //   3121: ifeq -> 2515
        //   3124: invokestatic access$68 : ()I
        //   3127: iconst_1
        //   3128: if_icmpne -> 3184
        //   3131: aload_0
        //   3132: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3135: invokestatic access$72 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   3138: ifeq -> 3174
        //   3141: aload_0
        //   3142: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3145: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   3148: invokevirtual getVersionPrefix : ()Ljava/lang/String;
        //   3151: aload_0
        //   3152: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3155: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   3158: invokevirtual getVersion : ()Ljava/lang/String;
        //   3161: aload_0
        //   3162: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3165: invokestatic access$6 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Ljava/lang/String;
        //   3168: invokestatic isCancellableShotByAFFail : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z
        //   3171: ifne -> 3184
        //   3174: aload_0
        //   3175: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3178: invokestatic access$70 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   3181: goto -> 2515
        //   3184: aload_0
        //   3185: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3188: iconst_0
        //   3189: invokestatic access$41 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   3192: aload_0
        //   3193: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3196: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   3199: ldc_w 'do action RELEASE_SELF_TIMER 11'
        //   3202: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   3205: aload_0
        //   3206: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3209: iconst_0
        //   3210: invokestatic access$73 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   3213: aload_0
        //   3214: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3217: invokestatic access$74 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   3220: aload_0
        //   3221: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3224: bipush #13
        //   3226: ldc ''
        //   3228: invokevirtual doAction : (ILjava/lang/String;)V
        //   3231: goto -> 2515
        //   3234: aload_0
        //   3235: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3238: bipush #13
        //   3240: ldc ''
        //   3242: invokevirtual doAction : (ILjava/lang/String;)V
        //   3245: goto -> 2515
        //   3248: aload_0
        //   3249: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3252: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   3255: return
        //   3256: aload_1
        //   3257: getfield obj : Ljava/lang/Object;
        //   3260: checkcast java/lang/Boolean
        //   3263: invokevirtual booleanValue : ()Z
        //   3266: istore #4
        //   3268: aload_0
        //   3269: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3272: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   3275: new java/lang/StringBuilder
        //   3278: dup
        //   3279: ldc_w 'result : '
        //   3282: invokespecial <init> : (Ljava/lang/String;)V
        //   3285: iload #4
        //   3287: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   3290: invokevirtual toString : ()Ljava/lang/String;
        //   3293: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   3296: iload #4
        //   3298: ifeq -> 3334
        //   3301: aload_0
        //   3302: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3305: bipush #9
        //   3307: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   3310: aload_0
        //   3311: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3314: aload_0
        //   3315: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3318: invokestatic access$34 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   3321: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   3324: aload_0
        //   3325: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3328: ldc_w 2131558685
        //   3331: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   3334: aload_0
        //   3335: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3338: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   3341: return
        //   3342: aload_0
        //   3343: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3346: invokestatic access$66 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   3349: ifeq -> 404
        //   3352: aload_0
        //   3353: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3356: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   3359: return
        //   3360: aload_0
        //   3361: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3364: invokestatic access$66 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   3367: ifeq -> 3694
        //   3370: aload_1
        //   3371: getfield obj : Ljava/lang/Object;
        //   3374: checkcast java/lang/Boolean
        //   3377: invokevirtual booleanValue : ()Z
        //   3380: istore #4
        //   3382: aload_0
        //   3383: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3386: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   3389: bipush #12
        //   3391: invokevirtual getAction : (I)Lorg/cybergarage/upnp/Action;
        //   3394: astore_1
        //   3395: aload_0
        //   3396: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3399: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   3402: new java/lang/StringBuilder
        //   3405: dup
        //   3406: ldc_w 'result : '
        //   3409: invokespecial <init> : (Ljava/lang/String;)V
        //   3412: iload #4
        //   3414: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   3417: invokevirtual toString : ()Ljava/lang/String;
        //   3420: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   3423: iload #4
        //   3425: ifeq -> 3461
        //   3428: aload_1
        //   3429: ldc_w 'TOUCHAF_RESULT'
        //   3432: invokevirtual getArgumentValue : (Ljava/lang/String;)Ljava/lang/String;
        //   3435: ldc_w 'AFOK'
        //   3438: invokevirtual equals : (Ljava/lang/Object;)Z
        //   3441: ifeq -> 3640
        //   3444: aload_0
        //   3445: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3448: iconst_0
        //   3449: invokestatic access$67 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   3452: invokestatic access$36 : ()Landroid/widget/ImageView;
        //   3455: ldc_w 2130837573
        //   3458: invokevirtual setImageResource : (I)V
        //   3461: invokestatic access$68 : ()I
        //   3464: iconst_1
        //   3465: if_icmpne -> 3538
        //   3468: aload_0
        //   3469: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3472: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   3475: invokevirtual getDriveValue : ()Ljava/lang/String;
        //   3478: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   3481: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   3484: ldc_w 'SINGLE'
        //   3487: invokevirtual equals : (Ljava/lang/Object;)Z
        //   3490: ifne -> 3538
        //   3493: aload_0
        //   3494: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3497: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   3500: invokevirtual getDriveValue : ()Ljava/lang/String;
        //   3503: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   3506: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   3509: ldc_w 'CONTINUOUS'
        //   3512: invokevirtual equals : (Ljava/lang/Object;)Z
        //   3515: ifne -> 3538
        //   3518: invokestatic access$75 : ()Z
        //   3521: ifeq -> 3538
        //   3524: invokestatic access$36 : ()Landroid/widget/ImageView;
        //   3527: iconst_4
        //   3528: invokevirtual setVisibility : (I)V
        //   3531: aload_0
        //   3532: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3535: invokestatic access$70 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   3538: aload_0
        //   3539: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3542: iconst_1
        //   3543: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   3546: aload_0
        //   3547: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3550: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   3553: aload_0
        //   3554: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3557: invokestatic access$72 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   3560: ifne -> 404
        //   3563: aload_0
        //   3564: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3567: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   3570: invokevirtual getDriveValue : ()Ljava/lang/String;
        //   3573: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   3576: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   3579: ldc_w 'CONTINUOUS'
        //   3582: invokevirtual equals : (Ljava/lang/Object;)Z
        //   3585: ifeq -> 404
        //   3588: invokestatic access$68 : ()I
        //   3591: ifne -> 3660
        //   3594: aload_0
        //   3595: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3598: invokestatic access$52 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/os/Handler;
        //   3601: iconst_4
        //   3602: invokevirtual removeMessages : (I)V
        //   3605: aload_0
        //   3606: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3609: iconst_5
        //   3610: invokestatic access$41 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   3613: aload_0
        //   3614: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3617: iconst_3
        //   3618: invokestatic access$73 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   3621: invokestatic access$36 : ()Landroid/widget/ImageView;
        //   3624: iconst_4
        //   3625: invokevirtual setVisibility : (I)V
        //   3628: aload_0
        //   3629: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3632: bipush #30
        //   3634: ldc ''
        //   3636: invokevirtual doAction : (ILjava/lang/String;)V
        //   3639: return
        //   3640: aload_0
        //   3641: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3644: iconst_1
        //   3645: invokestatic access$67 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   3648: invokestatic access$36 : ()Landroid/widget/ImageView;
        //   3651: ldc_w 2130837574
        //   3654: invokevirtual setImageResource : (I)V
        //   3657: goto -> 3461
        //   3660: invokestatic access$68 : ()I
        //   3663: iconst_1
        //   3664: if_icmpne -> 404
        //   3667: aload_0
        //   3668: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3671: iconst_0
        //   3672: invokestatic access$73 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   3675: aload_0
        //   3676: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3679: invokestatic access$74 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   3682: aload_0
        //   3683: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3686: bipush #13
        //   3688: ldc ''
        //   3690: invokevirtual doAction : (ILjava/lang/String;)V
        //   3693: return
        //   3694: aload_0
        //   3695: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3698: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   3701: aload_0
        //   3702: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3705: bipush #13
        //   3707: ldc ''
        //   3709: invokevirtual doAction : (ILjava/lang/String;)V
        //   3712: return
        //   3713: aload_0
        //   3714: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3717: invokestatic access$66 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   3720: ifeq -> 404
        //   3723: aload_0
        //   3724: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3727: iconst_0
        //   3728: invokestatic access$76 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   3731: aload_0
        //   3732: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3735: invokestatic access$77 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/RelativeLayout;
        //   3738: ifnull -> 404
        //   3741: aload_0
        //   3742: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3745: invokestatic access$77 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/RelativeLayout;
        //   3748: bipush #8
        //   3750: invokevirtual setVisibility : (I)V
        //   3753: iconst_0
        //   3754: putstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.bshutter : Z
        //   3757: return
        //   3758: aload_0
        //   3759: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3762: invokestatic access$78 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   3765: ifeq -> 3857
        //   3768: aload_0
        //   3769: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3772: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   3775: invokevirtual getCardStatusValue : ()Ljava/lang/String;
        //   3778: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   3781: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   3784: ldc_w 'EXTERNAL'
        //   3787: invokevirtual equals : (Ljava/lang/Object;)Z
        //   3790: ifeq -> 404
        //   3793: aload_0
        //   3794: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3797: invokestatic access$52 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/os/Handler;
        //   3800: iconst_5
        //   3801: invokevirtual removeMessages : (I)V
        //   3804: aload_0
        //   3805: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3808: invokestatic access$79 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   3811: invokestatic destruct : ()I
        //   3814: pop
        //   3815: new android/content/Intent
        //   3818: dup
        //   3819: aload_0
        //   3820: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3823: ldc_w com/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing
        //   3826: invokespecial <init> : (Landroid/content/Context;Ljava/lang/Class;)V
        //   3829: astore_1
        //   3830: aload_1
        //   3831: ldc_w 'requestServiceChange'
        //   3834: ldc_w 'changeToML'
        //   3837: invokevirtual putExtra : (Ljava/lang/String;Ljava/lang/String;)Landroid/content/Intent;
        //   3840: pop
        //   3841: aload_0
        //   3842: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3845: aload_1
        //   3846: invokevirtual startActivity : (Landroid/content/Intent;)V
        //   3849: aload_0
        //   3850: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3853: invokevirtual finish : ()V
        //   3856: return
        //   3857: aload_0
        //   3858: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3861: invokestatic access$66 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   3864: ifeq -> 3886
        //   3867: iconst_0
        //   3868: invokestatic access$43 : (Z)V
        //   3871: aload_0
        //   3872: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3875: invokestatic access$74 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   3878: aload_0
        //   3879: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3882: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   3885: return
        //   3886: bipush #16
        //   3888: invokestatic access$28 : (I)V
        //   3891: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   3894: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   3897: dup
        //   3898: aload_0
        //   3899: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3902: invokestatic access$29 : ()I
        //   3905: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   3908: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   3911: pop
        //   3912: goto -> 3878
        //   3915: aload_0
        //   3916: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3919: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   3922: ldc_w 'Performance Check Point : Shot Action Start'
        //   3925: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   3928: aload_0
        //   3929: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3932: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   3935: invokevirtual getShutterSpeedValue : ()Ljava/lang/String;
        //   3938: ldc_w '"'
        //   3941: ldc ''
        //   3943: invokevirtual replaceAll : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   3946: astore_1
        //   3947: aload_1
        //   3948: invokestatic isNumeric : (Ljava/lang/String;)Z
        //   3951: ifeq -> 3982
        //   3954: aload_1
        //   3955: invokestatic parseInt : (Ljava/lang/String;)I
        //   3958: iconst_5
        //   3959: if_icmplt -> 3982
        //   3962: aload_0
        //   3963: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3966: invokestatic access$52 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/os/Handler;
        //   3969: iconst_5
        //   3970: invokevirtual removeMessages : (I)V
        //   3973: aload_0
        //   3974: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3977: bipush #6
        //   3979: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   3982: aload_0
        //   3983: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   3986: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   3989: return
        //   3990: aload_1
        //   3991: getfield obj : Ljava/lang/Object;
        //   3994: checkcast java/lang/Boolean
        //   3997: invokevirtual booleanValue : ()Z
        //   4000: istore #4
        //   4002: aload_0
        //   4003: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4006: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   4009: new java/lang/StringBuilder
        //   4012: dup
        //   4013: ldc_w 'result : '
        //   4016: invokespecial <init> : (Ljava/lang/String;)V
        //   4019: iload #4
        //   4021: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   4024: invokevirtual toString : ()Ljava/lang/String;
        //   4027: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   4030: iload #4
        //   4032: ifeq -> 4544
        //   4035: aload_0
        //   4036: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4039: invokestatic access$80 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)I
        //   4042: iconst_2
        //   4043: if_icmpne -> 4225
        //   4046: aload_0
        //   4047: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4050: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   4053: invokevirtual getCurrentLEDTimeMenuItem : ()Ljava/lang/String;
        //   4056: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   4059: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   4062: ldc_w 'DOUBLE'
        //   4065: invokevirtual equals : (Ljava/lang/Object;)Z
        //   4068: ifeq -> 4225
        //   4071: iconst_0
        //   4072: invokestatic access$43 : (Z)V
        //   4075: aload_0
        //   4076: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4079: iconst_1
        //   4080: invokestatic access$81 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   4083: aload_0
        //   4084: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4087: invokestatic access$15 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)I
        //   4090: ifne -> 4113
        //   4093: aload_0
        //   4094: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4097: invokestatic access$46 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
        //   4100: aload_0
        //   4101: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4104: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   4107: invokevirtual getAFShotResult : ()Ljava/lang/String;
        //   4110: invokevirtual downloadImage : (Ljava/lang/String;)V
        //   4113: aload_0
        //   4114: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4117: invokestatic access$82 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/app/ProgressDialog;
        //   4120: ifnull -> 4156
        //   4123: aload_0
        //   4124: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4127: invokestatic access$82 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/app/ProgressDialog;
        //   4130: invokevirtual isShowing : ()Z
        //   4133: ifeq -> 4156
        //   4136: aload_0
        //   4137: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4140: invokestatic access$82 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/app/ProgressDialog;
        //   4143: invokevirtual dismiss : ()V
        //   4146: aload_0
        //   4147: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4150: sipush #1010
        //   4153: invokevirtual removeDialog : (I)V
        //   4156: aload_0
        //   4157: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4160: iconst_1
        //   4161: invokestatic access$83 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   4164: aload_0
        //   4165: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4168: iconst_2
        //   4169: invokestatic access$84 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   4172: aload_0
        //   4173: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4176: invokestatic access$52 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/os/Handler;
        //   4179: bipush #6
        //   4181: invokevirtual removeMessages : (I)V
        //   4184: aload_0
        //   4185: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4188: invokestatic access$40 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   4191: ifeq -> 4644
        //   4194: aload_0
        //   4195: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4198: sipush #1009
        //   4201: invokevirtual showDialog : (I)V
        //   4204: aload_0
        //   4205: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4208: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   4211: aload_0
        //   4212: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4215: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   4218: ldc_w 'Performance Check Point : Shot Action End'
        //   4221: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   4224: return
        //   4225: iconst_0
        //   4226: invokestatic access$43 : (Z)V
        //   4229: aload_0
        //   4230: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4233: invokestatic access$31 : ()Landroid/widget/ImageButton;
        //   4236: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   4239: invokestatic access$85 : ()Z
        //   4242: ifne -> 404
        //   4245: aload_0
        //   4246: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4249: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   4252: invokevirtual getAFShotResult : ()Ljava/lang/String;
        //   4255: ldc ''
        //   4257: invokevirtual equals : (Ljava/lang/Object;)Z
        //   4260: ifne -> 4439
        //   4263: iconst_1
        //   4264: istore #5
        //   4266: iload #5
        //   4268: istore #4
        //   4270: aload_0
        //   4271: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4274: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   4277: invokevirtual getDriveValue : ()Ljava/lang/String;
        //   4280: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   4283: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   4286: ldc_w 'CONTINUOUS'
        //   4289: invokevirtual equals : (Ljava/lang/Object;)Z
        //   4292: ifeq -> 4320
        //   4295: aload_0
        //   4296: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4299: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   4302: invokevirtual getFileSaveMenuItemsDim : ()Ljava/util/ArrayList;
        //   4305: astore_1
        //   4306: iconst_0
        //   4307: istore_2
        //   4308: iload_2
        //   4309: aload_1
        //   4310: invokevirtual size : ()I
        //   4313: if_icmplt -> 4450
        //   4316: iload #5
        //   4318: istore #4
        //   4320: aload_0
        //   4321: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4324: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   4327: new java/lang/StringBuilder
        //   4330: dup
        //   4331: ldc_w 'bSupportedSave : '
        //   4334: invokespecial <init> : (Ljava/lang/String;)V
        //   4337: iload #4
        //   4339: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   4342: ldc_w ' nCameraSaveSelect : '
        //   4345: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   4348: aload_0
        //   4349: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4352: invokestatic access$15 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)I
        //   4355: invokevirtual append : (I)Ljava/lang/StringBuilder;
        //   4358: invokevirtual toString : ()Ljava/lang/String;
        //   4361: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   4364: iload #4
        //   4366: ifeq -> 4513
        //   4369: aload_0
        //   4370: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4373: invokestatic access$15 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)I
        //   4376: ifne -> 4513
        //   4379: aload_0
        //   4380: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4383: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   4386: invokevirtual getQualityValue : ()Ljava/lang/String;
        //   4389: ldc_w 'RAW'
        //   4392: invokevirtual equals : (Ljava/lang/Object;)Z
        //   4395: ifne -> 4513
        //   4398: aload_0
        //   4399: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4402: iconst_4
        //   4403: invokestatic access$73 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   4406: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$ImageDownloadTask
        //   4409: dup
        //   4410: aload_0
        //   4411: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4414: aconst_null
        //   4415: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$ImageDownloadTask;)V
        //   4418: iconst_1
        //   4419: anewarray java/lang/String
        //   4422: dup
        //   4423: iconst_0
        //   4424: aload_0
        //   4425: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4428: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   4431: invokevirtual getAFShotResult : ()Ljava/lang/String;
        //   4434: aastore
        //   4435: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
        //   4438: pop
        //   4439: aload_0
        //   4440: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4443: iconst_0
        //   4444: invokestatic access$44 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   4447: goto -> 4172
        //   4450: aload_1
        //   4451: iload_2
        //   4452: invokevirtual get : (I)Ljava/lang/Object;
        //   4455: checkcast com/samsungimaging/connectionmanager/app/pullservice/datatype/DSCMenuItem
        //   4458: astore #6
        //   4460: aload #6
        //   4462: invokevirtual getName : ()Ljava/lang/String;
        //   4465: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   4468: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   4471: ldc_w 'DRIVE'
        //   4474: invokevirtual equals : (Ljava/lang/Object;)Z
        //   4477: ifeq -> 4506
        //   4480: aload #6
        //   4482: invokevirtual getValue : ()Ljava/lang/String;
        //   4485: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   4488: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   4491: ldc_w 'CONTINUOUS'
        //   4494: invokevirtual equals : (Ljava/lang/Object;)Z
        //   4497: ifeq -> 4506
        //   4500: iconst_0
        //   4501: istore #4
        //   4503: goto -> 4320
        //   4506: iload_2
        //   4507: iconst_1
        //   4508: iadd
        //   4509: istore_2
        //   4510: goto -> 4308
        //   4513: aload_0
        //   4514: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4517: iconst_0
        //   4518: invokestatic access$73 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   4521: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$ShotPrepareTask
        //   4524: dup
        //   4525: aload_0
        //   4526: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4529: aconst_null
        //   4530: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$ShotPrepareTask;)V
        //   4533: iconst_0
        //   4534: anewarray java/lang/String
        //   4537: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
        //   4540: pop
        //   4541: goto -> 4439
        //   4544: aload_0
        //   4545: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4548: iconst_0
        //   4549: invokestatic access$73 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   4552: iconst_0
        //   4553: invokestatic access$43 : (Z)V
        //   4556: invokestatic access$85 : ()Z
        //   4559: ifne -> 404
        //   4562: aload_0
        //   4563: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4566: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   4569: bipush #14
        //   4571: invokevirtual getAction : (I)Lorg/cybergarage/upnp/Action;
        //   4574: invokevirtual getControlResponse : ()Lorg/cybergarage/upnp/control/ControlResponse;
        //   4577: invokevirtual getUPnPErrorCode : ()I
        //   4580: sipush #803
        //   4583: if_icmpne -> 4615
        //   4586: bipush #125
        //   4588: invokestatic access$28 : (I)V
        //   4591: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   4594: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   4597: dup
        //   4598: aload_0
        //   4599: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4602: invokestatic access$29 : ()I
        //   4605: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   4608: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   4611: pop
        //   4612: goto -> 4172
        //   4615: bipush #10
        //   4617: invokestatic access$28 : (I)V
        //   4620: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   4623: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   4626: dup
        //   4627: aload_0
        //   4628: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4631: invokestatic access$29 : ()I
        //   4634: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   4637: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   4640: pop
        //   4641: goto -> 4172
        //   4644: aload_0
        //   4645: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4648: invokestatic access$39 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   4651: ifne -> 4204
        //   4654: aload_0
        //   4655: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4658: iconst_1
        //   4659: invokestatic access$45 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   4662: goto -> 4204
        //   4665: aload_0
        //   4666: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4669: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   4672: ldc_w 'Performance Check Point : Set Led Action Start'
        //   4675: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   4678: aload_0
        //   4679: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4682: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   4685: return
        //   4686: aload_1
        //   4687: getfield obj : Ljava/lang/Object;
        //   4690: checkcast java/lang/Boolean
        //   4693: invokevirtual booleanValue : ()Z
        //   4696: ifeq -> 4833
        //   4699: aload_0
        //   4700: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4703: ldc_w 2131558679
        //   4706: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   4709: aload_0
        //   4710: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4713: invokestatic access$11 : ()Landroid/widget/ImageButton;
        //   4716: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   4719: aload_0
        //   4720: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4723: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   4726: invokevirtual getVersionPrefix : ()Ljava/lang/String;
        //   4729: aload_0
        //   4730: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4733: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   4736: invokevirtual getVersion : ()Ljava/lang/String;
        //   4739: invokestatic isSupportedSmartPanel : (Ljava/lang/String;Ljava/lang/String;)Z
        //   4742: ifeq -> 4768
        //   4745: aload_0
        //   4746: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4749: aload_0
        //   4750: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4753: invokestatic access$86 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   4756: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   4759: aload_0
        //   4760: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4763: bipush #9
        //   4765: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   4768: aload_0
        //   4769: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4772: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   4775: invokevirtual getVersionPrefix : ()Ljava/lang/String;
        //   4778: aload_0
        //   4779: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4782: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   4785: invokevirtual getVersion : ()Ljava/lang/String;
        //   4788: invokestatic isSupportedSmartPanel : (Ljava/lang/String;Ljava/lang/String;)Z
        //   4791: ifne -> 4812
        //   4794: aload_0
        //   4795: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4798: iconst_2
        //   4799: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   4802: aload_0
        //   4803: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4806: invokestatic access$21 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$MainOptionMenuListAdapter;
        //   4809: invokevirtual notifyDataSetChanged : ()V
        //   4812: aload_0
        //   4813: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4816: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   4819: aload_0
        //   4820: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4823: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   4826: ldc_w 'Performance Check Point : Set Led Action End'
        //   4829: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   4832: return
        //   4833: aload_0
        //   4834: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4837: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   4840: ldc 'MESSAGE : DISPLAY_NETERRORMSG'
        //   4842: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   4845: bipush #10
        //   4847: invokestatic access$28 : (I)V
        //   4850: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   4853: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   4856: dup
        //   4857: aload_0
        //   4858: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4861: invokestatic access$29 : ()I
        //   4864: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   4867: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   4870: pop
        //   4871: goto -> 4768
        //   4874: aload_0
        //   4875: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4878: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   4881: ldc_w 'Performance Check Point : Flash set Action Start'
        //   4884: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   4887: aload_0
        //   4888: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4891: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   4894: return
        //   4895: aload_1
        //   4896: getfield obj : Ljava/lang/Object;
        //   4899: checkcast java/lang/Boolean
        //   4902: invokevirtual booleanValue : ()Z
        //   4905: istore #4
        //   4907: aload_0
        //   4908: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4911: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   4914: new java/lang/StringBuilder
        //   4917: dup
        //   4918: ldc_w 'result : '
        //   4921: invokespecial <init> : (Ljava/lang/String;)V
        //   4924: iload #4
        //   4926: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   4929: invokevirtual toString : ()Ljava/lang/String;
        //   4932: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   4935: aload_0
        //   4936: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4939: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   4942: bipush #17
        //   4944: invokevirtual getAction : (I)Lorg/cybergarage/upnp/Action;
        //   4947: astore_1
        //   4948: aload_0
        //   4949: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4952: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   4955: new java/lang/StringBuilder
        //   4958: dup
        //   4959: ldc_w 'action.getOutputArgumentList().size() : '
        //   4962: invokespecial <init> : (Ljava/lang/String;)V
        //   4965: aload_1
        //   4966: invokevirtual getOutputArgumentList : ()Lorg/cybergarage/upnp/ArgumentList;
        //   4969: invokevirtual size : ()I
        //   4972: invokevirtual append : (I)Ljava/lang/StringBuilder;
        //   4975: invokevirtual toString : ()Ljava/lang/String;
        //   4978: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   4981: iload #4
        //   4983: ifeq -> 5120
        //   4986: aload_0
        //   4987: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4990: invokestatic access$31 : ()Landroid/widget/ImageButton;
        //   4993: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   4996: aload_0
        //   4997: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5000: aload_0
        //   5001: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5004: invokestatic access$30 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   5007: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   5010: aload_0
        //   5011: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5014: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   5017: invokevirtual getVersionPrefix : ()Ljava/lang/String;
        //   5020: aload_0
        //   5021: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5024: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   5027: invokevirtual getVersion : ()Ljava/lang/String;
        //   5030: invokestatic isSupportedSmartPanel : (Ljava/lang/String;Ljava/lang/String;)Z
        //   5033: ifeq -> 5045
        //   5036: aload_0
        //   5037: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5040: bipush #9
        //   5042: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   5045: aload_0
        //   5046: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5049: ldc_w 2131558680
        //   5052: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   5055: aload_0
        //   5056: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5059: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   5062: invokevirtual getVersionPrefix : ()Ljava/lang/String;
        //   5065: aload_0
        //   5066: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5069: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   5072: invokevirtual getVersion : ()Ljava/lang/String;
        //   5075: invokestatic isSupportedSmartPanel : (Ljava/lang/String;Ljava/lang/String;)Z
        //   5078: ifne -> 5099
        //   5081: aload_0
        //   5082: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5085: iconst_2
        //   5086: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   5089: aload_0
        //   5090: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5093: invokestatic access$21 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$MainOptionMenuListAdapter;
        //   5096: invokevirtual notifyDataSetChanged : ()V
        //   5099: aload_0
        //   5100: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5103: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   5106: aload_0
        //   5107: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5110: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   5113: ldc_w 'Performance Check Point : Flash set Action End'
        //   5116: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   5119: return
        //   5120: aload_1
        //   5121: invokevirtual getStatus : ()Lorg/cybergarage/upnp/UPnPStatus;
        //   5124: astore #6
        //   5126: aload_1
        //   5127: invokevirtual getControlStatus : ()Lorg/cybergarage/upnp/UPnPStatus;
        //   5130: invokevirtual getDescription : ()Ljava/lang/String;
        //   5133: astore #7
        //   5135: aload_1
        //   5136: invokevirtual getControlStatus : ()Lorg/cybergarage/upnp/UPnPStatus;
        //   5139: invokevirtual getCode : ()I
        //   5142: istore_2
        //   5143: aload_0
        //   5144: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5147: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   5150: new java/lang/StringBuilder
        //   5153: dup
        //   5154: ldc_w 'err.getCode() : '
        //   5157: invokespecial <init> : (Ljava/lang/String;)V
        //   5160: aload #6
        //   5162: invokevirtual getCode : ()I
        //   5165: invokevirtual append : (I)Ljava/lang/StringBuilder;
        //   5168: ldc_w '\\t'
        //   5171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   5174: ldc_w ' err.getDescription() : '
        //   5177: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   5180: aload #6
        //   5182: invokevirtual getDescription : ()Ljava/lang/String;
        //   5185: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   5188: invokevirtual toString : ()Ljava/lang/String;
        //   5191: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   5194: aload_0
        //   5195: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5198: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   5201: new java/lang/StringBuilder
        //   5204: dup
        //   5205: ldc_w 'err2 : '
        //   5208: invokespecial <init> : (Ljava/lang/String;)V
        //   5211: aload #7
        //   5213: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   5216: invokevirtual toString : ()Ljava/lang/String;
        //   5219: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   5222: aload_0
        //   5223: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5226: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   5229: new java/lang/StringBuilder
        //   5232: dup
        //   5233: ldc_w 'err3 : '
        //   5236: invokespecial <init> : (Ljava/lang/String;)V
        //   5239: iload_2
        //   5240: invokevirtual append : (I)Ljava/lang/StringBuilder;
        //   5243: invokevirtual toString : ()Ljava/lang/String;
        //   5246: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   5249: iload_2
        //   5250: sipush #800
        //   5253: if_icmpne -> 5301
        //   5256: aload_1
        //   5257: ldc_w 'FLASHMODE'
        //   5260: invokevirtual getArgumentValue : (Ljava/lang/String;)Ljava/lang/String;
        //   5263: ldc_w 'OFF'
        //   5266: invokevirtual equals : (Ljava/lang/Object;)Z
        //   5269: ifne -> 5055
        //   5272: bipush #121
        //   5274: invokestatic access$28 : (I)V
        //   5277: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   5280: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   5283: dup
        //   5284: aload_0
        //   5285: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5288: invokestatic access$29 : ()I
        //   5291: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   5294: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   5297: pop
        //   5298: goto -> 5055
        //   5301: iload_2
        //   5302: sipush #802
        //   5305: if_icmpne -> 5406
        //   5308: bipush #121
        //   5310: invokestatic access$28 : (I)V
        //   5313: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   5316: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   5319: dup
        //   5320: aload_0
        //   5321: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5324: invokestatic access$29 : ()I
        //   5327: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   5330: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   5333: pop
        //   5334: aload_0
        //   5335: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5338: invokestatic access$6 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Ljava/lang/String;
        //   5341: invokestatic getFlashProtocolValueType : (Ljava/lang/String;)I
        //   5344: tableswitch default -> 5364, 2 -> 5390
        //   5364: aload_0
        //   5365: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5368: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   5371: ldc_w 'off'
        //   5374: invokevirtual setCurrentFlashDisplay : (Ljava/lang/String;)V
        //   5377: aload_0
        //   5378: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5381: invokestatic access$31 : ()Landroid/widget/ImageButton;
        //   5384: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   5387: goto -> 5055
        //   5390: aload_0
        //   5391: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5394: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   5397: ldc_w 'OFF'
        //   5400: invokevirtual setCurrentFlashDisplay : (Ljava/lang/String;)V
        //   5403: goto -> 5377
        //   5406: aload #6
        //   5408: invokevirtual getCode : ()I
        //   5411: ifeq -> 5055
        //   5414: iload_2
        //   5415: sipush #501
        //   5418: if_icmpne -> 5490
        //   5421: aload_0
        //   5422: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5425: invokestatic access$6 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Ljava/lang/String;
        //   5428: invokestatic getFlashProtocolValueType : (Ljava/lang/String;)I
        //   5431: tableswitch default -> 5448, 2 -> 5474
        //   5448: aload_0
        //   5449: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5452: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   5455: ldc_w 'off'
        //   5458: invokevirtual setCurrentFlashDisplay : (Ljava/lang/String;)V
        //   5461: aload_0
        //   5462: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5465: invokestatic access$31 : ()Landroid/widget/ImageButton;
        //   5468: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   5471: goto -> 5055
        //   5474: aload_0
        //   5475: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5478: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   5481: ldc_w 'OFF'
        //   5484: invokevirtual setCurrentFlashDisplay : (Ljava/lang/String;)V
        //   5487: goto -> 5461
        //   5490: aload_0
        //   5491: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5494: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   5497: new java/lang/StringBuilder
        //   5500: dup
        //   5501: ldc_w 'DISPLAY_NETERRORMSG 11'
        //   5504: invokespecial <init> : (Ljava/lang/String;)V
        //   5507: aload #6
        //   5509: invokevirtual toString : ()Ljava/lang/String;
        //   5512: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   5515: invokevirtual toString : ()Ljava/lang/String;
        //   5518: invokestatic e : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   5521: aload_0
        //   5522: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5525: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   5528: ldc 'MESSAGE : DISPLAY_NETERRORMSG'
        //   5530: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   5533: bipush #10
        //   5535: invokestatic access$28 : (I)V
        //   5538: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   5541: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   5544: dup
        //   5545: aload_0
        //   5546: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5549: invokestatic access$29 : ()I
        //   5552: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   5555: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   5558: pop
        //   5559: goto -> 5055
        //   5562: aload_0
        //   5563: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5566: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   5569: return
        //   5570: aload_1
        //   5571: getfield obj : Ljava/lang/Object;
        //   5574: checkcast java/lang/Boolean
        //   5577: invokevirtual booleanValue : ()Z
        //   5580: istore #4
        //   5582: aload_0
        //   5583: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5586: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   5589: new java/lang/StringBuilder
        //   5592: dup
        //   5593: ldc_w 'result : '
        //   5596: invokespecial <init> : (Ljava/lang/String;)V
        //   5599: iload #4
        //   5601: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   5604: invokevirtual toString : ()Ljava/lang/String;
        //   5607: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   5610: iload #4
        //   5612: ifeq -> 5635
        //   5615: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$RestartCodecTask
        //   5618: dup
        //   5619: aload_0
        //   5620: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5623: aconst_null
        //   5624: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$RestartCodecTask;)V
        //   5627: iconst_0
        //   5628: anewarray java/lang/Integer
        //   5631: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
        //   5634: pop
        //   5635: aload_0
        //   5636: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5639: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   5642: invokevirtual getVersionPrefix : ()Ljava/lang/String;
        //   5645: aload_0
        //   5646: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5649: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   5652: invokevirtual getVersion : ()Ljava/lang/String;
        //   5655: invokestatic isSupportedSmartPanel : (Ljava/lang/String;Ljava/lang/String;)Z
        //   5658: ifeq -> 5702
        //   5661: aload_0
        //   5662: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5665: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/dialog/CustomDialogListMenu;
        //   5668: ifnull -> 5680
        //   5671: aload_0
        //   5672: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5675: bipush #9
        //   5677: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   5680: aload_0
        //   5681: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5684: aload_0
        //   5685: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5688: invokestatic access$87 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   5691: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   5694: aload_0
        //   5695: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5698: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   5701: return
        //   5702: aload_0
        //   5703: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5706: iconst_3
        //   5707: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   5710: aload_0
        //   5711: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5714: invokestatic access$23 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$SubOptionMenuListAdapter;
        //   5717: invokevirtual notifyDataSetChanged : ()V
        //   5720: aload_0
        //   5721: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5724: invokestatic access$21 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$MainOptionMenuListAdapter;
        //   5727: invokevirtual notifyDataSetChanged : ()V
        //   5730: goto -> 5694
        //   5733: aload_0
        //   5734: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5737: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   5740: aload_0
        //   5741: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5744: sipush #1016
        //   5747: invokevirtual showDialog : (I)V
        //   5750: return
        //   5751: aload_1
        //   5752: getfield obj : Ljava/lang/Object;
        //   5755: checkcast java/lang/Boolean
        //   5758: invokevirtual booleanValue : ()Z
        //   5761: istore #4
        //   5763: aload_0
        //   5764: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5767: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   5770: new java/lang/StringBuilder
        //   5773: dup
        //   5774: ldc_w 'result : '
        //   5777: invokespecial <init> : (Ljava/lang/String;)V
        //   5780: iload #4
        //   5782: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   5785: invokevirtual toString : ()Ljava/lang/String;
        //   5788: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   5791: iload #4
        //   5793: ifeq -> 5817
        //   5796: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$StartRecordTask
        //   5799: dup
        //   5800: aload_0
        //   5801: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5804: aconst_null
        //   5805: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$StartRecordTask;)V
        //   5808: iconst_0
        //   5809: anewarray java/lang/Void
        //   5812: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
        //   5815: pop
        //   5816: return
        //   5817: aload_0
        //   5818: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5821: sipush #1016
        //   5824: invokevirtual dismissDialog : (I)V
        //   5827: return
        //   5828: aload_0
        //   5829: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5832: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   5835: return
        //   5836: aload_1
        //   5837: getfield obj : Ljava/lang/Object;
        //   5840: checkcast java/lang/Boolean
        //   5843: invokevirtual booleanValue : ()Z
        //   5846: istore #4
        //   5848: aload_0
        //   5849: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5852: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   5855: new java/lang/StringBuilder
        //   5858: dup
        //   5859: ldc_w 'result : '
        //   5862: invokespecial <init> : (Ljava/lang/String;)V
        //   5865: iload #4
        //   5867: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   5870: invokevirtual toString : ()Ljava/lang/String;
        //   5873: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   5876: iload #4
        //   5878: ifeq -> 5888
        //   5881: aload_0
        //   5882: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5885: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   5888: aload_0
        //   5889: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5892: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   5895: return
        //   5896: aload_0
        //   5897: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5900: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   5903: return
        //   5904: aload_1
        //   5905: getfield obj : Ljava/lang/Object;
        //   5908: checkcast java/lang/Boolean
        //   5911: invokevirtual booleanValue : ()Z
        //   5914: istore #4
        //   5916: aload_0
        //   5917: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5920: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   5923: new java/lang/StringBuilder
        //   5926: dup
        //   5927: ldc_w 'result : '
        //   5930: invokespecial <init> : (Ljava/lang/String;)V
        //   5933: iload #4
        //   5935: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   5938: invokevirtual toString : ()Ljava/lang/String;
        //   5941: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   5944: iload #4
        //   5946: ifeq -> 5956
        //   5949: aload_0
        //   5950: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5953: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   5956: aload_0
        //   5957: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5960: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   5963: return
        //   5964: aload_0
        //   5965: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5968: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   5971: return
        //   5972: aload_1
        //   5973: getfield obj : Ljava/lang/Object;
        //   5976: checkcast java/lang/Boolean
        //   5979: invokevirtual booleanValue : ()Z
        //   5982: istore #4
        //   5984: aload_0
        //   5985: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   5988: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   5991: new java/lang/StringBuilder
        //   5994: dup
        //   5995: ldc_w 'result : '
        //   5998: invokespecial <init> : (Ljava/lang/String;)V
        //   6001: iload #4
        //   6003: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   6006: invokevirtual toString : ()Ljava/lang/String;
        //   6009: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   6012: iload #4
        //   6014: ifeq -> 6024
        //   6017: aload_0
        //   6018: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6021: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6024: aload_0
        //   6025: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6028: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6031: return
        //   6032: aload_0
        //   6033: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6036: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6039: return
        //   6040: aload_1
        //   6041: getfield obj : Ljava/lang/Object;
        //   6044: checkcast java/lang/Boolean
        //   6047: invokevirtual booleanValue : ()Z
        //   6050: istore #4
        //   6052: aload_0
        //   6053: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6056: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   6059: new java/lang/StringBuilder
        //   6062: dup
        //   6063: ldc_w 'result : '
        //   6066: invokespecial <init> : (Ljava/lang/String;)V
        //   6069: iload #4
        //   6071: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   6074: invokevirtual toString : ()Ljava/lang/String;
        //   6077: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   6080: iload #4
        //   6082: ifeq -> 6092
        //   6085: aload_0
        //   6086: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6089: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6092: aload_0
        //   6093: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6096: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6099: return
        //   6100: aload_0
        //   6101: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6104: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6107: return
        //   6108: aload_1
        //   6109: getfield obj : Ljava/lang/Object;
        //   6112: checkcast java/lang/Boolean
        //   6115: invokevirtual booleanValue : ()Z
        //   6118: istore #4
        //   6120: aload_0
        //   6121: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6124: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   6127: new java/lang/StringBuilder
        //   6130: dup
        //   6131: ldc_w 'result : '
        //   6134: invokespecial <init> : (Ljava/lang/String;)V
        //   6137: iload #4
        //   6139: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   6142: invokevirtual toString : ()Ljava/lang/String;
        //   6145: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   6148: iload #4
        //   6150: ifeq -> 6186
        //   6153: aload_0
        //   6154: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6157: bipush #9
        //   6159: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   6162: aload_0
        //   6163: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6166: aload_0
        //   6167: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6170: invokestatic access$88 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   6173: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   6176: aload_0
        //   6177: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6180: ldc_w 2131558677
        //   6183: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   6186: aload_0
        //   6187: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6190: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6193: return
        //   6194: aload_0
        //   6195: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6198: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6201: return
        //   6202: aload_1
        //   6203: getfield obj : Ljava/lang/Object;
        //   6206: checkcast java/lang/Boolean
        //   6209: invokevirtual booleanValue : ()Z
        //   6212: istore #4
        //   6214: aload_0
        //   6215: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6218: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   6221: new java/lang/StringBuilder
        //   6224: dup
        //   6225: ldc_w 'result : '
        //   6228: invokespecial <init> : (Ljava/lang/String;)V
        //   6231: iload #4
        //   6233: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   6236: invokevirtual toString : ()Ljava/lang/String;
        //   6239: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   6242: iload #4
        //   6244: ifeq -> 6336
        //   6247: aload_0
        //   6248: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6251: bipush #9
        //   6253: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   6256: aload_0
        //   6257: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6260: aload_0
        //   6261: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6264: invokestatic access$89 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   6267: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   6270: aload_0
        //   6271: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6274: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   6277: invokevirtual getAFAreaValue : ()Ljava/lang/String;
        //   6280: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   6283: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   6286: ldc 'MULTI AF'
        //   6288: invokevirtual equals : (Ljava/lang/Object;)Z
        //   6291: ifeq -> 6344
        //   6294: aload_0
        //   6295: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6298: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   6301: invokevirtual getMeteringValue : ()Ljava/lang/String;
        //   6304: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   6307: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   6310: ldc_w 'SPOT'
        //   6313: invokevirtual equals : (Ljava/lang/Object;)Z
        //   6316: ifeq -> 6344
        //   6319: invokestatic access$90 : ()Landroid/widget/ImageView;
        //   6322: iconst_0
        //   6323: invokevirtual setVisibility : (I)V
        //   6326: aload_0
        //   6327: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6330: ldc_w 2131558678
        //   6333: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   6336: aload_0
        //   6337: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6340: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6343: return
        //   6344: invokestatic access$90 : ()Landroid/widget/ImageView;
        //   6347: iconst_4
        //   6348: invokevirtual setVisibility : (I)V
        //   6351: goto -> 6326
        //   6354: aload_0
        //   6355: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6358: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6361: return
        //   6362: aload_1
        //   6363: getfield obj : Ljava/lang/Object;
        //   6366: checkcast java/lang/Boolean
        //   6369: invokevirtual booleanValue : ()Z
        //   6372: istore #4
        //   6374: aload_0
        //   6375: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6378: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   6381: new java/lang/StringBuilder
        //   6384: dup
        //   6385: ldc_w 'result : '
        //   6388: invokespecial <init> : (Ljava/lang/String;)V
        //   6391: iload #4
        //   6393: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   6396: invokevirtual toString : ()Ljava/lang/String;
        //   6399: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   6402: iload #4
        //   6404: ifeq -> 6430
        //   6407: aload_0
        //   6408: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6411: bipush #9
        //   6413: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   6416: aload_0
        //   6417: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6420: aload_0
        //   6421: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6424: invokestatic access$91 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   6427: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   6430: aload_0
        //   6431: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6434: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6437: return
        //   6438: aload_0
        //   6439: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6442: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6445: return
        //   6446: aload_1
        //   6447: getfield obj : Ljava/lang/Object;
        //   6450: checkcast java/lang/Boolean
        //   6453: invokevirtual booleanValue : ()Z
        //   6456: istore #4
        //   6458: aload_0
        //   6459: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6462: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   6465: new java/lang/StringBuilder
        //   6468: dup
        //   6469: ldc_w 'result : '
        //   6472: invokespecial <init> : (Ljava/lang/String;)V
        //   6475: iload #4
        //   6477: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   6480: invokevirtual toString : ()Ljava/lang/String;
        //   6483: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   6486: iload #4
        //   6488: ifeq -> 6605
        //   6491: aload_0
        //   6492: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6495: bipush #9
        //   6497: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   6500: aload_0
        //   6501: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6504: aload_0
        //   6505: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6508: invokestatic access$33 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   6511: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   6514: aload_0
        //   6515: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6518: ldc_w 2131558684
        //   6521: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   6524: aload_0
        //   6525: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6528: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   6531: invokevirtual getAFAreaValue : ()Ljava/lang/String;
        //   6534: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   6537: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   6540: ldc 'MULTI AF'
        //   6542: invokevirtual equals : (Ljava/lang/Object;)Z
        //   6545: ifeq -> 6613
        //   6548: aload_0
        //   6549: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6552: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   6555: invokevirtual getDialModeValue : ()Ljava/lang/String;
        //   6558: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   6561: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   6564: ldc_w 'AUTO'
        //   6567: invokevirtual equals : (Ljava/lang/Object;)Z
        //   6570: ifne -> 6613
        //   6573: aload_0
        //   6574: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6577: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   6580: invokevirtual getMeteringValue : ()Ljava/lang/String;
        //   6583: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   6586: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   6589: ldc_w 'SPOT'
        //   6592: invokevirtual equals : (Ljava/lang/Object;)Z
        //   6595: ifeq -> 6613
        //   6598: invokestatic access$90 : ()Landroid/widget/ImageView;
        //   6601: iconst_0
        //   6602: invokevirtual setVisibility : (I)V
        //   6605: aload_0
        //   6606: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6609: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6612: return
        //   6613: invokestatic access$90 : ()Landroid/widget/ImageView;
        //   6616: iconst_4
        //   6617: invokevirtual setVisibility : (I)V
        //   6620: goto -> 6605
        //   6623: aload_0
        //   6624: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6627: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6630: return
        //   6631: aload_1
        //   6632: getfield obj : Ljava/lang/Object;
        //   6635: checkcast java/lang/Boolean
        //   6638: invokevirtual booleanValue : ()Z
        //   6641: istore #4
        //   6643: aload_0
        //   6644: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6647: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   6650: new java/lang/StringBuilder
        //   6653: dup
        //   6654: ldc_w 'result : '
        //   6657: invokespecial <init> : (Ljava/lang/String;)V
        //   6660: iload #4
        //   6662: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   6665: invokevirtual toString : ()Ljava/lang/String;
        //   6668: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   6671: iload #4
        //   6673: ifeq -> 6719
        //   6676: aload_0
        //   6677: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6680: bipush #9
        //   6682: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   6685: aload_0
        //   6686: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6689: aload_0
        //   6690: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6693: invokestatic access$86 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   6696: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   6699: aload_0
        //   6700: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6703: ldc_w 2131558679
        //   6706: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   6709: aload_0
        //   6710: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6713: ldc_w 2131558686
        //   6716: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   6719: aload_0
        //   6720: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6723: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6726: return
        //   6727: aload_0
        //   6728: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6731: sipush #1010
        //   6734: invokevirtual showDialog : (I)V
        //   6737: return
        //   6738: aload_1
        //   6739: getfield obj : Ljava/lang/Object;
        //   6742: checkcast java/lang/Boolean
        //   6745: invokevirtual booleanValue : ()Z
        //   6748: istore #4
        //   6750: aload_0
        //   6751: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6754: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   6757: new java/lang/StringBuilder
        //   6760: dup
        //   6761: ldc_w 'result : '
        //   6764: invokespecial <init> : (Ljava/lang/String;)V
        //   6767: iload #4
        //   6769: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   6772: invokevirtual toString : ()Ljava/lang/String;
        //   6775: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   6778: aload_0
        //   6779: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6782: invokestatic access$52 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/os/Handler;
        //   6785: iconst_5
        //   6786: invokevirtual removeMessages : (I)V
        //   6789: return
        //   6790: aload_1
        //   6791: getfield obj : Ljava/lang/Object;
        //   6794: checkcast java/lang/Boolean
        //   6797: invokevirtual booleanValue : ()Z
        //   6800: istore #4
        //   6802: aload_0
        //   6803: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6806: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   6809: new java/lang/StringBuilder
        //   6812: dup
        //   6813: ldc_w 'result : '
        //   6816: invokespecial <init> : (Ljava/lang/String;)V
        //   6819: iload #4
        //   6821: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   6824: invokevirtual toString : ()Ljava/lang/String;
        //   6827: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   6830: iload #4
        //   6832: ifeq -> 6863
        //   6835: aload_0
        //   6836: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6839: iconst_0
        //   6840: invokestatic access$73 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   6843: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$ShotPrepareTask
        //   6846: dup
        //   6847: aload_0
        //   6848: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6851: aconst_null
        //   6852: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$ShotPrepareTask;)V
        //   6855: iconst_0
        //   6856: anewarray java/lang/String
        //   6859: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
        //   6862: pop
        //   6863: aload_0
        //   6864: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6867: iconst_0
        //   6868: invokestatic access$73 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   6871: iconst_0
        //   6872: invokestatic access$43 : (Z)V
        //   6875: aload_0
        //   6876: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6879: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6882: return
        //   6883: aload_0
        //   6884: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6887: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   6890: return
        //   6891: aload_1
        //   6892: getfield obj : Ljava/lang/Object;
        //   6895: checkcast java/lang/Boolean
        //   6898: invokevirtual booleanValue : ()Z
        //   6901: istore #4
        //   6903: aload_0
        //   6904: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6907: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   6910: new java/lang/StringBuilder
        //   6913: dup
        //   6914: ldc_w 'result : '
        //   6917: invokespecial <init> : (Ljava/lang/String;)V
        //   6920: iload #4
        //   6922: invokevirtual append : (Z)Ljava/lang/StringBuilder;
        //   6925: invokevirtual toString : ()Ljava/lang/String;
        //   6928: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   6931: iload #4
        //   6933: ifeq -> 7238
        //   6936: aload_0
        //   6937: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6940: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   6943: invokevirtual getVersionPrefix : ()Ljava/lang/String;
        //   6946: aload_0
        //   6947: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6950: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   6953: invokevirtual getVersion : ()Ljava/lang/String;
        //   6956: invokestatic isSupportedSmartPanel : (Ljava/lang/String;Ljava/lang/String;)Z
        //   6959: ifeq -> 7035
        //   6962: aload_0
        //   6963: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6966: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   6969: bipush #32
        //   6971: invokevirtual getAction : (I)Lorg/cybergarage/upnp/Action;
        //   6974: ldc_w 'RatioChangeStatus'
        //   6977: invokevirtual getArgumentValue : (Ljava/lang/String;)Ljava/lang/String;
        //   6980: ldc_w '1'
        //   6983: invokevirtual equals : (Ljava/lang/Object;)Z
        //   6986: ifeq -> 7294
        //   6989: aload_0
        //   6990: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   6993: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   6996: invokevirtual getQualityValue : ()Ljava/lang/String;
        //   6999: ldc_w 'RAW'
        //   7002: invokevirtual equals : (Ljava/lang/Object;)Z
        //   7005: ifeq -> 7271
        //   7008: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$RestartCodecTask
        //   7011: dup
        //   7012: aload_0
        //   7013: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7016: aconst_null
        //   7017: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$RestartCodecTask;)V
        //   7020: iconst_1
        //   7021: anewarray java/lang/Integer
        //   7024: dup
        //   7025: iconst_0
        //   7026: iconst_3
        //   7027: invokestatic valueOf : (I)Ljava/lang/Integer;
        //   7030: aastore
        //   7031: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
        //   7034: pop
        //   7035: aload_0
        //   7036: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7039: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7042: invokevirtual getDialModeValue : ()Ljava/lang/String;
        //   7045: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   7048: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   7051: ldc_w 'AUTO'
        //   7054: invokevirtual equals : (Ljava/lang/Object;)Z
        //   7057: ifeq -> 7370
        //   7060: invokestatic access$92 : ()Landroid/widget/TextView;
        //   7063: ldc_w 'Auto'
        //   7066: invokevirtual setText : (Ljava/lang/CharSequence;)V
        //   7069: aload_0
        //   7070: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7073: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7076: invokevirtual getVersionPrefix : ()Ljava/lang/String;
        //   7079: aload_0
        //   7080: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7083: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7086: invokevirtual getVersion : ()Ljava/lang/String;
        //   7089: invokestatic isSupportedSmartPanel : (Ljava/lang/String;Ljava/lang/String;)Z
        //   7092: ifeq -> 7183
        //   7095: aload_0
        //   7096: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7099: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   7102: aload_0
        //   7103: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7106: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7109: invokevirtual getAFAreaValue : ()Ljava/lang/String;
        //   7112: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   7115: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   7118: ldc 'MULTI AF'
        //   7120: invokevirtual equals : (Ljava/lang/Object;)Z
        //   7123: ifeq -> 7518
        //   7126: aload_0
        //   7127: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7130: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7133: invokevirtual getDialModeValue : ()Ljava/lang/String;
        //   7136: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   7139: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   7142: ldc_w 'AUTO'
        //   7145: invokevirtual equals : (Ljava/lang/Object;)Z
        //   7148: ifne -> 7518
        //   7151: aload_0
        //   7152: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7155: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7158: invokevirtual getMeteringValue : ()Ljava/lang/String;
        //   7161: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   7164: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   7167: ldc_w 'SPOT'
        //   7170: invokevirtual equals : (Ljava/lang/Object;)Z
        //   7173: ifeq -> 7518
        //   7176: invokestatic access$90 : ()Landroid/widget/ImageView;
        //   7179: iconst_0
        //   7180: invokevirtual setVisibility : (I)V
        //   7183: aload_0
        //   7184: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7187: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7190: invokevirtual getAFAreaMenuItems : ()Ljava/util/ArrayList;
        //   7193: invokevirtual size : ()I
        //   7196: ifle -> 7528
        //   7199: aload_0
        //   7200: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7203: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7206: invokevirtual getAFAreaValue : ()Ljava/lang/String;
        //   7209: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   7212: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   7215: ldc 'SELECTION AF'
        //   7217: invokevirtual equals : (Ljava/lang/Object;)Z
        //   7220: ifeq -> 7528
        //   7223: invokestatic access$36 : ()Landroid/widget/ImageView;
        //   7226: ldc 2130837575
        //   7228: invokevirtual setImageResource : (I)V
        //   7231: invokestatic access$36 : ()Landroid/widget/ImageView;
        //   7234: iconst_0
        //   7235: invokevirtual setVisibility : (I)V
        //   7238: aload_0
        //   7239: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7242: invokestatic access$93 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/dialog/CustomDialogWheelMenu;
        //   7245: iconst_1
        //   7246: invokevirtual setCancelable : (Z)V
        //   7249: aload_0
        //   7250: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7253: invokestatic access$58 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   7256: aload_0
        //   7257: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7260: invokestatic access$20 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   7263: aload_0
        //   7264: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7267: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   7270: return
        //   7271: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$RestartCodecTask
        //   7274: dup
        //   7275: aload_0
        //   7276: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7279: aconst_null
        //   7280: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$RestartCodecTask;)V
        //   7283: iconst_0
        //   7284: anewarray java/lang/Integer
        //   7287: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
        //   7290: pop
        //   7291: goto -> 7035
        //   7294: aload_0
        //   7295: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7298: invokestatic access$6 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Ljava/lang/String;
        //   7301: invokestatic isNotSupportedRawQualityAndContinuousShotAtTheSameTime : (Ljava/lang/String;)Z
        //   7304: ifeq -> 7035
        //   7307: aload_0
        //   7308: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7311: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7314: invokevirtual getQualityValue : ()Ljava/lang/String;
        //   7317: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   7320: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   7323: ldc_w 'RAW'
        //   7326: invokevirtual contains : (Ljava/lang/CharSequence;)Z
        //   7329: ifeq -> 7035
        //   7332: aload_0
        //   7333: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7336: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7339: invokevirtual getDriveValue : ()Ljava/lang/String;
        //   7342: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   7345: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   7348: ldc_w 'CONTINUOUS'
        //   7351: invokevirtual contains : (Ljava/lang/CharSequence;)Z
        //   7354: ifeq -> 7035
        //   7357: aload_0
        //   7358: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7361: sipush #2007
        //   7364: invokevirtual showDialog : (I)V
        //   7367: goto -> 7035
        //   7370: aload_0
        //   7371: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7374: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7377: invokevirtual getDialModeValue : ()Ljava/lang/String;
        //   7380: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   7383: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   7386: ldc_w 'P'
        //   7389: invokevirtual equals : (Ljava/lang/Object;)Z
        //   7392: ifeq -> 7407
        //   7395: invokestatic access$92 : ()Landroid/widget/TextView;
        //   7398: ldc_w 'Program'
        //   7401: invokevirtual setText : (Ljava/lang/CharSequence;)V
        //   7404: goto -> 7069
        //   7407: aload_0
        //   7408: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7411: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7414: invokevirtual getDialModeValue : ()Ljava/lang/String;
        //   7417: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   7420: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   7423: ldc_w 'A'
        //   7426: invokevirtual equals : (Ljava/lang/Object;)Z
        //   7429: ifeq -> 7444
        //   7432: invokestatic access$92 : ()Landroid/widget/TextView;
        //   7435: ldc_w 'Aperture Priority'
        //   7438: invokevirtual setText : (Ljava/lang/CharSequence;)V
        //   7441: goto -> 7069
        //   7444: aload_0
        //   7445: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7448: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7451: invokevirtual getDialModeValue : ()Ljava/lang/String;
        //   7454: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   7457: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   7460: ldc_w 'S'
        //   7463: invokevirtual equals : (Ljava/lang/Object;)Z
        //   7466: ifeq -> 7481
        //   7469: invokestatic access$92 : ()Landroid/widget/TextView;
        //   7472: ldc_w 'Shutter Priority'
        //   7475: invokevirtual setText : (Ljava/lang/CharSequence;)V
        //   7478: goto -> 7069
        //   7481: aload_0
        //   7482: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7485: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7488: invokevirtual getDialModeValue : ()Ljava/lang/String;
        //   7491: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   7494: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   7497: ldc_w 'M'
        //   7500: invokevirtual equals : (Ljava/lang/Object;)Z
        //   7503: ifeq -> 7069
        //   7506: invokestatic access$92 : ()Landroid/widget/TextView;
        //   7509: ldc_w 'Manual'
        //   7512: invokevirtual setText : (Ljava/lang/CharSequence;)V
        //   7515: goto -> 7069
        //   7518: invokestatic access$90 : ()Landroid/widget/ImageView;
        //   7521: iconst_4
        //   7522: invokevirtual setVisibility : (I)V
        //   7525: goto -> 7183
        //   7528: aload_0
        //   7529: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7532: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7535: invokevirtual getAFAreaValue : ()Ljava/lang/String;
        //   7538: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   7541: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   7544: ldc 'MULTI AF'
        //   7546: invokevirtual equals : (Ljava/lang/Object;)Z
        //   7549: ifeq -> 7238
        //   7552: invokestatic access$36 : ()Landroid/widget/ImageView;
        //   7555: iconst_4
        //   7556: invokevirtual setVisibility : (I)V
        //   7559: goto -> 7238
        //   7562: aload_1
        //   7563: getfield obj : Ljava/lang/Object;
        //   7566: checkcast java/lang/Boolean
        //   7569: invokevirtual booleanValue : ()Z
        //   7572: ifeq -> 7737
        //   7575: aload_0
        //   7576: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7579: invokevirtual getSmartPanelCustomDialogListMenuId : ()I
        //   7582: ifeq -> 7594
        //   7585: aload_0
        //   7586: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7589: bipush #9
        //   7591: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   7594: aload_0
        //   7595: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7598: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7601: invokevirtual getVersionPrefix : ()Ljava/lang/String;
        //   7604: aload_0
        //   7605: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7608: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7611: invokevirtual getVersion : ()Ljava/lang/String;
        //   7614: invokestatic isSupportedSmartPanel : (Ljava/lang/String;Ljava/lang/String;)Z
        //   7617: ifeq -> 7693
        //   7620: aload_0
        //   7621: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7624: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7627: bipush #33
        //   7629: invokevirtual getAction : (I)Lorg/cybergarage/upnp/Action;
        //   7632: ldc_w 'RatioChangeStatus'
        //   7635: invokevirtual getArgumentValue : (Ljava/lang/String;)Ljava/lang/String;
        //   7638: ldc_w '1'
        //   7641: invokevirtual equals : (Ljava/lang/Object;)Z
        //   7644: ifeq -> 7693
        //   7647: aload_0
        //   7648: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7651: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7654: invokevirtual getQualityValue : ()Ljava/lang/String;
        //   7657: ldc_w 'RAW'
        //   7660: invokevirtual equals : (Ljava/lang/Object;)Z
        //   7663: ifeq -> 7780
        //   7666: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$RestartCodecTask
        //   7669: dup
        //   7670: aload_0
        //   7671: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7674: aconst_null
        //   7675: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$RestartCodecTask;)V
        //   7678: iconst_1
        //   7679: anewarray java/lang/Integer
        //   7682: dup
        //   7683: iconst_0
        //   7684: iconst_3
        //   7685: invokestatic valueOf : (I)Ljava/lang/Integer;
        //   7688: aastore
        //   7689: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
        //   7692: pop
        //   7693: aload_0
        //   7694: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7697: aload_0
        //   7698: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7701: invokestatic access$94 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   7704: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   7707: aload_0
        //   7708: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7711: ldc_w 2131558682
        //   7714: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   7717: aload_0
        //   7718: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7721: ldc_w 2131558681
        //   7724: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   7727: aload_0
        //   7728: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7731: ldc_w 2131558686
        //   7734: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   7737: aload_0
        //   7738: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7741: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   7744: aload_0
        //   7745: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7748: invokestatic access$39 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   7751: ifne -> 7772
        //   7754: aload_0
        //   7755: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7758: invokestatic access$95 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)I
        //   7761: ifeq -> 7772
        //   7764: aload_0
        //   7765: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7768: iconst_1
        //   7769: invokestatic access$45 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   7772: aload_0
        //   7773: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7776: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   7779: return
        //   7780: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$RestartCodecTask
        //   7783: dup
        //   7784: aload_0
        //   7785: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7788: aconst_null
        //   7789: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$RestartCodecTask;)V
        //   7792: iconst_0
        //   7793: anewarray java/lang/Integer
        //   7796: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
        //   7799: pop
        //   7800: goto -> 7693
        //   7803: aload_1
        //   7804: getfield obj : Ljava/lang/Object;
        //   7807: checkcast java/lang/Boolean
        //   7810: invokevirtual booleanValue : ()Z
        //   7813: ifeq -> 7849
        //   7816: aload_0
        //   7817: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7820: bipush #9
        //   7822: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   7825: aload_0
        //   7826: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7829: aload_0
        //   7830: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7833: invokestatic access$96 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   7836: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   7839: aload_0
        //   7840: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7843: ldc_w 2131558683
        //   7846: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   7849: aload_0
        //   7850: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7853: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   7856: return
        //   7857: aload_1
        //   7858: getfield obj : Ljava/lang/Object;
        //   7861: checkcast java/lang/Boolean
        //   7864: invokevirtual booleanValue : ()Z
        //   7867: ifeq -> 7990
        //   7870: aload_0
        //   7871: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7874: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7877: invokevirtual getStreamUrlMenuItems : ()Ljava/util/ArrayList;
        //   7880: invokevirtual size : ()I
        //   7883: iconst_2
        //   7884: if_icmpgt -> 7895
        //   7887: aload_0
        //   7888: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7891: iconst_2
        //   7892: invokestatic access$97 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   7895: aload_0
        //   7896: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7899: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7902: bipush #35
        //   7904: invokevirtual getAction : (I)Lorg/cybergarage/upnp/Action;
        //   7907: invokevirtual getControlResponse : ()Lorg/cybergarage/upnp/control/ControlResponse;
        //   7910: invokevirtual getUPnPErrorCode : ()I
        //   7913: sipush #804
        //   7916: if_icmpne -> 7958
        //   7919: aload_0
        //   7920: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7923: sipush #1016
        //   7926: invokevirtual dismissDialog : (I)V
        //   7929: aload_0
        //   7930: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7933: sipush #1018
        //   7936: invokevirtual showDialog : (I)V
        //   7939: aload_0
        //   7940: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7943: bipush #36
        //   7945: ldc ''
        //   7947: invokevirtual doAction : (ILjava/lang/String;)V
        //   7950: aload_0
        //   7951: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7954: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   7957: return
        //   7958: aload_0
        //   7959: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7962: invokestatic access$52 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/os/Handler;
        //   7965: bipush #7
        //   7967: invokevirtual removeMessages : (I)V
        //   7970: aload_0
        //   7971: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7974: sipush #1016
        //   7977: invokevirtual dismissDialog : (I)V
        //   7980: aload_0
        //   7981: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7984: invokestatic access$98 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   7987: goto -> 7950
        //   7990: aload_0
        //   7991: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   7994: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   7997: bipush #35
        //   7999: invokevirtual getAction : (I)Lorg/cybergarage/upnp/Action;
        //   8002: invokevirtual getControlResponse : ()Lorg/cybergarage/upnp/control/ControlResponse;
        //   8005: invokevirtual getUPnPErrorCode : ()I
        //   8008: sipush #804
        //   8011: if_icmpne -> 8048
        //   8014: aload_0
        //   8015: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8018: sipush #1016
        //   8021: invokevirtual dismissDialog : (I)V
        //   8024: aload_0
        //   8025: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8028: sipush #1018
        //   8031: invokevirtual showDialog : (I)V
        //   8034: aload_0
        //   8035: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8038: bipush #36
        //   8040: ldc ''
        //   8042: invokevirtual doAction : (ILjava/lang/String;)V
        //   8045: goto -> 7950
        //   8048: aload_0
        //   8049: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8052: invokestatic access$52 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/os/Handler;
        //   8055: bipush #7
        //   8057: invokevirtual removeMessages : (I)V
        //   8060: aload_0
        //   8061: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8064: sipush #1016
        //   8067: invokevirtual dismissDialog : (I)V
        //   8070: aload_0
        //   8071: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8074: invokestatic access$98 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   8077: goto -> 7950
        //   8080: aload_0
        //   8081: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8084: invokestatic access$99 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageView;
        //   8087: bipush #8
        //   8089: invokevirtual setVisibility : (I)V
        //   8092: invokestatic access$36 : ()Landroid/widget/ImageView;
        //   8095: iconst_4
        //   8096: invokevirtual setVisibility : (I)V
        //   8099: aload_0
        //   8100: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8103: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   8106: iconst_0
        //   8107: invokevirtual setTouchAFMovie : (Z)V
        //   8110: aload_0
        //   8111: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8114: invokestatic access$50 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/common/Timer;
        //   8117: ifnull -> 404
        //   8120: aload_0
        //   8121: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8124: invokestatic access$50 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/common/Timer;
        //   8127: invokevirtual interrupt : ()V
        //   8130: aload_0
        //   8131: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8134: aconst_null
        //   8135: invokestatic access$51 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/common/Timer;)V
        //   8138: return
        //   8139: aload_0
        //   8140: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8143: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   8146: invokevirtual getStreamUrlMenuItems : ()Ljava/util/ArrayList;
        //   8149: invokevirtual size : ()I
        //   8152: iconst_2
        //   8153: if_icmpgt -> 8164
        //   8156: aload_0
        //   8157: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8160: iconst_2
        //   8161: invokestatic access$100 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   8164: aload_0
        //   8165: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8168: bipush #7
        //   8170: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   8173: aload_0
        //   8174: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8177: bipush #18
        //   8179: aload_0
        //   8180: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8183: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   8186: invokevirtual getCurrentStreamQuality : ()Ljava/lang/String;
        //   8189: invokevirtual doAction : (ILjava/lang/String;)V
        //   8192: aload_0
        //   8193: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8196: invokestatic access$39 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   8199: ifne -> 8211
        //   8202: aload_0
        //   8203: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8206: iconst_1
        //   8207: invokestatic access$45 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   8210: return
        //   8211: aload_0
        //   8212: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8215: invokestatic access$101 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   8218: ifne -> 404
        //   8221: aload_0
        //   8222: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8225: iconst_0
        //   8226: invokestatic access$45 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   8229: return
        //   8230: aload_0
        //   8231: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8234: invokestatic access$59 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   8237: return
        //   8238: aload_0
        //   8239: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8242: invokestatic access$15 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)I
        //   8245: ifne -> 8365
        //   8248: aload_0
        //   8249: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8252: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   8255: invokevirtual getVersionPrefix : ()Ljava/lang/String;
        //   8258: aload_0
        //   8259: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8262: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   8265: invokevirtual getVersion : ()Ljava/lang/String;
        //   8268: aload_0
        //   8269: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8272: invokestatic access$6 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Ljava/lang/String;
        //   8275: invokestatic getSaveGuideDialogType : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I
        //   8278: ifeq -> 8291
        //   8281: aload_0
        //   8282: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8285: sipush #1017
        //   8288: invokevirtual showDialog : (I)V
        //   8291: invokestatic isMemoryFull : ()Z
        //   8294: ifeq -> 8317
        //   8297: aload_0
        //   8298: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8301: invokestatic access$16 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/ExToast;
        //   8304: bipush #7
        //   8306: invokevirtual show : (I)V
        //   8309: aload_0
        //   8310: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8313: iconst_0
        //   8314: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   8317: aload_0
        //   8318: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8321: aload_0
        //   8322: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8325: invokestatic access$102 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   8328: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   8331: aload_0
        //   8332: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8335: ldc_w 2131558686
        //   8338: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   8341: aload_0
        //   8342: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8345: invokestatic access$20 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   8348: aload_0
        //   8349: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8352: bipush #9
        //   8354: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   8357: aload_0
        //   8358: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8361: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   8364: return
        //   8365: aload_0
        //   8366: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8369: iconst_1
        //   8370: invokestatic access$17 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   8373: goto -> 8317
        //   8376: aload_0
        //   8377: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8380: invokestatic access$99 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageView;
        //   8383: bipush #8
        //   8385: invokevirtual setVisibility : (I)V
        //   8388: invokestatic access$36 : ()Landroid/widget/ImageView;
        //   8391: iconst_4
        //   8392: invokevirtual setVisibility : (I)V
        //   8395: aload_0
        //   8396: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8399: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   8402: iconst_0
        //   8403: invokevirtual setTouchAFMovie : (Z)V
        //   8406: aload_0
        //   8407: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8410: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   8413: return
        //   8414: invokestatic access$36 : ()Landroid/widget/ImageView;
        //   8417: iconst_0
        //   8418: invokevirtual setVisibility : (I)V
        //   8421: invokestatic access$36 : ()Landroid/widget/ImageView;
        //   8424: ldc 2130837575
        //   8426: invokevirtual setImageResource : (I)V
        //   8429: aload_0
        //   8430: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8433: invokestatic access$99 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageView;
        //   8436: iconst_0
        //   8437: invokevirtual setVisibility : (I)V
        //   8440: aload_0
        //   8441: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8444: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   8447: iconst_1
        //   8448: invokevirtual setTouchAFMovie : (Z)V
        //   8451: aload_0
        //   8452: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8455: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   8458: return
        //   8459: aload_0
        //   8460: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   8463: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   8466: return
        //   8467: astore #6
        //   8469: goto -> 2108
        //   8472: astore #6
        //   8474: goto -> 2204
        // Exception table:
        //   from	to	target	type
        //   2102	2108	8467	java/lang/Throwable
        //   2198	2204	8472	java/lang/Throwable
      }
    };
  
  private DeviceList mDeviceList;
  
  private int mDoubleTimerShotState = 0;
  
  private Handler mEventHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        // Byte code:
        //   0: aload_0
        //   1: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   4: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   7: new java/lang/StringBuilder
        //   10: dup
        //   11: ldc 'start handleMessage() : '
        //   13: invokespecial <init> : (Ljava/lang/String;)V
        //   16: aload_1
        //   17: getfield what : I
        //   20: invokevirtual append : (I)Ljava/lang/StringBuilder;
        //   23: invokevirtual toString : ()Ljava/lang/String;
        //   26: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   29: aload_1
        //   30: getfield what : I
        //   33: tableswitch default -> 112, 102 -> 113, 103 -> 377, 104 -> 557, 105 -> 907, 106 -> 1183, 107 -> 1221, 108 -> 1259, 109 -> 1297, 110 -> 1532, 111 -> 1616, 112 -> 1669, 113 -> 1751, 114 -> 1833, 115 -> 1967, 116 -> 2000, 117 -> 2108
        //   112: return
        //   113: aload_1
        //   114: getfield obj : Ljava/lang/Object;
        //   117: checkcast java/lang/String
        //   120: astore_1
        //   121: aload_1
        //   122: ldc 'rotation'
        //   124: invokevirtual contains : (Ljava/lang/CharSequence;)Z
        //   127: ifeq -> 225
        //   130: aload_1
        //   131: aload_0
        //   132: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   135: invokestatic access$24 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Ljava/lang/String;
        //   138: invokevirtual equals : (Ljava/lang/Object;)Z
        //   141: ifne -> 225
        //   144: aload_1
        //   145: ldc 'ssdp:rotationD'
        //   147: invokevirtual equals : (Ljava/lang/Object;)Z
        //   150: ifne -> 180
        //   153: aload_1
        //   154: ldc 'ssdp:rotationU'
        //   156: invokevirtual equals : (Ljava/lang/Object;)Z
        //   159: ifne -> 180
        //   162: aload_1
        //   163: ldc 'ssdp:rotationL'
        //   165: invokevirtual equals : (Ljava/lang/Object;)Z
        //   168: ifne -> 180
        //   171: aload_1
        //   172: ldc 'ssdp:rotationR'
        //   174: invokevirtual equals : (Ljava/lang/Object;)Z
        //   177: ifeq -> 112
        //   180: aload_0
        //   181: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   184: aload_1
        //   185: invokestatic access$25 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Ljava/lang/String;)V
        //   188: aload_0
        //   189: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   192: invokestatic access$26 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   195: aload_0
        //   196: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   199: invokevirtual getPhoneCamType : ()I
        //   202: iconst_2
        //   203: if_icmpeq -> 217
        //   206: aload_0
        //   207: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   210: invokevirtual getPhoneCamType : ()I
        //   213: iconst_1
        //   214: if_icmpne -> 112
        //   217: aload_0
        //   218: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   221: invokestatic access$27 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   224: return
        //   225: aload_1
        //   226: ldc 'flash'
        //   228: invokevirtual contains : (Ljava/lang/CharSequence;)Z
        //   231: ifeq -> 112
        //   234: aload_1
        //   235: bipush #10
        //   237: invokevirtual substring : (I)Ljava/lang/String;
        //   240: invokevirtual toLowerCase : ()Ljava/lang/String;
        //   243: aload_0
        //   244: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   247: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   250: invokevirtual getCurrentFlashDisplay : ()Ljava/lang/String;
        //   253: invokevirtual toLowerCase : ()Ljava/lang/String;
        //   256: invokevirtual equals : (Ljava/lang/Object;)Z
        //   259: ifne -> 112
        //   262: iconst_0
        //   263: istore_2
        //   264: iload_2
        //   265: aload_0
        //   266: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   269: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   272: invokevirtual getFlashDisplayMenuItems : ()Ljava/util/ArrayList;
        //   275: invokevirtual size : ()I
        //   278: if_icmpge -> 112
        //   281: aload_1
        //   282: bipush #10
        //   284: invokevirtual substring : (I)Ljava/lang/String;
        //   287: invokevirtual toLowerCase : ()Ljava/lang/String;
        //   290: aload_0
        //   291: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   294: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   297: invokevirtual getFlashDisplayMenuItems : ()Ljava/util/ArrayList;
        //   300: iload_2
        //   301: invokevirtual get : (I)Ljava/lang/Object;
        //   304: checkcast java/lang/String
        //   307: invokevirtual toLowerCase : ()Ljava/lang/String;
        //   310: invokevirtual equals : (Ljava/lang/Object;)Z
        //   313: ifeq -> 370
        //   316: aload_0
        //   317: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   320: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   323: aload_0
        //   324: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   327: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   330: invokevirtual getFlashDisplayMenuItems : ()Ljava/util/ArrayList;
        //   333: iload_2
        //   334: invokevirtual get : (I)Ljava/lang/Object;
        //   337: checkcast java/lang/String
        //   340: invokevirtual setCurrentFlashDisplay : (Ljava/lang/String;)V
        //   343: bipush #123
        //   345: invokestatic access$28 : (I)V
        //   348: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   351: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   354: dup
        //   355: aload_0
        //   356: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   359: invokestatic access$29 : ()I
        //   362: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   365: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   368: pop
        //   369: return
        //   370: iload_2
        //   371: iconst_1
        //   372: iadd
        //   373: istore_2
        //   374: goto -> 264
        //   377: aload_1
        //   378: getfield obj : Ljava/lang/Object;
        //   381: checkcast java/lang/String
        //   384: ldc 'NULL'
        //   386: invokevirtual equals : (Ljava/lang/Object;)Z
        //   389: ifne -> 112
        //   392: ldc ''
        //   394: astore_3
        //   395: aload_1
        //   396: getfield obj : Ljava/lang/Object;
        //   399: checkcast java/lang/String
        //   402: ldc 'D'
        //   404: invokevirtual equals : (Ljava/lang/Object;)Z
        //   407: ifeq -> 494
        //   410: ldc 'ssdp:rotationD'
        //   412: astore_3
        //   413: aload_3
        //   414: aload_0
        //   415: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   418: invokestatic access$24 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Ljava/lang/String;
        //   421: invokevirtual equals : (Ljava/lang/Object;)Z
        //   424: ifne -> 112
        //   427: aload_0
        //   428: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   431: aload_3
        //   432: invokestatic access$25 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Ljava/lang/String;)V
        //   435: aload_0
        //   436: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   439: invokestatic access$26 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   442: aload_0
        //   443: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   446: invokevirtual getPhoneCamType : ()I
        //   449: iconst_4
        //   450: if_icmpeq -> 486
        //   453: aload_0
        //   454: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   457: invokevirtual getPhoneCamType : ()I
        //   460: iconst_3
        //   461: if_icmpeq -> 486
        //   464: aload_0
        //   465: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   468: invokevirtual getPhoneCamType : ()I
        //   471: iconst_2
        //   472: if_icmpeq -> 486
        //   475: aload_0
        //   476: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   479: invokevirtual getPhoneCamType : ()I
        //   482: iconst_1
        //   483: if_icmpne -> 112
        //   486: aload_0
        //   487: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   490: invokestatic access$27 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   493: return
        //   494: aload_1
        //   495: getfield obj : Ljava/lang/Object;
        //   498: checkcast java/lang/String
        //   501: ldc 'L'
        //   503: invokevirtual equals : (Ljava/lang/Object;)Z
        //   506: ifeq -> 515
        //   509: ldc 'ssdp:rotationL'
        //   511: astore_3
        //   512: goto -> 413
        //   515: aload_1
        //   516: getfield obj : Ljava/lang/Object;
        //   519: checkcast java/lang/String
        //   522: ldc 'U'
        //   524: invokevirtual equals : (Ljava/lang/Object;)Z
        //   527: ifeq -> 536
        //   530: ldc 'ssdp:rotationU'
        //   532: astore_3
        //   533: goto -> 413
        //   536: aload_1
        //   537: getfield obj : Ljava/lang/Object;
        //   540: checkcast java/lang/String
        //   543: ldc 'R'
        //   545: invokevirtual equals : (Ljava/lang/Object;)Z
        //   548: ifeq -> 413
        //   551: ldc 'ssdp:rotationR'
        //   553: astore_3
        //   554: goto -> 413
        //   557: aload_1
        //   558: getfield obj : Ljava/lang/Object;
        //   561: checkcast java/lang/String
        //   564: astore_1
        //   565: aload_1
        //   566: ldc 'NULL'
        //   568: invokevirtual equals : (Ljava/lang/Object;)Z
        //   571: ifne -> 112
        //   574: aload_1
        //   575: invokestatic isNumeric : (Ljava/lang/String;)Z
        //   578: ifeq -> 822
        //   581: aload_1
        //   582: invokestatic parseInt : (Ljava/lang/String;)I
        //   585: ifne -> 734
        //   588: aload_0
        //   589: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   592: invokestatic access$6 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Ljava/lang/String;
        //   595: invokestatic getFlashProtocolValueType : (Ljava/lang/String;)I
        //   598: tableswitch default -> 616, 2 -> 719
        //   616: aload_0
        //   617: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   620: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   623: ldc 'off'
        //   625: invokevirtual setCurrentFlashDisplay : (Ljava/lang/String;)V
        //   628: aload_0
        //   629: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   632: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   635: invokevirtual getFlashStrobeStatus : ()Ljava/lang/String;
        //   638: invokevirtual isEmpty : ()Z
        //   641: ifne -> 656
        //   644: aload_0
        //   645: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   648: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   651: ldc 'DETACHED'
        //   653: invokevirtual setFlashStrobeStatus : (Ljava/lang/String;)V
        //   656: aload_0
        //   657: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   660: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   663: invokevirtual isConnected : ()Z
        //   666: ifeq -> 112
        //   669: aload_0
        //   670: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   673: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   676: invokevirtual getVersionPrefix : ()Ljava/lang/String;
        //   679: aload_0
        //   680: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   683: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   686: invokevirtual getVersion : ()Ljava/lang/String;
        //   689: invokestatic isSupportedSmartPanel : (Ljava/lang/String;Ljava/lang/String;)Z
        //   692: ifeq -> 894
        //   695: aload_0
        //   696: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   699: aload_0
        //   700: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   703: invokestatic access$30 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   706: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   709: aload_0
        //   710: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   713: ldc 2131558680
        //   715: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   718: return
        //   719: aload_0
        //   720: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   723: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   726: ldc 'OFF'
        //   728: invokevirtual setCurrentFlashDisplay : (Ljava/lang/String;)V
        //   731: goto -> 628
        //   734: aload_0
        //   735: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   738: invokestatic access$6 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Ljava/lang/String;
        //   741: invokestatic getFlashProtocolValueType : (Ljava/lang/String;)I
        //   744: tableswitch default -> 764, 2 -> 807
        //   764: aload_0
        //   765: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   768: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   771: ldc 'auto'
        //   773: invokevirtual setCurrentFlashDisplay : (Ljava/lang/String;)V
        //   776: aload_0
        //   777: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   780: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   783: invokevirtual getFlashStrobeStatus : ()Ljava/lang/String;
        //   786: invokevirtual isEmpty : ()Z
        //   789: ifne -> 656
        //   792: aload_0
        //   793: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   796: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   799: ldc 'ATTACHED'
        //   801: invokevirtual setFlashStrobeStatus : (Ljava/lang/String;)V
        //   804: goto -> 656
        //   807: aload_0
        //   808: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   811: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   814: ldc 'AUTO'
        //   816: invokevirtual setCurrentFlashDisplay : (Ljava/lang/String;)V
        //   819: goto -> 776
        //   822: aload_0
        //   823: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   826: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   829: aload_1
        //   830: invokevirtual setCurrentFlashDisplay : (Ljava/lang/String;)V
        //   833: aload_0
        //   834: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   837: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   840: invokevirtual getFlashStrobeStatus : ()Ljava/lang/String;
        //   843: invokevirtual isEmpty : ()Z
        //   846: ifne -> 656
        //   849: aload_1
        //   850: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   853: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   856: ldc 'OFF'
        //   858: invokevirtual equals : (Ljava/lang/Object;)Z
        //   861: ifeq -> 879
        //   864: aload_0
        //   865: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   868: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   871: ldc 'DETACHED'
        //   873: invokevirtual setFlashStrobeStatus : (Ljava/lang/String;)V
        //   876: goto -> 656
        //   879: aload_0
        //   880: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   883: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   886: ldc 'ATTACHED'
        //   888: invokevirtual setFlashStrobeStatus : (Ljava/lang/String;)V
        //   891: goto -> 656
        //   894: aload_0
        //   895: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   898: invokestatic access$31 : ()Landroid/widget/ImageButton;
        //   901: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   904: goto -> 709
        //   907: aload_1
        //   908: getfield obj : Ljava/lang/Object;
        //   911: checkcast java/lang/String
        //   914: astore_3
        //   915: aload_0
        //   916: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   919: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
        //   922: new java/lang/StringBuilder
        //   925: dup
        //   926: ldc 'msg.obj : '
        //   928: invokespecial <init> : (Ljava/lang/String;)V
        //   931: aload_1
        //   932: getfield obj : Ljava/lang/Object;
        //   935: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   938: invokevirtual toString : ()Ljava/lang/String;
        //   941: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
        //   944: aload_3
        //   945: ldc '0'
        //   947: invokevirtual equals : (Ljava/lang/Object;)Z
        //   950: ifeq -> 1043
        //   953: aload_0
        //   954: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   957: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   960: ldc 'af'
        //   962: invokevirtual setDefaultFocusState : (Ljava/lang/String;)V
        //   965: aload_0
        //   966: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   969: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   972: invokevirtual isConnected : ()Z
        //   975: ifeq -> 112
        //   978: aload_0
        //   979: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   982: aload_0
        //   983: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   986: invokestatic access$33 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   989: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   992: aload_0
        //   993: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   996: invokestatic access$33 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   999: iconst_1
        //   1000: invokevirtual setEnabled : (Z)V
        //   1003: aload_0
        //   1004: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1007: aload_0
        //   1008: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1011: invokestatic access$34 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   1014: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   1017: aload_0
        //   1018: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1021: invokestatic access$34 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   1024: iconst_1
        //   1025: invokevirtual setEnabled : (Z)V
        //   1028: aload_0
        //   1029: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1032: aload_0
        //   1033: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1036: invokestatic access$35 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   1039: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   1042: return
        //   1043: aload_0
        //   1044: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1047: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1050: ldc_w 'mf'
        //   1053: invokevirtual setDefaultFocusState : (Ljava/lang/String;)V
        //   1056: aload_0
        //   1057: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1060: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1063: invokevirtual isConnected : ()Z
        //   1066: ifeq -> 112
        //   1069: invokestatic access$36 : ()Landroid/widget/ImageView;
        //   1072: iconst_4
        //   1073: invokevirtual setVisibility : (I)V
        //   1076: aload_0
        //   1077: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1080: aload_0
        //   1081: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1084: invokestatic access$33 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   1087: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   1090: aload_0
        //   1091: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1094: invokestatic access$33 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   1097: iconst_0
        //   1098: invokevirtual setEnabled : (Z)V
        //   1101: aload_0
        //   1102: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1105: aload_0
        //   1106: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1109: invokestatic access$34 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   1112: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   1115: aload_0
        //   1116: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1119: invokestatic access$34 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   1122: iconst_0
        //   1123: invokevirtual setEnabled : (Z)V
        //   1126: aload_0
        //   1127: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1130: invokevirtual getSmartPanelCustomDialogListMenuId : ()I
        //   1133: bipush #11
        //   1135: if_icmpeq -> 1150
        //   1138: aload_0
        //   1139: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1142: invokevirtual getSmartPanelCustomDialogListMenuId : ()I
        //   1145: bipush #28
        //   1147: if_icmpne -> 1168
        //   1150: aload_0
        //   1151: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1154: iconst_0
        //   1155: invokevirtual setSmartPanelCustomDialogListMenuId : (I)V
        //   1158: aload_0
        //   1159: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1162: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/dialog/CustomDialogListMenu;
        //   1165: invokevirtual dismiss : ()V
        //   1168: aload_0
        //   1169: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1172: aload_0
        //   1173: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1176: invokestatic access$35 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   1179: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   1182: return
        //   1183: aload_0
        //   1184: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1187: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1190: aload_1
        //   1191: getfield obj : Ljava/lang/Object;
        //   1194: checkcast java/lang/String
        //   1197: invokevirtual setShutterSpeedValue : (Ljava/lang/String;)V
        //   1200: aload_0
        //   1201: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1204: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1207: invokevirtual isConnected : ()Z
        //   1210: ifeq -> 112
        //   1213: aload_0
        //   1214: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1217: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1220: return
        //   1221: aload_0
        //   1222: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1225: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1228: aload_1
        //   1229: getfield obj : Ljava/lang/Object;
        //   1232: checkcast java/lang/String
        //   1235: invokevirtual setApertureValue : (Ljava/lang/String;)V
        //   1238: aload_0
        //   1239: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1242: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1245: invokevirtual isConnected : ()Z
        //   1248: ifeq -> 112
        //   1251: aload_0
        //   1252: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1255: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1258: return
        //   1259: aload_0
        //   1260: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1263: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1266: aload_1
        //   1267: getfield obj : Ljava/lang/Object;
        //   1270: checkcast java/lang/String
        //   1273: invokevirtual setEVValue : (Ljava/lang/String;)V
        //   1276: aload_0
        //   1277: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1280: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1283: invokevirtual isConnected : ()Z
        //   1286: ifeq -> 112
        //   1289: aload_0
        //   1290: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1293: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1296: return
        //   1297: aload_1
        //   1298: getfield obj : Ljava/lang/Object;
        //   1301: checkcast java/lang/String
        //   1304: ldc 'NULL'
        //   1306: invokevirtual equals : (Ljava/lang/Object;)Z
        //   1309: ifne -> 112
        //   1312: aload_1
        //   1313: getfield obj : Ljava/lang/Object;
        //   1316: checkcast java/lang/String
        //   1319: ldc ''
        //   1321: invokevirtual equals : (Ljava/lang/Object;)Z
        //   1324: ifne -> 1344
        //   1327: aload_0
        //   1328: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1331: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1334: aload_1
        //   1335: getfield obj : Ljava/lang/Object;
        //   1338: checkcast java/lang/String
        //   1341: invokevirtual setAvailShots : (Ljava/lang/String;)V
        //   1344: aload_0
        //   1345: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1348: invokestatic access$38 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)I
        //   1351: iconst_5
        //   1352: if_icmpne -> 1375
        //   1355: aload_0
        //   1356: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1359: invokestatic access$39 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   1362: ifeq -> 1375
        //   1365: aload_0
        //   1366: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1369: invokestatic access$40 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   1372: ifeq -> 1492
        //   1375: aload_0
        //   1376: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1379: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1382: invokevirtual getDriveValue : ()Ljava/lang/String;
        //   1385: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   1388: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   1391: ldc_w 'CONTINUOUS'
        //   1394: invokevirtual equals : (Ljava/lang/Object;)Z
        //   1397: ifeq -> 1431
        //   1400: aload_0
        //   1401: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1404: invokestatic access$38 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)I
        //   1407: iconst_5
        //   1408: if_icmpne -> 1431
        //   1411: aload_0
        //   1412: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1415: bipush #6
        //   1417: invokestatic access$41 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   1420: aload_0
        //   1421: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1424: bipush #31
        //   1426: ldc ''
        //   1428: invokevirtual doAction : (ILjava/lang/String;)V
        //   1431: aload_0
        //   1432: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1435: iconst_1
        //   1436: invokestatic access$42 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   1439: bipush #111
        //   1441: invokestatic access$28 : (I)V
        //   1444: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
        //   1447: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
        //   1450: dup
        //   1451: aload_0
        //   1452: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1455: invokestatic access$29 : ()I
        //   1458: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   1461: invokevirtual post : (Ljava/lang/Runnable;)Z
        //   1464: pop
        //   1465: iconst_0
        //   1466: invokestatic access$43 : (Z)V
        //   1469: aload_0
        //   1470: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1473: iconst_0
        //   1474: invokestatic access$44 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
        //   1477: aload_0
        //   1478: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1481: iconst_0
        //   1482: invokestatic access$41 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   1485: aload_0
        //   1486: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1489: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1492: aload_0
        //   1493: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1496: invokestatic access$40 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   1499: ifeq -> 1513
        //   1502: aload_0
        //   1503: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1506: sipush #1009
        //   1509: invokevirtual showDialog : (I)V
        //   1512: return
        //   1513: aload_0
        //   1514: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1517: invokestatic access$39 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
        //   1520: ifne -> 112
        //   1523: aload_0
        //   1524: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1527: iconst_1
        //   1528: invokestatic access$45 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   1531: return
        //   1532: aload_1
        //   1533: getfield obj : Ljava/lang/Object;
        //   1536: checkcast java/lang/String
        //   1539: ldc 'NULL'
        //   1541: invokevirtual equals : (Ljava/lang/Object;)Z
        //   1544: ifne -> 112
        //   1547: aload_0
        //   1548: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1551: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1554: aload_1
        //   1555: getfield obj : Ljava/lang/Object;
        //   1558: checkcast java/lang/String
        //   1561: invokevirtual setAFShotResult : (Ljava/lang/String;)V
        //   1564: aload_0
        //   1565: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1568: invokestatic access$15 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)I
        //   1571: ifne -> 1595
        //   1574: aload_0
        //   1575: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1578: invokestatic access$46 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
        //   1581: aload_0
        //   1582: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1585: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1588: invokevirtual getAFShotResult : ()Ljava/lang/String;
        //   1591: invokevirtual downloadImage : (Ljava/lang/String;)V
        //   1594: return
        //   1595: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$ShotPrepareTask
        //   1598: dup
        //   1599: aload_0
        //   1600: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1603: aconst_null
        //   1604: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$ShotPrepareTask;)V
        //   1607: iconst_0
        //   1608: anewarray java/lang/String
        //   1611: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
        //   1614: pop
        //   1615: return
        //   1616: aload_1
        //   1617: getfield obj : Ljava/lang/Object;
        //   1620: checkcast java/lang/String
        //   1623: ldc 'NULL'
        //   1625: invokevirtual equals : (Ljava/lang/Object;)Z
        //   1628: ifne -> 112
        //   1631: aload_0
        //   1632: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1635: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1638: aload_1
        //   1639: getfield obj : Ljava/lang/Object;
        //   1642: checkcast java/lang/String
        //   1645: invokevirtual setDefaultZoom : (Ljava/lang/String;)V
        //   1648: aload_0
        //   1649: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1652: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1655: invokevirtual isConnected : ()Z
        //   1658: ifeq -> 112
        //   1661: aload_0
        //   1662: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1665: invokestatic access$47 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1668: return
        //   1669: aload_1
        //   1670: getfield obj : Ljava/lang/Object;
        //   1673: checkcast java/lang/String
        //   1676: ldc 'NULL'
        //   1678: invokevirtual equals : (Ljava/lang/Object;)Z
        //   1681: ifne -> 112
        //   1684: aload_0
        //   1685: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1688: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1691: invokevirtual getApertureMenuItems : ()Ljava/util/ArrayList;
        //   1694: invokevirtual clear : ()V
        //   1697: aload_1
        //   1698: getfield obj : Ljava/lang/Object;
        //   1701: checkcast java/lang/String
        //   1704: ldc_w ','
        //   1707: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
        //   1710: astore_1
        //   1711: iconst_0
        //   1712: istore_2
        //   1713: iload_2
        //   1714: aload_1
        //   1715: arraylength
        //   1716: if_icmplt -> 1727
        //   1719: aload_0
        //   1720: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1723: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1726: return
        //   1727: aload_0
        //   1728: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1731: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1734: invokevirtual getApertureMenuItems : ()Ljava/util/ArrayList;
        //   1737: aload_1
        //   1738: iload_2
        //   1739: aaload
        //   1740: invokevirtual add : (Ljava/lang/Object;)Z
        //   1743: pop
        //   1744: iload_2
        //   1745: iconst_1
        //   1746: iadd
        //   1747: istore_2
        //   1748: goto -> 1713
        //   1751: aload_1
        //   1752: getfield obj : Ljava/lang/Object;
        //   1755: checkcast java/lang/String
        //   1758: ldc 'NULL'
        //   1760: invokevirtual equals : (Ljava/lang/Object;)Z
        //   1763: ifne -> 112
        //   1766: aload_0
        //   1767: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1770: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1773: invokevirtual getShutterSpeedMenuItems : ()Ljava/util/ArrayList;
        //   1776: invokevirtual clear : ()V
        //   1779: aload_1
        //   1780: getfield obj : Ljava/lang/Object;
        //   1783: checkcast java/lang/String
        //   1786: ldc_w ','
        //   1789: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
        //   1792: astore_1
        //   1793: iconst_0
        //   1794: istore_2
        //   1795: iload_2
        //   1796: aload_1
        //   1797: arraylength
        //   1798: if_icmplt -> 1809
        //   1801: aload_0
        //   1802: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1805: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
        //   1808: return
        //   1809: aload_0
        //   1810: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1813: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1816: invokevirtual getShutterSpeedMenuItems : ()Ljava/util/ArrayList;
        //   1819: aload_1
        //   1820: iload_2
        //   1821: aaload
        //   1822: invokevirtual add : (Ljava/lang/Object;)Z
        //   1825: pop
        //   1826: iload_2
        //   1827: iconst_1
        //   1828: iadd
        //   1829: istore_2
        //   1830: goto -> 1795
        //   1833: aload_1
        //   1834: getfield obj : Ljava/lang/Object;
        //   1837: checkcast java/lang/String
        //   1840: ldc 'NULL'
        //   1842: invokevirtual equals : (Ljava/lang/Object;)Z
        //   1845: ifne -> 112
        //   1848: aload_0
        //   1849: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1852: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1855: aload_1
        //   1856: getfield obj : Ljava/lang/Object;
        //   1859: checkcast java/lang/String
        //   1862: invokevirtual setMovieRecordTime : (Ljava/lang/String;)V
        //   1865: aload_0
        //   1866: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1869: invokestatic access$48 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/TextView;
        //   1872: aload_0
        //   1873: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1876: aload_1
        //   1877: getfield obj : Ljava/lang/Object;
        //   1880: checkcast java/lang/String
        //   1883: invokestatic access$49 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Ljava/lang/String;)Ljava/lang/String;
        //   1886: invokevirtual setText : (Ljava/lang/CharSequence;)V
        //   1889: aload_1
        //   1890: getfield obj : Ljava/lang/Object;
        //   1893: checkcast java/lang/String
        //   1896: aload_0
        //   1897: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1900: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1903: invokevirtual getRemainRecTimeValue : ()Ljava/lang/String;
        //   1906: invokevirtual equals : (Ljava/lang/Object;)Z
        //   1909: ifeq -> 1924
        //   1912: aload_0
        //   1913: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1916: bipush #36
        //   1918: ldc ''
        //   1920: invokevirtual doAction : (ILjava/lang/String;)V
        //   1923: return
        //   1924: aload_0
        //   1925: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1928: invokestatic access$50 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/common/Timer;
        //   1931: ifnonnull -> 112
        //   1934: aload_0
        //   1935: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1938: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/common/Timer
        //   1941: dup
        //   1942: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.msgHandler : Landroid/os/Handler;
        //   1945: sipush #139
        //   1948: bipush #100
        //   1950: invokespecial <init> : (Landroid/os/Handler;II)V
        //   1953: invokestatic access$51 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/common/Timer;)V
        //   1956: aload_0
        //   1957: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1960: invokestatic access$50 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/common/Timer;
        //   1963: invokevirtual start : ()V
        //   1966: return
        //   1967: aload_1
        //   1968: getfield obj : Ljava/lang/Object;
        //   1971: checkcast java/lang/String
        //   1974: ldc 'NULL'
        //   1976: invokevirtual equals : (Ljava/lang/Object;)Z
        //   1979: ifne -> 112
        //   1982: aload_0
        //   1983: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   1986: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   1989: aload_1
        //   1990: getfield obj : Ljava/lang/Object;
        //   1993: checkcast java/lang/String
        //   1996: invokevirtual setRemainRecTimeValue : (Ljava/lang/String;)V
        //   1999: return
        //   2000: aload_1
        //   2001: getfield obj : Ljava/lang/Object;
        //   2004: checkcast java/lang/String
        //   2007: ldc 'NULL'
        //   2009: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2012: ifne -> 112
        //   2015: aload_0
        //   2016: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2019: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   2022: aload_1
        //   2023: getfield obj : Ljava/lang/Object;
        //   2026: checkcast java/lang/String
        //   2029: invokevirtual setFlashStrobeStatus : (Ljava/lang/String;)V
        //   2032: aload_0
        //   2033: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2036: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   2039: invokevirtual isConnected : ()Z
        //   2042: ifeq -> 112
        //   2045: aload_0
        //   2046: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2049: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   2052: invokevirtual getVersionPrefix : ()Ljava/lang/String;
        //   2055: aload_0
        //   2056: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2059: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   2062: invokevirtual getVersion : ()Ljava/lang/String;
        //   2065: invokestatic isSupportedSmartPanel : (Ljava/lang/String;Ljava/lang/String;)Z
        //   2068: ifeq -> 2095
        //   2071: aload_0
        //   2072: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2075: aload_0
        //   2076: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2079: invokestatic access$30 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
        //   2082: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   2085: aload_0
        //   2086: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2089: ldc 2131558680
        //   2091: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
        //   2094: return
        //   2095: aload_0
        //   2096: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2099: invokestatic access$31 : ()Landroid/widget/ImageButton;
        //   2102: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
        //   2105: goto -> 2085
        //   2108: aload_1
        //   2109: getfield obj : Ljava/lang/Object;
        //   2112: checkcast java/lang/String
        //   2115: ldc_w 'TransitionToML'
        //   2118: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2121: ifeq -> 112
        //   2124: aload_0
        //   2125: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2128: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
        //   2131: invokevirtual getCardStatusValue : ()Ljava/lang/String;
        //   2134: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
        //   2137: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
        //   2140: ldc_w 'EXTERNAL'
        //   2143: invokevirtual equals : (Ljava/lang/Object;)Z
        //   2146: ifeq -> 112
        //   2149: aload_0
        //   2150: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2153: invokestatic access$52 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/os/Handler;
        //   2156: iconst_5
        //   2157: invokevirtual removeMessages : (I)V
        //   2160: new android/content/Intent
        //   2163: dup
        //   2164: aload_0
        //   2165: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2168: ldc_w com/samsungimaging/connectionmanager/app/pullservice/demo/ml/FileSharing
        //   2171: invokespecial <init> : (Landroid/content/Context;Ljava/lang/Class;)V
        //   2174: astore_1
        //   2175: aload_0
        //   2176: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2179: aload_1
        //   2180: invokevirtual startActivity : (Landroid/content/Intent;)V
        //   2183: aload_0
        //   2184: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
        //   2187: invokevirtual finish : ()V
        //   2190: return
      }
    };
  
  private ExToast mExToast = ExToast.getInstance();
  
  private int mFrameHeight = 0;
  
  private int mFrameWidth = 0;
  
  private Handler mHandleTimer = null;
  
  private ImageDownloader mImageDownloader = null;
  
  private AdapterView.OnItemClickListener mMainOptionMenuItemClickListener = new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
        DSCResolution dSCResolution;
        String str;
        LiveShutter.this.setTimer(7);
        switch (LiveShutter.this.getCurrentMainOptionMenuId()) {
          default:
            return;
          case 1:
            LiveShutter.this.doAction(17, LiveShutter.this.mDeviceController.getFlashMenuItems().get(param1Int));
            LiveShutter.this.buttondisplay(true);
            return;
          case 2:
            if (((String)LiveShutter.this.mDeviceController.getLedTimeMenuItems().get(param1Int)).equals(LiveShutter.this.getString(2131361997))) {
              LiveShutter.this.doAction(16, "0");
            } else if (((String)LiveShutter.this.mDeviceController.getLedTimeMenuItems().get(param1Int)).equals(LiveShutter.this.getString(2131361993))) {
              LiveShutter.this.doAction(16, "2");
            } else if (((String)LiveShutter.this.mDeviceController.getLedTimeMenuItems().get(param1Int)).equals(LiveShutter.this.getString(2131361994))) {
              LiveShutter.this.doAction(16, "5");
            } else if (((String)LiveShutter.this.mDeviceController.getLedTimeMenuItems().get(param1Int)).equals(LiveShutter.this.getString(2131361996))) {
              LiveShutter.this.doAction(16, "10");
            } else if (((String)LiveShutter.this.mDeviceController.getLedTimeMenuItems().get(param1Int)).equals(LiveShutter.this.getString(2131361995))) {
              LiveShutter.this.doAction(16, "Double");
            } 
            LiveShutter.this.setImageResource(LiveShutter.rvf_timer_button);
            LiveShutter.this.buttondisplay(true);
            return;
          case 3:
            LiveShutter.this.nPhotoSizeSelect = param1Int;
            dSCResolution = LiveShutter.this.mDeviceController.getResolutionMenuItems().get(LiveShutter.this.nPhotoSizeSelect);
            str = String.format("%dx%d", new Object[] { Integer.valueOf(dSCResolution.getWidth()), Integer.valueOf(dSCResolution.getHeight()) });
            LiveShutter.this.doAction(2, str);
            LiveShutter.this.setImageResource(LiveShutter.rvf_photosize_button);
            LiveShutter.this.buttondisplay(true);
            return;
          case 4:
            LiveShutter.this.nCameraSaveSelect = param1Int;
            if (LiveShutter.this.nCameraSaveSelect == 0) {
              if (RVFFunctionManager.getSaveGuideDialogType(LiveShutter.this.mDeviceController.getVersionPrefix(), LiveShutter.this.mDeviceController.getVersion(), LiveShutter.this.connectedSsid) != 0)
                LiveShutter.this.showDialog(1017); 
              if (CommonUtils.isMemoryFull()) {
                LiveShutter.this.mExToast.show(7);
                LiveShutter.this.setShutterButtonEnabled(false);
              } 
              LiveShutter.this.mDeviceController.setFileSaveValue("");
            } else {
              LiveShutter.this.setShutterButtonEnabled(true);
            } 
            LiveShutter.this.mDeviceController.setFileSaveValue(LiveShutter.this.mDeviceController.getFileSaveMenuItems().get(LiveShutter.this.nCameraSaveSelect));
            LiveShutter.this.setImageResource(LiveShutter.this.rvf_indicator_storage);
            LiveShutter.this.setImageResource(LiveShutter.rvf_camerasave_button);
            LiveShutter.this.updateImageOfGallaryButton();
            LiveShutter.this.setTimer(2);
            LiveShutter.this.mMainOptionMenuListAdapter.notifyDataSetChanged();
            return;
          case 5:
            break;
        } 
        switch (param1Int) {
          default:
            return;
          case 0:
            LiveShutter.this.showSubOptionMenu(4);
            return;
          case 1:
            break;
        } 
        LiveShutter.this.showSubOptionMenu(6);
      }
    };
  
  private ArrayList<Menu> mMainOptionMenuList = new ArrayList<Menu>();
  
  private MainOptionMenuListAdapter mMainOptionMenuListAdapter;
  
  private int mMarginLeft = -1;
  
  private int mMarginTop = -1;
  
  private int mOrientationWhenCheckMargin = 1;
  
  private int mPhoneCamType = 1;
  
  private int mShotState = 0;
  
  private int mShotStateEx = 0;
  
  private CustomDialogListMenu mSmartPanelCustomDialogList;
  
  private int mSmartPanelCustomDialogListMenuId;
  
  private AdapterView.OnItemClickListener mSmartPanelCustomDialogListMenuItemClickListener = new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
        LiveShutter.this.mSmartPanelCustomDialogList.setEnabled(false);
        if (((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).isSelected()) {
          LiveShutter.this.setTimer(9);
          LiveShutter.this.actionEnd();
          return;
        } 
        int i = 0;
        while (true) {
          if (i >= LiveShutter.this.mSmartPanelCustomDialogListMenuItems.size()) {
            LiveShutter.this.mSmartPanelCustomDialogList.notifyDataSetChanged();
            switch (LiveShutter.this.getSmartPanelCustomDialogListMenuId()) {
              default:
                return;
              case 2:
                LiveShutter.this.nPhotoSizeSelect = param1Int;
                if (Integer.parseInt(LiveShutter.this.mDeviceController.getDefaultResolutionIndex()) != param1Int) {
                  LiveShutter.this.doAction(LiveShutter.this.getSmartPanelCustomDialogListMenuId(), ((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName());
                  return;
                } 
                return;
              case 25:
                if (!LiveShutter.this.mDeviceController.getWBValue().equals(((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName())) {
                  LiveShutter.this.doAction(LiveShutter.this.getSmartPanelCustomDialogListMenuId(), ((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName());
                  return;
                } 
                return;
              case 26:
                if (!LiveShutter.this.mDeviceController.getMeteringValue().equals(((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName())) {
                  LiveShutter.this.doAction(LiveShutter.this.getSmartPanelCustomDialogListMenuId(), ((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName());
                  return;
                } 
                return;
              case 33:
                if (!LiveShutter.this.mDeviceController.getQualityValue().equals(((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName())) {
                  if (FunctionManager.isNotSupportedRawQualityAndContinuousShotAtTheSameTime(LiveShutter.this.connectedSsid) && ((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName().toUpperCase(Locale.ENGLISH).contains("RAW") && LiveShutter.this.mDeviceController.getDriveValue().toUpperCase(Locale.ENGLISH).equals("CONTINUOUS")) {
                    if (LiveShutter.this.mConfirmDialog == null) {
                      Trace.d(LiveShutter.this.TAG, "mConfirmDialog is null");
                      Bundle bundle = new Bundle();
                      bundle.putString("DIALOG_MESSAGE_KEY", LiveShutter.this.getString(2131362072));
                      LiveShutter.this.showDialog(2000, bundle);
                    } else {
                      Trace.d(LiveShutter.this.TAG, "mConfirmDialog is not null");
                      LiveShutter.this.mConfirmDialog.setMessage(LiveShutter.this.getString(2131362072));
                      LiveShutter.this.mConfirmDialog.show();
                    } 
                    LiveShutter.this.setSmartPanelCustomDialogListMenuId(0);
                    LiveShutter.this.mSmartPanelCustomDialogList.hide();
                    return;
                  } 
                  LiveShutter.this.doAction(LiveShutter.this.getSmartPanelCustomDialogListMenuId(), ((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName());
                  return;
                } 
                return;
              case 34:
                if (!LiveShutter.this.mDeviceController.getMovieResolutionValue().equals(((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName())) {
                  LiveShutter.this.doAction(LiveShutter.this.getSmartPanelCustomDialogListMenuId(), ((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName());
                  return;
                } 
                return;
              case 27:
                if (!LiveShutter.this.mDeviceController.getAFModeValue().equals(((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName())) {
                  LiveShutter.this.doAction(LiveShutter.this.getSmartPanelCustomDialogListMenuId(), ((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName());
                  return;
                } 
                return;
              case 29:
                if (!LiveShutter.this.mDeviceController.getDriveValue().equals(((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName())) {
                  if (FunctionManager.isNotSupportedRawQualityAndContinuousShotAtTheSameTime(LiveShutter.this.connectedSsid) && ((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName().toUpperCase(Locale.ENGLISH).equals("CONTINUOUS") && LiveShutter.this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).contains("RAW")) {
                    if (LiveShutter.this.mConfirmDialog == null) {
                      Trace.d(LiveShutter.this.TAG, "mConfirmDialog is null");
                      Bundle bundle = new Bundle();
                      bundle.putString("DIALOG_MESSAGE_KEY", LiveShutter.this.getString(2131362071));
                      LiveShutter.this.showDialog(2000, bundle);
                    } else {
                      Trace.d(LiveShutter.this.TAG, "mConfirmDialog is not null");
                      LiveShutter.this.mConfirmDialog.setMessage(LiveShutter.this.getString(2131362071));
                      LiveShutter.this.mConfirmDialog.show();
                    } 
                    LiveShutter.this.setSmartPanelCustomDialogListMenuId(0);
                    LiveShutter.this.mSmartPanelCustomDialogList.hide();
                    return;
                  } 
                  LiveShutter.this.doAction(LiveShutter.this.getSmartPanelCustomDialogListMenuId(), ((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName());
                  return;
                } 
                return;
              case 17:
                if (LiveShutter.this.mDeviceController.getFlashStrobeStatus().equals("DETACHED")) {
                  LiveShutter.this.mSmartPanelCustomDialogList.setEnabled(true);
                  LiveShutter.this.setSmartPanelCustomDialogListMenuId(0);
                  LiveShutter.this.mSmartPanelCustomDialogList.hide();
                  Bundle bundle = new Bundle();
                  bundle.putString("DIALOG_MESSAGE_KEY", LiveShutter.this.getString(2131362028));
                  LiveShutter.this.showDialog(1013, bundle);
                  return;
                } 
                if (!LiveShutter.this.mDeviceController.getDefaultFlash().equals(((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName())) {
                  LiveShutter.this.doAction(LiveShutter.this.getSmartPanelCustomDialogListMenuId(), ((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName());
                  return;
                } 
                return;
              case 28:
                if (!LiveShutter.this.mDeviceController.getAFAreaValue().equals(((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName())) {
                  LiveShutter.this.doAction(LiveShutter.this.getSmartPanelCustomDialogListMenuId(), ((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName());
                  return;
                } 
                return;
              case 11:
                if (!LiveShutter.this.mDeviceController.getTouchAFValue().equals(((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName())) {
                  LiveShutter.this.doAction(LiveShutter.this.getSmartPanelCustomDialogListMenuId(), ((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName());
                  return;
                } 
                return;
              case 37:
                if (!LiveShutter.this.mDeviceController.getFileSaveValue().equals(((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName())) {
                  LiveShutter.this.doAction(LiveShutter.this.getSmartPanelCustomDialogListMenuId(), ((Menu)LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(param1Int)).getName());
                  LiveShutter.this.nCameraSaveSelect = param1Int;
                  return;
                } 
                return;
              case 18:
                break;
            } 
          } else {
            boolean bool;
            Menu menu = LiveShutter.this.mSmartPanelCustomDialogListMenuItems.get(i);
            if (i == param1Int) {
              bool = true;
            } else {
              bool = false;
            } 
            menu.setSelected(bool);
            i++;
            continue;
          } 
          if (!LiveShutter.this.mDeviceController.getCurrentStreamQuality().equals(LiveShutter.this.mDeviceController.getStreamQualityMenuItems().get(param1Int))) {
            LiveShutter.this.doAction(LiveShutter.this.getSmartPanelCustomDialogListMenuId(), LiveShutter.this.mDeviceController.getStreamQualityMenuItems().get(param1Int));
            return;
          } 
          return;
        } 
      }
    };
  
  private ArrayList<Menu> mSmartPanelCustomDialogListMenuItems;
  
  private CustomDialogWheelMenu mSmartPanelCustomDialogWheel;
  
  private int mSmartPanelCustomDialogWheelMenuId = 0;
  
  private ArrayList<String> mSmartPanelCustomDialogWheelMenuItems;
  
  private OnWheelClickedListener mSmartPanelCustomDialogWheelMenuWheelClickedListener = new OnWheelClickedListener() {
      public void onItemClicked(WheelView param1WheelView, int param1Int) {
        param1WheelView.setCurrentItem(param1Int);
        switch (LiveShutter.this.mSmartPanelCustomDialogWheelMenuId) {
          default:
            LiveShutter.this.mSmartPanelCustomDialogWheel.setValue(LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1WheelView.getCurrentItem()));
            return;
          case 32:
            if (!LiveShutter.this.mDeviceController.getDialModeValue().equals(LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1Int))) {
              LiveShutter.this.mSmartPanelCustomDialogWheel.setCancelable(false);
              LiveShutter.this.doAction(LiveShutter.this.mSmartPanelCustomDialogWheelMenuId, LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1Int));
            } 
          case 21:
            if (!LiveShutter.this.mDeviceController.getShutterSpeedValue().equals(LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1Int)))
              LiveShutter.this.doAction(LiveShutter.this.mSmartPanelCustomDialogWheelMenuId, LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1Int)); 
          case 22:
            if (!LiveShutter.this.mDeviceController.getApertureValue().equals(LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1Int)))
              LiveShutter.this.doAction(LiveShutter.this.mSmartPanelCustomDialogWheelMenuId, LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1Int)); 
          case 23:
            if (!LiveShutter.this.mDeviceController.getEVValue().equals(LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1Int)))
              LiveShutter.this.doAction(LiveShutter.this.mSmartPanelCustomDialogWheelMenuId, LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1Int)); 
          case 24:
            break;
        } 
        if (!LiveShutter.this.mDeviceController.getISOValue().equals(LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1Int)))
          LiveShutter.this.doAction(LiveShutter.this.mSmartPanelCustomDialogWheelMenuId, LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1Int)); 
      }
    };
  
  private OnWheelScrollListener mSmartPanelCustomDialogWheelMenuWheelScrollListener = new OnWheelScrollListener() {
      public void onScrollingFinished(WheelView param1WheelView) {
        if (LiveShutter.this.mSmartPanelCustomDialogWheelMenuId == 32) {
          LiveShutter.this.mHandleTimer.removeMessages(4);
          LiveShutter.rvf_touchAutoFocus.setVisibility(4);
        } 
        switch (LiveShutter.this.mSmartPanelCustomDialogWheelMenuId) {
          default:
            LiveShutter.this.mSmartPanelCustomDialogWheel.setValue(LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1WheelView.getCurrentItem()));
            return;
          case 32:
            if (!LiveShutter.this.mDeviceController.getDialModeValue().equals(LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1WheelView.getCurrentItem()))) {
              LiveShutter.this.mSmartPanelCustomDialogWheel.setCancelable(false);
              LiveShutter.this.doAction(LiveShutter.this.mSmartPanelCustomDialogWheelMenuId, LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1WheelView.getCurrentItem()));
            } 
          case 21:
            if (!LiveShutter.this.mDeviceController.getShutterSpeedValue().equals(LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1WheelView.getCurrentItem())))
              LiveShutter.this.doAction(LiveShutter.this.mSmartPanelCustomDialogWheelMenuId, LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1WheelView.getCurrentItem())); 
          case 22:
            if (!LiveShutter.this.mDeviceController.getApertureValue().equals(LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1WheelView.getCurrentItem())))
              LiveShutter.this.doAction(LiveShutter.this.mSmartPanelCustomDialogWheelMenuId, LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1WheelView.getCurrentItem())); 
          case 23:
            if (!LiveShutter.this.mDeviceController.getEVValue().equals(LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1WheelView.getCurrentItem())))
              LiveShutter.this.doAction(LiveShutter.this.mSmartPanelCustomDialogWheelMenuId, LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1WheelView.getCurrentItem())); 
          case 24:
            break;
        } 
        if (!LiveShutter.this.mDeviceController.getISOValue().equals(LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1WheelView.getCurrentItem())))
          LiveShutter.this.doAction(LiveShutter.this.mSmartPanelCustomDialogWheelMenuId, LiveShutter.this.mSmartPanelCustomDialogWheelMenuItems.get(param1WheelView.getCurrentItem())); 
      }
      
      public void onScrollingStarted(WheelView param1WheelView) {}
    };
  
  private int mSmartPanelVisibility = 8;
  
  private AdapterView.OnItemClickListener mSubOptionMenuItemClickListener = new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
        LiveShutter.this.setTimer(7);
        switch (LiveShutter.this.getCurrentSubOptionMenuId()) {
          default:
            return;
          case 4:
            LiveShutter.this.nCameraSaveSelect = param1Int;
            if (LiveShutter.this.nCameraSaveSelect == 0 && RVFFunctionManager.getSaveGuideDialogType(LiveShutter.this.mDeviceController.getVersionPrefix(), LiveShutter.this.mDeviceController.getVersion(), LiveShutter.this.connectedSsid) != 0)
              LiveShutter.this.showDialog(1017); 
            LiveShutter.this.mDeviceController.setFileSaveValue(LiveShutter.this.mDeviceController.getFileSaveMenuItems().get(LiveShutter.this.nCameraSaveSelect));
            LiveShutter.this.setImageResource(LiveShutter.this.rvf_indicator_storage);
            LiveShutter.this.setImageResource(LiveShutter.rvf_camerasave_button);
            LiveShutter.this.updateImageOfGallaryButton();
            LiveShutter.this.setTimer(3);
            LiveShutter.this.mMainOptionMenuListAdapter.notifyDataSetChanged();
            LiveShutter.this.mSubOptionMenuListAdapter.notifyDataSetChanged();
            return;
          case 6:
            break;
        } 
        switch (param1Int) {
          default:
            return;
          case 0:
            if (!LiveShutter.this.mDeviceController.getCurrentStreamQuality().equals("high")) {
              LiveShutter.this.doAction(18, "high");
              return;
            } 
            LiveShutter.this.setTimer(3);
            return;
          case 1:
            break;
        } 
        if (!LiveShutter.this.mDeviceController.getCurrentStreamQuality().equals("low")) {
          LiveShutter.this.doAction(18, "low");
          return;
        } 
        LiveShutter.this.setTimer(3);
      }
    };
  
  private ArrayList<Menu> mSubOptionMenuList = new ArrayList<Menu>();
  
  private SubOptionMenuListAdapter mSubOptionMenuListAdapter;
  
  private Timer mTimer = null;
  
  private int mZoomState = 0;
  
  private int nAFGap = 0;
  
  private int nCameraSaveSelect = 1;
  
  private int nConnectCount = 0;
  
  int nCurShowDlg = -1;
  
  private int nPhotoSizeSelect = 0;
  
  private int nSaveCount = 0;
  
  private int nTimerCount = 0;
  
  private int nZoomGap = 0;
  
  Dialog notice = null;
  
  private CheckBox noticecheck;
  
  WindowManager.LayoutParams params;
  
  private PopupWindow popupWindow;
  
  private int positionX = 50;
  
  private int positionY = 50;
  
  private ProgressDialog pro = null;
  
  private ProgressDialog proap = null;
  
  private DialogInterface.OnCancelListener progDiaCancelListener = new DialogInterface.OnCancelListener() {
      private DialogInterface.OnCancelListener progDiaCancelListener = new DialogInterface.OnCancelListener() {
          public void onCancel(DialogInterface param2DialogInterface) {
            Trace.d((LiveShutter.null.access$0(LiveShutter.null.this)).TAG, "progDiaCancelListener1");
            LiveShutter.null.access$0(LiveShutter.null.this).showDialog(1011);
            LiveShutter.null.access$0(LiveShutter.null.this).holderremove();
            LiveShutter.null.access$0(LiveShutter.null.this).appclose();
          }
        };
      
      public void onCancel(DialogInterface param1DialogInterface) {
        Trace.d(LiveShutter.this.TAG, "progDiaCancelListener2");
        LiveShutter.this.showDialog(1011);
        LiveShutter.this.holderremove();
        LiveShutter.this.appclose();
      }
    };
  
  private Timer recordTimer = null;
  
  private ImageView rvf_af_extend_leftdown;
  
  private ImageView rvf_af_extend_leftup;
  
  private ImageView rvf_af_extend_rightdown;
  
  private ImageView rvf_af_extend_rightup;
  
  private ImageButton rvf_camcorder_button;
  
  private ImageView rvf_camera_btn_af;
  
  private RelativeLayout rvf_gallery;
  
  private ImageButton rvf_indicator_af_area;
  
  private LinearLayout rvf_indicator_bar;
  
  private ImageButton rvf_indicator_drive;
  
  private ImageButton rvf_indicator_flash;
  
  private ImageButton rvf_indicator_metering;
  
  private ImageButton rvf_indicator_movie_size;
  
  private ImageButton rvf_indicator_photo_size;
  
  private ImageButton rvf_indicator_quality;
  
  private ImageButton rvf_indicator_storage;
  
  private ImageButton rvf_indicator_touch_af;
  
  private ImageButton rvf_indicator_wb;
  
  private ListView rvf_list;
  
  private RelativeLayout rvf_live_shutter;
  
  private ImageView rvf_memory_full;
  
  private TextView rvf_menuTitle;
  
  private ImageView rvf_menuTop1;
  
  private ImageView rvf_menuTop2;
  
  private ImageView rvf_menuTop3;
  
  private ImageView rvf_menuTop4;
  
  private RelativeLayout rvf_menu_button;
  
  private Button rvf_mode_button;
  
  private ImageView rvf_mode_icon;
  
  private ImageView rvf_no_item;
  
  private LinearLayout rvf_option;
  
  private ProgressBar rvf_progress;
  
  private ImageView rvf_rec_icon;
  
  private ImageButton rvf_rec_stop_button;
  
  private LinearLayout rvf_rec_time;
  
  private LinearLayout rvf_rec_title;
  
  private TextView rvf_recording_time;
  
  private TextView rvf_remain_time;
  
  private LinearLayout rvf_setting_sub_menu;
  
  private ListView rvf_setting_sub_menu_list;
  
  private TextView rvf_setting_sub_menu_title;
  
  private ImageButton rvf_shutter_button;
  
  private LinearLayout rvf_smart_panel;
  
  private ImageButton rvf_smart_panel_afArea;
  
  private ImageButton rvf_smart_panel_afMode;
  
  private WheelView rvf_smart_panel_aperture;
  
  private ImageButton rvf_smart_panel_drive;
  
  private WheelView rvf_smart_panel_ev;
  
  private ImageButton rvf_smart_panel_flash;
  
  private ImageButton rvf_smart_panel_icon;
  
  private WheelView rvf_smart_panel_iso;
  
  private ImageButton rvf_smart_panel_metering;
  
  private WheelView rvf_smart_panel_mode;
  
  private ImageButton rvf_smart_panel_movieSize;
  
  private ImageButton rvf_smart_panel_photoSize;
  
  private ImageButton rvf_smart_panel_quality;
  
  private ImageButton rvf_smart_panel_save;
  
  private WheelView rvf_smart_panel_shutterSpeed;
  
  private ImageButton rvf_smart_panel_streaming_quality;
  
  private ImageButton rvf_smart_panel_touchAF;
  
  private ImageButton rvf_smart_panel_wb;
  
  private SurfaceView rvf_surface;
  
  private ImageView rvf_thumbnail;
  
  private ImageView rvf_timecount;
  
  private RelativeLayout rvf_timerCountMain;
  
  private ImageView rvf_timerHat;
  
  private TextView rvf_toast_message;
  
  private int[][] screenPositoin = new int[4][];
  
  private int[][] screenSize = new int[4][];
  
  private long shotcanceltime = 0L;
  
  private SoundPool soundPool;
  
  private int soundTimerBeep1;
  
  private int soundTimerBeep2;
  
  private ProgressDialog streampro = null;
  
  private int surfaceViewHeight = 0;
  
  private int surfaceViewWidth = 0;
  
  private TimerTaskRun timetask;
  
  private Toast toast = null;
  
  TextView txtView;
  
  TextView txtView2;
  
  private String versionName = "";
  
  private Vibrator vibe;
  
  private long zoomtime = 0L;
  
  static {
    DSCdevice = null;
    bmImg = null;
    cBmpRotation = 'D';
    bgbitmap = null;
    bgbitmap1 = null;
    thumbnail_bmp = null;
    holder = null;
    isButtonShow = false;
    nDisplayFlag = 0;
    dHandler = new Handler();
    mHandler = new Handler();
    bappclose = false;
    bsaveflag = false;
    bshutter = false;
    codec_init = false;
    bClosing = false;
    bVersionFail = false;
    nShutterDnUp = -1;
    nHolderLongWidth = 0;
    nHolderShortWidth = 0;
    nHolderShortHeight = 0;
    nCanvasY = 0;
    nCanvasX = 0;
    mLatitude = 0.0D;
    mLongitude = 0.0D;
    isShotProcessing = false;
    bNoTelephonyDevice = false;
    mExistStatusBarDeviceForTablet = false;
    configChanged = false;
  }
  
  private void CheckLayout() {
    nDisplayFlag = 122;
    mHandler.post(new XxxThread(nDisplayFlag));
  }
  
  private void CheckVersion() {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    this.gDensity = displayMetrics.density;
    this.gDeviceWidth = displayMetrics.widthPixels;
    this.gDeviceHeight = displayMetrics.heightPixels;
    this.gWidthDips = (int)(this.gDeviceWidth / this.gDensity + 0.5F);
    this.gHeightDips = (int)(this.gDeviceHeight / this.gDensity + 0.5F);
    Trace.d(this.TAG, "density:" + displayMetrics.density + ",wdip:" + this.gWidthDips + ",hdip:" + this.gHeightDips);
    Build.MANUFACTURER.toLowerCase();
    if (2 == (getResources().getConfiguration()).orientation) {
      if (this.gWidthDips < 480 || this.gHeightDips < 320) {
        bVersionFail = true;
        showDialog(1005);
        return;
      } 
    } else if (this.gWidthDips < 320 || this.gHeightDips < 480) {
      bVersionFail = true;
      showDialog(1005);
      return;
    } 
    if (Integer.parseInt(Build.VERSION.SDK) >= 8) {
      bVersionFail = false;
      return;
    } 
    bVersionFail = true;
    showDialog(1005);
  }
  
  private static String CreatFileName() {
    String str = (new SimpleDateFormat("yyyy-MM-dd HH.mm.ss.ms")).format(new Date());
    return String.valueOf(Utils.getDefaultStorage()) + "/" + str + ".jpg";
  }
  
  private void CreateDir() {
    File file = new File(Utils.getDefaultStorage());
    if (!file.isDirectory())
      file.mkdirs(); 
  }
  
  private void Createchanged() {
    Trace.d(this.TAG, "start Createchanged()");
    setContentView();
    initLayout();
    if (this.mZoomState == 1) {
      this.mZoomState = 0;
      this.rvf_progress.setVisibility(8);
      doAction(8, "Tele");
    } else if (this.mZoomState == 2) {
      this.mZoomState = 0;
      this.rvf_progress.setVisibility(8);
      doAction(8, "Wide");
    } else if (this.mShotStateEx == 5) {
      this.mShotStateEx = 6;
      doAction(31, "");
    } 
    if (isShotProcessing) {
      if (nShutterDnUp != 1 && this.rvf_timerCountMain.getVisibility() != 0) {
        resetVisibilityTouchAFFrame();
        (new Thread(new Runnable() {
              public void run() {
                do {
                
                } while (LiveShutter.this.getActionState() == 1);
                Trace.d(LiveShutter.this.TAG, "do action RELEASE_SELF_TIMER 09");
                LiveShutter.this.mShotState = 0;
                LiveShutter.this.doAction(13, "");
              }
            })).start();
      } 
      setButtonEnabled(false);
    } 
    if (this.nCameraSaveSelect == 0 && CommonUtils.isMemoryFull()) {
      this.mExToast.show(7);
      setShutterButtonEnabled(false);
    } 
    holderremove();
    holder = this.rvf_surface.getHolder();
    Trace.d(this.TAG, "Add callback of surface holder");
    holder.addCallback(this);
  }
  
  private void GetLiveStream() {
    Trace.d(this.TAG, "start GetLiveStream()");
    this.isSettedLiveStream = true;
    (new Thread(new Runnable() {
          public void run() {
            LiveShutter.bshutter = false;
            Trace.d(LiveShutter.this.TAG, "MESSAGE : RUN_CODEC_INIT");
            LiveShutter.nDisplayFlag = 48;
            LiveShutter.mHandler.post(new LiveShutter.XxxThread(LiveShutter.nDisplayFlag));
          }
        })).start();
  }
  
  private void LayoutChange(int paramInt) {
    Createchanged();
    setImageResource(rvf_flash_button);
    setImageResource(rvf_timer_button);
    if (getCurrentMainOptionMenuId() != 0)
      showMainOptionMenu(getCurrentMainOptionMenuId()); 
    if (getCurrentSubOptionMenuId() != 0)
      showSubOptionMenu(getCurrentSubOptionMenuId()); 
  }
  
  private void MenuClose(boolean paramBoolean) {
    if (paramBoolean)
      setCurrentMainOptionMenuId(0); 
    Trace.d(this.TAG, "MenuClose  ");
    this.rvf_option.setVisibility(4);
    this.rvf_menuTop1.setVisibility(4);
    this.rvf_menuTop2.setVisibility(4);
    this.rvf_menuTop3.setVisibility(4);
    this.rvf_menuTop4.setVisibility(4);
    this.rvf_shutter_button.setVisibility(0);
    if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0)
      showZoomBar(); 
    setImageResource(rvf_flash_button);
    setImageResource(rvf_timer_button);
    setImageResource(rvf_photosize_button);
    setImageResource(rvf_camerasave_button);
    setImageResource(rvf_cameramore_button);
  }
  
  private boolean SaveImageVGA(String paramString) {
    Trace.d(this.TAG, "start SaveImageVGA");
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(paramString);
      try {
        bmImg.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        bmImg.recycle();
        if (!bClosing && this.nCameraSaveSelect == 0 && CommonUtils.isMemoryFull()) {
          this.mExToast.show(7);
          setShutterButtonEnabled(false);
        } 
        fileOutputStream.close();
        bsaveflag = true;
        return true;
      } catch (NullPointerException nullPointerException) {
        nullPointerException.printStackTrace();
        nDisplayFlag = 5;
        mHandler.post(new XxxThread(nDisplayFlag));
        fileOutputStream.close();
        bsaveflag = true;
        return true;
      } catch (FileNotFoundException null) {
        iOException.printStackTrace();
        Trace.d(this.TAG, "MESSAGE : DISPLAY_STORAGEMSG_1");
        nDisplayFlag = 5;
        mHandler.post(new XxxThread(nDisplayFlag));
        return false;
      } catch (IOException null) {}
      iOException.printStackTrace();
      Trace.d(this.TAG, "MESSAGE : DISPLAY_STORAGEMSG_2");
      nDisplayFlag = 5;
      mHandler.post(new XxxThread(nDisplayFlag));
      return false;
    } catch (FileNotFoundException null) {
    
    } catch (IOException iOException) {
      iOException.printStackTrace();
      Trace.d(this.TAG, "MESSAGE : DISPLAY_STORAGEMSG_2");
      nDisplayFlag = 5;
      mHandler.post(new XxxThread(nDisplayFlag));
      return false;
    } 
    iOException.printStackTrace();
    Trace.d(this.TAG, "MESSAGE : DISPLAY_STORAGEMSG_1");
    nDisplayFlag = 5;
    mHandler.post(new XxxThread(nDisplayFlag));
    return false;
  }
  
  private Dialog SmartPhoneSaveGuideDialog() {
    Trace.d(this.TAG, "Start SmartPhoneSaveGuideDialogForDSC");
    View view = ((LayoutInflater)getSystemService("layout_inflater")).inflate(2130903100, (ViewGroup)findViewById(2131558692));
    boolean bool = true;
    ArrayList<DSCMenuItem> arrayList = this.mDeviceController.getFileSaveMenuItemsDim();
    int i = 0;
    while (true) {
      if (i < arrayList.size()) {
        DSCMenuItem dSCMenuItem = arrayList.get(i);
        if (dSCMenuItem.getName().toUpperCase(Locale.ENGLISH).equals("DRIVE") && dSCMenuItem.getValue().toUpperCase(Locale.ENGLISH).equals("CONTINUOUS")) {
          bool = false;
        } else {
          i++;
          continue;
        } 
      } 
      Trace.d(this.TAG, "bSupportedSaveOnContinuousShot : " + bool);
      if (!bool)
        ((TextView)view.findViewById(2131558693)).setText(2131362026); 
      AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
      builder.setView(view);
      builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface param1DialogInterface, int param1Int) {
              param1DialogInterface.dismiss();
            }
          });
      return (Dialog)builder.create();
    } 
  }
  
  private Dialog SmartPhoneSaveGuideDialogForCSC() {
    Trace.d(this.TAG, "Start SmartPhoneSaveGuideDialogForCSC");
    View view = ((LayoutInflater)getSystemService("layout_inflater")).inflate(2130903102, (ViewGroup)findViewById(2131558692));
    AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
    builder.setView(view);
    builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            param1DialogInterface.dismiss();
          }
        });
    return (Dialog)builder.create();
  }
  
  private Dialog SmartPhoneSaveGuideDialogForDSC() {
    Trace.d(this.TAG, "Start SmartPhoneSaveGuideDialogForDSC");
    View view = ((LayoutInflater)getSystemService("layout_inflater")).inflate(2130903101, (ViewGroup)findViewById(2131558692));
    AlertDialog.Builder builder = new AlertDialog.Builder((Context)this);
    builder.setView(view);
    builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            param1DialogInterface.dismiss();
          }
        });
    return (Dialog)builder.create();
  }
  
  private void ZoomButtondisplay() {
    if (this.rvf_option != null && this.rvf_option.getVisibility() == 0)
      return; 
    if ((!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0) || !this.bConnect) {
      showZoomBar();
    } else {
      hideZoomBar();
    } 
    zoombardisplay();
  }
  
  private void ZoomIn() {
    long l = System.currentTimeMillis();
    setTimer(1);
    buttondisplay(true);
    if (l - this.zoomtime > this.nZoomGap) {
      if (Integer.parseInt(this.mDeviceController.getDefaultZoom()) != Integer.parseInt(this.mDeviceController.getMaxZoom()) && codec_init && !this.bOnZoomProcess) {
        this.btoast = false;
        this.bOnZoomProcess = true;
        Trace.d(this.TAG, "MESSAGE : DISPLAY_ZOOMINBUTTON");
        nDisplayFlag = 26;
        mHandler.postDelayed(new XxxThread(nDisplayFlag), (this.nZoomGap - 2));
        this.zoomtime = l;
        this.mHandleTimer.removeMessages(1);
        this.mZoomState = 1;
        this.rvf_progress.setVisibility(0);
        doAction(3, "");
      } 
      return;
    } 
    buttondisplay(true);
  }
  
  private void ZoomOut() {
    long l = System.currentTimeMillis();
    setTimer(1);
    buttondisplay(true);
    if (l - this.zoomtime > this.nZoomGap) {
      if (Integer.parseInt(this.mDeviceController.getDefaultZoom()) != 0 && codec_init && !this.bOnZoomProcess) {
        this.btoast = false;
        this.bOnZoomProcess = true;
        Trace.d(this.TAG, "MESSAGE : DISPLAY_ZOOMOUTBUTTON");
        nDisplayFlag = 25;
        mHandler.postDelayed(new XxxThread(nDisplayFlag), (this.nZoomGap - 2));
        this.zoomtime = l;
        this.mHandleTimer.removeMessages(1);
        this.mZoomState = 2;
        this.rvf_progress.setVisibility(0);
        doAction(4, "");
      } 
      return;
    } 
    buttondisplay(true);
  }
  
  private void actionEnd() {
    Trace.d(this.TAG, "start actionEnd() nTimerCount : " + this.nTimerCount + " isShotProcessing : " + isShotProcessing);
    this.mHandleTimer.removeMessages(5);
    if (!isShotProcessing) {
      setButtonEnabled(true);
      updateZoomButtonEnabled();
    } 
    setActionState(2);
    if (this.customProgressBar != null)
      this.customProgressBar.hide(); 
  }
  
  private void actionStart() {
    if (this.pro == null && (this.streampro == null || !this.streampro.isShowing())) {
      this.customProgressBar = ProgressDialog.show((Context)this, null, null);
      this.customProgressBar.setContentView(2130903092);
    } 
    setButtonEnabled(false);
  }
  
  private void appClose() {
    Trace.d(this.TAG, "start appClose()");
    this.mHandleTimer.removeMessages(7);
    codec_stop();
    FFmpegJNI.destruct();
    CMService.getInstance().beforefinish(0);
    finish();
  }
  
  private void appclose() {
    Trace.d(this.TAG, "start appclose()");
    if (this.timetask != null)
      this.timetask.cancel(); 
    bClosing = true;
    if (this.bConnect)
      codec_stop(); 
    Trace.d(this.TAG, "-=> appclose DSCdevice=" + DSCdevice);
    Trace.e(this.TAG, "1 ----------- setWifiEnabled(false) -------------");
    if (!bappclose)
      bappclose = true; 
    backap();
    Trace.d(this.TAG, "systemExit() before");
    Trace.d(this.TAG, "systemExit() after");
  }
  
  private void autoFocus1x1() {
    Trace.d(this.TAG, "start autoFocus1x1() bAFFAIL : " + this.bAFFAIL);
    if (this.bAFFAIL) {
      rvf_af3x3_11.setImageResource(2130837574);
    } else {
      rvf_af3x3_11.setImageResource(2130837573);
    } 
    rvf_af3x3_11.setVisibility(0);
  }
  
  private void autoFocus3x3() {
    Trace.d(this.TAG, "start autoFocus3x3() bAFFAIL : " + this.bAFFAIL);
    if (this.bAFFAIL) {
      rvf_af3x3_11.setImageResource(2130837574);
      rvf_af3x3_11.setVisibility(0);
      return;
    } 
    int i = this.AFData.length;
    if (i != 0) {
      int j = 0;
      while (true) {
        if (j < i) {
          if (this.AFData[j].equals("0x0")) {
            rvf_af3x3_00.setImageResource(2130837573);
            rvf_af3x3_00.setVisibility(0);
          } else if (this.AFData[j].equals("0x1")) {
            rvf_af3x3_01.setImageResource(2130837573);
            rvf_af3x3_01.setVisibility(0);
          } else if (this.AFData[j].equals("0x2")) {
            rvf_af3x3_02.setImageResource(2130837573);
            rvf_af3x3_02.setVisibility(0);
          } else if (this.AFData[j].equals("1x0")) {
            rvf_af3x3_10.setImageResource(2130837573);
            rvf_af3x3_10.setVisibility(0);
          } else if (this.AFData[j].equals("1x1")) {
            rvf_af3x3_11.setImageResource(2130837573);
            rvf_af3x3_11.setVisibility(0);
          } else if (this.AFData[j].equals("1x2")) {
            rvf_af3x3_12.setImageResource(2130837573);
            rvf_af3x3_12.setVisibility(0);
          } else if (this.AFData[j].equals("2x0")) {
            rvf_af3x3_20.setImageResource(2130837573);
            rvf_af3x3_20.setVisibility(0);
          } else if (this.AFData[j].equals("2x1")) {
            rvf_af3x3_21.setImageResource(2130837573);
            rvf_af3x3_21.setVisibility(0);
          } else if (this.AFData[j].equals("2x2")) {
            rvf_af3x3_22.setImageResource(2130837573);
            rvf_af3x3_22.setVisibility(0);
          } 
          j++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  private void autoFocus3x5() {
    Trace.d(this.TAG, "start autoFocus3x5() bAFFAIL : " + this.bAFFAIL);
    if (this.bAFFAIL) {
      rvf_af3x5_12.setImageResource(2130837574);
      rvf_af3x5_12.setVisibility(0);
      return;
    } 
    int i = this.AFData.length;
    if (i != 0) {
      int j = 0;
      while (true) {
        if (j < i) {
          if (this.AFData[j].equals("0x0")) {
            rvf_af3x5_00.setImageResource(2130837573);
            rvf_af3x5_00.setVisibility(0);
          } else if (this.AFData[j].equals("0x1")) {
            rvf_af3x5_01.setImageResource(2130837573);
            rvf_af3x5_01.setVisibility(0);
          } else if (this.AFData[j].equals("0x2")) {
            rvf_af3x5_02.setImageResource(2130837573);
            rvf_af3x5_02.setVisibility(0);
          } else if (this.AFData[j].equals("0x3")) {
            rvf_af3x5_03.setImageResource(2130837573);
            rvf_af3x5_03.setVisibility(0);
          } else if (this.AFData[j].equals("0x4")) {
            rvf_af3x5_04.setImageResource(2130837573);
            rvf_af3x5_04.setVisibility(0);
          } else if (this.AFData[j].equals("1x0")) {
            rvf_af3x5_10.setImageResource(2130837573);
            rvf_af3x5_10.setVisibility(0);
          } else if (this.AFData[j].equals("1x1")) {
            rvf_af3x5_11.setImageResource(2130837573);
            rvf_af3x5_11.setVisibility(0);
          } else if (this.AFData[j].equals("1x2")) {
            rvf_af3x5_12.setImageResource(2130837573);
            rvf_af3x5_12.setVisibility(0);
          } else if (this.AFData[j].equals("1x3")) {
            rvf_af3x5_13.setImageResource(2130837573);
            rvf_af3x5_13.setVisibility(0);
          } else if (this.AFData[j].equals("1x4")) {
            rvf_af3x5_14.setImageResource(2130837573);
            rvf_af3x5_14.setVisibility(0);
          } else if (this.AFData[j].equals("2x0")) {
            rvf_af3x5_20.setImageResource(2130837573);
            rvf_af3x5_20.setVisibility(0);
          } else if (this.AFData[j].equals("2x1")) {
            rvf_af3x5_21.setImageResource(2130837573);
            rvf_af3x5_21.setVisibility(0);
          } else if (this.AFData[j].equals("2x2")) {
            rvf_af3x5_22.setImageResource(2130837573);
            rvf_af3x5_22.setVisibility(0);
          } else if (this.AFData[j].equals("2x3")) {
            rvf_af3x5_23.setImageResource(2130837573);
            rvf_af3x5_23.setVisibility(0);
          } else if (this.AFData[j].equals("2x4")) {
            rvf_af3x5_24.setImageResource(2130837573);
            rvf_af3x5_24.setVisibility(0);
          } 
          j++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  private void autoFocus3x7() {
    Trace.d(this.TAG, "start autoFocus3x7() bAFFAIL : " + this.bAFFAIL);
    if (this.bAFFAIL) {
      rvf_af3x7_13.setImageResource(2130837574);
      rvf_af3x7_13.setVisibility(0);
      return;
    } 
    int i = this.AFData.length;
    if (i != 0) {
      int j = 0;
      while (true) {
        if (j < i) {
          if (this.AFData[j].equals("0x0")) {
            rvf_af3x7_00.setImageResource(2130837573);
            rvf_af3x7_00.setVisibility(0);
          } else if (this.AFData[j].equals("0x1")) {
            rvf_af3x7_01.setImageResource(2130837573);
            rvf_af3x7_01.setVisibility(0);
          } else if (this.AFData[j].equals("0x2")) {
            rvf_af3x7_02.setImageResource(2130837573);
            rvf_af3x7_02.setVisibility(0);
          } else if (this.AFData[j].equals("0x3")) {
            rvf_af3x7_03.setImageResource(2130837573);
            rvf_af3x7_03.setVisibility(0);
          } else if (this.AFData[j].equals("0x4")) {
            rvf_af3x7_04.setImageResource(2130837573);
            rvf_af3x7_04.setVisibility(0);
          } else if (this.AFData[j].equals("0x5")) {
            rvf_af3x7_05.setImageResource(2130837573);
            rvf_af3x7_05.setVisibility(0);
          } else if (this.AFData[j].equals("0x6")) {
            rvf_af3x7_06.setImageResource(2130837573);
            rvf_af3x7_06.setVisibility(0);
          } else if (this.AFData[j].equals("1x0")) {
            rvf_af3x7_10.setImageResource(2130837573);
            rvf_af3x7_10.setVisibility(0);
          } else if (this.AFData[j].equals("1x1")) {
            rvf_af3x7_11.setImageResource(2130837573);
            rvf_af3x7_11.setVisibility(0);
          } else if (this.AFData[j].equals("1x2")) {
            rvf_af3x7_12.setImageResource(2130837573);
            rvf_af3x7_12.setVisibility(0);
          } else if (this.AFData[j].equals("1x3")) {
            rvf_af3x7_13.setImageResource(2130837573);
            rvf_af3x7_13.setVisibility(0);
          } else if (this.AFData[j].equals("1x4")) {
            rvf_af3x7_14.setImageResource(2130837573);
            rvf_af3x7_14.setVisibility(0);
          } else if (this.AFData[j].equals("1x5")) {
            rvf_af3x7_15.setImageResource(2130837573);
            rvf_af3x7_15.setVisibility(0);
          } else if (this.AFData[j].equals("1x6")) {
            rvf_af3x7_16.setImageResource(2130837573);
            rvf_af3x7_16.setVisibility(0);
          } else if (this.AFData[j].equals("2x0")) {
            rvf_af3x7_20.setImageResource(2130837573);
            rvf_af3x7_20.setVisibility(0);
          } else if (this.AFData[j].equals("2x1")) {
            rvf_af3x7_21.setImageResource(2130837573);
            rvf_af3x7_21.setVisibility(0);
          } else if (this.AFData[j].equals("2x2")) {
            rvf_af3x7_22.setImageResource(2130837573);
            rvf_af3x7_22.setVisibility(0);
          } else if (this.AFData[j].equals("2x3")) {
            rvf_af3x7_23.setImageResource(2130837573);
            rvf_af3x7_23.setVisibility(0);
          } else if (this.AFData[j].equals("2x4")) {
            rvf_af3x7_24.setImageResource(2130837573);
            rvf_af3x7_24.setVisibility(0);
          } else if (this.AFData[j].equals("2x5")) {
            rvf_af3x7_25.setImageResource(2130837573);
            rvf_af3x7_25.setVisibility(0);
          } else if (this.AFData[j].equals("2x6")) {
            rvf_af3x7_26.setImageResource(2130837573);
            rvf_af3x7_26.setVisibility(0);
          } 
          j++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  private void backap() {
    if (!bVersionFail) {
      Trace.d(this.TAG, "-=> shutterbutton(false)-1 rvf_shutter_button=" + this.rvf_shutter_button);
      if (this.rvf_shutter_button != null)
        try {
          setShutterButtonEnabled(false);
          shutterbutton(false);
          return;
        } catch (Exception exception) {
          return;
        }  
    } 
  }
  
  private void buttondisplay(boolean paramBoolean) {
    Trace.d(this.TAG, "buttondisplay == bDisplay:" + paramBoolean + " isButtonShow : " + isButtonShow);
    if (!isButtonShow)
      paramBoolean = false; 
    if (paramBoolean) {
      if (getPhoneCamType() == 1) {
        rvf_flash_button.setVisibility(0);
        rvf_timer_button.setVisibility(0);
        rvf_photosize_button.setVisibility(0);
        if (RVFFunctionManager.isSupportedMoreButton(this.connectedSsid)) {
          rvf_cameramore_button.setVisibility(0);
          return;
        } 
      } else {
        return;
      } 
      rvf_camerasave_button.setVisibility(0);
      return;
    } 
    if (getPhoneCamType() == 1) {
      rvf_flash_button.setVisibility(8);
      rvf_timer_button.setVisibility(8);
      rvf_photosize_button.setVisibility(8);
      if (RVFFunctionManager.isSupportedMoreButton(this.connectedSsid)) {
        rvf_cameramore_button.setVisibility(8);
        return;
      } 
      rvf_camerasave_button.setVisibility(8);
      return;
    } 
  }
  
  private void checkDevice(Device paramDevice) {
    this.mDeviceList = this.upnpController.getDeviceList();
    int j = this.mDeviceList.size();
    boolean bool = false;
    for (int i = 0;; i++) {
      if (i >= j) {
        i = bool;
      } else if ((new String(this.mDeviceList.getDevice(i).getDeviceType())).contains("MediaServer")) {
        Trace.d(this.TAG, "MESSAGE : DISPLAY_MSGCONNECTING");
        nDisplayFlag = 15;
        mHandler.post(new XxxThread(nDisplayFlag));
        paramDevice = this.mDeviceList.getDevice(i);
        if (paramDevice != null)
          this.mDeviceController.setAction(paramDevice); 
        if (!this.bConnect) {
          i = 1;
          DSCdevice = paramDevice;
        } else {
          continue;
        } 
      } else {
        continue;
      } 
      if (i == 0)
        DSCdevice = null; 
      return;
    } 
  }
  
  private void checkphonecamtype() {
    Trace.d(this.TAG, "checkphonecamtype cameratype=" + this.cameratype + ", orientation=" + orientation);
    if (this.cameratype.equals("ssdp:rotationD") || this.cameratype.equals("ssdp:rotationU")) {
      if (orientation == 2) {
        setPhoneCamType(3);
      } else {
        setPhoneCamType(1);
      } 
      this.cameratype.equals("ssdp:rotationD");
    } else if (this.cameratype.equals("ssdp:rotationL") || this.cameratype.equals("ssdp:rotationR")) {
      if (orientation == 2) {
        setPhoneCamType(4);
      } else {
        setPhoneCamType(2);
      } 
      this.cameratype.equals("ssdp:rotationR");
    } 
    Trace.d(this.TAG, "-=> phonecamtype=" + getPhoneCamType());
    if (bClosing)
      return; 
    if (!this.mDeviceController.getRatioValue().isEmpty())
      if (this.mDeviceController.getRatioOffsetMenuItems().size() > 0) {
        screenConfigChange(2);
      } else {
        if (this.mShotStateEx == 7) {
          int i = 0;
          while (true) {
            if (i < this.mDeviceController.getMovieResolutionMenuItems().size())
              if (((DSCMovieResolution)this.mDeviceController.getMovieResolutionMenuItems().get(i)).getResolution().equals(this.mDeviceController.getMovieResolutionValue())) {
                screenConfigChange(Utils.stringToRatioType(((DSCMovieResolution)this.mDeviceController.getMovieResolutionMenuItems().get(i)).getRatio()));
              } else {
                i++;
                continue;
              }  
            if (this.cameratype.equals("ssdp:rotationD")) {
              Trace.d(this.TAG, "FFmpegJNI.request arg1=" + (getPhoneCamType() - 1) + ", degree=" + Character.MIN_VALUE);
              FFmpegJNI.request(FFmpegJNI.Command.SCREEN_CONFIG_CHANGE, getPhoneCamType() - 1, 0, this.screenPositoin, this.screenSize);
              return;
            } 
            if (this.cameratype.equals("ssdp:rotationR")) {
              Trace.d(this.TAG, "FFmpegJNI.request arg1=" + (getPhoneCamType() - 1) + ", degree=" + 'Ď');
              FFmpegJNI.request(FFmpegJNI.Command.SCREEN_CONFIG_CHANGE, getPhoneCamType() - 1, 270, this.screenPositoin, this.screenSize);
              Trace.d(this.TAG, "FFmpegJNI.request arg1=" + (getPhoneCamType() - 1) + ", degree=" + 'Z');
              FFmpegJNI.request(FFmpegJNI.Command.SCREEN_CONFIG_CHANGE, getPhoneCamType() - 1, 90, this.screenPositoin, this.screenSize);
              return;
            } 
            if (this.cameratype.equals("ssdp:rotationU")) {
              Trace.d(this.TAG, "FFmpegJNI.request arg1=" + (getPhoneCamType() - 1) + ", degree=" + '´');
              FFmpegJNI.request(FFmpegJNI.Command.SCREEN_CONFIG_CHANGE, getPhoneCamType() - 1, 180, this.screenPositoin, this.screenSize);
              return;
            } 
            if (this.cameratype.equals("ssdp:rotationL")) {
              Trace.d(this.TAG, "FFmpegJNI.request arg1=" + (getPhoneCamType() - 1) + ", degree=" + 'Ď');
              FFmpegJNI.request(FFmpegJNI.Command.SCREEN_CONFIG_CHANGE, getPhoneCamType() - 1, 270, this.screenPositoin, this.screenSize);
              return;
            } 
            // Byte code: goto -> 121
          } 
        } 
        screenConfigChange(Utils.stringToRatioType(this.mDeviceController.getRatioValue()));
      }  
    if (this.cameratype.equals("ssdp:rotationD")) {
      Trace.d(this.TAG, "FFmpegJNI.request arg1=" + (getPhoneCamType() - 1) + ", degree=" + Character.MIN_VALUE);
      FFmpegJNI.request(FFmpegJNI.Command.SCREEN_CONFIG_CHANGE, getPhoneCamType() - 1, 0, this.screenPositoin, this.screenSize);
      return;
    } 
    if (this.cameratype.equals("ssdp:rotationR")) {
      Trace.d(this.TAG, "FFmpegJNI.request arg1=" + (getPhoneCamType() - 1) + ", degree=" + 'Ď');
      FFmpegJNI.request(FFmpegJNI.Command.SCREEN_CONFIG_CHANGE, getPhoneCamType() - 1, 270, this.screenPositoin, this.screenSize);
      Trace.d(this.TAG, "FFmpegJNI.request arg1=" + (getPhoneCamType() - 1) + ", degree=" + 'Z');
      FFmpegJNI.request(FFmpegJNI.Command.SCREEN_CONFIG_CHANGE, getPhoneCamType() - 1, 90, this.screenPositoin, this.screenSize);
      return;
    } 
    if (this.cameratype.equals("ssdp:rotationU")) {
      Trace.d(this.TAG, "FFmpegJNI.request arg1=" + (getPhoneCamType() - 1) + ", degree=" + '´');
      FFmpegJNI.request(FFmpegJNI.Command.SCREEN_CONFIG_CHANGE, getPhoneCamType() - 1, 180, this.screenPositoin, this.screenSize);
      return;
    } 
    if (this.cameratype.equals("ssdp:rotationL")) {
      Trace.d(this.TAG, "FFmpegJNI.request arg1=" + (getPhoneCamType() - 1) + ", degree=" + 'Ď');
      FFmpegJNI.request(FFmpegJNI.Command.SCREEN_CONFIG_CHANGE, getPhoneCamType() - 1, 270, this.screenPositoin, this.screenSize);
      return;
    } 
    // Byte code: goto -> 121
  }
  
  private void closedata() {
    Trace.d(this.TAG, "-=> closedata");
    if (!bClosing) {
      if (this.timetask != null)
        this.timetask.cancel(); 
      bClosing = true;
      holderremove();
      if (this.bConnect)
        FFmpegJNI.request(FFmpegJNI.Command.VIDEO_STREAMING_QUIT, "ffplay quit"); 
      this.bConnect = false;
      if (!bappclose) {
        bappclose = true;
        return;
      } 
    } 
  }
  
  private boolean codec_start() {
    Trace.d(this.TAG, "start codec_start() phonecamtype : " + getPhoneCamType());
    Trace.d(this.TAG, "Connection Timeout is : " + connectionTimeout);
    if (!codec_init) {
      codec_init = true;
      String str = getInitialUserInput();
      if (this.mDeviceController.getCurrentStreamUrl().contains(".avi") || this.mDeviceController.getCurrentStreamUrl().contains(".AVI")) {
        str = str.concat("-f avi ");
      } else if (this.mDeviceController.getCurrentStreamUrl().contains(".mp4") || this.mDeviceController.getCurrentStreamUrl().contains(".MP4")) {
        str = str.concat("-f mp4 ");
      } else {
        str = str.concat("-f avi ");
      } 
      FFmpegJNI.request(FFmpegJNI.Command.VIDEO_STREAMING_START, str.concat(this.mDeviceController.getCurrentStreamUrl()));
      try {
        while (true) {
          Trace.d(this.TAG, "==> Connection is : " + FFmpegJNI.mConnectResponse + " Timeout : " + connectionTimeout);
          boolean bool = FFmpegJNI.mConnectResponse;
          if (bool)
            return true; 
          connectionTimeout += 100L;
          if (connectionTimeout > 5000L) {
            onUnsubscribe();
            appClose();
            return false;
          } 
          Thread.sleep(100L);
        } 
      } catch (InterruptedException interruptedException) {
        interruptedException.printStackTrace();
        return false;
      } 
    } 
    return false;
  }
  
  private void codec_stop() {
    Trace.d(this.TAG, "start codec_stop()");
    if (codec_init) {
      codec_init = false;
      FFmpegJNI.request(FFmpegJNI.Command.VIDEO_STREAMING_QUIT, "ffplay quit");
    } 
    FFmpegJNI.mConnectResponse = false;
    connectionTimeout = 0L;
  }
  
  private void connectionFailDialog(String paramString) {
    View view = getLayoutInflater().inflate(2130903110, null);
    Button button = (Button)view.findViewById(2131558721);
    ((TextView)view.findViewById(2131558719)).setText(paramString);
    TextView textView = (TextView)view.findViewById(2131558720);
    textView.setText(textView.getText() + this.connectedSsid);
    this.mConnectionFail = (new AlertDialog.Builder((Context)this)).setView(view).setOnKeyListener(new DialogInterface.OnKeyListener() {
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
  
  private static int convertDipToPx(Context paramContext, int paramInt) {
    float f = (paramContext.getResources().getDisplayMetrics()).density;
    return (int)(paramInt * f + 0.5F);
  }
  
  private static String deg_to_dms(double paramDouble) {
    if (paramDouble < 0.0D)
      return ""; 
    int m = (int)paramDouble;
    paramDouble = (paramDouble - m) * 60.0D;
    int j = (int)paramDouble;
    paramDouble = roundDouble((paramDouble - j) * 60.0D, 2);
    int i = j;
    if (paramDouble == 60.0D)
      i = j + 1; 
    int k = m;
    j = i;
    if (i == 60) {
      k = m + 1;
      j = 0;
    } 
    return String.format("%d/1,%d/1,%.02f/1", new Object[] { Integer.valueOf(k), Integer.valueOf(j), Double.valueOf(paramDouble) });
  }
  
  private boolean fileExist(String paramString) {
    return (new File(paramString)).exists();
  }
  
  private int getActionState() {
    return this.mActionState;
  }
  
  private Dialog getCustomDialog() {
    this.notice = new Dialog((Context)this);
    this.notice.requestWindowFeature(1);
    this.notice.setContentView(2130903088);
    this.notice.setCanceledOnTouchOutside(false);
    this.txtView = (TextView)this.notice.findViewById(2131558529);
    this.layout = (LinearLayout)this.notice.findViewById(2131558528);
    this.layout.setVisibility(0);
    this.txtView.setText(2131361961);
    this.closenotice = (Button)this.notice.findViewById(2131558526);
    this.notice.setOnKeyListener(new DialogInterface.OnKeyListener() {
          public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
            switch (param1Int) {
              default:
                return false;
              case 4:
                break;
            } 
            LiveShutter.this.onUnsubscribe();
            return true;
          }
        });
    this.noticecheck = (CheckBox)this.notice.findViewById(2131558667);
    this.noticecheck.setButtonDrawable(2130838239);
    float f = (getResources().getDisplayMetrics()).density;
    this.noticecheck.setPadding(this.noticecheck.getPaddingLeft() + (int)(10.0F * f + 0.5F), this.noticecheck.getPaddingTop(), this.noticecheck.getPaddingRight(), this.noticecheck.getPaddingBottom());
    Trace.e(this.TAG, "notice is : " + this.noticecheck.isChecked());
    this.noticecheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
            LiveShutter.this.noticecheck.setChecked(param1Boolean);
            Trace.e(LiveShutter.this.TAG, "notice is : " + LiveShutter.this.noticecheck.isChecked());
          }
        });
    this.closenotice.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            LiveShutter.this.notice.dismiss();
            if (LiveShutter.this.noticecheck.isChecked()) {
              Trace.e(LiveShutter.this.TAG, "notice is checked");
              LiveShutter.this.mConfigurationManager.setCheckedStatusOfNotice(true);
            } 
            if (LiveShutter.this.mDeviceController.getCardStatusValue().toUpperCase(Locale.ENGLISH).equals("INTERNAL")) {
              LiveShutter.this.showDialog(2008);
              return;
            } 
            if (FunctionManager.isNotSupportedRawQualityAndContinuousShotAtTheSameTime(LiveShutter.this.connectedSsid) && LiveShutter.this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).contains("RAW") && LiveShutter.this.mDeviceController.getDriveValue().toUpperCase(Locale.ENGLISH).contains("CONTINUOUS")) {
              LiveShutter.this.showDialog(2007);
              return;
            } 
          }
        });
    return this.notice;
  }
  
  private File[] getFileList(String paramString) {
    FilenameFilter filenameFilter = new FilenameFilter() {
        public boolean accept(File param1File, String param1String) {
          return param1String.toLowerCase().endsWith(".jpg");
        }
      };
    return (new File(paramString)).listFiles(filenameFilter);
  }
  
  private String getInitialUserInput() {
    int j = 0;
    int i = 0;
    switch (getPhoneCamType()) {
      default:
        return new String("ffplay -livestreaming -pktqimmediateflush -pictqinitialemptysec 4 -demuxprobesize 512 -an -sync ext -framedrop -infbuf -pictqsize " + '\017' + " -displayresolution " + j + "x" + i + " ");
      case 1:
        j = this.screenSize[0][0];
        i = this.screenSize[0][1];
      case 2:
        j = this.screenSize[1][0];
        i = this.screenSize[1][1];
      case 3:
        j = this.screenSize[2][0];
        i = this.screenSize[2][1];
      case 4:
        break;
    } 
    j = this.screenSize[3][0];
    i = this.screenSize[3][1];
  }
  
  private int getSmartPanelVisibility() {
    return this.mSmartPanelVisibility;
  }
  
  private Bitmap getThumbnail(String paramString) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inSampleSize = 16;
    Bitmap bitmap2 = BitmapFactory.decodeFile(paramString, options);
    Bitmap bitmap1 = bitmap2;
    if (bitmap2 != null)
      bitmap1 = Bitmap.createScaledBitmap(bitmap2, (int)(50.0F * bitmap2.getWidth() / bitmap2.getHeight()), 50, true); 
    return bitmap1;
  }
  
  private String getTimeFormat(String paramString) {
    int i = Integer.parseInt(paramString);
    return String.valueOf(String.valueOf(String.format("%02d", new Object[] { Integer.valueOf(i / 60) })) + ":") + String.format("%02d", new Object[] { Integer.valueOf(i % 60) });
  }
  
  private void hideAFExtend() {
    Trace.d(this.TAG, "start hideAFExtend()");
    rvf_af_extend.setVisibility(8);
  }
  
  private void hideAutoFocusFrame() {
    Trace.d(this.TAG, "start hideAutoFocusFrame()");
    rvf_af3x3_00.setVisibility(8);
    rvf_af3x3_01.setVisibility(8);
    rvf_af3x3_02.setVisibility(8);
    rvf_af3x3_10.setVisibility(8);
    rvf_af3x3_11.setVisibility(8);
    rvf_af3x3_12.setVisibility(8);
    rvf_af3x3_20.setVisibility(8);
    rvf_af3x3_21.setVisibility(8);
    rvf_af3x3_22.setVisibility(8);
    rvf_af3x5_00.setVisibility(8);
    rvf_af3x5_01.setVisibility(8);
    rvf_af3x5_02.setVisibility(8);
    rvf_af3x5_03.setVisibility(8);
    rvf_af3x5_04.setVisibility(8);
    rvf_af3x5_10.setVisibility(8);
    rvf_af3x5_11.setVisibility(8);
    rvf_af3x5_12.setVisibility(8);
    rvf_af3x5_13.setVisibility(8);
    rvf_af3x5_14.setVisibility(8);
    rvf_af3x5_20.setVisibility(8);
    rvf_af3x5_21.setVisibility(8);
    rvf_af3x5_22.setVisibility(8);
    rvf_af3x5_23.setVisibility(8);
    rvf_af3x5_24.setVisibility(8);
    rvf_af3x7_00.setVisibility(8);
    rvf_af3x7_01.setVisibility(8);
    rvf_af3x7_02.setVisibility(8);
    rvf_af3x7_03.setVisibility(8);
    rvf_af3x7_04.setVisibility(8);
    rvf_af3x7_05.setVisibility(8);
    rvf_af3x7_06.setVisibility(8);
    rvf_af3x7_10.setVisibility(8);
    rvf_af3x7_11.setVisibility(8);
    rvf_af3x7_12.setVisibility(8);
    rvf_af3x7_13.setVisibility(8);
    rvf_af3x7_14.setVisibility(8);
    rvf_af3x7_15.setVisibility(8);
    rvf_af3x7_16.setVisibility(8);
    rvf_af3x7_20.setVisibility(8);
    rvf_af3x7_21.setVisibility(8);
    rvf_af3x7_22.setVisibility(8);
    rvf_af3x7_23.setVisibility(8);
    rvf_af3x7_24.setVisibility(8);
    rvf_af3x7_25.setVisibility(8);
    rvf_af3x7_26.setVisibility(8);
  }
  
  private void hideIndicatorBar() {
    this.rvf_indicator_bar.setVisibility(8);
  }
  
  private void hideMainOptionMenu() {
    setCurrentMainOptionMenuId(0);
    this.rvf_option.setVisibility(8);
    this.rvf_menuTop1.setVisibility(8);
    this.rvf_menuTop2.setVisibility(8);
    this.rvf_menuTop3.setVisibility(8);
    this.rvf_menuTop4.setVisibility(8);
    setImageResource(rvf_flash_button);
    setImageResource(rvf_timer_button);
    setImageResource(rvf_photosize_button);
    setImageResource(rvf_camerasave_button);
    setImageResource(rvf_cameramore_button);
    setTimer(1);
    if (!isAvailShot())
      showMemoryFullDialog(1); 
  }
  
  private void hideMemoryFullDialog() {
    Trace.d(this.TAG, "start hideMemoryFullDialog");
    if (RVFFunctionManager.isTouchableZoomSeekBar(this.connectedSsid))
      rvf_zoomSeekBar.setEnabled(true); 
    hidePopupWindow(1);
  }
  
  private void hideOSD() {
    rvf_title.setVisibility(4);
    this.rvf_gallery.setVisibility(8);
    this.rvf_shutter_button.setVisibility(8);
    if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0)
      hideZoomBar(); 
  }
  
  private void hidePopupWindow(int paramInt) {
    Trace.d(this.TAG, "start hidePopupWindow()");
    if (this.mCurPopupWindowId == paramInt && this.popupWindow != null) {
      this.popupWindow.dismiss();
      this.popupWindow = null;
    } 
  }
  
  private void hideProgressBar() {
    Trace.d(this.TAG, "start hideProgressBar()");
    if (this.pro != null) {
      this.pro.dismiss();
      this.pro = null;
    } 
  }
  
  private void hideQuickSettingMenu() {
    Trace.d(this.TAG, "hide QuickSettingMenu");
    showIndicatorBar();
    rvf_QuickSettingMenu.setVisibility(8);
    rvf_open_button.setVisibility(0);
  }
  
  private void hideSubOptionMenu() {
    setCurrentSubOptionMenuId(0);
    this.rvf_setting_sub_menu.setVisibility(8);
  }
  
  private void hideZoomBar() {
    Trace.d(this.TAG, "hideZoomBar()");
    rvf_zoomMain.setVisibility(8);
  }
  
  private void holderremove() {
    if (holder != null) {
      holder.removeCallback(this);
      holder = null;
    } 
  }
  
  private void init() {
    Trace.d(this.TAG, "start init()");
    this.mExToast.register((Context)this, 6, 2131361938, 0);
    this.mExToast.register((Context)this, 7, 2131361827, 0);
    this.mExToast.register((Context)this, 8, 2131361981, 0);
    this.soundPool = new SoundPool(2, 3, 0);
    this.soundTimerBeep1 = this.soundPool.load((Context)this, 2131034112, 1);
    this.soundTimerBeep2 = this.soundPool.load((Context)this, 2131034113, 1);
    if (!this.bConnect && this.timetask == null) {
      this.timetask = new TimerTaskRun(null);
      this.mTimer = new Timer();
      this.mTimer.schedule(this.timetask, 0L, 1000L);
    } 
    if (this.frameCounter == null) {
      this.frameCounter = new FrameCounterTask(null);
      if (this.mTimer == null)
        this.mTimer = new Timer(); 
      this.mTimer.schedule(this.frameCounter, 0L, 1000L);
    } 
    try {
      this.versionName = (getPackageManager().getPackageInfo(getPackageName(), 0)).versionName;
    } catch (android.content.pm.PackageManager.NameNotFoundException nameNotFoundException) {}
    this.upnpController.setEventHandler(this.mEventHandler);
    this.mDeviceController.setHandler(this.mDeviceControlHandler);
    this.mDeviceController.setDefaultFlash(RVFFunctionManager.getDefaultFlash(this.connectedSsid));
    this.mDeviceController.getLedTimeMenuItems().clear();
    this.mDeviceController.getLedTimeMenuItems().add(getString(2131361997));
    this.mDeviceController.getLedTimeMenuItems().add(getString(2131361993));
    if (RVFFunctionManager.isSupported5SecondsTimerShot(this.connectedSsid))
      this.mDeviceController.getLedTimeMenuItems().add(getString(2131361994)); 
    this.mDeviceController.getLedTimeMenuItems().add(getString(2131361996));
    if (RVFFunctionManager.isSupportedDoubleTimerShot(this.connectedSsid))
      this.mDeviceController.getLedTimeMenuItems().add(getString(2131361995)); 
    this.mDeviceController.getFileSaveMenuItems().clear();
    this.mDeviceController.getFileSaveMenuItems().add("Smartphone + Camera");
    this.mDeviceController.getFileSaveMenuItems().add("Camera");
    this.mConfigurationManager = SRVFConfigurationManager.getInstance(getApplicationContext());
    this.mHandleTimer = new Handler() {
        public void handleMessage(Message param1Message) {
          String str;
          LiveShutter liveShutter;
          Trace.d(LiveShutter.this.TAG, "mHandleTimer msg.what : " + param1Message.what);
          switch (param1Message.what) {
            default:
              super.handleMessage(param1Message);
              return;
            case 1:
              if (LiveShutter.this.getPhoneCamType() != 1)
                LiveShutter.this.hideZoomBar(); 
            case 2:
              LiveShutter.this.hideMainOptionMenu();
              LiveShutter.this.hideSubOptionMenu();
              if (LiveShutter.this.getPhoneCamType() == 1) {
                if (!RVFFunctionManager.isUnsupportedZoomableLens(LiveShutter.this.connectedSsid) && Integer.parseInt(LiveShutter.this.mDeviceController.getMaxZoom()) > 0)
                  LiveShutter.this.showZoomBar(); 
                LiveShutter.this.rvf_shutter_button.setVisibility(0);
              } 
            case 3:
              LiveShutter.this.hideSubOptionMenu();
            case 4:
              str = LiveShutter.this.mDeviceController.getAFAreaValue().toUpperCase(Locale.ENGLISH);
              if (str.equals("SELECTION AF")) {
                LiveShutter.rvf_touchAutoFocus.setImageResource(2130837575);
              } else if (str.equals("MULTI AF")) {
                LiveShutter.rvf_touchAutoFocus.setVisibility(4);
              } 
              LiveShutter.this.mShotState = 0;
              Trace.d(LiveShutter.this.TAG, "do action RELEASE_SELF_TIMER 07");
              LiveShutter.this.resetVisibilityTouchAFFrame();
              LiveShutter.this.doAction(13, "");
            case 5:
            case 6:
              if (LiveShutter.this.streampro != null) {
                LiveShutter.this.streampro.dismiss();
                LiveShutter.this.streampro = null;
                LiveShutter.this.removeDialog(1010);
              } 
              LiveShutter.this.closedata();
              if (LiveShutter.this.isDestroyActivity() || LiveShutter.this.isFinishing()) {
                Trace.d(LiveShutter.this.TAG, "LiveShatter Activity is finished or destroyed");
                return;
              } 
              LiveShutter.this.showDialog(2002);
            case 7:
              if (!CMUtil.isTestMode(LiveShutter.Appcontext) && !LiveShutter.this.bpopupflag) {
                LiveShutter.this.bpopupflag = true;
                LiveShutter.this.mExToast.show(6);
                LiveShutter.this.setTimer(8);
              } 
            case 8:
              Trace.d(LiveShutter.this.TAG, "Const.MsgId.RUN_EXIT");
              if (!LiveShutter.this.isFinishing())
                LiveShutter.this.showDialog(1011); 
              LiveShutter.this.onUnsubscribe();
            case 9:
              LiveShutter.this.setSmartPanelCustomDialogListMenuId(0);
              LiveShutter.this.mSmartPanelCustomDialogList.hide();
            case 10:
              if (LiveShutter.this.AfFrameType == 4) {
                LiveShutter.this.hideAFExtend();
              } else if (LiveShutter.this.AfFrameType == 2) {
                LiveShutter.rvf_touchAutoFocus.setVisibility(4);
              } else {
                LiveShutter.this.hideAutoFocusFrame();
              } 
            case 11:
              liveShutter = LiveShutter.this;
              liveShutter.nTimerCount = liveShutter.nTimerCount - 1;
              LiveShutter.this.updateLEDTimeDisplay(LiveShutter.this.nTimerCount);
              if (LiveShutter.this.nTimerCount == 2) {
                LiveShutter.this.soundPool.play(LiveShutter.this.soundTimerBeep2, 1.0F, 1.0F, 0, 0, 1.0F);
                LiveShutter.this.setTimer(11);
              } else if (LiveShutter.this.nTimerCount > 0) {
                LiveShutter.this.soundPool.play(LiveShutter.this.soundTimerBeep1, 1.0F, 1.0F, 0, 0, 1.0F);
                LiveShutter.this.setTimer(11);
              } else {
                LiveShutter.this.rvf_timerCountMain.setVisibility(8);
                if (LiveShutter.bshutter) {
                  LiveShutter.this.shutterbutton(false);
                  LiveShutter.this.setShutterButtonEnabled(false);
                  Trace.d(LiveShutter.this.TAG, "MESSAGE : DISPLAY_STARTPRO");
                  LiveShutter.mHandler.post(new LiveShutter.XxxThread(22));
                } 
              } 
            case 12:
              break;
          } 
          LiveShutter.this.hideMemoryFullDialog();
        }
      };
    Display display = ((WindowManager)getSystemService("window")).getDefaultDisplay();
    this.isOptimusVu = Utils.CheckOptimusView(display.getWidth(), display.getHeight());
    orientation = (getResources().getConfiguration()).orientation;
    if (orientation == 2) {
      setPhoneCamType(3);
      return;
    } 
    setPhoneCamType(1);
  }
  
  private void initLayout() {
    // Byte code:
    //   0: aload_0
    //   1: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   4: ldc_w 'start initLayout()'
    //   7: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   10: aload_0
    //   11: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   14: invokevirtual getDialModeValue : ()Ljava/lang/String;
    //   17: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   20: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   23: ldc_w 'AUTO'
    //   26: invokevirtual equals : (Ljava/lang/Object;)Z
    //   29: ifeq -> 700
    //   32: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_title : Landroid/widget/TextView;
    //   35: ldc_w 'Auto'
    //   38: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   41: aload_0
    //   42: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   45: invokevirtual getVersionPrefix : ()Ljava/lang/String;
    //   48: aload_0
    //   49: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   52: invokevirtual getVersion : ()Ljava/lang/String;
    //   55: invokestatic isSupportedSmartPanel : (Ljava/lang/String;Ljava/lang/String;)Z
    //   58: ifeq -> 984
    //   61: aload_0
    //   62: getfield rvf_menu_button : Landroid/widget/RelativeLayout;
    //   65: bipush #8
    //   67: invokevirtual setVisibility : (I)V
    //   70: aload_0
    //   71: getfield mShotStateEx : I
    //   74: bipush #7
    //   76: if_icmpne -> 836
    //   79: aload_0
    //   80: getfield rvf_smart_panel_icon : Landroid/widget/ImageButton;
    //   83: bipush #8
    //   85: invokevirtual setVisibility : (I)V
    //   88: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_title : Landroid/widget/TextView;
    //   91: iconst_4
    //   92: invokevirtual setVisibility : (I)V
    //   95: aload_0
    //   96: invokespecial hideIndicatorBar : ()V
    //   99: aload_0
    //   100: getfield rvf_gallery : Landroid/widget/RelativeLayout;
    //   103: bipush #8
    //   105: invokevirtual setVisibility : (I)V
    //   108: aload_0
    //   109: getfield rvf_mode_button : Landroid/widget/Button;
    //   112: bipush #8
    //   114: invokevirtual setVisibility : (I)V
    //   117: aload_0
    //   118: getfield rvf_shutter_button : Landroid/widget/ImageButton;
    //   121: bipush #8
    //   123: invokevirtual setVisibility : (I)V
    //   126: aload_0
    //   127: getfield rvf_camcorder_button : Landroid/widget/ImageButton;
    //   130: bipush #8
    //   132: invokevirtual setVisibility : (I)V
    //   135: aload_0
    //   136: invokespecial showZoomBar : ()V
    //   139: aload_0
    //   140: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   143: invokevirtual getDefaultFocusState : ()Ljava/lang/String;
    //   146: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   149: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   152: ldc_w 'MF'
    //   155: invokevirtual equals : (Ljava/lang/Object;)Z
    //   158: ifeq -> 203
    //   161: aload_0
    //   162: aload_0
    //   163: getfield rvf_smart_panel_afArea : Landroid/widget/ImageButton;
    //   166: invokespecial setImageResource : (Landroid/widget/ImageButton;)V
    //   169: aload_0
    //   170: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   173: ldc_w '>>>>>>> 001'
    //   176: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   179: aload_0
    //   180: getfield rvf_smart_panel_afArea : Landroid/widget/ImageButton;
    //   183: iconst_0
    //   184: invokevirtual setEnabled : (Z)V
    //   187: aload_0
    //   188: aload_0
    //   189: getfield rvf_smart_panel_touchAF : Landroid/widget/ImageButton;
    //   192: invokespecial setImageResource : (Landroid/widget/ImageButton;)V
    //   195: aload_0
    //   196: getfield rvf_smart_panel_touchAF : Landroid/widget/ImageButton;
    //   199: iconst_0
    //   200: invokevirtual setEnabled : (Z)V
    //   203: aload_0
    //   204: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   207: invokevirtual getAFAreaValue : ()Ljava/lang/String;
    //   210: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   213: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   216: ldc_w 'MULTI AF'
    //   219: invokevirtual equals : (Ljava/lang/Object;)Z
    //   222: ifeq -> 276
    //   225: aload_0
    //   226: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   229: invokevirtual getDialModeValue : ()Ljava/lang/String;
    //   232: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   235: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   238: ldc_w 'AUTO'
    //   241: invokevirtual equals : (Ljava/lang/Object;)Z
    //   244: ifne -> 276
    //   247: aload_0
    //   248: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   251: invokevirtual getMeteringValue : ()Ljava/lang/String;
    //   254: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   257: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   260: ldc_w 'SPOT'
    //   263: invokevirtual equals : (Ljava/lang/Object;)Z
    //   266: ifeq -> 276
    //   269: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_metering_spot_frame : Landroid/widget/ImageView;
    //   272: iconst_0
    //   273: invokevirtual setVisibility : (I)V
    //   276: aload_0
    //   277: getfield connectedSsid : Ljava/lang/String;
    //   280: invokestatic isTouchableZoomButton : (Ljava/lang/String;)Z
    //   283: ifne -> 300
    //   286: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_zoomin_button : Landroid/widget/ImageButton;
    //   289: iconst_0
    //   290: invokevirtual setEnabled : (Z)V
    //   293: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_zoomout_button : Landroid/widget/ImageButton;
    //   296: iconst_0
    //   297: invokevirtual setEnabled : (Z)V
    //   300: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_zoomSeekBar : Landroid/widget/SeekBar;
    //   303: aload_0
    //   304: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   307: invokevirtual getMaxZoom : ()Ljava/lang/String;
    //   310: invokestatic parseInt : (Ljava/lang/String;)I
    //   313: aload_0
    //   314: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   317: invokevirtual getMinZoom : ()Ljava/lang/String;
    //   320: invokestatic parseInt : (Ljava/lang/String;)I
    //   323: isub
    //   324: invokevirtual setMax : (I)V
    //   327: aload_0
    //   328: invokespecial zoombardisplay : ()V
    //   331: aload_0
    //   332: getfield connectedSsid : Ljava/lang/String;
    //   335: invokestatic isTouchableZoomSeekBar : (Ljava/lang/String;)Z
    //   338: ifeq -> 1053
    //   341: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_zoomSeekBar : Landroid/widget/SeekBar;
    //   344: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$13
    //   347: dup
    //   348: aload_0
    //   349: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
    //   352: invokevirtual setOnSeekBarChangeListener : (Landroid/widget/SeekBar$OnSeekBarChangeListener;)V
    //   355: aload_0
    //   356: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   359: invokevirtual getVersionPrefix : ()Ljava/lang/String;
    //   362: aload_0
    //   363: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   366: invokevirtual getVersion : ()Ljava/lang/String;
    //   369: invokestatic isSupportedSmartPanel : (Ljava/lang/String;Ljava/lang/String;)Z
    //   372: ifeq -> 1144
    //   375: aload_0
    //   376: invokespecial setSmartPanel : ()V
    //   379: aload_0
    //   380: getfield mShotStateEx : I
    //   383: bipush #7
    //   385: if_icmpne -> 1063
    //   388: aload_0
    //   389: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   392: invokevirtual isTouchAFMovie : ()Z
    //   395: ifeq -> 406
    //   398: aload_0
    //   399: getfield rvf_camera_btn_af : Landroid/widget/ImageView;
    //   402: iconst_0
    //   403: invokevirtual setVisibility : (I)V
    //   406: aload_0
    //   407: getfield rvf_rec_stop_button : Landroid/widget/ImageButton;
    //   410: iconst_0
    //   411: invokevirtual setVisibility : (I)V
    //   414: aload_0
    //   415: getfield mShotState : I
    //   418: ifne -> 429
    //   421: aload_0
    //   422: getfield rvf_rec_stop_button : Landroid/widget/ImageButton;
    //   425: iconst_0
    //   426: invokevirtual setEnabled : (Z)V
    //   429: aload_0
    //   430: getfield rvf_rec_title : Landroid/widget/LinearLayout;
    //   433: iconst_0
    //   434: invokevirtual setVisibility : (I)V
    //   437: aload_0
    //   438: getfield rvf_rec_time : Landroid/widget/LinearLayout;
    //   441: iconst_0
    //   442: invokevirtual setVisibility : (I)V
    //   445: aload_0
    //   446: getfield rvf_recording_time : Landroid/widget/TextView;
    //   449: aload_0
    //   450: aload_0
    //   451: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   454: invokevirtual getMovieRecordTime : ()Ljava/lang/String;
    //   457: invokespecial getTimeFormat : (Ljava/lang/String;)Ljava/lang/String;
    //   460: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   463: aload_0
    //   464: getfield rvf_remain_time : Landroid/widget/TextView;
    //   467: aload_0
    //   468: aload_0
    //   469: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   472: invokevirtual getRemainRecTimeValue : ()Ljava/lang/String;
    //   475: invokespecial getTimeFormat : (Ljava/lang/String;)Ljava/lang/String;
    //   478: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   481: aload_0
    //   482: invokespecial setSmartPanel : ()V
    //   485: aload_0
    //   486: aload_0
    //   487: invokespecial getSmartPanelVisibility : ()I
    //   490: invokespecial setSmartPanelVisibility : (I)V
    //   493: aload_0
    //   494: getfield nTimerCount : I
    //   497: ifle -> 595
    //   500: aload_0
    //   501: getfield rvf_timerCountMain : Landroid/widget/RelativeLayout;
    //   504: iconst_0
    //   505: invokevirtual setVisibility : (I)V
    //   508: aload_0
    //   509: getfield nTimerCount : I
    //   512: iconst_2
    //   513: if_icmple -> 1459
    //   516: aload_0
    //   517: getfield rvf_timerHat : Landroid/widget/ImageView;
    //   520: ldc_w 2130837875
    //   523: invokevirtual setImageResource : (I)V
    //   526: aload_0
    //   527: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   530: new java/lang/StringBuilder
    //   533: dup
    //   534: ldc_w 'nTimerCount : '
    //   537: invokespecial <init> : (Ljava/lang/String;)V
    //   540: aload_0
    //   541: getfield nTimerCount : I
    //   544: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   547: invokevirtual toString : ()Ljava/lang/String;
    //   550: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   553: aload_0
    //   554: aload_0
    //   555: getfield nTimerCount : I
    //   558: invokespecial updateLEDTimeDisplay : (I)V
    //   561: aload_0
    //   562: invokespecial updateZoomButtonEnabled : ()V
    //   565: aload_0
    //   566: invokevirtual getPhoneCamType : ()I
    //   569: iconst_1
    //   570: if_icmpeq -> 579
    //   573: aload_0
    //   574: bipush #8
    //   576: invokespecial setVisibilityOSD : (I)V
    //   579: aload_0
    //   580: getfield rvf_timerCountMain : Landroid/widget/RelativeLayout;
    //   583: iconst_0
    //   584: invokevirtual setVisibility : (I)V
    //   587: aload_0
    //   588: aload_0
    //   589: getfield nTimerCount : I
    //   592: invokespecial updateLEDTimeDisplay : (I)V
    //   595: aload_0
    //   596: getfield rvf_list : Landroid/widget/ListView;
    //   599: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$14
    //   602: dup
    //   603: aload_0
    //   604: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
    //   607: invokevirtual setOnScrollListener : (Landroid/widget/AbsListView$OnScrollListener;)V
    //   610: aload_0
    //   611: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$MainOptionMenuListAdapter
    //   614: dup
    //   615: aload_0
    //   616: aload_0
    //   617: ldc_w 2130903099
    //   620: aload_0
    //   621: getfield mMainOptionMenuList : Ljava/util/ArrayList;
    //   624: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/content/Context;ILjava/util/List;)V
    //   627: putfield mMainOptionMenuListAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$MainOptionMenuListAdapter;
    //   630: aload_0
    //   631: getfield rvf_list : Landroid/widget/ListView;
    //   634: aload_0
    //   635: getfield mMainOptionMenuListAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$MainOptionMenuListAdapter;
    //   638: invokevirtual setAdapter : (Landroid/widget/ListAdapter;)V
    //   641: aload_0
    //   642: getfield rvf_setting_sub_menu_list : Landroid/widget/ListView;
    //   645: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$15
    //   648: dup
    //   649: aload_0
    //   650: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
    //   653: invokevirtual setOnScrollListener : (Landroid/widget/AbsListView$OnScrollListener;)V
    //   656: aload_0
    //   657: getfield connectedSsid : Ljava/lang/String;
    //   660: invokestatic isSupportedMoreButton : (Ljava/lang/String;)Z
    //   663: ifeq -> 1472
    //   666: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_camerasave_button : Landroid/widget/ImageButton;
    //   669: bipush #8
    //   671: invokevirtual setVisibility : (I)V
    //   674: aload_0
    //   675: invokespecial updateImageOfGallaryButton : ()V
    //   678: aload_0
    //   679: getfield bConnect : Z
    //   682: ifeq -> 1483
    //   685: aload_0
    //   686: iconst_1
    //   687: invokespecial setShutterButtonEnabled : (Z)V
    //   690: aload_0
    //   691: iconst_1
    //   692: invokespecial setButtonEnabled : (Z)V
    //   695: aload_0
    //   696: invokespecial setIndicator : ()V
    //   699: return
    //   700: aload_0
    //   701: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   704: invokevirtual getDialModeValue : ()Ljava/lang/String;
    //   707: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   710: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   713: ldc_w 'P'
    //   716: invokevirtual equals : (Ljava/lang/Object;)Z
    //   719: ifeq -> 734
    //   722: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_title : Landroid/widget/TextView;
    //   725: ldc_w 'Program'
    //   728: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   731: goto -> 41
    //   734: aload_0
    //   735: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   738: invokevirtual getDialModeValue : ()Ljava/lang/String;
    //   741: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   744: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   747: ldc_w 'A'
    //   750: invokevirtual equals : (Ljava/lang/Object;)Z
    //   753: ifeq -> 768
    //   756: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_title : Landroid/widget/TextView;
    //   759: ldc_w 'Aperture Priority'
    //   762: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   765: goto -> 41
    //   768: aload_0
    //   769: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   772: invokevirtual getDialModeValue : ()Ljava/lang/String;
    //   775: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   778: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   781: ldc_w 'S'
    //   784: invokevirtual equals : (Ljava/lang/Object;)Z
    //   787: ifeq -> 802
    //   790: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_title : Landroid/widget/TextView;
    //   793: ldc_w 'Shutter Priority'
    //   796: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   799: goto -> 41
    //   802: aload_0
    //   803: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   806: invokevirtual getDialModeValue : ()Ljava/lang/String;
    //   809: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
    //   812: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
    //   815: ldc_w 'M'
    //   818: invokevirtual equals : (Ljava/lang/Object;)Z
    //   821: ifeq -> 41
    //   824: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_title : Landroid/widget/TextView;
    //   827: ldc_w 'Manual'
    //   830: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   833: goto -> 41
    //   836: aload_0
    //   837: invokespecial getSmartPanelVisibility : ()I
    //   840: ifeq -> 850
    //   843: aload_0
    //   844: getfield mSmartPanelCustomDialogWheelMenuId : I
    //   847: ifeq -> 913
    //   850: aload_0
    //   851: getfield rvf_smart_panel_icon : Landroid/widget/ImageButton;
    //   854: bipush #8
    //   856: invokevirtual setVisibility : (I)V
    //   859: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_title : Landroid/widget/TextView;
    //   862: iconst_4
    //   863: invokevirtual setVisibility : (I)V
    //   866: aload_0
    //   867: invokespecial hideIndicatorBar : ()V
    //   870: aload_0
    //   871: getfield rvf_gallery : Landroid/widget/RelativeLayout;
    //   874: bipush #8
    //   876: invokevirtual setVisibility : (I)V
    //   879: aload_0
    //   880: getfield rvf_mode_button : Landroid/widget/Button;
    //   883: bipush #8
    //   885: invokevirtual setVisibility : (I)V
    //   888: aload_0
    //   889: getfield rvf_shutter_button : Landroid/widget/ImageButton;
    //   892: bipush #8
    //   894: invokevirtual setVisibility : (I)V
    //   897: aload_0
    //   898: getfield rvf_camcorder_button : Landroid/widget/ImageButton;
    //   901: bipush #8
    //   903: invokevirtual setVisibility : (I)V
    //   906: aload_0
    //   907: invokespecial hideZoomBar : ()V
    //   910: goto -> 139
    //   913: aload_0
    //   914: getfield rvf_smart_panel_icon : Landroid/widget/ImageButton;
    //   917: iconst_0
    //   918: invokevirtual setVisibility : (I)V
    //   921: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_title : Landroid/widget/TextView;
    //   924: iconst_0
    //   925: invokevirtual setVisibility : (I)V
    //   928: aload_0
    //   929: invokespecial showIndicatorBar : ()V
    //   932: aload_0
    //   933: getfield rvf_gallery : Landroid/widget/RelativeLayout;
    //   936: iconst_0
    //   937: invokevirtual setVisibility : (I)V
    //   940: aload_0
    //   941: getfield rvf_mode_button : Landroid/widget/Button;
    //   944: iconst_0
    //   945: invokevirtual setVisibility : (I)V
    //   948: aload_0
    //   949: getfield rvf_shutter_button : Landroid/widget/ImageButton;
    //   952: iconst_0
    //   953: invokevirtual setVisibility : (I)V
    //   956: aload_0
    //   957: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   960: invokevirtual getMovieResolutionMenuItems : ()Ljava/util/ArrayList;
    //   963: invokevirtual size : ()I
    //   966: ifle -> 977
    //   969: aload_0
    //   970: getfield rvf_camcorder_button : Landroid/widget/ImageButton;
    //   973: iconst_0
    //   974: invokevirtual setVisibility : (I)V
    //   977: aload_0
    //   978: invokespecial showZoomBar : ()V
    //   981: goto -> 139
    //   984: aload_0
    //   985: getfield rvf_menu_button : Landroid/widget/RelativeLayout;
    //   988: iconst_0
    //   989: invokevirtual setVisibility : (I)V
    //   992: aload_0
    //   993: getfield rvf_smart_panel_icon : Landroid/widget/ImageButton;
    //   996: bipush #8
    //   998: invokevirtual setVisibility : (I)V
    //   1001: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_title : Landroid/widget/TextView;
    //   1004: iconst_0
    //   1005: invokevirtual setVisibility : (I)V
    //   1008: aload_0
    //   1009: invokespecial showIndicatorBar : ()V
    //   1012: aload_0
    //   1013: getfield rvf_gallery : Landroid/widget/RelativeLayout;
    //   1016: iconst_0
    //   1017: invokevirtual setVisibility : (I)V
    //   1020: aload_0
    //   1021: getfield rvf_mode_button : Landroid/widget/Button;
    //   1024: bipush #8
    //   1026: invokevirtual setVisibility : (I)V
    //   1029: aload_0
    //   1030: getfield rvf_shutter_button : Landroid/widget/ImageButton;
    //   1033: iconst_0
    //   1034: invokevirtual setVisibility : (I)V
    //   1037: aload_0
    //   1038: getfield rvf_camcorder_button : Landroid/widget/ImageButton;
    //   1041: bipush #8
    //   1043: invokevirtual setVisibility : (I)V
    //   1046: aload_0
    //   1047: invokespecial showZoomBar : ()V
    //   1050: goto -> 276
    //   1053: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_zoomSeekBar : Landroid/widget/SeekBar;
    //   1056: iconst_0
    //   1057: invokevirtual setEnabled : (Z)V
    //   1060: goto -> 355
    //   1063: aload_0
    //   1064: invokevirtual getSmartPanelCustomDialogListMenuId : ()I
    //   1067: ifeq -> 1106
    //   1070: aload_0
    //   1071: iconst_0
    //   1072: invokespecial setSmartPanelVisibility : (I)V
    //   1075: aload_0
    //   1076: invokevirtual getPhoneCamType : ()I
    //   1079: iconst_1
    //   1080: if_icmpne -> 1099
    //   1083: aload_0
    //   1084: getfield rvf_rec_stop_button : Landroid/widget/ImageButton;
    //   1087: bipush #8
    //   1089: invokevirtual setVisibility : (I)V
    //   1092: aload_0
    //   1093: invokespecial hideZoomBar : ()V
    //   1096: goto -> 445
    //   1099: aload_0
    //   1100: invokespecial hideOSD : ()V
    //   1103: goto -> 445
    //   1106: aload_0
    //   1107: getfield rvf_surface : Landroid/view/SurfaceView;
    //   1110: iconst_1
    //   1111: invokevirtual setEnabled : (Z)V
    //   1114: aload_0
    //   1115: getfield rvf_rec_stop_button : Landroid/widget/ImageButton;
    //   1118: bipush #8
    //   1120: invokevirtual setVisibility : (I)V
    //   1123: aload_0
    //   1124: getfield rvf_rec_title : Landroid/widget/LinearLayout;
    //   1127: bipush #8
    //   1129: invokevirtual setVisibility : (I)V
    //   1132: aload_0
    //   1133: getfield rvf_rec_time : Landroid/widget/LinearLayout;
    //   1136: bipush #8
    //   1138: invokevirtual setVisibility : (I)V
    //   1141: goto -> 445
    //   1144: aload_0
    //   1145: getfield connectedSsid : Ljava/lang/String;
    //   1148: invokestatic isSupportedMoreButton : (Ljava/lang/String;)Z
    //   1151: ifeq -> 1289
    //   1154: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_camerasave_button : Landroid/widget/ImageButton;
    //   1157: bipush #8
    //   1159: invokevirtual setVisibility : (I)V
    //   1162: aload_0
    //   1163: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$MainOptionMenuListAdapter
    //   1166: dup
    //   1167: aload_0
    //   1168: aload_0
    //   1169: ldc_w 2130903099
    //   1172: aload_0
    //   1173: getfield mMainOptionMenuList : Ljava/util/ArrayList;
    //   1176: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/content/Context;ILjava/util/List;)V
    //   1179: putfield mMainOptionMenuListAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$MainOptionMenuListAdapter;
    //   1182: aload_0
    //   1183: getfield rvf_list : Landroid/widget/ListView;
    //   1186: aload_0
    //   1187: getfield mMainOptionMenuItemClickListener : Landroid/widget/AdapterView$OnItemClickListener;
    //   1190: invokevirtual setOnItemClickListener : (Landroid/widget/AdapterView$OnItemClickListener;)V
    //   1193: aload_0
    //   1194: getfield rvf_list : Landroid/widget/ListView;
    //   1197: aload_0
    //   1198: getfield mMainOptionMenuListAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$MainOptionMenuListAdapter;
    //   1201: invokevirtual setAdapter : (Landroid/widget/ListAdapter;)V
    //   1204: aload_0
    //   1205: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$SubOptionMenuListAdapter
    //   1208: dup
    //   1209: aload_0
    //   1210: aload_0
    //   1211: ldc_w 2130903099
    //   1214: aload_0
    //   1215: getfield mSubOptionMenuList : Ljava/util/ArrayList;
    //   1218: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/content/Context;ILjava/util/List;)V
    //   1221: putfield mSubOptionMenuListAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$SubOptionMenuListAdapter;
    //   1224: aload_0
    //   1225: getfield rvf_setting_sub_menu_list : Landroid/widget/ListView;
    //   1228: aload_0
    //   1229: getfield mSubOptionMenuItemClickListener : Landroid/widget/AdapterView$OnItemClickListener;
    //   1232: invokevirtual setOnItemClickListener : (Landroid/widget/AdapterView$OnItemClickListener;)V
    //   1235: aload_0
    //   1236: getfield rvf_setting_sub_menu_list : Landroid/widget/ListView;
    //   1239: aload_0
    //   1240: getfield mSubOptionMenuListAdapter : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$SubOptionMenuListAdapter;
    //   1243: invokevirtual setAdapter : (Landroid/widget/ListAdapter;)V
    //   1246: aload_0
    //   1247: invokevirtual getPhoneCamType : ()I
    //   1250: tableswitch default -> 1280, 1 -> 1300, 2 -> 1411, 3 -> 1411, 4 -> 1411
    //   1280: aload_0
    //   1281: bipush #8
    //   1283: invokespecial setSmartPanelVisibility : (I)V
    //   1286: goto -> 493
    //   1289: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_cameramore_button : Landroid/widget/ImageButton;
    //   1292: bipush #8
    //   1294: invokevirtual setVisibility : (I)V
    //   1297: goto -> 1162
    //   1300: aload_0
    //   1301: invokevirtual getCurrentMainOptionMenuId : ()I
    //   1304: ifeq -> 1373
    //   1307: aload_0
    //   1308: invokespecial showQuickSettingMenu : ()V
    //   1311: aload_0
    //   1312: aload_0
    //   1313: invokevirtual getCurrentMainOptionMenuId : ()I
    //   1316: invokespecial showMainOptionMenu : (I)V
    //   1319: aload_0
    //   1320: invokevirtual getCurrentSubOptionMenuId : ()I
    //   1323: ifeq -> 1334
    //   1326: aload_0
    //   1327: aload_0
    //   1328: invokevirtual getCurrentSubOptionMenuId : ()I
    //   1331: invokespecial showSubOptionMenu : (I)V
    //   1334: aload_0
    //   1335: getfield connectedSsid : Ljava/lang/String;
    //   1338: invokestatic isUnsupportedZoomableLens : (Ljava/lang/String;)Z
    //   1341: ifne -> 1361
    //   1344: aload_0
    //   1345: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   1348: invokevirtual getMaxZoom : ()Ljava/lang/String;
    //   1351: invokestatic parseInt : (Ljava/lang/String;)I
    //   1354: ifle -> 1361
    //   1357: aload_0
    //   1358: invokespecial hideZoomBar : ()V
    //   1361: aload_0
    //   1362: getfield rvf_shutter_button : Landroid/widget/ImageButton;
    //   1365: bipush #8
    //   1367: invokevirtual setVisibility : (I)V
    //   1370: goto -> 1280
    //   1373: aload_0
    //   1374: getfield connectedSsid : Ljava/lang/String;
    //   1377: invokestatic isUnsupportedZoomableLens : (Ljava/lang/String;)Z
    //   1380: ifne -> 1400
    //   1383: aload_0
    //   1384: getfield mDeviceController : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
    //   1387: invokevirtual getMaxZoom : ()Ljava/lang/String;
    //   1390: invokestatic parseInt : (Ljava/lang/String;)I
    //   1393: ifle -> 1400
    //   1396: aload_0
    //   1397: invokespecial showZoomBar : ()V
    //   1400: aload_0
    //   1401: getfield rvf_shutter_button : Landroid/widget/ImageButton;
    //   1404: iconst_0
    //   1405: invokevirtual setVisibility : (I)V
    //   1408: goto -> 1280
    //   1411: aload_0
    //   1412: invokevirtual getCurrentMainOptionMenuId : ()I
    //   1415: ifeq -> 1452
    //   1418: aload_0
    //   1419: invokespecial showQuickSettingMenu : ()V
    //   1422: aload_0
    //   1423: aload_0
    //   1424: invokevirtual getCurrentMainOptionMenuId : ()I
    //   1427: invokespecial showMainOptionMenu : (I)V
    //   1430: aload_0
    //   1431: invokevirtual getCurrentSubOptionMenuId : ()I
    //   1434: ifeq -> 1445
    //   1437: aload_0
    //   1438: aload_0
    //   1439: invokevirtual getCurrentSubOptionMenuId : ()I
    //   1442: invokespecial showSubOptionMenu : (I)V
    //   1445: aload_0
    //   1446: invokespecial hideOSD : ()V
    //   1449: goto -> 1280
    //   1452: aload_0
    //   1453: invokespecial showOSD : ()V
    //   1456: goto -> 1280
    //   1459: aload_0
    //   1460: getfield rvf_timerHat : Landroid/widget/ImageView;
    //   1463: ldc_w 2130837888
    //   1466: invokevirtual setImageResource : (I)V
    //   1469: goto -> 526
    //   1472: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.rvf_cameramore_button : Landroid/widget/ImageButton;
    //   1475: bipush #8
    //   1477: invokevirtual setVisibility : (I)V
    //   1480: goto -> 674
    //   1483: aload_0
    //   1484: iconst_0
    //   1485: invokespecial setShutterButtonEnabled : (Z)V
    //   1488: aload_0
    //   1489: iconst_0
    //   1490: invokespecial setButtonEnabled : (Z)V
    //   1493: goto -> 695
  }
  
  private boolean isAvailMovieRecording() {
    if (this.mDeviceController.getRemainRecTimeValue().equals("0")) {
      boolean bool1 = false;
      Trace.d(this.TAG, "end isAvailMovieRecording() isAvailMovieRecording : " + bool1);
      return bool1;
    } 
    boolean bool = true;
    Trace.d(this.TAG, "end isAvailMovieRecording() isAvailMovieRecording : " + bool);
    return bool;
  }
  
  private boolean isAvailShot() {
    if (!this.mDeviceController.getCardStatusValue().toUpperCase(Locale.ENGLISH).equals("INTERNAL") && this.mDeviceController.getAvailShots().equals("0")) {
      boolean bool1 = false;
      Trace.d(this.TAG, "end isAvailShot() isAvailShot : " + bool1);
      return bool1;
    } 
    boolean bool = true;
    Trace.d(this.TAG, "end isAvailShot() isAvailShot : " + bool);
    return bool;
  }
  
  private boolean isDCFFull() {
    if (!this.mDeviceController.getCardStatusValue().toUpperCase(Locale.ENGLISH).equals("INTERNAL") && this.mDeviceController.getAvailShots().equals("-1")) {
      boolean bool1 = true;
      Trace.d(this.TAG, "end isDCFFull() isDCFFull : " + bool1);
      return bool1;
    } 
    boolean bool = false;
    Trace.d(this.TAG, "end isDCFFull() isDCFFull : " + bool);
    return bool;
  }
  
  private boolean isDestroyActivity() {
    return this.mDestroyed;
  }
  
  private boolean isExistImageFile(String paramString) {
    boolean bool = false;
    if ((new File(paramString)).exists()) {
      if ((getFileList(paramString)).length != 0)
        bool = true; 
      return bool;
    } 
    return true;
  }
  
  private boolean isLowBattery() {
    boolean bool = false;
    int j = registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED")).getIntExtra("level", -1);
    int i = j;
    if (Utils.isSCHi909())
      i = j / 10; 
    if (i <= 4)
      bool = true; 
    return bool;
  }
  
  private void loadingdisplay() {
    Intent intent = new Intent((Context)this, LocalGallery.class);
    intent.putExtra("extra_description_res_id", 2131362053);
    intent.putExtra("EXTRA_IS_SUPPORT_BACK_BUTTON", this.mDeviceController.getCardStatusValue().toUpperCase(Locale.ENGLISH).equals("EXTERNAL"));
    if (CMUtil.checkOldVersionSmartCameraApp(getConnectedSSID())) {
      intent.putExtra("extra_back_button_string_res_id", 2131361805);
    } else {
      intent.putExtra("extra_back_button_string_res_id", 2131361803);
    } 
    startActivityForResult(intent, 0);
  }
  
  private void resetVisibilityTouchAFFrame() {
    Trace.d(this.TAG, "start resetVisibilityTouchAFFrame() isTouchAFMovie : " + this.mDeviceController.isTouchAFMovie());
    if (this.mDeviceController.isTouchAFMovie()) {
      rvf_touchAutoFocus.setImageResource(2130837575);
      rvf_touchAutoFocus.setVisibility(0);
      setTouchAFPosition();
      return;
    } 
    if (getSmartPanelVisibility() == 0 || this.mSmartPanelCustomDialogWheelMenuId != 0 || this.mDeviceController.getDefaultFocusState().toUpperCase(Locale.ENGLISH).equals("MF") || this.mDeviceController.getDialModeValue().toUpperCase(Locale.ENGLISH).equals("AUTO")) {
      rvf_touchAutoFocus.setVisibility(4);
      return;
    } 
    if (this.mDeviceController.getAFAreaValue().toUpperCase(Locale.ENGLISH).equals("SELECTION AF")) {
      rvf_touchAutoFocus.setImageResource(2130837575);
      rvf_touchAutoFocus.setVisibility(0);
      setTouchAFPosition();
      return;
    } 
    if (this.mDeviceController.getAFAreaValue().toUpperCase(Locale.ENGLISH).equals("MULTI AF")) {
      rvf_touchAutoFocus.setVisibility(4);
      return;
    } 
  }
  
  private static Bitmap rotate(Bitmap paramBitmap, int paramInt) {
    Bitmap bitmap1 = paramBitmap;
    Bitmap bitmap2 = bitmap1;
    if (paramInt != 0) {
      bitmap2 = bitmap1;
      if (paramBitmap != null) {
        Matrix matrix = new Matrix();
        matrix.setRotate(paramInt, paramBitmap.getWidth() / 2.0F, paramBitmap.getHeight() / 2.0F);
        try {
          return Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError outOfMemoryError) {
          return bitmap1;
        } 
      } 
    } 
    return bitmap2;
  }
  
  private static final double roundDouble(double paramDouble, int paramInt) {
    return Math.round(Math.pow(10.0D, paramInt) * paramDouble) / Math.pow(10.0D, paramInt);
  }
  
  private void screenConfigChange(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   4: new java/lang/StringBuilder
    //   7: dup
    //   8: ldc_w 'start screenConfigChange() ratioType : '
    //   11: invokespecial <init> : (Ljava/lang/String;)V
    //   14: iload_1
    //   15: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   18: invokevirtual toString : ()Ljava/lang/String;
    //   21: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   24: iconst_0
    //   25: istore_2
    //   26: iconst_0
    //   27: istore_3
    //   28: aload_0
    //   29: ldc_w 'window'
    //   32: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   35: checkcast android/view/WindowManager
    //   38: invokeinterface getDefaultDisplay : ()Landroid/view/Display;
    //   43: astore #15
    //   45: aload #15
    //   47: invokevirtual getWidth : ()I
    //   50: aload #15
    //   52: invokevirtual getHeight : ()I
    //   55: if_icmple -> 870
    //   58: aload_0
    //   59: aload #15
    //   61: invokevirtual getWidth : ()I
    //   64: putfield surfaceViewWidth : I
    //   67: aload_0
    //   68: aload #15
    //   70: invokevirtual getHeight : ()I
    //   73: putfield surfaceViewHeight : I
    //   76: iload_1
    //   77: tableswitch default -> 108, 1 -> 891, 2 -> 900, 3 -> 907, 4 -> 914
    //   108: iload_3
    //   109: istore_1
    //   110: aload_0
    //   111: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   114: new java/lang/StringBuilder
    //   117: dup
    //   118: ldc_w '>>>>> surface ratio : '
    //   121: invokespecial <init> : (Ljava/lang/String;)V
    //   124: aload_0
    //   125: getfield surfaceViewWidth : I
    //   128: i2f
    //   129: aload_0
    //   130: getfield surfaceViewHeight : I
    //   133: i2f
    //   134: fdiv
    //   135: invokevirtual append : (F)Ljava/lang/StringBuilder;
    //   138: invokevirtual toString : ()Ljava/lang/String;
    //   141: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   144: aload_0
    //   145: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   148: new java/lang/StringBuilder
    //   151: dup
    //   152: ldc_w '>>>>> image ratio : '
    //   155: invokespecial <init> : (Ljava/lang/String;)V
    //   158: iload_2
    //   159: i2f
    //   160: iload_1
    //   161: i2f
    //   162: fdiv
    //   163: invokevirtual append : (F)Ljava/lang/StringBuilder;
    //   166: invokevirtual toString : ()Ljava/lang/String;
    //   169: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   172: aload_0
    //   173: getfield surfaceViewWidth : I
    //   176: i2f
    //   177: aload_0
    //   178: getfield surfaceViewHeight : I
    //   181: i2f
    //   182: fdiv
    //   183: iload_2
    //   184: i2f
    //   185: iload_1
    //   186: i2f
    //   187: fdiv
    //   188: fcmpg
    //   189: ifge -> 921
    //   192: aload_0
    //   193: getfield surfaceViewWidth : I
    //   196: istore #4
    //   198: aload_0
    //   199: getfield surfaceViewWidth : I
    //   202: i2f
    //   203: iload_2
    //   204: i2f
    //   205: iload_1
    //   206: i2f
    //   207: fdiv
    //   208: fdiv
    //   209: f2i
    //   210: istore_3
    //   211: aload_0
    //   212: getfield surfaceViewWidth : I
    //   215: i2f
    //   216: aload_0
    //   217: getfield surfaceViewHeight : I
    //   220: i2f
    //   221: fdiv
    //   222: iload_1
    //   223: i2f
    //   224: iload_2
    //   225: i2f
    //   226: fdiv
    //   227: fcmpg
    //   228: ifge -> 943
    //   231: aload_0
    //   232: getfield surfaceViewWidth : I
    //   235: istore #6
    //   237: aload_0
    //   238: getfield surfaceViewWidth : I
    //   241: i2f
    //   242: iload_1
    //   243: i2f
    //   244: iload_2
    //   245: i2f
    //   246: fdiv
    //   247: fdiv
    //   248: f2i
    //   249: istore #5
    //   251: ldc_w 1.3333334
    //   254: iload_2
    //   255: i2f
    //   256: iload_1
    //   257: i2f
    //   258: fdiv
    //   259: fcmpg
    //   260: ifge -> 966
    //   263: aload_0
    //   264: getfield surfaceViewHeight : I
    //   267: istore #8
    //   269: aload_0
    //   270: getfield surfaceViewHeight : I
    //   273: i2f
    //   274: iload_2
    //   275: i2f
    //   276: iload_1
    //   277: i2f
    //   278: fdiv
    //   279: fdiv
    //   280: f2i
    //   281: istore #7
    //   283: aload_0
    //   284: getfield surfaceViewWidth : I
    //   287: iload #4
    //   289: isub
    //   290: iconst_2
    //   291: idiv
    //   292: istore #9
    //   294: aload_0
    //   295: getfield surfaceViewHeight : I
    //   298: iload_3
    //   299: isub
    //   300: iconst_2
    //   301: idiv
    //   302: istore #10
    //   304: aload_0
    //   305: getfield surfaceViewWidth : I
    //   308: iload #6
    //   310: isub
    //   311: iconst_2
    //   312: idiv
    //   313: istore #11
    //   315: aload_0
    //   316: getfield surfaceViewHeight : I
    //   319: iload #5
    //   321: isub
    //   322: iconst_2
    //   323: idiv
    //   324: istore #12
    //   326: aload_0
    //   327: getfield surfaceViewHeight : I
    //   330: iload #8
    //   332: isub
    //   333: iconst_2
    //   334: idiv
    //   335: istore #13
    //   337: aload_0
    //   338: getfield surfaceViewHeight : I
    //   341: i2f
    //   342: ldc_w 1.3333334
    //   345: fdiv
    //   346: f2i
    //   347: iload #7
    //   349: isub
    //   350: iconst_2
    //   351: idiv
    //   352: istore #14
    //   354: aload_0
    //   355: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   358: new java/lang/StringBuilder
    //   361: dup
    //   362: ldc_w '>>>>> surfaceView W : '
    //   365: invokespecial <init> : (Ljava/lang/String;)V
    //   368: aload_0
    //   369: getfield surfaceViewWidth : I
    //   372: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   375: ldc_w ' H : '
    //   378: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   381: aload_0
    //   382: getfield surfaceViewHeight : I
    //   385: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   388: invokevirtual toString : ()Ljava/lang/String;
    //   391: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   394: aload_0
    //   395: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   398: new java/lang/StringBuilder
    //   401: dup
    //   402: ldc_w '>>>>> ratio W : '
    //   405: invokespecial <init> : (Ljava/lang/String;)V
    //   408: iload_2
    //   409: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   412: ldc_w ' H : '
    //   415: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   418: iload_1
    //   419: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   422: invokevirtual toString : ()Ljava/lang/String;
    //   425: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   428: aload_0
    //   429: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   432: new java/lang/StringBuilder
    //   435: dup
    //   436: ldc_w '>>>>> landImage W : '
    //   439: invokespecial <init> : (Ljava/lang/String;)V
    //   442: iload #4
    //   444: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   447: ldc_w ' H : '
    //   450: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   453: iload_3
    //   454: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   457: invokevirtual toString : ()Ljava/lang/String;
    //   460: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   463: aload_0
    //   464: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   467: new java/lang/StringBuilder
    //   470: dup
    //   471: ldc_w '>>>>> portImage W : '
    //   474: invokespecial <init> : (Ljava/lang/String;)V
    //   477: iload #6
    //   479: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   482: ldc_w ' H : '
    //   485: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   488: iload #5
    //   490: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   493: invokevirtual toString : ()Ljava/lang/String;
    //   496: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   499: aload_0
    //   500: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   503: new java/lang/StringBuilder
    //   506: dup
    //   507: ldc_w '>>>>> exImage W : '
    //   510: invokespecial <init> : (Ljava/lang/String;)V
    //   513: iload #8
    //   515: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   518: ldc_w ' H : '
    //   521: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   524: iload #7
    //   526: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   529: invokevirtual toString : ()Ljava/lang/String;
    //   532: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   535: aload_0
    //   536: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   539: new java/lang/StringBuilder
    //   542: dup
    //   543: ldc_w '>>>>> landMargin X : '
    //   546: invokespecial <init> : (Ljava/lang/String;)V
    //   549: iload #9
    //   551: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   554: ldc_w ' Y : '
    //   557: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   560: iload #10
    //   562: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   565: invokevirtual toString : ()Ljava/lang/String;
    //   568: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   571: aload_0
    //   572: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   575: new java/lang/StringBuilder
    //   578: dup
    //   579: ldc_w '>>>>> portMargin X : '
    //   582: invokespecial <init> : (Ljava/lang/String;)V
    //   585: iload #11
    //   587: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   590: ldc_w ' Y : '
    //   593: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   596: iload #12
    //   598: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   601: invokevirtual toString : ()Ljava/lang/String;
    //   604: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   607: aload_0
    //   608: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   611: new java/lang/StringBuilder
    //   614: dup
    //   615: ldc_w '>>>>> exMargin X : '
    //   618: invokespecial <init> : (Ljava/lang/String;)V
    //   621: iload #13
    //   623: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   626: ldc_w ' Y : '
    //   629: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   632: iload #14
    //   634: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   637: invokevirtual toString : ()Ljava/lang/String;
    //   640: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   643: getstatic android/os/Build.MODEL : Ljava/lang/String;
    //   646: ldc_w 'N920'
    //   649: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   652: ifeq -> 993
    //   655: aload_0
    //   656: getfield screenPositoin : [[I
    //   659: iconst_0
    //   660: aaload
    //   661: iconst_0
    //   662: iload #13
    //   664: iastore
    //   665: aload_0
    //   666: getfield screenPositoin : [[I
    //   669: iconst_0
    //   670: aaload
    //   671: iconst_1
    //   672: iload #14
    //   674: bipush #100
    //   676: iadd
    //   677: iastore
    //   678: aload_0
    //   679: getfield screenPositoin : [[I
    //   682: iconst_1
    //   683: aaload
    //   684: iconst_0
    //   685: iload #10
    //   687: iastore
    //   688: aload_0
    //   689: getfield screenPositoin : [[I
    //   692: iconst_1
    //   693: aaload
    //   694: iconst_1
    //   695: iload #9
    //   697: iastore
    //   698: aload_0
    //   699: getfield screenPositoin : [[I
    //   702: iconst_2
    //   703: aaload
    //   704: iconst_0
    //   705: iload #9
    //   707: iastore
    //   708: aload_0
    //   709: getfield screenPositoin : [[I
    //   712: iconst_2
    //   713: aaload
    //   714: iconst_1
    //   715: iload #10
    //   717: iastore
    //   718: aload_0
    //   719: getfield screenPositoin : [[I
    //   722: iconst_3
    //   723: aaload
    //   724: iconst_0
    //   725: iload #11
    //   727: iastore
    //   728: aload_0
    //   729: getfield screenPositoin : [[I
    //   732: iconst_3
    //   733: aaload
    //   734: iconst_1
    //   735: iload #12
    //   737: iastore
    //   738: aload_0
    //   739: getfield screenSize : [[I
    //   742: iconst_0
    //   743: aaload
    //   744: iconst_0
    //   745: iload #8
    //   747: iastore
    //   748: aload_0
    //   749: getfield screenSize : [[I
    //   752: iconst_0
    //   753: aaload
    //   754: iconst_1
    //   755: iload #7
    //   757: iastore
    //   758: aload_0
    //   759: getfield screenSize : [[I
    //   762: iconst_1
    //   763: aaload
    //   764: iconst_0
    //   765: iload_3
    //   766: iastore
    //   767: aload_0
    //   768: getfield screenSize : [[I
    //   771: iconst_1
    //   772: aaload
    //   773: iconst_1
    //   774: iload #4
    //   776: iastore
    //   777: aload_0
    //   778: getfield screenSize : [[I
    //   781: iconst_2
    //   782: aaload
    //   783: iconst_0
    //   784: iload #4
    //   786: iastore
    //   787: aload_0
    //   788: getfield screenSize : [[I
    //   791: iconst_2
    //   792: aaload
    //   793: iconst_1
    //   794: iload_3
    //   795: iastore
    //   796: aload_0
    //   797: getfield screenSize : [[I
    //   800: iconst_3
    //   801: aaload
    //   802: iconst_0
    //   803: iload #6
    //   805: iastore
    //   806: aload_0
    //   807: getfield screenSize : [[I
    //   810: iconst_3
    //   811: aaload
    //   812: iconst_1
    //   813: iload #5
    //   815: iastore
    //   816: iconst_0
    //   817: istore_1
    //   818: iload_1
    //   819: iconst_4
    //   820: if_icmplt -> 1016
    //   823: iconst_0
    //   824: istore_1
    //   825: iload_1
    //   826: iconst_4
    //   827: if_icmplt -> 1088
    //   830: iconst_0
    //   831: istore_1
    //   832: aload_0
    //   833: getfield cameratype : Ljava/lang/String;
    //   836: ldc_w 'ssdp:rotationD'
    //   839: invokevirtual equals : (Ljava/lang/Object;)Z
    //   842: ifeq -> 1160
    //   845: iconst_0
    //   846: istore_1
    //   847: getstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI$Command.SCREEN_CONFIG_CHANGE : Lcom/samsungimaging/asphodel/multimedia/FFmpegJNI$Command;
    //   850: aload_0
    //   851: invokevirtual getPhoneCamType : ()I
    //   854: iconst_1
    //   855: isub
    //   856: iload_1
    //   857: aload_0
    //   858: getfield screenPositoin : [[I
    //   861: aload_0
    //   862: getfield screenSize : [[I
    //   865: invokestatic request : (Lcom/samsungimaging/asphodel/multimedia/FFmpegJNI$Command;II[[I[[I)I
    //   868: pop
    //   869: return
    //   870: aload_0
    //   871: aload #15
    //   873: invokevirtual getHeight : ()I
    //   876: putfield surfaceViewWidth : I
    //   879: aload_0
    //   880: aload #15
    //   882: invokevirtual getWidth : ()I
    //   885: putfield surfaceViewHeight : I
    //   888: goto -> 76
    //   891: bipush #16
    //   893: istore_2
    //   894: bipush #9
    //   896: istore_1
    //   897: goto -> 110
    //   900: iconst_4
    //   901: istore_2
    //   902: iconst_3
    //   903: istore_1
    //   904: goto -> 110
    //   907: iconst_3
    //   908: istore_2
    //   909: iconst_2
    //   910: istore_1
    //   911: goto -> 110
    //   914: iconst_1
    //   915: istore_2
    //   916: iconst_1
    //   917: istore_1
    //   918: goto -> 110
    //   921: aload_0
    //   922: getfield surfaceViewHeight : I
    //   925: i2f
    //   926: iload_1
    //   927: i2f
    //   928: iload_2
    //   929: i2f
    //   930: fdiv
    //   931: fdiv
    //   932: f2i
    //   933: istore #4
    //   935: aload_0
    //   936: getfield surfaceViewHeight : I
    //   939: istore_3
    //   940: goto -> 211
    //   943: aload_0
    //   944: getfield surfaceViewHeight : I
    //   947: i2f
    //   948: iload_2
    //   949: i2f
    //   950: iload_1
    //   951: i2f
    //   952: fdiv
    //   953: fdiv
    //   954: f2i
    //   955: istore #6
    //   957: aload_0
    //   958: getfield surfaceViewHeight : I
    //   961: istore #5
    //   963: goto -> 251
    //   966: aload_0
    //   967: getfield surfaceViewHeight : I
    //   970: i2f
    //   971: ldc_w 1.3333334
    //   974: fdiv
    //   975: f2i
    //   976: istore #7
    //   978: iload #7
    //   980: i2f
    //   981: iload_1
    //   982: i2f
    //   983: iload_2
    //   984: i2f
    //   985: fdiv
    //   986: fdiv
    //   987: f2i
    //   988: istore #8
    //   990: goto -> 283
    //   993: aload_0
    //   994: getfield screenPositoin : [[I
    //   997: iconst_0
    //   998: aaload
    //   999: iconst_0
    //   1000: iload #13
    //   1002: iastore
    //   1003: aload_0
    //   1004: getfield screenPositoin : [[I
    //   1007: iconst_0
    //   1008: aaload
    //   1009: iconst_1
    //   1010: iload #14
    //   1012: iastore
    //   1013: goto -> 678
    //   1016: iconst_0
    //   1017: istore_2
    //   1018: iload_2
    //   1019: iconst_2
    //   1020: if_icmplt -> 1030
    //   1023: iload_1
    //   1024: iconst_1
    //   1025: iadd
    //   1026: istore_1
    //   1027: goto -> 818
    //   1030: aload_0
    //   1031: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1034: new java/lang/StringBuilder
    //   1037: dup
    //   1038: ldc_w '>>>>> screenPositoin['
    //   1041: invokespecial <init> : (Ljava/lang/String;)V
    //   1044: iload_1
    //   1045: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1048: ldc_w ']['
    //   1051: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1054: iload_2
    //   1055: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1058: ldc_w '] : '
    //   1061: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1064: aload_0
    //   1065: getfield screenPositoin : [[I
    //   1068: iload_1
    //   1069: aaload
    //   1070: iload_2
    //   1071: iaload
    //   1072: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1075: invokevirtual toString : ()Ljava/lang/String;
    //   1078: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1081: iload_2
    //   1082: iconst_1
    //   1083: iadd
    //   1084: istore_2
    //   1085: goto -> 1018
    //   1088: iconst_0
    //   1089: istore_2
    //   1090: iload_2
    //   1091: iconst_2
    //   1092: if_icmplt -> 1102
    //   1095: iload_1
    //   1096: iconst_1
    //   1097: iadd
    //   1098: istore_1
    //   1099: goto -> 825
    //   1102: aload_0
    //   1103: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1106: new java/lang/StringBuilder
    //   1109: dup
    //   1110: ldc_w '>>>>> screenSize['
    //   1113: invokespecial <init> : (Ljava/lang/String;)V
    //   1116: iload_1
    //   1117: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1120: ldc_w ']['
    //   1123: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1126: iload_2
    //   1127: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1130: ldc_w '] : '
    //   1133: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1136: aload_0
    //   1137: getfield screenSize : [[I
    //   1140: iload_1
    //   1141: aaload
    //   1142: iload_2
    //   1143: iaload
    //   1144: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1147: invokevirtual toString : ()Ljava/lang/String;
    //   1150: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1153: iload_2
    //   1154: iconst_1
    //   1155: iadd
    //   1156: istore_2
    //   1157: goto -> 1090
    //   1160: aload_0
    //   1161: getfield cameratype : Ljava/lang/String;
    //   1164: ldc_w 'ssdp:rotationR'
    //   1167: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1170: ifeq -> 1179
    //   1173: bipush #90
    //   1175: istore_1
    //   1176: goto -> 847
    //   1179: aload_0
    //   1180: getfield cameratype : Ljava/lang/String;
    //   1183: ldc_w 'ssdp:rotationU'
    //   1186: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1189: ifeq -> 1199
    //   1192: sipush #180
    //   1195: istore_1
    //   1196: goto -> 847
    //   1199: aload_0
    //   1200: getfield cameratype : Ljava/lang/String;
    //   1203: ldc_w 'ssdp:rotationL'
    //   1206: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1209: ifeq -> 847
    //   1212: sipush #270
    //   1215: istore_1
    //   1216: goto -> 847
  }
  
  private void setActionState(int paramInt) {
    this.mActionState = paramInt;
  }
  
  private void setButtonEnabled(boolean paramBoolean) {
    Trace.d(this.TAG, "start setButtonEnabled() enabled : " + paramBoolean);
    rvf_flash_button.setEnabled(paramBoolean);
    rvf_timer_button.setEnabled(paramBoolean);
    rvf_photosize_button.setEnabled(paramBoolean);
    rvf_camerasave_button.setEnabled(paramBoolean);
    rvf_cameramore_button.setEnabled(paramBoolean);
    this.rvf_thumbnail.setEnabled(paramBoolean);
    if (isAvailShot()) {
      if (RVFFunctionManager.isTouchableZoomSeekBar(this.connectedSsid))
        rvf_zoomSeekBar.setEnabled(paramBoolean); 
      if (RVFFunctionManager.isTouchableZoomButton(this.connectedSsid)) {
        if (this.mZoomState == 1 || this.mZoomState == 2) {
          rvf_zoomin_button.setEnabled(true);
          rvf_zoomout_button.setEnabled(true);
        } else {
          rvf_zoomin_button.setEnabled(paramBoolean);
          rvf_zoomout_button.setEnabled(paramBoolean);
        } 
      } else {
        rvf_zoomin_button.setEnabled(false);
        rvf_zoomout_button.setEnabled(false);
      } 
    } else {
      rvf_zoomSeekBar.setEnabled(false);
      rvf_zoomin_button.setEnabled(false);
      rvf_zoomout_button.setEnabled(false);
    } 
    this.rvf_smart_panel_icon.setEnabled(paramBoolean);
    this.rvf_smart_panel_wb.setEnabled(paramBoolean);
    this.rvf_smart_panel_metering.setEnabled(paramBoolean);
    if ((!this.mDeviceController.getDialModeValue().equals("Auto") && this.mDeviceController.getQualityValue().equals("RAW")) || isDimMenu(this.rvf_smart_panel_photoSize)) {
      this.rvf_smart_panel_photoSize.setEnabled(false);
    } else {
      this.rvf_smart_panel_photoSize.setEnabled(paramBoolean);
    } 
    this.rvf_smart_panel_quality.setEnabled(paramBoolean);
    this.rvf_smart_panel_movieSize.setEnabled(paramBoolean);
    this.rvf_smart_panel_afMode.setEnabled(paramBoolean);
    this.rvf_smart_panel_drive.setEnabled(paramBoolean);
    this.rvf_smart_panel_flash.setEnabled(paramBoolean);
    if (isDimMenu(this.rvf_smart_panel_touchAF)) {
      Trace.d(this.TAG, ">>>>>>> 002");
      this.rvf_smart_panel_afArea.setEnabled(false);
    } else {
      Trace.d(this.TAG, ">>>>>>> 003");
      this.rvf_smart_panel_afArea.setEnabled(paramBoolean);
    } 
    if (isDimMenu(this.rvf_smart_panel_touchAF)) {
      this.rvf_smart_panel_touchAF.setEnabled(false);
    } else {
      this.rvf_smart_panel_touchAF.setEnabled(paramBoolean);
    } 
    if (isDimMenu(this.rvf_smart_panel_save)) {
      this.rvf_smart_panel_save.setEnabled(false);
    } else {
      this.rvf_smart_panel_save.setEnabled(paramBoolean);
    } 
    this.rvf_smart_panel_streaming_quality.setEnabled(paramBoolean);
    this.rvf_mode_button.setEnabled(paramBoolean);
    this.rvf_camcorder_button.setEnabled(paramBoolean);
  }
  
  private void setContentView() {
    Trace.d(this.TAG, "start setContentView()");
    if (this.isOptimusVu) {
      switch (getPhoneCamType()) {
        default:
          this.rvf_camera_btn_af = (ImageView)findViewById(2131558687);
          this.rvf_camera_btn_af.setOnClickListener(this);
          this.rvf_surface = (SurfaceView)findViewById(2131558403);
          this.rvf_live_shutter = (RelativeLayout)findViewById(2131558402);
          if (Build.MODEL.contains("N920")) {
            (this.rvf_live_shutter.getLayoutParams()).height = 1245;
            (this.rvf_live_shutter.getLayoutParams()).width = -1;
          } 
          this.rvf_surface.setOnTouchListener(this);
          this.rvf_surface.setOnClickListener(this);
          rvf_touchAutoFocus = (ImageView)findViewById(2131558405);
          rvf_metering_spot_frame = (ImageView)findViewById(2131558404);
          rvf_af_extend = (RelativeLayout)findViewById(2131558406);
          this.rvf_af_extend_leftup = (ImageView)findViewById(2131558407);
          this.rvf_af_extend_leftdown = (ImageView)findViewById(2131558408);
          this.rvf_af_extend_rightup = (ImageView)findViewById(2131558409);
          this.rvf_af_extend_rightdown = (ImageView)findViewById(2131558410);
          rvf_af3x3_00 = (ImageView)findViewById(2131558411);
          rvf_af3x3_01 = (ImageView)findViewById(2131558412);
          rvf_af3x3_02 = (ImageView)findViewById(2131558413);
          rvf_af3x3_10 = (ImageView)findViewById(2131558414);
          rvf_af3x3_11 = (ImageView)findViewById(2131558415);
          rvf_af3x3_12 = (ImageView)findViewById(2131558416);
          rvf_af3x3_20 = (ImageView)findViewById(2131558417);
          rvf_af3x3_21 = (ImageView)findViewById(2131558418);
          rvf_af3x3_22 = (ImageView)findViewById(2131558419);
          rvf_af3x5_00 = (ImageView)findViewById(2131558420);
          rvf_af3x5_01 = (ImageView)findViewById(2131558421);
          rvf_af3x5_02 = (ImageView)findViewById(2131558422);
          rvf_af3x5_03 = (ImageView)findViewById(2131558423);
          rvf_af3x5_04 = (ImageView)findViewById(2131558424);
          rvf_af3x5_10 = (ImageView)findViewById(2131558425);
          rvf_af3x5_11 = (ImageView)findViewById(2131558426);
          rvf_af3x5_12 = (ImageView)findViewById(2131558427);
          rvf_af3x5_13 = (ImageView)findViewById(2131558428);
          rvf_af3x5_14 = (ImageView)findViewById(2131558429);
          rvf_af3x5_20 = (ImageView)findViewById(2131558430);
          rvf_af3x5_21 = (ImageView)findViewById(2131558431);
          rvf_af3x5_22 = (ImageView)findViewById(2131558432);
          rvf_af3x5_23 = (ImageView)findViewById(2131558433);
          rvf_af3x5_24 = (ImageView)findViewById(2131558434);
          rvf_af3x7_00 = (ImageView)findViewById(2131558435);
          rvf_af3x7_01 = (ImageView)findViewById(2131558436);
          rvf_af3x7_02 = (ImageView)findViewById(2131558437);
          rvf_af3x7_03 = (ImageView)findViewById(2131558438);
          rvf_af3x7_04 = (ImageView)findViewById(2131558439);
          rvf_af3x7_05 = (ImageView)findViewById(2131558440);
          rvf_af3x7_06 = (ImageView)findViewById(2131558441);
          rvf_af3x7_10 = (ImageView)findViewById(2131558442);
          rvf_af3x7_11 = (ImageView)findViewById(2131558443);
          rvf_af3x7_12 = (ImageView)findViewById(2131558444);
          rvf_af3x7_13 = (ImageView)findViewById(2131558445);
          rvf_af3x7_14 = (ImageView)findViewById(2131558446);
          rvf_af3x7_15 = (ImageView)findViewById(2131558447);
          rvf_af3x7_16 = (ImageView)findViewById(2131558448);
          rvf_af3x7_20 = (ImageView)findViewById(2131558449);
          rvf_af3x7_21 = (ImageView)findViewById(2131558450);
          rvf_af3x7_22 = (ImageView)findViewById(2131558451);
          rvf_af3x7_23 = (ImageView)findViewById(2131558452);
          rvf_af3x7_24 = (ImageView)findViewById(2131558453);
          rvf_af3x7_25 = (ImageView)findViewById(2131558454);
          rvf_af3x7_26 = (ImageView)findViewById(2131558455);
          this.rvf_timerCountMain = (RelativeLayout)findViewById(2131558456);
          this.rvf_timerHat = (ImageView)findViewById(2131558457);
          this.rvf_timecount = (ImageView)findViewById(2131558458);
          rvf_title = (TextView)findViewById(2131558459);
          this.rvf_memory_full = (ImageView)findViewById(2131558460);
          this.rvf_mode_icon = (ImageView)findViewById(2131558461);
          this.rvf_smart_panel_icon = (ImageButton)findViewById(2131558462);
          this.rvf_smart_panel_icon.setOnClickListener(this);
          this.rvf_menu_button = (RelativeLayout)findViewById(2131558463);
          rvf_open_button = (ImageButton)findViewById(2131558464);
          rvf_open_button.setOnClickListener(this);
          rvf_QuickSettingMenu = (RelativeLayout)findViewById(2131558465);
          rvf_flash_button = (ImageButton)findViewById(2131558466);
          rvf_flash_button.setOnClickListener(this);
          rvf_timer_button = (ImageButton)findViewById(2131558467);
          rvf_timer_button.setOnClickListener(this);
          rvf_photosize_button = (ImageButton)findViewById(2131558468);
          rvf_photosize_button.setOnClickListener(this);
          rvf_camerasave_button = (ImageButton)findViewById(2131558469);
          rvf_camerasave_button.setOnClickListener(this);
          rvf_cameramore_button = (ImageButton)findViewById(2131558470);
          rvf_cameramore_button.setOnClickListener(this);
          this.rvf_menuTop1 = (ImageView)findViewById(2131558471);
          this.rvf_menuTop2 = (ImageView)findViewById(2131558472);
          this.rvf_menuTop3 = (ImageView)findViewById(2131558473);
          this.rvf_menuTop4 = (ImageView)findViewById(2131558474);
          rvf_close_button = (ImageButton)findViewById(2131558475);
          rvf_close_button.setOnClickListener(this);
          this.rvf_indicator_bar = (LinearLayout)findViewById(2131558676);
          this.rvf_indicator_wb = (ImageButton)findViewById(2131558677);
          this.rvf_indicator_metering = (ImageButton)findViewById(2131558678);
          this.rvf_indicator_drive = (ImageButton)findViewById(2131558679);
          this.rvf_indicator_flash = (ImageButton)findViewById(2131558680);
          this.rvf_indicator_photo_size = (ImageButton)findViewById(2131558681);
          this.rvf_indicator_quality = (ImageButton)findViewById(2131558682);
          this.rvf_indicator_movie_size = (ImageButton)findViewById(2131558683);
          this.rvf_indicator_af_area = (ImageButton)findViewById(2131558684);
          this.rvf_indicator_touch_af = (ImageButton)findViewById(2131558685);
          this.rvf_indicator_storage = (ImageButton)findViewById(2131558686);
          rvf_zoomMain = (RelativeLayout)findViewById(2131558477);
          rvf_zoomMain.setOnTouchListener(this);
          rvf_zoomout_button = (ImageButton)findViewById(2131558478);
          rvf_zoomout_button.setOnTouchListener(this);
          rvf_zoomin_button = (ImageButton)findViewById(2131558479);
          rvf_zoomin_button.setOnTouchListener(this);
          if (orientation == 1) {
            rvf_zoomSeekBar = (SeekBar)findViewById(2131558480);
          } else {
            break;
          } 
          this.rvf_mode_button = (Button)findViewById(2131558481);
          this.rvf_mode_button.setOnTouchListener(this);
          this.rvf_mode_button.setOnClickListener(this);
          this.rvf_shutter_button = (ImageButton)findViewById(2131558482);
          this.rvf_shutter_button.setOnTouchListener(this);
          this.rvf_camcorder_button = (ImageButton)findViewById(2131558483);
          this.rvf_camcorder_button.setOnTouchListener(this);
          this.rvf_camcorder_button.setOnClickListener(this);
          this.rvf_rec_stop_button = (ImageButton)findViewById(2131558484);
          this.rvf_rec_stop_button.setOnClickListener(this);
          this.rvf_rec_title = (LinearLayout)findViewById(2131558485);
          this.rvf_rec_icon = (ImageView)findViewById(2131558486);
          this.rvf_rec_time = (LinearLayout)findViewById(2131558487);
          this.rvf_recording_time = (TextView)findViewById(2131558488);
          this.rvf_remain_time = (TextView)findViewById(2131558489);
          this.rvf_list = (ListView)findViewById(2131558495);
          this.rvf_option = (LinearLayout)findViewById(2131558493);
          this.rvf_menuTitle = (TextView)findViewById(2131558494);
          this.rvf_setting_sub_menu = (LinearLayout)findViewById(2131558496);
          this.rvf_setting_sub_menu_title = (TextView)findViewById(2131558497);
          this.rvf_setting_sub_menu_list = (ListView)findViewById(2131558498);
          this.rvf_gallery = (RelativeLayout)findViewById(2131558490);
          this.rvf_thumbnail = (ImageView)findViewById(2131558492);
          this.rvf_thumbnail.setOnClickListener(this);
          this.rvf_no_item = (ImageView)findViewById(2131558491);
          this.rvf_smart_panel = (LinearLayout)findViewById(2131558694);
          this.rvf_smart_panel.setOnClickListener(this);
          this.rvf_smart_panel_mode = (WheelView)findViewById(2131558695);
          this.rvf_smart_panel_mode.addScrollingListener(this);
          this.rvf_smart_panel_mode.addClickingListener(this);
          this.rvf_smart_panel_shutterSpeed = (WheelView)findViewById(2131558696);
          this.rvf_smart_panel_shutterSpeed.addScrollingListener(this);
          this.rvf_smart_panel_shutterSpeed.addClickingListener(this);
          this.rvf_smart_panel_aperture = (WheelView)findViewById(2131558697);
          this.rvf_smart_panel_aperture.addScrollingListener(this);
          this.rvf_smart_panel_aperture.addClickingListener(this);
          this.rvf_smart_panel_ev = (WheelView)findViewById(2131558698);
          this.rvf_smart_panel_ev.addScrollingListener(this);
          this.rvf_smart_panel_ev.addClickingListener(this);
          this.rvf_smart_panel_iso = (WheelView)findViewById(2131558699);
          this.rvf_smart_panel_iso.addScrollingListener(this);
          this.rvf_smart_panel_iso.addClickingListener(this);
          this.rvf_smart_panel_wb = (ImageButton)findViewById(2131558700);
          this.rvf_smart_panel_wb.setOnClickListener(this);
          this.rvf_smart_panel_metering = (ImageButton)findViewById(2131558701);
          this.rvf_smart_panel_metering.setOnClickListener(this);
          this.rvf_smart_panel_photoSize = (ImageButton)findViewById(2131558705);
          this.rvf_smart_panel_photoSize.setOnClickListener(this);
          this.rvf_smart_panel_quality = (ImageButton)findViewById(2131558706);
          this.rvf_smart_panel_quality.setOnClickListener(this);
          this.rvf_smart_panel_movieSize = (ImageButton)findViewById(2131558707);
          this.rvf_smart_panel_movieSize.setOnClickListener(this);
          this.rvf_smart_panel_afMode = (ImageButton)findViewById(2131558704);
          this.rvf_smart_panel_afMode.setOnClickListener(this);
          this.rvf_smart_panel_drive = (ImageButton)findViewById(2131558702);
          this.rvf_smart_panel_drive.setOnClickListener(this);
          this.rvf_smart_panel_flash = (ImageButton)findViewById(2131558703);
          this.rvf_smart_panel_flash.setOnClickListener(this);
          this.rvf_smart_panel_afArea = (ImageButton)findViewById(2131558709);
          this.rvf_smart_panel_afArea.setOnClickListener(this);
          this.rvf_smart_panel_touchAF = (ImageButton)findViewById(2131558710);
          this.rvf_smart_panel_touchAF.setOnClickListener(this);
          this.rvf_smart_panel_save = (ImageButton)findViewById(2131558711);
          this.rvf_smart_panel_save.setOnClickListener(this);
          this.rvf_smart_panel_streaming_quality = (ImageButton)findViewById(2131558708);
          this.rvf_smart_panel_streaming_quality.setOnClickListener(this);
          this.rvf_toast_message = (TextView)findViewById(2131558714);
          this.rvf_progress = (ProgressBar)findViewById(2131558476);
          return;
        case 1:
          setContentView(2130903097);
        case 3:
          setContentView(2130903095);
        case 2:
          setContentView(2130903098);
        case 4:
          setContentView(2130903096);
      } 
    } else {
      switch (getPhoneCamType()) {
        default:
        
        case 1:
        case 3:
          setContentView(2130903095);
        case 2:
        case 4:
          break;
      } 
      setContentView(2130903096);
    } 
    rvf_zoomSeekBar = (SeekBar)findViewById(2131558480);
    this.rvf_mode_button = (Button)findViewById(2131558481);
    this.rvf_mode_button.setOnTouchListener(this);
    this.rvf_mode_button.setOnClickListener(this);
    this.rvf_shutter_button = (ImageButton)findViewById(2131558482);
    this.rvf_shutter_button.setOnTouchListener(this);
    this.rvf_camcorder_button = (ImageButton)findViewById(2131558483);
    this.rvf_camcorder_button.setOnTouchListener(this);
    this.rvf_camcorder_button.setOnClickListener(this);
    this.rvf_rec_stop_button = (ImageButton)findViewById(2131558484);
    this.rvf_rec_stop_button.setOnClickListener(this);
    this.rvf_rec_title = (LinearLayout)findViewById(2131558485);
    this.rvf_rec_icon = (ImageView)findViewById(2131558486);
    this.rvf_rec_time = (LinearLayout)findViewById(2131558487);
    this.rvf_recording_time = (TextView)findViewById(2131558488);
    this.rvf_remain_time = (TextView)findViewById(2131558489);
    this.rvf_list = (ListView)findViewById(2131558495);
    this.rvf_option = (LinearLayout)findViewById(2131558493);
    this.rvf_menuTitle = (TextView)findViewById(2131558494);
    this.rvf_setting_sub_menu = (LinearLayout)findViewById(2131558496);
    this.rvf_setting_sub_menu_title = (TextView)findViewById(2131558497);
    this.rvf_setting_sub_menu_list = (ListView)findViewById(2131558498);
    this.rvf_gallery = (RelativeLayout)findViewById(2131558490);
    this.rvf_thumbnail = (ImageView)findViewById(2131558492);
    this.rvf_thumbnail.setOnClickListener(this);
    this.rvf_no_item = (ImageView)findViewById(2131558491);
    this.rvf_smart_panel = (LinearLayout)findViewById(2131558694);
    this.rvf_smart_panel.setOnClickListener(this);
    this.rvf_smart_panel_mode = (WheelView)findViewById(2131558695);
    this.rvf_smart_panel_mode.addScrollingListener(this);
    this.rvf_smart_panel_mode.addClickingListener(this);
    this.rvf_smart_panel_shutterSpeed = (WheelView)findViewById(2131558696);
    this.rvf_smart_panel_shutterSpeed.addScrollingListener(this);
    this.rvf_smart_panel_shutterSpeed.addClickingListener(this);
    this.rvf_smart_panel_aperture = (WheelView)findViewById(2131558697);
    this.rvf_smart_panel_aperture.addScrollingListener(this);
    this.rvf_smart_panel_aperture.addClickingListener(this);
    this.rvf_smart_panel_ev = (WheelView)findViewById(2131558698);
    this.rvf_smart_panel_ev.addScrollingListener(this);
    this.rvf_smart_panel_ev.addClickingListener(this);
    this.rvf_smart_panel_iso = (WheelView)findViewById(2131558699);
    this.rvf_smart_panel_iso.addScrollingListener(this);
    this.rvf_smart_panel_iso.addClickingListener(this);
    this.rvf_smart_panel_wb = (ImageButton)findViewById(2131558700);
    this.rvf_smart_panel_wb.setOnClickListener(this);
    this.rvf_smart_panel_metering = (ImageButton)findViewById(2131558701);
    this.rvf_smart_panel_metering.setOnClickListener(this);
    this.rvf_smart_panel_photoSize = (ImageButton)findViewById(2131558705);
    this.rvf_smart_panel_photoSize.setOnClickListener(this);
    this.rvf_smart_panel_quality = (ImageButton)findViewById(2131558706);
    this.rvf_smart_panel_quality.setOnClickListener(this);
    this.rvf_smart_panel_movieSize = (ImageButton)findViewById(2131558707);
    this.rvf_smart_panel_movieSize.setOnClickListener(this);
    this.rvf_smart_panel_afMode = (ImageButton)findViewById(2131558704);
    this.rvf_smart_panel_afMode.setOnClickListener(this);
    this.rvf_smart_panel_drive = (ImageButton)findViewById(2131558702);
    this.rvf_smart_panel_drive.setOnClickListener(this);
    this.rvf_smart_panel_flash = (ImageButton)findViewById(2131558703);
    this.rvf_smart_panel_flash.setOnClickListener(this);
    this.rvf_smart_panel_afArea = (ImageButton)findViewById(2131558709);
    this.rvf_smart_panel_afArea.setOnClickListener(this);
    this.rvf_smart_panel_touchAF = (ImageButton)findViewById(2131558710);
    this.rvf_smart_panel_touchAF.setOnClickListener(this);
    this.rvf_smart_panel_save = (ImageButton)findViewById(2131558711);
    this.rvf_smart_panel_save.setOnClickListener(this);
    this.rvf_smart_panel_streaming_quality = (ImageButton)findViewById(2131558708);
    this.rvf_smart_panel_streaming_quality.setOnClickListener(this);
    this.rvf_toast_message = (TextView)findViewById(2131558714);
    this.rvf_progress = (ProgressBar)findViewById(2131558476);
  }
  
  private String setGpsValue(Location paramLocation) {
    String str1;
    int i = (int)(paramLocation.getLatitude() * 3600.0D);
    int j = (int)(paramLocation.getLongitude() * 3600.0D);
    if (i >= 0) {
      str1 = "N";
    } else {
      str1 = "S";
      i *= -1;
    } 
    if (j >= 0) {
      String str = "E";
      return String.valueOf(str1) + i + "X" + str + j;
    } 
    String str2 = "W";
    j *= -1;
    return String.valueOf(str1) + i + "X" + str2 + j;
  }
  
  private void setImageBitmapOfGalleryButton() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mConfigurationManager : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/configuration/manager/SRVFConfigurationManager;
    //   4: invokevirtual getLastSaveFileName : ()Ljava/lang/String;
    //   7: astore_3
    //   8: aload_0
    //   9: aload_3
    //   10: invokespecial fileExist : (Ljava/lang/String;)Z
    //   13: ifeq -> 59
    //   16: aload_3
    //   17: astore_2
    //   18: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.thumbnail_bmp : Landroid/graphics/Bitmap;
    //   21: ifnonnull -> 34
    //   24: aload_0
    //   25: aload_3
    //   26: invokespecial getThumbnail : (Ljava/lang/String;)Landroid/graphics/Bitmap;
    //   29: putstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.thumbnail_bmp : Landroid/graphics/Bitmap;
    //   32: aload_3
    //   33: astore_2
    //   34: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.thumbnail_bmp : Landroid/graphics/Bitmap;
    //   37: ifnull -> 209
    //   40: aload_0
    //   41: getfield rvf_thumbnail : Landroid/widget/ImageView;
    //   44: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.thumbnail_bmp : Landroid/graphics/Bitmap;
    //   47: invokevirtual setImageBitmap : (Landroid/graphics/Bitmap;)V
    //   50: aload_0
    //   51: getfield mConfigurationManager : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/configuration/manager/SRVFConfigurationManager;
    //   54: aload_2
    //   55: invokevirtual setLastSaveFileName : (Ljava/lang/String;)V
    //   58: return
    //   59: aconst_null
    //   60: putstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.thumbnail_bmp : Landroid/graphics/Bitmap;
    //   63: new java/util/ArrayList
    //   66: dup
    //   67: invokespecial <init> : ()V
    //   70: astore #4
    //   72: aload_0
    //   73: invokestatic getDefaultStorage : ()Ljava/lang/String;
    //   76: invokespecial getFileList : (Ljava/lang/String;)[Ljava/io/File;
    //   79: astore #5
    //   81: aload_3
    //   82: astore_2
    //   83: aload #5
    //   85: ifnull -> 34
    //   88: aload_3
    //   89: astore_2
    //   90: aload #5
    //   92: arraylength
    //   93: ifle -> 34
    //   96: iconst_0
    //   97: istore_1
    //   98: iload_1
    //   99: aload #5
    //   101: arraylength
    //   102: if_icmplt -> 182
    //   105: aload #4
    //   107: aload_3
    //   108: invokevirtual add : (Ljava/lang/Object;)Z
    //   111: pop
    //   112: aload #4
    //   114: invokevirtual toArray : ()[Ljava/lang/Object;
    //   117: astore #4
    //   119: aload #4
    //   121: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$17
    //   124: dup
    //   125: aload_0
    //   126: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
    //   129: invokestatic sort : ([Ljava/lang/Object;Ljava/util/Comparator;)V
    //   132: iconst_0
    //   133: istore_1
    //   134: aload_3
    //   135: astore_2
    //   136: iload_1
    //   137: aload #4
    //   139: arraylength
    //   140: if_icmpge -> 34
    //   143: aload #4
    //   145: iload_1
    //   146: aaload
    //   147: invokevirtual toString : ()Ljava/lang/String;
    //   150: aload_3
    //   151: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   154: ifeq -> 202
    //   157: iload_1
    //   158: ifle -> 202
    //   161: aload #4
    //   163: iload_1
    //   164: iconst_1
    //   165: isub
    //   166: aaload
    //   167: invokevirtual toString : ()Ljava/lang/String;
    //   170: astore_2
    //   171: aload_0
    //   172: aload_2
    //   173: invokespecial getThumbnail : (Ljava/lang/String;)Landroid/graphics/Bitmap;
    //   176: putstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.thumbnail_bmp : Landroid/graphics/Bitmap;
    //   179: goto -> 34
    //   182: aload #4
    //   184: aload #5
    //   186: iload_1
    //   187: aaload
    //   188: invokevirtual toString : ()Ljava/lang/String;
    //   191: invokevirtual add : (Ljava/lang/Object;)Z
    //   194: pop
    //   195: iload_1
    //   196: iconst_1
    //   197: iadd
    //   198: istore_1
    //   199: goto -> 98
    //   202: iload_1
    //   203: iconst_1
    //   204: iadd
    //   205: istore_1
    //   206: goto -> 134
    //   209: ldc_w 'no_file'
    //   212: astore_2
    //   213: aload_0
    //   214: getfield rvf_thumbnail : Landroid/widget/ImageView;
    //   217: aconst_null
    //   218: invokevirtual setImageBitmap : (Landroid/graphics/Bitmap;)V
    //   221: goto -> 50
  }
  
  private void setImageResource(ImageButton paramImageButton) {
    String str;
    Trace.d(this.TAG, "start setImageResource()");
    switch (paramImageButton.getId()) {
      default:
        return;
      case 2131558700:
        if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("AUTO")) {
          paramImageButton.setImageResource(2130838048);
          return;
        } 
        if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("DAYLIGHT")) {
          paramImageButton.setImageResource(2130838052);
          return;
        } 
        if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("CLOUDY")) {
          paramImageButton.setImageResource(2130838049);
          return;
        } 
        if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("FLUORESCENT_W")) {
          paramImageButton.setImageResource(2130838058);
          return;
        } 
        if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("FLUORESCENT_N") || this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("FLUORESCENT_NW")) {
          paramImageButton.setImageResource(2130838057);
          return;
        } 
        if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("FLUORESCENT_D")) {
          paramImageButton.setImageResource(2130838054);
          return;
        } 
        if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("TUNGSTEN")) {
          paramImageButton.setImageResource(2130838059);
          return;
        } 
        if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("FLASHWB")) {
          paramImageButton.setImageResource(2130838053);
          return;
        } 
        if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("COLORTEMP")) {
          paramImageButton.setImageResource(2130838050);
          return;
        } 
      case 2131558677:
        if (!this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("AUTO")) {
          if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("DAYLIGHT")) {
            paramImageButton.setBackgroundResource(2130838152);
            return;
          } 
          if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("CLOUDY")) {
            paramImageButton.setBackgroundResource(2130838150);
            return;
          } 
          if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("FLUORESCENT_W")) {
            paramImageButton.setBackgroundResource(2130838156);
            return;
          } 
          if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("FLUORESCENT_N") || this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("FLUORESCENT_NW")) {
            paramImageButton.setBackgroundResource(2130838155);
            return;
          } 
          if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("FLUORESCENT_D")) {
            paramImageButton.setBackgroundResource(2130838154);
            return;
          } 
          if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("TUNGSTEN")) {
            paramImageButton.setBackgroundResource(2130838157);
            return;
          } 
          if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("FLASHWB")) {
            paramImageButton.setBackgroundResource(2130838153);
            return;
          } 
          if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("COLORTEMP")) {
            paramImageButton.setBackgroundResource(2130838151);
            return;
          } 
        } 
      case 2131558701:
        if (this.mDeviceController.getMeteringValue().toUpperCase(Locale.ENGLISH).equals("MULTI")) {
          paramImageButton.setImageResource(2130837980);
          return;
        } 
        if (this.mDeviceController.getMeteringValue().toUpperCase(Locale.ENGLISH).equals("CENTERWEIGHTED") || this.mDeviceController.getMeteringValue().toUpperCase(Locale.ENGLISH).equals("CENTER-WEIGHTED")) {
          paramImageButton.setImageResource(2130837979);
          return;
        } 
        if (this.mDeviceController.getMeteringValue().toUpperCase(Locale.ENGLISH).equals("SPOT")) {
          paramImageButton.setImageResource(2130837981);
          return;
        } 
      case 2131558678:
        if (!this.mDeviceController.getMeteringValue().toUpperCase(Locale.ENGLISH).equals("MULTI")) {
          if (this.mDeviceController.getMeteringValue().toUpperCase(Locale.ENGLISH).equals("CENTERWEIGHTED") || this.mDeviceController.getMeteringValue().toUpperCase(Locale.ENGLISH).equals("CENTER-WEIGHTED")) {
            paramImageButton.setBackgroundResource(2130838089);
            return;
          } 
          if (this.mDeviceController.getMeteringValue().toUpperCase(Locale.ENGLISH).equals("SPOT")) {
            paramImageButton.setBackgroundResource(2130838090);
            return;
          } 
        } 
      case 2131558706:
        if (this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).equals("SUPER FINE")) {
          paramImageButton.setImageResource(2130838000);
          return;
        } 
        if (this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).equals("FINE")) {
          paramImageButton.setImageResource(2130837996);
          return;
        } 
        if (this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).equals("NORMAL")) {
          paramImageButton.setImageResource(2130837997);
          return;
        } 
        if (this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).equals("RAW")) {
          paramImageButton.setImageResource(2130837998);
          return;
        } 
        if (this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).equals("RAW+JPEG")) {
          paramImageButton.setImageResource(2130837999);
          return;
        } 
      case 2131558682:
        if (!this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).equals("SUPER FINE") && !this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).equals("FINE") && !this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).equals("NORMAL")) {
          if (this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).equals("RAW")) {
            paramImageButton.setBackgroundResource(2130838104);
            return;
          } 
          if (this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).equals("RAW+JPEG")) {
            paramImageButton.setBackgroundResource(2130838105);
            return;
          } 
        } 
      case 2131558707:
        str = this.mDeviceController.getMovieResolutionValue();
        if (this.mDeviceController.getVideoOutValue().toUpperCase(Locale.ENGLISH).equals("NTSC")) {
          if (str.equals("1920x1080 (15p)")) {
            paramImageButton.setImageResource(2130837982);
            return;
          } 
          if (str.equals("1920x1080 (30p)")) {
            paramImageButton.setImageResource(2130837984);
            return;
          } 
          if (str.equals("1920x1080 (60p)")) {
            paramImageButton.setImageResource(2130837985);
            return;
          } 
          if (str.equals("1920x810 (24p)")) {
            paramImageButton.setImageResource(2130837995);
            return;
          } 
          if (str.equals("1280x720 (30p)")) {
            paramImageButton.setImageResource(2130837993);
            return;
          } 
          if (str.equals("1280x720 (60p)")) {
            paramImageButton.setImageResource(2130837994);
            return;
          } 
          if (str.equals("640x480 (30p)")) {
            paramImageButton.setImageResource(2130837990);
            return;
          } 
          if (str.equals("320x240 (30p)") || str.equals("For Sharing")) {
            paramImageButton.setImageResource(2130837987);
            return;
          } 
        } 
        if (this.mDeviceController.getVideoOutValue().toUpperCase(Locale.ENGLISH).equals("PAL")) {
          if (str.equals("1920x1080 (25p)")) {
            paramImageButton.setImageResource(2130837983);
            return;
          } 
          if (str.equals("1280x720 (25p)")) {
            paramImageButton.setImageResource(2130837992);
            return;
          } 
          if (str.equals("640x480 (25p)")) {
            paramImageButton.setImageResource(2130837989);
            return;
          } 
          if (str.equals("For Sharing")) {
            paramImageButton.setImageResource(2130837987);
            return;
          } 
        } 
      case 2131558683:
        str = this.mDeviceController.getMovieResolutionValue();
        if (this.mDeviceController.getVideoOutValue().toUpperCase(Locale.ENGLISH).equals("NTSC")) {
          if (str.equals("1920x1080 (15p)")) {
            paramImageButton.setBackgroundResource(2130838091);
            return;
          } 
          if (str.equals("1920x1080 (30p)")) {
            paramImageButton.setBackgroundResource(2130838093);
            return;
          } 
          if (str.equals("1920x1080 (60p)")) {
            paramImageButton.setBackgroundResource(2130838094);
            return;
          } 
          if (str.equals("1920x810 (24p)")) {
            paramImageButton.setBackgroundResource(2130838102);
            return;
          } 
          if (str.equals("1280x720 (30p)")) {
            paramImageButton.setBackgroundResource(2130838100);
            return;
          } 
          if (str.equals("1280x720 (60p)")) {
            paramImageButton.setBackgroundResource(2130838101);
            return;
          } 
          if (str.equals("640x480 (30p)")) {
            paramImageButton.setBackgroundResource(2130838098);
            return;
          } 
          if (str.equals("320x240 (30p)") || str.equals("For Sharing")) {
            paramImageButton.setBackgroundResource(2130838096);
            return;
          } 
        } 
        if (this.mDeviceController.getVideoOutValue().toUpperCase(Locale.ENGLISH).equals("PAL")) {
          if (str.equals("1920x1080 (25p)")) {
            paramImageButton.setBackgroundResource(2130838092);
            return;
          } 
          if (str.equals("1280x720 (25p)")) {
            paramImageButton.setBackgroundResource(2130838099);
            return;
          } 
          if (str.equals("640x480 (25p)")) {
            paramImageButton.setBackgroundResource(2130838097);
            return;
          } 
          if (str.equals("For Sharing")) {
            paramImageButton.setBackgroundResource(2130838096);
            return;
          } 
        } 
      case 2131558684:
        str = this.mDeviceController.getAFAreaValue().toUpperCase(Locale.ENGLISH);
        if (str.equals("SELECTION AF")) {
          paramImageButton.setBackgroundResource(2130838078);
          return;
        } 
        if (str.equals("MULTI AF")) {
          paramImageButton.setBackgroundResource(2130838077);
          return;
        } 
      case 2131558685:
        if (isDimMenu(this.rvf_indicator_touch_af)) {
          paramImageButton.setBackgroundResource(2130838147);
          return;
        } 
        str = this.mDeviceController.getTouchAFValue().toUpperCase(Locale.ENGLISH);
        if (str.equals("OFF")) {
          paramImageButton.setBackgroundResource(2130838147);
          return;
        } 
        if (str.equals("TOUCH AF")) {
          paramImageButton.setBackgroundResource(2130838149);
          return;
        } 
        if (str.equals("AF POINT")) {
          paramImageButton.setBackgroundResource(2130838146);
          return;
        } 
        if (str.equals("ONE TOUCH SHOT")) {
          paramImageButton.setBackgroundResource(2130838148);
          return;
        } 
      case 2131558702:
        str = this.mDeviceController.getDriveValue();
        if (str.toUpperCase(Locale.ENGLISH).equals("SINGLE")) {
          paramImageButton.setImageResource(2130837969);
          return;
        } 
        if (str.toUpperCase(Locale.ENGLISH).equals("CONTINUOUS")) {
          paramImageButton.setImageResource(2130837966);
          return;
        } 
        if (str.toUpperCase(Locale.ENGLISH).equals("TIMER (2SEC)")) {
          paramImageButton.setImageResource(2130838039);
          return;
        } 
        if (str.toUpperCase(Locale.ENGLISH).equals("TIMER (5SEC)")) {
          paramImageButton.setImageResource(2130838041);
          return;
        } 
        if (str.toUpperCase(Locale.ENGLISH).equals("TIMER (10SEC)")) {
          paramImageButton.setImageResource(2130838038);
          return;
        } 
        if (str.toUpperCase(Locale.ENGLISH).equals("TIMER (DOUBLE)")) {
          paramImageButton.setImageResource(2130838042);
          return;
        } 
      case 2131558679:
        str = this.mDeviceController.getDriveValue();
        if (!str.toUpperCase(Locale.ENGLISH).equals("SINGLE")) {
          if (str.toUpperCase(Locale.ENGLISH).equals("CONTINUOUS")) {
            paramImageButton.setBackgroundResource(2130838079);
            return;
          } 
          if (str.toUpperCase(Locale.ENGLISH).equals("TIMER (2SEC)")) {
            paramImageButton.setBackgroundResource(2130838141);
            return;
          } 
          if (str.toUpperCase(Locale.ENGLISH).equals("TIMER (5SEC)")) {
            paramImageButton.setBackgroundResource(2130838143);
            return;
          } 
          if (str.toUpperCase(Locale.ENGLISH).equals("TIMER (10SEC)")) {
            paramImageButton.setBackgroundResource(2130838140);
            return;
          } 
          if (str.toUpperCase(Locale.ENGLISH).equals("TIMER (30SEC)")) {
            paramImageButton.setBackgroundResource(2130838142);
            return;
          } 
          if (str.toUpperCase(Locale.ENGLISH).equals("TIMER (DOUBLE)")) {
            paramImageButton.setBackgroundResource(2130838144);
            return;
          } 
        } 
      case 2131558466:
        if (this.mDeviceController.getDefaultFlash().toUpperCase(Locale.ENGLISH).equals("OFF") || this.mDeviceController.getDefaultFlash().toUpperCase(Locale.ENGLISH).equals("") || this.mDeviceController.getFlashStrobeStatus().toUpperCase(Locale.ENGLISH).equals("DETACHED")) {
          paramImageButton.setBackgroundResource(2130837974);
          return;
        } 
        str = this.mDeviceController.getCurrentFlashDisplay().toUpperCase(Locale.ENGLISH);
        Trace.d(this.TAG, "defaultFlash : " + str);
        if (str.equals("OFF")) {
          paramImageButton.setBackgroundResource(2130837974);
          return;
        } 
        if (str.equals("AUTO")) {
          paramImageButton.setBackgroundResource(2130837973);
          return;
        } 
        if (str.equals("REDEYE")) {
          paramImageButton.setBackgroundResource(2130837976);
          return;
        } 
        if (str.equals("FILLIN") || str.equals("FILL-IN") || str.equals("FILL IN")) {
          paramImageButton.setBackgroundResource(2130837972);
          return;
        } 
        if (str.equals("SLOWSYNC")) {
          paramImageButton.setBackgroundResource(2130837978);
          return;
        } 
        if (str.equals("REDEYEFIX")) {
          paramImageButton.setBackgroundResource(2130837977);
          return;
        } 
        if (str.equals("FILLINREDEYE") || str.equals("FILL-IN_RED") || str.equals("FILL-IN RED")) {
          paramImageButton.setBackgroundResource(2130837975);
          return;
        } 
        if (str.equals("1STCURTAIN") || str.equals("1ST_CURTAIN") || str.equals("1ST CURTAIN")) {
          paramImageButton.setBackgroundResource(2130837970);
          return;
        } 
        if (str.equals("2NDCURTAIN") || str.equals("2ND_CURTAIN") || str.equals("2ND CURTAIN")) {
          paramImageButton.setBackgroundResource(2130837971);
          return;
        } 
      case 2131558680:
        if (!this.mDeviceController.getDefaultFlash().toUpperCase(Locale.ENGLISH).equals("OFF") && !this.mDeviceController.getDefaultFlash().toUpperCase(Locale.ENGLISH).equals("")) {
          str = this.mDeviceController.getCurrentFlashDisplay().toUpperCase(Locale.ENGLISH);
          Trace.d(this.TAG, "defaultFlash : " + str);
          if (!str.equals("OFF")) {
            if (str.equals("AUTO")) {
              paramImageButton.setBackgroundResource(2130838082);
              return;
            } 
            if (str.equals("REDEYE")) {
              paramImageButton.setBackgroundResource(2130838085);
              return;
            } 
            if (str.equals("FILLIN") || str.equals("FILL-IN") || str.equals("FILL IN")) {
              paramImageButton.setBackgroundResource(2130838083);
              return;
            } 
            if (str.equals("SLOWSYNC")) {
              paramImageButton.setBackgroundResource(2130838087);
              return;
            } 
            if (str.equals("REDEYEFIX")) {
              paramImageButton.setBackgroundResource(2130838086);
              return;
            } 
            if (str.equals("FILLINREDEYE") || str.equals("FILL-IN_RED") || str.equals("FILL-IN RED")) {
              paramImageButton.setBackgroundResource(2130838085);
              return;
            } 
            if (str.equals("1STCURTAIN") || str.equals("1ST_CURTAIN") || str.equals("1ST CURTAIN")) {
              paramImageButton.setBackgroundResource(2130838080);
              return;
            } 
            if (str.equals("2NDCURTAIN") || str.equals("2ND_CURTAIN") || str.equals("2ND CURTAIN")) {
              paramImageButton.setBackgroundResource(2130838081);
              return;
            } 
          } 
        } 
      case 2131558703:
        if (this.mDeviceController.getDefaultFlash().toUpperCase(Locale.ENGLISH).equals("OFF") || this.mDeviceController.getDefaultFlash().toUpperCase(Locale.ENGLISH).equals("") || this.mDeviceController.getFlashStrobeStatus().toUpperCase(Locale.ENGLISH).equals("DETACHED")) {
          paramImageButton.setImageResource(2130837974);
          return;
        } 
        str = this.mDeviceController.getCurrentFlashDisplay().toUpperCase(Locale.ENGLISH);
        Trace.d(this.TAG, "defaultFlash : " + str);
        if (str.equals("OFF")) {
          paramImageButton.setImageResource(2130837974);
          return;
        } 
        if (str.equals("AUTO")) {
          paramImageButton.setImageResource(2130837973);
          return;
        } 
        if (str.equals("REDEYE")) {
          paramImageButton.setImageResource(2130837976);
          return;
        } 
        if (str.equals("FILLIN") || str.equals("FILL-IN") || str.equals("FILL IN")) {
          paramImageButton.setImageResource(2130837972);
          return;
        } 
        if (str.equals("SLOWSYNC")) {
          paramImageButton.setImageResource(2130837978);
          return;
        } 
        if (str.equals("REDEYEFIX")) {
          paramImageButton.setImageResource(2130837977);
          return;
        } 
        if (str.equals("FILLINREDEYE") || str.equals("FILL-IN_RED") || str.equals("FILL-IN RED")) {
          paramImageButton.setImageResource(2130837975);
          return;
        } 
        if (str.equals("1STCURTAIN") || str.equals("1ST_CURTAIN") || str.equals("1ST CURTAIN")) {
          paramImageButton.setImageResource(2130837970);
          return;
        } 
        if (str.equals("2NDCURTAIN") || str.equals("2ND_CURTAIN") || str.equals("2ND CURTAIN")) {
          paramImageButton.setImageResource(2130837971);
          return;
        } 
      case 2131558467:
        if (this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("OFF")) {
          paramImageButton.setBackgroundResource(2130838043);
          return;
        } 
        if (this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("2SEC")) {
          paramImageButton.setBackgroundResource(2130838039);
          return;
        } 
        if (this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("5SEC")) {
          paramImageButton.setBackgroundResource(2130838041);
          return;
        } 
        if (this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("10SEC")) {
          paramImageButton.setBackgroundResource(2130838038);
          return;
        } 
        if (this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("DOUBLE")) {
          paramImageButton.setBackgroundResource(2130838042);
          return;
        } 
      case 2131558468:
        if (this.mDeviceController.getResolutionMenuItems().size() > 0) {
          DSCResolution dSCResolution = this.mDeviceController.getResolutionMenuItems().get(Integer.parseInt(this.mDeviceController.getDefaultResolutionIndex()));
          if (dSCResolution != null) {
            String str1 = Utils.unitChange(this, dSCResolution.getWidth(), dSCResolution.getHeight(), FunctionManager.getPhotoSizePresentationType(this.connectedSsid));
            if (str1.equals("1M")) {
              paramImageButton.setBackgroundResource(2130838018);
              return;
            } 
            if (str1.equals("1.1M")) {
              paramImageButton.setBackgroundResource(2130838017);
              return;
            } 
            if (str1.equals("2M")) {
              paramImageButton.setBackgroundResource(2130838021);
              return;
            } 
            if (str1.equals("2.1M")) {
              paramImageButton.setBackgroundResource(2130838020);
              return;
            } 
            if (str1.equals("W2M")) {
              paramImageButton.setBackgroundResource(2130838022);
              return;
            } 
            if (str1.equals("3M")) {
              paramImageButton.setBackgroundResource(2130838023);
              return;
            } 
            if (str1.equals("4M")) {
              paramImageButton.setBackgroundResource(2130838026);
              return;
            } 
            if (str1.equals("4.9M")) {
              paramImageButton.setBackgroundResource(2130838025);
              return;
            } 
            if (str1.equals("W4M")) {
              paramImageButton.setBackgroundResource(2130838027);
              return;
            } 
            if (str1.equals("5M")) {
              paramImageButton.setBackgroundResource(2130838029);
              return;
            } 
            if (str1.equals("5.9M")) {
              paramImageButton.setBackgroundResource(2130838028);
              return;
            } 
            if (str1.equals("7M")) {
              paramImageButton.setBackgroundResource(2130838031);
              return;
            } 
            if (str1.equals("7.8M")) {
              paramImageButton.setBackgroundResource(2130838030);
              return;
            } 
            if (str1.equals("W7M")) {
              paramImageButton.setBackgroundResource(2130838032);
              return;
            } 
            if (str1.equals("8M")) {
              paramImageButton.setBackgroundResource(2130838033);
              return;
            } 
            if (str1.equals("9M")) {
              paramImageButton.setBackgroundResource(2130838034);
              return;
            } 
            if (str1.equals("10M")) {
              paramImageButton.setBackgroundResource(2130838008);
              return;
            } 
            if (str1.equals("10.1M")) {
              paramImageButton.setBackgroundResource(2130838007);
              return;
            } 
            if (str1.equals("W10M")) {
              paramImageButton.setBackgroundResource(2130838009);
              return;
            } 
            if (str1.equals("W12M")) {
              paramImageButton.setBackgroundResource(2130838010);
              return;
            } 
            if (str1.equals("13M")) {
              paramImageButton.setBackgroundResource(2130838012);
              return;
            } 
            if (str1.equals("13.3M")) {
              paramImageButton.setBackgroundResource(2130838011);
              return;
            } 
            if (str1.equals("P14M")) {
              paramImageButton.setBackgroundResource(2130838013);
              return;
            } 
            if (str1.equals("16M")) {
              paramImageButton.setBackgroundResource(2130838015);
              return;
            } 
            if (str1.equals("16.9M")) {
              paramImageButton.setBackgroundResource(2130838014);
              return;
            } 
            if (str1.equals("W16M")) {
              paramImageButton.setBackgroundResource(2130838016);
              return;
            } 
            if (str1.equals("20M")) {
              paramImageButton.setBackgroundResource(2130838019);
              return;
            } 
            paramImageButton.setBackgroundResource(2130838008);
            return;
          } 
        } 
        paramImageButton.setBackgroundResource(2130838008);
        return;
      case 2131558681:
        if (isDimMenu(this.rvf_indicator_photo_size) || (!this.mDeviceController.getDialModeValue().equals("Auto") && this.mDeviceController.getQualityValue().equals("RAW"))) {
          paramImageButton.setBackgroundResource(2130838119);
          return;
        } 
        if (this.mDeviceController.getResolutionMenuItems().size() > 0) {
          DSCResolution dSCResolution = this.mDeviceController.getResolutionMenuItems().get(Integer.parseInt(this.mDeviceController.getDefaultResolutionIndex()));
          if (dSCResolution != null) {
            String str1 = Utils.unitChange(this, dSCResolution.getWidth(), dSCResolution.getHeight(), FunctionManager.getPhotoSizePresentationType(this.connectedSsid));
            if (str1.equals("1M")) {
              paramImageButton.setBackgroundResource(2130838118);
              return;
            } 
            if (str1.equals("1.1M")) {
              paramImageButton.setBackgroundResource(2130838117);
              return;
            } 
            if (str1.equals("2M")) {
              paramImageButton.setBackgroundResource(2130838122);
              return;
            } 
            if (str1.equals("2.1M")) {
              paramImageButton.setBackgroundResource(2130838120);
              return;
            } 
            if (str1.equals("W2M")) {
              paramImageButton.setBackgroundResource(2130838123);
              return;
            } 
            if (str1.equals("3M")) {
              paramImageButton.setBackgroundResource(2130838124);
              return;
            } 
            if (str1.equals("4M")) {
              paramImageButton.setBackgroundResource(2130838127);
              return;
            } 
            if (str1.equals("W4M")) {
              paramImageButton.setBackgroundResource(2130838128);
              return;
            } 
            if (str1.equals("4.9M")) {
              paramImageButton.setBackgroundResource(2130838125);
              return;
            } 
            if (str1.equals("5M")) {
              paramImageButton.setBackgroundResource(2130838130);
              return;
            } 
            if (str1.equals("5.9M")) {
              paramImageButton.setBackgroundResource(2130838129);
              return;
            } 
            if (str1.equals("7M")) {
              paramImageButton.setBackgroundResource(2130838133);
              return;
            } 
            if (str1.equals("7.8M")) {
              paramImageButton.setBackgroundResource(2130838131);
              return;
            } 
            if (str1.equals("W7M")) {
              paramImageButton.setBackgroundResource(2130838134);
              return;
            } 
            if (str1.equals("8M")) {
              paramImageButton.setBackgroundResource(2130838135);
              return;
            } 
            if (str1.equals("9M")) {
              paramImageButton.setBackgroundResource(2130838136);
              return;
            } 
            if (str1.equals("10M")) {
              paramImageButton.setBackgroundResource(2130838107);
              return;
            } 
            if (str1.equals("10.1M")) {
              paramImageButton.setBackgroundResource(2130838106);
              return;
            } 
            if (str1.equals("W10M")) {
              paramImageButton.setBackgroundResource(2130838108);
              return;
            } 
            if (str1.equals("W12M")) {
              paramImageButton.setBackgroundResource(2130838109);
              return;
            } 
            if (str1.equals("13M")) {
              paramImageButton.setBackgroundResource(2130838111);
              return;
            } 
            if (str1.equals("13.3M")) {
              paramImageButton.setBackgroundResource(2130838110);
              return;
            } 
            if (str1.equals("P14M")) {
              paramImageButton.setBackgroundResource(2130838112);
              return;
            } 
            if (str1.equals("16M")) {
              paramImageButton.setBackgroundResource(2130838115);
              return;
            } 
            if (str1.equals("16.9M")) {
              paramImageButton.setBackgroundResource(2130838113);
              return;
            } 
            if (str1.equals("W16M")) {
              paramImageButton.setBackgroundResource(2130838116);
              return;
            } 
            if (str1.equals("20M")) {
              paramImageButton.setBackgroundResource(2130838119);
              return;
            } 
          } 
        } 
      case 2131558705:
        if (!this.mDeviceController.getDialModeValue().equals("Auto") && this.mDeviceController.getQualityValue().equals("RAW")) {
          this.rvf_smart_panel_photoSize.setImageResource(2130837819);
          return;
        } 
        if (this.mDeviceController.getResolutionMenuItems().size() > 0) {
          DSCResolution dSCResolution = this.mDeviceController.getResolutionMenuItems().get(Integer.parseInt(this.mDeviceController.getDefaultResolutionIndex()));
          if (dSCResolution != null) {
            String str1 = Utils.unitChange(this, dSCResolution.getWidth(), dSCResolution.getHeight(), FunctionManager.getPhotoSizePresentationType(this.connectedSsid));
            if (str1.equals("1M")) {
              paramImageButton.setImageResource(2130838018);
              return;
            } 
            if (str1.equals("1.1M")) {
              paramImageButton.setImageResource(2130838017);
              return;
            } 
            if (str1.equals("2M")) {
              paramImageButton.setImageResource(2130838021);
              return;
            } 
            if (str1.equals("2.1M")) {
              paramImageButton.setImageResource(2130838020);
              return;
            } 
            if (str1.equals("W2M")) {
              paramImageButton.setImageResource(2130838022);
              return;
            } 
            if (str1.equals("3M")) {
              paramImageButton.setImageResource(2130838023);
              return;
            } 
            if (str1.equals("4M")) {
              paramImageButton.setImageResource(2130838026);
              return;
            } 
            if (str1.equals("4.9M")) {
              paramImageButton.setImageResource(2130838025);
              return;
            } 
            if (str1.equals("W4M")) {
              paramImageButton.setImageResource(2130838027);
              return;
            } 
            if (str1.equals("5M")) {
              paramImageButton.setImageResource(2130838029);
              return;
            } 
            if (str1.equals("5.9M")) {
              paramImageButton.setImageResource(2130838028);
              return;
            } 
            if (str1.equals("7M")) {
              paramImageButton.setImageResource(2130838031);
              return;
            } 
            if (str1.equals("7.8M")) {
              paramImageButton.setImageResource(2130838030);
              return;
            } 
            if (str1.equals("W7M")) {
              paramImageButton.setImageResource(2130838032);
              return;
            } 
            if (str1.equals("8M")) {
              paramImageButton.setImageResource(2130838033);
              return;
            } 
            if (str1.equals("9M")) {
              paramImageButton.setImageResource(2130838034);
              return;
            } 
            if (str1.equals("10M")) {
              paramImageButton.setImageResource(2130838008);
              return;
            } 
            if (str1.equals("10.1M")) {
              paramImageButton.setImageResource(2130838007);
              return;
            } 
            if (str1.equals("W10M")) {
              paramImageButton.setImageResource(2130838009);
              return;
            } 
            if (str1.equals("W12M")) {
              paramImageButton.setImageResource(2130838010);
              return;
            } 
            if (str1.equals("13M")) {
              paramImageButton.setImageResource(2130838012);
              return;
            } 
            if (str1.equals("13.3M")) {
              paramImageButton.setImageResource(2130838011);
              return;
            } 
            if (str1.equals("P14M")) {
              paramImageButton.setImageResource(2130838013);
              return;
            } 
            if (str1.equals("16M")) {
              paramImageButton.setImageResource(2130838015);
              return;
            } 
            if (str1.equals("16.9M")) {
              paramImageButton.setImageResource(2130838014);
              return;
            } 
            if (str1.equals("W16M")) {
              paramImageButton.setImageResource(2130838016);
              return;
            } 
            if (str1.equals("20M")) {
              paramImageButton.setImageResource(2130838019);
              return;
            } 
            paramImageButton.setImageResource(2130838008);
            return;
          } 
        } 
        paramImageButton.setImageResource(2130838008);
        return;
      case 2131558469:
        if (this.nCameraSaveSelect == 0) {
          paramImageButton.setBackgroundResource(2130838036);
          return;
        } 
        if (this.nCameraSaveSelect == 1) {
          paramImageButton.setBackgroundResource(2130838035);
          return;
        } 
      case 2131558711:
        if (this.mDeviceController.getFileSaveMenuItems().size() > 0) {
          if (this.mDeviceController.getFileSaveValue().equals(this.mDeviceController.getFileSaveMenuItems().get(0))) {
            paramImageButton.setImageResource(2130838036);
            return;
          } 
          if (this.mDeviceController.getFileSaveValue().equals(this.mDeviceController.getFileSaveMenuItems().get(1))) {
            paramImageButton.setImageResource(2130838035);
            return;
          } 
        } 
      case 2131558686:
        if (this.mDeviceController.getCardStatusValue().toUpperCase(Locale.ENGLISH).equals("INTERNAL")) {
          paramImageButton.setBackgroundResource(2130838138);
          return;
        } 
        if (isDimMenu(this.rvf_indicator_storage)) {
          paramImageButton.setBackgroundResource(2130838137);
          return;
        } 
        if (this.mDeviceController.getFileSaveMenuItems().size() > 0) {
          if (this.mDeviceController.getFileSaveValue().equals(this.mDeviceController.getFileSaveMenuItems().get(0))) {
            paramImageButton.setBackgroundResource(2130838139);
            return;
          } 
          if (this.mDeviceController.getFileSaveValue().equals(this.mDeviceController.getFileSaveMenuItems().get(1))) {
            paramImageButton.setBackgroundResource(2130838137);
            return;
          } 
        } 
      case 2131558470:
        paramImageButton.setBackgroundResource(2130837964);
        return;
      case 2131558708:
        if (this.mDeviceController.getCurrentStreamQuality().toUpperCase(Locale.ENGLISH).equals("HIGH")) {
          paramImageButton.setImageResource(2130837991);
          return;
        } 
        if (this.mDeviceController.getCurrentStreamQuality().toUpperCase(Locale.ENGLISH).equals("LOW")) {
          paramImageButton.setImageResource(2130837988);
          return;
        } 
      case 2131558709:
        if (this.mDeviceController.getDefaultFocusState().toUpperCase(Locale.ENGLISH).equals("MF")) {
          if (this.mDeviceController.getAFAreaValue().toUpperCase(Locale.ENGLISH).equals("SELECTION AF")) {
            paramImageButton.setImageResource(2130837586);
            return;
          } 
          if (this.mDeviceController.getAFAreaValue().toUpperCase(Locale.ENGLISH).equals("MULTI AF")) {
            paramImageButton.setImageResource(2130837583);
            return;
          } 
        } 
        if (this.mDeviceController.getAFAreaValue().toUpperCase(Locale.ENGLISH).equals("SELECTION AF")) {
          paramImageButton.setImageResource(2130837953);
          return;
        } 
        if (this.mDeviceController.getAFAreaValue().toUpperCase(Locale.ENGLISH).equals("MULTI AF")) {
          paramImageButton.setImageResource(2130837952);
          return;
        } 
      case 2131558710:
        break;
    } 
    if (this.mDeviceController.getDefaultFocusState().toUpperCase(Locale.ENGLISH).equals("MF")) {
      paramImageButton.setImageResource(2130837899);
      return;
    } 
    if (this.mDeviceController.getTouchAFValue().toUpperCase(Locale.ENGLISH).equals("OFF")) {
      paramImageButton.setImageResource(2130838045);
      return;
    } 
    if (this.mDeviceController.getTouchAFValue().toUpperCase(Locale.ENGLISH).equals("TOUCH AF")) {
      paramImageButton.setImageResource(2130838047);
      return;
    } 
    if (this.mDeviceController.getTouchAFValue().toUpperCase(Locale.ENGLISH).equals("AF POINT")) {
      paramImageButton.setImageResource(2130838044);
      return;
    } 
  }
  
  private void setImageResourceButtonFocus(ImageButton paramImageButton) {
    String str;
    switch (paramImageButton.getId()) {
      default:
        return;
      case 2131558466:
        if (this.mDeviceController.getDefaultFlash().toUpperCase(Locale.ENGLISH).equals("OFF")) {
          paramImageButton.setBackgroundResource(2130837654);
          return;
        } 
        str = this.mDeviceController.getCurrentFlashDisplay().toUpperCase(Locale.ENGLISH);
        Trace.d(this.TAG, "defaultFlash : " + str);
        if (str.equals("OFF")) {
          paramImageButton.setBackgroundResource(2130837654);
          return;
        } 
        if (str.equals("AUTO")) {
          paramImageButton.setBackgroundResource(2130837651);
          return;
        } 
        if (str.equals("REDEYE")) {
          paramImageButton.setBackgroundResource(2130837660);
          return;
        } 
        if (str.equals("FILLIN") || str.equals("FILL-IN") || str.equals("FILL IN")) {
          paramImageButton.setBackgroundResource(2130837648);
          return;
        } 
        if (str.equals("SLOWSYNC")) {
          paramImageButton.setBackgroundResource(2130837666);
          return;
        } 
        if (str.equals("REDEYEFIX")) {
          paramImageButton.setBackgroundResource(2130837663);
          return;
        } 
        if (str.equals("FILLINREDEYE") || str.equals("FILL-IN_RED") || str.equals("FILL-IN RED")) {
          paramImageButton.setBackgroundResource(2130837657);
          return;
        } 
        if (str.equals("1STCURTAIN") || str.equals("1ST_CURTAIN") || str.equals("1ST CURTAIN")) {
          paramImageButton.setBackgroundResource(2130837642);
          return;
        } 
        if (str.equals("2NDCURTAIN") || str.equals("2ND_CURTAIN") || str.equals("2ND CURTAIN")) {
          paramImageButton.setBackgroundResource(2130837645);
          return;
        } 
      case 2131558467:
        if (this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("OFF")) {
          paramImageButton.setBackgroundResource(2130837894);
          return;
        } 
        if (this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("2SEC")) {
          paramImageButton.setBackgroundResource(2130837881);
          return;
        } 
        if (this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("5SEC")) {
          paramImageButton.setBackgroundResource(2130837887);
          return;
        } 
        if (this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("10SEC")) {
          paramImageButton.setBackgroundResource(2130837878);
          return;
        } 
        if (this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("30SEC")) {
          paramImageButton.setBackgroundResource(2130837884);
          return;
        } 
        if (this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("DOUBLE")) {
          paramImageButton.setBackgroundResource(2130837891);
          return;
        } 
      case 2131558468:
        if (this.mDeviceController.getResolutionMenuItems().size() > 0) {
          DSCResolution dSCResolution = this.mDeviceController.getResolutionMenuItems().get(Integer.parseInt(this.mDeviceController.getDefaultResolutionIndex()));
          if (dSCResolution != null) {
            String str1 = Utils.unitChange(this, dSCResolution.getWidth(), dSCResolution.getHeight(), FunctionManager.getPhotoSizePresentationType(this.connectedSsid));
            if (str1.equals("1M")) {
              paramImageButton.setBackgroundResource(2130837817);
              return;
            } 
            if (str1.equals("1.1M")) {
              paramImageButton.setBackgroundResource(2130837814);
              return;
            } 
            if (str1.equals("2M")) {
              paramImageButton.setBackgroundResource(2130837826);
              return;
            } 
            if (str1.equals("2.1M")) {
              paramImageButton.setBackgroundResource(2130837823);
              return;
            } 
            if (str1.equals("W2M")) {
              paramImageButton.setBackgroundResource(2130837829);
              return;
            } 
            if (str1.equals("3M")) {
              paramImageButton.setBackgroundResource(2130837832);
              return;
            } 
            if (str1.equals("4M")) {
              paramImageButton.setBackgroundResource(2130837841);
              return;
            } 
            if (str1.equals("4.8M")) {
              paramImageButton.setBackgroundResource(2130837838);
              return;
            } 
            if (str1.equals("W4M")) {
              paramImageButton.setBackgroundResource(2130837844);
              return;
            } 
            if (str1.equals("5M")) {
              paramImageButton.setBackgroundResource(2130837850);
              return;
            } 
            if (str1.equals("5.9M")) {
              paramImageButton.setBackgroundResource(2130837847);
              return;
            } 
            if (str1.equals("7M")) {
              paramImageButton.setBackgroundResource(2130837856);
              return;
            } 
            if (str1.equals("7.8M")) {
              paramImageButton.setBackgroundResource(2130837853);
              return;
            } 
            if (str1.equals("W7M")) {
              paramImageButton.setBackgroundResource(2130837859);
              return;
            } 
            if (str1.equals("8M")) {
              paramImageButton.setBackgroundResource(2130837862);
              return;
            } 
            if (str1.equals("9M")) {
              paramImageButton.setBackgroundResource(2130837865);
              return;
            } 
            if (str1.equals("10M")) {
              paramImageButton.setBackgroundResource(2130837787);
              return;
            } 
            if (str1.equals("10.1M")) {
              paramImageButton.setBackgroundResource(2130837784);
              return;
            } 
            if (str1.equals("W10M")) {
              paramImageButton.setBackgroundResource(2130837790);
              return;
            } 
            if (str1.equals("W12M")) {
              paramImageButton.setBackgroundResource(2130837793);
              return;
            } 
            if (str1.equals("13M")) {
              paramImageButton.setBackgroundResource(2130837799);
              return;
            } 
            if (str1.equals("13.3M")) {
              paramImageButton.setBackgroundResource(2130837796);
              return;
            } 
            if (str1.equals("P14M")) {
              paramImageButton.setBackgroundResource(2130837802);
              return;
            } 
            if (str1.equals("16M")) {
              paramImageButton.setBackgroundResource(2130837808);
              return;
            } 
            if (str1.equals("16.9M")) {
              paramImageButton.setBackgroundResource(2130837805);
              return;
            } 
            if (str1.equals("W16M")) {
              paramImageButton.setBackgroundResource(2130837811);
              return;
            } 
            if (str1.equals("20M")) {
              paramImageButton.setBackgroundResource(2130837820);
              return;
            } 
            paramImageButton.setBackgroundResource(2130837787);
            return;
          } 
        } 
        paramImageButton.setBackgroundResource(2130837787);
        return;
      case 2131558469:
        if (this.nCameraSaveSelect == 0) {
          paramImageButton.setBackgroundResource(2130837871);
          return;
        } 
        if (this.nCameraSaveSelect == 1) {
          paramImageButton.setBackgroundResource(2130837868);
          return;
        } 
      case 2131558470:
        break;
    } 
    paramImageButton.setBackgroundResource(2130837624);
  }
  
  private void setIndicator() {
    if (this.mDeviceController.getWBValue().equals("") || this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("AUTO")) {
      this.rvf_indicator_wb.setVisibility(8);
    } else {
      setImageResource(this.rvf_indicator_wb);
      this.rvf_indicator_wb.setVisibility(0);
    } 
    if (this.mDeviceController.getMeteringValue().equals("") || this.mDeviceController.getMeteringValue().toUpperCase(Locale.ENGLISH).equals("MULTI")) {
      this.rvf_indicator_metering.setVisibility(8);
    } else {
      setImageResource(this.rvf_indicator_metering);
      this.rvf_indicator_metering.setVisibility(0);
    } 
    if (this.mDeviceController.getDriveValue().equals("") || this.mDeviceController.getDriveValue().toUpperCase(Locale.ENGLISH).equals("SINGLE")) {
      this.rvf_indicator_drive.setVisibility(8);
    } else {
      setImageResource(this.rvf_indicator_drive);
      this.rvf_indicator_drive.setVisibility(0);
    } 
    if (this.mDeviceController.getCurrentFlashDisplay().equals("") || this.mDeviceController.getCurrentFlashDisplay().toUpperCase(Locale.ENGLISH).equals("OFF") || this.mDeviceController.getFlashStrobeStatus().toUpperCase(Locale.ENGLISH).equals("DETACHED")) {
      this.rvf_indicator_flash.setVisibility(8);
    } else {
      setImageResource(this.rvf_indicator_flash);
      this.rvf_indicator_flash.setVisibility(0);
    } 
    setImageResource(this.rvf_indicator_photo_size);
    this.rvf_indicator_photo_size.setVisibility(0);
    if (this.mDeviceController.getQualityValue().equals("") || this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).equals("SUPER FINE") || this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).equals("FINE") || this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).equals("NORMAL") || this.mDeviceController.getQualityMenuItems().size() == 0) {
      this.rvf_indicator_quality.setVisibility(8);
    } else {
      setImageResource(this.rvf_indicator_quality);
      this.rvf_indicator_quality.setVisibility(0);
    } 
    if (this.mDeviceController.getMovieResolutionValue().equals("")) {
      this.rvf_indicator_movie_size.setVisibility(8);
    } else {
      setImageResource(this.rvf_indicator_movie_size);
      this.rvf_indicator_movie_size.setVisibility(0);
    } 
    setImageResource(this.rvf_indicator_af_area);
    if (this.mDeviceController.getAFAreaMenuItems().size() > 0) {
      this.rvf_indicator_af_area.setVisibility(0);
    } else {
      this.rvf_indicator_af_area.setVisibility(8);
    } 
    if (this.mDeviceController.getTouchAFMenuItems().size() == 0 || this.mDeviceController.getTouchAFValue().toUpperCase(Locale.ENGLISH).equals("OFF")) {
      this.rvf_indicator_touch_af.setVisibility(8);
    } else {
      setImageResource(this.rvf_indicator_touch_af);
      this.rvf_indicator_touch_af.setVisibility(0);
    } 
    setImageResource(this.rvf_indicator_storage);
    this.rvf_indicator_storage.setVisibility(0);
  }
  
  private void setIndicator(int paramInt) {
    switch (paramInt) {
      default:
        return;
      case 2131558677:
        if (this.mDeviceController.getWBValue().toUpperCase(Locale.ENGLISH).equals("AUTO")) {
          this.rvf_indicator_wb.setVisibility(8);
          return;
        } 
        setImageResource(this.rvf_indicator_wb);
        this.rvf_indicator_wb.setVisibility(0);
        return;
      case 2131558678:
        if (this.mDeviceController.getMeteringValue().toUpperCase(Locale.ENGLISH).equals("MULTI")) {
          this.rvf_indicator_metering.setVisibility(8);
          return;
        } 
        setImageResource(this.rvf_indicator_metering);
        this.rvf_indicator_metering.setVisibility(0);
        return;
      case 2131558679:
        if (this.mDeviceController.getDriveValue().toUpperCase(Locale.ENGLISH).equals("SINGLE")) {
          this.rvf_indicator_drive.setVisibility(8);
          return;
        } 
        setImageResource(this.rvf_indicator_drive);
        this.rvf_indicator_drive.setVisibility(0);
        return;
      case 2131558680:
        if (this.mDeviceController.getCurrentFlashDisplay().toUpperCase(Locale.ENGLISH).equals("OFF") || this.mDeviceController.getDefaultFlash().toUpperCase(Locale.ENGLISH).equals("") || this.mDeviceController.getFlashStrobeStatus().toUpperCase(Locale.ENGLISH).equals("DETACHED")) {
          this.rvf_indicator_flash.setVisibility(8);
          return;
        } 
        setImageResource(this.rvf_indicator_flash);
        this.rvf_indicator_flash.setVisibility(0);
        return;
      case 2131558681:
        setImageResource(this.rvf_indicator_photo_size);
        this.rvf_indicator_photo_size.setVisibility(0);
        return;
      case 2131558682:
        if (this.mDeviceController.getQualityValue().equals("") || this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).equals("SUPER FINE") || this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).equals("FINE") || this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).equals("NORMAL") || this.mDeviceController.getQualityMenuItems().size() == 0) {
          this.rvf_indicator_quality.setVisibility(8);
          return;
        } 
        setImageResource(this.rvf_indicator_quality);
        this.rvf_indicator_quality.setVisibility(0);
        return;
      case 2131558683:
        setImageResource(this.rvf_indicator_movie_size);
        this.rvf_indicator_movie_size.setVisibility(0);
        return;
      case 2131558684:
        setImageResource(this.rvf_indicator_af_area);
        if (this.mDeviceController.getAFAreaMenuItems().size() > 0) {
          this.rvf_indicator_af_area.setVisibility(0);
          return;
        } 
        this.rvf_indicator_af_area.setVisibility(8);
        return;
      case 2131558685:
        setImageResource(this.rvf_indicator_touch_af);
        if (this.mDeviceController.getTouchAFMenuItems().size() == 0 || this.mDeviceController.getTouchAFValue().toUpperCase(Locale.ENGLISH).equals("OFF")) {
          this.rvf_indicator_touch_af.setVisibility(8);
          return;
        } 
        this.rvf_indicator_touch_af.setVisibility(0);
        return;
      case 2131558686:
        break;
    } 
    setImageResource(this.rvf_indicator_storage);
    this.rvf_indicator_storage.setVisibility(0);
  }
  
  private void setShutterButtonEnabled(boolean paramBoolean) {
    this.rvf_shutter_button.setEnabled(paramBoolean);
    Trace.d(this.TAG, "start setShutterButtonEnabled() state : " + paramBoolean);
  }
  
  private void setSmartPanel() {
    Trace.d(this.TAG, "start setSmartPanel()");
    String[] arrayOfString = (String[])this.mDeviceController.getDialModeMenuItems().toArray((Object[])new String[this.mDeviceController.getDialModeMenuItems().size()]);
    setWheelView(this.rvf_smart_panel_mode, 5, this.mDeviceController.getDialModeMenuItems().indexOf(this.mDeviceController.getDialModeValue()), arrayOfString, 84);
    this.rvf_smart_panel_shutterSpeed.measure(0, 0);
    Trace.d(this.TAG, "rvf_smart_panel_shutterSpeed W : " + this.rvf_smart_panel_shutterSpeed.getMeasuredWidth());
    arrayOfString = (String[])this.mDeviceController.getShutterSpeedMenuItems().toArray((Object[])new String[this.mDeviceController.getShutterSpeedMenuItems().size()]);
    setWheelView(this.rvf_smart_panel_shutterSpeed, 3, this.mDeviceController.getShutterSpeedMenuItems().indexOf(this.mDeviceController.getShutterSpeedValue()), arrayOfString, this.rvf_smart_panel_shutterSpeed.getMeasuredWidth() / 3);
    arrayOfString = (String[])this.mDeviceController.getApertureMenuItems().toArray((Object[])new String[this.mDeviceController.getApertureMenuItems().size()]);
    setWheelView(this.rvf_smart_panel_aperture, 5, this.mDeviceController.getApertureMenuItems().indexOf(this.mDeviceController.getApertureValue()), arrayOfString, this.rvf_smart_panel_shutterSpeed.getMeasuredWidth() / 5);
    arrayOfString = (String[])this.mDeviceController.getEVMenuItems().toArray((Object[])new String[this.mDeviceController.getEVMenuItems().size()]);
    setWheelView(this.rvf_smart_panel_ev, 5, this.mDeviceController.getEVMenuItems().indexOf(this.mDeviceController.getEVValue()), arrayOfString, this.rvf_smart_panel_shutterSpeed.getMeasuredWidth() / 5);
    arrayOfString = (String[])this.mDeviceController.getISOMenuItems().toArray((Object[])new String[this.mDeviceController.getISOMenuItems().size()]);
    setWheelView(this.rvf_smart_panel_iso, 3, this.mDeviceController.getISOMenuItems().indexOf(this.mDeviceController.getISOValue()), arrayOfString, this.rvf_smart_panel_shutterSpeed.getMeasuredWidth() / 3);
    setSmartPanelVisibility(this.mDeviceController.getDialModeValue());
    setSmartPanelEnable(this.mDeviceController.getDialModeValue());
  }
  
  private void setSmartPanelEnable(String paramString) {
    if (paramString.equals("Auto")) {
      this.rvf_smart_panel_shutterSpeed.setEnabled(false);
      this.rvf_smart_panel_aperture.setEnabled(false);
      this.rvf_smart_panel_ev.setEnabled(true);
      if (isDimMenu(this.rvf_smart_panel_photoSize)) {
        this.rvf_smart_panel_photoSize.setEnabled(false);
        return;
      } 
      this.rvf_smart_panel_photoSize.setEnabled(true);
      return;
    } 
    if (paramString.equals("P")) {
      this.rvf_smart_panel_shutterSpeed.setEnabled(false);
      this.rvf_smart_panel_aperture.setEnabled(false);
      this.rvf_smart_panel_ev.setEnabled(true);
      if (this.mDeviceController.getQualityValue().equals("RAW") || isDimMenu(this.rvf_smart_panel_photoSize)) {
        this.rvf_smart_panel_photoSize.setEnabled(false);
        return;
      } 
      this.rvf_smart_panel_photoSize.setEnabled(true);
      return;
    } 
    if (paramString.equals("A")) {
      this.rvf_smart_panel_shutterSpeed.setEnabled(false);
      this.rvf_smart_panel_aperture.setEnabled(true);
      this.rvf_smart_panel_ev.setEnabled(true);
      if (this.mDeviceController.getQualityValue().equals("RAW") || isDimMenu(this.rvf_smart_panel_photoSize)) {
        this.rvf_smart_panel_photoSize.setEnabled(false);
        return;
      } 
      this.rvf_smart_panel_photoSize.setEnabled(true);
      return;
    } 
    if (paramString.equals("S")) {
      this.rvf_smart_panel_shutterSpeed.setEnabled(true);
      this.rvf_smart_panel_aperture.setEnabled(false);
      this.rvf_smart_panel_ev.setEnabled(true);
      if (this.mDeviceController.getQualityValue().equals("RAW") || isDimMenu(this.rvf_smart_panel_photoSize)) {
        this.rvf_smart_panel_photoSize.setEnabled(false);
        return;
      } 
      this.rvf_smart_panel_photoSize.setEnabled(true);
      return;
    } 
    if (paramString.equals("M")) {
      this.rvf_smart_panel_shutterSpeed.setEnabled(true);
      this.rvf_smart_panel_aperture.setEnabled(true);
      this.rvf_smart_panel_ev.setEnabled(false);
      if (this.mDeviceController.getQualityValue().equals("RAW") || isDimMenu(this.rvf_smart_panel_photoSize)) {
        this.rvf_smart_panel_photoSize.setEnabled(false);
        return;
      } 
      this.rvf_smart_panel_photoSize.setEnabled(true);
      return;
    } 
  }
  
  private void setSmartPanelVisibility(int paramInt) {
    Trace.d(this.TAG, "start setSmartPanelVisibility() visibility : " + paramInt);
    this.mSmartPanelVisibility = paramInt;
    this.rvf_smart_panel.setVisibility(paramInt);
    if (paramInt == 4 || paramInt == 8) {
      if (this.mSmartPanelCustomDialogWheelMenuId == 0) {
        resetVisibilityTouchAFFrame();
      } else {
        rvf_touchAutoFocus.setVisibility(4);
      } 
    } else {
      rvf_touchAutoFocus.setVisibility(4);
    } 
    if (isAvailShot()) {
      hideMemoryFullDialog();
      return;
    } 
    if (paramInt == 0 || this.mSmartPanelCustomDialogWheelMenuId != 0) {
      hideMemoryFullDialog();
      return;
    } 
    if (paramInt == 8 || paramInt == 4) {
      showMemoryFullDialog(1);
      return;
    } 
  }
  
  private void setSmartPanelVisibility(String paramString) {
    Trace.d(this.TAG, "start setSmartPanelVisibility()");
    this.rvf_smart_panel_shutterSpeed.setVisibility(0);
    this.rvf_smart_panel_aperture.setVisibility(0);
    if (paramString.equals("Auto")) {
      this.rvf_smart_panel_ev.setVisibility(4);
      this.rvf_smart_panel_iso.setVisibility(4);
      this.rvf_smart_panel_wb.setVisibility(4);
      this.rvf_smart_panel_metering.setVisibility(4);
      this.rvf_smart_panel_photoSize.setVisibility(0);
      this.rvf_smart_panel_quality.setVisibility(4);
    } else {
      this.rvf_smart_panel_ev.setVisibility(0);
      this.rvf_smart_panel_iso.setVisibility(0);
      this.rvf_smart_panel_wb.setVisibility(0);
      this.rvf_smart_panel_metering.setVisibility(0);
      this.rvf_smart_panel_photoSize.setVisibility(0);
      this.rvf_smart_panel_quality.setVisibility(0);
    } 
    if (this.mDeviceController.getMovieResolutionMenuItems().size() > 0) {
      this.rvf_smart_panel_movieSize.setVisibility(0);
    } else {
      this.rvf_smart_panel_movieSize.setVisibility(4);
    } 
    this.rvf_smart_panel_drive.setVisibility(0);
    if (this.mDeviceController.getFlashMenuItems().size() > 0) {
      this.rvf_smart_panel_flash.setVisibility(0);
    } else {
      this.rvf_smart_panel_flash.setVisibility(4);
    } 
    setImageResource(this.rvf_smart_panel_wb);
    setImageResource(this.rvf_smart_panel_metering);
    if (!paramString.equals("Auto") && this.mDeviceController.getQualityValue().equals("RAW")) {
      this.rvf_smart_panel_photoSize.setImageResource(2130837819);
    } else {
      setImageResource(this.rvf_smart_panel_photoSize);
    } 
    if (this.mDeviceController.getAFAreaMenuItems().size() == 0) {
      this.rvf_smart_panel_afArea.setVisibility(4);
    } else {
      this.rvf_smart_panel_afArea.setVisibility(0);
    } 
    if (this.mDeviceController.getTouchAFMenuItems().size() == 0) {
      this.rvf_smart_panel_touchAF.setVisibility(4);
    } else {
      this.rvf_smart_panel_touchAF.setVisibility(0);
    } 
    setImageResource(this.rvf_smart_panel_quality);
    setImageResource(this.rvf_smart_panel_movieSize);
    setImageResource(this.rvf_smart_panel_afMode);
    setImageResource(this.rvf_smart_panel_drive);
    setImageResource(this.rvf_smart_panel_flash);
    setImageResource(this.rvf_smart_panel_afArea);
    setImageResource(this.rvf_smart_panel_touchAF);
    setImageResource(this.rvf_smart_panel_save);
    setImageResource(this.rvf_smart_panel_streaming_quality);
  }
  
  private void setTimer(int paramInt) {
    Trace.d(this.TAG, "start setTimer() timerID : " + paramInt);
    long l = 0L;
    Message message = new Message();
    message.what = paramInt;
    this.mHandleTimer.removeMessages(message.what);
    switch (paramInt) {
      default:
        this.mHandleTimer.sendMessageDelayed(message, l);
        return;
      case 1:
        l = 5000L;
      case 2:
      case 3:
        l = 300L;
      case 4:
        l = 5000L;
      case 5:
        l = 60000L;
      case 6:
        l = 90000L;
      case 7:
        l = 180000L;
      case 8:
        l = 3000L;
      case 9:
        l = 300L;
      case 10:
        l = 1000L;
      case 11:
        l = 1000L;
      case 12:
        break;
    } 
    l = 3000L;
  }
  
  private void setTouchAFPosition() {
    int i;
    int j;
    Trace.d(this.TAG, "start setTouchAFPosition()");
    if (this.mMarginLeft < 0 || this.mOrientationWhenCheckMargin != orientation) {
      int k;
      int m;
      int n;
      int i1;
      if (this.mFrameWidth == 0) {
        this.mFrameWidth = rvf_touchAutoFocus.getWidth();
        this.mFrameHeight = rvf_touchAutoFocus.getHeight();
      } 
      if (this.mDeviceController.getRatioOffsetMenuItems().size() > 0) {
        k = this.mDeviceController.getRatioOffsetValue().getWidth() * this.screenSize[getPhoneCamType() - 1][0] / 640 + this.screenPositoin[getPhoneCamType() - 1][0];
        m = this.mDeviceController.getRatioOffsetValue().getHeight() * this.screenSize[getPhoneCamType() - 1][1] / 480;
        i1 = this.screenSize[getPhoneCamType() - 1][0] - this.mDeviceController.getRatioOffsetValue().getWidth() * 2 * this.screenSize[getPhoneCamType() - 1][0] / 640;
        n = this.screenSize[getPhoneCamType() - 1][1] - m * 2;
      } else {
        k = this.screenPositoin[getPhoneCamType() - 1][0];
        m = this.screenPositoin[getPhoneCamType() - 1][1];
        i1 = this.screenSize[getPhoneCamType() - 1][0];
        n = this.screenSize[getPhoneCamType() - 1][1];
      } 
      j = this.positionX * i1 / 100 + k - this.mFrameWidth / 2;
      int i2 = this.positionY * n / 100 + m - this.mFrameHeight / 2;
      if (j < k) {
        i = k;
      } else {
        i = j;
        if (this.mFrameWidth + j > k + i1)
          i = k + i1 - this.mFrameWidth; 
      } 
      if (i2 < m) {
        j = m;
      } else {
        j = i2;
        if (this.mFrameHeight + i2 > m + n)
          j = m + n - this.mFrameHeight; 
      } 
      Trace.d(this.TAG, "<<<<< screenSize W : " + i1 + " H : " + n + " frame W : " + this.mFrameWidth + " H : " + this.mFrameHeight);
      Trace.d(this.TAG, "screenPositionX : " + k + " screenPositionY : " + m);
      Trace.d(this.TAG, "marginLeft : " + i + " marginTop : " + j + " positionX : " + this.positionX + " Y : " + this.positionY);
    } else {
      i = this.mMarginLeft;
      j = this.mMarginTop;
    } 
    ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(rvf_touchAutoFocus.getLayoutParams());
    marginLayoutParams.setMargins(i, j, 0, 0);
    rvf_touchAutoFocus.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(marginLayoutParams));
  }
  
  private void setVisibilityOSD(int paramInt) {
    Trace.d(this.TAG, "start setVisibilityOSD() visibility : " + paramInt);
    if (paramInt == 0) {
      rvf_title.setVisibility(paramInt);
      this.rvf_gallery.setVisibility(paramInt);
      this.rvf_shutter_button.setVisibility(paramInt);
      showZoomBar();
      showIndicatorBar();
      if (RVFFunctionManager.isSupportedSmartPanel(this.mDeviceController.getVersionPrefix(), this.mDeviceController.getVersion())) {
        this.rvf_smart_panel_icon.setVisibility(paramInt);
        this.rvf_mode_button.setVisibility(paramInt);
        if (this.mDeviceController.getMovieResolutionMenuItems().size() > 0)
          this.rvf_camcorder_button.setVisibility(paramInt); 
        return;
      } 
      this.rvf_menu_button.setVisibility(paramInt);
      return;
    } 
    rvf_title.setVisibility(4);
    this.rvf_gallery.setVisibility(paramInt);
    this.rvf_shutter_button.setVisibility(paramInt);
    hideZoomBar();
    hideIndicatorBar();
    if (RVFFunctionManager.isSupportedSmartPanel(this.mDeviceController.getVersionPrefix(), this.mDeviceController.getVersion())) {
      this.rvf_smart_panel_icon.setVisibility(paramInt);
      this.rvf_mode_button.setVisibility(paramInt);
      if (this.mDeviceController.getMovieResolutionMenuItems().size() > 0) {
        this.rvf_camcorder_button.setVisibility(paramInt);
        return;
      } 
      return;
    } 
    this.rvf_menu_button.setVisibility(paramInt);
  }
  
  private void setWheelView(WheelView paramWheelView, int paramInt1, int paramInt2, String[] paramArrayOfString, int paramInt3) {
    Trace.d(this.TAG, "text size : " + getApplicationContext().getResources().getInteger(2131296263));
    ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter((Context)this, (Object[])paramArrayOfString);
    arrayWheelAdapter.setTextSize(getApplicationContext().getResources().getInteger(2131296263));
    arrayWheelAdapter.setTextViewWidth(paramInt3);
    paramWheelView.setVisibleItems(paramInt1);
    paramWheelView.setViewAdapter((WheelViewAdapter)arrayWheelAdapter);
    paramWheelView.setCurrentItem(paramInt2);
    updateWheelView(paramWheelView);
  }
  
  private void setmHandler(int paramInt) {
    Trace.e(this.TAG, "nDisplayFlag is  " + nDisplayFlag + " ==> " + paramInt);
    nDisplayFlag = paramInt;
    mHandler.post(new XxxThread(nDisplayFlag));
  }
  
  private void showAFExtend() {
    Trace.d(this.TAG, "start showAFExtend() bAFFAIL : " + this.bAFFAIL);
    if (this.bAFFAIL) {
      this.rvf_af_extend_leftup.setBackgroundResource(2130837566);
      this.rvf_af_extend_leftdown.setBackgroundResource(2130837565);
      this.rvf_af_extend_rightup.setBackgroundResource(2130837568);
      this.rvf_af_extend_rightdown.setBackgroundResource(2130837567);
    } else {
      this.rvf_af_extend_leftup.setBackgroundResource(2130837562);
      this.rvf_af_extend_leftdown.setBackgroundResource(2130837561);
      this.rvf_af_extend_rightup.setBackgroundResource(2130837564);
      this.rvf_af_extend_rightdown.setBackgroundResource(2130837563);
    } 
    rvf_af_extend.setVisibility(0);
  }
  
  private void showAutoFocusFrame(boolean paramBoolean) {
    switch (RVFFunctionManager.getAFFrameMetricsType(this.mDeviceController.getMultiAFMatrixSizeValue(), this.connectedSsid)) {
      default:
        return;
      case 0:
        if (paramBoolean) {
          rvf_af3x3_11.setImageResource(2130837573);
        } else {
          rvf_af3x3_11.setImageResource(2130837574);
        } 
        rvf_af3x3_11.setVisibility(0);
        return;
      case 1:
        if (!paramBoolean) {
          rvf_af3x3_11.setImageResource(2130837574);
          rvf_af3x3_11.setVisibility(0);
          return;
        } 
      case 2:
        if (!paramBoolean) {
          rvf_af3x5_12.setImageResource(2130837574);
          rvf_af3x5_12.setVisibility(0);
          return;
        } 
      case 3:
        break;
    } 
    if (!paramBoolean) {
      rvf_af3x7_13.setImageResource(2130837574);
      rvf_af3x7_13.setVisibility(0);
      return;
    } 
  }
  
  private void showIndicatorBar() {
    switch (this.mPhoneCamType) {
      default:
        return;
      case 1:
        if (RVFFunctionManager.isSupportedSmartPanel(this.mDeviceController.getVersionPrefix(), this.mDeviceController.getVersion())) {
          this.rvf_indicator_bar.setVisibility(0);
          return;
        } 
        this.rvf_indicator_bar.setVisibility(8);
        return;
      case 2:
      case 3:
      case 4:
        break;
    } 
    this.rvf_indicator_bar.setVisibility(0);
  }
  
  private void showMainOptionMenu(int paramInt) {
    if (!isAvailShot())
      hideMemoryFullDialog(); 
    setCurrentMainOptionMenuId(paramInt);
    this.rvf_option.setVisibility(0);
    this.rvf_menuTop1.setVisibility(8);
    this.rvf_menuTop2.setVisibility(8);
    this.rvf_menuTop3.setVisibility(8);
    this.rvf_menuTop4.setVisibility(8);
    this.mMainOptionMenuList.clear();
    switch (paramInt) {
      default:
        this.mMainOptionMenuListAdapter.notifyDataSetChanged();
        setTimer(1);
        return;
      case 1:
        this.rvf_menuTop1.setVisibility(0);
        this.rvf_menuTitle.setText(2131361982);
        if (getPhoneCamType() == 2)
          ((RelativeLayout.LayoutParams)this.rvf_option.getLayoutParams()).topMargin = getApplicationContext().getResources().getInteger(2131296258); 
        Trace.d(this.TAG, "mDeviceController.getFlashMenuItems().size() : " + this.mDeviceController.getFlashMenuItems().size());
        paramInt = 0;
        while (true) {
          if (paramInt < this.mDeviceController.getFlashMenuItems().size()) {
            String str = ((String)this.mDeviceController.getFlashMenuItems().get(paramInt)).toUpperCase(Locale.ENGLISH);
            if (str.equals("AUTO")) {
              this.mMainOptionMenuList.add(new Menu(getString(2131361987)));
            } else if (str.equals("REDEYE")) {
              this.mMainOptionMenuList.add(new Menu(getString(2131361986)));
            } else if (str.equals("FILLIN") || str.equals("FILL-IN") || str.equals("FILL IN")) {
              this.mMainOptionMenuList.add(new Menu(getString(2131361985)));
            } else if (str.equals("SLOWSYNC")) {
              this.mMainOptionMenuList.add(new Menu(getString(2131361984)));
            } else if (str.equals("REDEYEFIX")) {
              this.mMainOptionMenuList.add(new Menu(getString(2131361983)));
            } else if (str.equals("FILLINREDEYE") || str.equals("FILL-IN_RED") || str.equals("FILL-IN RED")) {
              this.mMainOptionMenuList.add(new Menu(getString(2131361989)));
            } else if (str.equals("1STCURTAIN") || str.equals("1ST_CURTAIN") || str.equals("1ST CURTAIN")) {
              this.mMainOptionMenuList.add(new Menu(getString(2131361990)));
            } else if (str.equals("2NDCURTAIN") || str.equals("2ND_CURTAIN") || str.equals("2ND CURTAIN")) {
              this.mMainOptionMenuList.add(new Menu(getString(2131361991)));
            } else {
              this.mMainOptionMenuList.add(new Menu(getString(2131361988)));
            } 
            paramInt++;
          } 
        } 
      case 2:
        this.rvf_menuTop2.setVisibility(0);
        this.rvf_menuTitle.setText(2131361992);
        if (getPhoneCamType() == 2)
          ((RelativeLayout.LayoutParams)this.rvf_option.getLayoutParams()).topMargin = getApplicationContext().getResources().getInteger(2131296259); 
        paramInt = 0;
        while (true) {
          if (paramInt < this.mDeviceController.getLedTimeMenuItems().size()) {
            this.mMainOptionMenuList.add(new Menu(this.mDeviceController.getLedTimeMenuItems().get(paramInt)));
            paramInt++;
          } 
        } 
      case 3:
        this.rvf_menuTop3.setVisibility(0);
        this.rvf_menuTitle.setText(2131361998);
        if (getPhoneCamType() == 2)
          ((RelativeLayout.LayoutParams)this.rvf_option.getLayoutParams()).topMargin = getApplicationContext().getResources().getInteger(2131296260); 
        paramInt = 0;
        while (true) {
          if (paramInt < this.mDeviceController.getResolutionMenuItems().size()) {
            DSCResolution dSCResolution = this.mDeviceController.getResolutionMenuItems().get(paramInt);
            String str = String.format("%s (%dx%d)", new Object[] { Utils.unitChange(this, dSCResolution.getHeight(), dSCResolution.getWidth(), FunctionManager.getPhotoSizePresentationType(this.connectedSsid)), Integer.valueOf(dSCResolution.getWidth()), Integer.valueOf(dSCResolution.getHeight()) });
            this.mMainOptionMenuList.add(new Menu(str));
            paramInt++;
          } 
        } 
      case 4:
        this.rvf_menuTop4.setVisibility(0);
        this.rvf_menuTitle.setText(2131362000);
        if (getPhoneCamType() == 2)
          ((RelativeLayout.LayoutParams)this.rvf_option.getLayoutParams()).topMargin = getApplicationContext().getResources().getInteger(2131296261); 
        this.mMainOptionMenuList.add(new Menu(getString(2131362010)));
        this.mMainOptionMenuList.add(new Menu(getString(2131362009)));
      case 5:
        break;
    } 
    this.rvf_menuTop4.setVisibility(0);
    this.rvf_menuTitle.setText(2131362016);
    if (getPhoneCamType() == 2)
      ((RelativeLayout.LayoutParams)this.rvf_option.getLayoutParams()).topMargin = getApplicationContext().getResources().getInteger(2131296261); 
    this.mMainOptionMenuList.add(new Menu(getString(2131362000)));
    this.mMainOptionMenuList.add(new Menu(getString(2131362017)));
  }
  
  private void showMemoryFullDialog(int paramInt) {
    Trace.d(this.TAG, "start showMemoryFullDialog() delay : " + paramInt);
    rvf_zoomSeekBar.setEnabled(false);
    switch (paramInt) {
      default:
        return;
      case 0:
        showPopupWindow(1);
        setTimer(12);
        return;
      case 1:
        break;
    } 
    showPopupWindow(1);
  }
  
  private void showOSD() {
    rvf_title.setVisibility(0);
    this.rvf_gallery.setVisibility(0);
    this.rvf_shutter_button.setVisibility(0);
    if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0)
      showZoomBar(); 
  }
  
  private void showPopupWindow(int paramInt) {
    TextView textView1;
    TextView textView2;
    Trace.d(this.TAG, "start showPopupWindow() id : " + paramInt);
    if (this.rvf_surface.getWidth() > 0) {
      this.mCurPopupWindowId = paramInt;
      View view = getLayoutInflater().inflate(2130903104, null);
      textView1 = (TextView)view.findViewById(2131558713);
      textView2 = (TextView)view.findViewById(2131558714);
      switch (paramInt) {
        default:
          if (this.popupWindow == null)
            this.popupWindow = new PopupWindow(view, -2, -2); 
          this.popupWindow.showAtLocation((View)this.rvf_surface, 17, 0, 0);
          return;
        case 1:
          textView2.setVisibility(0);
          textView2.setText(2131361944);
        case 2:
          break;
      } 
    } else {
      return;
    } 
    textView1.setVisibility(0);
    textView1.setText("Recording...");
    textView2.setVisibility(0);
    textView2.setText(2131362027);
  }
  
  private void showProgressBar(String paramString1, String paramString2) {
    Trace.d(this.TAG, "start showProgressBar()");
    if (this.pro == null) {
      this.pro = ProgressDialog.show((Context)this, paramString1, paramString2, true, false, this.progDiaCancelListener);
      this.pro.getWindow().clearFlags(2);
      this.pro.setCancelable(false);
      return;
    } 
    this.pro.setMessage(paramString2);
  }
  
  private void showQuickSettingMenu() {
    Trace.d(this.TAG, "show QuickSettingMenu");
    hideIndicatorBar();
    rvf_QuickSettingMenu.setVisibility(0);
    rvf_open_button.setVisibility(8);
  }
  
  private void showSmartPanelCustomDialogListMenu(int paramInt, String paramString, ArrayList<String> paramArrayList) {
    Menu menu;
    DSCMovieResolution<String> dSCMovieResolution;
    ArrayList<Menu> arrayList = new ArrayList();
    switch (paramInt) {
      default:
        setSmartPanelCustomDialogListMenuId(paramInt);
        this.mSmartPanelCustomDialogListMenuItems = arrayList;
        this.mSmartPanelCustomDialogList = new CustomDialogListMenu((Context)this, arrayList, this.mSmartPanelCustomDialogListMenuItemClickListener);
        this.mSmartPanelCustomDialogList.setTitle(paramString);
        this.mSmartPanelCustomDialogList.setCanceledOnTouchOutside(true);
        this.mSmartPanelCustomDialogList.show();
        this.mSmartPanelCustomDialogList.setOnDismissListener(new DialogInterface.OnDismissListener() {
              public void onDismiss(DialogInterface param1DialogInterface) {
                LiveShutter.this.setSmartPanelCustomDialogListMenuId(0);
              }
            });
        return;
      case 25:
        i = 0;
        while (true) {
          if (i < paramArrayList.size()) {
            Menu menu1 = new Menu(paramArrayList.get(i));
            if (((String)paramArrayList.get(i)).toUpperCase(Locale.ENGLISH).equals("AUTO")) {
              menu1.setIconResourceId(2130838048);
            } else if (((String)paramArrayList.get(i)).toUpperCase(Locale.ENGLISH).equals("DAYLIGHT")) {
              menu1.setIconResourceId(2130838052);
            } else if (((String)paramArrayList.get(i)).toUpperCase(Locale.ENGLISH).equals("CLOUDY")) {
              menu1.setIconResourceId(2130838049);
            } else if (((String)paramArrayList.get(i)).toUpperCase(Locale.ENGLISH).equals("FLUORESCENT_W")) {
              menu1.setIconResourceId(2130838058);
            } else if (((String)paramArrayList.get(i)).toUpperCase(Locale.ENGLISH).equals("FLUORESCENT_N") || ((String)paramArrayList.get(i)).toUpperCase(Locale.ENGLISH).equals("FLUORESCENT_NW")) {
              menu1.setIconResourceId(2130838057);
            } else if (((String)paramArrayList.get(i)).toUpperCase(Locale.ENGLISH).equals("FLUORESCENT_D")) {
              menu1.setIconResourceId(2130838054);
            } else if (((String)paramArrayList.get(i)).toUpperCase(Locale.ENGLISH).equals("TUNGSTEN")) {
              menu1.setIconResourceId(2130838059);
            } else if (((String)paramArrayList.get(i)).toUpperCase(Locale.ENGLISH).equals("FLASHWB")) {
              menu1.setIconResourceId(2130838053);
            } else if (((String)paramArrayList.get(i)).toUpperCase(Locale.ENGLISH).equals("COLORTEMP")) {
              menu1.setIconResourceId(2130838050);
            } 
            menu1.setSelected(((String)paramArrayList.get(i)).equals(this.mDeviceController.getWBValue()));
            arrayList.add(menu1);
            i++;
          } 
        } 
      case 26:
        i = 0;
        while (true) {
          if (i < paramArrayList.size()) {
            Menu menu1 = new Menu(paramArrayList.get(i));
            if (((String)paramArrayList.get(i)).toUpperCase(Locale.ENGLISH).equals("MULTI")) {
              menu1.setIconResourceId(2130837980);
            } else if (((String)paramArrayList.get(i)).toUpperCase(Locale.ENGLISH).equals("CENTERWEIGHTED") || ((String)paramArrayList.get(i)).toUpperCase(Locale.ENGLISH).equals("CENTER-WEIGHTED")) {
              menu1.setIconResourceId(2130837979);
            } else if (((String)paramArrayList.get(i)).toUpperCase(Locale.ENGLISH).equals("SPOT")) {
              menu1.setIconResourceId(2130837981);
            } 
            menu1.setSelected(((String)paramArrayList.get(i)).equals(this.mDeviceController.getMeteringValue()));
            arrayList.add(menu1);
            i++;
          } 
        } 
      case 2:
        i = 0;
        while (true) {
          if (i < this.mDeviceController.getResolutionMenuItems().size()) {
            boolean bool;
            DSCResolution dSCResolution = this.mDeviceController.getResolutionMenuItems().get(i);
            menu = new Menu(String.format("%dx%d", new Object[] { Integer.valueOf(dSCResolution.getWidth()), Integer.valueOf(dSCResolution.getHeight()) }));
            String str = Utils.unitChange(this, dSCResolution.getWidth(), dSCResolution.getHeight(), FunctionManager.getPhotoSizePresentationType(this.connectedSsid));
            if (str.equals("1M")) {
              menu.setIconResourceId(2130838018);
            } else if (str.equals("1.1M")) {
              menu.setIconResourceId(2130838017);
            } else if (str.equals("2M")) {
              menu.setIconResourceId(2130838021);
            } else if (str.equals("2.1M")) {
              menu.setIconResourceId(2130838020);
            } else if (str.equals("W2M")) {
              menu.setIconResourceId(2130838022);
            } else if (str.equals("3M")) {
              menu.setIconResourceId(2130838023);
            } else if (str.equals("4M")) {
              menu.setIconResourceId(2130838026);
            } else if (str.equals("4.9M")) {
              menu.setIconResourceId(2130838025);
            } else if (str.equals("W4M")) {
              menu.setIconResourceId(2130838027);
            } else if (str.equals("5M")) {
              menu.setIconResourceId(2130838029);
            } else if (str.equals("5.9M")) {
              menu.setIconResourceId(2130838028);
            } else if (str.equals("7M")) {
              menu.setIconResourceId(2130838031);
            } else if (str.equals("7.8M")) {
              menu.setIconResourceId(2130838030);
            } else if (str.equals("W7M")) {
              menu.setIconResourceId(2130838032);
            } else if (str.equals("8M")) {
              menu.setIconResourceId(2130838033);
            } else if (str.equals("9M")) {
              menu.setIconResourceId(2130838034);
            } else if (str.equals("10M")) {
              menu.setIconResourceId(2130838008);
            } else if (str.equals("10.1M")) {
              menu.setIconResourceId(2130838007);
            } else if (str.equals("W10M")) {
              menu.setIconResourceId(2130838009);
            } else if (str.equals("W12M")) {
              menu.setIconResourceId(2130838010);
            } else if (str.equals("13M")) {
              menu.setIconResourceId(2130838012);
            } else if (str.equals("13.3M")) {
              menu.setIconResourceId(2130838011);
            } else if (str.equals("P14M")) {
              menu.setIconResourceId(2130838013);
            } else if (str.equals("16M")) {
              menu.setIconResourceId(2130838015);
            } else if (str.equals("16.9M")) {
              menu.setIconResourceId(2130838014);
            } else if (str.equals("W16M")) {
              menu.setIconResourceId(2130838016);
            } else if (str.equals("20M")) {
              menu.setIconResourceId(2130838019);
            } else {
              menu.setIconResourceId(2130838008);
            } 
            if (i == Integer.parseInt(this.mDeviceController.getDefaultResolutionIndex())) {
              bool = true;
            } else {
              bool = false;
            } 
            menu.setSelected(bool);
            arrayList.add(menu);
            i++;
          } 
        } 
      case 33:
        i = 0;
        while (true) {
          if (i < menu.size()) {
            Menu menu1 = new Menu(menu.get(i));
            if (((String)menu.get(i)).toUpperCase(Locale.ENGLISH).equals("SUPER FINE")) {
              menu1.setIconResourceId(2130838000);
            } else if (((String)menu.get(i)).toUpperCase(Locale.ENGLISH).equals("FINE")) {
              menu1.setIconResourceId(2130837996);
            } else if (((String)menu.get(i)).toUpperCase(Locale.ENGLISH).equals("NORMAL")) {
              menu1.setIconResourceId(2130837997);
            } else if (((String)menu.get(i)).toUpperCase(Locale.ENGLISH).equals("RAW")) {
              menu1.setIconResourceId(2130837998);
            } else if (((String)menu.get(i)).toUpperCase(Locale.ENGLISH).equals("RAW+JPEG")) {
              menu1.setIconResourceId(2130837999);
            } 
            menu1.setSelected(((String)menu.get(i)).equals(this.mDeviceController.getQualityValue()));
            arrayList.add(menu1);
            i++;
          } 
        } 
      case 34:
        if (this.mDeviceController.getVideoOutValue().toUpperCase(Locale.ENGLISH).equals("NTSC")) {
          i = 0;
          while (true) {
            if (i < this.mDeviceController.getMovieResolutionMenuItems().size()) {
              dSCMovieResolution = this.mDeviceController.getMovieResolutionMenuItems().get(i);
              Menu menu1 = new Menu(dSCMovieResolution.getResolution());
              if (dSCMovieResolution.getResolution().equals("1920x1080 (15p)")) {
                menu1.setIconResourceId(2130837982);
              } else if (dSCMovieResolution.getResolution().equals("1920x1080 (30p)")) {
                menu1.setIconResourceId(2130837984);
              } else if (dSCMovieResolution.getResolution().equals("1920x1080 (60p)")) {
                menu1.setIconResourceId(2130837985);
              } else if (dSCMovieResolution.getResolution().equals("1920x810 (24p)")) {
                menu1.setIconResourceId(2130837995);
              } else if (dSCMovieResolution.getResolution().equals("1280x720 (30p)")) {
                menu1.setIconResourceId(2130837993);
              } else if (dSCMovieResolution.getResolution().equals("1280x720 (60p)")) {
                menu1.setIconResourceId(2130837994);
              } else if (dSCMovieResolution.getResolution().equals("640x480 (30p)")) {
                menu1.setIconResourceId(2130837990);
              } else if (dSCMovieResolution.getResolution().equals("320x240 (30p)") || dSCMovieResolution.getResolution().equals("For Sharing")) {
                menu1.setIconResourceId(2130837987);
              } 
              menu1.setSelected(dSCMovieResolution.getResolution().equals(this.mDeviceController.getMovieResolutionValue()));
              arrayList.add(menu1);
              i++;
            } 
          } 
        } 
        if (this.mDeviceController.getVideoOutValue().toUpperCase(Locale.ENGLISH).equals("PAL")) {
          i = 0;
          while (true) {
            if (i < this.mDeviceController.getMovieResolutionMenuItems().size()) {
              dSCMovieResolution = this.mDeviceController.getMovieResolutionMenuItems().get(i);
              Menu menu1 = new Menu(dSCMovieResolution.getResolution());
              if (dSCMovieResolution.getResolution().equals("1920x1080 (25p)")) {
                menu1.setIconResourceId(2130837983);
              } else if (dSCMovieResolution.getResolution().equals("1280x720 (25p)")) {
                menu1.setIconResourceId(2130837992);
              } else if (dSCMovieResolution.getResolution().equals("640x480 (25p)")) {
                menu1.setIconResourceId(2130837989);
              } else if (dSCMovieResolution.getResolution().equals("For Sharing")) {
                menu1.setIconResourceId(2130837987);
              } 
              menu1.setSelected(dSCMovieResolution.getResolution().equals(this.mDeviceController.getMovieResolutionValue()));
              arrayList.add(menu1);
              i++;
            } 
          } 
        } 
      case 27:
        i = 0;
        while (true) {
          if (i < dSCMovieResolution.size()) {
            arrayList.add(new Menu(dSCMovieResolution.get(i)));
            i++;
          } 
        } 
      case 29:
        i = 0;
        while (true) {
          if (i < dSCMovieResolution.size()) {
            Menu menu1 = new Menu(dSCMovieResolution.get(i));
            if (((String)dSCMovieResolution.get(i)).toUpperCase(Locale.ENGLISH).equals("SINGLE")) {
              menu1.setIconResourceId(2130837969);
            } else if (((String)dSCMovieResolution.get(i)).toUpperCase(Locale.ENGLISH).equals("CONTINUOUS")) {
              menu1.setIconResourceId(2130837966);
            } else if (((String)dSCMovieResolution.get(i)).toUpperCase(Locale.ENGLISH).equals("TIMER (2SEC)")) {
              menu1.setIconResourceId(2130838039);
            } else if (((String)dSCMovieResolution.get(i)).toUpperCase(Locale.ENGLISH).equals("TIMER (5SEC)")) {
              menu1.setIconResourceId(2130838041);
            } else if (((String)dSCMovieResolution.get(i)).toUpperCase(Locale.ENGLISH).equals("TIMER (10SEC)")) {
              menu1.setIconResourceId(2130838038);
            } else if (((String)dSCMovieResolution.get(i)).toUpperCase(Locale.ENGLISH).equals("TIMER (DOUBLE)")) {
              menu1.setIconResourceId(2130838042);
            } 
            menu1.setSelected(((String)dSCMovieResolution.get(i)).equals(this.mDeviceController.getDriveValue()));
            arrayList.add(menu1);
            i++;
          } 
        } 
      case 17:
        i = 0;
        while (true) {
          if (i < dSCMovieResolution.size()) {
            Menu menu1 = new Menu(dSCMovieResolution.get(i));
            String str = ((String)dSCMovieResolution.get(i)).toUpperCase(Locale.ENGLISH);
            if (str.equals("AUTO")) {
              menu1.setIconResourceId(2130837973);
            } else if (str.equals("FILL-IN") || str.equals("FILL IN")) {
              menu1.setIconResourceId(2130837972);
            } else if (str.equals("FILL-IN_RED") || str.equals("FILL-IN RED")) {
              menu1.setIconResourceId(2130837975);
            } else if (str.equals("1ST_CURTAIN") || str.equals("1ST CURTAIN")) {
              menu1.setIconResourceId(2130837970);
            } else if (str.equals("2ND_CURTAIN") || str.equals("2ND CURTAIN")) {
              menu1.setIconResourceId(2130837971);
            } else {
              menu1.setIconResourceId(2130837974);
            } 
            menu1.setSelected(((String)dSCMovieResolution.get(i)).equals(this.mDeviceController.getDefaultFlash()));
            arrayList.add(menu1);
            i++;
          } 
        } 
      case 28:
        i = 0;
        while (true) {
          if (i < dSCMovieResolution.size()) {
            Menu menu1 = new Menu(dSCMovieResolution.get(i));
            String str = ((String)dSCMovieResolution.get(i)).toUpperCase(Locale.ENGLISH);
            if (str.equals("SELECTION AF")) {
              menu1.setIconResourceId(2130837953);
            } else if (str.equals("MULTI AF")) {
              menu1.setIconResourceId(2130837952);
            } 
            menu1.setSelected(((String)dSCMovieResolution.get(i)).equals(this.mDeviceController.getAFAreaValue()));
            arrayList.add(menu1);
            i++;
          } 
        } 
      case 11:
        i = 0;
        while (true) {
          if (i < dSCMovieResolution.size()) {
            Menu menu1 = new Menu(dSCMovieResolution.get(i));
            String str = ((String)dSCMovieResolution.get(i)).toUpperCase(Locale.ENGLISH);
            if (str.equals("OFF")) {
              menu1.setIconResourceId(2130838045);
            } else if (str.equals("TOUCH AF")) {
              menu1.setIconResourceId(2130838047);
            } else if (str.equals("AF POINT")) {
              menu1.setIconResourceId(2130838044);
            } 
            menu1.setSelected(((String)dSCMovieResolution.get(i)).equals(this.mDeviceController.getTouchAFValue()));
            arrayList.add(menu1);
            i++;
          } 
        } 
      case 37:
        i = 0;
        while (true) {
          if (i < dSCMovieResolution.size()) {
            Menu menu1 = new Menu(dSCMovieResolution.get(i));
            if (i == 0) {
              menu1.setIconResourceId(2130838036);
            } else if (i == 1) {
              menu1.setIconResourceId(2130838035);
            } 
            menu1.setSelected(((String)dSCMovieResolution.get(i)).equals(this.mDeviceController.getFileSaveValue()));
            arrayList.add(menu1);
            i++;
          } 
        } 
      case 18:
        break;
    } 
    int i = 0;
    while (true) {
      if (i < dSCMovieResolution.size()) {
        Menu menu1 = new Menu(dSCMovieResolution.get(i));
        if (((String)dSCMovieResolution.get(i)).toUpperCase(Locale.ENGLISH).equals("HIGH")) {
          menu1.setName("VGA");
          menu1.setIconResourceId(2130837991);
        } else if (((String)dSCMovieResolution.get(i)).toUpperCase(Locale.ENGLISH).equals("LOW")) {
          menu1.setName("QVGA");
          menu1.setIconResourceId(2130837988);
        } 
        menu1.setSelected(((String)dSCMovieResolution.get(i)).equals(this.mDeviceController.getCurrentStreamQuality()));
        arrayList.add(menu1);
        i++;
      } 
    } 
  }
  
  private void showSmartPanelCustomDialogWheelMenu(int paramInt1, String paramString, int paramInt2, int paramInt3, ArrayList<String> paramArrayList, int paramInt4) {
    this.mSmartPanelCustomDialogWheelMenuId = paramInt1;
    this.mSmartPanelCustomDialogWheelMenuItems = paramArrayList;
    this.mSmartPanelCustomDialogWheel = new CustomDialogWheelMenu((Context)this, String.valueOf(paramString) + " : ", paramInt2, paramInt3, paramArrayList.<String>toArray(new String[paramArrayList.size()]), paramInt4, this.mSmartPanelCustomDialogWheelMenuWheelScrollListener, this.mSmartPanelCustomDialogWheelMenuWheelClickedListener);
    this.mSmartPanelCustomDialogWheel.setCanceledOnTouchOutside(true);
    this.mSmartPanelCustomDialogWheel.show();
    this.mSmartPanelCustomDialogWheel.setOnDismissListener(new DialogInterface.OnDismissListener() {
          public void onDismiss(DialogInterface param1DialogInterface) {
            if (LiveShutter.this.mSmartPanelCustomDialogWheelMenuId != 32) {
              LiveShutter.this.setSmartPanelVisibility(0);
            } else {
              LiveShutter.this.setVisibilityOSD(0);
              if (!LiveShutter.this.isAvailShot())
                LiveShutter.this.showMemoryFullDialog(1); 
            } 
            LiveShutter.this.setButtonEnabled(true);
            LiveShutter.this.mSmartPanelCustomDialogWheelMenuId = 0;
            if (LiveShutter.this.mDeviceController.getAFAreaMenuItems().size() > 0 && LiveShutter.this.mDeviceController.getAFAreaValue().toUpperCase(Locale.ENGLISH).equals("SELECTION AF")) {
              LiveShutter.rvf_touchAutoFocus.setImageResource(2130837575);
              LiveShutter.rvf_touchAutoFocus.setVisibility(0);
              return;
            } 
            if (LiveShutter.this.mDeviceController.getAFAreaValue().toUpperCase(Locale.ENGLISH).equals("MULTI AF")) {
              LiveShutter.rvf_touchAutoFocus.setVisibility(4);
              return;
            } 
          }
        });
    setSmartPanelVisibility(8);
  }
  
  private void showSubOptionMenu(int paramInt) {
    setCurrentSubOptionMenuId(paramInt);
    this.rvf_setting_sub_menu.setVisibility(0);
    this.mSubOptionMenuList.clear();
    if (getPhoneCamType() == 2) {
      RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.rvf_option.getLayoutParams();
      layoutParams.topMargin += 56;
    } 
    switch (paramInt) {
      default:
        this.mSubOptionMenuListAdapter.notifyDataSetChanged();
        setTimer(1);
        return;
      case 4:
        this.rvf_setting_sub_menu_title.setText(2131362000);
        this.mSubOptionMenuList.add(new Menu(getString(2131362010)));
        this.mSubOptionMenuList.add(new Menu(getString(2131362009)));
      case 6:
        break;
    } 
    this.rvf_setting_sub_menu_title.setText(2131362017);
    paramInt = 0;
    while (true) {
      if (paramInt < this.mDeviceController.getStreamQualityMenuItems().size()) {
        if (((String)this.mDeviceController.getStreamQualityMenuItems().get(paramInt)).equals("high")) {
          this.mSubOptionMenuList.add(new Menu("VGA"));
        } else if (((String)this.mDeviceController.getStreamQualityMenuItems().get(paramInt)).equals("low")) {
          this.mSubOptionMenuList.add(new Menu("QVGA"));
        } 
        paramInt++;
      } 
    } 
  }
  
  private void showZoomBar() {
    Trace.d(this.TAG, "showZoomBar()");
    if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0) {
      rvf_zoomMain.setVisibility(0);
      zoombardisplay();
    } else {
      rvf_zoomMain.setVisibility(8);
    } 
    setTimer(1);
  }
  
  private void shutterbutton(boolean paramBoolean) {
    if (!isButtonShow)
      paramBoolean = false; 
    Trace.d(this.TAG, "-=> shutterbutton flag=" + paramBoolean + ", bConnect=" + this.bConnect);
    if (paramBoolean && this.bConnect) {
      paramBoolean = CommonUtils.isMemoryFull();
      if (this.mDeviceController != null && (!paramBoolean || this.nCameraSaveSelect != 0)) {
        setShutterButtonEnabled(true);
        return;
      } 
    } else {
      return;
    } 
    if (this.mDeviceController.getAvailShots().equals("-100") && (!paramBoolean || this.nCameraSaveSelect != 0)) {
      setShutterButtonEnabled(true);
      return;
    } 
    setShutterButtonEnabled(false);
  }
  
  private void startAPSearch() {
    Trace.d(this.TAG, "!!!!SH100 is ready!!!!");
    CheckVersion();
    CreateDir();
    this.nConnectCount = 0;
    if (this.proap != null) {
      this.proap.dismiss();
      this.proap = null;
    } 
    Trace.e(this.TAG, " this is runrun ---- 22");
    this.nConnectCount = 0;
  }
  
  private void startLEDTimeDisplay(int paramInt) {
    this.nTimerCount = paramInt;
    switch (this.nTimerCount) {
      default:
        this.rvf_timerCountMain.setVisibility(0);
        if (this.nTimerCount > 2) {
          this.soundPool.play(this.soundTimerBeep1, 1.0F, 1.0F, 0, 0, 1.0F);
        } else {
          break;
        } 
        setTimer(11);
        return;
      case 2:
        this.rvf_timecount.setBackgroundResource(2130838191);
        this.rvf_timerHat.setImageResource(2130837888);
      case 5:
        this.rvf_timecount.setBackgroundResource(2130838194);
        this.rvf_timerHat.setImageResource(2130837875);
      case 10:
        this.rvf_timecount.setBackgroundResource(2130838190);
        this.rvf_timerHat.setImageResource(2130837875);
    } 
    this.soundPool.play(this.soundTimerBeep2, 1.0F, 1.0F, 0, 0, 1.0F);
    setTimer(11);
  }
  
  private void startRecord() {
    this.mShotStateEx = 7;
    this.rvf_smart_panel_icon.setVisibility(8);
    rvf_title.setVisibility(4);
    hideIndicatorBar();
    this.rvf_gallery.setVisibility(8);
    this.rvf_mode_button.setVisibility(8);
    this.rvf_shutter_button.setVisibility(8);
    this.rvf_camcorder_button.setVisibility(8);
    this.rvf_rec_stop_button.setVisibility(0);
    this.rvf_rec_title.setVisibility(0);
    this.rvf_rec_time.setVisibility(0);
    this.rvf_recording_time.setText("00:00");
    this.rvf_remain_time.setText(getTimeFormat(this.mDeviceController.getRemainRecTimeValue()));
  }
  
  private void stopLEDTimeDisplay() {
    this.nTimerCount = 0;
    this.rvf_timerCountMain.setVisibility(8);
    this.soundPool.autoPause();
    this.mHandleTimer.removeMessages(11);
    shutterbutton(true);
    bshutter = false;
    this.bshotrunflag = false;
  }
  
  private void stopRecord() {
    this.mShotStateEx = 0;
    this.rvf_smart_panel_icon.setVisibility(0);
    rvf_title.setVisibility(0);
    showIndicatorBar();
    this.rvf_gallery.setVisibility(0);
    this.rvf_mode_button.setVisibility(0);
    this.rvf_shutter_button.setVisibility(0);
    this.rvf_camcorder_button.setVisibility(0);
    this.rvf_rec_stop_button.setVisibility(8);
    this.rvf_rec_stop_button.setEnabled(true);
    this.rvf_rec_title.setVisibility(8);
    this.rvf_rec_time.setVisibility(8);
  }
  
  private void timerdisplay() {
    Trace.d(this.TAG, "-=> timerdisplay bAFFAIL=" + this.bAFFAIL + " connectedSsid : " + this.connectedSsid);
    this.mHandleTimer.removeMessages(4);
    if (this.bAFFAIL && RVFFunctionManager.isCancellableShotByAFFail(this.mDeviceController.getVersionPrefix(), this.mDeviceController.getVersion(), this.connectedSsid)) {
      this.mShotState = 0;
      Trace.d(this.TAG, "do action RELEASE_SELF_TIMER 10");
      resetVisibilityTouchAFFrame();
      doAction(13, "");
      Trace.e(this.TAG, "-=> timerdisplay ReleaseSelfTimerAct_DSC DISPLAY_BUTTON_ACTIVE");
      Trace.d(this.TAG, "MESSAGE : DISPLAY_BUTTON_ACTIVE");
      nDisplayFlag = 110;
      mHandler.post(new XxxThread(nDisplayFlag));
      return;
    } 
    if (getPhoneCamType() != 1)
      setVisibilityOSD(8); 
    this.mShotState = 2;
    setShutterButtonEnabled(true);
    if (this.mDeviceController.getDriveValue().equals("Timer (2sec)")) {
      startLEDTimeDisplay(2);
      return;
    } 
    if (this.mDeviceController.getDriveValue().equals("Timer (5sec)")) {
      startLEDTimeDisplay(5);
      return;
    } 
    if (this.mDeviceController.getDriveValue().equals("Timer (10sec)")) {
      startLEDTimeDisplay(10);
      return;
    } 
    if (this.mDeviceController.getDriveValue().equals("Timer (Double)")) {
      this.mDoubleTimerShotState = 2;
      startLEDTimeDisplay(10);
      return;
    } 
  }
  
  private void updateImageOfGallaryButton() {
    Trace.d(this.TAG, "start updateImageOfGallaryButton()");
    if (CMUtil.checkOldVersionSmartCameraApp(getConnectedSSID())) {
      setImageBitmapOfGalleryButton();
      return;
    } 
    switch (getPhoneCamType()) {
      default:
        return;
      case 1:
        if (this.mDeviceController.getFileSaveValue().equals(this.mDeviceController.getFileSaveMenuItems().get(0))) {
          this.rvf_no_item.setBackgroundResource(2130837672);
          setImageBitmapOfGalleryButton();
          return;
        } 
        if (this.mDeviceController.getFileSaveValue().equals(this.mDeviceController.getFileSaveMenuItems().get(1))) {
          this.rvf_thumbnail.setImageBitmap(null);
          this.rvf_no_item.setBackgroundResource(2130837673);
          return;
        } 
        return;
      case 2:
      case 3:
      case 4:
        break;
    } 
    if (this.mDeviceController.getFileSaveValue().equals(this.mDeviceController.getFileSaveMenuItems().get(0))) {
      setImageBitmapOfGalleryButton();
      this.rvf_no_item.setBackgroundResource(2130837671);
      return;
    } 
    if (this.mDeviceController.getFileSaveValue().equals(this.mDeviceController.getFileSaveMenuItems().get(1))) {
      this.rvf_thumbnail.setImageBitmap(null);
      this.rvf_no_item.setBackgroundResource(2130837674);
      return;
    } 
  }
  
  private void updateLEDTimeDisplay(int paramInt) {
    switch (paramInt) {
      default:
        return;
      case 9:
        this.rvf_timecount.setBackgroundResource(2130838198);
        return;
      case 8:
        this.rvf_timecount.setBackgroundResource(2130838197);
        return;
      case 7:
        this.rvf_timecount.setBackgroundResource(2130838196);
        return;
      case 6:
        this.rvf_timecount.setBackgroundResource(2130838195);
        return;
      case 5:
        this.rvf_timecount.setBackgroundResource(2130838194);
        return;
      case 4:
        this.rvf_timecount.setBackgroundResource(2130838193);
        return;
      case 3:
        this.rvf_timecount.setBackgroundResource(2130838192);
        return;
      case 2:
        this.rvf_timecount.setBackgroundResource(2130838191);
        this.rvf_timerHat.setImageResource(2130837888);
        return;
      case 1:
        this.rvf_timecount.setBackgroundResource(2130838189);
        return;
      case 0:
        break;
    } 
    this.rvf_timerCountMain.setVisibility(8);
  }
  
  private void updateWheelView(WheelView paramWheelView) {
    String str = this.mDeviceController.getDialModeValue();
    switch (paramWheelView.getId()) {
      default:
        paramWheelView.invalidateWheel(true);
        return;
      case 2131558695:
        paramWheelView.setCurrentItem(this.mDeviceController.getDialModeMenuItems().indexOf(str));
        paramWheelView.setEnabled(true);
        ((ArrayWheelAdapter)paramWheelView.getViewAdapter()).setTextColor(-1);
      case 2131558696:
        paramWheelView.setCurrentItem(this.mDeviceController.getShutterSpeedMenuItems().indexOf(this.mDeviceController.getShutterSpeedValue()));
        if (str.equals("Auto") || str.equals("P") || str.equals("A")) {
          paramWheelView.setEnabled(false);
          ((ArrayWheelAdapter)paramWheelView.getViewAdapter()).setTextColor(-7829368);
        } else if (str.equals("S") || str.equals("M")) {
          paramWheelView.setEnabled(true);
          ((ArrayWheelAdapter)paramWheelView.getViewAdapter()).setTextColor(-1);
        } 
      case 2131558697:
        paramWheelView.setCurrentItem(this.mDeviceController.getApertureMenuItems().indexOf(this.mDeviceController.getApertureValue()));
        if (str.equals("Auto") || str.equals("P") || str.equals("S")) {
          paramWheelView.setEnabled(false);
          ((ArrayWheelAdapter)paramWheelView.getViewAdapter()).setTextColor(-7829368);
        } else if (str.equals("A") || str.equals("M")) {
          paramWheelView.setEnabled(true);
          ((ArrayWheelAdapter)paramWheelView.getViewAdapter()).setTextColor(-1);
        } 
      case 2131558698:
        paramWheelView.setCurrentItem(this.mDeviceController.getEVMenuItems().indexOf(this.mDeviceController.getEVValue()));
        if (str.equals("Auto") || str.equals("M")) {
          paramWheelView.setEnabled(false);
          ((ArrayWheelAdapter)paramWheelView.getViewAdapter()).setTextColor(-7829368);
        } else if (str.equals("P") || str.equals("A") || str.equals("S")) {
          paramWheelView.setEnabled(true);
          ((ArrayWheelAdapter)paramWheelView.getViewAdapter()).setTextColor(-1);
        } 
      case 2131558699:
        break;
    } 
    paramWheelView.setCurrentItem(this.mDeviceController.getISOMenuItems().indexOf(this.mDeviceController.getISOValue()));
    if (str.equals("Auto")) {
      paramWheelView.setEnabled(false);
      ((ArrayWheelAdapter)paramWheelView.getViewAdapter()).setTextColor(-7829368);
    } 
    if (str.equals("P") || str.equals("A") || str.equals("S") || str.equals("M")) {
      paramWheelView.setEnabled(true);
      ((ArrayWheelAdapter)paramWheelView.getViewAdapter()).setTextColor(-1);
    } 
  }
  
  private void updateZoomButtonEnabled() {
    if (this.nTimerCount <= 0 && RVFFunctionManager.isTouchableZoomButton(this.connectedSsid)) {
      rvf_zoomin_button.setEnabled(true);
      rvf_zoomout_button.setEnabled(true);
      return;
    } 
    rvf_zoomin_button.setEnabled(false);
    rvf_zoomout_button.setEnabled(false);
  }
  
  private void zoombardisplay() {
    rvf_zoomSeekBar.setProgress(Integer.parseInt(this.mDeviceController.getDefaultZoom()) - Integer.parseInt(this.mDeviceController.getMinZoom()));
    if (orientation == 2)
      ((VerticalSeekBar)rvf_zoomSeekBar).updateThumb(); 
  }
  
  public void CameraChange(String paramString) {
    Trace.d(this.TAG, "CameraChange() strNTS : " + paramString);
    if (paramString.contains("rotation") && !paramString.equals(this.cameratype)) {
      if (paramString.equals("ssdp:rotationD") || paramString.equals("ssdp:rotationU") || paramString.equals("ssdp:rotationL") || paramString.equals("ssdp:rotationR")) {
        this.cameratype = paramString;
        (new Thread(new Runnable() {
              public void run() {
                Trace.d(LiveShutter.this.TAG, "-=> CameraChange checkphonecamtype() orientation=" + LiveShutter.orientation);
                LiveShutter.this.checkphonecamtype();
                if (LiveShutter.this.getPhoneCamType() == 2 || LiveShutter.this.getPhoneCamType() == 1)
                  LiveShutter.this.CheckLayout(); 
              }
            })).start();
      } 
      return;
    } 
    if (paramString.contains("flash") && !paramString.substring(10).toLowerCase().equals(this.mDeviceController.getCurrentFlashDisplay().toLowerCase())) {
      int i = 0;
      while (true) {
        if (i < this.mDeviceController.getFlashDisplayMenuItems().size()) {
          if (paramString.substring(10).toLowerCase().equals(((String)this.mDeviceController.getFlashDisplayMenuItems().get(i)).toLowerCase())) {
            this.mDeviceController.setCurrentFlashDisplay(this.mDeviceController.getFlashDisplayMenuItems().get(i));
            nDisplayFlag = 123;
            mHandler.post(new XxxThread(nDisplayFlag));
            return;
          } 
          i++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public String GpsValue() {
    if (this.locationManager == null)
      this.locationManager = (LocationManager)getSystemService("location"); 
    this.currentLocation_gps = this.locationManager.getLastKnownLocation("gps");
    this.currentLocation_network = this.locationManager.getLastKnownLocation("network");
    if (this.currentLocation_gps != null || this.currentLocation_network != null) {
      if (this.currentLocation_gps != null) {
        String str2 = setGpsValue(this.currentLocation_gps);
        Trace.d(this.TAG, "GPS Value : " + str2);
        return str2;
      } 
      String str1 = setGpsValue(this.currentLocation_network);
      Trace.d(this.TAG, "GPS Value : " + str1);
      return str1;
    } 
    String str = "UNKNOWN";
    Trace.d(this.TAG, "GPS Value : " + str);
    return str;
  }
  
  public void byebye() {
    this.isByeBye = true;
    Trace.d(this.TAG, "MESSAGE : RUN_EXIT_BYEBYE");
    nDisplayFlag = 45;
    mHandler.post(new XxxThread(nDisplayFlag));
  }
  
  public void closeService() {
    Trace.d(this.TAG, "start closeService()");
    this.mHandleTimer.removeMessages(7);
    if (this.timetask != null)
      this.timetask.cancel(); 
    if (!CMUtil.checkOldVersionSmartCameraApp(getConnectedSSID())) {
      CMService.getInstance().beforefinish(0);
      codec_stop();
      FFmpegJNI.destruct();
    } else {
      codec_stop();
      FFmpegJNI.destruct();
      CMService.getInstance().beforefinish(0);
    } 
    finish();
  }
  
  public void doAction(int paramInt, String paramString) {
    Trace.d(this.TAG, "start doAction() id : " + paramInt + " param : " + paramString);
    setActionState(1);
    setTimer(5);
    if (!this.mDeviceController.doAction(paramInt, paramString)) {
      if (paramInt == 13)
        isShotProcessing = false; 
      actionEnd();
    } 
  }
  
  public int getCurrentMainOptionMenuId() {
    return this.mCurrentMainOptionMenuId;
  }
  
  public int getCurrentSubOptionMenuId() {
    return this.mCurrentSubOptionMenuId;
  }
  
  public DeviceController getDeviceController() {
    return this.mDeviceController;
  }
  
  public int getPhoneCamType() {
    return this.mPhoneCamType;
  }
  
  public int getSmartPanelCustomDialogListMenuId() {
    Trace.d(this.TAG, "start getSmartPanelCustomDialogListMenuId() mSmartPanelCustomDialogListMenuId : " + this.mSmartPanelCustomDialogListMenuId);
    return this.mSmartPanelCustomDialogListMenuId;
  }
  
  boolean isDimMenu(ImageButton paramImageButton) {
    Trace.d(this.TAG, "start isDimMenu()");
    boolean bool3 = false;
    boolean bool2 = false;
    boolean bool1 = false;
    switch (paramImageButton.getId()) {
      default:
        return false;
      case 2131558681:
      case 2131558705:
        arrayList = this.mDeviceController.getResolutionMenuItemsDim();
        i = 0;
        while (true) {
          if (i < arrayList.size()) {
            DSCMenuItem dSCMenuItem = arrayList.get(i);
            if (dSCMenuItem.getName().toUpperCase(Locale.ENGLISH).equals("QUALITY") && dSCMenuItem.getValue().toUpperCase(Locale.ENGLISH).equals(this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH))) {
              bool1 = true;
            } else {
              i++;
              continue;
            } 
          } 
          if (bool1) {
            this.rvf_smart_panel_photoSize.setImageResource(2130838019);
            return bool1;
          } 
          setImageResource(this.rvf_smart_panel_photoSize);
          return bool1;
        } 
      case 2131558686:
      case 2131558711:
        arrayList = this.mDeviceController.getFileSaveMenuItemsDim();
        i = 0;
        while (true) {
          if (i >= arrayList.size()) {
            bool1 = bool3;
          } else {
            DSCMenuItem dSCMenuItem = arrayList.get(i);
            if (dSCMenuItem.getName().toUpperCase(Locale.ENGLISH).equals("DRIVE") && dSCMenuItem.getValue().toUpperCase(Locale.ENGLISH).equals(this.mDeviceController.getDriveValue().toUpperCase(Locale.ENGLISH))) {
              bool1 = true;
            } else if (dSCMenuItem.getName().toUpperCase(Locale.ENGLISH).equals("QUALITY") && dSCMenuItem.getValue().toUpperCase(Locale.ENGLISH).equals(this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH))) {
              bool1 = true;
            } else if (dSCMenuItem.getName().toUpperCase(Locale.ENGLISH).equals("CARDSTATUS") && dSCMenuItem.getValue().toUpperCase(Locale.ENGLISH).equals(this.mDeviceController.getCardStatusValue().toUpperCase(Locale.ENGLISH))) {
              bool1 = true;
            } else {
              i++;
              continue;
            } 
          } 
          if (bool1) {
            this.rvf_smart_panel_save.setImageResource(2130838035);
            return bool1;
          } 
          setImageResource(this.rvf_smart_panel_save);
          return bool1;
        } 
      case 2131558685:
      case 2131558710:
        arrayList = this.mDeviceController.getTouchAFMenuItemsDim();
        if (this.mDeviceController.getDefaultFocusState().toUpperCase(Locale.ENGLISH).equals("MF")) {
          bool1 = true;
        } else {
          i = 0;
          while (true) {
            bool1 = bool2;
            if (i < arrayList.size()) {
              DSCMenuItem dSCMenuItem = arrayList.get(i);
              if (dSCMenuItem.getName().toUpperCase(Locale.ENGLISH).equals("AFMode") && dSCMenuItem.getValue().toUpperCase(Locale.ENGLISH).equals(this.mDeviceController.getAFModeValue().toUpperCase(Locale.ENGLISH))) {
                bool1 = true;
              } else {
                i++;
                continue;
              } 
            } 
            if (bool1) {
              this.rvf_smart_panel_touchAF.setImageResource(2130838045);
              return bool1;
            } 
            setImageResource(this.rvf_smart_panel_touchAF);
            return bool1;
          } 
        } 
        if (bool1) {
          this.rvf_smart_panel_touchAF.setImageResource(2130838045);
          return bool1;
        } 
        setImageResource(this.rvf_smart_panel_touchAF);
        return bool1;
      case 2131558684:
      case 2131558709:
        break;
    } 
    ArrayList<DSCMenuItem> arrayList = this.mDeviceController.getAFAreaMenuItemsDim();
    if (this.mDeviceController.getDefaultFocusState().toUpperCase(Locale.ENGLISH).equals("MF"))
      return true; 
    int i = 0;
    while (true) {
      if (i < arrayList.size()) {
        DSCMenuItem dSCMenuItem = arrayList.get(i);
        if (dSCMenuItem.getName().toUpperCase(Locale.ENGLISH).equals("AFMode") && dSCMenuItem.getValue().toUpperCase(Locale.ENGLISH).equals(this.mDeviceController.getAFModeValue().toUpperCase(Locale.ENGLISH)))
          return true; 
        i++;
      } 
    } 
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    Trace.d(this.TAG, "start onActivityResult() requestCode : " + paramInt1 + " resultCode : " + paramInt2);
    switch (paramInt1) {
      default:
        return;
      case 0:
        break;
    } 
    if (paramInt2 == -1) {
      if (paramIntent.getStringExtra("request_code").equals("goto_ml"))
        if (this.mDeviceController.getCardStatusValue().toUpperCase(Locale.ENGLISH).equals("EXTERNAL")) {
          this.mHandleTimer.removeMessages(7);
          this.mHandleTimer.removeMessages(5);
          codec_stop();
          FFmpegJNI.destruct();
          paramIntent = new Intent((Context)this, FileSharing.class);
          paramIntent.putExtra("requestServiceChange", "changeToML");
          startActivity(paramIntent);
          finish();
          return;
        }  
      if (!paramIntent.getStringExtra("request_code").equals("goto_rvf")) {
        this.bCloseByFinishSafe = true;
        (new UnsubscribeTask(null)).execute((Object[])new Device[] { this.upnpController.getConnectedDevice() });
        return;
      } 
    } 
  }
  
  public void onAlive() {
    Trace.d(this.TAG, "start onAlive()");
    if (!this.bConnect)
      showDialog(1016); 
    doAction(1, GpsValue());
  }
  
  public void onBackPressed() {
    setTimer(7);
    if (this.rvf_setting_sub_menu.getVisibility() == 0) {
      hideSubOptionMenu();
      return;
    } 
    if (getCurrentMainOptionMenuId() != 0) {
      hideMainOptionMenu();
      if (getPhoneCamType() == 1 && getPhoneCamType() == 1) {
        if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0)
          showZoomBar(); 
        this.rvf_shutter_button.setVisibility(0);
        return;
      } 
      return;
    } 
    if (this.rvf_smart_panel.getVisibility() == 0) {
      setSmartPanelVisibility(8);
      this.rvf_smart_panel_icon.setVisibility(0);
      rvf_title.setVisibility(0);
      showIndicatorBar();
      this.rvf_gallery.setVisibility(0);
      this.rvf_mode_button.setVisibility(0);
      this.rvf_shutter_button.setVisibility(0);
      if (this.mDeviceController.getMovieResolutionMenuItems().size() > 0)
        this.rvf_camcorder_button.setVisibility(0); 
      showZoomBar();
      return;
    } 
    if (getPhoneCamType() != 1 && rvf_QuickSettingMenu.getVisibility() == 0) {
      hideQuickSettingMenu();
      showOSD();
      return;
    } 
    super.onBackPressed();
  }
  
  public void onClick(View paramView) {
    Trace.d(this.TAG, "start onClick()");
    setTimer(7);
    if (bshutter && this.nTimerCount > 0) {
      setVisibilityOSD(0);
      stopLEDTimeDisplay();
      Trace.d(this.TAG, "do action RELEASE_SELF_TIMER 03");
      this.mShotState = 0;
      resetVisibilityTouchAFFrame();
      doAction(13, "");
    } 
    switch (paramView.getId()) {
      default:
        return;
      case 2131558687:
        doAction(39, "off");
        return;
      case 2131558694:
        Trace.d(this.TAG, "click smart panel clicked");
        return;
      case 2131558403:
        Trace.d(this.TAG, "click smart panel");
        if (this.rvf_setting_sub_menu.getVisibility() == 0) {
          hideSubOptionMenu();
          return;
        } 
        if (getCurrentMainOptionMenuId() != 0) {
          hideMainOptionMenu();
          if (getPhoneCamType() == 1 && getPhoneCamType() == 1) {
            if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0)
              showZoomBar(); 
            this.rvf_shutter_button.setVisibility(0);
          } 
          setImageResource(rvf_flash_button);
          setImageResource(rvf_timer_button);
          setImageResource(rvf_photosize_button);
          setImageResource(rvf_camerasave_button);
          setImageResource(rvf_cameramore_button);
          return;
        } 
        if (this.rvf_smart_panel.getVisibility() == 0) {
          setSmartPanelVisibility(8);
          this.rvf_smart_panel_icon.setVisibility(0);
          rvf_title.setVisibility(0);
          showIndicatorBar();
          this.rvf_gallery.setVisibility(0);
          this.rvf_mode_button.setVisibility(0);
          this.rvf_shutter_button.setVisibility(0);
          if (this.mDeviceController.getMovieResolutionMenuItems().size() > 0)
            this.rvf_camcorder_button.setVisibility(0); 
          showZoomBar();
          return;
        } 
        if (getPhoneCamType() != 1 && rvf_QuickSettingMenu.getVisibility() == 0) {
          hideQuickSettingMenu();
          showOSD();
          return;
        } 
        if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0) {
          if (rvf_zoomMain.getVisibility() == 0)
            if (getPhoneCamType() != 1) {
              hideZoomBar();
              return;
            }  
          showZoomBar();
          return;
        } 
      case 2131558481:
        Trace.d(this.TAG, "nShutterDnUp : " + nShutterDnUp + " mShotState : " + this.mShotState + " isShotProcessing : " + isShotProcessing);
        if (nShutterDnUp != 0 && !isShotProcessing) {
          if (this.mShotState == 1) {
            this.mHandleTimer.removeMessages(4);
            Trace.d(this.TAG, "do action RELEASE_SELF_TIMER 04");
            this.mShotState = 0;
            resetVisibilityTouchAFFrame();
            doAction(13, "");
          } 
          rvf_touchAutoFocus.setVisibility(4);
          Display display = ((WindowManager)getSystemService("window")).getDefaultDisplay();
          setVisibilityOSD(8);
          showSmartPanelCustomDialogWheelMenu(32, "Mode", 5, this.mDeviceController.getDialModeMenuItems().indexOf(this.mDeviceController.getDialModeValue()), this.mDeviceController.getDialModeMenuItems(), display.getWidth() / 5);
          this.mSmartPanelCustomDialogWheel.setCurrentItem(this.mDeviceController.getDialModeMenuItems().indexOf(this.mDeviceController.getDialModeValue()));
          return;
        } 
      case 2131558483:
        if (isAvailShot()) {
          if (isAvailMovieRecording()) {
            this.rvf_camcorder_button.setEnabled(false);
            if (rvf_touchAutoFocus.getVisibility() == 0) {
              this.mHandleTimer.removeMessages(4);
              rvf_touchAutoFocus.setVisibility(4);
              Trace.d(this.TAG, "do action RELEASE_SELF_TIMER 05");
              this.mShotState = 0;
              doAction(13, "");
            } 
            this.mShotState = 5;
            doAction(19, this.mDeviceController.getCurrentStreamQuality());
            return;
          } 
          showMemoryFullDialog(0);
          return;
        } 
      case 2131558484:
        this.rvf_rec_stop_button.setEnabled(false);
        this.mShotState = 0;
        doAction(36, "");
        return;
      case 2131558464:
        showQuickSettingMenu();
        hideOSD();
        return;
      case 2131558475:
        hideQuickSettingMenu();
        hideMainOptionMenu();
        showOSD();
        setImageResource(rvf_flash_button);
        setImageResource(rvf_timer_button);
        setImageResource(rvf_photosize_button);
        setImageResource(rvf_camerasave_button);
        setImageResource(rvf_cameramore_button);
        return;
      case 2131558466:
        if (getCurrentMainOptionMenuId() == 1) {
          hideMainOptionMenu();
          hideSubOptionMenu();
          if (getPhoneCamType() == 1) {
            if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0)
              showZoomBar(); 
            this.rvf_shutter_button.setVisibility(0);
          } 
          setImageResource(rvf_flash_button);
          return;
        } 
        showMainOptionMenu(1);
        hideSubOptionMenu();
        if (getPhoneCamType() == 1) {
          if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0)
            hideZoomBar(); 
          this.rvf_shutter_button.setVisibility(8);
        } 
        setImageResource(rvf_timer_button);
        setImageResource(rvf_photosize_button);
        setImageResource(rvf_camerasave_button);
        setImageResource(rvf_cameramore_button);
        setImageResourceButtonFocus(rvf_flash_button);
        return;
      case 2131558467:
        if (getCurrentMainOptionMenuId() == 2) {
          hideMainOptionMenu();
          hideSubOptionMenu();
          if (getPhoneCamType() == 1) {
            if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0)
              showZoomBar(); 
            this.rvf_shutter_button.setVisibility(0);
          } 
          setImageResource(rvf_timer_button);
          return;
        } 
        showMainOptionMenu(2);
        hideSubOptionMenu();
        if (getPhoneCamType() == 1) {
          if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0)
            hideZoomBar(); 
          this.rvf_shutter_button.setVisibility(8);
        } 
        setImageResource(rvf_flash_button);
        setImageResource(rvf_photosize_button);
        setImageResource(rvf_camerasave_button);
        setImageResource(rvf_cameramore_button);
        setImageResourceButtonFocus(rvf_timer_button);
        return;
      case 2131558468:
        if (getCurrentMainOptionMenuId() == 3) {
          hideMainOptionMenu();
          hideSubOptionMenu();
          if (getPhoneCamType() == 1) {
            if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0)
              showZoomBar(); 
            this.rvf_shutter_button.setVisibility(0);
          } 
          setImageResource(rvf_photosize_button);
          return;
        } 
        showMainOptionMenu(3);
        hideSubOptionMenu();
        if (getPhoneCamType() == 1) {
          if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0)
            hideZoomBar(); 
          this.rvf_shutter_button.setVisibility(8);
        } 
        setImageResource(rvf_flash_button);
        setImageResource(rvf_timer_button);
        setImageResource(rvf_camerasave_button);
        setImageResource(rvf_cameramore_button);
        setImageResourceButtonFocus(rvf_photosize_button);
        return;
      case 2131558469:
        if (getCurrentMainOptionMenuId() == 4) {
          hideMainOptionMenu();
          hideSubOptionMenu();
          if (getPhoneCamType() == 1) {
            if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0)
              showZoomBar(); 
            this.rvf_shutter_button.setVisibility(0);
          } 
          setImageResource(rvf_camerasave_button);
          return;
        } 
        showMainOptionMenu(4);
        hideSubOptionMenu();
        if (getPhoneCamType() == 1) {
          if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0)
            hideZoomBar(); 
          this.rvf_shutter_button.setVisibility(8);
        } 
        setImageResource(rvf_flash_button);
        setImageResource(rvf_timer_button);
        setImageResource(rvf_photosize_button);
        setImageResourceButtonFocus(rvf_camerasave_button);
        return;
      case 2131558470:
        if (getCurrentMainOptionMenuId() == 5) {
          hideMainOptionMenu();
          hideSubOptionMenu();
          if (getPhoneCamType() == 1) {
            if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0)
              showZoomBar(); 
            this.rvf_shutter_button.setVisibility(0);
          } 
          setImageResource(rvf_cameramore_button);
          return;
        } 
        showMainOptionMenu(5);
        hideSubOptionMenu();
        if (getPhoneCamType() == 1) {
          if (!RVFFunctionManager.isUnsupportedZoomableLens(this.connectedSsid) && Integer.parseInt(this.mDeviceController.getMaxZoom()) > 0)
            hideZoomBar(); 
          this.rvf_shutter_button.setVisibility(8);
        } 
        setImageResource(rvf_flash_button);
        setImageResource(rvf_timer_button);
        setImageResource(rvf_photosize_button);
        setImageResourceButtonFocus(rvf_cameramore_button);
        return;
      case 2131558492:
        if (CMUtil.checkOldVersionSmartCameraApp(getConnectedSSID()) || this.mDeviceController.getFileSaveValue().equals(this.mDeviceController.getFileSaveMenuItems().get(0))) {
          MenuClose(true);
          if (codec_init) {
            setButtonEnabled(false);
            setShutterButtonEnabled(false);
            loadingdisplay();
            return;
          } 
        } 
        if (this.mDeviceController.getFileSaveValue().equals(this.mDeviceController.getFileSaveMenuItems().get(1))) {
          this.mHandleTimer.removeMessages(7);
          Trace.d(this.TAG, "hasMessages : " + this.mHandleTimer.hasMessages(4));
          if (this.mHandleTimer.hasMessages(4)) {
            this.mHandleTimer.removeMessages(4);
            rvf_touchAutoFocus.setVisibility(4);
            Trace.d(this.TAG, "do action RELEASE_SELF_TIMER 12");
            this.mShotState = 0;
            resetVisibilityTouchAFFrame();
            this.isOperationStateChange = true;
            doAction(13, "");
            return;
          } 
          if (this.mDeviceController.getCardStatusValue().toUpperCase(Locale.ENGLISH).equals("EXTERNAL")) {
            setButtonEnabled(false);
            this.mHandleTimer.removeMessages(5);
            codec_stop();
            FFmpegJNI.destruct();
            Intent intent = new Intent((Context)this, FileSharing.class);
            intent.putExtra("requestServiceChange", "changeToML");
            startActivity(intent);
            finish();
            return;
          } 
        } 
      case 2131558462:
        if (this.mShotState == 1) {
          this.mHandleTimer.removeMessages(4);
          rvf_touchAutoFocus.setVisibility(4);
          Trace.d(this.TAG, "do action RELEASE_SELF_TIMER 06");
          this.mShotState = 0;
          resetVisibilityTouchAFFrame();
          doAction(13, "");
        } 
        setSmartPanelVisibility(0);
        this.rvf_smart_panel_icon.setVisibility(8);
        rvf_title.setVisibility(4);
        hideIndicatorBar();
        this.rvf_gallery.setVisibility(8);
        this.rvf_mode_button.setVisibility(8);
        this.rvf_shutter_button.setVisibility(8);
        this.rvf_camcorder_button.setVisibility(8);
        hideZoomBar();
        return;
      case 2131558700:
        if (getSmartPanelCustomDialogListMenuId() == 0) {
          showSmartPanelCustomDialogListMenu(25, getString(2131362008), this.mDeviceController.getWBMenuItems());
          return;
        } 
      case 2131558701:
        if (getSmartPanelCustomDialogListMenuId() == 0) {
          showSmartPanelCustomDialogListMenu(26, getString(2131362002), this.mDeviceController.getMeteringMenuItems());
          return;
        } 
      case 2131558705:
        if (getSmartPanelCustomDialogListMenuId() == 0) {
          showSmartPanelCustomDialogListMenu(2, getString(2131361998), (ArrayList<String>)null);
          return;
        } 
      case 2131558706:
        if (getSmartPanelCustomDialogListMenuId() == 0) {
          showSmartPanelCustomDialogListMenu(33, getString(2131362004), this.mDeviceController.getQualityMenuItems());
          return;
        } 
      case 2131558707:
        if (getSmartPanelCustomDialogListMenuId() == 0) {
          showSmartPanelCustomDialogListMenu(34, getString(2131362005), (ArrayList<String>)null);
          return;
        } 
      case 2131558704:
        if (getSmartPanelCustomDialogListMenuId() == 0) {
          showSmartPanelCustomDialogListMenu(27, "AF Mode", this.mDeviceController.getAFModeMenuItems());
          return;
        } 
      case 2131558702:
        if (getSmartPanelCustomDialogListMenuId() == 0) {
          showSmartPanelCustomDialogListMenu(29, getString(2131362003), this.mDeviceController.getDriveMenuItems());
          return;
        } 
      case 2131558703:
        if (getSmartPanelCustomDialogListMenuId() == 0) {
          showSmartPanelCustomDialogListMenu(17, getString(2131361982), this.mDeviceController.getFlashMenuItems());
          return;
        } 
      case 2131558709:
        if (getSmartPanelCustomDialogListMenuId() == 0) {
          showSmartPanelCustomDialogListMenu(28, getString(2131362006), this.mDeviceController.getAFAreaMenuItems());
          return;
        } 
      case 2131558710:
        if (getSmartPanelCustomDialogListMenuId() == 0) {
          showSmartPanelCustomDialogListMenu(11, getString(2131362007), this.mDeviceController.getTouchAFMenuItems());
          return;
        } 
      case 2131558711:
        if (getSmartPanelCustomDialogListMenuId() == 0) {
          showSmartPanelCustomDialogListMenu(37, getString(2131362000), this.mDeviceController.getFileSaveMenuItems());
          return;
        } 
      case 2131558708:
        break;
    } 
    if (getSmartPanelCustomDialogListMenuId() == 0) {
      showSmartPanelCustomDialogListMenu(18, getString(2131362017), this.mDeviceController.getStreamQualityMenuItems());
      return;
    } 
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    setTimer(7);
    orientation = paramConfiguration.orientation;
    switch (paramConfiguration.orientation) {
      default:
        CommonUtils.setSystemConfigurationChanged(getApplicationContext());
        super.onConfigurationChanged(paramConfiguration);
        return;
      case 1:
      case 2:
        break;
    } 
    Trace.d(this.TAG, "-=> onConfigurationChanged checkphonecamtype() orientation=" + orientation);
    checkphonecamtype();
    Createchanged();
    if (getPhoneCamType() == 2) {
      CheckLayout();
    } else if (getCurrentMainOptionMenuId() != 0) {
      CheckLayout();
    } 
    Trace.d(this.TAG, "onConfigurationChanged start!!!!!");
  }
  
  public void onCreate(Bundle paramBundle) {
    Trace.i(this.TAG, "Performance Check Point : start onCreate()");
    Trace.d(this.TAG, "**** liveshutter oncreate");
    super.onCreate(paramBundle);
    this.mDestroyed = false;
    CMService.ACTIVE_SERVICE = 2;
    init();
    setTimer(7);
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    bNoTelephonyDevice = Utils.CheckTelephonyDevice(Build.MODEL);
    mExistStatusBarDeviceForTablet = Utils.CheckexistStatusBarDeviceForTablet(Build.MODEL);
    Trace.d(this.TAG, "model name : " + Build.MODEL + ",is No telephony device: " + bNoTelephonyDevice);
    this.vibe = (Vibrator)getSystemService("vibrator");
    Utils.setUseragent(getApplicationContext());
    Trace.d(this.TAG, "language : " + (getResources().getConfiguration()).locale.getLanguage());
    Appcontext = getApplicationContext();
    msgHandler = new Handler() {
        public void handleMessage(Message param1Message) {
          // Byte code:
          //   0: aload_0
          //   1: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   4: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
          //   7: new java/lang/StringBuilder
          //   10: dup
          //   11: ldc 'msg : '
          //   13: invokespecial <init> : (Ljava/lang/String;)V
          //   16: aload_1
          //   17: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
          //   20: invokevirtual toString : ()Ljava/lang/String;
          //   23: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
          //   26: aload_1
          //   27: getfield what : I
          //   30: tableswitch default -> 112, 126 -> 118, 127 -> 278, 128 -> 501, 129 -> 707, 130 -> 734, 131 -> 761, 132 -> 788, 133 -> 1027, 134 -> 1241, 135 -> 1283, 136 -> 1367, 137 -> 1451, 138 -> 1589, 139 -> 1624, 140 -> 1115, 141 -> 112, 142 -> 1793
          //   112: aload_0
          //   113: aload_1
          //   114: invokespecial handleMessage : (Landroid/os/Message;)V
          //   117: return
          //   118: aload_1
          //   119: getfield obj : Ljava/lang/Object;
          //   122: checkcast java/lang/String
          //   125: ldc 'NULL'
          //   127: invokevirtual equals : (Ljava/lang/Object;)Z
          //   130: ifne -> 112
          //   133: ldc ''
          //   135: astore_3
          //   136: aload_1
          //   137: getfield obj : Ljava/lang/Object;
          //   140: checkcast java/lang/String
          //   143: ldc 'D'
          //   145: invokevirtual equals : (Ljava/lang/Object;)Z
          //   148: ifeq -> 215
          //   151: ldc 'ssdp:rotationD'
          //   153: astore_3
          //   154: aload_3
          //   155: aload_0
          //   156: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   159: invokestatic access$24 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Ljava/lang/String;
          //   162: invokevirtual equals : (Ljava/lang/Object;)Z
          //   165: ifne -> 112
          //   168: aload_0
          //   169: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   172: aload_3
          //   173: invokestatic access$25 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Ljava/lang/String;)V
          //   176: aload_0
          //   177: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   180: invokestatic access$26 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
          //   183: aload_0
          //   184: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   187: invokevirtual getPhoneCamType : ()I
          //   190: iconst_2
          //   191: if_icmpeq -> 205
          //   194: aload_0
          //   195: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   198: invokevirtual getPhoneCamType : ()I
          //   201: iconst_1
          //   202: if_icmpne -> 112
          //   205: aload_0
          //   206: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   209: invokestatic access$27 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
          //   212: goto -> 112
          //   215: aload_1
          //   216: getfield obj : Ljava/lang/Object;
          //   219: checkcast java/lang/String
          //   222: ldc 'L'
          //   224: invokevirtual equals : (Ljava/lang/Object;)Z
          //   227: ifeq -> 236
          //   230: ldc 'ssdp:rotationL'
          //   232: astore_3
          //   233: goto -> 154
          //   236: aload_1
          //   237: getfield obj : Ljava/lang/Object;
          //   240: checkcast java/lang/String
          //   243: ldc 'U'
          //   245: invokevirtual equals : (Ljava/lang/Object;)Z
          //   248: ifeq -> 257
          //   251: ldc 'ssdp:rotationU'
          //   253: astore_3
          //   254: goto -> 154
          //   257: aload_1
          //   258: getfield obj : Ljava/lang/Object;
          //   261: checkcast java/lang/String
          //   264: ldc 'R'
          //   266: invokevirtual equals : (Ljava/lang/Object;)Z
          //   269: ifeq -> 154
          //   272: ldc 'ssdp:rotationR'
          //   274: astore_3
          //   275: goto -> 154
          //   278: aload_1
          //   279: getfield obj : Ljava/lang/Object;
          //   282: checkcast java/lang/String
          //   285: astore_3
          //   286: aload_3
          //   287: ldc 'NULL'
          //   289: invokevirtual equals : (Ljava/lang/Object;)Z
          //   292: ifne -> 112
          //   295: aload_3
          //   296: invokestatic isNumeric : (Ljava/lang/String;)Z
          //   299: ifeq -> 474
          //   302: aload_3
          //   303: invokestatic parseInt : (Ljava/lang/String;)I
          //   306: ifne -> 415
          //   309: aload_0
          //   310: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   313: invokestatic access$6 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Ljava/lang/String;
          //   316: invokestatic getFlashProtocolValueType : (Ljava/lang/String;)I
          //   319: tableswitch default -> 336, 2 -> 400
          //   336: aload_0
          //   337: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   340: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   343: ldc 'off'
          //   345: invokevirtual setCurrentFlashDisplay : (Ljava/lang/String;)V
          //   348: aload_0
          //   349: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   352: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   355: invokevirtual getVersionPrefix : ()Ljava/lang/String;
          //   358: aload_0
          //   359: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   362: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   365: invokevirtual getVersion : ()Ljava/lang/String;
          //   368: invokestatic isSupportedSmartPanel : (Ljava/lang/String;Ljava/lang/String;)Z
          //   371: ifeq -> 488
          //   374: aload_0
          //   375: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   378: aload_0
          //   379: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   382: invokestatic access$30 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
          //   385: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
          //   388: aload_0
          //   389: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   392: ldc 2131558680
          //   394: invokestatic access$32 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
          //   397: goto -> 112
          //   400: aload_0
          //   401: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   404: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   407: ldc 'OFF'
          //   409: invokevirtual setCurrentFlashDisplay : (Ljava/lang/String;)V
          //   412: goto -> 348
          //   415: aload_0
          //   416: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   419: invokestatic access$6 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Ljava/lang/String;
          //   422: invokestatic getFlashProtocolValueType : (Ljava/lang/String;)I
          //   425: tableswitch default -> 444, 2 -> 459
          //   444: aload_0
          //   445: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   448: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   451: ldc 'auto'
          //   453: invokevirtual setCurrentFlashDisplay : (Ljava/lang/String;)V
          //   456: goto -> 348
          //   459: aload_0
          //   460: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   463: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   466: ldc 'AUTO'
          //   468: invokevirtual setCurrentFlashDisplay : (Ljava/lang/String;)V
          //   471: goto -> 348
          //   474: aload_0
          //   475: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   478: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   481: aload_3
          //   482: invokevirtual setCurrentFlashDisplay : (Ljava/lang/String;)V
          //   485: goto -> 348
          //   488: aload_0
          //   489: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   492: invokestatic access$31 : ()Landroid/widget/ImageButton;
          //   495: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
          //   498: goto -> 388
          //   501: aload_1
          //   502: getfield obj : Ljava/lang/Object;
          //   505: checkcast java/lang/Integer
          //   508: invokevirtual intValue : ()I
          //   511: ifne -> 586
          //   514: aload_0
          //   515: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   518: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   521: ldc 'af'
          //   523: invokevirtual setDefaultFocusState : (Ljava/lang/String;)V
          //   526: aload_0
          //   527: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   530: aload_0
          //   531: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   534: invokestatic access$33 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
          //   537: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
          //   540: aload_0
          //   541: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   544: invokestatic access$33 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
          //   547: iconst_1
          //   548: invokevirtual setEnabled : (Z)V
          //   551: aload_0
          //   552: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   555: aload_0
          //   556: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   559: invokestatic access$34 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
          //   562: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
          //   565: aload_0
          //   566: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   569: invokestatic access$34 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
          //   572: iconst_1
          //   573: invokevirtual setEnabled : (Z)V
          //   576: aload_0
          //   577: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   580: invokestatic access$74 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
          //   583: goto -> 112
          //   586: invokestatic access$36 : ()Landroid/widget/ImageView;
          //   589: iconst_4
          //   590: invokevirtual setVisibility : (I)V
          //   593: aload_0
          //   594: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   597: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   600: ldc 'mf'
          //   602: invokevirtual setDefaultFocusState : (Ljava/lang/String;)V
          //   605: aload_0
          //   606: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   609: aload_0
          //   610: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   613: invokestatic access$33 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
          //   616: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
          //   619: aload_0
          //   620: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   623: invokestatic access$33 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
          //   626: iconst_0
          //   627: invokevirtual setEnabled : (Z)V
          //   630: aload_0
          //   631: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   634: aload_0
          //   635: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   638: invokestatic access$34 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
          //   641: invokestatic access$12 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Landroid/widget/ImageButton;)V
          //   644: aload_0
          //   645: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   648: invokestatic access$34 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageButton;
          //   651: iconst_0
          //   652: invokevirtual setEnabled : (Z)V
          //   655: aload_0
          //   656: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   659: invokevirtual getSmartPanelCustomDialogListMenuId : ()I
          //   662: bipush #11
          //   664: if_icmpeq -> 679
          //   667: aload_0
          //   668: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   671: invokevirtual getSmartPanelCustomDialogListMenuId : ()I
          //   674: bipush #28
          //   676: if_icmpne -> 697
          //   679: aload_0
          //   680: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   683: iconst_0
          //   684: invokevirtual setSmartPanelCustomDialogListMenuId : (I)V
          //   687: aload_0
          //   688: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   691: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/dialog/CustomDialogListMenu;
          //   694: invokevirtual dismiss : ()V
          //   697: aload_0
          //   698: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   701: invokestatic access$74 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
          //   704: goto -> 112
          //   707: aload_0
          //   708: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   711: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   714: aload_1
          //   715: getfield obj : Ljava/lang/Object;
          //   718: checkcast java/lang/String
          //   721: invokevirtual setShutterSpeedValue : (Ljava/lang/String;)V
          //   724: aload_0
          //   725: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   728: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
          //   731: goto -> 112
          //   734: aload_0
          //   735: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   738: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   741: aload_1
          //   742: getfield obj : Ljava/lang/Object;
          //   745: checkcast java/lang/String
          //   748: invokevirtual setApertureValue : (Ljava/lang/String;)V
          //   751: aload_0
          //   752: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   755: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
          //   758: goto -> 112
          //   761: aload_0
          //   762: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   765: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   768: aload_1
          //   769: getfield obj : Ljava/lang/Object;
          //   772: checkcast java/lang/String
          //   775: invokevirtual setEVValue : (Ljava/lang/String;)V
          //   778: aload_0
          //   779: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   782: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
          //   785: goto -> 112
          //   788: aload_1
          //   789: getfield obj : Ljava/lang/Object;
          //   792: checkcast java/lang/String
          //   795: ldc 'NULL'
          //   797: invokevirtual equals : (Ljava/lang/Object;)Z
          //   800: ifne -> 112
          //   803: aload_1
          //   804: getfield obj : Ljava/lang/Object;
          //   807: checkcast java/lang/String
          //   810: ldc ''
          //   812: invokevirtual equals : (Ljava/lang/Object;)Z
          //   815: ifne -> 835
          //   818: aload_0
          //   819: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   822: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   825: aload_1
          //   826: getfield obj : Ljava/lang/Object;
          //   829: checkcast java/lang/String
          //   832: invokevirtual setAvailShots : (Ljava/lang/String;)V
          //   835: aload_0
          //   836: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   839: invokestatic access$38 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)I
          //   842: iconst_5
          //   843: if_icmpne -> 866
          //   846: aload_0
          //   847: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   850: invokestatic access$39 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
          //   853: ifeq -> 866
          //   856: aload_0
          //   857: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   860: invokestatic access$40 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
          //   863: ifeq -> 983
          //   866: aload_0
          //   867: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   870: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   873: invokevirtual getDriveValue : ()Ljava/lang/String;
          //   876: getstatic java/util/Locale.ENGLISH : Ljava/util/Locale;
          //   879: invokevirtual toUpperCase : (Ljava/util/Locale;)Ljava/lang/String;
          //   882: ldc_w 'CONTINUOUS'
          //   885: invokevirtual equals : (Ljava/lang/Object;)Z
          //   888: ifeq -> 922
          //   891: aload_0
          //   892: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   895: invokestatic access$38 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)I
          //   898: iconst_5
          //   899: if_icmpne -> 922
          //   902: aload_0
          //   903: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   906: bipush #6
          //   908: invokestatic access$41 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
          //   911: aload_0
          //   912: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   915: bipush #31
          //   917: ldc ''
          //   919: invokevirtual doAction : (ILjava/lang/String;)V
          //   922: aload_0
          //   923: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   926: iconst_1
          //   927: invokestatic access$42 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
          //   930: bipush #111
          //   932: invokestatic access$28 : (I)V
          //   935: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.mHandler : Landroid/os/Handler;
          //   938: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$XxxThread
          //   941: dup
          //   942: aload_0
          //   943: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   946: invokestatic access$29 : ()I
          //   949: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
          //   952: invokevirtual post : (Ljava/lang/Runnable;)Z
          //   955: pop
          //   956: iconst_0
          //   957: invokestatic access$43 : (Z)V
          //   960: aload_0
          //   961: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   964: iconst_0
          //   965: invokestatic access$44 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Z)V
          //   968: aload_0
          //   969: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   972: iconst_0
          //   973: invokestatic access$41 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
          //   976: aload_0
          //   977: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   980: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
          //   983: aload_0
          //   984: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   987: invokestatic access$40 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
          //   990: ifeq -> 1006
          //   993: aload_0
          //   994: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   997: sipush #1009
          //   1000: invokevirtual showDialog : (I)V
          //   1003: goto -> 112
          //   1006: aload_0
          //   1007: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1010: invokestatic access$39 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Z
          //   1013: ifne -> 112
          //   1016: aload_0
          //   1017: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1020: iconst_1
          //   1021: invokestatic access$45 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;I)V
          //   1024: goto -> 112
          //   1027: aload_1
          //   1028: getfield obj : Ljava/lang/Object;
          //   1031: checkcast java/lang/String
          //   1034: ldc 'NULL'
          //   1036: invokevirtual equals : (Ljava/lang/Object;)Z
          //   1039: ifne -> 112
          //   1042: aload_0
          //   1043: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1046: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   1049: aload_1
          //   1050: getfield obj : Ljava/lang/Object;
          //   1053: checkcast java/lang/String
          //   1056: invokevirtual setAFShotResult : (Ljava/lang/String;)V
          //   1059: aload_0
          //   1060: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1063: invokestatic access$15 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)I
          //   1066: ifne -> 1092
          //   1069: aload_0
          //   1070: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1073: invokestatic access$46 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
          //   1076: aload_0
          //   1077: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1080: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   1083: invokevirtual getAFShotResult : ()Ljava/lang/String;
          //   1086: invokevirtual downloadImage : (Ljava/lang/String;)V
          //   1089: goto -> 112
          //   1092: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$ShotPrepareTask
          //   1095: dup
          //   1096: aload_0
          //   1097: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1100: aconst_null
          //   1101: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter$ShotPrepareTask;)V
          //   1104: iconst_0
          //   1105: anewarray java/lang/String
          //   1108: invokevirtual execute : ([Ljava/lang/Object;)Landroid/os/AsyncTask;
          //   1111: pop
          //   1112: goto -> 112
          //   1115: aload_0
          //   1116: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1119: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
          //   1122: ldc_w 'Const.MsgId.ON_DOWNLOAD_SUCCESS'
          //   1125: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
          //   1128: aload_1
          //   1129: getfield obj : Ljava/lang/Object;
          //   1132: checkcast java/lang/String
          //   1135: invokestatic getThumbnail : (Ljava/lang/String;)Landroid/graphics/Bitmap;
          //   1138: invokestatic access$150 : (Landroid/graphics/Bitmap;)V
          //   1141: aload_0
          //   1142: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1145: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
          //   1148: new java/lang/StringBuilder
          //   1151: dup
          //   1152: ldc_w 'scan file path 01 : '
          //   1155: invokespecial <init> : (Ljava/lang/String;)V
          //   1158: aload_1
          //   1159: getfield obj : Ljava/lang/Object;
          //   1162: checkcast java/lang/String
          //   1165: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   1168: invokevirtual toString : ()Ljava/lang/String;
          //   1171: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
          //   1174: aload_1
          //   1175: getfield obj : Ljava/lang/Object;
          //   1178: checkcast java/lang/String
          //   1181: invokestatic getMimeType : (Ljava/lang/String;)Ljava/lang/String;
          //   1184: astore_3
          //   1185: invokestatic access$151 : ()Landroid/content/Context;
          //   1188: iconst_1
          //   1189: anewarray java/lang/String
          //   1192: dup
          //   1193: iconst_0
          //   1194: aload_1
          //   1195: getfield obj : Ljava/lang/Object;
          //   1198: checkcast java/lang/String
          //   1201: aastore
          //   1202: iconst_1
          //   1203: anewarray java/lang/String
          //   1206: dup
          //   1207: iconst_0
          //   1208: aload_3
          //   1209: aastore
          //   1210: aconst_null
          //   1211: invokestatic scanFile : (Landroid/content/Context;[Ljava/lang/String;[Ljava/lang/String;Landroid/media/MediaScannerConnection$OnScanCompletedListener;)V
          //   1214: aload_0
          //   1215: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1218: invokestatic access$131 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/configuration/manager/SRVFConfigurationManager;
          //   1221: aload_1
          //   1222: getfield obj : Ljava/lang/Object;
          //   1225: checkcast java/lang/String
          //   1228: invokevirtual setLastSaveFileName : (Ljava/lang/String;)V
          //   1231: aload_0
          //   1232: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1235: invokestatic access$20 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
          //   1238: goto -> 112
          //   1241: aload_1
          //   1242: getfield obj : Ljava/lang/Object;
          //   1245: checkcast java/lang/String
          //   1248: ldc 'NULL'
          //   1250: invokevirtual equals : (Ljava/lang/Object;)Z
          //   1253: ifne -> 112
          //   1256: aload_0
          //   1257: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1260: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   1263: aload_1
          //   1264: getfield obj : Ljava/lang/Object;
          //   1267: checkcast java/lang/String
          //   1270: invokevirtual setDefaultZoom : (Ljava/lang/String;)V
          //   1273: aload_0
          //   1274: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1277: invokestatic access$47 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
          //   1280: goto -> 112
          //   1283: aload_1
          //   1284: getfield obj : Ljava/lang/Object;
          //   1287: checkcast java/lang/String
          //   1290: ldc 'NULL'
          //   1292: invokevirtual equals : (Ljava/lang/Object;)Z
          //   1295: ifne -> 112
          //   1298: aload_0
          //   1299: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1302: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   1305: invokevirtual getApertureMenuItems : ()Ljava/util/ArrayList;
          //   1308: invokevirtual clear : ()V
          //   1311: aload_1
          //   1312: getfield obj : Ljava/lang/Object;
          //   1315: checkcast java/lang/String
          //   1318: ldc_w ','
          //   1321: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
          //   1324: astore_3
          //   1325: iconst_0
          //   1326: istore_2
          //   1327: iload_2
          //   1328: aload_3
          //   1329: arraylength
          //   1330: if_icmplt -> 1343
          //   1333: aload_0
          //   1334: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1337: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
          //   1340: goto -> 112
          //   1343: aload_0
          //   1344: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1347: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   1350: invokevirtual getApertureMenuItems : ()Ljava/util/ArrayList;
          //   1353: aload_3
          //   1354: iload_2
          //   1355: aaload
          //   1356: invokevirtual add : (Ljava/lang/Object;)Z
          //   1359: pop
          //   1360: iload_2
          //   1361: iconst_1
          //   1362: iadd
          //   1363: istore_2
          //   1364: goto -> 1327
          //   1367: aload_1
          //   1368: getfield obj : Ljava/lang/Object;
          //   1371: checkcast java/lang/String
          //   1374: ldc 'NULL'
          //   1376: invokevirtual equals : (Ljava/lang/Object;)Z
          //   1379: ifne -> 112
          //   1382: aload_0
          //   1383: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1386: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   1389: invokevirtual getShutterSpeedMenuItems : ()Ljava/util/ArrayList;
          //   1392: invokevirtual clear : ()V
          //   1395: aload_1
          //   1396: getfield obj : Ljava/lang/Object;
          //   1399: checkcast java/lang/String
          //   1402: ldc_w ','
          //   1405: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
          //   1408: astore_3
          //   1409: iconst_0
          //   1410: istore_2
          //   1411: iload_2
          //   1412: aload_3
          //   1413: arraylength
          //   1414: if_icmplt -> 1427
          //   1417: aload_0
          //   1418: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1421: invokestatic access$37 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)V
          //   1424: goto -> 112
          //   1427: aload_0
          //   1428: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1431: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   1434: invokevirtual getShutterSpeedMenuItems : ()Ljava/util/ArrayList;
          //   1437: aload_3
          //   1438: iload_2
          //   1439: aaload
          //   1440: invokevirtual add : (Ljava/lang/Object;)Z
          //   1443: pop
          //   1444: iload_2
          //   1445: iconst_1
          //   1446: iadd
          //   1447: istore_2
          //   1448: goto -> 1411
          //   1451: aload_1
          //   1452: getfield obj : Ljava/lang/Object;
          //   1455: checkcast java/lang/String
          //   1458: ldc 'NULL'
          //   1460: invokevirtual equals : (Ljava/lang/Object;)Z
          //   1463: ifne -> 112
          //   1466: aload_0
          //   1467: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1470: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   1473: aload_1
          //   1474: getfield obj : Ljava/lang/Object;
          //   1477: checkcast java/lang/String
          //   1480: invokevirtual setMovieRecordTime : (Ljava/lang/String;)V
          //   1483: aload_0
          //   1484: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1487: invokestatic access$48 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/TextView;
          //   1490: aload_0
          //   1491: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1494: aload_1
          //   1495: getfield obj : Ljava/lang/Object;
          //   1498: checkcast java/lang/String
          //   1501: invokestatic access$49 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Ljava/lang/String;)Ljava/lang/String;
          //   1504: invokevirtual setText : (Ljava/lang/CharSequence;)V
          //   1507: aload_1
          //   1508: getfield obj : Ljava/lang/Object;
          //   1511: checkcast java/lang/String
          //   1514: aload_0
          //   1515: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1518: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   1521: invokevirtual getRemainRecTimeValue : ()Ljava/lang/String;
          //   1524: invokevirtual equals : (Ljava/lang/Object;)Z
          //   1527: ifeq -> 1544
          //   1530: aload_0
          //   1531: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1534: bipush #36
          //   1536: ldc ''
          //   1538: invokevirtual doAction : (ILjava/lang/String;)V
          //   1541: goto -> 112
          //   1544: aload_0
          //   1545: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1548: invokestatic access$50 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/common/Timer;
          //   1551: ifnonnull -> 112
          //   1554: aload_0
          //   1555: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1558: new com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/common/Timer
          //   1561: dup
          //   1562: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter.msgHandler : Landroid/os/Handler;
          //   1565: sipush #139
          //   1568: bipush #100
          //   1570: invokespecial <init> : (Landroid/os/Handler;II)V
          //   1573: invokestatic access$51 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/common/Timer;)V
          //   1576: aload_0
          //   1577: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1580: invokestatic access$50 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/common/Timer;
          //   1583: invokevirtual start : ()V
          //   1586: goto -> 112
          //   1589: aload_1
          //   1590: getfield obj : Ljava/lang/Object;
          //   1593: checkcast java/lang/String
          //   1596: ldc 'NULL'
          //   1598: invokevirtual equals : (Ljava/lang/Object;)Z
          //   1601: ifne -> 112
          //   1604: aload_0
          //   1605: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1608: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   1611: aload_1
          //   1612: getfield obj : Ljava/lang/Object;
          //   1615: checkcast java/lang/String
          //   1618: invokevirtual setRemainRecTimeValue : (Ljava/lang/String;)V
          //   1621: goto -> 112
          //   1624: aload_0
          //   1625: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1628: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
          //   1631: new java/lang/StringBuilder
          //   1634: dup
          //   1635: ldc_w 'movie record time : '
          //   1638: invokespecial <init> : (Ljava/lang/String;)V
          //   1641: aload_1
          //   1642: getfield obj : Ljava/lang/Object;
          //   1645: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
          //   1648: invokevirtual toString : ()Ljava/lang/String;
          //   1651: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
          //   1654: aload_1
          //   1655: getfield obj : Ljava/lang/Object;
          //   1658: checkcast java/lang/Long
          //   1661: invokevirtual longValue : ()J
          //   1664: ldc2_w 1000
          //   1667: ldiv
          //   1668: invokestatic valueOf : (J)Ljava/lang/Long;
          //   1671: astore_3
          //   1672: aload_3
          //   1673: invokevirtual longValue : ()J
          //   1676: aload_0
          //   1677: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1680: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   1683: invokevirtual getRemainRecTimeValue : ()Ljava/lang/String;
          //   1686: invokestatic parseLong : (Ljava/lang/String;)J
          //   1689: lcmp
          //   1690: ifge -> 1780
          //   1693: aload_0
          //   1694: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1697: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   1700: aload_3
          //   1701: invokevirtual longValue : ()J
          //   1704: lconst_1
          //   1705: ladd
          //   1706: invokestatic valueOf : (J)Ljava/lang/String;
          //   1709: invokevirtual setMovieRecordTime : (Ljava/lang/String;)V
          //   1712: aload_0
          //   1713: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1716: invokestatic access$48 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/TextView;
          //   1719: aload_0
          //   1720: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1723: aload_0
          //   1724: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1727: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
          //   1730: invokevirtual getMovieRecordTime : ()Ljava/lang/String;
          //   1733: invokestatic access$49 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Ljava/lang/String;)Ljava/lang/String;
          //   1736: invokevirtual setText : (Ljava/lang/CharSequence;)V
          //   1739: aload_3
          //   1740: invokevirtual longValue : ()J
          //   1743: ldc2_w 2
          //   1746: lrem
          //   1747: lconst_0
          //   1748: lcmp
          //   1749: ifne -> 1766
          //   1752: aload_0
          //   1753: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1756: invokestatic access$166 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageView;
          //   1759: iconst_0
          //   1760: invokevirtual setVisibility : (I)V
          //   1763: goto -> 112
          //   1766: aload_0
          //   1767: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1770: invokestatic access$166 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/widget/ImageView;
          //   1773: iconst_4
          //   1774: invokevirtual setVisibility : (I)V
          //   1777: goto -> 112
          //   1780: aload_0
          //   1781: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1784: invokestatic access$50 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/common/Timer;
          //   1787: invokevirtual interrupt : ()V
          //   1790: goto -> 112
          //   1793: aload_1
          //   1794: getfield obj : Ljava/lang/Object;
          //   1797: checkcast java/lang/String
          //   1800: astore_3
          //   1801: aload_0
          //   1802: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1805: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
          //   1808: new java/lang/StringBuilder
          //   1811: dup
          //   1812: ldc_w 'rvf message : '
          //   1815: invokespecial <init> : (Ljava/lang/String;)V
          //   1818: aload_3
          //   1819: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
          //   1822: invokevirtual toString : ()Ljava/lang/String;
          //   1825: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
          //   1828: aload_3
          //   1829: ldc_w 'HTTP_UNAUTHORIZED'
          //   1832: invokevirtual contains : (Ljava/lang/CharSequence;)Z
          //   1835: ifeq -> 1858
          //   1838: aload_0
          //   1839: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1842: aload_0
          //   1843: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1846: ldc_w 2131361829
          //   1849: invokevirtual getString : (I)Ljava/lang/String;
          //   1852: invokestatic access$167 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Ljava/lang/String;)V
          //   1855: goto -> 112
          //   1858: aload_3
          //   1859: ldc_w 'HTTP_UNAVAILABLE'
          //   1862: invokevirtual contains : (Ljava/lang/CharSequence;)Z
          //   1865: ifeq -> 112
          //   1868: aload_0
          //   1869: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1872: aload_0
          //   1873: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
          //   1876: ldc_w 2131361830
          //   1879: invokevirtual getString : (I)Ljava/lang/String;
          //   1882: invokestatic access$167 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Ljava/lang/String;)V
          //   1885: goto -> 112
        }
      };
    this.mImageDownloader = new ImageDownloader(msgHandler, 140);
    if (Utils.isNexusOne()) {
      this.nZoomGap = 4000;
      this.nAFGap = 4000;
      this.btoastflag = false;
    } else {
      this.nZoomGap = 900;
      this.nAFGap = 2000;
      this.btoastflag = true;
    } 
    if (configChanged) {
      this.mExToast.show(8);
      Trace.d(this.TAG, "onCreate  configChanged is true");
      configChanged = false;
      mHandler.postDelayed(new XxxThread(124), 3000L);
      return;
    } 
    Process.setThreadPriority(-8);
    System.gc();
    getWindow().addFlags(128);
    this.locationManager = (LocationManager)getSystemService("location");
    (new Criteria()).setAccuracy(1);
    int i = 0;
    while (true) {
      if (i >= 4) {
        switch (RVFFunctionManager.getLiveViewResolutionType(this.connectedSsid)) {
          default:
            if (this.mDeviceController.getRatioOffsetMenuItems().size() > 0) {
              screenConfigChange(2);
            } else {
              screenConfigChange(Utils.stringToRatioType(this.mDeviceController.getRatioValue()));
            } 
            if (this.mDeviceController.isConnected()) {
              setContentView();
              initLayout();
              holderremove();
              holder = this.rvf_surface.getHolder();
              Trace.d(this.TAG, "Add callback of surface holder");
              holder.addCallback(this);
            } 
            bgbitmap1 = Bitmap.createBitmap(this.surfaceViewHeight, this.surfaceViewWidth, Bitmap.Config.RGB_565);
            bgbitmap = Bitmap.createBitmap(this.surfaceViewWidth + 18, this.surfaceViewHeight, Bitmap.Config.RGB_565);
            FFmpegJNI.construct(bgbitmap1, bgbitmap, this.screenPositoin, this.screenSize, getPhoneCamType() - 1, 0);
            bClosing = false;
            if (!isLowBattery()) {
              if (this.upnpController.isConnected()) {
                showDialog(1016);
                this.upnpController.setEventHandler(this.mEventHandler);
                doAction(1, GpsValue());
                isButtonShow = true;
                return;
              } 
              break;
            } 
            return;
          case 1:
            this.mDeviceController.setRatioValue("16:9");
          case 2:
            this.mDeviceController.setRatioValue("4:3");
          case 3:
            this.mDeviceController.setRatioValue("3:2");
          case 4:
            this.mDeviceController.setRatioValue("1:1");
        } 
      } else {
        this.screenPositoin[i] = new int[2];
        this.screenSize[i] = new int[2];
        i++;
        continue;
      } 
      startAPSearch();
      return;
    } 
  }
  
  protected Dialog onCreateDialog(int paramInt, Bundle paramBundle) {
    CustomDialog customDialog;
    String str2;
    Dialog dialog;
    String str1;
    TextView textView;
    Trace.d(this.TAG, "start onCreateDialog() id : " + paramInt);
    switch (paramInt) {
      default:
        return super.onCreateDialog(paramInt, paramBundle);
      case 2008:
        customDialog = new CustomDialog((Context)this);
        customDialog.setIcon(2130838178);
        customDialog.setTitle(2131361934);
        customDialog.setMessage(getString(2131362001));
        customDialog.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                LiveShutter.this.dismissDialog(2008);
                if (FunctionManager.isNotSupportedRawQualityAndContinuousShotAtTheSameTime(LiveShutter.this.connectedSsid) && LiveShutter.this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).contains("RAW") && LiveShutter.this.mDeviceController.getDriveValue().toUpperCase(Locale.ENGLISH).contains("CONTINUOUS"))
                  LiveShutter.this.showDialog(2007); 
              }
            });
        return (Dialog)customDialog;
      case 2007:
        customDialog = new CustomDialog((Context)this);
        customDialog.setIcon(2130838178);
        customDialog.setTitle(2131361934);
        customDialog.setMessage(getString(2131362070));
        customDialog.setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                LiveShutter.this.doAction(33, "Normal");
                LiveShutter.this.dismissDialog(2007);
              }
            });
        return (Dialog)customDialog;
      case 1012:
        return getCustomDialog();
      case 1002:
        this.nCurShowDlg = 1002;
        return (Dialog)(new AlertDialog.Builder((Context)this)).setIcon(2130838178).setTitle(2131361934).setMessage(2131361946).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
            }).setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                return (param1Int == 84 && param1KeyEvent.getRepeatCount() == 0);
              }
            }).create();
      case 1007:
        return (Dialog)(new AlertDialog.Builder((Context)this)).setIcon(2130838178).setTitle(2131361821).setMessage(2131361824).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                LiveShutter.this.appClose();
              }
            }).setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                return !((param1Int != 84 || param1KeyEvent.getRepeatCount() != 0) && (param1Int != 4 || param1KeyEvent.getRepeatCount() != 0));
              }
            }).create();
      case 1003:
        this.nCurShowDlg = 1003;
        getWindow().clearFlags(128);
        return (Dialog)(new AlertDialog.Builder((Context)this)).setIcon(2130838178).setTitle(2131361821).setMessage(2131361937).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                LiveShutter.this.nCurShowDlg = -1;
                Trace.d(LiveShutter.this.TAG, "Const.MsgId.MSGBOX_DSC_NOTSEARCH");
                LiveShutter.this.showDialog(1011);
                LiveShutter.this.backap();
              }
            }).setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                return !((param1Int != 84 || param1KeyEvent.getRepeatCount() != 0) && (param1Int != 4 || param1KeyEvent.getRepeatCount() != 0));
              }
            }).create();
      case 1005:
        this.nCurShowDlg = 1005;
        return (Dialog)(bClosing ? null : (new AlertDialog.Builder((Context)this)).setIcon(2130838178).setTitle(2131361821).setMessage(2131361940).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                LiveShutter.this.nCurShowDlg = -1;
                Trace.d(LiveShutter.this.TAG, "Const.MsgId.MSGBOX_NOTSERVICEVERSION");
                LiveShutter.this.showDialog(1011);
                LiveShutter.this.holderremove();
                LiveShutter.this.appclose();
              }
            }).setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                return !((param1Int != 84 || param1KeyEvent.getRepeatCount() != 0) && (param1Int != 4 || param1KeyEvent.getRepeatCount() != 0));
              }
            }).create());
      case 1006:
        this.nCurShowDlg = 1006;
        return (Dialog)((bClosing || !this.bStart) ? null : (new AlertDialog.Builder((Context)this)).setIcon(2130838178).setTitle(2131361934).setMessage(2131361944).setCancelable(false).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                if (LiveShutter.bClosing)
                  return; 
                LiveShutter.this.nCurShowDlg = -1;
                Trace.d(LiveShutter.this.TAG, "Const.MsgId.MSGBOX_MEMORYFULL");
              }
            }).create());
      case 1008:
        this.nCurShowDlg = 1008;
        return (Dialog)((bClosing || !this.bStart) ? null : (new AlertDialog.Builder((Context)this)).setIcon(2130838178).setTitle(2131361934).setMessage(2131361945).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                LiveShutter.this.nCurShowDlg = -1;
                if (LiveShutter.this.bexitflag) {
                  Trace.d(LiveShutter.this.TAG, "Const.MsgId.MSGBOX_MEMORYFULLERROR");
                  LiveShutter.this.showDialog(1011);
                  LiveShutter.this.holderremove();
                  LiveShutter.this.appclose();
                } 
              }
            }).create());
      case 1009:
        this.nCurShowDlg = 1009;
        return (Dialog)(new AlertDialog.Builder((Context)this)).setIcon(2130838178).setTitle(2131361934).setMessage(2131361945).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                LiveShutter.this.nCurShowDlg = -1;
                Trace.d(LiveShutter.this.TAG, "onClick() Const.MsgId.MSGBOX_DCF_FULL_ERROR");
                LiveShutter.this.showDialog(1011);
                LiveShutter.this.holderremove();
                LiveShutter.this.onUnsubscribe();
              }
            }).create();
      case 1010:
        if (bClosing || !this.bStart)
          return null; 
        str2 = getString(2131361947);
        if (this.streampro == null)
          this.streampro = new ProgressDialog((Context)this); 
        this.streampro.setMessage(str2);
        this.streampro.setIndeterminate(true);
        this.streampro.setProgressStyle(0);
        this.streampro.setCancelable(false);
        this.streampro.getWindow().clearFlags(2);
        this.streampro.setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                return (param1KeyEvent.getKeyCode() == 84);
              }
            });
        return (Dialog)this.streampro;
      case 1016:
        dialog = new Dialog((Context)this);
        dialog.requestWindowFeature(1);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(2130903051);
        textView = (TextView)dialog.findViewById(2131558521);
        ((LinearLayout)dialog.findViewById(2131558528)).setVisibility(0);
        ((TextView)dialog.findViewById(2131558529)).setText(2131362018);
        textView.setText(2131362019);
        ((ImageView)dialog.findViewById(2131558530)).startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), 2130968576));
        return dialog;
      case 1011:
        Trace.e(this.TAG, "-=> Const.MsgBoxId.PROGRESS_BAR_DISPLAY_EXIT_1");
        if (this.proap != null) {
          this.proap.dismiss();
          this.proap = null;
        } 
        hideProgressBar();
        str1 = getString(2131361956);
        if (this.streampro == null)
          this.streampro = new ProgressDialog((Context)this); 
        this.streampro.setMessage(str1);
        this.streampro.setIndeterminate(true);
        this.streampro.setProgressStyle(0);
        this.streampro.setCancelable(false);
        this.streampro.setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                return (param1KeyEvent.getKeyCode() == 84);
              }
            });
        return (Dialog)this.streampro;
      case 1013:
        return (Dialog)(new AlertDialog.Builder((Context)this)).setIcon(2130838178).setTitle(2131361966).setMessage(str1.getString("DIALOG_MESSAGE_KEY")).setCancelable(false).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                param1DialogInterface.dismiss();
                LiveShutter.this.setImageResource(LiveShutter.rvf_flash_button);
              }
            }).create();
      case 1014:
        return (Dialog)(new AlertDialog.Builder((Context)this)).setIcon(2130838247).setTitle(2131361821).setMessage(2131361822).setCancelable(false).setPositiveButton(2131361842, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                LiveShutter.this.showDialog(1011);
                LiveShutter.mHandler.sendEmptyMessageDelayed(11, 1000L);
                LiveShutter.this.holderremove();
                LiveShutter.this.appclose();
              }
            }).create();
      case 1015:
        return (Dialog)(new AlertDialog.Builder((Context)this)).setIcon(2130838178).setTitle(2131361934).setMessage(2131362013).setPositiveButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                LiveShutter.this.streampro.dismiss();
              }
            }).setOnKeyListener(new DialogInterface.OnKeyListener() {
              public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
                return (param1Int == 84 && param1KeyEvent.getRepeatCount() == 0);
              }
            }).create();
      case 1017:
        switch (RVFFunctionManager.getSaveGuideDialogType(this.mDeviceController.getVersionPrefix(), this.mDeviceController.getVersion(), this.connectedSsid)) {
          default:
            break;
          case 1:
            return SmartPhoneSaveGuideDialog();
          case 2:
            return SmartPhoneSaveGuideDialogForCSC();
          case 3:
            break;
        } 
        return SmartPhoneSaveGuideDialogForDSC();
      case 1018:
        break;
    } 
    return (Dialog)(new AlertDialog.Builder((Context)this)).setIcon(2130838178).setMessage("RECORD ERROR: CODEC ERROR!").setPositiveButton(17039370, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {}
        }).create();
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu) {
    return super.onCreateOptionsMenu(paramMenu);
  }
  
  protected void onDestroy() {
    Trace.d(Trace.Tag.RVF, "start onDestroy()");
    this.mDestroyed = true;
    if (this.timetask != null)
      this.timetask.cancel(); 
    if (this.mTimer != null) {
      this.mTimer.cancel();
      this.mTimer = null;
    } 
    sendBroadcast(new Intent("action_disconnect"));
    super.onDestroy();
    if (CMUtil.isForceClose((ActivityManager)getSystemService("activity")))
      SystemClock.sleep(3000L); 
  }
  
  public void onItemClicked(WheelView paramWheelView, int paramInt) {
    setTimer(7);
    Trace.d(this.TAG, "rvf_smart_panel : " + this.rvf_smart_panel.getMeasuredWidth() + " rvf_smart_panel_shutterSpeed : " + this.rvf_smart_panel_shutterSpeed.getMeasuredWidth());
    switch (paramWheelView.getId()) {
      default:
        return;
      case 2131558695:
        showSmartPanelCustomDialogWheelMenu(32, "Mode", 5, this.mDeviceController.getDialModeMenuItems().indexOf(this.mDeviceController.getDialModeValue()), this.mDeviceController.getDialModeMenuItems(), 84);
        return;
      case 2131558696:
        showSmartPanelCustomDialogWheelMenu(21, "Shutter Speed", 3, this.mDeviceController.getShutterSpeedMenuItems().indexOf(this.mDeviceController.getShutterSpeedValue()), this.mDeviceController.getShutterSpeedMenuItems(), this.rvf_smart_panel.getMeasuredWidth() / 3);
        return;
      case 2131558697:
        showSmartPanelCustomDialogWheelMenu(22, "Aperture", 5, this.mDeviceController.getApertureMenuItems().indexOf(this.mDeviceController.getApertureValue()), this.mDeviceController.getApertureMenuItems(), this.rvf_smart_panel.getMeasuredWidth() / 5);
        return;
      case 2131558698:
        showSmartPanelCustomDialogWheelMenu(23, "EV", 5, this.mDeviceController.getEVMenuItems().indexOf(this.mDeviceController.getEVValue()), this.mDeviceController.getEVMenuItems(), this.rvf_smart_panel.getMeasuredWidth() / 5);
        return;
      case 2131558699:
        break;
    } 
    showSmartPanelCustomDialogWheelMenu(24, "ISO", 5, this.mDeviceController.getISOMenuItems().indexOf(this.mDeviceController.getISOValue()), this.mDeviceController.getISOMenuItems(), this.rvf_smart_panel.getMeasuredWidth() / 3);
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
    if (paramMenuItem.getTitle().equals("capture")) {
      String str = String.valueOf(Utils.getDefaultStorage()) + "/test00.jpg";
      Trace.d(this.TAG, "strFilePath : " + str);
      Graph.saveBitmapToJPEGFile(FFmpegJNI.getCurrentFrame(), str);
      return super.onOptionsItemSelected(paramMenuItem);
    } 
    if (paramMenuItem.getTitle().equals("goto ML")) {
      showDialog(3001);
      this.mHandleTimer.removeMessages(7);
      codec_stop();
      FFmpegJNI.destruct();
      doAction(43, "changeToML");
      return super.onOptionsItemSelected(paramMenuItem);
    } 
    if (paramMenuItem.getGroupId() == 35) {
      doAction(19, this.mDeviceController.getCurrentStreamQuality());
      return super.onOptionsItemSelected(paramMenuItem);
    } 
    if (paramMenuItem.getGroupId() == 36) {
      doAction(paramMenuItem.getGroupId(), paramMenuItem.getTitle().toString());
      return super.onOptionsItemSelected(paramMenuItem);
    } 
    if (paramMenuItem.getGroupId() > 1)
      doAction(paramMenuItem.getGroupId(), paramMenuItem.getTitle().toString()); 
    return super.onOptionsItemSelected(paramMenuItem);
  }
  
  public void onPause() {
    Trace.d(this.TAG, "start onPause()");
    super.onPause();
    if (this.mZoomState == 1) {
      this.mZoomState = 0;
      this.rvf_progress.setVisibility(8);
      doAction(8, "Tele");
      return;
    } 
    if (this.mZoomState == 2) {
      this.mZoomState = 0;
      this.rvf_progress.setVisibility(8);
      doAction(8, "Wide");
      return;
    } 
    switch (this.mShotStateEx) {
      default:
        return;
      case 5:
        break;
    } 
    this.mShotStateEx = 6;
    doAction(31, "");
  }
  
  protected void onResume() {
    super.onResume();
    Trace.d(this.TAG, "**** liveshutter onresume");
    if (bClosing) {
      hideProgressBar();
      if (this.proap != null) {
        this.proap.dismiss();
        this.proap = null;
      } 
      Trace.d(this.TAG, "onResume1");
      showDialog(1011);
      holderremove();
      appclose();
      return;
    } 
    CreateDir();
    if (this.mDeviceController.isConnected()) {
      updateImageOfGallaryButton();
      setButtonEnabled(true);
      if (this.nCameraSaveSelect == 0 && CommonUtils.isMemoryFull()) {
        this.mExToast.show(7);
        setShutterButtonEnabled(false);
        return;
      } 
      if (this.bConnect) {
        setShutterButtonEnabled(true);
        return;
      } 
    } 
  }
  
  public void onScrollingFinished(WheelView paramWheelView) {
    setTimer(7);
    switch (paramWheelView.getId()) {
      default:
        return;
      case 2131558695:
        if (!this.mDeviceController.getDialModeValue().equals(this.mDeviceController.getDialModeMenuItems().get(this.rvf_smart_panel_mode.getCurrentItem()))) {
          this.mHandleTimer.removeMessages(4);
          rvf_touchAutoFocus.setVisibility(4);
          this.mDeviceController.setDialModeValue(this.mDeviceController.getDialModeMenuItems().get(this.rvf_smart_panel_mode.getCurrentItem()));
          doAction(32, this.mDeviceController.getDialModeMenuItems().get(this.rvf_smart_panel_mode.getCurrentItem()));
          return;
        } 
      case 2131558696:
        if (!this.mDeviceController.getShutterSpeedValue().equals(this.mDeviceController.getShutterSpeedMenuItems().get(this.rvf_smart_panel_shutterSpeed.getCurrentItem()))) {
          this.mDeviceController.setShutterSpeedValue(this.mDeviceController.getShutterSpeedMenuItems().get(this.rvf_smart_panel_shutterSpeed.getCurrentItem()));
          doAction(21, this.mDeviceController.getShutterSpeedMenuItems().get(this.rvf_smart_panel_shutterSpeed.getCurrentItem()));
          return;
        } 
      case 2131558697:
        if (!this.mDeviceController.getApertureValue().equals(this.mDeviceController.getApertureMenuItems().get(this.rvf_smart_panel_aperture.getCurrentItem()))) {
          this.mDeviceController.setApertureValue(this.mDeviceController.getApertureMenuItems().get(this.rvf_smart_panel_aperture.getCurrentItem()));
          doAction(22, this.mDeviceController.getApertureMenuItems().get(this.rvf_smart_panel_aperture.getCurrentItem()));
          return;
        } 
      case 2131558698:
        if (!this.mDeviceController.getEVValue().equals(this.mDeviceController.getEVMenuItems().get(this.rvf_smart_panel_ev.getCurrentItem()))) {
          this.mDeviceController.setEVValue(this.mDeviceController.getEVMenuItems().get(this.rvf_smart_panel_ev.getCurrentItem()));
          doAction(23, this.mDeviceController.getEVMenuItems().get(this.rvf_smart_panel_ev.getCurrentItem()));
          return;
        } 
      case 2131558699:
        break;
    } 
    if (!this.mDeviceController.getISOValue().equals(this.mDeviceController.getISOMenuItems().get(this.rvf_smart_panel_iso.getCurrentItem()))) {
      this.mDeviceController.setISOValue(this.mDeviceController.getISOMenuItems().get(this.rvf_smart_panel_iso.getCurrentItem()));
      doAction(24, this.mDeviceController.getISOMenuItems().get(this.rvf_smart_panel_iso.getCurrentItem()));
      return;
    } 
  }
  
  public void onScrollingStarted(WheelView paramWheelView) {
    setTimer(7);
    switch (paramWheelView.getId()) {
    
    } 
  }
  
  protected void onStop() {
    super.onStop();
    Trace.d(this.TAG, "Application Received onSTOP...");
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
    int i;
    int j;
    Trace.d(this.TAG, "start onTouch() action : " + paramMotionEvent.getAction());
    switch (paramMotionEvent.getAction()) {
      default:
      
      case 0:
        setTimer(7);
        switch (paramView.getId()) {
          case 2131558403:
          case 2131558484:
          case 0:
            return false;
          case 2131558477:
            return true;
          case 2131558481:
            Trace.d(this.TAG, "camcorder button isPressed : " + this.rvf_camcorder_button.isPressed());
            return this.rvf_camcorder_button.isPressed();
          case 2131558482:
            Trace.d(this.TAG, "camcorder button isPressed : " + this.rvf_camcorder_button.isPressed());
            if (this.rvf_camcorder_button.isPressed())
              return true; 
            nShutterDnUp = 0;
            MenuClose(true);
            if (isAvailShot()) {
              isShotProcessing = true;
              long l = System.currentTimeMillis();
              Trace.d(this.TAG, "-=> rvf_shutter_button ctime=" + l + ", shotcanceltime=" + this.shotcanceltime + ", ctime-shotcanceltime=" + (l - this.shotcanceltime) + ", nAFGap=" + this.nAFGap);
              Trace.d(this.TAG, "-=> rvf_shutter_button bshotrunflag=" + this.bshotrunflag);
              if (l - this.shotcanceltime > this.nAFGap) {
                if (getActionState() != 1) {
                  buttondisplay(true);
                  if (this.mShotState != 3 && this.mShotState != 4 && this.mShotState != 2) {
                    this.bshotrunflag = true;
                    this.shotcanceltime = System.currentTimeMillis();
                    this.mHandleTimer.removeMessages(11);
                    if (rvf_touchAutoFocus.getVisibility() == 0 && this.mDeviceController.getDefaultFocusState().equals("af")) {
                      if (getActionState() != 1) {
                        this.mHandleTimer.removeMessages(4);
                        this.mShotState = 1;
                        doAction(12, String.valueOf(String.valueOf(this.positionX)) + "x" + String.valueOf(this.positionY));
                      } 
                      return false;
                    } 
                  } else {
                    return false;
                  } 
                  this.mShotStateEx = 2;
                  if (RVFFunctionManager.getAFMode(this.connectedSsid) == 0) {
                    this.mShotState = 1;
                    doAction(9, "");
                    return false;
                  } 
                  if (RVFFunctionManager.getAFMode(this.connectedSsid) == 1) {
                    this.mShotState = 1;
                    doAction(10, "");
                  } 
                  return false;
                } 
                this.bshotrunflag = false;
                return false;
              } 
            } else {
              return false;
            } 
            this.bshotrunflag = false;
            return false;
          case 2131558483:
            Trace.d(this.TAG, "shutter button isPressed : " + this.rvf_shutter_button.isPressed());
            return this.rvf_mode_button.isPressed() | this.rvf_shutter_button.isPressed();
          case 2131558479:
            if (!isShotProcessing) {
              if (RVFFunctionManager.isSupportedZoomLongKey(this.connectedSsid, this.mDeviceController.getVersionPrefix(), this.mDeviceController.getVersion())) {
                if (Integer.parseInt(this.mDeviceController.getDefaultZoom()) < Integer.parseInt(this.mDeviceController.getMaxZoom())) {
                  setShutterButtonEnabled(false);
                  this.mZoomState = 1;
                  this.rvf_progress.setVisibility(0);
                  if (rvf_touchAutoFocus.getVisibility() == 0) {
                    this.mHandleTimer.removeMessages(4);
                    Trace.d(this.TAG, "do action RELEASE_SELF_TIMER 13");
                    this.mShotState = 0;
                    resetVisibilityTouchAFFrame();
                    doAction(13, "");
                  } 
                  doAction(7, "Tele");
                } 
                return true;
              } 
            } else {
              return true;
            } 
            this.isLongZoom = true;
            ZoomIn();
            return true;
          case 2131558478:
            break;
        } 
        if (!isShotProcessing) {
          if (RVFFunctionManager.isSupportedZoomLongKey(this.connectedSsid, this.mDeviceController.getVersionPrefix(), this.mDeviceController.getVersion())) {
            if (Integer.parseInt(this.mDeviceController.getDefaultZoom()) > Integer.parseInt(this.mDeviceController.getMinZoom())) {
              setShutterButtonEnabled(false);
              this.mZoomState = 2;
              this.rvf_progress.setVisibility(0);
              if (rvf_touchAutoFocus.getVisibility() == 0) {
                this.mHandleTimer.removeMessages(4);
                Trace.d(this.TAG, "do action RELEASE_SELF_TIMER 13");
                this.mShotState = 0;
                resetVisibilityTouchAFFrame();
                doAction(13, "");
              } 
              doAction(7, "Wide");
            } 
            return true;
          } 
        } else {
          return true;
        } 
        this.isLongZoom = true;
        ZoomOut();
        return true;
      case 1:
        break;
    } 
    setTimer(7);
    switch (paramView.getId()) {
      case 2131558477:
      case 2131558481:
      case 2131558483:
      case 2131558484:
      case 0:
      
      case 2131558403:
        if ((this.mShotStateEx != 7 || (this.mDeviceController.getAction(40) != null && this.mDeviceController.getAction(39) != null)) && isAvailShot() && getSmartPanelVisibility() != 0) {
          Trace.d(this.TAG, "mShotStateEx : " + this.mShotStateEx + " isShotProcessing : " + isShotProcessing + " focus state : " + this.mDeviceController.getDefaultFocusState() + " nTimerCount : " + this.nTimerCount + " action State : " + getActionState() + " touch AF value : " + this.mDeviceController.getTouchAFValue());
          if (!isShotProcessing && this.mDeviceController.getDefaultFocusState().equals("af") && this.nTimerCount == 0 && getActionState() != 1 && !this.mDeviceController.getTouchAFValue().toUpperCase(Locale.ENGLISH).equals("OFF")) {
            int k;
            int m;
            this.mFrameWidth = rvf_touchAutoFocus.getWidth();
            this.mFrameHeight = rvf_touchAutoFocus.getHeight();
            int n = (int)paramMotionEvent.getX();
            int i1 = (int)paramMotionEvent.getY();
            Trace.d(this.TAG, "<<<<< getRatioOffsetMenuItems().size() : " + this.mDeviceController.getRatioOffsetMenuItems().size());
            if (this.mDeviceController.getRatioOffsetMenuItems().size() > 0) {
              i = this.mDeviceController.getRatioOffsetValue().getWidth() * this.screenSize[getPhoneCamType() - 1][0] / 640 + this.screenPositoin[getPhoneCamType() - 1][0];
              j = this.mDeviceController.getRatioOffsetValue().getHeight() * this.screenSize[getPhoneCamType() - 1][1] / 480;
              m = this.screenSize[getPhoneCamType() - 1][0] - this.mDeviceController.getRatioOffsetValue().getWidth() * 2 * this.screenSize[getPhoneCamType() - 1][0] / 640;
              k = this.screenSize[getPhoneCamType() - 1][1] - j * 2;
            } else {
              i = this.screenPositoin[getPhoneCamType() - 1][0];
              j = this.screenPositoin[getPhoneCamType() - 1][1];
              m = this.screenSize[getPhoneCamType() - 1][0];
              k = this.screenSize[getPhoneCamType() - 1][1];
            } 
            Trace.d(this.TAG, "<<<<< screenSize W : " + m + " H : " + k + " frame W : " + this.mFrameWidth + " H : " + this.mFrameHeight);
            Trace.d(this.TAG, "x : " + n + " screenPositionX : " + i + " y : " + i1 + " screenPositionY : " + j);
            if (n > i && n < i + m && i1 > j && i1 < j + k) {
              this.mOrientationWhenCheckMargin = orientation;
              this.mMarginLeft = n - this.mFrameWidth / 2;
              this.mMarginTop = i1 - this.mFrameHeight / 2;
              if (this.mMarginLeft < i) {
                this.mMarginLeft = i;
              } else if (this.mMarginLeft + this.mFrameWidth > i + m) {
                this.mMarginLeft = i + m - this.mFrameWidth;
              } 
              if (this.mMarginTop < j) {
                this.mMarginTop = j;
              } else if (this.mMarginTop + this.mFrameHeight > j + k) {
                this.mMarginTop = j + k - this.mFrameHeight;
              } 
              ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(rvf_touchAutoFocus.getLayoutParams());
              marginLayoutParams.setMargins(this.mMarginLeft, this.mMarginTop, 0, 0);
              rvf_touchAutoFocus.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(marginLayoutParams));
              nShutterDnUp = -1;
              this.positionX = this.mMarginLeft + this.mFrameWidth / 2;
              this.positionY = this.mMarginTop + this.mFrameHeight / 2;
              this.positionX -= i;
              this.positionY -= j;
              this.positionX = this.positionX * 100 / m;
              this.positionY = this.positionY * 100 / k;
              if (this.positionX < 8)
                this.positionX = 8; 
              if (this.positionY > 88)
                this.positionY = 88; 
              Trace.d(this.TAG, "mMarginLeft : " + this.mMarginLeft + " mMarginTop : " + this.mMarginTop + " positionX : " + this.positionX + " Y : " + this.positionY);
              if (this.mShotStateEx == 7)
                doAction(40, String.valueOf(String.valueOf(this.positionX)) + "x" + String.valueOf(this.positionY)); 
              rvf_touchAutoFocus.setVisibility(0);
              rvf_touchAutoFocus.setImageResource(2130837575);
              if (this.mDeviceController.getTouchAFValue().toUpperCase(Locale.ENGLISH).equals("TOUCH AF")) {
                this.mShotState = 1;
                doAction(12, String.valueOf(String.valueOf(this.positionX)) + "x" + String.valueOf(this.positionY));
                setShutterButtonEnabled(false);
              } 
              if (this.mDeviceController.getAFAreaValue().toUpperCase(Locale.ENGLISH).equals("MULTI AF") || this.mDeviceController.getTouchAFValue().toUpperCase(Locale.ENGLISH).equals("TOUCH AF"))
                setTimer(4); 
            } 
          } 
        } 
      case 2131558482:
        if (isAvailShot()) {
          i = (int)paramMotionEvent.getX();
          j = (int)paramMotionEvent.getY();
          int k = this.rvf_shutter_button.getWidth();
          int m = this.rvf_shutter_button.getHeight();
          if (i < 0 || j < 0 || i > k || j > m) {
            nShutterDnUp = -1;
            if (this.mDeviceController.getDriveValue().toUpperCase(Locale.ENGLISH).equals("CONTINUOUS")) {
              if (this.mShotStateEx == 5) {
                this.mShotStateEx = 6;
                doAction(31, "");
              } 
              return false;
            } 
            if (this.bshotrunflag) {
              stopLEDTimeDisplay();
              this.mHandleTimer.removeMessages(4);
              Trace.d(this.TAG, "do action RELEASE_SELF_TIMER 00");
              this.mShotState = 0;
              resetVisibilityTouchAFFrame();
              doAction(13, "");
            } 
            return false;
          } 
        } else {
          return false;
        } 
        nShutterDnUp = 1;
        if (this.mDeviceController.getDriveValue().toUpperCase(Locale.ENGLISH).equals("CONTINUOUS")) {
          if (this.bAFFAIL && RVFFunctionManager.isCancellableShotByAFFail(this.mDeviceController.getVersionPrefix(), this.mDeviceController.getVersion(), this.connectedSsid)) {
            Trace.d(this.TAG, "do action RELEASE_SELF_TIMER 02");
            this.mShotState = 0;
            resetVisibilityTouchAFFrame();
            doAction(13, "");
            Trace.e(this.TAG, "-=> shutter_button10 ReleaseSelfTimerAct_DSC DISPLAY_BUTTON_ACTIVE");
            Trace.d(this.TAG, "MESSAGE : DISPLAY_BUTTON_ACTIVE");
            nDisplayFlag = 110;
            mHandler.post(new XxxThread(nDisplayFlag));
            return false;
          } 
          if (this.mShotStateEx == 5) {
            this.mShotStateEx = 6;
            doAction(31, "");
          } 
          return false;
        } 
        isShotProcessing = true;
        Trace.d(this.TAG, "-=> shutter_button2 ACTION_UP");
        buttondisplay(true);
        Trace.d(this.TAG, "-=> shutter_button3 ACTION_UP bshutter=" + bshutter + ", bshotrunflag=" + this.bshotrunflag);
        if (!bshutter && this.bshotrunflag) {
          Trace.d(this.TAG, "MESSAGE : DISPLAY_AFDONE");
          nDisplayFlag = 9;
          mHandler.post(new XxxThread(nDisplayFlag));
        } 
        Trace.d(this.TAG, "-=> shutter_button4 ACTION_UP touchX=" + i + ", touchY=" + j);
        Trace.d(this.TAG, "-=> shutterbutton(true)-2 mShotState : " + this.mShotState);
        if (!this.mDeviceController.getDriveValue().toUpperCase(Locale.ENGLISH).equals("SINGLE") && this.mShotState == 2) {
          stopLEDTimeDisplay();
          Trace.d(this.TAG, "do action RELEASE_SELF_TIMER 01");
          this.mShotState = 0;
          resetVisibilityTouchAFFrame();
          doAction(13, "");
          return false;
        } 
        Trace.d(this.TAG, "-=> shutterbutton(false)-5 bshotrunflag=" + this.bshotrunflag);
        if (this.bshotrunflag) {
          bshutter = true;
          shutterbutton(false);
          if (this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("SINGLE") || this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("OFF")) {
            this.mHandleTimer.removeMessages(4);
            setShutterButtonEnabled(false);
            if (getActionState() == 2)
              if (this.AfFrameType == 4) {
                hideAFExtend();
              } else {
                hideAutoFocusFrame();
              }  
            Trace.d(this.TAG, "-=> shutter_button8 ACTION_UP");
            (new Thread(new Runnable() {
                  public void run() {
                    while (true) {
                      if (LiveShutter.this.getActionState() == 2) {
                        LiveShutter.this.bshotrunflag = false;
                        Trace.d(LiveShutter.this.TAG, "-=> shutter_button10 ACTION_UP bAFFAIL=" + LiveShutter.this.bAFFAIL);
                        if (LiveShutter.this.bAFFAIL && RVFFunctionManager.isCancellableShotByAFFail(LiveShutter.this.mDeviceController.getVersionPrefix(), LiveShutter.this.mDeviceController.getVersion(), LiveShutter.this.connectedSsid)) {
                          Trace.d(LiveShutter.this.TAG, "do action RELEASE_SELF_TIMER 02");
                          LiveShutter.this.mShotState = 0;
                          LiveShutter.this.doAction(13, "");
                          Trace.e(LiveShutter.this.TAG, "-=> shutter_button10 ReleaseSelfTimerAct_DSC DISPLAY_BUTTON_ACTIVE");
                          Trace.d(LiveShutter.this.TAG, "MESSAGE : DISPLAY_BUTTON_ACTIVE");
                          LiveShutter.nDisplayFlag = 110;
                          LiveShutter.mHandler.post(new LiveShutter.XxxThread(LiveShutter.nDisplayFlag));
                          return;
                        } 
                        break;
                      } 
                      try {
                        Thread.sleep(500L);
                      } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                      } 
                    } 
                    Trace.d(LiveShutter.this.TAG, "-=> shutter_button11 ACTION_UP");
                    Trace.d(LiveShutter.this.TAG, "MESSAGE : RUN_DISPLAY_SHOT");
                    LiveShutter.nDisplayFlag = 47;
                    LiveShutter.mHandler.post(new LiveShutter.XxxThread(LiveShutter.nDisplayFlag));
                  }
                })).start();
            return false;
          } 
          if (this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("2SEC") || this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("TIMER (2SEC)") || this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("5SEC") || this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("TIMER (5SEC)") || this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("10SEC") || this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("TIMER (10SEC)") || this.mDeviceController.getCurrentLEDTimeMenuItem().equals("Double")) {
            if (getActionState() == 2) {
              if (this.AfFrameType == 4) {
                hideAFExtend();
              } else {
                hideAutoFocusFrame();
              } 
              rvf_touchAutoFocus.setVisibility(4);
              timerdisplay();
            } 
            if (RVFFunctionManager.isSupportedShutterUpAct(this.connectedSsid)) {
              nDisplayFlag = 50;
              mHandler.post(new XxxThread(nDisplayFlag));
            } 
          } 
          return false;
        } 
        isShotProcessing = false;
        return false;
      case 2131558479:
        if (RVFFunctionManager.isSupportedZoomLongKey(this.connectedSsid, this.mDeviceController.getVersionPrefix(), this.mDeviceController.getVersion())) {
          if (this.mZoomState == 1) {
            rvf_zoomin_button.setEnabled(false);
            rvf_zoomout_button.setEnabled(false);
            doAction(8, "Tele");
          } 
          return true;
        } 
        this.isLongZoom = false;
        return true;
      case 2131558478:
        break;
    } 
    if (RVFFunctionManager.isSupportedZoomLongKey(this.connectedSsid, this.mDeviceController.getVersionPrefix(), this.mDeviceController.getVersion())) {
      if (this.mZoomState == 2) {
        rvf_zoomin_button.setEnabled(false);
        rvf_zoomout_button.setEnabled(false);
        doAction(8, "Wide");
      } 
      return true;
    } 
    this.isLongZoom = false;
    return true;
  }
  
  public void setCurrentMainOptionMenuId(int paramInt) {
    this.mCurrentMainOptionMenuId = paramInt;
  }
  
  public void setCurrentSubOptionMenuId(int paramInt) {
    this.mCurrentSubOptionMenuId = paramInt;
  }
  
  public void setPhoneCamType(int paramInt) {
    this.mPhoneCamType = paramInt;
  }
  
  public void setSmartPanelCustomDialogListMenuId(int paramInt) {
    this.mSmartPanelCustomDialogListMenuId = paramInt;
  }
  
  public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3) {
    Trace.d(this.TAG, "start surfaceChanged() isSettedLiveStream : " + this.isSettedLiveStream);
    FFmpegJNI.request(FFmpegJNI.Command.SURFACE_CHANGED, paramSurfaceHolder);
    if (this.mShotStateEx == 7 && this.mDeviceController.getStreamUrlMenuItems().size() <= 2)
      FFmpegJNI.onFreezeDisplay(); 
    if (!this.isSettedLiveStream)
      GetLiveStream(); 
  }
  
  public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
    Trace.d(this.TAG, "start surfaceCreated()");
    if (!this.bStart)
      this.bStart = true; 
    buttondisplay(true);
    setImageResource(rvf_flash_button);
    setImageResource(rvf_timer_button);
    setImageResource(rvf_photosize_button);
    setImageResource(rvf_camerasave_button);
    setImageResource(rvf_cameramore_button);
    Trace.d(this.TAG, "-=> shutterbutton(false)-3");
    shutterbutton(false);
    Trace.d(this.TAG, "-=> shutterbutton(false)-4 mShotState : " + this.mShotState);
    if (this.mShotState != 0 && this.mShotState != 1) {
      shutterbutton(false);
      if (this.nTimerCount > 0) {
        setShutterButtonEnabled(true);
      } else {
        setShutterButtonEnabled(false);
      } 
    } else {
      shutterbutton(true);
    } 
    mHandler = new Handler() {
        public void handleMessage(Message param1Message) {
          Trace.d(LiveShutter.this.TAG, "mHandler msg.what : " + param1Message.what);
          switch (param1Message.what) {
            default:
              return;
            case 1:
              l = System.currentTimeMillis();
              LiveShutter.this.buttondisplay(true);
              if (l - LiveShutter.this.zoomtime > LiveShutter.this.nZoomGap) {
                LiveShutter.rvf_zoomin_button.setImageResource(2130838208);
                if (Integer.parseInt(LiveShutter.this.mDeviceController.getDefaultZoom()) != Integer.parseInt(LiveShutter.this.mDeviceController.getMaxZoom()) && LiveShutter.codec_init) {
                  LiveShutter.this.btoast = false;
                  Trace.d(LiveShutter.this.TAG, "MESSAGE : DISPLAY_ZOOMINBUTTON");
                  LiveShutter.nDisplayFlag = 26;
                  LiveShutter.mHandler.postDelayed(new LiveShutter.XxxThread(LiveShutter.nDisplayFlag), (LiveShutter.this.nZoomGap - 2));
                  LiveShutter.this.zoomtime = l;
                  LiveShutter.this.doAction(3, "");
                  return;
                } 
              } 
              if (Integer.parseInt(LiveShutter.this.mDeviceController.getDefaultZoom()) != 0 && LiveShutter.codec_init) {
                LiveShutter.rvf_zoomin_button.setImageResource(2130838209);
              } else if (Integer.parseInt(LiveShutter.this.mDeviceController.getDefaultZoom()) == 0) {
                LiveShutter.rvf_zoomin_button.setImageResource(2130838208);
              } 
              LiveShutter.this.buttondisplay(true);
              if (LiveShutter.this.btoastflag) {
                if (LiveShutter.this.toast == null) {
                  String str = LiveShutter.this.getString(2131361958);
                  LiveShutter.this.toast = Toast.makeText(LiveShutter.Appcontext, str, 0);
                } 
                if (!LiveShutter.this.btoast) {
                  if (LiveShutter.orientation == 2) {
                    LiveShutter.this.toast.setGravity(81, 0, LiveShutter.convertDipToPx(LiveShutter.Appcontext, 43));
                  } else {
                    LiveShutter.this.toast.setGravity(81, 0, LiveShutter.convertDipToPx(LiveShutter.Appcontext, 150));
                  } 
                  LiveShutter.this.btoast = true;
                  LiveShutter.this.toast.show();
                  return;
                } 
              } 
            case 2:
              break;
          } 
          LiveShutter.this.buttondisplay(true);
          long l = System.currentTimeMillis();
          if (l - LiveShutter.this.zoomtime > LiveShutter.this.nZoomGap) {
            LiveShutter.rvf_zoomout_button.setImageResource(2130838213);
            if (Integer.parseInt(LiveShutter.this.mDeviceController.getDefaultZoom()) != 0 && LiveShutter.codec_init) {
              LiveShutter.this.btoast = false;
              Trace.d(LiveShutter.this.TAG, "MESSAGE : DISPLAY_ZOOMOUTBUTTON");
              LiveShutter.nDisplayFlag = 25;
              LiveShutter.mHandler.postDelayed(new LiveShutter.XxxThread(LiveShutter.nDisplayFlag), (LiveShutter.this.nZoomGap - 2));
              LiveShutter.this.zoomtime = l;
              LiveShutter.this.doAction(4, "");
              return;
            } 
          } 
          if (Integer.parseInt(LiveShutter.this.mDeviceController.getDefaultZoom()) != 0 && LiveShutter.codec_init) {
            LiveShutter.rvf_zoomout_button.setImageResource(2130838214);
          } else if (Integer.parseInt(LiveShutter.this.mDeviceController.getDefaultZoom()) == 0) {
            LiveShutter.rvf_zoomout_button.setImageResource(2130838213);
          } 
          LiveShutter.this.buttondisplay(true);
          if (LiveShutter.this.btoastflag) {
            if (LiveShutter.this.toast == null) {
              String str = LiveShutter.this.getString(2131361958);
              LiveShutter.this.toast = Toast.makeText(LiveShutter.Appcontext, str, 0);
            } 
            if (!LiveShutter.this.btoast) {
              if (LiveShutter.orientation == 2) {
                LiveShutter.this.toast.setGravity(81, 0, LiveShutter.convertDipToPx(LiveShutter.Appcontext, 43));
              } else {
                LiveShutter.this.toast.setGravity(81, 0, LiveShutter.convertDipToPx(LiveShutter.Appcontext, 150));
              } 
              LiveShutter.this.btoast = true;
              LiveShutter.this.toast.show();
              return;
            } 
          } 
        }
      };
    FFmpegJNI.request(FFmpegJNI.Command.SURFACE_CREATED, paramSurfaceHolder);
  }
  
  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
    Trace.d(this.TAG, "start surfaceDestroyed()");
    FFmpegJNI.request(FFmpegJNI.Command.SURFACE_DESTROYED, paramSurfaceHolder);
  }
  
  private static class ActionState {
    public static final int END = 2;
    
    public static final int NONE = 0;
    
    public static final int START = 1;
  }
  
  private static class AutoFocusFrameType {
    private static final int EXTEND = 4;
    
    private static final int MATRIX = 1;
    
    private static final int METERING_SPOT = 3;
    
    private static final int NONE = 0;
    
    private static final int TOUCH = 2;
  }
  
  private class DoubleTimerShotState {
    private static final int NONE = 0;
    
    private static final int SECOND_10 = 2;
    
    private static final int SECOND_2 = 1;
  }
  
  private class FrameCounterTask extends TimerTask {
    private FrameCounterTask() {}
    
    public void run() {
      Trace.d(LiveShutter.this.TAG, "frame count : " + FFmpegJNI.request(FFmpegJNI.Command.FRAME_COUNT_GET, 0, 0, LiveShutter.this.screenPositoin, LiveShutter.this.screenSize));
    }
  }
  
  private class ImageDownloadTask extends AsyncTask<String, Void, Boolean> {
    private ImageDownloadTask() {}
    
    protected Boolean doInBackground(String... param1VarArgs) {
      // Byte code:
      //   0: aload_0
      //   1: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
      //   4: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   7: ldc 'start ImageDownloadTask.doInBackground()'
      //   9: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   12: invokestatic access$149 : ()Ljava/lang/String;
      //   15: astore #4
      //   17: aload_0
      //   18: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
      //   21: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
      //   24: invokevirtual getVersion : ()Ljava/lang/String;
      //   27: invokestatic isSupportedDownloadBYOriginalImageURL : (Ljava/lang/String;)Z
      //   30: ifeq -> 224
      //   33: aload_1
      //   34: iconst_0
      //   35: aaload
      //   36: astore_3
      //   37: aload #4
      //   39: astore_1
      //   40: aload_0
      //   41: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
      //   44: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/controller/DeviceController;
      //   47: invokevirtual getVersion : ()Ljava/lang/String;
      //   50: invokestatic isSupportedDownloadBYOriginalImageURL : (Ljava/lang/String;)Z
      //   53: ifeq -> 249
      //   56: aload #4
      //   58: astore_1
      //   59: aload_0
      //   60: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
      //   63: invokestatic access$46 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/util/ImageDownloader;
      //   66: aload_3
      //   67: invokevirtual syncDownloadImage : (Ljava/lang/String;)Ljava/lang/String;
      //   70: astore_3
      //   71: aload_3
      //   72: astore_1
      //   73: aload_3
      //   74: invokestatic getThumbnail : (Ljava/lang/String;)Landroid/graphics/Bitmap;
      //   77: invokestatic access$150 : (Landroid/graphics/Bitmap;)V
      //   80: aload_3
      //   81: astore_1
      //   82: aload_0
      //   83: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
      //   86: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   89: new java/lang/StringBuilder
      //   92: dup
      //   93: ldc 'scan file path 00 : '
      //   95: invokespecial <init> : (Ljava/lang/String;)V
      //   98: aload_3
      //   99: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   102: invokevirtual toString : ()Ljava/lang/String;
      //   105: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   108: aload_3
      //   109: astore_1
      //   110: aload_3
      //   111: invokestatic getMimeType : (Ljava/lang/String;)Ljava/lang/String;
      //   114: astore #4
      //   116: aload_3
      //   117: astore_1
      //   118: invokestatic access$151 : ()Landroid/content/Context;
      //   121: iconst_1
      //   122: anewarray java/lang/String
      //   125: dup
      //   126: iconst_0
      //   127: aload_3
      //   128: aastore
      //   129: iconst_1
      //   130: anewarray java/lang/String
      //   133: dup
      //   134: iconst_0
      //   135: aload #4
      //   137: aastore
      //   138: aconst_null
      //   139: invokestatic scanFile : (Landroid/content/Context;[Ljava/lang/String;[Ljava/lang/String;Landroid/media/MediaScannerConnection$OnScanCompletedListener;)V
      //   142: aload_3
      //   143: astore_1
      //   144: aload_0
      //   145: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
      //   148: aload_3
      //   149: invokevirtual CheckDirtyCache : (Ljava/lang/String;)V
      //   152: aload_3
      //   153: astore_1
      //   154: aload_0
      //   155: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
      //   158: invokestatic access$157 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/location/Location;
      //   161: ifnonnull -> 179
      //   164: aload_3
      //   165: astore #4
      //   167: aload_3
      //   168: astore_1
      //   169: aload_0
      //   170: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
      //   173: invokestatic access$158 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/location/Location;
      //   176: ifnull -> 207
      //   179: aload_3
      //   180: astore_1
      //   181: aload_0
      //   182: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
      //   185: invokestatic access$157 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/location/Location;
      //   188: ifnull -> 635
      //   191: aload_3
      //   192: astore_1
      //   193: aload_0
      //   194: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
      //   197: invokestatic access$157 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/location/Location;
      //   200: aload_3
      //   201: invokestatic setGPSInfoOfExif : (Landroid/location/Location;Ljava/lang/String;)V
      //   204: aload_3
      //   205: astore #4
      //   207: aload_0
      //   208: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
      //   211: invokestatic access$131 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/configuration/manager/SRVFConfigurationManager;
      //   214: aload #4
      //   216: invokevirtual setLastSaveFileName : (Ljava/lang/String;)V
      //   219: iconst_1
      //   220: invokestatic valueOf : (Z)Ljava/lang/Boolean;
      //   223: areturn
      //   224: new java/lang/StringBuilder
      //   227: dup
      //   228: aload_1
      //   229: iconst_0
      //   230: aaload
      //   231: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
      //   234: invokespecial <init> : (Ljava/lang/String;)V
      //   237: ldc '_SM'
      //   239: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   242: invokevirtual toString : ()Ljava/lang/String;
      //   245: astore_3
      //   246: goto -> 37
      //   249: aload #4
      //   251: astore_1
      //   252: new java/net/URL
      //   255: dup
      //   256: aload_3
      //   257: invokespecial <init> : (Ljava/lang/String;)V
      //   260: astore #5
      //   262: aload #4
      //   264: astore_1
      //   265: new org/cybergarage/http/HTTPRequest
      //   268: dup
      //   269: invokespecial <init> : ()V
      //   272: astore #6
      //   274: aload #4
      //   276: astore_1
      //   277: aload #6
      //   279: ldc 'GET'
      //   281: invokevirtual setMethod : (Ljava/lang/String;)V
      //   284: aload #4
      //   286: astore_1
      //   287: aload #6
      //   289: aload_3
      //   290: invokevirtual setURI : (Ljava/lang/String;)V
      //   293: aload #4
      //   295: astore_1
      //   296: aload #6
      //   298: aload #5
      //   300: invokevirtual getHost : ()Ljava/lang/String;
      //   303: aload #5
      //   305: invokevirtual getPort : ()I
      //   308: invokevirtual post : (Ljava/lang/String;I)Lorg/cybergarage/http/HTTPResponse;
      //   311: astore_3
      //   312: aload #4
      //   314: astore_1
      //   315: aload_0
      //   316: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
      //   319: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   322: new java/lang/StringBuilder
      //   325: dup
      //   326: ldc '[Thumbnail] Contents Length : '
      //   328: invokespecial <init> : (Ljava/lang/String;)V
      //   331: aload_3
      //   332: invokevirtual getContentLength : ()J
      //   335: invokevirtual append : (J)Ljava/lang/StringBuilder;
      //   338: ldc ' bytes'
      //   340: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   343: invokevirtual toString : ()Ljava/lang/String;
      //   346: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   349: aload #4
      //   351: astore_1
      //   352: aload_3
      //   353: invokevirtual getContent : ()[B
      //   356: iconst_0
      //   357: aload_3
      //   358: invokevirtual getContentLength : ()J
      //   361: l2i
      //   362: invokestatic decodeByteArray : ([BII)Landroid/graphics/Bitmap;
      //   365: invokestatic access$152 : (Landroid/graphics/Bitmap;)V
      //   368: aload #4
      //   370: astore_1
      //   371: invokestatic access$153 : ()C
      //   374: bipush #68
      //   376: if_icmpeq -> 404
      //   379: aload #4
      //   381: astore_1
      //   382: invokestatic access$153 : ()C
      //   385: bipush #76
      //   387: if_icmpne -> 578
      //   390: aload #4
      //   392: astore_1
      //   393: invokestatic access$154 : ()Landroid/graphics/Bitmap;
      //   396: bipush #-90
      //   398: invokestatic access$155 : (Landroid/graphics/Bitmap;I)Landroid/graphics/Bitmap;
      //   401: invokestatic access$152 : (Landroid/graphics/Bitmap;)V
      //   404: aload #4
      //   406: astore_1
      //   407: invokestatic access$154 : ()Landroid/graphics/Bitmap;
      //   410: ifnull -> 458
      //   413: aload #4
      //   415: astore_1
      //   416: invokestatic access$154 : ()Landroid/graphics/Bitmap;
      //   419: invokevirtual getWidth : ()I
      //   422: i2f
      //   423: invokestatic access$154 : ()Landroid/graphics/Bitmap;
      //   426: invokevirtual getHeight : ()I
      //   429: i2f
      //   430: fdiv
      //   431: fstore_2
      //   432: fload_2
      //   433: fconst_0
      //   434: fcmpl
      //   435: ifle -> 458
      //   438: aload #4
      //   440: astore_1
      //   441: invokestatic access$154 : ()Landroid/graphics/Bitmap;
      //   444: ldc 50.0
      //   446: fload_2
      //   447: fmul
      //   448: f2i
      //   449: bipush #50
      //   451: iconst_1
      //   452: invokestatic createScaledBitmap : (Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;
      //   455: invokestatic access$150 : (Landroid/graphics/Bitmap;)V
      //   458: aload #4
      //   460: astore_3
      //   461: aload #4
      //   463: astore_1
      //   464: invokestatic isMemoryFull : ()Z
      //   467: ifne -> 152
      //   470: aload #4
      //   472: astore_1
      //   473: aload_0
      //   474: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
      //   477: aload #4
      //   479: invokestatic access$156 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;Ljava/lang/String;)Z
      //   482: pop
      //   483: aload #4
      //   485: astore_1
      //   486: aload_0
      //   487: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
      //   490: invokestatic access$8 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   493: new java/lang/StringBuilder
      //   496: dup
      //   497: ldc 'scan file path 02 : '
      //   499: invokespecial <init> : (Ljava/lang/String;)V
      //   502: aload #4
      //   504: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   507: invokevirtual toString : ()Ljava/lang/String;
      //   510: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   513: aload #4
      //   515: astore_1
      //   516: aload #4
      //   518: invokestatic getMimeType : (Ljava/lang/String;)Ljava/lang/String;
      //   521: astore_3
      //   522: aload #4
      //   524: astore_1
      //   525: invokestatic access$151 : ()Landroid/content/Context;
      //   528: iconst_1
      //   529: anewarray java/lang/String
      //   532: dup
      //   533: iconst_0
      //   534: aload #4
      //   536: aastore
      //   537: iconst_1
      //   538: anewarray java/lang/String
      //   541: dup
      //   542: iconst_0
      //   543: aload_3
      //   544: aastore
      //   545: aconst_null
      //   546: invokestatic scanFile : (Landroid/content/Context;[Ljava/lang/String;[Ljava/lang/String;Landroid/media/MediaScannerConnection$OnScanCompletedListener;)V
      //   549: aload #4
      //   551: astore_1
      //   552: aload_0
      //   553: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
      //   556: aload #4
      //   558: invokevirtual CheckDirtyCache : (Ljava/lang/String;)V
      //   561: aload #4
      //   563: astore_3
      //   564: goto -> 152
      //   567: astore_3
      //   568: aload_3
      //   569: invokevirtual printStackTrace : ()V
      //   572: aload_1
      //   573: astore #4
      //   575: goto -> 207
      //   578: aload #4
      //   580: astore_1
      //   581: invokestatic access$153 : ()C
      //   584: bipush #82
      //   586: if_icmpne -> 606
      //   589: aload #4
      //   591: astore_1
      //   592: invokestatic access$154 : ()Landroid/graphics/Bitmap;
      //   595: bipush #90
      //   597: invokestatic access$155 : (Landroid/graphics/Bitmap;I)Landroid/graphics/Bitmap;
      //   600: invokestatic access$152 : (Landroid/graphics/Bitmap;)V
      //   603: goto -> 404
      //   606: aload #4
      //   608: astore_1
      //   609: invokestatic access$153 : ()C
      //   612: bipush #85
      //   614: if_icmpne -> 404
      //   617: aload #4
      //   619: astore_1
      //   620: invokestatic access$154 : ()Landroid/graphics/Bitmap;
      //   623: sipush #180
      //   626: invokestatic access$155 : (Landroid/graphics/Bitmap;I)Landroid/graphics/Bitmap;
      //   629: invokestatic access$152 : (Landroid/graphics/Bitmap;)V
      //   632: goto -> 404
      //   635: aload_3
      //   636: astore_1
      //   637: aload_0
      //   638: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;
      //   641: invokestatic access$158 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/rvf/LiveShutter;)Landroid/location/Location;
      //   644: aload_3
      //   645: invokestatic setGPSInfoOfExif : (Landroid/location/Location;Ljava/lang/String;)V
      //   648: aload_3
      //   649: astore #4
      //   651: goto -> 207
      // Exception table:
      //   from	to	target	type
      //   40	56	567	java/lang/Exception
      //   59	71	567	java/lang/Exception
      //   73	80	567	java/lang/Exception
      //   82	108	567	java/lang/Exception
      //   110	116	567	java/lang/Exception
      //   118	142	567	java/lang/Exception
      //   144	152	567	java/lang/Exception
      //   154	164	567	java/lang/Exception
      //   169	179	567	java/lang/Exception
      //   181	191	567	java/lang/Exception
      //   193	204	567	java/lang/Exception
      //   252	262	567	java/lang/Exception
      //   265	274	567	java/lang/Exception
      //   277	284	567	java/lang/Exception
      //   287	293	567	java/lang/Exception
      //   296	312	567	java/lang/Exception
      //   315	349	567	java/lang/Exception
      //   352	368	567	java/lang/Exception
      //   371	379	567	java/lang/Exception
      //   382	390	567	java/lang/Exception
      //   393	404	567	java/lang/Exception
      //   407	413	567	java/lang/Exception
      //   416	432	567	java/lang/Exception
      //   441	458	567	java/lang/Exception
      //   464	470	567	java/lang/Exception
      //   473	483	567	java/lang/Exception
      //   486	513	567	java/lang/Exception
      //   516	522	567	java/lang/Exception
      //   525	549	567	java/lang/Exception
      //   552	561	567	java/lang/Exception
      //   581	589	567	java/lang/Exception
      //   592	603	567	java/lang/Exception
      //   609	617	567	java/lang/Exception
      //   620	632	567	java/lang/Exception
      //   637	648	567	java/lang/Exception
    }
    
    protected void onPostExecute(Boolean param1Boolean) {
      Trace.d(LiveShutter.this.TAG, "start ImageDownloadTask.onPostExecute()");
      if (param1Boolean.booleanValue())
        LiveShutter.this.updateImageOfGallaryButton(); 
      if (!LiveShutter.this.mDeviceController.getDriveValue().toUpperCase(Locale.ENGLISH).equals("CONTINUOUS")) {
        LiveShutter.nDisplayFlag = 48;
        LiveShutter.mHandler.post(new LiveShutter.XxxThread(LiveShutter.nDisplayFlag));
      } 
      if (LiveShutter.this.nCameraSaveSelect == 0 && CommonUtils.isMemoryFull()) {
        LiveShutter.this.mExToast.show(7);
        LiveShutter.this.setShutterButtonEnabled(false);
      } 
      LiveShutter.this.mShotState = 0;
      super.onPostExecute(param1Boolean);
    }
    
    protected void onPreExecute() {
      LiveShutter.nDisplayFlag = 20;
      LiveShutter.mHandler.post(new LiveShutter.XxxThread(LiveShutter.nDisplayFlag));
      super.onPreExecute();
    }
  }
  
  private class InitCodecTask extends AsyncTask<Void, Void, Boolean> {
    private InitCodecTask() {}
    
    protected Boolean doInBackground(Void... param1VarArgs) {
      LiveShutter.this.codec_start();
      return null;
    }
    
    protected void onPostExecute(Boolean param1Boolean) {
      if (LiveShutter.this.mDeviceController.getAFAreaMenuItems().size() > 0 && LiveShutter.this.mDeviceController.getAFAreaValue().toUpperCase(Locale.ENGLISH).equals("SELECTION AF")) {
        LiveShutter.rvf_touchAutoFocus.setImageResource(2130837575);
        LiveShutter.rvf_touchAutoFocus.setVisibility(0);
      } else if (LiveShutter.this.mDeviceController.getAFAreaValue().toUpperCase(Locale.ENGLISH).equals("MULTI AF")) {
        LiveShutter.rvf_touchAutoFocus.setVisibility(4);
      } 
      LiveShutter.nDisplayFlag = 144;
      LiveShutter.mHandler.postDelayed(new LiveShutter.XxxThread(LiveShutter.nDisplayFlag), 300L);
      if (LiveShutter.this.mShotStateEx == 7)
        LiveShutter.this.stopRecord(); 
      if (LiveShutter.this.getPhoneCamType() != 1 && LiveShutter.rvf_QuickSettingMenu.getVisibility() == 0)
        LiveShutter.this.hideQuickSettingMenu(); 
      LiveShutter.this.setVisibilityOSD(0);
      LiveShutter.this.resetVisibilityTouchAFFrame();
      LiveShutter.this.shutterbutton(true);
      if (LiveShutter.this.streampro != null && LiveShutter.this.streampro.isShowing()) {
        LiveShutter.this.streampro.dismiss();
        LiveShutter.this.removeDialog(1010);
        LiveShutter.this.removeDialog(1011);
      } 
      super.onPostExecute(param1Boolean);
    }
    
    protected void onPreExecute() {
      Trace.d(LiveShutter.this.TAG, "start InitCodecTask.onPreExecute()");
      if (LiveShutter.this.streampro != null) {
        String str = LiveShutter.this.getString(2131361948);
        LiveShutter.this.streampro.setMessage(str);
      } 
      super.onPreExecute();
    }
  }
  
  private class MainOptionMenuListAdapter extends ArrayAdapter<Menu> {
    public MainOptionMenuListAdapter(Context param1Context, int param1Int, List<Menu> param1List) {
      super(param1Context, param1Int, param1List);
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      String str;
      View view = param1View;
      if (param1View == null)
        view = ((LayoutInflater)LiveShutter.this.getSystemService("layout_inflater")).inflate(2130903099, null); 
      Menu menu = (Menu)getItem(param1Int);
      ImageView imageView1 = (ImageView)view.findViewById(2131558669);
      TextView textView = (TextView)view.findViewById(2131558670);
      ImageView imageView2 = (ImageView)view.findViewById(2131558671);
      textView.setText(menu.getName());
      switch (LiveShutter.this.getCurrentMainOptionMenuId()) {
        default:
          return view;
        case 1:
          str = ((String)LiveShutter.this.mDeviceController.getFlashMenuItems().get(param1Int)).toUpperCase(Locale.ENGLISH);
          if (str.equals("OFF")) {
            imageView1.setImageResource(2130837974);
          } else if (str.equals("AUTO")) {
            imageView1.setImageResource(2130837973);
          } else if (str.equals("REDEYE")) {
            imageView1.setImageResource(2130837976);
          } else if (str.equals("FILLIN") || str.equals("FILL-IN") || str.equals("FILL IN")) {
            imageView1.setImageResource(2130837972);
          } else if (str.equals("SLOWSYNC")) {
            imageView1.setImageResource(2130837978);
          } else if (str.equals("REDEYEFIX")) {
            imageView1.setImageResource(2130837977);
          } else if (str.equals("FILLINREDEYE") || str.equals("FILL-IN_RED") || str.equals("FILL-IN RED")) {
            imageView1.setImageResource(2130837975);
          } else if (str.equals("1STCURTAIN") || str.equals("1ST_CURTAIN") || str.equals("1ST CURTAIN")) {
            imageView1.setImageResource(2130837970);
          } else if (str.equals("2NDCURTAIN") || str.equals("2ND_CURTAIN") || str.equals("2ND CURTAIN")) {
            imageView1.setImageResource(2130837971);
          } else {
            imageView1.setImageResource(2130837974);
          } 
          if (LiveShutter.this.mDeviceController.getDefaultFlashIndex() == param1Int) {
            imageView2.setImageResource(2130837948);
            imageView2.setVisibility(0);
            return view;
          } 
          imageView2.setImageResource(2130837947);
          imageView2.setVisibility(0);
          return view;
        case 2:
          if (str.getName().equals(LiveShutter.this.getString(2131361997))) {
            imageView1.setImageResource(2130838043);
            if (LiveShutter.this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("OFF")) {
              imageView2.setImageResource(2130837948);
              imageView2.setVisibility(0);
              return view;
            } 
            imageView2.setImageResource(2130837947);
            imageView2.setVisibility(0);
            return view;
          } 
          if (str.getName().equals(LiveShutter.this.getString(2131361993))) {
            imageView1.setImageResource(2130838039);
            if (LiveShutter.this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("2SEC")) {
              imageView2.setImageResource(2130837948);
              imageView2.setVisibility(0);
              return view;
            } 
            imageView2.setImageResource(2130837947);
            imageView2.setVisibility(0);
            return view;
          } 
          if (str.getName().equals(LiveShutter.this.getString(2131361994))) {
            imageView1.setImageResource(2130838041);
            if (LiveShutter.this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("5SEC")) {
              imageView2.setImageResource(2130837948);
              imageView2.setVisibility(0);
              return view;
            } 
            imageView2.setImageResource(2130837947);
            imageView2.setVisibility(0);
            return view;
          } 
          if (str.getName().equals(LiveShutter.this.getString(2131361996))) {
            imageView1.setImageResource(2130838038);
            if (LiveShutter.this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("10SEC")) {
              imageView2.setImageResource(2130837948);
              imageView2.setVisibility(0);
              return view;
            } 
            imageView2.setImageResource(2130837947);
            imageView2.setVisibility(0);
            return view;
          } 
          if (str.getName().equals(LiveShutter.this.getString(2131361995))) {
            imageView1.setImageResource(2130838042);
            if (LiveShutter.this.mDeviceController.getCurrentLEDTimeMenuItem().toUpperCase(Locale.ENGLISH).equals("DOUBLE")) {
              imageView2.setImageResource(2130837948);
              imageView2.setVisibility(0);
              return view;
            } 
            imageView2.setImageResource(2130837947);
          } 
          imageView2.setVisibility(0);
          return view;
        case 3:
          if (LiveShutter.this.mDeviceController.getResolutionMenuItems().size() > 0) {
            DSCResolution dSCResolution = LiveShutter.this.mDeviceController.getResolutionMenuItems().get(param1Int);
            if (dSCResolution != null) {
              String str1 = Utils.unitChange(LiveShutter.this, dSCResolution.getWidth(), dSCResolution.getHeight(), FunctionManager.getPhotoSizePresentationType(LiveShutter.this.connectedSsid));
              if (str1.equals("1M")) {
                imageView1.setImageResource(2130838018);
              } else if (str1.equals("1.1M")) {
                imageView1.setImageResource(2130838017);
              } else if (str1.equals("2M")) {
                imageView1.setImageResource(2130838021);
              } else if (str1.equals("2.1M")) {
                imageView1.setImageResource(2130838020);
              } else if (str1.equals("W2M")) {
                imageView1.setImageResource(2130838022);
              } else if (str1.equals("3M")) {
                imageView1.setImageResource(2130838023);
              } else if (str1.equals("4M")) {
                imageView1.setImageResource(2130838026);
              } else if (str1.equals("4.9M")) {
                imageView1.setImageResource(2130838025);
              } else if (str1.equals("W4M")) {
                imageView1.setImageResource(2130838027);
              } else if (str1.equals("5M")) {
                imageView1.setImageResource(2130838029);
              } else if (str1.equals("5.9M")) {
                imageView1.setImageResource(2130838028);
              } else if (str1.equals("7M")) {
                imageView1.setImageResource(2130838031);
              } else if (str1.equals("7.8M")) {
                imageView1.setImageResource(2130838030);
              } else if (str1.equals("W7M")) {
                imageView1.setImageResource(2130838032);
              } else if (str1.equals("8M")) {
                imageView1.setImageResource(2130838033);
              } else if (str1.equals("9M")) {
                imageView1.setImageResource(2130838034);
              } else if (str1.equals("10M")) {
                imageView1.setImageResource(2130838008);
              } else if (str1.equals("10.1M")) {
                imageView1.setImageResource(2130838007);
              } else if (str1.equals("W10M")) {
                imageView1.setImageResource(2130838009);
              } else if (str1.equals("W12M")) {
                imageView1.setImageResource(2130838010);
              } else if (str1.equals("13M")) {
                imageView1.setImageResource(2130838012);
              } else if (str1.equals("13.3M")) {
                imageView1.setImageResource(2130838011);
              } else if (str1.equals("P14M")) {
                imageView1.setImageResource(2130838013);
              } else if (str1.equals("16M")) {
                imageView1.setImageResource(2130838015);
              } else if (str1.equals("16.9M")) {
                imageView1.setImageResource(2130838014);
              } else if (str1.equals("W16M")) {
                imageView1.setImageResource(2130838016);
              } else if (str1.equals("20M")) {
                imageView1.setImageResource(2130838019);
              } else {
                imageView1.setImageResource(2130838008);
              } 
            } 
          } 
          if (Integer.parseInt(LiveShutter.this.mDeviceController.getDefaultResolutionIndex()) == param1Int) {
            imageView2.setImageResource(2130837948);
            imageView2.setVisibility(0);
            return view;
          } 
          imageView2.setImageResource(2130837947);
          imageView2.setVisibility(0);
          return view;
        case 4:
          if (param1Int == 0) {
            imageView1.setImageResource(2130838036);
            LiveShutter.this.REQUESTIMAGE = 1;
          } else if (param1Int == 1) {
            imageView1.setImageResource(2130838035);
            LiveShutter.this.REQUESTIMAGE = 0;
          } 
          if (LiveShutter.this.nCameraSaveSelect == param1Int) {
            imageView2.setImageResource(2130837948);
            imageView2.setVisibility(0);
            return view;
          } 
          imageView2.setImageResource(2130837947);
          imageView2.setVisibility(0);
          return view;
        case 5:
          break;
      } 
      if (param1Int == 0) {
        if (LiveShutter.this.nCameraSaveSelect == 0) {
          imageView1.setImageResource(2130838036);
          imageView2.setVisibility(4);
          return view;
        } 
        if (LiveShutter.this.nCameraSaveSelect == 1)
          imageView1.setImageResource(2130838035); 
        imageView2.setVisibility(4);
        return view;
      } 
      if (param1Int == 1) {
        if (LiveShutter.this.mDeviceController.getCurrentStreamQuality().equals("low")) {
          imageView1.setImageResource(2130837988);
          imageView2.setVisibility(4);
          return view;
        } 
        imageView1.setImageResource(2130837991);
      } 
      imageView2.setVisibility(4);
      return view;
    }
  }
  
  private class RestartCodecTask extends AsyncTask<Integer, Void, Boolean> {
    private RestartCodecTask() {}
    
    protected Boolean doInBackground(Integer... param1VarArgs) {
      LiveShutter.this.codec_stop();
      if (param1VarArgs.length != 0) {
        Trace.d(LiveShutter.this.TAG, "params[0] is not null");
        LiveShutter.this.screenConfigChange(param1VarArgs[0].intValue());
        LiveShutter.this.codec_start();
        return null;
      } 
      Trace.d(LiveShutter.this.TAG, "params[0] is null");
      if (LiveShutter.this.mDeviceController.getRatioOffsetMenuItems().size() > 0) {
        LiveShutter.this.screenConfigChange(2);
        LiveShutter.this.codec_start();
        return null;
      } 
      LiveShutter.this.screenConfigChange(Utils.stringToRatioType(LiveShutter.this.mDeviceController.getRatioValue()));
      LiveShutter.this.codec_start();
      return null;
    }
    
    protected void onPostExecute(Boolean param1Boolean) {
      if (FunctionManager.isNotSupportedRawQualityAndContinuousShotAtTheSameTime(LiveShutter.this.connectedSsid) && LiveShutter.this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).contains("RAW") && LiveShutter.this.mDeviceController.getDriveValue().toUpperCase(Locale.ENGLISH).contains("CONTINUOUS"))
        LiveShutter.this.showDialog(2007); 
      LiveShutter.this.dismissDialog(1016);
      LiveShutter.nDisplayFlag = 144;
      LiveShutter.mHandler.postDelayed(new LiveShutter.XxxThread(LiveShutter.nDisplayFlag), 300L);
      if (LiveShutter.this.mShotStateEx == 7)
        LiveShutter.this.stopRecord(); 
      LiveShutter.this.resetVisibilityTouchAFFrame();
      super.onPostExecute(param1Boolean);
    }
    
    protected void onPreExecute() {
      LiveShutter.this.showDialog(1016);
      super.onPreExecute();
    }
  }
  
  private class ShotPrepareTask extends AsyncTask<String, Void, Boolean> {
    private ShotPrepareTask() {}
    
    protected Boolean doInBackground(String... param1VarArgs) {
      Trace.d(LiveShutter.this.TAG, "start ShotPrepareTask.doInBackground()");
      return null;
    }
    
    protected void onPostExecute(Boolean param1Boolean) {
      Trace.d(LiveShutter.this.TAG, "start ShotPrepareTask.onPostExecute()");
      LiveShutter.nDisplayFlag = 48;
      LiveShutter.mHandler.post(new LiveShutter.XxxThread(LiveShutter.nDisplayFlag));
      super.onPostExecute(param1Boolean);
    }
  }
  
  private class ShotState {
    private static final int AF = 1;
    
    private static final int MOVIE = 5;
    
    private static final int NONE = 0;
    
    private static final int SAVE = 4;
    
    private static final int SHOT = 3;
    
    private static final int TIMER = 2;
  }
  
  private class ShotStateEx {
    private static final int AUTO_FOCUSING = 2;
    
    private static final int MOVIE_RECORDING = 7;
    
    private static final int NONE = 0;
    
    private static final int START_SHOT = 5;
    
    private static final int STOP_SHOT = 6;
  }
  
  private class StartRecordTask extends AsyncTask<Void, Void, Boolean> {
    private StartRecordTask() {}
    
    protected Boolean doInBackground(Void... param1VarArgs) {
      LiveShutter.this.codec_stop();
      if (LiveShutter.this.mDeviceController.getStreamUrlMenuItems().size() > 2)
        if (LiveShutter.this.mDeviceController.getRatioOffsetMenuItems().size() > 0) {
          LiveShutter.this.screenConfigChange(2);
        } else {
          int i = 0;
          while (true) {
            if (i < LiveShutter.this.mDeviceController.getMovieResolutionMenuItems().size())
              if (((DSCMovieResolution)LiveShutter.this.mDeviceController.getMovieResolutionMenuItems().get(i)).getResolution().equals(LiveShutter.this.mDeviceController.getMovieResolutionValue())) {
                LiveShutter.this.screenConfigChange(Utils.stringToRatioType(((DSCMovieResolution)LiveShutter.this.mDeviceController.getMovieResolutionMenuItems().get(i)).getRatio()));
              } else {
                i++;
                continue;
              }  
            if (LiveShutter.this.codec_start()) {
              Trace.d(LiveShutter.this.TAG, " codec_start is True : START_RECORD_ACTION_ID");
              LiveShutter.this.doAction(35, "");
            } 
            return null;
          } 
        }  
      if (LiveShutter.this.codec_start()) {
        Trace.d(LiveShutter.this.TAG, " codec_start is True : START_RECORD_ACTION_ID");
        LiveShutter.this.doAction(35, "");
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
  
  private class SubOptionMenuListAdapter extends ArrayAdapter<Menu> {
    public SubOptionMenuListAdapter(Context param1Context, int param1Int, List<Menu> param1List) {
      super(param1Context, param1Int, param1List);
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      View view = param1View;
      if (param1View == null)
        view = ((LayoutInflater)LiveShutter.this.getSystemService("layout_inflater")).inflate(2130903099, null); 
      Menu menu = (Menu)getItem(param1Int);
      ImageView imageView1 = (ImageView)view.findViewById(2131558669);
      TextView textView = (TextView)view.findViewById(2131558670);
      ImageView imageView2 = (ImageView)view.findViewById(2131558671);
      textView.setText(menu.getName());
      switch (LiveShutter.this.getCurrentSubOptionMenuId()) {
        default:
          return view;
        case 4:
          if (param1Int == 0) {
            imageView1.setImageResource(2130838036);
            LiveShutter.this.REQUESTIMAGE = 1;
          } else if (param1Int == 1) {
            imageView1.setImageResource(2130838035);
            LiveShutter.this.REQUESTIMAGE = 0;
          } 
          if (LiveShutter.this.nCameraSaveSelect == param1Int) {
            imageView2.setBackgroundResource(2130837948);
            imageView2.setVisibility(0);
            return view;
          } 
          imageView2.setBackgroundResource(2130837947);
          imageView2.setVisibility(0);
          return view;
        case 6:
          break;
      } 
      if (((String)LiveShutter.this.mDeviceController.getStreamQualityMenuItems().get(param1Int)).equals("high")) {
        imageView1.setImageResource(2130837991);
      } else if (((String)LiveShutter.this.mDeviceController.getStreamQualityMenuItems().get(param1Int)).equals("low")) {
        imageView1.setImageResource(2130837988);
      } 
      if (((String)LiveShutter.this.mDeviceController.getStreamQualityMenuItems().get(param1Int)).equals(LiveShutter.this.mDeviceController.getCurrentStreamQuality())) {
        imageView2.setBackgroundResource(2130837948);
        imageView2.setVisibility(0);
        return view;
      } 
      imageView2.setBackgroundResource(2130837947);
      imageView2.setVisibility(0);
      return view;
    }
  }
  
  private static class TimerMessageID {
    private static final int ACTION_WAIT_TIME = 5;
    
    private static final int ACTION_WAIT_TIME_LONG = 6;
    
    private static final int APP_CLOSE = 8;
    
    private static final int DISPLAY_APP_CLOSE_MESSAGE = 7;
    
    private static final int HIDE_AUTO_FOCUS_FRAME = 10;
    
    private static final int HIDE_CUSTOM_DIALOG = 9;
    
    private static final int HIDE_TOAST_MESSAGE = 12;
    
    private static final int OPTION_MENU_CLOSE = 2;
    
    private static final int OSD_CLOSE = 1;
    
    private static final int SUB_MENU_CLOSE = 3;
    
    private static final int TOUCH_AUTO_FOCUS_FRAME_CLOSE = 4;
    
    private static final int UPDATE_LED_TIME_DISPLAY = 11;
  }
  
  private class TimerTaskRun extends TimerTask {
    private TimerTaskRun() {}
    
    public void run() {
      if (!LiveShutter.this.bConnect && FFmpegJNI.request(FFmpegJNI.Command.FRAME_COUNT_GET, 0, 0, LiveShutter.this.screenPositoin, LiveShutter.this.screenSize) > 0) {
        Trace.d(LiveShutter.this.TAG, "MESSAGE : DISPLAY_MSGCONNECTED");
        LiveShutter.this.dismissDialog(1016);
        LiveShutter.this.bConnect = true;
        if (LiveShutter.rvf_touchAutoFocus.getVisibility() == 0 && LiveShutter.this.mDeviceController.getDefaultFocusState().equals("af")) {
          if (LiveShutter.this.getActionState() != 1) {
            LiveShutter.this.mHandleTimer.removeMessages(4);
            LiveShutter.this.mShotState = 1;
            LiveShutter.this.doAction(12, String.valueOf(String.valueOf(LiveShutter.this.positionX)) + "x" + String.valueOf(LiveShutter.this.positionY));
          } 
        } else {
          LiveShutter.this.mShotStateEx = 2;
          if (RVFFunctionManager.getAFMode(LiveShutter.this.connectedSsid) == 0) {
            LiveShutter.this.mShotState = 1;
            LiveShutter.this.doAction(9, "");
          } else if (RVFFunctionManager.getAFMode(LiveShutter.this.connectedSsid) == 1) {
            LiveShutter.this.mShotState = 1;
            LiveShutter.this.doAction(10, "");
          } 
        } 
      } 
      if (!LiveShutter.this.bConnect) {
        LiveShutter liveShutter = LiveShutter.this;
        liveShutter.nConnectCount = liveShutter.nConnectCount + 1;
      } else {
        LiveShutter.this.nConnectCount = 0;
      } 
      if (LiveShutter.this.nConnectCount == 180 && !LiveShutter.this.bConnect) {
        Trace.e(LiveShutter.this.TAG, "nConnectCount == CONNECT_TIME && !bConnect");
        if (LiveShutter.this.timetask != null)
          LiveShutter.this.timetask.cancel(); 
        LiveShutter.this.closedata();
        Trace.d(LiveShutter.this.TAG, "MESSAGE : DISPLAY_CLOSEMSG");
        LiveShutter.nDisplayFlag = 7;
        LiveShutter.mHandler.post(new LiveShutter.XxxThread(LiveShutter.nDisplayFlag));
      } 
    }
  }
  
  private class UnsubscribeTask extends AsyncTask<Device, Void, Void> {
    private UnsubscribeTask() {}
    
    protected Void doInBackground(Device... param1VarArgs) {
      Trace.d(LiveShutter.this.TAG, "start UnsubscribeTask.doInBackground()");
      if (LiveShutter.this.upnpController != null)
        LiveShutter.this.upnpController.unsubscribe(param1VarArgs[0]); 
      return null;
    }
    
    protected void onPostExecute(Void param1Void) {
      Trace.d(LiveShutter.this.TAG, "start UnsubscribeTask.onPostExecute()");
      LiveShutter.this.appClose();
      if (LiveShutter.this.bCloseByFinishSafe)
        CMService.getInstance().finishSafe(); 
      super.onPostExecute(param1Void);
    }
  }
  
  private class XxxThread extends Thread {
    public int nnDisplayFlag;
    
    public XxxThread(int param1Int) {
      this.nnDisplayFlag = param1Int;
    }
    
    public void run() {
      Trace.e(LiveShutter.this.TAG, "-=> nnDisplayFlag=" + String.valueOf(this.nnDisplayFlag));
      long l2 = System.currentTimeMillis();
      long l1 = 0L;
      boolean bool = false;
      while (true) {
        String str;
        LiveShutter liveShutter;
        Bundle bundle;
        boolean bool1 = bool;
        switch (this.nnDisplayFlag) {
          default:
            Trace.d(LiveShutter.this.TAG, "\tUNKNOWN MESSAGE (DROPPED?) = " + this.nnDisplayFlag);
            bool1 = true;
          case 9:
            try {
              Thread.sleep(10L);
            } catch (Throwable throwable) {}
            Thread.yield();
            l1 = System.currentTimeMillis() - l2;
            if (l1 > 1000L) {
              Trace.d(LiveShutter.this.TAG, "\tTIMEOUT (HANDLING MESSAGE)");
              return;
            } 
            break;
          case 111:
            if (LiveShutter.this.streampro != null && LiveShutter.this.streampro.isShowing()) {
              LiveShutter.this.streampro.dismiss();
              LiveShutter.this.removeDialog(1010);
            } 
            LiveShutter.this.shutterbutton(true);
            bool1 = bool;
          case 22:
            LiveShutter.this.showDialog(1010);
            Trace.d(LiveShutter.this.TAG, "-=> timer clear");
            Trace.d(LiveShutter.this.TAG, "MESSAGE : DISPLAY_SHUTTING");
            this.nnDisplayFlag = 24;
            LiveShutter.mHandler.post(new XxxThread(this.nnDisplayFlag));
            bool1 = bool;
          case 5:
            LiveShutter.this.showDialog(1002);
            bool1 = bool;
          case 10:
            bool1 = bool;
            if (!LiveShutter.this.bpopupflag) {
              if (LiveShutter.this.timetask != null)
                LiveShutter.this.timetask.cancel(); 
              LiveShutter.this.bpopupflag = true;
              LiveShutter.this.closedata();
              LiveShutter.this.showDialog(2006);
              this.nnDisplayFlag = 44;
              LiveShutter.mHandler.postDelayed(new XxxThread(this.nnDisplayFlag), 500L);
              bool1 = bool;
            } 
          case 144:
            LiveShutter.this.rvf_surface.setEnabled(true);
            bool1 = bool;
          case 19:
            bool1 = bool;
            if (!LiveShutter.this.bpopupflag) {
              LiveShutter.this.bpopupflag = true;
              this.nnDisplayFlag = 44;
              LiveShutter.this.showDialog(1007);
              LiveShutter.mHandler.postDelayed(new XxxThread(this.nnDisplayFlag), 500L);
              bool1 = bool;
            } 
          case 11:
            bool1 = bool;
            if (!LiveShutter.this.bpopupflag) {
              LiveShutter.this.bpopupflag = true;
              LiveShutter.this.mExToast.show(6);
              LiveShutter.this.closedata();
              this.nnDisplayFlag = 44;
              LiveShutter.mHandler.postDelayed(new XxxThread(this.nnDisplayFlag), 3000L);
              bool1 = bool;
            } 
          case 7:
            bool1 = bool;
            if (!LiveShutter.this.bpopupflag) {
              LiveShutter.this.bpopupflag = true;
              LiveShutter.this.showDialog(1003);
              bool1 = bool;
            } 
          case 29:
            LiveShutter.this.showDialog(1008);
            bool1 = bool;
          case 18:
            LiveShutter.this.showDialog(1006);
            bool1 = bool;
          case 6:
          
          case 13:
            LiveShutter.this.rvf_timerCountMain.setVisibility(8);
            bool1 = bool;
          case 15:
            str = LiveShutter.this.getString(2131361953);
            bool1 = bool;
            if (LiveShutter.this.pro != null) {
              LiveShutter.this.pro.setMessage(str);
              bool1 = bool;
            } 
          case 16:
            Trace.d(LiveShutter.this.TAG, "Const.MsgId.DISPLAY_MSGCONNECTED");
            str = LiveShutter.this.getString(2131361954);
            if (LiveShutter.this.pro != null)
              LiveShutter.this.pro.setMessage(str); 
            LiveShutter.isButtonShow = true;
            LiveShutter.this.buttondisplay(true);
            LiveShutter.this.setImageResource(LiveShutter.rvf_photosize_button);
            if (!LiveShutter.this.mConfigurationManager.isCheckedStatusOfNotice()) {
              LiveShutter.this.showDialog(1012);
            } else if (LiveShutter.this.mDeviceController.getCardStatusValue().toUpperCase(Locale.ENGLISH).equals("INTERNAL")) {
              LiveShutter.this.showDialog(2008);
            } else if (FunctionManager.isNotSupportedRawQualityAndContinuousShotAtTheSameTime(LiveShutter.this.connectedSsid) && LiveShutter.this.mDeviceController.getQualityValue().toUpperCase(Locale.ENGLISH).contains("RAW") && LiveShutter.this.mDeviceController.getDriveValue().toUpperCase(Locale.ENGLISH).contains("CONTINUOUS")) {
              LiveShutter.this.showDialog(2007);
            } 
            LiveShutter.this.isInitializedCamera = true;
            LiveShutter.this.initLayout();
            bool1 = bool;
          case 24:
            if (!LiveShutter.bClosing) {
              str = LiveShutter.this.getString(2131361947);
              LiveShutter.this.streampro.setMessage(str);
              this.nnDisplayFlag = 40;
              LiveShutter.mHandler.post(new XxxThread(this.nnDisplayFlag));
              bool1 = bool;
            } else {
              return;
            } 
          case 25:
          case 26:
            LiveShutter.this.btoast = false;
            bool1 = bool;
            if (LiveShutter.this.toast != null) {
              LiveShutter.this.toast.cancel();
              bool1 = bool;
            } 
          case 20:
            Trace.e(LiveShutter.this.TAG, "-=> DISPLAY_SAVE nSaveCount=" + LiveShutter.this.nSaveCount);
            if (LiveShutter.this.nSaveCount == 0) {
              str = LiveShutter.this.getString(2131361949);
              LiveShutter.this.streampro.setMessage(str);
              Trace.e(LiveShutter.this.TAG, "-=> DISPLAY_SAVE str=" + str);
              LiveShutter.bshutter = false;
              this.nnDisplayFlag = 34;
              LiveShutter.mHandler.post(new XxxThread(this.nnDisplayFlag));
            } 
            liveShutter = LiveShutter.this;
            liveShutter.nSaveCount = liveShutter.nSaveCount + 1;
            bool1 = bool;
          case 28:
            Trace.d(LiveShutter.this.TAG, "-=> shutterbutton(true,false)-1 bshutter=" + LiveShutter.bshutter);
            if (LiveShutter.bshutter) {
              LiveShutter.this.shutterbutton(false);
              if (LiveShutter.this.nTimerCount > 0) {
                LiveShutter.this.setShutterButtonEnabled(true);
                bool1 = bool;
              } else {
                LiveShutter.this.setShutterButtonEnabled(false);
                bool1 = bool;
              } 
            } else {
              LiveShutter.this.shutterbutton(true);
              bool1 = bool;
            } 
          case 30:
            LiveShutter.this.bOnZoomProcess = false;
            LiveShutter.this.zoombardisplay();
            bool1 = bool;
          case 108:
            LiveShutter.this.ZoomButtondisplay();
            bool1 = bool;
          case 40:
            LiveShutter.this.btimercount = false;
            LiveShutter.codec_init = false;
            LiveShutter.cam_rotated_type = LiveShutter.this.cameratype;
            LiveShutter.this.nSaveCount = 0;
            if ("ssdp:rotationD".equals(LiveShutter.cam_rotated_type)) {
              LiveShutter.cBmpRotation = 'D';
            } else if ("ssdp:rotationL".equals(LiveShutter.cam_rotated_type)) {
              LiveShutter.cBmpRotation = 'L';
            } else if ("ssdp:rotationR".equals(LiveShutter.cam_rotated_type)) {
              LiveShutter.cBmpRotation = 'R';
            } else if ("ssdp:rotationU".equals(LiveShutter.cam_rotated_type)) {
              LiveShutter.cBmpRotation = 'U';
            } 
            LiveShutter.this.doAction(14, String.valueOf(LiveShutter.this.REQUESTIMAGE));
            bool1 = bool;
          case 124:
            LiveShutter.this.appclose();
            bool1 = bool;
          case 44:
            LiveShutter.this.appClose();
            bool1 = bool;
          case 45:
            bool1 = bool;
            if (!LiveShutter.this.bpopupflag) {
              Trace.d(LiveShutter.this.TAG, "RUN_EXIT_BYEBYE+++++++");
              if (LiveShutter.this.timetask != null)
                LiveShutter.this.timetask.cancel(); 
              LiveShutter.this.bpopupflag = true;
              LiveShutter.this.closedata();
              Trace.d(LiveShutter.this.TAG, "MESSAGE : RUN_EXIT");
              LiveShutter.this.showDialog(1011);
              this.nnDisplayFlag = 44;
              LiveShutter.mHandler.postDelayed(new XxxThread(this.nnDisplayFlag), 500L);
              bool1 = bool;
            } 
          case 50:
            Trace.e(LiveShutter.this.TAG, "-=> Const.MsgId.RUN_SHUTTER_UP");
            LiveShutter.this.doAction(20, "");
            bool1 = bool;
          case 47:
            Trace.e(LiveShutter.this.TAG, "-=> Const.MsgId.RUN_DISPLAY_SHOT");
            LiveShutter.rvf_touchAutoFocus.setVisibility(4);
            LiveShutter.this.showDialog(1010);
            LiveShutter.this.setmHandler(24);
            bool1 = bool;
          case 34:
            LiveShutter.this.buttondisplay(true);
            bool1 = bool;
          case 110:
            LiveShutter.this.resetVisibilityTouchAFFrame();
            Trace.d(LiveShutter.this.TAG, "-=> Const.MsgId.DISPLAY_BUTTON_ACTIVE");
            LiveShutter.bshutter = false;
            LiveShutter.this.shutterbutton(true);
            LiveShutter.this.setShutterButtonEnabled(true);
            bool1 = bool;
          case 48:
            (new LiveShutter.InitCodecTask(null)).execute((Object[])new Void[0]);
            LiveShutter.this.hideProgressBar();
            bool1 = bool;
          case 121:
            Trace.d(LiveShutter.this.TAG, "Const.MsgId.FLASH_ON_DIALOG_OPEN");
            bundle = new Bundle();
            if (RVFFunctionManager.getFlashMountType(LiveShutter.this.connectedSsid) == 0) {
              bundle.putString("DIALOG_MESSAGE_KEY", LiveShutter.this.getString(2131362015));
            } else {
              bundle.putString("DIALOG_MESSAGE_KEY", LiveShutter.this.getString(2131362014));
            } 
            LiveShutter.this.showDialog(1013, bundle);
            bool1 = bool;
          case 122:
            LiveShutter.this.LayoutChange(LiveShutter.this.getPhoneCamType());
            bool1 = bool;
          case 123:
            LiveShutter.this.setImageResource(LiveShutter.rvf_flash_button);
            bool1 = bool;
          case 125:
            LiveShutter.this.showDialog(1015);
            LiveShutter.this.shutterbutton(true);
            bool1 = bool;
        } 
        bool = bool1;
        continue;
        if (SYNTHETIC_LOCAL_VARIABLE_2 == null)
          break; 
      } 
    }
  }
  
  private class ZoomState {
    private static final int NONE = 0;
    
    private static final int ZOOM_IN = 1;
    
    private static final int ZOOM_OUT = 2;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\rvf\LiveShutter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */