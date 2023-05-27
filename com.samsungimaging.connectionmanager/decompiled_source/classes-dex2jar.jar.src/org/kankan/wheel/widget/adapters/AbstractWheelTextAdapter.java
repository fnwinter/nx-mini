package org.kankan.wheel.widget.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class AbstractWheelTextAdapter extends AbstractWheelAdapter {
  public static final int DEFAULT_TEXT_COLOR = -15724528;
  
  public static final int DEFAULT_TEXT_SIZE = 24;
  
  public static final int DEFAULT_TEXT_VIEW_HEIGHT = 40;
  
  public static final int DEFAULT_TEXT_VIEW_WIDTH = 50;
  
  public static final int LABEL_COLOR = -9437072;
  
  protected static final int NO_RESOURCE = 0;
  
  public static final int TEXT_VIEW_ITEM_RESOURCE = -1;
  
  protected Context context;
  
  protected int emptyItemResourceId;
  
  protected LayoutInflater inflater;
  
  protected int itemResourceId;
  
  protected int itemTextResourceId;
  
  private int textColor = -15724528;
  
  private int textSize = 24;
  
  private int textViewHeight = 40;
  
  private int textViewTopPadding = 0;
  
  private int textViewWidth = 50;
  
  protected AbstractWheelTextAdapter(Context paramContext) {
    this(paramContext, -1);
  }
  
  protected AbstractWheelTextAdapter(Context paramContext, int paramInt) {
    this(paramContext, paramInt, 0);
  }
  
  protected AbstractWheelTextAdapter(Context paramContext, int paramInt1, int paramInt2) {
    this.context = paramContext;
    this.itemResourceId = paramInt1;
    this.itemTextResourceId = paramInt2;
    this.inflater = (LayoutInflater)paramContext.getSystemService("layout_inflater");
  }
  
  private TextView getTextView(View paramView, int paramInt) {
    if (paramInt == 0) {
      try {
        if (paramView instanceof TextView)
          return (TextView)paramView; 
        if (paramInt != 0)
          return (TextView)paramView.findViewById(paramInt); 
      } catch (ClassCastException classCastException) {
        Log.e("AbstractWheelAdapter", "You must supply a resource ID for a TextView");
        throw new IllegalStateException("AbstractWheelAdapter requires the resource ID to be a TextView", classCastException);
      } 
      return null;
    } 
    if (paramInt != 0)
      return (TextView)classCastException.findViewById(paramInt); 
  }
  
  private View getView(int paramInt, ViewGroup paramViewGroup) {
    switch (paramInt) {
      default:
        return this.inflater.inflate(paramInt, paramViewGroup, false);
      case 0:
        return null;
      case -1:
        break;
    } 
    return (View)new TextView(this.context);
  }
  
  protected void configureTextView(TextView paramTextView) {
    paramTextView.setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(-2, -1));
    paramTextView.setTextColor(this.textColor);
    paramTextView.setGravity(17);
    paramTextView.setTextSize(this.textSize);
    paramTextView.setLines(1);
    paramTextView.setTypeface(Typeface.SANS_SERIF, 1);
    paramTextView.setWidth(this.textViewWidth);
    if (this.textViewHeight != 0)
      paramTextView.setHeight(this.textViewHeight); 
    if (this.textViewTopPadding != 0)
      paramTextView.setPadding(paramTextView.getPaddingLeft(), this.textViewTopPadding, paramTextView.getPaddingRight(), paramTextView.getPaddingBottom()); 
  }
  
  public View getEmptyItem(View paramView, ViewGroup paramViewGroup) {
    View view = paramView;
    if (paramView == null)
      view = getView(this.emptyItemResourceId, paramViewGroup); 
    if (this.emptyItemResourceId == -1 && view instanceof TextView)
      configureTextView((TextView)view); 
    return view;
  }
  
  public int getEmptyItemResource() {
    return this.emptyItemResourceId;
  }
  
  public View getItem(int paramInt, View paramView, ViewGroup paramViewGroup) {
    if (paramInt >= 0 && paramInt < getItemsCount()) {
      View view = paramView;
      if (paramView == null)
        view = getView(this.itemResourceId, paramViewGroup); 
      TextView textView = getTextView(view, this.itemTextResourceId);
      if (textView != null) {
        CharSequence charSequence2 = getItemText(paramInt);
        CharSequence charSequence1 = charSequence2;
        if (charSequence2 == null)
          charSequence1 = ""; 
        textView.setText(charSequence1);
        if (this.itemResourceId == -1)
          configureTextView(textView); 
      } 
      return view;
    } 
    return null;
  }
  
  public int getItemResource() {
    return this.itemResourceId;
  }
  
  protected abstract CharSequence getItemText(int paramInt);
  
  public int getItemTextResource() {
    return this.itemTextResourceId;
  }
  
  public int getTextColor() {
    return this.textColor;
  }
  
  public int getTextSize() {
    return this.textSize;
  }
  
  public int getTextViewHeight() {
    return this.textViewHeight;
  }
  
  public int getTextViewTopPadding() {
    return this.textViewTopPadding;
  }
  
  public int getTextViewWidth() {
    return this.textViewWidth;
  }
  
  public void setEmptyItemResource(int paramInt) {
    this.emptyItemResourceId = paramInt;
  }
  
  public void setItemResource(int paramInt) {
    this.itemResourceId = paramInt;
  }
  
  public void setItemTextResource(int paramInt) {
    this.itemTextResourceId = paramInt;
  }
  
  public void setTextColor(int paramInt) {
    this.textColor = paramInt;
  }
  
  public void setTextSize(int paramInt) {
    this.textSize = paramInt;
  }
  
  public void setTextViewHeight(int paramInt) {
    this.textViewHeight = paramInt;
  }
  
  public void setTextViewTopPadding(int paramInt) {
    this.textViewTopPadding = paramInt;
  }
  
  public void setTextViewWidth(int paramInt) {
    this.textViewWidth = paramInt;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\kankan\wheel\widget\adapters\AbstractWheelTextAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */