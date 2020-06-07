package com.jh;

import redis.clients.jedis.Jedis;

import java.util.Collections;

/**
 * @Auther: JiangHeng
 * @ClassName: JedisClient
 * @Description: 采用jedis来操作redis
 * @Date: 2020/6/6 0006 上午 10:19
 * @version : V1.0
 *
 *  https://www.cnblogs.com/love-cj/p/8242439.html
 * 分布式锁的4大特性
 * 1,互斥性。在任意时刻，只有一个客户端能持有锁。
 * 2,不会发生死锁。即使有一个客户端在持有锁的期间崩溃而没有主动解锁，也能保证后续其他客户端能加锁。
 * 3,具有容错性。只要大部分的Redis节点正常运行，客户端就可以加锁和解锁。
 * 4,解铃还须系铃人。加锁和解锁必须是同一个客户端，客户端自己不能把别人加的锁给解了。
 *
 */
public class JedisClient {

     public static void main(String[] args) {
         Jedis jedis = new Jedis("127.0.0.1", 6379);
//         String set = jedis.set("jedis", "jedis");
//         System.out.println(set);
//
//         String jedis1 = jedis.get("jedis");
//         System.out.println(jedis1);

         String oldValue = jedis.getSet("jedis", "123");
         System.out.println(oldValue);

     }

     /**
     * @description:分布式加锁
      * @param: jedis
      * @param: lockey
      * @param: requestId
      * @param: expire
     * @return boolean
     * @author JiangHeng
     * @date 2020/6/7 0007 下午 5:35
      *
      * jedis.set方法
      * key
      * value
      * nxxx NX|XX, NX -- Only set the key if it does not already exist. XX
      * -- Only set the key if it already exist.
      * expx EX|PX, expire time units: EX = seconds; PX = milliseconds
      * time expire time in the units of {@param #expx}
     */
     public static boolean addLock(Jedis jedis,String lockey,String requestId,long expire) {
         //2.9.0
         String result = jedis.set(lockey, requestId, "NX", "PX", expire);
         //3.0.1版本
         //String result = jedis.set(lockKey, requestId,SetParams.setParams().nx().px(expireTime));
         if ("OK".equals(result)) {
             return true;
         }
         return false;
     }

     /**
     * @description:分布式释放锁
      * @param: jedis
      * @param: lockey
      * @param: requestId
     * @return boolean
     * @author JiangHeng
     * @date 2020/6/7 0007 下午 5:37
     */
    public static boolean unLock(Jedis jedis,String lockey,String requestId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockey), Collections.singletonList(requestId));
        if ("OK".equals(result)) {
            return true;
        }
        return false;
    }

}
