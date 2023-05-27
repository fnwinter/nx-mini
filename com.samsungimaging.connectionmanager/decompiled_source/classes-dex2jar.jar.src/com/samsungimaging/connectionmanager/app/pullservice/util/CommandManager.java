package com.samsungimaging.connectionmanager.app.pullservice.util;

import java.util.Stack;

public class CommandManager extends Thread {
  private ListenerList commandRequestListenerList = new ListenerList();
  
  private Stack commands = new Stack();
  
  public void addCommandRequestListener(CommandRequestListener paramCommandRequestListener) {
    this.commandRequestListenerList.add(paramCommandRequestListener);
  }
  
  public void performOnCommandListener(Object paramObject) {
    int j = this.commandRequestListenerList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      ((CommandRequestListener)this.commandRequestListenerList.get(i)).onCommand(paramObject);
    } 
  }
  
  public void queueCommand(Object paramObject) {
    synchronized (this.commands) {
      this.commands.push(paramObject);
      this.commands.notifyAll();
      if (getState() == Thread.State.NEW)
        start(); 
      return;
    } 
  }
  
  public void removeCommandRequestListener(CommandRequestListener paramCommandRequestListener) {
    this.commandRequestListenerList.remove(paramCommandRequestListener);
  }
  
  public void run() {
    // Byte code:
    //   0: aload_0
    //   1: getfield commands : Ljava/util/Stack;
    //   4: invokevirtual size : ()I
    //   7: ifne -> 26
    //   10: aload_0
    //   11: getfield commands : Ljava/util/Stack;
    //   14: astore_1
    //   15: aload_1
    //   16: monitorenter
    //   17: aload_0
    //   18: getfield commands : Ljava/util/Stack;
    //   21: invokevirtual wait : ()V
    //   24: aload_1
    //   25: monitorexit
    //   26: aload_0
    //   27: getfield commands : Ljava/util/Stack;
    //   30: invokevirtual size : ()I
    //   33: ifeq -> 0
    //   36: aload_0
    //   37: getfield commands : Ljava/util/Stack;
    //   40: astore_1
    //   41: aload_1
    //   42: monitorenter
    //   43: aload_0
    //   44: getfield commands : Ljava/util/Stack;
    //   47: iconst_0
    //   48: invokevirtual remove : (I)Ljava/lang/Object;
    //   51: astore_2
    //   52: aload_1
    //   53: monitorexit
    //   54: aload_0
    //   55: aload_2
    //   56: invokevirtual performOnCommandListener : (Ljava/lang/Object;)V
    //   59: goto -> 0
    //   62: astore_1
    //   63: aload_1
    //   64: invokevirtual printStackTrace : ()V
    //   67: return
    //   68: astore_2
    //   69: aload_1
    //   70: monitorexit
    //   71: aload_2
    //   72: athrow
    //   73: astore_2
    //   74: aload_1
    //   75: monitorexit
    //   76: aload_2
    //   77: athrow
    // Exception table:
    //   from	to	target	type
    //   0	17	62	java/lang/InterruptedException
    //   17	26	68	finally
    //   26	43	62	java/lang/InterruptedException
    //   43	54	73	finally
    //   54	59	62	java/lang/InterruptedException
    //   69	71	68	finally
    //   71	73	62	java/lang/InterruptedException
    //   74	76	73	finally
    //   76	78	62	java/lang/InterruptedException
  }
  
  public void stopThread() {
    interrupt();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservic\\util\CommandManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */