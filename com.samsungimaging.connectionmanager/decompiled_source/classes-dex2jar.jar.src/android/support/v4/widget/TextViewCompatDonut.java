package android.support.v4.widget;

import android.util.Log;
import android.widget.TextView;
import java.lang.reflect.Field;

class TextViewCompatDonut {
  private static final int LINES = 1;
  
  private static final String LOG_TAG = "TextViewCompatDonut";
  
  private static Field sMaxModeField;
  
  private static boolean sMaxModeFieldFetched;
  
  private static Field sMaximumField;
  
  private static boolean sMaximumFieldFetched;
  
  private static Field sMinModeField;
  
  private static boolean sMinModeFieldFetched;
  
  private static Field sMinimumField;
  
  private static boolean sMinimumFieldFetched;
  
  static int getMaxLines(TextView paramTextView) {
    if (!sMaxModeFieldFetched) {
      sMaxModeField = retrieveField("mMaxMode");
      sMaxModeFieldFetched = true;
    } 
    if (sMaxModeField != null && retrieveIntFromField(sMaxModeField, paramTextView) == 1) {
      if (!sMaximumFieldFetched) {
        sMaximumField = retrieveField("mMaximum");
        sMaximumFieldFetched = true;
      } 
      if (sMaximumField != null)
        return retrieveIntFromField(sMaximumField, paramTextView); 
    } 
    return -1;
  }
  
  static int getMinLines(TextView paramTextView) {
    if (!sMinModeFieldFetched) {
      sMinModeField = retrieveField("mMinMode");
      sMinModeFieldFetched = true;
    } 
    if (sMinModeField != null && retrieveIntFromField(sMinModeField, paramTextView) == 1) {
      if (!sMinimumFieldFetched) {
        sMinimumField = retrieveField("mMinimum");
        sMinimumFieldFetched = true;
      } 
      if (sMinimumField != null)
        return retrieveIntFromField(sMinimumField, paramTextView); 
    } 
    return -1;
  }
  
  private static Field retrieveField(String paramString) {
    Field field = null;
    try {
      Field field1 = TextView.class.getDeclaredField(paramString);
      field = field1;
      field1.setAccessible(true);
      return field1;
    } catch (NoSuchFieldException noSuchFieldException) {
      Log.e("TextViewCompatDonut", "Could not retrieve " + paramString + " field.");
      return field;
    } 
  }
  
  private static int retrieveIntFromField(Field paramField, TextView paramTextView) {
    try {
      return paramField.getInt(paramTextView);
    } catch (IllegalAccessException illegalAccessException) {
      Log.d("TextViewCompatDonut", "Could not retrieve value of " + paramField.getName() + " field.");
      return -1;
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\widget\TextViewCompatDonut.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */