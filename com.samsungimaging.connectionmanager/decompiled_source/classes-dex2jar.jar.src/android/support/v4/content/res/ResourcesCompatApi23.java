package android.support.v4.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;

class ResourcesCompatApi23 {
  public static int getColor(Resources paramResources, int paramInt, Resources.Theme paramTheme) throws Resources.NotFoundException {
    return paramResources.getColor(paramInt, paramTheme);
  }
  
  public static ColorStateList getColorStateList(Resources paramResources, int paramInt, Resources.Theme paramTheme) throws Resources.NotFoundException {
    return paramResources.getColorStateList(paramInt, paramTheme);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\content\res\ResourcesCompatApi23.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */