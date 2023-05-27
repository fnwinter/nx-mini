package com.samsungimaging.connectionmanager.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.manager.DatabaseManager;
import com.samsungimaging.connectionmanager.provider.DatabaseAP;

public class PairedCameraRemoveDialog extends CustomDialog implements DialogInterface.OnClickListener {
  private DatabaseAP mAp = null;
  
  public PairedCameraRemoveDialog(Context paramContext) {
    super(paramContext);
  }
  
  private void initContent() {
    setView(2130903062);
    setPositiveButton(17039360, (DialogInterface.OnClickListener)null);
    setNegativeButton(2131362044, this);
  }
  
  public void onClick(DialogInterface paramDialogInterface, int paramInt) {
    DatabaseManager.deleteForAP(getContext(), this.mAp.getUri());
    dismiss();
  }
  
  protected void onCreate(Bundle paramBundle) {
    initContent();
    super.onCreate(paramBundle);
  }
  
  protected void onStart() {
    TextView textView = (TextView)this.mView.findViewById(2131558500);
    textView.setText(2131361864);
    textView.append("\n" + this.mAp.getSSID());
    super.onStart();
  }
  
  public void setTag(Object paramObject) {
    this.mAp = (DatabaseAP)paramObject;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\PairedCameraRemoveDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */