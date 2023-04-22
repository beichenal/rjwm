package com.example.rg.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.AntPathMatcher;

import com.alibaba.fastjson2.JSON;
import com.example.rg.common.R;

import lombok.extern.slf4j.Slf4j;

/**
 * 检查用户是否已经完成登录
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
  public static final AntPathMatcher antPathMatcher = new AntPathMatcher();

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {

    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    String requestURI = request.getRequestURI();

    String[] passUrls = new String[] {
        "/employee/login",
        "/employee/logout",
        "/backend/**",
        "/front/**"
    };
    // 判断本次请求是否需要处理
    if (this.checkPassUrl(requestURI, passUrls)) {
      filterChain.doFilter(request, response);
      return;
    }
    //是否登录
    if(request.getSession().getAttribute("employee") != null ){
      log.info("用户已登录，id 为：{}", request.getSession().getAttribute("employee"));
      filterChain.doFilter(request, response);
      return;
    }
    log.info("拦截到请求：{}", request.getRequestURI());
    log.info("未登录");
    //未登录
    response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    return;
  }

  private boolean checkPassUrl(String url, String[] passUrls) {
    for (String passUrlPattern : passUrls) {
      if (antPathMatcher.match(passUrlPattern, url)) {
        return true;
      }
    }
    return false;
  }

}
