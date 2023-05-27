package org.cybergarage.upnp.event;

import java.util.Vector;

public class SubscriberList extends Vector {
  public Subscriber getSubscriber(int paramInt) {
    E e;
    Object object = null;
    try {
      E e1 = get(paramInt);
      e = e1;
    } catch (Exception exception) {}
    return (Subscriber)e;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\event\SubscriberList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */