package android.support.v4.view;

import android.os.Build;
import android.view.ViewConfiguration;

public class ViewConfigurationCompat {
  static final ViewConfigurationVersionImpl IMPL = new BaseViewConfigurationVersionImpl();
  
  public static int getScaledPagingTouchSlop(ViewConfiguration paramViewConfiguration) {
    return IMPL.getScaledPagingTouchSlop(paramViewConfiguration);
  }
  
  public static boolean hasPermanentMenuKey(ViewConfiguration paramViewConfiguration) {
    return IMPL.hasPermanentMenuKey(paramViewConfiguration);
  }
  
  static {
    if (Build.VERSION.SDK_INT >= 14) {
      IMPL = new IcsViewConfigurationVersionImpl();
      return;
    } 
    if (Build.VERSION.SDK_INT >= 11) {
      IMPL = new HoneycombViewConfigurationVersionImpl();
      return;
    } 
    if (Build.VERSION.SDK_INT >= 8) {
      IMPL = new FroyoViewConfigurationVersionImpl();
      return;
    } 
  }
  
  static class BaseViewConfigurationVersionImpl implements ViewConfigurationVersionImpl {
    public int getScaledPagingTouchSlop(ViewConfiguration param1ViewConfiguration) {
      return param1ViewConfiguration.getScaledTouchSlop();
    }
    
    public boolean hasPermanentMenuKey(ViewConfiguration param1ViewConfiguration) {
      return true;
    }
  }
  
  static class FroyoViewConfigurationVersionImpl extends BaseViewConfigurationVersionImpl {
    public int getScaledPagingTouchSlop(ViewConfiguration param1ViewConfiguration) {
      return ViewConfigurationCompatFroyo.getScaledPagingTouchSlop(param1ViewConfiguration);
    }
  }
  
  static class HoneycombViewConfigurationVersionImpl extends FroyoViewConfigurationVersionImpl {
    public boolean hasPermanentMenuKey(ViewConfiguration param1ViewConfiguration) {
      return false;
    }
  }
  
  static class IcsViewConfigurationVersionImpl extends HoneycombViewConfigurationVersionImpl {
    public boolean hasPermanentMenuKey(ViewConfiguration param1ViewConfiguration) {
      return ViewConfigurationCompatICS.hasPermanentMenuKey(param1ViewConfiguration);
    }
  }
  
  static interface ViewConfigurationVersionImpl {
    int getScaledPagingTouchSlop(ViewConfiguration param1ViewConfiguration);
    
    boolean hasPermanentMenuKey(ViewConfiguration param1ViewConfiguration);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\view\ViewConfigurationCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */