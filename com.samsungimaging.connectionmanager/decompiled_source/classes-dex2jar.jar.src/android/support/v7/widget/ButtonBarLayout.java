package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ButtonBarLayout extends LinearLayout {
  private boolean mAllowStacking;
  
  private int mLastWidthSize = -1;
  
  public ButtonBarLayout(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ButtonBarLayout);
    this.mAllowStacking = typedArray.getBoolean(R.styleable.ButtonBarLayout_allowStacking, false);
    typedArray.recycle();
  }
  
  private boolean isStacked() {
    return (getOrientation() == 1);
  }
  
  private void setStacked(boolean paramBoolean) {
    if (paramBoolean) {
      i = 1;
    } else {
      i = 0;
    } 
    setOrientation(i);
    if (paramBoolean) {
      i = 5;
    } else {
      i = 80;
    } 
    setGravity(i);
    View view = findViewById(R.id.spacer);
    if (view != null) {
      if (paramBoolean) {
        i = 8;
      } else {
        i = 4;
      } 
      view.setVisibility(i);
    } 
    int i;
    for (i = getChildCount() - 2; i >= 0; i--)
      bringChildToFront(getChildAt(i)); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int i = View.MeasureSpec.getSize(paramInt1);
    if (this.mAllowStacking) {
      if (i > this.mLastWidthSize && isStacked())
        setStacked(false); 
      this.mLastWidthSize = i;
    } 
    boolean bool = false;
    if (!isStacked() && View.MeasureSpec.getMode(paramInt1) == 1073741824) {
      i = View.MeasureSpec.makeMeasureSpec(i, -2147483648);
      bool = true;
    } else {
      i = paramInt1;
    } 
    super.onMeasure(i, paramInt2);
    i = bool;
    if (this.mAllowStacking) {
      i = bool;
      if (!isStacked()) {
        i = bool;
        if ((getMeasuredWidthAndState() & 0xFF000000) == 16777216) {
          setStacked(true);
          i = 1;
        } 
      } 
    } 
    if (i != 0)
      super.onMeasure(paramInt1, paramInt2); 
  }
  
  public void setAllowStacking(boolean paramBoolean) {
    if (this.mAllowStacking != paramBoolean) {
      this.mAllowStacking = paramBoolean;
      if (!this.mAllowStacking && getOrientation() == 1)
        setStacked(false); 
      requestLayout();
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\ButtonBarLayout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */