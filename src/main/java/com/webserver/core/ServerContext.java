package com.webserver.core;

import java.util.HashMap;
import java.util.Map;

import com.webserver.servlet.HttpServlet;

/**
 * 服务端相关配置信息
 * @author ta
 *
 */
class ServerContext {
    /**
     * 请求与对应的处理类Servlet的对应关系
     * key:请求路径
     * value:对应的某Servlet实例
     */
    private static final Map<String,HttpServlet> servletMapping = new HashMap<>();

    static {
        initServletMapping();
    }

    private static void initServletMapping() {
//		servletMapping.put("/myweb/reg", new RegServlet());
//		servletMapping.put("/myweb/login", new LoginServlet());
        /*
         * 使用DOM4J解析conf/servlets.xml文件.
         * 将根标签下的所有<servlet>标签获取到.
         * 并且用每个<servlet>标签中的属性:
         * path:的值作为key
         * className:的值得到后利用反射加载并实例化对应的
         *           Servlet的实例作为value
         *
         * 存入到servletMapping这个Map中完成初始化
         *
         */
    }
    /**
     * 根据请求获取对应的Servlet,如果该请求没有
     * 对应的Servlet方法会返回null.
     * @param path 得到的请求，用于获取 Servlet
     * @return 对应的 Servlet
     */
    static HttpServlet getServlet(String path) {
        return servletMapping.get(path);
    }


}
