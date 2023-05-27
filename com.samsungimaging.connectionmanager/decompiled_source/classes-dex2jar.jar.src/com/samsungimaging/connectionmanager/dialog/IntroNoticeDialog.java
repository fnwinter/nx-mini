package com.samsungimaging.connectionmanager.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.samsungimaging.connectionmanager.app.BaseActivity;

public class IntroNoticeDialog extends CustomDialog implements CompoundButton.OnCheckedChangeListener {
  private BaseActivity mActivity = null;
  
  private boolean mIsChecked = false;
  
  public IntroNoticeDialog(BaseActivity paramBaseActivity) {
    super((Context)paramBaseActivity);
    this.mActivity = paramBaseActivity;
  }
  
  private void initContent() {
    setTitle(2131361907);
    setMessage(2131362073);
    setView(2130903057);
    ((CheckBox)this.mView.findViewById(2131558548)).setOnCheckedChangeListener(this);
  }
  
  public void onCheckedChanged(CompoundButton paramCompoundButton, boolean paramBoolean) {
    paramCompoundButton.playSoundEffect(0);
    this.mIsChecked = paramBoolean;
  }
  
  protected void onCreate(Bundle paramBundle) {
    initContent();
    super.onCreate(paramBundle);
  }
  
  public void onDismiss(DialogInterface paramDialogInterface) {
    this.mActivity.getSettings().setIntroNoticeGuide(this.mIsChecked);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\IntroNoticeDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */