package org.kankan.wheel.widget.adapters;

import android.content.Context;

public class ArrayWheelAdapter<T> extends AbstractWheelTextAdapter {
  private T[] items;
  
  public ArrayWheelAdapter(Context paramContext, T[] paramArrayOfT) {
    super(paramContext);
    this.items = paramArrayOfT;
  }
  
  public CharSequence getItemText(int paramInt) {
    if (paramInt >= 0 && paramInt < this.items.length) {
      T t = this.items[paramInt];
      return (t instanceof CharSequence) ? (CharSequence)t : t.toString();
    } 
    return null;
  }
  
  public int getItemsCount() {
    return this.items.length;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\kankan\wheel\widget\adapters\ArrayWheelAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */