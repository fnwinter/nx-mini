package org.kankan.wheel.widget;

import android.view.View;
import android.widget.LinearLayout;
import java.util.LinkedList;
import java.util.List;

public class WheelRecycle {
  private List<View> emptyItems;
  
  private List<View> items;
  
  private WheelView wheel;
  
  public WheelRecycle(WheelView paramWheelView) {
    this.wheel = paramWheelView;
  }
  
  private List<View> addView(View paramView, List<View> paramList) {
    List<View> list = paramList;
    if (paramList == null)
      list = new LinkedList<View>(); 
    list.add(paramView);
    return list;
  }
  
  private View getCachedView(List<View> paramList) {
    if (paramList != null && paramList.size() > 0) {
      View view = paramList.get(0);
      paramList.remove(0);
      return view;
    } 
    return null;
  }
  
  private void recycleView(View paramView, int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: getfield wheel : Lorg/kankan/wheel/widget/WheelView;
    //   4: invokevirtual getViewAdapter : ()Lorg/kankan/wheel/widget/adapters/WheelViewAdapter;
    //   7: invokeinterface getItemsCount : ()I
    //   12: istore #4
    //   14: iload_2
    //   15: iflt -> 26
    //   18: iload_2
    //   19: istore_3
    //   20: iload_2
    //   21: iload #4
    //   23: if_icmplt -> 57
    //   26: iload_2
    //   27: istore_3
    //   28: aload_0
    //   29: getfield wheel : Lorg/kankan/wheel/widget/WheelView;
    //   32: invokevirtual isCyclic : ()Z
    //   35: ifne -> 57
    //   38: aload_0
    //   39: aload_0
    //   40: aload_1
    //   41: aload_0
    //   42: getfield emptyItems : Ljava/util/List;
    //   45: invokespecial addView : (Landroid/view/View;Ljava/util/List;)Ljava/util/List;
    //   48: putfield emptyItems : Ljava/util/List;
    //   51: return
    //   52: iload_3
    //   53: iload #4
    //   55: iadd
    //   56: istore_3
    //   57: iload_3
    //   58: iflt -> 52
    //   61: aload_0
    //   62: aload_0
    //   63: aload_1
    //   64: aload_0
    //   65: getfield items : Ljava/util/List;
    //   68: invokespecial addView : (Landroid/view/View;Ljava/util/List;)Ljava/util/List;
    //   71: putfield items : Ljava/util/List;
    //   74: return
  }
  
  public void clearAll() {
    if (this.items != null)
      this.items.clear(); 
    if (this.emptyItems != null)
      this.emptyItems.clear(); 
  }
  
  public View getEmptyItem() {
    return getCachedView(this.emptyItems);
  }
  
  public View getItem() {
    return getCachedView(this.items);
  }
  
  public int recycleItems(LinearLayout paramLinearLayout, int paramInt, ItemsRange paramItemsRange) {
    int j = paramInt;
    int k = 0;
    int i = paramInt;
    paramInt = j;
    j = k;
    while (true) {
      int m;
      if (j >= paramLinearLayout.getChildCount())
        return i; 
      if (!paramItemsRange.contains(paramInt)) {
        recycleView(paramLinearLayout.getChildAt(j), paramInt);
        paramLinearLayout.removeViewAt(j);
        k = j;
        m = i;
        if (j == 0) {
          m = i + 1;
          k = j;
        } 
      } else {
        k = j + 1;
        m = i;
      } 
      paramInt++;
      j = k;
      i = m;
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\kankan\wheel\widget\WheelRecycle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */