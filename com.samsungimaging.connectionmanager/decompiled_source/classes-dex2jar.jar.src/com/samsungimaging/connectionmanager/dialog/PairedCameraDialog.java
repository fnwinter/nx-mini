package com.samsungimaging.connectionmanager.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.manager.DatabaseManager;
import com.samsungimaging.connectionmanager.provider.DatabaseAP;
import java.io.Serializable;

public class PairedCameraDialog extends CustomDialog implements DialogInterface.OnClickListener {
  private Activity mActivity = null;
  
  public PairedCameraDialog(Activity paramActivity) {
    super((Context)paramActivity);
    this.mActivity = paramActivity;
  }
  
  private void delete(DatabaseAP paramDatabaseAP) {
    Bundle bundle = new Bundle();
    bundle.putSerializable("data", (Serializable)paramDatabaseAP);
    this.mActivity.showDialog(1, bundle);
  }
  
  private void initContent() {
    InternalAdapter internalAdapter = new InternalAdapter(getContext(), DatabaseManager.fetchAllToCursorForAP(getContext()));
    TextView textView = (TextView)getLayoutInflater().inflate(2130903105, null);
    textView.setText(2131362057);
    setTitle(2131361832);
    setListEmptyView((View)textView);
    setAdapter((ListAdapter)internalAdapter, this);
    setNeutralButton(2131361817, (DialogInterface.OnClickListener)null);
  }
  
  public void onClick(DialogInterface paramDialogInterface, int paramInt) {
    Cursor cursor = ((InternalAdapter)((CustomDialog)paramDialogInterface).getListView().getAdapter()).getCursor();
    cursor.moveToPosition(paramInt);
    delete(DatabaseAP.builder(cursor));
  }
  
  protected void onCreate(Bundle paramBundle) {
    initContent();
    super.onCreate(paramBundle);
  }
  
  private class InternalAdapter extends CursorAdapter {
    public InternalAdapter(Context param1Context, Cursor param1Cursor) {
      super(param1Context, param1Cursor, true);
    }
    
    public void bindView(View param1View, Context param1Context, Cursor param1Cursor) {
      final DatabaseAP ap = DatabaseAP.builder(param1Cursor);
      TextView textView = (TextView)param1View.findViewById(2131558500);
      textView.setSelected(true);
      textView.setText(databaseAP.getSSID());
      ImageView imageView = (ImageView)param1View.findViewById(2131558501);
      imageView.setImageResource(2130838220);
      imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param2View) {
              PairedCameraDialog.InternalAdapter.access$0(PairedCameraDialog.InternalAdapter.this).delete(ap);
            }
          });
    }
    
    public View newView(Context param1Context, Cursor param1Cursor, ViewGroup param1ViewGroup) {
      return PairedCameraDialog.this.getLayoutInflater().inflate(2130903046, null);
    }
  }
  
  class null implements View.OnClickListener {
    public void onClick(View param1View) {
      PairedCameraDialog.InternalAdapter.access$0(this.this$1).delete(ap);
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\PairedCameraDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */