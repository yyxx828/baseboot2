package com.xajiusuo.jpa.param.service.impl;

import com.xajiusuo.jpa.config.BaseDao;
import com.xajiusuo.jpa.config.BaseServiceImpl;
import com.xajiusuo.jpa.param.dao.ResultDao;
import com.xajiusuo.jpa.param.entity.ResultBean;
import com.xajiusuo.jpa.param.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 杨勇 on 19-06-10.
 */
@Service
public class ResultServiceImpl extends BaseServiceImpl<ResultBean, String> implements ResultService {


    private static Map<String,String> map = new HashMap<String,String>();

    @Autowired
    private ResultDao entityRepository;


    @Override
    public BaseDao<ResultBean, String> getBaseDao() {
        return entityRepository;
    }

}
