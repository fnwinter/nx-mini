package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;

public class ActivityOptionsCompat {
  public static ActivityOptionsCompat makeCustomAnimation(Context paramContext, int paramInt1, int paramInt2) {
    return (Build.VERSION.SDK_INT >= 16) ? new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeCustomAnimation(paramContext, paramInt1, paramInt2)) : new ActivityOptionsCompat();
  }
  
  public static ActivityOptionsCompat makeScaleUpAnimation(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    return (Build.VERSION.SDK_INT >= 16) ? new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeScaleUpAnimation(paramView, paramInt1, paramInt2, paramInt3, paramInt4)) : new ActivityOptionsCompat();
  }
  
  public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity paramActivity, View paramView, String paramString) {
    return (Build.VERSION.SDK_INT >= 21) ? new ActivityOptionsImpl21(ActivityOptionsCompat21.makeSceneTransitionAnimation(paramActivity, paramView, paramString)) : new ActivityOptionsCompat();
  }
  
  public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity paramActivity, Pair<View, String>... paramVarArgs) {
    if (Build.VERSION.SDK_INT >= 21) {
      View[] arrayOfView = null;
      String[] arrayOfString = null;
      if (paramVarArgs != null) {
        View[] arrayOfView1 = new View[paramVarArgs.length];
        String[] arrayOfString1 = new String[paramVarArgs.length];
        int i = 0;
        while (true) {
          arrayOfString = arrayOfString1;
          arrayOfView = arrayOfView1;
          if (i < paramVarArgs.length) {
            arrayOfView1[i] = (View)(paramVarArgs[i]).first;
            arrayOfString1[i] = (String)(paramVarArgs[i]).second;
            i++;
            continue;
          } 
          break;
        } 
      } 
      return new ActivityOptionsImpl21(ActivityOptionsCompat21.makeSceneTransitionAnimation(paramActivity, arrayOfView, arrayOfString));
    } 
    return new ActivityOptionsCompat();
  }
  
  public static ActivityOptionsCompat makeThumbnailScaleUpAnimation(View paramView, Bitmap paramBitmap, int paramInt1, int paramInt2) {
    return (Build.VERSION.SDK_INT >= 16) ? new ActivityOptionsImplJB(ActivityOptionsCompatJB.makeThumbnailScaleUpAnimation(paramView, paramBitmap, paramInt1, paramInt2)) : new ActivityOptionsCompat();
  }
  
  public Bundle toBundle() {
    return null;
  }
  
  public void update(ActivityOptionsCompat paramActivityOptionsCompat) {}
  
  private static class ActivityOptionsImpl21 extends ActivityOptionsCompat {
    private final ActivityOptionsCompat21 mImpl;
    
    ActivityOptionsImpl21(ActivityOptionsCompat21 param1ActivityOptionsCompat21) {
      this.mImpl = param1ActivityOptionsCompat21;
    }
    
    public Bundle toBundle() {
      return this.mImpl.toBundle();
    }
    
    public void update(ActivityOptionsCompat param1ActivityOptionsCompat) {
      if (param1ActivityOptionsCompat instanceof ActivityOptionsImpl21) {
        param1ActivityOptionsCompat = param1ActivityOptionsCompat;
        this.mImpl.update(((ActivityOptionsImpl21)param1ActivityOptionsCompat).mImpl);
      } 
    }
  }
  
  private static class ActivityOptionsImplJB extends ActivityOptionsCompat {
    private final ActivityOptionsCompatJB mImpl;
    
    ActivityOptionsImplJB(ActivityOptionsCompatJB param1ActivityOptionsCompatJB) {
      this.mImpl = param1ActivityOptionsCompatJB;
    }
    
    public Bundle toBundle() {
      return this.mImpl.toBundle();
    }
    
    public void update(ActivityOptionsCompat param1ActivityOptionsCompat) {
      if (param1ActivityOptionsCompat instanceof ActivityOptionsImplJB) {
        param1ActivityOptionsCompat = param1ActivityOptionsCompat;
        this.mImpl.update(((ActivityOptionsImplJB)param1ActivityOptionsCompat).mImpl);
      } 
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\app\ActivityOptionsCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */