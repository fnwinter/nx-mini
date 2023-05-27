package com.samsungimaging.connectionmanager.app.pullservice.demo.rvf.common;

import android.os.Handler;
import android.os.Message;

public class Timer extends Thread {
  int delay;
  
  Handler handler;
  
  int messageID;
  
  long startTime = 0L;
  
  public Timer(Handler paramHandler, int paramInt1, int paramInt2) {
    this.handler = paramHandler;
    this.messageID = paramInt1;
    this.delay = paramInt2;
  }
  
  public void run() {
    this.startTime = System.currentTimeMillis();
    try {
      while (true) {
        Thread.sleep(this.delay);
        Message message = new Message();
        message.what = this.messageID;
        message.obj = Long.valueOf(System.currentTimeMillis() - this.startTime);
        this.handler.sendMessage(message);
      } 
    } catch (InterruptedException interruptedException) {
      interruptedException.printStackTrace();
      return;
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\rvf\common\Timer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */