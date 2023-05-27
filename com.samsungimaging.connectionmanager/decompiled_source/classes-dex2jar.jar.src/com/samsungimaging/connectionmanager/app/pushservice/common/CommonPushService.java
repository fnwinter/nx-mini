package com.samsungimaging.connectionmanager.app.pushservice.common;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.app.BaseActivity;
import com.samsungimaging.connectionmanager.app.BaseGalleryActivity;
import com.samsungimaging.connectionmanager.dialog.CustomDialog;
import com.samsungimaging.connectionmanager.dialog.CustomProgressDialog;
import com.samsungimaging.connectionmanager.dialog.IntroGuideDialog;
import com.samsungimaging.connectionmanager.gallery.GalleryFragment;
import com.samsungimaging.connectionmanager.provider.DatabaseMedia;
import com.samsungimaging.connectionmanager.util.Trace;
import com.samsungimaging.connectionmanager.util.Utils;
import java.util.Locale;

public abstract class CommonPushService extends BaseGalleryActivity implements MediaScannerConnection.OnScanCompletedListener {
  private static final Trace.Tag TAG = Trace.Tag.COMMON;
  
  private BroadcastReceiver mBatteryChangedReceiver = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        if ("android.intent.action.BATTERY_CHANGED".equals(param1Intent.getAction())) {
          int i = param1Intent.getIntExtra("level", -1);
          if (i != -1 && i <= 4) {
            Trace.d(CommonPushService.TAG, "Broadcasted Low Battery level : " + i);
            CommonPushService.this.showDialog(1008);
          } 
        } 
      }
    };
  
  private TextView mMainDescription = null;
  
  private Service mService = Service.NONE;
  
  private TextView mSubDescription = null;
  
  protected abstract void exitAfterSendByeBye(String paramString);
  
  protected Service getService() {
    return this.mService;
  }
  
  public void mediaScannerConnectionScanFile(String paramString) {
    String str = Utils.getMimeType(paramString);
    MediaScannerConnection.scanFile((Context)this, new String[] { paramString }, new String[] { str }, this);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2130903045);
    this.mGallery = (GalleryFragment)getFragmentManager().findFragmentById(2131558499);
    this.mGallery.setDiskCacheDir("thumbnail", "viewer");
    this.mGallery.setOnItemClickListener((GalleryFragment.OnItemClickListener)this);
    this.mGallery.setOnItemLongClickListener((GalleryFragment.OnItemLongClickListener)this);
    this.mGallery.setQuery(getQuery());
    this.mActionBar.setTitle(2131362050);
    this.mActionBar.show();
    this.mMainDescription = (TextView)findViewById(16908308);
    this.mSubDescription = (TextView)findViewById(16908309);
    registerReceiver(this.mBatteryChangedReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
  }
  
  protected Dialog onCreateDialog(int paramInt, Bundle paramBundle) {
    CustomProgressDialog customProgressDialog;
    CustomDialog customDialog;
    switch (paramInt) {
      default:
        return super.onCreateDialog(paramInt, paramBundle);
      case 1000:
        customProgressDialog = new CustomProgressDialog((Context)this);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setProgressStyle(1);
        customProgressDialog.setTitle(2131361897);
        customProgressDialog.setOnProcessNumberListener(new CustomProgressDialog.OnProcessNumberListener() {
              public String onProcessNumber(CustomProgressDialog param1CustomProgressDialog) {
                return String.format(Locale.getDefault(), "%,.01f/%,.01fMB", new Object[] { Float.valueOf(param1CustomProgressDialog.getProgress() / 1000.0F), Float.valueOf(param1CustomProgressDialog.getMax() / 1000.0F) });
              }
            });
        Trace.d(TAG, "create dialog DIALOG_ID_PROCESSING");
        return (Dialog)customProgressDialog;
      case 1001:
        customProgressDialog = new CustomProgressDialog((Context)this);
        customProgressDialog.setCancelable(false);
        customProgressDialog.setProgressStyle(0);
        customProgressDialog.setMessage(2131361897);
        Trace.d(TAG, "create dialog DIALOG_ID_SPINNER");
        return (Dialog)customProgressDialog;
      case 1003:
        customDialog = new CustomDialog((Context)this);
        customDialog.setMessage(2131361822);
        customDialog.setCancelable(false);
        customDialog.setNeutralButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                CommonPushService.this.exitAfterSendByeBye("Removed_SDCard");
              }
            });
        return (Dialog)customDialog;
      case 1004:
        customDialog = new CustomDialog((Context)this);
        customDialog.setMessage(2131361827);
        customDialog.setCancelable(false);
        customDialog.setNeutralButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                CommonPushService.this.exitAfterSendByeBye("Memory_Full");
              }
            });
        return (Dialog)customDialog;
      case 1008:
        customDialog = new CustomDialog((Context)this);
        customDialog.setMessage(2131361936);
        customDialog.setNeutralButton(17039370, new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                CommonPushService.this.exitAfterSendByeBye("Low_Battery");
                CommonPushService.this.exit();
              }
            });
        return (Dialog)customDialog;
      case 1006:
        customDialog = new CustomDialog((Context)this);
        customDialog.setMessage(2131361824);
        return (Dialog)customDialog;
      case 1002:
        break;
    } 
    return (Dialog)new IntroGuideDialog((BaseActivity)this);
  }
  
  protected void onDestroy() {
    unregisterReceiver(this.mBatteryChangedReceiver);
    super.onDestroy();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    return keyDown(paramInt, paramKeyEvent) ? true : super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    return keyUp(paramInt, paramKeyEvent) ? true : super.onKeyUp(paramInt, paramKeyEvent);
  }
  
  public boolean onPrepareOptionsMenu(Menu paramMenu) {
    super.onPrepareOptionsMenu(paramMenu);
    if (this.mService == Service.MOBILEBACKUP) {
      int i = 0;
      while (true) {
        if (i < paramMenu.size()) {
          MenuItem menuItem = paramMenu.getItem(i);
          if ((menuItem.getItemId() == 2131558726 || menuItem.getItemId() == 2131558723) && menuItem.isVisible())
            menuItem.setVisible(false); 
          i++;
          continue;
        } 
        return true;
      } 
    } 
    return true;
  }
  
  public void onScanCompleted(String paramString, Uri paramUri) {
    Cursor cursor = getContentResolver().query(paramUri, null, null, null, null);
    if (cursor != null && cursor.getCount() > 0) {
      cursor.moveToFirst();
      this.mGallery.setReceivedMedia(DatabaseMedia.builder(cursor));
      cursor.close();
    } 
  }
  
  protected void setService(Service paramService) {
    this.mService = paramService;
    switch (this.mService) {
      default:
        return;
      case AUTOSHARE:
        this.mMainDescription.setText(2131362051);
        this.mSubDescription.setText(2131362041);
        return;
      case SELECTIVEPUSH:
      case GROUPSHARE:
        this.mMainDescription.setText(2131362052);
        this.mSubDescription.setText(2131362042);
        return;
      case MOBILEBACKUP:
        break;
    } 
    this.mMainDescription.setText(2131362060);
    this.mSubDescription.setText(2131362061);
    this.mGallery.setOnItemLongClickListener(null);
  }
  
  public void updateMediaContent(String paramString) {
    mediaScannerConnectionScanFile(paramString);
    this.mGallery.CheckDirtyCache(paramString);
  }
  
  protected enum Service {
    AUTOSHARE, GROUPSHARE, MOBILEBACKUP, NONE, SELECTIVEPUSH;
    
    static {
      GROUPSHARE = new Service("GROUPSHARE", 3);
      MOBILEBACKUP = new Service("MOBILEBACKUP", 4);
      ENUM$VALUES = new Service[] { NONE, AUTOSHARE, SELECTIVEPUSH, GROUPSHARE, MOBILEBACKUP };
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pushservice\common\CommonPushService.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */