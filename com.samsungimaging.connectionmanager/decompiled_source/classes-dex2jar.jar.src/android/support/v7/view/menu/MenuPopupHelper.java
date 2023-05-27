package android.support.v7.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcelable;
import android.support.v7.appcompat.R;
import android.support.v7.widget.ListPopupWindow;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import java.util.ArrayList;

public class MenuPopupHelper implements AdapterView.OnItemClickListener, View.OnKeyListener, ViewTreeObserver.OnGlobalLayoutListener, PopupWindow.OnDismissListener, MenuPresenter {
  static final int ITEM_LAYOUT = R.layout.abc_popup_menu_item_layout;
  
  private static final String TAG = "MenuPopupHelper";
  
  private final MenuAdapter mAdapter;
  
  private View mAnchorView;
  
  private int mContentWidth;
  
  private final Context mContext;
  
  private int mDropDownGravity = 0;
  
  boolean mForceShowIcon;
  
  private boolean mHasContentWidth;
  
  private final LayoutInflater mInflater;
  
  private ViewGroup mMeasureParent;
  
  private final MenuBuilder mMenu;
  
  private final boolean mOverflowOnly;
  
  private ListPopupWindow mPopup;
  
  private final int mPopupMaxWidth;
  
  private final int mPopupStyleAttr;
  
  private final int mPopupStyleRes;
  
  private MenuPresenter.Callback mPresenterCallback;
  
  private ViewTreeObserver mTreeObserver;
  
  public MenuPopupHelper(Context paramContext, MenuBuilder paramMenuBuilder) {
    this(paramContext, paramMenuBuilder, null, false, R.attr.popupMenuStyle);
  }
  
  public MenuPopupHelper(Context paramContext, MenuBuilder paramMenuBuilder, View paramView) {
    this(paramContext, paramMenuBuilder, paramView, false, R.attr.popupMenuStyle);
  }
  
  public MenuPopupHelper(Context paramContext, MenuBuilder paramMenuBuilder, View paramView, boolean paramBoolean, int paramInt) {
    this(paramContext, paramMenuBuilder, paramView, paramBoolean, paramInt, 0);
  }
  
  public MenuPopupHelper(Context paramContext, MenuBuilder paramMenuBuilder, View paramView, boolean paramBoolean, int paramInt1, int paramInt2) {
    this.mContext = paramContext;
    this.mInflater = LayoutInflater.from(paramContext);
    this.mMenu = paramMenuBuilder;
    this.mAdapter = new MenuAdapter(this.mMenu);
    this.mOverflowOnly = paramBoolean;
    this.mPopupStyleAttr = paramInt1;
    this.mPopupStyleRes = paramInt2;
    Resources resources = paramContext.getResources();
    this.mPopupMaxWidth = Math.max((resources.getDisplayMetrics()).widthPixels / 2, resources.getDimensionPixelSize(R.dimen.abc_config_prefDialogWidth));
    this.mAnchorView = paramView;
    paramMenuBuilder.addMenuPresenter(this, paramContext);
  }
  
  private int measureContentWidth() {
    int i = 0;
    View view = null;
    int k = 0;
    MenuAdapter menuAdapter = this.mAdapter;
    int m = View.MeasureSpec.makeMeasureSpec(0, 0);
    int n = View.MeasureSpec.makeMeasureSpec(0, 0);
    int i1 = menuAdapter.getCount();
    int j = 0;
    while (true) {
      int i2 = i;
      if (j < i1) {
        int i4 = menuAdapter.getItemViewType(j);
        i2 = k;
        if (i4 != k) {
          i2 = i4;
          view = null;
        } 
        if (this.mMeasureParent == null)
          this.mMeasureParent = (ViewGroup)new FrameLayout(this.mContext); 
        view = menuAdapter.getView(j, view, this.mMeasureParent);
        view.measure(m, n);
        k = view.getMeasuredWidth();
        if (k >= this.mPopupMaxWidth)
          return this.mPopupMaxWidth; 
      } else {
        return i2;
      } 
      int i3 = i;
      if (k > i)
        i3 = k; 
      j++;
      k = i2;
      i = i3;
    } 
  }
  
  public boolean collapseItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl) {
    return false;
  }
  
  public void dismiss() {
    if (isShowing())
      this.mPopup.dismiss(); 
  }
  
  public boolean expandItemActionView(MenuBuilder paramMenuBuilder, MenuItemImpl paramMenuItemImpl) {
    return false;
  }
  
  public boolean flagActionItems() {
    return false;
  }
  
  public int getGravity() {
    return this.mDropDownGravity;
  }
  
  public int getId() {
    return 0;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup) {
    throw new UnsupportedOperationException("MenuPopupHelpers manage their own views");
  }
  
  public ListPopupWindow getPopup() {
    return this.mPopup;
  }
  
  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder) {}
  
  public boolean isShowing() {
    return (this.mPopup != null && this.mPopup.isShowing());
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
    if (paramMenuBuilder == this.mMenu) {
      dismiss();
      if (this.mPresenterCallback != null) {
        this.mPresenterCallback.onCloseMenu(paramMenuBuilder, paramBoolean);
        return;
      } 
    } 
  }
  
  public void onDismiss() {
    this.mPopup = null;
    this.mMenu.close();
    if (this.mTreeObserver != null) {
      if (!this.mTreeObserver.isAlive())
        this.mTreeObserver = this.mAnchorView.getViewTreeObserver(); 
      this.mTreeObserver.removeGlobalOnLayoutListener(this);
      this.mTreeObserver = null;
    } 
  }
  
  public void onGlobalLayout() {
    if (isShowing()) {
      View view = this.mAnchorView;
      if (view == null || !view.isShown()) {
        dismiss();
        return;
      } 
    } else {
      return;
    } 
    if (isShowing()) {
      this.mPopup.show();
      return;
    } 
  }
  
  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
    MenuAdapter menuAdapter = this.mAdapter;
    menuAdapter.mAdapterMenu.performItemAction((MenuItem)menuAdapter.getItem(paramInt), 0);
  }
  
  public boolean onKey(View paramView, int paramInt, KeyEvent paramKeyEvent) {
    if (paramKeyEvent.getAction() == 1 && paramInt == 82) {
      dismiss();
      return true;
    } 
    return false;
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {}
  
  public Parcelable onSaveInstanceState() {
    return null;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder) {
    if (paramSubMenuBuilder.hasVisibleItems()) {
      MenuPopupHelper menuPopupHelper = new MenuPopupHelper(this.mContext, paramSubMenuBuilder, this.mAnchorView);
      menuPopupHelper.setCallback(this.mPresenterCallback);
      boolean bool = false;
      int j = paramSubMenuBuilder.size();
      int i = 0;
      while (true) {
        boolean bool1 = bool;
        if (i < j) {
          MenuItem menuItem = paramSubMenuBuilder.getItem(i);
          if (menuItem.isVisible() && menuItem.getIcon() != null) {
            bool1 = true;
          } else {
            i++;
            continue;
          } 
        } 
        menuPopupHelper.setForceShowIcon(bool1);
        if (menuPopupHelper.tryShow()) {
          if (this.mPresenterCallback != null)
            this.mPresenterCallback.onOpenSubMenu(paramSubMenuBuilder); 
          return true;
        } 
        break;
      } 
    } 
    return false;
  }
  
  public void setAnchorView(View paramView) {
    this.mAnchorView = paramView;
  }
  
  public void setCallback(MenuPresenter.Callback paramCallback) {
    this.mPresenterCallback = paramCallback;
  }
  
  public void setForceShowIcon(boolean paramBoolean) {
    this.mForceShowIcon = paramBoolean;
  }
  
  public void setGravity(int paramInt) {
    this.mDropDownGravity = paramInt;
  }
  
  public void show() {
    if (!tryShow())
      throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor"); 
  }
  
  public boolean tryShow() {
    boolean bool = false;
    this.mPopup = new ListPopupWindow(this.mContext, null, this.mPopupStyleAttr, this.mPopupStyleRes);
    this.mPopup.setOnDismissListener(this);
    this.mPopup.setOnItemClickListener(this);
    this.mPopup.setAdapter((ListAdapter)this.mAdapter);
    this.mPopup.setModal(true);
    View view = this.mAnchorView;
    if (view != null) {
      if (this.mTreeObserver == null)
        bool = true; 
      this.mTreeObserver = view.getViewTreeObserver();
      if (bool)
        this.mTreeObserver.addOnGlobalLayoutListener(this); 
      this.mPopup.setAnchorView(view);
      this.mPopup.setDropDownGravity(this.mDropDownGravity);
      if (!this.mHasContentWidth) {
        this.mContentWidth = measureContentWidth();
        this.mHasContentWidth = true;
      } 
      this.mPopup.setContentWidth(this.mContentWidth);
      this.mPopup.setInputMethodMode(2);
      this.mPopup.show();
      this.mPopup.getListView().setOnKeyListener(this);
      return true;
    } 
    return false;
  }
  
  public void updateMenuView(boolean paramBoolean) {
    this.mHasContentWidth = false;
    if (this.mAdapter != null)
      this.mAdapter.notifyDataSetChanged(); 
  }
  
  private class MenuAdapter extends BaseAdapter {
    private MenuBuilder mAdapterMenu;
    
    private int mExpandedIndex = -1;
    
    public MenuAdapter(MenuBuilder param1MenuBuilder) {
      this.mAdapterMenu = param1MenuBuilder;
      findExpandedIndex();
    }
    
    void findExpandedIndex() {
      MenuItemImpl menuItemImpl = MenuPopupHelper.this.mMenu.getExpandedItem();
      if (menuItemImpl != null) {
        ArrayList<MenuItemImpl> arrayList = MenuPopupHelper.this.mMenu.getNonActionItems();
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
      ArrayList<MenuItemImpl> arrayList;
      if (MenuPopupHelper.this.mOverflowOnly) {
        arrayList = this.mAdapterMenu.getNonActionItems();
      } else {
        arrayList = this.mAdapterMenu.getVisibleItems();
      } 
      return (this.mExpandedIndex < 0) ? arrayList.size() : (arrayList.size() - 1);
    }
    
    public MenuItemImpl getItem(int param1Int) {
      ArrayList<MenuItemImpl> arrayList;
      if (MenuPopupHelper.this.mOverflowOnly) {
        arrayList = this.mAdapterMenu.getNonActionItems();
      } else {
        arrayList = this.mAdapterMenu.getVisibleItems();
      } 
      int i = param1Int;
      if (this.mExpandedIndex >= 0) {
        i = param1Int;
        if (param1Int >= this.mExpandedIndex)
          i = param1Int + 1; 
      } 
      return arrayList.get(i);
    }
    
    public long getItemId(int param1Int) {
      return param1Int;
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      View view = param1View;
      if (param1View == null)
        view = MenuPopupHelper.this.mInflater.inflate(MenuPopupHelper.ITEM_LAYOUT, param1ViewGroup, false); 
      MenuView.ItemView itemView = (MenuView.ItemView)view;
      if (MenuPopupHelper.this.mForceShowIcon)
        ((ListMenuItemView)view).setForceShowIcon(true); 
      itemView.initialize(getItem(param1Int), 0);
      return view;
    }
    
    public void notifyDataSetChanged() {
      findExpandedIndex();
      super.notifyDataSetChanged();
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\view\menu\MenuPopupHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */