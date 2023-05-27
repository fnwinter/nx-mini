package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

class AppCompatTextHelperV17 extends AppCompatTextHelper {
  private static final int[] VIEW_ATTRS_v17 = new int[] { 16843666, 16843667 };
  
  private TintInfo mDrawableEndTint;
  
  private TintInfo mDrawableStartTint;
  
  AppCompatTextHelperV17(TextView paramTextView) {
    super(paramTextView);
  }
  
  void applyCompoundDrawablesTints() {
    super.applyCompoundDrawablesTints();
    if (this.mDrawableStartTint != null || this.mDrawableEndTint != null) {
      Drawable[] arrayOfDrawable = this.mView.getCompoundDrawablesRelative();
      applyCompoundDrawableTint(arrayOfDrawable[0], this.mDrawableStartTint);
      applyCompoundDrawableTint(arrayOfDrawable[2], this.mDrawableEndTint);
    } 
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt) {
    super.loadFromAttributes(paramAttributeSet, paramInt);
    Context context = this.mView.getContext();
    TintManager tintManager = TintManager.get(context);
    TypedArray typedArray = context.obtainStyledAttributes(paramAttributeSet, VIEW_ATTRS_v17, paramInt, 0);
    if (typedArray.hasValue(0))
      this.mDrawableStartTint = createTintInfo(context, tintManager, typedArray.getResourceId(0, 0)); 
    if (typedArray.hasValue(1))
      this.mDrawableEndTint = createTintInfo(context, tintManager, typedArray.getResourceId(1, 0)); 
    typedArray.recycle();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\AppCompatTextHelperV17.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */