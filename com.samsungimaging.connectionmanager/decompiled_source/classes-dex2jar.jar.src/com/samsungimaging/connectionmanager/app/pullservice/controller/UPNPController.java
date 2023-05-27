package com.samsungimaging.connectionmanager.app.pullservice.controller;

import android.os.Handler;
import android.os.Message;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;
import com.samsungimaging.connectionmanager.app.cm.common.CMUtil;
import com.samsungimaging.connectionmanager.util.Trace;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.DeviceList;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.ServiceList;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.cybergarage.upnp.device.NotifyListener;
import org.cybergarage.upnp.device.SearchResponseListener;
import org.cybergarage.upnp.event.EventListener;
import org.cybergarage.upnp.ssdp.SSDP;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.xml.ParserException;
import org.cybergarage.xml.ParserExceptionListener;

public class UPNPController extends ControlPoint implements DeviceChangeListener, NotifyListener, SearchResponseListener, EventListener, Controller, ParserExceptionListener {
  public static UPNPController controller = null;
  
  public Trace.Tag TAG = Trace.Tag.CYBERGARAGE;
  
  private boolean bCheckOnce = false;
  
  private Device connectedDevice = null;
  
  private DeviceController deviceController = null;
  
  private Handler deviceNotifyEventHandler = null;
  
  private Handler eventHandler = null;
  
  private boolean isConnected = false;
  
  private String strUSN = null;
  
  private UPNPController() {
    this.deviceController = new DeviceController();
    addDeviceChangeListener(this);
    addNotifyListener(this);
    addSearchResponseListener(this);
    addEventListener(this);
    addParserExceptionListener(this);
  }
  
  private void MSearchStart(final String userAgent, final String accessMethod) {
    (new Thread(new Runnable() {
          public void run() {
            boolean bool = false;
            while (true) {
              if (UPNPController.this.connectedDevice != null)
                return; 
              boolean bool1 = bool;
              try {
                Trace.d(UPNPController.this.TAG, "bStarted : " + bool + " userAgent : " + userAgent + " accessMethod : " + accessMethod);
                if (!bool) {
                  bool1 = bool;
                  bool = UPNPController.this.start("ssdp:all", userAgent, accessMethod);
                  bool1 = bool;
                  Thread.sleep(3000L);
                  continue;
                } 
              } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
                bool = bool1;
                continue;
              } 
              bool1 = bool;
              UPNPController.this.search("ssdp:all");
              bool1 = bool;
              Thread.sleep(1000L);
            } 
          }
        })).start();
  }
  
  public static UPNPController getInstance() {
    // Byte code:
    //   0: ldc com/samsungimaging/connectionmanager/app/pullservice/controller/UPNPController
    //   2: monitorenter
    //   3: getstatic com/samsungimaging/connectionmanager/app/pullservice/controller/UPNPController.controller : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/UPNPController;
    //   6: ifnonnull -> 19
    //   9: new com/samsungimaging/connectionmanager/app/pullservice/controller/UPNPController
    //   12: dup
    //   13: invokespecial <init> : ()V
    //   16: putstatic com/samsungimaging/connectionmanager/app/pullservice/controller/UPNPController.controller : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/UPNPController;
    //   19: getstatic com/samsungimaging/connectionmanager/app/pullservice/controller/UPNPController.controller : Lcom/samsungimaging/connectionmanager/app/pullservice/controller/UPNPController;
    //   22: astore_0
    //   23: ldc com/samsungimaging/connectionmanager/app/pullservice/controller/UPNPController
    //   25: monitorexit
    //   26: aload_0
    //   27: areturn
    //   28: astore_0
    //   29: ldc com/samsungimaging/connectionmanager/app/pullservice/controller/UPNPController
    //   31: monitorexit
    //   32: aload_0
    //   33: athrow
    // Exception table:
    //   from	to	target	type
    //   3	19	28	finally
    //   19	23	28	finally
  }
  
  private void setupPeerCamDevice(String paramString1, String paramString2, String paramString3) {
    Message message;
    if (paramString2.equals("urn:schemas-upnp-org:service:ConnectionManager:1") || paramString2.equals("urn:schemas-upnp-org:service:ContentDirectory:1")) {
      Trace.d(this.TAG, "start setupPeerCamDevice() bCheckOnce : " + this.bCheckOnce);
      Trace.d(this.TAG, "subType : " + paramString1);
      Trace.d(this.TAG, "strNT : " + paramString2);
      Trace.d(this.TAG, "strLocalUSN : " + paramString3);
      if (!this.bCheckOnce) {
        if (!CMUtil.checkOldVersionSmartCameraApp(CMInfo.getInstance().getConnectedSSID()) && paramString1.equals("ssdp:byebye")) {
          message = new Message();
          message.what = 101;
          this.deviceNotifyEventHandler.sendMessage(message);
          return;
        } 
        if (this.connectedDevice != null) {
          this.strUSN = paramString3;
          this.bCheckOnce = true;
          message = new Message();
          message.what = 100;
          this.deviceNotifyEventHandler.sendMessage(message);
          return;
        } 
        return;
      } 
    } else {
      return;
    } 
    if (message.equals("ssdp:byebye") && this.strUSN.equals(paramString3)) {
      message = new Message();
      message.what = 101;
      this.deviceNotifyEventHandler.sendMessage(message);
      return;
    } 
    if (!message.equals("")) {
      Message message1 = new Message();
      message1.what = 102;
      message1.obj = message;
      this.eventHandler.sendMessage(message1);
      return;
    } 
  }
  
  public void connect(String paramString1, String paramString2, int paramInt) {
    Trace.d(this.TAG, "start connect()");
    MSearchStart(paramString1, paramString2);
    SSDP.setSSDPPort(paramInt);
  }
  
  public void deviceAdded(Device paramDevice) {
    Trace.d(this.TAG, "start deviceAdded()");
    if (this.connectedDevice == null) {
      DeviceList deviceList = getDeviceList();
      int j = deviceList.size();
      int i = 0;
      label20: while (true) {
        if (i < j) {
          Device device = deviceList.getDevice(i);
          Trace.d(this.TAG, "device type : " + device.getDeviceType());
          if (device.getDeviceType().contains("MediaServer")) {
            this.deviceController.setAction(device);
            ServiceList serviceList = device.getServiceList();
            int m = serviceList.size();
            for (int k = 0;; k++) {
              if (k >= m) {
                this.connectedDevice = device;
                Trace.d(this.TAG, "set connected device");
                this.isConnected = true;
                i++;
                continue label20;
              } 
              Service service = serviceList.getService(k);
              Trace.d(this.TAG, "service type : " + service.getServiceType());
              if (service.getServiceType().contains("ContentDirectory")) {
                Trace.d(this.TAG, "call subscribe()");
                subscribe(service);
              } 
            } 
            break;
          } 
          continue;
        } 
        return;
      } 
    } 
  }
  
  public void deviceNotifyReceived(SSDPPacket paramSSDPPacket) {
    Trace.d(this.TAG, "start deviceNotifyReceived()");
    setupPeerCamDevice(paramSSDPPacket.getNTS(), paramSSDPPacket.getNT(), paramSSDPPacket.getUSN());
  }
  
  public void deviceRemoved(Device paramDevice) {
    Trace.d(this.TAG, "start deviceRemoved()");
  }
  
  public void deviceSearchResponseReceived(SSDPPacket paramSSDPPacket) {
    Trace.d(this.TAG, "start deviceSearchResponseReceived()");
    setupPeerCamDevice(paramSSDPPacket.getNTS(), paramSSDPPacket.getST(), paramSSDPPacket.getUSN());
  }
  
  public void disconnect() {
    Trace.d(this.TAG, "start disconnect()");
    removeDeviceChangeListener(this);
    removeNotifyListener(this);
    removeSearchResponseListener(this);
    removeEventListener(this);
    removeParserExceptionListener(this);
    stop();
    this.bCheckOnce = false;
    this.isConnected = false;
    controller = null;
    this.connectedDevice = null;
    this.deviceController = null;
  }
  
  public void eventNotifyReceived(String paramString1, long paramLong, String paramString2, String paramString3) {
    Trace.d(this.TAG, "start eventNotifyReceived() varName : " + paramString2 + " value : " + paramString3);
    if (!paramString3.isEmpty()) {
      Message message = new Message();
      if (paramString2.equals("RVF_ROTATION_STATUS")) {
        message.what = 103;
        message.obj = paramString3;
      } else if (paramString2.equals("RVF_FLASH_STATUS")) {
        message.what = 104;
        message.obj = paramString3;
      } else if (paramString2.equals("RVF_FOCUS_STATUS")) {
        message.what = 105;
        message.obj = paramString3;
      } else if (paramString2.equals("RVF_SHUTTER_SPEED_SETUP_VALUE")) {
        message.what = 106;
        message.obj = paramString3;
      } else if (paramString2.equals("RVF_APERTURE_SETUP_VALUE")) {
        message.what = 107;
        message.obj = paramString3;
      } else if (paramString2.equals("RVF_EV_SETUP_VALUE")) {
        message.what = 108;
        message.obj = paramString3;
      } else if (paramString2.equals("RVF_REMAIN_SHOT_COUNT_VALUE")) {
        message.what = 109;
        message.obj = paramString3;
      } else if (paramString2.equals("RVF_LAST_PHOTO_URL_VALUE")) {
        message.what = 110;
        message.obj = paramString3;
      } else if (paramString2.equals("RVF_CURRENT_ZOOM_VALUE")) {
        message.what = 111;
        message.obj = paramString3;
      } else if (paramString2.equals("RVF_APERTURE_LIST")) {
        message.what = 112;
        message.obj = paramString3;
      } else if (paramString2.equals("RVF_SHUTTER_SPEED_LIST")) {
        message.what = 113;
        message.obj = paramString3;
      } else if (paramString2.equals("RVF_MOVIE_RECORD_TIME")) {
        message.what = 114;
        message.obj = paramString3;
      } else if (paramString2.equals("RVF_REMAIN_REC_TIME_VALUE")) {
        message.what = 115;
        message.obj = paramString3;
      } else if (paramString2.equals("RVF_FLASH_STROBE_STATUS")) {
        message.what = 116;
        message.obj = paramString3;
      } else if (paramString2.equals("OperationState")) {
        message.what = 117;
        message.obj = paramString3;
      } else {
        message.what = 20;
        message.obj = paramString3;
      } 
      if (this.eventHandler != null)
        this.eventHandler.sendMessage(message); 
    } 
  }
  
  public Device getConnectedDevice() {
    Trace.d(this.TAG, "start getConnectedDevice()");
    return this.connectedDevice;
  }
  
  public DeviceController getDeviceController() {
    return this.deviceController;
  }
  
  public boolean isConnected() {
    Trace.d(this.TAG, "start isConnected() : " + this.isConnected);
    return this.isConnected;
  }
  
  public void occuredParserException(ParserException paramParserException) {
    Trace.d(this.TAG, "start occuredParserException()");
    paramParserException.printStackTrace();
    String str = paramParserException.getMessage();
    if (str.contains("HTTP_UNAUTHORIZED") || str.contains("HTTP_INTERNAL_ERROR") || str.contains("HTTP_NOT_IMPLEMENTED") || str.contains("HTTP_BAD_GATEWAY") || str.contains("HTTP_UNAVAILABLE") || str.contains("HTTP_GATEWAY_TIMEOUT") || str.contains("HTTP_VERSION")) {
      Message message = new Message();
      message.what = 118;
      message.obj = str;
      this.deviceNotifyEventHandler.sendMessage(message);
    } 
  }
  
  public void setDeviceNotifyEventHandler(Handler paramHandler) {
    this.deviceNotifyEventHandler = paramHandler;
  }
  
  public void setEventHandler(Handler paramHandler) {
    this.eventHandler = paramHandler;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\controller\UPNPController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */