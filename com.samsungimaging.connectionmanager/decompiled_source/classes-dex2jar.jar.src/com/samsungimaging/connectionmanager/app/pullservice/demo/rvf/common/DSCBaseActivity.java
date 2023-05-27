package com.samsungimaging.connectionmanager.app.pullservice.demo.rvf.common;

import android.app.Activity;
import android.os.Bundle;
import com.samsungimaging.connectionmanager.util.Trace;

public class DSCBaseActivity extends Activity {
  private Trace.Tag TAG = Trace.Tag.RVF;
  
  protected void onCreate(Bundle paramBundle) {
    Trace.d(this.TAG, "start onCreate()");
    super.onCreate(paramBundle);
  }
  
  protected void onDestroy() {
    Trace.d(this.TAG, "start onDestroy()");
    super.onDestroy();
  }
  
  protected void onPause() {
    Trace.d(this.TAG, "start onPause()");
    super.onPause();
  }
  
  protected void onRestart() {
    Trace.d(this.TAG, "start onRestart()");
    super.onRestart();
  }
  
  protected void onResume() {
    Trace.d(this.TAG, "start onResume()");
    super.onResume();
  }
  
  protected void onStart() {
    Trace.d(this.TAG, "start onStart()");
    super.onStart();
  }
  
  protected void onStop() {
    Trace.d(this.TAG, "start onStop()");
    super.onStop();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\rvf\common\DSCBaseActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */