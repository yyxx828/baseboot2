package com.xajiusuo.jpa.config;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by sht on 2018/1/23.
 */
@Component
@PropertySource(value="classpath:/message_zh_CN.properties",encoding="UTF-8")
public class PropertiesConfig {

    @Resource
    private   Environment env;

    private String key;

    private String name;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public   String getValue(String key) {
        return env.getRequiredProperty(key);
    }

    public String getV(String key){
        try{
            return getValue(key.replace("_",".").toLowerCase());
        }catch (Exception e){
            return null;
        }
    }
}
