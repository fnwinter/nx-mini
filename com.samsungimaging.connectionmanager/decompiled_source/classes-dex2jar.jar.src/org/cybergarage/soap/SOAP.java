package org.cybergarage.soap;

import org.cybergarage.xml.Node;
import org.cybergarage.xml.Parser;

public class SOAP {
  public static final String BODY = "Body";
  
  public static final String CONTENT_TYPE = "text/xml; charset=\"utf-8\"";
  
  public static final String DELIM = ":";
  
  public static final String DETAIL = "detail";
  
  public static final String ENCSTYLE_URL = "http://schemas.xmlsoap.org/soap/encoding/";
  
  public static final String ENVELOPE = "Envelope";
  
  public static final String ERROR_CODE = "errorCode";
  
  public static final String ERROR_DESCRIPTION = "errorDescription";
  
  public static final String FAULT = "Fault";
  
  public static final String FAULTACTOR = "faultactor";
  
  public static final String FAULT_CODE = "faultcode";
  
  public static final String FAULT_STRING = "faultstring";
  
  public static final String METHODNS = "u";
  
  public static final String RESPONSE = "Response";
  
  public static final String RESULTSTATUS = "ResultStatus";
  
  public static final String UPNP_ERROR = "UPnPError";
  
  public static final String VERSION_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
  
  public static final String XMLNS = "s";
  
  public static final String XMLNS_URL = "http://schemas.xmlsoap.org/soap/envelope/";
  
  private static Parser xmlParser;
  
  public static final Node createEnvelopeBodyNode() {
    Node node = new Node("s:Envelope");
    node.setAttribute("xmlns:s", "http://schemas.xmlsoap.org/soap/envelope/");
    node.setAttribute("s:encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/");
    node.addNode(new Node("s:Body"));
    return node;
  }
  
  public static final Parser getXMLParser() {
    return xmlParser;
  }
  
  public static final void setXMLParser(Parser paramParser) {
    xmlParser = paramParser;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\soap\SOAP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */