package com.webserver.servlet;

import java.io.File;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * 所有 Servlet 的超类
 * @author ta
 *
 */
public abstract class HttpServlet {
    public abstract void service(HttpRequest request,HttpResponse response);

    /**
     * 跳转到指定路径对应的资源
     * @param path 接收的路径名
     * @param request 请求
     * @param response 响应
     */
    void forward(String path, HttpRequest request, HttpResponse response) {
        File file = new File("./webapps"+path);
        response.setEntity(file);
    }
}









