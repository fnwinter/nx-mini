package android.support.v4.media.session;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.view.KeyEvent;
import java.util.List;

public class MediaButtonReceiver extends BroadcastReceiver {
  public static KeyEvent handleIntent(MediaSessionCompat paramMediaSessionCompat, Intent paramIntent) {
    if (paramMediaSessionCompat == null || paramIntent == null || !"android.intent.action.MEDIA_BUTTON".equals(paramIntent.getAction()) || !paramIntent.hasExtra("android.intent.extra.KEY_EVENT"))
      return null; 
    KeyEvent keyEvent = (KeyEvent)paramIntent.getParcelableExtra("android.intent.extra.KEY_EVENT");
    paramMediaSessionCompat.getController().dispatchMediaButtonEvent(keyEvent);
    return keyEvent;
  }
  
  public void onReceive(Context paramContext, Intent paramIntent) {
    Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
    intent.setPackage(paramContext.getPackageName());
    List<ResolveInfo> list = paramContext.getPackageManager().queryIntentServices(intent, 0);
    if (list.size() != 1)
      throw new IllegalStateException("Expected 1 Service that handles android.intent.action.MEDIA_BUTTON, found " + list.size()); 
    ResolveInfo resolveInfo = list.get(0);
    paramIntent.setComponent(new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name));
    paramContext.startService(paramIntent);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\media\session\MediaButtonReceiver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */