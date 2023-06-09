package android.support.v7.view.menu;

import android.content.Context;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.internal.view.SupportSubMenu;
import android.support.v4.util.ArrayMap;
import android.view.MenuItem;
import android.view.SubMenu;
import java.util.Iterator;
import java.util.Map;

abstract class BaseMenuWrapper<T> extends BaseWrapper<T> {
  final Context mContext;
  
  private Map<SupportMenuItem, MenuItem> mMenuItems;
  
  private Map<SupportSubMenu, SubMenu> mSubMenus;
  
  BaseMenuWrapper(Context paramContext, T paramT) {
    super(paramT);
    this.mContext = paramContext;
  }
  
  final MenuItem getMenuItemWrapper(MenuItem paramMenuItem) {
    if (paramMenuItem instanceof SupportMenuItem) {
      SupportMenuItem supportMenuItem = (SupportMenuItem)paramMenuItem;
      if (this.mMenuItems == null)
        this.mMenuItems = (Map<SupportMenuItem, MenuItem>)new ArrayMap(); 
      MenuItem menuItem = this.mMenuItems.get(paramMenuItem);
      paramMenuItem = menuItem;
      if (menuItem == null) {
        paramMenuItem = MenuWrapperFactory.wrapSupportMenuItem(this.mContext, supportMenuItem);
        this.mMenuItems.put(supportMenuItem, paramMenuItem);
      } 
      return paramMenuItem;
    } 
    return paramMenuItem;
  }
  
  final SubMenu getSubMenuWrapper(SubMenu paramSubMenu) {
    if (paramSubMenu instanceof SupportSubMenu) {
      SupportSubMenu supportSubMenu = (SupportSubMenu)paramSubMenu;
      if (this.mSubMenus == null)
        this.mSubMenus = (Map<SupportSubMenu, SubMenu>)new ArrayMap(); 
      SubMenu subMenu = this.mSubMenus.get(supportSubMenu);
      paramSubMenu = subMenu;
      if (subMenu == null) {
        paramSubMenu = MenuWrapperFactory.wrapSupportSubMenu(this.mContext, supportSubMenu);
        this.mSubMenus.put(supportSubMenu, paramSubMenu);
      } 
      return paramSubMenu;
    } 
    return paramSubMenu;
  }
  
  final void internalClear() {
    if (this.mMenuItems != null)
      this.mMenuItems.clear(); 
    if (this.mSubMenus != null)
      this.mSubMenus.clear(); 
  }
  
  final void internalRemoveGroup(int paramInt) {
    if (this.mMenuItems != null) {
      Iterator<MenuItem> iterator = this.mMenuItems.keySet().iterator();
      while (true) {
        if (iterator.hasNext()) {
          if (paramInt == ((MenuItem)iterator.next()).getGroupId())
            iterator.remove(); 
          continue;
        } 
        return;
      } 
    } 
  }
  
  final void internalRemoveItem(int paramInt) {
    if (this.mMenuItems != null) {
      Iterator<MenuItem> iterator = this.mMenuItems.keySet().iterator();
      while (true) {
        if (iterator.hasNext()) {
          if (paramInt == ((MenuItem)iterator.next()).getItemId()) {
            iterator.remove();
            return;
          } 
          continue;
        } 
        return;
      } 
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\view\menu\BaseMenuWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */