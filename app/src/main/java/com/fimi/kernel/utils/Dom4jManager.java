package com.fimi.kernel.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Dom4jManager {
    public <T> List<T> readXML(String xmlPath, String elementName, Class cls) {
        long lasting = System.currentTimeMillis();
        ArrayList arrayList = new ArrayList();
        try {
            File f = new File(xmlPath);
            SAXReader reader = new SAXReader();
            Document doc = reader.read(f);
            Element root = doc.getRootElement();
            Field[] properties = cls.getDeclaredFields();
            Iterator i = root.elementIterator(elementName);
            while (i.hasNext()) {
                Element foo = (Element) i.next();
                Object newInstance = cls.newInstance();
                for (int j = 0; j < properties.length; j++) {
                    if (!properties[j].isSynthetic() && !properties[j].getName().equals("serialVersionUID")) {
                        Method setmeth = newInstance.getClass().getMethod("set" + properties[j].getName().substring(0, 1).toUpperCase() + properties[j].getName().substring(1), properties[j].getType());
                        setmeth.invoke(newInstance, foo.attributeValue(properties[j].getName()));
                    }
                }
                arrayList.add(newInstance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long lasting2 = System.currentTimeMillis();
        System.out.println("读取XML文件结束,用时" + (lasting2 - lasting) + "ms" + arrayList.size());
        return arrayList;
    }
}
