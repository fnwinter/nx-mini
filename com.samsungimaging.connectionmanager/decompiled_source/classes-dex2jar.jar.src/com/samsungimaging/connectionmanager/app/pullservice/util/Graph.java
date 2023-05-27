package com.samsungimaging.connectionmanager.app.pullservice.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Graph {
  public static Bitmap getThumbnail(String paramString) {
    try {
      BitmapFactory.Options options;
      ExifInterface exifInterface = new ExifInterface(paramString);
      if (exifInterface.getThumbnail() == null) {
        options = new BitmapFactory.Options();
        options.inSampleSize = 16;
        return BitmapFactory.decodeFile(paramString, options);
      } 
      return BitmapFactory.decodeByteArray(options.getThumbnail(), 0, (options.getThumbnail()).length);
    } catch (IOException iOException) {
      iOException.printStackTrace();
      return null;
    } 
  }
  
  public static void saveBitmapToJPEGFile(Bitmap paramBitmap, String paramString) {
    try {
      FileOutputStream fileOutputStream = new FileOutputStream(paramString);
      paramBitmap.compress(Bitmap.CompressFormat.JPEG, 0, fileOutputStream);
      return;
    } catch (FileNotFoundException fileNotFoundException) {
      fileNotFoundException.printStackTrace();
      return;
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservic\\util\Graph.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */