package android.support.v4.animation;

import android.os.Build;
import android.view.View;

public abstract class AnimatorCompatHelper {
  static AnimatorProvider IMPL = new DonutAnimatorCompatProvider();
  
  public static void clearInterpolator(View paramView) {
    IMPL.clearInterpolator(paramView);
  }
  
  public static ValueAnimatorCompat emptyValueAnimator() {
    return IMPL.emptyValueAnimator();
  }
  
  static {
    if (Build.VERSION.SDK_INT >= 12) {
      IMPL = new HoneycombMr1AnimatorCompatProvider();
      return;
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\animation\AnimatorCompatHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */