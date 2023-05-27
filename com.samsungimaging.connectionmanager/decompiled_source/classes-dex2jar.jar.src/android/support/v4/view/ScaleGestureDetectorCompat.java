package android.support.v4.view;

import android.os.Build;

public class ScaleGestureDetectorCompat {
  static final ScaleGestureDetectorImpl IMPL = new BaseScaleGestureDetectorImpl();
  
  public static boolean isQuickScaleEnabled(Object paramObject) {
    return IMPL.isQuickScaleEnabled(paramObject);
  }
  
  public static void setQuickScaleEnabled(Object paramObject, boolean paramBoolean) {
    IMPL.setQuickScaleEnabled(paramObject, paramBoolean);
  }
  
  static {
    if (Build.VERSION.SDK_INT >= 19) {
      IMPL = new ScaleGestureDetectorCompatKitKatImpl();
      return;
    } 
  }
  
  private static class BaseScaleGestureDetectorImpl implements ScaleGestureDetectorImpl {
    private BaseScaleGestureDetectorImpl() {}
    
    public boolean isQuickScaleEnabled(Object param1Object) {
      return false;
    }
    
    public void setQuickScaleEnabled(Object param1Object, boolean param1Boolean) {}
  }
  
  private static class ScaleGestureDetectorCompatKitKatImpl implements ScaleGestureDetectorImpl {
    private ScaleGestureDetectorCompatKitKatImpl() {}
    
    public boolean isQuickScaleEnabled(Object param1Object) {
      return ScaleGestureDetectorCompatKitKat.isQuickScaleEnabled(param1Object);
    }
    
    public void setQuickScaleEnabled(Object param1Object, boolean param1Boolean) {
      ScaleGestureDetectorCompatKitKat.setQuickScaleEnabled(param1Object, param1Boolean);
    }
  }
  
  static interface ScaleGestureDetectorImpl {
    boolean isQuickScaleEnabled(Object param1Object);
    
    void setQuickScaleEnabled(Object param1Object, boolean param1Boolean);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\view\ScaleGestureDetectorCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */