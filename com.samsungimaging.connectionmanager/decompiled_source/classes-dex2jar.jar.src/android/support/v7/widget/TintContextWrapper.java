package android.support.v7.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

class TintContextWrapper extends ContextWrapper {
  private Resources mResources;
  
  private TintContextWrapper(Context paramContext) {
    super(paramContext);
  }
  
  public static Context wrap(Context paramContext) {
    TintContextWrapper tintContextWrapper;
    Context context = paramContext;
    if (!(paramContext instanceof TintContextWrapper))
      tintContextWrapper = new TintContextWrapper(paramContext); 
    return (Context)tintContextWrapper;
  }
  
  public Resources getResources() {
    if (this.mResources == null)
      this.mResources = new TintResources(super.getResources(), TintManager.get((Context)this)); 
    return this.mResources;
  }
  
  static class TintResources extends ResourcesWrapper {
    private final TintManager mTintManager;
    
    public TintResources(Resources param1Resources, TintManager param1TintManager) {
      super(param1Resources);
      this.mTintManager = param1TintManager;
    }
    
    public Drawable getDrawable(int param1Int) throws Resources.NotFoundException {
      Drawable drawable = super.getDrawable(param1Int);
      if (drawable != null)
        this.mTintManager.tintDrawableUsingColorFilter(param1Int, drawable); 
      return drawable;
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\TintContextWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */