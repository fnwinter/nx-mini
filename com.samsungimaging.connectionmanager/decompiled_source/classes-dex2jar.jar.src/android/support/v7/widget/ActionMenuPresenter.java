package android.support.v7.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ActionProvider;
import android.support.v7.appcompat.R;
import android.support.v7.transition.ActionBarTransition;
import android.support.v7.view.ActionBarPolicy;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.BaseMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.view.menu.MenuView;
import android.support.v7.view.menu.SubMenuBuilder;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

class ActionMenuPresenter extends BaseMenuPresenter implements ActionProvider.SubUiVisibilityListener {
  private static final String TAG = "ActionMenuPresenter";
  
  private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
  
  private ActionButtonSubmenu mActionButtonPopup;
  
  private int mActionItemWidthLimit;
  
  private boolean mExpandedActionViewsExclusive;
  
  private int mMaxItems;
  
  private boolean mMaxItemsSet;
  
  private int mMinCellSize;
  
  int mOpenSubMenuId;
  
  private OverflowMenuButton mOverflowButton;
  
  private OverflowPopup mOverflowPopup;
  
  private Drawable mPendingOverflowIcon;
  
  private boolean mPendingOverflowIconSet;
  
  private ActionMenuPopupCallback mPopupCallback;
  
  final PopupPresenterCallback mPopupPresenterCallback = new PopupPresenterCallback();
  
  private OpenOverflowRunnable mPostedOpenRunnable;
  
  private boolean mReserveOverflow;
  
  private boolean mReserveOverflowSet;
  
  private View mScrapActionButtonView;
  
  private boolean mStrictWidthLimit;
  
  private int mWidthLimit;
  
  private boolean mWidthLimitSet;
  
  public ActionMenuPresenter(Context paramContext) {
    super(paramContext, R.layout.abc_action_menu_layout, R.layout.abc_action_menu_item_layout);
  }
  
  private View findViewForItem(MenuItem paramMenuItem) {
    ViewGroup viewGroup = (ViewGroup)this.mMenuView;
    if (viewGroup == null)
      return null; 
    int j = viewGroup.getChildCount();
    for (int i = 0; i < j; i++) {
      View view = viewGroup.getChildAt(i);
      if (view instanceof MenuView.ItemView) {
        View view1 = view;
        if (((MenuView.ItemView)view).getItemData() != paramMenuItem)
          continue; 
        return view1;
      } 
      continue;
    } 
    return null;
  }
  
  public void bindItemView(MenuItemImpl paramMenuItemImpl, MenuView.ItemView paramItemView) {
    paramItemView.initialize(paramMenuItemImpl, 0);
    ActionMenuView actionMenuView = (ActionMenuView)this.mMenuView;
    ActionMenuItemView actionMenuItemView = (ActionMenuItemView)paramItemView;
    actionMenuItemView.setItemInvoker(actionMenuView);
    if (this.mPopupCallback == null)
      this.mPopupCallback = new ActionMenuPopupCallback(); 
    actionMenuItemView.setPopupCallback(this.mPopupCallback);
  }
  
  public boolean dismissPopupMenus() {
    return hideOverflowMenu() | hideSubMenus();
  }
  
  public boolean filterLeftoverView(ViewGroup paramViewGroup, int paramInt) {
    return (paramViewGroup.getChildAt(paramInt) == this.mOverflowButton) ? false : super.filterLeftoverView(paramViewGroup, paramInt);
  }
  
  public boolean flagActionItems() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mMenu : Landroid/support/v7/view/menu/MenuBuilder;
    //   4: invokevirtual getVisibleItems : ()Ljava/util/ArrayList;
    //   7: astore #16
    //   9: aload #16
    //   11: invokevirtual size : ()I
    //   14: istore #10
    //   16: aload_0
    //   17: getfield mMaxItems : I
    //   20: istore_1
    //   21: aload_0
    //   22: getfield mActionItemWidthLimit : I
    //   25: istore #9
    //   27: iconst_0
    //   28: iconst_0
    //   29: invokestatic makeMeasureSpec : (II)I
    //   32: istore #11
    //   34: aload_0
    //   35: getfield mMenuView : Landroid/support/v7/view/menu/MenuView;
    //   38: checkcast android/view/ViewGroup
    //   41: astore #17
    //   43: iconst_0
    //   44: istore_2
    //   45: iconst_0
    //   46: istore #4
    //   48: iconst_0
    //   49: istore #7
    //   51: iconst_0
    //   52: istore #5
    //   54: iconst_0
    //   55: istore_3
    //   56: iload_3
    //   57: iload #10
    //   59: if_icmpge -> 142
    //   62: aload #16
    //   64: iload_3
    //   65: invokevirtual get : (I)Ljava/lang/Object;
    //   68: checkcast android/support/v7/view/menu/MenuItemImpl
    //   71: astore #18
    //   73: aload #18
    //   75: invokevirtual requiresActionButton : ()Z
    //   78: ifeq -> 119
    //   81: iload_2
    //   82: iconst_1
    //   83: iadd
    //   84: istore_2
    //   85: iload_1
    //   86: istore #6
    //   88: aload_0
    //   89: getfield mExpandedActionViewsExclusive : Z
    //   92: ifeq -> 109
    //   95: iload_1
    //   96: istore #6
    //   98: aload #18
    //   100: invokevirtual isActionViewExpanded : ()Z
    //   103: ifeq -> 109
    //   106: iconst_0
    //   107: istore #6
    //   109: iload_3
    //   110: iconst_1
    //   111: iadd
    //   112: istore_3
    //   113: iload #6
    //   115: istore_1
    //   116: goto -> 56
    //   119: aload #18
    //   121: invokevirtual requestsActionButton : ()Z
    //   124: ifeq -> 136
    //   127: iload #4
    //   129: iconst_1
    //   130: iadd
    //   131: istore #4
    //   133: goto -> 85
    //   136: iconst_1
    //   137: istore #5
    //   139: goto -> 85
    //   142: iload_1
    //   143: istore_3
    //   144: aload_0
    //   145: getfield mReserveOverflow : Z
    //   148: ifeq -> 170
    //   151: iload #5
    //   153: ifne -> 166
    //   156: iload_1
    //   157: istore_3
    //   158: iload_2
    //   159: iload #4
    //   161: iadd
    //   162: iload_1
    //   163: if_icmple -> 170
    //   166: iload_1
    //   167: iconst_1
    //   168: isub
    //   169: istore_3
    //   170: iload_3
    //   171: iload_2
    //   172: isub
    //   173: istore_3
    //   174: aload_0
    //   175: getfield mActionButtonGroups : Landroid/util/SparseBooleanArray;
    //   178: astore #18
    //   180: aload #18
    //   182: invokevirtual clear : ()V
    //   185: iconst_0
    //   186: istore #8
    //   188: iconst_0
    //   189: istore_2
    //   190: aload_0
    //   191: getfield mStrictWidthLimit : Z
    //   194: ifeq -> 223
    //   197: iload #9
    //   199: aload_0
    //   200: getfield mMinCellSize : I
    //   203: idiv
    //   204: istore_2
    //   205: aload_0
    //   206: getfield mMinCellSize : I
    //   209: istore_1
    //   210: aload_0
    //   211: getfield mMinCellSize : I
    //   214: iload #9
    //   216: iload_1
    //   217: irem
    //   218: iload_2
    //   219: idiv
    //   220: iadd
    //   221: istore #8
    //   223: iconst_0
    //   224: istore_1
    //   225: iload #9
    //   227: istore #5
    //   229: iload_1
    //   230: istore #9
    //   232: iload #7
    //   234: istore_1
    //   235: iload #9
    //   237: iload #10
    //   239: if_icmpge -> 759
    //   242: aload #16
    //   244: iload #9
    //   246: invokevirtual get : (I)Ljava/lang/Object;
    //   249: checkcast android/support/v7/view/menu/MenuItemImpl
    //   252: astore #19
    //   254: aload #19
    //   256: invokevirtual requiresActionButton : ()Z
    //   259: ifeq -> 386
    //   262: aload_0
    //   263: aload #19
    //   265: aload_0
    //   266: getfield mScrapActionButtonView : Landroid/view/View;
    //   269: aload #17
    //   271: invokevirtual getItemView : (Landroid/support/v7/view/menu/MenuItemImpl;Landroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    //   274: astore #20
    //   276: aload_0
    //   277: getfield mScrapActionButtonView : Landroid/view/View;
    //   280: ifnonnull -> 289
    //   283: aload_0
    //   284: aload #20
    //   286: putfield mScrapActionButtonView : Landroid/view/View;
    //   289: aload_0
    //   290: getfield mStrictWidthLimit : Z
    //   293: ifeq -> 374
    //   296: iload_2
    //   297: aload #20
    //   299: iload #8
    //   301: iload_2
    //   302: iload #11
    //   304: iconst_0
    //   305: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   308: isub
    //   309: istore_2
    //   310: aload #20
    //   312: invokevirtual getMeasuredWidth : ()I
    //   315: istore #6
    //   317: iload #5
    //   319: iload #6
    //   321: isub
    //   322: istore #4
    //   324: iload_1
    //   325: istore #5
    //   327: iload_1
    //   328: ifne -> 335
    //   331: iload #6
    //   333: istore #5
    //   335: aload #19
    //   337: invokevirtual getGroupId : ()I
    //   340: istore_1
    //   341: iload_1
    //   342: ifeq -> 352
    //   345: aload #18
    //   347: iload_1
    //   348: iconst_1
    //   349: invokevirtual put : (IZ)V
    //   352: aload #19
    //   354: iconst_1
    //   355: invokevirtual setIsActionButton : (Z)V
    //   358: iload #5
    //   360: istore_1
    //   361: iload #9
    //   363: iconst_1
    //   364: iadd
    //   365: istore #9
    //   367: iload #4
    //   369: istore #5
    //   371: goto -> 235
    //   374: aload #20
    //   376: iload #11
    //   378: iload #11
    //   380: invokevirtual measure : (II)V
    //   383: goto -> 310
    //   386: aload #19
    //   388: invokevirtual requestsActionButton : ()Z
    //   391: ifeq -> 746
    //   394: aload #19
    //   396: invokevirtual getGroupId : ()I
    //   399: istore #12
    //   401: aload #18
    //   403: iload #12
    //   405: invokevirtual get : (I)Z
    //   408: istore #15
    //   410: iload_3
    //   411: ifgt -> 419
    //   414: iload #15
    //   416: ifeq -> 619
    //   419: iload #5
    //   421: ifle -> 619
    //   424: aload_0
    //   425: getfield mStrictWidthLimit : Z
    //   428: ifeq -> 435
    //   431: iload_2
    //   432: ifle -> 619
    //   435: iconst_1
    //   436: istore #13
    //   438: iload_2
    //   439: istore #7
    //   441: iload_1
    //   442: istore #6
    //   444: iload #13
    //   446: istore #14
    //   448: iload #5
    //   450: istore #4
    //   452: iload #13
    //   454: ifeq -> 572
    //   457: aload_0
    //   458: aload #19
    //   460: aload_0
    //   461: getfield mScrapActionButtonView : Landroid/view/View;
    //   464: aload #17
    //   466: invokevirtual getItemView : (Landroid/support/v7/view/menu/MenuItemImpl;Landroid/view/View;Landroid/view/ViewGroup;)Landroid/view/View;
    //   469: astore #20
    //   471: aload_0
    //   472: getfield mScrapActionButtonView : Landroid/view/View;
    //   475: ifnonnull -> 484
    //   478: aload_0
    //   479: aload #20
    //   481: putfield mScrapActionButtonView : Landroid/view/View;
    //   484: aload_0
    //   485: getfield mStrictWidthLimit : Z
    //   488: ifeq -> 625
    //   491: aload #20
    //   493: iload #8
    //   495: iload_2
    //   496: iload #11
    //   498: iconst_0
    //   499: invokestatic measureChildForCells : (Landroid/view/View;IIII)I
    //   502: istore #6
    //   504: iload_2
    //   505: iload #6
    //   507: isub
    //   508: istore #4
    //   510: iload #4
    //   512: istore_2
    //   513: iload #6
    //   515: ifne -> 524
    //   518: iconst_0
    //   519: istore #13
    //   521: iload #4
    //   523: istore_2
    //   524: aload #20
    //   526: invokevirtual getMeasuredWidth : ()I
    //   529: istore #7
    //   531: iload #5
    //   533: iload #7
    //   535: isub
    //   536: istore #4
    //   538: iload_1
    //   539: istore #6
    //   541: iload_1
    //   542: ifne -> 549
    //   545: iload #7
    //   547: istore #6
    //   549: aload_0
    //   550: getfield mStrictWidthLimit : Z
    //   553: ifeq -> 642
    //   556: iload #4
    //   558: iflt -> 637
    //   561: iconst_1
    //   562: istore_1
    //   563: iload #13
    //   565: iload_1
    //   566: iand
    //   567: istore #14
    //   569: iload_2
    //   570: istore #7
    //   572: iload #14
    //   574: ifeq -> 669
    //   577: iload #12
    //   579: ifeq -> 669
    //   582: aload #18
    //   584: iload #12
    //   586: iconst_1
    //   587: invokevirtual put : (IZ)V
    //   590: iload_3
    //   591: istore_1
    //   592: iload_1
    //   593: istore_3
    //   594: iload #14
    //   596: ifeq -> 603
    //   599: iload_1
    //   600: iconst_1
    //   601: isub
    //   602: istore_3
    //   603: aload #19
    //   605: iload #14
    //   607: invokevirtual setIsActionButton : (Z)V
    //   610: iload #7
    //   612: istore_2
    //   613: iload #6
    //   615: istore_1
    //   616: goto -> 361
    //   619: iconst_0
    //   620: istore #13
    //   622: goto -> 438
    //   625: aload #20
    //   627: iload #11
    //   629: iload #11
    //   631: invokevirtual measure : (II)V
    //   634: goto -> 524
    //   637: iconst_0
    //   638: istore_1
    //   639: goto -> 563
    //   642: iload #4
    //   644: iload #6
    //   646: iadd
    //   647: ifle -> 664
    //   650: iconst_1
    //   651: istore_1
    //   652: iload #13
    //   654: iload_1
    //   655: iand
    //   656: istore #14
    //   658: iload_2
    //   659: istore #7
    //   661: goto -> 572
    //   664: iconst_0
    //   665: istore_1
    //   666: goto -> 652
    //   669: iload_3
    //   670: istore_1
    //   671: iload #15
    //   673: ifeq -> 592
    //   676: aload #18
    //   678: iload #12
    //   680: iconst_0
    //   681: invokevirtual put : (IZ)V
    //   684: iconst_0
    //   685: istore_2
    //   686: iload_3
    //   687: istore_1
    //   688: iload_2
    //   689: iload #9
    //   691: if_icmpge -> 592
    //   694: aload #16
    //   696: iload_2
    //   697: invokevirtual get : (I)Ljava/lang/Object;
    //   700: checkcast android/support/v7/view/menu/MenuItemImpl
    //   703: astore #20
    //   705: iload_3
    //   706: istore_1
    //   707: aload #20
    //   709: invokevirtual getGroupId : ()I
    //   712: iload #12
    //   714: if_icmpne -> 737
    //   717: iload_3
    //   718: istore_1
    //   719: aload #20
    //   721: invokevirtual isActionButton : ()Z
    //   724: ifeq -> 731
    //   727: iload_3
    //   728: iconst_1
    //   729: iadd
    //   730: istore_1
    //   731: aload #20
    //   733: iconst_0
    //   734: invokevirtual setIsActionButton : (Z)V
    //   737: iload_2
    //   738: iconst_1
    //   739: iadd
    //   740: istore_2
    //   741: iload_1
    //   742: istore_3
    //   743: goto -> 686
    //   746: aload #19
    //   748: iconst_0
    //   749: invokevirtual setIsActionButton : (Z)V
    //   752: iload #5
    //   754: istore #4
    //   756: goto -> 361
    //   759: iconst_1
    //   760: ireturn
  }
  
  public View getItemView(MenuItemImpl paramMenuItemImpl, View paramView, ViewGroup paramViewGroup) {
    boolean bool;
    View view = paramMenuItemImpl.getActionView();
    if (view == null || paramMenuItemImpl.hasCollapsibleActionView())
      view = super.getItemView(paramMenuItemImpl, paramView, paramViewGroup); 
    if (paramMenuItemImpl.isActionViewExpanded()) {
      bool = true;
    } else {
      bool = false;
    } 
    view.setVisibility(bool);
    ActionMenuView actionMenuView = (ActionMenuView)paramViewGroup;
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    if (!actionMenuView.checkLayoutParams(layoutParams))
      view.setLayoutParams((ViewGroup.LayoutParams)actionMenuView.generateLayoutParams(layoutParams)); 
    return view;
  }
  
  public MenuView getMenuView(ViewGroup paramViewGroup) {
    MenuView menuView = super.getMenuView(paramViewGroup);
    ((ActionMenuView)menuView).setPresenter(this);
    return menuView;
  }
  
  public Drawable getOverflowIcon() {
    return (this.mOverflowButton != null) ? this.mOverflowButton.getDrawable() : (this.mPendingOverflowIconSet ? this.mPendingOverflowIcon : null);
  }
  
  public boolean hideOverflowMenu() {
    if (this.mPostedOpenRunnable != null && this.mMenuView != null) {
      ((View)this.mMenuView).removeCallbacks(this.mPostedOpenRunnable);
      this.mPostedOpenRunnable = null;
      return true;
    } 
    OverflowPopup overflowPopup = this.mOverflowPopup;
    if (overflowPopup != null) {
      overflowPopup.dismiss();
      return true;
    } 
    return false;
  }
  
  public boolean hideSubMenus() {
    if (this.mActionButtonPopup != null) {
      this.mActionButtonPopup.dismiss();
      return true;
    } 
    return false;
  }
  
  public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder) {
    super.initForMenu(paramContext, paramMenuBuilder);
    Resources resources = paramContext.getResources();
    ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(paramContext);
    if (!this.mReserveOverflowSet)
      this.mReserveOverflow = actionBarPolicy.showsOverflowMenuButton(); 
    if (!this.mWidthLimitSet)
      this.mWidthLimit = actionBarPolicy.getEmbeddedMenuWidthLimit(); 
    if (!this.mMaxItemsSet)
      this.mMaxItems = actionBarPolicy.getMaxActionButtons(); 
    int i = this.mWidthLimit;
    if (this.mReserveOverflow) {
      if (this.mOverflowButton == null) {
        this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
        if (this.mPendingOverflowIconSet) {
          this.mOverflowButton.setImageDrawable(this.mPendingOverflowIcon);
          this.mPendingOverflowIcon = null;
          this.mPendingOverflowIconSet = false;
        } 
        int j = View.MeasureSpec.makeMeasureSpec(0, 0);
        this.mOverflowButton.measure(j, j);
      } 
      i -= this.mOverflowButton.getMeasuredWidth();
    } else {
      this.mOverflowButton = null;
    } 
    this.mActionItemWidthLimit = i;
    this.mMinCellSize = (int)(56.0F * (resources.getDisplayMetrics()).density);
    this.mScrapActionButtonView = null;
  }
  
  public boolean isOverflowMenuShowPending() {
    return (this.mPostedOpenRunnable != null || isOverflowMenuShowing());
  }
  
  public boolean isOverflowMenuShowing() {
    return (this.mOverflowPopup != null && this.mOverflowPopup.isShowing());
  }
  
  public boolean isOverflowReserved() {
    return this.mReserveOverflow;
  }
  
  public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
    dismissPopupMenus();
    super.onCloseMenu(paramMenuBuilder, paramBoolean);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    if (!this.mMaxItemsSet)
      this.mMaxItems = this.mContext.getResources().getInteger(R.integer.abc_max_action_buttons); 
    if (this.mMenu != null)
      this.mMenu.onItemsChanged(true); 
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable) {
    paramParcelable = paramParcelable;
    if (((SavedState)paramParcelable).openSubMenuId > 0) {
      MenuItem menuItem = this.mMenu.findItem(((SavedState)paramParcelable).openSubMenuId);
      if (menuItem != null)
        onSubMenuSelected((SubMenuBuilder)menuItem.getSubMenu()); 
    } 
  }
  
  public Parcelable onSaveInstanceState() {
    SavedState savedState = new SavedState();
    savedState.openSubMenuId = this.mOpenSubMenuId;
    return savedState;
  }
  
  public boolean onSubMenuSelected(SubMenuBuilder paramSubMenuBuilder) {
    OverflowMenuButton overflowMenuButton;
    if (!paramSubMenuBuilder.hasVisibleItems())
      return false; 
    SubMenuBuilder subMenuBuilder;
    for (subMenuBuilder = paramSubMenuBuilder; subMenuBuilder.getParentMenu() != this.mMenu; subMenuBuilder = (SubMenuBuilder)subMenuBuilder.getParentMenu());
    View view2 = findViewForItem(subMenuBuilder.getItem());
    View view1 = view2;
    if (view2 == null) {
      if (this.mOverflowButton != null) {
        overflowMenuButton = this.mOverflowButton;
        this.mOpenSubMenuId = paramSubMenuBuilder.getItem().getItemId();
        this.mActionButtonPopup = new ActionButtonSubmenu(this.mContext, paramSubMenuBuilder);
        this.mActionButtonPopup.setAnchorView((View)overflowMenuButton);
        this.mActionButtonPopup.show();
        super.onSubMenuSelected(paramSubMenuBuilder);
        return true;
      } 
      return false;
    } 
    this.mOpenSubMenuId = paramSubMenuBuilder.getItem().getItemId();
    this.mActionButtonPopup = new ActionButtonSubmenu(this.mContext, paramSubMenuBuilder);
    this.mActionButtonPopup.setAnchorView((View)overflowMenuButton);
    this.mActionButtonPopup.show();
    super.onSubMenuSelected(paramSubMenuBuilder);
    return true;
  }
  
  public void onSubUiVisibilityChanged(boolean paramBoolean) {
    if (paramBoolean) {
      super.onSubMenuSelected(null);
      return;
    } 
    this.mMenu.close(false);
  }
  
  public void setExpandedActionViewsExclusive(boolean paramBoolean) {
    this.mExpandedActionViewsExclusive = paramBoolean;
  }
  
  public void setItemLimit(int paramInt) {
    this.mMaxItems = paramInt;
    this.mMaxItemsSet = true;
  }
  
  public void setMenuView(ActionMenuView paramActionMenuView) {
    this.mMenuView = paramActionMenuView;
    paramActionMenuView.initialize(this.mMenu);
  }
  
  public void setOverflowIcon(Drawable paramDrawable) {
    if (this.mOverflowButton != null) {
      this.mOverflowButton.setImageDrawable(paramDrawable);
      return;
    } 
    this.mPendingOverflowIconSet = true;
    this.mPendingOverflowIcon = paramDrawable;
  }
  
  public void setReserveOverflow(boolean paramBoolean) {
    this.mReserveOverflow = paramBoolean;
    this.mReserveOverflowSet = true;
  }
  
  public void setWidthLimit(int paramInt, boolean paramBoolean) {
    this.mWidthLimit = paramInt;
    this.mStrictWidthLimit = paramBoolean;
    this.mWidthLimitSet = true;
  }
  
  public boolean shouldIncludeItem(int paramInt, MenuItemImpl paramMenuItemImpl) {
    return paramMenuItemImpl.isActionButton();
  }
  
  public boolean showOverflowMenu() {
    if (this.mReserveOverflow && !isOverflowMenuShowing() && this.mMenu != null && this.mMenuView != null && this.mPostedOpenRunnable == null && !this.mMenu.getNonActionItems().isEmpty()) {
      this.mPostedOpenRunnable = new OpenOverflowRunnable(new OverflowPopup(this.mContext, this.mMenu, (View)this.mOverflowButton, true));
      ((View)this.mMenuView).post(this.mPostedOpenRunnable);
      super.onSubMenuSelected(null);
      return true;
    } 
    return false;
  }
  
  public void updateMenuView(boolean paramBoolean) {
    ViewGroup<MenuItemImpl> viewGroup = (ViewGroup)((View)this.mMenuView).getParent();
    if (viewGroup != null)
      ActionBarTransition.beginDelayedTransition(viewGroup); 
    super.updateMenuView(paramBoolean);
    ((View)this.mMenuView).requestLayout();
    if (this.mMenu != null) {
      ArrayList<MenuItemImpl> arrayList = this.mMenu.getActionItems();
      int k = arrayList.size();
      for (int j = 0; j < k; j++) {
        ActionProvider actionProvider = ((MenuItemImpl)arrayList.get(j)).getSupportActionProvider();
        if (actionProvider != null)
          actionProvider.setSubUiVisibilityListener(this); 
      } 
    } 
    if (this.mMenu != null) {
      ArrayList arrayList = this.mMenu.getNonActionItems();
    } else {
      viewGroup = null;
    } 
    byte b = 0;
    int i = b;
    if (this.mReserveOverflow) {
      i = b;
      if (viewGroup != null) {
        i = viewGroup.size();
        if (i == 1) {
          if (!((MenuItemImpl)viewGroup.get(0)).isActionViewExpanded()) {
            i = 1;
          } else {
            i = 0;
          } 
        } else if (i > 0) {
          i = 1;
        } else {
          i = 0;
        } 
      } 
    } 
    if (i != 0) {
      if (this.mOverflowButton == null)
        this.mOverflowButton = new OverflowMenuButton(this.mSystemContext); 
      viewGroup = (ViewGroup<MenuItemImpl>)this.mOverflowButton.getParent();
      if (viewGroup != this.mMenuView) {
        if (viewGroup != null)
          viewGroup.removeView((View)this.mOverflowButton); 
        viewGroup = (ActionMenuView)this.mMenuView;
        viewGroup.addView((View)this.mOverflowButton, (ViewGroup.LayoutParams)viewGroup.generateOverflowButtonLayoutParams());
      } 
    } else if (this.mOverflowButton != null && this.mOverflowButton.getParent() == this.mMenuView) {
      ((ViewGroup)this.mMenuView).removeView((View)this.mOverflowButton);
    } 
    ((ActionMenuView)this.mMenuView).setOverflowReserved(this.mReserveOverflow);
  }
  
  private class ActionButtonSubmenu extends MenuPopupHelper {
    private SubMenuBuilder mSubMenu;
    
    public ActionButtonSubmenu(Context param1Context, SubMenuBuilder param1SubMenuBuilder) {
      super(param1Context, (MenuBuilder)param1SubMenuBuilder, null, false, R.attr.actionOverflowMenuStyle);
      this.mSubMenu = param1SubMenuBuilder;
      if (!((MenuItemImpl)param1SubMenuBuilder.getItem()).isActionButton()) {
        ActionMenuPresenter.OverflowMenuButton overflowMenuButton;
        if (ActionMenuPresenter.this.mOverflowButton == null) {
          View view = (View)ActionMenuPresenter.this.mMenuView;
        } else {
          overflowMenuButton = ActionMenuPresenter.this.mOverflowButton;
        } 
        setAnchorView((View)overflowMenuButton);
      } 
      setCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
      boolean bool = false;
      int j = param1SubMenuBuilder.size();
      int i = 0;
      while (true) {
        boolean bool1 = bool;
        if (i < j) {
          MenuItem menuItem = param1SubMenuBuilder.getItem(i);
          if (menuItem.isVisible() && menuItem.getIcon() != null) {
            bool1 = true;
          } else {
            i++;
            continue;
          } 
        } 
        setForceShowIcon(bool1);
        return;
      } 
    }
    
    public void onDismiss() {
      super.onDismiss();
      ActionMenuPresenter.access$802(ActionMenuPresenter.this, (ActionButtonSubmenu)null);
      ActionMenuPresenter.this.mOpenSubMenuId = 0;
    }
  }
  
  private class ActionMenuPopupCallback extends ActionMenuItemView.PopupCallback {
    private ActionMenuPopupCallback() {}
    
    public ListPopupWindow getPopup() {
      return (ActionMenuPresenter.this.mActionButtonPopup != null) ? ActionMenuPresenter.this.mActionButtonPopup.getPopup() : null;
    }
  }
  
  private class OpenOverflowRunnable implements Runnable {
    private ActionMenuPresenter.OverflowPopup mPopup;
    
    public OpenOverflowRunnable(ActionMenuPresenter.OverflowPopup param1OverflowPopup) {
      this.mPopup = param1OverflowPopup;
    }
    
    public void run() {
      ActionMenuPresenter.this.mMenu.changeMenuMode();
      View view = (View)ActionMenuPresenter.this.mMenuView;
      if (view != null && view.getWindowToken() != null && this.mPopup.tryShow())
        ActionMenuPresenter.access$202(ActionMenuPresenter.this, this.mPopup); 
      ActionMenuPresenter.access$302(ActionMenuPresenter.this, (OpenOverflowRunnable)null);
    }
  }
  
  private class OverflowMenuButton extends AppCompatImageView implements ActionMenuView.ActionMenuChildView {
    private final float[] mTempPts = new float[2];
    
    public OverflowMenuButton(Context param1Context) {
      super(param1Context, (AttributeSet)null, R.attr.actionOverflowButtonStyle);
      setClickable(true);
      setFocusable(true);
      setVisibility(0);
      setEnabled(true);
      setOnTouchListener(new ListPopupWindow.ForwardingListener((View)this) {
            public ListPopupWindow getPopup() {
              return (ActionMenuPresenter.this.mOverflowPopup == null) ? null : ActionMenuPresenter.this.mOverflowPopup.getPopup();
            }
            
            public boolean onForwardingStarted() {
              ActionMenuPresenter.this.showOverflowMenu();
              return true;
            }
            
            public boolean onForwardingStopped() {
              if (ActionMenuPresenter.this.mPostedOpenRunnable != null)
                return false; 
              ActionMenuPresenter.this.hideOverflowMenu();
              return true;
            }
          });
    }
    
    public boolean needsDividerAfter() {
      return false;
    }
    
    public boolean needsDividerBefore() {
      return false;
    }
    
    public boolean performClick() {
      if (super.performClick())
        return true; 
      playSoundEffect(0);
      ActionMenuPresenter.this.showOverflowMenu();
      return true;
    }
    
    protected boolean setFrame(int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      boolean bool = super.setFrame(param1Int1, param1Int2, param1Int3, param1Int4);
      Drawable drawable1 = getDrawable();
      Drawable drawable2 = getBackground();
      if (drawable1 != null && drawable2 != null) {
        int i = getWidth();
        param1Int2 = getHeight();
        param1Int1 = Math.max(i, param1Int2) / 2;
        int j = getPaddingLeft();
        int k = getPaddingRight();
        param1Int3 = getPaddingTop();
        param1Int4 = getPaddingBottom();
        i = (i + j - k) / 2;
        param1Int2 = (param1Int2 + param1Int3 - param1Int4) / 2;
        DrawableCompat.setHotspotBounds(drawable2, i - param1Int1, param1Int2 - param1Int1, i + param1Int1, param1Int2 + param1Int1);
      } 
      return bool;
    }
  }
  
  class null extends ListPopupWindow.ForwardingListener {
    null(View param1View) {
      super(param1View);
    }
    
    public ListPopupWindow getPopup() {
      return (ActionMenuPresenter.this.mOverflowPopup == null) ? null : ActionMenuPresenter.this.mOverflowPopup.getPopup();
    }
    
    public boolean onForwardingStarted() {
      ActionMenuPresenter.this.showOverflowMenu();
      return true;
    }
    
    public boolean onForwardingStopped() {
      if (ActionMenuPresenter.this.mPostedOpenRunnable != null)
        return false; 
      ActionMenuPresenter.this.hideOverflowMenu();
      return true;
    }
  }
  
  private class OverflowPopup extends MenuPopupHelper {
    public OverflowPopup(Context param1Context, MenuBuilder param1MenuBuilder, View param1View, boolean param1Boolean) {
      super(param1Context, param1MenuBuilder, param1View, param1Boolean, R.attr.actionOverflowMenuStyle);
      setGravity(8388613);
      setCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
    }
    
    public void onDismiss() {
      super.onDismiss();
      if (ActionMenuPresenter.this.mMenu != null)
        ActionMenuPresenter.this.mMenu.close(); 
      ActionMenuPresenter.access$202(ActionMenuPresenter.this, (OverflowPopup)null);
    }
  }
  
  private class PopupPresenterCallback implements MenuPresenter.Callback {
    private PopupPresenterCallback() {}
    
    public void onCloseMenu(MenuBuilder param1MenuBuilder, boolean param1Boolean) {
      if (param1MenuBuilder instanceof SubMenuBuilder)
        ((SubMenuBuilder)param1MenuBuilder).getRootMenu().close(false); 
      MenuPresenter.Callback callback = ActionMenuPresenter.this.getCallback();
      if (callback != null)
        callback.onCloseMenu(param1MenuBuilder, param1Boolean); 
    }
    
    public boolean onOpenSubMenu(MenuBuilder param1MenuBuilder) {
      if (param1MenuBuilder == null)
        return false; 
      ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder)param1MenuBuilder).getItem().getItemId();
      MenuPresenter.Callback callback = ActionMenuPresenter.this.getCallback();
      return (callback != null) ? callback.onOpenSubMenu(param1MenuBuilder) : false;
    }
  }
  
  private static class SavedState implements Parcelable {
    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
        public ActionMenuPresenter.SavedState createFromParcel(Parcel param2Parcel) {
          return new ActionMenuPresenter.SavedState(param2Parcel);
        }
        
        public ActionMenuPresenter.SavedState[] newArray(int param2Int) {
          return new ActionMenuPresenter.SavedState[param2Int];
        }
      };
    
    public int openSubMenuId;
    
    SavedState() {}
    
    SavedState(Parcel param1Parcel) {
      this.openSubMenuId = param1Parcel.readInt();
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.openSubMenuId);
    }
  }
  
  static final class null implements Parcelable.Creator<SavedState> {
    public ActionMenuPresenter.SavedState createFromParcel(Parcel param1Parcel) {
      return new ActionMenuPresenter.SavedState(param1Parcel);
    }
    
    public ActionMenuPresenter.SavedState[] newArray(int param1Int) {
      return new ActionMenuPresenter.SavedState[param1Int];
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\ActionMenuPresenter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */