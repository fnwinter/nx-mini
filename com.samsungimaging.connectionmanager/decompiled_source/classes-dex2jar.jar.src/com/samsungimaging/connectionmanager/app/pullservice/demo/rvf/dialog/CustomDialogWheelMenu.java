package com.samsungimaging.connectionmanager.app.pullservice.demo.rvf.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;
import org.kankan.wheel.widget.OnWheelClickedListener;
import org.kankan.wheel.widget.OnWheelScrollListener;
import org.kankan.wheel.widget.WheelView;
import org.kankan.wheel.widget.adapters.ArrayWheelAdapter;
import org.kankan.wheel.widget.adapters.WheelViewAdapter;

public class CustomDialogWheelMenu extends Dialog {
  int focusIndex;
  
  String[] items;
  
  int textViewHeight = 0;
  
  int textViewTopPadding = 0;
  
  int textViewWidth;
  
  String title;
  
  TextView titleView;
  
  TextView valueView;
  
  int visibleItems;
  
  OnWheelClickedListener wheelCliekedListener;
  
  OnWheelScrollListener wheelScrollListener;
  
  WheelView wheelView;
  
  public CustomDialogWheelMenu(Context paramContext, String paramString, int paramInt1, int paramInt2, String[] paramArrayOfString, int paramInt3, OnWheelScrollListener paramOnWheelScrollListener, OnWheelClickedListener paramOnWheelClickedListener) {
    super(paramContext);
    this.items = paramArrayOfString;
    this.wheelScrollListener = paramOnWheelScrollListener;
    this.wheelCliekedListener = paramOnWheelClickedListener;
    this.title = paramString;
    this.visibleItems = paramInt1;
    this.focusIndex = paramInt2;
    this.textViewWidth = paramInt3;
  }
  
  private void updateWheelView(WheelView paramWheelView, String[] paramArrayOfString) {
    ArrayWheelAdapter arrayWheelAdapter = new ArrayWheelAdapter(getContext(), (Object[])paramArrayOfString);
    arrayWheelAdapter.setTextSize(getContext().getResources().getInteger(2131296264));
    arrayWheelAdapter.setTextColor(-1);
    arrayWheelAdapter.setTextViewWidth(this.textViewWidth);
    arrayWheelAdapter.setTextViewHeight(this.textViewHeight);
    arrayWheelAdapter.setTextViewTopPadding(this.textViewTopPadding);
    paramWheelView.setVisibleItems(this.visibleItems);
    paramWheelView.setViewAdapter((WheelViewAdapter)arrayWheelAdapter);
    paramWheelView.setCurrentItem(this.focusIndex);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2130903091);
    getWindow().setBackgroundDrawable((Drawable)new ColorDrawable(0));
    this.titleView = (TextView)findViewById(2131558672);
    this.titleView.setText(this.title);
    this.valueView = (TextView)findViewById(2131558673);
    this.valueView.setText(this.items[this.focusIndex]);
    this.wheelView = (WheelView)findViewById(2131558674);
    this.wheelView.setVisibleItems(5);
    updateWheelView(this.wheelView, this.items);
    this.wheelView.addScrollingListener(this.wheelScrollListener);
    this.wheelView.addClickingListener(this.wheelCliekedListener);
  }
  
  public void setCurrentItem(int paramInt) {
    this.wheelView.setCurrentItem(paramInt);
  }
  
  public void setValue(String paramString) {
    this.valueView.setText(paramString);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\rvf\dialog\CustomDialogWheelMenu.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */