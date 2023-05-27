package com.samsungimaging.connectionmanager.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SeekBar;

public class VerticalSeekBar extends SeekBar {
  public VerticalSeekBar(Context paramContext) {
    super(paramContext);
  }
  
  public VerticalSeekBar(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
  }
  
  public VerticalSeekBar(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  protected void onDraw(Canvas paramCanvas) {
    paramCanvas.rotate(-90.0F);
    paramCanvas.translate(-getHeight(), 0.0F);
    super.onDraw(paramCanvas);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_2
    //   4: iload_1
    //   5: invokespecial onMeasure : (II)V
    //   8: aload_0
    //   9: aload_0
    //   10: invokevirtual getMeasuredHeight : ()I
    //   13: aload_0
    //   14: invokevirtual getMeasuredWidth : ()I
    //   17: invokevirtual setMeasuredDimension : (II)V
    //   20: aload_0
    //   21: monitorexit
    //   22: return
    //   23: astore_3
    //   24: aload_0
    //   25: monitorexit
    //   26: aload_3
    //   27: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	23	finally
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    super.onSizeChanged(paramInt2, paramInt1, paramInt4, paramInt3);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (!isEnabled())
      return true; 
    switch (paramMotionEvent.getAction()) {
      default:
        return true;
      case 0:
      case 1:
      case 2:
        break;
    } 
    setProgress(getMax() - (int)(getMax() * paramMotionEvent.getY() / getHeight()));
    Log.i("Progress", (new StringBuilder(String.valueOf(getProgress()))).toString());
    onSizeChanged(getWidth(), getHeight(), 0, 0);
    return true;
  }
  
  public void updateThumb() {
    onSizeChanged(getWidth(), getHeight(), 0, 0);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\widget\VerticalSeekBar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */