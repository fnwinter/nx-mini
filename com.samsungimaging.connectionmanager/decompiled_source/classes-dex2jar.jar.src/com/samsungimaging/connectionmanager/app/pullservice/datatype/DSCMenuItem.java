package com.samsungimaging.connectionmanager.app.pullservice.datatype;

public class DSCMenuItem {
  private int id;
  
  private int imageResourceId;
  
  private boolean isSelected;
  
  private String name;
  
  private String value;
  
  public int getId() {
    return 0;
  }
  
  public int getImageResourceId() {
    return 0;
  }
  
  public String getName() {
    return this.name;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public boolean isSelected() {
    return this.isSelected;
  }
  
  public void setId(int paramInt) {}
  
  public void setImageResourceId(int paramInt) {}
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public void setSelected(boolean paramBoolean) {
    this.isSelected = paramBoolean;
  }
  
  public void setValue(String paramString) {
    this.value = paramString;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\datatype\DSCMenuItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */