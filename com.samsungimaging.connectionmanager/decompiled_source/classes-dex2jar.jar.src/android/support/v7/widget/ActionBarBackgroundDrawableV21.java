package android.support.v7.widget;

import android.graphics.Outline;
import android.support.annotation.NonNull;

class ActionBarBackgroundDrawableV21 extends ActionBarBackgroundDrawable {
  public ActionBarBackgroundDrawableV21(ActionBarContainer paramActionBarContainer) {
    super(paramActionBarContainer);
  }
  
  public void getOutline(@NonNull Outline paramOutline) {
    if (this.mContainer.mIsSplit) {
      if (this.mContainer.mSplitBackground != null)
        this.mContainer.mSplitBackground.getOutline(paramOutline); 
      return;
    } 
    if (this.mContainer.mBackground != null) {
      this.mContainer.mBackground.getOutline(paramOutline);
      return;
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\ActionBarBackgroundDrawableV21.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */