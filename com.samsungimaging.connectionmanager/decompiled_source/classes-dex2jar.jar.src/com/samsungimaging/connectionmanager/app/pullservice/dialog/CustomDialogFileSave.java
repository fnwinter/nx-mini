package com.samsungimaging.connectionmanager.app.pullservice.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.samsungimaging.connectionmanager.app.pullservice.PullService;
import com.samsungimaging.connectionmanager.app.pullservice.demo.ml.ImageLoader;
import com.samsungimaging.connectionmanager.app.pullservice.demo.ml.Item;
import com.samsungimaging.connectionmanager.app.pullservice.demo.ml.Utils;
import com.samsungimaging.connectionmanager.util.Trace;
import com.samsungimaging.connectionmanager.util.Utils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.List;

public class CustomDialogFileSave extends AsyncTask<Void, Bundle, Integer> {
  private static final Trace.Tag TAG = Trace.Tag.COMMON;
  
  private DialogInterface.OnClickListener clickListener = null;
  
  private Button closenotice;
  
  private DialogInterface.OnKeyListener keyListener = null;
  
  private LinearLayout layout;
  
  private Context mContext = null;
  
  private Item mCurrentSavingItem = null;
  
  private String mCurrentSavingItemPath = null;
  
  private Handler mHandler = null;
  
  private ImageLoader mImageLoader = null;
  
  private boolean mIsResizeMode = false;
  
  private List<Item> mItems = null;
  
  private long mPartInfo_itemDownloadedSize = 0L;
  
  private long mPartInfo_itemSize = 0L;
  
  private Dialog mProgressDialog = null;
  
  private long mTotalInfo_DownloadedItemSize = 0L;
  
  private long mTotalInfo_TotalCheckedItemSize = 0L;
  
  private TextView ml_partInfo_downloadedSize;
  
  private TextView ml_partInfo_fileName;
  
  private TextView ml_partInfo_fileSize;
  
  private ProgressBar ml_partInfo_progressBar;
  
  private LinearLayout ml_totalInfo;
  
  private TextView ml_totalInfo_currentDownloadedSize;
  
  private TextView ml_totalInfo_currentIndex;
  
  private ProgressBar ml_totalInfo_progressBar;
  
  private LinearLayout ml_totalInfo_size;
  
  private TextView ml_totalInfo_totalCount;
  
  private TextView ml_totalInfo_totalSize;
  
  Dialog notice = null;
  
  private ImageView showPicture;
  
  private TextView txtView;
  
  private TextView txtView2;
  
  public CustomDialogFileSave(Context paramContext, Handler paramHandler, boolean paramBoolean, List<Item> paramList, ImageLoader paramImageLoader) {
    Trace.d(TAG, "start CustomDialogFileSave() isResizeMode : " + paramBoolean);
    this.mContext = paramContext;
    this.mHandler = paramHandler;
    this.mIsResizeMode = paramBoolean;
    this.mItems = paramList;
    this.mImageLoader = paramImageLoader;
  }
  
  private String getDownloadFileName(Item paramItem) {
    Trace.d(TAG, "start getDownloadFileName()");
    String str2 = paramItem.getTitle();
    String str1 = str2;
    if (!str2.contains(".")) {
      String str = Utils.getExtention(paramItem.getRes().toLowerCase());
      str1 = str2;
      if (str != null) {
        str1 = str2;
        if (str.length() > 0)
          str1 = String.valueOf(str2) + "." + str; 
      } 
    } 
    return String.valueOf(Utils.getDefaultStorage()) + "/" + Utils.renameFile(Utils.getDefaultStorage(), str1);
  }
  
  private long getProgressUpdateIntervalSize(long paramLong) {
    Trace.d(TAG, "start getProgressUpdateIntervalSize()");
    long l1 = 1024L * 80L;
    long l2 = 1024L * 800L;
    if (paramLong >= 1048576L) {
      if (paramLong < 104857600L) {
        l1 = (l2 - l1) / 103809024L * paramLong + l1 - 1048576L * (l2 - l1) / 103809024L;
        Trace.d(TAG, "intervalSize : " + l1 + " fileSize : " + paramLong);
        return l1;
      } 
      l1 = l2;
    } 
    Trace.d(TAG, "intervalSize : " + l1 + " fileSize : " + paramLong);
    return l1;
  }
  
  private void hideProgress() {
    Trace.d(TAG, "start hideProgress() mProgressDialog.isShowing : " + this.mProgressDialog.isShowing());
    try {
      this.mProgressDialog.dismiss();
      return;
    } catch (IllegalArgumentException illegalArgumentException) {
      illegalArgumentException.printStackTrace();
      Trace.e(TAG, "Occured IllegalArgumentException on dismiss save dialog.");
      this.mProgressDialog = null;
      return;
    } 
  }
  
  private void saveFile(Item paramItem) throws IOException {
    Trace.d(TAG, "start saveFile()");
    this.mCurrentSavingItem = paramItem;
    this.mCurrentSavingItemPath = getDownloadFileName(paramItem);
    this.mPartInfo_itemSize = Long.parseLong(paramItem.getSize());
    this.mPartInfo_itemDownloadedSize = 0L;
    this.mImageLoader.setImage(this.mCurrentSavingItem.getThumbRes(), (Activity)this.mContext, this.showPicture, this.mCurrentSavingItem);
    FileOutputStream fileOutputStream = new FileOutputStream(this.mCurrentSavingItemPath);
    URLConnection uRLConnection = (new URL(paramItem.getRes())).openConnection();
    uRLConnection.setReadTimeout(5000);
    uRLConnection.connect();
    if (!Utils.getExtention(this.mCurrentSavingItem.getRes()).equalsIgnoreCase("MP4"))
      this.mPartInfo_itemSize = uRLConnection.getContentLength(); 
    Bundle bundle = new Bundle();
    bundle.putString("path", this.mCurrentSavingItemPath);
    bundle.putInt("index", this.mItems.indexOf(this.mCurrentSavingItem));
    bundle.putInt("command", 0);
    publishProgress((Object[])new Bundle[] { bundle });
    BufferedInputStream bufferedInputStream = new BufferedInputStream(uRLConnection.getInputStream(), 8192);
    int i = 0;
    long l = getProgressUpdateIntervalSize(this.mPartInfo_itemSize);
    byte[] arrayOfByte = new byte[4096];
    while (true) {
      if (!isCancelled()) {
        int j = bufferedInputStream.read(arrayOfByte);
        if (j >= 0) {
          this.mPartInfo_itemDownloadedSize += j;
          this.mTotalInfo_DownloadedItemSize += j;
          int k = i;
          if (this.mPartInfo_itemDownloadedSize > i * l) {
            bundle.putInt("command", 1);
            publishProgress((Object[])new Bundle[] { bundle });
            k = i + 1;
          } 
          fileOutputStream.write(arrayOfByte, 0, j);
          i = k;
          continue;
        } 
      } 
      if (!isCancelled()) {
        this.mCurrentSavingItem.setItemState(3);
        bundle.putInt("command", 1);
        publishProgress((Object[])new Bundle[] { bundle });
        fileOutputStream.flush();
        bundle.putInt("command", 2);
        publishProgress((Object[])new Bundle[] { bundle });
        String str = Utils.getMimeType(this.mCurrentSavingItemPath);
        MediaScannerConnection.scanFile(this.mContext, new String[] { this.mCurrentSavingItemPath }, new String[] { str }, null);
        if (this.mContext instanceof PullService)
          ((PullService)this.mContext).CheckDirtyCache(this.mCurrentSavingItemPath); 
        ExifInterface exifInterface = new ExifInterface(this.mCurrentSavingItemPath);
        Trace.d(TAG, "System date : " + Calendar.getInstance().get(1) + "-" + (Calendar.getInstance().get(2) + 1) + "-" + Calendar.getInstance().get(5) + " exif date : " + exifInterface.getAttribute("DateTime") + " sorting Info : " + this.mCurrentSavingItem.getDate());
      } 
      return;
    } 
  }
  
  private void showProgress() {
    // Byte code:
    //   0: getstatic com/samsungimaging/connectionmanager/app/pullservice/dialog/CustomDialogFileSave.TAG : Lcom/samsungimaging/connectionmanager/util/Trace$Tag;
    //   3: ldc_w 'start showProgress()'
    //   6: invokestatic d : (Lcom/samsungimaging/connectionmanager/util/Trace$Tag;Ljava/lang/String;)V
    //   9: aload_0
    //   10: getfield mItems : Ljava/util/List;
    //   13: invokeinterface size : ()I
    //   18: istore_2
    //   19: aload_0
    //   20: getfield mContext : Landroid/content/Context;
    //   23: checkcast android/app/Activity
    //   26: invokevirtual getLayoutInflater : ()Landroid/view/LayoutInflater;
    //   29: ldc_w 2130903076
    //   32: aconst_null
    //   33: invokevirtual inflate : (ILandroid/view/ViewGroup;)Landroid/view/View;
    //   36: astore_3
    //   37: aload_0
    //   38: aload_3
    //   39: ldc_w 2131558589
    //   42: invokevirtual findViewById : (I)Landroid/view/View;
    //   45: checkcast android/widget/ImageView
    //   48: putfield showPicture : Landroid/widget/ImageView;
    //   51: aload_0
    //   52: aload_3
    //   53: ldc_w 2131558591
    //   56: invokevirtual findViewById : (I)Landroid/view/View;
    //   59: checkcast android/widget/TextView
    //   62: putfield ml_partInfo_fileName : Landroid/widget/TextView;
    //   65: aload_0
    //   66: aload_3
    //   67: ldc_w 2131558592
    //   70: invokevirtual findViewById : (I)Landroid/view/View;
    //   73: checkcast android/widget/TextView
    //   76: putfield ml_partInfo_downloadedSize : Landroid/widget/TextView;
    //   79: aload_0
    //   80: aload_3
    //   81: ldc_w 2131558593
    //   84: invokevirtual findViewById : (I)Landroid/view/View;
    //   87: checkcast android/widget/TextView
    //   90: putfield ml_partInfo_fileSize : Landroid/widget/TextView;
    //   93: aload_0
    //   94: aload_3
    //   95: ldc_w 2131558594
    //   98: invokevirtual findViewById : (I)Landroid/view/View;
    //   101: checkcast android/widget/ProgressBar
    //   104: putfield ml_partInfo_progressBar : Landroid/widget/ProgressBar;
    //   107: aload_0
    //   108: aload_3
    //   109: ldc_w 2131558595
    //   112: invokevirtual findViewById : (I)Landroid/view/View;
    //   115: checkcast android/widget/LinearLayout
    //   118: putfield ml_totalInfo : Landroid/widget/LinearLayout;
    //   121: aload_0
    //   122: aload_3
    //   123: ldc_w 2131558596
    //   126: invokevirtual findViewById : (I)Landroid/view/View;
    //   129: checkcast android/widget/TextView
    //   132: putfield ml_totalInfo_currentIndex : Landroid/widget/TextView;
    //   135: aload_0
    //   136: aload_3
    //   137: ldc_w 2131558597
    //   140: invokevirtual findViewById : (I)Landroid/view/View;
    //   143: checkcast android/widget/TextView
    //   146: putfield ml_totalInfo_totalCount : Landroid/widget/TextView;
    //   149: aload_0
    //   150: aload_3
    //   151: ldc_w 2131558598
    //   154: invokevirtual findViewById : (I)Landroid/view/View;
    //   157: checkcast android/widget/LinearLayout
    //   160: putfield ml_totalInfo_size : Landroid/widget/LinearLayout;
    //   163: aload_0
    //   164: aload_3
    //   165: ldc_w 2131558599
    //   168: invokevirtual findViewById : (I)Landroid/view/View;
    //   171: checkcast android/widget/TextView
    //   174: putfield ml_totalInfo_currentDownloadedSize : Landroid/widget/TextView;
    //   177: aload_0
    //   178: aload_3
    //   179: ldc_w 2131558600
    //   182: invokevirtual findViewById : (I)Landroid/view/View;
    //   185: checkcast android/widget/TextView
    //   188: putfield ml_totalInfo_totalSize : Landroid/widget/TextView;
    //   191: aload_0
    //   192: aload_3
    //   193: ldc_w 2131558601
    //   196: invokevirtual findViewById : (I)Landroid/view/View;
    //   199: checkcast android/widget/ProgressBar
    //   202: putfield ml_totalInfo_progressBar : Landroid/widget/ProgressBar;
    //   205: aload_0
    //   206: getfield ml_partInfo_fileName : Landroid/widget/TextView;
    //   209: ldc_w ''
    //   212: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   215: aload_0
    //   216: getfield ml_partInfo_progressBar : Landroid/widget/ProgressBar;
    //   219: sipush #1000
    //   222: invokevirtual setMax : (I)V
    //   225: aload_0
    //   226: getfield ml_partInfo_downloadedSize : Landroid/widget/TextView;
    //   229: lconst_0
    //   230: invokestatic formatSize : (J)Ljava/lang/String;
    //   233: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   236: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   239: aload_0
    //   240: getfield ml_partInfo_fileSize : Landroid/widget/TextView;
    //   243: lconst_0
    //   244: invokestatic formatSize : (J)Ljava/lang/String;
    //   247: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   250: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   253: aload_0
    //   254: getfield mIsResizeMode : Z
    //   257: ifeq -> 456
    //   260: aload_0
    //   261: getfield ml_totalInfo_progressBar : Landroid/widget/ProgressBar;
    //   264: iload_2
    //   265: invokevirtual setMax : (I)V
    //   268: aload_0
    //   269: getfield ml_totalInfo_size : Landroid/widget/LinearLayout;
    //   272: iconst_4
    //   273: invokevirtual setVisibility : (I)V
    //   276: iload_2
    //   277: iconst_1
    //   278: if_icmpne -> 528
    //   281: aload_0
    //   282: getfield ml_totalInfo : Landroid/widget/LinearLayout;
    //   285: bipush #8
    //   287: invokevirtual setVisibility : (I)V
    //   290: aload_0
    //   291: getfield ml_totalInfo_progressBar : Landroid/widget/ProgressBar;
    //   294: bipush #8
    //   296: invokevirtual setVisibility : (I)V
    //   299: aload_0
    //   300: getfield ml_totalInfo_currentIndex : Landroid/widget/TextView;
    //   303: ldc_w '0'
    //   306: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   309: aload_0
    //   310: new android/app/Dialog
    //   313: dup
    //   314: aload_0
    //   315: getfield mContext : Landroid/content/Context;
    //   318: invokespecial <init> : (Landroid/content/Context;)V
    //   321: putfield mProgressDialog : Landroid/app/Dialog;
    //   324: aload_0
    //   325: getfield mProgressDialog : Landroid/app/Dialog;
    //   328: iconst_1
    //   329: invokevirtual requestWindowFeature : (I)Z
    //   332: pop
    //   333: aload_0
    //   334: getfield mProgressDialog : Landroid/app/Dialog;
    //   337: aload_3
    //   338: invokevirtual setContentView : (Landroid/view/View;)V
    //   341: aload_0
    //   342: aload_0
    //   343: getfield mProgressDialog : Landroid/app/Dialog;
    //   346: ldc_w 2131558526
    //   349: invokevirtual findViewById : (I)Landroid/view/View;
    //   352: checkcast android/widget/Button
    //   355: putfield closenotice : Landroid/widget/Button;
    //   358: aload_0
    //   359: getfield closenotice : Landroid/widget/Button;
    //   362: new com/samsungimaging/connectionmanager/app/pullservice/dialog/CustomDialogFileSave$1
    //   365: dup
    //   366: aload_0
    //   367: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/dialog/CustomDialogFileSave;)V
    //   370: invokevirtual setOnClickListener : (Landroid/view/View$OnClickListener;)V
    //   373: aload_0
    //   374: getfield mProgressDialog : Landroid/app/Dialog;
    //   377: new com/samsungimaging/connectionmanager/app/pullservice/dialog/CustomDialogFileSave$2
    //   380: dup
    //   381: aload_0
    //   382: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/app/pullservice/dialog/CustomDialogFileSave;)V
    //   385: invokevirtual setOnKeyListener : (Landroid/content/DialogInterface$OnKeyListener;)V
    //   388: aload_0
    //   389: aload_0
    //   390: getfield mProgressDialog : Landroid/app/Dialog;
    //   393: ldc_w 2131558500
    //   396: invokevirtual findViewById : (I)Landroid/view/View;
    //   399: checkcast android/widget/TextView
    //   402: putfield txtView : Landroid/widget/TextView;
    //   405: aload_0
    //   406: aload_0
    //   407: getfield mProgressDialog : Landroid/app/Dialog;
    //   410: ldc_w 2131558587
    //   413: invokevirtual findViewById : (I)Landroid/view/View;
    //   416: checkcast android/widget/LinearLayout
    //   419: putfield layout : Landroid/widget/LinearLayout;
    //   422: aload_0
    //   423: getfield layout : Landroid/widget/LinearLayout;
    //   426: iconst_0
    //   427: invokevirtual setVisibility : (I)V
    //   430: aload_0
    //   431: getfield txtView : Landroid/widget/TextView;
    //   434: ldc_w 2131361897
    //   437: invokevirtual setText : (I)V
    //   440: aload_0
    //   441: getfield mProgressDialog : Landroid/app/Dialog;
    //   444: iconst_0
    //   445: invokevirtual setCanceledOnTouchOutside : (Z)V
    //   448: aload_0
    //   449: getfield mProgressDialog : Landroid/app/Dialog;
    //   452: invokevirtual show : ()V
    //   455: return
    //   456: aload_0
    //   457: getfield ml_totalInfo_progressBar : Landroid/widget/ProgressBar;
    //   460: sipush #1000
    //   463: invokevirtual setMax : (I)V
    //   466: iconst_0
    //   467: istore_1
    //   468: iload_1
    //   469: iload_2
    //   470: if_icmplt -> 493
    //   473: aload_0
    //   474: getfield ml_totalInfo_totalSize : Landroid/widget/TextView;
    //   477: aload_0
    //   478: getfield mTotalInfo_TotalCheckedItemSize : J
    //   481: invokestatic formatSize : (J)Ljava/lang/String;
    //   484: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
    //   487: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   490: goto -> 276
    //   493: aload_0
    //   494: aload_0
    //   495: getfield mTotalInfo_TotalCheckedItemSize : J
    //   498: aload_0
    //   499: getfield mItems : Ljava/util/List;
    //   502: iload_1
    //   503: invokeinterface get : (I)Ljava/lang/Object;
    //   508: checkcast com/samsungimaging/connectionmanager/app/pullservice/demo/ml/Item
    //   511: invokevirtual getSize : ()Ljava/lang/String;
    //   514: invokestatic parseLong : (Ljava/lang/String;)J
    //   517: ladd
    //   518: putfield mTotalInfo_TotalCheckedItemSize : J
    //   521: iload_1
    //   522: iconst_1
    //   523: iadd
    //   524: istore_1
    //   525: goto -> 468
    //   528: aload_0
    //   529: getfield ml_totalInfo_totalCount : Landroid/widget/TextView;
    //   532: iload_2
    //   533: invokestatic valueOf : (I)Ljava/lang/String;
    //   536: invokevirtual setText : (Ljava/lang/CharSequence;)V
    //   539: goto -> 299
  }
  
  protected Integer doInBackground(Void... paramVarArgs) {
    Trace.d(TAG, "start doInBackground()");
    int j = this.mItems.size();
    Trace.d(TAG, "Download item count : " + j);
    int i = 0;
    while (true) {
      if (i >= j || isCancelled())
        return Integer.valueOf(0); 
      try {
        if (Long.parseLong(((Item)this.mItems.get(i)).getSize()) < Utils.getAvailableExternalMemorySize()) {
          saveFile(this.mItems.get(i));
          i++;
          continue;
        } 
        Log.d("Not enough memory", "Not enough memory is available on phone");
        return Integer.valueOf(2);
      } catch (IOException iOException) {
        iOException.printStackTrace();
        return Integer.valueOf(1);
      } 
    } 
  }
  
  protected void onCancelled() {
    Trace.d(TAG, "start onCancelled() mCurrentSavingItemPath : " + this.mCurrentSavingItemPath);
    File file = new File(this.mCurrentSavingItemPath);
    if (file.exists()) {
      Trace.d(TAG, "delete file");
      file.delete();
    } 
    this.mHandler.sendEmptyMessage(36);
    super.onCancelled();
  }
  
  protected void onPostExecute(Integer paramInteger) {
    Trace.d(TAG, "start onPostExecute()");
    hideProgress();
    switch (paramInteger.intValue()) {
      default:
        super.onPostExecute(paramInteger);
        return;
      case 0:
        this.mHandler.sendEmptyMessage(4);
      case 1:
        this.mHandler.sendEmptyMessage(47);
        onCancelled();
      case 2:
        break;
    } 
    Trace.d(TAG, "start onPostExecute(): result memory full");
    this.mHandler.sendEmptyMessage(21);
    this.mHandler.sendEmptyMessage(36);
  }
  
  protected void onPreExecute() {
    Trace.d(TAG, "start onPreExecute()");
    showProgress();
    super.onPreExecute();
  }
  
  protected void onProgressUpdate(Bundle... paramVarArgs) {
    Integer integer = Integer.valueOf(paramVarArgs[0].getInt("index"));
    String str = paramVarArgs[0].getString("path");
    switch (paramVarArgs[0].getInt("command")) {
      default:
        super.onProgressUpdate((Object[])paramVarArgs);
        return;
      case 0:
        Trace.d(TAG, "START index : " + integer + " path : " + str);
        this.ml_partInfo_fileName.setText(this.mCurrentSavingItem.getTitle());
        this.ml_partInfo_downloadedSize.setText(Utils.formatSize(0L));
        this.ml_partInfo_fileSize.setText(String.valueOf(Utils.formatSize(this.mPartInfo_itemSize)));
        this.ml_partInfo_progressBar.setProgress(0);
        this.ml_totalInfo_currentDownloadedSize.setText("0");
        this.ml_totalInfo_currentIndex.setText(String.valueOf(integer));
        if (this.mIsResizeMode) {
          this.ml_totalInfo_progressBar.setProgress(integer.intValue());
        } else {
          this.ml_totalInfo_progressBar.setProgress(0);
        } 
      case 1:
        this.ml_partInfo_fileName.setText(this.mCurrentSavingItem.getTitle());
        this.ml_partInfo_downloadedSize.setText(String.valueOf(Utils.formatSize(this.mPartInfo_itemDownloadedSize)));
        this.ml_partInfo_fileSize.setText(String.valueOf(Utils.formatSize(this.mPartInfo_itemSize)));
        this.ml_partInfo_progressBar.setProgress((int)(this.mPartInfo_itemDownloadedSize * 1000L / this.mPartInfo_itemSize));
        this.ml_totalInfo_currentDownloadedSize.setText(String.valueOf(Utils.formatSize(this.mTotalInfo_DownloadedItemSize)));
        this.ml_totalInfo_currentIndex.setText(String.valueOf(integer));
        if (this.mIsResizeMode) {
          this.ml_totalInfo_progressBar.setProgress(integer.intValue());
        } else {
          this.ml_totalInfo_progressBar.setProgress((int)(this.mTotalInfo_DownloadedItemSize * 1000L / this.mTotalInfo_TotalCheckedItemSize));
        } 
      case 2:
        break;
    } 
    Trace.d(TAG, "END index : " + integer + " path : " + str);
    this.ml_partInfo_progressBar.setProgress(1000);
    if (this.mIsResizeMode)
      this.ml_totalInfo_progressBar.setProgress(integer.intValue() + 1); 
  }
  
  public void setNegativeButton(DialogInterface.OnClickListener paramOnClickListener) {
    this.clickListener = paramOnClickListener;
  }
  
  public static class Command {
    public static final int END = 2;
    
    public static final int START = 0;
    
    public static final int UPDATE = 1;
  }
  
  public static class ResultID {
    public static final int IO_EXCEPTION = 1;
    
    public static final int MEMORY_FULL = 2;
    
    public static final int SUCCESS = 0;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanager\app\pullservice\dialog\CustomDialogFileSave.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */