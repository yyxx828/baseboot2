package com.xajiusuo.jpa.config;

import com.google.common.collect.Sets;
import com.xajiusuo.jpa.config.e.BeanDelete;
import com.xajiusuo.jpa.config.e.BeanInit;
import com.xajiusuo.jpa.criteria.Criteria;
import com.xajiusuo.jpa.param.e.Result;
import com.xajiusuo.jpa.param.entity.UserContainer;
import com.xajiusuo.jpa.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by sht on 2018/1/15.
 */
@Slf4j
@NoRepositoryBean
@Transactional(readOnly = true)
public class BaseDaoImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseDao<T, ID> {

    private  EntityManager entityManager;

    private Class<T> entityClass;

    protected JpaEntityInformation<T, ?> entityInformation;

    private final static ThreadLocal<Boolean> willDelete = new ThreadLocal<Boolean>(){//删除控制
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    public static boolean willDelete(){
        return willDelete.get();
    }

    /***
     * 查询范围
     */
    private final static ThreadLocal<Boolean> queryRange = new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return false;
        }
    };

    private ThreadLocal<String> pre = new ThreadLocal<String>(){
        @Override
        protected String initialValue() {
            return "";
        }
    };

    public void queryRangeExist(){
        queryRange.set(false);
    }

    public void queryRangeDelete(){
        queryRange.set(true);
    }

    public void queryRangeAll(){
        queryRange.set(null);
    }

    public static void clear(){
        queryRange.remove();
    }

    public void queryPreMark(String pre){
        if(StringUtils.isNotBlank(pre)){
            pre = pre.trim();
            if(!pre.endsWith(".")){
                pre += ".";
            }
            this.pre.set(pre);
        }
    }

    public Boolean getRange(){
        if(!BeanDelete.class.isAssignableFrom(entityClass)){
            return null;
        }
        return queryRange.get();
    }

    @Override
    public <S extends T> S save(S entity) {
        entity = save0(entity);
        flush();
        return entity;
    }

    private <S extends T> S save0(S entity) {
        idNvl(entity);
        compleEntityProper(entity);
        if(entity instanceof BeanDelete){
            willDelete.set(true);
            ((BeanDelete) entity).setDelFlag(false);//新增
            willDelete.set(false);
        }
        getEm().persist(entity);
        return entity;
    }

    @Override
    public <S extends T> S saveOrUpdate(S entity) {
        entity = saveOrUpdate0(entity);
        flush();
        return entity;
    }

    private <S extends T> S saveOrUpdate0(S entity) {
        idNvl(entity);
        if(entityInformation.isNew(entity)){
            return save0(entity);
        }
        try{
            return update0(entity);
        }catch (Exception e){
            return save0(entity);
        }
    }

    @Override
    public <S extends T> Result saveUpdate(S entity){
        idNvl(entity);
        if(entityInformation.isNew(entity)){
            try{
                return Result.SAVE_SUCCESS.setData(save(entity));
            }catch (Exception e){
                e.printStackTrace();
                return Result.SAVE_FAIL.setData(e.getMessage());
            }
        }
        try{
            return Result.UPDATE_SUCCESS.setData(update(entity));
        }catch (Exception e){
            try{
                return Result.SAVE_SUCCESS.setData(save(entity));
            }catch (Exception e1){
                e.printStackTrace();
                return Result.UPDATE_FAIL.setData(e.getMessage());
            }
        }
    }

    private T findById0(ID id){
        Exception e2 = null;
        try{
            return findById(id).get();
        }catch (Exception e){
            for(int i = 0;i<5;i++){
                try{
                    return findById(id).get();
                }catch (Exception e1){e2 = e1;}
            }
        }
        throw new RuntimeException("查询ID:" + id + ",出错" + e2.getMessage());
    }

    @Override
    public <S extends T> S update(S entity) {
        entity = update0(entity);
        flush();
        return entity;
    }

    private <S extends T> S update0(S entity) {
        idNvl(entity);
        if(entityInformation.isNew(entity)){
            throw new RuntimeException("业务ID不存在,不能进行修改操作");
        }

        if(entity instanceof HibernateProxy){//如果是加载保存不进行任何处理
            getEm().merge(entity);
            return entity;
        }

        if(entity instanceof BeanDelete && ((BeanDelete) entity).getDelFlag() == null){
            willDelete.set(true);
            ((BeanDelete) entity).setDelFlag(false);//修改
            willDelete.set(false);
        }

        ID id = (ID) entityInformation.getId(entity);
        T old = findById0(id);
        entityManager.detach(old);
        //获取传入新值属性
        List<String> inParams = new ArrayList<String>(UserContainer.info().keySet());
        if(inParams.size() > 0){
            //去杂去除非对象参数
            inParams.removeAll(Sets.newHashSet("createUID","createName","lastModifyUID","id","init","delFlag"));
            if(entity instanceof BeanInit){
                inParams.removeAll(((BeanInit) entity).init());
            }
            for (int i = 0; i < inParams.size(); i++) {
                String k = inParams.get(i);
                if(k.contains(".")){
                    inParams.set(i,k.substring(0,k.indexOf(".")));
                }
            }
            //回填旧对象去传入值
            IMapp m1 = Mapp.createMapp(entity);
            IMapp m2 = Mapp.createMapp(old);
            for(String f:m2.getFields()){
                if(entity instanceof BaseIntEntity && ((BaseIntEntity) entity).init().contains(f)){
                    m1.setObject(f,m2.getObjectbyField(f));
                    continue;
                }else if(inParams.contains(f) || m1.getObjectbyField(f) != null) continue;
                m1.setObject(f,m2.getObjectbyField(f));
            }
            m2.unLoad();
            m1.unLoad();
        }
        compleEntityProper(entity);
        getEm().merge(entity);
        return entity;
    }

    //父类没有不带参数的构造方法，这里手动构造父类
    public BaseDaoImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
        this.entityClass = domainClass;
    }

    public void setEntityInformation(JpaEntityInformation<T, ?> entityInformation) {
        this.entityInformation = entityInformation;
    }

    public BaseDaoImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        Type type = getClass().getGenericSuperclass();
        if(type instanceof ParameterizedType){
            Type[] ps = ((ParameterizedType) type).getActualTypeArguments();
            this.entityClass = (Class<T>) ps[0];
        }
    }
    //通过EntityManager来完成查询
    @Override
    public List<Object[]> listBySQL(String sql,Object... params) {
        Query query = entityManager.createNativeQuery(sql);
        setParams(query, params);
        return query.getResultList();
    }


    public void deleteById(ID id) {
        Assert.notNull(id, "The given id must not be null!");
        delete(findById(id).get());
    }

    public void delete(T e) {
        if(e instanceof BeanDelete){
            if(!(e instanceof HibernateProxy)){
                e = findById((ID) entityInformation.getId(e)).get();
            }
            willDelete.set(true);
            ((BeanDelete) e).setDelFlag(true);//删除
            willDelete.set(false);
            getEm().persist(e);
            flush();
        }
    }

    public void recover(ID id){
        Assert.notNull(id, "The given id must not be null!");
        recover(findById(id).get());
    }

    public void recover(T e){
        if(e instanceof BeanDelete){
            willDelete.set(true);
            ((BeanDelete) e).setDelFlag(false);//恢复
            willDelete.set(false);
            getEm().persist(e);
            flush();
        }
    }

    @Override
    public void recover(ID[] ids) {
        Assert.notEmpty(ids, "ids must not be empty");
        for (int i = 0; i < ids.length; i++) {
            this.recover(ids[i]);
        }
    }

    @Override
    public void delete(ID[] ids) {
        Assert.notEmpty(ids, "ids must not be empty");
        for (int i = 0; i < ids.length; i++) {
            this.deleteById(ids[i]);
        }
    }

    public void destroy(ID id){
        Assert.notNull(id, "The given id must not be null!");
        destroy(findById(id).get());
    }


    @Override
    public void destroy(ID[] ids) {
        Assert.notEmpty(ids, "ids must not be empty");
        for (int i = 0; i < ids.length; i++) {
            this.destroy(ids[i]);
        }
    }

    public void destroy(T e){
        super.delete(e);
    }

    @Override
    public List<T> findBy(String propertyName, Object value) {
        Assert.notNull(value, "参数不能为空");
        Specification<T> spec = JpaUtil.specEq(propertyName, value);
        return this.findAll(spec);
    }

    @Override
    public List<T> findBy(String propertyName, Object value, Sort sort) {
        Assert.notNull(value, "参数不能为空");
        Specification<T> spec = JpaUtil.specEq(propertyName, value);
        return this.findAll(spec, sort);
    }

    @Override
    public boolean isUnique(String propertyName, Object oldValue, Object newValue) {
        return false;
    }

    @Override
    public boolean isExist(String propertyName, Object value) {
        Assert.notNull(value, "参数不能为空");
        return this.findBy(propertyName, value) != null
                && this.findBy(propertyName, value).size() > 0;
    }

    @Override
    public T findUniqueBy(Criteria<T> criteria) {
        Assert.notNull(criteria, "参数不能为空");
        long count = this.count(criteria);
        if (count == 1) {
            return this.findOne(criteria).get();
        } else if (count > 1) {
            return this.findAll(criteria).get(0);
        } else {
            return null;
        }
    }

    @Override
    public Page<T> findBy(String propertyName, Object value, Pageable pageable) {
        Assert.notNull(value, "参数不能为空");
        Specification<T> spec = JpaUtil.specEq(propertyName, value);

        if(pageable.getPageNumber() > 0){
            pageable = PageRequest.of(pageable.getPageNumber() - 1,pageable.getPageSize(),pageable.getSort());
        }
        return this.findAll(spec, pageable);
    }

    @Override
    public List<T> executeNativeQuerySql(String sql, Object... params) {
        Query query = entityManager.createNativeQuery(sql, entityClass);
        setParams(query,params);
        return query.getResultList();
    }

    public List<T> findAll() {
        return findListWithHQL("from " + className());
    }

    @Override
    public void updateByHql(String hql) {
        Query query = entityManager.createQuery(hql);
        query.executeUpdate();
    }

    @Override
    public void updateByHql(String hql, Object...params) {
        Query query = entityManager.createQuery(hql);
        setParams(query,params);
        query.executeUpdate();
    }

    @Override
    public int executeUpdateSql(String sql) {
        Query query = entityManager.createNativeQuery(sql);
        return query.executeUpdate();
    }

    @Override
    public int executeUpdateSql(String sql, Object... params) {
        Query query = entityManager.createNativeQuery(sql);
        setParams(query,params);
        return query.executeUpdate();
    }

    @Override
    public Page<T> executeQuerySqlByPage(Pageable pageable, String sql, Object... params) {
        Long count = this.count(sql, params);
        sql = addOrder(sql,pageable,null, false);
        Query query = entityManager.createNativeQuery(sql, entityClass);
        setParams(query,params);
        pageable = setPage(query,pageable,count);

        return pager(query.getResultList(),pageable,count);
    }

    @Override
    public Page<T> querySqlByPage(Pageable pageable, String sql, Object... params) {
        Long count = this.count(sql, params);
        sql = addOrder(sql,pageable,null,false);
        //执行原生SQL
        Query query = entityManager.createNativeQuery(sql);
        pageable = setPage(query,pageable,count);
        //指定返回对象类型
        List resultList= query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        resultList=objectToList(resultList,entityClass);
        return pager(resultList,pageable,count);
    }

    @Override
    public long count(String sql, Object... params) {
        if(sql.contains("group")){
            Query queryCount = getEm().createNativeQuery(sql);
            setParams(queryCount,params);
            return queryCount.getResultList().size();
        }
        sql = addDelFlag(sql);
        Query query = entityManager.createNativeQuery("select count(*) " + sql.substring(sql.toLowerCase().indexOf("from")));
        setParams(query,params);
        return ((Number)query.getSingleResult()).longValue();
    }

    @Override
    public long countHQL(String hql, Object... params) {
        if(hql.contains("group")){
            Query queryCount = getEm().createQuery(h2h(hql));
            setParams(queryCount,params);
            return queryCount.getResultList().size();
        }
        hql = addDelFlag(hql);
        Query query = entityManager.createQuery(h2h("select count(*) " + hql.substring(hql.toLowerCase().indexOf("from"))));
        setParams(query,params);
        return ((Number)query.getSingleResult()).longValue();
    }

    @Override
    @Transactional
    public void batchSave(List<T> data) {
        if(data == null || data.size() <= 0){
            return;
        }
        for(int i = 0; i < data.size(); i++) {
            try {
                save0(data.get(i));
            }catch(Exception e) {
                continue;
            }
            if(i % 50 == 0) { // 单次批量操作的数目为100
                flush();
            }
        }
        flush();
    }

    @Override
    @Transactional
    public void batchUpdate(List<T> data) {
        if(data == null || data.size() <= 0){
            return;
        }
        for(int i = 0; i < data.size(); i++) {
            try {
                update0(data.get(i));
            }catch(Exception e) {
                continue;
            }
            if(i % 50 == 0) { // 单次批量操作的数目为100
                flush();
            }
        }
        flush();
    }

    @Override
    @Transactional
    public void batchSaveOrUpdate(List<T> data) {
        if(data == null || data.size() <= 0){
            return;
        }
        for(int i = 0; i < data.size(); i++) {
            try {
               saveOrUpdate0(data.get(i));
            }catch(Exception e) {
                continue;
            }
            if(i % 50 == 0) { // 单次批量操作的数目为100
                flush();
            }
        }
        flush();
    }

    @Override
    @Transactional
    public void deleteBy(String propertyName, Object value) {
        Assert.hasText(propertyName, "propertyName must not be empty");
        Assert.notNull(value, "value is required");
        List<T> list = findBy(propertyName,value);
        list.forEach(e -> delete(e));
    }

    public Page<Map> queryPageSqlBean(Pageable pageable, String sql, Object[] value){
        StringBuilder sb = new StringBuilder();
        Object[] param = convertHSql(sql,sb,value);
        sql = sb.toString();
        Long count = count(sql,param);
        sql = addOrder(sb.toString(),pageable,null,null);
        Query query = entityManager.createNativeQuery(sql);
        pageable = setPage(query,pageable,count);
        setParams(query,param);
        List ll= query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return pager(ll,pageable,count);
    }

    public <E> Page<E> queryPageSqlBean(Pageable pageable, String sql, Class<E> eClass, Object[] value){
        StringBuilder sb = new StringBuilder();
        Object[] param = convertHSql(sql,sb,value);
        sql = sb.toString();
        Long count = count(sql,param);
        Query query = entityManager.createNativeQuery(addOrder0(sql,pageable,null,null));
        pageable = setPage(query,pageable,count);
        setParams(query,param);
        List ll= query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        List<E> list = objectToList(ll,eClass);
        return pager(list,pageable,count);
    }

    public List<Object[]> querySqlArr(String sql, Object[] value, int maxRow){
        StringBuilder sb = new StringBuilder();
        Object[] param = convertHSql(sql,sb,value);
        Query query = entityManager.createNativeQuery(sql);
        setParams(query,param);
        if(maxRow > 0) query.setMaxResults(maxRow);
        return query.getResultList();
    }

    public <E> List<E> querySqlBean(String sql, Class<E> eClass, Object[] value,int maxRow) {
        //执行原生SQL
        StringBuilder sb = new StringBuilder();
        Object[] param = convertHSql(sql,sb,value);
        Query query = entityManager.createNativeQuery(sql);
        setParams(query,param);
        if(maxRow > 0) query.setMaxResults(maxRow);
        //指定返回对象类型
        List ll= query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return objectToList(ll,eClass);
    }

    @Override
    public List querySqlMap(String sql, Object[] value, int rowMax) {
        //执行原生SQL
        Query query = entityManager.createNativeQuery(sql);
        setParams(query,value);
        //指定返回对象类型
        List ll= query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return ll;
    }


    ////////////////////////////////////////////////// 19-6-20  新增功能接口//////////////////////////////////////////////
    public EntityManager getEm(){
        return entityManager;
    }

    public Class<T> getEc() {
        return entityClass;
    }

    private String[] ids;

    /***
     *
     * @param type true hql,false sql;
     * @return
     */
    public String getIdName(boolean type){
        if(ids == null){
            ids = new String[]{"id","id"};
        }else{
            return type ? ids[0] : ids[1];
        }
        IMapp m = null;
        try {
            m = Mapp.createMapp(entityClass.newInstance());
            for(String f: m.getFields()){
                Id idname =  m.getField(f).getAnnotation(Id.class);
                if(idname != null){
                    ids[0] = f;
                    Column c = m.getField(f).getAnnotation(Column.class);
                    if(c != null && StringUtils.isNotBlank(c.name())){
                        ids[1] = c.name();
                    }
                }
            }
        } catch (Exception e) {
        }finally {
            m.unLoad();
        }
        return getIdName(type);
    }

    ////////////////////////////////////////////////// 19-6-20  新增业务接口//////////////////////////////////////////////

//    public T getOne(ID id){
//        super.getOne(id);
//        return null;
//    }

    public T getOne(String propertyName, Object value) {
        Assert.hasText(propertyName, "propertyName must not be empty");
        Assert.notNull(value, "value is required");
        String hql = "from " + className() + " e where " + propertyName + " = ?1";
        hql = addDelFlag(hql);
        return (T) getEm().createQuery(hql).setParameter(1, value).getSingleResult();
    }

    public <S> S getOne(String propertyName, Object value,Class<S> clazz) {
        Assert.hasText(propertyName, "propertyName must not be empty");
        Assert.notNull(value, "value is required");
        String hql = "from " + clazz.getSimpleName() + " e where " + propertyName + " = ?1";
        hql = addDelFlag(hql);
        return (S) getEm().createQuery(hql).setParameter(1, value).getSingleResult();
    }

    public T getOne(String[] propertyName, Object[] value) {
        StringBuilder sb = new StringBuilder("from " + className() + " e where 1=1");
        int index = 1;
        for(int i = 0 ;i<propertyName.length;i++){
            if(value == null || value[i] == null){
                sb.append(" and " + propertyName[i] + " is null");
            }else{
                sb.append(" and " + propertyName[i] + " = ?" + index++);
            }
        }
        String hql = addDelFlag(sb.toString());
        sb.delete(0, sb.length());
        Query q = getEm().createQuery(hql);
        index = 1;
        if(value != null){
            for(Object v:value){
                if(v != null){
                    q.setParameter(index++, v);
                }
            }
        }
        return (T) q.getSingleResult();
    }

    public <S> S getOne(String[] propertyName, Object[] value,Class<S> clazz) {
        StringBuilder sb = new StringBuilder("from " + clazz.getSimpleName() + " e where 1=1");
        int index = 1;
        for(int i = 0 ;i<propertyName.length;i++){
            if(value == null || value[i] == null){
                sb.append(" and " + propertyName[i] + " is null");
            }else{
                sb.append(" and " + propertyName[i] + " = ?" + index++);
            }
        }
        String hql = addDelFlag(sb.toString());
        sb.delete(0, sb.length());
        Query q = getEm().createQuery(hql);
        setParams(q,value);
        return (S) q.getSingleResult();
    }


    public T getFirst(String propertyName, Object value) {
        Assert.hasText(propertyName, "propertyName must not be empty");
        Assert.notNull(value, "value is required");
        String hql = "from " + className() + " e where " + propertyName + " = ?1";
        hql = addDelFlag(hql);
        return (T) CommonUtils.getFirst(getEm().createQuery(hql).setParameter(1, value).getResultList());
    }

    public <S> S getFirst(String propertyName, Object value,Class<S> clazz) {
        Assert.hasText(propertyName, "propertyName must not be empty");
        Assert.notNull(value, "value is required");
        String hql = "from " + clazz.getSimpleName() + " e where " + propertyName + " = ?1";
        hql = addDelFlag(hql);
        return (S) CommonUtils.getFirst(getEm().createQuery(hql).setParameter(1, value).getResultList());
    }

    public T getFirst(String[] propertyName, Object[] value) {
        StringBuilder sb = new StringBuilder("from " + className() + " e where 1=1");
        int index = 1;
        for(int i = 0 ;i<propertyName.length;i++){
            if(value == null || value[i] == null){
                sb.append(" and " + propertyName[i] + " is null");
            }else{
                sb.append(" and " + propertyName[i] + " = ?" + index++);
            }
        }
        String hql = addDelFlag(sb.toString());
        sb.delete(0, sb.length());
        Query q = getEm().createQuery(hql);
        index = 1;
        if(value != null){
            for(Object v:value){
                if(v != null){
                    q.setParameter(index++, v);
                }
            }
        }
        return (T) CommonUtils.getFirst(q.getResultList());
    }

    public <S> S getFirst(String[] propertyName, Object[] value,Class<S> clazz) {
        StringBuilder sb = new StringBuilder("from " + clazz.getSimpleName() + " e where 1=1");
        int index = 1;
        for(int i = 0 ;i<propertyName.length;i++){
            if(value == null || value[i] == null){
                sb.append(" and " + propertyName[i] + " is null");
            }else{
                sb.append(" and " + propertyName[i] + " = ?" + index++);
            }
        }
        String hql = addDelFlag(sb.toString());
        sb.delete(0, sb.length());
        Query q = getEm().createQuery(hql);
        setParams(q,value);
        return (S) CommonUtils.getFirst(q.getResultList());
    }

    public List<T> getList(String propertyName, Object value, String plugHql, List<Object> params, String order) {
        List<Object> param = new ArrayList<Object>();
        String orders = "";
        if(StringUtils.isNotBlank(order)) {
            orders = " order by " + order;
        }else{
            orders =  defaultOrder(true);
        }
        if(plugHql == null) {
            plugHql = "";
        }
        if(params != null) {
            param.addAll(params);
        }
        StringBuilder hql = new StringBuilder(MessageFormat.format( "from {0} e where 1=1 {1}",className(), plugHql));

        setOneParam(hql,propertyName,value,param);

        String hql1 = addDelFlag(hql.toString());
        Query q = entityManager.createQuery(h2h(hql1 + orders));
        setParams(q,param.toArray());
        return q.getResultList();
    }

    public List<T> getList(String[] propertyName, Object[] value, String plugHql, List<Object> params, String order) {
        List<Object> param = new ArrayList<Object>();
        if(plugHql == null) {
            plugHql = "";
        }
        String orders;
        if(StringUtils.isNotBlank(order)) {
            orders = " order by " + order;
        }else{
            orders =  defaultOrder(true);
        }
        if(params != null) {
            param.addAll(params);
        }
        StringBuilder hql = new StringBuilder( MessageFormat.format( "from {0} e where 1=1{1}",className(),plugHql));

        setManyParam(hql,propertyName,value,param);
        String hql1 = addDelFlag(hql.toString());
        Query q = getEm().createQuery(h2h(hql1 + orders));
        setParams(q,param.toArray());
        param.clear();
        return q.getResultList();
    }

    public <E> List<E> getList(String target, String propertyName, Object value, String plugHql, List<Object> params, String order) {
        List<Object> param = new ArrayList<Object>();

        String orders;
        if(StringUtils.isNotBlank(order)) {
            orders = " order by " + order;
        }else{
            orders =  defaultOrder(true);
        }

        if(plugHql == null) {
            plugHql = "";
        }
        if(params != null) {
            param.addAll(params);
        }
        StringBuilder hql = new StringBuilder(MessageFormat.format( "select {0} from {1} e where 1=1{2}" ,target,className(), plugHql));

        setOneParam(hql,propertyName,value,param);
        String hql1 = addDelFlag(hql.toString());
        Query q = getEm().createQuery(h2h(hql1 + orders));
        setParams(q,param.toArray());
        return q.getResultList();
    }


    @Override
    public <E> List<E> getList(String target, String[] propertyName, Object[] value, String plugHql,  List<Object> params, String order) {
        List<Object> param = new ArrayList<Object>();
        if(plugHql == null) {
            plugHql = "";
        }
        String orders;
        if(StringUtils.isNotBlank(order)) {
            orders = " order by " + order;
        }else{
            orders =  defaultOrder(true);
        }
        if(params != null) {
            param.addAll(params);
        }
        StringBuilder hql = new StringBuilder(MessageFormat.format( "select {0} from {1} e where 1=1{2}", target,className(), plugHql));

        setManyParam(hql,propertyName,value,param);
        String hql1 = addDelFlag(hql.toString());
        Query q = getEm().createQuery(h2h(hql1 + orders));
        setParams(q,param.toArray());
        param.clear();
        return q.getResultList();
    }

    public int getSizeByHQL(String propertyName, Object value, String plugSql, List<Object> params) {
        if(plugSql == null) {
            plugSql = "";
        }
        String hql = null;
        List<Object> param = new ArrayList<Object>();
        if(value == null || (value instanceof String && StringUtils.isBlank(value.toString()))) {
            hql = "select count(e) from " + className() + " e where " + propertyName + " is null" + plugSql;
        }else {
            hql = "select count(e) from " + className() + " e where " + propertyName + " = ?" + plugSql;
            param.add(value);
        }
        hql = addDelFlag(hql);
        Query q = getEm().createQuery(h2h(hql));
        if(params != null) {
            param.addAll(params);
        }
        setParams(q,param.toArray());
        param.clear();
        return ((Number)(q.getSingleResult())).intValue();
    }

    @Override
    public int getSizeByHQL(String[] propertyNames, Object[] values, String plugHQL, List<Object> params) {
        if(plugHQL == null) {
            plugHQL = "";
        }
        StringBuilder hql = new StringBuilder(MessageFormat.format("select count(e) from {0} e where 1=1{1}" ,className(), plugHQL));
        int index = 0;
        List<Object> param = new ArrayList<Object>(0);
        if(params != null) {
            param.addAll(params);
        }
        if(values != null) {
            for(Object value : values) {
                if(value == null || (value instanceof String && StringUtils.isBlank(value.toString()))) {
                    hql.append(" and " + propertyNames[index] + " is null");
                }else {
                    hql.append(" and " + propertyNames[index] + "= ?");
                    param.add(value);
                }
                index++;
            }
        }
        String hql0 = addDelFlag(hql.toString());
        Query q = getEm().createQuery(h2h(hql0));
        setParams(q,param.toArray());
        return ((Number)(q.getSingleResult())).intValue();
    }

    @Override
    public int getSizeLikeByHQL(String propertyName, String value, String plugHql, List<Object> params) {
        if(plugHql == null) {
            plugHql = "";
        }
        String hql = null;
        List<Object> param = new ArrayList<Object>();
        if(value == null || StringUtils.isBlank(value)) {
            hql = "select count(e) from " + className() + " e where " + propertyName + " is null" + plugHql;
        }else {
            hql = "select count(e) from " + className() + " e where " + propertyName + " like ?" + plugHql;
            param.add(SqlUtils.sqlLike(value));
        }
        hql = addDelFlag(hql);
        Query q = getEm().createQuery(hql);
        int index = 0;
        if(params != null) {
            param.addAll(params);
        }
        for(Object o : param) {
            q.setParameter(index++, o);
        }
        param.clear();
        return ((Number)(q.getSingleResult())).intValue();
    }

    @Override
    public int getSizeLikeByHQL(String[] propertyNames, String[] values, String plugHql, List<Object> params) {
        if(plugHql == null) {
            plugHql = "";
        }
        StringBuilder hql = new StringBuilder("select count(e) from " + className() + " e where 1=1" + plugHql);
        int index = 0;
        List<Object> param = new ArrayList<Object>(0);
        if(params != null) {
            param.addAll(params);
        }
        if(values != null) {
            for(String value : values) {
                if(value == null || StringUtils.isBlank(value)) {
                    hql.append(" and " + propertyNames[index] + " is null");
                }else {
                    hql.append(" and " + propertyNames[index] + " like ?");
                    param.add(SqlUtils.sqlLike(value));
                }
                index++;
            }
        }
        String hql0 = addDelFlag(hql.toString());
        Query q = getEm().createQuery(h2h(hql0));
        setParams(q,param.toArray());
        param.clear();
        return ((Number)(q.getSingleResult())).intValue();
    }

    @Override
    public boolean findSame(String pro, Object val, ID id, String plugHQL) {
        plugHQL = plugHQL == null ? "" : plugHQL;
        if(id instanceof Number && ((Number) id).intValue() == 0){
            id = null;
        }else if(id instanceof String && StringUtils.isBlank((String)id)){
            id = null;
        }
        if(id != null){
            String hql = MessageFormat.format( "select count(e) from {0} e where {1} = ? and {2} <> ?{3}" ,className(),pro,getIdName(true),  plugHQL);
            return queryCountHql(hql, val, id) > 0;
        }else{
            String hql = MessageFormat.format( "select count(e) from {0} e where {1} = ?{2}" ,className(),pro, plugHQL);
            return queryCountHql(hql, val) > 0;
        }
    }

    // query by hql
    public Page<T> findPageWithHQL(Pageable pageable, String hql, Object... params) {
        return findPageWithHQLAndCountHQL(pageable, hql, toHqlCount(hql), params);
    }

    public Page<T> findPageWithHQLAndCountHQL(Pageable pageable, String hql, String hqlCount, Object... params) {
        long count = countHQL(hqlCount,params);
        hql = addOrder(hql,pageable,null,true);
        Query query = getEm().createQuery(h2h(hql));
        pageable = setPage(query, pageable,count);
        setParams(query,params);
        return pager(query.getResultList(), pageable,count);
    }

    public Page<T> findPageWithHQLnoBlack(Pageable pageable, String hql, Object[] params) {
        return findPageWithHQLAndCountHQLnoBlack(pageable, hql, toHqlCount(hql), params);
    }

    public Page<T> findPageWithHQLAndCountHQLnoBlack(Pageable pageable, String hql, String hqlCount, Object[] params) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        convertHSql(hqlCount, sb2, params);
        Object[] params1 = convertHSql(hql, sb, params);
        return findPageWithHQLAndCountHQL(pageable, sb.toString(), sb2.toString(), params1);
    }

    // query sql
    public Page<T> findPageWithSql(Pageable pageable, String sql, Object... params) {
        return findPageWithSqlAndCountSql(pageable, sql ,toSqlCount(sql), params);
    }

    public Page<T> findPageWithSqlAndCountSql(Pageable pageable, String sql, String sqlCount, Object... params) {
        long count = count(sqlCount,params);
        sql = addOrder(sql,pageable,null,false);
        Query query = getEm().createNativeQuery(sql);
        pageable = setPage(query, pageable,count);
        setParams(query,params);
        List ll= query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        ll = objectToList(ll,entityClass);

        return pager(ll, pageable,count);
    }

    public Page<T> findPageWithSqlnoBlack(Pageable pageable, String sql, Object[] params) {
        return findPageWithSqlAndCountnoBlack(pageable, sql, toSqlCount(sql), params);
    }

    public Page<T> findPageWithSqlAndCountnoBlack(Pageable pageable, String sql, String sqlCount, Object[] params) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        convertHSql(sqlCount, sb2, params);
        Object[] params1 = convertHSql(sql, sb, params);
        return findPageWithSqlAndCountSql(pageable, sb.toString(), sb2.toString(), params1);
    }

    // like
    public Page<T> findPageWithHQLLike(Pageable pageable, String hql, Object... params) {
        List<Object> list = new ArrayList<Object>();
        for(Object o : params) {
            if(o != null && o instanceof String && !StringUtils.isEmpty(o.toString().trim())) {
                list.add(SqlUtils.sqlLike(o.toString().trim()));
            }else {
                list.add(o);
            }
        }
        return findPageWithHQL(pageable, hql, list.toArray());
    }

    public Page<T> findPageWithHQLLikenoBlack(Pageable pageable, String hql, Object[] params) {
        List<Object> list = new ArrayList<Object>();
        for(Object o : params) {
            if(o != null && o instanceof String && !StringUtils.isEmpty(o.toString().trim())) {
                list.add(SqlUtils.sqlLike(o.toString().trim()));
            }else {
                list.add(o);
            }
        }
        return findPageWithHQLnoBlack(pageable, hql, list.toArray());
    }

    public Page<T> findPageWithSqlLike(Pageable pageable, String sql, Object... params) {
        List<Object> list = new ArrayList<Object>();
        for(Object o : params) {
            if(o != null && o instanceof String && !StringUtils.isEmpty(o.toString().trim())) {
                list.add(SqlUtils.sqlLike(o.toString().trim()));
            }else {
                list.add(o);
            }
        }
        return findPageWithSql(pageable, sql, list.toArray());
    }

    public Page<T> findPageWithSqlLikenoBlack(Pageable pageable, String sql, Object[] params) {
        List<Object> list = new ArrayList<Object>();
        for(Object o : params) {
            if(o != null && o instanceof String && !StringUtils.isEmpty(o.toString().trim())) {
                list.add(SqlUtils.sqlLike(o.toString().trim()));
            }else {
                list.add(o);
            }
        }
        return findPageWithSqlnoBlack(pageable, sql, list.toArray());
    }

    // list
    public List<T> findListWithHQL(String hql, Object... params) {
        hql = addDelFlag(hql);
        return setParams(getEm().createQuery(h2h(hql)),params).getResultList();
    }

    public List<T> findListWithHQLnoBlack(String hql, Object[] params) {
        StringBuilder sb = new StringBuilder();
        Object[] params1 = convertHSql(hql, sb, params);
        return findListWithHQL(sb.toString(), params1);
    }

    public List<T> findListWithSQL(String sql, Object... params) {
        sql = addDelFlag(sql);
        Query query = getEm().createNativeQuery(sql);
        setParams(query,params);

        List ll= query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
        return objectToList(ll,entityClass);
    }

    public List<T> findListWithSQLnoBlack(String sql, Object[] params) {
        StringBuilder sb = new StringBuilder();
        Object[] params1 = convertHSql(sql, sb, params);
        return findListWithSQL(sb.toString(), params1);
    }

    public int executeHql(String hql, Object... params) {
        return setParams(getEm().createQuery(h2h(hql)),params).executeUpdate();
    }

    public int executeHqlnoBlack(String hql, Object[] params) {
        StringBuilder sb = new StringBuilder();
        Object[] params1 = convertHSql(hql, sb, params);
        return executeHql(sb.toString(), params1);
    }

    public int executeSql(String sql, Object... params) {
        return setParams(getEm().createNativeQuery(sql),params).executeUpdate();
    }

    public int executeSqlnoBlack(String sql, Object[] params) {
        StringBuilder sb = new StringBuilder();
        Object[] params1 = convertHSql(sql, sb, params);
        return executeSql(sb.toString(), params1);

    }

    public Integer queryCountSql(String sql, Object... params) {
        sql = addDelFlag(sql);
        Query queryCount = getEm().createNativeQuery(sql);
        return ((Number)setParams(queryCount,params).getSingleResult()).intValue();
    }

    @Override
    public Object queryOneSql(String sql, Object... params) {
        Query queryCount = getEm().createNativeQuery(sql);
        return setParams(queryCount,params).getSingleResult();
    }

    @Override
    public Integer queryCountSqlnoBlack(String sql, Object[] params) {
        StringBuilder sb = new StringBuilder();
        Object[] params1 = convertHSql(sql, sb, params);
        return queryCountSql(sb.toString(), params1);
    }

    @Override
    public Integer queryCountHql(String hql, Object... params) {
        hql = addDelFlag(hql);
        Query queryCount = getEm().createQuery(h2h(hql));
        return ((Number)setParams(queryCount,params).getSingleResult()).intValue();
    }

    @Override
    public Integer queryCountHqlnoBlack(String hql, Object[] params) {
        StringBuilder sb = new StringBuilder();
        Object[] params1 = convertHSql(hql, sb, params);
        return queryCountHql(sb.toString(), params1);
    }


    @Override
    public void flush() {
        try{
            super.flush();
        }catch (Exception e){}
    }

    ////////////////////////////////////////////////// 19-6-20  私有方法/////////////////////////////////////////////////
    private void idNvl(T entity){
        Object id = entityInformation.getId(entity);
        if(id == null) return;
        if((id instanceof String && (StringUtils.isBlank((String)id))) || "undefined".equals(id)  || "null".equals(id) || (id instanceof Number && ((Number)id).intValue() == 0)){
            if(entity instanceof BaseEntity){
                ((BaseEntity) entity).setId(null);
            }else if(entity instanceof BaseIntEntity){
                ((BaseIntEntity) entity).setId(null);
            }else {
                try {
                    Field f = entityClass.getDeclaredField(getIdName(true));
                    f.setAccessible(true);
                    f.set(entity,null);
                } catch (Exception e) {
                }
            }
        }
    }

    //回填多对一属性
    private void compleEntityProper(Object entity){
        IMapp m1 = Mapp.createMapp(entity);
        for(String f:m1.getFields()){//遍历待保存的所有属性
            Field field1 = m1.getField(f);//获取该属性对象
            ManyToOne mto = field1.getAnnotation(ManyToOne.class);
            if(mto != null){//如果是多对一,进行从数据库读取
                JoinColumn jc = field1.getAnnotation(JoinColumn.class);
                Class clazz = field1.getType();
                Object p = m1.getObjectbyField(f);
                if(p == null) continue;
                try {
                    Object e0 = null;
                    if(jc != null && StringUtils.isNotBlank(jc.referencedColumnName())){
                        String get = jc.referencedColumnName();
                        get = "get" + get.toUpperCase().charAt(0) + get.substring(1);
                        Object val = p.getClass().getMethod(get).invoke(p);
                        if(val instanceof Number && ((Number)val).intValue() == 0){
                            val = null;
                        }else if(val instanceof String && StringUtils.isBlank((String)val)){
                            val = null;
                        }else if("undefined".equals(val) || "null".equals(val)){
                            val = null;
                        }
                        if(val != null){
                            e0 = getOne(jc.referencedColumnName(),val,clazz);
                        }
                    }else{
                        IMapp m = Mapp.createMapp(p);
                        Object val = null;
                        for(String id1:m.getFields()){
                            Id id = m.getField(id1).getAnnotation(Id.class);
                            if(id != null){
                                val = m.getObjectbyField(id1);
                                break;
                            }
                        }
                        m.unLoad();
                        if(val != null){
                            e0 = entityManager.getReference(clazz, val);
                        }
                    }
                    m1.setObject(f,e0);
                } catch (Exception e1) {
                    throw new RuntimeException( e1 );
                }
            }
        }
        m1.unLoad();
    }


    private <E> List<E> objectToList(List ll,Class<E> obj) {
        List list = new ArrayList();
        for(Object m:ll){
            Object o;
            try{
                o = obj.newInstance();
            }catch (Exception e){
                throw new RuntimeException("该类[" + obj +  "],没有无参构造器.无法使用sql查询");
            }
            IMapp mapp = Mapp.createMapp(o);
            mapp.setValueIgnore((Map<String, Object>) m);
            mapp.unLoad();
            list.add(o);
        }
        return list;
    }


    /***
     * 传入参数
     * @param query
     * @param params
     */
    private Query setParams(Query query, Object... params){
        if (params != null) {
            int i = 0;
            for (Object param : params) {
                if(param != null || (param instanceof String && StringUtils.isNotBlank((CharSequence) param))){
                    query.setParameter(++i, param);
                }
            }
        }
        return query;
    }

    /***
     * 通过总条数重新定义page偏移量
     * @param query
     * @param p
     * @param count
     */
    private Pageable setPage(Query query, Pageable p, Long count){
        int num = p.getPageNumber() > 0 ? p.getPageNumber() - 1 : 0;
        int size = p.getPageSize();
        if(size <= 0){
            size = 10;
        }
        if(size > 500) size = 500;
        query.setMaxResults(size);

        if(count != 0 && num * size >= count) {//最后一页号码重新计算
            Long num1 = num + (count - size - num * size) / size;
            num = num1.intValue();
        }
        query.setFirstResult(num * size);
        return PageRequest.of(num,size,p.getSort());
    }

    private String addOrder(String sql, Pageable pageable, Sort defOrder, Boolean type){
        sql = addDelFlag(sql);
        return addOrder0(sql,pageable,defOrder,type);
    }

    private String addOrder0(String sql, Pageable pageable, Sort defOrder, Boolean type){
        if(!sql.contains("order by")){
            StringBuilder sb = new StringBuilder(sql);
            if(pageable.getSort() != null && pageable.getSort().isSorted()){
                defOrder = pageable.getSort();
            }
            if(defOrder != null && defOrder.isSorted()){
                sb.append(" order by ");
                Iterator<Sort.Order> it = defOrder.iterator();
                while(it.hasNext()){
                    Sort.Order o = it.next();
                    sb.append(o.getProperty());
                    if(o.getDirection() != null){
                        sb.append(o.getDirection().isAscending() ? " ASC," : " DESC,");
                    }
                }
                sb.deleteCharAt(sb.length() - 1);
            } else{
                sb.append(defaultOrder(type));
            }
            sql = sb.toString();
        }
        return sql;
    }

    private String defaultOrder(Boolean type){
        if(type == null){
            return "";
        }
        if(TableEntity.class.isAssignableFrom(getEc())){
            return " order by " + pre.get() + "lastModifyTime DESC";
        }else{
            return " order by "  + pre.get() + getIdName(type) + " desc";
        }
    }


    private void setOneParam(StringBuilder hql, String propertyName,Object value,List<Object> param){
        if(StringUtils.isNotEmpty(propertyName)){
            if(value instanceof Date) {
                Date min = P.U.dateYMD((Date)value);
                Date max = new Date(min.getTime() + 24 * 60 * 60 * 1000);
                hql .append(" and " + propertyName + " >= ? and " + propertyName + " < ?");
                param.add(min);
                param.add(max);
            }else {
                if(value == null) {
                    hql.append(" and " + propertyName + " is null");
                }else {
                    hql.append(" and " + propertyName + " = ?");
                    param.add(value);
                }
            }
        }
    }

    private void setManyParam(StringBuilder hql,String[] propertyName,Object[] value,List<Object> param){
        int index = 0;
        for(Object v : value) {
            if(v instanceof Date) {
                Date min = P.U.dateYMD((Date)v);
                Date max = new Date(min.getTime() + 24 * 60 * 60 * 1000);
                hql .append( " and " + propertyName[index] + " >= ? and " + propertyName[index] + " < ?");
                param.add(min);
                param.add(max);
            }else {
                if(v == null || "".equals(v)) {
                    hql.append(" and " + propertyName[index] + " is null");
                }else {
                    hql.append(" and " + propertyName[index] + " = ?");
                    param.add(v);
                }
            }
            index++;
        }
    }

    private Object[] convertHSql(String hsql, StringBuilder sb, Object[] params) {
        return SqlUtils.convertHSql(hsql,sb,params);
    }

    private String toHqlCount(String hql) {
        String hql1 = SqlUtils.toHqlCount(hql);
        hql1 = addDelFlag(hql1);
        return SqlUtils.toHqlCount(hql);
    }

    /***
     * 增加删除范围
     * @param sql
     * @return
     */
    private String addDelFlag(String sql){
        Boolean range = getRange();
        if(range == null){
            return sql;
        }
        sql = sql.replace("="," = ").replace("< =","<=").replace("> =",">=");
        while(sql.contains("  ")){
            sql = sql.replace("  "," ");
        }
        String sql1 = sql.toLowerCase();
        if(sql1.contains(".delflag") ||sql1.contains(" delflag") || sql1.contains(" group ") || sql1.contains(" order ") || sql1.contains(" having ")){
            return sql;
        }

        String delSql = range ? (" where " + pre.get() + "delFlag = 1") : (" where (" + pre.get() + "delFlag = 0 or " + pre.get() + "delFlag is null)");

        String q= " where 1 = 1";
        int pos = sql1.indexOf(q);
        while(pos != -1){
            sql = sql.replace(sql.substring(pos,pos + q.length()),"{0}");
            sql1 = sql.toLowerCase();
            pos = sql1.indexOf(q);
        }
        q = " where";
        pos = sql1.indexOf(q);
        while(pos != -1){
            sql = sql.replace(sql.substring(pos,pos + q.length()),"{1}");
            sql1 = sql.toLowerCase();
            pos = sql1.indexOf(q);
        }

        sql = sql.replace("{0}",delSql).replace("{1}",delSql + " and");

        if(!sql.contains("where")){
            sql += delSql;
        }

        return sql;
    }

    private String h2h(String hql){
        if(hql.contains("?") && !hql.contains("?1")){
            StringBuilder s = new StringBuilder(hql + " ");
            int i = 1;
            do{
                int pos = s.indexOf("? ");
                int pos1 = s.indexOf("?)");
                if(pos1 != -1 && (pos1 < pos || pos == -1)){
                    s.replace(pos1,pos1 + 2,"?" + i + ")");
                }else if(pos != -1){
                    s.replace(pos,pos + 2,"?" + i + " ");
                }else{
                    break;
                }
                i++;
            }while(s.indexOf("? ") > 0 || s.indexOf("?)") > 0);
            hql = s.toString();
        }
        return hql;
    }

    private String toSqlCount(String sql) {
        if(StringUtils.isEmpty(sql))
            return "";
        sql = addDelFlag(sql);
        return "select count(*) from (" + sql + ")";
    }

    private <E> Page<E> pager(List resultList,Pageable pageable,long count){
        return new PageImpl<E>(resultList != null ? resultList : new ArrayList<E>(), pageable, count);
    }

}
