package com.samsungimaging.connectionmanager.gallery;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import com.samsungimaging.connectionmanager.widget.ImageViewer;

public class GalleryPager extends ViewPager {
  private GestureDetector mGestureDetector;
  
  public GalleryPager(Context paramContext) {
    super(paramContext);
    init();
  }
  
  public GalleryPager(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  private void init() {
    this.mGestureDetector = new GestureDetector(getContext(), (GestureDetector.OnGestureListener)new InternalGestureListener(null));
  }
  
  private boolean touchable(MotionEvent paramMotionEvent) {
    if (this.mGestureDetector.onTouchEvent(paramMotionEvent))
      return false; 
    switch (paramMotionEvent.getAction()) {
      default:
        return true;
      case 2:
        break;
    } 
    if (!getCurrentImageViewer().isScaleMin())
      return false; 
  }
  
  public GalleryPagerAdapter getAdapter() {
    return (GalleryPagerAdapter)super.getAdapter();
  }
  
  public ImageViewer getCurrentImageViewer() {
    return getAdapter().getItem(getCurrentItem());
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
    return touchable(paramMotionEvent) ? super.onInterceptTouchEvent(paramMotionEvent) : false;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    return touchable(paramMotionEvent) ? super.onTouchEvent(paramMotionEvent) : false;
  }
  
  private class InternalGestureListener extends GestureDetector.SimpleOnGestureListener {
    private static final float NUM_OF_FLING_AVAILABLE = 2000.0F;
    
    private InternalGestureListener() {}
    
    public boolean onFling(MotionEvent param1MotionEvent1, MotionEvent param1MotionEvent2, float param1Float1, float param1Float2) {
      if (Math.abs(param1Float1) >= Math.abs(param1Float2) && Math.abs(param1Float1) > 2000.0F && !GalleryPager.this.getCurrentImageViewer().isScaleMin()) {
        int i = GalleryPager.this.getCurrentItem();
        ImageViewer imageViewer = GalleryPager.this.getCurrentImageViewer();
        if (param1Float1 > 0.0F && imageViewer.isBoundaryLeft()) {
          if (i > 0) {
            GalleryPager.this.setCurrentItem(i - 1);
            imageViewer.setScaleMin();
            return true;
          } 
          return false;
        } 
        if (param1Float1 < 0.0F && imageViewer.isBoundaryRight() && i < GalleryPager.this.getAdapter().getCount() - 1) {
          GalleryPager.this.setCurrentItem(i + 1);
          imageViewer.setScaleMin();
          return true;
        } 
      } 
      return false;
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\gallery\GalleryPager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */