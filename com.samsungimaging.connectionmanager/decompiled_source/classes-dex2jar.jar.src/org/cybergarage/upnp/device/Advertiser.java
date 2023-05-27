package org.cybergarage.upnp.device;

import org.cybergarage.upnp.Device;
import org.cybergarage.util.ThreadCore;

public class Advertiser extends ThreadCore {
  private Device device;
  
  public Advertiser(Device paramDevice) {
    setDevice(paramDevice);
  }
  
  public Device getDevice() {
    return this.device;
  }
  
  public void run() {
    Device device = getDevice();
    long l = device.getLeaseTime();
    while (true) {
      if (!isRunnable())
        return; 
      long l1 = l / 4L;
      long l2 = (long)((float)l * Math.random() * 0.25D);
      try {
        Thread.sleep((l1 + l2) * 1000L);
      } catch (InterruptedException interruptedException) {}
      device.announce();
    } 
  }
  
  public void setDevice(Device paramDevice) {
    this.device = paramDevice;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\device\Advertiser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */