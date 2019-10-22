package com.xajiusuo.jpa.config;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Sets;
import com.xajiusuo.jpa.config.e.BeanDelete;
import com.xajiusuo.jpa.config.e.BeanInit;
import com.xajiusuo.jpa.param.entity.UserContainer;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

/**
 * Created by sht on 2018/1/25.
 */
@MappedSuperclass
public class TableEntity extends BeanDelete implements Serializable {

    /** 创建时间 */
    @ApiModelProperty(required = false, value = "创建时间", dataType = "date")
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    protected Date createTime;
    /** 最后更新时间 */
    @ApiModelProperty(required = false, value = "修改时间", dataType = "date")
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    protected Date lastModifyTime;
    /** 创建人 */
    @ApiModelProperty(required = false, value = "创建人ID", dataType = "int")
    protected Integer createUID;
    /** 修改人 */
    @ApiModelProperty(required = false, value = "修改人ID", dataType = "int")
    protected Integer lastModifyUID;
    /** 创建人名称 */
    @ApiModelProperty(required = false, value = "创建人姓名", dataType = "string")
    protected String createName;

    @ApiModelProperty(value = "创建时间", dataType = "Date", notes = "新增时不要传进", hidden = true)
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false, updatable = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @ApiModelProperty(value = "修改时间", dataType = "Date", notes = "新增时不要传进", hidden = true)
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    @Column(insertable = false)
    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    @ApiModelProperty(value = "创建人id", dataType = "Integer", notes = "新增时不要传进", hidden = true)
    public Integer getCreateUID() {
        return createUID;
    }

    public void setCreateUID(Integer createUID) {
        this.createUID = createUID;
    }
    @ApiModelProperty(value = "更新人id", dataType = "Integer", notes = "新增时不要传进", hidden = true)
    public Integer getLastModifyUID() {
        return lastModifyUID;
    }

    public void setLastModifyUID(Integer lastModifyUID) {
        this.lastModifyUID = lastModifyUID;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    @PrePersist
    @PreUpdate
    public void updateDate(){
        if(createTime==null){
            createTime = new Date();
        }
        if(createUID == null){
            createUID = UserContainer.getId();
        }
        if(createName == null){
            createName = UserContainer.getFullName();
        }
        lastModifyTime = new Date();
        lastModifyUID = UserContainer.getId();
        if(delFlag == null){
            delFlag = false;
        }
    }

}
