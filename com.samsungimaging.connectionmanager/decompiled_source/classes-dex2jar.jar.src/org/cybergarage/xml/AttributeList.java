package org.cybergarage.xml;

import java.util.Vector;

public class AttributeList extends Vector {
  public Attribute getAttribute(int paramInt) {
    return (Attribute)get(paramInt);
  }
  
  public Attribute getAttribute(String paramString) {
    if (paramString == null)
      return null; 
    int j = size();
    int i = 0;
    while (true) {
      if (i >= j)
        return null; 
      Attribute attribute2 = getAttribute(i);
      Attribute attribute1 = attribute2;
      if (paramString.compareTo(attribute2.getName()) != 0) {
        i++;
        continue;
      } 
      return attribute1;
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\xml\AttributeList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */