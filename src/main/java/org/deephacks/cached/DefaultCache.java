package org.deephacks.cached;


import org.deephacks.cached.buffer.ByteBuf;
import org.deephacks.cached.buffer.util.internal.chmv8.ConcurrentHashMapV8;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class DefaultCache<K, V> implements Cache<K, V> {
    private final CacheValueSerializer<Object> serializer;
    private ConcurrentHashMapV8<K, ByteBuf> cache = new ConcurrentHashMapV8<>();

    public DefaultCache(CacheValueSerializer<Object> serializer) {
        this.serializer = serializer;
    }

    @Override
    public int size() {
        return cache.size();
    }

    @Override
    public boolean isEmpty() {
        return cache.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return cache.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        ByteBuf buf = write((V) value);
        return cache.containsValue(buf);
    }

    @Override
    public V get(Object key) {
        ByteBuf buf = cache.get(key);
        return read(buf);
    }

    @Override
    public V put(K key, V value) {
        ByteBuf buf = write(value);
        cache.put(key, buf);
        return value;
    }

    @Override
    public V remove(Object key) {
        ByteBuf buf = cache.remove(key);
        return read(buf);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (K k : m.keySet()) {
            ByteBuf buf = write(m.get(k));
            cache.put(k, buf);
        }
    }

    @Override
    public void clear() {
        cache.clear();
    }

    @Override
    public Set<K> keySet() {
        return cache.keySet();
    }

    @Override
    public Collection<V> values() {
        ArrayList<V> list = new ArrayList<>();
        for(K key : cache.keySet()) {
            ByteBuf buf = cache.get(key);
            V value = read(buf);
            list.add(value);
        }
        return list;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new HashSet<>();
        for(final Entry<K, ByteBuf> buf : cache.entrySet()) {
            final V value = read(buf.getValue());

            Entry<K, V> entry = new Entry<K, V>() {
                @Override
                public K getKey() {
                    return buf.getKey();
                }

                @Override
                public V getValue() {
                    return value;
                }

                @Override
                public V setValue(V value) {
                    return value;
                }
            };
            set.add(entry);
        }
        return set;
    }


    private ByteBuf write(V value) {
        return value == null ? null : serializer.write(value);
    }

    private V read(ByteBuf buf) {
        buf.resetReaderIndex();
        return buf == null ? null : (V) serializer.read(buf);
    }
}
