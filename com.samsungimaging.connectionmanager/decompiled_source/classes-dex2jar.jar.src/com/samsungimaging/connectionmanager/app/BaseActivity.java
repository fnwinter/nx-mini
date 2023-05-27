package com.samsungimaging.connectionmanager.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.util.SparseArray;
import android.widget.Toast;
import com.samsungimaging.connectionmanager.dialog.CustomDialog;
import com.samsungimaging.connectionmanager.util.HandlerMessage;
import com.samsungimaging.connectionmanager.util.Settings;
import com.samsungimaging.connectionmanager.util.Trace;
import com.samsungimaging.connectionmanager.util.Utils;

public class BaseActivity extends Activity {
  private static final int BACK_PRESSED_TIME_DELAY = 2000;
  
  private static final int HANDLER_STATUS_BAR_HIDE = 0;
  
  private static final int STATUS_BAR_HIDE_DELAY = 2000;
  
  protected ActionBar mActionBar = null;
  
  private long mBackPressedTimeMillis = 0L;
  
  protected SparseArray<CustomDialog> mDialogList = new SparseArray();
  
  private HandlerMessage mHandler = new HandlerMessage(this) {
      public void onHandleMessage(Message param1Message) {
        switch (param1Message.what) {
          default:
            return;
          case 0:
            break;
        } 
        BaseActivity.this.hideStatusBar();
      }
    };
  
  private boolean mIsBackPressed = false;
  
  protected Settings mSettings = null;
  
  private boolean mStatusBarVisible = false;
  
  public Settings getSettings() {
    return this.mSettings;
  }
  
  public void hideStatusBar() {
    if (this.mStatusBarVisible) {
      getWindow().clearFlags(2048);
      this.mStatusBarVisible = false;
    } 
    removeMessage(0);
  }
  
  protected boolean isBackPressAvailable() {
    if (SystemClock.elapsedRealtime() - this.mBackPressedTimeMillis > 2000L) {
      this.mBackPressedTimeMillis = SystemClock.elapsedRealtime();
      Toast.makeText((Context)this, 2131361826, 0).show();
      return false;
    } 
    if (!isFinishing() && !this.mIsBackPressed) {
      this.mIsBackPressed = true;
      return true;
    } 
    return false;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    Trace.d(Trace.Tag.COMMON, "onConfigurationChanged");
    super.onConfigurationChanged(paramConfiguration);
    if (this.mDialogList != null) {
      int i = 0;
      while (true) {
        if (i < this.mDialogList.size()) {
          int j = this.mDialogList.keyAt(i);
          ((CustomDialog)this.mDialogList.get(j)).onOrientationChanged();
          i++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  protected void onCreate(Bundle paramBundle) {
    setTheme(2131427328);
    super.onCreate(paramBundle);
    this.mSettings = new Settings((Context)this);
    this.mActionBar = getActionBar();
    this.mActionBar.hide();
  }
  
  protected void onDestroy() {
    if (this.mActionBar != null)
      this.mActionBar = null; 
    if (this.mDialogList != null) {
      this.mDialogList.clear();
      this.mDialogList = null;
    } 
    Utils.unbindView(getWindow().getDecorView());
    super.onDestroy();
  }
  
  protected void onPrepareDialog(int paramInt, Dialog paramDialog, Bundle paramBundle) {
    if (this.mDialogList != null && paramDialog != null && paramDialog instanceof CustomDialog) {
      this.mDialogList.put(paramInt, paramDialog);
      super.onPrepareDialog(paramInt, paramDialog, paramBundle);
    } 
  }
  
  public void onWindowFocusChanged(boolean paramBoolean) {
    if (paramBoolean) {
      hideStatusBar();
    } else {
      removeMessage(0);
    } 
    super.onWindowFocusChanged(paramBoolean);
  }
  
  protected void removeMessage(int paramInt) {
    if (this.mHandler != null && this.mHandler.hasMessages(paramInt))
      this.mHandler.removeMessages(paramInt); 
  }
  
  public void showStatusBar() {
    if (!this.mStatusBarVisible) {
      getWindow().addFlags(2048);
      this.mStatusBarVisible = true;
    } 
    if (this.mHandler != null) {
      removeMessage(0);
      this.mHandler.sendEmptyMessageDelayed(0, 2000L);
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\BaseActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */