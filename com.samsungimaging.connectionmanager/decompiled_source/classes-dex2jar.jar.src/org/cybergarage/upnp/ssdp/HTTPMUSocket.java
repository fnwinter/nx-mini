package org.cybergarage.upnp.ssdp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.util.Debug;

public class HTTPMUSocket {
  private InetSocketAddress ssdpMultiGroup = null;
  
  private NetworkInterface ssdpMultiIf = null;
  
  private MulticastSocket ssdpMultiSock = null;
  
  public HTTPMUSocket() {}
  
  public HTTPMUSocket(String paramString1, int paramInt, String paramString2) {
    open(paramString1, paramInt, paramString2);
  }
  
  public boolean close() {
    if (this.ssdpMultiSock == null)
      return true; 
    try {
      this.ssdpMultiSock.leaveGroup(this.ssdpMultiGroup, this.ssdpMultiIf);
      this.ssdpMultiSock.close();
      this.ssdpMultiSock = null;
      return true;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  protected void finalize() {
    close();
  }
  
  public String getLocalAddress() {
    if (this.ssdpMultiGroup == null || this.ssdpMultiIf == null)
      return ""; 
    InetAddress inetAddress = this.ssdpMultiGroup.getAddress();
    Enumeration<InetAddress> enumeration = this.ssdpMultiIf.getInetAddresses();
    while (true) {
      if (!enumeration.hasMoreElements())
        return ""; 
      InetAddress inetAddress1 = enumeration.nextElement();
      if (inetAddress instanceof java.net.Inet6Address && inetAddress1 instanceof java.net.Inet6Address)
        return inetAddress1.getHostAddress(); 
      if (inetAddress instanceof java.net.Inet4Address && inetAddress1 instanceof java.net.Inet4Address)
        return inetAddress1.getHostAddress(); 
    } 
  }
  
  public int getLocalPort() {
    return this.ssdpMultiSock.getLocalPort();
  }
  
  public String getMulticastAddress() {
    return getMulticastInetAddress().getHostAddress();
  }
  
  public InetAddress getMulticastInetAddress() {
    return this.ssdpMultiGroup.getAddress();
  }
  
  public int getMulticastPort() {
    return this.ssdpMultiGroup.getPort();
  }
  
  public MulticastSocket getSocket() {
    return this.ssdpMultiSock;
  }
  
  public boolean open(String paramString1, int paramInt, String paramString2) {
    try {
      return open(paramString1, paramInt, InetAddress.getByName(paramString2));
    } catch (Exception exception) {
      Debug.warning(exception);
      return false;
    } 
  }
  
  public boolean open(String paramString, int paramInt, InetAddress paramInetAddress) {
    try {
      this.ssdpMultiSock = new MulticastSocket(null);
      this.ssdpMultiSock.setReuseAddress(true);
      InetSocketAddress inetSocketAddress = new InetSocketAddress(paramInt);
      this.ssdpMultiSock.bind(inetSocketAddress);
      this.ssdpMultiGroup = new InetSocketAddress(InetAddress.getByName(paramString), paramInt);
      this.ssdpMultiIf = NetworkInterface.getByInetAddress(paramInetAddress);
      this.ssdpMultiSock.joinGroup(this.ssdpMultiGroup, this.ssdpMultiIf);
      return true;
    } catch (Exception exception) {
      Debug.warning(exception);
      return false;
    } 
  }
  
  public boolean post(HTTPRequest paramHTTPRequest) {
    return send(paramHTTPRequest.toString(), null, -1);
  }
  
  public boolean post(HTTPRequest paramHTTPRequest, String paramString, int paramInt) {
    return send(paramHTTPRequest.toString(), paramString, paramInt);
  }
  
  public SSDPPacket receive() throws IOException {
    byte[] arrayOfByte = new byte[1024];
    SSDPPacket sSDPPacket = new SSDPPacket(arrayOfByte, arrayOfByte.length);
    sSDPPacket.setLocalAddress(getLocalAddress());
    if (this.ssdpMultiSock == null)
      return null; 
    this.ssdpMultiSock.receive(sSDPPacket.getDatagramPacket());
    sSDPPacket.setTimeStamp(System.currentTimeMillis());
    return sSDPPacket;
  }
  
  public boolean send(String paramString) {
    return send(paramString, null, -1);
  }
  
  public boolean send(String paramString1, String paramString2, int paramInt) {
    if (paramString2 != null && paramInt > 0)
      try {
        MulticastSocket multicastSocket2 = new MulticastSocket(null);
        multicastSocket2.bind(new InetSocketAddress(paramString2, paramInt));
        MulticastSocket multicastSocket1 = multicastSocket2;
        DatagramPacket datagramPacket1 = new DatagramPacket(paramString1.getBytes(), paramString1.length(), this.ssdpMultiGroup);
        multicastSocket1.setTimeToLive(UPnP.getTimeToLive());
        multicastSocket1.send(datagramPacket1);
        multicastSocket1.close();
        return true;
      } catch (Exception exception) {
        Debug.warning(exception);
        return false;
      }  
    MulticastSocket multicastSocket = new MulticastSocket();
    DatagramPacket datagramPacket = new DatagramPacket(exception.getBytes(), exception.length(), this.ssdpMultiGroup);
    multicastSocket.setTimeToLive(UPnP.getTimeToLive());
    multicastSocket.send(datagramPacket);
    multicastSocket.close();
    return true;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ssdp\HTTPMUSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */