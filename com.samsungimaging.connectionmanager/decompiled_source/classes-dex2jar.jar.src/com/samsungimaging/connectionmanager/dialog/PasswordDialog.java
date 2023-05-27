package com.samsungimaging.connectionmanager.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.app.cm.connector.CMCameraAPConnector;
import com.samsungimaging.connectionmanager.app.cm.connector.LastRequestedCameraInfo;
import com.samsungimaging.connectionmanager.app.cm.service.CMService;

public class PasswordDialog extends CustomDialog {
  private Activity mActivity;
  
  private TextView mDesc;
  
  private EditText mInputSpace;
  
  private CheckBox mPinDisplayCheckBox = null;
  
  private TextView mSSID;
  
  private TextView mSecurity;
  
  private CharSequence[] mSecurityType = (CharSequence[])new String[3];
  
  public PasswordDialog(Activity paramActivity) {
    super((Context)paramActivity);
    this.mActivity = paramActivity;
  }
  
  private void initContent() {
    setView(2130903063);
    setTitle(getContext().getResources().getString(2131361856));
    setPositiveButton(2131361817, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            LastRequestedCameraInfo.getInstance().setLastRequestedCameraSSID(null);
            PasswordDialog.this.dismiss();
          }
        });
    setNeutralButton(2131361842, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            String str = PasswordDialog.this.mInputSpace.getText().toString();
            if (PasswordDialog.this.mInputSpace.length() > 0) {
              String[] arrayOfString = new String[4];
              arrayOfString[0] = (String)PasswordDialog.this.mSecurityType[0];
              arrayOfString[1] = (String)PasswordDialog.this.mSecurityType[1];
              arrayOfString[2] = (String)PasswordDialog.this.mSecurityType[2];
              arrayOfString[3] = str;
              LastRequestedCameraInfo.getInstance().setLastRequestedCameraSSID(arrayOfString[0].toString());
              CMCameraAPConnector.getInstance().setSr((CharSequence[])arrayOfString, PasswordDialog.this.getContext(), CMService.mWifiManager);
            } else {
              LastRequestedCameraInfo.getInstance().setLastRequestedCameraSSID(null);
            } 
            PasswordDialog.this.dismiss();
          }
        });
  }
  
  private void initCustomContent() {
    this.mInputSpace = (EditText)this.mView.findViewById(2131558562);
    this.mSSID = (TextView)this.mView.findViewById(2131558560);
    this.mSecurity = (TextView)this.mView.findViewById(2131558561);
    this.mDesc = (TextView)this.mView.findViewById(2131558559);
    this.mPinDisplayCheckBox = (CheckBox)findViewById(2131558548);
    this.mPinDisplayCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
            if (param1Boolean) {
              PasswordDialog.this.mInputSpace.setInputType(1);
            } else {
              PasswordDialog.this.mInputSpace.setInputType(129);
            } 
            PasswordDialog.this.mInputSpace.setSelection(PasswordDialog.this.mInputSpace.length());
          }
        });
  }
  
  protected void onCreate(Bundle paramBundle) {
    initContent();
    super.onCreate(paramBundle);
    initCustomContent();
  }
  
  protected void onStart() {
    super.onStart();
    getWindow().clearFlags(131080);
    this.mDesc.setText(getContext().getResources().getString(2131361856));
    this.mInputSpace.setInputType(129);
    setTitle(this.mSecurityType[0]);
    this.mSSID.setVisibility(8);
    this.mSecurity.setVisibility(8);
    this.mDesc.setVisibility(8);
    this.mPinDisplayCheckBox.setChecked(false);
    if (this.mSecurityType[0].length() > 0)
      this.mSSID.setText(this.mSecurityType[0]); 
    if (this.mSecurityType[2].length() > 0)
      this.mSecurity.setText(this.mSecurityType[2]); 
  }
  
  protected void onStop() {
    super.onStop();
    this.mInputSpace.setText("");
    CMUtil.sendBroadCastToMain(getContext(), "SHOW_SEARCHAPDIALOG");
  }
  
  public void setTag(Object paramObject) {
    this.mSecurityType = (CharSequence[])paramObject;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\PasswordDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */