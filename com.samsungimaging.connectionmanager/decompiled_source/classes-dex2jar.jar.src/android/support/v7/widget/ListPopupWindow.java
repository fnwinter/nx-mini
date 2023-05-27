package android.support.v7.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.widget.ListViewAutoScrollHelper;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import java.lang.reflect.Method;

public class ListPopupWindow {
  private static final boolean DEBUG = false;
  
  private static final int EXPAND_LIST_TIMEOUT = 250;
  
  public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
  
  public static final int INPUT_METHOD_NEEDED = 1;
  
  public static final int INPUT_METHOD_NOT_NEEDED = 2;
  
  public static final int MATCH_PARENT = -1;
  
  public static final int POSITION_PROMPT_ABOVE = 0;
  
  public static final int POSITION_PROMPT_BELOW = 1;
  
  private static final String TAG = "ListPopupWindow";
  
  public static final int WRAP_CONTENT = -2;
  
  private static Method sClipToWindowEnabledMethod;
  
  private static Method sGetMaxAvailableHeightMethod;
  
  private ListAdapter mAdapter;
  
  private Context mContext;
  
  private boolean mDropDownAlwaysVisible = false;
  
  private View mDropDownAnchorView;
  
  private int mDropDownGravity = 0;
  
  private int mDropDownHeight = -2;
  
  private int mDropDownHorizontalOffset;
  
  private DropDownListView mDropDownList;
  
  private Drawable mDropDownListHighlight;
  
  private int mDropDownVerticalOffset;
  
  private boolean mDropDownVerticalOffsetSet;
  
  private int mDropDownWidth = -2;
  
  private int mDropDownWindowLayoutType = 1002;
  
  private boolean mForceIgnoreOutsideTouch = false;
  
  private final Handler mHandler;
  
  private final ListSelectorHider mHideSelector = new ListSelectorHider();
  
  private AdapterView.OnItemClickListener mItemClickListener;
  
  private AdapterView.OnItemSelectedListener mItemSelectedListener;
  
  private int mLayoutDirection;
  
  int mListItemExpandMaximum = Integer.MAX_VALUE;
  
  private boolean mModal;
  
  private DataSetObserver mObserver;
  
  private PopupWindow mPopup;
  
  private int mPromptPosition = 0;
  
  private View mPromptView;
  
  private final ResizePopupRunnable mResizePopupRunnable = new ResizePopupRunnable();
  
  private final PopupScrollListener mScrollListener = new PopupScrollListener();
  
  private Runnable mShowDropDownRunnable;
  
  private Rect mTempRect = new Rect();
  
  private final PopupTouchInterceptor mTouchInterceptor = new PopupTouchInterceptor();
  
  static {
    try {
      sClipToWindowEnabledMethod = PopupWindow.class.getDeclaredMethod("setClipToScreenEnabled", new Class[] { boolean.class });
      try {
        sGetMaxAvailableHeightMethod = PopupWindow.class.getDeclaredMethod("getMaxAvailableHeight", new Class[] { View.class, int.class, boolean.class });
        return;
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.i("ListPopupWindow", "Could not find method getMaxAvailableHeight(View, int, boolean) on PopupWindow. Oh well.");
      } 
    } catch (NoSuchMethodException noSuchMethodException) {
      Log.i("ListPopupWindow", "Could not find method setClipToScreenEnabled() on PopupWindow. Oh well.");
      try {
        sGetMaxAvailableHeightMethod = PopupWindow.class.getDeclaredMethod("getMaxAvailableHeight", new Class[] { View.class, int.class, boolean.class });
        return;
      } catch (NoSuchMethodException noSuchMethodException1) {
        Log.i("ListPopupWindow", "Could not find method getMaxAvailableHeight(View, int, boolean) on PopupWindow. Oh well.");
      } 
    } 
  }
  
  public ListPopupWindow(Context paramContext) {
    this(paramContext, null, R.attr.listPopupWindowStyle);
  }
  
  public ListPopupWindow(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.listPopupWindowStyle);
  }
  
  public ListPopupWindow(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    this(paramContext, paramAttributeSet, paramInt, 0);
  }
  
  public ListPopupWindow(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    this.mContext = paramContext;
    this.mHandler = new Handler(paramContext.getMainLooper());
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.ListPopupWindow, paramInt1, paramInt2);
    this.mDropDownHorizontalOffset = typedArray.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownHorizontalOffset, 0);
    this.mDropDownVerticalOffset = typedArray.getDimensionPixelOffset(R.styleable.ListPopupWindow_android_dropDownVerticalOffset, 0);
    if (this.mDropDownVerticalOffset != 0)
      this.mDropDownVerticalOffsetSet = true; 
    typedArray.recycle();
    this.mPopup = new AppCompatPopupWindow(paramContext, paramAttributeSet, paramInt1);
    this.mPopup.setInputMethodMode(1);
    this.mLayoutDirection = TextUtilsCompat.getLayoutDirectionFromLocale((this.mContext.getResources().getConfiguration()).locale);
  }
  
  private int buildDropDown() {
    // Byte code:
    //   0: iconst_0
    //   1: istore_2
    //   2: iconst_0
    //   3: istore_1
    //   4: aload_0
    //   5: getfield mDropDownList : Landroid/support/v7/widget/ListPopupWindow$DropDownListView;
    //   8: ifnonnull -> 491
    //   11: aload_0
    //   12: getfield mContext : Landroid/content/Context;
    //   15: astore #9
    //   17: aload_0
    //   18: new android/support/v7/widget/ListPopupWindow$2
    //   21: dup
    //   22: aload_0
    //   23: invokespecial <init> : (Landroid/support/v7/widget/ListPopupWindow;)V
    //   26: putfield mShowDropDownRunnable : Ljava/lang/Runnable;
    //   29: aload_0
    //   30: getfield mModal : Z
    //   33: ifne -> 440
    //   36: iconst_1
    //   37: istore #5
    //   39: aload_0
    //   40: new android/support/v7/widget/ListPopupWindow$DropDownListView
    //   43: dup
    //   44: aload #9
    //   46: iload #5
    //   48: invokespecial <init> : (Landroid/content/Context;Z)V
    //   51: putfield mDropDownList : Landroid/support/v7/widget/ListPopupWindow$DropDownListView;
    //   54: aload_0
    //   55: getfield mDropDownListHighlight : Landroid/graphics/drawable/Drawable;
    //   58: ifnull -> 72
    //   61: aload_0
    //   62: getfield mDropDownList : Landroid/support/v7/widget/ListPopupWindow$DropDownListView;
    //   65: aload_0
    //   66: getfield mDropDownListHighlight : Landroid/graphics/drawable/Drawable;
    //   69: invokevirtual setSelector : (Landroid/graphics/drawable/Drawable;)V
    //   72: aload_0
    //   73: getfield mDropDownList : Landroid/support/v7/widget/ListPopupWindow$DropDownListView;
    //   76: aload_0
    //   77: getfield mAdapter : Landroid/widget/ListAdapter;
    //   80: invokevirtual setAdapter : (Landroid/widget/ListAdapter;)V
    //   83: aload_0
    //   84: getfield mDropDownList : Landroid/support/v7/widget/ListPopupWindow$DropDownListView;
    //   87: aload_0
    //   88: getfield mItemClickListener : Landroid/widget/AdapterView$OnItemClickListener;
    //   91: invokevirtual setOnItemClickListener : (Landroid/widget/AdapterView$OnItemClickListener;)V
    //   94: aload_0
    //   95: getfield mDropDownList : Landroid/support/v7/widget/ListPopupWindow$DropDownListView;
    //   98: iconst_1
    //   99: invokevirtual setFocusable : (Z)V
    //   102: aload_0
    //   103: getfield mDropDownList : Landroid/support/v7/widget/ListPopupWindow$DropDownListView;
    //   106: iconst_1
    //   107: invokevirtual setFocusableInTouchMode : (Z)V
    //   110: aload_0
    //   111: getfield mDropDownList : Landroid/support/v7/widget/ListPopupWindow$DropDownListView;
    //   114: new android/support/v7/widget/ListPopupWindow$3
    //   117: dup
    //   118: aload_0
    //   119: invokespecial <init> : (Landroid/support/v7/widget/ListPopupWindow;)V
    //   122: invokevirtual setOnItemSelectedListener : (Landroid/widget/AdapterView$OnItemSelectedListener;)V
    //   125: aload_0
    //   126: getfield mDropDownList : Landroid/support/v7/widget/ListPopupWindow$DropDownListView;
    //   129: aload_0
    //   130: getfield mScrollListener : Landroid/support/v7/widget/ListPopupWindow$PopupScrollListener;
    //   133: invokevirtual setOnScrollListener : (Landroid/widget/AbsListView$OnScrollListener;)V
    //   136: aload_0
    //   137: getfield mItemSelectedListener : Landroid/widget/AdapterView$OnItemSelectedListener;
    //   140: ifnull -> 154
    //   143: aload_0
    //   144: getfield mDropDownList : Landroid/support/v7/widget/ListPopupWindow$DropDownListView;
    //   147: aload_0
    //   148: getfield mItemSelectedListener : Landroid/widget/AdapterView$OnItemSelectedListener;
    //   151: invokevirtual setOnItemSelectedListener : (Landroid/widget/AdapterView$OnItemSelectedListener;)V
    //   154: aload_0
    //   155: getfield mDropDownList : Landroid/support/v7/widget/ListPopupWindow$DropDownListView;
    //   158: astore #7
    //   160: aload_0
    //   161: getfield mPromptView : Landroid/view/View;
    //   164: astore #8
    //   166: aload #7
    //   168: astore #6
    //   170: aload #8
    //   172: ifnull -> 316
    //   175: new android/widget/LinearLayout
    //   178: dup
    //   179: aload #9
    //   181: invokespecial <init> : (Landroid/content/Context;)V
    //   184: astore #6
    //   186: aload #6
    //   188: iconst_1
    //   189: invokevirtual setOrientation : (I)V
    //   192: new android/widget/LinearLayout$LayoutParams
    //   195: dup
    //   196: iconst_m1
    //   197: iconst_0
    //   198: fconst_1
    //   199: invokespecial <init> : (IIF)V
    //   202: astore #9
    //   204: aload_0
    //   205: getfield mPromptPosition : I
    //   208: tableswitch default -> 232, 0 -> 465, 1 -> 446
    //   232: ldc 'ListPopupWindow'
    //   234: new java/lang/StringBuilder
    //   237: dup
    //   238: invokespecial <init> : ()V
    //   241: ldc_w 'Invalid hint position '
    //   244: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   247: aload_0
    //   248: getfield mPromptPosition : I
    //   251: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   254: invokevirtual toString : ()Ljava/lang/String;
    //   257: invokestatic e : (Ljava/lang/String;Ljava/lang/String;)I
    //   260: pop
    //   261: aload_0
    //   262: getfield mDropDownWidth : I
    //   265: iflt -> 484
    //   268: ldc_w -2147483648
    //   271: istore_1
    //   272: aload_0
    //   273: getfield mDropDownWidth : I
    //   276: istore_2
    //   277: aload #8
    //   279: iload_2
    //   280: iload_1
    //   281: invokestatic makeMeasureSpec : (II)I
    //   284: iconst_0
    //   285: invokevirtual measure : (II)V
    //   288: aload #8
    //   290: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   293: checkcast android/widget/LinearLayout$LayoutParams
    //   296: astore #7
    //   298: aload #8
    //   300: invokevirtual getMeasuredHeight : ()I
    //   303: aload #7
    //   305: getfield topMargin : I
    //   308: iadd
    //   309: aload #7
    //   311: getfield bottomMargin : I
    //   314: iadd
    //   315: istore_1
    //   316: aload_0
    //   317: getfield mPopup : Landroid/widget/PopupWindow;
    //   320: aload #6
    //   322: invokevirtual setContentView : (Landroid/view/View;)V
    //   325: iconst_0
    //   326: istore_3
    //   327: aload_0
    //   328: getfield mPopup : Landroid/widget/PopupWindow;
    //   331: invokevirtual getBackground : ()Landroid/graphics/drawable/Drawable;
    //   334: astore #6
    //   336: aload #6
    //   338: ifnull -> 547
    //   341: aload #6
    //   343: aload_0
    //   344: getfield mTempRect : Landroid/graphics/Rect;
    //   347: invokevirtual getPadding : (Landroid/graphics/Rect;)Z
    //   350: pop
    //   351: aload_0
    //   352: getfield mTempRect : Landroid/graphics/Rect;
    //   355: getfield top : I
    //   358: aload_0
    //   359: getfield mTempRect : Landroid/graphics/Rect;
    //   362: getfield bottom : I
    //   365: iadd
    //   366: istore_2
    //   367: iload_2
    //   368: istore_3
    //   369: aload_0
    //   370: getfield mDropDownVerticalOffsetSet : Z
    //   373: ifne -> 390
    //   376: aload_0
    //   377: aload_0
    //   378: getfield mTempRect : Landroid/graphics/Rect;
    //   381: getfield top : I
    //   384: ineg
    //   385: putfield mDropDownVerticalOffset : I
    //   388: iload_2
    //   389: istore_3
    //   390: aload_0
    //   391: getfield mPopup : Landroid/widget/PopupWindow;
    //   394: invokevirtual getInputMethodMode : ()I
    //   397: iconst_2
    //   398: if_icmpne -> 557
    //   401: iconst_1
    //   402: istore #5
    //   404: aload_0
    //   405: aload_0
    //   406: invokevirtual getAnchorView : ()Landroid/view/View;
    //   409: aload_0
    //   410: getfield mDropDownVerticalOffset : I
    //   413: iload #5
    //   415: invokespecial getMaxAvailableHeight : (Landroid/view/View;IZ)I
    //   418: istore #4
    //   420: aload_0
    //   421: getfield mDropDownAlwaysVisible : Z
    //   424: ifne -> 435
    //   427: aload_0
    //   428: getfield mDropDownHeight : I
    //   431: iconst_m1
    //   432: if_icmpne -> 563
    //   435: iload #4
    //   437: iload_3
    //   438: iadd
    //   439: ireturn
    //   440: iconst_0
    //   441: istore #5
    //   443: goto -> 39
    //   446: aload #6
    //   448: aload #7
    //   450: aload #9
    //   452: invokevirtual addView : (Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V
    //   455: aload #6
    //   457: aload #8
    //   459: invokevirtual addView : (Landroid/view/View;)V
    //   462: goto -> 261
    //   465: aload #6
    //   467: aload #8
    //   469: invokevirtual addView : (Landroid/view/View;)V
    //   472: aload #6
    //   474: aload #7
    //   476: aload #9
    //   478: invokevirtual addView : (Landroid/view/View;Landroid/view/ViewGroup$LayoutParams;)V
    //   481: goto -> 261
    //   484: iconst_0
    //   485: istore_1
    //   486: iconst_0
    //   487: istore_2
    //   488: goto -> 277
    //   491: aload_0
    //   492: getfield mPopup : Landroid/widget/PopupWindow;
    //   495: invokevirtual getContentView : ()Landroid/view/View;
    //   498: checkcast android/view/ViewGroup
    //   501: astore #6
    //   503: aload_0
    //   504: getfield mPromptView : Landroid/view/View;
    //   507: astore #6
    //   509: iload_2
    //   510: istore_1
    //   511: aload #6
    //   513: ifnull -> 325
    //   516: aload #6
    //   518: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   521: checkcast android/widget/LinearLayout$LayoutParams
    //   524: astore #7
    //   526: aload #6
    //   528: invokevirtual getMeasuredHeight : ()I
    //   531: aload #7
    //   533: getfield topMargin : I
    //   536: iadd
    //   537: aload #7
    //   539: getfield bottomMargin : I
    //   542: iadd
    //   543: istore_1
    //   544: goto -> 325
    //   547: aload_0
    //   548: getfield mTempRect : Landroid/graphics/Rect;
    //   551: invokevirtual setEmpty : ()V
    //   554: goto -> 390
    //   557: iconst_0
    //   558: istore #5
    //   560: goto -> 404
    //   563: aload_0
    //   564: getfield mDropDownWidth : I
    //   567: tableswitch default -> 588, -2 -> 632, -1 -> 671
    //   588: aload_0
    //   589: getfield mDropDownWidth : I
    //   592: ldc_w 1073741824
    //   595: invokestatic makeMeasureSpec : (II)I
    //   598: istore_2
    //   599: aload_0
    //   600: getfield mDropDownList : Landroid/support/v7/widget/ListPopupWindow$DropDownListView;
    //   603: iload_2
    //   604: iconst_0
    //   605: iconst_m1
    //   606: iload #4
    //   608: iload_1
    //   609: isub
    //   610: iconst_m1
    //   611: invokevirtual measureHeightOfChildrenCompat : (IIIII)I
    //   614: istore #4
    //   616: iload_1
    //   617: istore_2
    //   618: iload #4
    //   620: ifle -> 627
    //   623: iload_1
    //   624: iload_3
    //   625: iadd
    //   626: istore_2
    //   627: iload #4
    //   629: iload_2
    //   630: iadd
    //   631: ireturn
    //   632: aload_0
    //   633: getfield mContext : Landroid/content/Context;
    //   636: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   639: invokevirtual getDisplayMetrics : ()Landroid/util/DisplayMetrics;
    //   642: getfield widthPixels : I
    //   645: aload_0
    //   646: getfield mTempRect : Landroid/graphics/Rect;
    //   649: getfield left : I
    //   652: aload_0
    //   653: getfield mTempRect : Landroid/graphics/Rect;
    //   656: getfield right : I
    //   659: iadd
    //   660: isub
    //   661: ldc_w -2147483648
    //   664: invokestatic makeMeasureSpec : (II)I
    //   667: istore_2
    //   668: goto -> 599
    //   671: aload_0
    //   672: getfield mContext : Landroid/content/Context;
    //   675: invokevirtual getResources : ()Landroid/content/res/Resources;
    //   678: invokevirtual getDisplayMetrics : ()Landroid/util/DisplayMetrics;
    //   681: getfield widthPixels : I
    //   684: aload_0
    //   685: getfield mTempRect : Landroid/graphics/Rect;
    //   688: getfield left : I
    //   691: aload_0
    //   692: getfield mTempRect : Landroid/graphics/Rect;
    //   695: getfield right : I
    //   698: iadd
    //   699: isub
    //   700: ldc_w 1073741824
    //   703: invokestatic makeMeasureSpec : (II)I
    //   706: istore_2
    //   707: goto -> 599
  }
  
  private int getMaxAvailableHeight(View paramView, int paramInt, boolean paramBoolean) {
    if (sGetMaxAvailableHeightMethod != null)
      try {
        return ((Integer)sGetMaxAvailableHeightMethod.invoke(this.mPopup, new Object[] { paramView, Integer.valueOf(paramInt), Boolean.valueOf(paramBoolean) })).intValue();
      } catch (Exception exception) {
        Log.i("ListPopupWindow", "Could not call getMaxAvailableHeightMethod(View, int, boolean) on PopupWindow. Using the public version.");
      }  
    return this.mPopup.getMaxAvailableHeight(paramView, paramInt);
  }
  
  private static boolean isConfirmKey(int paramInt) {
    return (paramInt == 66 || paramInt == 23);
  }
  
  private void removePromptView() {
    if (this.mPromptView != null) {
      ViewParent viewParent = this.mPromptView.getParent();
      if (viewParent instanceof ViewGroup)
        ((ViewGroup)viewParent).removeView(this.mPromptView); 
    } 
  }
  
  private void setPopupClipToScreenEnabled(boolean paramBoolean) {
    if (sClipToWindowEnabledMethod != null)
      try {
        sClipToWindowEnabledMethod.invoke(this.mPopup, new Object[] { Boolean.valueOf(paramBoolean) });
        return;
      } catch (Exception exception) {
        Log.i("ListPopupWindow", "Could not call setClipToScreenEnabled() on PopupWindow. Oh well.");
        return;
      }  
  }
  
  public void clearListSelection() {
    DropDownListView dropDownListView = this.mDropDownList;
    if (dropDownListView != null) {
      DropDownListView.access$502(dropDownListView, true);
      dropDownListView.requestLayout();
    } 
  }
  
  public View.OnTouchListener createDragToOpenListener(View paramView) {
    return new ForwardingListener(paramView) {
        public ListPopupWindow getPopup() {
          return ListPopupWindow.this;
        }
      };
  }
  
  public void dismiss() {
    this.mPopup.dismiss();
    removePromptView();
    this.mPopup.setContentView(null);
    this.mDropDownList = null;
    this.mHandler.removeCallbacks(this.mResizePopupRunnable);
  }
  
  public View getAnchorView() {
    return this.mDropDownAnchorView;
  }
  
  public int getAnimationStyle() {
    return this.mPopup.getAnimationStyle();
  }
  
  public Drawable getBackground() {
    return this.mPopup.getBackground();
  }
  
  public int getHeight() {
    return this.mDropDownHeight;
  }
  
  public int getHorizontalOffset() {
    return this.mDropDownHorizontalOffset;
  }
  
  public int getInputMethodMode() {
    return this.mPopup.getInputMethodMode();
  }
  
  public ListView getListView() {
    return this.mDropDownList;
  }
  
  public int getPromptPosition() {
    return this.mPromptPosition;
  }
  
  public Object getSelectedItem() {
    return !isShowing() ? null : this.mDropDownList.getSelectedItem();
  }
  
  public long getSelectedItemId() {
    return !isShowing() ? Long.MIN_VALUE : this.mDropDownList.getSelectedItemId();
  }
  
  public int getSelectedItemPosition() {
    return !isShowing() ? -1 : this.mDropDownList.getSelectedItemPosition();
  }
  
  public View getSelectedView() {
    return !isShowing() ? null : this.mDropDownList.getSelectedView();
  }
  
  public int getSoftInputMode() {
    return this.mPopup.getSoftInputMode();
  }
  
  public int getVerticalOffset() {
    return !this.mDropDownVerticalOffsetSet ? 0 : this.mDropDownVerticalOffset;
  }
  
  public int getWidth() {
    return this.mDropDownWidth;
  }
  
  public boolean isDropDownAlwaysVisible() {
    return this.mDropDownAlwaysVisible;
  }
  
  public boolean isInputMethodNotNeeded() {
    return (this.mPopup.getInputMethodMode() == 2);
  }
  
  public boolean isModal() {
    return this.mModal;
  }
  
  public boolean isShowing() {
    return this.mPopup.isShowing();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    int i;
    int j;
    boolean bool;
    int k;
    if (isShowing() && paramInt != 62 && (this.mDropDownList.getSelectedItemPosition() >= 0 || !isConfirmKey(paramInt))) {
      k = this.mDropDownList.getSelectedItemPosition();
      if (!this.mPopup.isAboveAnchor()) {
        bool = true;
      } else {
        bool = false;
      } 
      ListAdapter listAdapter = this.mAdapter;
      i = Integer.MAX_VALUE;
      j = Integer.MIN_VALUE;
      if (listAdapter != null) {
        boolean bool1 = listAdapter.areAllItemsEnabled();
        if (bool1) {
          i = 0;
        } else {
          i = this.mDropDownList.lookForSelectablePosition(0, true);
        } 
        if (bool1) {
          j = listAdapter.getCount() - 1;
        } else {
          j = this.mDropDownList.lookForSelectablePosition(listAdapter.getCount() - 1, false);
        } 
      } 
      if ((bool && paramInt == 19 && k <= i) || (!bool && paramInt == 20 && k >= j)) {
        clearListSelection();
        this.mPopup.setInputMethodMode(1);
        show();
      } 
      DropDownListView.access$502(this.mDropDownList, false);
      if (this.mDropDownList.onKeyDown(paramInt, paramKeyEvent)) {
        this.mPopup.setInputMethodMode(2);
        this.mDropDownList.requestFocusFromTouch();
        show();
        switch (paramInt) {
          case 19:
          case 20:
          case 23:
          case 66:
            return true;
        } 
        return false;
      } 
    } else {
      return false;
    } 
    return (bool && paramInt == 20) ? ((k == j)) : ((!bool && paramInt == 19 && k == i));
  }
  
  public boolean onKeyPreIme(int paramInt, KeyEvent paramKeyEvent) {
    if (paramInt == 4 && isShowing()) {
      KeyEvent.DispatcherState dispatcherState;
      View view = this.mDropDownAnchorView;
      if (paramKeyEvent.getAction() == 0 && paramKeyEvent.getRepeatCount() == 0) {
        dispatcherState = view.getKeyDispatcherState();
        if (dispatcherState != null)
          dispatcherState.startTracking(paramKeyEvent, this); 
        return true;
      } 
      if (paramKeyEvent.getAction() == 1) {
        dispatcherState = dispatcherState.getKeyDispatcherState();
        if (dispatcherState != null)
          dispatcherState.handleUpEvent(paramKeyEvent); 
        if (paramKeyEvent.isTracking() && !paramKeyEvent.isCanceled()) {
          dismiss();
          return true;
        } 
      } 
    } 
    return false;
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    if (isShowing() && this.mDropDownList.getSelectedItemPosition() >= 0) {
      boolean bool = this.mDropDownList.onKeyUp(paramInt, paramKeyEvent);
      if (bool && isConfirmKey(paramInt))
        dismiss(); 
      return bool;
    } 
    return false;
  }
  
  public boolean performItemClick(int paramInt) {
    if (isShowing()) {
      if (this.mItemClickListener != null) {
        DropDownListView dropDownListView = this.mDropDownList;
        View view = dropDownListView.getChildAt(paramInt - dropDownListView.getFirstVisiblePosition());
        ListAdapter listAdapter = dropDownListView.getAdapter();
        this.mItemClickListener.onItemClick((AdapterView)dropDownListView, view, paramInt, listAdapter.getItemId(paramInt));
      } 
      return true;
    } 
    return false;
  }
  
  public void postShow() {
    this.mHandler.post(this.mShowDropDownRunnable);
  }
  
  public void setAdapter(ListAdapter paramListAdapter) {
    if (this.mObserver == null) {
      this.mObserver = new PopupDataSetObserver();
    } else if (this.mAdapter != null) {
      this.mAdapter.unregisterDataSetObserver(this.mObserver);
    } 
    this.mAdapter = paramListAdapter;
    if (this.mAdapter != null)
      paramListAdapter.registerDataSetObserver(this.mObserver); 
    if (this.mDropDownList != null)
      this.mDropDownList.setAdapter(this.mAdapter); 
  }
  
  public void setAnchorView(View paramView) {
    this.mDropDownAnchorView = paramView;
  }
  
  public void setAnimationStyle(int paramInt) {
    this.mPopup.setAnimationStyle(paramInt);
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable) {
    this.mPopup.setBackgroundDrawable(paramDrawable);
  }
  
  public void setContentWidth(int paramInt) {
    Drawable drawable = this.mPopup.getBackground();
    if (drawable != null) {
      drawable.getPadding(this.mTempRect);
      this.mDropDownWidth = this.mTempRect.left + this.mTempRect.right + paramInt;
      return;
    } 
    setWidth(paramInt);
  }
  
  public void setDropDownAlwaysVisible(boolean paramBoolean) {
    this.mDropDownAlwaysVisible = paramBoolean;
  }
  
  public void setDropDownGravity(int paramInt) {
    this.mDropDownGravity = paramInt;
  }
  
  public void setForceIgnoreOutsideTouch(boolean paramBoolean) {
    this.mForceIgnoreOutsideTouch = paramBoolean;
  }
  
  public void setHeight(int paramInt) {
    this.mDropDownHeight = paramInt;
  }
  
  public void setHorizontalOffset(int paramInt) {
    this.mDropDownHorizontalOffset = paramInt;
  }
  
  public void setInputMethodMode(int paramInt) {
    this.mPopup.setInputMethodMode(paramInt);
  }
  
  void setListItemExpandMax(int paramInt) {
    this.mListItemExpandMaximum = paramInt;
  }
  
  public void setListSelector(Drawable paramDrawable) {
    this.mDropDownListHighlight = paramDrawable;
  }
  
  public void setModal(boolean paramBoolean) {
    this.mModal = paramBoolean;
    this.mPopup.setFocusable(paramBoolean);
  }
  
  public void setOnDismissListener(PopupWindow.OnDismissListener paramOnDismissListener) {
    this.mPopup.setOnDismissListener(paramOnDismissListener);
  }
  
  public void setOnItemClickListener(AdapterView.OnItemClickListener paramOnItemClickListener) {
    this.mItemClickListener = paramOnItemClickListener;
  }
  
  public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener paramOnItemSelectedListener) {
    this.mItemSelectedListener = paramOnItemSelectedListener;
  }
  
  public void setPromptPosition(int paramInt) {
    this.mPromptPosition = paramInt;
  }
  
  public void setPromptView(View paramView) {
    boolean bool = isShowing();
    if (bool)
      removePromptView(); 
    this.mPromptView = paramView;
    if (bool)
      show(); 
  }
  
  public void setSelection(int paramInt) {
    DropDownListView dropDownListView = this.mDropDownList;
    if (isShowing() && dropDownListView != null) {
      DropDownListView.access$502(dropDownListView, false);
      dropDownListView.setSelection(paramInt);
      if (Build.VERSION.SDK_INT >= 11 && dropDownListView.getChoiceMode() != 0)
        dropDownListView.setItemChecked(paramInt, true); 
    } 
  }
  
  public void setSoftInputMode(int paramInt) {
    this.mPopup.setSoftInputMode(paramInt);
  }
  
  public void setVerticalOffset(int paramInt) {
    this.mDropDownVerticalOffset = paramInt;
    this.mDropDownVerticalOffsetSet = true;
  }
  
  public void setWidth(int paramInt) {
    this.mDropDownWidth = paramInt;
  }
  
  public void setWindowLayoutType(int paramInt) {
    this.mDropDownWindowLayoutType = paramInt;
  }
  
  public void show() {
    int i;
    boolean bool1 = true;
    boolean bool2 = false;
    byte b = -1;
    int j = buildDropDown();
    boolean bool = isInputMethodNotNeeded();
    PopupWindowCompat.setWindowLayoutType(this.mPopup, this.mDropDownWindowLayoutType);
    if (this.mPopup.isShowing()) {
      if (this.mDropDownWidth == -1) {
        i = -1;
      } else if (this.mDropDownWidth == -2) {
        i = getAnchorView().getWidth();
      } else {
        i = this.mDropDownWidth;
      } 
      if (this.mDropDownHeight == -1) {
        if (!bool)
          j = -1; 
        if (bool) {
          boolean bool3;
          PopupWindow popupWindow2 = this.mPopup;
          if (this.mDropDownWidth == -1) {
            bool3 = true;
          } else {
            bool3 = false;
          } 
          popupWindow2.setWidth(bool3);
          this.mPopup.setHeight(0);
        } else {
          boolean bool3;
          PopupWindow popupWindow2 = this.mPopup;
          if (this.mDropDownWidth == -1) {
            bool3 = true;
          } else {
            bool3 = false;
          } 
          popupWindow2.setWidth(bool3);
          this.mPopup.setHeight(-1);
        } 
      } else if (this.mDropDownHeight != -2) {
        j = this.mDropDownHeight;
      } 
      PopupWindow popupWindow1 = this.mPopup;
      bool1 = bool2;
      if (!this.mForceIgnoreOutsideTouch) {
        bool1 = bool2;
        if (!this.mDropDownAlwaysVisible)
          bool1 = true; 
      } 
      popupWindow1.setOutsideTouchable(bool1);
      popupWindow1 = this.mPopup;
      View view = getAnchorView();
      int k = this.mDropDownHorizontalOffset;
      int m = this.mDropDownVerticalOffset;
      if (i < 0)
        i = -1; 
      if (j < 0)
        j = b; 
      popupWindow1.update(view, k, m, i, j);
      return;
    } 
    if (this.mDropDownWidth == -1) {
      i = -1;
    } else if (this.mDropDownWidth == -2) {
      i = getAnchorView().getWidth();
    } else {
      i = this.mDropDownWidth;
    } 
    if (this.mDropDownHeight == -1) {
      j = -1;
    } else if (this.mDropDownHeight != -2) {
      j = this.mDropDownHeight;
    } 
    this.mPopup.setWidth(i);
    this.mPopup.setHeight(j);
    setPopupClipToScreenEnabled(true);
    PopupWindow popupWindow = this.mPopup;
    if (this.mForceIgnoreOutsideTouch || this.mDropDownAlwaysVisible)
      bool1 = false; 
    popupWindow.setOutsideTouchable(bool1);
    this.mPopup.setTouchInterceptor(this.mTouchInterceptor);
    PopupWindowCompat.showAsDropDown(this.mPopup, getAnchorView(), this.mDropDownHorizontalOffset, this.mDropDownVerticalOffset, this.mDropDownGravity);
    this.mDropDownList.setSelection(-1);
    if (!this.mModal || this.mDropDownList.isInTouchMode())
      clearListSelection(); 
    if (!this.mModal) {
      this.mHandler.post(this.mHideSelector);
      return;
    } 
  }
  
  private static class DropDownListView extends ListViewCompat {
    private ViewPropertyAnimatorCompat mClickAnimation;
    
    private boolean mDrawsInPressedState;
    
    private boolean mHijackFocus;
    
    private boolean mListSelectionHidden;
    
    private ListViewAutoScrollHelper mScrollHelper;
    
    public DropDownListView(Context param1Context, boolean param1Boolean) {
      super(param1Context, (AttributeSet)null, R.attr.dropDownListViewStyle);
      this.mHijackFocus = param1Boolean;
      setCacheColorHint(0);
    }
    
    private void clearPressedItem() {
      this.mDrawsInPressedState = false;
      setPressed(false);
      drawableStateChanged();
      View view = getChildAt(this.mMotionPosition - getFirstVisiblePosition());
      if (view != null)
        view.setPressed(false); 
      if (this.mClickAnimation != null) {
        this.mClickAnimation.cancel();
        this.mClickAnimation = null;
      } 
    }
    
    private void clickPressedItem(View param1View, int param1Int) {
      performItemClick(param1View, param1Int, getItemIdAtPosition(param1Int));
    }
    
    private void setPressedItem(View param1View, int param1Int, float param1Float1, float param1Float2) {
      this.mDrawsInPressedState = true;
      if (Build.VERSION.SDK_INT >= 21)
        drawableHotspotChanged(param1Float1, param1Float2); 
      if (!isPressed())
        setPressed(true); 
      layoutChildren();
      if (this.mMotionPosition != -1) {
        View view = getChildAt(this.mMotionPosition - getFirstVisiblePosition());
        if (view != null && view != param1View && view.isPressed())
          view.setPressed(false); 
      } 
      this.mMotionPosition = param1Int;
      float f1 = param1View.getLeft();
      float f2 = param1View.getTop();
      if (Build.VERSION.SDK_INT >= 21)
        param1View.drawableHotspotChanged(param1Float1 - f1, param1Float2 - f2); 
      if (!param1View.isPressed())
        param1View.setPressed(true); 
      setSelection(param1Int);
      positionSelectorLikeTouchCompat(param1Int, param1View, param1Float1, param1Float2);
      setSelectorEnabled(false);
      refreshDrawableState();
    }
    
    public boolean hasFocus() {
      return (this.mHijackFocus || super.hasFocus());
    }
    
    public boolean hasWindowFocus() {
      return (this.mHijackFocus || super.hasWindowFocus());
    }
    
    public boolean isFocused() {
      return (this.mHijackFocus || super.isFocused());
    }
    
    public boolean isInTouchMode() {
      return ((this.mHijackFocus && this.mListSelectionHidden) || super.isInTouchMode());
    }
    
    public boolean onForwardedEvent(MotionEvent param1MotionEvent, int param1Int) {
      // Byte code:
      //   0: iconst_1
      //   1: istore #7
      //   3: iconst_1
      //   4: istore #8
      //   6: iconst_0
      //   7: istore_3
      //   8: aload_1
      //   9: invokestatic getActionMasked : (Landroid/view/MotionEvent;)I
      //   12: istore #4
      //   14: iload #4
      //   16: tableswitch default -> 44, 1 -> 117, 2 -> 120, 3 -> 109
      //   44: iload #8
      //   46: istore #7
      //   48: iload_3
      //   49: istore_2
      //   50: iload #7
      //   52: ifeq -> 59
      //   55: iload_2
      //   56: ifeq -> 63
      //   59: aload_0
      //   60: invokespecial clearPressedItem : ()V
      //   63: iload #7
      //   65: ifeq -> 235
      //   68: aload_0
      //   69: getfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
      //   72: ifnonnull -> 87
      //   75: aload_0
      //   76: new android/support/v4/widget/ListViewAutoScrollHelper
      //   79: dup
      //   80: aload_0
      //   81: invokespecial <init> : (Landroid/widget/ListView;)V
      //   84: putfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
      //   87: aload_0
      //   88: getfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
      //   91: iconst_1
      //   92: invokevirtual setEnabled : (Z)Landroid/support/v4/widget/AutoScrollHelper;
      //   95: pop
      //   96: aload_0
      //   97: getfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
      //   100: aload_0
      //   101: aload_1
      //   102: invokevirtual onTouch : (Landroid/view/View;Landroid/view/MotionEvent;)Z
      //   105: pop
      //   106: iload #7
      //   108: ireturn
      //   109: iconst_0
      //   110: istore #7
      //   112: iload_3
      //   113: istore_2
      //   114: goto -> 50
      //   117: iconst_0
      //   118: istore #7
      //   120: aload_1
      //   121: iload_2
      //   122: invokevirtual findPointerIndex : (I)I
      //   125: istore #5
      //   127: iload #5
      //   129: ifge -> 140
      //   132: iconst_0
      //   133: istore #7
      //   135: iload_3
      //   136: istore_2
      //   137: goto -> 50
      //   140: aload_1
      //   141: iload #5
      //   143: invokevirtual getX : (I)F
      //   146: f2i
      //   147: istore_2
      //   148: aload_1
      //   149: iload #5
      //   151: invokevirtual getY : (I)F
      //   154: f2i
      //   155: istore #6
      //   157: aload_0
      //   158: iload_2
      //   159: iload #6
      //   161: invokevirtual pointToPosition : (II)I
      //   164: istore #5
      //   166: iload #5
      //   168: iconst_m1
      //   169: if_icmpne -> 177
      //   172: iconst_1
      //   173: istore_2
      //   174: goto -> 50
      //   177: aload_0
      //   178: iload #5
      //   180: aload_0
      //   181: invokevirtual getFirstVisiblePosition : ()I
      //   184: isub
      //   185: invokevirtual getChildAt : (I)Landroid/view/View;
      //   188: astore #9
      //   190: aload_0
      //   191: aload #9
      //   193: iload #5
      //   195: iload_2
      //   196: i2f
      //   197: iload #6
      //   199: i2f
      //   200: invokespecial setPressedItem : (Landroid/view/View;IFF)V
      //   203: iconst_1
      //   204: istore #8
      //   206: iload_3
      //   207: istore_2
      //   208: iload #8
      //   210: istore #7
      //   212: iload #4
      //   214: iconst_1
      //   215: if_icmpne -> 50
      //   218: aload_0
      //   219: aload #9
      //   221: iload #5
      //   223: invokespecial clickPressedItem : (Landroid/view/View;I)V
      //   226: iload_3
      //   227: istore_2
      //   228: iload #8
      //   230: istore #7
      //   232: goto -> 50
      //   235: aload_0
      //   236: getfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
      //   239: ifnull -> 106
      //   242: aload_0
      //   243: getfield mScrollHelper : Landroid/support/v4/widget/ListViewAutoScrollHelper;
      //   246: iconst_0
      //   247: invokevirtual setEnabled : (Z)Landroid/support/v4/widget/AutoScrollHelper;
      //   250: pop
      //   251: iload #7
      //   253: ireturn
    }
    
    protected boolean touchModeDrawsInPressedStateCompat() {
      return (this.mDrawsInPressedState || super.touchModeDrawsInPressedStateCompat());
    }
  }
  
  public static abstract class ForwardingListener implements View.OnTouchListener {
    private int mActivePointerId;
    
    private Runnable mDisallowIntercept;
    
    private boolean mForwarding;
    
    private final int mLongPressTimeout;
    
    private final float mScaledTouchSlop;
    
    private final View mSrc;
    
    private final int mTapTimeout;
    
    private final int[] mTmpLocation = new int[2];
    
    private Runnable mTriggerLongPress;
    
    private boolean mWasLongPress;
    
    public ForwardingListener(View param1View) {
      this.mSrc = param1View;
      this.mScaledTouchSlop = ViewConfiguration.get(param1View.getContext()).getScaledTouchSlop();
      this.mTapTimeout = ViewConfiguration.getTapTimeout();
      this.mLongPressTimeout = (this.mTapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
    }
    
    private void clearCallbacks() {
      if (this.mTriggerLongPress != null)
        this.mSrc.removeCallbacks(this.mTriggerLongPress); 
      if (this.mDisallowIntercept != null)
        this.mSrc.removeCallbacks(this.mDisallowIntercept); 
    }
    
    private void onLongPress() {
      clearCallbacks();
      View view = this.mSrc;
      if (view.isEnabled() && !view.isLongClickable() && onForwardingStarted()) {
        view.getParent().requestDisallowInterceptTouchEvent(true);
        long l = SystemClock.uptimeMillis();
        MotionEvent motionEvent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
        view.onTouchEvent(motionEvent);
        motionEvent.recycle();
        this.mForwarding = true;
        this.mWasLongPress = true;
        return;
      } 
    }
    
    private boolean onTouchForwarded(MotionEvent param1MotionEvent) {
      boolean bool = true;
      View view = this.mSrc;
      ListPopupWindow listPopupWindow = getPopup();
      if (listPopupWindow != null && listPopupWindow.isShowing()) {
        ListPopupWindow.DropDownListView dropDownListView = listPopupWindow.mDropDownList;
        if (dropDownListView != null && dropDownListView.isShown()) {
          MotionEvent motionEvent = MotionEvent.obtainNoHistory(param1MotionEvent);
          toGlobalMotionEvent(view, motionEvent);
          toLocalMotionEvent((View)dropDownListView, motionEvent);
          boolean bool1 = dropDownListView.onForwardedEvent(motionEvent, this.mActivePointerId);
          motionEvent.recycle();
          int i = MotionEventCompat.getActionMasked(param1MotionEvent);
          if (i != 1 && i != 3) {
            i = 1;
          } else {
            i = 0;
          } 
          if (!bool1 || i == 0)
            bool = false; 
          return bool;
        } 
      } 
      return false;
    }
    
    private boolean onTouchObserved(MotionEvent param1MotionEvent) {
      View view = this.mSrc;
      if (view.isEnabled()) {
        int i;
        switch (MotionEventCompat.getActionMasked(param1MotionEvent)) {
          default:
            return false;
          case 0:
            this.mActivePointerId = param1MotionEvent.getPointerId(0);
            this.mWasLongPress = false;
            if (this.mDisallowIntercept == null)
              this.mDisallowIntercept = new DisallowIntercept(); 
            view.postDelayed(this.mDisallowIntercept, this.mTapTimeout);
            if (this.mTriggerLongPress == null)
              this.mTriggerLongPress = new TriggerLongPress(); 
            view.postDelayed(this.mTriggerLongPress, this.mLongPressTimeout);
            return false;
          case 2:
            i = param1MotionEvent.findPointerIndex(this.mActivePointerId);
            if (i >= 0 && !pointInView(view, param1MotionEvent.getX(i), param1MotionEvent.getY(i), this.mScaledTouchSlop)) {
              clearCallbacks();
              view.getParent().requestDisallowInterceptTouchEvent(true);
              return true;
            } 
            return false;
          case 1:
          case 3:
            break;
        } 
        clearCallbacks();
        return false;
      } 
      return false;
    }
    
    private static boolean pointInView(View param1View, float param1Float1, float param1Float2, float param1Float3) {
      return (param1Float1 >= -param1Float3 && param1Float2 >= -param1Float3 && param1Float1 < (param1View.getRight() - param1View.getLeft()) + param1Float3 && param1Float2 < (param1View.getBottom() - param1View.getTop()) + param1Float3);
    }
    
    private boolean toGlobalMotionEvent(View param1View, MotionEvent param1MotionEvent) {
      int[] arrayOfInt = this.mTmpLocation;
      param1View.getLocationOnScreen(arrayOfInt);
      param1MotionEvent.offsetLocation(arrayOfInt[0], arrayOfInt[1]);
      return true;
    }
    
    private boolean toLocalMotionEvent(View param1View, MotionEvent param1MotionEvent) {
      int[] arrayOfInt = this.mTmpLocation;
      param1View.getLocationOnScreen(arrayOfInt);
      param1MotionEvent.offsetLocation(-arrayOfInt[0], -arrayOfInt[1]);
      return true;
    }
    
    public abstract ListPopupWindow getPopup();
    
    protected boolean onForwardingStarted() {
      ListPopupWindow listPopupWindow = getPopup();
      if (listPopupWindow != null && !listPopupWindow.isShowing())
        listPopupWindow.show(); 
      return true;
    }
    
    protected boolean onForwardingStopped() {
      ListPopupWindow listPopupWindow = getPopup();
      if (listPopupWindow != null && listPopupWindow.isShowing())
        listPopupWindow.dismiss(); 
      return true;
    }
    
    public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
      boolean bool = false;
      boolean bool1 = this.mForwarding;
      if (bool1) {
        if (this.mWasLongPress) {
          null = onTouchForwarded(param1MotionEvent);
        } else if (onTouchForwarded(param1MotionEvent) || !onForwardingStopped()) {
          null = true;
        } else {
          null = false;
        } 
      } else {
        boolean bool2;
        if (onTouchObserved(param1MotionEvent) && onForwardingStarted()) {
          bool2 = true;
        } else {
          bool2 = false;
        } 
        null = bool2;
        if (bool2) {
          long l = SystemClock.uptimeMillis();
          MotionEvent motionEvent = MotionEvent.obtain(l, l, 3, 0.0F, 0.0F, 0);
          this.mSrc.onTouchEvent(motionEvent);
          motionEvent.recycle();
          null = bool2;
        } 
      } 
      this.mForwarding = null;
      if (!null) {
        null = bool;
        return bool1 ? true : null;
      } 
      return true;
    }
    
    private class DisallowIntercept implements Runnable {
      private DisallowIntercept() {}
      
      public void run() {
        ListPopupWindow.ForwardingListener.this.mSrc.getParent().requestDisallowInterceptTouchEvent(true);
      }
    }
    
    private class TriggerLongPress implements Runnable {
      private TriggerLongPress() {}
      
      public void run() {
        ListPopupWindow.ForwardingListener.this.onLongPress();
      }
    }
  }
  
  private class DisallowIntercept implements Runnable {
    private DisallowIntercept() {}
    
    public void run() {
      ListPopupWindow.ForwardingListener.this.mSrc.getParent().requestDisallowInterceptTouchEvent(true);
    }
  }
  
  private class TriggerLongPress implements Runnable {
    private TriggerLongPress() {}
    
    public void run() {
      ListPopupWindow.ForwardingListener.this.onLongPress();
    }
  }
  
  private class ListSelectorHider implements Runnable {
    private ListSelectorHider() {}
    
    public void run() {
      ListPopupWindow.this.clearListSelection();
    }
  }
  
  private class PopupDataSetObserver extends DataSetObserver {
    private PopupDataSetObserver() {}
    
    public void onChanged() {
      if (ListPopupWindow.this.isShowing())
        ListPopupWindow.this.show(); 
    }
    
    public void onInvalidated() {
      ListPopupWindow.this.dismiss();
    }
  }
  
  private class PopupScrollListener implements AbsListView.OnScrollListener {
    private PopupScrollListener() {}
    
    public void onScroll(AbsListView param1AbsListView, int param1Int1, int param1Int2, int param1Int3) {}
    
    public void onScrollStateChanged(AbsListView param1AbsListView, int param1Int) {
      if (param1Int == 1 && !ListPopupWindow.this.isInputMethodNotNeeded() && ListPopupWindow.this.mPopup.getContentView() != null) {
        ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable);
        ListPopupWindow.this.mResizePopupRunnable.run();
      } 
    }
  }
  
  private class PopupTouchInterceptor implements View.OnTouchListener {
    private PopupTouchInterceptor() {}
    
    public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
      int i = param1MotionEvent.getAction();
      int j = (int)param1MotionEvent.getX();
      int k = (int)param1MotionEvent.getY();
      if (i == 0 && ListPopupWindow.this.mPopup != null && ListPopupWindow.this.mPopup.isShowing() && j >= 0 && j < ListPopupWindow.this.mPopup.getWidth() && k >= 0 && k < ListPopupWindow.this.mPopup.getHeight()) {
        ListPopupWindow.this.mHandler.postDelayed(ListPopupWindow.this.mResizePopupRunnable, 250L);
        return false;
      } 
      if (i == 1)
        ListPopupWindow.this.mHandler.removeCallbacks(ListPopupWindow.this.mResizePopupRunnable); 
      return false;
    }
  }
  
  private class ResizePopupRunnable implements Runnable {
    private ResizePopupRunnable() {}
    
    public void run() {
      if (ListPopupWindow.this.mDropDownList != null && ViewCompat.isAttachedToWindow((View)ListPopupWindow.this.mDropDownList) && ListPopupWindow.this.mDropDownList.getCount() > ListPopupWindow.this.mDropDownList.getChildCount() && ListPopupWindow.this.mDropDownList.getChildCount() <= ListPopupWindow.this.mListItemExpandMaximum) {
        ListPopupWindow.this.mPopup.setInputMethodMode(2);
        ListPopupWindow.this.show();
      } 
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\ListPopupWindow.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */