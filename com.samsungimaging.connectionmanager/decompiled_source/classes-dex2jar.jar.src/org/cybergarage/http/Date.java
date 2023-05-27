package org.cybergarage.http;

import java.util.Calendar;
import java.util.TimeZone;

public class Date {
  private static final String[] MONTH_STRING = new String[] { 
      "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", 
      "Nov", "Dec" };
  
  private static final String[] WEEK_STRING = new String[] { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
  
  private Calendar cal;
  
  public Date(Calendar paramCalendar) {
    this.cal = paramCalendar;
  }
  
  public static final Date getInstance() {
    return new Date(Calendar.getInstance(TimeZone.getTimeZone("GMT")));
  }
  
  public static final Date getLocalInstance() {
    return new Date(Calendar.getInstance());
  }
  
  public static final String toDateString(int paramInt) {
    return (paramInt < 10) ? ("0" + Integer.toString(paramInt)) : Integer.toString(paramInt);
  }
  
  public static final String toMonthString(int paramInt) {
    paramInt += 0;
    return (paramInt >= 0 && paramInt < 12) ? MONTH_STRING[paramInt] : "";
  }
  
  public static final String toTimeString(int paramInt) {
    String str = "";
    if (paramInt < 10)
      str = String.valueOf("") + "0"; 
    return String.valueOf(str) + Integer.toString(paramInt);
  }
  
  public static final String toWeekString(int paramInt) {
    return (--paramInt >= 0 && paramInt < 7) ? WEEK_STRING[paramInt] : "";
  }
  
  public Calendar getCalendar() {
    return this.cal;
  }
  
  public String getDateString() {
    Calendar calendar = getCalendar();
    return String.valueOf(toWeekString(calendar.get(7))) + ", " + toTimeString(calendar.get(5)) + " " + toMonthString(calendar.get(2)) + " " + Integer.toString(calendar.get(1)) + " " + toTimeString(calendar.get(11)) + ":" + toTimeString(calendar.get(12)) + ":" + toTimeString(calendar.get(13)) + " GMT";
  }
  
  public int getHour() {
    return getCalendar().get(11);
  }
  
  public int getMinute() {
    return getCalendar().get(12);
  }
  
  public int getSecond() {
    return getCalendar().get(13);
  }
  
  public String getTimeString() {
    Calendar calendar = getCalendar();
    StringBuilder stringBuilder = new StringBuilder(String.valueOf(toDateString(calendar.get(11))));
    if (calendar.get(13) % 2 == 0) {
      String str1 = ":";
      return stringBuilder.append(str1).append(toDateString(calendar.get(12))).toString();
    } 
    String str = " ";
    return stringBuilder.append(str).append(toDateString(calendar.get(12))).toString();
  }
}


/* Location:              C:\Users\fnwin\source\dev\nx-mini\com.samsungimaging.connectionmanager\classes-dex2jar.jar!\org\cybergarage\http\Date.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */