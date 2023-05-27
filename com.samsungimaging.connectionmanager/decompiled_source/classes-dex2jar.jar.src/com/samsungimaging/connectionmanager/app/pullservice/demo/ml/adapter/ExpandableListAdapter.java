package com.samsungimaging.connectionmanager.app.pullservice.demo.ml.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.samsungimaging.connectionmanager.app.cm.common.CMInfo;
import com.samsungimaging.connectionmanager.app.pullservice.demo.ml.ImageLoader;
import com.samsungimaging.connectionmanager.app.pullservice.demo.ml.Item;
import com.samsungimaging.connectionmanager.app.pullservice.demo.ml.Utils;
import com.samsungimaging.connectionmanager.app.pullservice.demo.ml.entity.GroupEntity;
import com.samsungimaging.connectionmanager.util.ExToast;
import com.samsungimaging.connectionmanager.util.Trace;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
  private Dialog EnlargeImagedialog;
  
  private Trace.Tag TAG = Trace.Tag.ML;
  
  public Activity activity;
  
  ImageView img;
  
  private String item_sreenRes = null;
  
  public HashMap<Item, CheckBox> mChildMap = new HashMap<Item, CheckBox>();
  
  private int mColumnNum;
  
  private Context mContext;
  
  private ExToast mExToast;
  
  private ExpandableListView mExpandableListView;
  
  private List<GroupEntity> mGroupCollection;
  
  public HashMap<Integer, GroupHolder> mGroupMap = new HashMap<Integer, GroupHolder>();
  
  private Handler mHandler;
  
  public ImageLoader mImageLoader;
  
  public ExpandableListAdapter(Context paramContext, Activity paramActivity, ExpandableListView paramExpandableListView, List<GroupEntity> paramList, int paramInt, boolean paramBoolean, Handler paramHandler, ExToast paramExToast) {
    Trace.d(this.TAG, "start ExpandableListAdapter() columnNum : " + paramInt);
    this.activity = paramActivity;
    this.mHandler = paramHandler;
    this.mExToast = paramExToast;
    this.mGroupMap = new HashMap<Integer, GroupHolder>();
    this.mChildMap = new HashMap<Item, CheckBox>();
    if (paramBoolean)
      this.mImageLoader = new ImageLoader(paramContext, paramActivity, 2130837530, Utils.getThumbStorage()); 
    this.mContext = paramContext;
    this.mGroupCollection = paramList;
    this.mColumnNum = paramInt;
    this.mExpandableListView = paramExpandableListView;
    setListEvent();
  }
  
  private void setListEvent() {
    this.mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
          public void onGroupExpand(int param1Int) {
            Trace.d(ExpandableListAdapter.this.TAG, "start onGroupExpand()");
            (ExpandableListAdapter.this.mGroupCollection.get(param1Int)).groupExpandedStatus = 1;
          }
        });
    this.mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
          public void onGroupCollapse(int param1Int) {
            Trace.d(ExpandableListAdapter.this.TAG, "start onGroupCollapse()");
            (ExpandableListAdapter.this.mGroupCollection.get(param1Int)).groupExpandedStatus = 0;
          }
        });
  }
  
  public int getCheckedItemCount() {
    int j = 0;
    int i = 0;
    label21: while (true) {
      if (i >= this.mGroupCollection.size()) {
        Trace.d(this.TAG, "end getCheckedItemCount() checkedItemCount : " + j);
        return j;
      } 
      GroupEntity groupEntity = this.mGroupCollection.get(i);
      int k = 0;
      label19: while (true) {
        if (k >= groupEntity.GroupItemCollection.size()) {
          i++;
          continue label21;
        } 
        GroupEntity.GroupItemEntity groupItemEntity = groupEntity.GroupItemCollection.get(k);
        int m = 0;
        while (true) {
          if (m >= groupItemEntity.ItemList.size()) {
            k++;
            continue label19;
          } 
          int n = j;
          if (((Item)groupItemEntity.ItemList.get(m)).getItemState() == 2)
            n = j + 1; 
          m++;
          j = n;
        } 
        break;
      } 
      break;
    } 
  }
  
  public long getCheckedItemSize() {
    long l = 0L;
    int i = 0;
    label21: while (true) {
      if (i >= this.mGroupCollection.size()) {
        Trace.d(this.TAG, "end getCheckedItemSize() checkedItemSize : " + l);
        return l;
      } 
      GroupEntity groupEntity = this.mGroupCollection.get(i);
      int j = 0;
      label19: while (true) {
        if (j >= groupEntity.GroupItemCollection.size()) {
          i++;
          continue label21;
        } 
        GroupEntity.GroupItemEntity groupItemEntity = groupEntity.GroupItemCollection.get(j);
        int k = 0;
        while (true) {
          if (k >= groupItemEntity.ItemList.size()) {
            j++;
            continue label19;
          } 
          Item item = groupItemEntity.ItemList.get(k);
          long l1 = l;
          if (item.getItemState() == 2)
            l1 = l + Long.parseLong(item.getSize()); 
          k++;
          l = l1;
        } 
        break;
      } 
      break;
    } 
  }
  
  public ArrayList<Item> getChild(int paramInt1, int paramInt2) {
    return ((GroupEntity.GroupItemEntity)((GroupEntity)this.mGroupCollection.get(paramInt1)).GroupItemCollection.get(paramInt2)).ItemList;
  }
  
  public long getChildId(int paramInt1, int paramInt2) {
    return 0L;
  }
  
  public View getChildView(int paramInt1, int paramInt2, boolean paramBoolean, View paramView, ViewGroup paramViewGroup) {
    // Byte code:
    //   0: aload_0
    //   1: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   4: new java/lang/StringBuilder
    //   7: dup
    //   8: ldc 'start getChildView() groupPosition : '
    //   10: invokespecial <init> : (Ljava/lang/String;)V
    //   13: iload_1
    //   14: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   17: ldc ' childPosition : '
    //   19: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   22: iload_2
    //   23: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   26: invokevirtual toString : ()Ljava/lang/String;
    //   29: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   32: aload #4
    //   34: ifnonnull -> 1333
    //   37: aload_0
    //   38: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   41: ldc 'convertView is null'
    //   43: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   46: aload_0
    //   47: getfield mContext : Landroid/content/Context;
    //   50: invokestatic from : (Landroid/content/Context;)Landroid/view/LayoutInflater;
    //   53: ldc 2130903080
    //   55: aconst_null
    //   56: invokevirtual inflate : (ILandroid/view/ViewGroup;)Landroid/view/View;
    //   59: astore #4
    //   61: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$ChildHolder
    //   64: dup
    //   65: aload_0
    //   66: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;)V
    //   69: astore #7
    //   71: iconst_0
    //   72: istore #6
    //   74: iload #6
    //   76: aload_0
    //   77: getfield mColumnNum : I
    //   80: if_icmplt -> 143
    //   83: aload #4
    //   85: aload #7
    //   87: invokevirtual setTag : (Ljava/lang/Object;)V
    //   90: aload_0
    //   91: getfield mGroupCollection : Ljava/util/List;
    //   94: iload_1
    //   95: invokeinterface get : (I)Ljava/lang/Object;
    //   100: checkcast com/samsungimaging/connectionmanager/app/pullservice/demo/ml/entity/GroupEntity
    //   103: getfield GroupItemCollection : Ljava/util/List;
    //   106: iload_2
    //   107: invokeinterface get : (I)Ljava/lang/Object;
    //   112: checkcast com/samsungimaging/connectionmanager/app/pullservice/demo/ml/entity/GroupEntity$GroupItemEntity
    //   115: getfield ItemList : Ljava/util/ArrayList;
    //   118: astore #8
    //   120: iconst_0
    //   121: istore_1
    //   122: iload_1
    //   123: aload_0
    //   124: getfield mColumnNum : I
    //   127: if_icmplt -> 1356
    //   130: aload_0
    //   131: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   134: ldc_w 'return convertView'
    //   137: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   140: aload #4
    //   142: areturn
    //   143: iload #6
    //   145: tableswitch default -> 184, 0 -> 193, 1 -> 383, 2 -> 573, 3 -> 763, 4 -> 953, 5 -> 1143
    //   184: iload #6
    //   186: iconst_1
    //   187: iadd
    //   188: istore #6
    //   190: goto -> 74
    //   193: aload #7
    //   195: getfield frame : [Landroid/widget/FrameLayout;
    //   198: iload #6
    //   200: aload #4
    //   202: ldc_w 2131558614
    //   205: invokevirtual findViewById : (I)Landroid/view/View;
    //   208: checkcast android/widget/FrameLayout
    //   211: aastore
    //   212: aload #7
    //   214: getfield thumbImg : [Landroid/widget/ImageView;
    //   217: iload #6
    //   219: aload #4
    //   221: ldc_w 2131558616
    //   224: invokevirtual findViewById : (I)Landroid/view/View;
    //   227: checkcast android/widget/ImageView
    //   230: aastore
    //   231: aload #7
    //   233: getfield movieImage : [Landroid/widget/ImageView;
    //   236: iload #6
    //   238: aload #4
    //   240: ldc_w 2131558618
    //   243: invokevirtual findViewById : (I)Landroid/view/View;
    //   246: checkcast android/widget/ImageView
    //   249: aastore
    //   250: aload #7
    //   252: getfield checkBox : [Landroid/widget/CheckBox;
    //   255: iload #6
    //   257: aload #4
    //   259: ldc_w 2131558617
    //   262: invokevirtual findViewById : (I)Landroid/view/View;
    //   265: checkcast android/widget/CheckBox
    //   268: aastore
    //   269: aload #4
    //   271: ldc_w 2131558617
    //   274: invokevirtual findViewById : (I)Landroid/view/View;
    //   277: astore #8
    //   279: aload #7
    //   281: getfield thumbImg : [Landroid/widget/ImageView;
    //   284: iload #6
    //   286: aaload
    //   287: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$thumbClickListener
    //   290: dup
    //   291: aload_0
    //   292: aload #8
    //   294: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Landroid/view/View;)V
    //   297: invokevirtual setOnClickListener : (Landroid/view/View$OnClickListener;)V
    //   300: aload #7
    //   302: getfield checkBox : [Landroid/widget/CheckBox;
    //   305: iload #6
    //   307: aaload
    //   308: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxClickListener
    //   311: dup
    //   312: aload_0
    //   313: aconst_null
    //   314: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxClickListener;)V
    //   317: invokevirtual setOnClickListener : (Landroid/view/View$OnClickListener;)V
    //   320: aload #7
    //   322: getfield checkBox : [Landroid/widget/CheckBox;
    //   325: iload #6
    //   327: aaload
    //   328: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxLongClickListener
    //   331: dup
    //   332: aload_0
    //   333: aconst_null
    //   334: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxLongClickListener;)V
    //   337: invokevirtual setOnLongClickListener : (Landroid/view/View$OnLongClickListener;)V
    //   340: aload #7
    //   342: getfield notSupportMask : [Landroid/widget/ImageView;
    //   345: iload #6
    //   347: aload #4
    //   349: ldc_w 2131558619
    //   352: invokevirtual findViewById : (I)Landroid/view/View;
    //   355: checkcast android/widget/ImageView
    //   358: aastore
    //   359: aload #7
    //   361: getfield thumbImg : [Landroid/widget/ImageView;
    //   364: iload #6
    //   366: aaload
    //   367: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$imageLongclicklistener
    //   370: dup
    //   371: aload_0
    //   372: aload #8
    //   374: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Landroid/view/View;)V
    //   377: invokevirtual setOnLongClickListener : (Landroid/view/View$OnLongClickListener;)V
    //   380: goto -> 184
    //   383: aload #7
    //   385: getfield frame : [Landroid/widget/FrameLayout;
    //   388: iload #6
    //   390: aload #4
    //   392: ldc_w 2131558620
    //   395: invokevirtual findViewById : (I)Landroid/view/View;
    //   398: checkcast android/widget/FrameLayout
    //   401: aastore
    //   402: aload #7
    //   404: getfield thumbImg : [Landroid/widget/ImageView;
    //   407: iload #6
    //   409: aload #4
    //   411: ldc_w 2131558622
    //   414: invokevirtual findViewById : (I)Landroid/view/View;
    //   417: checkcast android/widget/ImageView
    //   420: aastore
    //   421: aload #7
    //   423: getfield movieImage : [Landroid/widget/ImageView;
    //   426: iload #6
    //   428: aload #4
    //   430: ldc_w 2131558624
    //   433: invokevirtual findViewById : (I)Landroid/view/View;
    //   436: checkcast android/widget/ImageView
    //   439: aastore
    //   440: aload #7
    //   442: getfield checkBox : [Landroid/widget/CheckBox;
    //   445: iload #6
    //   447: aload #4
    //   449: ldc_w 2131558623
    //   452: invokevirtual findViewById : (I)Landroid/view/View;
    //   455: checkcast android/widget/CheckBox
    //   458: aastore
    //   459: aload #7
    //   461: getfield checkBox : [Landroid/widget/CheckBox;
    //   464: iload #6
    //   466: aaload
    //   467: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxClickListener
    //   470: dup
    //   471: aload_0
    //   472: aconst_null
    //   473: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxClickListener;)V
    //   476: invokevirtual setOnClickListener : (Landroid/view/View$OnClickListener;)V
    //   479: aload #7
    //   481: getfield checkBox : [Landroid/widget/CheckBox;
    //   484: iload #6
    //   486: aaload
    //   487: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxLongClickListener
    //   490: dup
    //   491: aload_0
    //   492: aconst_null
    //   493: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxLongClickListener;)V
    //   496: invokevirtual setOnLongClickListener : (Landroid/view/View$OnLongClickListener;)V
    //   499: aload #4
    //   501: ldc_w 2131558623
    //   504: invokevirtual findViewById : (I)Landroid/view/View;
    //   507: astore #8
    //   509: aload #7
    //   511: getfield thumbImg : [Landroid/widget/ImageView;
    //   514: iload #6
    //   516: aaload
    //   517: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$thumbClickListener
    //   520: dup
    //   521: aload_0
    //   522: aload #8
    //   524: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Landroid/view/View;)V
    //   527: invokevirtual setOnClickListener : (Landroid/view/View$OnClickListener;)V
    //   530: aload #7
    //   532: getfield notSupportMask : [Landroid/widget/ImageView;
    //   535: iload #6
    //   537: aload #4
    //   539: ldc_w 2131558625
    //   542: invokevirtual findViewById : (I)Landroid/view/View;
    //   545: checkcast android/widget/ImageView
    //   548: aastore
    //   549: aload #7
    //   551: getfield thumbImg : [Landroid/widget/ImageView;
    //   554: iload #6
    //   556: aaload
    //   557: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$imageLongclicklistener
    //   560: dup
    //   561: aload_0
    //   562: aload #8
    //   564: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Landroid/view/View;)V
    //   567: invokevirtual setOnLongClickListener : (Landroid/view/View$OnLongClickListener;)V
    //   570: goto -> 184
    //   573: aload #7
    //   575: getfield frame : [Landroid/widget/FrameLayout;
    //   578: iload #6
    //   580: aload #4
    //   582: ldc_w 2131558626
    //   585: invokevirtual findViewById : (I)Landroid/view/View;
    //   588: checkcast android/widget/FrameLayout
    //   591: aastore
    //   592: aload #7
    //   594: getfield thumbImg : [Landroid/widget/ImageView;
    //   597: iload #6
    //   599: aload #4
    //   601: ldc_w 2131558628
    //   604: invokevirtual findViewById : (I)Landroid/view/View;
    //   607: checkcast android/widget/ImageView
    //   610: aastore
    //   611: aload #7
    //   613: getfield movieImage : [Landroid/widget/ImageView;
    //   616: iload #6
    //   618: aload #4
    //   620: ldc_w 2131558630
    //   623: invokevirtual findViewById : (I)Landroid/view/View;
    //   626: checkcast android/widget/ImageView
    //   629: aastore
    //   630: aload #7
    //   632: getfield checkBox : [Landroid/widget/CheckBox;
    //   635: iload #6
    //   637: aload #4
    //   639: ldc_w 2131558629
    //   642: invokevirtual findViewById : (I)Landroid/view/View;
    //   645: checkcast android/widget/CheckBox
    //   648: aastore
    //   649: aload #7
    //   651: getfield checkBox : [Landroid/widget/CheckBox;
    //   654: iload #6
    //   656: aaload
    //   657: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxClickListener
    //   660: dup
    //   661: aload_0
    //   662: aconst_null
    //   663: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxClickListener;)V
    //   666: invokevirtual setOnClickListener : (Landroid/view/View$OnClickListener;)V
    //   669: aload #7
    //   671: getfield checkBox : [Landroid/widget/CheckBox;
    //   674: iload #6
    //   676: aaload
    //   677: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxLongClickListener
    //   680: dup
    //   681: aload_0
    //   682: aconst_null
    //   683: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxLongClickListener;)V
    //   686: invokevirtual setOnLongClickListener : (Landroid/view/View$OnLongClickListener;)V
    //   689: aload #4
    //   691: ldc_w 2131558629
    //   694: invokevirtual findViewById : (I)Landroid/view/View;
    //   697: astore #8
    //   699: aload #7
    //   701: getfield thumbImg : [Landroid/widget/ImageView;
    //   704: iload #6
    //   706: aaload
    //   707: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$thumbClickListener
    //   710: dup
    //   711: aload_0
    //   712: aload #8
    //   714: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Landroid/view/View;)V
    //   717: invokevirtual setOnClickListener : (Landroid/view/View$OnClickListener;)V
    //   720: aload #7
    //   722: getfield notSupportMask : [Landroid/widget/ImageView;
    //   725: iload #6
    //   727: aload #4
    //   729: ldc_w 2131558631
    //   732: invokevirtual findViewById : (I)Landroid/view/View;
    //   735: checkcast android/widget/ImageView
    //   738: aastore
    //   739: aload #7
    //   741: getfield thumbImg : [Landroid/widget/ImageView;
    //   744: iload #6
    //   746: aaload
    //   747: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$imageLongclicklistener
    //   750: dup
    //   751: aload_0
    //   752: aload #8
    //   754: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Landroid/view/View;)V
    //   757: invokevirtual setOnLongClickListener : (Landroid/view/View$OnLongClickListener;)V
    //   760: goto -> 184
    //   763: aload #7
    //   765: getfield frame : [Landroid/widget/FrameLayout;
    //   768: iload #6
    //   770: aload #4
    //   772: ldc_w 2131558632
    //   775: invokevirtual findViewById : (I)Landroid/view/View;
    //   778: checkcast android/widget/FrameLayout
    //   781: aastore
    //   782: aload #7
    //   784: getfield thumbImg : [Landroid/widget/ImageView;
    //   787: iload #6
    //   789: aload #4
    //   791: ldc_w 2131558634
    //   794: invokevirtual findViewById : (I)Landroid/view/View;
    //   797: checkcast android/widget/ImageView
    //   800: aastore
    //   801: aload #7
    //   803: getfield movieImage : [Landroid/widget/ImageView;
    //   806: iload #6
    //   808: aload #4
    //   810: ldc_w 2131558636
    //   813: invokevirtual findViewById : (I)Landroid/view/View;
    //   816: checkcast android/widget/ImageView
    //   819: aastore
    //   820: aload #7
    //   822: getfield checkBox : [Landroid/widget/CheckBox;
    //   825: iload #6
    //   827: aload #4
    //   829: ldc_w 2131558635
    //   832: invokevirtual findViewById : (I)Landroid/view/View;
    //   835: checkcast android/widget/CheckBox
    //   838: aastore
    //   839: aload #7
    //   841: getfield checkBox : [Landroid/widget/CheckBox;
    //   844: iload #6
    //   846: aaload
    //   847: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxClickListener
    //   850: dup
    //   851: aload_0
    //   852: aconst_null
    //   853: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxClickListener;)V
    //   856: invokevirtual setOnClickListener : (Landroid/view/View$OnClickListener;)V
    //   859: aload #7
    //   861: getfield checkBox : [Landroid/widget/CheckBox;
    //   864: iload #6
    //   866: aaload
    //   867: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxLongClickListener
    //   870: dup
    //   871: aload_0
    //   872: aconst_null
    //   873: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxLongClickListener;)V
    //   876: invokevirtual setOnLongClickListener : (Landroid/view/View$OnLongClickListener;)V
    //   879: aload #4
    //   881: ldc_w 2131558635
    //   884: invokevirtual findViewById : (I)Landroid/view/View;
    //   887: astore #8
    //   889: aload #7
    //   891: getfield thumbImg : [Landroid/widget/ImageView;
    //   894: iload #6
    //   896: aaload
    //   897: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$thumbClickListener
    //   900: dup
    //   901: aload_0
    //   902: aload #8
    //   904: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Landroid/view/View;)V
    //   907: invokevirtual setOnClickListener : (Landroid/view/View$OnClickListener;)V
    //   910: aload #7
    //   912: getfield notSupportMask : [Landroid/widget/ImageView;
    //   915: iload #6
    //   917: aload #4
    //   919: ldc_w 2131558637
    //   922: invokevirtual findViewById : (I)Landroid/view/View;
    //   925: checkcast android/widget/ImageView
    //   928: aastore
    //   929: aload #7
    //   931: getfield thumbImg : [Landroid/widget/ImageView;
    //   934: iload #6
    //   936: aaload
    //   937: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$imageLongclicklistener
    //   940: dup
    //   941: aload_0
    //   942: aload #8
    //   944: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Landroid/view/View;)V
    //   947: invokevirtual setOnLongClickListener : (Landroid/view/View$OnLongClickListener;)V
    //   950: goto -> 184
    //   953: aload #7
    //   955: getfield frame : [Landroid/widget/FrameLayout;
    //   958: iload #6
    //   960: aload #4
    //   962: ldc_w 2131558638
    //   965: invokevirtual findViewById : (I)Landroid/view/View;
    //   968: checkcast android/widget/FrameLayout
    //   971: aastore
    //   972: aload #7
    //   974: getfield thumbImg : [Landroid/widget/ImageView;
    //   977: iload #6
    //   979: aload #4
    //   981: ldc_w 2131558640
    //   984: invokevirtual findViewById : (I)Landroid/view/View;
    //   987: checkcast android/widget/ImageView
    //   990: aastore
    //   991: aload #7
    //   993: getfield movieImage : [Landroid/widget/ImageView;
    //   996: iload #6
    //   998: aload #4
    //   1000: ldc_w 2131558642
    //   1003: invokevirtual findViewById : (I)Landroid/view/View;
    //   1006: checkcast android/widget/ImageView
    //   1009: aastore
    //   1010: aload #7
    //   1012: getfield checkBox : [Landroid/widget/CheckBox;
    //   1015: iload #6
    //   1017: aload #4
    //   1019: ldc_w 2131558641
    //   1022: invokevirtual findViewById : (I)Landroid/view/View;
    //   1025: checkcast android/widget/CheckBox
    //   1028: aastore
    //   1029: aload #7
    //   1031: getfield checkBox : [Landroid/widget/CheckBox;
    //   1034: iload #6
    //   1036: aaload
    //   1037: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxClickListener
    //   1040: dup
    //   1041: aload_0
    //   1042: aconst_null
    //   1043: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxClickListener;)V
    //   1046: invokevirtual setOnClickListener : (Landroid/view/View$OnClickListener;)V
    //   1049: aload #7
    //   1051: getfield checkBox : [Landroid/widget/CheckBox;
    //   1054: iload #6
    //   1056: aaload
    //   1057: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxLongClickListener
    //   1060: dup
    //   1061: aload_0
    //   1062: aconst_null
    //   1063: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxLongClickListener;)V
    //   1066: invokevirtual setOnLongClickListener : (Landroid/view/View$OnLongClickListener;)V
    //   1069: aload #4
    //   1071: ldc_w 2131558641
    //   1074: invokevirtual findViewById : (I)Landroid/view/View;
    //   1077: astore #8
    //   1079: aload #7
    //   1081: getfield thumbImg : [Landroid/widget/ImageView;
    //   1084: iload #6
    //   1086: aaload
    //   1087: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$thumbClickListener
    //   1090: dup
    //   1091: aload_0
    //   1092: aload #8
    //   1094: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Landroid/view/View;)V
    //   1097: invokevirtual setOnClickListener : (Landroid/view/View$OnClickListener;)V
    //   1100: aload #7
    //   1102: getfield notSupportMask : [Landroid/widget/ImageView;
    //   1105: iload #6
    //   1107: aload #4
    //   1109: ldc_w 2131558643
    //   1112: invokevirtual findViewById : (I)Landroid/view/View;
    //   1115: checkcast android/widget/ImageView
    //   1118: aastore
    //   1119: aload #7
    //   1121: getfield thumbImg : [Landroid/widget/ImageView;
    //   1124: iload #6
    //   1126: aaload
    //   1127: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$imageLongclicklistener
    //   1130: dup
    //   1131: aload_0
    //   1132: aload #8
    //   1134: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Landroid/view/View;)V
    //   1137: invokevirtual setOnLongClickListener : (Landroid/view/View$OnLongClickListener;)V
    //   1140: goto -> 184
    //   1143: aload #7
    //   1145: getfield frame : [Landroid/widget/FrameLayout;
    //   1148: iload #6
    //   1150: aload #4
    //   1152: ldc_w 2131558644
    //   1155: invokevirtual findViewById : (I)Landroid/view/View;
    //   1158: checkcast android/widget/FrameLayout
    //   1161: aastore
    //   1162: aload #7
    //   1164: getfield thumbImg : [Landroid/widget/ImageView;
    //   1167: iload #6
    //   1169: aload #4
    //   1171: ldc_w 2131558646
    //   1174: invokevirtual findViewById : (I)Landroid/view/View;
    //   1177: checkcast android/widget/ImageView
    //   1180: aastore
    //   1181: aload #7
    //   1183: getfield movieImage : [Landroid/widget/ImageView;
    //   1186: iload #6
    //   1188: aload #4
    //   1190: ldc_w 2131558648
    //   1193: invokevirtual findViewById : (I)Landroid/view/View;
    //   1196: checkcast android/widget/ImageView
    //   1199: aastore
    //   1200: aload #7
    //   1202: getfield checkBox : [Landroid/widget/CheckBox;
    //   1205: iload #6
    //   1207: aload #4
    //   1209: ldc_w 2131558647
    //   1212: invokevirtual findViewById : (I)Landroid/view/View;
    //   1215: checkcast android/widget/CheckBox
    //   1218: aastore
    //   1219: aload #4
    //   1221: ldc_w 2131558647
    //   1224: invokevirtual findViewById : (I)Landroid/view/View;
    //   1227: astore #8
    //   1229: aload #7
    //   1231: getfield thumbImg : [Landroid/widget/ImageView;
    //   1234: iload #6
    //   1236: aaload
    //   1237: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$thumbClickListener
    //   1240: dup
    //   1241: aload_0
    //   1242: aload #8
    //   1244: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Landroid/view/View;)V
    //   1247: invokevirtual setOnClickListener : (Landroid/view/View$OnClickListener;)V
    //   1250: aload #7
    //   1252: getfield checkBox : [Landroid/widget/CheckBox;
    //   1255: iload #6
    //   1257: aaload
    //   1258: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxClickListener
    //   1261: dup
    //   1262: aload_0
    //   1263: aconst_null
    //   1264: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxClickListener;)V
    //   1267: invokevirtual setOnClickListener : (Landroid/view/View$OnClickListener;)V
    //   1270: aload #7
    //   1272: getfield checkBox : [Landroid/widget/CheckBox;
    //   1275: iload #6
    //   1277: aaload
    //   1278: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxLongClickListener
    //   1281: dup
    //   1282: aload_0
    //   1283: aconst_null
    //   1284: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$CheckBoxLongClickListener;)V
    //   1287: invokevirtual setOnLongClickListener : (Landroid/view/View$OnLongClickListener;)V
    //   1290: aload #7
    //   1292: getfield notSupportMask : [Landroid/widget/ImageView;
    //   1295: iload #6
    //   1297: aload #4
    //   1299: ldc_w 2131558649
    //   1302: invokevirtual findViewById : (I)Landroid/view/View;
    //   1305: checkcast android/widget/ImageView
    //   1308: aastore
    //   1309: aload #7
    //   1311: getfield thumbImg : [Landroid/widget/ImageView;
    //   1314: iload #6
    //   1316: aaload
    //   1317: new com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$imageLongclicklistener
    //   1320: dup
    //   1321: aload_0
    //   1322: aload #8
    //   1324: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter;Landroid/view/View;)V
    //   1327: invokevirtual setOnLongClickListener : (Landroid/view/View$OnLongClickListener;)V
    //   1330: goto -> 184
    //   1333: aload_0
    //   1334: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1337: ldc_w 'convertView is not null'
    //   1340: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1343: aload #4
    //   1345: invokevirtual getTag : ()Ljava/lang/Object;
    //   1348: checkcast com/samsungimaging/connectionmanager/app/pullservice/demo/ml/adapter/ExpandableListAdapter$ChildHolder
    //   1351: astore #7
    //   1353: goto -> 90
    //   1356: iload_1
    //   1357: aload #8
    //   1359: invokevirtual size : ()I
    //   1362: if_icmplt -> 1383
    //   1365: aload #7
    //   1367: getfield frame : [Landroid/widget/FrameLayout;
    //   1370: iload_1
    //   1371: aaload
    //   1372: iconst_4
    //   1373: invokevirtual setVisibility : (I)V
    //   1376: iload_1
    //   1377: iconst_1
    //   1378: iadd
    //   1379: istore_1
    //   1380: goto -> 122
    //   1383: aload #7
    //   1385: getfield frame : [Landroid/widget/FrameLayout;
    //   1388: iload_1
    //   1389: aaload
    //   1390: iconst_0
    //   1391: invokevirtual setVisibility : (I)V
    //   1394: aload #8
    //   1396: iload_1
    //   1397: invokevirtual get : (I)Ljava/lang/Object;
    //   1400: checkcast com/samsungimaging/connectionmanager/app/pullservice/demo/ml/Item
    //   1403: astore #9
    //   1405: aload_0
    //   1406: getfield mImageLoader : Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/ImageLoader;
    //   1409: aload #9
    //   1411: invokevirtual getThumbRes : ()Ljava/lang/String;
    //   1414: aload #5
    //   1416: invokevirtual getContext : ()Landroid/content/Context;
    //   1419: checkcast android/app/Activity
    //   1422: aload #7
    //   1424: getfield thumbImg : [Landroid/widget/ImageView;
    //   1427: iload_1
    //   1428: aaload
    //   1429: aload #9
    //   1431: invokevirtual setImage : (Ljava/lang/String;Landroid/app/Activity;Landroid/widget/ImageView;Lcom/samsungimaging/connectionmanager/app/pullservice/demo/ml/Item;)V
    //   1434: aload #9
    //   1436: invokevirtual getRes : ()Ljava/lang/String;
    //   1439: invokestatic getExtention : (Ljava/lang/String;)Ljava/lang/String;
    //   1442: ldc_w 'MP4'
    //   1445: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   1448: ifeq -> 1621
    //   1451: aload #7
    //   1453: getfield movieImage : [Landroid/widget/ImageView;
    //   1456: iload_1
    //   1457: aaload
    //   1458: iconst_0
    //   1459: invokevirtual setVisibility : (I)V
    //   1462: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/common/CMInfo;
    //   1465: invokevirtual getConnectedSSID : ()Ljava/lang/String;
    //   1468: ifnull -> 1506
    //   1471: invokestatic getInstance : ()Lcom/samsungimaging/connectionmanager/app/cm/common/CMInfo;
    //   1474: invokevirtual getConnectedSSID : ()Ljava/lang/String;
    //   1477: ldc_w 'QF30'
    //   1480: invokevirtual contains : (Ljava/lang/CharSequence;)Z
    //   1483: ifeq -> 1506
    //   1486: aload #9
    //   1488: invokevirtual getResolution : ()Ljava/lang/String;
    //   1491: ldc_w '1920x1080'
    //   1494: invokevirtual equals : (Ljava/lang/Object;)Z
    //   1497: ifeq -> 1506
    //   1500: aload #9
    //   1502: iconst_4
    //   1503: invokevirtual setItemState : (I)V
    //   1506: aload_0
    //   1507: getfield TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   1510: new java/lang/StringBuilder
    //   1513: dup
    //   1514: ldc_w 'item state : '
    //   1517: invokespecial <init> : (Ljava/lang/String;)V
    //   1520: aload #9
    //   1522: invokevirtual getItemState : ()I
    //   1525: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   1528: invokevirtual toString : ()Ljava/lang/String;
    //   1531: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   1534: aload #9
    //   1536: invokevirtual getItemState : ()I
    //   1539: tableswitch default -> 1568, 1 -> 1635, 2 -> 1684, 3 -> 1635, 4 -> 1733
    //   1568: aload #9
    //   1570: invokevirtual isSupported : ()Z
    //   1573: ifne -> 1589
    //   1576: aload #7
    //   1578: getfield thumbImg : [Landroid/widget/ImageView;
    //   1581: iload_1
    //   1582: aaload
    //   1583: ldc_w 2130838227
    //   1586: invokevirtual setImageResource : (I)V
    //   1589: aload #7
    //   1591: getfield checkBox : [Landroid/widget/CheckBox;
    //   1594: iload_1
    //   1595: aaload
    //   1596: aload #9
    //   1598: invokevirtual setTag : (Ljava/lang/Object;)V
    //   1601: aload_0
    //   1602: getfield mChildMap : Ljava/util/HashMap;
    //   1605: aload #9
    //   1607: aload #7
    //   1609: getfield checkBox : [Landroid/widget/CheckBox;
    //   1612: iload_1
    //   1613: aaload
    //   1614: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   1617: pop
    //   1618: goto -> 1376
    //   1621: aload #7
    //   1623: getfield movieImage : [Landroid/widget/ImageView;
    //   1626: iload_1
    //   1627: aaload
    //   1628: iconst_4
    //   1629: invokevirtual setVisibility : (I)V
    //   1632: goto -> 1506
    //   1635: aload #7
    //   1637: getfield checkBox : [Landroid/widget/CheckBox;
    //   1640: iload_1
    //   1641: aaload
    //   1642: ldc_w 2130838239
    //   1645: invokevirtual setButtonDrawable : (I)V
    //   1648: aload #7
    //   1650: getfield checkBox : [Landroid/widget/CheckBox;
    //   1653: iload_1
    //   1654: aaload
    //   1655: iconst_0
    //   1656: invokevirtual setChecked : (Z)V
    //   1659: aload #7
    //   1661: getfield checkBox : [Landroid/widget/CheckBox;
    //   1664: iload_1
    //   1665: aaload
    //   1666: iconst_0
    //   1667: invokevirtual setVisibility : (I)V
    //   1670: aload #7
    //   1672: getfield notSupportMask : [Landroid/widget/ImageView;
    //   1675: iload_1
    //   1676: aaload
    //   1677: iconst_4
    //   1678: invokevirtual setVisibility : (I)V
    //   1681: goto -> 1568
    //   1684: aload #7
    //   1686: getfield checkBox : [Landroid/widget/CheckBox;
    //   1689: iload_1
    //   1690: aaload
    //   1691: ldc_w 2130838239
    //   1694: invokevirtual setButtonDrawable : (I)V
    //   1697: aload #7
    //   1699: getfield checkBox : [Landroid/widget/CheckBox;
    //   1702: iload_1
    //   1703: aaload
    //   1704: iconst_1
    //   1705: invokevirtual setChecked : (Z)V
    //   1708: aload #7
    //   1710: getfield checkBox : [Landroid/widget/CheckBox;
    //   1713: iload_1
    //   1714: aaload
    //   1715: iconst_0
    //   1716: invokevirtual setVisibility : (I)V
    //   1719: aload #7
    //   1721: getfield notSupportMask : [Landroid/widget/ImageView;
    //   1724: iload_1
    //   1725: aaload
    //   1726: iconst_4
    //   1727: invokevirtual setVisibility : (I)V
    //   1730: goto -> 1568
    //   1733: aload #7
    //   1735: getfield checkBox : [Landroid/widget/CheckBox;
    //   1738: iload_1
    //   1739: aaload
    //   1740: iconst_4
    //   1741: invokevirtual setVisibility : (I)V
    //   1744: aload #7
    //   1746: getfield notSupportMask : [Landroid/widget/ImageView;
    //   1749: iload_1
    //   1750: aaload
    //   1751: iconst_0
    //   1752: invokevirtual setVisibility : (I)V
    //   1755: goto -> 1568
  }
  
  public int getChildrenCount(int paramInt) {
    return ((GroupEntity)this.mGroupCollection.get(paramInt)).GroupItemCollection.size();
  }
  
  public Object getGroup(int paramInt) {
    return this.mGroupCollection.get(paramInt);
  }
  
  public int getGroupCount() {
    return this.mGroupCollection.size();
  }
  
  public long getGroupId(int paramInt) {
    return paramInt;
  }
  
  public int getGroupItemCount(int paramInt) {
    int i = 0;
    GroupEntity groupEntity = this.mGroupCollection.get(paramInt);
    for (paramInt = 0;; paramInt++) {
      if (paramInt >= groupEntity.GroupItemCollection.size())
        return i; 
      i += ((GroupEntity.GroupItemEntity)groupEntity.GroupItemCollection.get(paramInt)).ItemList.size();
    } 
  }
  
  public int getGroupUncheckedItemCount(int paramInt) {
    int i = 0;
    GroupEntity groupEntity = this.mGroupCollection.get(paramInt);
    paramInt = 0;
    label15: while (true) {
      if (paramInt >= groupEntity.GroupItemCollection.size())
        return i; 
      GroupEntity.GroupItemEntity groupItemEntity = groupEntity.GroupItemCollection.get(paramInt);
      int j = 0;
      while (true) {
        if (j >= groupItemEntity.ItemList.size()) {
          paramInt++;
          continue label15;
        } 
        int k = i;
        if (((Item)groupItemEntity.ItemList.get(j)).getItemState() == 1)
          k = i + 1; 
        j++;
        i = k;
      } 
      break;
    } 
  }
  
  public View getGroupView(int paramInt, boolean paramBoolean, View paramView, ViewGroup paramViewGroup) {
    GroupHolder groupHolder;
    if (paramView == null) {
      paramView = LayoutInflater.from(this.mContext).inflate(2130903082, null);
      groupHolder = new GroupHolder();
      groupHolder.img = (ImageView)paramView.findViewById(2131558656);
      groupHolder.img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              int i = ((Integer)param1View.getTag()).intValue();
              if (((GroupEntity)ExpandableListAdapter.this.getGroup(i)).groupExpandedStatus == 0) {
                ExpandableListAdapter.this.mExpandableListView.expandGroup(i);
                return;
              } 
              ExpandableListAdapter.this.mExpandableListView.collapseGroup(i);
            }
          });
      groupHolder.title = (CheckBox)paramView.findViewById(2131558655);
      groupHolder.title.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              GroupEntity.GroupItemEntity groupItemEntity;
              Trace.d(ExpandableListAdapter.this.TAG, "start onClick() title");
              int i = ((Integer)param1View.getTag()).intValue();
              GroupEntity groupEntity = (GroupEntity)ExpandableListAdapter.this.getGroup(i);
              if (groupEntity.groupCheckedAllStatus) {
                i = 0;
                label51: while (true) {
                  if (i >= groupEntity.GroupItemCollection.size()) {
                    groupEntity.groupCheckedAllStatus = false;
                    ExpandableListAdapter.this.mHandler.sendEmptyMessage(37);
                    return;
                  } 
                  groupItemEntity = groupEntity.GroupItemCollection.get(i);
                  for (int k = 0;; k++) {
                    if (k >= groupItemEntity.ItemList.size()) {
                      i++;
                      continue label51;
                    } 
                    Item item = groupItemEntity.ItemList.get(k);
                    if (item.getItemState() == 2) {
                      item.setItemState(1);
                      if (ExpandableListAdapter.this.mChildMap.get(item) != null && item.equals(((CheckBox)ExpandableListAdapter.this.mChildMap.get(item)).getTag()))
                        ((CheckBox)ExpandableListAdapter.this.mChildMap.get(item)).setChecked(false); 
                    } 
                  } 
                  break;
                } 
              } 
              int j = 1000 - ExpandableListAdapter.this.getCheckedItemCount();
              long l = ExpandableListAdapter.this.getCheckedItemSize();
              if (j > 0 && ExpandableListAdapter.this.getGroupUncheckedItemCount(i) <= j) {
                groupEntity.groupCheckedAllStatus = true;
                i = 0;
                label54: while (true) {
                  if (i < groupEntity.GroupItemCollection.size()) {
                    Trace.d(ExpandableListAdapter.this.TAG, "j : " + i);
                    if (j > 0) {
                      Object object1;
                      Object object2;
                      GroupEntity.GroupItemEntity groupItemEntity1 = groupEntity.GroupItemCollection.get(i);
                      int k = 0;
                      while (true) {
                        if (k < groupItemEntity1.ItemList.size()) {
                          Trace.d(ExpandableListAdapter.this.TAG, "k : " + k);
                          Item item = groupItemEntity1.ItemList.get(k);
                          Object object3 = object1;
                          Object object4 = object2;
                          if (item.getItemState() == 1) {
                            Trace.d(ExpandableListAdapter.this.TAG, "selectAllItem step 1");
                            if (Utils.getAvailableExternalMemorySize() > Long.parseLong(item.getSize()) + object2) {
                              Trace.d(ExpandableListAdapter.this.TAG, "selectAllItem step 2");
                              if (object1 > null) {
                                Trace.d(ExpandableListAdapter.this.TAG, "selectAllItem step 3");
                                int m = object1 - 1;
                                long l2 = object2 + Long.parseLong(item.getSize());
                                item.setItemState(2);
                                int n = m;
                                long l1 = l2;
                                if (ExpandableListAdapter.this.mChildMap.get(item) != null) {
                                  n = m;
                                  l1 = l2;
                                  if (item.equals(((CheckBox)ExpandableListAdapter.this.mChildMap.get(item)).getTag())) {
                                    ((CheckBox)ExpandableListAdapter.this.mChildMap.get(item)).setChecked(true);
                                    l1 = l2;
                                    n = m;
                                  } 
                                } 
                                continue;
                              } 
                            } else {
                              groupEntity.groupCheckedAllStatus = false;
                              ExpandableListAdapter.this.mExToast.show(6);
                            } 
                          } else {
                            continue;
                          } 
                        } 
                        i++;
                        continue label54;
                        k++;
                        object1 = SYNTHETIC_LOCAL_VARIABLE_5;
                        object2 = SYNTHETIC_LOCAL_VARIABLE_6;
                      } 
                      break;
                    } 
                  } 
                  ExpandableListAdapter.this.mHandler.sendEmptyMessage(37);
                  ((CheckBox)groupItemEntity).setChecked(groupEntity.groupCheckedAllStatus);
                  return;
                } 
              } 
              ((CheckBox)groupItemEntity).setChecked(false);
            }
          });
      paramView.setTag(groupHolder);
    } else {
      groupHolder = (GroupHolder)paramView.getTag();
    } 
    if (((GroupEntity)this.mGroupCollection.get(paramInt)).groupExpandedStatus == 0) {
      groupHolder.img.setImageResource(2130838300);
    } else {
      groupHolder.img.setImageResource(2130838295);
    } 
    groupHolder.img.setTag(Integer.valueOf(paramInt));
    groupHolder.title.setChecked(((GroupEntity)this.mGroupCollection.get(paramInt)).groupCheckedAllStatus);
    groupHolder.title.setTag(Integer.valueOf(paramInt));
    int j = 0;
    GroupEntity groupEntity = this.mGroupCollection.get(paramInt);
    int i;
    for (i = 0;; i++) {
      if (i >= groupEntity.GroupItemCollection.size()) {
        groupHolder.title.setText(String.valueOf(((GroupEntity)this.mGroupCollection.get(paramInt)).Name) + "(" + j + ")");
        this.mGroupMap.put(Integer.valueOf(paramInt), groupHolder);
        return paramView;
      } 
      j += ((GroupEntity.GroupItemEntity)groupEntity.GroupItemCollection.get(i)).ItemList.size();
    } 
  }
  
  public int getItemCount() {
    int j = 0;
    int i = 0;
    label12: while (true) {
      if (i >= this.mGroupCollection.size()) {
        Trace.d(this.TAG, "end getItemCount() itemCount : " + j);
        return j;
      } 
      GroupEntity groupEntity = this.mGroupCollection.get(i);
      for (int k = 0;; k++) {
        if (k >= groupEntity.GroupItemCollection.size()) {
          i++;
          continue label12;
        } 
        j += ((GroupEntity.GroupItemEntity)groupEntity.GroupItemCollection.get(k)).ItemList.size();
      } 
      break;
    } 
  }
  
  public boolean hasStableIds() {
    return true;
  }
  
  public boolean isChildSelectable(int paramInt1, int paramInt2) {
    return true;
  }
  
  private class CheckBoxClickListener implements View.OnClickListener {
    private CheckBoxClickListener() {}
    
    public void onClick(View param1View) {
      Item item = (Item)param1View.getTag();
      if (item.getItemState() != 3) {
        if (((CheckBox)param1View).isChecked()) {
          if (ExpandableListAdapter.this.getCheckedItemCount() >= 1000) {
            ((CheckBox)param1View).setChecked(false);
            item.setItemState(1);
            ExpandableListAdapter.this.mExToast.show(4);
          } else if (Utils.getAvailableExternalMemorySize() < ExpandableListAdapter.this.getCheckedItemSize() + Long.parseLong(item.getSize())) {
            ((CheckBox)param1View).setChecked(false);
            item.setItemState(1);
            ExpandableListAdapter.this.mExToast.show(6);
          } else if (CMInfo.getInstance().getConnectedSSID() != null && CMInfo.getInstance().getConnectedSSID().contains("QF30") && ExpandableListAdapter.this.getCheckedItemSize() + Long.parseLong(item.getSize()) > 2147483648L) {
            ((CheckBox)param1View).setChecked(false);
            item.setItemState(1);
            ExpandableListAdapter.this.mExToast.show(5);
          } else {
            item.setItemState(2);
            ExpandableListAdapter.this.mHandler.sendEmptyMessage(37);
          } 
          int j = 0;
          while (true) {
            if (j < ExpandableListAdapter.this.mGroupCollection.size()) {
              GroupEntity groupEntity = ExpandableListAdapter.this.mGroupCollection.get(j);
              if (groupEntity.Name.equals(item.getDate())) {
                if (ExpandableListAdapter.this.getGroupUncheckedItemCount(j) == 0) {
                  groupEntity.groupCheckedAllStatus = true;
                  ((ExpandableListAdapter.GroupHolder)ExpandableListAdapter.this.mGroupMap.get(Integer.valueOf(j))).title.setChecked(true);
                  return;
                } 
                return;
              } 
              j++;
              continue;
            } 
            return;
          } 
        } 
        int i = 0;
        while (true) {
          if (i < ExpandableListAdapter.this.mGroupCollection.size())
            if ((ExpandableListAdapter.this.mGroupCollection.get(i)).Name.equals(item.getDate())) {
              (ExpandableListAdapter.this.mGroupCollection.get(i)).groupCheckedAllStatus = false;
              ((ExpandableListAdapter.GroupHolder)ExpandableListAdapter.this.mGroupMap.get(Integer.valueOf(i))).title.setChecked(false);
            } else {
              i++;
              continue;
            }  
          item.setItemState(1);
          ExpandableListAdapter.this.mHandler.sendEmptyMessage(37);
          return;
        } 
      } 
    }
  }
  
  private class CheckBoxLongClickListener implements View.OnLongClickListener {
    private CheckBoxLongClickListener() {}
    
    public boolean onLongClick(View param1View) {
      if (((Item)param1View.getTag()).isSupported()) {
        Message message = new Message();
        message.what = 42;
        message.obj = param1View.getTag();
        ExpandableListAdapter.this.mHandler.sendMessage(message);
      } 
      return true;
    }
  }
  
  class ChildHolder {
    public CheckBox[] checkBox = new CheckBox[8];
    
    public FrameLayout[] frame = new FrameLayout[8];
    
    public ImageView[] movieImage = new ImageView[8];
    
    public ImageView[] notSupportMask = new ImageView[8];
    
    public ImageView[] thumbImg = new ImageView[8];
  }
  
  public class GroupHolder {
    public ImageView img;
    
    public CheckBox title;
  }
  
  private class imageLongclicklistener implements View.OnLongClickListener {
    private View view;
    
    public imageLongclicklistener(View param1View) {
      this.view = param1View;
    }
    
    public boolean onLongClick(View param1View) {
      Item item = (Item)this.view.getTag();
      if (item != null) {
        ExpandableListAdapter.this.EnlargeImagedialog = new Dialog((Context)ExpandableListAdapter.this.activity);
        ExpandableListAdapter.this.EnlargeImagedialog.requestWindowFeature(1);
        ExpandableListAdapter.this.EnlargeImagedialog.setContentView(2130903111);
        ImageView imageView = (ImageView)ExpandableListAdapter.this.EnlargeImagedialog.findViewById(2131558722);
        ExpandableListAdapter.this.item_sreenRes = item.getScreenRes();
        if (ExpandableListAdapter.this.item_sreenRes == null) {
          ExpandableListAdapter.this.mImageLoader.setImage(item.getThumbRes(), ExpandableListAdapter.this.activity, imageView, item);
        } else {
          ExpandableListAdapter.this.mImageLoader.setImage(ExpandableListAdapter.this.item_sreenRes, ExpandableListAdapter.this.activity, imageView, item);
        } 
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
              public boolean onLongClick(View param2View) {
                (ExpandableListAdapter.imageLongclicklistener.access$0(ExpandableListAdapter.imageLongclicklistener.this)).EnlargeImagedialog.dismiss();
                return false;
              }
            });
        ExpandableListAdapter.this.EnlargeImagedialog.show();
        imageView.setOnTouchListener(new View.OnTouchListener() {
              public boolean onTouch(View param2View, MotionEvent param2MotionEvent) {
                (ExpandableListAdapter.imageLongclicklistener.access$0(ExpandableListAdapter.imageLongclicklistener.this)).EnlargeImagedialog.dismiss();
                return false;
              }
            });
      } 
      return false;
    }
  }
  
  class null implements View.OnLongClickListener {
    public boolean onLongClick(View param1View) {
      (ExpandableListAdapter.imageLongclicklistener.access$0(this.this$1)).EnlargeImagedialog.dismiss();
      return false;
    }
  }
  
  class null implements View.OnTouchListener {
    public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
      (ExpandableListAdapter.imageLongclicklistener.access$0(this.this$1)).EnlargeImagedialog.dismiss();
      return false;
    }
  }
  
  private class thumbClickListener implements View.OnClickListener {
    View v;
    
    public thumbClickListener(View param1View) {
      this.v = param1View;
    }
    
    public void onClick(View param1View) {
      if (((CheckBox)this.v).isChecked()) {
        ((CheckBox)this.v).setChecked(false);
      } else {
        ((CheckBox)this.v).setChecked(true);
      } 
      Trace.d(Trace.Tag.COMMON, "click thumbnail sending " + ((CheckBox)this.v).isChecked());
      Item item = (Item)this.v.getTag();
      if (item.getItemState() != 3) {
        if (((CheckBox)this.v).isChecked()) {
          if (ExpandableListAdapter.this.getCheckedItemCount() >= 1000) {
            item.setItemState(1);
            ExpandableListAdapter.this.mExToast.show(4);
          } else if (Utils.getAvailableExternalMemorySize() < ExpandableListAdapter.this.getCheckedItemSize() + Long.parseLong(item.getSize())) {
            item.setItemState(1);
            ExpandableListAdapter.this.mExToast.show(6);
          } else if (CMInfo.getInstance().getConnectedSSID() != null && CMInfo.getInstance().getConnectedSSID().contains("QF30") && ExpandableListAdapter.this.getCheckedItemSize() + Long.parseLong(item.getSize()) > 2147483648L) {
            item.setItemState(1);
            ExpandableListAdapter.this.mExToast.show(5);
          } else {
            item.setItemState(2);
            ExpandableListAdapter.this.mHandler.sendEmptyMessage(37);
          } 
          int j = 0;
          while (true) {
            if (j < ExpandableListAdapter.this.mGroupCollection.size()) {
              GroupEntity groupEntity = ExpandableListAdapter.this.mGroupCollection.get(j);
              if (groupEntity.Name.equals(item.getDate())) {
                if (ExpandableListAdapter.this.getGroupUncheckedItemCount(j) == 0) {
                  groupEntity.groupCheckedAllStatus = true;
                  ((ExpandableListAdapter.GroupHolder)ExpandableListAdapter.this.mGroupMap.get(Integer.valueOf(j))).title.setChecked(true);
                  return;
                } 
                return;
              } 
              j++;
              continue;
            } 
            return;
          } 
        } 
        int i = 0;
        while (true) {
          if (i < ExpandableListAdapter.this.mGroupCollection.size())
            if ((ExpandableListAdapter.this.mGroupCollection.get(i)).Name.equals(item.getDate())) {
              (ExpandableListAdapter.this.mGroupCollection.get(i)).groupCheckedAllStatus = false;
              ((ExpandableListAdapter.GroupHolder)ExpandableListAdapter.this.mGroupMap.get(Integer.valueOf(i))).title.setChecked(false);
            } else {
              i++;
              continue;
            }  
          item.setItemState(1);
          ExpandableListAdapter.this.mHandler.sendEmptyMessage(37);
          return;
        } 
      } 
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\demo\ml\adapter\ExpandableListAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */