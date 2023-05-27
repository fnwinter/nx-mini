package android.support.v4.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

class PrintHelperKitkat {
  public static final int COLOR_MODE_COLOR = 2;
  
  public static final int COLOR_MODE_MONOCHROME = 1;
  
  private static final String LOG_TAG = "PrintHelperKitkat";
  
  private static final int MAX_PRINT_SIZE = 3500;
  
  public static final int ORIENTATION_LANDSCAPE = 1;
  
  public static final int ORIENTATION_PORTRAIT = 2;
  
  public static final int SCALE_MODE_FILL = 2;
  
  public static final int SCALE_MODE_FIT = 1;
  
  int mColorMode = 2;
  
  final Context mContext;
  
  BitmapFactory.Options mDecodeOptions = null;
  
  private final Object mLock = new Object();
  
  int mOrientation = 1;
  
  int mScaleMode = 2;
  
  PrintHelperKitkat(Context paramContext) {
    this.mContext = paramContext;
  }
  
  private Bitmap convertBitmapForColorMode(Bitmap paramBitmap, int paramInt) {
    if (paramInt != 1)
      return paramBitmap; 
    Bitmap bitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    ColorMatrix colorMatrix = new ColorMatrix();
    colorMatrix.setSaturation(0.0F);
    paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
    canvas.drawBitmap(paramBitmap, 0.0F, 0.0F, paint);
    canvas.setBitmap(null);
    return bitmap;
  }
  
  private Matrix getMatrix(int paramInt1, int paramInt2, RectF paramRectF, int paramInt3) {
    Matrix matrix = new Matrix();
    float f = paramRectF.width() / paramInt1;
    if (paramInt3 == 2) {
      f = Math.max(f, paramRectF.height() / paramInt2);
      matrix.postScale(f, f);
      matrix.postTranslate((paramRectF.width() - paramInt1 * f) / 2.0F, (paramRectF.height() - paramInt2 * f) / 2.0F);
      return matrix;
    } 
    f = Math.min(f, paramRectF.height() / paramInt2);
    matrix.postScale(f, f);
    matrix.postTranslate((paramRectF.width() - paramInt1 * f) / 2.0F, (paramRectF.height() - paramInt2 * f) / 2.0F);
    return matrix;
  }
  
  private Bitmap loadBitmap(Uri paramUri, BitmapFactory.Options paramOptions) throws FileNotFoundException {
    if (paramUri == null || this.mContext == null)
      throw new IllegalArgumentException("bad argument to loadBitmap"); 
    InputStream inputStream = null;
    try {
      InputStream inputStream1 = this.mContext.getContentResolver().openInputStream(paramUri);
      inputStream = inputStream1;
      Bitmap bitmap = BitmapFactory.decodeStream(inputStream1, null, paramOptions);
      return bitmap;
    } finally {
      if (inputStream != null)
        try {
          inputStream.close();
        } catch (IOException iOException) {
          Log.w("PrintHelperKitkat", "close fail ", iOException);
        }  
    } 
  }
  
  private Bitmap loadConstrainedBitmap(Uri paramUri, int paramInt) throws FileNotFoundException {
    // Byte code:
    //   0: iload_2
    //   1: ifle -> 15
    //   4: aload_1
    //   5: ifnull -> 15
    //   8: aload_0
    //   9: getfield mContext : Landroid/content/Context;
    //   12: ifnonnull -> 25
    //   15: new java/lang/IllegalArgumentException
    //   18: dup
    //   19: ldc 'bad argument to getScaledBitmap'
    //   21: invokespecial <init> : (Ljava/lang/String;)V
    //   24: athrow
    //   25: new android/graphics/BitmapFactory$Options
    //   28: dup
    //   29: invokespecial <init> : ()V
    //   32: astore #7
    //   34: aload #7
    //   36: iconst_1
    //   37: putfield inJustDecodeBounds : Z
    //   40: aload_0
    //   41: aload_1
    //   42: aload #7
    //   44: invokespecial loadBitmap : (Landroid/net/Uri;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   47: pop
    //   48: aload #7
    //   50: getfield outWidth : I
    //   53: istore #5
    //   55: aload #7
    //   57: getfield outHeight : I
    //   60: istore #6
    //   62: iload #5
    //   64: ifle -> 72
    //   67: iload #6
    //   69: ifgt -> 74
    //   72: aconst_null
    //   73: areturn
    //   74: iload #5
    //   76: iload #6
    //   78: invokestatic max : (II)I
    //   81: istore #4
    //   83: iconst_1
    //   84: istore_3
    //   85: iload #4
    //   87: iload_2
    //   88: if_icmple -> 104
    //   91: iload #4
    //   93: iconst_1
    //   94: iushr
    //   95: istore #4
    //   97: iload_3
    //   98: iconst_1
    //   99: ishl
    //   100: istore_3
    //   101: goto -> 85
    //   104: iload_3
    //   105: ifle -> 72
    //   108: iload #5
    //   110: iload #6
    //   112: invokestatic min : (II)I
    //   115: iload_3
    //   116: idiv
    //   117: ifle -> 72
    //   120: aload_0
    //   121: getfield mLock : Ljava/lang/Object;
    //   124: astore #7
    //   126: aload #7
    //   128: monitorenter
    //   129: aload_0
    //   130: new android/graphics/BitmapFactory$Options
    //   133: dup
    //   134: invokespecial <init> : ()V
    //   137: putfield mDecodeOptions : Landroid/graphics/BitmapFactory$Options;
    //   140: aload_0
    //   141: getfield mDecodeOptions : Landroid/graphics/BitmapFactory$Options;
    //   144: iconst_1
    //   145: putfield inMutable : Z
    //   148: aload_0
    //   149: getfield mDecodeOptions : Landroid/graphics/BitmapFactory$Options;
    //   152: iload_3
    //   153: putfield inSampleSize : I
    //   156: aload_0
    //   157: getfield mDecodeOptions : Landroid/graphics/BitmapFactory$Options;
    //   160: astore #8
    //   162: aload #7
    //   164: monitorexit
    //   165: aload_0
    //   166: aload_1
    //   167: aload #8
    //   169: invokespecial loadBitmap : (Landroid/net/Uri;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
    //   172: astore #7
    //   174: aload_0
    //   175: getfield mLock : Ljava/lang/Object;
    //   178: astore_1
    //   179: aload_1
    //   180: monitorenter
    //   181: aload_0
    //   182: aconst_null
    //   183: putfield mDecodeOptions : Landroid/graphics/BitmapFactory$Options;
    //   186: aload_1
    //   187: monitorexit
    //   188: aload #7
    //   190: areturn
    //   191: astore #7
    //   193: aload_1
    //   194: monitorexit
    //   195: aload #7
    //   197: athrow
    //   198: astore_1
    //   199: aload #7
    //   201: monitorexit
    //   202: aload_1
    //   203: athrow
    //   204: astore #7
    //   206: aload_0
    //   207: getfield mLock : Ljava/lang/Object;
    //   210: astore_1
    //   211: aload_1
    //   212: monitorenter
    //   213: aload_0
    //   214: aconst_null
    //   215: putfield mDecodeOptions : Landroid/graphics/BitmapFactory$Options;
    //   218: aload_1
    //   219: monitorexit
    //   220: aload #7
    //   222: athrow
    //   223: astore #7
    //   225: aload_1
    //   226: monitorexit
    //   227: aload #7
    //   229: athrow
    // Exception table:
    //   from	to	target	type
    //   129	165	198	finally
    //   165	174	204	finally
    //   181	188	191	finally
    //   193	195	191	finally
    //   199	202	198	finally
    //   213	220	223	finally
    //   225	227	223	finally
  }
  
  public int getColorMode() {
    return this.mColorMode;
  }
  
  public int getOrientation() {
    return this.mOrientation;
  }
  
  public int getScaleMode() {
    return this.mScaleMode;
  }
  
  public void printBitmap(final String jobName, final Bitmap bitmap, final OnPrintFinishCallback callback) {
    if (bitmap == null)
      return; 
    final int fittingMode = this.mScaleMode;
    PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
    PrintAttributes.MediaSize mediaSize = PrintAttributes.MediaSize.UNKNOWN_PORTRAIT;
    if (bitmap.getWidth() > bitmap.getHeight())
      mediaSize = PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE; 
    PrintAttributes printAttributes = (new PrintAttributes.Builder()).setMediaSize(mediaSize).setColorMode(this.mColorMode).build();
    printManager.print(jobName, new PrintDocumentAdapter() {
          private PrintAttributes mAttributes;
          
          public void onFinish() {
            if (callback != null)
              callback.onFinish(); 
          }
          
          public void onLayout(PrintAttributes param1PrintAttributes1, PrintAttributes param1PrintAttributes2, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.LayoutResultCallback param1LayoutResultCallback, Bundle param1Bundle) {
            boolean bool = true;
            this.mAttributes = param1PrintAttributes2;
            PrintDocumentInfo printDocumentInfo = (new PrintDocumentInfo.Builder(jobName)).setContentType(1).setPageCount(1).build();
            if (param1PrintAttributes2.equals(param1PrintAttributes1))
              bool = false; 
            param1LayoutResultCallback.onLayoutFinished(printDocumentInfo, bool);
          }
          
          public void onWrite(PageRange[] param1ArrayOfPageRange, ParcelFileDescriptor param1ParcelFileDescriptor, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.WriteResultCallback param1WriteResultCallback) {
            PrintedPdfDocument printedPdfDocument = new PrintedPdfDocument(PrintHelperKitkat.this.mContext, this.mAttributes);
            Bitmap bitmap = PrintHelperKitkat.this.convertBitmapForColorMode(bitmap, this.mAttributes.getColorMode());
            try {
              PdfDocument.Page page = printedPdfDocument.startPage(1);
              RectF rectF = new RectF(page.getInfo().getContentRect());
              Matrix matrix = PrintHelperKitkat.this.getMatrix(bitmap.getWidth(), bitmap.getHeight(), rectF, fittingMode);
              page.getCanvas().drawBitmap(bitmap, matrix, null);
              printedPdfDocument.finishPage(page);
              try {
                printedPdfDocument.writeTo(new FileOutputStream(param1ParcelFileDescriptor.getFileDescriptor()));
                param1WriteResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
              } catch (IOException iOException1) {}
              return;
            } finally {
              if (printedPdfDocument != null)
                printedPdfDocument.close(); 
              if (iOException != null)
                try {
                  iOException.close();
                } catch (IOException iOException1) {} 
              if (bitmap != bitmap)
                bitmap.recycle(); 
            } 
          }
        }printAttributes);
  }
  
  public void printBitmap(final String jobName, final Uri imageFile, final OnPrintFinishCallback callback) throws FileNotFoundException {
    PrintDocumentAdapter printDocumentAdapter = new PrintDocumentAdapter() {
        private PrintAttributes mAttributes;
        
        Bitmap mBitmap = null;
        
        AsyncTask<Uri, Boolean, Bitmap> mLoadBitmap;
        
        private void cancelLoad() {
          synchronized (PrintHelperKitkat.this.mLock) {
            if (PrintHelperKitkat.this.mDecodeOptions != null) {
              PrintHelperKitkat.this.mDecodeOptions.requestCancelDecode();
              PrintHelperKitkat.this.mDecodeOptions = null;
            } 
            return;
          } 
        }
        
        public void onFinish() {
          super.onFinish();
          cancelLoad();
          if (this.mLoadBitmap != null)
            this.mLoadBitmap.cancel(true); 
          if (callback != null)
            callback.onFinish(); 
          if (this.mBitmap != null) {
            this.mBitmap.recycle();
            this.mBitmap = null;
          } 
        }
        
        public void onLayout(final PrintAttributes oldPrintAttributes, final PrintAttributes newPrintAttributes, CancellationSignal param1CancellationSignal, final PrintDocumentAdapter.LayoutResultCallback layoutResultCallback, Bundle param1Bundle) {
          final PrintDocumentInfo cancellationSignal;
          boolean bool = true;
          this.mAttributes = newPrintAttributes;
          if (param1CancellationSignal.isCanceled()) {
            layoutResultCallback.onLayoutCancelled();
            return;
          } 
          if (this.mBitmap != null) {
            printDocumentInfo = (new PrintDocumentInfo.Builder(jobName)).setContentType(1).setPageCount(1).build();
            if (newPrintAttributes.equals(oldPrintAttributes))
              bool = false; 
            layoutResultCallback.onLayoutFinished(printDocumentInfo, bool);
            return;
          } 
          this.mLoadBitmap = (new AsyncTask<Uri, Boolean, Bitmap>() {
              protected Bitmap doInBackground(Uri... param2VarArgs) {
                try {
                  return PrintHelperKitkat.this.loadConstrainedBitmap(imageFile, 3500);
                } catch (FileNotFoundException fileNotFoundException) {
                  return null;
                } 
              }
              
              protected void onCancelled(Bitmap param2Bitmap) {
                layoutResultCallback.onLayoutCancelled();
                PrintHelperKitkat.null.this.mLoadBitmap = null;
              }
              
              protected void onPostExecute(Bitmap param2Bitmap) {
                boolean bool = true;
                super.onPostExecute(param2Bitmap);
                PrintHelperKitkat.null.this.mBitmap = param2Bitmap;
                if (param2Bitmap != null) {
                  PrintDocumentInfo printDocumentInfo = (new PrintDocumentInfo.Builder(jobName)).setContentType(1).setPageCount(1).build();
                  if (newPrintAttributes.equals(oldPrintAttributes))
                    bool = false; 
                  layoutResultCallback.onLayoutFinished(printDocumentInfo, bool);
                } else {
                  layoutResultCallback.onLayoutFailed(null);
                } 
                PrintHelperKitkat.null.this.mLoadBitmap = null;
              }
              
              protected void onPreExecute() {
                cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
                      public void onCancel() {
                        PrintHelperKitkat.null.this.cancelLoad();
                        PrintHelperKitkat.null.null.this.cancel(false);
                      }
                    });
              }
            }).execute((Object[])new Uri[0]);
        }
        
        public void onWrite(PageRange[] param1ArrayOfPageRange, ParcelFileDescriptor param1ParcelFileDescriptor, CancellationSignal param1CancellationSignal, PrintDocumentAdapter.WriteResultCallback param1WriteResultCallback) {
          PrintedPdfDocument printedPdfDocument = new PrintedPdfDocument(PrintHelperKitkat.this.mContext, this.mAttributes);
          Bitmap bitmap = PrintHelperKitkat.this.convertBitmapForColorMode(this.mBitmap, this.mAttributes.getColorMode());
          try {
            PdfDocument.Page page = printedPdfDocument.startPage(1);
            RectF rectF = new RectF(page.getInfo().getContentRect());
            Matrix matrix = PrintHelperKitkat.this.getMatrix(this.mBitmap.getWidth(), this.mBitmap.getHeight(), rectF, fittingMode);
            page.getCanvas().drawBitmap(bitmap, matrix, null);
            printedPdfDocument.finishPage(page);
            try {
              printedPdfDocument.writeTo(new FileOutputStream(param1ParcelFileDescriptor.getFileDescriptor()));
              param1WriteResultCallback.onWriteFinished(new PageRange[] { PageRange.ALL_PAGES });
            } catch (IOException iOException1) {}
            return;
          } finally {
            if (printedPdfDocument != null)
              printedPdfDocument.close(); 
            if (iOException != null)
              try {
                iOException.close();
              } catch (IOException iOException1) {} 
            if (bitmap != this.mBitmap)
              bitmap.recycle(); 
          } 
        }
      };
    PrintManager printManager = (PrintManager)this.mContext.getSystemService("print");
    PrintAttributes.Builder builder = new PrintAttributes.Builder();
    builder.setColorMode(this.mColorMode);
    if (this.mOrientation == 1) {
      builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
    } else if (this.mOrientation == 2) {
      builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_PORTRAIT);
    } 
    printManager.print(jobName, printDocumentAdapter, builder.build());
  }
  
  public void setColorMode(int paramInt) {
    this.mColorMode = paramInt;
  }
  
  public void setOrientation(int paramInt) {
    this.mOrientation = paramInt;
  }
  
  public void setScaleMode(int paramInt) {
    this.mScaleMode = paramInt;
  }
  
  public static interface OnPrintFinishCallback {
    void onFinish();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\print\PrintHelperKitkat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */