package com.xajiusuo.jpa.config;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * @company 西安九索通讯科技有限公司
 * @author 孙海涛
 *此方式为UUID的主键新增
 */
// 标注为@MappedSuperclass的类将不是一个完整的实体类，他将不会映射到数据库表，但是他的属性都将映射到其子类的数据库字段中。
@MappedSuperclass
public class BaseEntity extends TableEntity {

    private static final long	serialVersionUID = 1L;

    /**
     * UUID 使用String
     *
     * @return
     */
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @ApiModelProperty(required = false, value = "主键", dataType = "string")
    protected String id;						// ID

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
