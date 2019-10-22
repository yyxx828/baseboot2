package com.xajiusuo.jpa.util;


import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.StringBuilderFormattable;

import java.io.*;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类
 *
 * @author martin
 */
public class CommonUtils {

    private static Pattern phone = Pattern.compile("^1\\d{10}$");
    private static Pattern tel = Pattern.compile("^(\\d{3,4})?[1-9]\\d{6,7}$");
    private static Pattern number = Pattern.compile("^\\d+$");
    private static Pattern dateTime = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})$");
    private static Pattern datePattern = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2})$");
    private static Pattern chineseWord = Pattern.compile("[\u4E00-\u9FA5]+");
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 判断给定的手机号码是不是合法的号码
     *
     * @param phoneCode 电话号码
     * @return 判断结果, 合法:true,非法:false
     */
    public static boolean isDateString(String phoneCode) {
        return phoneCode != null && datePattern.matcher(phoneCode).find();
    }

    public static String join(String[] strs, String join) {
        if (isNotEmpty(strs)) {
            StringBuilder sb = new StringBuilder();
            sb.append(strs[0]);
            for (int i = 1; i < strs.length; i++) {
                sb.append(",").append(trimString(strs[i]));
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * 判断给定的手机号码是不是合法的号码
     *
     * @param phoneCode 手机号码
     * @return 是否合法
     */
    public static boolean isPhoneCode(String phoneCode) {
        return phoneCode != null && phone.matcher(phoneCode).find();
    }

    public static double round(double value, int scale) {
        BigDecimal decimal = new BigDecimal(value);
        decimal = decimal.setScale(scale, BigDecimal.ROUND_HALF_UP);
        return decimal.doubleValue();
    }

    /**
     * 判断给定的电话号码是不是合法的号码
     *
     * @param telCode 电话号码
     * @return 是否合法
     */
    public static boolean isTelCode(String telCode) {
        return telCode != null && tel.matcher(telCode).find();
    }

    /**
     * 判断是否不为空
     *
     * @param list 集合
     * @return 不为空:true;为空:false.
     */
    public static boolean isNotEmpty(Collection list) {
        return list != null && list.size() > 0;
    }

    /**
     * 判断是否为空
     *
     * @param list 集合
     * @return 为空:true;不为空:false.
     */
    public static boolean isEmpty(Collection list) {
        return !isNotEmpty(list);
    }

    /**
     * 判断是否不为空
     *
     * @param objs 数组
     * @return 不为空:true;为空:false.
     */
    public static boolean isNotEmpty(Object[] objs) {
        return objs != null && objs.length > 0;
    }

    /**
     * 判断是否为空
     *
     * @param objs 数组
     * @return 为空:true;不为空:false.
     */
    public static boolean isEmpty(Object[] objs) {
        return !isNotEmpty(objs);
    }

    /**
     * 判断是否不为空
     *
     * @param str 字符串
     * @return 不为空:true;为空:false.
     */
    public static boolean isNotEmpty(String str) {
        return str != null && str.trim().length() > 0;
    }

    /**
     * 判断是否不为空
     *
     * @param s 缓存字符串
     * @return 不为空:true;为空:false.
     */
    public static boolean isNotEmpty(StringBuffer s) {
        return s != null && s.length() > 0;
    }

    /**
     * 判断是否为空
     *
     * @param s 缓存字符串
     * @return 为空:true;不为空:false.
     */
    public static boolean isEmpty(StringBuffer s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断是否为空
     *
     * @param str 字符串
     * @return 为空:true;不为空:false.
     */
    public static boolean isEmpty(String str) {
        return !isNotEmpty(str);
    }

    /**
     * 去除字符串前后空格，如果字符串为空，返回""
     *
     * @param str 待处理字符串
     * @return 结果字符串
     */
    public static String trimString(String str) {
        return str != null ? str.trim() : "";
    }

    /**
     * 去除字符串所有空格，如果字符串为空，返回""
     */
    public static String trimAllString(String str) {
        return str != null ? str.replace(" ", "") : "";
    }

    /**
     * 去除字符串前后空格，如果字符串为空，返回NULL
     *
     * @param str 字符串
     * @return 结果字符串
     */
    public static String trimStringWithNull(String str) {
        return str != null ? str.trim() : null;
    }

    /**
     * 判断给定的号码是不是合法的手机号码或电话号码
     *
     * @param code 手机号码或电话号码
     * @return 是否合法
     */
    public static boolean isPhoneOrTelCode(String code) {
        return code == null || phone.matcher(code).find() || tel.matcher(code).find();
    }

    /**
     * 是否为标准时间格式（yyyy-MM-dd HH:mm:ss）
     *
     * @param datetimeString 时间格式字符串
     * @return 判断结果
     */
    public static boolean isDatetime(String datetimeString) {
        return datetimeString != null && dateTime.matcher(datetimeString).find();
    }

    /**
     * 格式化时间
     *
     * @param date 时间
     * @return 格式字符串
     */
    public static String formatDate(Date date) {
        return sdf.format(date);
    }

    /**
     * 根据制定格式格式化时间
     *
     * @param date   时间
     * @param format 格式字符串
     * @return 格式字符串
     */
    public static String formatDate(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    /**
     * 将字符串装换成长整形数
     *
     * @param langValue 字符串
     * @return 长整形数字
     */
    public static Long toLong(String langValue) {
        try {
            return Long.parseLong(trimString(langValue));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将时间字符串解析为时间对象
     *
     * @param dateString 时间字符串
     * @return 时间对象
     */
    public static Date parseDate(String dateString) {
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取数组指定下标的元素
     *
     * @param objs  目标数组
     * @param index 下标
     * @param <T>   类型
     * @return 元素
     */
    public static <T extends Object> T getValue(T[] objs, int index) {
        if (objs != null && objs.length > index) {
            return objs[index];
        }
        return null;
    }

    /**
     * 获取数组指定下标的元素（带默认值）
     *
     * @param objs  目标数组
     * @param index 下标
     * @param <T>   类型
     * @return 元素
     */
    public static <T extends Object> T getValue(T[] objs, int index, T defaultValue) {
        if (objs != null && objs.length > index) {
            return objs[index];
        }
        return defaultValue;
    }

    /**
     * 根据指定格式解析时间字符串
     *
     * @param dateString 时间字符串
     * @param format     时间格式
     * @return 时间对象
     */
    public static Date parseDate(String dateString, String format) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获得UUID字符串
     *
     * @return UUID字符串
     */
    public static String getUUID() {
        String str = UUID.randomUUID().toString();
        str = str.replace("-", "");
        return str;
    }

    /**
     * 序列化对象
     *
     * @param obj 对象
     * @return 序列化字符串
     */
    public static String serializeObject(Serializable obj) {
        return serializeObj(obj);
    }

    /**
     * 序列化对象
     *
     * @param obj 对象
     * @return 序列化字符串
     */
    private static String serializeObj(Object obj) {
        String str = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            str = baos.toString("ISO-8859-1");
            str = URLEncoder.encode(str, "UTF-8");
            oos.close();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 序列化集合
     *
     * @param obj 集合
     * @return 序列化字符串
     */
    public static String serializeList(List obj) {
        return serializeObj(obj);
    }

    /**
     * 反序列化对象
     *
     * @param objStr 序列化字符串
     * @return 对象
     */
    public static Object deserializeObject(String objStr) {
        Object obj = null;
        try {
            objStr = URLDecoder.decode(objStr, "UTF-8");
            ByteArrayInputStream bis = new ByteArrayInputStream(objStr.getBytes("ISO-8859-1"));
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            bis.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * 反序列化集合
     *
     * @param objStr 序列化字符串
     * @return 集合
     */
    public static List deserializeList(String objStr) {
        List list = null;
        try {
            objStr = URLDecoder.decode(objStr, "UTF-8");
            ByteArrayInputStream bis = new ByteArrayInputStream(objStr.getBytes("ISO-8859-1"));
            ObjectInputStream ois = new ObjectInputStream(bis);
            list = (List) ois.readObject();
            bis.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 将字符串转化为整数
     *
     * @param intStr 字符串
     * @return 整形数字
     */
    public static int parseInteger(String intStr) {
        try {
            return Integer.parseInt(intStr.trim());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将字符串转化为长整型数
     *
     * @param intStr 字符串
     * @return 长整型数字
     */
    public static long parseLong(String intStr) {
        try {
            return Long.parseLong(intStr.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 将字符串转化为长整型数
     *
     * @param intStr 字符串
     * @return 长整型数字
     */
    public static Long parseLongObj(String intStr) {
        try {
            return Long.parseLong(intStr.trim());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将字符串转化为双精度数字
     *
     * @param intStr 字符串
     * @return 双精度数字
     */
    public static Double parseDoubleObj(String intStr) {
        try {
            return Double.parseDouble(intStr.trim());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将字符串转化为双精度数字
     *
     * @param intStr 字符串
     * @return 双精度数字
     */
    public static double parseDouble(String intStr) {
        try {
            return Double.parseDouble(intStr.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 将字符串转换为整型数字
     *
     * @param intStr 字符串
     * @return 整型数字
     */
    public static Integer parseIntegerObject(String intStr) {
        try {
            return Integer.parseInt(intStr.trim());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断两个对象是否相同
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 相同:true;不同:false.
     */
    public static boolean equals(Object obj1, Object obj2) {
        if (obj1 != null && obj2 != null) {
            return obj1.equals(obj2);
        } else if (obj1 == null && obj2 == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 过滤路径
     *
     * @param pathList 需要过滤的路径集合
     * @return 路径集合
     */
    public static List filterPath(List<String> pathList) {
        if (pathList != null && pathList.size() > 1) {
            String firstPath = pathList.get(0);
            for (int i = 1; i < pathList.size(); i++) {
                String path = pathList.get(i);
                if (path.startsWith(firstPath)) {
                    pathList.remove(0);
                    break;
                }
            }
            return pathList;
        }
        return pathList;
    }

    /**
     * 过滤路径
     *
     * @param path 需要过滤的路径数组
     * @return 路径数组
     */
    public static String[] filterPath(String path[]) {
        if (path != null && path.length > 1) {
            for (int i = 0; i < path.length - 1; i++) {
                String prePath = path[i];
                String nextPath = path[i + 1];
                if (nextPath.startsWith(prePath)) {
                    path = (String[]) ArrayUtils.remove(path, i);
                    return filterPath(path);
                }
            }
        }
        return path;
    }

    /**
     * 通过KEY读取webService配置
     *
     * @param key webService键值
     * @return webService地址
     * @throws IOException
     */
    @Deprecated
    public static String readRoutProperties(String key) throws IOException {
        Properties config = new Properties();
        InputStreamReader in = new InputStreamReader(
                CommonUtils.class.getResourceAsStream("/config/webService.properties"), "UTF-8");
        config.load(in);
        return config.getProperty(key);
    }

    /**
     * 是一个正常的电话号码
     *
     * @return 是否合法
     */
    public static boolean isPhoneCodeCorrect(String code) {
        if (isNotEmpty(code)) {
            if (!number.matcher(code).find()) {
                return false;
            } else if (code.length() < 7) {
                return false;
            } else if (code.startsWith("10")) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 将字符串切割为数字和字符串
     *
     * @param code 目标字符串
     * @return 结果Map对象. textField为字符串, numberField为数字.
     */
    public static Map<String, String> getFourNumAndString(String code) {
        if (StringUtils.isNotBlank(code)) {
            Map<String, String> map = new HashMap<String, String>();
            String number = "";
            Matcher matcher = Pattern.compile("[0-9]{1,99}").matcher(code);
            while (matcher.find()) {
                number += matcher.group(0) + ",";
            }
            if (number.length() > 1)
                number = number.substring(0, number.length() - 1);
            String[] a = number.split(",");
            for (String anA : a) {
                code = code.replace(anA, "");
            }
            map.put("textField", code);
            map.put("numberField", number);
            return map;
        }
        return null;
    }

    /**
     * 将字符串数组切割为数字和字符串
     *
     * @param code 目标字符串
     * @return 结果Map对象. textField为字符串, numberField为数字.
     */
    public static Map<String, String> getFourNumAndString(String code[]) {
        if (code != null && code.length > 1) {
            String line = "";
            for (int i = 1; i < code.length; i++) {
                line += code[i] + ",";
            }
            line = line.substring(0, line.length() - 1);
            Map<String, String> map = new HashMap<>();
            String number = "";
            Matcher matcher = Pattern.compile("[0-9]{1,99}").matcher(line);
            while (matcher.find()) {
                number += matcher.group(0) + ",";
            }
            if (number.length() > 1)
                number = number.substring(0, number.length() - 1);
            String[] a = number.split(",");
            for (String s : a) {
                line = line.replace(s, "");
            }
            map.put("textField", line);
            map.put("numberField", number);
            return map;
        }
        return null;
    }

    /**
     * 是一个正常的电话号码
     *
     * @param code 电话号码
     * @return 是否合法
     */
    public static boolean isNumber(String code) {
        if (isNotEmpty(code)) {
            if (number.matcher(trimString(code)).find()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否不为汉字
     *
     * @param word 字符串
     * @return 不为汉字:true; 为汉字:false.
     */
    public static boolean isChinseDecorrect(String word) {
        if (isNotEmpty(word)) {
            if (chineseWord.matcher(word).find()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否为数字类型
     *
     * @param clazz 类型
     * @return 为数字类型:true;不为数字类型:false.
     */
    public static boolean isNumber(Class<?> clazz) {
        if (null != clazz) {
            if (clazz.equals(Integer.class) || clazz.equals(Double.class) || clazz.equals(Float.class)
                    || clazz.equals(Long.class) || clazz.equals(int.class) || clazz.equals(double.class)
                    || clazz.equals(float.class) || clazz.equals(long.class)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 小于10的数字补0
     *
     * @param i 目标数字
     * @return 转换结果
     */
    public static String i2s(int i) {
        if (i < 10) {
            return "0" + i;
        }
        return "" + i;
    }

    /**
     * 通过正则表达式抓取匹配的字符串
     *
     * @param src   源字符串
     * @param regex 正则表达式
     * @return 匹配结果
     */
    public static String stringMatch(String src, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(src);
        if (m.find()) {
            return m.group(1);
        }
        return null;
    }

    /**
     * 转换xajsdb查询语句
     * <br>
     * 等于 1 不等于 2 以...开头 5 以...结尾 6 包含 7 不包含 8在...之间 9 不在...之间 A
     *
     * @param zd    字段
     * @param value 值
     * @param fh    逻辑运算符
     * @return 查询语句
     */
    private static String convert(String zd, String value, String fh) {
        StringBuilder s_all = new StringBuilder();
        if ("1".equals(fh)) {
            String s = zd + ":" + value;
            s_all.append(s);
        }
        if ("2".equals(fh)) {
            String s = zd + " NOT " + value;
            s_all.append(s);
        }
        if ("5".equals(fh)) {
            String s = zd + ":" + value + "*";
            s_all.append(s);
        }
        if ("6".equals(fh)) {
            String s = zd + ":*" + value;
            s_all.append(s);
        }
        if ("7".equals(fh)) {
            String s = zd + ":*" + value + "*";
            s_all.append(s);
        }
        if ("8".equals(fh)) {
            String s = zd + " NOT *" + value + "*";
            s_all.append(s);
        }
        return s_all.toString();
    }

    /**
     * 通过参数MAP生成查询字符串
     *
     * @param params 参数MAP
     * @return 查询字符串
     */
    public static String getQueryString(Map<String, String[]> params) {
        StringBuffer stringresult = new StringBuffer();
        try {
            if (params != null && params.size() > 0) {
                // 类型
                StringBuilder s_lx = new StringBuilder();
                if (CommonUtils.isNotEmpty(params.get("lx"))) {
                    s_lx.append("(");
                    String[] lxs = params.get("lx");
                    if (lxs != null && lxs.length > 2) {
                        String lx = lxs[1];
                        if (null != lx && lx.contains(",")) {
                            String[] lx_arr = lx.split(",");
                            if (isNotEmpty(lx_arr)) {
                                for (int i = 0; i < lx_arr.length; i++) {
                                    if (null != lx_arr[i] && lx_arr[i].length() > 0) {
                                        String s = convert("lx", lx_arr[i], lxs[0]);
                                        s_lx.append(s);
                                    }
                                    if (i < lx_arr.length - 1) {
                                        s_lx.append(" OR ");
                                    }
                                }
                            }
                        } else {
                            String s = convert("fwhm", lx, lxs[0]);
                            s_lx.append(s);
                        }
                        s_lx.append(")");
                    }
                }
                // 服务号码
                StringBuilder s_fwhm = new StringBuilder();
                if (CommonUtils.isNotEmpty(params.get("fwhm"))) {
                    s_fwhm.append("(");
                    String[] fwhms = params.get("fwhm");
                    if (fwhms != null && fwhms.length > 2) {
                        String fwhm = fwhms[1].replace("，", ",");
                        if (fwhm.contains(",")) {
                            String[] fwhm_arr = fwhm.split(",");
                            if (isNotEmpty(fwhm_arr)) {
                                for (int i = 0; i < fwhm_arr.length; i++) {
                                    String s = convert("fwhm", fwhm_arr[i], fwhms[0]);
                                    s_fwhm.append(s);
                                    if (i < fwhm_arr.length - 1) {
                                        s_fwhm.append(" OR ");
                                    }
                                }
                            }
                        } else {
                            String s = convert("fwhm", fwhm, fwhms[0]);
                            s_fwhm.append(s);
                        }
                    }
                    s_fwhm.append(")");
                }
                // 卡号
                StringBuilder s_kh = new StringBuilder();
                if (CommonUtils.isNotEmpty(params.get("kh"))) {
                    s_kh.append("(");
                    String[] khs = params.get("kh");
                    if (khs != null && khs.length > 2) {
                        String kh = khs[1].replace("，", ",");
                        if (kh.contains(",")) {
                            String[] kh_arr = kh.split(",");
                            for (int i = 0; i < kh_arr.length; i++) {
                                if (CommonUtils.isNotEmpty(kh_arr[i])) {
                                    if (kh_arr[i] != null && kh_arr[i].length() > 0) {
                                        String s = convert("kh", kh_arr[i], khs[0]);
                                        s_kh.append(s);
                                    }
                                }
                                if (i < kh_arr.length - 1) {
                                    s_kh.append(" OR ");
                                }
                            }
                        } else {
                            String s = convert("kh", kh, khs[0]);
                            s_kh.append(s);
                        }
                    }
                    s_kh.append(")");
                }
                // 设备号
                StringBuilder s_sbh = new StringBuilder();
                if (CommonUtils.isNotEmpty(params.get("sbh"))) {
                    s_sbh.append("(");
                    String[] sbhs = params.get("sbh");
                    if (sbhs != null && sbhs.length > 2) {
                        String sbh = sbhs[1].replace("，", ",");
                        if (sbh.contains(",")) {
                            String[] sbh_arr = sbh.split(",");
                            for (int i = 0; i < sbh_arr.length; i++) {
                                if (CommonUtils.isNotEmpty(sbh_arr[i])) {
                                    String s = convert("sbh", sbh_arr[i], sbhs[0]);
                                    s_sbh.append(s);
                                    if (i < sbh_arr.length - 1) {
                                        s_sbh.append(" OR ");
                                    }
                                }
                            }
                        } else {
                            String s = convert("sbh", sbh, sbhs[0]);
                            s_sbh.append(s);
                        }
                    }
                    s_sbh.append(")");
                }
                // 对方号码
                StringBuilder s_dfhm = new StringBuilder();
                if (CommonUtils.isNotEmpty(params.get("dfhm"))) {
                    s_dfhm.append("(");
                    String[] dfhms = params.get("dfhm");
                    if (dfhms != null && dfhms.length > 2) {
                        String dfhm = dfhms[1].replace("，", ",");
                        if (dfhm.contains(",")) {
                            String[] dfhm_arr = dfhm.split(",");
                            if (isNotEmpty(dfhm_arr)) {
                                for (int i = 0; i < dfhm_arr.length; i++) {
                                    if (dfhm_arr[i] != null && dfhm_arr[i].length() > 0) {
                                        String s = convert("dfhm", dfhm_arr[i], dfhms[0]);
                                        s_dfhm.append(s);
                                    }
                                    if (i < dfhm_arr.length - 1) {
                                        s_dfhm.append(" OR ");
                                    }
                                }
                            }
                        } else {
                            String s = convert("dfhm", dfhm, dfhms[0]);
                            s_dfhm.append(s);
                        }
                    }
                    s_dfhm.append(")");
                }
                // 呼叫转移号码
                StringBuilder s_hjzyhm = new StringBuilder();
                if (CommonUtils.isNotEmpty(params.get("hjzyhm"))) {
                    s_hjzyhm.append("(");
                    String[] hjzyhms = params.get("hjzyhm");
                    String hjzyhm = hjzyhms[1].replace("，", ",");
                    if (hjzyhm.contains(",")) {
                        String[] hjzyhm_arr = hjzyhm.split(",");
                        for (int i = 0; i < hjzyhm_arr.length; i++) {
                            if (CommonUtils.isNotEmpty(hjzyhm_arr[i])) {
                                String s = convert("hjzyhm", hjzyhm_arr[i], hjzyhms[0]);
                                s_hjzyhm.append(s);
                            }
                            if (i < hjzyhm_arr.length - 1) {
                                s_hjzyhm.append(" OR ");
                            }
                        }
                    } else {
                        String s = convert("hjzyhm", hjzyhm, hjzyhms[0]);
                        s_hjzyhm.append(s);
                    }
                    s_hjzyhm.append(")");
                }
                // 开始大区小区
                StringBuilder s_startlac = new StringBuilder();
                if (CommonUtils.isNotEmpty(params.get("startlaccellid"))) {
                    String[] startlacs = params.get("startlaccellid");
                    String startlaccid = startlacs[1].replace("，", ",").replace("；", ";");
                    if (startlaccid.contains(";")) {
                        String[] startlac_arr = startlaccid.split(";");
                        for (int i = 0; i < startlac_arr.length; i++) {
                            s_startlac.append("(");
                            if (startlac_arr[i].contains(",")) {
                                String[] lc_arr = startlac_arr[i].split(",");
                                String s = convert("startlac", lc_arr[0], startlacs[0]);
                                s_startlac.append(s);
                                if (lc_arr.length > 1 && CommonUtils.isNotEmpty(lc_arr[1])) {
                                    String slac = convert(" AND startcellid", lc_arr[1], startlacs[0]);
                                    s_startlac.append(slac);
                                }
                            } else {
                                s_startlac.append(convert("startlac", startlac_arr[i], startlacs[0]));
                            }
                            s_startlac.append(")");
                            if (i < startlac_arr.length - 1) {
                                s_startlac.append(" OR ");
                            }
                        }
                    } else {
                        s_startlac.append(convert("startlac", startlaccid, startlacs[0]));
                    }
                }
                // 结束大区小区
                StringBuilder s_endlac = new StringBuilder();
                if (CommonUtils.isNotEmpty(params.get("endlaccellid"))) {
                    String[] endlacs = params.get("endlaccellid");
                    String endlaccid = endlacs[1].replace("，", ",").replace("；", ";");
                    if (endlaccid.contains(";")) {
                        if (endlaccid.contains(";")) {
                            String[] startlac_arr = endlaccid.split(";");
                            for (int i = 0; i < startlac_arr.length; i++) {
                                s_endlac.append("(");
                                if (startlac_arr[i].contains(",")) {
                                    String[] lc_arr = startlac_arr[i].split(",");
                                    String s = convert("lac", lc_arr[0], endlacs[0]);
                                    s_endlac.append(s);
                                    if (lc_arr.length > 1 && CommonUtils.isNotEmpty(lc_arr[1])) {
                                        String slac = convert(" AND cellid", lc_arr[1], endlacs[0]);
                                        s_endlac.append(slac);
                                    }
                                } else {
                                    s_endlac.append(convert("lac", startlac_arr[i], endlacs[0]));
                                }
                                s_endlac.append(")");
                                if (i < startlac_arr.length - 1) {
                                    s_endlac.append(" OR ");
                                }
                            }
                        } else {
                            s_endlac.append(convert("lac", endlaccid, endlacs[0]));
                        }
                    }
                    // s_endlac.append(")");
                }
                // 是否接通
                StringBuilder s_sfjt = new StringBuilder();
                if (CommonUtils.isNotEmpty(params.get("sfjt"))) {
                    String[] sfjt = params.get("sfjt");
                    s_sfjt.append("(sjjt:");
                    s_sfjt.append(sfjt[0]);
                    s_sfjt.append(")");
                }
                // 通话时长
                StringBuilder s_thsc = new StringBuilder();
                if (CommonUtils.isNotEmpty(params.get("thsc"))) {
                    s_thsc.append("(");
                    String[] thscs = params.get("thsc");
                    String thsc = thscs[1].replace("，", ",");
                    if (thsc.contains(",")) {
                        String[] thsc_arr = thscs[1].split(",");
                        int min = Integer.parseInt(thsc_arr[0]);
                        int max = Integer.parseInt(thsc_arr[1]);
                        for (int i = min; i <= max; i++) {
                            String s = "thsc: " + i;
                            s_thsc.append(s);
                            if (i <= max - 1) {
                                s_thsc.append(" OR ");
                            }
                        }
                    }
                    s_thsc.append(")");
                }
                // 交换机号
                StringBuilder s_msc = new StringBuilder();
                if (CommonUtils.isNotEmpty(params.get("msc"))) {
                    s_msc.append("(");
                    String[] mscs = params.get("msc");
                    String msc = mscs[1].replace("，", ",");
                    if (msc.contains(",")) {
                        String[] msc_arr = msc.split(",");
                        for (int i = 0; i < msc_arr.length; i++) {
                            if (CommonUtils.isNotEmpty(msc_arr[i])) {
                                String s = convert("msc", msc_arr[i], mscs[0]);
                                s_msc.append(s);
                            }
                            if (i < msc_arr.length - 1) {
                                s_msc.append(" OR ");
                            }
                        }
                    } else {
                        String s = convert("msc", msc, mscs[0]);
                        s_msc.append(s);
                    }
                    s_msc.append(")");
                }
                // 服务号码归属地
                StringBuilder s_fwhmgsd = new StringBuilder();
                if (CommonUtils.isNotEmpty(params.get("fwhmgsd"))) {
                    s_fwhmgsd.append("(");
                    String[] fwhmgsds = params.get("fwhmgsd");
                    String fwhmgsd = fwhmgsds[1].replace("，", ",");
                    if (fwhmgsd.contains(",")) {
                        String[] fwhmgsd_arr = fwhmgsd.split(",");
                        for (int i = 0; i < fwhmgsd_arr.length; i++) {
                            if (CommonUtils.isNotEmpty(fwhmgsd_arr[i])) {
                                String s = convert("fwhmgsd", fwhmgsd_arr[i], fwhmgsds[0]);
                                s_fwhmgsd.append(s);
                            }
                            if (i < fwhmgsd_arr.length - 1) {
                                s_fwhmgsd.append(" OR ");
                            }
                        }
                    } else {
                        String s = convert("fwhmgsd", fwhmgsd, fwhmgsds[0]);
                        s_fwhmgsd.append(s);
                    }
                    s_fwhmgsd.append(")");
                }
                // 落单地区号
                StringBuilder s_ldmyqh = new StringBuilder();
                if (CommonUtils.isNotEmpty(params.get("ldmyqh"))) {
                    s_ldmyqh.append("(");
                    String[] ldmyqhs = params.get("ldmyqh");
                    String ldmyqh = ldmyqhs[1].replace("，", ",");
                    if (ldmyqh.contains(",")) {
                        String[] ldmyqh_arr = ldmyqh.split(",");
                        for (int i = 0; i < ldmyqh_arr.length; i++) {
                            if (CommonUtils.isNotEmpty(ldmyqh_arr[i])) {
                                String s = convert("ldmyqh", ldmyqh_arr[i], ldmyqhs[0]);
                                s_ldmyqh.append(s);
                            }
                            if (i < ldmyqh_arr.length - 1) {
                                s_ldmyqh.append(" OR ");
                            }
                        }
                    } else {
                        String s = convert("ldmyqh", ldmyqh, ldmyqhs[0]);
                        s_ldmyqh.append(s);
                    }
                    s_ldmyqh.append(")");
                }
                // 对方国家代码
                StringBuilder s_dfgjdm = new StringBuilder();
                if (CommonUtils.isNotEmpty(params.get("dfgjdm"))) {
                    s_dfgjdm.append("(");
                    String[] dfgjdms = params.get("dfgjdm");
                    String dfgjdm = dfgjdms[1].replace("，", ",");
                    if (dfgjdm.contains(",")) {
                        String[] dfgjdm_arr = dfgjdm.split(",");
                        for (int i = 0; i < dfgjdm_arr.length; i++) {
                            if (CommonUtils.isNotEmpty(dfgjdm_arr[i])) {
                                String s = convert("dfgjdm", dfgjdm_arr[i], dfgjdms[0]);
                                s_dfgjdm.append(s);
                            }
                            if (i < dfgjdm_arr.length - 1) {
                                s_dfgjdm.append(" OR ");
                            }
                        }
                    } else {
                        String s = convert("dfgjdm", dfgjdm, dfgjdms[0]);
                        s_dfgjdm.append(s);
                    }
                    s_dfgjdm.append(")");
                }
                // 设备IP
                StringBuilder s_sbip = new StringBuilder();
                if (CommonUtils.isNotEmpty(params.get("sbip"))) {
                    s_sbip.append("(");
                    String[] sbips = params.get("sbip");
                    String sbip = sbips[1].replace("，", ",");
                    if (sbip.contains(",")) {
                        String[] sbip_arr = sbip.split(",");
                        for (int i = 0; i < sbip_arr.length; i++) {
                            if (CommonUtils.isNotEmpty(sbip_arr[i])) {
                                String s = convert("sbip", sbip_arr[i], sbips[0]);
                                s_sbip.append(s);
                            }
                            if (i < sbip_arr.length - 1) {
                                s_sbip.append(" OR ");
                            }
                        }
                    } else {
                        String s = convert("sbip", sbip, sbips[0]);
                        s_sbip.append(s);
                    }
                    s_sbip.append(")");
                }
                // 拼装所有条件
                // 类型
                stringresult = new StringBuffer();
                if (CommonUtils.isNotEmpty(s_lx.toString())) {
                    stringresult.append(" AND ");
                    stringresult.append(s_lx);
                }
                // 卡号
                if (CommonUtils.isNotEmpty(s_kh.toString())) {
                    stringresult.append(" AND ");
                    stringresult.append(s_kh);
                }
                // 设备号
                if (CommonUtils.isNotEmpty(s_sbh.toString())) {
                    stringresult.append(" AND ");
                    stringresult.append(s_sbh);
                }
                // 服务号码
                if (CommonUtils.isNotEmpty(s_fwhm.toString())) {
                    stringresult.append(" AND ");
                    stringresult.append(s_fwhm);
                }
                // 对方号码
                if (CommonUtils.isNotEmpty(s_dfhm.toString())) {
                    stringresult.append(" AND ");
                    stringresult.append(s_dfhm);
                }
                // 呼叫转移号码
                if (CommonUtils.isNotEmpty(s_hjzyhm.toString())) {
                    stringresult.append(" AND ");
                    stringresult.append(s_hjzyhm);
                }
                // 开始大区
                if (CommonUtils.isNotEmpty(s_startlac.toString())) {
                    stringresult.append(" AND ");
                    stringresult.append(s_startlac);
                }
                // 结束大区
                if (CommonUtils.isNotEmpty(s_endlac.toString())) {
                    stringresult.append(" AND ");
                    stringresult.append(s_endlac);
                }
                // 是否接通
                if (CommonUtils.isNotEmpty(s_sfjt.toString())) {
                    stringresult.append(" AND ");
                    stringresult.append(s_sfjt);
                }
                // 通话时长
                if (CommonUtils.isNotEmpty(s_thsc.toString())) {
                    stringresult.append(" AND ");
                    stringresult.append(s_thsc);
                }
                // msc
                if (CommonUtils.isNotEmpty(s_msc.toString())) {
                    stringresult.append(" AND ");
                    stringresult.append(s_msc);
                }
                // 服务号码归属地
                if (CommonUtils.isNotEmpty(s_fwhmgsd.toString())) {
                    stringresult.append(" AND ");
                    stringresult.append(s_fwhmgsd);
                }
                // 落单地区号
                if (CommonUtils.isNotEmpty(s_ldmyqh.toString())) {
                    stringresult.append(" AND ");
                    stringresult.append(s_ldmyqh);
                }
                // 对方国家代码
                if (CommonUtils.isNotEmpty(s_dfgjdm.toString())) {
                    stringresult.append(" AND ");
                    stringresult.append(s_dfgjdm);
                }
                // 设备IP
                if (CommonUtils.isNotEmpty(s_sbip.toString())) {
                    stringresult.append(" AND ");
                    stringresult.append(s_sbip);
                }
                return stringresult.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringresult.toString();
    }

    /**
     * 获取执法监督内容
     *
     * @param starttime 开始时间
     * @param endtime   结束时间
     * @param laccids   大小区
     * @param fwhm      服务号码
     * @param kh        卡号
     * @param sbh       设备号
     * @return 内容字符串
     */
    public static String getContent(String starttime, String endtime, String laccids, String fwhm, String kh,
                                    String sbh) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(starttime) && StringUtils.isNotBlank(endtime)) {
            sb.append("开始时间:").append(starttime).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;").append("结束时间:")
                    .append(endtime).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        if (StringUtils.isNotBlank(laccids)) {
            sb.append("大区小区:").append(laccids).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        if (StringUtils.isNotBlank(fwhm)) {
            sb.append("服务号码:").append(fwhm).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        if (StringUtils.isNotBlank(kh)) {
            sb.append("卡号:").append(kh).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        if (StringUtils.isNotBlank(sbh)) {
            sb.append("设备号:").append(sbh).append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
        }
        return sb.toString();
    }

    /**
     * 将目标字符串按行切分
     *
     * @param lineCount 行数
     * @param lineNum   每行字符数
     * @param resource  源字符串
     * @return 结果字符串
     */
    public static String splitStrForLine(int lineCount, int lineNum, String resource) {
        if (CommonUtils.isNotEmpty(resource) && lineCount > 0 && lineNum > 0) {
            char[] ca = resource.toCharArray();
            StringBuilder s = new StringBuilder();
            int i = 0;
            int lc = 0;
            for (char c : ca) {
                if (lc == lineCount) {
                    break;
                }
                s.append(c);
                if (i != 0 && i % lineNum == 0) {
                    s.append("</br>");
                    lc++;
                }
                i++;
            }
            return s.toString();
        }
        return null;
    }

    /**
     * 序列化map对象
     *
     * @param map map对象
     * @return 序列化字符串
     */
    public static String serializeMap(Map<String, List> map) {
        if (map != null && map.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Entry<String, List> entry : map.entrySet()) {
                String key = entry.getKey();
                List list = entry.getValue();
                if (list != null) {
                    String value = serializeList(list);
                    sb.append(key);
                    sb.append(",");
                    sb.append(value);
                    sb.append(";");
                }
            }
            String str = sb.toString();
            if (!"".equals(str)) {
                str = str.substring(0, str.length() - 1);
            }
            return str;
        }
        return null;
    }

    /**
     * 反序列化map对象
     *
     * @param mapString 序列化字符串
     * @return map对象
     */
    public static Map<String, List> deseriaMap(String mapString) {
        Map<String, List> map = new HashMap<>();
        if (mapString != null && !"".equals(mapString)) {
            String[] ele = mapString.split(";");
            for (String e : ele) {
                String[] keyValue = e.split(",");
                map.put(keyValue[0], deserializeList(keyValue[1]));
            }
            return map;
        }
        return null;
    }


    public static <T> T getFirst(List<T> list){
        if(isEmpty(list)){
            return null;
        }
        for (T t : list) {
            if(t != null){
                return t;
            }
        }
        return null;
    }

    public static String strToHex(String str){
        char[] cs = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char c : cs) {
            if(c > 256){
                sb.append("\\u" + Integer.toHexString(c));
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

}