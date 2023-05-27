package com.samsungimaging.connectionmanager.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class HelpDialog extends CustomDialog {
  public HelpDialog(Context paramContext) {
    super(paramContext, 2131427331);
  }
  
  private void initContent() {
    setTitle(2131361844);
    setView(2130903054);
    setNeutralButton(2131361817, (DialogInterface.OnClickListener)null);
  }
  
  protected void onCreate(Bundle paramBundle) {
    initContent();
    super.onCreate(paramBundle);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\HelpDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */