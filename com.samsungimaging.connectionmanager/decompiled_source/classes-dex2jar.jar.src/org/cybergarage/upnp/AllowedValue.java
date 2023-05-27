package org.cybergarage.upnp;

import org.cybergarage.xml.Node;

public class AllowedValue {
  public static final String ELEM_NAME = "allowedValue";
  
  private Node allowedValueNode = new Node("allowedValue");
  
  public AllowedValue(String paramString) {
    setValue(paramString);
  }
  
  public AllowedValue(Node paramNode) {}
  
  public static boolean isAllowedValueNode(Node paramNode) {
    return "allowedValue".equals(paramNode.getName());
  }
  
  public Node getAllowedValueNode() {
    return this.allowedValueNode;
  }
  
  public String getValue() {
    return getAllowedValueNode().getValue();
  }
  
  public void setValue(String paramString) {
    getAllowedValueNode().setValue(paramString);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\AllowedValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */