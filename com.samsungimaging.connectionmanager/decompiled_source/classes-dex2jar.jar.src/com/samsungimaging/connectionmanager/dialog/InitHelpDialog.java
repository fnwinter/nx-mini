package com.samsungimaging.connectionmanager.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;

public class InitHelpDialog extends HelpDialog {
  public InitHelpDialog(Context paramContext) {
    super(paramContext);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
  }
  
  public void onDismiss(DialogInterface paramDialogInterface) {
    CMUtil.sendBroadCastToMain(getContext(), "SHOW_SEARCHAPDIALOG");
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\InitHelpDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */