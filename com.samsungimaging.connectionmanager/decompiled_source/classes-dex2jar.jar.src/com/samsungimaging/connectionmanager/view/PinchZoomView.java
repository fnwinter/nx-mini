package com.samsungimaging.connectionmanager.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.Scroller;
import com.samsungimaging.connectionmanager.util.Trace;

public class PinchZoomView extends RecycleImageView {
  public static final float NUM_OF_SCALE_DOUBLE_TAB = 2.0F;
  
  public static final float NUM_OF_SCALE_MAX = 6.0F;
  
  public static final float NUM_OF_SCALE_MIN = 1.0F;
  
  private PointF mCenterRatio = new PointF();
  
  private InternalDoubleTap mDoubleTap = null;
  
  private InternalFling mFling = null;
  
  private GestureDetector mGestureDetector;
  
  private float mImageHeight = 0.0F;
  
  private float mImageWidth = 0.0F;
  
  private boolean mInitializedLayout = false;
  
  private Matrix mMatrix = new Matrix();
  
  private float[] mMatrixCoordinate = new float[9];
  
  private int mOrientation = 0;
  
  private float mOriginalImageHeight = 0.0F;
  
  private float mOriginalImageWidth = 0.0F;
  
  private boolean mPinchZoomable = true;
  
  private float mScale = 0.0F;
  
  private ScaleGestureDetector mScaleDetector;
  
  private float mScaleMax = 0.0F;
  
  private float mScaleMin = 0.0F;
  
  private State mState = State.NONE;
  
  public PinchZoomView(Context paramContext) {
    super(paramContext);
    init();
  }
  
  public PinchZoomView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  public PinchZoomView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }
  
  private float checkDragBound(float paramFloat1, float paramFloat2, float paramFloat3) {
    if (paramFloat1 > 0.0F)
      return 0.0F; 
    float f = paramFloat1;
    return (paramFloat1 < -paramFloat3 + paramFloat2) ? (-paramFloat3 + paramFloat2) : f;
  }
  
  private void checkScaleBound() {
    this.mMatrix.getValues(this.mMatrixCoordinate);
    if (this.mImageWidth >= getWidth()) {
      if (this.mMatrixCoordinate[2] >= 0.0F) {
        this.mMatrixCoordinate[2] = 0.0F;
      } else if (this.mMatrixCoordinate[2] < getWidth() - this.mImageWidth) {
        this.mMatrixCoordinate[2] = getWidth() - this.mImageWidth;
      } 
    } else {
      this.mMatrixCoordinate[2] = (getWidth() - this.mImageWidth) / 2.0F;
    } 
    if (this.mImageHeight >= getHeight()) {
      if (this.mMatrixCoordinate[5] >= 0.0F) {
        this.mMatrixCoordinate[5] = 0.0F;
      } else if (this.mMatrixCoordinate[5] < getHeight() - this.mImageHeight) {
        this.mMatrixCoordinate[5] = getHeight() - this.mImageHeight;
      } 
    } else {
      this.mMatrixCoordinate[5] = (getHeight() - this.mImageHeight) / 2.0F;
    } 
    this.mMatrix.setValues(this.mMatrixCoordinate);
  }
  
  private void init() {
    this.mInitializedLayout = false;
    this.mScaleDetector = new ScaleGestureDetector(getContext(), new InternalGestureListener(null));
    this.mGestureDetector = new GestureDetector(getContext(), (GestureDetector.OnGestureListener)new InternalGestureListener(null));
    setScaleType(ImageView.ScaleType.MATRIX);
  }
  
  private void setImage() {
    this.mInitializedLayout = false;
    Drawable drawable = getDrawable();
    if (drawable != null) {
      this.mOriginalImageWidth = drawable.getIntrinsicWidth();
      this.mOriginalImageHeight = drawable.getIntrinsicHeight();
      if (this.mOriginalImageWidth != 0.0F && this.mOriginalImageHeight != 0.0F)
        Trace.d(Trace.Tag.COMMON, "OriginalImageWidth : " + this.mOriginalImageWidth + ",    OriginalImageHeight : " + this.mOriginalImageHeight); 
    } 
  }
  
  private void setMatrix(Matrix paramMatrix) {
    this.mMatrix.getValues(this.mMatrixCoordinate);
    this.mCenterRatio.x = ((getWidth() / 2) - this.mMatrixCoordinate[2]) / this.mImageWidth;
    this.mCenterRatio.y = ((getHeight() / 2) - this.mMatrixCoordinate[5]) / this.mImageHeight;
    setImageMatrix(paramMatrix);
  }
  
  public boolean isBoundaryLeft() {
    this.mMatrix.getValues(this.mMatrixCoordinate);
    return !(getWidth() <= this.mImageWidth && this.mMatrixCoordinate[2] != 0.0F);
  }
  
  public boolean isBoundaryRight() {
    this.mMatrix.getValues(this.mMatrixCoordinate);
    return !(getWidth() <= this.mImageWidth && -this.mMatrixCoordinate[2] + getWidth() != this.mImageWidth);
  }
  
  public boolean isScaleMin() {
    return (this.mScale == this.mScaleMin);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (this.mOriginalImageWidth <= 0.0F || this.mOriginalImageHeight <= 0.0F) {
      super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
      return;
    } 
    if (this.mDoubleTap != null)
      this.mDoubleTap.cancel(); 
    if (this.mFling != null)
      this.mFling.cancel(); 
    int i = (getResources().getConfiguration()).orientation;
    float f = Math.min(getWidth() / this.mOriginalImageWidth, getHeight() / this.mOriginalImageHeight);
    this.mMatrix.getValues(this.mMatrixCoordinate);
    if (!this.mInitializedLayout || this.mScale == this.mScaleMin) {
      this.mInitializedLayout = true;
      if (f >= 6.0F) {
        f = 6.0F;
        this.mScaleMax = 6.0F;
        this.mScaleMin = 6.0F;
        this.mScale = 6.0F;
      } else {
        this.mScale = 1.0F;
        this.mScaleMin = 1.0F;
        this.mScaleMax = 6.0F / f;
      } 
      this.mImageWidth = this.mOriginalImageWidth * f;
      this.mImageHeight = this.mOriginalImageHeight * f;
      this.mMatrixCoordinate[0] = f;
      this.mMatrixCoordinate[4] = f;
      this.mMatrixCoordinate[2] = (getWidth() - this.mImageWidth) / 2.0F;
      this.mMatrixCoordinate[5] = (getHeight() - this.mImageHeight) / 2.0F;
      this.mMatrix.setValues(this.mMatrixCoordinate);
    } else if (i != this.mOrientation) {
      if (f >= 6.0F) {
        f = 6.0F;
        this.mScaleMax = 6.0F;
        this.mScaleMin = 6.0F;
        this.mScale = 6.0F;
      } else {
        float f1 = 6.0F / f;
        this.mScaleMin = 1.0F;
        this.mScale = this.mScale / this.mScaleMax * f1;
        if (this.mScale < 1.0F)
          this.mScale = 1.0F; 
        this.mScaleMax = f1;
        f *= this.mScale;
      } 
      this.mMatrixCoordinate[0] = f;
      this.mMatrixCoordinate[4] = f;
      this.mMatrixCoordinate[2] = -(this.mImageWidth * this.mCenterRatio.x - (getWidth() / 2));
      this.mMatrixCoordinate[5] = -(this.mImageHeight * this.mCenterRatio.y - (getHeight() / 2));
      this.mMatrix.setValues(this.mMatrixCoordinate);
      checkScaleBound();
    } 
    setMatrix(this.mMatrix);
    this.mOrientation = i;
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (this.mScaleMin == this.mScaleMax || !this.mPinchZoomable)
      return true; 
    this.mScaleDetector.onTouchEvent(paramMotionEvent);
    this.mGestureDetector.onTouchEvent(paramMotionEvent);
    setMatrix(this.mMatrix);
    return true;
  }
  
  public void setImageDrawable(Drawable paramDrawable) {
    super.setImageDrawable(paramDrawable);
    setImage();
  }
  
  public void setPinchZoomable(boolean paramBoolean) {
    this.mPinchZoomable = paramBoolean;
  }
  
  public void setScale(float paramFloat1, float paramFloat2, float paramFloat3) {
    float f = this.mScale * paramFloat1;
    if (f < this.mScaleMin) {
      paramFloat1 = this.mScaleMin / this.mScale;
      this.mScale = this.mScaleMin;
    } else if (f > this.mScaleMax) {
      paramFloat1 = this.mScaleMax / this.mScale;
      this.mScale = this.mScaleMax;
    } else {
      this.mScale = f;
    } 
    if (paramFloat1 != 1.0F) {
      this.mImageWidth *= paramFloat1;
      this.mImageHeight *= paramFloat1;
      this.mMatrix.postScale(paramFloat1, paramFloat1, paramFloat2, paramFloat3);
      checkScaleBound();
    } 
    this.mMatrix.getValues(this.mMatrixCoordinate);
  }
  
  public void setScaleMin() {
    setScale(1.0F / this.mScale, getWidth() / 2.0F, getHeight() / 2.0F);
    setMatrix(this.mMatrix);
  }
  
  private class InternalDoubleTap implements Runnable {
    private static final float SCALE_TIME_DELAY = 300.0F;
    
    private float mFromScale;
    
    private DecelerateInterpolator mInterpolator = new DecelerateInterpolator();
    
    private long mStartTime;
    
    private float mToScale;
    
    private PointF mTouchPosition = null;
    
    private InternalDoubleTap() {}
    
    private float getElapsedTime() {
      float f = Math.min(1.0F, (float)(SystemClock.elapsedRealtime() - this.mStartTime) / 300.0F);
      return this.mInterpolator.getInterpolation(f);
    }
    
    private void translateCenterForTouchPosition(float param1Float) {
      float f = ((PinchZoomView.this.getWidth() / 2) - this.mTouchPosition.x) * param1Float;
      param1Float = ((PinchZoomView.this.getHeight() / 2) - this.mTouchPosition.y) * param1Float;
      PinchZoomView.this.mMatrix.getValues(PinchZoomView.this.mMatrixCoordinate);
      float[] arrayOfFloat = PinchZoomView.this.mMatrixCoordinate;
      arrayOfFloat[2] = arrayOfFloat[2] + f;
      arrayOfFloat = PinchZoomView.this.mMatrixCoordinate;
      arrayOfFloat[5] = arrayOfFloat[5] + param1Float;
      PinchZoomView.this.mMatrix.setValues(PinchZoomView.this.mMatrixCoordinate);
      PointF pointF = this.mTouchPosition;
      pointF.x += f;
      pointF = this.mTouchPosition;
      pointF.y += param1Float;
      PinchZoomView.this.checkScaleBound();
    }
    
    public void cancel() {
      PinchZoomView.this.removeCallbacks(this);
      PinchZoomView.this.mState = PinchZoomView.State.NONE;
    }
    
    public void execute(float param1Float, MotionEvent param1MotionEvent) {
      PinchZoomView.this.mState = PinchZoomView.State.ZOOM;
      this.mStartTime = SystemClock.elapsedRealtime();
      this.mFromScale = PinchZoomView.this.mScale;
      this.mToScale = param1Float;
      this.mTouchPosition = new PointF(param1MotionEvent.getX(), param1MotionEvent.getY());
      PinchZoomView.this.post(this);
    }
    
    public void run() {
      float f1 = getElapsedTime();
      float f2 = (this.mFromScale + (this.mToScale - this.mFromScale) * f1) / PinchZoomView.this.mScale;
      if (this.mToScale == PinchZoomView.this.mScaleMin) {
        PinchZoomView.this.setScale(f2, (PinchZoomView.this.getWidth() / 2), (PinchZoomView.this.getHeight() / 2));
      } else {
        PinchZoomView.this.setScale(f2, this.mTouchPosition.x, this.mTouchPosition.y);
        translateCenterForTouchPosition(f1);
      } 
      PinchZoomView.this.setMatrix(PinchZoomView.this.mMatrix);
      if (f1 < 1.0F) {
        PinchZoomView.this.post(this);
        return;
      } 
      PinchZoomView.this.mState = PinchZoomView.State.NONE;
    }
  }
  
  private class InternalFling implements Runnable {
    private Scroller mScroller = new Scroller(PinchZoomView.this.getContext(), (Interpolator)new AccelerateDecelerateInterpolator());
    
    public void cancel() {
      PinchZoomView.this.removeCallbacks(this);
      if (this.mScroller != null)
        this.mScroller.forceFinished(true); 
    }
    
    public void execute(float param1Float1, float param1Float2) {
      int k;
      int m;
      int n;
      int i1;
      PinchZoomView.this.mMatrix.getValues(PinchZoomView.this.mMatrixCoordinate);
      int i = (int)PinchZoomView.this.mMatrixCoordinate[2];
      int j = (int)PinchZoomView.this.mMatrixCoordinate[5];
      if (PinchZoomView.this.mImageWidth > PinchZoomView.this.getWidth()) {
        k = PinchZoomView.this.getWidth() - (int)PinchZoomView.this.mImageWidth;
        m = 0;
      } else {
        m = i;
        k = i;
      } 
      if (PinchZoomView.this.mImageHeight > PinchZoomView.this.getHeight()) {
        n = PinchZoomView.this.getHeight() - (int)PinchZoomView.this.mImageHeight;
        i1 = 0;
      } else {
        i1 = j;
        n = j;
      } 
      this.mScroller.fling(i, j, (int)param1Float1, (int)param1Float2, k, m, n, i1);
      PinchZoomView.this.post(this);
    }
    
    public void run() {
      if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
        if (PinchZoomView.this.mMatrixCoordinate[2] != this.mScroller.getCurrX() || PinchZoomView.this.mMatrixCoordinate[5] != this.mScroller.getCurrY()) {
          PinchZoomView.this.mMatrix.getValues(PinchZoomView.this.mMatrixCoordinate);
          PinchZoomView.this.mMatrixCoordinate[2] = this.mScroller.getCurrX();
          PinchZoomView.this.mMatrixCoordinate[5] = this.mScroller.getCurrY();
          PinchZoomView.this.mMatrix.setValues(PinchZoomView.this.mMatrixCoordinate);
          PinchZoomView.this.setMatrix(PinchZoomView.this.mMatrix);
        } 
        PinchZoomView.this.post(this);
        return;
      } 
    }
  }
  
  private class InternalGestureListener extends GestureDetector.SimpleOnGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
    private InternalGestureListener() {}
    
    public boolean onDoubleTap(MotionEvent param1MotionEvent) {
      float f;
      if (PinchZoomView.this.mState == PinchZoomView.State.ZOOM)
        return true; 
      if (PinchZoomView.this.mScale == PinchZoomView.this.mScaleMin) {
        float f1 = PinchZoomView.this.mOriginalImageWidth / PinchZoomView.this.mImageWidth;
        f = f1;
        if (f1 < 1.0F)
          if (PinchZoomView.this.mScaleMax > 2.0F) {
            f = 2.0F;
          } else {
            f = PinchZoomView.this.mScaleMax;
          }  
      } else {
        f = PinchZoomView.this.mScaleMin;
      } 
      if (PinchZoomView.this.mDoubleTap == null)
        PinchZoomView.this.mDoubleTap = new PinchZoomView.InternalDoubleTap(null); 
      PinchZoomView.this.mDoubleTap.execute(f, param1MotionEvent);
      return true;
    }
    
    public boolean onDown(MotionEvent param1MotionEvent) {
      if (PinchZoomView.this.mFling != null)
        PinchZoomView.this.mFling.cancel(); 
      return true;
    }
    
    public boolean onFling(MotionEvent param1MotionEvent1, MotionEvent param1MotionEvent2, float param1Float1, float param1Float2) {
      if (PinchZoomView.this.mScale == PinchZoomView.this.mScaleMin || PinchZoomView.this.mState == PinchZoomView.State.ZOOM)
        return true; 
      if (PinchZoomView.this.mFling == null)
        PinchZoomView.this.mFling = new PinchZoomView.InternalFling(); 
      PinchZoomView.this.mFling.execute(param1Float1 / 4.0F, param1Float2 / 4.0F);
      return true;
    }
    
    public boolean onScale(ScaleGestureDetector param1ScaleGestureDetector) {
      PinchZoomView.this.setScale(param1ScaleGestureDetector.getScaleFactor(), param1ScaleGestureDetector.getFocusX(), param1ScaleGestureDetector.getFocusY());
      return true;
    }
    
    public boolean onScaleBegin(ScaleGestureDetector param1ScaleGestureDetector) {
      PinchZoomView.this.mState = PinchZoomView.State.ZOOM;
      return true;
    }
    
    public void onScaleEnd(ScaleGestureDetector param1ScaleGestureDetector) {
      PinchZoomView.this.mState = PinchZoomView.State.NONE;
    }
    
    public boolean onScroll(MotionEvent param1MotionEvent1, MotionEvent param1MotionEvent2, float param1Float1, float param1Float2) {
      if (PinchZoomView.this.mScale != PinchZoomView.this.mScaleMin && PinchZoomView.this.mState != PinchZoomView.State.ZOOM && param1MotionEvent2.getPointerCount() == 1) {
        PinchZoomView.this.mMatrix.getValues(PinchZoomView.this.mMatrixCoordinate);
        float f1 = PinchZoomView.this.mMatrixCoordinate[2];
        float f2 = PinchZoomView.this.mMatrixCoordinate[5];
        if (PinchZoomView.this.getWidth() < PinchZoomView.this.mImageWidth)
          PinchZoomView.this.mMatrixCoordinate[2] = PinchZoomView.this.checkDragBound(f1 - param1Float1, PinchZoomView.this.getWidth(), PinchZoomView.this.mImageWidth); 
        if (PinchZoomView.this.getHeight() < PinchZoomView.this.mImageHeight)
          PinchZoomView.this.mMatrixCoordinate[5] = PinchZoomView.this.checkDragBound(f2 - param1Float2, PinchZoomView.this.getHeight(), PinchZoomView.this.mImageHeight); 
        PinchZoomView.this.mMatrix.setValues(PinchZoomView.this.mMatrixCoordinate);
        return true;
      } 
      return true;
    }
  }
  
  private enum State {
    NONE, ZOOM;
    
    static {
    
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\view\PinchZoomView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */