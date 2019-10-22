package com.xajiusuo.jpa.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;



/**
 * Desk parameter 参数类
 */
public interface P {
/*
	*//***
	 * 用户信息
	 *//*
	ThreadLocal<UserInfo> u = new ThreadLocal<UserInfo>();*/

	/***
	 * 配置相关参数
	 * 
	 * @author 杨勇
	 *
	 */
	 interface C {

		/**
		 * 最大同时审核角色
		 */
		static int max_role = 4;

		/***
		 * 最大同时审核人员
		 */
		static int max_person = 4;

		/***
		 * 今天之前多少天时间可以申请加班流程
		 */
		static int otime_min = 60;

		/***
		 * 加班申请日前后多少天的申请记录
		 */
		static int otime_recard = 15;

		/***
		 * 加班申请日前后多少天的考勤记录
		 */
		static int otime_word_recard = 15;

		/***
		 * 加班最少多长时间算一次(分钟)
		 */
		static int otime_hour = 120;

		/***
		 * 加班最小小时
		 */
		static int otime_min_day = 2;

		/***
		 * 加班申请时 判定9点
		 */
		static String otime_index = "09:00";

		/***
		 * 加班时间晚上以后第二天不需要上班打卡，需要下班打卡
		 */
		static String otime_night = "20:30-08:30";

		/***
		 * 加班时间凌晨之后算作上午加班
		 */
		static String otime_dawn = "08:31-12:00";

		/***
		 * 加班时间下午之后算作下午加班
		 */
		static String otime_after = "14:00-23:59";
		
		/***
		 * 多少分钟算半天
		 */
		static int otime_holi_minu = 120;
		
		/***
		 * 2级流程
		 */
		static int leave_flow_role_num = 2;

		/***
		 * 请假流程3级生效
		 */
		static int leave_flow_num = 3;

		/***
		 * 自动导入行分割
		 */
		static String work_split_row = "\\|";

		/***
		 * 自动分割单元分割
		 */
		static String work_split_unit = ",";

		/***
		 * 用户工号下标
		 */
		static int work_user_index = 0;

		/***
		 * 公出上下午分界时
		 */
		static String trval_time = "12:00-14:00";

		/***
		 * 时间下标
		 */
		static int work_date_index = 1;

		/***
		 * 自动导入时间格式
		 */
		static String work_date_pattern = "yyyy-M-d H:m:s";

		/***
		 * 显示最大天数
		 */
		static int work_sheet_max_day = 40;

		/***
		 * 考勤统计/表默认截至日期
		 */
		static int work_sheet_end_day = 25;

		/***
		 * 公休/年假名称
		 */
		static String work_sheet_yearholi = "公休";

		/***
		 * 考勤图形<br>
		 * 0√按时<br>
		 * 1×迟到早退<br>
		 * 2△加班<br>
		 * 3▲请假<br>
		 * 4○公出<br>
		 * 5●公休<br>
		 * 6★旷工
		 */
		static String[] work_sheet_type = new String[]{"√", "×", "△", "▲", "○", "●", "★"};

		/***
		 * 加班规则,true为申请则为加班,false为按照打卡记录算加班
		 */
		static boolean work_otime = true;

		/***
		 * 新增用户是否可填写警号
		 */
		static boolean user_police = true;

		/***
		 * 默认工作时间,1为周日,2为周一...
		 */
		static String work_days = "23456";

		/***
		 * 每月自助补卡最大天数
		 */
		static int month_work_max = 3;
		
		/***
		 * 每月自助补卡最大天数
		 */
		static int month_work_later_max = 3;

		/***
		 * 每月自助补卡起始日
		 */
		static int month_work_start_day = work_sheet_end_day;

		/***
		 * 公出最小字数
		 */
		static int tra_work_max = 10;

		/***
		 * 公出确定延迟天数
		 */
		static int tra_ok_day = 1;

		/***
		 * 文件服务器位置
		 */
		static String[] file_service1 = new String[]{"Administrator", "123$asd", "127.0.0.1", "file"};
//		static String[] file_service1 = new String[]{"Administrator", "yangyong0828", "127.0.0.1", "file"};
		/***
		 * 文件服务器位置
		 */
		static String[] file_service2 = new String[]{"Administrator", "1", "192.168.1.234", "file"};

		/***
		 * 上午考勤正常 下午考勤不计早退
		 */
		static boolean work_after_on = true;
		
		/***
		 * 加班第二天不用打卡
		 */
		static boolean work_over_no = true;

		/***
		 * 周末节假日值班延时分钟
		 */
		static int work_week_wait = 60;

		/***
		 * 周末延时大家是否可见
		 */
		static boolean work_user_visible = false;

		/***
		 * 自定义加班是否延时
		 */
		static boolean work_week_all = true;

		/***
		 * 值班有效验证
		 */
		static boolean wrok_duty_valid = false;
		
		/***
		 * 机卡对应接口地址
		 */
		static String jkdyWebService = "http://191.10.1.8:8080/jkdyModulecxServlet/JkdyWebService";
	}

	/**
	 * action attr 中 key
	 * 
	 * @author Administrator
	 * 
	 */
	interface A {
		/**
		 * 用户
		 */
		String user = "uInfor";
		/**
		 * 用户
		 */
		String user1 = "uInfo1";

		/**
		 * 上传文件
		 */
		String file = "files";

		/**
		 * 上传标记
		 */
		String upload = "upload";
	}

	class M {
		/***
		 * 值班设置
		 */
		public static Map<String, String> map_duty = new HashMap<String, String>();

		/***
		 * 流程状态
		 */
		public static Map<String, String> map_task = new HashMap<String, String>();

		/***
		 * 类名
		 */
		public static Map<String, String> map_cofs = new HashMap<String, String>();
		
		public static String filetypes(){
			HashSet<String> set=new HashSet<String>();
			set.addAll(map_file.values());
			List<String> list = new ArrayList<String>(set);
			set.clear();
			list.add("其他");
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			for(String s:list){
				sb.append("{name:'" + s + "',value:'" + s + "'},");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("]");
			return sb.toString();
		}
		
		/***
		 * 文件格式
		 */
		public static Map<String, String> map_file = new HashMap<String, String>();
		
		/***
		 * 文件格式
		 */
		public static Map<String, String> map_status1 = new HashMap<String, String>();

		static {
			map_duty.put("1", "值班领导");
			map_duty.put("2", "指挥中心");
			map_duty.put("3", "值班部门");

			map_task.put(null, "在办");
			map_task.put("", "在办");
			map_task.put("0", "待提交");
			map_task.put("1", "待审");
			map_task.put("2", "通过");
			map_task.put("-1", "驳回");
			map_task.put("3", "首页");
			map_task.put("4", "设置");

			//安装
			map_file.put("exe", "安装包");
			map_file.put("ios", "安装包");
			map_file.put("apk", "安装包");
			map_file.put("elf", "安装包");
			map_file.put("gz", "安装包");
			//视频
			map_file.put("mpeg", "视频");
			map_file.put("avi", "视频");
			map_file.put("mov", "视频");
			map_file.put("asf", "视频");
			map_file.put("wmv", "视频");
			map_file.put("navi", "视频");
			map_file.put("3gp", "视频");
			map_file.put("4gp", "视频");
			map_file.put("ra", "视频");
			map_file.put("ram", "视频");
			map_file.put("f4v", "视频");
			map_file.put("rmvb", "视频");
			map_file.put("mpg", "视频");
			map_file.put("mp4", "视频");
			map_file.put("mkv", "视频");
			//文档
			map_file.put("doc", "文档");
			map_file.put("docx", "文档");
			map_file.put("ppt", "文档");
			map_file.put("pptx", "文档");
			map_file.put("xls", "文档");
			map_file.put("xlsx", "文档");
			map_file.put("txt", "文档");
			map_file.put("pdf", "文档");
			map_file.put("jnt", "文档");
			//图片
			map_file.put("bmp", "图片");
			map_file.put("jpg", "图片");
			map_file.put("jpeg", "图片");
			map_file.put("gif", "图片");
			map_file.put("png", "图片");
			map_file.put("ico", "图片");
			//音频
			map_file.put("wav", "音频");
			map_file.put("mp3", "音频");
			map_file.put("au", "音频");
			map_file.put("mid", "音频");
			map_file.put("ogg", "音频");
			//压缩
			map_file.put("zip", "压缩");
			map_file.put("7z", "压缩");
			map_file.put("jar", "压缩");
			map_file.put("war", "压缩");
			map_file.put("kz", "压缩");
			map_file.put("rar", "压缩");
			map_file.put("rz", "压缩");
			map_file.put("zipx", "压缩");
			map_file.put("zix", "压缩");
			//其他
			
			//装备状态
			map_status1.put("1", "待处理");
			map_status1.put("2", "已处理");
		}
		
	}

	/**
	 * Desk 常量
	 */
	interface S {

		String comname = "";

		String comName = comname + "综合管理平台";

		String compName = "西安市公安局技术侦察支队";
		
		String casePrefix = "6101";
		
		Integer caseMarkStart = 8;


		/**
		 * <b>fmtYmd01</b> <span style='color:red'>yyyyMMddHHmmss</span><br>
		 * <b>fmtYmd02</b> <span style='color:red'>yyyyMMddHHmm</span><br>
		 * <b>fmtYmd03</b> <span style='color:red'>yyyyMMddHH</span><br>
		 * <b>fmtYmd04</b> <span style='color:red'>yyyyMMdd</span><br>
		 * <b>fmtYmd05</b> <span style='color:red'>yyyyMM</span><br>
		 * <b>fmtYmd06</b> <span style='color:red'>yyyy</span><br>
		 * <b>fmtYmd11</b> <span style='color:red'>yyyy-MM-dd HH:mm:ss</span><br>
		 * <b>fmtYmd12</b> <span style='color:red'>yyyy-MM-dd HH:mm</span><br>
		 * <b>fmtYmd13</b> <span style='color:red'>yyyy-MM-dd HH</span><br>
		 * <b>fmtYmd14</b> <span style='color:red'>yyyy-MM-dd</span><br>
		 * <b>fmtYmd15</b> <span style='color:red'>yyyy-MM</span><br>
		 * <b>fmtYmd16</b> <span style='color:red'>yyyy-M-d H:m:s</span><br>
		 * <b>fmtYmd17</b> <span style='color:red'>yyyy-M-d H:m</span><br>
		 * <b>fmtYmd18</b> <span style='color:red'>yyyy-M-d H</span><br>
		 * <b>fmtYmd19</b> <span style='color:red'>yyyy-M-d</span><br>
		 * <b>fmtYmd20</b> <span style='color:red'>yyyy-M</span><br>
		 * <b>fmtYmd21</b> <span style='color:red'>HHmmss</span><br>
		 * <b>fmtYmd22</b> <span style='color:red'>HHmm</span><br>
		 * <b>fmtYmd23</b> <span style='color:red'>HH:mm</span><br>
		 * <b>fmtYmd41</b> <span style='color:red'>yyyy/MM/dd HH:mm:ss</span><br>
		 * <b>fmtYmd42</b> <span style='color:red'>yyyy/MM/dd HH:mm</span><br>
		 * <b>fmtYmd43</b> <span style='color:red'>yyyy/MM/dd HH</span><br>
		 * <b>fmtYmd44</b> <span style='color:red'>yyyy/MM/dd</span><br>
		 * <b>fmtYmd45</b> <span style='color:red'>yyyy/MM</span><br>
		 * <b>fmtYmd46</b> <span style='color:red'>yyyy/M/d H:m:s</span><br>
		 * <b>fmtYmd47</b> <span style='color:red'>yyyy/M/d H:m</span><br>
		 * <b>fmtYmd48</b> <span style='color:red'>yyyy/M/d H</span><br>
		 * <b>fmtYmd49</b> <span style='color:red'>yyyy/M/d</span><br>
		 * <b>fmtYmd50</b> <span style='color:red'>yyyy/M</span><br>
		 * <b>fmtYmd51</b> <span style='color:red'>yyyy年MM月dd日</span><br>
		 * <b>fmtYmd52</b> <span style='color:red'>yyyy年MM月</span><br>
		 */
		String fmtYmd01 = "yyyyMMddHHmmss",
		fmtYmd02 = "yyyyMMddHHmm",
		fmtYmd03 = "yyyyMMddHH",
		fmtYmd04 = "yyyyMMdd",
		fmtYmd05 = "yyyyMM",
		fmtYmd06 = "yyyy",
		fmtYmd11 = "yyyy-MM-dd HH:mm:ss",
		fmtYmd12 = "yyyy-MM-dd HH:mm",
		fmtYmd13 = "yyyy-MM-dd HH",
		fmtYmd14 = "yyyy-MM-dd",
		fmtYmd15 = "yyyy-MM",
		fmtYmd16 = "yyyy-M-d H:m:s",
		fmtYmd17 = "yyyy-M-d H:m",
		fmtYmd18 = "yyyy-M-d H",
		fmtYmd19 = "yyyy-M-d",
		fmtYmd20 = "yyyy-M",
		fmtYmd21 = "HHmmss",
		fmtYmd22 = "HHmm",
		fmtYmd23 = "HH:mm",
		fmtYmd41 = "yyyy/MM/dd HH:mm:ss",
		fmtYmd42 = "yyyy/MM/dd HH:mm",
		fmtYmd43 = "yyyy/MM/dd HH",
		fmtYmd44 = "yyyy/MM/dd",
		fmtYmd45 = "yyyy/MM",
		fmtYmd46 = "yyyy/M/d H:m:s",
		fmtYmd47 = "yyyy/M/d H:m",
		fmtYmd48 = "yyyy/M/d H",
		fmtYmd49 = "yyyy/M/d",
		fmtYmd50 = "yyyy/M",
		fmtYmd51 = "yyyy年MM月dd日",
		fmtYmd52 = "yyyy年MM月";
		
	}

	/**
	 * Desk 工具
	 */
	class U {

		private static Map<String, SimpleDateFormat> map = new HashMap<String, SimpleDateFormat>();

		private static DecimalFormat df = new DecimalFormat("0.0");

		static Random r = new Random();

		public static String i2s(double d) {
			return df.format(d);
		}

		/***
		 * 去除时间的时分秒
		 * 
		 * @author 杨勇 2016年1月6日
		 * @param d
		 * @return
		 */
		public static Date dateYMD(Date d) {
			try {
				return getSDF(S.fmtYmd14).parse(getSDF(S.fmtYmd14).format(d));
			}catch(Exception e) {
				return null;
			}
		}

		public static Date dateParseYMD(String date) {
			try {
				return getSDF(S.fmtYmd14).parse(date);
			}catch(Exception e) {
				return null;
			}
		}

		public static String formatYMD(Date date) {
			if(date == null) {
				return "";
			}
			return getSDF(S.fmtYmd14).format(date);
		}

		/**
		 * 返回格式yyyy-MM-dd
		 * 
		 * @param date
		 * @return
		 */
		public static String dateFormat(Date date) {
			if(date == null) {
				return null;
			}
			return getSDF(S.fmtYmd14).format(date);
		}

		/**
		 * 返回格式yyyy
		 * 
		 * @param date
		 * @return
		 */
		public static String dateFormaty(Date date) {
			if(date == null) {
				return null;
			}
			return getSDF(S.fmtYmd06).format(date);
		}

		/**
		 * 返回格式yyyy-MM
		 * 
		 * @param date
		 * @return
		 */
		public static String dateFormatym(Date date) {
			if(date == null) {
				return null;
			}
			return getSDF(S.fmtYmd15).format(date);
		}

		public static String formatYM(Date date) {
			if(date == null) {
				return "";
			}
			return getSDF(S.fmtYmd15).format(date);
		}

		public static String dateFormat(Date date, String pattern) {
			try {
				return getSDF(pattern).format(date);
			}catch(Exception e) {
				return "";
			}
		}

		public static Date dateParse(String date, String pattern) {
			if(date == null){
				return null;
			}
			try {
				return getSDF(pattern).parse(date.trim());
			}catch(Exception e) {
				return null;
			}
		}

		/***
		 * 获取和今天相差的时间当前的0时
		 * 
		 * @param devDay 相差的天数1为明天，-1为昨天
		 * @return
		 */
		public static Date getDateByDay(Integer devDay) {
			return getDateByDay(devDay, null);
		}
		
		@SuppressWarnings("deprecation")
		public static Date getDateEnd(Date d){
			d.setTime((d.getTime() - (d.getTimezoneOffset() * 60 * 1000)) / 86400000 * 86400000 + 86400000 - 1000  + (d.getTimezoneOffset() * 60 * 1000));
			return d;
		}
		
		/***
		 * 获取和今天相差的时间当前的0时
		 * 
		 * @param devDay 相差的天数1为明天，-1为昨天
		 * @return
		 */
		public static Date getDateByDay(Integer devDay, Date date) {
			try {
				if(devDay == null) {
					devDay = 0;
				}
				if(date == null) {
					date = new Date();
				}
				Calendar c = Calendar.getInstance();
				c.setTime(getSDF(S.fmtYmd14).parse(getSDF(S.fmtYmd14).format(date)));
				c.add(Calendar.DATE, devDay);
				Date d = c.getTime();
				c.clear();
				return d;
			}catch(Exception e) {
				return null;
			}
		}

		/**
		 * 获取当前日期对应周所在年的第几周
		 * 
		 * @param date 要计算的当前时间
		 * @param begin false:以周日为第一天,true:以周一为第一天
		 * @return
		 */
		public static int getIndexWeekInYear(Date date, boolean begin) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			if(begin) {
				c.add(Calendar.DATE, -1);
			}
			int index = c.get(Calendar.WEEK_OF_YEAR);
			c.clear();
			return index;
		}

		/**
		 * 获取当前日期对应的周几时间yyyy-MM-dd,type false为00:00:00,true为23:59:59
		 * 
		 * @param date 要计算的当前时间
		 * @param week 返回周几1:周日,2:周一,3:周二...
		 * @param type false为00:00:00,true为23:59:59
		 * @param begin false:以周日为第一天,true:以周一为第一天
		 * @return
		 */
		public static Date getIndexDateWeek(Date date, Integer week, boolean type, boolean begin) {
			if(week < 1 || week > 7) {
				throw new RuntimeException("week 不能小于1或者大于7");
			}
			if(date == null) {
				return null;
			}
			Calendar c = Calendar.getInstance();
			try {
				c.setTime(getSDF(S.fmtYmd14).parse(getSDF(S.fmtYmd14).format(date)));
			}catch(Exception e) {
			}
			if(begin) {//周一则跳前一天
				c.add(Calendar.DATE, -1);
				if(week == 1) {
					c.add(Calendar.DATE, week - c.get(Calendar.DAY_OF_WEEK) + 7);
				}else {
					c.add(Calendar.DATE, week - c.get(Calendar.DAY_OF_WEEK));
				}
			}else {
				c.add(Calendar.DATE, week - c.get(Calendar.DAY_OF_WEEK));
			}
			if(type) {//天结束
				c.add(Calendar.DATE, 1);
				c.add(Calendar.SECOND, -1);
			}
			Date d = c.getTime();
			c.clear();
			return d;
		}

		/**
		 * 获取当前日期下周对应的周几时间yyyy-MM-dd,type false为00:00:00,true为23:59:59
		 * 
		 * @param date 要计算的当前时间
		 * @param week 返回周几1:周日,2:周一,3:周二...
		 * @param type false为00:00:00,true为23:59:59
		 * @param begin false:以周日为第一天,true:以周一为第一天
		 * @return
		 */
		public static Date getNextIndexDateWeek(Date date, Integer week, boolean type, boolean begin) {
			if(week < 1 || week > 7) {
				throw new RuntimeException("week 不能小于1或者大于7");
			}
			if(date == null) {
				return null;
			}
			Calendar c = Calendar.getInstance();
			try {
				c.setTime(getSDF(S.fmtYmd14).parse(getSDF(S.fmtYmd14).format(date)));
			}catch(Exception e) {
			}
			c.add(Calendar.DATE, 7);
			if(begin) {//周一则跳前一天
				c.add(Calendar.DATE, -1);
				if(week == 1) {
					c.add(Calendar.DATE, week - c.get(Calendar.DAY_OF_WEEK) + 7);
				}else {
					c.add(Calendar.DATE, week - c.get(Calendar.DAY_OF_WEEK));
				}
			}else {
				c.add(Calendar.DATE, week - c.get(Calendar.DAY_OF_WEEK));
			}
			if(type) {//天结束
				c.add(Calendar.DATE, 1);
				c.add(Calendar.SECOND, -1);
			}
			Date d = c.getTime();
			c.clear();
			return d;
		}

		/**
		 * 获取当前日期上周对应的周几时间yyyy-MM-dd,type false为00:00:00,true为23:59:59
		 * 
		 * @param date 要计算的当前时间
		 * @param week 返回周几1:周日,2:周一,3:周二...
		 * @param type 0为00:00:00,1为23:59:59
		 * @param begin false:以周日为第一天,true:以周一为第一天
		 * @return
		 */
		public static Date getPrevIndexDateWeek(Date date, Integer week, boolean type, boolean begin) {
			if(week < 1 || week > 7) {
				throw new RuntimeException("week 不能小于1或者大于7");
			}
			if(date == null) {
				return null;
			}
			Calendar c = Calendar.getInstance();
			try {
				c.setTime(getSDF(S.fmtYmd14).parse(getSDF(S.fmtYmd14).format(date)));
			}catch(Exception e) {
			}
			c.add(Calendar.DATE, -7);
			if(begin) {//周一则跳前一天
				c.add(Calendar.DATE, -1);
				if(week == 1) {
					c.add(Calendar.DATE, week - c.get(Calendar.DAY_OF_WEEK) + 7);
				}else {
					c.add(Calendar.DATE, week - c.get(Calendar.DAY_OF_WEEK));
				}
			}else {
				c.add(Calendar.DATE, week - c.get(Calendar.DAY_OF_WEEK));
			}
			if(type) {//天结束
				c.add(Calendar.DATE, 1);
				c.add(Calendar.SECOND, -1);
			}
			Date d = c.getTime();
			c.clear();
			return d;
		}

		/**
		 * 获取当前月份最大的周数
		 * 
		 * @param date
		 * @return
		 */
		public static int getMaxWeekInMonth(Date date) {
			if(date == null) {
				return 0;
			}
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.set(Calendar.DATE, 1);
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			int index = c.get(Calendar.WEEK_OF_MONTH);
			c.clear();
			return index;
		}
		
		/**
		 * 获取当前月份最大的周数
		 * 
		 * @param date
		 * @return
		 */
		public static int getMaxDayInMonth(Date date) {
			if(date == null) {
				return 0;
			}
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.set(Calendar.DATE, 1);
			c.add(Calendar.MONTH, 1);
			c.add(Calendar.DATE, -1);
			int index = c.get(Calendar.DAY_OF_MONTH);
			c.clear();
			return index;
		}

		/***
		 * 1-7返回对应大写
		 * 
		 * @return
		 */
		public static String i2s(int index) {
			switch(index) {
				case 1:
					return "日";
				case 2:
					return "一";
				case 3:
					return "二";
				case 4:
					return "三";
				case 5:
					return "四";
				case 6:
					return "五";
				case 7:
					return "六";
				default:
					break;
			}
			return "";
		}

		/***
		 * 获取格式的日期format
		 * 
		 * @param pattern
		 * @return
		 */
		public static SimpleDateFormat getSDF(String pattern) {
			SimpleDateFormat sdf = map.get(pattern);
			if(sdf == null) {
				sdf = new SimpleDateFormat(pattern);
				map.put(pattern, sdf);
			}
			return sdf;
		}

		public static int getRandon(int n) {
			return r.nextInt(n);
		}

		public static boolean eq(Integer i, int d) {
			if(i == null) {
				return false;
			}
			return i == d;
		}

//		public static void setU(UserInfo user) {
//			u.set(user);
//		}
//
//		public static UserInfo getU() {
//			return u.get();
//		}
//
//		public static void clearU() {
//			u.remove();
//		}

		public static <E> E get(List<E> list, int index) {
			if(index >= list.size()) {
				return null;
			}
			return list.get(index);
		}

		/***
		 * 获取当前时间周几
		 * 
		 * @author 杨勇 2016年1月28日
		 * @param date
		 * @return
		 */
		public static int getWeek(Date date) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int w = c.get(Calendar.DAY_OF_WEEK);
			c.clear();
			return w;
		}

		/***
		 * 获取当天日期23:59:59
		 * 
		 * @author 杨勇 2016年3月8日
		 * @param endtime
		 * @return
		 */
		public static Date dateMax(Date endtime) {
			if(endtime != null) {
				endtime.setTime((endtime.getTime() + 57600000) / 86400000 * 86400000 + 57600000 - 1);
			}
			return endtime;
		}
		
		/***
		 * 返回小的时间
		 * @author 杨勇 2016年8月10日
		 * @param d1
		 * @param d2
		 * @return
		 */
		public static Date min(Date d1,Date d2){
			if(d1 == null){
				return d2;
			}
			if(d2 == null){
				return d1;
			}
			return d1.after(d2) ? d2 : d1;
		}
		
		/***
		 * 返回大的时间
		 * @author 杨勇 2016年8月10日
		 * @param d1
		 * @param d2
		 * @return
		 */
		public static Date max(Date d1,Date d2){
			if(d1 == null){
				return d2;
			}
			if(d2 == null){
				return d1;
			}
			return d1.after(d2) ? d1 : d2;
		}

		//测试监控
		static List<StringBuilder> list = new ArrayList<StringBuilder>();
		static Map<Long,StringBuilder> testMap = new HashMap<Long,StringBuilder>();
		static ThreadLocal<long[]> t2 = new ThreadLocal<long[]>(){//初始语句的初始时间
			@Override
			protected long[] initialValue() {
				return new long[]{System.currentTimeMillis(),System.currentTimeMillis()};
			}
		};
		static ThreadLocal<StringBuilder> nowTest = new ThreadLocal<StringBuilder>(){//初始一个测试语句
			@Override
			protected StringBuilder initialValue() {
				StringBuilder sb = new StringBuilder("");
				list.add(0,sb);
				return sb;
			}
		};

		/***
		 * 方法卡顿测试(删除不代表不建议用,是为了更好区别)
		 * @param msg
		 * @deprecated
         */
		public synchronized static void addTest(String msg){
			if(list.size() > 100){//保持数量100
				StringBuilder t = list.remove(list.size() - 1);
				t.delete(0,t.length()-1);
			}
			long[] l2 = t2.get();
			StringBuilder sb = nowTest.get();

			if(msg == null){
				msg = "";
			}

			StackTraceElement statck = statck();//获取调用处
			String from = statck.getClassName() + "." + statck.getMethodName() + "()";

			long one = System.currentTimeMillis() - l2[1];
			sb.append("\t\t\t<tr" + (one > 1000 ? " style='color:red;'" : " style='color:blue;'") + "><td nowrap>" + U.dateFormat(new Date(),"yyyy-MM-dd HH:mm:ss") + "</td><td nowrap>" + statck().getLineNumber() + "</td><td nowrap>" + one + "</td><td nowrap>" + (System.currentTimeMillis() - l2[0]) + "</td><td>" + msg + "</td><td nowrap>" + from + "</td></tr>\n");
			l2[1] = System.currentTimeMillis();
			if("结束".equals(msg)){
				t2.remove();
				nowTest.remove();
			}
		}

		public synchronized static List<StringBuilder> showTest(){
			return list;
		}

		public static StackTraceElement statck(){
			return new Throwable().getStackTrace()[2];
		}

	}

	/**
	 * Desk Diction 字典值
	 */
	interface D {

		/**
		 * dic_1 装备重要程度<br>
		 * dic_2 装备类型<br>
		 * dic_3 职务<br>
		 * dic_4 取得方式：自购，政府采购<br>
		 * dic_5 公出类型<br>
		 * dic_6 戒备等级<br>
		 * dic_7 年假定义<br>
		 * dic_8 值班设置<br>
		 * dic_9 菜单定制<br>
		 * dic_10 三方定制<br>
		 * dic_11 值班管理<br>
		 * dic_12 内勤员<br>
		 * dic_13 燃油类型:汽油 ，柴油<br>
		 * dic_14 车辆类型:轿车，越野车<br>
		 * dic_15 车辆用途：一般执法用车，特种专业用车<br>
		 * dic_33 车辆排量<br>
		 * dic_34保险公司<br>
		 * dic_35情报信息标记<br>
		 * dic_36情报信息类型<br>
		 */
		String dic_1 = "1", dic_2 = "2", dic_3 = "3", dic_4 = "4", dic_5 = "5", dic_6 = "6", dic_7 = "7", dic_8 = "8",
				dic_9 = "9", dic_10 = "10", dic_11 = "11", dic_12 = "12", dic_13 = "13", dic_14 = "14", dic_15 = "15", 
				dic_33 = "33", dic_34 = "34",dic_35 = "35",dic_36 = "36";

		/**
		 * dic_16 代办菜单<br>
		 * dic_17 考勤机<br>
		 * dic_18 使用方向<br>
		 * dic_19 使用状态<br>
		 * dic_20 价值类型<br>
		 * dic_21 财务入账状态<br>
		 * dic_22 交通工具<br>
		 * dic_23 装备分类<br>
		 * dic_24 装备状态<br>
		 * dic_25 菜单类型<br>
		 * dic_26案件管理统计类型<br>
		 * dic_27案件全部搜索类型<br>
		 * dic_28装备借用用途<br>
		 * dic_29发挥作用<br>
		 * dic_30评定等级<br>
		 * dic_31使用手段<br>
		 * dic_32案件成果类型<br>
		 */
		String dic_16 = "16", dic_17 = "17", dic_18 = "18", dic_19 = "19", dic_20 = "20", dic_21 = "21", dic_22 = "22",
				dic_23 = "23",dic_24 = "24",dic_25 = "25",dic_26 = "26",dic_27 = "27",dic_28 = "28",dic_29 = "29",dic_30 = "30",dic_31 = "31",dic_32 = "32";

		/**
		 * EQZK 601在库<br>
		 * EQZY 602再用<br>
		 * EQDX 603待修<br>
		 * EQBF 604报废
		 */
		String EQZK = "601", EQZY = "602", EQDX = "603", EQBF = "604";
		/**
		 * astype1 1在库/待处理中<br>
		 * astype2 2在用/已处理<br>
		 * astype3 3待修<br>
		 * astype_1 -1报废
		 */
		String astype1 = "1", astype2 = "2", astype3 = "3", astype_1 = "-1";
		/**
		 * EYY 605有源<br>
		 * EWY 606无源
		 */
		String EYY = "605", EWY = "606";

	}

	interface F {
		/**
		 * FLOW_1 已驳回<br>
		 * FLOW_2 已通过<br>
		 * FLOW_3 退回<br>
		 * FLOW_4 同意<br>
		 * FLOW_5 提交<br>
		 * FLOW_6 撤回<br>
		 */
		String FLOW_1 = "已驳回", FLOW_2 = "已通过", FLOW_3 = "退回", FLOW_4 = "同意", FLOW_5 = "提交", FLOW_6 = "撤回";

		/**
		 * FLOW_TYPE_1 案件类型<br>
		 * FLOW_TYPE_2 装备类型<br>
		 */
		Integer FLOW_TYPE_1 = 1, FLOW_TYPE_2 = 2;

		/**
		 * STEP01 登记成功<br>
		 * STEP02 已登记<br>
		 * STEP03 入库成功<br>
		 * STEP04 已入库<br>
		 * STEP05 警告未授权<br>
		 * STEP06 出库成功<br>
		 * STEP07 已出库<br>
		 * STEP08 已出库归还请先登记<br>
		 * STEP09 归还成功<br>
		 * STEP10 归还待修成功<br>
		 * STEP11 已归还<br>
		 * STEP97 盘点成功<br>
		 * STEP98 已盘点<br>
		 * STEP99 标签拆毁警告<br>
		 */
		String STEP01 = "step01", STEP02 = "step02", STEP03 = "step03", STEP04 = "step04", STEP05 = "step05",
				STEP06 = "step06", STEP07 = "step07", STEP08 = "step08", STEP09 = "step09", STEP10 = "step10",
				STEP11 = "step11", STEP97 = "step97", STEP98 = "step98", STEP99 = "step99";

		/**
		 * T0 无源<br>
		 * T1 有源<br>
		 */
		String T0 = "b00", T1 = "b01";
	}
}
