package android.support.v4.app;

import android.app.Activity;
import android.app.ActivityOptions;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

class ActivityOptionsCompat21 {
  private final ActivityOptions mActivityOptions;
  
  private ActivityOptionsCompat21(ActivityOptions paramActivityOptions) {
    this.mActivityOptions = paramActivityOptions;
  }
  
  public static ActivityOptionsCompat21 makeSceneTransitionAnimation(Activity paramActivity, View paramView, String paramString) {
    return new ActivityOptionsCompat21(ActivityOptions.makeSceneTransitionAnimation(paramActivity, paramView, paramString));
  }
  
  public static ActivityOptionsCompat21 makeSceneTransitionAnimation(Activity paramActivity, View[] paramArrayOfView, String[] paramArrayOfString) {
    Pair[] arrayOfPair = null;
    if (paramArrayOfView != null) {
      Pair[] arrayOfPair1 = new Pair[paramArrayOfView.length];
      int i = 0;
      while (true) {
        arrayOfPair = arrayOfPair1;
        if (i < arrayOfPair1.length) {
          arrayOfPair1[i] = Pair.create(paramArrayOfView[i], paramArrayOfString[i]);
          i++;
          continue;
        } 
        break;
      } 
    } 
    return new ActivityOptionsCompat21(ActivityOptions.makeSceneTransitionAnimation(paramActivity, arrayOfPair));
  }
  
  public Bundle toBundle() {
    return this.mActivityOptions.toBundle();
  }
  
  public void update(ActivityOptionsCompat21 paramActivityOptionsCompat21) {
    this.mActivityOptions.update(paramActivityOptionsCompat21.mActivityOptions);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\app\ActivityOptionsCompat21.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */