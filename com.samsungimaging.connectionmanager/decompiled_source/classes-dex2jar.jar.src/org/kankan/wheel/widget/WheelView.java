package org.kankan.wheel.widget;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.kankan.wheel.widget.adapters.WheelViewAdapter;

public class WheelView extends View {
  private static final int DEF_VISIBLE_ITEMS = 5;
  
  private static final int ITEM_OFFSET_PERCENT = 10;
  
  private static final int PADDING = 10;
  
  private static final int[] SHADOWS_COLORS = new int[] { -15658735, 11184810, 11184810 };
  
  private Drawable centerDrawable;
  
  private List<OnWheelChangedListener> changingListeners = new LinkedList<OnWheelChangedListener>();
  
  private List<OnWheelClickedListener> clickingListeners = new LinkedList<OnWheelClickedListener>();
  
  private int currentItem = 0;
  
  private DataSetObserver dataObserver = new DataSetObserver() {
      public void onChanged() {
        WheelView.this.invalidateWheel(false);
      }
      
      public void onInvalidated() {
        WheelView.this.invalidateWheel(true);
      }
    };
  
  private int firstItem;
  
  boolean isCyclic = false;
  
  private boolean isScrollingPerformed;
  
  private int itemWidth = 0;
  
  private LinearLayout itemsLayout;
  
  private GradientDrawable leftShadow;
  
  private WheelRecycle recycle = new WheelRecycle(this);
  
  private GradientDrawable rightShadow;
  
  private WheelScroller scroller;
  
  WheelScroller.ScrollingListener scrollingListener = new WheelScroller.ScrollingListener() {
      public void onFinished() {
        if (WheelView.this.isScrollingPerformed) {
          WheelView.this.notifyScrollingListenersAboutEnd();
          WheelView.this.isScrollingPerformed = false;
        } 
        WheelView.this.scrollingOffset = 0;
        WheelView.this.invalidate();
      }
      
      public void onJustify() {
        if (Math.abs(WheelView.this.scrollingOffset) > 1)
          WheelView.this.scroller.scroll(WheelView.this.scrollingOffset, 0); 
      }
      
      public void onScroll(int param1Int) {
        WheelView.this.doScroll(param1Int);
        param1Int = WheelView.this.getWidth();
        if (WheelView.this.scrollingOffset > param1Int) {
          WheelView.this.scrollingOffset = param1Int;
          WheelView.this.scroller.stopScrolling();
          return;
        } 
        if (WheelView.this.scrollingOffset < -param1Int) {
          WheelView.this.scrollingOffset = -param1Int;
          WheelView.this.scroller.stopScrolling();
          return;
        } 
      }
      
      public void onStarted() {
        WheelView.this.isScrollingPerformed = true;
        WheelView.this.notifyScrollingListenersAboutStart();
      }
    };
  
  private List<OnWheelScrollListener> scrollingListeners = new LinkedList<OnWheelScrollListener>();
  
  private int scrollingOffset;
  
  private WheelViewAdapter viewAdapter;
  
  private int visibleItems = 5;
  
  public WheelView(Context paramContext) {
    super(paramContext);
    initData(paramContext);
  }
  
  public WheelView(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    initData(paramContext);
  }
  
  public WheelView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    initData(paramContext);
  }
  
  private boolean addViewItem(int paramInt, boolean paramBoolean) {
    boolean bool = false;
    View view = getItemView(paramInt);
    if (view != null) {
      if (paramBoolean) {
        this.itemsLayout.addView(view, 0);
      } else {
        this.itemsLayout.addView(view);
      } 
      bool = true;
    } 
    return bool;
  }
  
  private void buildViewForMeasuring() {
    if (this.itemsLayout != null) {
      this.recycle.recycleItems(this.itemsLayout, this.firstItem, new ItemsRange());
    } else {
      createItemsLayout();
    } 
    int j = this.visibleItems / 2;
    for (int i = this.currentItem + j;; i--) {
      if (i < this.currentItem - j)
        return; 
      if (addViewItem(i, true))
        this.firstItem = i; 
    } 
  }
  
  private int calculateLayoutHeight(int paramInt1, int paramInt2) {
    initResourcesIfNecessary();
    this.itemsLayout.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
    this.itemsLayout.measure(View.MeasureSpec.makeMeasureSpec(paramInt1, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
    int i = this.itemsLayout.getMeasuredHeight();
    if (paramInt2 == 1073741824) {
      i = paramInt1;
      this.itemsLayout.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(i - 20, 1073741824));
      return i;
    } 
    int j = Math.max(i + 20, getSuggestedMinimumHeight());
    i = j;
    if (paramInt2 == Integer.MIN_VALUE) {
      i = j;
      if (paramInt1 < j)
        i = paramInt1; 
    } 
    this.itemsLayout.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(i - 20, 1073741824));
    return i;
  }
  
  private void createItemsLayout() {
    if (this.itemsLayout == null) {
      this.itemsLayout = new LinearLayout(getContext());
      this.itemsLayout.setOrientation(0);
    } 
  }
  
  private void doScroll(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: aload_0
    //   2: getfield scrollingOffset : I
    //   5: iload_1
    //   6: iadd
    //   7: putfield scrollingOffset : I
    //   10: aload_0
    //   11: invokespecial getItemWidth : ()I
    //   14: istore #6
    //   16: aload_0
    //   17: getfield scrollingOffset : I
    //   20: iload #6
    //   22: idiv
    //   23: istore_3
    //   24: aload_0
    //   25: getfield currentItem : I
    //   28: iload_3
    //   29: isub
    //   30: istore #4
    //   32: aload_0
    //   33: getfield viewAdapter : Lorg/kankan/wheel/widget/adapters/WheelViewAdapter;
    //   36: invokeinterface getItemsCount : ()I
    //   41: istore #7
    //   43: aload_0
    //   44: getfield scrollingOffset : I
    //   47: iload #6
    //   49: irem
    //   50: istore_1
    //   51: iload_1
    //   52: istore #5
    //   54: iload_1
    //   55: invokestatic abs : (I)I
    //   58: iload #6
    //   60: iconst_2
    //   61: idiv
    //   62: if_icmpgt -> 68
    //   65: iconst_0
    //   66: istore #5
    //   68: aload_0
    //   69: getfield isCyclic : Z
    //   72: ifeq -> 192
    //   75: iload #7
    //   77: ifle -> 192
    //   80: iload #5
    //   82: ifle -> 162
    //   85: iload #4
    //   87: iconst_1
    //   88: isub
    //   89: istore_2
    //   90: iload_3
    //   91: iconst_1
    //   92: iadd
    //   93: istore_1
    //   94: iload_2
    //   95: iflt -> 184
    //   98: iload_2
    //   99: iload #7
    //   101: irem
    //   102: istore_2
    //   103: aload_0
    //   104: getfield scrollingOffset : I
    //   107: istore_3
    //   108: iload_2
    //   109: aload_0
    //   110: getfield currentItem : I
    //   113: if_icmpeq -> 290
    //   116: aload_0
    //   117: iload_2
    //   118: iconst_0
    //   119: invokevirtual setCurrentItem : (IZ)V
    //   122: aload_0
    //   123: iload_3
    //   124: iload_1
    //   125: iload #6
    //   127: imul
    //   128: isub
    //   129: putfield scrollingOffset : I
    //   132: aload_0
    //   133: getfield scrollingOffset : I
    //   136: aload_0
    //   137: invokevirtual getWidth : ()I
    //   140: if_icmple -> 161
    //   143: aload_0
    //   144: aload_0
    //   145: getfield scrollingOffset : I
    //   148: aload_0
    //   149: invokevirtual getWidth : ()I
    //   152: irem
    //   153: aload_0
    //   154: invokevirtual getWidth : ()I
    //   157: iadd
    //   158: putfield scrollingOffset : I
    //   161: return
    //   162: iload_3
    //   163: istore_1
    //   164: iload #4
    //   166: istore_2
    //   167: iload #5
    //   169: ifge -> 94
    //   172: iload #4
    //   174: iconst_1
    //   175: iadd
    //   176: istore_2
    //   177: iload_3
    //   178: iconst_1
    //   179: isub
    //   180: istore_1
    //   181: goto -> 94
    //   184: iload_2
    //   185: iload #7
    //   187: iadd
    //   188: istore_2
    //   189: goto -> 94
    //   192: iload #4
    //   194: ifge -> 207
    //   197: aload_0
    //   198: getfield currentItem : I
    //   201: istore_1
    //   202: iconst_0
    //   203: istore_2
    //   204: goto -> 103
    //   207: iload #4
    //   209: iload #7
    //   211: if_icmplt -> 232
    //   214: aload_0
    //   215: getfield currentItem : I
    //   218: iload #7
    //   220: isub
    //   221: iconst_1
    //   222: iadd
    //   223: istore_1
    //   224: iload #7
    //   226: iconst_1
    //   227: isub
    //   228: istore_2
    //   229: goto -> 103
    //   232: iload #4
    //   234: ifle -> 254
    //   237: iload #5
    //   239: ifle -> 254
    //   242: iload #4
    //   244: iconst_1
    //   245: isub
    //   246: istore_2
    //   247: iload_3
    //   248: iconst_1
    //   249: iadd
    //   250: istore_1
    //   251: goto -> 103
    //   254: iload_3
    //   255: istore_1
    //   256: iload #4
    //   258: istore_2
    //   259: iload #4
    //   261: iload #7
    //   263: iconst_1
    //   264: isub
    //   265: if_icmpge -> 103
    //   268: iload_3
    //   269: istore_1
    //   270: iload #4
    //   272: istore_2
    //   273: iload #5
    //   275: ifge -> 103
    //   278: iload #4
    //   280: iconst_1
    //   281: iadd
    //   282: istore_2
    //   283: iload_3
    //   284: iconst_1
    //   285: isub
    //   286: istore_1
    //   287: goto -> 103
    //   290: aload_0
    //   291: invokevirtual invalidate : ()V
    //   294: goto -> 122
  }
  
  private void drawCenterRect(Canvas paramCanvas) {
    int i = getWidth() / 2;
    int j = (int)((getItemWidth() / 2) * 1.2D);
    this.centerDrawable.setBounds(i - j, 0, i + j, getHeight());
    this.centerDrawable.draw(paramCanvas);
  }
  
  private void drawItems(Canvas paramCanvas) {
    paramCanvas.save();
    paramCanvas.translate((-((this.currentItem - this.firstItem) * getItemWidth() + (getItemWidth() - getWidth()) / 2) + this.scrollingOffset), 10.0F);
    this.itemsLayout.draw(paramCanvas);
    paramCanvas.restore();
  }
  
  private void drawShadows(Canvas paramCanvas) {
    int i = (int)(1.5D * getItemWidth());
    this.leftShadow.setBounds(0, 0, i, getHeight());
    this.leftShadow.draw(paramCanvas);
    this.rightShadow.setBounds(getWidth() - i, 0, getWidth(), getHeight());
    this.rightShadow.draw(paramCanvas);
  }
  
  private int getDesiredWidth(LinearLayout paramLinearLayout) {
    if (paramLinearLayout != null && paramLinearLayout.getChildAt(0) != null)
      this.itemWidth = paramLinearLayout.getChildAt(0).getMeasuredWidth(); 
    return Math.max(this.itemWidth * this.visibleItems - this.itemWidth * 10 / 50, getSuggestedMinimumWidth());
  }
  
  private View getItemView(int paramInt) {
    if (this.viewAdapter == null || this.viewAdapter.getItemsCount() == 0)
      return null; 
    int j = this.viewAdapter.getItemsCount();
    int i = paramInt;
    if (!isValidItemIndex(paramInt))
      return this.viewAdapter.getEmptyItem(this.recycle.getEmptyItem(), (ViewGroup)this.itemsLayout); 
    while (i < 0)
      i += j; 
    return this.viewAdapter.getItem(i % j, this.recycle.getItem(), (ViewGroup)this.itemsLayout);
  }
  
  private int getItemWidth() {
    if (this.itemWidth != 0)
      return this.itemWidth; 
    if (this.itemsLayout != null && this.itemsLayout.getChildAt(0) != null) {
      this.itemWidth = this.itemsLayout.getChildAt(0).getWidth();
      return this.itemWidth;
    } 
    return getWidth() / this.visibleItems;
  }
  
  private ItemsRange getItemsRange() {
    if (getItemWidth() == 0)
      return null; 
    int i = this.currentItem;
    for (int j = 1;; j += 2) {
      if (getItemWidth() * j >= getWidth()) {
        int m = j;
        int k = i;
        if (this.scrollingOffset != 0) {
          k = i;
          if (this.scrollingOffset > 0)
            k = i - 1; 
          i = this.scrollingOffset / getItemWidth();
          k -= i;
          m = (int)((j + 1) + Math.asin(i));
        } 
        return new ItemsRange(k, m);
      } 
      i--;
    } 
  }
  
  private void initData(Context paramContext) {
    this.scroller = new WheelScroller(getContext(), this.scrollingListener);
  }
  
  private void initResourcesIfNecessary() {
    if (this.centerDrawable == null)
      this.centerDrawable = getContext().getResources().getDrawable(2130838204); 
    if (this.leftShadow == null)
      this.leftShadow = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, SHADOWS_COLORS); 
    if (this.rightShadow == null)
      this.rightShadow = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, SHADOWS_COLORS); 
  }
  
  private boolean isValidItemIndex(int paramInt) {
    return (this.viewAdapter != null && this.viewAdapter.getItemsCount() > 0 && (this.isCyclic || (paramInt >= 0 && paramInt < this.viewAdapter.getItemsCount())));
  }
  
  private void layout(int paramInt1, int paramInt2) {
    this.itemsLayout.layout(0, 0, paramInt1, paramInt2 - 20);
  }
  
  private boolean rebuildItems() {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial getItemsRange : ()Lorg/kankan/wheel/widget/ItemsRange;
    //   4: astore #6
    //   6: aload_0
    //   7: getfield itemsLayout : Landroid/widget/LinearLayout;
    //   10: ifnull -> 162
    //   13: aload_0
    //   14: getfield recycle : Lorg/kankan/wheel/widget/WheelRecycle;
    //   17: aload_0
    //   18: getfield itemsLayout : Landroid/widget/LinearLayout;
    //   21: aload_0
    //   22: getfield firstItem : I
    //   25: aload #6
    //   27: invokevirtual recycleItems : (Landroid/widget/LinearLayout;ILorg/kankan/wheel/widget/ItemsRange;)I
    //   30: istore_1
    //   31: aload_0
    //   32: getfield firstItem : I
    //   35: iload_1
    //   36: if_icmpeq -> 156
    //   39: iconst_1
    //   40: istore #4
    //   42: aload_0
    //   43: iload_1
    //   44: putfield firstItem : I
    //   47: iload #4
    //   49: istore #5
    //   51: iload #4
    //   53: ifne -> 86
    //   56: aload_0
    //   57: getfield firstItem : I
    //   60: aload #6
    //   62: invokevirtual getFirst : ()I
    //   65: if_icmpne -> 172
    //   68: aload_0
    //   69: getfield itemsLayout : Landroid/widget/LinearLayout;
    //   72: invokevirtual getChildCount : ()I
    //   75: aload #6
    //   77: invokevirtual getCount : ()I
    //   80: if_icmpne -> 172
    //   83: iconst_0
    //   84: istore #5
    //   86: aload_0
    //   87: getfield firstItem : I
    //   90: aload #6
    //   92: invokevirtual getFirst : ()I
    //   95: if_icmple -> 199
    //   98: aload_0
    //   99: getfield firstItem : I
    //   102: aload #6
    //   104: invokevirtual getLast : ()I
    //   107: if_icmpgt -> 199
    //   110: aload_0
    //   111: getfield firstItem : I
    //   114: iconst_1
    //   115: isub
    //   116: istore_1
    //   117: iload_1
    //   118: aload #6
    //   120: invokevirtual getFirst : ()I
    //   123: if_icmpge -> 178
    //   126: aload_0
    //   127: getfield firstItem : I
    //   130: istore_2
    //   131: aload_0
    //   132: getfield itemsLayout : Landroid/widget/LinearLayout;
    //   135: invokevirtual getChildCount : ()I
    //   138: istore_1
    //   139: iload_1
    //   140: aload #6
    //   142: invokevirtual getCount : ()I
    //   145: if_icmplt -> 211
    //   148: aload_0
    //   149: iload_2
    //   150: putfield firstItem : I
    //   153: iload #5
    //   155: ireturn
    //   156: iconst_0
    //   157: istore #4
    //   159: goto -> 42
    //   162: aload_0
    //   163: invokespecial createItemsLayout : ()V
    //   166: iconst_1
    //   167: istore #4
    //   169: goto -> 47
    //   172: iconst_1
    //   173: istore #5
    //   175: goto -> 86
    //   178: aload_0
    //   179: iload_1
    //   180: iconst_1
    //   181: invokespecial addViewItem : (IZ)Z
    //   184: ifeq -> 126
    //   187: aload_0
    //   188: iload_1
    //   189: putfield firstItem : I
    //   192: iload_1
    //   193: iconst_1
    //   194: isub
    //   195: istore_1
    //   196: goto -> 117
    //   199: aload_0
    //   200: aload #6
    //   202: invokevirtual getFirst : ()I
    //   205: putfield firstItem : I
    //   208: goto -> 126
    //   211: iload_2
    //   212: istore_3
    //   213: aload_0
    //   214: aload_0
    //   215: getfield firstItem : I
    //   218: iload_1
    //   219: iadd
    //   220: iconst_0
    //   221: invokespecial addViewItem : (IZ)Z
    //   224: ifne -> 243
    //   227: iload_2
    //   228: istore_3
    //   229: aload_0
    //   230: getfield itemsLayout : Landroid/widget/LinearLayout;
    //   233: invokevirtual getChildCount : ()I
    //   236: ifne -> 243
    //   239: iload_2
    //   240: iconst_1
    //   241: iadd
    //   242: istore_3
    //   243: iload_1
    //   244: iconst_1
    //   245: iadd
    //   246: istore_1
    //   247: iload_3
    //   248: istore_2
    //   249: goto -> 139
  }
  
  private void updateView() {
    if (rebuildItems()) {
      calculateLayoutHeight(getHeight(), 1073741824);
      layout(getWidth(), getHeight());
    } 
  }
  
  public void addChangingListener(OnWheelChangedListener paramOnWheelChangedListener) {
    this.changingListeners.add(paramOnWheelChangedListener);
  }
  
  public void addClickingListener(OnWheelClickedListener paramOnWheelClickedListener) {
    this.clickingListeners.add(paramOnWheelClickedListener);
  }
  
  public void addScrollingListener(OnWheelScrollListener paramOnWheelScrollListener) {
    this.scrollingListeners.add(paramOnWheelScrollListener);
  }
  
  public int getCurrentItem() {
    return this.currentItem;
  }
  
  public WheelViewAdapter getViewAdapter() {
    return this.viewAdapter;
  }
  
  public int getVisibleItems() {
    return this.visibleItems;
  }
  
  public void invalidateWheel(boolean paramBoolean) {
    if (paramBoolean) {
      this.recycle.clearAll();
      if (this.itemsLayout != null)
        this.itemsLayout.removeAllViews(); 
      this.scrollingOffset = 0;
    } else if (this.itemsLayout != null) {
      this.recycle.recycleItems(this.itemsLayout, this.firstItem, new ItemsRange());
    } 
    invalidate();
  }
  
  public boolean isCyclic() {
    return this.isCyclic;
  }
  
  protected void notifyChangingListeners(int paramInt1, int paramInt2) {
    Iterator<OnWheelChangedListener> iterator = this.changingListeners.iterator();
    while (true) {
      if (!iterator.hasNext())
        return; 
      ((OnWheelChangedListener)iterator.next()).onChanged(this, paramInt1, paramInt2);
    } 
  }
  
  protected void notifyClickListenersAboutClick(int paramInt) {
    Iterator<OnWheelClickedListener> iterator = this.clickingListeners.iterator();
    while (true) {
      if (!iterator.hasNext())
        return; 
      ((OnWheelClickedListener)iterator.next()).onItemClicked(this, paramInt);
    } 
  }
  
  protected void notifyScrollingListenersAboutEnd() {
    Iterator<OnWheelScrollListener> iterator = this.scrollingListeners.iterator();
    while (true) {
      if (!iterator.hasNext())
        return; 
      ((OnWheelScrollListener)iterator.next()).onScrollingFinished(this);
    } 
  }
  
  protected void notifyScrollingListenersAboutStart() {
    Iterator<OnWheelScrollListener> iterator = this.scrollingListeners.iterator();
    while (true) {
      if (!iterator.hasNext())
        return; 
      ((OnWheelScrollListener)iterator.next()).onScrollingStarted(this);
    } 
  }
  
  protected void onDraw(Canvas paramCanvas) {
    super.onDraw(paramCanvas);
    if (this.viewAdapter != null && this.viewAdapter.getItemsCount() > 0) {
      updateView();
      drawItems(paramCanvas);
    } 
    drawShadows(paramCanvas);
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    layout(paramInt3 - paramInt1, paramInt4 - paramInt2);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int j = View.MeasureSpec.getMode(paramInt1);
    int k = View.MeasureSpec.getMode(paramInt2);
    int i = View.MeasureSpec.getSize(paramInt1);
    paramInt1 = View.MeasureSpec.getSize(paramInt2);
    buildViewForMeasuring();
    k = calculateLayoutHeight(paramInt1, k);
    if (j == 1073741824) {
      paramInt1 = i;
    } else {
      paramInt2 = getDesiredWidth(this.itemsLayout);
      paramInt1 = paramInt2;
      if (j == Integer.MIN_VALUE)
        paramInt1 = Math.min(paramInt2, i); 
    } 
    setMeasuredDimension(paramInt1, k);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    if (!isEnabled() || getViewAdapter() == null)
      return true; 
    switch (paramMotionEvent.getAction()) {
      default:
        return this.scroller.onTouchEvent(paramMotionEvent);
      case 2:
        if (getParent() != null)
          getParent().requestDisallowInterceptTouchEvent(true); 
      case 1:
        break;
    } 
    if (!this.isScrollingPerformed) {
      int i = (int)paramMotionEvent.getX() - getWidth() / 2;
      if (i > 0) {
        i += getItemWidth() / 2;
      } else {
        i -= getItemWidth() / 2;
      } 
      i /= getItemWidth();
      if (isValidItemIndex(this.currentItem + i))
        notifyClickListenersAboutClick(this.currentItem + i); 
    } 
  }
  
  public void removeChangingListener(OnWheelChangedListener paramOnWheelChangedListener) {
    this.changingListeners.remove(paramOnWheelChangedListener);
  }
  
  public void removeClickingListener(OnWheelClickedListener paramOnWheelClickedListener) {
    this.clickingListeners.remove(paramOnWheelClickedListener);
  }
  
  public void removeScrollingListener(OnWheelScrollListener paramOnWheelScrollListener) {
    this.scrollingListeners.remove(paramOnWheelScrollListener);
  }
  
  public void scroll(int paramInt1, int paramInt2) {
    int i = getItemWidth();
    int j = this.scrollingOffset;
    this.scroller.scroll(i * paramInt1 - j, paramInt2);
  }
  
  public void setCurrentItem(int paramInt) {
    setCurrentItem(paramInt, false);
  }
  
  public void setCurrentItem(int paramInt, boolean paramBoolean) {
    // Byte code:
    //   0: aload_0
    //   1: getfield viewAdapter : Lorg/kankan/wheel/widget/adapters/WheelViewAdapter;
    //   4: ifnull -> 19
    //   7: aload_0
    //   8: getfield viewAdapter : Lorg/kankan/wheel/widget/adapters/WheelViewAdapter;
    //   11: invokeinterface getItemsCount : ()I
    //   16: ifne -> 20
    //   19: return
    //   20: aload_0
    //   21: getfield viewAdapter : Lorg/kankan/wheel/widget/adapters/WheelViewAdapter;
    //   24: invokeinterface getItemsCount : ()I
    //   29: istore #5
    //   31: iload_1
    //   32: iflt -> 43
    //   35: iload_1
    //   36: istore_3
    //   37: iload_1
    //   38: iload #5
    //   40: if_icmplt -> 59
    //   43: aload_0
    //   44: getfield isCyclic : Z
    //   47: ifeq -> 19
    //   50: iload_1
    //   51: iflt -> 136
    //   54: iload_1
    //   55: iload #5
    //   57: irem
    //   58: istore_3
    //   59: iload_3
    //   60: aload_0
    //   61: getfield currentItem : I
    //   64: if_icmpeq -> 19
    //   67: iload_2
    //   68: ifeq -> 150
    //   71: iload_3
    //   72: aload_0
    //   73: getfield currentItem : I
    //   76: isub
    //   77: istore #4
    //   79: iload #4
    //   81: istore_1
    //   82: aload_0
    //   83: getfield isCyclic : Z
    //   86: ifeq -> 129
    //   89: iload_3
    //   90: aload_0
    //   91: getfield currentItem : I
    //   94: invokestatic min : (II)I
    //   97: iload #5
    //   99: iadd
    //   100: iload_3
    //   101: aload_0
    //   102: getfield currentItem : I
    //   105: invokestatic max : (II)I
    //   108: isub
    //   109: istore_3
    //   110: iload #4
    //   112: istore_1
    //   113: iload_3
    //   114: iload #4
    //   116: invokestatic abs : (I)I
    //   119: if_icmpge -> 129
    //   122: iload #4
    //   124: ifge -> 144
    //   127: iload_3
    //   128: istore_1
    //   129: aload_0
    //   130: iload_1
    //   131: iconst_0
    //   132: invokevirtual scroll : (II)V
    //   135: return
    //   136: iload_1
    //   137: iload #5
    //   139: iadd
    //   140: istore_1
    //   141: goto -> 50
    //   144: iload_3
    //   145: ineg
    //   146: istore_1
    //   147: goto -> 129
    //   150: aload_0
    //   151: iconst_0
    //   152: putfield scrollingOffset : I
    //   155: aload_0
    //   156: getfield currentItem : I
    //   159: istore_1
    //   160: aload_0
    //   161: iload_3
    //   162: putfield currentItem : I
    //   165: aload_0
    //   166: iload_1
    //   167: aload_0
    //   168: getfield currentItem : I
    //   171: invokevirtual notifyChangingListeners : (II)V
    //   174: aload_0
    //   175: invokevirtual invalidate : ()V
    //   178: return
  }
  
  public void setCyclic(boolean paramBoolean) {
    this.isCyclic = paramBoolean;
    invalidateWheel(false);
  }
  
  public void setInterpolator(Interpolator paramInterpolator) {
    this.scroller.setInterpolator(paramInterpolator);
  }
  
  public void setViewAdapter(WheelViewAdapter paramWheelViewAdapter) {
    if (this.viewAdapter != null)
      this.viewAdapter.unregisterDataSetObserver(this.dataObserver); 
    this.viewAdapter = paramWheelViewAdapter;
    if (this.viewAdapter != null)
      this.viewAdapter.registerDataSetObserver(this.dataObserver); 
    invalidateWheel(true);
  }
  
  public void setVisibleItems(int paramInt) {
    this.visibleItems = paramInt;
  }
  
  public void stopScrolling() {
    this.scroller.stopScrolling();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\kankan\wheel\widget\WheelView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */