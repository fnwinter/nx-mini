package android.support.v4.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;

final class BackStackState implements Parcelable {
  public static final Parcelable.Creator<BackStackState> CREATOR = new Parcelable.Creator<BackStackState>() {
      public BackStackState createFromParcel(Parcel param1Parcel) {
        return new BackStackState(param1Parcel);
      }
      
      public BackStackState[] newArray(int param1Int) {
        return new BackStackState[param1Int];
      }
    };
  
  final int mBreadCrumbShortTitleRes;
  
  final CharSequence mBreadCrumbShortTitleText;
  
  final int mBreadCrumbTitleRes;
  
  final CharSequence mBreadCrumbTitleText;
  
  final int mIndex;
  
  final String mName;
  
  final int[] mOps;
  
  final ArrayList<String> mSharedElementSourceNames;
  
  final ArrayList<String> mSharedElementTargetNames;
  
  final int mTransition;
  
  final int mTransitionStyle;
  
  public BackStackState(Parcel paramParcel) {
    this.mOps = paramParcel.createIntArray();
    this.mTransition = paramParcel.readInt();
    this.mTransitionStyle = paramParcel.readInt();
    this.mName = paramParcel.readString();
    this.mIndex = paramParcel.readInt();
    this.mBreadCrumbTitleRes = paramParcel.readInt();
    this.mBreadCrumbTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
    this.mBreadCrumbShortTitleRes = paramParcel.readInt();
    this.mBreadCrumbShortTitleText = (CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel);
    this.mSharedElementSourceNames = paramParcel.createStringArrayList();
    this.mSharedElementTargetNames = paramParcel.createStringArrayList();
  }
  
  public BackStackState(BackStackRecord paramBackStackRecord) {
    int i = 0;
    BackStackRecord.Op op = paramBackStackRecord.mHead;
    while (op != null) {
      int j = i;
      if (op.removed != null)
        j = i + op.removed.size(); 
      op = op.next;
      i = j;
    } 
    this.mOps = new int[paramBackStackRecord.mNumOp * 7 + i];
    if (!paramBackStackRecord.mAddToBackStack)
      throw new IllegalStateException("Not on back stack"); 
    op = paramBackStackRecord.mHead;
    i = 0;
    while (op != null) {
      int[] arrayOfInt = this.mOps;
      int j = i + 1;
      arrayOfInt[i] = op.cmd;
      arrayOfInt = this.mOps;
      int k = j + 1;
      if (op.fragment != null) {
        i = op.fragment.mIndex;
      } else {
        i = -1;
      } 
      arrayOfInt[j] = i;
      arrayOfInt = this.mOps;
      i = k + 1;
      arrayOfInt[k] = op.enterAnim;
      arrayOfInt = this.mOps;
      j = i + 1;
      arrayOfInt[i] = op.exitAnim;
      arrayOfInt = this.mOps;
      i = j + 1;
      arrayOfInt[j] = op.popEnterAnim;
      arrayOfInt = this.mOps;
      int m = i + 1;
      arrayOfInt[i] = op.popExitAnim;
      if (op.removed != null) {
        k = op.removed.size();
        this.mOps[m] = k;
        j = 0;
        for (i = m + 1; j < k; i++) {
          this.mOps[i] = ((Fragment)op.removed.get(j)).mIndex;
          j++;
        } 
      } else {
        arrayOfInt = this.mOps;
        i = m + 1;
        arrayOfInt[m] = 0;
      } 
      op = op.next;
    } 
    this.mTransition = paramBackStackRecord.mTransition;
    this.mTransitionStyle = paramBackStackRecord.mTransitionStyle;
    this.mName = paramBackStackRecord.mName;
    this.mIndex = paramBackStackRecord.mIndex;
    this.mBreadCrumbTitleRes = paramBackStackRecord.mBreadCrumbTitleRes;
    this.mBreadCrumbTitleText = paramBackStackRecord.mBreadCrumbTitleText;
    this.mBreadCrumbShortTitleRes = paramBackStackRecord.mBreadCrumbShortTitleRes;
    this.mBreadCrumbShortTitleText = paramBackStackRecord.mBreadCrumbShortTitleText;
    this.mSharedElementSourceNames = paramBackStackRecord.mSharedElementSourceNames;
    this.mSharedElementTargetNames = paramBackStackRecord.mSharedElementTargetNames;
  }
  
  public int describeContents() {
    return 0;
  }
  
  public BackStackRecord instantiate(FragmentManagerImpl paramFragmentManagerImpl) {
    BackStackRecord backStackRecord = new BackStackRecord(paramFragmentManagerImpl);
    int i = 0;
    int j;
    for (j = 0; i < this.mOps.length; j++) {
      BackStackRecord.Op op = new BackStackRecord.Op();
      int[] arrayOfInt = this.mOps;
      int k = i + 1;
      op.cmd = arrayOfInt[i];
      if (FragmentManagerImpl.DEBUG)
        Log.v("FragmentManager", "Instantiate " + backStackRecord + " op #" + j + " base fragment #" + this.mOps[k]); 
      arrayOfInt = this.mOps;
      i = k + 1;
      k = arrayOfInt[k];
      if (k >= 0) {
        op.fragment = paramFragmentManagerImpl.mActive.get(k);
      } else {
        op.fragment = null;
      } 
      arrayOfInt = this.mOps;
      k = i + 1;
      op.enterAnim = arrayOfInt[i];
      arrayOfInt = this.mOps;
      i = k + 1;
      op.exitAnim = arrayOfInt[k];
      arrayOfInt = this.mOps;
      k = i + 1;
      op.popEnterAnim = arrayOfInt[i];
      arrayOfInt = this.mOps;
      int m = k + 1;
      op.popExitAnim = arrayOfInt[k];
      arrayOfInt = this.mOps;
      i = m + 1;
      int n = arrayOfInt[m];
      k = i;
      if (n > 0) {
        op.removed = new ArrayList<Fragment>(n);
        m = 0;
        while (true) {
          k = i;
          if (m < n) {
            if (FragmentManagerImpl.DEBUG)
              Log.v("FragmentManager", "Instantiate " + backStackRecord + " set remove fragment #" + this.mOps[i]); 
            Fragment fragment = paramFragmentManagerImpl.mActive.get(this.mOps[i]);
            op.removed.add(fragment);
            m++;
            i++;
            continue;
          } 
          break;
        } 
      } 
      i = k;
      backStackRecord.addOp(op);
    } 
    backStackRecord.mTransition = this.mTransition;
    backStackRecord.mTransitionStyle = this.mTransitionStyle;
    backStackRecord.mName = this.mName;
    backStackRecord.mIndex = this.mIndex;
    backStackRecord.mAddToBackStack = true;
    backStackRecord.mBreadCrumbTitleRes = this.mBreadCrumbTitleRes;
    backStackRecord.mBreadCrumbTitleText = this.mBreadCrumbTitleText;
    backStackRecord.mBreadCrumbShortTitleRes = this.mBreadCrumbShortTitleRes;
    backStackRecord.mBreadCrumbShortTitleText = this.mBreadCrumbShortTitleText;
    backStackRecord.mSharedElementSourceNames = this.mSharedElementSourceNames;
    backStackRecord.mSharedElementTargetNames = this.mSharedElementTargetNames;
    backStackRecord.bumpBackStackNesting(1);
    return backStackRecord;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeIntArray(this.mOps);
    paramParcel.writeInt(this.mTransition);
    paramParcel.writeInt(this.mTransitionStyle);
    paramParcel.writeString(this.mName);
    paramParcel.writeInt(this.mIndex);
    paramParcel.writeInt(this.mBreadCrumbTitleRes);
    TextUtils.writeToParcel(this.mBreadCrumbTitleText, paramParcel, 0);
    paramParcel.writeInt(this.mBreadCrumbShortTitleRes);
    TextUtils.writeToParcel(this.mBreadCrumbShortTitleText, paramParcel, 0);
    paramParcel.writeStringList(this.mSharedElementSourceNames);
    paramParcel.writeStringList(this.mSharedElementTargetNames);
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\app\BackStackState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */