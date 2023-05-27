package org.kankan.wheel.widget.adapters;

import android.content.Context;
import org.kankan.wheel.widget.WheelAdapter;

public class AdapterWheel extends AbstractWheelTextAdapter {
  private WheelAdapter adapter;
  
  public AdapterWheel(Context paramContext, WheelAdapter paramWheelAdapter) {
    super(paramContext);
    this.adapter = paramWheelAdapter;
  }
  
  public WheelAdapter getAdapter() {
    return this.adapter;
  }
  
  protected CharSequence getItemText(int paramInt) {
    return this.adapter.getItem(paramInt);
  }
  
  public int getItemsCount() {
    return this.adapter.getItemsCount();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\kankan\wheel\widget\adapters\AdapterWheel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */