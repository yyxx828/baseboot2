package com.xajiusuo.jpa.util;

import com.xajiusuo.jpa.config.BaseIntEntity;
import com.xajiusuo.jpa.config.TableEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * SQL常用工具
 *
 * @author xhj
 */
public class SqlUtils {


    /**
     * 若trim后length为1则返回null 前后添加[]
     *
     * @param s
     * @return
     */
    public static String sqlLike1(String s) {
        if(StringUtils.isBlank(s))
            return null;
        return "%[" + s.trim() + "]%";
    }

    ////////////////////

    /**
     * 若trim后length为1则返回null
     *
     * @param s SQL条件
     * @return like语句格式的条件
     */
    public static String sqlLike(String s) {
        if (s == null || s.trim().length() < 1)
            return null;
        return "%" + s.trim() + "%";
    }

    /**
     * 若trim后length为1则返回null
     *
     * @param s SQL条件
     * @return like语句格式的条件
     */
    public static String sqllLike(String s) {
        if (s == null || s.trim().length() < 1)
            return null;
        return "%" + s.trim();
    }

    /**
     * 若trim后length为1则返回null
     *
     * @param s SQL条件
     * @return like语句格式的条件
     */
    public static String sqlrLike(String s) {
        if (s == null || s.trim().length() < 1)
            return null;
        return  s.trim()+"%";
    }

    /**
     * s 以,分割，进行添加''。
     *
     * @param s SQL条件
     * @return in语句格式的条件
     */
    public static String sqlIn(String s) {
        if (s == null || s.trim().length() < 1) {
            return "''";
        }
        return sqlIn(Arrays.asList(s.split(",")));
    }

    /**
     * 将集合中的元素添加到SQL in语句中
     *
     * @param s SQL条件
     * @return in语句格式的条件
     */
    public static String sqlIn(Collection<?> s) {
        if (s == null || s.size() < 1) {
            return "''";
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Object o : s) {
            if(o == null) continue;
            if (i++ != 0) {
                sb.append(",");
            }
            sb.append("'").append(o.toString()).append("'");
        }
        return sb.toString();
    }

    /**
     * 将数组中的元素添加到SQL in语句中
     *
     * @param s SQL条件
     * @return in语句格式的条件
     */
    public static String sqlIn(Object[] s) {
        if (s == null || s.length < 1) {
            return "''";
        }
        return sqlIn(Arrays.asList(s));
    }

    /**
     * 字符串去除空白字符
     *
     * @param s 要处理的字符串
     * @return 去除空白字符后的字符串
     */
    public static String trim(String s) {
        if (s == null) {
            return null;
        }
        return s.trim();
    }

    /**
     * 将集合中的元素拼装成指定字段名的OR条件语句
     *
     * @param s    指定集合
     * @param name 指定字段名
     * @return OR条件语句
     */
    public static String sSOr(Collection<?> s, String name) {
        if (s == null || s.size() < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" AND (");
        int i = 0;
        for (Object o : s) {
            if (i++ != 0) {
                sb.append("");
            }
            sb.append(name).append(":").append(o.toString()).append(" OR ");
        }
        return sb.toString().substring(0, sb.toString().length() - 3) + ")";
    }

    public static Object[] convertHSql(String hsql, StringBuilder sb, Object[] params) {
        if(!hsql.contains("?")) {
            sb.append(hsql);
            return params;
        }
        List<Object> np = new ArrayList<Object>();
        String[] hs = hsql.replaceAll("/", "").split("\\?");
        int i = 0;
        if(params != null) {
            for(i = 0; i < params.length; i++) {
                if(hs[i].contains("~")) {
                    if(params[i] == null
                            || (params[i] instanceof String && StringUtils.isEmpty(params[i].toString().trim()))) {
                        sb.append(hs[i].substring(0, hs[i].indexOf("~")));
                        String s = hs[i + 1];
                        hs[i + 1] = s.replaceFirst(s.substring(0, s.indexOf("~") + 1).replace(")", "\\)"), "");
                    }else {
                        sb.append(hs[i].replaceFirst("~", "") + "?");
                        np.add(params[i]);
                        hs[i + 1] = hs[i + 1].replaceFirst("~", "");
                    }
                }else {
                    sb.append(hs[i] + "?");
                    np.add(params[i]);
                }
            }
        }
        if(hs.length > i) {
            sb.append(hs[i]);
        }
        return np.toArray();
    }

    public static String toHqlCount(String hql) {
        if(StringUtils.isEmpty(hql))
            return "";
        if(hql.contains("group by ")) {
            String g = hql.substring(hql.indexOf("group by ") + "group by ".length());
            if(g.contains(" ")) {
                g = g.substring(0, g.indexOf(" "));
            }
            return "select count(" + g + ") " + hql.substring(hql.indexOf("from"));
        }else if(hql.contains("distinct")) {
            return "select count(" + hql.substring(hql.indexOf("select") + "select".length(), hql.indexOf("from")).replace(" , ", " || ") + ")" + hql.substring(hql.indexOf("from"));
        }
        return "select count(*) " + hql.substring(hql.indexOf("from"));
    }

    private static Map<Class<?>,String> map = new HashMap<Class<?>,String>();

    public static String tableName(Class<?> te){
        String tn = map.get(te);
        if(tn == null){
            tn = te.getAnnotation(Table.class).name();
            map.put(te,tn);
        }
        return tn;

    }

    public static void toHql0(List<Object> param, TableEntity entity, String split, StringBuilder hql, String pre,
                              Set<Object> has) {
        if(entity != null) {
            if(pre == null) {
                pre = "";
            }
            if(has == null) {
                has = new HashSet<Object>();
            }
            if(split == null){
                split = "";
            }
            IMapp m = Mapp.createMapp(entity);
            for(String s : m.getFields()) {
                Field f = m.getField(s);
                OneToMany otm = f.getAnnotation(OneToMany.class);
                Transient t = f.getAnnotation(Transient.class);

                if(t == null && otm == null) {
                    Class<?> ft = f.getType();
                    if(Number.class.isAssignableFrom(ft)
                            || ft.equals(Boolean.class)
                            || ft.equals(String.class)
                            || ft.equals(Date.class)
                            || TableEntity.class.isAssignableFrom(ft)
                            || String.class.equals(ft)) {
                        Object o = m.getObjectbyField(s);
                        if(o == null) {
                            continue;
                        }
                        if(o instanceof TableEntity) {
                            if(!has.contains(o)) {
                                has.add(o);
                                toHql0(param, (BaseIntEntity)o, split, hql, pre + s + ".", has);
                            }
                            continue;
                        }
                        if(o instanceof String) {
                            if(StringUtils.isBlank((String)o)) {
                                continue;
                            }
                            if(o.toString().contains("%")) {
                                param.add(o.toString());
                            }else {
                                param.add(SqlUtils.sqlLike(o.toString()));
                            }
                            hql.append(split + " and e." + pre + s + " like ?" + split);
                        }else {
                            param.add(o);
                            hql.append(split + " and e." + pre + s + " = ?" + split);
                        }
                    }
                }
            }
            m.unLoad();
        }
    }

    public static String addSqlOrderby(Pageable pageable, String as){
        String orderBy = "";
        if(pageable.getSort()!=null){
            orderBy += " order by";
            Iterator<Sort.Order> it = pageable.getSort().iterator();
            while(it.hasNext()){
                Sort.Order order = it.next();
                orderBy += " ";
                if(as!=null && !"".equals(as)){
                    orderBy += as+".";
                }
                orderBy += order.getProperty()+" "+order.getDirection();
                if(it.hasNext()) orderBy += ",";
            }
        }
        return orderBy;
    }

    public static String convertCharset(String s) {
        if (s != null) {
            try {
                int length = s.length();
                byte[] buffer = new byte[length];
                // 0x81 to Unicode 0x0081, 0x8d to 0x008d, 0x8f to 0x008f, 0x90
                // to 0x0090, and 0x9d to 0x009d.
                for (int i = 0; i < length; ++i) {
                    char c = s.charAt(i);
                    if (c == 0x0081) {
                        buffer[i] = (byte) 0x81;
                    } else if (c == 0x008d) {
                        buffer[i] = (byte) 0x8d;
                    } else if (c == 0x008f) {
                        buffer[i] = (byte) 0x8f;
                    } else if (c == 0x0090) {
                        buffer[i] = (byte) 0x90;
                    } else if (c == 0x009d) {
                        buffer[i] = (byte) 0x9d;
                    } else {
                        buffer[i] = Character.toString(c).getBytes("CP1252")[0];
                    }
                }
                String result = new String(buffer, "UTF-8");
                return result;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return s;
    }

    /***
     * 获取seq名称
     * @return
     */
    public static String getIntSeqName(){
        try{
            SequenceGenerator seq = BaseIntEntity.class.getDeclaredField("id").getAnnotation(SequenceGenerator.class);
            return seq.sequenceName();
        }catch (Exception e){
            return "xxcp_orcl";
        }
    }

}
