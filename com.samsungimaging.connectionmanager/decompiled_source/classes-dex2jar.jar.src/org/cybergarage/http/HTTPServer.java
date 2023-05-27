package org.cybergarage.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.cybergarage.util.Debug;
import org.cybergarage.util.ListenerList;

public class HTTPServer implements Runnable {
  public static final int DEFAULT_PORT = 80;
  
  public static final int DEFAULT_TIMEOUT = 15000;
  
  public static final String NAME = "CyberHTTP";
  
  public static final String VERSION = "1.0";
  
  private InetAddress bindAddr = null;
  
  private int bindPort = 0;
  
  private ListenerList httpRequestListenerList = new ListenerList();
  
  private Thread httpServerThread = null;
  
  private ServerSocket serverSock = null;
  
  protected int timeout = 15000;
  
  public HTTPServer() {
    this.serverSock = null;
  }
  
  public static String getName() {
    String str1 = System.getProperty("os.name");
    String str2 = System.getProperty("os.version");
    return String.valueOf(str1) + "/" + str2 + " " + "CyberHTTP" + "/" + "1.0";
  }
  
  public Socket accept() {
    if (this.serverSock == null)
      return null; 
    try {
      Socket socket = this.serverSock.accept();
      socket.setSoTimeout(getTimeout());
      return socket;
    } catch (IOException iOException) {
      return null;
    } catch (Exception exception) {
      return null;
    } 
  }
  
  public void addRequestListener(HTTPRequestListener paramHTTPRequestListener) {
    this.httpRequestListenerList.add(paramHTTPRequestListener);
  }
  
  public boolean close() {
    if (this.serverSock == null)
      return true; 
    try {
      this.serverSock.close();
      this.serverSock = null;
      this.bindAddr = null;
      this.bindPort = 0;
      return true;
    } catch (Exception exception) {
      Debug.warning(exception);
      return false;
    } 
  }
  
  public String getBindAddress() {
    return (this.bindAddr == null) ? "" : this.bindAddr.toString();
  }
  
  public int getBindPort() {
    return this.bindPort;
  }
  
  public ServerSocket getServerSock() {
    return this.serverSock;
  }
  
  public int getTimeout() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield timeout : I
    //   6: istore_1
    //   7: aload_0
    //   8: monitorexit
    //   9: iload_1
    //   10: ireturn
    //   11: astore_2
    //   12: aload_0
    //   13: monitorexit
    //   14: aload_2
    //   15: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	11	finally
  }
  
  public boolean isOpened() {
    return (this.serverSock != null);
  }
  
  public boolean open(String paramString, int paramInt) {
    if (this.serverSock != null)
      return true; 
    try {
      this.bindAddr = InetAddress.getByName(paramString);
      this.bindPort = paramInt;
      this.serverSock = new ServerSocket(this.bindPort, 0, this.bindAddr);
      return true;
    } catch (IOException iOException) {
      return false;
    } 
  }
  
  public boolean open(InetAddress paramInetAddress, int paramInt) {
    if (this.serverSock != null)
      return true; 
    try {
      this.serverSock = new ServerSocket(this.bindPort, 0, this.bindAddr);
      return true;
    } catch (IOException iOException) {
      return false;
    } 
  }
  
  public void performRequestListener(HTTPRequest paramHTTPRequest) {
    int j = this.httpRequestListenerList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      ((HTTPRequestListener)this.httpRequestListenerList.get(i)).httpRequestRecieved(paramHTTPRequest);
    } 
  }
  
  public void removeRequestListener(HTTPRequestListener paramHTTPRequestListener) {
    this.httpRequestListenerList.remove(paramHTTPRequestListener);
  }
  
  public void run() {
    if (isOpened()) {
      Thread thread = Thread.currentThread();
      while (true) {
        if (this.httpServerThread == thread) {
          Socket socket;
          Thread.yield();
          try {
            Debug.message("accept ...");
            socket = accept();
            if (socket != null)
              Debug.message("sock = " + socket.getRemoteSocketAddress()); 
            if (thread.isInterrupted()) {
              if (socket != null)
                try {
                  socket.close();
                } catch (IOException iOException) {
                  iOException.printStackTrace();
                }  
              Debug.message("httpServerThread interrupted");
              return;
            } 
          } catch (Exception exception) {
            Debug.warning(exception);
            return;
          } 
          (new HTTPServerThread(this, socket)).start();
          Debug.message("httpServerThread ...");
          continue;
        } 
        return;
      } 
    } 
  }
  
  public void setTimeout(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: putfield timeout : I
    //   7: aload_0
    //   8: monitorexit
    //   9: return
    //   10: astore_2
    //   11: aload_0
    //   12: monitorexit
    //   13: aload_2
    //   14: athrow
    // Exception table:
    //   from	to	target	type
    //   2	7	10	finally
  }
  
  public boolean start() {
    this.httpServerThread = new Thread(this, (new StringBuffer("Cyber.HTTPServer/")).toString());
    this.httpServerThread.start();
    return true;
  }
  
  public boolean stop() {
    if (this.httpServerThread != null) {
      this.httpServerThread.interrupt();
      this.httpServerThread = null;
    } 
    this.httpRequestListenerList.clear();
    return true;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\http\HTTPServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */