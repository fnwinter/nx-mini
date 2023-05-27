package org.cybergarage.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public final class FileUtil {
  public static final boolean isXMLFileName(String paramString) {
    return !StringUtil.hasData(paramString) ? false : paramString.toLowerCase().endsWith("xml");
  }
  
  public static final byte[] load(File paramFile) {
    try {
      return load(new FileInputStream(paramFile));
    } catch (Exception exception) {
      Debug.warning(exception);
      return new byte[0];
    } 
  }
  
  public static final byte[] load(FileInputStream paramFileInputStream) {
    byte[] arrayOfByte = new byte[524288];
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      for (int i = paramFileInputStream.read(arrayOfByte);; i = paramFileInputStream.read(arrayOfByte)) {
        if (i <= 0) {
          paramFileInputStream.close();
          return byteArrayOutputStream.toByteArray();
        } 
        byteArrayOutputStream.write(arrayOfByte, 0, i);
      } 
    } catch (Exception exception) {
      Debug.warning(exception);
      return new byte[0];
    } 
  }
  
  public static final byte[] load(String paramString) {
    try {
      return load(new FileInputStream(paramString));
    } catch (Exception exception) {
      Debug.warning(exception);
      return new byte[0];
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\util\FileUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */