package android.support.v7.app;

import android.app.Notification;
import android.media.session.MediaSession;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;

class NotificationCompatImpl21 {
  public static void addMediaStyle(NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor, int[] paramArrayOfint, Object paramObject) {
    Notification.MediaStyle mediaStyle = new Notification.MediaStyle(paramNotificationBuilderWithBuilderAccessor.getBuilder());
    if (paramArrayOfint != null)
      mediaStyle.setShowActionsInCompactView(paramArrayOfint); 
    if (paramObject != null)
      mediaStyle.setMediaSession((MediaSession.Token)paramObject); 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\app\NotificationCompatImpl21.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */