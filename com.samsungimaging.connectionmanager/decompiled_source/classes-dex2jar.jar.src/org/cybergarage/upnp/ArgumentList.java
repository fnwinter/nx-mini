package org.cybergarage.upnp;

import java.util.Vector;

public class ArgumentList extends Vector {
  public static final String ELEM_NAME = "argumentList";
  
  public Argument getArgument(int paramInt) {
    return (Argument)get(paramInt);
  }
  
  public Argument getArgument(String paramString) {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return null; 
      Argument argument = getArgument(i);
      String str = argument.getName();
      if (str != null && str.equals(paramString))
        return argument; 
    } 
  }
  
  public void set(ArgumentList paramArgumentList) {
    int j = paramArgumentList.size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      Argument argument1 = paramArgumentList.getArgument(i);
      Argument argument2 = getArgument(argument1.getName());
      if (argument2 != null)
        argument2.setValue(argument1.getValue()); 
    } 
  }
  
  public void setReqArgs(ArgumentList paramArgumentList) {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      Argument argument = getArgument(i);
      if (argument.isInDirection()) {
        String str = argument.getName();
        Argument argument1 = paramArgumentList.getArgument(str);
        if (argument1 == null)
          throw new IllegalArgumentException("Argument \"" + str + "\" missing."); 
        argument.setValue(argument1.getValue());
      } 
    } 
  }
  
  public void setResArgs(ArgumentList paramArgumentList) {
    int j = size();
    for (int i = 0;; i++) {
      if (i >= j)
        return; 
      Argument argument = getArgument(i);
      if (argument.isOutDirection()) {
        String str = argument.getName();
        Argument argument1 = paramArgumentList.getArgument(str);
        if (argument1 == null)
          throw new IllegalArgumentException("Argument \"" + str + "\" missing."); 
        argument.setValue(argument1.getValue());
      } 
    } 
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarag\\upnp\ArgumentList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */