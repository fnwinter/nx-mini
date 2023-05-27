package com.samsungimaging.connectionmanager.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.app.cm.common.CMConstants;
import com.samsungimaging.connectionmanager.app.cm.common.CMSharedPreferenceUtil;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.util.Trace;
import java.util.ArrayList;

public class CMTestModeDialog extends Dialog {
  private ArrayList<String> mArrayList_testmode_forSpinner;
  
  private ArrayList<String> mArrayList_testmode_forSpinner_2;
  
  private ArrayList<String> mArrayList_testmode_forSpinner_3;
  
  Context mContext = null;
  
  EditText met_mac01;
  
  EditText met_mac01_2;
  
  EditText met_mac01_3;
  
  EditText met_mac02;
  
  EditText met_mac02_2;
  
  EditText met_mac02_3;
  
  EditText met_mac03;
  
  EditText met_mac03_2;
  
  EditText met_mac03_3;
  
  EditText met_model;
  
  EditText met_model_2;
  
  EditText met_model_3;
  
  TextView mtv_ssid;
  
  TextView mtv_ssid_2;
  
  TextView mtv_ssid_3;
  
  private String selected_item = "AP_SSC_";
  
  private String selected_item_2 = "AP_SSC_";
  
  private String selected_item_3 = "AP_SSC_";
  
  TextWatcher watcher = new TextWatcher() {
      public void afterTextChanged(Editable param1Editable) {}
      
      public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
      
      public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {
        CMTestModeDialog.this.setSSID();
      }
    };
  
  TextWatcher watcher_2 = new TextWatcher() {
      public void afterTextChanged(Editable param1Editable) {}
      
      public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
      
      public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {
        CMTestModeDialog.this.setSSID2();
      }
    };
  
  TextWatcher watcher_3 = new TextWatcher() {
      public void afterTextChanged(Editable param1Editable) {}
      
      public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
      
      public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {
        CMTestModeDialog.this.setSSID3();
      }
    };
  
  public CMTestModeDialog(Context paramContext, int paramInt) {
    super(paramContext, paramInt);
    this.mContext = paramContext;
  }
  
  private void enableEditTextArea(boolean paramBoolean) {
    if (paramBoolean) {
      this.met_model.setEnabled(true);
      this.met_mac01.setEnabled(true);
      this.met_mac02.setEnabled(true);
      this.met_mac03.setEnabled(true);
      return;
    } 
    this.met_model.setEnabled(false);
    this.met_mac01.setEnabled(false);
    this.met_mac02.setEnabled(false);
    this.met_mac03.setEnabled(false);
    this.met_model.setText("");
    this.met_mac01.setText("");
    this.met_mac02.setText("");
    this.met_mac03.setText("");
  }
  
  private void enableEditTextArea2(boolean paramBoolean) {
    if (paramBoolean) {
      this.met_model_2.setEnabled(true);
      this.met_mac01_2.setEnabled(true);
      this.met_mac02_2.setEnabled(true);
      this.met_mac03_2.setEnabled(true);
      return;
    } 
    this.met_model_2.setEnabled(false);
    this.met_mac01_2.setEnabled(false);
    this.met_mac02_2.setEnabled(false);
    this.met_mac03_2.setEnabled(false);
    this.met_model_2.setText("");
    this.met_mac01_2.setText("");
    this.met_mac02_2.setText("");
    this.met_mac03_2.setText("");
  }
  
  private void enableEditTextArea3(boolean paramBoolean) {
    if (paramBoolean) {
      this.met_model_3.setEnabled(true);
      this.met_mac01_3.setEnabled(true);
      this.met_mac02_3.setEnabled(true);
      this.met_mac03_3.setEnabled(true);
      return;
    } 
    this.met_model_3.setEnabled(false);
    this.met_mac01_3.setEnabled(false);
    this.met_mac02_3.setEnabled(false);
    this.met_mac03_3.setEnabled(false);
    this.met_model_3.setText("");
    this.met_mac01_3.setText("");
    this.met_mac02_3.setText("");
    this.met_mac03_3.setText("");
  }
  
  private void setSSID() {
    String str2 = this.met_model.getText().toString();
    String str3 = this.met_mac01.getText().toString();
    String str4 = this.met_mac02.getText().toString();
    String str5 = this.met_mac03.getText().toString();
    String str1 = this.selected_item;
    if (this.selected_item.startsWith(this.mArrayList_testmode_forSpinner.get(0))) {
      if (!str2.equals("")) {
        str1 = String.valueOf(this.selected_item) + str2.toUpperCase() + "_0-";
        if (!str3.equals("")) {
          str1 = String.valueOf(this.selected_item) + str2.toUpperCase() + "_0-" + str3.toUpperCase();
          if (!str4.equals("")) {
            str1 = String.valueOf(this.selected_item) + str2.toUpperCase() + "_0-" + str3.toUpperCase() + ":" + str4.toUpperCase();
            if (!str5.equals(""))
              str1 = String.valueOf(this.selected_item) + str2.toUpperCase() + "_0-" + str3.toUpperCase() + ":" + str4.toUpperCase() + ":" + str5.toUpperCase(); 
          } 
        } 
      } 
    } else if (this.selected_item.startsWith(this.mArrayList_testmode_forSpinner.get(1)) || this.selected_item.startsWith(this.mArrayList_testmode_forSpinner.get(2)) || this.selected_item.startsWith(this.mArrayList_testmode_forSpinner.get(3))) {
      if (!str2.equals("")) {
        str1 = String.valueOf(this.selected_item) + str2.toUpperCase() + "-";
        if (!str3.equals("")) {
          str1 = String.valueOf(this.selected_item) + str2.toUpperCase() + "-" + str3.toUpperCase();
          if (!str4.equals("")) {
            str1 = String.valueOf(this.selected_item) + str2.toUpperCase() + "-" + str3.toUpperCase() + ":" + str4.toUpperCase();
            if (!str5.equals(""))
              str1 = String.valueOf(this.selected_item) + str2.toUpperCase() + "-" + str3.toUpperCase() + ":" + str4.toUpperCase() + ":" + str5.toUpperCase(); 
          } 
        } 
      } 
    } else if (this.selected_item.startsWith(this.mArrayList_testmode_forSpinner.get(4))) {
      CMSharedPreferenceUtil.put(this.mContext, "com.samsungimaging.connectionmanager.TESTMODE_SSID", "");
      str1 = this.selected_item;
    } 
    this.mtv_ssid.setText(str1);
  }
  
  private void setSSID2() {
    String str2 = this.met_model_2.getText().toString();
    String str3 = this.met_mac01_2.getText().toString();
    String str4 = this.met_mac02_2.getText().toString();
    String str5 = this.met_mac03_2.getText().toString();
    String str1 = this.selected_item_2;
    if (this.selected_item_2.startsWith(this.mArrayList_testmode_forSpinner_2.get(0))) {
      if (!str2.equals("")) {
        str1 = String.valueOf(this.selected_item_2) + str2.toUpperCase() + "_0-";
        if (!str3.equals("")) {
          str1 = String.valueOf(this.selected_item_2) + str2.toUpperCase() + "_0-" + str3.toUpperCase();
          if (!str4.equals("")) {
            str1 = String.valueOf(this.selected_item_2) + str2.toUpperCase() + "_0-" + str3.toUpperCase() + ":" + str4.toUpperCase();
            if (!str5.equals(""))
              str1 = String.valueOf(this.selected_item_2) + str2.toUpperCase() + "_0-" + str3.toUpperCase() + ":" + str4.toUpperCase() + ":" + str5.toUpperCase(); 
          } 
        } 
      } 
    } else if (this.selected_item_2.startsWith(this.mArrayList_testmode_forSpinner_2.get(1)) || this.selected_item_2.startsWith(this.mArrayList_testmode_forSpinner_2.get(2)) || this.selected_item_2.startsWith(this.mArrayList_testmode_forSpinner_2.get(3))) {
      if (!str2.equals("")) {
        str1 = String.valueOf(this.selected_item_2) + str2.toUpperCase() + "-";
        if (!str3.equals("")) {
          str1 = String.valueOf(this.selected_item_2) + str2.toUpperCase() + "-" + str3.toUpperCase();
          if (!str4.equals("")) {
            str1 = String.valueOf(this.selected_item_2) + str2.toUpperCase() + "-" + str3.toUpperCase() + ":" + str4.toUpperCase();
            if (!str5.equals(""))
              str1 = String.valueOf(this.selected_item_2) + str2.toUpperCase() + "-" + str3.toUpperCase() + ":" + str4.toUpperCase() + ":" + str5.toUpperCase(); 
          } 
        } 
      } 
    } else if (this.selected_item_2.startsWith(this.mArrayList_testmode_forSpinner_2.get(4))) {
      CMSharedPreferenceUtil.put(this.mContext, "com.samsungimaging.connectionmanager.TESTMODE_SSID2", "");
      str1 = this.selected_item_2;
    } 
    this.mtv_ssid_2.setText(str1);
  }
  
  private void setSSID3() {
    String str2 = this.met_model_3.getText().toString();
    String str3 = this.met_mac01_3.getText().toString();
    String str4 = this.met_mac02_3.getText().toString();
    String str5 = this.met_mac03_3.getText().toString();
    String str1 = this.selected_item_3;
    if (this.selected_item_3.startsWith(this.mArrayList_testmode_forSpinner_3.get(0))) {
      if (!str2.equals("")) {
        str1 = String.valueOf(this.selected_item_3) + str2.toUpperCase() + "_0-";
        if (!str3.equals("")) {
          str1 = String.valueOf(this.selected_item_3) + str2.toUpperCase() + "_0-" + str3.toUpperCase();
          if (!str4.equals("")) {
            str1 = String.valueOf(this.selected_item_3) + str2.toUpperCase() + "_0-" + str3.toUpperCase() + ":" + str4.toUpperCase();
            if (!str5.equals(""))
              str1 = String.valueOf(this.selected_item_3) + str2.toUpperCase() + "_0-" + str3.toUpperCase() + ":" + str4.toUpperCase() + ":" + str5.toUpperCase(); 
          } 
        } 
      } 
    } else if (this.selected_item_3.startsWith(this.mArrayList_testmode_forSpinner_3.get(1)) || this.selected_item_3.startsWith(this.mArrayList_testmode_forSpinner_3.get(2)) || this.selected_item_3.startsWith(this.mArrayList_testmode_forSpinner_3.get(3))) {
      if (!str2.equals("")) {
        str1 = String.valueOf(this.selected_item_3) + str2.toUpperCase() + "-";
        if (!str3.equals("")) {
          str1 = String.valueOf(this.selected_item_3) + str2.toUpperCase() + "-" + str3.toUpperCase();
          if (!str4.equals("")) {
            str1 = String.valueOf(this.selected_item_3) + str2.toUpperCase() + "-" + str3.toUpperCase() + ":" + str4.toUpperCase();
            if (!str5.equals(""))
              str1 = String.valueOf(this.selected_item_3) + str2.toUpperCase() + "-" + str3.toUpperCase() + ":" + str4.toUpperCase() + ":" + str5.toUpperCase(); 
          } 
        } 
      } 
    } else if (this.selected_item_3.startsWith(this.mArrayList_testmode_forSpinner_3.get(4))) {
      CMSharedPreferenceUtil.put(this.mContext, "com.samsungimaging.connectionmanager.TESTMODE_SSID3", "");
      str1 = this.selected_item_3;
    } 
    this.mtv_ssid_3.setText(str1);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    setContentView(2130903047);
    this.mtv_ssid = (TextView)findViewById(2131558508);
    this.met_model = (EditText)findViewById(2131558503);
    this.met_model.addTextChangedListener(this.watcher);
    this.met_mac01 = (EditText)findViewById(2131558504);
    this.met_mac01.addTextChangedListener(this.watcher);
    this.met_mac02 = (EditText)findViewById(2131558505);
    this.met_mac02.addTextChangedListener(this.watcher);
    this.met_mac03 = (EditText)findViewById(2131558506);
    this.met_mac03.addTextChangedListener(this.watcher);
    this.mtv_ssid_2 = (TextView)findViewById(2131558514);
    this.met_model_2 = (EditText)findViewById(2131558507);
    this.met_model_2.addTextChangedListener(this.watcher_2);
    this.met_mac01_2 = (EditText)findViewById(2131558510);
    this.met_mac01_2.addTextChangedListener(this.watcher_2);
    this.met_mac02_2 = (EditText)findViewById(2131558511);
    this.met_mac02_2.addTextChangedListener(this.watcher_2);
    this.met_mac03_2 = (EditText)findViewById(2131558512);
    this.met_mac03_2.addTextChangedListener(this.watcher_2);
    this.mtv_ssid_3 = (TextView)findViewById(2131558519);
    this.met_model_3 = (EditText)findViewById(2131558513);
    this.met_model_3.addTextChangedListener(this.watcher_3);
    this.met_mac01_3 = (EditText)findViewById(2131558516);
    this.met_mac01_3.addTextChangedListener(this.watcher_3);
    this.met_mac02_3 = (EditText)findViewById(2131558517);
    this.met_mac02_3.addTextChangedListener(this.watcher_3);
    this.met_mac03_3 = (EditText)findViewById(2131558518);
    this.met_mac03_3.addTextChangedListener(this.watcher_3);
    enableEditTextArea(true);
    enableEditTextArea2(true);
    enableEditTextArea3(true);
    this.mArrayList_testmode_forSpinner = new ArrayList<String>();
    this.mArrayList_testmode_forSpinner.add("AP_SSC_");
    this.mArrayList_testmode_forSpinner.add("EK_");
    this.mArrayList_testmode_forSpinner.add("SM_");
    this.mArrayList_testmode_forSpinner.add("GC_");
    this.mArrayList_testmode_forSpinner.add("NONE");
    ArrayAdapter arrayAdapter1 = new ArrayAdapter(this.mContext, 17367048, this.mArrayList_testmode_forSpinner);
    arrayAdapter1.setDropDownViewResource(2130903048);
    Spinner spinner1 = (Spinner)findViewById(2131558502);
    spinner1.setAdapter((SpinnerAdapter)arrayAdapter1);
    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          public void onItemSelected(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            CMTestModeDialog.this.selected_item = String.valueOf(CMTestModeDialog.this.mArrayList_testmode_forSpinner.get(param1Int));
            if (CMTestModeDialog.this.selected_item.equals("NONE")) {
              CMTestModeDialog.this.enableEditTextArea(false);
            } else {
              CMTestModeDialog.this.enableEditTextArea(true);
            } 
            CMTestModeDialog.this.setSSID();
            Trace.d(CMConstants.TAG_NAME, "arg2 = " + param1Int + ", selected_item = " + CMTestModeDialog.this.selected_item);
          }
          
          public void onNothingSelected(AdapterView<?> param1AdapterView) {
            Trace.d(CMConstants.TAG_NAME, "onNothingSelected.");
          }
        });
    this.mArrayList_testmode_forSpinner_2 = new ArrayList<String>();
    this.mArrayList_testmode_forSpinner_2.add("AP_SSC_");
    this.mArrayList_testmode_forSpinner_2.add("EK_");
    this.mArrayList_testmode_forSpinner_2.add("SM_");
    this.mArrayList_testmode_forSpinner_2.add("GC_");
    this.mArrayList_testmode_forSpinner_2.add("NONE");
    ArrayAdapter arrayAdapter2 = new ArrayAdapter(this.mContext, 17367048, this.mArrayList_testmode_forSpinner_2);
    arrayAdapter2.setDropDownViewResource(2130903048);
    Spinner spinner2 = (Spinner)findViewById(2131558509);
    spinner2.setAdapter((SpinnerAdapter)arrayAdapter2);
    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          public void onItemSelected(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            CMTestModeDialog.this.selected_item_2 = String.valueOf(CMTestModeDialog.this.mArrayList_testmode_forSpinner_2.get(param1Int));
            if (CMTestModeDialog.this.selected_item_2.equals("NONE")) {
              CMTestModeDialog.this.enableEditTextArea2(false);
            } else {
              CMTestModeDialog.this.enableEditTextArea2(true);
            } 
            CMTestModeDialog.this.setSSID2();
            Trace.d(CMConstants.TAG_NAME, "arg2 = " + param1Int + ", selected_item_2 = " + CMTestModeDialog.this.selected_item_2);
          }
          
          public void onNothingSelected(AdapterView<?> param1AdapterView) {
            Trace.d(CMConstants.TAG_NAME, "onNothingSelected.");
          }
        });
    this.mArrayList_testmode_forSpinner_3 = new ArrayList<String>();
    this.mArrayList_testmode_forSpinner_3.add("AP_SSC_");
    this.mArrayList_testmode_forSpinner_3.add("EK_");
    this.mArrayList_testmode_forSpinner_3.add("SM_");
    this.mArrayList_testmode_forSpinner_3.add("GC_");
    this.mArrayList_testmode_forSpinner_3.add("NONE");
    ArrayAdapter arrayAdapter3 = new ArrayAdapter(this.mContext, 17367048, this.mArrayList_testmode_forSpinner_3);
    arrayAdapter3.setDropDownViewResource(2130903048);
    Spinner spinner3 = (Spinner)findViewById(2131558515);
    spinner3.setAdapter((SpinnerAdapter)arrayAdapter3);
    spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          public void onItemSelected(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
            CMTestModeDialog.this.selected_item_3 = String.valueOf(CMTestModeDialog.this.mArrayList_testmode_forSpinner_3.get(param1Int));
            if (CMTestModeDialog.this.selected_item_3.equals("NONE")) {
              CMTestModeDialog.this.enableEditTextArea3(false);
            } else {
              CMTestModeDialog.this.enableEditTextArea3(true);
            } 
            CMTestModeDialog.this.setSSID3();
            Trace.d(CMConstants.TAG_NAME, "arg2 = " + param1Int + ", selected_item_3 = " + CMTestModeDialog.this.selected_item_3);
          }
          
          public void onNothingSelected(AdapterView<?> param1AdapterView) {
            Trace.d(CMConstants.TAG_NAME, "onNothingSelected.");
          }
        });
    this.met_model.addTextChangedListener(this.watcher);
    this.met_model_2.addTextChangedListener(this.watcher_2);
    this.met_model_3.addTextChangedListener(this.watcher_3);
    ((Button)findViewById(2131558520)).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            String str2 = CMTestModeDialog.this.mtv_ssid.getText().toString();
            String str1 = str2;
            if (str2.length() != 0) {
              str1 = str2;
              if (str2.equals("NONE"))
                str1 = ""; 
            } 
            String str3 = CMTestModeDialog.this.mtv_ssid_2.getText().toString();
            str2 = str3;
            if (str3.length() != 0) {
              str2 = str3;
              if (str3.equals("NONE"))
                str2 = ""; 
            } 
            String str4 = CMTestModeDialog.this.mtv_ssid_3.getText().toString();
            str3 = str4;
            if (str4.length() != 0) {
              str3 = str4;
              if (str4.equals("NONE"))
                str3 = ""; 
            } 
            Trace.d(CMConstants.TAG_NAME, "test mode Dialog, ssid_temp = " + str1);
            Trace.d(CMConstants.TAG_NAME, "test mode Dialog, ssid_temp_2 = " + str2);
            Trace.d(CMConstants.TAG_NAME, "test mode Dialog, ssid_temp_3 = " + str3);
            ArrayList<String> arrayList = new ArrayList();
            arrayList.add(str1);
            arrayList.add(str2);
            arrayList.add(str3);
            str4 = "";
            if ("true".equalsIgnoreCase(CMSharedPreferenceUtil.getString(CMTestModeDialog.this.mContext, "com.samsungimaging.connectionmanager.TESTMODE", "false"))) {
              int i = 0;
              while (true) {
                if (i >= arrayList.size()) {
                  Trace.d(CMConstants.TAG_NAME, "test mode Dialog, title = " + str4);
                } else {
                  String str6 = arrayList.get(i);
                  String str5 = str4;
                  if (str6.length() != 0) {
                    str5 = str4;
                    if (!str6.equals("NONE"))
                      str5 = String.valueOf(str4) + "(" + str6 + ")"; 
                  } 
                  i++;
                  str4 = str5;
                  continue;
                } 
                CMSharedPreferenceUtil.put(CMTestModeDialog.this.mContext, "com.samsungimaging.connectionmanager.TESTMODE_SSID", str1);
                CMSharedPreferenceUtil.put(CMTestModeDialog.this.mContext, "com.samsungimaging.connectionmanager.TESTMODE_SSID2", str2);
                CMSharedPreferenceUtil.put(CMTestModeDialog.this.mContext, "com.samsungimaging.connectionmanager.TESTMODE_SSID3", str3);
                CMUtil.setdscPrefix_fortest(CMTestModeDialog.this.mContext, arrayList);
                CMTestModeDialog.this.dismiss();
                return;
              } 
            } 
            CMSharedPreferenceUtil.put(CMTestModeDialog.this.mContext, "com.samsungimaging.connectionmanager.TESTMODE_SSID", str1);
            CMSharedPreferenceUtil.put(CMTestModeDialog.this.mContext, "com.samsungimaging.connectionmanager.TESTMODE_SSID2", str2);
            CMSharedPreferenceUtil.put(CMTestModeDialog.this.mContext, "com.samsungimaging.connectionmanager.TESTMODE_SSID3", str3);
            CMUtil.setdscPrefix_fortest(CMTestModeDialog.this.mContext, arrayList);
            CMTestModeDialog.this.dismiss();
          }
        });
    String str2 = CMSharedPreferenceUtil.getString(this.mContext, "com.samsungimaging.connectionmanager.TESTMODE_SSID", "");
    if (str2.length() > 0) {
      if (str2.startsWith(this.mArrayList_testmode_forSpinner.get(0))) {
        spinner1.setSelection(0);
      } else if (str2.startsWith(this.mArrayList_testmode_forSpinner.get(1))) {
        spinner1.setSelection(1);
      } else if (str2.startsWith(this.mArrayList_testmode_forSpinner.get(2))) {
        spinner1.setSelection(2);
      } else if (str2.startsWith(this.mArrayList_testmode_forSpinner.get(3))) {
        spinner1.setSelection(3);
      } 
    } else {
      spinner1.setSelection(4);
    } 
    String str1 = CMSharedPreferenceUtil.getString(this.mContext, "com.samsungimaging.connectionmanager.TESTMODE_SSID2", "");
    if (str1.length() > 0) {
      if (str1.startsWith(this.mArrayList_testmode_forSpinner_2.get(0))) {
        spinner2.setSelection(0);
      } else if (str1.startsWith(this.mArrayList_testmode_forSpinner_2.get(1))) {
        spinner2.setSelection(1);
      } else if (str1.startsWith(this.mArrayList_testmode_forSpinner_2.get(2))) {
        spinner2.setSelection(2);
      } else if (str1.startsWith(this.mArrayList_testmode_forSpinner_2.get(3))) {
        spinner2.setSelection(3);
      } 
    } else {
      spinner2.setSelection(4);
    } 
    str1 = CMSharedPreferenceUtil.getString(this.mContext, "com.samsungimaging.connectionmanager.TESTMODE_SSID3", "");
    if (str1.length() > 0) {
      if (str1.startsWith(this.mArrayList_testmode_forSpinner_3.get(0))) {
        spinner3.setSelection(0);
        return;
      } 
      if (str1.startsWith(this.mArrayList_testmode_forSpinner_3.get(1))) {
        spinner3.setSelection(1);
        return;
      } 
      if (str1.startsWith(this.mArrayList_testmode_forSpinner_3.get(2))) {
        spinner3.setSelection(2);
        return;
      } 
      if (str1.startsWith(this.mArrayList_testmode_forSpinner_3.get(3))) {
        spinner3.setSelection(3);
        return;
      } 
      return;
    } 
    spinner3.setSelection(4);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\CMTestModeDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */