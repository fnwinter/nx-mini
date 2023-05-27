package android.support.v7.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.util.LruCache;
import android.support.v7.appcompat.R;
import android.util.Log;
import android.util.SparseArray;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public final class TintManager {
  private static final int[] COLORFILTER_COLOR_BACKGROUND_MULTIPLY;
  
  private static final int[] COLORFILTER_COLOR_CONTROL_ACTIVATED;
  
  private static final int[] COLORFILTER_TINT_COLOR_CONTROL_NORMAL;
  
  private static final ColorFilterLruCache COLOR_FILTER_CACHE;
  
  private static final boolean DEBUG = false;
  
  private static final PorterDuff.Mode DEFAULT_MODE;
  
  private static final WeakHashMap<Context, TintManager> INSTANCE_CACHE;
  
  public static final boolean SHOULD_BE_USED;
  
  private static final String TAG = "TintManager";
  
  private static final int[] TINT_CHECKABLE_BUTTON_LIST;
  
  private static final int[] TINT_COLOR_CONTROL_NORMAL;
  
  private static final int[] TINT_COLOR_CONTROL_STATE_LIST;
  
  private final WeakReference<Context> mContextRef;
  
  private ColorStateList mDefaultColorStateList;
  
  private SparseArray<ColorStateList> mTintLists;
  
  static {
    boolean bool;
    if (Build.VERSION.SDK_INT < 21) {
      bool = true;
    } else {
      bool = false;
    } 
    SHOULD_BE_USED = bool;
    DEFAULT_MODE = PorterDuff.Mode.SRC_IN;
    INSTANCE_CACHE = new WeakHashMap<Context, TintManager>();
    COLOR_FILTER_CACHE = new ColorFilterLruCache(6);
    COLORFILTER_TINT_COLOR_CONTROL_NORMAL = new int[] { R.drawable.abc_textfield_search_default_mtrl_alpha, R.drawable.abc_textfield_default_mtrl_alpha, R.drawable.abc_ab_share_pack_mtrl_alpha };
    TINT_COLOR_CONTROL_NORMAL = new int[] { 
        R.drawable.abc_ic_ab_back_mtrl_am_alpha, R.drawable.abc_ic_go_search_api_mtrl_alpha, R.drawable.abc_ic_search_api_mtrl_alpha, R.drawable.abc_ic_commit_search_api_mtrl_alpha, R.drawable.abc_ic_clear_mtrl_alpha, R.drawable.abc_ic_menu_share_mtrl_alpha, R.drawable.abc_ic_menu_copy_mtrl_am_alpha, R.drawable.abc_ic_menu_cut_mtrl_alpha, R.drawable.abc_ic_menu_selectall_mtrl_alpha, R.drawable.abc_ic_menu_paste_mtrl_am_alpha, 
        R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha, R.drawable.abc_ic_voice_search_api_mtrl_alpha };
    COLORFILTER_COLOR_CONTROL_ACTIVATED = new int[] { R.drawable.abc_textfield_activated_mtrl_alpha, R.drawable.abc_textfield_search_activated_mtrl_alpha, R.drawable.abc_cab_background_top_mtrl_alpha, R.drawable.abc_text_cursor_material };
    COLORFILTER_COLOR_BACKGROUND_MULTIPLY = new int[] { R.drawable.abc_popup_background_mtrl_mult, R.drawable.abc_cab_background_internal_bg, R.drawable.abc_menu_hardkey_panel_mtrl_mult };
    TINT_COLOR_CONTROL_STATE_LIST = new int[] { R.drawable.abc_edit_text_material, R.drawable.abc_tab_indicator_material, R.drawable.abc_textfield_search_material, R.drawable.abc_spinner_mtrl_am_alpha, R.drawable.abc_spinner_textfield_background_material, R.drawable.abc_ratingbar_full_material, R.drawable.abc_switch_track_mtrl_alpha, R.drawable.abc_switch_thumb_material, R.drawable.abc_btn_default_mtrl_shape, R.drawable.abc_btn_borderless_material };
    TINT_CHECKABLE_BUTTON_LIST = new int[] { R.drawable.abc_btn_check_material, R.drawable.abc_btn_radio_material };
  }
  
  private TintManager(Context paramContext) {
    this.mContextRef = new WeakReference<Context>(paramContext);
  }
  
  private static boolean arrayContains(int[] paramArrayOfint, int paramInt) {
    int j = paramArrayOfint.length;
    for (int i = 0; i < j; i++) {
      if (paramArrayOfint[i] == paramInt)
        return true; 
    } 
    return false;
  }
  
  private ColorStateList createButtonColorStateList(Context paramContext, int paramInt) {
    int[][] arrayOfInt = new int[4][];
    int[] arrayOfInt1 = new int[4];
    paramInt = ThemeUtils.getThemeAttrColor(paramContext, paramInt);
    int i = ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlHighlight);
    arrayOfInt[0] = ThemeUtils.DISABLED_STATE_SET;
    arrayOfInt1[0] = ThemeUtils.getDisabledThemeAttrColor(paramContext, R.attr.colorButtonNormal);
    int j = 0 + 1;
    arrayOfInt[j] = ThemeUtils.PRESSED_STATE_SET;
    arrayOfInt1[j] = ColorUtils.compositeColors(i, paramInt);
    arrayOfInt[++j] = ThemeUtils.FOCUSED_STATE_SET;
    arrayOfInt1[j] = ColorUtils.compositeColors(i, paramInt);
    i = j + 1;
    arrayOfInt[i] = ThemeUtils.EMPTY_STATE_SET;
    arrayOfInt1[i] = paramInt;
    return new ColorStateList(arrayOfInt, arrayOfInt1);
  }
  
  private ColorStateList createCheckableButtonColorStateList(Context paramContext) {
    int[][] arrayOfInt = new int[3][];
    int[] arrayOfInt1 = new int[3];
    arrayOfInt[0] = ThemeUtils.DISABLED_STATE_SET;
    arrayOfInt1[0] = ThemeUtils.getDisabledThemeAttrColor(paramContext, R.attr.colorControlNormal);
    int i = 0 + 1;
    arrayOfInt[i] = ThemeUtils.CHECKED_STATE_SET;
    arrayOfInt1[i] = ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlActivated);
    arrayOfInt[++i] = ThemeUtils.EMPTY_STATE_SET;
    arrayOfInt1[i] = ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlNormal);
    return new ColorStateList(arrayOfInt, arrayOfInt1);
  }
  
  private ColorStateList createColoredButtonColorStateList(Context paramContext) {
    return createButtonColorStateList(paramContext, R.attr.colorAccent);
  }
  
  private ColorStateList createDefaultButtonColorStateList(Context paramContext) {
    return createButtonColorStateList(paramContext, R.attr.colorButtonNormal);
  }
  
  private ColorStateList createEditTextColorStateList(Context paramContext) {
    int[][] arrayOfInt = new int[3][];
    int[] arrayOfInt1 = new int[3];
    arrayOfInt[0] = ThemeUtils.DISABLED_STATE_SET;
    arrayOfInt1[0] = ThemeUtils.getDisabledThemeAttrColor(paramContext, R.attr.colorControlNormal);
    int i = 0 + 1;
    arrayOfInt[i] = ThemeUtils.NOT_PRESSED_OR_FOCUSED_STATE_SET;
    arrayOfInt1[i] = ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlNormal);
    arrayOfInt[++i] = ThemeUtils.EMPTY_STATE_SET;
    arrayOfInt1[i] = ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlActivated);
    return new ColorStateList(arrayOfInt, arrayOfInt1);
  }
  
  private ColorStateList createSeekbarThumbColorStateList(Context paramContext) {
    int[][] arrayOfInt = new int[2][];
    int[] arrayOfInt1 = new int[2];
    arrayOfInt[0] = ThemeUtils.DISABLED_STATE_SET;
    arrayOfInt1[0] = ThemeUtils.getDisabledThemeAttrColor(paramContext, R.attr.colorControlActivated);
    int i = 0 + 1;
    arrayOfInt[i] = ThemeUtils.EMPTY_STATE_SET;
    arrayOfInt1[i] = ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlActivated);
    return new ColorStateList(arrayOfInt, arrayOfInt1);
  }
  
  private ColorStateList createSpinnerColorStateList(Context paramContext) {
    int[][] arrayOfInt = new int[3][];
    int[] arrayOfInt1 = new int[3];
    arrayOfInt[0] = ThemeUtils.DISABLED_STATE_SET;
    arrayOfInt1[0] = ThemeUtils.getDisabledThemeAttrColor(paramContext, R.attr.colorControlNormal);
    int i = 0 + 1;
    arrayOfInt[i] = ThemeUtils.NOT_PRESSED_OR_FOCUSED_STATE_SET;
    arrayOfInt1[i] = ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlNormal);
    arrayOfInt[++i] = ThemeUtils.EMPTY_STATE_SET;
    arrayOfInt1[i] = ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlActivated);
    return new ColorStateList(arrayOfInt, arrayOfInt1);
  }
  
  private ColorStateList createSwitchThumbColorStateList(Context paramContext) {
    int[][] arrayOfInt = new int[3][];
    int[] arrayOfInt1 = new int[3];
    ColorStateList colorStateList = ThemeUtils.getThemeAttrColorStateList(paramContext, R.attr.colorSwitchThumbNormal);
    if (colorStateList != null && colorStateList.isStateful()) {
      arrayOfInt[0] = ThemeUtils.DISABLED_STATE_SET;
      arrayOfInt1[0] = colorStateList.getColorForState(arrayOfInt[0], 0);
      int j = 0 + 1;
      arrayOfInt[j] = ThemeUtils.CHECKED_STATE_SET;
      arrayOfInt1[j] = ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlActivated);
      arrayOfInt[++j] = ThemeUtils.EMPTY_STATE_SET;
      arrayOfInt1[j] = colorStateList.getDefaultColor();
      return new ColorStateList(arrayOfInt, arrayOfInt1);
    } 
    arrayOfInt[0] = ThemeUtils.DISABLED_STATE_SET;
    arrayOfInt1[0] = ThemeUtils.getDisabledThemeAttrColor(paramContext, R.attr.colorSwitchThumbNormal);
    int i = 0 + 1;
    arrayOfInt[i] = ThemeUtils.CHECKED_STATE_SET;
    arrayOfInt1[i] = ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlActivated);
    arrayOfInt[++i] = ThemeUtils.EMPTY_STATE_SET;
    arrayOfInt1[i] = ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorSwitchThumbNormal);
    return new ColorStateList(arrayOfInt, arrayOfInt1);
  }
  
  private ColorStateList createSwitchTrackColorStateList(Context paramContext) {
    int[][] arrayOfInt = new int[3][];
    int[] arrayOfInt1 = new int[3];
    arrayOfInt[0] = ThemeUtils.DISABLED_STATE_SET;
    arrayOfInt1[0] = ThemeUtils.getThemeAttrColor(paramContext, 16842800, 0.1F);
    int i = 0 + 1;
    arrayOfInt[i] = ThemeUtils.CHECKED_STATE_SET;
    arrayOfInt1[i] = ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlActivated, 0.3F);
    arrayOfInt[++i] = ThemeUtils.EMPTY_STATE_SET;
    arrayOfInt1[i] = ThemeUtils.getThemeAttrColor(paramContext, 16842800, 0.3F);
    return new ColorStateList(arrayOfInt, arrayOfInt1);
  }
  
  private static PorterDuffColorFilter createTintFilter(ColorStateList paramColorStateList, PorterDuff.Mode paramMode, int[] paramArrayOfint) {
    return (paramColorStateList == null || paramMode == null) ? null : getPorterDuffColorFilter(paramColorStateList.getColorForState(paramArrayOfint, 0), paramMode);
  }
  
  public static TintManager get(Context paramContext) {
    TintManager tintManager2 = INSTANCE_CACHE.get(paramContext);
    TintManager tintManager1 = tintManager2;
    if (tintManager2 == null) {
      tintManager1 = new TintManager(paramContext);
      INSTANCE_CACHE.put(paramContext, tintManager1);
    } 
    return tintManager1;
  }
  
  private ColorStateList getDefaultColorStateList(Context paramContext) {
    if (this.mDefaultColorStateList == null) {
      int i = ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlNormal);
      int j = ThemeUtils.getThemeAttrColor(paramContext, R.attr.colorControlActivated);
      int[][] arrayOfInt = new int[7][];
      int[] arrayOfInt1 = new int[7];
      arrayOfInt[0] = ThemeUtils.DISABLED_STATE_SET;
      arrayOfInt1[0] = ThemeUtils.getDisabledThemeAttrColor(paramContext, R.attr.colorControlNormal);
      int k = 0 + 1;
      arrayOfInt[k] = ThemeUtils.FOCUSED_STATE_SET;
      arrayOfInt1[k] = j;
      arrayOfInt[++k] = ThemeUtils.ACTIVATED_STATE_SET;
      arrayOfInt1[k] = j;
      arrayOfInt[++k] = ThemeUtils.PRESSED_STATE_SET;
      arrayOfInt1[k] = j;
      arrayOfInt[++k] = ThemeUtils.CHECKED_STATE_SET;
      arrayOfInt1[k] = j;
      arrayOfInt[++k] = ThemeUtils.SELECTED_STATE_SET;
      arrayOfInt1[k] = j;
      j = k + 1;
      arrayOfInt[j] = ThemeUtils.EMPTY_STATE_SET;
      arrayOfInt1[j] = i;
      this.mDefaultColorStateList = new ColorStateList(arrayOfInt, arrayOfInt1);
    } 
    return this.mDefaultColorStateList;
  }
  
  public static Drawable getDrawable(Context paramContext, int paramInt) {
    return isInTintList(paramInt) ? get(paramContext).getDrawable(paramInt) : ContextCompat.getDrawable(paramContext, paramInt);
  }
  
  private static PorterDuffColorFilter getPorterDuffColorFilter(int paramInt, PorterDuff.Mode paramMode) {
    PorterDuffColorFilter porterDuffColorFilter2 = COLOR_FILTER_CACHE.get(paramInt, paramMode);
    PorterDuffColorFilter porterDuffColorFilter1 = porterDuffColorFilter2;
    if (porterDuffColorFilter2 == null) {
      porterDuffColorFilter1 = new PorterDuffColorFilter(paramInt, paramMode);
      COLOR_FILTER_CACHE.put(paramInt, paramMode, porterDuffColorFilter1);
    } 
    return porterDuffColorFilter1;
  }
  
  private static boolean isInTintList(int paramInt) {
    return (arrayContains(TINT_COLOR_CONTROL_NORMAL, paramInt) || arrayContains(COLORFILTER_TINT_COLOR_CONTROL_NORMAL, paramInt) || arrayContains(COLORFILTER_COLOR_CONTROL_ACTIVATED, paramInt) || arrayContains(TINT_COLOR_CONTROL_STATE_LIST, paramInt) || arrayContains(COLORFILTER_COLOR_BACKGROUND_MULTIPLY, paramInt) || arrayContains(TINT_CHECKABLE_BUTTON_LIST, paramInt) || paramInt == R.drawable.abc_cab_background_top_material);
  }
  
  private static void setPorterDuffColorFilter(Drawable paramDrawable, int paramInt, PorterDuff.Mode paramMode) {
    PorterDuff.Mode mode = paramMode;
    if (paramMode == null)
      mode = DEFAULT_MODE; 
    paramDrawable.setColorFilter((ColorFilter)getPorterDuffColorFilter(paramInt, mode));
  }
  
  private static boolean shouldMutateBackground(Drawable paramDrawable) {
    if (Build.VERSION.SDK_INT < 16) {
      if (paramDrawable instanceof LayerDrawable)
        return !(Build.VERSION.SDK_INT < 16); 
      if (paramDrawable instanceof android.graphics.drawable.InsetDrawable)
        return !(Build.VERSION.SDK_INT < 14); 
      if (paramDrawable instanceof DrawableContainer) {
        Drawable.ConstantState constantState = paramDrawable.getConstantState();
        if (constantState instanceof DrawableContainer.DrawableContainerState) {
          Drawable[] arrayOfDrawable = ((DrawableContainer.DrawableContainerState)constantState).getChildren();
          int j = arrayOfDrawable.length;
          int i = 0;
          while (true) {
            if (i < j) {
              if (!shouldMutateBackground(arrayOfDrawable[i]))
                return false; 
              i++;
              continue;
            } 
            return true;
          } 
        } 
      } 
    } 
    return true;
  }
  
  public static void tintDrawable(Drawable paramDrawable, TintInfo paramTintInfo, int[] paramArrayOfint) {
    if (shouldMutateBackground(paramDrawable) && paramDrawable.mutate() != paramDrawable) {
      Log.d("TintManager", "Mutated drawable is not the same instance as the input.");
      return;
    } 
    if (paramTintInfo.mHasTintList || paramTintInfo.mHasTintMode) {
      PorterDuff.Mode mode;
      ColorStateList colorStateList;
      if (paramTintInfo.mHasTintList) {
        colorStateList = paramTintInfo.mTintList;
      } else {
        colorStateList = null;
      } 
      if (paramTintInfo.mHasTintMode) {
        mode = paramTintInfo.mTintMode;
      } else {
        mode = DEFAULT_MODE;
      } 
      paramDrawable.setColorFilter((ColorFilter)createTintFilter(colorStateList, mode, paramArrayOfint));
    } else {
      paramDrawable.clearColorFilter();
    } 
    if (Build.VERSION.SDK_INT <= 10) {
      paramDrawable.invalidateSelf();
      return;
    } 
  }
  
  public Drawable getDrawable(int paramInt) {
    return getDrawable(paramInt, false);
  }
  
  public Drawable getDrawable(int paramInt, boolean paramBoolean) {
    Context context = this.mContextRef.get();
    if (context == null)
      return null; 
    Drawable drawable2 = ContextCompat.getDrawable(context, paramInt);
    Drawable drawable1 = drawable2;
    if (drawable2 != null) {
      Drawable drawable = drawable2;
      if (Build.VERSION.SDK_INT >= 8)
        drawable = drawable2.mutate(); 
      ColorStateList colorStateList = getTintList(paramInt);
      if (colorStateList != null) {
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(drawable, colorStateList);
        PorterDuff.Mode mode = getTintMode(paramInt);
        Drawable drawable3 = drawable;
        if (mode != null) {
          DrawableCompat.setTintMode(drawable, mode);
          return drawable;
        } 
        return drawable3;
      } 
      if (paramInt == R.drawable.abc_cab_background_top_material)
        return (Drawable)new LayerDrawable(new Drawable[] { getDrawable(R.drawable.abc_cab_background_internal_bg), getDrawable(R.drawable.abc_cab_background_top_mtrl_alpha) }); 
      if (paramInt == R.drawable.abc_seekbar_track_material) {
        LayerDrawable layerDrawable = (LayerDrawable)drawable;
        setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908288), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), DEFAULT_MODE);
        setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908303), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlNormal), DEFAULT_MODE);
        setPorterDuffColorFilter(layerDrawable.findDrawableByLayerId(16908301), ThemeUtils.getThemeAttrColor(context, R.attr.colorControlActivated), DEFAULT_MODE);
        return drawable;
      } 
      drawable1 = drawable;
      if (!tintDrawableUsingColorFilter(paramInt, drawable)) {
        drawable1 = drawable;
        if (paramBoolean)
          return null; 
      } 
    } 
    return drawable1;
  }
  
  public final ColorStateList getTintList(int paramInt) {
    ColorStateList colorStateList1 = null;
    ColorStateList colorStateList2 = null;
    Context context = this.mContextRef.get();
    if (context != null) {
      if (this.mTintLists != null)
        colorStateList1 = (ColorStateList)this.mTintLists.get(paramInt); 
      colorStateList2 = colorStateList1;
      if (colorStateList1 == null) {
        if (paramInt == R.drawable.abc_edit_text_material) {
          colorStateList1 = createEditTextColorStateList(context);
        } else if (paramInt == R.drawable.abc_switch_track_mtrl_alpha) {
          colorStateList1 = createSwitchTrackColorStateList(context);
        } else if (paramInt == R.drawable.abc_switch_thumb_material) {
          colorStateList1 = createSwitchThumbColorStateList(context);
        } else if (paramInt == R.drawable.abc_btn_default_mtrl_shape || paramInt == R.drawable.abc_btn_borderless_material) {
          colorStateList1 = createDefaultButtonColorStateList(context);
        } else if (paramInt == R.drawable.abc_btn_colored_material) {
          colorStateList1 = createColoredButtonColorStateList(context);
        } else if (paramInt == R.drawable.abc_spinner_mtrl_am_alpha || paramInt == R.drawable.abc_spinner_textfield_background_material) {
          colorStateList1 = createSpinnerColorStateList(context);
        } else if (arrayContains(TINT_COLOR_CONTROL_NORMAL, paramInt)) {
          colorStateList1 = ThemeUtils.getThemeAttrColorStateList(context, R.attr.colorControlNormal);
        } else if (arrayContains(TINT_COLOR_CONTROL_STATE_LIST, paramInt)) {
          colorStateList1 = getDefaultColorStateList(context);
        } else if (arrayContains(TINT_CHECKABLE_BUTTON_LIST, paramInt)) {
          colorStateList1 = createCheckableButtonColorStateList(context);
        } else if (paramInt == R.drawable.abc_seekbar_thumb_material) {
          colorStateList1 = createSeekbarThumbColorStateList(context);
        } 
        colorStateList2 = colorStateList1;
        if (colorStateList1 != null) {
          if (this.mTintLists == null)
            this.mTintLists = new SparseArray(); 
          this.mTintLists.append(paramInt, colorStateList1);
          return colorStateList1;
        } 
      } 
    } 
    return colorStateList2;
  }
  
  final PorterDuff.Mode getTintMode(int paramInt) {
    PorterDuff.Mode mode = null;
    if (paramInt == R.drawable.abc_switch_thumb_material)
      mode = PorterDuff.Mode.MULTIPLY; 
    return mode;
  }
  
  public final boolean tintDrawableUsingColorFilter(int paramInt, Drawable paramDrawable) {
    Context context = this.mContextRef.get();
    if (context != null) {
      PorterDuff.Mode mode1;
      PorterDuff.Mode mode2 = DEFAULT_MODE;
      boolean bool = false;
      int i = 0;
      int j = -1;
      if (arrayContains(COLORFILTER_TINT_COLOR_CONTROL_NORMAL, paramInt)) {
        i = R.attr.colorControlNormal;
        bool = true;
        mode1 = mode2;
      } else if (arrayContains(COLORFILTER_COLOR_CONTROL_ACTIVATED, paramInt)) {
        i = R.attr.colorControlActivated;
        bool = true;
        mode1 = mode2;
      } else if (arrayContains(COLORFILTER_COLOR_BACKGROUND_MULTIPLY, paramInt)) {
        i = 16842801;
        bool = true;
        mode1 = PorterDuff.Mode.MULTIPLY;
      } else {
        mode1 = mode2;
        if (paramInt == R.drawable.abc_list_divider_mtrl_alpha) {
          i = 16842800;
          bool = true;
          j = Math.round(40.8F);
          mode1 = mode2;
        } 
      } 
      if (bool) {
        paramDrawable.setColorFilter((ColorFilter)getPorterDuffColorFilter(ThemeUtils.getThemeAttrColor(context, i), mode1));
        if (j != -1)
          paramDrawable.setAlpha(j); 
        return true;
      } 
    } 
    return false;
  }
  
  private static class ColorFilterLruCache extends LruCache<Integer, PorterDuffColorFilter> {
    public ColorFilterLruCache(int param1Int) {
      super(param1Int);
    }
    
    private static int generateCacheKey(int param1Int, PorterDuff.Mode param1Mode) {
      return (param1Int + 31) * 31 + param1Mode.hashCode();
    }
    
    PorterDuffColorFilter get(int param1Int, PorterDuff.Mode param1Mode) {
      return (PorterDuffColorFilter)get(Integer.valueOf(generateCacheKey(param1Int, param1Mode)));
    }
    
    PorterDuffColorFilter put(int param1Int, PorterDuff.Mode param1Mode, PorterDuffColorFilter param1PorterDuffColorFilter) {
      return (PorterDuffColorFilter)put(Integer.valueOf(generateCacheKey(param1Int, param1Mode)), param1PorterDuffColorFilter);
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\widget\TintManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */