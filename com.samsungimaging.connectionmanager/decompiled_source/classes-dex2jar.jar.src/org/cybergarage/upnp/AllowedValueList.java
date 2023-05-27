package org.cybergarage.upnp;

import java.util.Iterator;
import java.util.Vector;

public class AllowedValueList extends Vector {
  public static final String ELEM_NAME = "allowedValueList";
  
  public AllowedValueList() {}
  
  public AllowedValueList(String[] paramArrayOfString) {
    for (int i = 0;; i++) {
      if (i >= paramArrayOfString.length)
        return; 
      add((E)new AllowedValue(paramArrayOfString[i]));
    } 
  }
  
  public AllowedValue getAllowedValue(int paramInt) {
    return (AllowedValue)get(paramInt);
  }
  
  public boolean isAllowed(String paramString) {
    Iterator<E> iterator = iterator();
    while (true) {
      if (!iterator.hasNext())
        return false; 
      if (((AllowedValue)iterator.next()).getValue().equals(paramString))
        return true; 
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\AllowedValueList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */