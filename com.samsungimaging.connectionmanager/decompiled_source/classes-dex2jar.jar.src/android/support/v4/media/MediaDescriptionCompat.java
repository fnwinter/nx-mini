package android.support.v4.media;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public final class MediaDescriptionCompat implements Parcelable {
  public static final Parcelable.Creator<MediaDescriptionCompat> CREATOR = new Parcelable.Creator<MediaDescriptionCompat>() {
      public MediaDescriptionCompat createFromParcel(Parcel param1Parcel) {
        return (Build.VERSION.SDK_INT < 21) ? new MediaDescriptionCompat(param1Parcel) : MediaDescriptionCompat.fromMediaDescription(MediaDescriptionCompatApi21.fromParcel(param1Parcel));
      }
      
      public MediaDescriptionCompat[] newArray(int param1Int) {
        return new MediaDescriptionCompat[param1Int];
      }
    };
  
  private final CharSequence mDescription;
  
  private Object mDescriptionObj;
  
  private final Bundle mExtras;
  
  private final Bitmap mIcon;
  
  private final Uri mIconUri;
  
  private final String mMediaId;
  
  private final Uri mMediaUri;
  
  private final CharSequence mSubtitle;
  
  private final CharSequence mTitle;
  
  private MediaDescriptionCompat(Parcel paramParcel) {
    this.mMediaId = paramParcel.readString();
    this.mTitle = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
    this.mSubtitle = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
    this.mDescription = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
    this.mIcon = (Bitmap)paramParcel.readParcelable(null);
    this.mIconUri = (Uri)paramParcel.readParcelable(null);
    this.mExtras = paramParcel.readBundle();
    this.mMediaUri = (Uri)paramParcel.readParcelable(null);
  }
  
  private MediaDescriptionCompat(String paramString, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, Bitmap paramBitmap, Uri paramUri1, Bundle paramBundle, Uri paramUri2) {
    this.mMediaId = paramString;
    this.mTitle = paramCharSequence1;
    this.mSubtitle = paramCharSequence2;
    this.mDescription = paramCharSequence3;
    this.mIcon = paramBitmap;
    this.mIconUri = paramUri1;
    this.mExtras = paramBundle;
    this.mMediaUri = paramUri2;
  }
  
  public static MediaDescriptionCompat fromMediaDescription(Object paramObject) {
    if (paramObject == null || Build.VERSION.SDK_INT < 21)
      return null; 
    Builder builder = new Builder();
    builder.setMediaId(MediaDescriptionCompatApi21.getMediaId(paramObject));
    builder.setTitle(MediaDescriptionCompatApi21.getTitle(paramObject));
    builder.setSubtitle(MediaDescriptionCompatApi21.getSubtitle(paramObject));
    builder.setDescription(MediaDescriptionCompatApi21.getDescription(paramObject));
    builder.setIconBitmap(MediaDescriptionCompatApi21.getIconBitmap(paramObject));
    builder.setIconUri(MediaDescriptionCompatApi21.getIconUri(paramObject));
    builder.setExtras(MediaDescriptionCompatApi21.getExtras(paramObject));
    if (Build.VERSION.SDK_INT >= 23)
      builder.setMediaUri(MediaDescriptionCompatApi23.getMediaUri(paramObject)); 
    MediaDescriptionCompat mediaDescriptionCompat = builder.build();
    mediaDescriptionCompat.mDescriptionObj = paramObject;
    return mediaDescriptionCompat;
  }
  
  public int describeContents() {
    return 0;
  }
  
  @Nullable
  public CharSequence getDescription() {
    return this.mDescription;
  }
  
  @Nullable
  public Bundle getExtras() {
    return this.mExtras;
  }
  
  @Nullable
  public Bitmap getIconBitmap() {
    return this.mIcon;
  }
  
  @Nullable
  public Uri getIconUri() {
    return this.mIconUri;
  }
  
  public Object getMediaDescription() {
    if (this.mDescriptionObj != null || Build.VERSION.SDK_INT < 21)
      return this.mDescriptionObj; 
    Object object = MediaDescriptionCompatApi21.Builder.newInstance();
    MediaDescriptionCompatApi21.Builder.setMediaId(object, this.mMediaId);
    MediaDescriptionCompatApi21.Builder.setTitle(object, this.mTitle);
    MediaDescriptionCompatApi21.Builder.setSubtitle(object, this.mSubtitle);
    MediaDescriptionCompatApi21.Builder.setDescription(object, this.mDescription);
    MediaDescriptionCompatApi21.Builder.setIconBitmap(object, this.mIcon);
    MediaDescriptionCompatApi21.Builder.setIconUri(object, this.mIconUri);
    MediaDescriptionCompatApi21.Builder.setExtras(object, this.mExtras);
    if (Build.VERSION.SDK_INT >= 23)
      MediaDescriptionCompatApi23.Builder.setMediaUri(object, this.mMediaUri); 
    this.mDescriptionObj = MediaDescriptionCompatApi21.Builder.build(object);
    return this.mDescriptionObj;
  }
  
  @Nullable
  public String getMediaId() {
    return this.mMediaId;
  }
  
  @Nullable
  public Uri getMediaUri() {
    return this.mMediaUri;
  }
  
  @Nullable
  public CharSequence getSubtitle() {
    return this.mSubtitle;
  }
  
  @Nullable
  public CharSequence getTitle() {
    return this.mTitle;
  }
  
  public String toString() {
    return this.mTitle + ", " + this.mSubtitle + ", " + this.mDescription;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    if (Build.VERSION.SDK_INT < 21) {
      paramParcel.writeString(this.mMediaId);
      TextUtils.writeToParcel(this.mTitle, paramParcel, paramInt);
      TextUtils.writeToParcel(this.mSubtitle, paramParcel, paramInt);
      TextUtils.writeToParcel(this.mDescription, paramParcel, paramInt);
      paramParcel.writeParcelable((Parcelable)this.mIcon, paramInt);
      paramParcel.writeParcelable((Parcelable)this.mIconUri, paramInt);
      paramParcel.writeBundle(this.mExtras);
      return;
    } 
    MediaDescriptionCompatApi21.writeToParcel(getMediaDescription(), paramParcel, paramInt);
  }
  
  public static final class Builder {
    private CharSequence mDescription;
    
    private Bundle mExtras;
    
    private Bitmap mIcon;
    
    private Uri mIconUri;
    
    private String mMediaId;
    
    private Uri mMediaUri;
    
    private CharSequence mSubtitle;
    
    private CharSequence mTitle;
    
    public MediaDescriptionCompat build() {
      return new MediaDescriptionCompat(this.mMediaId, this.mTitle, this.mSubtitle, this.mDescription, this.mIcon, this.mIconUri, this.mExtras, this.mMediaUri);
    }
    
    public Builder setDescription(@Nullable CharSequence param1CharSequence) {
      this.mDescription = param1CharSequence;
      return this;
    }
    
    public Builder setExtras(@Nullable Bundle param1Bundle) {
      this.mExtras = param1Bundle;
      return this;
    }
    
    public Builder setIconBitmap(@Nullable Bitmap param1Bitmap) {
      this.mIcon = param1Bitmap;
      return this;
    }
    
    public Builder setIconUri(@Nullable Uri param1Uri) {
      this.mIconUri = param1Uri;
      return this;
    }
    
    public Builder setMediaId(@Nullable String param1String) {
      this.mMediaId = param1String;
      return this;
    }
    
    public Builder setMediaUri(@Nullable Uri param1Uri) {
      this.mMediaUri = param1Uri;
      return this;
    }
    
    public Builder setSubtitle(@Nullable CharSequence param1CharSequence) {
      this.mSubtitle = param1CharSequence;
      return this;
    }
    
    public Builder setTitle(@Nullable CharSequence param1CharSequence) {
      this.mTitle = param1CharSequence;
      return this;
    }
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\media\MediaDescriptionCompat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */