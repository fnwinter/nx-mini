package com.samsungimaging.connectionmanager.app.pullservice.demo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;
import com.samsungimaging.connectionmanager.app.pullservice.demo.util.CommonUtils;

public class CMWaitConnectDialog extends Dialog {
  public CMWaitConnectDialog(Context paramContext) {
    super(paramContext);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    setContentView(2130903077);
    TextView textView = (TextView)findViewById(2131558602);
    if (CMInfo.getInstance().getIsNFCLaunch()) {
      textView.setText(2131361880);
    } else {
      textView.setText(2131361860);
    } 
    textView = (TextView)findViewById(2131558554);
    textView.setText(textView.getText() + CommonUtils.getUseragent(getContext()));
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\dialog\CMWaitConnectDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */