package com.samsungimaging.connectionmanager.widget;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.samsungimaging.connectionmanager.provider.DatabaseMedia;
import com.samsungimaging.connectionmanager.util.ImageLoader;
import com.samsungimaging.connectionmanager.util.RecycleBitmapDrawable;
import com.samsungimaging.connectionmanager.view.PinchZoomView;
import com.samsungimaging.connectionmanager.view.RecycleImageView;
import java.io.File;

public class ImageViewer extends FrameLayout implements View.OnClickListener {
  private ImageView mIconPlay = null;
  
  private ImageLoader mImageLoader = null;
  
  private RecycleImageView mLoadingView = null;
  
  private DatabaseMedia mMedia = null;
  
  private PinchZoomView mPinchZoom = null;
  
  public ImageViewer(Context paramContext) {
    super(paramContext);
    init();
  }
  
  public ImageViewer(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    init();
  }
  
  public ImageViewer(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    init();
  }
  
  public static Uri fetchUriUsingContentProvider(Context paramContext, String paramString) {
    Uri uri1;
    ContentValues contentValues;
    Uri uri2 = null;
    File file = new File(paramString);
    if (paramString.endsWith(".mp4") || paramString.endsWith(".MP4")) {
      Cursor cursor1 = paramContext.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, new String[] { "_id" }, "_data=? ", new String[] { paramString }, null);
      if (cursor1 != null) {
        if (cursor1.moveToFirst()) {
          int i = cursor1.getInt(cursor1.getColumnIndex("_id"));
          uri1 = Uri.parse("content://media/external/video/media");
          cursor1.close();
          return Uri.withAppendedPath(uri1, i);
        } 
      } else {
        return uri2;
      } 
      if (file.exists()) {
        contentValues = new ContentValues();
        contentValues.put("_data", paramString);
        cursor1.close();
        return uri1.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
      } 
      cursor1.close();
      return null;
    } 
    Cursor cursor = uri1.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { "_id" }, "_data=? ", new String[] { paramString }, null);
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        int i = cursor.getInt(cursor.getColumnIndex("_id"));
        uri1 = Uri.parse("content://media/external/images/media");
        cursor.close();
        return Uri.withAppendedPath(uri1, i);
      } 
      if (file.exists()) {
        contentValues = new ContentValues();
        contentValues.put("_data", paramString);
        cursor.close();
        return uri1.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
      } 
      cursor.close();
      return null;
    } 
    return (Uri)contentValues;
  }
  
  private void init() {
    View view = ((LayoutInflater)getContext().getSystemService("layout_inflater")).inflate(2130903071, null);
    this.mLoadingView = (RecycleImageView)view.findViewById(2131558575);
    this.mPinchZoom = (PinchZoomView)view.findViewById(2131558576);
    this.mIconPlay = (ImageView)view.findViewById(2131558568);
    this.mIconPlay.setOnClickListener(this);
    addView(view);
  }
  
  public void clearImage() {
    if (this.mImageLoader != null)
      this.mImageLoader.cancelLoader((RecycleImageView)this.mPinchZoom); 
    this.mPinchZoom.setImageDrawable(null);
    this.mLoadingView.setImageDrawable(null);
  }
  
  public DatabaseMedia getMedia() {
    return this.mMedia;
  }
  
  public boolean hasValidBitmap() {
    return (this.mPinchZoom.getDrawable() instanceof RecycleBitmapDrawable) ? ((RecycleBitmapDrawable)this.mPinchZoom.getDrawable()).hasValidBitmap() : false;
  }
  
  public boolean isBoundaryLeft() {
    return this.mPinchZoom.isBoundaryLeft();
  }
  
  public boolean isBoundaryRight() {
    return this.mPinchZoom.isBoundaryRight();
  }
  
  public boolean isScaleMin() {
    return this.mPinchZoom.isScaleMin();
  }
  
  public void loadImage(DatabaseMedia paramDatabaseMedia, RecycleBitmapDrawable paramRecycleBitmapDrawable) {
    this.mMedia = paramDatabaseMedia;
    if (this.mMedia.getMediaType() == 1) {
      this.mIconPlay.setVisibility(4);
      if (paramRecycleBitmapDrawable.isNoneImage()) {
        this.mPinchZoom.setPinchZoomable(false);
      } else {
        this.mPinchZoom.setPinchZoomable(true);
      } 
    } else {
      this.mIconPlay.setVisibility(0);
      this.mPinchZoom.setPinchZoomable(false);
    } 
    if (URLUtil.isHttpUrl(this.mMedia.getViewerPath())) {
      this.mLoadingView.setVisibility(0);
      this.mLoadingView.setImageBitmap(paramRecycleBitmapDrawable.getBitmap());
    } else {
      BitmapFactory.Options options = new BitmapFactory.Options();
      options.inJustDecodeBounds = true;
      BitmapFactory.decodeFile(this.mMedia.getViewerPath(), options);
      DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
      if (options.outWidth * 6.0F < displayMetrics.widthPixels && options.outHeight * 6.0F < displayMetrics.heightPixels && this.mMedia.getMediaType() == 1) {
        this.mLoadingView.setVisibility(4);
      } else {
        this.mLoadingView.setVisibility(0);
        this.mLoadingView.setImageBitmap(paramRecycleBitmapDrawable.getBitmap());
      } 
    } 
    if (this.mImageLoader != null)
      this.mImageLoader.loadImage(this.mMedia, (RecycleImageView)this.mPinchZoom); 
  }
  
  public void onClick(View paramView) {
    try {
      Uri uri = fetchUriUsingContentProvider(getContext(), this.mMedia.getOriginalPath());
      Intent intent = new Intent();
      intent.setAction("android.intent.action.VIEW");
      intent.setDataAndType(uri, "video/*");
      getContext().startActivity(intent);
      return;
    } catch (Exception exception) {
      exception.printStackTrace();
      return;
    } 
  }
  
  public void setImageLoader(ImageLoader paramImageLoader) {
    this.mImageLoader = paramImageLoader;
  }
  
  public void setScaleMin() {
    this.mPinchZoom.setScaleMin();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\widget\ImageViewer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */