package android.support.v7.view.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.appcompat.R;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class ListMenuItemView extends LinearLayout implements MenuView.ItemView {
  private static final String TAG = "ListMenuItemView";
  
  private Drawable mBackground;
  
  private CheckBox mCheckBox;
  
  private Context mContext;
  
  private boolean mForceShowIcon;
  
  private ImageView mIconView;
  
  private LayoutInflater mInflater;
  
  private MenuItemImpl mItemData;
  
  private int mMenuType;
  
  private boolean mPreserveIconSpacing;
  
  private RadioButton mRadioButton;
  
  private TextView mShortcutView;
  
  private int mTextAppearance;
  
  private Context mTextAppearanceContext;
  
  private TextView mTitleView;
  
  public ListMenuItemView(Context paramContext, AttributeSet paramAttributeSet) {
    this(paramContext, paramAttributeSet, 0);
  }
  
  public ListMenuItemView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.MenuView, paramInt, 0);
    this.mBackground = typedArray.getDrawable(R.styleable.MenuView_android_itemBackground);
    this.mTextAppearance = typedArray.getResourceId(R.styleable.MenuView_android_itemTextAppearance, -1);
    this.mPreserveIconSpacing = typedArray.getBoolean(R.styleable.MenuView_preserveIconSpacing, false);
    this.mTextAppearanceContext = paramContext;
    typedArray.recycle();
  }
  
  private LayoutInflater getInflater() {
    if (this.mInflater == null)
      this.mInflater = LayoutInflater.from(this.mContext); 
    return this.mInflater;
  }
  
  private void insertCheckBox() {
    this.mCheckBox = (CheckBox)getInflater().inflate(R.layout.abc_list_menu_item_checkbox, (ViewGroup)this, false);
    addView((View)this.mCheckBox);
  }
  
  private void insertIconView() {
    this.mIconView = (ImageView)getInflater().inflate(R.layout.abc_list_menu_item_icon, (ViewGroup)this, false);
    addView((View)this.mIconView, 0);
  }
  
  private void insertRadioButton() {
    this.mRadioButton = (RadioButton)getInflater().inflate(R.layout.abc_list_menu_item_radio, (ViewGroup)this, false);
    addView((View)this.mRadioButton);
  }
  
  public MenuItemImpl getItemData() {
    return this.mItemData;
  }
  
  public void initialize(MenuItemImpl paramMenuItemImpl, int paramInt) {
    this.mItemData = paramMenuItemImpl;
    this.mMenuType = paramInt;
    if (paramMenuItemImpl.isVisible()) {
      paramInt = 0;
    } else {
      paramInt = 8;
    } 
    setVisibility(paramInt);
    setTitle(paramMenuItemImpl.getTitleForItemView(this));
    setCheckable(paramMenuItemImpl.isCheckable());
    setShortcut(paramMenuItemImpl.shouldShowShortcut(), paramMenuItemImpl.getShortcut());
    setIcon(paramMenuItemImpl.getIcon());
    setEnabled(paramMenuItemImpl.isEnabled());
  }
  
  protected void onFinishInflate() {
    super.onFinishInflate();
    setBackgroundDrawable(this.mBackground);
    this.mTitleView = (TextView)findViewById(R.id.title);
    if (this.mTextAppearance != -1)
      this.mTitleView.setTextAppearance(this.mTextAppearanceContext, this.mTextAppearance); 
    this.mShortcutView = (TextView)findViewById(R.id.shortcut);
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    if (this.mIconView != null && this.mPreserveIconSpacing) {
      ViewGroup.LayoutParams layoutParams = getLayoutParams();
      LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams)this.mIconView.getLayoutParams();
      if (layoutParams.height > 0 && layoutParams1.width <= 0)
        layoutParams1.width = layoutParams.height; 
    } 
    super.onMeasure(paramInt1, paramInt2);
  }
  
  public boolean prefersCondensedTitle() {
    return false;
  }
  
  public void setCheckable(boolean paramBoolean) {
    if (paramBoolean || this.mRadioButton != null || this.mCheckBox != null) {
      CheckBox checkBox;
      RadioButton radioButton;
      if (this.mItemData.isExclusiveCheckable()) {
        if (this.mRadioButton == null)
          insertRadioButton(); 
        RadioButton radioButton1 = this.mRadioButton;
        CheckBox checkBox1 = this.mCheckBox;
      } else {
        if (this.mCheckBox == null)
          insertCheckBox(); 
        checkBox = this.mCheckBox;
        radioButton = this.mRadioButton;
      } 
      if (paramBoolean) {
        byte b;
        checkBox.setChecked(this.mItemData.isChecked());
        if (paramBoolean) {
          b = 0;
        } else {
          b = 8;
        } 
        if (checkBox.getVisibility() != b)
          checkBox.setVisibility(b); 
        if (radioButton != null && radioButton.getVisibility() != 8) {
          radioButton.setVisibility(8);
          return;
        } 
        return;
      } 
      if (this.mCheckBox != null)
        this.mCheckBox.setVisibility(8); 
      if (this.mRadioButton != null) {
        this.mRadioButton.setVisibility(8);
        return;
      } 
    } 
  }
  
  public void setChecked(boolean paramBoolean) {
    CheckBox checkBox;
    if (this.mItemData.isExclusiveCheckable()) {
      if (this.mRadioButton == null)
        insertRadioButton(); 
      RadioButton radioButton = this.mRadioButton;
    } else {
      if (this.mCheckBox == null)
        insertCheckBox(); 
      checkBox = this.mCheckBox;
    } 
    checkBox.setChecked(paramBoolean);
  }
  
  public void setForceShowIcon(boolean paramBoolean) {
    this.mForceShowIcon = paramBoolean;
    this.mPreserveIconSpacing = paramBoolean;
  }
  
  public void setIcon(Drawable paramDrawable) {
    boolean bool;
    if (this.mItemData.shouldShowIcon() || this.mForceShowIcon) {
      bool = true;
    } else {
      bool = false;
    } 
    if ((bool || this.mPreserveIconSpacing) && (this.mIconView != null || paramDrawable != null || this.mPreserveIconSpacing)) {
      if (this.mIconView == null)
        insertIconView(); 
      if (paramDrawable != null || this.mPreserveIconSpacing) {
        ImageView imageView = this.mIconView;
        if (!bool)
          paramDrawable = null; 
        imageView.setImageDrawable(paramDrawable);
        if (this.mIconView.getVisibility() != 0) {
          this.mIconView.setVisibility(0);
          return;
        } 
        return;
      } 
      this.mIconView.setVisibility(8);
      return;
    } 
  }
  
  public void setShortcut(boolean paramBoolean, char paramChar) {
    if (paramBoolean && this.mItemData.shouldShowShortcut()) {
      paramChar = Character.MIN_VALUE;
    } else {
      paramChar = '\b';
    } 
    if (paramChar == '\000')
      this.mShortcutView.setText(this.mItemData.getShortcutLabel()); 
    if (this.mShortcutView.getVisibility() != paramChar)
      this.mShortcutView.setVisibility(paramChar); 
  }
  
  public void setTitle(CharSequence paramCharSequence) {
    if (paramCharSequence != null) {
      this.mTitleView.setText(paramCharSequence);
      if (this.mTitleView.getVisibility() != 0)
        this.mTitleView.setVisibility(0); 
      return;
    } 
    if (this.mTitleView.getVisibility() != 8) {
      this.mTitleView.setVisibility(8);
      return;
    } 
  }
  
  public boolean showsIcon() {
    return this.mForceShowIcon;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\view\menu\ListMenuItemView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */