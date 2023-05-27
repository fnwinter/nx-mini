package org.cybergarage.upnp;

import java.util.Vector;

public class ActionList extends Vector {
  public static final String ELEM_NAME = "actionList";
  
  public Action getAction(int paramInt) {
    return (Action)get(paramInt);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ActionList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */