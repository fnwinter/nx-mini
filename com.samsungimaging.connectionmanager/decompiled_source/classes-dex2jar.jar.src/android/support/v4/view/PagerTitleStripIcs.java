package android.support.v4.view;

import android.content.Context;
import android.text.method.SingleLineTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.widget.TextView;
import java.util.Locale;

class PagerTitleStripIcs {
  public static void setSingleLineAllCaps(TextView paramTextView) {
    paramTextView.setTransformationMethod((TransformationMethod)new SingleLineAllCapsTransform(paramTextView.getContext()));
  }
  
  private static class SingleLineAllCapsTransform extends SingleLineTransformationMethod {
    private static final String TAG = "SingleLineAllCapsTransform";
    
    private Locale mLocale;
    
    public SingleLineAllCapsTransform(Context param1Context) {
      this.mLocale = (param1Context.getResources().getConfiguration()).locale;
    }
    
    public CharSequence getTransformation(CharSequence param1CharSequence, View param1View) {
      param1CharSequence = super.getTransformation(param1CharSequence, param1View);
      return (param1CharSequence != null) ? param1CharSequence.toString().toUpperCase(this.mLocale) : null;
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\view\PagerTitleStripIcs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */