package com.webserver.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 响应对象
 * 该类的每一个实例用于表示服务端发送给客户端的一个
 * 响应内容.
 * 一个响应包含的三部分为:
 * 状态行,响应头,响应正文
 * @author ta
 *
 */
public class HttpResponse {
    /*
     * 状态行相关信息
     */
    //状态代码
    private int statusCode = 200;
    //状态描述
    private String statusReason = "OK";

    /*
     * 响应头相关信息
     */
    private Map<String,String> headers = new HashMap<>();


    /*
     * 响应正文相关信息
     */
    //响应正文的实体文件
    private File entity;

    /*
     * 与连接相关的信息
     */
    private Socket socket;
    private OutputStream out;

    public HttpResponse(Socket socket) {
        try {
            this.socket = socket;
            this.out = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将当前响应对象内容以一个标准HTTP响应格式
     * 发送给客户端
     */
    public void flush() {
        try {
            //发送状态行
            sendStatusLine();
            //发送响应头
            sendHeaders();
            //发送响应正文
            sendContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送状态行
     */
    private void sendStatusLine() {
        try {
            String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);//written CR
            out.write(10);//written LF
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送响应头
     */
    private void sendHeaders() {
        try {
            //用来说明响应正文的类型
//			String line = "Content-Type: text/html";
//			out.write(line.getBytes("ISO8859-1"));
//			out.write(13);//written CR
//			out.write(10);//written LF
//
//			//用来说明响应正文的长度(字节量)
//			line = "Content-Length: "+entity.length();
//			out.write(line.getBytes("ISO8859-1"));
//			out.write(13);//written CR
//			out.write(10);//written LF
            /*
             * 遍历headers,将所有响应头进行发送
             */
            Set<Entry<String,String>> set = headers.entrySet();
            for(Entry<String,String> header : set) {
                String name = header.getKey();
                String value = header.getValue();
                String line = name+": "+value;
                out.write(line.getBytes("ISO8859-1"));
                out.write(13);//written CR
                out.write(10);//written LF
            }



            //单独发送CRLF表示响应头发送完毕了!
            out.write(13);//written CR
            out.write(10);//written LF
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送响应正文
     */
    private void sendContent() {
        if(entity!=null) {
            try(
                    FileInputStream fis
                            = new FileInputStream(entity)
            ){
                int len = -1;
                byte[] data = new byte[1024*10];
                while((len = fis.read(data))!=-1) {
                    out.write(data, 0, len);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置响应正文文件的同时会自动包含两个
     * 响应头:
     * Content-Type与Content-Length
     * @param entity 实体
     */
    public void setEntity(File entity) {
        /*
         * 根据设置的正文文件名,获取它对应的后缀名,
         * 然后去HttpContext中找到对应的Content-Type
         * 的值.
         */
        String fileName = entity.getName();
        String ext = fileName.substring(
                fileName.lastIndexOf(".")+1);
        String type = HttpContext.getMimeType(ext);

        //添加响应头
        putHeader("Content-Type", type);
        putHeader("Content-Length", entity.length()+"");


        this.entity = entity;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }
    /**
     * 添加一个响应头
     * @param name
     * @param value
     */
    public void putHeader(String name,String value) {
        this.headers.put(name, value);
    }



}
