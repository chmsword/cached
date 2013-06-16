package org.deephacks.cached;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;

public class CacheTest {
    private KryoCacheValueSerializer<AnObject> serializer = new KryoCacheValueSerializer<>(AnObject.class);

    private Cache<Integer, AnObject> cache = CacheBuilder.<Integer, AnObject>newBuilder()
            .serializer(serializer).build();

    @Test
    public void test_put_get() {
        int count = 0;
        while(count < 100) {
            AnObject object = createObject();
            cache.put(++count, object);
            AnObject theObject = cache.get(count);
            assertEquals(object, theObject);
        }
        assertEquals(cache.size(), 100);
        assertEquals(cache.values().size(), 100);
        assertEquals(cache.keySet().size(), 100);
        cache.clear();
        assertTrue(cache.isEmpty());
        Map<Integer, AnObject> objs = new HashMap<>();
        objs.put(1, createObject());
        objs.put(2, createObject());
        objs.put(3, createObject());
        cache.putAll(objs);
        assertEquals(cache.keySet().size(), 3);
    }

    public AnObject createObject() {
        AnObject2 o2 = new AnObject2(UUID.randomUUID().toString());
        ArrayList<Integer> ints = new ArrayList<>();
        ints.add(6);
        ints.add(7);
        ints.add(8);
        byte[] bytes = new byte[1024];
        new Random().nextBytes(bytes);

        return new AnObject('c', new Random().nextInt(), ints, o2, bytes);
    }

    public static class AnObject {
        private char hello;
        private int hello2;
        private List<Integer> list;
        private AnObject2 o2;
        private byte[] bytes;

        public AnObject(char hello, int hello2, List<Integer> ints, AnObject2 o2, byte[] bytes){
            this.hello = hello;
            this.hello2 = hello2;
            this.list = ints;
            this.o2 = o2;
            this.bytes = bytes;
        }

        @Override
        public String toString() {
            return "AnObject{" +
                    "hello=" + hello +
                    ", hello2=" + hello2 +
                    ", list=" + list +
                    ", o2=" + o2 +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AnObject anObject = (AnObject) o;

            if (hello != anObject.hello) return false;
            if (hello2 != anObject.hello2) return false;
            if (!Arrays.equals(bytes, anObject.bytes)) return false;
            if (list != null ? !list.equals(anObject.list) : anObject.list != null) return false;
            if (o2 != null ? !o2.equals(anObject.o2) : anObject.o2 != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (int) hello;
            result = 31 * result + hello2;
            result = 31 * result + (list != null ? list.hashCode() : 0);
            result = 31 * result + (o2 != null ? o2.hashCode() : 0);
            result = 31 * result + (bytes != null ? Arrays.hashCode(bytes) : 0);
            return result;
        }
    }
    public static class AnObject2 {
        private String value;

        public AnObject2(String value){
            this.value = value;
        }
        @Override
        public String toString() {
            return "AnObject2{" +
                    "value='" + value + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AnObject2 anObject2 = (AnObject2) o;

            if (value != null ? !value.equals(anObject2.value) : anObject2.value != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }
}
