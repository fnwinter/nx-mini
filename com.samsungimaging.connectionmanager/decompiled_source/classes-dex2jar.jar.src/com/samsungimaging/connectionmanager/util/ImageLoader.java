package com.samsungimaging.connectionmanager.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.webkit.URLUtil;
import com.samsungimaging.connectionmanager.provider.DatabaseMedia;
import com.samsungimaging.connectionmanager.view.RecycleImageView;
import java.lang.ref.WeakReference;

public class ImageLoader {
  private static final int IO_BUFFER_SIZE = 8192;
  
  private static final int MESSAGE_CLEAR_CACHE = 1;
  
  private static final int MESSAGE_CLOSE_CACHE = 3;
  
  private static final int MESSAGE_FLUSH_CACHE = 2;
  
  private static final int MESSAGE_INIT_DISK_CACHE = 0;
  
  private static final int MESSAGE_REMOVE_CACHE = 4;
  
  private static final int SMALL_SIZE = 100;
  
  private static final Trace.Tag TAG = Trace.Tag.COMMON;
  
  private Context mContext = null;
  
  ImageCache mImageCache = null;
  
  ImageSize mImageSize = ImageSize.SMALL;
  
  private Bitmap mLoadingBitmap = null;
  
  private boolean mPauseLoad = false;
  
  private final Object mPauseLoadLock = new Object();
  
  public ImageLoader(Context paramContext, ImageSize paramImageSize, String paramString) {
    this.mContext = paramContext;
    this.mImageSize = paramImageSize;
    this.mImageCache = new ImageCache(paramContext, paramString);
    initDiskCache();
  }
  
  private Bitmap getBitmap(long paramLong, String paramString, int paramInt) {
    // Byte code:
    //   0: aconst_null
    //   1: astore #11
    //   3: aload #11
    //   5: astore #10
    //   7: new android/graphics/BitmapFactory$Options
    //   10: dup
    //   11: invokespecial <init> : ()V
    //   14: astore #12
    //   16: aload #11
    //   18: astore #10
    //   20: aload_0
    //   21: getfield mImageSize : Lcom/samsungimaging/connectionmanager/util/ImageLoader$ImageSize;
    //   24: getstatic com/samsungimaging/connectionmanager/util/ImageLoader$ImageSize.SMALL : Lcom/samsungimaging/connectionmanager/util/ImageLoader$ImageSize;
    //   27: if_acmpne -> 227
    //   30: iconst_0
    //   31: istore #8
    //   33: iconst_0
    //   34: istore #9
    //   36: iconst_2
    //   37: istore #7
    //   39: aload #11
    //   41: astore #10
    //   43: aload_0
    //   44: getfield mContext : Landroid/content/Context;
    //   47: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   50: lload_1
    //   51: iconst_1
    //   52: aconst_null
    //   53: invokestatic queryMiniThumbnail : (Landroid/content/ContentResolver;JI[Ljava/lang/String;)Landroid/database/Cursor;
    //   56: astore_3
    //   57: iload #9
    //   59: istore #5
    //   61: iload #8
    //   63: istore #6
    //   65: aload_3
    //   66: ifnull -> 503
    //   69: aload #11
    //   71: astore #10
    //   73: iload #9
    //   75: istore #5
    //   77: iload #8
    //   79: istore #6
    //   81: aload_3
    //   82: invokeinterface getCount : ()I
    //   87: ifle -> 503
    //   90: aload #11
    //   92: astore #10
    //   94: aload_3
    //   95: invokeinterface moveToFirst : ()Z
    //   100: pop
    //   101: aload #11
    //   103: astore #10
    //   105: aload_3
    //   106: aload_3
    //   107: ldc 'width'
    //   109: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   114: invokeinterface getInt : (I)I
    //   119: istore #6
    //   121: aload #11
    //   123: astore #10
    //   125: aload_3
    //   126: aload_3
    //   127: ldc 'height'
    //   129: invokeinterface getColumnIndex : (Ljava/lang/String;)I
    //   134: invokeinterface getInt : (I)I
    //   139: istore #5
    //   141: aload #11
    //   143: astore #10
    //   145: aload_3
    //   146: invokeinterface close : ()V
    //   151: goto -> 503
    //   154: aload #11
    //   156: astore #10
    //   158: iload #6
    //   160: i2f
    //   161: ldc 100.0
    //   163: fdiv
    //   164: invokestatic round : (F)I
    //   167: iload #5
    //   169: i2f
    //   170: ldc 100.0
    //   172: fdiv
    //   173: invokestatic round : (F)I
    //   176: invokestatic min : (II)I
    //   179: istore #7
    //   181: aload #11
    //   183: astore #10
    //   185: aload #12
    //   187: iload #7
    //   189: putfield inSampleSize : I
    //   192: aload #11
    //   194: astore #10
    //   196: aload_0
    //   197: getfield mContext : Landroid/content/Context;
    //   200: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   203: lload_1
    //   204: iconst_1
    //   205: aload #12
    //   207: invokestatic getThumbnail : (Landroid/content/ContentResolver;JILandroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   210: astore_3
    //   211: iload #4
    //   213: ifeq -> 520
    //   216: aload_3
    //   217: astore #10
    //   219: aload_0
    //   220: aload_3
    //   221: iload #4
    //   223: invokespecial rotateBitmap : (Landroid/graphics/Bitmap;I)Landroid/graphics/Bitmap;
    //   226: areturn
    //   227: iconst_1
    //   228: istore #5
    //   230: aload #11
    //   232: astore #10
    //   234: aload_0
    //   235: getfield mContext : Landroid/content/Context;
    //   238: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   241: invokevirtual getDisplayMetrics : ()Landroid/util/DisplayMetrics;
    //   244: astore #13
    //   246: aload #11
    //   248: astore #10
    //   250: aload #12
    //   252: iconst_1
    //   253: putfield inJustDecodeBounds : Z
    //   256: aload #11
    //   258: astore #10
    //   260: aload_3
    //   261: aload #12
    //   263: invokestatic decodeFile : (Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   266: pop
    //   267: aload #11
    //   269: astore #10
    //   271: aload #12
    //   273: getfield outWidth : I
    //   276: aload #13
    //   278: getfield widthPixels : I
    //   281: if_icmpgt -> 301
    //   284: aload #11
    //   286: astore #10
    //   288: aload #12
    //   290: getfield outHeight : I
    //   293: aload #13
    //   295: getfield heightPixels : I
    //   298: if_icmple -> 391
    //   301: aload #11
    //   303: astore #10
    //   305: aload #12
    //   307: getfield outWidth : I
    //   310: aload #12
    //   312: getfield outHeight : I
    //   315: if_icmple -> 426
    //   318: aload #11
    //   320: astore #10
    //   322: aload #12
    //   324: getfield outWidth : I
    //   327: i2f
    //   328: aload #13
    //   330: getfield widthPixels : I
    //   333: aload #13
    //   335: getfield heightPixels : I
    //   338: invokestatic max : (II)I
    //   341: i2f
    //   342: fdiv
    //   343: invokestatic round : (F)I
    //   346: istore #6
    //   348: aload #11
    //   350: astore #10
    //   352: aload #12
    //   354: getfield outHeight : I
    //   357: i2f
    //   358: aload #13
    //   360: getfield widthPixels : I
    //   363: aload #13
    //   365: getfield heightPixels : I
    //   368: invokestatic min : (II)I
    //   371: i2f
    //   372: fdiv
    //   373: invokestatic round : (F)I
    //   376: istore #5
    //   378: aload #11
    //   380: astore #10
    //   382: iload #6
    //   384: iload #5
    //   386: invokestatic min : (II)I
    //   389: istore #5
    //   391: aload #11
    //   393: astore #10
    //   395: aload #12
    //   397: iload #5
    //   399: putfield inSampleSize : I
    //   402: aload #11
    //   404: astore #10
    //   406: aload #12
    //   408: iconst_0
    //   409: putfield inJustDecodeBounds : Z
    //   412: aload #11
    //   414: astore #10
    //   416: aload_3
    //   417: aload #12
    //   419: invokestatic decodeFile : (Ljava/lang/String;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   422: astore_3
    //   423: goto -> 211
    //   426: aload #11
    //   428: astore #10
    //   430: aload #12
    //   432: getfield outWidth : I
    //   435: i2f
    //   436: aload #13
    //   438: getfield widthPixels : I
    //   441: aload #13
    //   443: getfield heightPixels : I
    //   446: invokestatic min : (II)I
    //   449: i2f
    //   450: fdiv
    //   451: invokestatic round : (F)I
    //   454: istore #6
    //   456: aload #11
    //   458: astore #10
    //   460: aload #12
    //   462: getfield outHeight : I
    //   465: i2f
    //   466: aload #13
    //   468: getfield widthPixels : I
    //   471: aload #13
    //   473: getfield heightPixels : I
    //   476: invokestatic max : (II)I
    //   479: i2f
    //   480: fdiv
    //   481: invokestatic round : (F)I
    //   484: istore #5
    //   486: goto -> 378
    //   489: astore_3
    //   490: getstatic com/samsungimaging/connectionmanager/util/ImageLoader.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   493: aload_3
    //   494: invokevirtual getMessage : ()Ljava/lang/String;
    //   497: invokestatic e : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   500: aload #10
    //   502: areturn
    //   503: iload #6
    //   505: bipush #100
    //   507: if_icmpgt -> 154
    //   510: iload #5
    //   512: bipush #100
    //   514: if_icmple -> 181
    //   517: goto -> 154
    //   520: aload_3
    //   521: areturn
    // Exception table:
    //   from	to	target	type
    //   7	16	489	java/lang/Exception
    //   20	30	489	java/lang/Exception
    //   43	57	489	java/lang/Exception
    //   81	90	489	java/lang/Exception
    //   94	101	489	java/lang/Exception
    //   105	121	489	java/lang/Exception
    //   125	141	489	java/lang/Exception
    //   145	151	489	java/lang/Exception
    //   158	181	489	java/lang/Exception
    //   185	192	489	java/lang/Exception
    //   196	211	489	java/lang/Exception
    //   219	227	489	java/lang/Exception
    //   234	246	489	java/lang/Exception
    //   250	256	489	java/lang/Exception
    //   260	267	489	java/lang/Exception
    //   271	284	489	java/lang/Exception
    //   288	301	489	java/lang/Exception
    //   305	318	489	java/lang/Exception
    //   322	348	489	java/lang/Exception
    //   352	378	489	java/lang/Exception
    //   382	391	489	java/lang/Exception
    //   395	402	489	java/lang/Exception
    //   406	412	489	java/lang/Exception
    //   416	423	489	java/lang/Exception
    //   430	456	489	java/lang/Exception
    //   460	486	489	java/lang/Exception
  }
  
  private Bitmap getBitmapFromStream(String paramString) {
    // Byte code:
    //   0: aconst_null
    //   1: astore_2
    //   2: aconst_null
    //   3: astore #4
    //   5: aconst_null
    //   6: astore #5
    //   8: aconst_null
    //   9: astore #7
    //   11: aconst_null
    //   12: astore #6
    //   14: aload #5
    //   16: astore_3
    //   17: new java/net/URL
    //   20: dup
    //   21: aload_1
    //   22: invokespecial <init> : (Ljava/lang/String;)V
    //   25: invokevirtual openConnection : ()Ljava/net/URLConnection;
    //   28: checkcast java/net/HttpURLConnection
    //   31: astore_1
    //   32: aload_1
    //   33: astore #4
    //   35: aload #5
    //   37: astore_3
    //   38: aload_1
    //   39: astore_2
    //   40: new java/io/BufferedInputStream
    //   43: dup
    //   44: aload_1
    //   45: invokevirtual getInputStream : ()Ljava/io/InputStream;
    //   48: sipush #8192
    //   51: invokespecial <init> : (Ljava/io/InputStream;I)V
    //   54: astore #5
    //   56: aload #5
    //   58: invokestatic decodeStream : (Ljava/io/InputStream;)Landroid/graphics/Bitmap;
    //   61: astore_2
    //   62: aload_1
    //   63: ifnull -> 70
    //   66: aload_1
    //   67: invokevirtual disconnect : ()V
    //   70: aload #5
    //   72: ifnull -> 198
    //   75: aload #5
    //   77: invokevirtual close : ()V
    //   80: aload_2
    //   81: areturn
    //   82: astore #5
    //   84: aload #4
    //   86: astore_1
    //   87: aload #7
    //   89: astore #4
    //   91: aload #4
    //   93: astore_3
    //   94: aload_1
    //   95: astore_2
    //   96: getstatic com/samsungimaging/connectionmanager/util/ImageLoader.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   99: new java/lang/StringBuilder
    //   102: dup
    //   103: ldc 'Error in getBitmapFromStream() - '
    //   105: invokespecial <init> : (Ljava/lang/String;)V
    //   108: aload #5
    //   110: invokevirtual getMessage : ()Ljava/lang/String;
    //   113: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   116: invokevirtual toString : ()Ljava/lang/String;
    //   119: invokestatic e : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   122: aload_1
    //   123: ifnull -> 130
    //   126: aload_1
    //   127: invokevirtual disconnect : ()V
    //   130: aload #6
    //   132: astore_2
    //   133: aload #4
    //   135: ifnull -> 80
    //   138: aload #4
    //   140: invokevirtual close : ()V
    //   143: aconst_null
    //   144: areturn
    //   145: astore_1
    //   146: aconst_null
    //   147: areturn
    //   148: astore_1
    //   149: aload_2
    //   150: ifnull -> 157
    //   153: aload_2
    //   154: invokevirtual disconnect : ()V
    //   157: aload_3
    //   158: ifnull -> 165
    //   161: aload_3
    //   162: invokevirtual close : ()V
    //   165: aload_1
    //   166: athrow
    //   167: astore_1
    //   168: aload_2
    //   169: areturn
    //   170: astore_2
    //   171: goto -> 165
    //   174: astore #4
    //   176: aload #5
    //   178: astore_3
    //   179: aload_1
    //   180: astore_2
    //   181: aload #4
    //   183: astore_1
    //   184: goto -> 149
    //   187: astore_2
    //   188: aload #5
    //   190: astore #4
    //   192: aload_2
    //   193: astore #5
    //   195: goto -> 91
    //   198: aload_2
    //   199: areturn
    // Exception table:
    //   from	to	target	type
    //   17	32	82	java/io/IOException
    //   17	32	148	finally
    //   40	56	82	java/io/IOException
    //   40	56	148	finally
    //   56	62	187	java/io/IOException
    //   56	62	174	finally
    //   75	80	167	java/io/IOException
    //   96	122	148	finally
    //   138	143	145	java/io/IOException
    //   161	165	170	java/io/IOException
  }
  
  private static BitmapLoaderTask getBitmapLoaderTask(RecycleImageView paramRecycleImageView) {
    if (paramRecycleImageView != null) {
      Drawable drawable = paramRecycleImageView.getDrawable();
      if (drawable instanceof AsyncBitmapDrawable)
        return ((AsyncBitmapDrawable)drawable).getBitmapLoaderTask(); 
    } 
    return null;
  }
  
  private Bitmap rotateBitmap(Bitmap paramBitmap, int paramInt) {
    Matrix matrix = new Matrix();
    matrix.preRotate(paramInt, 0.5F, 0.5F);
    Bitmap bitmap = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), matrix, false);
    paramBitmap.recycle();
    return bitmap;
  }
  
  public void cancelLoader(RecycleImageView paramRecycleImageView) {
    BitmapLoaderTask bitmapLoaderTask = getBitmapLoaderTask(paramRecycleImageView);
    if (bitmapLoaderTask != null)
      bitmapLoaderTask.cancel(true); 
  }
  
  public void clearCache() {
    (new CacheAsyncTask(null)).execute(new Object[] { Integer.valueOf(1) });
  }
  
  public void clearMemoryCache() {
    if (this.mImageCache != null)
      this.mImageCache.clearMemoryCache(); 
  }
  
  public void closeCache() {
    (new CacheAsyncTask(null)).execute(new Object[] { Integer.valueOf(3) });
  }
  
  public void flushCache() {
    (new CacheAsyncTask(null)).execute(new Object[] { Integer.valueOf(2) });
  }
  
  public RecycleBitmapDrawable getBitmapDrawableFromMemoryCache(String paramString) {
    return (this.mImageCache != null) ? this.mImageCache.getBitmapDrawableFromMemoryCache(paramString) : null;
  }
  
  public boolean hasSnapshotInDiskCache(String paramString) {
    return (this.mImageCache != null) ? this.mImageCache.hasSnapshotInDiskCache(paramString) : false;
  }
  
  public void initDiskCache() {
    (new CacheAsyncTask(null)).execute(new Object[] { Integer.valueOf(0) });
  }
  
  public void loadImage(DatabaseMedia paramDatabaseMedia, RecycleImageView paramRecycleImageView) {
    if (paramDatabaseMedia != null) {
      String str;
      if (this.mImageSize == ImageSize.SMALL) {
        str = paramDatabaseMedia.getThumbnailPath();
      } else {
        str = paramDatabaseMedia.getViewerPath();
      } 
      RecycleBitmapDrawable recycleBitmapDrawable = getBitmapDrawableFromMemoryCache(str);
      if (recycleBitmapDrawable != null) {
        if (recycleBitmapDrawable != paramRecycleImageView.getDrawable()) {
          paramRecycleImageView.setImageDrawable((Drawable)recycleBitmapDrawable);
          return;
        } 
        return;
      } 
      BitmapLoaderTask bitmapLoaderTask2 = getBitmapLoaderTask(paramRecycleImageView);
      if (bitmapLoaderTask2 != null) {
        String str1;
        recycleBitmapDrawable = null;
        if (bitmapLoaderTask2.mMedia != null)
          if (this.mImageSize == ImageSize.SMALL) {
            str1 = bitmapLoaderTask2.mMedia.getThumbnailPath();
          } else {
            str1 = bitmapLoaderTask2.mMedia.getViewerPath();
          }  
        if (str1 == null || !str1.equals(str)) {
          bitmapLoaderTask2.cancel(true);
        } else {
          return;
        } 
      } 
      BitmapLoaderTask bitmapLoaderTask1 = new BitmapLoaderTask(paramRecycleImageView);
      paramRecycleImageView.setImageDrawable((Drawable)new AsyncBitmapDrawable(this.mLoadingBitmap, bitmapLoaderTask1));
      bitmapLoaderTask1.executeOnExecutor(AsyncTask.DUAL_THREAD_EXECUTOR, new DatabaseMedia[] { paramDatabaseMedia });
      return;
    } 
  }
  
  public RecycleBitmapDrawable loadImageSync(DatabaseMedia paramDatabaseMedia) {
    RecycleBitmapDrawable recycleBitmapDrawable;
    String str;
    byte b = 1;
    Bitmap bitmap3 = null;
    Bitmap bitmap4 = null;
    boolean bool = false;
    if (this.mImageSize == ImageSize.SMALL) {
      str = paramDatabaseMedia.getThumbnailPath();
    } else {
      str = paramDatabaseMedia.getViewerPath();
    } 
    if (this.mImageCache != null)
      bitmap3 = this.mImageCache.getBitmapFromDiskCache(str); 
    Bitmap bitmap2 = bitmap3;
    if (bitmap3 == null)
      if (URLUtil.isHttpUrl(str)) {
        bitmap2 = getBitmapFromStream(str);
      } else if (paramDatabaseMedia.getMediaType() == 1) {
        bitmap2 = getBitmap(paramDatabaseMedia.getID(), str, paramDatabaseMedia.getOrientation());
      } else {
        if (this.mImageSize != ImageSize.SMALL)
          b = 2; 
        bitmap2 = ThumbnailUtils.createVideoThumbnail(str, b);
      }  
    Bitmap bitmap1 = bitmap2;
    if (bitmap2 == null) {
      bitmap1 = BitmapFactory.decodeResource(this.mContext.getResources(), 2130838227);
      bool = true;
    } 
    bitmap2 = bitmap4;
    if (bitmap1 != null) {
      bitmap1.setDensity(160);
      recycleBitmapDrawable = new RecycleBitmapDrawable(bitmap1);
      recycleBitmapDrawable.setNoneImage(bool);
      RecycleBitmapDrawable recycleBitmapDrawable1 = recycleBitmapDrawable;
      if (this.mImageCache != null) {
        if (bool) {
          this.mImageCache.addBitmapToMemoryCache(str, recycleBitmapDrawable);
          return recycleBitmapDrawable;
        } 
      } else {
        return recycleBitmapDrawable1;
      } 
    } else {
      return (RecycleBitmapDrawable)bitmap2;
    } 
    this.mImageCache.addBitmapToCache(str, recycleBitmapDrawable);
    return recycleBitmapDrawable;
  }
  
  public void removeCache(String paramString) {
    (new CacheAsyncTask(null)).execute(new Object[] { Integer.valueOf(4), paramString });
  }
  
  public void setLoadingImage(int paramInt) {
    this.mLoadingBitmap = BitmapFactory.decodeResource(this.mContext.getResources(), paramInt);
  }
  
  public void setLoadingImage(Bitmap paramBitmap) {
    this.mLoadingBitmap = paramBitmap;
  }
  
  public void setPauseLoad(boolean paramBoolean) {
    synchronized (this.mPauseLoadLock) {
      this.mPauseLoad = paramBoolean;
      if (!this.mPauseLoad)
        this.mPauseLoadLock.notifyAll(); 
      return;
    } 
  }
  
  private static class AsyncBitmapDrawable extends BitmapDrawable {
    private final WeakReference<ImageLoader.BitmapLoaderTask> mBitmapLoaderTask;
    
    public AsyncBitmapDrawable(Bitmap param1Bitmap, ImageLoader.BitmapLoaderTask param1BitmapLoaderTask) {
      super(param1Bitmap);
      this.mBitmapLoaderTask = new WeakReference<ImageLoader.BitmapLoaderTask>(param1BitmapLoaderTask);
    }
    
    public ImageLoader.BitmapLoaderTask getBitmapLoaderTask() {
      return this.mBitmapLoaderTask.get();
    }
  }
  
  private class BitmapLoaderTask extends AsyncTask<DatabaseMedia, Void, RecycleBitmapDrawable> {
    private final WeakReference<RecycleImageView> mImageView;
    
    private DatabaseMedia mMedia;
    
    public BitmapLoaderTask(RecycleImageView param1RecycleImageView) {
      this.mImageView = new WeakReference<RecycleImageView>(param1RecycleImageView);
    }
    
    private RecycleImageView getAttachedImageView() {
      RecycleImageView recycleImageView = this.mImageView.get();
      return (this == ImageLoader.getBitmapLoaderTask(recycleImageView)) ? recycleImageView : null;
    }
    
    protected RecycleBitmapDrawable doInBackground(DatabaseMedia... param1VarArgs) {
      byte b = 1;
      synchronized (ImageLoader.this.mPauseLoadLock) {
        while (true) {
          if (!ImageLoader.this.mPauseLoad || isCancelled()) {
            String str;
            this.mMedia = param1VarArgs[0];
            param1VarArgs = null;
            Object object1 = null;
            boolean bool2 = false;
            if (ImageLoader.this.mImageSize == ImageLoader.ImageSize.SMALL) {
              str = this.mMedia.getThumbnailPath();
            } else {
              str = this.mMedia.getViewerPath();
            } 
            null = param1VarArgs;
            if (ImageLoader.this.mImageCache != null) {
              null = param1VarArgs;
              if (!isCancelled()) {
                null = param1VarArgs;
                if (getAttachedImageView() != null)
                  null = ImageLoader.this.mImageCache.getBitmapFromDiskCache(str); 
              } 
            } 
            Object object = null;
            if (null == null) {
              object = null;
              if (!isCancelled()) {
                object = null;
                if (getAttachedImageView() != null)
                  if (URLUtil.isHttpUrl(str)) {
                    object = ImageLoader.this.getBitmapFromStream(str);
                  } else if (this.mMedia.getMediaType() == 1) {
                    object = ImageLoader.this.getBitmap(this.mMedia.getID(), str, this.mMedia.getOrientation());
                  } else {
                    if (ImageLoader.this.mImageSize != ImageLoader.ImageSize.SMALL)
                      b = 2; 
                    object = ThumbnailUtils.createVideoThumbnail(str, b);
                  }  
              } 
            } 
            null = object;
            boolean bool1 = bool2;
            if (object == null) {
              null = object;
              bool1 = bool2;
              if (!isCancelled()) {
                null = object;
                bool1 = bool2;
                if (getAttachedImageView() != null) {
                  null = BitmapFactory.decodeResource(ImageLoader.this.mContext.getResources(), 2130838227);
                  bool1 = true;
                } 
              } 
            } 
            object = object1;
            if (null != null) {
              null.setDensity(160);
              null = new RecycleBitmapDrawable((Bitmap)null);
              null.setNoneImage(bool1);
              object = null;
              if (ImageLoader.this.mImageCache != null) {
                if (bool1) {
                  ImageLoader.this.mImageCache.addBitmapToMemoryCache(str, (RecycleBitmapDrawable)null);
                  return (RecycleBitmapDrawable)null;
                } 
              } else {
                return (RecycleBitmapDrawable)object;
              } 
            } else {
              return (RecycleBitmapDrawable)object;
            } 
          } else {
            try {
              ImageLoader.this.mPauseLoadLock.wait();
            } catch (InterruptedException interruptedException) {}
            continue;
          } 
          ImageLoader.this.mImageCache.addBitmapToCache((String)interruptedException, (RecycleBitmapDrawable)null);
          return (RecycleBitmapDrawable)null;
        } 
      } 
    }
    
    protected void onCancelled() {
      super.onCancelled();
      synchronized (ImageLoader.this.mPauseLoadLock) {
        ImageLoader.this.mPauseLoadLock.notifyAll();
        return;
      } 
    }
    
    protected void onPostExecute(RecycleBitmapDrawable param1RecycleBitmapDrawable) {
      if (isCancelled())
        param1RecycleBitmapDrawable = null; 
      RecycleImageView recycleImageView = getAttachedImageView();
      if (param1RecycleBitmapDrawable != null && recycleImageView != null)
        recycleImageView.setImageDrawable((Drawable)param1RecycleBitmapDrawable); 
    }
  }
  
  private class CacheAsyncTask extends AsyncTask<Object, Void, Void> {
    private CacheAsyncTask() {}
    
    protected Void doInBackground(Object... param1VarArgs) {
      switch (((Integer)param1VarArgs[0]).intValue()) {
        default:
          return null;
        case 0:
          if (ImageLoader.this.mImageCache != null) {
            ImageLoader.this.mImageCache.initDiskCache();
            return null;
          } 
        case 1:
          if (ImageLoader.this.mImageCache != null) {
            ImageLoader.this.mImageCache.clearCache();
            return null;
          } 
        case 2:
          if (ImageLoader.this.mImageCache != null) {
            ImageLoader.this.mImageCache.flushCache();
            return null;
          } 
        case 3:
          if (ImageLoader.this.mImageCache != null) {
            ImageLoader.this.mImageCache.closeCache();
            ImageLoader.this.mImageCache = null;
            return null;
          } 
        case 4:
          break;
      } 
      if (ImageLoader.this.mImageCache != null) {
        ImageLoader.this.mImageCache.remove((String)param1VarArgs[1]);
        return null;
      } 
    }
  }
  
  public enum ImageSize {
    LARGE, SMALL;
    
    static {
      ENUM$VALUES = new ImageSize[] { SMALL, LARGE };
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanage\\util\ImageLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */