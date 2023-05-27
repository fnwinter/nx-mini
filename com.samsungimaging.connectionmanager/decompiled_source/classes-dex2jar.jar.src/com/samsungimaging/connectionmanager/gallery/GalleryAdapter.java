package com.samsungimaging.connectionmanager.gallery;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorTreeAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.provider.DatabaseMedia;
import com.samsungimaging.connectionmanager.util.ImageLoader;
import com.samsungimaging.connectionmanager.util.Trace;
import com.samsungimaging.connectionmanager.util.Utils;
import com.samsungimaging.connectionmanager.view.RecycleImageView;

public class GalleryAdapter extends CursorTreeAdapter implements View.OnClickListener, View.OnLongClickListener {
  private static final Trace.Tag TAG = Trace.Tag.COMMON;
  
  private int mCheckedCount = 0;
  
  private SparseArray<CheckedInfo> mCheckedList = new SparseArray();
  
  private int mChildTotalCount = 0;
  
  private int mColumnHeight = 0;
  
  private int mColumnNum = 0;
  
  private int mColumnWidth = 0;
  
  private GalleryFragment mGalleryFragment = null;
  
  private ImageLoader mImageLoader = null;
  
  private boolean mIsCheckAll = false;
  
  private boolean mIsCheckable = false;
  
  public GalleryAdapter(GalleryFragment paramGalleryFragment, Cursor paramCursor, ImageLoader paramImageLoader) {
    super(paramCursor, (Context)paramGalleryFragment.getActivity(), false);
    this.mGalleryFragment = paramGalleryFragment;
    this.mImageLoader = paramImageLoader;
    this.mChildTotalCount = getChildrenCountFromCursor();
    initCoordinate();
  }
  
  private void changeCheckedForGroup(int paramInt) {
    boolean bool;
    CheckedInfo checkedInfo = (CheckedInfo)this.mCheckedList.get(paramInt);
    if (checkedInfo == null) {
      bool = false;
    } else {
      bool = checkedInfo.mIsChecked;
    } 
    if (this.mIsCheckAll) {
      this.mIsCheckAll = false;
      int i = 0;
      while (true) {
        if (i >= getGroupCount()) {
          setCheckedCount(this.mCheckedCount - getChildrenCountFromCursor(paramInt));
        } else {
          if (i != paramInt) {
            CheckedInfo checkedInfo1 = new CheckedInfo();
            checkedInfo1.mIsChecked = true;
            this.mCheckedList.put(i, checkedInfo1);
          } 
          i++;
          continue;
        } 
        notifyDataSetChanged(false);
        return;
      } 
    } 
    if (bool) {
      if (checkedInfo.mChildCheckedArray.size() == 0) {
        setCheckedCount(this.mCheckedCount - getChildrenCountFromCursor(paramInt));
      } else {
        setCheckedCount(this.mCheckedCount - checkedInfo.mChildCheckedArray.size());
      } 
      this.mCheckedList.remove(paramInt);
    } else {
      if (checkedInfo != null && checkedInfo.mChildCheckedArray.size() > 0)
        this.mCheckedCount -= checkedInfo.mChildCheckedArray.size(); 
      this.mCheckedCount += getChildrenCountFromCursor(paramInt);
      setCheckedCount(this.mCheckedCount);
      if (this.mCheckedCount == this.mChildTotalCount) {
        this.mIsCheckAll = true;
        this.mCheckedList.clear();
      } else {
        CheckedInfo checkedInfo1 = checkedInfo;
        if (checkedInfo == null)
          checkedInfo1 = new CheckedInfo(); 
        checkedInfo1.mIsChecked = true;
        checkedInfo1.mChildCheckedArray.clear();
        this.mCheckedList.put(paramInt, checkedInfo1);
      } 
    } 
    notifyDataSetChanged(false);
  }
  
  private int getChildrenCountFromCursor() {
    int j = 0;
    for (int i = 0;; i++) {
      if (i >= getGroupCount())
        return j; 
      j += getChildrenCountFromCursor(i);
    } 
  }
  
  private void initCoordinate() {
    int i = this.mGalleryFragment.getActivity().getWindowManager().getDefaultDisplay().getWidth();
    this.mColumnNum = this.mGalleryFragment.getActivity().getResources().getInteger(2131296256);
    this.mColumnWidth = i / this.mColumnNum;
    this.mColumnHeight = (int)(this.mColumnWidth * 0.75F);
  }
  
  private boolean isChildChecked(int paramInt1, int paramInt2) {
    CheckedInfo checkedInfo = (CheckedInfo)this.mCheckedList.get(paramInt1);
    if (checkedInfo != null) {
      Boolean bool = Boolean.valueOf(checkedInfo.mChildCheckedArray.get(paramInt2));
      if (bool != null)
        return bool.booleanValue(); 
    } 
    return false;
  }
  
  private boolean isGroupChecked(int paramInt) {
    CheckedInfo checkedInfo = (CheckedInfo)this.mCheckedList.get(paramInt);
    return (checkedInfo == null) ? false : checkedInfo.mIsChecked;
  }
  
  private void setCheckedCount(int paramInt) {
    this.mCheckedCount = paramInt;
    this.mGalleryFragment.getActivity().invalidateOptionsMenu();
  }
  
  protected void bindChildView(View paramView, Context paramContext, Cursor paramCursor, boolean paramBoolean) {
    int j = ((Integer)paramView.getTag()).intValue();
    int k = paramCursor.getPosition();
    int m = this.mColumnNum;
    LinearLayout linearLayout = (LinearLayout)paramView;
    paramBoolean = isGroupChecked(j);
    for (int i = 0;; i++) {
      if (i >= this.mColumnNum)
        return; 
      int n = k * m + i;
      View view = linearLayout.getChildAt(i);
      RecycleImageView recycleImageView = (RecycleImageView)view.findViewById(2131558567);
      CheckBox checkBox = (CheckBox)view.findViewById(2131558548);
      ImageView imageView = (ImageView)view.findViewById(2131558568);
      if (n < paramCursor.getCount()) {
        paramCursor.moveToPosition(n);
        DatabaseMedia databaseMedia = DatabaseMedia.builder(paramCursor);
        if (this.mImageLoader != null)
          this.mImageLoader.loadImage(databaseMedia, recycleImageView); 
        if (this.mIsCheckable) {
          if (this.mIsCheckAll || paramBoolean) {
            checkBox.setChecked(true);
          } else {
            checkBox.setChecked(isChildChecked(j, n));
          } 
          checkBox.setVisibility(0);
        } else {
          checkBox.setVisibility(4);
        } 
        if (databaseMedia.getMediaType() == 1) {
          imageView.setVisibility(4);
        } else {
          imageView.setVisibility(0);
        } 
        view.setTag(new int[] { j, n });
        view.setVisibility(0);
      } else {
        view.setVisibility(4);
      } 
    } 
  }
  
  protected void bindGroupView(View paramView, Context paramContext, Cursor paramCursor, boolean paramBoolean) {
    int i = paramCursor.getPosition();
    String str1 = paramCursor.getString(paramCursor.getColumnIndex("datetaken_string"));
    String str2 = paramCursor.getString(paramCursor.getColumnIndex("_count"));
    CheckBox checkBox = (CheckBox)paramView.findViewById(2131558572);
    TextView textView = (TextView)paramView.findViewById(2131558573);
    RecycleImageView recycleImageView = (RecycleImageView)paramView.findViewById(2131558574);
    if (this.mIsCheckable) {
      checkBox.setTag(Integer.valueOf(i));
      if (this.mIsCheckAll) {
        checkBox.setChecked(true);
      } else {
        checkBox.setChecked(isGroupChecked(i));
      } 
      checkBox.setVisibility(0);
    } else {
      checkBox.setVisibility(8);
    } 
    textView.setText(String.valueOf(str1) + " (" + str2 + ")");
    recycleImageView.setTag(Integer.valueOf(i));
    recycleImageView.setSelected(paramBoolean);
  }
  
  protected void changeCheckedForChild(int paramInt1, int paramInt2) {
    boolean bool;
    CheckedInfo checkedInfo = (CheckedInfo)this.mCheckedList.get(paramInt1);
    if (checkedInfo == null) {
      bool = false;
    } else {
      bool = checkedInfo.mIsChecked;
    } 
    if (this.mIsCheckAll) {
      this.mIsCheckAll = false;
      int i = 0;
      label63: while (true) {
        if (i >= getGroupCount()) {
          paramInt1 = this.mCheckedCount - 1;
          this.mCheckedCount = paramInt1;
          setCheckedCount(paramInt1);
          return;
        } 
        CheckedInfo checkedInfo1 = new CheckedInfo();
        if (i == paramInt1) {
          for (int j = 0;; j++) {
            if (j >= getChildrenCountFromCursor(paramInt1)) {
              this.mCheckedList.put(i, checkedInfo1);
              i++;
              continue label63;
            } 
            if (j != paramInt2)
              checkedInfo1.mChildCheckedArray.put(j, true); 
          } 
          break;
        } 
        checkedInfo1.mIsChecked = true;
        continue;
      } 
    } 
    if (bool) {
      checkedInfo.mIsChecked = false;
      if (checkedInfo.mChildCheckedArray.size() == 0) {
        int i = 0;
        while (true) {
          if (i < getChildrenCountFromCursor(paramInt1)) {
            if (i != paramInt2)
              checkedInfo.mChildCheckedArray.put(i, true); 
            i++;
            continue;
          } 
          this.mCheckedList.put(paramInt1, checkedInfo);
          paramInt1 = this.mCheckedCount - 1;
          this.mCheckedCount = paramInt1;
          setCheckedCount(paramInt1);
          return;
        } 
      } 
      checkedInfo.mChildCheckedArray.delete(paramInt2);
    } else {
      CheckedInfo checkedInfo1;
      bool = isChildChecked(paramInt1, paramInt2);
      if (bool) {
        checkedInfo1 = checkedInfo;
        if (checkedInfo.mChildCheckedArray.size() == 1) {
          this.mCheckedList.remove(paramInt1);
          paramInt1 = this.mCheckedCount - 1;
          this.mCheckedCount = paramInt1;
          setCheckedCount(paramInt1);
          return;
        } 
      } else {
        if (this.mCheckedCount + 1 == this.mChildTotalCount) {
          this.mIsCheckAll = true;
          this.mCheckedList.clear();
          paramInt1 = this.mCheckedCount + 1;
          this.mCheckedCount = paramInt1;
          setCheckedCount(paramInt1);
          return;
        } 
        CheckedInfo checkedInfo2 = checkedInfo;
        if (checkedInfo == null)
          checkedInfo2 = new CheckedInfo(); 
        checkedInfo1 = checkedInfo2;
        if (checkedInfo2.mChildCheckedArray.size() + 1 == getChildrenCountFromCursor(paramInt1)) {
          checkedInfo2.mIsChecked = true;
          checkedInfo2.mChildCheckedArray.clear();
          this.mCheckedList.put(paramInt1, checkedInfo2);
          paramInt1 = this.mCheckedCount + 1;
          this.mCheckedCount = paramInt1;
          setCheckedCount(paramInt1);
          return;
        } 
      } 
      if (bool) {
        checkedInfo1.mChildCheckedArray.delete(paramInt2);
        paramInt2 = this.mCheckedCount - 1;
        this.mCheckedCount = paramInt2;
        setCheckedCount(paramInt2);
      } else {
        checkedInfo1.mChildCheckedArray.put(paramInt2, true);
        paramInt2 = this.mCheckedCount + 1;
        this.mCheckedCount = paramInt2;
        setCheckedCount(paramInt2);
      } 
      this.mCheckedList.put(paramInt1, checkedInfo1);
      return;
    } 
    this.mCheckedList.put(paramInt1, checkedInfo);
    paramInt1 = this.mCheckedCount - 1;
    this.mCheckedCount = paramInt1;
    setCheckedCount(paramInt1);
  }
  
  public boolean getCheckAll() {
    return this.mIsCheckAll;
  }
  
  public int getCheckedCount() {
    return this.mCheckedCount;
  }
  
  public SparseArray<CheckedInfo> getCheckedList() {
    return this.mCheckedList;
  }
  
  public int getChildTotalCount() {
    return this.mChildTotalCount;
  }
  
  public View getChildView(int paramInt1, int paramInt2, boolean paramBoolean, View paramView, ViewGroup paramViewGroup) {
    if (paramView == null) {
      View view1 = newChildView((Context)this.mGalleryFragment.getActivity(), getChild(paramInt1, paramInt2), paramBoolean, paramViewGroup);
      view1.setTag(Integer.valueOf(paramInt1));
      return super.getChildView(paramInt1, paramInt2, paramBoolean, view1, paramViewGroup);
    } 
    View view = paramView;
    if (((LinearLayout)paramView).getChildCount() != this.mColumnNum) {
      Utils.unbindView(paramView);
      view = newChildView((Context)this.mGalleryFragment.getActivity(), getChild(paramInt1, paramInt2), paramBoolean, paramViewGroup);
    } 
    view.setTag(Integer.valueOf(paramInt1));
    return super.getChildView(paramInt1, paramInt2, paramBoolean, view, paramViewGroup);
  }
  
  public int getChildrenCount(int paramInt) {
    int i = getChildrenCountFromCursor(paramInt);
    if (i % this.mColumnNum > 0) {
      paramInt = 1;
      return i / this.mColumnNum + paramInt;
    } 
    paramInt = 0;
    return i / this.mColumnNum + paramInt;
  }
  
  public int getChildrenCountFromCursor(int paramInt) {
    Cursor cursor = getGroup(paramInt);
    return cursor.getInt(cursor.getColumnIndex("_count"));
  }
  
  protected Cursor getChildrenCursor(Cursor paramCursor) {
    GalleryFragment.QueryInfo queryInfo = this.mGalleryFragment.getQuery().getChildQueryInfo(paramCursor);
    return this.mGalleryFragment.getActivity().getContentResolver().query(queryInfo.mUri, queryInfo.mProjection, queryInfo.mSelection, queryInfo.mSelectionArgs, queryInfo.mOrderBy);
  }
  
  public int getColumnNum() {
    return this.mColumnNum;
  }
  
  protected View newChildView(Context paramContext, Cursor paramCursor, boolean paramBoolean, ViewGroup paramViewGroup) {
    Trace.d(TAG, "newChildView()");
    int i = this.mGalleryFragment.getActivity().getResources().getDimensionPixelSize(2131230720);
    LinearLayout linearLayout = new LinearLayout((Context)this.mGalleryFragment.getActivity());
    linearLayout.setPadding(i, 0, i, 0);
    linearLayout.setBackgroundColor(Color.rgb(28, 36, 43));
    linearLayout.setOrientation(0);
    linearLayout.setGravity(1);
    for (i = 0;; i++) {
      if (i >= this.mColumnNum)
        return (View)linearLayout; 
      View view = this.mGalleryFragment.getActivity().getLayoutInflater().inflate(2130903067, null);
      view.setOnClickListener(this);
      view.setOnLongClickListener(this);
      linearLayout.addView(view, this.mColumnWidth, this.mColumnHeight);
    } 
  }
  
  protected View newGroupView(Context paramContext, Cursor paramCursor, boolean paramBoolean, ViewGroup paramViewGroup) {
    Trace.d(TAG, "newGroupView()");
    View view = this.mGalleryFragment.getActivity().getLayoutInflater().inflate(2130903069, null);
    ((CheckBox)view.findViewById(2131558572)).setOnClickListener(this);
    ((RecycleImageView)view.findViewById(2131558574)).setOnClickListener(this);
    return view;
  }
  
  public void notifyDataSetChanged(boolean paramBoolean) {
    super.notifyDataSetChanged(paramBoolean);
    if (paramBoolean)
      this.mChildTotalCount = getChildrenCountFromCursor(); 
  }
  
  public void onClick(View paramView) {
    int i;
    switch (paramView.getId()) {
      default:
        return;
      case 2131558572:
        changeCheckedForGroup(((Integer)paramView.getTag()).intValue());
        return;
      case 2131558574:
        i = ((Integer)paramView.getTag()).intValue();
        if (paramView.isSelected()) {
          this.mGalleryFragment.collapseGroup(i);
          return;
        } 
        this.mGalleryFragment.expandGroup(i);
        return;
      case 2131558566:
        break;
    } 
    GalleryFragment.OnItemClickListener onItemClickListener = this.mGalleryFragment.mOnItemClickListener;
    if (onItemClickListener != null) {
      int[] arrayOfInt = (int[])paramView.getTag();
      onItemClickListener.onItemClick(paramView, arrayOfInt[0], arrayOfInt[1]);
      return;
    } 
  }
  
  public boolean onLongClick(View paramView) {
    boolean bool = false;
    GalleryFragment.OnItemLongClickListener onItemLongClickListener = this.mGalleryFragment.mOnItemLongClickListener;
    if (onItemLongClickListener != null) {
      int[] arrayOfInt = (int[])paramView.getTag();
      bool = onItemLongClickListener.onItemLongClick(paramView, arrayOfInt[0], arrayOfInt[1]);
    } 
    return bool;
  }
  
  public void onOrientationChanged() {
    initCoordinate();
  }
  
  public void setCheckAll(boolean paramBoolean) {
    if ((this.mCheckedCount == 0 || this.mCheckedCount == this.mChildTotalCount) && this.mIsCheckAll == paramBoolean)
      return; 
    this.mIsCheckAll = paramBoolean;
    if (paramBoolean) {
      setCheckedCount(this.mChildTotalCount);
    } else {
      setCheckedCount(0);
    } 
    this.mCheckedList.clear();
    notifyDataSetChanged(false);
  }
  
  public void setCheckable(boolean paramBoolean) {
    if (this.mIsCheckable == paramBoolean)
      return; 
    this.mIsCheckable = paramBoolean;
    if (!paramBoolean) {
      this.mIsCheckAll = false;
      setCheckedCount(0);
      this.mCheckedList.clear();
    } 
    notifyDataSetChanged(false);
  }
  
  protected class CheckedInfo {
    private SparseBooleanArray mChildCheckedArray = new SparseBooleanArray();
    
    private boolean mIsChecked = false;
    
    public SparseBooleanArray getChildCheckedArray() {
      return this.mChildCheckedArray;
    }
    
    public boolean isChecked() {
      return this.mIsChecked;
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\gallery\GalleryAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */