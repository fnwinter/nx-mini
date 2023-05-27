package com.samsungimaging.connectionmanager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class ScaleAnimationRelativeLayout extends RelativeLayout {
  private ScaleAnimation mScaleInAnimation = null;
  
  private ScaleAnimation mScaleOutAnimation = null;
  
  public ScaleAnimationRelativeLayout(Context paramContext) {
    super(paramContext);
    init();
  }
  
  public ScaleAnimationRelativeLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  public ScaleAnimationRelativeLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }
  
  private void init() {
    this.mScaleInAnimation = new ScaleAnimation(1.0F, 0.9F, 1.0F, 0.9F, 1, 0.5F, 1, 0.5F);
    this.mScaleInAnimation.setDuration(100L);
    this.mScaleInAnimation.setFillAfter(true);
    this.mScaleOutAnimation = new ScaleAnimation(0.9F, 1.0F, 0.9F, 1.0F, 1, 0.5F, 1, 0.5F);
    this.mScaleOutAnimation.setDuration(100L);
    this.mScaleOutAnimation.setFillAfter(true);
    this.mScaleOutAnimation.setAnimationListener(new Animation.AnimationListener() {
          public void onAnimationEnd(Animation param1Animation) {
            param1Animation.setFillAfter(false);
          }
          
          public void onAnimationRepeat(Animation param1Animation) {}
          
          public void onAnimationStart(Animation param1Animation) {}
        });
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    switch (paramMotionEvent.getAction()) {
      default:
        return super.onTouchEvent(paramMotionEvent);
      case 0:
        startAnimation((Animation)this.mScaleInAnimation);
      case 1:
        startAnimation((Animation)this.mScaleOutAnimation);
      case 3:
        break;
    } 
    clearAnimation();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\view\ScaleAnimationRelativeLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */