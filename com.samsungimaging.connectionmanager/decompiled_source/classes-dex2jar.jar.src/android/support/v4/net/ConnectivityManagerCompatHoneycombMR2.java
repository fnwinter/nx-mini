package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class ConnectivityManagerCompatHoneycombMR2 {
  public static boolean isActiveNetworkMetered(ConnectivityManager paramConnectivityManager) {
    NetworkInfo networkInfo = paramConnectivityManager.getActiveNetworkInfo();
    if (networkInfo == null);
    switch (networkInfo.getType()) {
      case 0:
      case 2:
      case 3:
      case 4:
      case 5:
      case 6:
        return true;
      default:
        return true;
      case 1:
      case 7:
      case 9:
        break;
    } 
    return false;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\net\ConnectivityManagerCompatHoneycombMR2.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */