package android.support.v4.widget;

import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

public class PopupWindowCompat {
  static final PopupWindowImpl IMPL = new BasePopupWindowImpl();
  
  public static boolean getOverlapAnchor(PopupWindow paramPopupWindow) {
    return IMPL.getOverlapAnchor(paramPopupWindow);
  }
  
  public static int getWindowLayoutType(PopupWindow paramPopupWindow) {
    return IMPL.getWindowLayoutType(paramPopupWindow);
  }
  
  public static void setOverlapAnchor(PopupWindow paramPopupWindow, boolean paramBoolean) {
    IMPL.setOverlapAnchor(paramPopupWindow, paramBoolean);
  }
  
  public static void setWindowLayoutType(PopupWindow paramPopupWindow, int paramInt) {
    IMPL.setWindowLayoutType(paramPopupWindow, paramInt);
  }
  
  public static void showAsDropDown(PopupWindow paramPopupWindow, View paramView, int paramInt1, int paramInt2, int paramInt3) {
    IMPL.showAsDropDown(paramPopupWindow, paramView, paramInt1, paramInt2, paramInt3);
  }
  
  static {
    int i = Build.VERSION.SDK_INT;
    if (i >= 23) {
      IMPL = new Api23PopupWindowImpl();
      return;
    } 
    if (i >= 21) {
      IMPL = new Api21PopupWindowImpl();
      return;
    } 
    if (i >= 19) {
      IMPL = new KitKatPopupWindowImpl();
      return;
    } 
    if (i >= 9) {
      IMPL = new GingerbreadPopupWindowImpl();
      return;
    } 
  }
  
  static class Api21PopupWindowImpl extends KitKatPopupWindowImpl {
    public boolean getOverlapAnchor(PopupWindow param1PopupWindow) {
      return PopupWindowCompatApi21.getOverlapAnchor(param1PopupWindow);
    }
    
    public void setOverlapAnchor(PopupWindow param1PopupWindow, boolean param1Boolean) {
      PopupWindowCompatApi21.setOverlapAnchor(param1PopupWindow, param1Boolean);
    }
  }
  
  static class Api23PopupWindowImpl extends Api21PopupWindowImpl {
    public boolean getOverlapAnchor(PopupWindow param1PopupWindow) {
      return PopupWindowCompatApi23.getOverlapAnchor(param1PopupWindow);
    }
    
    public int getWindowLayoutType(PopupWindow param1PopupWindow) {
      return PopupWindowCompatApi23.getWindowLayoutType(param1PopupWindow);
    }
    
    public void setOverlapAnchor(PopupWindow param1PopupWindow, boolean param1Boolean) {
      PopupWindowCompatApi23.setOverlapAnchor(param1PopupWindow, param1Boolean);
    }
    
    public void setWindowLayoutType(PopupWindow param1PopupWindow, int param1Int) {
      PopupWindowCompatApi23.setWindowLayoutType(param1PopupWindow, param1Int);
    }
  }
  
  static class BasePopupWindowImpl implements PopupWindowImpl {
    public boolean getOverlapAnchor(PopupWindow param1PopupWindow) {
      return false;
    }
    
    public int getWindowLayoutType(PopupWindow param1PopupWindow) {
      return 0;
    }
    
    public void setOverlapAnchor(PopupWindow param1PopupWindow, boolean param1Boolean) {}
    
    public void setWindowLayoutType(PopupWindow param1PopupWindow, int param1Int) {}
    
    public void showAsDropDown(PopupWindow param1PopupWindow, View param1View, int param1Int1, int param1Int2, int param1Int3) {
      param1PopupWindow.showAsDropDown(param1View, param1Int1, param1Int2);
    }
  }
  
  static class GingerbreadPopupWindowImpl extends BasePopupWindowImpl {
    public int getWindowLayoutType(PopupWindow param1PopupWindow) {
      return PopupWindowCompatGingerbread.getWindowLayoutType(param1PopupWindow);
    }
    
    public void setWindowLayoutType(PopupWindow param1PopupWindow, int param1Int) {
      PopupWindowCompatGingerbread.setWindowLayoutType(param1PopupWindow, param1Int);
    }
  }
  
  static class KitKatPopupWindowImpl extends GingerbreadPopupWindowImpl {
    public void showAsDropDown(PopupWindow param1PopupWindow, View param1View, int param1Int1, int param1Int2, int param1Int3) {
      PopupWindowCompatKitKat.showAsDropDown(param1PopupWindow, param1View, param1Int1, param1Int2, param1Int3);
    }
  }
  
  static interface PopupWindowImpl {
    boolean getOverlapAnchor(PopupWindow param1PopupWindow);
    
    int getWindowLayoutType(PopupWindow param1PopupWindow);
    
    void setOverlapAnchor(PopupWindow param1PopupWindow, boolean param1Boolean);
    
    void setWindowLayoutType(PopupWindow param1PopupWindow, int param1Int);
    
    void showAsDropDown(PopupWindow param1PopupWindow, View param1View, int param1Int1, int param1Int2, int param1Int3);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\widget\PopupWindowCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */