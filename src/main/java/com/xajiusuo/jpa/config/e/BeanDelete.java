package com.xajiusuo.jpa.config.e;

import com.xajiusuo.jpa.config.BaseDaoImpl;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.MappedSuperclass;

/**
 * Created by 杨勇 on 19-6-27.
 */
@MappedSuperclass
public class BeanDelete extends BeanInit{

    /** 删除标记 */
    @ApiModelProperty(required = false, value = "删除标记", dataType = "boolean")
    protected Boolean delFlag;


    public Boolean getDelFlag() {
        if(delFlag == null)
            return false;
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        if(!BaseDaoImpl.willDelete()) return;
        this.delFlag = delFlag;
    }

}
