package com.samsungimaging.connectionmanager.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ImageView;
import com.samsungimaging.connectionmanager.gallery.GalleryFragment;

public class Utils {
  public static final String ACTION_DISCONNECT = "action_disconnect";
  
  public static Fragment findFragment(Activity paramActivity, Class<?> paramClass) {
    GalleryFragment galleryFragment1;
    FragmentManager fragmentManager = paramActivity.getFragmentManager();
    Fragment fragment = fragmentManager.findFragmentByTag(paramClass.getSimpleName());
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    if (fragment == null) {
      if (paramClass == GalleryFragment.class)
        galleryFragment1 = new GalleryFragment(); 
      fragmentTransaction.add(16908290, (Fragment)galleryFragment1, paramClass.getSimpleName());
      GalleryFragment galleryFragment = galleryFragment1;
      fragmentTransaction.commit();
      return (Fragment)galleryFragment;
    } 
    GalleryFragment galleryFragment2 = galleryFragment1;
    if (galleryFragment1.isHidden()) {
      fragmentTransaction.show((Fragment)galleryFragment1);
      galleryFragment2 = galleryFragment1;
    } 
    fragmentTransaction.commit();
    return (Fragment)galleryFragment2;
  }
  
  public static String getCameraMediaQuerySelection() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("(");
    stringBuilder.append("media_type=1");
    stringBuilder.append(" OR media_type=3");
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public static String getLocalMediaQuerySelection() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("_data LIKE ?");
    stringBuilder.append(" AND (");
    stringBuilder.append("media_type=1");
    stringBuilder.append(" OR media_type=3");
    stringBuilder.append(")");
    return stringBuilder.toString();
  }
  
  public static String[] getMediaGroupQueryProjection() {
    return new String[] { "_id", "date((datetaken / 1000), 'unixepoch', 'localtime') as datetaken_string", "media_type", "count(*) as _count" };
  }
  
  @SuppressLint({"DefaultLocale"})
  public static String getMimeType(String paramString) {
    paramString = MimeTypeMap.getFileExtensionFromUrl(paramString.toLowerCase());
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(paramString);
  }
  
  public static void hideFragment(Activity paramActivity, Fragment paramFragment) {
    FragmentTransaction fragmentTransaction = paramActivity.getFragmentManager().beginTransaction();
    if (paramFragment.isAdded() || paramFragment.isVisible())
      fragmentTransaction.hide(paramFragment); 
    fragmentTransaction.commit();
  }
  
  public static void showFragment(Activity paramActivity, Fragment paramFragment) {
    FragmentTransaction fragmentTransaction = paramActivity.getFragmentManager().beginTransaction();
    if (paramFragment.isAdded() || paramFragment.isHidden())
      fragmentTransaction.show(paramFragment); 
    fragmentTransaction.commit();
  }
  
  public static void unbindView(View paramView) {
    AdapterView adapterView;
    if (paramView == null)
      return; 
    try {
      paramView.setOnClickListener(null);
    } catch (Exception exception) {}
    try {
      paramView.setOnCreateContextMenuListener(null);
    } catch (Exception exception) {}
    try {
      paramView.setOnFocusChangeListener(null);
    } catch (Exception exception) {}
    try {
      paramView.setOnKeyListener(null);
    } catch (Exception exception) {}
    try {
      paramView.setOnLongClickListener(null);
    } catch (Exception exception) {}
    try {
      paramView.setOnTouchListener(null);
    } catch (Exception exception) {}
    Drawable drawable = paramView.getBackground();
    if (drawable != null) {
      drawable.setCallback(null);
      paramView.setBackgroundDrawable(null);
    } 
    if (paramView instanceof ViewGroup) {
      ViewGroup viewGroup = (ViewGroup)paramView;
      viewGroup.setOnHierarchyChangeListener(null);
      int i = 0;
      while (true) {
        if (i >= viewGroup.getChildCount()) {
          if (paramView instanceof AdapterView) {
            adapterView = (AdapterView)paramView;
            adapterView.setOnItemClickListener(null);
            adapterView.setOnItemLongClickListener(null);
            adapterView.setOnItemSelectedListener(null);
            return;
          } 
        } else {
          unbindView(viewGroup.getChildAt(i));
          i++;
          continue;
        } 
        viewGroup.removeAllViews();
        return;
      } 
    } 
    if (adapterView instanceof ImageView) {
      ImageView imageView = (ImageView)adapterView;
      drawable = imageView.getDrawable();
      if (drawable != null) {
        drawable.setCallback(null);
        imageView.setImageDrawable(null);
      } 
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\com\samsungimaging\connectionmanage\\util\Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */