package com.webserver.http;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求对象
 * 该类每一个实例用于表示客户端发送过来的请求内容.
 *
 * 一个请求包含三部分:请求行,消息头,消息正文
 * @author ta
 *
 */
public class HttpRequest {
    /*
     * 请求行相关信息
     */
    // 请求方式
    private String method;
    // 请求资源的抽象路径
    private String url;
    // 请求使用的协议版本
    private String protocol;
    // 抽象路径中的请求部分
    private String requestURI;
    // 抽象路径中的参数部分
    private String queryString;
    // 保存具体每一个参数
    private Map<String,String> parameters = new HashMap<>();

    /*
     * 消息头相关信息
     */
    private Map<String,String> headers = new HashMap<>();

    /*
     * 消息正文相关信息
     */

    // 通过Socket获取的输入流,用于读取客户端消息
    private InputStream in;

    /**
     * 构造方法,用于初始化请求对象
     * @throws EmptyRequestException 空请求异常
     */
    public HttpRequest(Socket socket) throws EmptyRequestException {
        try {
            this.in = socket.getInputStream();
            /*
             * 实例化请求对象要分为三部分解析:
             * 1:解析请求行
             * 2:解析消息头
             * 3:解析消息正文
             */
            System.out.println("HttpRequest:开始解析请求...");
            parseRequestLine();
            parseHeaders();
            parseContent();
            System.out.println("HttpRequest:解析请求完毕!");
        } catch(EmptyRequestException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 解析请求行
     * @throws EmptyRequestException 空请求异常
     */
    private void parseRequestLine() throws EmptyRequestException {
        System.out.println("开始解析请求行...");
        /*
         * 1:通过输入流读取第一行字符串(请求行内容)
         * 2:将请求行内容按照空格拆分为三部分
         * 3:将这三部分内容设置到请求行对应属性
         *   method,url,protocol上完成解析请求行
         *   的工作.
         */
        try {
            //读取第一行字符串,请求行的内容
            String line = readLine();
            //判断是否为空请求
            if("".equals(line)) {
                //抛出空请求异常
                throw new EmptyRequestException();
            }

            System.out.println("请求行:"+line);
            String[] data = line.split("\\s");

            method = data[0];
            url = data[1];
            protocol = data[2];

            //进一步解析抽象路径
            parseURL();

            System.out.println("method:"+method);
            System.out.println("url:"+url);
            System.out.println("protocol:"+protocol);

        } catch (EmptyRequestException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("请求行解析完毕!");
    }
    /**
     * 进一步解析抽象路径部分
     */
    private void parseURL() {
        System.out.println("进一步解析抽象路径...");
        /*
         * 抽象路径部分有两种情况
         * 1:带参数    2:不带参数
         * 是否带参数可以通过判断url中是否含有"?"
         * 来决定.
         *
         * 如果不含有参数,那么直接将url的值赋值
         * 给requestURI即可.
         *
         * 如果含有参数,那么首先将url的值按照"?"
         * 拆分为两部分
         * 第一部分赋值给requestURI
         * 第二部分赋值给queryString
         *
         * 然后再对queryString进行拆分,首先按照
         * "&"拆分出每一个参数.接着每个参数再按照
         * "="拆分为参数名和参数值,并分别作为key
         * 和value保存到parameters中即可.
         */
        if(url.contains("?")) {
            //先按照"?"进行拆分
            String[] data = url.split("\\?");
            requestURI = data[0];
            if(data.length>1) {
                queryString = data[1];
                //首先对参数部分解码(将"%XX"内容还原对应字符)
                /*
                 * username=%E8%8C%83&password=...
                 * 经过解码得到的字符串为:
                 * username=范&password=...
                 *
                 */
                try {
                    queryString = URLDecoder.decode(
                            queryString,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //进一步拆分每一个参数
                data = queryString.split("&");
                for(String para : data) {
                    //每一个参数按照"="拆分
                    String[] arr = para.split("=");
                    if(arr.length>1) {
                        parameters.put(arr[0], arr[1]);
                    }else {
                        parameters.put(arr[0],null);
                    }
                }
            }

        }else {
            requestURI = url;
        }

        System.out.println("requestURI:"+requestURI);
        System.out.println("queryString:"+queryString);
        System.out.println("parameters:"+parameters);
        System.out.println("解析抽象路径完成");
    }

    /**
     * 解析消息头
     */
    private void parseHeaders() {
        System.out.println("开始解析消息头...");
        try {
            /*
             * 循环调用readLine方法读取每一个
             * 消息头,如果readLine方法返回的
             * 字符串是一个空字符串时,说明这次
             * 单独读取到了CRLF,那么循环就应当
             * 停止了.
             *
             * 每当我们读取到一个消息头后,就可以
             * 按照"冒号空格" 即": "进行拆分,
             * 拆分出的内容就是消息头的名字和对应
             * 的值,然后将他们分别以key,value形式
             * 存入到headers这个Map中完成消息头
             * 的解析工作
             */
            while(true) {
                String line = readLine();
                if("".equals(line)) {
                    break;
                }
                String[] data = line.split(": ");
                headers.put(data[0], data[1]);
                System.out.println(line);
            }

            System.out.println("headers:"+headers);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("消息头解析完毕!");
    }
    /**
     * 解析消息正文
     */
    private void parseContent() {
        System.out.println("开始解析消息正文...");


        System.out.println("消息正文解析完毕!");
    }

    /**
     * 通过客户端输入流读取一行字符串(以CRLF结尾的)
     * @return 返回每一段报文的信息
     * @throws Exception 读取文件的IO异常
     */
    private String readLine() throws Exception  {
        try {
            StringBuilder builder = new StringBuilder();
            int d = -1;// 记录每次读取到的字节
            // c1表示上次读取的字符,c2表示本次读取到的字符
            char c1 = 'a',c2='a';
            while((d = in.read())!=-1) {
                c2 = (char)d;
                //是否上次读取的是CR,本次读取的是LF
                if(c1==13&&c2==10) {
                    break;
                }
                builder.append(c2);
                c1 = c2;
            }
            return builder.toString().trim();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
    public String getMethod() {
        return method;
    }
    public String getUrl() {
        return url;
    }
    public String getProtocol() {
        return protocol;
    }
    /**
     * 获取当前请求中指定消息头的值
     * @param name 消息头的名字
     * @return 消息头的信息
     */
    public String getHeader(String name) {
        return headers.get(name);
    }
    public String getRequestURI() {
        return requestURI;
    }
    public String getQueryString() {
        return queryString;
    }
    /**
     * 根据参数名获取对应的参数值,若给定的参数不存在
     * 则返回值为null
     * @param name 参数名
     * @return 参数
     */
    public String getPrameter(String name) {
        return this.parameters.get(name);
    }

}

