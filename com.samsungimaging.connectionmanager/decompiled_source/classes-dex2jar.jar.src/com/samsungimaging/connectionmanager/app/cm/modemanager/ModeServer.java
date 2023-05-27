package com.samsungimaging.connectionmanager.app.cm.modemanager;

import android.text.format.Formatter;
import com.samsungimaging.connectionmanager.app.cm.Interface.IModeServer;
import com.samsungimaging.connectionmanager.app.cm.common.CMConstants;
import com.samsungimaging.connectionmanager.app.cm.common.CMSharedPreferenceUtil;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;
import com.samsungimaging.connectionmanager.util.Trace;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ModeServer implements Runnable {
  private static ModeServer instance_ModeServer;
  
  private static ArrayList<IModeServer> mIModeServerList;
  
  public static int mServerPort = 7789;
  
  private InetAddress callBackServerIP = null;
  
  private Thread mRunModeServer = null;
  
  private ServerSocket mServerSocket = null;
  
  static {
    instance_ModeServer = null;
    mIModeServerList = new ArrayList<IModeServer>();
  }
  
  private void clearModeServerListner() {
    mIModeServerList.clear();
  }
  
  public static ModeServer getInstance() {
    // Byte code:
    //   0: ldc com/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer
    //   2: monitorenter
    //   3: getstatic com/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer.instance_ModeServer : Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer;
    //   6: ifnonnull -> 19
    //   9: new com/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer
    //   12: dup
    //   13: invokespecial <init> : ()V
    //   16: putstatic com/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer.instance_ModeServer : Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer;
    //   19: getstatic com/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer.instance_ModeServer : Lcom/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer;
    //   22: astore_0
    //   23: ldc com/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer
    //   25: monitorexit
    //   26: aload_0
    //   27: areturn
    //   28: astore_0
    //   29: ldc com/samsungimaging/connectionmanager/app/cm/modemanager/ModeServer
    //   31: monitorexit
    //   32: aload_0
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   3	19	28	finally
    //   19	23	28	finally
  }
  
  public void addModeServerListener(IModeServer paramIModeServer) {
    Trace.d(CMConstants.TAG_NAME, "ModeServer, addModeServerListener -> " + paramIModeServer);
    int j = mIModeServerList.size();
    for (int i = 0;; i++) {
      if (i >= j) {
        Trace.d(CMConstants.TAG_NAME, "ModeServer, addModeServerListener, add done.");
        mIModeServerList.add(paramIModeServer);
        return;
      } 
      Trace.d(CMConstants.TAG_NAME, "ModeServer, addModeServerListener -> mIModeServerList.get(" + i + ") = " + mIModeServerList.get(i));
      if (paramIModeServer.equals(mIModeServerList.get(i))) {
        Trace.d(CMConstants.TAG_NAME, "ModeServer, addModeServerListener, already added.");
        return;
      } 
    } 
  }
  
  public void performRunByebye() {
    int j = mIModeServerList.size();
    Trace.d(CMConstants.TAG_NAME, "ModeServer, performRunByebye, listenerSize = " + j);
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      ((IModeServer)mIModeServerList.get(i)).runByebye();
    } 
  }
  
  public void performRunSubApplication(int paramInt) {
    int j = mIModeServerList.size();
    Trace.d(CMConstants.TAG_NAME, "ModeServer, performRunSubApplication, listenerSize = " + j);
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      ((IModeServer)mIModeServerList.get(i)).runSubApplication(paramInt);
    } 
  }
  
  public void removeModeServerListener(IModeServer paramIModeServer) {
    Trace.d(CMConstants.TAG_NAME, "ModeServer, removeModeServerListener -> " + paramIModeServer);
    int j = mIModeServerList.size();
    for (int i = 0;; i++) {
      if (i >= j) {
        Trace.d(CMConstants.TAG_NAME, "ModeServer, removeModeServerListener, There is no item to be removed");
        return;
      } 
      Trace.d(CMConstants.TAG_NAME, "ModeServer, removeModeServerListener -> mIModeServerList.get(" + i + ") = " + mIModeServerList.get(i));
      if (paramIModeServer.equals(mIModeServerList.get(i))) {
        mIModeServerList.remove(i);
        Trace.d(CMConstants.TAG_NAME, "ModeServer, removeModeServerListener, remove done.");
        return;
      } 
    } 
  }
  
  public void run() {
    try {
      if (this.mServerSocket == null) {
        Trace.d(CMConstants.TAG_NAME, "ModeServer, ServerSocket created. mServerPort = " + mServerPort);
        this.callBackServerIP = InetAddress.getByName(Formatter.formatIpAddress((CMService.mWifiManager.getDhcpInfo()).ipAddress));
        this.mServerSocket = new ServerSocket(mServerPort, 0, this.callBackServerIP);
        Trace.d(CMConstants.TAG_NAME, "ModeServer, ServerSocket created.");
      } 
      Trace.d(CMConstants.TAG_NAME, "ModeServer, Connecting...");
      while (true) {
        Trace.d(CMConstants.TAG_NAME, "ModeServer, Request Waiting...");
        Socket socket = this.mServerSocket.accept();
        Trace.d(CMConstants.TAG_NAME, "ModeServer, Request Receiving.");
        if (Thread.currentThread().isInterrupted()) {
          Trace.d(CMConstants.TAG_NAME, "ModeServer is inTerrupted.");
          try {
            if (this.mServerSocket != null) {
              this.mServerSocket.close();
              this.mServerSocket = null;
            } 
            mServerPort++;
            if (mServerPort == 7794)
              mServerPort = 7789; 
            return;
          } catch (IOException iOException) {
            return;
          } 
        } 
        (new ModeServerResponse((Socket)iOException)).start();
      } 
    } catch (Exception exception) {
      Trace.d(CMConstants.TAG_NAME, "ModeServer, Exception02");
      exception.printStackTrace();
    } finally {
      try {
        if (this.mServerSocket != null) {
          this.mServerSocket.close();
          this.mServerSocket = null;
        } 
        mServerPort++;
        if (mServerPort == 7794)
          mServerPort = 7789; 
        CMSharedPreferenceUtil.put(CMService.mContext, "com.samsungimaging.connectionmanager.MODESERVER_PORT", mServerPort);
      } catch (IOException iOException) {
        iOException.printStackTrace();
      } 
    } 
  }
  
  public void runModeServer() {
    if (this.mRunModeServer == null) {
      Trace.d(CMConstants.TAG_NAME, "ModeServer, runModeServer(), start!!!");
      if (instance_ModeServer == null)
        instance_ModeServer = new ModeServer(); 
      this.mRunModeServer = new Thread(instance_ModeServer);
      this.mRunModeServer.start();
      return;
    } 
    Trace.d(CMConstants.TAG_NAME, "ModeServer, runModeServer(), already started!!!");
  }
  
  public void stopModeServer() {
    Trace.d(CMConstants.TAG_NAME, "ModeServer, stopModeServer()");
    clearModeServerListner();
    if (this.mServerSocket != null)
      try {
        this.mServerSocket.close();
        this.mServerSocket = null;
      } catch (IOException iOException) {
        iOException.printStackTrace();
        Trace.d(CMConstants.TAG_NAME, "ModeServer, stopModeServer(), mServerSocket exception!!!");
      }  
    if (this.mRunModeServer != null) {
      this.mRunModeServer.interrupt();
      this.mRunModeServer = null;
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\modemanager\ModeServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */