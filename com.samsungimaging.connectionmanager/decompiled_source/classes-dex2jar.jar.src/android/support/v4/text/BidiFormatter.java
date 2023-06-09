package android.support.v4.text;

import java.util.Locale;

public final class BidiFormatter {
  private static final int DEFAULT_FLAGS = 2;
  
  private static final BidiFormatter DEFAULT_LTR_INSTANCE;
  
  private static final BidiFormatter DEFAULT_RTL_INSTANCE;
  
  private static TextDirectionHeuristicCompat DEFAULT_TEXT_DIRECTION_HEURISTIC = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
  
  private static final int DIR_LTR = -1;
  
  private static final int DIR_RTL = 1;
  
  private static final int DIR_UNKNOWN = 0;
  
  private static final String EMPTY_STRING = "";
  
  private static final int FLAG_STEREO_RESET = 2;
  
  private static final char LRE = '‪';
  
  private static final char LRM = '‎';
  
  private static final String LRM_STRING = Character.toString('‎');
  
  private static final char PDF = '‬';
  
  private static final char RLE = '‫';
  
  private static final char RLM = '‏';
  
  private static final String RLM_STRING = Character.toString('‏');
  
  private final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
  
  private final int mFlags;
  
  private final boolean mIsRtlContext;
  
  static {
    DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
    DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, DEFAULT_TEXT_DIRECTION_HEURISTIC);
  }
  
  private BidiFormatter(boolean paramBoolean, int paramInt, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat) {
    this.mIsRtlContext = paramBoolean;
    this.mFlags = paramInt;
    this.mDefaultTextDirectionHeuristicCompat = paramTextDirectionHeuristicCompat;
  }
  
  private static int getEntryDir(String paramString) {
    return (new DirectionalityEstimator(paramString, false)).getEntryDir();
  }
  
  private static int getExitDir(String paramString) {
    return (new DirectionalityEstimator(paramString, false)).getExitDir();
  }
  
  public static BidiFormatter getInstance() {
    return (new Builder()).build();
  }
  
  public static BidiFormatter getInstance(Locale paramLocale) {
    return (new Builder(paramLocale)).build();
  }
  
  public static BidiFormatter getInstance(boolean paramBoolean) {
    return (new Builder(paramBoolean)).build();
  }
  
  private static boolean isRtlLocale(Locale paramLocale) {
    return (TextUtilsCompat.getLayoutDirectionFromLocale(paramLocale) == 1);
  }
  
  private String markAfter(String paramString, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat) {
    boolean bool = paramTextDirectionHeuristicCompat.isRtl(paramString, 0, paramString.length());
    return (!this.mIsRtlContext && (bool || getExitDir(paramString) == 1)) ? LRM_STRING : ((this.mIsRtlContext && (!bool || getExitDir(paramString) == -1)) ? RLM_STRING : "");
  }
  
  private String markBefore(String paramString, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat) {
    boolean bool = paramTextDirectionHeuristicCompat.isRtl(paramString, 0, paramString.length());
    return (!this.mIsRtlContext && (bool || getEntryDir(paramString) == 1)) ? LRM_STRING : ((this.mIsRtlContext && (!bool || getEntryDir(paramString) == -1)) ? RLM_STRING : "");
  }
  
  public boolean getStereoReset() {
    return ((this.mFlags & 0x2) != 0);
  }
  
  public boolean isRtl(String paramString) {
    return this.mDefaultTextDirectionHeuristicCompat.isRtl(paramString, 0, paramString.length());
  }
  
  public boolean isRtlContext() {
    return this.mIsRtlContext;
  }
  
  public String unicodeWrap(String paramString) {
    return unicodeWrap(paramString, this.mDefaultTextDirectionHeuristicCompat, true);
  }
  
  public String unicodeWrap(String paramString, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat) {
    return unicodeWrap(paramString, paramTextDirectionHeuristicCompat, true);
  }
  
  public String unicodeWrap(String paramString, TextDirectionHeuristicCompat paramTextDirectionHeuristicCompat, boolean paramBoolean) {
    if (paramString == null)
      return null; 
    boolean bool = paramTextDirectionHeuristicCompat.isRtl(paramString, 0, paramString.length());
    StringBuilder stringBuilder = new StringBuilder();
    if (getStereoReset() && paramBoolean) {
      if (bool) {
        paramTextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.RTL;
      } else {
        paramTextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.LTR;
      } 
      stringBuilder.append(markBefore(paramString, paramTextDirectionHeuristicCompat));
    } 
    if (bool != this.mIsRtlContext) {
      char c;
      if (bool) {
        c = '‫';
      } else {
        c = '‪';
      } 
      stringBuilder.append(c);
      stringBuilder.append(paramString);
      stringBuilder.append('‬');
    } else {
      stringBuilder.append(paramString);
    } 
    if (paramBoolean) {
      if (bool) {
        paramTextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.RTL;
      } else {
        paramTextDirectionHeuristicCompat = TextDirectionHeuristicsCompat.LTR;
      } 
      stringBuilder.append(markAfter(paramString, paramTextDirectionHeuristicCompat));
    } 
    return stringBuilder.toString();
  }
  
  public String unicodeWrap(String paramString, boolean paramBoolean) {
    return unicodeWrap(paramString, this.mDefaultTextDirectionHeuristicCompat, paramBoolean);
  }
  
  public static final class Builder {
    private int mFlags;
    
    private boolean mIsRtlContext;
    
    private TextDirectionHeuristicCompat mTextDirectionHeuristicCompat;
    
    public Builder() {
      initialize(BidiFormatter.isRtlLocale(Locale.getDefault()));
    }
    
    public Builder(Locale param1Locale) {
      initialize(BidiFormatter.isRtlLocale(param1Locale));
    }
    
    public Builder(boolean param1Boolean) {
      initialize(param1Boolean);
    }
    
    private static BidiFormatter getDefaultInstanceFromContext(boolean param1Boolean) {
      return param1Boolean ? BidiFormatter.DEFAULT_RTL_INSTANCE : BidiFormatter.DEFAULT_LTR_INSTANCE;
    }
    
    private void initialize(boolean param1Boolean) {
      this.mIsRtlContext = param1Boolean;
      this.mTextDirectionHeuristicCompat = BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC;
      this.mFlags = 2;
    }
    
    public BidiFormatter build() {
      return (this.mFlags == 2 && this.mTextDirectionHeuristicCompat == BidiFormatter.DEFAULT_TEXT_DIRECTION_HEURISTIC) ? getDefaultInstanceFromContext(this.mIsRtlContext) : new BidiFormatter(this.mIsRtlContext, this.mFlags, this.mTextDirectionHeuristicCompat);
    }
    
    public Builder setTextDirectionHeuristic(TextDirectionHeuristicCompat param1TextDirectionHeuristicCompat) {
      this.mTextDirectionHeuristicCompat = param1TextDirectionHeuristicCompat;
      return this;
    }
    
    public Builder stereoReset(boolean param1Boolean) {
      if (param1Boolean) {
        this.mFlags |= 0x2;
        return this;
      } 
      this.mFlags &= 0xFFFFFFFD;
      return this;
    }
  }
  
  private static class DirectionalityEstimator {
    private static final byte[] DIR_TYPE_CACHE = new byte[1792];
    
    private static final int DIR_TYPE_CACHE_SIZE = 1792;
    
    private int charIndex;
    
    private final boolean isHtml;
    
    private char lastChar;
    
    private final int length;
    
    private final String text;
    
    static {
      for (int i = 0; i < 1792; i++)
        DIR_TYPE_CACHE[i] = Character.getDirectionality(i); 
    }
    
    DirectionalityEstimator(String param1String, boolean param1Boolean) {
      this.text = param1String;
      this.isHtml = param1Boolean;
      this.length = param1String.length();
    }
    
    private static byte getCachedDirectionality(char param1Char) {
      return (param1Char < '܀') ? DIR_TYPE_CACHE[param1Char] : Character.getDirectionality(param1Char);
    }
    
    private byte skipEntityBackward() {
      int i = this.charIndex;
      while (this.charIndex > 0) {
        String str = this.text;
        int j = this.charIndex - 1;
        this.charIndex = j;
        this.lastChar = str.charAt(j);
        if (this.lastChar == '&')
          return 12; 
        if (this.lastChar == ';')
          break; 
      } 
      this.charIndex = i;
      this.lastChar = ';';
      return 13;
    }
    
    private byte skipEntityForward() {
      while (this.charIndex < this.length) {
        String str = this.text;
        int i = this.charIndex;
        this.charIndex = i + 1;
        char c = str.charAt(i);
        this.lastChar = c;
        if (c == ';')
          break; 
      } 
      return 12;
    }
    
    private byte skipTagBackward() {
      int i = this.charIndex;
      while (true) {
        if (this.charIndex > 0) {
          String str = this.text;
          int j = this.charIndex - 1;
          this.charIndex = j;
          this.lastChar = str.charAt(j);
          if (this.lastChar == '<')
            return 12; 
          if (this.lastChar != '>') {
            if (this.lastChar == '"' || this.lastChar == '\'') {
              j = this.lastChar;
              while (this.charIndex > 0) {
                str = this.text;
                int k = this.charIndex - 1;
                this.charIndex = k;
                char c = str.charAt(k);
                this.lastChar = c;
                if (c != j);
              } 
            } 
            continue;
          } 
        } 
        this.charIndex = i;
        this.lastChar = '>';
        return 13;
      } 
    }
    
    private byte skipTagForward() {
      int i = this.charIndex;
      while (this.charIndex < this.length) {
        String str = this.text;
        int j = this.charIndex;
        this.charIndex = j + 1;
        this.lastChar = str.charAt(j);
        if (this.lastChar == '>')
          return 12; 
        if (this.lastChar == '"' || this.lastChar == '\'') {
          j = this.lastChar;
          while (this.charIndex < this.length) {
            str = this.text;
            int k = this.charIndex;
            this.charIndex = k + 1;
            char c = str.charAt(k);
            this.lastChar = c;
            if (c != j);
          } 
        } 
      } 
      this.charIndex = i;
      this.lastChar = '<';
      return 13;
    }
    
    byte dirTypeBackward() {
      this.lastChar = this.text.charAt(this.charIndex - 1);
      if (Character.isLowSurrogate(this.lastChar)) {
        int i = Character.codePointBefore(this.text, this.charIndex);
        this.charIndex -= Character.charCount(i);
        return Character.getDirectionality(i);
      } 
      this.charIndex--;
      byte b1 = getCachedDirectionality(this.lastChar);
      byte b = b1;
      if (this.isHtml) {
        if (this.lastChar == '>')
          return skipTagBackward(); 
        b = b1;
        if (this.lastChar == ';')
          return skipEntityBackward(); 
      } 
      return b;
    }
    
    byte dirTypeForward() {
      this.lastChar = this.text.charAt(this.charIndex);
      if (Character.isHighSurrogate(this.lastChar)) {
        int i = Character.codePointAt(this.text, this.charIndex);
        this.charIndex += Character.charCount(i);
        return Character.getDirectionality(i);
      } 
      this.charIndex++;
      byte b1 = getCachedDirectionality(this.lastChar);
      byte b = b1;
      if (this.isHtml) {
        if (this.lastChar == '<')
          return skipTagForward(); 
        b = b1;
        if (this.lastChar == '&')
          return skipEntityForward(); 
      } 
      return b;
    }
    
    int getEntryDir() {
      this.charIndex = 0;
      int i = 0;
      byte b1 = 0;
      int j;
      for (j = 0; this.charIndex < this.length && !j; j = i) {
        switch (dirTypeForward()) {
          case 9:
            continue;
          default:
            j = i;
            continue;
          case 14:
          case 15:
            i++;
            b1 = -1;
            continue;
          case 16:
          case 17:
            i++;
            b1 = 1;
            continue;
          case 18:
            i--;
            b1 = 0;
            continue;
          case 0:
            if (i == 0)
              return -1; 
            j = i;
            continue;
          case 1:
          case 2:
            break;
        } 
        if (i == 0)
          return 1; 
      } 
      if (j == 0)
        return 0; 
      byte b2 = b1;
      if (b1 == 0) {
        while (this.charIndex > 0) {
          switch (dirTypeBackward()) {
            case 14:
            case 15:
              if (j == i)
                return -1; 
              i--;
              break;
            case 16:
            case 17:
              if (j == i)
                return 1; 
              i--;
              break;
            case 18:
              i++;
              break;
          } 
        } 
        return 0;
      } 
      return b2;
    }
    
    int getExitDir() {
      this.charIndex = this.length;
      int i = 0;
      int j = 0;
      while (this.charIndex > 0) {
        switch (dirTypeBackward()) {
          case 9:
            break;
          case 0:
            if (i) {
              if (!j)
                j = i; 
              break;
            } 
            return -1;
          case 14:
          case 15:
            if (j != i) {
              i--;
              break;
            } 
            return -1;
          case 1:
          case 2:
            if (i == 0)
              return 1; 
            if (j == 0)
              j = i; 
            break;
          case 16:
          case 17:
            if (j == i)
              return 1; 
            i--;
            break;
          case 18:
            i++;
            break;
        } 
      } 
      return 0;
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\text\BidiFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */