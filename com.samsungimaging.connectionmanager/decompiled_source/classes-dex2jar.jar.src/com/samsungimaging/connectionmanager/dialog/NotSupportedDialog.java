package com.samsungimaging.connectionmanager.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class NotSupportedDialog extends CustomDialog {
  public NotSupportedDialog(Context paramContext) {
    super(paramContext);
  }
  
  private void initContent() {
    setView(2130903061);
    setNeutralButton(2131361817, (DialogInterface.OnClickListener)null);
  }
  
  private void initCustomContent() {
    ((LinearLayout)findViewById(2131558557)).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.samsungimaging.filesharing"));
            NotSupportedDialog.this.getContext().startActivity(intent);
          }
        });
    ((LinearLayout)findViewById(2131558558)).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=com.samsung.app"));
            NotSupportedDialog.this.getContext().startActivity(intent);
          }
        });
  }
  
  protected void onCreate(Bundle paramBundle) {
    initContent();
    super.onCreate(paramBundle);
    initCustomContent();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\NotSupportedDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */