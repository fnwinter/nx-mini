package com.samsungimaging.connectionmanager.util;

import android.util.Log;

public class Trace {
  private static final boolean DEBUG_MODE = true;
  
  private static final String TAG = "SSCA_";
  
  public static void d(Tag paramTag, String paramString) {
    StackTraceElement stackTraceElement = (new Exception()).getStackTrace()[1];
    Log.d("SSCA_" + paramTag, String.format("[%s : %s] %s", new Object[] { stackTraceElement.getFileName(), Integer.valueOf(stackTraceElement.getLineNumber()), paramString }));
  }
  
  public static void e(Tag paramTag, String paramString) {
    StackTraceElement stackTraceElement = (new Exception()).getStackTrace()[1];
    Log.e("SSCA_" + paramTag, String.format("[%s : %s] %s", new Object[] { stackTraceElement.getFileName(), Integer.valueOf(stackTraceElement.getLineNumber()), paramString }));
  }
  
  public static void i(Tag paramTag, String paramString) {
    StackTraceElement stackTraceElement = (new Exception()).getStackTrace()[1];
    Log.i("SSCA_" + paramTag, String.format("[%s : %s] %s", new Object[] { stackTraceElement.getFileName(), Integer.valueOf(stackTraceElement.getLineNumber()), paramString }));
  }
  
  public static void v(Tag paramTag, String paramString) {
    StackTraceElement stackTraceElement = (new Exception()).getStackTrace()[1];
    Log.v("SSCA_" + paramTag, String.format("[%s : %s] %s", new Object[] { stackTraceElement.getFileName(), Integer.valueOf(stackTraceElement.getLineNumber()), paramString }));
  }
  
  public static void w(Tag paramTag, String paramString) {
    StackTraceElement stackTraceElement = (new Exception()).getStackTrace()[1];
    Log.w("SSCA_" + paramTag, String.format("[%s : %s] %s", new Object[] { stackTraceElement.getFileName(), Integer.valueOf(stackTraceElement.getLineNumber()), paramString }));
  }
  
  public enum Tag {
    AS, CM, COMMON, CYBERGARAGE, FFMPEG, ML, RVF, SP;
    
    static {
      AS = new Tag("AS", 2);
      SP = new Tag("SP", 3);
      ML = new Tag("ML", 4);
      RVF = new Tag("RVF", 5);
      FFMPEG = new Tag("FFMPEG", 6);
      CYBERGARAGE = new Tag("CYBERGARAGE", 7);
      ENUM$VALUES = new Tag[] { COMMON, CM, AS, SP, ML, RVF, FFMPEG, CYBERGARAGE };
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanage\\util\Trace.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */