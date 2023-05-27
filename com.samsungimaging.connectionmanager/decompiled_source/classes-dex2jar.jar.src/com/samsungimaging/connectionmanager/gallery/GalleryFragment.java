package com.samsungimaging.connectionmanager.gallery;

import android.app.Fragment;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.dialog.CustomDialog;
import com.samsungimaging.connectionmanager.dialog.CustomProgressDialog;
import com.samsungimaging.connectionmanager.manager.DatabaseManager;
import com.samsungimaging.connectionmanager.provider.DatabaseMedia;
import com.samsungimaging.connectionmanager.util.AsyncTask;
import com.samsungimaging.connectionmanager.util.ImageLoader;
import com.samsungimaging.connectionmanager.util.RecycleBitmapDrawable;
import com.samsungimaging.connectionmanager.util.Trace;
import com.samsungimaging.connectionmanager.util.Utils;
import com.samsungimaging.connectionmanager.widget.ImageViewer;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment implements AbsListView.OnScrollListener, ExpandableListView.OnGroupClickListener, GalleryPagerAdapter.OnInstantiateItemListener {
  public static final String DISK_CACHE_DIR_THUMBNAIL = "thumbnail";
  
  public static final String DISK_CACHE_DIR_VIEWER = "viewer";
  
  private static final int HANDLER_RECEIVED_MEDIA = 0;
  
  private static final int QUERY_HANDLER_GROUP = 0;
  
  private static final Trace.Tag TAG = Trace.Tag.COMMON;
  
  private ArrayList<DatabaseMedia> delete_data_list;
  
  TextView emptyView;
  
  public List<String> groupList = new ArrayList<String>();
  
  private GalleryAdapter mAdapter = null;
  
  private QueryHandler mAsyncQueryHandler = null;
  
  private InternalContentObserver mContentObserver = new InternalContentObserver();
  
  private GalleryPager mGalleryPager = null;
  
  private GalleryPagerAdapter mGalleryPagerAdapter = null;
  
  private Cursor mGroupCursor = null;
  
  private boolean mGroupCursorChangeable = true;
  
  private Handler mHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        switch (param1Message.what) {
          default:
            return;
          case 0:
            break;
        } 
        GalleryFragment.this.requeryForGroup();
        GalleryFragment.this.setSelectionChild((DatabaseMedia)param1Message.obj);
      }
    };
  
  private ImageLoader mImageLoaderThumbnail = null;
  
  private ImageLoader mImageLoaderViewer = null;
  
  private ExpandableListView mListView = null;
  
  protected OnItemClickListener mOnItemClickListener = null;
  
  protected OnItemLongClickListener mOnItemLongClickListener = null;
  
  private Query mQuery = null;
  
  private State mState = State.NORMAL;
  
  private int[] getGalleryPositions(int paramInt) {
    int i = 0;
    int j = paramInt;
    paramInt = 0;
    while (true) {
      if (paramInt >= this.mAdapter.getGroupCount()) {
        int n = i;
        return new int[] { n, j };
      } 
      i = paramInt;
      int m = this.mAdapter.getChildrenCountFromCursor(i);
      int k = i;
      if (j - m >= 0) {
        j -= m;
        paramInt++;
        continue;
      } 
      return new int[] { k, j };
    } 
  }
  
  private int getPagerPosition(int paramInt1, int paramInt2) {
    boolean bool = false;
    int i = paramInt2;
    for (paramInt2 = bool;; paramInt2++) {
      if (paramInt2 >= paramInt1)
        return i; 
      i += this.mAdapter.getChildrenCountFromCursor(paramInt2);
    } 
  }
  
  private void queryForGroup() {
    QueryInfo queryInfo = this.mQuery.getGroupQueryInfo();
    this.mAsyncQueryHandler.startQuery(0, null, queryInfo.mUri, queryInfo.mProjection, queryInfo.mSelection, queryInfo.mSelectionArgs, queryInfo.mOrderBy);
  }
  
  private void requeryForGroup() {
    if (this.mGroupCursor != null && this.mGroupCursorChangeable) {
      Trace.d(TAG, "requeryForGroupCursor");
      this.mGroupCursor.requery();
      expandGroupAll();
      getActivity().invalidateOptionsMenu();
      if (this.mGalleryPagerAdapter != null)
        this.mGalleryPagerAdapter.notifyDataSetChanged(); 
    } 
  }
  
  private void setSelectionChild(DatabaseMedia paramDatabaseMedia) {
    Cursor cursor = this.mAdapter.getCursor();
    int j = 0;
    int i = 0;
    while (true) {
      if (i >= cursor.getCount()) {
        i = j;
      } else {
        cursor.moveToPosition(i);
        String str = cursor.getString(cursor.getColumnIndex("datetaken_string"));
        if (!paramDatabaseMedia.getDateTakenString().equals(str)) {
          i++;
          continue;
        } 
      } 
      cursor = this.mAdapter.getChildrenCursor(cursor);
      byte b = 0;
      j = 0;
      while (true) {
        if (j >= cursor.getCount()) {
          j = b;
        } else {
          cursor.moveToPosition(j);
          String str = cursor.getString(cursor.getColumnIndex("_data"));
          if (paramDatabaseMedia.getOriginalPath().equals(str)) {
            j /= this.mAdapter.getColumnNum();
          } else {
            j++;
            continue;
          } 
        } 
        this.mListView.setSelectedChild(i, j, true);
        return;
      } 
      break;
    } 
  }
  
  public void CheckDirtyCache(String paramString) {
    if (this.mImageLoaderThumbnail != null && this.mImageLoaderThumbnail.hasSnapshotInDiskCache(paramString)) {
      Trace.i(Trace.Tag.COMMON, "CheckDirtyCache remove Thumbnail Cache : " + paramString);
      this.mImageLoaderThumbnail.removeCache(paramString);
    } 
    if (this.mImageLoaderViewer != null && this.mImageLoaderViewer.hasSnapshotInDiskCache(paramString)) {
      Trace.i(Trace.Tag.COMMON, "CheckDirtyCache remove Viewer Cache : " + paramString);
      this.mImageLoaderViewer.removeCache(paramString);
    } 
  }
  
  public void clearCache() {
    this.mImageLoaderThumbnail.clearCache();
    this.mImageLoaderViewer.clearCache();
  }
  
  public void collapseGroup(int paramInt) {
    if (this.mListView != null) {
      this.mListView.collapseGroup(paramInt);
      if (!this.groupList.contains(Integer.valueOf(paramInt))) {
        String str = (new StringBuilder(String.valueOf(paramInt))).toString();
        this.groupList.add(str);
      } 
    } 
  }
  
  public void delete() {
    int i = getCheckedCount();
    CustomDialog customDialog = new CustomDialog((Context)getActivity());
    customDialog.setTitle(2131362044);
    customDialog.setMessage(getString(2131362045, new Object[] { Integer.valueOf(i) }));
    customDialog.setPositiveButton(17039360, null);
    customDialog.setNegativeButton(17039370, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface param1DialogInterface, int param1Int) {
            param1DialogInterface.dismiss();
            GalleryFragment.this.getdeletdata();
            (new GalleryFragment.DeleteAsyncTask(null)).execute((Object[])new Void[0]);
          }
        });
    customDialog.show();
  }
  
  public void expandGroup(int paramInt) {
    this.mListView.expandGroup(paramInt);
    if (this.mListView != null && this.groupList.contains((new StringBuilder(String.valueOf(paramInt))).toString()))
      this.groupList.remove((new StringBuilder(String.valueOf(paramInt))).toString()); 
  }
  
  public void expandGroupAll() {
    for (int i = 0;; i++) {
      if (i >= this.mAdapter.getGroupCount())
        return; 
      if (this.groupList.contains((new StringBuilder(String.valueOf(i))).toString())) {
        this.mListView.collapseGroup(i);
      } else {
        this.mListView.expandGroup(i);
      } 
    } 
  }
  
  public int getCheckedCount() {
    int i = 0;
    if (this.mState == State.MULTI_SELECT) {
      if (this.mAdapter != null)
        i = this.mAdapter.getCheckedCount(); 
      return i;
    } 
    return (this.mState == State.IMAGE_VIEWER) ? 1 : i;
  }
  
  public int getChildTotalCount() {
    return this.mAdapter.getChildTotalCount();
  }
  
  public Query getQuery() {
    return this.mQuery;
  }
  
  public State getState() {
    return this.mState;
  }
  
  public void getdeletdata() {
    this.delete_data_list = new ArrayList<DatabaseMedia>();
    if (this.mState == State.MULTI_SELECT) {
      if (this.mAdapter.getCheckAll()) {
        int j = 0;
        label35: while (true) {
          if (j < this.mAdapter.getGroupCount()) {
            for (int k = 0;; k++) {
              if (k >= this.mAdapter.getChildrenCountFromCursor(j)) {
                j++;
                continue label35;
              } 
              DatabaseMedia databaseMedia = DatabaseMedia.builder(this.mAdapter.getChild(j, k));
              this.delete_data_list.add(databaseMedia);
            } 
            break;
          } 
          return;
        } 
      } 
      SparseArray<GalleryAdapter.CheckedInfo> sparseArray = this.mAdapter.getCheckedList();
      int i = 0;
      label36: while (true) {
        if (i < sparseArray.size()) {
          DatabaseMedia databaseMedia;
          GalleryAdapter.CheckedInfo checkedInfo = (GalleryAdapter.CheckedInfo)sparseArray.valueAt(i);
          int k = sparseArray.keyAt(i);
          if (checkedInfo.isChecked()) {
            int m = 0;
            while (true) {
              if (m < this.mAdapter.getChildrenCountFromCursor(k)) {
                databaseMedia = DatabaseMedia.builder(this.mAdapter.getChild(k, m));
                this.delete_data_list.add(databaseMedia);
                m++;
                continue;
              } 
              i++;
              continue label36;
            } 
            break;
          } 
          SparseBooleanArray sparseBooleanArray = databaseMedia.getChildCheckedArray();
          int j = 0;
          while (true) {
            if (j < sparseBooleanArray.size()) {
              int m = sparseBooleanArray.keyAt(j);
              DatabaseMedia databaseMedia1 = DatabaseMedia.builder(this.mAdapter.getChild(k, m));
              this.delete_data_list.add(databaseMedia1);
              j++;
              continue;
            } 
            i++;
            continue label36;
          } 
          break;
        } 
        return;
      } 
    } 
    if (this.mState == State.IMAGE_VIEWER) {
      this.delete_data_list.add(this.mGalleryPager.getCurrentImageViewer().getMedia());
      return;
    } 
  }
  
  public boolean isEmpty() {
    return !(this.mAdapter != null && this.mAdapter.getChildTotalCount() > 0);
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration) {
    super.onConfigurationChanged(paramConfiguration);
    if (this.mAdapter != null) {
      this.mAdapter.onOrientationChanged();
      this.mAdapter.notifyDataSetChanged(false);
    } 
  }
  
  public void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    Trace.d(TAG, "onCreate()");
    this.mAsyncQueryHandler = new QueryHandler(getActivity().getContentResolver());
    if (this.mQuery != null)
      queryForGroup(); 
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle) {
    Trace.d(TAG, "onCreateView()");
    View view = paramLayoutInflater.inflate(2130903068, paramViewGroup, false);
    view.setOnTouchListener(new View.OnTouchListener() {
          public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
            return true;
          }
        });
    this.mListView = (ExpandableListView)view.findViewById(2131558570);
    this.emptyView = (TextView)view.findViewById(2131558565);
    this.mListView.setEmptyView((View)this.emptyView);
    this.mListView.setOnGroupClickListener(this);
    this.mGalleryPager = (GalleryPager)view.findViewById(2131558569);
    return view;
  }
  
  public void onDestroy() {
    Trace.d(TAG, "onDestroy()");
    this.mAsyncQueryHandler.removeMessages(0);
    this.mAsyncQueryHandler = null;
    super.onDestroy();
  }
  
  public void onDestroyView() {
    Trace.d(TAG, "onDestroyView()");
    Utils.unbindView(getView());
    if (this.mGroupCursor != null) {
      this.mGroupCursor.unregisterContentObserver(this.mContentObserver);
      this.mGroupCursor.close();
      this.mGroupCursor = null;
    } 
    if (this.mAdapter != null) {
      this.mAdapter.changeCursor(null);
      this.mAdapter = null;
    } 
    if (this.mGalleryPagerAdapter != null) {
      this.mGalleryPagerAdapter.clear();
      this.mGalleryPagerAdapter = null;
    } 
    if (this.mGalleryPager != null) {
      this.mGalleryPager.removeAllViews();
      this.mGalleryPager = null;
    } 
    if (this.mImageLoaderThumbnail != null) {
      this.mImageLoaderThumbnail.clearMemoryCache();
      this.mImageLoaderThumbnail.closeCache();
      this.mImageLoaderThumbnail = null;
    } 
    if (this.mImageLoaderViewer != null) {
      this.mImageLoaderViewer.clearMemoryCache();
      this.mImageLoaderViewer.closeCache();
      this.mImageLoaderViewer = null;
    } 
    super.onDestroyView();
  }
  
  public boolean onGroupClick(ExpandableListView paramExpandableListView, View paramView, int paramInt, long paramLong) {
    return true;
  }
  
  public ImageViewer onInstantiateItem(ViewGroup paramViewGroup, int paramInt) {
    int[] arrayOfInt = getGalleryPositions(paramInt);
    ImageViewer imageViewer = new ImageViewer((Context)getActivity());
    imageViewer.setImageLoader(this.mImageLoaderViewer);
    DatabaseMedia databaseMedia = DatabaseMedia.builder(this.mAdapter.getChild(arrayOfInt[0], arrayOfInt[1]));
    RecycleBitmapDrawable recycleBitmapDrawable2 = this.mImageLoaderThumbnail.getBitmapDrawableFromMemoryCache(databaseMedia.getThumbnailPath());
    RecycleBitmapDrawable recycleBitmapDrawable1 = recycleBitmapDrawable2;
    if (recycleBitmapDrawable2 == null)
      recycleBitmapDrawable1 = this.mImageLoaderThumbnail.loadImageSync(databaseMedia); 
    imageViewer.loadImage(databaseMedia, recycleBitmapDrawable1);
    ((ViewPager)paramViewGroup).addView((View)imageViewer);
    return imageViewer;
  }
  
  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
    return false;
  }
  
  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent) {
    boolean bool2 = false;
    boolean bool1 = bool2;
    if (paramInt == 4) {
      bool1 = bool2;
      if (this.mState != State.NORMAL) {
        setState(State.NORMAL, new Object[0]);
        bool1 = true;
      } 
    } 
    return bool1;
  }
  
  public void onPause() {
    Trace.d(TAG, "onPause()");
    if (this.mGroupCursor != null)
      this.mGroupCursor.deactivate(); 
    if (this.mImageLoaderThumbnail != null) {
      this.mImageLoaderThumbnail.setPauseLoad(false);
      this.mImageLoaderThumbnail.flushCache();
    } 
    if (this.mImageLoaderViewer != null) {
      this.mImageLoaderViewer.setPauseLoad(false);
      this.mImageLoaderViewer.flushCache();
    } 
    this.emptyView.setVisibility(8);
    super.onPause();
  }
  
  public void onResume() {
    Trace.d(TAG, "onResume()");
    requeryForGroup();
    super.onResume();
  }
  
  public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3) {}
  
  public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt) {
    if (paramInt == 2) {
      this.mImageLoaderThumbnail.setPauseLoad(true);
      return;
    } 
    this.mImageLoaderThumbnail.setPauseLoad(false);
  }
  
  public void setCheckAll(boolean paramBoolean) {
    if (this.mAdapter != null)
      this.mAdapter.setCheckAll(paramBoolean); 
  }
  
  public void setChecked(int paramInt1, int paramInt2) {
    if (this.mAdapter != null) {
      this.mAdapter.changeCheckedForChild(paramInt1, paramInt2);
      this.mAdapter.notifyDataSetChanged(false);
    } 
  }
  
  public void setDiskCacheDir(String paramString1, String paramString2) {
    this.mImageLoaderThumbnail = new ImageLoader((Context)getActivity(), ImageLoader.ImageSize.SMALL, paramString1);
    this.mImageLoaderViewer = new ImageLoader((Context)getActivity(), ImageLoader.ImageSize.LARGE, paramString2);
  }
  
  public void setOnItemClickListener(OnItemClickListener paramOnItemClickListener) {
    this.mOnItemClickListener = paramOnItemClickListener;
  }
  
  public void setOnItemLongClickListener(OnItemLongClickListener paramOnItemLongClickListener) {
    this.mOnItemLongClickListener = paramOnItemLongClickListener;
  }
  
  public void setQuery(Query paramQuery) {
    if (paramQuery != null) {
      this.mQuery = paramQuery;
      if (this.mAsyncQueryHandler != null) {
        queryForGroup();
        return;
      } 
    } 
  }
  
  public void setReceivedMedia(DatabaseMedia paramDatabaseMedia) {
    if (this.mHandler.hasMessages(0))
      this.mHandler.removeMessages(0); 
    Message message = Message.obtain();
    message.what = 0;
    message.obj = paramDatabaseMedia;
    this.mHandler.sendMessage(message);
  }
  
  public void setState(State paramState, Object... paramVarArgs) {
    this.mState = paramState;
    switch (paramState) {
      default:
        getActivity().invalidateOptionsMenu();
        return;
      case NORMAL:
        if (this.mImageLoaderViewer != null) {
          this.mImageLoaderViewer.setPauseLoad(false);
          this.mImageLoaderViewer.flushCache();
        } 
        if (this.mGalleryPager.getVisibility() == 0) {
          ImageViewer imageViewer = this.mGalleryPager.getCurrentImageViewer();
          if (imageViewer != null && !imageViewer.isScaleMin())
            imageViewer.setScaleMin(); 
          this.mGalleryPager.setVisibility(8);
          this.mListView.setVisibility(0);
        } 
        if (this.mAdapter != null)
          this.mAdapter.setCheckable(false); 
      case MULTI_SELECT:
        if (this.mImageLoaderViewer != null) {
          this.mImageLoaderViewer.setPauseLoad(false);
          this.mImageLoaderViewer.flushCache();
        } 
        if (this.mGalleryPager.getVisibility() == 0) {
          ImageViewer imageViewer = this.mGalleryPager.getCurrentImageViewer();
          if (imageViewer != null && !imageViewer.isScaleMin())
            imageViewer.setScaleMin(); 
          this.mGalleryPager.setVisibility(8);
          this.mListView.setVisibility(0);
        } 
        if (this.mAdapter != null)
          this.mAdapter.setCheckable(true); 
        if (paramVarArgs.length > 0)
          setChecked(((Integer)paramVarArgs[0]).intValue(), ((Integer)paramVarArgs[1]).intValue()); 
      case IMAGE_VIEWER:
        break;
    } 
    if (this.mImageLoaderThumbnail != null) {
      this.mImageLoaderThumbnail.setPauseLoad(false);
      this.mImageLoaderThumbnail.flushCache();
    } 
    if (this.mGalleryPagerAdapter == null) {
      this.mGalleryPagerAdapter = new GalleryPagerAdapter(this);
      this.mGalleryPagerAdapter.setOnInstantiateItemListener(this);
      this.mGalleryPager.setAdapter(this.mGalleryPagerAdapter);
    } 
    this.mGalleryPager.setCurrentItem(getPagerPosition(((Integer)paramVarArgs[0]).intValue(), ((Integer)paramVarArgs[1]).intValue()), false);
    this.mGalleryPager.setVisibility(0);
    this.mListView.setVisibility(8);
  }
  
  private class DeleteAsyncTask extends AsyncTask<Void, Integer, Void> {
    private CustomProgressDialog mProgressDialog = null;
    
    private DeleteAsyncTask() {}
    
    private void delete(DatabaseMedia param1DatabaseMedia, int param1Int) {
      DatabaseManager.deleteForLocalMedia((Context)GalleryFragment.this.getActivity(), param1DatabaseMedia);
      if (GalleryFragment.this.mImageLoaderThumbnail != null)
        GalleryFragment.this.mImageLoaderThumbnail.removeCache(param1DatabaseMedia.getThumbnailPath()); 
      if (GalleryFragment.this.mImageLoaderViewer != null)
        GalleryFragment.this.mImageLoaderViewer.removeCache(param1DatabaseMedia.getViewerPath()); 
      publishProgress((Object[])new Integer[] { Integer.valueOf(param1Int) });
    }
    
    private void finish() {
      GalleryFragment.this.setState(GalleryFragment.State.NORMAL, new Object[0]);
      if (this.mProgressDialog != null) {
        this.mProgressDialog.dismiss();
        this.mProgressDialog = null;
      } 
      GalleryFragment.this.requeryForGroup();
    }
    
    protected Void doInBackground(Void... param1VarArgs) {
      int i = 0;
      if (GalleryFragment.this.delete_data_list != null) {
        int j = 0;
        while (true) {
          if (j < GalleryFragment.this.delete_data_list.size()) {
            DatabaseMedia databaseMedia = GalleryFragment.this.delete_data_list.get(j);
            delete(databaseMedia, ++i);
            j++;
            continue;
          } 
          GalleryFragment.this.mGroupCursorChangeable = true;
          return null;
        } 
      } 
      GalleryFragment.this.mGroupCursorChangeable = true;
      return null;
    }
    
    protected void onCancelled() {
      finish();
      super.onCancelled();
    }
    
    protected void onPostExecute(Void param1Void) {
      finish();
      GalleryFragment.this.delete_data_list = null;
      super.onPostExecute(param1Void);
    }
    
    protected void onPreExecute() {
      GalleryFragment.this.mGroupCursorChangeable = false;
      int i = GalleryFragment.this.getCheckedCount();
      this.mProgressDialog = new CustomProgressDialog((Context)GalleryFragment.this.getActivity());
      this.mProgressDialog.setProgressStyle(1);
      this.mProgressDialog.setTitle(2131362044);
      this.mProgressDialog.setMax(i);
      this.mProgressDialog.setCancelable(false);
      this.mProgressDialog.show();
    }
    
    protected void onProgressUpdate(Integer... param1VarArgs) {
      this.mProgressDialog.setProgress(param1VarArgs[0].intValue());
      super.onProgressUpdate((Object[])param1VarArgs);
    }
  }
  
  private class InternalContentObserver extends ContentObserver {
    public InternalContentObserver() {
      super(new Handler());
    }
    
    public boolean deliverSelfNotifications() {
      return true;
    }
    
    public void onChange(boolean param1Boolean) {
      GalleryFragment.this.requeryForGroup();
    }
  }
  
  public static interface OnItemClickListener {
    void onItemClick(View param1View, int param1Int1, int param1Int2);
  }
  
  public static interface OnItemLongClickListener {
    boolean onItemLongClick(View param1View, int param1Int1, int param1Int2);
  }
  
  public static interface Query {
    GalleryFragment.QueryInfo getChildQueryInfo(Cursor param1Cursor);
    
    GalleryFragment.QueryInfo getGroupQueryInfo();
  }
  
  private class QueryHandler extends AsyncQueryHandler {
    public QueryHandler(ContentResolver param1ContentResolver) {
      super(param1ContentResolver);
    }
    
    protected void onQueryComplete(int param1Int, Object param1Object, Cursor param1Cursor) {
      Trace.d(GalleryFragment.TAG, "onQueryComplete() - " + param1Int);
      if (GalleryFragment.this.mAsyncQueryHandler == null)
        return; 
      switch (param1Int) {
        default:
          return;
        case 0:
          break;
      } 
      if (GalleryFragment.this.mListView != null)
        if (GalleryFragment.this.mAdapter == null) {
          GalleryFragment.this.mAdapter = new GalleryAdapter(GalleryFragment.this, param1Cursor, GalleryFragment.this.mImageLoaderThumbnail);
          if (GalleryFragment.this.mState == GalleryFragment.State.MULTI_SELECT)
            GalleryFragment.this.mAdapter.setCheckable(true); 
          GalleryFragment.this.mListView.setAdapter((ExpandableListAdapter)GalleryFragment.this.mAdapter);
          GalleryFragment.this.expandGroupAll();
        } else {
          GalleryFragment.this.mAdapter.changeCursor(param1Cursor);
          GalleryFragment.this.expandGroupAll();
        }  
      if (param1Cursor != null) {
        GalleryFragment.this.mGroupCursor = param1Cursor;
        GalleryFragment.this.mGroupCursor.registerContentObserver(GalleryFragment.this.mContentObserver);
      } 
      GalleryFragment.this.getActivity().invalidateOptionsMenu();
    }
  }
  
  public static final class QueryInfo {
    public String mOrderBy = null;
    
    public String[] mProjection = null;
    
    public String mSelection = null;
    
    public String[] mSelectionArgs = null;
    
    public Uri mUri = null;
    
    public QueryInfo(Uri param1Uri, String[] param1ArrayOfString1, String param1String1, String[] param1ArrayOfString2, String param1String2) {
      this.mUri = param1Uri;
      this.mProjection = param1ArrayOfString1;
      this.mSelection = param1String1;
      this.mSelectionArgs = param1ArrayOfString2;
      this.mOrderBy = param1String2;
    }
  }
  
  public enum State {
    IMAGE_VIEWER, MULTI_SELECT, NORMAL;
    
    static {
      IMAGE_VIEWER = new State("IMAGE_VIEWER", 2);
      ENUM$VALUES = new State[] { NORMAL, MULTI_SELECT, IMAGE_VIEWER };
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\gallery\GalleryFragment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */