package com.samsungimaging.connectionmanager.app.pullservice.demo.ml;

import org.cybergarage.upnp.Action;
import org.cybergarage.xml.Node;

public class Container extends Item {
  public static int mContainerIndex = 0;
  
  private int mCurrentContainerIndex;
  
  public Container(Action paramAction) {
    super(null, paramAction);
    mContainerIndex = 0;
    this.mCurrentContainerIndex = 0;
    setIsContainer(Boolean.valueOf(true));
  }
  
  public Container(Node paramNode, Action paramAction) {
    super(paramNode, paramAction);
    mContainerIndex++;
    this.mCurrentContainerIndex = 0;
    setIsContainer(Boolean.valueOf(true));
  }
  
  public int getmContainerIndex() {
    return mContainerIndex;
  }
  
  public int getmCurrentContainerIndex() {
    return this.mCurrentContainerIndex;
  }
  
  public void setmContainerIndex(int paramInt) {
    mContainerIndex = paramInt;
  }
  
  public void setmCurrentContainerIndex(int paramInt) {
    this.mCurrentContainerIndex = paramInt;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\ml\Container.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */