package android.support.v4.media;

import android.view.KeyEvent;

interface TransportMediatorCallback {
  long getPlaybackPosition();
  
  void handleAudioFocusChange(int paramInt);
  
  void handleKey(KeyEvent paramKeyEvent);
  
  void playbackPositionUpdate(long paramLong);
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\media\TransportMediatorCallback.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */