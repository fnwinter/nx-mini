package com.samsungimaging.connectionmanager.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.samsungimaging.connectionmanager.app.cm.common.CMSharedPreferenceUtil;

public class CMTestModePassWordDialog extends Dialog {
  private Activity mActivity = null;
  
  Context mContext = null;
  
  EditText met_password;
  
  public CMTestModePassWordDialog(Context paramContext, Activity paramActivity) {
    super(paramContext);
    this.mContext = paramContext;
    this.mActivity = paramActivity;
  }
  
  public void dismiss() {
    super.dismiss();
    this.met_password.setText("");
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    setContentView(2130903049);
    this.met_password = (EditText)findViewById(2131558522);
    ((Button)findViewById(2131558520)).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            String str = CMTestModePassWordDialog.this.met_password.getText().toString();
            if ("true".equalsIgnoreCase(CMSharedPreferenceUtil.getString(CMTestModePassWordDialog.this.mContext, "com.samsungimaging.connectionmanager.TESTMODE", "false"))) {
              if (str.equals("6440")) {
                Toast.makeText(CMTestModePassWordDialog.this.mContext, "TEST MODEë¡œ ì§„ìž…í•©ë‹ˆë‹¤.", 0).show();
                CMTestModePassWordDialog.this.met_password.setText("");
                CMTestModePassWordDialog.this.mActivity.showDialog(1011);
              } else {
                Toast.makeText(CMTestModePassWordDialog.this.mContext, "ìž…ë ¥ë�œ Passwordê°€ í‹€ë¦½ë‹ˆë‹¤.", 0).show();
                CMTestModePassWordDialog.this.met_password.setText("");
              } 
            } else {
              Toast.makeText(CMTestModePassWordDialog.this.mContext, "ìž…ë ¥ë�œ Passwordê°€ í‹€ë¦½ë‹ˆë‹¤.", 0).show();
              CMTestModePassWordDialog.this.met_password.setText("");
            } 
            CMTestModePassWordDialog.this.dismiss();
          }
        });
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\CMTestModePassWordDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */