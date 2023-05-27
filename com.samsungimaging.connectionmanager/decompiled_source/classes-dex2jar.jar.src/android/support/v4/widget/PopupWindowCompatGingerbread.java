package android.support.v4.widget;

import android.widget.PopupWindow;
import java.lang.reflect.Method;

class PopupWindowCompatGingerbread {
  private static Method sGetWindowLayoutTypeMethod;
  
  private static boolean sGetWindowLayoutTypeMethodAttempted;
  
  private static Method sSetWindowLayoutTypeMethod;
  
  private static boolean sSetWindowLayoutTypeMethodAttempted;
  
  static int getWindowLayoutType(PopupWindow paramPopupWindow) {
    if (!sGetWindowLayoutTypeMethodAttempted) {
      try {
        sGetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("getWindowLayoutType", new Class[0]);
        sGetWindowLayoutTypeMethod.setAccessible(true);
      } catch (Exception exception) {}
      sGetWindowLayoutTypeMethodAttempted = true;
    } 
    if (sGetWindowLayoutTypeMethod != null)
      try {
        return ((Integer)sGetWindowLayoutTypeMethod.invoke(paramPopupWindow, new Object[0])).intValue();
      } catch (Exception exception) {} 
    return 0;
  }
  
  static void setWindowLayoutType(PopupWindow paramPopupWindow, int paramInt) {
    if (!sSetWindowLayoutTypeMethodAttempted) {
      try {
        sSetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("setWindowLayoutType", new Class[] { int.class });
        sSetWindowLayoutTypeMethod.setAccessible(true);
      } catch (Exception exception) {}
      sSetWindowLayoutTypeMethodAttempted = true;
    } 
    if (sSetWindowLayoutTypeMethod != null)
      try {
        sSetWindowLayoutTypeMethod.invoke(paramPopupWindow, new Object[] { Integer.valueOf(paramInt) });
        return;
      } catch (Exception exception) {
        return;
      }  
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\widget\PopupWindowCompatGingerbread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */