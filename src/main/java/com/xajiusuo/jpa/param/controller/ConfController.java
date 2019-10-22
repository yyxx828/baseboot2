package com.xajiusuo.jpa.param.controller;

import com.xajiusuo.jpa.config.BaseController;
import com.xajiusuo.jpa.config.PropertiesConfig;
import com.xajiusuo.jpa.param.e.Configs;
import com.xajiusuo.jpa.param.e.Result;
import com.xajiusuo.jpa.param.entity.ConfigBean;
import com.xajiusuo.jpa.param.entity.ResultBean;
import com.xajiusuo.jpa.param.service.ConfigService;
import com.xajiusuo.jpa.param.service.ResultService;
import com.xajiusuo.jpa.util.ResponseBean;
import com.xajiusuo.jpa.util.RsaKeyUtil;
import com.xajiusuo.jpa.util.SqlUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by 杨勇 on 18-8-21.
 */
@Api(description = "配置参数")
@RestController
@RequestMapping(value = "/api/param")
public class ConfController extends BaseController {

    /** 增加日志 */
    private final Logger log = LoggerFactory.getLogger(ConfController.class);

    @Autowired
    private ResultService resultService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private PropertiesConfig propertiesConfig;


    /***
     * @desc 请求描述列表
     * @author 杨勇 19-6-15
     * @return
     */
    @ApiOperation(value = "请求描述列表", httpMethod = "GET",response = ResultBean.class)
    @RequestMapping(value = "/listResult", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "请输入查询描述", paramType = "query", dataType = "string"),
    })
    @ResponseBody
    public ResponseBean listResult(String key) {
        List list = resultService.findListWithHQLnoBlack("from " + resultService.className() + " where 1=1~ and msg like ?~",new Object[]{SqlUtils.sqlLike(key)});
        return Result.QUERY_SUCCESS.toBean(list);
    }

    /***
     * @desc 请求描述修改
     * @author 杨勇 19-6-15
     * @return
     */
    @ApiOperation(value = "请求描述修改", httpMethod = "POST",response = ResultBean.class)
    @RequestMapping(value = "/updateResult", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "enumName", value = "请求标识", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "msg", value = "描述", paramType = "query", dataType = "string"),
    })
    @ResponseBody
    public ResponseBean updateResult(String enumName,String msg) {
        Result r = null;
        try{
            r = Result.find(enumName);
        }catch (Exception e){
            return Result.NOTRESULT_FAIL.toBean();
        }
        if(StringUtils.isBlank(msg)){
            return Result.NOTNULL_FAIL.toBean();
        }
        if(r.getMsg().equals(msg)){
            return Result.NOTMODIFY_WARN.toBean();
        }

        Result.dynamicAdd(enumName, msg);

        return Result.UPDATE_SUCCESS.toBean(new ResultBean(r));
    }

    /***
     * @desc 配置参数列表
     * @author 杨勇 19-6-19
     * @return
     */
    @ApiOperation(value = "配置参数列表", httpMethod = "GET",response = ResultBean.class)
    @RequestMapping(value = "/listConfigs", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "数据类型", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "key", value = "查询描述内容", paramType = "query", dataType = "string"),
    })
    @ResponseBody
    public ResponseBean listConfigs(String type, String key) {
        List list = resultService.findListWithHQLnoBlack("from " + configService.className() + " where 1=1~ and type = ?~~ and describe like ?~",new Object[]{type, SqlUtils.sqlLike(key)});
        return Result.QUERY_SUCCESS.toBean(list);
    }

    private static List<Map> list = null;

    /***
     * @desc 数据类型列表
     * @author 杨勇 19-7-4
     * @return
     */
    @ApiOperation(value = "数据类型列表", httpMethod = "GET",response = ResultBean.class)
    @RequestMapping(value = "/listTypes", method = RequestMethod.GET)
    @ResponseBody
    public ResponseBean listTypes() {
        if(list == null || list.size() == 0){
            list = new ArrayList(Configs.DataType.values().length);
            for(Configs.DataType t: Configs.DataType.values()){
                Map<String,String> m = new HashMap<String,String>();
                list.add(m);
                m.put("key",t.name());
                m.put("value",t.getDesc());
            }
        }
        return Result.QUERY_SUCCESS.toBean(list);
    }

    /***
     * @desc 配置参数修改
     * @author 杨勇 19-6-19
     * @return
     */
    @ApiOperation(value = "配置参数修改", httpMethod = "POST",response = ResultBean.class)
    @RequestMapping(value = "/updateConfigs", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "confName", value = "参数标识", paramType = "query", dataType = "string"),
            @ApiImplicitParam(name = "value", value = "对应值", paramType = "query", dataType = "string"),
    })
    @ResponseBody
    public ResponseBean updateConfigs(String confName,String value) {
        Configs c;
        try{
            c = Configs.findEnum(confName);
        }catch (Exception e){
            return Result.NOTCONFIGS_FAIL.toBean();
        }
        if(!c.isEdit()){
            return Result.NOTUPDATE_WARN.toBean();
        }
        if(StringUtils.isBlank(value)){
            return Result.NOTNULL_FAIL.toBean();
        }
        if(c.getValue().equals(value)){
            return Result.NOTMODIFY_WARN.toBean();
        }

        Configs.dynamicAdd(confName,c.getType(),c.getDesc(),value);

        return Result.UPDATE_SUCCESS.toBean(new ConfigBean(c));
    }

    private boolean init = false;

    @PostConstruct
    public synchronized void init(){
        Result.setResultService(resultService);
        Configs.setConfigService(configService, propertiesConfig);

        String sql = MessageFormat.format("select count(*) from user_tables where table_name = ?", Configs.COM_RESULT_TABLE.getValue());
        long num = resultService.getBaseDao().count(sql, Configs.COM_RESULT_TABLE.getValue());
        if(num == 0){
            log.info(MessageFormat.format("初始化参数表【请求描述-{0}】...", Configs.COM_RESULT_TABLE.getValue()));
            sql = "CREATE TABLE " + Configs.COM_RESULT_TABLE.getValue() + " (ENUMNAME VARCHAR2(200 BYTE) NOT NULL , CODE VARCHAR2(200 CHAR) , MSG VARCHAR2(200 BYTE) , CONSTRAINT P_RESULT_TABLE_11_PK PRIMARY KEY   (    ENUMNAME   )  ENABLE )";
            resultService.getBaseDao().executeUpdateSql(sql);
            sql = "COMMENT ON COLUMN " + Configs.COM_RESULT_TABLE.getValue() + ".ENUMNAME IS '类型标识'";
            resultService.getBaseDao().executeUpdateSql(sql);
            sql = "COMMENT ON COLUMN " + Configs.COM_RESULT_TABLE.getValue() + ".CODE IS '请求类型'";
            resultService.getBaseDao().executeUpdateSql(sql);
            sql = "COMMENT ON COLUMN " + Configs.COM_RESULT_TABLE.getValue() + ".MSG IS '请求描述'";
            resultService.getBaseDao().executeUpdateSql(sql);
            log.info(MessageFormat.format("初始化参数表【请求描述-{0}】完成！", Configs.COM_RESULT_TABLE.getValue()));
            //数据库保存
        }
        //进行补全
        List<ResultBean> resultList = resultService.getAll();
        //对现有描述进行增量入库
        for(Result r:Result.values()){
            ResultBean rb = new ResultBean(r);
            if(!resultList.contains(rb)){
                resultService.save(rb);
            }
        }
        //从数据库补全
        resultList.forEach(r -> Result.dynamicAdd(r.getEnumName(),r.getMsg()));

        sql = MessageFormat.format("select count(*) from user_tables where table_name = ?", Configs.COM_CONFIG_TABLE.getValue());
        num = configService.getBaseDao().count(sql, Configs.COM_CONFIG_TABLE.getValue());
        if(num == 0){
            log.info(MessageFormat.format("初始化配置表【参数配置-{0}】...", Configs.COM_CONFIG_TABLE.getValue()));
            sql = "CREATE TABLE " + Configs.COM_CONFIG_TABLE.getValue() + " (  CONFNAME VARCHAR2(200 BYTE) NOT NULL , TYPE VARCHAR2(200 CHAR) , DESCRIBE VARCHAR2(200 BYTE) , VALUE VARCHAR2(512 CHAR) , CONSTRAINT P_CONFIG_TABLE_11_PK PRIMARY KEY   (    CONFNAME   )  ENABLE ) ";
            configService.getBaseDao().executeUpdateSql(sql);
            sql = "COMMENT ON COLUMN " + Configs.COM_CONFIG_TABLE.getValue() + ".CONFNAME IS '配置标识'";
            configService.getBaseDao().executeUpdateSql(sql);
            sql = "COMMENT ON COLUMN " + Configs.COM_CONFIG_TABLE.getValue() + ".TYPE IS '数据类型使用中添加，不允许修改INT, DOUBLE, STRING, DATE, BOOLEAN'";
            configService.getBaseDao().executeUpdateSql(sql);
            sql = "COMMENT ON COLUMN " + Configs.COM_CONFIG_TABLE.getValue() + ".DESCRIBE IS '参数描述'";
            configService.getBaseDao().executeUpdateSql(sql);
            sql = "COMMENT ON COLUMN " + Configs.COM_CONFIG_TABLE.getValue() + ".VALUE IS '对应值'";
            configService.getBaseDao().executeUpdateSql(sql);
            log.info(MessageFormat.format("初始化参数表【参数配置-{0}】完成！", Configs.COM_CONFIG_TABLE.getValue()));
        }

        //进行补全
        List<ConfigBean> confList = configService.getAll();
        //对现有描述进行增量入库
        for(Configs c:Configs.values()){
            if(!c.isEdit()) continue;
            ConfigBean cb = new ConfigBean(c);
            if(!confList.contains(cb)){
                configService.save(cb);
            }
        }
        //从数据库取值
        confList.forEach(c -> {
            Configs.dynamicAdd(c.getConfName(), Configs.DataType.valueOf(c.getType()),c.getDescribe(),c.getValue());
        });
        Configs.run();
        Result.run();

        init = true;
    }


    @Scheduled(cron = "0 0/1 * * * ?")
    public synchronized void init0(){
        if(!init) return;
        int h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if(h <= 6 || h >= 23) return;
        //从数据库动态读取取值
        List<ConfigBean> confList = configService.getAll();
        confList.forEach(c -> {
            if(!c.getConfName().equals(Configs.COM_CURR_TIME.name())){
                Configs.dynamicAdd(c.getConfName(), Configs.DataType.valueOf(c.getType()),c.getDescribe(),c.getValue());
            }
        });

        //时钟校验
        if(StringUtils.isNotBlank(Configs.COM_CURR_TIME.getValue())){//存在时钟,进行时钟增加
            if(Long.parseLong(Configs.COM_CURR_TIME.getValue()) - 12L * 60 * 60 * 1000 > System.currentTimeMillis()){
                Configs.update(Configs.COM_CURR_TIME.name(),(Long.parseLong(Configs.COM_CURR_TIME.getValue()) + 60000) + "");
            }else{
                Configs.update(Configs.COM_CURR_TIME.name(),System.currentTimeMillis() + "");
            }
        }

        //许可认证
        boolean authentication;//认证
        try{
            authentication = RsaKeyUtil.authenticate();
        }catch (Exception e){authentication = false;}

        if(!authentication){
            RsaKeyUtil.verify(RsaKeyUtil.ser0(Configs.COM_SERIAL.getValue()), Configs.COM_LICENCE.getValue(),RsaKeyUtil.publicKey);
        }
    }

}