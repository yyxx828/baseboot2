package com.xajiusuo.jpa.config;

import com.xajiusuo.jpa.param.e.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by sht on 2018/1/17.
 */
public interface BaseService<T, PK extends Serializable> {


    BaseDao<T, PK> getBaseDao();


    default void queryRangeExist(){
        getBaseDao().queryRangeExist();
    }

    default void queryRangeDelete(){
        getBaseDao().queryRangeDelete();
    }

    default void queryRangeAll(){
        getBaseDao().queryRangeAll();
    }

    default Boolean getRange(){
        return getBaseDao().getRange();
    }

    /**
     * 保存实体对象.
     *
     * @param entity
     *            对象
     * @return ID
     */
    @Transactional
    default <S extends T> S save(S entity){
        return getBaseDao().save(entity);
    }

    /**
     * 保存实体对象.
     *
     * @param entity
     *            对象
     * @return ID
     */
    @Transactional
    default <S extends T> S update(S entity){
        return getBaseDao().update(entity);
    }


    /**
     * 保存实体对象.
     *
     * @param entity
     *            对象
     * @return ID
     */
    @Transactional
    default <S extends T> S saveOrUpdate(S entity){
        return getBaseDao().saveOrUpdate(entity);
    }

    /**
     * 保存实体对象.
     *
     * @param entity
     *            对象
     * @return ID
     */
    @Transactional
    default <S extends T> Result saveUpdate(S entity){
        return getBaseDao().saveUpdate(entity);
    }

    @Transactional
    default void batchSave(List<T> data) {
        getBaseDao().batchSave(data);
    }

    @Transactional
    default void batchUpdate(List<T> data) {
        getBaseDao().batchUpdate(data);
    }

    @Transactional
    default void batchSaveOrUpdate(List<T> data) {
        getBaseDao().batchSaveOrUpdate(data);
    }

    /**
     * 物理删除实体对象.
     *
     * @param entity
     *            对象
     * @return
     */
    @Transactional
    default void destroy(T entity) {
        getBaseDao().destroy(entity);
    }


    /**
     * 物理删除实体对象.
     *
     * @param id
     *            编号
     * @return
     */
    @Transactional
    default void destroy(PK id) {
        getBaseDao().destroy(id);
    }


    @Transactional
    default void destroy(PK[] ids) {
        getBaseDao().destroy(ids);
    }

    /**
     * 删除实体对象.
     *
     * @param entity
     *            对象
     * @return
     */
    @Transactional
    default void delete(T entity) {
        getBaseDao().delete(entity);
    }


    /**
     * 根据ID逻辑删除实体对象.
     *
     * @param id
     *            记录ID
     */
    @Transactional
    default void delete(PK id) {
        getBaseDao().deleteById(id);
    }

    /**
     * 根据ID数组逻辑删除实体对象.
     *
     * @param ids
     *            ID数组
     */
    @Transactional
    default void delete(PK[] ids){
        getBaseDao().delete(ids);
    }


    /**
     * 删除实体对象.
     *
     * @param entity
     *            对象
     * @return
     */
    @Transactional
    default void recover(T entity) {
        getBaseDao().recover(entity);
    }

    /**
     * 根据ID逻辑删除实体对象.
     *
     * @param id
     *            记录ID
     */
    @Transactional
    default void recover(PK id) {
        getBaseDao().recover(id);
    }

    /**
     * 根据ID数组逻辑删除实体对象.
     *
     * @param ids
     *            ID数组
     */
    @Transactional
    default void recover(PK[] ids){
        getBaseDao().recover(ids);
    }

    default T get(PK id){
        return getBaseDao().get(id);
    }


    /**
     * 根据ID获取实体对象.
     *
     * @param id
     *            记录ID
     * @return 实体对象
     */
    default T getOne(PK id){
        if(null == id){
            return null;
        }
        return getBaseDao().findById(id).get();
    }



    /**
     * 获取所有实体对象集合.
     *
     * @return 实体对象集合
     */
    default List<T> getAll(){
        return getBaseDao().findAll();
    }

    /**
     * 简单的相等的查询方法
     *
     * @param fieldName
     * @param value
     * @return
     */
    default List<T> findBy(String fieldName, Object value){
        return getBaseDao().getList(fieldName,value,null,null,null);
    }

    /**
     * 分页无条件查询
     * @param pageable
     * @return
     */
    default Page<T> findAll(Pageable pageable){
        return getBaseDao().findAll(pageable);
    }

    /**
     * 执行本地查询sql 返回实体集合
     * @param sql
     * @param params
     * @return
     */
    List<T> executeNativeQuerySql(String sql, Object... params);

    /**
     * 本地查询sql 返回实体集合并且分页
     * @param pageable
     * @param sql
     * @param params
     * @return
     */
    Page<T> executeNativeQuerySqlForPage(Pageable pageable, String sql,Object... params);

    /**
     * 执行本地修改sql，无参数 返回int
     * @param sql
     * @return
     */

    int executeUpdateSql(String sql);

    /**
     *执行本地修改sql，有参数 返回int
     * @param sql update Sql
     * @param params
     * @return
     */
    int executeUpdateSql(String sql, Object... params);


    ////////////////////////////////////////////////// 19-6-20  新增功能接口//////////////////////////////////////////////

    default EntityManager getEm(){
        return getBaseDao().getEm();
    }

    default Class<T> getEc(){
        return getBaseDao().getEc();
    }

    default String tableName(){
        return getBaseDao().tableName();
    }

    default String className(){
        return getBaseDao().className();
    }

    default String getIdName(boolean type){
        return getBaseDao().getIdName(type);
    }

    ////////////////////////////////////////////////// 19-6-20  新增业务接口//////////////////////////////////////////////

    /**
     * 根据属性名和属性值获取实体对象.
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 实体对象
     */
    default T getOne(String propertyName, Object value){
        return getBaseDao().getOne(propertyName,value);
    }


    /**
     * 根据属性名和属性值获取唯一实体,如果无或者多个会抛出异常
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 实体对象
     */
    default <S> S getOne(String propertyName, Object value,Class<S> clazz){
        return getBaseDao().getOne(propertyName,value,clazz);
    }


    /**
     * 根据属性名和属性值获取唯一实体,如果无或者多个会抛出异常
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 实体对象
     */
    default T getOne(String[] propertyName, Object[] value){
        return getBaseDao().getOne(propertyName,value);
    }




    /**
     * 根据属性名和属性值获取唯一实体,如果无或者多个会抛出异常
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 实体对象
     */
    default <S> S getOne(String[] propertyName, Object[] value,Class<S> clazz){
        return getBaseDao().getOne(propertyName,value,clazz);
    }



    /**
     * 根据属性名和属性值获第一个实体,如果无返回null
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 实体对象
     */
    default T getFirst(String propertyName, Object value){
        return getBaseDao().getFirst(propertyName,value);
    }


    /**
     * 根据属性名和属性值获第一个实体,如果无返回null
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 实体对象
     */
    default <S> S getFirst(String propertyName, Object value,Class<S> clazz){
        return getBaseDao().getFirst(propertyName,value,clazz);
    }


    /**
     * 根据属性名和属性值获第一个实体,如果无返回null
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 实体对象
     */
    default T getFirst(String[] propertyName, Object[] value){
        return getBaseDao().getFirst(propertyName,value);
    }


    /**
     * 根据属性名和属性值获第一个实体,如果无返回null
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 实体对象
     */
    default <S> S getFirst(String[] propertyName, Object[] value,Class<S> clazz){
        return getBaseDao().getFirst(propertyName,value,clazz);
    }

    /**
     * 根据属性名和属性值获取实体对象的条数.
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @param plugHql TODO
     * @param param TODO
     * @return 实体对象
     */
    default int getSize(String propertyName, Object value, String plugHql, List<Object> param) {
        return getBaseDao().getSizeByHQL(propertyName,value,plugHql,param);
    }

    /**
     * 根据属性名和属性值获取实体对象的条数.
     * @param plugHql TODO
     * @param param TODO
     *
     * @return 实体对象
     */
    default int getSize(String[] propertyNames, Object[] values, String plugHql, List<Object> param) {
        return getBaseDao().getSizeByHQL(propertyNames,values,plugHql,param);
    }


    /**
     * 根据属性名和属性值获取实体对象的条数.
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @param plugHql TODO
     * @param param TODO
     * @return 实体对象
     */
    default int getSizeLike(String propertyName, String value, String plugHql, List<Object> param){
        return getBaseDao().getSizeLikeByHQL(propertyName,value,plugHql,param);
    }

    /***
     * 查询包含属性条数
     * @param propertyNames
     * @param values
     * @param plugSql
     * @param param
     * @return
     */
    default int getSizeLike(String[] propertyNames, String[] values, String plugSql, List<Object> param){
        return getBaseDao().getSizeLikeByHQL(propertyNames,values,plugSql,param);
    }

    /**
     * 根据属性名和属性值获取实体对象集合.
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @param plugHql TODO
     * @param param TODO
     * @param order TODO
     * @return 实体对象集合
     */
    default List<T> getList(String propertyName, Object value, String plugHql, List<Object> param, String order){
        return getBaseDao().getList(propertyName,value,plugHql,param,order);
    }

    /**
     * 根据属性名和属性值获取实体对象集合.
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @param plugHql TODO
     * @param param TODO
     * @param order TODO
     * @return 实体对象集合
     */
    default List<T> getList(String[] propertyName, Object[] value, String plugHql, List<Object> param, String order){
        return getBaseDao().getList(propertyName,value,plugHql,param,order);
    }

    /***
     * 通过某字段查询其他字段
     * @author 杨勇 2016年1月28日
     * @param target 要返回的字段
     * @param propertyName 要查询的字段
     * @param value 查询字段的值
     * @param plugSql TODO
     * @param param TODO
     * @param order TODO
     * @return
     */
    default <E> List<E> getList(String target, String propertyName, Object value, String plugSql, List<Object> param, String order){
        return getBaseDao().getList(target,propertyName,value,plugSql,param,order);
    }

    /***
     * 通过某字段查询其他字段
     * @author 杨勇 2016年1月28日
     * @param target 要返回的字段
     * @param propertyName 要查询的字段
     * @param value 查询字段的值
     * @param plugSql TODO
     * @param param TODO
     * @param order TODO
     * @return
     */
    default <E> List<E> getList(String target, String[] propertyName, Object[] value, String plugSql, List<Object> param, String order){
        return getBaseDao().getList(target,propertyName,value,plugSql,param,order);
    }

    /***
     * 查找是否有重复对象
     * @author 杨勇 2016年5月16日
     * @param pro
     * @param val
     * @param id
     * @param plugSql TODO
     * @return
     */
    default boolean findSame(String pro, Object val, PK id, String plugSql){
        return getBaseDao().findSame(pro,val,id,plugSql);
    }

    default Page<T> findPageWithHQLnoBlack(Pageable pageable, String hql, Object[] params){
        return getBaseDao().findPageWithHQLnoBlack(pageable,hql,params);
    }


    default Page<T> findPageWithSqlnoBlack(Pageable pageable, String sql, Object[] params){
        return getBaseDao().findPageWithSqlnoBlack(pageable,sql,params);
    }


    default Page<T> findPageWithHQLLikenoBlack(Pageable pageable, String hql, Object[] params){
        return getBaseDao().findPageWithHQLLikenoBlack(pageable,hql,params);
    }


    default Page<T> findPageWithSqlLikenoBlack(Pageable pageable, String sql, Object[] params){
        return getBaseDao().findPageWithSqlLikenoBlack(pageable,sql,params);
    }

    default List<T> findListWithHQLnoBlack(String hql, Object[] params){
        return getBaseDao().findListWithHQLnoBlack(hql,params);
    }

    default List<T> findListWithSQLnoBlack(String sql, Object[] params){
        return getBaseDao().findListWithSQLnoBlack(sql,params);
    }


    default int executeHqlnoBlack(String hql, Object[] params){
        return getBaseDao().executeHqlnoBlack(hql,params);
    }


    default int executeSqlnoBlack(String sql, Object[] params){
        return getBaseDao().executeSqlnoBlack(sql,params);
    }

    default Integer queryCountSqlnoBlack(String sql, Object[] params){
        return getBaseDao().queryCountSqlnoBlack(sql,params);
    }

    default Integer queryCountHqlnoBlack(String hql, Object[] params){
        return getBaseDao().queryCountHqlnoBlack(hql,params);
    }

    /**
     * 执行本地查询sql 带分页
     * @param sql
     * @param params
     * @return
     */
    default Page<T> executeQuerySqlByPage(Pageable pageable, String sql, Object... params){
        return getBaseDao().executeQuerySqlByPage(pageable,sql,params);
    }

    /**
     * 通过SQL查询某类型对象集合
     *
     * @param sql  SQL语句
     * @param value 参数数组
     * @return 结果集
     */
    default Page<Map> queryPageSqlBean(Pageable pageable, String sql, Object[] value){
        return getBaseDao().queryPageSqlBean(pageable,sql,value);
    }

    /**
     * 通过SQL查询某类型对象集合
     *
     * @param sql  SQL语句
     * @param eClass  目标对象类型
     * @param value 参数数组
     * @return 结果集
     */
    default <E> Page<E> queryPageSqlBean(Pageable pageable, String sql, Class<E> eClass, Object[] value) {
        return getBaseDao().queryPageSqlBean(pageable,sql,eClass,value);
    }

    /**
     * 通过SQL查询某类型对象集合
     *
     * @param sql  SQL语句
     * @param value 参数数组
     * @param maxRow
     * @return 结果集
     */
    default List<Object[]> querySqlArr(String sql, Object[] value, int maxRow){
        return getBaseDao().querySqlArr(sql,value, maxRow);
    }


    /**
     * 通过SQL查询某类型对象集合
     *
     * @param sql  SQL语句
     * @param value 参数数组
     * @return 结果集
     */
    default List<Object[]> querySqlArr(String sql, Object[] value){
        return getBaseDao().querySqlArr(sql,value);
    }


    /**
     * 通过SQL查询某类型对象集合
     *
     * @param sql  SQL语句
     * @param eClass  目标对象类型
     * @param value 参数数组
     * @return 结果集
     */
    default <E> List<E> querySqlBean(String sql, Class<E> eClass, Object[] value,int maxRow){
        return getBaseDao().querySqlBean(sql,eClass,value,maxRow);
    }

    /**
     * 通过SQL查询某类型对象集合
     *
     * @param sql  SQL语句
     * @param eClass  目标对象类型
     * @param value 参数数组
     * @return 结果集
     */
    default <E> List<E> querySqlBean(String sql, Class<E> eClass, Object[] value){
        return getBaseDao().querySqlBean(sql,eClass,value);
    }

    /**
     * 通过SQL查询,返回Map对象
     * YBB 181026
     *
     * @param sql  SQL语句
     * @param objs 参数数组
     * @return 结果集
     */
    default List querySqlMap(String sql, Object[] objs) {
        return getBaseDao().querySqlMap(sql,objs);
    }

    /**
     * 通过SQL查询,返回Map对象
     * YBB 181026
     *
     * @param sql  SQL语句
     * @param objs 参数数组
     * @return 结果集
     */
    default List querySqlMap(String sql, Object[] objs,int maxRow) {
        return getBaseDao().querySqlMap(sql,objs,maxRow);
    }

    default Object queryOneSql(String sql,Object ... param){
        return getBaseDao().queryOneSql(sql, param);
    }

    /**
     * 通过SQL查询返回对象集合并分页
     * @param pageable
     * @param sql
     * @param params
     * @return
     */
    default Page<T> querySqlByPage(Pageable pageable, String sql, Object... params){
        return getBaseDao().querySqlByPage(pageable,sql,params);
    }
}
