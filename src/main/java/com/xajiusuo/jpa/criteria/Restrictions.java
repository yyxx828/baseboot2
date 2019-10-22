package com.xajiusuo.jpa.criteria;

import java.util.Collection;


public class Restrictions {

    /**
     * 等于
     * @param fieldName
     * @param value
     * @return
     */
    public static SimpleExpression eq(String fieldName, Object value) {
        if(value == null) {
            return null;
        }
        return new SimpleExpression (fieldName, value, Criterion.Operator.EQ);
    }

    /**
     * 不等于
     * @param fieldName
     * @param value
     * @return
     */
    public static SimpleExpression ne(String fieldName, Object value) {
        if(value == null) {
            return null;
        }
        return new SimpleExpression (fieldName, value, Criterion.Operator.NE);
    }

    /**
     * 模糊匹配
     * @param fieldName
     * @param value
     * @return
     */
    public static SimpleExpression like(String fieldName, String value) {
        if(value == null) {
            return null;
        }
        return new SimpleExpression (fieldName, value, Criterion.Operator.LIKE);
    }

    /**
     * 模糊匹配
     * @param fieldName
     * @param value
     * @return
     */
    public static SimpleExpression leftLike(String fieldName, String value) {
        if(value == null) {
            return null;
        }
        return new SimpleExpression (fieldName, value, Criterion.Operator.LEFTLIKE);
    }

    /**
     * 模糊匹配
     * @param fieldName
     * @param value
     * @return
     */
    public static SimpleExpression rightLike(String fieldName, String value) {
        if(value == null) {
            return null;
        }
        return new SimpleExpression (fieldName, value, Criterion.Operator.RIGHTLIKE);
    }



    /**
     * 大于
     * @param fieldName
     * @param value
     * @return
     */
    public static SimpleExpression gt(String fieldName, Object value) {
        if(value == null) {
            return null;
        }
        return new SimpleExpression (fieldName, value, Criterion.Operator.GT);
    }

    /**
     * 小于
     * @param fieldName
     * @param value
     * @return
     */
    public static SimpleExpression lt(String fieldName, Object value) {
        if(value == null) {
            return null;
        }
        return new SimpleExpression (fieldName, value, Criterion.Operator.LT);
    }

    /**
     * 大于等于
     * @param fieldName
     * @param value
     * @return
     */
    public static SimpleExpression gte(String fieldName, Object value) {
        if(value == null) {
            return null;
        }
        return new SimpleExpression (fieldName, value, Criterion.Operator.GTE);
    }

    /**
     * 小于等于
     * @param fieldName
     * @param value
     * @return
     */
    public static SimpleExpression lte(String fieldName, Object value) {
        if(value == null) {
            return null;
        }

        return new SimpleExpression (fieldName, value, Criterion.Operator.LTE);
    }

    /**
     * 并且
     * @param criterions
     * @return
     */
    public static LogicalExpression and(Criterion... criterions){
        return new LogicalExpression(criterions, Criterion.Operator.AND);
    }
    /**
     * 或者
     * @param criterions
     * @return
     */
    public static LogicalExpression or(Criterion... criterions){
        return new LogicalExpression(criterions, Criterion.Operator.OR);
    }
    /**
     * 包含于
     * @param fieldName
     * @param value
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static LogicalExpression in(String fieldName, Collection value) {
        if((value==null||value.isEmpty())){
            return null;
        }
        SimpleExpression[] ses = new SimpleExpression[value.size()];
        int i=0;
        for(Object obj : value){
            ses[i]=new SimpleExpression(fieldName,obj, Criterion.Operator.EQ);
            i++;
        }
        return new LogicalExpression(ses, Criterion.Operator.OR);
    }


    /**
     * 不包含于
     * @param fieldName
     * @param value
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static LogicalExpression notIn(String fieldName, Collection value) {
        if((value==null||value.isEmpty())){
            return null;
        }
        SimpleExpression[] ses = new SimpleExpression[value.size()];
        int i=0;
        for(Object obj : value){
            ses[i]=new SimpleExpression(fieldName,obj, Criterion.Operator.NE);
            i++;
        }
        return new LogicalExpression(ses, Criterion.Operator.AND);
    }

    /**
     * 列为空
     * @param fieldName
     * @return
     */
    public static LogicalExpression isNull(String fieldName) {
        SimpleExpression[] ses = new SimpleExpression[1];
        ses[0] = new SimpleExpression(fieldName, Criterion.Operator.ISNULL);
        return new LogicalExpression(ses, Criterion.Operator.ISNULL);
    }

    /**
     * 列不为空
     * @param fieldName
     * @return
     */
    public static LogicalExpression isNotNull(String fieldName) {
        SimpleExpression[] ses = new SimpleExpression[1];
        ses[0] = new SimpleExpression(fieldName, Criterion.Operator.ISNOTNULL);
        return new LogicalExpression(ses, Criterion.Operator.ISNOTNULL);
    }
}
