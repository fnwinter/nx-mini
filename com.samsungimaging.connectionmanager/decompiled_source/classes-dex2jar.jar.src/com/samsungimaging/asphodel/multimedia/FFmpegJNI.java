package com.samsungimaging.asphodel.multimedia;

import android.graphics.Bitmap;
import android.view.SurfaceHolder;
import com.samsungimaging.connectionmanager.util.Trace;

public class FFmpegJNI {
  public static final String ARG_VIDEO_STREAMING_FLUSH = "ffplay flush";
  
  public static final String ARG_VIDEO_STREAMING_QUIT = "ffplay quit";
  
  public static final String ARG_VIDEO_STREAMING_TOGGLE_PAUSE = "ffplay toggle_pause";
  
  private static final String DELIMITER = " ";
  
  private static final int FFMPEG_ARGB = 27;
  
  private static final int FFMPEG_RGB565LE = 44;
  
  public static final int RESOLUTION_QVGA = 0;
  
  public static final int RESOLUTION_VGA = 1;
  
  public static final int SCREEN_TYPE_PHONE_LAND_CAMERA_LAND = 2;
  
  public static final int SCREEN_TYPE_PHONE_LAND_CAMERA_PORT = 3;
  
  public static final int SCREEN_TYPE_PHONE_PORT_CAMERA_LAND = 0;
  
  public static final int SCREEN_TYPE_PHONE_PORT_CAMERA_PORT = 1;
  
  private static final String TAG = "[FFmpegJNI]";
  
  public static boolean mConnectResponse;
  
  private static int m_Resolution;
  
  private static int m_argc;
  
  private static String[] m_argv;
  
  private static int m_currentFrameIdx;
  
  private static Thread m_ffmpegMainThread;
  
  private static FFmpegPicture[] m_ffmpegPicture;
  
  private static FFmpegScreen m_ffmpegScreen;
  
  private static int m_frameCount;
  
  private static int m_pictureHeigtht;
  
  private static int m_pictureQueueLen;
  
  private static int m_pictureWidth;
  
  private static boolean m_run = false;
  
  static {
    m_argc = 0;
    m_argv = null;
    m_ffmpegMainThread = null;
    m_ffmpegScreen = null;
    m_frameCount = 0;
    m_currentFrameIdx = 0;
    m_pictureQueueLen = 0;
    m_pictureWidth = 0;
    m_pictureHeigtht = 0;
    m_ffmpegPicture = null;
    m_Resolution = 1;
    mConnectResponse = false;
  }
  
  public static int construct(Bitmap paramBitmap1, Bitmap paramBitmap2, int[][] paramArrayOfint1, int[][] paramArrayOfint2, int paramInt1, int paramInt2) {
    int i = CPUFeaturesJNI.construct();
    if (i == 0) {
      Trace.i(Trace.Tag.FFMPEG, "Load libffplay-jni.so");
      System.loadLibrary("ffplay-jni");
      m_ffmpegScreen = new FFmpegScreen(paramBitmap1, paramBitmap2, paramArrayOfint1, paramArrayOfint2, paramInt1, paramInt2);
      m_run = true;
      return i;
    } 
    Trace.i(Trace.Tag.FFMPEG, "Load libffplay-neon-jni.so");
    System.loadLibrary("ffplay-neon-jni");
    m_ffmpegScreen = new FFmpegScreen(paramBitmap1, paramBitmap2, paramArrayOfint1, paramArrayOfint2, paramInt1, paramInt2);
    m_run = true;
    return i;
  }
  
  public static int destruct() {
    m_run = false;
    m_frameCount = 0;
    return 0;
  }
  
  private static native int ffplayMain(int paramInt, String[] paramArrayOfString);
  
  private static native int ffplaySDL(int paramInt, String[] paramArrayOfString);
  
  public static Bitmap getCurrentFrame() {
    return onGetPicture(m_currentFrameIdx);
  }
  
  private static long getFreeHeapSize() {
    Trace.d(Trace.Tag.FFMPEG, "maxMemory : " + Runtime.getRuntime().maxMemory() + " totalMemory : " + Runtime.getRuntime().totalMemory() + " freeMemory : " + Runtime.getRuntime().freeMemory());
    return Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
  }
  
  private static int getQueueSize() {
    return 15;
  }
  
  public static int getResolution() {
    return m_Resolution;
  }
  
  private static int onDisplay(int paramInt) {
    m_currentFrameIdx = paramInt;
    m_ffmpegScreen.drawScreen(onGetPicture(paramInt));
    m_frameCount++;
    return 0;
  }
  
  public static void onFreezeDisplay() {
    m_ffmpegScreen.drawScreen(onGetPicture(m_currentFrameIdx));
  }
  
  private static Bitmap onGetPicture(int paramInt) {
    // Byte code:
    //   0: ldc com/samsungimaging/asphodel/multimedia/FFmpegJNI
    //   2: monitorenter
    //   3: iload_0
    //   4: getstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI.m_pictureQueueLen : I
    //   7: if_icmpge -> 24
    //   10: getstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI.m_ffmpegPicture : [Lcom/samsungimaging/asphodel/multimedia/FFmpegPicture;
    //   13: iload_0
    //   14: aaload
    //   15: invokevirtual getPicture : ()Landroid/graphics/Bitmap;
    //   18: astore_1
    //   19: ldc com/samsungimaging/asphodel/multimedia/FFmpegJNI
    //   21: monitorexit
    //   22: aload_1
    //   23: areturn
    //   24: aconst_null
    //   25: astore_1
    //   26: goto -> 19
    //   29: astore_1
    //   30: ldc com/samsungimaging/asphodel/multimedia/FFmpegJNI
    //   32: monitorexit
    //   33: aload_1
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   3	19	29	finally
  }
  
  public static int onHttpConnectRequest(int paramInt1, int paramInt2) {
    return 0;
  }
  
  public static int onHttpConnetResponse(int paramInt1, int paramInt2) {
    mConnectResponse = true;
    return 0;
  }
  
  private static int onInitPictureQueue(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    Bitmap.Config config;
    Trace.i(Trace.Tag.FFMPEG, "picture queue len : " + paramInt1);
    Trace.i(Trace.Tag.FFMPEG, "picture width : " + paramInt2);
    Trace.i(Trace.Tag.FFMPEG, "picture height : " + paramInt3);
    Trace.i(Trace.Tag.FFMPEG, "picture type : " + paramInt4);
    m_pictureQueueLen = paramInt1;
    m_ffmpegScreen.m_pictureQueueLen = paramInt1;
    m_pictureWidth = paramInt2;
    m_pictureHeigtht = paramInt3;
    if (paramInt4 == 27) {
      Trace.i(Trace.Tag.FFMPEG, "RGB Type : ARGB8888");
      config = Bitmap.Config.ARGB_8888;
    } else if (paramInt4 == 44) {
      Trace.i(Trace.Tag.FFMPEG, "RGB Type : RGB565");
      config = Bitmap.Config.RGB_565;
    } else {
      Trace.e(Trace.Tag.FFMPEG, "Unknown RGB Type!");
      return -1;
    } 
    m_ffmpegPicture = new FFmpegPicture[m_pictureQueueLen];
    for (paramInt1 = 0;; paramInt1++) {
      if (paramInt1 >= m_pictureQueueLen)
        return 0; 
      m_ffmpegPicture[paramInt1] = new FFmpegPicture(m_pictureWidth, m_pictureHeigtht, config);
    } 
  }
  
  public static int request(Command paramCommand, int paramInt1, int paramInt2, int[][] paramArrayOfint1, int[][] paramArrayOfint2) {
    if (!m_run) {
      Trace.e(Trace.Tag.FFMPEG, "You should call construct() function first!");
      return -1;
    } 
    switch (paramCommand) {
      default:
        Trace.e(Trace.Tag.FFMPEG, "Unknown Command!");
        return -1;
      case SCREEN_CONFIG_CHANGE:
        Trace.i(Trace.Tag.FFMPEG, "Command : " + paramCommand.toString());
        m_ffmpegScreen.changeScreenConfig(paramInt1, paramInt2, paramArrayOfint1, paramArrayOfint2);
        return 0;
      case FRAME_COUNT_GET:
        break;
    } 
    return m_frameCount;
  }
  
  public static int request(Command paramCommand, SurfaceHolder paramSurfaceHolder) {
    if (!m_run) {
      Trace.e(Trace.Tag.FFMPEG, "You should call construct() function first!");
      return -1;
    } 
    switch (paramCommand) {
      default:
        Trace.e(Trace.Tag.FFMPEG, "Unknown Command!");
        return -1;
      case SURFACE_CREATED:
      case SURFACE_CHANGED:
      case SURFACE_DESTROYED:
        break;
    } 
    Trace.i(Trace.Tag.FFMPEG, "Command : " + paramCommand.toString());
    m_ffmpegScreen.configureSurface(paramCommand, paramSurfaceHolder);
    return 0;
  }
  
  public static int request(Command paramCommand, String paramString) {
    // Byte code:
    //   0: getstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI.m_run : Z
    //   3: ifne -> 17
    //   6: getstatic com/samsungimaging/connectionmanager/util/Trace$Tag.FFMPEG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   9: ldc_w 'You should call construct() function first!'
    //   12: invokestatic e : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   15: iconst_m1
    //   16: ireturn
    //   17: aload_1
    //   18: ldc ' '
    //   20: invokevirtual split : (Ljava/lang/String;)[Ljava/lang/String;
    //   23: putstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI.m_argv : [Ljava/lang/String;
    //   26: getstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI.m_argv : [Ljava/lang/String;
    //   29: arraylength
    //   30: putstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI.m_argc : I
    //   33: iconst_0
    //   34: istore_2
    //   35: iload_2
    //   36: getstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI.m_argc : I
    //   39: if_icmplt -> 143
    //   42: getstatic com/samsungimaging/connectionmanager/util/Trace$Tag.FFMPEG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   45: new java/lang/StringBuilder
    //   48: dup
    //   49: ldc_w 'm_argc : '
    //   52: invokespecial <init> : (Ljava/lang/String;)V
    //   55: getstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI.m_argc : I
    //   58: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   61: invokevirtual toString : ()Ljava/lang/String;
    //   64: invokestatic i : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   67: getstatic com/samsungimaging/connectionmanager/util/Trace$Tag.FFMPEG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   70: new java/lang/StringBuilder
    //   73: dup
    //   74: ldc_w 'Command : '
    //   77: invokespecial <init> : (Ljava/lang/String;)V
    //   80: aload_0
    //   81: invokevirtual toString : ()Ljava/lang/String;
    //   84: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   87: invokevirtual toString : ()Ljava/lang/String;
    //   90: invokestatic i : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   93: invokestatic $SWITCH_TABLE$com$samsungimaging$asphodel$multimedia$FFmpegJNI$Command : ()[I
    //   96: aload_0
    //   97: invokevirtual ordinal : ()I
    //   100: iaload
    //   101: tableswitch default -> 132, 1 -> 187, 2 -> 229, 3 -> 229, 4 -> 229
    //   132: getstatic com/samsungimaging/connectionmanager/util/Trace$Tag.FFMPEG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   135: ldc_w 'Unknown Command!'
    //   138: invokestatic e : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   141: iconst_m1
    //   142: ireturn
    //   143: getstatic com/samsungimaging/connectionmanager/util/Trace$Tag.FFMPEG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   146: new java/lang/StringBuilder
    //   149: dup
    //   150: ldc_w 'm_argv['
    //   153: invokespecial <init> : (Ljava/lang/String;)V
    //   156: iload_2
    //   157: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   160: ldc_w '] : '
    //   163: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   166: getstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI.m_argv : [Ljava/lang/String;
    //   169: iload_2
    //   170: aaload
    //   171: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   174: invokevirtual toString : ()Ljava/lang/String;
    //   177: invokestatic i : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   180: iload_2
    //   181: iconst_1
    //   182: iadd
    //   183: istore_2
    //   184: goto -> 35
    //   187: getstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI.m_ffmpegMainThread : Ljava/lang/Thread;
    //   190: ifnonnull -> 218
    //   193: new java/lang/Thread
    //   196: dup
    //   197: new com/samsungimaging/asphodel/multimedia/FFmpegJNI$1
    //   200: dup
    //   201: invokespecial <init> : ()V
    //   204: invokespecial <init> : (Ljava/lang/Runnable;)V
    //   207: putstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI.m_ffmpegMainThread : Ljava/lang/Thread;
    //   210: getstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI.m_ffmpegMainThread : Ljava/lang/Thread;
    //   213: invokevirtual start : ()V
    //   216: iconst_0
    //   217: ireturn
    //   218: getstatic com/samsungimaging/connectionmanager/util/Trace$Tag.FFMPEG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   221: ldc_w 'Video Streaming already started!'
    //   224: invokestatic i : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   227: iconst_m1
    //   228: ireturn
    //   229: getstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI.m_argc : I
    //   232: getstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI.m_argv : [Ljava/lang/String;
    //   235: invokestatic ffplaySDL : (I[Ljava/lang/String;)I
    //   238: pop
    //   239: aload_0
    //   240: getstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI$Command.VIDEO_STREAMING_QUIT : Lcom/samsungimaging/asphodel/multimedia/FFmpegJNI$Command;
    //   243: if_acmpne -> 216
    //   246: iconst_0
    //   247: istore_2
    //   248: getstatic com/samsungimaging/connectionmanager/util/Trace$Tag.FFMPEG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   251: ldc_w 'wait for termination,...'
    //   254: invokestatic i : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   257: ldc2_w 100
    //   260: invokestatic sleep : (J)V
    //   263: getstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI.m_run : Z
    //   266: ifeq -> 216
    //   269: iload_2
    //   270: bipush #30
    //   272: if_icmpgt -> 216
    //   275: getstatic com/samsungimaging/asphodel/multimedia/FFmpegJNI.m_ffmpegMainThread : Ljava/lang/Thread;
    //   278: ifnull -> 216
    //   281: iload_2
    //   282: iconst_1
    //   283: iadd
    //   284: istore_2
    //   285: goto -> 248
    //   288: astore_0
    //   289: aload_0
    //   290: invokevirtual printStackTrace : ()V
    //   293: goto -> 263
    // Exception table:
    //   from	to	target	type
    //   257	263	288	java/lang/InterruptedException
  }
  
  public static void setResolution(int paramInt) {
    m_Resolution = paramInt;
  }
  
  public enum Command {
    FRAME_COUNT_GET, SCREEN_CONFIG_CHANGE, SURFACE_CHANGED, SURFACE_CREATED, SURFACE_DESTROYED, VIDEO_STREAMING_FLUSH, VIDEO_STREAMING_QUIT, VIDEO_STREAMING_START, VIDEO_STREAMING_TOGGLE_PAUSE;
    
    static {
      SURFACE_CREATED = new Command("SURFACE_CREATED", 4);
      SURFACE_CHANGED = new Command("SURFACE_CHANGED", 5);
      SURFACE_DESTROYED = new Command("SURFACE_DESTROYED", 6);
      SCREEN_CONFIG_CHANGE = new Command("SCREEN_CONFIG_CHANGE", 7);
      FRAME_COUNT_GET = new Command("FRAME_COUNT_GET", 8);
      ENUM$VALUES = new Command[] { VIDEO_STREAMING_START, VIDEO_STREAMING_TOGGLE_PAUSE, VIDEO_STREAMING_FLUSH, VIDEO_STREAMING_QUIT, SURFACE_CREATED, SURFACE_CHANGED, SURFACE_DESTROYED, SCREEN_CONFIG_CHANGE, FRAME_COUNT_GET };
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\asphodel\multimedia\FFmpegJNI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */