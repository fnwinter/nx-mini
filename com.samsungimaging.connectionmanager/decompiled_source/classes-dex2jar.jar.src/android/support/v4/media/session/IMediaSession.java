package android.support.v4.media.session;

import android.app.PendingIntent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.RatingCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import java.util.List;

public interface IMediaSession extends IInterface {
  void adjustVolume(int paramInt1, int paramInt2, String paramString) throws RemoteException;
  
  void fastForward() throws RemoteException;
  
  Bundle getExtras() throws RemoteException;
  
  long getFlags() throws RemoteException;
  
  PendingIntent getLaunchPendingIntent() throws RemoteException;
  
  MediaMetadataCompat getMetadata() throws RemoteException;
  
  String getPackageName() throws RemoteException;
  
  PlaybackStateCompat getPlaybackState() throws RemoteException;
  
  List<MediaSessionCompat.QueueItem> getQueue() throws RemoteException;
  
  CharSequence getQueueTitle() throws RemoteException;
  
  int getRatingType() throws RemoteException;
  
  String getTag() throws RemoteException;
  
  ParcelableVolumeInfo getVolumeAttributes() throws RemoteException;
  
  boolean isTransportControlEnabled() throws RemoteException;
  
  void next() throws RemoteException;
  
  void pause() throws RemoteException;
  
  void play() throws RemoteException;
  
  void playFromMediaId(String paramString, Bundle paramBundle) throws RemoteException;
  
  void playFromSearch(String paramString, Bundle paramBundle) throws RemoteException;
  
  void playFromUri(Uri paramUri, Bundle paramBundle) throws RemoteException;
  
  void previous() throws RemoteException;
  
  void rate(RatingCompat paramRatingCompat) throws RemoteException;
  
  void registerCallbackListener(IMediaControllerCallback paramIMediaControllerCallback) throws RemoteException;
  
  void rewind() throws RemoteException;
  
  void seekTo(long paramLong) throws RemoteException;
  
  void sendCommand(String paramString, Bundle paramBundle, MediaSessionCompat.ResultReceiverWrapper paramResultReceiverWrapper) throws RemoteException;
  
  void sendCustomAction(String paramString, Bundle paramBundle) throws RemoteException;
  
  boolean sendMediaButton(KeyEvent paramKeyEvent) throws RemoteException;
  
  void setVolumeTo(int paramInt1, int paramInt2, String paramString) throws RemoteException;
  
  void skipToQueueItem(long paramLong) throws RemoteException;
  
  void stop() throws RemoteException;
  
  void unregisterCallbackListener(IMediaControllerCallback paramIMediaControllerCallback) throws RemoteException;
  
  public static abstract class Stub extends Binder implements IMediaSession {
    private static final String DESCRIPTOR = "android.support.v4.media.session.IMediaSession";
    
    static final int TRANSACTION_adjustVolume = 11;
    
    static final int TRANSACTION_fastForward = 22;
    
    static final int TRANSACTION_getExtras = 31;
    
    static final int TRANSACTION_getFlags = 9;
    
    static final int TRANSACTION_getLaunchPendingIntent = 8;
    
    static final int TRANSACTION_getMetadata = 27;
    
    static final int TRANSACTION_getPackageName = 6;
    
    static final int TRANSACTION_getPlaybackState = 28;
    
    static final int TRANSACTION_getQueue = 29;
    
    static final int TRANSACTION_getQueueTitle = 30;
    
    static final int TRANSACTION_getRatingType = 32;
    
    static final int TRANSACTION_getTag = 7;
    
    static final int TRANSACTION_getVolumeAttributes = 10;
    
    static final int TRANSACTION_isTransportControlEnabled = 5;
    
    static final int TRANSACTION_next = 20;
    
    static final int TRANSACTION_pause = 18;
    
    static final int TRANSACTION_play = 13;
    
    static final int TRANSACTION_playFromMediaId = 14;
    
    static final int TRANSACTION_playFromSearch = 15;
    
    static final int TRANSACTION_playFromUri = 16;
    
    static final int TRANSACTION_previous = 21;
    
    static final int TRANSACTION_rate = 25;
    
    static final int TRANSACTION_registerCallbackListener = 3;
    
    static final int TRANSACTION_rewind = 23;
    
    static final int TRANSACTION_seekTo = 24;
    
    static final int TRANSACTION_sendCommand = 1;
    
    static final int TRANSACTION_sendCustomAction = 26;
    
    static final int TRANSACTION_sendMediaButton = 2;
    
    static final int TRANSACTION_setVolumeTo = 12;
    
    static final int TRANSACTION_skipToQueueItem = 17;
    
    static final int TRANSACTION_stop = 19;
    
    static final int TRANSACTION_unregisterCallbackListener = 4;
    
    public Stub() {
      attachInterface(this, "android.support.v4.media.session.IMediaSession");
    }
    
    public static IMediaSession asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface("android.support.v4.media.session.IMediaSession");
      return (iInterface != null && iInterface instanceof IMediaSession) ? (IMediaSession)iInterface : new Proxy(param1IBinder);
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      String str1;
      PendingIntent pendingIntent;
      ParcelableVolumeInfo parcelableVolumeInfo;
      MediaMetadataCompat mediaMetadataCompat;
      PlaybackStateCompat playbackStateCompat;
      List<MediaSessionCompat.QueueItem> list;
      CharSequence charSequence;
      Bundle bundle1;
      boolean bool;
      long l;
      Bundle bundle2;
      String str2;
      String str3;
      boolean bool2 = false;
      boolean bool1 = false;
      switch (param1Int1) {
        default:
          return super.onTransact(param1Int1, param1Parcel1, param1Parcel2, param1Int2);
        case 1598968902:
          param1Parcel2.writeString("android.support.v4.media.session.IMediaSession");
          return true;
        case 1:
          param1Parcel1.enforceInterface("android.support.v4.media.session.IMediaSession");
          str3 = param1Parcel1.readString();
          if (param1Parcel1.readInt() != 0) {
            bundle2 = (Bundle)Bundle.CREATOR.createFromParcel(param1Parcel1);
          } else {
            bundle2 = null;
          } 
          if (param1Parcel1.readInt() != 0) {
            MediaSessionCompat.ResultReceiverWrapper resultReceiverWrapper = (MediaSessionCompat.ResultReceiverWrapper)MediaSessionCompat.ResultReceiverWrapper.CREATOR.createFromParcel(param1Parcel1);
            sendCommand(str3, bundle2, resultReceiverWrapper);
            param1Parcel2.writeNoException();
            return true;
          } 
          param1Parcel1 = null;
          sendCommand(str3, bundle2, (MediaSessionCompat.ResultReceiverWrapper)param1Parcel1);
          param1Parcel2.writeNoException();
          return true;
        case 2:
          param1Parcel1.enforceInterface("android.support.v4.media.session.IMediaSession");
          if (param1Parcel1.readInt() != 0) {
            KeyEvent keyEvent = (KeyEvent)KeyEvent.CREATOR.createFromParcel(param1Parcel1);
          } else {
            param1Parcel1 = null;
          } 
          bool = sendMediaButton((KeyEvent)param1Parcel1);
          param1Parcel2.writeNoException();
          param1Int1 = bool1;
          if (bool)
            param1Int1 = 1; 
          param1Parcel2.writeInt(param1Int1);
          return true;
        case 3:
          param1Parcel1.enforceInterface("android.support.v4.media.session.IMediaSession");
          registerCallbackListener(IMediaControllerCallback.Stub.asInterface(param1Parcel1.readStrongBinder()));
          param1Parcel2.writeNoException();
          return true;
        case 4:
          param1Parcel1.enforceInterface("android.support.v4.media.session.IMediaSession");
          unregisterCallbackListener(IMediaControllerCallback.Stub.asInterface(param1Parcel1.readStrongBinder()));
          param1Parcel2.writeNoException();
          return true;
        case 5:
          param1Parcel1.enforceInterface("android.support.v4.media.session.IMediaSession");
          bool = isTransportControlEnabled();
          param1Parcel2.writeNoException();
          param1Int1 = bool2;
          if (bool)
            param1Int1 = 1; 
          param1Parcel2.writeInt(param1Int1);
          return true;
        case 6:
          param1Parcel1.enforceInterface("android.support.v4.media.session.IMediaSession");
          str1 = getPackageName();
          param1Parcel2.writeNoException();
          param1Parcel2.writeString(str1);
          return true;
        case 7:
          str1.enforceInterface("android.support.v4.media.session.IMediaSession");
          str1 = getTag();
          param1Parcel2.writeNoException();
          param1Parcel2.writeString(str1);
          return true;
        case 8:
          str1.enforceInterface("android.support.v4.media.session.IMediaSession");
          pendingIntent = getLaunchPendingIntent();
          param1Parcel2.writeNoException();
          if (pendingIntent != null) {
            param1Parcel2.writeInt(1);
            pendingIntent.writeToParcel(param1Parcel2, 1);
            return true;
          } 
          param1Parcel2.writeInt(0);
          return true;
        case 9:
          pendingIntent.enforceInterface("android.support.v4.media.session.IMediaSession");
          l = getFlags();
          param1Parcel2.writeNoException();
          param1Parcel2.writeLong(l);
          return true;
        case 10:
          pendingIntent.enforceInterface("android.support.v4.media.session.IMediaSession");
          parcelableVolumeInfo = getVolumeAttributes();
          param1Parcel2.writeNoException();
          if (parcelableVolumeInfo != null) {
            param1Parcel2.writeInt(1);
            parcelableVolumeInfo.writeToParcel(param1Parcel2, 1);
            return true;
          } 
          param1Parcel2.writeInt(0);
          return true;
        case 11:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          adjustVolume(parcelableVolumeInfo.readInt(), parcelableVolumeInfo.readInt(), parcelableVolumeInfo.readString());
          param1Parcel2.writeNoException();
          return true;
        case 12:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          setVolumeTo(parcelableVolumeInfo.readInt(), parcelableVolumeInfo.readInt(), parcelableVolumeInfo.readString());
          param1Parcel2.writeNoException();
          return true;
        case 13:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          play();
          param1Parcel2.writeNoException();
          return true;
        case 14:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          str2 = parcelableVolumeInfo.readString();
          if (parcelableVolumeInfo.readInt() != 0) {
            Bundle bundle = (Bundle)Bundle.CREATOR.createFromParcel((Parcel)parcelableVolumeInfo);
            playFromMediaId(str2, bundle);
            param1Parcel2.writeNoException();
            return true;
          } 
          parcelableVolumeInfo = null;
          playFromMediaId(str2, (Bundle)parcelableVolumeInfo);
          param1Parcel2.writeNoException();
          return true;
        case 15:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          str2 = parcelableVolumeInfo.readString();
          if (parcelableVolumeInfo.readInt() != 0) {
            Bundle bundle = (Bundle)Bundle.CREATOR.createFromParcel((Parcel)parcelableVolumeInfo);
            playFromSearch(str2, bundle);
            param1Parcel2.writeNoException();
            return true;
          } 
          parcelableVolumeInfo = null;
          playFromSearch(str2, (Bundle)parcelableVolumeInfo);
          param1Parcel2.writeNoException();
          return true;
        case 16:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          if (parcelableVolumeInfo.readInt() != 0) {
            Uri uri = (Uri)Uri.CREATOR.createFromParcel((Parcel)parcelableVolumeInfo);
          } else {
            str2 = null;
          } 
          if (parcelableVolumeInfo.readInt() != 0) {
            Bundle bundle = (Bundle)Bundle.CREATOR.createFromParcel((Parcel)parcelableVolumeInfo);
            playFromUri((Uri)str2, bundle);
            param1Parcel2.writeNoException();
            return true;
          } 
          parcelableVolumeInfo = null;
          playFromUri((Uri)str2, (Bundle)parcelableVolumeInfo);
          param1Parcel2.writeNoException();
          return true;
        case 17:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          skipToQueueItem(parcelableVolumeInfo.readLong());
          param1Parcel2.writeNoException();
          return true;
        case 18:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          pause();
          param1Parcel2.writeNoException();
          return true;
        case 19:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          stop();
          param1Parcel2.writeNoException();
          return true;
        case 20:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          next();
          param1Parcel2.writeNoException();
          return true;
        case 21:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          previous();
          param1Parcel2.writeNoException();
          return true;
        case 22:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          fastForward();
          param1Parcel2.writeNoException();
          return true;
        case 23:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          rewind();
          param1Parcel2.writeNoException();
          return true;
        case 24:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          seekTo(parcelableVolumeInfo.readLong());
          param1Parcel2.writeNoException();
          return true;
        case 25:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          if (parcelableVolumeInfo.readInt() != 0) {
            RatingCompat ratingCompat = (RatingCompat)RatingCompat.CREATOR.createFromParcel((Parcel)parcelableVolumeInfo);
            rate(ratingCompat);
            param1Parcel2.writeNoException();
            return true;
          } 
          parcelableVolumeInfo = null;
          rate((RatingCompat)parcelableVolumeInfo);
          param1Parcel2.writeNoException();
          return true;
        case 26:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          str2 = parcelableVolumeInfo.readString();
          if (parcelableVolumeInfo.readInt() != 0) {
            Bundle bundle = (Bundle)Bundle.CREATOR.createFromParcel((Parcel)parcelableVolumeInfo);
            sendCustomAction(str2, bundle);
            param1Parcel2.writeNoException();
            return true;
          } 
          parcelableVolumeInfo = null;
          sendCustomAction(str2, (Bundle)parcelableVolumeInfo);
          param1Parcel2.writeNoException();
          return true;
        case 27:
          parcelableVolumeInfo.enforceInterface("android.support.v4.media.session.IMediaSession");
          mediaMetadataCompat = getMetadata();
          param1Parcel2.writeNoException();
          if (mediaMetadataCompat != null) {
            param1Parcel2.writeInt(1);
            mediaMetadataCompat.writeToParcel(param1Parcel2, 1);
            return true;
          } 
          param1Parcel2.writeInt(0);
          return true;
        case 28:
          mediaMetadataCompat.enforceInterface("android.support.v4.media.session.IMediaSession");
          playbackStateCompat = getPlaybackState();
          param1Parcel2.writeNoException();
          if (playbackStateCompat != null) {
            param1Parcel2.writeInt(1);
            playbackStateCompat.writeToParcel(param1Parcel2, 1);
            return true;
          } 
          param1Parcel2.writeInt(0);
          return true;
        case 29:
          playbackStateCompat.enforceInterface("android.support.v4.media.session.IMediaSession");
          list = getQueue();
          param1Parcel2.writeNoException();
          param1Parcel2.writeTypedList(list);
          return true;
        case 30:
          list.enforceInterface("android.support.v4.media.session.IMediaSession");
          charSequence = getQueueTitle();
          param1Parcel2.writeNoException();
          if (charSequence != null) {
            param1Parcel2.writeInt(1);
            TextUtils.writeToParcel(charSequence, param1Parcel2, 1);
            return true;
          } 
          param1Parcel2.writeInt(0);
          return true;
        case 31:
          charSequence.enforceInterface("android.support.v4.media.session.IMediaSession");
          bundle1 = getExtras();
          param1Parcel2.writeNoException();
          if (bundle1 != null) {
            param1Parcel2.writeInt(1);
            bundle1.writeToParcel(param1Parcel2, 1);
            return true;
          } 
          param1Parcel2.writeInt(0);
          return true;
        case 32:
          break;
      } 
      bundle1.enforceInterface("android.support.v4.media.session.IMediaSession");
      param1Int1 = getRatingType();
      param1Parcel2.writeNoException();
      param1Parcel2.writeInt(param1Int1);
      return true;
    }
    
    private static class Proxy implements IMediaSession {
      private IBinder mRemote;
      
      Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public void adjustVolume(int param2Int1, int param2Int2, String param2String) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          parcel1.writeInt(param2Int1);
          parcel1.writeInt(param2Int2);
          parcel1.writeString(param2String);
          this.mRemote.transact(11, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public void fastForward() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(22, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public Bundle getExtras() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(31, parcel1, parcel2, 0);
          parcel2.readException();
          if (parcel2.readInt() != 0)
            return (Bundle)Bundle.CREATOR.createFromParcel(parcel2); 
          return null;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public long getFlags() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(9, parcel1, parcel2, 0);
          parcel2.readException();
          return parcel2.readLong();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public String getInterfaceDescriptor() {
        return "android.support.v4.media.session.IMediaSession";
      }
      
      public PendingIntent getLaunchPendingIntent() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(8, parcel1, parcel2, 0);
          parcel2.readException();
          if (parcel2.readInt() != 0)
            return (PendingIntent)PendingIntent.CREATOR.createFromParcel(parcel2); 
          return null;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public MediaMetadataCompat getMetadata() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(27, parcel1, parcel2, 0);
          parcel2.readException();
          if (parcel2.readInt() != 0)
            return (MediaMetadataCompat)MediaMetadataCompat.CREATOR.createFromParcel(parcel2); 
          return null;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public String getPackageName() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(6, parcel1, parcel2, 0);
          parcel2.readException();
          return parcel2.readString();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public PlaybackStateCompat getPlaybackState() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(28, parcel1, parcel2, 0);
          parcel2.readException();
          if (parcel2.readInt() != 0)
            return (PlaybackStateCompat)PlaybackStateCompat.CREATOR.createFromParcel(parcel2); 
          return null;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public List<MediaSessionCompat.QueueItem> getQueue() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(29, parcel1, parcel2, 0);
          parcel2.readException();
          return parcel2.createTypedArrayList(MediaSessionCompat.QueueItem.CREATOR);
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public CharSequence getQueueTitle() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(30, parcel1, parcel2, 0);
          parcel2.readException();
          if (parcel2.readInt() != 0)
            return (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel2); 
          return null;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public int getRatingType() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(32, parcel1, parcel2, 0);
          parcel2.readException();
          return parcel2.readInt();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public String getTag() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(7, parcel1, parcel2, 0);
          parcel2.readException();
          return parcel2.readString();
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public ParcelableVolumeInfo getVolumeAttributes() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(10, parcel1, parcel2, 0);
          parcel2.readException();
          if (parcel2.readInt() != 0)
            return (ParcelableVolumeInfo)ParcelableVolumeInfo.CREATOR.createFromParcel(parcel2); 
          return null;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public boolean isTransportControlEnabled() throws RemoteException {
        boolean bool = false;
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(5, parcel1, parcel2, 0);
          parcel2.readException();
          int i = parcel2.readInt();
          if (i != 0)
            bool = true; 
          return bool;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void next() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(20, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void pause() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(18, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void play() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(13, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void playFromMediaId(String param2String, Bundle param2Bundle) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          parcel1.writeString(param2String);
          if (param2Bundle != null) {
            parcel1.writeInt(1);
            param2Bundle.writeToParcel(parcel1, 0);
          } else {
            parcel1.writeInt(0);
          } 
          this.mRemote.transact(14, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void playFromSearch(String param2String, Bundle param2Bundle) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          parcel1.writeString(param2String);
          if (param2Bundle != null) {
            parcel1.writeInt(1);
            param2Bundle.writeToParcel(parcel1, 0);
          } else {
            parcel1.writeInt(0);
          } 
          this.mRemote.transact(15, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void playFromUri(Uri param2Uri, Bundle param2Bundle) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          if (param2Uri != null) {
            parcel1.writeInt(1);
            param2Uri.writeToParcel(parcel1, 0);
          } else {
            parcel1.writeInt(0);
          } 
          if (param2Bundle != null) {
            parcel1.writeInt(1);
            param2Bundle.writeToParcel(parcel1, 0);
          } else {
            parcel1.writeInt(0);
          } 
          this.mRemote.transact(16, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void previous() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(21, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void rate(RatingCompat param2RatingCompat) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          if (param2RatingCompat != null) {
            parcel1.writeInt(1);
            param2RatingCompat.writeToParcel(parcel1, 0);
          } else {
            parcel1.writeInt(0);
          } 
          this.mRemote.transact(25, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void registerCallbackListener(IMediaControllerCallback param2IMediaControllerCallback) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          if (param2IMediaControllerCallback != null) {
            IBinder iBinder = param2IMediaControllerCallback.asBinder();
          } else {
            param2IMediaControllerCallback = null;
          } 
          parcel1.writeStrongBinder((IBinder)param2IMediaControllerCallback);
          this.mRemote.transact(3, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void rewind() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(23, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void seekTo(long param2Long) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          parcel1.writeLong(param2Long);
          this.mRemote.transact(24, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void sendCommand(String param2String, Bundle param2Bundle, MediaSessionCompat.ResultReceiverWrapper param2ResultReceiverWrapper) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          parcel1.writeString(param2String);
          if (param2Bundle != null) {
            parcel1.writeInt(1);
            param2Bundle.writeToParcel(parcel1, 0);
          } else {
            parcel1.writeInt(0);
          } 
          if (param2ResultReceiverWrapper != null) {
            parcel1.writeInt(1);
            param2ResultReceiverWrapper.writeToParcel(parcel1, 0);
          } else {
            parcel1.writeInt(0);
          } 
          this.mRemote.transact(1, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void sendCustomAction(String param2String, Bundle param2Bundle) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          parcel1.writeString(param2String);
          if (param2Bundle != null) {
            parcel1.writeInt(1);
            param2Bundle.writeToParcel(parcel1, 0);
          } else {
            parcel1.writeInt(0);
          } 
          this.mRemote.transact(26, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public boolean sendMediaButton(KeyEvent param2KeyEvent) throws RemoteException {
        boolean bool = true;
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          if (param2KeyEvent != null) {
            parcel1.writeInt(1);
            param2KeyEvent.writeToParcel(parcel1, 0);
          } else {
            parcel1.writeInt(0);
          } 
          this.mRemote.transact(2, parcel1, parcel2, 0);
          parcel2.readException();
          int i = parcel2.readInt();
          if (i == 0)
            bool = false; 
          return bool;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void setVolumeTo(int param2Int1, int param2Int2, String param2String) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          parcel1.writeInt(param2Int1);
          parcel1.writeInt(param2Int2);
          parcel1.writeString(param2String);
          this.mRemote.transact(12, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void skipToQueueItem(long param2Long) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          parcel1.writeLong(param2Long);
          this.mRemote.transact(17, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void stop() throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          this.mRemote.transact(19, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
      
      public void unregisterCallbackListener(IMediaControllerCallback param2IMediaControllerCallback) throws RemoteException {
        Parcel parcel1 = Parcel.obtain();
        Parcel parcel2 = Parcel.obtain();
        try {
          parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
          if (param2IMediaControllerCallback != null) {
            IBinder iBinder = param2IMediaControllerCallback.asBinder();
          } else {
            param2IMediaControllerCallback = null;
          } 
          parcel1.writeStrongBinder((IBinder)param2IMediaControllerCallback);
          this.mRemote.transact(4, parcel1, parcel2, 0);
          parcel2.readException();
          return;
        } finally {
          parcel2.recycle();
          parcel1.recycle();
        } 
      }
    }
  }
  
  private static class Proxy implements IMediaSession {
    private IBinder mRemote;
    
    Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public void adjustVolume(int param1Int1, int param1Int2, String param1String) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        parcel1.writeInt(param1Int1);
        parcel1.writeInt(param1Int2);
        parcel1.writeString(param1String);
        this.mRemote.transact(11, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public void fastForward() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(22, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public Bundle getExtras() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(31, parcel1, parcel2, 0);
        parcel2.readException();
        if (parcel2.readInt() != 0)
          return (Bundle)Bundle.CREATOR.createFromParcel(parcel2); 
        return null;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public long getFlags() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(9, parcel1, parcel2, 0);
        parcel2.readException();
        return parcel2.readLong();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public String getInterfaceDescriptor() {
      return "android.support.v4.media.session.IMediaSession";
    }
    
    public PendingIntent getLaunchPendingIntent() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(8, parcel1, parcel2, 0);
        parcel2.readException();
        if (parcel2.readInt() != 0)
          return (PendingIntent)PendingIntent.CREATOR.createFromParcel(parcel2); 
        return null;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public MediaMetadataCompat getMetadata() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(27, parcel1, parcel2, 0);
        parcel2.readException();
        if (parcel2.readInt() != 0)
          return (MediaMetadataCompat)MediaMetadataCompat.CREATOR.createFromParcel(parcel2); 
        return null;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public String getPackageName() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(6, parcel1, parcel2, 0);
        parcel2.readException();
        return parcel2.readString();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public PlaybackStateCompat getPlaybackState() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(28, parcel1, parcel2, 0);
        parcel2.readException();
        if (parcel2.readInt() != 0)
          return (PlaybackStateCompat)PlaybackStateCompat.CREATOR.createFromParcel(parcel2); 
        return null;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public List<MediaSessionCompat.QueueItem> getQueue() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(29, parcel1, parcel2, 0);
        parcel2.readException();
        return parcel2.createTypedArrayList(MediaSessionCompat.QueueItem.CREATOR);
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public CharSequence getQueueTitle() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(30, parcel1, parcel2, 0);
        parcel2.readException();
        if (parcel2.readInt() != 0)
          return (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel2); 
        return null;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public int getRatingType() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(32, parcel1, parcel2, 0);
        parcel2.readException();
        return parcel2.readInt();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public String getTag() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(7, parcel1, parcel2, 0);
        parcel2.readException();
        return parcel2.readString();
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public ParcelableVolumeInfo getVolumeAttributes() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(10, parcel1, parcel2, 0);
        parcel2.readException();
        if (parcel2.readInt() != 0)
          return (ParcelableVolumeInfo)ParcelableVolumeInfo.CREATOR.createFromParcel(parcel2); 
        return null;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public boolean isTransportControlEnabled() throws RemoteException {
      boolean bool = false;
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(5, parcel1, parcel2, 0);
        parcel2.readException();
        int i = parcel2.readInt();
        if (i != 0)
          bool = true; 
        return bool;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void next() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(20, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void pause() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(18, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void play() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(13, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void playFromMediaId(String param1String, Bundle param1Bundle) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        parcel1.writeString(param1String);
        if (param1Bundle != null) {
          parcel1.writeInt(1);
          param1Bundle.writeToParcel(parcel1, 0);
        } else {
          parcel1.writeInt(0);
        } 
        this.mRemote.transact(14, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void playFromSearch(String param1String, Bundle param1Bundle) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        parcel1.writeString(param1String);
        if (param1Bundle != null) {
          parcel1.writeInt(1);
          param1Bundle.writeToParcel(parcel1, 0);
        } else {
          parcel1.writeInt(0);
        } 
        this.mRemote.transact(15, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void playFromUri(Uri param1Uri, Bundle param1Bundle) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        if (param1Uri != null) {
          parcel1.writeInt(1);
          param1Uri.writeToParcel(parcel1, 0);
        } else {
          parcel1.writeInt(0);
        } 
        if (param1Bundle != null) {
          parcel1.writeInt(1);
          param1Bundle.writeToParcel(parcel1, 0);
        } else {
          parcel1.writeInt(0);
        } 
        this.mRemote.transact(16, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void previous() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(21, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void rate(RatingCompat param1RatingCompat) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        if (param1RatingCompat != null) {
          parcel1.writeInt(1);
          param1RatingCompat.writeToParcel(parcel1, 0);
        } else {
          parcel1.writeInt(0);
        } 
        this.mRemote.transact(25, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void registerCallbackListener(IMediaControllerCallback param1IMediaControllerCallback) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        if (param1IMediaControllerCallback != null) {
          IBinder iBinder = param1IMediaControllerCallback.asBinder();
        } else {
          param1IMediaControllerCallback = null;
        } 
        parcel1.writeStrongBinder((IBinder)param1IMediaControllerCallback);
        this.mRemote.transact(3, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void rewind() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(23, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void seekTo(long param1Long) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        parcel1.writeLong(param1Long);
        this.mRemote.transact(24, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void sendCommand(String param1String, Bundle param1Bundle, MediaSessionCompat.ResultReceiverWrapper param1ResultReceiverWrapper) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        parcel1.writeString(param1String);
        if (param1Bundle != null) {
          parcel1.writeInt(1);
          param1Bundle.writeToParcel(parcel1, 0);
        } else {
          parcel1.writeInt(0);
        } 
        if (param1ResultReceiverWrapper != null) {
          parcel1.writeInt(1);
          param1ResultReceiverWrapper.writeToParcel(parcel1, 0);
        } else {
          parcel1.writeInt(0);
        } 
        this.mRemote.transact(1, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void sendCustomAction(String param1String, Bundle param1Bundle) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        parcel1.writeString(param1String);
        if (param1Bundle != null) {
          parcel1.writeInt(1);
          param1Bundle.writeToParcel(parcel1, 0);
        } else {
          parcel1.writeInt(0);
        } 
        this.mRemote.transact(26, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public boolean sendMediaButton(KeyEvent param1KeyEvent) throws RemoteException {
      boolean bool = true;
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        if (param1KeyEvent != null) {
          parcel1.writeInt(1);
          param1KeyEvent.writeToParcel(parcel1, 0);
        } else {
          parcel1.writeInt(0);
        } 
        this.mRemote.transact(2, parcel1, parcel2, 0);
        parcel2.readException();
        int i = parcel2.readInt();
        if (i == 0)
          bool = false; 
        return bool;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void setVolumeTo(int param1Int1, int param1Int2, String param1String) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        parcel1.writeInt(param1Int1);
        parcel1.writeInt(param1Int2);
        parcel1.writeString(param1String);
        this.mRemote.transact(12, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void skipToQueueItem(long param1Long) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        parcel1.writeLong(param1Long);
        this.mRemote.transact(17, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void stop() throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        this.mRemote.transact(19, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
    
    public void unregisterCallbackListener(IMediaControllerCallback param1IMediaControllerCallback) throws RemoteException {
      Parcel parcel1 = Parcel.obtain();
      Parcel parcel2 = Parcel.obtain();
      try {
        parcel1.writeInterfaceToken("android.support.v4.media.session.IMediaSession");
        if (param1IMediaControllerCallback != null) {
          IBinder iBinder = param1IMediaControllerCallback.asBinder();
        } else {
          param1IMediaControllerCallback = null;
        } 
        parcel1.writeStrongBinder((IBinder)param1IMediaControllerCallback);
        this.mRemote.transact(4, parcel1, parcel2, 0);
        parcel2.readException();
        return;
      } finally {
        parcel2.recycle();
        parcel1.recycle();
      } 
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\media\session\IMediaSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */