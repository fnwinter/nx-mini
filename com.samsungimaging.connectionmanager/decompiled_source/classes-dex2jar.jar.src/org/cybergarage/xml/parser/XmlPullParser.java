package org.cybergarage.xml.parser;

import java.io.InputStream;
import org.cybergarage.xml.Node;
import org.cybergarage.xml.Parser;
import org.cybergarage.xml.ParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class XmlPullParser extends Parser {
  public Node parse(InputStream paramInputStream) throws ParserException {
    try {
      XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
      xmlPullParserFactory.setNamespaceAware(true);
      return parse(xmlPullParserFactory.newPullParser(), paramInputStream);
    } catch (Exception exception) {
      throw new ParserException(exception);
    } 
  }
  
  public Node parse(org.xmlpull.v1.XmlPullParser paramXmlPullParser, InputStream paramInputStream) throws ParserException {
    int i;
    Node node1 = null;
    Node node2 = null;
    try {
      paramXmlPullParser.setInput(paramInputStream, null);
      i = paramXmlPullParser.getEventType();
    } catch (Exception exception) {
      throw new ParserException(exception);
    } 
    while (true) {
      String str2;
      Node node3;
      int j;
      Node node4;
      String str5;
      String str7;
      String str6;
      StringBuffer stringBuffer;
      if (i == 1)
        return node1; 
      switch (i) {
        case 2:
          node4 = new Node();
          str2 = exception.getPrefix();
          str7 = exception.getName();
          stringBuffer = new StringBuffer();
          if (str2 != null && str2.length() > 0) {
            stringBuffer.append(str2);
            stringBuffer.append(":");
          } 
          if (str7 != null && str7.length() > 0)
            stringBuffer.append(str7); 
          node4.setName(stringBuffer.toString());
          j = exception.getAttributeCount();
          for (i = 0;; i++) {
            if (i >= j) {
              if (node2 != null)
                node2.addNode(node4); 
              break;
            } 
            node4.setAttribute(exception.getAttributeName(i), exception.getAttributeValue(i));
          } 
          break;
        case 4:
          str5 = exception.getText();
          node3 = node2;
          node5 = node1;
          if (str5 != null) {
            node3 = node2;
            node5 = node1;
            if (node2 != null) {
              node2.setValue(str5);
              node3 = node2;
              node5 = node1;
            } 
          } 
          i = exception.next();
          node2 = node3;
          node1 = node5;
          continue;
        case 3:
          node3 = node2.getParentNode();
          node5 = node1;
          i = exception.next();
          node2 = node3;
          node1 = node5;
          continue;
        default:
          node3 = node2;
          node5 = node1;
          i = exception.next();
          node2 = node3;
          node1 = node5;
          continue;
      } 
      String str4 = str5;
      String str1 = str4;
      Node node5 = node1;
      if (node1 == null) {
        str1 = str4;
        str6 = str5;
      } 
      i = exception.next();
      str4 = str1;
      String str3 = str6;
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\xml\parser\XmlPullParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */