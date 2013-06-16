package org.deephacks.cached;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.deephacks.cached.buffer.ByteBuf;
import org.deephacks.cached.buffer.ByteBufOutputStream;
import org.deephacks.cached.buffer.PooledByteBufAllocator;

public class KryoCacheValueSerializer<T> extends CacheValueSerializer<T> {
    private static final PooledByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
    private static final KryoReflectionFactorySupport kryo = new KryoReflectionFactorySupport();
    private final Class<?> cls;

    public KryoCacheValueSerializer(Class<T> cls) {
        this.cls = cls;
        kryo.register(this.cls);
    }

    @Override
    public ByteBuf write(T value) {
        ByteBuf buf = allocator.directBuffer();
        ByteBufOutputStream byteBufOutput = new ByteBufOutputStream(buf);
        Output output = new Output(byteBufOutput);
        kryo.writeObject(output, value);
        output.flush();
        return buf;
    }

    @Override
    public T read(ByteBuf buf) {
        buf.resetReaderIndex();
        int readable = buf.readableBytes();
        byte[] bytes = new byte[readable];
        buf.readBytes(bytes);
        Input input = new Input(bytes);
        return (T) kryo.readObject(input, cls);
    }

}
