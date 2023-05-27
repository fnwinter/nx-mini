package com.samsungimaging.connectionmanager.dialog;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class LicenseDialog extends Activity {
  WebView web;
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setContentView(2130903058);
    this.web = (WebView)findViewById(2131558550);
    this.web.setBackgroundColor(0);
    this.web.loadUrl("file:///android_asset/open_source_license.html");
    this.web.setBackgroundColor(0);
    this.web.getSettings().setJavaScriptEnabled(true);
    ((Button)findViewById(2131558551)).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            LicenseDialog.this.onBackPressed();
          }
        });
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\LicenseDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */