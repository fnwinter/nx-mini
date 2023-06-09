package android.support.v7.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.PopupWindow;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class AppCompatPopupWindow extends PopupWindow {
  private static final boolean COMPAT_OVERLAP_ANCHOR;
  
  private static final String TAG = "AppCompatPopupWindow";
  
  private boolean mOverlapAnchor;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT < 21) {
      bool = true;
    } else {
      bool = false;
    } 
    COMPAT_OVERLAP_ANCHOR = bool;
  }
  
  public AppCompatPopupWindow(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(paramContext, paramAttributeSet, R.styleable.PopupWindow, paramInt, 0);
    if (tintTypedArray.hasValue(R.styleable.PopupWindow_overlapAnchor))
      setSupportOverlapAnchor(tintTypedArray.getBoolean(R.styleable.PopupWindow_overlapAnchor, false)); 
    setBackgroundDrawable(tintTypedArray.getDrawable(R.styleable.PopupWindow_android_popupBackground));
    tintTypedArray.recycle();
    if (Build.VERSION.SDK_INT < 14)
      wrapOnScrollChangedListener(this); 
  }
  
  private static void wrapOnScrollChangedListener(final PopupWindow popup) {
    try {
      final Field fieldAnchor = PopupWindow.class.getDeclaredField("mAnchor");
      field1.setAccessible(true);
      Field field2 = PopupWindow.class.getDeclaredField("mOnScrollChangedListener");
      field2.setAccessible(true);
      field2.set(popup, new ViewTreeObserver.OnScrollChangedListener() {
            public void onScrollChanged() {
              try {
                WeakReference weakReference = (WeakReference)fieldAnchor.get(popup);
                if (weakReference != null) {
                  if (weakReference.get() == null)
                    return; 
                  originalListener.onScrollChanged();
                  return;
                } 
              } catch (IllegalAccessException illegalAccessException) {}
            }
          });
      return;
    } catch (Exception exception) {
      Log.d("AppCompatPopupWindow", "Exception while installing workaround OnScrollChangedListener", exception);
      return;
    } 
  }
  
  public boolean getSupportOverlapAnchor() {
    return COMPAT_OVERLAP_ANCHOR ? this.mOverlapAnchor : PopupWindowCompat.getOverlapAnchor(this);
  }
  
  public void setSupportOverlapAnchor(boolean paramBoolean) {
    if (COMPAT_OVERLAP_ANCHOR) {
      this.mOverlapAnchor = paramBoolean;
      return;
    } 
    PopupWindowCompat.setOverlapAnchor(this, paramBoolean);
  }
  
  public void showAsDropDown(View paramView, int paramInt1, int paramInt2) {
    int i = paramInt2;
    if (COMPAT_OVERLAP_ANCHOR) {
      i = paramInt2;
      if (this.mOverlapAnchor)
        i = paramInt2 - paramView.getHeight(); 
    } 
    super.showAsDropDown(paramView, paramInt1, i);
  }
  
  @TargetApi(19)
  public void showAsDropDown(View paramView, int paramInt1, int paramInt2, int paramInt3) {
    int i = paramInt2;
    if (COMPAT_OVERLAP_ANCHOR) {
      i = paramInt2;
      if (this.mOverlapAnchor)
        i = paramInt2 - paramView.getHeight(); 
    } 
    super.showAsDropDown(paramView, paramInt1, i, paramInt3);
  }
  
  public void update(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    int i = paramInt2;
    if (COMPAT_OVERLAP_ANCHOR) {
      i = paramInt2;
      if (this.mOverlapAnchor)
        i = paramInt2 - paramView.getHeight(); 
    } 
    super.update(paramView, paramInt1, i, paramInt3, paramInt4);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\AppCompatPopupWindow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */