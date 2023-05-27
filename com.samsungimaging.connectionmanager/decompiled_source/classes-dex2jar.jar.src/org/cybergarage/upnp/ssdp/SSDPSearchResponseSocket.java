package org.cybergarage.upnp.ssdp;

import java.net.DatagramSocket;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.SocketAddressException;

public class SSDPSearchResponseSocket extends HTTPUSocket implements Runnable {
  private ControlPoint controlPoint = null;
  
  private Thread deviceSearchResponseThread = null;
  
  public SSDPSearchResponseSocket() {
    setControlPoint((ControlPoint)null);
  }
  
  public SSDPSearchResponseSocket(String paramString, int paramInt) {
    super(paramString, paramInt);
    setControlPoint((ControlPoint)null);
  }
  
  public ControlPoint getControlPoint() {
    return this.controlPoint;
  }
  
  public boolean post(String paramString, int paramInt, SSDPSearchRequest paramSSDPSearchRequest) {
    return post(paramString, paramInt, paramSSDPSearchRequest.toString());
  }
  
  public boolean post(String paramString, int paramInt, SSDPSearchResponse paramSSDPSearchResponse) {
    return post(paramString, paramInt, paramSSDPSearchResponse.getHeader());
  }
  
  public void run() {
    Thread thread = Thread.currentThread();
    ControlPoint controlPoint = getControlPoint();
    while (true) {
      if (this.deviceSearchResponseThread == thread) {
        Thread.yield();
        SSDPPacket sSDPPacket = receive();
        if (sSDPPacket != null) {
          if (controlPoint != null)
            controlPoint.searchResponseReceived(sSDPPacket); 
          continue;
        } 
      } 
      return;
    } 
  }
  
  public void setControlPoint(ControlPoint paramControlPoint) {
    this.controlPoint = paramControlPoint;
  }
  
  public void start() throws SocketAddressException {
    StringBuffer stringBuffer = new StringBuffer("Cyber.SSDPSearchResponseSocket/");
    DatagramSocket datagramSocket = getDatagramSocket();
    if (datagramSocket != null) {
      if (datagramSocket.getLocalAddress() != null) {
        stringBuffer.append(datagramSocket.getLocalAddress()).append(':');
        stringBuffer.append(datagramSocket.getLocalPort());
      } 
      this.deviceSearchResponseThread = new Thread(this, stringBuffer.toString());
      this.deviceSearchResponseThread.start();
      return;
    } 
    throw new SocketAddressException("");
  }
  
  public void stop() {
    this.deviceSearchResponseThread = null;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ssdp\SSDPSearchResponseSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */