package org.cybergarage.upnp;

import java.util.Iterator;
import org.cybergarage.http.HTTPResponse;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.ActionRequest;
import org.cybergarage.upnp.control.ActionResponse;
import org.cybergarage.upnp.control.ControlResponse;
import org.cybergarage.upnp.xml.ActionData;
import org.cybergarage.util.Debug;
import org.cybergarage.util.Mutex;
import org.cybergarage.xml.Node;

public class Action {
  public static final String ELEM_NAME = "action";
  
  private static final String NAME = "name";
  
  private ArgumentList actionArgList = null;
  
  private Node actionNode;
  
  private boolean bSuccess = false;
  
  private ActionResponse ctrlRes = null;
  
  private Mutex mutex = new Mutex();
  
  private Node serviceNode;
  
  private UPnPStatus upnpStatus = new UPnPStatus();
  
  private Object userData = null;
  
  public Action(Action paramAction) {
    this.serviceNode = paramAction.getServiceNode();
    this.actionNode = paramAction.getActionNode();
  }
  
  public Action(Node paramNode) {
    this.serviceNode = paramNode;
    this.actionNode = new Node("action");
  }
  
  public Action(Node paramNode1, Node paramNode2) {
    this.serviceNode = paramNode1;
    this.actionNode = paramNode2;
  }
  
  private void clearOutputAgumentValues() {
    ArgumentList argumentList = getArgumentList();
    int j = argumentList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      Argument argument = argumentList.getArgument(i);
      if (argument.isOutDirection())
        argument.setValue(""); 
    } 
  }
  
  private ActionData getActionData() {
    Node node = getActionNode();
    ActionData actionData2 = (ActionData)node.getUserData();
    ActionData actionData1 = actionData2;
    if (actionData2 == null) {
      actionData1 = new ActionData();
      node.setUserData(actionData1);
      actionData1.setNode(node);
    } 
    return actionData1;
  }
  
  private Node getServiceNode() {
    return this.serviceNode;
  }
  
  public static boolean isActionNode(Node paramNode) {
    return "action".equals(paramNode.getName());
  }
  
  private void setControlResponse(ControlResponse paramControlResponse) {
    getActionData().setControlResponse(paramControlResponse);
  }
  
  public ActionListener getActionListener() {
    return getActionData().getActionListener();
  }
  
  public Node getActionNode() {
    return this.actionNode;
  }
  
  public Argument getArgument(String paramString) {
    ArgumentList argumentList = getArgumentList();
    int j = argumentList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return null; 
      Argument argument = argumentList.getArgument(i);
      String str = argument.getName();
      if (str != null && paramString.equals(str))
        return argument; 
    } 
  }
  
  public int getArgumentIntegerValue(String paramString) {
    Argument argument = getArgument(paramString);
    return (argument == null) ? 0 : argument.getIntegerValue();
  }
  
  public ArgumentList getArgumentList() {
    ArgumentList argumentList = new ArgumentList();
    Node node = getActionNode().getNode("argumentList");
    if (node != null) {
      int j = node.getNNodes();
      int i = 0;
      while (true) {
        if (i < j) {
          Node node1 = node.getNode(i);
          if (Argument.isArgumentNode(node1))
            argumentList.add((E)new Argument(getServiceNode(), node1)); 
          i++;
          continue;
        } 
        return argumentList;
      } 
    } 
    return argumentList;
  }
  
  public String getArgumentValue(String paramString) {
    Argument argument = getArgument(paramString);
    return (argument == null) ? "" : argument.getValue();
  }
  
  public ControlResponse getControlResponse() {
    return getActionData().getControlResponse();
  }
  
  public UPnPStatus getControlStatus() {
    return getControlResponse().getUPnPError();
  }
  
  public ArgumentList getInputArgumentList() {
    ArgumentList argumentList1 = getArgumentList();
    int j = argumentList1.size();
    ArgumentList argumentList2 = new ArgumentList();
    for (int i = 0;; i++) {
      if (i >= j)
        return argumentList2; 
      Argument argument = argumentList1.getArgument(i);
      if (argument.isInDirection())
        argumentList2.add((E)argument); 
    } 
  }
  
  public String getName() {
    return getActionNode().getNodeValue("name");
  }
  
  public ArgumentList getOutputArgumentList() {
    ArgumentList argumentList1 = getArgumentList();
    int j = argumentList1.size();
    ArgumentList argumentList2 = new ArgumentList();
    for (int i = 0;; i++) {
      if (i >= j)
        return argumentList2; 
      Argument argument = argumentList1.getArgument(i);
      if (argument.isOutDirection())
        argumentList2.add((E)argument); 
    } 
  }
  
  public Service getService() {
    return new Service(getServiceNode());
  }
  
  public UPnPStatus getStatus() {
    return this.upnpStatus;
  }
  
  public Object getUserData() {
    return this.userData;
  }
  
  public void lock() {
    this.mutex.lock();
  }
  
  public boolean performActionListener(ActionRequest paramActionRequest) {
    ActionListener actionListener = getActionListener();
    if (actionListener == null)
      return false; 
    ActionResponse actionResponse = new ActionResponse();
    setStatus(401);
    clearOutputAgumentValues();
    if (actionListener.actionControlReceived(this)) {
      actionResponse.setResponse(this);
    } else {
      UPnPStatus uPnPStatus = getStatus();
      actionResponse.setFaultResponse(uPnPStatus.getCode(), uPnPStatus.getDescription());
    } 
    if (Debug.isOn())
      actionResponse.print(); 
    paramActionRequest.post((HTTPResponse)actionResponse);
    return true;
  }
  
  public boolean postControlAction() {
    ArgumentList argumentList1 = getArgumentList();
    ArgumentList argumentList3 = getInputArgumentList();
    ActionRequest actionRequest = new ActionRequest();
    actionRequest.setRequest(this, argumentList3);
    if (Debug.isOn())
      actionRequest.print(); 
    ActionResponse actionResponse = actionRequest.post();
    if (Debug.isOn())
      actionResponse.print(); 
    setControlResponse((ControlResponse)actionResponse);
    setStatus(actionResponse.getStatusCode());
    if (!actionResponse.isSuccessful())
      return false; 
    ArgumentList argumentList2 = actionResponse.getResponse();
    try {
      argumentList1.setResArgs(argumentList2);
      return true;
    } catch (IllegalArgumentException illegalArgumentException) {
      illegalArgumentException.printStackTrace();
      setStatus(402, "Action succesfully delivered but invalid arguments returned.");
      return false;
    } 
  }
  
  public void print() {
    System.out.println("Action : " + getName());
    ArgumentList argumentList = getArgumentList();
    int j = argumentList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      Argument argument = argumentList.getArgument(i);
      String str1 = argument.getName();
      String str2 = argument.getValue();
      String str3 = argument.getDirection();
      System.out.println(" [" + i + "] = " + str3 + ", " + str1 + ", " + str2);
    } 
  }
  
  public void setActionListener(ActionListener paramActionListener) {
    getActionData().setActionListener(paramActionListener);
  }
  
  public void setArgumentList(ArgumentList paramArgumentList) {
    Node node = getActionNode().getNode("argumentList");
    if (node == null) {
      node = new Node("argumentList");
      getActionNode().addNode(node);
    } else {
      node.removeAllNodes();
    } 
    Iterator<E> iterator = paramArgumentList.iterator();
    while (true) {
      if (!iterator.hasNext())
        return; 
      Argument argument = (Argument)iterator.next();
      argument.setService(getService());
      node.addNode(argument.getArgumentNode());
    } 
  }
  
  public void setArgumentValue(String paramString, int paramInt) {
    setArgumentValue(paramString, Integer.toString(paramInt));
  }
  
  public void setArgumentValue(String paramString1, String paramString2) {
    Argument argument = getArgument(paramString1);
    if (argument == null)
      return; 
    argument.setValue(paramString2);
  }
  
  public void setArgumentValues(ArgumentList paramArgumentList) {
    getArgumentList().set(paramArgumentList);
  }
  
  public void setInArgumentValues(ArgumentList paramArgumentList) {
    getArgumentList().setReqArgs(paramArgumentList);
  }
  
  public void setName(String paramString) {
    getActionNode().setNode("name", paramString);
  }
  
  public void setOutArgumentValues(ArgumentList paramArgumentList) {
    getArgumentList().setResArgs(paramArgumentList);
  }
  
  void setService(Service paramService) {
    this.serviceNode = paramService.getServiceNode();
    Iterator<E> iterator = getArgumentList().iterator();
    while (true) {
      if (!iterator.hasNext())
        return; 
      ((Argument)iterator.next()).setService(paramService);
    } 
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
  
  public void unlock() {
    this.mutex.unlock();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\Action.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */