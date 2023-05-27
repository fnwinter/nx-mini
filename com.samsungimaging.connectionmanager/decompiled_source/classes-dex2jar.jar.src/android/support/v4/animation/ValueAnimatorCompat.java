package android.support.v4.animation;

import android.view.View;

public interface ValueAnimatorCompat {
  void addListener(AnimatorListenerCompat paramAnimatorListenerCompat);
  
  void addUpdateListener(AnimatorUpdateListenerCompat paramAnimatorUpdateListenerCompat);
  
  void cancel();
  
  float getAnimatedFraction();
  
  void setDuration(long paramLong);
  
  void setTarget(View paramView);
  
  void start();
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\animation\ValueAnimatorCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */