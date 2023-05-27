package android.support.v7.widget;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import java.lang.reflect.Field;

class DrawableUtils {
  public static final Rect INSETS_NONE = new Rect();
  
  private static final String TAG = "DrawableUtils";
  
  private static Class<?> sInsetsClazz;
  
  static {
    if (Build.VERSION.SDK_INT >= 18)
      try {
        sInsetsClazz = Class.forName("android.graphics.Insets");
        return;
      } catch (ClassNotFoundException classNotFoundException) {
        return;
      }  
  }
  
  public static Rect getOpticalBounds(Drawable paramDrawable) {
    if (sInsetsClazz != null)
      try {
        paramDrawable = DrawableCompat.unwrap(paramDrawable);
        Object object = paramDrawable.getClass().getMethod("getOpticalInsets", new Class[0]).invoke(paramDrawable, new Object[0]);
        if (object != null) {
          Rect rect = new Rect();
          Field[] arrayOfField = sInsetsClazz.getFields();
          int j = arrayOfField.length;
          for (int i = 0;; i++) {
            Field field;
            byte b;
            Rect rect1 = rect;
            if (i < j) {
              field = arrayOfField[i];
              String str = field.getName();
              b = -1;
              switch (str.hashCode()) {
                case 3317767:
                  if (str.equals("left"))
                    b = 0; 
                  break;
                case 115029:
                  if (str.equals("top"))
                    b = 1; 
                  break;
                case 108511772:
                  if (str.equals("right"))
                    b = 2; 
                  break;
                case -1383228885:
                  if (str.equals("bottom"))
                    b = 3; 
                  break;
              } 
            } else {
              return (Rect)field;
            } 
            switch (b) {
              case 0:
                rect.left = field.getInt(object);
                break;
              case 1:
                rect.top = field.getInt(object);
                break;
              case 2:
                rect.right = field.getInt(object);
                break;
              case 3:
                rect.bottom = field.getInt(object);
                break;
            } 
          } 
        } 
      } catch (Exception exception) {
        Log.e("DrawableUtils", "Couldn't obtain the optical insets. Ignoring.");
      }  
    return INSETS_NONE;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\DrawableUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */