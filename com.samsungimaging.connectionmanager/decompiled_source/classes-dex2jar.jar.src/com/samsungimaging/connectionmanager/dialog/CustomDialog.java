package com.samsungimaging.connectionmanager.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class CustomDialog extends AlertDialog implements DialogInterface.OnDismissListener {
  private ListAdapter mAdapter = null;
  
  private int mCheckedItem = 0;
  
  private boolean[] mCheckedItems = null;
  
  private ChoiceMode mChoiceMode = ChoiceMode.NONE;
  
  private Cursor mCursor = null;
  
  private View mCustomTitleView = null;
  
  private Drawable mIcon = null;
  
  private View.OnClickListener mInternalButtonClickListener = new View.OnClickListener() {
      public void onClick(View param1View) {
        int i = ((Integer)param1View.getTag()).intValue();
        switch (i) {
          default:
            throw new IllegalArgumentException("Unknown which " + i);
          case -1:
            if (CustomDialog.this.mPositiveButtonClickListener != null) {
              CustomDialog.this.mPositiveButtonClickListener.onClick((DialogInterface)CustomDialog.this, i);
              return;
            } 
            CustomDialog.this.dismiss();
            return;
          case -2:
            if (CustomDialog.this.mNegativeButtonClickListener != null) {
              CustomDialog.this.mNegativeButtonClickListener.onClick((DialogInterface)CustomDialog.this, i);
              return;
            } 
            CustomDialog.this.dismiss();
            return;
          case -3:
            break;
        } 
        if (CustomDialog.this.mNeutralButtonClickListener != null) {
          CustomDialog.this.mNeutralButtonClickListener.onClick((DialogInterface)CustomDialog.this, i);
          return;
        } 
        CustomDialog.this.dismiss();
      }
    };
  
  private AdapterView.OnItemClickListener mInternalItemClickListener = new AdapterView.OnItemClickListener() {
      public void onItemClick(AdapterView<?> param1AdapterView, View param1View, int param1Int, long param1Long) {
        switch (CustomDialog.this.mChoiceMode) {
          default:
            return;
          case NONE:
          case SINGLE:
            if (CustomDialog.this.mListClickListener != null) {
              CustomDialog.this.mListClickListener.onClick((DialogInterface)CustomDialog.this, param1Int);
              return;
            } 
          case MULTI:
            break;
        } 
        if (CustomDialog.this.mListMultiChoiceClickListener != null) {
          CustomDialog.this.mListMultiChoiceClickListener.onClick((DialogInterface)CustomDialog.this, param1Int, CustomDialog.this.mListView.isItemChecked(param1Int));
          return;
        } 
      }
    };
  
  private String mIsCheckedColumn = null;
  
  private CharSequence[] mItems = null;
  
  private String mLabelColumn = null;
  
  private DialogInterface.OnClickListener mListClickListener = null;
  
  private View mListEmptyView = null;
  
  private DialogInterface.OnMultiChoiceClickListener mListMultiChoiceClickListener = null;
  
  private ListView mListView = null;
  
  protected CharSequence mMessage = null;
  
  private TextView mMessageView = null;
  
  private DialogInterface.OnClickListener mNegativeButtonClickListener = null;
  
  private CharSequence mNegativeButtonText = null;
  
  private DialogInterface.OnClickListener mNeutralButtonClickListener = null;
  
  private CharSequence mNeutralButtonText = null;
  
  private DialogInterface.OnClickListener mPositiveButtonClickListener = null;
  
  private CharSequence mPositiveButtonText = null;
  
  private Object mTag = null;
  
  private CharSequence mTitle = null;
  
  private ImageView mTitleImageView = null;
  
  private LinearLayout mTitleLayout = null;
  
  private TextView mTitleTextView = null;
  
  protected View mView = null;
  
  public CustomDialog(Context paramContext) {
    super(paramContext);
  }
  
  public CustomDialog(Context paramContext, int paramInt) {
    super(paramContext, paramInt);
  }
  
  private boolean initButtonLayout() {
    boolean bool = false;
    LinearLayout linearLayout = (LinearLayout)findViewById(2131558538);
    int i = getContext().getResources().getDimensionPixelSize(2131230722);
    if (this.mPositiveButtonText != null) {
      Button button = (Button)findViewById(2131558539);
      button.setTag(Integer.valueOf(-1));
      button.setText(this.mPositiveButtonText);
      button.setOnClickListener(this.mInternalButtonClickListener);
      LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)button.getLayoutParams();
      layoutParams.setMargins(i, i, i, i);
      button.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      button.setVisibility(0);
      bool = true;
    } 
    if (this.mNeutralButtonText != null) {
      Button button = (Button)findViewById(2131558540);
      button.setTag(Integer.valueOf(-3));
      button.setText(this.mNeutralButtonText);
      button.setOnClickListener(this.mInternalButtonClickListener);
      LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)button.getLayoutParams();
      if (this.mPositiveButtonText == null) {
        layoutParams.setMargins(i, i, i, i);
      } else {
        layoutParams.setMargins(0, i, i, i);
      } 
      button.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      button.setVisibility(0);
      bool = true;
    } 
    if (this.mNegativeButtonText != null) {
      Button button = (Button)findViewById(2131558541);
      button.setTag(Integer.valueOf(-2));
      button.setText(this.mNegativeButtonText);
      button.setOnClickListener(this.mInternalButtonClickListener);
      LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)button.getLayoutParams();
      if (this.mPositiveButtonText == null && this.mNeutralButtonText == null) {
        layoutParams.setMargins(i, i, i, i);
      } else {
        layoutParams.setMargins(0, i, i, i);
      } 
      button.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
      button.setVisibility(0);
      bool = true;
    } 
    if (bool)
      linearLayout.setVisibility(0); 
    return bool;
  }
  
  private boolean initMessageLayout() {
    boolean bool = false;
    ViewGroup viewGroup = (ViewGroup)findViewById(2131558533);
    LinearLayout linearLayout = (LinearLayout)findViewById(2131558535);
    if (this.mChoiceMode == ChoiceMode.MULTI) {
      if (this.mItems != null) {
        this.mAdapter = (ListAdapter)new InternalAdapter(getContext());
      } else if (this.mCursor != null) {
        this.mAdapter = (ListAdapter)new InternalCursorAdapter(getContext());
      } 
    } else {
      int i;
      if (this.mChoiceMode == ChoiceMode.SINGLE) {
        i = 2130903109;
      } else {
        i = 2130903106;
      } 
      if (this.mItems != null) {
        this.mAdapter = (ListAdapter)new ArrayAdapter(getContext(), i, (Object[])this.mItems);
      } else if (this.mCursor != null) {
        this.mAdapter = (ListAdapter)new SimpleCursorAdapter(getContext(), i, this.mCursor, new String[] { this.mLabelColumn }, new int[] { 16908308 });
      } 
    } 
    this.mListView = (ListView)findViewById(2131558534);
    if (this.mAdapter == null) {
      this.mListView.setVisibility(8);
    } else {
      if (!getContext().getSharedPreferences("configuration", 0).getBoolean("firstTimeAccount", true)) {
        this.mListView = (ListView)findViewById(2131558537);
        this.mListView.setAdapter(this.mAdapter);
      } else {
        this.mListView.setAdapter(this.mAdapter);
      } 
      this.mListView.setOnItemClickListener(this.mInternalItemClickListener);
      if (this.mChoiceMode == ChoiceMode.SINGLE) {
        this.mListView.setChoiceMode(1);
        this.mListView.setItemChecked(this.mCheckedItem, true);
        this.mListView.setSelection(this.mCheckedItem);
      } else if (this.mChoiceMode == ChoiceMode.MULTI) {
        this.mListView.setChoiceMode(2);
      } 
      if (this.mListEmptyView != null) {
        this.mListView.setEmptyView(this.mListEmptyView);
        viewGroup.addView(this.mListEmptyView);
      } 
      bool = true;
    } 
    if (this.mMessage != null) {
      this.mMessageView = (TextView)findViewById(2131558536);
      this.mMessageView.setText(this.mMessage);
      this.mMessageView.setVisibility(0);
      bool = true;
    } 
    if (this.mView != null) {
      linearLayout.addView(this.mView);
      bool = true;
    } 
    if (bool)
      viewGroup.setVisibility(0); 
    return bool;
  }
  
  private boolean initTitleLayout() {
    boolean bool = false;
    this.mTitleLayout = (LinearLayout)findViewById(2131558531);
    this.mTitleImageView = (ImageView)findViewById(2131558532);
    this.mTitleTextView = (TextView)findViewById(2131558500);
    if (this.mCustomTitleView != null) {
      this.mTitleLayout.addView(this.mCustomTitleView);
      bool = true;
    } else {
      if (this.mIcon != null) {
        this.mTitleImageView.setImageDrawable(this.mIcon);
        this.mTitleImageView.setVisibility(0);
        bool = true;
      } 
      if (this.mTitle != null) {
        this.mTitleTextView.setText(this.mTitle);
        this.mTitleTextView.setVisibility(0);
        bool = true;
      } 
    } 
    if (bool)
      this.mTitleLayout.setVisibility(0); 
    return bool;
  }
  
  private void refreshTitleLayout() {
    if (this.mCustomTitleView == null && this.mTitleLayout != null) {
      boolean bool2 = false;
      boolean bool1 = bool2;
      if (this.mIcon != null) {
        bool1 = bool2;
        if (this.mTitleImageView != null) {
          this.mTitleImageView.setImageDrawable(this.mIcon);
          this.mTitleImageView.setVisibility(0);
          bool1 = true;
        } 
      } 
      bool2 = bool1;
      if (this.mTitle != null) {
        bool2 = bool1;
        if (this.mTitleTextView != null) {
          this.mTitleTextView.setText(this.mTitle);
          this.mTitleTextView.setVisibility(0);
          bool2 = true;
        } 
      } 
      if (bool2)
        this.mTitleLayout.setVisibility(0); 
    } 
  }
  
  public ListView getListView() {
    return this.mListView;
  }
  
  public Object getTag() {
    return this.mTag;
  }
  
  protected final void initialize() {
    ViewGroup viewGroup;
    setContentView(2130903052);
    boolean bool1 = initTitleLayout();
    boolean bool2 = initMessageLayout();
    boolean bool3 = initButtonLayout();
    if (bool2) {
      viewGroup = (ViewGroup)findViewById(2131558533);
      if (bool1 && bool3) {
        viewGroup.setBackgroundResource(2130838292);
        return;
      } 
    } else {
      return;
    } 
    if (!bool1 && !bool3) {
      viewGroup.setBackgroundResource(2130838291);
      return;
    } 
    if (!bool1) {
      viewGroup.setBackgroundResource(2130838293);
      return;
    } 
    viewGroup.setBackgroundResource(2130838289);
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    setCanceledOnTouchOutside(false);
    setOnDismissListener(this);
    initialize();
  }
  
  public void onDismiss(DialogInterface paramDialogInterface) {}
  
  public void onOrientationChanged() {}
  
  protected void onStart() {
    if (this.mAdapter != null && this.mAdapter.getCount() > 0) {
      if (this.mListEmptyView != null && this.mListEmptyView.getVisibility() == 0) {
        this.mListView.setVisibility(0);
        this.mListEmptyView.setVisibility(8);
      } 
    } else if (this.mListEmptyView != null && this.mListEmptyView.getVisibility() == 8) {
      this.mListView.setVisibility(8);
      this.mListEmptyView.setVisibility(0);
    } 
    super.onStart();
  }
  
  public void setAdapter(ListAdapter paramListAdapter) {
    this.mAdapter = paramListAdapter;
  }
  
  public void setAdapter(ListAdapter paramListAdapter, DialogInterface.OnClickListener paramOnClickListener) {
    this.mAdapter = paramListAdapter;
    this.mListClickListener = paramOnClickListener;
  }
  
  public void setCursor(Cursor paramCursor, String paramString, DialogInterface.OnClickListener paramOnClickListener) {
    this.mCursor = paramCursor;
    this.mLabelColumn = paramString;
    this.mListClickListener = paramOnClickListener;
  }
  
  public void setCustomTitle(int paramInt) {
    this.mCustomTitleView = getLayoutInflater().inflate(paramInt, null);
  }
  
  public void setCustomTitle(View paramView) {
    this.mCustomTitleView = paramView;
  }
  
  public void setIcon(int paramInt) {
    this.mIcon = getContext().getResources().getDrawable(paramInt);
  }
  
  public void setIcon(Drawable paramDrawable) {
    this.mIcon = paramDrawable;
  }
  
  public void setItems(int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
    this.mItems = getContext().getResources().getTextArray(paramInt);
    this.mListClickListener = paramOnClickListener;
    this.mChoiceMode = ChoiceMode.NONE;
  }
  
  public void setItems(CharSequence[] paramArrayOfCharSequence, DialogInterface.OnClickListener paramOnClickListener) {
    this.mItems = paramArrayOfCharSequence;
    this.mListClickListener = paramOnClickListener;
    this.mChoiceMode = ChoiceMode.NONE;
  }
  
  public void setListEmptyView(int paramInt) {
    this.mListEmptyView = getLayoutInflater().inflate(paramInt, null);
  }
  
  public void setListEmptyView(View paramView) {
    this.mListEmptyView = paramView;
  }
  
  public void setMessage(int paramInt) {
    this.mMessage = getContext().getText(paramInt);
  }
  
  public void setMessage(CharSequence paramCharSequence) {
    this.mMessage = paramCharSequence;
    if (this.mMessageView != null)
      this.mMessageView.setText(paramCharSequence); 
  }
  
  public void setMultiChoiceItems(int paramInt, boolean[] paramArrayOfboolean, DialogInterface.OnMultiChoiceClickListener paramOnMultiChoiceClickListener) {
    this.mItems = getContext().getResources().getTextArray(paramInt);
    this.mCheckedItems = paramArrayOfboolean;
    this.mListMultiChoiceClickListener = paramOnMultiChoiceClickListener;
    this.mChoiceMode = ChoiceMode.MULTI;
  }
  
  public void setMultiChoiceItems(Cursor paramCursor, String paramString1, String paramString2, DialogInterface.OnMultiChoiceClickListener paramOnMultiChoiceClickListener) {
    this.mCursor = paramCursor;
    this.mIsCheckedColumn = paramString1;
    this.mLabelColumn = paramString2;
    this.mListMultiChoiceClickListener = paramOnMultiChoiceClickListener;
    this.mChoiceMode = ChoiceMode.MULTI;
  }
  
  public void setMultiChoiceItems(CharSequence[] paramArrayOfCharSequence, boolean[] paramArrayOfboolean, DialogInterface.OnMultiChoiceClickListener paramOnMultiChoiceClickListener) {
    this.mItems = paramArrayOfCharSequence;
    this.mCheckedItems = paramArrayOfboolean;
    this.mListMultiChoiceClickListener = paramOnMultiChoiceClickListener;
    this.mChoiceMode = ChoiceMode.MULTI;
  }
  
  public void setNegativeButton(int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
    this.mNegativeButtonText = getContext().getText(paramInt);
    this.mNegativeButtonClickListener = paramOnClickListener;
  }
  
  public void setNegativeButton(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener) {
    this.mNegativeButtonText = paramCharSequence;
    this.mNegativeButtonClickListener = paramOnClickListener;
  }
  
  public void setNeutralButton(int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
    this.mNeutralButtonText = getContext().getText(paramInt);
    this.mNeutralButtonClickListener = paramOnClickListener;
  }
  
  public void setNeutralButton(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener) {
    this.mNeutralButtonText = paramCharSequence;
    this.mNeutralButtonClickListener = paramOnClickListener;
  }
  
  public void setPositiveButton(int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
    this.mPositiveButtonText = getContext().getText(paramInt);
    this.mPositiveButtonClickListener = paramOnClickListener;
  }
  
  public void setPositiveButton(CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener) {
    this.mPositiveButtonText = paramCharSequence;
    this.mPositiveButtonClickListener = paramOnClickListener;
  }
  
  public void setSingleChoiceItems(int paramInt1, int paramInt2, DialogInterface.OnClickListener paramOnClickListener) {
    this.mItems = getContext().getResources().getTextArray(paramInt1);
    this.mListClickListener = paramOnClickListener;
    this.mCheckedItem = paramInt2;
    this.mChoiceMode = ChoiceMode.SINGLE;
  }
  
  public void setSingleChoiceItems(Cursor paramCursor, int paramInt, String paramString, DialogInterface.OnClickListener paramOnClickListener) {
    this.mCursor = paramCursor;
    this.mCheckedItem = paramInt;
    this.mLabelColumn = paramString;
    this.mListClickListener = paramOnClickListener;
    this.mChoiceMode = ChoiceMode.SINGLE;
  }
  
  public void setSingleChoiceItems(ListAdapter paramListAdapter, int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
    this.mAdapter = paramListAdapter;
    this.mCheckedItem = paramInt;
    this.mListClickListener = paramOnClickListener;
    this.mChoiceMode = ChoiceMode.SINGLE;
  }
  
  public void setSingleChoiceItems(CharSequence[] paramArrayOfCharSequence, int paramInt, DialogInterface.OnClickListener paramOnClickListener) {
    this.mItems = paramArrayOfCharSequence;
    this.mCheckedItem = paramInt;
    this.mListClickListener = paramOnClickListener;
    this.mChoiceMode = ChoiceMode.SINGLE;
  }
  
  public void setTag(Object paramObject) {
    this.mTag = paramObject;
  }
  
  public void setTitle(int paramInt) {
    this.mTitle = getContext().getText(paramInt);
    refreshTitleLayout();
  }
  
  public void setTitle(CharSequence paramCharSequence) {
    this.mTitle = paramCharSequence;
    refreshTitleLayout();
  }
  
  public void setView(int paramInt) {
    this.mView = getLayoutInflater().inflate(paramInt, null);
  }
  
  public void setView(View paramView) {
    this.mView = paramView;
  }
  
  private enum ChoiceMode {
    MULTI, NONE, SINGLE;
    
    static {
      ENUM$VALUES = new ChoiceMode[] { NONE, SINGLE, MULTI };
    }
  }
  
  private class InternalAdapter extends ArrayAdapter<CharSequence> {
    public InternalAdapter(Context param1Context) {
      super(param1Context, 2130903107, (Object[])CustomDialog.this.mItems);
    }
    
    public View getView(int param1Int, View param1View, ViewGroup param1ViewGroup) {
      param1View = super.getView(param1Int, param1View, param1ViewGroup);
      if (CustomDialog.this.mCheckedItems[param1Int])
        CustomDialog.this.mListView.setItemChecked(param1Int, true); 
      return param1View;
    }
  }
  
  private class InternalCursorAdapter extends CursorAdapter {
    public InternalCursorAdapter(Context param1Context) {
      super(param1Context, CustomDialog.this.mCursor);
    }
    
    public void bindView(View param1View, Context param1Context, Cursor param1Cursor) {
      boolean bool = true;
      ((CheckedTextView)param1View).setText(param1Cursor.getString(param1Cursor.getColumnIndex(CustomDialog.this.mLabelColumn)));
      if (param1Cursor.getInt(param1Cursor.getColumnIndex(CustomDialog.this.mIsCheckedColumn)) != 1)
        bool = false; 
      CustomDialog.this.mListView.setItemChecked(param1Cursor.getPosition(), bool);
    }
    
    public View newView(Context param1Context, Cursor param1Cursor, ViewGroup param1ViewGroup) {
      return CustomDialog.this.getLayoutInflater().inflate(2130903107, param1ViewGroup, false);
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\dialog\CustomDialog.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */