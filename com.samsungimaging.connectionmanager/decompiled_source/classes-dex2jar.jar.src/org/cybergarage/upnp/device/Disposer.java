package org.cybergarage.upnp.device;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.util.ThreadCore;

public class Disposer extends ThreadCore {
  private ControlPoint ctrlPoint;
  
  public Disposer(ControlPoint paramControlPoint) {
    setControlPoint(paramControlPoint);
  }
  
  public ControlPoint getControlPoint() {
    return this.ctrlPoint;
  }
  
  public void run() {
    ControlPoint controlPoint = getControlPoint();
    long l = controlPoint.getExpiredDeviceMonitoringInterval();
    while (true) {
      if (!isRunnable())
        return; 
      try {
        Thread.sleep(l * 1000L);
      } catch (InterruptedException interruptedException) {}
      controlPoint.removeExpiredDevices();
    } 
  }
  
  public void setControlPoint(ControlPoint paramControlPoint) {
    this.ctrlPoint = paramControlPoint;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\device\Disposer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */