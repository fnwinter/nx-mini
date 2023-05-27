package android.support.v4.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.view.View;
import java.lang.reflect.Field;

class ViewCompatBase {
  private static final String TAG = "ViewCompatBase";
  
  private static Field sMinHeightField;
  
  private static boolean sMinHeightFieldFetched;
  
  private static Field sMinWidthField;
  
  private static boolean sMinWidthFieldFetched;
  
  static ColorStateList getBackgroundTintList(View paramView) {
    return (paramView instanceof TintableBackgroundView) ? ((TintableBackgroundView)paramView).getSupportBackgroundTintList() : null;
  }
  
  static PorterDuff.Mode getBackgroundTintMode(View paramView) {
    return (paramView instanceof TintableBackgroundView) ? ((TintableBackgroundView)paramView).getSupportBackgroundTintMode() : null;
  }
  
  static int getMinimumHeight(View paramView) {
    if (!sMinHeightFieldFetched) {
      try {
        sMinHeightField = View.class.getDeclaredField("mMinHeight");
        sMinHeightField.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException) {}
      sMinHeightFieldFetched = true;
    } 
    if (sMinHeightField != null)
      try {
        return ((Integer)sMinHeightField.get(paramView)).intValue();
      } catch (Exception exception) {} 
    return 0;
  }
  
  static int getMinimumWidth(View paramView) {
    if (!sMinWidthFieldFetched) {
      try {
        sMinWidthField = View.class.getDeclaredField("mMinWidth");
        sMinWidthField.setAccessible(true);
      } catch (NoSuchFieldException noSuchFieldException) {}
      sMinWidthFieldFetched = true;
    } 
    if (sMinWidthField != null)
      try {
        return ((Integer)sMinWidthField.get(paramView)).intValue();
      } catch (Exception exception) {} 
    return 0;
  }
  
  static boolean isAttachedToWindow(View paramView) {
    return (paramView.getWindowToken() != null);
  }
  
  static boolean isLaidOut(View paramView) {
    return (paramView.getWidth() > 0 && paramView.getHeight() > 0);
  }
  
  static void setBackgroundTintList(View paramView, ColorStateList paramColorStateList) {
    if (paramView instanceof TintableBackgroundView)
      ((TintableBackgroundView)paramView).setSupportBackgroundTintList(paramColorStateList); 
  }
  
  static void setBackgroundTintMode(View paramView, PorterDuff.Mode paramMode) {
    if (paramView instanceof TintableBackgroundView)
      ((TintableBackgroundView)paramView).setSupportBackgroundTintMode(paramMode); 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\view\ViewCompatBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */