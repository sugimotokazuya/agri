package agri.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Util {
    
    public static Calendar getCalendar() {
        return Calendar.getInstance(TimeZone.getTimeZone("Asia/Tokyo"));
    }
    
    public static SimpleDateFormat getSimpleDateFormat(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.JAPANESE);
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Tokyo"));
        return sdf;
    }
    
    public static Date[] createOneDayRange(int year, int month, int day) {
        Calendar cal = getCalendar();
        resetTime(cal);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        Date[] dates = new Date[2];
        dates[0] = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        dates[1] = cal.getTime();
        return dates;
    }
    
    /**
     * dateの時間と分を結合して４桁もしくは3桁の数値にして返す
     * 例：0900, 1030
     * @param date
     * @return
     */
    public static int createTimeInt(Date date) {
        DecimalFormat df = new DecimalFormat("00");
        Calendar cal = Util.getCalendar();
        cal.setTime(date);
        StringBuilder sb = new StringBuilder(df.format(cal.get(Calendar.HOUR_OF_DAY)));
        sb.append(df.format(cal.get(Calendar.MINUTE)));
        return Integer.parseInt(sb.toString());
    }

    public static String trim(String orgStr) {
        if(orgStr == null) return null;
        char[] value = orgStr.toCharArray();
        int len = value.length;
        int st = 0;
        char[] val = value;
        
        while ((st < len) && (val[st] <= ' ' || val[st] == '　')) {
            st++;
        }
        while ((st < len) && (val[len - 1] <= ' ' || val[len - 1] == '　')) {
            len--;
        }
        
        return ((st>0) || (len<value.length)) ? orgStr.substring(st,len):orgStr;
    }
    
    public static boolean isEmpty(String str) {
        if(str == null) return true;
        if(str.length() == 0) return true;
        return false;
    }
    
    public static void resetTime(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }
    
    public static void resetSecond(Calendar cal) {
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }
    
    public static Date[] createOneMonthRange(int year, int month) {
        
        Calendar start = Util.getCalendar();
        Util.resetTime(start);
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, month);
        start.set(Calendar.DAY_OF_MONTH, 1);
        
        Calendar end = Util.getCalendar();
        Util.resetTime(end);
        end.setTime(start.getTime());
        end.add(Calendar.MONTH, 1);
        
        return new Date[] {start.getTime(), end.getTime()};
    }
    
    public static Date[] createOneMonthRange() {
        Calendar cal = Util.getCalendar();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        return createOneMonthRange(year, month);
    }
    
    public static int[] createYearRange(int year) {
        Calendar cal = Util.getCalendar();
        int thisYear = cal.get(Calendar.YEAR);
        int count = thisYear - year + 1;
        int[] years = new int[count];
        for(int i = 0; i < count; ++i) {
            years[i] = thisYear - i;
        }
        return years;
    }
    
    public static Date[] createOneYearRange(int year) {
        Calendar start = Util.getCalendar();
        start.clear();
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, 0);
        start.set(Calendar.DAY_OF_MONTH, 1);
        Calendar end = Util.getCalendar();
        end.setTimeInMillis(start.getTimeInMillis());
        end.set(Calendar.YEAR, start.get(Calendar.YEAR) + 1);
        
        return new Date[] {start.getTime(), end.getTime()};
    }
    
    public static Date[] createOneYearNendoRange(int year) {
        Calendar start = Util.getCalendar();
        start.clear();
        start.set(Calendar.YEAR, year);
        start.set(Calendar.MONTH, 3);
        start.set(Calendar.DAY_OF_MONTH, 1);
        Calendar end = Util.getCalendar();
        end.setTimeInMillis(start.getTimeInMillis());
        end.set(Calendar.YEAR, start.get(Calendar.YEAR) + 1);
        
        return new Date[] {start.getTime(), end.getTime()};
    }
    
    /**
     * aよりbが大きい前提とする
     * @param a
     * @param b
     * @return
     */
    public static int getDiffMinute(Date a, Date b) {
        Calendar calA = Util.getCalendar();
        calA.setTime(a);
        Calendar calB = Util.getCalendar();
        calB.setTime(b);
        int minuteB = calB.get(Calendar.HOUR_OF_DAY) * 60 + calB.get(Calendar.MINUTE);
        int minuteA = calA.get(Calendar.HOUR_OF_DAY) * 60 + calA.get(Calendar.MINUTE);
        return minuteB - minuteA;
    }
    
    /**
     * M/SからHRDへの移行でのBlobkeyのヒモ付用メソッド
     * @param oldBlobKey
     * @return
     */
    public static String getNewBlobKey(String oldBlobKey) {
        String newBlobKey = "";
        try {
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            Key key = KeyFactory.createKey("__BlobMigration__", oldBlobKey);
            Entity blobkey = datastore.get(key);
            BlobKey blobKey = (BlobKey) blobkey.getProperty("new_blob_key");
            newBlobKey = blobKey.getKeyString();
        } catch (EntityNotFoundException e) {
            newBlobKey = oldBlobKey;
        }
        return newBlobKey;
    }
    
    /**
     * 引数のstrがnullの場合はブランクを返す
     * @param str
     * @return
     */
    public static String getStringNoNull(String str) {
        if(str == null) {
            return "";
        } else {
            return str;
        }
    }
   
}
