package android.support.v4.content.res;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

class ResourcesCompatApi21 {
  public static Drawable getDrawable(Resources paramResources, int paramInt, Resources.Theme paramTheme) throws Resources.NotFoundException {
    return paramResources.getDrawable(paramInt, paramTheme);
  }
  
  public static Drawable getDrawableForDensity(Resources paramResources, int paramInt1, int paramInt2, Resources.Theme paramTheme) throws Resources.NotFoundException {
    return paramResources.getDrawableForDensity(paramInt1, paramInt2, paramTheme);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\content\res\ResourcesCompatApi21.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */