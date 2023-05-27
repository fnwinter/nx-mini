package org.cybergarage.util;

public final class TimerUtil {
  public static final void wait(int paramInt) {
    long l = paramInt;
    try {
      Thread.sleep(l);
      return;
    } catch (Exception exception) {
      return;
    } 
  }
  
  public static final void waitRandom(int paramInt) {
    long l = (int)(Math.random() * paramInt);
    try {
      Thread.sleep(l);
      return;
    } catch (Exception exception) {
      return;
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\util\TimerUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */