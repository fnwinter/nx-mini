package com.samsungimaging.connectionmanager.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.v4.util.LruCache;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.NumberFormat;

public class ImageCache {
  private static final int DISK_CACHE_SIZE = 10485760;
  
  private Context mContext = null;
  
  private DiskLruCache mDiskCache;
  
  private String mDiskCacheDir = null;
  
  private final Object mDiskCacheLock = new Object();
  
  private boolean mDiskCacheStarting = true;
  
  private LruCache<String, RecycleBitmapDrawable> mMemoryCache;
  
  public ImageCache(Context paramContext, String paramString) {
    this.mContext = paramContext;
    this.mDiskCacheDir = paramString;
    int i = Math.round((float)Runtime.getRuntime().maxMemory() * 0.2F);
    Trace.d(Trace.Tag.COMMON, "[" + paramString + "] MemoryCacheSize : " + NumberFormat.getInstance().format(i) + " byte");
    this.mMemoryCache = new LruCache<String, RecycleBitmapDrawable>(i) {
        protected void entryRemoved(boolean param1Boolean, String param1String, RecycleBitmapDrawable param1RecycleBitmapDrawable1, RecycleBitmapDrawable param1RecycleBitmapDrawable2) {
          param1RecycleBitmapDrawable1.setCached(false);
        }
        
        protected int sizeOf(String param1String, RecycleBitmapDrawable param1RecycleBitmapDrawable) {
          Bitmap bitmap = param1RecycleBitmapDrawable.getBitmap();
          return bitmap.getRowBytes() * bitmap.getHeight();
        }
      };
  }
  
  public void addBitmapToCache(String paramString, RecycleBitmapDrawable paramRecycleBitmapDrawable) {
    if (paramString == null || paramRecycleBitmapDrawable == null)
      return; 
    addBitmapToMemoryCache(paramString, paramRecycleBitmapDrawable);
    synchronized (this.mDiskCacheLock) {
      if (this.mDiskCache != null) {
        String str2 = String.valueOf(paramString.hashCode());
        OutputStream outputStream3 = null;
        String str1 = null;
        OutputStream outputStream2 = null;
        OutputStream outputStream1 = outputStream3;
        paramString = str1;
        try {
          DiskLruCache.Snapshot snapshot = this.mDiskCache.get(str2);
          if (snapshot == null) {
            outputStream1 = outputStream3;
            paramString = str1;
            DiskLruCache.Editor editor = this.mDiskCache.edit(str2);
            if (editor != null) {
              outputStream1 = outputStream3;
              paramString = str1;
              outputStream2 = editor.newOutputStream(0);
              outputStream1 = outputStream2;
              OutputStream outputStream = outputStream2;
              paramRecycleBitmapDrawable.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, outputStream2);
              outputStream1 = outputStream2;
              outputStream = outputStream2;
              editor.commit();
              outputStream1 = outputStream2;
              outputStream = outputStream2;
              outputStream2.close();
            } 
          } else {
            outputStream1 = outputStream3;
            paramString = str1;
            snapshot.getInputStream(0).close();
          } 
          return;
        } catch (Exception exception) {
          OutputStream outputStream = outputStream1;
          Trace.e(Trace.Tag.COMMON, "addBitmapToCache() - " + exception.getMessage());
          return;
        } finally {
          if (iOException != null)
            try {
              iOException.close();
            } catch (IOException iOException1) {} 
        } 
      } 
      return;
    } 
  }
  
  public void addBitmapToMemoryCache(String paramString, RecycleBitmapDrawable paramRecycleBitmapDrawable) {
    if (paramString != null && paramRecycleBitmapDrawable != null && this.mMemoryCache != null) {
      paramRecycleBitmapDrawable.setCached(true);
      this.mMemoryCache.put(paramString, paramRecycleBitmapDrawable);
      return;
    } 
  }
  
  public void clearCache() {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual clearMemoryCache : ()V
    //   4: aload_0
    //   5: getfield mDiskCacheLock : Ljava/lang/Object;
    //   8: astore_2
    //   9: aload_2
    //   10: monitorenter
    //   11: aload_0
    //   12: iconst_1
    //   13: putfield mDiskCacheStarting : Z
    //   16: aload_0
    //   17: getfield mDiskCache : Lcom/samsungimaging/connectionmanager/util/DiskLruCache;
    //   20: ifnull -> 51
    //   23: aload_0
    //   24: getfield mDiskCache : Lcom/samsungimaging/connectionmanager/util/DiskLruCache;
    //   27: invokevirtual isClosed : ()Z
    //   30: istore_1
    //   31: iload_1
    //   32: ifne -> 51
    //   35: aload_0
    //   36: getfield mDiskCache : Lcom/samsungimaging/connectionmanager/util/DiskLruCache;
    //   39: invokevirtual delete : ()V
    //   42: aload_0
    //   43: aconst_null
    //   44: putfield mDiskCache : Lcom/samsungimaging/connectionmanager/util/DiskLruCache;
    //   47: aload_0
    //   48: invokevirtual initDiskCache : ()V
    //   51: aload_2
    //   52: monitorexit
    //   53: return
    //   54: astore_3
    //   55: getstatic com/samsungimaging/connectionmanager/util/Trace$Tag.COMMON : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   58: new java/lang/StringBuilder
    //   61: dup
    //   62: ldc 'clearCache() - '
    //   64: invokespecial <init> : (Ljava/lang/String;)V
    //   67: aload_3
    //   68: invokevirtual getMessage : ()Ljava/lang/String;
    //   71: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   74: invokevirtual toString : ()Ljava/lang/String;
    //   77: invokestatic e : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   80: goto -> 42
    //   83: astore_3
    //   84: aload_2
    //   85: monitorexit
    //   86: aload_3
    //   87: athrow
    // Exception table:
    //   from	to	target	type
    //   11	31	83	finally
    //   35	42	54	java/io/IOException
    //   35	42	83	finally
    //   42	51	83	finally
    //   51	53	83	finally
    //   55	80	83	finally
    //   84	86	83	finally
  }
  
  public void clearMemoryCache() {
    if (this.mMemoryCache != null)
      this.mMemoryCache.evictAll(); 
  }
  
  public void closeCache() {
    synchronized (this.mDiskCacheLock) {
      DiskLruCache diskLruCache = this.mDiskCache;
      if (diskLruCache != null)
        try {
          if (!this.mDiskCache.isClosed()) {
            this.mDiskCache.close();
            this.mDiskCache = null;
          } 
        } catch (IOException iOException) {} 
      return;
    } 
  }
  
  public void flushCache() {
    synchronized (this.mDiskCacheLock) {
      DiskLruCache diskLruCache = this.mDiskCache;
      if (diskLruCache != null)
        try {
          this.mDiskCache.flush();
        } catch (IOException iOException) {} 
      return;
    } 
  }
  
  public RecycleBitmapDrawable getBitmapDrawableFromMemoryCache(String paramString) {
    RecycleBitmapDrawable recycleBitmapDrawable = null;
    if (this.mMemoryCache != null)
      recycleBitmapDrawable = (RecycleBitmapDrawable)this.mMemoryCache.get(paramString); 
    return recycleBitmapDrawable;
  }
  
  public Bitmap getBitmapFromDiskCache(String paramString) {
    int i = paramString.hashCode();
    DiskLruCache diskLruCache1 = null;
    DiskLruCache diskLruCache2 = null;
    synchronized (this.mDiskCacheLock) {
      while (true) {
        if (!this.mDiskCacheStarting) {
          DiskLruCache diskLruCache3 = this.mDiskCache;
          DiskLruCache diskLruCache4 = diskLruCache1;
          if (diskLruCache3 != null) {
            Bitmap bitmap;
            diskLruCache4 = null;
            InputStream inputStream2 = null;
            InputStream inputStream3 = null;
            diskLruCache3 = diskLruCache4;
            InputStream inputStream1 = inputStream2;
            try {
              DiskLruCache.Snapshot snapshot = this.mDiskCache.get(String.valueOf(i));
              diskLruCache3 = diskLruCache2;
              inputStream1 = inputStream3;
              if (snapshot != null) {
                diskLruCache3 = diskLruCache4;
                inputStream1 = inputStream2;
                InputStream inputStream = snapshot.getInputStream(0);
                diskLruCache3 = diskLruCache2;
                inputStream1 = inputStream;
                if (inputStream != null) {
                  InputStream inputStream4 = inputStream;
                  inputStream1 = inputStream;
                  Bitmap bitmap2 = BitmapFactory.decodeStream(inputStream);
                  null = bitmap2;
                  inputStream1 = inputStream;
                } 
              } 
              Bitmap bitmap1 = null;
              return bitmap1;
            } catch (IOException iOException) {
              bitmap = null;
              Trace.e(Trace.Tag.COMMON, "getBitmapFromDiskCache() - " + iOException.getMessage());
              diskLruCache4 = diskLruCache1;
              return (Bitmap)diskLruCache4;
            } finally {
              if (bitmap != null)
                try {
                  bitmap.close();
                } catch (IOException iOException) {} 
            } 
          } 
          return (Bitmap)diskLruCache4;
        } 
        try {
          this.mDiskCacheLock.wait();
        } catch (InterruptedException interruptedException) {}
      } 
    } 
  }
  
  public boolean hasSnapshotInDiskCache(String paramString) {
    boolean bool = false;
    int i = paramString.hashCode();
    synchronized (this.mDiskCacheLock) {
      while (true) {
        if (!this.mDiskCacheStarting) {
          DiskLruCache diskLruCache = this.mDiskCache;
          boolean bool1 = bool;
          if (diskLruCache != null)
            try {
              DiskLruCache.Snapshot snapshot = this.mDiskCache.get(String.valueOf(i));
              bool1 = bool;
              if (snapshot != null)
                bool1 = true; 
            } catch (IOException iOException) {} 
          return bool1;
        } 
        try {
          this.mDiskCacheLock.wait();
        } catch (InterruptedException interruptedException) {}
      } 
    } 
  }
  
  public void initDiskCache() {
    synchronized (this.mDiskCacheLock) {
      if (this.mDiskCache == null || this.mDiskCache.isClosed()) {
        String str;
        if (("mounted".equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) && this.mContext.getExternalCacheDir() != null) {
          str = this.mContext.getExternalCacheDir().getPath();
        } else {
          str = this.mContext.getCacheDir().getPath();
        } 
        File file = new File(String.valueOf(str) + File.separator + this.mDiskCacheDir);
        if (!file.exists())
          file.mkdirs(); 
        long l = file.getUsableSpace();
        if (l > 10485760L)
          try {
            this.mDiskCache = DiskLruCache.open(file, 1, 1, 10485760L);
          } catch (IOException iOException) {} 
      } 
      this.mDiskCacheStarting = false;
      this.mDiskCacheLock.notifyAll();
      return;
    } 
  }
  
  public void remove(String paramString) {
    if (this.mMemoryCache != null && this.mMemoryCache != null) {
      RecycleBitmapDrawable recycleBitmapDrawable = (RecycleBitmapDrawable)this.mMemoryCache.get(paramString);
      if (recycleBitmapDrawable != null) {
        Bitmap bitmap = recycleBitmapDrawable.getBitmap();
        this.mMemoryCache.remove(paramString);
        if (Build.VERSION.SDK_INT < 19 && !bitmap.isRecycled()) {
          Trace.i(Trace.Tag.COMMON, "bitmap recycle!!!");
          bitmap.recycle();
        } 
      } 
    } 
    synchronized (this.mDiskCacheLock) {
      DiskLruCache diskLruCache = this.mDiskCache;
      if (diskLruCache != null)
        try {
          int i = paramString.hashCode();
          this.mDiskCache.remove(String.valueOf(i));
        } catch (IOException iOException) {} 
      return;
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanage\\util\ImageCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */