package org.cybergarage.upnp.ssdp;

import java.io.IOException;
import java.net.InetAddress;
import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.util.Debug;

public class SSDPNotifySocket extends HTTPMUSocket implements Runnable {
  private ControlPoint controlPoint = null;
  
  private Thread deviceNotifyThread = null;
  
  private boolean useIPv6Address;
  
  public SSDPNotifySocket(String paramString) {
    String str = "239.255.255.250";
    this.useIPv6Address = false;
    if (HostInterface.isIPv6Address(paramString)) {
      str = SSDP.getIPv6Address();
      this.useIPv6Address = true;
    } 
    open(str, SSDP.PORT, paramString);
    setControlPoint((ControlPoint)null);
  }
  
  public ControlPoint getControlPoint() {
    return this.controlPoint;
  }
  
  public boolean post(SSDPNotifyRequest paramSSDPNotifyRequest) {
    String str = "239.255.255.250";
    if (this.useIPv6Address)
      str = SSDP.getIPv6Address(); 
    paramSSDPNotifyRequest.setHost(str, SSDP.PORT);
    return post(paramSSDPNotifyRequest);
  }
  
  public void run() {
    Thread thread = Thread.currentThread();
    ControlPoint controlPoint = getControlPoint();
    while (true) {
      if (this.deviceNotifyThread != thread)
        return; 
      Thread.yield();
      try {
        SSDPPacket sSDPPacket = receive();
        if (sSDPPacket != null) {
          InetAddress inetAddress1 = getMulticastInetAddress();
          InetAddress inetAddress2 = sSDPPacket.getHostInetAddress();
          if (!inetAddress1.equals(inetAddress2)) {
            Debug.warning("Invalidate Multicast Recieved from IP " + inetAddress1 + " on " + inetAddress2);
            continue;
          } 
          if (controlPoint != null)
            controlPoint.notifyReceived(sSDPPacket); 
        } 
      } catch (IOException iOException) {
        return;
      } 
    } 
  }
  
  public void setControlPoint(ControlPoint paramControlPoint) {
    this.controlPoint = paramControlPoint;
  }
  
  public void start() {
    StringBuffer stringBuffer = new StringBuffer("Cyber.SSDPNotifySocket/");
    String str = getLocalAddress();
    if (str != null && str.length() > 0) {
      stringBuffer.append(getLocalAddress()).append(':');
      stringBuffer.append(getLocalPort()).append(" -> ");
      stringBuffer.append(getMulticastAddress()).append(':');
      stringBuffer.append(getMulticastPort());
    } 
    this.deviceNotifyThread = new Thread(this, stringBuffer.toString());
    this.deviceNotifyThread.start();
  }
  
  public void stop() {
    close();
    this.deviceNotifyThread = null;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ssdp\SSDPNotifySocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */