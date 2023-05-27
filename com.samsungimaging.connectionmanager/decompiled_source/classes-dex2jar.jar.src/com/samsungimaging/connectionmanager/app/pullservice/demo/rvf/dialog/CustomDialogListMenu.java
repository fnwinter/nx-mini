package com.samsungimaging.connectionmanager.app.pullservice.demo.rvf.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.samsungimaging.connectionmanager.app.pullservice.demo.rvf.Menu;
import java.util.ArrayList;

public class CustomDialogListMenu extends Dialog {
  CustomDialogListMenuAdapter adapter;
  
  AdapterView.OnItemClickListener itemClickListener;
  
  ListView rvf_itemListView;
  
  public CustomDialogListMenu(Context paramContext, ArrayList<Menu> paramArrayList, AdapterView.OnItemClickListener paramOnItemClickListener) {
    super(paramContext);
    this.adapter = new CustomDialogListMenuAdapter(getContext(), 2130903090, paramArrayList);
    this.itemClickListener = paramOnItemClickListener;
  }
  
  public void notifyDataSetChanged() {
    this.adapter.notifyDataSetChanged();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2130903089);
    this.rvf_itemListView = (ListView)findViewById(2131558668);
    this.rvf_itemListView.setAdapter((ListAdapter)this.adapter);
    this.rvf_itemListView.setOnItemClickListener(this.itemClickListener);
  }
  
  public void setEnabled(boolean paramBoolean) {
    this.rvf_itemListView.setEnabled(paramBoolean);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\rvf\dialog\CustomDialogListMenu.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */