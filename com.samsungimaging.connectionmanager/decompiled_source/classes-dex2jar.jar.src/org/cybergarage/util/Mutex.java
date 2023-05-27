package org.cybergarage.util;

public class Mutex {
  private boolean syncLock = false;
  
  public void lock() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield syncLock : Z
    //   6: ifne -> 17
    //   9: aload_0
    //   10: iconst_1
    //   11: putfield syncLock : Z
    //   14: aload_0
    //   15: monitorexit
    //   16: return
    //   17: aload_0
    //   18: invokevirtual wait : ()V
    //   21: goto -> 2
    //   24: astore_1
    //   25: aload_1
    //   26: invokestatic warning : (Ljava/lang/Exception;)V
    //   29: goto -> 2
    //   32: astore_1
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_1
    //   36: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	32	finally
    //   17	21	24	java/lang/Exception
    //   17	21	32	finally
    //   25	29	32	finally
  }
  
  public void unlock() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iconst_0
    //   4: putfield syncLock : Z
    //   7: aload_0
    //   8: invokevirtual notifyAll : ()V
    //   11: aload_0
    //   12: monitorexit
    //   13: return
    //   14: astore_1
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_1
    //   18: athrow
    // Exception table:
    //   from	to	target	type
    //   2	11	14	finally
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\util\Mutex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */