package com.xajiusuo.jpa.param.dao;

import com.xajiusuo.jpa.config.BaseDao;
import com.xajiusuo.jpa.param.entity.ConfigBean;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by 杨勇 on 18-8-20.
 */
public interface ConfigDao extends BaseDao<ConfigBean,String>,JpaSpecificationExecutor<ConfigBean> {

}
