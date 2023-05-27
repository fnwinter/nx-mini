package android.support.v4.media.session;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.support.v4.media.VolumeProviderCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MediaSessionCompat {
  public static final int FLAG_HANDLES_MEDIA_BUTTONS = 1;
  
  public static final int FLAG_HANDLES_TRANSPORT_CONTROLS = 2;
  
  private static final String TAG = "MediaSessionCompat";
  
  private final ArrayList<OnActiveChangeListener> mActiveListeners;
  
  private final MediaControllerCompat mController;
  
  private final MediaSessionImpl mImpl;
  
  private MediaSessionCompat(Context paramContext, MediaSessionImpl paramMediaSessionImpl) {
    this.mActiveListeners = new ArrayList<OnActiveChangeListener>();
    this.mImpl = paramMediaSessionImpl;
    this.mController = new MediaControllerCompat(paramContext, this);
  }
  
  public MediaSessionCompat(Context paramContext, String paramString) {
    this(paramContext, paramString, null, null);
  }
  
  public MediaSessionCompat(Context paramContext, String paramString, ComponentName paramComponentName, PendingIntent paramPendingIntent) {
    ResolveInfo resolveInfo;
    this.mActiveListeners = new ArrayList<OnActiveChangeListener>();
    if (paramContext == null)
      throw new IllegalArgumentException("context must not be null"); 
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("tag must not be null or empty"); 
    ComponentName componentName = paramComponentName;
    if (paramComponentName == null) {
      ResolveInfo resolveInfo1;
      Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
      intent.setPackage(paramContext.getPackageName());
      List<ResolveInfo> list = paramContext.getPackageManager().queryBroadcastReceivers(intent, 0);
      if (list.size() == 1) {
        resolveInfo1 = list.get(0);
        ComponentName componentName1 = new ComponentName(resolveInfo1.activityInfo.packageName, resolveInfo1.activityInfo.name);
      } else {
        resolveInfo = resolveInfo1;
        if (list.size() > 1) {
          Log.w("MediaSessionCompat", "More than one BroadcastReceiver that handles android.intent.action.MEDIA_BUTTON was found, using null. Provide a specific ComponentName to use as this session's media button receiver");
          resolveInfo = resolveInfo1;
        } 
      } 
    } 
    PendingIntent pendingIntent = paramPendingIntent;
    if (resolveInfo != null) {
      pendingIntent = paramPendingIntent;
      if (paramPendingIntent == null) {
        Intent intent = new Intent("android.intent.action.MEDIA_BUTTON");
        intent.setComponent((ComponentName)resolveInfo);
        pendingIntent = PendingIntent.getBroadcast(paramContext, 0, intent, 0);
      } 
    } 
    if (Build.VERSION.SDK_INT >= 21) {
      this.mImpl = new MediaSessionImplApi21(paramContext, paramString);
      this.mImpl.setMediaButtonReceiver(pendingIntent);
    } else {
      this.mImpl = new MediaSessionImplBase(paramContext, paramString, (ComponentName)resolveInfo, pendingIntent);
    } 
    this.mController = new MediaControllerCompat(paramContext, this);
  }
  
  public static MediaSessionCompat obtain(Context paramContext, Object paramObject) {
    return new MediaSessionCompat(paramContext, new MediaSessionImplApi21(paramObject));
  }
  
  public void addOnActiveChangeListener(OnActiveChangeListener paramOnActiveChangeListener) {
    if (paramOnActiveChangeListener == null)
      throw new IllegalArgumentException("Listener may not be null"); 
    this.mActiveListeners.add(paramOnActiveChangeListener);
  }
  
  public MediaControllerCompat getController() {
    return this.mController;
  }
  
  public Object getMediaSession() {
    return this.mImpl.getMediaSession();
  }
  
  public Object getRemoteControlClient() {
    return this.mImpl.getRemoteControlClient();
  }
  
  public Token getSessionToken() {
    return this.mImpl.getSessionToken();
  }
  
  public boolean isActive() {
    return this.mImpl.isActive();
  }
  
  public void release() {
    this.mImpl.release();
  }
  
  public void removeOnActiveChangeListener(OnActiveChangeListener paramOnActiveChangeListener) {
    if (paramOnActiveChangeListener == null)
      throw new IllegalArgumentException("Listener may not be null"); 
    this.mActiveListeners.remove(paramOnActiveChangeListener);
  }
  
  public void sendSessionEvent(String paramString, Bundle paramBundle) {
    if (TextUtils.isEmpty(paramString))
      throw new IllegalArgumentException("event cannot be null or empty"); 
    this.mImpl.sendSessionEvent(paramString, paramBundle);
  }
  
  public void setActive(boolean paramBoolean) {
    this.mImpl.setActive(paramBoolean);
    Iterator<OnActiveChangeListener> iterator = this.mActiveListeners.iterator();
    while (iterator.hasNext())
      ((OnActiveChangeListener)iterator.next()).onActiveChanged(); 
  }
  
  public void setCallback(Callback paramCallback) {
    setCallback(paramCallback, null);
  }
  
  public void setCallback(Callback paramCallback, Handler paramHandler) {
    MediaSessionImpl mediaSessionImpl = this.mImpl;
    if (paramHandler == null)
      paramHandler = new Handler(); 
    mediaSessionImpl.setCallback(paramCallback, paramHandler);
  }
  
  public void setExtras(Bundle paramBundle) {
    this.mImpl.setExtras(paramBundle);
  }
  
  public void setFlags(int paramInt) {
    this.mImpl.setFlags(paramInt);
  }
  
  public void setMediaButtonReceiver(PendingIntent paramPendingIntent) {
    this.mImpl.setMediaButtonReceiver(paramPendingIntent);
  }
  
  public void setMetadata(MediaMetadataCompat paramMediaMetadataCompat) {
    this.mImpl.setMetadata(paramMediaMetadataCompat);
  }
  
  public void setPlaybackState(PlaybackStateCompat paramPlaybackStateCompat) {
    this.mImpl.setPlaybackState(paramPlaybackStateCompat);
  }
  
  public void setPlaybackToLocal(int paramInt) {
    this.mImpl.setPlaybackToLocal(paramInt);
  }
  
  public void setPlaybackToRemote(VolumeProviderCompat paramVolumeProviderCompat) {
    if (paramVolumeProviderCompat == null)
      throw new IllegalArgumentException("volumeProvider may not be null!"); 
    this.mImpl.setPlaybackToRemote(paramVolumeProviderCompat);
  }
  
  public void setQueue(List<QueueItem> paramList) {
    this.mImpl.setQueue(paramList);
  }
  
  public void setQueueTitle(CharSequence paramCharSequence) {
    this.mImpl.setQueueTitle(paramCharSequence);
  }
  
  public void setRatingType(int paramInt) {
    this.mImpl.setRatingType(paramInt);
  }
  
  public void setSessionActivity(PendingIntent paramPendingIntent) {
    this.mImpl.setSessionActivity(paramPendingIntent);
  }
  
  public static abstract class Callback {
    final Object mCallbackObj;
    
    public Callback() {
      if (Build.VERSION.SDK_INT >= 23) {
        this.mCallbackObj = MediaSessionCompatApi23.createCallback(new StubApi23());
        return;
      } 
      if (Build.VERSION.SDK_INT >= 21) {
        this.mCallbackObj = MediaSessionCompatApi21.createCallback(new StubApi21());
        return;
      } 
      this.mCallbackObj = null;
    }
    
    public void onCommand(String param1String, Bundle param1Bundle, ResultReceiver param1ResultReceiver) {}
    
    public void onCustomAction(String param1String, Bundle param1Bundle) {}
    
    public void onFastForward() {}
    
    public boolean onMediaButtonEvent(Intent param1Intent) {
      return false;
    }
    
    public void onPause() {}
    
    public void onPlay() {}
    
    public void onPlayFromMediaId(String param1String, Bundle param1Bundle) {}
    
    public void onPlayFromSearch(String param1String, Bundle param1Bundle) {}
    
    public void onPlayFromUri(Uri param1Uri, Bundle param1Bundle) {}
    
    public void onRewind() {}
    
    public void onSeekTo(long param1Long) {}
    
    public void onSetRating(RatingCompat param1RatingCompat) {}
    
    public void onSkipToNext() {}
    
    public void onSkipToPrevious() {}
    
    public void onSkipToQueueItem(long param1Long) {}
    
    public void onStop() {}
    
    private class StubApi21 implements MediaSessionCompatApi21.Callback {
      private StubApi21() {}
      
      public void onCommand(String param2String, Bundle param2Bundle, ResultReceiver param2ResultReceiver) {
        MediaSessionCompat.Callback.this.onCommand(param2String, param2Bundle, param2ResultReceiver);
      }
      
      public void onCustomAction(String param2String, Bundle param2Bundle) {
        MediaSessionCompat.Callback.this.onCustomAction(param2String, param2Bundle);
      }
      
      public void onFastForward() {
        MediaSessionCompat.Callback.this.onFastForward();
      }
      
      public boolean onMediaButtonEvent(Intent param2Intent) {
        return MediaSessionCompat.Callback.this.onMediaButtonEvent(param2Intent);
      }
      
      public void onPause() {
        MediaSessionCompat.Callback.this.onPause();
      }
      
      public void onPlay() {
        MediaSessionCompat.Callback.this.onPlay();
      }
      
      public void onPlayFromMediaId(String param2String, Bundle param2Bundle) {
        MediaSessionCompat.Callback.this.onPlayFromMediaId(param2String, param2Bundle);
      }
      
      public void onPlayFromSearch(String param2String, Bundle param2Bundle) {
        MediaSessionCompat.Callback.this.onPlayFromSearch(param2String, param2Bundle);
      }
      
      public void onRewind() {
        MediaSessionCompat.Callback.this.onRewind();
      }
      
      public void onSeekTo(long param2Long) {
        MediaSessionCompat.Callback.this.onSeekTo(param2Long);
      }
      
      public void onSetRating(Object param2Object) {
        MediaSessionCompat.Callback.this.onSetRating(RatingCompat.fromRating(param2Object));
      }
      
      public void onSkipToNext() {
        MediaSessionCompat.Callback.this.onSkipToNext();
      }
      
      public void onSkipToPrevious() {
        MediaSessionCompat.Callback.this.onSkipToPrevious();
      }
      
      public void onSkipToQueueItem(long param2Long) {
        MediaSessionCompat.Callback.this.onSkipToQueueItem(param2Long);
      }
      
      public void onStop() {
        MediaSessionCompat.Callback.this.onStop();
      }
    }
    
    private class StubApi23 extends StubApi21 implements MediaSessionCompatApi23.Callback {
      private StubApi23() {}
      
      public void onPlayFromUri(Uri param2Uri, Bundle param2Bundle) {
        MediaSessionCompat.Callback.this.onPlayFromUri(param2Uri, param2Bundle);
      }
    }
  }
  
  private class StubApi21 implements MediaSessionCompatApi21.Callback {
    private StubApi21() {}
    
    public void onCommand(String param1String, Bundle param1Bundle, ResultReceiver param1ResultReceiver) {
      MediaSessionCompat.Callback.this.onCommand(param1String, param1Bundle, param1ResultReceiver);
    }
    
    public void onCustomAction(String param1String, Bundle param1Bundle) {
      MediaSessionCompat.Callback.this.onCustomAction(param1String, param1Bundle);
    }
    
    public void onFastForward() {
      MediaSessionCompat.Callback.this.onFastForward();
    }
    
    public boolean onMediaButtonEvent(Intent param1Intent) {
      return MediaSessionCompat.Callback.this.onMediaButtonEvent(param1Intent);
    }
    
    public void onPause() {
      MediaSessionCompat.Callback.this.onPause();
    }
    
    public void onPlay() {
      MediaSessionCompat.Callback.this.onPlay();
    }
    
    public void onPlayFromMediaId(String param1String, Bundle param1Bundle) {
      MediaSessionCompat.Callback.this.onPlayFromMediaId(param1String, param1Bundle);
    }
    
    public void onPlayFromSearch(String param1String, Bundle param1Bundle) {
      MediaSessionCompat.Callback.this.onPlayFromSearch(param1String, param1Bundle);
    }
    
    public void onRewind() {
      MediaSessionCompat.Callback.this.onRewind();
    }
    
    public void onSeekTo(long param1Long) {
      MediaSessionCompat.Callback.this.onSeekTo(param1Long);
    }
    
    public void onSetRating(Object param1Object) {
      MediaSessionCompat.Callback.this.onSetRating(RatingCompat.fromRating(param1Object));
    }
    
    public void onSkipToNext() {
      MediaSessionCompat.Callback.this.onSkipToNext();
    }
    
    public void onSkipToPrevious() {
      MediaSessionCompat.Callback.this.onSkipToPrevious();
    }
    
    public void onSkipToQueueItem(long param1Long) {
      MediaSessionCompat.Callback.this.onSkipToQueueItem(param1Long);
    }
    
    public void onStop() {
      MediaSessionCompat.Callback.this.onStop();
    }
  }
  
  private class StubApi23 extends Callback.StubApi21 implements MediaSessionCompatApi23.Callback {
    private StubApi23() {
      super((MediaSessionCompat.Callback)this$0);
    }
    
    public void onPlayFromUri(Uri param1Uri, Bundle param1Bundle) {
      MediaSessionCompat.Callback.this.onPlayFromUri(param1Uri, param1Bundle);
    }
  }
  
  static interface MediaSessionImpl {
    Object getMediaSession();
    
    Object getRemoteControlClient();
    
    MediaSessionCompat.Token getSessionToken();
    
    boolean isActive();
    
    void release();
    
    void sendSessionEvent(String param1String, Bundle param1Bundle);
    
    void setActive(boolean param1Boolean);
    
    void setCallback(MediaSessionCompat.Callback param1Callback, Handler param1Handler);
    
    void setExtras(Bundle param1Bundle);
    
    void setFlags(int param1Int);
    
    void setMediaButtonReceiver(PendingIntent param1PendingIntent);
    
    void setMetadata(MediaMetadataCompat param1MediaMetadataCompat);
    
    void setPlaybackState(PlaybackStateCompat param1PlaybackStateCompat);
    
    void setPlaybackToLocal(int param1Int);
    
    void setPlaybackToRemote(VolumeProviderCompat param1VolumeProviderCompat);
    
    void setQueue(List<MediaSessionCompat.QueueItem> param1List);
    
    void setQueueTitle(CharSequence param1CharSequence);
    
    void setRatingType(int param1Int);
    
    void setSessionActivity(PendingIntent param1PendingIntent);
  }
  
  static class MediaSessionImplApi21 implements MediaSessionImpl {
    private PendingIntent mMediaButtonIntent;
    
    private final Object mSessionObj;
    
    private final MediaSessionCompat.Token mToken;
    
    public MediaSessionImplApi21(Context param1Context, String param1String) {
      this.mSessionObj = MediaSessionCompatApi21.createSession(param1Context, param1String);
      this.mToken = new MediaSessionCompat.Token(MediaSessionCompatApi21.getSessionToken(this.mSessionObj));
    }
    
    public MediaSessionImplApi21(Object param1Object) {
      this.mSessionObj = MediaSessionCompatApi21.verifySession(param1Object);
      this.mToken = new MediaSessionCompat.Token(MediaSessionCompatApi21.getSessionToken(this.mSessionObj));
    }
    
    public Object getMediaSession() {
      return this.mSessionObj;
    }
    
    public Object getRemoteControlClient() {
      return null;
    }
    
    public MediaSessionCompat.Token getSessionToken() {
      return this.mToken;
    }
    
    public boolean isActive() {
      return MediaSessionCompatApi21.isActive(this.mSessionObj);
    }
    
    public void release() {
      MediaSessionCompatApi21.release(this.mSessionObj);
    }
    
    public void sendSessionEvent(String param1String, Bundle param1Bundle) {
      MediaSessionCompatApi21.sendSessionEvent(this.mSessionObj, param1String, param1Bundle);
    }
    
    public void setActive(boolean param1Boolean) {
      MediaSessionCompatApi21.setActive(this.mSessionObj, param1Boolean);
    }
    
    public void setCallback(MediaSessionCompat.Callback param1Callback, Handler param1Handler) {
      Object object1;
      Object object2 = this.mSessionObj;
      if (param1Callback == null) {
        param1Callback = null;
      } else {
        object1 = param1Callback.mCallbackObj;
      } 
      MediaSessionCompatApi21.setCallback(object2, object1, param1Handler);
    }
    
    public void setExtras(Bundle param1Bundle) {
      MediaSessionCompatApi21.setExtras(this.mSessionObj, param1Bundle);
    }
    
    public void setFlags(int param1Int) {
      MediaSessionCompatApi21.setFlags(this.mSessionObj, param1Int);
    }
    
    public void setMediaButtonReceiver(PendingIntent param1PendingIntent) {
      this.mMediaButtonIntent = param1PendingIntent;
      MediaSessionCompatApi21.setMediaButtonReceiver(this.mSessionObj, param1PendingIntent);
    }
    
    public void setMetadata(MediaMetadataCompat param1MediaMetadataCompat) {
      Object object1;
      Object object2 = this.mSessionObj;
      if (param1MediaMetadataCompat == null) {
        param1MediaMetadataCompat = null;
      } else {
        object1 = param1MediaMetadataCompat.getMediaMetadata();
      } 
      MediaSessionCompatApi21.setMetadata(object2, object1);
    }
    
    public void setPlaybackState(PlaybackStateCompat param1PlaybackStateCompat) {
      Object object1;
      Object object2 = this.mSessionObj;
      if (param1PlaybackStateCompat == null) {
        param1PlaybackStateCompat = null;
      } else {
        object1 = param1PlaybackStateCompat.getPlaybackState();
      } 
      MediaSessionCompatApi21.setPlaybackState(object2, object1);
    }
    
    public void setPlaybackToLocal(int param1Int) {
      MediaSessionCompatApi21.setPlaybackToLocal(this.mSessionObj, param1Int);
    }
    
    public void setPlaybackToRemote(VolumeProviderCompat param1VolumeProviderCompat) {
      MediaSessionCompatApi21.setPlaybackToRemote(this.mSessionObj, param1VolumeProviderCompat.getVolumeProvider());
    }
    
    public void setQueue(List<MediaSessionCompat.QueueItem> param1List) {
      ArrayList<Object> arrayList = null;
      if (param1List != null) {
        ArrayList<Object> arrayList1 = new ArrayList();
        Iterator<MediaSessionCompat.QueueItem> iterator = param1List.iterator();
        while (true) {
          arrayList = arrayList1;
          if (iterator.hasNext()) {
            arrayList1.add(((MediaSessionCompat.QueueItem)iterator.next()).getQueueItem());
            continue;
          } 
          break;
        } 
      } 
      MediaSessionCompatApi21.setQueue(this.mSessionObj, arrayList);
    }
    
    public void setQueueTitle(CharSequence param1CharSequence) {
      MediaSessionCompatApi21.setQueueTitle(this.mSessionObj, param1CharSequence);
    }
    
    public void setRatingType(int param1Int) {
      if (Build.VERSION.SDK_INT < 22)
        return; 
      MediaSessionCompatApi22.setRatingType(this.mSessionObj, param1Int);
    }
    
    public void setSessionActivity(PendingIntent param1PendingIntent) {
      MediaSessionCompatApi21.setSessionActivity(this.mSessionObj, param1PendingIntent);
    }
  }
  
  static class MediaSessionImplBase implements MediaSessionImpl {
    private final AudioManager mAudioManager;
    
    private MediaSessionCompat.Callback mCallback;
    
    private final ComponentName mComponentName;
    
    private final Context mContext;
    
    private final RemoteCallbackList<IMediaControllerCallback> mControllerCallbacks = new RemoteCallbackList();
    
    private boolean mDestroyed = false;
    
    private Bundle mExtras;
    
    private int mFlags;
    
    private final MessageHandler mHandler;
    
    private boolean mIsActive = false;
    
    private boolean mIsMbrRegistered = false;
    
    private boolean mIsRccRegistered = false;
    
    private int mLocalStream;
    
    private final Object mLock = new Object();
    
    private final PendingIntent mMediaButtonEventReceiver;
    
    private MediaMetadataCompat mMetadata;
    
    private final String mPackageName;
    
    private List<MediaSessionCompat.QueueItem> mQueue;
    
    private CharSequence mQueueTitle;
    
    private int mRatingType;
    
    private final Object mRccObj;
    
    private PendingIntent mSessionActivity;
    
    private PlaybackStateCompat mState;
    
    private final MediaSessionStub mStub;
    
    private final String mTag;
    
    private final MediaSessionCompat.Token mToken;
    
    private VolumeProviderCompat.Callback mVolumeCallback = new VolumeProviderCompat.Callback() {
        public void onVolumeChanged(VolumeProviderCompat param2VolumeProviderCompat) {
          if (MediaSessionCompat.MediaSessionImplBase.this.mVolumeProvider != param2VolumeProviderCompat)
            return; 
          ParcelableVolumeInfo parcelableVolumeInfo = new ParcelableVolumeInfo(MediaSessionCompat.MediaSessionImplBase.this.mVolumeType, MediaSessionCompat.MediaSessionImplBase.this.mLocalStream, param2VolumeProviderCompat.getVolumeControl(), param2VolumeProviderCompat.getMaxVolume(), param2VolumeProviderCompat.getCurrentVolume());
          MediaSessionCompat.MediaSessionImplBase.this.sendVolumeInfoChanged(parcelableVolumeInfo);
        }
      };
    
    private VolumeProviderCompat mVolumeProvider;
    
    private int mVolumeType;
    
    public MediaSessionImplBase(Context param1Context, String param1String, ComponentName param1ComponentName, PendingIntent param1PendingIntent) {
      if (param1ComponentName == null)
        throw new IllegalArgumentException("MediaButtonReceiver component may not be null."); 
      this.mContext = param1Context;
      this.mPackageName = param1Context.getPackageName();
      this.mAudioManager = (AudioManager)param1Context.getSystemService("audio");
      this.mTag = param1String;
      this.mComponentName = param1ComponentName;
      this.mMediaButtonEventReceiver = param1PendingIntent;
      this.mStub = new MediaSessionStub();
      this.mToken = new MediaSessionCompat.Token(this.mStub);
      this.mHandler = new MessageHandler(Looper.myLooper());
      this.mRatingType = 0;
      this.mVolumeType = 1;
      this.mLocalStream = 3;
      if (Build.VERSION.SDK_INT >= 14) {
        this.mRccObj = MediaSessionCompatApi14.createRemoteControlClient(param1PendingIntent);
        return;
      } 
      this.mRccObj = null;
    }
    
    private void adjustVolume(int param1Int1, int param1Int2) {
      if (this.mVolumeType == 2) {
        if (this.mVolumeProvider != null)
          this.mVolumeProvider.onAdjustVolume(param1Int1); 
        return;
      } 
      this.mAudioManager.adjustStreamVolume(this.mLocalStream, param1Int1, param1Int2);
    }
    
    private PlaybackStateCompat getStateWithUpdatedPosition() {
      // Byte code:
      //   0: ldc2_w -1
      //   3: lstore_3
      //   4: aload_0
      //   5: getfield mLock : Ljava/lang/Object;
      //   8: astore #7
      //   10: aload #7
      //   12: monitorenter
      //   13: aload_0
      //   14: getfield mState : Landroid/support/v4/media/session/PlaybackStateCompat;
      //   17: astore #9
      //   19: lload_3
      //   20: lstore_1
      //   21: aload_0
      //   22: getfield mMetadata : Landroid/support/v4/media/MediaMetadataCompat;
      //   25: ifnull -> 54
      //   28: lload_3
      //   29: lstore_1
      //   30: aload_0
      //   31: getfield mMetadata : Landroid/support/v4/media/MediaMetadataCompat;
      //   34: ldc_w 'android.media.metadata.DURATION'
      //   37: invokevirtual containsKey : (Ljava/lang/String;)Z
      //   40: ifeq -> 54
      //   43: aload_0
      //   44: getfield mMetadata : Landroid/support/v4/media/MediaMetadataCompat;
      //   47: ldc_w 'android.media.metadata.DURATION'
      //   50: invokevirtual getLong : (Ljava/lang/String;)J
      //   53: lstore_1
      //   54: aload #7
      //   56: monitorexit
      //   57: aconst_null
      //   58: astore #8
      //   60: aload #8
      //   62: astore #7
      //   64: aload #9
      //   66: ifnull -> 189
      //   69: aload #9
      //   71: invokevirtual getState : ()I
      //   74: iconst_3
      //   75: if_icmpeq -> 100
      //   78: aload #9
      //   80: invokevirtual getState : ()I
      //   83: iconst_4
      //   84: if_icmpeq -> 100
      //   87: aload #8
      //   89: astore #7
      //   91: aload #9
      //   93: invokevirtual getState : ()I
      //   96: iconst_5
      //   97: if_icmpne -> 189
      //   100: aload #9
      //   102: invokevirtual getLastPositionUpdateTime : ()J
      //   105: lstore_3
      //   106: invokestatic elapsedRealtime : ()J
      //   109: lstore #5
      //   111: aload #8
      //   113: astore #7
      //   115: lload_3
      //   116: lconst_0
      //   117: lcmp
      //   118: ifle -> 189
      //   121: aload #9
      //   123: invokevirtual getPlaybackSpeed : ()F
      //   126: lload #5
      //   128: lload_3
      //   129: lsub
      //   130: l2f
      //   131: fmul
      //   132: f2l
      //   133: aload #9
      //   135: invokevirtual getPosition : ()J
      //   138: ladd
      //   139: lstore_3
      //   140: lload_1
      //   141: lconst_0
      //   142: lcmp
      //   143: iflt -> 205
      //   146: lload_3
      //   147: lload_1
      //   148: lcmp
      //   149: ifle -> 205
      //   152: new android/support/v4/media/session/PlaybackStateCompat$Builder
      //   155: dup
      //   156: aload #9
      //   158: invokespecial <init> : (Landroid/support/v4/media/session/PlaybackStateCompat;)V
      //   161: astore #7
      //   163: aload #7
      //   165: aload #9
      //   167: invokevirtual getState : ()I
      //   170: lload_1
      //   171: aload #9
      //   173: invokevirtual getPlaybackSpeed : ()F
      //   176: lload #5
      //   178: invokevirtual setState : (IJFJ)Landroid/support/v4/media/session/PlaybackStateCompat$Builder;
      //   181: pop
      //   182: aload #7
      //   184: invokevirtual build : ()Landroid/support/v4/media/session/PlaybackStateCompat;
      //   187: astore #7
      //   189: aload #7
      //   191: ifnonnull -> 218
      //   194: aload #9
      //   196: areturn
      //   197: astore #8
      //   199: aload #7
      //   201: monitorexit
      //   202: aload #8
      //   204: athrow
      //   205: lload_3
      //   206: lstore_1
      //   207: lload_3
      //   208: lconst_0
      //   209: lcmp
      //   210: ifge -> 152
      //   213: lconst_0
      //   214: lstore_1
      //   215: goto -> 152
      //   218: aload #7
      //   220: areturn
      // Exception table:
      //   from	to	target	type
      //   13	19	197	finally
      //   21	28	197	finally
      //   30	54	197	finally
      //   54	57	197	finally
      //   199	202	197	finally
    }
    
    private void sendEvent(String param1String, Bundle param1Bundle) {
      int i = this.mControllerCallbacks.beginBroadcast() - 1;
      while (true) {
        if (i >= 0) {
          IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
          try {
            iMediaControllerCallback.onEvent(param1String, param1Bundle);
          } catch (RemoteException remoteException) {}
          i--;
          continue;
        } 
        this.mControllerCallbacks.finishBroadcast();
        return;
      } 
    }
    
    private void sendMetadata(MediaMetadataCompat param1MediaMetadataCompat) {
      int i = this.mControllerCallbacks.beginBroadcast() - 1;
      while (true) {
        if (i >= 0) {
          IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
          try {
            iMediaControllerCallback.onMetadataChanged(param1MediaMetadataCompat);
          } catch (RemoteException remoteException) {}
          i--;
          continue;
        } 
        this.mControllerCallbacks.finishBroadcast();
        return;
      } 
    }
    
    private void sendQueue(List<MediaSessionCompat.QueueItem> param1List) {
      int i = this.mControllerCallbacks.beginBroadcast() - 1;
      while (true) {
        if (i >= 0) {
          IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
          try {
            iMediaControllerCallback.onQueueChanged(param1List);
          } catch (RemoteException remoteException) {}
          i--;
          continue;
        } 
        this.mControllerCallbacks.finishBroadcast();
        return;
      } 
    }
    
    private void sendQueueTitle(CharSequence param1CharSequence) {
      int i = this.mControllerCallbacks.beginBroadcast() - 1;
      while (true) {
        if (i >= 0) {
          IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
          try {
            iMediaControllerCallback.onQueueTitleChanged(param1CharSequence);
          } catch (RemoteException remoteException) {}
          i--;
          continue;
        } 
        this.mControllerCallbacks.finishBroadcast();
        return;
      } 
    }
    
    private void sendSessionDestroyed() {
      int i = this.mControllerCallbacks.beginBroadcast() - 1;
      while (true) {
        if (i >= 0) {
          IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
          try {
            iMediaControllerCallback.onSessionDestroyed();
          } catch (RemoteException remoteException) {}
          i--;
          continue;
        } 
        this.mControllerCallbacks.finishBroadcast();
        this.mControllerCallbacks.kill();
        return;
      } 
    }
    
    private void sendState(PlaybackStateCompat param1PlaybackStateCompat) {
      int i = this.mControllerCallbacks.beginBroadcast() - 1;
      while (true) {
        if (i >= 0) {
          IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
          try {
            iMediaControllerCallback.onPlaybackStateChanged(param1PlaybackStateCompat);
          } catch (RemoteException remoteException) {}
          i--;
          continue;
        } 
        this.mControllerCallbacks.finishBroadcast();
        return;
      } 
    }
    
    private void sendVolumeInfoChanged(ParcelableVolumeInfo param1ParcelableVolumeInfo) {
      int i = this.mControllerCallbacks.beginBroadcast() - 1;
      while (true) {
        if (i >= 0) {
          IMediaControllerCallback iMediaControllerCallback = (IMediaControllerCallback)this.mControllerCallbacks.getBroadcastItem(i);
          try {
            iMediaControllerCallback.onVolumeInfoChanged(param1ParcelableVolumeInfo);
          } catch (RemoteException remoteException) {}
          i--;
          continue;
        } 
        this.mControllerCallbacks.finishBroadcast();
        return;
      } 
    }
    
    private void setVolumeTo(int param1Int1, int param1Int2) {
      if (this.mVolumeType == 2) {
        if (this.mVolumeProvider != null)
          this.mVolumeProvider.onSetVolumeTo(param1Int1); 
        return;
      } 
      this.mAudioManager.setStreamVolume(this.mLocalStream, param1Int1, param1Int2);
    }
    
    private boolean update() {
      boolean bool2 = false;
      if (this.mIsActive) {
        if (Build.VERSION.SDK_INT >= 8)
          if (!this.mIsMbrRegistered && (this.mFlags & 0x1) != 0) {
            if (Build.VERSION.SDK_INT >= 18) {
              MediaSessionCompatApi18.registerMediaButtonEventReceiver(this.mContext, this.mMediaButtonEventReceiver, this.mComponentName);
            } else {
              MediaSessionCompatApi8.registerMediaButtonEventReceiver(this.mContext, this.mComponentName);
            } 
            this.mIsMbrRegistered = true;
          } else if (this.mIsMbrRegistered && (this.mFlags & 0x1) == 0) {
            if (Build.VERSION.SDK_INT >= 18) {
              MediaSessionCompatApi18.unregisterMediaButtonEventReceiver(this.mContext, this.mMediaButtonEventReceiver, this.mComponentName);
            } else {
              MediaSessionCompatApi8.unregisterMediaButtonEventReceiver(this.mContext, this.mComponentName);
            } 
            this.mIsMbrRegistered = false;
          }  
        boolean bool = bool2;
        if (Build.VERSION.SDK_INT >= 14) {
          if (!this.mIsRccRegistered && (this.mFlags & 0x2) != 0) {
            MediaSessionCompatApi14.registerRemoteControlClient(this.mContext, this.mRccObj);
            this.mIsRccRegistered = true;
            return true;
          } 
        } else {
          return bool;
        } 
        bool = bool2;
        if (this.mIsRccRegistered) {
          bool = bool2;
          if ((this.mFlags & 0x2) == 0) {
            MediaSessionCompatApi14.setState(this.mRccObj, 0);
            MediaSessionCompatApi14.unregisterRemoteControlClient(this.mContext, this.mRccObj);
            this.mIsRccRegistered = false;
            return false;
          } 
        } 
        return bool;
      } 
      if (this.mIsMbrRegistered) {
        if (Build.VERSION.SDK_INT >= 18) {
          MediaSessionCompatApi18.unregisterMediaButtonEventReceiver(this.mContext, this.mMediaButtonEventReceiver, this.mComponentName);
        } else {
          MediaSessionCompatApi8.unregisterMediaButtonEventReceiver(this.mContext, this.mComponentName);
        } 
        this.mIsMbrRegistered = false;
      } 
      boolean bool1 = bool2;
      if (this.mIsRccRegistered) {
        MediaSessionCompatApi14.setState(this.mRccObj, 0);
        MediaSessionCompatApi14.unregisterRemoteControlClient(this.mContext, this.mRccObj);
        this.mIsRccRegistered = false;
        return false;
      } 
      return bool1;
    }
    
    public Object getMediaSession() {
      return null;
    }
    
    public Object getRemoteControlClient() {
      return this.mRccObj;
    }
    
    public MediaSessionCompat.Token getSessionToken() {
      return this.mToken;
    }
    
    public boolean isActive() {
      return this.mIsActive;
    }
    
    public void release() {
      this.mIsActive = false;
      this.mDestroyed = true;
      update();
      sendSessionDestroyed();
    }
    
    public void sendSessionEvent(String param1String, Bundle param1Bundle) {
      sendEvent(param1String, param1Bundle);
    }
    
    public void setActive(boolean param1Boolean) {
      if (param1Boolean != this.mIsActive) {
        this.mIsActive = param1Boolean;
        if (update()) {
          setMetadata(this.mMetadata);
          setPlaybackState(this.mState);
          return;
        } 
      } 
    }
    
    public void setCallback(final MediaSessionCompat.Callback callback, Handler param1Handler) {
      if (callback == this.mCallback)
        return; 
      if (callback == null || Build.VERSION.SDK_INT < 18) {
        if (Build.VERSION.SDK_INT >= 18)
          MediaSessionCompatApi18.setOnPlaybackPositionUpdateListener(this.mRccObj, null); 
        if (Build.VERSION.SDK_INT >= 19)
          MediaSessionCompatApi19.setOnMetadataUpdateListener(this.mRccObj, null); 
      } else {
        if (param1Handler == null)
          new Handler(); 
        MediaSessionCompatApi14.Callback callback = new MediaSessionCompatApi14.Callback() {
            public void onCommand(String param2String, Bundle param2Bundle, ResultReceiver param2ResultReceiver) {
              callback.onCommand(param2String, param2Bundle, param2ResultReceiver);
            }
            
            public void onFastForward() {
              callback.onFastForward();
            }
            
            public boolean onMediaButtonEvent(Intent param2Intent) {
              return callback.onMediaButtonEvent(param2Intent);
            }
            
            public void onPause() {
              callback.onPause();
            }
            
            public void onPlay() {
              callback.onPlay();
            }
            
            public void onRewind() {
              callback.onRewind();
            }
            
            public void onSeekTo(long param2Long) {
              callback.onSeekTo(param2Long);
            }
            
            public void onSetRating(Object param2Object) {
              callback.onSetRating(RatingCompat.fromRating(param2Object));
            }
            
            public void onSkipToNext() {
              callback.onSkipToNext();
            }
            
            public void onSkipToPrevious() {
              callback.onSkipToPrevious();
            }
            
            public void onStop() {
              callback.onStop();
            }
          };
        if (Build.VERSION.SDK_INT >= 18) {
          Object object = MediaSessionCompatApi18.createPlaybackPositionUpdateListener(callback);
          MediaSessionCompatApi18.setOnPlaybackPositionUpdateListener(this.mRccObj, object);
        } 
        if (Build.VERSION.SDK_INT >= 19) {
          Object object = MediaSessionCompatApi19.createMetadataUpdateListener(callback);
          MediaSessionCompatApi19.setOnMetadataUpdateListener(this.mRccObj, object);
        } 
      } 
      this.mCallback = callback;
    }
    
    public void setExtras(Bundle param1Bundle) {
      this.mExtras = param1Bundle;
    }
    
    public void setFlags(int param1Int) {
      synchronized (this.mLock) {
        this.mFlags = param1Int;
        update();
        return;
      } 
    }
    
    public void setMediaButtonReceiver(PendingIntent param1PendingIntent) {}
    
    public void setMetadata(MediaMetadataCompat param1MediaMetadataCompat) {
      Object object = null;
      MediaMetadataCompat mediaMetadataCompat = null;
      synchronized (this.mLock) {
        this.mMetadata = param1MediaMetadataCompat;
        sendMetadata(param1MediaMetadataCompat);
        if (this.mIsActive) {
          Bundle bundle;
          if (Build.VERSION.SDK_INT >= 19) {
            long l;
            object = this.mRccObj;
            if (param1MediaMetadataCompat == null) {
              param1MediaMetadataCompat = mediaMetadataCompat;
            } else {
              bundle = param1MediaMetadataCompat.getBundle();
            } 
            if (this.mState == null) {
              l = 0L;
            } else {
              l = this.mState.getActions();
            } 
            MediaSessionCompatApi19.setMetadata(object, bundle, l);
            return;
          } 
          if (Build.VERSION.SDK_INT >= 14) {
            Object object1;
            Object object2 = this.mRccObj;
            if (bundle == null) {
              object1 = object;
            } else {
              object1 = object1.getBundle();
            } 
            MediaSessionCompatApi14.setMetadata(object2, (Bundle)object1);
            return;
          } 
        } 
        return;
      } 
    }
    
    public void setPlaybackState(PlaybackStateCompat param1PlaybackStateCompat) {
      synchronized (this.mLock) {
        this.mState = param1PlaybackStateCompat;
        sendState(param1PlaybackStateCompat);
        if (this.mIsActive) {
          if (param1PlaybackStateCompat == null) {
            if (Build.VERSION.SDK_INT >= 14) {
              MediaSessionCompatApi14.setState(this.mRccObj, 0);
              MediaSessionCompatApi14.setTransportControlFlags(this.mRccObj, 0L);
              return;
            } 
            return;
          } 
          if (Build.VERSION.SDK_INT >= 18) {
            MediaSessionCompatApi18.setState(this.mRccObj, param1PlaybackStateCompat.getState(), param1PlaybackStateCompat.getPosition(), param1PlaybackStateCompat.getPlaybackSpeed(), param1PlaybackStateCompat.getLastPositionUpdateTime());
          } else if (Build.VERSION.SDK_INT >= 14) {
            MediaSessionCompatApi14.setState(this.mRccObj, param1PlaybackStateCompat.getState());
          } 
          if (Build.VERSION.SDK_INT >= 19) {
            MediaSessionCompatApi19.setTransportControlFlags(this.mRccObj, param1PlaybackStateCompat.getActions());
            return;
          } 
          if (Build.VERSION.SDK_INT >= 18) {
            MediaSessionCompatApi18.setTransportControlFlags(this.mRccObj, param1PlaybackStateCompat.getActions());
            return;
          } 
          if (Build.VERSION.SDK_INT >= 14) {
            MediaSessionCompatApi14.setTransportControlFlags(this.mRccObj, param1PlaybackStateCompat.getActions());
            return;
          } 
        } 
        return;
      } 
    }
    
    public void setPlaybackToLocal(int param1Int) {
      if (this.mVolumeProvider != null)
        this.mVolumeProvider.setCallback(null); 
      this.mVolumeType = 1;
      sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, 2, this.mAudioManager.getStreamMaxVolume(this.mLocalStream), this.mAudioManager.getStreamVolume(this.mLocalStream)));
    }
    
    public void setPlaybackToRemote(VolumeProviderCompat param1VolumeProviderCompat) {
      if (param1VolumeProviderCompat == null)
        throw new IllegalArgumentException("volumeProvider may not be null"); 
      if (this.mVolumeProvider != null)
        this.mVolumeProvider.setCallback(null); 
      this.mVolumeType = 2;
      this.mVolumeProvider = param1VolumeProviderCompat;
      sendVolumeInfoChanged(new ParcelableVolumeInfo(this.mVolumeType, this.mLocalStream, this.mVolumeProvider.getVolumeControl(), this.mVolumeProvider.getMaxVolume(), this.mVolumeProvider.getCurrentVolume()));
      param1VolumeProviderCompat.setCallback(this.mVolumeCallback);
    }
    
    public void setQueue(List<MediaSessionCompat.QueueItem> param1List) {
      this.mQueue = param1List;
      sendQueue(param1List);
    }
    
    public void setQueueTitle(CharSequence param1CharSequence) {
      this.mQueueTitle = param1CharSequence;
      sendQueueTitle(param1CharSequence);
    }
    
    public void setRatingType(int param1Int) {
      this.mRatingType = param1Int;
    }
    
    public void setSessionActivity(PendingIntent param1PendingIntent) {
      synchronized (this.mLock) {
        this.mSessionActivity = param1PendingIntent;
        return;
      } 
    }
    
    private static final class Command {
      public final String command;
      
      public final Bundle extras;
      
      public final ResultReceiver stub;
      
      public Command(String param2String, Bundle param2Bundle, ResultReceiver param2ResultReceiver) {
        this.command = param2String;
        this.extras = param2Bundle;
        this.stub = param2ResultReceiver;
      }
    }
    
    class MediaSessionStub extends IMediaSession.Stub {
      public void adjustVolume(int param2Int1, int param2Int2, String param2String) {
        MediaSessionCompat.MediaSessionImplBase.this.adjustVolume(param2Int1, param2Int2);
      }
      
      public void fastForward() throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.mHandler.post(9);
      }
      
      public Bundle getExtras() {
        synchronized (MediaSessionCompat.MediaSessionImplBase.this.mLock) {
          return MediaSessionCompat.MediaSessionImplBase.this.mExtras;
        } 
      }
      
      public long getFlags() {
        synchronized (MediaSessionCompat.MediaSessionImplBase.this.mLock) {
          return MediaSessionCompat.MediaSessionImplBase.this.mFlags;
        } 
      }
      
      public PendingIntent getLaunchPendingIntent() {
        synchronized (MediaSessionCompat.MediaSessionImplBase.this.mLock) {
          return MediaSessionCompat.MediaSessionImplBase.this.mSessionActivity;
        } 
      }
      
      public MediaMetadataCompat getMetadata() {
        return MediaSessionCompat.MediaSessionImplBase.this.mMetadata;
      }
      
      public String getPackageName() {
        return MediaSessionCompat.MediaSessionImplBase.this.mPackageName;
      }
      
      public PlaybackStateCompat getPlaybackState() {
        return MediaSessionCompat.MediaSessionImplBase.this.getStateWithUpdatedPosition();
      }
      
      public List<MediaSessionCompat.QueueItem> getQueue() {
        synchronized (MediaSessionCompat.MediaSessionImplBase.this.mLock) {
          return MediaSessionCompat.MediaSessionImplBase.this.mQueue;
        } 
      }
      
      public CharSequence getQueueTitle() {
        return MediaSessionCompat.MediaSessionImplBase.this.mQueueTitle;
      }
      
      public int getRatingType() {
        return MediaSessionCompat.MediaSessionImplBase.this.mRatingType;
      }
      
      public String getTag() {
        return MediaSessionCompat.MediaSessionImplBase.this.mTag;
      }
      
      public ParcelableVolumeInfo getVolumeAttributes() {
        synchronized (MediaSessionCompat.MediaSessionImplBase.this.mLock) {
          int k = MediaSessionCompat.MediaSessionImplBase.this.mVolumeType;
          int m = MediaSessionCompat.MediaSessionImplBase.this.mLocalStream;
          VolumeProviderCompat volumeProviderCompat = MediaSessionCompat.MediaSessionImplBase.this.mVolumeProvider;
          if (k == 2) {
            int n = volumeProviderCompat.getVolumeControl();
            int i1 = volumeProviderCompat.getMaxVolume();
            int i2 = volumeProviderCompat.getCurrentVolume();
            return new ParcelableVolumeInfo(k, m, n, i1, i2);
          } 
          byte b = 2;
          int i = MediaSessionCompat.MediaSessionImplBase.this.mAudioManager.getStreamMaxVolume(m);
          int j = MediaSessionCompat.MediaSessionImplBase.this.mAudioManager.getStreamVolume(m);
          return new ParcelableVolumeInfo(k, m, b, i, j);
        } 
      }
      
      public boolean isTransportControlEnabled() {
        return ((MediaSessionCompat.MediaSessionImplBase.this.mFlags & 0x2) != 0);
      }
      
      public void next() throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.mHandler.post(7);
      }
      
      public void pause() throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.mHandler.post(5);
      }
      
      public void play() throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.mHandler.post(1);
      }
      
      public void playFromMediaId(String param2String, Bundle param2Bundle) throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.mHandler.post(2, param2String, param2Bundle);
      }
      
      public void playFromSearch(String param2String, Bundle param2Bundle) throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.mHandler.post(3, param2String, param2Bundle);
      }
      
      public void playFromUri(Uri param2Uri, Bundle param2Bundle) throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.mHandler.post(18, param2Uri, param2Bundle);
      }
      
      public void previous() throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.mHandler.post(8);
      }
      
      public void rate(RatingCompat param2RatingCompat) throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.mHandler.post(12, param2RatingCompat);
      }
      
      public void registerCallbackListener(IMediaControllerCallback param2IMediaControllerCallback) {
        if (MediaSessionCompat.MediaSessionImplBase.this.mDestroyed)
          try {
            param2IMediaControllerCallback.onSessionDestroyed();
            return;
          } catch (Exception exception) {
            return;
          }  
        MediaSessionCompat.MediaSessionImplBase.this.mControllerCallbacks.register((IInterface)exception);
      }
      
      public void rewind() throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.mHandler.post(10);
      }
      
      public void seekTo(long param2Long) throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.mHandler.post(11, Long.valueOf(param2Long));
      }
      
      public void sendCommand(String param2String, Bundle param2Bundle, MediaSessionCompat.ResultReceiverWrapper param2ResultReceiverWrapper) {
        MediaSessionCompat.MediaSessionImplBase.this.mHandler.post(15, new MediaSessionCompat.MediaSessionImplBase.Command(param2String, param2Bundle, param2ResultReceiverWrapper.mResultReceiver));
      }
      
      public void sendCustomAction(String param2String, Bundle param2Bundle) throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.mHandler.post(13, param2String, param2Bundle);
      }
      
      public boolean sendMediaButton(KeyEvent param2KeyEvent) {
        boolean bool;
        if ((MediaSessionCompat.MediaSessionImplBase.this.mFlags & 0x1) != 0) {
          bool = true;
        } else {
          bool = false;
        } 
        if (bool)
          MediaSessionCompat.MediaSessionImplBase.this.mHandler.post(14, param2KeyEvent); 
        return bool;
      }
      
      public void setVolumeTo(int param2Int1, int param2Int2, String param2String) {
        MediaSessionCompat.MediaSessionImplBase.this.setVolumeTo(param2Int1, param2Int2);
      }
      
      public void skipToQueueItem(long param2Long) {
        MediaSessionCompat.MediaSessionImplBase.this.mHandler.post(4, Long.valueOf(param2Long));
      }
      
      public void stop() throws RemoteException {
        MediaSessionCompat.MediaSessionImplBase.this.mHandler.post(6);
      }
      
      public void unregisterCallbackListener(IMediaControllerCallback param2IMediaControllerCallback) {
        MediaSessionCompat.MediaSessionImplBase.this.mControllerCallbacks.unregister(param2IMediaControllerCallback);
      }
    }
    
    private class MessageHandler extends Handler {
      private static final int KEYCODE_MEDIA_PAUSE = 127;
      
      private static final int KEYCODE_MEDIA_PLAY = 126;
      
      private static final int MSG_ADJUST_VOLUME = 16;
      
      private static final int MSG_COMMAND = 15;
      
      private static final int MSG_CUSTOM_ACTION = 13;
      
      private static final int MSG_FAST_FORWARD = 9;
      
      private static final int MSG_MEDIA_BUTTON = 14;
      
      private static final int MSG_NEXT = 7;
      
      private static final int MSG_PAUSE = 5;
      
      private static final int MSG_PLAY = 1;
      
      private static final int MSG_PLAY_MEDIA_ID = 2;
      
      private static final int MSG_PLAY_SEARCH = 3;
      
      private static final int MSG_PLAY_URI = 18;
      
      private static final int MSG_PREVIOUS = 8;
      
      private static final int MSG_RATE = 12;
      
      private static final int MSG_REWIND = 10;
      
      private static final int MSG_SEEK_TO = 11;
      
      private static final int MSG_SET_VOLUME = 17;
      
      private static final int MSG_SKIP_TO_ITEM = 4;
      
      private static final int MSG_STOP = 6;
      
      public MessageHandler(Looper param2Looper) {
        super(param2Looper);
      }
      
      private void onMediaButtonEvent(KeyEvent param2KeyEvent) {
        boolean bool = true;
        if (param2KeyEvent != null && param2KeyEvent.getAction() == 0) {
          boolean bool1;
          boolean bool2;
          long l;
          if (MediaSessionCompat.MediaSessionImplBase.this.mState == null) {
            l = 0L;
          } else {
            l = MediaSessionCompat.MediaSessionImplBase.this.mState.getActions();
          } 
          switch (param2KeyEvent.getKeyCode()) {
            default:
              return;
            case 79:
            case 85:
              if (MediaSessionCompat.MediaSessionImplBase.this.mState != null && MediaSessionCompat.MediaSessionImplBase.this.mState.getState() == 3) {
                bool1 = true;
              } else {
                bool1 = false;
              } 
              if ((0x204L & l) != 0L) {
                bool2 = true;
              } else {
                bool2 = false;
              } 
              if ((0x202L & l) == 0L)
                bool = false; 
              if (bool1 && bool) {
                MediaSessionCompat.MediaSessionImplBase.this.mCallback.onPause();
                return;
              } 
              break;
            case 126:
              if ((0x4L & l) != 0L) {
                MediaSessionCompat.MediaSessionImplBase.this.mCallback.onPlay();
                return;
              } 
              return;
            case 127:
              if ((0x2L & l) != 0L) {
                MediaSessionCompat.MediaSessionImplBase.this.mCallback.onPause();
                return;
              } 
              return;
            case 87:
              if ((0x20L & l) != 0L) {
                MediaSessionCompat.MediaSessionImplBase.this.mCallback.onSkipToNext();
                return;
              } 
              return;
            case 88:
              if ((0x10L & l) != 0L) {
                MediaSessionCompat.MediaSessionImplBase.this.mCallback.onSkipToPrevious();
                return;
              } 
              return;
            case 86:
              if ((0x1L & l) != 0L) {
                MediaSessionCompat.MediaSessionImplBase.this.mCallback.onStop();
                return;
              } 
              return;
            case 90:
              if ((0x40L & l) != 0L) {
                MediaSessionCompat.MediaSessionImplBase.this.mCallback.onFastForward();
                return;
              } 
              return;
            case 89:
              if ((0x8L & l) != 0L) {
                MediaSessionCompat.MediaSessionImplBase.this.mCallback.onRewind();
                return;
              } 
              return;
          } 
          if (!bool1 && bool2) {
            MediaSessionCompat.MediaSessionImplBase.this.mCallback.onPlay();
            return;
          } 
        } 
      }
      
      public void handleMessage(Message param2Message) {
        if (MediaSessionCompat.MediaSessionImplBase.this.mCallback != null) {
          KeyEvent keyEvent;
          MediaSessionCompat.MediaSessionImplBase.Command command;
          Intent intent;
          switch (param2Message.what) {
            default:
              return;
            case 1:
              MediaSessionCompat.MediaSessionImplBase.this.mCallback.onPlay();
              return;
            case 2:
              MediaSessionCompat.MediaSessionImplBase.this.mCallback.onPlayFromMediaId((String)param2Message.obj, param2Message.getData());
              return;
            case 3:
              MediaSessionCompat.MediaSessionImplBase.this.mCallback.onPlayFromSearch((String)param2Message.obj, param2Message.getData());
              return;
            case 18:
              MediaSessionCompat.MediaSessionImplBase.this.mCallback.onPlayFromUri((Uri)param2Message.obj, param2Message.getData());
              return;
            case 4:
              MediaSessionCompat.MediaSessionImplBase.this.mCallback.onSkipToQueueItem(((Long)param2Message.obj).longValue());
              return;
            case 5:
              MediaSessionCompat.MediaSessionImplBase.this.mCallback.onPause();
              return;
            case 6:
              MediaSessionCompat.MediaSessionImplBase.this.mCallback.onStop();
              return;
            case 7:
              MediaSessionCompat.MediaSessionImplBase.this.mCallback.onSkipToNext();
              return;
            case 8:
              MediaSessionCompat.MediaSessionImplBase.this.mCallback.onSkipToPrevious();
              return;
            case 9:
              MediaSessionCompat.MediaSessionImplBase.this.mCallback.onFastForward();
              return;
            case 10:
              MediaSessionCompat.MediaSessionImplBase.this.mCallback.onRewind();
              return;
            case 11:
              MediaSessionCompat.MediaSessionImplBase.this.mCallback.onSeekTo(((Long)param2Message.obj).longValue());
              return;
            case 12:
              MediaSessionCompat.MediaSessionImplBase.this.mCallback.onSetRating((RatingCompat)param2Message.obj);
              return;
            case 13:
              MediaSessionCompat.MediaSessionImplBase.this.mCallback.onCustomAction((String)param2Message.obj, param2Message.getData());
              return;
            case 14:
              keyEvent = (KeyEvent)param2Message.obj;
              intent = new Intent("android.intent.action.MEDIA_BUTTON");
              intent.putExtra("android.intent.extra.KEY_EVENT", (Parcelable)keyEvent);
              if (!MediaSessionCompat.MediaSessionImplBase.this.mCallback.onMediaButtonEvent(intent)) {
                onMediaButtonEvent(keyEvent);
                return;
              } 
              return;
            case 15:
              command = (MediaSessionCompat.MediaSessionImplBase.Command)((Message)keyEvent).obj;
              MediaSessionCompat.MediaSessionImplBase.this.mCallback.onCommand(command.command, command.extras, command.stub);
              return;
            case 16:
              MediaSessionCompat.MediaSessionImplBase.this.adjustVolume(((Integer)((Message)command).obj).intValue(), 0);
              return;
            case 17:
              break;
          } 
          MediaSessionCompat.MediaSessionImplBase.this.setVolumeTo(((Integer)((Message)command).obj).intValue(), 0);
          return;
        } 
      }
      
      public void post(int param2Int) {
        post(param2Int, (Object)null);
      }
      
      public void post(int param2Int, Object param2Object) {
        obtainMessage(param2Int, param2Object).sendToTarget();
      }
      
      public void post(int param2Int1, Object param2Object, int param2Int2) {
        obtainMessage(param2Int1, param2Int2, 0, param2Object).sendToTarget();
      }
      
      public void post(int param2Int, Object param2Object, Bundle param2Bundle) {
        param2Object = obtainMessage(param2Int, param2Object);
        param2Object.setData(param2Bundle);
        param2Object.sendToTarget();
      }
    }
  }
  
  class null extends VolumeProviderCompat.Callback {
    public void onVolumeChanged(VolumeProviderCompat param1VolumeProviderCompat) {
      if (this.this$0.mVolumeProvider != param1VolumeProviderCompat)
        return; 
      ParcelableVolumeInfo parcelableVolumeInfo = new ParcelableVolumeInfo(this.this$0.mVolumeType, this.this$0.mLocalStream, param1VolumeProviderCompat.getVolumeControl(), param1VolumeProviderCompat.getMaxVolume(), param1VolumeProviderCompat.getCurrentVolume());
      this.this$0.sendVolumeInfoChanged(parcelableVolumeInfo);
    }
  }
  
  class null implements MediaSessionCompatApi14.Callback {
    public void onCommand(String param1String, Bundle param1Bundle, ResultReceiver param1ResultReceiver) {
      callback.onCommand(param1String, param1Bundle, param1ResultReceiver);
    }
    
    public void onFastForward() {
      callback.onFastForward();
    }
    
    public boolean onMediaButtonEvent(Intent param1Intent) {
      return callback.onMediaButtonEvent(param1Intent);
    }
    
    public void onPause() {
      callback.onPause();
    }
    
    public void onPlay() {
      callback.onPlay();
    }
    
    public void onRewind() {
      callback.onRewind();
    }
    
    public void onSeekTo(long param1Long) {
      callback.onSeekTo(param1Long);
    }
    
    public void onSetRating(Object param1Object) {
      callback.onSetRating(RatingCompat.fromRating(param1Object));
    }
    
    public void onSkipToNext() {
      callback.onSkipToNext();
    }
    
    public void onSkipToPrevious() {
      callback.onSkipToPrevious();
    }
    
    public void onStop() {
      callback.onStop();
    }
  }
  
  private static final class Command {
    public final String command;
    
    public final Bundle extras;
    
    public final ResultReceiver stub;
    
    public Command(String param1String, Bundle param1Bundle, ResultReceiver param1ResultReceiver) {
      this.command = param1String;
      this.extras = param1Bundle;
      this.stub = param1ResultReceiver;
    }
  }
  
  class MediaSessionStub extends IMediaSession.Stub {
    public void adjustVolume(int param1Int1, int param1Int2, String param1String) {
      this.this$0.adjustVolume(param1Int1, param1Int2);
    }
    
    public void fastForward() throws RemoteException {
      this.this$0.mHandler.post(9);
    }
    
    public Bundle getExtras() {
      synchronized (this.this$0.mLock) {
        return this.this$0.mExtras;
      } 
    }
    
    public long getFlags() {
      synchronized (this.this$0.mLock) {
        return this.this$0.mFlags;
      } 
    }
    
    public PendingIntent getLaunchPendingIntent() {
      synchronized (this.this$0.mLock) {
        return this.this$0.mSessionActivity;
      } 
    }
    
    public MediaMetadataCompat getMetadata() {
      return this.this$0.mMetadata;
    }
    
    public String getPackageName() {
      return this.this$0.mPackageName;
    }
    
    public PlaybackStateCompat getPlaybackState() {
      return this.this$0.getStateWithUpdatedPosition();
    }
    
    public List<MediaSessionCompat.QueueItem> getQueue() {
      synchronized (this.this$0.mLock) {
        return this.this$0.mQueue;
      } 
    }
    
    public CharSequence getQueueTitle() {
      return this.this$0.mQueueTitle;
    }
    
    public int getRatingType() {
      return this.this$0.mRatingType;
    }
    
    public String getTag() {
      return this.this$0.mTag;
    }
    
    public ParcelableVolumeInfo getVolumeAttributes() {
      synchronized (this.this$0.mLock) {
        int k = this.this$0.mVolumeType;
        int m = this.this$0.mLocalStream;
        VolumeProviderCompat volumeProviderCompat = this.this$0.mVolumeProvider;
        if (k == 2) {
          int n = volumeProviderCompat.getVolumeControl();
          int i1 = volumeProviderCompat.getMaxVolume();
          int i2 = volumeProviderCompat.getCurrentVolume();
          return new ParcelableVolumeInfo(k, m, n, i1, i2);
        } 
        byte b = 2;
        int i = this.this$0.mAudioManager.getStreamMaxVolume(m);
        int j = this.this$0.mAudioManager.getStreamVolume(m);
        return new ParcelableVolumeInfo(k, m, b, i, j);
      } 
    }
    
    public boolean isTransportControlEnabled() {
      return ((this.this$0.mFlags & 0x2) != 0);
    }
    
    public void next() throws RemoteException {
      this.this$0.mHandler.post(7);
    }
    
    public void pause() throws RemoteException {
      this.this$0.mHandler.post(5);
    }
    
    public void play() throws RemoteException {
      this.this$0.mHandler.post(1);
    }
    
    public void playFromMediaId(String param1String, Bundle param1Bundle) throws RemoteException {
      this.this$0.mHandler.post(2, param1String, param1Bundle);
    }
    
    public void playFromSearch(String param1String, Bundle param1Bundle) throws RemoteException {
      this.this$0.mHandler.post(3, param1String, param1Bundle);
    }
    
    public void playFromUri(Uri param1Uri, Bundle param1Bundle) throws RemoteException {
      this.this$0.mHandler.post(18, param1Uri, param1Bundle);
    }
    
    public void previous() throws RemoteException {
      this.this$0.mHandler.post(8);
    }
    
    public void rate(RatingCompat param1RatingCompat) throws RemoteException {
      this.this$0.mHandler.post(12, param1RatingCompat);
    }
    
    public void registerCallbackListener(IMediaControllerCallback param1IMediaControllerCallback) {
      if (this.this$0.mDestroyed)
        try {
          param1IMediaControllerCallback.onSessionDestroyed();
          return;
        } catch (Exception exception) {
          return;
        }  
      this.this$0.mControllerCallbacks.register((IInterface)exception);
    }
    
    public void rewind() throws RemoteException {
      this.this$0.mHandler.post(10);
    }
    
    public void seekTo(long param1Long) throws RemoteException {
      this.this$0.mHandler.post(11, Long.valueOf(param1Long));
    }
    
    public void sendCommand(String param1String, Bundle param1Bundle, MediaSessionCompat.ResultReceiverWrapper param1ResultReceiverWrapper) {
      this.this$0.mHandler.post(15, new MediaSessionCompat.MediaSessionImplBase.Command(param1String, param1Bundle, param1ResultReceiverWrapper.mResultReceiver));
    }
    
    public void sendCustomAction(String param1String, Bundle param1Bundle) throws RemoteException {
      this.this$0.mHandler.post(13, param1String, param1Bundle);
    }
    
    public boolean sendMediaButton(KeyEvent param1KeyEvent) {
      boolean bool;
      if ((this.this$0.mFlags & 0x1) != 0) {
        bool = true;
      } else {
        bool = false;
      } 
      if (bool)
        this.this$0.mHandler.post(14, param1KeyEvent); 
      return bool;
    }
    
    public void setVolumeTo(int param1Int1, int param1Int2, String param1String) {
      this.this$0.setVolumeTo(param1Int1, param1Int2);
    }
    
    public void skipToQueueItem(long param1Long) {
      this.this$0.mHandler.post(4, Long.valueOf(param1Long));
    }
    
    public void stop() throws RemoteException {
      this.this$0.mHandler.post(6);
    }
    
    public void unregisterCallbackListener(IMediaControllerCallback param1IMediaControllerCallback) {
      this.this$0.mControllerCallbacks.unregister(param1IMediaControllerCallback);
    }
  }
  
  private class MessageHandler extends Handler {
    private static final int KEYCODE_MEDIA_PAUSE = 127;
    
    private static final int KEYCODE_MEDIA_PLAY = 126;
    
    private static final int MSG_ADJUST_VOLUME = 16;
    
    private static final int MSG_COMMAND = 15;
    
    private static final int MSG_CUSTOM_ACTION = 13;
    
    private static final int MSG_FAST_FORWARD = 9;
    
    private static final int MSG_MEDIA_BUTTON = 14;
    
    private static final int MSG_NEXT = 7;
    
    private static final int MSG_PAUSE = 5;
    
    private static final int MSG_PLAY = 1;
    
    private static final int MSG_PLAY_MEDIA_ID = 2;
    
    private static final int MSG_PLAY_SEARCH = 3;
    
    private static final int MSG_PLAY_URI = 18;
    
    private static final int MSG_PREVIOUS = 8;
    
    private static final int MSG_RATE = 12;
    
    private static final int MSG_REWIND = 10;
    
    private static final int MSG_SEEK_TO = 11;
    
    private static final int MSG_SET_VOLUME = 17;
    
    private static final int MSG_SKIP_TO_ITEM = 4;
    
    private static final int MSG_STOP = 6;
    
    public MessageHandler(Looper param1Looper) {
      super(param1Looper);
    }
    
    private void onMediaButtonEvent(KeyEvent param1KeyEvent) {
      boolean bool = true;
      if (param1KeyEvent != null && param1KeyEvent.getAction() == 0) {
        boolean bool1;
        boolean bool2;
        long l;
        if (this.this$0.mState == null) {
          l = 0L;
        } else {
          l = this.this$0.mState.getActions();
        } 
        switch (param1KeyEvent.getKeyCode()) {
          default:
            return;
          case 79:
          case 85:
            if (this.this$0.mState != null && this.this$0.mState.getState() == 3) {
              bool1 = true;
            } else {
              bool1 = false;
            } 
            if ((0x204L & l) != 0L) {
              bool2 = true;
            } else {
              bool2 = false;
            } 
            if ((0x202L & l) == 0L)
              bool = false; 
            if (bool1 && bool) {
              this.this$0.mCallback.onPause();
              return;
            } 
            break;
          case 126:
            if ((0x4L & l) != 0L) {
              this.this$0.mCallback.onPlay();
              return;
            } 
            return;
          case 127:
            if ((0x2L & l) != 0L) {
              this.this$0.mCallback.onPause();
              return;
            } 
            return;
          case 87:
            if ((0x20L & l) != 0L) {
              this.this$0.mCallback.onSkipToNext();
              return;
            } 
            return;
          case 88:
            if ((0x10L & l) != 0L) {
              this.this$0.mCallback.onSkipToPrevious();
              return;
            } 
            return;
          case 86:
            if ((0x1L & l) != 0L) {
              this.this$0.mCallback.onStop();
              return;
            } 
            return;
          case 90:
            if ((0x40L & l) != 0L) {
              this.this$0.mCallback.onFastForward();
              return;
            } 
            return;
          case 89:
            if ((0x8L & l) != 0L) {
              this.this$0.mCallback.onRewind();
              return;
            } 
            return;
        } 
        if (!bool1 && bool2) {
          this.this$0.mCallback.onPlay();
          return;
        } 
      } 
    }
    
    public void handleMessage(Message param1Message) {
      if (this.this$0.mCallback != null) {
        KeyEvent keyEvent;
        MediaSessionCompat.MediaSessionImplBase.Command command;
        Intent intent;
        switch (param1Message.what) {
          default:
            return;
          case 1:
            this.this$0.mCallback.onPlay();
            return;
          case 2:
            this.this$0.mCallback.onPlayFromMediaId((String)param1Message.obj, param1Message.getData());
            return;
          case 3:
            this.this$0.mCallback.onPlayFromSearch((String)param1Message.obj, param1Message.getData());
            return;
          case 18:
            this.this$0.mCallback.onPlayFromUri((Uri)param1Message.obj, param1Message.getData());
            return;
          case 4:
            this.this$0.mCallback.onSkipToQueueItem(((Long)param1Message.obj).longValue());
            return;
          case 5:
            this.this$0.mCallback.onPause();
            return;
          case 6:
            this.this$0.mCallback.onStop();
            return;
          case 7:
            this.this$0.mCallback.onSkipToNext();
            return;
          case 8:
            this.this$0.mCallback.onSkipToPrevious();
            return;
          case 9:
            this.this$0.mCallback.onFastForward();
            return;
          case 10:
            this.this$0.mCallback.onRewind();
            return;
          case 11:
            this.this$0.mCallback.onSeekTo(((Long)param1Message.obj).longValue());
            return;
          case 12:
            this.this$0.mCallback.onSetRating((RatingCompat)param1Message.obj);
            return;
          case 13:
            this.this$0.mCallback.onCustomAction((String)param1Message.obj, param1Message.getData());
            return;
          case 14:
            keyEvent = (KeyEvent)param1Message.obj;
            intent = new Intent("android.intent.action.MEDIA_BUTTON");
            intent.putExtra("android.intent.extra.KEY_EVENT", (Parcelable)keyEvent);
            if (!this.this$0.mCallback.onMediaButtonEvent(intent)) {
              onMediaButtonEvent(keyEvent);
              return;
            } 
            return;
          case 15:
            command = (MediaSessionCompat.MediaSessionImplBase.Command)((Message)keyEvent).obj;
            this.this$0.mCallback.onCommand(command.command, command.extras, command.stub);
            return;
          case 16:
            this.this$0.adjustVolume(((Integer)((Message)command).obj).intValue(), 0);
            return;
          case 17:
            break;
        } 
        this.this$0.setVolumeTo(((Integer)((Message)command).obj).intValue(), 0);
        return;
      } 
    }
    
    public void post(int param1Int) {
      post(param1Int, (Object)null);
    }
    
    public void post(int param1Int, Object param1Object) {
      obtainMessage(param1Int, param1Object).sendToTarget();
    }
    
    public void post(int param1Int1, Object param1Object, int param1Int2) {
      obtainMessage(param1Int1, param1Int2, 0, param1Object).sendToTarget();
    }
    
    public void post(int param1Int, Object param1Object, Bundle param1Bundle) {
      param1Object = obtainMessage(param1Int, param1Object);
      param1Object.setData(param1Bundle);
      param1Object.sendToTarget();
    }
  }
  
  public static interface OnActiveChangeListener {
    void onActiveChanged();
  }
  
  public static final class QueueItem implements Parcelable {
    public static final Parcelable.Creator<QueueItem> CREATOR = new Parcelable.Creator<QueueItem>() {
        public MediaSessionCompat.QueueItem createFromParcel(Parcel param2Parcel) {
          return new MediaSessionCompat.QueueItem(param2Parcel);
        }
        
        public MediaSessionCompat.QueueItem[] newArray(int param2Int) {
          return new MediaSessionCompat.QueueItem[param2Int];
        }
      };
    
    public static final int UNKNOWN_ID = -1;
    
    private final MediaDescriptionCompat mDescription;
    
    private final long mId;
    
    private Object mItem;
    
    private QueueItem(Parcel param1Parcel) {
      this.mDescription = (MediaDescriptionCompat)MediaDescriptionCompat.CREATOR.createFromParcel(param1Parcel);
      this.mId = param1Parcel.readLong();
    }
    
    public QueueItem(MediaDescriptionCompat param1MediaDescriptionCompat, long param1Long) {
      this(null, param1MediaDescriptionCompat, param1Long);
    }
    
    private QueueItem(Object param1Object, MediaDescriptionCompat param1MediaDescriptionCompat, long param1Long) {
      if (param1MediaDescriptionCompat == null)
        throw new IllegalArgumentException("Description cannot be null."); 
      if (param1Long == -1L)
        throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID"); 
      this.mDescription = param1MediaDescriptionCompat;
      this.mId = param1Long;
      this.mItem = param1Object;
    }
    
    public static QueueItem obtain(Object param1Object) {
      return new QueueItem(param1Object, MediaDescriptionCompat.fromMediaDescription(MediaSessionCompatApi21.QueueItem.getDescription(param1Object)), MediaSessionCompatApi21.QueueItem.getQueueId(param1Object));
    }
    
    public int describeContents() {
      return 0;
    }
    
    public MediaDescriptionCompat getDescription() {
      return this.mDescription;
    }
    
    public long getQueueId() {
      return this.mId;
    }
    
    public Object getQueueItem() {
      if (this.mItem != null || Build.VERSION.SDK_INT < 21)
        return this.mItem; 
      this.mItem = MediaSessionCompatApi21.QueueItem.createItem(this.mDescription.getMediaDescription(), this.mId);
      return this.mItem;
    }
    
    public String toString() {
      return "MediaSession.QueueItem {Description=" + this.mDescription + ", Id=" + this.mId + " }";
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      this.mDescription.writeToParcel(param1Parcel, param1Int);
      param1Parcel.writeLong(this.mId);
    }
  }
  
  static final class null implements Parcelable.Creator<QueueItem> {
    public MediaSessionCompat.QueueItem createFromParcel(Parcel param1Parcel) {
      return new MediaSessionCompat.QueueItem(param1Parcel);
    }
    
    public MediaSessionCompat.QueueItem[] newArray(int param1Int) {
      return new MediaSessionCompat.QueueItem[param1Int];
    }
  }
  
  static final class ResultReceiverWrapper implements Parcelable {
    public static final Parcelable.Creator<ResultReceiverWrapper> CREATOR = new Parcelable.Creator<ResultReceiverWrapper>() {
        public MediaSessionCompat.ResultReceiverWrapper createFromParcel(Parcel param2Parcel) {
          return new MediaSessionCompat.ResultReceiverWrapper(param2Parcel);
        }
        
        public MediaSessionCompat.ResultReceiverWrapper[] newArray(int param2Int) {
          return new MediaSessionCompat.ResultReceiverWrapper[param2Int];
        }
      };
    
    private ResultReceiver mResultReceiver;
    
    ResultReceiverWrapper(Parcel param1Parcel) {
      this.mResultReceiver = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(param1Parcel);
    }
    
    public ResultReceiverWrapper(ResultReceiver param1ResultReceiver) {
      this.mResultReceiver = param1ResultReceiver;
    }
    
    public int describeContents() {
      return 0;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      this.mResultReceiver.writeToParcel(param1Parcel, param1Int);
    }
  }
  
  static final class null implements Parcelable.Creator<ResultReceiverWrapper> {
    public MediaSessionCompat.ResultReceiverWrapper createFromParcel(Parcel param1Parcel) {
      return new MediaSessionCompat.ResultReceiverWrapper(param1Parcel);
    }
    
    public MediaSessionCompat.ResultReceiverWrapper[] newArray(int param1Int) {
      return new MediaSessionCompat.ResultReceiverWrapper[param1Int];
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface SessionFlags {}
  
  public static final class Token implements Parcelable {
    public static final Parcelable.Creator<Token> CREATOR = new Parcelable.Creator<Token>() {
        public MediaSessionCompat.Token createFromParcel(Parcel param2Parcel) {
          Parcelable parcelable;
          if (Build.VERSION.SDK_INT >= 21) {
            parcelable = param2Parcel.readParcelable(null);
            return new MediaSessionCompat.Token(parcelable);
          } 
          IBinder iBinder = parcelable.readStrongBinder();
          return new MediaSessionCompat.Token(iBinder);
        }
        
        public MediaSessionCompat.Token[] newArray(int param2Int) {
          return new MediaSessionCompat.Token[param2Int];
        }
      };
    
    private final Object mInner;
    
    Token(Object param1Object) {
      this.mInner = param1Object;
    }
    
    public static Token fromToken(Object param1Object) {
      return (param1Object == null || Build.VERSION.SDK_INT < 21) ? null : new Token(MediaSessionCompatApi21.verifyToken(param1Object));
    }
    
    public int describeContents() {
      return 0;
    }
    
    public Object getToken() {
      return this.mInner;
    }
    
    public void writeToParcel(Parcel param1Parcel, int param1Int) {
      if (Build.VERSION.SDK_INT >= 21) {
        param1Parcel.writeParcelable((Parcelable)this.mInner, param1Int);
        return;
      } 
      param1Parcel.writeStrongBinder((IBinder)this.mInner);
    }
  }
  
  static final class null implements Parcelable.Creator<Token> {
    public MediaSessionCompat.Token createFromParcel(Parcel param1Parcel) {
      Parcelable parcelable;
      if (Build.VERSION.SDK_INT >= 21) {
        parcelable = param1Parcel.readParcelable(null);
        return new MediaSessionCompat.Token(parcelable);
      } 
      IBinder iBinder = parcelable.readStrongBinder();
      return new MediaSessionCompat.Token(iBinder);
    }
    
    public MediaSessionCompat.Token[] newArray(int param1Int) {
      return new MediaSessionCompat.Token[param1Int];
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\media\session\MediaSessionCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */