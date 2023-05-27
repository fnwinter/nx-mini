package org.cybergarage.soap;

import java.io.ByteArrayInputStream;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.http.HTTPResponse;
import org.cybergarage.util.Debug;
import org.cybergarage.xml.Node;

public class SOAPRequest extends HTTPRequest {
  private static final String SOAPACTION = "SOAPACTION";
  
  private Node rootNode;
  
  public SOAPRequest() {
    setContentType("text/xml; charset=\"utf-8\"");
    setMethod("POST");
  }
  
  public SOAPRequest(HTTPRequest paramHTTPRequest) {
    set(paramHTTPRequest);
  }
  
  private Node getRootNode() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield rootNode : Lorg/cybergarage/xml/Node;
    //   6: ifnull -> 18
    //   9: aload_0
    //   10: getfield rootNode : Lorg/cybergarage/xml/Node;
    //   13: astore_1
    //   14: aload_0
    //   15: monitorexit
    //   16: aload_1
    //   17: areturn
    //   18: new java/io/ByteArrayInputStream
    //   21: dup
    //   22: aload_0
    //   23: invokevirtual getContent : ()[B
    //   26: invokespecial <init> : ([B)V
    //   29: astore_1
    //   30: aload_0
    //   31: invokestatic getXMLParser : ()Lorg/cybergarage/xml/Parser;
    //   34: aload_1
    //   35: invokevirtual parse : (Ljava/io/InputStream;)Lorg/cybergarage/xml/Node;
    //   38: putfield rootNode : Lorg/cybergarage/xml/Node;
    //   41: aload_0
    //   42: getfield rootNode : Lorg/cybergarage/xml/Node;
    //   45: astore_1
    //   46: goto -> 14
    //   49: astore_1
    //   50: aload_1
    //   51: invokestatic warning : (Ljava/lang/Exception;)V
    //   54: goto -> 41
    //   57: astore_1
    //   58: aload_0
    //   59: monitorexit
    //   60: aload_1
    //   61: athrow
    // Exception table:
    //   from	to	target	type
    //   2	14	57	finally
    //   18	41	49	org/cybergarage/xml/ParserException
    //   18	41	57	finally
    //   41	46	57	finally
    //   50	54	57	finally
  }
  
  private void setRootNode(Node paramNode) {
    this.rootNode = paramNode;
  }
  
  public Node getBodyNode() {
    Node node = getEnvelopeNode();
    return (node != null && node.hasNodes()) ? node.getNode(0) : null;
  }
  
  public Node getEnvelopeNode() {
    return getRootNode();
  }
  
  public String getSOAPAction() {
    return getStringHeaderValue("SOAPACTION");
  }
  
  public boolean isSOAPAction(String paramString) {
    String str = getHeaderValue("SOAPACTION");
    if (str != null) {
      if (str.equals(paramString))
        return true; 
      str = getSOAPAction();
      if (str != null)
        return str.equals(paramString); 
    } 
    return false;
  }
  
  public SOAPResponse postMessage(String paramString, int paramInt) {
    HTTPResponse hTTPResponse = post(paramString, paramInt);
    hTTPResponse.getStatusCode();
    hTTPResponse = new SOAPResponse(hTTPResponse);
    byte[] arrayOfByte = hTTPResponse.getContent();
    if (arrayOfByte.length <= 0)
      return (SOAPResponse)hTTPResponse; 
    try {
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
      hTTPResponse.setEnvelopeNode(SOAP.getXMLParser().parse(byteArrayInputStream));
      return (SOAPResponse)hTTPResponse;
    } catch (Exception exception) {
      Debug.warning(exception);
      return (SOAPResponse)hTTPResponse;
    } 
  }
  
  public void print() {
    Debug.message(toString());
    if (!hasContent()) {
      Node node = getRootNode();
      if (node != null) {
        Debug.message(node.toString());
        return;
      } 
    } 
  }
  
  public void setContent(Node paramNode) {
    setContent(String.valueOf(String.valueOf(String.valueOf("") + "<?xml version=\"1.0\" encoding=\"utf-8\"?>") + "\n") + paramNode.toString());
  }
  
  public void setEnvelopeNode(Node paramNode) {
    setRootNode(paramNode);
  }
  
  public void setSOAPAction(String paramString) {
    setStringHeader("SOAPACTION", paramString);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\soap\SOAPRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */