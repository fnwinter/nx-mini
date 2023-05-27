package com.samsungimaging.connectionmanager.dialog;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;

public class RefusalDialog extends CustomDialog {
  public RefusalDialog(Context paramContext) {
    super(paramContext);
  }
  
  private void initContent() {
    setView(2130903064);
  }
  
  protected void onCreate(Bundle paramBundle) {
    initContent();
    super.onCreate(paramBundle);
  }
  
  protected void onStart() {
    ((TextView)findViewById(2131558564)).setText(CMInfo.getInstance().getConnectedSSID());
    TextView textView = (TextView)findViewById(2131558563);
    String str = (String)getTag();
    if (str.equals("CHECK_FOR_401_ERROR")) {
      textView.setText(2131361829);
    } else if (str.equals("CHECK_FOR_503_ERROR")) {
      textView.setText(2131361830);
    } 
    super.onStart();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\RefusalDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */