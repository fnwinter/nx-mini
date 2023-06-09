package android.support.v4.widget;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.view.View;
import android.widget.SearchView;

class SearchViewCompatHoneycomb {
  public static CharSequence getQuery(View paramView) {
    return ((SearchView)paramView).getQuery();
  }
  
  public static boolean isIconified(View paramView) {
    return ((SearchView)paramView).isIconified();
  }
  
  public static boolean isQueryRefinementEnabled(View paramView) {
    return ((SearchView)paramView).isQueryRefinementEnabled();
  }
  
  public static boolean isSubmitButtonEnabled(View paramView) {
    return ((SearchView)paramView).isSubmitButtonEnabled();
  }
  
  public static Object newOnCloseListener(final OnCloseListenerCompatBridge listener) {
    return new SearchView.OnCloseListener() {
        public boolean onClose() {
          return listener.onClose();
        }
      };
  }
  
  public static Object newOnQueryTextListener(final OnQueryTextListenerCompatBridge listener) {
    return new SearchView.OnQueryTextListener() {
        public boolean onQueryTextChange(String param1String) {
          return listener.onQueryTextChange(param1String);
        }
        
        public boolean onQueryTextSubmit(String param1String) {
          return listener.onQueryTextSubmit(param1String);
        }
      };
  }
  
  public static View newSearchView(Context paramContext) {
    return (View)new SearchView(paramContext);
  }
  
  public static void setIconified(View paramView, boolean paramBoolean) {
    ((SearchView)paramView).setIconified(paramBoolean);
  }
  
  public static void setMaxWidth(View paramView, int paramInt) {
    ((SearchView)paramView).setMaxWidth(paramInt);
  }
  
  public static void setOnCloseListener(Object paramObject1, Object paramObject2) {
    ((SearchView)paramObject1).setOnCloseListener((SearchView.OnCloseListener)paramObject2);
  }
  
  public static void setOnQueryTextListener(Object paramObject1, Object paramObject2) {
    ((SearchView)paramObject1).setOnQueryTextListener((SearchView.OnQueryTextListener)paramObject2);
  }
  
  public static void setQuery(View paramView, CharSequence paramCharSequence, boolean paramBoolean) {
    ((SearchView)paramView).setQuery(paramCharSequence, paramBoolean);
  }
  
  public static void setQueryHint(View paramView, CharSequence paramCharSequence) {
    ((SearchView)paramView).setQueryHint(paramCharSequence);
  }
  
  public static void setQueryRefinementEnabled(View paramView, boolean paramBoolean) {
    ((SearchView)paramView).setQueryRefinementEnabled(paramBoolean);
  }
  
  public static void setSearchableInfo(View paramView, ComponentName paramComponentName) {
    SearchView searchView = (SearchView)paramView;
    searchView.setSearchableInfo(((SearchManager)searchView.getContext().getSystemService("search")).getSearchableInfo(paramComponentName));
  }
  
  public static void setSubmitButtonEnabled(View paramView, boolean paramBoolean) {
    ((SearchView)paramView).setSubmitButtonEnabled(paramBoolean);
  }
  
  static interface OnCloseListenerCompatBridge {
    boolean onClose();
  }
  
  static interface OnQueryTextListenerCompatBridge {
    boolean onQueryTextChange(String param1String);
    
    boolean onQueryTextSubmit(String param1String);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\widget\SearchViewCompatHoneycomb.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */