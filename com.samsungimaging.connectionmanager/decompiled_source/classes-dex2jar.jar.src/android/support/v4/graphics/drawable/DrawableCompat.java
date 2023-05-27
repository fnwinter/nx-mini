package android.support.v4.graphics.drawable;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;

public class DrawableCompat {
  static final DrawableImpl IMPL = new BaseDrawableImpl();
  
  public static int getLayoutDirection(Drawable paramDrawable) {
    return IMPL.getLayoutDirection(paramDrawable);
  }
  
  public static boolean isAutoMirrored(Drawable paramDrawable) {
    return IMPL.isAutoMirrored(paramDrawable);
  }
  
  public static void jumpToCurrentState(Drawable paramDrawable) {
    IMPL.jumpToCurrentState(paramDrawable);
  }
  
  public static void setAutoMirrored(Drawable paramDrawable, boolean paramBoolean) {
    IMPL.setAutoMirrored(paramDrawable, paramBoolean);
  }
  
  public static void setHotspot(Drawable paramDrawable, float paramFloat1, float paramFloat2) {
    IMPL.setHotspot(paramDrawable, paramFloat1, paramFloat2);
  }
  
  public static void setHotspotBounds(Drawable paramDrawable, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
    IMPL.setHotspotBounds(paramDrawable, paramInt1, paramInt2, paramInt3, paramInt4);
  }
  
  public static void setLayoutDirection(Drawable paramDrawable, int paramInt) {
    IMPL.setLayoutDirection(paramDrawable, paramInt);
  }
  
  public static void setTint(Drawable paramDrawable, int paramInt) {
    IMPL.setTint(paramDrawable, paramInt);
  }
  
  public static void setTintList(Drawable paramDrawable, ColorStateList paramColorStateList) {
    IMPL.setTintList(paramDrawable, paramColorStateList);
  }
  
  public static void setTintMode(Drawable paramDrawable, PorterDuff.Mode paramMode) {
    IMPL.setTintMode(paramDrawable, paramMode);
  }
  
  public static <T extends Drawable> T unwrap(Drawable paramDrawable) {
    Drawable drawable = paramDrawable;
    if (paramDrawable instanceof DrawableWrapper)
      drawable = ((DrawableWrapper)paramDrawable).getWrappedDrawable(); 
    return (T)drawable;
  }
  
  public static Drawable wrap(Drawable paramDrawable) {
    return IMPL.wrap(paramDrawable);
  }
  
  static {
    int i = Build.VERSION.SDK_INT;
    if (i >= 23) {
      IMPL = new MDrawableImpl();
      return;
    } 
    if (i >= 22) {
      IMPL = new LollipopMr1DrawableImpl();
      return;
    } 
    if (i >= 21) {
      IMPL = new LollipopDrawableImpl();
      return;
    } 
    if (i >= 19) {
      IMPL = new KitKatDrawableImpl();
      return;
    } 
    if (i >= 17) {
      IMPL = new JellybeanMr1DrawableImpl();
      return;
    } 
    if (i >= 11) {
      IMPL = new HoneycombDrawableImpl();
      return;
    } 
  }
  
  static class BaseDrawableImpl implements DrawableImpl {
    public int getLayoutDirection(Drawable param1Drawable) {
      return 0;
    }
    
    public boolean isAutoMirrored(Drawable param1Drawable) {
      return false;
    }
    
    public void jumpToCurrentState(Drawable param1Drawable) {}
    
    public void setAutoMirrored(Drawable param1Drawable, boolean param1Boolean) {}
    
    public void setHotspot(Drawable param1Drawable, float param1Float1, float param1Float2) {}
    
    public void setHotspotBounds(Drawable param1Drawable, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {}
    
    public void setLayoutDirection(Drawable param1Drawable, int param1Int) {}
    
    public void setTint(Drawable param1Drawable, int param1Int) {
      DrawableCompatBase.setTint(param1Drawable, param1Int);
    }
    
    public void setTintList(Drawable param1Drawable, ColorStateList param1ColorStateList) {
      DrawableCompatBase.setTintList(param1Drawable, param1ColorStateList);
    }
    
    public void setTintMode(Drawable param1Drawable, PorterDuff.Mode param1Mode) {
      DrawableCompatBase.setTintMode(param1Drawable, param1Mode);
    }
    
    public Drawable wrap(Drawable param1Drawable) {
      return DrawableCompatBase.wrapForTinting(param1Drawable);
    }
  }
  
  static interface DrawableImpl {
    int getLayoutDirection(Drawable param1Drawable);
    
    boolean isAutoMirrored(Drawable param1Drawable);
    
    void jumpToCurrentState(Drawable param1Drawable);
    
    void setAutoMirrored(Drawable param1Drawable, boolean param1Boolean);
    
    void setHotspot(Drawable param1Drawable, float param1Float1, float param1Float2);
    
    void setHotspotBounds(Drawable param1Drawable, int param1Int1, int param1Int2, int param1Int3, int param1Int4);
    
    void setLayoutDirection(Drawable param1Drawable, int param1Int);
    
    void setTint(Drawable param1Drawable, int param1Int);
    
    void setTintList(Drawable param1Drawable, ColorStateList param1ColorStateList);
    
    void setTintMode(Drawable param1Drawable, PorterDuff.Mode param1Mode);
    
    Drawable wrap(Drawable param1Drawable);
  }
  
  static class HoneycombDrawableImpl extends BaseDrawableImpl {
    public void jumpToCurrentState(Drawable param1Drawable) {
      DrawableCompatHoneycomb.jumpToCurrentState(param1Drawable);
    }
    
    public Drawable wrap(Drawable param1Drawable) {
      return DrawableCompatHoneycomb.wrapForTinting(param1Drawable);
    }
  }
  
  static class JellybeanMr1DrawableImpl extends HoneycombDrawableImpl {
    public int getLayoutDirection(Drawable param1Drawable) {
      int i = DrawableCompatJellybeanMr1.getLayoutDirection(param1Drawable);
      return (i >= 0) ? i : 0;
    }
    
    public void setLayoutDirection(Drawable param1Drawable, int param1Int) {
      DrawableCompatJellybeanMr1.setLayoutDirection(param1Drawable, param1Int);
    }
  }
  
  static class KitKatDrawableImpl extends JellybeanMr1DrawableImpl {
    public boolean isAutoMirrored(Drawable param1Drawable) {
      return DrawableCompatKitKat.isAutoMirrored(param1Drawable);
    }
    
    public void setAutoMirrored(Drawable param1Drawable, boolean param1Boolean) {
      DrawableCompatKitKat.setAutoMirrored(param1Drawable, param1Boolean);
    }
    
    public Drawable wrap(Drawable param1Drawable) {
      return DrawableCompatKitKat.wrapForTinting(param1Drawable);
    }
  }
  
  static class LollipopDrawableImpl extends KitKatDrawableImpl {
    public void setHotspot(Drawable param1Drawable, float param1Float1, float param1Float2) {
      DrawableCompatLollipop.setHotspot(param1Drawable, param1Float1, param1Float2);
    }
    
    public void setHotspotBounds(Drawable param1Drawable, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
      DrawableCompatLollipop.setHotspotBounds(param1Drawable, param1Int1, param1Int2, param1Int3, param1Int4);
    }
    
    public void setTint(Drawable param1Drawable, int param1Int) {
      DrawableCompatLollipop.setTint(param1Drawable, param1Int);
    }
    
    public void setTintList(Drawable param1Drawable, ColorStateList param1ColorStateList) {
      DrawableCompatLollipop.setTintList(param1Drawable, param1ColorStateList);
    }
    
    public void setTintMode(Drawable param1Drawable, PorterDuff.Mode param1Mode) {
      DrawableCompatLollipop.setTintMode(param1Drawable, param1Mode);
    }
    
    public Drawable wrap(Drawable param1Drawable) {
      return DrawableCompatLollipop.wrapForTinting(param1Drawable);
    }
  }
  
  static class LollipopMr1DrawableImpl extends LollipopDrawableImpl {
    public Drawable wrap(Drawable param1Drawable) {
      return DrawableCompatApi22.wrapForTinting(param1Drawable);
    }
  }
  
  static class MDrawableImpl extends LollipopMr1DrawableImpl {
    public int getLayoutDirection(Drawable param1Drawable) {
      return DrawableCompatApi23.getLayoutDirection(param1Drawable);
    }
    
    public void setLayoutDirection(Drawable param1Drawable, int param1Int) {
      DrawableCompatApi23.setLayoutDirection(param1Drawable, param1Int);
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\graphics\drawable\DrawableCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */