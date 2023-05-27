package com.samsungimaging.connectionmanager.util;

import android.app.Application;

public class MyLocalGallery extends Application {
  private static boolean activityCreated;
  
  public static void activityCreated() {
    activityCreated = true;
  }
  
  public static void activityDestroyed() {
    activityCreated = false;
  }
  
  public static boolean isActivityCreated() {
    return activityCreated;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanage\\util\MyLocalGallery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */