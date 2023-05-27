package org.cybergarage.upnp.device;

import java.io.File;

public class InvalidDescriptionException extends Exception {
  public InvalidDescriptionException() {}
  
  public InvalidDescriptionException(Exception paramException) {
    super(paramException.getMessage());
  }
  
  public InvalidDescriptionException(String paramString) {
    super(paramString);
  }
  
  public InvalidDescriptionException(String paramString, File paramFile) {
    super(String.valueOf(paramString) + " (" + paramFile.toString() + ")");
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\device\InvalidDescriptionException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */