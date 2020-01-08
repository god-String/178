package com.baizhi.zjy.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Component
public class MyInterceptor1 implements HandlerInterceptor {
    @Override
    // Controller执行之前
    // 拦截处理，并且 根据处理的结果，决定是否继续往后进行
    // 返回true-放行； false--没通过
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        // 进行拦截处理的功能代码
        /*System.out.println("......1.......");
        HttpSession session = request.getSession();
        Object o1 = session.getAttribute("admin");
        if (o1!=null){
            return true; // 放行
        }*/
        //response.sendRedirect(request.getContextPath()+"/jsp/login.jsp");
        //System.out.println(request.getContextPath());
        //response.sendRedirect(request.getContextPath()+"/login.jsp");
        //return false;
        //return true;
        return true;
    }

    @Override
    // Controller执行之后
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("......2.......");
    }

    @Override
    // 视图 执行后
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("......3.......");
    }
}
