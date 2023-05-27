package org.cybergarage.http;

import java.net.URL;

public class HTTP {
  public static final String ACCESS_METHOD = "Access-Method";
  
  public static final String CACHE_CONTROL = "Cache-Control";
  
  public static final String CALLBACK = "CALLBACK";
  
  public static final String CHARSET = "charset";
  
  public static final String CHUNKED = "Chunked";
  
  public static final String CLOSE = "close";
  
  public static final String CONNECTION = "Connection";
  
  public static final String CONTENT_LENGTH = "Content-Length";
  
  public static final String CONTENT_RANGE = "Content-Range";
  
  public static final String CONTENT_RANGE_BYTES = "bytes";
  
  public static final String CONTENT_TYPE = "Content-Type";
  
  public static final byte CR = 13;
  
  public static final String CRLF = "\r\n";
  
  public static final String DATE = "Date";
  
  public static final int DEFAULT_CHUNK_SIZE = 524288;
  
  public static final int DEFAULT_PORT = 80;
  
  public static final int DEFAULT_TIMEOUT = 30;
  
  public static final String EXT = "EXT";
  
  public static final String GET = "GET";
  
  public static final String HEAD = "HEAD";
  
  public static final String HEADER_LINE_DELIM = " :";
  
  public static final String HOST = "HOST";
  
  public static final String KEEP_ALIVE = "Keep-Alive";
  
  public static final byte LF = 10;
  
  public static final String LOCATION = "LOCATION";
  
  public static final String MAN = "MAN";
  
  public static final String MAX_AGE = "max-age";
  
  public static final String MX = "MX";
  
  public static final String MYNAME = "MYNAME";
  
  public static final String M_SEARCH = "M-SEARCH";
  
  public static final String NOTIFY = "NOTIFY";
  
  public static final String NO_CACHE = "no-cache";
  
  public static final String NT = "NT";
  
  public static final String NTS = "NTS";
  
  public static final String POST = "POST";
  
  public static final String RANGE = "Range";
  
  public static final String REQEST_LINE_DELIM = " ";
  
  public static final String SEQ = "SEQ";
  
  public static final String SERVER = "Server";
  
  public static final String SID = "SID";
  
  public static final String SOAP_ACTION = "SOAPACTION";
  
  public static final String ST = "ST";
  
  public static final String STATUS_LINE_DELIM = " ";
  
  public static final String SUBSCRIBE = "SUBSCRIBE";
  
  public static final String TAB = "\t";
  
  public static final String TIMEOUT = "TIMEOUT";
  
  public static final String TRANSFER_ENCODING = "Transfer-Encoding";
  
  public static final String UNSUBSCRIBE = "UNSUBSCRIBE";
  
  public static final String USER_AGENT = "User-Agent";
  
  public static final String USN = "USN";
  
  public static final String VERSION = "1.1";
  
  public static final String VERSION_10 = "1.0";
  
  public static final String VERSION_11 = "1.1";
  
  private static int chunkSize = 524288;
  
  public static final String getAbsoluteURL(String paramString1, String paramString2) {
    try {
      URL uRL = new URL(paramString1);
      return String.valueOf(uRL.getProtocol()) + "://" + uRL.getHost() + ":" + uRL.getPort() + toRelativeURL(paramString2);
    } catch (Exception exception) {
      return "";
    } 
  }
  
  public static final int getChunkSize() {
    return chunkSize;
  }
  
  public static final String getHost(String paramString) {
    try {
      return (new URL(paramString)).getHost();
    } catch (Exception exception) {
      return "";
    } 
  }
  
  public static final int getPort(String paramString) {
    try {
      int i = (new URL(paramString)).getPort();
      int j = i;
      if (i <= 0)
        j = 80; 
      return j;
    } catch (Exception exception) {
      return 80;
    } 
  }
  
  public static final String getRequestHostURL(String paramString, int paramInt) {
    return "http://" + paramString + ":" + paramInt;
  }
  
  public static final boolean isAbsoluteURL(String paramString) {
    try {
      new URL(paramString);
      return true;
    } catch (Exception exception) {
      return false;
    } 
  }
  
  public static final void setChunkSize(int paramInt) {
    chunkSize = paramInt;
  }
  
  public static final String toRelativeURL(String paramString) {
    return toRelativeURL(paramString, true);
  }
  
  public static final String toRelativeURL(String paramString, boolean paramBoolean) {
    String str3;
    String str2 = paramString;
    if (!isAbsoluteURL(paramString)) {
      str3 = str2;
      if (paramString.length() > 0) {
        str3 = str2;
        if (paramString.charAt(0) != '/')
          str3 = "/" + paramString; 
      } 
      return str3;
    } 
    String str1 = str2;
    try {
      URL uRL = new URL(paramString);
      str1 = str2;
      str2 = uRL.getPath();
      paramString = str2;
      if (paramBoolean) {
        str1 = str2;
        String str = uRL.getQuery();
        paramString = str2;
        str1 = str2;
        if (!str.equals("")) {
          str1 = str2;
          paramString = String.valueOf(str2) + "?" + str;
        } 
      } 
      str3 = paramString;
      str1 = paramString;
      if (paramString.endsWith("/")) {
        str1 = paramString;
        return paramString.substring(0, paramString.length() - 1);
      } 
    } catch (Exception exception) {
      return str1;
    } 
    return str3;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\http\HTTP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */