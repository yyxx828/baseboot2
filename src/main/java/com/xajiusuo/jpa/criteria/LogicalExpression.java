package com.xajiusuo.jpa.criteria;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;


public class LogicalExpression implements Criterion {

    private Criterion[] criterion;  // 逻辑表达式中包含的表达式
    private Operator operator;      //计算符

    public LogicalExpression(Criterion[] criterions, Operator operator) {
        this.criterion = criterions;
        this.operator = operator;
    }

    @Override
    public Predicate toPredicate(Root<?> root, CriteriaQuery<?> query,
                                 CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        for (Criterion aCriterion : this.criterion) {
            predicates.add(aCriterion.toPredicate(root, query, builder));
        }
        switch (operator) {
            case OR:
                return builder.or(predicates.toArray(new Predicate[predicates.size()]));
            default:
                return builder.and(predicates.toArray(new Predicate[predicates.size()]));
        }
    }
}
