package com.samsungimaging.connectionmanager.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.samsungimaging.connectionmanager.app.cm.common.CMConstants;
import com.samsungimaging.connectionmanager.app.cm.common.CMSharedPreferenceUtil;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.util.Trace;

public class InitGuideDialog extends CustomDialog {
  private Button mHelpButton = null;
  
  private CheckBox mInitCheckBox = null;
  
  private DialogInterface.OnClickListener mOnClickListener;
  
  public InitGuideDialog(Context paramContext) {
    super(paramContext);
  }
  
  private void initContent() {
    Trace.d(CMConstants.TAG_NAME, "InitGuideDialog, initContent");
    setView(2130903055);
    setNeutralButton(2131361842, (DialogInterface.OnClickListener)null);
  }
  
  private void initCustomView() {
    this.mHelpButton = (Button)findViewById(2131558547);
    this.mHelpButton.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            CMUtil.sendBroadCastToMain(InitGuideDialog.this.getContext(), "SHOW_HELPDIALOG");
            InitGuideDialog.this.dismiss();
          }
        });
    this.mInitCheckBox = (CheckBox)findViewById(2131558548);
    this.mInitCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
            param1CompoundButton.playSoundEffect(0);
            if (param1Boolean) {
              CMSharedPreferenceUtil.put(InitGuideDialog.this.getContext(), "com.samsungimaging.connectionmanager.SP_KEY_SHOW_INIT_GUIDE_DAILOG", "false");
              return;
            } 
            CMSharedPreferenceUtil.put(InitGuideDialog.this.getContext(), "com.samsungimaging.connectionmanager.SP_KEY_SHOW_INIT_GUIDE_DAILOG", "true");
          }
        });
    this.mOnClickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface param1DialogInterface, int param1Int) {
          CMUtil.sendBroadCastToMain(InitGuideDialog.this.getContext(), "SHOW_SEARCHAPDIALOG");
          InitGuideDialog.this.dismiss();
        }
      };
    setNeutralButton(2131361842, this.mOnClickListener);
  }
  
  public void onBackPressed() {
    CMUtil.sendBroadCastToMain(getContext(), "SHOW_SEARCHAPDIALOG");
    super.onBackPressed();
  }
  
  protected void onCreate(Bundle paramBundle) {
    Trace.d(CMConstants.TAG_NAME, "InitGuideDialog, onCreate");
    initContent();
    super.onCreate(paramBundle);
    initCustomView();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\InitGuideDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */