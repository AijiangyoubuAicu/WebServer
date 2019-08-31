package com.webserver.servlet;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

/**
 * 处理登录流程
 * @author ta
 *
 */
public class LoginServlet extends HttpServlet {
    public void service(HttpRequest request,HttpResponse response) {
        System.out.println("LoginServlet:开始登录...");
        //1
        String username = request.getPrameter("username");
        String password = request.getPrameter("password");
        if(username==null||password==null) {
            forward("/myweb/login_fail.html",request,response);
            return;
        }
        //2
        try (
                RandomAccessFile raf
                        = new RandomAccessFile("user.dat","r")
        ){
            for(int i=0;i<raf.length()/100;i++) {
                raf.seek(i*100);

                byte[] data = new byte[32];
                raf.read(data);
                String name = new String(data, StandardCharsets.UTF_8).trim();
                if(name.equals(username)) {
                    raf.read(data);
                    String pwd = new String(data, StandardCharsets.UTF_8).trim();
                    if(pwd.equals(password)) {
                        //登录成功
                        forward("/myweb/login_success.html",request,response);
                        return;
                    }
                    break;
                }
            }
            forward("/myweb/login_fail.html",request,response);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("LoginServlet:登录完毕!");
    }
}

