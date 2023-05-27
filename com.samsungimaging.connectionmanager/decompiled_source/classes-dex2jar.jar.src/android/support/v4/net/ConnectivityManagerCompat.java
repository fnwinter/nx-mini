package android.support.v4.net;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

public class ConnectivityManagerCompat {
  private static final ConnectivityManagerCompatImpl IMPL = new BaseConnectivityManagerCompatImpl();
  
  public static NetworkInfo getNetworkInfoFromBroadcast(ConnectivityManager paramConnectivityManager, Intent paramIntent) {
    NetworkInfo networkInfo = (NetworkInfo)paramIntent.getParcelableExtra("networkInfo");
    return (networkInfo != null) ? paramConnectivityManager.getNetworkInfo(networkInfo.getType()) : null;
  }
  
  public static boolean isActiveNetworkMetered(ConnectivityManager paramConnectivityManager) {
    return IMPL.isActiveNetworkMetered(paramConnectivityManager);
  }
  
  static {
    if (Build.VERSION.SDK_INT >= 16) {
      IMPL = new JellyBeanConnectivityManagerCompatImpl();
      return;
    } 
    if (Build.VERSION.SDK_INT >= 13) {
      IMPL = new HoneycombMR2ConnectivityManagerCompatImpl();
      return;
    } 
    if (Build.VERSION.SDK_INT >= 8) {
      IMPL = new GingerbreadConnectivityManagerCompatImpl();
      return;
    } 
  }
  
  static class BaseConnectivityManagerCompatImpl implements ConnectivityManagerCompatImpl {
    public boolean isActiveNetworkMetered(ConnectivityManager param1ConnectivityManager) {
      NetworkInfo networkInfo = param1ConnectivityManager.getActiveNetworkInfo();
      if (networkInfo == null);
      switch (networkInfo.getType()) {
        case 0:
          return true;
        default:
          return true;
        case 1:
          break;
      } 
      return false;
    }
  }
  
  static interface ConnectivityManagerCompatImpl {
    boolean isActiveNetworkMetered(ConnectivityManager param1ConnectivityManager);
  }
  
  static class GingerbreadConnectivityManagerCompatImpl implements ConnectivityManagerCompatImpl {
    public boolean isActiveNetworkMetered(ConnectivityManager param1ConnectivityManager) {
      return ConnectivityManagerCompatGingerbread.isActiveNetworkMetered(param1ConnectivityManager);
    }
  }
  
  static class HoneycombMR2ConnectivityManagerCompatImpl implements ConnectivityManagerCompatImpl {
    public boolean isActiveNetworkMetered(ConnectivityManager param1ConnectivityManager) {
      return ConnectivityManagerCompatHoneycombMR2.isActiveNetworkMetered(param1ConnectivityManager);
    }
  }
  
  static class JellyBeanConnectivityManagerCompatImpl implements ConnectivityManagerCompatImpl {
    public boolean isActiveNetworkMetered(ConnectivityManager param1ConnectivityManager) {
      return ConnectivityManagerCompatJellyBean.isActiveNetworkMetered(param1ConnectivityManager);
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\net\ConnectivityManagerCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */