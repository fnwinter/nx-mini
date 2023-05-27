package android.support.v4.content;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;

public class SharedPreferencesCompat {
  public static class EditorCompat {
    private static EditorCompat sInstance;
    
    private final Helper mHelper;
    
    private EditorCompat() {
      if (Build.VERSION.SDK_INT >= 9) {
        this.mHelper = new EditorHelperApi9Impl();
        return;
      } 
      this.mHelper = new EditorHelperBaseImpl();
    }
    
    public static EditorCompat getInstance() {
      if (sInstance == null)
        sInstance = new EditorCompat(); 
      return sInstance;
    }
    
    public void apply(@NonNull SharedPreferences.Editor param1Editor) {
      this.mHelper.apply(param1Editor);
    }
    
    private static class EditorHelperApi9Impl implements Helper {
      private EditorHelperApi9Impl() {}
      
      public void apply(@NonNull SharedPreferences.Editor param2Editor) {
        EditorCompatGingerbread.apply(param2Editor);
      }
    }
    
    private static class EditorHelperBaseImpl implements Helper {
      private EditorHelperBaseImpl() {}
      
      public void apply(@NonNull SharedPreferences.Editor param2Editor) {
        param2Editor.commit();
      }
    }
    
    private static interface Helper {
      void apply(@NonNull SharedPreferences.Editor param2Editor);
    }
  }
  
  private static class EditorHelperApi9Impl implements EditorCompat.Helper {
    private EditorHelperApi9Impl() {}
    
    public void apply(@NonNull SharedPreferences.Editor param1Editor) {
      EditorCompatGingerbread.apply(param1Editor);
    }
  }
  
  private static class EditorHelperBaseImpl implements EditorCompat.Helper {
    private EditorHelperBaseImpl() {}
    
    public void apply(@NonNull SharedPreferences.Editor param1Editor) {
      param1Editor.commit();
    }
  }
  
  private static interface Helper {
    void apply(@NonNull SharedPreferences.Editor param1Editor);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\content\SharedPreferencesCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */