package org.cybergarage.http;

import java.io.InputStream;

public class HTTPResponse extends HTTPPacket {
  private int statusCode = 0;
  
  public HTTPResponse() {
    setVersion("1.1");
    setContentType("text/html; charset=\"utf-8\"");
    setServer(HTTPServer.getName());
    setContent("");
  }
  
  public HTTPResponse(InputStream paramInputStream) {
    super(paramInputStream);
  }
  
  public HTTPResponse(HTTPResponse paramHTTPResponse) {
    set(paramHTTPResponse);
  }
  
  public HTTPResponse(HTTPSocket paramHTTPSocket) {
    this(paramHTTPSocket.getInputStream());
  }
  
  public String getHeader() {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(getStatusLineString());
    stringBuffer.append(getHeaderString());
    return stringBuffer.toString();
  }
  
  public int getStatusCode() {
    return (this.statusCode != 0) ? this.statusCode : (new HTTPStatus(getFirstLine())).getStatusCode();
  }
  
  public String getStatusLineString() {
    return "HTTP/" + getVersion() + " " + getStatusCode() + " " + HTTPStatus.code2String(this.statusCode) + "\r\n";
  }
  
  public boolean isSuccessful() {
    return HTTPStatus.isSuccessful(getStatusCode());
  }
  
  public void print() {
    System.out.println(toString());
  }
  
  public void setStatusCode(int paramInt) {
    this.statusCode = paramInt;
  }
  
  public String toString() {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(getStatusLineString());
    stringBuffer.append(getHeaderString());
    stringBuffer.append("\r\n");
    stringBuffer.append(getContentString());
    return stringBuffer.toString();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\http\HTTPResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */