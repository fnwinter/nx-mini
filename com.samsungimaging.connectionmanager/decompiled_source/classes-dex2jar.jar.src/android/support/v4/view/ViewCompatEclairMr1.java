package android.support.v4.view;

import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.Method;

class ViewCompatEclairMr1 {
  public static final String TAG = "ViewCompat";
  
  private static Method sChildrenDrawingOrderMethod;
  
  public static boolean isOpaque(View paramView) {
    return paramView.isOpaque();
  }
  
  public static void setChildrenDrawingOrderEnabled(ViewGroup paramViewGroup, boolean paramBoolean) {
    // Byte code:
    //   0: getstatic android/support/v4/view/ViewCompatEclairMr1.sChildrenDrawingOrderMethod : Ljava/lang/reflect/Method;
    //   3: ifnonnull -> 33
    //   6: ldc android/view/ViewGroup
    //   8: ldc 'setChildrenDrawingOrderEnabled'
    //   10: iconst_1
    //   11: anewarray java/lang/Class
    //   14: dup
    //   15: iconst_0
    //   16: getstatic java/lang/Boolean.TYPE : Ljava/lang/Class;
    //   19: aastore
    //   20: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   23: putstatic android/support/v4/view/ViewCompatEclairMr1.sChildrenDrawingOrderMethod : Ljava/lang/reflect/Method;
    //   26: getstatic android/support/v4/view/ViewCompatEclairMr1.sChildrenDrawingOrderMethod : Ljava/lang/reflect/Method;
    //   29: iconst_1
    //   30: invokevirtual setAccessible : (Z)V
    //   33: getstatic android/support/v4/view/ViewCompatEclairMr1.sChildrenDrawingOrderMethod : Ljava/lang/reflect/Method;
    //   36: aload_0
    //   37: iconst_1
    //   38: anewarray java/lang/Object
    //   41: dup
    //   42: iconst_0
    //   43: iload_1
    //   44: invokestatic valueOf : (Z)Ljava/lang/Boolean;
    //   47: aastore
    //   48: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   51: pop
    //   52: return
    //   53: astore_2
    //   54: ldc 'ViewCompat'
    //   56: ldc 'Unable to find childrenDrawingOrderEnabled'
    //   58: aload_2
    //   59: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   62: pop
    //   63: goto -> 26
    //   66: astore_0
    //   67: ldc 'ViewCompat'
    //   69: ldc 'Unable to invoke childrenDrawingOrderEnabled'
    //   71: aload_0
    //   72: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   75: pop
    //   76: return
    //   77: astore_0
    //   78: ldc 'ViewCompat'
    //   80: ldc 'Unable to invoke childrenDrawingOrderEnabled'
    //   82: aload_0
    //   83: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   86: pop
    //   87: return
    //   88: astore_0
    //   89: ldc 'ViewCompat'
    //   91: ldc 'Unable to invoke childrenDrawingOrderEnabled'
    //   93: aload_0
    //   94: invokestatic e : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   97: pop
    //   98: return
    // Exception table:
    //   from	to	target	type
    //   6	26	53	java/lang/NoSuchMethodException
    //   33	52	66	java/lang/IllegalAccessException
    //   33	52	77	java/lang/IllegalArgumentException
    //   33	52	88	java/lang/reflect/InvocationTargetException
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\view\ViewCompatEclairMr1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */