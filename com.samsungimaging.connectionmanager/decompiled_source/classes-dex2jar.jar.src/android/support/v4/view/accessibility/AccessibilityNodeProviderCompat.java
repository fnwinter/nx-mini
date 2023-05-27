package android.support.v4.view.accessibility;

import android.os.Build;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class AccessibilityNodeProviderCompat {
  private static final AccessibilityNodeProviderImpl IMPL = new AccessibilityNodeProviderStubImpl();
  
  private final Object mProvider = IMPL.newAccessibilityNodeProviderBridge(this);
  
  public AccessibilityNodeProviderCompat() {}
  
  public AccessibilityNodeProviderCompat(Object paramObject) {}
  
  public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int paramInt) {
    return null;
  }
  
  public List<AccessibilityNodeInfoCompat> findAccessibilityNodeInfosByText(String paramString, int paramInt) {
    return null;
  }
  
  public AccessibilityNodeInfoCompat findFocus(int paramInt) {
    return null;
  }
  
  public Object getProvider() {
    return this.mProvider;
  }
  
  public boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle) {
    return false;
  }
  
  static {
    if (Build.VERSION.SDK_INT >= 19) {
      IMPL = new AccessibilityNodeProviderKitKatImpl();
      return;
    } 
    if (Build.VERSION.SDK_INT >= 16) {
      IMPL = new AccessibilityNodeProviderJellyBeanImpl();
      return;
    } 
  }
  
  static interface AccessibilityNodeProviderImpl {
    Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat param1AccessibilityNodeProviderCompat);
  }
  
  static class AccessibilityNodeProviderJellyBeanImpl extends AccessibilityNodeProviderStubImpl {
    public Object newAccessibilityNodeProviderBridge(final AccessibilityNodeProviderCompat compat) {
      return AccessibilityNodeProviderCompatJellyBean.newAccessibilityNodeProviderBridge(new AccessibilityNodeProviderCompatJellyBean.AccessibilityNodeInfoBridge() {
            public Object createAccessibilityNodeInfo(int param2Int) {
              AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = compat.createAccessibilityNodeInfo(param2Int);
              return (accessibilityNodeInfoCompat == null) ? null : accessibilityNodeInfoCompat.getInfo();
            }
            
            public List<Object> findAccessibilityNodeInfosByText(String param2String, int param2Int) {
              List<AccessibilityNodeInfoCompat> list = compat.findAccessibilityNodeInfosByText(param2String, param2Int);
              ArrayList<Object> arrayList = new ArrayList();
              int i = list.size();
              for (param2Int = 0; param2Int < i; param2Int++)
                arrayList.add(((AccessibilityNodeInfoCompat)list.get(param2Int)).getInfo()); 
              return arrayList;
            }
            
            public boolean performAction(int param2Int1, int param2Int2, Bundle param2Bundle) {
              return compat.performAction(param2Int1, param2Int2, param2Bundle);
            }
          });
    }
  }
  
  class null implements AccessibilityNodeProviderCompatJellyBean.AccessibilityNodeInfoBridge {
    public Object createAccessibilityNodeInfo(int param1Int) {
      AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = compat.createAccessibilityNodeInfo(param1Int);
      return (accessibilityNodeInfoCompat == null) ? null : accessibilityNodeInfoCompat.getInfo();
    }
    
    public List<Object> findAccessibilityNodeInfosByText(String param1String, int param1Int) {
      List<AccessibilityNodeInfoCompat> list = compat.findAccessibilityNodeInfosByText(param1String, param1Int);
      ArrayList<Object> arrayList = new ArrayList();
      int i = list.size();
      for (param1Int = 0; param1Int < i; param1Int++)
        arrayList.add(((AccessibilityNodeInfoCompat)list.get(param1Int)).getInfo()); 
      return arrayList;
    }
    
    public boolean performAction(int param1Int1, int param1Int2, Bundle param1Bundle) {
      return compat.performAction(param1Int1, param1Int2, param1Bundle);
    }
  }
  
  static class AccessibilityNodeProviderKitKatImpl extends AccessibilityNodeProviderStubImpl {
    public Object newAccessibilityNodeProviderBridge(final AccessibilityNodeProviderCompat compat) {
      return AccessibilityNodeProviderCompatKitKat.newAccessibilityNodeProviderBridge(new AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge() {
            public Object createAccessibilityNodeInfo(int param2Int) {
              AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = compat.createAccessibilityNodeInfo(param2Int);
              return (accessibilityNodeInfoCompat == null) ? null : accessibilityNodeInfoCompat.getInfo();
            }
            
            public List<Object> findAccessibilityNodeInfosByText(String param2String, int param2Int) {
              List<AccessibilityNodeInfoCompat> list = compat.findAccessibilityNodeInfosByText(param2String, param2Int);
              ArrayList<Object> arrayList = new ArrayList();
              int i = list.size();
              for (param2Int = 0; param2Int < i; param2Int++)
                arrayList.add(((AccessibilityNodeInfoCompat)list.get(param2Int)).getInfo()); 
              return arrayList;
            }
            
            public Object findFocus(int param2Int) {
              AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = compat.findFocus(param2Int);
              return (accessibilityNodeInfoCompat == null) ? null : accessibilityNodeInfoCompat.getInfo();
            }
            
            public boolean performAction(int param2Int1, int param2Int2, Bundle param2Bundle) {
              return compat.performAction(param2Int1, param2Int2, param2Bundle);
            }
          });
    }
  }
  
  class null implements AccessibilityNodeProviderCompatKitKat.AccessibilityNodeInfoBridge {
    public Object createAccessibilityNodeInfo(int param1Int) {
      AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = compat.createAccessibilityNodeInfo(param1Int);
      return (accessibilityNodeInfoCompat == null) ? null : accessibilityNodeInfoCompat.getInfo();
    }
    
    public List<Object> findAccessibilityNodeInfosByText(String param1String, int param1Int) {
      List<AccessibilityNodeInfoCompat> list = compat.findAccessibilityNodeInfosByText(param1String, param1Int);
      ArrayList<Object> arrayList = new ArrayList();
      int i = list.size();
      for (param1Int = 0; param1Int < i; param1Int++)
        arrayList.add(((AccessibilityNodeInfoCompat)list.get(param1Int)).getInfo()); 
      return arrayList;
    }
    
    public Object findFocus(int param1Int) {
      AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = compat.findFocus(param1Int);
      return (accessibilityNodeInfoCompat == null) ? null : accessibilityNodeInfoCompat.getInfo();
    }
    
    public boolean performAction(int param1Int1, int param1Int2, Bundle param1Bundle) {
      return compat.performAction(param1Int1, param1Int2, param1Bundle);
    }
  }
  
  static class AccessibilityNodeProviderStubImpl implements AccessibilityNodeProviderImpl {
    public Object newAccessibilityNodeProviderBridge(AccessibilityNodeProviderCompat param1AccessibilityNodeProviderCompat) {
      return null;
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\view\accessibility\AccessibilityNodeProviderCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */