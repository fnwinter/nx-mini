package org.cybergarage.upnp;

import java.util.Iterator;
import org.cybergarage.http.HTTPResponse;
import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.control.QueryRequest;
import org.cybergarage.upnp.control.QueryResponse;
import org.cybergarage.upnp.xml.NodeData;
import org.cybergarage.upnp.xml.StateVariableData;
import org.cybergarage.util.Debug;
import org.cybergarage.xml.Node;

public class StateVariable extends NodeData {
  private static final String DATATYPE = "dataType";
  
  private static final String DEFAULT_VALUE = "defaultValue";
  
  public static final String ELEM_NAME = "stateVariable";
  
  private static final String NAME = "name";
  
  private static final String SENDEVENTS = "sendEvents";
  
  private static final String SENDEVENTS_NO = "no";
  
  private static final String SENDEVENTS_YES = "yes";
  
  private Node serviceNode = null;
  
  private Node stateVariableNode = new Node("stateVariable");
  
  private UPnPStatus upnpStatus = new UPnPStatus();
  
  private Object userData = null;
  
  public StateVariable() {}
  
  public StateVariable(Node paramNode1, Node paramNode2) {}
  
  public static boolean isStateVariableNode(Node paramNode) {
    return "stateVariable".equals(paramNode.getName());
  }
  
  private void setQueryResponse(QueryResponse paramQueryResponse) {
    getStateVariableData().setQueryResponse(paramQueryResponse);
  }
  
  public AllowedValueList getAllowedValueList() {
    AllowedValueList allowedValueList = new AllowedValueList();
    Node node = getStateVariableNode().getNode("allowedValueList");
    if (node == null)
      return null; 
    int j = node.getNNodes();
    int i = 0;
    while (true) {
      Node node1;
      AllowedValueList allowedValueList1 = allowedValueList;
      if (i < j) {
        node1 = node.getNode(i);
        if (AllowedValue.isAllowedValueNode(node1))
          allowedValueList.add((E)new AllowedValue(node1)); 
        i++;
        continue;
      } 
      return (AllowedValueList)node1;
    } 
  }
  
  public AllowedValueRange getAllowedValueRange() {
    Node node = getStateVariableNode().getNode("allowedValueRange");
    return (node == null) ? null : new AllowedValueRange(node);
  }
  
  public String getDataType() {
    return getStateVariableNode().getNodeValue("dataType");
  }
  
  public String getDefaultValue() {
    return getStateVariableNode().getNodeValue("defaultValue");
  }
  
  public String getName() {
    return getStateVariableNode().getNodeValue("name");
  }
  
  public QueryListener getQueryListener() {
    return getStateVariableData().getQueryListener();
  }
  
  public QueryResponse getQueryResponse() {
    return getStateVariableData().getQueryResponse();
  }
  
  public UPnPStatus getQueryStatus() {
    return getQueryResponse().getUPnPError();
  }
  
  public Service getService() {
    Node node = getServiceNode();
    return (node == null) ? null : new Service(node);
  }
  
  public Node getServiceNode() {
    return this.serviceNode;
  }
  
  public StateVariableData getStateVariableData() {
    Node node = getStateVariableNode();
    StateVariableData stateVariableData2 = (StateVariableData)node.getUserData();
    StateVariableData stateVariableData1 = stateVariableData2;
    if (stateVariableData2 == null) {
      stateVariableData1 = new StateVariableData();
      node.setUserData(stateVariableData1);
      stateVariableData1.setNode(node);
    } 
    return stateVariableData1;
  }
  
  public Node getStateVariableNode() {
    return this.stateVariableNode;
  }
  
  public UPnPStatus getStatus() {
    return this.upnpStatus;
  }
  
  public Object getUserData() {
    return this.userData;
  }
  
  public String getValue() {
    return getStateVariableData().getValue();
  }
  
  public boolean hasAllowedValueList() {
    return (getAllowedValueList() != null);
  }
  
  public boolean hasAllowedValueRange() {
    return (getAllowedValueRange() != null);
  }
  
  public boolean isSendEvents() {
    String str = getStateVariableNode().getAttributeValue("sendEvents");
    return (str != null && str.equalsIgnoreCase("yes"));
  }
  
  public boolean performQueryListener(QueryRequest paramQueryRequest) {
    QueryListener queryListener = getQueryListener();
    if (queryListener == null)
      return false; 
    QueryResponse queryResponse = new QueryResponse();
    StateVariable stateVariable = new StateVariable();
    stateVariable.set(this);
    stateVariable.setValue("");
    stateVariable.setStatus(404);
    if (queryListener.queryControlReceived(stateVariable)) {
      queryResponse.setResponse(stateVariable);
      paramQueryRequest.post((HTTPResponse)queryResponse);
      return true;
    } 
    UPnPStatus uPnPStatus = stateVariable.getStatus();
    queryResponse.setFaultResponse(uPnPStatus.getCode(), uPnPStatus.getDescription());
    paramQueryRequest.post((HTTPResponse)queryResponse);
    return true;
  }
  
  public boolean postQuerylAction() {
    QueryRequest queryRequest = new QueryRequest();
    queryRequest.setRequest(this);
    if (Debug.isOn())
      queryRequest.print(); 
    QueryResponse queryResponse = queryRequest.post();
    if (Debug.isOn())
      queryResponse.print(); 
    setQueryResponse(queryResponse);
    if (!queryResponse.isSuccessful()) {
      setValue(queryResponse.getReturnValue());
      return false;
    } 
    setValue(queryResponse.getReturnValue());
    return true;
  }
  
  public void set(StateVariable paramStateVariable) {
    setName(paramStateVariable.getName());
    setValue(paramStateVariable.getValue());
    setDataType(paramStateVariable.getDataType());
    setSendEvents(paramStateVariable.isSendEvents());
  }
  
  public void setAllowedValueList(AllowedValueList paramAllowedValueList) {
    getStateVariableNode().removeNode("allowedValueList");
    getStateVariableNode().removeNode("allowedValueRange");
    Node node = new Node("allowedValueList");
    Iterator<E> iterator = paramAllowedValueList.iterator();
    while (true) {
      if (!iterator.hasNext()) {
        getStateVariableNode().addNode(node);
        return;
      } 
      node.addNode(((AllowedValue)iterator.next()).getAllowedValueNode());
    } 
  }
  
  public void setAllowedValueRange(AllowedValueRange paramAllowedValueRange) {
    getStateVariableNode().removeNode("allowedValueList");
    getStateVariableNode().removeNode("allowedValueRange");
    getStateVariableNode().addNode(paramAllowedValueRange.getAllowedValueRangeNode());
  }
  
  public void setDataType(String paramString) {
    getStateVariableNode().setNode("dataType", paramString);
  }
  
  public void setDefaultValue(String paramString) {
    getStateVariableNode().setNode("defaultValue", paramString);
  }
  
  public void setName(String paramString) {
    getStateVariableNode().setNode("name", paramString);
  }
  
  public void setQueryListener(QueryListener paramQueryListener) {
    getStateVariableData().setQueryListener(paramQueryListener);
  }
  
  public void setSendEvents(boolean paramBoolean) {
    String str;
    Node node = getStateVariableNode();
    if (paramBoolean) {
      str = "yes";
    } else {
      str = "no";
    } 
    node.setAttribute("sendEvents", str);
  }
  
  void setServiceNode(Node paramNode) {
    this.serviceNode = paramNode;
  }
  
  public void setStatus(int paramInt) {
    setStatus(paramInt, UPnPStatus.code2String(paramInt));
  }
  
  public void setStatus(int paramInt, String paramString) {
    this.upnpStatus.setCode(paramInt);
    this.upnpStatus.setDescription(paramString);
  }
  
  public void setUserData(Object paramObject) {
    this.userData = paramObject;
  }
  
  public void setValue(int paramInt) {
    setValue(Integer.toString(paramInt));
  }
  
  public void setValue(long paramLong) {
    setValue(Long.toString(paramLong));
  }
  
  public void setValue(String paramString) {
    String str = getStateVariableData().getValue();
    if (str == null || !str.equals(paramString)) {
      getStateVariableData().setValue(paramString);
      Service service = getService();
      if (service != null && isSendEvents()) {
        service.notify(this);
        return;
      } 
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\StateVariable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */