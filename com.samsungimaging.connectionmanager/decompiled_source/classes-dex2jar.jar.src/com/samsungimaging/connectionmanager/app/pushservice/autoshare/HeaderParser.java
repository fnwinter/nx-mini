package com.samsungimaging.connectionmanager.app.pushservice.autoshare;

import com.samsungimaging.connectionmanager.util.Trace;
import java.lang.reflect.Array;

public class HeaderParser {
  private static final Trace.Tag TAG = Trace.Tag.AS;
  
  String[][] headerArray;
  
  int line_cnt = 0;
  
  String mFileName = null;
  
  String mFileSize = null;
  
  int mHeaderParsed = 2;
  
  HeaderParser(String paramString) {
    parse(paramString);
  }
  
  public String getContentLength() {
    Trace.d(TAG, "HeaderParser getHeaderParsed : " + this.mFileSize);
    return this.mFileSize.trim();
  }
  
  public String getFileName() {
    Trace.d(TAG, "HeaderParser getHeaderParsed : " + this.mFileName);
    return this.mFileName.trim();
  }
  
  public int getHeaderParsed() {
    Trace.d(TAG, "HeaderParser getHeaderParsed : " + this.mHeaderParsed);
    return this.mHeaderParsed;
  }
  
  public String getSpecificValue(String paramString) {
    String str = null;
    for (int i = 1;; i++) {
      if (i > this.line_cnt) {
        paramString = str;
        return paramString.trim();
      } 
      if (this.headerArray[i][0].contains(paramString)) {
        str = this.headerArray[i][1];
        Trace.d(TAG, "getSpecificValue [" + paramString + "] : " + str);
        paramString = str;
        return paramString.trim();
      } 
    } 
  }
  
  void parse(String paramString) {
    String[] arrayOfString = paramString.split("\r\n");
    int j = 2;
    this.line_cnt = arrayOfString.length;
    Trace.d(TAG, "HeaderParser Total Line : " + this.line_cnt);
    this.headerArray = (String[][])Array.newInstance(String.class, new int[] { arrayOfString.length, 2 });
    for (int i = 1;; i++) {
      if (i >= arrayOfString.length) {
        int m = arrayOfString[0].lastIndexOf("/") + 1;
        i = j;
        if (m > 0) {
          i = 2 - 1;
          this.mFileName = arrayOfString[0].substring(m);
          Trace.d(TAG, "parsed file name : " + this.mFileName);
        } 
        j = 1;
        while (true) {
          if (j < this.line_cnt)
            if (this.headerArray[j][0].contains("Content-length")) {
              this.mFileSize = this.headerArray[j][1];
              i--;
              Trace.d(TAG, "parsed file size: " + this.mFileSize);
            } else {
              j++;
              continue;
            }  
          if (i == 0)
            this.mHeaderParsed = 1; 
          return;
        } 
        break;
      } 
      int k = arrayOfString[i].indexOf(":");
      this.headerArray[i][0] = arrayOfString[i].substring(0, k);
      this.headerArray[i][1] = arrayOfString[i].substring(k + 1);
      Trace.d(TAG, "headerArray[" + i + "][pre] : " + this.headerArray[i][0]);
      Trace.d(TAG, "headerArray[" + i + "][post] : " + this.headerArray[i][1]);
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pushservice\autoshare\HeaderParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */