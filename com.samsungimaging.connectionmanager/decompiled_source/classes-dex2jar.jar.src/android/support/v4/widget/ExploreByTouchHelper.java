package android.support.v4.widget;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.AccessibilityDelegateCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewParentCompat;
import android.support.v4.view.accessibility.AccessibilityEventCompat;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.support.v4.view.accessibility.AccessibilityNodeProviderCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import java.util.LinkedList;
import java.util.List;

public abstract class ExploreByTouchHelper extends AccessibilityDelegateCompat {
  private static final String DEFAULT_CLASS_NAME = View.class.getName();
  
  public static final int HOST_ID = -1;
  
  public static final int INVALID_ID = -2147483648;
  
  private int mFocusedVirtualViewId = Integer.MIN_VALUE;
  
  private int mHoveredVirtualViewId = Integer.MIN_VALUE;
  
  private final AccessibilityManager mManager;
  
  private ExploreByTouchNodeProvider mNodeProvider;
  
  private final int[] mTempGlobalRect = new int[2];
  
  private final Rect mTempParentRect = new Rect();
  
  private final Rect mTempScreenRect = new Rect();
  
  private final Rect mTempVisibleRect = new Rect();
  
  private final View mView;
  
  public ExploreByTouchHelper(View paramView) {
    if (paramView == null)
      throw new IllegalArgumentException("View may not be null"); 
    this.mView = paramView;
    this.mManager = (AccessibilityManager)paramView.getContext().getSystemService("accessibility");
  }
  
  private boolean clearAccessibilityFocus(int paramInt) {
    if (isAccessibilityFocused(paramInt)) {
      this.mFocusedVirtualViewId = Integer.MIN_VALUE;
      this.mView.invalidate();
      sendEventForVirtualView(paramInt, 65536);
      return true;
    } 
    return false;
  }
  
  private AccessibilityEvent createEvent(int paramInt1, int paramInt2) {
    switch (paramInt1) {
      default:
        return createEventForChild(paramInt1, paramInt2);
      case -1:
        break;
    } 
    return createEventForHost(paramInt2);
  }
  
  private AccessibilityEvent createEventForChild(int paramInt1, int paramInt2) {
    AccessibilityEvent accessibilityEvent = AccessibilityEvent.obtain(paramInt2);
    accessibilityEvent.setEnabled(true);
    accessibilityEvent.setClassName(DEFAULT_CLASS_NAME);
    onPopulateEventForVirtualView(paramInt1, accessibilityEvent);
    if (accessibilityEvent.getText().isEmpty() && accessibilityEvent.getContentDescription() == null)
      throw new RuntimeException("Callbacks must add text or a content description in populateEventForVirtualViewId()"); 
    accessibilityEvent.setPackageName(this.mView.getContext().getPackageName());
    AccessibilityEventCompat.asRecord(accessibilityEvent).setSource(this.mView, paramInt1);
    return accessibilityEvent;
  }
  
  private AccessibilityEvent createEventForHost(int paramInt) {
    AccessibilityEvent accessibilityEvent = AccessibilityEvent.obtain(paramInt);
    ViewCompat.onInitializeAccessibilityEvent(this.mView, accessibilityEvent);
    return accessibilityEvent;
  }
  
  private AccessibilityNodeInfoCompat createNode(int paramInt) {
    switch (paramInt) {
      default:
        return createNodeForChild(paramInt);
      case -1:
        break;
    } 
    return createNodeForHost();
  }
  
  private AccessibilityNodeInfoCompat createNodeForChild(int paramInt) {
    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain();
    accessibilityNodeInfoCompat.setEnabled(true);
    accessibilityNodeInfoCompat.setClassName(DEFAULT_CLASS_NAME);
    onPopulateNodeForVirtualView(paramInt, accessibilityNodeInfoCompat);
    if (accessibilityNodeInfoCompat.getText() == null && accessibilityNodeInfoCompat.getContentDescription() == null)
      throw new RuntimeException("Callbacks must add text or a content description in populateNodeForVirtualViewId()"); 
    accessibilityNodeInfoCompat.getBoundsInParent(this.mTempParentRect);
    if (this.mTempParentRect.isEmpty())
      throw new RuntimeException("Callbacks must set parent bounds in populateNodeForVirtualViewId()"); 
    int i = accessibilityNodeInfoCompat.getActions();
    if ((i & 0x40) != 0)
      throw new RuntimeException("Callbacks must not add ACTION_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()"); 
    if ((i & 0x80) != 0)
      throw new RuntimeException("Callbacks must not add ACTION_CLEAR_ACCESSIBILITY_FOCUS in populateNodeForVirtualViewId()"); 
    accessibilityNodeInfoCompat.setPackageName(this.mView.getContext().getPackageName());
    accessibilityNodeInfoCompat.setSource(this.mView, paramInt);
    accessibilityNodeInfoCompat.setParent(this.mView);
    if (this.mFocusedVirtualViewId == paramInt) {
      accessibilityNodeInfoCompat.setAccessibilityFocused(true);
      accessibilityNodeInfoCompat.addAction(128);
    } else {
      accessibilityNodeInfoCompat.setAccessibilityFocused(false);
      accessibilityNodeInfoCompat.addAction(64);
    } 
    if (intersectVisibleToUser(this.mTempParentRect)) {
      accessibilityNodeInfoCompat.setVisibleToUser(true);
      accessibilityNodeInfoCompat.setBoundsInParent(this.mTempParentRect);
    } 
    this.mView.getLocationOnScreen(this.mTempGlobalRect);
    paramInt = this.mTempGlobalRect[0];
    i = this.mTempGlobalRect[1];
    this.mTempScreenRect.set(this.mTempParentRect);
    this.mTempScreenRect.offset(paramInt, i);
    accessibilityNodeInfoCompat.setBoundsInScreen(this.mTempScreenRect);
    return accessibilityNodeInfoCompat;
  }
  
  private AccessibilityNodeInfoCompat createNodeForHost() {
    AccessibilityNodeInfoCompat accessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.obtain(this.mView);
    ViewCompat.onInitializeAccessibilityNodeInfo(this.mView, accessibilityNodeInfoCompat);
    onPopulateNodeForHost(accessibilityNodeInfoCompat);
    LinkedList<Integer> linkedList = new LinkedList();
    getVisibleVirtualViews(linkedList);
    for (Integer integer : linkedList)
      accessibilityNodeInfoCompat.addChild(this.mView, integer.intValue()); 
    return accessibilityNodeInfoCompat;
  }
  
  private boolean intersectVisibleToUser(Rect paramRect) {
    // Byte code:
    //   0: aload_1
    //   1: ifnull -> 11
    //   4: aload_1
    //   5: invokevirtual isEmpty : ()Z
    //   8: ifeq -> 13
    //   11: iconst_0
    //   12: ireturn
    //   13: aload_0
    //   14: getfield mView : Landroid/view/View;
    //   17: invokevirtual getWindowVisibility : ()I
    //   20: ifne -> 11
    //   23: aload_0
    //   24: getfield mView : Landroid/view/View;
    //   27: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   30: astore_2
    //   31: aload_2
    //   32: instanceof android/view/View
    //   35: ifeq -> 67
    //   38: aload_2
    //   39: checkcast android/view/View
    //   42: astore_2
    //   43: aload_2
    //   44: invokestatic getAlpha : (Landroid/view/View;)F
    //   47: fconst_0
    //   48: fcmpg
    //   49: ifle -> 11
    //   52: aload_2
    //   53: invokevirtual getVisibility : ()I
    //   56: ifne -> 11
    //   59: aload_2
    //   60: invokevirtual getParent : ()Landroid/view/ViewParent;
    //   63: astore_2
    //   64: goto -> 31
    //   67: aload_2
    //   68: ifnull -> 11
    //   71: aload_0
    //   72: getfield mView : Landroid/view/View;
    //   75: aload_0
    //   76: getfield mTempVisibleRect : Landroid/graphics/Rect;
    //   79: invokevirtual getLocalVisibleRect : (Landroid/graphics/Rect;)Z
    //   82: ifeq -> 11
    //   85: aload_1
    //   86: aload_0
    //   87: getfield mTempVisibleRect : Landroid/graphics/Rect;
    //   90: invokevirtual intersect : (Landroid/graphics/Rect;)Z
    //   93: ireturn
  }
  
  private boolean isAccessibilityFocused(int paramInt) {
    return (this.mFocusedVirtualViewId == paramInt);
  }
  
  private boolean manageFocusForChild(int paramInt1, int paramInt2, Bundle paramBundle) {
    switch (paramInt2) {
      default:
        return false;
      case 64:
        return requestAccessibilityFocus(paramInt1);
      case 128:
        break;
    } 
    return clearAccessibilityFocus(paramInt1);
  }
  
  private boolean performAction(int paramInt1, int paramInt2, Bundle paramBundle) {
    switch (paramInt1) {
      default:
        return performActionForChild(paramInt1, paramInt2, paramBundle);
      case -1:
        break;
    } 
    return performActionForHost(paramInt2, paramBundle);
  }
  
  private boolean performActionForChild(int paramInt1, int paramInt2, Bundle paramBundle) {
    switch (paramInt2) {
      default:
        return onPerformActionForVirtualView(paramInt1, paramInt2, paramBundle);
      case 64:
      case 128:
        break;
    } 
    return manageFocusForChild(paramInt1, paramInt2, paramBundle);
  }
  
  private boolean performActionForHost(int paramInt, Bundle paramBundle) {
    return ViewCompat.performAccessibilityAction(this.mView, paramInt, paramBundle);
  }
  
  private boolean requestAccessibilityFocus(int paramInt) {
    if (this.mManager.isEnabled() && AccessibilityManagerCompat.isTouchExplorationEnabled(this.mManager) && !isAccessibilityFocused(paramInt)) {
      if (this.mFocusedVirtualViewId != Integer.MIN_VALUE)
        sendEventForVirtualView(this.mFocusedVirtualViewId, 65536); 
      this.mFocusedVirtualViewId = paramInt;
      this.mView.invalidate();
      sendEventForVirtualView(paramInt, 32768);
      return true;
    } 
    return false;
  }
  
  private void updateHoveredVirtualView(int paramInt) {
    if (this.mHoveredVirtualViewId == paramInt)
      return; 
    int i = this.mHoveredVirtualViewId;
    this.mHoveredVirtualViewId = paramInt;
    sendEventForVirtualView(paramInt, 128);
    sendEventForVirtualView(i, 256);
  }
  
  public boolean dispatchHoverEvent(MotionEvent paramMotionEvent) {
    boolean bool = true;
    if (this.mManager.isEnabled() && AccessibilityManagerCompat.isTouchExplorationEnabled(this.mManager)) {
      int i;
      switch (paramMotionEvent.getAction()) {
        default:
          return false;
        case 7:
        case 9:
          i = getVirtualViewAt(paramMotionEvent.getX(), paramMotionEvent.getY());
          updateHoveredVirtualView(i);
          if (i == Integer.MIN_VALUE)
            bool = false; 
          return bool;
        case 10:
          break;
      } 
      if (this.mFocusedVirtualViewId != Integer.MIN_VALUE) {
        updateHoveredVirtualView(-2147483648);
        return true;
      } 
    } 
    return false;
  }
  
  public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View paramView) {
    if (this.mNodeProvider == null)
      this.mNodeProvider = new ExploreByTouchNodeProvider(); 
    return this.mNodeProvider;
  }
  
  public int getFocusedVirtualView() {
    return this.mFocusedVirtualViewId;
  }
  
  protected abstract int getVirtualViewAt(float paramFloat1, float paramFloat2);
  
  protected abstract void getVisibleVirtualViews(List<Integer> paramList);
  
  public void invalidateRoot() {
    invalidateVirtualView(-1);
  }
  
  public void invalidateVirtualView(int paramInt) {
    sendEventForVirtualView(paramInt, 2048);
  }
  
  protected abstract boolean onPerformActionForVirtualView(int paramInt1, int paramInt2, Bundle paramBundle);
  
  protected abstract void onPopulateEventForVirtualView(int paramInt, AccessibilityEvent paramAccessibilityEvent);
  
  public void onPopulateNodeForHost(AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat) {}
  
  protected abstract void onPopulateNodeForVirtualView(int paramInt, AccessibilityNodeInfoCompat paramAccessibilityNodeInfoCompat);
  
  public boolean sendEventForVirtualView(int paramInt1, int paramInt2) {
    if (paramInt1 != Integer.MIN_VALUE && this.mManager.isEnabled()) {
      ViewParent viewParent = this.mView.getParent();
      if (viewParent != null) {
        AccessibilityEvent accessibilityEvent = createEvent(paramInt1, paramInt2);
        return ViewParentCompat.requestSendAccessibilityEvent(viewParent, this.mView, accessibilityEvent);
      } 
    } 
    return false;
  }
  
  private class ExploreByTouchNodeProvider extends AccessibilityNodeProviderCompat {
    private ExploreByTouchNodeProvider() {}
    
    public AccessibilityNodeInfoCompat createAccessibilityNodeInfo(int param1Int) {
      return ExploreByTouchHelper.this.createNode(param1Int);
    }
    
    public boolean performAction(int param1Int1, int param1Int2, Bundle param1Bundle) {
      return ExploreByTouchHelper.this.performAction(param1Int1, param1Int2, param1Bundle);
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\widget\ExploreByTouchHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */