package org.cybergarage.upnp.control;

import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.util.ThreadCore;

public class RenewSubscriber extends ThreadCore {
  public static final long INTERVAL = 900L;
  
  private ControlPoint ctrlPoint;
  
  public RenewSubscriber(ControlPoint paramControlPoint) {
    setControlPoint(paramControlPoint);
  }
  
  public ControlPoint getControlPoint() {
    return this.ctrlPoint;
  }
  
  public void run() {
    ControlPoint controlPoint = getControlPoint();
    while (true) {
      if (!isRunnable())
        return; 
      try {
        Thread.sleep(900000L);
      } catch (InterruptedException interruptedException) {}
      controlPoint.renewSubscriberService();
    } 
  }
  
  public void setControlPoint(ControlPoint paramControlPoint) {
    this.ctrlPoint = paramControlPoint;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\control\RenewSubscriber.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */