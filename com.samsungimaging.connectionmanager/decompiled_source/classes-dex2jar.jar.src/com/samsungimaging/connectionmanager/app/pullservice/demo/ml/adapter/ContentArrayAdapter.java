package com.samsungimaging.connectionmanager.app.pullservice.demo.ml.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;
import com.samsungimaging.connectionmanager.app.pullservice.demo.ml.ImageLoader;
import com.samsungimaging.connectionmanager.app.pullservice.demo.ml.Item;
import com.samsungimaging.connectionmanager.app.pullservice.demo.ml.Utils;
import com.samsungimaging.connectionmanager.util.ExToast;
import java.util.List;

public class ContentArrayAdapter extends ArrayAdapter<Item> {
  private ExToast mExToast;
  
  private Handler mHandler;
  
  public ImageLoader mImageLoader = null;
  
  public ContentArrayAdapter(Context paramContext, Activity paramActivity, int paramInt, List<Item> paramList, boolean paramBoolean, Handler paramHandler, ExToast paramExToast) {
    super(paramContext, paramInt, paramList);
    if (paramBoolean)
      this.mImageLoader = new ImageLoader(paramContext, paramActivity, 2130837530, Utils.getThumbStorage()); 
    this.mHandler = paramHandler;
    this.mExToast = paramExToast;
  }
  
  public int getCheckedItemCount() {
    int j = 0;
    int i = 0;
    while (true) {
      if (i >= getCount())
        return j; 
      int k = j;
      if (((Item)getItem(i)).getItemState() == 2)
        k = j + 1; 
      i++;
      j = k;
    } 
  }
  
  public long getCheckedItemSize() {
    long l = 0L;
    int i = 0;
    while (true) {
      if (i >= getCount())
        return l; 
      long l1 = l;
      if (((Item)getItem(i)).getItemState() == 2)
        l1 = l + Long.parseLong(((Item)getItem(i)).getSize()); 
      i++;
      l = l1;
    } 
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    ViewHolder viewHolder;
    Item item = (Item)getItem(paramInt);
    if (paramView == null) {
      paramView = ((LayoutInflater)getContext().getSystemService("layout_inflater")).inflate(2130903081, paramViewGroup, false);
      viewHolder = new ViewHolder();
      viewHolder.thumbImg = (ImageView)paramView.findViewById(2131558651);
      viewHolder.movieImage = (ImageView)paramView.findViewById(2131558653);
      viewHolder.checkBox = (CheckBox)paramView.findViewById(2131558652);
      viewHolder.notSupportMask = (ImageView)paramView.findViewById(2131558654);
      viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              Item item = (Item)param1View.getTag();
              if (item.getItemState() == 3)
                return; 
              if (((CheckBox)param1View).isChecked()) {
                if (ContentArrayAdapter.this.getCheckedItemCount() >= 1000) {
                  ((CheckBox)param1View).setChecked(false);
                  item.setItemState(1);
                  ContentArrayAdapter.this.mExToast.show(4);
                  return;
                } 
                if (Utils.getAvailableExternalMemorySize() < ContentArrayAdapter.this.getCheckedItemSize() + Long.parseLong(item.getSize())) {
                  ((CheckBox)param1View).setChecked(false);
                  item.setItemState(1);
                  ContentArrayAdapter.this.mExToast.show(6);
                  return;
                } 
                if (CMInfo.getInstance().getConnectedSSID() != null && CMInfo.getInstance().getConnectedSSID().contains("QF30") && ContentArrayAdapter.this.getCheckedItemSize() + Long.parseLong(item.getSize()) > 2147483648L) {
                  ((CheckBox)param1View).setChecked(false);
                  item.setItemState(1);
                  ContentArrayAdapter.this.mExToast.show(5);
                  return;
                } 
                item.setItemState(2);
                ContentArrayAdapter.this.mHandler.sendEmptyMessage(37);
                return;
              } 
              item.setItemState(1);
              ContentArrayAdapter.this.mHandler.sendEmptyMessage(37);
            }
          });
      viewHolder.checkBox.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View param1View) {
              if (((Item)param1View.getTag()).isSupported()) {
                Message message = new Message();
                message.what = 42;
                message.obj = param1View.getTag();
                ContentArrayAdapter.this.mHandler.sendMessage(message);
              } 
              return true;
            }
          });
      paramView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder)paramView.getTag();
    } 
    viewHolder.checkBox.setTag(item);
    this.mImageLoader.setImage(item.getThumbRes(), (Activity)getContext(), viewHolder.thumbImg, item);
    if (Utils.getExtention(item.getRes()).equalsIgnoreCase("MP4")) {
      viewHolder.movieImage.setVisibility(0);
      if (CMInfo.getInstance().getConnectedSSID() != null && CMInfo.getInstance().getConnectedSSID().contains("QF30") && item.getResolution().equals("1920x1080"))
        item.setItemState(4); 
    } else {
      viewHolder.movieImage.setVisibility(4);
    } 
    switch (item.getItemState()) {
      default:
        if (!item.isSupported())
          viewHolder.thumbImg.setBackgroundResource(2130838227); 
        return paramView;
      case 1:
        viewHolder.checkBox.setButtonDrawable(2130838239);
        viewHolder.checkBox.setChecked(false);
        viewHolder.checkBox.setVisibility(0);
        viewHolder.notSupportMask.setVisibility(4);
      case 2:
        viewHolder.checkBox.setButtonDrawable(2130838239);
        viewHolder.checkBox.setChecked(true);
        viewHolder.checkBox.setVisibility(0);
        viewHolder.notSupportMask.setVisibility(4);
      case 3:
        viewHolder.checkBox.setButtonDrawable(2130837525);
        viewHolder.checkBox.setVisibility(0);
        viewHolder.notSupportMask.setVisibility(4);
      case 4:
        break;
    } 
    viewHolder.checkBox.setVisibility(4);
    viewHolder.notSupportMask.setVisibility(0);
  }
  
  public class ViewHolder {
    public CheckBox checkBox;
    
    public ImageView movieImage;
    
    public ImageView notSupportMask;
    
    public ImageView thumbImg;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\ml\adapter\ContentArrayAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */