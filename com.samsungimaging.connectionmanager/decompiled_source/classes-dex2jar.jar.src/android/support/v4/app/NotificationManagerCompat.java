package android.support.v4.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NotificationManagerCompat {
  public static final String ACTION_BIND_SIDE_CHANNEL = "android.support.BIND_NOTIFICATION_SIDE_CHANNEL";
  
  public static final String EXTRA_USE_SIDE_CHANNEL = "android.support.useSideChannel";
  
  private static final Impl IMPL;
  
  static final int MAX_SIDE_CHANNEL_SDK_VERSION = 19;
  
  private static final String SETTING_ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
  
  private static final int SIDE_CHANNEL_BIND_FLAGS;
  
  private static final int SIDE_CHANNEL_RETRY_BASE_INTERVAL_MS = 1000;
  
  private static final int SIDE_CHANNEL_RETRY_MAX_COUNT = 6;
  
  private static final String TAG = "NotifManCompat";
  
  private static Set<String> sEnabledNotificationListenerPackages;
  
  private static String sEnabledNotificationListeners;
  
  private static final Object sEnabledNotificationListenersLock = new Object();
  
  private static final Object sLock;
  
  private static SideChannelManager sSideChannelManager;
  
  private final Context mContext;
  
  private final NotificationManager mNotificationManager;
  
  static {
    sEnabledNotificationListenerPackages = new HashSet<String>();
    sLock = new Object();
    if (Build.VERSION.SDK_INT >= 14) {
      IMPL = new ImplIceCreamSandwich();
    } else if (Build.VERSION.SDK_INT >= 5) {
      IMPL = new ImplEclair();
    } else {
      IMPL = new ImplBase();
    } 
    SIDE_CHANNEL_BIND_FLAGS = IMPL.getSideChannelBindFlags();
  }
  
  private NotificationManagerCompat(Context paramContext) {
    this.mContext = paramContext;
    this.mNotificationManager = (NotificationManager)this.mContext.getSystemService("notification");
  }
  
  public static NotificationManagerCompat from(Context paramContext) {
    return new NotificationManagerCompat(paramContext);
  }
  
  public static Set<String> getEnabledListenerPackages(Context paramContext) {
    String str = Settings.Secure.getString(paramContext.getContentResolver(), "enabled_notification_listeners");
    if (str != null && !str.equals(sEnabledNotificationListeners)) {
      String[] arrayOfString = str.split(":");
      HashSet<String> hashSet = new HashSet(arrayOfString.length);
      int j = arrayOfString.length;
      for (int i = 0; i < j; i++) {
        ComponentName componentName = ComponentName.unflattenFromString(arrayOfString[i]);
        if (componentName != null)
          hashSet.add(componentName.getPackageName()); 
      } 
      synchronized (sEnabledNotificationListenersLock) {
        sEnabledNotificationListenerPackages = hashSet;
        sEnabledNotificationListeners = str;
        return sEnabledNotificationListenerPackages;
      } 
    } 
    return sEnabledNotificationListenerPackages;
  }
  
  private void pushSideChannelQueue(Task paramTask) {
    synchronized (sLock) {
      if (sSideChannelManager == null)
        sSideChannelManager = new SideChannelManager(this.mContext.getApplicationContext()); 
      sSideChannelManager.queueTask(paramTask);
      return;
    } 
  }
  
  private static boolean useSideChannelForNotification(Notification paramNotification) {
    Bundle bundle = NotificationCompat.getExtras(paramNotification);
    return (bundle != null && bundle.getBoolean("android.support.useSideChannel"));
  }
  
  public void cancel(int paramInt) {
    cancel(null, paramInt);
  }
  
  public void cancel(String paramString, int paramInt) {
    IMPL.cancelNotification(this.mNotificationManager, paramString, paramInt);
    if (Build.VERSION.SDK_INT <= 19)
      pushSideChannelQueue(new CancelTask(this.mContext.getPackageName(), paramInt, paramString)); 
  }
  
  public void cancelAll() {
    this.mNotificationManager.cancelAll();
    if (Build.VERSION.SDK_INT <= 19)
      pushSideChannelQueue(new CancelTask(this.mContext.getPackageName())); 
  }
  
  public void notify(int paramInt, Notification paramNotification) {
    notify(null, paramInt, paramNotification);
  }
  
  public void notify(String paramString, int paramInt, Notification paramNotification) {
    if (useSideChannelForNotification(paramNotification)) {
      pushSideChannelQueue(new NotifyTask(this.mContext.getPackageName(), paramInt, paramString, paramNotification));
      IMPL.cancelNotification(this.mNotificationManager, paramString, paramInt);
      return;
    } 
    IMPL.postNotification(this.mNotificationManager, paramString, paramInt, paramNotification);
  }
  
  private static class CancelTask implements Task {
    final boolean all;
    
    final int id;
    
    final String packageName;
    
    final String tag;
    
    public CancelTask(String param1String) {
      this.packageName = param1String;
      this.id = 0;
      this.tag = null;
      this.all = true;
    }
    
    public CancelTask(String param1String1, int param1Int, String param1String2) {
      this.packageName = param1String1;
      this.id = param1Int;
      this.tag = param1String2;
      this.all = false;
    }
    
    public void send(INotificationSideChannel param1INotificationSideChannel) throws RemoteException {
      if (this.all) {
        param1INotificationSideChannel.cancelAll(this.packageName);
        return;
      } 
      param1INotificationSideChannel.cancel(this.packageName, this.id, this.tag);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder("CancelTask[");
      stringBuilder.append("packageName:").append(this.packageName);
      stringBuilder.append(", id:").append(this.id);
      stringBuilder.append(", tag:").append(this.tag);
      stringBuilder.append(", all:").append(this.all);
      stringBuilder.append("]");
      return stringBuilder.toString();
    }
  }
  
  static interface Impl {
    void cancelNotification(NotificationManager param1NotificationManager, String param1String, int param1Int);
    
    int getSideChannelBindFlags();
    
    void postNotification(NotificationManager param1NotificationManager, String param1String, int param1Int, Notification param1Notification);
  }
  
  static class ImplBase implements Impl {
    public void cancelNotification(NotificationManager param1NotificationManager, String param1String, int param1Int) {
      param1NotificationManager.cancel(param1Int);
    }
    
    public int getSideChannelBindFlags() {
      return 1;
    }
    
    public void postNotification(NotificationManager param1NotificationManager, String param1String, int param1Int, Notification param1Notification) {
      param1NotificationManager.notify(param1Int, param1Notification);
    }
  }
  
  static class ImplEclair extends ImplBase {
    public void cancelNotification(NotificationManager param1NotificationManager, String param1String, int param1Int) {
      NotificationManagerCompatEclair.cancelNotification(param1NotificationManager, param1String, param1Int);
    }
    
    public void postNotification(NotificationManager param1NotificationManager, String param1String, int param1Int, Notification param1Notification) {
      NotificationManagerCompatEclair.postNotification(param1NotificationManager, param1String, param1Int, param1Notification);
    }
  }
  
  static class ImplIceCreamSandwich extends ImplEclair {
    public int getSideChannelBindFlags() {
      return 33;
    }
  }
  
  private static class NotifyTask implements Task {
    final int id;
    
    final Notification notif;
    
    final String packageName;
    
    final String tag;
    
    public NotifyTask(String param1String1, int param1Int, String param1String2, Notification param1Notification) {
      this.packageName = param1String1;
      this.id = param1Int;
      this.tag = param1String2;
      this.notif = param1Notification;
    }
    
    public void send(INotificationSideChannel param1INotificationSideChannel) throws RemoteException {
      param1INotificationSideChannel.notify(this.packageName, this.id, this.tag, this.notif);
    }
    
    public String toString() {
      StringBuilder stringBuilder = new StringBuilder("NotifyTask[");
      stringBuilder.append("packageName:").append(this.packageName);
      stringBuilder.append(", id:").append(this.id);
      stringBuilder.append(", tag:").append(this.tag);
      stringBuilder.append("]");
      return stringBuilder.toString();
    }
  }
  
  private static class ServiceConnectedEvent {
    final ComponentName componentName;
    
    final IBinder iBinder;
    
    public ServiceConnectedEvent(ComponentName param1ComponentName, IBinder param1IBinder) {
      this.componentName = param1ComponentName;
      this.iBinder = param1IBinder;
    }
  }
  
  private static class SideChannelManager implements Handler.Callback, ServiceConnection {
    private static final String KEY_BINDER = "binder";
    
    private static final int MSG_QUEUE_TASK = 0;
    
    private static final int MSG_RETRY_LISTENER_QUEUE = 3;
    
    private static final int MSG_SERVICE_CONNECTED = 1;
    
    private static final int MSG_SERVICE_DISCONNECTED = 2;
    
    private Set<String> mCachedEnabledPackages = new HashSet<String>();
    
    private final Context mContext;
    
    private final Handler mHandler;
    
    private final HandlerThread mHandlerThread;
    
    private final Map<ComponentName, ListenerRecord> mRecordMap = new HashMap<ComponentName, ListenerRecord>();
    
    public SideChannelManager(Context param1Context) {
      this.mContext = param1Context;
      this.mHandlerThread = new HandlerThread("NotificationManagerCompat");
      this.mHandlerThread.start();
      this.mHandler = new Handler(this.mHandlerThread.getLooper(), this);
    }
    
    private boolean ensureServiceBound(ListenerRecord param1ListenerRecord) {
      if (param1ListenerRecord.bound)
        return true; 
      Intent intent = (new Intent("android.support.BIND_NOTIFICATION_SIDE_CHANNEL")).setComponent(param1ListenerRecord.componentName);
      param1ListenerRecord.bound = this.mContext.bindService(intent, this, NotificationManagerCompat.SIDE_CHANNEL_BIND_FLAGS);
      if (param1ListenerRecord.bound) {
        param1ListenerRecord.retryCount = 0;
        return param1ListenerRecord.bound;
      } 
      Log.w("NotifManCompat", "Unable to bind to listener " + param1ListenerRecord.componentName);
      this.mContext.unbindService(this);
      return param1ListenerRecord.bound;
    }
    
    private void ensureServiceUnbound(ListenerRecord param1ListenerRecord) {
      if (param1ListenerRecord.bound) {
        this.mContext.unbindService(this);
        param1ListenerRecord.bound = false;
      } 
      param1ListenerRecord.service = null;
    }
    
    private void handleQueueTask(NotificationManagerCompat.Task param1Task) {
      updateListenerMap();
      for (ListenerRecord listenerRecord : this.mRecordMap.values()) {
        listenerRecord.taskQueue.add(param1Task);
        processListenerQueue(listenerRecord);
      } 
    }
    
    private void handleRetryListenerQueue(ComponentName param1ComponentName) {
      ListenerRecord listenerRecord = this.mRecordMap.get(param1ComponentName);
      if (listenerRecord != null)
        processListenerQueue(listenerRecord); 
    }
    
    private void handleServiceConnected(ComponentName param1ComponentName, IBinder param1IBinder) {
      ListenerRecord listenerRecord = this.mRecordMap.get(param1ComponentName);
      if (listenerRecord != null) {
        listenerRecord.service = INotificationSideChannel.Stub.asInterface(param1IBinder);
        listenerRecord.retryCount = 0;
        processListenerQueue(listenerRecord);
      } 
    }
    
    private void handleServiceDisconnected(ComponentName param1ComponentName) {
      ListenerRecord listenerRecord = this.mRecordMap.get(param1ComponentName);
      if (listenerRecord != null)
        ensureServiceUnbound(listenerRecord); 
    }
    
    private void processListenerQueue(ListenerRecord param1ListenerRecord) {
      if (Log.isLoggable("NotifManCompat", 3))
        Log.d("NotifManCompat", "Processing component " + param1ListenerRecord.componentName + ", " + param1ListenerRecord.taskQueue.size() + " queued tasks"); 
      if (!param1ListenerRecord.taskQueue.isEmpty()) {
        if (!ensureServiceBound(param1ListenerRecord) || param1ListenerRecord.service == null) {
          scheduleListenerRetry(param1ListenerRecord);
          return;
        } 
        while (true) {
          NotificationManagerCompat.Task task = param1ListenerRecord.taskQueue.peek();
          if (task != null)
            try {
              if (Log.isLoggable("NotifManCompat", 3))
                Log.d("NotifManCompat", "Sending task " + task); 
              task.send(param1ListenerRecord.service);
              param1ListenerRecord.taskQueue.remove();
              continue;
            } catch (DeadObjectException deadObjectException) {
              if (Log.isLoggable("NotifManCompat", 3))
                Log.d("NotifManCompat", "Remote service has died: " + param1ListenerRecord.componentName); 
            } catch (RemoteException remoteException) {
              Log.w("NotifManCompat", "RemoteException communicating with " + param1ListenerRecord.componentName, (Throwable)remoteException);
            }  
          if (!param1ListenerRecord.taskQueue.isEmpty()) {
            scheduleListenerRetry(param1ListenerRecord);
            return;
          } 
          return;
        } 
      } 
    }
    
    private void scheduleListenerRetry(ListenerRecord param1ListenerRecord) {
      if (this.mHandler.hasMessages(3, param1ListenerRecord.componentName))
        return; 
      param1ListenerRecord.retryCount++;
      if (param1ListenerRecord.retryCount > 6) {
        Log.w("NotifManCompat", "Giving up on delivering " + param1ListenerRecord.taskQueue.size() + " tasks to " + param1ListenerRecord.componentName + " after " + param1ListenerRecord.retryCount + " retries");
        param1ListenerRecord.taskQueue.clear();
        return;
      } 
      int i = (1 << param1ListenerRecord.retryCount - 1) * 1000;
      if (Log.isLoggable("NotifManCompat", 3))
        Log.d("NotifManCompat", "Scheduling retry for " + i + " ms"); 
      Message message = this.mHandler.obtainMessage(3, param1ListenerRecord.componentName);
      this.mHandler.sendMessageDelayed(message, i);
    }
    
    private void updateListenerMap() {
      Set<String> set = NotificationManagerCompat.getEnabledListenerPackages(this.mContext);
      if (!set.equals(this.mCachedEnabledPackages)) {
        this.mCachedEnabledPackages = set;
        List list = this.mContext.getPackageManager().queryIntentServices((new Intent()).setAction("android.support.BIND_NOTIFICATION_SIDE_CHANNEL"), 4);
        HashSet<ComponentName> hashSet = new HashSet();
        for (ResolveInfo resolveInfo : list) {
          if (set.contains(resolveInfo.serviceInfo.packageName)) {
            ComponentName componentName = new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
            if (resolveInfo.serviceInfo.permission != null) {
              Log.w("NotifManCompat", "Permission present on component " + componentName + ", not adding listener record.");
              continue;
            } 
            hashSet.add(componentName);
          } 
        } 
        for (ComponentName componentName : hashSet) {
          if (!this.mRecordMap.containsKey(componentName)) {
            if (Log.isLoggable("NotifManCompat", 3))
              Log.d("NotifManCompat", "Adding listener record for " + componentName); 
            this.mRecordMap.put(componentName, new ListenerRecord(componentName));
          } 
        } 
        Iterator<Map.Entry> iterator = this.mRecordMap.entrySet().iterator();
        while (true) {
          if (iterator.hasNext()) {
            Map.Entry entry = iterator.next();
            if (!hashSet.contains(entry.getKey())) {
              if (Log.isLoggable("NotifManCompat", 3))
                Log.d("NotifManCompat", "Removing listener record for " + entry.getKey()); 
              ensureServiceUnbound((ListenerRecord)entry.getValue());
              iterator.remove();
            } 
            continue;
          } 
          return;
        } 
      } 
    }
    
    public boolean handleMessage(Message param1Message) {
      NotificationManagerCompat.ServiceConnectedEvent serviceConnectedEvent;
      switch (param1Message.what) {
        default:
          return false;
        case 0:
          handleQueueTask((NotificationManagerCompat.Task)param1Message.obj);
          return true;
        case 1:
          serviceConnectedEvent = (NotificationManagerCompat.ServiceConnectedEvent)param1Message.obj;
          handleServiceConnected(serviceConnectedEvent.componentName, serviceConnectedEvent.iBinder);
          return true;
        case 2:
          handleServiceDisconnected((ComponentName)((Message)serviceConnectedEvent).obj);
          return true;
        case 3:
          break;
      } 
      handleRetryListenerQueue((ComponentName)((Message)serviceConnectedEvent).obj);
      return true;
    }
    
    public void onServiceConnected(ComponentName param1ComponentName, IBinder param1IBinder) {
      if (Log.isLoggable("NotifManCompat", 3))
        Log.d("NotifManCompat", "Connected to service " + param1ComponentName); 
      this.mHandler.obtainMessage(1, new NotificationManagerCompat.ServiceConnectedEvent(param1ComponentName, param1IBinder)).sendToTarget();
    }
    
    public void onServiceDisconnected(ComponentName param1ComponentName) {
      if (Log.isLoggable("NotifManCompat", 3))
        Log.d("NotifManCompat", "Disconnected from service " + param1ComponentName); 
      this.mHandler.obtainMessage(2, param1ComponentName).sendToTarget();
    }
    
    public void queueTask(NotificationManagerCompat.Task param1Task) {
      this.mHandler.obtainMessage(0, param1Task).sendToTarget();
    }
    
    private static class ListenerRecord {
      public boolean bound = false;
      
      public final ComponentName componentName;
      
      public int retryCount = 0;
      
      public INotificationSideChannel service;
      
      public LinkedList<NotificationManagerCompat.Task> taskQueue = new LinkedList<NotificationManagerCompat.Task>();
      
      public ListenerRecord(ComponentName param2ComponentName) {
        this.componentName = param2ComponentName;
      }
    }
  }
  
  private static class ListenerRecord {
    public boolean bound = false;
    
    public final ComponentName componentName;
    
    public int retryCount = 0;
    
    public INotificationSideChannel service;
    
    public LinkedList<NotificationManagerCompat.Task> taskQueue = new LinkedList<NotificationManagerCompat.Task>();
    
    public ListenerRecord(ComponentName param1ComponentName) {
      this.componentName = param1ComponentName;
    }
  }
  
  private static interface Task {
    void send(INotificationSideChannel param1INotificationSideChannel) throws RemoteException;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\app\NotificationManagerCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */