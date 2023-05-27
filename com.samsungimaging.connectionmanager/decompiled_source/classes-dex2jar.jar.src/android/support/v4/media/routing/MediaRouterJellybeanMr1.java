package android.support.v4.media.routing;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class MediaRouterJellybeanMr1 extends MediaRouterJellybean {
  private static final String TAG = "MediaRouterJellybeanMr1";
  
  public static Object createCallback(Callback paramCallback) {
    return new CallbackProxy<Callback>(paramCallback);
  }
  
  public static final class ActiveScanWorkaround implements Runnable {
    private static final int WIFI_DISPLAY_SCAN_INTERVAL = 15000;
    
    private boolean mActivelyScanningWifiDisplays;
    
    private final DisplayManager mDisplayManager;
    
    private final Handler mHandler;
    
    private Method mScanWifiDisplaysMethod;
    
    public ActiveScanWorkaround(Context param1Context, Handler param1Handler) {
      if (Build.VERSION.SDK_INT != 17)
        throw new UnsupportedOperationException(); 
      this.mDisplayManager = (DisplayManager)param1Context.getSystemService("display");
      this.mHandler = param1Handler;
      try {
        this.mScanWifiDisplaysMethod = DisplayManager.class.getMethod("scanWifiDisplays", new Class[0]);
        return;
      } catch (NoSuchMethodException noSuchMethodException) {
        return;
      } 
    }
    
    public void run() {
      if (this.mActivelyScanningWifiDisplays) {
        try {
          this.mScanWifiDisplaysMethod.invoke(this.mDisplayManager, new Object[0]);
        } catch (IllegalAccessException illegalAccessException) {
          Log.w("MediaRouterJellybeanMr1", "Cannot scan for wifi displays.", illegalAccessException);
        } catch (InvocationTargetException invocationTargetException) {
          Log.w("MediaRouterJellybeanMr1", "Cannot scan for wifi displays.", invocationTargetException);
        } 
        this.mHandler.postDelayed(this, 15000L);
      } 
    }
    
    public void setActiveScanRouteTypes(int param1Int) {
      if ((param1Int & 0x2) != 0) {
        if (!this.mActivelyScanningWifiDisplays) {
          if (this.mScanWifiDisplaysMethod != null) {
            this.mActivelyScanningWifiDisplays = true;
            this.mHandler.post(this);
            return;
          } 
        } else {
          return;
        } 
        Log.w("MediaRouterJellybeanMr1", "Cannot scan for wifi displays because the DisplayManager.scanWifiDisplays() method is not available on this device.");
        return;
      } 
      if (this.mActivelyScanningWifiDisplays) {
        this.mActivelyScanningWifiDisplays = false;
        this.mHandler.removeCallbacks(this);
        return;
      } 
    }
  }
  
  public static interface Callback extends MediaRouterJellybean.Callback {
    void onRoutePresentationDisplayChanged(Object param1Object);
  }
  
  static class CallbackProxy<T extends Callback> extends MediaRouterJellybean.CallbackProxy<T> {
    public CallbackProxy(T param1T) {
      super(param1T);
    }
    
    public void onRoutePresentationDisplayChanged(MediaRouter param1MediaRouter, MediaRouter.RouteInfo param1RouteInfo) {
      ((MediaRouterJellybeanMr1.Callback)this.mCallback).onRoutePresentationDisplayChanged(param1RouteInfo);
    }
  }
  
  public static final class IsConnectingWorkaround {
    private Method mGetStatusCodeMethod;
    
    private int mStatusConnecting;
    
    public IsConnectingWorkaround() {
      if (Build.VERSION.SDK_INT != 17)
        throw new UnsupportedOperationException(); 
      try {
        this.mStatusConnecting = MediaRouter.RouteInfo.class.getField("STATUS_CONNECTING").getInt(null);
        this.mGetStatusCodeMethod = MediaRouter.RouteInfo.class.getMethod("getStatusCode", new Class[0]);
        return;
      } catch (NoSuchFieldException noSuchFieldException) {
        return;
      } catch (NoSuchMethodException noSuchMethodException) {
        return;
      } catch (IllegalAccessException illegalAccessException) {
        return;
      } 
    }
    
    public boolean isConnecting(Object param1Object) {
      param1Object = param1Object;
      if (this.mGetStatusCodeMethod != null)
        try {
          int i = ((Integer)this.mGetStatusCodeMethod.invoke(param1Object, new Object[0])).intValue();
          int j = this.mStatusConnecting;
          return (i == j);
        } catch (IllegalAccessException illegalAccessException) {
        
        } catch (InvocationTargetException invocationTargetException) {} 
      return false;
    }
  }
  
  public static final class RouteInfo {
    public static Display getPresentationDisplay(Object param1Object) {
      return ((MediaRouter.RouteInfo)param1Object).getPresentationDisplay();
    }
    
    public static boolean isEnabled(Object param1Object) {
      return ((MediaRouter.RouteInfo)param1Object).isEnabled();
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\media\routing\MediaRouterJellybeanMr1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */