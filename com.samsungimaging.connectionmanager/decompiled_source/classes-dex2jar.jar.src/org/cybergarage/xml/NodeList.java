package org.cybergarage.xml;

import java.util.Vector;

public class NodeList extends Vector {
  public Node getEndsWith(String paramString) {
    if (paramString == null)
      return null; 
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return null; 
      Node node = getNode(i);
      String str = node.getName();
      if (str != null && str.endsWith(paramString))
        return node; 
    } 
  }
  
  public Node getNode(int paramInt) {
    return (Node)get(paramInt);
  }
  
  public Node getNode(String paramString) {
    if (paramString == null)
      return null; 
    int j = size();
    int i = 0;
    while (true) {
      if (i >= j)
        return null; 
      Node node2 = getNode(i);
      Node node1 = node2;
      if (paramString.compareTo(node2.getName()) != 0) {
        i++;
        continue;
      } 
      return node1;
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\xml\NodeList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */