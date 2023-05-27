package org.cybergarage.upnp.xml;

import org.cybergarage.upnp.event.SubscriberList;
import org.cybergarage.util.ListenerList;
import org.cybergarage.xml.Node;

public class ServiceData extends NodeData {
  private ListenerList controlActionListenerList = new ListenerList();
  
  private String descriptionURL = "";
  
  private Node scpdNode = null;
  
  private String sid = "";
  
  private SubscriberList subscriberList = new SubscriberList();
  
  private long timeout = 0L;
  
  public ListenerList getControlActionListenerList() {
    return this.controlActionListenerList;
  }
  
  public String getDescriptionURL() {
    return this.descriptionURL;
  }
  
  public Node getSCPDNode() {
    return this.scpdNode;
  }
  
  public String getSID() {
    return this.sid;
  }
  
  public SubscriberList getSubscriberList() {
    return this.subscriberList;
  }
  
  public long getTimeout() {
    return this.timeout;
  }
  
  public void setDescriptionURL(String paramString) {
    this.descriptionURL = paramString;
  }
  
  public void setSCPDNode(Node paramNode) {
    this.scpdNode = paramNode;
  }
  
  public void setSID(String paramString) {
    this.sid = paramString;
  }
  
  public void setTimeout(long paramLong) {
    this.timeout = paramLong;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\xml\ServiceData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */