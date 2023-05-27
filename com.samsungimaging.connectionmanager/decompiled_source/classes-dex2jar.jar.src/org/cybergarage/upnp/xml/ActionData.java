package org.cybergarage.upnp.xml;

import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.ControlResponse;

public class ActionData extends NodeData {
  private ActionListener actionListener = null;
  
  private ControlResponse ctrlRes = null;
  
  public ActionListener getActionListener() {
    return this.actionListener;
  }
  
  public ControlResponse getControlResponse() {
    return this.ctrlRes;
  }
  
  public void setActionListener(ActionListener paramActionListener) {
    this.actionListener = paramActionListener;
  }
  
  public void setControlResponse(ControlResponse paramControlResponse) {
    this.ctrlRes = paramControlResponse;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\xml\ActionData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */