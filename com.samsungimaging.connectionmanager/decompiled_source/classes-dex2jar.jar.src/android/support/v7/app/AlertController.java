package android.support.v7.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.appcompat.R;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import java.lang.ref.WeakReference;

class AlertController {
  private ListAdapter mAdapter;
  
  private int mAlertDialogLayout;
  
  private final View.OnClickListener mButtonHandler = new View.OnClickListener() {
      public void onClick(View param1View) {
        Message message;
        if (param1View == AlertController.this.mButtonPositive && AlertController.this.mButtonPositiveMessage != null) {
          message = Message.obtain(AlertController.this.mButtonPositiveMessage);
        } else if (message == AlertController.this.mButtonNegative && AlertController.this.mButtonNegativeMessage != null) {
          message = Message.obtain(AlertController.this.mButtonNegativeMessage);
        } else if (message == AlertController.this.mButtonNeutral && AlertController.this.mButtonNeutralMessage != null) {
          message = Message.obtain(AlertController.this.mButtonNeutralMessage);
        } else {
          message = null;
        } 
        if (message != null)
          message.sendToTarget(); 
        AlertController.this.mHandler.obtainMessage(1, AlertController.this.mDialog).sendToTarget();
      }
    };
  
  private Button mButtonNegative;
  
  private Message mButtonNegativeMessage;
  
  private CharSequence mButtonNegativeText;
  
  private Button mButtonNeutral;
  
  private Message mButtonNeutralMessage;
  
  private CharSequence mButtonNeutralText;
  
  private int mButtonPanelLayoutHint = 0;
  
  private int mButtonPanelSideLayout;
  
  private Button mButtonPositive;
  
  private Message mButtonPositiveMessage;
  
  private CharSequence mButtonPositiveText;
  
  private int mCheckedItem = -1;
  
  private final Context mContext;
  
  private View mCustomTitleView;
  
  private final AppCompatDialog mDialog;
  
  private Handler mHandler;
  
  private Drawable mIcon;
  
  private int mIconId = 0;
  
  private ImageView mIconView;
  
  private int mListItemLayout;
  
  private int mListLayout;
  
  private ListView mListView;
  
  private CharSequence mMessage;
  
  private TextView mMessageView;
  
  private int mMultiChoiceItemLayout;
  
  private NestedScrollView mScrollView;
  
  private int mSingleChoiceItemLayout;
  
  private CharSequence mTitle;
  
  private TextView mTitleView;
  
  private View mView;
  
  private int mViewLayoutResId;
  
  private int mViewSpacingBottom;
  
  private int mViewSpacingLeft;
  
  private int mViewSpacingRight;
  
  private boolean mViewSpacingSpecified = false;
  
  private int mViewSpacingTop;
  
  private final Window mWindow;
  
  public AlertController(Context paramContext, AppCompatDialog paramAppCompatDialog, Window paramWindow) {
    this.mContext = paramContext;
    this.mDialog = paramAppCompatDialog;
    this.mWindow = paramWindow;
    this.mHandler = new ButtonHandler((DialogInterface)paramAppCompatDialog);
    TypedArray typedArray = paramContext.obtainStyledAttributes(null, R.styleable.AlertDialog, R.attr.alertDialogStyle, 0);
    this.mAlertDialogLayout = typedArray.getResourceId(R.styleable.AlertDialog_android_layout, 0);
    this.mButtonPanelSideLayout = typedArray.getResourceId(R.styleable.AlertDialog_buttonPanelSideLayout, 0);
    this.mListLayout = typedArray.getResourceId(R.styleable.AlertDialog_listLayout, 0);
    this.mMultiChoiceItemLayout = typedArray.getResourceId(R.styleable.AlertDialog_multiChoiceItemLayout, 0);
    this.mSingleChoiceItemLayout = typedArray.getResourceId(R.styleable.AlertDialog_singleChoiceItemLayout, 0);
    this.mListItemLayout = typedArray.getResourceId(R.styleable.AlertDialog_listItemLayout, 0);
    typedArray.recycle();
  }
  
  static boolean canTextInput(View paramView) {
    if (paramView.onCheckIsTextEditor())
      return true; 
    if (!(paramView instanceof ViewGroup))
      return false; 
    ViewGroup viewGroup = (ViewGroup)paramView;
    int i = viewGroup.getChildCount();
    while (i > 0) {
      int j = i - 1;
      i = j;
      if (canTextInput(viewGroup.getChildAt(j)))
        return true; 
    } 
    return false;
  }
  
  private static void manageScrollIndicators(View paramView1, View paramView2, View paramView3) {
    boolean bool = false;
    if (paramView2 != null) {
      byte b;
      if (ViewCompat.canScrollVertically(paramView1, -1)) {
        b = 0;
      } else {
        b = 4;
      } 
      paramView2.setVisibility(b);
    } 
    if (paramView3 != null) {
      byte b;
      if (ViewCompat.canScrollVertically(paramView1, 1)) {
        b = bool;
      } else {
        b = 4;
      } 
      paramView3.setVisibility(b);
    } 
  }
  
  @Nullable
  private ViewGroup resolvePanel(@Nullable View paramView1, @Nullable View paramView2) {
    if (paramView1 == null) {
      paramView1 = paramView2;
      if (paramView2 instanceof ViewStub)
        paramView1 = ((ViewStub)paramView2).inflate(); 
      return (ViewGroup)paramView1;
    } 
    if (paramView2 != null) {
      ViewParent viewParent = paramView2.getParent();
      if (viewParent instanceof ViewGroup)
        ((ViewGroup)viewParent).removeView(paramView2); 
    } 
    paramView2 = paramView1;
    if (paramView1 instanceof ViewStub)
      paramView2 = ((ViewStub)paramView1).inflate(); 
    return (ViewGroup)paramView2;
  }
  
  private int selectContentView() {
    return (this.mButtonPanelSideLayout == 0) ? this.mAlertDialogLayout : ((this.mButtonPanelLayoutHint == 1) ? this.mButtonPanelSideLayout : this.mAlertDialogLayout);
  }
  
  private void setScrollIndicators(ViewGroup paramViewGroup, final View top, int paramInt1, int paramInt2) {
    final View bottom = this.mWindow.findViewById(R.id.scrollIndicatorUp);
    View view2 = this.mWindow.findViewById(R.id.scrollIndicatorDown);
    if (Build.VERSION.SDK_INT >= 23) {
      ViewCompat.setScrollIndicators(top, paramInt1, paramInt2);
      if (view1 != null)
        paramViewGroup.removeView(view1); 
      if (view2 != null)
        paramViewGroup.removeView(view2); 
      return;
    } 
    top = view1;
    if (view1 != null) {
      top = view1;
      if ((paramInt1 & 0x1) == 0) {
        paramViewGroup.removeView(view1);
        top = null;
      } 
    } 
    view1 = view2;
    if (view2 != null) {
      view1 = view2;
      if ((paramInt1 & 0x2) == 0) {
        paramViewGroup.removeView(view2);
        view1 = null;
      } 
    } 
    if (top != null || view1 != null) {
      if (this.mMessage != null) {
        this.mScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
              public void onScrollChange(NestedScrollView param1NestedScrollView, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
                AlertController.manageScrollIndicators((View)param1NestedScrollView, top, bottom);
              }
            });
        this.mScrollView.post(new Runnable() {
              public void run() {
                AlertController.manageScrollIndicators((View)AlertController.this.mScrollView, top, bottom);
              }
            });
        return;
      } 
      if (this.mListView != null) {
        this.mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
              public void onScroll(AbsListView param1AbsListView, int param1Int1, int param1Int2, int param1Int3) {
                AlertController.manageScrollIndicators((View)param1AbsListView, top, bottom);
              }
              
              public void onScrollStateChanged(AbsListView param1AbsListView, int param1Int) {}
            });
        this.mListView.post(new Runnable() {
              public void run() {
                AlertController.manageScrollIndicators((View)AlertController.this.mListView, top, bottom);
              }
            });
        return;
      } 
      if (top != null)
        paramViewGroup.removeView(top); 
      if (view1 != null) {
        paramViewGroup.removeView(view1);
        return;
      } 
    } 
  }
  
  private void setupButtons(ViewGroup paramViewGroup) {
    boolean bool = false;
    int i = 0;
    this.mButtonPositive = (Button)paramViewGroup.findViewById(16908313);
    this.mButtonPositive.setOnClickListener(this.mButtonHandler);
    if (TextUtils.isEmpty(this.mButtonPositiveText)) {
      this.mButtonPositive.setVisibility(8);
    } else {
      this.mButtonPositive.setText(this.mButtonPositiveText);
      this.mButtonPositive.setVisibility(0);
      i = false | true;
    } 
    this.mButtonNegative = (Button)paramViewGroup.findViewById(16908314);
    this.mButtonNegative.setOnClickListener(this.mButtonHandler);
    if (TextUtils.isEmpty(this.mButtonNegativeText)) {
      this.mButtonNegative.setVisibility(8);
    } else {
      this.mButtonNegative.setText(this.mButtonNegativeText);
      this.mButtonNegative.setVisibility(0);
      i |= 0x2;
    } 
    this.mButtonNeutral = (Button)paramViewGroup.findViewById(16908315);
    this.mButtonNeutral.setOnClickListener(this.mButtonHandler);
    if (TextUtils.isEmpty(this.mButtonNeutralText)) {
      this.mButtonNeutral.setVisibility(8);
    } else {
      this.mButtonNeutral.setText(this.mButtonNeutralText);
      this.mButtonNeutral.setVisibility(0);
      i |= 0x4;
    } 
    if (i != 0)
      bool = true; 
    if (!bool)
      paramViewGroup.setVisibility(8); 
  }
  
  private void setupContent(ViewGroup paramViewGroup) {
    this.mScrollView = (NestedScrollView)this.mWindow.findViewById(R.id.scrollView);
    this.mScrollView.setFocusable(false);
    this.mScrollView.setNestedScrollingEnabled(false);
    this.mMessageView = (TextView)paramViewGroup.findViewById(16908299);
    if (this.mMessageView == null)
      return; 
    if (this.mMessage != null) {
      this.mMessageView.setText(this.mMessage);
      return;
    } 
    this.mMessageView.setVisibility(8);
    this.mScrollView.removeView((View)this.mMessageView);
    if (this.mListView != null) {
      paramViewGroup = (ViewGroup)this.mScrollView.getParent();
      int i = paramViewGroup.indexOfChild((View)this.mScrollView);
      paramViewGroup.removeViewAt(i);
      paramViewGroup.addView((View)this.mListView, i, new ViewGroup.LayoutParams(-1, -1));
      return;
    } 
    paramViewGroup.setVisibility(8);
  }
  
  private void setupCustomContent(ViewGroup paramViewGroup) {
    View view;
    boolean bool = false;
    if (this.mView != null) {
      view = this.mView;
    } else if (this.mViewLayoutResId != 0) {
      view = LayoutInflater.from(this.mContext).inflate(this.mViewLayoutResId, paramViewGroup, false);
    } else {
      view = null;
    } 
    if (view != null)
      bool = true; 
    if (!bool || !canTextInput(view))
      this.mWindow.setFlags(131072, 131072); 
    if (bool) {
      FrameLayout frameLayout = (FrameLayout)this.mWindow.findViewById(R.id.custom);
      frameLayout.addView(view, new ViewGroup.LayoutParams(-1, -1));
      if (this.mViewSpacingSpecified)
        frameLayout.setPadding(this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom); 
      if (this.mListView != null)
        ((LinearLayout.LayoutParams)paramViewGroup.getLayoutParams()).weight = 0.0F; 
      return;
    } 
    paramViewGroup.setVisibility(8);
  }
  
  private void setupTitle(ViewGroup paramViewGroup) {
    boolean bool = false;
    if (this.mCustomTitleView != null) {
      ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1, -2);
      paramViewGroup.addView(this.mCustomTitleView, 0, layoutParams);
      this.mWindow.findViewById(R.id.title_template).setVisibility(8);
      return;
    } 
    this.mIconView = (ImageView)this.mWindow.findViewById(16908294);
    if (!TextUtils.isEmpty(this.mTitle))
      bool = true; 
    if (bool) {
      this.mTitleView = (TextView)this.mWindow.findViewById(R.id.alertTitle);
      this.mTitleView.setText(this.mTitle);
      if (this.mIconId != 0) {
        this.mIconView.setImageResource(this.mIconId);
        return;
      } 
      if (this.mIcon != null) {
        this.mIconView.setImageDrawable(this.mIcon);
        return;
      } 
      this.mTitleView.setPadding(this.mIconView.getPaddingLeft(), this.mIconView.getPaddingTop(), this.mIconView.getPaddingRight(), this.mIconView.getPaddingBottom());
      this.mIconView.setVisibility(8);
      return;
    } 
    this.mWindow.findViewById(R.id.title_template).setVisibility(8);
    this.mIconView.setVisibility(8);
    paramViewGroup.setVisibility(8);
  }
  
  private void setupView() {
    int i;
    byte b;
    boolean bool;
    View view2 = this.mWindow.findViewById(R.id.parentPanel);
    View view5 = view2.findViewById(R.id.topPanel);
    View view3 = view2.findViewById(R.id.contentPanel);
    View view1 = view2.findViewById(R.id.buttonPanel);
    ViewGroup viewGroup2 = (ViewGroup)view2.findViewById(R.id.customPanel);
    setupCustomContent(viewGroup2);
    View view7 = viewGroup2.findViewById(R.id.topPanel);
    View view6 = viewGroup2.findViewById(R.id.contentPanel);
    View view4 = viewGroup2.findViewById(R.id.buttonPanel);
    ViewGroup viewGroup4 = resolvePanel(view7, view5);
    ViewGroup viewGroup3 = resolvePanel(view6, view3);
    ViewGroup viewGroup1 = resolvePanel(view4, view1);
    setupContent(viewGroup3);
    setupButtons(viewGroup1);
    setupTitle(viewGroup4);
    if (viewGroup2 != null && viewGroup2.getVisibility() != 8) {
      i = 1;
    } else {
      i = 0;
    } 
    if (viewGroup4 != null && viewGroup4.getVisibility() != 8) {
      bool = true;
    } else {
      bool = false;
    } 
    if (viewGroup1 != null && viewGroup1.getVisibility() != 8) {
      b = 1;
    } else {
      b = 0;
    } 
    if (!b && viewGroup3 != null) {
      View view = viewGroup3.findViewById(R.id.textSpacerNoButtons);
      if (view != null)
        view.setVisibility(0); 
    } 
    if (bool && this.mScrollView != null)
      this.mScrollView.setClipToPadding(true); 
    if (!i) {
      NestedScrollView nestedScrollView;
      if (this.mListView != null) {
        ListView listView1 = this.mListView;
      } else {
        nestedScrollView = this.mScrollView;
      } 
      if (nestedScrollView != null) {
        if (bool) {
          i = 1;
        } else {
          i = 0;
        } 
        if (b) {
          b = 2;
        } else {
          b = 0;
        } 
        setScrollIndicators(viewGroup3, (View)nestedScrollView, i | b, 3);
      } 
    } 
    ListView listView = this.mListView;
    if (listView != null && this.mAdapter != null) {
      listView.setAdapter(this.mAdapter);
      i = this.mCheckedItem;
      if (i > -1) {
        listView.setItemChecked(i, true);
        listView.setSelection(i);
      } 
    } 
  }
  
  public Button getButton(int paramInt) {
    switch (paramInt) {
      default:
        return null;
      case -1:
        return this.mButtonPositive;
      case -2:
        return this.mButtonNegative;
      case -3:
        break;
    } 
    return this.mButtonNeutral;
  }
  
  public int getIconAttributeResId(int paramInt) {
    TypedValue typedValue = new TypedValue();
    this.mContext.getTheme().resolveAttribute(paramInt, typedValue, true);
    return typedValue.resourceId;
  }
  
  public ListView getListView() {
    return this.mListView;
  }
  
  public void installContent() {
    this.mDialog.supportRequestWindowFeature(1);
    int i = selectContentView();
    this.mDialog.setContentView(i);
    setupView();
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    return (this.mScrollView != null && this.mScrollView.executeKeyEvent(paramKeyEvent));
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    return (this.mScrollView != null && this.mScrollView.executeKeyEvent(paramKeyEvent));
  }
  
  public void setButton(int paramInt, CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener, Message paramMessage) {
    Message message = paramMessage;
    if (paramMessage == null) {
      message = paramMessage;
      if (paramOnClickListener != null)
        message = this.mHandler.obtainMessage(paramInt, paramOnClickListener); 
    } 
    switch (paramInt) {
      default:
        throw new IllegalArgumentException("Button does not exist");
      case -1:
        this.mButtonPositiveText = paramCharSequence;
        this.mButtonPositiveMessage = message;
        return;
      case -2:
        this.mButtonNegativeText = paramCharSequence;
        this.mButtonNegativeMessage = message;
        return;
      case -3:
        break;
    } 
    this.mButtonNeutralText = paramCharSequence;
    this.mButtonNeutralMessage = message;
  }
  
  public void setButtonPanelLayoutHint(int paramInt) {
    this.mButtonPanelLayoutHint = paramInt;
  }
  
  public void setCustomTitle(View paramView) {
    this.mCustomTitleView = paramView;
  }
  
  public void setIcon(int paramInt) {
    this.mIcon = null;
    this.mIconId = paramInt;
    if (this.mIconView != null) {
      if (paramInt != 0) {
        this.mIconView.setImageResource(this.mIconId);
        return;
      } 
    } else {
      return;
    } 
    this.mIconView.setVisibility(8);
  }
  
  public void setIcon(Drawable paramDrawable) {
    this.mIcon = paramDrawable;
    this.mIconId = 0;
    if (this.mIconView != null) {
      if (paramDrawable != null) {
        this.mIconView.setImageDrawable(paramDrawable);
        return;
      } 
    } else {
      return;
    } 
    this.mIconView.setVisibility(8);
  }
  
  public void setMessage(CharSequence paramCharSequence) {
    this.mMessage = paramCharSequence;
    if (this.mMessageView != null)
      this.mMessageView.setText(paramCharSequence); 
  }
  
  public void setTitle(CharSequence paramCharSequence) {
    this.mTitle = paramCharSequence;
    if (this.mTitleView != null)
      this.mTitleView.setText(paramCharSequence); 
  }
  
  public void setView(int paramInt) {
    this.mView = null;
    this.mViewLayoutResId = paramInt;
    this.mViewSpacingSpecified = false;
  }
  
  public void setView(View paramView) {
    this.mView = paramView;
    this.mViewLayoutResId = 0;
    this.mViewSpacingSpecified = false;
  }
  
  public void setView(View paramView, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    this.mView = paramView;
    this.mViewLayoutResId = 0;
    this.mViewSpacingSpecified = true;
    this.mViewSpacingLeft = paramInt1;
    this.mViewSpacingTop = paramInt2;
    this.mViewSpacingRight = paramInt3;
    this.mViewSpacingBottom = paramInt4;
  }
  
  public static class AlertParams {
    public ListAdapter mAdapter;
    
    public boolean mCancelable;
    
    public int mCheckedItem = -1;
    
    public boolean[] mCheckedItems;
    
    public final Context mContext;
    
    public Cursor mCursor;
    
    public View mCustomTitleView;
    
    public boolean mForceInverseBackground;
    
    public Drawable mIcon;
    
    public int mIconAttrId = 0;
    
    public int mIconId = 0;
    
    public final LayoutInflater mInflater;
    
    public String mIsCheckedColumn;
    
    public boolean mIsMultiChoice;
    
    public boolean mIsSingleChoice;
    
    public CharSequence[] mItems;
    
    public String mLabelColumn;
    
    public CharSequence mMessage;
    
    public DialogInterface.OnClickListener mNegativeButtonListener;
    
    public CharSequence mNegativeButtonText;
    
    public DialogInterface.OnClickListener mNeutralButtonListener;
    
    public CharSequence mNeutralButtonText;
    
    public DialogInterface.OnCancelListener mOnCancelListener;
    
    public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
    
    public DialogInterface.OnClickListener mOnClickListener;
    
    public DialogInterface.OnDismissListener mOnDismissListener;
    
    public AdapterView.OnItemSelectedListener mOnItemSelectedListener;
    
    public DialogInterface.OnKeyListener mOnKeyListener;
    
    public OnPrepareListViewListener mOnPrepareListViewListener;
    
    public DialogInterface.OnClickListener mPositiveButtonListener;
    
    public CharSequence mPositiveButtonText;
    
    public boolean mRecycleOnMeasure = true;
    
    public CharSequence mTitle;
    
    public View mView;
    
    public int mViewLayoutResId;
    
    public int mViewSpacingBottom;
    
    public int mViewSpacingLeft;
    
    public int mViewSpacingRight;
    
    public boolean mViewSpacingSpecified = false;
    
    public int mViewSpacingTop;
    
    public AlertParams(Context param1Context) {
      this.mContext = param1Context;
      this.mCancelable = true;
      this.mInflater = (LayoutInflater)param1Context.getSystemService("layout_inflater");
    }
    
    private void createListView(final AlertController dialog) {
      ArrayAdapter<CharSequence> arrayAdapter;
      final ListView listView = (ListView)this.mInflater.inflate(dialog.mListLayout, null);
      if (this.mIsMultiChoice) {
        if (this.mCursor == null) {
          arrayAdapter = new ArrayAdapter<CharSequence>(this.mContext, dialog.mMultiChoiceItemLayout, 16908308, this.mItems) {
              public View getView(int param2Int, View param2View, ViewGroup param2ViewGroup) {
                param2View = super.getView(param2Int, param2View, param2ViewGroup);
                if (AlertController.AlertParams.this.mCheckedItems != null && AlertController.AlertParams.this.mCheckedItems[param2Int])
                  listView.setItemChecked(param2Int, true); 
                return param2View;
              }
            };
        } else {
          CursorAdapter cursorAdapter = new CursorAdapter(this.mContext, this.mCursor, false) {
              private final int mIsCheckedIndex;
              
              private final int mLabelIndex;
              
              public void bindView(View param2View, Context param2Context, Cursor param2Cursor) {
                boolean bool = true;
                ((CheckedTextView)param2View.findViewById(16908308)).setText(param2Cursor.getString(this.mLabelIndex));
                ListView listView = listView;
                int i = param2Cursor.getPosition();
                if (param2Cursor.getInt(this.mIsCheckedIndex) != 1)
                  bool = false; 
                listView.setItemChecked(i, bool);
              }
              
              public View newView(Context param2Context, Cursor param2Cursor, ViewGroup param2ViewGroup) {
                return AlertController.AlertParams.this.mInflater.inflate(dialog.mMultiChoiceItemLayout, param2ViewGroup, false);
              }
            };
        } 
      } else {
        int i;
        if (this.mIsSingleChoice) {
          i = dialog.mSingleChoiceItemLayout;
        } else {
          i = dialog.mListItemLayout;
        } 
        if (this.mCursor != null) {
          SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this.mContext, i, this.mCursor, new String[] { this.mLabelColumn }, new int[] { 16908308 });
        } else if (this.mAdapter != null) {
          ListAdapter listAdapter = this.mAdapter;
        } else {
          arrayAdapter = new AlertController.CheckedItemAdapter(this.mContext, i, 16908308, this.mItems);
        } 
      } 
      if (this.mOnPrepareListViewListener != null)
        this.mOnPrepareListViewListener.onPrepareListView(listView); 
      AlertController.access$1502(dialog, (ListAdapter)arrayAdapter);
      AlertController.access$1602(dialog, this.mCheckedItem);
      if (this.mOnClickListener != null) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              public void onItemClick(AdapterView<?> param2AdapterView, View param2View, int param2Int, long param2Long) {
                AlertController.AlertParams.this.mOnClickListener.onClick((DialogInterface)dialog.mDialog, param2Int);
                if (!AlertController.AlertParams.this.mIsSingleChoice)
                  dialog.mDialog.dismiss(); 
              }
            });
      } else if (this.mOnCheckboxClickListener != null) {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              public void onItemClick(AdapterView<?> param2AdapterView, View param2View, int param2Int, long param2Long) {
                if (AlertController.AlertParams.this.mCheckedItems != null)
                  AlertController.AlertParams.this.mCheckedItems[param2Int] = listView.isItemChecked(param2Int); 
                AlertController.AlertParams.this.mOnCheckboxClickListener.onClick((DialogInterface)dialog.mDialog, param2Int, listView.isItemChecked(param2Int));
              }
            });
      } 
      if (this.mOnItemSelectedListener != null)
        listView.setOnItemSelectedListener(this.mOnItemSelectedListener); 
      if (this.mIsSingleChoice) {
        listView.setChoiceMode(1);
      } else if (this.mIsMultiChoice) {
        listView.setChoiceMode(2);
      } 
      AlertController.access$1002(dialog, listView);
    }
    
    public void apply(AlertController param1AlertController) {
      if (this.mCustomTitleView != null) {
        param1AlertController.setCustomTitle(this.mCustomTitleView);
      } else {
        if (this.mTitle != null)
          param1AlertController.setTitle(this.mTitle); 
        if (this.mIcon != null)
          param1AlertController.setIcon(this.mIcon); 
        if (this.mIconId != 0)
          param1AlertController.setIcon(this.mIconId); 
        if (this.mIconAttrId != 0)
          param1AlertController.setIcon(param1AlertController.getIconAttributeResId(this.mIconAttrId)); 
      } 
      if (this.mMessage != null)
        param1AlertController.setMessage(this.mMessage); 
      if (this.mPositiveButtonText != null)
        param1AlertController.setButton(-1, this.mPositiveButtonText, this.mPositiveButtonListener, null); 
      if (this.mNegativeButtonText != null)
        param1AlertController.setButton(-2, this.mNegativeButtonText, this.mNegativeButtonListener, null); 
      if (this.mNeutralButtonText != null)
        param1AlertController.setButton(-3, this.mNeutralButtonText, this.mNeutralButtonListener, null); 
      if (this.mItems != null || this.mCursor != null || this.mAdapter != null)
        createListView(param1AlertController); 
      if (this.mView != null) {
        if (this.mViewSpacingSpecified) {
          param1AlertController.setView(this.mView, this.mViewSpacingLeft, this.mViewSpacingTop, this.mViewSpacingRight, this.mViewSpacingBottom);
          return;
        } 
        param1AlertController.setView(this.mView);
        return;
      } 
      if (this.mViewLayoutResId != 0) {
        param1AlertController.setView(this.mViewLayoutResId);
        return;
      } 
    }
    
    public static interface OnPrepareListViewListener {
      void onPrepareListView(ListView param2ListView);
    }
  }
  
  class null extends ArrayAdapter<CharSequence> {
    null(Context param1Context, int param1Int1, int param1Int2, CharSequence[] param1ArrayOfCharSequence) {
      super(param1Context, param1Int1, param1Int2, (Object[])param1ArrayOfCharSequence);
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      param1View = super.getView(param1Int, param1View, param1ViewGroup);
      if (this.this$0.mCheckedItems != null && this.this$0.mCheckedItems[param1Int])
        listView.setItemChecked(param1Int, true); 
      return param1View;
    }
  }
  
  class null extends CursorAdapter {
    private final int mIsCheckedIndex;
    
    private final int mLabelIndex;
    
    null(Context param1Context, Cursor param1Cursor, boolean param1Boolean) {
      super(param1Context, param1Cursor, param1Boolean);
      Cursor cursor = getCursor();
      this.mLabelIndex = cursor.getColumnIndexOrThrow(this.this$0.mLabelColumn);
      this.mIsCheckedIndex = cursor.getColumnIndexOrThrow(this.this$0.mIsCheckedColumn);
    }
    
    public void bindView(View param1View, Context param1Context, Cursor param1Cursor) {
      boolean bool = true;
      ((CheckedTextView)param1View.findViewById(16908308)).setText(param1Cursor.getString(this.mLabelIndex));
      ListView listView = listView;
      int i = param1Cursor.getPosition();
      if (param1Cursor.getInt(this.mIsCheckedIndex) != 1)
        bool = false; 
      listView.setItemChecked(i, bool);
    }
    
    public View newView(Context param1Context, Cursor param1Cursor, ViewGroup param1ViewGroup) {
      return this.this$0.mInflater.inflate(dialog.mMultiChoiceItemLayout, param1ViewGroup, false);
    }
  }
  
  class null implements AdapterView.OnItemClickListener {
    public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
      this.this$0.mOnClickListener.onClick((DialogInterface)dialog.mDialog, param1Int);
      if (!this.this$0.mIsSingleChoice)
        dialog.mDialog.dismiss(); 
    }
  }
  
  class null implements AdapterView.OnItemClickListener {
    public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
      if (this.this$0.mCheckedItems != null)
        this.this$0.mCheckedItems[param1Int] = listView.isItemChecked(param1Int); 
      this.this$0.mOnCheckboxClickListener.onClick((DialogInterface)dialog.mDialog, param1Int, listView.isItemChecked(param1Int));
    }
  }
  
  public static interface OnPrepareListViewListener {
    void onPrepareListView(ListView param1ListView);
  }
  
  private static final class ButtonHandler extends Handler {
    private static final int MSG_DISMISS_DIALOG = 1;
    
    private WeakReference<DialogInterface> mDialog;
    
    public ButtonHandler(DialogInterface param1DialogInterface) {
      this.mDialog = new WeakReference<DialogInterface>(param1DialogInterface);
    }
    
    public void handleMessage(Message param1Message) {
      switch (param1Message.what) {
        default:
          return;
        case -3:
        case -2:
        case -1:
          ((DialogInterface.OnClickListener)param1Message.obj).onClick(this.mDialog.get(), param1Message.what);
          return;
        case 1:
          break;
      } 
      ((DialogInterface)param1Message.obj).dismiss();
    }
  }
  
  private static class CheckedItemAdapter extends ArrayAdapter<CharSequence> {
    public CheckedItemAdapter(Context param1Context, int param1Int1, int param1Int2, CharSequence[] param1ArrayOfCharSequence) {
      super(param1Context, param1Int1, param1Int2, (Object[])param1ArrayOfCharSequence);
    }
    
    public long getItemId(int param1Int) {
      return param1Int;
    }
    
    public boolean hasStableIds() {
      return true;
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\app\AlertController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */