package com.samsungimaging.connectionmanager.app.localgallery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.app.BaseGalleryActivity;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.gallery.GalleryFragment;
import com.samsungimaging.connectionmanager.util.MyLocalGallery;
import com.samsungimaging.connectionmanager.util.Trace;

public class LocalGallery extends BaseGalleryActivity implements View.OnClickListener {
  public static final String EXTRA_BACK_BUTTON_STRING_RES_ID = "extra_back_button_string_res_id";
  
  public static final String EXTRA_DESCRIPTION_RES_ID = "extra_description_res_id";
  
  public static final String EXTRA_IS_SUPPORT_BACK_BUTTON = "EXTRA_IS_SUPPORT_BACK_BUTTON";
  
  private TextView mBackButton = null;
  
  private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        if ("action_disconnect".equals(param1Intent.getAction()))
          LocalGallery.this.finish(); 
      }
    };
  
  private TextView mDescription = null;
  
  private void initIntentFilter() {
    IntentFilter intentFilter = new IntentFilter("action_disconnect");
    registerReceiver(this.mBroadcastReceiver, intentFilter);
  }
  
  protected void exit() {
    setResult(-1);
    finish();
  }
  
  public void onClick(View paramView) {
    if (((TextView)paramView).getText().equals(getString(2131361803))) {
      Intent intent = new Intent();
      intent.putExtra("request_code", "goto_ml");
      setResult(-1, intent);
    } 
    finish();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    MyLocalGallery.activityCreated();
    setContentView(2130903044);
    this.mGallery = (GalleryFragment)getFragmentManager().findFragmentById(2131558499);
    this.mGallery.setDiskCacheDir("thumbnail", "viewer");
    this.mGallery.setOnItemClickListener((GalleryFragment.OnItemClickListener)this);
    this.mGallery.setOnItemLongClickListener((GalleryFragment.OnItemLongClickListener)this);
    this.mGallery.setQuery(getQuery());
    this.mActionBar.setTitle(2131362065);
    this.mActionBar.show();
    this.mDescription = (TextView)findViewById(16908308);
    this.mDescription.setText(getIntent().getIntExtra("extra_description_res_id", 0));
    this.mBackButton = (TextView)findViewById(16908313);
    this.mBackButton.setText(getIntent().getIntExtra("extra_back_button_string_res_id", 0));
    this.mBackButton.setOnClickListener(this);
    if (getIntent().getBooleanExtra("EXTRA_IS_SUPPORT_BACK_BUTTON", true)) {
      this.mBackButton.setVisibility(0);
    } else {
      this.mBackButton.setVisibility(4);
    } 
    initIntentFilter();
  }
  
  protected void onDestroy() {
    unregisterReceiver(this.mBroadcastReceiver);
    MyLocalGallery.activityDestroyed();
    super.onDestroy();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    return keyDown(paramInt, paramKeyEvent) ? true : super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    return keyUp(paramInt, paramKeyEvent) ? true : super.onKeyUp(paramInt, paramKeyEvent);
  }
  
  protected void onNewIntent(Intent paramIntent) {
    super.onNewIntent(paramIntent);
    Log.d("LG", "LG_NEW INTENT");
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
    if (paramMenuItem.getItemId() == 2131558724) {
      Trace.d(Trace.Tag.COMMON, "R.id.menu_camera");
      Intent intent = new Intent();
      intent.putExtra("request_code", "goto_rvf");
      setResult(-1, intent);
      finish();
    } 
    return super.onOptionsItemSelected(paramMenuItem);
  }
  
  public boolean onPrepareOptionsMenu(Menu paramMenu) {
    super.onPrepareOptionsMenu(paramMenu);
    if ((this.mActionBar.getDisplayOptions() & 0x4) == 0) {
      this.mActionBar.setDisplayHomeAsUpEnabled(true);
      this.mActionBar.setTitle(2131362065);
    } 
    if (!CMUtil.checkOldVersionSmartCameraApp(CMInfo.getInstance().getConnectedSSID())) {
      int i = 0;
      while (true) {
        if (i < paramMenu.size()) {
          MenuItem menuItem = paramMenu.getItem(i);
          if (menuItem.getItemId() == 2131558724 && !menuItem.isVisible())
            menuItem.setVisible(true); 
          i++;
          continue;
        } 
        return true;
      } 
    } 
    return true;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\localgallery\LocalGallery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */