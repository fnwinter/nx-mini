package android.support.v7.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.appcompat.R;
import android.support.v7.view.CollapsibleActionView;
import android.support.v7.view.SupportMenuInflater;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Toolbar extends ViewGroup {
  private static final String TAG = "Toolbar";
  
  private MenuPresenter.Callback mActionMenuPresenterCallback;
  
  private int mButtonGravity;
  
  private ImageButton mCollapseButtonView;
  
  private CharSequence mCollapseDescription;
  
  private Drawable mCollapseIcon;
  
  private boolean mCollapsible;
  
  private final RtlSpacingHelper mContentInsets = new RtlSpacingHelper();
  
  private boolean mEatingHover;
  
  private boolean mEatingTouch;
  
  View mExpandedActionView;
  
  private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
  
  private int mGravity = 8388627;
  
  private final ArrayList<View> mHiddenViews = new ArrayList<View>();
  
  private ImageView mLogoView;
  
  private int mMaxButtonHeight;
  
  private MenuBuilder.Callback mMenuBuilderCallback;
  
  private ActionMenuView mMenuView;
  
  private final ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener = new ActionMenuView.OnMenuItemClickListener() {
      public boolean onMenuItemClick(MenuItem param1MenuItem) {
        return (Toolbar.this.mOnMenuItemClickListener != null) ? Toolbar.this.mOnMenuItemClickListener.onMenuItemClick(param1MenuItem) : false;
      }
    };
  
  private ImageButton mNavButtonView;
  
  private OnMenuItemClickListener mOnMenuItemClickListener;
  
  private ActionMenuPresenter mOuterActionMenuPresenter;
  
  private Context mPopupContext;
  
  private int mPopupTheme;
  
  private final Runnable mShowOverflowMenuRunnable = new Runnable() {
      public void run() {
        Toolbar.this.showOverflowMenu();
      }
    };
  
  private CharSequence mSubtitleText;
  
  private int mSubtitleTextAppearance;
  
  private int mSubtitleTextColor;
  
  private TextView mSubtitleTextView;
  
  private final int[] mTempMargins = new int[2];
  
  private final ArrayList<View> mTempViews = new ArrayList<View>();
  
  private final TintManager mTintManager;
  
  private int mTitleMarginBottom;
  
  private int mTitleMarginEnd;
  
  private int mTitleMarginStart;
  
  private int mTitleMarginTop;
  
  private CharSequence mTitleText;
  
  private int mTitleTextAppearance;
  
  private int mTitleTextColor;
  
  private TextView mTitleTextView;
  
  private ToolbarWidgetWrapper mWrapper;
  
  public Toolbar(Context paramContext) {
    this(paramContext, (AttributeSet)null);
  }
  
  public Toolbar(Context paramContext, @Nullable AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, R.attr.toolbarStyle);
  }
  
  public Toolbar(Context paramContext, @Nullable AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    TintTypedArray tintTypedArray = TintTypedArray.obtainStyledAttributes(getContext(), paramAttributeSet, R.styleable.Toolbar, paramInt, 0);
    this.mTitleTextAppearance = tintTypedArray.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0);
    this.mSubtitleTextAppearance = tintTypedArray.getResourceId(R.styleable.Toolbar_subtitleTextAppearance, 0);
    this.mGravity = tintTypedArray.getInteger(R.styleable.Toolbar_android_gravity, this.mGravity);
    this.mButtonGravity = 48;
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMargins, 0);
    this.mTitleMarginBottom = paramInt;
    this.mTitleMarginTop = paramInt;
    this.mTitleMarginEnd = paramInt;
    this.mTitleMarginStart = paramInt;
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginStart, -1);
    if (paramInt >= 0)
      this.mTitleMarginStart = paramInt; 
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginEnd, -1);
    if (paramInt >= 0)
      this.mTitleMarginEnd = paramInt; 
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginTop, -1);
    if (paramInt >= 0)
      this.mTitleMarginTop = paramInt; 
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_titleMarginBottom, -1);
    if (paramInt >= 0)
      this.mTitleMarginBottom = paramInt; 
    this.mMaxButtonHeight = tintTypedArray.getDimensionPixelSize(R.styleable.Toolbar_maxButtonHeight, -1);
    paramInt = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetStart, -2147483648);
    int i = tintTypedArray.getDimensionPixelOffset(R.styleable.Toolbar_contentInsetEnd, -2147483648);
    int j = tintTypedArray.getDimensionPixelSize(R.styleable.Toolbar_contentInsetLeft, 0);
    int k = tintTypedArray.getDimensionPixelSize(R.styleable.Toolbar_contentInsetRight, 0);
    this.mContentInsets.setAbsolute(j, k);
    if (paramInt != Integer.MIN_VALUE || i != Integer.MIN_VALUE)
      this.mContentInsets.setRelative(paramInt, i); 
    this.mCollapseIcon = tintTypedArray.getDrawable(R.styleable.Toolbar_collapseIcon);
    this.mCollapseDescription = tintTypedArray.getText(R.styleable.Toolbar_collapseContentDescription);
    CharSequence charSequence3 = tintTypedArray.getText(R.styleable.Toolbar_title);
    if (!TextUtils.isEmpty(charSequence3))
      setTitle(charSequence3); 
    charSequence3 = tintTypedArray.getText(R.styleable.Toolbar_subtitle);
    if (!TextUtils.isEmpty(charSequence3))
      setSubtitle(charSequence3); 
    this.mPopupContext = getContext();
    setPopupTheme(tintTypedArray.getResourceId(R.styleable.Toolbar_popupTheme, 0));
    Drawable drawable2 = tintTypedArray.getDrawable(R.styleable.Toolbar_navigationIcon);
    if (drawable2 != null)
      setNavigationIcon(drawable2); 
    CharSequence charSequence2 = tintTypedArray.getText(R.styleable.Toolbar_navigationContentDescription);
    if (!TextUtils.isEmpty(charSequence2))
      setNavigationContentDescription(charSequence2); 
    Drawable drawable1 = tintTypedArray.getDrawable(R.styleable.Toolbar_logo);
    if (drawable1 != null)
      setLogo(drawable1); 
    CharSequence charSequence1 = tintTypedArray.getText(R.styleable.Toolbar_logoDescription);
    if (!TextUtils.isEmpty(charSequence1))
      setLogoDescription(charSequence1); 
    if (tintTypedArray.hasValue(R.styleable.Toolbar_titleTextColor))
      setTitleTextColor(tintTypedArray.getColor(R.styleable.Toolbar_titleTextColor, -1)); 
    if (tintTypedArray.hasValue(R.styleable.Toolbar_subtitleTextColor))
      setSubtitleTextColor(tintTypedArray.getColor(R.styleable.Toolbar_subtitleTextColor, -1)); 
    tintTypedArray.recycle();
    this.mTintManager = tintTypedArray.getTintManager();
  }
  
  private void addCustomViewsWithGravity(List<View> paramList, int paramInt) {
    boolean bool = true;
    if (ViewCompat.getLayoutDirection((View)this) != 1)
      bool = false; 
    int j = getChildCount();
    int i = GravityCompat.getAbsoluteGravity(paramInt, ViewCompat.getLayoutDirection((View)this));
    paramList.clear();
    if (bool) {
      for (paramInt = j - 1; paramInt >= 0; paramInt--) {
        View view = getChildAt(paramInt);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mViewType == 0 && shouldLayout(view) && getChildHorizontalGravity(layoutParams.gravity) == i)
          paramList.add(view); 
      } 
    } else {
      for (paramInt = 0; paramInt < j; paramInt++) {
        View view = getChildAt(paramInt);
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (layoutParams.mViewType == 0 && shouldLayout(view) && getChildHorizontalGravity(layoutParams.gravity) == i)
          paramList.add(view); 
      } 
    } 
  }
  
  private void addSystemView(View paramView, boolean paramBoolean) {
    LayoutParams layoutParams;
    ViewGroup.LayoutParams layoutParams1 = paramView.getLayoutParams();
    if (layoutParams1 == null) {
      layoutParams = generateDefaultLayoutParams();
    } else if (!checkLayoutParams((ViewGroup.LayoutParams)layoutParams)) {
      layoutParams = generateLayoutParams((ViewGroup.LayoutParams)layoutParams);
    } else {
      layoutParams = layoutParams;
    } 
    layoutParams.mViewType = 1;
    if (paramBoolean && this.mExpandedActionView != null) {
      paramView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      this.mHiddenViews.add(paramView);
      return;
    } 
    addView(paramView, (ViewGroup.LayoutParams)layoutParams);
  }
  
  private void ensureCollapseButtonView() {
    if (this.mCollapseButtonView == null) {
      this.mCollapseButtonView = new ImageButton(getContext(), null, R.attr.toolbarNavigationButtonStyle);
      this.mCollapseButtonView.setImageDrawable(this.mCollapseIcon);
      this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
      LayoutParams layoutParams = generateDefaultLayoutParams();
      layoutParams.gravity = 0x800003 | this.mButtonGravity & 0x70;
      layoutParams.mViewType = 2;
      this.mCollapseButtonView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      this.mCollapseButtonView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              Toolbar.this.collapseActionView();
            }
          });
    } 
  }
  
  private void ensureLogoView() {
    if (this.mLogoView == null)
      this.mLogoView = new ImageView(getContext()); 
  }
  
  private void ensureMenu() {
    ensureMenuView();
    if (this.mMenuView.peekMenu() == null) {
      MenuBuilder menuBuilder = (MenuBuilder)this.mMenuView.getMenu();
      if (this.mExpandedMenuPresenter == null)
        this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(); 
      this.mMenuView.setExpandedActionViewsExclusive(true);
      menuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
    } 
  }
  
  private void ensureMenuView() {
    if (this.mMenuView == null) {
      this.mMenuView = new ActionMenuView(getContext());
      this.mMenuView.setPopupTheme(this.mPopupTheme);
      this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
      this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
      LayoutParams layoutParams = generateDefaultLayoutParams();
      layoutParams.gravity = 0x800005 | this.mButtonGravity & 0x70;
      this.mMenuView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      addSystemView((View)this.mMenuView, false);
    } 
  }
  
  private void ensureNavButtonView() {
    if (this.mNavButtonView == null) {
      this.mNavButtonView = new ImageButton(getContext(), null, R.attr.toolbarNavigationButtonStyle);
      LayoutParams layoutParams = generateDefaultLayoutParams();
      layoutParams.gravity = 0x800003 | this.mButtonGravity & 0x70;
      this.mNavButtonView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    } 
  }
  
  private int getChildHorizontalGravity(int paramInt) {
    int j = ViewCompat.getLayoutDirection((View)this);
    int i = GravityCompat.getAbsoluteGravity(paramInt, j) & 0x7;
    paramInt = i;
    switch (i) {
      default:
        if (j == 1)
          paramInt = 5; 
        break;
      case 1:
      case 3:
      case 5:
        return paramInt;
    } 
    paramInt = 3;
  }
  
  private int getChildTop(View paramView, int paramInt) {
    int i;
    int k;
    int m;
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int j = paramView.getMeasuredHeight();
    if (paramInt > 0) {
      paramInt = (j - paramInt) / 2;
    } else {
      paramInt = 0;
    } 
    switch (getChildVerticalGravity(layoutParams.gravity)) {
      default:
        k = getPaddingTop();
        paramInt = getPaddingBottom();
        m = getHeight();
        i = (m - k - paramInt - j) / 2;
        if (i < layoutParams.topMargin) {
          paramInt = layoutParams.topMargin;
          return k + paramInt;
        } 
        break;
      case 48:
        return getPaddingTop() - paramInt;
      case 80:
        return getHeight() - getPaddingBottom() - j - layoutParams.bottomMargin - paramInt;
    } 
    j = m - paramInt - j - i - k;
    paramInt = i;
    if (j < layoutParams.bottomMargin)
      paramInt = Math.max(0, i - layoutParams.bottomMargin - j); 
    return k + paramInt;
  }
  
  private int getChildVerticalGravity(int paramInt) {
    int i = paramInt & 0x70;
    paramInt = i;
    switch (i) {
      default:
        paramInt = this.mGravity & 0x70;
        break;
      case 16:
      case 48:
      case 80:
        break;
    } 
    return paramInt;
  }
  
  private int getHorizontalMargins(View paramView) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    return MarginLayoutParamsCompat.getMarginStart(marginLayoutParams) + MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams);
  }
  
  private MenuInflater getMenuInflater() {
    return (MenuInflater)new SupportMenuInflater(getContext());
  }
  
  private int getVerticalMargins(View paramView) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    return marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
  }
  
  private int getViewListMeasuredWidth(List<View> paramList, int[] paramArrayOfint) {
    int m = paramArrayOfint[0];
    int k = paramArrayOfint[1];
    int j = 0;
    int n = paramList.size();
    for (int i = 0; i < n; i++) {
      View view = paramList.get(i);
      LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
      m = layoutParams.leftMargin - m;
      k = layoutParams.rightMargin - k;
      int i1 = Math.max(0, m);
      int i2 = Math.max(0, k);
      m = Math.max(0, -m);
      k = Math.max(0, -k);
      j += view.getMeasuredWidth() + i1 + i2;
    } 
    return j;
  }
  
  private boolean isChildOrHidden(View paramView) {
    return (paramView.getParent() == this || this.mHiddenViews.contains(paramView));
  }
  
  private static boolean isCustomView(View paramView) {
    return (((LayoutParams)paramView.getLayoutParams()).mViewType == 0);
  }
  
  private int layoutChildLeft(View paramView, int paramInt1, int[] paramArrayOfint, int paramInt2) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = layoutParams.leftMargin - paramArrayOfint[0];
    paramInt1 += Math.max(0, i);
    paramArrayOfint[0] = Math.max(0, -i);
    paramInt2 = getChildTop(paramView, paramInt2);
    i = paramView.getMeasuredWidth();
    paramView.layout(paramInt1, paramInt2, paramInt1 + i, paramView.getMeasuredHeight() + paramInt2);
    return paramInt1 + layoutParams.rightMargin + i;
  }
  
  private int layoutChildRight(View paramView, int paramInt1, int[] paramArrayOfint, int paramInt2) {
    LayoutParams layoutParams = (LayoutParams)paramView.getLayoutParams();
    int i = layoutParams.rightMargin - paramArrayOfint[1];
    paramInt1 -= Math.max(0, i);
    paramArrayOfint[1] = Math.max(0, -i);
    paramInt2 = getChildTop(paramView, paramInt2);
    i = paramView.getMeasuredWidth();
    paramView.layout(paramInt1 - i, paramInt2, paramInt1, paramView.getMeasuredHeight() + paramInt2);
    return paramInt1 - layoutParams.leftMargin + i;
  }
  
  private int measureChildCollapseMargins(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfint) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    int i = marginLayoutParams.leftMargin - paramArrayOfint[0];
    int j = marginLayoutParams.rightMargin - paramArrayOfint[1];
    int k = Math.max(0, i) + Math.max(0, j);
    paramArrayOfint[0] = Math.max(0, -i);
    paramArrayOfint[1] = Math.max(0, -j);
    paramView.measure(getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight() + k + paramInt2, marginLayoutParams.width), getChildMeasureSpec(paramInt3, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + paramInt4, marginLayoutParams.height));
    return paramView.getMeasuredWidth() + k;
  }
  
  private void measureChildConstrained(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    int i = getChildMeasureSpec(paramInt1, getPaddingLeft() + getPaddingRight() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + paramInt2, marginLayoutParams.width);
    paramInt2 = getChildMeasureSpec(paramInt3, getPaddingTop() + getPaddingBottom() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + paramInt4, marginLayoutParams.height);
    paramInt3 = View.MeasureSpec.getMode(paramInt2);
    paramInt1 = paramInt2;
    if (paramInt3 != 1073741824) {
      paramInt1 = paramInt2;
      if (paramInt5 >= 0) {
        if (paramInt3 != 0) {
          paramInt1 = Math.min(View.MeasureSpec.getSize(paramInt2), paramInt5);
        } else {
          paramInt1 = paramInt5;
        } 
        paramInt1 = View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824);
      } 
    } 
    paramView.measure(i, paramInt1);
  }
  
  private void postShowOverflowMenu() {
    removeCallbacks(this.mShowOverflowMenuRunnable);
    post(this.mShowOverflowMenuRunnable);
  }
  
  private boolean shouldCollapse() {
    if (this.mCollapsible) {
      int j = getChildCount();
      int i = 0;
      while (i < j) {
        View view = getChildAt(i);
        if (!shouldLayout(view) || view.getMeasuredWidth() <= 0 || view.getMeasuredHeight() <= 0) {
          i++;
          continue;
        } 
        return false;
      } 
      return true;
    } 
    return false;
  }
  
  private boolean shouldLayout(View paramView) {
    return (paramView != null && paramView.getParent() == this && paramView.getVisibility() != 8);
  }
  
  void addChildrenForExpandedActionView() {
    for (int i = this.mHiddenViews.size() - 1; i >= 0; i--)
      addView(this.mHiddenViews.get(i)); 
    this.mHiddenViews.clear();
  }
  
  public boolean canShowOverflowMenu() {
    return (getVisibility() == 0 && this.mMenuView != null && this.mMenuView.isOverflowReserved());
  }
  
  protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (super.checkLayoutParams(paramLayoutParams) && paramLayoutParams instanceof LayoutParams);
  }
  
  public void collapseActionView() {
    MenuItemImpl menuItemImpl;
    if (this.mExpandedMenuPresenter == null) {
      menuItemImpl = null;
    } else {
      menuItemImpl = this.mExpandedMenuPresenter.mCurrentExpandedItem;
    } 
    if (menuItemImpl != null)
      menuItemImpl.collapseActionView(); 
  }
  
  public void dismissPopupMenus() {
    if (this.mMenuView != null)
      this.mMenuView.dismissPopupMenus(); 
  }
  
  protected LayoutParams generateDefaultLayoutParams() {
    return new LayoutParams(-2, -2);
  }
  
  public LayoutParams generateLayoutParams(AttributeSet paramAttributeSet) {
    return new LayoutParams(getContext(), paramAttributeSet);
  }
  
  protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
    return (paramLayoutParams instanceof LayoutParams) ? new LayoutParams((LayoutParams)paramLayoutParams) : ((paramLayoutParams instanceof ActionBar.LayoutParams) ? new LayoutParams((ActionBar.LayoutParams)paramLayoutParams) : ((paramLayoutParams instanceof ViewGroup.MarginLayoutParams) ? new LayoutParams((ViewGroup.MarginLayoutParams)paramLayoutParams) : new LayoutParams(paramLayoutParams)));
  }
  
  public int getContentInsetEnd() {
    return this.mContentInsets.getEnd();
  }
  
  public int getContentInsetLeft() {
    return this.mContentInsets.getLeft();
  }
  
  public int getContentInsetRight() {
    return this.mContentInsets.getRight();
  }
  
  public int getContentInsetStart() {
    return this.mContentInsets.getStart();
  }
  
  public Drawable getLogo() {
    return (this.mLogoView != null) ? this.mLogoView.getDrawable() : null;
  }
  
  public CharSequence getLogoDescription() {
    return (this.mLogoView != null) ? this.mLogoView.getContentDescription() : null;
  }
  
  public Menu getMenu() {
    ensureMenu();
    return this.mMenuView.getMenu();
  }
  
  @Nullable
  public CharSequence getNavigationContentDescription() {
    return (this.mNavButtonView != null) ? this.mNavButtonView.getContentDescription() : null;
  }
  
  @Nullable
  public Drawable getNavigationIcon() {
    return (this.mNavButtonView != null) ? this.mNavButtonView.getDrawable() : null;
  }
  
  @Nullable
  public Drawable getOverflowIcon() {
    ensureMenu();
    return this.mMenuView.getOverflowIcon();
  }
  
  public int getPopupTheme() {
    return this.mPopupTheme;
  }
  
  public CharSequence getSubtitle() {
    return this.mSubtitleText;
  }
  
  public CharSequence getTitle() {
    return this.mTitleText;
  }
  
  public DecorToolbar getWrapper() {
    if (this.mWrapper == null)
      this.mWrapper = new ToolbarWidgetWrapper(this, true); 
    return this.mWrapper;
  }
  
  public boolean hasExpandedActionView() {
    return (this.mExpandedMenuPresenter != null && this.mExpandedMenuPresenter.mCurrentExpandedItem != null);
  }
  
  public boolean hideOverflowMenu() {
    return (this.mMenuView != null && this.mMenuView.hideOverflowMenu());
  }
  
  public void inflateMenu(@MenuRes int paramInt) {
    getMenuInflater().inflate(paramInt, getMenu());
  }
  
  public boolean isOverflowMenuShowPending() {
    return (this.mMenuView != null && this.mMenuView.isOverflowMenuShowPending());
  }
  
  public boolean isOverflowMenuShowing() {
    return (this.mMenuView != null && this.mMenuView.isOverflowMenuShowing());
  }
  
  public boolean isTitleTruncated() {
    if (this.mTitleTextView != null) {
      Layout layout = this.mTitleTextView.getLayout();
      if (layout != null) {
        int j = layout.getLineCount();
        int i = 0;
        while (true) {
          if (i < j) {
            if (layout.getEllipsisCount(i) > 0)
              return true; 
            i++;
            continue;
          } 
          return false;
        } 
      } 
    } 
    return false;
  }
  
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    removeCallbacks(this.mShowOverflowMenuRunnable);
  }
  
  public boolean onHoverEvent(MotionEvent paramMotionEvent) {
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    if (i == 9)
      this.mEatingHover = false; 
    if (!this.mEatingHover) {
      boolean bool = super.onHoverEvent(paramMotionEvent);
      if (i == 9 && !bool)
        this.mEatingHover = true; 
    } 
    if (i == 10 || i == 3)
      this.mEatingHover = false; 
    return true;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    // Byte code:
    //   0: aload_0
    //   1: invokestatic getLayoutDirection : (Landroid/view/View;)I
    //   4: iconst_1
    //   5: if_icmpne -> 895
    //   8: iconst_1
    //   9: istore #7
    //   11: aload_0
    //   12: invokevirtual getWidth : ()I
    //   15: istore #12
    //   17: aload_0
    //   18: invokevirtual getHeight : ()I
    //   21: istore #14
    //   23: aload_0
    //   24: invokevirtual getPaddingLeft : ()I
    //   27: istore #10
    //   29: aload_0
    //   30: invokevirtual getPaddingRight : ()I
    //   33: istore #13
    //   35: aload_0
    //   36: invokevirtual getPaddingTop : ()I
    //   39: istore #9
    //   41: aload_0
    //   42: invokevirtual getPaddingBottom : ()I
    //   45: istore #15
    //   47: iload #10
    //   49: istore #4
    //   51: iload #12
    //   53: iload #13
    //   55: isub
    //   56: istore #5
    //   58: aload_0
    //   59: getfield mTempMargins : [I
    //   62: astore #19
    //   64: aload #19
    //   66: iconst_1
    //   67: iconst_0
    //   68: iastore
    //   69: aload #19
    //   71: iconst_0
    //   72: iconst_0
    //   73: iastore
    //   74: aload_0
    //   75: invokestatic getMinimumHeight : (Landroid/view/View;)I
    //   78: istore #11
    //   80: iload #4
    //   82: istore_2
    //   83: iload #5
    //   85: istore_3
    //   86: aload_0
    //   87: aload_0
    //   88: getfield mNavButtonView : Landroid/widget/ImageButton;
    //   91: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   94: ifeq -> 120
    //   97: iload #7
    //   99: ifeq -> 901
    //   102: aload_0
    //   103: aload_0
    //   104: getfield mNavButtonView : Landroid/widget/ImageButton;
    //   107: iload #5
    //   109: aload #19
    //   111: iload #11
    //   113: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   116: istore_3
    //   117: iload #4
    //   119: istore_2
    //   120: iload_2
    //   121: istore #4
    //   123: iload_3
    //   124: istore #5
    //   126: aload_0
    //   127: aload_0
    //   128: getfield mCollapseButtonView : Landroid/widget/ImageButton;
    //   131: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   134: ifeq -> 160
    //   137: iload #7
    //   139: ifeq -> 922
    //   142: aload_0
    //   143: aload_0
    //   144: getfield mCollapseButtonView : Landroid/widget/ImageButton;
    //   147: iload_3
    //   148: aload #19
    //   150: iload #11
    //   152: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   155: istore #5
    //   157: iload_2
    //   158: istore #4
    //   160: iload #4
    //   162: istore_3
    //   163: iload #5
    //   165: istore_2
    //   166: aload_0
    //   167: aload_0
    //   168: getfield mMenuView : Landroid/support/v7/widget/ActionMenuView;
    //   171: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   174: ifeq -> 200
    //   177: iload #7
    //   179: ifeq -> 943
    //   182: aload_0
    //   183: aload_0
    //   184: getfield mMenuView : Landroid/support/v7/widget/ActionMenuView;
    //   187: iload #4
    //   189: aload #19
    //   191: iload #11
    //   193: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   196: istore_3
    //   197: iload #5
    //   199: istore_2
    //   200: aload #19
    //   202: iconst_0
    //   203: iconst_0
    //   204: aload_0
    //   205: invokevirtual getContentInsetLeft : ()I
    //   208: iload_3
    //   209: isub
    //   210: invokestatic max : (II)I
    //   213: iastore
    //   214: aload #19
    //   216: iconst_1
    //   217: iconst_0
    //   218: aload_0
    //   219: invokevirtual getContentInsetRight : ()I
    //   222: iload #12
    //   224: iload #13
    //   226: isub
    //   227: iload_2
    //   228: isub
    //   229: isub
    //   230: invokestatic max : (II)I
    //   233: iastore
    //   234: iload_3
    //   235: aload_0
    //   236: invokevirtual getContentInsetLeft : ()I
    //   239: invokestatic max : (II)I
    //   242: istore #4
    //   244: iload_2
    //   245: iload #12
    //   247: iload #13
    //   249: isub
    //   250: aload_0
    //   251: invokevirtual getContentInsetRight : ()I
    //   254: isub
    //   255: invokestatic min : (II)I
    //   258: istore #5
    //   260: iload #4
    //   262: istore_2
    //   263: iload #5
    //   265: istore_3
    //   266: aload_0
    //   267: aload_0
    //   268: getfield mExpandedActionView : Landroid/view/View;
    //   271: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   274: ifeq -> 300
    //   277: iload #7
    //   279: ifeq -> 964
    //   282: aload_0
    //   283: aload_0
    //   284: getfield mExpandedActionView : Landroid/view/View;
    //   287: iload #5
    //   289: aload #19
    //   291: iload #11
    //   293: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   296: istore_3
    //   297: iload #4
    //   299: istore_2
    //   300: iload_2
    //   301: istore #4
    //   303: iload_3
    //   304: istore #5
    //   306: aload_0
    //   307: aload_0
    //   308: getfield mLogoView : Landroid/widget/ImageView;
    //   311: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   314: ifeq -> 340
    //   317: iload #7
    //   319: ifeq -> 985
    //   322: aload_0
    //   323: aload_0
    //   324: getfield mLogoView : Landroid/widget/ImageView;
    //   327: iload_3
    //   328: aload #19
    //   330: iload #11
    //   332: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   335: istore #5
    //   337: iload_2
    //   338: istore #4
    //   340: aload_0
    //   341: aload_0
    //   342: getfield mTitleTextView : Landroid/widget/TextView;
    //   345: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   348: istore_1
    //   349: aload_0
    //   350: aload_0
    //   351: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   354: invokespecial shouldLayout : (Landroid/view/View;)Z
    //   357: istore #16
    //   359: iconst_0
    //   360: istore_2
    //   361: iload_1
    //   362: ifeq -> 399
    //   365: aload_0
    //   366: getfield mTitleTextView : Landroid/widget/TextView;
    //   369: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   372: checkcast android/support/v7/widget/Toolbar$LayoutParams
    //   375: astore #17
    //   377: iconst_0
    //   378: aload #17
    //   380: getfield topMargin : I
    //   383: aload_0
    //   384: getfield mTitleTextView : Landroid/widget/TextView;
    //   387: invokevirtual getMeasuredHeight : ()I
    //   390: iadd
    //   391: aload #17
    //   393: getfield bottomMargin : I
    //   396: iadd
    //   397: iadd
    //   398: istore_2
    //   399: iload_2
    //   400: istore #8
    //   402: iload #16
    //   404: ifeq -> 442
    //   407: aload_0
    //   408: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   411: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   414: checkcast android/support/v7/widget/Toolbar$LayoutParams
    //   417: astore #17
    //   419: iload_2
    //   420: aload #17
    //   422: getfield topMargin : I
    //   425: aload_0
    //   426: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   429: invokevirtual getMeasuredHeight : ()I
    //   432: iadd
    //   433: aload #17
    //   435: getfield bottomMargin : I
    //   438: iadd
    //   439: iadd
    //   440: istore #8
    //   442: iload_1
    //   443: ifne -> 457
    //   446: iload #4
    //   448: istore_3
    //   449: iload #5
    //   451: istore_2
    //   452: iload #16
    //   454: ifeq -> 836
    //   457: iload_1
    //   458: ifeq -> 1006
    //   461: aload_0
    //   462: getfield mTitleTextView : Landroid/widget/TextView;
    //   465: astore #17
    //   467: iload #16
    //   469: ifeq -> 1015
    //   472: aload_0
    //   473: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   476: astore #18
    //   478: aload #17
    //   480: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   483: checkcast android/support/v7/widget/Toolbar$LayoutParams
    //   486: astore #17
    //   488: aload #18
    //   490: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   493: checkcast android/support/v7/widget/Toolbar$LayoutParams
    //   496: astore #18
    //   498: iload_1
    //   499: ifeq -> 512
    //   502: aload_0
    //   503: getfield mTitleTextView : Landroid/widget/TextView;
    //   506: invokevirtual getMeasuredWidth : ()I
    //   509: ifgt -> 527
    //   512: iload #16
    //   514: ifeq -> 1024
    //   517: aload_0
    //   518: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   521: invokevirtual getMeasuredWidth : ()I
    //   524: ifle -> 1024
    //   527: iconst_1
    //   528: istore #6
    //   530: aload_0
    //   531: getfield mGravity : I
    //   534: bipush #112
    //   536: iand
    //   537: lookupswitch default -> 564, 48 -> 1030, 80 -> 1104
    //   564: iload #14
    //   566: iload #9
    //   568: isub
    //   569: iload #15
    //   571: isub
    //   572: iload #8
    //   574: isub
    //   575: iconst_2
    //   576: idiv
    //   577: istore_3
    //   578: iload_3
    //   579: aload #17
    //   581: getfield topMargin : I
    //   584: aload_0
    //   585: getfield mTitleMarginTop : I
    //   588: iadd
    //   589: if_icmpge -> 1049
    //   592: aload #17
    //   594: getfield topMargin : I
    //   597: aload_0
    //   598: getfield mTitleMarginTop : I
    //   601: iadd
    //   602: istore_2
    //   603: iload #9
    //   605: iload_2
    //   606: iadd
    //   607: istore_2
    //   608: iload #7
    //   610: ifeq -> 1132
    //   613: iload #6
    //   615: ifeq -> 1127
    //   618: aload_0
    //   619: getfield mTitleMarginStart : I
    //   622: istore_3
    //   623: iload_3
    //   624: aload #19
    //   626: iconst_1
    //   627: iaload
    //   628: isub
    //   629: istore_3
    //   630: iload #5
    //   632: iconst_0
    //   633: iload_3
    //   634: invokestatic max : (II)I
    //   637: isub
    //   638: istore #5
    //   640: aload #19
    //   642: iconst_1
    //   643: iconst_0
    //   644: iload_3
    //   645: ineg
    //   646: invokestatic max : (II)I
    //   649: iastore
    //   650: iload #5
    //   652: istore #8
    //   654: iload #5
    //   656: istore_3
    //   657: iload #8
    //   659: istore #7
    //   661: iload_2
    //   662: istore #9
    //   664: iload_1
    //   665: ifeq -> 736
    //   668: aload_0
    //   669: getfield mTitleTextView : Landroid/widget/TextView;
    //   672: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   675: checkcast android/support/v7/widget/Toolbar$LayoutParams
    //   678: astore #17
    //   680: iload #8
    //   682: aload_0
    //   683: getfield mTitleTextView : Landroid/widget/TextView;
    //   686: invokevirtual getMeasuredWidth : ()I
    //   689: isub
    //   690: istore #7
    //   692: iload_2
    //   693: aload_0
    //   694: getfield mTitleTextView : Landroid/widget/TextView;
    //   697: invokevirtual getMeasuredHeight : ()I
    //   700: iadd
    //   701: istore #9
    //   703: aload_0
    //   704: getfield mTitleTextView : Landroid/widget/TextView;
    //   707: iload #7
    //   709: iload_2
    //   710: iload #8
    //   712: iload #9
    //   714: invokevirtual layout : (IIII)V
    //   717: iload #7
    //   719: aload_0
    //   720: getfield mTitleMarginEnd : I
    //   723: isub
    //   724: istore #7
    //   726: iload #9
    //   728: aload #17
    //   730: getfield bottomMargin : I
    //   733: iadd
    //   734: istore #9
    //   736: iload_3
    //   737: istore #8
    //   739: iload #16
    //   741: ifeq -> 814
    //   744: aload_0
    //   745: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   748: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   751: checkcast android/support/v7/widget/Toolbar$LayoutParams
    //   754: astore #17
    //   756: iload #9
    //   758: aload #17
    //   760: getfield topMargin : I
    //   763: iadd
    //   764: istore_2
    //   765: aload_0
    //   766: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   769: invokevirtual getMeasuredWidth : ()I
    //   772: istore #8
    //   774: iload_2
    //   775: aload_0
    //   776: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   779: invokevirtual getMeasuredHeight : ()I
    //   782: iadd
    //   783: istore #9
    //   785: aload_0
    //   786: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   789: iload_3
    //   790: iload #8
    //   792: isub
    //   793: iload_2
    //   794: iload_3
    //   795: iload #9
    //   797: invokevirtual layout : (IIII)V
    //   800: iload_3
    //   801: aload_0
    //   802: getfield mTitleMarginEnd : I
    //   805: isub
    //   806: istore #8
    //   808: aload #17
    //   810: getfield bottomMargin : I
    //   813: istore_2
    //   814: iload #4
    //   816: istore_3
    //   817: iload #5
    //   819: istore_2
    //   820: iload #6
    //   822: ifeq -> 836
    //   825: iload #7
    //   827: iload #8
    //   829: invokestatic min : (II)I
    //   832: istore_2
    //   833: iload #4
    //   835: istore_3
    //   836: aload_0
    //   837: aload_0
    //   838: getfield mTempViews : Ljava/util/ArrayList;
    //   841: iconst_3
    //   842: invokespecial addCustomViewsWithGravity : (Ljava/util/List;I)V
    //   845: aload_0
    //   846: getfield mTempViews : Ljava/util/ArrayList;
    //   849: invokevirtual size : ()I
    //   852: istore #5
    //   854: iconst_0
    //   855: istore #4
    //   857: iload #4
    //   859: iload #5
    //   861: if_icmpge -> 1365
    //   864: aload_0
    //   865: aload_0
    //   866: getfield mTempViews : Ljava/util/ArrayList;
    //   869: iload #4
    //   871: invokevirtual get : (I)Ljava/lang/Object;
    //   874: checkcast android/view/View
    //   877: iload_3
    //   878: aload #19
    //   880: iload #11
    //   882: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   885: istore_3
    //   886: iload #4
    //   888: iconst_1
    //   889: iadd
    //   890: istore #4
    //   892: goto -> 857
    //   895: iconst_0
    //   896: istore #7
    //   898: goto -> 11
    //   901: aload_0
    //   902: aload_0
    //   903: getfield mNavButtonView : Landroid/widget/ImageButton;
    //   906: iload #4
    //   908: aload #19
    //   910: iload #11
    //   912: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   915: istore_2
    //   916: iload #5
    //   918: istore_3
    //   919: goto -> 120
    //   922: aload_0
    //   923: aload_0
    //   924: getfield mCollapseButtonView : Landroid/widget/ImageButton;
    //   927: iload_2
    //   928: aload #19
    //   930: iload #11
    //   932: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   935: istore #4
    //   937: iload_3
    //   938: istore #5
    //   940: goto -> 160
    //   943: aload_0
    //   944: aload_0
    //   945: getfield mMenuView : Landroid/support/v7/widget/ActionMenuView;
    //   948: iload #5
    //   950: aload #19
    //   952: iload #11
    //   954: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   957: istore_2
    //   958: iload #4
    //   960: istore_3
    //   961: goto -> 200
    //   964: aload_0
    //   965: aload_0
    //   966: getfield mExpandedActionView : Landroid/view/View;
    //   969: iload #4
    //   971: aload #19
    //   973: iload #11
    //   975: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   978: istore_2
    //   979: iload #5
    //   981: istore_3
    //   982: goto -> 300
    //   985: aload_0
    //   986: aload_0
    //   987: getfield mLogoView : Landroid/widget/ImageView;
    //   990: iload_2
    //   991: aload #19
    //   993: iload #11
    //   995: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   998: istore #4
    //   1000: iload_3
    //   1001: istore #5
    //   1003: goto -> 340
    //   1006: aload_0
    //   1007: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1010: astore #17
    //   1012: goto -> 467
    //   1015: aload_0
    //   1016: getfield mTitleTextView : Landroid/widget/TextView;
    //   1019: astore #18
    //   1021: goto -> 478
    //   1024: iconst_0
    //   1025: istore #6
    //   1027: goto -> 530
    //   1030: aload_0
    //   1031: invokevirtual getPaddingTop : ()I
    //   1034: aload #17
    //   1036: getfield topMargin : I
    //   1039: iadd
    //   1040: aload_0
    //   1041: getfield mTitleMarginTop : I
    //   1044: iadd
    //   1045: istore_2
    //   1046: goto -> 608
    //   1049: iload #14
    //   1051: iload #15
    //   1053: isub
    //   1054: iload #8
    //   1056: isub
    //   1057: iload_3
    //   1058: isub
    //   1059: iload #9
    //   1061: isub
    //   1062: istore #8
    //   1064: iload_3
    //   1065: istore_2
    //   1066: iload #8
    //   1068: aload #17
    //   1070: getfield bottomMargin : I
    //   1073: aload_0
    //   1074: getfield mTitleMarginBottom : I
    //   1077: iadd
    //   1078: if_icmpge -> 603
    //   1081: iconst_0
    //   1082: iload_3
    //   1083: aload #18
    //   1085: getfield bottomMargin : I
    //   1088: aload_0
    //   1089: getfield mTitleMarginBottom : I
    //   1092: iadd
    //   1093: iload #8
    //   1095: isub
    //   1096: isub
    //   1097: invokestatic max : (II)I
    //   1100: istore_2
    //   1101: goto -> 603
    //   1104: iload #14
    //   1106: iload #15
    //   1108: isub
    //   1109: aload #18
    //   1111: getfield bottomMargin : I
    //   1114: isub
    //   1115: aload_0
    //   1116: getfield mTitleMarginBottom : I
    //   1119: isub
    //   1120: iload #8
    //   1122: isub
    //   1123: istore_2
    //   1124: goto -> 608
    //   1127: iconst_0
    //   1128: istore_3
    //   1129: goto -> 623
    //   1132: iload #6
    //   1134: ifeq -> 1360
    //   1137: aload_0
    //   1138: getfield mTitleMarginStart : I
    //   1141: istore_3
    //   1142: iload_3
    //   1143: aload #19
    //   1145: iconst_0
    //   1146: iaload
    //   1147: isub
    //   1148: istore #7
    //   1150: iload #4
    //   1152: iconst_0
    //   1153: iload #7
    //   1155: invokestatic max : (II)I
    //   1158: iadd
    //   1159: istore_3
    //   1160: aload #19
    //   1162: iconst_0
    //   1163: iconst_0
    //   1164: iload #7
    //   1166: ineg
    //   1167: invokestatic max : (II)I
    //   1170: iastore
    //   1171: iload_3
    //   1172: istore #8
    //   1174: iload_3
    //   1175: istore #4
    //   1177: iload #8
    //   1179: istore #7
    //   1181: iload_2
    //   1182: istore #9
    //   1184: iload_1
    //   1185: ifeq -> 1256
    //   1188: aload_0
    //   1189: getfield mTitleTextView : Landroid/widget/TextView;
    //   1192: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1195: checkcast android/support/v7/widget/Toolbar$LayoutParams
    //   1198: astore #17
    //   1200: iload #8
    //   1202: aload_0
    //   1203: getfield mTitleTextView : Landroid/widget/TextView;
    //   1206: invokevirtual getMeasuredWidth : ()I
    //   1209: iadd
    //   1210: istore #7
    //   1212: iload_2
    //   1213: aload_0
    //   1214: getfield mTitleTextView : Landroid/widget/TextView;
    //   1217: invokevirtual getMeasuredHeight : ()I
    //   1220: iadd
    //   1221: istore #9
    //   1223: aload_0
    //   1224: getfield mTitleTextView : Landroid/widget/TextView;
    //   1227: iload #8
    //   1229: iload_2
    //   1230: iload #7
    //   1232: iload #9
    //   1234: invokevirtual layout : (IIII)V
    //   1237: iload #7
    //   1239: aload_0
    //   1240: getfield mTitleMarginEnd : I
    //   1243: iadd
    //   1244: istore #7
    //   1246: iload #9
    //   1248: aload #17
    //   1250: getfield bottomMargin : I
    //   1253: iadd
    //   1254: istore #9
    //   1256: iload #4
    //   1258: istore #8
    //   1260: iload #16
    //   1262: ifeq -> 1338
    //   1265: aload_0
    //   1266: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1269: invokevirtual getLayoutParams : ()Landroid/view/ViewGroup$LayoutParams;
    //   1272: checkcast android/support/v7/widget/Toolbar$LayoutParams
    //   1275: astore #17
    //   1277: iload #9
    //   1279: aload #17
    //   1281: getfield topMargin : I
    //   1284: iadd
    //   1285: istore_2
    //   1286: iload #4
    //   1288: aload_0
    //   1289: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1292: invokevirtual getMeasuredWidth : ()I
    //   1295: iadd
    //   1296: istore #8
    //   1298: iload_2
    //   1299: aload_0
    //   1300: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1303: invokevirtual getMeasuredHeight : ()I
    //   1306: iadd
    //   1307: istore #9
    //   1309: aload_0
    //   1310: getfield mSubtitleTextView : Landroid/widget/TextView;
    //   1313: iload #4
    //   1315: iload_2
    //   1316: iload #8
    //   1318: iload #9
    //   1320: invokevirtual layout : (IIII)V
    //   1323: iload #8
    //   1325: aload_0
    //   1326: getfield mTitleMarginEnd : I
    //   1329: iadd
    //   1330: istore #8
    //   1332: aload #17
    //   1334: getfield bottomMargin : I
    //   1337: istore_2
    //   1338: iload #5
    //   1340: istore_2
    //   1341: iload #6
    //   1343: ifeq -> 836
    //   1346: iload #7
    //   1348: iload #8
    //   1350: invokestatic max : (II)I
    //   1353: istore_3
    //   1354: iload #5
    //   1356: istore_2
    //   1357: goto -> 836
    //   1360: iconst_0
    //   1361: istore_3
    //   1362: goto -> 1142
    //   1365: aload_0
    //   1366: aload_0
    //   1367: getfield mTempViews : Ljava/util/ArrayList;
    //   1370: iconst_5
    //   1371: invokespecial addCustomViewsWithGravity : (Ljava/util/List;I)V
    //   1374: aload_0
    //   1375: getfield mTempViews : Ljava/util/ArrayList;
    //   1378: invokevirtual size : ()I
    //   1381: istore #6
    //   1383: iconst_0
    //   1384: istore #5
    //   1386: iload_2
    //   1387: istore #4
    //   1389: iload #5
    //   1391: istore_2
    //   1392: iload_2
    //   1393: iload #6
    //   1395: if_icmpge -> 1428
    //   1398: aload_0
    //   1399: aload_0
    //   1400: getfield mTempViews : Ljava/util/ArrayList;
    //   1403: iload_2
    //   1404: invokevirtual get : (I)Ljava/lang/Object;
    //   1407: checkcast android/view/View
    //   1410: iload #4
    //   1412: aload #19
    //   1414: iload #11
    //   1416: invokespecial layoutChildRight : (Landroid/view/View;I[II)I
    //   1419: istore #4
    //   1421: iload_2
    //   1422: iconst_1
    //   1423: iadd
    //   1424: istore_2
    //   1425: goto -> 1392
    //   1428: aload_0
    //   1429: aload_0
    //   1430: getfield mTempViews : Ljava/util/ArrayList;
    //   1433: iconst_1
    //   1434: invokespecial addCustomViewsWithGravity : (Ljava/util/List;I)V
    //   1437: aload_0
    //   1438: aload_0
    //   1439: getfield mTempViews : Ljava/util/ArrayList;
    //   1442: aload #19
    //   1444: invokespecial getViewListMeasuredWidth : (Ljava/util/List;[I)I
    //   1447: istore_2
    //   1448: iload #10
    //   1450: iload #12
    //   1452: iload #10
    //   1454: isub
    //   1455: iload #13
    //   1457: isub
    //   1458: iconst_2
    //   1459: idiv
    //   1460: iadd
    //   1461: iload_2
    //   1462: iconst_2
    //   1463: idiv
    //   1464: isub
    //   1465: istore #5
    //   1467: iload #5
    //   1469: iload_2
    //   1470: iadd
    //   1471: istore #6
    //   1473: iload #5
    //   1475: iload_3
    //   1476: if_icmpge -> 1526
    //   1479: iload_3
    //   1480: istore_2
    //   1481: aload_0
    //   1482: getfield mTempViews : Ljava/util/ArrayList;
    //   1485: invokevirtual size : ()I
    //   1488: istore #4
    //   1490: iconst_0
    //   1491: istore_3
    //   1492: iload_3
    //   1493: iload #4
    //   1495: if_icmpge -> 1548
    //   1498: aload_0
    //   1499: aload_0
    //   1500: getfield mTempViews : Ljava/util/ArrayList;
    //   1503: iload_3
    //   1504: invokevirtual get : (I)Ljava/lang/Object;
    //   1507: checkcast android/view/View
    //   1510: iload_2
    //   1511: aload #19
    //   1513: iload #11
    //   1515: invokespecial layoutChildLeft : (Landroid/view/View;I[II)I
    //   1518: istore_2
    //   1519: iload_3
    //   1520: iconst_1
    //   1521: iadd
    //   1522: istore_3
    //   1523: goto -> 1492
    //   1526: iload #5
    //   1528: istore_2
    //   1529: iload #6
    //   1531: iload #4
    //   1533: if_icmple -> 1481
    //   1536: iload #5
    //   1538: iload #6
    //   1540: iload #4
    //   1542: isub
    //   1543: isub
    //   1544: istore_2
    //   1545: goto -> 1481
    //   1548: aload_0
    //   1549: getfield mTempViews : Ljava/util/ArrayList;
    //   1552: invokevirtual clear : ()V
    //   1555: return
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    int m = 0;
    int k = 0;
    int[] arrayOfInt = this.mTempMargins;
    if (ViewUtils.isLayoutRtl((View)this)) {
      i2 = 1;
      i1 = 0;
    } else {
      i2 = 0;
      i1 = 1;
    } 
    int n = 0;
    if (shouldLayout((View)this.mNavButtonView)) {
      measureChildConstrained((View)this.mNavButtonView, paramInt1, 0, paramInt2, 0, this.mMaxButtonHeight);
      n = this.mNavButtonView.getMeasuredWidth() + getHorizontalMargins((View)this.mNavButtonView);
      m = Math.max(0, this.mNavButtonView.getMeasuredHeight() + getVerticalMargins((View)this.mNavButtonView));
      k = ViewUtils.combineMeasuredStates(0, ViewCompat.getMeasuredState((View)this.mNavButtonView));
    } 
    int i = k;
    int j = m;
    if (shouldLayout((View)this.mCollapseButtonView)) {
      measureChildConstrained((View)this.mCollapseButtonView, paramInt1, 0, paramInt2, 0, this.mMaxButtonHeight);
      n = this.mCollapseButtonView.getMeasuredWidth() + getHorizontalMargins((View)this.mCollapseButtonView);
      j = Math.max(m, this.mCollapseButtonView.getMeasuredHeight() + getVerticalMargins((View)this.mCollapseButtonView));
      i = ViewUtils.combineMeasuredStates(k, ViewCompat.getMeasuredState((View)this.mCollapseButtonView));
    } 
    k = getContentInsetStart();
    int i3 = 0 + Math.max(k, n);
    arrayOfInt[i2] = Math.max(0, k - n);
    n = 0;
    k = i;
    m = j;
    if (shouldLayout((View)this.mMenuView)) {
      measureChildConstrained((View)this.mMenuView, paramInt1, i3, paramInt2, 0, this.mMaxButtonHeight);
      n = this.mMenuView.getMeasuredWidth() + getHorizontalMargins((View)this.mMenuView);
      m = Math.max(j, this.mMenuView.getMeasuredHeight() + getVerticalMargins((View)this.mMenuView));
      k = ViewUtils.combineMeasuredStates(i, ViewCompat.getMeasuredState((View)this.mMenuView));
    } 
    i = getContentInsetEnd();
    int i2 = i3 + Math.max(i, n);
    arrayOfInt[i1] = Math.max(0, i - n);
    int i1 = i2;
    i = k;
    j = m;
    if (shouldLayout(this.mExpandedActionView)) {
      i1 = i2 + measureChildCollapseMargins(this.mExpandedActionView, paramInt1, i2, paramInt2, 0, arrayOfInt);
      j = Math.max(m, this.mExpandedActionView.getMeasuredHeight() + getVerticalMargins(this.mExpandedActionView));
      i = ViewUtils.combineMeasuredStates(k, ViewCompat.getMeasuredState(this.mExpandedActionView));
    } 
    k = i1;
    m = i;
    n = j;
    if (shouldLayout((View)this.mLogoView)) {
      k = i1 + measureChildCollapseMargins((View)this.mLogoView, paramInt1, i1, paramInt2, 0, arrayOfInt);
      n = Math.max(j, this.mLogoView.getMeasuredHeight() + getVerticalMargins((View)this.mLogoView));
      m = ViewUtils.combineMeasuredStates(i, ViewCompat.getMeasuredState((View)this.mLogoView));
    } 
    i3 = getChildCount();
    j = 0;
    i1 = n;
    i = m;
    n = k;
    while (j < i3) {
      View view = getChildAt(j);
      k = n;
      m = i;
      i2 = i1;
      if (((LayoutParams)view.getLayoutParams()).mViewType == 0)
        if (!shouldLayout(view)) {
          i2 = i1;
          m = i;
          k = n;
        } else {
          k = n + measureChildCollapseMargins(view, paramInt1, n, paramInt2, 0, arrayOfInt);
          i2 = Math.max(i1, view.getMeasuredHeight() + getVerticalMargins(view));
          m = ViewUtils.combineMeasuredStates(i, ViewCompat.getMeasuredState(view));
        }  
      j++;
      n = k;
      i = m;
      i1 = i2;
    } 
    m = 0;
    k = 0;
    int i4 = this.mTitleMarginTop + this.mTitleMarginBottom;
    int i5 = this.mTitleMarginStart + this.mTitleMarginEnd;
    j = i;
    if (shouldLayout((View)this.mTitleTextView)) {
      measureChildCollapseMargins((View)this.mTitleTextView, paramInt1, n + i5, paramInt2, i4, arrayOfInt);
      m = this.mTitleTextView.getMeasuredWidth() + getHorizontalMargins((View)this.mTitleTextView);
      k = this.mTitleTextView.getMeasuredHeight() + getVerticalMargins((View)this.mTitleTextView);
      j = ViewUtils.combineMeasuredStates(i, ViewCompat.getMeasuredState((View)this.mTitleTextView));
    } 
    i2 = j;
    i3 = k;
    i = m;
    if (shouldLayout((View)this.mSubtitleTextView)) {
      i = Math.max(m, measureChildCollapseMargins((View)this.mSubtitleTextView, paramInt1, n + i5, paramInt2, k + i4, arrayOfInt));
      i3 = k + this.mSubtitleTextView.getMeasuredHeight() + getVerticalMargins((View)this.mSubtitleTextView);
      i2 = ViewUtils.combineMeasuredStates(j, ViewCompat.getMeasuredState((View)this.mSubtitleTextView));
    } 
    j = Math.max(i1, i3);
    i1 = getPaddingLeft();
    i3 = getPaddingRight();
    k = getPaddingTop();
    m = getPaddingBottom();
    i = ViewCompat.resolveSizeAndState(Math.max(n + i + i1 + i3, getSuggestedMinimumWidth()), paramInt1, 0xFF000000 & i2);
    paramInt1 = ViewCompat.resolveSizeAndState(Math.max(j + k + m, getSuggestedMinimumHeight()), paramInt2, i2 << 16);
    if (shouldCollapse())
      paramInt1 = 0; 
    setMeasuredDimension(i, paramInt1);
  }
  
  protected void onRestoreInstanceState(Parcelable paramParcelable) {
    SavedState savedState = (SavedState)paramParcelable;
    super.onRestoreInstanceState(savedState.getSuperState());
    if (this.mMenuView != null) {
      MenuBuilder menuBuilder = this.mMenuView.peekMenu();
    } else {
      paramParcelable = null;
    } 
    if (savedState.expandedMenuItemId != 0 && this.mExpandedMenuPresenter != null && paramParcelable != null) {
      MenuItem menuItem = paramParcelable.findItem(savedState.expandedMenuItemId);
      if (menuItem != null)
        MenuItemCompat.expandActionView(menuItem); 
    } 
    if (savedState.isOverflowOpen)
      postShowOverflowMenu(); 
  }
  
  public void onRtlPropertiesChanged(int paramInt) {
    boolean bool = true;
    if (Build.VERSION.SDK_INT >= 17)
      super.onRtlPropertiesChanged(paramInt); 
    RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
    if (paramInt != 1)
      bool = false; 
    rtlSpacingHelper.setDirection(bool);
  }
  
  protected Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState(super.onSaveInstanceState());
    if (this.mExpandedMenuPresenter != null && this.mExpandedMenuPresenter.mCurrentExpandedItem != null)
      savedState.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId(); 
    savedState.isOverflowOpen = isOverflowMenuShowing();
    return (Parcelable)savedState;
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    int i = MotionEventCompat.getActionMasked(paramMotionEvent);
    if (i == 0)
      this.mEatingTouch = false; 
    if (!this.mEatingTouch) {
      boolean bool = super.onTouchEvent(paramMotionEvent);
      if (i == 0 && !bool)
        this.mEatingTouch = true; 
    } 
    if (i == 1 || i == 3)
      this.mEatingTouch = false; 
    return true;
  }
  
  void removeChildrenForExpandedActionView() {
    for (int i = getChildCount() - 1; i >= 0; i--) {
      View view = getChildAt(i);
      if (((LayoutParams)view.getLayoutParams()).mViewType != 2 && view != this.mMenuView) {
        removeViewAt(i);
        this.mHiddenViews.add(view);
      } 
    } 
  }
  
  public void setCollapsible(boolean paramBoolean) {
    this.mCollapsible = paramBoolean;
    requestLayout();
  }
  
  public void setContentInsetsAbsolute(int paramInt1, int paramInt2) {
    this.mContentInsets.setAbsolute(paramInt1, paramInt2);
  }
  
  public void setContentInsetsRelative(int paramInt1, int paramInt2) {
    this.mContentInsets.setRelative(paramInt1, paramInt2);
  }
  
  public void setLogo(@DrawableRes int paramInt) {
    setLogo(this.mTintManager.getDrawable(paramInt));
  }
  
  public void setLogo(Drawable paramDrawable) {
    if (paramDrawable != null) {
      ensureLogoView();
      if (!isChildOrHidden((View)this.mLogoView))
        addSystemView((View)this.mLogoView, true); 
    } else if (this.mLogoView != null && isChildOrHidden((View)this.mLogoView)) {
      removeView((View)this.mLogoView);
      this.mHiddenViews.remove(this.mLogoView);
    } 
    if (this.mLogoView != null)
      this.mLogoView.setImageDrawable(paramDrawable); 
  }
  
  public void setLogoDescription(@StringRes int paramInt) {
    setLogoDescription(getContext().getText(paramInt));
  }
  
  public void setLogoDescription(CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence))
      ensureLogoView(); 
    if (this.mLogoView != null)
      this.mLogoView.setContentDescription(paramCharSequence); 
  }
  
  public void setMenu(MenuBuilder paramMenuBuilder, ActionMenuPresenter paramActionMenuPresenter) {
    if (paramMenuBuilder != null || this.mMenuView != null) {
      ensureMenuView();
      MenuBuilder menuBuilder = this.mMenuView.peekMenu();
      if (menuBuilder != paramMenuBuilder) {
        if (menuBuilder != null) {
          menuBuilder.removeMenuPresenter((MenuPresenter)this.mOuterActionMenuPresenter);
          menuBuilder.removeMenuPresenter(this.mExpandedMenuPresenter);
        } 
        if (this.mExpandedMenuPresenter == null)
          this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(); 
        paramActionMenuPresenter.setExpandedActionViewsExclusive(true);
        if (paramMenuBuilder != null) {
          paramMenuBuilder.addMenuPresenter((MenuPresenter)paramActionMenuPresenter, this.mPopupContext);
          paramMenuBuilder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        } else {
          paramActionMenuPresenter.initForMenu(this.mPopupContext, null);
          this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, null);
          paramActionMenuPresenter.updateMenuView(true);
          this.mExpandedMenuPresenter.updateMenuView(true);
        } 
        this.mMenuView.setPopupTheme(this.mPopupTheme);
        this.mMenuView.setPresenter(paramActionMenuPresenter);
        this.mOuterActionMenuPresenter = paramActionMenuPresenter;
        return;
      } 
    } 
  }
  
  public void setMenuCallbacks(MenuPresenter.Callback paramCallback, MenuBuilder.Callback paramCallback1) {
    this.mActionMenuPresenterCallback = paramCallback;
    this.mMenuBuilderCallback = paramCallback1;
  }
  
  public void setNavigationContentDescription(@StringRes int paramInt) {
    CharSequence charSequence;
    if (paramInt != 0) {
      charSequence = getContext().getText(paramInt);
    } else {
      charSequence = null;
    } 
    setNavigationContentDescription(charSequence);
  }
  
  public void setNavigationContentDescription(@Nullable CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence))
      ensureNavButtonView(); 
    if (this.mNavButtonView != null)
      this.mNavButtonView.setContentDescription(paramCharSequence); 
  }
  
  public void setNavigationIcon(@DrawableRes int paramInt) {
    setNavigationIcon(this.mTintManager.getDrawable(paramInt));
  }
  
  public void setNavigationIcon(@Nullable Drawable paramDrawable) {
    if (paramDrawable != null) {
      ensureNavButtonView();
      if (!isChildOrHidden((View)this.mNavButtonView))
        addSystemView((View)this.mNavButtonView, true); 
    } else if (this.mNavButtonView != null && isChildOrHidden((View)this.mNavButtonView)) {
      removeView((View)this.mNavButtonView);
      this.mHiddenViews.remove(this.mNavButtonView);
    } 
    if (this.mNavButtonView != null)
      this.mNavButtonView.setImageDrawable(paramDrawable); 
  }
  
  public void setNavigationOnClickListener(View.OnClickListener paramOnClickListener) {
    ensureNavButtonView();
    this.mNavButtonView.setOnClickListener(paramOnClickListener);
  }
  
  public void setOnMenuItemClickListener(OnMenuItemClickListener paramOnMenuItemClickListener) {
    this.mOnMenuItemClickListener = paramOnMenuItemClickListener;
  }
  
  public void setOverflowIcon(@Nullable Drawable paramDrawable) {
    ensureMenu();
    this.mMenuView.setOverflowIcon(paramDrawable);
  }
  
  public void setPopupTheme(@StyleRes int paramInt) {
    if (this.mPopupTheme != paramInt) {
      this.mPopupTheme = paramInt;
      if (paramInt == 0) {
        this.mPopupContext = getContext();
        return;
      } 
    } else {
      return;
    } 
    this.mPopupContext = (Context)new ContextThemeWrapper(getContext(), paramInt);
  }
  
  public void setSubtitle(@StringRes int paramInt) {
    setSubtitle(getContext().getText(paramInt));
  }
  
  public void setSubtitle(CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence)) {
      if (this.mSubtitleTextView == null) {
        Context context = getContext();
        this.mSubtitleTextView = new TextView(context);
        this.mSubtitleTextView.setSingleLine();
        this.mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        if (this.mSubtitleTextAppearance != 0)
          this.mSubtitleTextView.setTextAppearance(context, this.mSubtitleTextAppearance); 
        if (this.mSubtitleTextColor != 0)
          this.mSubtitleTextView.setTextColor(this.mSubtitleTextColor); 
      } 
      if (!isChildOrHidden((View)this.mSubtitleTextView))
        addSystemView((View)this.mSubtitleTextView, true); 
    } else if (this.mSubtitleTextView != null && isChildOrHidden((View)this.mSubtitleTextView)) {
      removeView((View)this.mSubtitleTextView);
      this.mHiddenViews.remove(this.mSubtitleTextView);
    } 
    if (this.mSubtitleTextView != null)
      this.mSubtitleTextView.setText(paramCharSequence); 
    this.mSubtitleText = paramCharSequence;
  }
  
  public void setSubtitleTextAppearance(Context paramContext, @StyleRes int paramInt) {
    this.mSubtitleTextAppearance = paramInt;
    if (this.mSubtitleTextView != null)
      this.mSubtitleTextView.setTextAppearance(paramContext, paramInt); 
  }
  
  public void setSubtitleTextColor(@ColorInt int paramInt) {
    this.mSubtitleTextColor = paramInt;
    if (this.mSubtitleTextView != null)
      this.mSubtitleTextView.setTextColor(paramInt); 
  }
  
  public void setTitle(@StringRes int paramInt) {
    setTitle(getContext().getText(paramInt));
  }
  
  public void setTitle(CharSequence paramCharSequence) {
    if (!TextUtils.isEmpty(paramCharSequence)) {
      if (this.mTitleTextView == null) {
        Context context = getContext();
        this.mTitleTextView = new TextView(context);
        this.mTitleTextView.setSingleLine();
        this.mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        if (this.mTitleTextAppearance != 0)
          this.mTitleTextView.setTextAppearance(context, this.mTitleTextAppearance); 
        if (this.mTitleTextColor != 0)
          this.mTitleTextView.setTextColor(this.mTitleTextColor); 
      } 
      if (!isChildOrHidden((View)this.mTitleTextView))
        addSystemView((View)this.mTitleTextView, true); 
    } else if (this.mTitleTextView != null && isChildOrHidden((View)this.mTitleTextView)) {
      removeView((View)this.mTitleTextView);
      this.mHiddenViews.remove(this.mTitleTextView);
    } 
    if (this.mTitleTextView != null)
      this.mTitleTextView.setText(paramCharSequence); 
    this.mTitleText = paramCharSequence;
  }
  
  public void setTitleTextAppearance(Context paramContext, @StyleRes int paramInt) {
    this.mTitleTextAppearance = paramInt;
    if (this.mTitleTextView != null)
      this.mTitleTextView.setTextAppearance(paramContext, paramInt); 
  }
  
  public void setTitleTextColor(@ColorInt int paramInt) {
    this.mTitleTextColor = paramInt;
    if (this.mTitleTextView != null)
      this.mTitleTextView.setTextColor(paramInt); 
  }
  
  public boolean showOverflowMenu() {
    return (this.mMenuView != null && this.mMenuView.showOverflowMenu());
  }
  
  private class ExpandedActionViewMenuPresenter implements MenuPresenter {
    MenuItemImpl mCurrentExpandedItem;
    
    MenuBuilder mMenu;
    
    private ExpandedActionViewMenuPresenter() {}
    
    public boolean collapseItemActionView(MenuBuilder param1MenuBuilder, MenuItemImpl param1MenuItemImpl) {
      if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView)
        ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewCollapsed(); 
      Toolbar.this.removeView(Toolbar.this.mExpandedActionView);
      Toolbar.this.removeView((View)Toolbar.this.mCollapseButtonView);
      Toolbar.this.mExpandedActionView = null;
      Toolbar.this.addChildrenForExpandedActionView();
      this.mCurrentExpandedItem = null;
      Toolbar.this.requestLayout();
      param1MenuItemImpl.setActionViewExpanded(false);
      return true;
    }
    
    public boolean expandItemActionView(MenuBuilder param1MenuBuilder, MenuItemImpl param1MenuItemImpl) {
      Toolbar.this.ensureCollapseButtonView();
      if (Toolbar.this.mCollapseButtonView.getParent() != Toolbar.this)
        Toolbar.this.addView((View)Toolbar.this.mCollapseButtonView); 
      Toolbar.this.mExpandedActionView = param1MenuItemImpl.getActionView();
      this.mCurrentExpandedItem = param1MenuItemImpl;
      if (Toolbar.this.mExpandedActionView.getParent() != Toolbar.this) {
        Toolbar.LayoutParams layoutParams = Toolbar.this.generateDefaultLayoutParams();
        layoutParams.gravity = 0x800003 | Toolbar.this.mButtonGravity & 0x70;
        layoutParams.mViewType = 2;
        Toolbar.this.mExpandedActionView.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        Toolbar.this.addView(Toolbar.this.mExpandedActionView);
      } 
      Toolbar.this.removeChildrenForExpandedActionView();
      Toolbar.this.requestLayout();
      param1MenuItemImpl.setActionViewExpanded(true);
      if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView)
        ((CollapsibleActionView)Toolbar.this.mExpandedActionView).onActionViewExpanded(); 
      return true;
    }
    
    public boolean flagActionItems() {
      return false;
    }
    
    public int getId() {
      return 0;
    }
    
    public MenuView getMenuView(ViewGroup param1ViewGroup) {
      return null;
    }
    
    public void initForMenu(Context param1Context, MenuBuilder param1MenuBuilder) {
      if (this.mMenu != null && this.mCurrentExpandedItem != null)
        this.mMenu.collapseItemActionView(this.mCurrentExpandedItem); 
      this.mMenu = param1MenuBuilder;
    }
    
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {}
    
    public void onRestoreInstanceState(Parcelable param1Parcelable) {}
    
    public Parcelable onSaveInstanceState() {
      return null;
    }
    
    public boolean onSubMenuSelected(SubMenuBuilder param1SubMenuBuilder) {
      return false;
    }
    
    public void setCallback(MenuPresenter.Callback param1Callback) {}
    
    public void updateMenuView(boolean param1Boolean) {
      boolean bool;
      if (this.mCurrentExpandedItem != null) {
        boolean bool1 = false;
        bool = bool1;
        if (this.mMenu != null) {
          int j = this.mMenu.size();
          int i = 0;
          while (true) {
            bool = bool1;
            if (i < j)
              if (this.mMenu.getItem(i) == this.mCurrentExpandedItem) {
                bool = true;
              } else {
                i++;
                continue;
              }  
            if (!bool)
              collapseItemActionView(this.mMenu, this.mCurrentExpandedItem); 
            return;
          } 
        } 
      } else {
        return;
      } 
      if (!bool)
        collapseItemActionView(this.mMenu, this.mCurrentExpandedItem); 
    }
  }
  
  public static class LayoutParams extends ActionBar.LayoutParams {
    static final int CUSTOM = 0;
    
    static final int EXPANDED = 2;
    
    static final int SYSTEM = 1;
    
    int mViewType = 0;
    
    public LayoutParams(int param1Int) {
      this(-2, -1, param1Int);
    }
    
    public LayoutParams(int param1Int1, int param1Int2) {
      super(param1Int1, param1Int2);
      this.gravity = 8388627;
    }
    
    public LayoutParams(int param1Int1, int param1Int2, int param1Int3) {
      super(param1Int1, param1Int2);
      this.gravity = param1Int3;
    }
    
    public LayoutParams(@NonNull Context param1Context, AttributeSet param1AttributeSet) {
      super(param1Context, param1AttributeSet);
    }
    
    public LayoutParams(ActionBar.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
      this.mViewType = param1LayoutParams.mViewType;
    }
    
    public LayoutParams(ViewGroup.LayoutParams param1LayoutParams) {
      super(param1LayoutParams);
    }
    
    public LayoutParams(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      super((ViewGroup.LayoutParams)param1MarginLayoutParams);
      copyMarginsFromCompat(param1MarginLayoutParams);
    }
    
    void copyMarginsFromCompat(ViewGroup.MarginLayoutParams param1MarginLayoutParams) {
      this.leftMargin = param1MarginLayoutParams.leftMargin;
      this.topMargin = param1MarginLayoutParams.topMargin;
      this.rightMargin = param1MarginLayoutParams.rightMargin;
      this.bottomMargin = param1MarginLayoutParams.bottomMargin;
    }
  }
  
  public static interface OnMenuItemClickListener {
    boolean onMenuItemClick(MenuItem param1MenuItem);
  }
  
  public static class SavedState extends View.BaseSavedState {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public Toolbar.SavedState createFromParcel(Parcel param2Parcel) {
          return new Toolbar.SavedState(param2Parcel);
        }
        
        public Toolbar.SavedState[] newArray(int param2Int) {
          return new Toolbar.SavedState[param2Int];
        }
      };
    
    int expandedMenuItemId;
    
    boolean isOverflowOpen;
    
    public SavedState(Parcel param1Parcel) {
      super(param1Parcel);
      boolean bool;
      this.expandedMenuItemId = param1Parcel.readInt();
      if (param1Parcel.readInt() != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      this.isOverflowOpen = bool;
    }
    
    public SavedState(Parcelable param1Parcelable) {
      super(param1Parcelable);
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      super.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeInt(this.expandedMenuItemId);
      if (this.isOverflowOpen) {
        param1Int = 1;
      } else {
        param1Int = 0;
      } 
      param1Parcel.writeInt(param1Int);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public Toolbar.SavedState createFromParcel(Parcel param1Parcel) {
      return new Toolbar.SavedState(param1Parcel);
    }
    
    public Toolbar.SavedState[] newArray(int param1Int) {
      return new Toolbar.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\Toolbar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */