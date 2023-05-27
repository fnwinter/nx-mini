package org.cybergarage.upnp.event;

import org.cybergarage.http.HTTPRequest;
import org.cybergarage.soap.SOAPRequest;
import org.cybergarage.xml.Node;

public class NotifyRequest extends SOAPRequest {
  private static final String PROPERTY = "property";
  
  private static final String PROPERTYSET = "propertyset";
  
  private static final String XMLNS = "e";
  
  public NotifyRequest() {}
  
  public NotifyRequest(HTTPRequest paramHTTPRequest) {
    set(paramHTTPRequest);
  }
  
  private Node createPropertySetNode(String paramString1, String paramString2) {
    Node node2 = new Node("propertyset");
    node2.setNameSpace("e", "urn:schemas-upnp-org:event-1-0");
    Node node3 = new Node("property");
    node2.addNode(node3);
    Node node1 = new Node(paramString1);
    node1.setValue(paramString2);
    node3.addNode(node1);
    return node2;
  }
  
  private Property getProperty(Node paramNode) {
    Property property = new Property();
    if (paramNode == null)
      return property; 
    String str2 = paramNode.getName();
    int i = str2.lastIndexOf(':');
    String str1 = str2;
    if (i != -1)
      str1 = str2.substring(i + 1); 
    property.setName(str1);
    property.setValue(paramNode.getValue());
    return property;
  }
  
  private Node getVariableNode() {
    Node node = getEnvelopeNode();
    if (node != null && node.hasNodes()) {
      node = node.getNode(0);
      if (node.hasNodes())
        return node.getNode(0); 
    } 
    return null;
  }
  
  public PropertyList getPropertyList() {
    PropertyList propertyList = new PropertyList();
    Node node = getEnvelopeNode();
    if (node != null) {
      int i = 0;
      while (true) {
        if (i < node.getNNodes()) {
          Node node1 = node.getNode(i);
          if (node1 != null)
            propertyList.add((E)getProperty(node1.getNode(0))); 
          i++;
          continue;
        } 
        return propertyList;
      } 
    } 
    return propertyList;
  }
  
  public long getSEQ() {
    return getLongHeaderValue("SEQ");
  }
  
  public String getSID() {
    return Subscription.getSID(getHeaderValue("SID"));
  }
  
  public void setNT(String paramString) {
    setHeader("NT", paramString);
  }
  
  public void setNTS(String paramString) {
    setHeader("NTS", paramString);
  }
  
  public boolean setRequest(Subscriber paramSubscriber, String paramString1, String paramString2) {
    paramSubscriber.getDeliveryURL();
    String str1 = paramSubscriber.getSID();
    long l = paramSubscriber.getNotifyCount();
    String str2 = paramSubscriber.getDeliveryHost();
    String str3 = paramSubscriber.getDeliveryPath();
    int i = paramSubscriber.getDeliveryPort();
    setMethod("NOTIFY");
    setURI(str3);
    setHost(str2, i);
    setNT("upnp:event");
    setNTS("upnp:propchange");
    setSID(str1);
    setSEQ(l);
    setContentType("text/xml; charset=\"utf-8\"");
    setContent(createPropertySetNode(paramString1, paramString2));
    return true;
  }
  
  public void setSEQ(long paramLong) {
    setHeader("SEQ", Long.toString(paramLong));
  }
  
  public void setSID(String paramString) {
    setHeader("SID", Subscription.toSIDHeaderString(paramString));
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\event\NotifyRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */