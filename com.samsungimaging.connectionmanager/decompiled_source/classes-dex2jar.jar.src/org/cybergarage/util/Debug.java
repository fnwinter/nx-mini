package org.cybergarage.util;

import java.io.PrintStream;

public final class Debug {
  public static Debug debug = new Debug();
  
  public static boolean enabled = true;
  
  private PrintStream out = System.out;
  
  public static Debug getDebug() {
    return debug;
  }
  
  public static boolean isOn() {
    return enabled;
  }
  
  public static final void message(String paramString) {
    if (enabled)
      debug.getOut().println("CyberGarage message : " + paramString); 
  }
  
  public static final void message(String paramString1, String paramString2) {
    if (enabled)
      debug.getOut().println("CyberGarage message : "); 
    debug.getOut().println(paramString1);
    debug.getOut().println(paramString2);
  }
  
  public static final void off() {
    enabled = false;
  }
  
  public static final void on() {
    enabled = true;
  }
  
  public static final void warning(Exception paramException) {
    warning(paramException.getMessage());
    paramException.printStackTrace(debug.getOut());
  }
  
  public static final void warning(String paramString) {
    debug.getOut().println("CyberGarage warning : " + paramString);
  }
  
  public static final void warning(String paramString, Exception paramException) {
    if (paramException.getMessage() == null) {
      debug.getOut().println("CyberGarage warning : " + paramString + " START");
      paramException.printStackTrace(debug.getOut());
      debug.getOut().println("CyberGarage warning : " + paramString + " END");
      return;
    } 
    debug.getOut().println("CyberGarage warning : " + paramString + " (" + paramException.getMessage() + ")");
    paramException.printStackTrace(debug.getOut());
  }
  
  public PrintStream getOut() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield out : Ljava/io/PrintStream;
    //   6: astore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: aload_1
    //   10: areturn
    //   11: astore_1
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_1
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public void setOut(PrintStream paramPrintStream) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_1
    //   4: putfield out : Ljava/io/PrintStream;
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_1
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_1
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\util\Debug.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */