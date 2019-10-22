package com.xajiusuo.jpa.config;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by sht on 2018/1/17.
 * 此方式为主键为INT
 */
/**
 * @company 西安九索通讯科技有限公司
 * @author 孙海涛
 *
 */
// 标注为@MappedSuperclass的类将不是一个完整的实体类，他将不会映射到数据库表，但是他的属性都将映射到其子类的数据库字段中。
@MappedSuperclass
public class BaseIntEntity extends TableEntity implements Serializable {
    private static final long	serialVersionUID = 1L;
    /**
     *  ID 使用INT
     *
     * @return
     */
    @Id
    @SequenceGenerator(name="SeqGenerator",sequenceName="xxcp_orcl",allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE,generator="SeqGenerator")
    @ApiModelProperty(required = false, value = "主键", dataType = "int")
    protected Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
