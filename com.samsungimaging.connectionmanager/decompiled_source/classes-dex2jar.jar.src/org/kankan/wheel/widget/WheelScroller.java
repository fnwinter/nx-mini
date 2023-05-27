package org.kankan.wheel.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class WheelScroller {
  public static final int MIN_DELTA_FOR_SCROLLING = 1;
  
  private static final int SCROLLING_DURATION = 400;
  
  private final int MESSAGE_JUSTIFY = 1;
  
  private final int MESSAGE_SCROLL = 0;
  
  private Handler animationHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        WheelScroller.this.scroller.computeScrollOffset();
        int i = WheelScroller.this.scroller.getCurrX();
        int j = WheelScroller.this.lastScrollX - i;
        WheelScroller.this.lastScrollX = i;
        if (j != 0)
          WheelScroller.this.listener.onScroll(j); 
        if (Math.abs(i - WheelScroller.this.scroller.getFinalX()) < 1) {
          WheelScroller.this.scroller.getFinalX();
          WheelScroller.this.scroller.forceFinished(true);
        } 
        if (!WheelScroller.this.scroller.isFinished()) {
          WheelScroller.this.animationHandler.sendEmptyMessage(param1Message.what);
          return;
        } 
        if (param1Message.what == 0) {
          WheelScroller.this.justify();
          return;
        } 
        WheelScroller.this.finishScrolling();
      }
    };
  
  private Context context;
  
  private GestureDetector gestureDetector;
  
  private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
      public boolean onFling(MotionEvent param1MotionEvent1, MotionEvent param1MotionEvent2, float param1Float1, float param1Float2) {
        WheelScroller.this.lastScrollX = 0;
        WheelScroller.this.scroller.fling(WheelScroller.this.lastScrollX, 0, (int)-param1Float1, 0, -2147483647, 2147483647, 0, 0);
        WheelScroller.this.setNextMessage(0);
        return true;
      }
      
      public boolean onScroll(MotionEvent param1MotionEvent1, MotionEvent param1MotionEvent2, float param1Float1, float param1Float2) {
        return true;
      }
    };
  
  private boolean isScrollingPerformed;
  
  private int lastScrollX;
  
  private float lastTouchedX;
  
  private ScrollingListener listener;
  
  private Scroller scroller;
  
  public WheelScroller(Context paramContext, ScrollingListener paramScrollingListener) {
    this.gestureDetector = new GestureDetector(paramContext, (GestureDetector.OnGestureListener)this.gestureListener);
    this.gestureDetector.setIsLongpressEnabled(false);
    this.scroller = new Scroller(paramContext);
    this.listener = paramScrollingListener;
    this.context = paramContext;
  }
  
  private void clearMessages() {
    this.animationHandler.removeMessages(0);
    this.animationHandler.removeMessages(1);
  }
  
  private void justify() {
    this.listener.onJustify();
    setNextMessage(1);
  }
  
  private void setNextMessage(int paramInt) {
    clearMessages();
    this.animationHandler.sendEmptyMessage(paramInt);
  }
  
  private void startScrolling() {
    if (!this.isScrollingPerformed) {
      this.isScrollingPerformed = true;
      this.listener.onStarted();
    } 
  }
  
  void finishScrolling() {
    if (this.isScrollingPerformed) {
      this.listener.onFinished();
      this.isScrollingPerformed = false;
    } 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    switch (paramMotionEvent.getAction()) {
      default:
        if (!this.gestureDetector.onTouchEvent(paramMotionEvent) && paramMotionEvent.getAction() == 1)
          justify(); 
        return true;
      case 0:
        this.lastTouchedX = paramMotionEvent.getX();
        this.scroller.forceFinished(true);
        clearMessages();
      case 2:
        break;
    } 
    int i = (int)(paramMotionEvent.getX() - this.lastTouchedX);
    if (i != 0) {
      startScrolling();
      this.listener.onScroll(i);
      this.lastTouchedX = paramMotionEvent.getX();
    } 
  }
  
  public void scroll(int paramInt1, int paramInt2) {
    this.scroller.forceFinished(true);
    this.lastScrollX = 0;
    Scroller scroller = this.scroller;
    if (paramInt2 == 0)
      paramInt2 = 400; 
    scroller.startScroll(0, 0, paramInt1, 0, paramInt2);
    setNextMessage(0);
    startScrolling();
  }
  
  public void setInterpolator(Interpolator paramInterpolator) {
    this.scroller.forceFinished(true);
    this.scroller = new Scroller(this.context, paramInterpolator);
  }
  
  public void stopScrolling() {
    this.scroller.forceFinished(true);
  }
  
  public static interface ScrollingListener {
    void onFinished();
    
    void onJustify();
    
    void onScroll(int param1Int);
    
    void onStarted();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\kankan\wheel\widget\WheelScroller.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */