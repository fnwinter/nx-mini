package com.samsungimaging.connectionmanager.app.pullservice.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.app.pullservice.datatype.DSCMenuItem;
import java.util.List;

public class CustomDialogListMenuAdapter extends ArrayAdapter<DSCMenuItem> {
  public CustomDialogListMenuAdapter(Context paramContext, int paramInt, List<DSCMenuItem> paramList) {
    super(paramContext, paramInt, paramList);
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
    View view = paramView;
    if (paramView == null)
      view = LayoutInflater.from(getContext()).inflate(2130903090, null); 
    DSCMenuItem dSCMenuItem = (DSCMenuItem)getItem(paramInt);
    ImageView imageView1 = (ImageView)view.findViewById(2131558669);
    TextView textView = (TextView)view.findViewById(2131558670);
    ImageView imageView2 = (ImageView)view.findViewById(2131558671);
    imageView1.setImageResource(dSCMenuItem.getImageResourceId());
    textView.setText(dSCMenuItem.getName());
    if (dSCMenuItem.isSelected()) {
      imageView2.setImageResource(2130837948);
      return view;
    } 
    imageView2.setImageResource(2130837947);
    return view;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\dialog\CustomDialogListMenuAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */