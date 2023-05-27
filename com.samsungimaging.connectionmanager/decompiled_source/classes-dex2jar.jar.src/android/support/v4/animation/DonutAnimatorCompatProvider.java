package android.support.v4.animation;

import android.view.View;
import java.util.ArrayList;
import java.util.List;

class DonutAnimatorCompatProvider implements AnimatorProvider {
  public void clearInterpolator(View paramView) {}
  
  public ValueAnimatorCompat emptyValueAnimator() {
    return new DonutFloatValueAnimator();
  }
  
  private static class DonutFloatValueAnimator implements ValueAnimatorCompat {
    private long mDuration = 200L;
    
    private boolean mEnded = false;
    
    private float mFraction = 0.0F;
    
    List<AnimatorListenerCompat> mListeners = new ArrayList<AnimatorListenerCompat>();
    
    private Runnable mLoopRunnable = new Runnable() {
        public void run() {
          float f = (float)(DonutAnimatorCompatProvider.DonutFloatValueAnimator.this.getTime() - DonutAnimatorCompatProvider.DonutFloatValueAnimator.this.mStartTime) * 1.0F / (float)DonutAnimatorCompatProvider.DonutFloatValueAnimator.this.mDuration;
          if (f > 1.0F || DonutAnimatorCompatProvider.DonutFloatValueAnimator.this.mTarget.getParent() == null)
            f = 1.0F; 
          DonutAnimatorCompatProvider.DonutFloatValueAnimator.access$302(DonutAnimatorCompatProvider.DonutFloatValueAnimator.this, f);
          DonutAnimatorCompatProvider.DonutFloatValueAnimator.this.notifyUpdateListeners();
          if (DonutAnimatorCompatProvider.DonutFloatValueAnimator.this.mFraction >= 1.0F) {
            DonutAnimatorCompatProvider.DonutFloatValueAnimator.this.dispatchEnd();
            return;
          } 
          DonutAnimatorCompatProvider.DonutFloatValueAnimator.this.mTarget.postDelayed(DonutAnimatorCompatProvider.DonutFloatValueAnimator.this.mLoopRunnable, 16L);
        }
      };
    
    private long mStartTime;
    
    private boolean mStarted = false;
    
    View mTarget;
    
    List<AnimatorUpdateListenerCompat> mUpdateListeners = new ArrayList<AnimatorUpdateListenerCompat>();
    
    private void dispatchCancel() {
      for (int i = this.mListeners.size() - 1; i >= 0; i--)
        ((AnimatorListenerCompat)this.mListeners.get(i)).onAnimationCancel(this); 
    }
    
    private void dispatchEnd() {
      for (int i = this.mListeners.size() - 1; i >= 0; i--)
        ((AnimatorListenerCompat)this.mListeners.get(i)).onAnimationEnd(this); 
    }
    
    private void dispatchStart() {
      for (int i = this.mListeners.size() - 1; i >= 0; i--)
        ((AnimatorListenerCompat)this.mListeners.get(i)).onAnimationStart(this); 
    }
    
    private long getTime() {
      return this.mTarget.getDrawingTime();
    }
    
    private void notifyUpdateListeners() {
      for (int i = this.mUpdateListeners.size() - 1; i >= 0; i--)
        ((AnimatorUpdateListenerCompat)this.mUpdateListeners.get(i)).onAnimationUpdate(this); 
    }
    
    public void addListener(AnimatorListenerCompat param1AnimatorListenerCompat) {
      this.mListeners.add(param1AnimatorListenerCompat);
    }
    
    public void addUpdateListener(AnimatorUpdateListenerCompat param1AnimatorUpdateListenerCompat) {
      this.mUpdateListeners.add(param1AnimatorUpdateListenerCompat);
    }
    
    public void cancel() {
      if (this.mEnded)
        return; 
      this.mEnded = true;
      if (this.mStarted)
        dispatchCancel(); 
      dispatchEnd();
    }
    
    public float getAnimatedFraction() {
      return this.mFraction;
    }
    
    public void setDuration(long param1Long) {
      if (!this.mStarted)
        this.mDuration = param1Long; 
    }
    
    public void setTarget(View param1View) {
      this.mTarget = param1View;
    }
    
    public void start() {
      if (this.mStarted)
        return; 
      this.mStarted = true;
      dispatchStart();
      this.mFraction = 0.0F;
      this.mStartTime = getTime();
      this.mTarget.postDelayed(this.mLoopRunnable, 16L);
    }
  }
  
  class null implements Runnable {
    public void run() {
      float f = (float)(this.this$0.getTime() - this.this$0.mStartTime) * 1.0F / (float)this.this$0.mDuration;
      if (f > 1.0F || this.this$0.mTarget.getParent() == null)
        f = 1.0F; 
      DonutAnimatorCompatProvider.DonutFloatValueAnimator.access$302(this.this$0, f);
      this.this$0.notifyUpdateListeners();
      if (this.this$0.mFraction >= 1.0F) {
        this.this$0.dispatchEnd();
        return;
      } 
      this.this$0.mTarget.postDelayed(this.this$0.mLoopRunnable, 16L);
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\animation\DonutAnimatorCompatProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */