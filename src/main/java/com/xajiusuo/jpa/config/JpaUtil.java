package com.xajiusuo.jpa.config;


import com.xajiusuo.jpa.criteria.Criteria;
import com.xajiusuo.jpa.criteria.Criterion;
import com.xajiusuo.jpa.criteria.Restrictions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
public class JpaUtil {

    private static final ConversionService conversionService = new DefaultConversionService();


    private static final String SHORT_DATE = "yyyy-MM-dd";
    private static final String LONG_DATE = "yyyy-MM-dd mm:HH:ss";
    private static final String TIME = "mm:HH:ss";


    /**
     *
     * @param fieldName
     * @param value
     * @param <T>
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> Specification<T> specEq(final String fieldName, final Object value ) {

        return new Specification<T>() {
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
                                         CriteriaBuilder builder) {
                if (StringUtils.isNotBlank(fieldName) && value != null) {
                    Path exp = root;
                    exp = exp.get(fieldName);
                    return builder.equal(exp, value);
                }
                return builder.conjunction();
            }
        };
    }

    public static Criteria transCriteria(Map<String, Object> params) {
        Criteria criteria = new Criteria();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            criteria.add(makeRestrictions(key, params.get(key)));
        }
        return criteria;
    }

    /**
     * 如果key的值以:分割，则这个key的比较条件是指定的；否则这个条件默认.
     * 比较条件同sql中的比较条件。如果比较条件为is null 或 is not null时，值可以为空
     * @param key  propertyName:=
     * @param value 值
     * @return
     */
    private static Criterion makeRestrictions(String key, Object value){
        if(key!=null && key.length()>0 && value!=null){
            String[] keySplits = key.split(":");
            if(keySplits.length==1){
                return Restrictions.eq(key,value);
            }else if(keySplits.length>1){
                String oper = keySplits[1].trim().toLowerCase().replaceAll("\\s"," ");
                String key0 = keySplits[0].trim();
                if("=".equals(oper)){
                    return Restrictions.eq(key0,value);
                }else if("!=".equals(oper)){
                    return Restrictions.ne(key0,value);
                }else if(">=".equals(oper)){
                    return Restrictions.gte(key0,value);
                }else if(">".equals(oper)){
                    return Restrictions.gt(key0,value);
                }else if("<=".equals(oper)){
                    return Restrictions.lte(key0,value);
                }else if("<".equals(oper)){
                    return Restrictions.lt(key0,value);
                }else if("like".equals(oper)){
                    String valueString=value.toString();
                    if(valueString.startsWith("%")&&valueString.endsWith("%")){
                        return Restrictions.like(key0,valueString.replaceAll("%",""));
                    }else if(valueString.startsWith("%")){
                        return Restrictions.leftLike(key0,valueString.replaceAll("%",""));
                    }else if(valueString.endsWith("%")){
                        return Restrictions.rightLike(key0,valueString.replaceAll("%",""));
                    }else{
                        return Restrictions.like(key0,valueString);
                    }
                }else if("in".equals(oper)){
                    return Restrictions.in(key0,makeCollection(value));
                }else if("not in".equals(oper)){
                    return Restrictions.notIn(key0,makeCollection(value));
                }else if("is null".equals(oper)){
                    return Restrictions.isNull(key0);
                }else if("is not null".equals(oper)){
                    return Restrictions.isNotNull(key0);
                }
            }
        }
        return null;
    }

    private static Collection makeCollection(Object obj){
        if(obj.getClass().isArray()){
            return  Arrays.asList(obj);
        }else if(obj instanceof Collection){
            return (Collection) obj;
        }else{
            List list= new ArrayList(0);
            list.add(obj);
            return list;
        }
    }



    private static Date convert2Date(String dateString) {
        SimpleDateFormat sFormat = new SimpleDateFormat(SHORT_DATE);
        try {
            return sFormat.parse(dateString);
        } catch (ParseException e) {
            try {
                return sFormat.parse(LONG_DATE);
            } catch (ParseException e1) {
                try {
                    return sFormat.parse(TIME);
                } catch (ParseException e2) {
                    log.error("Convert time is error! The dateString is " + dateString);
                }
            }
        }

        return null;
    }


    private static <E extends Enum<E>> E convert2Enum(Class<E> enumClass, String enumString) {
        return EnumUtils.getEnum(enumClass, enumString);
    }
}
