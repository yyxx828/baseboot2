package com.xajiusuo.jpa.param.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xajiusuo.jpa.param.e.Result;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * Created by 杨勇 on 19-06-10..自定义字段对应值
 */
@Entity
@Table(name = "P_RESULT_TABLE_11")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ResultBean implements Serializable {

    @Id
    private String enumName;
    private String code;
    private String msg;

    public ResultBean() {
    }

    public ResultBean(Result result) {
        this.enumName = result.name();
        this.code = result.getCode().name();
        this.msg = result.getMsg();
    }

    public String getEnumName() {
        return enumName;
    }

    public void setEnumName(String enumName) {
        this.enumName = enumName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultBean that = (ResultBean) o;
        return Objects.equals(enumName, that.enumName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enumName);
    }
}
