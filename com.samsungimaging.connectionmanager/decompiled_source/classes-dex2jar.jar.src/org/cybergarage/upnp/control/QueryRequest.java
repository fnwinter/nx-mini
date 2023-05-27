package org.cybergarage.upnp.control;

import org.cybergarage.http.HTTPRequest;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.xml.Node;

public class QueryRequest extends ControlRequest {
  public QueryRequest() {}
  
  public QueryRequest(HTTPRequest paramHTTPRequest) {
    set(paramHTTPRequest);
  }
  
  private Node createContentNode(StateVariable paramStateVariable) {
    Node node1 = new Node();
    node1.setName("u", "QueryStateVariable");
    node1.setNameSpace("u", "urn:schemas-upnp-org:control-1-0");
    Node node2 = new Node();
    node2.setName("u", "varName");
    node2.setValue(paramStateVariable.getName());
    node1.addNode(node2);
    return node1;
  }
  
  private Node getVarNameNode() {
    Node node = getBodyNode();
    if (node != null && node.hasNodes()) {
      node = node.getNode(0);
      if (node != null && node.hasNodes())
        return node.getNode(0); 
    } 
    return null;
  }
  
  public String getVarName() {
    Node node = getVarNameNode();
    return (node == null) ? "" : node.getValue();
  }
  
  public QueryResponse post() {
    return new QueryResponse(postMessage(getRequestHost(), getRequestPort()));
  }
  
  public void setRequest(StateVariable paramStateVariable) {
    Service service = paramStateVariable.getService();
    service.getControlURL();
    setRequestHost(service);
    setEnvelopeNode(SOAP.createEnvelopeBodyNode());
    Node node = getEnvelopeNode();
    getBodyNode().addNode(createContentNode(paramStateVariable));
    setContent(node);
    setSOAPAction("urn:schemas-upnp-org:control-1-0#QueryStateVariable");
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\control\QueryRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */