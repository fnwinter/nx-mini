package android.support.v7.widget;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

class AppCompatImageHelper {
  private static final int[] VIEW_ATTRS = new int[] { 16843033 };
  
  private final TintManager mTintManager;
  
  private final ImageView mView;
  
  AppCompatImageHelper(ImageView paramImageView, TintManager paramTintManager) {
    this.mView = paramImageView;
    this.mTintManager = paramTintManager;
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt) {
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(this.mView.getContext(), paramAttributeSet, VIEW_ATTRS, paramInt, 0);
    try {
      if (tintTypedArray.hasValue(0))
        this.mView.setImageDrawable(tintTypedArray.getDrawable(0)); 
      return;
    } finally {
      tintTypedArray.recycle();
    } 
  }
  
  void setImageResource(int paramInt) {
    if (paramInt != 0) {
      Drawable drawable;
      ImageView imageView = this.mView;
      if (this.mTintManager != null) {
        drawable = this.mTintManager.getDrawable(paramInt);
      } else {
        drawable = ContextCompat.getDrawable(this.mView.getContext(), paramInt);
      } 
      imageView.setImageDrawable(drawable);
      return;
    } 
    this.mView.setImageDrawable(null);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\AppCompatImageHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */