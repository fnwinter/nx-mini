package android.support.v7.view.menu;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.appcompat.R;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import java.util.ArrayList;

public class ListMenuPresenter implements MenuPresenter, AdapterView.OnItemClickListener {
  private static final String TAG = "ListMenuPresenter";
  
  public static final String VIEWS_TAG = "android:menu:list";
  
  MenuAdapter mAdapter;
  
  private MenuPresenter.Callback mCallback;
  
  Context mContext;
  
  private int mId;
  
  LayoutInflater mInflater;
  
  private int mItemIndexOffset;
  
  int mItemLayoutRes;
  
  MenuBuilder mMenu;
  
  ExpandedMenuView mMenuView;
  
  int mThemeRes;
  
  public ListMenuPresenter(int paramInt1, int paramInt2) {
    this.mItemLayoutRes = paramInt1;
    this.mThemeRes = paramInt2;
  }
  
  public ListMenuPresenter(Context paramContext, int paramInt) {
    this(paramInt, 0);
    this.mContext = paramContext;
    this.mInflater = LayoutInflater.from(this.mContext);
  }
  
  public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl) {
    return false;
  }
  
  public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl) {
    return false;
  }
  
  public boolean flagActionItems() {
    return false;
  }
  
  public ListAdapter getAdapter() {
    if (this.mAdapter == null)
      this.mAdapter = new MenuAdapter(); 
    return (ListAdapter)this.mAdapter;
  }
  
  public int getId() {
    return this.mId;
  }
  
  int getItemIndexOffset() {
    return this.mItemIndexOffset;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup) {
    if (this.mMenuView == null) {
      this.mMenuView = (ExpandedMenuView)this.mInflater.inflate(R.layout.abc_expanded_menu_layout, paramViewGroup, false);
      if (this.mAdapter == null)
        this.mAdapter = new MenuAdapter(); 
      this.mMenuView.setAdapter((ListAdapter)this.mAdapter);
      this.mMenuView.setOnItemClickListener(this);
    } 
    return this.mMenuView;
  }
  
  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder) {
    if (this.mThemeRes != 0) {
      this.mContext = (Context)new ContextThemeWrapper(paramContext, this.mThemeRes);
      this.mInflater = LayoutInflater.from(this.mContext);
    } else if (this.mContext != null) {
      this.mContext = paramContext;
      if (this.mInflater == null)
        this.mInflater = LayoutInflater.from(this.mContext); 
    } 
    this.mMenu = paramMenuBuilder;
    if (this.mAdapter != null)
      this.mAdapter.notifyDataSetChanged(); 
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
    if (this.mCallback != null)
      this.mCallback.onCloseMenu(paramMenuBuilder, paramBoolean); 
  }
  
  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
    this.mMenu.performItemAction((MenuItem)this.mAdapter.getItem(paramInt), this, 0);
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    restoreHierarchyState((Bundle)paramParcelable);
  }
  
  public Parcelable onSaveInstanceState() {
    if (this.mMenuView == null)
      return null; 
    Bundle bundle = new Bundle();
    saveHierarchyState(bundle);
    return (Parcelable)bundle;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder) {
    if (!paramSubMenuBuilder.hasVisibleItems())
      return false; 
    (new MenuDialogHelper(paramSubMenuBuilder)).show(null);
    if (this.mCallback != null)
      this.mCallback.onOpenSubMenu(paramSubMenuBuilder); 
    return true;
  }
  
  public void restoreHierarchyState(Bundle paramBundle) {
    SparseArray sparseArray = paramBundle.getSparseParcelableArray("android:menu:list");
    if (sparseArray != null)
      this.mMenuView.restoreHierarchyState(sparseArray); 
  }
  
  public void saveHierarchyState(Bundle paramBundle) {
    SparseArray sparseArray = new SparseArray();
    if (this.mMenuView != null)
      this.mMenuView.saveHierarchyState(sparseArray); 
    paramBundle.putSparseParcelableArray("android:menu:list", sparseArray);
  }
  
  public void setCallback(MenuPresenter.Callback paramCallback) {
    this.mCallback = paramCallback;
  }
  
  public void setId(int paramInt) {
    this.mId = paramInt;
  }
  
  public void setItemIndexOffset(int paramInt) {
    this.mItemIndexOffset = paramInt;
    if (this.mMenuView != null)
      updateMenuView(false); 
  }
  
  public void updateMenuView(boolean paramBoolean) {
    if (this.mAdapter != null)
      this.mAdapter.notifyDataSetChanged(); 
  }
  
  private class MenuAdapter extends BaseAdapter {
    private int mExpandedIndex = -1;
    
    public MenuAdapter() {
      findExpandedIndex();
    }
    
    void findExpandedIndex() {
      MenuItemImpl menuItemImpl = ListMenuPresenter.this.mMenu.getExpandedItem();
      if (menuItemImpl != null) {
        ArrayList<MenuItemImpl> arrayList = ListMenuPresenter.this.mMenu.getNonActionItems();
        int j = arrayList.size();
        for (int i = 0; i < j; i++) {
          if ((MenuItemImpl)arrayList.get(i) == menuItemImpl) {
            this.mExpandedIndex = i;
            return;
          } 
        } 
      } 
      this.mExpandedIndex = -1;
    }
    
    public int getCount() {
      int i = ListMenuPresenter.this.mMenu.getNonActionItems().size() - ListMenuPresenter.this.mItemIndexOffset;
      return (this.mExpandedIndex < 0) ? i : (i - 1);
    }
    
    public MenuItemImpl getItem(int param1Int) {
      ArrayList<MenuItemImpl> arrayList = ListMenuPresenter.this.mMenu.getNonActionItems();
      int i = param1Int + ListMenuPresenter.this.mItemIndexOffset;
      param1Int = i;
      if (this.mExpandedIndex >= 0) {
        param1Int = i;
        if (i >= this.mExpandedIndex)
          param1Int = i + 1; 
      } 
      return arrayList.get(param1Int);
    }
    
    public long getItemId(int param1Int) {
      return param1Int;
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      View view = param1View;
      if (param1View == null)
        view = ListMenuPresenter.this.mInflater.inflate(ListMenuPresenter.this.mItemLayoutRes, param1ViewGroup, false); 
      ((MenuView.ItemView)view).initialize(getItem(param1Int), 0);
      return view;
    }
    
    public void notifyDataSetChanged() {
      findExpandedIndex();
      super.notifyDataSetChanged();
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\view\menu\ListMenuPresenter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */