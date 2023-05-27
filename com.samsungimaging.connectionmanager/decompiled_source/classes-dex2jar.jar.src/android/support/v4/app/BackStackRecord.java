package android.support.v4.app;

import android.os.Build;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.LogWriter;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Map;

final class BackStackRecord extends FragmentTransaction implements FragmentManager.BackStackEntry, Runnable {
  static final int OP_ADD = 1;
  
  static final int OP_ATTACH = 7;
  
  static final int OP_DETACH = 6;
  
  static final int OP_HIDE = 4;
  
  static final int OP_NULL = 0;
  
  static final int OP_REMOVE = 3;
  
  static final int OP_REPLACE = 2;
  
  static final int OP_SHOW = 5;
  
  static final boolean SUPPORTS_TRANSITIONS;
  
  static final String TAG = "FragmentManager";
  
  boolean mAddToBackStack;
  
  boolean mAllowAddToBackStack = true;
  
  int mBreadCrumbShortTitleRes;
  
  CharSequence mBreadCrumbShortTitleText;
  
  int mBreadCrumbTitleRes;
  
  CharSequence mBreadCrumbTitleText;
  
  boolean mCommitted;
  
  int mEnterAnim;
  
  int mExitAnim;
  
  Op mHead;
  
  int mIndex = -1;
  
  final FragmentManagerImpl mManager;
  
  String mName;
  
  int mNumOp;
  
  int mPopEnterAnim;
  
  int mPopExitAnim;
  
  ArrayList<String> mSharedElementSourceNames;
  
  ArrayList<String> mSharedElementTargetNames;
  
  Op mTail;
  
  int mTransition;
  
  int mTransitionStyle;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 21) {
      bool = true;
    } else {
      bool = false;
    } 
    SUPPORTS_TRANSITIONS = bool;
  }
  
  public BackStackRecord(FragmentManagerImpl paramFragmentManagerImpl) {
    this.mManager = paramFragmentManagerImpl;
  }
  
  private TransitionState beginTransition(SparseArray<Fragment> paramSparseArray1, SparseArray<Fragment> paramSparseArray2, boolean paramBoolean) {
    TransitionState transitionState2 = new TransitionState();
    transitionState2.nonExistentView = new View(this.mManager.mHost.getContext());
    boolean bool = false;
    int i;
    for (i = 0; i < paramSparseArray1.size(); i++) {
      if (configureTransitions(paramSparseArray1.keyAt(i), transitionState2, paramBoolean, paramSparseArray1, paramSparseArray2))
        bool = true; 
    } 
    i = 0;
    while (i < paramSparseArray2.size()) {
      int j = paramSparseArray2.keyAt(i);
      boolean bool1 = bool;
      if (paramSparseArray1.get(j) == null) {
        bool1 = bool;
        if (configureTransitions(j, transitionState2, paramBoolean, paramSparseArray1, paramSparseArray2))
          bool1 = true; 
      } 
      i++;
      bool = bool1;
    } 
    TransitionState transitionState1 = transitionState2;
    if (!bool)
      transitionState1 = null; 
    return transitionState1;
  }
  
  private void calculateFragments(SparseArray<Fragment> paramSparseArray1, SparseArray<Fragment> paramSparseArray2) {
    if (this.mManager.mContainer.onHasView()) {
      Op op = this.mHead;
      while (true) {
        if (op != null)
          continue; 
        return;
        switch (op.cmd) {
          case 1:
            setLastIn(paramSparseArray2, op.fragment);
            op = op.next;
            break;
          case 2:
            fragment1 = op.fragment;
            fragment2 = fragment1;
          case 3:
            setFirstOut(paramSparseArray1, op.fragment);
            op = op.next;
            break;
          case 4:
            setFirstOut(paramSparseArray1, op.fragment);
            op = op.next;
            break;
          case 5:
            setLastIn(paramSparseArray2, op.fragment);
            op = op.next;
            break;
          case 6:
            setFirstOut(paramSparseArray1, op.fragment);
            op = op.next;
            break;
          case 7:
            setLastIn(paramSparseArray2, op.fragment);
            op = op.next;
            break;
        } 
      } 
    } 
  }
  
  private void callSharedElementEnd(TransitionState paramTransitionState, Fragment paramFragment1, Fragment paramFragment2, boolean paramBoolean, ArrayMap<String, View> paramArrayMap) {
    SharedElementCallback sharedElementCallback;
    if (paramBoolean) {
      sharedElementCallback = paramFragment2.mEnterTransitionCallback;
    } else {
      sharedElementCallback = paramFragment1.mEnterTransitionCallback;
    } 
    if (sharedElementCallback != null)
      sharedElementCallback.onSharedElementEnd(new ArrayList<String>(paramArrayMap.keySet()), new ArrayList<View>(paramArrayMap.values()), null); 
  }
  
  private static Object captureExitingViews(Object paramObject, Fragment paramFragment, ArrayList<View> paramArrayList, ArrayMap<String, View> paramArrayMap, View paramView) {
    Object object = paramObject;
    if (paramObject != null)
      object = FragmentTransitionCompat21.captureExitingViews(paramObject, paramFragment.getView(), paramArrayList, (Map<String, View>)paramArrayMap, paramView); 
    return object;
  }
  
  private boolean configureTransitions(int paramInt, TransitionState paramTransitionState, boolean paramBoolean, SparseArray<Fragment> paramSparseArray1, SparseArray<Fragment> paramSparseArray2) {
    ArrayMap<String, View> arrayMap;
    ViewGroup viewGroup = (ViewGroup)this.mManager.mContainer.onFindViewById(paramInt);
    if (viewGroup == null)
      return false; 
    final Fragment inFragment = (Fragment)paramSparseArray2.get(paramInt);
    Fragment fragment2 = (Fragment)paramSparseArray1.get(paramInt);
    Object object5 = getEnterTransition(fragment1, paramBoolean);
    Object object2 = getSharedElementTransition(fragment1, fragment2, paramBoolean);
    Object object6 = getExitTransition(fragment2, paramBoolean);
    paramSparseArray1 = null;
    ArrayList<View> arrayList = new ArrayList();
    Object object1 = object2;
    if (object2 != null) {
      ArrayMap<String, View> arrayMap2 = remapSharedElements(paramTransitionState, fragment2, paramBoolean);
      if (arrayMap2.isEmpty()) {
        object1 = null;
        paramSparseArray1 = null;
      } else {
        SharedElementCallback sharedElementCallback;
        if (paramBoolean) {
          sharedElementCallback = fragment2.mEnterTransitionCallback;
        } else {
          sharedElementCallback = fragment1.mEnterTransitionCallback;
        } 
        if (sharedElementCallback != null)
          sharedElementCallback.onSharedElementStart(new ArrayList<String>(arrayMap2.keySet()), new ArrayList<View>(arrayMap2.values()), null); 
        prepareSharedElementTransition(paramTransitionState, (View)viewGroup, object2, fragment1, fragment2, paramBoolean, arrayList);
        object1 = object2;
        arrayMap = arrayMap2;
      } 
    } 
    if (object5 == null && object1 == null && object6 == null)
      return false; 
    object2 = new ArrayList();
    Object object3 = captureExitingViews(object6, fragment2, (ArrayList<View>)object2, arrayMap, paramTransitionState.nonExistentView);
    if (this.mSharedElementTargetNames != null && arrayMap != null) {
      View view = (View)arrayMap.get(this.mSharedElementTargetNames.get(0));
      if (view != null) {
        if (object3 != null)
          FragmentTransitionCompat21.setEpicenter(object3, view); 
        if (object1 != null)
          FragmentTransitionCompat21.setEpicenter(object1, view); 
      } 
    } 
    FragmentTransitionCompat21.ViewRetriever viewRetriever = new FragmentTransitionCompat21.ViewRetriever() {
        public View getView() {
          return inFragment.getView();
        }
      };
    object6 = new ArrayList();
    ArrayMap arrayMap1 = new ArrayMap();
    boolean bool = true;
    if (fragment1 != null)
      if (paramBoolean) {
        bool = fragment1.getAllowReturnTransitionOverlap();
      } else {
        bool = fragment1.getAllowEnterTransitionOverlap();
      }  
    Object object4 = FragmentTransitionCompat21.mergeTransitions(object5, object3, object1, bool);
    if (object4 != null) {
      FragmentTransitionCompat21.addTransitionTargets(object5, object1, (View)viewGroup, viewRetriever, paramTransitionState.nonExistentView, paramTransitionState.enteringEpicenterView, (Map<String, String>)paramTransitionState.nameOverrides, (ArrayList<View>)object6, (Map<String, View>)arrayMap, (Map<String, View>)arrayMap1, arrayList);
      excludeHiddenFragmentsAfterEnter((View)viewGroup, paramTransitionState, paramInt, object4);
      FragmentTransitionCompat21.excludeTarget(object4, paramTransitionState.nonExistentView, true);
      excludeHiddenFragments(paramTransitionState, paramInt, object4);
      FragmentTransitionCompat21.beginDelayedTransition(viewGroup, object4);
      FragmentTransitionCompat21.cleanupTransitions((View)viewGroup, paramTransitionState.nonExistentView, object5, (ArrayList<View>)object6, object3, (ArrayList<View>)object2, object1, arrayList, object4, paramTransitionState.hiddenFragmentViews, (Map<String, View>)arrayMap1);
    } 
    return (object4 != null);
  }
  
  private void doAddOp(int paramInt1, Fragment paramFragment, String paramString, int paramInt2) {
    paramFragment.mFragmentManager = this.mManager;
    if (paramString != null) {
      if (paramFragment.mTag != null && !paramString.equals(paramFragment.mTag))
        throw new IllegalStateException("Can't change tag of fragment " + paramFragment + ": was " + paramFragment.mTag + " now " + paramString); 
      paramFragment.mTag = paramString;
    } 
    if (paramInt1 != 0) {
      if (paramFragment.mFragmentId != 0 && paramFragment.mFragmentId != paramInt1)
        throw new IllegalStateException("Can't change container ID of fragment " + paramFragment + ": was " + paramFragment.mFragmentId + " now " + paramInt1); 
      paramFragment.mFragmentId = paramInt1;
      paramFragment.mContainerId = paramInt1;
    } 
    Op op = new Op();
    op.cmd = paramInt2;
    op.fragment = paramFragment;
    addOp(op);
  }
  
  private void excludeHiddenFragments(TransitionState paramTransitionState, int paramInt, Object paramObject) {
    if (this.mManager.mAdded != null)
      for (int i = 0; i < this.mManager.mAdded.size(); i++) {
        Fragment fragment = this.mManager.mAdded.get(i);
        if (fragment.mView != null && fragment.mContainer != null && fragment.mContainerId == paramInt)
          if (fragment.mHidden) {
            if (!paramTransitionState.hiddenFragmentViews.contains(fragment.mView)) {
              FragmentTransitionCompat21.excludeTarget(paramObject, fragment.mView, true);
              paramTransitionState.hiddenFragmentViews.add(fragment.mView);
            } 
          } else {
            FragmentTransitionCompat21.excludeTarget(paramObject, fragment.mView, false);
            paramTransitionState.hiddenFragmentViews.remove(fragment.mView);
          }  
      }  
  }
  
  private void excludeHiddenFragmentsAfterEnter(final View sceneRoot, final TransitionState state, final int containerId, final Object transition) {
    sceneRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          public boolean onPreDraw() {
            sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
            BackStackRecord.this.excludeHiddenFragments(state, containerId, transition);
            return true;
          }
        });
  }
  
  private static Object getEnterTransition(Fragment paramFragment, boolean paramBoolean) {
    if (paramFragment == null)
      return null; 
    if (paramBoolean) {
      object = paramFragment.getReenterTransition();
      return FragmentTransitionCompat21.cloneTransition(object);
    } 
    Object object = object.getEnterTransition();
    return FragmentTransitionCompat21.cloneTransition(object);
  }
  
  private static Object getExitTransition(Fragment paramFragment, boolean paramBoolean) {
    if (paramFragment == null)
      return null; 
    if (paramBoolean) {
      object = paramFragment.getReturnTransition();
      return FragmentTransitionCompat21.cloneTransition(object);
    } 
    Object object = object.getExitTransition();
    return FragmentTransitionCompat21.cloneTransition(object);
  }
  
  private static Object getSharedElementTransition(Fragment paramFragment1, Fragment paramFragment2, boolean paramBoolean) {
    if (paramFragment1 == null || paramFragment2 == null)
      return null; 
    if (paramBoolean) {
      object = paramFragment2.getSharedElementReturnTransition();
      return FragmentTransitionCompat21.wrapSharedElementTransition(object);
    } 
    Object object = object.getSharedElementEnterTransition();
    return FragmentTransitionCompat21.wrapSharedElementTransition(object);
  }
  
  private ArrayMap<String, View> mapEnteringSharedElements(TransitionState paramTransitionState, Fragment paramFragment, boolean paramBoolean) {
    ArrayMap<String, View> arrayMap2 = new ArrayMap();
    View view = paramFragment.getView();
    ArrayMap<String, View> arrayMap1 = arrayMap2;
    if (view != null) {
      arrayMap1 = arrayMap2;
      if (this.mSharedElementSourceNames != null) {
        FragmentTransitionCompat21.findNamedViews((Map<String, View>)arrayMap2, view);
        if (paramBoolean)
          return remapNames(this.mSharedElementSourceNames, this.mSharedElementTargetNames, arrayMap2); 
      } else {
        return arrayMap1;
      } 
    } else {
      return arrayMap1;
    } 
    arrayMap2.retainAll(this.mSharedElementTargetNames);
    return arrayMap2;
  }
  
  private ArrayMap<String, View> mapSharedElementsIn(TransitionState paramTransitionState, boolean paramBoolean, Fragment paramFragment) {
    ArrayMap<String, View> arrayMap = mapEnteringSharedElements(paramTransitionState, paramFragment, paramBoolean);
    if (paramBoolean) {
      if (paramFragment.mExitTransitionCallback != null)
        paramFragment.mExitTransitionCallback.onMapSharedElements(this.mSharedElementTargetNames, (Map<String, View>)arrayMap); 
      setBackNameOverrides(paramTransitionState, arrayMap, true);
      return arrayMap;
    } 
    if (paramFragment.mEnterTransitionCallback != null)
      paramFragment.mEnterTransitionCallback.onMapSharedElements(this.mSharedElementTargetNames, (Map<String, View>)arrayMap); 
    setNameOverrides(paramTransitionState, arrayMap, true);
    return arrayMap;
  }
  
  private void prepareSharedElementTransition(final TransitionState state, final View sceneRoot, final Object sharedElementTransition, final Fragment inFragment, final Fragment outFragment, final boolean isBack, final ArrayList<View> sharedElementTargets) {
    sceneRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
          public boolean onPreDraw() {
            sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
            if (sharedElementTransition != null) {
              FragmentTransitionCompat21.removeTargets(sharedElementTransition, sharedElementTargets);
              sharedElementTargets.clear();
              ArrayMap arrayMap = BackStackRecord.this.mapSharedElementsIn(state, isBack, inFragment);
              FragmentTransitionCompat21.setSharedElementTargets(sharedElementTransition, state.nonExistentView, (Map<String, View>)arrayMap, sharedElementTargets);
              BackStackRecord.this.setEpicenterIn(arrayMap, state);
              BackStackRecord.this.callSharedElementEnd(state, inFragment, outFragment, isBack, arrayMap);
            } 
            return true;
          }
        });
  }
  
  private static ArrayMap<String, View> remapNames(ArrayList<String> paramArrayList1, ArrayList<String> paramArrayList2, ArrayMap<String, View> paramArrayMap) {
    if (paramArrayMap.isEmpty())
      return paramArrayMap; 
    ArrayMap<String, View> arrayMap = new ArrayMap();
    int j = paramArrayList1.size();
    for (int i = 0; i < j; i++) {
      View view = (View)paramArrayMap.get(paramArrayList1.get(i));
      if (view != null)
        arrayMap.put(paramArrayList2.get(i), view); 
    } 
    return arrayMap;
  }
  
  private ArrayMap<String, View> remapSharedElements(TransitionState paramTransitionState, Fragment paramFragment, boolean paramBoolean) {
    ArrayMap<String, View> arrayMap2 = new ArrayMap();
    ArrayMap<String, View> arrayMap1 = arrayMap2;
    if (this.mSharedElementSourceNames != null) {
      FragmentTransitionCompat21.findNamedViews((Map<String, View>)arrayMap2, paramFragment.getView());
      if (paramBoolean) {
        arrayMap2.retainAll(this.mSharedElementTargetNames);
        arrayMap1 = arrayMap2;
      } else {
        arrayMap1 = remapNames(this.mSharedElementSourceNames, this.mSharedElementTargetNames, arrayMap2);
      } 
    } 
    if (paramBoolean) {
      if (paramFragment.mEnterTransitionCallback != null)
        paramFragment.mEnterTransitionCallback.onMapSharedElements(this.mSharedElementTargetNames, (Map<String, View>)arrayMap1); 
      setBackNameOverrides(paramTransitionState, arrayMap1, false);
      return arrayMap1;
    } 
    if (paramFragment.mExitTransitionCallback != null)
      paramFragment.mExitTransitionCallback.onMapSharedElements(this.mSharedElementTargetNames, (Map<String, View>)arrayMap1); 
    setNameOverrides(paramTransitionState, arrayMap1, false);
    return arrayMap1;
  }
  
  private void setBackNameOverrides(TransitionState paramTransitionState, ArrayMap<String, View> paramArrayMap, boolean paramBoolean) {
    int i;
    if (this.mSharedElementTargetNames == null) {
      i = 0;
    } else {
      i = this.mSharedElementTargetNames.size();
    } 
    for (int j = 0; j < i; j++) {
      String str = this.mSharedElementSourceNames.get(j);
      View view = (View)paramArrayMap.get(this.mSharedElementTargetNames.get(j));
      if (view != null) {
        String str1 = FragmentTransitionCompat21.getTransitionName(view);
        if (paramBoolean) {
          setNameOverride(paramTransitionState.nameOverrides, str, str1);
        } else {
          setNameOverride(paramTransitionState.nameOverrides, str1, str);
        } 
      } 
    } 
  }
  
  private void setEpicenterIn(ArrayMap<String, View> paramArrayMap, TransitionState paramTransitionState) {
    if (this.mSharedElementTargetNames != null && !paramArrayMap.isEmpty()) {
      View view = (View)paramArrayMap.get(this.mSharedElementTargetNames.get(0));
      if (view != null)
        paramTransitionState.enteringEpicenterView.epicenter = view; 
    } 
  }
  
  private static void setFirstOut(SparseArray<Fragment> paramSparseArray, Fragment paramFragment) {
    if (paramFragment != null) {
      int i = paramFragment.mContainerId;
      if (i != 0 && !paramFragment.isHidden() && paramFragment.isAdded() && paramFragment.getView() != null && paramSparseArray.get(i) == null)
        paramSparseArray.put(i, paramFragment); 
    } 
  }
  
  private void setLastIn(SparseArray<Fragment> paramSparseArray, Fragment paramFragment) {
    if (paramFragment != null) {
      int i = paramFragment.mContainerId;
      if (i != 0)
        paramSparseArray.put(i, paramFragment); 
    } 
  }
  
  private static void setNameOverride(ArrayMap<String, String> paramArrayMap, String paramString1, String paramString2) {
    if (paramString1 != null && paramString2 != null) {
      for (int i = 0; i < paramArrayMap.size(); i++) {
        if (paramString1.equals(paramArrayMap.valueAt(i))) {
          paramArrayMap.setValueAt(i, paramString2);
          return;
        } 
      } 
    } else {
      return;
    } 
    paramArrayMap.put(paramString1, paramString2);
  }
  
  private void setNameOverrides(TransitionState paramTransitionState, ArrayMap<String, View> paramArrayMap, boolean paramBoolean) {
    int j = paramArrayMap.size();
    for (int i = 0; i < j; i++) {
      String str1 = (String)paramArrayMap.keyAt(i);
      String str2 = FragmentTransitionCompat21.getTransitionName((View)paramArrayMap.valueAt(i));
      if (paramBoolean) {
        setNameOverride(paramTransitionState.nameOverrides, str1, str2);
      } else {
        setNameOverride(paramTransitionState.nameOverrides, str2, str1);
      } 
    } 
  }
  
  private static void setNameOverrides(TransitionState paramTransitionState, ArrayList<String> paramArrayList1, ArrayList<String> paramArrayList2) {
    if (paramArrayList1 != null)
      for (int i = 0; i < paramArrayList1.size(); i++) {
        String str1 = paramArrayList1.get(i);
        String str2 = paramArrayList2.get(i);
        setNameOverride(paramTransitionState.nameOverrides, str1, str2);
      }  
  }
  
  public FragmentTransaction add(int paramInt, Fragment paramFragment) {
    doAddOp(paramInt, paramFragment, null, 1);
    return this;
  }
  
  public FragmentTransaction add(int paramInt, Fragment paramFragment, String paramString) {
    doAddOp(paramInt, paramFragment, paramString, 1);
    return this;
  }
  
  public FragmentTransaction add(Fragment paramFragment, String paramString) {
    doAddOp(0, paramFragment, paramString, 1);
    return this;
  }
  
  void addOp(Op paramOp) {
    if (this.mHead == null) {
      this.mTail = paramOp;
      this.mHead = paramOp;
    } else {
      paramOp.prev = this.mTail;
      this.mTail.next = paramOp;
      this.mTail = paramOp;
    } 
    paramOp.enterAnim = this.mEnterAnim;
    paramOp.exitAnim = this.mExitAnim;
    paramOp.popEnterAnim = this.mPopEnterAnim;
    paramOp.popExitAnim = this.mPopExitAnim;
    this.mNumOp++;
  }
  
  public FragmentTransaction addSharedElement(View paramView, String paramString) {
    if (SUPPORTS_TRANSITIONS) {
      String str = FragmentTransitionCompat21.getTransitionName(paramView);
      if (str == null)
        throw new IllegalArgumentException("Unique transitionNames are required for all sharedElements"); 
      if (this.mSharedElementSourceNames == null) {
        this.mSharedElementSourceNames = new ArrayList<String>();
        this.mSharedElementTargetNames = new ArrayList<String>();
      } 
      this.mSharedElementSourceNames.add(str);
      this.mSharedElementTargetNames.add(paramString);
    } 
    return this;
  }
  
  public FragmentTransaction addToBackStack(String paramString) {
    if (!this.mAllowAddToBackStack)
      throw new IllegalStateException("This FragmentTransaction is not allowed to be added to the back stack."); 
    this.mAddToBackStack = true;
    this.mName = paramString;
    return this;
  }
  
  public FragmentTransaction attach(Fragment paramFragment) {
    Op op = new Op();
    op.cmd = 7;
    op.fragment = paramFragment;
    addOp(op);
    return this;
  }
  
  void bumpBackStackNesting(int paramInt) {
    if (this.mAddToBackStack) {
      if (FragmentManagerImpl.DEBUG)
        Log.v("FragmentManager", "Bump nesting in " + this + " by " + paramInt); 
      Op op = this.mHead;
      while (true) {
        if (op != null) {
          if (op.fragment != null) {
            Fragment fragment = op.fragment;
            fragment.mBackStackNesting += paramInt;
            if (FragmentManagerImpl.DEBUG)
              Log.v("FragmentManager", "Bump nesting of " + op.fragment + " to " + op.fragment.mBackStackNesting); 
          } 
          if (op.removed != null)
            for (int i = op.removed.size() - 1; i >= 0; i--) {
              Fragment fragment = op.removed.get(i);
              fragment.mBackStackNesting += paramInt;
              if (FragmentManagerImpl.DEBUG)
                Log.v("FragmentManager", "Bump nesting of " + fragment + " to " + fragment.mBackStackNesting); 
            }  
          op = op.next;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public void calculateBackFragments(SparseArray<Fragment> paramSparseArray1, SparseArray<Fragment> paramSparseArray2) {
    if (this.mManager.mContainer.onHasView()) {
      Op op = this.mHead;
      while (true) {
        if (op != null) {
          switch (op.cmd) {
            case 1:
              setFirstOut(paramSparseArray1, op.fragment);
              op = op.next;
              break;
            case 2:
              if (op.removed != null)
                for (int i = op.removed.size() - 1; i >= 0; i--)
                  setLastIn(paramSparseArray2, op.removed.get(i));  
              setFirstOut(paramSparseArray1, op.fragment);
              op = op.next;
              break;
            case 3:
              setLastIn(paramSparseArray2, op.fragment);
              op = op.next;
              break;
            case 4:
              setLastIn(paramSparseArray2, op.fragment);
              op = op.next;
              break;
            case 5:
              setFirstOut(paramSparseArray1, op.fragment);
              op = op.next;
              break;
            case 6:
              setLastIn(paramSparseArray2, op.fragment);
              op = op.next;
              break;
            case 7:
              setFirstOut(paramSparseArray1, op.fragment);
              op = op.next;
              break;
          } 
          continue;
        } 
        return;
      } 
    } 
  }
  
  public int commit() {
    return commitInternal(false);
  }
  
  public int commitAllowingStateLoss() {
    return commitInternal(true);
  }
  
  int commitInternal(boolean paramBoolean) {
    if (this.mCommitted)
      throw new IllegalStateException("commit already called"); 
    if (FragmentManagerImpl.DEBUG) {
      Log.v("FragmentManager", "Commit: " + this);
      dump("  ", null, new PrintWriter((Writer)new LogWriter("FragmentManager")), null);
    } 
    this.mCommitted = true;
    if (this.mAddToBackStack) {
      this.mIndex = this.mManager.allocBackStackIndex(this);
      this.mManager.enqueueAction(this, paramBoolean);
      return this.mIndex;
    } 
    this.mIndex = -1;
    this.mManager.enqueueAction(this, paramBoolean);
    return this.mIndex;
  }
  
  public FragmentTransaction detach(Fragment paramFragment) {
    Op op = new Op();
    op.cmd = 6;
    op.fragment = paramFragment;
    addOp(op);
    return this;
  }
  
  public FragmentTransaction disallowAddToBackStack() {
    if (this.mAddToBackStack)
      throw new IllegalStateException("This transaction is already being added to the back stack"); 
    this.mAllowAddToBackStack = false;
    return this;
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
    dump(paramString, paramPrintWriter, true);
  }
  
  public void dump(String paramString, PrintWriter paramPrintWriter, boolean paramBoolean) {
    // Byte code:
    //   0: iload_3
    //   1: ifeq -> 316
    //   4: aload_2
    //   5: aload_1
    //   6: invokevirtual print : (Ljava/lang/String;)V
    //   9: aload_2
    //   10: ldc_w 'mName='
    //   13: invokevirtual print : (Ljava/lang/String;)V
    //   16: aload_2
    //   17: aload_0
    //   18: getfield mName : Ljava/lang/String;
    //   21: invokevirtual print : (Ljava/lang/String;)V
    //   24: aload_2
    //   25: ldc_w ' mIndex='
    //   28: invokevirtual print : (Ljava/lang/String;)V
    //   31: aload_2
    //   32: aload_0
    //   33: getfield mIndex : I
    //   36: invokevirtual print : (I)V
    //   39: aload_2
    //   40: ldc_w ' mCommitted='
    //   43: invokevirtual print : (Ljava/lang/String;)V
    //   46: aload_2
    //   47: aload_0
    //   48: getfield mCommitted : Z
    //   51: invokevirtual println : (Z)V
    //   54: aload_0
    //   55: getfield mTransition : I
    //   58: ifeq -> 102
    //   61: aload_2
    //   62: aload_1
    //   63: invokevirtual print : (Ljava/lang/String;)V
    //   66: aload_2
    //   67: ldc_w 'mTransition=#'
    //   70: invokevirtual print : (Ljava/lang/String;)V
    //   73: aload_2
    //   74: aload_0
    //   75: getfield mTransition : I
    //   78: invokestatic toHexString : (I)Ljava/lang/String;
    //   81: invokevirtual print : (Ljava/lang/String;)V
    //   84: aload_2
    //   85: ldc_w ' mTransitionStyle=#'
    //   88: invokevirtual print : (Ljava/lang/String;)V
    //   91: aload_2
    //   92: aload_0
    //   93: getfield mTransitionStyle : I
    //   96: invokestatic toHexString : (I)Ljava/lang/String;
    //   99: invokevirtual println : (Ljava/lang/String;)V
    //   102: aload_0
    //   103: getfield mEnterAnim : I
    //   106: ifne -> 116
    //   109: aload_0
    //   110: getfield mExitAnim : I
    //   113: ifeq -> 157
    //   116: aload_2
    //   117: aload_1
    //   118: invokevirtual print : (Ljava/lang/String;)V
    //   121: aload_2
    //   122: ldc_w 'mEnterAnim=#'
    //   125: invokevirtual print : (Ljava/lang/String;)V
    //   128: aload_2
    //   129: aload_0
    //   130: getfield mEnterAnim : I
    //   133: invokestatic toHexString : (I)Ljava/lang/String;
    //   136: invokevirtual print : (Ljava/lang/String;)V
    //   139: aload_2
    //   140: ldc_w ' mExitAnim=#'
    //   143: invokevirtual print : (Ljava/lang/String;)V
    //   146: aload_2
    //   147: aload_0
    //   148: getfield mExitAnim : I
    //   151: invokestatic toHexString : (I)Ljava/lang/String;
    //   154: invokevirtual println : (Ljava/lang/String;)V
    //   157: aload_0
    //   158: getfield mPopEnterAnim : I
    //   161: ifne -> 171
    //   164: aload_0
    //   165: getfield mPopExitAnim : I
    //   168: ifeq -> 212
    //   171: aload_2
    //   172: aload_1
    //   173: invokevirtual print : (Ljava/lang/String;)V
    //   176: aload_2
    //   177: ldc_w 'mPopEnterAnim=#'
    //   180: invokevirtual print : (Ljava/lang/String;)V
    //   183: aload_2
    //   184: aload_0
    //   185: getfield mPopEnterAnim : I
    //   188: invokestatic toHexString : (I)Ljava/lang/String;
    //   191: invokevirtual print : (Ljava/lang/String;)V
    //   194: aload_2
    //   195: ldc_w ' mPopExitAnim=#'
    //   198: invokevirtual print : (Ljava/lang/String;)V
    //   201: aload_2
    //   202: aload_0
    //   203: getfield mPopExitAnim : I
    //   206: invokestatic toHexString : (I)Ljava/lang/String;
    //   209: invokevirtual println : (Ljava/lang/String;)V
    //   212: aload_0
    //   213: getfield mBreadCrumbTitleRes : I
    //   216: ifne -> 226
    //   219: aload_0
    //   220: getfield mBreadCrumbTitleText : Ljava/lang/CharSequence;
    //   223: ifnull -> 264
    //   226: aload_2
    //   227: aload_1
    //   228: invokevirtual print : (Ljava/lang/String;)V
    //   231: aload_2
    //   232: ldc_w 'mBreadCrumbTitleRes=#'
    //   235: invokevirtual print : (Ljava/lang/String;)V
    //   238: aload_2
    //   239: aload_0
    //   240: getfield mBreadCrumbTitleRes : I
    //   243: invokestatic toHexString : (I)Ljava/lang/String;
    //   246: invokevirtual print : (Ljava/lang/String;)V
    //   249: aload_2
    //   250: ldc_w ' mBreadCrumbTitleText='
    //   253: invokevirtual print : (Ljava/lang/String;)V
    //   256: aload_2
    //   257: aload_0
    //   258: getfield mBreadCrumbTitleText : Ljava/lang/CharSequence;
    //   261: invokevirtual println : (Ljava/lang/Object;)V
    //   264: aload_0
    //   265: getfield mBreadCrumbShortTitleRes : I
    //   268: ifne -> 278
    //   271: aload_0
    //   272: getfield mBreadCrumbShortTitleText : Ljava/lang/CharSequence;
    //   275: ifnull -> 316
    //   278: aload_2
    //   279: aload_1
    //   280: invokevirtual print : (Ljava/lang/String;)V
    //   283: aload_2
    //   284: ldc_w 'mBreadCrumbShortTitleRes=#'
    //   287: invokevirtual print : (Ljava/lang/String;)V
    //   290: aload_2
    //   291: aload_0
    //   292: getfield mBreadCrumbShortTitleRes : I
    //   295: invokestatic toHexString : (I)Ljava/lang/String;
    //   298: invokevirtual print : (Ljava/lang/String;)V
    //   301: aload_2
    //   302: ldc_w ' mBreadCrumbShortTitleText='
    //   305: invokevirtual print : (Ljava/lang/String;)V
    //   308: aload_2
    //   309: aload_0
    //   310: getfield mBreadCrumbShortTitleText : Ljava/lang/CharSequence;
    //   313: invokevirtual println : (Ljava/lang/Object;)V
    //   316: aload_0
    //   317: getfield mHead : Landroid/support/v4/app/BackStackRecord$Op;
    //   320: ifnull -> 823
    //   323: aload_2
    //   324: aload_1
    //   325: invokevirtual print : (Ljava/lang/String;)V
    //   328: aload_2
    //   329: ldc_w 'Operations:'
    //   332: invokevirtual println : (Ljava/lang/String;)V
    //   335: new java/lang/StringBuilder
    //   338: dup
    //   339: invokespecial <init> : ()V
    //   342: aload_1
    //   343: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   346: ldc_w '    '
    //   349: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   352: invokevirtual toString : ()Ljava/lang/String;
    //   355: astore #8
    //   357: aload_0
    //   358: getfield mHead : Landroid/support/v4/app/BackStackRecord$Op;
    //   361: astore #7
    //   363: iconst_0
    //   364: istore #4
    //   366: aload #7
    //   368: ifnull -> 823
    //   371: aload #7
    //   373: getfield cmd : I
    //   376: tableswitch default -> 424, 0 -> 702, 1 -> 710, 2 -> 718, 3 -> 726, 4 -> 734, 5 -> 742, 6 -> 750, 7 -> 758
    //   424: new java/lang/StringBuilder
    //   427: dup
    //   428: invokespecial <init> : ()V
    //   431: ldc_w 'cmd='
    //   434: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   437: aload #7
    //   439: getfield cmd : I
    //   442: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   445: invokevirtual toString : ()Ljava/lang/String;
    //   448: astore #6
    //   450: aload_2
    //   451: aload_1
    //   452: invokevirtual print : (Ljava/lang/String;)V
    //   455: aload_2
    //   456: ldc_w '  Op #'
    //   459: invokevirtual print : (Ljava/lang/String;)V
    //   462: aload_2
    //   463: iload #4
    //   465: invokevirtual print : (I)V
    //   468: aload_2
    //   469: ldc_w ': '
    //   472: invokevirtual print : (Ljava/lang/String;)V
    //   475: aload_2
    //   476: aload #6
    //   478: invokevirtual print : (Ljava/lang/String;)V
    //   481: aload_2
    //   482: ldc_w ' '
    //   485: invokevirtual print : (Ljava/lang/String;)V
    //   488: aload_2
    //   489: aload #7
    //   491: getfield fragment : Landroid/support/v4/app/Fragment;
    //   494: invokevirtual println : (Ljava/lang/Object;)V
    //   497: iload_3
    //   498: ifeq -> 619
    //   501: aload #7
    //   503: getfield enterAnim : I
    //   506: ifne -> 517
    //   509: aload #7
    //   511: getfield exitAnim : I
    //   514: ifeq -> 560
    //   517: aload_2
    //   518: aload_1
    //   519: invokevirtual print : (Ljava/lang/String;)V
    //   522: aload_2
    //   523: ldc_w 'enterAnim=#'
    //   526: invokevirtual print : (Ljava/lang/String;)V
    //   529: aload_2
    //   530: aload #7
    //   532: getfield enterAnim : I
    //   535: invokestatic toHexString : (I)Ljava/lang/String;
    //   538: invokevirtual print : (Ljava/lang/String;)V
    //   541: aload_2
    //   542: ldc_w ' exitAnim=#'
    //   545: invokevirtual print : (Ljava/lang/String;)V
    //   548: aload_2
    //   549: aload #7
    //   551: getfield exitAnim : I
    //   554: invokestatic toHexString : (I)Ljava/lang/String;
    //   557: invokevirtual println : (Ljava/lang/String;)V
    //   560: aload #7
    //   562: getfield popEnterAnim : I
    //   565: ifne -> 576
    //   568: aload #7
    //   570: getfield popExitAnim : I
    //   573: ifeq -> 619
    //   576: aload_2
    //   577: aload_1
    //   578: invokevirtual print : (Ljava/lang/String;)V
    //   581: aload_2
    //   582: ldc_w 'popEnterAnim=#'
    //   585: invokevirtual print : (Ljava/lang/String;)V
    //   588: aload_2
    //   589: aload #7
    //   591: getfield popEnterAnim : I
    //   594: invokestatic toHexString : (I)Ljava/lang/String;
    //   597: invokevirtual print : (Ljava/lang/String;)V
    //   600: aload_2
    //   601: ldc_w ' popExitAnim=#'
    //   604: invokevirtual print : (Ljava/lang/String;)V
    //   607: aload_2
    //   608: aload #7
    //   610: getfield popExitAnim : I
    //   613: invokestatic toHexString : (I)Ljava/lang/String;
    //   616: invokevirtual println : (Ljava/lang/String;)V
    //   619: aload #7
    //   621: getfield removed : Ljava/util/ArrayList;
    //   624: ifnull -> 807
    //   627: aload #7
    //   629: getfield removed : Ljava/util/ArrayList;
    //   632: invokevirtual size : ()I
    //   635: ifle -> 807
    //   638: iconst_0
    //   639: istore #5
    //   641: iload #5
    //   643: aload #7
    //   645: getfield removed : Ljava/util/ArrayList;
    //   648: invokevirtual size : ()I
    //   651: if_icmpge -> 807
    //   654: aload_2
    //   655: aload #8
    //   657: invokevirtual print : (Ljava/lang/String;)V
    //   660: aload #7
    //   662: getfield removed : Ljava/util/ArrayList;
    //   665: invokevirtual size : ()I
    //   668: iconst_1
    //   669: if_icmpne -> 766
    //   672: aload_2
    //   673: ldc_w 'Removed: '
    //   676: invokevirtual print : (Ljava/lang/String;)V
    //   679: aload_2
    //   680: aload #7
    //   682: getfield removed : Ljava/util/ArrayList;
    //   685: iload #5
    //   687: invokevirtual get : (I)Ljava/lang/Object;
    //   690: invokevirtual println : (Ljava/lang/Object;)V
    //   693: iload #5
    //   695: iconst_1
    //   696: iadd
    //   697: istore #5
    //   699: goto -> 641
    //   702: ldc_w 'NULL'
    //   705: astore #6
    //   707: goto -> 450
    //   710: ldc_w 'ADD'
    //   713: astore #6
    //   715: goto -> 450
    //   718: ldc_w 'REPLACE'
    //   721: astore #6
    //   723: goto -> 450
    //   726: ldc_w 'REMOVE'
    //   729: astore #6
    //   731: goto -> 450
    //   734: ldc_w 'HIDE'
    //   737: astore #6
    //   739: goto -> 450
    //   742: ldc_w 'SHOW'
    //   745: astore #6
    //   747: goto -> 450
    //   750: ldc_w 'DETACH'
    //   753: astore #6
    //   755: goto -> 450
    //   758: ldc_w 'ATTACH'
    //   761: astore #6
    //   763: goto -> 450
    //   766: iload #5
    //   768: ifne -> 778
    //   771: aload_2
    //   772: ldc_w 'Removed:'
    //   775: invokevirtual println : (Ljava/lang/String;)V
    //   778: aload_2
    //   779: aload #8
    //   781: invokevirtual print : (Ljava/lang/String;)V
    //   784: aload_2
    //   785: ldc_w '  #'
    //   788: invokevirtual print : (Ljava/lang/String;)V
    //   791: aload_2
    //   792: iload #5
    //   794: invokevirtual print : (I)V
    //   797: aload_2
    //   798: ldc_w ': '
    //   801: invokevirtual print : (Ljava/lang/String;)V
    //   804: goto -> 679
    //   807: aload #7
    //   809: getfield next : Landroid/support/v4/app/BackStackRecord$Op;
    //   812: astore #7
    //   814: iload #4
    //   816: iconst_1
    //   817: iadd
    //   818: istore #4
    //   820: goto -> 366
    //   823: return
  }
  
  public CharSequence getBreadCrumbShortTitle() {
    return (this.mBreadCrumbShortTitleRes != 0) ? this.mManager.mHost.getContext().getText(this.mBreadCrumbShortTitleRes) : this.mBreadCrumbShortTitleText;
  }
  
  public int getBreadCrumbShortTitleRes() {
    return this.mBreadCrumbShortTitleRes;
  }
  
  public CharSequence getBreadCrumbTitle() {
    return (this.mBreadCrumbTitleRes != 0) ? this.mManager.mHost.getContext().getText(this.mBreadCrumbTitleRes) : this.mBreadCrumbTitleText;
  }
  
  public int getBreadCrumbTitleRes() {
    return this.mBreadCrumbTitleRes;
  }
  
  public int getId() {
    return this.mIndex;
  }
  
  public String getName() {
    return this.mName;
  }
  
  public int getTransition() {
    return this.mTransition;
  }
  
  public int getTransitionStyle() {
    return this.mTransitionStyle;
  }
  
  public FragmentTransaction hide(Fragment paramFragment) {
    Op op = new Op();
    op.cmd = 4;
    op.fragment = paramFragment;
    addOp(op);
    return this;
  }
  
  public boolean isAddToBackStackAllowed() {
    return this.mAllowAddToBackStack;
  }
  
  public boolean isEmpty() {
    return (this.mNumOp == 0);
  }
  
  public TransitionState popFromBackStack(boolean paramBoolean, TransitionState paramTransitionState, SparseArray<Fragment> paramSparseArray1, SparseArray<Fragment> paramSparseArray2) {
    // Byte code:
    //   0: getstatic android/support/v4/app/FragmentManagerImpl.DEBUG : Z
    //   3: ifeq -> 57
    //   6: ldc 'FragmentManager'
    //   8: new java/lang/StringBuilder
    //   11: dup
    //   12: invokespecial <init> : ()V
    //   15: ldc_w 'popFromBackStack: '
    //   18: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   21: aload_0
    //   22: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   25: invokevirtual toString : ()Ljava/lang/String;
    //   28: invokestatic v : (Ljava/lang/String;Ljava/lang/String;)I
    //   31: pop
    //   32: aload_0
    //   33: ldc_w '  '
    //   36: aconst_null
    //   37: new java/io/PrintWriter
    //   40: dup
    //   41: new android/support/v4/util/LogWriter
    //   44: dup
    //   45: ldc 'FragmentManager'
    //   47: invokespecial <init> : (Ljava/lang/String;)V
    //   50: invokespecial <init> : (Ljava/io/Writer;)V
    //   53: aconst_null
    //   54: invokevirtual dump : (Ljava/lang/String;Ljava/io/FileDescriptor;Ljava/io/PrintWriter;[Ljava/lang/String;)V
    //   57: aload_2
    //   58: astore #9
    //   60: getstatic android/support/v4/app/BackStackRecord.SUPPORTS_TRANSITIONS : Z
    //   63: ifeq -> 98
    //   66: aload_2
    //   67: ifnonnull -> 223
    //   70: aload_3
    //   71: invokevirtual size : ()I
    //   74: ifne -> 88
    //   77: aload_2
    //   78: astore #9
    //   80: aload #4
    //   82: invokevirtual size : ()I
    //   85: ifeq -> 98
    //   88: aload_0
    //   89: aload_3
    //   90: aload #4
    //   92: iconst_1
    //   93: invokespecial beginTransition : (Landroid/util/SparseArray;Landroid/util/SparseArray;Z)Landroid/support/v4/app/BackStackRecord$TransitionState;
    //   96: astore #9
    //   98: aload_0
    //   99: iconst_m1
    //   100: invokevirtual bumpBackStackNesting : (I)V
    //   103: aload #9
    //   105: ifnull -> 248
    //   108: iconst_0
    //   109: istore #5
    //   111: aload #9
    //   113: ifnull -> 257
    //   116: iconst_0
    //   117: istore #6
    //   119: aload_0
    //   120: getfield mTail : Landroid/support/v4/app/BackStackRecord$Op;
    //   123: astore_2
    //   124: aload_2
    //   125: ifnull -> 546
    //   128: aload #9
    //   130: ifnull -> 266
    //   133: iconst_0
    //   134: istore #7
    //   136: aload #9
    //   138: ifnull -> 275
    //   141: iconst_0
    //   142: istore #8
    //   144: aload_2
    //   145: getfield cmd : I
    //   148: tableswitch default -> 192, 1 -> 284, 2 -> 318, 3 -> 407, 4 -> 430, 5 -> 459, 6 -> 488, 7 -> 517
    //   192: new java/lang/IllegalArgumentException
    //   195: dup
    //   196: new java/lang/StringBuilder
    //   199: dup
    //   200: invokespecial <init> : ()V
    //   203: ldc_w 'Unknown cmd: '
    //   206: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   209: aload_2
    //   210: getfield cmd : I
    //   213: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   216: invokevirtual toString : ()Ljava/lang/String;
    //   219: invokespecial <init> : (Ljava/lang/String;)V
    //   222: athrow
    //   223: aload_2
    //   224: astore #9
    //   226: iload_1
    //   227: ifne -> 98
    //   230: aload_2
    //   231: aload_0
    //   232: getfield mSharedElementTargetNames : Ljava/util/ArrayList;
    //   235: aload_0
    //   236: getfield mSharedElementSourceNames : Ljava/util/ArrayList;
    //   239: invokestatic setNameOverrides : (Landroid/support/v4/app/BackStackRecord$TransitionState;Ljava/util/ArrayList;Ljava/util/ArrayList;)V
    //   242: aload_2
    //   243: astore #9
    //   245: goto -> 98
    //   248: aload_0
    //   249: getfield mTransitionStyle : I
    //   252: istore #5
    //   254: goto -> 111
    //   257: aload_0
    //   258: getfield mTransition : I
    //   261: istore #6
    //   263: goto -> 119
    //   266: aload_2
    //   267: getfield popEnterAnim : I
    //   270: istore #7
    //   272: goto -> 136
    //   275: aload_2
    //   276: getfield popExitAnim : I
    //   279: istore #8
    //   281: goto -> 144
    //   284: aload_2
    //   285: getfield fragment : Landroid/support/v4/app/Fragment;
    //   288: astore_3
    //   289: aload_3
    //   290: iload #8
    //   292: putfield mNextAnim : I
    //   295: aload_0
    //   296: getfield mManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   299: aload_3
    //   300: iload #6
    //   302: invokestatic reverseTransit : (I)I
    //   305: iload #5
    //   307: invokevirtual removeFragment : (Landroid/support/v4/app/Fragment;II)V
    //   310: aload_2
    //   311: getfield prev : Landroid/support/v4/app/BackStackRecord$Op;
    //   314: astore_2
    //   315: goto -> 124
    //   318: aload_2
    //   319: getfield fragment : Landroid/support/v4/app/Fragment;
    //   322: astore_3
    //   323: aload_3
    //   324: ifnull -> 348
    //   327: aload_3
    //   328: iload #8
    //   330: putfield mNextAnim : I
    //   333: aload_0
    //   334: getfield mManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   337: aload_3
    //   338: iload #6
    //   340: invokestatic reverseTransit : (I)I
    //   343: iload #5
    //   345: invokevirtual removeFragment : (Landroid/support/v4/app/Fragment;II)V
    //   348: aload_2
    //   349: getfield removed : Ljava/util/ArrayList;
    //   352: ifnull -> 310
    //   355: iconst_0
    //   356: istore #8
    //   358: iload #8
    //   360: aload_2
    //   361: getfield removed : Ljava/util/ArrayList;
    //   364: invokevirtual size : ()I
    //   367: if_icmpge -> 310
    //   370: aload_2
    //   371: getfield removed : Ljava/util/ArrayList;
    //   374: iload #8
    //   376: invokevirtual get : (I)Ljava/lang/Object;
    //   379: checkcast android/support/v4/app/Fragment
    //   382: astore_3
    //   383: aload_3
    //   384: iload #7
    //   386: putfield mNextAnim : I
    //   389: aload_0
    //   390: getfield mManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   393: aload_3
    //   394: iconst_0
    //   395: invokevirtual addFragment : (Landroid/support/v4/app/Fragment;Z)V
    //   398: iload #8
    //   400: iconst_1
    //   401: iadd
    //   402: istore #8
    //   404: goto -> 358
    //   407: aload_2
    //   408: getfield fragment : Landroid/support/v4/app/Fragment;
    //   411: astore_3
    //   412: aload_3
    //   413: iload #7
    //   415: putfield mNextAnim : I
    //   418: aload_0
    //   419: getfield mManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   422: aload_3
    //   423: iconst_0
    //   424: invokevirtual addFragment : (Landroid/support/v4/app/Fragment;Z)V
    //   427: goto -> 310
    //   430: aload_2
    //   431: getfield fragment : Landroid/support/v4/app/Fragment;
    //   434: astore_3
    //   435: aload_3
    //   436: iload #7
    //   438: putfield mNextAnim : I
    //   441: aload_0
    //   442: getfield mManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   445: aload_3
    //   446: iload #6
    //   448: invokestatic reverseTransit : (I)I
    //   451: iload #5
    //   453: invokevirtual showFragment : (Landroid/support/v4/app/Fragment;II)V
    //   456: goto -> 310
    //   459: aload_2
    //   460: getfield fragment : Landroid/support/v4/app/Fragment;
    //   463: astore_3
    //   464: aload_3
    //   465: iload #8
    //   467: putfield mNextAnim : I
    //   470: aload_0
    //   471: getfield mManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   474: aload_3
    //   475: iload #6
    //   477: invokestatic reverseTransit : (I)I
    //   480: iload #5
    //   482: invokevirtual hideFragment : (Landroid/support/v4/app/Fragment;II)V
    //   485: goto -> 310
    //   488: aload_2
    //   489: getfield fragment : Landroid/support/v4/app/Fragment;
    //   492: astore_3
    //   493: aload_3
    //   494: iload #7
    //   496: putfield mNextAnim : I
    //   499: aload_0
    //   500: getfield mManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   503: aload_3
    //   504: iload #6
    //   506: invokestatic reverseTransit : (I)I
    //   509: iload #5
    //   511: invokevirtual attachFragment : (Landroid/support/v4/app/Fragment;II)V
    //   514: goto -> 310
    //   517: aload_2
    //   518: getfield fragment : Landroid/support/v4/app/Fragment;
    //   521: astore_3
    //   522: aload_3
    //   523: iload #7
    //   525: putfield mNextAnim : I
    //   528: aload_0
    //   529: getfield mManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   532: aload_3
    //   533: iload #6
    //   535: invokestatic reverseTransit : (I)I
    //   538: iload #5
    //   540: invokevirtual detachFragment : (Landroid/support/v4/app/Fragment;II)V
    //   543: goto -> 310
    //   546: iload_1
    //   547: ifeq -> 575
    //   550: aload_0
    //   551: getfield mManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   554: aload_0
    //   555: getfield mManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   558: getfield mCurState : I
    //   561: iload #6
    //   563: invokestatic reverseTransit : (I)I
    //   566: iload #5
    //   568: iconst_1
    //   569: invokevirtual moveToState : (IIIZ)V
    //   572: aconst_null
    //   573: astore #9
    //   575: aload_0
    //   576: getfield mIndex : I
    //   579: iflt -> 598
    //   582: aload_0
    //   583: getfield mManager : Landroid/support/v4/app/FragmentManagerImpl;
    //   586: aload_0
    //   587: getfield mIndex : I
    //   590: invokevirtual freeBackStackIndex : (I)V
    //   593: aload_0
    //   594: iconst_m1
    //   595: putfield mIndex : I
    //   598: aload #9
    //   600: areturn
  }
  
  public FragmentTransaction remove(Fragment paramFragment) {
    Op op = new Op();
    op.cmd = 3;
    op.fragment = paramFragment;
    addOp(op);
    return this;
  }
  
  public FragmentTransaction replace(int paramInt, Fragment paramFragment) {
    return replace(paramInt, paramFragment, null);
  }
  
  public FragmentTransaction replace(int paramInt, Fragment paramFragment, String paramString) {
    if (paramInt == 0)
      throw new IllegalArgumentException("Must use non-zero containerViewId"); 
    doAddOp(paramInt, paramFragment, paramString, 2);
    return this;
  }
  
  public void run() {
    int i;
    int j;
    TransitionState transitionState;
    if (FragmentManagerImpl.DEBUG)
      Log.v("FragmentManager", "Run: " + this); 
    if (this.mAddToBackStack && this.mIndex < 0)
      throw new IllegalStateException("addToBackStack() called after commit()"); 
    bumpBackStackNesting(1);
    SparseArray<Fragment> sparseArray = null;
    if (SUPPORTS_TRANSITIONS) {
      SparseArray<Fragment> sparseArray1 = new SparseArray();
      sparseArray = new SparseArray();
      calculateFragments(sparseArray1, sparseArray);
      transitionState = beginTransition(sparseArray1, sparseArray, false);
    } 
    if (transitionState != null) {
      i = 0;
    } else {
      i = this.mTransitionStyle;
    } 
    if (transitionState != null) {
      j = 0;
    } else {
      j = this.mTransition;
    } 
    Op op = this.mHead;
    while (op != null) {
      int k;
      int m;
      int n;
      Fragment fragment1;
      Fragment fragment2;
      if (transitionState != null) {
        k = 0;
      } else {
        k = op.enterAnim;
      } 
      if (transitionState != null) {
        m = 0;
      } else {
        m = op.exitAnim;
      } 
      switch (op.cmd) {
        case 1:
          fragment1 = op.fragment;
          fragment1.mNextAnim = k;
          this.mManager.addFragment(fragment1, false);
          op = op.next;
          break;
        case 2:
          fragment1 = op.fragment;
          n = fragment1.mContainerId;
          fragment2 = fragment1;
          if (this.mManager.mAdded != null) {
            int i1 = 0;
            while (true) {
              fragment2 = fragment1;
              if (i1 < this.mManager.mAdded.size()) {
                Fragment fragment = this.mManager.mAdded.get(i1);
                if (FragmentManagerImpl.DEBUG)
                  Log.v("FragmentManager", "OP_REPLACE: adding=" + fragment1 + " old=" + fragment); 
                fragment2 = fragment1;
                if (fragment.mContainerId == n)
                  if (fragment == fragment1) {
                    fragment2 = null;
                    op.fragment = null;
                  } else {
                    if (op.removed == null)
                      op.removed = new ArrayList<Fragment>(); 
                    op.removed.add(fragment);
                    fragment.mNextAnim = m;
                    if (this.mAddToBackStack) {
                      fragment.mBackStackNesting++;
                      if (FragmentManagerImpl.DEBUG)
                        Log.v("FragmentManager", "Bump nesting of " + fragment + " to " + fragment.mBackStackNesting); 
                    } 
                    this.mManager.removeFragment(fragment, j, i);
                    fragment2 = fragment1;
                  }  
                i1++;
                fragment1 = fragment2;
                continue;
              } 
              break;
            } 
          } 
          if (fragment2 != null) {
            fragment2.mNextAnim = k;
            this.mManager.addFragment(fragment2, false);
          } 
          op = op.next;
          break;
        case 3:
          fragment1 = op.fragment;
          fragment1.mNextAnim = m;
          this.mManager.removeFragment(fragment1, j, i);
          op = op.next;
          break;
        case 4:
          fragment1 = op.fragment;
          fragment1.mNextAnim = m;
          this.mManager.hideFragment(fragment1, j, i);
          op = op.next;
          break;
        case 5:
          fragment1 = op.fragment;
          fragment1.mNextAnim = k;
          this.mManager.showFragment(fragment1, j, i);
          op = op.next;
          break;
        case 6:
          fragment1 = op.fragment;
          fragment1.mNextAnim = m;
          this.mManager.detachFragment(fragment1, j, i);
          op = op.next;
          break;
        case 7:
          fragment1 = op.fragment;
          fragment1.mNextAnim = k;
          this.mManager.attachFragment(fragment1, j, i);
          op = op.next;
          break;
      } 
    } 
    this.mManager.moveToState(this.mManager.mCurState, j, i, true);
    if (this.mAddToBackStack)
      this.mManager.addBackStackState(this); 
  }
  
  public FragmentTransaction setBreadCrumbShortTitle(int paramInt) {
    this.mBreadCrumbShortTitleRes = paramInt;
    this.mBreadCrumbShortTitleText = null;
    return this;
  }
  
  public FragmentTransaction setBreadCrumbShortTitle(CharSequence paramCharSequence) {
    this.mBreadCrumbShortTitleRes = 0;
    this.mBreadCrumbShortTitleText = paramCharSequence;
    return this;
  }
  
  public FragmentTransaction setBreadCrumbTitle(int paramInt) {
    this.mBreadCrumbTitleRes = paramInt;
    this.mBreadCrumbTitleText = null;
    return this;
  }
  
  public FragmentTransaction setBreadCrumbTitle(CharSequence paramCharSequence) {
    this.mBreadCrumbTitleRes = 0;
    this.mBreadCrumbTitleText = paramCharSequence;
    return this;
  }
  
  public FragmentTransaction setCustomAnimations(int paramInt1, int paramInt2) {
    return setCustomAnimations(paramInt1, paramInt2, 0, 0);
  }
  
  public FragmentTransaction setCustomAnimations(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mEnterAnim = paramInt1;
    this.mExitAnim = paramInt2;
    this.mPopEnterAnim = paramInt3;
    this.mPopExitAnim = paramInt4;
    return this;
  }
  
  public FragmentTransaction setTransition(int paramInt) {
    this.mTransition = paramInt;
    return this;
  }
  
  public FragmentTransaction setTransitionStyle(int paramInt) {
    this.mTransitionStyle = paramInt;
    return this;
  }
  
  public FragmentTransaction show(Fragment paramFragment) {
    Op op = new Op();
    op.cmd = 5;
    op.fragment = paramFragment;
    addOp(op);
    return this;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(128);
    stringBuilder.append("BackStackEntry{");
    stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
    if (this.mIndex >= 0) {
      stringBuilder.append(" #");
      stringBuilder.append(this.mIndex);
    } 
    if (this.mName != null) {
      stringBuilder.append(" ");
      stringBuilder.append(this.mName);
    } 
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
  
  static final class Op {
    int cmd;
    
    int enterAnim;
    
    int exitAnim;
    
    Fragment fragment;
    
    Op next;
    
    int popEnterAnim;
    
    int popExitAnim;
    
    Op prev;
    
    ArrayList<Fragment> removed;
  }
  
  public class TransitionState {
    public FragmentTransitionCompat21.EpicenterView enteringEpicenterView = new FragmentTransitionCompat21.EpicenterView();
    
    public ArrayList<View> hiddenFragmentViews = new ArrayList<View>();
    
    public ArrayMap<String, String> nameOverrides = new ArrayMap();
    
    public View nonExistentView;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\app\BackStackRecord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */