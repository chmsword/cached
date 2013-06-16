package org.deephacks.cached.buffer;


import sun.misc.Cleaner;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.Buffer;
import java.nio.ByteBuffer;

public class UnsafeUtil {
    private static final Unsafe UNSAFE;
    static {
        ByteBuffer direct = ByteBuffer.allocateDirect(1);
        Field cleanerField;
        try {
            cleanerField = direct.getClass().getDeclaredField("cleaner");
            cleanerField.setAccessible(true);
            Cleaner cleaner = (Cleaner) cleanerField.get(direct);
            cleaner.clean();
        } catch (Throwable t) {
            cleanerField = null;
        }
        Field addressField;
        try {
            addressField = Buffer.class.getDeclaredField("address");
            addressField.setAccessible(true);
            if (addressField.getLong(ByteBuffer.allocate(1)) != 0) {
                addressField = null;
            } else {
                direct = ByteBuffer.allocateDirect(1);
                if (addressField.getLong(direct) == 0) {
                    addressField = null;
                }
                Cleaner cleaner = (Cleaner) cleanerField.get(direct);
                cleaner.clean();
            }
        } catch (Throwable t) {
            addressField = null;
        }

        Unsafe unsafe;
        if (addressField != null && cleanerField != null) {
            try {
                Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                unsafe = (Unsafe) unsafeField.get(null);

                // Ensure the unsafe supports all necessary methods to work around the mistake in the latest OpenJDK.
                // https://github.com/netty/netty/issues/1061
                // http://www.mail-archive.com/jdk6-dev@openjdk.java.net/msg00698.html
                try {
                    unsafe.getClass().getDeclaredMethod(
                            "copyMemory",
                            new Class[] { Object.class, long.class, Object.class, long.class, long.class });

                } catch (NoSuchMethodError t) {
                    throw t;
                }
            } catch (Throwable cause) {
                unsafe = null;
            }
        } else {
            // If we cannot access the address of a direct buffer, there's no point of using unsafe.
            // Let's just pretend unsafe is unavailable for overall simplicity.
            unsafe = null;
        }
        UNSAFE = unsafe;
    }

    public static Unsafe getUnsafe(){
        return UNSAFE;
    }
}
