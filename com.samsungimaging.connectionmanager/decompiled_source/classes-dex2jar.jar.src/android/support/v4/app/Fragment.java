package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.util.DebugUtils;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.LayoutInflaterCompat;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import java.io.FileDescriptor;
import java.io.PrintWriter;

public class Fragment implements ComponentCallbacks, View.OnCreateContextMenuListener {
  static final int ACTIVITY_CREATED = 2;
  
  static final int CREATED = 1;
  
  static final int INITIALIZING = 0;
  
  static final int RESUMED = 5;
  
  static final int STARTED = 4;
  
  static final int STOPPED = 3;
  
  static final Object USE_DEFAULT_TRANSITION;
  
  private static final SimpleArrayMap<String, Class<?>> sClassMap = new SimpleArrayMap();
  
  boolean mAdded;
  
  Boolean mAllowEnterTransitionOverlap;
  
  Boolean mAllowReturnTransitionOverlap;
  
  View mAnimatingAway;
  
  Bundle mArguments;
  
  int mBackStackNesting;
  
  boolean mCalled;
  
  boolean mCheckedForLoaderManager;
  
  FragmentManagerImpl mChildFragmentManager;
  
  ViewGroup mContainer;
  
  int mContainerId;
  
  boolean mDeferStart;
  
  boolean mDetached;
  
  Object mEnterTransition = null;
  
  SharedElementCallback mEnterTransitionCallback = null;
  
  Object mExitTransition = null;
  
  SharedElementCallback mExitTransitionCallback = null;
  
  int mFragmentId;
  
  FragmentManagerImpl mFragmentManager;
  
  boolean mFromLayout;
  
  boolean mHasMenu;
  
  boolean mHidden;
  
  FragmentHostCallback mHost;
  
  boolean mInLayout;
  
  int mIndex = -1;
  
  View mInnerView;
  
  LoaderManagerImpl mLoaderManager;
  
  boolean mLoadersStarted;
  
  boolean mMenuVisible = true;
  
  int mNextAnim;
  
  Fragment mParentFragment;
  
  Object mReenterTransition = USE_DEFAULT_TRANSITION;
  
  boolean mRemoving;
  
  boolean mRestored;
  
  boolean mResumed;
  
  boolean mRetainInstance;
  
  boolean mRetaining;
  
  Object mReturnTransition = USE_DEFAULT_TRANSITION;
  
  Bundle mSavedFragmentState;
  
  SparseArray<Parcelable> mSavedViewState;
  
  Object mSharedElementEnterTransition = null;
  
  Object mSharedElementReturnTransition = USE_DEFAULT_TRANSITION;
  
  int mState = 0;
  
  int mStateAfterAnimating;
  
  String mTag;
  
  Fragment mTarget;
  
  int mTargetIndex = -1;
  
  int mTargetRequestCode;
  
  boolean mUserVisibleHint = true;
  
  View mView;
  
  String mWho;
  
  static {
    USE_DEFAULT_TRANSITION = new Object();
  }
  
  public static Fragment instantiate(Context paramContext, String paramString) {
    return instantiate(paramContext, paramString, null);
  }
  
  public static Fragment instantiate(Context paramContext, String paramString, @Nullable Bundle paramBundle) {
    try {
      Class<?> clazz2 = (Class)sClassMap.get(paramString);
      Class<?> clazz1 = clazz2;
      if (clazz2 == null) {
        clazz1 = paramContext.getClassLoader().loadClass(paramString);
        sClassMap.put(paramString, clazz1);
      } 
      Fragment fragment = (Fragment)clazz1.newInstance();
      if (paramBundle != null) {
        paramBundle.setClassLoader(fragment.getClass().getClassLoader());
        fragment.mArguments = paramBundle;
      } 
      return fragment;
    } catch (ClassNotFoundException classNotFoundException) {
      throw new InstantiationException("Unable to instantiate fragment " + paramString + ": make sure class name exists, is public, and has an" + " empty constructor that is public", classNotFoundException);
    } catch (InstantiationException instantiationException) {
      throw new InstantiationException("Unable to instantiate fragment " + paramString + ": make sure class name exists, is public, and has an" + " empty constructor that is public", instantiationException);
    } catch (IllegalAccessException illegalAccessException) {
      throw new InstantiationException("Unable to instantiate fragment " + paramString + ": make sure class name exists, is public, and has an" + " empty constructor that is public", illegalAccessException);
    } 
  }
  
  static boolean isSupportFragmentClass(Context paramContext, String paramString) {
    try {
      Class<?> clazz2 = (Class)sClassMap.get(paramString);
      Class<?> clazz1 = clazz2;
      if (clazz2 == null) {
        clazz1 = paramContext.getClassLoader().loadClass(paramString);
        sClassMap.put(paramString, clazz1);
      } 
      return Fragment.class.isAssignableFrom(clazz1);
    } catch (ClassNotFoundException classNotFoundException) {
      return false;
    } 
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mFragmentId=#");
    paramPrintWriter.print(Integer.toHexString(this.mFragmentId));
    paramPrintWriter.print(" mContainerId=#");
    paramPrintWriter.print(Integer.toHexString(this.mContainerId));
    paramPrintWriter.print(" mTag=");
    paramPrintWriter.println(this.mTag);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mState=");
    paramPrintWriter.print(this.mState);
    paramPrintWriter.print(" mIndex=");
    paramPrintWriter.print(this.mIndex);
    paramPrintWriter.print(" mWho=");
    paramPrintWriter.print(this.mWho);
    paramPrintWriter.print(" mBackStackNesting=");
    paramPrintWriter.println(this.mBackStackNesting);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mAdded=");
    paramPrintWriter.print(this.mAdded);
    paramPrintWriter.print(" mRemoving=");
    paramPrintWriter.print(this.mRemoving);
    paramPrintWriter.print(" mResumed=");
    paramPrintWriter.print(this.mResumed);
    paramPrintWriter.print(" mFromLayout=");
    paramPrintWriter.print(this.mFromLayout);
    paramPrintWriter.print(" mInLayout=");
    paramPrintWriter.println(this.mInLayout);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mHidden=");
    paramPrintWriter.print(this.mHidden);
    paramPrintWriter.print(" mDetached=");
    paramPrintWriter.print(this.mDetached);
    paramPrintWriter.print(" mMenuVisible=");
    paramPrintWriter.print(this.mMenuVisible);
    paramPrintWriter.print(" mHasMenu=");
    paramPrintWriter.println(this.mHasMenu);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("mRetainInstance=");
    paramPrintWriter.print(this.mRetainInstance);
    paramPrintWriter.print(" mRetaining=");
    paramPrintWriter.print(this.mRetaining);
    paramPrintWriter.print(" mUserVisibleHint=");
    paramPrintWriter.println(this.mUserVisibleHint);
    if (this.mFragmentManager != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mFragmentManager=");
      paramPrintWriter.println(this.mFragmentManager);
    } 
    if (this.mHost != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mHost=");
      paramPrintWriter.println(this.mHost);
    } 
    if (this.mParentFragment != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mParentFragment=");
      paramPrintWriter.println(this.mParentFragment);
    } 
    if (this.mArguments != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mArguments=");
      paramPrintWriter.println(this.mArguments);
    } 
    if (this.mSavedFragmentState != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mSavedFragmentState=");
      paramPrintWriter.println(this.mSavedFragmentState);
    } 
    if (this.mSavedViewState != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mSavedViewState=");
      paramPrintWriter.println(this.mSavedViewState);
    } 
    if (this.mTarget != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mTarget=");
      paramPrintWriter.print(this.mTarget);
      paramPrintWriter.print(" mTargetRequestCode=");
      paramPrintWriter.println(this.mTargetRequestCode);
    } 
    if (this.mNextAnim != 0) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mNextAnim=");
      paramPrintWriter.println(this.mNextAnim);
    } 
    if (this.mContainer != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mContainer=");
      paramPrintWriter.println(this.mContainer);
    } 
    if (this.mView != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mView=");
      paramPrintWriter.println(this.mView);
    } 
    if (this.mInnerView != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mInnerView=");
      paramPrintWriter.println(this.mView);
    } 
    if (this.mAnimatingAway != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mAnimatingAway=");
      paramPrintWriter.println(this.mAnimatingAway);
      paramPrintWriter.print(paramString);
      paramPrintWriter.print("mStateAfterAnimating=");
      paramPrintWriter.println(this.mStateAfterAnimating);
    } 
    if (this.mLoaderManager != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Loader Manager:");
      this.mLoaderManager.dump(paramString + "  ", paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    } 
    if (this.mChildFragmentManager != null) {
      paramPrintWriter.print(paramString);
      paramPrintWriter.println("Child " + this.mChildFragmentManager + ":");
      this.mChildFragmentManager.dump(paramString + "  ", paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    } 
  }
  
  public final boolean equals(Object paramObject) {
    return super.equals(paramObject);
  }
  
  Fragment findFragmentByWho(String paramString) {
    return paramString.equals(this.mWho) ? this : ((this.mChildFragmentManager != null) ? this.mChildFragmentManager.findFragmentByWho(paramString) : null);
  }
  
  public final FragmentActivity getActivity() {
    return (this.mHost == null) ? null : (FragmentActivity)this.mHost.getActivity();
  }
  
  public boolean getAllowEnterTransitionOverlap() {
    return (this.mAllowEnterTransitionOverlap == null) ? true : this.mAllowEnterTransitionOverlap.booleanValue();
  }
  
  public boolean getAllowReturnTransitionOverlap() {
    return (this.mAllowReturnTransitionOverlap == null) ? true : this.mAllowReturnTransitionOverlap.booleanValue();
  }
  
  public final Bundle getArguments() {
    return this.mArguments;
  }
  
  public final FragmentManager getChildFragmentManager() {
    if (this.mChildFragmentManager == null) {
      instantiateChildFragmentManager();
      if (this.mState >= 5) {
        this.mChildFragmentManager.dispatchResume();
        return this.mChildFragmentManager;
      } 
    } else {
      return this.mChildFragmentManager;
    } 
    if (this.mState >= 4) {
      this.mChildFragmentManager.dispatchStart();
      return this.mChildFragmentManager;
    } 
    if (this.mState >= 2) {
      this.mChildFragmentManager.dispatchActivityCreated();
      return this.mChildFragmentManager;
    } 
    if (this.mState >= 1)
      this.mChildFragmentManager.dispatchCreate(); 
    return this.mChildFragmentManager;
  }
  
  public Context getContext() {
    return (this.mHost == null) ? null : this.mHost.getContext();
  }
  
  public Object getEnterTransition() {
    return this.mEnterTransition;
  }
  
  public Object getExitTransition() {
    return this.mExitTransition;
  }
  
  public final FragmentManager getFragmentManager() {
    return this.mFragmentManager;
  }
  
  public final Object getHost() {
    return (this.mHost == null) ? null : this.mHost.onGetHost();
  }
  
  public final int getId() {
    return this.mFragmentId;
  }
  
  public LayoutInflater getLayoutInflater(Bundle paramBundle) {
    LayoutInflater layoutInflater = this.mHost.onGetLayoutInflater();
    getChildFragmentManager();
    LayoutInflaterCompat.setFactory(layoutInflater, this.mChildFragmentManager.getLayoutInflaterFactory());
    return layoutInflater;
  }
  
  public LoaderManager getLoaderManager() {
    if (this.mLoaderManager != null)
      return this.mLoaderManager; 
    if (this.mHost == null)
      throw new IllegalStateException("Fragment " + this + " not attached to Activity"); 
    this.mCheckedForLoaderManager = true;
    this.mLoaderManager = this.mHost.getLoaderManager(this.mWho, this.mLoadersStarted, true);
    return this.mLoaderManager;
  }
  
  public final Fragment getParentFragment() {
    return this.mParentFragment;
  }
  
  public Object getReenterTransition() {
    return (this.mReenterTransition == USE_DEFAULT_TRANSITION) ? getExitTransition() : this.mReenterTransition;
  }
  
  public final Resources getResources() {
    if (this.mHost == null)
      throw new IllegalStateException("Fragment " + this + " not attached to Activity"); 
    return this.mHost.getContext().getResources();
  }
  
  public final boolean getRetainInstance() {
    return this.mRetainInstance;
  }
  
  public Object getReturnTransition() {
    return (this.mReturnTransition == USE_DEFAULT_TRANSITION) ? getEnterTransition() : this.mReturnTransition;
  }
  
  public Object getSharedElementEnterTransition() {
    return this.mSharedElementEnterTransition;
  }
  
  public Object getSharedElementReturnTransition() {
    return (this.mSharedElementReturnTransition == USE_DEFAULT_TRANSITION) ? getSharedElementEnterTransition() : this.mSharedElementReturnTransition;
  }
  
  public final String getString(@StringRes int paramInt) {
    return getResources().getString(paramInt);
  }
  
  public final String getString(@StringRes int paramInt, Object... paramVarArgs) {
    return getResources().getString(paramInt, paramVarArgs);
  }
  
  public final String getTag() {
    return this.mTag;
  }
  
  public final Fragment getTargetFragment() {
    return this.mTarget;
  }
  
  public final int getTargetRequestCode() {
    return this.mTargetRequestCode;
  }
  
  public final CharSequence getText(@StringRes int paramInt) {
    return getResources().getText(paramInt);
  }
  
  public boolean getUserVisibleHint() {
    return this.mUserVisibleHint;
  }
  
  @Nullable
  public View getView() {
    return this.mView;
  }
  
  public final boolean hasOptionsMenu() {
    return this.mHasMenu;
  }
  
  public final int hashCode() {
    return super.hashCode();
  }
  
  void initState() {
    this.mIndex = -1;
    this.mWho = null;
    this.mAdded = false;
    this.mRemoving = false;
    this.mResumed = false;
    this.mFromLayout = false;
    this.mInLayout = false;
    this.mRestored = false;
    this.mBackStackNesting = 0;
    this.mFragmentManager = null;
    this.mChildFragmentManager = null;
    this.mHost = null;
    this.mFragmentId = 0;
    this.mContainerId = 0;
    this.mTag = null;
    this.mHidden = false;
    this.mDetached = false;
    this.mRetaining = false;
    this.mLoaderManager = null;
    this.mLoadersStarted = false;
    this.mCheckedForLoaderManager = false;
  }
  
  void instantiateChildFragmentManager() {
    this.mChildFragmentManager = new FragmentManagerImpl();
    this.mChildFragmentManager.attachController(this.mHost, new FragmentContainer() {
          @Nullable
          public View onFindViewById(int param1Int) {
            if (Fragment.this.mView == null)
              throw new IllegalStateException("Fragment does not have a view"); 
            return Fragment.this.mView.findViewById(param1Int);
          }
          
          public boolean onHasView() {
            return (Fragment.this.mView != null);
          }
        },  this);
  }
  
  public final boolean isAdded() {
    return (this.mHost != null && this.mAdded);
  }
  
  public final boolean isDetached() {
    return this.mDetached;
  }
  
  public final boolean isHidden() {
    return this.mHidden;
  }
  
  final boolean isInBackStack() {
    return (this.mBackStackNesting > 0);
  }
  
  public final boolean isInLayout() {
    return this.mInLayout;
  }
  
  public final boolean isMenuVisible() {
    return this.mMenuVisible;
  }
  
  public final boolean isRemoving() {
    return this.mRemoving;
  }
  
  public final boolean isResumed() {
    return this.mResumed;
  }
  
  public final boolean isVisible() {
    return (isAdded() && !isHidden() && this.mView != null && this.mView.getWindowToken() != null && this.mView.getVisibility() == 0);
  }
  
  public void onActivityCreated(@Nullable Bundle paramBundle) {
    this.mCalled = true;
  }
  
  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {}
  
  @Deprecated
  public void onAttach(Activity paramActivity) {
    this.mCalled = true;
  }
  
  public void onAttach(Context paramContext) {
    Activity activity;
    this.mCalled = true;
    if (this.mHost == null) {
      paramContext = null;
    } else {
      activity = this.mHost.getActivity();
    } 
    if (activity != null) {
      this.mCalled = false;
      onAttach(activity);
    } 
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    this.mCalled = true;
  }
  
  public boolean onContextItemSelected(MenuItem paramMenuItem) {
    return false;
  }
  
  public void onCreate(@Nullable Bundle paramBundle) {
    this.mCalled = true;
  }
  
  public Animation onCreateAnimation(int paramInt1, boolean paramBoolean, int paramInt2) {
    return null;
  }
  
  public void onCreateContextMenu(ContextMenu paramContextMenu, View paramView, ContextMenu.ContextMenuInfo paramContextMenuInfo) {
    getActivity().onCreateContextMenu(paramContextMenu, paramView, paramContextMenuInfo);
  }
  
  public void onCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater) {}
  
  @Nullable
  public View onCreateView(LayoutInflater paramLayoutInflater, @Nullable ViewGroup paramViewGroup, @Nullable Bundle paramBundle) {
    return null;
  }
  
  public void onDestroy() {
    this.mCalled = true;
    if (!this.mCheckedForLoaderManager) {
      this.mCheckedForLoaderManager = true;
      this.mLoaderManager = this.mHost.getLoaderManager(this.mWho, this.mLoadersStarted, false);
    } 
    if (this.mLoaderManager != null)
      this.mLoaderManager.doDestroy(); 
  }
  
  public void onDestroyOptionsMenu() {}
  
  public void onDestroyView() {
    this.mCalled = true;
  }
  
  public void onDetach() {
    this.mCalled = true;
  }
  
  public void onHiddenChanged(boolean paramBoolean) {}
  
  @Deprecated
  public void onInflate(Activity paramActivity, AttributeSet paramAttributeSet, Bundle paramBundle) {
    this.mCalled = true;
  }
  
  public void onInflate(Context paramContext, AttributeSet paramAttributeSet, Bundle paramBundle) {
    Activity activity;
    this.mCalled = true;
    if (this.mHost == null) {
      paramContext = null;
    } else {
      activity = this.mHost.getActivity();
    } 
    if (activity != null) {
      this.mCalled = false;
      onInflate(activity, paramAttributeSet, paramBundle);
    } 
  }
  
  public void onLowMemory() {
    this.mCalled = true;
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
    return false;
  }
  
  public void onOptionsMenuClosed(Menu paramMenu) {}
  
  public void onPause() {
    this.mCalled = true;
  }
  
  public void onPrepareOptionsMenu(Menu paramMenu) {}
  
  public void onRequestPermissionsResult(int paramInt, @NonNull String[] paramArrayOfString, @NonNull int[] paramArrayOfint) {}
  
  public void onResume() {
    this.mCalled = true;
  }
  
  public void onSaveInstanceState(Bundle paramBundle) {}
  
  public void onStart() {
    this.mCalled = true;
    if (!this.mLoadersStarted) {
      this.mLoadersStarted = true;
      if (!this.mCheckedForLoaderManager) {
        this.mCheckedForLoaderManager = true;
        this.mLoaderManager = this.mHost.getLoaderManager(this.mWho, this.mLoadersStarted, false);
      } 
      if (this.mLoaderManager != null)
        this.mLoaderManager.doStart(); 
    } 
  }
  
  public void onStop() {
    this.mCalled = true;
  }
  
  public void onViewCreated(View paramView, @Nullable Bundle paramBundle) {}
  
  public void onViewStateRestored(@Nullable Bundle paramBundle) {
    this.mCalled = true;
  }
  
  void performActivityCreated(Bundle paramBundle) {
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.noteStateNotSaved(); 
    this.mCalled = false;
    onActivityCreated(paramBundle);
    if (!this.mCalled)
      throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onActivityCreated()"); 
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.dispatchActivityCreated(); 
  }
  
  void performConfigurationChanged(Configuration paramConfiguration) {
    onConfigurationChanged(paramConfiguration);
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.dispatchConfigurationChanged(paramConfiguration); 
  }
  
  boolean performContextItemSelected(MenuItem paramMenuItem) {
    return !(this.mHidden || (!onContextItemSelected(paramMenuItem) && (this.mChildFragmentManager == null || !this.mChildFragmentManager.dispatchContextItemSelected(paramMenuItem))));
  }
  
  void performCreate(Bundle paramBundle) {
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.noteStateNotSaved(); 
    this.mCalled = false;
    onCreate(paramBundle);
    if (!this.mCalled)
      throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onCreate()"); 
    if (paramBundle != null) {
      Parcelable parcelable = paramBundle.getParcelable("android:support:fragments");
      if (parcelable != null) {
        if (this.mChildFragmentManager == null)
          instantiateChildFragmentManager(); 
        this.mChildFragmentManager.restoreAllState(parcelable, null);
        this.mChildFragmentManager.dispatchCreate();
      } 
    } 
  }
  
  boolean performCreateOptionsMenu(Menu paramMenu, MenuInflater paramMenuInflater) {
    boolean bool1 = false;
    boolean bool2 = false;
    if (!this.mHidden) {
      boolean bool = bool2;
      if (this.mHasMenu) {
        bool = bool2;
        if (this.mMenuVisible) {
          bool = true;
          onCreateOptionsMenu(paramMenu, paramMenuInflater);
        } 
      } 
      bool1 = bool;
      if (this.mChildFragmentManager != null)
        bool1 = bool | this.mChildFragmentManager.dispatchCreateOptionsMenu(paramMenu, paramMenuInflater); 
    } 
    return bool1;
  }
  
  View performCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.noteStateNotSaved(); 
    return onCreateView(paramLayoutInflater, paramViewGroup, paramBundle);
  }
  
  void performDestroy() {
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.dispatchDestroy(); 
    this.mCalled = false;
    onDestroy();
    if (!this.mCalled)
      throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onDestroy()"); 
  }
  
  void performDestroyView() {
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.dispatchDestroyView(); 
    this.mCalled = false;
    onDestroyView();
    if (!this.mCalled)
      throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onDestroyView()"); 
    if (this.mLoaderManager != null)
      this.mLoaderManager.doReportNextStart(); 
  }
  
  void performLowMemory() {
    onLowMemory();
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.dispatchLowMemory(); 
  }
  
  boolean performOptionsItemSelected(MenuItem paramMenuItem) {
    return !(this.mHidden || ((!this.mHasMenu || !this.mMenuVisible || !onOptionsItemSelected(paramMenuItem)) && (this.mChildFragmentManager == null || !this.mChildFragmentManager.dispatchOptionsItemSelected(paramMenuItem))));
  }
  
  void performOptionsMenuClosed(Menu paramMenu) {
    if (!this.mHidden) {
      if (this.mHasMenu && this.mMenuVisible)
        onOptionsMenuClosed(paramMenu); 
      if (this.mChildFragmentManager != null)
        this.mChildFragmentManager.dispatchOptionsMenuClosed(paramMenu); 
    } 
  }
  
  void performPause() {
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.dispatchPause(); 
    this.mCalled = false;
    onPause();
    if (!this.mCalled)
      throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onPause()"); 
  }
  
  boolean performPrepareOptionsMenu(Menu paramMenu) {
    boolean bool1 = false;
    boolean bool2 = false;
    if (!this.mHidden) {
      boolean bool = bool2;
      if (this.mHasMenu) {
        bool = bool2;
        if (this.mMenuVisible) {
          bool = true;
          onPrepareOptionsMenu(paramMenu);
        } 
      } 
      bool1 = bool;
      if (this.mChildFragmentManager != null)
        bool1 = bool | this.mChildFragmentManager.dispatchPrepareOptionsMenu(paramMenu); 
    } 
    return bool1;
  }
  
  void performReallyStop() {
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.dispatchReallyStop(); 
    if (this.mLoadersStarted) {
      this.mLoadersStarted = false;
      if (!this.mCheckedForLoaderManager) {
        this.mCheckedForLoaderManager = true;
        this.mLoaderManager = this.mHost.getLoaderManager(this.mWho, this.mLoadersStarted, false);
      } 
      if (this.mLoaderManager != null) {
        if (this.mHost.getRetainLoaders()) {
          this.mLoaderManager.doRetain();
          return;
        } 
      } else {
        return;
      } 
    } else {
      return;
    } 
    this.mLoaderManager.doStop();
  }
  
  void performResume() {
    if (this.mChildFragmentManager != null) {
      this.mChildFragmentManager.noteStateNotSaved();
      this.mChildFragmentManager.execPendingActions();
    } 
    this.mCalled = false;
    onResume();
    if (!this.mCalled)
      throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onResume()"); 
    if (this.mChildFragmentManager != null) {
      this.mChildFragmentManager.dispatchResume();
      this.mChildFragmentManager.execPendingActions();
    } 
  }
  
  void performSaveInstanceState(Bundle paramBundle) {
    onSaveInstanceState(paramBundle);
    if (this.mChildFragmentManager != null) {
      Parcelable parcelable = this.mChildFragmentManager.saveAllState();
      if (parcelable != null)
        paramBundle.putParcelable("android:support:fragments", parcelable); 
    } 
  }
  
  void performStart() {
    if (this.mChildFragmentManager != null) {
      this.mChildFragmentManager.noteStateNotSaved();
      this.mChildFragmentManager.execPendingActions();
    } 
    this.mCalled = false;
    onStart();
    if (!this.mCalled)
      throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onStart()"); 
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.dispatchStart(); 
    if (this.mLoaderManager != null)
      this.mLoaderManager.doReportStart(); 
  }
  
  void performStop() {
    if (this.mChildFragmentManager != null)
      this.mChildFragmentManager.dispatchStop(); 
    this.mCalled = false;
    onStop();
    if (!this.mCalled)
      throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onStop()"); 
  }
  
  public void registerForContextMenu(View paramView) {
    paramView.setOnCreateContextMenuListener(this);
  }
  
  public final void requestPermissions(@NonNull String[] paramArrayOfString, int paramInt) {
    if (this.mHost == null)
      throw new IllegalStateException("Fragment " + this + " not attached to Activity"); 
    this.mHost.onRequestPermissionsFromFragment(this, paramArrayOfString, paramInt);
  }
  
  final void restoreViewState(Bundle paramBundle) {
    if (this.mSavedViewState != null) {
      this.mInnerView.restoreHierarchyState(this.mSavedViewState);
      this.mSavedViewState = null;
    } 
    this.mCalled = false;
    onViewStateRestored(paramBundle);
    if (!this.mCalled)
      throw new SuperNotCalledException("Fragment " + this + " did not call through to super.onViewStateRestored()"); 
  }
  
  public void setAllowEnterTransitionOverlap(boolean paramBoolean) {
    this.mAllowEnterTransitionOverlap = Boolean.valueOf(paramBoolean);
  }
  
  public void setAllowReturnTransitionOverlap(boolean paramBoolean) {
    this.mAllowReturnTransitionOverlap = Boolean.valueOf(paramBoolean);
  }
  
  public void setArguments(Bundle paramBundle) {
    if (this.mIndex >= 0)
      throw new IllegalStateException("Fragment already active"); 
    this.mArguments = paramBundle;
  }
  
  public void setEnterSharedElementCallback(SharedElementCallback paramSharedElementCallback) {
    this.mEnterTransitionCallback = paramSharedElementCallback;
  }
  
  public void setEnterTransition(Object paramObject) {
    this.mEnterTransition = paramObject;
  }
  
  public void setExitSharedElementCallback(SharedElementCallback paramSharedElementCallback) {
    this.mExitTransitionCallback = paramSharedElementCallback;
  }
  
  public void setExitTransition(Object paramObject) {
    this.mExitTransition = paramObject;
  }
  
  public void setHasOptionsMenu(boolean paramBoolean) {
    if (this.mHasMenu != paramBoolean) {
      this.mHasMenu = paramBoolean;
      if (isAdded() && !isHidden())
        this.mHost.onSupportInvalidateOptionsMenu(); 
    } 
  }
  
  final void setIndex(int paramInt, Fragment paramFragment) {
    this.mIndex = paramInt;
    if (paramFragment != null) {
      paramFragment.mWho += ":" + this.mIndex;
      return;
    } 
    this.mWho = "android:fragment:" + this.mIndex;
  }
  
  public void setInitialSavedState(SavedState paramSavedState) {
    if (this.mIndex >= 0)
      throw new IllegalStateException("Fragment already active"); 
    if (paramSavedState != null && paramSavedState.mState != null) {
      Bundle bundle = paramSavedState.mState;
    } else {
      paramSavedState = null;
    } 
    this.mSavedFragmentState = (Bundle)paramSavedState;
  }
  
  public void setMenuVisibility(boolean paramBoolean) {
    if (this.mMenuVisible != paramBoolean) {
      this.mMenuVisible = paramBoolean;
      if (this.mHasMenu && isAdded() && !isHidden())
        this.mHost.onSupportInvalidateOptionsMenu(); 
    } 
  }
  
  public void setReenterTransition(Object paramObject) {
    this.mReenterTransition = paramObject;
  }
  
  public void setRetainInstance(boolean paramBoolean) {
    if (paramBoolean && this.mParentFragment != null)
      throw new IllegalStateException("Can't retain fragements that are nested in other fragments"); 
    this.mRetainInstance = paramBoolean;
  }
  
  public void setReturnTransition(Object paramObject) {
    this.mReturnTransition = paramObject;
  }
  
  public void setSharedElementEnterTransition(Object paramObject) {
    this.mSharedElementEnterTransition = paramObject;
  }
  
  public void setSharedElementReturnTransition(Object paramObject) {
    this.mSharedElementReturnTransition = paramObject;
  }
  
  public void setTargetFragment(Fragment paramFragment, int paramInt) {
    this.mTarget = paramFragment;
    this.mTargetRequestCode = paramInt;
  }
  
  public void setUserVisibleHint(boolean paramBoolean) {
    if (!this.mUserVisibleHint && paramBoolean && this.mState < 4)
      this.mFragmentManager.performPendingDeferredStart(this); 
    this.mUserVisibleHint = paramBoolean;
    if (!paramBoolean) {
      paramBoolean = true;
    } else {
      paramBoolean = false;
    } 
    this.mDeferStart = paramBoolean;
  }
  
  public boolean shouldShowRequestPermissionRationale(@NonNull String paramString) {
    return (this.mHost != null) ? this.mHost.onShouldShowRequestPermissionRationale(paramString) : false;
  }
  
  public void startActivity(Intent paramIntent) {
    if (this.mHost == null)
      throw new IllegalStateException("Fragment " + this + " not attached to Activity"); 
    this.mHost.onStartActivityFromFragment(this, paramIntent, -1);
  }
  
  public void startActivityForResult(Intent paramIntent, int paramInt) {
    if (this.mHost == null)
      throw new IllegalStateException("Fragment " + this + " not attached to Activity"); 
    this.mHost.onStartActivityFromFragment(this, paramIntent, paramInt);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder(128);
    DebugUtils.buildShortClassTag(this, stringBuilder);
    if (this.mIndex >= 0) {
      stringBuilder.append(" #");
      stringBuilder.append(this.mIndex);
    } 
    if (this.mFragmentId != 0) {
      stringBuilder.append(" id=0x");
      stringBuilder.append(Integer.toHexString(this.mFragmentId));
    } 
    if (this.mTag != null) {
      stringBuilder.append(" ");
      stringBuilder.append(this.mTag);
    } 
    stringBuilder.append('}');
    return stringBuilder.toString();
  }
  
  public void unregisterForContextMenu(View paramView) {
    paramView.setOnCreateContextMenuListener(null);
  }
  
  public static class InstantiationException extends RuntimeException {
    public InstantiationException(String param1String, Exception param1Exception) {
      super(param1String, param1Exception);
    }
  }
  
  public static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public Fragment.SavedState createFromParcel(Parcel param2Parcel) {
          return new Fragment.SavedState(param2Parcel, null);
        }
        
        public Fragment.SavedState[] newArray(int param2Int) {
          return new Fragment.SavedState[param2Int];
        }
      };
    
    final Bundle mState;
    
    SavedState(Bundle param1Bundle) {
      this.mState = param1Bundle;
    }
    
    SavedState(Parcel param1Parcel, ClassLoader param1ClassLoader) {
      this.mState = param1Parcel.readBundle();
      if (param1ClassLoader != null && this.mState != null)
        this.mState.setClassLoader(param1ClassLoader); 
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeBundle(this.mState);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public Fragment.SavedState createFromParcel(Parcel param1Parcel) {
      return new Fragment.SavedState(param1Parcel, null);
    }
    
    public Fragment.SavedState[] newArray(int param1Int) {
      return new Fragment.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\app\Fragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */