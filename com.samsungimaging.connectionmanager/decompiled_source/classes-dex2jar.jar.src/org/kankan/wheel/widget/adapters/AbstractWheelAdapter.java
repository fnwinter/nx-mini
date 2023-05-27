package org.kankan.wheel.widget.adapters;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractWheelAdapter implements WheelViewAdapter {
  private List<DataSetObserver> datasetObservers;
  
  public View getEmptyItem(View paramView, ViewGroup paramViewGroup) {
    return null;
  }
  
  protected void notifyDataChangedEvent() {
    if (this.datasetObservers != null) {
      Iterator<DataSetObserver> iterator = this.datasetObservers.iterator();
      while (true) {
        if (iterator.hasNext()) {
          ((DataSetObserver)iterator.next()).onChanged();
          continue;
        } 
        return;
      } 
    } 
  }
  
  protected void notifyDataInvalidatedEvent() {
    if (this.datasetObservers != null) {
      Iterator<DataSetObserver> iterator = this.datasetObservers.iterator();
      while (true) {
        if (iterator.hasNext()) {
          ((DataSetObserver)iterator.next()).onInvalidated();
          continue;
        } 
        return;
      } 
    } 
  }
  
  public void registerDataSetObserver(DataSetObserver paramDataSetObserver) {
    if (this.datasetObservers == null)
      this.datasetObservers = new LinkedList<DataSetObserver>(); 
    this.datasetObservers.add(paramDataSetObserver);
  }
  
  public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver) {
    if (this.datasetObservers != null)
      this.datasetObservers.remove(paramDataSetObserver); 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\kankan\wheel\widget\adapters\AbstractWheelAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */