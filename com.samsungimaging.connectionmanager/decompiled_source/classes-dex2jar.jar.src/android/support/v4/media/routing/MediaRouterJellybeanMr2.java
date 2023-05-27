package android.support.v4.media.routing;

import android.media.MediaRouter;

class MediaRouterJellybeanMr2 extends MediaRouterJellybeanMr1 {
  public static void addCallback(Object paramObject1, int paramInt1, Object paramObject2, int paramInt2) {
    ((MediaRouter)paramObject1).addCallback(paramInt1, (MediaRouter.Callback)paramObject2, paramInt2);
  }
  
  public static Object getDefaultRoute(Object paramObject) {
    return ((MediaRouter)paramObject).getDefaultRoute();
  }
  
  public static final class RouteInfo {
    public static CharSequence getDescription(Object param1Object) {
      return ((MediaRouter.RouteInfo)param1Object).getDescription();
    }
    
    public static boolean isConnecting(Object param1Object) {
      return ((MediaRouter.RouteInfo)param1Object).isConnecting();
    }
  }
  
  public static final class UserRouteInfo {
    public static void setDescription(Object param1Object, CharSequence param1CharSequence) {
      ((MediaRouter.UserRouteInfo)param1Object).setDescription(param1CharSequence);
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\media\routing\MediaRouterJellybeanMr2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */