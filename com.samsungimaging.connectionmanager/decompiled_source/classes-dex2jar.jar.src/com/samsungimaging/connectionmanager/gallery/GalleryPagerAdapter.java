package com.samsungimaging.connectionmanager.gallery;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import com.samsungimaging.connectionmanager.widget.ImageViewer;

public class GalleryPagerAdapter extends PagerAdapter {
  private GalleryFragment mGalleryFragment = null;
  
  private OnInstantiateItemListener mOnInstantiateItemListener = null;
  
  private SparseArray<ImageViewer> mViewList = new SparseArray();
  
  public GalleryPagerAdapter(GalleryFragment paramGalleryFragment) {
    this.mGalleryFragment = paramGalleryFragment;
  }
  
  public void clear() {
    for (int i = 0;; i++) {
      if (i >= this.mViewList.size()) {
        this.mViewList.clear();
        this.mViewList = null;
        return;
      } 
      int j = this.mViewList.keyAt(i);
      ((ImageViewer)this.mViewList.get(j)).clearImage();
    } 
  }
  
  public void destroyItem(ViewGroup paramViewGroup, int paramInt, Object paramObject) {
    paramObject = paramObject;
    paramObject.clearImage();
    ((ViewPager)paramViewGroup).removeView((View)paramObject);
    this.mViewList.remove(paramInt);
  }
  
  public int getCount() {
    return this.mGalleryFragment.getChildTotalCount();
  }
  
  public ImageViewer getItem(int paramInt) {
    return (ImageViewer)this.mViewList.get(paramInt);
  }
  
  public int getItemPosition(Object paramObject) {
    return -2;
  }
  
  public ImageViewer instantiateItem(ViewGroup paramViewGroup, int paramInt) {
    ImageViewer imageViewer = null;
    if (this.mOnInstantiateItemListener != null) {
      imageViewer = this.mOnInstantiateItemListener.onInstantiateItem(paramViewGroup, paramInt);
      this.mViewList.put(paramInt, imageViewer);
    } 
    return imageViewer;
  }
  
  public boolean isViewFromObject(View paramView, Object paramObject) {
    return (paramView == paramObject);
  }
  
  public void setOnInstantiateItemListener(OnInstantiateItemListener paramOnInstantiateItemListener) {
    this.mOnInstantiateItemListener = paramOnInstantiateItemListener;
  }
  
  public static interface OnInstantiateItemListener {
    ImageViewer onInstantiateItem(ViewGroup param1ViewGroup, int param1Int);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\gallery\GalleryPagerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */