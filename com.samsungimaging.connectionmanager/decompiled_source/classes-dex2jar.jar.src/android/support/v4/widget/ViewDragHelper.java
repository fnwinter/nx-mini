package android.support.v4.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import java.util.Arrays;

public class ViewDragHelper {
  private static final int BASE_SETTLE_DURATION = 256;
  
  public static final int DIRECTION_ALL = 3;
  
  public static final int DIRECTION_HORIZONTAL = 1;
  
  public static final int DIRECTION_VERTICAL = 2;
  
  public static final int EDGE_ALL = 15;
  
  public static final int EDGE_BOTTOM = 8;
  
  public static final int EDGE_LEFT = 1;
  
  public static final int EDGE_RIGHT = 2;
  
  private static final int EDGE_SIZE = 20;
  
  public static final int EDGE_TOP = 4;
  
  public static final int INVALID_POINTER = -1;
  
  private static final int MAX_SETTLE_DURATION = 600;
  
  public static final int STATE_DRAGGING = 1;
  
  public static final int STATE_IDLE = 0;
  
  public static final int STATE_SETTLING = 2;
  
  private static final String TAG = "ViewDragHelper";
  
  private static final Interpolator sInterpolator = new Interpolator() {
      public float getInterpolation(float param1Float) {
        param1Float--;
        return param1Float * param1Float * param1Float * param1Float * param1Float + 1.0F;
      }
    };
  
  private int mActivePointerId = -1;
  
  private final Callback mCallback;
  
  private View mCapturedView;
  
  private int mDragState;
  
  private int[] mEdgeDragsInProgress;
  
  private int[] mEdgeDragsLocked;
  
  private int mEdgeSize;
  
  private int[] mInitialEdgesTouched;
  
  private float[] mInitialMotionX;
  
  private float[] mInitialMotionY;
  
  private float[] mLastMotionX;
  
  private float[] mLastMotionY;
  
  private float mMaxVelocity;
  
  private float mMinVelocity;
  
  private final ViewGroup mParentView;
  
  private int mPointersDown;
  
  private boolean mReleaseInProgress;
  
  private ScrollerCompat mScroller;
  
  private final Runnable mSetIdleRunnable = new Runnable() {
      public void run() {
        ViewDragHelper.this.setDragState(0);
      }
    };
  
  private int mTouchSlop;
  
  private int mTrackingEdges;
  
  private VelocityTracker mVelocityTracker;
  
  private ViewDragHelper(Context paramContext, ViewGroup paramViewGroup, Callback paramCallback) {
    if (paramViewGroup == null)
      throw new IllegalArgumentException("Parent view may not be null"); 
    if (paramCallback == null)
      throw new IllegalArgumentException("Callback may not be null"); 
    this.mParentView = paramViewGroup;
    this.mCallback = paramCallback;
    ViewConfiguration viewConfiguration = ViewConfiguration.get(paramContext);
    this.mEdgeSize = (int)(20.0F * (paramContext.getResources().getDisplayMetrics()).density + 0.5F);
    this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
    this.mMaxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
    this.mMinVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
    this.mScroller = ScrollerCompat.create(paramContext, sInterpolator);
  }
  
  private boolean checkNewEdgeDrag(float paramFloat1, float paramFloat2, int paramInt1, int paramInt2) {
    paramFloat1 = Math.abs(paramFloat1);
    paramFloat2 = Math.abs(paramFloat2);
    if ((this.mInitialEdgesTouched[paramInt1] & paramInt2) == paramInt2 && (this.mTrackingEdges & paramInt2) != 0 && (this.mEdgeDragsLocked[paramInt1] & paramInt2) != paramInt2 && (this.mEdgeDragsInProgress[paramInt1] & paramInt2) != paramInt2 && (paramFloat1 > this.mTouchSlop || paramFloat2 > this.mTouchSlop)) {
      if (paramFloat1 < 0.5F * paramFloat2 && this.mCallback.onEdgeLock(paramInt2)) {
        int[] arrayOfInt = this.mEdgeDragsLocked;
        arrayOfInt[paramInt1] = arrayOfInt[paramInt1] | paramInt2;
        return false;
      } 
      if ((this.mEdgeDragsInProgress[paramInt1] & paramInt2) == 0 && paramFloat1 > this.mTouchSlop)
        return true; 
    } 
    return false;
  }
  
  private boolean checkTouchSlop(View paramView, float paramFloat1, float paramFloat2) {
    boolean bool1;
    boolean bool2;
    boolean bool3 = true;
    if (paramView == null)
      return false; 
    if (this.mCallback.getViewHorizontalDragRange(paramView) > 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if (this.mCallback.getViewVerticalDragRange(paramView) > 0) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    return (bool1 && bool2) ? ((paramFloat1 * paramFloat1 + paramFloat2 * paramFloat2 <= (this.mTouchSlop * this.mTouchSlop)) ? false : bool3) : (bool1 ? ((Math.abs(paramFloat1) <= this.mTouchSlop) ? false : bool3) : (bool2 ? ((Math.abs(paramFloat2) <= this.mTouchSlop) ? false : bool3) : false));
  }
  
  private float clampMag(float paramFloat1, float paramFloat2, float paramFloat3) {
    float f = Math.abs(paramFloat1);
    if (f < paramFloat2)
      return 0.0F; 
    if (f > paramFloat3) {
      paramFloat2 = paramFloat3;
      return (paramFloat1 <= 0.0F) ? -paramFloat3 : paramFloat2;
    } 
    return paramFloat1;
  }
  
  private int clampMag(int paramInt1, int paramInt2, int paramInt3) {
    int i = Math.abs(paramInt1);
    if (i < paramInt2)
      return 0; 
    if (i > paramInt3) {
      paramInt2 = paramInt3;
      return (paramInt1 <= 0) ? -paramInt3 : paramInt2;
    } 
    return paramInt1;
  }
  
  private void clearMotionHistory() {
    if (this.mInitialMotionX == null)
      return; 
    Arrays.fill(this.mInitialMotionX, 0.0F);
    Arrays.fill(this.mInitialMotionY, 0.0F);
    Arrays.fill(this.mLastMotionX, 0.0F);
    Arrays.fill(this.mLastMotionY, 0.0F);
    Arrays.fill(this.mInitialEdgesTouched, 0);
    Arrays.fill(this.mEdgeDragsInProgress, 0);
    Arrays.fill(this.mEdgeDragsLocked, 0);
    this.mPointersDown = 0;
  }
  
  private void clearMotionHistory(int paramInt) {
    if (this.mInitialMotionX == null)
      return; 
    this.mInitialMotionX[paramInt] = 0.0F;
    this.mInitialMotionY[paramInt] = 0.0F;
    this.mLastMotionX[paramInt] = 0.0F;
    this.mLastMotionY[paramInt] = 0.0F;
    this.mInitialEdgesTouched[paramInt] = 0;
    this.mEdgeDragsInProgress[paramInt] = 0;
    this.mEdgeDragsLocked[paramInt] = 0;
    this.mPointersDown &= 1 << paramInt ^ 0xFFFFFFFF;
  }
  
  private int computeAxisDuration(int paramInt1, int paramInt2, int paramInt3) {
    if (paramInt1 == 0)
      return 0; 
    int i = this.mParentView.getWidth();
    int j = i / 2;
    float f3 = Math.min(1.0F, Math.abs(paramInt1) / i);
    float f1 = j;
    float f2 = j;
    f3 = distanceInfluenceForSnapDuration(f3);
    paramInt2 = Math.abs(paramInt2);
    if (paramInt2 > 0) {
      paramInt1 = Math.round(1000.0F * Math.abs((f1 + f2 * f3) / paramInt2)) * 4;
      return Math.min(paramInt1, 600);
    } 
    paramInt1 = (int)((Math.abs(paramInt1) / paramInt3 + 1.0F) * 256.0F);
    return Math.min(paramInt1, 600);
  }
  
  private int computeSettleDuration(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    float f1;
    paramInt3 = clampMag(paramInt3, (int)this.mMinVelocity, (int)this.mMaxVelocity);
    paramInt4 = clampMag(paramInt4, (int)this.mMinVelocity, (int)this.mMaxVelocity);
    int i = Math.abs(paramInt1);
    int j = Math.abs(paramInt2);
    int k = Math.abs(paramInt3);
    int m = Math.abs(paramInt4);
    int n = k + m;
    int i1 = i + j;
    if (paramInt3 != 0) {
      f1 = k / n;
    } else {
      f1 = i / i1;
    } 
    if (paramInt4 != 0) {
      float f = m / n;
      paramInt1 = computeAxisDuration(paramInt1, paramInt3, this.mCallback.getViewHorizontalDragRange(paramView));
      paramInt2 = computeAxisDuration(paramInt2, paramInt4, this.mCallback.getViewVerticalDragRange(paramView));
      return (int)(paramInt1 * f1 + paramInt2 * f);
    } 
    float f2 = j / i1;
    paramInt1 = computeAxisDuration(paramInt1, paramInt3, this.mCallback.getViewHorizontalDragRange(paramView));
    paramInt2 = computeAxisDuration(paramInt2, paramInt4, this.mCallback.getViewVerticalDragRange(paramView));
    return (int)(paramInt1 * f1 + paramInt2 * f2);
  }
  
  public static ViewDragHelper create(ViewGroup paramViewGroup, float paramFloat, Callback paramCallback) {
    ViewDragHelper viewDragHelper = create(paramViewGroup, paramCallback);
    viewDragHelper.mTouchSlop = (int)(viewDragHelper.mTouchSlop * 1.0F / paramFloat);
    return viewDragHelper;
  }
  
  public static ViewDragHelper create(ViewGroup paramViewGroup, Callback paramCallback) {
    return new ViewDragHelper(paramViewGroup.getContext(), paramViewGroup, paramCallback);
  }
  
  private void dispatchViewReleased(float paramFloat1, float paramFloat2) {
    this.mReleaseInProgress = true;
    this.mCallback.onViewReleased(this.mCapturedView, paramFloat1, paramFloat2);
    this.mReleaseInProgress = false;
    if (this.mDragState == 1)
      setDragState(0); 
  }
  
  private float distanceInfluenceForSnapDuration(float paramFloat) {
    return (float)Math.sin((float)((paramFloat - 0.5F) * 0.4712389167638204D));
  }
  
  private void dragTo(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int j = paramInt1;
    int i = paramInt2;
    int k = this.mCapturedView.getLeft();
    int m = this.mCapturedView.getTop();
    if (paramInt3 != 0) {
      j = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, paramInt1, paramInt3);
      this.mCapturedView.offsetLeftAndRight(j - k);
    } 
    if (paramInt4 != 0) {
      i = this.mCallback.clampViewPositionVertical(this.mCapturedView, paramInt2, paramInt4);
      this.mCapturedView.offsetTopAndBottom(i - m);
    } 
    if (paramInt3 != 0 || paramInt4 != 0)
      this.mCallback.onViewPositionChanged(this.mCapturedView, j, i, j - k, i - m); 
  }
  
  private void ensureMotionHistorySizeForId(int paramInt) {
    if (this.mInitialMotionX == null || this.mInitialMotionX.length <= paramInt) {
      float[] arrayOfFloat1 = new float[paramInt + 1];
      float[] arrayOfFloat2 = new float[paramInt + 1];
      float[] arrayOfFloat3 = new float[paramInt + 1];
      float[] arrayOfFloat4 = new float[paramInt + 1];
      int[] arrayOfInt1 = new int[paramInt + 1];
      int[] arrayOfInt2 = new int[paramInt + 1];
      int[] arrayOfInt3 = new int[paramInt + 1];
      if (this.mInitialMotionX != null) {
        System.arraycopy(this.mInitialMotionX, 0, arrayOfFloat1, 0, this.mInitialMotionX.length);
        System.arraycopy(this.mInitialMotionY, 0, arrayOfFloat2, 0, this.mInitialMotionY.length);
        System.arraycopy(this.mLastMotionX, 0, arrayOfFloat3, 0, this.mLastMotionX.length);
        System.arraycopy(this.mLastMotionY, 0, arrayOfFloat4, 0, this.mLastMotionY.length);
        System.arraycopy(this.mInitialEdgesTouched, 0, arrayOfInt1, 0, this.mInitialEdgesTouched.length);
        System.arraycopy(this.mEdgeDragsInProgress, 0, arrayOfInt2, 0, this.mEdgeDragsInProgress.length);
        System.arraycopy(this.mEdgeDragsLocked, 0, arrayOfInt3, 0, this.mEdgeDragsLocked.length);
      } 
      this.mInitialMotionX = arrayOfFloat1;
      this.mInitialMotionY = arrayOfFloat2;
      this.mLastMotionX = arrayOfFloat3;
      this.mLastMotionY = arrayOfFloat4;
      this.mInitialEdgesTouched = arrayOfInt1;
      this.mEdgeDragsInProgress = arrayOfInt2;
      this.mEdgeDragsLocked = arrayOfInt3;
    } 
  }
  
  private boolean forceSettleCapturedViewAt(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = this.mCapturedView.getLeft();
    int j = this.mCapturedView.getTop();
    paramInt1 -= i;
    paramInt2 -= j;
    if (paramInt1 == 0 && paramInt2 == 0) {
      this.mScroller.abortAnimation();
      setDragState(0);
      return false;
    } 
    paramInt3 = computeSettleDuration(this.mCapturedView, paramInt1, paramInt2, paramInt3, paramInt4);
    this.mScroller.startScroll(i, j, paramInt1, paramInt2, paramInt3);
    setDragState(2);
    return true;
  }
  
  private int getEdgesTouched(int paramInt1, int paramInt2) {
    int j = 0;
    if (paramInt1 < this.mParentView.getLeft() + this.mEdgeSize)
      j = false | true; 
    int i = j;
    if (paramInt2 < this.mParentView.getTop() + this.mEdgeSize)
      i = j | 0x4; 
    j = i;
    if (paramInt1 > this.mParentView.getRight() - this.mEdgeSize)
      j = i | 0x2; 
    paramInt1 = j;
    if (paramInt2 > this.mParentView.getBottom() - this.mEdgeSize)
      paramInt1 = j | 0x8; 
    return paramInt1;
  }
  
  private void releaseViewForPointerUp() {
    this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
    dispatchViewReleased(clampMag(VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), clampMag(VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
  }
  
  private void reportNewEdgeDrags(float paramFloat1, float paramFloat2, int paramInt) {
    int j = 0;
    if (checkNewEdgeDrag(paramFloat1, paramFloat2, paramInt, 1))
      j = false | true; 
    int i = j;
    if (checkNewEdgeDrag(paramFloat2, paramFloat1, paramInt, 4))
      i = j | 0x4; 
    j = i;
    if (checkNewEdgeDrag(paramFloat1, paramFloat2, paramInt, 2))
      j = i | 0x2; 
    i = j;
    if (checkNewEdgeDrag(paramFloat2, paramFloat1, paramInt, 8))
      i = j | 0x8; 
    if (i != 0) {
      int[] arrayOfInt = this.mEdgeDragsInProgress;
      arrayOfInt[paramInt] = arrayOfInt[paramInt] | i;
      this.mCallback.onEdgeDragStarted(i, paramInt);
    } 
  }
  
  private void saveInitialMotion(float paramFloat1, float paramFloat2, int paramInt) {
    ensureMotionHistorySizeForId(paramInt);
    float[] arrayOfFloat = this.mInitialMotionX;
    this.mLastMotionX[paramInt] = paramFloat1;
    arrayOfFloat[paramInt] = paramFloat1;
    arrayOfFloat = this.mInitialMotionY;
    this.mLastMotionY[paramInt] = paramFloat2;
    arrayOfFloat[paramInt] = paramFloat2;
    this.mInitialEdgesTouched[paramInt] = getEdgesTouched((int)paramFloat1, (int)paramFloat2);
    this.mPointersDown |= 1 << paramInt;
  }
  
  private void saveLastMotion(MotionEvent paramMotionEvent) {
    int j = MotionEventCompat.getPointerCount(paramMotionEvent);
    int i;
    for (i = 0; i < j; i++) {
      int k = MotionEventCompat.getPointerId(paramMotionEvent, i);
      float f1 = MotionEventCompat.getX(paramMotionEvent, i);
      float f2 = MotionEventCompat.getY(paramMotionEvent, i);
      this.mLastMotionX[k] = f1;
      this.mLastMotionY[k] = f2;
    } 
  }
  
  public void abort() {
    cancel();
    if (this.mDragState == 2) {
      int i = this.mScroller.getCurrX();
      int j = this.mScroller.getCurrY();
      this.mScroller.abortAnimation();
      int k = this.mScroller.getCurrX();
      int m = this.mScroller.getCurrY();
      this.mCallback.onViewPositionChanged(this.mCapturedView, k, m, k - i, m - j);
    } 
    setDragState(0);
  }
  
  protected boolean canScroll(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      int j = paramView.getScrollX();
      int k = paramView.getScrollY();
      int i;
      for (i = viewGroup.getChildCount() - 1; i >= 0; i--) {
        View view = viewGroup.getChildAt(i);
        if (paramInt3 + j >= view.getLeft() && paramInt3 + j < view.getRight() && paramInt4 + k >= view.getTop() && paramInt4 + k < view.getBottom() && canScroll(view, true, paramInt1, paramInt2, paramInt3 + j - view.getLeft(), paramInt4 + k - view.getTop()))
          return true; 
      } 
    } 
    return (paramBoolean && (ViewCompat.canScrollHorizontally(paramView, -paramInt1) || ViewCompat.canScrollVertically(paramView, -paramInt2)));
  }
  
  public void cancel() {
    this.mActivePointerId = -1;
    clearMotionHistory();
    if (this.mVelocityTracker != null) {
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    } 
  }
  
  public void captureChildView(View paramView, int paramInt) {
    if (paramView.getParent() != this.mParentView)
      throw new IllegalArgumentException("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (" + this.mParentView + ")"); 
    this.mCapturedView = paramView;
    this.mActivePointerId = paramInt;
    this.mCallback.onViewCaptured(paramView, paramInt);
    setDragState(1);
  }
  
  public boolean checkTouchSlop(int paramInt) {
    int j = this.mInitialMotionX.length;
    for (int i = 0; i < j; i++) {
      if (checkTouchSlop(paramInt, i))
        return true; 
    } 
    return false;
  }
  
  public boolean checkTouchSlop(int paramInt1, int paramInt2) {
    boolean bool1;
    boolean bool2 = true;
    if (!isPointerDown(paramInt2))
      return false; 
    if ((paramInt1 & 0x1) == 1) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    if ((paramInt1 & 0x2) == 2) {
      paramInt1 = 1;
    } else {
      paramInt1 = 0;
    } 
    float f1 = this.mLastMotionX[paramInt2] - this.mInitialMotionX[paramInt2];
    float f2 = this.mLastMotionY[paramInt2] - this.mInitialMotionY[paramInt2];
    return (bool1 && paramInt1 != 0) ? ((f1 * f1 + f2 * f2 <= (this.mTouchSlop * this.mTouchSlop)) ? false : bool2) : (bool1 ? ((Math.abs(f1) <= this.mTouchSlop) ? false : bool2) : ((paramInt1 != 0) ? ((Math.abs(f2) <= this.mTouchSlop) ? false : bool2) : false));
  }
  
  public boolean continueSettling(boolean paramBoolean) {
    if (this.mDragState == 2) {
      boolean bool2 = this.mScroller.computeScrollOffset();
      int i = this.mScroller.getCurrX();
      int j = this.mScroller.getCurrY();
      int k = i - this.mCapturedView.getLeft();
      int m = j - this.mCapturedView.getTop();
      if (k != 0)
        this.mCapturedView.offsetLeftAndRight(k); 
      if (m != 0)
        this.mCapturedView.offsetTopAndBottom(m); 
      if (k != 0 || m != 0)
        this.mCallback.onViewPositionChanged(this.mCapturedView, i, j, k, m); 
      boolean bool1 = bool2;
      if (bool2) {
        bool1 = bool2;
        if (i == this.mScroller.getFinalX()) {
          bool1 = bool2;
          if (j == this.mScroller.getFinalY()) {
            this.mScroller.abortAnimation();
            bool1 = false;
          } 
        } 
      } 
      if (!bool1)
        if (paramBoolean) {
          this.mParentView.post(this.mSetIdleRunnable);
        } else {
          setDragState(0);
        }  
    } 
    return (this.mDragState == 2);
  }
  
  public View findTopChildUnder(int paramInt1, int paramInt2) {
    for (int i = this.mParentView.getChildCount() - 1; i >= 0; i--) {
      View view = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(i));
      if (paramInt1 >= view.getLeft() && paramInt1 < view.getRight() && paramInt2 >= view.getTop() && paramInt2 < view.getBottom())
        return view; 
    } 
    return null;
  }
  
  public void flingCapturedView(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    if (!this.mReleaseInProgress)
      throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased"); 
    this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int)VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), (int)VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId), paramInt1, paramInt3, paramInt2, paramInt4);
    setDragState(2);
  }
  
  public int getActivePointerId() {
    return this.mActivePointerId;
  }
  
  public View getCapturedView() {
    return this.mCapturedView;
  }
  
  public int getEdgeSize() {
    return this.mEdgeSize;
  }
  
  public float getMinVelocity() {
    return this.mMinVelocity;
  }
  
  public int getTouchSlop() {
    return this.mTouchSlop;
  }
  
  public int getViewDragState() {
    return this.mDragState;
  }
  
  public boolean isCapturedViewUnder(int paramInt1, int paramInt2) {
    return isViewUnder(this.mCapturedView, paramInt1, paramInt2);
  }
  
  public boolean isEdgeTouched(int paramInt) {
    int j = this.mInitialEdgesTouched.length;
    for (int i = 0; i < j; i++) {
      if (isEdgeTouched(paramInt, i))
        return true; 
    } 
    return false;
  }
  
  public boolean isEdgeTouched(int paramInt1, int paramInt2) {
    return (isPointerDown(paramInt2) && (this.mInitialEdgesTouched[paramInt2] & paramInt1) != 0);
  }
  
  public boolean isPointerDown(int paramInt) {
    return ((this.mPointersDown & 1 << paramInt) != 0);
  }
  
  public boolean isViewUnder(View paramView, int paramInt1, int paramInt2) {
    return (paramView != null && paramInt1 >= paramView.getLeft() && paramInt1 < paramView.getRight() && paramInt2 >= paramView.getTop() && paramInt2 < paramView.getBottom());
  }
  
  public void processTouchEvent(MotionEvent paramMotionEvent) {
    View view;
    float f1;
    float f2;
    int k;
    int j = MotionEventCompat.getActionMasked(paramMotionEvent);
    int i = MotionEventCompat.getActionIndex(paramMotionEvent);
    if (j == 0)
      cancel(); 
    if (this.mVelocityTracker == null)
      this.mVelocityTracker = VelocityTracker.obtain(); 
    this.mVelocityTracker.addMovement(paramMotionEvent);
    switch (j) {
      default:
        return;
      case 0:
        f1 = paramMotionEvent.getX();
        f2 = paramMotionEvent.getY();
        i = MotionEventCompat.getPointerId(paramMotionEvent, 0);
        view = findTopChildUnder((int)f1, (int)f2);
        saveInitialMotion(f1, f2, i);
        tryCaptureViewForDrag(view, i);
        j = this.mInitialEdgesTouched[i];
        if ((this.mTrackingEdges & j) != 0) {
          this.mCallback.onEdgeTouched(this.mTrackingEdges & j, i);
          return;
        } 
      case 5:
        j = MotionEventCompat.getPointerId((MotionEvent)view, i);
        f1 = MotionEventCompat.getX((MotionEvent)view, i);
        f2 = MotionEventCompat.getY((MotionEvent)view, i);
        saveInitialMotion(f1, f2, j);
        if (this.mDragState == 0) {
          tryCaptureViewForDrag(findTopChildUnder((int)f1, (int)f2), j);
          i = this.mInitialEdgesTouched[j];
          if ((this.mTrackingEdges & i) != 0) {
            this.mCallback.onEdgeTouched(this.mTrackingEdges & i, j);
            return;
          } 
        } 
        if (isCapturedViewUnder((int)f1, (int)f2)) {
          tryCaptureViewForDrag(this.mCapturedView, j);
          return;
        } 
      case 2:
        if (this.mDragState == 1) {
          i = MotionEventCompat.findPointerIndex((MotionEvent)view, this.mActivePointerId);
          f1 = MotionEventCompat.getX((MotionEvent)view, i);
          f2 = MotionEventCompat.getY((MotionEvent)view, i);
          i = (int)(f1 - this.mLastMotionX[this.mActivePointerId]);
          j = (int)(f2 - this.mLastMotionY[this.mActivePointerId]);
          dragTo(this.mCapturedView.getLeft() + i, this.mCapturedView.getTop() + j, i, j);
          saveLastMotion((MotionEvent)view);
          return;
        } 
        j = MotionEventCompat.getPointerCount((MotionEvent)view);
        i = 0;
        while (true) {
          if (i < j) {
            int m = MotionEventCompat.getPointerId((MotionEvent)view, i);
            f1 = MotionEventCompat.getX((MotionEvent)view, i);
            f2 = MotionEventCompat.getY((MotionEvent)view, i);
            float f3 = f1 - this.mInitialMotionX[m];
            float f4 = f2 - this.mInitialMotionY[m];
            reportNewEdgeDrags(f3, f4, m);
            if (this.mDragState != 1) {
              View view1 = findTopChildUnder((int)f1, (int)f2);
              if (!checkTouchSlop(view1, f3, f4) || !tryCaptureViewForDrag(view1, m)) {
                i++;
                continue;
              } 
            } 
          } 
          saveLastMotion((MotionEvent)view);
          return;
        } 
      case 6:
        k = MotionEventCompat.getPointerId((MotionEvent)view, i);
        if (this.mDragState == 1 && k == this.mActivePointerId) {
          byte b = -1;
          int m = MotionEventCompat.getPointerCount((MotionEvent)view);
          i = 0;
          while (true) {
            j = b;
            if (i < m) {
              j = MotionEventCompat.getPointerId((MotionEvent)view, i);
              if (j != this.mActivePointerId) {
                f1 = MotionEventCompat.getX((MotionEvent)view, i);
                f2 = MotionEventCompat.getY((MotionEvent)view, i);
                if (findTopChildUnder((int)f1, (int)f2) == this.mCapturedView && tryCaptureViewForDrag(this.mCapturedView, j)) {
                  j = this.mActivePointerId;
                  break;
                } 
              } 
              i++;
              continue;
            } 
            break;
          } 
          if (j == -1)
            releaseViewForPointerUp(); 
        } 
        clearMotionHistory(k);
        return;
      case 1:
        if (this.mDragState == 1)
          releaseViewForPointerUp(); 
        cancel();
        return;
      case 3:
        break;
    } 
    if (this.mDragState == 1)
      dispatchViewReleased(0.0F, 0.0F); 
    cancel();
  }
  
  void setDragState(int paramInt) {
    this.mParentView.removeCallbacks(this.mSetIdleRunnable);
    if (this.mDragState != paramInt) {
      this.mDragState = paramInt;
      this.mCallback.onViewDragStateChanged(paramInt);
      if (this.mDragState == 0)
        this.mCapturedView = null; 
    } 
  }
  
  public void setEdgeTrackingEnabled(int paramInt) {
    this.mTrackingEdges = paramInt;
  }
  
  public void setMinVelocity(float paramFloat) {
    this.mMinVelocity = paramFloat;
  }
  
  public boolean settleCapturedViewAt(int paramInt1, int paramInt2) {
    if (!this.mReleaseInProgress)
      throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased"); 
    return forceSettleCapturedViewAt(paramInt1, paramInt2, (int)VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), (int)VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId));
  }
  
  public boolean shouldInterceptTouchEvent(MotionEvent paramMotionEvent) {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic getActionMasked : (Landroid/view/MotionEvent;)I
    //   4: istore #7
    //   6: aload_1
    //   7: invokestatic getActionIndex : (Landroid/view/MotionEvent;)I
    //   10: istore #6
    //   12: iload #7
    //   14: ifne -> 21
    //   17: aload_0
    //   18: invokevirtual cancel : ()V
    //   21: aload_0
    //   22: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   25: ifnonnull -> 35
    //   28: aload_0
    //   29: invokestatic obtain : ()Landroid/view/VelocityTracker;
    //   32: putfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   35: aload_0
    //   36: getfield mVelocityTracker : Landroid/view/VelocityTracker;
    //   39: aload_1
    //   40: invokevirtual addMovement : (Landroid/view/MotionEvent;)V
    //   43: iload #7
    //   45: tableswitch default -> 88, 0 -> 98, 1 -> 603, 2 -> 305, 3 -> 603, 4 -> 88, 5 -> 194, 6 -> 590
    //   88: aload_0
    //   89: getfield mDragState : I
    //   92: iconst_1
    //   93: if_icmpne -> 610
    //   96: iconst_1
    //   97: ireturn
    //   98: aload_1
    //   99: invokevirtual getX : ()F
    //   102: fstore_2
    //   103: aload_1
    //   104: invokevirtual getY : ()F
    //   107: fstore_3
    //   108: aload_1
    //   109: iconst_0
    //   110: invokestatic getPointerId : (Landroid/view/MotionEvent;I)I
    //   113: istore #6
    //   115: aload_0
    //   116: fload_2
    //   117: fload_3
    //   118: iload #6
    //   120: invokespecial saveInitialMotion : (FFI)V
    //   123: aload_0
    //   124: fload_2
    //   125: f2i
    //   126: fload_3
    //   127: f2i
    //   128: invokevirtual findTopChildUnder : (II)Landroid/view/View;
    //   131: astore_1
    //   132: aload_1
    //   133: aload_0
    //   134: getfield mCapturedView : Landroid/view/View;
    //   137: if_acmpne -> 156
    //   140: aload_0
    //   141: getfield mDragState : I
    //   144: iconst_2
    //   145: if_icmpne -> 156
    //   148: aload_0
    //   149: aload_1
    //   150: iload #6
    //   152: invokevirtual tryCaptureViewForDrag : (Landroid/view/View;I)Z
    //   155: pop
    //   156: aload_0
    //   157: getfield mInitialEdgesTouched : [I
    //   160: iload #6
    //   162: iaload
    //   163: istore #7
    //   165: aload_0
    //   166: getfield mTrackingEdges : I
    //   169: iload #7
    //   171: iand
    //   172: ifeq -> 88
    //   175: aload_0
    //   176: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   179: aload_0
    //   180: getfield mTrackingEdges : I
    //   183: iload #7
    //   185: iand
    //   186: iload #6
    //   188: invokevirtual onEdgeTouched : (II)V
    //   191: goto -> 88
    //   194: aload_1
    //   195: iload #6
    //   197: invokestatic getPointerId : (Landroid/view/MotionEvent;I)I
    //   200: istore #7
    //   202: aload_1
    //   203: iload #6
    //   205: invokestatic getX : (Landroid/view/MotionEvent;I)F
    //   208: fstore_2
    //   209: aload_1
    //   210: iload #6
    //   212: invokestatic getY : (Landroid/view/MotionEvent;I)F
    //   215: fstore_3
    //   216: aload_0
    //   217: fload_2
    //   218: fload_3
    //   219: iload #7
    //   221: invokespecial saveInitialMotion : (FFI)V
    //   224: aload_0
    //   225: getfield mDragState : I
    //   228: ifne -> 269
    //   231: aload_0
    //   232: getfield mInitialEdgesTouched : [I
    //   235: iload #7
    //   237: iaload
    //   238: istore #6
    //   240: aload_0
    //   241: getfield mTrackingEdges : I
    //   244: iload #6
    //   246: iand
    //   247: ifeq -> 88
    //   250: aload_0
    //   251: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   254: aload_0
    //   255: getfield mTrackingEdges : I
    //   258: iload #6
    //   260: iand
    //   261: iload #7
    //   263: invokevirtual onEdgeTouched : (II)V
    //   266: goto -> 88
    //   269: aload_0
    //   270: getfield mDragState : I
    //   273: iconst_2
    //   274: if_icmpne -> 88
    //   277: aload_0
    //   278: fload_2
    //   279: f2i
    //   280: fload_3
    //   281: f2i
    //   282: invokevirtual findTopChildUnder : (II)Landroid/view/View;
    //   285: astore_1
    //   286: aload_1
    //   287: aload_0
    //   288: getfield mCapturedView : Landroid/view/View;
    //   291: if_acmpne -> 88
    //   294: aload_0
    //   295: aload_1
    //   296: iload #7
    //   298: invokevirtual tryCaptureViewForDrag : (Landroid/view/View;I)Z
    //   301: pop
    //   302: goto -> 88
    //   305: aload_0
    //   306: getfield mInitialMotionX : [F
    //   309: ifnull -> 88
    //   312: aload_0
    //   313: getfield mInitialMotionY : [F
    //   316: ifnull -> 88
    //   319: aload_1
    //   320: invokestatic getPointerCount : (Landroid/view/MotionEvent;)I
    //   323: istore #8
    //   325: iconst_0
    //   326: istore #6
    //   328: iload #6
    //   330: iload #8
    //   332: if_icmpge -> 533
    //   335: aload_1
    //   336: iload #6
    //   338: invokestatic getPointerId : (Landroid/view/MotionEvent;I)I
    //   341: istore #9
    //   343: aload_1
    //   344: iload #6
    //   346: invokestatic getX : (Landroid/view/MotionEvent;I)F
    //   349: fstore_2
    //   350: aload_1
    //   351: iload #6
    //   353: invokestatic getY : (Landroid/view/MotionEvent;I)F
    //   356: fstore_3
    //   357: fload_2
    //   358: aload_0
    //   359: getfield mInitialMotionX : [F
    //   362: iload #9
    //   364: faload
    //   365: fsub
    //   366: fstore #4
    //   368: fload_3
    //   369: aload_0
    //   370: getfield mInitialMotionY : [F
    //   373: iload #9
    //   375: faload
    //   376: fsub
    //   377: fstore #5
    //   379: aload_0
    //   380: fload_2
    //   381: f2i
    //   382: fload_3
    //   383: f2i
    //   384: invokevirtual findTopChildUnder : (II)Landroid/view/View;
    //   387: astore #16
    //   389: aload #16
    //   391: ifnull -> 541
    //   394: aload_0
    //   395: aload #16
    //   397: fload #4
    //   399: fload #5
    //   401: invokespecial checkTouchSlop : (Landroid/view/View;FF)Z
    //   404: ifeq -> 541
    //   407: iconst_1
    //   408: istore #7
    //   410: iload #7
    //   412: ifeq -> 547
    //   415: aload #16
    //   417: invokevirtual getLeft : ()I
    //   420: istore #10
    //   422: fload #4
    //   424: f2i
    //   425: istore #11
    //   427: aload_0
    //   428: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   431: aload #16
    //   433: iload #10
    //   435: iload #11
    //   437: iadd
    //   438: fload #4
    //   440: f2i
    //   441: invokevirtual clampViewPositionHorizontal : (Landroid/view/View;II)I
    //   444: istore #11
    //   446: aload #16
    //   448: invokevirtual getTop : ()I
    //   451: istore #12
    //   453: fload #5
    //   455: f2i
    //   456: istore #13
    //   458: aload_0
    //   459: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   462: aload #16
    //   464: iload #12
    //   466: iload #13
    //   468: iadd
    //   469: fload #5
    //   471: f2i
    //   472: invokevirtual clampViewPositionVertical : (Landroid/view/View;II)I
    //   475: istore #13
    //   477: aload_0
    //   478: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   481: aload #16
    //   483: invokevirtual getViewHorizontalDragRange : (Landroid/view/View;)I
    //   486: istore #14
    //   488: aload_0
    //   489: getfield mCallback : Landroid/support/v4/widget/ViewDragHelper$Callback;
    //   492: aload #16
    //   494: invokevirtual getViewVerticalDragRange : (Landroid/view/View;)I
    //   497: istore #15
    //   499: iload #14
    //   501: ifeq -> 516
    //   504: iload #14
    //   506: ifle -> 547
    //   509: iload #11
    //   511: iload #10
    //   513: if_icmpne -> 547
    //   516: iload #15
    //   518: ifeq -> 533
    //   521: iload #15
    //   523: ifle -> 547
    //   526: iload #13
    //   528: iload #12
    //   530: if_icmpne -> 547
    //   533: aload_0
    //   534: aload_1
    //   535: invokespecial saveLastMotion : (Landroid/view/MotionEvent;)V
    //   538: goto -> 88
    //   541: iconst_0
    //   542: istore #7
    //   544: goto -> 410
    //   547: aload_0
    //   548: fload #4
    //   550: fload #5
    //   552: iload #9
    //   554: invokespecial reportNewEdgeDrags : (FFI)V
    //   557: aload_0
    //   558: getfield mDragState : I
    //   561: iconst_1
    //   562: if_icmpeq -> 533
    //   565: iload #7
    //   567: ifeq -> 581
    //   570: aload_0
    //   571: aload #16
    //   573: iload #9
    //   575: invokevirtual tryCaptureViewForDrag : (Landroid/view/View;I)Z
    //   578: ifne -> 533
    //   581: iload #6
    //   583: iconst_1
    //   584: iadd
    //   585: istore #6
    //   587: goto -> 328
    //   590: aload_0
    //   591: aload_1
    //   592: iload #6
    //   594: invokestatic getPointerId : (Landroid/view/MotionEvent;I)I
    //   597: invokespecial clearMotionHistory : (I)V
    //   600: goto -> 88
    //   603: aload_0
    //   604: invokevirtual cancel : ()V
    //   607: goto -> 88
    //   610: iconst_0
    //   611: ireturn
  }
  
  public boolean smoothSlideViewTo(View paramView, int paramInt1, int paramInt2) {
    this.mCapturedView = paramView;
    this.mActivePointerId = -1;
    boolean bool = forceSettleCapturedViewAt(paramInt1, paramInt2, 0, 0);
    if (!bool && this.mDragState == 0 && this.mCapturedView != null)
      this.mCapturedView = null; 
    return bool;
  }
  
  boolean tryCaptureViewForDrag(View paramView, int paramInt) {
    if (paramView == this.mCapturedView && this.mActivePointerId == paramInt)
      return true; 
    if (paramView != null && this.mCallback.tryCaptureView(paramView, paramInt)) {
      this.mActivePointerId = paramInt;
      captureChildView(paramView, paramInt);
      return true;
    } 
    return false;
  }
  
  public static abstract class Callback {
    public int clampViewPositionHorizontal(View param1View, int param1Int1, int param1Int2) {
      return 0;
    }
    
    public int clampViewPositionVertical(View param1View, int param1Int1, int param1Int2) {
      return 0;
    }
    
    public int getOrderedChildIndex(int param1Int) {
      return param1Int;
    }
    
    public int getViewHorizontalDragRange(View param1View) {
      return 0;
    }
    
    public int getViewVerticalDragRange(View param1View) {
      return 0;
    }
    
    public void onEdgeDragStarted(int param1Int1, int param1Int2) {}
    
    public boolean onEdgeLock(int param1Int) {
      return false;
    }
    
    public void onEdgeTouched(int param1Int1, int param1Int2) {}
    
    public void onViewCaptured(View param1View, int param1Int) {}
    
    public void onViewDragStateChanged(int param1Int) {}
    
    public void onViewPositionChanged(View param1View, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {}
    
    public void onViewReleased(View param1View, float param1Float1, float param1Float2) {}
    
    public abstract boolean tryCaptureView(View param1View, int param1Int);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\widget\ViewDragHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */