package com.samsungimaging.connectionmanager.app.pullservice;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.app.BaseGalleryActivity;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;
import com.samsungimaging.connectionmanager.app.localgallery.LocalGallery;
import com.samsungimaging.connectionmanager.gallery.GalleryFragment;
import com.samsungimaging.connectionmanager.manager.DatabaseManager;
import com.samsungimaging.connectionmanager.provider.DatabaseMedia;
import com.samsungimaging.connectionmanager.util.Utils;
import java.util.Calendar;

public class MobileLink extends BaseGalleryActivity implements View.OnClickListener {
  private static final int REQUEST_GALLERY = 0;
  
  private TextView mMainDescription = null;
  
  private TextView mSubDescription = null;
  
  private void testDBInsert() {
    DatabaseManager.deleteForCameraMedia((Context)this, null);
    this.mGallery.clearCache();
    byte b = 1;
    Calendar calendar = Calendar.getInstance();
    for (int i = 0;; i++) {
      if (i >= 10)
        return; 
      if (b == 1) {
        b = 3;
      } else {
        b = 1;
      } 
      calendar.set(5, calendar.get(5) - 1);
      DatabaseManager.putForCameraMedia((Context)this, new DatabaseMedia("http://192.168.101.1:7679/smp000001129121.jpg", "http://192.168.101.1:7679/smpthumb000002129121.jpg", "http://192.168.101.1:7679/smpscreen000003129121.jpg", b, calendar.getTimeInMillis(), 0));
    } 
  }
  
  protected GalleryFragment.Query getQuery() {
    return new GalleryFragment.Query() {
        public GalleryFragment.QueryInfo getChildQueryInfo(Cursor param1Cursor) {
          String str1 = param1Cursor.getString(param1Cursor.getColumnIndex("datetaken_string"));
          Uri uri = DatabaseMedia.CONTENT_URI;
          StringBuilder stringBuilder = new StringBuilder(Utils.getCameraMediaQuerySelection());
          stringBuilder.append(" AND datetaken_string=?");
          String str2 = stringBuilder.toString();
          return new GalleryFragment.QueryInfo(uri, new String[] { "_id", "_data", "thumbnail_path", "screen_path", "datetaken", "date((datetaken / 1000), 'unixepoch', 'localtime') as datetaken_string", "media_type", "orientation" }, str2, new String[] { str1 }, "datetaken DESC");
        }
        
        public GalleryFragment.QueryInfo getGroupQueryInfo() {
          Uri uri = DatabaseMedia.CONTENT_URI;
          String[] arrayOfString = Utils.getMediaGroupQueryProjection();
          StringBuilder stringBuilder = new StringBuilder(Utils.getCameraMediaQuerySelection());
          stringBuilder.append(") GROUP BY (datetaken_string");
          return new GalleryFragment.QueryInfo(uri, arrayOfString, stringBuilder.toString(), null, "datetaken_string DESC");
        }
      };
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    switch (paramInt1) {
    
    } 
  }
  
  public void onClick(View paramView) {
    switch (paramView.getId()) {
      default:
        return;
      case 2131558401:
        break;
    } 
    Intent intent = new Intent((Context)this, LocalGallery.class);
    intent.putExtra("extra_description_res_id", 2131362052);
    intent.putExtra("extra_back_button_string_res_id", 2131362062);
    startActivityForResult(intent, 0);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2130903041);
    this.mGallery = (GalleryFragment)getFragmentManager().findFragmentById(2131558499);
    this.mGallery.setDiskCacheDir("camera_thumbnail", "camera_viewer");
    this.mGallery.setOnItemClickListener((GalleryFragment.OnItemClickListener)this);
    this.mGallery.setOnItemLongClickListener((GalleryFragment.OnItemLongClickListener)this);
    testDBInsert();
    this.mActionBar.setTitle(2131361803);
    this.mActionBar.show();
    setGravityForActionBarCustomView(5);
    this.mMainDescription = (TextView)findViewById(16908308);
    this.mMainDescription.setText(2131362052);
    this.mSubDescription = (TextView)findViewById(16908309);
    this.mSubDescription.setText(2131362043);
    this.mGallery.setState(GalleryFragment.State.MULTI_SELECT, new Object[0]);
    this.mGallery.setQuery(getQuery());
  }
  
  protected void onDestroy() {
    CMService.getInstance().beforefinish(0);
    sendBroadcast(new Intent("action_disconnect"));
    super.onDestroy();
  }
  
  public boolean onItemLongClick(View paramView, int paramInt1, int paramInt2) {
    this.mGallery.setState(GalleryFragment.State.IMAGE_VIEWER, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    return true;
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    return keyDown(paramInt, paramKeyEvent) ? true : super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    if (paramInt == 4 && this.mGallery.getState() != GalleryFragment.State.MULTI_SELECT) {
      this.mGallery.setState(GalleryFragment.State.MULTI_SELECT, new Object[0]);
      return true;
    } 
    return super.onKeyUp(paramInt, paramKeyEvent);
  }
  
  public boolean onPrepareOptionsMenu(Menu paramMenu) {
    super.onPrepareOptionsMenu(paramMenu);
    int i = 0;
    while (true) {
      if (i >= paramMenu.size()) {
        switch (this.mGallery.getState()) {
          default:
            return true;
          case MULTI_SELECT:
            break;
        } 
      } else {
        MenuItem menuItem = paramMenu.getItem(i);
        if (menuItem.isVisible())
          menuItem.setVisible(false); 
        i++;
        continue;
      } 
      if ((this.mActionBar.getDisplayOptions() & 0x4) == 4) {
        this.mActionBar.setDisplayHomeAsUpEnabled(false);
        this.mActionBar.setTitle(2131361803);
      } 
      Button button = (Button)findViewById(2131558400);
      if (this.mGallery.getCheckedCount() > 0) {
        button.setEnabled(true);
        return true;
      } 
      button.setEnabled(false);
      return true;
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\MobileLink.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */