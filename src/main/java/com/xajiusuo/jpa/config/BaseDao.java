package com.xajiusuo.jpa.config;

import com.xajiusuo.jpa.criteria.Criteria;
import com.xajiusuo.jpa.param.e.Result;
import com.xajiusuo.jpa.util.SqlUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.QueryHint;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by sht on 2018/1/15.
 */
@NoRepositoryBean
public interface BaseDao<T,ID extends Serializable> extends JpaRepository<T,ID> {

    void queryRangeExist();

    void queryRangeDelete();

    void queryRangeAll();

    Boolean getRange();

    <S extends T> S saveOrUpdate(S entity);

    <S extends T> Result saveUpdate(S entity);

    <S extends T> S update(S entity);

    /**
     * 根据属性名、修改前后属性值判断在数据库中是否唯一(若新修改的值与原来值相等则直接返回true).
     *
     * @param propertyName
     *            属性名称
     * @param oldValue
     *            修改前的属性值
     * @param oldValue
     *            修改后的属性值
     * @return boolean
     */
    boolean isUnique(String propertyName, Object oldValue, Object newValue);

    /**
     * 根据属性名判断数据是否已存在.
     *
     * @param propertyName
     *            属性名称
     * @param value
     *            值
     * @return boolean
     */
    boolean isExist(String propertyName, Object value);



    /**
     * 根据ID数组删除实体对象.
     *
     * @param ids ID数组
     */
    void delete(ID[] ids);


    void recover(ID id);

    void recover(T e);

    void recover(ID[] id);

    /**
     * 简单的删除方法
     * @param propertyName
     * @param value
     */
    void deleteBy(String propertyName, Object value);

    void destroy(ID id);

    void destroy(T e);

    void destroy(ID[] id);

    /**
     * 简单的相等的查询方法
     *
     * @param propertyName
     * @param value
     * @return
     */
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<T> findBy(String propertyName, Object value);

    /**
     * 简单的相等的查询方法 带排序
     *
     * @param propertyName
     * @param value
     * @return
     */
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<T> findBy(String propertyName, Object value, Sort sort);

    /**
     * 简单的相等的查询方法,带分页
     *
     * @param propertyName
     * @param value
     * @return
     */
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    Page<T> findBy(String propertyName, Object value, Pageable pageable);

    /**
     * 根据传入的hql和修改参数 ，执行HQL修改操作
     * @param hql
     */
    @Transactional
    void updateByHql(String hql);

    /**
     * 根据传入的hql和修改参数 ，执行HQL修改操作
     * @param hql
     * @param args
     */
    @Transactional
    void updateByHql(String hql, Object... args);


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

    /**
     * 根据条件查找(hibernate模式)
     *
     * @param criteria
     * @return
     */
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    T findUniqueBy(Criteria<T> criteria);

    /**
     * 根据条件查找(hibernate模式)
     * 求出总记录数
     * @param
     * @return
     */
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    long count(String sql, Object... params);

    /**
     * 根据条件查找(hibernate模式)
     * 求出总记录数
     * @param
     * @return
     */
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    long countHQL(String hql, Object... params);

    /**
     * 本地sql查询获取到List<Object[]>
     * @param sql
     * @param params
     * @return
     */
    List<Object[]> listBySQL(String sql,Object... params);
    /**
     * 执行本地查询sql
     * @param sql
     * @param params
     * @return
     */
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true") })
    List<T> executeNativeQuerySql(String sql, Object... params);
    /**
     * 执行本地查询sql 带分页
     * @param sql
     * @param params
     * @return
     */
    Page<T> executeQuerySqlByPage(Pageable pageable, String sql, Object... params) ;


    /**
     * 通过SQL查询某类型对象集合
     *
     * @param sql  SQL语句
     * @param value 参数数组
     * @return 结果集
     * @throws Exception
     */
    Page<Map> queryPageSqlBean(Pageable pageable, String sql, Object[] value);

    /**
     * 通过SQL查询某类型对象集合
     *
     * @param sql  SQL语句
     * @param eClass  目标对象类型
     * @param value 参数数组
     * @return 结果集
     * @throws Exception
     */
    <E> Page<E> queryPageSqlBean(Pageable pageable, String sql, Class<E> eClass, Object[] value);


    /**
     * 通过SQL查询某类型对象集合
     *
     * @param sql  SQL语句
     * @param value 参数数组
     * @param maxRow
     * @return 结果集
     * @throws Exception
     */
    List<Object[]> querySqlArr(String sql, Object[] value, int maxRow);

    /**
     * 通过SQL查询某类型对象集合
     *
     * @param sql  SQL语句
     * @param value 参数数组
     * @return 结果集
     * @throws Exception
     */
    default List<Object[]> querySqlArr(String sql, Object[] value){
        return querySqlArr(sql,value,0);
    }


    /**
     * 通过SQL查询某类型对象集合
     *
     * @param sql  SQL语句
     * @param eClass  目标对象类型
     * @param value 参数数组
     * @return 结果集
     * @throws Exception
     */
    <E> List<E> querySqlBean(String sql, Class<E> eClass, Object[] value,int maxRow);

    /**
     * 通过SQL查询某类型对象集合
     *
     * @param sql  SQL语句
     * @param eClass  目标对象类型
     * @param value 参数数组
     * @return 结果集
     * @throws Exception
     */
    default <E> List<E> querySqlBean(String sql, Class<E> eClass, Object[] value){
        return querySqlBean(sql,eClass,value,0);
    }

    /**
     * 通过SQL查询,返回Map对象
     * YBB 181026
     *
     * @param sql  SQL语句
     * @param objs 参数数组
     * @param rowMax
     * @return 结果集
     * @throws Exception
     */
    List querySqlMap(String sql, Object[] objs, int rowMax) ;


    /**
     * 通过SQL查询,返回Map对象
     * YBB 181026
     *
     * @param sql  SQL语句
     * @param objs 参数数组
     * @param rowMax
     * @return 结果集
     * @throws Exception
     */
    default List querySqlMap(String sql, Object[] objs) {
        return querySqlMap(sql,objs,0);
    }


    /**
     * 通过SQL查询返回对象集合并分页
     * @param pageable
     * @param sql
     * @param params
     * @return
     */
    Page<T> querySqlByPage(Pageable pageable, String sql, Object... params) ;


    void batchSave(List<T> data);

    void batchUpdate(List<T> data);

    /***
     * 批量新增和修改
     * @param data
     */
    void batchSaveOrUpdate(List<T> data);


    ////////////////////////////////////////////////// 19-6-20  新增功能接口//////////////////////////////////////////////

    EntityManager getEm();

    Class<T> getEc();

    default String tableName(){
        return SqlUtils.tableName(getEc());
    }

    default String className(){
        return getEc().getSimpleName();
    }

    String getIdName(boolean type);

    ////////////////////////////////////////////////// 19-6-20  新增业务接口//////////////////////////////////////////////

    default T get(ID id){
        try{
            return findById(id).get();
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 根据属性名和属性值获取唯一实体,如果无或者多个会抛出异常
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 实体对象
     */
    T getOne(String propertyName, Object value);


    /**
     * 根据属性名和属性值获取唯一实体,如果无或者多个会抛出异常
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 实体对象
     */
    <S> S getOne(String propertyName, Object value,Class<S> clazz);


    /**
     * 根据属性名和属性值获取唯一实体,如果无或者多个会抛出异常
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 实体对象
     */
    T getOne(String[] propertyName, Object[] value);


    /**
     * 根据属性名和属性值获取唯一实体,如果无或者多个会抛出异常
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 实体对象
     */
    <S> S getOne(String[] propertyName, Object[] value,Class<S> clazz);


    /**
     * 根据属性名和属性值获第一个实体,如果无返回null
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 实体对象
     */
    T getFirst(String propertyName, Object value);


    /**
     * 根据属性名和属性值获第一个实体,如果无返回null
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 实体对象
     */
    <S> S getFirst(String propertyName, Object value,Class<S> clazz);


    /**
     * 根据属性名和属性值获第一个实体,如果无返回null
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 实体对象
     */
    T getFirst(String[] propertyName, Object[] value);


    /**
     * 根据属性名和属性值获第一个实体,如果无返回null
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @return 实体对象
     */
    <S> S getFirst(String[] propertyName, Object[] value,Class<S> clazz);

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
    List<T> getList(String propertyName, Object value, String plugHql, List<Object> param, String order);

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
    List<T> getList(String[] propertyName, Object[] value, String plugHql, List<Object> param, String order);

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
    <E> List<E> getList(String target, String propertyName, Object value, String plugSql, List<Object> param, String order);

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
    <E> List<E> getList(String target, String[] propertyName, Object[] value, String plugSql, List<Object> param, String order);


    int getSizeByHQL(String propertyName, Object value, String plugSql, List<Object> params);

    /***
     * 查询匹配属性条数
     * @param propertyNames
     * @param values
     * @param plugSql
     * @param param
     * @return
     */
    int getSizeByHQL(String[] propertyNames, Object[] values, String plugSql, List<Object> param);

    /**
     * 根据属性名和属性值获取实体对象的条数.
     *
     * @param propertyName 属性名称
     * @param value 属性值
     * @param plugHql TODO
     * @param param TODO
     * @return 实体对象
     */
    int getSizeLikeByHQL(String propertyName, String value, String plugHql, List<Object> param);

    /***
     * 查询包含属性条数
     * @param propertyNames
     * @param values
     * @param plugSql
     * @param param
     * @return
     */
    int getSizeLikeByHQL(String[] propertyNames, String[] values, String plugSql, List<Object> param);


    /***
     * 查找是否有重复对象
     * @author 杨勇 2016年5月16日
     * @param pro
     * @param val
     * @param id
     * @param plugSql TODO
     * @return
     */
    boolean findSame(String pro, Object val, ID id, String plugSql);

    // hql
    Page<T> findPageWithHQL(Pageable pageable, String hql, Object... params);

    Page<T> findPageWithHQLAndCountHQL(Pageable pageable, String hql, String countHql, Object... params);

    Page<T> findPageWithHQLnoBlack(Pageable pageable, String hql, Object[] params);

    Page<T> findPageWithHQLAndCountHQLnoBlack(Pageable pageable, String hql, String countHql, Object[] params);

    // sql
    Page<T> findPageWithSql(Pageable pageable, String sql, Object... params);

    Page<T> findPageWithSqlAndCountSql(Pageable pageable, String sql, String countSql, Object... params);

    Page<T> findPageWithSqlnoBlack(Pageable pageable, String sql, Object[] params);

    Page<T> findPageWithSqlAndCountnoBlack(Pageable pageable, String sql, String countSql, Object[] params);


    // like
    Page<T> findPageWithHQLLike(Pageable pageable, String hql, Object... params);

    Page<T> findPageWithHQLLikenoBlack(Pageable pageable, String hql, Object[] params);

    Page<T> findPageWithSqlLike(Pageable pageable, String sql, Object... params);

    Page<T> findPageWithSqlLikenoBlack(Pageable pageable, String sql, Object[] params);

    // list
    List<T> findListWithHQL(String hql, Object... params);

    List<T> findListWithHQLnoBlack(String hql, Object[] params);

    List<T> findListWithSQL(String sql, Object... params);

    List<T> findListWithSQLnoBlack(String sql, Object[] params);

    // excute
    int executeHql(String hql, Object... params);

    int executeHqlnoBlack(String hql, Object[] params);

    int executeSql(String sql, Object... params);

    int executeSqlnoBlack(String sql, Object[] params);

    Integer queryCountSql(String sql, Object... params);

    Object queryOneSql(String sql, Object... params);

    Integer queryCountSqlnoBlack(String sql, Object[] params);

    Integer queryCountHql(String hql, Object... params);

    Integer queryCountHqlnoBlack(String hql, Object[] params);
}
