package com.xajiusuo.jpa.param.e;

import java.lang.annotation.*;

/**
 * Created by hadoop on 19-8-15.
 * 不进行数据库日志保存
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface NoLog{

}
