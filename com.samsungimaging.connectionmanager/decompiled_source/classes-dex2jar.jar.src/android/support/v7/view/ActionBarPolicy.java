package android.support.v7.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v7.appcompat.R;
import android.view.ViewConfiguration;

public class ActionBarPolicy {
  private Context mContext;
  
  private ActionBarPolicy(Context paramContext) {
    this.mContext = paramContext;
  }
  
  public static ActionBarPolicy get(Context paramContext) {
    return new ActionBarPolicy(paramContext);
  }
  
  public boolean enableHomeButtonByDefault() {
    return ((this.mContext.getApplicationInfo()).targetSdkVersion < 14);
  }
  
  public int getEmbeddedMenuWidthLimit() {
    return (this.mContext.getResources().getDisplayMetrics()).widthPixels / 2;
  }
  
  public int getMaxActionButtons() {
    return this.mContext.getResources().getInteger(R.integer.abc_max_action_buttons);
  }
  
  public int getStackedTabMaxWidth() {
    return this.mContext.getResources().getDimensionPixelSize(R.dimen.abc_action_bar_stacked_tab_max_width);
  }
  
  public int getTabContainerHeight() {
    TypedArray typedArray = this.mContext.obtainStyledAttributes(null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
    int j = typedArray.getLayoutDimension(R.styleable.ActionBar_height, 0);
    Resources resources = this.mContext.getResources();
    int i = j;
    if (!hasEmbeddedTabs())
      i = Math.min(j, resources.getDimensionPixelSize(R.dimen.abc_action_bar_stacked_max_height)); 
    typedArray.recycle();
    return i;
  }
  
  public boolean hasEmbeddedTabs() {
    return ((this.mContext.getApplicationInfo()).targetSdkVersion >= 16) ? this.mContext.getResources().getBoolean(R.bool.abc_action_bar_embed_tabs) : this.mContext.getResources().getBoolean(R.bool.abc_action_bar_embed_tabs_pre_jb);
  }
  
  public boolean showsOverflowMenuButton() {
    return !(Build.VERSION.SDK_INT < 19 && ViewConfigurationCompat.hasPermanentMenuKey(ViewConfiguration.get(this.mContext)));
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\view\ActionBarPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */