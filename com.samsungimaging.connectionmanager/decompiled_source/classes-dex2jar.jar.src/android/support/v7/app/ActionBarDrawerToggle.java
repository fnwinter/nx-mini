package android.support.v7.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

public class ActionBarDrawerToggle implements DrawerLayout.DrawerListener {
  private final Delegate mActivityImpl;
  
  private final int mCloseDrawerContentDescRes;
  
  private boolean mDrawerIndicatorEnabled = true;
  
  private final DrawerLayout mDrawerLayout;
  
  private boolean mHasCustomUpIndicator;
  
  private Drawable mHomeAsUpIndicator;
  
  private final int mOpenDrawerContentDescRes;
  
  private DrawerToggle mSlider;
  
  private View.OnClickListener mToolbarNavigationClickListener;
  
  private boolean mWarnedForDisplayHomeAsUp = false;
  
  public ActionBarDrawerToggle(Activity paramActivity, DrawerLayout paramDrawerLayout, @StringRes int paramInt1, @StringRes int paramInt2) {
    this(paramActivity, null, paramDrawerLayout, null, paramInt1, paramInt2);
  }
  
  public ActionBarDrawerToggle(Activity paramActivity, DrawerLayout paramDrawerLayout, Toolbar paramToolbar, @StringRes int paramInt1, @StringRes int paramInt2) {
    this(paramActivity, paramToolbar, paramDrawerLayout, null, paramInt1, paramInt2);
  }
  
  <T extends Drawable & DrawerToggle> ActionBarDrawerToggle(Activity paramActivity, Toolbar paramToolbar, DrawerLayout paramDrawerLayout, T paramT, @StringRes int paramInt1, @StringRes int paramInt2) {
    if (paramToolbar != null) {
      this.mActivityImpl = new ToolbarCompatDelegate(paramToolbar);
      paramToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              if (ActionBarDrawerToggle.this.mDrawerIndicatorEnabled) {
                ActionBarDrawerToggle.this.toggle();
                return;
              } 
              if (ActionBarDrawerToggle.this.mToolbarNavigationClickListener != null) {
                ActionBarDrawerToggle.this.mToolbarNavigationClickListener.onClick(param1View);
                return;
              } 
            }
          });
    } else if (paramActivity instanceof DelegateProvider) {
      this.mActivityImpl = ((DelegateProvider)paramActivity).getDrawerToggleDelegate();
    } else if (Build.VERSION.SDK_INT >= 18) {
      this.mActivityImpl = new JellybeanMr2Delegate(paramActivity);
    } else if (Build.VERSION.SDK_INT >= 11) {
      this.mActivityImpl = new HoneycombDelegate(paramActivity);
    } else {
      this.mActivityImpl = new DummyDelegate(paramActivity);
    } 
    this.mDrawerLayout = paramDrawerLayout;
    this.mOpenDrawerContentDescRes = paramInt1;
    this.mCloseDrawerContentDescRes = paramInt2;
    if (paramT == null) {
      this.mSlider = new DrawerArrowDrawableToggle(paramActivity, this.mActivityImpl.getActionBarThemedContext());
    } else {
      this.mSlider = (DrawerToggle)paramT;
    } 
    this.mHomeAsUpIndicator = getThemeUpIndicator();
  }
  
  private void toggle() {
    if (this.mDrawerLayout.isDrawerVisible(8388611)) {
      this.mDrawerLayout.closeDrawer(8388611);
      return;
    } 
    this.mDrawerLayout.openDrawer(8388611);
  }
  
  Drawable getThemeUpIndicator() {
    return this.mActivityImpl.getThemeUpIndicator();
  }
  
  public View.OnClickListener getToolbarNavigationClickListener() {
    return this.mToolbarNavigationClickListener;
  }
  
  public boolean isDrawerIndicatorEnabled() {
    return this.mDrawerIndicatorEnabled;
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    if (!this.mHasCustomUpIndicator)
      this.mHomeAsUpIndicator = getThemeUpIndicator(); 
    syncState();
  }
  
  public void onDrawerClosed(View paramView) {
    this.mSlider.setPosition(0.0F);
    if (this.mDrawerIndicatorEnabled)
      setActionBarDescription(this.mOpenDrawerContentDescRes); 
  }
  
  public void onDrawerOpened(View paramView) {
    this.mSlider.setPosition(1.0F);
    if (this.mDrawerIndicatorEnabled)
      setActionBarDescription(this.mCloseDrawerContentDescRes); 
  }
  
  public void onDrawerSlide(View paramView, float paramFloat) {
    this.mSlider.setPosition(Math.min(1.0F, Math.max(0.0F, paramFloat)));
  }
  
  public void onDrawerStateChanged(int paramInt) {}
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
    if (paramMenuItem != null && paramMenuItem.getItemId() == 16908332 && this.mDrawerIndicatorEnabled) {
      toggle();
      return true;
    } 
    return false;
  }
  
  void setActionBarDescription(int paramInt) {
    this.mActivityImpl.setActionBarDescription(paramInt);
  }
  
  void setActionBarUpIndicator(Drawable paramDrawable, int paramInt) {
    if (!this.mWarnedForDisplayHomeAsUp && !this.mActivityImpl.isNavigationVisible()) {
      Log.w("ActionBarDrawerToggle", "DrawerToggle may not show up because NavigationIcon is not visible. You may need to call actionbar.setDisplayHomeAsUpEnabled(true);");
      this.mWarnedForDisplayHomeAsUp = true;
    } 
    this.mActivityImpl.setActionBarUpIndicator(paramDrawable, paramInt);
  }
  
  public void setDrawerIndicatorEnabled(boolean paramBoolean) {
    if (paramBoolean != this.mDrawerIndicatorEnabled) {
      if (paramBoolean) {
        int i;
        Drawable drawable = (Drawable)this.mSlider;
        if (this.mDrawerLayout.isDrawerOpen(8388611)) {
          i = this.mCloseDrawerContentDescRes;
        } else {
          i = this.mOpenDrawerContentDescRes;
        } 
        setActionBarUpIndicator(drawable, i);
      } else {
        setActionBarUpIndicator(this.mHomeAsUpIndicator, 0);
      } 
      this.mDrawerIndicatorEnabled = paramBoolean;
    } 
  }
  
  public void setHomeAsUpIndicator(int paramInt) {
    Drawable drawable = null;
    if (paramInt != 0)
      drawable = this.mDrawerLayout.getResources().getDrawable(paramInt); 
    setHomeAsUpIndicator(drawable);
  }
  
  public void setHomeAsUpIndicator(Drawable paramDrawable) {
    if (paramDrawable == null) {
      this.mHomeAsUpIndicator = getThemeUpIndicator();
      this.mHasCustomUpIndicator = false;
    } else {
      this.mHomeAsUpIndicator = paramDrawable;
      this.mHasCustomUpIndicator = true;
    } 
    if (!this.mDrawerIndicatorEnabled)
      setActionBarUpIndicator(this.mHomeAsUpIndicator, 0); 
  }
  
  public void setToolbarNavigationClickListener(View.OnClickListener paramOnClickListener) {
    this.mToolbarNavigationClickListener = paramOnClickListener;
  }
  
  public void syncState() {
    if (this.mDrawerLayout.isDrawerOpen(8388611)) {
      this.mSlider.setPosition(1.0F);
    } else {
      this.mSlider.setPosition(0.0F);
    } 
    if (this.mDrawerIndicatorEnabled) {
      int i;
      Drawable drawable = (Drawable)this.mSlider;
      if (this.mDrawerLayout.isDrawerOpen(8388611)) {
        i = this.mCloseDrawerContentDescRes;
      } else {
        i = this.mOpenDrawerContentDescRes;
      } 
      setActionBarUpIndicator(drawable, i);
    } 
  }
  
  public static interface Delegate {
    Context getActionBarThemedContext();
    
    Drawable getThemeUpIndicator();
    
    boolean isNavigationVisible();
    
    void setActionBarDescription(@StringRes int param1Int);
    
    void setActionBarUpIndicator(Drawable param1Drawable, @StringRes int param1Int);
  }
  
  public static interface DelegateProvider {
    @Nullable
    ActionBarDrawerToggle.Delegate getDrawerToggleDelegate();
  }
  
  static class DrawerArrowDrawableToggle extends DrawerArrowDrawable implements DrawerToggle {
    private final Activity mActivity;
    
    public DrawerArrowDrawableToggle(Activity param1Activity, Context param1Context) {
      super(param1Context);
      this.mActivity = param1Activity;
    }
    
    public float getPosition() {
      return getProgress();
    }
    
    public void setPosition(float param1Float) {
      if (param1Float == 1.0F) {
        setVerticalMirror(true);
      } else if (param1Float == 0.0F) {
        setVerticalMirror(false);
      } 
      setProgress(param1Float);
    }
  }
  
  static interface DrawerToggle {
    float getPosition();
    
    void setPosition(float param1Float);
  }
  
  static class DummyDelegate implements Delegate {
    final Activity mActivity;
    
    DummyDelegate(Activity param1Activity) {
      this.mActivity = param1Activity;
    }
    
    public Context getActionBarThemedContext() {
      return (Context)this.mActivity;
    }
    
    public Drawable getThemeUpIndicator() {
      return null;
    }
    
    public boolean isNavigationVisible() {
      return true;
    }
    
    public void setActionBarDescription(@StringRes int param1Int) {}
    
    public void setActionBarUpIndicator(Drawable param1Drawable, @StringRes int param1Int) {}
  }
  
  private static class HoneycombDelegate implements Delegate {
    final Activity mActivity;
    
    ActionBarDrawerToggleHoneycomb.SetIndicatorInfo mSetIndicatorInfo;
    
    private HoneycombDelegate(Activity param1Activity) {
      this.mActivity = param1Activity;
    }
    
    public Context getActionBarThemedContext() {
      ActionBar actionBar = this.mActivity.getActionBar();
      return (Context)((actionBar != null) ? actionBar.getThemedContext() : this.mActivity);
    }
    
    public Drawable getThemeUpIndicator() {
      return ActionBarDrawerToggleHoneycomb.getThemeUpIndicator(this.mActivity);
    }
    
    public boolean isNavigationVisible() {
      ActionBar actionBar = this.mActivity.getActionBar();
      return (actionBar != null && (actionBar.getDisplayOptions() & 0x4) != 0);
    }
    
    public void setActionBarDescription(int param1Int) {
      this.mSetIndicatorInfo = ActionBarDrawerToggleHoneycomb.setActionBarDescription(this.mSetIndicatorInfo, this.mActivity, param1Int);
    }
    
    public void setActionBarUpIndicator(Drawable param1Drawable, int param1Int) {
      this.mActivity.getActionBar().setDisplayShowHomeEnabled(true);
      this.mSetIndicatorInfo = ActionBarDrawerToggleHoneycomb.setActionBarUpIndicator(this.mSetIndicatorInfo, this.mActivity, param1Drawable, param1Int);
      this.mActivity.getActionBar().setDisplayShowHomeEnabled(false);
    }
  }
  
  private static class JellybeanMr2Delegate implements Delegate {
    final Activity mActivity;
    
    private JellybeanMr2Delegate(Activity param1Activity) {
      this.mActivity = param1Activity;
    }
    
    public Context getActionBarThemedContext() {
      ActionBar actionBar = this.mActivity.getActionBar();
      return (Context)((actionBar != null) ? actionBar.getThemedContext() : this.mActivity);
    }
    
    public Drawable getThemeUpIndicator() {
      TypedArray typedArray = getActionBarThemedContext().obtainStyledAttributes(null, new int[] { 16843531 }, 16843470, 0);
      Drawable drawable = typedArray.getDrawable(0);
      typedArray.recycle();
      return drawable;
    }
    
    public boolean isNavigationVisible() {
      ActionBar actionBar = this.mActivity.getActionBar();
      return (actionBar != null && (actionBar.getDisplayOptions() & 0x4) != 0);
    }
    
    public void setActionBarDescription(int param1Int) {
      ActionBar actionBar = this.mActivity.getActionBar();
      if (actionBar != null)
        actionBar.setHomeActionContentDescription(param1Int); 
    }
    
    public void setActionBarUpIndicator(Drawable param1Drawable, int param1Int) {
      ActionBar actionBar = this.mActivity.getActionBar();
      if (actionBar != null) {
        actionBar.setHomeAsUpIndicator(param1Drawable);
        actionBar.setHomeActionContentDescription(param1Int);
      } 
    }
  }
  
  static class ToolbarCompatDelegate implements Delegate {
    final CharSequence mDefaultContentDescription;
    
    final Drawable mDefaultUpIndicator;
    
    final Toolbar mToolbar;
    
    ToolbarCompatDelegate(Toolbar param1Toolbar) {
      this.mToolbar = param1Toolbar;
      this.mDefaultUpIndicator = param1Toolbar.getNavigationIcon();
      this.mDefaultContentDescription = param1Toolbar.getNavigationContentDescription();
    }
    
    public Context getActionBarThemedContext() {
      return this.mToolbar.getContext();
    }
    
    public Drawable getThemeUpIndicator() {
      return this.mDefaultUpIndicator;
    }
    
    public boolean isNavigationVisible() {
      return true;
    }
    
    public void setActionBarDescription(@StringRes int param1Int) {
      if (param1Int == 0) {
        this.mToolbar.setNavigationContentDescription(this.mDefaultContentDescription);
        return;
      } 
      this.mToolbar.setNavigationContentDescription(param1Int);
    }
    
    public void setActionBarUpIndicator(Drawable param1Drawable, @StringRes int param1Int) {
      this.mToolbar.setNavigationIcon(param1Drawable);
      setActionBarDescription(param1Int);
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\app\ActionBarDrawerToggle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */