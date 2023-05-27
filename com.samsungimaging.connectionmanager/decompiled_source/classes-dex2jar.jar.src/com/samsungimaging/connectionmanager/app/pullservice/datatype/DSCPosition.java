package com.samsungimaging.connectionmanager.app.pullservice.datatype;

public class DSCPosition {
  private int x;
  
  private int y;
  
  public DSCPosition(int paramInt1, int paramInt2) {
    this.x = paramInt1;
    this.y = paramInt2;
  }
  
  public int getX() {
    return this.x;
  }
  
  public int getY() {
    return this.y;
  }
  
  public void setX(int paramInt) {
    this.x = paramInt;
  }
  
  public void setY(int paramInt) {
    this.y = paramInt;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\datatype\DSCPosition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */