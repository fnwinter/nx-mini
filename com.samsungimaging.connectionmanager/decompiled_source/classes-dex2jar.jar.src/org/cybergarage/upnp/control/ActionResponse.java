package org.cybergarage.upnp.control;

import org.cybergarage.soap.SOAPResponse;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ArgumentList;
import org.cybergarage.upnp.Service;
import org.cybergarage.xml.Node;

public class ActionResponse extends ControlResponse {
  public ActionResponse() {
    setHeader("EXT", "");
  }
  
  public ActionResponse(SOAPResponse paramSOAPResponse) {
    super(paramSOAPResponse);
    setHeader("EXT", "");
  }
  
  private Node createResponseNode(Action paramAction) {
    String str = paramAction.getName();
    Node node = new Node("u:" + str + "Response");
    Service service = paramAction.getService();
    if (service != null)
      node.setAttribute("xmlns:u", service.getServiceType()); 
    ArgumentList argumentList = paramAction.getArgumentList();
    int j = argumentList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return node; 
      Argument argument = argumentList.getArgument(i);
      if (argument.isOutDirection()) {
        Node node1 = new Node();
        node1.setName(argument.getName());
        node1.setValue(argument.getValue());
        node.addNode(node1);
      } 
    } 
  }
  
  private Node getActionResponseNode() {
    Node node = getBodyNode();
    return (node == null || !node.hasNodes()) ? null : node.getNode(0);
  }
  
  public ArgumentList getResponse() {
    ArgumentList argumentList = new ArgumentList();
    Node node = getActionResponseNode();
    if (node != null) {
      int j = node.getNNodes();
      int i = 0;
      while (true) {
        if (i < j) {
          Node node1 = node.getNode(i);
          argumentList.add(new Argument(node1.getName(), node1.getValue()));
          i++;
          continue;
        } 
        return argumentList;
      } 
    } 
    return argumentList;
  }
  
  public void setResponse(Action paramAction) {
    setStatusCode(200);
    getBodyNode().addNode(createResponseNode(paramAction));
    setContent(getEnvelopeNode());
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\control\ActionResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */