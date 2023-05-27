package org.cybergarage.xml;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

public class Node {
  private AttributeList attrList = new AttributeList();
  
  private String name = new String();
  
  private NodeList nodeList = new NodeList();
  
  private Node parentNode = null;
  
  private Object userData = null;
  
  private String value = new String();
  
  public Node() {
    setUserData(null);
    setParentNode(null);
  }
  
  public Node(String paramString) {
    this();
    setName(paramString);
  }
  
  public Node(String paramString1, String paramString2) {
    this();
    setName(paramString1, paramString2);
  }
  
  public void addAttribute(String paramString1, String paramString2) {
    addAttribute(new Attribute(paramString1, paramString2));
  }
  
  public void addAttribute(Attribute paramAttribute) {
    this.attrList.add((E)paramAttribute);
  }
  
  public void addNode(Node paramNode) {
    paramNode.setParentNode(this);
    this.nodeList.add((E)paramNode);
  }
  
  public void addValue(String paramString) {
    if (this.value == null) {
      this.value = paramString;
      return;
    } 
    if (paramString != null) {
      this.value = String.valueOf(this.value) + paramString;
      return;
    } 
  }
  
  public Attribute getAttribute(int paramInt) {
    return this.attrList.getAttribute(paramInt);
  }
  
  public Attribute getAttribute(String paramString) {
    return this.attrList.getAttribute(paramString);
  }
  
  public int getAttributeIntegerValue(String paramString) {
    paramString = getAttributeValue(paramString);
    try {
      return Integer.parseInt(paramString);
    } catch (Exception exception) {
      return 0;
    } 
  }
  
  public String getAttributeValue(String paramString) {
    Attribute attribute = getAttribute(paramString);
    return (attribute != null) ? attribute.getValue() : "";
  }
  
  public String getIndentLevelString(int paramInt) {
    return getIndentLevelString(paramInt, "   ");
  }
  
  public String getIndentLevelString(int paramInt, String paramString) {
    StringBuffer stringBuffer = new StringBuffer(paramString.length() * paramInt);
    for (int i = 0;; i++) {
      if (i >= paramInt)
        return stringBuffer.toString(); 
      stringBuffer.append(paramString);
    } 
  }
  
  public int getIndex(String paramString) {
    int i = -1;
    Iterator<E> iterator = this.nodeList.iterator();
    while (true) {
      if (!iterator.hasNext())
        return i; 
      int j = i + 1;
      i = j;
      if (((Node)iterator.next()).getName().equals(paramString))
        return j; 
    } 
  }
  
  public int getNAttributes() {
    return this.attrList.size();
  }
  
  public int getNNodes() {
    return this.nodeList.size();
  }
  
  public String getName() {
    return this.name;
  }
  
  public Node getNode(int paramInt) {
    return this.nodeList.getNode(paramInt);
  }
  
  public Node getNode(String paramString) {
    return this.nodeList.getNode(paramString);
  }
  
  public Node getNodeEndsWith(String paramString) {
    return this.nodeList.getEndsWith(paramString);
  }
  
  public String getNodeValue(String paramString) {
    Node node = getNode(paramString);
    return (node != null) ? node.getValue() : "";
  }
  
  public Node getParentNode() {
    return this.parentNode;
  }
  
  public Node getRootNode() {
    Node node2 = null;
    for (Node node1 = getParentNode();; node1 = node2.getParentNode()) {
      if (node1 == null)
        return node2; 
      node2 = node1;
    } 
  }
  
  public Object getUserData() {
    return this.userData;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public boolean hasAttributes() {
    return (getNAttributes() > 0);
  }
  
  public boolean hasNodes() {
    return (getNNodes() > 0);
  }
  
  public void insertAttributeAt(Attribute paramAttribute, int paramInt) {
    this.attrList.insertElementAt((E)paramAttribute, paramInt);
  }
  
  public void insertNode(Node paramNode, int paramInt) {
    paramNode.setParentNode(this);
    this.nodeList.insertElementAt((E)paramNode, paramInt);
  }
  
  public boolean isName(String paramString) {
    return this.name.equals(paramString);
  }
  
  public void output(PrintWriter paramPrintWriter, int paramInt, boolean paramBoolean) {
    String str1 = getIndentLevelString(paramInt);
    String str2 = getName();
    String str3 = getValue();
    if (!hasNodes() || !paramBoolean) {
      paramPrintWriter.print(String.valueOf(str1) + "<" + str2);
      outputAttributes(paramPrintWriter);
      if (str3 == null || str3.length() == 0) {
        paramPrintWriter.println("></" + str2 + ">");
        return;
      } 
      paramPrintWriter.println(">" + XML.escapeXMLChars(str3) + "</" + str2 + ">");
      return;
    } 
    paramPrintWriter.print(String.valueOf(str1) + "<" + str2);
    outputAttributes(paramPrintWriter);
    paramPrintWriter.println(">");
    int j = getNNodes();
    int i;
    for (i = 0;; i++) {
      if (i >= j) {
        paramPrintWriter.println(String.valueOf(str1) + "</" + str2 + ">");
        return;
      } 
      getNode(i).output(paramPrintWriter, paramInt + 1, true);
    } 
  }
  
  public void outputAttributes(PrintWriter paramPrintWriter) {
    int j = getNAttributes();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      Attribute attribute = getAttribute(i);
      paramPrintWriter.print(" " + attribute.getName() + "=\"" + XML.escapeXMLChars(attribute.getValue()) + "\"");
    } 
  }
  
  public void print() {
    print(true);
  }
  
  public void print(boolean paramBoolean) {
    PrintWriter printWriter = new PrintWriter(System.out);
    output(printWriter, 0, paramBoolean);
    printWriter.flush();
  }
  
  public void removeAllNodes() {
    this.nodeList.clear();
  }
  
  public boolean removeAttribute(String paramString) {
    return removeAttribute(getAttribute(paramString));
  }
  
  public boolean removeAttribute(Attribute paramAttribute) {
    return this.attrList.remove(paramAttribute);
  }
  
  public boolean removeNode(String paramString) {
    return this.nodeList.remove(getNode(paramString));
  }
  
  public boolean removeNode(Node paramNode) {
    paramNode.setParentNode(null);
    return this.nodeList.remove(paramNode);
  }
  
  public void setAttribute(String paramString, int paramInt) {
    setAttribute(paramString, Integer.toString(paramInt));
  }
  
  public void setAttribute(String paramString1, String paramString2) {
    Attribute attribute = getAttribute(paramString1);
    if (attribute != null) {
      attribute.setValue(paramString2);
      return;
    } 
    addAttribute(new Attribute(paramString1, paramString2));
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public void setName(String paramString1, String paramString2) {
    this.name = String.valueOf(paramString1) + ":" + paramString2;
  }
  
  public void setNameSpace(String paramString1, String paramString2) {
    setAttribute("xmlns:" + paramString1, paramString2);
  }
  
  public void setNode(String paramString1, String paramString2) {
    Node node2 = getNode(paramString1);
    if (node2 != null) {
      node2.setValue(paramString2);
      return;
    } 
    Node node1 = new Node(paramString1);
    node1.setValue(paramString2);
    addNode(node1);
  }
  
  public void setParentNode(Node paramNode) {
    this.parentNode = paramNode;
  }
  
  public void setUserData(Object paramObject) {
    this.userData = paramObject;
  }
  
  public void setValue(int paramInt) {
    setValue(Integer.toString(paramInt));
  }
  
  public void setValue(String paramString) {
    this.value = paramString;
  }
  
  public String toString() {
    return toString("utf-8", true);
  }
  
  public String toString(String paramString, boolean paramBoolean) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
    output(printWriter, 0, paramBoolean);
    printWriter.flush();
    if (paramString != null)
      try {
        if (paramString.length() > 0)
          return byteArrayOutputStream.toString(paramString); 
      } catch (UnsupportedEncodingException unsupportedEncodingException) {} 
    return byteArrayOutputStream.toString();
  }
  
  public String toXMLString() {
    return toXMLString(true);
  }
  
  public String toXMLString(boolean paramBoolean) {
    return toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("&", "&amp;").replaceAll("\"", "&quot;").replaceAll("'", "&apos;");
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\xml\Node.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */