package com.samsungimaging.connectionmanager.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.app.BaseGalleryActivity;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;

public class NFCConnectionGuideDialog extends CustomDialog implements CompoundButton.OnCheckedChangeListener {
  private BaseGalleryActivity mActivity = null;
  
  private AnimationDrawable mAnimation = null;
  
  private boolean mIsChecked = false;
  
  public NFCConnectionGuideDialog(BaseGalleryActivity paramBaseGalleryActivity) {
    super((Context)paramBaseGalleryActivity, 2131427331);
    this.mActivity = paramBaseGalleryActivity;
  }
  
  private void initContent() {
    setTitle(2131361878);
    setView(2130903060);
    setNeutralButton(2131361817, (DialogInterface.OnClickListener)null);
    setOnDismissListener(this);
    this.mAnimation = (AnimationDrawable)((ImageView)this.mView.findViewById(2131558555)).getBackground();
    CheckBox checkBox = (CheckBox)this.mView.findViewById(2131558548);
    checkBox.setChecked(this.mIsChecked);
    checkBox.setOnCheckedChangeListener(this);
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
    if (((Boolean)getTag()).booleanValue()) {
      this.mActivity.getSettings().setNFCConnectionGuide(this.mIsChecked);
      CMUtil.sendBroadCastToMain(getContext(), "SHOW_SEARCHAPDIALOG");
    } 
  }
  
  public void onOrientationChanged() {
    initContent();
    initialize();
    onStart();
  }
  
  protected void onStart() {
    this.mAnimation.start();
    CheckBox checkBox = (CheckBox)this.mView.findViewById(2131558548);
    TextView textView = (TextView)this.mView.findViewById(2131558556);
    if (((Boolean)getTag()).booleanValue()) {
      checkBox.setVisibility(0);
      textView.setVisibility(0);
    } else {
      checkBox.setVisibility(8);
      textView.setVisibility(8);
    } 
    super.onStart();
  }
  
  protected void onStop() {
    this.mAnimation.stop();
    super.onStop();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\NFCConnectionGuideDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */