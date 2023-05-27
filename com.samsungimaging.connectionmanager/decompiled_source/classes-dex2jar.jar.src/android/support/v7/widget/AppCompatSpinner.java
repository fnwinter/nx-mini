package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.view.TintableBackgroundView;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.ThemedSpinnerAdapter;

public class AppCompatSpinner extends Spinner implements TintableBackgroundView {
  private static final int[] ATTRS_ANDROID_SPINNERMODE;
  
  private static final boolean IS_AT_LEAST_JB;
  
  private static final boolean IS_AT_LEAST_M;
  
  private static final int MAX_ITEMS_MEASURED = 15;
  
  private static final int MODE_DIALOG = 0;
  
  private static final int MODE_DROPDOWN = 1;
  
  private static final int MODE_THEME = -1;
  
  private static final String TAG = "AppCompatSpinner";
  
  private AppCompatBackgroundHelper mBackgroundTintHelper;
  
  private int mDropDownWidth;
  
  private ListPopupWindow.ForwardingListener mForwardingListener;
  
  private DropdownPopup mPopup;
  
  private Context mPopupContext;
  
  private boolean mPopupSet;
  
  private SpinnerAdapter mTempAdapter;
  
  private final Rect mTempRect;
  
  private TintManager mTintManager;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 23) {
      bool = true;
    } else {
      bool = false;
    } 
    IS_AT_LEAST_M = bool;
    if (Build.VERSION.SDK_INT >= 16) {
      bool = true;
    } else {
      bool = false;
    } 
    IS_AT_LEAST_JB = bool;
    ATTRS_ANDROID_SPINNERMODE = new int[] { 16843505 };
  }
  
  public AppCompatSpinner(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public AppCompatSpinner(Context paramContext, int paramInt) {
    this(paramContext, (AttributeSet)null, R.attr.spinnerStyle, paramInt);
  }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.spinnerStyle);
  }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    this(paramContext, paramAttributeSet, paramInt, -1);
  }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2) {
    this(paramContext, paramAttributeSet, paramInt1, paramInt2, (Resources.Theme)null);
  }
  
  public AppCompatSpinner(Context paramContext, AttributeSet paramAttributeSet, int paramInt1, int paramInt2, Resources.Theme paramTheme) {
    // Byte code:
    //   0: aload_0
    //   1: aload_1
    //   2: aload_2
    //   3: iload_3
    //   4: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;I)V
    //   7: aload_0
    //   8: new android/graphics/Rect
    //   11: dup
    //   12: invokespecial <init> : ()V
    //   15: putfield mTempRect : Landroid/graphics/Rect;
    //   18: aload_1
    //   19: aload_2
    //   20: getstatic android/support/v7/appcompat/R$styleable.Spinner : [I
    //   23: iload_3
    //   24: iconst_0
    //   25: invokestatic obtainStyledAttributes : (Landroid/content/Context;Landroid/util/AttributeSet;[III)Landroid/support/v7/widget/TintTypedArray;
    //   28: astore #9
    //   30: aload_0
    //   31: aload #9
    //   33: invokevirtual getTintManager : ()Landroid/support/v7/widget/TintManager;
    //   36: putfield mTintManager : Landroid/support/v7/widget/TintManager;
    //   39: aload_0
    //   40: new android/support/v7/widget/AppCompatBackgroundHelper
    //   43: dup
    //   44: aload_0
    //   45: aload_0
    //   46: getfield mTintManager : Landroid/support/v7/widget/TintManager;
    //   49: invokespecial <init> : (Landroid/view/View;Landroid/support/v7/widget/TintManager;)V
    //   52: putfield mBackgroundTintHelper : Landroid/support/v7/widget/AppCompatBackgroundHelper;
    //   55: aload #5
    //   57: ifnull -> 302
    //   60: aload_0
    //   61: new android/support/v7/view/ContextThemeWrapper
    //   64: dup
    //   65: aload_1
    //   66: aload #5
    //   68: invokespecial <init> : (Landroid/content/Context;Landroid/content/res/Resources$Theme;)V
    //   71: putfield mPopupContext : Landroid/content/Context;
    //   74: aload_0
    //   75: getfield mPopupContext : Landroid/content/Context;
    //   78: ifnull -> 262
    //   81: iload #4
    //   83: istore #7
    //   85: iload #4
    //   87: iconst_m1
    //   88: if_icmpne -> 164
    //   91: getstatic android/os/Build$VERSION.SDK_INT : I
    //   94: bipush #11
    //   96: if_icmplt -> 407
    //   99: aconst_null
    //   100: astore #8
    //   102: aconst_null
    //   103: astore #5
    //   105: aload_1
    //   106: aload_2
    //   107: getstatic android/support/v7/widget/AppCompatSpinner.ATTRS_ANDROID_SPINNERMODE : [I
    //   110: iload_3
    //   111: iconst_0
    //   112: invokevirtual obtainStyledAttributes : (Landroid/util/AttributeSet;[III)Landroid/content/res/TypedArray;
    //   115: astore_1
    //   116: iload #4
    //   118: istore #6
    //   120: aload_1
    //   121: astore #5
    //   123: aload_1
    //   124: astore #8
    //   126: aload_1
    //   127: iconst_0
    //   128: invokevirtual hasValue : (I)Z
    //   131: ifeq -> 148
    //   134: aload_1
    //   135: astore #5
    //   137: aload_1
    //   138: astore #8
    //   140: aload_1
    //   141: iconst_0
    //   142: iconst_0
    //   143: invokevirtual getInt : (II)I
    //   146: istore #6
    //   148: iload #6
    //   150: istore #7
    //   152: aload_1
    //   153: ifnull -> 164
    //   156: aload_1
    //   157: invokevirtual recycle : ()V
    //   160: iload #6
    //   162: istore #7
    //   164: iload #7
    //   166: iconst_1
    //   167: if_icmpne -> 262
    //   170: new android/support/v7/widget/AppCompatSpinner$DropdownPopup
    //   173: dup
    //   174: aload_0
    //   175: aload_0
    //   176: getfield mPopupContext : Landroid/content/Context;
    //   179: aload_2
    //   180: iload_3
    //   181: invokespecial <init> : (Landroid/support/v7/widget/AppCompatSpinner;Landroid/content/Context;Landroid/util/AttributeSet;I)V
    //   184: astore_1
    //   185: aload_0
    //   186: getfield mPopupContext : Landroid/content/Context;
    //   189: aload_2
    //   190: getstatic android/support/v7/appcompat/R$styleable.Spinner : [I
    //   193: iload_3
    //   194: iconst_0
    //   195: invokestatic obtainStyledAttributes : (Landroid/content/Context;Landroid/util/AttributeSet;[III)Landroid/support/v7/widget/TintTypedArray;
    //   198: astore #5
    //   200: aload_0
    //   201: aload #5
    //   203: getstatic android/support/v7/appcompat/R$styleable.Spinner_android_dropDownWidth : I
    //   206: bipush #-2
    //   208: invokevirtual getLayoutDimension : (II)I
    //   211: putfield mDropDownWidth : I
    //   214: aload_1
    //   215: aload #5
    //   217: getstatic android/support/v7/appcompat/R$styleable.Spinner_android_popupBackground : I
    //   220: invokevirtual getDrawable : (I)Landroid/graphics/drawable/Drawable;
    //   223: invokevirtual setBackgroundDrawable : (Landroid/graphics/drawable/Drawable;)V
    //   226: aload_1
    //   227: aload #9
    //   229: getstatic android/support/v7/appcompat/R$styleable.Spinner_android_prompt : I
    //   232: invokevirtual getString : (I)Ljava/lang/String;
    //   235: invokevirtual setPromptText : (Ljava/lang/CharSequence;)V
    //   238: aload #5
    //   240: invokevirtual recycle : ()V
    //   243: aload_0
    //   244: aload_1
    //   245: putfield mPopup : Landroid/support/v7/widget/AppCompatSpinner$DropdownPopup;
    //   248: aload_0
    //   249: new android/support/v7/widget/AppCompatSpinner$1
    //   252: dup
    //   253: aload_0
    //   254: aload_0
    //   255: aload_1
    //   256: invokespecial <init> : (Landroid/support/v7/widget/AppCompatSpinner;Landroid/view/View;Landroid/support/v7/widget/AppCompatSpinner$DropdownPopup;)V
    //   259: putfield mForwardingListener : Landroid/support/v7/widget/ListPopupWindow$ForwardingListener;
    //   262: aload #9
    //   264: invokevirtual recycle : ()V
    //   267: aload_0
    //   268: iconst_1
    //   269: putfield mPopupSet : Z
    //   272: aload_0
    //   273: getfield mTempAdapter : Landroid/widget/SpinnerAdapter;
    //   276: ifnull -> 292
    //   279: aload_0
    //   280: aload_0
    //   281: getfield mTempAdapter : Landroid/widget/SpinnerAdapter;
    //   284: invokevirtual setAdapter : (Landroid/widget/SpinnerAdapter;)V
    //   287: aload_0
    //   288: aconst_null
    //   289: putfield mTempAdapter : Landroid/widget/SpinnerAdapter;
    //   292: aload_0
    //   293: getfield mBackgroundTintHelper : Landroid/support/v7/widget/AppCompatBackgroundHelper;
    //   296: aload_2
    //   297: iload_3
    //   298: invokevirtual loadFromAttributes : (Landroid/util/AttributeSet;I)V
    //   301: return
    //   302: aload #9
    //   304: getstatic android/support/v7/appcompat/R$styleable.Spinner_popupTheme : I
    //   307: iconst_0
    //   308: invokevirtual getResourceId : (II)I
    //   311: istore #6
    //   313: iload #6
    //   315: ifeq -> 335
    //   318: aload_0
    //   319: new android/support/v7/view/ContextThemeWrapper
    //   322: dup
    //   323: aload_1
    //   324: iload #6
    //   326: invokespecial <init> : (Landroid/content/Context;I)V
    //   329: putfield mPopupContext : Landroid/content/Context;
    //   332: goto -> 74
    //   335: getstatic android/support/v7/widget/AppCompatSpinner.IS_AT_LEAST_M : Z
    //   338: ifne -> 353
    //   341: aload_1
    //   342: astore #5
    //   344: aload_0
    //   345: aload #5
    //   347: putfield mPopupContext : Landroid/content/Context;
    //   350: goto -> 74
    //   353: aconst_null
    //   354: astore #5
    //   356: goto -> 344
    //   359: astore_1
    //   360: aload #5
    //   362: astore #8
    //   364: ldc 'AppCompatSpinner'
    //   366: ldc 'Could not read android:spinnerMode'
    //   368: aload_1
    //   369: invokestatic i : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   372: pop
    //   373: iload #4
    //   375: istore #7
    //   377: aload #5
    //   379: ifnull -> 164
    //   382: aload #5
    //   384: invokevirtual recycle : ()V
    //   387: iload #4
    //   389: istore #7
    //   391: goto -> 164
    //   394: astore_1
    //   395: aload #8
    //   397: ifnull -> 405
    //   400: aload #8
    //   402: invokevirtual recycle : ()V
    //   405: aload_1
    //   406: athrow
    //   407: iconst_1
    //   408: istore #7
    //   410: goto -> 164
    // Exception table:
    //   from	to	target	type
    //   105	116	359	java/lang/Exception
    //   105	116	394	finally
    //   126	134	359	java/lang/Exception
    //   126	134	394	finally
    //   140	148	359	java/lang/Exception
    //   140	148	394	finally
    //   364	373	394	finally
  }
  
  private int compatMeasureContentWidth(SpinnerAdapter paramSpinnerAdapter, Drawable paramDrawable) {
    if (paramSpinnerAdapter == null)
      return 0; 
    int i = 0;
    View view = null;
    int k = 0;
    int m = View.MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 0);
    int n = View.MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 0);
    int j = Math.max(0, getSelectedItemPosition());
    int i1 = Math.min(paramSpinnerAdapter.getCount(), j + 15);
    j = Math.max(0, j - 15 - i1 - j);
    while (j < i1) {
      int i3 = paramSpinnerAdapter.getItemViewType(j);
      int i2 = k;
      if (i3 != k) {
        i2 = i3;
        view = null;
      } 
      view = paramSpinnerAdapter.getView(j, view, (ViewGroup)this);
      if (view.getLayoutParams() == null)
        view.setLayoutParams(new ViewGroup.LayoutParams(-2, -2)); 
      view.measure(m, n);
      i = Math.max(i, view.getMeasuredWidth());
      j++;
      k = i2;
    } 
    j = i;
    if (paramDrawable != null) {
      paramDrawable.getPadding(this.mTempRect);
      return i + this.mTempRect.left + this.mTempRect.right;
    } 
    return j;
  }
  
  protected void drawableStateChanged() {
    super.drawableStateChanged();
    if (this.mBackgroundTintHelper != null)
      this.mBackgroundTintHelper.applySupportBackgroundTint(); 
  }
  
  public int getDropDownHorizontalOffset() {
    return (this.mPopup != null) ? this.mPopup.getHorizontalOffset() : (IS_AT_LEAST_JB ? super.getDropDownHorizontalOffset() : 0);
  }
  
  public int getDropDownVerticalOffset() {
    return (this.mPopup != null) ? this.mPopup.getVerticalOffset() : (IS_AT_LEAST_JB ? super.getDropDownVerticalOffset() : 0);
  }
  
  public int getDropDownWidth() {
    return (this.mPopup != null) ? this.mDropDownWidth : (IS_AT_LEAST_JB ? super.getDropDownWidth() : 0);
  }
  
  public Drawable getPopupBackground() {
    return (this.mPopup != null) ? this.mPopup.getBackground() : (IS_AT_LEAST_JB ? super.getPopupBackground() : null);
  }
  
  public Context getPopupContext() {
    return (this.mPopup != null) ? this.mPopupContext : (IS_AT_LEAST_M ? super.getPopupContext() : null);
  }
  
  public CharSequence getPrompt() {
    return (this.mPopup != null) ? this.mPopup.getHintText() : super.getPrompt();
  }
  
  @Nullable
  public ColorStateList getSupportBackgroundTintList() {
    return (this.mBackgroundTintHelper != null) ? this.mBackgroundTintHelper.getSupportBackgroundTintList() : null;
  }
  
  @Nullable
  public PorterDuff.Mode getSupportBackgroundTintMode() {
    return (this.mBackgroundTintHelper != null) ? this.mBackgroundTintHelper.getSupportBackgroundTintMode() : null;
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    if (this.mPopup != null && this.mPopup.isShowing())
      this.mPopup.dismiss(); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    if (this.mPopup != null && View.MeasureSpec.getMode(paramInt1) == Integer.MIN_VALUE)
      setMeasuredDimension(Math.min(Math.max(getMeasuredWidth(), compatMeasureContentWidth(getAdapter(), getBackground())), View.MeasureSpec.getSize(paramInt1)), getMeasuredHeight()); 
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    return (this.mForwardingListener != null && this.mForwardingListener.onTouch((View)this, paramMotionEvent)) ? true : super.onTouchEvent(paramMotionEvent);
  }
  
  public boolean performClick() {
    if (this.mPopup != null && !this.mPopup.isShowing()) {
      this.mPopup.show();
      return true;
    } 
    return super.performClick();
  }
  
  public void setAdapter(SpinnerAdapter paramSpinnerAdapter) {
    if (!this.mPopupSet) {
      this.mTempAdapter = paramSpinnerAdapter;
      return;
    } 
    super.setAdapter(paramSpinnerAdapter);
    if (this.mPopup != null) {
      Context context;
      if (this.mPopupContext == null) {
        context = getContext();
      } else {
        context = this.mPopupContext;
      } 
      this.mPopup.setAdapter(new DropDownAdapter(paramSpinnerAdapter, context.getTheme()));
      return;
    } 
  }
  
  public void setBackgroundDrawable(Drawable paramDrawable) {
    super.setBackgroundDrawable(paramDrawable);
    if (this.mBackgroundTintHelper != null)
      this.mBackgroundTintHelper.onSetBackgroundDrawable(paramDrawable); 
  }
  
  public void setBackgroundResource(@DrawableRes int paramInt) {
    super.setBackgroundResource(paramInt);
    if (this.mBackgroundTintHelper != null)
      this.mBackgroundTintHelper.onSetBackgroundResource(paramInt); 
  }
  
  public void setDropDownHorizontalOffset(int paramInt) {
    if (this.mPopup != null) {
      this.mPopup.setHorizontalOffset(paramInt);
      return;
    } 
    if (IS_AT_LEAST_JB) {
      super.setDropDownHorizontalOffset(paramInt);
      return;
    } 
  }
  
  public void setDropDownVerticalOffset(int paramInt) {
    if (this.mPopup != null) {
      this.mPopup.setVerticalOffset(paramInt);
      return;
    } 
    if (IS_AT_LEAST_JB) {
      super.setDropDownVerticalOffset(paramInt);
      return;
    } 
  }
  
  public void setDropDownWidth(int paramInt) {
    if (this.mPopup != null) {
      this.mDropDownWidth = paramInt;
      return;
    } 
    if (IS_AT_LEAST_JB) {
      super.setDropDownWidth(paramInt);
      return;
    } 
  }
  
  public void setPopupBackgroundDrawable(Drawable paramDrawable) {
    if (this.mPopup != null) {
      this.mPopup.setBackgroundDrawable(paramDrawable);
      return;
    } 
    if (IS_AT_LEAST_JB) {
      super.setPopupBackgroundDrawable(paramDrawable);
      return;
    } 
  }
  
  public void setPopupBackgroundResource(@DrawableRes int paramInt) {
    setPopupBackgroundDrawable(getPopupContext().getDrawable(paramInt));
  }
  
  public void setPrompt(CharSequence paramCharSequence) {
    if (this.mPopup != null) {
      this.mPopup.setPromptText(paramCharSequence);
      return;
    } 
    super.setPrompt(paramCharSequence);
  }
  
  public void setSupportBackgroundTintList(@Nullable ColorStateList paramColorStateList) {
    if (this.mBackgroundTintHelper != null)
      this.mBackgroundTintHelper.setSupportBackgroundTintList(paramColorStateList); 
  }
  
  public void setSupportBackgroundTintMode(@Nullable PorterDuff.Mode paramMode) {
    if (this.mBackgroundTintHelper != null)
      this.mBackgroundTintHelper.setSupportBackgroundTintMode(paramMode); 
  }
  
  private static class DropDownAdapter implements ListAdapter, SpinnerAdapter {
    private SpinnerAdapter mAdapter;
    
    private ListAdapter mListAdapter;
    
    public DropDownAdapter(@Nullable SpinnerAdapter param1SpinnerAdapter, @Nullable Resources.Theme param1Theme) {
      ThemedSpinnerAdapter themedSpinnerAdapter;
      this.mAdapter = param1SpinnerAdapter;
      if (param1SpinnerAdapter instanceof ListAdapter)
        this.mListAdapter = (ListAdapter)param1SpinnerAdapter; 
      if (param1Theme != null) {
        if (AppCompatSpinner.IS_AT_LEAST_M && param1SpinnerAdapter instanceof ThemedSpinnerAdapter) {
          themedSpinnerAdapter = (ThemedSpinnerAdapter)param1SpinnerAdapter;
          if (themedSpinnerAdapter.getDropDownViewTheme() != param1Theme)
            themedSpinnerAdapter.setDropDownViewTheme(param1Theme); 
          return;
        } 
      } else {
        return;
      } 
      if (themedSpinnerAdapter instanceof ThemedSpinnerAdapter) {
        ThemedSpinnerAdapter themedSpinnerAdapter1 = (ThemedSpinnerAdapter)themedSpinnerAdapter;
        if (themedSpinnerAdapter1.getDropDownViewTheme() == null) {
          themedSpinnerAdapter1.setDropDownViewTheme(param1Theme);
          return;
        } 
      } 
    }
    
    public boolean areAllItemsEnabled() {
      ListAdapter listAdapter = this.mListAdapter;
      return (listAdapter != null) ? listAdapter.areAllItemsEnabled() : true;
    }
    
    public int getCount() {
      return (this.mAdapter == null) ? 0 : this.mAdapter.getCount();
    }
    
    public View getDropDownView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      return (this.mAdapter == null) ? null : this.mAdapter.getDropDownView(param1Int, param1View, param1ViewGroup);
    }
    
    public Object getItem(int param1Int) {
      return (this.mAdapter == null) ? null : this.mAdapter.getItem(param1Int);
    }
    
    public long getItemId(int param1Int) {
      return (this.mAdapter == null) ? -1L : this.mAdapter.getItemId(param1Int);
    }
    
    public int getItemViewType(int param1Int) {
      return 0;
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      return getDropDownView(param1Int, param1View, param1ViewGroup);
    }
    
    public int getViewTypeCount() {
      return 1;
    }
    
    public boolean hasStableIds() {
      return (this.mAdapter != null && this.mAdapter.hasStableIds());
    }
    
    public boolean isEmpty() {
      return (getCount() == 0);
    }
    
    public boolean isEnabled(int param1Int) {
      ListAdapter listAdapter = this.mListAdapter;
      return (listAdapter != null) ? listAdapter.isEnabled(param1Int) : true;
    }
    
    public void registerDataSetObserver(DataSetObserver param1DataSetObserver) {
      if (this.mAdapter != null)
        this.mAdapter.registerDataSetObserver(param1DataSetObserver); 
    }
    
    public void unregisterDataSetObserver(DataSetObserver param1DataSetObserver) {
      if (this.mAdapter != null)
        this.mAdapter.unregisterDataSetObserver(param1DataSetObserver); 
    }
  }
  
  private class DropdownPopup extends ListPopupWindow {
    private ListAdapter mAdapter;
    
    private CharSequence mHintText;
    
    private final Rect mVisibleRect = new Rect();
    
    public DropdownPopup(Context param1Context, AttributeSet param1AttributeSet, int param1Int) {
      super(param1Context, param1AttributeSet, param1Int);
      setAnchorView((View)AppCompatSpinner.this);
      setModal(true);
      setPromptPosition(0);
      setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> param2AdapterView, View param2View, int param2Int, long param2Long) {
              AppCompatSpinner.this.setSelection(param2Int);
              if (AppCompatSpinner.this.getOnItemClickListener() != null)
                AppCompatSpinner.this.performItemClick(param2View, param2Int, AppCompatSpinner.DropdownPopup.this.mAdapter.getItemId(param2Int)); 
              AppCompatSpinner.DropdownPopup.this.dismiss();
            }
          });
    }
    
    private boolean isVisibleToUser(View param1View) {
      return (ViewCompat.isAttachedToWindow(param1View) && param1View.getGlobalVisibleRect(this.mVisibleRect));
    }
    
    void computeContentWidth() {
      Drawable drawable = getBackground();
      int i = 0;
      if (drawable != null) {
        drawable.getPadding(AppCompatSpinner.this.mTempRect);
        if (ViewUtils.isLayoutRtl((View)AppCompatSpinner.this)) {
          i = AppCompatSpinner.this.mTempRect.right;
        } else {
          i = -AppCompatSpinner.this.mTempRect.left;
        } 
      } else {
        Rect rect = AppCompatSpinner.this.mTempRect;
        AppCompatSpinner.this.mTempRect.right = 0;
        rect.left = 0;
      } 
      int j = AppCompatSpinner.this.getPaddingLeft();
      int k = AppCompatSpinner.this.getPaddingRight();
      int m = AppCompatSpinner.this.getWidth();
      if (AppCompatSpinner.this.mDropDownWidth == -2) {
        int i1 = AppCompatSpinner.this.compatMeasureContentWidth((SpinnerAdapter)this.mAdapter, getBackground());
        int i2 = (AppCompatSpinner.this.getContext().getResources().getDisplayMetrics()).widthPixels - AppCompatSpinner.this.mTempRect.left - AppCompatSpinner.this.mTempRect.right;
        int n = i1;
        if (i1 > i2)
          n = i2; 
        setContentWidth(Math.max(n, m - j - k));
      } else if (AppCompatSpinner.this.mDropDownWidth == -1) {
        setContentWidth(m - j - k);
      } else {
        setContentWidth(AppCompatSpinner.this.mDropDownWidth);
      } 
      if (ViewUtils.isLayoutRtl((View)AppCompatSpinner.this)) {
        i += m - k - getWidth();
      } else {
        i += j;
      } 
      setHorizontalOffset(i);
    }
    
    public CharSequence getHintText() {
      return this.mHintText;
    }
    
    public void setAdapter(ListAdapter param1ListAdapter) {
      super.setAdapter(param1ListAdapter);
      this.mAdapter = param1ListAdapter;
    }
    
    public void setPromptText(CharSequence param1CharSequence) {
      this.mHintText = param1CharSequence;
    }
    
    public void show() {
      boolean bool = isShowing();
      computeContentWidth();
      setInputMethodMode(2);
      super.show();
      getListView().setChoiceMode(1);
      setSelection(AppCompatSpinner.this.getSelectedItemPosition());
      if (!bool) {
        ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
        if (viewTreeObserver != null) {
          final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
              public void onGlobalLayout() {
                if (!AppCompatSpinner.DropdownPopup.this.isVisibleToUser((View)AppCompatSpinner.this)) {
                  AppCompatSpinner.DropdownPopup.this.dismiss();
                  return;
                } 
                AppCompatSpinner.DropdownPopup.this.computeContentWidth();
                AppCompatSpinner.DropdownPopup.this.show();
              }
            };
          viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener);
          setOnDismissListener(new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                  ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
                  if (viewTreeObserver != null)
                    viewTreeObserver.removeGlobalOnLayoutListener(layoutListener); 
                }
              });
          return;
        } 
      } 
    }
  }
  
  class null implements AdapterView.OnItemClickListener {
    public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
      AppCompatSpinner.this.setSelection(param1Int);
      if (AppCompatSpinner.this.getOnItemClickListener() != null)
        AppCompatSpinner.this.performItemClick(param1View, param1Int, this.this$1.mAdapter.getItemId(param1Int)); 
      this.this$1.dismiss();
    }
  }
  
  class null implements ViewTreeObserver.OnGlobalLayoutListener {
    public void onGlobalLayout() {
      if (!this.this$1.isVisibleToUser((View)AppCompatSpinner.this)) {
        this.this$1.dismiss();
        return;
      } 
      this.this$1.computeContentWidth();
      this.this$1.show();
    }
  }
  
  class null implements PopupWindow.OnDismissListener {
    public void onDismiss() {
      ViewTreeObserver viewTreeObserver = AppCompatSpinner.this.getViewTreeObserver();
      if (viewTreeObserver != null)
        viewTreeObserver.removeGlobalOnLayoutListener(layoutListener); 
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\AppCompatSpinner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */