package org.cybergarage.upnp.ssdp;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.device.SearchListener;
import org.cybergarage.util.ListenerList;

public class SSDPSearchSocket extends HTTPMUSocket implements Runnable {
  private ListenerList deviceSearchListenerList = new ListenerList();
  
  private Thread deviceSearchThread = null;
  
  private boolean useIPv6Address;
  
  public SSDPSearchSocket(String paramString1, int paramInt, String paramString2) {
    open(paramString1, paramString2);
  }
  
  public SSDPSearchSocket(InetAddress paramInetAddress) {
    if ((paramInetAddress.getAddress()).length != 4) {
      open((Inet6Address)paramInetAddress);
      return;
    } 
    open((Inet4Address)paramInetAddress);
  }
  
  public void addSearchListener(SearchListener paramSearchListener) {
    this.deviceSearchListenerList.add(paramSearchListener);
  }
  
  public boolean open(String paramString) {
    String str = "239.255.255.250";
    this.useIPv6Address = false;
    if (HostInterface.isIPv6Address(paramString)) {
      str = SSDP.getIPv6Address();
      this.useIPv6Address = true;
    } 
    return open(str, SSDP.PORT, paramString);
  }
  
  public boolean open(String paramString1, String paramString2) {
    if (HostInterface.isIPv6Address(paramString1) && HostInterface.isIPv6Address(paramString2)) {
      this.useIPv6Address = true;
      return open(paramString2, SSDP.PORT, paramString1);
    } 
    if (HostInterface.isIPv4Address(paramString1) && HostInterface.isIPv4Address(paramString2)) {
      this.useIPv6Address = false;
      return open(paramString2, SSDP.PORT, paramString1);
    } 
    throw new IllegalArgumentException("Cannot open a UDP Socket for IPv6 address on IPv4 interface or viceversa");
  }
  
  public boolean open(Inet4Address paramInet4Address) {
    this.useIPv6Address = false;
    return open("239.255.255.250", SSDP.PORT, paramInet4Address);
  }
  
  public boolean open(Inet6Address paramInet6Address) {
    this.useIPv6Address = true;
    return open(SSDP.getIPv6Address(), SSDP.PORT, paramInet6Address);
  }
  
  public void performSearchListener(SSDPPacket paramSSDPPacket) {
    int j = this.deviceSearchListenerList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      ((SearchListener)this.deviceSearchListenerList.get(i)).deviceSearchReceived(paramSSDPPacket);
    } 
  }
  
  public void removeSearchListener(SearchListener paramSearchListener) {
    this.deviceSearchListenerList.remove(paramSearchListener);
  }
  
  public void run() {
    Thread thread = Thread.currentThread();
    while (true) {
      if (this.deviceSearchThread != thread)
        return; 
      Thread.yield();
      try {
        SSDPPacket sSDPPacket = receive();
        if (sSDPPacket != null && sSDPPacket.isDiscover())
          performSearchListener(sSDPPacket); 
      } catch (IOException iOException) {
        break;
      } 
    } 
  }
  
  public void start() {
    StringBuffer stringBuffer = new StringBuffer("Cyber.SSDPSearchSocket/");
    String str = getLocalAddress();
    if (str != null && str.length() > 0) {
      stringBuffer.append(getLocalAddress()).append(':');
      stringBuffer.append(getLocalPort()).append(" -> ");
      stringBuffer.append(getMulticastAddress()).append(':');
      stringBuffer.append(getMulticastPort());
    } 
    this.deviceSearchThread = new Thread(this, stringBuffer.toString());
    this.deviceSearchThread.start();
  }
  
  public void stop() {
    close();
    this.deviceSearchThread = null;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ssdp\SSDPSearchSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */