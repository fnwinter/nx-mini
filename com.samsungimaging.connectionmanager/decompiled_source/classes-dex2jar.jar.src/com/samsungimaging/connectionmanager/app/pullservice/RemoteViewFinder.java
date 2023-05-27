package com.samsungimaging.connectionmanager.app.pullservice;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.samsungimaging.asphodel.multimedia.FFmpegJNI;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;
import com.samsungimaging.connectionmanager.app.localgallery.LocalGallery;
import com.samsungimaging.connectionmanager.app.pullservice.controller.DeviceController;
import com.samsungimaging.connectionmanager.app.pullservice.controller.UPNPController;
import com.samsungimaging.connectionmanager.app.pullservice.datatype.DSCResolution;
import com.samsungimaging.connectionmanager.app.pullservice.dialog.CustomDialogWheelMenu;
import com.samsungimaging.connectionmanager.app.pullservice.util.Graph;
import com.samsungimaging.connectionmanager.app.pullservice.util.ImageDownloader;
import com.samsungimaging.connectionmanager.app.pullservice.util.Utils;
import com.samsungimaging.connectionmanager.util.ExToast;
import com.samsungimaging.connectionmanager.util.Trace;
import com.samsungimaging.connectionmanager.util.Utils;
import java.util.ArrayList;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ArgumentList;
import org.kankan.wheel.widget.OnWheelClickedListener;
import org.kankan.wheel.widget.OnWheelScrollListener;
import org.kankan.wheel.widget.WheelView;
import org.kankan.wheel.widget.adapters.ArrayWheelAdapter;
import org.kankan.wheel.widget.adapters.WheelViewAdapter;

public final class RemoteViewFinder extends PullService implements OnWheelScrollListener, OnWheelClickedListener, View.OnClickListener, View.OnTouchListener, SurfaceHolder.Callback {
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
  
  private String[] AFData;
  
  private String cameraType;
  
  private UPNPController controller;
  
  private int curScreenType;
  
  private Handler deviceControlHandler;
  
  private DeviceController deviceController;
  
  private Display display;
  
  private ExToast exToast;
  
  private SurfaceHolder holder;
  
  private ImageDownloader imageDownloader;
  
  private LocationManager locationManager;
  
  private Handler msgHandler;
  
  private int nShutterDnUp;
  
  private ProgressDialog progressDialog;
  
  private ImageView rvf_af_extend_leftdown;
  
  private ImageView rvf_af_extend_leftup;
  
  private ImageView rvf_af_extend_rightdown;
  
  private ImageView rvf_af_extend_rightup;
  
  private ImageButton rvf_camcorder_button;
  
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
  
  private int[][] screenPosition = new int[4][];
  
  private int[][] screenSize = new int[4][];
  
  private int smartPanelVisibility;
  
  private String ssid;
  
  private CustomDialogWheelMenu wheelMenuDialog;
  
  private int wheelMenuID;
  
  private ArrayList<String> wheelMenuItems;
  
  private void checkScreenType() {
    switch ((getResources().getConfiguration()).orientation) {
      default:
        return;
      case 2:
        if (this.cameraType.equals("ssdp:rotationD") || this.cameraType.equals("ssdp:rotationU")) {
          this.curScreenType = 2;
          return;
        } 
        if (this.cameraType.equals("ssdp:rotationL") || this.cameraType.equals("ssdp:rotationR")) {
          this.curScreenType = 3;
          return;
        } 
      case 1:
        break;
    } 
    if (this.cameraType.equals("ssdp:rotationD") || this.cameraType.equals("ssdp:rotationU")) {
      this.curScreenType = 0;
      return;
    } 
    if (this.cameraType.equals("ssdp:rotationL") || this.cameraType.equals("ssdp:rotationR")) {
      this.curScreenType = 1;
      return;
    } 
  }
  
  private void doAction(int paramInt, String paramString) {
    this.deviceController.doAction(paramInt, paramString);
  }
  
  private void hideAFFrame() {
    rvf_af_extend.setVisibility(8);
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
  
  private void init() {
    this.deviceControlHandler = new Handler() {
        public void handleMessage(Message param1Message) {
          if (((Boolean)param1Message.obj).booleanValue()) {
            ArgumentList argumentList;
            int i;
            int j;
            switch (param1Message.what) {
              default:
                return;
              case 3:
                RemoteViewFinder.this.setContentView();
                (new RemoteViewFinder.StartCodecTask(null)).execute((Object[])new Void[0]);
                return;
              case 21:
                argumentList = RemoteViewFinder.this.deviceController.getAction(10).getOutputArgumentList();
                j = argumentList.size();
                i = 0;
                while (true) {
                  if (i >= j) {
                    if (RemoteViewFinder.this.nShutterDnUp == 2) {
                      RemoteViewFinder.this.setTimer(18);
                      RemoteViewFinder.this.doAction(14, String.valueOf(1));
                      return;
                    } 
                  } else {
                    Argument argument = argumentList.getArgument(i);
                    String str1 = argument.getName();
                    String str2 = argument.getValue();
                    if (!str1.equals("AF_MF") && str1.equals("AFSTATUS"))
                      if (str2.equals("AFEXTEND_OK")) {
                        RemoteViewFinder.this.showAFFrame(2, true);
                      } else if (str2.equals("AFEXTEND_FAIL")) {
                        RemoteViewFinder.this.showAFFrame(2, false);
                      } else if (!str2.equals("3")) {
                        if (str2.equals("AFFAIL") || str2.equals("0")) {
                          RemoteViewFinder.this.showAFFrame(1, false);
                        } else {
                          RemoteViewFinder.this.AFData = str2.split(",");
                          RemoteViewFinder.this.showAFFrame(1, true);
                        } 
                      }  
                    i++;
                    continue;
                  } 
                  if (RemoteViewFinder.this.nShutterDnUp == 3) {
                    RemoteViewFinder.this.setTimer(18);
                    RemoteViewFinder.this.doAction(13, "");
                    return;
                  } 
                  // Byte code: goto -> 384
                } 
              case 29:
                break;
            } 
            (new RemoteViewFinder.ImageDownloadTask(null)).execute((Object[])new String[] { RemoteViewFinder.access$2(this.this$0).getAFShotResult() });
            return;
          } 
        }
      };
    this.msgHandler = new Handler() {
        public void handleMessage(Message param1Message) {
          switch (param1Message.what) {
            default:
              super.handleMessage(param1Message);
              return;
            case 18:
              break;
          } 
          RemoteViewFinder.this.hideAFFrame();
        }
      };
    this.smartPanelVisibility = 8;
    this.ssid = CMInfo.getInstance().getConnectedSSID();
    this.cameraType = "ssdp:rotationD";
    this.nShutterDnUp = 0;
    for (int i = 0;; i++) {
      if (i >= 4) {
        this.exToast = ExToast.getInstance();
        this.display = ((WindowManager)getSystemService("window")).getDefaultDisplay();
        this.controller = UPNPController.getInstance();
        this.deviceController = this.controller.getDeviceController();
        this.imageDownloader = new ImageDownloader(this.msgHandler, 19);
        this.locationManager = (LocationManager)getSystemService("location");
        return;
      } 
      this.screenPosition[i] = new int[2];
      this.screenSize[i] = new int[2];
    } 
  }
  
  private void initCodec() {
    int i;
    int j;
    if (this.display.getWidth() > this.display.getHeight()) {
      j = this.display.getWidth();
      i = this.display.getHeight();
    } else {
      j = this.display.getHeight();
      i = this.display.getWidth();
    } 
    FFmpegJNI.construct(Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565), Bitmap.createBitmap(j, i, Bitmap.Config.RGB_565), this.screenPosition, this.screenSize, this.curScreenType, 0);
  }
  
  private void initLayout() {
    if (this.holder != null) {
      this.holder.removeCallback(this);
      this.holder = null;
    } 
    this.holder = this.rvf_surface.getHolder();
    this.holder.addCallback(this);
    checkScreenType();
    if (this.deviceController.getRatioOffsetMenuItems().size() > 0) {
      screenConfigChange(2);
    } else {
      screenConfigChange(Utils.toRatioType(this.deviceController.getRatioValue()));
    } 
    setIndicator();
    if (FunctionManager.isSupportedSmartPanel(this.deviceController.getVersionPrefix(), this.deviceController.getVersion())) {
      if (this.smartPanelVisibility == 0) {
        setSmartPanelVisibility(0);
        return;
      } 
    } else {
      return;
    } 
    setSmartPanelVisibility(8);
  }
  
  private void screenConfigChange(int paramInt) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: iconst_0
    //   3: istore #5
    //   5: aload_0
    //   6: ldc_w 'window'
    //   9: invokevirtual getSystemService : (Ljava/lang/String;)Ljava/lang/Object;
    //   12: checkcast android/view/WindowManager
    //   15: invokeinterface getDefaultDisplay : ()Landroid/view/Display;
    //   20: astore #13
    //   22: aload #13
    //   24: invokevirtual getWidth : ()I
    //   27: aload #13
    //   29: invokevirtual getHeight : ()I
    //   32: if_icmple -> 421
    //   35: aload #13
    //   37: invokevirtual getWidth : ()I
    //   40: istore #4
    //   42: aload #13
    //   44: invokevirtual getHeight : ()I
    //   47: istore_2
    //   48: iload_1
    //   49: tableswitch default -> 80, 1 -> 437, 2 -> 446, 3 -> 453, 4 -> 460
    //   80: iload #5
    //   82: istore_1
    //   83: iload #4
    //   85: i2f
    //   86: iload_2
    //   87: i2f
    //   88: fdiv
    //   89: iload_3
    //   90: i2f
    //   91: iload_1
    //   92: i2f
    //   93: fdiv
    //   94: fcmpg
    //   95: ifge -> 467
    //   98: iload #4
    //   100: istore #6
    //   102: iload #4
    //   104: i2f
    //   105: iload_3
    //   106: i2f
    //   107: iload_1
    //   108: i2f
    //   109: fdiv
    //   110: fdiv
    //   111: f2i
    //   112: istore #5
    //   114: iload #4
    //   116: i2f
    //   117: iload_2
    //   118: i2f
    //   119: fdiv
    //   120: iload_1
    //   121: i2f
    //   122: iload_3
    //   123: i2f
    //   124: fdiv
    //   125: fcmpg
    //   126: ifge -> 484
    //   129: iload #4
    //   131: istore #8
    //   133: iload #4
    //   135: i2f
    //   136: iload_1
    //   137: i2f
    //   138: iload_3
    //   139: i2f
    //   140: fdiv
    //   141: fdiv
    //   142: f2i
    //   143: istore #7
    //   145: ldc_w 1.3333334
    //   148: iload_3
    //   149: i2f
    //   150: iload_1
    //   151: i2f
    //   152: fdiv
    //   153: fcmpg
    //   154: ifge -> 501
    //   157: iload_2
    //   158: istore #9
    //   160: iload_2
    //   161: i2f
    //   162: iload_3
    //   163: i2f
    //   164: iload_1
    //   165: i2f
    //   166: fdiv
    //   167: fdiv
    //   168: f2i
    //   169: istore_1
    //   170: iload #9
    //   172: istore_3
    //   173: iload #4
    //   175: iload #6
    //   177: isub
    //   178: iconst_2
    //   179: idiv
    //   180: istore #9
    //   182: iload_2
    //   183: iload #5
    //   185: isub
    //   186: iconst_2
    //   187: idiv
    //   188: istore #10
    //   190: iload #4
    //   192: iload #8
    //   194: isub
    //   195: iconst_2
    //   196: idiv
    //   197: istore #4
    //   199: iload_2
    //   200: iload #7
    //   202: isub
    //   203: iconst_2
    //   204: idiv
    //   205: istore #11
    //   207: iload_2
    //   208: iload_3
    //   209: isub
    //   210: iconst_2
    //   211: idiv
    //   212: istore #12
    //   214: iload_2
    //   215: i2f
    //   216: ldc_w 1.3333334
    //   219: fdiv
    //   220: f2i
    //   221: iload_1
    //   222: isub
    //   223: iconst_2
    //   224: idiv
    //   225: istore_2
    //   226: aload_0
    //   227: getfield screenPosition : [[I
    //   230: iconst_0
    //   231: aaload
    //   232: iconst_0
    //   233: iload #12
    //   235: iastore
    //   236: aload_0
    //   237: getfield screenPosition : [[I
    //   240: iconst_0
    //   241: aaload
    //   242: iconst_1
    //   243: iload_2
    //   244: iastore
    //   245: aload_0
    //   246: getfield screenPosition : [[I
    //   249: iconst_1
    //   250: aaload
    //   251: iconst_0
    //   252: iload #10
    //   254: iastore
    //   255: aload_0
    //   256: getfield screenPosition : [[I
    //   259: iconst_1
    //   260: aaload
    //   261: iconst_1
    //   262: iload #9
    //   264: iastore
    //   265: aload_0
    //   266: getfield screenPosition : [[I
    //   269: iconst_2
    //   270: aaload
    //   271: iconst_0
    //   272: iload #9
    //   274: iastore
    //   275: aload_0
    //   276: getfield screenPosition : [[I
    //   279: iconst_2
    //   280: aaload
    //   281: iconst_1
    //   282: iload #10
    //   284: iastore
    //   285: aload_0
    //   286: getfield screenPosition : [[I
    //   289: iconst_3
    //   290: aaload
    //   291: iconst_0
    //   292: iload #4
    //   294: iastore
    //   295: aload_0
    //   296: getfield screenPosition : [[I
    //   299: iconst_3
    //   300: aaload
    //   301: iconst_1
    //   302: iload #11
    //   304: iastore
    //   305: aload_0
    //   306: getfield screenSize : [[I
    //   309: iconst_0
    //   310: aaload
    //   311: iconst_0
    //   312: iload_3
    //   313: iastore
    //   314: aload_0
    //   315: getfield screenSize : [[I
    //   318: iconst_0
    //   319: aaload
    //   320: iconst_1
    //   321: iload_1
    //   322: iastore
    //   323: aload_0
    //   324: getfield screenSize : [[I
    //   327: iconst_1
    //   328: aaload
    //   329: iconst_0
    //   330: iload #5
    //   332: iastore
    //   333: aload_0
    //   334: getfield screenSize : [[I
    //   337: iconst_1
    //   338: aaload
    //   339: iconst_1
    //   340: iload #6
    //   342: iastore
    //   343: aload_0
    //   344: getfield screenSize : [[I
    //   347: iconst_2
    //   348: aaload
    //   349: iconst_0
    //   350: iload #6
    //   352: iastore
    //   353: aload_0
    //   354: getfield screenSize : [[I
    //   357: iconst_2
    //   358: aaload
    //   359: iconst_1
    //   360: iload #5
    //   362: iastore
    //   363: aload_0
    //   364: getfield screenSize : [[I
    //   367: iconst_3
    //   368: aaload
    //   369: iconst_0
    //   370: iload #8
    //   372: iastore
    //   373: aload_0
    //   374: getfield screenSize : [[I
    //   377: iconst_3
    //   378: aaload
    //   379: iconst_1
    //   380: iload #7
    //   382: iastore
    //   383: iconst_0
    //   384: istore_1
    //   385: aload_0
    //   386: getfield cameraType : Ljava/lang/String;
    //   389: ldc_w 'ssdp:rotationD'
    //   392: invokevirtual equals : (Ljava/lang/Object;)Z
    //   395: ifeq -> 527
    //   398: iconst_0
    //   399: istore_1
    //   400: getstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI$Command.SCREEN_CONFIG_CHANGE : Lcom/samsungimaging/asphodel/multimedia/FFmpegJNI$Command;
    //   403: aload_0
    //   404: getfield curScreenType : I
    //   407: iload_1
    //   408: aload_0
    //   409: getfield screenPosition : [[I
    //   412: aload_0
    //   413: getfield screenSize : [[I
    //   416: invokestatic request : (Lcom/samsungimaging/asphodel/multimedia/FFmpegJNI$Command;II[[I[[I)I
    //   419: pop
    //   420: return
    //   421: aload #13
    //   423: invokevirtual getHeight : ()I
    //   426: istore #4
    //   428: aload #13
    //   430: invokevirtual getWidth : ()I
    //   433: istore_2
    //   434: goto -> 48
    //   437: bipush #16
    //   439: istore_3
    //   440: bipush #9
    //   442: istore_1
    //   443: goto -> 83
    //   446: iconst_4
    //   447: istore_3
    //   448: iconst_3
    //   449: istore_1
    //   450: goto -> 83
    //   453: iconst_3
    //   454: istore_3
    //   455: iconst_2
    //   456: istore_1
    //   457: goto -> 83
    //   460: iconst_1
    //   461: istore_3
    //   462: iconst_1
    //   463: istore_1
    //   464: goto -> 83
    //   467: iload_2
    //   468: i2f
    //   469: iload_1
    //   470: i2f
    //   471: iload_3
    //   472: i2f
    //   473: fdiv
    //   474: fdiv
    //   475: f2i
    //   476: istore #6
    //   478: iload_2
    //   479: istore #5
    //   481: goto -> 114
    //   484: iload_2
    //   485: i2f
    //   486: iload_3
    //   487: i2f
    //   488: iload_1
    //   489: i2f
    //   490: fdiv
    //   491: fdiv
    //   492: f2i
    //   493: istore #8
    //   495: iload_2
    //   496: istore #7
    //   498: goto -> 145
    //   501: iload_2
    //   502: i2f
    //   503: ldc_w 1.3333334
    //   506: fdiv
    //   507: f2i
    //   508: istore #9
    //   510: iload #9
    //   512: i2f
    //   513: iload_1
    //   514: i2f
    //   515: iload_3
    //   516: i2f
    //   517: fdiv
    //   518: fdiv
    //   519: f2i
    //   520: istore_3
    //   521: iload #9
    //   523: istore_1
    //   524: goto -> 173
    //   527: aload_0
    //   528: getfield cameraType : Ljava/lang/String;
    //   531: ldc_w 'ssdp:rotationR'
    //   534: invokevirtual equals : (Ljava/lang/Object;)Z
    //   537: ifeq -> 546
    //   540: bipush #90
    //   542: istore_1
    //   543: goto -> 400
    //   546: aload_0
    //   547: getfield cameraType : Ljava/lang/String;
    //   550: ldc_w 'ssdp:rotationU'
    //   553: invokevirtual equals : (Ljava/lang/Object;)Z
    //   556: ifeq -> 566
    //   559: sipush #180
    //   562: istore_1
    //   563: goto -> 400
    //   566: aload_0
    //   567: getfield cameraType : Ljava/lang/String;
    //   570: ldc_w 'ssdp:rotationL'
    //   573: invokevirtual equals : (Ljava/lang/Object;)Z
    //   576: ifeq -> 400
    //   579: sipush #270
    //   582: istore_1
    //   583: goto -> 400
  }
  
  private void setContentView() {
    Trace.d(Trace.Tag.RVF, "start setContentView()");
    switch (this.curScreenType) {
      default:
        this.rvf_surface = (SurfaceView)findViewById(2131558403);
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
        rvf_zoomSeekBar = (SeekBar)findViewById(2131558480);
        this.rvf_mode_button = (Button)findViewById(2131558481);
        this.rvf_mode_button.setOnClickListener(this);
        this.rvf_shutter_button = (ImageButton)findViewById(2131558482);
        this.rvf_shutter_button.setOnTouchListener(this);
        this.rvf_camcorder_button = (ImageButton)findViewById(2131558483);
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
        initLayout();
        return;
      case 0:
      case 2:
        setContentView(2130903042);
      case 1:
      case 3:
        break;
    } 
    setContentView(2130903043);
  }
  
  private void setImageResource(ImageButton paramImageButton) {
    String str1;
    String str2;
    switch (paramImageButton.getId()) {
      default:
        return;
      case 2131558466:
        str1 = this.deviceController.getDefaultFlash().toUpperCase();
        if (str1.equals("OFF") || str1.equals("")) {
          paramImageButton.setBackgroundResource(2130837974);
          return;
        } 
        str1 = this.deviceController.getCurrentFlashDisplay().toUpperCase();
        if (str1.equals("OFF")) {
          paramImageButton.setBackgroundResource(2130837974);
          return;
        } 
        if (str1.equals("AUTO")) {
          paramImageButton.setBackgroundResource(2130837973);
          return;
        } 
        if (str1.equals("REDEYE")) {
          paramImageButton.setBackgroundResource(2130837976);
          return;
        } 
        if (str1.equals("FILLIN") || str1.equals("FILL-IN") || str1.equals("FILL IN")) {
          paramImageButton.setBackgroundResource(2130837972);
          return;
        } 
        if (str1.equals("SLOWSYNC")) {
          paramImageButton.setBackgroundResource(2130837978);
          return;
        } 
        if (str1.equals("REDEYEFIX")) {
          paramImageButton.setBackgroundResource(2130837977);
          return;
        } 
        if (str1.equals("FILLINREDEYE") || str1.equals("FILL-IN_RED") || str1.equals("FILL-IN RED")) {
          paramImageButton.setBackgroundResource(2130837975);
          return;
        } 
        if (str1.equals("1STCURTAIN") || str1.equals("1ST_CURTAIN") || str1.equals("1ST CURTAIN")) {
          paramImageButton.setBackgroundResource(2130837970);
          return;
        } 
        if (str1.equals("2NDCURTAIN") || str1.equals("2ND_CURTAIN") || str1.equals("2ND CURTAIN")) {
          paramImageButton.setBackgroundResource(2130837971);
          return;
        } 
      case 2131558467:
        str1 = this.deviceController.getCurrentLEDTimeMenuItem().toUpperCase();
        if (str1.equals("OFF")) {
          paramImageButton.setBackgroundResource(2130838043);
          return;
        } 
        if (str1.equals("2SEC")) {
          paramImageButton.setBackgroundResource(2130838039);
          return;
        } 
        if (str1.equals("5SEC")) {
          paramImageButton.setBackgroundResource(2130838041);
          return;
        } 
        if (str1.equals("10SEC")) {
          paramImageButton.setBackgroundResource(2130838038);
          return;
        } 
        if (str1.equals("DOUBLE")) {
          paramImageButton.setBackgroundResource(2130838042);
          return;
        } 
      case 2131558468:
        if (this.deviceController.getResolutionMenuItems().size() > 0) {
          DSCResolution dSCResolution = this.deviceController.getResolutionMenuItems().get(Integer.parseInt(this.deviceController.getDefaultResolutionIndex()));
          if (dSCResolution != null) {
            String str = Utils.unitChange(dSCResolution.getWidth(), dSCResolution.getHeight());
            if (str.equals("1M")) {
              paramImageButton.setBackgroundResource(2130838018);
              return;
            } 
            if (str.equals("1.1 M")) {
              paramImageButton.setBackgroundResource(2130838017);
              return;
            } 
            if (str.equals("2.1 M") || str.equals("2M")) {
              paramImageButton.setBackgroundResource(2130838021);
              return;
            } 
            if (str.equals("W2M")) {
              paramImageButton.setBackgroundResource(2130838022);
              return;
            } 
            if (str.equals("3M")) {
              paramImageButton.setBackgroundResource(2130838023);
              return;
            } 
            if (str.equals("4M")) {
              paramImageButton.setBackgroundResource(2130838026);
              return;
            } 
            if (str.equals("W4M")) {
              paramImageButton.setBackgroundResource(2130838027);
              return;
            } 
            if (str.equals("5M")) {
              paramImageButton.setBackgroundResource(2130838029);
              return;
            } 
            if (str.equals("5.9M")) {
              paramImageButton.setBackgroundResource(2130838028);
              return;
            } 
            if (str.equals("7M")) {
              paramImageButton.setBackgroundResource(2130838031);
              return;
            } 
            if (str.equals("W7M")) {
              paramImageButton.setBackgroundResource(2130838032);
              return;
            } 
            if (str.equals("8M")) {
              paramImageButton.setBackgroundResource(2130838033);
              return;
            } 
            if (str.equals("9M")) {
              paramImageButton.setBackgroundResource(2130838034);
              return;
            } 
            if (str.equals("10M")) {
              paramImageButton.setBackgroundResource(2130838008);
              return;
            } 
            if (str.equals("W10M")) {
              paramImageButton.setBackgroundResource(2130838009);
              return;
            } 
            if (str.equals("W12M")) {
              paramImageButton.setBackgroundResource(2130838010);
              return;
            } 
            if (str.equals("13M")) {
              paramImageButton.setBackgroundResource(2130838012);
              return;
            } 
            if (str.equals("P14M")) {
              paramImageButton.setBackgroundResource(2130838013);
              return;
            } 
            if (str.equals("16M")) {
              paramImageButton.setBackgroundResource(2130838015);
              return;
            } 
            if (str.equals("W16M")) {
              paramImageButton.setBackgroundResource(2130838016);
              return;
            } 
            if (str.equals("20M")) {
              paramImageButton.setBackgroundResource(2130838019);
              return;
            } 
            paramImageButton.setBackgroundResource(2130838008);
            return;
          } 
        } 
        paramImageButton.setBackgroundResource(2130838008);
        return;
      case 2131558470:
        paramImageButton.setBackgroundResource(2130837964);
        return;
      case 2131558700:
        str1 = this.deviceController.getWBValue().toUpperCase();
        if (str1.equals("AUTO")) {
          paramImageButton.setImageResource(2130838048);
          return;
        } 
        if (str1.equals("DAYLIGHT")) {
          paramImageButton.setImageResource(2130838052);
          return;
        } 
        if (str1.equals("CLOUDY")) {
          paramImageButton.setImageResource(2130838049);
          return;
        } 
        if (str1.equals("FLUORESCENT_W")) {
          paramImageButton.setImageResource(2130838058);
          return;
        } 
        if (str1.equals("FLUORESCENT_N") || str1.equals("FLUORESCENT_NW")) {
          paramImageButton.setImageResource(2130838057);
          return;
        } 
        if (str1.equals("FLUORESCENT_D")) {
          paramImageButton.setImageResource(2130838054);
          return;
        } 
        if (str1.equals("TUNGSTEN")) {
          paramImageButton.setImageResource(2130838059);
          return;
        } 
        if (str1.equals("FLASHWB")) {
          paramImageButton.setImageResource(2130838053);
          return;
        } 
        if (str1.equals("COLORTEMP")) {
          paramImageButton.setImageResource(2130838050);
          return;
        } 
      case 2131558701:
        str1 = this.deviceController.getMeteringValue().toUpperCase();
        if (str1.equals("MULTI")) {
          paramImageButton.setImageResource(2130837980);
          return;
        } 
        if (str1.equals("CENTERWEIGHTED") || str1.equals("CENTER-WEIGHTED")) {
          paramImageButton.setImageResource(2130837979);
          return;
        } 
        if (str1.equals("SPOT")) {
          paramImageButton.setImageResource(2130837981);
          return;
        } 
      case 2131558703:
        str1 = this.deviceController.getDefaultFlash().toUpperCase();
        if (str1.equals("OFF") || str1.equals("")) {
          paramImageButton.setImageResource(2130837974);
          return;
        } 
        str1 = this.deviceController.getCurrentFlashDisplay().toUpperCase();
        if (str1.equals("OFF")) {
          paramImageButton.setImageResource(2130837974);
          return;
        } 
        if (str1.equals("AUTO")) {
          paramImageButton.setImageResource(2130837973);
          return;
        } 
        if (str1.equals("REDEYE")) {
          paramImageButton.setImageResource(2130837976);
          return;
        } 
        if (str1.equals("FILLIN") || str1.equals("FILL-IN") || str1.equals("FILL IN")) {
          paramImageButton.setImageResource(2130837972);
          return;
        } 
        if (str1.equals("SLOWSYNC")) {
          paramImageButton.setImageResource(2130837978);
          return;
        } 
        if (str1.equals("REDEYEFIX")) {
          paramImageButton.setImageResource(2130837977);
          return;
        } 
        if (str1.equals("FILLINREDEYE") || str1.equals("FILL-IN_RED") || str1.equals("FILL-IN RED")) {
          paramImageButton.setImageResource(2130837975);
          return;
        } 
        if (str1.equals("1STCURTAIN") || str1.equals("1ST_CURTAIN") || str1.equals("1ST CURTAIN")) {
          paramImageButton.setImageResource(2130837970);
          return;
        } 
        if (str1.equals("2NDCURTAIN") || str1.equals("2ND_CURTAIN") || str1.equals("2ND CURTAIN")) {
          paramImageButton.setImageResource(2130837971);
          return;
        } 
      case 2131558705:
        if (this.deviceController.getResolutionMenuItems().size() > 0) {
          DSCResolution dSCResolution = this.deviceController.getResolutionMenuItems().get(Integer.parseInt(this.deviceController.getDefaultResolutionIndex()));
          if (dSCResolution != null) {
            String str = Utils.unitChange(dSCResolution.getWidth(), dSCResolution.getHeight());
            if (str.equals("1M")) {
              paramImageButton.setImageResource(2130838018);
              return;
            } 
            if (str.equals("1.1 M")) {
              paramImageButton.setImageResource(2130838017);
              return;
            } 
            if (str.equals("2.1 M") || str.equals("2M")) {
              paramImageButton.setImageResource(2130838021);
              return;
            } 
            if (str.equals("W2M")) {
              paramImageButton.setImageResource(2130838022);
              return;
            } 
            if (str.equals("3M")) {
              paramImageButton.setImageResource(2130838023);
              return;
            } 
            if (str.equals("4M")) {
              paramImageButton.setImageResource(2130838026);
              return;
            } 
            if (str.equals("W4M")) {
              paramImageButton.setImageResource(2130838027);
              return;
            } 
            if (str.equals("5M")) {
              paramImageButton.setImageResource(2130838029);
              return;
            } 
            if (str.equals("5.9M")) {
              paramImageButton.setImageResource(2130838028);
              return;
            } 
            if (str.equals("7M")) {
              paramImageButton.setImageResource(2130838031);
              return;
            } 
            if (str.equals("W7M")) {
              paramImageButton.setImageResource(2130838032);
              return;
            } 
            if (str.equals("8M")) {
              paramImageButton.setImageResource(2130838033);
              return;
            } 
            if (str.equals("9M")) {
              paramImageButton.setImageResource(2130838034);
              return;
            } 
            if (str.equals("10M")) {
              paramImageButton.setImageResource(2130838008);
              return;
            } 
            if (str.equals("W10M")) {
              paramImageButton.setImageResource(2130838009);
              return;
            } 
            if (str.equals("W12M")) {
              paramImageButton.setImageResource(2130838010);
              return;
            } 
            if (str.equals("13M")) {
              paramImageButton.setImageResource(2130838012);
              return;
            } 
            if (str.equals("P14M")) {
              paramImageButton.setImageResource(2130838013);
              return;
            } 
            if (str.equals("16M")) {
              paramImageButton.setImageResource(2130838015);
              return;
            } 
            if (str.equals("W16M")) {
              paramImageButton.setImageResource(2130838016);
              return;
            } 
            if (str.equals("20M")) {
              paramImageButton.setImageResource(2130838019);
              return;
            } 
            paramImageButton.setImageResource(2130838008);
            return;
          } 
        } 
        paramImageButton.setImageResource(2130838008);
        return;
      case 2131558706:
        str1 = this.deviceController.getQualityValue().toUpperCase();
        if (str1.equals("SUPER FINE")) {
          paramImageButton.setImageResource(2130838000);
          return;
        } 
        if (str1.equals("FINE")) {
          paramImageButton.setImageResource(2130837996);
          return;
        } 
        if (str1.equals("NORMAL")) {
          paramImageButton.setImageResource(2130837997);
          return;
        } 
        if (str1.equals("RAW")) {
          paramImageButton.setImageResource(2130837998);
          return;
        } 
        if (str1.equals("RAW+JPEG")) {
          paramImageButton.setImageResource(2130837999);
          return;
        } 
      case 2131558707:
        str1 = this.deviceController.getMovieResolutionValue().toUpperCase();
        if (this.deviceController.getVideoOutValue().toUpperCase().equals("NTSC")) {
          if (str1.equals("1920x1080 (15p)")) {
            paramImageButton.setImageResource(2130837982);
            return;
          } 
          if (str1.equals("1920x1080 (30p)")) {
            paramImageButton.setImageResource(2130837984);
            return;
          } 
          if (str1.equals("1920x1080 (60p)")) {
            paramImageButton.setImageResource(2130837985);
            return;
          } 
          if (str1.equals("1920x810 (24p)")) {
            paramImageButton.setImageResource(2130837995);
            return;
          } 
          if (str1.equals("1280x720 (30p)")) {
            paramImageButton.setImageResource(2130837993);
            return;
          } 
          if (str1.equals("1280x720 (60p)")) {
            paramImageButton.setImageResource(2130837994);
            return;
          } 
          if (str1.equals("640x480 (30p)")) {
            paramImageButton.setImageResource(2130837990);
            return;
          } 
          if (str1.equals("320x240 (30p)")) {
            paramImageButton.setImageResource(2130837987);
            return;
          } 
        } 
        if (this.deviceController.getVideoOutValue().toUpperCase().equals("PAL")) {
          if (str1.equals("1920x1080 (25p)")) {
            paramImageButton.setImageResource(2130837983);
            return;
          } 
          if (str1.equals("1280x720 (25p)")) {
            paramImageButton.setImageResource(2130837992);
            return;
          } 
          if (str1.equals("640x480 (25p)")) {
            paramImageButton.setImageResource(2130837989);
            return;
          } 
        } 
      case 2131558709:
        str1 = this.deviceController.getDefaultFocusState().toUpperCase();
        str2 = this.deviceController.getAFAreaValue().toUpperCase();
        if (str1.equals("MF")) {
          if (str2.equals("SELECTION AF")) {
            paramImageButton.setImageResource(2130837586);
            return;
          } 
          if (str2.equals("MULTI AF")) {
            paramImageButton.setImageResource(2130837583);
            return;
          } 
        } 
        if (str2.equals("SELECTION AF")) {
          paramImageButton.setImageResource(2130837953);
          return;
        } 
        if (str2.equals("MULTI AF")) {
          paramImageButton.setImageResource(2130837952);
          return;
        } 
      case 2131558710:
        str1 = this.deviceController.getDefaultFocusState().toUpperCase();
        str2 = this.deviceController.getTouchAFValue().toUpperCase();
        if (str1.equals("MF")) {
          paramImageButton.setImageResource(2130837899);
          return;
        } 
        if (str2.equals("OFF")) {
          paramImageButton.setImageResource(2130838045);
          return;
        } 
        if (str2.equals("TOUCH AF")) {
          paramImageButton.setImageResource(2130838047);
          return;
        } 
        if (str2.equals("AF POINT")) {
          paramImageButton.setImageResource(2130838044);
          return;
        } 
      case 2131558711:
        if (this.deviceController.getFileSaveMenuItems().size() > 0) {
          str1 = this.deviceController.getFileSaveValue();
          if (str1.equals(this.deviceController.getFileSaveMenuItems().get(0))) {
            paramImageButton.setImageResource(2130838036);
            return;
          } 
          if (str1.equals(this.deviceController.getFileSaveMenuItems().get(1))) {
            paramImageButton.setImageResource(2130838035);
            return;
          } 
        } 
      case 2131558708:
        str1 = this.deviceController.getCurrentStreamQuality().toUpperCase();
        if (str1.equals("HIGH")) {
          paramImageButton.setImageResource(2130837991);
          return;
        } 
        if (str1.equals("LOW")) {
          paramImageButton.setImageResource(2130837988);
          return;
        } 
      case 2131558677:
        str1 = this.deviceController.getWBValue().toUpperCase();
        if (!str1.equals("AUTO")) {
          if (str1.equals("DAYLIGHT")) {
            paramImageButton.setBackgroundResource(2130838152);
            return;
          } 
          if (str1.equals("CLOUDY")) {
            paramImageButton.setBackgroundResource(2130838150);
            return;
          } 
          if (str1.equals("FLUORESCENT_W")) {
            paramImageButton.setBackgroundResource(2130838156);
            return;
          } 
          if (str1.equals("FLUORESCENT_N") || str1.equals("FLUORESCENT_NW")) {
            paramImageButton.setBackgroundResource(2130838155);
            return;
          } 
          if (str1.equals("FLUORESCENT_D")) {
            paramImageButton.setBackgroundResource(2130838154);
            return;
          } 
          if (str1.equals("TUNGSTEN")) {
            paramImageButton.setBackgroundResource(2130838157);
            return;
          } 
          if (str1.equals("FLASHWB")) {
            paramImageButton.setBackgroundResource(2130838153);
            return;
          } 
          if (str1.equals("COLORTEMP")) {
            paramImageButton.setBackgroundResource(2130838151);
            return;
          } 
        } 
      case 2131558678:
        str1 = this.deviceController.getMeteringValue().toUpperCase();
        if (!str1.equals("MULTI")) {
          if (str1.equals("CENTERWEIGHTED") || str1.equals("CENTER-WEIGHTED")) {
            paramImageButton.setBackgroundResource(2130838089);
            return;
          } 
          if (str1.equals("SPOT")) {
            paramImageButton.setBackgroundResource(2130838090);
            return;
          } 
        } 
      case 2131558679:
        str1 = this.deviceController.getDriveValue().toUpperCase();
        if (!str1.equals("SINGLE")) {
          if (str1.equals("CONTINUOUS")) {
            paramImageButton.setBackgroundResource(2130838079);
            return;
          } 
          if (str1.equals("TIMER (2SEC)")) {
            paramImageButton.setBackgroundResource(2130838141);
            return;
          } 
          if (str1.equals("TIMER (5SEC)")) {
            paramImageButton.setBackgroundResource(2130838143);
            return;
          } 
          if (str1.equals("TIMER (10SEC)")) {
            paramImageButton.setBackgroundResource(2130838140);
            return;
          } 
          if (str1.equals("TIMER (30SEC)")) {
            paramImageButton.setBackgroundResource(2130838142);
            return;
          } 
          if (str1.equals("TIMER (DOUBLE)")) {
            paramImageButton.setBackgroundResource(2130838144);
            return;
          } 
        } 
      case 2131558680:
        str1 = this.deviceController.getDefaultFlash().toUpperCase();
        if (!str1.equals("OFF") && !str1.equals("")) {
          str1 = this.deviceController.getCurrentFlashDisplay().toUpperCase();
          if (!str1.equals("OFF")) {
            if (str1.equals("AUTO")) {
              paramImageButton.setBackgroundResource(2130838082);
              return;
            } 
            if (str1.equals("REDEYE")) {
              paramImageButton.setBackgroundResource(2130838085);
              return;
            } 
            if (str1.equals("FILLIN") || str1.equals("FILL-IN") || str1.equals("FILL IN")) {
              paramImageButton.setBackgroundResource(2130838083);
              return;
            } 
            if (str1.equals("SLOWSYNC")) {
              paramImageButton.setBackgroundResource(2130838087);
              return;
            } 
            if (str1.equals("REDEYEFIX")) {
              paramImageButton.setBackgroundResource(2130838086);
              return;
            } 
            if (str1.equals("FILLINREDEYE") || str1.equals("FILL-IN_RED") || str1.equals("FILL-IN RED")) {
              paramImageButton.setBackgroundResource(2130838085);
              return;
            } 
            if (str1.equals("1STCURTAIN") || str1.equals("1ST_CURTAIN") || str1.equals("1ST CURTAIN")) {
              paramImageButton.setBackgroundResource(2130838080);
              return;
            } 
            if (str1.equals("2NDCURTAIN") || str1.equals("2ND_CURTAIN") || str1.equals("2ND CURTAIN")) {
              paramImageButton.setBackgroundResource(2130838081);
              return;
            } 
          } 
        } 
      case 2131558681:
        if (this.deviceController.getResolutionMenuItems().size() > 0) {
          DSCResolution dSCResolution = this.deviceController.getResolutionMenuItems().get(Integer.parseInt(this.deviceController.getDefaultResolutionIndex()));
          if (dSCResolution != null) {
            String str = Utils.unitChange(dSCResolution.getWidth(), dSCResolution.getHeight());
            if (str.equals("1M")) {
              paramImageButton.setBackgroundResource(2130838118);
              return;
            } 
            if (str.equals("1.1 M")) {
              paramImageButton.setBackgroundResource(2130838117);
              return;
            } 
            if (str.equals("2.1 M") || str.equals("2M")) {
              paramImageButton.setBackgroundResource(2130838122);
              return;
            } 
            if (str.equals("W2M")) {
              paramImageButton.setBackgroundResource(2130838123);
              return;
            } 
            if (str.equals("3M")) {
              paramImageButton.setBackgroundResource(2130838124);
              return;
            } 
            if (str.equals("4M")) {
              paramImageButton.setBackgroundResource(2130838127);
              return;
            } 
            if (str.equals("W4M")) {
              paramImageButton.setBackgroundResource(2130838128);
              return;
            } 
            if (str.equals("5M")) {
              paramImageButton.setBackgroundResource(2130838130);
              return;
            } 
            if (str.equals("5.9M")) {
              paramImageButton.setBackgroundResource(2130838129);
              return;
            } 
            if (str.equals("7M")) {
              paramImageButton.setBackgroundResource(2130838133);
              return;
            } 
            if (str.equals("W7M")) {
              paramImageButton.setBackgroundResource(2130838134);
              return;
            } 
            if (str.equals("8M")) {
              paramImageButton.setBackgroundResource(2130838135);
              return;
            } 
            if (str.equals("9M")) {
              paramImageButton.setBackgroundResource(2130838136);
              return;
            } 
            if (str.equals("10M")) {
              paramImageButton.setBackgroundResource(2130838107);
              return;
            } 
            if (str.equals("W10M")) {
              paramImageButton.setBackgroundResource(2130838108);
              return;
            } 
            if (str.equals("W12M")) {
              paramImageButton.setBackgroundResource(2130838109);
              return;
            } 
            if (str.equals("13M")) {
              paramImageButton.setBackgroundResource(2130838111);
              return;
            } 
            if (str.equals("P14M")) {
              paramImageButton.setBackgroundResource(2130838112);
              return;
            } 
            if (str.equals("16M")) {
              paramImageButton.setBackgroundResource(2130838115);
              return;
            } 
            if (str.equals("W16M")) {
              paramImageButton.setBackgroundResource(2130838116);
              return;
            } 
            if (str.equals("20M")) {
              paramImageButton.setBackgroundResource(2130838119);
              return;
            } 
          } 
        } 
      case 2131558682:
        str1 = this.deviceController.getQualityValue().toUpperCase();
        if (!str1.equals("SUPER FINE") && !str1.equals("FINE") && !str1.equals("NORMAL")) {
          if (str1.equals("RAW")) {
            paramImageButton.setBackgroundResource(2130838104);
            return;
          } 
          if (str1.equals("RAW+JPEG")) {
            paramImageButton.setBackgroundResource(2130838105);
            return;
          } 
        } 
      case 2131558683:
        str1 = this.deviceController.getMovieResolutionValue();
        if (this.deviceController.getVideoOutValue().toUpperCase().equals("NTSC")) {
          if (str1.equals("1920x1080 (15p)")) {
            paramImageButton.setBackgroundResource(2130838091);
            return;
          } 
          if (str1.equals("1920x1080 (30p)")) {
            paramImageButton.setBackgroundResource(2130838093);
            return;
          } 
          if (str1.equals("1920x1080 (60p)")) {
            paramImageButton.setBackgroundResource(2130838094);
            return;
          } 
          if (str1.equals("1920x810 (24p)")) {
            paramImageButton.setBackgroundResource(2130838102);
            return;
          } 
          if (str1.equals("1280x720 (30p)")) {
            paramImageButton.setBackgroundResource(2130838100);
            return;
          } 
          if (str1.equals("1280x720 (60p)")) {
            paramImageButton.setBackgroundResource(2130838101);
            return;
          } 
          if (str1.equals("640x480 (30p)")) {
            paramImageButton.setBackgroundResource(2130838098);
            return;
          } 
          if (str1.equals("320x240 (30p)")) {
            paramImageButton.setBackgroundResource(2130838096);
            return;
          } 
        } 
        if (this.deviceController.getVideoOutValue().toUpperCase().equals("PAL")) {
          if (str1.equals("1920x1080 (15p)")) {
            paramImageButton.setBackgroundResource(2130838092);
            return;
          } 
          if (str1.equals("1280x720 (30p)")) {
            paramImageButton.setBackgroundResource(2130838099);
            return;
          } 
          if (str1.equals("640x480 (30p)")) {
            paramImageButton.setBackgroundResource(2130838097);
            return;
          } 
        } 
      case 2131558684:
        str1 = this.deviceController.getAFAreaValue().toUpperCase();
        if (str1.equals("SELECTION AF")) {
          paramImageButton.setBackgroundResource(2130838078);
          return;
        } 
        if (str1.equals("MULTI AF")) {
          paramImageButton.setBackgroundResource(2130838077);
          return;
        } 
      case 2131558685:
        str1 = this.deviceController.getTouchAFValue().toUpperCase();
        if (str1.equals("OFF")) {
          paramImageButton.setBackgroundResource(2130838147);
          return;
        } 
        if (str1.equals("TOUCH AF")) {
          paramImageButton.setBackgroundResource(2130838149);
          return;
        } 
        if (str1.equals("AF POINT")) {
          paramImageButton.setBackgroundResource(2130838146);
          return;
        } 
        if (str1.equals("ONE TOUCH SHOT")) {
          paramImageButton.setBackgroundResource(2130838148);
          return;
        } 
      case 2131558686:
        break;
    } 
    if (this.deviceController.getFileSaveMenuItems().size() > 0) {
      str1 = this.deviceController.getFileSaveValue();
      if (str1.equals(this.deviceController.getFileSaveMenuItems().get(0))) {
        paramImageButton.setBackgroundResource(2130838139);
        return;
      } 
      if (str1.equals(this.deviceController.getFileSaveMenuItems().get(1))) {
        paramImageButton.setBackgroundResource(2130838137);
        return;
      } 
    } 
  }
  
  private void setIndicator() {
    setIndicator(2131558677);
    setIndicator(2131558678);
    setIndicator(2131558679);
    setIndicator(2131558680);
    setIndicator(2131558681);
    setIndicator(2131558682);
    setIndicator(2131558683);
    setIndicator(2131558684);
    setIndicator(2131558685);
    setIndicator(2131558686);
  }
  
  private void setIndicator(int paramInt) {
    String str;
    switch (paramInt) {
      default:
        return;
      case 2131558677:
        str = this.deviceController.getWBValue().toUpperCase();
        if (str.equals("") || str.equals("AUTO")) {
          this.rvf_indicator_wb.setVisibility(8);
          return;
        } 
        this.rvf_indicator_wb.setVisibility(0);
        setImageResource(this.rvf_indicator_wb);
        return;
      case 2131558678:
        if (this.deviceController.getMeteringValue().toUpperCase().equals("MULTI")) {
          this.rvf_indicator_metering.setVisibility(8);
          return;
        } 
        this.rvf_indicator_metering.setVisibility(0);
        setImageResource(this.rvf_indicator_metering);
        return;
      case 2131558679:
        if (this.deviceController.getDriveValue().toUpperCase().equals("SINGLE")) {
          this.rvf_indicator_drive.setVisibility(8);
          return;
        } 
        this.rvf_indicator_drive.setVisibility(0);
        setImageResource(this.rvf_indicator_drive);
        return;
      case 2131558680:
        if (this.deviceController.getCurrentFlashDisplay().toUpperCase().equals("OFF")) {
          this.rvf_indicator_flash.setVisibility(8);
          return;
        } 
        this.rvf_indicator_flash.setVisibility(0);
        setImageResource(this.rvf_indicator_flash);
        return;
      case 2131558681:
        this.rvf_indicator_photo_size.setVisibility(0);
        setImageResource(this.rvf_indicator_photo_size);
        return;
      case 2131558682:
        str = this.deviceController.getQualityValue().toUpperCase();
        if (str.equals("SUPER FINE") || str.equals("FINE") || str.equals("NORMAL")) {
          this.rvf_indicator_quality.setVisibility(8);
          return;
        } 
        this.rvf_indicator_quality.setVisibility(0);
        setImageResource(this.rvf_indicator_quality);
        return;
      case 2131558683:
        this.rvf_indicator_movie_size.setVisibility(0);
        setImageResource(this.rvf_indicator_movie_size);
        return;
      case 2131558684:
        if (this.deviceController.getAFAreaMenuItems().size() == 0) {
          this.rvf_indicator_af_area.setVisibility(8);
          return;
        } 
        this.rvf_indicator_af_area.setVisibility(0);
        setImageResource(this.rvf_indicator_af_area);
        return;
      case 2131558685:
        if (this.deviceController.getDriveValue().toUpperCase().equals("OFF") || this.deviceController.getTouchAFMenuItems().size() == 0) {
          this.rvf_indicator_touch_af.setVisibility(8);
          return;
        } 
        this.rvf_indicator_touch_af.setVisibility(0);
        setImageResource(this.rvf_indicator_touch_af);
        return;
      case 2131558686:
        break;
    } 
    this.rvf_indicator_storage.setVisibility(0);
    setImageResource(this.rvf_indicator_storage);
  }
  
  private void setOSDVisibility(int paramInt) {
    rvf_title.setVisibility(paramInt);
    if (FunctionManager.isSupportedSmartPanel(this.deviceController.getVersionPrefix(), this.deviceController.getVersion())) {
      this.rvf_smart_panel_icon.setVisibility(paramInt);
      this.rvf_mode_button.setVisibility(paramInt);
      this.rvf_camcorder_button.setVisibility(paramInt);
    } else {
      this.rvf_menu_button.setVisibility(paramInt);
    } 
    this.rvf_indicator_bar.setVisibility(paramInt);
    this.rvf_gallery.setVisibility(paramInt);
    this.rvf_shutter_button.setVisibility(paramInt);
    rvf_zoomMain.setVisibility(paramInt);
  }
  
  private void setSmartPanel() {
    String[] arrayOfString = (String[])this.deviceController.getDialModeMenuItems().toArray((Object[])new String[this.deviceController.getDialModeMenuItems().size()]);
    setWheelView(this.rvf_smart_panel_mode, 5, this.deviceController.getDialModeMenuItems().indexOf(this.deviceController.getDialModeValue()), arrayOfString, 84);
    this.rvf_smart_panel_shutterSpeed.measure(0, 0);
    arrayOfString = (String[])this.deviceController.getShutterSpeedMenuItems().toArray((Object[])new String[this.deviceController.getShutterSpeedMenuItems().size()]);
    setWheelView(this.rvf_smart_panel_shutterSpeed, 3, this.deviceController.getShutterSpeedMenuItems().indexOf(this.deviceController.getShutterSpeedValue()), arrayOfString, this.rvf_smart_panel_shutterSpeed.getMeasuredWidth() / 3);
    arrayOfString = (String[])this.deviceController.getApertureMenuItems().toArray((Object[])new String[this.deviceController.getApertureMenuItems().size()]);
    setWheelView(this.rvf_smart_panel_aperture, 5, this.deviceController.getApertureMenuItems().indexOf(this.deviceController.getApertureValue()), arrayOfString, this.rvf_smart_panel_shutterSpeed.getMeasuredWidth() / 5);
    arrayOfString = (String[])this.deviceController.getEVMenuItems().toArray((Object[])new String[this.deviceController.getEVMenuItems().size()]);
    setWheelView(this.rvf_smart_panel_ev, 5, this.deviceController.getEVMenuItems().indexOf(this.deviceController.getEVValue()), arrayOfString, this.rvf_smart_panel_shutterSpeed.getMeasuredWidth() / 5);
    arrayOfString = (String[])this.deviceController.getISOMenuItems().toArray((Object[])new String[this.deviceController.getISOMenuItems().size()]);
    setWheelView(this.rvf_smart_panel_iso, 3, this.deviceController.getISOMenuItems().indexOf(this.deviceController.getISOValue()), arrayOfString, this.rvf_smart_panel_shutterSpeed.getMeasuredWidth() / 3);
  }
  
  private void setSmartPanelVisibility(int paramInt) {
    this.rvf_smart_panel.setVisibility(paramInt);
    if (paramInt == 0) {
      setOSDVisibility(8);
    } else {
      setOSDVisibility(0);
    } 
    this.smartPanelVisibility = paramInt;
  }
  
  private void setTimer(int paramInt) {
    long l = 0L;
    Message message = new Message();
    message.what = paramInt;
    this.msgHandler.removeMessages(paramInt);
    switch (paramInt) {
      default:
        this.msgHandler.sendMessageDelayed(message, l);
        return;
      case 18:
        break;
    } 
    l = 1000L;
  }
  
  private void setWheelView(WheelView paramWheelView, int paramInt1, int paramInt2, String[] paramArrayOfString, int paramInt3) {
    ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter((Context)this, (Object[])paramArrayOfString);
    arrayWheelAdapter.setTextViewWidth(paramInt3);
    paramWheelView.setVisibleItems(paramInt1);
    paramWheelView.setViewAdapter((WheelViewAdapter)arrayWheelAdapter);
    paramWheelView.setCurrentItem(paramInt2);
    String str = this.deviceController.getDialModeValue();
    switch (paramWheelView.getId()) {
      default:
        paramWheelView.invalidateWheel(true);
        return;
      case 2131558695:
        paramWheelView.setCurrentItem(this.deviceController.getDialModeMenuItems().indexOf(str));
        paramWheelView.setEnabled(true);
        ((ArrayWheelAdapter)paramWheelView.getViewAdapter()).setTextColor(-1);
      case 2131558696:
        paramWheelView.setCurrentItem(this.deviceController.getShutterSpeedMenuItems().indexOf(this.deviceController.getShutterSpeedValue()));
        if (str.equals("Auto") || str.equals("P") || str.equals("A")) {
          paramWheelView.setEnabled(false);
          ((ArrayWheelAdapter)paramWheelView.getViewAdapter()).setTextColor(-7829368);
        } else if (str.equals("S") || str.equals("M")) {
          paramWheelView.setEnabled(true);
          ((ArrayWheelAdapter)paramWheelView.getViewAdapter()).setTextColor(-1);
        } 
      case 2131558697:
        paramWheelView.setCurrentItem(this.deviceController.getApertureMenuItems().indexOf(this.deviceController.getApertureValue()));
        if (str.equals("Auto") || str.equals("P") || str.equals("S")) {
          paramWheelView.setEnabled(false);
          ((ArrayWheelAdapter)paramWheelView.getViewAdapter()).setTextColor(-7829368);
        } else if (str.equals("A") || str.equals("M")) {
          paramWheelView.setEnabled(true);
          ((ArrayWheelAdapter)paramWheelView.getViewAdapter()).setTextColor(-1);
        } 
      case 2131558698:
        paramWheelView.setCurrentItem(this.deviceController.getEVMenuItems().indexOf(this.deviceController.getEVValue()));
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
    paramWheelView.setCurrentItem(this.deviceController.getISOMenuItems().indexOf(this.deviceController.getISOValue()));
    if (str.equals("Auto")) {
      paramWheelView.setEnabled(false);
      ((ArrayWheelAdapter)paramWheelView.getViewAdapter()).setTextColor(-7829368);
    } 
    if (str.equals("P") || str.equals("A") || str.equals("S") || str.equals("M")) {
      paramWheelView.setEnabled(true);
      ((ArrayWheelAdapter)paramWheelView.getViewAdapter()).setTextColor(-1);
    } 
  }
  
  private void showAF1X1(boolean paramBoolean) {
    if (paramBoolean) {
      rvf_af3x3_11.setImageResource(2130837573);
    } else {
      rvf_af3x3_11.setImageResource(2130837574);
    } 
    rvf_af3x3_11.setVisibility(0);
  }
  
  private void showAF3X3(boolean paramBoolean) {
    if (paramBoolean) {
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
      return;
    } 
    rvf_af3x3_11.setImageResource(2130837574);
    rvf_af3x3_11.setVisibility(0);
  }
  
  private void showAF3X5(boolean paramBoolean) {
    if (paramBoolean) {
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
      return;
    } 
    rvf_af3x5_12.setImageResource(2130837574);
    rvf_af3x5_12.setVisibility(0);
  }
  
  private void showAF3X7(boolean paramBoolean) {
    if (paramBoolean) {
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
      return;
    } 
    rvf_af3x7_13.setImageResource(2130837574);
    rvf_af3x7_13.setVisibility(0);
  }
  
  private void showAFExtend(boolean paramBoolean) {
    if (paramBoolean) {
      this.rvf_af_extend_leftup.setBackgroundResource(2130837562);
      this.rvf_af_extend_leftdown.setBackgroundResource(2130837561);
      this.rvf_af_extend_rightup.setBackgroundResource(2130837564);
      this.rvf_af_extend_rightdown.setBackgroundResource(2130837563);
    } else {
      this.rvf_af_extend_leftup.setBackgroundResource(2130837566);
      this.rvf_af_extend_leftdown.setBackgroundResource(2130837565);
      this.rvf_af_extend_rightup.setBackgroundResource(2130837568);
      this.rvf_af_extend_rightdown.setBackgroundResource(2130837567);
    } 
    rvf_af_extend.setVisibility(0);
  }
  
  private void showAFFrame(int paramInt, boolean paramBoolean) {
    if (paramInt == 2) {
      showAFExtend(paramBoolean);
      return;
    } 
    String str = this.deviceController.getMultiAFMatrixSizeValue();
    if (FunctionManager.getAFFrameMetricsType(str, this.ssid) == 0) {
      showAF1X1(paramBoolean);
      return;
    } 
    if (FunctionManager.getAFFrameMetricsType(str, this.ssid) == 2) {
      showAF3X5(paramBoolean);
      return;
    } 
    if (FunctionManager.getAFFrameMetricsType(str, this.ssid) == 3) {
      showAF3X7(paramBoolean);
      return;
    } 
    showAF3X3(paramBoolean);
  }
  
  private void showWheelMenu(int paramInt1, String paramString, int paramInt2, int paramInt3, ArrayList<String> paramArrayList, int paramInt4) {
    this.wheelMenuID = paramInt1;
    this.wheelMenuItems = paramArrayList;
    this.wheelMenuDialog = new CustomDialogWheelMenu((Context)this, String.valueOf(paramString) + " : ", paramInt2, paramInt3, paramArrayList.<String>toArray(new String[paramArrayList.size()]), paramInt4, this, this);
    this.wheelMenuDialog.setCanceledOnTouchOutside(true);
    this.wheelMenuDialog.show();
    this.wheelMenuDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
          public void onDismiss(DialogInterface param1DialogInterface) {
            RemoteViewFinder.this.wheelMenuID = 0;
          }
        });
  }
  
  private void startCodec() {
    String str = this.deviceController.getCurrentStreamUrl();
    if (str.contains(".mp4") || str.contains(".MP4")) {
      str = "ffplay -livestreaming -pktqperiodicflush -pictqinitialemptysec 4 -demuxprobesize 512 -an -sync ext -framedrop -infbuf ".concat("-f mp4 ");
    } else {
      str = "ffplay -livestreaming -pktqperiodicflush -pictqinitialemptysec 4 -demuxprobesize 512 -an -sync ext -framedrop -infbuf ".concat("-f avi ");
    } 
    FFmpegJNI.request(FFmpegJNI.Command.VIDEO_STREAMING_START, str.concat(this.deviceController.getCurrentStreamUrl()));
  }
  
  private void stopCodec() {
    FFmpegJNI.request(FFmpegJNI.Command.VIDEO_STREAMING_QUIT, "ffplay quit");
  }
  
  public void closeService() {
    stopCodec();
    this.exToast.unregisterAll();
    CMService.getInstance().beforefinish(0);
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    switch (paramInt1) {
    
    } 
  }
  
  public void onAlive() {}
  
  public void onBackPressed() {
    Trace.d(Trace.Tag.RVF, "start onBackPressed()");
    if (this.rvf_smart_panel.getVisibility() == 0) {
      setSmartPanelVisibility(8);
      return;
    } 
    super.onBackPressed();
  }
  
  public void onClick(View paramView) {
    Intent intent;
    Trace.d(Trace.Tag.RVF, "start onClick()");
    switch (paramView.getId()) {
      default:
        return;
      case 2131558403:
        if (this.smartPanelVisibility == 0) {
          setSmartPanelVisibility(8);
          return;
        } 
      case 2131558481:
        showWheelMenu(32, "Mode", 5, this.deviceController.getDialModeMenuItems().indexOf(this.deviceController.getDialModeValue()), this.deviceController.getDialModeMenuItems(), this.display.getWidth() / 5);
        return;
      case 2131558492:
        intent = new Intent((Context)this, LocalGallery.class);
        intent.putExtra("extra_description_res_id", 2131362053);
        intent.putExtra("extra_back_button_string_res_id", 2131361805);
        startActivityForResult(intent, 0);
        return;
      case 2131558462:
        break;
    } 
    setSmartPanelVisibility(0);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    Trace.d(Trace.Tag.RVF, "start onConfigurationChanged()");
    setContentView();
    super.onConfigurationChanged(paramConfiguration);
  }
  
  protected void onCreate(Bundle paramBundle) {
    Trace.d(Trace.Tag.RVF, "start onCreate()");
    super.onCreate(paramBundle);
    init();
    initCodec();
    this.deviceController.setHandler(this.deviceControlHandler);
    if (this.controller.isConnected())
      doAction(1, Utils.getLocation(this.locationManager)); 
  }
  
  protected Dialog onCreateDialog(int paramInt, Bundle paramBundle) {
    return super.onCreateDialog(paramInt, paramBundle);
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu) {
    paramMenu.addSubMenu("goto ML");
    return super.onCreateOptionsMenu(paramMenu);
  }
  
  protected void onDestroy() {
    Trace.d(Trace.Tag.RVF, "start onDestroy()");
    sendBroadcast(new Intent("action_disconnect"));
    super.onDestroy();
  }
  
  public void onItemClicked(WheelView paramWheelView, int paramInt) {}
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
    if (paramMenuItem.getTitle().equals("goto ML")) {
      stopCodec();
      doAction(43, "changeToML");
    } 
    return super.onOptionsItemSelected(paramMenuItem);
  }
  
  public void onScrollingFinished(WheelView paramWheelView) {}
  
  public void onScrollingStarted(WheelView paramWheelView) {}
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
    int i;
    int j;
    int k;
    int m;
    Trace.d(Trace.Tag.RVF, "start onTouch()");
    switch (paramView.getId()) {
      default:
      
      case 2131558403:
        switch (paramMotionEvent.getAction()) {
        
        } 
      case 2131558482:
        switch (paramMotionEvent.getAction()) {
          default:
            return false;
          case 0:
            this.nShutterDnUp = 1;
            if (this.deviceController.getDefaultFocusState().equals("af")) {
              if (FunctionManager.getAFMode(this.ssid) == 0)
                doAction(9, ""); 
              if (FunctionManager.getAFMode(this.ssid) == 1)
                doAction(10, ""); 
            } 
          case 1:
            break;
        } 
        i = (int)paramMotionEvent.getX();
        j = (int)paramMotionEvent.getY();
        k = this.rvf_shutter_button.getWidth();
        m = this.rvf_shutter_button.getHeight();
        if (i < 0 || j < 0 || i > k || j > m) {
          this.nShutterDnUp = 3;
          doAction(13, "");
        } 
        this.nShutterDnUp = 2;
      case 2131558479:
        switch (paramMotionEvent.getAction()) {
        
        } 
      case 2131558478:
        break;
    } 
    switch (paramMotionEvent.getAction()) {
    
    } 
  }
  
  public void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3) {
    FFmpegJNI.request(FFmpegJNI.Command.SURFACE_CHANGED, paramSurfaceHolder);
  }
  
  public void surfaceCreated(SurfaceHolder paramSurfaceHolder) {
    FFmpegJNI.request(FFmpegJNI.Command.SURFACE_CREATED, paramSurfaceHolder);
  }
  
  public void surfaceDestroyed(SurfaceHolder paramSurfaceHolder) {
    FFmpegJNI.request(FFmpegJNI.Command.SURFACE_DESTROYED, paramSurfaceHolder);
  }
  
  private class ImageDownloadTask extends AsyncTask<String, Void, Bitmap> {
    private ImageDownloadTask() {}
    
    protected Bitmap doInBackground(String... param1VarArgs) {
      String str;
      if (FunctionManager.isSupportedDownloadBYOriginalImageURL(RemoteViewFinder.this.deviceController.getVersion())) {
        str = param1VarArgs[0];
        str = RemoteViewFinder.this.imageDownloader.syncDownloadImage(str);
        Bitmap bitmap = Graph.getThumbnail(str);
        String str1 = Utils.getMimeType(str);
        MediaScannerConnection.scanFile(RemoteViewFinder.this.getApplicationContext(), new String[] { str }, new String[] { str1 }, null);
        return bitmap;
      } 
      String.valueOf(str[0]) + "_SM";
      return null;
    }
    
    protected void onPostExecute(Bitmap param1Bitmap) {
      RemoteViewFinder.this.rvf_thumbnail.setImageBitmap(param1Bitmap);
      (new RemoteViewFinder.StartCodecTask(null)).execute((Object[])new Void[0]);
      super.onPostExecute(param1Bitmap);
    }
    
    protected void onPreExecute() {
      super.onPreExecute();
    }
  }
  
  private class StartCodecTask extends AsyncTask<Void, Void, Boolean> {
    private StartCodecTask() {}
    
    protected Boolean doInBackground(Void... param1VarArgs) {
      RemoteViewFinder.this.startCodec();
      return null;
    }
    
    protected void onPreExecute() {
      Trace.d(Trace.Tag.RVF, "start StartCodecTask.onPreExecute()");
      super.onPreExecute();
    }
  }
  
  private class StopCodecTask extends AsyncTask<Void, Void, Boolean> {
    protected Boolean doInBackground(Void... param1VarArgs) {
      RemoteViewFinder.this.stopCodec();
      return null;
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\RemoteViewFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */