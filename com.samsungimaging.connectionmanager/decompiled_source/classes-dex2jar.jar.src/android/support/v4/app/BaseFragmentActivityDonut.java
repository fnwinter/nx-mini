package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

abstract class BaseFragmentActivityDonut extends Activity {
  abstract View dispatchFragmentsOnCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet);
  
  protected void onCreate(Bundle paramBundle) {
    if (Build.VERSION.SDK_INT < 11 && getLayoutInflater().getFactory() == null)
      getLayoutInflater().setFactory((LayoutInflater.Factory)this); 
    super.onCreate(paramBundle);
  }
  
  public View onCreateView(String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    View view2 = dispatchFragmentsOnCreateView(null, paramString, paramContext, paramAttributeSet);
    View view1 = view2;
    if (view2 == null)
      view1 = super.onCreateView(paramString, paramContext, paramAttributeSet); 
    return view1;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\app\BaseFragmentActivityDonut.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */