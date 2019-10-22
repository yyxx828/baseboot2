package com.xajiusuo.jpa.param.service.impl;

import com.xajiusuo.jpa.config.BaseDao;
import com.xajiusuo.jpa.config.BaseServiceImpl;
import com.xajiusuo.jpa.param.dao.ConfigDao;
import com.xajiusuo.jpa.param.dao.ResultDao;
import com.xajiusuo.jpa.param.entity.ConfigBean;
import com.xajiusuo.jpa.param.entity.ResultBean;
import com.xajiusuo.jpa.param.service.ConfigService;
import com.xajiusuo.jpa.param.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 杨勇 on 19-06-10.
 */
@Service
public class ConfigServiceImpl extends BaseServiceImpl<ConfigBean, String> implements ConfigService {


    private static Map<String,String> map = new HashMap<String,String>();

    @Autowired
    private ConfigDao entityRepository;


    @Override
    public BaseDao<ConfigBean, String> getBaseDao() {
        return entityRepository;
    }

}
