package com.transquiz.transasker.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheStats;
import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class ControllableCache<K, V> implements Cache<K, V> {
    private ConcurrentHashMap<K, V> cacheMap;
    private Function<K, V> loader;

    public ControllableCache(Function<K, V> loader) {
        cacheMap = new ConcurrentHashMap<>();
        this.loader = loader;
    }

    @Override
    public V getIfPresent(Object key) {
        return cacheMap.get(key);
    }

    @Deprecated
    @Override
    @SneakyThrows
    public V get(K key, Callable<? extends V> loader) throws ExecutionException {
        V result;
        if (!cacheMap.containsKey(key)) {
            result = loader.call();
            cacheMap.put(key, result);
        } else {
            result = cacheMap.get(key);
        }
        return result;
    }

    public V get(K key) {
        V result;
        if (!cacheMap.containsKey(key)) {
            result = loader.apply(key);
            cacheMap.put(key, result);
        } else {
            result = cacheMap.get(key);
        }
        return result;
    }


    @Override
    public ImmutableMap<K, V> getAllPresent(Iterable<?> keys) {
        ImmutableMap.Builder<K, V> resultMap = ImmutableMap.builder();
        for (Object key : keys) {
            V value = cacheMap.get(key);
            if (value != null) {
                resultMap.put((K) key, value);
            }
        }
        return resultMap.build();
    }

    @Override
    public void put(K key, V value) {
        if (key != null && value != null) {
            cacheMap.put(key, value);
        } else {
            throw new IllegalArgumentException("Key or value can not be null!");
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (m == null) {
            throw new IllegalArgumentException("Your map is null!");
        }
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            if (key != null && value != null) {
                cacheMap.put(key, value);
            } else {
                throw new IllegalArgumentException("Key or value can not be null!");
            }
        }
    }

    @Override
    public void invalidate(Object key) {
        if (key != null) {
            cacheMap.remove(key);
        } else {
            throw new IllegalArgumentException("Key can not be null!");
        }
    }

    @Override
    public void invalidateAll(Iterable<?> keys) {
        if (keys == null) {
            throw new IllegalArgumentException("Your collection is null!");
        }
        for (Object key : keys) {
            if (key != null) {
                cacheMap.remove(key);
            } else {
                throw new IllegalArgumentException("Key can not be null!");
            }
        }
    }

    @Override
    public void invalidateAll() {
        cacheMap = new ConcurrentHashMap<>();
    }

    @Override
    public long size() {
        return cacheMap.size();
    }

    @Override
    public CacheStats stats() {
        return null;
    }

    @Override
    public ConcurrentMap<K, V> asMap() {
        return cacheMap;
    }

    @Override
    public void cleanUp() {
    }
}
