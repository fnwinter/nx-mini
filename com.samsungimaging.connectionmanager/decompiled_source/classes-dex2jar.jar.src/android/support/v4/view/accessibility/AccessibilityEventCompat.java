package android.support.v4.view.accessibility;

import android.os.Build;
import android.view.accessibility.AccessibilityEvent;

public class AccessibilityEventCompat {
  public static final int CONTENT_CHANGE_TYPE_CONTENT_DESCRIPTION = 4;
  
  public static final int CONTENT_CHANGE_TYPE_SUBTREE = 1;
  
  public static final int CONTENT_CHANGE_TYPE_TEXT = 2;
  
  public static final int CONTENT_CHANGE_TYPE_UNDEFINED = 0;
  
  private static final AccessibilityEventVersionImpl IMPL = new AccessibilityEventStubImpl();
  
  public static final int TYPES_ALL_MASK = -1;
  
  public static final int TYPE_ANNOUNCEMENT = 16384;
  
  public static final int TYPE_GESTURE_DETECTION_END = 524288;
  
  public static final int TYPE_GESTURE_DETECTION_START = 262144;
  
  public static final int TYPE_TOUCH_EXPLORATION_GESTURE_END = 1024;
  
  public static final int TYPE_TOUCH_EXPLORATION_GESTURE_START = 512;
  
  public static final int TYPE_TOUCH_INTERACTION_END = 2097152;
  
  public static final int TYPE_TOUCH_INTERACTION_START = 1048576;
  
  public static final int TYPE_VIEW_ACCESSIBILITY_FOCUSED = 32768;
  
  public static final int TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED = 65536;
  
  public static final int TYPE_VIEW_HOVER_ENTER = 128;
  
  public static final int TYPE_VIEW_HOVER_EXIT = 256;
  
  public static final int TYPE_VIEW_SCROLLED = 4096;
  
  public static final int TYPE_VIEW_TEXT_SELECTION_CHANGED = 8192;
  
  public static final int TYPE_VIEW_TEXT_TRAVERSED_AT_MOVEMENT_GRANULARITY = 131072;
  
  public static final int TYPE_WINDOW_CONTENT_CHANGED = 2048;
  
  public static void appendRecord(AccessibilityEvent paramAccessibilityEvent, AccessibilityRecordCompat paramAccessibilityRecordCompat) {
    IMPL.appendRecord(paramAccessibilityEvent, paramAccessibilityRecordCompat.getImpl());
  }
  
  public static AccessibilityRecordCompat asRecord(AccessibilityEvent paramAccessibilityEvent) {
    return new AccessibilityRecordCompat(paramAccessibilityEvent);
  }
  
  public static int getContentChangeTypes(AccessibilityEvent paramAccessibilityEvent) {
    return IMPL.getContentChangeTypes(paramAccessibilityEvent);
  }
  
  public static AccessibilityRecordCompat getRecord(AccessibilityEvent paramAccessibilityEvent, int paramInt) {
    return new AccessibilityRecordCompat(IMPL.getRecord(paramAccessibilityEvent, paramInt));
  }
  
  public static int getRecordCount(AccessibilityEvent paramAccessibilityEvent) {
    return IMPL.getRecordCount(paramAccessibilityEvent);
  }
  
  public static void setContentChangeTypes(AccessibilityEvent paramAccessibilityEvent, int paramInt) {
    IMPL.setContentChangeTypes(paramAccessibilityEvent, paramInt);
  }
  
  static {
    if (Build.VERSION.SDK_INT >= 19) {
      IMPL = new AccessibilityEventKitKatImpl();
      return;
    } 
    if (Build.VERSION.SDK_INT >= 14) {
      IMPL = new AccessibilityEventIcsImpl();
      return;
    } 
  }
  
  static class AccessibilityEventIcsImpl extends AccessibilityEventStubImpl {
    public void appendRecord(AccessibilityEvent param1AccessibilityEvent, Object param1Object) {
      AccessibilityEventCompatIcs.appendRecord(param1AccessibilityEvent, param1Object);
    }
    
    public Object getRecord(AccessibilityEvent param1AccessibilityEvent, int param1Int) {
      return AccessibilityEventCompatIcs.getRecord(param1AccessibilityEvent, param1Int);
    }
    
    public int getRecordCount(AccessibilityEvent param1AccessibilityEvent) {
      return AccessibilityEventCompatIcs.getRecordCount(param1AccessibilityEvent);
    }
  }
  
  static class AccessibilityEventKitKatImpl extends AccessibilityEventIcsImpl {
    public int getContentChangeTypes(AccessibilityEvent param1AccessibilityEvent) {
      return AccessibilityEventCompatKitKat.getContentChangeTypes(param1AccessibilityEvent);
    }
    
    public void setContentChangeTypes(AccessibilityEvent param1AccessibilityEvent, int param1Int) {
      AccessibilityEventCompatKitKat.setContentChangeTypes(param1AccessibilityEvent, param1Int);
    }
  }
  
  static class AccessibilityEventStubImpl implements AccessibilityEventVersionImpl {
    public void appendRecord(AccessibilityEvent param1AccessibilityEvent, Object param1Object) {}
    
    public int getContentChangeTypes(AccessibilityEvent param1AccessibilityEvent) {
      return 0;
    }
    
    public Object getRecord(AccessibilityEvent param1AccessibilityEvent, int param1Int) {
      return null;
    }
    
    public int getRecordCount(AccessibilityEvent param1AccessibilityEvent) {
      return 0;
    }
    
    public void setContentChangeTypes(AccessibilityEvent param1AccessibilityEvent, int param1Int) {}
  }
  
  static interface AccessibilityEventVersionImpl {
    void appendRecord(AccessibilityEvent param1AccessibilityEvent, Object param1Object);
    
    int getContentChangeTypes(AccessibilityEvent param1AccessibilityEvent);
    
    Object getRecord(AccessibilityEvent param1AccessibilityEvent, int param1Int);
    
    int getRecordCount(AccessibilityEvent param1AccessibilityEvent);
    
    void setContentChangeTypes(AccessibilityEvent param1AccessibilityEvent, int param1Int);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\view\accessibility\AccessibilityEventCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */