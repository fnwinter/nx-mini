package com.samsungimaging.asphodel.multimedia;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.SurfaceHolder;
import com.samsungimaging.connectionmanager.util.Trace;

class FFmpegScreen {
  private static final int SCREEN_TYPE_NUM = 4;
  
  private static final String TAG = "[FFmpegScreen]";
  
  private static SurfaceHolder m_surfaceHolder = null;
  
  private Bitmap[] m_background = null;
  
  private Canvas m_canvas = null;
  
  private int m_canvasClearCount = 0;
  
  private int m_curCameraRotate = 0;
  
  private int m_curScreenType = 0;
  
  private Matrix m_matrix = null;
  
  public int m_pictureQueueLen = 0;
  
  private int[][] m_screenPositoin = null;
  
  private int[][] m_screenSize = null;
  
  public FFmpegScreen(Bitmap paramBitmap1, Bitmap paramBitmap2, int[][] paramArrayOfint1, int[][] paramArrayOfint2, int paramInt1, int paramInt2) {
    this.m_matrix = new Matrix();
    this.m_background = new Bitmap[2];
    this.m_background[0] = paramBitmap1;
    this.m_background[1] = paramBitmap2;
    this.m_screenPositoin = new int[4][];
    this.m_screenSize = new int[4][];
    changeScreenConfig(paramInt1, paramInt2, paramArrayOfint1, paramArrayOfint2);
  }
  
  private void delay(String paramString, int paramInt) {
    int i = paramInt;
    if (paramInt < 0)
      i = 2000; 
    Trace.d(Trace.Tag.FFMPEG, "-=> delay1 " + paramString);
    long l = i;
    try {
      Thread.sleep(l);
    } catch (Exception exception) {}
    Trace.d(Trace.Tag.FFMPEG, "-=> delay2 " + paramString);
  }
  
  public int changeScreenConfig(int paramInt1, int paramInt2, int[][] paramArrayOfint1, int[][] paramArrayOfint2) {
    this.m_canvasClearCount = 0;
    this.m_curScreenType = paramInt1;
    this.m_curCameraRotate = paramInt2;
    for (paramInt1 = 0;; paramInt1++) {
      if (paramInt1 >= 4)
        return 0; 
      this.m_screenPositoin[paramInt1] = new int[2];
      if (paramArrayOfint1 != null) {
        this.m_screenPositoin[paramInt1][0] = paramArrayOfint1[paramInt1][0];
        this.m_screenPositoin[paramInt1][1] = paramArrayOfint1[paramInt1][1];
      } 
      this.m_screenSize[paramInt1] = new int[2];
      if (paramArrayOfint2 != null) {
        this.m_screenSize[paramInt1][0] = paramArrayOfint2[paramInt1][0];
        this.m_screenSize[paramInt1][1] = paramArrayOfint2[paramInt1][1];
      } 
    } 
  }
  
  public void configureSurface(FFmpegJNI.Command paramCommand, SurfaceHolder paramSurfaceHolder) {
    switch (paramCommand) {
      default:
        Trace.e(Trace.Tag.FFMPEG, "Unknown Command!");
      case SURFACE_CREATED:
      case SURFACE_DESTROYED:
        return;
      case SURFACE_CHANGED:
        break;
    } 
    m_surfaceHolder = paramSurfaceHolder;
  }
  
  public int drawScreen(Bitmap paramBitmap) {
    // Byte code:
    //   0: iconst_0
    //   1: istore_3
    //   2: iconst_0
    //   3: istore_2
    //   4: invokestatic currentThread : ()Ljava/lang/Thread;
    //   7: invokevirtual isInterrupted : ()Z
    //   10: ifne -> 411
    //   13: aload_0
    //   14: getfield m_curCameraRotate : I
    //   17: lookupswitch default -> 60, 0 -> 234, 90 -> 247, 180 -> 234, 270 -> 247
    //   60: aload_0
    //   61: getfield m_matrix : Landroid/graphics/Matrix;
    //   64: invokevirtual reset : ()V
    //   67: aload_0
    //   68: getfield m_curCameraRotate : I
    //   71: ifeq -> 101
    //   74: aload_0
    //   75: getfield m_matrix : Landroid/graphics/Matrix;
    //   78: aload_0
    //   79: getfield m_curCameraRotate : I
    //   82: i2f
    //   83: aload_1
    //   84: invokevirtual getWidth : ()I
    //   87: i2f
    //   88: fconst_2
    //   89: fdiv
    //   90: aload_1
    //   91: invokevirtual getHeight : ()I
    //   94: i2f
    //   95: fconst_2
    //   96: fdiv
    //   97: invokevirtual postRotate : (FFF)Z
    //   100: pop
    //   101: aload_0
    //   102: getfield m_matrix : Landroid/graphics/Matrix;
    //   105: aload_0
    //   106: getfield m_screenSize : [[I
    //   109: aload_0
    //   110: getfield m_curScreenType : I
    //   113: aaload
    //   114: iconst_0
    //   115: iaload
    //   116: i2f
    //   117: iload_3
    //   118: i2f
    //   119: fdiv
    //   120: aload_0
    //   121: getfield m_screenSize : [[I
    //   124: aload_0
    //   125: getfield m_curScreenType : I
    //   128: aaload
    //   129: iconst_1
    //   130: iaload
    //   131: i2f
    //   132: iload_2
    //   133: i2f
    //   134: fdiv
    //   135: invokevirtual postScale : (FF)Z
    //   138: pop
    //   139: invokestatic getResolution : ()I
    //   142: ifne -> 260
    //   145: aload_1
    //   146: iconst_0
    //   147: iconst_0
    //   148: aload_1
    //   149: invokevirtual getWidth : ()I
    //   152: iconst_2
    //   153: idiv
    //   154: aload_1
    //   155: invokevirtual getHeight : ()I
    //   158: iconst_2
    //   159: idiv
    //   160: invokestatic createBitmap : (Landroid/graphics/Bitmap;IIII)Landroid/graphics/Bitmap;
    //   163: astore_1
    //   164: aload_1
    //   165: aload_1
    //   166: invokevirtual getWidth : ()I
    //   169: iconst_2
    //   170: imul
    //   171: aload_1
    //   172: invokevirtual getHeight : ()I
    //   175: iconst_2
    //   176: imul
    //   177: iconst_1
    //   178: invokestatic createScaledBitmap : (Landroid/graphics/Bitmap;IIZ)Landroid/graphics/Bitmap;
    //   181: astore_1
    //   182: aload_1
    //   183: iconst_0
    //   184: iconst_0
    //   185: aload_1
    //   186: invokevirtual getWidth : ()I
    //   189: aload_1
    //   190: invokevirtual getHeight : ()I
    //   193: aload_0
    //   194: getfield m_matrix : Landroid/graphics/Matrix;
    //   197: iconst_1
    //   198: invokestatic createBitmap : (Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;
    //   201: astore_1
    //   202: getstatic com/samsungimaging/asphodel/multimedia/FFmpegScreen.m_surfaceHolder : Landroid/view/SurfaceHolder;
    //   205: astore #4
    //   207: aload #4
    //   209: monitorenter
    //   210: aload_0
    //   211: getstatic com/samsungimaging/asphodel/multimedia/FFmpegScreen.m_surfaceHolder : Landroid/view/SurfaceHolder;
    //   214: invokeinterface lockCanvas : ()Landroid/graphics/Canvas;
    //   219: putfield m_canvas : Landroid/graphics/Canvas;
    //   222: aload_0
    //   223: getfield m_canvas : Landroid/graphics/Canvas;
    //   226: ifnonnull -> 283
    //   229: aload #4
    //   231: monitorexit
    //   232: iconst_m1
    //   233: ireturn
    //   234: aload_1
    //   235: invokevirtual getWidth : ()I
    //   238: istore_3
    //   239: aload_1
    //   240: invokevirtual getHeight : ()I
    //   243: istore_2
    //   244: goto -> 60
    //   247: aload_1
    //   248: invokevirtual getHeight : ()I
    //   251: istore_3
    //   252: aload_1
    //   253: invokevirtual getWidth : ()I
    //   256: istore_2
    //   257: goto -> 60
    //   260: aload_1
    //   261: iconst_0
    //   262: iconst_0
    //   263: aload_1
    //   264: invokevirtual getWidth : ()I
    //   267: aload_1
    //   268: invokevirtual getHeight : ()I
    //   271: aload_0
    //   272: getfield m_matrix : Landroid/graphics/Matrix;
    //   275: iconst_1
    //   276: invokestatic createBitmap : (Landroid/graphics/Bitmap;IIIILandroid/graphics/Matrix;Z)Landroid/graphics/Bitmap;
    //   279: astore_1
    //   280: goto -> 202
    //   283: aload_0
    //   284: getfield m_canvasClearCount : I
    //   287: aload_0
    //   288: getfield m_pictureQueueLen : I
    //   291: if_icmpge -> 335
    //   294: aload_0
    //   295: aload_0
    //   296: getfield m_canvasClearCount : I
    //   299: iconst_1
    //   300: iadd
    //   301: putfield m_canvasClearCount : I
    //   304: aload_0
    //   305: getfield m_curScreenType : I
    //   308: ifeq -> 319
    //   311: aload_0
    //   312: getfield m_curScreenType : I
    //   315: iconst_1
    //   316: if_icmpne -> 386
    //   319: aload_0
    //   320: getfield m_canvas : Landroid/graphics/Canvas;
    //   323: aload_0
    //   324: getfield m_background : [Landroid/graphics/Bitmap;
    //   327: iconst_0
    //   328: aaload
    //   329: fconst_0
    //   330: fconst_0
    //   331: aconst_null
    //   332: invokevirtual drawBitmap : (Landroid/graphics/Bitmap;FFLandroid/graphics/Paint;)V
    //   335: aload_0
    //   336: getfield m_canvas : Landroid/graphics/Canvas;
    //   339: aload_1
    //   340: aload_0
    //   341: getfield m_screenPositoin : [[I
    //   344: aload_0
    //   345: getfield m_curScreenType : I
    //   348: aaload
    //   349: iconst_0
    //   350: iaload
    //   351: i2f
    //   352: aload_0
    //   353: getfield m_screenPositoin : [[I
    //   356: aload_0
    //   357: getfield m_curScreenType : I
    //   360: aaload
    //   361: iconst_1
    //   362: iaload
    //   363: i2f
    //   364: aconst_null
    //   365: invokevirtual drawBitmap : (Landroid/graphics/Bitmap;FFLandroid/graphics/Paint;)V
    //   368: getstatic com/samsungimaging/asphodel/multimedia/FFmpegScreen.m_surfaceHolder : Landroid/view/SurfaceHolder;
    //   371: aload_0
    //   372: getfield m_canvas : Landroid/graphics/Canvas;
    //   375: invokeinterface unlockCanvasAndPost : (Landroid/graphics/Canvas;)V
    //   380: aload #4
    //   382: monitorexit
    //   383: goto -> 411
    //   386: aload_0
    //   387: getfield m_canvas : Landroid/graphics/Canvas;
    //   390: aload_0
    //   391: getfield m_background : [Landroid/graphics/Bitmap;
    //   394: iconst_1
    //   395: aaload
    //   396: fconst_0
    //   397: fconst_0
    //   398: aconst_null
    //   399: invokevirtual drawBitmap : (Landroid/graphics/Bitmap;FFLandroid/graphics/Paint;)V
    //   402: goto -> 335
    //   405: astore_1
    //   406: aload #4
    //   408: monitorexit
    //   409: aload_1
    //   410: athrow
    //   411: iconst_0
    //   412: ireturn
    // Exception table:
    //   from	to	target	type
    //   210	232	405	finally
    //   283	319	405	finally
    //   319	335	405	finally
    //   335	383	405	finally
    //   386	402	405	finally
    //   406	409	405	finally
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\asphodel\multimedia\FFmpegScreen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */