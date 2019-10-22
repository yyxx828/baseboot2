package com.xajiusuo.jpa.param.e;

import com.xajiusuo.jpa.param.entity.ResultBean;
import com.xajiusuo.jpa.param.service.ResultService;
import com.xajiusuo.jpa.util.DynamicEnumUtil;
import com.xajiusuo.jpa.util.ResponseBean;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by hadoop on 19-6-6.
 */
public enum Result {

    //成功请求
     OPERATE_SUCCESS("操作成功")
    ,QUERY_SUCCESS("查询成功")
    ,UPLOAD_SUCCESS("上传成功")
    ,SAVE_SUCCESS("保存成功")
    ,UPDATE_SUCCESS("修改成功")
    ,SUBMIT_SUCCESS("提交成功")
    ,DELETE_SUCCESS("删除成功")
    ,VALIDATE_SUCCESS("校验成功")

    //失败请求
    ,OPERATE_FAIL("操作失败")
    ,UPLOAD_FAIL("上传失败")
    ,SAVE_FAIL("保存失败")
    ,QUERY_FAIL("查询失败")
    ,UPDATE_FAIL("修改失败")
    ,SUBMIT_FAIL("提交失败")
    ,DELETE_FAIL("删除失败")
    ,VALIDATE_FAIL("校验失败")
    ,NOPOWER_FAIL("无权进行当前操作")
    ,NOTRESULT_FAIL("该请求描述不存在")
    ,NOTCONFIGS_FAIL("配置参数不存在")
    ,NOTNULL_FAIL("不能为空")
    ,NOTUPDATE_FAIL("不可修改")
    ,REQUEST_FAIL("请求异常")

    //警告请求
    ,NOTUPDATE_WARN("默认值,不可修改")
    ,NOTDELETE_WARN("不可删除")
    ,NOTMODIFY_WARN("修改值无变化")

    ,VALIDY_WARN("有效期到期,无法使用,请联系管理员")

    //服务错误请求
    ;

    Result(String msg){
        String name = this.name();
        top:while(true){
            for(CODE c:CODE.values()){
                if(name.toUpperCase().endsWith("_" + c.name())){
                    this.code = c;
                    this.msg = msg;
                    break top;
                }
            }
            throw new RuntimeException(MessageFormat.format("请求描述标识[name={0}]结束必须以_拼接{1},例如:[name={0}_{2}]",name, Arrays.toString(CODE.values()),CODE.values()[new Random().nextInt(CODE.values().length)].name()));
        }
    }

    /***
     * 查找请求描述,默认为 OPERATE_SUCCESS
     * @param name
     * @return
     */
    public static Result find(String name){
        try{
            if(name.contains(":")){
                name = name.substring(0,name.indexOf(":"));
            }
            return Result.valueOf(name.replace(".", "_").toUpperCase());
        }catch (Exception e){
            throw e;
        }
    }

    /***
     * 查找请求描述,默认为 OPERATE_SUCCESS
     * @param name
     * @return
     */
    public static Result find(String[] name){
        return find(name[0]);
    }

    /***
     * 查找name对应的返回类型,如果没查询到,则按照指定类型进行添加
     * @param nameAndMsg 传入nameAndMsg值必须为[XXX_CODE:请求描述]
     * @return
     */
    public static Result findAdd(String nameAndMsg){
        String[] ss = nameAndMsg.split(":");
        if(ss.length != 2){
            throw new RuntimeException("传入nameAndMsg值必须为[XXX_CODE:请求描述]");
        }
        return findAdd(ss[0],ss[1]);
    }

    /***
     * 查找name对应的返回类型,如果没查询到,则按照指定类型进行添加
     * @param name
     * @param msg
     * @return
     */
    public static Result findAdd(String name, String msg){
        try{
            return find(name);
        }catch (Exception e){
            name = name.replace(".", "_").toUpperCase();
            return dynamicAdd(name, msg);
        }
    }

    /***
     * 动态增加/修改枚举
     * @param name 枚举定义名称 eg:OPERATE_SUCCESS or OPERATE.SUCCESS
     * @param msg 描述
     * @return
     */
    public static Result dynamicAdd(String name, String msg){
        top:while(true){
            String name1 = name.replace(".","_").toUpperCase();
            for(CODE c:CODE.values()){
                if(name1.toUpperCase().endsWith("_" + c.name())){
                    break top;
                }
            }
            throw new RuntimeException(MessageFormat.format("请求描述标识[name={0}]结束必须以_拼接{1},例如:[name={0}_{2}]",name, Arrays.toString(CODE.values()),CODE.values()[new Random().nextInt(CODE.values().length)].name()));
        }
        try{
            Result r = find(name);
            if(r.msg.equals(msg)){
                return r;
            }
            r.msg = msg;
            if(resultService != null){
                resultService.saveOrUpdate(new ResultBean(r));
            }
            return r;
        }catch (Exception e){
        }


        DynamicEnumUtil.addEnum(Result.class,name,new Class<?>[]{String.class},new Object[]{msg});
        Result r = Result.valueOf(name);
        if(resultService != null){
            resultService.saveOrUpdate(new ResultBean(r));
        }
        return r;
    }


    private static ResultService resultService;
    private final static ThreadLocal<Object> data = new ThreadLocal<>();
    private final static ThreadLocal<Object> o = new ThreadLocal<>();

    private final static ThreadLocal<Result> results = new ThreadLocal<>();

    static Thread willGo;
    private CODE code;
    private String msg;

    public CODE getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public <T> T getData() {
        T t= (T) data.get();
        if(t == null){
            try{
                t = (T) Collections.emptyList();
            }catch (Exception e){}
        }
        return t;
    }

    public static Result responseBean(){
        return results.get();
    }

    public void clear(){
        results.remove();
        data.remove();
        o.remove();
    }

    public static void setResultService(ResultService resultService) {
        Result.resultService = resultService;
    }

    public Result setData(Object data) {
        Result.data.set(data);
        return this;
    }

    public <T> T getO() {
        T t= (T) o.get();
        return t;
    }

    public Result setO(Object o) {
        Result.o.set(o);
        return this;
    }

    public ResponseBean toBean(){
        results.set(this);
        return new ResponseBean(this);
    }

    public ResponseBean toBean(Object data){
        return setData(data).toBean();
    }

    public static void run() {
        if(willGo != null){
            willGo.start();
        }
    }

    public static void setWillGo(Thread willGo) {
        Result.willGo = willGo;
    }

    public enum CODE{
        SUCCESS(200),
        FAIL(500),
        WARN(300);

        CODE(int code){
            this.code = code;
        }
        private int code;

        public int getCode() {
            return code;
        }
    }

}
