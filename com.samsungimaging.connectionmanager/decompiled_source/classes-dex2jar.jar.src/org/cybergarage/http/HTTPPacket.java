package org.cybergarage.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.Vector;
import org.cybergarage.net.HostInterface;
import org.cybergarage.util.Debug;
import org.cybergarage.util.StringUtil;

public class HTTPPacket {
  private byte[] content = new byte[0];
  
  private InputStream contentInput = null;
  
  private String firstLine = "";
  
  private Vector httpHeaderList = new Vector();
  
  private String version;
  
  public HTTPPacket() {
    setVersion("1.1");
    setContentInputStream(null);
  }
  
  public HTTPPacket(InputStream paramInputStream) {
    setVersion("1.1");
    set(paramInputStream);
    setContentInputStream(null);
  }
  
  public HTTPPacket(HTTPPacket paramHTTPPacket) {
    setVersion("1.1");
    set(paramHTTPPacket);
    setContentInputStream(null);
  }
  
  private String readLine(BufferedInputStream paramBufferedInputStream) {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    byte[] arrayOfByte = new byte[1];
    try {
      int i = paramBufferedInputStream.read(arrayOfByte);
      while (true) {
        if (i > 0 && arrayOfByte[0] != 10) {
          if (arrayOfByte[0] != 13)
            byteArrayOutputStream.write(arrayOfByte[0]); 
          i = paramBufferedInputStream.read(arrayOfByte);
          continue;
        } 
        return byteArrayOutputStream.toString();
      } 
    } catch (InterruptedIOException interruptedIOException) {
    
    } catch (IOException iOException) {
      Debug.warning(iOException);
    } 
    return byteArrayOutputStream.toString();
  }
  
  private void setFirstLine(String paramString) {
    this.firstLine = paramString;
  }
  
  public void addHeader(String paramString1, String paramString2) {
    HTTPHeader hTTPHeader = new HTTPHeader(paramString1, paramString2);
    this.httpHeaderList.add(hTTPHeader);
  }
  
  public void addHeader(HTTPHeader paramHTTPHeader) {
    this.httpHeaderList.add(paramHTTPHeader);
  }
  
  public void clearHeaders() {
    this.httpHeaderList.clear();
    this.httpHeaderList = new Vector();
  }
  
  public String getCacheControl() {
    return getHeaderValue("Cache-Control");
  }
  
  public String getCharSet() {
    String str1 = getContentType();
    if (str1 == null)
      return ""; 
    str1 = str1.toLowerCase();
    int i = str1.indexOf("charset");
    if (i < 0)
      return ""; 
    i = "charset".length() + i + 1;
    String str2 = new String(str1.getBytes(), i, str1.length() - i);
    if (str2.length() < 0)
      return ""; 
    str1 = str2;
    if (str2.charAt(0) == '"')
      str1 = str2.substring(1, str2.length() - 1); 
    if (str1.length() < 0)
      return ""; 
    str2 = str1;
    return (str1.charAt(str1.length() - 1) == '"') ? str1.substring(0, str1.length() - 1) : str2;
  }
  
  public String getConnection() {
    return getHeaderValue("Connection");
  }
  
  public byte[] getContent() {
    return this.content;
  }
  
  public InputStream getContentInputStream() {
    return this.contentInput;
  }
  
  public long getContentLength() {
    return getLongHeaderValue("Content-Length");
  }
  
  public long[] getContentRange() {
    long[] arrayOfLong = new long[3];
    arrayOfLong[2] = 0L;
    arrayOfLong[1] = 0L;
    arrayOfLong[0] = 0L;
    if (hasContentRange()) {
      String str2 = getHeaderValue("Content-Range");
      String str1 = str2;
      if (str2.length() <= 0)
        str1 = getHeaderValue("Range"); 
      if (str1.length() > 0) {
        StringTokenizer stringTokenizer = new StringTokenizer(str1, " =");
        if (stringTokenizer.hasMoreTokens()) {
          stringTokenizer.nextToken(" ");
          if (stringTokenizer.hasMoreTokens()) {
            str2 = stringTokenizer.nextToken(" -");
            try {
              arrayOfLong[0] = Long.parseLong(str2);
              if (stringTokenizer.hasMoreTokens()) {
                str2 = stringTokenizer.nextToken("-/");
                try {
                  arrayOfLong[1] = Long.parseLong(str2);
                  if (stringTokenizer.hasMoreTokens()) {
                    String str = stringTokenizer.nextToken("/");
                    try {
                      arrayOfLong[2] = Long.parseLong(str);
                      return arrayOfLong;
                    } catch (NumberFormatException numberFormatException) {
                      return arrayOfLong;
                    } 
                  } 
                } catch (NumberFormatException numberFormatException) {}
              } 
            } catch (NumberFormatException numberFormatException) {}
          } 
        } 
      } 
    } 
    return arrayOfLong;
  }
  
  public long getContentRangeFirstPosition() {
    return getContentRange()[0];
  }
  
  public long getContentRangeInstanceLength() {
    return getContentRange()[2];
  }
  
  public long getContentRangeLastPosition() {
    return getContentRange()[1];
  }
  
  public String getContentString() {
    String str = getCharSet();
    if (str == null || str.length() <= 0)
      return new String(this.content); 
    try {
      return new String(this.content, str);
    } catch (Exception exception) {
      Debug.warning(exception);
      return new String(this.content);
    } 
  }
  
  public String getContentType() {
    return getHeaderValue("Content-Type");
  }
  
  public String getDate() {
    return getHeaderValue("Date");
  }
  
  protected String getFirstLine() {
    return this.firstLine;
  }
  
  protected String getFirstLineToken(int paramInt) {
    StringTokenizer stringTokenizer = new StringTokenizer(this.firstLine, " ");
    String str = "";
    for (int i = 0;; i++) {
      if (i > paramInt)
        return str; 
      if (!stringTokenizer.hasMoreTokens())
        return ""; 
      str = stringTokenizer.nextToken();
    } 
  }
  
  public HTTPHeader getHeader(int paramInt) {
    return this.httpHeaderList.get(paramInt);
  }
  
  public HTTPHeader getHeader(String paramString) {
    int j = getNHeaders();
    int i = 0;
    while (true) {
      if (i >= j)
        return null; 
      HTTPHeader hTTPHeader2 = getHeader(i);
      HTTPHeader hTTPHeader1 = hTTPHeader2;
      if (!hTTPHeader2.getName().equalsIgnoreCase(paramString)) {
        i++;
        continue;
      } 
      return hTTPHeader1;
    } 
  }
  
  public String getHeaderString() {
    StringBuffer stringBuffer = new StringBuffer();
    int j = getNHeaders();
    for (int i = 0;; i++) {
      if (i >= j)
        return stringBuffer.toString(); 
      HTTPHeader hTTPHeader = getHeader(i);
      stringBuffer.append(String.valueOf(hTTPHeader.getName()) + ": " + hTTPHeader.getValue() + "\r\n");
    } 
  }
  
  public String getHeaderValue(String paramString) {
    HTTPHeader hTTPHeader = getHeader(paramString);
    return (hTTPHeader == null) ? "" : hTTPHeader.getValue();
  }
  
  public String getHost() {
    return getHeaderValue("HOST");
  }
  
  public int getIntegerHeaderValue(String paramString) {
    HTTPHeader hTTPHeader = getHeader(paramString);
    return (hTTPHeader == null) ? 0 : StringUtil.toInteger(hTTPHeader.getValue());
  }
  
  public long getLongHeaderValue(String paramString) {
    HTTPHeader hTTPHeader = getHeader(paramString);
    return (hTTPHeader == null) ? 0L : StringUtil.toLong(hTTPHeader.getValue());
  }
  
  public int getNHeaders() {
    return this.httpHeaderList.size();
  }
  
  public String getServer() {
    return getHeaderValue("Server");
  }
  
  public String getStringHeaderValue(String paramString) {
    return getStringHeaderValue(paramString, "\"", "\"");
  }
  
  public String getStringHeaderValue(String paramString1, String paramString2, String paramString3) {
    String str = getHeaderValue(paramString1);
    paramString1 = str;
    if (str.startsWith(paramString2))
      paramString1 = str.substring(1, str.length()); 
    paramString2 = paramString1;
    if (paramString1.endsWith(paramString3))
      paramString2 = paramString1.substring(0, paramString1.length() - 1); 
    return paramString2;
  }
  
  public String getTransferEncoding() {
    return getHeaderValue("Transfer-Encoding");
  }
  
  public String getVersion() {
    return this.version;
  }
  
  public boolean hasConnection() {
    return hasHeader("Connection");
  }
  
  public boolean hasContent() {
    return (this.content.length > 0);
  }
  
  public boolean hasContentInputStream() {
    return (this.contentInput != null);
  }
  
  public boolean hasContentRange() {
    return !(!hasHeader("Content-Range") && !hasHeader("Range"));
  }
  
  public boolean hasFirstLine() {
    return (this.firstLine.length() > 0);
  }
  
  public boolean hasHeader(String paramString) {
    return (getHeader(paramString) != null);
  }
  
  public boolean hasTransferEncoding() {
    return hasHeader("Transfer-Encoding");
  }
  
  public void init() {
    setFirstLine("");
    clearHeaders();
    setContent(new byte[0], false);
    setContentInputStream(null);
  }
  
  public boolean isChunked() {
    if (hasTransferEncoding()) {
      String str = getTransferEncoding();
      if (str != null)
        return str.equalsIgnoreCase("Chunked"); 
    } 
    return false;
  }
  
  public boolean isCloseConnection() {
    if (hasConnection()) {
      String str = getConnection();
      if (str != null)
        return str.equalsIgnoreCase("close"); 
    } 
    return false;
  }
  
  public boolean isKeepAliveConnection() {
    if (hasConnection()) {
      String str = getConnection();
      if (str != null)
        return str.equalsIgnoreCase("Keep-Alive"); 
    } 
    return false;
  }
  
  public boolean read(HTTPSocket paramHTTPSocket) {
    init();
    return set(paramHTTPSocket);
  }
  
  protected void set(HTTPPacket paramHTTPPacket) {
    setFirstLine(paramHTTPPacket.getFirstLine());
    clearHeaders();
    int j = paramHTTPPacket.getNHeaders();
    for (int i = 0;; i++) {
      if (i >= j) {
        setContent(paramHTTPPacket.getContent());
        return;
      } 
      addHeader(paramHTTPPacket.getHeader(i));
    } 
  }
  
  protected boolean set(InputStream paramInputStream) {
    return set(paramInputStream, false);
  }
  
  protected boolean set(InputStream paramInputStream, boolean paramBoolean) {
    // Byte code:
    //   0: new java/io/BufferedInputStream
    //   3: dup
    //   4: aload_1
    //   5: invokespecial <init> : (Ljava/io/InputStream;)V
    //   8: astore #13
    //   10: aload_0
    //   11: aload #13
    //   13: invokespecial readLine : (Ljava/io/BufferedInputStream;)Ljava/lang/String;
    //   16: astore_1
    //   17: aload_1
    //   18: ifnull -> 484
    //   21: aload_1
    //   22: invokevirtual length : ()I
    //   25: ifgt -> 31
    //   28: goto -> 484
    //   31: aload_0
    //   32: aload_1
    //   33: invokespecial setFirstLine : (Ljava/lang/String;)V
    //   36: new org/cybergarage/http/HTTPStatus
    //   39: dup
    //   40: aload_1
    //   41: invokespecial <init> : (Ljava/lang/String;)V
    //   44: invokevirtual getStatusCode : ()I
    //   47: bipush #100
    //   49: if_icmpne -> 93
    //   52: aload_0
    //   53: aload #13
    //   55: invokespecial readLine : (Ljava/io/BufferedInputStream;)Ljava/lang/String;
    //   58: astore_1
    //   59: aload_1
    //   60: ifnull -> 70
    //   63: aload_1
    //   64: invokevirtual length : ()I
    //   67: ifgt -> 124
    //   70: aload_0
    //   71: aload #13
    //   73: invokespecial readLine : (Ljava/io/BufferedInputStream;)Ljava/lang/String;
    //   76: astore_1
    //   77: aload_1
    //   78: ifnull -> 486
    //   81: aload_1
    //   82: invokevirtual length : ()I
    //   85: ifle -> 486
    //   88: aload_0
    //   89: aload_1
    //   90: invokespecial setFirstLine : (Ljava/lang/String;)V
    //   93: aload_0
    //   94: aload #13
    //   96: invokespecial readLine : (Ljava/io/BufferedInputStream;)Ljava/lang/String;
    //   99: astore_1
    //   100: aload_1
    //   101: ifnull -> 111
    //   104: aload_1
    //   105: invokevirtual length : ()I
    //   108: ifgt -> 155
    //   111: iload_2
    //   112: ifeq -> 186
    //   115: aload_0
    //   116: ldc ''
    //   118: iconst_0
    //   119: invokevirtual setContent : (Ljava/lang/String;Z)V
    //   122: iconst_1
    //   123: ireturn
    //   124: new org/cybergarage/http/HTTPHeader
    //   127: dup
    //   128: aload_1
    //   129: invokespecial <init> : (Ljava/lang/String;)V
    //   132: astore_1
    //   133: aload_1
    //   134: invokevirtual hasName : ()Z
    //   137: ifeq -> 145
    //   140: aload_0
    //   141: aload_1
    //   142: invokevirtual setHeader : (Lorg/cybergarage/http/HTTPHeader;)V
    //   145: aload_0
    //   146: aload #13
    //   148: invokespecial readLine : (Ljava/io/BufferedInputStream;)Ljava/lang/String;
    //   151: astore_1
    //   152: goto -> 59
    //   155: new org/cybergarage/http/HTTPHeader
    //   158: dup
    //   159: aload_1
    //   160: invokespecial <init> : (Ljava/lang/String;)V
    //   163: astore_1
    //   164: aload_1
    //   165: invokevirtual hasName : ()Z
    //   168: ifeq -> 176
    //   171: aload_0
    //   172: aload_1
    //   173: invokevirtual setHeader : (Lorg/cybergarage/http/HTTPHeader;)V
    //   176: aload_0
    //   177: aload #13
    //   179: invokespecial readLine : (Ljava/io/BufferedInputStream;)Ljava/lang/String;
    //   182: astore_1
    //   183: goto -> 100
    //   186: aload_0
    //   187: invokevirtual isChunked : ()Z
    //   190: istore_2
    //   191: lconst_0
    //   192: lstore #5
    //   194: iload_2
    //   195: ifeq -> 250
    //   198: aload_0
    //   199: aload #13
    //   201: invokespecial readLine : (Ljava/io/BufferedInputStream;)Ljava/lang/String;
    //   204: astore_1
    //   205: aload_1
    //   206: ifnull -> 488
    //   209: aload_1
    //   210: invokevirtual trim : ()Ljava/lang/String;
    //   213: bipush #16
    //   215: invokestatic parseLong : (Ljava/lang/String;I)J
    //   218: lstore #7
    //   220: lload #7
    //   222: lstore #5
    //   224: new java/io/ByteArrayOutputStream
    //   227: dup
    //   228: invokespecial <init> : ()V
    //   231: astore_1
    //   232: lconst_0
    //   233: lload #5
    //   235: lcmp
    //   236: iflt -> 259
    //   239: aload_0
    //   240: aload_1
    //   241: invokevirtual toByteArray : ()[B
    //   244: iconst_0
    //   245: invokevirtual setContent : ([BZ)V
    //   248: iconst_1
    //   249: ireturn
    //   250: aload_0
    //   251: invokevirtual getContentLength : ()J
    //   254: lstore #5
    //   256: goto -> 224
    //   259: invokestatic getChunkSize : ()I
    //   262: istore_3
    //   263: lload #5
    //   265: iload_3
    //   266: i2l
    //   267: lcmp
    //   268: ifle -> 349
    //   271: iload_3
    //   272: i2l
    //   273: lstore #7
    //   275: lload #7
    //   277: l2i
    //   278: newarray byte
    //   280: astore #14
    //   282: lconst_0
    //   283: lstore #7
    //   285: goto -> 494
    //   288: aload #13
    //   290: ldc '\\r\\n'
    //   292: invokevirtual length : ()I
    //   295: i2l
    //   296: lload #5
    //   298: lsub
    //   299: invokevirtual skip : (J)J
    //   302: lstore #7
    //   304: lload #7
    //   306: lconst_0
    //   307: lcmp
    //   308: ifge -> 438
    //   311: aload_0
    //   312: aload #13
    //   314: invokespecial readLine : (Ljava/io/BufferedInputStream;)Ljava/lang/String;
    //   317: astore #14
    //   319: new java/lang/String
    //   322: dup
    //   323: aload #14
    //   325: invokevirtual getBytes : ()[B
    //   328: iconst_0
    //   329: aload #14
    //   331: invokevirtual length : ()I
    //   334: iconst_2
    //   335: isub
    //   336: invokespecial <init> : ([BII)V
    //   339: bipush #16
    //   341: invokestatic parseLong : (Ljava/lang/String;I)J
    //   344: lstore #5
    //   346: goto -> 232
    //   349: lload #5
    //   351: lstore #7
    //   353: goto -> 275
    //   356: lload #5
    //   358: lload #7
    //   360: lsub
    //   361: lstore #11
    //   363: lload #11
    //   365: lstore #9
    //   367: iload_3
    //   368: i2l
    //   369: lload #11
    //   371: lcmp
    //   372: ifge -> 379
    //   375: iload_3
    //   376: i2l
    //   377: lstore #9
    //   379: lload #9
    //   381: l2i
    //   382: istore #4
    //   384: aload #13
    //   386: aload #14
    //   388: iconst_0
    //   389: iload #4
    //   391: invokevirtual read : ([BII)I
    //   394: istore #4
    //   396: iload #4
    //   398: iflt -> 502
    //   401: aload_1
    //   402: aload #14
    //   404: iconst_0
    //   405: iload #4
    //   407: invokevirtual write : ([BII)V
    //   410: lload #7
    //   412: iload #4
    //   414: i2l
    //   415: ladd
    //   416: lstore #7
    //   418: goto -> 494
    //   421: astore #14
    //   423: aload #14
    //   425: invokestatic warning : (Ljava/lang/Exception;)V
    //   428: goto -> 502
    //   431: astore_1
    //   432: aload_1
    //   433: invokestatic warning : (Ljava/lang/Exception;)V
    //   436: iconst_0
    //   437: ireturn
    //   438: lload #5
    //   440: lload #7
    //   442: ladd
    //   443: lstore #7
    //   445: ldc '\\r\\n'
    //   447: invokevirtual length : ()I
    //   450: istore_3
    //   451: lload #7
    //   453: lstore #5
    //   455: lload #7
    //   457: iload_3
    //   458: i2l
    //   459: lcmp
    //   460: iflt -> 288
    //   463: goto -> 311
    //   466: astore #14
    //   468: lconst_0
    //   469: lstore #5
    //   471: goto -> 232
    //   474: lconst_0
    //   475: lstore #5
    //   477: goto -> 232
    //   480: astore_1
    //   481: goto -> 224
    //   484: iconst_0
    //   485: ireturn
    //   486: iconst_1
    //   487: ireturn
    //   488: lconst_0
    //   489: lstore #5
    //   491: goto -> 224
    //   494: lload #7
    //   496: lload #5
    //   498: lcmp
    //   499: iflt -> 356
    //   502: iload_2
    //   503: ifeq -> 474
    //   506: lconst_0
    //   507: lstore #5
    //   509: goto -> 288
    // Exception table:
    //   from	to	target	type
    //   0	17	431	java/lang/Exception
    //   21	28	431	java/lang/Exception
    //   31	59	431	java/lang/Exception
    //   63	70	431	java/lang/Exception
    //   70	77	431	java/lang/Exception
    //   81	93	431	java/lang/Exception
    //   93	100	431	java/lang/Exception
    //   104	111	431	java/lang/Exception
    //   115	122	431	java/lang/Exception
    //   124	145	431	java/lang/Exception
    //   145	152	431	java/lang/Exception
    //   155	176	431	java/lang/Exception
    //   176	183	431	java/lang/Exception
    //   186	191	431	java/lang/Exception
    //   198	205	480	java/lang/Exception
    //   209	220	480	java/lang/Exception
    //   224	232	431	java/lang/Exception
    //   239	248	431	java/lang/Exception
    //   250	256	431	java/lang/Exception
    //   259	263	431	java/lang/Exception
    //   275	282	431	java/lang/Exception
    //   288	304	431	java/lang/Exception
    //   311	346	466	java/lang/Exception
    //   384	396	421	java/lang/Exception
    //   401	410	421	java/lang/Exception
    //   423	428	431	java/lang/Exception
    //   445	451	431	java/lang/Exception
  }
  
  protected boolean set(HTTPSocket paramHTTPSocket) {
    return set(paramHTTPSocket.getInputStream());
  }
  
  public void setCacheControl(int paramInt) {
    setCacheControl("max-age", paramInt);
  }
  
  public void setCacheControl(String paramString) {
    setHeader("Cache-Control", paramString);
  }
  
  public void setCacheControl(String paramString, int paramInt) {
    setHeader("Cache-Control", String.valueOf(paramString) + "=" + Integer.toString(paramInt));
  }
  
  public void setConnection(String paramString) {
    setHeader("Connection", paramString);
  }
  
  public void setContent(String paramString) {
    setContent(paramString, true);
  }
  
  public void setContent(String paramString, boolean paramBoolean) {
    setContent(paramString.getBytes(), paramBoolean);
  }
  
  public void setContent(byte[] paramArrayOfbyte) {
    setContent(paramArrayOfbyte, true);
  }
  
  public void setContent(byte[] paramArrayOfbyte, boolean paramBoolean) {
    this.content = paramArrayOfbyte;
    if (paramBoolean)
      setContentLength(paramArrayOfbyte.length); 
  }
  
  public void setContentInputStream(InputStream paramInputStream) {
    this.contentInput = paramInputStream;
  }
  
  public void setContentLength(long paramLong) {
    setLongHeader("Content-Length", paramLong);
  }
  
  public void setContentRange(long paramLong1, long paramLong2, long paramLong3) {
    String str;
    StringBuilder stringBuilder = new StringBuilder(String.valueOf(String.valueOf(String.valueOf(String.valueOf("") + "bytes ") + Long.toString(paramLong1) + "-") + Long.toString(paramLong2) + "/"));
    if (0L < paramLong3) {
      str = Long.toString(paramLong3);
    } else {
      str = "*";
    } 
    setHeader("Content-Range", stringBuilder.append(str).toString());
  }
  
  public void setContentType(String paramString) {
    setHeader("Content-Type", paramString);
  }
  
  public void setDate(Calendar paramCalendar) {
    setHeader("Date", (new Date(paramCalendar)).getDateString());
  }
  
  public void setHeader(String paramString, int paramInt) {
    setHeader(paramString, Integer.toString(paramInt));
  }
  
  public void setHeader(String paramString, long paramLong) {
    setHeader(paramString, Long.toString(paramLong));
  }
  
  public void setHeader(String paramString1, String paramString2) {
    HTTPHeader hTTPHeader = getHeader(paramString1);
    if (hTTPHeader != null) {
      hTTPHeader.setValue(paramString2);
      return;
    } 
    addHeader(paramString1, paramString2);
  }
  
  public void setHeader(HTTPHeader paramHTTPHeader) {
    setHeader(paramHTTPHeader.getName(), paramHTTPHeader.getValue());
  }
  
  public void setHost(String paramString) {
    String str = paramString;
    if (HostInterface.isIPv6Address(paramString))
      str = "[" + paramString + "]"; 
    setHeader("HOST", str);
  }
  
  public void setHost(String paramString, int paramInt) {
    String str = paramString;
    if (HostInterface.isIPv6Address(paramString))
      str = "[" + paramString + "]"; 
    setHeader("HOST", String.valueOf(str) + ":" + Integer.toString(paramInt));
  }
  
  public void setIntegerHeader(String paramString, int paramInt) {
    setHeader(paramString, Integer.toString(paramInt));
  }
  
  public void setLongHeader(String paramString, long paramLong) {
    setHeader(paramString, Long.toString(paramLong));
  }
  
  public void setServer(String paramString) {
    setHeader("Server", paramString);
  }
  
  public void setStringHeader(String paramString1, String paramString2) {
    setStringHeader(paramString1, paramString2, "\"", "\"");
  }
  
  public void setStringHeader(String paramString1, String paramString2, String paramString3, String paramString4) {
    String str = paramString2;
    paramString2 = str;
    if (!str.startsWith(paramString3))
      paramString2 = String.valueOf(paramString3) + str; 
    paramString3 = paramString2;
    if (!paramString2.endsWith(paramString4))
      paramString3 = String.valueOf(paramString2) + paramString4; 
    setHeader(paramString1, paramString3);
  }
  
  public void setTransferEncoding(String paramString) {
    setHeader("Transfer-Encoding", paramString);
  }
  
  public void setVersion(String paramString) {
    this.version = paramString;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\http\HTTPPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */