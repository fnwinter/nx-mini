package org.cybergarage.upnp;

import java.util.Vector;

public class ServiceStateTable extends Vector {
  public static final String ELEM_NAME = "serviceStateTable";
  
  public StateVariable getStateVariable(int paramInt) {
    return (StateVariable)get(paramInt);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ServiceStateTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */