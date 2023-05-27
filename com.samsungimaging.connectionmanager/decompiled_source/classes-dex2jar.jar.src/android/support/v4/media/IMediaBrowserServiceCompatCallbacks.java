package android.support.v4.media;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v4.media.session.MediaSessionCompat;
import java.util.List;

public interface IMediaBrowserServiceCompatCallbacks extends IInterface {
  void onConnect(String paramString, MediaSessionCompat.Token paramToken, Bundle paramBundle) throws RemoteException;
  
  void onConnectFailed() throws RemoteException;
  
  void onLoadChildren(String paramString, List paramList) throws RemoteException;
  
  public static abstract class Stub extends Binder implements IMediaBrowserServiceCompatCallbacks {
    private static final String DESCRIPTOR = "android.support.v4.media.IMediaBrowserServiceCompatCallbacks";
    
    static final int TRANSACTION_onConnect = 1;
    
    static final int TRANSACTION_onConnectFailed = 2;
    
    static final int TRANSACTION_onLoadChildren = 3;
    
    public Stub() {
      attachInterface(this, "android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
    }
    
    public static IMediaBrowserServiceCompatCallbacks asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
      return (iInterface != null && iInterface instanceof IMediaBrowserServiceCompatCallbacks) ? (IMediaBrowserServiceCompatCallbacks)iInterface : new Proxy(param1IBinder);
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      String str;
      switch (param1Int1) {
        default:
          return super.onTransact(param1Int1, param1Parcel1, param1Parcel2, param1Int2);
        case 1598968902:
          param1Parcel2.writeString("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
          return true;
        case 1:
          param1Parcel1.enforceInterface("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
          str = param1Parcel1.readString();
          if (param1Parcel1.readInt() != 0) {
            MediaSessionCompat.Token token = (MediaSessionCompat.Token)MediaSessionCompat.Token.CREATOR.createFromParcel(param1Parcel1);
          } else {
            param1Parcel2 = null;
          } 
          if (param1Parcel1.readInt() != 0) {
            Bundle bundle = (Bundle)Bundle.CREATOR.createFromParcel(param1Parcel1);
            onConnect(str, (MediaSessionCompat.Token)param1Parcel2, bundle);
            return true;
          } 
          param1Parcel1 = null;
          onConnect(str, (MediaSessionCompat.Token)param1Parcel2, (Bundle)param1Parcel1);
          return true;
        case 2:
          param1Parcel1.enforceInterface("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
          onConnectFailed();
          return true;
        case 3:
          break;
      } 
      param1Parcel1.enforceInterface("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
      onLoadChildren(param1Parcel1.readString(), param1Parcel1.readArrayList(getClass().getClassLoader()));
      return true;
    }
    
    private static class Proxy implements IMediaBrowserServiceCompatCallbacks {
      private IBinder mRemote;
      
      Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public String getInterfaceDescriptor() {
        return "android.support.v4.media.IMediaBrowserServiceCompatCallbacks";
      }
      
      public void onConnect(String param2String, MediaSessionCompat.Token param2Token, Bundle param2Bundle) throws RemoteException {
        Parcel parcel = Parcel.obtain();
        try {
          parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
          parcel.writeString(param2String);
          if (param2Token != null) {
            parcel.writeInt(1);
            param2Token.writeToParcel(parcel, 0);
          } else {
            parcel.writeInt(0);
          } 
          if (param2Bundle != null) {
            parcel.writeInt(1);
            param2Bundle.writeToParcel(parcel, 0);
          } else {
            parcel.writeInt(0);
          } 
          this.mRemote.transact(1, parcel, null, 1);
          return;
        } finally {
          parcel.recycle();
        } 
      }
      
      public void onConnectFailed() throws RemoteException {
        Parcel parcel = Parcel.obtain();
        try {
          parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
          this.mRemote.transact(2, parcel, null, 1);
          return;
        } finally {
          parcel.recycle();
        } 
      }
      
      public void onLoadChildren(String param2String, List param2List) throws RemoteException {
        Parcel parcel = Parcel.obtain();
        try {
          parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
          parcel.writeString(param2String);
          parcel.writeList(param2List);
          this.mRemote.transact(3, parcel, null, 1);
          return;
        } finally {
          parcel.recycle();
        } 
      }
    }
  }
  
  private static class Proxy implements IMediaBrowserServiceCompatCallbacks {
    private IBinder mRemote;
    
    Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public String getInterfaceDescriptor() {
      return "android.support.v4.media.IMediaBrowserServiceCompatCallbacks";
    }
    
    public void onConnect(String param1String, MediaSessionCompat.Token param1Token, Bundle param1Bundle) throws RemoteException {
      Parcel parcel = Parcel.obtain();
      try {
        parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
        parcel.writeString(param1String);
        if (param1Token != null) {
          parcel.writeInt(1);
          param1Token.writeToParcel(parcel, 0);
        } else {
          parcel.writeInt(0);
        } 
        if (param1Bundle != null) {
          parcel.writeInt(1);
          param1Bundle.writeToParcel(parcel, 0);
        } else {
          parcel.writeInt(0);
        } 
        this.mRemote.transact(1, parcel, null, 1);
        return;
      } finally {
        parcel.recycle();
      } 
    }
    
    public void onConnectFailed() throws RemoteException {
      Parcel parcel = Parcel.obtain();
      try {
        parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
        this.mRemote.transact(2, parcel, null, 1);
        return;
      } finally {
        parcel.recycle();
      } 
    }
    
    public void onLoadChildren(String param1String, List param1List) throws RemoteException {
      Parcel parcel = Parcel.obtain();
      try {
        parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompatCallbacks");
        parcel.writeString(param1String);
        parcel.writeList(param1List);
        this.mRemote.transact(3, parcel, null, 1);
        return;
      } finally {
        parcel.recycle();
      } 
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\media\IMediaBrowserServiceCompatCallbacks.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */