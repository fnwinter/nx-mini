package com.samsungimaging.connectionmanager.app.pullservice.demo.ml;

import com.samsungimaging.connectionmanager.util.Trace;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HumbleHttpClient {
  private Trace.Tag TAG = Trace.Tag.ML;
  
  protected BufferedReader br = null;
  
  public long contentLength = -1L;
  
  public String contentType = null;
  
  public Map<String, String> headers = null;
  
  protected InputStream is = null;
  
  protected PrintWriter pw = null;
  
  protected Socket sock = null;
  
  private void close() {
    if (this.is != null)
      try {
        this.is.close();
      } catch (Throwable throwable) {} 
    if (this.pw != null)
      try {
        this.pw.close();
      } catch (Throwable throwable) {} 
    if (this.br != null)
      try {
        this.br.close();
      } catch (Throwable throwable) {} 
    if (this.sock != null)
      try {
        this.sock.close();
      } catch (Throwable throwable) {} 
    this.is = null;
    this.pw = null;
    this.br = null;
    this.sock = null;
    this.contentLength = -1L;
    this.contentType = null;
  }
  
  public long getContentLength() {
    return this.contentLength;
  }
  
  public InputStream request(String paramString) {
    int i = 0;
    try {
      URL uRL = new URL(paramString);
      try {
        this.sock = new Socket(uRL.getHost(), uRL.getPort());
        this.pw = new PrintWriter(this.sock.getOutputStream(), false);
        this.is = this.sock.getInputStream();
        Trace.d(this.TAG, "\nGET " + uRL.getPath() + " HTTP/1.0\r\n\r\n\r\n");
        this.pw.print("GET " + uRL.getPath() + " HTTP/1.0\r\n\r\n\r\n\r\n");
        this.pw.flush();
        this.headers = Collections.synchronizedMap(new HashMap<String, String>());
        this.br = new BufferedReader(new InputStreamReader(this.is));
        while (true) {
          String str = this.br.readLine();
          if (str == null || str.length() <= 0) {
            Trace.d(this.TAG, "Returning remained data...");
            return this.sock.getInputStream();
          } 
          Trace.d(this.TAG, str);
          if (i) {
            String[] arrayOfString = str.split(":", 2);
            if (arrayOfString.length == 2) {
              str = arrayOfString[0].trim();
              String str1 = arrayOfString[1].trim();
              this.headers.put(str, str1);
              if (str.equalsIgnoreCase("Content-Type")) {
                this.contentType = str1;
              } else {
                boolean bool = str.equalsIgnoreCase("Content-Length");
                if (bool)
                  try {
                    this.contentLength = Long.parseLong(str1);
                  } catch (NumberFormatException numberFormatException) {
                    numberFormatException.printStackTrace();
                  }  
              } 
            } 
          } 
          i++;
        } 
      } catch (MalformedURLException null) {
        iOException.printStackTrace();
        close();
        return null;
      } catch (IOException null) {}
      iOException.printStackTrace();
      close();
      return null;
    } catch (MalformedURLException null) {
    
    } catch (IOException iOException) {
      iOException.printStackTrace();
      close();
      return null;
    } 
    iOException.printStackTrace();
    close();
    return null;
  }
  
  public InputStream request_Resize(String paramString) {
    int i = 0;
    try {
      URL uRL = new URL(paramString);
      try {
        this.sock = new Socket(uRL.getHost(), uRL.getPort());
        this.pw = new PrintWriter(this.sock.getOutputStream(), false);
        this.is = this.sock.getInputStream();
        Trace.d(this.TAG, "\nGET " + uRL.getPath() + " HTTP/1.0\r\n\r\n\r\n");
        this.pw.print("GET " + uRL.getPath() + " HTTP/1.0\r\n\r\n\r\n\r\n");
        this.pw.flush();
        this.headers = Collections.synchronizedMap(new HashMap<String, String>());
        this.br = new BufferedReader(new InputStreamReader(this.is));
        while (true) {
          String str = this.br.readLine();
          if (str == null || str.length() <= 0) {
            Trace.d(this.TAG, "Returning remained data...");
            return this.sock.getInputStream();
          } 
          Trace.d(this.TAG, str);
          if (i) {
            String[] arrayOfString = str.split(":", 2);
            if (arrayOfString.length == 2) {
              str = arrayOfString[0].trim();
              String str1 = arrayOfString[1].trim();
              this.headers.put(str, str1);
              if (str.equalsIgnoreCase("Content-Type")) {
                this.contentType = str1;
              } else {
                boolean bool = str.equalsIgnoreCase("Content-Length");
                if (bool)
                  try {
                    this.contentLength = Long.parseLong(str1);
                    Trace.d(this.TAG, "contentLength : " + this.contentLength);
                  } catch (NumberFormatException numberFormatException) {
                    numberFormatException.printStackTrace();
                  }  
              } 
            } 
          } 
          i++;
        } 
      } catch (MalformedURLException null) {
        iOException.printStackTrace();
        close();
        return null;
      } catch (IOException null) {}
      iOException.printStackTrace();
      close();
      return null;
    } catch (MalformedURLException null) {
    
    } catch (IOException iOException) {
      iOException.printStackTrace();
      close();
      return null;
    } 
    iOException.printStackTrace();
    close();
    return null;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\ml\HumbleHttpClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */