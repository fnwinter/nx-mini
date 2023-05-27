package org.cybergarage.http;

import java.util.Vector;

public class ParameterList extends Vector {
  public Parameter at(int paramInt) {
    return (Parameter)get(paramInt);
  }
  
  public Parameter getParameter(int paramInt) {
    return (Parameter)get(paramInt);
  }
  
  public Parameter getParameter(String paramString) {
    if (paramString == null)
      return null; 
    int j = size();
    int i = 0;
    while (true) {
      if (i >= j)
        return null; 
      Parameter parameter2 = at(i);
      Parameter parameter1 = parameter2;
      if (paramString.compareTo(parameter2.getName()) != 0) {
        i++;
        continue;
      } 
      return parameter1;
    } 
  }
  
  public String getValue(String paramString) {
    Parameter parameter = getParameter(paramString);
    return (parameter == null) ? "" : parameter.getValue();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\http\ParameterList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */