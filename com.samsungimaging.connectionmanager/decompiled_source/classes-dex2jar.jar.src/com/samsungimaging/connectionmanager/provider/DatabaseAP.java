package com.samsungimaging.connectionmanager.provider;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import java.io.Serializable;
import java.util.HashMap;

public class DatabaseAP implements BaseColumns, Serializable {
  public static final int COLUMN_COUNT = 2;
  
  public static final int COLUMN_ID = 0;
  
  public static final int COLUMN_SSID = 1;
  
  protected static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.samsungimaging.connectionmanager.ap";
  
  protected static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.samsungimaging.connectionmanager.ap";
  
  public static final Uri CONTENT_URI = Uri.parse("content://com.samsungimaging.connectionmanager/APList");
  
  protected static final String CREATE_TABLE = "CREATE TABLE APList (_id INTEGER PRIMARY KEY, ssid TEXT, count INTEGER);";
  
  protected static final String DEFAULT_SORT_ORDER = "count DESC";
  
  public static final String KEY_COUNT = "count";
  
  public static final String KEY_SSID = "ssid";
  
  protected static final String[] PROJECTION = new String[] { "_id", "ssid", "count" };
  
  protected static HashMap<String, String> PROJECTION_MAP = new HashMap<String, String>();
  
  protected static final String TABLE_NAME = "APList";
  
  private static final long serialVersionUID = 1L;
  
  private int mCount = 0;
  
  private String mSSID = null;
  
  private long mUriId = -1L;
  
  static {
    PROJECTION_MAP.put("_id", "_id");
    PROJECTION_MAP.put("ssid", "ssid");
    PROJECTION_MAP.put("count", "count");
  }
  
  public DatabaseAP() {}
  
  public DatabaseAP(String paramString) {
    this.mSSID = paramString;
  }
  
  public DatabaseAP(String paramString, int paramInt) {
    this.mSSID = paramString;
    this.mCount = paramInt;
  }
  
  public static DatabaseAP builder(Cursor paramCursor) {
    DatabaseAP databaseAP = new DatabaseAP();
    databaseAP.mUriId = paramCursor.getLong(0);
    databaseAP.mSSID = paramCursor.getString(1);
    databaseAP.mCount = paramCursor.getInt(2);
    return databaseAP;
  }
  
  public int getConnectedCount() {
    return this.mCount;
  }
  
  public ContentValues getContentValues() {
    ContentValues contentValues = new ContentValues();
    contentValues.put("ssid", this.mSSID);
    contentValues.put("count", Integer.valueOf(this.mCount));
    return contentValues;
  }
  
  public String getSSID() {
    return this.mSSID;
  }
  
  public Uri getUri() {
    return (this.mUriId != -1L) ? ContentUris.withAppendedId(CONTENT_URI, this.mUriId) : null;
  }
  
  public void setConnectedCount(int paramInt) {
    this.mCount = paramInt;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("AP");
    stringBuilder.append(" [");
    stringBuilder.append("ssid=" + this.mSSID);
    stringBuilder.append(", count=" + this.mCount);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\provider\DatabaseAP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */