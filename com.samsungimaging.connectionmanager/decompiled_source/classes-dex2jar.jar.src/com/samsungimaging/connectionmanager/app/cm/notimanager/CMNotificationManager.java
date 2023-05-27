package com.samsungimaging.connectionmanager.app.cm.notimanager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.NotificationCompat;
import com.samsungimaging.connectionmanager.app.cm.Main;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;

public class CMNotificationManager {
  private static CMNotificationManager s_obj = new CMNotificationManager();
  
  Context mContext = null;
  
  private boolean mIsConnected = false;
  
  NotificationManager mNotificationManager = null;
  
  private String mSsid = null;
  
  public static CMNotificationManager getInstance() {
    return s_obj;
  }
  
  public void cancelAllNoti() {
    cancelNoti(2131361840);
    if (this.mNotificationManager != null)
      this.mNotificationManager.cancelAll(); 
  }
  
  public void cancelNoti(int paramInt) {
    if (this.mNotificationManager != null)
      this.mNotificationManager.cancel(paramInt); 
  }
  
  public boolean getCurrentNoti_mIsConnected() {
    return this.mIsConnected;
  }
  
  public String getCurrentNoti_mSsid() {
    return this.mSsid;
  }
  
  public void init(Context paramContext) {
    this.mContext = paramContext;
    this.mNotificationManager = (NotificationManager)this.mContext.getSystemService("notification");
  }
  
  public void notifyStatusChange(boolean paramBoolean, String paramString1, String paramString2, int paramInt) {
    this.mIsConnected = paramBoolean;
    if (this.mContext != null) {
      NotificationCompat.Builder builder = new NotificationCompat.Builder(this.mContext);
      builder.setLargeIcon(((BitmapDrawable)this.mContext.getResources().getDrawable(2130837551)).getBitmap());
      builder.setContentTitle(this.mContext.getResources().getString(2131361800));
      if (paramBoolean) {
        builder.setSmallIcon(2130837552);
      } else {
        builder.setSmallIcon(2130837553);
      } 
      builder.setContentText(paramString2);
      builder.setTicker(paramString2);
      builder.setOngoing(true);
      Intent intent = new Intent(this.mContext, Main.class);
      intent.addFlags(872415232);
      builder.setContentIntent(PendingIntent.getActivity(this.mContext, paramInt, intent, 134217728));
      if (paramString1 != null) {
        this.mSsid = CMUtil.convertToNOTQuotedString(paramString1);
        builder.setSubText(this.mSsid);
      } 
      this.mNotificationManager.notify(paramInt, builder.build());
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\notimanager\CMNotificationManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */