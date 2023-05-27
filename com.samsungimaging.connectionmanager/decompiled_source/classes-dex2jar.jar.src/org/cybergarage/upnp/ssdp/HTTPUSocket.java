package org.cybergarage.upnp.ssdp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.cybergarage.util.Debug;

public class HTTPUSocket {
  private String localAddr = "";
  
  private DatagramSocket ssdpUniSock = null;
  
  public HTTPUSocket() {
    open();
  }
  
  public HTTPUSocket(int paramInt) {
    open(paramInt);
  }
  
  public HTTPUSocket(String paramString, int paramInt) {
    open(paramString, paramInt);
  }
  
  public boolean close() {
    if (this.ssdpUniSock == null)
      return true; 
    try {
      this.ssdpUniSock.close();
      this.ssdpUniSock = null;
      return true;
    } catch (Exception exception) {
      Debug.warning(exception);
      return false;
    } 
  }
  
  protected void finalize() {
    close();
  }
  
  public DatagramSocket getDatagramSocket() {
    return this.ssdpUniSock;
  }
  
  public String getLocalAddress() {
    return (this.localAddr.length() > 0) ? this.localAddr : this.ssdpUniSock.getLocalAddress().getHostAddress();
  }
  
  public DatagramSocket getUDPSocket() {
    return this.ssdpUniSock;
  }
  
  public boolean open() {
    close();
    try {
      this.ssdpUniSock = new DatagramSocket();
      return true;
    } catch (Exception exception) {
      Debug.warning(exception);
      return false;
    } 
  }
  
  public boolean open(int paramInt) {
    close();
    try {
      InetSocketAddress inetSocketAddress = new InetSocketAddress(paramInt);
      this.ssdpUniSock = new DatagramSocket(null);
      this.ssdpUniSock.setReuseAddress(true);
      this.ssdpUniSock.bind(inetSocketAddress);
      return true;
    } catch (Exception exception) {
      Debug.warning(exception);
      return false;
    } 
  }
  
  public boolean open(String paramString, int paramInt) {
    close();
    try {
      this.ssdpUniSock = new DatagramSocket(new InetSocketAddress(paramInt));
      this.ssdpUniSock.setReuseAddress(true);
      setLocalAddress(paramString);
      return true;
    } catch (Exception exception) {
      Debug.warning(exception);
      return false;
    } 
  }
  
  public boolean post(String paramString1, int paramInt, String paramString2) {
    try {
      InetAddress inetAddress = InetAddress.getByName(paramString1);
      DatagramPacket datagramPacket = new DatagramPacket(paramString2.getBytes(), paramString2.length(), inetAddress, paramInt);
      this.ssdpUniSock.send(datagramPacket);
      this.ssdpUniSock.send(datagramPacket);
      this.ssdpUniSock.send(datagramPacket);
      return true;
    } catch (Exception exception) {
      Debug.warning("addr = " + this.ssdpUniSock.getLocalAddress().getHostName());
      Debug.warning("port = " + this.ssdpUniSock.getLocalPort());
      Debug.warning(exception);
      return false;
    } 
  }
  
  public SSDPPacket receive() {
    byte[] arrayOfByte = new byte[1024];
    SSDPPacket sSDPPacket = new SSDPPacket(arrayOfByte, arrayOfByte.length);
    sSDPPacket.setLocalAddress(getLocalAddress());
    try {
      this.ssdpUniSock.receive(sSDPPacket.getDatagramPacket());
      sSDPPacket.setTimeStamp(System.currentTimeMillis());
      return sSDPPacket;
    } catch (Exception exception) {
      Debug.warning(exception);
      return null;
    } 
  }
  
  public void setLocalAddress(String paramString) {
    this.localAddr = paramString;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ssdp\HTTPUSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */