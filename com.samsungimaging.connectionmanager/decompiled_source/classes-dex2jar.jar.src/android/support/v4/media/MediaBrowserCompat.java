package android.support.v4.media;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.os.ResultReceiver;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class MediaBrowserCompat {
  private final MediaBrowserImplBase mImpl;
  
  public MediaBrowserCompat(Context paramContext, ComponentName paramComponentName, ConnectionCallback paramConnectionCallback, Bundle paramBundle) {
    this.mImpl = new MediaBrowserImplBase(paramContext, paramComponentName, paramConnectionCallback, paramBundle);
  }
  
  public void connect() {
    this.mImpl.connect();
  }
  
  public void disconnect() {
    this.mImpl.disconnect();
  }
  
  @Nullable
  public Bundle getExtras() {
    return this.mImpl.getExtras();
  }
  
  public void getItem(@NonNull String paramString, @NonNull ItemCallback paramItemCallback) {
    this.mImpl.getItem(paramString, paramItemCallback);
  }
  
  @NonNull
  public String getRoot() {
    return this.mImpl.getRoot();
  }
  
  @NonNull
  public ComponentName getServiceComponent() {
    return this.mImpl.getServiceComponent();
  }
  
  @NonNull
  public MediaSessionCompat.Token getSessionToken() {
    return this.mImpl.getSessionToken();
  }
  
  public boolean isConnected() {
    return this.mImpl.isConnected();
  }
  
  public void subscribe(@NonNull String paramString, @NonNull SubscriptionCallback paramSubscriptionCallback) {
    this.mImpl.subscribe(paramString, paramSubscriptionCallback);
  }
  
  public void unsubscribe(@NonNull String paramString) {
    this.mImpl.unsubscribe(paramString);
  }
  
  public static class ConnectionCallback {
    public void onConnected() {}
    
    public void onConnectionFailed() {}
    
    public void onConnectionSuspended() {}
  }
  
  public static abstract class ItemCallback {
    public void onError(@NonNull String param1String) {}
    
    public void onItemLoaded(MediaBrowserCompat.MediaItem param1MediaItem) {}
  }
  
  static class MediaBrowserImplBase {
    private static final int CONNECT_STATE_CONNECTED = 2;
    
    private static final int CONNECT_STATE_CONNECTING = 1;
    
    private static final int CONNECT_STATE_DISCONNECTED = 0;
    
    private static final int CONNECT_STATE_SUSPENDED = 3;
    
    private static final boolean DBG = false;
    
    private static final String TAG = "MediaBrowserCompat";
    
    private final MediaBrowserCompat.ConnectionCallback mCallback;
    
    private final Context mContext;
    
    private Bundle mExtras;
    
    private final Handler mHandler = new Handler();
    
    private MediaSessionCompat.Token mMediaSessionToken;
    
    private final Bundle mRootHints;
    
    private String mRootId;
    
    private IMediaBrowserServiceCompat mServiceBinder;
    
    private IMediaBrowserServiceCompatCallbacks mServiceCallbacks;
    
    private final ComponentName mServiceComponent;
    
    private MediaServiceConnection mServiceConnection;
    
    private int mState = 0;
    
    private final ArrayMap<String, Subscription> mSubscriptions = new ArrayMap();
    
    public MediaBrowserImplBase(Context param1Context, ComponentName param1ComponentName, MediaBrowserCompat.ConnectionCallback param1ConnectionCallback, Bundle param1Bundle) {
      if (param1Context == null)
        throw new IllegalArgumentException("context must not be null"); 
      if (param1ComponentName == null)
        throw new IllegalArgumentException("service component must not be null"); 
      if (param1ConnectionCallback == null)
        throw new IllegalArgumentException("connection callback must not be null"); 
      this.mContext = param1Context;
      this.mServiceComponent = param1ComponentName;
      this.mCallback = param1ConnectionCallback;
      this.mRootHints = param1Bundle;
    }
    
    private void forceCloseConnection() {
      if (this.mServiceConnection != null)
        this.mContext.unbindService(this.mServiceConnection); 
      this.mState = 0;
      this.mServiceConnection = null;
      this.mServiceBinder = null;
      this.mServiceCallbacks = null;
      this.mRootId = null;
      this.mMediaSessionToken = null;
    }
    
    private ServiceCallbacks getNewServiceCallbacks() {
      return new ServiceCallbacks(this);
    }
    
    private static String getStateLabel(int param1Int) {
      switch (param1Int) {
        default:
          return "UNKNOWN/" + param1Int;
        case 0:
          return "CONNECT_STATE_DISCONNECTED";
        case 1:
          return "CONNECT_STATE_CONNECTING";
        case 2:
          return "CONNECT_STATE_CONNECTED";
        case 3:
          break;
      } 
      return "CONNECT_STATE_SUSPENDED";
    }
    
    private boolean isCurrent(IMediaBrowserServiceCompatCallbacks param1IMediaBrowserServiceCompatCallbacks, String param1String) {
      if (this.mServiceCallbacks != param1IMediaBrowserServiceCompatCallbacks) {
        if (this.mState != 0)
          Log.i("MediaBrowserCompat", param1String + " for " + this.mServiceComponent + " with mServiceConnection=" + this.mServiceCallbacks + " this=" + this); 
        return false;
      } 
      return true;
    }
    
    private final void onConnectionFailed(final IMediaBrowserServiceCompatCallbacks callback) {
      this.mHandler.post(new Runnable() {
            public void run() {
              Log.e("MediaBrowserCompat", "onConnectFailed for " + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceComponent);
              if (!MediaBrowserCompat.MediaBrowserImplBase.this.isCurrent(callback, "onConnectFailed"))
                return; 
              if (MediaBrowserCompat.MediaBrowserImplBase.this.mState != 1) {
                Log.w("MediaBrowserCompat", "onConnect from service while mState=" + MediaBrowserCompat.MediaBrowserImplBase.getStateLabel(MediaBrowserCompat.MediaBrowserImplBase.this.mState) + "... ignoring");
                return;
              } 
              MediaBrowserCompat.MediaBrowserImplBase.this.forceCloseConnection();
              MediaBrowserCompat.MediaBrowserImplBase.this.mCallback.onConnectionFailed();
            }
          });
    }
    
    private final void onLoadChildren(final IMediaBrowserServiceCompatCallbacks callback, final String parentId, final List list) {
      this.mHandler.post(new Runnable() {
            public void run() {
              if (MediaBrowserCompat.MediaBrowserImplBase.this.isCurrent(callback, "onLoadChildren")) {
                List<?> list2 = list;
                List<?> list1 = list2;
                if (list2 == null)
                  list1 = Collections.emptyList(); 
                MediaBrowserCompat.MediaBrowserImplBase.Subscription subscription = (MediaBrowserCompat.MediaBrowserImplBase.Subscription)MediaBrowserCompat.MediaBrowserImplBase.this.mSubscriptions.get(parentId);
                if (subscription != null) {
                  subscription.callback.onChildrenLoaded(parentId, (List)list1);
                  return;
                } 
              } 
            }
          });
    }
    
    private final void onServiceConnected(final IMediaBrowserServiceCompatCallbacks callback, final String root, final MediaSessionCompat.Token session, final Bundle extra) {
      this.mHandler.post(new Runnable() {
            public void run() {
              if (MediaBrowserCompat.MediaBrowserImplBase.this.isCurrent(callback, "onConnect")) {
                if (MediaBrowserCompat.MediaBrowserImplBase.this.mState != 1) {
                  Log.w("MediaBrowserCompat", "onConnect from service while mState=" + MediaBrowserCompat.MediaBrowserImplBase.getStateLabel(MediaBrowserCompat.MediaBrowserImplBase.this.mState) + "... ignoring");
                  return;
                } 
                MediaBrowserCompat.MediaBrowserImplBase.access$802(MediaBrowserCompat.MediaBrowserImplBase.this, root);
                MediaBrowserCompat.MediaBrowserImplBase.access$902(MediaBrowserCompat.MediaBrowserImplBase.this, session);
                MediaBrowserCompat.MediaBrowserImplBase.access$1002(MediaBrowserCompat.MediaBrowserImplBase.this, extra);
                MediaBrowserCompat.MediaBrowserImplBase.access$602(MediaBrowserCompat.MediaBrowserImplBase.this, 2);
                MediaBrowserCompat.MediaBrowserImplBase.this.mCallback.onConnected();
                Iterator<String> iterator = MediaBrowserCompat.MediaBrowserImplBase.this.mSubscriptions.keySet().iterator();
                while (true) {
                  if (iterator.hasNext()) {
                    String str = iterator.next();
                    try {
                      MediaBrowserCompat.MediaBrowserImplBase.this.mServiceBinder.addSubscription(str, MediaBrowserCompat.MediaBrowserImplBase.this.mServiceCallbacks);
                    } catch (RemoteException remoteException) {
                      Log.d("MediaBrowserCompat", "addSubscription failed with RemoteException parentId=" + str);
                    } 
                    continue;
                  } 
                  return;
                } 
              } 
            }
          });
    }
    
    public void connect() {
      if (this.mState != 0)
        throw new IllegalStateException("connect() called while not disconnected (state=" + getStateLabel(this.mState) + ")"); 
      if (this.mServiceBinder != null)
        throw new RuntimeException("mServiceBinder should be null. Instead it is " + this.mServiceBinder); 
      if (this.mServiceCallbacks != null)
        throw new RuntimeException("mServiceCallbacks should be null. Instead it is " + this.mServiceCallbacks); 
      this.mState = 1;
      Intent intent = new Intent("android.media.browse.MediaBrowserServiceCompat");
      intent.setComponent(this.mServiceComponent);
      final MediaServiceConnection thisConnection = new MediaServiceConnection();
      this.mServiceConnection = mediaServiceConnection;
      boolean bool = false;
      try {
        boolean bool1 = this.mContext.bindService(intent, this.mServiceConnection, 1);
        bool = bool1;
      } catch (Exception exception) {
        Log.e("MediaBrowserCompat", "Failed binding to service " + this.mServiceComponent);
      } 
      if (!bool)
        this.mHandler.post(new Runnable() {
              public void run() {
                if (thisConnection == MediaBrowserCompat.MediaBrowserImplBase.this.mServiceConnection) {
                  MediaBrowserCompat.MediaBrowserImplBase.this.forceCloseConnection();
                  MediaBrowserCompat.MediaBrowserImplBase.this.mCallback.onConnectionFailed();
                } 
              }
            }); 
    }
    
    public void disconnect() {
      if (this.mServiceCallbacks != null)
        try {
          this.mServiceBinder.disconnect(this.mServiceCallbacks);
        } catch (RemoteException remoteException) {
          Log.w("MediaBrowserCompat", "RemoteException during connect for " + this.mServiceComponent);
        }  
      forceCloseConnection();
    }
    
    void dump() {
      Log.d("MediaBrowserCompat", "MediaBrowserCompat...");
      Log.d("MediaBrowserCompat", "  mServiceComponent=" + this.mServiceComponent);
      Log.d("MediaBrowserCompat", "  mCallback=" + this.mCallback);
      Log.d("MediaBrowserCompat", "  mRootHints=" + this.mRootHints);
      Log.d("MediaBrowserCompat", "  mState=" + getStateLabel(this.mState));
      Log.d("MediaBrowserCompat", "  mServiceConnection=" + this.mServiceConnection);
      Log.d("MediaBrowserCompat", "  mServiceBinder=" + this.mServiceBinder);
      Log.d("MediaBrowserCompat", "  mServiceCallbacks=" + this.mServiceCallbacks);
      Log.d("MediaBrowserCompat", "  mRootId=" + this.mRootId);
      Log.d("MediaBrowserCompat", "  mMediaSessionToken=" + this.mMediaSessionToken);
    }
    
    @Nullable
    public Bundle getExtras() {
      if (!isConnected())
        throw new IllegalStateException("getExtras() called while not connected (state=" + getStateLabel(this.mState) + ")"); 
      return this.mExtras;
    }
    
    public void getItem(@NonNull final String mediaId, @NonNull final MediaBrowserCompat.ItemCallback cb) {
      if (TextUtils.isEmpty(mediaId))
        throw new IllegalArgumentException("mediaId is empty."); 
      if (cb == null)
        throw new IllegalArgumentException("cb is null."); 
      if (this.mState != 2) {
        Log.i("MediaBrowserCompat", "Not connected, unable to retrieve the MediaItem.");
        this.mHandler.post(new Runnable() {
              public void run() {
                cb.onError(mediaId);
              }
            });
        return;
      } 
      ResultReceiver resultReceiver = new ResultReceiver(this.mHandler) {
          protected void onReceiveResult(int param2Int, Bundle param2Bundle) {
            if (param2Int != 0 || param2Bundle == null || !param2Bundle.containsKey("media_item")) {
              cb.onError(mediaId);
              return;
            } 
            Parcelable parcelable = param2Bundle.getParcelable("media_item");
            if (!(parcelable instanceof MediaBrowserCompat.MediaItem)) {
              cb.onError(mediaId);
              return;
            } 
            cb.onItemLoaded((MediaBrowserCompat.MediaItem)parcelable);
          }
        };
      try {
        this.mServiceBinder.getMediaItem(mediaId, resultReceiver);
        return;
      } catch (RemoteException remoteException) {
        Log.i("MediaBrowserCompat", "Remote error getting media item.");
        this.mHandler.post(new Runnable() {
              public void run() {
                cb.onError(mediaId);
              }
            });
        return;
      } 
    }
    
    @NonNull
    public String getRoot() {
      if (!isConnected())
        throw new IllegalStateException("getSessionToken() called while not connected(state=" + getStateLabel(this.mState) + ")"); 
      return this.mRootId;
    }
    
    @NonNull
    public ComponentName getServiceComponent() {
      if (!isConnected())
        throw new IllegalStateException("getServiceComponent() called while not connected (state=" + this.mState + ")"); 
      return this.mServiceComponent;
    }
    
    @NonNull
    public MediaSessionCompat.Token getSessionToken() {
      if (!isConnected())
        throw new IllegalStateException("getSessionToken() called while not connected(state=" + this.mState + ")"); 
      return this.mMediaSessionToken;
    }
    
    public boolean isConnected() {
      return (this.mState == 2);
    }
    
    public void subscribe(@NonNull String param1String, @NonNull MediaBrowserCompat.SubscriptionCallback param1SubscriptionCallback) {
      boolean bool;
      if (param1String == null)
        throw new IllegalArgumentException("parentId is null"); 
      if (param1SubscriptionCallback == null)
        throw new IllegalArgumentException("callback is null"); 
      Subscription subscription = (Subscription)this.mSubscriptions.get(param1String);
      if (subscription == null) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool) {
        subscription = new Subscription(param1String);
        this.mSubscriptions.put(param1String, subscription);
      } 
      subscription.callback = param1SubscriptionCallback;
      if (this.mState == 2)
        try {
          this.mServiceBinder.addSubscription(param1String, this.mServiceCallbacks);
          return;
        } catch (RemoteException remoteException) {
          Log.d("MediaBrowserCompat", "addSubscription failed with RemoteException parentId=" + param1String);
          return;
        }  
    }
    
    public void unsubscribe(@NonNull String param1String) {
      if (TextUtils.isEmpty(param1String))
        throw new IllegalArgumentException("parentId is empty."); 
      Subscription subscription = (Subscription)this.mSubscriptions.remove(param1String);
      if (this.mState == 2 && subscription != null)
        try {
          this.mServiceBinder.removeSubscription(param1String, this.mServiceCallbacks);
          return;
        } catch (RemoteException remoteException) {
          Log.d("MediaBrowserCompat", "removeSubscription failed with RemoteException parentId=" + param1String);
          return;
        }  
    }
    
    private class MediaServiceConnection implements ServiceConnection {
      private MediaServiceConnection() {}
      
      private boolean isCurrent(String param2String) {
        if (MediaBrowserCompat.MediaBrowserImplBase.this.mServiceConnection != this) {
          if (MediaBrowserCompat.MediaBrowserImplBase.this.mState != 0)
            Log.i("MediaBrowserCompat", param2String + " for " + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceComponent + " with mServiceConnection=" + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceConnection + " this=" + this); 
          return false;
        } 
        return true;
      }
      
      public void onServiceConnected(ComponentName param2ComponentName, IBinder param2IBinder) {
        if (!isCurrent("onServiceConnected"))
          return; 
        MediaBrowserCompat.MediaBrowserImplBase.access$1302(MediaBrowserCompat.MediaBrowserImplBase.this, IMediaBrowserServiceCompat.Stub.asInterface(param2IBinder));
        MediaBrowserCompat.MediaBrowserImplBase.access$1202(MediaBrowserCompat.MediaBrowserImplBase.this, MediaBrowserCompat.MediaBrowserImplBase.this.getNewServiceCallbacks());
        MediaBrowserCompat.MediaBrowserImplBase.access$602(MediaBrowserCompat.MediaBrowserImplBase.this, 1);
        try {
          MediaBrowserCompat.MediaBrowserImplBase.this.mServiceBinder.connect(MediaBrowserCompat.MediaBrowserImplBase.this.mContext.getPackageName(), MediaBrowserCompat.MediaBrowserImplBase.this.mRootHints, MediaBrowserCompat.MediaBrowserImplBase.this.mServiceCallbacks);
          return;
        } catch (RemoteException remoteException) {
          Log.w("MediaBrowserCompat", "RemoteException during connect for " + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceComponent);
          return;
        } 
      }
      
      public void onServiceDisconnected(ComponentName param2ComponentName) {
        if (!isCurrent("onServiceDisconnected"))
          return; 
        MediaBrowserCompat.MediaBrowserImplBase.access$1302(MediaBrowserCompat.MediaBrowserImplBase.this, null);
        MediaBrowserCompat.MediaBrowserImplBase.access$1202(MediaBrowserCompat.MediaBrowserImplBase.this, null);
        MediaBrowserCompat.MediaBrowserImplBase.access$602(MediaBrowserCompat.MediaBrowserImplBase.this, 3);
        MediaBrowserCompat.MediaBrowserImplBase.this.mCallback.onConnectionSuspended();
      }
    }
    
    private static class ServiceCallbacks extends IMediaBrowserServiceCompatCallbacks.Stub {
      private WeakReference<MediaBrowserCompat.MediaBrowserImplBase> mMediaBrowser;
      
      public ServiceCallbacks(MediaBrowserCompat.MediaBrowserImplBase param2MediaBrowserImplBase) {
        this.mMediaBrowser = new WeakReference<MediaBrowserCompat.MediaBrowserImplBase>(param2MediaBrowserImplBase);
      }
      
      public void onConnect(String param2String, MediaSessionCompat.Token param2Token, Bundle param2Bundle) {
        MediaBrowserCompat.MediaBrowserImplBase mediaBrowserImplBase = this.mMediaBrowser.get();
        if (mediaBrowserImplBase != null)
          mediaBrowserImplBase.onServiceConnected(this, param2String, param2Token, param2Bundle); 
      }
      
      public void onConnectFailed() {
        MediaBrowserCompat.MediaBrowserImplBase mediaBrowserImplBase = this.mMediaBrowser.get();
        if (mediaBrowserImplBase != null)
          mediaBrowserImplBase.onConnectionFailed(this); 
      }
      
      public void onLoadChildren(String param2String, List param2List) {
        MediaBrowserCompat.MediaBrowserImplBase mediaBrowserImplBase = this.mMediaBrowser.get();
        if (mediaBrowserImplBase != null)
          mediaBrowserImplBase.onLoadChildren(this, param2String, param2List); 
      }
    }
    
    private static class Subscription {
      MediaBrowserCompat.SubscriptionCallback callback;
      
      final String id;
      
      Subscription(String param2String) {
        this.id = param2String;
      }
    }
  }
  
  class null implements Runnable {
    public void run() {
      if (thisConnection == this.this$0.mServiceConnection) {
        this.this$0.forceCloseConnection();
        this.this$0.mCallback.onConnectionFailed();
      } 
    }
  }
  
  class null implements Runnable {
    public void run() {
      cb.onError(mediaId);
    }
  }
  
  class null extends ResultReceiver {
    null(Handler param1Handler) {
      super(param1Handler);
    }
    
    protected void onReceiveResult(int param1Int, Bundle param1Bundle) {
      if (param1Int != 0 || param1Bundle == null || !param1Bundle.containsKey("media_item")) {
        cb.onError(mediaId);
        return;
      } 
      Parcelable parcelable = param1Bundle.getParcelable("media_item");
      if (!(parcelable instanceof MediaBrowserCompat.MediaItem)) {
        cb.onError(mediaId);
        return;
      } 
      cb.onItemLoaded((MediaBrowserCompat.MediaItem)parcelable);
    }
  }
  
  class null implements Runnable {
    public void run() {
      cb.onError(mediaId);
    }
  }
  
  class null implements Runnable {
    public void run() {
      if (this.this$0.isCurrent(callback, "onConnect")) {
        if (this.this$0.mState != 1) {
          Log.w("MediaBrowserCompat", "onConnect from service while mState=" + MediaBrowserCompat.MediaBrowserImplBase.getStateLabel(this.this$0.mState) + "... ignoring");
          return;
        } 
        MediaBrowserCompat.MediaBrowserImplBase.access$802(this.this$0, root);
        MediaBrowserCompat.MediaBrowserImplBase.access$902(this.this$0, session);
        MediaBrowserCompat.MediaBrowserImplBase.access$1002(this.this$0, extra);
        MediaBrowserCompat.MediaBrowserImplBase.access$602(this.this$0, 2);
        this.this$0.mCallback.onConnected();
        Iterator<String> iterator = this.this$0.mSubscriptions.keySet().iterator();
        while (true) {
          if (iterator.hasNext()) {
            String str = iterator.next();
            try {
              this.this$0.mServiceBinder.addSubscription(str, this.this$0.mServiceCallbacks);
            } catch (RemoteException remoteException) {
              Log.d("MediaBrowserCompat", "addSubscription failed with RemoteException parentId=" + str);
            } 
            continue;
          } 
          return;
        } 
      } 
    }
  }
  
  class null implements Runnable {
    public void run() {
      Log.e("MediaBrowserCompat", "onConnectFailed for " + this.this$0.mServiceComponent);
      if (!this.this$0.isCurrent(callback, "onConnectFailed"))
        return; 
      if (this.this$0.mState != 1) {
        Log.w("MediaBrowserCompat", "onConnect from service while mState=" + MediaBrowserCompat.MediaBrowserImplBase.getStateLabel(this.this$0.mState) + "... ignoring");
        return;
      } 
      this.this$0.forceCloseConnection();
      this.this$0.mCallback.onConnectionFailed();
    }
  }
  
  class null implements Runnable {
    public void run() {
      if (this.this$0.isCurrent(callback, "onLoadChildren")) {
        List<?> list2 = list;
        List<?> list1 = list2;
        if (list2 == null)
          list1 = Collections.emptyList(); 
        MediaBrowserCompat.MediaBrowserImplBase.Subscription subscription = (MediaBrowserCompat.MediaBrowserImplBase.Subscription)this.this$0.mSubscriptions.get(parentId);
        if (subscription != null) {
          subscription.callback.onChildrenLoaded(parentId, (List)list1);
          return;
        } 
      } 
    }
  }
  
  private class MediaServiceConnection implements ServiceConnection {
    private MediaServiceConnection() {}
    
    private boolean isCurrent(String param1String) {
      if (MediaBrowserCompat.MediaBrowserImplBase.this.mServiceConnection != this) {
        if (MediaBrowserCompat.MediaBrowserImplBase.this.mState != 0)
          Log.i("MediaBrowserCompat", param1String + " for " + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceComponent + " with mServiceConnection=" + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceConnection + " this=" + this); 
        return false;
      } 
      return true;
    }
    
    public void onServiceConnected(ComponentName param1ComponentName, IBinder param1IBinder) {
      if (!isCurrent("onServiceConnected"))
        return; 
      MediaBrowserCompat.MediaBrowserImplBase.access$1302(MediaBrowserCompat.MediaBrowserImplBase.this, IMediaBrowserServiceCompat.Stub.asInterface(param1IBinder));
      MediaBrowserCompat.MediaBrowserImplBase.access$1202(MediaBrowserCompat.MediaBrowserImplBase.this, MediaBrowserCompat.MediaBrowserImplBase.this.getNewServiceCallbacks());
      MediaBrowserCompat.MediaBrowserImplBase.access$602(MediaBrowserCompat.MediaBrowserImplBase.this, 1);
      try {
        MediaBrowserCompat.MediaBrowserImplBase.this.mServiceBinder.connect(MediaBrowserCompat.MediaBrowserImplBase.this.mContext.getPackageName(), MediaBrowserCompat.MediaBrowserImplBase.this.mRootHints, MediaBrowserCompat.MediaBrowserImplBase.this.mServiceCallbacks);
        return;
      } catch (RemoteException remoteException) {
        Log.w("MediaBrowserCompat", "RemoteException during connect for " + MediaBrowserCompat.MediaBrowserImplBase.this.mServiceComponent);
        return;
      } 
    }
    
    public void onServiceDisconnected(ComponentName param1ComponentName) {
      if (!isCurrent("onServiceDisconnected"))
        return; 
      MediaBrowserCompat.MediaBrowserImplBase.access$1302(MediaBrowserCompat.MediaBrowserImplBase.this, null);
      MediaBrowserCompat.MediaBrowserImplBase.access$1202(MediaBrowserCompat.MediaBrowserImplBase.this, null);
      MediaBrowserCompat.MediaBrowserImplBase.access$602(MediaBrowserCompat.MediaBrowserImplBase.this, 3);
      MediaBrowserCompat.MediaBrowserImplBase.this.mCallback.onConnectionSuspended();
    }
  }
  
  private static class ServiceCallbacks extends IMediaBrowserServiceCompatCallbacks.Stub {
    private WeakReference<MediaBrowserCompat.MediaBrowserImplBase> mMediaBrowser;
    
    public ServiceCallbacks(MediaBrowserCompat.MediaBrowserImplBase param1MediaBrowserImplBase) {
      this.mMediaBrowser = new WeakReference<MediaBrowserCompat.MediaBrowserImplBase>(param1MediaBrowserImplBase);
    }
    
    public void onConnect(String param1String, MediaSessionCompat.Token param1Token, Bundle param1Bundle) {
      MediaBrowserCompat.MediaBrowserImplBase mediaBrowserImplBase = this.mMediaBrowser.get();
      if (mediaBrowserImplBase != null)
        mediaBrowserImplBase.onServiceConnected(this, param1String, param1Token, param1Bundle); 
    }
    
    public void onConnectFailed() {
      MediaBrowserCompat.MediaBrowserImplBase mediaBrowserImplBase = this.mMediaBrowser.get();
      if (mediaBrowserImplBase != null)
        mediaBrowserImplBase.onConnectionFailed(this); 
    }
    
    public void onLoadChildren(String param1String, List param1List) {
      MediaBrowserCompat.MediaBrowserImplBase mediaBrowserImplBase = this.mMediaBrowser.get();
      if (mediaBrowserImplBase != null)
        mediaBrowserImplBase.onLoadChildren(this, param1String, param1List); 
    }
  }
  
  private static class Subscription {
    MediaBrowserCompat.SubscriptionCallback callback;
    
    final String id;
    
    Subscription(String param1String) {
      this.id = param1String;
    }
  }
  
  public static class MediaItem implements Parcelable {
    public static final Parcelable.Creator<MediaItem> CREATOR = new Parcelable.Creator<MediaItem>() {
        public MediaBrowserCompat.MediaItem createFromParcel(Parcel param2Parcel) {
          return new MediaBrowserCompat.MediaItem(param2Parcel);
        }
        
        public MediaBrowserCompat.MediaItem[] newArray(int param2Int) {
          return new MediaBrowserCompat.MediaItem[param2Int];
        }
      };
    
    public static final int FLAG_BROWSABLE = 1;
    
    public static final int FLAG_PLAYABLE = 2;
    
    private final MediaDescriptionCompat mDescription;
    
    private final int mFlags;
    
    private MediaItem(Parcel param1Parcel) {
      this.mFlags = param1Parcel.readInt();
      this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(param1Parcel);
    }
    
    public MediaItem(@NonNull MediaDescriptionCompat param1MediaDescriptionCompat, int param1Int) {
      if (param1MediaDescriptionCompat == null)
        throw new IllegalArgumentException("description cannot be null"); 
      if (TextUtils.isEmpty(param1MediaDescriptionCompat.getMediaId()))
        throw new IllegalArgumentException("description must have a non-empty media id"); 
      this.mFlags = param1Int;
      this.mDescription = param1MediaDescriptionCompat;
    }
    
    public int describeContents() {
      return 0;
    }
    
    @NonNull
    public MediaDescriptionCompat getDescription() {
      return this.mDescription;
    }
    
    public int getFlags() {
      return this.mFlags;
    }
    
    @NonNull
    public String getMediaId() {
      return this.mDescription.getMediaId();
    }
    
    public boolean isBrowsable() {
      return ((this.mFlags & 0x1) != 0);
    }
    
    public boolean isPlayable() {
      return ((this.mFlags & 0x2) != 0);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder("MediaItem{");
      stringBuilder.append("mFlags=").append(this.mFlags);
      stringBuilder.append(", mDescription=").append(this.mDescription);
      stringBuilder.append('}');
      return stringBuilder.toString();
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      param1Parcel.writeInt(this.mFlags);
      this.mDescription.writeToParcel(param1Parcel, param1Int);
    }
    
    @Retention(RetentionPolicy.SOURCE)
    public static @interface Flags {}
  }
  
  static final class null implements Parcelable.Creator<MediaItem> {
    public MediaBrowserCompat.MediaItem createFromParcel(Parcel param1Parcel) {
      return new MediaBrowserCompat.MediaItem(param1Parcel);
    }
    
    public MediaBrowserCompat.MediaItem[] newArray(int param1Int) {
      return new MediaBrowserCompat.MediaItem[param1Int];
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface Flags {}
  
  public static abstract class SubscriptionCallback {
    public void onChildrenLoaded(@NonNull String param1String, @NonNull List<MediaBrowserCompat.MediaItem> param1List) {}
    
    public void onError(@NonNull String param1String) {}
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\media\MediaBrowserCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */