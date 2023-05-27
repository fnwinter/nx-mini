package org.cybergarage.http;

import com.samsungimaging.connectionmanager.util.Trace;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.StringTokenizer;
import org.cybergarage.util.Debug;

public class HTTPRequest extends HTTPPacket {
  private Trace.Tag TAG = Trace.Tag.CYBERGARAGE;
  
  private HTTPSocket httpSocket = null;
  
  private String method = null;
  
  private Socket postSocket = null;
  
  private String requestHost = "";
  
  private int requestPort = -1;
  
  private String uri = null;
  
  public HTTPRequest() {
    setVersion("1.0");
  }
  
  public HTTPRequest(InputStream paramInputStream) {
    super(paramInputStream);
  }
  
  public HTTPRequest(HTTPSocket paramHTTPSocket) {
    this(paramHTTPSocket.getInputStream());
    setSocket(paramHTTPSocket);
  }
  
  public String getFirstLineString() {
    String str = null;
    try {
      URL uRL = new URL(getURI());
      if (uRL != null) {
        try {
          return String.valueOf(getMethod()) + " " + uRL.getPath() + " " + getHTTPVersion() + "\r\n";
        } catch (MalformedURLException null) {}
      } else {
        return (String)malformedURLException;
      } 
    } catch (MalformedURLException malformedURLException) {}
    malformedURLException.printStackTrace();
    return String.valueOf(getMethod()) + " " + getURI() + " " + getHTTPVersion() + "\r\n";
  }
  
  public String getHTTPVersion() {
    return hasFirstLine() ? getFirstLineToken(2) : ("HTTP/" + getVersion());
  }
  
  public String getHeader() {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(getFirstLineString());
    stringBuffer.append(getHeaderString());
    return stringBuffer.toString();
  }
  
  public String getLocalAddress() {
    return getSocket().getLocalAddress();
  }
  
  public int getLocalPort() {
    return getSocket().getLocalPort();
  }
  
  public String getMethod() {
    return (this.method != null) ? this.method : getFirstLineToken(0);
  }
  
  public ParameterList getParameterList() {
    ParameterList parameterList = new ParameterList();
    String str = getURI();
    if (str != null) {
      int i = str.indexOf('?');
      if (i >= 0)
        while (true) {
          if (i > 0) {
            int j;
            int k = str.indexOf('=', i + 1);
            String str1 = str.substring(i + 1, k);
            i = str.indexOf('&', k + 1);
            if (i > 0) {
              j = i;
            } else {
              j = str.length();
            } 
            parameterList.add((E)new Parameter(str1, str.substring(k + 1, j)));
            continue;
          } 
          return parameterList;
        }  
    } 
    return parameterList;
  }
  
  public String getParameterValue(String paramString) {
    return getParameterList().getValue(paramString);
  }
  
  public String getRequestHost() {
    return this.requestHost;
  }
  
  public int getRequestPort() {
    return this.requestPort;
  }
  
  public HTTPSocket getSocket() {
    return this.httpSocket;
  }
  
  public String getURI() {
    return (this.uri != null) ? this.uri : getFirstLineToken(1);
  }
  
  public boolean isGetRequest() {
    return isMethod("GET");
  }
  
  public boolean isHeadRequest() {
    return isMethod("HEAD");
  }
  
  public boolean isKeepAlive() {
    if (!isCloseConnection()) {
      boolean bool;
      if (isKeepAliveConnection())
        return true; 
      if (getHTTPVersion().indexOf("1.0") > 0) {
        bool = true;
      } else {
        bool = false;
      } 
      if (!bool)
        return true; 
    } 
    return false;
  }
  
  public boolean isMethod(String paramString) {
    String str = getMethod();
    return (str == null) ? false : str.equalsIgnoreCase(paramString);
  }
  
  public boolean isNotifyRequest() {
    return isMethod("NOTIFY");
  }
  
  public boolean isPostRequest() {
    return isMethod("POST");
  }
  
  public boolean isSOAPAction() {
    return hasHeader("SOAPACTION");
  }
  
  public boolean isSubscribeRequest() {
    return isMethod("SUBSCRIBE");
  }
  
  public boolean isUnsubscribeRequest() {
    return isMethod("UNSUBSCRIBE");
  }
  
  public boolean parseRequestLine(String paramString) {
    StringTokenizer stringTokenizer = new StringTokenizer(paramString, " ");
    if (stringTokenizer.hasMoreTokens()) {
      setMethod(stringTokenizer.nextToken());
      if (stringTokenizer.hasMoreTokens()) {
        setURI(stringTokenizer.nextToken());
        if (stringTokenizer.hasMoreTokens()) {
          setVersion(stringTokenizer.nextToken());
          return true;
        } 
      } 
    } 
    return false;
  }
  
  public HTTPResponse post(String paramString, int paramInt) {
    return post(paramString, paramInt, false, 15000);
  }
  
  public HTTPResponse post(String paramString, int paramInt1, int paramInt2) {
    return post(paramString, paramInt1, false, paramInt2);
  }
  
  public HTTPResponse post(String paramString, int paramInt1, boolean paramBoolean, int paramInt2) {
    OutputStream outputStream1;
    Trace.d(this.TAG, "start post() host : " + paramString + " port : " + paramInt1);
    Trace.d(this.TAG, "Res 1");
    HTTPResponse hTTPResponse = new HTTPResponse();
    Trace.d(this.TAG, "Res 2");
    setHost(paramString);
    Trace.d(this.TAG, "Res 3");
    if (paramBoolean) {
      str1 = "Keep-Alive";
    } else {
      str1 = "close";
    } 
    setConnection(str1);
    Trace.d(this.TAG, "Res 4");
    boolean bool = isHeadRequest();
    Trace.d(this.TAG, "Res 5");
    OutputStream outputStream6 = null;
    OutputStream outputStream7 = null;
    String str2 = null;
    OutputStream outputStream5 = null;
    InputStream inputStream6 = null;
    InputStream inputStream7 = null;
    InputStream inputStream8 = null;
    InputStream inputStream2 = null;
    InputStream inputStream3 = inputStream2;
    OutputStream outputStream2 = outputStream5;
    InputStream inputStream4 = inputStream6;
    OutputStream outputStream3 = outputStream6;
    InputStream inputStream5 = inputStream7;
    OutputStream outputStream4 = outputStream7;
    InputStream inputStream1 = inputStream8;
    String str1 = str2;
    try {
      Trace.d(this.TAG, "Try 1");
      inputStream3 = inputStream2;
      outputStream2 = outputStream5;
      inputStream4 = inputStream6;
      outputStream3 = outputStream6;
      inputStream5 = inputStream7;
      outputStream4 = outputStream7;
      inputStream1 = inputStream8;
      str1 = str2;
      if (this.postSocket == null) {
        inputStream3 = inputStream2;
        outputStream2 = outputStream5;
        inputStream4 = inputStream6;
        outputStream3 = outputStream6;
        inputStream5 = inputStream7;
        outputStream4 = outputStream7;
        inputStream1 = inputStream8;
        str1 = str2;
        this.postSocket = new Socket();
        inputStream3 = inputStream2;
        outputStream2 = outputStream5;
        inputStream4 = inputStream6;
        outputStream3 = outputStream6;
        inputStream5 = inputStream7;
        outputStream4 = outputStream7;
        inputStream1 = inputStream8;
        str1 = str2;
        InetSocketAddress inetSocketAddress = new InetSocketAddress(paramString, paramInt1);
        inputStream3 = inputStream2;
        outputStream2 = outputStream5;
        inputStream4 = inputStream6;
        outputStream3 = outputStream6;
        inputStream5 = inputStream7;
        outputStream4 = outputStream7;
        inputStream1 = inputStream8;
        str1 = str2;
        this.postSocket.connect(inetSocketAddress, paramInt2);
      } 
      inputStream3 = inputStream2;
      outputStream2 = outputStream5;
      inputStream4 = inputStream6;
      outputStream3 = outputStream6;
      inputStream5 = inputStream7;
      outputStream4 = outputStream7;
      inputStream1 = inputStream8;
      str1 = str2;
      Trace.d(this.TAG, "Try 2");
      inputStream3 = inputStream2;
      outputStream2 = outputStream5;
      inputStream4 = inputStream6;
      outputStream3 = outputStream6;
      inputStream5 = inputStream7;
      outputStream4 = outputStream7;
      inputStream1 = inputStream8;
      str1 = str2;
      OutputStream outputStream = this.postSocket.getOutputStream();
      inputStream3 = inputStream2;
      outputStream2 = outputStream;
      inputStream4 = inputStream6;
      outputStream3 = outputStream;
      inputStream5 = inputStream7;
      outputStream4 = outputStream;
      inputStream1 = inputStream8;
      outputStream1 = outputStream;
      Trace.d(this.TAG, "Try 3");
      inputStream3 = inputStream2;
      outputStream2 = outputStream;
      inputStream4 = inputStream6;
      outputStream3 = outputStream;
      inputStream5 = inputStream7;
      outputStream4 = outputStream;
      inputStream1 = inputStream8;
      outputStream1 = outputStream;
      outputStream5 = new PrintStream(outputStream);
      inputStream3 = inputStream2;
      outputStream2 = outputStream;
      inputStream4 = inputStream6;
      outputStream3 = outputStream;
      inputStream5 = inputStream7;
      outputStream4 = outputStream;
      inputStream1 = inputStream8;
      outputStream1 = outputStream;
      outputStream5.print(getHeader());
      inputStream3 = inputStream2;
      outputStream2 = outputStream;
      inputStream4 = inputStream6;
      outputStream3 = outputStream;
      inputStream5 = inputStream7;
      outputStream4 = outputStream;
      inputStream1 = inputStream8;
      outputStream1 = outputStream;
      Trace.d(this.TAG, "Try 4");
      inputStream3 = inputStream2;
      outputStream2 = outputStream;
      inputStream4 = inputStream6;
      outputStream3 = outputStream;
      inputStream5 = inputStream7;
      outputStream4 = outputStream;
      inputStream1 = inputStream8;
      outputStream1 = outputStream;
      outputStream5.print("\r\n");
      inputStream3 = inputStream2;
      outputStream2 = outputStream;
      inputStream4 = inputStream6;
      outputStream3 = outputStream;
      inputStream5 = inputStream7;
      outputStream4 = outputStream;
      inputStream1 = inputStream8;
      outputStream1 = outputStream;
      boolean bool1 = isChunked();
      inputStream3 = inputStream2;
      outputStream2 = outputStream;
      inputStream4 = inputStream6;
      outputStream3 = outputStream;
      inputStream5 = inputStream7;
      outputStream4 = outputStream;
      inputStream1 = inputStream8;
      outputStream1 = outputStream;
      Trace.d(this.TAG, "Try 5");
      inputStream3 = inputStream2;
      outputStream2 = outputStream;
      inputStream4 = inputStream6;
      outputStream3 = outputStream;
      inputStream5 = inputStream7;
      outputStream4 = outputStream;
      inputStream1 = inputStream8;
      outputStream1 = outputStream;
      String str = getContentString();
      inputStream3 = inputStream2;
      outputStream2 = outputStream;
      inputStream4 = inputStream6;
      outputStream3 = outputStream;
      inputStream5 = inputStream7;
      outputStream4 = outputStream;
      inputStream1 = inputStream8;
      outputStream1 = outputStream;
      Trace.d(this.TAG, "Try 6");
      paramInt1 = 0;
      if (str != null) {
        inputStream3 = inputStream2;
        outputStream2 = outputStream;
        inputStream4 = inputStream6;
        outputStream3 = outputStream;
        inputStream5 = inputStream7;
        outputStream4 = outputStream;
        inputStream1 = inputStream8;
        outputStream1 = outputStream;
        paramInt1 = str.length();
      } 
      inputStream3 = inputStream2;
      outputStream2 = outputStream;
      inputStream4 = inputStream6;
      outputStream3 = outputStream;
      inputStream5 = inputStream7;
      outputStream4 = outputStream;
      inputStream1 = inputStream8;
      outputStream1 = outputStream;
      Trace.d(this.TAG, "Try 7");
      if (paramInt1 > 0) {
        if (bool1) {
          inputStream3 = inputStream2;
          outputStream2 = outputStream;
          inputStream4 = inputStream6;
          outputStream3 = outputStream;
          inputStream5 = inputStream7;
          outputStream4 = outputStream;
          inputStream1 = inputStream8;
          outputStream1 = outputStream;
          outputStream5.print(Long.toHexString(paramInt1));
          inputStream3 = inputStream2;
          outputStream2 = outputStream;
          inputStream4 = inputStream6;
          outputStream3 = outputStream;
          inputStream5 = inputStream7;
          outputStream4 = outputStream;
          inputStream1 = inputStream8;
          outputStream1 = outputStream;
          outputStream5.print("\r\n");
        } 
        inputStream3 = inputStream2;
        outputStream2 = outputStream;
        inputStream4 = inputStream6;
        outputStream3 = outputStream;
        inputStream5 = inputStream7;
        outputStream4 = outputStream;
        inputStream1 = inputStream8;
        outputStream1 = outputStream;
        outputStream5.print(str);
        if (bool1) {
          inputStream3 = inputStream2;
          outputStream2 = outputStream;
          inputStream4 = inputStream6;
          outputStream3 = outputStream;
          inputStream5 = inputStream7;
          outputStream4 = outputStream;
          inputStream1 = inputStream8;
          outputStream1 = outputStream;
          outputStream5.print("\r\n");
        } 
      } 
      if (bool1) {
        inputStream3 = inputStream2;
        outputStream2 = outputStream;
        inputStream4 = inputStream6;
        outputStream3 = outputStream;
        inputStream5 = inputStream7;
        outputStream4 = outputStream;
        inputStream1 = inputStream8;
        outputStream1 = outputStream;
        outputStream5.print("0");
        inputStream3 = inputStream2;
        outputStream2 = outputStream;
        inputStream4 = inputStream6;
        outputStream3 = outputStream;
        inputStream5 = inputStream7;
        outputStream4 = outputStream;
        inputStream1 = inputStream8;
        outputStream1 = outputStream;
        outputStream5.print("\r\n");
      } 
      inputStream3 = inputStream2;
      outputStream2 = outputStream;
      inputStream4 = inputStream6;
      outputStream3 = outputStream;
      inputStream5 = inputStream7;
      outputStream4 = outputStream;
      inputStream1 = inputStream8;
      outputStream1 = outputStream;
      outputStream5.flush();
      inputStream3 = inputStream2;
      outputStream2 = outputStream;
      inputStream4 = inputStream6;
      outputStream3 = outputStream;
      inputStream5 = inputStream7;
      outputStream4 = outputStream;
      inputStream1 = inputStream8;
      outputStream1 = outputStream;
      Trace.d(this.TAG, "Try 8");
      inputStream3 = inputStream2;
      outputStream2 = outputStream;
      inputStream4 = inputStream6;
      outputStream3 = outputStream;
      inputStream5 = inputStream7;
      outputStream4 = outputStream;
      inputStream1 = inputStream8;
      outputStream1 = outputStream;
      inputStream2 = this.postSocket.getInputStream();
      inputStream3 = inputStream2;
      outputStream2 = outputStream;
      inputStream4 = inputStream2;
      outputStream3 = outputStream;
      inputStream5 = inputStream2;
      outputStream4 = outputStream;
      inputStream1 = inputStream2;
      outputStream1 = outputStream;
      Trace.d(this.TAG, "Try 9");
      inputStream3 = inputStream2;
      outputStream2 = outputStream;
      inputStream4 = inputStream2;
      outputStream3 = outputStream;
      inputStream5 = inputStream2;
      outputStream4 = outputStream;
      inputStream1 = inputStream2;
      outputStream1 = outputStream;
      hTTPResponse.set(inputStream2, bool);
      inputStream3 = inputStream2;
      outputStream2 = outputStream;
      inputStream4 = inputStream2;
      outputStream3 = outputStream;
      inputStream5 = inputStream2;
      outputStream4 = outputStream;
      inputStream1 = inputStream2;
      outputStream1 = outputStream;
      Trace.d(this.TAG, "Try 10");
      return hTTPResponse;
    } catch (SocketException socketException) {
      inputStream1 = inputStream3;
      outputStream1 = outputStream2;
      Trace.d(this.TAG, "Res SocketException");
      inputStream1 = inputStream3;
      outputStream1 = outputStream2;
      hTTPResponse.setStatusCode(500);
      inputStream1 = inputStream3;
      outputStream1 = outputStream2;
      Debug.warning(socketException);
      return hTTPResponse;
    } catch (IOException iOException) {
      inputStream1 = inputStream4;
      outputStream1 = outputStream3;
      Trace.d(this.TAG, "Res IOException");
      inputStream1 = inputStream4;
      outputStream1 = outputStream3;
      hTTPResponse.setStatusCode(500);
      inputStream1 = inputStream4;
      outputStream1 = outputStream3;
      Debug.warning(iOException);
      return hTTPResponse;
    } catch (Exception exception) {
      inputStream1 = inputStream5;
      outputStream1 = outputStream4;
      Trace.d(this.TAG, "Res Exception");
      inputStream1 = inputStream5;
      outputStream1 = outputStream4;
      Debug.warning(exception);
      return hTTPResponse;
    } finally {
      if (!paramBoolean) {
        try {
          inputStream1.close();
        } catch (Exception exception) {}
        if (inputStream1 != null)
          try {
            outputStream1.close();
          } catch (Exception exception) {} 
        if (outputStream1 != null)
          try {
            this.postSocket.close();
          } catch (Exception exception) {} 
        this.postSocket = null;
      } 
    } 
  }
  
  public boolean post(HTTPResponse paramHTTPResponse) {
    HTTPSocket hTTPSocket = getSocket();
    long l2 = 0L;
    long l3 = paramHTTPResponse.getContentLength();
    long l1 = l3;
    if (hasContentRange()) {
      long l = getContentRangeFirstPosition();
      l2 = getContentRangeLastPosition();
      l1 = l2;
      if (l2 <= 0L)
        l1 = l3 - 1L; 
      if (l > l3 || l1 > l3)
        return returnResponse(416); 
      paramHTTPResponse.setContentRange(l, l1, l3);
      paramHTTPResponse.setStatusCode(206);
      l2 = l;
      l1 = l1 - l + 1L;
    } 
    return hTTPSocket.post(paramHTTPResponse, l2, l1, isHeadRequest());
  }
  
  public void print() {
    System.out.println(toString());
  }
  
  public boolean read() {
    return read(getSocket());
  }
  
  public boolean returnBadRequest() {
    return returnResponse(400);
  }
  
  public boolean returnOK() {
    return returnResponse(200);
  }
  
  public boolean returnResponse(int paramInt) {
    HTTPResponse hTTPResponse = new HTTPResponse();
    hTTPResponse.setStatusCode(paramInt);
    hTTPResponse.setContentLength(0L);
    return post(hTTPResponse);
  }
  
  public void set(HTTPRequest paramHTTPRequest) {
    set(paramHTTPRequest);
    setSocket(paramHTTPRequest.getSocket());
  }
  
  public void setMethod(String paramString) {
    this.method = paramString;
  }
  
  public void setRequestHost(String paramString) {
    this.requestHost = paramString;
  }
  
  public void setRequestPort(int paramInt) {
    this.requestPort = paramInt;
  }
  
  public void setSocket(HTTPSocket paramHTTPSocket) {
    this.httpSocket = paramHTTPSocket;
  }
  
  public void setURI(String paramString) {
    setURI(paramString, false);
  }
  
  public void setURI(String paramString, boolean paramBoolean) {
    this.uri = paramString;
    if (!paramBoolean)
      return; 
    this.uri = HTTP.toRelativeURL(this.uri);
  }
  
  public String toString() {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(getHeader());
    stringBuffer.append("\r\n");
    stringBuffer.append(getContentString());
    return stringBuffer.toString();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\http\HTTPRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */