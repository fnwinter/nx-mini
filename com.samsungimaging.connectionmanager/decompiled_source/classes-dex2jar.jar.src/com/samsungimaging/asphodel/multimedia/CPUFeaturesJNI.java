package com.samsungimaging.asphodel.multimedia;

import android.util.Log;

public class CPUFeaturesJNI {
  private static final String LOG_TAG = "[CPUFeaturesJNI]";
  
  private static int m_IsSupportNEON;
  
  private static int m_argc = 0;
  
  private static String[] m_argv = null;
  
  static {
    m_IsSupportNEON = 0;
  }
  
  public static int construct() {
    System.loadLibrary("cpufeatures-jni");
    m_IsSupportNEON = cpufeaturesMain(m_argc, m_argv);
    Log.i("[CPUFeaturesJNI]", "Is Support NEON? : " + String.valueOf(m_IsSupportNEON));
    return m_IsSupportNEON;
  }
  
  private static native int cpufeaturesMain(int paramInt, String[] paramArrayOfString);
  
  public static int destruct() {
    return 0;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\asphodel\multimedia\CPUFeaturesJNI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */