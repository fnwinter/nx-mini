package android.support.v4.view.animation;

import android.graphics.Path;
import android.view.animation.Interpolator;

class PathInterpolatorCompatBase {
  public static Interpolator create(float paramFloat1, float paramFloat2) {
    return new PathInterpolatorDonut(paramFloat1, paramFloat2);
  }
  
  public static Interpolator create(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    return new PathInterpolatorDonut(paramFloat1, paramFloat2, paramFloat3, paramFloat4);
  }
  
  public static Interpolator create(Path paramPath) {
    return new PathInterpolatorDonut(paramPath);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\view\animation\PathInterpolatorCompatBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */