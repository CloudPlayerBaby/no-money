package com.yuriyuri.web.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/*
* BaseServlet用于资源分发
* */
public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp){
        String uri = req.getRequestURI();  //假设我要获取这段资源 /no-money/user/loginServlet
        int index = uri.lastIndexOf('/');
        String methodName = uri.substring(index+1); //这样就获取了loginServlet

        //获取UserServlet的字节码对象，这里的this就代表UserServlet
        Class<? extends BaseServlet> cls = this.getClass();
        try {
            //然后就通过获取的字节码对象，从该对象中获取方法
            //第一个参数是方法名，之后的参数是该方法需要使用的形参的字节码对象
            Method method = cls.getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //调用方法
            //第一个参数是谁调用这个方法，之后的形参就是与getMethod中对应的实际参数
            method.invoke(this,req,resp);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
