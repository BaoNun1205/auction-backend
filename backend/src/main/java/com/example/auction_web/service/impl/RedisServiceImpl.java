package com.example.auction_web.service.impl;

import com.example.auction_web.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl<K, F, V> implements RedisService<K, F, V> {
    RedisTemplate<K, V> redisTemplate;
    HashOperations<K, F, V> hashOperations;

    public RedisServiceImpl(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }


    @Override
    public void set(K key, V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void setTimeToLive(K key, long timeoutInDays) {
        redisTemplate.expire(key, timeoutInDays, TimeUnit.DAYS);
    }

    @Override
    public void hashSet(K key, F field, V value) {
        hashOperations.put(key, field, value);
    }

    @Override
    public boolean hashExists(K key, F field) {
        return hashOperations.hasKey(key, field);
    }

    @Override
    public Object get(K key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Map<F, V> getFields(K key) {
        return hashOperations.entries(key);
    }

    @Override
    public Object hashGet(K key, F field) {
        return hashOperations.get(key, field);
    }

    @Override
    public List<Object> hashGetFiledPrefixes(K key, String filedPrefix) {
        List<Object> objects = new ArrayList<>();
        Map<F, V> hashEntries = hashOperations.entries(key);

        for (Map.Entry<F, V> entry : hashEntries.entrySet()) {
            if (entry.getKey().toString().startsWith(filedPrefix)) {
                objects.add(entry.getValue());
            }
        }
        return objects;
    }

    @Override
    public Set<F> getFiledPrefix(K key) {
        return hashOperations.entries(key).keySet();
    }

    @Override
    public void delete(K key) {
        redisTemplate.delete(key);
    }

    @Override
    public void deleteField(K key, F field) {
        hashOperations.delete(key, field);
    }

    @Override
    public void deletes(K key, List<F> fields) {
        for (F filed : fields) {
            hashOperations.delete(key, filed);
        }
    }
}
