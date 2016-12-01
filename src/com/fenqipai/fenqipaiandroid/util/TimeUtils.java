package com.fenqipai.fenqipaiandroid.util;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.fenqipai.fenqipaiandroid.model.TimeInfo;

import android.annotation.SuppressLint;

public class TimeUtils {

	private TimeUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("TimeUtils cannot be instantiated");
	}

	/**
	 * 判断时间是否大于今天
	 * 
	 * @title isGreaterCurrentDay
	 * @author liuchengbao
	 * @param str
	 * @return
	 */
	public static boolean isGreaterCurrentDay(String str) {
		long l1 = parseStringToLong(str);
		TimeInfo localTimeInfo = getTodayStartAndEndTime();
		long l2 = localTimeInfo.getStartTime();

		if (l1 >= l2) {
			return true;
		}
		return false;
	}

	/**
	 * String 类型转Long
	 * 
	 * @param str
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static long parseStringToLong(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Long l = 0l;
		try {
			l = sdf.parse(str).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return l;
	}

	/**
	 * 将时间转换为yyyy-MM-dd HH:mm:ss
	 * 
	 * @title convertTimeToDay
	 * @author liuchengbao
	 * @param timeStr
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertTime(Date date) {
		SimpleDateFormat s1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return s1.format(date);
	}

	/**
	 * 将时间转换为yyyy-MM-dd HH:mm:ss
	 * 
	 * @title convertTimeToDay
	 * @author liuchengbao
	 * @param timeStr
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertTime(String Str) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date = new Date(Long.valueOf(Str));
			return df.format(date);
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * 将时间转换为yyyy-MM-dd
	 * 
	 * @title convertTimeToDate
	 * @param timeStr
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertTimeToDate(String Str) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = new Date(Long.valueOf(Str));
			return df.format(date);
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * 将时间转换为yyyy-MM-dd HH:mm
	 * 
	 * @title convertTimeToDay
	 * @author liuchengbao
	 * @param timeStr
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertToTime(String Str) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date date = new Date(Long.valueOf(Str));
			return df.format(date);
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * 将时间转换为HH:mm:ss
	 * 
	 * @title convertTimeToMin
	 * @param timeStr
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertTimeToMin(String Str) {
		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		try {
			Date date = new Date(Long.valueOf(Str));
			return df.format(date);
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * 将时间转换为HH:mm
	 * 
	 * @title convertTimeToDay
	 * @author liuchengbao
	 * @param timeStr
	 * @return
	 */
	public static String convertTimeToHours(String timeStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
		Date date = new Date();
		Long l = 0l;
		try {
			l = sdf.parse(timeStr).getTime();
			date.setTime(l);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sdf1.format(date);
	}

	
	/**
	 * 将Dec 10, 2016 9:05:12 AM时间转换为yyyy-MM-dd
	 * 
	 * @title convertTimeToDate
	 * @param timeStr
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String convertDateTime(String Str) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = new Date(Str);
			return df.format(date);
		} catch (Exception ex) {
			return "";
		}
	}

	/**
	 * 将一个时间转换成提示性时间字符串，如刚刚，1秒前
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static String convertTimeToFormat(String timeStr) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		long timeStamp = 0;

		try {
			timeStamp = sdf.parse(timeStr).getTime() / (long) 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long curTime = System.currentTimeMillis() / (long) 1000;
		long time = curTime - timeStamp;

		if (time < 60 && time >= 0) {
			return "刚刚";
		} else if (time >= 60 && time < 3600) {
			return time / 60 + "分钟前";
		} else if (time >= 3600 && time < 3600 * 24) {
			return time / 3600 + "小时前";
		} else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
			return time / 3600 / 24 + "天前";
		} else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
			return time / 3600 / 24 / 30 + "个月前";
		} else if (time >= 3600 * 24 * 30 * 12) {
			return time / 3600 / 24 / 30 / 12 + "年前";
		} else {
			return "刚刚";
		}
	}

	/**
	 * 出谷时间转换成提示性时间字符串，几天后 几个小时后
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static String villainTimeToFormat(String timeStr) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		long timeStamp = 0;

		try {
			timeStamp = sdf.parse(timeStr).getTime() / (long) 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		long curTime = System.currentTimeMillis() / (long) 1000;
		long time = timeStamp - curTime;

		if (time < 60 && time >= 0) {
			return "马上出谷";
		} else if (time >= 60 && time < 3600) {
			return time / 60 + "分钟";
		} else if (time >= 3600 && time < 3600 * 24) {
			return time / 3600 + "小时";
		} else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
			return time / 3600 / 24 + "天";
		} else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
			return time / 3600 / 24 / 30 + "个月";
		} else if (time >= 3600 * 24 * 30 * 12) {
			return time / 3600 / 24 / 30 / 12 + "年";
		} else {
			return "马上出谷";
		}
	}

	/**
	 * 将一个时间转换成提示性时间字符串，昨天 11:30
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static String getTimestampString(String timeStr) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date paramDate = null;
		try {
			paramDate = sdf.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		if (paramDate == null) {
			return "未知时间";
		}

		String str = null;

		long l = paramDate.getTime();

		Calendar localCalendar = GregorianCalendar.getInstance();

		localCalendar.setTime(paramDate);

		int year = localCalendar.get(Calendar.YEAR);

		if (!isSameYear(year)) { // 去年，直接返回
			String paramDate2str = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(paramDate);
			return paramDate2str;
		}

		if (isSameDay(l)) {
			int i = localCalendar.get(Calendar.HOUR_OF_DAY);
			if (i > 17) {
				str = "晚上 HH:mm";// HH表示24小时,hh表示12小时进制，
			} else if ((i >= 0) && (i <= 6)) {
				str = "凌晨 HH:mm";
			} else if ((i > 11) && (i <= 17)) {
				str = "下午 HH:mm";
			} else {
				str = "上午 HH:mm";
			}
		} else if (isYesterday(l)) {
			str = "昨天 HH:mm";
		} else if (isBeforeYesterday(l)) {
			str = "前天 HH:mm";
		} else {
			str = "M月d日 HH:mm";
		}
		String paramDate2str = new SimpleDateFormat(str, Locale.CHINA).format(paramDate);

		return paramDate2str;
	}

	/**
	 * 获取 今天开始结束 时间
	 * 
	 * @title getTodayStartAndEndTime
	 * @author zhaoqingyang
	 * @return
	 */
	public static TimeInfo getTodayStartAndEndTime() {

		Calendar localCalendar1 = Calendar.getInstance();
		localCalendar1.set(Calendar.HOUR_OF_DAY, 0);
		localCalendar1.set(Calendar.MINUTE, 0);
		localCalendar1.set(Calendar.SECOND, 0);
		localCalendar1.set(Calendar.MILLISECOND, 0);
		Date localDate1 = localCalendar1.getTime();
		long l1 = localDate1.getTime();

		Calendar localCalendar2 = Calendar.getInstance();
		localCalendar2.set(Calendar.HOUR_OF_DAY, 23);
		localCalendar2.set(Calendar.MINUTE, 59);
		localCalendar2.set(Calendar.SECOND, 59);
		localCalendar2.set(Calendar.MILLISECOND, 999);
		Date localDate2 = localCalendar2.getTime();
		long l2 = localDate2.getTime();

		TimeInfo localTimeInfo = new TimeInfo();
		localTimeInfo.setStartTime(l1);
		localTimeInfo.setEndTime(l2);
		return localTimeInfo;
	}

	/**
	 * 获取 昨天开始结束 时间
	 * 
	 * @title getYesterdayStartAndEndTime
	 * @author zhaoqingyang
	 * @return
	 */
	public static TimeInfo getYesterdayStartAndEndTime() {
		Calendar localCalendar1 = Calendar.getInstance();
		localCalendar1.add(Calendar.DAY_OF_MONTH, -1);// 5
		localCalendar1.set(Calendar.HOUR_OF_DAY, 0);// 11
		localCalendar1.set(Calendar.MINUTE, 0);// 12
		localCalendar1.set(Calendar.SECOND, 0);// 13
		localCalendar1.set(Calendar.MILLISECOND, 0);// Calendar.MILLISECOND
		Date localDate1 = localCalendar1.getTime();
		long l1 = localDate1.getTime();

		Calendar localCalendar2 = Calendar.getInstance();
		localCalendar2.add(Calendar.DAY_OF_MONTH, -1);// 5
		localCalendar2.set(Calendar.HOUR_OF_DAY, 23);// 11
		localCalendar2.set(Calendar.MINUTE, 59);// 12
		localCalendar2.set(Calendar.SECOND, 59);// 13
		localCalendar2.set(Calendar.MILLISECOND, 999);// Calendar.MILLISECOND
		Date localDate2 = localCalendar2.getTime();
		long l2 = localDate2.getTime();

		TimeInfo localTimeInfo = new TimeInfo();
		localTimeInfo.setStartTime(l1);
		localTimeInfo.setEndTime(l2);
		return localTimeInfo;
	}

	/**
	 * 获取 前天开始结束 时间
	 * 
	 * @title getBeforeYesterdayStartAndEndTime
	 * @author zhaoqingyang
	 * @return
	 */
	public static TimeInfo getBeforeYesterdayStartAndEndTime() {
		Calendar localCalendar1 = Calendar.getInstance();
		localCalendar1.add(Calendar.DAY_OF_MONTH, -2);
		localCalendar1.set(Calendar.HOUR_OF_DAY, 0);
		localCalendar1.set(Calendar.MINUTE, 0);
		localCalendar1.set(Calendar.SECOND, 0);
		localCalendar1.set(Calendar.MILLISECOND, 0);
		Date localDate1 = localCalendar1.getTime();
		long l1 = localDate1.getTime();

		Calendar localCalendar2 = Calendar.getInstance();
		localCalendar2.add(Calendar.DAY_OF_MONTH, -2);
		localCalendar2.set(Calendar.HOUR_OF_DAY, 23);
		localCalendar2.set(Calendar.MINUTE, 59);
		localCalendar2.set(Calendar.SECOND, 59);
		localCalendar2.set(Calendar.MILLISECOND, 999);
		Date localDate2 = localCalendar2.getTime();
		long l2 = localDate2.getTime();
		TimeInfo localTimeInfo = new TimeInfo();
		localTimeInfo.setStartTime(l1);
		localTimeInfo.setEndTime(l2);
		return localTimeInfo;
	}

	private static boolean isSameDay(long paramLong) {
		TimeInfo localTimeInfo = getTodayStartAndEndTime();
		return (paramLong > localTimeInfo.getStartTime()) && (paramLong < localTimeInfo.getEndTime());
	}

	private static boolean isYesterday(long paramLong) {
		TimeInfo localTimeInfo = getYesterdayStartAndEndTime();
		return (paramLong > localTimeInfo.getStartTime()) && (paramLong < localTimeInfo.getEndTime());
	}

	private static boolean isBeforeYesterday(long paramLong) {
		TimeInfo localTimeInfo = getBeforeYesterdayStartAndEndTime();
		return (paramLong > localTimeInfo.getStartTime()) && (paramLong < localTimeInfo.getEndTime());
	}

	public static boolean isSameYear(int year) {
		Calendar cal = Calendar.getInstance();
		int CurYear = cal.get(Calendar.YEAR);
		return CurYear == year;
	}

	public static String timeFormat(long timeMillis, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
		return format.format(new Date(timeMillis));
	}

	public static String formatPhotoDate(long time) {
		return timeFormat(time, "yyyy-MM-dd");
	}

	public static String formatPhotoDate(String path) {
		File file = new File(path);
		if (file.exists()) {
			long time = file.lastModified();
			return formatPhotoDate(time);
		}
		return "1970-01-01";
	}

}
