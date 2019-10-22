package com.xajiusuo.jpa.param.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xajiusuo.jpa.param.e.Configs;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by 杨勇 on 19-06-10..自定义字段对应值
 */
@Entity
@Table(name = "P_CONFIG_TABLE_11")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ConfigBean implements Serializable {

    @Id
    private String confName;

    private String type;
    private String describe;
    private String value;

    public ConfigBean() {
    }

    public ConfigBean(Configs conf) {
        this.confName = conf.name();
        this.type = conf.getType().name();
        this.describe = conf.getDesc();
        this.value = conf.getValue();
    }

    public String getConfName() {
        return confName;
    }

    public void setConfName(String confName) {
        this.confName = confName;
    }

    public String getType() {
        return type;
    }

    public String getTypes() {
        try{
            return Configs.DataType.valueOf(type).getDesc();
        }catch (Exception e){
            return null;
        }
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigBean that = (ConfigBean) o;
        return Objects.equals(confName, that.confName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(confName);
    }
}
