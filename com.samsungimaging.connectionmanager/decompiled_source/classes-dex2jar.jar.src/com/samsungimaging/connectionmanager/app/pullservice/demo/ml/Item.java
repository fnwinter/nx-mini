package com.samsungimaging.connectionmanager.app.pullservice.demo.ml;

import com.samsungimaging.connectionmanager.util.Trace;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.ArgumentList;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.xml.Node;
import org.cybergarage.xml.Parser;
import org.cybergarage.xml.ParserException;

public class Item {
  public static final String ELEM_NAME_OF_CONTAINER = "container";
  
  public static final String ELEM_NAME_OF_ITEM = "item";
  
  private Trace.Tag TAG = Trace.Tag.ML;
  
  private String album;
  
  private Action browseAction = null;
  
  private boolean checkFlag = false;
  
  private String date;
  
  private String id = "0";
  
  private Boolean isContainer = Boolean.valueOf(false);
  
  private boolean isSupported = true;
  
  private ItemList itemList;
  
  private Node itemNode;
  
  private int itemState;
  
  private int mStartingIndex = 0;
  
  private String parentId = "-1";
  
  private String protocolInfo;
  
  private String res;
  
  private String resURL;
  
  private String resolution;
  
  private String screenProtocolInfo;
  
  private String screenRes;
  
  private String screenResolution;
  
  private String screenSize;
  
  private String screenUrl;
  
  private String size;
  
  private String thumbProtocolInfo;
  
  private String thumbRes;
  
  private String thumbResolution;
  
  private String thumbSize;
  
  private String thumbUrl;
  
  private String title;
  
  private String upnpClass;
  
  public Item() {
    Trace.d(this.TAG, "Item()contructor shouldn't be called");
    this.itemNode = new Node("action");
    this.itemList = new ItemList();
    setItemState(1);
    setSupported(true);
    setResolution("");
  }
  
  public Item(Node paramNode, Action paramAction) {
    this.itemNode = paramNode;
    this.browseAction = paramAction;
    this.itemList = new ItemList();
    setItemState(1);
    setSupported(true);
    setResolution("");
  }
  
  private int addItem(Node paramNode, int paramInt) {
    byte b2 = 0;
    Trace.d(this.TAG, "addItem()");
    if (paramNode == null)
      return 0; 
    int j = paramNode.getNNodes();
    paramNode.print();
    byte b1 = 0;
    int i = paramInt;
    while (true) {
      if (b1 < j) {
        Item item;
        if (i <= 0) {
          Trace.d(this.TAG, "Max Full!! break");
          Trace.d(this.TAG, "List : " + getItemList().toString());
          Trace.d(this.TAG, "return res = " + b2);
          return b2;
        } 
        Node node1 = paramNode.getNode(b1);
        String str1 = node1.getName();
        String str2 = node1.getNode("dc:title").getValue();
        Trace.d(this.TAG, str1);
        Trace.d(this.TAG, str2);
        if (node1.getName().equals("container")) {
          item = new Container(node1, this.browseAction);
          item.setIsContainer(Boolean.valueOf(true));
        } else {
          item = new Item(node1, this.browseAction);
        } 
        item.setId(node1.getAttributeValue("id"));
        item.setParentId(getId());
        Node node2 = node1.getNode("dc:title");
        if (node2 != null)
          item.setTitle(node2.getValue()); 
        node2 = node1.getNode("upnp:album");
        if (node2 != null)
          item.setAlbum(node2.getValue()); 
        node2 = node1.getNode("dc:date");
        if (node2 != null) {
          item.setDate(node2.getValue());
        } else {
          item.setDate("0000-00-00");
        } 
        node2 = node1.getNode("upnp:class");
        if (node2 != null)
          item.setUpnpClass(node2.getValue()); 
        boolean bool = false;
        int m = node1.getNNodes();
        paramInt = 0;
        int k = 0;
        while (true) {
          k++;
          object2 = SYNTHETIC_LOCAL_VARIABLE_3;
          object1 = SYNTHETIC_LOCAL_VARIABLE_4;
        } 
        if (item.isContainer()) {
          Trace.d(this.TAG, "add container");
          getItemList().add((E)item);
          paramInt = b2;
          continue;
        } 
        Trace.d(this.TAG, "This is item");
        paramInt = b2 + 1;
        FileSharing.mContainerList.add(item);
        continue;
      } 
      Trace.d(this.TAG, "List : " + getItemList().toString());
      Trace.d(this.TAG, "return res = " + b2);
      return b2;
    } 
  }
  
  public int browse(int paramInt1, int paramInt2) {
    Parser parser;
    boolean bool = false;
    if (!isContainer())
      return 0; 
    this.browseAction.setArgumentValue("ObjectID", this.id);
    this.browseAction.setArgumentValue("BrowseFlag", "BrowseDirectChildren");
    this.browseAction.setArgumentValue("Filter", "*");
    this.browseAction.setArgumentValue("StartingIndex", getmStartingIndex());
    this.browseAction.setArgumentValue("RequestedCount", paramInt1);
    this.browseAction.setArgumentValue("SortCriteria", "");
    Trace.d(this.TAG, "ObjectID=" + this.id + ", StartingIndex=" + getmStartingIndex() + ", RequestedCount=" + paramInt1);
    paramInt1 = bool;
    if (this.browseAction.postControlAction()) {
      ArgumentList argumentList = this.browseAction.getOutputArgumentList();
      argumentList.getArgument("NumberReturned").getValue();
      String str = argumentList.getArgument("Result").getValue();
      try {
        parser = UPnP.getXMLParser();
        if (parser == null) {
          Trace.d(this.TAG, "Error: No parser");
          return 0;
        } 
      } catch (ParserException parserException) {
        parserException.printStackTrace();
        paramInt1 = bool;
        setmStartingIndex(getmStartingIndex() + paramInt1);
        return paramInt1;
      } 
    } else {
      setmStartingIndex(getmStartingIndex() + paramInt1);
      return paramInt1;
    } 
    paramInt1 = addItem(parser.parse((String)parserException), paramInt2);
    setmStartingIndex(getmStartingIndex() + paramInt1);
    return paramInt1;
  }
  
  public void changedToggle() {}
  
  public String getAlbum() {
    return this.album;
  }
  
  public String getDate() {
    return this.date;
  }
  
  public String getId() {
    return this.id;
  }
  
  public Boolean getIsContainer() {
    return this.isContainer;
  }
  
  public ItemList getItemList() {
    return this.itemList;
  }
  
  public Node getItemNode() {
    return this.itemNode;
  }
  
  public int getItemState() {
    return this.itemState;
  }
  
  public String getParentId() {
    return this.parentId;
  }
  
  public String getProtocolInfo() {
    return this.protocolInfo;
  }
  
  public String getRes() {
    return this.res;
  }
  
  public String getResURL() {
    return this.resURL;
  }
  
  public String getResolution() {
    return this.resolution;
  }
  
  public String getScreenProtocolInfo() {
    return this.screenProtocolInfo;
  }
  
  public String getScreenRes() {
    return this.screenRes;
  }
  
  public String getScreenResolution() {
    return this.screenResolution;
  }
  
  public String getScreenSize() {
    return this.screenSize;
  }
  
  public String getScreenUrl() {
    return this.screenUrl;
  }
  
  public String getSize() {
    return this.size;
  }
  
  public String getThumbProtocolInfo() {
    return this.thumbProtocolInfo;
  }
  
  public String getThumbRes() {
    return this.thumbRes;
  }
  
  public String getThumbResolution() {
    return this.thumbResolution;
  }
  
  public String getThumbSize() {
    return this.thumbSize;
  }
  
  public String getThumbUrl() {
    return this.thumbUrl;
  }
  
  public String getTitle() {
    return this.title;
  }
  
  public String getUpnpClass() {
    return this.upnpClass;
  }
  
  public int getmStartingIndex() {
    return this.mStartingIndex;
  }
  
  public boolean isCheckFlag() {
    return this.checkFlag;
  }
  
  public boolean isContainer() {
    return this.isContainer.booleanValue();
  }
  
  public boolean isSupported() {
    return this.isSupported;
  }
  
  public void setAlbum(String paramString) {
    this.album = paramString;
  }
  
  public void setCheckFlag(boolean paramBoolean) {
    this.checkFlag = paramBoolean;
  }
  
  public void setDate(String paramString) {
    this.date = paramString;
  }
  
  public void setId(String paramString) {
    this.id = paramString;
  }
  
  public void setIsContainer(Boolean paramBoolean) {
    this.isContainer = paramBoolean;
  }
  
  public void setItemList(ItemList paramItemList) {
    this.itemList = paramItemList;
  }
  
  public void setItemNode(Node paramNode) {
    this.itemNode = paramNode;
  }
  
  public void setItemState(int paramInt) {
    Trace.d(this.TAG, "start setItemState() itemState : " + paramInt);
    this.itemState = paramInt;
  }
  
  public void setParentId(String paramString) {
    this.parentId = paramString;
  }
  
  public void setProtocolInfo(String paramString) {
    this.protocolInfo = paramString;
  }
  
  public void setRes(String paramString) {
    this.res = paramString;
  }
  
  public void setResURL(String paramString) {
    this.resURL = paramString;
  }
  
  public void setResolution(String paramString) {
    this.resolution = paramString;
  }
  
  public void setScreenProtocolInfo(String paramString) {
    this.screenProtocolInfo = paramString;
  }
  
  public void setScreenRes(String paramString) {
    this.screenRes = paramString;
  }
  
  public void setScreenResolution(String paramString) {
    this.screenResolution = paramString;
  }
  
  public void setScreenSize(String paramString) {
    this.screenSize = paramString;
  }
  
  public void setScreenUrl(String paramString) {
    this.screenUrl = paramString;
  }
  
  public void setSize(String paramString) {
    this.size = paramString;
  }
  
  public void setSupported(boolean paramBoolean) {
    this.isSupported = paramBoolean;
  }
  
  public void setThumbProtocolInfo(String paramString) {
    this.thumbProtocolInfo = paramString;
  }
  
  public void setThumbRes(String paramString) {
    this.thumbRes = paramString;
  }
  
  public void setThumbResolution(String paramString) {
    this.thumbResolution = paramString;
  }
  
  public void setThumbSize(String paramString) {
    this.thumbSize = paramString;
  }
  
  public void setThumbUrl(String paramString) {
    this.thumbUrl = paramString;
  }
  
  public void setTitle(String paramString) {
    this.title = paramString;
  }
  
  public void setUpnpClass(String paramString) {
    this.upnpClass = paramString;
  }
  
  public void setmStartingIndex(int paramInt) {
    this.mStartingIndex = paramInt;
  }
  
  public String toString() {
    return this.title;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\ml\Item.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */