package com.lagou.edu.factory;

import com.alibaba.druid.util.StringUtils;
import com.lagou.edu.anno.MyAutowired;
import com.lagou.edu.anno.MyService;
import com.lagou.edu.anno.MyTransactional;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * \* @Author: ZhuFangTao
 * \* @Date: 2020/5/6 8:01 下午
 * \
 */
public class AnnotationBeanFactory {

    //存放javaBean
    private static Map<String, Object> map = new HashMap<>();

    //设置扫描包路径
    private static String SCAN_PATH = "com.lagou.edu";

    //所有被MyService注解修饰的类
    private static Set<Class<?>> myServiceSet;

    static {
        try {
            //获取所有被@MyService修饰的类
            myServiceSet = new Reflections(SCAN_PATH,
                    new TypeAnnotationsScanner(),
                    new SubTypesScanner(),
                    new FieldAnnotationsScanner())
                    .getTypesAnnotatedWith(MyService.class);

            //处理被MyService修饰的类 创建bean （没有处理循环依赖）；
            createBeans();

            //处理创建的bean中被MyAutowired修饰的属性并设置；
            handleBeanProps();

            //处理事务注解（创建动态代理）
            processTransactionalBean();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    private static void createBeans() throws IllegalAccessException, InstantiationException {
        for (Class service : myServiceSet) {
            map.put(getBeanName(service), service.newInstance());
        }
    }

    private static String getBeanName(Class clazz) {
        MyService annotation = (MyService) clazz.getAnnotation(MyService.class);
        String beanName = annotation.value();
        if (StringUtils.isEmpty(beanName)) {
            String className = clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
            beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
        }
        return beanName;
    }

    private static void handleBeanProps() throws IllegalAccessException {
        for (Object bean : map.values()) {
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(MyAutowired.class)) {
                    field.setAccessible(true);
                    MyAutowired annotation = field.getAnnotation(MyAutowired.class);
                    String beanName = annotation.beanName();
                    if (!StringUtils.isEmpty(beanName)) {
                        //如果已经指定了beanName 则直接从map中取
                        field.set(bean, map.get(beanName));
                    } else {
                        //否则根据类型设置
                        Class clazz = field.getType();
                        for (Object o : map.values()) {
                            if (o.getClass().getName() == clazz.getName() || clazz.isAssignableFrom(o.getClass())) {
                                field.set(bean, o);
                            }
                        }
                    }
                }
            }
        }
    }


    private static void processTransactionalBean() {
        ProxyFactory proxyFactory = (ProxyFactory) map.get("proxyFactory");
        for (Class service : myServiceSet) {
            if (service.isAnnotationPresent(MyTransactional.class)) {
                Class[] interfaces = service.getInterfaces();
                if (interfaces != null && interfaces.length > 0) {
                    map.put(getBeanName(service), proxyFactory.getJdkProxy(map.get(getBeanName(service))));
                } else {
                    map.put(getBeanName(service), proxyFactory.getCglibProxy(map.get(getBeanName(service))));
                }
            }
        }
    }

    public static  Object getBean(String id) {
        return map.get(id);
    }
}