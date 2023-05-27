package com.samsungimaging.connectionmanager.dialog;

import android.content.Context;
import android.os.Bundle;

public class NowAppClosingProgressDialog extends CustomProgressDialog {
  public NowAppClosingProgressDialog(Context paramContext) {
    super(paramContext);
  }
  
  private void initContent() {
    setCancelable(false);
    setMessage(2131361896);
  }
  
  protected void onCreate(Bundle paramBundle) {
    initContent();
    super.onCreate(paramBundle);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\NowAppClosingProgressDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */