package com.dar.services.servlets.filters;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    System.out.println("Request"+ request.getMethod());

    HttpServletResponse resp = (HttpServletResponse) servletResponse;
    resp.addHeader("Access-Control-Allow-Origin","*");
    resp.addHeader("Access-Control-Allow-Methods","GET,POST,DELETE,PUT");
    resp.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, timezone,resolution,enabledCookie,token, Content-Type, Accept, Authorization");
    resp.setHeader("Content-Type", "text/json; charset=UTF-8");
    // Just ACCEPT and REPLY OK if OPTIONS
    if ( request.getMethod().equals("OPTIONS") ) {
        resp.setStatus(HttpServletResponse.SC_ACCEPTED);
        return;
    }
    chain.doFilter(request, servletResponse);
}

 @Override
public void destroy() {}
}