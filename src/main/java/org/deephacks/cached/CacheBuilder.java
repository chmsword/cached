package org.deephacks.cached;

public class CacheBuilder<K, V> {

    private CacheValueSerializer<?> serializer = new DefaultCacheValueSerializer();

    private CacheBuilder() {
    }

    public static <K, V>  CacheBuilder<K, V> newBuilder() {
        return new CacheBuilder<>();
    }

    public CacheBuilder<K, V> serializer(CacheValueSerializer<V> serializer) {
        this.serializer = serializer;
        return this;
    }

    public Cache<K, V> build() {
        return new DefaultCache(serializer);
    }

}
