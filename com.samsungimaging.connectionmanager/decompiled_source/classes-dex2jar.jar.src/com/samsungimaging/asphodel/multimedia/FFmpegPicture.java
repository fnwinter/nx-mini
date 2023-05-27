package com.samsungimaging.asphodel.multimedia;

import android.graphics.Bitmap;
import com.samsungimaging.connectionmanager.util.Trace;

class FFmpegPicture {
  private static final String LOG_TAG = "[FFmpegPicture]";
  
  private Bitmap m_bitmap = null;
  
  public FFmpegPicture(int paramInt1, int paramInt2, Bitmap.Config paramConfig) {
    Trace.d(Trace.Tag.FFMPEG, "picture creation");
    this.m_bitmap = Bitmap.createBitmap(paramInt1, paramInt2, paramConfig);
  }
  
  public Bitmap getPicture() {
    return this.m_bitmap;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\asphodel\multimedia\FFmpegPicture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */