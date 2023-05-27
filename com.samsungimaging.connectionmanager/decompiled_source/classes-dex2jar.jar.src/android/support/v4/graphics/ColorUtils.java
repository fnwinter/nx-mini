package android.support.v4.graphics;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

public class ColorUtils {
  private static final int MIN_ALPHA_SEARCH_MAX_ITERATIONS = 10;
  
  private static final int MIN_ALPHA_SEARCH_PRECISION = 1;
  
  @ColorInt
  public static int HSLToColor(@NonNull float[] paramArrayOffloat) {
    float f1 = paramArrayOffloat[0];
    float f2 = paramArrayOffloat[1];
    float f3 = paramArrayOffloat[2];
    f2 = (1.0F - Math.abs(2.0F * f3 - 1.0F)) * f2;
    f3 -= 0.5F * f2;
    float f4 = f2 * (1.0F - Math.abs(f1 / 60.0F % 2.0F - 1.0F));
    int m = (int)f1 / 60;
    int k = 0;
    int j = 0;
    int i = 0;
    switch (m) {
      default:
        return Color.rgb(constrain(k, 0, 255), constrain(j, 0, 255), constrain(i, 0, 255));
      case 0:
        k = Math.round(255.0F * (f2 + f3));
        j = Math.round(255.0F * (f4 + f3));
        i = Math.round(255.0F * f3);
      case 1:
        k = Math.round(255.0F * (f4 + f3));
        j = Math.round(255.0F * (f2 + f3));
        i = Math.round(255.0F * f3);
      case 2:
        k = Math.round(255.0F * f3);
        j = Math.round(255.0F * (f2 + f3));
        i = Math.round(255.0F * (f4 + f3));
      case 3:
        k = Math.round(255.0F * f3);
        j = Math.round(255.0F * (f4 + f3));
        i = Math.round(255.0F * (f2 + f3));
      case 4:
        k = Math.round(255.0F * (f4 + f3));
        j = Math.round(255.0F * f3);
        i = Math.round(255.0F * (f2 + f3));
      case 5:
      case 6:
        break;
    } 
    k = Math.round(255.0F * (f2 + f3));
    j = Math.round(255.0F * f3);
    i = Math.round(255.0F * (f4 + f3));
  }
  
  public static void RGBToHSL(@IntRange(from = 0L, to = 255L) int paramInt1, @IntRange(from = 0L, to = 255L) int paramInt2, @IntRange(from = 0L, to = 255L) int paramInt3, @NonNull float[] paramArrayOffloat) {
    float f1 = paramInt1 / 255.0F;
    float f3 = paramInt2 / 255.0F;
    float f5 = paramInt3 / 255.0F;
    float f6 = Math.max(f1, Math.max(f3, f5));
    float f7 = Math.min(f1, Math.min(f3, f5));
    float f2 = f6 - f7;
    float f4 = (f6 + f7) / 2.0F;
    if (f6 == f7) {
      f1 = 0.0F;
      f2 = 0.0F;
    } else {
      if (f6 == f1) {
        f1 = (f3 - f5) / f2 % 6.0F;
      } else if (f6 == f3) {
        f1 = (f5 - f1) / f2 + 2.0F;
      } else {
        f1 = (f1 - f3) / f2 + 4.0F;
      } 
      f3 = f2 / (1.0F - Math.abs(2.0F * f4 - 1.0F));
      f2 = f1;
      f1 = f3;
    } 
    f3 = 60.0F * f2 % 360.0F;
    f2 = f3;
    if (f3 < 0.0F)
      f2 = f3 + 360.0F; 
    paramArrayOffloat[0] = constrain(f2, 0.0F, 360.0F);
    paramArrayOffloat[1] = constrain(f1, 0.0F, 1.0F);
    paramArrayOffloat[2] = constrain(f4, 0.0F, 1.0F);
  }
  
  public static double calculateContrast(@ColorInt int paramInt1, @ColorInt int paramInt2) {
    if (Color.alpha(paramInt2) != 255)
      throw new IllegalArgumentException("background can not be translucent: #" + Integer.toHexString(paramInt2)); 
    int i = paramInt1;
    if (Color.alpha(paramInt1) < 255)
      i = compositeColors(paramInt1, paramInt2); 
    double d1 = calculateLuminance(i) + 0.05D;
    double d2 = calculateLuminance(paramInt2) + 0.05D;
    return Math.max(d1, d2) / Math.min(d1, d2);
  }
  
  @FloatRange(from = 0.0D, to = 1.0D)
  public static double calculateLuminance(@ColorInt int paramInt) {
    double d1 = Color.red(paramInt) / 255.0D;
    if (d1 < 0.03928D) {
      d1 /= 12.92D;
    } else {
      d1 = Math.pow((0.055D + d1) / 1.055D, 2.4D);
    } 
    double d2 = Color.green(paramInt) / 255.0D;
    if (d2 < 0.03928D) {
      d2 /= 12.92D;
    } else {
      d2 = Math.pow((0.055D + d2) / 1.055D, 2.4D);
    } 
    double d3 = Color.blue(paramInt) / 255.0D;
    if (d3 < 0.03928D) {
      d3 /= 12.92D;
      return 0.2126D * d1 + 0.7152D * d2 + 0.0722D * d3;
    } 
    d3 = Math.pow((0.055D + d3) / 1.055D, 2.4D);
    return 0.2126D * d1 + 0.7152D * d2 + 0.0722D * d3;
  }
  
  public static int calculateMinimumAlpha(@ColorInt int paramInt1, @ColorInt int paramInt2, float paramFloat) {
    if (Color.alpha(paramInt2) != 255)
      throw new IllegalArgumentException("background can not be translucent: #" + Integer.toHexString(paramInt2)); 
    if (calculateContrast(setAlphaComponent(paramInt1, 255), paramInt2) < paramFloat)
      return -1; 
    int j = 0;
    int k = 0;
    int i = 255;
    while (true) {
      int m = i;
      if (j <= 10) {
        m = i;
        if (i - k > 1) {
          m = (k + i) / 2;
          if (calculateContrast(setAlphaComponent(paramInt1, m), paramInt2) < paramFloat) {
            k = m;
          } else {
            i = m;
          } 
          j++;
          continue;
        } 
      } 
      return m;
    } 
  }
  
  public static void colorToHSL(@ColorInt int paramInt, @NonNull float[] paramArrayOffloat) {
    RGBToHSL(Color.red(paramInt), Color.green(paramInt), Color.blue(paramInt), paramArrayOffloat);
  }
  
  private static int compositeAlpha(int paramInt1, int paramInt2) {
    return 255 - (255 - paramInt2) * (255 - paramInt1) / 255;
  }
  
  public static int compositeColors(@ColorInt int paramInt1, @ColorInt int paramInt2) {
    int i = Color.alpha(paramInt2);
    int j = Color.alpha(paramInt1);
    int k = compositeAlpha(j, i);
    return Color.argb(k, compositeComponent(Color.red(paramInt1), j, Color.red(paramInt2), i, k), compositeComponent(Color.green(paramInt1), j, Color.green(paramInt2), i, k), compositeComponent(Color.blue(paramInt1), j, Color.blue(paramInt2), i, k));
  }
  
  private static int compositeComponent(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
    return (paramInt5 == 0) ? 0 : ((paramInt1 * 255 * paramInt2 + paramInt3 * paramInt4 * (255 - paramInt2)) / paramInt5 * 255);
  }
  
  private static float constrain(float paramFloat1, float paramFloat2, float paramFloat3) {
    return (paramFloat1 < paramFloat2) ? paramFloat2 : ((paramFloat1 > paramFloat3) ? paramFloat3 : paramFloat1);
  }
  
  private static int constrain(int paramInt1, int paramInt2, int paramInt3) {
    return (paramInt1 < paramInt2) ? paramInt2 : ((paramInt1 > paramInt3) ? paramInt3 : paramInt1);
  }
  
  @ColorInt
  public static int setAlphaComponent(@ColorInt int paramInt1, @IntRange(from = 0L, to = 255L) int paramInt2) {
    if (paramInt2 < 0 || paramInt2 > 255)
      throw new IllegalArgumentException("alpha must be between 0 and 255."); 
    return 0xFFFFFF & paramInt1 | paramInt2 << 24;
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\graphics\ColorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */