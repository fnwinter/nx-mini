package android.support.v4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

class CircleImageView extends ImageView {
  private static final int FILL_SHADOW_COLOR = 1023410176;
  
  private static final int KEY_SHADOW_COLOR = 503316480;
  
  private static final int SHADOW_ELEVATION = 4;
  
  private static final float SHADOW_RADIUS = 3.5F;
  
  private static final float X_OFFSET = 0.0F;
  
  private static final float Y_OFFSET = 1.75F;
  
  private Animation.AnimationListener mListener;
  
  private int mShadowRadius;
  
  public CircleImageView(Context paramContext, int paramInt, float paramFloat) {
    super(paramContext);
    ShapeDrawable shapeDrawable;
    float f = (getContext().getResources().getDisplayMetrics()).density;
    int i = (int)(paramFloat * f * 2.0F);
    int j = (int)(1.75F * f);
    int k = (int)(0.0F * f);
    this.mShadowRadius = (int)(3.5F * f);
    if (elevationSupported()) {
      shapeDrawable = new ShapeDrawable((Shape)new OvalShape());
      ViewCompat.setElevation((View)this, 4.0F * f);
    } else {
      shapeDrawable = new ShapeDrawable((Shape)new OvalShadow(this.mShadowRadius, i));
      ViewCompat.setLayerType((View)this, 1, shapeDrawable.getPaint());
      shapeDrawable.getPaint().setShadowLayer(this.mShadowRadius, k, j, 503316480);
      i = this.mShadowRadius;
      setPadding(i, i, i, i);
    } 
    shapeDrawable.getPaint().setColor(paramInt);
    setBackgroundDrawable((Drawable)shapeDrawable);
  }
  
  private boolean elevationSupported() {
    return (Build.VERSION.SDK_INT >= 21);
  }
  
  public void onAnimationEnd() {
    super.onAnimationEnd();
    if (this.mListener != null)
      this.mListener.onAnimationEnd(getAnimation()); 
  }
  
  public void onAnimationStart() {
    super.onAnimationStart();
    if (this.mListener != null)
      this.mListener.onAnimationStart(getAnimation()); 
  }
  
  protected void onMeasure(int paramInt1, int paramInt2) {
    super.onMeasure(paramInt1, paramInt2);
    if (!elevationSupported())
      setMeasuredDimension(getMeasuredWidth() + this.mShadowRadius * 2, getMeasuredHeight() + this.mShadowRadius * 2); 
  }
  
  public void setAnimationListener(Animation.AnimationListener paramAnimationListener) {
    this.mListener = paramAnimationListener;
  }
  
  public void setBackgroundColor(int paramInt) {
    if (getBackground() instanceof ShapeDrawable)
      ((ShapeDrawable)getBackground()).getPaint().setColor(paramInt); 
  }
  
  public void setBackgroundColorRes(int paramInt) {
    setBackgroundColor(getContext().getResources().getColor(paramInt));
  }
  
  private class OvalShadow extends OvalShape {
    private int mCircleDiameter;
    
    private RadialGradient mRadialGradient;
    
    private Paint mShadowPaint = new Paint();
    
    public OvalShadow(int param1Int1, int param1Int2) {
      CircleImageView.access$002(CircleImageView.this, param1Int1);
      this.mCircleDiameter = param1Int2;
      float f1 = (this.mCircleDiameter / 2);
      float f2 = (this.mCircleDiameter / 2);
      float f3 = CircleImageView.this.mShadowRadius;
      Shader.TileMode tileMode = Shader.TileMode.CLAMP;
      this.mRadialGradient = new RadialGradient(f1, f2, f3, new int[] { 1023410176, 0 }, null, tileMode);
      this.mShadowPaint.setShader((Shader)this.mRadialGradient);
    }
    
    public void draw(Canvas param1Canvas, Paint param1Paint) {
      int i = CircleImageView.this.getWidth();
      int j = CircleImageView.this.getHeight();
      param1Canvas.drawCircle((i / 2), (j / 2), (this.mCircleDiameter / 2 + CircleImageView.this.mShadowRadius), this.mShadowPaint);
      param1Canvas.drawCircle((i / 2), (j / 2), (this.mCircleDiameter / 2), param1Paint);
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\widget\CircleImageView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */