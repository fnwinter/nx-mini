package org.cybergarage.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Calendar;

public class HTTPSocket {
  private InputStream sockIn = null;
  
  private OutputStream sockOut = null;
  
  private Socket socket = null;
  
  public HTTPSocket(Socket paramSocket) {
    setSocket(paramSocket);
    open();
  }
  
  public HTTPSocket(HTTPSocket paramHTTPSocket) {
    setSocket(paramHTTPSocket.getSocket());
    setInputStream(paramHTTPSocket.getInputStream());
    setOutputStream(paramHTTPSocket.getOutputStream());
  }
  
  private OutputStream getOutputStream() {
    return this.sockOut;
  }
  
  private boolean post(HTTPResponse paramHTTPResponse, InputStream paramInputStream, long paramLong1, long paramLong2, boolean paramBoolean) {
    int i;
    int j;
    paramHTTPResponse.setDate(Calendar.getInstance());
    OutputStream outputStream = getOutputStream();
    try {
      paramHTTPResponse.setContentLength(paramLong2);
      outputStream.write(paramHTTPResponse.getHeader().getBytes());
      outputStream.write("\r\n".getBytes());
      if (paramBoolean) {
        outputStream.flush();
        return true;
      } 
      paramBoolean = paramHTTPResponse.isChunked();
      if (0L < paramLong1)
        paramInputStream.skip(paramLong1); 
      j = HTTP.getChunkSize();
      byte[] arrayOfByte = new byte[j];
      long l = 0L;
      if (j < paramLong2) {
        paramLong1 = j;
      } else {
        paramLong1 = paramLong2;
      } 
      i = paramInputStream.read(arrayOfByte, 0, (int)paramLong1);
      paramLong1 = l;
    } catch (Exception exception) {
      return false;
    } 
    while (true) {
      if (i <= 0 || paramLong1 >= paramLong2) {
        if (paramBoolean) {
          outputStream.write("0".getBytes());
          outputStream.write("\r\n".getBytes());
        } 
        outputStream.flush();
        return true;
      } 
      if (paramBoolean) {
        outputStream.write(Long.toHexString(i).getBytes());
        outputStream.write("\r\n".getBytes());
      } 
      outputStream.write((byte[])exception, 0, i);
      if (paramBoolean)
        outputStream.write("\r\n".getBytes()); 
      long l = paramLong1 + i;
      if (j < paramLong2 - l) {
        paramLong1 = j;
      } else {
        paramLong1 = paramLong2 - l;
      } 
      i = paramInputStream.read((byte[])exception, 0, (int)paramLong1);
      paramLong1 = l;
    } 
  }
  
  private boolean post(HTTPResponse paramHTTPResponse, byte[] paramArrayOfbyte, long paramLong1, long paramLong2, boolean paramBoolean) {
    paramHTTPResponse.setDate(Calendar.getInstance());
    OutputStream outputStream = getOutputStream();
    try {
      paramHTTPResponse.setContentLength(paramLong2);
      outputStream.write(paramHTTPResponse.getHeader().getBytes());
      outputStream.write("\r\n".getBytes());
      if (paramBoolean) {
        outputStream.flush();
        return true;
      } 
      paramBoolean = paramHTTPResponse.isChunked();
      if (paramBoolean) {
        outputStream.write(Long.toHexString(paramLong2).getBytes());
        outputStream.write("\r\n".getBytes());
      } 
      outputStream.write(paramArrayOfbyte, (int)paramLong1, (int)paramLong2);
      if (paramBoolean) {
        outputStream.write("\r\n".getBytes());
        outputStream.write("0".getBytes());
        outputStream.write("\r\n".getBytes());
      } 
      outputStream.flush();
      return true;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  private void setInputStream(InputStream paramInputStream) {
    this.sockIn = paramInputStream;
  }
  
  private void setOutputStream(OutputStream paramOutputStream) {
    this.sockOut = paramOutputStream;
  }
  
  private void setSocket(Socket paramSocket) {
    this.socket = paramSocket;
  }
  
  public boolean close() {
    try {
      if (this.sockIn != null)
        this.sockIn.close(); 
      if (this.sockOut != null)
        this.sockOut.close(); 
      getSocket().close();
      return true;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public void finalize() {
    close();
  }
  
  public InputStream getInputStream() {
    return this.sockIn;
  }
  
  public String getLocalAddress() {
    return getSocket().getLocalAddress().getHostAddress();
  }
  
  public int getLocalPort() {
    return getSocket().getLocalPort();
  }
  
  public Socket getSocket() {
    return this.socket;
  }
  
  public boolean open() {
    Socket socket = getSocket();
    try {
      this.sockIn = socket.getInputStream();
      this.sockOut = socket.getOutputStream();
      return true;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public boolean post(HTTPResponse paramHTTPResponse, long paramLong1, long paramLong2, boolean paramBoolean) {
    return paramHTTPResponse.hasContentInputStream() ? post(paramHTTPResponse, paramHTTPResponse.getContentInputStream(), paramLong1, paramLong2, paramBoolean) : post(paramHTTPResponse, paramHTTPResponse.getContent(), paramLong1, paramLong2, paramBoolean);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\http\HTTPSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */