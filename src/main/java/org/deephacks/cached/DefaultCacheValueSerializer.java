package org.deephacks.cached;

import org.deephacks.cached.buffer.ByteBuf;
import org.deephacks.cached.buffer.PooledByteBufAllocator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DefaultCacheValueSerializer extends CacheValueSerializer<Object> {
    private PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
    @Override
    public ByteBuf write(Object value) {
        ByteBuf buf = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(value);
            byte[] bytes = baos.toByteArray();
            buf = allocator.directBuffer(bytes.length);
            buf.writeBytes(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return buf;
    }

    @Override
    public Object read(ByteBuf buf) {
        try {
            int readable = buf.readableBytes();
            byte[] bytes = new byte[readable];
            buf.readBytes(bytes);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream in = new ObjectInputStream(bais);
            return in.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
