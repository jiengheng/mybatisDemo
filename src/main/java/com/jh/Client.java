package com.jh;

import com.jh.mapper.OrderMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/***
 * @Auther: JiangHeng
 * @ClassName: Client
 * @Description: 测试mybatis的一级,二级缓存
 * @Date: 2020/6/5 0005 下午 10:38
 * @version : V1.0
 */
public class Client {

     static String resource = "SqlMapConfig.xml";
     public static void main(String[] args) throws IOException {
         getSession2();
     }

     /**
     * @description:TODO
      * @param:
     * @return void
     * @author JiangHeng
     * @date 2020/6/5 0005 下午 10:59
     */
    private static void getSession1() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        //-------一级缓存是SqlSession缓存,只要SqlSession关闭了,那么缓存数据就不存在了
        // 2,只要sqlsession不关闭,那么查询的就是一级缓存(sqlsession)
        // --------
        SqlSession session = factory.openSession();
        OrderMapper orderMapper = session.getMapper(OrderMapper.class);
        //第一次查询
        Map<String, Object> map = orderMapper.selectOrderByid(1);
        System.out.println("map--->"+map);
        //第二次查询
        Map<String, Object> map2 = orderMapper.selectOrderByid(1);
        System.out.println("map2--->"+map2);
        session.close();
        //--------------
    }

    /**
    * @description:sqlsession关闭了,再次创建session时就是不同的对象,就无法使用一级缓存
     * @param:
    * @return void
    * @author JiangHeng
    * @date 2020/6/5 0005 下午 11:05
    */
    private static void getSession2() throws IOException {
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        //-------一级缓存是SqlSession缓存,只要SqlSession关闭了,那么缓存数据就不存在了
        // 2,只要sqlsession不关闭,那么查询的就是二级缓存(sqlsession)
        // --------
        //第一次查询
        SqlSession session1 = factory.openSession();
        OrderMapper orderMapper1 = session1.getMapper(OrderMapper.class);
        Map<String, Object> map = orderMapper1.selectOrderByid(1);
        System.out.println("map--->"+map);
        session1.close();

        //第二次查询
        SqlSession session2 = factory.openSession();
        OrderMapper orderMapper2 = session2.getMapper(OrderMapper.class);
        Map<String, Object> map2 = orderMapper2.selectOrderByid(1);
        System.out.println("map2--->"+map2);
        session2.close();
        //--------------
    }

}
