package android.support.v4.media;

public abstract class TransportController {
  public abstract int getBufferPercentage();
  
  public abstract long getCurrentPosition();
  
  public abstract long getDuration();
  
  public abstract int getTransportControlFlags();
  
  public abstract boolean isPlaying();
  
  public abstract void pausePlaying();
  
  public abstract void registerStateListener(TransportStateListener paramTransportStateListener);
  
  public abstract void seekTo(long paramLong);
  
  public abstract void startPlaying();
  
  public abstract void stopPlaying();
  
  public abstract void unregisterStateListener(TransportStateListener paramTransportStateListener);
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\media\TransportController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */