package android.support.v7.widget;

import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.graphics.drawable.DrawableUtils;
import android.util.AttributeSet;
import android.view.View;

class AppCompatBackgroundHelper {
  private TintInfo mBackgroundTint;
  
  private TintInfo mInternalBackgroundTint;
  
  private final TintManager mTintManager;
  
  private final View mView;
  
  AppCompatBackgroundHelper(View paramView, TintManager paramTintManager) {
    this.mView = paramView;
    this.mTintManager = paramTintManager;
  }
  
  void applySupportBackgroundTint() {
    Drawable drawable = this.mView.getBackground();
    if (drawable != null) {
      if (this.mBackgroundTint != null) {
        TintManager.tintDrawable(drawable, this.mBackgroundTint, this.mView.getDrawableState());
        return;
      } 
    } else {
      return;
    } 
    if (this.mInternalBackgroundTint != null) {
      TintManager.tintDrawable(drawable, this.mInternalBackgroundTint, this.mView.getDrawableState());
      return;
    } 
  }
  
  ColorStateList getSupportBackgroundTintList() {
    return (this.mBackgroundTint != null) ? this.mBackgroundTint.mTintList : null;
  }
  
  PorterDuff.Mode getSupportBackgroundTintMode() {
    return (this.mBackgroundTint != null) ? this.mBackgroundTint.mTintMode : null;
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt) {
    TypedArray typedArray = this.mView.getContext().obtainStyledAttributes(paramAttributeSet, R.styleable.ViewBackgroundHelper, paramInt, 0);
    try {
      if (typedArray.hasValue(R.styleable.ViewBackgroundHelper_android_background)) {
        ColorStateList colorStateList = this.mTintManager.getTintList(typedArray.getResourceId(R.styleable.ViewBackgroundHelper_android_background, -1));
        if (colorStateList != null)
          setInternalBackgroundTint(colorStateList); 
      } 
      if (typedArray.hasValue(R.styleable.ViewBackgroundHelper_backgroundTint))
        ViewCompat.setBackgroundTintList(this.mView, typedArray.getColorStateList(R.styleable.ViewBackgroundHelper_backgroundTint)); 
      if (typedArray.hasValue(R.styleable.ViewBackgroundHelper_backgroundTintMode))
        ViewCompat.setBackgroundTintMode(this.mView, DrawableUtils.parseTintMode(typedArray.getInt(R.styleable.ViewBackgroundHelper_backgroundTintMode, -1), null)); 
      return;
    } finally {
      typedArray.recycle();
    } 
  }
  
  void onSetBackgroundDrawable(Drawable paramDrawable) {
    setInternalBackgroundTint(null);
  }
  
  void onSetBackgroundResource(int paramInt) {
    ColorStateList colorStateList;
    if (this.mTintManager != null) {
      colorStateList = this.mTintManager.getTintList(paramInt);
    } else {
      colorStateList = null;
    } 
    setInternalBackgroundTint(colorStateList);
  }
  
  void setInternalBackgroundTint(ColorStateList paramColorStateList) {
    if (paramColorStateList != null) {
      if (this.mInternalBackgroundTint == null)
        this.mInternalBackgroundTint = new TintInfo(); 
      this.mInternalBackgroundTint.mTintList = paramColorStateList;
      this.mInternalBackgroundTint.mHasTintList = true;
    } else {
      this.mInternalBackgroundTint = null;
    } 
    applySupportBackgroundTint();
  }
  
  void setSupportBackgroundTintList(ColorStateList paramColorStateList) {
    if (this.mBackgroundTint == null)
      this.mBackgroundTint = new TintInfo(); 
    this.mBackgroundTint.mTintList = paramColorStateList;
    this.mBackgroundTint.mHasTintList = true;
    applySupportBackgroundTint();
  }
  
  void setSupportBackgroundTintMode(PorterDuff.Mode paramMode) {
    if (this.mBackgroundTint == null)
      this.mBackgroundTint = new TintInfo(); 
    this.mBackgroundTint.mTintMode = paramMode;
    this.mBackgroundTint.mHasTintMode = true;
    applySupportBackgroundTint();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\AppCompatBackgroundHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */