package org.cybergarage.upnp;

import org.cybergarage.upnp.xml.ArgumentData;
import org.cybergarage.xml.Node;

public class Argument {
  private static final String DIRECTION = "direction";
  
  public static final String ELEM_NAME = "argument";
  
  public static final String IN = "in";
  
  private static final String NAME = "name";
  
  public static final String OUT = "out";
  
  private static final String RELATED_STATE_VARIABLE = "relatedStateVariable";
  
  private Node argumentNode;
  
  private Node serviceNode;
  
  private Object userData = null;
  
  public Argument() {
    this.argumentNode = new Node("argument");
    this.serviceNode = null;
  }
  
  public Argument(String paramString1, String paramString2) {
    this();
    setName(paramString1);
    setValue(paramString2);
  }
  
  public Argument(Node paramNode) {
    this.argumentNode = new Node("argument");
    this.serviceNode = paramNode;
  }
  
  public Argument(Node paramNode1, Node paramNode2) {
    this.serviceNode = paramNode1;
    this.argumentNode = paramNode2;
  }
  
  private ArgumentData getArgumentData() {
    Node node = getArgumentNode();
    ArgumentData argumentData2 = (ArgumentData)node.getUserData();
    ArgumentData argumentData1 = argumentData2;
    if (argumentData2 == null) {
      argumentData1 = new ArgumentData();
      node.setUserData(argumentData1);
      argumentData1.setNode(node);
    } 
    return argumentData1;
  }
  
  private Node getServiceNode() {
    return this.serviceNode;
  }
  
  public static boolean isArgumentNode(Node paramNode) {
    return "argument".equals(paramNode.getName());
  }
  
  public Action getAction() {
    return new Action(getServiceNode(), getActionNode());
  }
  
  public Node getActionNode() {
    Node node1 = getArgumentNode().getParentNode();
    if (node1 == null)
      return null; 
    Node node2 = node1.getParentNode();
    if (node2 == null)
      return null; 
    node1 = node2;
    return !Action.isActionNode(node2) ? null : node1;
  }
  
  public Node getArgumentNode() {
    return this.argumentNode;
  }
  
  public String getDirection() {
    return getArgumentNode().getNodeValue("direction");
  }
  
  public int getIntegerValue() {
    String str = getValue();
    try {
      return Integer.parseInt(str);
    } catch (Exception exception) {
      return 0;
    } 
  }
  
  public String getName() {
    return getArgumentNode().getNodeValue("name");
  }
  
  public StateVariable getRelatedStateVariable() {
    Service service = getService();
    return (service == null) ? null : service.getStateVariable(getRelatedStateVariableName());
  }
  
  public String getRelatedStateVariableName() {
    return getArgumentNode().getNodeValue("relatedStateVariable");
  }
  
  public Service getService() {
    return new Service(getServiceNode());
  }
  
  public Object getUserData() {
    return this.userData;
  }
  
  public String getValue() {
    return getArgumentData().getValue();
  }
  
  public boolean isInDirection() {
    String str = getDirection();
    return (str == null) ? false : str.equalsIgnoreCase("in");
  }
  
  public boolean isOutDirection() {
    return !isInDirection();
  }
  
  public void setDirection(String paramString) {
    getArgumentNode().setNode("direction", paramString);
  }
  
  public void setName(String paramString) {
    getArgumentNode().setNode("name", paramString);
  }
  
  public void setRelatedStateVariableName(String paramString) {
    getArgumentNode().setNode("relatedStateVariable", paramString);
  }
  
  void setService(Service paramService) {
    paramService.getServiceNode();
  }
  
  public void setUserData(Object paramObject) {
    this.userData = paramObject;
  }
  
  public void setValue(int paramInt) {
    setValue(Integer.toString(paramInt));
  }
  
  public void setValue(String paramString) {
    getArgumentData().setValue(paramString);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\Argument.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */