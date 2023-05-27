package com.samsungimaging.connectionmanager.app.pushservice.selectivepush;

import com.samsungimaging.connectionmanager.util.Trace;
import java.lang.reflect.Array;

public class HeaderParser {
  private static final Trace.Tag TAG = Trace.Tag.SP;
  
  String[][] headerArray;
  
  int line_cnt = 0;
  
  String mFileCommand = null;
  
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
    int j = 3;
    this.line_cnt = arrayOfString.length;
    Trace.d(TAG, "HeaderParser Total Line : " + this.line_cnt);
    this.headerArray = (String[][])Array.newInstance(String.class, new int[] { arrayOfString.length, 2 });
    for (int i = 1;; i++) {
      if (i >= arrayOfString.length) {
        arrayOfString = arrayOfString[0].split(" ");
        i = 0;
        while (true) {
          if (i >= arrayOfString.length) {
            i = j;
          } else if (arrayOfString[i].contains("DCIM")) {
            j = arrayOfString[i].lastIndexOf("/");
            this.mFileName = arrayOfString[i].substring(j + 1);
            Trace.d(TAG, "parsed file name : " + this.mFileName);
            i = 3 - 1;
          } else {
            i++;
            continue;
          } 
          j = 1;
          while (true) {
            if (j < this.line_cnt)
              if (this.headerArray[j][0].contains("Content-Length")) {
                this.mFileSize = this.headerArray[j][1];
                i--;
                Trace.d(TAG, "parsed file size: " + this.mFileSize);
              } else {
                j++;
                continue;
              }  
            j = 1;
            while (true) {
              if (j < this.line_cnt)
                if (this.headerArray[j][0].equals("Expect")) {
                  i--;
                } else {
                  j++;
                  continue;
                }  
              if (i == 0)
                this.mHeaderParsed = 1; 
              for (i = 1;; i++) {
                if (i >= this.line_cnt)
                  return; 
                if (this.headerArray[i][0].contains("Command")) {
                  this.mFileCommand = this.headerArray[i][1].trim();
                  if (this.mFileCommand.equals("Getout")) {
                    this.mHeaderParsed = 3;
                    Trace.d(TAG, "parsed Command: " + this.mHeaderParsed);
                    return;
                  } 
                } 
              } 
              break;
            } 
            break;
          } 
          break;
        } 
      } 
      int k = arrayOfString[i].indexOf(":");
      this.headerArray[i][0] = arrayOfString[i].substring(0, k);
      this.headerArray[i][1] = arrayOfString[i].substring(k + 1);
      Trace.d(TAG, "headerArray[" + i + "][pre] : " + this.headerArray[i][0]);
      Trace.d(TAG, "headerArray[" + i + "][post] : " + this.headerArray[i][1]);
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pushservice\selectivepush\HeaderParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */