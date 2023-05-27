package android.support.v4.app;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

public class BundleCompat {
  public static IBinder getBinder(Bundle paramBundle, String paramString) {
    return (Build.VERSION.SDK_INT >= 18) ? BundleCompatJellybeanMR2.getBinder(paramBundle, paramString) : BundleCompatDonut.getBinder(paramBundle, paramString);
  }
  
  public static void putBinder(Bundle paramBundle, String paramString, IBinder paramIBinder) {
    if (Build.VERSION.SDK_INT >= 18) {
      BundleCompatJellybeanMR2.putBinder(paramBundle, paramString, paramIBinder);
      return;
    } 
    BundleCompatDonut.putBinder(paramBundle, paramString, paramIBinder);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\app\BundleCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */