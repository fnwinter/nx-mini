package com.samsungimaging.connectionmanager.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DatabaseMedia implements GalleryColumns {
  public static final int COLUMN_DATE_TAKEN = 5;
  
  public static final int COLUMN_ID = 0;
  
  public static final int COLUMN_MEDIA_TYPE = 4;
  
  public static final int COLUMN_ORIENTATION = 6;
  
  public static final int COLUMN_ORIGINAL_PATH = 1;
  
  public static final int COLUMN_SCREEN_PATH = 3;
  
  public static final int COLUMN_THUMBNAIL_PATH = 2;
  
  protected static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.samsungimaging.connectionmanager.cameramedia";
  
  protected static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.samsungimaging.connectionmanager.cameramedia";
  
  public static final Uri CONTENT_URI = Uri.parse("content://com.samsungimaging.connectionmanager/CameraMedia");
  
  protected static final String CREATE_TABLE = "CREATE TABLE CameraMedia (_id INTEGER PRIMARY KEY, _data TEXT, thumbnail_path TEXT, screen_path TEXT, media_type INTEGER, datetaken INTEGER, orientation INTEGER);";
  
  protected static final String DEFAULT_SORT_ORDER = "datetaken DESC";
  
  protected static final String[] PROJECTION = new String[] { "_id", "_data", "thumbnail_path", "screen_path", "media_type", "datetaken", "orientation" };
  
  protected static HashMap<String, String> PROJECTION_MAP = new HashMap<String, String>();
  
  protected static final String TABLE_NAME = "CameraMedia";
  
  private long mDateTaken = 0L;
  
  private String mDateTakenString = null;
  
  private int mMediaType = 0;
  
  private int mOrientation = 0;
  
  private String mOriginalPath = null;
  
  private String mThumbnailPath = null;
  
  private long mUriID = -1L;
  
  private String mViewerPath = null;
  
  static {
    PROJECTION_MAP.put("_id", "_id");
    PROJECTION_MAP.put("_data", "_data");
    PROJECTION_MAP.put("thumbnail_path", "thumbnail_path");
    PROJECTION_MAP.put("screen_path", "screen_path");
    PROJECTION_MAP.put("media_type", "media_type");
    PROJECTION_MAP.put("datetaken", "datetaken");
    PROJECTION_MAP.put("orientation", "orientation");
  }
  
  public DatabaseMedia() {}
  
  public DatabaseMedia(String paramString1, String paramString2, String paramString3, int paramInt1, long paramLong, int paramInt2) {
    this.mOriginalPath = paramString1;
    this.mThumbnailPath = paramString2;
    this.mViewerPath = paramString3;
    this.mMediaType = paramInt1;
    this.mDateTaken = paramLong;
    this.mDateTakenString = (new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())).format(new Date(this.mDateTaken));
    this.mOrientation = paramInt2;
  }
  
  public static DatabaseMedia builder(Cursor paramCursor) {
    DatabaseMedia databaseMedia = new DatabaseMedia();
    databaseMedia.mUriID = paramCursor.getLong(paramCursor.getColumnIndex("_id"));
    databaseMedia.mOriginalPath = paramCursor.getString(paramCursor.getColumnIndex("_data"));
    try {
      databaseMedia.mThumbnailPath = paramCursor.getString(paramCursor.getColumnIndexOrThrow("thumbnail_path"));
      databaseMedia.mViewerPath = paramCursor.getString(paramCursor.getColumnIndexOrThrow("screen_path"));
    } catch (Exception exception) {
      databaseMedia.mThumbnailPath = databaseMedia.mOriginalPath;
      databaseMedia.mViewerPath = databaseMedia.mOriginalPath;
    } 
    databaseMedia.mDateTaken = paramCursor.getLong(paramCursor.getColumnIndex("datetaken"));
    databaseMedia.mDateTakenString = (new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())).format(new Date(databaseMedia.mDateTaken));
    try {
      databaseMedia.mMediaType = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("media_type"));
    } catch (Exception exception) {}
    try {
      databaseMedia.mOrientation = paramCursor.getInt(paramCursor.getColumnIndexOrThrow("orientation"));
      return databaseMedia;
    } catch (Exception exception) {
      return databaseMedia;
    } 
  }
  
  public ContentValues getContentValues() {
    ContentValues contentValues = new ContentValues();
    contentValues.put("_data", this.mOriginalPath);
    contentValues.put("thumbnail_path", this.mThumbnailPath);
    contentValues.put("screen_path", this.mViewerPath);
    contentValues.put("media_type", Integer.valueOf(this.mMediaType));
    contentValues.put("datetaken", Long.valueOf(this.mDateTaken));
    contentValues.put("orientation", Integer.valueOf(this.mOrientation));
    return contentValues;
  }
  
  public long getDateTaken() {
    return this.mDateTaken;
  }
  
  public String getDateTakenString() {
    return this.mDateTakenString;
  }
  
  public long getID() {
    return this.mUriID;
  }
  
  public int getMediaType() {
    return this.mMediaType;
  }
  
  public int getOrientation() {
    return this.mOrientation;
  }
  
  public String getOriginalPath() {
    return this.mOriginalPath;
  }
  
  public String getThumbnailPath() {
    return this.mThumbnailPath;
  }
  
  public Uri getUri() {
    return (this.mUriID != -1L) ? ContentUris.withAppendedId(CONTENT_URI, this.mUriID) : null;
  }
  
  public String getViewerPath() {
    return this.mViewerPath;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("Meida");
    stringBuilder.append(" [");
    stringBuilder.append("_data=" + this.mOriginalPath);
    stringBuilder.append("thumbnail_path=" + this.mThumbnailPath);
    stringBuilder.append("screen_path=" + this.mViewerPath);
    stringBuilder.append(", media_type=" + this.mMediaType);
    stringBuilder.append(", datetaken=" + this.mDateTaken);
    stringBuilder.append(", datetaken_string=" + this.mDateTakenString);
    stringBuilder.append(", orientation=" + this.mOrientation);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\provider\DatabaseMedia.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */