package android.support.v4.app;

import android.graphics.Rect;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class FragmentTransitionCompat21 {
  public static void addTargets(Object paramObject, ArrayList<View> paramArrayList) {
    paramObject = paramObject;
    if (paramObject instanceof TransitionSet) {
      paramObject = paramObject;
      int j = paramObject.getTransitionCount();
      for (int i = 0; i < j; i++)
        addTargets(paramObject.getTransitionAt(i), paramArrayList); 
    } else if (!hasSimpleTarget((Transition)paramObject) && isNullOrEmpty(paramObject.getTargets())) {
      int j = paramArrayList.size();
      for (int i = 0; i < j; i++)
        paramObject.addTarget(paramArrayList.get(i)); 
    } 
  }
  
  public static void addTransitionTargets(final Object enterTransition, Object paramObject2, final View container, final ViewRetriever inFragment, final View nonExistentView, EpicenterView paramEpicenterView, final Map<String, String> nameOverrides, final ArrayList<View> enteringViews, Map<String, View> paramMap1, final Map<String, View> renamedViews, ArrayList<View> paramArrayList2) {
    if (enterTransition != null || paramObject2 != null) {
      enterTransition = enterTransition;
      if (enterTransition != null)
        enterTransition.addTarget(nonExistentView); 
      if (paramObject2 != null)
        setSharedElementTargets(paramObject2, nonExistentView, paramMap1, paramArrayList2); 
      if (inFragment != null)
        container.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
              public boolean onPreDraw() {
                container.getViewTreeObserver().removeOnPreDrawListener(this);
                if (enterTransition != null)
                  enterTransition.removeTarget(nonExistentView); 
                View view = inFragment.getView();
                if (view != null) {
                  if (!nameOverrides.isEmpty()) {
                    FragmentTransitionCompat21.findNamedViews(renamedViews, view);
                    renamedViews.keySet().retainAll(nameOverrides.values());
                    for (Map.Entry entry : nameOverrides.entrySet()) {
                      String str = (String)entry.getValue();
                      View view1 = (View)renamedViews.get(str);
                      if (view1 != null)
                        view1.setTransitionName((String)entry.getKey()); 
                    } 
                  } 
                  if (enterTransition != null) {
                    FragmentTransitionCompat21.captureTransitioningViews(enteringViews, view);
                    enteringViews.removeAll(renamedViews.values());
                    enteringViews.add(nonExistentView);
                    FragmentTransitionCompat21.addTargets(enterTransition, enteringViews);
                  } 
                } 
                return true;
              }
            }); 
      setSharedElementEpicenter((Transition)enterTransition, paramEpicenterView);
    } 
  }
  
  public static void beginDelayedTransition(ViewGroup paramViewGroup, Object paramObject) {
    TransitionManager.beginDelayedTransition(paramViewGroup, (Transition)paramObject);
  }
  
  private static void bfsAddViewChildren(List<View> paramList, View paramView) {
    int i = paramList.size();
    if (!containedBeforeIndex(paramList, paramView, i)) {
      paramList.add(paramView);
      int j = i;
      while (true) {
        if (j < paramList.size()) {
          paramView = paramList.get(j);
          if (paramView instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup)paramView;
            int m = viewGroup.getChildCount();
            for (int k = 0; k < m; k++) {
              View view = viewGroup.getChildAt(k);
              if (!containedBeforeIndex(paramList, view, i))
                paramList.add(view); 
            } 
          } 
          j++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  public static Object captureExitingViews(Object paramObject, View paramView1, ArrayList<View> paramArrayList, Map<String, View> paramMap, View paramView2) {
    Object object = paramObject;
    if (paramObject != null) {
      captureTransitioningViews(paramArrayList, paramView1);
      if (paramMap != null)
        paramArrayList.removeAll(paramMap.values()); 
      if (paramArrayList.isEmpty())
        return null; 
    } else {
      return object;
    } 
    paramArrayList.add(paramView2);
    addTargets(paramObject, paramArrayList);
    return paramObject;
  }
  
  private static void captureTransitioningViews(ArrayList<View> paramArrayList, View paramView) {
    ViewGroup viewGroup;
    if (paramView.getVisibility() == 0) {
      if (paramView instanceof ViewGroup) {
        viewGroup = (ViewGroup)paramView;
        if (viewGroup.isTransitionGroup()) {
          paramArrayList.add(viewGroup);
          return;
        } 
        int j = viewGroup.getChildCount();
        int i = 0;
        while (true) {
          if (i < j) {
            captureTransitioningViews(paramArrayList, viewGroup.getChildAt(i));
            i++;
            continue;
          } 
          return;
        } 
      } 
    } else {
      return;
    } 
    paramArrayList.add(viewGroup);
  }
  
  public static void cleanupTransitions(final View sceneRoot, final View nonExistentView, final Object enterTransition, final ArrayList<View> enteringViews, final Object exitTransition, final ArrayList<View> exitingViews, final Object sharedElementTransition, final ArrayList<View> sharedElementTargets, final Object overallTransition, final ArrayList<View> hiddenViews, final Map<String, View> renamedViews) {
    enterTransition = enterTransition;
    exitTransition = exitTransition;
    sharedElementTransition = sharedElementTransition;
    overallTransition = overallTransition;
    if (overallTransition != null)
      sceneRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
              sceneRoot.getViewTreeObserver().removeOnPreDrawListener(this);
              if (enterTransition != null)
                FragmentTransitionCompat21.removeTargets(enterTransition, enteringViews); 
              if (exitTransition != null)
                FragmentTransitionCompat21.removeTargets(exitTransition, exitingViews); 
              if (sharedElementTransition != null)
                FragmentTransitionCompat21.removeTargets(sharedElementTransition, sharedElementTargets); 
              for (Map.Entry entry : renamedViews.entrySet())
                ((View)entry.getValue()).setTransitionName((String)entry.getKey()); 
              int j = hiddenViews.size();
              for (int i = 0; i < j; i++)
                overallTransition.excludeTarget(hiddenViews.get(i), false); 
              overallTransition.excludeTarget(nonExistentView, false);
              return true;
            }
          }); 
  }
  
  public static Object cloneTransition(Object paramObject) {
    Object object = paramObject;
    if (paramObject != null)
      object = ((Transition)paramObject).clone(); 
    return object;
  }
  
  private static boolean containedBeforeIndex(List<View> paramList, View paramView, int paramInt) {
    for (int i = 0; i < paramInt; i++) {
      if (paramList.get(i) == paramView)
        return true; 
    } 
    return false;
  }
  
  public static void excludeTarget(Object paramObject, View paramView, boolean paramBoolean) {
    ((Transition)paramObject).excludeTarget(paramView, paramBoolean);
  }
  
  public static void findNamedViews(Map<String, View> paramMap, View paramView) {
    if (paramView.getVisibility() == 0) {
      String str = paramView.getTransitionName();
      if (str != null)
        paramMap.put(str, paramView); 
      if (paramView instanceof ViewGroup) {
        ViewGroup viewGroup = (ViewGroup)paramView;
        int j = viewGroup.getChildCount();
        for (int i = 0; i < j; i++)
          findNamedViews(paramMap, viewGroup.getChildAt(i)); 
      } 
    } 
  }
  
  private static Rect getBoundsOnScreen(View paramView) {
    Rect rect = new Rect();
    int[] arrayOfInt = new int[2];
    paramView.getLocationOnScreen(arrayOfInt);
    rect.set(arrayOfInt[0], arrayOfInt[1], arrayOfInt[0] + paramView.getWidth(), arrayOfInt[1] + paramView.getHeight());
    return rect;
  }
  
  public static String getTransitionName(View paramView) {
    return paramView.getTransitionName();
  }
  
  private static boolean hasSimpleTarget(Transition paramTransition) {
    return (!isNullOrEmpty(paramTransition.getTargetIds()) || !isNullOrEmpty(paramTransition.getTargetNames()) || !isNullOrEmpty(paramTransition.getTargetTypes()));
  }
  
  private static boolean isNullOrEmpty(List paramList) {
    return (paramList == null || paramList.isEmpty());
  }
  
  public static Object mergeTransitions(Object paramObject1, Object paramObject2, Object paramObject3, boolean paramBoolean) {
    boolean bool2 = true;
    Transition transition = (Transition)paramObject1;
    paramObject1 = paramObject2;
    paramObject3 = paramObject3;
    boolean bool1 = bool2;
    if (transition != null) {
      bool1 = bool2;
      if (paramObject1 != null)
        bool1 = paramBoolean; 
    } 
    if (bool1) {
      paramObject2 = new TransitionSet();
      if (transition != null)
        paramObject2.addTransition(transition); 
      if (paramObject1 != null)
        paramObject2.addTransition((Transition)paramObject1); 
      if (paramObject3 != null)
        paramObject2.addTransition((Transition)paramObject3); 
      return paramObject2;
    } 
    paramObject2 = null;
    if (paramObject1 != null && transition != null) {
      paramObject1 = (new TransitionSet()).addTransition((Transition)paramObject1).addTransition(transition).setOrdering(1);
    } else if (paramObject1 == null) {
      paramObject1 = paramObject2;
      if (transition != null)
        paramObject1 = transition; 
    } 
    if (paramObject3 != null) {
      paramObject2 = new TransitionSet();
      if (paramObject1 != null)
        paramObject2.addTransition((Transition)paramObject1); 
      paramObject2.addTransition((Transition)paramObject3);
      return paramObject2;
    } 
    return paramObject1;
  }
  
  public static void removeTargets(Object paramObject, ArrayList<View> paramArrayList) {
    paramObject = paramObject;
    if (paramObject instanceof TransitionSet) {
      paramObject = paramObject;
      int j = paramObject.getTransitionCount();
      for (int i = 0; i < j; i++)
        removeTargets(paramObject.getTransitionAt(i), paramArrayList); 
    } else if (!hasSimpleTarget((Transition)paramObject)) {
      List list = paramObject.getTargets();
      if (list != null && list.size() == paramArrayList.size() && list.containsAll(paramArrayList))
        for (int i = paramArrayList.size() - 1; i >= 0; i--)
          paramObject.removeTarget(paramArrayList.get(i));  
    } 
  }
  
  public static void setEpicenter(Object paramObject, View paramView) {
    ((Transition)paramObject).setEpicenterCallback(new Transition.EpicenterCallback(getBoundsOnScreen(paramView)) {
          public Rect onGetEpicenter(Transition param1Transition) {
            return epicenter;
          }
        });
  }
  
  private static void setSharedElementEpicenter(Transition paramTransition, final EpicenterView epicenterView) {
    if (paramTransition != null)
      paramTransition.setEpicenterCallback(new Transition.EpicenterCallback() {
            private Rect mEpicenter;
            
            public Rect onGetEpicenter(Transition param1Transition) {
              if (this.mEpicenter == null && epicenterView.epicenter != null)
                this.mEpicenter = FragmentTransitionCompat21.getBoundsOnScreen(epicenterView.epicenter); 
              return this.mEpicenter;
            }
          }); 
  }
  
  public static void setSharedElementTargets(Object paramObject, View paramView, Map<String, View> paramMap, ArrayList<View> paramArrayList) {
    paramObject = paramObject;
    paramArrayList.clear();
    paramArrayList.addAll(paramMap.values());
    List<View> list = paramObject.getTargets();
    list.clear();
    int j = paramArrayList.size();
    int i;
    for (i = 0; i < j; i++)
      bfsAddViewChildren(list, paramArrayList.get(i)); 
    paramArrayList.add(paramView);
    addTargets(paramObject, paramArrayList);
  }
  
  public static Object wrapSharedElementTransition(Object paramObject) {
    if (paramObject != null) {
      paramObject = paramObject;
      if (paramObject != null) {
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition((Transition)paramObject);
        return transitionSet;
      } 
    } 
    return null;
  }
  
  public static class EpicenterView {
    public View epicenter;
  }
  
  public static interface ViewRetriever {
    View getView();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\android\support\v4\app\FragmentTransitionCompat21.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */