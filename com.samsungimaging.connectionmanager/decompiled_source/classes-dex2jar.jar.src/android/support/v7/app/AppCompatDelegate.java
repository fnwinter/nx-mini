package android.support.v7.app;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public abstract class AppCompatDelegate {
  public static final int FEATURE_ACTION_MODE_OVERLAY = 10;
  
  public static final int FEATURE_SUPPORT_ACTION_BAR = 108;
  
  public static final int FEATURE_SUPPORT_ACTION_BAR_OVERLAY = 109;
  
  static final String TAG = "AppCompatDelegate";
  
  public static AppCompatDelegate create(Activity paramActivity, AppCompatCallback paramAppCompatCallback) {
    return create((Context)paramActivity, paramActivity.getWindow(), paramAppCompatCallback);
  }
  
  public static AppCompatDelegate create(Dialog paramDialog, AppCompatCallback paramAppCompatCallback) {
    return create(paramDialog.getContext(), paramDialog.getWindow(), paramAppCompatCallback);
  }
  
  private static AppCompatDelegate create(Context paramContext, Window paramWindow, AppCompatCallback paramAppCompatCallback) {
    int i = Build.VERSION.SDK_INT;
    return (i >= 23) ? new AppCompatDelegateImplV23(paramContext, paramWindow, paramAppCompatCallback) : ((i >= 14) ? new AppCompatDelegateImplV14(paramContext, paramWindow, paramAppCompatCallback) : ((i >= 11) ? new AppCompatDelegateImplV11(paramContext, paramWindow, paramAppCompatCallback) : new AppCompatDelegateImplV7(paramContext, paramWindow, paramAppCompatCallback)));
  }
  
  public abstract void addContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams);
  
  public abstract View createView(View paramView, String paramString, @NonNull Context paramContext, @NonNull AttributeSet paramAttributeSet);
  
  public abstract ActionBarDrawerToggle.Delegate getDrawerToggleDelegate();
  
  public abstract MenuInflater getMenuInflater();
  
  public abstract ActionBar getSupportActionBar();
  
  public abstract boolean hasWindowFeature(int paramInt);
  
  public abstract void installViewFactory();
  
  public abstract void invalidateOptionsMenu();
  
  public abstract boolean isHandleNativeActionModesEnabled();
  
  public abstract void onConfigurationChanged(Configuration paramConfiguration);
  
  public abstract void onCreate(Bundle paramBundle);
  
  public abstract void onDestroy();
  
  public abstract void onPostCreate(Bundle paramBundle);
  
  public abstract void onPostResume();
  
  public abstract void onStop();
  
  public abstract boolean requestWindowFeature(int paramInt);
  
  public abstract void setContentView(@LayoutRes int paramInt);
  
  public abstract void setContentView(View paramView);
  
  public abstract void setContentView(View paramView, ViewGroup.LayoutParams paramLayoutParams);
  
  public abstract void setHandleNativeActionModesEnabled(boolean paramBoolean);
  
  public abstract void setSupportActionBar(Toolbar paramToolbar);
  
  public abstract void setTitle(CharSequence paramCharSequence);
  
  public abstract ActionMode startSupportActionMode(ActionMode.Callback paramCallback);
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\app\AppCompatDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */