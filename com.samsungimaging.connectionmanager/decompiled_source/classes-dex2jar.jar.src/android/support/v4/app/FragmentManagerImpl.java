package android.support.v4.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.CallSuper;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.LogWriter;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

final class FragmentManagerImpl extends FragmentManager implements LayoutInflaterFactory {
  static final Interpolator ACCELERATE_CUBIC;
  
  static final Interpolator ACCELERATE_QUINT;
  
  static final int ANIM_DUR = 220;
  
  public static final int ANIM_STYLE_CLOSE_ENTER = 3;
  
  public static final int ANIM_STYLE_CLOSE_EXIT = 4;
  
  public static final int ANIM_STYLE_FADE_ENTER = 5;
  
  public static final int ANIM_STYLE_FADE_EXIT = 6;
  
  public static final int ANIM_STYLE_OPEN_ENTER = 1;
  
  public static final int ANIM_STYLE_OPEN_EXIT = 2;
  
  static boolean DEBUG = false;
  
  static final Interpolator DECELERATE_CUBIC;
  
  static final Interpolator DECELERATE_QUINT;
  
  static final boolean HONEYCOMB;
  
  static final String TAG = "FragmentManager";
  
  static final String TARGET_REQUEST_CODE_STATE_TAG = "android:target_req_state";
  
  static final String TARGET_STATE_TAG = "android:target_state";
  
  static final String USER_VISIBLE_HINT_TAG = "android:user_visible_hint";
  
  static final String VIEW_STATE_TAG = "android:view_state";
  
  static Field sAnimationListenerField = null;
  
  ArrayList<Fragment> mActive;
  
  ArrayList<Fragment> mAdded;
  
  ArrayList<Integer> mAvailBackStackIndices;
  
  ArrayList<Integer> mAvailIndices;
  
  ArrayList<BackStackRecord> mBackStack;
  
  ArrayList<FragmentManager.OnBackStackChangedListener> mBackStackChangeListeners;
  
  ArrayList<BackStackRecord> mBackStackIndices;
  
  FragmentContainer mContainer;
  
  FragmentController mController;
  
  ArrayList<Fragment> mCreatedMenus;
  
  int mCurState = 0;
  
  boolean mDestroyed;
  
  Runnable mExecCommit = new Runnable() {
      public void run() {
        FragmentManagerImpl.this.execPendingActions();
      }
    };
  
  boolean mExecutingActions;
  
  boolean mHavePendingDeferredStart;
  
  FragmentHostCallback mHost;
  
  boolean mNeedMenuInvalidate;
  
  String mNoTransactionsBecause;
  
  Fragment mParent;
  
  ArrayList<Runnable> mPendingActions;
  
  SparseArray<Parcelable> mStateArray = null;
  
  Bundle mStateBundle = null;
  
  boolean mStateSaved;
  
  Runnable[] mTmpActions;
  
  static {
    DECELERATE_QUINT = (Interpolator)new DecelerateInterpolator(2.5F);
    DECELERATE_CUBIC = (Interpolator)new DecelerateInterpolator(1.5F);
    ACCELERATE_QUINT = (Interpolator)new AccelerateInterpolator(2.5F);
    ACCELERATE_CUBIC = (Interpolator)new AccelerateInterpolator(1.5F);
  }
  
  private void checkStateLoss() {
    if (this.mStateSaved)
      throw new IllegalStateException("Can not perform this action after onSaveInstanceState"); 
    if (this.mNoTransactionsBecause != null)
      throw new IllegalStateException("Can not perform this action inside of " + this.mNoTransactionsBecause); 
  }
  
  static Animation makeFadeAnimation(Context paramContext, float paramFloat1, float paramFloat2) {
    AlphaAnimation alphaAnimation = new AlphaAnimation(paramFloat1, paramFloat2);
    alphaAnimation.setInterpolator(DECELERATE_CUBIC);
    alphaAnimation.setDuration(220L);
    return (Animation)alphaAnimation;
  }
  
  static Animation makeOpenCloseAnimation(Context paramContext, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
    AnimationSet animationSet = new AnimationSet(false);
    ScaleAnimation scaleAnimation = new ScaleAnimation(paramFloat1, paramFloat2, paramFloat1, paramFloat2, 1, 0.5F, 1, 0.5F);
    scaleAnimation.setInterpolator(DECELERATE_QUINT);
    scaleAnimation.setDuration(220L);
    animationSet.addAnimation((Animation)scaleAnimation);
    AlphaAnimation alphaAnimation = new AlphaAnimation(paramFloat3, paramFloat4);
    alphaAnimation.setInterpolator(DECELERATE_CUBIC);
    alphaAnimation.setDuration(220L);
    animationSet.addAnimation((Animation)alphaAnimation);
    return (Animation)animationSet;
  }
  
  static boolean modifiesAlpha(Animation paramAnimation) {
    if (!(paramAnimation instanceof AlphaAnimation)) {
      if (paramAnimation instanceof AnimationSet) {
        List list = ((AnimationSet)paramAnimation).getAnimations();
        int i = 0;
        while (i < list.size()) {
          if (!(list.get(i) instanceof AlphaAnimation)) {
            i++;
            continue;
          } 
          return true;
        } 
      } 
      return false;
    } 
    return true;
  }
  
  public static int reverseTransit(int paramInt) {
    switch (paramInt) {
      default:
        return 0;
      case 4097:
        return 8194;
      case 8194:
        return 4097;
      case 4099:
        break;
    } 
    return 4099;
  }
  
  private void setHWLayerAnimListenerIfAlpha(View paramView, Animation paramAnimation) {
    if (paramView != null && paramAnimation != null && shouldRunOnHWLayer(paramView, paramAnimation)) {
      Animation.AnimationListener animationListener = null;
      try {
        if (sAnimationListenerField == null) {
          sAnimationListenerField = Animation.class.getDeclaredField("mListener");
          sAnimationListenerField.setAccessible(true);
        } 
        Animation.AnimationListener animationListener1 = (Animation.AnimationListener)sAnimationListenerField.get(paramAnimation);
        animationListener = animationListener1;
      } catch (NoSuchFieldException noSuchFieldException) {
        Log.e("FragmentManager", "No field with the name mListener is found in Animation class", noSuchFieldException);
      } catch (IllegalAccessException illegalAccessException) {
        Log.e("FragmentManager", "Cannot access Animation's mListener field", illegalAccessException);
      } 
      paramAnimation.setAnimationListener(new AnimateOnHWLayerIfNeededListener(paramView, paramAnimation, animationListener));
      return;
    } 
  }
  
  static boolean shouldRunOnHWLayer(View paramView, Animation paramAnimation) {
    return (Build.VERSION.SDK_INT >= 19 && ViewCompat.getLayerType(paramView) == 0 && ViewCompat.hasOverlappingRendering(paramView) && modifiesAlpha(paramAnimation));
  }
  
  private void throwException(RuntimeException paramRuntimeException) {
    Log.e("FragmentManager", paramRuntimeException.getMessage());
    Log.e("FragmentManager", "Activity state:");
    PrintWriter printWriter = new PrintWriter((Writer)new LogWriter("FragmentManager"));
    if (this.mHost != null) {
      try {
        this.mHost.onDump("  ", null, printWriter, new String[0]);
      } catch (Exception exception) {
        Log.e("FragmentManager", "Failed dumping state", exception);
      } 
      throw paramRuntimeException;
    } 
    try {
      dump("  ", null, (PrintWriter)exception, new String[0]);
    } catch (Exception exception1) {
      Log.e("FragmentManager", "Failed dumping state", exception1);
    } 
    throw paramRuntimeException;
  }
  
  public static int transitToStyleIndex(int paramInt, boolean paramBoolean) {
    switch (paramInt) {
      default:
        return -1;
      case 4097:
        return paramBoolean ? 1 : 2;
      case 8194:
        return paramBoolean ? 3 : 4;
      case 4099:
        break;
    } 
    return paramBoolean ? 5 : 6;
  }
  
  void addBackStackState(BackStackRecord paramBackStackRecord) {
    if (this.mBackStack == null)
      this.mBackStack = new ArrayList<BackStackRecord>(); 
    this.mBackStack.add(paramBackStackRecord);
    reportBackStackChanged();
  }
  
  public void addFragment(Fragment paramFragment, boolean paramBoolean) {
    if (this.mAdded == null)
      this.mAdded = new ArrayList<Fragment>(); 
    if (DEBUG)
      Log.v("FragmentManager", "add: " + paramFragment); 
    makeActive(paramFragment);
    if (!paramFragment.mDetached) {
      if (this.mAdded.contains(paramFragment))
        throw new IllegalStateException("Fragment already added: " + paramFragment); 
      this.mAdded.add(paramFragment);
      paramFragment.mAdded = true;
      paramFragment.mRemoving = false;
      if (paramFragment.mHasMenu && paramFragment.mMenuVisible)
        this.mNeedMenuInvalidate = true; 
      if (paramBoolean)
        moveToState(paramFragment); 
    } 
  }
  
  public void addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener) {
    if (this.mBackStackChangeListeners == null)
      this.mBackStackChangeListeners = new ArrayList<FragmentManager.OnBackStackChangedListener>(); 
    this.mBackStackChangeListeners.add(paramOnBackStackChangedListener);
  }
  
  public int allocBackStackIndex(BackStackRecord paramBackStackRecord) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   6: ifnull -> 19
    //   9: aload_0
    //   10: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   13: invokevirtual size : ()I
    //   16: ifgt -> 100
    //   19: aload_0
    //   20: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   23: ifnonnull -> 37
    //   26: aload_0
    //   27: new java/util/ArrayList
    //   30: dup
    //   31: invokespecial <init> : ()V
    //   34: putfield mBackStackIndices : Ljava/util/ArrayList;
    //   37: aload_0
    //   38: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   41: invokevirtual size : ()I
    //   44: istore_2
    //   45: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   48: ifeq -> 87
    //   51: ldc 'FragmentManager'
    //   53: new java/lang/StringBuilder
    //   56: dup
    //   57: invokespecial <init> : ()V
    //   60: ldc_w 'Setting back stack index '
    //   63: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   66: iload_2
    //   67: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   70: ldc_w ' to '
    //   73: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   76: aload_1
    //   77: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   80: invokevirtual toString : ()Ljava/lang/String;
    //   83: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   86: pop
    //   87: aload_0
    //   88: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   91: aload_1
    //   92: invokevirtual add : (Ljava/lang/Object;)Z
    //   95: pop
    //   96: aload_0
    //   97: monitorexit
    //   98: iload_2
    //   99: ireturn
    //   100: aload_0
    //   101: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   104: aload_0
    //   105: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   108: invokevirtual size : ()I
    //   111: iconst_1
    //   112: isub
    //   113: invokevirtual remove : (I)Ljava/lang/Object;
    //   116: checkcast java/lang/Integer
    //   119: invokevirtual intValue : ()I
    //   122: istore_2
    //   123: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   126: ifeq -> 165
    //   129: ldc 'FragmentManager'
    //   131: new java/lang/StringBuilder
    //   134: dup
    //   135: invokespecial <init> : ()V
    //   138: ldc_w 'Adding back stack index '
    //   141: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   144: iload_2
    //   145: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   148: ldc_w ' with '
    //   151: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   154: aload_1
    //   155: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   158: invokevirtual toString : ()Ljava/lang/String;
    //   161: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   164: pop
    //   165: aload_0
    //   166: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   169: iload_2
    //   170: aload_1
    //   171: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   174: pop
    //   175: aload_0
    //   176: monitorexit
    //   177: iload_2
    //   178: ireturn
    //   179: astore_1
    //   180: aload_0
    //   181: monitorexit
    //   182: aload_1
    //   183: athrow
    // Exception table:
    //   from	to	target	type
    //   2	19	179	finally
    //   19	37	179	finally
    //   37	87	179	finally
    //   87	98	179	finally
    //   100	165	179	finally
    //   165	177	179	finally
    //   180	182	179	finally
  }
  
  public void attachController(FragmentHostCallback paramFragmentHostCallback, FragmentContainer paramFragmentContainer, Fragment paramFragment) {
    if (this.mHost != null)
      throw new IllegalStateException("Already attached"); 
    this.mHost = paramFragmentHostCallback;
    this.mContainer = paramFragmentContainer;
    this.mParent = paramFragment;
  }
  
  public void attachFragment(Fragment paramFragment, int paramInt1, int paramInt2) {
    if (DEBUG)
      Log.v("FragmentManager", "attach: " + paramFragment); 
    if (paramFragment.mDetached) {
      paramFragment.mDetached = false;
      if (!paramFragment.mAdded) {
        if (this.mAdded == null)
          this.mAdded = new ArrayList<Fragment>(); 
        if (this.mAdded.contains(paramFragment))
          throw new IllegalStateException("Fragment already added: " + paramFragment); 
        if (DEBUG)
          Log.v("FragmentManager", "add from attach: " + paramFragment); 
        this.mAdded.add(paramFragment);
        paramFragment.mAdded = true;
        if (paramFragment.mHasMenu && paramFragment.mMenuVisible)
          this.mNeedMenuInvalidate = true; 
        moveToState(paramFragment, this.mCurState, paramInt1, paramInt2, false);
      } 
    } 
  }
  
  public FragmentTransaction beginTransaction() {
    return new BackStackRecord(this);
  }
  
  public void detachFragment(Fragment paramFragment, int paramInt1, int paramInt2) {
    if (DEBUG)
      Log.v("FragmentManager", "detach: " + paramFragment); 
    if (!paramFragment.mDetached) {
      paramFragment.mDetached = true;
      if (paramFragment.mAdded) {
        if (this.mAdded != null) {
          if (DEBUG)
            Log.v("FragmentManager", "remove from detach: " + paramFragment); 
          this.mAdded.remove(paramFragment);
        } 
        if (paramFragment.mHasMenu && paramFragment.mMenuVisible)
          this.mNeedMenuInvalidate = true; 
        paramFragment.mAdded = false;
        moveToState(paramFragment, 1, paramInt1, paramInt2, false);
      } 
    } 
  }
  
  public void dispatchActivityCreated() {
    this.mStateSaved = false;
    moveToState(2, false);
  }
  
  public void dispatchConfigurationChanged(Configuration paramConfiguration) {
    if (this.mAdded != null)
      for (int i = 0; i < this.mAdded.size(); i++) {
        Fragment fragment = this.mAdded.get(i);
        if (fragment != null)
          fragment.performConfigurationChanged(paramConfiguration); 
      }  
  }
  
  public boolean dispatchContextItemSelected(MenuItem paramMenuItem) {
    if (this.mAdded != null)
      for (int i = 0; i < this.mAdded.size(); i++) {
        Fragment fragment = this.mAdded.get(i);
        if (fragment != null && fragment.performContextItemSelected(paramMenuItem))
          return true; 
      }  
    return false;
  }
  
  public void dispatchCreate() {
    this.mStateSaved = false;
    moveToState(1, false);
  }
  
  public boolean dispatchCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater) {
    boolean bool2 = false;
    boolean bool1 = false;
    ArrayList<Fragment> arrayList2 = null;
    ArrayList<Fragment> arrayList1 = null;
    if (this.mAdded != null) {
      int i = 0;
      while (true) {
        arrayList2 = arrayList1;
        bool2 = bool1;
        if (i < this.mAdded.size()) {
          Fragment fragment = this.mAdded.get(i);
          arrayList2 = arrayList1;
          bool2 = bool1;
          if (fragment != null) {
            arrayList2 = arrayList1;
            bool2 = bool1;
            if (fragment.performCreateOptionsMenu(paramMenu, paramMenuInflater)) {
              bool2 = true;
              arrayList2 = arrayList1;
              if (arrayList1 == null)
                arrayList2 = new ArrayList(); 
              arrayList2.add(fragment);
            } 
          } 
          i++;
          arrayList1 = arrayList2;
          bool1 = bool2;
          continue;
        } 
        break;
      } 
    } 
    if (this.mCreatedMenus != null)
      for (int i = 0; i < this.mCreatedMenus.size(); i++) {
        Fragment fragment = this.mCreatedMenus.get(i);
        if (arrayList2 == null || !arrayList2.contains(fragment))
          fragment.onDestroyOptionsMenu(); 
      }  
    this.mCreatedMenus = arrayList2;
    return bool2;
  }
  
  public void dispatchDestroy() {
    this.mDestroyed = true;
    execPendingActions();
    moveToState(0, false);
    this.mHost = null;
    this.mContainer = null;
    this.mParent = null;
  }
  
  public void dispatchDestroyView() {
    moveToState(1, false);
  }
  
  public void dispatchLowMemory() {
    if (this.mAdded != null)
      for (int i = 0; i < this.mAdded.size(); i++) {
        Fragment fragment = this.mAdded.get(i);
        if (fragment != null)
          fragment.performLowMemory(); 
      }  
  }
  
  public boolean dispatchOptionsItemSelected(MenuItem paramMenuItem) {
    if (this.mAdded != null)
      for (int i = 0; i < this.mAdded.size(); i++) {
        Fragment fragment = this.mAdded.get(i);
        if (fragment != null && fragment.performOptionsItemSelected(paramMenuItem))
          return true; 
      }  
    return false;
  }
  
  public void dispatchOptionsMenuClosed(Menu paramMenu) {
    if (this.mAdded != null)
      for (int i = 0; i < this.mAdded.size(); i++) {
        Fragment fragment = this.mAdded.get(i);
        if (fragment != null)
          fragment.performOptionsMenuClosed(paramMenu); 
      }  
  }
  
  public void dispatchPause() {
    moveToState(4, false);
  }
  
  public boolean dispatchPrepareOptionsMenu(Menu paramMenu) {
    boolean bool2 = false;
    boolean bool1 = false;
    if (this.mAdded != null) {
      int i = 0;
      while (true) {
        bool2 = bool1;
        if (i < this.mAdded.size()) {
          Fragment fragment = this.mAdded.get(i);
          bool2 = bool1;
          if (fragment != null) {
            bool2 = bool1;
            if (fragment.performPrepareOptionsMenu(paramMenu))
              bool2 = true; 
          } 
          i++;
          bool1 = bool2;
          continue;
        } 
        break;
      } 
    } 
    return bool2;
  }
  
  public void dispatchReallyStop() {
    moveToState(2, false);
  }
  
  public void dispatchResume() {
    this.mStateSaved = false;
    moveToState(5, false);
  }
  
  public void dispatchStart() {
    this.mStateSaved = false;
    moveToState(4, false);
  }
  
  public void dispatchStop() {
    this.mStateSaved = true;
    moveToState(3, false);
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
    // Byte code:
    //   0: new java/lang/StringBuilder
    //   3: dup
    //   4: invokespecial <init> : ()V
    //   7: aload_1
    //   8: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   11: ldc_w '    '
    //   14: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   17: invokevirtual toString : ()Ljava/lang/String;
    //   20: astore #7
    //   22: aload_0
    //   23: getfield mActive : Ljava/util/ArrayList;
    //   26: ifnull -> 153
    //   29: aload_0
    //   30: getfield mActive : Ljava/util/ArrayList;
    //   33: invokevirtual size : ()I
    //   36: istore #6
    //   38: iload #6
    //   40: ifle -> 153
    //   43: aload_3
    //   44: aload_1
    //   45: invokevirtual print : (Ljava/lang/String;)V
    //   48: aload_3
    //   49: ldc_w 'Active Fragments in '
    //   52: invokevirtual print : (Ljava/lang/String;)V
    //   55: aload_3
    //   56: aload_0
    //   57: invokestatic identityHashCode : (Ljava/lang/Object;)I
    //   60: invokestatic toHexString : (I)Ljava/lang/String;
    //   63: invokevirtual print : (Ljava/lang/String;)V
    //   66: aload_3
    //   67: ldc_w ':'
    //   70: invokevirtual println : (Ljava/lang/String;)V
    //   73: iconst_0
    //   74: istore #5
    //   76: iload #5
    //   78: iload #6
    //   80: if_icmpge -> 153
    //   83: aload_0
    //   84: getfield mActive : Ljava/util/ArrayList;
    //   87: iload #5
    //   89: invokevirtual get : (I)Ljava/lang/Object;
    //   92: checkcast android/support/v4/app/Fragment
    //   95: astore #8
    //   97: aload_3
    //   98: aload_1
    //   99: invokevirtual print : (Ljava/lang/String;)V
    //   102: aload_3
    //   103: ldc_w '  #'
    //   106: invokevirtual print : (Ljava/lang/String;)V
    //   109: aload_3
    //   110: iload #5
    //   112: invokevirtual print : (I)V
    //   115: aload_3
    //   116: ldc_w ': '
    //   119: invokevirtual print : (Ljava/lang/String;)V
    //   122: aload_3
    //   123: aload #8
    //   125: invokevirtual println : (Ljava/lang/Object;)V
    //   128: aload #8
    //   130: ifnull -> 144
    //   133: aload #8
    //   135: aload #7
    //   137: aload_2
    //   138: aload_3
    //   139: aload #4
    //   141: invokevirtual dump : (Ljava/lang/String;Ljava/io/FileDescriptor;Ljava/io/PrintWriter;[Ljava/lang/String;)V
    //   144: iload #5
    //   146: iconst_1
    //   147: iadd
    //   148: istore #5
    //   150: goto -> 76
    //   153: aload_0
    //   154: getfield mAdded : Ljava/util/ArrayList;
    //   157: ifnull -> 253
    //   160: aload_0
    //   161: getfield mAdded : Ljava/util/ArrayList;
    //   164: invokevirtual size : ()I
    //   167: istore #6
    //   169: iload #6
    //   171: ifle -> 253
    //   174: aload_3
    //   175: aload_1
    //   176: invokevirtual print : (Ljava/lang/String;)V
    //   179: aload_3
    //   180: ldc_w 'Added Fragments:'
    //   183: invokevirtual println : (Ljava/lang/String;)V
    //   186: iconst_0
    //   187: istore #5
    //   189: iload #5
    //   191: iload #6
    //   193: if_icmpge -> 253
    //   196: aload_0
    //   197: getfield mAdded : Ljava/util/ArrayList;
    //   200: iload #5
    //   202: invokevirtual get : (I)Ljava/lang/Object;
    //   205: checkcast android/support/v4/app/Fragment
    //   208: astore #8
    //   210: aload_3
    //   211: aload_1
    //   212: invokevirtual print : (Ljava/lang/String;)V
    //   215: aload_3
    //   216: ldc_w '  #'
    //   219: invokevirtual print : (Ljava/lang/String;)V
    //   222: aload_3
    //   223: iload #5
    //   225: invokevirtual print : (I)V
    //   228: aload_3
    //   229: ldc_w ': '
    //   232: invokevirtual print : (Ljava/lang/String;)V
    //   235: aload_3
    //   236: aload #8
    //   238: invokevirtual toString : ()Ljava/lang/String;
    //   241: invokevirtual println : (Ljava/lang/String;)V
    //   244: iload #5
    //   246: iconst_1
    //   247: iadd
    //   248: istore #5
    //   250: goto -> 189
    //   253: aload_0
    //   254: getfield mCreatedMenus : Ljava/util/ArrayList;
    //   257: ifnull -> 353
    //   260: aload_0
    //   261: getfield mCreatedMenus : Ljava/util/ArrayList;
    //   264: invokevirtual size : ()I
    //   267: istore #6
    //   269: iload #6
    //   271: ifle -> 353
    //   274: aload_3
    //   275: aload_1
    //   276: invokevirtual print : (Ljava/lang/String;)V
    //   279: aload_3
    //   280: ldc_w 'Fragments Created Menus:'
    //   283: invokevirtual println : (Ljava/lang/String;)V
    //   286: iconst_0
    //   287: istore #5
    //   289: iload #5
    //   291: iload #6
    //   293: if_icmpge -> 353
    //   296: aload_0
    //   297: getfield mCreatedMenus : Ljava/util/ArrayList;
    //   300: iload #5
    //   302: invokevirtual get : (I)Ljava/lang/Object;
    //   305: checkcast android/support/v4/app/Fragment
    //   308: astore #8
    //   310: aload_3
    //   311: aload_1
    //   312: invokevirtual print : (Ljava/lang/String;)V
    //   315: aload_3
    //   316: ldc_w '  #'
    //   319: invokevirtual print : (Ljava/lang/String;)V
    //   322: aload_3
    //   323: iload #5
    //   325: invokevirtual print : (I)V
    //   328: aload_3
    //   329: ldc_w ': '
    //   332: invokevirtual print : (Ljava/lang/String;)V
    //   335: aload_3
    //   336: aload #8
    //   338: invokevirtual toString : ()Ljava/lang/String;
    //   341: invokevirtual println : (Ljava/lang/String;)V
    //   344: iload #5
    //   346: iconst_1
    //   347: iadd
    //   348: istore #5
    //   350: goto -> 289
    //   353: aload_0
    //   354: getfield mBackStack : Ljava/util/ArrayList;
    //   357: ifnull -> 464
    //   360: aload_0
    //   361: getfield mBackStack : Ljava/util/ArrayList;
    //   364: invokevirtual size : ()I
    //   367: istore #6
    //   369: iload #6
    //   371: ifle -> 464
    //   374: aload_3
    //   375: aload_1
    //   376: invokevirtual print : (Ljava/lang/String;)V
    //   379: aload_3
    //   380: ldc_w 'Back Stack:'
    //   383: invokevirtual println : (Ljava/lang/String;)V
    //   386: iconst_0
    //   387: istore #5
    //   389: iload #5
    //   391: iload #6
    //   393: if_icmpge -> 464
    //   396: aload_0
    //   397: getfield mBackStack : Ljava/util/ArrayList;
    //   400: iload #5
    //   402: invokevirtual get : (I)Ljava/lang/Object;
    //   405: checkcast android/support/v4/app/BackStackRecord
    //   408: astore #8
    //   410: aload_3
    //   411: aload_1
    //   412: invokevirtual print : (Ljava/lang/String;)V
    //   415: aload_3
    //   416: ldc_w '  #'
    //   419: invokevirtual print : (Ljava/lang/String;)V
    //   422: aload_3
    //   423: iload #5
    //   425: invokevirtual print : (I)V
    //   428: aload_3
    //   429: ldc_w ': '
    //   432: invokevirtual print : (Ljava/lang/String;)V
    //   435: aload_3
    //   436: aload #8
    //   438: invokevirtual toString : ()Ljava/lang/String;
    //   441: invokevirtual println : (Ljava/lang/String;)V
    //   444: aload #8
    //   446: aload #7
    //   448: aload_2
    //   449: aload_3
    //   450: aload #4
    //   452: invokevirtual dump : (Ljava/lang/String;Ljava/io/FileDescriptor;Ljava/io/PrintWriter;[Ljava/lang/String;)V
    //   455: iload #5
    //   457: iconst_1
    //   458: iadd
    //   459: istore #5
    //   461: goto -> 389
    //   464: aload_0
    //   465: monitorenter
    //   466: aload_0
    //   467: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   470: ifnull -> 561
    //   473: aload_0
    //   474: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   477: invokevirtual size : ()I
    //   480: istore #6
    //   482: iload #6
    //   484: ifle -> 561
    //   487: aload_3
    //   488: aload_1
    //   489: invokevirtual print : (Ljava/lang/String;)V
    //   492: aload_3
    //   493: ldc_w 'Back Stack Indices:'
    //   496: invokevirtual println : (Ljava/lang/String;)V
    //   499: iconst_0
    //   500: istore #5
    //   502: iload #5
    //   504: iload #6
    //   506: if_icmpge -> 561
    //   509: aload_0
    //   510: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   513: iload #5
    //   515: invokevirtual get : (I)Ljava/lang/Object;
    //   518: checkcast android/support/v4/app/BackStackRecord
    //   521: astore_2
    //   522: aload_3
    //   523: aload_1
    //   524: invokevirtual print : (Ljava/lang/String;)V
    //   527: aload_3
    //   528: ldc_w '  #'
    //   531: invokevirtual print : (Ljava/lang/String;)V
    //   534: aload_3
    //   535: iload #5
    //   537: invokevirtual print : (I)V
    //   540: aload_3
    //   541: ldc_w ': '
    //   544: invokevirtual print : (Ljava/lang/String;)V
    //   547: aload_3
    //   548: aload_2
    //   549: invokevirtual println : (Ljava/lang/Object;)V
    //   552: iload #5
    //   554: iconst_1
    //   555: iadd
    //   556: istore #5
    //   558: goto -> 502
    //   561: aload_0
    //   562: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   565: ifnull -> 604
    //   568: aload_0
    //   569: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   572: invokevirtual size : ()I
    //   575: ifle -> 604
    //   578: aload_3
    //   579: aload_1
    //   580: invokevirtual print : (Ljava/lang/String;)V
    //   583: aload_3
    //   584: ldc_w 'mAvailBackStackIndices: '
    //   587: invokevirtual print : (Ljava/lang/String;)V
    //   590: aload_3
    //   591: aload_0
    //   592: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   595: invokevirtual toArray : ()[Ljava/lang/Object;
    //   598: invokestatic toString : ([Ljava/lang/Object;)Ljava/lang/String;
    //   601: invokevirtual println : (Ljava/lang/String;)V
    //   604: aload_0
    //   605: monitorexit
    //   606: aload_0
    //   607: getfield mPendingActions : Ljava/util/ArrayList;
    //   610: ifnull -> 706
    //   613: aload_0
    //   614: getfield mPendingActions : Ljava/util/ArrayList;
    //   617: invokevirtual size : ()I
    //   620: istore #6
    //   622: iload #6
    //   624: ifle -> 706
    //   627: aload_3
    //   628: aload_1
    //   629: invokevirtual print : (Ljava/lang/String;)V
    //   632: aload_3
    //   633: ldc_w 'Pending Actions:'
    //   636: invokevirtual println : (Ljava/lang/String;)V
    //   639: iconst_0
    //   640: istore #5
    //   642: iload #5
    //   644: iload #6
    //   646: if_icmpge -> 706
    //   649: aload_0
    //   650: getfield mPendingActions : Ljava/util/ArrayList;
    //   653: iload #5
    //   655: invokevirtual get : (I)Ljava/lang/Object;
    //   658: checkcast java/lang/Runnable
    //   661: astore_2
    //   662: aload_3
    //   663: aload_1
    //   664: invokevirtual print : (Ljava/lang/String;)V
    //   667: aload_3
    //   668: ldc_w '  #'
    //   671: invokevirtual print : (Ljava/lang/String;)V
    //   674: aload_3
    //   675: iload #5
    //   677: invokevirtual print : (I)V
    //   680: aload_3
    //   681: ldc_w ': '
    //   684: invokevirtual print : (Ljava/lang/String;)V
    //   687: aload_3
    //   688: aload_2
    //   689: invokevirtual println : (Ljava/lang/Object;)V
    //   692: iload #5
    //   694: iconst_1
    //   695: iadd
    //   696: istore #5
    //   698: goto -> 642
    //   701: astore_1
    //   702: aload_0
    //   703: monitorexit
    //   704: aload_1
    //   705: athrow
    //   706: aload_3
    //   707: aload_1
    //   708: invokevirtual print : (Ljava/lang/String;)V
    //   711: aload_3
    //   712: ldc_w 'FragmentManager misc state:'
    //   715: invokevirtual println : (Ljava/lang/String;)V
    //   718: aload_3
    //   719: aload_1
    //   720: invokevirtual print : (Ljava/lang/String;)V
    //   723: aload_3
    //   724: ldc_w '  mHost='
    //   727: invokevirtual print : (Ljava/lang/String;)V
    //   730: aload_3
    //   731: aload_0
    //   732: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   735: invokevirtual println : (Ljava/lang/Object;)V
    //   738: aload_3
    //   739: aload_1
    //   740: invokevirtual print : (Ljava/lang/String;)V
    //   743: aload_3
    //   744: ldc_w '  mContainer='
    //   747: invokevirtual print : (Ljava/lang/String;)V
    //   750: aload_3
    //   751: aload_0
    //   752: getfield mContainer : Landroid/support/v4/app/FragmentContainer;
    //   755: invokevirtual println : (Ljava/lang/Object;)V
    //   758: aload_0
    //   759: getfield mParent : Landroid/support/v4/app/Fragment;
    //   762: ifnull -> 785
    //   765: aload_3
    //   766: aload_1
    //   767: invokevirtual print : (Ljava/lang/String;)V
    //   770: aload_3
    //   771: ldc_w '  mParent='
    //   774: invokevirtual print : (Ljava/lang/String;)V
    //   777: aload_3
    //   778: aload_0
    //   779: getfield mParent : Landroid/support/v4/app/Fragment;
    //   782: invokevirtual println : (Ljava/lang/Object;)V
    //   785: aload_3
    //   786: aload_1
    //   787: invokevirtual print : (Ljava/lang/String;)V
    //   790: aload_3
    //   791: ldc_w '  mCurState='
    //   794: invokevirtual print : (Ljava/lang/String;)V
    //   797: aload_3
    //   798: aload_0
    //   799: getfield mCurState : I
    //   802: invokevirtual print : (I)V
    //   805: aload_3
    //   806: ldc_w ' mStateSaved='
    //   809: invokevirtual print : (Ljava/lang/String;)V
    //   812: aload_3
    //   813: aload_0
    //   814: getfield mStateSaved : Z
    //   817: invokevirtual print : (Z)V
    //   820: aload_3
    //   821: ldc_w ' mDestroyed='
    //   824: invokevirtual print : (Ljava/lang/String;)V
    //   827: aload_3
    //   828: aload_0
    //   829: getfield mDestroyed : Z
    //   832: invokevirtual println : (Z)V
    //   835: aload_0
    //   836: getfield mNeedMenuInvalidate : Z
    //   839: ifeq -> 862
    //   842: aload_3
    //   843: aload_1
    //   844: invokevirtual print : (Ljava/lang/String;)V
    //   847: aload_3
    //   848: ldc_w '  mNeedMenuInvalidate='
    //   851: invokevirtual print : (Ljava/lang/String;)V
    //   854: aload_3
    //   855: aload_0
    //   856: getfield mNeedMenuInvalidate : Z
    //   859: invokevirtual println : (Z)V
    //   862: aload_0
    //   863: getfield mNoTransactionsBecause : Ljava/lang/String;
    //   866: ifnull -> 889
    //   869: aload_3
    //   870: aload_1
    //   871: invokevirtual print : (Ljava/lang/String;)V
    //   874: aload_3
    //   875: ldc_w '  mNoTransactionsBecause='
    //   878: invokevirtual print : (Ljava/lang/String;)V
    //   881: aload_3
    //   882: aload_0
    //   883: getfield mNoTransactionsBecause : Ljava/lang/String;
    //   886: invokevirtual println : (Ljava/lang/String;)V
    //   889: aload_0
    //   890: getfield mAvailIndices : Ljava/util/ArrayList;
    //   893: ifnull -> 932
    //   896: aload_0
    //   897: getfield mAvailIndices : Ljava/util/ArrayList;
    //   900: invokevirtual size : ()I
    //   903: ifle -> 932
    //   906: aload_3
    //   907: aload_1
    //   908: invokevirtual print : (Ljava/lang/String;)V
    //   911: aload_3
    //   912: ldc_w '  mAvailIndices: '
    //   915: invokevirtual print : (Ljava/lang/String;)V
    //   918: aload_3
    //   919: aload_0
    //   920: getfield mAvailIndices : Ljava/util/ArrayList;
    //   923: invokevirtual toArray : ()[Ljava/lang/Object;
    //   926: invokestatic toString : ([Ljava/lang/Object;)Ljava/lang/String;
    //   929: invokevirtual println : (Ljava/lang/String;)V
    //   932: return
    // Exception table:
    //   from	to	target	type
    //   466	482	701	finally
    //   487	499	701	finally
    //   509	552	701	finally
    //   561	604	701	finally
    //   604	606	701	finally
    //   702	704	701	finally
  }
  
  public void enqueueAction(Runnable paramRunnable, boolean paramBoolean) {
    // Byte code:
    //   0: iload_2
    //   1: ifne -> 8
    //   4: aload_0
    //   5: invokespecial checkStateLoss : ()V
    //   8: aload_0
    //   9: monitorenter
    //   10: aload_0
    //   11: getfield mDestroyed : Z
    //   14: ifne -> 24
    //   17: aload_0
    //   18: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   21: ifnonnull -> 40
    //   24: new java/lang/IllegalStateException
    //   27: dup
    //   28: ldc_w 'Activity has been destroyed'
    //   31: invokespecial <init> : (Ljava/lang/String;)V
    //   34: athrow
    //   35: astore_1
    //   36: aload_0
    //   37: monitorexit
    //   38: aload_1
    //   39: athrow
    //   40: aload_0
    //   41: getfield mPendingActions : Ljava/util/ArrayList;
    //   44: ifnonnull -> 58
    //   47: aload_0
    //   48: new java/util/ArrayList
    //   51: dup
    //   52: invokespecial <init> : ()V
    //   55: putfield mPendingActions : Ljava/util/ArrayList;
    //   58: aload_0
    //   59: getfield mPendingActions : Ljava/util/ArrayList;
    //   62: aload_1
    //   63: invokevirtual add : (Ljava/lang/Object;)Z
    //   66: pop
    //   67: aload_0
    //   68: getfield mPendingActions : Ljava/util/ArrayList;
    //   71: invokevirtual size : ()I
    //   74: iconst_1
    //   75: if_icmpne -> 107
    //   78: aload_0
    //   79: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   82: invokevirtual getHandler : ()Landroid/os/Handler;
    //   85: aload_0
    //   86: getfield mExecCommit : Ljava/lang/Runnable;
    //   89: invokevirtual removeCallbacks : (Ljava/lang/Runnable;)V
    //   92: aload_0
    //   93: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   96: invokevirtual getHandler : ()Landroid/os/Handler;
    //   99: aload_0
    //   100: getfield mExecCommit : Ljava/lang/Runnable;
    //   103: invokevirtual post : (Ljava/lang/Runnable;)Z
    //   106: pop
    //   107: aload_0
    //   108: monitorexit
    //   109: return
    // Exception table:
    //   from	to	target	type
    //   10	24	35	finally
    //   24	35	35	finally
    //   36	38	35	finally
    //   40	58	35	finally
    //   58	107	35	finally
    //   107	109	35	finally
  }
  
  public boolean execPendingActions() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mExecutingActions : Z
    //   4: ifeq -> 18
    //   7: new java/lang/IllegalStateException
    //   10: dup
    //   11: ldc_w 'Recursive entry to executePendingTransactions'
    //   14: invokespecial <init> : (Ljava/lang/String;)V
    //   17: athrow
    //   18: invokestatic myLooper : ()Landroid/os/Looper;
    //   21: aload_0
    //   22: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   25: invokevirtual getHandler : ()Landroid/os/Handler;
    //   28: invokevirtual getLooper : ()Landroid/os/Looper;
    //   31: if_acmpeq -> 45
    //   34: new java/lang/IllegalStateException
    //   37: dup
    //   38: ldc_w 'Must be called from main thread of process'
    //   41: invokespecial <init> : (Ljava/lang/String;)V
    //   44: athrow
    //   45: iconst_0
    //   46: istore #4
    //   48: aload_0
    //   49: monitorenter
    //   50: aload_0
    //   51: getfield mPendingActions : Ljava/util/ArrayList;
    //   54: ifnull -> 67
    //   57: aload_0
    //   58: getfield mPendingActions : Ljava/util/ArrayList;
    //   61: invokevirtual size : ()I
    //   64: ifne -> 141
    //   67: aload_0
    //   68: monitorexit
    //   69: aload_0
    //   70: getfield mHavePendingDeferredStart : Z
    //   73: ifeq -> 276
    //   76: iconst_0
    //   77: istore_2
    //   78: iconst_0
    //   79: istore_1
    //   80: iload_1
    //   81: aload_0
    //   82: getfield mActive : Ljava/util/ArrayList;
    //   85: invokevirtual size : ()I
    //   88: if_icmpge -> 263
    //   91: aload_0
    //   92: getfield mActive : Ljava/util/ArrayList;
    //   95: iload_1
    //   96: invokevirtual get : (I)Ljava/lang/Object;
    //   99: checkcast android/support/v4/app/Fragment
    //   102: astore #5
    //   104: iload_2
    //   105: istore_3
    //   106: aload #5
    //   108: ifnull -> 132
    //   111: iload_2
    //   112: istore_3
    //   113: aload #5
    //   115: getfield mLoaderManager : Landroid/support/v4/app/LoaderManagerImpl;
    //   118: ifnull -> 132
    //   121: iload_2
    //   122: aload #5
    //   124: getfield mLoaderManager : Landroid/support/v4/app/LoaderManagerImpl;
    //   127: invokevirtual hasRunningLoaders : ()Z
    //   130: ior
    //   131: istore_3
    //   132: iload_1
    //   133: iconst_1
    //   134: iadd
    //   135: istore_1
    //   136: iload_3
    //   137: istore_2
    //   138: goto -> 80
    //   141: aload_0
    //   142: getfield mPendingActions : Ljava/util/ArrayList;
    //   145: invokevirtual size : ()I
    //   148: istore_2
    //   149: aload_0
    //   150: getfield mTmpActions : [Ljava/lang/Runnable;
    //   153: ifnull -> 165
    //   156: aload_0
    //   157: getfield mTmpActions : [Ljava/lang/Runnable;
    //   160: arraylength
    //   161: iload_2
    //   162: if_icmpge -> 173
    //   165: aload_0
    //   166: iload_2
    //   167: anewarray java/lang/Runnable
    //   170: putfield mTmpActions : [Ljava/lang/Runnable;
    //   173: aload_0
    //   174: getfield mPendingActions : Ljava/util/ArrayList;
    //   177: aload_0
    //   178: getfield mTmpActions : [Ljava/lang/Runnable;
    //   181: invokevirtual toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
    //   184: pop
    //   185: aload_0
    //   186: getfield mPendingActions : Ljava/util/ArrayList;
    //   189: invokevirtual clear : ()V
    //   192: aload_0
    //   193: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   196: invokevirtual getHandler : ()Landroid/os/Handler;
    //   199: aload_0
    //   200: getfield mExecCommit : Ljava/lang/Runnable;
    //   203: invokevirtual removeCallbacks : (Ljava/lang/Runnable;)V
    //   206: aload_0
    //   207: monitorexit
    //   208: aload_0
    //   209: iconst_1
    //   210: putfield mExecutingActions : Z
    //   213: iconst_0
    //   214: istore_1
    //   215: iload_1
    //   216: iload_2
    //   217: if_icmpge -> 252
    //   220: aload_0
    //   221: getfield mTmpActions : [Ljava/lang/Runnable;
    //   224: iload_1
    //   225: aaload
    //   226: invokeinterface run : ()V
    //   231: aload_0
    //   232: getfield mTmpActions : [Ljava/lang/Runnable;
    //   235: iload_1
    //   236: aconst_null
    //   237: aastore
    //   238: iload_1
    //   239: iconst_1
    //   240: iadd
    //   241: istore_1
    //   242: goto -> 215
    //   245: astore #5
    //   247: aload_0
    //   248: monitorexit
    //   249: aload #5
    //   251: athrow
    //   252: aload_0
    //   253: iconst_0
    //   254: putfield mExecutingActions : Z
    //   257: iconst_1
    //   258: istore #4
    //   260: goto -> 48
    //   263: iload_2
    //   264: ifne -> 276
    //   267: aload_0
    //   268: iconst_0
    //   269: putfield mHavePendingDeferredStart : Z
    //   272: aload_0
    //   273: invokevirtual startPendingDeferredFragments : ()V
    //   276: iload #4
    //   278: ireturn
    // Exception table:
    //   from	to	target	type
    //   50	67	245	finally
    //   67	69	245	finally
    //   141	165	245	finally
    //   165	173	245	finally
    //   173	208	245	finally
    //   247	249	245	finally
  }
  
  public boolean executePendingTransactions() {
    return execPendingActions();
  }
  
  public Fragment findFragmentById(int paramInt) {
    if (this.mAdded != null) {
      int i = this.mAdded.size() - 1;
      while (i >= 0) {
        Fragment fragment = this.mAdded.get(i);
        if (fragment == null || fragment.mFragmentId != paramInt) {
          i--;
          continue;
        } 
        return fragment;
      } 
    } 
    if (this.mActive != null)
      for (int i = this.mActive.size() - 1; i >= 0; i--) {
        Fragment fragment = this.mActive.get(i);
        if (fragment != null) {
          Fragment fragment1 = fragment;
          if (fragment.mFragmentId != paramInt)
            continue; 
          return fragment1;
        } 
        continue;
      }  
    return null;
  }
  
  public Fragment findFragmentByTag(String paramString) {
    if (this.mAdded != null && paramString != null) {
      int i = this.mAdded.size() - 1;
      while (i >= 0) {
        Fragment fragment = this.mAdded.get(i);
        if (fragment == null || !paramString.equals(fragment.mTag)) {
          i--;
          continue;
        } 
        return fragment;
      } 
    } 
    if (this.mActive != null && paramString != null)
      for (int i = this.mActive.size() - 1; i >= 0; i--) {
        Fragment fragment = this.mActive.get(i);
        if (fragment != null) {
          Fragment fragment1 = fragment;
          if (!paramString.equals(fragment.mTag))
            continue; 
          return fragment1;
        } 
        continue;
      }  
    return null;
  }
  
  public Fragment findFragmentByWho(String paramString) {
    if (this.mActive != null && paramString != null)
      for (int i = this.mActive.size() - 1; i >= 0; i--) {
        Fragment fragment = this.mActive.get(i);
        if (fragment != null) {
          fragment = fragment.findFragmentByWho(paramString);
          if (fragment != null)
            return fragment; 
        } 
      }  
    return null;
  }
  
  public void freeBackStackIndex(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   6: iload_1
    //   7: aconst_null
    //   8: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   11: pop
    //   12: aload_0
    //   13: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   16: ifnonnull -> 30
    //   19: aload_0
    //   20: new java/util/ArrayList
    //   23: dup
    //   24: invokespecial <init> : ()V
    //   27: putfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   30: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   33: ifeq -> 62
    //   36: ldc 'FragmentManager'
    //   38: new java/lang/StringBuilder
    //   41: dup
    //   42: invokespecial <init> : ()V
    //   45: ldc_w 'Freeing back stack index '
    //   48: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: iload_1
    //   52: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   55: invokevirtual toString : ()Ljava/lang/String;
    //   58: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   61: pop
    //   62: aload_0
    //   63: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   66: iload_1
    //   67: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   70: invokevirtual add : (Ljava/lang/Object;)Z
    //   73: pop
    //   74: aload_0
    //   75: monitorexit
    //   76: return
    //   77: astore_2
    //   78: aload_0
    //   79: monitorexit
    //   80: aload_2
    //   81: athrow
    // Exception table:
    //   from	to	target	type
    //   2	30	77	finally
    //   30	62	77	finally
    //   62	76	77	finally
    //   78	80	77	finally
  }
  
  public FragmentManager.BackStackEntry getBackStackEntryAt(int paramInt) {
    return this.mBackStack.get(paramInt);
  }
  
  public int getBackStackEntryCount() {
    return (this.mBackStack != null) ? this.mBackStack.size() : 0;
  }
  
  public Fragment getFragment(Bundle paramBundle, String paramString) {
    int i = paramBundle.getInt(paramString, -1);
    if (i == -1)
      return null; 
    if (i >= this.mActive.size())
      throwException(new IllegalStateException("Fragment no longer exists for key " + paramString + ": index " + i)); 
    Fragment fragment2 = this.mActive.get(i);
    Fragment fragment1 = fragment2;
    if (fragment2 == null) {
      throwException(new IllegalStateException("Fragment no longer exists for key " + paramString + ": index " + i));
      return fragment2;
    } 
    return fragment1;
  }
  
  public List<Fragment> getFragments() {
    return this.mActive;
  }
  
  LayoutInflaterFactory getLayoutInflaterFactory() {
    return this;
  }
  
  public void hideFragment(Fragment paramFragment, int paramInt1, int paramInt2) {
    if (DEBUG)
      Log.v("FragmentManager", "hide: " + paramFragment); 
    if (!paramFragment.mHidden) {
      paramFragment.mHidden = true;
      if (paramFragment.mView != null) {
        Animation animation = loadAnimation(paramFragment, paramInt1, false, paramInt2);
        if (animation != null) {
          setHWLayerAnimListenerIfAlpha(paramFragment.mView, animation);
          paramFragment.mView.startAnimation(animation);
        } 
        paramFragment.mView.setVisibility(8);
      } 
      if (paramFragment.mAdded && paramFragment.mHasMenu && paramFragment.mMenuVisible)
        this.mNeedMenuInvalidate = true; 
      paramFragment.onHiddenChanged(true);
    } 
  }
  
  public boolean isDestroyed() {
    return this.mDestroyed;
  }
  
  Animation loadAnimation(Fragment paramFragment, int paramInt1, boolean paramBoolean, int paramInt2) {
    Animation animation = paramFragment.onCreateAnimation(paramInt1, paramBoolean, paramFragment.mNextAnim);
    if (animation != null)
      return animation; 
    if (paramFragment.mNextAnim != 0) {
      Animation animation1 = AnimationUtils.loadAnimation(this.mHost.getContext(), paramFragment.mNextAnim);
      if (animation1 != null)
        return animation1; 
    } 
    if (paramInt1 == 0)
      return null; 
    paramInt1 = transitToStyleIndex(paramInt1, paramBoolean);
    if (paramInt1 < 0)
      return null; 
    switch (paramInt1) {
      default:
        paramInt1 = paramInt2;
        if (paramInt2 == 0) {
          paramInt1 = paramInt2;
          if (this.mHost.onHasWindowAnimations())
            paramInt1 = this.mHost.onGetWindowAnimations(); 
        } 
        return (Animation)((paramInt1 == 0) ? null : null);
      case 1:
        return makeOpenCloseAnimation(this.mHost.getContext(), 1.125F, 1.0F, 0.0F, 1.0F);
      case 2:
        return makeOpenCloseAnimation(this.mHost.getContext(), 1.0F, 0.975F, 1.0F, 0.0F);
      case 3:
        return makeOpenCloseAnimation(this.mHost.getContext(), 0.975F, 1.0F, 0.0F, 1.0F);
      case 4:
        return makeOpenCloseAnimation(this.mHost.getContext(), 1.0F, 1.075F, 1.0F, 0.0F);
      case 5:
        return makeFadeAnimation(this.mHost.getContext(), 0.0F, 1.0F);
      case 6:
        break;
    } 
    return makeFadeAnimation(this.mHost.getContext(), 1.0F, 0.0F);
  }
  
  void makeActive(Fragment paramFragment) {
    if (paramFragment.mIndex < 0) {
      if (this.mAvailIndices == null || this.mAvailIndices.size() <= 0) {
        if (this.mActive == null)
          this.mActive = new ArrayList<Fragment>(); 
        paramFragment.setIndex(this.mActive.size(), this.mParent);
        this.mActive.add(paramFragment);
      } else {
        paramFragment.setIndex(((Integer)this.mAvailIndices.remove(this.mAvailIndices.size() - 1)).intValue(), this.mParent);
        this.mActive.set(paramFragment.mIndex, paramFragment);
      } 
      if (DEBUG) {
        Log.v("FragmentManager", "Allocated fragment index " + paramFragment);
        return;
      } 
    } 
  }
  
  void makeInactive(Fragment paramFragment) {
    if (paramFragment.mIndex < 0)
      return; 
    if (DEBUG)
      Log.v("FragmentManager", "Freeing fragment index " + paramFragment); 
    this.mActive.set(paramFragment.mIndex, null);
    if (this.mAvailIndices == null)
      this.mAvailIndices = new ArrayList<Integer>(); 
    this.mAvailIndices.add(Integer.valueOf(paramFragment.mIndex));
    this.mHost.inactivateFragment(paramFragment.mWho);
    paramFragment.initState();
  }
  
  void moveToState(int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    if (this.mHost == null && paramInt1 != 0)
      throw new IllegalStateException("No host"); 
    if (paramBoolean || this.mCurState != paramInt1) {
      this.mCurState = paramInt1;
      if (this.mActive != null) {
        boolean bool = false;
        int i = 0;
        while (i < this.mActive.size()) {
          Fragment fragment = this.mActive.get(i);
          boolean bool1 = bool;
          if (fragment != null) {
            moveToState(fragment, paramInt1, paramInt2, paramInt3, false);
            bool1 = bool;
            if (fragment.mLoaderManager != null)
              bool1 = bool | fragment.mLoaderManager.hasRunningLoaders(); 
          } 
          i++;
          bool = bool1;
        } 
        if (!bool)
          startPendingDeferredFragments(); 
        if (this.mNeedMenuInvalidate && this.mHost != null && this.mCurState == 5) {
          this.mHost.onSupportInvalidateOptionsMenu();
          this.mNeedMenuInvalidate = false;
          return;
        } 
      } 
    } 
  }
  
  void moveToState(int paramInt, boolean paramBoolean) {
    moveToState(paramInt, 0, 0, paramBoolean);
  }
  
  void moveToState(Fragment paramFragment) {
    moveToState(paramFragment, this.mCurState, 0, 0, false);
  }
  
  void moveToState(Fragment paramFragment, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
    // Byte code:
    //   0: aload_1
    //   1: getfield mAdded : Z
    //   4: ifeq -> 17
    //   7: iload_2
    //   8: istore #6
    //   10: aload_1
    //   11: getfield mDetached : Z
    //   14: ifeq -> 28
    //   17: iload_2
    //   18: istore #6
    //   20: iload_2
    //   21: iconst_1
    //   22: if_icmple -> 28
    //   25: iconst_1
    //   26: istore #6
    //   28: iload #6
    //   30: istore #7
    //   32: aload_1
    //   33: getfield mRemoving : Z
    //   36: ifeq -> 58
    //   39: iload #6
    //   41: istore #7
    //   43: iload #6
    //   45: aload_1
    //   46: getfield mState : I
    //   49: if_icmple -> 58
    //   52: aload_1
    //   53: getfield mState : I
    //   56: istore #7
    //   58: iload #7
    //   60: istore_2
    //   61: aload_1
    //   62: getfield mDeferStart : Z
    //   65: ifeq -> 90
    //   68: iload #7
    //   70: istore_2
    //   71: aload_1
    //   72: getfield mState : I
    //   75: iconst_4
    //   76: if_icmpge -> 90
    //   79: iload #7
    //   81: istore_2
    //   82: iload #7
    //   84: iconst_3
    //   85: if_icmple -> 90
    //   88: iconst_3
    //   89: istore_2
    //   90: aload_1
    //   91: getfield mState : I
    //   94: iload_2
    //   95: if_icmpge -> 1078
    //   98: aload_1
    //   99: getfield mFromLayout : Z
    //   102: ifeq -> 113
    //   105: aload_1
    //   106: getfield mInLayout : Z
    //   109: ifne -> 113
    //   112: return
    //   113: aload_1
    //   114: getfield mAnimatingAway : Landroid/view/View;
    //   117: ifnull -> 137
    //   120: aload_1
    //   121: aconst_null
    //   122: putfield mAnimatingAway : Landroid/view/View;
    //   125: aload_0
    //   126: aload_1
    //   127: aload_1
    //   128: getfield mStateAfterAnimating : I
    //   131: iconst_0
    //   132: iconst_0
    //   133: iconst_1
    //   134: invokevirtual moveToState : (Landroid/support/v4/app/Fragment;IIIZ)V
    //   137: iload_2
    //   138: istore #6
    //   140: iload_2
    //   141: istore #8
    //   143: iload_2
    //   144: istore #7
    //   146: aload_1
    //   147: getfield mState : I
    //   150: tableswitch default -> 184, 0 -> 194, 1 -> 578, 2 -> 912, 3 -> 912, 4 -> 962
    //   184: iload_2
    //   185: istore #6
    //   187: aload_1
    //   188: iload #6
    //   190: putfield mState : I
    //   193: return
    //   194: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   197: ifeq -> 226
    //   200: ldc 'FragmentManager'
    //   202: new java/lang/StringBuilder
    //   205: dup
    //   206: invokespecial <init> : ()V
    //   209: ldc_w 'moveto CREATED: '
    //   212: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   215: aload_1
    //   216: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   219: invokevirtual toString : ()Ljava/lang/String;
    //   222: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   225: pop
    //   226: iload_2
    //   227: istore #7
    //   229: aload_1
    //   230: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   233: ifnull -> 341
    //   236: aload_1
    //   237: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   240: aload_0
    //   241: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   244: invokevirtual getContext : ()Landroid/content/Context;
    //   247: invokevirtual getClassLoader : ()Ljava/lang/ClassLoader;
    //   250: invokevirtual setClassLoader : (Ljava/lang/ClassLoader;)V
    //   253: aload_1
    //   254: aload_1
    //   255: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   258: ldc 'android:view_state'
    //   260: invokevirtual getSparseParcelableArray : (Ljava/lang/String;)Landroid/util/SparseArray;
    //   263: putfield mSavedViewState : Landroid/util/SparseArray;
    //   266: aload_1
    //   267: aload_0
    //   268: aload_1
    //   269: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   272: ldc 'android:target_state'
    //   274: invokevirtual getFragment : (Landroid/os/Bundle;Ljava/lang/String;)Landroid/support/v4/app/Fragment;
    //   277: putfield mTarget : Landroid/support/v4/app/Fragment;
    //   280: aload_1
    //   281: getfield mTarget : Landroid/support/v4/app/Fragment;
    //   284: ifnull -> 301
    //   287: aload_1
    //   288: aload_1
    //   289: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   292: ldc 'android:target_req_state'
    //   294: iconst_0
    //   295: invokevirtual getInt : (Ljava/lang/String;I)I
    //   298: putfield mTargetRequestCode : I
    //   301: aload_1
    //   302: aload_1
    //   303: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   306: ldc 'android:user_visible_hint'
    //   308: iconst_1
    //   309: invokevirtual getBoolean : (Ljava/lang/String;Z)Z
    //   312: putfield mUserVisibleHint : Z
    //   315: iload_2
    //   316: istore #7
    //   318: aload_1
    //   319: getfield mUserVisibleHint : Z
    //   322: ifne -> 341
    //   325: aload_1
    //   326: iconst_1
    //   327: putfield mDeferStart : Z
    //   330: iload_2
    //   331: istore #7
    //   333: iload_2
    //   334: iconst_3
    //   335: if_icmple -> 341
    //   338: iconst_3
    //   339: istore #7
    //   341: aload_1
    //   342: aload_0
    //   343: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   346: putfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   349: aload_1
    //   350: aload_0
    //   351: getfield mParent : Landroid/support/v4/app/Fragment;
    //   354: putfield mParentFragment : Landroid/support/v4/app/Fragment;
    //   357: aload_0
    //   358: getfield mParent : Landroid/support/v4/app/Fragment;
    //   361: ifnull -> 436
    //   364: aload_0
    //   365: getfield mParent : Landroid/support/v4/app/Fragment;
    //   368: getfield mChildFragmentManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   371: astore #9
    //   373: aload_1
    //   374: aload #9
    //   376: putfield mFragmentManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   379: aload_1
    //   380: iconst_0
    //   381: putfield mCalled : Z
    //   384: aload_1
    //   385: aload_0
    //   386: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   389: invokevirtual getContext : ()Landroid/content/Context;
    //   392: invokevirtual onAttach : (Landroid/content/Context;)V
    //   395: aload_1
    //   396: getfield mCalled : Z
    //   399: ifne -> 448
    //   402: new android/support/v4/app/SuperNotCalledException
    //   405: dup
    //   406: new java/lang/StringBuilder
    //   409: dup
    //   410: invokespecial <init> : ()V
    //   413: ldc_w 'Fragment '
    //   416: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   419: aload_1
    //   420: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   423: ldc_w ' did not call through to super.onAttach()'
    //   426: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   429: invokevirtual toString : ()Ljava/lang/String;
    //   432: invokespecial <init> : (Ljava/lang/String;)V
    //   435: athrow
    //   436: aload_0
    //   437: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   440: invokevirtual getFragmentManagerImpl : ()Landroid/support/v4/app/FragmentManagerImpl;
    //   443: astore #9
    //   445: goto -> 373
    //   448: aload_1
    //   449: getfield mParentFragment : Landroid/support/v4/app/Fragment;
    //   452: ifnonnull -> 463
    //   455: aload_0
    //   456: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   459: aload_1
    //   460: invokevirtual onAttachFragment : (Landroid/support/v4/app/Fragment;)V
    //   463: aload_1
    //   464: getfield mRetaining : Z
    //   467: ifne -> 478
    //   470: aload_1
    //   471: aload_1
    //   472: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   475: invokevirtual performCreate : (Landroid/os/Bundle;)V
    //   478: aload_1
    //   479: iconst_0
    //   480: putfield mRetaining : Z
    //   483: iload #7
    //   485: istore #6
    //   487: aload_1
    //   488: getfield mFromLayout : Z
    //   491: ifeq -> 578
    //   494: aload_1
    //   495: aload_1
    //   496: aload_1
    //   497: aload_1
    //   498: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   501: invokevirtual getLayoutInflater : (Landroid/os/Bundle;)Landroid/view/LayoutInflater;
    //   504: aconst_null
    //   505: aload_1
    //   506: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   509: invokevirtual performCreateView : (Landroid/view/LayoutInflater;Landroid/view/ViewGroup;Landroid/os/Bundle;)Landroid/view/View;
    //   512: putfield mView : Landroid/view/View;
    //   515: aload_1
    //   516: getfield mView : Landroid/view/View;
    //   519: ifnull -> 1044
    //   522: aload_1
    //   523: aload_1
    //   524: getfield mView : Landroid/view/View;
    //   527: putfield mInnerView : Landroid/view/View;
    //   530: getstatic android/os/Build$VERSION.SDK_INT : I
    //   533: bipush #11
    //   535: if_icmplt -> 1030
    //   538: aload_1
    //   539: getfield mView : Landroid/view/View;
    //   542: iconst_0
    //   543: invokestatic setSaveFromParentEnabled : (Landroid/view/View;Z)V
    //   546: aload_1
    //   547: getfield mHidden : Z
    //   550: ifeq -> 562
    //   553: aload_1
    //   554: getfield mView : Landroid/view/View;
    //   557: bipush #8
    //   559: invokevirtual setVisibility : (I)V
    //   562: aload_1
    //   563: aload_1
    //   564: getfield mView : Landroid/view/View;
    //   567: aload_1
    //   568: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   571: invokevirtual onViewCreated : (Landroid/view/View;Landroid/os/Bundle;)V
    //   574: iload #7
    //   576: istore #6
    //   578: iload #6
    //   580: istore #8
    //   582: iload #6
    //   584: iconst_1
    //   585: if_icmple -> 912
    //   588: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   591: ifeq -> 620
    //   594: ldc 'FragmentManager'
    //   596: new java/lang/StringBuilder
    //   599: dup
    //   600: invokespecial <init> : ()V
    //   603: ldc_w 'moveto ACTIVITY_CREATED: '
    //   606: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   609: aload_1
    //   610: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   613: invokevirtual toString : ()Ljava/lang/String;
    //   616: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   619: pop
    //   620: aload_1
    //   621: getfield mFromLayout : Z
    //   624: ifne -> 880
    //   627: aconst_null
    //   628: astore #9
    //   630: aload_1
    //   631: getfield mContainerId : I
    //   634: ifeq -> 744
    //   637: aload_0
    //   638: getfield mContainer : Landroid/support/v4/app/FragmentContainer;
    //   641: aload_1
    //   642: getfield mContainerId : I
    //   645: invokevirtual onFindViewById : (I)Landroid/view/View;
    //   648: checkcast android/view/ViewGroup
    //   651: astore #10
    //   653: aload #10
    //   655: astore #9
    //   657: aload #10
    //   659: ifnonnull -> 744
    //   662: aload #10
    //   664: astore #9
    //   666: aload_1
    //   667: getfield mRestored : Z
    //   670: ifne -> 744
    //   673: aload_0
    //   674: new java/lang/IllegalArgumentException
    //   677: dup
    //   678: new java/lang/StringBuilder
    //   681: dup
    //   682: invokespecial <init> : ()V
    //   685: ldc_w 'No view found for id 0x'
    //   688: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   691: aload_1
    //   692: getfield mContainerId : I
    //   695: invokestatic toHexString : (I)Ljava/lang/String;
    //   698: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   701: ldc_w ' ('
    //   704: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   707: aload_1
    //   708: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   711: aload_1
    //   712: getfield mContainerId : I
    //   715: invokevirtual getResourceName : (I)Ljava/lang/String;
    //   718: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   721: ldc_w ') for fragment '
    //   724: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   727: aload_1
    //   728: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   731: invokevirtual toString : ()Ljava/lang/String;
    //   734: invokespecial <init> : (Ljava/lang/String;)V
    //   737: invokespecial throwException : (Ljava/lang/RuntimeException;)V
    //   740: aload #10
    //   742: astore #9
    //   744: aload_1
    //   745: aload #9
    //   747: putfield mContainer : Landroid/view/ViewGroup;
    //   750: aload_1
    //   751: aload_1
    //   752: aload_1
    //   753: aload_1
    //   754: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   757: invokevirtual getLayoutInflater : (Landroid/os/Bundle;)Landroid/view/LayoutInflater;
    //   760: aload #9
    //   762: aload_1
    //   763: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   766: invokevirtual performCreateView : (Landroid/view/LayoutInflater;Landroid/view/ViewGroup;Landroid/os/Bundle;)Landroid/view/View;
    //   769: putfield mView : Landroid/view/View;
    //   772: aload_1
    //   773: getfield mView : Landroid/view/View;
    //   776: ifnull -> 1070
    //   779: aload_1
    //   780: aload_1
    //   781: getfield mView : Landroid/view/View;
    //   784: putfield mInnerView : Landroid/view/View;
    //   787: getstatic android/os/Build$VERSION.SDK_INT : I
    //   790: bipush #11
    //   792: if_icmplt -> 1056
    //   795: aload_1
    //   796: getfield mView : Landroid/view/View;
    //   799: iconst_0
    //   800: invokestatic setSaveFromParentEnabled : (Landroid/view/View;Z)V
    //   803: aload #9
    //   805: ifnull -> 852
    //   808: aload_0
    //   809: aload_1
    //   810: iload_3
    //   811: iconst_1
    //   812: iload #4
    //   814: invokevirtual loadAnimation : (Landroid/support/v4/app/Fragment;IZI)Landroid/view/animation/Animation;
    //   817: astore #10
    //   819: aload #10
    //   821: ifnull -> 843
    //   824: aload_0
    //   825: aload_1
    //   826: getfield mView : Landroid/view/View;
    //   829: aload #10
    //   831: invokespecial setHWLayerAnimListenerIfAlpha : (Landroid/view/View;Landroid/view/animation/Animation;)V
    //   834: aload_1
    //   835: getfield mView : Landroid/view/View;
    //   838: aload #10
    //   840: invokevirtual startAnimation : (Landroid/view/animation/Animation;)V
    //   843: aload #9
    //   845: aload_1
    //   846: getfield mView : Landroid/view/View;
    //   849: invokevirtual addView : (Landroid/view/View;)V
    //   852: aload_1
    //   853: getfield mHidden : Z
    //   856: ifeq -> 868
    //   859: aload_1
    //   860: getfield mView : Landroid/view/View;
    //   863: bipush #8
    //   865: invokevirtual setVisibility : (I)V
    //   868: aload_1
    //   869: aload_1
    //   870: getfield mView : Landroid/view/View;
    //   873: aload_1
    //   874: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   877: invokevirtual onViewCreated : (Landroid/view/View;Landroid/os/Bundle;)V
    //   880: aload_1
    //   881: aload_1
    //   882: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   885: invokevirtual performActivityCreated : (Landroid/os/Bundle;)V
    //   888: aload_1
    //   889: getfield mView : Landroid/view/View;
    //   892: ifnull -> 903
    //   895: aload_1
    //   896: aload_1
    //   897: getfield mSavedFragmentState : Landroid/os/Bundle;
    //   900: invokevirtual restoreViewState : (Landroid/os/Bundle;)V
    //   903: aload_1
    //   904: aconst_null
    //   905: putfield mSavedFragmentState : Landroid/os/Bundle;
    //   908: iload #6
    //   910: istore #8
    //   912: iload #8
    //   914: istore #7
    //   916: iload #8
    //   918: iconst_3
    //   919: if_icmple -> 962
    //   922: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   925: ifeq -> 954
    //   928: ldc 'FragmentManager'
    //   930: new java/lang/StringBuilder
    //   933: dup
    //   934: invokespecial <init> : ()V
    //   937: ldc_w 'moveto STARTED: '
    //   940: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   943: aload_1
    //   944: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   947: invokevirtual toString : ()Ljava/lang/String;
    //   950: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   953: pop
    //   954: aload_1
    //   955: invokevirtual performStart : ()V
    //   958: iload #8
    //   960: istore #7
    //   962: iload #7
    //   964: istore #6
    //   966: iload #7
    //   968: iconst_4
    //   969: if_icmple -> 187
    //   972: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   975: ifeq -> 1004
    //   978: ldc 'FragmentManager'
    //   980: new java/lang/StringBuilder
    //   983: dup
    //   984: invokespecial <init> : ()V
    //   987: ldc_w 'moveto RESUMED: '
    //   990: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   993: aload_1
    //   994: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   997: invokevirtual toString : ()Ljava/lang/String;
    //   1000: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1003: pop
    //   1004: aload_1
    //   1005: iconst_1
    //   1006: putfield mResumed : Z
    //   1009: aload_1
    //   1010: invokevirtual performResume : ()V
    //   1013: aload_1
    //   1014: aconst_null
    //   1015: putfield mSavedFragmentState : Landroid/os/Bundle;
    //   1018: aload_1
    //   1019: aconst_null
    //   1020: putfield mSavedViewState : Landroid/util/SparseArray;
    //   1023: iload #7
    //   1025: istore #6
    //   1027: goto -> 187
    //   1030: aload_1
    //   1031: aload_1
    //   1032: getfield mView : Landroid/view/View;
    //   1035: invokestatic wrap : (Landroid/view/View;)Landroid/view/ViewGroup;
    //   1038: putfield mView : Landroid/view/View;
    //   1041: goto -> 546
    //   1044: aload_1
    //   1045: aconst_null
    //   1046: putfield mInnerView : Landroid/view/View;
    //   1049: iload #7
    //   1051: istore #6
    //   1053: goto -> 578
    //   1056: aload_1
    //   1057: aload_1
    //   1058: getfield mView : Landroid/view/View;
    //   1061: invokestatic wrap : (Landroid/view/View;)Landroid/view/ViewGroup;
    //   1064: putfield mView : Landroid/view/View;
    //   1067: goto -> 803
    //   1070: aload_1
    //   1071: aconst_null
    //   1072: putfield mInnerView : Landroid/view/View;
    //   1075: goto -> 880
    //   1078: iload_2
    //   1079: istore #6
    //   1081: aload_1
    //   1082: getfield mState : I
    //   1085: iload_2
    //   1086: if_icmple -> 187
    //   1089: aload_1
    //   1090: getfield mState : I
    //   1093: tableswitch default -> 1128, 1 -> 1134, 2 -> 1318, 3 -> 1277, 4 -> 1236, 5 -> 1190
    //   1128: iload_2
    //   1129: istore #6
    //   1131: goto -> 187
    //   1134: iload_2
    //   1135: istore #6
    //   1137: iload_2
    //   1138: iconst_1
    //   1139: if_icmpge -> 187
    //   1142: aload_0
    //   1143: getfield mDestroyed : Z
    //   1146: ifeq -> 1172
    //   1149: aload_1
    //   1150: getfield mAnimatingAway : Landroid/view/View;
    //   1153: ifnull -> 1172
    //   1156: aload_1
    //   1157: getfield mAnimatingAway : Landroid/view/View;
    //   1160: astore #9
    //   1162: aload_1
    //   1163: aconst_null
    //   1164: putfield mAnimatingAway : Landroid/view/View;
    //   1167: aload #9
    //   1169: invokevirtual clearAnimation : ()V
    //   1172: aload_1
    //   1173: getfield mAnimatingAway : Landroid/view/View;
    //   1176: ifnull -> 1515
    //   1179: aload_1
    //   1180: iload_2
    //   1181: putfield mStateAfterAnimating : I
    //   1184: iconst_1
    //   1185: istore #6
    //   1187: goto -> 187
    //   1190: iload_2
    //   1191: iconst_5
    //   1192: if_icmpge -> 1236
    //   1195: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1198: ifeq -> 1227
    //   1201: ldc 'FragmentManager'
    //   1203: new java/lang/StringBuilder
    //   1206: dup
    //   1207: invokespecial <init> : ()V
    //   1210: ldc_w 'movefrom RESUMED: '
    //   1213: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1216: aload_1
    //   1217: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1220: invokevirtual toString : ()Ljava/lang/String;
    //   1223: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1226: pop
    //   1227: aload_1
    //   1228: invokevirtual performPause : ()V
    //   1231: aload_1
    //   1232: iconst_0
    //   1233: putfield mResumed : Z
    //   1236: iload_2
    //   1237: iconst_4
    //   1238: if_icmpge -> 1277
    //   1241: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1244: ifeq -> 1273
    //   1247: ldc 'FragmentManager'
    //   1249: new java/lang/StringBuilder
    //   1252: dup
    //   1253: invokespecial <init> : ()V
    //   1256: ldc_w 'movefrom STARTED: '
    //   1259: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1262: aload_1
    //   1263: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1266: invokevirtual toString : ()Ljava/lang/String;
    //   1269: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1272: pop
    //   1273: aload_1
    //   1274: invokevirtual performStop : ()V
    //   1277: iload_2
    //   1278: iconst_3
    //   1279: if_icmpge -> 1318
    //   1282: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1285: ifeq -> 1314
    //   1288: ldc 'FragmentManager'
    //   1290: new java/lang/StringBuilder
    //   1293: dup
    //   1294: invokespecial <init> : ()V
    //   1297: ldc_w 'movefrom STOPPED: '
    //   1300: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1303: aload_1
    //   1304: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1307: invokevirtual toString : ()Ljava/lang/String;
    //   1310: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1313: pop
    //   1314: aload_1
    //   1315: invokevirtual performReallyStop : ()V
    //   1318: iload_2
    //   1319: iconst_2
    //   1320: if_icmpge -> 1134
    //   1323: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1326: ifeq -> 1355
    //   1329: ldc 'FragmentManager'
    //   1331: new java/lang/StringBuilder
    //   1334: dup
    //   1335: invokespecial <init> : ()V
    //   1338: ldc_w 'movefrom ACTIVITY_CREATED: '
    //   1341: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1344: aload_1
    //   1345: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1348: invokevirtual toString : ()Ljava/lang/String;
    //   1351: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1354: pop
    //   1355: aload_1
    //   1356: getfield mView : Landroid/view/View;
    //   1359: ifnull -> 1385
    //   1362: aload_0
    //   1363: getfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   1366: aload_1
    //   1367: invokevirtual onShouldSaveFragmentState : (Landroid/support/v4/app/Fragment;)Z
    //   1370: ifeq -> 1385
    //   1373: aload_1
    //   1374: getfield mSavedViewState : Landroid/util/SparseArray;
    //   1377: ifnonnull -> 1385
    //   1380: aload_0
    //   1381: aload_1
    //   1382: invokevirtual saveFragmentViewState : (Landroid/support/v4/app/Fragment;)V
    //   1385: aload_1
    //   1386: invokevirtual performDestroyView : ()V
    //   1389: aload_1
    //   1390: getfield mView : Landroid/view/View;
    //   1393: ifnull -> 1497
    //   1396: aload_1
    //   1397: getfield mContainer : Landroid/view/ViewGroup;
    //   1400: ifnull -> 1497
    //   1403: aconst_null
    //   1404: astore #10
    //   1406: aload #10
    //   1408: astore #9
    //   1410: aload_0
    //   1411: getfield mCurState : I
    //   1414: ifle -> 1439
    //   1417: aload #10
    //   1419: astore #9
    //   1421: aload_0
    //   1422: getfield mDestroyed : Z
    //   1425: ifne -> 1439
    //   1428: aload_0
    //   1429: aload_1
    //   1430: iload_3
    //   1431: iconst_0
    //   1432: iload #4
    //   1434: invokevirtual loadAnimation : (Landroid/support/v4/app/Fragment;IZI)Landroid/view/animation/Animation;
    //   1437: astore #9
    //   1439: aload #9
    //   1441: ifnull -> 1486
    //   1444: aload_1
    //   1445: aload_1
    //   1446: getfield mView : Landroid/view/View;
    //   1449: putfield mAnimatingAway : Landroid/view/View;
    //   1452: aload_1
    //   1453: iload_2
    //   1454: putfield mStateAfterAnimating : I
    //   1457: aload #9
    //   1459: new android/support/v4/app/FragmentManagerImpl$5
    //   1462: dup
    //   1463: aload_0
    //   1464: aload_1
    //   1465: getfield mView : Landroid/view/View;
    //   1468: aload #9
    //   1470: aload_1
    //   1471: invokespecial <init> : (Landroid/support/v4/app/FragmentManagerImpl;Landroid/view/View;Landroid/view/animation/Animation;Landroid/support/v4/app/Fragment;)V
    //   1474: invokevirtual setAnimationListener : (Landroid/view/animation/Animation$AnimationListener;)V
    //   1477: aload_1
    //   1478: getfield mView : Landroid/view/View;
    //   1481: aload #9
    //   1483: invokevirtual startAnimation : (Landroid/view/animation/Animation;)V
    //   1486: aload_1
    //   1487: getfield mContainer : Landroid/view/ViewGroup;
    //   1490: aload_1
    //   1491: getfield mView : Landroid/view/View;
    //   1494: invokevirtual removeView : (Landroid/view/View;)V
    //   1497: aload_1
    //   1498: aconst_null
    //   1499: putfield mContainer : Landroid/view/ViewGroup;
    //   1502: aload_1
    //   1503: aconst_null
    //   1504: putfield mView : Landroid/view/View;
    //   1507: aload_1
    //   1508: aconst_null
    //   1509: putfield mInnerView : Landroid/view/View;
    //   1512: goto -> 1134
    //   1515: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   1518: ifeq -> 1547
    //   1521: ldc 'FragmentManager'
    //   1523: new java/lang/StringBuilder
    //   1526: dup
    //   1527: invokespecial <init> : ()V
    //   1530: ldc_w 'movefrom CREATED: '
    //   1533: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1536: aload_1
    //   1537: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1540: invokevirtual toString : ()Ljava/lang/String;
    //   1543: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   1546: pop
    //   1547: aload_1
    //   1548: getfield mRetaining : Z
    //   1551: ifne -> 1558
    //   1554: aload_1
    //   1555: invokevirtual performDestroy : ()V
    //   1558: aload_1
    //   1559: iconst_0
    //   1560: putfield mCalled : Z
    //   1563: aload_1
    //   1564: invokevirtual onDetach : ()V
    //   1567: aload_1
    //   1568: getfield mCalled : Z
    //   1571: ifne -> 1608
    //   1574: new android/support/v4/app/SuperNotCalledException
    //   1577: dup
    //   1578: new java/lang/StringBuilder
    //   1581: dup
    //   1582: invokespecial <init> : ()V
    //   1585: ldc_w 'Fragment '
    //   1588: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1591: aload_1
    //   1592: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   1595: ldc_w ' did not call through to super.onDetach()'
    //   1598: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   1601: invokevirtual toString : ()Ljava/lang/String;
    //   1604: invokespecial <init> : (Ljava/lang/String;)V
    //   1607: athrow
    //   1608: iload_2
    //   1609: istore #6
    //   1611: iload #5
    //   1613: ifne -> 187
    //   1616: aload_1
    //   1617: getfield mRetaining : Z
    //   1620: ifne -> 1634
    //   1623: aload_0
    //   1624: aload_1
    //   1625: invokevirtual makeInactive : (Landroid/support/v4/app/Fragment;)V
    //   1628: iload_2
    //   1629: istore #6
    //   1631: goto -> 187
    //   1634: aload_1
    //   1635: aconst_null
    //   1636: putfield mHost : Landroid/support/v4/app/FragmentHostCallback;
    //   1639: aload_1
    //   1640: aconst_null
    //   1641: putfield mParentFragment : Landroid/support/v4/app/Fragment;
    //   1644: aload_1
    //   1645: aconst_null
    //   1646: putfield mFragmentManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   1649: aload_1
    //   1650: aconst_null
    //   1651: putfield mChildFragmentManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   1654: iload_2
    //   1655: istore #6
    //   1657: goto -> 187
  }
  
  public void noteStateNotSaved() {
    this.mStateSaved = false;
  }
  
  public View onCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    if ("fragment".equals(paramString)) {
      paramString = paramAttributeSet.getAttributeValue(null, "class");
      TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, FragmentTag.Fragment);
      String str1 = paramString;
      if (paramString == null)
        str1 = typedArray.getString(0); 
      int i = typedArray.getResourceId(1, -1);
      String str2 = typedArray.getString(2);
      typedArray.recycle();
      if (Fragment.isSupportFragmentClass(this.mHost.getContext(), str1)) {
        Fragment fragment1;
        boolean bool;
        if (paramView != null) {
          bool = paramView.getId();
        } else {
          bool = false;
        } 
        if (bool == -1 && i == -1 && str2 == null)
          throw new IllegalArgumentException(paramAttributeSet.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + str1); 
        if (i != -1) {
          Fragment fragment = findFragmentById(i);
        } else {
          paramString = null;
        } 
        String str = paramString;
        if (paramString == null) {
          str = paramString;
          if (str2 != null)
            fragment1 = findFragmentByTag(str2); 
        } 
        Fragment fragment2 = fragment1;
        if (fragment1 == null) {
          fragment2 = fragment1;
          if (bool != -1)
            fragment2 = findFragmentById(bool); 
        } 
        if (DEBUG)
          Log.v("FragmentManager", "onCreateView: id=0x" + Integer.toHexString(i) + " fname=" + str1 + " existing=" + fragment2); 
        if (fragment2 == null) {
          boolean bool1;
          fragment1 = Fragment.instantiate(paramContext, str1);
          fragment1.mFromLayout = true;
          if (i != 0) {
            bool1 = i;
          } else {
            bool1 = bool;
          } 
          fragment1.mFragmentId = bool1;
          fragment1.mContainerId = bool;
          fragment1.mTag = str2;
          fragment1.mInLayout = true;
          fragment1.mFragmentManager = this;
          fragment1.mHost = this.mHost;
          fragment1.onInflate(this.mHost.getContext(), paramAttributeSet, fragment1.mSavedFragmentState);
          addFragment(fragment1, true);
        } else {
          if (fragment2.mInLayout)
            throw new IllegalArgumentException(paramAttributeSet.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(i) + ", tag " + str2 + ", or parent id 0x" + Integer.toHexString(bool) + " with another fragment for " + str1); 
          fragment2.mInLayout = true;
          fragment1 = fragment2;
          if (!fragment2.mRetaining) {
            fragment2.onInflate(this.mHost.getContext(), paramAttributeSet, fragment2.mSavedFragmentState);
            fragment1 = fragment2;
          } 
        } 
        if (this.mCurState < 1 && fragment1.mFromLayout) {
          moveToState(fragment1, 1, 0, 0, false);
        } else {
          moveToState(fragment1);
        } 
        if (fragment1.mView == null)
          throw new IllegalStateException("Fragment " + str1 + " did not create a view."); 
        if (i != 0)
          fragment1.mView.setId(i); 
        if (fragment1.mView.getTag() == null)
          fragment1.mView.setTag(str2); 
        return fragment1.mView;
      } 
    } 
    return null;
  }
  
  public void performPendingDeferredStart(Fragment paramFragment) {
    if (paramFragment.mDeferStart) {
      if (this.mExecutingActions) {
        this.mHavePendingDeferredStart = true;
        return;
      } 
    } else {
      return;
    } 
    paramFragment.mDeferStart = false;
    moveToState(paramFragment, this.mCurState, 0, 0, false);
  }
  
  public void popBackStack() {
    enqueueAction(new Runnable() {
          public void run() {
            FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mHost.getHandler(), null, -1, 0);
          }
        }false);
  }
  
  public void popBackStack(final int id, final int flags) {
    if (id < 0)
      throw new IllegalArgumentException("Bad id: " + id); 
    enqueueAction(new Runnable() {
          public void run() {
            FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mHost.getHandler(), null, id, flags);
          }
        }false);
  }
  
  public void popBackStack(final String name, final int flags) {
    enqueueAction(new Runnable() {
          public void run() {
            FragmentManagerImpl.this.popBackStackState(FragmentManagerImpl.this.mHost.getHandler(), name, -1, flags);
          }
        }false);
  }
  
  public boolean popBackStackImmediate() {
    checkStateLoss();
    executePendingTransactions();
    return popBackStackState(this.mHost.getHandler(), null, -1, 0);
  }
  
  public boolean popBackStackImmediate(int paramInt1, int paramInt2) {
    checkStateLoss();
    executePendingTransactions();
    if (paramInt1 < 0)
      throw new IllegalArgumentException("Bad id: " + paramInt1); 
    return popBackStackState(this.mHost.getHandler(), null, paramInt1, paramInt2);
  }
  
  public boolean popBackStackImmediate(String paramString, int paramInt) {
    checkStateLoss();
    executePendingTransactions();
    return popBackStackState(this.mHost.getHandler(), paramString, -1, paramInt);
  }
  
  boolean popBackStackState(Handler paramHandler, String paramString, int paramInt1, int paramInt2) {
    // Byte code:
    //   0: aload_0
    //   1: getfield mBackStack : Ljava/util/ArrayList;
    //   4: ifnonnull -> 9
    //   7: iconst_0
    //   8: ireturn
    //   9: aload_2
    //   10: ifnonnull -> 92
    //   13: iload_3
    //   14: ifge -> 92
    //   17: iload #4
    //   19: iconst_1
    //   20: iand
    //   21: ifne -> 92
    //   24: aload_0
    //   25: getfield mBackStack : Ljava/util/ArrayList;
    //   28: invokevirtual size : ()I
    //   31: iconst_1
    //   32: isub
    //   33: istore_3
    //   34: iload_3
    //   35: ifge -> 40
    //   38: iconst_0
    //   39: ireturn
    //   40: aload_0
    //   41: getfield mBackStack : Ljava/util/ArrayList;
    //   44: iload_3
    //   45: invokevirtual remove : (I)Ljava/lang/Object;
    //   48: checkcast android/support/v4/app/BackStackRecord
    //   51: astore_1
    //   52: new android/util/SparseArray
    //   55: dup
    //   56: invokespecial <init> : ()V
    //   59: astore_2
    //   60: new android/util/SparseArray
    //   63: dup
    //   64: invokespecial <init> : ()V
    //   67: astore #8
    //   69: aload_1
    //   70: aload_2
    //   71: aload #8
    //   73: invokevirtual calculateBackFragments : (Landroid/util/SparseArray;Landroid/util/SparseArray;)V
    //   76: aload_1
    //   77: iconst_1
    //   78: aconst_null
    //   79: aload_2
    //   80: aload #8
    //   82: invokevirtual popFromBackStack : (ZLandroid/support/v4/app/BackStackRecord$TransitionState;Landroid/util/SparseArray;Landroid/util/SparseArray;)Landroid/support/v4/app/BackStackRecord$TransitionState;
    //   85: pop
    //   86: aload_0
    //   87: invokevirtual reportBackStackChanged : ()V
    //   90: iconst_1
    //   91: ireturn
    //   92: iconst_m1
    //   93: istore #5
    //   95: aload_2
    //   96: ifnonnull -> 103
    //   99: iload_3
    //   100: iflt -> 258
    //   103: aload_0
    //   104: getfield mBackStack : Ljava/util/ArrayList;
    //   107: invokevirtual size : ()I
    //   110: iconst_1
    //   111: isub
    //   112: istore #6
    //   114: iload #6
    //   116: iflt -> 147
    //   119: aload_0
    //   120: getfield mBackStack : Ljava/util/ArrayList;
    //   123: iload #6
    //   125: invokevirtual get : (I)Ljava/lang/Object;
    //   128: checkcast android/support/v4/app/BackStackRecord
    //   131: astore_1
    //   132: aload_2
    //   133: ifnull -> 154
    //   136: aload_2
    //   137: aload_1
    //   138: invokevirtual getName : ()Ljava/lang/String;
    //   141: invokevirtual equals : (Ljava/lang/Object;)Z
    //   144: ifeq -> 154
    //   147: iload #6
    //   149: ifge -> 175
    //   152: iconst_0
    //   153: ireturn
    //   154: iload_3
    //   155: iflt -> 166
    //   158: iload_3
    //   159: aload_1
    //   160: getfield mIndex : I
    //   163: if_icmpeq -> 147
    //   166: iload #6
    //   168: iconst_1
    //   169: isub
    //   170: istore #6
    //   172: goto -> 114
    //   175: iload #6
    //   177: istore #5
    //   179: iload #4
    //   181: iconst_1
    //   182: iand
    //   183: ifeq -> 258
    //   186: iload #6
    //   188: iconst_1
    //   189: isub
    //   190: istore #4
    //   192: iload #4
    //   194: istore #5
    //   196: iload #4
    //   198: iflt -> 258
    //   201: aload_0
    //   202: getfield mBackStack : Ljava/util/ArrayList;
    //   205: iload #4
    //   207: invokevirtual get : (I)Ljava/lang/Object;
    //   210: checkcast android/support/v4/app/BackStackRecord
    //   213: astore_1
    //   214: aload_2
    //   215: ifnull -> 229
    //   218: aload_2
    //   219: aload_1
    //   220: invokevirtual getName : ()Ljava/lang/String;
    //   223: invokevirtual equals : (Ljava/lang/Object;)Z
    //   226: ifne -> 249
    //   229: iload #4
    //   231: istore #5
    //   233: iload_3
    //   234: iflt -> 258
    //   237: iload #4
    //   239: istore #5
    //   241: iload_3
    //   242: aload_1
    //   243: getfield mIndex : I
    //   246: if_icmpne -> 258
    //   249: iload #4
    //   251: iconst_1
    //   252: isub
    //   253: istore #4
    //   255: goto -> 192
    //   258: iload #5
    //   260: aload_0
    //   261: getfield mBackStack : Ljava/util/ArrayList;
    //   264: invokevirtual size : ()I
    //   267: iconst_1
    //   268: isub
    //   269: if_icmpne -> 274
    //   272: iconst_0
    //   273: ireturn
    //   274: new java/util/ArrayList
    //   277: dup
    //   278: invokespecial <init> : ()V
    //   281: astore_2
    //   282: aload_0
    //   283: getfield mBackStack : Ljava/util/ArrayList;
    //   286: invokevirtual size : ()I
    //   289: iconst_1
    //   290: isub
    //   291: istore_3
    //   292: iload_3
    //   293: iload #5
    //   295: if_icmple -> 318
    //   298: aload_2
    //   299: aload_0
    //   300: getfield mBackStack : Ljava/util/ArrayList;
    //   303: iload_3
    //   304: invokevirtual remove : (I)Ljava/lang/Object;
    //   307: invokevirtual add : (Ljava/lang/Object;)Z
    //   310: pop
    //   311: iload_3
    //   312: iconst_1
    //   313: isub
    //   314: istore_3
    //   315: goto -> 292
    //   318: aload_2
    //   319: invokevirtual size : ()I
    //   322: iconst_1
    //   323: isub
    //   324: istore #4
    //   326: new android/util/SparseArray
    //   329: dup
    //   330: invokespecial <init> : ()V
    //   333: astore #8
    //   335: new android/util/SparseArray
    //   338: dup
    //   339: invokespecial <init> : ()V
    //   342: astore #9
    //   344: iconst_0
    //   345: istore_3
    //   346: iload_3
    //   347: iload #4
    //   349: if_icmpgt -> 374
    //   352: aload_2
    //   353: iload_3
    //   354: invokevirtual get : (I)Ljava/lang/Object;
    //   357: checkcast android/support/v4/app/BackStackRecord
    //   360: aload #8
    //   362: aload #9
    //   364: invokevirtual calculateBackFragments : (Landroid/util/SparseArray;Landroid/util/SparseArray;)V
    //   367: iload_3
    //   368: iconst_1
    //   369: iadd
    //   370: istore_3
    //   371: goto -> 346
    //   374: aconst_null
    //   375: astore_1
    //   376: iconst_0
    //   377: istore_3
    //   378: iload_3
    //   379: iload #4
    //   381: if_icmpgt -> 465
    //   384: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   387: ifeq -> 420
    //   390: ldc 'FragmentManager'
    //   392: new java/lang/StringBuilder
    //   395: dup
    //   396: invokespecial <init> : ()V
    //   399: ldc_w 'Popping back stack state: '
    //   402: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   405: aload_2
    //   406: iload_3
    //   407: invokevirtual get : (I)Ljava/lang/Object;
    //   410: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   413: invokevirtual toString : ()Ljava/lang/String;
    //   416: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   419: pop
    //   420: aload_2
    //   421: iload_3
    //   422: invokevirtual get : (I)Ljava/lang/Object;
    //   425: checkcast android/support/v4/app/BackStackRecord
    //   428: astore #10
    //   430: iload_3
    //   431: iload #4
    //   433: if_icmpne -> 459
    //   436: iconst_1
    //   437: istore #7
    //   439: aload #10
    //   441: iload #7
    //   443: aload_1
    //   444: aload #8
    //   446: aload #9
    //   448: invokevirtual popFromBackStack : (ZLandroid/support/v4/app/BackStackRecord$TransitionState;Landroid/util/SparseArray;Landroid/util/SparseArray;)Landroid/support/v4/app/BackStackRecord$TransitionState;
    //   451: astore_1
    //   452: iload_3
    //   453: iconst_1
    //   454: iadd
    //   455: istore_3
    //   456: goto -> 378
    //   459: iconst_0
    //   460: istore #7
    //   462: goto -> 439
    //   465: aload_0
    //   466: invokevirtual reportBackStackChanged : ()V
    //   469: goto -> 90
  }
  
  public void putFragment(Bundle paramBundle, String paramString, Fragment paramFragment) {
    if (paramFragment.mIndex < 0)
      throwException(new IllegalStateException("Fragment " + paramFragment + " is not currently in the FragmentManager")); 
    paramBundle.putInt(paramString, paramFragment.mIndex);
  }
  
  public void removeFragment(Fragment paramFragment, int paramInt1, int paramInt2) {
    boolean bool;
    if (DEBUG)
      Log.v("FragmentManager", "remove: " + paramFragment + " nesting=" + paramFragment.mBackStackNesting); 
    if (!paramFragment.isInBackStack()) {
      bool = true;
    } else {
      bool = false;
    } 
    if (!paramFragment.mDetached || bool) {
      if (this.mAdded != null)
        this.mAdded.remove(paramFragment); 
      if (paramFragment.mHasMenu && paramFragment.mMenuVisible)
        this.mNeedMenuInvalidate = true; 
      paramFragment.mAdded = false;
      paramFragment.mRemoving = true;
      if (bool) {
        bool = false;
      } else {
        bool = true;
      } 
      moveToState(paramFragment, bool, paramInt1, paramInt2, false);
    } 
  }
  
  public void removeOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener paramOnBackStackChangedListener) {
    if (this.mBackStackChangeListeners != null)
      this.mBackStackChangeListeners.remove(paramOnBackStackChangedListener); 
  }
  
  void reportBackStackChanged() {
    if (this.mBackStackChangeListeners != null)
      for (int i = 0; i < this.mBackStackChangeListeners.size(); i++)
        ((FragmentManager.OnBackStackChangedListener)this.mBackStackChangeListeners.get(i)).onBackStackChanged();  
  }
  
  void restoreAllState(Parcelable paramParcelable, List<Fragment> paramList) {
    if (paramParcelable != null) {
      paramParcelable = paramParcelable;
      if (((FragmentManagerState)paramParcelable).mActive != null) {
        if (paramList != null)
          for (int j = 0; j < paramList.size(); j++) {
            Fragment fragment = paramList.get(j);
            if (DEBUG)
              Log.v("FragmentManager", "restoreAllState: re-attaching retained " + fragment); 
            FragmentState fragmentState = ((FragmentManagerState)paramParcelable).mActive[fragment.mIndex];
            fragmentState.mInstance = fragment;
            fragment.mSavedViewState = null;
            fragment.mBackStackNesting = 0;
            fragment.mInLayout = false;
            fragment.mAdded = false;
            fragment.mTarget = null;
            if (fragmentState.mSavedFragmentState != null) {
              fragmentState.mSavedFragmentState.setClassLoader(this.mHost.getContext().getClassLoader());
              fragment.mSavedViewState = fragmentState.mSavedFragmentState.getSparseParcelableArray("android:view_state");
              fragment.mSavedFragmentState = fragmentState.mSavedFragmentState;
            } 
          }  
        this.mActive = new ArrayList<Fragment>(((FragmentManagerState)paramParcelable).mActive.length);
        if (this.mAvailIndices != null)
          this.mAvailIndices.clear(); 
        int i;
        for (i = 0; i < ((FragmentManagerState)paramParcelable).mActive.length; i++) {
          FragmentState fragmentState = ((FragmentManagerState)paramParcelable).mActive[i];
          if (fragmentState != null) {
            Fragment fragment = fragmentState.instantiate(this.mHost, this.mParent);
            if (DEBUG)
              Log.v("FragmentManager", "restoreAllState: active #" + i + ": " + fragment); 
            this.mActive.add(fragment);
            fragmentState.mInstance = null;
          } else {
            this.mActive.add(null);
            if (this.mAvailIndices == null)
              this.mAvailIndices = new ArrayList<Integer>(); 
            if (DEBUG)
              Log.v("FragmentManager", "restoreAllState: avail #" + i); 
            this.mAvailIndices.add(Integer.valueOf(i));
          } 
        } 
        if (paramList != null)
          for (i = 0; i < paramList.size(); i++) {
            Fragment fragment = paramList.get(i);
            if (fragment.mTargetIndex >= 0)
              if (fragment.mTargetIndex < this.mActive.size()) {
                fragment.mTarget = this.mActive.get(fragment.mTargetIndex);
              } else {
                Log.w("FragmentManager", "Re-attaching retained fragment " + fragment + " target no longer exists: " + fragment.mTargetIndex);
                fragment.mTarget = null;
              }  
          }  
        if (((FragmentManagerState)paramParcelable).mAdded != null) {
          this.mAdded = new ArrayList<Fragment>(((FragmentManagerState)paramParcelable).mAdded.length);
          for (i = 0; i < ((FragmentManagerState)paramParcelable).mAdded.length; i++) {
            Fragment fragment = this.mActive.get(((FragmentManagerState)paramParcelable).mAdded[i]);
            if (fragment == null)
              throwException(new IllegalStateException("No instantiated fragment for index #" + ((FragmentManagerState)paramParcelable).mAdded[i])); 
            fragment.mAdded = true;
            if (DEBUG)
              Log.v("FragmentManager", "restoreAllState: added #" + i + ": " + fragment); 
            if (this.mAdded.contains(fragment))
              throw new IllegalStateException("Already added!"); 
            this.mAdded.add(fragment);
          } 
        } else {
          this.mAdded = null;
        } 
        if (((FragmentManagerState)paramParcelable).mBackStack != null) {
          this.mBackStack = new ArrayList<BackStackRecord>(((FragmentManagerState)paramParcelable).mBackStack.length);
          i = 0;
          while (true) {
            if (i < ((FragmentManagerState)paramParcelable).mBackStack.length) {
              BackStackRecord backStackRecord = ((FragmentManagerState)paramParcelable).mBackStack[i].instantiate(this);
              if (DEBUG) {
                Log.v("FragmentManager", "restoreAllState: back stack #" + i + " (index " + backStackRecord.mIndex + "): " + backStackRecord);
                backStackRecord.dump("  ", new PrintWriter((Writer)new LogWriter("FragmentManager")), false);
              } 
              this.mBackStack.add(backStackRecord);
              if (backStackRecord.mIndex >= 0)
                setBackStackIndex(backStackRecord.mIndex, backStackRecord); 
              i++;
              continue;
            } 
            return;
          } 
        } 
        this.mBackStack = null;
        return;
      } 
    } 
  }
  
  ArrayList<Fragment> retainNonConfig() {
    ArrayList<Fragment> arrayList2 = null;
    ArrayList<Fragment> arrayList1 = null;
    if (this.mActive != null) {
      int i = 0;
      while (true) {
        arrayList2 = arrayList1;
        if (i < this.mActive.size()) {
          Fragment fragment = this.mActive.get(i);
          ArrayList<Fragment> arrayList = arrayList1;
          if (fragment != null) {
            arrayList = arrayList1;
            if (fragment.mRetainInstance) {
              byte b;
              arrayList2 = arrayList1;
              if (arrayList1 == null)
                arrayList2 = new ArrayList(); 
              arrayList2.add(fragment);
              fragment.mRetaining = true;
              if (fragment.mTarget != null) {
                b = fragment.mTarget.mIndex;
              } else {
                b = -1;
              } 
              fragment.mTargetIndex = b;
              arrayList = arrayList2;
              if (DEBUG) {
                Log.v("FragmentManager", "retainNonConfig: keeping retained " + fragment);
                arrayList = arrayList2;
              } 
            } 
          } 
          i++;
          arrayList1 = arrayList;
          continue;
        } 
        break;
      } 
    } 
    return arrayList2;
  }
  
  Parcelable saveAllState() {
    execPendingActions();
    if (HONEYCOMB)
      this.mStateSaved = true; 
    if (this.mActive != null && this.mActive.size() > 0) {
      BackStackState[] arrayOfBackStackState;
      int k = this.mActive.size();
      FragmentState[] arrayOfFragmentState = new FragmentState[k];
      int j = 0;
      int i;
      for (i = 0; i < k; i++) {
        Fragment fragment = this.mActive.get(i);
        if (fragment != null) {
          if (fragment.mIndex < 0)
            throwException(new IllegalStateException("Failure saving state: active " + fragment + " has cleared index: " + fragment.mIndex)); 
          byte b = 1;
          FragmentState fragmentState = new FragmentState(fragment);
          arrayOfFragmentState[i] = fragmentState;
          if (fragment.mState > 0 && fragmentState.mSavedFragmentState == null) {
            fragmentState.mSavedFragmentState = saveFragmentBasicState(fragment);
            if (fragment.mTarget != null) {
              if (fragment.mTarget.mIndex < 0)
                throwException(new IllegalStateException("Failure saving state: " + fragment + " has target not in fragment manager: " + fragment.mTarget)); 
              if (fragmentState.mSavedFragmentState == null)
                fragmentState.mSavedFragmentState = new Bundle(); 
              putFragment(fragmentState.mSavedFragmentState, "android:target_state", fragment.mTarget);
              if (fragment.mTargetRequestCode != 0)
                fragmentState.mSavedFragmentState.putInt("android:target_req_state", fragment.mTargetRequestCode); 
            } 
          } else {
            fragmentState.mSavedFragmentState = fragment.mSavedFragmentState;
          } 
          j = b;
          if (DEBUG) {
            Log.v("FragmentManager", "Saved state of " + fragment + ": " + fragmentState.mSavedFragmentState);
            j = b;
          } 
        } 
      } 
      if (!j) {
        if (DEBUG) {
          Log.v("FragmentManager", "saveAllState: no fragments!");
          return null;
        } 
        return null;
      } 
      int[] arrayOfInt2 = null;
      int[] arrayOfInt3 = null;
      int[] arrayOfInt1 = arrayOfInt2;
      if (this.mAdded != null) {
        j = this.mAdded.size();
        arrayOfInt1 = arrayOfInt2;
        if (j > 0) {
          arrayOfInt2 = new int[j];
          i = 0;
          while (true) {
            arrayOfInt1 = arrayOfInt2;
            if (i < j) {
              arrayOfInt2[i] = ((Fragment)this.mAdded.get(i)).mIndex;
              if (arrayOfInt2[i] < 0)
                throwException(new IllegalStateException("Failure saving state: active " + this.mAdded.get(i) + " has cleared index: " + arrayOfInt2[i])); 
              if (DEBUG)
                Log.v("FragmentManager", "saveAllState: adding fragment #" + i + ": " + this.mAdded.get(i)); 
              i++;
              continue;
            } 
            break;
          } 
        } 
      } 
      arrayOfInt2 = arrayOfInt3;
      if (this.mBackStack != null) {
        j = this.mBackStack.size();
        arrayOfInt2 = arrayOfInt3;
        if (j > 0) {
          BackStackState[] arrayOfBackStackState1 = new BackStackState[j];
          i = 0;
          while (true) {
            arrayOfBackStackState = arrayOfBackStackState1;
            if (i < j) {
              arrayOfBackStackState1[i] = new BackStackState(this.mBackStack.get(i));
              if (DEBUG)
                Log.v("FragmentManager", "saveAllState: adding back stack #" + i + ": " + this.mBackStack.get(i)); 
              i++;
              continue;
            } 
            break;
          } 
        } 
      } 
      FragmentManagerState fragmentManagerState = new FragmentManagerState();
      fragmentManagerState.mActive = arrayOfFragmentState;
      fragmentManagerState.mAdded = arrayOfInt1;
      fragmentManagerState.mBackStack = arrayOfBackStackState;
      return fragmentManagerState;
    } 
    return null;
  }
  
  Bundle saveFragmentBasicState(Fragment paramFragment) {
    Bundle bundle2 = null;
    if (this.mStateBundle == null)
      this.mStateBundle = new Bundle(); 
    paramFragment.performSaveInstanceState(this.mStateBundle);
    if (!this.mStateBundle.isEmpty()) {
      bundle2 = this.mStateBundle;
      this.mStateBundle = null;
    } 
    if (paramFragment.mView != null)
      saveFragmentViewState(paramFragment); 
    Bundle bundle1 = bundle2;
    if (paramFragment.mSavedViewState != null) {
      bundle1 = bundle2;
      if (bundle2 == null)
        bundle1 = new Bundle(); 
      bundle1.putSparseParcelableArray("android:view_state", paramFragment.mSavedViewState);
    } 
    bundle2 = bundle1;
    if (!paramFragment.mUserVisibleHint) {
      bundle2 = bundle1;
      if (bundle1 == null)
        bundle2 = new Bundle(); 
      bundle2.putBoolean("android:user_visible_hint", paramFragment.mUserVisibleHint);
    } 
    return bundle2;
  }
  
  public Fragment.SavedState saveFragmentInstanceState(Fragment paramFragment) {
    Fragment.SavedState savedState2 = null;
    if (paramFragment.mIndex < 0)
      throwException(new IllegalStateException("Fragment " + paramFragment + " is not currently in the FragmentManager")); 
    Fragment.SavedState savedState1 = savedState2;
    if (paramFragment.mState > 0) {
      Bundle bundle = saveFragmentBasicState(paramFragment);
      savedState1 = savedState2;
      if (bundle != null)
        savedState1 = new Fragment.SavedState(bundle); 
    } 
    return savedState1;
  }
  
  void saveFragmentViewState(Fragment paramFragment) {
    if (paramFragment.mInnerView != null) {
      if (this.mStateArray == null) {
        this.mStateArray = new SparseArray();
      } else {
        this.mStateArray.clear();
      } 
      paramFragment.mInnerView.saveHierarchyState(this.mStateArray);
      if (this.mStateArray.size() > 0) {
        paramFragment.mSavedViewState = this.mStateArray;
        this.mStateArray = null;
        return;
      } 
    } 
  }
  
  public void setBackStackIndex(int paramInt, BackStackRecord paramBackStackRecord) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   6: ifnonnull -> 20
    //   9: aload_0
    //   10: new java/util/ArrayList
    //   13: dup
    //   14: invokespecial <init> : ()V
    //   17: putfield mBackStackIndices : Ljava/util/ArrayList;
    //   20: aload_0
    //   21: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   24: invokevirtual size : ()I
    //   27: istore #4
    //   29: iload #4
    //   31: istore_3
    //   32: iload_1
    //   33: iload #4
    //   35: if_icmpge -> 93
    //   38: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   41: ifeq -> 80
    //   44: ldc 'FragmentManager'
    //   46: new java/lang/StringBuilder
    //   49: dup
    //   50: invokespecial <init> : ()V
    //   53: ldc_w 'Setting back stack index '
    //   56: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   59: iload_1
    //   60: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   63: ldc_w ' to '
    //   66: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   69: aload_2
    //   70: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   73: invokevirtual toString : ()Ljava/lang/String;
    //   76: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   79: pop
    //   80: aload_0
    //   81: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   84: iload_1
    //   85: aload_2
    //   86: invokevirtual set : (ILjava/lang/Object;)Ljava/lang/Object;
    //   89: pop
    //   90: aload_0
    //   91: monitorexit
    //   92: return
    //   93: iload_3
    //   94: iload_1
    //   95: if_icmpge -> 176
    //   98: aload_0
    //   99: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   102: aconst_null
    //   103: invokevirtual add : (Ljava/lang/Object;)Z
    //   106: pop
    //   107: aload_0
    //   108: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   111: ifnonnull -> 125
    //   114: aload_0
    //   115: new java/util/ArrayList
    //   118: dup
    //   119: invokespecial <init> : ()V
    //   122: putfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   125: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   128: ifeq -> 157
    //   131: ldc 'FragmentManager'
    //   133: new java/lang/StringBuilder
    //   136: dup
    //   137: invokespecial <init> : ()V
    //   140: ldc_w 'Adding available back stack index '
    //   143: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   146: iload_3
    //   147: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   150: invokevirtual toString : ()Ljava/lang/String;
    //   153: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   156: pop
    //   157: aload_0
    //   158: getfield mAvailBackStackIndices : Ljava/util/ArrayList;
    //   161: iload_3
    //   162: invokestatic valueOf : (I)Ljava/lang/Integer;
    //   165: invokevirtual add : (Ljava/lang/Object;)Z
    //   168: pop
    //   169: iload_3
    //   170: iconst_1
    //   171: iadd
    //   172: istore_3
    //   173: goto -> 93
    //   176: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   179: ifeq -> 218
    //   182: ldc 'FragmentManager'
    //   184: new java/lang/StringBuilder
    //   187: dup
    //   188: invokespecial <init> : ()V
    //   191: ldc_w 'Adding back stack index '
    //   194: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   197: iload_1
    //   198: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   201: ldc_w ' with '
    //   204: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   207: aload_2
    //   208: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   211: invokevirtual toString : ()Ljava/lang/String;
    //   214: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   217: pop
    //   218: aload_0
    //   219: getfield mBackStackIndices : Ljava/util/ArrayList;
    //   222: aload_2
    //   223: invokevirtual add : (Ljava/lang/Object;)Z
    //   226: pop
    //   227: goto -> 90
    //   230: astore_2
    //   231: aload_0
    //   232: monitorexit
    //   233: aload_2
    //   234: athrow
    // Exception table:
    //   from	to	target	type
    //   2	20	230	finally
    //   20	29	230	finally
    //   38	80	230	finally
    //   80	90	230	finally
    //   90	92	230	finally
    //   98	125	230	finally
    //   125	157	230	finally
    //   157	169	230	finally
    //   176	218	230	finally
    //   218	227	230	finally
    //   231	233	230	finally
  }
  
  public void showFragment(Fragment paramFragment, int paramInt1, int paramInt2) {
    if (DEBUG)
      Log.v("FragmentManager", "show: " + paramFragment); 
    if (paramFragment.mHidden) {
      paramFragment.mHidden = false;
      if (paramFragment.mView != null) {
        Animation animation = loadAnimation(paramFragment, paramInt1, true, paramInt2);
        if (animation != null) {
          setHWLayerAnimListenerIfAlpha(paramFragment.mView, animation);
          paramFragment.mView.startAnimation(animation);
        } 
        paramFragment.mView.setVisibility(0);
      } 
      if (paramFragment.mAdded && paramFragment.mHasMenu && paramFragment.mMenuVisible)
        this.mNeedMenuInvalidate = true; 
      paramFragment.onHiddenChanged(false);
    } 
  }
  
  void startPendingDeferredFragments() {
    if (this.mActive != null) {
      int i = 0;
      while (true) {
        if (i < this.mActive.size()) {
          Fragment fragment = this.mActive.get(i);
          if (fragment != null)
            performPendingDeferredStart(fragment); 
          i++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(128);
    stringBuilder.append("FragmentManager{");
    stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    stringBuilder.append(" in ");
    if (this.mParent != null) {
      DebugUtils.buildShortClassTag(this.mParent, stringBuilder);
      stringBuilder.append("}}");
      return stringBuilder.toString();
    } 
    DebugUtils.buildShortClassTag(this.mHost, stringBuilder);
    stringBuilder.append("}}");
    return stringBuilder.toString();
  }
  
  static {
    boolean bool = false;
  }
  
  static {
    if (Build.VERSION.SDK_INT >= 11)
      bool = true; 
    HONEYCOMB = bool;
  }
  
  static class AnimateOnHWLayerIfNeededListener implements Animation.AnimationListener {
    private Animation.AnimationListener mOrignalListener = null;
    
    private boolean mShouldRunOnHWLayer = false;
    
    private View mView = null;
    
    public AnimateOnHWLayerIfNeededListener(View param1View, Animation param1Animation) {
      if (param1View == null || param1Animation == null)
        return; 
      this.mView = param1View;
    }
    
    public AnimateOnHWLayerIfNeededListener(View param1View, Animation param1Animation, Animation.AnimationListener param1AnimationListener) {
      if (param1View == null || param1Animation == null)
        return; 
      this.mOrignalListener = param1AnimationListener;
      this.mView = param1View;
    }
    
    @CallSuper
    public void onAnimationEnd(Animation param1Animation) {
      if (this.mView != null && this.mShouldRunOnHWLayer)
        this.mView.post(new Runnable() {
              public void run() {
                ViewCompat.setLayerType(FragmentManagerImpl.AnimateOnHWLayerIfNeededListener.this.mView, 0, null);
              }
            }); 
      if (this.mOrignalListener != null)
        this.mOrignalListener.onAnimationEnd(param1Animation); 
    }
    
    public void onAnimationRepeat(Animation param1Animation) {
      if (this.mOrignalListener != null)
        this.mOrignalListener.onAnimationRepeat(param1Animation); 
    }
    
    @CallSuper
    public void onAnimationStart(Animation param1Animation) {
      if (this.mView != null) {
        this.mShouldRunOnHWLayer = FragmentManagerImpl.shouldRunOnHWLayer(this.mView, param1Animation);
        if (this.mShouldRunOnHWLayer)
          this.mView.post(new Runnable() {
                public void run() {
                  ViewCompat.setLayerType(FragmentManagerImpl.AnimateOnHWLayerIfNeededListener.this.mView, 2, null);
                }
              }); 
      } 
      if (this.mOrignalListener != null)
        this.mOrignalListener.onAnimationStart(param1Animation); 
    }
  }
  
  class null implements Runnable {
    public void run() {
      ViewCompat.setLayerType(this.this$0.mView, 2, null);
    }
  }
  
  class null implements Runnable {
    public void run() {
      ViewCompat.setLayerType(this.this$0.mView, 0, null);
    }
  }
  
  static class FragmentTag {
    public static final int[] Fragment = new int[] { 16842755, 16842960, 16842961 };
    
    public static final int Fragment_id = 1;
    
    public static final int Fragment_name = 0;
    
    public static final int Fragment_tag = 2;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\app\FragmentManagerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */