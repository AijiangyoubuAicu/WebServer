package com.webserver.http;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * HTTP协议规定内容
 * @author ta
 *
 */
public class HttpContext {
    /**
     * 资源类型与Content-Type的对应关系
     * key:资源的后缀名
     * value:Content-Type消息头对应的值
     */
    private static final Map<String,String> mimeMapping = new HashMap<>();

    static {
        //初始化
        initMimeMapping();
    }
    /**
     * 初始化资源类型与Content-Type的值的对应关系
     */
    private static void initMimeMapping() {
        /*
         * 实现:
         * 解析conf/web.xml文件,将根标签下所有
         * 名为<mime-mapping>的子标签获取到,并
         * 且将其子标签:
         * <extension>中间的文本作为key
         * <mime-type>中间的文本作为value
         * 存入到mimeMapping这个Map中完成初始化
         *
         */
        try {
            SAXReader reader = new SAXReader();
            Document doc
                    = reader.read(new File("conf/web.xml"));
            Element root = doc.getRootElement();
            List<Element> list = root.elements("mime-mapping");
            for(Element mime : list) {
                String key = mime.elementText("extension");
                String value = mime.elementText("mime-type");
                mimeMapping.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(mimeMapping.size());
    }
    /**
     * 根据资源类型获取对应的Content-Type值
     * @param ext
     * @return
     */
    static String getMimeType(String ext) {
        return mimeMapping.get(ext);
    }

    public static void main(String[] args) {
        String type
                = HttpContext.getMimeType("png");
        System.out.println(type);
    }
}








