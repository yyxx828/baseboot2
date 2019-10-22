package com.xajiusuo.jpa.filter;

import com.xajiusuo.jpa.config.BaseDaoImpl;
import com.xajiusuo.jpa.param.entity.UserContainer;
import com.xajiusuo.jpa.servlet.JscpHttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by 杨勇 on 2019/7/8.
 */
@Slf4j
@WebFilter(urlPatterns = "/*",filterName = "RequestFilter")
public class RequestFilter implements Filter {


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        JscpHttpServletRequest request = new JscpHttpServletRequest((HttpServletRequest) servletRequest);

        UserContainer.setUser(null,request);

        filterChain.doFilter(request,servletResponse);

        BaseDaoImpl.clear();
    }

    @Override
    public void destroy() {
    }



}
