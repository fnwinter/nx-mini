package com.samsungimaging.connectionmanager.dialog;

import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;

public class ConnectionWaitDialog extends CustomDialog {
  public ConnectionWaitDialog(Context paramContext) {
    super(paramContext);
  }
  
  private void initContent() {
    setView(2130903059);
  }
  
  protected void onCreate(Bundle paramBundle) {
    initContent();
    super.onCreate(paramBundle);
    if (CMUtil.isTestMode(getContext()))
      ((LinearLayout)findViewById(2131558538)).setVisibility(4); 
  }
  
  protected void onStart() {
    ((TextView)findViewById(2131558552)).setText(2131361860);
    TextView textView = (TextView)findViewById(2131558554);
    textView.setText(textView.getText() + CMUtil.getUseragent(getContext()));
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\ConnectionWaitDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */