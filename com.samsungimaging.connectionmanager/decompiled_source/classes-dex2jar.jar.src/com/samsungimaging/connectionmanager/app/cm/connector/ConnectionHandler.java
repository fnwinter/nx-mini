package com.samsungimaging.connectionmanager.app.cm.connector;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.samsungimaging.connectionmanager.app.cm.common.CMConstants;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.util.Trace;

public class ConnectionHandler extends Handler {
  private static ConnectionHandler instance_ConnectionHandler = null;
  
  private Context mContext;
  
  private ConnectionHandler(Context paramContext) {
    this.mContext = paramContext;
  }
  
  public static ConnectionHandler getInstance(Context paramContext) {
    // Byte code:
    //   0: ldc com/samsungimaging/connectionmanager/app/cm/connector/ConnectionHandler
    //   2: monitorenter
    //   3: getstatic com/samsungimaging/connectionmanager/app/cm/connector/ConnectionHandler.instance_ConnectionHandler : Lcom/samsungimaging/connectionmanager/app/cm/connector/ConnectionHandler;
    //   6: ifnonnull -> 20
    //   9: new com/samsungimaging/connectionmanager/app/cm/connector/ConnectionHandler
    //   12: dup
    //   13: aload_0
    //   14: invokespecial <init> : (Landroid/content/Context;)V
    //   17: putstatic com/samsungimaging/connectionmanager/app/cm/connector/ConnectionHandler.instance_ConnectionHandler : Lcom/samsungimaging/connectionmanager/app/cm/connector/ConnectionHandler;
    //   20: getstatic com/samsungimaging/connectionmanager/app/cm/connector/ConnectionHandler.instance_ConnectionHandler : Lcom/samsungimaging/connectionmanager/app/cm/connector/ConnectionHandler;
    //   23: astore_0
    //   24: ldc com/samsungimaging/connectionmanager/app/cm/connector/ConnectionHandler
    //   26: monitorexit
    //   27: aload_0
    //   28: areturn
    //   29: astore_0
    //   30: ldc com/samsungimaging/connectionmanager/app/cm/connector/ConnectionHandler
    //   32: monitorexit
    //   33: aload_0
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   3	20	29	finally
    //   20	24	29	finally
  }
  
  public void handleMessage(Message paramMessage) {
    Trace.d(CMConstants.TAG_NAME, "ConnectionHandler, msg : " + paramMessage);
    Trace.d(CMConstants.TAG_NAME, "ConnectionHandler, msg.obj : " + paramMessage.obj);
    switch (paramMessage.what) {
      default:
        super.handleMessage(paramMessage);
        return;
      case 100:
        break;
    } 
    Trace.d(CMConstants.TAG_NAME, "ConnectionHandler, MSG_NEED_PASSWORD");
    Bundle bundle = (Bundle)paramMessage.obj;
    CMUtil.sendBroadCastToMain(this.mContext, "NEED_PASSWORD", bundle);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\cm\connector\ConnectionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */