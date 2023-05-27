package android.support.v4.widget;

import android.content.Context;
import android.os.Build;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class ScrollerCompat {
  static final int CHASE_FRAME_TIME = 16;
  
  private static final String TAG = "ScrollerCompat";
  
  ScrollerCompatImpl mImpl;
  
  Object mScroller;
  
  private ScrollerCompat(int paramInt, Context paramContext, Interpolator paramInterpolator) {
    if (paramInt >= 14) {
      this.mImpl = new ScrollerCompatImplIcs();
    } else if (paramInt >= 9) {
      this.mImpl = new ScrollerCompatImplGingerbread();
    } else {
      this.mImpl = new ScrollerCompatImplBase();
    } 
    this.mScroller = this.mImpl.createScroller(paramContext, paramInterpolator);
  }
  
  ScrollerCompat(Context paramContext, Interpolator paramInterpolator) {
    this(Build.VERSION.SDK_INT, paramContext, paramInterpolator);
  }
  
  public static ScrollerCompat create(Context paramContext) {
    return create(paramContext, null);
  }
  
  public static ScrollerCompat create(Context paramContext, Interpolator paramInterpolator) {
    return new ScrollerCompat(paramContext, paramInterpolator);
  }
  
  public void abortAnimation() {
    this.mImpl.abortAnimation(this.mScroller);
  }
  
  public boolean computeScrollOffset() {
    return this.mImpl.computeScrollOffset(this.mScroller);
  }
  
  public void fling(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8) {
    this.mImpl.fling(this.mScroller, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8);
  }
  
  public void fling(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10) {
    this.mImpl.fling(this.mScroller, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramInt9, paramInt10);
  }
  
  public float getCurrVelocity() {
    return this.mImpl.getCurrVelocity(this.mScroller);
  }
  
  public int getCurrX() {
    return this.mImpl.getCurrX(this.mScroller);
  }
  
  public int getCurrY() {
    return this.mImpl.getCurrY(this.mScroller);
  }
  
  public int getFinalX() {
    return this.mImpl.getFinalX(this.mScroller);
  }
  
  public int getFinalY() {
    return this.mImpl.getFinalY(this.mScroller);
  }
  
  public boolean isFinished() {
    return this.mImpl.isFinished(this.mScroller);
  }
  
  public boolean isOverScrolled() {
    return this.mImpl.isOverScrolled(this.mScroller);
  }
  
  public void notifyHorizontalEdgeReached(int paramInt1, int paramInt2, int paramInt3) {
    this.mImpl.notifyHorizontalEdgeReached(this.mScroller, paramInt1, paramInt2, paramInt3);
  }
  
  public void notifyVerticalEdgeReached(int paramInt1, int paramInt2, int paramInt3) {
    this.mImpl.notifyVerticalEdgeReached(this.mScroller, paramInt1, paramInt2, paramInt3);
  }
  
  public boolean springBack(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
    return this.mImpl.springBack(this.mScroller, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
  }
  
  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mImpl.startScroll(this.mScroller, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    this.mImpl.startScroll(this.mScroller, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
  }
  
  static interface ScrollerCompatImpl {
    void abortAnimation(Object param1Object);
    
    boolean computeScrollOffset(Object param1Object);
    
    Object createScroller(Context param1Context, Interpolator param1Interpolator);
    
    void fling(Object param1Object, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, int param1Int8);
    
    void fling(Object param1Object, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, int param1Int8, int param1Int9, int param1Int10);
    
    float getCurrVelocity(Object param1Object);
    
    int getCurrX(Object param1Object);
    
    int getCurrY(Object param1Object);
    
    int getFinalX(Object param1Object);
    
    int getFinalY(Object param1Object);
    
    boolean isFinished(Object param1Object);
    
    boolean isOverScrolled(Object param1Object);
    
    void notifyHorizontalEdgeReached(Object param1Object, int param1Int1, int param1Int2, int param1Int3);
    
    void notifyVerticalEdgeReached(Object param1Object, int param1Int1, int param1Int2, int param1Int3);
    
    boolean springBack(Object param1Object, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6);
    
    void startScroll(Object param1Object, int param1Int1, int param1Int2, int param1Int3, int param1Int4);
    
    void startScroll(Object param1Object, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5);
  }
  
  static class ScrollerCompatImplBase implements ScrollerCompatImpl {
    public void abortAnimation(Object param1Object) {
      ((Scroller)param1Object).abortAnimation();
    }
    
    public boolean computeScrollOffset(Object param1Object) {
      return ((Scroller)param1Object).computeScrollOffset();
    }
    
    public Object createScroller(Context param1Context, Interpolator param1Interpolator) {
      return (param1Interpolator != null) ? new Scroller(param1Context, param1Interpolator) : new Scroller(param1Context);
    }
    
    public void fling(Object param1Object, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, int param1Int8) {
      ((Scroller)param1Object).fling(param1Int1, param1Int2, param1Int3, param1Int4, param1Int5, param1Int6, param1Int7, param1Int8);
    }
    
    public void fling(Object param1Object, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, int param1Int8, int param1Int9, int param1Int10) {
      ((Scroller)param1Object).fling(param1Int1, param1Int2, param1Int3, param1Int4, param1Int5, param1Int6, param1Int7, param1Int8);
    }
    
    public float getCurrVelocity(Object param1Object) {
      return 0.0F;
    }
    
    public int getCurrX(Object param1Object) {
      return ((Scroller)param1Object).getCurrX();
    }
    
    public int getCurrY(Object param1Object) {
      return ((Scroller)param1Object).getCurrY();
    }
    
    public int getFinalX(Object param1Object) {
      return ((Scroller)param1Object).getFinalX();
    }
    
    public int getFinalY(Object param1Object) {
      return ((Scroller)param1Object).getFinalY();
    }
    
    public boolean isFinished(Object param1Object) {
      return ((Scroller)param1Object).isFinished();
    }
    
    public boolean isOverScrolled(Object param1Object) {
      return false;
    }
    
    public void notifyHorizontalEdgeReached(Object param1Object, int param1Int1, int param1Int2, int param1Int3) {}
    
    public void notifyVerticalEdgeReached(Object param1Object, int param1Int1, int param1Int2, int param1Int3) {}
    
    public boolean springBack(Object param1Object, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6) {
      return false;
    }
    
    public void startScroll(Object param1Object, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      ((Scroller)param1Object).startScroll(param1Int1, param1Int2, param1Int3, param1Int4);
    }
    
    public void startScroll(Object param1Object, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5) {
      ((Scroller)param1Object).startScroll(param1Int1, param1Int2, param1Int3, param1Int4, param1Int5);
    }
  }
  
  static class ScrollerCompatImplGingerbread implements ScrollerCompatImpl {
    public void abortAnimation(Object param1Object) {
      ScrollerCompatGingerbread.abortAnimation(param1Object);
    }
    
    public boolean computeScrollOffset(Object param1Object) {
      return ScrollerCompatGingerbread.computeScrollOffset(param1Object);
    }
    
    public Object createScroller(Context param1Context, Interpolator param1Interpolator) {
      return ScrollerCompatGingerbread.createScroller(param1Context, param1Interpolator);
    }
    
    public void fling(Object param1Object, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, int param1Int8) {
      ScrollerCompatGingerbread.fling(param1Object, param1Int1, param1Int2, param1Int3, param1Int4, param1Int5, param1Int6, param1Int7, param1Int8);
    }
    
    public void fling(Object param1Object, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6, int param1Int7, int param1Int8, int param1Int9, int param1Int10) {
      ScrollerCompatGingerbread.fling(param1Object, param1Int1, param1Int2, param1Int3, param1Int4, param1Int5, param1Int6, param1Int7, param1Int8, param1Int9, param1Int10);
    }
    
    public float getCurrVelocity(Object param1Object) {
      return 0.0F;
    }
    
    public int getCurrX(Object param1Object) {
      return ScrollerCompatGingerbread.getCurrX(param1Object);
    }
    
    public int getCurrY(Object param1Object) {
      return ScrollerCompatGingerbread.getCurrY(param1Object);
    }
    
    public int getFinalX(Object param1Object) {
      return ScrollerCompatGingerbread.getFinalX(param1Object);
    }
    
    public int getFinalY(Object param1Object) {
      return ScrollerCompatGingerbread.getFinalY(param1Object);
    }
    
    public boolean isFinished(Object param1Object) {
      return ScrollerCompatGingerbread.isFinished(param1Object);
    }
    
    public boolean isOverScrolled(Object param1Object) {
      return ScrollerCompatGingerbread.isOverScrolled(param1Object);
    }
    
    public void notifyHorizontalEdgeReached(Object param1Object, int param1Int1, int param1Int2, int param1Int3) {
      ScrollerCompatGingerbread.notifyHorizontalEdgeReached(param1Object, param1Int1, param1Int2, param1Int3);
    }
    
    public void notifyVerticalEdgeReached(Object param1Object, int param1Int1, int param1Int2, int param1Int3) {
      ScrollerCompatGingerbread.notifyVerticalEdgeReached(param1Object, param1Int1, param1Int2, param1Int3);
    }
    
    public boolean springBack(Object param1Object, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, int param1Int6) {
      return ScrollerCompatGingerbread.springBack(param1Object, param1Int1, param1Int2, param1Int3, param1Int4, param1Int5, param1Int6);
    }
    
    public void startScroll(Object param1Object, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      ScrollerCompatGingerbread.startScroll(param1Object, param1Int1, param1Int2, param1Int3, param1Int4);
    }
    
    public void startScroll(Object param1Object, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5) {
      ScrollerCompatGingerbread.startScroll(param1Object, param1Int1, param1Int2, param1Int3, param1Int4, param1Int5);
    }
  }
  
  static class ScrollerCompatImplIcs extends ScrollerCompatImplGingerbread {
    public float getCurrVelocity(Object param1Object) {
      return ScrollerCompatIcs.getCurrVelocity(param1Object);
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\widget\ScrollerCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */