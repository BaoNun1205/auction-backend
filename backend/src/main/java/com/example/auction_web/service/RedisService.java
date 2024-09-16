package com.example.auction_web.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RedisService<K, F, V> {
    void set(K key, V value);

    void setTimeToLive(K key, long timeoutInDays);

    void hashSet(K key, F field, V value);

    boolean hashExists(K key, F field);

    Object get(K key);

    public Map<F, V> getFields(K key);

    Object hashGet(K key, F field);

    List<Object> hashGetFiledPrefixes(K key, String filedPrefix);

    Set<F> getFiledPrefix(K key);

    void delete(K key);

    void deleteField(K key, F field);

    void deletes(K key, List<F> fields);
}
