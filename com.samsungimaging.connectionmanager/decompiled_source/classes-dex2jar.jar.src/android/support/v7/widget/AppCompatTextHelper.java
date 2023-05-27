package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.appcompat.R;
import android.support.v7.text.AllCapsTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.widget.TextView;

class AppCompatTextHelper {
  private static final int[] TEXT_APPEARANCE_ATTRS;
  
  private static final int[] VIEW_ATTRS = new int[] { 16842804, 16843119, 16843117, 16843120, 16843118 };
  
  private TintInfo mDrawableBottomTint;
  
  private TintInfo mDrawableLeftTint;
  
  private TintInfo mDrawableRightTint;
  
  private TintInfo mDrawableTopTint;
  
  final TextView mView;
  
  static {
    TEXT_APPEARANCE_ATTRS = new int[] { R.attr.textAllCaps };
  }
  
  AppCompatTextHelper(TextView paramTextView) {
    this.mView = paramTextView;
  }
  
  static AppCompatTextHelper create(TextView paramTextView) {
    return (Build.VERSION.SDK_INT >= 17) ? new AppCompatTextHelperV17(paramTextView) : new AppCompatTextHelper(paramTextView);
  }
  
  protected static TintInfo createTintInfo(Context paramContext, TintManager paramTintManager, int paramInt) {
    ColorStateList colorStateList = paramTintManager.getTintList(paramInt);
    if (colorStateList != null) {
      TintInfo tintInfo = new TintInfo();
      tintInfo.mHasTintList = true;
      tintInfo.mTintList = colorStateList;
    } 
    return null;
  }
  
  final void applyCompoundDrawableTint(Drawable paramDrawable, TintInfo paramTintInfo) {
    if (paramDrawable != null && paramTintInfo != null)
      TintManager.tintDrawable(paramDrawable, paramTintInfo, this.mView.getDrawableState()); 
  }
  
  void applyCompoundDrawablesTints() {
    if (this.mDrawableLeftTint != null || this.mDrawableTopTint != null || this.mDrawableRightTint != null || this.mDrawableBottomTint != null) {
      Drawable[] arrayOfDrawable = this.mView.getCompoundDrawables();
      applyCompoundDrawableTint(arrayOfDrawable[0], this.mDrawableLeftTint);
      applyCompoundDrawableTint(arrayOfDrawable[1], this.mDrawableTopTint);
      applyCompoundDrawableTint(arrayOfDrawable[2], this.mDrawableRightTint);
      applyCompoundDrawableTint(arrayOfDrawable[3], this.mDrawableBottomTint);
    } 
  }
  
  void loadFromAttributes(AttributeSet paramAttributeSet, int paramInt) {
    Context context = this.mView.getContext();
    TintManager tintManager = TintManager.get(context);
    TypedArray typedArray2 = context.obtainStyledAttributes(paramAttributeSet, VIEW_ATTRS, paramInt, 0);
    int i = typedArray2.getResourceId(0, -1);
    if (typedArray2.hasValue(1))
      this.mDrawableLeftTint = createTintInfo(context, tintManager, typedArray2.getResourceId(1, 0)); 
    if (typedArray2.hasValue(2))
      this.mDrawableTopTint = createTintInfo(context, tintManager, typedArray2.getResourceId(2, 0)); 
    if (typedArray2.hasValue(3))
      this.mDrawableRightTint = createTintInfo(context, tintManager, typedArray2.getResourceId(3, 0)); 
    if (typedArray2.hasValue(4))
      this.mDrawableBottomTint = createTintInfo(context, tintManager, typedArray2.getResourceId(4, 0)); 
    typedArray2.recycle();
    if (i != -1) {
      TypedArray typedArray = context.obtainStyledAttributes(i, R.styleable.TextAppearance);
      if (typedArray.hasValue(R.styleable.TextAppearance_textAllCaps))
        setAllCaps(typedArray.getBoolean(R.styleable.TextAppearance_textAllCaps, false)); 
      typedArray.recycle();
    } 
    TypedArray typedArray1 = context.obtainStyledAttributes(paramAttributeSet, TEXT_APPEARANCE_ATTRS, paramInt, 0);
    if (typedArray1.getBoolean(0, false))
      setAllCaps(true); 
    typedArray1.recycle();
  }
  
  void onSetTextAppearance(Context paramContext, int paramInt) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramInt, TEXT_APPEARANCE_ATTRS);
    if (typedArray.hasValue(0))
      setAllCaps(typedArray.getBoolean(0, false)); 
    typedArray.recycle();
  }
  
  void setAllCaps(boolean paramBoolean) {
    TransformationMethod transformationMethod;
    TextView textView = this.mView;
    if (paramBoolean) {
      transformationMethod = (TransformationMethod)new AllCapsTransformationMethod(this.mView.getContext());
    } else {
      transformationMethod = null;
    } 
    textView.setTransformationMethod(transformationMethod);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\AppCompatTextHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */