package com.samsungimaging.connectionmanager.app.pullservice.demo.ml;

import android.graphics.Bitmap;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public class MemoryCache {
  private HashMap<String, SoftReference<Bitmap>> cache = new HashMap<String, SoftReference<Bitmap>>();
  
  public void clear() {
    this.cache.clear();
  }
  
  public Bitmap get(String paramString) {
    if (this.cache.containsKey(paramString)) {
      SoftReference<Bitmap> softReference = this.cache.get(paramString);
      if (softReference != null)
        return softReference.get(); 
    } 
    return null;
  }
  
  public void put(String paramString, Bitmap paramBitmap) {
    this.cache.put(paramString, new SoftReference<Bitmap>(paramBitmap));
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\ml\MemoryCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */