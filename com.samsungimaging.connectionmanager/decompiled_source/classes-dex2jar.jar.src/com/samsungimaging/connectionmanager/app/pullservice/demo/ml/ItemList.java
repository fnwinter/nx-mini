package com.samsungimaging.connectionmanager.app.pullservice.demo.ml;

import java.util.Vector;

public class ItemList extends Vector {
  public Item getItem(int paramInt) {
    return (Item)get(paramInt);
  }
  
  public Item getItem(String paramString) {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return null; 
      Item item = getItem(i);
      String str = item.getTitle();
      if (str != null && str.equals(paramString))
        return item; 
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\ml\ItemList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */