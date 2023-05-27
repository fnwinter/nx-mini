package org.cybergarage.upnp.event;

import java.util.Vector;

public class PropertyList extends Vector {
  public static final String ELEM_NAME = "PropertyList";
  
  public Property getProperty(int paramInt) {
    return (Property)get(paramInt);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\event\PropertyList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */