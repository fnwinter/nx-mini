package org.cybergarage.util;

import java.util.Vector;

public class ListenerList extends Vector {
  public boolean add(Object paramObject) {
    return (indexOf(paramObject) >= 0) ? false : super.add(paramObject);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\util\ListenerList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */