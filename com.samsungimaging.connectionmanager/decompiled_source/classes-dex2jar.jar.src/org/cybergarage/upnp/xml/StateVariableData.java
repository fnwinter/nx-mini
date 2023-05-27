package org.cybergarage.upnp.xml;

import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.control.QueryResponse;

public class StateVariableData extends NodeData {
  private QueryListener queryListener = null;
  
  private QueryResponse queryRes = null;
  
  private String value = "";
  
  public QueryListener getQueryListener() {
    return this.queryListener;
  }
  
  public QueryResponse getQueryResponse() {
    return this.queryRes;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public void setQueryListener(QueryListener paramQueryListener) {
    this.queryListener = paramQueryListener;
  }
  
  public void setQueryResponse(QueryResponse paramQueryResponse) {
    this.queryRes = paramQueryResponse;
  }
  
  public void setValue(String paramString) {
    this.value = paramString;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\xml\StateVariableData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */