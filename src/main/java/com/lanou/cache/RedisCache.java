package com.lanou.cache;

import org.apache.ibatis.cache.Cache;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by dllo on 17/11/2.
 */
public class RedisCache implements Cache {

    private static JedisConnectionFactory jedisConnectionFactory;

    private final String id;

    //读写锁
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public RedisCache(final String id) {

        //若传入的id为空,则抛出参数异常
        if (id == null) {
            throw new IllegalArgumentException("缓存需要一个id");
        }

        this.id = id;
    }

    //获取缓存对象的唯一标识
    @Override
    public String getId() {
        return this.id;
    }

    //将参数的key 和 value存到缓存对象中
    @Override
    public void putObject(Object key, Object value) {
        JedisConnection connection = null;

        try {

            connection = (JedisConnection) jedisConnectionFactory.getConnection();

            //redis 的序列化工具
            RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();

            //将 object 转成序列化对象
            connection.set(serializer.serialize(key),
                    serializer.serialize(value));

        } catch (JedisConnectionException e) {
            e.printStackTrace();
        } finally {
            //关闭connection
            if (connection != null) {
                connection.close();
            }
        }
    }

    //缓存中根据key获取值
    @Override
    public Object getObject(Object key) {
        JedisConnection connection = null;

        Object result = null;
        try {
            connection = (JedisConnection) jedisConnectionFactory.getConnection();

            RedisSerializer<Object> redisSerializer = new JdkSerializationRedisSerializer();

            //根据key获取缓存中的值
            //将序列化对象,反转
            result = redisSerializer.deserialize(connection.get(redisSerializer.serialize(key)));

        } catch (JedisConnectionException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return result;
    }

    //根据key 移除value
    //方法可选 , 核心代码中未调用此方法
    @Override
    public Object removeObject(Object key) {

        JedisConnection connection = null;

        Object result = null;
        try {
            connection = (JedisConnection) jedisConnectionFactory.getConnection();

            RedisSerializer<Object> redisSerializer = new JdkSerializationRedisSerializer();

            //移除key对应的一条数据
            result = connection.expire(redisSerializer.serialize(key), 0);//

        } catch (JedisConnectionException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return result;
    }

    //清空缓存
    @Override
    public void clear() {

        JedisConnection connection = null;
        try {

            connection = (JedisConnection) jedisConnectionFactory.getConnection();
            //清空缓存的两个方法
            connection.flushDb();
            connection.flushAll();

        } catch (JedisConnectionException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

    }

    @Override
    public int getSize() {

        JedisConnection connection = null;
        int count = 0;
        try {
            connection = (JedisConnection) jedisConnectionFactory.getConnection();

            count = Integer.valueOf(connection.dbSize().toString());

        } catch (JedisConnectionException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return count;
    }

    //读写锁
    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.lock;
    }

    public static void setJedisConnectionFactory(JedisConnectionFactory jedisConnectionFactory) {
        RedisCache.jedisConnectionFactory = jedisConnectionFactory;
    }
}
