package com.samsungimaging.connectionmanager.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import java.lang.ref.WeakReference;

public abstract class HandlerMessage extends Handler {
  private final WeakReference<Activity> mActivity;
  
  public HandlerMessage(Activity paramActivity) {
    this.mActivity = new WeakReference<Activity>(paramActivity);
  }
  
  public void handleMessage(Message paramMessage) {
    if ((Activity)this.mActivity.get() == null)
      return; 
    onHandleMessage(paramMessage);
  }
  
  public abstract void onHandleMessage(Message paramMessage);
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanage\\util\HandlerMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */