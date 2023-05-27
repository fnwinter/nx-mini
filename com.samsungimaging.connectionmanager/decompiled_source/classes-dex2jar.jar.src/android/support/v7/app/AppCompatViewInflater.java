package android.support.v7.app;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.ViewCompat;
import android.support.v7.appcompat.R;
import android.support.v7.view.ContextThemeWrapper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

class AppCompatViewInflater {
  private static final String LOG_TAG = "AppCompatViewInflater";
  
  private static final Map<String, Constructor<? extends View>> sConstructorMap;
  
  private static final Class<?>[] sConstructorSignature = new Class[] { Context.class, AttributeSet.class };
  
  private static final int[] sOnClickAttrs = new int[] { 16843375 };
  
  private final Object[] mConstructorArgs = new Object[2];
  
  static {
    sConstructorMap = (Map<String, Constructor<? extends View>>)new ArrayMap();
  }
  
  private void checkOnClickListener(View paramView, AttributeSet paramAttributeSet) {
    Context context = paramView.getContext();
    if (!ViewCompat.hasOnClickListeners(paramView) || !(context instanceof ContextWrapper))
      return; 
    TypedArray typedArray = context.obtainStyledAttributes(paramAttributeSet, sOnClickAttrs);
    String str = typedArray.getString(0);
    if (str != null)
      paramView.setOnClickListener(new DeclaredOnClickListener(paramView, str)); 
    typedArray.recycle();
  }
  
  private View createView(Context paramContext, String paramString1, String paramString2) throws ClassNotFoundException, InflateException {
    Constructor constructor1 = sConstructorMap.get(paramString1);
    Constructor<? extends View> constructor = constructor1;
    if (constructor1 == null)
      try {
        String str;
        ClassLoader classLoader = paramContext.getClassLoader();
        if (paramString2 != null) {
          str = paramString2 + paramString1;
        } else {
          str = paramString1;
        } 
        constructor = classLoader.loadClass(str).<View>asSubclass(View.class).getConstructor(sConstructorSignature);
        sConstructorMap.put(paramString1, constructor);
        constructor.setAccessible(true);
        return constructor.newInstance(this.mConstructorArgs);
      } catch (Exception exception) {
        return null;
      }  
    constructor.setAccessible(true);
    return constructor.newInstance(this.mConstructorArgs);
  }
  
  private View createViewFromTag(Context paramContext, String paramString, AttributeSet paramAttributeSet) {
    String str = paramString;
    if (paramString.equals("view"))
      str = paramAttributeSet.getAttributeValue(null, "class"); 
    try {
      this.mConstructorArgs[0] = paramContext;
      this.mConstructorArgs[1] = paramAttributeSet;
      if (-1 == str.indexOf('.')) {
        view = createView(paramContext, str, "android.widget.");
        return view;
      } 
      View view = createView((Context)view, str, null);
      return view;
    } catch (Exception exception) {
      return null;
    } finally {
      this.mConstructorArgs[0] = null;
      this.mConstructorArgs[1] = null;
    } 
  }
  
  private static Context themifyContext(Context paramContext, AttributeSet paramAttributeSet, boolean paramBoolean1, boolean paramBoolean2) {
    TypedArray typedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.View, 0, 0);
    int i = 0;
    if (paramBoolean1)
      i = typedArray.getResourceId(R.styleable.View_android_theme, 0); 
    int j = i;
    if (paramBoolean2) {
      j = i;
      if (i == 0) {
        i = typedArray.getResourceId(R.styleable.View_theme, 0);
        j = i;
        if (i != 0) {
          Log.i("AppCompatViewInflater", "app:theme is now deprecated. Please move to using android:theme instead.");
          j = i;
        } 
      } 
    } 
    typedArray.recycle();
    Context context = paramContext;
    if (j != 0) {
      if (paramContext instanceof ContextThemeWrapper) {
        context = paramContext;
        return (Context)((((ContextThemeWrapper)paramContext).getThemeResId() != j) ? new ContextThemeWrapper(paramContext, j) : context);
      } 
    } else {
      return context;
    } 
    return (Context)new ContextThemeWrapper(paramContext, j);
  }
  
  public final View createView(View paramView, String paramString, @NonNull Context paramContext, @NonNull AttributeSet paramAttributeSet, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3) {
    // Byte code:
    //   0: aload_3
    //   1: astore #9
    //   3: iload #5
    //   5: ifeq -> 21
    //   8: aload_3
    //   9: astore #9
    //   11: aload_1
    //   12: ifnull -> 21
    //   15: aload_1
    //   16: invokevirtual getContext : ()Landroid/content/Context;
    //   19: astore #9
    //   21: aload #9
    //   23: astore_1
    //   24: iload #6
    //   26: ifne -> 37
    //   29: aload_1
    //   30: astore #9
    //   32: iload #7
    //   34: ifeq -> 49
    //   37: aload_1
    //   38: aload #4
    //   40: iload #6
    //   42: iload #7
    //   44: invokestatic themifyContext : (Landroid/content/Context;Landroid/util/AttributeSet;ZZ)Landroid/content/Context;
    //   47: astore #9
    //   49: aconst_null
    //   50: astore_1
    //   51: iconst_m1
    //   52: istore #8
    //   54: aload_2
    //   55: invokevirtual hashCode : ()I
    //   58: lookupswitch default -> 172, -1946472170 -> 453, -1455429095 -> 405, -1346021293 -> 437, -938935918 -> 283, -937446323 -> 358, -658531749 -> 469, -339785223 -> 343, 776382189 -> 389, 1125864064 -> 298, 1413872058 -> 421, 1601505219 -> 373, 1666676343 -> 328, 2001146706 -> 313
    //   172: iload #8
    //   174: tableswitch default -> 240, 0 -> 485, 1 -> 500, 2 -> 515, 3 -> 530, 4 -> 545, 5 -> 560, 6 -> 575, 7 -> 590, 8 -> 605, 9 -> 620, 10 -> 635, 11 -> 650, 12 -> 665
    //   240: aload_1
    //   241: astore #10
    //   243: aload_1
    //   244: ifnonnull -> 267
    //   247: aload_1
    //   248: astore #10
    //   250: aload_3
    //   251: aload #9
    //   253: if_acmpeq -> 267
    //   256: aload_0
    //   257: aload #9
    //   259: aload_2
    //   260: aload #4
    //   262: invokespecial createViewFromTag : (Landroid/content/Context;Ljava/lang/String;Landroid/util/AttributeSet;)Landroid/view/View;
    //   265: astore #10
    //   267: aload #10
    //   269: ifnull -> 280
    //   272: aload_0
    //   273: aload #10
    //   275: aload #4
    //   277: invokespecial checkOnClickListener : (Landroid/view/View;Landroid/util/AttributeSet;)V
    //   280: aload #10
    //   282: areturn
    //   283: aload_2
    //   284: ldc 'TextView'
    //   286: invokevirtual equals : (Ljava/lang/Object;)Z
    //   289: ifeq -> 172
    //   292: iconst_0
    //   293: istore #8
    //   295: goto -> 172
    //   298: aload_2
    //   299: ldc 'ImageView'
    //   301: invokevirtual equals : (Ljava/lang/Object;)Z
    //   304: ifeq -> 172
    //   307: iconst_1
    //   308: istore #8
    //   310: goto -> 172
    //   313: aload_2
    //   314: ldc 'Button'
    //   316: invokevirtual equals : (Ljava/lang/Object;)Z
    //   319: ifeq -> 172
    //   322: iconst_2
    //   323: istore #8
    //   325: goto -> 172
    //   328: aload_2
    //   329: ldc 'EditText'
    //   331: invokevirtual equals : (Ljava/lang/Object;)Z
    //   334: ifeq -> 172
    //   337: iconst_3
    //   338: istore #8
    //   340: goto -> 172
    //   343: aload_2
    //   344: ldc 'Spinner'
    //   346: invokevirtual equals : (Ljava/lang/Object;)Z
    //   349: ifeq -> 172
    //   352: iconst_4
    //   353: istore #8
    //   355: goto -> 172
    //   358: aload_2
    //   359: ldc 'ImageButton'
    //   361: invokevirtual equals : (Ljava/lang/Object;)Z
    //   364: ifeq -> 172
    //   367: iconst_5
    //   368: istore #8
    //   370: goto -> 172
    //   373: aload_2
    //   374: ldc 'CheckBox'
    //   376: invokevirtual equals : (Ljava/lang/Object;)Z
    //   379: ifeq -> 172
    //   382: bipush #6
    //   384: istore #8
    //   386: goto -> 172
    //   389: aload_2
    //   390: ldc 'RadioButton'
    //   392: invokevirtual equals : (Ljava/lang/Object;)Z
    //   395: ifeq -> 172
    //   398: bipush #7
    //   400: istore #8
    //   402: goto -> 172
    //   405: aload_2
    //   406: ldc 'CheckedTextView'
    //   408: invokevirtual equals : (Ljava/lang/Object;)Z
    //   411: ifeq -> 172
    //   414: bipush #8
    //   416: istore #8
    //   418: goto -> 172
    //   421: aload_2
    //   422: ldc 'AutoCompleteTextView'
    //   424: invokevirtual equals : (Ljava/lang/Object;)Z
    //   427: ifeq -> 172
    //   430: bipush #9
    //   432: istore #8
    //   434: goto -> 172
    //   437: aload_2
    //   438: ldc 'MultiAutoCompleteTextView'
    //   440: invokevirtual equals : (Ljava/lang/Object;)Z
    //   443: ifeq -> 172
    //   446: bipush #10
    //   448: istore #8
    //   450: goto -> 172
    //   453: aload_2
    //   454: ldc 'RatingBar'
    //   456: invokevirtual equals : (Ljava/lang/Object;)Z
    //   459: ifeq -> 172
    //   462: bipush #11
    //   464: istore #8
    //   466: goto -> 172
    //   469: aload_2
    //   470: ldc 'SeekBar'
    //   472: invokevirtual equals : (Ljava/lang/Object;)Z
    //   475: ifeq -> 172
    //   478: bipush #12
    //   480: istore #8
    //   482: goto -> 172
    //   485: new android/support/v7/widget/AppCompatTextView
    //   488: dup
    //   489: aload #9
    //   491: aload #4
    //   493: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;)V
    //   496: astore_1
    //   497: goto -> 240
    //   500: new android/support/v7/widget/AppCompatImageView
    //   503: dup
    //   504: aload #9
    //   506: aload #4
    //   508: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;)V
    //   511: astore_1
    //   512: goto -> 240
    //   515: new android/support/v7/widget/AppCompatButton
    //   518: dup
    //   519: aload #9
    //   521: aload #4
    //   523: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;)V
    //   526: astore_1
    //   527: goto -> 240
    //   530: new android/support/v7/widget/AppCompatEditText
    //   533: dup
    //   534: aload #9
    //   536: aload #4
    //   538: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;)V
    //   541: astore_1
    //   542: goto -> 240
    //   545: new android/support/v7/widget/AppCompatSpinner
    //   548: dup
    //   549: aload #9
    //   551: aload #4
    //   553: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;)V
    //   556: astore_1
    //   557: goto -> 240
    //   560: new android/support/v7/widget/AppCompatImageButton
    //   563: dup
    //   564: aload #9
    //   566: aload #4
    //   568: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;)V
    //   571: astore_1
    //   572: goto -> 240
    //   575: new android/support/v7/widget/AppCompatCheckBox
    //   578: dup
    //   579: aload #9
    //   581: aload #4
    //   583: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;)V
    //   586: astore_1
    //   587: goto -> 240
    //   590: new android/support/v7/widget/AppCompatRadioButton
    //   593: dup
    //   594: aload #9
    //   596: aload #4
    //   598: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;)V
    //   601: astore_1
    //   602: goto -> 240
    //   605: new android/support/v7/widget/AppCompatCheckedTextView
    //   608: dup
    //   609: aload #9
    //   611: aload #4
    //   613: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;)V
    //   616: astore_1
    //   617: goto -> 240
    //   620: new android/support/v7/widget/AppCompatAutoCompleteTextView
    //   623: dup
    //   624: aload #9
    //   626: aload #4
    //   628: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;)V
    //   631: astore_1
    //   632: goto -> 240
    //   635: new android/support/v7/widget/AppCompatMultiAutoCompleteTextView
    //   638: dup
    //   639: aload #9
    //   641: aload #4
    //   643: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;)V
    //   646: astore_1
    //   647: goto -> 240
    //   650: new android/support/v7/widget/AppCompatRatingBar
    //   653: dup
    //   654: aload #9
    //   656: aload #4
    //   658: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;)V
    //   661: astore_1
    //   662: goto -> 240
    //   665: new android/support/v7/widget/AppCompatSeekBar
    //   668: dup
    //   669: aload #9
    //   671: aload #4
    //   673: invokespecial <init> : (Landroid/content/Context;Landroid/util/AttributeSet;)V
    //   676: astore_1
    //   677: goto -> 240
  }
  
  private static class DeclaredOnClickListener implements View.OnClickListener {
    private final View mHostView;
    
    private final String mMethodName;
    
    private Context mResolvedContext;
    
    private Method mResolvedMethod;
    
    public DeclaredOnClickListener(@NonNull View param1View, @NonNull String param1String) {
      this.mHostView = param1View;
      this.mMethodName = param1String;
    }
    
    @NonNull
    private void resolveMethod(@Nullable Context param1Context, @NonNull String param1String) {
      while (param1Context != null) {
        try {
          if (!param1Context.isRestricted()) {
            Method method = param1Context.getClass().getMethod(this.mMethodName, new Class[] { View.class });
            if (method != null) {
              this.mResolvedMethod = method;
              this.mResolvedContext = param1Context;
              return;
            } 
          } 
        } catch (NoSuchMethodException noSuchMethodException) {}
        if (param1Context instanceof ContextWrapper) {
          param1Context = ((ContextWrapper)param1Context).getBaseContext();
          continue;
        } 
        param1Context = null;
      } 
      int i = this.mHostView.getId();
      if (i == -1) {
        String str1 = "";
        throw new IllegalStateException("Could not find method " + this.mMethodName + "(View) in a parent or ancestor Context for android:onClick " + "attribute defined on view " + this.mHostView.getClass() + str1);
      } 
      String str = " with id '" + this.mHostView.getContext().getResources().getResourceEntryName(i) + "'";
      throw new IllegalStateException("Could not find method " + this.mMethodName + "(View) in a parent or ancestor Context for android:onClick " + "attribute defined on view " + this.mHostView.getClass() + str);
    }
    
    public void onClick(@NonNull View param1View) {
      if (this.mResolvedMethod == null)
        resolveMethod(this.mHostView.getContext(), this.mMethodName); 
      try {
        this.mResolvedMethod.invoke(this.mResolvedContext, new Object[] { param1View });
        return;
      } catch (IllegalAccessException illegalAccessException) {
        throw new IllegalStateException("Could not execute non-public method for android:onClick", illegalAccessException);
      } catch (InvocationTargetException invocationTargetException) {
        throw new IllegalStateException("Could not execute method for android:onClick", invocationTargetException);
      } 
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v7\app\AppCompatViewInflater.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */