package com.samsungimaging.connectionmanager.app.pullservice.util;

import android.os.Environment;
import com.samsungimaging.connectionmanager.util.Trace;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileManager {
  private static Trace.Tag TAG = Trace.Tag.RVF;
  
  public static String createFileName(String paramString) {
    return String.valueOf(getDefaultStorage()) + "/" + renameFile(getDefaultStorage(), paramString);
  }
  
  private static String getDefaultStorage() {
    return String.valueOf(Environment.getExternalStorageDirectory().getPath()) + "/DCIM/Camera/Samsung Smart Camera Application/RemoteViewfinder";
  }
  
  private static String rename(String paramString) {
    Matcher matcher = Pattern.compile("(\\-)(\\d*)+[.]").matcher(paramString);
    if (matcher.find()) {
      int i = Integer.parseInt(matcher.group().replace("-", "").replace(".", ""));
      return paramString.replaceAll("(\\-)(\\d*)+[.]", "-" + (i + 1) + ".");
    } 
    return String.valueOf(paramString.substring(0, paramString.lastIndexOf("."))) + "-0" + paramString.substring(paramString.lastIndexOf("."), paramString.length());
  }
  
  public static String renameFile(String paramString1, String paramString2) {
    Trace.d(TAG, "start renameFile() dir : " + paramString1 + " fileName : " + paramString2);
    File file2 = new File(paramString1, paramString2);
    String str = paramString2;
    for (File file1 = file2;; file1 = new File(paramString1, str)) {
      if (!file1.exists()) {
        Trace.d(TAG, "end renameFile() fileName : " + str);
        return str;
      } 
      str = rename(str);
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservic\\util\FileManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */