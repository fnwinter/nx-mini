package android.support.v4.media.session;

import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;

class MediaSessionCompatApi8 {
  public static void registerMediaButtonEventReceiver(Context paramContext, ComponentName paramComponentName) {
    ((AudioManager)paramContext.getSystemService("audio")).registerMediaButtonEventReceiver(paramComponentName);
  }
  
  public static void unregisterMediaButtonEventReceiver(Context paramContext, ComponentName paramComponentName) {
    ((AudioManager)paramContext.getSystemService("audio")).unregisterMediaButtonEventReceiver(paramComponentName);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\media\session\MediaSessionCompatApi8.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */