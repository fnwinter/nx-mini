package org.cybergarage.upnp;

import java.util.Vector;

public class DeviceList extends Vector {
  public static final String ELEM_NAME = "deviceList";
  
  public Device getDevice(int paramInt) {
    return (Device)get(paramInt);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\DeviceList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */