package android.support.v4.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.os.Build;
import android.view.accessibility.AccessibilityManager;
import java.util.Collections;
import java.util.List;

public class AccessibilityManagerCompat {
  private static final AccessibilityManagerVersionImpl IMPL = new AccessibilityManagerStubImpl();
  
  public static boolean addAccessibilityStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityStateChangeListenerCompat paramAccessibilityStateChangeListenerCompat) {
    return IMPL.addAccessibilityStateChangeListener(paramAccessibilityManager, paramAccessibilityStateChangeListenerCompat);
  }
  
  public static List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager paramAccessibilityManager, int paramInt) {
    return IMPL.getEnabledAccessibilityServiceList(paramAccessibilityManager, paramInt);
  }
  
  public static List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager paramAccessibilityManager) {
    return IMPL.getInstalledAccessibilityServiceList(paramAccessibilityManager);
  }
  
  public static boolean isTouchExplorationEnabled(AccessibilityManager paramAccessibilityManager) {
    return IMPL.isTouchExplorationEnabled(paramAccessibilityManager);
  }
  
  public static boolean removeAccessibilityStateChangeListener(AccessibilityManager paramAccessibilityManager, AccessibilityStateChangeListenerCompat paramAccessibilityStateChangeListenerCompat) {
    return IMPL.removeAccessibilityStateChangeListener(paramAccessibilityManager, paramAccessibilityStateChangeListenerCompat);
  }
  
  static {
    if (Build.VERSION.SDK_INT >= 14) {
      IMPL = new AccessibilityManagerIcsImpl();
      return;
    } 
  }
  
  static class AccessibilityManagerIcsImpl extends AccessibilityManagerStubImpl {
    public boolean addAccessibilityStateChangeListener(AccessibilityManager param1AccessibilityManager, AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat param1AccessibilityStateChangeListenerCompat) {
      return AccessibilityManagerCompatIcs.addAccessibilityStateChangeListener(param1AccessibilityManager, param1AccessibilityStateChangeListenerCompat.mListener);
    }
    
    public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager param1AccessibilityManager, int param1Int) {
      return AccessibilityManagerCompatIcs.getEnabledAccessibilityServiceList(param1AccessibilityManager, param1Int);
    }
    
    public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager param1AccessibilityManager) {
      return AccessibilityManagerCompatIcs.getInstalledAccessibilityServiceList(param1AccessibilityManager);
    }
    
    public boolean isTouchExplorationEnabled(AccessibilityManager param1AccessibilityManager) {
      return AccessibilityManagerCompatIcs.isTouchExplorationEnabled(param1AccessibilityManager);
    }
    
    public Object newAccessiblityStateChangeListener(final AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat listener) {
      return AccessibilityManagerCompatIcs.newAccessibilityStateChangeListener(new AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerBridge() {
            public void onAccessibilityStateChanged(boolean param2Boolean) {
              listener.onAccessibilityStateChanged(param2Boolean);
            }
          });
    }
    
    public boolean removeAccessibilityStateChangeListener(AccessibilityManager param1AccessibilityManager, AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat param1AccessibilityStateChangeListenerCompat) {
      return AccessibilityManagerCompatIcs.removeAccessibilityStateChangeListener(param1AccessibilityManager, param1AccessibilityStateChangeListenerCompat.mListener);
    }
  }
  
  class null implements AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerBridge {
    public void onAccessibilityStateChanged(boolean param1Boolean) {
      listener.onAccessibilityStateChanged(param1Boolean);
    }
  }
  
  static class AccessibilityManagerStubImpl implements AccessibilityManagerVersionImpl {
    public boolean addAccessibilityStateChangeListener(AccessibilityManager param1AccessibilityManager, AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat param1AccessibilityStateChangeListenerCompat) {
      return false;
    }
    
    public List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager param1AccessibilityManager, int param1Int) {
      return Collections.emptyList();
    }
    
    public List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager param1AccessibilityManager) {
      return Collections.emptyList();
    }
    
    public boolean isTouchExplorationEnabled(AccessibilityManager param1AccessibilityManager) {
      return false;
    }
    
    public Object newAccessiblityStateChangeListener(AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat param1AccessibilityStateChangeListenerCompat) {
      return null;
    }
    
    public boolean removeAccessibilityStateChangeListener(AccessibilityManager param1AccessibilityManager, AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat param1AccessibilityStateChangeListenerCompat) {
      return false;
    }
  }
  
  static interface AccessibilityManagerVersionImpl {
    boolean addAccessibilityStateChangeListener(AccessibilityManager param1AccessibilityManager, AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat param1AccessibilityStateChangeListenerCompat);
    
    List<AccessibilityServiceInfo> getEnabledAccessibilityServiceList(AccessibilityManager param1AccessibilityManager, int param1Int);
    
    List<AccessibilityServiceInfo> getInstalledAccessibilityServiceList(AccessibilityManager param1AccessibilityManager);
    
    boolean isTouchExplorationEnabled(AccessibilityManager param1AccessibilityManager);
    
    Object newAccessiblityStateChangeListener(AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat param1AccessibilityStateChangeListenerCompat);
    
    boolean removeAccessibilityStateChangeListener(AccessibilityManager param1AccessibilityManager, AccessibilityManagerCompat.AccessibilityStateChangeListenerCompat param1AccessibilityStateChangeListenerCompat);
  }
  
  public static abstract class AccessibilityStateChangeListenerCompat {
    final Object mListener = AccessibilityManagerCompat.IMPL.newAccessiblityStateChangeListener(this);
    
    public abstract void onAccessibilityStateChanged(boolean param1Boolean);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\view\accessibility\AccessibilityManagerCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */