package android.support.v4.media;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v4.os.ResultReceiver;

public interface IMediaBrowserServiceCompat extends IInterface {
  void addSubscription(String paramString, IMediaBrowserServiceCompatCallbacks paramIMediaBrowserServiceCompatCallbacks) throws RemoteException;
  
  void connect(String paramString, Bundle paramBundle, IMediaBrowserServiceCompatCallbacks paramIMediaBrowserServiceCompatCallbacks) throws RemoteException;
  
  void disconnect(IMediaBrowserServiceCompatCallbacks paramIMediaBrowserServiceCompatCallbacks) throws RemoteException;
  
  void getMediaItem(String paramString, ResultReceiver paramResultReceiver) throws RemoteException;
  
  void removeSubscription(String paramString, IMediaBrowserServiceCompatCallbacks paramIMediaBrowserServiceCompatCallbacks) throws RemoteException;
  
  public static abstract class Stub extends Binder implements IMediaBrowserServiceCompat {
    private static final String DESCRIPTOR = "android.support.v4.media.IMediaBrowserServiceCompat";
    
    static final int TRANSACTION_addSubscription = 3;
    
    static final int TRANSACTION_connect = 1;
    
    static final int TRANSACTION_disconnect = 2;
    
    static final int TRANSACTION_getMediaItem = 5;
    
    static final int TRANSACTION_removeSubscription = 4;
    
    public Stub() {
      attachInterface(this, "android.support.v4.media.IMediaBrowserServiceCompat");
    }
    
    public static IMediaBrowserServiceCompat asInterface(IBinder param1IBinder) {
      if (param1IBinder == null)
        return null; 
      IInterface iInterface = param1IBinder.queryLocalInterface("android.support.v4.media.IMediaBrowserServiceCompat");
      return (iInterface != null && iInterface instanceof IMediaBrowserServiceCompat) ? (IMediaBrowserServiceCompat)iInterface : new Proxy(param1IBinder);
    }
    
    public IBinder asBinder() {
      return (IBinder)this;
    }
    
    public boolean onTransact(int param1Int1, Parcel param1Parcel1, Parcel param1Parcel2, int param1Int2) throws RemoteException {
      String str2;
      switch (param1Int1) {
        default:
          return super.onTransact(param1Int1, param1Parcel1, param1Parcel2, param1Int2);
        case 1598968902:
          param1Parcel2.writeString("android.support.v4.media.IMediaBrowserServiceCompat");
          return true;
        case 1:
          param1Parcel1.enforceInterface("android.support.v4.media.IMediaBrowserServiceCompat");
          str2 = param1Parcel1.readString();
          if (param1Parcel1.readInt() != 0) {
            Bundle bundle = (Bundle)Bundle.CREATOR.createFromParcel(param1Parcel1);
            connect(str2, bundle, IMediaBrowserServiceCompatCallbacks.Stub.asInterface(param1Parcel1.readStrongBinder()));
            return true;
          } 
          param1Parcel2 = null;
          connect(str2, (Bundle)param1Parcel2, IMediaBrowserServiceCompatCallbacks.Stub.asInterface(param1Parcel1.readStrongBinder()));
          return true;
        case 2:
          param1Parcel1.enforceInterface("android.support.v4.media.IMediaBrowserServiceCompat");
          disconnect(IMediaBrowserServiceCompatCallbacks.Stub.asInterface(param1Parcel1.readStrongBinder()));
          return true;
        case 3:
          param1Parcel1.enforceInterface("android.support.v4.media.IMediaBrowserServiceCompat");
          addSubscription(param1Parcel1.readString(), IMediaBrowserServiceCompatCallbacks.Stub.asInterface(param1Parcel1.readStrongBinder()));
          return true;
        case 4:
          param1Parcel1.enforceInterface("android.support.v4.media.IMediaBrowserServiceCompat");
          removeSubscription(param1Parcel1.readString(), IMediaBrowserServiceCompatCallbacks.Stub.asInterface(param1Parcel1.readStrongBinder()));
          return true;
        case 5:
          break;
      } 
      param1Parcel1.enforceInterface("android.support.v4.media.IMediaBrowserServiceCompat");
      String str1 = param1Parcel1.readString();
      if (param1Parcel1.readInt() != 0) {
        ResultReceiver resultReceiver = (ResultReceiver)ResultReceiver.CREATOR.createFromParcel(param1Parcel1);
        getMediaItem(str1, resultReceiver);
        return true;
      } 
      param1Parcel1 = null;
      getMediaItem(str1, (ResultReceiver)param1Parcel1);
      return true;
    }
    
    private static class Proxy implements IMediaBrowserServiceCompat {
      private IBinder mRemote;
      
      Proxy(IBinder param2IBinder) {
        this.mRemote = param2IBinder;
      }
      
      public void addSubscription(String param2String, IMediaBrowserServiceCompatCallbacks param2IMediaBrowserServiceCompatCallbacks) throws RemoteException {
        String str = null;
        Parcel parcel = Parcel.obtain();
        try {
          IBinder iBinder;
          parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompat");
          parcel.writeString(param2String);
          param2String = str;
          if (param2IMediaBrowserServiceCompatCallbacks != null)
            iBinder = param2IMediaBrowserServiceCompatCallbacks.asBinder(); 
          parcel.writeStrongBinder(iBinder);
          this.mRemote.transact(3, parcel, null, 1);
          return;
        } finally {
          parcel.recycle();
        } 
      }
      
      public IBinder asBinder() {
        return this.mRemote;
      }
      
      public void connect(String param2String, Bundle param2Bundle, IMediaBrowserServiceCompatCallbacks param2IMediaBrowserServiceCompatCallbacks) throws RemoteException {
        String str = null;
        Parcel parcel = Parcel.obtain();
        try {
          IBinder iBinder;
          parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompat");
          parcel.writeString(param2String);
          if (param2Bundle != null) {
            parcel.writeInt(1);
            param2Bundle.writeToParcel(parcel, 0);
          } else {
            parcel.writeInt(0);
          } 
          param2String = str;
          if (param2IMediaBrowserServiceCompatCallbacks != null)
            iBinder = param2IMediaBrowserServiceCompatCallbacks.asBinder(); 
          parcel.writeStrongBinder(iBinder);
          this.mRemote.transact(1, parcel, null, 1);
          return;
        } finally {
          parcel.recycle();
        } 
      }
      
      public void disconnect(IMediaBrowserServiceCompatCallbacks param2IMediaBrowserServiceCompatCallbacks) throws RemoteException {
        IBinder iBinder = null;
        Parcel parcel = Parcel.obtain();
        try {
          parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompat");
          if (param2IMediaBrowserServiceCompatCallbacks != null)
            iBinder = param2IMediaBrowserServiceCompatCallbacks.asBinder(); 
          parcel.writeStrongBinder(iBinder);
          this.mRemote.transact(2, parcel, null, 1);
          return;
        } finally {
          parcel.recycle();
        } 
      }
      
      public String getInterfaceDescriptor() {
        return "android.support.v4.media.IMediaBrowserServiceCompat";
      }
      
      public void getMediaItem(String param2String, ResultReceiver param2ResultReceiver) throws RemoteException {
        Parcel parcel = Parcel.obtain();
        try {
          parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompat");
          parcel.writeString(param2String);
          if (param2ResultReceiver != null) {
            parcel.writeInt(1);
            param2ResultReceiver.writeToParcel(parcel, 0);
          } else {
            parcel.writeInt(0);
          } 
          this.mRemote.transact(5, parcel, null, 1);
          return;
        } finally {
          parcel.recycle();
        } 
      }
      
      public void removeSubscription(String param2String, IMediaBrowserServiceCompatCallbacks param2IMediaBrowserServiceCompatCallbacks) throws RemoteException {
        String str = null;
        Parcel parcel = Parcel.obtain();
        try {
          IBinder iBinder;
          parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompat");
          parcel.writeString(param2String);
          param2String = str;
          if (param2IMediaBrowserServiceCompatCallbacks != null)
            iBinder = param2IMediaBrowserServiceCompatCallbacks.asBinder(); 
          parcel.writeStrongBinder(iBinder);
          this.mRemote.transact(4, parcel, null, 1);
          return;
        } finally {
          parcel.recycle();
        } 
      }
    }
  }
  
  private static class Proxy implements IMediaBrowserServiceCompat {
    private IBinder mRemote;
    
    Proxy(IBinder param1IBinder) {
      this.mRemote = param1IBinder;
    }
    
    public void addSubscription(String param1String, IMediaBrowserServiceCompatCallbacks param1IMediaBrowserServiceCompatCallbacks) throws RemoteException {
      String str = null;
      Parcel parcel = Parcel.obtain();
      try {
        IBinder iBinder;
        parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompat");
        parcel.writeString(param1String);
        param1String = str;
        if (param1IMediaBrowserServiceCompatCallbacks != null)
          iBinder = param1IMediaBrowserServiceCompatCallbacks.asBinder(); 
        parcel.writeStrongBinder(iBinder);
        this.mRemote.transact(3, parcel, null, 1);
        return;
      } finally {
        parcel.recycle();
      } 
    }
    
    public IBinder asBinder() {
      return this.mRemote;
    }
    
    public void connect(String param1String, Bundle param1Bundle, IMediaBrowserServiceCompatCallbacks param1IMediaBrowserServiceCompatCallbacks) throws RemoteException {
      String str = null;
      Parcel parcel = Parcel.obtain();
      try {
        IBinder iBinder;
        parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompat");
        parcel.writeString(param1String);
        if (param1Bundle != null) {
          parcel.writeInt(1);
          param1Bundle.writeToParcel(parcel, 0);
        } else {
          parcel.writeInt(0);
        } 
        param1String = str;
        if (param1IMediaBrowserServiceCompatCallbacks != null)
          iBinder = param1IMediaBrowserServiceCompatCallbacks.asBinder(); 
        parcel.writeStrongBinder(iBinder);
        this.mRemote.transact(1, parcel, null, 1);
        return;
      } finally {
        parcel.recycle();
      } 
    }
    
    public void disconnect(IMediaBrowserServiceCompatCallbacks param1IMediaBrowserServiceCompatCallbacks) throws RemoteException {
      IBinder iBinder = null;
      Parcel parcel = Parcel.obtain();
      try {
        parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompat");
        if (param1IMediaBrowserServiceCompatCallbacks != null)
          iBinder = param1IMediaBrowserServiceCompatCallbacks.asBinder(); 
        parcel.writeStrongBinder(iBinder);
        this.mRemote.transact(2, parcel, null, 1);
        return;
      } finally {
        parcel.recycle();
      } 
    }
    
    public String getInterfaceDescriptor() {
      return "android.support.v4.media.IMediaBrowserServiceCompat";
    }
    
    public void getMediaItem(String param1String, ResultReceiver param1ResultReceiver) throws RemoteException {
      Parcel parcel = Parcel.obtain();
      try {
        parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompat");
        parcel.writeString(param1String);
        if (param1ResultReceiver != null) {
          parcel.writeInt(1);
          param1ResultReceiver.writeToParcel(parcel, 0);
        } else {
          parcel.writeInt(0);
        } 
        this.mRemote.transact(5, parcel, null, 1);
        return;
      } finally {
        parcel.recycle();
      } 
    }
    
    public void removeSubscription(String param1String, IMediaBrowserServiceCompatCallbacks param1IMediaBrowserServiceCompatCallbacks) throws RemoteException {
      String str = null;
      Parcel parcel = Parcel.obtain();
      try {
        IBinder iBinder;
        parcel.writeInterfaceToken("android.support.v4.media.IMediaBrowserServiceCompat");
        parcel.writeString(param1String);
        param1String = str;
        if (param1IMediaBrowserServiceCompatCallbacks != null)
          iBinder = param1IMediaBrowserServiceCompatCallbacks.asBinder(); 
        parcel.writeStrongBinder(iBinder);
        this.mRemote.transact(4, parcel, null, 1);
        return;
      } finally {
        parcel.recycle();
      } 
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\media\IMediaBrowserServiceCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */