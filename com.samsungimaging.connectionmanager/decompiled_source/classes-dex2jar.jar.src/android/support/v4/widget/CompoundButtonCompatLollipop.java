package android.support.v4.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.widget.CompoundButton;

class CompoundButtonCompatLollipop {
  static ColorStateList getButtonTintList(CompoundButton paramCompoundButton) {
    return paramCompoundButton.getButtonTintList();
  }
  
  static PorterDuff.Mode getButtonTintMode(CompoundButton paramCompoundButton) {
    return paramCompoundButton.getButtonTintMode();
  }
  
  static void setButtonTintList(CompoundButton paramCompoundButton, ColorStateList paramColorStateList) {
    paramCompoundButton.setButtonTintList(paramColorStateList);
  }
  
  static void setButtonTintMode(CompoundButton paramCompoundButton, PorterDuff.Mode paramMode) {
    paramCompoundButton.setButtonTintMode(paramMode);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\widget\CompoundButtonCompatLollipop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */