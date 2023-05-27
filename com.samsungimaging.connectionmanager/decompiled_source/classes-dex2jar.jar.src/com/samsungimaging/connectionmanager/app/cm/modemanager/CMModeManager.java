package com.samsungimaging.connectionmanager.app.cm.modemanager;

import com.samsungimaging.connectionmanager.app.cm.Interface.IModeClient;
import com.samsungimaging.connectionmanager.app.cm.Interface.IModeServer;

public class CMModeManager {
  public static void removeModeServerListener(IModeServer paramIModeServer) {
    if (paramIModeServer != null)
      ModeServer.getInstance().removeModeServerListener(paramIModeServer); 
  }
  
  public static void runModeClient(IModeClient paramIModeClient, boolean paramBoolean) {
    if (paramIModeClient != null) {
      ModeClient.getInstance().addModeClientListener(paramIModeClient);
      ModeClient.getInstance().runModeClient(paramBoolean);
    } 
  }
  
  public static void runModeServer(IModeServer paramIModeServer) {
    if (paramIModeServer != null) {
      ModeServer.getInstance().addModeServerListener(paramIModeServer);
      ModeServer.getInstance().runModeServer();
    } 
  }
  
  public static void stopModeClient() {
    ModeClient.getInstance().stopModeClient();
  }
  
  public static void stopModeServer() {
    ModeServer.getInstance().stopModeServer();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\modemanager\CMModeManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */