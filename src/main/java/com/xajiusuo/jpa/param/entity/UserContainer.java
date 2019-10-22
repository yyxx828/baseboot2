package com.xajiusuo.jpa.param.entity;

/**
 * Created by hadoop on 19-6-13.
 */

import com.alibaba.fastjson.JSON;
import com.xajiusuo.jpa.servlet.JscpHttpServletRequest;
import com.xajiusuo.jpa.servlet.JscpInputStream;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

/***
 * 通过线程获取当前用户，需要项目通过请求过滤器进行注入
 */
public class UserContainer {

    private final static ThreadLocal<UserSession> us = new ThreadLocal<UserSession>();

    private final static ThreadLocal<ServletRequest> reqs = new ThreadLocal<ServletRequest>();

    private UserContainer(){}

    public static void setUser(UserSession u,ServletRequest request){
        if(u != null)
        us.set(u);
        if(request != null && reqs.get() == null)
        reqs.set(request);
    }

    public static UserSession getU(){
        return us.get();
    }

    public static Integer getId(){
        return us.get() == null ? null : us.get().getId();
    }

    public static String getFullName(){
        return us.get() == null ? null : us.get().getFullname();
    }

    public static ServletRequest getQ(){
        return reqs.get();
    }

    public static void remove(){
        us.remove();
        reqs.remove();
    }

    public static Map<String,Object> info(){
        ServletRequest request = getQ();

        if(request == null){
            return Collections.emptyMap();
        }

        Map map = null;
        if(request instanceof JscpHttpServletRequest){
            map = JSON.parseObject(((JscpHttpServletRequest) request).toString("UTF-8"));
        }

        if(map == null){map = new HashMap<>();}

        Enumeration<String> it = request.getParameterNames();
        while(it.hasMoreElements()){
            String key = it.nextElement();
            map.put(key,request.getParameter(key));
        }

        return map;
    }

    static class User implements Serializable{
        Integer uid;
        String name;
        String password;
    }

    public static void main(String[] args) throws Exception {
        User u = new User();
//        u.uid = 21432;
//        u.name = "dsfaf";
//        u.password = "dsfafdsfdsaf";

//        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("/home/hadoop/test/user.out")));
//
//        oos.writeObject(u);
//
//        oos.flush();
//        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("/home/hadoop/test/user.out")));

        Object o = ois.readObject();
        if(o instanceof User){
            System.out.println(((User) o).uid);
            System.out.println(((User) o).name);
            System.out.println(((User) o).password);
        }
        ois.close();


    }

}
