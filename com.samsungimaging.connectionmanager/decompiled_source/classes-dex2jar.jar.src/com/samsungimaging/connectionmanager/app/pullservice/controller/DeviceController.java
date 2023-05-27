package com.samsungimaging.connectionmanager.app.pullservice.controller;

import android.os.Handler;
import android.os.Message;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;
import com.samsungimaging.connectionmanager.app.pullservice.datatype.DSCCamera;
import com.samsungimaging.connectionmanager.app.pullservice.datatype.DSCCommand;
import com.samsungimaging.connectionmanager.app.pullservice.datatype.DSCMenuItem;
import com.samsungimaging.connectionmanager.app.pullservice.datatype.DSCMovieResolution;
import com.samsungimaging.connectionmanager.app.pullservice.datatype.DSCResolution;
import com.samsungimaging.connectionmanager.app.pullservice.demo.rvf.RVFFunctionManager;
import com.samsungimaging.connectionmanager.app.pullservice.util.CommandManager;
import com.samsungimaging.connectionmanager.app.pullservice.util.CommandRequestListener;
import com.samsungimaging.connectionmanager.app.pullservice.util.Utils;
import com.samsungimaging.connectionmanager.util.Trace;
import java.util.Locale;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.ArgumentList;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.ServiceList;
import org.cybergarage.upnp.xml.ActionData;
import org.cybergarage.xml.Node;

public class DeviceController extends DSCCamera implements CommandRequestListener {
  private Action AFAct;
  
  private Action BrowseAct;
  
  private Action CancelTouchAFMovieAct;
  
  private Action GetDeviceConfigurationAct;
  
  private Action GetInformationAct;
  
  private Action GetZoomAct;
  
  private Action MULTIAFAct;
  
  private Action ReleaseSelfTimerAct;
  
  private Action SetAFAreaAct;
  
  private Action SetAFModeAct;
  
  private Action SetApertureAct;
  
  private Action SetDialModeAct;
  
  private Action SetDriveAct;
  
  private Action SetEVAct;
  
  private Action SetFileSaveAct;
  
  private Action SetFlashAct;
  
  private Action SetISOAct;
  
  private Action SetLEDAct;
  
  private Action SetMeteringAct;
  
  private Action SetMovieResolutionAct;
  
  private Action SetMovieStreamQualityAct;
  
  private Action SetOperationStateAct;
  
  private Action SetQualityAct;
  
  private Action SetResolutionAct;
  
  private Action SetShutterSpeedAct;
  
  private Action SetStreamQualityAct;
  
  private Action SetTotalCopyItemsAct;
  
  private Action SetTouchAFOptionAct;
  
  private Action SetWBAct;
  
  private Action SetZoomAct;
  
  private Action ShotAct;
  
  private Action ShotWithGPSAct;
  
  private Action ShutterUpAct;
  
  private Action StartRecordAct;
  
  private Action StartShotAct;
  
  private Action StartZoomAct;
  
  private Action StopRecordAct;
  
  private Action StopShotAct;
  
  private Action StopZoomAct;
  
  private Trace.Tag TAG = Trace.Tag.CYBERGARAGE;
  
  private Action TouchAFAct;
  
  private Action TouchAFMovieAct;
  
  private Action TouchAFPointAct;
  
  private Action ZoomINAct;
  
  private Action ZoomOUTAct;
  
  private CommandManager commandManager = new CommandManager();
  
  private Handler handler;
  
  public DeviceController() {
    this.commandManager.addCommandRequestListener(this);
  }
  
  private void requestAction(int paramInt, String paramString) {
    switch (paramInt) {
      default:
        return;
      case 1:
        getInformateionAct(paramString);
        return;
      case 2:
        setResolutionAct(paramString);
        return;
      case 3:
        zoomINAct();
        return;
      case 4:
        zoomOUTAct();
        return;
      case 5:
        getZoomAct();
        return;
      case 6:
        setZoomAct(paramString);
        return;
      case 7:
        startZoomAct(paramString);
        return;
      case 8:
        stopZoomAct(paramString);
        return;
      case 9:
        afAct();
        return;
      case 10:
        multiAFAct();
        return;
      case 11:
        setTouchAFOptionAct(paramString);
        return;
      case 12:
        touchAFAct(paramString);
        return;
      case 13:
        releaseSelfTimerAct();
        return;
      case 14:
        shotAct(paramString);
        return;
      case 15:
        shotWithGPSAct();
        return;
      case 16:
        setLEDAct(paramString);
        return;
      case 17:
        setFlashAct(paramString);
        return;
      case 18:
        setStreamQualityAct(paramString);
        return;
      case 19:
        setMovieStreamQualityAct(paramString);
        return;
      case 20:
        shutterUpAct();
        return;
      case 21:
        setShutterSpeedAct(paramString);
        return;
      case 22:
        setApertureAct(paramString);
        return;
      case 23:
        setEVAct(paramString);
        return;
      case 24:
        setISOAct(paramString);
        return;
      case 25:
        setWBAct(paramString);
        return;
      case 26:
        setMeteringAct(paramString);
        return;
      case 27:
        setAFModeAct(paramString);
        return;
      case 28:
        setAFAreaAct(paramString);
        return;
      case 29:
        setDriveAct(paramString);
        return;
      case 30:
        startShotAct(paramString);
        return;
      case 31:
        stopShotAct(paramString);
        return;
      case 32:
        setDialModeAct(paramString);
        return;
      case 33:
        setQualityAct(paramString);
        return;
      case 34:
        setMovieResolutionAct(paramString);
        return;
      case 35:
        startRecord();
        return;
      case 36:
        stopRecord();
        return;
      case 37:
        setFileSave(paramString);
        return;
      case 39:
        cancelTouchAFMovie(paramString);
        return;
      case 40:
        touchAFMovie(paramString);
        return;
      case 41:
        browse(paramString);
        return;
      case 42:
        getDeviceConfiguration(paramString);
        return;
      case 43:
        setOperationState(paramString);
        return;
      case 44:
        break;
    } 
    setTotalCopyItems(paramString);
  }
  
  public String actionStatusToString(int paramInt) {
    switch (paramInt) {
      default:
        return "";
      case 0:
        return "ACTION_STATUS_NONE";
      case 2:
        return "GET_INFORMATION_ACTION_START";
      case 3:
        return "GET_INFORMATION_ACTION_END";
      case 4:
        return "SET_RESOLUTION_ACTION_START";
      case 5:
        return "SET_RESOLUTION_ACTION_END";
      case 6:
        return "ZOOM_IN_ACTION_START";
      case 7:
        return "ZOOM_IN_ACTION_END";
      case 8:
        return "ZOOM_OUT_ACTION_START";
      case 9:
        return "ZOOM_OUT_ACTION_END";
      case 10:
        return "GET_ZOOM_ACTION_START";
      case 11:
        return "GET_ZOOM_ACTION_END";
      case 12:
        return "SET_ZOOM_ACTION_START";
      case 13:
        return "SET_ZOOM_ACTION_END";
      case 14:
        return "START_ZOOM_ACTION_START";
      case 15:
        return "START_ZOOM_ACTION_END";
      case 16:
        return "STOP_ZOOM_ACTION_START";
      case 17:
        return "STOP_ZOOM_ACTION_END";
      case 18:
        return "AUTO_FOCUS_ACTION_START";
      case 19:
        return "AUTO_FOCUS_ACTION_END";
      case 20:
        return "MULTI_AUTO_FOCUS_ACTION_START";
      case 21:
        return "MULTI_AUTO_FOCUS_ACTION_END";
      case 22:
        return "SET_TOUCH_AUTO_FOCUS_OPTION_ACTION_START";
      case 23:
        return "SET_TOUCH_AUTO_FOCUS_OPTION_ACTION_END";
      case 24:
        return "TOUCH_AUTO_FOCUS_ACTION_START";
      case 25:
        return "TOUCH_AUTO_FOCUS_ACTION_END";
      case 26:
        return "RELEASE_SELF_TIMER_ACTION_START";
      case 27:
        return "RELEASE_SELF_TIMER_ACTION_END";
      case 28:
        return "SHOT_ACTION_START";
      case 29:
        return "SHOT_ACTION_END";
      case 30:
        return "SHOT_WITH_GPS_ACTION_START";
      case 31:
        return "SHOT_WITH_GPS_ACTION_END";
      case 32:
        return "SET_LED_ACTION_START";
      case 33:
        return "SET_LED_ACTION_END";
      case 34:
        return "SET_FLASH_ACTION_START";
      case 35:
        return "SET_FLASH_ACTION_END";
      case 36:
        return "SET_STREAM_QUALITY_ACTION_START";
      case 37:
        return "SET_STREAM_QUALITY_ACTION_END";
      case 38:
        return "SET_MOVIE_STREAM_QUALITY_ACTION_START";
      case 39:
        return "SET_MOVIE_STREAM_QUALITY_ACTION_END";
      case 40:
        return "SHUTTER_UP_ACTION_START";
      case 41:
        return "SHUTTER_UP_ACTION_END";
      case 42:
        return "SET_SHUTTER_SPEED_ACTION_START";
      case 43:
        return "SET_SHUTTER_SPEED_ACTION_END";
      case 44:
        return "SET_APERTURE_ACTION_START";
      case 45:
        return "SET_APERTURE_ACTION_END";
      case 46:
        return "SET_EV_ACTION_START";
      case 47:
        return "SET_EV_ACTION_END";
      case 48:
        return "SET_ISO_ACTION_START";
      case 49:
        return "SET_ISO_ACTION_END";
      case 50:
        return "SET_WB_ACTION_START";
      case 51:
        return "SET_WB_ACTION_END";
      case 52:
        return "SET_METERING_ACTION_START";
      case 53:
        return "SET_METERING_ACTION_END";
      case 54:
        return "SET_AF_MODE_ACTION_START";
      case 55:
        return "SET_AF_MODE_ACTION_END";
      case 56:
        return "SET_AF_AREA_ACTION_START";
      case 57:
        return "SET_AF_AREA_ACTION_END";
      case 58:
        return "SET_DRIVE_ACTION_START";
      case 59:
        return "SET_DRIVE_ACTION_END";
      case 60:
        return "START_SHOT_ACTION_START";
      case 61:
        return "START_SHOT_ACTION_END";
      case 62:
        return "STOP_SHOT_ACTION_START";
      case 63:
        return "STOP_SHOT_ACTION_END";
      case 64:
        return "SET_DIAL_MODE_ACTION_START";
      case 65:
        return "SET_DIAL_MODE_ACTION_END";
      case 66:
        return "SET_QUALITY_ACTION_START";
      case 67:
        return "SET_QUALITY_ACTION_END";
      case 68:
        return "SET_MOVIE_RESOLUTION_ACTION_START";
      case 69:
        return "SET_MOVIE_RESOLUTION_ACTION_END";
      case 70:
        return "START_RECORD_ACTION_START";
      case 71:
        return "START_RECORD_ACTION_END";
      case 72:
        return "STOP_RECORD_ACTION_START";
      case 73:
        return "STOP_RECORD_ACTION_END";
      case 74:
        return "SET_FILE_SAVE_ACTION_START";
      case 75:
        return "SET_FILE_SAVE_ACTION_END";
      case 76:
        return "TOUCH_AF_POINT_ACTION_START";
      case 77:
        return "TOUCH_AF_POINT_ACTION_END";
      case 78:
        return "CANCEL_TOUCH_AF_MOVIE_ACTION_START";
      case 79:
        return "CANCEL_TOUCH_AF_MOVIE_ACTION_END";
      case 80:
        return "TOUCH_AF_MOVIE_ACTION_START";
      case 81:
        return "TOUCH_AF_MOVIE_ACTION_END";
      case 82:
        return "BROWSE_ACTION_START";
      case 83:
        return "BROWSE_ACTION_END";
      case 84:
        return "GET_DEVICE_CONFIGURATION_ACTION_START";
      case 85:
        return "GET_DEVICE_CONFIGURATION_ACTION_END";
      case 86:
        return "SET_OPERATION_STATE_ACTION_START";
      case 87:
        return "SET_OPERATION_STATE_ACTION_END";
      case 88:
        return "SET_TOTAL_COPY_ITEMS_ACTION_START";
      case 89:
        break;
    } 
    return "SET_TOTAL_COPY_ITEMS_ACTION_END";
  }
  
  public void afAct() {
    sendEvent(18, Boolean.valueOf(true));
    boolean bool = this.AFAct.postControlAction();
    if (bool) {
      ArgumentList argumentList = this.AFAct.getOutputArgumentList();
      int j = argumentList.size();
      int i = 0;
      while (true) {
        if (i < j) {
          Argument argument = argumentList.getArgument(i);
          String str1 = argument.getName();
          String str2 = argument.getValue();
          if (!str1.equals("AF_MF") && !str2.equals("3") && str1.equals("AFSTATUS"))
            str2.equals("0"); 
          i++;
          continue;
        } 
        sendEvent(19, Boolean.valueOf(bool));
        return;
      } 
    } 
    sendEvent(19, Boolean.valueOf(bool));
  }
  
  public void browse(String paramString) {
    sendEvent(82, Boolean.valueOf(true));
    this.BrowseAct.postControlAction();
    sendEvent(83, (Object)null);
  }
  
  public void cancelTouchAFMovie(String paramString) {
    Trace.d(this.TAG, "start cancelTouchAFMovie() param : " + paramString);
    this.CancelTouchAFMovieAct.setArgumentValue("MovieTouchAFStatus", paramString);
    sendEvent(78, Boolean.valueOf(true));
    sendEvent(79, Boolean.valueOf(this.CancelTouchAFMovieAct.postControlAction()));
  }
  
  public boolean doAction(int paramInt, String paramString) {
    Trace.d(this.TAG, "start doAction() action : " + paramInt + " param : " + paramString);
    boolean bool = false;
    if (getAction(paramInt) != null) {
      DSCCommand dSCCommand = new DSCCommand(paramInt, paramString);
      this.commandManager.queueCommand(dSCCommand);
      bool = true;
    } 
    Trace.d(this.TAG, "end doAction() action : " + paramInt + " result : " + bool);
    return bool;
  }
  
  public Action getAction(int paramInt) {
    switch (paramInt) {
      default:
        return null;
      case 1:
        return this.GetInformationAct;
      case 2:
        return this.SetResolutionAct;
      case 3:
        return this.ZoomINAct;
      case 4:
        return this.ZoomOUTAct;
      case 5:
        return this.GetZoomAct;
      case 6:
        return this.SetZoomAct;
      case 7:
        return this.StartZoomAct;
      case 8:
        return this.StopZoomAct;
      case 9:
        return this.AFAct;
      case 10:
        return this.MULTIAFAct;
      case 11:
        return this.SetTouchAFOptionAct;
      case 12:
        return this.TouchAFAct;
      case 13:
        return this.ReleaseSelfTimerAct;
      case 14:
        return this.ShotAct;
      case 15:
        return this.ShotWithGPSAct;
      case 16:
        return this.SetLEDAct;
      case 17:
        return this.SetFlashAct;
      case 18:
        return this.SetStreamQualityAct;
      case 19:
        return this.SetMovieStreamQualityAct;
      case 20:
        return this.ShutterUpAct;
      case 21:
        return this.SetShutterSpeedAct;
      case 22:
        return this.SetApertureAct;
      case 23:
        return this.SetEVAct;
      case 24:
        return this.SetISOAct;
      case 25:
        return this.SetWBAct;
      case 26:
        return this.SetMeteringAct;
      case 27:
        return this.SetAFModeAct;
      case 28:
        return this.SetAFAreaAct;
      case 29:
        return this.SetDriveAct;
      case 30:
        return this.StartShotAct;
      case 31:
        return this.StopShotAct;
      case 32:
        return this.SetDialModeAct;
      case 33:
        return this.SetQualityAct;
      case 34:
        return this.SetMovieResolutionAct;
      case 35:
        return this.StartRecordAct;
      case 36:
        return this.StopRecordAct;
      case 37:
        return this.SetFileSaveAct;
      case 38:
        return this.TouchAFPointAct;
      case 39:
        return this.CancelTouchAFMovieAct;
      case 40:
        return this.TouchAFMovieAct;
      case 41:
        return this.BrowseAct;
      case 42:
        return this.GetDeviceConfigurationAct;
      case 43:
        return this.SetOperationStateAct;
      case 44:
        break;
    } 
    return this.SetTotalCopyItemsAct;
  }
  
  public void getDeviceConfiguration(String paramString) {
    sendEvent(84, Boolean.valueOf(true));
    if (this.GetDeviceConfigurationAct.postControlAction())
      if (this.GetDeviceConfigurationAct.getArgumentValue("TranferOption").equals("0")) {
        setResizeMode(true);
      } else {
        setResizeMode(false);
      }  
    sendEvent(85, (Object)null);
  }
  
  public void getInformateionAct(String paramString) {
    this.GetInformationAct.setArgumentValue("GPSINFO", paramString);
    sendEvent(2, Boolean.valueOf(true));
    boolean bool = this.GetInformationAct.postControlAction();
    if (bool) {
      resetCamera(this.GetInformationAct.getActionNode());
      setConnected(true);
    } 
    sendEvent(3, Boolean.valueOf(bool));
  }
  
  public void getZoomAct() {
    sendEvent(10, Boolean.valueOf(true));
    boolean bool = this.GetZoomAct.postControlAction();
    if (bool) {
      ArgumentList argumentList = this.GetZoomAct.getOutputArgumentList();
      int j = argumentList.size();
      int i = 0;
      while (true) {
        if (i < j) {
          Argument argument = argumentList.getArgument(i);
          String str1 = argument.getName();
          String str2 = argument.getValue();
          if (str1.equals("CURRENTZOOM"))
            setDefaultZoom(str2); 
          i++;
          continue;
        } 
        sendEvent(11, Boolean.valueOf(bool));
        return;
      } 
    } 
    sendEvent(11, Boolean.valueOf(bool));
  }
  
  public void multiAFAct() {
    sendEvent(20, Boolean.valueOf(true));
    boolean bool = this.MULTIAFAct.postControlAction();
    if (bool) {
      ArgumentList argumentList = this.MULTIAFAct.getOutputArgumentList();
      int j = argumentList.size();
      int i = 0;
      while (true) {
        if (i < j) {
          Argument argument = argumentList.getArgument(i);
          String str1 = argument.getName();
          String str2 = argument.getValue();
          if (!str1.equals("AF_MF") && str1.equals("AFSTATUS"))
            str2.equals("AFFAIL"); 
          i++;
          continue;
        } 
        sendEvent(21, Boolean.valueOf(bool));
        return;
      } 
    } 
    sendEvent(21, Boolean.valueOf(bool));
  }
  
  public void onCommand(Object paramObject) {
    paramObject = paramObject;
    requestAction(paramObject.getcommandID(), paramObject.getparam());
  }
  
  public void releaseSelfTimerAct() {
    this.ReleaseSelfTimerAct.setArgumentValue("RELEASETIMER", "0");
    sendEvent(26, Boolean.valueOf(true));
    sendEvent(27, Boolean.valueOf(this.ReleaseSelfTimerAct.postControlAction()));
  }
  
  public void resetCamera(Node paramNode) {
    paramNode = ((ActionData)paramNode.getUserData()).getControlResponse().getBodyNode();
    paramNode.print();
    int i = 0;
    label657: while (true) {
      if (i >= paramNode.getNNodes())
        return; 
      Node node = paramNode.getNode(i);
      int j = 0;
      label650: while (true) {
        DSCMenuItem dSCMenuItem;
        if (j >= node.getNNodes()) {
          i++;
          continue label657;
        } 
        Node node1 = node.getNode(j);
        if (node1.getName().equals("GETINFORMATIONRESULT")) {
          int k = 0;
          label636: while (true) {
            if (k < node1.getNNodes()) {
              String str;
              Node node2 = node1.getNode(k);
              if (node2.getName().equals("Version")) {
                str = node2.getValue();
                if (str.contains("_")) {
                  setVersionPrefix(str.split("_", 3)[0]);
                  setVersion(str.split("_", 3)[1]);
                  continue;
                } 
                setVersion(str);
                continue;
              } 
              if (str.getName().equals("DialModeValue")) {
                int m = 0;
                label550: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Supports")) {
                      if (node3.getNNodes() > 0) {
                        getDialModeMenuItems().clear();
                        int n;
                        for (n = 0;; n++) {
                          if (n >= node3.getNNodes()) {
                            m++;
                            continue label550;
                          } 
                          Node node4 = node3.getNode(n);
                          getDialModeMenuItems().add(node4.getValue());
                        } 
                        break;
                      } 
                      continue;
                    } 
                    if (node3.getName().equals("Default")) {
                      setDialModeValue(node3.getValue());
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("Resolutions")) {
                getResolutionMenuItems().clear();
                getResolutionMenuItemsDim().clear();
                int m = 0;
                label629: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Resolution")) {
                      DSCResolution dSCResolution = new DSCResolution();
                      int n = 0;
                      while (true) {
                        if (n >= node3.getNNodes()) {
                          getResolutionMenuItems().add(dSCResolution);
                        } else {
                          Node node4 = node3.getNode(n);
                          if (node4.getName().equals("Width")) {
                            dSCResolution.setWidth(Integer.parseInt(node4.getValue()));
                          } else if (node4.getName().equals("Height")) {
                            dSCResolution.setHeight(Integer.parseInt(node4.getValue()));
                          } else if (node4.getName().equals("Ratio")) {
                            dSCResolution.setRatio(node4.getValue());
                          } 
                          n++;
                          continue;
                        } 
                        m++;
                        continue label629;
                      } 
                      break;
                    } 
                    if (node3.getName().equals("Default")) {
                      if (Utils.isNumeric(node3.getValue())) {
                        setDefaultResolutionIndex(node3.getValue());
                        continue;
                      } 
                      int n = 0;
                      while (true) {
                        if (n < getResolutionMenuItems().size()) {
                          DSCResolution dSCResolution = getResolutionMenuItems().get(n);
                          String str1 = String.format("%dx%d", new Object[] { Integer.valueOf(dSCResolution.getWidth()), Integer.valueOf(dSCResolution.getHeight()) });
                          if (node3.getValue().equals(str1)) {
                            setDefaultResolutionIndex(String.valueOf(n));
                            setRatioValue(((DSCResolution)getResolutionMenuItems().get(n)).getRatio());
                          } 
                          n++;
                          continue;
                        } 
                        m++;
                        continue label629;
                      } 
                      break;
                    } 
                    if (node3.getName().equals("Disable")) {
                      int n = 0;
                      while (true) {
                        if (n < node3.getNNodes()) {
                          Node node4 = node3.getNode(n);
                          DSCMenuItem dSCMenuItem1 = new DSCMenuItem();
                          dSCMenuItem1.setName(node4.getName());
                          dSCMenuItem1.setValue(node4.getValue());
                          getResolutionMenuItemsDim().add(dSCMenuItem1);
                          n++;
                          continue;
                        } 
                        m++;
                        continue label629;
                      } 
                      break;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("Flash")) {
                getFlashMenuItems().clear();
                int m = 0;
                label604: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Supports")) {
                      if (node3.getNNodes() > 0) {
                        int n;
                        for (n = 0;; n++) {
                          if (n >= node3.getNNodes()) {
                            m++;
                            continue label604;
                          } 
                          getFlashMenuItems().add(node3.getNode(n).getValue());
                        } 
                        break;
                      } 
                      continue;
                    } 
                    if (node3.getName().equals("Defaultflash")) {
                      setDefaultFlash(node3.getValue());
                      setCurrentFlashDisplay(node3.getValue());
                      continue;
                    } 
                    if (node3.getName().equals("Support")) {
                      getFlashMenuItems().add(node3.getValue());
                      continue;
                    } 
                    if (node3.getName().equals("Default")) {
                      if (Utils.isNumeric(node3.getValue())) {
                        setDefaultFlash(getFlashMenuItems().get(Integer.parseInt(node3.getValue())));
                        setCurrentFlashDisplay(getFlashMenuItems().get(Integer.parseInt(node3.getValue())));
                        continue;
                      } 
                      setDefaultFlash(node3.getValue());
                      setCurrentFlashDisplay(node3.getValue());
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("FlashDisplay")) {
                int m = 0;
                label587: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Supports")) {
                      if (node3.getNNodes() > 0) {
                        getFlashDisplayMenuItems().clear();
                        int n;
                        for (n = 0;; n++) {
                          if (n >= node3.getNNodes()) {
                            m++;
                            continue label587;
                          } 
                          Node node4 = node3.getNode(n);
                          getFlashDisplayMenuItems().add(node4.getValue());
                        } 
                        break;
                      } 
                      continue;
                    } 
                    if (node3.getName().equals("CurrentFlashDisplay")) {
                      if (Utils.isNumeric(node3.getValue())) {
                        setCurrentFlashDisplay(getFlashDisplayMenuItems().get(Integer.parseInt(node3.getValue())));
                        continue;
                      } 
                      if (node3.getValue().equals("On")) {
                        setCurrentFlashDisplay(getDefaultFlash());
                        continue;
                      } 
                      setCurrentFlashDisplay(node3.getValue());
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("DefaultFocusState")) {
                setDefaultFocusState(str.getValue());
                continue;
              } 
              if (str.getName().equals("ZoomInfo")) {
                int m = 0;
                while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("DefaultZoom")) {
                      setDefaultZoom(node3.getValue());
                    } else if (node3.getName().equals("MaxZoom")) {
                      if (RVFFunctionManager.isBigZoomMaxValue(CMInfo.getInstance().getConnectedSSID())) {
                        setMaxZoom(String.valueOf(Integer.parseInt(node3.getValue()) - 1));
                      } else {
                        setMaxZoom(node3.getValue());
                      } 
                    } else if (node3.getName().equals("MinZoom")) {
                      setMinZoom(node3.getValue());
                    } 
                    m++;
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("AutoFocus")) {
                int m = 0;
                while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("AFPriority"))
                      setAFPriority(node3.getValue()); 
                    m++;
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("AVAILSHOTS")) {
                if (!str.getValue().equals("")) {
                  setAvailShots(str.getValue());
                  continue;
                } 
                continue;
              } 
              if (str.getName().equals("RemainRecTime")) {
                setRemainRecTimeValue(str.getValue());
                continue;
              } 
              if (str.getName().equals("StreamQuality")) {
                getStreamQualityMenuItems().clear();
                int m = 0;
                label547: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Quality")) {
                      int n;
                      for (n = 0;; n++) {
                        if (n >= node3.getNNodes()) {
                          m++;
                          continue label547;
                        } 
                        Node node4 = node3.getNode(n);
                        getStreamQualityMenuItems().add(node4.getValue());
                      } 
                      break;
                    } 
                    if (node3.getName().equals("Default")) {
                      setCurrentStreamQuality(node3.getValue());
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("ROTATION")) {
                setRotation(str.getValue());
                continue;
              } 
              if (str.getName().equals("EVValue")) {
                int m = 0;
                label555: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Supports")) {
                      if (node3.getNNodes() > 0) {
                        getEVMenuItems().clear();
                        int n;
                        for (n = 0;; n++) {
                          if (n >= node3.getNNodes()) {
                            m++;
                            continue label555;
                          } 
                          Node node4 = node3.getNode(n);
                          getEVMenuItems().add(node4.getValue());
                        } 
                        break;
                      } 
                      continue;
                    } 
                    if (node3.getName().equals("Default")) {
                      setEVValue(node3.getValue());
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("ISOValue")) {
                int m = 0;
                label559: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Supports")) {
                      if (node3.getNNodes() > 0) {
                        getISOMenuItems().clear();
                        int n;
                        for (n = 0;; n++) {
                          if (n >= node3.getNNodes()) {
                            m++;
                            continue label559;
                          } 
                          Node node4 = node3.getNode(n);
                          getISOMenuItems().add(node4.getValue());
                        } 
                        break;
                      } 
                      continue;
                    } 
                    if (node3.getName().equals("Default")) {
                      setISOValue(node3.getValue());
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("WBValue")) {
                int m = 0;
                label563: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Supports")) {
                      if (node3.getNNodes() > 0) {
                        getWBMenuItems().clear();
                        int n;
                        for (n = 0;; n++) {
                          if (n >= node3.getNNodes()) {
                            m++;
                            continue label563;
                          } 
                          Node node4 = node3.getNode(n);
                          getWBMenuItems().add(node4.getValue());
                        } 
                        break;
                      } 
                      continue;
                    } 
                    if (node3.getName().equals("Default")) {
                      setWBValue(node3.getValue());
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("MeteringValue")) {
                int m = 0;
                label567: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Supports")) {
                      if (node3.getNNodes() > 0) {
                        getMeteringMenuItems().clear();
                        int n;
                        for (n = 0;; n++) {
                          if (n >= node3.getNNodes()) {
                            m++;
                            continue label567;
                          } 
                          Node node4 = node3.getNode(n);
                          getMeteringMenuItems().add(node4.getValue());
                        } 
                        break;
                      } 
                      continue;
                    } 
                    if (node3.getName().equals("Default")) {
                      setMeteringValue(node3.getValue());
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("AFModeValue")) {
                int m = 0;
                label571: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Supports")) {
                      if (node3.getNNodes() > 0) {
                        getAFModeMenuItems().clear();
                        int n;
                        for (n = 0;; n++) {
                          if (n >= node3.getNNodes()) {
                            m++;
                            continue label571;
                          } 
                          Node node4 = node3.getNode(n);
                          getAFModeMenuItems().add(node4.getValue());
                        } 
                        break;
                      } 
                      continue;
                    } 
                    if (node3.getName().equals("Default")) {
                      setAFModeValue(node3.getValue());
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("AFAreaValue")) {
                int m = 0;
                label615: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Supports")) {
                      getAFAreaMenuItems().clear();
                      if (node3.getNNodes() > 0) {
                        int n = 0;
                        while (true) {
                          if (n < node3.getNNodes()) {
                            Node node4 = node3.getNode(n);
                            getAFAreaMenuItems().add(node4.getValue());
                            n++;
                            continue;
                          } 
                          m++;
                          continue label615;
                        } 
                        break;
                      } 
                      continue;
                    } 
                    if (node3.getName().equals("Default")) {
                      setAFAreaValue(node3.getValue());
                      continue;
                    } 
                    if (node3.getName().equals("Disable")) {
                      getAFAreaMenuItemsDim().clear();
                      if (node3.getNNodes() > 0) {
                        int n = 0;
                        while (true) {
                          if (n < node3.getNNodes()) {
                            DSCMenuItem dSCMenuItem1 = new DSCMenuItem();
                            Node node4 = node3.getNode(n);
                            dSCMenuItem1.setName(node4.getName());
                            dSCMenuItem1.setValue(node4.getValue());
                            getAFAreaMenuItemsDim().add(dSCMenuItem1);
                            n++;
                            continue;
                          } 
                          m++;
                          continue label615;
                        } 
                        break;
                      } 
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("DriveValue")) {
                int m = 0;
                label620: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Supports")) {
                      if (node3.getNNodes() > 0) {
                        getDriveMenuItems().clear();
                        int n;
                        for (n = 0;; n++) {
                          if (n >= node3.getNNodes()) {
                            m++;
                            continue label620;
                          } 
                          getDriveMenuItems().add(node3.getNode(n).getValue());
                        } 
                        break;
                      } 
                      continue;
                    } 
                    if (node3.getName().equals("Default")) {
                      if (node3.getValue().toUpperCase(Locale.ENGLISH).equals("TIMER (2SEC)")) {
                        setCurrentLEDTimeMenuItem("Timer (2sec)");
                      } else if (node3.getValue().toUpperCase(Locale.ENGLISH).equals("TIMER (5SEC)")) {
                        setCurrentLEDTimeMenuItem("Timer (5sec)");
                      } else if (node3.getValue().toUpperCase(Locale.ENGLISH).equals("TIMER (10SEC)")) {
                        setCurrentLEDTimeMenuItem("Timer (10sec)");
                      } else {
                        setCurrentLEDTimeMenuItem("Single");
                      } 
                      setDriveValue(node3.getValue());
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("ShutterSpeedValue")) {
                int m = 0;
                label575: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Supports")) {
                      if (node3.getNNodes() > 0) {
                        getShutterSpeedMenuItems().clear();
                        int n;
                        for (n = 0;; n++) {
                          if (n >= node3.getNNodes()) {
                            m++;
                            continue label575;
                          } 
                          Node node4 = node3.getNode(n);
                          getShutterSpeedMenuItems().add(node4.getValue());
                        } 
                        break;
                      } 
                      continue;
                    } 
                    if (node3.getName().equals("Default")) {
                      setShutterSpeedValue(node3.getValue());
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("ApertureValue")) {
                int m = 0;
                label579: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Supports")) {
                      if (node3.getNNodes() > 0) {
                        getApertureMenuItems().clear();
                        int n;
                        for (n = 0;; n++) {
                          if (n >= node3.getNNodes()) {
                            m++;
                            continue label579;
                          } 
                          Node node4 = node3.getNode(n);
                          getApertureMenuItems().add(node4.getValue());
                        } 
                        break;
                      } 
                      continue;
                    } 
                    if (node3.getName().equals("Default")) {
                      setApertureValue(node3.getValue());
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("QualityValue")) {
                int m = 0;
                label593: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Supports")) {
                      getQualityMenuItems().clear();
                      setQualityValue("");
                      if (node3.getNNodes() > 0) {
                        int n;
                        for (n = 0;; n++) {
                          if (n >= node3.getNNodes()) {
                            m++;
                            continue label593;
                          } 
                          Node node4 = node3.getNode(n);
                          if (!node4.getValue().isEmpty())
                            getQualityMenuItems().add(node4.getValue()); 
                        } 
                        break;
                      } 
                      continue;
                    } 
                    if (node3.getName().equals("Default")) {
                      setQualityValue(node3.getValue());
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("MovieResolutionValue")) {
                int m = 0;
                label611: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Supports")) {
                      if (node3.getNNodes() > 0) {
                        getMovieResolutionMenuItems().clear();
                        for (int n = 0;; n++) {
                          if (n >= node3.getNNodes()) {
                            m++;
                            continue label611;
                          } 
                          Node node4 = node3.getNode(n);
                          if (!node4.getValue().isEmpty())
                            if (node4.getName().equals("S")) {
                              DSCMovieResolution dSCMovieResolution = new DSCMovieResolution();
                              dSCMovieResolution.setResolution(node4.getValue());
                              getMovieResolutionMenuItems().add(dSCMovieResolution);
                            } else if (node4.getName().equals("Ratio")) {
                              ((DSCMovieResolution)getMovieResolutionMenuItems().get(getMovieResolutionMenuItems().size() - 1)).setRatio(node4.getValue());
                            }  
                        } 
                        break;
                      } 
                      continue;
                    } 
                    if (node3.getName().equals("Default")) {
                      setMovieResolutionValue(node3.getValue());
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("MultiAFMatrixSize")) {
                setMultiAFMatrixSizeValue(str.getValue());
                continue;
              } 
              if (str.getName().equals("FileSaveValue")) {
                int m = 0;
                label624: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Supports")) {
                      getFileSaveMenuItems().clear();
                      getFileSaveMenuItemsDim().clear();
                      if (node3.getNNodes() > 0) {
                        int n = 0;
                        while (true) {
                          if (n < node3.getNNodes()) {
                            Node node4 = node3.getNode(n);
                            if (!node4.getValue().isEmpty())
                              getFileSaveMenuItems().add(node4.getValue()); 
                            n++;
                            continue;
                          } 
                          m++;
                          continue label624;
                        } 
                        break;
                      } 
                      continue;
                    } 
                    if (node3.getName().equals("Default")) {
                      setFileSaveValue(node3.getValue());
                      continue;
                    } 
                    if (node3.getName().equals("Disable")) {
                      if (node3.getNNodes() > 0) {
                        getFileSaveMenuItemsDim().clear();
                        int n = 0;
                        while (true) {
                          if (n < node3.getNNodes()) {
                            Node node4 = node3.getNode(n);
                            if (!node4.getValue().isEmpty()) {
                              DSCMenuItem dSCMenuItem1 = new DSCMenuItem();
                              dSCMenuItem1.setName(node4.getName());
                              dSCMenuItem1.setValue(node4.getValue());
                              getFileSaveMenuItemsDim().add(dSCMenuItem1);
                            } 
                            n++;
                            continue;
                          } 
                          m++;
                          continue label624;
                        } 
                        break;
                      } 
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("RatioOffset")) {
                if (str.getNNodes() > 0) {
                  getRatioOffsetMenuItems().clear();
                  int m = 0;
                  label632: while (true) {
                    if (m < str.getNNodes()) {
                      Node node3 = str.getNode(m);
                      if (node3.getName().equals("Supports")) {
                        if (node3.getNNodes() > 0) {
                          DSCResolution dSCResolution = new DSCResolution();
                          int n = 0;
                          while (true) {
                            if (n >= node3.getNNodes()) {
                              getRatioOffsetMenuItems().add(dSCResolution);
                            } else {
                              Node node4 = node3.getNode(n);
                              if (node4.getName().equals("Width")) {
                                dSCResolution.setWidth(Integer.parseInt(node4.getValue()));
                              } else if (node4.getName().equals("Height")) {
                                dSCResolution.setHeight(Integer.parseInt(node4.getValue()));
                              } else if (node4.getName().equals("Ratio")) {
                                dSCResolution.setRatio(node4.getValue());
                              } 
                              n++;
                              continue;
                            } 
                            m++;
                            continue label632;
                          } 
                          break;
                        } 
                        continue;
                      } 
                      if (node3.getName().equals("Default")) {
                        if (node3.getNNodes() > 0) {
                          DSCResolution dSCResolution = new DSCResolution();
                          int n = 0;
                          while (true) {
                            if (n >= node3.getNNodes()) {
                              setRatioOffsetValue(dSCResolution);
                            } else {
                              Node node4 = node3.getNode(n);
                              if (node4.getName().equals("Width")) {
                                dSCResolution.setWidth(Integer.parseInt(node4.getValue()));
                              } else if (node4.getName().equals("Height")) {
                                dSCResolution.setHeight(Integer.parseInt(node4.getValue()));
                              } else if (node4.getName().equals("Ratio")) {
                                dSCResolution.setRatio(node4.getValue());
                              } 
                              n++;
                              continue;
                            } 
                            m++;
                            continue label632;
                          } 
                          break;
                        } 
                        continue;
                      } 
                      continue;
                    } 
                    k++;
                    continue label636;
                  } 
                  break;
                } 
                continue;
              } 
              if (str.getName().equals("TouchAF")) {
                int m = 0;
                label597: while (true) {
                  if (m < str.getNNodes()) {
                    Node node3 = str.getNode(m);
                    if (node3.getName().equals("Supports")) {
                      if (node3.getNNodes() > 0) {
                        getTouchAFMenuItems().clear();
                        int n = 0;
                        while (true) {
                          if (n < node3.getNNodes()) {
                            Node node4 = node3.getNode(n);
                            getTouchAFMenuItems().add(node4.getValue());
                            n++;
                            continue;
                          } 
                          m++;
                          continue label597;
                        } 
                        break;
                      } 
                      continue;
                    } 
                    if (node3.getName().equals("Default")) {
                      setTouchAFValue(node3.getValue());
                      continue;
                    } 
                    if (node3.getName().equals("Disable")) {
                      if (node3.getNNodes() > 0) {
                        getTouchAFMenuItemsDim().clear();
                        int n = 0;
                        while (true) {
                          if (n < node3.getNNodes()) {
                            DSCMenuItem dSCMenuItem1 = new DSCMenuItem();
                            Node node4 = node3.getNode(n);
                            dSCMenuItem1.setName(node4.getName());
                            dSCMenuItem1.setValue(node4.getValue());
                            getTouchAFMenuItemsDim().add(dSCMenuItem1);
                            n++;
                            continue;
                          } 
                          m++;
                          continue label597;
                        } 
                        break;
                      } 
                      continue;
                    } 
                    continue;
                  } 
                  k++;
                  continue label636;
                } 
                break;
              } 
              if (str.getName().equals("VideoOutValue")) {
                setVideoOutValue(str.getValue());
                continue;
              } 
              if (str.getName().equals("FlashStrobeStatus")) {
                setFlashStrobeStatus(str.getValue());
                continue;
              } 
              if (str.getName().equals("CardStatusValue")) {
                setCardStatusValue(str.getValue());
                continue;
              } 
              continue;
            } 
            j++;
            continue label650;
          } 
          break;
        } 
        if (node1.getName().equals("StreamUrl")) {
          getStreamUrlMenuItems().clear();
          int k = 0;
          while (true) {
            if (k >= node1.getNNodes()) {
              if (getCurrentStreamQuality().equals("high")) {
                k = 0;
                while (true) {
                  if (k < getStreamUrlMenuItems().size()) {
                    dSCMenuItem = getStreamUrlMenuItems().get(k);
                    if (dSCMenuItem.getName().equals("QualityHighUrl")) {
                      setCurrentStreamUrl(dSCMenuItem.getValue());
                    } else {
                      k++;
                      continue;
                    } 
                  } 
                  j++;
                  continue label650;
                } 
                break;
              } 
            } else {
              Node node2 = dSCMenuItem.getNode(k);
              DSCMenuItem dSCMenuItem1 = new DSCMenuItem();
              dSCMenuItem1.setName(node2.getName());
              dSCMenuItem1.setValue(node2.getValue());
              getStreamUrlMenuItems().add(dSCMenuItem1);
              k++;
              continue;
            } 
            if (getCurrentStreamQuality().equals("low")) {
              k = 0;
              while (true) {
                if (k < getStreamUrlMenuItems().size()) {
                  dSCMenuItem = getStreamUrlMenuItems().get(k);
                  if (dSCMenuItem.getName().equals("QualityLowUrl")) {
                    setCurrentStreamUrl(dSCMenuItem.getValue());
                  } else {
                    k++;
                    continue;
                  } 
                } 
                j++;
                continue label650;
              } 
              break;
            } 
            continue label650;
          } 
          break;
        } 
        if (dSCMenuItem.getName().equals("GETIPRESULT")) {
          getStreamUrlMenuItems().clear();
          DSCMenuItem dSCMenuItem1 = new DSCMenuItem();
          dSCMenuItem1.setName(dSCMenuItem.getName());
          dSCMenuItem1.setValue(dSCMenuItem.getValue());
          getStreamUrlMenuItems().add(dSCMenuItem1);
          setCurrentStreamUrl(((DSCMenuItem)getStreamUrlMenuItems().get(0)).getValue());
          continue;
        } 
        if (dSCMenuItem.getName().equals("ShutterSpeedValue")) {
          setShutterSpeedValue(dSCMenuItem.getValue());
          continue;
        } 
        if (dSCMenuItem.getName().equals("ApertureValue")) {
          setApertureValue(dSCMenuItem.getValue());
          continue;
        } 
        if (dSCMenuItem.getName().equals("EVValue")) {
          setEVValue(dSCMenuItem.getValue());
          continue;
        } 
        continue;
      } 
      break;
    } 
  }
  
  public void sendEvent(int paramInt, Object paramObject) {
    Trace.d(this.TAG, "start sendEvent() name : " + actionStatusToString(paramInt));
    if (this.handler != null) {
      Message message = new Message();
      message.what = paramInt;
      message.obj = paramObject;
      this.handler.sendMessage(message);
    } 
  }
  
  public void setAFAreaAct(String paramString) {
    this.SetAFAreaAct.setArgumentValue("AFAreaValue", paramString);
    sendEvent(56, Boolean.valueOf(true));
    boolean bool = this.SetAFAreaAct.postControlAction();
    if (bool)
      setAFAreaValue(this.SetAFAreaAct.getArgumentValue("AFAreaValue")); 
    sendEvent(57, Boolean.valueOf(bool));
  }
  
  public void setAFModeAct(String paramString) {
    this.SetAFModeAct.setArgumentValue("AFModeValue", paramString);
    sendEvent(54, Boolean.valueOf(true));
    boolean bool = this.SetAFModeAct.postControlAction();
    if (bool)
      setAFModeValue(this.SetAFModeAct.getArgumentValue("AFModeValue")); 
    sendEvent(55, Boolean.valueOf(bool));
  }
  
  public void setAction(Device paramDevice) {
    if (paramDevice != null) {
      ServiceList serviceList = paramDevice.getServiceList();
      int i = 0;
      while (true) {
        if (i < serviceList.size()) {
          Service service = serviceList.getService(i);
          if (service.getServiceType().contains("service:ContentDirectory")) {
            this.GetInformationAct = service.getAction("GetInfomation");
            this.SetResolutionAct = service.getAction("SetResolution");
            this.GetZoomAct = service.getAction("GetZoom");
            this.SetZoomAct = service.getAction("SetZoom");
            this.ZoomINAct = service.getAction("ZoomIN");
            this.ZoomOUTAct = service.getAction("ZoomOUT");
            this.StartZoomAct = service.getAction("StartZoom");
            this.StopZoomAct = service.getAction("StopZoom");
            this.AFAct = service.getAction("AF");
            this.MULTIAFAct = service.getAction("MULTIAF");
            this.SetTouchAFOptionAct = service.getAction("setTouchAFOption");
            this.TouchAFAct = service.getAction("touchAF");
            this.ReleaseSelfTimerAct = service.getAction("ReleaseSelfTimer");
            this.ShotAct = service.getAction("Shot");
            this.ShotWithGPSAct = service.getAction("ShotWithGPS");
            this.SetLEDAct = service.getAction("SetLED");
            this.SetFlashAct = service.getAction("SetFlash");
            this.SetStreamQualityAct = service.getAction("SetStreamQuality");
            this.SetMovieStreamQualityAct = service.getAction("SetStreamQuality");
            this.ShutterUpAct = service.getAction("ShutterUp");
            this.SetShutterSpeedAct = service.getAction("SetShutterSpeed");
            this.SetApertureAct = service.getAction("SetAperture");
            this.SetEVAct = service.getAction("SetEV");
            this.SetISOAct = service.getAction("SetISO");
            this.SetWBAct = service.getAction("SetWB");
            this.SetMeteringAct = service.getAction("SetMetering");
            this.SetAFModeAct = service.getAction("SetAFMode");
            this.SetAFAreaAct = service.getAction("SetAFArea");
            this.SetDriveAct = service.getAction("SetDrive");
            this.StartShotAct = service.getAction("StartShot");
            this.StopShotAct = service.getAction("StopShot");
            this.SetDialModeAct = service.getAction("SetDialMode");
            this.SetQualityAct = service.getAction("SetQuality");
            this.SetMovieResolutionAct = service.getAction("SetMovieResolution");
            this.StartRecordAct = service.getAction("StartRecord");
            this.StopRecordAct = service.getAction("StopRecord");
            this.SetFileSaveAct = service.getAction("SetFileSave");
            this.TouchAFPointAct = service.getAction("touchAFPoint");
            this.CancelTouchAFMovieAct = service.getAction("CancelTouchAFMovie");
            this.TouchAFMovieAct = service.getAction("touchAFMovie");
            this.BrowseAct = service.getAction("Browse");
            this.GetDeviceConfigurationAct = service.getAction("GetDeviceConfiguration");
            this.SetOperationStateAct = service.getAction("SetOperationState");
            this.SetTotalCopyItemsAct = service.getAction("SetTotalCopyItems");
            return;
          } 
          i++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public void setApertureAct(String paramString) {
    this.SetApertureAct.setArgumentValue("ApertureValue", paramString);
    sendEvent(44, Boolean.valueOf(true));
    boolean bool = this.SetApertureAct.postControlAction();
    if (bool) {
      setApertureValue(this.SetApertureAct.getArgumentValue("ApertureValue"));
      resetCamera(this.SetApertureAct.getActionNode());
    } 
    sendEvent(45, Boolean.valueOf(bool));
  }
  
  public void setDialModeAct(String paramString) {
    this.SetDialModeAct.setArgumentValue("DialModeValue", paramString);
    sendEvent(64, Boolean.valueOf(true));
    boolean bool = this.SetDialModeAct.postControlAction();
    if (bool)
      resetCamera(this.SetDialModeAct.getActionNode()); 
    sendEvent(65, Boolean.valueOf(bool));
  }
  
  public void setDriveAct(String paramString) {
    this.SetDriveAct.setArgumentValue("DriveValue", paramString);
    sendEvent(58, Boolean.valueOf(true));
    boolean bool = this.SetDriveAct.postControlAction();
    if (bool) {
      if (this.SetDriveAct.getArgumentValue("DriveValue").toUpperCase(Locale.ENGLISH).equals("TIMER (2SEC)")) {
        setCurrentLEDTimeMenuItem("Timer (2sec)");
      } else if (this.SetDriveAct.getArgumentValue("DriveValue").toUpperCase(Locale.ENGLISH).equals("TIMER (5SEC)")) {
        setCurrentLEDTimeMenuItem("Timer (5sec)");
      } else if (this.SetDriveAct.getArgumentValue("DriveValue").toUpperCase(Locale.ENGLISH).equals("TIMER (10SEC)")) {
        setCurrentLEDTimeMenuItem("Timer (10sec)");
      } else {
        setCurrentLEDTimeMenuItem("Single");
      } 
      setDriveValue(this.SetDriveAct.getArgumentValue("DriveValue"));
    } 
    sendEvent(59, Boolean.valueOf(bool));
  }
  
  public void setEVAct(String paramString) {
    this.SetEVAct.setArgumentValue("EVValue", paramString);
    sendEvent(46, Boolean.valueOf(true));
    boolean bool = this.SetEVAct.postControlAction();
    if (bool) {
      setEVValue(this.SetEVAct.getArgumentValue("EVValue"));
      resetCamera(this.SetEVAct.getActionNode());
    } 
    sendEvent(47, Boolean.valueOf(bool));
  }
  
  public void setFileSave(String paramString) {
    this.SetFileSaveAct.setArgumentValue("FileSaveValue", paramString);
    sendEvent(74, Boolean.valueOf(true));
    boolean bool = this.SetFileSaveAct.postControlAction();
    if (bool)
      setFileSaveValue(this.SetFileSaveAct.getArgumentValue("FileSaveValue")); 
    sendEvent(75, Boolean.valueOf(bool));
  }
  
  public void setFlashAct(String paramString) {
    this.SetFlashAct.setArgumentValue("FLASHMODE", paramString);
    sendEvent(34, Boolean.valueOf(true));
    boolean bool = this.SetFlashAct.postControlAction();
    if (bool) {
      setDefaultFlash(this.SetFlashAct.getArgumentValue("FLASHMODE"));
      setCurrentFlashDisplay(this.SetFlashAct.getArgumentValue("FLASHMODE"));
      Node node = ((ActionData)this.SetFlashAct.getActionNode().getUserData()).getControlResponse().getBodyNode();
      int i = 0;
      label28: while (true) {
        if (i < node.getNNodes()) {
          Node node1 = node.getNode(i);
          int j = 0;
          while (true) {
            if (j < node1.getNNodes()) {
              Node node2 = node1.getNode(j);
              if (node2.getName().equals("CurrentFlashDisplay")) {
                if (node2.getValue().equals("1")) {
                  setCurrentFlashDisplay("auto");
                } else if (node2.getValue().equals("0")) {
                  setCurrentFlashDisplay("off");
                } else if (node2.getValue().equals("On")) {
                  setCurrentFlashDisplay(getDefaultFlash());
                } else {
                  setCurrentFlashDisplay(node2.getValue());
                } 
              } else {
                j++;
                continue;
              } 
            } 
            i++;
            continue label28;
          } 
          break;
        } 
        sendEvent(35, Boolean.valueOf(bool));
        return;
      } 
    } 
    sendEvent(35, Boolean.valueOf(bool));
  }
  
  public void setHandler(Handler paramHandler) {
    this.handler = paramHandler;
  }
  
  public void setISOAct(String paramString) {
    this.SetISOAct.setArgumentValue("ISOValue", paramString);
    sendEvent(48, Boolean.valueOf(true));
    boolean bool = this.SetISOAct.postControlAction();
    if (bool) {
      setISOValue(this.SetISOAct.getArgumentValue("ISOValue"));
      resetCamera(this.SetISOAct.getActionNode());
    } 
    sendEvent(49, Boolean.valueOf(bool));
  }
  
  public void setLEDAct(String paramString) {
    this.SetLEDAct.setArgumentValue("LEDTIME", paramString);
    sendEvent(32, Boolean.valueOf(true));
    boolean bool = this.SetLEDAct.postControlAction();
    if (bool) {
      paramString = this.SetLEDAct.getArgumentValue("LEDTIME");
      if (paramString.equals("2")) {
        setCurrentLEDTimeMenuItem("2sec");
        setDriveValue("Timer (2sec)");
      } else if (paramString.equals("5")) {
        setCurrentLEDTimeMenuItem("5sec");
        setDriveValue("Timer (5sec)");
      } else if (paramString.equals("10")) {
        setCurrentLEDTimeMenuItem("10sec");
        setDriveValue("Timer (10sec)");
      } else if (paramString.equals("Double")) {
        setCurrentLEDTimeMenuItem("Double");
        setDriveValue("Timer (Double)");
      } else {
        setCurrentLEDTimeMenuItem("Off");
        setDriveValue("SINGLE");
      } 
    } 
    sendEvent(33, Boolean.valueOf(bool));
  }
  
  public void setMeteringAct(String paramString) {
    this.SetMeteringAct.setArgumentValue("MeteringValue", paramString);
    sendEvent(52, Boolean.valueOf(true));
    boolean bool = this.SetMeteringAct.postControlAction();
    if (bool)
      setMeteringValue(this.SetMeteringAct.getArgumentValue("MeteringValue")); 
    sendEvent(53, Boolean.valueOf(bool));
  }
  
  public void setMovieResolutionAct(String paramString) {
    this.SetMovieResolutionAct.setArgumentValue("MovieResolutionValue", paramString);
    sendEvent(68, Boolean.valueOf(true));
    boolean bool = this.SetMovieResolutionAct.postControlAction();
    if (bool) {
      setRemainRecTimeValue(this.SetMovieResolutionAct.getArgumentValue("RemainRecTime"));
      setMovieResolutionRatio(this.SetMovieResolutionAct.getArgumentValue("MovieResolutionRatio"));
      setMovieResolutionValue(this.SetMovieResolutionAct.getArgumentValue("MovieResolutionValue"));
    } 
    sendEvent(69, Boolean.valueOf(bool));
  }
  
  public void setMovieStreamQualityAct(String paramString) {
    this.SetMovieStreamQualityAct.setArgumentValue("Quality", paramString);
    sendEvent(38, Boolean.valueOf(true));
    boolean bool = this.SetMovieStreamQualityAct.postControlAction();
    if (bool) {
      setCurrentStreamQuality(this.SetMovieStreamQualityAct.getArgumentValue("Quality"));
      if (getCurrentStreamQuality().equals("low")) {
        paramString = "QualityLowMjpgUrl";
      } else {
        paramString = "QualityHighMjpgUrl";
      } 
      int i = 0;
      while (true) {
        if (i < getStreamUrlMenuItems().size()) {
          DSCMenuItem dSCMenuItem = getStreamUrlMenuItems().get(i);
          if (dSCMenuItem.getName().equals(paramString)) {
            setCurrentStreamUrl(dSCMenuItem.getValue());
          } else {
            i++;
            continue;
          } 
        } 
        sendEvent(39, Boolean.valueOf(bool));
        return;
      } 
    } 
    sendEvent(39, Boolean.valueOf(bool));
  }
  
  public void setOperationState(String paramString) {
    this.SetOperationStateAct.setArgumentValue("StateEvent", paramString);
    sendEvent(86, Boolean.valueOf(true));
    this.SetOperationStateAct.postControlAction();
    sendEvent(87, (Object)null);
  }
  
  public void setQualityAct(String paramString) {
    this.SetQualityAct.setArgumentValue("QualityValue", paramString);
    sendEvent(66, Boolean.valueOf(true));
    boolean bool = this.SetQualityAct.postControlAction();
    if (bool) {
      setQualityValue(this.SetQualityAct.getArgumentValue("QualityValue"));
      setAvailShots(this.SetQualityAct.getArgumentValue("AVAILSHOTS"));
      if (getRatioOffsetMenuItems().size() > 0)
        if (getQualityValue().toUpperCase(Locale.ENGLISH).contains("RAW")) {
          setRatioValue("3:2");
          setRatioOffsetValue(getRatioOffsetMenuItems().get(0));
        } else {
          DSCResolution dSCResolution = getResolutionMenuItems().get(Integer.parseInt(getDefaultResolutionIndex()));
          int i = 0;
          while (true) {
            if (i < getRatioOffsetMenuItems().size()) {
              DSCResolution dSCResolution1 = getRatioOffsetMenuItems().get(i);
              if (dSCResolution.getRatio().equals(dSCResolution1.getRatio())) {
                setRatioValue(dSCResolution1.getRatio());
                setRatioOffsetValue(dSCResolution1);
              } else {
                i++;
                continue;
              } 
            } 
            sendEvent(67, Boolean.valueOf(bool));
            return;
          } 
        }  
    } 
    sendEvent(67, Boolean.valueOf(bool));
  }
  
  public void setResolutionAct(String paramString) {
    this.SetResolutionAct.setArgumentValue("RESOLUTION", paramString);
    sendEvent(4, Boolean.valueOf(true));
    boolean bool = this.SetResolutionAct.postControlAction();
    if (bool) {
      Node node = ((ActionData)this.SetResolutionAct.getActionNode().getUserData()).getControlResponse().getBodyNode();
      int i = 0;
      label32: while (true) {
        if (i < node.getNNodes()) {
          Node node1 = node.getNode(i);
          int j = 0;
          label27: while (true) {
            if (j >= node1.getNNodes()) {
              i++;
              continue label32;
            } 
            Node node2 = node1.getNode(j);
            if (node2.getName().equals("AVAILSHOTS")) {
              if (!node2.getValue().equals("")) {
                setAvailShots(node2.getValue());
                continue;
              } 
              continue;
            } 
            if (node2.getName().equals("ResolutionRatio")) {
              setRatioValue(node2.getValue());
              int k = 0;
              while (true) {
                if (k < getRatioOffsetMenuItems().size())
                  if (((DSCResolution)getRatioOffsetMenuItems().get(k)).getRatio().equals(node2.getValue())) {
                    setRatioOffsetValue(getRatioOffsetMenuItems().get(k));
                  } else {
                    k++;
                    continue;
                  }  
                j++;
                continue label27;
              } 
              break;
            } 
            continue;
          } 
          break;
        } 
        sendEvent(5, Boolean.valueOf(bool));
        return;
      } 
    } 
    sendEvent(5, Boolean.valueOf(bool));
  }
  
  public void setShutterSpeedAct(String paramString) {
    this.SetShutterSpeedAct.setArgumentValue("ShutterSpeedValue", paramString);
    sendEvent(42, Boolean.valueOf(true));
    boolean bool = this.SetShutterSpeedAct.postControlAction();
    if (bool) {
      setShutterSpeedValue(this.SetShutterSpeedAct.getArgumentValue("ShutterSpeedValue"));
      resetCamera(this.SetShutterSpeedAct.getActionNode());
    } 
    sendEvent(43, Boolean.valueOf(bool));
  }
  
  public void setStreamQualityAct(String paramString) {
    this.SetStreamQualityAct.setArgumentValue("Quality", paramString);
    sendEvent(36, Boolean.valueOf(true));
    boolean bool = this.SetStreamQualityAct.postControlAction();
    if (bool) {
      setCurrentStreamQuality(this.SetStreamQualityAct.getArgumentValue("Quality"));
      if (getCurrentStreamQuality().equals("low")) {
        paramString = "QualityLowUrl";
      } else {
        paramString = "QualityHighUrl";
      } 
      int i = 0;
      while (true) {
        if (i < getStreamUrlMenuItems().size()) {
          DSCMenuItem dSCMenuItem = getStreamUrlMenuItems().get(i);
          if (dSCMenuItem.getName().equals(paramString)) {
            setCurrentStreamUrl(dSCMenuItem.getValue());
          } else {
            i++;
            continue;
          } 
        } 
        sendEvent(37, Boolean.valueOf(bool));
        return;
      } 
    } 
    sendEvent(37, Boolean.valueOf(bool));
  }
  
  public void setTotalCopyItems(String paramString) {
    this.SetTotalCopyItemsAct.setArgumentValue("TotalNumber", paramString);
    sendEvent(88, Boolean.valueOf(true));
    sendEvent(89, Boolean.valueOf(this.SetTotalCopyItemsAct.postControlAction()));
  }
  
  public void setTouchAFOptionAct(String paramString) {
    this.SetTouchAFOptionAct.setArgumentValue("TOUCH_AF_OPTION", paramString);
    sendEvent(22, Boolean.valueOf(true));
    boolean bool = this.SetTouchAFOptionAct.postControlAction();
    if (bool)
      setTouchAFValue(this.SetTouchAFOptionAct.getArgumentValue("TOUCH_AF_OPTION")); 
    sendEvent(23, Boolean.valueOf(bool));
  }
  
  public void setWBAct(String paramString) {
    this.SetWBAct.setArgumentValue("WBValue", paramString);
    sendEvent(50, Boolean.valueOf(true));
    boolean bool = this.SetWBAct.postControlAction();
    if (bool)
      setWBValue(this.SetWBAct.getArgumentValue("WBValue")); 
    sendEvent(51, Boolean.valueOf(bool));
  }
  
  public void setZoomAct(String paramString) {
    this.SetZoomAct.setArgumentValue("ZOOMLEVEL", paramString);
    sendEvent(12, Boolean.valueOf(true));
    boolean bool = this.SetZoomAct.postControlAction();
    if (bool) {
      ArgumentList argumentList = this.SetZoomAct.getOutputArgumentList();
      int j = argumentList.size();
      int i = 0;
      while (true) {
        if (i < j) {
          Argument argument = argumentList.getArgument(i);
          String str1 = argument.getName();
          String str2 = argument.getValue();
          if (str1.equals("CURRENTZOOM"))
            setDefaultZoom(str2); 
          i++;
          continue;
        } 
        sendEvent(13, Boolean.valueOf(bool));
        return;
      } 
    } 
    sendEvent(13, Boolean.valueOf(bool));
  }
  
  public void shotAct(String paramString) {
    this.ShotAct.setArgumentValue("REQUESTIMAGE", paramString);
    sendEvent(28, Boolean.valueOf(true));
    setAFShotResult("");
    boolean bool = this.ShotAct.postControlAction();
    if (bool) {
      Node node = ((ActionData)this.ShotAct.getActionNode().getUserData()).getControlResponse().getBodyNode();
      int i = 0;
      label28: while (true) {
        if (i < node.getNNodes()) {
          Node node1 = node.getNode(i);
          for (int j = 0;; j++) {
            if (j >= node1.getNNodes()) {
              i++;
              continue label28;
            } 
            Node node2 = node1.getNode(j);
            if (node2.getName().equals("AFSHOTRESULT")) {
              setAFShotResult(node2.getValue());
            } else if (node2.getName().equals("AVAILSHOTS")) {
              if (!node2.getValue().equals(""))
                setAvailShots(node2.getValue()); 
            } else if (node2.getName().equals("CurrentFlashDisplay")) {
              setCurrentFlashDisplay(node2.getValue());
            } else if (node2.getName().equals("RemainRecTime")) {
              setRemainRecTimeValue(node2.getValue());
            } 
          } 
          break;
        } 
        sendEvent(29, Boolean.valueOf(bool));
        return;
      } 
    } 
    sendEvent(29, Boolean.valueOf(bool));
  }
  
  public void shotWithGPSAct() {
    sendEvent(30, Boolean.valueOf(true));
    this.ShotWithGPSAct.postControlAction();
    sendEvent(31, (Object)null);
  }
  
  public void shutterUpAct() {
    sendEvent(40, Boolean.valueOf(true));
    sendEvent(41, Boolean.valueOf(this.ShutterUpAct.postControlAction()));
  }
  
  public void startRecord() {
    sendEvent(70, Boolean.valueOf(true));
    boolean bool = this.StartRecordAct.postControlAction();
    if (bool)
      setRemainRecTimeValue(this.StartRecordAct.getArgumentValue("RemainRecTime")); 
    sendEvent(71, Boolean.valueOf(bool));
  }
  
  public void startShotAct(String paramString) {
    this.StartShotAct.setArgumentValue("DriveValue", paramString);
    sendEvent(60, Boolean.valueOf(true));
    sendEvent(61, Boolean.valueOf(this.StartShotAct.postControlAction()));
  }
  
  public void startZoomAct(String paramString) {
    this.StartZoomAct.setArgumentValue("ZoomDirection", paramString);
    sendEvent(14, Boolean.valueOf(true));
    sendEvent(15, Boolean.valueOf(this.StartZoomAct.postControlAction()));
  }
  
  public void stopRecord() {
    sendEvent(72, Boolean.valueOf(true));
    boolean bool = this.StopRecordAct.postControlAction();
    if (bool) {
      String str;
      setRemainRecTimeValue(this.StopRecordAct.getArgumentValue("RemainRecTime"));
      if (!this.StopRecordAct.getArgumentValue("AVAILSHOTS").equals(""))
        setAvailShots(this.StopRecordAct.getArgumentValue("AVAILSHOTS")); 
      if (getCurrentStreamQuality().equals("low")) {
        str = "QualityLowUrl";
      } else {
        str = "QualityHighUrl";
      } 
      int i = 0;
      while (true) {
        if (i < getStreamUrlMenuItems().size()) {
          DSCMenuItem dSCMenuItem = getStreamUrlMenuItems().get(i);
          if (dSCMenuItem.getName().equals(str)) {
            setCurrentStreamUrl(dSCMenuItem.getValue());
          } else {
            i++;
            continue;
          } 
        } 
        sendEvent(73, Boolean.valueOf(bool));
        return;
      } 
    } 
    sendEvent(73, Boolean.valueOf(bool));
  }
  
  public void stopShotAct(String paramString) {
    this.StopShotAct.setArgumentValue("DriveValue", paramString);
    sendEvent(62, Boolean.valueOf(true));
    sendEvent(63, Boolean.valueOf(this.StopShotAct.postControlAction()));
  }
  
  public void stopZoomAct(String paramString) {
    this.StopZoomAct.setArgumentValue("ZoomDirection", paramString);
    sendEvent(16, Boolean.valueOf(true));
    boolean bool = this.StopZoomAct.postControlAction();
    if (bool) {
      ArgumentList argumentList = this.StopZoomAct.getOutputArgumentList();
      int j = argumentList.size();
      int i = 0;
      while (true) {
        if (i < j) {
          Argument argument = argumentList.getArgument(i);
          String str1 = argument.getName();
          String str2 = argument.getValue();
          if (str1.equals("CURRENTZOOM") && !str2.equals(""))
            setDefaultZoom(str2); 
          i++;
          continue;
        } 
        sendEvent(17, Boolean.valueOf(bool));
        return;
      } 
    } 
    sendEvent(17, Boolean.valueOf(bool));
  }
  
  public void touchAFAct(String paramString) {
    this.TouchAFAct.setArgumentValue("AFPOSITION", paramString);
    sendEvent(24, Boolean.valueOf(true));
    sendEvent(25, Boolean.valueOf(this.TouchAFAct.postControlAction()));
  }
  
  public void touchAFMovie(String paramString) {
    this.TouchAFMovieAct.setArgumentValue("AFPOSITION", paramString);
    sendEvent(80, Boolean.valueOf(true));
    sendEvent(81, Boolean.valueOf(this.TouchAFMovieAct.postControlAction()));
  }
  
  public void touchAFPoint(String paramString) {
    this.TouchAFPointAct.setArgumentValue("AFPOSITION", paramString);
    sendEvent(76, Boolean.valueOf(true));
    sendEvent(77, Boolean.valueOf(this.TouchAFPointAct.postControlAction()));
  }
  
  public void zoomINAct() {
    sendEvent(6, Boolean.valueOf(true));
    boolean bool = this.ZoomINAct.postControlAction();
    if (bool) {
      ArgumentList argumentList = this.ZoomINAct.getOutputArgumentList();
      int j = argumentList.size();
      int i = 0;
      while (true) {
        if (i < j) {
          Argument argument = argumentList.getArgument(i);
          String str1 = argument.getName();
          String str2 = argument.getValue();
          if (str1.equals("CURRENTZOOM"))
            setDefaultZoom(str2); 
          i++;
          continue;
        } 
        sendEvent(7, Boolean.valueOf(bool));
        return;
      } 
    } 
    sendEvent(7, Boolean.valueOf(bool));
  }
  
  public void zoomOUTAct() {
    sendEvent(8, Boolean.valueOf(true));
    boolean bool = this.ZoomOUTAct.postControlAction();
    if (bool) {
      ArgumentList argumentList = this.ZoomOUTAct.getOutputArgumentList();
      int j = argumentList.size();
      int i = 0;
      while (true) {
        if (i < j) {
          Argument argument = argumentList.getArgument(i);
          String str1 = argument.getName();
          String str2 = argument.getValue();
          if (str1.equals("CURRENTZOOM"))
            setDefaultZoom(str2); 
          i++;
          continue;
        } 
        sendEvent(9, Boolean.valueOf(bool));
        return;
      } 
    } 
    sendEvent(9, Boolean.valueOf(bool));
  }
  
  public static class ActionID {
    public static final int AUTO_FOCUS_ACTION_ID = 9;
    
    public static final int BROWSE_ACTION_ID = 41;
    
    public static final int CANCEL_TOUCH_AF_MOVIE_ACTION_ID = 39;
    
    public static final int GET_DEVICE_CONFIGURATION_ACTION_ID = 42;
    
    public static final int GET_INFORMATION_ACTION_ID = 1;
    
    public static final int GET_ZOOM_ACTION_ID = 5;
    
    public static final int MULTI_AUTO_FOCUS_ACTION_ID = 10;
    
    public static final int NONE_ACTION_ID = 0;
    
    public static final int RELEASE_SELF_TIMER_ACTION_ID = 13;
    
    public static final int SET_AF_AREA_ACTION_ID = 28;
    
    public static final int SET_AF_MODE_ACTION_ID = 27;
    
    public static final int SET_APERTURE_ACTION_ID = 22;
    
    public static final int SET_DIAL_MODE_ACTION_ID = 32;
    
    public static final int SET_DRIVE_ACTION_ID = 29;
    
    public static final int SET_EV_ACTION_ID = 23;
    
    public static final int SET_FILE_SAVE_ACTION_ID = 37;
    
    public static final int SET_FLASH_ACTION_ID = 17;
    
    public static final int SET_ISO_ACTION_ID = 24;
    
    public static final int SET_LED_ACTION_ID = 16;
    
    public static final int SET_METERING_ACTION_ID = 26;
    
    public static final int SET_MOVIE_RESOLUTION_ACTION_ID = 34;
    
    public static final int SET_MOVIE_STREAM_QUALITY_ACTION_ID = 19;
    
    public static final int SET_OPERATION_STATE_ACTION_ID = 43;
    
    public static final int SET_QUALITY_ACTION_ID = 33;
    
    public static final int SET_RESOLUTION_ACTION_ID = 2;
    
    public static final int SET_SHUTTER_SPEED_ACTION_ID = 21;
    
    public static final int SET_STREAM_QUALITY_ACTION_ID = 18;
    
    public static final int SET_TOTAL_COPY_ITEMS_ACTION_ID = 44;
    
    public static final int SET_TOUCH_AUTO_FOCUS_OPTION_ACTION_ID = 11;
    
    public static final int SET_WB_ACTION_ID = 25;
    
    public static final int SET_ZOOM_ACTION_ID = 6;
    
    public static final int SHOT_ACTION_ID = 14;
    
    public static final int SHOT_WITH_GPS_ACTION_ID = 15;
    
    public static final int SHUTTER_UP_ACTION_ID = 20;
    
    public static final int START_RECORD_ACTION_ID = 35;
    
    public static final int START_SHOT_ACTION_ID = 30;
    
    public static final int START_ZOOM_ACTION_ID = 7;
    
    public static final int STOP_RECORD_ACTION_ID = 36;
    
    public static final int STOP_SHOT_ACTION_ID = 31;
    
    public static final int STOP_ZOOM_ACTION_ID = 8;
    
    public static final int TOUCH_AF_MOVIE_ACTION_ID = 40;
    
    public static final int TOUCH_AF_POINT_ACTION_ID = 38;
    
    public static final int TOUCH_AUTO_FOCUS_ACTION_ID = 12;
    
    public static final int ZOOM_IN_ACTION_ID = 3;
    
    public static final int ZOOM_OUT_ACTION_ID = 4;
  }
  
  public static class ActionName {
    public static final String AUTO_FOCUS_ACTION_NAME = "AF";
    
    public static final String BROWSE_ACTION_NAME = "Browse";
    
    public static final String CANCEL_TOUCH_AF_MOVIE_ACTION_NAME = "CancelTouchAFMovie";
    
    public static final String GET_DEVICE_CONFIGURATION_ACTION_NAME = "GetDeviceConfiguration";
    
    public static final String GET_INFOMATION_ACTION_NAME = "GetInfomation";
    
    public static final String GET_ZOOM_ACTION_NAME = "GetZoom";
    
    public static final String MULTI_AUTO_FOCUS_ACTION_NAME = "MULTIAF";
    
    public static final String RELEASE_SELF_TIMER_ACTION_NAME = "ReleaseSelfTimer";
    
    public static final String SET_AF_AREA_ACTION_NAME = "SetAFArea";
    
    public static final String SET_AF_MODE_ACTION_NAME = "SetAFMode";
    
    public static final String SET_APERTURE_ACTION_NAME = "SetAperture";
    
    public static final String SET_DIAL_MODE_ACTION_NAME = "SetDialMode";
    
    public static final String SET_DRIVE_ACTION_NAME = "SetDrive";
    
    public static final String SET_EV_ACTION_NAME = "SetEV";
    
    public static final String SET_FILE_SAVE_ACTION_NAME = "SetFileSave";
    
    public static final String SET_FLASH_ACTION_NAME = "SetFlash";
    
    public static final String SET_ISO_ACTION_NAME = "SetISO";
    
    public static final String SET_LED_ACTION_NAME = "SetLED";
    
    public static final String SET_METERING_ACTION_NAME = "SetMetering";
    
    public static final String SET_MOVIE_RESOLUTION_ACTION_NAME = "SetMovieResolution";
    
    public static final String SET_MOVIE_STREAM_QUALITY_ACTION_NAME = "SetStreamQuality";
    
    public static final String SET_OPERATION_STATE_ACTION_NAME = "SetOperationState";
    
    public static final String SET_QUALITY_ACTION_NAME = "SetQuality";
    
    public static final String SET_RESOLUTION_ACTION_NAME = "SetResolution";
    
    public static final String SET_SHUTTER_SPEED_ACTION_NAME = "SetShutterSpeed";
    
    public static final String SET_STREAM_QUALITY_ACTION_NAME = "SetStreamQuality";
    
    public static final String SET_TOTAL_COPY_ITEMS_ACTION_NAME = "SetTotalCopyItems";
    
    public static final String SET_TOUCH_AUTO_FOCUS_OPTION_ACTION_NAME = "setTouchAFOption";
    
    public static final String SET_WB_ACTION_NAME = "SetWB";
    
    public static final String SET_ZOOM_ACTION_NAME = "SetZoom";
    
    public static final String SHOT_ACTION_NAME = "Shot";
    
    public static final String SHOT_WITH_GPS_ACTION_NAME = "ShotWithGPS";
    
    public static final String SHUTTER_UP_ACTION_NAME = "ShutterUp";
    
    public static final String START_RECORD_ACTION_NAME = "StartRecord";
    
    public static final String START_SHOT_ACTION_NAME = "StartShot";
    
    public static final String START_ZOOM_ACTION_NAME = "StartZoom";
    
    public static final String STOP_RECORD_ACTION_NAME = "StopRecord";
    
    public static final String STOP_SHOT_ACTION_NAME = "StopShot";
    
    public static final String STOP_ZOOM_ACTION_NAME = "StopZoom";
    
    public static final String TOUCH_AF_MOVIE_ACTION_NAME = "touchAFMovie";
    
    public static final String TOUCH_AF_POINT_ACTION_NAME = "touchAFPoint";
    
    public static final String TOUCH_AUTO_FOCUS_ACTION_NAME = "touchAF";
    
    public static final String ZOOM_IN_ACTION_NAME = "ZoomIN";
    
    public static final String ZOOM_OUT_ACTION_NAME = "ZoomOUT";
  }
  
  public static class ActionNode {
    public static final String AF_AREA_VALUE = "AFAreaValue";
    
    public static final String AF_MODE_VALUE = "AFModeValue";
    
    public static final String AF_POSITION = "AFPOSITION";
    
    public static final String APERTURE_VALUE = "ApertureValue";
    
    public static final String AUTO_FOCUS = "AutoFocus";
    
    public static final String AUTO_FOCUS_PRIORITY = "AFPriority";
    
    public static final String AVAILSHOTS = "AVAILSHOTS";
    
    public static final String CARD_STATUS_VALUE = "CardStatusValue";
    
    public static final String CURRENT_FLASH_DISPLAY = "CurrentFlashDisplay";
    
    public static final String DEFAULT_FLASH = "Defaultflash";
    
    public static final String DEFAULT_FOCUS_STATE = "DefaultFocusState";
    
    public static final String DEFAULT_ZOOM = "DefaultZoom";
    
    public static final String DIAL_MODE_VALUE = "DialModeValue";
    
    public static final String DRIVE_VALUE = "DriveValue";
    
    public static final String EV_VALUE = "EVValue";
    
    public static final String FILE_SAVE_VALUE = "FileSaveValue";
    
    public static final String FLASH = "Flash";
    
    public static final String FLASH_DISPLAY = "FlashDisplay";
    
    public static final String FLASH_STROBE_STATUS = "FlashStrobeStatus";
    
    public static final String GET_INFORMATION_RESULT = "GETINFORMATIONRESULT";
    
    public static final String GET_IP_RESULT = "GETIPRESULT";
    
    public static final String ISO_VALUE = "ISOValue";
    
    public static final String MAX_ZOOM = "MaxZoom";
    
    public static final String METERING_VALUE = "MeteringValue";
    
    public static final String MIN_ZOOM = "MinZoom";
    
    public static final String MOVIE_RESOLUTION_VALUE = "MovieResolutionValue";
    
    public static final String MOVIE_TOUCH_AF_STATUS = "MovieTouchAFStatus";
    
    public static final String MULTI_AF_MATRIX_SIZE = "MultiAFMatrixSize";
    
    public static final String QUALITY_HIGH_URL = "QualityHighUrl";
    
    public static final String QUALITY_LOW_URL = "QualityLowUrl";
    
    public static final String QUALITY_VALUE = "QualityValue";
    
    public static final String RATIO_OFFSET = "RatioOffset";
    
    public static final String REMAIN_REC_TIME = "RemainRecTime";
    
    public static final String RESOLUTIONS = "Resolutions";
    
    public static final String RESOLUTION_RATIO = "ResolutionRatio";
    
    public static final String ROTATION = "ROTATION";
    
    public static final String SHUTTER_SPEED_VALUE = "ShutterSpeedValue";
    
    public static final String STREAMING_URLS = "StreamUrl";
    
    public static final String STREAM_QUALITY = "StreamQuality";
    
    public static final String TOUCH_AUTO_FOCUS = "TouchAF";
    
    public static final String VERSION = "Version";
    
    public static final String VIDEO_OUT_VALUE = "VideoOutValue";
    
    public static final String WB_VALUE = "WBValue";
    
    public static final String ZOOM_INFO = "ZoomInfo";
  }
  
  public static class ActionResult {
    public static final int FAIL = 0;
    
    public static final int SUCCESS = 1;
  }
  
  public static class ActionStatus {
    public static final int ACTION_STATUS_NONE = 0;
    
    public static final int ACTION_STATUS_READY = 1;
    
    public static final int AUTO_FOCUS_ACTION_END = 19;
    
    public static final int AUTO_FOCUS_ACTION_START = 18;
    
    public static final int BROWSE_ACTION_END = 83;
    
    public static final int BROWSE_ACTION_START = 82;
    
    public static final int CANCEL_TOUCH_AF_MOVIE_ACTION_END = 79;
    
    public static final int CANCEL_TOUCH_AF_MOVIE_ACTION_START = 78;
    
    public static final int GET_DEVICE_CONFIGURATION_ACTION_END = 85;
    
    public static final int GET_DEVICE_CONFIGURATION_ACTION_START = 84;
    
    public static final int GET_INFORMATION_ACTION_END = 3;
    
    public static final int GET_INFORMATION_ACTION_START = 2;
    
    public static final int GET_ZOOM_ACTION_END = 11;
    
    public static final int GET_ZOOM_ACTION_START = 10;
    
    public static final int MULTI_AUTO_FOCUS_ACTION_END = 21;
    
    public static final int MULTI_AUTO_FOCUS_ACTION_START = 20;
    
    public static final int RELEASE_SELF_TIMER_ACTION_END = 27;
    
    public static final int RELEASE_SELF_TIMER_ACTION_START = 26;
    
    public static final int SET_AF_AREA_ACTION_END = 57;
    
    public static final int SET_AF_AREA_ACTION_START = 56;
    
    public static final int SET_AF_MODE_ACTION_END = 55;
    
    public static final int SET_AF_MODE_ACTION_START = 54;
    
    public static final int SET_APERTURE_ACTION_END = 45;
    
    public static final int SET_APERTURE_ACTION_START = 44;
    
    public static final int SET_DIAL_MODE_ACTION_END = 65;
    
    public static final int SET_DIAL_MODE_ACTION_START = 64;
    
    public static final int SET_DRIVE_ACTION_END = 59;
    
    public static final int SET_DRIVE_ACTION_START = 58;
    
    public static final int SET_EV_ACTION_END = 47;
    
    public static final int SET_EV_ACTION_START = 46;
    
    public static final int SET_FILE_SAVE_ACTION_END = 75;
    
    public static final int SET_FILE_SAVE_ACTION_START = 74;
    
    public static final int SET_FLASH_ACTION_END = 35;
    
    public static final int SET_FLASH_ACTION_START = 34;
    
    public static final int SET_ISO_ACTION_END = 49;
    
    public static final int SET_ISO_ACTION_START = 48;
    
    public static final int SET_LED_ACTION_END = 33;
    
    public static final int SET_LED_ACTION_START = 32;
    
    public static final int SET_METERING_ACTION_END = 53;
    
    public static final int SET_METERING_ACTION_START = 52;
    
    public static final int SET_MOVIE_RESOLUTION_ACTION_END = 69;
    
    public static final int SET_MOVIE_RESOLUTION_ACTION_START = 68;
    
    public static final int SET_MOVIE_STREAM_QUALITY_ACTION_END = 39;
    
    public static final int SET_MOVIE_STREAM_QUALITY_ACTION_START = 38;
    
    public static final int SET_OPERATION_STATE_ACTION_END = 87;
    
    public static final int SET_OPERATION_STATE_ACTION_START = 86;
    
    public static final int SET_QUALITY_ACTION_END = 67;
    
    public static final int SET_QUALITY_ACTION_START = 66;
    
    public static final int SET_RESOLUTION_ACTION_END = 5;
    
    public static final int SET_RESOLUTION_ACTION_START = 4;
    
    public static final int SET_SHUTTER_SPEED_ACTION_END = 43;
    
    public static final int SET_SHUTTER_SPEED_ACTION_START = 42;
    
    public static final int SET_STREAM_QUALITY_ACTION_END = 37;
    
    public static final int SET_STREAM_QUALITY_ACTION_START = 36;
    
    public static final int SET_TOTAL_COPY_ITEMS_ACTION_END = 89;
    
    public static final int SET_TOTAL_COPY_ITEMS_ACTION_START = 88;
    
    public static final int SET_TOUCH_AUTO_FOCUS_OPTION_ACTION_END = 23;
    
    public static final int SET_TOUCH_AUTO_FOCUS_OPTION_ACTION_START = 22;
    
    public static final int SET_WB_ACTION_END = 51;
    
    public static final int SET_WB_ACTION_START = 50;
    
    public static final int SET_ZOOM_ACTION_END = 13;
    
    public static final int SET_ZOOM_ACTION_START = 12;
    
    public static final int SHOT_ACTION_END = 29;
    
    public static final int SHOT_ACTION_START = 28;
    
    public static final int SHOT_WITH_GPS_ACTION_END = 31;
    
    public static final int SHOT_WITH_GPS_ACTION_START = 30;
    
    public static final int SHUTTER_UP_ACTION_END = 41;
    
    public static final int SHUTTER_UP_ACTION_START = 40;
    
    public static final int START_RECORD_ACTION_END = 71;
    
    public static final int START_RECORD_ACTION_START = 70;
    
    public static final int START_SHOT_ACTION_END = 61;
    
    public static final int START_SHOT_ACTION_START = 60;
    
    public static final int START_ZOOM_ACTION_END = 15;
    
    public static final int START_ZOOM_ACTION_START = 14;
    
    public static final int STOP_RECORD_ACTION_END = 73;
    
    public static final int STOP_RECORD_ACTION_START = 72;
    
    public static final int STOP_SHOT_ACTION_END = 63;
    
    public static final int STOP_SHOT_ACTION_START = 62;
    
    public static final int STOP_ZOOM_ACTION_END = 17;
    
    public static final int STOP_ZOOM_ACTION_START = 16;
    
    public static final int TOUCH_AF_MOVIE_ACTION_END = 81;
    
    public static final int TOUCH_AF_MOVIE_ACTION_START = 80;
    
    public static final int TOUCH_AF_POINT_ACTION_END = 77;
    
    public static final int TOUCH_AF_POINT_ACTION_START = 76;
    
    public static final int TOUCH_AUTO_FOCUS_ACTION_END = 25;
    
    public static final int TOUCH_AUTO_FOCUS_ACTION_START = 24;
    
    public static final int ZOOM_IN_ACTION_END = 7;
    
    public static final int ZOOM_IN_ACTION_START = 6;
    
    public static final int ZOOM_OUT_ACTION_END = 9;
    
    public static final int ZOOM_OUT_ACTION_START = 8;
  }
  
  public static class FlashMode {
    public static final String AUTO = "AUTO";
    
    public static final String OFF = "OFF";
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\controller\DeviceController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */