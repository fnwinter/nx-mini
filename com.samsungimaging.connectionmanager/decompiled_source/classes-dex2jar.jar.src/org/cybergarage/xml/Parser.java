package org.cybergarage.xml;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.http.HTTPResponse;

public abstract class Parser {
  public Node parse(File paramFile) throws ParserException {
    try {
      FileInputStream fileInputStream = new FileInputStream(paramFile);
      Node node = parse(fileInputStream);
      fileInputStream.close();
      return node;
    } catch (Exception exception) {
      throw new ParserException(exception);
    } 
  }
  
  public abstract Node parse(InputStream paramInputStream) throws ParserException;
  
  public Node parse(String paramString) throws ParserException {
    try {
      return parse(new ByteArrayInputStream(paramString.getBytes()));
    } catch (Exception exception) {
      throw new ParserException(exception);
    } 
  }
  
  public Node parse(URL paramURL, String paramString1, String paramString2) throws ParserException {
    HTTPResponse hTTPResponse;
    String str1 = paramURL.getHost();
    int i = paramURL.getPort();
    String str2 = paramURL.getPath();
    try {
      HTTPRequest hTTPRequest = new HTTPRequest();
      hTTPRequest.setMethod("GET");
      hTTPRequest.setURI(str2);
      if (paramString1 != null)
        hTTPRequest.addHeader("User-Agent", paramString1); 
      if (paramString2 != null)
        hTTPRequest.addHeader("Access-Method", paramString2); 
      hTTPResponse = hTTPRequest.post(str1, i);
      if (paramString2 != null)
        switch (hTTPResponse.getStatusCode()) {
          case 401:
            throw new ParserException("HTTP_UNAUTHORIZED");
          case 500:
            throw new ParserException("HTTP_INTERNAL_ERROR");
          case 501:
            throw new ParserException("HTTP_NOT_IMPLEMENTED");
          case 502:
            throw new ParserException("HTTP_BAD_GATEWAY");
          case 503:
            throw new ParserException("HTTP_UNAVAILABLE");
          case 504:
            throw new ParserException("HTTP_GATEWAY_TIMEOUT");
          case 505:
            throw new ParserException("HTTP_VERSION");
        }  
      if (!hTTPResponse.isSuccessful())
        throw new ParserException("HTTP comunication failed: no answer from peer.Unable to retrive resoure -> " + paramURL.toString()); 
    } catch (ParserException parserException) {
      throw new ParserException(parserException);
    } catch (Exception exception) {
      try {
        HttpURLConnection httpURLConnection = (HttpURLConnection)parserException.openConnection();
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Content-Length", "0");
        if (str1 != null)
          httpURLConnection.setRequestProperty("HOST", str1); 
        if (paramString1 != null)
          httpURLConnection.setRequestProperty("User-Agent", paramString1); 
        InputStream inputStream = httpURLConnection.getInputStream();
        Node node = parse(inputStream);
        inputStream.close();
        httpURLConnection.disconnect();
        return node;
      } catch (Exception exception1) {
        return null;
      } 
    } 
    return parse(new ByteArrayInputStream((new String(hTTPResponse.getContent())).getBytes()));
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\xml\Parser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */