package org.cybergarage.upnp.control;

import org.cybergarage.http.HTTPRequest;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ArgumentList;
import org.cybergarage.upnp.Service;
import org.cybergarage.xml.Node;

public class ActionRequest extends ControlRequest {
  public ActionRequest() {}
  
  public ActionRequest(HTTPRequest paramHTTPRequest) {
    set(paramHTTPRequest);
  }
  
  private Node createContentNode(Service paramService, Action paramAction, ArgumentList paramArgumentList) {
    String str1 = paramAction.getName();
    String str2 = paramService.getServiceType();
    Node node = new Node();
    node.setName("u", str1);
    node.setNameSpace("u", str2);
    int j = paramArgumentList.size();
    int i;
    for (i = 0;; i++) {
      if (i >= j)
        return node; 
      Argument argument = paramArgumentList.getArgument(i);
      Node node1 = new Node();
      node1.setName(argument.getName());
      node1.setValue(argument.getValue());
      node.addNode(node1);
    } 
  }
  
  public String getActionName() {
    Node node = getActionNode();
    if (node == null)
      return ""; 
    String str = node.getName();
    if (str == null)
      return ""; 
    int i = str.indexOf(":") + 1;
    return (i < 0) ? "" : str.substring(i, str.length());
  }
  
  public Node getActionNode() {
    Node node = getBodyNode();
    return (node != null && node.hasNodes()) ? node.getNode(0) : null;
  }
  
  public ArgumentList getArgumentList() {
    Node node = getActionNode();
    int j = node.getNNodes();
    ArgumentList argumentList = new ArgumentList();
    for (int i = 0;; i++) {
      if (i >= j)
        return argumentList; 
      Argument argument = new Argument();
      Node node1 = node.getNode(i);
      argument.setName(node1.getName());
      argument.setValue(node1.getValue());
      argumentList.add(argument);
    } 
  }
  
  public ActionResponse post() {
    return new ActionResponse(postMessage(getRequestHost(), getRequestPort()));
  }
  
  public void setRequest(Action paramAction, ArgumentList paramArgumentList) {
    Service service = paramAction.getService();
    setRequestHost(service);
    setEnvelopeNode(SOAP.createEnvelopeBodyNode());
    Node node = getEnvelopeNode();
    getBodyNode().addNode(createContentNode(service, paramAction, paramArgumentList));
    setContent(node);
    String str2 = service.getServiceType();
    String str1 = paramAction.getName();
    setSOAPAction("\"" + str2 + "#" + str1 + "\"");
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\control\ActionRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */