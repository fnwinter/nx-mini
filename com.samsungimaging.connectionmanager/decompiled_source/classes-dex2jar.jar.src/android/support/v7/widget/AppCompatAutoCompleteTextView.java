package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.view.TintableBackgroundView;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class AppCompatAutoCompleteTextView extends AutoCompleteTextView implements TintableBackgroundView {
  private static final int[] TINT_ATTRS = new int[] { 16843126 };
  
  private AppCompatBackgroundHelper mBackgroundTintHelper;
  
  private AppCompatTextHelper mTextHelper;
  
  private TintManager mTintManager;
  
  public AppCompatAutoCompleteTextView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public AppCompatAutoCompleteTextView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.autoCompleteTextViewStyle);
  }
  
  public AppCompatAutoCompleteTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(TintContextWrapper.wrap(paramContext), paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(getContext(), paramAttributeSet, TINT_ATTRS, paramInt, 0);
    this.mTintManager = tintTypedArray.getTintManager();
    if (tintTypedArray.hasValue(0))
      setDropDownBackgroundDrawable(tintTypedArray.getDrawable(0)); 
    tintTypedArray.recycle();
    this.mBackgroundTintHelper = new AppCompatBackgroundHelper((View)this, this.mTintManager);
    this.mBackgroundTintHelper.loadFromAttributes(paramAttributeSet, paramInt);
    this.mTextHelper = AppCompatTextHelper.create((TextView)this);
    this.mTextHelper.loadFromAttributes(paramAttributeSet, paramInt);
    this.mTextHelper.applyCompoundDrawablesTints();
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    if (this.mBackgroundTintHelper != null)
      this.mBackgroundTintHelper.applySupportBackgroundTint(); 
    if (this.mTextHelper != null)
      this.mTextHelper.applyCompoundDrawablesTints(); 
  }
  
  @Nullable
  public ColorStateList getSupportBackgroundTintList() {
    return (this.mBackgroundTintHelper != null) ? this.mBackgroundTintHelper.getSupportBackgroundTintList() : null;
  }
  
  @Nullable
  public PorterDuff.Mode getSupportBackgroundTintMode() {
    return (this.mBackgroundTintHelper != null) ? this.mBackgroundTintHelper.getSupportBackgroundTintMode() : null;
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable) {
    super.setBackgroundDrawable(paramDrawable);
    if (this.mBackgroundTintHelper != null)
      this.mBackgroundTintHelper.onSetBackgroundDrawable(paramDrawable); 
  }
  
  public void setBackgroundResource(@DrawableRes int paramInt) {
    super.setBackgroundResource(paramInt);
    if (this.mBackgroundTintHelper != null)
      this.mBackgroundTintHelper.onSetBackgroundResource(paramInt); 
  }
  
  public void setDropDownBackgroundResource(@DrawableRes int paramInt) {
    if (this.mTintManager != null) {
      setDropDownBackgroundDrawable(this.mTintManager.getDrawable(paramInt));
      return;
    } 
    super.setDropDownBackgroundResource(paramInt);
  }
  
  public void setSupportBackgroundTintList(@Nullable ColorStateList paramColorStateList) {
    if (this.mBackgroundTintHelper != null)
      this.mBackgroundTintHelper.setSupportBackgroundTintList(paramColorStateList); 
  }
  
  public void setSupportBackgroundTintMode(@Nullable PorterDuff.Mode paramMode) {
    if (this.mBackgroundTintHelper != null)
      this.mBackgroundTintHelper.setSupportBackgroundTintMode(paramMode); 
  }
  
  public void setTextAppearance(Context paramContext, int paramInt) {
    super.setTextAppearance(paramContext, paramInt);
    if (this.mTextHelper != null)
      this.mTextHelper.onSetTextAppearance(paramContext, paramInt); 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\AppCompatAutoCompleteTextView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */