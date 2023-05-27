package org.kankan.wheel.widget;

public class ItemsRange {
  private int count;
  
  private int first;
  
  public ItemsRange() {
    this(0, 0);
  }
  
  public ItemsRange(int paramInt1, int paramInt2) {
    this.first = paramInt1;
    this.count = paramInt2;
  }
  
  public boolean contains(int paramInt) {
    return (paramInt >= getFirst() && paramInt <= getLast());
  }
  
  public int getCount() {
    return this.count;
  }
  
  public int getFirst() {
    return this.first;
  }
  
  public int getLast() {
    return getFirst() + getCount() - 1;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\kankan\wheel\widget\ItemsRange.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */