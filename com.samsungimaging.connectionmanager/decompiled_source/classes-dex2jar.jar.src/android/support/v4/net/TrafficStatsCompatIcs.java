package android.support.v4.net;

import android.net.TrafficStats;
import java.net.Socket;
import java.net.SocketException;

class TrafficStatsCompatIcs {
  public static void clearThreadStatsTag() {
    TrafficStats.clearThreadStatsTag();
  }
  
  public static int getThreadStatsTag() {
    return TrafficStats.getThreadStatsTag();
  }
  
  public static void incrementOperationCount(int paramInt) {
    TrafficStats.incrementOperationCount(paramInt);
  }
  
  public static void incrementOperationCount(int paramInt1, int paramInt2) {
    TrafficStats.incrementOperationCount(paramInt1, paramInt2);
  }
  
  public static void setThreadStatsTag(int paramInt) {
    TrafficStats.setThreadStatsTag(paramInt);
  }
  
  public static void tagSocket(Socket paramSocket) throws SocketException {
    TrafficStats.tagSocket(paramSocket);
  }
  
  public static void untagSocket(Socket paramSocket) throws SocketException {
    TrafficStats.untagSocket(paramSocket);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\net\TrafficStatsCompatIcs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */