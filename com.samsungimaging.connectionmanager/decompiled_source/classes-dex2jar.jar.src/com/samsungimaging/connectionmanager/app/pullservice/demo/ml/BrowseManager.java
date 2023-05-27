package com.samsungimaging.connectionmanager.app.pullservice.demo.ml;

import com.samsungimaging.connectionmanager.util.Trace;
import java.util.Collections;
import java.util.Comparator;
import org.cybergarage.upnp.Action;

public class BrowseManager extends Item {
  private static BrowseManager mInstance = null;
  
  private Trace.Tag TAG = Trace.Tag.ML;
  
  private Container mCurrentContainer = null;
  
  public boolean mFirstFlag = false;
  
  private Container mRootContainer = null;
  
  public BrowseManager() {}
  
  public BrowseManager(Action paramAction) {
    super(null, paramAction);
    Trace.d(this.TAG, "BrowseManager()");
    this.mRootContainer = new Container(paramAction);
    this.mCurrentContainer = this.mRootContainer;
  }
  
  public static BrowseManager getInstance(Action paramAction) {
    // Byte code:
    //   0: ldc com/samsungimaging/connectionmanager/app/pullservice/demo/ml/BrowseManager
    //   2: monitorenter
    //   3: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/ml/BrowseManager.mInstance : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/BrowseManager;
    //   6: ifnonnull -> 20
    //   9: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/BrowseManager
    //   12: dup
    //   13: aload_0
    //   14: invokespecial <init> : (Lorg/cybergarage/upnp/Action;)V
    //   17: putstatic com/samsungimaging/connectionmanager/app/pullservice/demo/ml/BrowseManager.mInstance : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/BrowseManager;
    //   20: getstatic com/samsungimaging/connectionmanager/app/pullservice/demo/ml/BrowseManager.mInstance : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/BrowseManager;
    //   23: astore_0
    //   24: ldc com/samsungimaging/connectionmanager/app/pullservice/demo/ml/BrowseManager
    //   26: monitorexit
    //   27: aload_0
    //   28: areturn
    //   29: astore_0
    //   30: ldc com/samsungimaging/connectionmanager/app/pullservice/demo/ml/BrowseManager
    //   32: monitorexit
    //   33: aload_0
    //   34: athrow
    // Exception table:
    //   from	to	target	type
    //   3	20	29	finally
    //   20	24	29	finally
  }
  
  public int doBrowse() {
    Trace.d(this.TAG, "Performance Check Point : start doBrowse()");
    this.mFirstFlag = false;
    this.mRootContainer.setId("0");
    this.mRootContainer.getItemList().clear();
    this.mCurrentContainer.getItemList().clear();
    if (!this.mFirstFlag) {
      Trace.d(this.TAG, "mFirstFlag");
      int m = this.mRootContainer.browse(100, 100);
      Trace.d(this.TAG, "cnt = " + m);
      this.mFirstFlag = true;
      if (this.mRootContainer.getItemList().size() > 0)
        this.mRootContainer.setmCurrentContainerIndex(1); 
    } 
    int j = this.mRootContainer.getmCurrentContainerIndex();
    Trace.d(this.TAG, "Index = " + j);
    int i = 0;
    int k = 0;
    if (Container.mContainerIndex > 0) {
      int m = this.mRootContainer.getItemList().size();
      Trace.d(this.TAG, "size = " + m);
      this.mCurrentContainer = (Container)this.mRootContainer.getItemList().getItem(j - 1);
      i = k;
      while (true) {
        if (i < 1000) {
          k = 1000 - i;
          Trace.d(this.TAG, "currentCnt = " + i + ", remainCnt = " + k);
          int n = this.mCurrentContainer.browse(100, k);
          Trace.d(this.TAG, "Result = " + n);
          k = i + n;
          i = k;
          if (n != 100) {
            i = k;
            if (k < 1000) {
              Trace.d(this.TAG, "less than NUM_OF_ITEMS : currentCnt = " + k);
              i = k;
              if (m != this.mRootContainer.getmCurrentContainerIndex()) {
                this.mRootContainer.setmCurrentContainerIndex(++j);
                Item item = this.mRootContainer.getItemList().getItem(j - 1);
                i = k;
                if (item != null) {
                  this.mCurrentContainer = (Container)item;
                  i = k;
                  continue;
                } 
              } 
              Collections.sort(FileSharing.mContainerList, new NameAscCompare());
              Trace.d(this.TAG, "doBrowse = " + i);
              Trace.d(this.TAG, "Performance Check Point : end doBrowse()");
              return i;
            } 
          } 
          continue;
        } 
        Collections.sort(FileSharing.mContainerList, new NameAscCompare());
        Trace.d(this.TAG, "doBrowse = " + i);
        Trace.d(this.TAG, "Performance Check Point : end doBrowse()");
        return i;
      } 
    } 
    Collections.sort(FileSharing.mContainerList, new NameAscCompare());
    Trace.d(this.TAG, "doBrowse = " + i);
    Trace.d(this.TAG, "Performance Check Point : end doBrowse()");
    return i;
  }
  
  static class NameAscCompare implements Comparator<Item> {
    public int compare(Item param1Item1, Item param1Item2) {
      return param1Item2.getDate().compareTo(param1Item1.getDate());
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\ml\BrowseManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */