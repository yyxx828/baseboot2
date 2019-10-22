package com.xajiusuo.jpa.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * 日期时间工具类
 * 
 * @author sunflower
 * 
 */
public class DateUtils {
	private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	private static final SimpleDateFormat datetimeMinsFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final SimpleDateFormat datetimeHoursFormat = new SimpleDateFormat("yyyy-MM-dd HH");

	/**
	 * 格式为yyyy-MM-dd
	 */
	public final static String dateFormateYYYYMMDD = "yyyy-MM-dd";

	/**
	 * 获得当前日期时间
	 * <p>
	 * 日期时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String currentDatetime() {
		return datetimeFormat.format(now());
	}

	/**
	 * 格式化日期时间
	 * <p>
	 * 日期时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String formatDatetime(Date date) {
		return datetimeFormat.format(date);
	}

	/**
	 * 格式化日期时间
	 * 
	 * @param date
	 * @param pattern
	 *            格式化模式，详见{@link SimpleDateFormat}构造器
	 *            <code>SimpleDateFormat(String pattern)</code>
	 * @return
	 */
	public static String formatDatetime(Date date, String pattern) {
		SimpleDateFormat customFormat = (SimpleDateFormat) datetimeFormat.clone();
		customFormat.applyPattern(pattern);
		return customFormat.format(date);
	}

	/**
	 * 获得当前日期
	 * <p>
	 * 日期格式yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String currentDate() {
		return dateFormat.format(now());
	}

	/**
	 * 格式化日期
	 * <p>
	 * 日期格式yyyy-MM-dd
	 * 
	 * @return
	 */
	public static String formatDate(Date date) {
		return dateFormat.format(date);
	}

	/**
	 * 获得当前时间
	 * <p>
	 * 时间格式HH:mm:ss
	 * 
	 * @return
	 */
	public static String currentTime() {
		return timeFormat.format(now());
	}

	/**
	 * 格式化时间
	 * <p>
	 * 时间格式HH:mm:ss
	 * 
	 * @return
	 */
	public static String formatTime(Date date) {
		return timeFormat.format(date);
	}

	/**
	 * 获得当前时间的<code>java.util.Date</code>对象
	 * 
	 * @return
	 */
	public static Date now() {
		return new Date();
	}

	public static Calendar calendar() {
		Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		return cal;
	}

	/**
	 * 获得当前时间的毫秒数
	 * <p>
	 * 详见{@link System#currentTimeMillis()}
	 * 
	 * @return
	 */
	public static long millis() {
		return System.currentTimeMillis();
	}

	/**
	 * 
	 * 获得当前Chinese月份
	 * 
	 * @return
	 */
	public static int month() {
		return calendar().get(Calendar.MONTH) + 1;
	}

	/**
	 * 获得月份中的第几天
	 * 
	 * @return
	 */
	public static int dayOfMonth() {
		return calendar().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 今天是星期的第几天
	 * 
	 * @return
	 */
	public static int dayOfWeek() {
		return calendar().get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 今天是年中的第几天
	 * 
	 * @return
	 */
	public static int dayOfYear() {
		return calendar().get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 判断原日期是否在目标日期之前
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	public static boolean isBefore(Date src, Date dst) {
		return src.before(dst);
	}

	/**
	 * 判断原日期是否在目标日期之后
	 * 
	 * @param src
	 * @param dst
	 * @return
	 */
	public static boolean isAfter(Date src, Date dst) {
		return src.after(dst);
	}

	/**
	 * 判断两日期是否相同
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static boolean isEqual(Date date1, Date date2) {
		return date1.compareTo(date2) == 0;
	}

	/**
	 * 判断某个日期是否在某个日期范围
	 * 
	 * @param beginDate
	 *            日期范围开始
	 * @param endDate
	 *            日期范围结束
	 * @param src
	 *            需要判断的日期
	 * @return
	 */
	public static boolean between(Date beginDate, Date endDate, Date src) {
		return beginDate.before(src) && endDate.after(src);
	}

	/**
	 * 获得当前月的最后一天
	 * <p>
	 * HH:mm:ss为0，毫秒为999
	 * 
	 * @return
	 */
	public static Date lastDayOfMonth() {
		Calendar cal = calendar();
		cal.set(Calendar.DAY_OF_MONTH, 0); // M月置零
		cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
		cal.set(Calendar.MINUTE, 0);// m置零
		cal.set(Calendar.SECOND, 0);// s置零
		cal.set(Calendar.MILLISECOND, 0);// S置零
		cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);// 月份+1
		cal.set(Calendar.MILLISECOND, -1);// 毫秒-1
		return cal.getTime();
	}

	/**
	 * 获得当前月的第一天
	 * <p>
	 * HH:mm:ss SS为零
	 * 
	 * @return
	 */
	public static Date firstDayOfMonth() {
		Calendar cal = calendar();
		cal.set(Calendar.DAY_OF_MONTH, 1); // M月置1
		cal.set(Calendar.HOUR_OF_DAY, 0);// H置零
		cal.set(Calendar.MINUTE, 0);// m置零
		cal.set(Calendar.SECOND, 0);// s置零
		cal.set(Calendar.MILLISECOND, 0);// S置零
		return cal.getTime();
	}

	private static Date weekDay(int week) {
		Calendar cal = calendar();
		cal.set(Calendar.DAY_OF_WEEK, week);
		return cal.getTime();
	}

	/**
	 * 获得周五日期
	 * <p>
	 * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
	 * 
	 * @return
	 */
	public static Date friday() {
		return weekDay(Calendar.FRIDAY);
	}

	/**
	 * 获得周六日期
	 * <p>
	 * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
	 * 
	 * @return
	 */
	public static Date saturday() {
		return weekDay(Calendar.SATURDAY);
	}

	/**
	 * 获得周日日期
	 * <p>
	 * 注：日历工厂方法{@link #calendar()}设置类每个星期的第一天为Monday，US等每星期第一天为sunday
	 * 
	 * @return
	 */
	public static Date sunday() {
		return weekDay(Calendar.SUNDAY);
	}

	/**
	 * 将字符串日期时间转换成java.util.Date类型
	 * <p>
	 * 日期时间格式yyyy-MM-dd HH:mm:ss
	 * 
	 * @param datetime
	 * @return
	 */
	public static Date parseDatetime(String datetime) throws ParseException {
		return datetimeFormat.parse(datetime);
	}

	/**
	 * 将字符串日期时间转换成java.util.Date类型
	 * <p>
	 * 日期时间格式yyyy-MM-dd HH:mm
	 * 
	 * @param datetime
	 * @return
	 */
	public static Date parseDatetimeMins(String datetime) throws ParseException {
		return datetimeMinsFormat.parse(datetime);
	}

	/**
	 * 将字符串日期时间转换成java.util.Date类型
	 * <p>
	 * 日期时间格式yyyy-MM-dd HH
	 * 
	 * @param datetime
	 * @return
	 */
	public static Date parseDatetimeHours(String datetime) throws ParseException {
		return datetimeHoursFormat.parse(datetime);
	}

	/**
	 * 将字符串日期转换成java.util.Date类型
	 * <p>
	 * 日期时间格式yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDate(String date)  {
		try {
			return dateFormat.parse(date);
		} catch (Exception e) {
			throw new RuntimeException("data pattern is [yyyy-MM-dd]");
		}
	}

	/**
	 * 将字符串日期转换成java.util.Date类型
	 * <p>
	 * 时间格式 HH:mm:ss
	 * 
	 * @param time
	 * @return
	 * @throws ParseException
	 */
	public static Date parseTime(String time) throws ParseException {
		return timeFormat.parse(time);
	}

	/**
	 * 根据自定义pattern将字符串日期转换成java.util.Date类型
	 * 
	 * @param datetime
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static Date parseDatetime(String datetime, String pattern) throws ParseException {
		SimpleDateFormat format = (SimpleDateFormat) datetimeFormat.clone();
		format.applyPattern(pattern);
		return format.parse(datetime);
	}

	/**
	 * @param
	 * @param lon
	 * @return
	 * @throws ParseException
	 */
	public static String currentDateAddSubHH(String date, Integer lon) throws ParseException {
		Date dd = null;
		dd = parseDatetime(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dd);
		cal.add(Calendar.HOUR_OF_DAY, lon);//
		Date time = cal.getTime();
		return formatDatetime(time);
	}

	/**
	 * 日期增加
	 * @param date yyyy-MM-dd
	 * @param addDay
	 * @return yyyy-MM-dd
	 */
	public static String dateAdds2s(String date, Integer addDay) {
		return formatDate(dateAdds2d(date,addDay));
	}

	/**
	 * 日期增加
	 * @param date yyyy-MM-dd
	 * @param addDay
	 * @return
	 */
	public static Date dateAdds2d(String date, Integer addDay) {
		Date dd = parseDate(date);
		return dateAddd2d(dd,addDay);
	}

	/**
	 * @param
	 * @param addDay
	 * @return
	 */
	public static Date dateAddd2d(Date date, Integer addDay) {
		return new Date(date.getTime() + ((long)addDay) * 24 * 60 * 60 * 1000);
	}

	/**
	 * @param
	 * @param addDay
	 * @return yyyy-MM-dd
	 */
	public static String dateAddd2s(Date date, Integer addDay) {
		return formatDate(dateAddd2d(date,addDay));
	}

	/**
	 * 获取当天的前一天日期
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static Date BeforDate(Date currentDate) {
		return dateAddd2d(currentDate,-1);
	}


	/**
	 * 日期增加
	 * @param date yyyy-MM-dd HH:mm:ss 否则错误
	 * @param addHour
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String hourAdds2s(String date, Integer addHour) {
		return datetimeFormat.format(hourAdds2d(date,addHour));
	}

	/**
	 * 日期增加
	 * @param date datetimeFormat 否则错误
	 * @param addHour
	 * @return
	 */
	public static Date hourAdds2d(String date, Integer addHour) {
		Date dd = parseDate(date);
		return hourAddd2d(dd,addHour);
	}

	/**
	 * @param
	 * @param addHour
	 * @return
	 */
	public static Date hourAddd2d(Date date, Integer addHour) {
		return new Date(date.getTime() + ((long)addHour) * 60 * 60 * 1000);
	}

	/**
	 * @param
	 * @param addHour
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String hourAddd2s(Date date, Integer addHour) {
		return datetimeFormat.format(hourAddd2d(date,addHour));
	}


	/**
	 * 
	 * @param date
	 *            格式为yyyy-MM-dd HH:mm
	 * @param lon
	 * @return
	 * @throws ParseException
	 */
	public static String currentDateHourBefor(String date, Integer lon) throws ParseException {

		Date sd = datetimeMinsFormat.parse(date);
		Calendar cl2 = Calendar.getInstance();
		cl2.setTime(sd);
		cl2.add(Calendar.HOUR_OF_DAY, lon);
		return datetimeMinsFormat.format(cl2.getTime());
	}

	public static boolean minsDates(String startDate, String endDate, String currentDate) throws ParseException {

		Calendar cal = Calendar.getInstance();
		cal.setTime(dateFormat.parse(startDate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(dateFormat.parse(endDate));
		long time2 = cal.getTimeInMillis();
		cal.setTime(dateFormat.parse(currentDate));
		long time3 = cal.getTimeInMillis();
		if (time3 > time1 && time3 < time2) {
			return true;
		} else {
			return false;
		}
	}


	public static String transNullToBlank(Object o) {
		if (o == null) {
			return "";
		}
		return o.toString();
	}

	/**
	 * 比较两个date
	 * 
	 * @param d1
	 * @param d2
	 * @return 较大的date对象
	 * @throws Exception
	 */
	public static Date compareDateMax(Date d1, Date d2) throws Exception {
		if (d1.getTime() - d2.getTime() == 0) {
			throw new Exception("不能比较两个相同的date!");
		}
		return (d1.getTime() - d2.getTime() > 0) ? d1 : d2;
	}

	/**
	 * 比较两个date
	 * 
	 * @param d1
	 * @param d2
	 * @return 较小的date对象
	 * @throws Exception
	 */
	public static Date compareDateMin(Date d1, Date d2) throws Exception {
		if (d1.getTime() - d2.getTime() == 0) {
			throw new Exception("不能比较两个相同的date!");
		}
		return (d1.getTime() - d2.getTime() > 0) ? d2 : d1;
	}

	/**
	 * 通过开始时间和结束时间获取需要查询的表名
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return 表名的List
	 */
	public static List<String> getDaysBetween(String startTime, String endTime) {
		List<String> list = new ArrayList<String>();
		try {
			Date sTime = DateUtils.parseDatetime(startTime);
			Date eTime = DateUtils.parseDatetime(endTime);
			Date max = DateUtils.compareDateMax(sTime, eTime);
			Date min = DateUtils.compareDateMin(sTime, eTime);
			Date tmpTime = null;
			Date bTime = null;
			list.add(DateUtils.formatDatetime(max, "yyyyMMdd"));
			while (true) {
				if (tmpTime == null) {
					bTime = DateUtils.BeforDate(max);
				} else {
					bTime = DateUtils.BeforDate(tmpTime);
				}
				if (bTime.before(min)) {
					break;
				}
				tmpTime = bTime;
				if (list.contains(DateUtils.formatDatetime(bTime, "yyyyMMdd"))) {
					continue;
				}
				list.add(DateUtils.formatDatetime(bTime, "yyyyMMdd"));
			}
			if (!list.contains(DateUtils.formatDatetime(min, "yyyyMMdd"))) {
				list.add(DateUtils.formatDatetime(min, "yyyyMMdd"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 通过开始时间和结束时间获取需要查询的表名
	 * 
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
	 * @return 表名的List
	 */
	public static Set<String> getDaysBetweenHhmm(String startTime, String endTime) {
		Set<String> set = new HashSet<String>();
		try {
			Date sTime = DateUtils.parseDatetimeMins(startTime);
			Date eTime = DateUtils.parseDatetimeMins(endTime);
			Date max = DateUtils.compareDateMax(sTime, eTime);
			Date min = DateUtils.compareDateMin(sTime, eTime);
			Date tmpTime = null;
			Date bTime = null;
			set.add(DateUtils.formatDatetime(max, "yyyyMMdd"));
			while (true) {
				if (tmpTime == null) {
					bTime = DateUtils.BeforDate(max);
				} else {
					bTime = DateUtils.BeforDate(tmpTime);
				}
				if (bTime.before(min)) {
					break;
				}
				tmpTime = bTime;
				set.add(DateUtils.formatDatetime(bTime, "yyyyMMdd"));
			}
			set.add(DateUtils.formatDatetime(min, "yyyyMMdd"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return set;
	}




	/**
	 * 获得默认的 date pattern
	 */
	public static String getDatePattern() {
		return P.S.fmtYmd14;
	}

	/**
	 * 返回预设Format的当前应用服务器运行的日期字符串。
	 */
	public static String getToday() {
		Date today = new Date();
		return format(today);
	}

	/**
	 * 使用默认Format格式化Date成字符串。
	 */
	public static String format(Date date) {
		return date == null ? "" : format(date, getDatePattern());
	}

	/**
	 * 使用参数Format格式化Date成字符串。如果参数date为null，则返回空字符串""。
	 *
	 * @param date 待转换日期。
	 * @param pattern 指定的转换格式。
	 */
	public static String format(Date date, String pattern) {
		return date == null ? "" : P.U.getSDF(pattern).format(date);
	}

	/**
	 * 使用预设格式将字符串解析为Date。
	 */
	public static Date parse(String strDate) {
		return StringUtils.isBlank(strDate) ? null : parse(strDate, getDatePattern());
	}

	/**
	 * 使用指定格式Format将字符串解析为Date。如果参数strDate为null，则返回null。
	 *
	 * @param strDate 待解析日期字符串。
	 * @param pattern 指定的转换格式。
	 */
	public static Date parse(String strDate, String pattern) {
		try {
			return StringUtils.isBlank(strDate) ? null : P.U.getSDF(pattern).parse(strDate);
		}catch(Exception e) {
			return null;
		}
	}

	/**
	 * 解析日期上限，要求传入格式为yyyy-MM-dd。 解析规则是天数加1，然后减去1秒。
	 */
	public static Date parseDate59(String strDate) throws ParseException {
		if(StringUtils.isBlank(strDate)) {
			return null;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(P.U.getSDF(P.S.fmtYmd14).parse(strDate));
		c.add(Calendar.DAY_OF_MONTH, 1);
		c.add(Calendar.SECOND, -1);
		Date d = c.getTime();
		c.clear();
		return d;
	}

	/**
	 * 在日期上增加数个整月。
	 *
	 * @param date 待操作日期。
	 * @param n 要增加的月数。
	 */
	public static Date addMonth(Date date, int n) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, n);
		date = cal.getTime();
		cal.clear();
		return date;
	}

	// ~ Methods
	// ================================================================

	/**
	 * 将一个日期转换成默认的日期格式。
	 *
	 * @param aDate 待转换日期。
	 * @return 格式化后的日期。
	 */
	public static final String getDate(Date aDate) {
		String returnValue = "";
		if(aDate != null) {
			returnValue = P.U.getSDF(getDatePattern()).format(aDate);
		}
		return returnValue;
	}

	/**
	 * 日期格式化方法。
	 *
	 * @param date 要格式化的日期。
	 * @param nFmt 格式化样式，使用<code>DateUtil.P.S.fmtYmd14</code>等来指定。
	 * @return
	 */
	public static String formatDate(Date date, String nFmt) {
		return P.U.getSDF(nFmt).format(date);
	}


	/**
	 * 将一个日期时间的字符串表示从inFormat格式转换为outFormat格式。
	 *
	 * @param dStr 日期时间的字符串。
	 * @param inFormat 原始格式。
	 * @param outFormat 转换后的格式。
	 * @return
	 */
	public static String convert(String dStr, String inFormat, String outFormat) {

		SimpleDateFormat sdf = P.U.getSDF(inFormat );
		Date d = null;
		try {
			d = sdf.parse(dStr);
		}catch(ParseException pe) {
		}

		return dateToString(d, outFormat);

	}

	/**
	 * 按给出的格式将输入的日期转换为字符串。
	 *
	 * @param currdate 输入的date对象。
	 * @param strFormat 约定的格式。
	 * @return 按输入时间及约定格式返回的字符串
	 */
	public static final String dateToString(java.util.Date currdate, String strFormat) {
		String returnDate = "";
		try {
			if(currdate == null)
				return returnDate;
			else
				returnDate = P.U.getSDF(strFormat).format(currdate);
		}catch(NullPointerException e) {
		}
		return returnDate;
	}

	/**
	 * 获取某一个日期在一年中是第几周的日期。
	 *
	 * 需要传递两个参数，第一个参数是要计算的日期，第二个参数指定周几作为一周的开始。 可以参见Calendar.SATURDAY等常量。
	 *
	 * @return
	 */
	public static int getWeekInYear(Date date, int startOfWeek) {
		Calendar cld = Calendar.getInstance();
		cld.setTime(date);
		cld.setFirstDayOfWeek(startOfWeek);
		int res = cld.get(Calendar.WEEK_OF_YEAR);
		cld.clear();
		return res;
	}

	/***
	 * 获得当前日期周一
	 * @param d
	 * @return
	 */
	public static Date getFistrDay(Date d){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.add(Calendar.DATE, -1 * c.get(Calendar.DAY_OF_WEEK) + 1);
		d = c.getTime();
		c.clear();
		return d;
	}

	/**
	 * 获取当前时间按和小时对应的时间
	 * @author 杨勇 2016年1月5日
	 * @param day 日期
	 * @param hm 时分
	 * @param pattern 格式
	 * @return 需要的时间
	 */
	public static Date getDate(Date day,String hm,String pattern){
		return P.U.dateParse(P.U.dateFormat(day, "yyyy-MM-dd") + " " + hm, pattern);
	}

	/**
	 * 日期比较 相同返回true，否则范围false
	 * @author 杨勇 2016年1月5日
	 * @return
	 */
	public static boolean deq(Date max,Date min,String pattern) {
		if(min == null){
			return true;
		}
		return P.U.dateFormat(max, pattern).compareTo(P.U.dateFormat(min, pattern)) == 0;
	}

	/**
	 * 日期比较 大返回true，否则范围false
	 * @author 杨勇 2016年1月5日
	 * @return
	 */
	public static boolean dgt(Date max,Date min,String pattern) {
		if(min == null){
			return true;
		}
		return P.U.dateFormat(max, pattern).compareTo(P.U.dateFormat(min, pattern)) > 0;
	}

	/**
	 * 日期比较 大于等于返回true，否则范围false
	 * @author 杨勇 2016年1月5日
	 * @return
	 */
	public static boolean dge(Date max,Date min,String pattern){
		if(min == null){
			return true;
		}
		return P.U.dateFormat(max, pattern).compareTo(P.U.dateFormat(min, pattern)) >= 0;
	}

	/**
	 * 日期比较 小于返回true，否则范围false
	 * @author 杨勇 2016年1月5日
	 * @return
	 */
	public static boolean dlt(Date min,Date max,String pattern){
		if(max == null){
			return true;
		}
		return P.U.dateFormat(min, pattern).compareTo(P.U.dateFormat(max, pattern)) < 0;
	}

	/**
	 * 日期比较 小于等于返回true，否则范围false
	 * @author 杨勇 2016年1月5日
	 * @return
	 */
	public static boolean dle(Date min,Date max,String pattern){
		if(max == null){
			return true;
		}
		return P.U.dateFormat(min, pattern).compareTo(P.U.dateFormat(max, pattern)) <= 0;
	}

	/***
	 * 获取该日期对应月第一个周日及后42天
	 * @author 杨勇 2016年1月6日
	 * @param seldate
	 * @return
	 */
	public static Date[] getDaysMonth(Date seldate){
		if(seldate == null){
			seldate = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(P.U.dateYMD(seldate));
		c.set(Calendar.DATE, 1);
		c.add(Calendar.DATE, -1 * c.get(Calendar.DAY_OF_WEEK) + 1);
		Date min = c.getTime();
		c.add(Calendar.DATE, 43);
		c.add(Calendar.SECOND, -1);
		Date max = c.getTime();
		c.clear();
		return new Date[]{min,max};
	}

	/***
	 * 获取该日期对应周周日及周六
	 * @author 杨勇 2016年1月6日
	 * @param seldate
	 * @return
	 */
	public static Date[] getDaysWeek(Date seldate){
		if(seldate == null){
			seldate = new Date();
		}
		Calendar c = Calendar.getInstance();
		c.setTime(P.U.dateYMD(seldate));
		c.add(Calendar.DATE, -1 * c.get(Calendar.DAY_OF_WEEK) + 1);
		Date min = c.getTime();
		c.add(Calendar.DATE, 8);
		c.add(Calendar.SECOND, -1);
		Date max = c.getTime();
		c.clear();
		return new Date[]{min,max};
	}

	/***
	 * 获取前i周后j周工i+j+1周时间
	 * @author 杨勇 2016年1月12日
	 * @param d
	 * @param i
	 * @param j
	 * @return
	 */
	public static Date[] getDays(Date d, int i, int j) {
		Calendar c = Calendar.getInstance();
		Date d1 =  P.U.getIndexDateWeek(d, Calendar.SUNDAY, false, false);
		Date d2 =  P.U.getIndexDateWeek(d, Calendar.SATURDAY, true, false);
		c.setTime(d1);
		c.add(Calendar.DATE, -7 * i);
		d1 = c.getTime();
		c.setTime(d2);
		c.add(Calendar.DATE, 7 * j);
		d2 = c.getTime();
		c.clear();
		return new Date[]{d1,d2};
	}

}
