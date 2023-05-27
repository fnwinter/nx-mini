package android.support.v4.app;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

final class FragmentState implements Parcelable {
  public static final Parcelable.Creator<FragmentState> CREATOR = new Parcelable.Creator<FragmentState>() {
      public FragmentState createFromParcel(Parcel param1Parcel) {
        return new FragmentState(param1Parcel);
      }
      
      public FragmentState[] newArray(int param1Int) {
        return new FragmentState[param1Int];
      }
    };
  
  final Bundle mArguments;
  
  final String mClassName;
  
  final int mContainerId;
  
  final boolean mDetached;
  
  final int mFragmentId;
  
  final boolean mFromLayout;
  
  final int mIndex;
  
  Fragment mInstance;
  
  final boolean mRetainInstance;
  
  Bundle mSavedFragmentState;
  
  final String mTag;
  
  public FragmentState(Parcel paramParcel) {
    boolean bool1;
    this.mClassName = paramParcel.readString();
    this.mIndex = paramParcel.readInt();
    if (paramParcel.readInt() != 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.mFromLayout = bool1;
    this.mFragmentId = paramParcel.readInt();
    this.mContainerId = paramParcel.readInt();
    this.mTag = paramParcel.readString();
    if (paramParcel.readInt() != 0) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    this.mRetainInstance = bool1;
    if (paramParcel.readInt() != 0) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    this.mDetached = bool1;
    this.mArguments = paramParcel.readBundle();
    this.mSavedFragmentState = paramParcel.readBundle();
  }
  
  public FragmentState(Fragment paramFragment) {
    this.mClassName = paramFragment.getClass().getName();
    this.mIndex = paramFragment.mIndex;
    this.mFromLayout = paramFragment.mFromLayout;
    this.mFragmentId = paramFragment.mFragmentId;
    this.mContainerId = paramFragment.mContainerId;
    this.mTag = paramFragment.mTag;
    this.mRetainInstance = paramFragment.mRetainInstance;
    this.mDetached = paramFragment.mDetached;
    this.mArguments = paramFragment.mArguments;
  }
  
  public int describeContents() {
    return 0;
  }
  
  public Fragment instantiate(FragmentHostCallback paramFragmentHostCallback, Fragment paramFragment) {
    if (this.mInstance != null)
      return this.mInstance; 
    Context context = paramFragmentHostCallback.getContext();
    if (this.mArguments != null)
      this.mArguments.setClassLoader(context.getClassLoader()); 
    this.mInstance = Fragment.instantiate(context, this.mClassName, this.mArguments);
    if (this.mSavedFragmentState != null) {
      this.mSavedFragmentState.setClassLoader(context.getClassLoader());
      this.mInstance.mSavedFragmentState = this.mSavedFragmentState;
    } 
    this.mInstance.setIndex(this.mIndex, paramFragment);
    this.mInstance.mFromLayout = this.mFromLayout;
    this.mInstance.mRestored = true;
    this.mInstance.mFragmentId = this.mFragmentId;
    this.mInstance.mContainerId = this.mContainerId;
    this.mInstance.mTag = this.mTag;
    this.mInstance.mRetainInstance = this.mRetainInstance;
    this.mInstance.mDetached = this.mDetached;
    this.mInstance.mFragmentManager = paramFragmentHostCallback.mFragmentManager;
    if (FragmentManagerImpl.DEBUG)
      Log.v("FragmentManager", "Instantiated fragment " + this.mInstance); 
    return this.mInstance;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    boolean bool = true;
    paramParcel.writeString(this.mClassName);
    paramParcel.writeInt(this.mIndex);
    if (this.mFromLayout) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    paramParcel.writeInt(paramInt);
    paramParcel.writeInt(this.mFragmentId);
    paramParcel.writeInt(this.mContainerId);
    paramParcel.writeString(this.mTag);
    if (this.mRetainInstance) {
      paramInt = 1;
    } else {
      paramInt = 0;
    } 
    paramParcel.writeInt(paramInt);
    if (this.mDetached) {
      paramInt = bool;
    } else {
      paramInt = 0;
    } 
    paramParcel.writeInt(paramInt);
    paramParcel.writeBundle(this.mArguments);
    paramParcel.writeBundle(this.mSavedFragmentState);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\app\FragmentState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */