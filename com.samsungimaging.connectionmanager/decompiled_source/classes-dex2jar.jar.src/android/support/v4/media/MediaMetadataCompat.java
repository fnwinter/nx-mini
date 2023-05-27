package android.support.v4.media;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.util.ArrayMap;
import android.util.Log;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

public final class MediaMetadataCompat implements Parcelable {
  public static final Parcelable.Creator<MediaMetadataCompat> CREATOR;
  
  private static final ArrayMap<String, Integer> METADATA_KEYS_TYPE = new ArrayMap();
  
  public static final String METADATA_KEY_ALBUM = "android.media.metadata.ALBUM";
  
  public static final String METADATA_KEY_ALBUM_ART = "android.media.metadata.ALBUM_ART";
  
  public static final String METADATA_KEY_ALBUM_ARTIST = "android.media.metadata.ALBUM_ARTIST";
  
  public static final String METADATA_KEY_ALBUM_ART_URI = "android.media.metadata.ALBUM_ART_URI";
  
  public static final String METADATA_KEY_ART = "android.media.metadata.ART";
  
  public static final String METADATA_KEY_ARTIST = "android.media.metadata.ARTIST";
  
  public static final String METADATA_KEY_ART_URI = "android.media.metadata.ART_URI";
  
  public static final String METADATA_KEY_AUTHOR = "android.media.metadata.AUTHOR";
  
  public static final String METADATA_KEY_COMPILATION = "android.media.metadata.COMPILATION";
  
  public static final String METADATA_KEY_COMPOSER = "android.media.metadata.COMPOSER";
  
  public static final String METADATA_KEY_DATE = "android.media.metadata.DATE";
  
  public static final String METADATA_KEY_DISC_NUMBER = "android.media.metadata.DISC_NUMBER";
  
  public static final String METADATA_KEY_DISPLAY_DESCRIPTION = "android.media.metadata.DISPLAY_DESCRIPTION";
  
  public static final String METADATA_KEY_DISPLAY_ICON = "android.media.metadata.DISPLAY_ICON";
  
  public static final String METADATA_KEY_DISPLAY_ICON_URI = "android.media.metadata.DISPLAY_ICON_URI";
  
  public static final String METADATA_KEY_DISPLAY_SUBTITLE = "android.media.metadata.DISPLAY_SUBTITLE";
  
  public static final String METADATA_KEY_DISPLAY_TITLE = "android.media.metadata.DISPLAY_TITLE";
  
  public static final String METADATA_KEY_DURATION = "android.media.metadata.DURATION";
  
  public static final String METADATA_KEY_GENRE = "android.media.metadata.GENRE";
  
  public static final String METADATA_KEY_MEDIA_ID = "android.media.metadata.MEDIA_ID";
  
  public static final String METADATA_KEY_NUM_TRACKS = "android.media.metadata.NUM_TRACKS";
  
  public static final String METADATA_KEY_RATING = "android.media.metadata.RATING";
  
  public static final String METADATA_KEY_TITLE = "android.media.metadata.TITLE";
  
  public static final String METADATA_KEY_TRACK_NUMBER = "android.media.metadata.TRACK_NUMBER";
  
  public static final String METADATA_KEY_USER_RATING = "android.media.metadata.USER_RATING";
  
  public static final String METADATA_KEY_WRITER = "android.media.metadata.WRITER";
  
  public static final String METADATA_KEY_YEAR = "android.media.metadata.YEAR";
  
  private static final int METADATA_TYPE_BITMAP = 2;
  
  private static final int METADATA_TYPE_LONG = 0;
  
  private static final int METADATA_TYPE_RATING = 3;
  
  private static final int METADATA_TYPE_TEXT = 1;
  
  private static final String[] PREFERRED_BITMAP_ORDER;
  
  private static final String[] PREFERRED_DESCRIPTION_ORDER = new String[] { "android.media.metadata.TITLE", "android.media.metadata.ARTIST", "android.media.metadata.ALBUM", "android.media.metadata.ALBUM_ARTIST", "android.media.metadata.WRITER", "android.media.metadata.AUTHOR", "android.media.metadata.COMPOSER" };
  
  private static final String[] PREFERRED_URI_ORDER;
  
  private static final String TAG = "MediaMetadata";
  
  private final Bundle mBundle;
  
  private MediaDescriptionCompat mDescription;
  
  private Object mMetadataObj;
  
  static {
    PREFERRED_BITMAP_ORDER = new String[] { "android.media.metadata.DISPLAY_ICON", "android.media.metadata.ART", "android.media.metadata.ALBUM_ART" };
    PREFERRED_URI_ORDER = new String[] { "android.media.metadata.DISPLAY_ICON_URI", "android.media.metadata.ART_URI", "android.media.metadata.ALBUM_ART_URI" };
    CREATOR = new Parcelable.Creator<MediaMetadataCompat>() {
        public MediaMetadataCompat createFromParcel(Parcel param1Parcel) {
          return new MediaMetadataCompat(param1Parcel);
        }
        
        public MediaMetadataCompat[] newArray(int param1Int) {
          return new MediaMetadataCompat[param1Int];
        }
      };
  }
  
  private MediaMetadataCompat(Bundle paramBundle) {
    this.mBundle = new Bundle(paramBundle);
  }
  
  private MediaMetadataCompat(Parcel paramParcel) {
    this.mBundle = paramParcel.readBundle();
  }
  
  public static MediaMetadataCompat fromMediaMetadata(Object paramObject) {
    if (paramObject == null || Build.VERSION.SDK_INT < 21)
      return null; 
    Builder builder = new Builder();
    for (String str : MediaMetadataCompatApi21.keySet(paramObject)) {
      Integer integer = (Integer)METADATA_KEYS_TYPE.get(str);
      if (integer != null) {
        switch (integer.intValue()) {
          default:
            continue;
          case 0:
            builder.putLong(str, MediaMetadataCompatApi21.getLong(paramObject, str));
            continue;
          case 2:
            builder.putBitmap(str, MediaMetadataCompatApi21.getBitmap(paramObject, str));
            continue;
          case 3:
            builder.putRating(str, RatingCompat.fromRating(MediaMetadataCompatApi21.getRating(paramObject, str)));
            continue;
          case 1:
            break;
        } 
        builder.putText(str, MediaMetadataCompatApi21.getText(paramObject, str));
      } 
    } 
    MediaMetadataCompat mediaMetadataCompat = builder.build();
    mediaMetadataCompat.mMetadataObj = paramObject;
    return mediaMetadataCompat;
  }
  
  public boolean containsKey(String paramString) {
    return this.mBundle.containsKey(paramString);
  }
  
  public int describeContents() {
    return 0;
  }
  
  public Bitmap getBitmap(String paramString) {
    try {
      return (Bitmap)this.mBundle.getParcelable(paramString);
    } catch (Exception exception) {
      Log.w("MediaMetadata", "Failed to retrieve a key as Bitmap.", exception);
      return null;
    } 
  }
  
  public Bundle getBundle() {
    return this.mBundle;
  }
  
  public MediaDescriptionCompat getDescription() {
    // Byte code:
    //   0: aload_0
    //   1: getfield mDescription : Landroid/support/v4/media/MediaDescriptionCompat;
    //   4: ifnull -> 12
    //   7: aload_0
    //   8: getfield mDescription : Landroid/support/v4/media/MediaDescriptionCompat;
    //   11: areturn
    //   12: aload_0
    //   13: ldc 'android.media.metadata.MEDIA_ID'
    //   15: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   18: astore #7
    //   20: iconst_3
    //   21: anewarray java/lang/CharSequence
    //   24: astore #8
    //   26: aconst_null
    //   27: astore #5
    //   29: aconst_null
    //   30: astore #6
    //   32: aload_0
    //   33: ldc 'android.media.metadata.DISPLAY_TITLE'
    //   35: invokevirtual getText : (Ljava/lang/String;)Ljava/lang/CharSequence;
    //   38: astore #4
    //   40: aload #4
    //   42: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   45: ifne -> 221
    //   48: aload #8
    //   50: iconst_0
    //   51: aload #4
    //   53: aastore
    //   54: aload #8
    //   56: iconst_1
    //   57: aload_0
    //   58: ldc 'android.media.metadata.DISPLAY_SUBTITLE'
    //   60: invokevirtual getText : (Ljava/lang/String;)Ljava/lang/CharSequence;
    //   63: aastore
    //   64: aload #8
    //   66: iconst_2
    //   67: aload_0
    //   68: ldc 'android.media.metadata.DISPLAY_DESCRIPTION'
    //   70: invokevirtual getText : (Ljava/lang/String;)Ljava/lang/CharSequence;
    //   73: aastore
    //   74: iconst_0
    //   75: istore_1
    //   76: aload #5
    //   78: astore #4
    //   80: iload_1
    //   81: getstatic android/support/v4/media/MediaMetadataCompat.PREFERRED_BITMAP_ORDER : [Ljava/lang/String;
    //   84: arraylength
    //   85: if_icmpge -> 104
    //   88: aload_0
    //   89: getstatic android/support/v4/media/MediaMetadataCompat.PREFERRED_BITMAP_ORDER : [Ljava/lang/String;
    //   92: iload_1
    //   93: aaload
    //   94: invokevirtual getBitmap : (Ljava/lang/String;)Landroid/graphics/Bitmap;
    //   97: astore #4
    //   99: aload #4
    //   101: ifnull -> 280
    //   104: iconst_0
    //   105: istore_1
    //   106: aload #6
    //   108: astore #5
    //   110: iload_1
    //   111: getstatic android/support/v4/media/MediaMetadataCompat.PREFERRED_URI_ORDER : [Ljava/lang/String;
    //   114: arraylength
    //   115: if_icmpge -> 144
    //   118: aload_0
    //   119: getstatic android/support/v4/media/MediaMetadataCompat.PREFERRED_URI_ORDER : [Ljava/lang/String;
    //   122: iload_1
    //   123: aaload
    //   124: invokevirtual getString : (Ljava/lang/String;)Ljava/lang/String;
    //   127: astore #5
    //   129: aload #5
    //   131: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   134: ifne -> 287
    //   137: aload #5
    //   139: invokestatic parse : (Ljava/lang/String;)Landroid/net/Uri;
    //   142: astore #5
    //   144: new android/support/v4/media/MediaDescriptionCompat$Builder
    //   147: dup
    //   148: invokespecial <init> : ()V
    //   151: astore #6
    //   153: aload #6
    //   155: aload #7
    //   157: invokevirtual setMediaId : (Ljava/lang/String;)Landroid/support/v4/media/MediaDescriptionCompat$Builder;
    //   160: pop
    //   161: aload #6
    //   163: aload #8
    //   165: iconst_0
    //   166: aaload
    //   167: invokevirtual setTitle : (Ljava/lang/CharSequence;)Landroid/support/v4/media/MediaDescriptionCompat$Builder;
    //   170: pop
    //   171: aload #6
    //   173: aload #8
    //   175: iconst_1
    //   176: aaload
    //   177: invokevirtual setSubtitle : (Ljava/lang/CharSequence;)Landroid/support/v4/media/MediaDescriptionCompat$Builder;
    //   180: pop
    //   181: aload #6
    //   183: aload #8
    //   185: iconst_2
    //   186: aaload
    //   187: invokevirtual setDescription : (Ljava/lang/CharSequence;)Landroid/support/v4/media/MediaDescriptionCompat$Builder;
    //   190: pop
    //   191: aload #6
    //   193: aload #4
    //   195: invokevirtual setIconBitmap : (Landroid/graphics/Bitmap;)Landroid/support/v4/media/MediaDescriptionCompat$Builder;
    //   198: pop
    //   199: aload #6
    //   201: aload #5
    //   203: invokevirtual setIconUri : (Landroid/net/Uri;)Landroid/support/v4/media/MediaDescriptionCompat$Builder;
    //   206: pop
    //   207: aload_0
    //   208: aload #6
    //   210: invokevirtual build : ()Landroid/support/v4/media/MediaDescriptionCompat;
    //   213: putfield mDescription : Landroid/support/v4/media/MediaDescriptionCompat;
    //   216: aload_0
    //   217: getfield mDescription : Landroid/support/v4/media/MediaDescriptionCompat;
    //   220: areturn
    //   221: iconst_0
    //   222: istore_2
    //   223: iconst_0
    //   224: istore_1
    //   225: iload_2
    //   226: aload #8
    //   228: arraylength
    //   229: if_icmpge -> 74
    //   232: iload_1
    //   233: getstatic android/support/v4/media/MediaMetadataCompat.PREFERRED_DESCRIPTION_ORDER : [Ljava/lang/String;
    //   236: arraylength
    //   237: if_icmpge -> 74
    //   240: aload_0
    //   241: getstatic android/support/v4/media/MediaMetadataCompat.PREFERRED_DESCRIPTION_ORDER : [Ljava/lang/String;
    //   244: iload_1
    //   245: aaload
    //   246: invokevirtual getText : (Ljava/lang/String;)Ljava/lang/CharSequence;
    //   249: astore #4
    //   251: iload_2
    //   252: istore_3
    //   253: aload #4
    //   255: invokestatic isEmpty : (Ljava/lang/CharSequence;)Z
    //   258: ifne -> 271
    //   261: aload #8
    //   263: iload_2
    //   264: aload #4
    //   266: aastore
    //   267: iload_2
    //   268: iconst_1
    //   269: iadd
    //   270: istore_3
    //   271: iload_1
    //   272: iconst_1
    //   273: iadd
    //   274: istore_1
    //   275: iload_3
    //   276: istore_2
    //   277: goto -> 225
    //   280: iload_1
    //   281: iconst_1
    //   282: iadd
    //   283: istore_1
    //   284: goto -> 76
    //   287: iload_1
    //   288: iconst_1
    //   289: iadd
    //   290: istore_1
    //   291: goto -> 106
  }
  
  public long getLong(String paramString) {
    return this.mBundle.getLong(paramString, 0L);
  }
  
  public Object getMediaMetadata() {
    if (this.mMetadataObj != null || Build.VERSION.SDK_INT < 21)
      return this.mMetadataObj; 
    Object object = MediaMetadataCompatApi21.Builder.newInstance();
    for (String str : keySet()) {
      Integer integer = (Integer)METADATA_KEYS_TYPE.get(str);
      if (integer != null) {
        switch (integer.intValue()) {
          default:
            continue;
          case 0:
            MediaMetadataCompatApi21.Builder.putLong(object, str, getLong(str));
            continue;
          case 2:
            MediaMetadataCompatApi21.Builder.putBitmap(object, str, getBitmap(str));
            continue;
          case 3:
            MediaMetadataCompatApi21.Builder.putRating(object, str, getRating(str).getRating());
            continue;
          case 1:
            break;
        } 
        MediaMetadataCompatApi21.Builder.putText(object, str, getText(str));
      } 
    } 
    this.mMetadataObj = MediaMetadataCompatApi21.Builder.build(object);
    return this.mMetadataObj;
  }
  
  public RatingCompat getRating(String paramString) {
    try {
      return (RatingCompat)this.mBundle.getParcelable(paramString);
    } catch (Exception exception) {
      Log.w("MediaMetadata", "Failed to retrieve a key as Rating.", exception);
      return null;
    } 
  }
  
  public String getString(String paramString) {
    CharSequence charSequence = this.mBundle.getCharSequence(paramString);
    return (charSequence != null) ? charSequence.toString() : null;
  }
  
  public CharSequence getText(String paramString) {
    return this.mBundle.getCharSequence(paramString);
  }
  
  public Set<String> keySet() {
    return this.mBundle.keySet();
  }
  
  public int size() {
    return this.mBundle.size();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeBundle(this.mBundle);
  }
  
  static {
    METADATA_KEYS_TYPE.put("android.media.metadata.TITLE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.ARTIST", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DURATION", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.AUTHOR", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.WRITER", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.COMPOSER", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.COMPILATION", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DATE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.YEAR", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.GENRE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.TRACK_NUMBER", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.NUM_TRACKS", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISC_NUMBER", Integer.valueOf(0));
    METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ARTIST", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.ART", Integer.valueOf(2));
    METADATA_KEYS_TYPE.put("android.media.metadata.ART_URI", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART", Integer.valueOf(2));
    METADATA_KEYS_TYPE.put("android.media.metadata.ALBUM_ART_URI", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.USER_RATING", Integer.valueOf(3));
    METADATA_KEYS_TYPE.put("android.media.metadata.RATING", Integer.valueOf(3));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_TITLE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_SUBTITLE", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_DESCRIPTION", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON", Integer.valueOf(2));
    METADATA_KEYS_TYPE.put("android.media.metadata.DISPLAY_ICON_URI", Integer.valueOf(1));
    METADATA_KEYS_TYPE.put("android.media.metadata.MEDIA_ID", Integer.valueOf(1));
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface BitmapKey {}
  
  public static final class Builder {
    private final Bundle mBundle = new Bundle();
    
    public Builder() {}
    
    public Builder(MediaMetadataCompat param1MediaMetadataCompat) {}
    
    public MediaMetadataCompat build() {
      return new MediaMetadataCompat(this.mBundle);
    }
    
    public Builder putBitmap(String param1String, Bitmap param1Bitmap) {
      if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(param1String) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(param1String)).intValue() != 2)
        throw new IllegalArgumentException("The " + param1String + " key cannot be used to put a Bitmap"); 
      this.mBundle.putParcelable(param1String, (Parcelable)param1Bitmap);
      return this;
    }
    
    public Builder putLong(String param1String, long param1Long) {
      if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(param1String) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(param1String)).intValue() != 0)
        throw new IllegalArgumentException("The " + param1String + " key cannot be used to put a long"); 
      this.mBundle.putLong(param1String, param1Long);
      return this;
    }
    
    public Builder putRating(String param1String, RatingCompat param1RatingCompat) {
      if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(param1String) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(param1String)).intValue() != 3)
        throw new IllegalArgumentException("The " + param1String + " key cannot be used to put a Rating"); 
      this.mBundle.putParcelable(param1String, param1RatingCompat);
      return this;
    }
    
    public Builder putString(String param1String1, String param1String2) {
      if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(param1String1) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(param1String1)).intValue() != 1)
        throw new IllegalArgumentException("The " + param1String1 + " key cannot be used to put a String"); 
      this.mBundle.putCharSequence(param1String1, param1String2);
      return this;
    }
    
    public Builder putText(String param1String, CharSequence param1CharSequence) {
      if (MediaMetadataCompat.METADATA_KEYS_TYPE.containsKey(param1String) && ((Integer)MediaMetadataCompat.METADATA_KEYS_TYPE.get(param1String)).intValue() != 1)
        throw new IllegalArgumentException("The " + param1String + " key cannot be used to put a CharSequence"); 
      this.mBundle.putCharSequence(param1String, param1CharSequence);
      return this;
    }
  }
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface LongKey {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface RatingKey {}
  
  @Retention(RetentionPolicy.SOURCE)
  public static @interface TextKey {}
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\media\MediaMetadataCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */