package org.cybergarage.upnp.ssdp;

import java.io.InputStream;
import org.cybergarage.http.HTTPResponse;

public class SSDPResponse extends HTTPResponse {
  public SSDPResponse() {
    setVersion("1.1");
  }
  
  public SSDPResponse(InputStream paramInputStream) {
    super(paramInputStream);
  }
  
  public String getHeader() {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(getStatusLineString());
    stringBuffer.append(getHeaderString());
    stringBuffer.append("\r\n");
    return stringBuffer.toString();
  }
  
  public int getLeaseTime() {
    return SSDP.getLeaseTime(getHeaderValue("Cache-Control"));
  }
  
  public String getLocation() {
    return getHeaderValue("LOCATION");
  }
  
  public String getMYNAME() {
    return getHeaderValue("MYNAME");
  }
  
  public String getST() {
    return getHeaderValue("ST");
  }
  
  public String getUSN() {
    return getHeaderValue("USN");
  }
  
  public void setLeaseTime(int paramInt) {
    setHeader("Cache-Control", "max-age=" + Integer.toString(paramInt));
  }
  
  public void setLocation(String paramString) {
    setHeader("LOCATION", paramString);
  }
  
  public void setMYNAME(String paramString) {
    setHeader("MYNAME", paramString);
  }
  
  public void setST(String paramString) {
    setHeader("ST", paramString);
  }
  
  public void setUSN(String paramString) {
    setHeader("USN", paramString);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ssdp\SSDPResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */