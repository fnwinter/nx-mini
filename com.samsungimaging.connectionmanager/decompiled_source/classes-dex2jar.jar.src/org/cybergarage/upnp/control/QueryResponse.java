package org.cybergarage.upnp.control;

import org.cybergarage.soap.SOAPResponse;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.xml.Node;

public class QueryResponse extends ControlResponse {
  public QueryResponse() {}
  
  public QueryResponse(SOAPResponse paramSOAPResponse) {
    super(paramSOAPResponse);
  }
  
  private Node createResponseNode(String paramString) {
    Node node1 = new Node();
    node1.setName("u", "QueryStateVariableResponse");
    node1.setNameSpace("u", "urn:schemas-upnp-org:control-1-0");
    Node node2 = new Node();
    node2.setName("return");
    node2.setValue(paramString);
    node1.addNode(node2);
    return node1;
  }
  
  private Node getReturnNode() {
    Node node = getBodyNode();
    if (node != null && node.hasNodes()) {
      node = node.getNode(0);
      if (node != null && node.hasNodes())
        return node.getNode(0); 
    } 
    return null;
  }
  
  public String getReturnValue() {
    Node node = getReturnNode();
    return (node == null) ? "" : node.getValue();
  }
  
  public void setResponse(StateVariable paramStateVariable) {
    String str = paramStateVariable.getValue();
    setStatusCode(200);
    getBodyNode().addNode(createResponseNode(str));
    setContent(getEnvelopeNode());
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\control\QueryResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */