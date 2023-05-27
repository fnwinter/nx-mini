package org.cybergarage.soap;

import org.cybergarage.http.HTTPResponse;
import org.cybergarage.util.Debug;
import org.cybergarage.xml.Node;

public class SOAPResponse extends HTTPResponse {
  private Node rootNode;
  
  public SOAPResponse() {
    setRootNode(SOAP.createEnvelopeBodyNode());
    setContentType("text/xml; charset=\"utf-8\"");
  }
  
  public SOAPResponse(HTTPResponse paramHTTPResponse) {
    super(paramHTTPResponse);
    setRootNode(SOAP.createEnvelopeBodyNode());
    setContentType("text/xml; charset=\"utf-8\"");
  }
  
  public SOAPResponse(SOAPResponse paramSOAPResponse) {
    super(paramSOAPResponse);
    setEnvelopeNode(paramSOAPResponse.getEnvelopeNode());
    setContentType("text/xml; charset=\"utf-8\"");
  }
  
  private Node getRootNode() {
    return this.rootNode;
  }
  
  private void setRootNode(Node paramNode) {
    this.rootNode = paramNode;
  }
  
  public Node getBodyNode() {
    Node node = getEnvelopeNode();
    return (node == null) ? null : node.getNodeEndsWith("Body");
  }
  
  public Node getEnvelopeNode() {
    return getRootNode();
  }
  
  public String getFaultActor() {
    Node node = getFaultActorNode();
    return (node == null) ? "" : node.getValue();
  }
  
  public Node getFaultActorNode() {
    Node node = getFaultNode();
    return (node == null) ? null : node.getNodeEndsWith("faultactor");
  }
  
  public String getFaultCode() {
    Node node = getFaultCodeNode();
    return (node == null) ? "" : node.getValue();
  }
  
  public Node getFaultCodeNode() {
    Node node = getFaultNode();
    return (node == null) ? null : node.getNodeEndsWith("faultcode");
  }
  
  public Node getFaultDetailNode() {
    Node node = getFaultNode();
    return (node == null) ? null : node.getNodeEndsWith("detail");
  }
  
  public Node getFaultNode() {
    Node node = getBodyNode();
    return (node == null) ? null : node.getNodeEndsWith("Fault");
  }
  
  public String getFaultString() {
    Node node = getFaultStringNode();
    return (node == null) ? "" : node.getValue();
  }
  
  public Node getFaultStringNode() {
    Node node = getFaultNode();
    return (node == null) ? null : node.getNodeEndsWith("faultstring");
  }
  
  public Node getMethodResponseNode(String paramString) {
    Node node = getBodyNode();
    return (node == null) ? null : node.getNodeEndsWith(String.valueOf(paramString) + "Response");
  }
  
  public void print() {
    Debug.message(toString());
    if (!hasContent()) {
      Node node = getRootNode();
      if (node != null) {
        Debug.message(node.toString());
        return;
      } 
    } 
  }
  
  public void setContent(Node paramNode) {
    setContent(String.valueOf(String.valueOf(String.valueOf("") + "<?xml version=\"1.0\" encoding=\"utf-8\"?>") + "\n") + paramNode.toString());
  }
  
  public void setEnvelopeNode(Node paramNode) {
    setRootNode(paramNode);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\soap\SOAPResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */