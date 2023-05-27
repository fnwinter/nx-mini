package android.support.v4.net;

import android.os.Build;
import java.net.Socket;
import java.net.SocketException;

public class TrafficStatsCompat {
  private static final TrafficStatsCompatImpl IMPL = new BaseTrafficStatsCompatImpl();
  
  public static void clearThreadStatsTag() {
    IMPL.clearThreadStatsTag();
  }
  
  public static int getThreadStatsTag() {
    return IMPL.getThreadStatsTag();
  }
  
  public static void incrementOperationCount(int paramInt) {
    IMPL.incrementOperationCount(paramInt);
  }
  
  public static void incrementOperationCount(int paramInt1, int paramInt2) {
    IMPL.incrementOperationCount(paramInt1, paramInt2);
  }
  
  public static void setThreadStatsTag(int paramInt) {
    IMPL.setThreadStatsTag(paramInt);
  }
  
  public static void tagSocket(Socket paramSocket) throws SocketException {
    IMPL.tagSocket(paramSocket);
  }
  
  public static void untagSocket(Socket paramSocket) throws SocketException {
    IMPL.untagSocket(paramSocket);
  }
  
  static {
    if (Build.VERSION.SDK_INT >= 14) {
      IMPL = new IcsTrafficStatsCompatImpl();
      return;
    } 
  }
  
  static class BaseTrafficStatsCompatImpl implements TrafficStatsCompatImpl {
    private ThreadLocal<SocketTags> mThreadSocketTags = new ThreadLocal<SocketTags>() {
        protected TrafficStatsCompat.BaseTrafficStatsCompatImpl.SocketTags initialValue() {
          return new TrafficStatsCompat.BaseTrafficStatsCompatImpl.SocketTags();
        }
      };
    
    public void clearThreadStatsTag() {
      ((SocketTags)this.mThreadSocketTags.get()).statsTag = -1;
    }
    
    public int getThreadStatsTag() {
      return ((SocketTags)this.mThreadSocketTags.get()).statsTag;
    }
    
    public void incrementOperationCount(int param1Int) {}
    
    public void incrementOperationCount(int param1Int1, int param1Int2) {}
    
    public void setThreadStatsTag(int param1Int) {
      ((SocketTags)this.mThreadSocketTags.get()).statsTag = param1Int;
    }
    
    public void tagSocket(Socket param1Socket) {}
    
    public void untagSocket(Socket param1Socket) {}
    
    private static class SocketTags {
      public int statsTag = -1;
      
      private SocketTags() {}
    }
  }
  
  class null extends ThreadLocal<BaseTrafficStatsCompatImpl.SocketTags> {
    protected TrafficStatsCompat.BaseTrafficStatsCompatImpl.SocketTags initialValue() {
      return new TrafficStatsCompat.BaseTrafficStatsCompatImpl.SocketTags();
    }
  }
  
  private static class SocketTags {
    public int statsTag = -1;
    
    private SocketTags() {}
  }
  
  static class IcsTrafficStatsCompatImpl implements TrafficStatsCompatImpl {
    public void clearThreadStatsTag() {
      TrafficStatsCompatIcs.clearThreadStatsTag();
    }
    
    public int getThreadStatsTag() {
      return TrafficStatsCompatIcs.getThreadStatsTag();
    }
    
    public void incrementOperationCount(int param1Int) {
      TrafficStatsCompatIcs.incrementOperationCount(param1Int);
    }
    
    public void incrementOperationCount(int param1Int1, int param1Int2) {
      TrafficStatsCompatIcs.incrementOperationCount(param1Int1, param1Int2);
    }
    
    public void setThreadStatsTag(int param1Int) {
      TrafficStatsCompatIcs.setThreadStatsTag(param1Int);
    }
    
    public void tagSocket(Socket param1Socket) throws SocketException {
      TrafficStatsCompatIcs.tagSocket(param1Socket);
    }
    
    public void untagSocket(Socket param1Socket) throws SocketException {
      TrafficStatsCompatIcs.untagSocket(param1Socket);
    }
  }
  
  static interface TrafficStatsCompatImpl {
    void clearThreadStatsTag();
    
    int getThreadStatsTag();
    
    void incrementOperationCount(int param1Int);
    
    void incrementOperationCount(int param1Int1, int param1Int2);
    
    void setThreadStatsTag(int param1Int);
    
    void tagSocket(Socket param1Socket) throws SocketException;
    
    void untagSocket(Socket param1Socket) throws SocketException;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\net\TrafficStatsCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */