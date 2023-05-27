package org.cybergarage.upnp;

import java.util.Vector;

public class ServiceList extends Vector {
  public static final String ELEM_NAME = "serviceList";
  
  public Service getService(int paramInt) {
    E e;
    Object object = null;
    try {
      E e1 = get(paramInt);
      e = e1;
    } catch (Exception exception) {}
    return (Service)e;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ServiceList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */