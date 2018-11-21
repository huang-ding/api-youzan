package com.saopay.apiyouzan.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.saopay.apiyouzan.enums.RedisKeyEnum;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author huangding
 * @description
 * @date 2018/9/29 9:08
 */
@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 指定缓存失效时间
     *
     * @param key 键
     * @param time 时间(秒) time>0
     */
    public boolean expire(RedisKeyEnum key, long time) {
        return redisTemplate.expire(key.getCode(), time, TimeUnit.SECONDS);
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(RedisKeyEnum key) {
        return redisTemplate.getExpire(key.getCode(), TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     */
    public boolean isKey(RedisKeyEnum key) {
        return redisTemplate.hasKey(key.getCode());
    }

    /**
     * 删除key
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }


    @Deprecated
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }


    public String get(RedisKeyEnum key) {
        return redisTemplate.opsForValue().get(key.getCode());
    }

    /**
     * add 无超时时间
     */
    @Deprecated
    public void set(String key, String val) {
        redisTemplate.opsForValue().set(key, val);
    }

    /**
     * add 无超时时间
     */
    public void set(RedisKeyEnum key, String val) {
        redisTemplate.opsForValue().set(key.getCode(), val);
    }


    /**
     * add 有超时时间
     */
    @Deprecated
    public void set(String key, String val, long time) {
        redisTemplate.opsForValue().set(key, val,time);
    }

    /**
     * add 有超时时间
     */
    public void set(RedisKeyEnum key, String val, long time) {
        redisTemplate.opsForValue().set(key.getCode(), val);
        expire(key, time);
    }


    public RedisUtil() {
    }

    /**
     * 获取对应key的数据 没有按照规则插入
     */
    public <T> T get(RedisKeyEnum key, RedisGetMethodInterface redisGetMethodInterface,
        long time, Class<T> type) {
        String data = redisTemplate.opsForValue().get(key.getCode());
        if (!StringUtils.isNotBlank(data)) {
            if (redisGetMethodInterface != null) {
                data = (String) redisGetMethodInterface.method();
                redisTemplate.opsForValue().set(key.getCode(), data);
                if (time > 0) {
                    expire(key, time);
                }
            } else {
                return null;
            }
        }
        String typeName = type.getName();
        String lang = "java.lang";
        if (StringUtils.contains(typeName, lang)) {
            return (T) data;
        }
        return JSON.parseObject(data, type);
    }


    /**
     * 向一张hash表中取数据
     */
    public <T> T hget(RedisKeyEnum key, String item, Class<T> type) {
        return hget(key, item, null, 0, type);
    }

    public String hget(RedisKeyEnum key, String item) {
        return (String) redisTemplate.opsForHash().get(key.getCode(), item);
    }


    /**
     * 向一张hash表中取数据,如果不存在按照规则创建
     */
    public <T> T hget(RedisKeyEnum key, String item,
        RedisGetMethodInterface redisGetMethodInterface,
        Class<T> type) {
        return hget(key, item, redisGetMethodInterface, 0, type);
    }

    /**
     * 向一张hash表中取数据,如果不存在按照规则创建
     *
     * @param redisGetMethodInterface 规则
     * @param time 超时时间
     */
    public <T> T hget(RedisKeyEnum key, String item,
        RedisGetMethodInterface redisGetMethodInterface,
        long time, Class<T> type) {
        String data = (String) redisTemplate.opsForHash().get(key.getCode(), item);
        if (!StringUtils.isNotBlank(data)) {
            return JSON.parseObject(data, type);
        }
        if (redisGetMethodInterface != null) {
            return redisGetMethodInterfaceData(redisGetMethodInterface, key, item, time);
        }
        return null;
    }

    public <T> T hget(RedisKeyEnum key, String item,
        RedisGetMethodInterface redisGetMethodInterface,
        long time, TypeReference<T> typeReference) {
        String data = (String) redisTemplate.opsForHash().get(key.getCode(), item);
        if (!StringUtils.isNotBlank(data)) {
            return JSON.parseObject(data, typeReference);
        }
        if (redisGetMethodInterface != null) {
            return redisGetMethodInterfaceData(redisGetMethodInterface, key, item, time);
        }
        return null;
    }

    private <T> T redisGetMethodInterfaceData(RedisGetMethodInterface redisGetMethodInterface,
        RedisKeyEnum key, String item, long time) {
        T t = (T) redisGetMethodInterface.method();
        redisTemplate.opsForHash().put(key.getCode(), item, JSON.toJSONString(t));
        if (time > 0) {
            expire(key, time);
        }
        return t;
    }

    public void hset(RedisKeyEnum key, String item, Object value) {
        hset(key, item, value, 0);
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param time 时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     */
    public void hset(RedisKeyEnum key, String item, Object value, long time) {
        redisTemplate.opsForHash().put(key.getCode(), item, value);
        if (time > 0) {
            expire(key, time);
        }
    }

    /**
     * 将所有指定的值插入存储在键的列表的头部。如果键不存在，则在执行推送操作之前将其创建为空列表。（从左边插入）
     */
    public Long leftPush(RedisKeyEnum key, String... value) {
        if (value != null && value.length > 0) {
            if (value.length == 1) {
                return redisTemplate.opsForList().leftPush(key.getCode(), value[0]);
            }
            redisTemplate.opsForList().leftPushAll(key.getCode(), value);
        }
        return null;
    }

    public String leftPop(RedisKeyEnum key) {
        return leftPop(key, 0);
    }

    /**
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     *
     * @param timeout 阻塞时间（秒）
     */
    public String leftPop(RedisKeyEnum key, long timeout) {
        if (timeout > 0) {
            return redisTemplate.opsForList().leftPop(key.getCode(), timeout, TimeUnit.SECONDS);
        }
        return redisTemplate.opsForList().leftPop(key.getCode());
    }


    /**
     * 将所有指定的值插入存储在键的列表的头部。如果键不存在，则在执行推送操作之前将其创建为空列表。（从右边插入）
     */
    public Long rightPush(RedisKeyEnum key, String... value) {
        if (value != null && value.length > 0) {
            if (value.length == 1) {
                return redisTemplate.opsForList().rightPush(key.getCode(), value[0]);
            }
            redisTemplate.opsForList().rightPushAll(key.getCode(), value);
        }
        return null;
    }

    public String rightPop(RedisKeyEnum key) {
        return rightPop(key, 0);
    }

    /**
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止。
     *
     * @param timeout 阻塞时间（秒）
     */
    public String rightPop(RedisKeyEnum key, long timeout) {
        if (timeout > 0) {
            return redisTemplate.opsForList().rightPop(key.getCode(), timeout, TimeUnit.SECONDS);
        }
        return redisTemplate.opsForList().rightPop(key.getCode());
    }


}
