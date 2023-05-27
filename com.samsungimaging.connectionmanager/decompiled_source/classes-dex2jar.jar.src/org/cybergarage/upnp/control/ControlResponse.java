package org.cybergarage.upnp.control;

import org.cybergarage.soap.SOAPResponse;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.UPnPStatus;
import org.cybergarage.xml.Node;

public class ControlResponse extends SOAPResponse {
  public static final String FAULT_CODE = "Client";
  
  public static final String FAULT_STRING = "UPnPError";
  
  private UPnPStatus upnpErr = new UPnPStatus();
  
  public ControlResponse() {
    setServer(UPnP.getServerName());
  }
  
  public ControlResponse(SOAPResponse paramSOAPResponse) {
    super(paramSOAPResponse);
  }
  
  private Node createFaultResponseNode(int paramInt) {
    return createFaultResponseNode(paramInt, UPnPStatus.code2String(paramInt));
  }
  
  private Node createFaultResponseNode(int paramInt, String paramString) {
    Node node1 = new Node("s:Fault");
    Node node2 = new Node("faultcode");
    node2.setValue("s:Client");
    node1.addNode(node2);
    node2 = new Node("faultstring");
    node2.setValue("UPnPError");
    node1.addNode(node2);
    Node node3 = new Node("detail");
    node1.addNode(node3);
    node2 = new Node("UPnPError");
    node2.setAttribute("xmlns", "urn:schemas-upnp-org:control-1-0");
    node3.addNode(node2);
    node3 = new Node("errorCode");
    node3.setValue(paramInt);
    node2.addNode(node3);
    node3 = new Node("errorDescription");
    node3.setValue(paramString);
    node2.addNode(node3);
    return node1;
  }
  
  private Node getUPnPErrorCodeNode() {
    Node node = getUPnPErrorNode();
    return (node == null) ? null : node.getNodeEndsWith("errorCode");
  }
  
  private Node getUPnPErrorDescriptionNode() {
    Node node = getUPnPErrorNode();
    return (node == null) ? null : node.getNodeEndsWith("errorDescription");
  }
  
  private Node getUPnPErrorNode() {
    Node node = getFaultDetailNode();
    return (node == null) ? null : node.getNodeEndsWith("UPnPError");
  }
  
  public UPnPStatus getUPnPError() {
    int i = getUPnPErrorCode();
    String str = getUPnPErrorDescription();
    this.upnpErr.setCode(i);
    this.upnpErr.setDescription(str);
    return this.upnpErr;
  }
  
  public int getUPnPErrorCode() {
    Node node = getUPnPErrorCodeNode();
    if (node == null)
      return -1; 
    String str = node.getValue();
    try {
      return Integer.parseInt(str);
    } catch (Exception exception) {
      return -1;
    } 
  }
  
  public String getUPnPErrorDescription() {
    Node node = getUPnPErrorDescriptionNode();
    return (node == null) ? "" : node.getValue();
  }
  
  public void setFaultResponse(int paramInt) {
    setFaultResponse(paramInt, UPnPStatus.code2String(paramInt));
  }
  
  public void setFaultResponse(int paramInt, String paramString) {
    setStatusCode(500);
    getBodyNode().addNode(createFaultResponseNode(paramInt, paramString));
    setContent(getEnvelopeNode());
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\control\ControlResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */