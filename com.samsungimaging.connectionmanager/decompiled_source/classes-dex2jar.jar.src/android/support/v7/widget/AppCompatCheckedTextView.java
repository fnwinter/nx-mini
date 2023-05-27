package android.support.v7.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.CheckedTextView;
import android.widget.TextView;

public class AppCompatCheckedTextView extends CheckedTextView {
  private static final int[] TINT_ATTRS = new int[] { 16843016 };
  
  private AppCompatTextHelper mTextHelper = AppCompatTextHelper.create((TextView)this);
  
  private TintManager mTintManager;
  
  public AppCompatCheckedTextView(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public AppCompatCheckedTextView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 16843720);
  }
  
  public AppCompatCheckedTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    this.mTextHelper.loadFromAttributes(paramAttributeSet, paramInt);
    this.mTextHelper.applyCompoundDrawablesTints();
    if (TintManager.SHOULD_BE_USED) {
      TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(getContext(), paramAttributeSet, TINT_ATTRS, paramInt, 0);
      setCheckMarkDrawable(tintTypedArray.getDrawable(0));
      tintTypedArray.recycle();
      this.mTintManager = tintTypedArray.getTintManager();
    } 
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    if (this.mTextHelper != null)
      this.mTextHelper.applyCompoundDrawablesTints(); 
  }
  
  public void setCheckMarkDrawable(@DrawableRes int paramInt) {
    if (this.mTintManager != null) {
      setCheckMarkDrawable(this.mTintManager.getDrawable(paramInt));
      return;
    } 
    super.setCheckMarkDrawable(paramInt);
  }
  
  public void setTextAppearance(Context paramContext, int paramInt) {
    super.setTextAppearance(paramContext, paramInt);
    if (this.mTextHelper != null)
      this.mTextHelper.onSetTextAppearance(paramContext, paramInt); 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\AppCompatCheckedTextView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */