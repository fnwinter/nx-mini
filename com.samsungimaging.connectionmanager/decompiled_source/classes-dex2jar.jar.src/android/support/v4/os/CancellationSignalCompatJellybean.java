package android.support.v4.os;

import android.os.CancellationSignal;

class CancellationSignalCompatJellybean {
  public static void cancel(Object paramObject) {
    ((CancellationSignal)paramObject).cancel();
  }
  
  public static Object create() {
    return new CancellationSignal();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\os\CancellationSignalCompatJellybean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */