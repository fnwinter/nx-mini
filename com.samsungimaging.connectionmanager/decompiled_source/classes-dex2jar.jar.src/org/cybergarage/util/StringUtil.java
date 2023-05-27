package org.cybergarage.util;

public final class StringUtil {
  public static final int findFirstNotOf(String paramString1, String paramString2) {
    return findOf(paramString1, paramString2, 0, paramString1.length() - 1, 1, false);
  }
  
  public static final int findFirstOf(String paramString1, String paramString2) {
    return findOf(paramString1, paramString2, 0, paramString1.length() - 1, 1, true);
  }
  
  public static final int findLastNotOf(String paramString1, String paramString2) {
    return findOf(paramString1, paramString2, paramString1.length() - 1, 0, -1, false);
  }
  
  public static final int findLastOf(String paramString1, String paramString2) {
    return findOf(paramString1, paramString2, paramString1.length() - 1, 0, -1, true);
  }
  
  public static final int findOf(String paramString1, String paramString2, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    if (paramInt3 == 0)
      return -1; 
    int i = paramString2.length();
    label27: while (true) {
      if ((paramInt3 > 0) ? (paramInt2 < paramInt1) : (paramInt1 < paramInt2))
        return -1; 
      char c = paramString1.charAt(paramInt1);
      int j = 0;
      for (int k = 0;; k++) {
        if (k >= i) {
          paramInt1 += paramInt3;
          continue label27;
        } 
        char c1 = paramString2.charAt(k);
        if (paramBoolean) {
          int n = paramInt1;
          if (c != c1)
            continue; 
          return n;
        } 
        int m = j;
        if (c != c1)
          m = j + 1; 
        j = m;
        if (m == i)
          return paramInt1; 
        continue;
      } 
      break;
    } 
  }
  
  public static final boolean hasData(String paramString) {
    return (paramString != null && paramString.length() > 0);
  }
  
  public static final int toInteger(String paramString) {
    try {
      return Integer.parseInt(paramString);
    } catch (Exception exception) {
      Debug.warning(exception);
      return 0;
    } 
  }
  
  public static final long toLong(String paramString) {
    try {
      return Long.parseLong(paramString);
    } catch (Exception exception) {
      Debug.warning(exception);
      return 0L;
    } 
  }
  
  public static final String trim(String paramString1, String paramString2) {
    int i = findFirstNotOf(paramString1, paramString2);
    if (i < 0)
      return paramString1; 
    paramString1 = paramString1.substring(i, paramString1.length());
    i = findLastNotOf(paramString1, paramString2);
    return (i < 0) ? paramString1 : paramString1.substring(0, i + 1);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\util\StringUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */