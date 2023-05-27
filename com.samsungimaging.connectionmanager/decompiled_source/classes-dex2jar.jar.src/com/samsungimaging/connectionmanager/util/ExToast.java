package com.samsungimaging.connectionmanager.util;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.LinkedList;

public class ExToast {
  private static final int MSG_TOAST_TIMEOUT = 1;
  
  private static ExToast mExToast = new ExToast();
  
  private LinkedList<String> mQueue = new LinkedList<String>();
  
  private Handler mToastHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        switch (param1Message.what) {
          default:
            return;
          case 1:
            break;
        } 
        synchronized (ExToast.this.mToastMap) {
          String str = (String)param1Message.obj;
          ExToast.this.hide(Integer.parseInt(str));
          ExToast.this.mQueue.remove(str);
          if (!ExToast.this.mQueue.isEmpty()) {
            str = ExToast.this.mQueue.getFirst();
            ExToast.this.doShow(str);
          } 
          return;
        } 
      }
    };
  
  private HashMap<String, MyToast> mToastMap = new HashMap<String, MyToast>();
  
  private void doShow(String paramString) {
    MyToast myToast = this.mToastMap.get(paramString);
    if (myToast != null) {
      myToast.mRunning = true;
      myToast.mToast.show();
      setTimeout(myToast.mTerm, paramString);
    } 
  }
  
  public static ExToast getInstance() {
    // Byte code:
    //   0: ldc com/samsungimaging/connectionmanager/util/ExToast
    //   2: monitorenter
    //   3: getstatic com/samsungimaging/connectionmanager/util/ExToast.mExToast : Lcom/samsungimaging/connectionmanager/util/ExToast;
    //   6: astore_0
    //   7: ldc com/samsungimaging/connectionmanager/util/ExToast
    //   9: monitorexit
    //   10: aload_0
    //   11: areturn
    //   12: astore_0
    //   13: ldc com/samsungimaging/connectionmanager/util/ExToast
    //   15: monitorexit
    //   16: aload_0
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   3	7	12	finally
  }
  
  private Toast makeToast(Context paramContext, View paramView, int paramInt) {
    Toast toast = new Toast(paramContext);
    toast.setView(paramView);
    toast.setDuration(paramInt);
    return toast;
  }
  
  private View makeView(Context paramContext, int paramInt1, int paramInt2) {
    TextView textView = new TextView(paramContext);
    textView.setGravity(17);
    textView.setBackgroundResource(2130838248);
    textView.setText((CharSequence)Html.fromHtml("<b>" + paramContext.getString(paramInt2) + "</b>"));
    textView.setTextSize(paramInt1);
    return (View)textView;
  }
  
  private void setTimeout(int paramInt, String paramString) {
    if (paramInt == 0) {
      paramInt = 2500;
    } else {
      paramInt = 4000;
    } 
    Message message = new Message();
    message.what = 1;
    message.obj = paramString;
    this.mToastHandler.sendMessageDelayed(message, paramInt);
  }
  
  public void hide(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mQueue : Ljava/util/LinkedList;
    //   6: new java/lang/StringBuilder
    //   9: dup
    //   10: invokespecial <init> : ()V
    //   13: iload_1
    //   14: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   17: invokevirtual toString : ()Ljava/lang/String;
    //   20: invokevirtual remove : (Ljava/lang/Object;)Z
    //   23: pop
    //   24: aload_0
    //   25: getfield mToastMap : Ljava/util/HashMap;
    //   28: new java/lang/StringBuilder
    //   31: dup
    //   32: invokespecial <init> : ()V
    //   35: iload_1
    //   36: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   39: invokevirtual toString : ()Ljava/lang/String;
    //   42: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   45: checkcast com/samsungimaging/connectionmanager/util/ExToast$MyToast
    //   48: astore_2
    //   49: aload_2
    //   50: ifnull -> 72
    //   53: aload_2
    //   54: getfield mToast : Landroid/widget/Toast;
    //   57: ifnull -> 72
    //   60: aload_2
    //   61: getfield mToast : Landroid/widget/Toast;
    //   64: invokevirtual cancel : ()V
    //   67: aload_2
    //   68: iconst_0
    //   69: putfield mRunning : Z
    //   72: aload_0
    //   73: monitorexit
    //   74: return
    //   75: astore_2
    //   76: aload_0
    //   77: monitorexit
    //   78: aload_2
    //   79: athrow
    // Exception table:
    //   from	to	target	type
    //   2	49	75	finally
    //   53	72	75	finally
  }
  
  public void hideAll() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mQueue : Ljava/util/LinkedList;
    //   6: invokevirtual clear : ()V
    //   9: aload_0
    //   10: getfield mToastMap : Ljava/util/HashMap;
    //   13: invokevirtual keySet : ()Ljava/util/Set;
    //   16: invokeinterface iterator : ()Ljava/util/Iterator;
    //   21: astore_2
    //   22: aload_2
    //   23: invokeinterface hasNext : ()Z
    //   28: istore_1
    //   29: iload_1
    //   30: ifne -> 36
    //   33: aload_0
    //   34: monitorexit
    //   35: return
    //   36: aload_2
    //   37: invokeinterface next : ()Ljava/lang/Object;
    //   42: checkcast java/lang/String
    //   45: astore_3
    //   46: aload_3
    //   47: ifnull -> 22
    //   50: aload_0
    //   51: getfield mToastMap : Ljava/util/HashMap;
    //   54: aload_3
    //   55: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   58: checkcast com/samsungimaging/connectionmanager/util/ExToast$MyToast
    //   61: astore_3
    //   62: aload_3
    //   63: ifnull -> 22
    //   66: aload_3
    //   67: getfield mToast : Landroid/widget/Toast;
    //   70: ifnull -> 22
    //   73: aload_3
    //   74: getfield mToast : Landroid/widget/Toast;
    //   77: invokevirtual cancel : ()V
    //   80: aload_3
    //   81: iconst_0
    //   82: putfield mRunning : Z
    //   85: goto -> 22
    //   88: astore_2
    //   89: aload_0
    //   90: monitorexit
    //   91: aload_2
    //   92: athrow
    // Exception table:
    //   from	to	target	type
    //   2	22	88	finally
    //   22	29	88	finally
    //   36	46	88	finally
    //   50	62	88	finally
    //   66	85	88	finally
  }
  
  public boolean isShowing() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_1
    //   4: aload_0
    //   5: getfield mToastMap : Ljava/util/HashMap;
    //   8: invokevirtual keySet : ()Ljava/util/Set;
    //   11: invokeinterface iterator : ()Ljava/util/Iterator;
    //   16: astore_3
    //   17: aload_3
    //   18: invokeinterface hasNext : ()Z
    //   23: istore_2
    //   24: iload_2
    //   25: ifne -> 32
    //   28: aload_0
    //   29: monitorexit
    //   30: iload_1
    //   31: ireturn
    //   32: aload_3
    //   33: invokeinterface next : ()Ljava/lang/Object;
    //   38: checkcast java/lang/String
    //   41: astore #4
    //   43: aload #4
    //   45: ifnull -> 17
    //   48: aload_0
    //   49: getfield mToastMap : Ljava/util/HashMap;
    //   52: aload #4
    //   54: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   57: checkcast com/samsungimaging/connectionmanager/util/ExToast$MyToast
    //   60: astore #4
    //   62: aload #4
    //   64: ifnull -> 17
    //   67: aload #4
    //   69: getfield mRunning : Z
    //   72: istore_2
    //   73: iload_2
    //   74: ifeq -> 17
    //   77: iconst_1
    //   78: istore_1
    //   79: goto -> 17
    //   82: astore_3
    //   83: aload_0
    //   84: monitorexit
    //   85: aload_3
    //   86: athrow
    // Exception table:
    //   from	to	target	type
    //   4	17	82	finally
    //   17	24	82	finally
    //   32	43	82	finally
    //   48	62	82	finally
    //   67	73	82	finally
  }
  
  public boolean isShowing(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: iconst_0
    //   3: istore_3
    //   4: aload_0
    //   5: getfield mToastMap : Ljava/util/HashMap;
    //   8: new java/lang/StringBuilder
    //   11: dup
    //   12: invokespecial <init> : ()V
    //   15: iload_1
    //   16: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   19: invokevirtual toString : ()Ljava/lang/String;
    //   22: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   25: checkcast com/samsungimaging/connectionmanager/util/ExToast$MyToast
    //   28: astore #5
    //   30: iload_3
    //   31: istore_2
    //   32: aload #5
    //   34: ifnull -> 53
    //   37: aload #5
    //   39: getfield mRunning : Z
    //   42: istore #4
    //   44: iload_3
    //   45: istore_2
    //   46: iload #4
    //   48: ifeq -> 53
    //   51: iconst_1
    //   52: istore_2
    //   53: aload_0
    //   54: monitorexit
    //   55: iload_2
    //   56: ireturn
    //   57: astore #5
    //   59: aload_0
    //   60: monitorexit
    //   61: aload #5
    //   63: athrow
    // Exception table:
    //   from	to	target	type
    //   4	30	57	finally
    //   37	44	57	finally
  }
  
  public void register(Context paramContext, int paramInt1, int paramInt2, int paramInt3) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: new com/samsungimaging/connectionmanager/util/ExToast$MyToast
    //   5: dup
    //   6: aload_0
    //   7: aconst_null
    //   8: invokespecial <init> : (Lcom/samsungimaging/connectionmanager/util/ExToast;Lcom/samsungimaging/connectionmanager/util/ExToast$MyToast;)V
    //   11: astore #5
    //   13: aload #5
    //   15: aload_0
    //   16: aload_1
    //   17: aload_0
    //   18: aload_1
    //   19: bipush #12
    //   21: iload_3
    //   22: invokespecial makeView : (Landroid/content/Context;II)Landroid/view/View;
    //   25: iload #4
    //   27: invokespecial makeToast : (Landroid/content/Context;Landroid/view/View;I)Landroid/widget/Toast;
    //   30: putfield mToast : Landroid/widget/Toast;
    //   33: aload #5
    //   35: iload #4
    //   37: putfield mTerm : I
    //   40: aload #5
    //   42: iconst_0
    //   43: putfield mRunning : Z
    //   46: aload_0
    //   47: getfield mToastMap : Ljava/util/HashMap;
    //   50: new java/lang/StringBuilder
    //   53: dup
    //   54: invokespecial <init> : ()V
    //   57: iload_2
    //   58: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   61: invokevirtual toString : ()Ljava/lang/String;
    //   64: aload #5
    //   66: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   69: pop
    //   70: aload_0
    //   71: monitorexit
    //   72: return
    //   73: astore_1
    //   74: aload_0
    //   75: monitorexit
    //   76: aload_1
    //   77: athrow
    // Exception table:
    //   from	to	target	type
    //   2	70	73	finally
  }
  
  public void show(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield mToastMap : Ljava/util/HashMap;
    //   6: new java/lang/StringBuilder
    //   9: dup
    //   10: invokespecial <init> : ()V
    //   13: iload_1
    //   14: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   17: invokevirtual toString : ()Ljava/lang/String;
    //   20: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   23: checkcast com/samsungimaging/connectionmanager/util/ExToast$MyToast
    //   26: astore #4
    //   28: aload #4
    //   30: ifnull -> 68
    //   33: iconst_0
    //   34: istore_2
    //   35: aload_0
    //   36: getfield mQueue : Ljava/util/LinkedList;
    //   39: invokevirtual iterator : ()Ljava/util/Iterator;
    //   42: astore #5
    //   44: aload #5
    //   46: invokeinterface hasNext : ()Z
    //   51: ifne -> 71
    //   54: aload #4
    //   56: getfield mRunning : Z
    //   59: istore_3
    //   60: iload_3
    //   61: ifne -> 68
    //   64: iload_2
    //   65: ifeq -> 106
    //   68: aload_0
    //   69: monitorexit
    //   70: return
    //   71: aload #5
    //   73: invokeinterface next : ()Ljava/lang/Object;
    //   78: checkcast java/lang/String
    //   81: new java/lang/StringBuilder
    //   84: dup
    //   85: invokespecial <init> : ()V
    //   88: iload_1
    //   89: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   92: invokevirtual toString : ()Ljava/lang/String;
    //   95: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   98: ifeq -> 44
    //   101: iconst_1
    //   102: istore_2
    //   103: goto -> 44
    //   106: aload_0
    //   107: getfield mQueue : Ljava/util/LinkedList;
    //   110: new java/lang/StringBuilder
    //   113: dup
    //   114: invokespecial <init> : ()V
    //   117: iload_1
    //   118: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   121: invokevirtual toString : ()Ljava/lang/String;
    //   124: invokevirtual add : (Ljava/lang/Object;)Z
    //   127: pop
    //   128: aload_0
    //   129: getfield mQueue : Ljava/util/LinkedList;
    //   132: invokevirtual size : ()I
    //   135: iconst_1
    //   136: if_icmpne -> 68
    //   139: aload_0
    //   140: new java/lang/StringBuilder
    //   143: dup
    //   144: invokespecial <init> : ()V
    //   147: iload_1
    //   148: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   151: invokevirtual toString : ()Ljava/lang/String;
    //   154: invokespecial doShow : (Ljava/lang/String;)V
    //   157: goto -> 68
    //   160: astore #4
    //   162: aload_0
    //   163: monitorexit
    //   164: aload #4
    //   166: athrow
    // Exception table:
    //   from	to	target	type
    //   2	28	160	finally
    //   35	44	160	finally
    //   44	60	160	finally
    //   71	101	160	finally
    //   106	157	160	finally
  }
  
  public void unregister(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: iload_1
    //   4: invokevirtual hide : (I)V
    //   7: aload_0
    //   8: getfield mToastMap : Ljava/util/HashMap;
    //   11: new java/lang/StringBuilder
    //   14: dup
    //   15: invokespecial <init> : ()V
    //   18: iload_1
    //   19: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   22: invokevirtual toString : ()Ljava/lang/String;
    //   25: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   28: pop
    //   29: aload_0
    //   30: monitorexit
    //   31: return
    //   32: astore_2
    //   33: aload_0
    //   34: monitorexit
    //   35: aload_2
    //   36: athrow
    // Exception table:
    //   from	to	target	type
    //   2	29	32	finally
  }
  
  public void unregisterAll() {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: invokevirtual hideAll : ()V
    //   6: aload_0
    //   7: getfield mToastMap : Ljava/util/HashMap;
    //   10: invokevirtual clear : ()V
    //   13: aload_0
    //   14: monitorexit
    //   15: return
    //   16: astore_1
    //   17: aload_0
    //   18: monitorexit
    //   19: aload_1
    //   20: athrow
    // Exception table:
    //   from	to	target	type
    //   2	13	16	finally
  }
  
  private class MyToast {
    boolean mRunning;
    
    int mTerm;
    
    Toast mToast;
    
    private MyToast() {}
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanage\\util\ExToast.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */