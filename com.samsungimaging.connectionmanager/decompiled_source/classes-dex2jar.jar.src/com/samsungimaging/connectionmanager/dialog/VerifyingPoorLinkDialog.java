package com.samsungimaging.connectionmanager.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class VerifyingPoorLinkDialog extends CustomDialog {
  public VerifyingPoorLinkDialog(Context paramContext) {
    super(paramContext);
  }
  
  private void initContent() {
    setView(2130903066);
    setNeutralButton(2131361842, (DialogInterface.OnClickListener)null);
  }
  
  protected void onCreate(Bundle paramBundle) {
    initContent();
    super.onCreate(paramBundle);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\VerifyingPoorLinkDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */