package com.xajiusuo.jpa.config;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sht on 2018/1/17.
 */
@Service
public abstract class BaseServiceImpl<T, PK extends Serializable> implements BaseService<T, PK> {

    @Override
    @Transactional
    public List<T> executeNativeQuerySql(String sql, Object... params) {
        return getBaseDao().executeNativeQuerySql(sql, params);
    }

    @Override
    @Transactional
    public Page<T> executeNativeQuerySqlForPage(Pageable pageable, String sql, Object... params) {
        return getBaseDao().executeQuerySqlByPage(pageable,sql, params);
    }

    @Override
    @Transactional
    public int executeUpdateSql(String sql) {
        return getBaseDao().executeUpdateSql(sql);
    }

    @Override
    @Transactional
    public int executeUpdateSql(String sql, Object... params) {
        return getBaseDao().executeUpdateSql(sql,params);
    }


}
