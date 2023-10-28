package com.lihuia.mysterious.service.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author maple@lihuia.com
 * @date 2023/4/14 9:03 PM
 */

@Component
public class RedisUtils {


    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    public Boolean redisHasKey(String redisKeyPrefix, String user) {
        return redisTemplate.hasKey(redisKeyPrefix + user);
    }

    public String getKeyValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setKeyValue(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    public void setKeyValue(String key, String value, long expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
    }

    public Boolean delRedisKey(String redisKeyPrefix, String key) {
        String nKey = redisKeyPrefix + key;
        return redisTemplate.delete(nKey);
    }

    public Set<String> getRedisSetValue(String redisKeyPrefix, String key) {
        String newKey = redisKeyPrefix + key;
        return redisTemplate.opsForSet().members(newKey);
    }

    public void setRedisSetValue(String redisKeyPrefix, String key, String value) {
        String newKey = redisKeyPrefix + key;
        redisTemplate.opsForSet().add(newKey, value);
    }

    public void batchSetRedisSetValue(String key, String[] values) {
        redisTemplate.opsForSet().add(key, values);
    }

    public void deleteRedisSetValue(String redisKeyPrefix, String key, String value) {
        String newKey = redisKeyPrefix + key;
        redisTemplate.opsForSet().remove(newKey, value);
    }

    /**
     * 设置有过期时间的 hash
     *
     * @param redisKey
     * @param hKey
     * @param hValue
     * @param expireTime
     * @param timeUnit
     */
    public void setRedisHash(String redisKey, String hKey, String hValue, long expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForHash().put(redisKey, hKey, hValue);
        redisTemplate.expire(redisKey, expireTime, timeUnit);
    }

    public void setRedisHash(String redisKey, String hKey, String hValue) {
        redisTemplate.opsForHash().put(redisKey, hKey, hValue);
    }

    public void delRedisHashKeys(String redisKey, List<String> keys) {
        keys.forEach(key -> redisTemplate.opsForHash().delete(redisKey, key));
    }

    public void delRedisHashKey(String redisKey, String key) {
        redisTemplate.opsForHash().delete(redisKey, key);
    }

    /**
     * 获取hash中key的值
     *
     * @param redisKey
     * @param hKey
     * @return
     */
    public String getRedisHashValue(String redisKey, String hKey) {
        Object hValue = redisTemplate.opsForHash().get(redisKey, hKey);
        if (hValue == null) {
            return null;
        }

        return hValue.toString();
    }

    /**
     * 获取hash
     *
     * @param redisKey
     * @return
     */
    public Map<Object, Object> getRedisHash(String redisKey) {
        return redisTemplate.opsForHash().entries(redisKey);
    }

    public boolean redisHashHasKey(String redisKey, String key) {
        return redisTemplate.opsForHash().hasKey(redisKey, key);
    }


    /**
     * 自增key初始化为value
     *
     * @param redisKeyPrefix
     * @param value          初始化值
     * @param liveTime       过期时间
     */
    public void incrInit(String redisKeyPrefix, int value, long liveTime) {
        RedisAtomicLong counter = new RedisAtomicLong(redisKeyPrefix, redisTemplate.getConnectionFactory());
        counter.set(value);
        counter.expire(liveTime, TimeUnit.MILLISECONDS);
    }

    /**
     * key 自增
     *
     * @param key
     * @param liveTime 过期时间
     * @return
     */
    public Long incr(String key, long liveTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = counter.incrementAndGet();
        if ((increment == null || increment.longValue() == 0) && liveTime > 0) {
            counter.expire(liveTime, TimeUnit.MILLISECONDS);
        }

        return increment;
    }

    /**
     * key 自减
     *
     * @param key
     * @param liveTime
     * @return
     */
    public Long decr(String key, long liveTime) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        long decrement = counter.decrementAndGet();
        if (decrement == 0 && liveTime > 0) {
            counter.expire(liveTime, TimeUnit.MILLISECONDS);
        }
        return decrement;
    }

    /**
     * 获取自增KEY的值
     *
     * @param key
     * @return
     */
    public Long getIncr(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        return counter.get();
    }

    /**
     * 删除自增key
     *
     * @param key
     */
    public void deleteIncrKey(String key) {
        RedisAtomicLong counter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        counter.expire(0, TimeUnit.MILLISECONDS);
    }

    /**
     * lpush和rpop成对使用 列表左侧入队
     *
     * @param key
     * @param value
     * @return
     */
    public Long leftPush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public Object leftPushAll(String key, List value) {
        Object size = redisTemplate.opsForList().leftPushAll(key, value);
        System.out.println( size );
        System.out.println( redisTemplate.opsForList().range(key,0,-1) );
        return size;
    }
    /**
     * right出队
     *
     * @param key
     * @return
     */
    public String rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 移除列表中的某个值
     *
     * @param key
     * @param value
     */
    public Long deleteFromList(String key, String value) {
        return redisTemplate.opsForList().remove(key, 0, value);
    }

    /**
     * 获取列表长度
     *
     * @param key
     * @return
     */
    public Long getListLength(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 获取列表所有元素
     *
     * @param key
     * @return
     */
    public List<String> getListAllItem(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }

    public boolean listContainsValue(String key, String value) {
        return this.getListAllItem(key).contains(value);
    }

    public void convertAndSend(ChannelTopic topic, String jsonString) {
        redisTemplate.convertAndSend(topic.getTopic(), jsonString);
    }
}
