package com.samsungimaging.connectionmanager.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class RecycleBitmapDrawable extends BitmapDrawable {
  private int mCacheRefCount = 0;
  
  private int mDisplayRefCount = 0;
  
  private boolean mHasBeenDisplayed;
  
  private boolean mIsNoneImage = false;
  
  public RecycleBitmapDrawable(Resources paramResources, Bitmap paramBitmap) {
    super(paramResources, paramBitmap);
  }
  
  public RecycleBitmapDrawable(Bitmap paramBitmap) {
    super(paramBitmap);
  }
  
  private void checkState() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mCacheRefCount : I
    //   6: ifgt -> 50
    //   9: aload_0
    //   10: getfield mDisplayRefCount : I
    //   13: ifgt -> 50
    //   16: aload_0
    //   17: getfield mHasBeenDisplayed : Z
    //   20: ifeq -> 50
    //   23: aload_0
    //   24: invokevirtual hasValidBitmap : ()Z
    //   27: ifeq -> 50
    //   30: getstatic com/samsungimaging/connectionmanager/util/Trace$Tag.COMMON : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   33: ldc 'bitmap recycle!!!'
    //   35: invokestatic i : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   38: aload_0
    //   39: iconst_0
    //   40: putfield mHasBeenDisplayed : Z
    //   43: aload_0
    //   44: invokevirtual getBitmap : ()Landroid/graphics/Bitmap;
    //   47: invokevirtual recycle : ()V
    //   50: aload_0
    //   51: monitorexit
    //   52: return
    //   53: astore_1
    //   54: aload_0
    //   55: monitorexit
    //   56: aload_1
    //   57: athrow
    // Exception table:
    //   from	to	target	type
    //   2	50	53	finally
  }
  
  public boolean hasValidBitmap() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual getBitmap : ()Landroid/graphics/Bitmap;
    //   6: astore_2
    //   7: aload_2
    //   8: ifnull -> 26
    //   11: aload_2
    //   12: invokevirtual isRecycled : ()Z
    //   15: istore_1
    //   16: iload_1
    //   17: ifne -> 26
    //   20: iconst_1
    //   21: istore_1
    //   22: aload_0
    //   23: monitorexit
    //   24: iload_1
    //   25: ireturn
    //   26: iconst_0
    //   27: istore_1
    //   28: goto -> 22
    //   31: astore_2
    //   32: aload_0
    //   33: monitorexit
    //   34: aload_2
    //   35: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	31	finally
    //   11	16	31	finally
  }
  
  public boolean isNoneImage() {
    return this.mIsNoneImage;
  }
  
  public void setCached(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_1
    //   3: ifeq -> 23
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mCacheRefCount : I
    //   11: iconst_1
    //   12: iadd
    //   13: putfield mCacheRefCount : I
    //   16: aload_0
    //   17: monitorexit
    //   18: aload_0
    //   19: invokespecial checkState : ()V
    //   22: return
    //   23: aload_0
    //   24: aload_0
    //   25: getfield mCacheRefCount : I
    //   28: iconst_1
    //   29: isub
    //   30: putfield mCacheRefCount : I
    //   33: goto -> 16
    //   36: astore_2
    //   37: aload_0
    //   38: monitorexit
    //   39: aload_2
    //   40: athrow
    // Exception table:
    //   from	to	target	type
    //   6	16	36	finally
    //   16	18	36	finally
    //   23	33	36	finally
    //   37	39	36	finally
  }
  
  public void setDisplayed(boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iload_1
    //   3: ifeq -> 28
    //   6: aload_0
    //   7: aload_0
    //   8: getfield mDisplayRefCount : I
    //   11: iconst_1
    //   12: iadd
    //   13: putfield mDisplayRefCount : I
    //   16: aload_0
    //   17: iconst_1
    //   18: putfield mHasBeenDisplayed : Z
    //   21: aload_0
    //   22: monitorexit
    //   23: aload_0
    //   24: invokespecial checkState : ()V
    //   27: return
    //   28: aload_0
    //   29: aload_0
    //   30: getfield mDisplayRefCount : I
    //   33: iconst_1
    //   34: isub
    //   35: putfield mDisplayRefCount : I
    //   38: goto -> 21
    //   41: astore_2
    //   42: aload_0
    //   43: monitorexit
    //   44: aload_2
    //   45: athrow
    // Exception table:
    //   from	to	target	type
    //   6	21	41	finally
    //   21	23	41	finally
    //   28	38	41	finally
    //   42	44	41	finally
  }
  
  public void setNoneImage(boolean paramBoolean) {
    this.mIsNoneImage = paramBoolean;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanage\\util\RecycleBitmapDrawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */