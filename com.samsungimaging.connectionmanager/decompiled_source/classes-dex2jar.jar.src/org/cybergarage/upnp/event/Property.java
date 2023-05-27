package org.cybergarage.upnp.event;

public class Property {
  private String name = "";
  
  private String value = "";
  
  public String getName() {
    return this.name;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public void setName(String paramString) {
    String str = paramString;
    if (paramString == null)
      str = ""; 
    this.name = str;
  }
  
  public void setValue(String paramString) {
    String str = paramString;
    if (paramString == null)
      str = ""; 
    this.value = str;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\event\Property.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */