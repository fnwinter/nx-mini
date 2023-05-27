package com.samsungimaging.connectionmanager.widget;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import com.samsungimaging.connectionmanager.util.Utils;

public class ListPopupWindow {
  private ArrayAdapter<String> mAdapter = null;
  
  private ListView mListView = null;
  
  private PopupWindow mPopupWindow = null;
  
  public ListPopupWindow(Activity paramActivity) {
    this.mAdapter = new ArrayAdapter((Context)paramActivity, 2130903106);
    this.mListView = (ListView)paramActivity.getLayoutInflater().inflate(2130903070, null);
    this.mListView.setAdapter((ListAdapter)this.mAdapter);
    this.mPopupWindow = new PopupWindow((Context)paramActivity);
    this.mPopupWindow.setContentView((View)this.mListView);
    this.mPopupWindow.setTouchable(true);
    this.mPopupWindow.setFocusable(true);
    this.mPopupWindow.setOutsideTouchable(true);
  }
  
  public void addItem(String[] paramArrayOfString) {
    if (this.mAdapter != null) {
      int j = paramArrayOfString.length;
      int i = 0;
      while (true) {
        if (i < j) {
          String str = paramArrayOfString[i];
          this.mAdapter.add(str);
          i++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public void clearItem() {
    if (this.mAdapter != null)
      this.mAdapter.clear(); 
  }
  
  public void destroy() {
    if (this.mAdapter != null) {
      this.mAdapter.clear();
      this.mAdapter = null;
    } 
    Utils.unbindView((View)this.mListView);
    if (this.mPopupWindow != null && this.mPopupWindow.isShowing()) {
      this.mPopupWindow.dismiss();
      this.mPopupWindow = null;
    } 
  }
  
  public void dismiss() {
    if (this.mPopupWindow != null)
      this.mPopupWindow.dismiss(); 
  }
  
  public ListView getListView() {
    return this.mListView;
  }
  
  public boolean isShowing() {
    return (this.mPopupWindow != null) ? this.mPopupWindow.isShowing() : false;
  }
  
  public void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener) {
    if (this.mListView != null)
      this.mListView.setOnItemClickListener(paramOnItemClickListener); 
  }
  
  public void setSize(int paramInt1, int paramInt2) {
    if (this.mPopupWindow != null) {
      this.mPopupWindow.setWidth(paramInt1);
      this.mPopupWindow.setHeight(paramInt2);
    } 
  }
  
  public void showAsDropDown(View paramView) {
    if (this.mPopupWindow != null)
      this.mPopupWindow.showAsDropDown(paramView); 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\widget\ListPopupWindow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */