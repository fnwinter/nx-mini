package android.support.v7.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompatBase;
import android.support.v7.appcompat.R;
import android.widget.RemoteViews;
import java.text.NumberFormat;
import java.util.List;

class NotificationCompatImplBase {
  static final int MAX_MEDIA_BUTTONS = 5;
  
  static final int MAX_MEDIA_BUTTONS_IN_COMPACT = 3;
  
  private static RemoteViews applyStandardTemplate(Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, int paramInt1, Bitmap paramBitmap, CharSequence paramCharSequence4, boolean paramBoolean1, long paramLong, int paramInt2, boolean paramBoolean2) {
    RemoteViews remoteViews = new RemoteViews(paramContext.getPackageName(), paramInt2);
    paramInt2 = 0;
    boolean bool = false;
    if (paramBitmap != null && Build.VERSION.SDK_INT >= 16) {
      remoteViews.setViewVisibility(R.id.icon, 0);
      remoteViews.setImageViewBitmap(R.id.icon, paramBitmap);
    } else {
      remoteViews.setViewVisibility(R.id.icon, 8);
    } 
    if (paramCharSequence1 != null)
      remoteViews.setTextViewText(R.id.title, paramCharSequence1); 
    if (paramCharSequence2 != null) {
      remoteViews.setTextViewText(R.id.text, paramCharSequence2);
      paramInt2 = 1;
    } 
    if (paramCharSequence3 != null) {
      remoteViews.setTextViewText(R.id.info, paramCharSequence3);
      remoteViews.setViewVisibility(R.id.info, 0);
      paramInt1 = 1;
    } else if (paramInt1 > 0) {
      if (paramInt1 > paramContext.getResources().getInteger(R.integer.status_bar_notification_info_maxnum)) {
        remoteViews.setTextViewText(R.id.info, paramContext.getResources().getString(R.string.status_bar_notification_info_overflow));
      } else {
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        remoteViews.setTextViewText(R.id.info, numberFormat.format(paramInt1));
      } 
      remoteViews.setViewVisibility(R.id.info, 0);
      paramInt1 = 1;
    } else {
      remoteViews.setViewVisibility(R.id.info, 8);
      paramInt1 = paramInt2;
    } 
    paramInt2 = bool;
    if (paramCharSequence4 != null) {
      paramInt2 = bool;
      if (Build.VERSION.SDK_INT >= 16) {
        remoteViews.setTextViewText(R.id.text, paramCharSequence4);
        if (paramCharSequence2 != null) {
          remoteViews.setTextViewText(R.id.text2, paramCharSequence2);
          remoteViews.setViewVisibility(R.id.text2, 0);
          paramInt2 = 1;
        } else {
          remoteViews.setViewVisibility(R.id.text2, 8);
          paramInt2 = bool;
        } 
      } 
    } 
    if (paramInt2 != 0 && Build.VERSION.SDK_INT >= 16) {
      if (paramBoolean2) {
        float f = paramContext.getResources().getDimensionPixelSize(R.dimen.notification_subtext_size);
        remoteViews.setTextViewTextSize(R.id.text, 0, f);
      } 
      remoteViews.setViewPadding(R.id.line1, 0, 0, 0, 0);
    } 
    if (paramLong != 0L)
      if (paramBoolean1) {
        remoteViews.setViewVisibility(R.id.chronometer, 0);
        remoteViews.setLong(R.id.chronometer, "setBase", SystemClock.elapsedRealtime() - System.currentTimeMillis() + paramLong);
        remoteViews.setBoolean(R.id.chronometer, "setStarted", true);
      } else {
        remoteViews.setViewVisibility(R.id.time, 0);
        remoteViews.setLong(R.id.time, "setTime", paramLong);
      }  
    paramInt2 = R.id.line3;
    if (paramInt1 != 0) {
      paramInt1 = 0;
      remoteViews.setViewVisibility(paramInt2, paramInt1);
      return remoteViews;
    } 
    paramInt1 = 8;
    remoteViews.setViewVisibility(paramInt2, paramInt1);
    return remoteViews;
  }
  
  private static <T extends NotificationCompatBase.Action> RemoteViews generateBigContentView(Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, int paramInt, Bitmap paramBitmap, CharSequence paramCharSequence4, boolean paramBoolean1, long paramLong, List<T> paramList, boolean paramBoolean2, PendingIntent paramPendingIntent) {
    int i = Math.min(paramList.size(), 5);
    RemoteViews remoteViews = applyStandardTemplate(paramContext, paramCharSequence1, paramCharSequence2, paramCharSequence3, paramInt, paramBitmap, paramCharSequence4, paramBoolean1, paramLong, getBigLayoutResource(i), false);
    remoteViews.removeAllViews(R.id.media_actions);
    if (i > 0)
      for (paramInt = 0; paramInt < i; paramInt++) {
        RemoteViews remoteViews1 = generateMediaActionButton(paramContext, (NotificationCompatBase.Action)paramList.get(paramInt));
        remoteViews.addView(R.id.media_actions, remoteViews1);
      }  
    if (paramBoolean2) {
      remoteViews.setViewVisibility(R.id.cancel_action, 0);
      remoteViews.setInt(R.id.cancel_action, "setAlpha", paramContext.getResources().getInteger(R.integer.cancel_button_image_alpha));
      remoteViews.setOnClickPendingIntent(R.id.cancel_action, paramPendingIntent);
      return remoteViews;
    } 
    remoteViews.setViewVisibility(R.id.cancel_action, 8);
    return remoteViews;
  }
  
  private static <T extends NotificationCompatBase.Action> RemoteViews generateContentView(Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, int paramInt, Bitmap paramBitmap, CharSequence paramCharSequence4, boolean paramBoolean1, long paramLong, List<T> paramList, int[] paramArrayOfint, boolean paramBoolean2, PendingIntent paramPendingIntent) {
    RemoteViews remoteViews = applyStandardTemplate(paramContext, paramCharSequence1, paramCharSequence2, paramCharSequence3, paramInt, paramBitmap, paramCharSequence4, paramBoolean1, paramLong, R.layout.notification_template_media, true);
    int i = paramList.size();
    if (paramArrayOfint == null) {
      paramInt = 0;
    } else {
      paramInt = Math.min(paramArrayOfint.length, 3);
    } 
    remoteViews.removeAllViews(R.id.media_actions);
    if (paramInt > 0) {
      int j;
      for (j = 0; j < paramInt; j++) {
        if (j >= i)
          throw new IllegalArgumentException(String.format("setShowActionsInCompactView: action %d out of bounds (max %d)", new Object[] { Integer.valueOf(j), Integer.valueOf(i - 1) })); 
        RemoteViews remoteViews1 = generateMediaActionButton(paramContext, (NotificationCompatBase.Action)paramList.get(paramArrayOfint[j]));
        remoteViews.addView(R.id.media_actions, remoteViews1);
      } 
    } 
    if (paramBoolean2) {
      remoteViews.setViewVisibility(R.id.end_padder, 8);
      remoteViews.setViewVisibility(R.id.cancel_action, 0);
      remoteViews.setOnClickPendingIntent(R.id.cancel_action, paramPendingIntent);
      remoteViews.setInt(R.id.cancel_action, "setAlpha", paramContext.getResources().getInteger(R.integer.cancel_button_image_alpha));
      return remoteViews;
    } 
    remoteViews.setViewVisibility(R.id.end_padder, 0);
    remoteViews.setViewVisibility(R.id.cancel_action, 8);
    return remoteViews;
  }
  
  private static RemoteViews generateMediaActionButton(Context paramContext, NotificationCompatBase.Action paramAction) {
    boolean bool;
    if (paramAction.getActionIntent() == null) {
      bool = true;
    } else {
      bool = false;
    } 
    RemoteViews remoteViews = new RemoteViews(paramContext.getPackageName(), R.layout.notification_media_action);
    remoteViews.setImageViewResource(R.id.action0, paramAction.getIcon());
    if (!bool)
      remoteViews.setOnClickPendingIntent(R.id.action0, paramAction.getActionIntent()); 
    if (Build.VERSION.SDK_INT >= 15)
      remoteViews.setContentDescription(R.id.action0, paramAction.getTitle()); 
    return remoteViews;
  }
  
  private static int getBigLayoutResource(int paramInt) {
    return (paramInt <= 3) ? R.layout.notification_template_big_media_narrow : R.layout.notification_template_big_media;
  }
  
  public static <T extends NotificationCompatBase.Action> void overrideBigContentView(Notification paramNotification, Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, int paramInt, Bitmap paramBitmap, CharSequence paramCharSequence4, boolean paramBoolean1, long paramLong, List<T> paramList, boolean paramBoolean2, PendingIntent paramPendingIntent) {
    paramNotification.bigContentView = generateBigContentView(paramContext, paramCharSequence1, paramCharSequence2, paramCharSequence3, paramInt, paramBitmap, paramCharSequence4, paramBoolean1, paramLong, paramList, paramBoolean2, paramPendingIntent);
    if (paramBoolean2)
      paramNotification.flags |= 0x2; 
  }
  
  public static <T extends NotificationCompatBase.Action> void overrideContentView(NotificationBuilderWithBuilderAccessor paramNotificationBuilderWithBuilderAccessor, Context paramContext, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, int paramInt, Bitmap paramBitmap, CharSequence paramCharSequence4, boolean paramBoolean1, long paramLong, List<T> paramList, int[] paramArrayOfint, boolean paramBoolean2, PendingIntent paramPendingIntent) {
    RemoteViews remoteViews = generateContentView(paramContext, paramCharSequence1, paramCharSequence2, paramCharSequence3, paramInt, paramBitmap, paramCharSequence4, paramBoolean1, paramLong, paramList, paramArrayOfint, paramBoolean2, paramPendingIntent);
    paramNotificationBuilderWithBuilderAccessor.getBuilder().setContent(remoteViews);
    if (paramBoolean2)
      paramNotificationBuilderWithBuilderAccessor.getBuilder().setOngoing(true); 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\app\NotificationCompatImplBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */