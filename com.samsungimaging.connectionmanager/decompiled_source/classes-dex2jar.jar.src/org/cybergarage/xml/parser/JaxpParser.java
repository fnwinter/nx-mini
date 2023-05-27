package org.cybergarage.xml.parser;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.cybergarage.xml.Node;
import org.cybergarage.xml.Parser;
import org.cybergarage.xml.ParserException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class JaxpParser extends Parser {
  public Node parse(InputStream paramInputStream) throws ParserException {
    InputStream inputStream = null;
    try {
      Node node;
      Element element = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(paramInputStream)).getDocumentElement();
      paramInputStream = inputStream;
      if (element != null)
        node = parse(null, element); 
      return node;
    } catch (Exception exception) {
      throw new ParserException(exception);
    } 
  }
  
  public Node parse(Node paramNode, Node paramNode1) {
    return parse(paramNode, paramNode1, 0);
  }
  
  public Node parse(Node paramNode, Node paramNode1, int paramInt) {
    // Byte code:
    //   0: aload_2
    //   1: invokeinterface getNodeType : ()S
    //   6: istore #4
    //   8: aload_2
    //   9: invokeinterface getNodeName : ()Ljava/lang/String;
    //   14: astore #7
    //   16: aload_2
    //   17: invokeinterface getNodeValue : ()Ljava/lang/String;
    //   22: astore #8
    //   24: aload_2
    //   25: invokeinterface getAttributes : ()Lorg/w3c/dom/NamedNodeMap;
    //   30: astore #6
    //   32: aload #6
    //   34: ifnull -> 59
    //   37: aload #6
    //   39: invokeinterface getLength : ()I
    //   44: pop
    //   45: iload #4
    //   47: iconst_3
    //   48: if_icmpne -> 62
    //   51: aload_1
    //   52: aload #8
    //   54: invokevirtual addValue : (Ljava/lang/String;)V
    //   57: aload_1
    //   58: areturn
    //   59: goto -> 45
    //   62: iload #4
    //   64: iconst_1
    //   65: if_icmpne -> 57
    //   68: new org/cybergarage/xml/Node
    //   71: dup
    //   72: invokespecial <init> : ()V
    //   75: astore #6
    //   77: aload #6
    //   79: aload #7
    //   81: invokevirtual setName : (Ljava/lang/String;)V
    //   84: aload #6
    //   86: aload #8
    //   88: invokevirtual setValue : (Ljava/lang/String;)V
    //   91: aload_1
    //   92: ifnull -> 101
    //   95: aload_1
    //   96: aload #6
    //   98: invokevirtual addNode : (Lorg/cybergarage/xml/Node;)V
    //   101: aload_2
    //   102: invokeinterface getAttributes : ()Lorg/w3c/dom/NamedNodeMap;
    //   107: astore_1
    //   108: aload_1
    //   109: ifnull -> 130
    //   112: aload_1
    //   113: invokeinterface getLength : ()I
    //   118: istore #5
    //   120: iconst_0
    //   121: istore #4
    //   123: iload #4
    //   125: iload #5
    //   127: if_icmplt -> 153
    //   130: aload_2
    //   131: invokeinterface getFirstChild : ()Lorg/w3c/dom/Node;
    //   136: astore_2
    //   137: aload_2
    //   138: astore_1
    //   139: aload_2
    //   140: ifnonnull -> 191
    //   143: aload #6
    //   145: ldc ''
    //   147: invokevirtual setValue : (Ljava/lang/String;)V
    //   150: aload #6
    //   152: areturn
    //   153: aload_1
    //   154: iload #4
    //   156: invokeinterface item : (I)Lorg/w3c/dom/Node;
    //   161: astore #7
    //   163: aload #6
    //   165: aload #7
    //   167: invokeinterface getNodeName : ()Ljava/lang/String;
    //   172: aload #7
    //   174: invokeinterface getNodeValue : ()Ljava/lang/String;
    //   179: invokevirtual setAttribute : (Ljava/lang/String;Ljava/lang/String;)V
    //   182: iload #4
    //   184: iconst_1
    //   185: iadd
    //   186: istore #4
    //   188: goto -> 123
    //   191: aload_0
    //   192: aload #6
    //   194: aload_1
    //   195: iload_3
    //   196: iconst_1
    //   197: iadd
    //   198: invokevirtual parse : (Lorg/cybergarage/xml/Node;Lorg/w3c/dom/Node;I)Lorg/cybergarage/xml/Node;
    //   201: pop
    //   202: aload_1
    //   203: invokeinterface getNextSibling : ()Lorg/w3c/dom/Node;
    //   208: astore_2
    //   209: aload_2
    //   210: astore_1
    //   211: aload_2
    //   212: ifnonnull -> 191
    //   215: aload #6
    //   217: areturn
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\xml\parser\JaxpParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */