package org.cybergarage.upnp.ssdp;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.cybergarage.http.HTTPHeader;
import org.cybergarage.upnp.device.MAN;
import org.cybergarage.upnp.device.NT;
import org.cybergarage.upnp.device.NTS;
import org.cybergarage.upnp.device.ST;
import org.cybergarage.upnp.device.USN;

public class SSDPPacket {
  private DatagramPacket dgmPacket = null;
  
  private String localAddr = "";
  
  public byte[] packetBytes = null;
  
  private long timeStamp;
  
  public SSDPPacket(byte[] paramArrayOfbyte, int paramInt) {
    this.dgmPacket = new DatagramPacket(paramArrayOfbyte, paramInt);
  }
  
  public String getCacheControl() {
    return HTTPHeader.getValue(getData(), "Cache-Control");
  }
  
  public byte[] getData() {
    if (this.packetBytes != null)
      return this.packetBytes; 
    DatagramPacket datagramPacket = getDatagramPacket();
    int i = datagramPacket.getLength();
    this.packetBytes = (new String(datagramPacket.getData(), 0, i)).getBytes();
    return this.packetBytes;
  }
  
  public DatagramPacket getDatagramPacket() {
    return this.dgmPacket;
  }
  
  public String getHost() {
    return HTTPHeader.getValue(getData(), "HOST");
  }
  
  public InetAddress getHostInetAddress() {
    String str2 = "127.0.0.1";
    String str1 = getHost();
    int i = str1.lastIndexOf(":");
    if (i >= 0) {
      str2 = str1.substring(0, i);
      str1 = str2;
      if (str2.charAt(0) == '[')
        str1 = str2.substring(1, str2.length()); 
      str2 = str1;
      if (str1.charAt(str1.length() - 1) == ']')
        str2 = str1.substring(0, str1.length() - 1); 
    } 
    return (new InetSocketAddress(str2, 0)).getAddress();
  }
  
  public int getLeaseTime() {
    return SSDP.getLeaseTime(getCacheControl());
  }
  
  public String getLocalAddress() {
    return this.localAddr;
  }
  
  public String getLocation() {
    return HTTPHeader.getValue(getData(), "LOCATION");
  }
  
  public String getMAN() {
    return HTTPHeader.getValue(getData(), "MAN");
  }
  
  public int getMX() {
    return HTTPHeader.getIntegerValue(getData(), "MX");
  }
  
  public String getNT() {
    return HTTPHeader.getValue(getData(), "NT");
  }
  
  public String getNTS() {
    return HTTPHeader.getValue(getData(), "NTS");
  }
  
  public String getRemoteAddress() {
    return getDatagramPacket().getAddress().getHostAddress();
  }
  
  public InetAddress getRemoteInetAddress() {
    return getDatagramPacket().getAddress();
  }
  
  public int getRemotePort() {
    return getDatagramPacket().getPort();
  }
  
  public String getST() {
    return HTTPHeader.getValue(getData(), "ST");
  }
  
  public String getServer() {
    return HTTPHeader.getValue(getData(), "Server");
  }
  
  public long getTimeStamp() {
    return this.timeStamp;
  }
  
  public String getUSN() {
    return HTTPHeader.getValue(getData(), "USN");
  }
  
  public boolean isAlive() {
    return NTS.isAlive(getNTS());
  }
  
  public boolean isByeBye() {
    return NTS.isByeBye(getNTS());
  }
  
  public boolean isDiscover() {
    return MAN.isDiscover(getMAN());
  }
  
  public boolean isRootDevice() {
    return (!NT.isRootDevice(getNT()) && !ST.isRootDevice(getST())) ? USN.isRootDevice(getUSN()) : true;
  }
  
  public void setLocalAddress(String paramString) {
    this.localAddr = paramString;
  }
  
  public void setTimeStamp(long paramLong) {
    this.timeStamp = paramLong;
  }
  
  public String toString() {
    return new String(getData());
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ssdp\SSDPPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */