package org.cybergarage.http;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import org.cybergarage.util.Debug;

public class HTTPHeader {
  private static int MAX_LENGTH = 1024;
  
  private String name;
  
  private String value;
  
  public HTTPHeader(String paramString) {
    setName("");
    setValue("");
    if (paramString != null) {
      int i = paramString.indexOf(':');
      if (i >= 0) {
        String str = new String(paramString.getBytes(), 0, i);
        paramString = new String(paramString.getBytes(), i + 1, paramString.length() - i - 1);
        setName(str.trim());
        setValue(paramString.trim());
        return;
      } 
    } 
  }
  
  public HTTPHeader(String paramString1, String paramString2) {
    setName(paramString1);
    setValue(paramString2);
  }
  
  public static final int getIntegerValue(String paramString1, String paramString2) {
    try {
      return Integer.parseInt(getValue(paramString1, paramString2));
    } catch (Exception exception) {
      return 0;
    } 
  }
  
  public static final int getIntegerValue(byte[] paramArrayOfbyte, String paramString) {
    try {
      return Integer.parseInt(getValue(paramArrayOfbyte, paramString));
    } catch (Exception exception) {
      return 0;
    } 
  }
  
  public static final String getValue(LineNumberReader paramLineNumberReader, String paramString) {
    String str = paramString.toUpperCase();
    try {
      paramString = paramLineNumberReader.readLine();
      while (true) {
        if (paramString != null) {
          int i = paramString.length();
          if (i > 0) {
            String str1;
            HTTPHeader hTTPHeader = new HTTPHeader(paramString);
            if (!hTTPHeader.hasName()) {
              str1 = paramLineNumberReader.readLine();
              continue;
            } 
            if (!str1.getName().toUpperCase().equals(str)) {
              str1 = paramLineNumberReader.readLine();
              continue;
            } 
            return str1.getValue();
          } 
        } 
        return "";
      } 
    } catch (IOException iOException) {
      Debug.warning(iOException);
      return "";
    } 
  }
  
  public static final String getValue(String paramString1, String paramString2) {
    return getValue(new LineNumberReader(new StringReader(paramString1), Math.min(paramString1.length(), MAX_LENGTH)), paramString2);
  }
  
  public static final String getValue(byte[] paramArrayOfbyte, String paramString) {
    return getValue(new String(paramArrayOfbyte), paramString);
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public boolean hasName() {
    return !(this.name == null || this.name.length() <= 0);
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public void setValue(String paramString) {
    this.value = paramString;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\http\HTTPHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */