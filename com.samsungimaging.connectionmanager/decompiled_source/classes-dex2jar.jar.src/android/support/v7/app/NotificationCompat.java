package android.support.v7.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaSessionCompat;

public class NotificationCompat extends NotificationCompat {
  private static void addBigMediaStyleToBuilderJellybean(Notification paramNotification, NotificationCompat.Builder paramBuilder) {
    if (paramBuilder.mStyle instanceof MediaStyle) {
      MediaStyle mediaStyle = (MediaStyle)paramBuilder.mStyle;
      NotificationCompatImplBase.overrideBigContentView(paramNotification, paramBuilder.mContext, paramBuilder.mContentTitle, paramBuilder.mContentText, paramBuilder.mContentInfo, paramBuilder.mNumber, paramBuilder.mLargeIcon, paramBuilder.mSubText, paramBuilder.mUseChronometer, paramBuilder.mNotification.when, paramBuilder.mActions, mediaStyle.mShowCancelButton, mediaStyle.mCancelButtonIntent);
    } 
  }
  
  private static void addMediaStyleToBuilderIcs(NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor, NotificationCompat.Builder paramBuilder) {
    if (paramBuilder.mStyle instanceof MediaStyle) {
      MediaStyle mediaStyle = (MediaStyle)paramBuilder.mStyle;
      NotificationCompatImplBase.overrideContentView(paramNotificationBuilderWithBuilderAccessor, paramBuilder.mContext, paramBuilder.mContentTitle, paramBuilder.mContentText, paramBuilder.mContentInfo, paramBuilder.mNumber, paramBuilder.mLargeIcon, paramBuilder.mSubText, paramBuilder.mUseChronometer, paramBuilder.mNotification.when, paramBuilder.mActions, mediaStyle.mActionsToShowInCompact, mediaStyle.mShowCancelButton, mediaStyle.mCancelButtonIntent);
    } 
  }
  
  private static void addMediaStyleToBuilderLollipop(NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor, NotificationCompat.Style paramStyle) {
    if (paramStyle instanceof MediaStyle) {
      paramStyle = paramStyle;
      int[] arrayOfInt = ((MediaStyle)paramStyle).mActionsToShowInCompact;
      if (((MediaStyle)paramStyle).mToken != null) {
        Object object = ((MediaStyle)paramStyle).mToken.getToken();
      } else {
        paramStyle = null;
      } 
      NotificationCompatImpl21.addMediaStyle(paramNotificationBuilderWithBuilderAccessor, arrayOfInt, paramStyle);
    } 
  }
  
  public static class Builder extends NotificationCompat.Builder {
    public Builder(Context param1Context) {
      super(param1Context);
    }
    
    protected NotificationCompat.BuilderExtender getExtender() {
      return (Build.VERSION.SDK_INT >= 21) ? new NotificationCompat.LollipopExtender() : ((Build.VERSION.SDK_INT >= 16) ? new NotificationCompat.JellybeanExtender() : ((Build.VERSION.SDK_INT >= 14) ? new NotificationCompat.IceCreamSandwichExtender() : super.getExtender()));
    }
  }
  
  private static class IceCreamSandwichExtender extends NotificationCompat.BuilderExtender {
    private IceCreamSandwichExtender() {}
    
    public Notification build(NotificationCompat.Builder param1Builder, NotificationBuilderWithBuilderAccessor param1NotificationBuilderWithBuilderAccessor) {
      NotificationCompat.addMediaStyleToBuilderIcs(param1NotificationBuilderWithBuilderAccessor, param1Builder);
      return param1NotificationBuilderWithBuilderAccessor.build();
    }
  }
  
  private static class JellybeanExtender extends NotificationCompat.BuilderExtender {
    private JellybeanExtender() {}
    
    public Notification build(NotificationCompat.Builder param1Builder, NotificationBuilderWithBuilderAccessor param1NotificationBuilderWithBuilderAccessor) {
      NotificationCompat.addMediaStyleToBuilderIcs(param1NotificationBuilderWithBuilderAccessor, param1Builder);
      Notification notification = param1NotificationBuilderWithBuilderAccessor.build();
      NotificationCompat.addBigMediaStyleToBuilderJellybean(notification, param1Builder);
      return notification;
    }
  }
  
  private static class LollipopExtender extends NotificationCompat.BuilderExtender {
    private LollipopExtender() {}
    
    public Notification build(NotificationCompat.Builder param1Builder, NotificationBuilderWithBuilderAccessor param1NotificationBuilderWithBuilderAccessor) {
      NotificationCompat.addMediaStyleToBuilderLollipop(param1NotificationBuilderWithBuilderAccessor, param1Builder.mStyle);
      return param1NotificationBuilderWithBuilderAccessor.build();
    }
  }
  
  public static class MediaStyle extends NotificationCompat.Style {
    int[] mActionsToShowInCompact = null;
    
    PendingIntent mCancelButtonIntent;
    
    boolean mShowCancelButton;
    
    MediaSessionCompat.Token mToken;
    
    public MediaStyle() {}
    
    public MediaStyle(NotificationCompat.Builder param1Builder) {
      setBuilder(param1Builder);
    }
    
    public MediaStyle setCancelButtonIntent(PendingIntent param1PendingIntent) {
      this.mCancelButtonIntent = param1PendingIntent;
      return this;
    }
    
    public MediaStyle setMediaSession(MediaSessionCompat.Token param1Token) {
      this.mToken = param1Token;
      return this;
    }
    
    public MediaStyle setShowActionsInCompactView(int... param1VarArgs) {
      this.mActionsToShowInCompact = param1VarArgs;
      return this;
    }
    
    public MediaStyle setShowCancelButton(boolean param1Boolean) {
      this.mShowCancelButton = param1Boolean;
      return this;
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\app\NotificationCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */