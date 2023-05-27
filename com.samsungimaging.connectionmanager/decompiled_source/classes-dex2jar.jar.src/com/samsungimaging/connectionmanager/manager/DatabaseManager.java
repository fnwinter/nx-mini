package com.samsungimaging.connectionmanager.manager;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import com.samsungimaging.connectionmanager.provider.DatabaseAP;
import com.samsungimaging.connectionmanager.provider.DatabaseMedia;
import java.util.ArrayList;

public class DatabaseManager {
  public static void deleteForAP(Context paramContext, Uri paramUri) {
    Uri uri = paramUri;
    if (paramUri == null)
      uri = DatabaseAP.CONTENT_URI; 
    paramContext.getContentResolver().delete(uri, null, null);
  }
  
  public static void deleteForCameraMedia(Context paramContext, Uri paramUri) {
    Uri uri = paramUri;
    if (paramUri == null)
      uri = DatabaseMedia.CONTENT_URI; 
    paramContext.getContentResolver().delete(uri, null, null);
  }
  
  public static void deleteForLocalMedia(Context paramContext, DatabaseMedia paramDatabaseMedia) {
    Uri uri = MediaStore.Files.getContentUri("external");
    String str = paramDatabaseMedia.getOriginalPath();
    paramContext.getContentResolver().delete(uri, "_data=?", new String[] { str });
  }
  
  public static ArrayList<DatabaseAP> fetchAllForAP(Context paramContext) {
    ArrayList<DatabaseAP> arrayList;
    Context context = null;
    Cursor cursor = fetchAllToCursorForAP(paramContext);
    paramContext = context;
    if (cursor != null) {
      paramContext = context;
      if (cursor.getCount() > 0) {
        arrayList = new ArrayList();
        for (int i = 0;; i++) {
          if (i >= cursor.getCount()) {
            cursor.close();
            return arrayList;
          } 
          cursor.moveToPosition(i);
          arrayList.add(DatabaseAP.builder(cursor));
        } 
      } 
    } 
    return arrayList;
  }
  
  public static Cursor fetchAllToCursorForAP(Context paramContext) {
    return paramContext.getContentResolver().query(DatabaseAP.CONTENT_URI, null, null, null, null);
  }
  
  public static DatabaseAP fetchForAP(Context paramContext, String paramString) {
    DatabaseAP databaseAP;
    Cursor cursor = paramContext.getContentResolver().query(DatabaseAP.CONTENT_URI, null, "ssid=?", new String[] { paramString }, null);
    paramContext = null;
    paramString = null;
    if (cursor != null) {
      String str = paramString;
      if (cursor.getCount() > 0) {
        cursor.moveToFirst();
        databaseAP = DatabaseAP.builder(cursor);
      } 
      cursor.close();
    } 
    return databaseAP;
  }
  
  public static int getCountForAP(Context paramContext) {
    byte b = 0;
    Uri uri = Uri.withAppendedPath(DatabaseAP.CONTENT_URI, "count");
    Cursor cursor = paramContext.getContentResolver().query(uri, null, null, null, null);
    int i = b;
    if (cursor != null) {
      i = b;
      if (cursor.getCount() > 0) {
        cursor.moveToFirst();
        i = cursor.getInt(0);
        cursor.close();
      } 
    } 
    return i;
  }
  
  private static void putForAP(Context paramContext, DatabaseAP paramDatabaseAP) {
    Uri uri = paramDatabaseAP.getUri();
    if (uri == null) {
      paramContext.getContentResolver().insert(DatabaseAP.CONTENT_URI, paramDatabaseAP.getContentValues());
      return;
    } 
    paramContext.getContentResolver().update(uri, paramDatabaseAP.getContentValues(), null, null);
  }
  
  public static void putForAP(Context paramContext, String paramString) {
    DatabaseAP databaseAP1;
    DatabaseAP databaseAP2 = fetchForAP(paramContext, paramString);
    if (databaseAP2 == null) {
      databaseAP1 = new DatabaseAP(paramString, 1);
    } else {
      databaseAP2.setConnectedCount(databaseAP2.getConnectedCount() + 1);
      databaseAP1 = databaseAP2;
    } 
    putForAP(paramContext, databaseAP1);
  }
  
  public static void putForCameraMedia(Context paramContext, DatabaseMedia paramDatabaseMedia) {
    Uri uri = paramDatabaseMedia.getUri();
    if (uri == null) {
      paramContext.getContentResolver().insert(DatabaseMedia.CONTENT_URI, paramDatabaseMedia.getContentValues());
      return;
    } 
    paramContext.getContentResolver().update(uri, paramDatabaseMedia.getContentValues(), null, null);
  }
  
  public static void putMaxCountForAP(Context paramContext, String paramString) {
    DatabaseAP databaseAP1;
    Uri uri = Uri.withAppendedPath(DatabaseAP.CONTENT_URI, "max");
    Cursor cursor = paramContext.getContentResolver().query(uri, null, null, null, null);
    int i = 0;
    byte b = 0;
    if (cursor != null) {
      i = b;
      if (cursor.getCount() > 0) {
        cursor.moveToFirst();
        i = cursor.getInt(0) + 1;
      } 
      cursor.close();
    } 
    DatabaseAP databaseAP2 = fetchForAP(paramContext, paramString);
    if (databaseAP2 == null) {
      databaseAP1 = new DatabaseAP(paramString, i);
    } else {
      databaseAP2.setConnectedCount(i);
      databaseAP1 = databaseAP2;
    } 
    putForAP(paramContext, databaseAP1);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\manager\DatabaseManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */