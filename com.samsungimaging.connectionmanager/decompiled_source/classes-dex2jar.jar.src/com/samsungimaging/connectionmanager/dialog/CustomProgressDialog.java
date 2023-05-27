package com.samsungimaging.connectionmanager.dialog;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.text.NumberFormat;
import java.util.Locale;

public class CustomProgressDialog extends CustomDialog {
  public static final int STYLE_HORIZONTAL = 1;
  
  public static final int STYLE_SPINNER = 0;
  
  private int mMax = 0;
  
  private CharSequence mMessage = null;
  
  private OnProcessNumberListener mOnProcessNumberListener = null;
  
  private OnProcessPercentListener mOnProcessPercentListener = null;
  
  private ProgressBar mProgressBar = null;
  
  private int mProgressStyle = 0;
  
  private int mProgressVal = 0;
  
  private Handler mViewUpdateHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        TextView textView1 = (TextView)CustomProgressDialog.this.mView.findViewById(2131558545);
        TextView textView2 = (TextView)CustomProgressDialog.this.mView.findViewById(2131558546);
        if (CustomProgressDialog.this.mOnProcessPercentListener == null) {
          textView1.setText(NumberFormat.getPercentInstance(Locale.ENGLISH).format(CustomProgressDialog.this.mProgressVal / CustomProgressDialog.this.mMax));
        } else {
          textView1.setText(CustomProgressDialog.this.mOnProcessPercentListener.onProcessPercent(CustomProgressDialog.this));
        } 
        if (CustomProgressDialog.this.mOnProcessNumberListener == null) {
          textView2.setText(String.format("%,d/%,d", new Object[] { Integer.valueOf(CustomProgressDialog.access$1(this.this$0)), Integer.valueOf(CustomProgressDialog.access$2(this.this$0)) }));
          return;
        } 
        textView2.setText(CustomProgressDialog.this.mOnProcessNumberListener.onProcessNumber(CustomProgressDialog.this));
      }
    };
  
  public CustomProgressDialog(Context paramContext) {
    super(paramContext);
  }
  
  private void initContent() {
    LinearLayout linearLayout;
    setView(2130903053);
    if (this.mProgressStyle == 0) {
      linearLayout = (LinearLayout)this.mView.findViewById(2131558542);
      ((TextView)linearLayout.findViewById(2131558536)).setText(this.mMessage);
      this.mProgressBar = (ProgressBar)linearLayout.findViewById(2131558543);
    } else {
      linearLayout = (LinearLayout)this.mView.findViewById(2131558544);
      super.mMessage = this.mMessage;
      this.mProgressBar = (ProgressBar)linearLayout.findViewById(2131558543);
    } 
    if (this.mMax > 0)
      setMax(this.mMax); 
    if (this.mProgressVal > 0)
      setProgress(this.mProgressVal); 
    linearLayout.setVisibility(0);
  }
  
  private void onProgressChanged() {
    if (this.mProgressStyle == 1 && this.mViewUpdateHandler != null && !this.mViewUpdateHandler.hasMessages(0))
      this.mViewUpdateHandler.sendEmptyMessage(0); 
  }
  
  public int getMax() {
    return this.mMax;
  }
  
  public int getProgress() {
    return this.mProgressVal;
  }
  
  protected void onCreate(Bundle paramBundle) {
    initContent();
    super.onCreate(paramBundle);
  }
  
  public void setMax(int paramInt) {
    this.mMax = paramInt;
    if (this.mProgressBar != null) {
      this.mProgressBar.setMax(paramInt);
      onProgressChanged();
    } 
  }
  
  public void setMessage(int paramInt) {
    this.mMessage = getContext().getText(paramInt);
  }
  
  public void setMessage(CharSequence paramCharSequence) {
    this.mMessage = paramCharSequence;
  }
  
  public void setOnProcessNumberListener(OnProcessNumberListener paramOnProcessNumberListener) {
    this.mOnProcessNumberListener = paramOnProcessNumberListener;
  }
  
  public void setOnProcessPercentListener(OnProcessPercentListener paramOnProcessPercentListener) {
    this.mOnProcessPercentListener = paramOnProcessPercentListener;
  }
  
  public void setProgress(int paramInt) {
    this.mProgressVal = paramInt;
    if (this.mProgressBar != null) {
      this.mProgressBar.setProgress(paramInt);
      onProgressChanged();
    } 
  }
  
  public void setProgressStyle(int paramInt) {
    this.mProgressStyle = paramInt;
  }
  
  public static interface OnProcessNumberListener {
    String onProcessNumber(CustomProgressDialog param1CustomProgressDialog);
  }
  
  public static interface OnProcessPercentListener {
    String onProcessPercent(CustomProgressDialog param1CustomProgressDialog);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\CustomProgressDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */