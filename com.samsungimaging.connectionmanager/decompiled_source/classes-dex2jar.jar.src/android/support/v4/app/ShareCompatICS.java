package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ActionProvider;
import android.view.MenuItem;
import android.widget.ShareActionProvider;

class ShareCompatICS {
  private static final String HISTORY_FILENAME_PREFIX = ".sharecompat_";
  
  public static void configureMenuItem(MenuItem paramMenuItem, Activity paramActivity, Intent paramIntent) {
    ShareActionProvider shareActionProvider;
    ActionProvider actionProvider = paramMenuItem.getActionProvider();
    if (!(actionProvider instanceof ShareActionProvider)) {
      shareActionProvider = new ShareActionProvider((Context)paramActivity);
    } else {
      shareActionProvider = shareActionProvider;
    } 
    shareActionProvider.setShareHistoryFileName(".sharecompat_" + paramActivity.getClass().getName());
    shareActionProvider.setShareIntent(paramIntent);
    paramMenuItem.setActionProvider((ActionProvider)shareActionProvider);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\app\ShareCompatICS.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */