package android.support.v4.app;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.util.SimpleArrayMap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class FragmentActivity extends BaseFragmentActivityHoneycomb implements ActivityCompat.OnRequestPermissionsResultCallback, ActivityCompatApi23.RequestPermissionsRequestCodeValidator {
  static final String FRAGMENTS_TAG = "android:support:fragments";
  
  private static final int HONEYCOMB = 11;
  
  static final int MSG_REALLY_STOPPED = 1;
  
  static final int MSG_RESUME_PENDING = 2;
  
  private static final String TAG = "FragmentActivity";
  
  boolean mCreated;
  
  final FragmentController mFragments = FragmentController.createController(new HostCallbacks());
  
  final Handler mHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        switch (param1Message.what) {
          default:
            super.handleMessage(param1Message);
            return;
          case 1:
            if (FragmentActivity.this.mStopped) {
              FragmentActivity.this.doReallyStop(false);
              return;
            } 
            return;
          case 2:
            break;
        } 
        FragmentActivity.this.onResumeFragments();
        FragmentActivity.this.mFragments.execPendingActions();
      }
    };
  
  MediaControllerCompat mMediaController;
  
  boolean mOptionsMenuInvalidated;
  
  boolean mReallyStopped;
  
  boolean mRequestedPermissionsFromFragment;
  
  boolean mResumed;
  
  boolean mRetaining;
  
  boolean mStopped;
  
  private void dumpViewHierarchy(String paramString, PrintWriter paramPrintWriter, View paramView) {
    paramPrintWriter.print(paramString);
    if (paramView == null) {
      paramPrintWriter.println("null");
      return;
    } 
    paramPrintWriter.println(viewToString(paramView));
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      int i = viewGroup.getChildCount();
      if (i > 0) {
        paramString = paramString + "  ";
        int j = 0;
        while (true) {
          if (j < i) {
            dumpViewHierarchy(paramString, paramPrintWriter, viewGroup.getChildAt(j));
            j++;
            continue;
          } 
          return;
        } 
      } 
    } 
  }
  
  private void requestPermissionsFromFragment(Fragment paramFragment, String[] paramArrayOfString, int paramInt) {
    if (paramInt == -1) {
      ActivityCompat.requestPermissions(this, paramArrayOfString, paramInt);
      return;
    } 
    if ((paramInt & 0xFFFFFF00) != 0)
      throw new IllegalArgumentException("Can only use lower 8 bits for requestCode"); 
    this.mRequestedPermissionsFromFragment = true;
    ActivityCompat.requestPermissions(this, paramArrayOfString, (paramFragment.mIndex + 1 << 8) + (paramInt & 0xFF));
  }
  
  private static String viewToString(View paramView) {
    // Byte code:
    //   0: bipush #70
    //   2: istore_3
    //   3: bipush #46
    //   5: istore_2
    //   6: new java/lang/StringBuilder
    //   9: dup
    //   10: sipush #128
    //   13: invokespecial <init> : (I)V
    //   16: astore #5
    //   18: aload #5
    //   20: aload_0
    //   21: invokevirtual getClass : ()Ljava/lang/Class;
    //   24: invokevirtual getName : ()Ljava/lang/String;
    //   27: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   30: pop
    //   31: aload #5
    //   33: bipush #123
    //   35: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   38: pop
    //   39: aload #5
    //   41: aload_0
    //   42: invokestatic identityHashCode : (Ljava/lang/Object;)I
    //   45: invokestatic toHexString : (I)Ljava/lang/String;
    //   48: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: pop
    //   52: aload #5
    //   54: bipush #32
    //   56: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   59: pop
    //   60: aload_0
    //   61: invokevirtual getVisibility : ()I
    //   64: lookupswitch default -> 100, 0 -> 523, 4 -> 534, 8 -> 545
    //   100: aload #5
    //   102: bipush #46
    //   104: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   107: pop
    //   108: aload_0
    //   109: invokevirtual isFocusable : ()Z
    //   112: ifeq -> 556
    //   115: bipush #70
    //   117: istore_1
    //   118: aload #5
    //   120: iload_1
    //   121: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   124: pop
    //   125: aload_0
    //   126: invokevirtual isEnabled : ()Z
    //   129: ifeq -> 562
    //   132: bipush #69
    //   134: istore_1
    //   135: aload #5
    //   137: iload_1
    //   138: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   141: pop
    //   142: aload_0
    //   143: invokevirtual willNotDraw : ()Z
    //   146: ifeq -> 568
    //   149: bipush #46
    //   151: istore_1
    //   152: aload #5
    //   154: iload_1
    //   155: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   158: pop
    //   159: aload_0
    //   160: invokevirtual isHorizontalScrollBarEnabled : ()Z
    //   163: ifeq -> 574
    //   166: bipush #72
    //   168: istore_1
    //   169: aload #5
    //   171: iload_1
    //   172: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   175: pop
    //   176: aload_0
    //   177: invokevirtual isVerticalScrollBarEnabled : ()Z
    //   180: ifeq -> 580
    //   183: bipush #86
    //   185: istore_1
    //   186: aload #5
    //   188: iload_1
    //   189: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   192: pop
    //   193: aload_0
    //   194: invokevirtual isClickable : ()Z
    //   197: ifeq -> 586
    //   200: bipush #67
    //   202: istore_1
    //   203: aload #5
    //   205: iload_1
    //   206: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   209: pop
    //   210: aload_0
    //   211: invokevirtual isLongClickable : ()Z
    //   214: ifeq -> 592
    //   217: bipush #76
    //   219: istore_1
    //   220: aload #5
    //   222: iload_1
    //   223: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   226: pop
    //   227: aload #5
    //   229: bipush #32
    //   231: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   234: pop
    //   235: aload_0
    //   236: invokevirtual isFocused : ()Z
    //   239: ifeq -> 598
    //   242: iload_3
    //   243: istore_1
    //   244: aload #5
    //   246: iload_1
    //   247: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   250: pop
    //   251: aload_0
    //   252: invokevirtual isSelected : ()Z
    //   255: ifeq -> 604
    //   258: bipush #83
    //   260: istore_1
    //   261: aload #5
    //   263: iload_1
    //   264: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   267: pop
    //   268: iload_2
    //   269: istore_1
    //   270: aload_0
    //   271: invokevirtual isPressed : ()Z
    //   274: ifeq -> 280
    //   277: bipush #80
    //   279: istore_1
    //   280: aload #5
    //   282: iload_1
    //   283: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   286: pop
    //   287: aload #5
    //   289: bipush #32
    //   291: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   294: pop
    //   295: aload #5
    //   297: aload_0
    //   298: invokevirtual getLeft : ()I
    //   301: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   304: pop
    //   305: aload #5
    //   307: bipush #44
    //   309: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   312: pop
    //   313: aload #5
    //   315: aload_0
    //   316: invokevirtual getTop : ()I
    //   319: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   322: pop
    //   323: aload #5
    //   325: bipush #45
    //   327: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   330: pop
    //   331: aload #5
    //   333: aload_0
    //   334: invokevirtual getRight : ()I
    //   337: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   340: pop
    //   341: aload #5
    //   343: bipush #44
    //   345: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   348: pop
    //   349: aload #5
    //   351: aload_0
    //   352: invokevirtual getBottom : ()I
    //   355: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   358: pop
    //   359: aload_0
    //   360: invokevirtual getId : ()I
    //   363: istore #4
    //   365: iload #4
    //   367: iconst_m1
    //   368: if_icmpeq -> 509
    //   371: aload #5
    //   373: ldc ' #'
    //   375: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   378: pop
    //   379: aload #5
    //   381: iload #4
    //   383: invokestatic toHexString : (I)Ljava/lang/String;
    //   386: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   389: pop
    //   390: aload_0
    //   391: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   394: astore #6
    //   396: iload #4
    //   398: ifeq -> 509
    //   401: aload #6
    //   403: ifnull -> 509
    //   406: ldc -16777216
    //   408: iload #4
    //   410: iand
    //   411: lookupswitch default -> 436, 16777216 -> 616, 2130706432 -> 610
    //   436: aload #6
    //   438: iload #4
    //   440: invokevirtual getResourcePackageName : (I)Ljava/lang/String;
    //   443: astore_0
    //   444: aload #6
    //   446: iload #4
    //   448: invokevirtual getResourceTypeName : (I)Ljava/lang/String;
    //   451: astore #7
    //   453: aload #6
    //   455: iload #4
    //   457: invokevirtual getResourceEntryName : (I)Ljava/lang/String;
    //   460: astore #6
    //   462: aload #5
    //   464: ldc ' '
    //   466: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   469: pop
    //   470: aload #5
    //   472: aload_0
    //   473: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   476: pop
    //   477: aload #5
    //   479: ldc ':'
    //   481: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   484: pop
    //   485: aload #5
    //   487: aload #7
    //   489: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   492: pop
    //   493: aload #5
    //   495: ldc '/'
    //   497: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   500: pop
    //   501: aload #5
    //   503: aload #6
    //   505: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   508: pop
    //   509: aload #5
    //   511: ldc '}'
    //   513: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   516: pop
    //   517: aload #5
    //   519: invokevirtual toString : ()Ljava/lang/String;
    //   522: areturn
    //   523: aload #5
    //   525: bipush #86
    //   527: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   530: pop
    //   531: goto -> 108
    //   534: aload #5
    //   536: bipush #73
    //   538: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   541: pop
    //   542: goto -> 108
    //   545: aload #5
    //   547: bipush #71
    //   549: invokevirtual append : (C)Ljava/lang/StringBuilder;
    //   552: pop
    //   553: goto -> 108
    //   556: bipush #46
    //   558: istore_1
    //   559: goto -> 118
    //   562: bipush #46
    //   564: istore_1
    //   565: goto -> 135
    //   568: bipush #68
    //   570: istore_1
    //   571: goto -> 152
    //   574: bipush #46
    //   576: istore_1
    //   577: goto -> 169
    //   580: bipush #46
    //   582: istore_1
    //   583: goto -> 186
    //   586: bipush #46
    //   588: istore_1
    //   589: goto -> 203
    //   592: bipush #46
    //   594: istore_1
    //   595: goto -> 220
    //   598: bipush #46
    //   600: istore_1
    //   601: goto -> 244
    //   604: bipush #46
    //   606: istore_1
    //   607: goto -> 261
    //   610: ldc 'app'
    //   612: astore_0
    //   613: goto -> 444
    //   616: ldc 'android'
    //   618: astore_0
    //   619: goto -> 444
    //   622: astore_0
    //   623: goto -> 509
    // Exception table:
    //   from	to	target	type
    //   436	444	622	android/content/res/Resources$NotFoundException
    //   444	509	622	android/content/res/Resources$NotFoundException
  }
  
  final View dispatchFragmentsOnCreateView(View paramView, String paramString, Context paramContext, AttributeSet paramAttributeSet) {
    return this.mFragments.onCreateView(paramView, paramString, paramContext, paramAttributeSet);
  }
  
  void doReallyStop(boolean paramBoolean) {
    if (!this.mReallyStopped) {
      this.mReallyStopped = true;
      this.mRetaining = paramBoolean;
      this.mHandler.removeMessages(1);
      onReallyStop();
    } 
  }
  
  public void dump(String paramString, FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {
    if (Build.VERSION.SDK_INT >= 11);
    paramPrintWriter.print(paramString);
    paramPrintWriter.print("Local FragmentActivity ");
    paramPrintWriter.print(Integer.toHexString(System.identityHashCode(this)));
    paramPrintWriter.println(" State:");
    String str = paramString + "  ";
    paramPrintWriter.print(str);
    paramPrintWriter.print("mCreated=");
    paramPrintWriter.print(this.mCreated);
    paramPrintWriter.print("mResumed=");
    paramPrintWriter.print(this.mResumed);
    paramPrintWriter.print(" mStopped=");
    paramPrintWriter.print(this.mStopped);
    paramPrintWriter.print(" mReallyStopped=");
    paramPrintWriter.println(this.mReallyStopped);
    this.mFragments.dumpLoaders(str, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    this.mFragments.getSupportFragmentManager().dump(paramString, paramFileDescriptor, paramPrintWriter, paramArrayOfString);
    paramPrintWriter.print(paramString);
    paramPrintWriter.println("View Hierarchy:");
    dumpViewHierarchy(paramString + "  ", paramPrintWriter, getWindow().getDecorView());
  }
  
  public Object getLastCustomNonConfigurationInstance() {
    NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
    return (nonConfigurationInstances != null) ? nonConfigurationInstances.custom : null;
  }
  
  public FragmentManager getSupportFragmentManager() {
    return this.mFragments.getSupportFragmentManager();
  }
  
  public LoaderManager getSupportLoaderManager() {
    return this.mFragments.getSupportLoaderManager();
  }
  
  public final MediaControllerCompat getSupportMediaController() {
    return this.mMediaController;
  }
  
  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent) {
    this.mFragments.noteStateNotSaved();
    int i = paramInt1 >> 16;
    if (i != 0) {
      i--;
      int j = this.mFragments.getActiveFragmentsCount();
      if (j == 0 || i < 0 || i >= j) {
        Log.w("FragmentActivity", "Activity result fragment index out of range: 0x" + Integer.toHexString(paramInt1));
        return;
      } 
      Fragment fragment = this.mFragments.getActiveFragments(new ArrayList<Fragment>(j)).get(i);
      if (fragment == null) {
        Log.w("FragmentActivity", "Activity result no fragment exists for index: 0x" + Integer.toHexString(paramInt1));
        return;
      } 
      fragment.onActivityResult(0xFFFF & paramInt1, paramInt2, paramIntent);
      return;
    } 
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }
  
  public void onAttachFragment(Fragment paramFragment) {}
  
  public void onBackPressed() {
    if (!this.mFragments.getSupportFragmentManager().popBackStackImmediate())
      supportFinishAfterTransition(); 
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    this.mFragments.dispatchConfigurationChanged(paramConfiguration);
  }
  
  protected void onCreate(@Nullable Bundle paramBundle) {
    Bundle bundle = null;
    this.mFragments.attachHost(null);
    super.onCreate(paramBundle);
    NonConfigurationInstances nonConfigurationInstances = (NonConfigurationInstances)getLastNonConfigurationInstance();
    if (nonConfigurationInstances != null)
      this.mFragments.restoreLoaderNonConfig(nonConfigurationInstances.loaders); 
    if (paramBundle != null) {
      List<Fragment> list;
      Parcelable parcelable = paramBundle.getParcelable("android:support:fragments");
      FragmentController fragmentController = this.mFragments;
      paramBundle = bundle;
      if (nonConfigurationInstances != null)
        list = nonConfigurationInstances.fragments; 
      fragmentController.restoreAllState(parcelable, list);
    } 
    this.mFragments.dispatchCreate();
  }
  
  public boolean onCreatePanelMenu(int paramInt, Menu paramMenu) {
    if (paramInt == 0) {
      boolean bool1 = super.onCreatePanelMenu(paramInt, paramMenu);
      boolean bool2 = this.mFragments.dispatchCreateOptionsMenu(paramMenu, getMenuInflater());
      return (Build.VERSION.SDK_INT >= 11) ? (bool1 | bool2) : true;
    } 
    return super.onCreatePanelMenu(paramInt, paramMenu);
  }
  
  protected void onDestroy() {
    super.onDestroy();
    doReallyStop(false);
    this.mFragments.dispatchDestroy();
    this.mFragments.doLoaderDestroy();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    if (Build.VERSION.SDK_INT < 5 && paramInt == 4 && paramKeyEvent.getRepeatCount() == 0) {
      onBackPressed();
      return true;
    } 
    return super.onKeyDown(paramInt, paramKeyEvent);
  }
  
  public void onLowMemory() {
    super.onLowMemory();
    this.mFragments.dispatchLowMemory();
  }
  
  public boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem) {
    if (super.onMenuItemSelected(paramInt, paramMenuItem))
      return true; 
    switch (paramInt) {
      default:
        return false;
      case 0:
        return this.mFragments.dispatchOptionsItemSelected(paramMenuItem);
      case 6:
        break;
    } 
    return this.mFragments.dispatchContextItemSelected(paramMenuItem);
  }
  
  protected void onNewIntent(Intent paramIntent) {
    super.onNewIntent(paramIntent);
    this.mFragments.noteStateNotSaved();
  }
  
  public void onPanelClosed(int paramInt, Menu paramMenu) {
    switch (paramInt) {
      default:
        super.onPanelClosed(paramInt, paramMenu);
        return;
      case 0:
        break;
    } 
    this.mFragments.dispatchOptionsMenuClosed(paramMenu);
  }
  
  protected void onPause() {
    super.onPause();
    this.mResumed = false;
    if (this.mHandler.hasMessages(2)) {
      this.mHandler.removeMessages(2);
      onResumeFragments();
    } 
    this.mFragments.dispatchPause();
  }
  
  protected void onPostResume() {
    super.onPostResume();
    this.mHandler.removeMessages(2);
    onResumeFragments();
    this.mFragments.execPendingActions();
  }
  
  protected boolean onPrepareOptionsPanel(View paramView, Menu paramMenu) {
    return super.onPreparePanel(0, paramView, paramMenu);
  }
  
  public boolean onPreparePanel(int paramInt, View paramView, Menu paramMenu) {
    if (paramInt == 0 && paramMenu != null) {
      if (this.mOptionsMenuInvalidated) {
        this.mOptionsMenuInvalidated = false;
        paramMenu.clear();
        onCreatePanelMenu(paramInt, paramMenu);
      } 
      return onPrepareOptionsPanel(paramView, paramMenu) | this.mFragments.dispatchPrepareOptionsMenu(paramMenu);
    } 
    return super.onPreparePanel(paramInt, paramView, paramMenu);
  }
  
  void onReallyStop() {
    this.mFragments.doLoaderStop(this.mRetaining);
    this.mFragments.dispatchReallyStop();
  }
  
  public void onRequestPermissionsResult(int paramInt, @NonNull String[] paramArrayOfString, @NonNull int[] paramArrayOfint) {
    int j;
    int i = paramInt >> 8 & 0xFF;
    if (i != 0) {
      i--;
      j = this.mFragments.getActiveFragmentsCount();
      if (j == 0 || i < 0 || i >= j) {
        Log.w("FragmentActivity", "Activity result fragment index out of range: 0x" + Integer.toHexString(paramInt));
        return;
      } 
    } else {
      return;
    } 
    Fragment fragment = this.mFragments.getActiveFragments(new ArrayList<Fragment>(j)).get(i);
    if (fragment == null) {
      Log.w("FragmentActivity", "Activity result no fragment exists for index: 0x" + Integer.toHexString(paramInt));
      return;
    } 
    fragment.onRequestPermissionsResult(paramInt & 0xFF, paramArrayOfString, paramArrayOfint);
  }
  
  protected void onResume() {
    super.onResume();
    this.mHandler.sendEmptyMessage(2);
    this.mResumed = true;
    this.mFragments.execPendingActions();
  }
  
  protected void onResumeFragments() {
    this.mFragments.dispatchResume();
  }
  
  public Object onRetainCustomNonConfigurationInstance() {
    return null;
  }
  
  public final Object onRetainNonConfigurationInstance() {
    if (this.mStopped)
      doReallyStop(true); 
    Object object = onRetainCustomNonConfigurationInstance();
    List<Fragment> list = this.mFragments.retainNonConfig();
    SimpleArrayMap<String, LoaderManager> simpleArrayMap = this.mFragments.retainLoaderNonConfig();
    if (list == null && simpleArrayMap == null && object == null)
      return null; 
    NonConfigurationInstances nonConfigurationInstances = new NonConfigurationInstances();
    nonConfigurationInstances.custom = object;
    nonConfigurationInstances.fragments = list;
    nonConfigurationInstances.loaders = simpleArrayMap;
    return nonConfigurationInstances;
  }
  
  protected void onSaveInstanceState(Bundle paramBundle) {
    super.onSaveInstanceState(paramBundle);
    Parcelable parcelable = this.mFragments.saveAllState();
    if (parcelable != null)
      paramBundle.putParcelable("android:support:fragments", parcelable); 
  }
  
  protected void onStart() {
    super.onStart();
    this.mStopped = false;
    this.mReallyStopped = false;
    this.mHandler.removeMessages(1);
    if (!this.mCreated) {
      this.mCreated = true;
      this.mFragments.dispatchActivityCreated();
    } 
    this.mFragments.noteStateNotSaved();
    this.mFragments.execPendingActions();
    this.mFragments.doLoaderStart();
    this.mFragments.dispatchStart();
    this.mFragments.reportLoaderStart();
  }
  
  public void onStateNotSaved() {
    this.mFragments.noteStateNotSaved();
  }
  
  protected void onStop() {
    super.onStop();
    this.mStopped = true;
    this.mHandler.sendEmptyMessage(1);
    this.mFragments.dispatchStop();
  }
  
  public void setEnterSharedElementCallback(SharedElementCallback paramSharedElementCallback) {
    ActivityCompat.setEnterSharedElementCallback(this, paramSharedElementCallback);
  }
  
  public void setExitSharedElementCallback(SharedElementCallback paramSharedElementCallback) {
    ActivityCompat.setExitSharedElementCallback(this, paramSharedElementCallback);
  }
  
  public final void setSupportMediaController(MediaControllerCompat paramMediaControllerCompat) {
    this.mMediaController = paramMediaControllerCompat;
    if (Build.VERSION.SDK_INT >= 21)
      ActivityCompat21.setMediaController(this, paramMediaControllerCompat.getMediaController()); 
  }
  
  public void startActivityForResult(Intent paramIntent, int paramInt) {
    if (paramInt != -1 && (0xFFFF0000 & paramInt) != 0)
      throw new IllegalArgumentException("Can only use lower 16 bits for requestCode"); 
    super.startActivityForResult(paramIntent, paramInt);
  }
  
  public void startActivityFromFragment(Fragment paramFragment, Intent paramIntent, int paramInt) {
    if (paramInt == -1) {
      super.startActivityForResult(paramIntent, -1);
      return;
    } 
    if ((0xFFFF0000 & paramInt) != 0)
      throw new IllegalArgumentException("Can only use lower 16 bits for requestCode"); 
    super.startActivityForResult(paramIntent, (paramFragment.mIndex + 1 << 16) + (0xFFFF & paramInt));
  }
  
  public void supportFinishAfterTransition() {
    ActivityCompat.finishAfterTransition(this);
  }
  
  public void supportInvalidateOptionsMenu() {
    if (Build.VERSION.SDK_INT >= 11) {
      ActivityCompatHoneycomb.invalidateOptionsMenu(this);
      return;
    } 
    this.mOptionsMenuInvalidated = true;
  }
  
  public void supportPostponeEnterTransition() {
    ActivityCompat.postponeEnterTransition(this);
  }
  
  public void supportStartPostponedEnterTransition() {
    ActivityCompat.startPostponedEnterTransition(this);
  }
  
  public final void validateRequestPermissionsRequestCode(int paramInt) {
    if (this.mRequestedPermissionsFromFragment) {
      this.mRequestedPermissionsFromFragment = false;
      return;
    } 
    if ((paramInt & 0xFFFFFF00) != 0)
      throw new IllegalArgumentException("Can only use lower 8 bits for requestCode"); 
  }
  
  class HostCallbacks extends FragmentHostCallback<FragmentActivity> {
    public HostCallbacks() {
      super(FragmentActivity.this);
    }
    
    public void onAttachFragment(Fragment param1Fragment) {
      FragmentActivity.this.onAttachFragment(param1Fragment);
    }
    
    public void onDump(String param1String, FileDescriptor param1FileDescriptor, PrintWriter param1PrintWriter, String[] param1ArrayOfString) {
      FragmentActivity.this.dump(param1String, param1FileDescriptor, param1PrintWriter, param1ArrayOfString);
    }
    
    @Nullable
    public View onFindViewById(int param1Int) {
      return FragmentActivity.this.findViewById(param1Int);
    }
    
    public FragmentActivity onGetHost() {
      return FragmentActivity.this;
    }
    
    public LayoutInflater onGetLayoutInflater() {
      return FragmentActivity.this.getLayoutInflater().cloneInContext((Context)FragmentActivity.this);
    }
    
    public int onGetWindowAnimations() {
      Window window = FragmentActivity.this.getWindow();
      return (window == null) ? 0 : (window.getAttributes()).windowAnimations;
    }
    
    public boolean onHasView() {
      Window window = FragmentActivity.this.getWindow();
      return (window != null && window.peekDecorView() != null);
    }
    
    public boolean onHasWindowAnimations() {
      return (FragmentActivity.this.getWindow() != null);
    }
    
    public void onRequestPermissionsFromFragment(@NonNull Fragment param1Fragment, @NonNull String[] param1ArrayOfString, int param1Int) {
      FragmentActivity.this.requestPermissionsFromFragment(param1Fragment, param1ArrayOfString, param1Int);
    }
    
    public boolean onShouldSaveFragmentState(Fragment param1Fragment) {
      return !FragmentActivity.this.isFinishing();
    }
    
    public boolean onShouldShowRequestPermissionRationale(@NonNull String param1String) {
      return ActivityCompat.shouldShowRequestPermissionRationale(FragmentActivity.this, param1String);
    }
    
    public void onStartActivityFromFragment(Fragment param1Fragment, Intent param1Intent, int param1Int) {
      FragmentActivity.this.startActivityFromFragment(param1Fragment, param1Intent, param1Int);
    }
    
    public void onSupportInvalidateOptionsMenu() {
      FragmentActivity.this.supportInvalidateOptionsMenu();
    }
  }
  
  static final class NonConfigurationInstances {
    Object custom;
    
    List<Fragment> fragments;
    
    SimpleArrayMap<String, LoaderManager> loaders;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\app\FragmentActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */