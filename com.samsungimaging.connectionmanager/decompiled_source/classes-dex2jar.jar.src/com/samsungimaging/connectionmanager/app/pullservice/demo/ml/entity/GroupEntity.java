package com.samsungimaging.connectionmanager.app.pullservice.demo.ml.entity;

import com.samsungimaging.connectionmanager.app.pullservice.demo.ml.Item;
import java.util.ArrayList;
import java.util.List;

public class GroupEntity {
  public List<GroupItemEntity> GroupItemCollection = new ArrayList<GroupItemEntity>();
  
  public String Name;
  
  public boolean groupCheckedAllStatus = false;
  
  public int groupExpandedStatus = 1;
  
  public class GroupItemEntity {
    public ArrayList<Item> ItemList;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\ml\entity\GroupEntity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */