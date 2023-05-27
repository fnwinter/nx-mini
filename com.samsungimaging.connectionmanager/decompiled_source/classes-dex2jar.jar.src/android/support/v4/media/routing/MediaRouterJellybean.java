package android.support.v4.media.routing;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaRouter;
import android.media.RemoteControlClient;
import android.os.Build;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class MediaRouterJellybean {
  public static final int ALL_ROUTE_TYPES = 8388611;
  
  public static final int ROUTE_TYPE_LIVE_AUDIO = 1;
  
  public static final int ROUTE_TYPE_LIVE_VIDEO = 2;
  
  public static final int ROUTE_TYPE_USER = 8388608;
  
  private static final String TAG = "MediaRouterJellybean";
  
  public static void addCallback(Object paramObject1, int paramInt, Object paramObject2) {
    ((MediaRouter)paramObject1).addCallback(paramInt, (MediaRouter.Callback)paramObject2);
  }
  
  public static void addUserRoute(Object paramObject1, Object paramObject2) {
    ((MediaRouter)paramObject1).addUserRoute((MediaRouter.UserRouteInfo)paramObject2);
  }
  
  public static Object createCallback(Callback paramCallback) {
    return new CallbackProxy<Callback>(paramCallback);
  }
  
  public static Object createRouteCategory(Object paramObject, String paramString, boolean paramBoolean) {
    return ((MediaRouter)paramObject).createRouteCategory(paramString, paramBoolean);
  }
  
  public static Object createUserRoute(Object paramObject1, Object paramObject2) {
    return ((MediaRouter)paramObject1).createUserRoute((MediaRouter.RouteCategory)paramObject2);
  }
  
  public static Object createVolumeCallback(VolumeCallback paramVolumeCallback) {
    return new VolumeCallbackProxy<VolumeCallback>(paramVolumeCallback);
  }
  
  public static List getCategories(Object paramObject) {
    paramObject = paramObject;
    int j = paramObject.getCategoryCount();
    ArrayList<MediaRouter.RouteCategory> arrayList = new ArrayList(j);
    for (int i = 0; i < j; i++)
      arrayList.add(paramObject.getCategoryAt(i)); 
    return arrayList;
  }
  
  public static Object getMediaRouter(Context paramContext) {
    return paramContext.getSystemService("media_router");
  }
  
  public static List getRoutes(Object paramObject) {
    paramObject = paramObject;
    int j = paramObject.getRouteCount();
    ArrayList<MediaRouter.RouteInfo> arrayList = new ArrayList(j);
    for (int i = 0; i < j; i++)
      arrayList.add(paramObject.getRouteAt(i)); 
    return arrayList;
  }
  
  public static Object getSelectedRoute(Object paramObject, int paramInt) {
    return ((MediaRouter)paramObject).getSelectedRoute(paramInt);
  }
  
  public static void removeCallback(Object paramObject1, Object paramObject2) {
    ((MediaRouter)paramObject1).removeCallback((MediaRouter.Callback)paramObject2);
  }
  
  public static void removeUserRoute(Object paramObject1, Object paramObject2) {
    ((MediaRouter)paramObject1).removeUserRoute((MediaRouter.UserRouteInfo)paramObject2);
  }
  
  public static void selectRoute(Object paramObject1, int paramInt, Object paramObject2) {
    ((MediaRouter)paramObject1).selectRoute(paramInt, (MediaRouter.RouteInfo)paramObject2);
  }
  
  public static interface Callback {
    void onRouteAdded(Object param1Object);
    
    void onRouteChanged(Object param1Object);
    
    void onRouteGrouped(Object param1Object1, Object param1Object2, int param1Int);
    
    void onRouteRemoved(Object param1Object);
    
    void onRouteSelected(int param1Int, Object param1Object);
    
    void onRouteUngrouped(Object param1Object1, Object param1Object2);
    
    void onRouteUnselected(int param1Int, Object param1Object);
    
    void onRouteVolumeChanged(Object param1Object);
  }
  
  static class CallbackProxy<T extends Callback> extends MediaRouter.Callback {
    protected final T mCallback;
    
    public CallbackProxy(T param1T) {
      this.mCallback = param1T;
    }
    
    public void onRouteAdded(MediaRouter param1MediaRouter, MediaRouter.RouteInfo param1RouteInfo) {
      this.mCallback.onRouteAdded(param1RouteInfo);
    }
    
    public void onRouteChanged(MediaRouter param1MediaRouter, MediaRouter.RouteInfo param1RouteInfo) {
      this.mCallback.onRouteChanged(param1RouteInfo);
    }
    
    public void onRouteGrouped(MediaRouter param1MediaRouter, MediaRouter.RouteInfo param1RouteInfo, MediaRouter.RouteGroup param1RouteGroup, int param1Int) {
      this.mCallback.onRouteGrouped(param1RouteInfo, param1RouteGroup, param1Int);
    }
    
    public void onRouteRemoved(MediaRouter param1MediaRouter, MediaRouter.RouteInfo param1RouteInfo) {
      this.mCallback.onRouteRemoved(param1RouteInfo);
    }
    
    public void onRouteSelected(MediaRouter param1MediaRouter, int param1Int, MediaRouter.RouteInfo param1RouteInfo) {
      this.mCallback.onRouteSelected(param1Int, param1RouteInfo);
    }
    
    public void onRouteUngrouped(MediaRouter param1MediaRouter, MediaRouter.RouteInfo param1RouteInfo, MediaRouter.RouteGroup param1RouteGroup) {
      this.mCallback.onRouteUngrouped(param1RouteInfo, param1RouteGroup);
    }
    
    public void onRouteUnselected(MediaRouter param1MediaRouter, int param1Int, MediaRouter.RouteInfo param1RouteInfo) {
      this.mCallback.onRouteUnselected(param1Int, param1RouteInfo);
    }
    
    public void onRouteVolumeChanged(MediaRouter param1MediaRouter, MediaRouter.RouteInfo param1RouteInfo) {
      this.mCallback.onRouteVolumeChanged(param1RouteInfo);
    }
  }
  
  public static final class GetDefaultRouteWorkaround {
    private Method mGetSystemAudioRouteMethod;
    
    public GetDefaultRouteWorkaround() {
      if (Build.VERSION.SDK_INT < 16 || Build.VERSION.SDK_INT > 17)
        throw new UnsupportedOperationException(); 
      try {
        this.mGetSystemAudioRouteMethod = MediaRouter.class.getMethod("getSystemAudioRoute", new Class[0]);
        return;
      } catch (NoSuchMethodException noSuchMethodException) {
        return;
      } 
    }
    
    public Object getDefaultRoute(Object param1Object) {
      param1Object = param1Object;
      if (this.mGetSystemAudioRouteMethod != null)
        try {
          return this.mGetSystemAudioRouteMethod.invoke(param1Object, new Object[0]);
        } catch (IllegalAccessException illegalAccessException) {
        
        } catch (InvocationTargetException invocationTargetException) {} 
      return param1Object.getRouteAt(0);
    }
  }
  
  public static final class RouteCategory {
    public static CharSequence getName(Object param1Object, Context param1Context) {
      return ((MediaRouter.RouteCategory)param1Object).getName(param1Context);
    }
    
    public static List getRoutes(Object param1Object) {
      ArrayList arrayList = new ArrayList();
      ((MediaRouter.RouteCategory)param1Object).getRoutes(arrayList);
      return arrayList;
    }
    
    public static int getSupportedTypes(Object param1Object) {
      return ((MediaRouter.RouteCategory)param1Object).getSupportedTypes();
    }
    
    public static boolean isGroupable(Object param1Object) {
      return ((MediaRouter.RouteCategory)param1Object).isGroupable();
    }
  }
  
  public static final class RouteGroup {
    public static List getGroupedRoutes(Object param1Object) {
      param1Object = param1Object;
      int j = param1Object.getRouteCount();
      ArrayList<MediaRouter.RouteInfo> arrayList = new ArrayList(j);
      for (int i = 0; i < j; i++)
        arrayList.add(param1Object.getRouteAt(i)); 
      return arrayList;
    }
  }
  
  public static final class RouteInfo {
    public static Object getCategory(Object param1Object) {
      return ((MediaRouter.RouteInfo)param1Object).getCategory();
    }
    
    public static Object getGroup(Object param1Object) {
      return ((MediaRouter.RouteInfo)param1Object).getGroup();
    }
    
    public static Drawable getIconDrawable(Object param1Object) {
      return ((MediaRouter.RouteInfo)param1Object).getIconDrawable();
    }
    
    public static CharSequence getName(Object param1Object, Context param1Context) {
      return ((MediaRouter.RouteInfo)param1Object).getName(param1Context);
    }
    
    public static int getPlaybackStream(Object param1Object) {
      return ((MediaRouter.RouteInfo)param1Object).getPlaybackStream();
    }
    
    public static int getPlaybackType(Object param1Object) {
      return ((MediaRouter.RouteInfo)param1Object).getPlaybackType();
    }
    
    public static CharSequence getStatus(Object param1Object) {
      return ((MediaRouter.RouteInfo)param1Object).getStatus();
    }
    
    public static int getSupportedTypes(Object param1Object) {
      return ((MediaRouter.RouteInfo)param1Object).getSupportedTypes();
    }
    
    public static Object getTag(Object param1Object) {
      return ((MediaRouter.RouteInfo)param1Object).getTag();
    }
    
    public static int getVolume(Object param1Object) {
      return ((MediaRouter.RouteInfo)param1Object).getVolume();
    }
    
    public static int getVolumeHandling(Object param1Object) {
      return ((MediaRouter.RouteInfo)param1Object).getVolumeHandling();
    }
    
    public static int getVolumeMax(Object param1Object) {
      return ((MediaRouter.RouteInfo)param1Object).getVolumeMax();
    }
    
    public static boolean isGroup(Object param1Object) {
      return param1Object instanceof MediaRouter.RouteGroup;
    }
    
    public static void requestSetVolume(Object param1Object, int param1Int) {
      ((MediaRouter.RouteInfo)param1Object).requestSetVolume(param1Int);
    }
    
    public static void requestUpdateVolume(Object param1Object, int param1Int) {
      ((MediaRouter.RouteInfo)param1Object).requestUpdateVolume(param1Int);
    }
    
    public static void setTag(Object param1Object1, Object param1Object2) {
      ((MediaRouter.RouteInfo)param1Object1).setTag(param1Object2);
    }
  }
  
  public static final class SelectRouteWorkaround {
    private Method mSelectRouteIntMethod;
    
    public SelectRouteWorkaround() {
      if (Build.VERSION.SDK_INT < 16 || Build.VERSION.SDK_INT > 17)
        throw new UnsupportedOperationException(); 
      try {
        this.mSelectRouteIntMethod = MediaRouter.class.getMethod("selectRouteInt", new Class[] { int.class, MediaRouter.RouteInfo.class });
        return;
      } catch (NoSuchMethodException noSuchMethodException) {
        return;
      } 
    }
    
    public void selectRoute(Object param1Object1, int param1Int, Object param1Object2) {
      param1Object1 = param1Object1;
      param1Object2 = param1Object2;
      if ((0x800000 & param1Object2.getSupportedTypes()) == 0)
        if (this.mSelectRouteIntMethod != null) {
          try {
            this.mSelectRouteIntMethod.invoke(param1Object1, new Object[] { Integer.valueOf(param1Int), param1Object2 });
            return;
          } catch (IllegalAccessException illegalAccessException) {
            Log.w("MediaRouterJellybean", "Cannot programmatically select non-user route.  Media routing may not work.", illegalAccessException);
          } catch (InvocationTargetException invocationTargetException) {
            Log.w("MediaRouterJellybean", "Cannot programmatically select non-user route.  Media routing may not work.", invocationTargetException);
          } 
        } else {
          Log.w("MediaRouterJellybean", "Cannot programmatically select non-user route because the platform is missing the selectRouteInt() method.  Media routing may not work.");
        }  
      param1Object1.selectRoute(param1Int, (MediaRouter.RouteInfo)param1Object2);
    }
  }
  
  public static final class UserRouteInfo {
    public static void setIconDrawable(Object param1Object, Drawable param1Drawable) {
      ((MediaRouter.UserRouteInfo)param1Object).setIconDrawable(param1Drawable);
    }
    
    public static void setName(Object param1Object, CharSequence param1CharSequence) {
      ((MediaRouter.UserRouteInfo)param1Object).setName(param1CharSequence);
    }
    
    public static void setPlaybackStream(Object param1Object, int param1Int) {
      ((MediaRouter.UserRouteInfo)param1Object).setPlaybackStream(param1Int);
    }
    
    public static void setPlaybackType(Object param1Object, int param1Int) {
      ((MediaRouter.UserRouteInfo)param1Object).setPlaybackType(param1Int);
    }
    
    public static void setRemoteControlClient(Object param1Object1, Object param1Object2) {
      ((MediaRouter.UserRouteInfo)param1Object1).setRemoteControlClient((RemoteControlClient)param1Object2);
    }
    
    public static void setStatus(Object param1Object, CharSequence param1CharSequence) {
      ((MediaRouter.UserRouteInfo)param1Object).setStatus(param1CharSequence);
    }
    
    public static void setVolume(Object param1Object, int param1Int) {
      ((MediaRouter.UserRouteInfo)param1Object).setVolume(param1Int);
    }
    
    public static void setVolumeCallback(Object param1Object1, Object param1Object2) {
      ((MediaRouter.UserRouteInfo)param1Object1).setVolumeCallback((MediaRouter.VolumeCallback)param1Object2);
    }
    
    public static void setVolumeHandling(Object param1Object, int param1Int) {
      ((MediaRouter.UserRouteInfo)param1Object).setVolumeHandling(param1Int);
    }
    
    public static void setVolumeMax(Object param1Object, int param1Int) {
      ((MediaRouter.UserRouteInfo)param1Object).setVolumeMax(param1Int);
    }
  }
  
  public static interface VolumeCallback {
    void onVolumeSetRequest(Object param1Object, int param1Int);
    
    void onVolumeUpdateRequest(Object param1Object, int param1Int);
  }
  
  static class VolumeCallbackProxy<T extends VolumeCallback> extends MediaRouter.VolumeCallback {
    protected final T mCallback;
    
    public VolumeCallbackProxy(T param1T) {
      this.mCallback = param1T;
    }
    
    public void onVolumeSetRequest(MediaRouter.RouteInfo param1RouteInfo, int param1Int) {
      this.mCallback.onVolumeSetRequest(param1RouteInfo, param1Int);
    }
    
    public void onVolumeUpdateRequest(MediaRouter.RouteInfo param1RouteInfo, int param1Int) {
      this.mCallback.onVolumeUpdateRequest(param1RouteInfo, param1Int);
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\media\routing\MediaRouterJellybean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */