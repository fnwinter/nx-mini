package com.samsungimaging.connectionmanager.app;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;
import com.samsungimaging.connectionmanager.dialog.CustomDialog;
import com.samsungimaging.connectionmanager.dialog.HelpDialog;
import com.samsungimaging.connectionmanager.dialog.LicenseDialog;
import com.samsungimaging.connectionmanager.dialog.ManualConnectionGuideDialog;
import com.samsungimaging.connectionmanager.dialog.NFCConnectionGuideDialog;
import com.samsungimaging.connectionmanager.dialog.PairedCameraDialog;
import com.samsungimaging.connectionmanager.dialog.PairedCameraRemoveDialog;
import com.samsungimaging.connectionmanager.dialog.PasswordDialog;
import com.samsungimaging.connectionmanager.gallery.GalleryFragment;
import com.samsungimaging.connectionmanager.provider.DatabaseAP;
import com.samsungimaging.connectionmanager.util.Trace;
import com.samsungimaging.connectionmanager.util.Utils;
import com.samsungimaging.connectionmanager.widget.ListPopupWindow;

public class BaseGalleryActivity extends BaseActivity implements GalleryFragment.OnItemClickListener, GalleryFragment.OnItemLongClickListener {
  public static final String BUNDLE_KEY_DATA = "data";
  
  private static final int DIALOG_ID_HELP = 4;
  
  private static final int DIALOG_ID_LICENSE = 5;
  
  private static final int DIALOG_ID_MANUAL_CONNECTION_GUIDE = 2;
  
  public static final int DIALOG_ID_NFC_CONNECTION_GUIDE = 3;
  
  private static final int DIALOG_ID_PAIRED_CAMERA = 0;
  
  public static final int DIALOG_ID_PAIRED_CAMERA_REMOVE = 1;
  
  public static final int DIALOG_ID_PASSWORD = 6;
  
  private static final String LOCAL_MEDIA_FOLDER = "%DCIM/Camera/Samsung Smart Camera Application%";
  
  private View.OnClickListener mActionBarCustomClickListener = new View.OnClickListener() {
      public void onClick(View param1View) {
        String[] arrayOfString;
        if (BaseGalleryActivity.this.mSelectPopupWindow == null) {
          BaseGalleryActivity.this.mSelectPopupWindow = new ListPopupWindow(BaseGalleryActivity.this);
          BaseGalleryActivity.this.mSelectPopupWindow.setSize(BaseGalleryActivity.this.getResources().getDimensionPixelSize(2131230721), -2);
          BaseGalleryActivity.this.mSelectPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> param2AdapterView, View param2View, int param2Int, long param2Long) {
                  boolean bool = false;
                  int i = (BaseGalleryActivity.null.access$0(BaseGalleryActivity.null.this)).mGallery.getCheckedCount();
                  int j = (BaseGalleryActivity.null.access$0(BaseGalleryActivity.null.this)).mGallery.getChildTotalCount();
                  if (i == 0) {
                    bool = true;
                  } else if (i != j) {
                    if (param2Int == 0) {
                      bool = true;
                    } else {
                      bool = false;
                    } 
                  } 
                  (BaseGalleryActivity.null.access$0(BaseGalleryActivity.null.this)).mGallery.setCheckAll(bool);
                  (BaseGalleryActivity.null.access$0(BaseGalleryActivity.null.this)).mSelectPopupWindow.dismiss();
                }
              });
        } 
        int i = BaseGalleryActivity.this.mGallery.getCheckedCount();
        int j = BaseGalleryActivity.this.mGallery.getChildTotalCount();
        if (i == 0) {
          arrayOfString = BaseGalleryActivity.this.getResources().getStringArray(2131099648);
        } else if (i == j) {
          arrayOfString = BaseGalleryActivity.this.getResources().getStringArray(2131099649);
        } else {
          arrayOfString = BaseGalleryActivity.this.getResources().getStringArray(2131099650);
        } 
        BaseGalleryActivity.this.mSelectPopupWindow.clearItem();
        BaseGalleryActivity.this.mSelectPopupWindow.addItem(arrayOfString);
        BaseGalleryActivity.this.mSelectPopupWindow.showAsDropDown(param1View);
      }
    };
  
  private Button mActionBarCustomView = null;
  
  protected GalleryFragment mGallery = null;
  
  protected NfcAdapter mNFCAdapter = null;
  
  private ListPopupWindow mSelectPopupWindow = null;
  
  private void invalidateActionBarCustomView() {
    int i = ((ActionBar.LayoutParams)this.mActionBarCustomView.getLayoutParams()).gravity;
    CharSequence charSequence = this.mActionBarCustomView.getText();
    Utils.unbindView((View)this.mActionBarCustomView);
    this.mActionBarCustomView = (Button)getLayoutInflater().inflate(2130903040, null);
    this.mActionBarCustomView.setText(charSequence);
    this.mActionBarCustomView.setOnClickListener(this.mActionBarCustomClickListener);
    this.mActionBar.setCustomView((View)this.mActionBarCustomView, new ActionBar.LayoutParams(i));
    if (this.mSelectPopupWindow != null && this.mSelectPopupWindow.isShowing()) {
      this.mSelectPopupWindow.dismiss();
      this.mSelectPopupWindow.showAsDropDown((View)this.mActionBarCustomView);
    } 
  }
  
  protected void exit() {
    CMService.getInstance().finishSafe();
  }
  
  protected GalleryFragment.Query getQuery() {
    return new GalleryFragment.Query() {
        public GalleryFragment.QueryInfo getChildQueryInfo(Cursor param1Cursor) {
          String str1 = param1Cursor.getString(param1Cursor.getColumnIndex("datetaken_string"));
          Uri uri = MediaStore.Files.getContentUri("external");
          StringBuilder stringBuilder = new StringBuilder(Utils.getLocalMediaQuerySelection());
          stringBuilder.append(" AND datetaken_string=?");
          String str2 = stringBuilder.toString();
          return new GalleryFragment.QueryInfo(uri, new String[] { "_id", "_data", "datetaken", "date((datetaken / 1000), 'unixepoch', 'localtime') as datetaken_string", "media_type", "orientation" }, str2, new String[] { "%DCIM/Camera/Samsung Smart Camera Application%", str1 }, "datetaken DESC");
        }
        
        public GalleryFragment.QueryInfo getGroupQueryInfo() {
          Uri uri = MediaStore.Files.getContentUri("external");
          String[] arrayOfString = Utils.getMediaGroupQueryProjection();
          StringBuilder stringBuilder = new StringBuilder(Utils.getLocalMediaQuerySelection());
          stringBuilder.append(") GROUP BY (datetaken_string");
          return new GalleryFragment.QueryInfo(uri, arrayOfString, stringBuilder.toString(), new String[] { "%DCIM/Camera/Samsung Smart Camera Application%" }, "datetaken_string DESC");
        }
      };
  }
  
  protected final boolean keyDown(int paramInt, KeyEvent paramKeyEvent) {
    return (this.mGallery.isVisible() && this.mGallery.onKeyDown(paramInt, paramKeyEvent));
  }
  
  protected final boolean keyUp(int paramInt, KeyEvent paramKeyEvent) {
    return (this.mGallery.isVisible() && this.mGallery.onKeyUp(paramInt, paramKeyEvent));
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    Trace.d(Trace.Tag.COMMON, "onConfigurationChanged");
    super.onConfigurationChanged(paramConfiguration);
    invalidateActionBarCustomView();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    this.mNFCAdapter = NfcAdapter.getDefaultAdapter((Context)this);
    this.mActionBarCustomView = (Button)getLayoutInflater().inflate(2130903040, null);
    this.mActionBarCustomView.setOnClickListener(this.mActionBarCustomClickListener);
    this.mActionBar.setCustomView((View)this.mActionBarCustomView, new ActionBar.LayoutParams(3));
    this.mActionBar.setDisplayShowTitleEnabled(true);
  }
  
  protected Dialog onCreateDialog(int paramInt, Bundle paramBundle) {
    NFCConnectionGuideDialog nFCConnectionGuideDialog;
    switch (paramInt) {
      default:
        return super.onCreateDialog(paramInt, paramBundle);
      case 0:
        return (Dialog)new PairedCameraDialog(this);
      case 1:
        return (Dialog)new PairedCameraRemoveDialog((Context)this);
      case 2:
        return (Dialog)new ManualConnectionGuideDialog((Context)this);
      case 3:
        nFCConnectionGuideDialog = new NFCConnectionGuideDialog(this);
        ((CustomDialog)nFCConnectionGuideDialog).setTag(Boolean.valueOf(paramBundle.getBoolean("data")));
        return (Dialog)nFCConnectionGuideDialog;
      case 4:
        return (Dialog)new HelpDialog((Context)this);
      case 6:
        break;
    } 
    return (Dialog)new PasswordDialog(this);
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu) {
    Trace.d(Trace.Tag.COMMON, "start onCreateOptionsMenu()");
    getMenuInflater().inflate(2131492864, paramMenu);
    if (this.mNFCAdapter == null)
      paramMenu.findItem(2131558729).setVisible(false); 
    return true;
  }
  
  protected void onDestroy() {
    if (this.mSelectPopupWindow != null)
      this.mSelectPopupWindow.destroy(); 
    super.onDestroy();
  }
  
  public void onItemClick(View paramView, int paramInt1, int paramInt2) {
    if (this.mGallery.getState() == GalleryFragment.State.MULTI_SELECT) {
      this.mGallery.setChecked(paramInt1, paramInt2);
    } else {
      this.mGallery.setState(GalleryFragment.State.IMAGE_VIEWER, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    } 
    MotionEvent motionEvent = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), 3, 0.0F, 0.0F, 0);
    paramView.dispatchTouchEvent(motionEvent);
    motionEvent.recycle();
  }
  
  public boolean onItemLongClick(View paramView, int paramInt1, int paramInt2) {
    if (this.mGallery.getState() != GalleryFragment.State.MULTI_SELECT) {
      this.mGallery.setState(GalleryFragment.State.MULTI_SELECT, new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
      MotionEvent motionEvent1 = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), 3, 0.0F, 0.0F, 0);
      paramView.dispatchTouchEvent(motionEvent1);
      motionEvent1.recycle();
      return true;
    } 
    this.mGallery.setChecked(paramInt1, paramInt2);
    MotionEvent motionEvent = MotionEvent.obtain(System.currentTimeMillis(), System.currentTimeMillis(), 3, 0.0F, 0.0F, 0);
    paramView.dispatchTouchEvent(motionEvent);
    motionEvent.recycle();
    return true;
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
    Bundle bundle;
    Trace.d(Trace.Tag.COMMON, "start onOptionsItemSelected()");
    switch (paramMenuItem.getItemId()) {
      default:
        return true;
      case 16908332:
        if ((this.mActionBar.getDisplayOptions() & 0x4) == 4) {
          dispatchKeyEvent(new KeyEvent(0, 4));
          dispatchKeyEvent(new KeyEvent(1, 4));
          return true;
        } 
      case 2131558726:
        this.mGallery.setState(GalleryFragment.State.MULTI_SELECT, new Object[0]);
        invalidateOptionsMenu();
        return true;
      case 2131558723:
        this.mGallery.delete();
        return true;
      case 2131558727:
        showDialog(0);
        return true;
      case 2131558728:
        showDialog(2);
        return true;
      case 2131558729:
        bundle = new Bundle();
        bundle.putBoolean("data", false);
        showDialog(3, bundle);
        return true;
      case 2131558730:
        showDialog(4);
        return true;
      case 2131558731:
        break;
    } 
    startActivity(new Intent((Context)this, LicenseDialog.class));
    return true;
  }
  
  protected void onPrepareDialog(int paramInt, Dialog paramDialog, Bundle paramBundle) {
    DatabaseAP databaseAP;
    switch (paramInt) {
      default:
        super.onPrepareDialog(paramInt, paramDialog, paramBundle);
        return;
      case 1:
        databaseAP = (DatabaseAP)paramBundle.getSerializable("data");
        ((CustomDialog)paramDialog).setTag(databaseAP);
      case 3:
        ((CustomDialog)paramDialog).setTag(Boolean.valueOf(paramBundle.getBoolean("data")));
      case 6:
        break;
    } 
    ((CustomDialog)paramDialog).setTag(paramBundle.getCharSequenceArray("data"));
  }
  
  public boolean onPrepareOptionsMenu(Menu paramMenu) {
    Trace.d(Trace.Tag.COMMON, "start onPrepareOptionsMenu()");
    if (this.mGallery != null) {
      MenuItem menuItem;
      int j;
      switch (this.mGallery.getState()) {
        default:
          return true;
        case NORMAL:
          for (i = 0;; i++) {
            if (i >= paramMenu.size()) {
              menuItem = paramMenu.findItem(2131558726);
              if (this.mGallery.isEmpty()) {
                if (menuItem.isVisible())
                  menuItem.setVisible(false); 
              } else if (!menuItem.isVisible()) {
                menuItem.setVisible(true);
              } 
              if ((this.mActionBar.getDisplayOptions() & 0x4) == 4) {
                this.mActionBar.setDisplayHomeAsUpEnabled(false);
                this.mActionBar.setTitle(2131362050);
              } 
              if ((this.mActionBar.getDisplayOptions() & 0x10) == 16) {
                this.mActionBar.setDisplayShowCustomEnabled(false);
                return true;
              } 
              return true;
            } 
            MenuItem menuItem1 = menuItem.getItem(i);
            if (menuItem1.getItemId() == 2131558725) {
              if (!menuItem1.isVisible())
                menuItem1.setVisible(true); 
            } else if (menuItem1.isVisible()) {
              menuItem1.setVisible(false);
            } 
          } 
        case MULTI_SELECT:
          j = this.mGallery.getCheckedCount();
          for (i = 0;; i++) {
            if (i >= menuItem.size()) {
              if ((this.mActionBar.getDisplayOptions() & 0x4) == 0) {
                this.mActionBar.setDisplayHomeAsUpEnabled(true);
                this.mActionBar.setTitle(2131362065);
              } 
              if ((this.mActionBar.getDisplayOptions() & 0x10) == 0)
                this.mActionBar.setDisplayShowCustomEnabled(true); 
              this.mActionBarCustomView.setText(getString(2131362036, new Object[] { Integer.valueOf(j) }));
              return true;
            } 
            MenuItem menuItem1 = menuItem.getItem(i);
            if (menuItem1.getItemId() == 2131558725) {
              if (!menuItem1.isVisible())
                menuItem1.setVisible(true); 
            } else if (menuItem1.getItemId() == 2131558723 && j > 0) {
              if (!menuItem1.isVisible())
                menuItem1.setVisible(true); 
            } else if (menuItem1.isVisible()) {
              menuItem1.setVisible(false);
            } 
          } 
        case IMAGE_VIEWER:
          break;
      } 
      for (int i = 0;; i++) {
        if (i >= menuItem.size()) {
          if ((this.mActionBar.getDisplayOptions() & 0x4) == 0) {
            this.mActionBar.setDisplayHomeAsUpEnabled(true);
            this.mActionBar.setTitle(2131362065);
          } 
          if ((this.mActionBar.getDisplayOptions() & 0x10) == 16) {
            this.mActionBar.setDisplayShowCustomEnabled(false);
            return true;
          } 
          return true;
        } 
        MenuItem menuItem1 = menuItem.getItem(i);
        if (menuItem1.getItemId() == 2131558723 || menuItem1.getItemId() == 2131558725) {
          if (!menuItem1.isVisible())
            menuItem1.setVisible(true); 
        } else if (menuItem1.isVisible()) {
          menuItem1.setVisible(false);
        } 
      } 
    } 
    return true;
  }
  
  protected void setGravityForActionBarCustomView(int paramInt) {
    ActionBar.LayoutParams layoutParams = (ActionBar.LayoutParams)this.mActionBarCustomView.getLayoutParams();
    layoutParams.gravity = paramInt;
    this.mActionBarCustomView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\BaseGalleryActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */