package org.kankan.wheel.widget.adapters;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;

public interface WheelViewAdapter {
  View getEmptyItem(View paramView, ViewGroup paramViewGroup);
  
  View getItem(int paramInt, View paramView, ViewGroup paramViewGroup);
  
  int getItemsCount();
  
  void registerDataSetObserver(DataSetObserver paramDataSetObserver);
  
  void unregisterDataSetObserver(DataSetObserver paramDataSetObserver);
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\kankan\wheel\widget\adapters\WheelViewAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */