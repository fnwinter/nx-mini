package com.samsungimaging.connectionmanager.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.samsungimaging.connectionmanager.util.RecycleBitmapDrawable;

public class RecycleImageView extends ImageView {
  public RecycleImageView(Context paramContext) {
    super(paramContext);
  }
  
  public RecycleImageView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public RecycleImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private static void notifyDrawable(Drawable paramDrawable, boolean paramBoolean) {
    if (paramDrawable instanceof RecycleBitmapDrawable)
      ((RecycleBitmapDrawable)paramDrawable).setDisplayed(paramBoolean); 
  }
  
  protected void onDetachedFromWindow() {
    setImageDrawable(null);
    super.onDetachedFromWindow();
  }
  
  public void setImageDrawable(Drawable paramDrawable) {
    Drawable drawable = getDrawable();
    super.setImageDrawable(paramDrawable);
    notifyDrawable(paramDrawable, true);
    notifyDrawable(drawable, false);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\view\RecycleImageView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */