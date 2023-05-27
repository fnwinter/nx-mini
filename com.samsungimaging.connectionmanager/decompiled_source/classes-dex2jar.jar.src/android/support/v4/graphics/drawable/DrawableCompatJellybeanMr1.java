package android.support.v4.graphics.drawable;

import android.graphics.drawable.Drawable;
import android.util.Log;
import java.lang.reflect.Method;

class DrawableCompatJellybeanMr1 {
  private static final String TAG = "DrawableCompatJellybeanMr1";
  
  private static Method sGetLayoutDirectionMethod;
  
  private static boolean sGetLayoutDirectionMethodFetched;
  
  private static Method sSetLayoutDirectionMethod;
  
  private static boolean sSetLayoutDirectionMethodFetched;
  
  public static int getLayoutDirection(Drawable paramDrawable) {
    // Byte code:
    //   0: getstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethodFetched : Z
    //   3: ifne -> 31
    //   6: ldc android/graphics/drawable/Drawable
    //   8: ldc 'getLayoutDirection'
    //   10: iconst_0
    //   11: anewarray java/lang/Class
    //   14: invokevirtual getDeclaredMethod : (Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
    //   17: putstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod : Ljava/lang/reflect/Method;
    //   20: getstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod : Ljava/lang/reflect/Method;
    //   23: iconst_1
    //   24: invokevirtual setAccessible : (Z)V
    //   27: iconst_1
    //   28: putstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethodFetched : Z
    //   31: getstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod : Ljava/lang/reflect/Method;
    //   34: ifnull -> 84
    //   37: getstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod : Ljava/lang/reflect/Method;
    //   40: aload_0
    //   41: iconst_0
    //   42: anewarray java/lang/Object
    //   45: invokevirtual invoke : (Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
    //   48: checkcast java/lang/Integer
    //   51: invokevirtual intValue : ()I
    //   54: istore_1
    //   55: iload_1
    //   56: ireturn
    //   57: astore_2
    //   58: ldc 'DrawableCompatJellybeanMr1'
    //   60: ldc 'Failed to retrieve getLayoutDirection() method'
    //   62: aload_2
    //   63: invokestatic i : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   66: pop
    //   67: goto -> 27
    //   70: astore_0
    //   71: ldc 'DrawableCompatJellybeanMr1'
    //   73: ldc 'Failed to invoke getLayoutDirection() via reflection'
    //   75: aload_0
    //   76: invokestatic i : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
    //   79: pop
    //   80: aconst_null
    //   81: putstatic android/support/v4/graphics/drawable/DrawableCompatJellybeanMr1.sGetLayoutDirectionMethod : Ljava/lang/reflect/Method;
    //   84: iconst_m1
    //   85: ireturn
    // Exception table:
    //   from	to	target	type
    //   6	27	57	java/lang/NoSuchMethodException
    //   37	55	70	java/lang/Exception
  }
  
  public static void setLayoutDirection(Drawable paramDrawable, int paramInt) {
    if (!sSetLayoutDirectionMethodFetched) {
      try {
        sSetLayoutDirectionMethod = Drawable.class.getDeclaredMethod("setLayoutDirection", new Class[] { int.class });
        sSetLayoutDirectionMethod.setAccessible(true);
      } catch (NoSuchMethodException noSuchMethodException) {
        Log.i("DrawableCompatJellybeanMr1", "Failed to retrieve setLayoutDirection(int) method", noSuchMethodException);
      } 
      sSetLayoutDirectionMethodFetched = true;
    } 
    if (sSetLayoutDirectionMethod != null)
      try {
        sSetLayoutDirectionMethod.invoke(paramDrawable, new Object[] { Integer.valueOf(paramInt) });
        return;
      } catch (Exception exception) {
        Log.i("DrawableCompatJellybeanMr1", "Failed to invoke setLayoutDirection(int) via reflection", exception);
        sSetLayoutDirectionMethod = null;
        return;
      }  
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\graphics\drawable\DrawableCompatJellybeanMr1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */