package com.samsungimaging.connectionmanager.app.pullservice.demo.rvf;

public class Menu {
  private String Name;
  
  private int iconResourceId;
  
  private boolean isSelected;
  
  public Menu(String paramString) {
    this.Name = paramString;
  }
  
  public int getIconResourceId() {
    return this.iconResourceId;
  }
  
  public String getName() {
    return this.Name;
  }
  
  public boolean isSelected() {
    return this.isSelected;
  }
  
  public void setIconResourceId(int paramInt) {
    this.iconResourceId = paramInt;
  }
  
  public void setName(String paramString) {
    this.Name = paramString;
  }
  
  public void setSelected(boolean paramBoolean) {
    this.isSelected = paramBoolean;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\rvf\Menu.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */