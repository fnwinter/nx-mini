package android.support.v4.view;

import android.os.Build;
import android.view.LayoutInflater;

public class LayoutInflaterCompat {
  static final LayoutInflaterCompatImpl IMPL = new LayoutInflaterCompatImplBase();
  
  public static void setFactory(LayoutInflater paramLayoutInflater, LayoutInflaterFactory paramLayoutInflaterFactory) {
    IMPL.setFactory(paramLayoutInflater, paramLayoutInflaterFactory);
  }
  
  static {
    int i = Build.VERSION.SDK_INT;
    if (i >= 21) {
      IMPL = new LayoutInflaterCompatImplV21();
      return;
    } 
    if (i >= 11) {
      IMPL = new LayoutInflaterCompatImplV11();
      return;
    } 
  }
  
  static interface LayoutInflaterCompatImpl {
    void setFactory(LayoutInflater param1LayoutInflater, LayoutInflaterFactory param1LayoutInflaterFactory);
  }
  
  static class LayoutInflaterCompatImplBase implements LayoutInflaterCompatImpl {
    public void setFactory(LayoutInflater param1LayoutInflater, LayoutInflaterFactory param1LayoutInflaterFactory) {
      LayoutInflaterCompatBase.setFactory(param1LayoutInflater, param1LayoutInflaterFactory);
    }
  }
  
  static class LayoutInflaterCompatImplV11 extends LayoutInflaterCompatImplBase {
    public void setFactory(LayoutInflater param1LayoutInflater, LayoutInflaterFactory param1LayoutInflaterFactory) {
      LayoutInflaterCompatHC.setFactory(param1LayoutInflater, param1LayoutInflaterFactory);
    }
  }
  
  static class LayoutInflaterCompatImplV21 extends LayoutInflaterCompatImplV11 {
    public void setFactory(LayoutInflater param1LayoutInflater, LayoutInflaterFactory param1LayoutInflaterFactory) {
      LayoutInflaterCompatLollipop.setFactory(param1LayoutInflater, param1LayoutInflaterFactory);
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\view\LayoutInflaterCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */