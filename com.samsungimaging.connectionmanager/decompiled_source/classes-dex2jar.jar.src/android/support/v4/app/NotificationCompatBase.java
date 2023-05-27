package android.support.v4.app;

import android.app.PendingIntent;
import android.os.Bundle;

public class NotificationCompatBase {
  public static abstract class Action {
    public abstract PendingIntent getActionIntent();
    
    public abstract Bundle getExtras();
    
    public abstract int getIcon();
    
    public abstract RemoteInputCompatBase.RemoteInput[] getRemoteInputs();
    
    public abstract CharSequence getTitle();
    
    public static interface Factory {
      NotificationCompatBase.Action build(int param2Int, CharSequence param2CharSequence, PendingIntent param2PendingIntent, Bundle param2Bundle, RemoteInputCompatBase.RemoteInput[] param2ArrayOfRemoteInput);
      
      NotificationCompatBase.Action[] newArray(int param2Int);
    }
  }
  
  public static interface Factory {
    NotificationCompatBase.Action build(int param1Int, CharSequence param1CharSequence, PendingIntent param1PendingIntent, Bundle param1Bundle, RemoteInputCompatBase.RemoteInput[] param1ArrayOfRemoteInput);
    
    NotificationCompatBase.Action[] newArray(int param1Int);
  }
  
  public static abstract class UnreadConversation {
    abstract long getLatestTimestamp();
    
    abstract String[] getMessages();
    
    abstract String getParticipant();
    
    abstract String[] getParticipants();
    
    abstract PendingIntent getReadPendingIntent();
    
    abstract RemoteInputCompatBase.RemoteInput getRemoteInput();
    
    abstract PendingIntent getReplyPendingIntent();
    
    public static interface Factory {
      NotificationCompatBase.UnreadConversation build(String[] param2ArrayOfString1, RemoteInputCompatBase.RemoteInput param2RemoteInput, PendingIntent param2PendingIntent1, PendingIntent param2PendingIntent2, String[] param2ArrayOfString2, long param2Long);
    }
  }
  
  public static interface Factory {
    NotificationCompatBase.UnreadConversation build(String[] param1ArrayOfString1, RemoteInputCompatBase.RemoteInput param1RemoteInput, PendingIntent param1PendingIntent1, PendingIntent param1PendingIntent2, String[] param1ArrayOfString2, long param1Long);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\app\NotificationCompatBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */