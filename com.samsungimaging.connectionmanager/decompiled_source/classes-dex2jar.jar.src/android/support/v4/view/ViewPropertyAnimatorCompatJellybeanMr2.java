package android.support.v4.view;

import android.view.View;
import android.view.animation.Interpolator;

class ViewPropertyAnimatorCompatJellybeanMr2 {
  public static Interpolator getInterpolator(View paramView) {
    return (Interpolator)paramView.animate().getInterpolator();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\view\ViewPropertyAnimatorCompatJellybeanMr2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */