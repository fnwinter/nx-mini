package org.cybergarage.util;

public class ThreadCore implements Runnable {
  private Thread mThreadObject = null;
  
  public Thread getThreadObject() {
    return this.mThreadObject;
  }
  
  public boolean isRunnable() {
    return (Thread.currentThread() == getThreadObject());
  }
  
  public void restart() {
    stop();
    start();
  }
  
  public void run() {}
  
  public void setThreadObject(Thread paramThread) {
    this.mThreadObject = paramThread;
  }
  
  public void start() {
    if (getThreadObject() == null) {
      Thread thread = new Thread(this, "Cyber.ThreadCore");
      setThreadObject(thread);
      thread.start();
    } 
  }
  
  public void stop() {
    Thread thread = getThreadObject();
    if (thread != null) {
      thread.interrupt();
      setThreadObject(null);
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\util\ThreadCore.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */