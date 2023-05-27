package android.support.v4.widget;

import android.view.View;
import android.widget.ListPopupWindow;

class ListPopupWindowCompatKitKat {
  public static View.OnTouchListener createDragToOpenListener(Object paramObject, View paramView) {
    return ((ListPopupWindow)paramObject).createDragToOpenListener(paramView);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\widget\ListPopupWindowCompatKitKat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */