package android.support.v4.content;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

class EditorCompatGingerbread {
  public static void apply(@NonNull SharedPreferences.Editor paramEditor) {
    try {
      paramEditor.apply();
      return;
    } catch (AbstractMethodError abstractMethodError) {
      paramEditor.commit();
      return;
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\content\EditorCompatGingerbread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */