package android.support.v7.widget;

import android.content.Context;
import android.support.annotation.MenuRes;
import android.support.v7.appcompat.R;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.SubMenuBuilder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class PopupMenu implements MenuBuilder.Callback, MenuPresenter.Callback {
  private View mAnchor;
  
  private Context mContext;
  
  private OnDismissListener mDismissListener;
  
  private View.OnTouchListener mDragListener;
  
  private MenuBuilder mMenu;
  
  private OnMenuItemClickListener mMenuItemClickListener;
  
  private MenuPopupHelper mPopup;
  
  public PopupMenu(Context paramContext, View paramView) {
    this(paramContext, paramView, 0);
  }
  
  public PopupMenu(Context paramContext, View paramView, int paramInt) {
    this(paramContext, paramView, paramInt, R.attr.popupMenuStyle, 0);
  }
  
  public PopupMenu(Context paramContext, View paramView, int paramInt1, int paramInt2, int paramInt3) {
    this.mContext = paramContext;
    this.mMenu = new MenuBuilder(paramContext);
    this.mMenu.setCallback(this);
    this.mAnchor = paramView;
    this.mPopup = new MenuPopupHelper(paramContext, this.mMenu, paramView, false, paramInt2, paramInt3);
    this.mPopup.setGravity(paramInt1);
    this.mPopup.setCallback(this);
  }
  
  public void dismiss() {
    this.mPopup.dismiss();
  }
  
  public View.OnTouchListener getDragToOpenListener() {
    if (this.mDragListener == null)
      this.mDragListener = new ListPopupWindow.ForwardingListener(this.mAnchor) {
          public ListPopupWindow getPopup() {
            return PopupMenu.this.mPopup.getPopup();
          }
          
          protected boolean onForwardingStarted() {
            PopupMenu.this.show();
            return true;
          }
          
          protected boolean onForwardingStopped() {
            PopupMenu.this.dismiss();
            return true;
          }
        }; 
    return this.mDragListener;
  }
  
  public int getGravity() {
    return this.mPopup.getGravity();
  }
  
  public Menu getMenu() {
    return (Menu)this.mMenu;
  }
  
  public MenuInflater getMenuInflater() {
    return (MenuInflater)new SupportMenuInflater(this.mContext);
  }
  
  public void inflate(@MenuRes int paramInt) {
    getMenuInflater().inflate(paramInt, (Menu)this.mMenu);
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
    if (this.mDismissListener != null)
      this.mDismissListener.onDismiss(this); 
  }
  
  public void onCloseSubMenu(SubMenuBuilder paramSubMenuBuilder) {}
  
  public boolean onMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem) {
    return (this.mMenuItemClickListener != null) ? this.mMenuItemClickListener.onMenuItemClick(paramMenuItem) : false;
  }
  
  public void onMenuModeChange(MenuBuilder paramMenuBuilder) {}
  
  public boolean onOpenSubMenu(MenuBuilder paramMenuBuilder) {
    boolean bool = true;
    if (paramMenuBuilder == null)
      return false; 
    if (paramMenuBuilder.hasVisibleItems()) {
      (new MenuPopupHelper(this.mContext, paramMenuBuilder, this.mAnchor)).show();
      return true;
    } 
    return bool;
  }
  
  public void setGravity(int paramInt) {
    this.mPopup.setGravity(paramInt);
  }
  
  public void setOnDismissListener(OnDismissListener paramOnDismissListener) {
    this.mDismissListener = paramOnDismissListener;
  }
  
  public void setOnMenuItemClickListener(OnMenuItemClickListener paramOnMenuItemClickListener) {
    this.mMenuItemClickListener = paramOnMenuItemClickListener;
  }
  
  public void show() {
    this.mPopup.show();
  }
  
  public static interface OnDismissListener {
    void onDismiss(PopupMenu param1PopupMenu);
  }
  
  public static interface OnMenuItemClickListener {
    boolean onMenuItemClick(MenuItem param1MenuItem);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\PopupMenu.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */