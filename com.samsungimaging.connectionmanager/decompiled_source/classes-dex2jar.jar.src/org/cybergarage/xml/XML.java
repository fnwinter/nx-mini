package org.cybergarage.xml;

public class XML {
  public static final String CHARSET_UTF8 = "utf-8";
  
  public static final String CONTENT_TYPE = "text/xml; charset=\"utf-8\"";
  
  public static final String escapeXMLChars(String paramString) {
    return escapeXMLChars(paramString, true);
  }
  
  private static final String escapeXMLChars(String paramString, boolean paramBoolean) {
    if (paramString == null)
      return null; 
    StringBuffer stringBuffer = new StringBuffer();
    int k = paramString.length();
    char[] arrayOfChar = new char[k];
    paramString.getChars(0, k, arrayOfChar, 0);
    int j = 0;
    String str = null;
    int i = 0;
    while (true) {
      int m;
      String str1;
      if (i >= k) {
        if (j) {
          stringBuffer.append(arrayOfChar, j, k - j);
          return stringBuffer.toString();
        } 
        return paramString;
      } 
      switch (arrayOfChar[i]) {
        default:
          str1 = str;
          m = j;
          if (str != null) {
            stringBuffer.append(arrayOfChar, j, i - j);
            stringBuffer.append(str);
            m = i + 1;
            str1 = null;
          } 
          i++;
          str = str1;
          j = m;
          continue;
        case '&':
          str = "&amp;";
        case '<':
          str = "&lt;";
        case '>':
          str = "&gt;";
        case '\'':
          if (paramBoolean) {
            str = "&apos;";
          } else {
            break;
          } 
        case '"':
          break;
      } 
      if (paramBoolean)
        str = "&quot;"; 
    } 
  }
  
  public static final String unescapeXMLChars(String paramString) {
    return (paramString == null) ? null : paramString.replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">").replace("&apos;", "'").replace("&quot;", "\"");
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\xml\XML.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */