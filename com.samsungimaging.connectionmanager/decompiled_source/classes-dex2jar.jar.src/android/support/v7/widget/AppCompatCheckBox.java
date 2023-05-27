package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TintableCompoundButton;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class AppCompatCheckBox extends CheckBox implements TintableCompoundButton {
  private AppCompatCompoundButtonHelper mCompoundButtonHelper;
  
  private TintManager mTintManager;
  
  public AppCompatCheckBox(Context paramContext) {
    this(paramContext, null);
  }
  
  public AppCompatCheckBox(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.checkboxStyle);
  }
  
  public AppCompatCheckBox(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    this.mTintManager = TintManager.get(paramContext);
    this.mCompoundButtonHelper = new AppCompatCompoundButtonHelper((CompoundButton)this, this.mTintManager);
    this.mCompoundButtonHelper.loadFromAttributes(paramAttributeSet, paramInt);
  }
  
  public int getCompoundPaddingLeft() {
    int j = super.getCompoundPaddingLeft();
    int i = j;
    if (this.mCompoundButtonHelper != null)
      i = this.mCompoundButtonHelper.getCompoundPaddingLeft(j); 
    return i;
  }
  
  @Nullable
  public ColorStateList getSupportButtonTintList() {
    return (this.mCompoundButtonHelper != null) ? this.mCompoundButtonHelper.getSupportButtonTintList() : null;
  }
  
  @Nullable
  public PorterDuff.Mode getSupportButtonTintMode() {
    return (this.mCompoundButtonHelper != null) ? this.mCompoundButtonHelper.getSupportButtonTintMode() : null;
  }
  
  public void setButtonDrawable(@DrawableRes int paramInt) {
    Drawable drawable;
    if (this.mTintManager != null) {
      drawable = this.mTintManager.getDrawable(paramInt);
    } else {
      drawable = ContextCompat.getDrawable(getContext(), paramInt);
    } 
    setButtonDrawable(drawable);
  }
  
  public void setButtonDrawable(Drawable paramDrawable) {
    super.setButtonDrawable(paramDrawable);
    if (this.mCompoundButtonHelper != null)
      this.mCompoundButtonHelper.onSetButtonDrawable(); 
  }
  
  public void setSupportButtonTintList(@Nullable ColorStateList paramColorStateList) {
    if (this.mCompoundButtonHelper != null)
      this.mCompoundButtonHelper.setSupportButtonTintList(paramColorStateList); 
  }
  
  public void setSupportButtonTintMode(@Nullable PorterDuff.Mode paramMode) {
    if (this.mCompoundButtonHelper != null)
      this.mCompoundButtonHelper.setSupportButtonTintMode(paramMode); 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\AppCompatCheckBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */