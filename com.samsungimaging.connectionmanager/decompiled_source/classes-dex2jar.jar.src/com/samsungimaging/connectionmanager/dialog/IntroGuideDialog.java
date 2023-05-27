package com.samsungimaging.connectionmanager.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import com.samsungimaging.connectionmanager.app.BaseActivity;

public class IntroGuideDialog extends CustomDialog implements CompoundButton.OnCheckedChangeListener {
  private BaseActivity mActivity = null;
  
  private boolean mIsChecked = false;
  
  public IntroGuideDialog(BaseActivity paramBaseActivity) {
    super((Context)paramBaseActivity);
    this.mActivity = paramBaseActivity;
  }
  
  private void initContent() {
    if (this.mActivity instanceof com.samsungimaging.connectionmanager.app.pushservice.autoshare.AutoShare) {
      setTitle(2131361802);
      setMessage(2131362031);
    } else if (this.mActivity instanceof com.samsungimaging.connectionmanager.app.pushservice.selectivepush.SelectivePush) {
      setTitle(2131361803);
      setMessage(2131362033);
    } 
    setView(2130903057);
    setNeutralButton(17039370, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            param1DialogInterface.dismiss();
            Toast.makeText(IntroGuideDialog.this.getContext(), 2131361823, 0).show();
          }
        });
    setOnKeyListener(new DialogInterface.OnKeyListener() {
          public boolean onKey(DialogInterface param1DialogInterface, int param1Int, KeyEvent param1KeyEvent) {
            switch (param1Int) {
              default:
                return false;
              case 84:
                return true;
              case 4:
                break;
            } 
            param1DialogInterface.dismiss();
            Toast.makeText(IntroGuideDialog.this.getContext(), 2131361823, 0).show();
            return false;
          }
        });
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
    this.mActivity.getSettings().setIntroGuide(this.mIsChecked);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\IntroGuideDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */