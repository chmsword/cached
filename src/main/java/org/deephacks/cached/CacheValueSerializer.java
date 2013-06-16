package org.deephacks.cached;

import org.deephacks.cached.buffer.ByteBuf;

public abstract class CacheValueSerializer<V> {

    public abstract ByteBuf write(V value);

    public abstract V read(ByteBuf buffer);
}
