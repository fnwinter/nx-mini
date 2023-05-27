package org.cybergarage.http;

import android.util.Log;
import java.util.StringTokenizer;
import org.cybergarage.util.Debug;

public class HTTPStatus {
  public static final int BAD_REQUEST = 400;
  
  public static final int CONTINUE = 100;
  
  public static final int INTERNAL_SERVER_ERROR = 500;
  
  public static final int INVALID_RANGE = 416;
  
  public static final int NOT_FOUND = 404;
  
  public static final int OK = 200;
  
  public static final int PARTIAL_CONTENT = 206;
  
  public static final int PRECONDITION_FAILED = 412;
  
  private String reasonPhrase = "";
  
  private int statusCode = 0;
  
  private String version = "";
  
  public HTTPStatus() {
    setVersion("");
    setStatusCode(0);
    setReasonPhrase("");
  }
  
  public HTTPStatus(String paramString) {
    set(paramString);
  }
  
  public HTTPStatus(String paramString1, int paramInt, String paramString2) {
    setVersion(paramString1);
    setStatusCode(paramInt);
    setReasonPhrase(paramString2);
  }
  
  public static final String code2String(int paramInt) {
    switch (paramInt) {
      default:
        return "";
      case 100:
        return "Continue";
      case 200:
        return "OK";
      case 206:
        return "Partial Content";
      case 400:
        return "Bad Request";
      case 404:
        return "Not Found";
      case 412:
        return "Precondition Failed";
      case 416:
        return "Invalid Range";
      case 500:
        break;
    } 
    return "Internal Server Error";
  }
  
  public static final boolean isSuccessful(int paramInt) {
    return (200 <= paramInt && paramInt < 300);
  }
  
  public String getReasonPhrase() {
    return this.reasonPhrase;
  }
  
  public int getStatusCode() {
    return this.statusCode;
  }
  
  public String getVersion() {
    return this.version;
  }
  
  public boolean isSuccessful() {
    return isSuccessful(getStatusCode());
  }
  
  public void set(String paramString) {
    if (paramString == null) {
      setVersion("1.1");
      setStatusCode(500);
      setReasonPhrase(code2String(500));
      return;
    } 
    try {
      StringTokenizer stringTokenizer = new StringTokenizer(paramString, " ");
      if (stringTokenizer.hasMoreTokens()) {
        setVersion(stringTokenizer.nextToken().trim());
        if (stringTokenizer.hasMoreTokens()) {
          paramString = stringTokenizer.nextToken();
          int i = 0;
          try {
            int j = Integer.parseInt(paramString);
            i = j;
            Log.d("RVF", "[HTTPStatus] code 1 : " + j);
            i = j;
          } catch (Exception exception) {}
          setStatusCode(i);
          Log.d("RVF", "[HTTPStatus] code 2 : " + i);
          for (paramString = "";; paramString = String.valueOf(str) + stringTokenizer.nextToken()) {
            if (!stringTokenizer.hasMoreTokens()) {
              setReasonPhrase(paramString.trim());
              return;
            } 
            String str = paramString;
            if (paramString.length() >= 0)
              str = String.valueOf(paramString) + " "; 
          } 
        } 
      } 
      return;
    } catch (Exception exception) {
      Debug.warning(exception);
      return;
    } 
  }
  
  public void setReasonPhrase(String paramString) {
    this.reasonPhrase = paramString;
  }
  
  public void setStatusCode(int paramInt) {
    this.statusCode = paramInt;
  }
  
  public void setVersion(String paramString) {
    this.version = paramString;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\http\HTTPStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */