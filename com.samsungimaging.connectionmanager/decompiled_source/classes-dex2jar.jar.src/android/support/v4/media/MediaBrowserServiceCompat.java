package android.support.v4.media;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

public abstract class MediaBrowserServiceCompat extends Service {
  private static final boolean DBG = false;
  
  public static final String KEY_MEDIA_ITEM = "media_item";
  
  public static final String SERVICE_INTERFACE = "android.media.browse.MediaBrowserServiceCompat";
  
  private static final String TAG = "MediaBrowserServiceCompat";
  
  private ServiceBinder mBinder;
  
  private final ArrayMap<IBinder, ConnectionRecord> mConnections = new ArrayMap();
  
  private final Handler mHandler = new Handler();
  
  MediaSessionCompat.Token mSession;
  
  private void addSubscription(String paramString, ConnectionRecord paramConnectionRecord) {
    paramConnectionRecord.subscriptions.add(paramString);
    performLoadChildren(paramString, paramConnectionRecord);
  }
  
  private boolean isValidPackage(String paramString, int paramInt) {
    if (paramString != null) {
      String[] arrayOfString = getPackageManager().getPackagesForUid(paramInt);
      int i = arrayOfString.length;
      paramInt = 0;
      while (true) {
        if (paramInt < i) {
          if (arrayOfString[paramInt].equals(paramString))
            return true; 
          paramInt++;
          continue;
        } 
        return false;
      } 
    } 
    return false;
  }
  
  private void performLoadChildren(final String parentId, final ConnectionRecord connection) {
    Result<List<MediaBrowserCompat.MediaItem>> result = new Result<List<MediaBrowserCompat.MediaItem>>(parentId) {
        void onResultSent(List<MediaBrowserCompat.MediaItem> param1List) {
          if (param1List == null)
            throw new IllegalStateException("onLoadChildren sent null list for id " + parentId); 
          if (MediaBrowserServiceCompat.this.mConnections.get(connection.callbacks.asBinder()) != connection)
            return; 
          try {
            connection.callbacks.onLoadChildren(parentId, param1List);
            return;
          } catch (RemoteException remoteException) {
            Log.w("MediaBrowserServiceCompat", "Calling onLoadChildren() failed for id=" + parentId + " package=" + connection.pkg);
            return;
          } 
        }
      };
    onLoadChildren(parentId, result);
    if (!result.isDone())
      throw new IllegalStateException("onLoadChildren must call detach() or sendResult() before returning for package=" + connection.pkg + " id=" + parentId); 
  }
  
  private void performLoadItem(String paramString, final ResultReceiver receiver) {
    Result<MediaBrowserCompat.MediaItem> result = new Result<MediaBrowserCompat.MediaItem>(paramString) {
        void onResultSent(MediaBrowserCompat.MediaItem param1MediaItem) {
          Bundle bundle = new Bundle();
          bundle.putParcelable("media_item", param1MediaItem);
          receiver.send(0, bundle);
        }
      };
    onLoadItem(paramString, result);
    if (!result.isDone())
      throw new IllegalStateException("onLoadItem must call detach() or sendResult() before returning for id=" + paramString); 
  }
  
  public void dump(FileDescriptor paramFileDescriptor, PrintWriter paramPrintWriter, String[] paramArrayOfString) {}
  
  @Nullable
  public MediaSessionCompat.Token getSessionToken() {
    return this.mSession;
  }
  
  public void notifyChildrenChanged(@NonNull final String parentId) {
    if (parentId == null)
      throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged"); 
    this.mHandler.post(new Runnable() {
          public void run() {
            for (IBinder iBinder : MediaBrowserServiceCompat.this.mConnections.keySet()) {
              MediaBrowserServiceCompat.ConnectionRecord connectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(iBinder);
              if (connectionRecord.subscriptions.contains(parentId))
                MediaBrowserServiceCompat.this.performLoadChildren(parentId, connectionRecord); 
            } 
          }
        });
  }
  
  public IBinder onBind(Intent paramIntent) {
    return (IBinder)("android.media.browse.MediaBrowserServiceCompat".equals(paramIntent.getAction()) ? this.mBinder : null);
  }
  
  public void onCreate() {
    super.onCreate();
    this.mBinder = new ServiceBinder();
  }
  
  @Nullable
  public abstract BrowserRoot onGetRoot(@NonNull String paramString, int paramInt, @Nullable Bundle paramBundle);
  
  public abstract void onLoadChildren(@NonNull String paramString, @NonNull Result<List<MediaBrowserCompat.MediaItem>> paramResult);
  
  public void onLoadItem(String paramString, Result<MediaBrowserCompat.MediaItem> paramResult) {
    paramResult.sendResult(null);
  }
  
  public void setSessionToken(final MediaSessionCompat.Token token) {
    if (token == null)
      throw new IllegalArgumentException("Session token may not be null."); 
    if (this.mSession != null)
      throw new IllegalStateException("The session token has already been set."); 
    this.mSession = token;
    this.mHandler.post(new Runnable() {
          public void run() {
            for (IBinder iBinder : MediaBrowserServiceCompat.this.mConnections.keySet()) {
              MediaBrowserServiceCompat.ConnectionRecord connectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(iBinder);
              try {
                connectionRecord.callbacks.onConnect(connectionRecord.root.getRootId(), token, connectionRecord.root.getExtras());
              } catch (RemoteException remoteException) {
                Log.w("MediaBrowserServiceCompat", "Connection for " + connectionRecord.pkg + " is no longer valid.");
                MediaBrowserServiceCompat.this.mConnections.remove(iBinder);
              } 
            } 
          }
        });
  }
  
  public static final class BrowserRoot {
    private final Bundle mExtras;
    
    private final String mRootId;
    
    public BrowserRoot(@NonNull String param1String, @Nullable Bundle param1Bundle) {
      if (param1String == null)
        throw new IllegalArgumentException("The root id in BrowserRoot cannot be null. Use null for BrowserRoot instead."); 
      this.mRootId = param1String;
      this.mExtras = param1Bundle;
    }
    
    public Bundle getExtras() {
      return this.mExtras;
    }
    
    public String getRootId() {
      return this.mRootId;
    }
  }
  
  private class ConnectionRecord {
    IMediaBrowserServiceCompatCallbacks callbacks;
    
    String pkg;
    
    MediaBrowserServiceCompat.BrowserRoot root;
    
    Bundle rootHints;
    
    HashSet<String> subscriptions = new HashSet<String>();
    
    private ConnectionRecord() {}
  }
  
  public class Result<T> {
    private Object mDebug;
    
    private boolean mDetachCalled;
    
    private boolean mSendResultCalled;
    
    Result(Object param1Object) {
      this.mDebug = param1Object;
    }
    
    public void detach() {
      if (this.mDetachCalled)
        throw new IllegalStateException("detach() called when detach() had already been called for: " + this.mDebug); 
      if (this.mSendResultCalled)
        throw new IllegalStateException("detach() called when sendResult() had already been called for: " + this.mDebug); 
      this.mDetachCalled = true;
    }
    
    boolean isDone() {
      return (this.mDetachCalled || this.mSendResultCalled);
    }
    
    void onResultSent(T param1T) {}
    
    public void sendResult(T param1T) {
      if (this.mSendResultCalled)
        throw new IllegalStateException("sendResult() called twice for: " + this.mDebug); 
      this.mSendResultCalled = true;
      onResultSent(param1T);
    }
  }
  
  private class ServiceBinder extends IMediaBrowserServiceCompat.Stub {
    private ServiceBinder() {}
    
    public void addSubscription(final String id, final IMediaBrowserServiceCompatCallbacks callbacks) {
      MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
            public void run() {
              IBinder iBinder = callbacks.asBinder();
              MediaBrowserServiceCompat.ConnectionRecord connectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(iBinder);
              if (connectionRecord == null) {
                Log.w("MediaBrowserServiceCompat", "addSubscription for callback that isn't registered id=" + id);
                return;
              } 
              MediaBrowserServiceCompat.this.addSubscription(id, connectionRecord);
            }
          });
    }
    
    public void connect(final String pkg, final Bundle rootHints, final IMediaBrowserServiceCompatCallbacks callbacks) {
      final int uid = Binder.getCallingUid();
      if (!MediaBrowserServiceCompat.this.isValidPackage(pkg, i))
        throw new IllegalArgumentException("Package/uid mismatch: uid=" + i + " package=" + pkg); 
      MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
            public void run() {
              IBinder iBinder = callbacks.asBinder();
              MediaBrowserServiceCompat.this.mConnections.remove(iBinder);
              MediaBrowserServiceCompat.ConnectionRecord connectionRecord = new MediaBrowserServiceCompat.ConnectionRecord();
              connectionRecord.pkg = pkg;
              connectionRecord.rootHints = rootHints;
              connectionRecord.callbacks = callbacks;
              connectionRecord.root = MediaBrowserServiceCompat.this.onGetRoot(pkg, uid, rootHints);
              if (connectionRecord.root == null) {
                Log.i("MediaBrowserServiceCompat", "No root for client " + pkg + " from service " + getClass().getName());
                try {
                  callbacks.onConnectFailed();
                  return;
                } catch (RemoteException remoteException) {
                  Log.w("MediaBrowserServiceCompat", "Calling onConnectFailed() failed. Ignoring. pkg=" + pkg);
                  return;
                } 
              } 
              try {
                MediaBrowserServiceCompat.this.mConnections.put(remoteException, connectionRecord);
                if (MediaBrowserServiceCompat.this.mSession != null) {
                  callbacks.onConnect(connectionRecord.root.getRootId(), MediaBrowserServiceCompat.this.mSession, connectionRecord.root.getExtras());
                  return;
                } 
                return;
              } catch (RemoteException remoteException1) {
                Log.w("MediaBrowserServiceCompat", "Calling onConnect() failed. Dropping client. pkg=" + pkg);
                MediaBrowserServiceCompat.this.mConnections.remove(remoteException);
                return;
              } 
            }
          });
    }
    
    public void disconnect(final IMediaBrowserServiceCompatCallbacks callbacks) {
      MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
            public void run() {
              IBinder iBinder = callbacks.asBinder();
              if ((MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.remove(iBinder) != null);
            }
          });
    }
    
    public void getMediaItem(final String mediaId, final ResultReceiver receiver) {
      if (TextUtils.isEmpty(mediaId) || receiver == null)
        return; 
      MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
            public void run() {
              MediaBrowserServiceCompat.this.performLoadItem(mediaId, receiver);
            }
          });
    }
    
    public void removeSubscription(final String id, final IMediaBrowserServiceCompatCallbacks callbacks) {
      MediaBrowserServiceCompat.this.mHandler.post(new Runnable() {
            public void run() {
              IBinder iBinder = callbacks.asBinder();
              MediaBrowserServiceCompat.ConnectionRecord connectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(iBinder);
              if (connectionRecord == null) {
                Log.w("MediaBrowserServiceCompat", "removeSubscription for callback that isn't registered id=" + id);
                return;
              } 
              if (!connectionRecord.subscriptions.remove(id)) {
                Log.w("MediaBrowserServiceCompat", "removeSubscription called for " + id + " which is not subscribed");
                return;
              } 
            }
          });
    }
  }
  
  class null implements Runnable {
    public void run() {
      IBinder iBinder = callbacks.asBinder();
      MediaBrowserServiceCompat.this.mConnections.remove(iBinder);
      MediaBrowserServiceCompat.ConnectionRecord connectionRecord = new MediaBrowserServiceCompat.ConnectionRecord();
      connectionRecord.pkg = pkg;
      connectionRecord.rootHints = rootHints;
      connectionRecord.callbacks = callbacks;
      connectionRecord.root = MediaBrowserServiceCompat.this.onGetRoot(pkg, uid, rootHints);
      if (connectionRecord.root == null) {
        Log.i("MediaBrowserServiceCompat", "No root for client " + pkg + " from service " + getClass().getName());
        try {
          callbacks.onConnectFailed();
          return;
        } catch (RemoteException remoteException) {
          Log.w("MediaBrowserServiceCompat", "Calling onConnectFailed() failed. Ignoring. pkg=" + pkg);
          return;
        } 
      } 
      try {
        MediaBrowserServiceCompat.this.mConnections.put(remoteException, connectionRecord);
        if (MediaBrowserServiceCompat.this.mSession != null) {
          callbacks.onConnect(connectionRecord.root.getRootId(), MediaBrowserServiceCompat.this.mSession, connectionRecord.root.getExtras());
          return;
        } 
        return;
      } catch (RemoteException remoteException1) {
        Log.w("MediaBrowserServiceCompat", "Calling onConnect() failed. Dropping client. pkg=" + pkg);
        MediaBrowserServiceCompat.this.mConnections.remove(remoteException);
        return;
      } 
    }
  }
  
  class null implements Runnable {
    public void run() {
      IBinder iBinder = callbacks.asBinder();
      if ((MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.remove(iBinder) != null);
    }
  }
  
  class null implements Runnable {
    public void run() {
      IBinder iBinder = callbacks.asBinder();
      MediaBrowserServiceCompat.ConnectionRecord connectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(iBinder);
      if (connectionRecord == null) {
        Log.w("MediaBrowserServiceCompat", "addSubscription for callback that isn't registered id=" + id);
        return;
      } 
      MediaBrowserServiceCompat.this.addSubscription(id, connectionRecord);
    }
  }
  
  class null implements Runnable {
    public void run() {
      IBinder iBinder = callbacks.asBinder();
      MediaBrowserServiceCompat.ConnectionRecord connectionRecord = (MediaBrowserServiceCompat.ConnectionRecord)MediaBrowserServiceCompat.this.mConnections.get(iBinder);
      if (connectionRecord == null) {
        Log.w("MediaBrowserServiceCompat", "removeSubscription for callback that isn't registered id=" + id);
        return;
      } 
      if (!connectionRecord.subscriptions.remove(id)) {
        Log.w("MediaBrowserServiceCompat", "removeSubscription called for " + id + " which is not subscribed");
        return;
      } 
    }
  }
  
  class null implements Runnable {
    public void run() {
      MediaBrowserServiceCompat.this.performLoadItem(mediaId, receiver);
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\media\MediaBrowserServiceCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */