package org.cybergarage.http;

import java.net.Socket;

public class HTTPServerThread extends Thread {
  private HTTPServer httpServer;
  
  private Socket sock;
  
  public HTTPServerThread(HTTPServer paramHTTPServer, Socket paramSocket) {
    super("Cyber.HTTPServerThread");
    this.httpServer = paramHTTPServer;
    this.sock = paramSocket;
  }
  
  public void run() {
    HTTPSocket hTTPSocket = new HTTPSocket(this.sock);
    if (!hTTPSocket.open())
      return; 
    HTTPRequest hTTPRequest = new HTTPRequest();
    hTTPRequest.setSocket(hTTPSocket);
    while (true) {
      if (hTTPRequest.read()) {
        this.httpServer.performRequestListener(hTTPRequest);
        if (!hTTPRequest.isKeepAlive()) {
          hTTPSocket.close();
          return;
        } 
        continue;
      } 
      hTTPSocket.close();
      return;
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\http\HTTPServerThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */