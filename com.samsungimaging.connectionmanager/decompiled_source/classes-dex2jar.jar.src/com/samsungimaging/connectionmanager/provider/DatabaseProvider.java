package com.samsungimaging.connectionmanager.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class DatabaseProvider extends ContentProvider {
  private static final int AP = 0;
  
  private static final int AP_COUNT = 2;
  
  private static final int AP_ID = 1;
  
  private static final int AP_MAX = 3;
  
  protected static final String AUTHORITY = "com.samsungimaging.connectionmanager";
  
  private static final int CAMERA_MEDIA = 10;
  
  private static final int CAMERA_MEDIA_COUNT = 12;
  
  private static final int CAMERA_MEDIA_ID = 11;
  
  private static final String DATABASE_NAME = "SmartCameraApp.db";
  
  private static final int DATABASE_VERSION = 1;
  
  private DatabaseHelper mDatabaseHelper;
  
  private final UriMatcher mUriMatcher = new UriMatcher(-1);
  
  public DatabaseProvider() {
    this.mUriMatcher.addURI("com.samsungimaging.connectionmanager", "APList", 0);
    this.mUriMatcher.addURI("com.samsungimaging.connectionmanager", "APList/#", 1);
    this.mUriMatcher.addURI("com.samsungimaging.connectionmanager", "APList/count", 2);
    this.mUriMatcher.addURI("com.samsungimaging.connectionmanager", "APList/max", 3);
    this.mUriMatcher.addURI("com.samsungimaging.connectionmanager", "CameraMedia", 10);
    this.mUriMatcher.addURI("com.samsungimaging.connectionmanager", "CameraMedia/#", 11);
    this.mUriMatcher.addURI("com.samsungimaging.connectionmanager", "CameraMedia/count", 12);
    this.mDatabaseHelper = null;
  }
  
  public int delete(Uri paramUri, String paramString, String[] paramArrayOfString) {
    SQLiteDatabase sQLiteDatabase = this.mDatabaseHelper.getWritableDatabase();
    switch (this.mUriMatcher.match(paramUri)) {
      default:
        throw new IllegalArgumentException("Unknown URI " + paramUri);
      case 0:
        i = sQLiteDatabase.delete("APList", paramString, paramArrayOfString);
        getContext().getContentResolver().notifyChange(paramUri, null);
        return i;
      case 1:
        stringBuilder = new StringBuilder("_id=" + (String)paramUri.getPathSegments().get(1));
        if (!TextUtils.isEmpty(paramString))
          stringBuilder.append(" AND (" + paramString + ")"); 
        i = sQLiteDatabase.delete("APList", stringBuilder.toString(), paramArrayOfString);
        getContext().getContentResolver().notifyChange(paramUri, null);
        return i;
      case 10:
        i = sQLiteDatabase.delete("CameraMedia", paramString, paramArrayOfString);
        getContext().getContentResolver().notifyChange(paramUri, null);
        return i;
      case 11:
        break;
    } 
    StringBuilder stringBuilder = new StringBuilder("_id=" + (String)paramUri.getPathSegments().get(1));
    if (!TextUtils.isEmpty(paramString))
      stringBuilder.append(" AND (" + paramString + ")"); 
    int i = sQLiteDatabase.delete("CameraMedia", stringBuilder.toString(), paramArrayOfString);
    getContext().getContentResolver().notifyChange(paramUri, null);
    return i;
  }
  
  public String getType(Uri paramUri) {
    switch (this.mUriMatcher.match(paramUri)) {
      default:
        throw new IllegalArgumentException("Unknown URI " + paramUri);
      case 0:
        return "vnd.android.cursor.dir/vnd.com.samsungimaging.connectionmanager.ap";
      case 1:
        return "vnd.android.cursor.item/vnd.com.samsungimaging.connectionmanager.ap";
      case 10:
        return "vnd.android.cursor.dir/vnd.com.samsungimaging.connectionmanager.cameramedia";
      case 11:
        break;
    } 
    return "vnd.android.cursor.item/vnd.com.samsungimaging.connectionmanager.cameramedia";
  }
  
  public Uri insert(Uri paramUri, ContentValues paramContentValues) {
    ContentValues contentValues = paramContentValues;
    if (paramContentValues == null)
      contentValues = new ContentValues(); 
    SQLiteDatabase sQLiteDatabase = this.mDatabaseHelper.getWritableDatabase();
    switch (this.mUriMatcher.match(paramUri)) {
      default:
        throw new IllegalArgumentException("Unknown URI " + paramUri);
      case 0:
        l = sQLiteDatabase.insert("APList", null, contentValues);
        if (l > 0L) {
          paramUri = ContentUris.withAppendedId(DatabaseAP.CONTENT_URI, l);
          getContext().getContentResolver().notifyChange(paramUri, null);
          return paramUri;
        } 
        throw new SQLException("Failed to insert row into " + paramUri);
      case 10:
        break;
    } 
    long l = sQLiteDatabase.insert("CameraMedia", null, contentValues);
    if (l > 0L) {
      paramUri = ContentUris.withAppendedId(DatabaseMedia.CONTENT_URI, l);
      getContext().getContentResolver().notifyChange(paramUri, null);
      return paramUri;
    } 
    throw new SQLException("Failed to insert row into " + paramUri);
  }
  
  public boolean onCreate() {
    this.mDatabaseHelper = new DatabaseHelper(getContext());
    return true;
  }
  
  public Cursor query(Uri paramUri, String[] paramArrayOfString1, String paramString1, String[] paramArrayOfString2, String paramString2) {
    Cursor cursor2;
    StringBuilder stringBuilder2;
    Cursor cursor1;
    String[] arrayOfString3;
    Cursor cursor4;
    String[] arrayOfString2;
    Cursor cursor3;
    String[] arrayOfString1;
    String[] arrayOfString5;
    StringBuilder stringBuilder3;
    String[] arrayOfString4;
    String str;
    SQLiteQueryBuilder sQLiteQueryBuilder = new SQLiteQueryBuilder();
    switch (this.mUriMatcher.match(paramUri)) {
      default:
        throw new IllegalArgumentException("Unknown URI " + paramUri);
      case 0:
        sQLiteQueryBuilder.setTables("APList");
        sQLiteQueryBuilder.setProjectionMap(DatabaseAP.PROJECTION_MAP);
        arrayOfString5 = paramArrayOfString1;
        if (paramArrayOfString1 == null)
          arrayOfString5 = DatabaseAP.PROJECTION; 
        arrayOfString3 = arrayOfString5;
        str = paramString2;
        if (paramString2 == null) {
          str = "count DESC";
          arrayOfString3 = arrayOfString5;
        } 
        cursor2 = sQLiteQueryBuilder.query(this.mDatabaseHelper.getReadableDatabase(), arrayOfString3, paramString1, paramArrayOfString2, null, null, str);
        cursor2.setNotificationUri(getContext().getContentResolver(), paramUri);
        return cursor2;
      case 1:
        sQLiteQueryBuilder.setTables("APList");
        sQLiteQueryBuilder.setProjectionMap(DatabaseAP.PROJECTION_MAP);
        sQLiteQueryBuilder.appendWhere("_id=" + (String)paramUri.getPathSegments().get(1));
        cursor4 = cursor2;
        str = paramString2;
        if (cursor2 == null) {
          arrayOfString2 = DatabaseAP.PROJECTION;
          str = paramString2;
        } 
        cursor2 = sQLiteQueryBuilder.query(this.mDatabaseHelper.getReadableDatabase(), arrayOfString2, paramString1, paramArrayOfString2, null, null, str);
        cursor2.setNotificationUri(getContext().getContentResolver(), paramUri);
        return cursor2;
      case 2:
        sQLiteDatabase = this.mDatabaseHelper.getReadableDatabase();
        stringBuilder2 = new StringBuilder("SELECT count(*) FROM APList");
        if (!TextUtils.isEmpty(paramString1))
          stringBuilder2.append(" WHERE " + paramString1); 
        return sQLiteDatabase.rawQuery(stringBuilder2.toString(), paramArrayOfString2);
      case 3:
        sQLiteDatabase = this.mDatabaseHelper.getReadableDatabase();
        stringBuilder2 = new StringBuilder("SELECT max(count) FROM APList");
        if (!TextUtils.isEmpty(paramString1))
          stringBuilder2.append(" WHERE " + paramString1); 
        return sQLiteDatabase.rawQuery(stringBuilder2.toString(), paramArrayOfString2);
      case 10:
        sQLiteQueryBuilder.setTables("CameraMedia");
        sQLiteQueryBuilder.setProjectionMap(DatabaseMedia.PROJECTION_MAP);
        stringBuilder3 = stringBuilder2;
        if (stringBuilder2 == null)
          arrayOfString4 = DatabaseMedia.PROJECTION; 
        arrayOfString2 = arrayOfString4;
        str = paramString2;
        if (paramString2 == null) {
          str = "datetaken DESC";
          arrayOfString2 = arrayOfString4;
        } 
        cursor1 = sQLiteQueryBuilder.query(this.mDatabaseHelper.getReadableDatabase(), arrayOfString2, paramString1, paramArrayOfString2, null, null, str);
        cursor1.setNotificationUri(getContext().getContentResolver(), (Uri)sQLiteDatabase);
        return cursor1;
      case 11:
        sQLiteQueryBuilder.setTables("CameraMedia");
        sQLiteQueryBuilder.setProjectionMap(DatabaseMedia.PROJECTION_MAP);
        sQLiteQueryBuilder.appendWhere("_id=" + (String)sQLiteDatabase.getPathSegments().get(1));
        cursor3 = cursor1;
        str = paramString2;
        if (cursor1 == null) {
          arrayOfString1 = DatabaseMedia.PROJECTION;
          str = paramString2;
        } 
        cursor1 = sQLiteQueryBuilder.query(this.mDatabaseHelper.getReadableDatabase(), arrayOfString1, paramString1, paramArrayOfString2, null, null, str);
        cursor1.setNotificationUri(getContext().getContentResolver(), (Uri)sQLiteDatabase);
        return cursor1;
      case 12:
        break;
    } 
    SQLiteDatabase sQLiteDatabase = this.mDatabaseHelper.getReadableDatabase();
    StringBuilder stringBuilder1 = new StringBuilder("SELECT count(*) FROM CameraMedia");
    if (!TextUtils.isEmpty(paramString1))
      stringBuilder1.append(" WHERE " + paramString1); 
    return sQLiteDatabase.rawQuery(stringBuilder1.toString(), paramArrayOfString2);
  }
  
  public int update(Uri paramUri, ContentValues paramContentValues, String paramString, String[] paramArrayOfString) {
    SQLiteDatabase sQLiteDatabase = this.mDatabaseHelper.getWritableDatabase();
    switch (this.mUriMatcher.match(paramUri)) {
      default:
        throw new IllegalArgumentException("Unknown URI " + paramUri);
      case 0:
        i = sQLiteDatabase.update("APList", paramContentValues, paramString, paramArrayOfString);
        getContext().getContentResolver().notifyChange(paramUri, null);
        return i;
      case 1:
        stringBuilder = new StringBuilder("_id=" + (String)paramUri.getPathSegments().get(1));
        if (!TextUtils.isEmpty(paramString))
          stringBuilder.append(" AND (" + paramString + ")"); 
        i = sQLiteDatabase.update("APList", paramContentValues, stringBuilder.toString(), paramArrayOfString);
        getContext().getContentResolver().notifyChange(paramUri, null);
        return i;
      case 10:
        i = sQLiteDatabase.update("CameraMedia", paramContentValues, paramString, paramArrayOfString);
        getContext().getContentResolver().notifyChange(paramUri, null);
        return i;
      case 11:
        break;
    } 
    StringBuilder stringBuilder = new StringBuilder("_id=" + (String)paramUri.getPathSegments().get(1));
    if (!TextUtils.isEmpty(paramString))
      stringBuilder.append(" AND (" + paramString + ")"); 
    int i = sQLiteDatabase.update("CameraMedia", paramContentValues, stringBuilder.toString(), paramArrayOfString);
    getContext().getContentResolver().notifyChange(paramUri, null);
    return i;
  }
  
  private class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(Context param1Context) {
      super(param1Context, "SmartCameraApp.db", null, 1);
    }
    
    public void onCreate(SQLiteDatabase param1SQLiteDatabase) {
      param1SQLiteDatabase.execSQL("CREATE TABLE APList (_id INTEGER PRIMARY KEY, ssid TEXT, count INTEGER);");
      param1SQLiteDatabase.execSQL("CREATE TABLE CameraMedia (_id INTEGER PRIMARY KEY, _data TEXT, thumbnail_path TEXT, screen_path TEXT, media_type INTEGER, datetaken INTEGER, orientation INTEGER);");
    }
    
    public void onUpgrade(SQLiteDatabase param1SQLiteDatabase, int param1Int1, int param1Int2) {
      param1SQLiteDatabase.execSQL("DROP TABLE IF EXISTS APList");
      param1SQLiteDatabase.execSQL("DROP TABLE IF EXISTS CameraMedia");
      onCreate(param1SQLiteDatabase);
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\provider\DatabaseProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */