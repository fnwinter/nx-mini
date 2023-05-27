package com.samsungimaging.connectionmanager.app.pullservice.demo.ml;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.widget.ImageView;
import com.samsungimaging.connectionmanager.util.Trace;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Map;
import java.util.Stack;
import java.util.WeakHashMap;

public class ImageLoader {
  private static File mCacheDir = null;
  
  private Trace.Tag TAG = Trace.Tag.ML;
  
  ImageDownloadQueue imageDownloadQueue = new ImageDownloadQueue();
  
  ImageDownloader imageDownloader = new ImageDownloader();
  
  private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
  
  Activity mActivity;
  
  Context mContext;
  
  private final int mDefaultDrawable;
  
  private final int mProgressDrawable;
  
  BitmapFactory.Options opts = new BitmapFactory.Options();
  
  public ImageLoader(Context paramContext, Activity paramActivity, int paramInt1, int paramInt2, String paramString) {
    this.imageDownloader.setPriority(4);
    this.opts.inPreferredConfig = Bitmap.Config.RGB_565;
    this.mContext = paramContext;
    this.mActivity = paramActivity;
    this.mProgressDrawable = paramInt1;
    this.mDefaultDrawable = paramInt2;
    if (mCacheDir == null)
      mCacheDir = new File(paramString); 
    if (!mCacheDir.exists()) {
      Trace.d(this.TAG, "LazyList doesn't exist");
      mCacheDir.mkdirs();
      return;
    } 
    Trace.d(this.TAG, "LazyList exist");
    mCacheDir.delete();
    mCacheDir.mkdirs();
  }
  
  public ImageLoader(Context paramContext, Activity paramActivity, int paramInt, String paramString) {
    this(paramContext, paramActivity, -1, paramInt, paramString);
  }
  
  private void addQueueImage(String paramString, Activity paramActivity, ImageView paramImageView, Item paramItem) {
    this.imageDownloadQueue.Clean(paramImageView);
    null = new ImageItem(paramString, paramImageView, paramItem);
    synchronized (this.imageDownloadQueue.imageItems) {
      this.imageDownloadQueue.imageItems.push(null);
      this.imageDownloadQueue.imageItems.notifyAll();
      if (this.imageDownloader.getState() == Thread.State.NEW)
        this.imageDownloader.start(); 
      return;
    } 
  }
  
  private long copyStreamQF30(InputStream paramInputStream, OutputStream paramOutputStream) {
    long l1 = 0L;
    long l2 = l1;
    try {
      byte[] arrayOfByte = new byte[8192];
      while (true) {
        l2 = l1;
        int i = paramInputStream.read(arrayOfByte, 0, 8192);
        if (i == -1) {
          l2 = l1;
          paramOutputStream.flush();
          return l1;
        } 
        l2 = l1;
        paramOutputStream.write(arrayOfByte, 0, i);
        l1 += i;
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
      return l2;
    } 
  }
  
  private Bitmap decodeFile(File paramFile) {
    FileNotFoundException fileNotFoundException2 = null;
    if (paramFile.exists() && paramFile.isFile()) {
      try {
        Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(paramFile), null, null);
      } catch (FileNotFoundException fileNotFoundException) {
        fileNotFoundException.printStackTrace();
        fileNotFoundException = fileNotFoundException2;
      } 
      Trace.d(this.TAG, "bWidth : " + Character.MIN_VALUE + " bHeight : " + Character.MIN_VALUE);
      return (Bitmap)fileNotFoundException;
    } 
    Trace.d(this.TAG, "File Not Found");
    FileNotFoundException fileNotFoundException1 = fileNotFoundException2;
    Trace.d(this.TAG, "bWidth : " + Character.MIN_VALUE + " bHeight : " + Character.MIN_VALUE);
    return (Bitmap)fileNotFoundException1;
  }
  
  private Bitmap getBitmapQF30(String paramString) {
    Trace.d(this.TAG, "getBitmap start url=" + paramString);
    File file = new File(mCacheDir, String.valueOf(paramString.hashCode()));
    Bitmap bitmap = decodeFile(file);
    if (bitmap == null) {
      long l = -1L;
      while (true) {
        if (l != -1L)
          try {
            return decodeFile(file);
          } catch (Exception exception) {
            exception.printStackTrace();
            break;
          }  
        HumbleHttpClient humbleHttpClient = new HumbleHttpClient();
        InputStream inputStream = humbleHttpClient.request((String)exception);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        long l1 = copyStreamQF30(inputStream, fileOutputStream);
        l = l1;
        if (l1 != humbleHttpClient.contentLength)
          l = -1L; 
        fileOutputStream.close();
      } 
    } 
    return bitmap;
  }
  
  private Bitmap getImage(String paramString) {
    int i = paramString.hashCode();
    File file = new File(mCacheDir, String.valueOf(i));
    Bitmap bitmap = decodeFile(file);
    if (bitmap != null)
      return bitmap; 
    try {
      Trace.d(this.TAG, "Performance Check Point : start getBitmap url=" + paramString);
      URLConnection uRLConnection = (new URL(paramString)).openConnection();
      if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13)
        uRLConnection.setRequestProperty("Connection", "close"); 
      InputStream inputStream = (new URL(paramString)).openStream();
      Trace.d(this.TAG, "getBitmap start url=" + paramString);
      FileOutputStream fileOutputStream = new FileOutputStream(file);
      copyStream(inputStream, fileOutputStream);
      fileOutputStream.close();
      Bitmap bitmap1 = decodeFile(file);
      Trace.d(this.TAG, "Performance Check Point : end getBitmap url=" + paramString);
      return bitmap1;
    } catch (Exception exception) {
      exception.printStackTrace();
      return null;
    } 
  }
  
  private Bitmap getNewHeight(int paramInt1, int paramInt2, Bitmap paramBitmap) {
    return Bitmap.createBitmap(paramBitmap, 0, (paramInt1 - paramInt2) / 2, paramInt2, paramInt2);
  }
  
  private Bitmap getNewWidth(int paramInt1, int paramInt2, Bitmap paramBitmap) {
    return Bitmap.createBitmap(paramBitmap, (paramInt2 - paramInt1) / 2, 0, paramInt1, paramInt1);
  }
  
  private void saveBitmap(Bitmap paramBitmap, String paramString, Bitmap.CompressFormat paramCompressFormat) {
    // Byte code:
    //   0: aload_3
    //   1: getstatic android/graphics/Bitmap$CompressFormat.PNG : Landroid/graphics/Bitmap$CompressFormat;
    //   4: if_acmpeq -> 43
    //   7: aload_1
    //   8: invokestatic createBitmap : (Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;
    //   11: astore #4
    //   13: new android/graphics/Canvas
    //   16: dup
    //   17: aload_1
    //   18: invokespecial <init> : (Landroid/graphics/Bitmap;)V
    //   21: astore #5
    //   23: aload_1
    //   24: iconst_m1
    //   25: invokevirtual eraseColor : (I)V
    //   28: aload #5
    //   30: aload #4
    //   32: fconst_0
    //   33: fconst_0
    //   34: aconst_null
    //   35: invokevirtual drawBitmap : (Landroid/graphics/Bitmap;FFLandroid/graphics/Paint;)V
    //   38: aload #4
    //   40: invokevirtual recycle : ()V
    //   43: new java/io/File
    //   46: dup
    //   47: aload_2
    //   48: invokespecial <init> : (Ljava/lang/String;)V
    //   51: astore #6
    //   53: aconst_null
    //   54: astore #4
    //   56: aconst_null
    //   57: astore #5
    //   59: aload #4
    //   61: astore_2
    //   62: aload #6
    //   64: invokevirtual createNewFile : ()Z
    //   67: pop
    //   68: aload #4
    //   70: astore_2
    //   71: new java/io/FileOutputStream
    //   74: dup
    //   75: aload #6
    //   77: invokespecial <init> : (Ljava/io/File;)V
    //   80: astore #4
    //   82: aload_1
    //   83: aload_3
    //   84: bipush #100
    //   86: aload #4
    //   88: invokevirtual compress : (Landroid/graphics/Bitmap$CompressFormat;ILjava/io/OutputStream;)Z
    //   91: pop
    //   92: aload #4
    //   94: invokevirtual flush : ()V
    //   97: aload #4
    //   99: invokevirtual close : ()V
    //   102: return
    //   103: astore_3
    //   104: aload #5
    //   106: astore_1
    //   107: aload_1
    //   108: astore_2
    //   109: aload_3
    //   110: invokevirtual printStackTrace : ()V
    //   113: aload_1
    //   114: invokevirtual flush : ()V
    //   117: aload_1
    //   118: invokevirtual close : ()V
    //   121: return
    //   122: astore_1
    //   123: aload_1
    //   124: invokevirtual printStackTrace : ()V
    //   127: return
    //   128: astore_1
    //   129: aload_2
    //   130: invokevirtual flush : ()V
    //   133: aload_2
    //   134: invokevirtual close : ()V
    //   137: aload_1
    //   138: athrow
    //   139: astore_2
    //   140: aload_2
    //   141: invokevirtual printStackTrace : ()V
    //   144: goto -> 137
    //   147: astore_1
    //   148: aload_1
    //   149: invokevirtual printStackTrace : ()V
    //   152: return
    //   153: astore_1
    //   154: aload #4
    //   156: astore_2
    //   157: goto -> 129
    //   160: astore_3
    //   161: aload #4
    //   163: astore_1
    //   164: goto -> 107
    // Exception table:
    //   from	to	target	type
    //   62	68	103	java/lang/Exception
    //   62	68	128	finally
    //   71	82	103	java/lang/Exception
    //   71	82	128	finally
    //   82	92	160	java/lang/Exception
    //   82	92	153	finally
    //   92	102	147	java/io/IOException
    //   109	113	128	finally
    //   113	121	122	java/io/IOException
    //   129	137	139	java/io/IOException
  }
  
  public void clearCache() {
    File[] arrayOfFile = mCacheDir.listFiles();
    if (arrayOfFile != null && arrayOfFile.length > 0) {
      int j = arrayOfFile.length;
      int i = 0;
      while (true) {
        if (i < j) {
          arrayOfFile[i].delete();
          i++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public void copyStream(InputStream paramInputStream, OutputStream paramOutputStream) {
    try {
      byte[] arrayOfByte = new byte[8192];
      while (true) {
        int i = paramInputStream.read(arrayOfByte, 0, 8192);
        if (i == -1) {
          paramOutputStream.flush();
          return;
        } 
        paramOutputStream.write(arrayOfByte, 0, i);
      } 
    } catch (Exception exception) {
      exception.printStackTrace();
      return;
    } 
  }
  
  public void setImage(String paramString, Activity paramActivity, ImageView paramImageView, Item paramItem) {
    Trace.d(this.TAG, "start displayImage() url=" + paramString);
    if (!paramString.equals(paramImageView.getTag())) {
      int i;
      Resources resources = this.mActivity.getResources();
      if (this.mProgressDrawable != -1) {
        i = this.mProgressDrawable;
      } else {
        i = this.mDefaultDrawable;
      } 
      BitmapDisplayer bitmapDisplayer = new BitmapDisplayer(BitmapFactory.decodeResource(resources, i), paramImageView);
      this.mActivity.runOnUiThread(bitmapDisplayer);
      paramImageView.setTag(null);
      this.imageViews.put(paramImageView, paramString);
      addQueueImage(paramString, paramActivity, paramImageView, paramItem);
    } 
  }
  
  public void stopThread() {
    this.imageDownloader.interrupt();
  }
  
  class BitmapDisplayer implements Runnable {
    Bitmap bitmap;
    
    ImageView imageView;
    
    public BitmapDisplayer(Bitmap param1Bitmap, ImageView param1ImageView) {
      this.bitmap = param1Bitmap;
      this.imageView = param1ImageView;
    }
    
    public void run() {
      if (this.bitmap != null) {
        this.imageView.setImageBitmap(this.bitmap);
        return;
      } 
      this.imageView.setImageBitmap(null);
    }
  }
  
  class ImageDownloadQueue {
    private Stack<ImageLoader.ImageItem> imageItems = new Stack<ImageLoader.ImageItem>();
    
    public void Clean(ImageView param1ImageView) {
      for (int i = 0;; i++) {
        if (i >= this.imageItems.size())
          return; 
        if (((ImageLoader.ImageItem)this.imageItems.get(i)).imageView == param1ImageView) {
          this.imageItems.remove(i);
        } else {
          i++;
        } 
      } 
    }
  }
  
  class ImageDownloader extends Thread {
    public void run() {
      // Byte code:
      //   0: aload_0
      //   1: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   4: getfield imageDownloadQueue : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader$ImageDownloadQueue;
      //   7: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader$ImageDownloadQueue;)Ljava/util/Stack;
      //   10: invokevirtual size : ()I
      //   13: ifne -> 47
      //   16: aload_0
      //   17: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   20: getfield imageDownloadQueue : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader$ImageDownloadQueue;
      //   23: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader$ImageDownloadQueue;)Ljava/util/Stack;
      //   26: astore #4
      //   28: aload #4
      //   30: monitorenter
      //   31: aload_0
      //   32: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   35: getfield imageDownloadQueue : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader$ImageDownloadQueue;
      //   38: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader$ImageDownloadQueue;)Ljava/util/Stack;
      //   41: invokevirtual wait : ()V
      //   44: aload #4
      //   46: monitorexit
      //   47: aload_0
      //   48: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   51: getfield imageDownloadQueue : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader$ImageDownloadQueue;
      //   54: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader$ImageDownloadQueue;)Ljava/util/Stack;
      //   57: invokevirtual size : ()I
      //   60: ifeq -> 415
      //   63: aload_0
      //   64: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   67: getfield imageDownloadQueue : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader$ImageDownloadQueue;
      //   70: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader$ImageDownloadQueue;)Ljava/util/Stack;
      //   73: astore #4
      //   75: aload #4
      //   77: monitorenter
      //   78: aload_0
      //   79: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   82: getfield imageDownloadQueue : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader$ImageDownloadQueue;
      //   85: invokestatic access$0 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader$ImageDownloadQueue;)Ljava/util/Stack;
      //   88: iconst_0
      //   89: invokevirtual remove : (I)Ljava/lang/Object;
      //   92: checkcast com/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader$ImageItem
      //   95: astore #6
      //   97: aload #4
      //   99: monitorexit
      //   100: aconst_null
      //   101: astore #5
      //   103: iconst_0
      //   104: istore_2
      //   105: aload #6
      //   107: getfield url : Ljava/lang/String;
      //   110: invokevirtual hashCode : ()I
      //   113: istore_1
      //   114: new java/io/File
      //   117: dup
      //   118: invokestatic access$0 : ()Ljava/io/File;
      //   121: iload_1
      //   122: invokestatic valueOf : (I)Ljava/lang/String;
      //   125: invokespecial <init> : (Ljava/io/File;Ljava/lang/String;)V
      //   128: astore #4
      //   130: aload_0
      //   131: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   134: invokestatic access$1 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   137: new java/lang/StringBuilder
      //   140: dup
      //   141: ldc 'file.exists() : '
      //   143: invokespecial <init> : (Ljava/lang/String;)V
      //   146: aload #4
      //   148: invokevirtual exists : ()Z
      //   151: invokevirtual append : (Z)Ljava/lang/StringBuilder;
      //   154: invokevirtual toString : ()Ljava/lang/String;
      //   157: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   160: aload #4
      //   162: invokevirtual exists : ()Z
      //   165: ifeq -> 179
      //   168: aload_0
      //   169: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   172: aload #4
      //   174: invokestatic access$2 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;Ljava/io/File;)Landroid/graphics/Bitmap;
      //   177: astore #5
      //   179: aload_0
      //   180: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   183: invokestatic access$1 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   186: astore #4
      //   188: new java/lang/StringBuilder
      //   191: dup
      //   192: ldc 'bmp is null : '
      //   194: invokespecial <init> : (Ljava/lang/String;)V
      //   197: astore #7
      //   199: aload #5
      //   201: ifnonnull -> 556
      //   204: iconst_1
      //   205: istore_3
      //   206: aload #4
      //   208: aload #7
      //   210: iload_3
      //   211: invokevirtual append : (Z)Ljava/lang/StringBuilder;
      //   214: invokevirtual toString : ()Ljava/lang/String;
      //   217: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   220: aload #5
      //   222: astore #4
      //   224: aload #5
      //   226: ifnonnull -> 284
      //   229: iload_2
      //   230: istore_1
      //   231: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/common/CMInfo;
      //   234: invokevirtual getConnectedSSID : ()Ljava/lang/String;
      //   237: ifnull -> 458
      //   240: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/common/CMInfo;
      //   243: invokevirtual getConnectedSSID : ()Ljava/lang/String;
      //   246: ldc 'QF30'
      //   248: invokevirtual contains : (Ljava/lang/CharSequence;)Z
      //   251: ifne -> 270
      //   254: iload_2
      //   255: istore_1
      //   256: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/common/CMInfo;
      //   259: invokevirtual getConnectedSSID : ()Ljava/lang/String;
      //   262: ldc 'EX2'
      //   264: invokevirtual contains : (Ljava/lang/CharSequence;)Z
      //   267: ifeq -> 458
      //   270: aload_0
      //   271: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   274: aload #6
      //   276: getfield url : Ljava/lang/String;
      //   279: invokestatic access$3 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;Ljava/lang/String;)Landroid/graphics/Bitmap;
      //   282: astore #4
      //   284: aload_0
      //   285: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   288: invokestatic access$1 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   291: astore #5
      //   293: new java/lang/StringBuilder
      //   296: dup
      //   297: ldc 'bmp is null : '
      //   299: invokespecial <init> : (Ljava/lang/String;)V
      //   302: astore #7
      //   304: aload #4
      //   306: ifnonnull -> 561
      //   309: iconst_1
      //   310: istore_3
      //   311: aload #5
      //   313: aload #7
      //   315: iload_3
      //   316: invokevirtual append : (Z)Ljava/lang/StringBuilder;
      //   319: invokevirtual toString : ()Ljava/lang/String;
      //   322: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   325: aload #4
      //   327: ifnull -> 499
      //   330: aload_0
      //   331: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   334: invokestatic access$5 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;)Ljava/util/Map;
      //   337: aload #6
      //   339: getfield imageView : Landroid/widget/ImageView;
      //   342: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
      //   347: checkcast java/lang/String
      //   350: astore #5
      //   352: aload #5
      //   354: ifnull -> 415
      //   357: aload #5
      //   359: aload #6
      //   361: getfield url : Ljava/lang/String;
      //   364: invokevirtual equals : (Ljava/lang/Object;)Z
      //   367: ifeq -> 415
      //   370: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader$BitmapDisplayer
      //   373: dup
      //   374: aload_0
      //   375: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   378: aload #4
      //   380: aload #6
      //   382: getfield imageView : Landroid/widget/ImageView;
      //   385: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;Landroid/graphics/Bitmap;Landroid/widget/ImageView;)V
      //   388: astore #4
      //   390: aload_0
      //   391: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   394: getfield mActivity : Landroid/app/Activity;
      //   397: aload #4
      //   399: invokevirtual runOnUiThread : (Ljava/lang/Runnable;)V
      //   402: aload #6
      //   404: getfield imageView : Landroid/widget/ImageView;
      //   407: aload #6
      //   409: getfield url : Ljava/lang/String;
      //   412: invokevirtual setTag : (Ljava/lang/Object;)V
      //   415: invokestatic interrupted : ()Z
      //   418: ifeq -> 0
      //   421: aload_0
      //   422: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   425: invokestatic access$1 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;)Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
      //   428: ldc 'Interrupted : While Retrieving Thumbnails.'
      //   430: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
      //   433: return
      //   434: astore #5
      //   436: aload #4
      //   438: monitorexit
      //   439: aload #5
      //   441: athrow
      //   442: astore #4
      //   444: aload #4
      //   446: invokevirtual printStackTrace : ()V
      //   449: return
      //   450: astore #5
      //   452: aload #4
      //   454: monitorexit
      //   455: aload #5
      //   457: athrow
      //   458: aload_0
      //   459: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   462: aload #6
      //   464: getfield url : Ljava/lang/String;
      //   467: invokestatic access$4 : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;Ljava/lang/String;)Landroid/graphics/Bitmap;
      //   470: astore #5
      //   472: iload_1
      //   473: iconst_1
      //   474: iadd
      //   475: istore_2
      //   476: aload #5
      //   478: astore #4
      //   480: aload #5
      //   482: ifnonnull -> 284
      //   485: iload_2
      //   486: istore_1
      //   487: iload_2
      //   488: iconst_3
      //   489: if_icmplt -> 458
      //   492: aload #5
      //   494: astore #4
      //   496: goto -> 284
      //   499: aload #6
      //   501: getfield item : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/Item;
      //   504: iconst_0
      //   505: invokevirtual setSupported : (Z)V
      //   508: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader$BitmapDisplayer
      //   511: dup
      //   512: aload_0
      //   513: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   516: aload_0
      //   517: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   520: getfield mContext : Landroid/content/Context;
      //   523: invokevirtual getResources : ()Landroid/content/res/Resources;
      //   526: ldc 2130838227
      //   528: invokestatic decodeResource : (Landroid/content/res/Resources;I)Landroid/graphics/Bitmap;
      //   531: aload #6
      //   533: getfield imageView : Landroid/widget/ImageView;
      //   536: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;Landroid/graphics/Bitmap;Landroid/widget/ImageView;)V
      //   539: astore #4
      //   541: aload_0
      //   542: getfield this$0 : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
      //   545: getfield mActivity : Landroid/app/Activity;
      //   548: aload #4
      //   550: invokevirtual runOnUiThread : (Ljava/lang/Runnable;)V
      //   553: goto -> 415
      //   556: iconst_0
      //   557: istore_3
      //   558: goto -> 206
      //   561: iconst_0
      //   562: istore_3
      //   563: goto -> 311
      // Exception table:
      //   from	to	target	type
      //   0	31	442	java/lang/InterruptedException
      //   31	47	434	finally
      //   47	78	442	java/lang/InterruptedException
      //   78	100	450	finally
      //   105	160	442	java/lang/InterruptedException
      //   160	179	442	java/lang/InterruptedException
      //   179	199	442	java/lang/InterruptedException
      //   206	220	442	java/lang/InterruptedException
      //   231	254	442	java/lang/InterruptedException
      //   256	270	442	java/lang/InterruptedException
      //   270	284	442	java/lang/InterruptedException
      //   284	304	442	java/lang/InterruptedException
      //   311	325	442	java/lang/InterruptedException
      //   330	352	442	java/lang/InterruptedException
      //   357	415	442	java/lang/InterruptedException
      //   415	433	442	java/lang/InterruptedException
      //   436	439	434	finally
      //   439	442	442	java/lang/InterruptedException
      //   452	455	450	finally
      //   455	458	442	java/lang/InterruptedException
      //   458	472	442	java/lang/InterruptedException
      //   499	553	442	java/lang/InterruptedException
    }
  }
  
  private class ImageItem {
    public ImageView imageView;
    
    public Item item;
    
    public String url;
    
    public ImageItem(String param1String, ImageView param1ImageView, Item param1Item) {
      this.url = param1String;
      this.imageView = param1ImageView;
      this.item = param1Item;
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\ml\ImageLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */