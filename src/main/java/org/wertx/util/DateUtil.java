package org.wertx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;

/**
* @author  瞿建军      
* E-mail:jianjun.qu@istuary.com
* 
* 创建时间： 2016年6月12日  下午5:43:43
* 
*/
public class DateUtil {
	
	
	public static void main(String[] args) {
		
		System.out.println("today:" + today());
		System.out.println("2004/3/4:" + str2Date("2004-03-04"));
		System.out.println("now day:" + DateUtil.today(8));
		System.out.println("now day:" + DateUtil.today(4));
		System.out.println("day to string:" + DateUtil.date2Str(DateUtil.today()));
		if (((String) null) == null) {
			System.out.println("yes,still is null");
		} else {
			System.out.println("no,is a string 'null'");
		}
		System.out.println(((String) null));
		System.out.println("this year:" + thisYear());
		System.out.println("java sql date:" + new java.sql.Date(System.currentTimeMillis()));

		System.out.println(
			"util to sql:"
				+ DateUtil.date2Str(DateUtil.utilToSql(new java.util.Date()), "yyyy-MM-dd hh:mm:ss"));
		System.out.println(
			"sql to util:"
				+ DateUtil.date2Str(
					DateUtil.sqlToUtil(new java.sql.Date(System.currentTimeMillis())),
					"yyyy-MM-dd hh:mm:ss"));

		System.out.println("first day:" + DateUtil.firstDayOfMonth(new java.util.Date()));
		System.out.println("last day:" + DateUtil.lastDayOfMonth(new java.util.Date()));

		System.out.println("first day:" + DateUtil.firstDayOfMonth("2000-02-09"));
		System.out.println("last day:" + DateUtil.lastDayOfMonth("2001-02-09"));

		System.out.println("first day of quarter:" + DateUtil.firstDayOfQuarter(new java.util.Date()));
		System.out.println("last day of quarter:" + DateUtil.lasstDayOfQuarter(new java.util.Date()));
		

		System.out.println("test date:"+(DateUtil.is1After2(DateUtil.str2Date("2000-02-01"),DateUtil.str2Date("2000-03-31"))));

		System.out.println("next date:"+DateUtil.nextDay(DateUtil.str2Date("2000-12-31")));
		
		System.out.println("to timestamp:"+DateUtil.str2Time("2000-12-31 12:12:12"));
		
		
		System.out.println("first day of year:" + DateUtil.firstDayOfYear(new java.util.Date()));
		System.out.println("last day year:" + DateUtil.lastDayOfYear(new java.util.Date()));
		
		System.out.println("2000-12-31 to timestamp :"+DateUtil.str2Time(" 2000-02-02 "));
		System.out.println("2000-12-31 13:13:13 to timestamp:"+DateUtil.str2Time("2000-12-31 13:13:13 "));
		System.out.println("2000-12-31 13:13:13.4 to timestamp:"+DateUtil.str2Time("2000-12-31 13:13:13.4"));
		
		System.out.println("is same day? 2000-12-31 to 2000-12-31 "+DateUtil.isSameDay(DateUtil.str2Date("2000-12-31"), DateUtil.str2Date("2000-12-31")));
		System.out.println("is same day? 2000-12-31 to 2000-12-30 "+DateUtil.isSameDay(DateUtil.str2Date("2000-12-30"), DateUtil.str2Date("2000-12-31")));
		
		System.out.println("2000-5-6 to timestamp :"+DateUtil.str2Time(" 2000-05-06 "));
		System.out.println("toGMT :"+DateUtil.toGMT(DateUtil.now()));
		System.out.println("fromGMT :"+DateUtil.fromGMT(DateUtil.toGMT(DateUtil.now())));
		System.out.println(DateUtil.now()+" nextMonth :"+DateUtil.nextMonth(DateUtil.now()));
		
		System.out.println("2011-04-18:"+DateUtil.daysFromToday(DateUtil.str2Date("2011-04-18")));
		System.out.println("2011-04-20:"+DateUtil.daysFromToday(DateUtil.str2Date("2011-04-20")));
		
		System.out.println("2011-04-30:"+DateUtil.daysFromToday(DateUtil.str2Date("2011-04-30")));
		System.out.println("2011-05-01:"+DateUtil.daysFromToday(DateUtil.str2Date("2011-05-01")));
		System.out.println("today:"+DateUtil.daysFromToday(DateUtil.today()));
		System.out.println("10:"+DateUtil.date2Str(DateUtil.dayFromToday(10)));
		System.out.println("0:"+DateUtil.date2Str(DateUtil.dayFromToday(0)));
		
		System.out.println("month from day: 2011-03-01:"+DateUtil.monthsFormToday(DateUtil.str2Date("2011-03-01")));
		System.out.println("month from day: 2011-05-01:"+DateUtil.monthsFormToday(DateUtil.str2Date("2011-05-01")));
		System.out.println("month from day: 2010-12-01:"+DateUtil.monthsFormToday(DateUtil.str2Date("2010-12-01")));
		System.out.println("month from day: 2012-01-01:"+DateUtil.monthsFormToday(DateUtil.str2Date("2012-01-01")));
		System.out.println("month from day: 2013-01-01:"+DateUtil.monthsFormToday(DateUtil.str2Date("2013-01-01")));
		
		System.out.println("year from day: 2010-01-01:"+DateUtil.yearsFormToday(DateUtil.str2Date("2010-01-01")));
		System.out.println("year from day: 2011-01-01:"+DateUtil.yearsFormToday(DateUtil.str2Date("2011-01-01")));
		System.out.println("year from day: 2012-01-01:"+DateUtil.yearsFormToday(DateUtil.str2Date("2012-01-01")));
		
		System.out.println("month from day: 1:"+DateUtil.date2Str(DateUtil.monthFromToday(1,DateUtil.str2Date("2010-01-31"))));
		System.out.println("month from day: -3:"+DateUtil.date2Str(DateUtil.monthFromToday(-3,new Date())));
		System.out.println("year from day: -1:"+DateUtil.date2Str(DateUtil.yearFromToday(-1,new Date())));
		System.out.println("year from day: 1:"+DateUtil.date2Str(DateUtil.yearFromToday(1,new Date())));
		
		System.out.println("relate from day:"+DateUtil.date2Str(relateFromToday(DateUtil.str2Date("2011-03-04"),
				                                                 DateUtil.str2Date("2011-03-12"),0)));
		
		System.out.println("relate from month:"+DateUtil.date2Str(relateFromToday(DateUtil.str2Date("2010-03-12"),
                                                                 DateUtil.str2Date("2010-02-28"),1)));
		
		System.out.println("relate from year:"+DateUtil.date2Str(relateFromToday(DateUtil.str2Date("2011-03-04"),
                                                                 DateUtil.str2Date("2012-03-12"),2)));
		
		System.out.println("test 1:"+DateUtil.str2Date("2120-10-18"));
		
		System.out.println("isRegular:"+DateUtil.isRegular("2120-30-98"));
		System.out.println("isRegular:"+DateUtil.isRegular("2120-3-98"));
		System.out.println("currentTime" +DateUtil.currentTime());
		System.out.println("now 19: " +DateUtil.now(19));
		System.out.println("now 14: " +DateUtil.now(14));
		System.out.println("now 14: " +DateUtil.now(0));
		System.out.println("hours form now:"+DateUtil.time2Str(DateUtil.hoursFromNow(19)));
	}
	
	public static boolean isRegular(String yyyy_mm_dd){
		if(yyyy_mm_dd==null || yyyy_mm_dd.length()!=10)
			return false;
		return yyyy_mm_dd.matches("^(\\d{1,4})(-)((0[1-9]?)|(1[0-2]?))(-)((0[1-9]?)|([1|2][0-9]?)|(30|31))$");		
	}
	
	public static Date relateFromToday(Date beginning,Date realDay,int onwhat){
		if(onwhat==0){  //相对日期
			int i=DateUtil.diffDay(beginning, realDay);
			return DateUtil.dayFromToday(i);
		}else if(onwhat==1){ //相对月份
			int i=DateUtil.diffMonth(beginning, realDay);
			return DateUtil.monthFromToday(i, realDay);
		}else if(onwhat==2){//相对年份
			int i=DateUtil.diffYear(beginning, realDay);
			return DateUtil.yearFromToday(i, realDay);
		}
		return realDay;
	}
	
	public static int daysFromToday(Date d){
		 return DateUtil.diffDay(DateUtil.today(), d);
	}
	
	public static int yearsFormToday(Date d){
		return diffYear(new Date(),d);
	}
	
	public static int monthsFormToday(Date d){
		 return diffMonth(new Date(),d);
	}
	
	public static int diffYear(Date start,Date end){
		 Calendar calstart = getCalendar(start);
//	     calstart.setTime(new Date());             
	     Calendar calend = getCalendar(end);    
	     int ys=calstart.get(Calendar.YEAR);
	     int ye=calend.get(Calendar.YEAR);
	     return ye-ys;
	}
	
	public static int diffMonth(Date start,Date end){
		 Calendar calstart = getCalendar(start);     
//	     calstart.setTime(new Date());             
	     Calendar calend = getCalendar(end);
	     int ys=calstart.get(Calendar.YEAR);
	     int ye=calend.get(Calendar.YEAR);
	     int ms=calstart.get(Calendar.MONTH);
	     int me=calend.get(Calendar.MONTH);
	     if(ys==ye)
	    	 return me-ms;
	     else{
	    	 if(ye>ys){
	    		 int yepast=me;
	    		 int yspast=12-ms;
	    		 int yearPast=(ye-ys-1)*12;
	    		 return yearPast+yepast+yspast;
	    	 }else{
	    		 int yepast=12-me;
	    		 int yspast=ms;
	    		 int yearPast=(ys-ye-1)*12;
	    		 return -1*(yearPast+yepast+yspast);
	    	 }
	     }
	}
	
	public static int diffDay(Date start,Date end){
		 Calendar calstart = getCalendar(start);  
	     Calendar calend = getCalendar(end);    
	     int ys=calstart.get(Calendar.YEAR);
	     int ye=calend.get(Calendar.YEAR);
	     if(ys==ye &&  calstart.get(Calendar.DAY_OF_YEAR)==calend.get(Calendar.DAY_OF_YEAR))
	    	 return 0;
	     
	     long diffmillis=calend.getTimeInMillis()-calstart.getTimeInMillis();
	     long between_days=diffmillis/(1000*3600*24);
//	     System.out.println(between_days+"  "+diffmillis);
	     if(diffmillis>0)	    
	         return Integer.parseInt(String.valueOf(between_days))+1;
	     else
	    	 return Integer.parseInt(String.valueOf(between_days)); 
	}
	
	
	public static Date dayFromToday(int i){
		Calendar c = getCalendar(new Date());
		if(i!=0)
		   c.add(Calendar.DAY_OF_YEAR,i);
		return c.getTime();
	}
	
	public static Date monthFromToday(int i,Date onday){
		Calendar c = getCalendar();
		c.setTime(new Date());
		if(i!=0)
		   c.add(Calendar.MONTH,i);
		if(onday!=null){
			Calendar c2 = getCalendar(onday);
			int c2DayOfMonth=c2.get(Calendar.DAY_OF_MONTH);
			int c2DayMax=c2.getActualMaximum(Calendar.DAY_OF_MONTH);
			int cDayMax=c.getActualMaximum(Calendar.DAY_OF_MONTH);
			if(c2DayOfMonth==c2DayMax || cDayMax<c2DayOfMonth) //如果设置的日期是最大日期，或者目标月份最大日期小于要设置的日期，
				c.set(Calendar.DAY_OF_MONTH, cDayMax);
			else
			    c.set(Calendar.DAY_OF_MONTH, c2DayOfMonth);
		}
		return c.getTime();
	}
	
	public static Date yearFromToday(int i,Date onmontday){
		Calendar c = getCalendar(new Date());
		if(i!=0)
		   c.add(Calendar.YEAR,i);
		if(onmontday!=null){
			Calendar c2 = getCalendar(onmontday);
			c.set(Calendar.MONTH, c2.get(Calendar.MONTH));
			c.set(Calendar.DAY_OF_MONTH, c2.get(Calendar.DAY_OF_MONTH));
		}
		return c.getTime();
	}
	
	public static Date hoursFromNow(int i){
		Date now=new Date();
		if(i==0)
			return now;
		Calendar c = getCalendar();
		c.setTime(now);
		c.add(Calendar.HOUR_OF_DAY,i);
		return c.getTime();
	}

	private static Calendar getCalendar(){
		return Calendar.getInstance();		
	}
	
	private static Calendar getCalendar(Date d){
		Calendar c= Calendar.getInstance();
		if(d!=null)
		   c.setTime(d);
		return c;
	}
	
	//=============================================================
	

	
	//=============================================================
	
	/**
	 * Param must in format of "YYYY-MM-DD"
	 * date("2000-09-08")  => java.sql.Date [2000-09-08 00:00:00]
	 * @param yyyymmdd  a String that denote a day
	 * @return java.sql.Date 
	 */
	public static java.sql.Date str2Date(String yyyymmdd) {
		if(yyyymmdd==null || yyyymmdd.length()==0)
			return null;
		return java.sql.Date.valueOf(yyyymmdd.trim());
	}
	
	/**
	 * Param must in format of "YYYY-MM-DD HH:mm:ss"
	 * date("2000-09-08")  => java.sql.Date [2000-09-08 00:00:00]
	 * @param yyyymmdd  a String that denote a day
	 * @return java.sql.Date 
	 */
	public static java.sql.Timestamp str2Time(String yyyyMMddHHmmss) {
		if(yyyyMMddHHmmss==null)
			return null;
		yyyyMMddHHmmss=yyyyMMddHHmmss.trim();
		if(yyyyMMddHHmmss.length()<=10)
			yyyyMMddHHmmss=yyyyMMddHHmmss+" 00:00:00";
		return java.sql.Timestamp.valueOf(yyyyMMddHHmmss);
	}

	/**
	 *  java.util.Date [2000-09-08] => "2000-09-08"
	 *  java.sql.Date  [2000-09-08] => "2000-09-08"
	 * @param d
	 * @return
	 */
	public static String date2Str(java.util.Date d) {		
		if(d==null)
			return null;
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");		
		f.setLenient(false);
		return f.format(d);
	}
	
	/**
	 *  java.util.Date [2000-09-08] => "2000-09-08"
	 *  java.sql.Date  [2000-09-08] => "2000-09-08"
	 * @param d
	 * @return
	 */
	public static String year2Str(java.util.Date d) {		
		if(d==null)
			return null;
		SimpleDateFormat f = new SimpleDateFormat("yyyy");		
		f.setLenient(false);
		return f.format(d);
	}
	
	public static String dateObj2Str(Object d) {
		if(d==null)
			return null;
		if(d instanceof java.util.Date){			
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");		
			f.setLenient(false);
			return f.format((java.util.Date)d);
		}
		return null;
	}

	/**
	 * patten char: y M d h m s 
	 * @param d
	 * @param patten
	 * @return
	 */
	public static String date2Str(java.util.Date d, String patten) {	
		if(d==null)
			return null;
		SimpleDateFormat f = new SimpleDateFormat(patten);
		f.setLenient(false);
		return f.format(d);
	}
	
	public static String dateObj2Str(Object d, String patten) {
		if(d==null)
			return null;
		if(d instanceof java.util.Date){			
			SimpleDateFormat f = new SimpleDateFormat(patten);		
			f.setLenient(false);
			return f.format((java.util.Date)d);
		}
		return null;
	}
	

	/**
	 * java.sql.Timestamp [2000-09-08 11:09:23] =>"2000-09-08 11:09:23"
	 * @param d
	 * @return
	 */
	public static String time2Str(java.util.Date d) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		f.setLenient(false);
		return f.format(d);
	}
	
	public static String timeObj2Str(Object d) {
		if(d==null)
			return null;
		if(d instanceof java.util.Date){	
			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			f.setLenient(false);
			return f.format((java.util.Date)d);
		}
		return null;
	}
	
	public static String toGMT(java.util.Date d){
	   SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
	   Calendar cal = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
	   
	   format.setCalendar(cal);
	   format.setLenient(false);
	   
	   return format.format(d);
	}
	
	public static java.util.Date fromGMT(String d){
		 try {
		   SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z",Locale.US);
		   Calendar cal = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));		   
		   format.setCalendar(cal);
		   format.setLenient(false);		  
		   return format.parse(d);
		} catch (ParseException e) {
		   e.printStackTrace();
		   return null;
		}
	}

	/**
	 * return today ,in java.sql.Date
	 * if want java.util.Date, you can use "new java.util.Date()"
	 * @return
	 */
	public static java.util.Date today() {		
		return now();
	}
	
	/**
	 * return current time
	 * @return
	 */
	public static java.util.Date now(){
		//return new Date(System.currentTimeMillis());
		//return Calendar.getInstance(new SimpleTimeZone(28800000, "China/Beijing")).getTime();
		Date d=getCalendar().getTime();
		return d;
	}
	
	public static String currentTime(){
		//return new Date(System.currentTimeMillis());
		//return Calendar.getInstance(new SimpleTimeZone(28800000, "China/Beijing")).getTime();
		Calendar d=getCalendar();
		int h=d.get(Calendar.HOUR_OF_DAY);
		int m=d.get(Calendar.MINUTE);
		int s=d.get(Calendar.SECOND);
		return 
				(h<10?("0"+h):h)+":"+ (m<10?("0"+m):m) +":"+ (s<10?("0"+s):s);
	}
	
	
	/**
	 * bit=8   20001108
	 * bit=6   001108
	 * @param bit
	 * @return
	 */
	public static String today(int bit) {
		SimpleDateFormat f;
		if (bit == 10) {
			f = new SimpleDateFormat("yyyy-MM-dd");
		} else if(bit==8){
			f = new SimpleDateFormat("yyyyMMdd");
		}else{
			f = new SimpleDateFormat("yyMMdd");
		}
		f.setLenient(false);
		return f.format(DateUtil.today());

	}
	
	/**
	 * bit=19   2014-05-14 17:02:35
	 * bit=14   20140514170235
	 * other    17:02:35
	 * @param bit
	 * @return
	 */
	public static String now(int bit){
		SimpleDateFormat f;
		if (bit == 19) {
			f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else if(bit==14){
			f = new SimpleDateFormat("yyyyMMddHHmmss");
		}else{
			f = new SimpleDateFormat("HH:mm:ss");
		}
		f.setLenient(false);
		return f.format(DateUtil.today());
			
	}
	/**
	 * return a int represent this year
	 * 2000-09-08 will return 2000
	 * @return
	 */
	public static int thisYear() {
		GregorianCalendar c = new GregorianCalendar();
		return c.get(Calendar.YEAR);
	}

	/**
	 * transform java.sql.Date to java.util.Date format
	 * @param sqldate
	 * @return
	 */
	public static java.util.Date sqlToUtil(java.sql.Date sqldate) {
		if (sqldate == null) {
			return null;
		}
		return (java.util.Date) sqldate;

	}

	/**
	 * transform java.util.Date to java.sql.Date format
	 * @param utildate
	 * @return
	 */
	public static java.sql.Date utilToSql(java.util.Date utildate) {
		if (utildate == null) {
			return null;
		}
		return new java.sql.Date(utildate.getTime());
	}

	/**
	 * ����ĳ�����ڣ����ظ�����������·ݵĵ�һ��
	 * 2004-01-09  ->> 2004-02-01  
	 * @param d
	 * @return
	 */
	public static java.util.Date firstDayOfMonth(java.util.Date d) {
		Calendar c = getCalendar();
		c.setTime(d);
		c.set(Calendar.DAY_OF_MONTH, 1);
		return c.getTime();
	}
	
	/**
	 * 一年的第一天
	 * @param d
	 * @return
	 */
	public static java.util.Date firstDayOfYear(java.util.Date d) {
		Calendar c = getCalendar();
		c.setTime(d);
		c.set(Calendar.DAY_OF_YEAR, 1);
		return c.getTime();
	}

	/**
	 * @param date : should in "yyyy-MM-dd" patten
	 * @return :first day of the month, string is in "yyyy-MM-dd" patten 
	 */
	public static String firstDayOfMonth(String date) {
		return DateUtil.date2Str(DateUtil.firstDayOfMonth(DateUtil.str2Date(date)));
	}

	/**
	 * ����ĳ�����ڣ����ظ����������·ݵ����һ��
	 * 2000-02-09  ->>  2000-02-28
	 * 2001-02-09  ->>  2000-02-29 
	 * @param d
	 * @return
	 */
	public static java.util.Date lastDayOfMonth(java.util.Date d) {
		Calendar c = getCalendar();
		c.setTime(d);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}
	/**
	 * 一年的最后一天
	 * @param d
	 * @return
	 */
	public static java.util.Date lastDayOfYear(java.util.Date d) {
		Calendar c = getCalendar();
		c.setTime(d);
		c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));
		return c.getTime();
	}

	/**
	 * @param date : should in "yyyy-MM-dd" patten
	 * @return :first day of the month, string is in "yyyy-MM-dd" patten 
	 */
	public static String lastDayOfMonth(String date) {
		return DateUtil.date2Str(DateUtil.lastDayOfMonth(DateUtil.str2Date(date)));
	}
	
	/**
	 * ���ȵĵ�һ��
	 * @param d
	 * @return
	 */
	public static java.util.Date firstDayOfQuarter(java.util.Date d){
		Calendar c = getCalendar();
		c.setTime(d);
		int i=c.get(Calendar.MONTH);
		c.set(Calendar.MONTH,3*(i/3));
		return DateUtil.firstDayOfMonth(c.getTime());
	}
	
	/**
	 * ���ȵ����һ��
	 * @param d
	 * @return
	 */
	public static java.util.Date lasstDayOfQuarter(java.util.Date d){
		Calendar c = getCalendar();
		c.setTime(d);
		int i=c.get(Calendar.MONTH);
		c.set(Calendar.MONTH,2+3*(i/3));
		return DateUtil.lastDayOfMonth(c.getTime());
	}	
	
	/**
	 * 判断第一个时间是否比第二个时间晚，如果是，返回true,如果不时，返回false
	 */
	public static boolean is1After2(java.util.Date scrDate,java.util.Date destDate){
		Calendar c = getCalendar();
		c.setTime(scrDate);
		Calendar b=getCalendar();
		b.setTime(destDate);
		return c.after(b);
	}
	
	/**
	 * 判断是否是同一天
	 */
	public static boolean isSameDay(java.util.Date scrDate,java.util.Date destDate){
		Calendar c=getCalendar();
		c.setTime(scrDate);
		Calendar b=getCalendar();
		b.setTime(destDate);
		if(c.get(Calendar.YEAR)==b.get(Calendar.YEAR) && c.get(Calendar.DAY_OF_YEAR)==b.get(Calendar.DAY_OF_YEAR))
			return true;
		return false;
	}
		
	/**
	 * 判断是否同一小时、分钟
	 */
	public static boolean isSameHourMinutes(java.util.Date scrDate,java.util.Date destDate){
		Calendar c=getCalendar();
		c.setTime(scrDate);
		Calendar b=getCalendar();
		b.setTime(destDate);
		if(c.get(Calendar.HOUR_OF_DAY)==b.get(Calendar.HOUR_OF_DAY) && c.get(Calendar.MINUTE)==b.get(Calendar.MINUTE))
			return true;
		return false;
	}
	
	/**
	 * 判断是否同一小时
	 */
	public static boolean isSameHour(java.util.Date scrDate,java.util.Date destDate){
		Calendar c=getCalendar();
		c.setTime(scrDate);
		Calendar b=getCalendar();
		b.setTime(destDate);
		if(c.get(Calendar.HOUR_OF_DAY)==b.get(Calendar.HOUR_OF_DAY))
			return true;
		return false;
	}
	/**
	 * 判断是否同一分钟
	 */
	public static boolean isSameMinute(java.util.Date scrDate,java.util.Date destDate){
		Calendar c=getCalendar();
		c.setTime(scrDate);
		Calendar b=getCalendar();
		b.setTime(destDate);
		if(c.get(Calendar.MINUTE)==b.get(Calendar.MINUTE))
			return true;
		return false;
	}
		
	
	/**
	 * 下一天
	 */
	public static Date nextDay(Date d){
	   Calendar c=getCalendar();
	   c.setTime(d);
	   c.add(Calendar.DAY_OF_MONTH, 1);
	   return c.getTime();
	}
		
	/**
	 * 上一天
	 */
	public static Date preDay(Date d){
		   Calendar c=getCalendar();
		   c.setTime(d);
		   c.add(Calendar.DAY_OF_MONTH, -1);
		   return c.getTime();		
	}
	
	/**
	 * 下一个月
	 */
	public static Date nextMonth(Date d){
	   Calendar c=getCalendar();
	   c.setTime(d);
	   c.add(Calendar.MONTH, 1);
	   return c.getTime();
	}
	
	/**
	 * 下一年
	 */
	public static Date nextYear(Date d){
	   Calendar c=getCalendar();
	   c.setTime(d);
	   c.add(Calendar.YEAR, 1);
	   return c.getTime();
	}
	
	public static Date long2Time(Long timeStart){
		Date date = new Date(timeStart);
		return date;
	} 
	public static String long2TimeStr(Long timeStart,String patten){
		Date date = long2Time(timeStart);
		return date2Str(date,patten);
	} 

}
