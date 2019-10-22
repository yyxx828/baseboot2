package com.xajiusuo.jpa.param.e;

import com.xajiusuo.jpa.config.PropertiesConfig;
import com.xajiusuo.jpa.param.entity.ConfigBean;
import com.xajiusuo.jpa.param.service.ConfigService;
import com.xajiusuo.jpa.util.DynamicEnumUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 公共参数 参数名称不区分大小写,'.'和'_'为分隔符,两者等价
 * Created by hadoop on 19-6-6.
 */
public enum Configs {

    //成功请求
    COM_DEPLY_USE(DataType.BOOLEAN,"参数使用环境TRUE:只读数据库,false:优先本地配置", "true")
    ,COM_CHARSET_NAME(DataType.STRING,"文件下载使用字符集", "utf-8")

    ,COM_CONFIG_TABLE("配置匹配数据表(忽略修改)","P_CONFIG_TABLE_11")
    ,COM_RESULT_TABLE("请求描述数据表(忽略修改)","P_RESULT_TABLE_11")
    ,COM_SERIAL("服务序列","")
    ,COM_LICENCE("服务许可","")
    ,COM_CURR_TIME("当前时间","")
    ,COM_TEMP_CONFIG("","")
    ;

    static SimpleDateFormat ymd1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static Thread willGo;

    static ConfigService configService;
    static PropertiesConfig propertiesConfig;
    static ThreadLocal<String> o = new ThreadLocal<String>();
    static ThreadLocal<Configs> c = new ThreadLocal<Configs>();

    SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    Configs(String desc, String value) {
        this(DataType.STRING, desc, value);
        this.edit = false;
    }

    Configs(DataType type, String desc, String value) {
        if (type == null) {
            throw new RuntimeException("type is must not null");
        }
        if (StringUtils.isBlank(desc)) {
            Assert.notNull(desc, "参数描述不能为空");
        }

        if (StringUtils.isNotBlank(value)) {
            switch (type) {
                case INT:
                    Integer.parseInt(value);
                    break;
                case DOUBLE:
                    Double.parseDouble(value);
                    break;
                case STRING:
                    break;
                case DATE:
                    try {
                        ymd.parse(value);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
            }
        }
        this.type = type;
        this.desc = desc;
        this.value = value;
        this.edit = true;
    }

    private DataType type;
    private String desc;
    private String value;

    private boolean edit;


    public String getDesc() {
        if(this == COM_TEMP_CONFIG && c.get() != null){
            return c.get().getDesc();
        }
        return desc;
    }

    public String getValue() {
        String value = this.value;
        if(this == COM_TEMP_CONFIG){
            value = o.get();
        }
        return value;
    }

    public DataType getType() {
        if(this == COM_TEMP_CONFIG && c.get() != null){
            return c.get().getType();
        }
        return type;
    }

    public boolean isEdit() {
        return edit;
    }

    public <T> T getV() {
        String value = this.value;
        DataType type = this.type;
        if(this == COM_TEMP_CONFIG){
            value = o.get();
            if(c.get() != null){
                type = c.get().type;
            }
        }
        Object val = null;
        switch (type) {
            case INT:
                if (StringUtils.isBlank(value)) {
                    val = 0;
                } else {
                    val = Integer.parseInt(value);
                }
                break;
            case DOUBLE:
                if (StringUtils.isBlank(value)) {
                    val = 0.0;
                } else {
                    val = Double.parseDouble(value);
                }
                break;
            case STRING:
                val = value;
                break;
            case BOOLEAN:
                val = value != null && ("1".equals(value) || "true".equals(value.toLowerCase()));
                break;
            case DATE:
                try {
                    if (value != null) {
                        val = ymd.format(value);
                    } else {
                        val = new Date();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        }
        return (T) val;
    }

    public int getInt() {
        String value = this.value;
        if(this == COM_TEMP_CONFIG)
            value = o.get();
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            return 0;
        }
    }

    public double getDouble() {
        String value = this.value;
        if(this == COM_TEMP_CONFIG)
            value = o.get();
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            return 0.0;
        }
    }

    public Date getDate() {
        String value = this.value;
        if(this == COM_TEMP_CONFIG)
            value = o.get();
        try {
            return ymd.parse(value);
        } catch (Exception e) {
            return new Date();
        }
    }

    public boolean getBoolean() {
        String value = this.value;
        if(this == COM_TEMP_CONFIG)
            value = o.get();
        return value != null && ("1".equals(value) || "true".equals(value.toLowerCase()));
    }

    /***
     * 查找配置,根据优先原则
     *
     * @return
     */
    public static Configs find(Object[] os) {
        if(os[0] instanceof String){
            return find((String) os[0]);
        }
        throw new RuntimeException("没有查询到的参数");
    }

    /***
     * 查找配置,根据优先原则
     *
     * @param name
     * @return
     */
    public static Configs find(String name) {
        if (!COM_DEPLY_USE.getBoolean()) {//优先本地
            String s = findPro(name);
            Configs t = null;
            try{
                t = findEnum(name);
            }catch (Exception e){
            }
            if (StringUtils.isNotBlank(s)) {
                return COM_TEMP_CONFIG.setV(s, t);
            }
        }
        return findEnum(name);
    }

    /***
     * 查找公共配置
     *
     * @param name
     * @return
     */
    public static Configs findEnum(String name) {
        try {
            return Configs.valueOf(name.replace(".", "_").toUpperCase());
        } catch (Exception e) {
            throw e;
        }
    }

    public static String findPro(String name){
        return propertiesConfig.getV(name);
    }

    /***
     * 查找name对应的返回类型,如果没查询到,则按照指定类型进行添加
     *
     * @param name
     * @param desc
     * @param value
     * @return
     */
    public static Configs findAdd(String name, DataType type, String desc, Object value) {
        try {
            Configs c = find(name);
            if(c == COM_TEMP_CONFIG){
                throw new RuntimeException();
            }
            return c;
        } catch (Exception e) {
            name = name.replace(".", "_").toUpperCase();
            String v = getV(type,value);
            return dynamicAdd(name,type, desc, v);
        }
    }

    public static void setConfigService(ConfigService configService, PropertiesConfig propertiesConfig) {
        Configs.configService = configService;
        Configs.propertiesConfig = propertiesConfig;
    }

    /***
     * 动态增加/修改枚举
     *
     * @param name  参数描述
     * @param value 值   @return
     */
    public static Configs update(String name, Object value) {
        Configs c = null;
        try{
            c = findEnum(name);
        }catch (Exception e){
            throw new RuntimeException("不存在的配置");
        }
        String v = getV(c.type,value);
        return dynamicAdd(name,c.type,c.desc,v);
    }

    /***
     * 动态增加/修改枚举
     *
     * @param name  参数描述
     * @param value 值   @return
     */
    public static Configs update(Object[] name, Object value) {
        if(name[0] instanceof String){
            return update((String) name[0],value);
        }
        throw new RuntimeException("没有查询到的参数");
    }

    /***
     * 动态增加/修改枚举
     *
     * @param name  参数描述
     * @param type
     *@param desc  参数描述
     * @param value 值   @return
     */
    public static Configs dynamicAdd(String name, DataType type, String desc, String value) {
        Configs old = null;
        try{
            old = findEnum(name);
            if(!old.edit && !old.equals(COM_SERIAL) && !old.equals(COM_LICENCE) && !old.equals(COM_CURR_TIME)){//不可编辑进行跳过
                return old;
            }
        }catch (Exception e){}

        if(old != null){
            if(old.getType() != type){
                throw new RuntimeException(MessageFormat.format("配置【{0}】的数据原类型为[{1}],无法变更数据类型为[{2}]",name,old.getType().name(),type.name()));
            }

            try{
                switch (type){
                    case INT:
                        Integer.parseInt(value);
                        break;
                    case DOUBLE:
                        Double.parseDouble(value);
                        break;
                    case BOOLEAN:
                        value = value.toLowerCase();
                        if(!("true".equals(value) || "false".equals(value) || "1".equals(value) || "0".equals(value))) {
                            throw new RuntimeException();
                        }
                        break;
                    case DATE:
                        try{
                            ymd1.parse(value);
                        }catch (Exception e){
                            throw new RuntimeException();
                        }
                }
            }catch (Exception e){
                throw new RuntimeException(MessageFormat.format("配置【{0}】的数据原类型为[{1}],要修改值为[{2}],错误传值",name,type.name(),value));
            }

            old.value = value;
            if(configService != null){
                configService.saveOrUpdate(new ConfigBean(old));
            }
            return old;
        }

        DynamicEnumUtil.addEnum(Configs.class, name, new Class<?>[]{DataType.class, String.class, String.class}, new Object[]{type,desc, value});
        Configs r = Configs.valueOf(name);
        if(configService != null){
            configService.saveOrUpdate(new ConfigBean(r));
        }
        return r;
    }

    public static void run() {
        if(willGo != null){
            willGo.start();
        }
    }

    public static void setWillGo(Thread willGo) {
        Configs.willGo = willGo;
    }

    private static String getV(DataType type,Object value){
        String v = null;
        switch (type){
            case INT:
                if(!(value instanceof Integer)){
                    throw new RuntimeException("传入值必须为Integer");
                }
                v = value.toString();
                break;
            case DOUBLE:
                if(!(value instanceof Double)){
                    throw new RuntimeException("传入值必须为Double");
                }
                v = value.toString();
                break;
            case STRING:
                if(!(value instanceof String)){
                    throw new RuntimeException("传入值必须为String");
                }
                v = value.toString();
                break;
            case BOOLEAN:
                if(!(value instanceof Boolean)){
                    throw new RuntimeException("传入值必须为Boolean");
                }
                v = value.toString();
                break;
            case DATE:
                if(!(value instanceof Date)){
                    throw new RuntimeException("传入值必须为Date");
                }
                v = ymd1.format(value);
                break;
        }
        return v;
    }

    public Configs setV(String v, Configs conf) {
        o.set(v);
        if(conf != null){
            c.set(conf);
        }
        return this;
    }

    public enum DataType {
        INT("整数"), DOUBLE("数字"), STRING("字符串"), DATE("日期"), BOOLEAN("布尔值");

        DataType(String desc){
            this.desc = desc;
        }

        private String desc;

        public String getDesc() {
            return desc;
        }
    }

}
