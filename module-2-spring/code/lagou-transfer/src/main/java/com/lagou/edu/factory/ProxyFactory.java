package com.lagou.edu.factory;

import com.lagou.edu.anno.MyAutowired;
import com.lagou.edu.anno.MyService;
import com.lagou.edu.utils.TransactionManager;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Proxy;

/**
 * @author 应癫
 * <p>
 * <p>
 * 代理对象工厂：生成代理对象的
 */

@MyService("proxyFactory")
public class ProxyFactory {


    @MyAutowired
    private TransactionManager transactionManager;


    /**
     * Jdk动态代理
     *
     * @param obj 委托对象
     * @return 代理对象
     */
    public Object getJdkProxy(Object obj) {

        // 获取代理对象
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    Object result = null;

                    try {
                        // 开启事务(关闭事务的自动提交)
                        transactionManager.beginTransaction();

                        result = method.invoke(obj, args);

                        // 提交事务

                        transactionManager.commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                        // 回滚事务
                        transactionManager.rollback();

                        // 抛出异常便于上层servlet捕获
                        throw e;

                    }

                    return result;
                });

    }


    /**
     * 使用cglib动态代理生成代理对象
     *
     * @param obj 委托对象
     * @return
     */
    public Object getCglibProxy(Object obj) {
        return Enhancer.create(obj.getClass(), (MethodInterceptor) (o, method, objects, methodProxy) -> {
            Object result = null;
            try {
                // 开启事务(关闭事务的自动提交)
                transactionManager.beginTransaction();

                result = method.invoke(obj, objects);

                // 提交事务

                transactionManager.commit();
            } catch (Exception e) {
                e.printStackTrace();
                // 回滚事务
                transactionManager.rollback();

                // 抛出异常便于上层servlet捕获
                throw e;

            }
            return result;
        });
    }
}
