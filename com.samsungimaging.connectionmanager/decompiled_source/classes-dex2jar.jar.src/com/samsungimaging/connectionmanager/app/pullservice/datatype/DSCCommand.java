package com.samsungimaging.connectionmanager.app.pullservice.datatype;

public class DSCCommand {
  private int commandID;
  
  private String param;
  
  public DSCCommand(int paramInt, String paramString) {
    this.commandID = paramInt;
    this.param = paramString;
  }
  
  public int getcommandID() {
    return this.commandID;
  }
  
  public String getparam() {
    return this.param;
  }
  
  public void setcommandID(int paramInt) {
    this.commandID = paramInt;
  }
  
  public void setparam(String paramString) {
    this.param = paramString;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\datatype\DSCCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */