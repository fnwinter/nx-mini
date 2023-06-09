package android.support.v4.widget;

import android.content.Context;
import android.view.View;
import android.widget.SearchView;

class SearchViewCompatIcs {
  public static View newSearchView(Context paramContext) {
    return (View)new MySearchView(paramContext);
  }
  
  public static void setImeOptions(View paramView, int paramInt) {
    ((SearchView)paramView).setImeOptions(paramInt);
  }
  
  public static void setInputType(View paramView, int paramInt) {
    ((SearchView)paramView).setInputType(paramInt);
  }
  
  public static class MySearchView extends SearchView {
    public MySearchView(Context param1Context) {
      super(param1Context);
    }
    
    public void onActionViewCollapsed() {
      setQuery("", false);
      super.onActionViewCollapsed();
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\widget\SearchViewCompatIcs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */