package com.xajiusuo.jpa.config;

import com.xajiusuo.jpa.down.*;
import com.xajiusuo.jpa.param.e.Configs;
import com.xajiusuo.jpa.param.e.Result;
import com.xajiusuo.jpa.util.ResponseBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NestedRuntimeException;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;

/**
 * Created by 杨勇 on 2019/06/18.
 */
@RestController
@Slf4j
public abstract class BaseController {

    @Autowired
    protected PropertiesConfig propertiesConfig;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    public ResponseBean getResponseBean(Result r){
        return r.toBean();
    }

    @Deprecated
    public Pageable transPage(Pageable pageable) {
        return transPage(pageable,null);
    }

    public Pageable transPage(Pageable pageable,Sort sort) {
        return transPage(pageable,sort,true);
    }

    public Pageable transPage(Pageable pageable,Sort sort,boolean usePageSort) {
        Sort s;
        Sort s1 = sort,s2 = sort;
        if(usePageSort){
            s1 = pageable.getSort();
        }else{
            s2 = pageable.getSort();
        }
        if(s1 != null && s1.isSorted() && !s1.equals(Sort.unsorted())){
            s = s1;
        }else if(s2 != null && s2.isSorted() && !s2.equals(Sort.unsorted())){
            s = s2;
        }else{
            s = Sort.unsorted();
        }

        if(s.equals(pageable.getSort())){
            return pageable;
        }
        return PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(),s);
    }

    /***
     * 文件下载方法
     * @param fileName 文件名称
     * @param contentType 文件类型
     * @param downFun 提供字节流输出流
     * @throws Exception
     */
    protected void fileDownOrShow(String fileName, String contentType, OutputStreamDown downFun) throws Exception {
        if(StringUtils.isBlank(contentType)){
            contentType = FileContentType.text;
        }
        response.setContentType(contentType);
        if(contentType.equals(FileContentType.text)){
            response.addHeader("Content-Type", "text/html; charset=utf-8");
        }else {
            response.addHeader("Content-Type", contentType);
        }
        if(StringUtils.isNotBlank(fileName)){
            response.addHeader("Content-Disposition", "attachment;filename=" + converName(fileName));
        }
        OutputStream out = response.getOutputStream();
        downFun.write(out);
        out.flush();
        out.close();
    }


    protected void fileDownOrShow(String fileName, String contentType, WriterDown downFun) throws Exception {
        fileDownOrShow(fileName,contentType,OutputStreamDown.to((out) -> downFun.write(new BufferedWriter(new OutputStreamWriter(out, Configs.COM_CHARSET_NAME.getValue())))));
    }

    protected void fileDownOrShow(String fileName, String contentType, List<?> list) throws Exception {
        fileDownOrShow(fileName, contentType, WriterDown.to((out) -> {
            for(Object o : list) {
                if(o != null) {
                    out.write(o.toString() + "\r\n");
                }
            }
        }));
    }

    protected void fileDownOrShow(String fileName, String contentType, InputStream in) throws Exception {
        fileDownOrShow(fileName,contentType,OutputStreamDown.to((out) -> {
            byte[] buffer = new byte[8192];
            int len = 0;
            while((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            in.close();
        }));
    }

    protected static  <E> Page<E> listToPage(List list,int num,int size){
        list = list == null ? Collections.emptyList() : list;
        num -= 1;
        size = size < 1 ? 10 : size;
        int count = list.size();

        if(count != 0 && num * size >= count) {//最后一页号码重新计算
            num = num + (count - size - num * size) / size;
        }
        list = list.subList(num * size ,(num * size + size) < count ? (num * size + size) : count);

        Pageable pageable = PageRequest.of(num,size);
        return new PageImpl<E>(list, pageable, count);
    }

    public String converName(String name) {
        try {
            return new String(name.getBytes(), "ISO8859-1");
        }catch(Exception e) {
            return name;
        }
    }

    public static Logger log() {
        return log;
    }

    /**
     * 本Controller的异常解析
     * @Author 杨勇
     * @Date 2018/3/5 11:19
     */
    @ExceptionHandler(Exception.class)
    public ResponseBean handleException(Exception e){
        log.error(e.getMessage(),e);
        String message;
        if (e instanceof NestedRuntimeException){
            message = ((NestedRuntimeException) e).getRootCause().getLocalizedMessage();
        }else{
            message = e.getLocalizedMessage();
        }
        return Result.REQUEST_FAIL.toBean(message);
    }

}