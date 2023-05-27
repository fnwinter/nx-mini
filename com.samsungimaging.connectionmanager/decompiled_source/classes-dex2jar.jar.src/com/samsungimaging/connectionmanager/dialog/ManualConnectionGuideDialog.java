package com.samsungimaging.connectionmanager.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

public class ManualConnectionGuideDialog extends CustomDialog implements DialogInterface.OnClickListener {
  public ManualConnectionGuideDialog(Context paramContext) {
    super(paramContext, 2131427331);
  }
  
  private void initContent() {
    setTitle(2131361843);
    setMessage(2131361855);
    setPositiveButton(2131361817, (DialogInterface.OnClickListener)null);
    setNegativeButton(2131361854, this);
  }
  
  public void onClick(DialogInterface paramDialogInterface, int paramInt) {
    dismiss();
    Intent intent = new Intent("android.settings.WIFI_SETTINGS");
    getContext().startActivity(intent);
  }
  
  protected void onCreate(Bundle paramBundle) {
    initContent();
    super.onCreate(paramBundle);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\ManualConnectionGuideDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */