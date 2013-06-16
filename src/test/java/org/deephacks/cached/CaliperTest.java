package org.deephacks.cached;


import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;
import org.deephacks.cached.CacheTest.AnObject;

import java.util.ArrayList;
import java.util.List;

public class CaliperTest extends SimpleBenchmark {
    private KryoCacheValueSerializer<AnObject> serializer = new KryoCacheValueSerializer<>(AnObject.class);

    private Cache<Integer, AnObject> defaultCache = CacheBuilder.<Integer, AnObject>newBuilder()
            .build();

    private Cache<Integer, AnObject> kryoCache = CacheBuilder.<Integer, AnObject>newBuilder()
            .serializer(serializer).build();

    private List<AnObject> objects = new ArrayList<>();

    @Param
    int size;


    @Override
    protected void setUp() throws Exception {
        for (int i = 0; i < size; i++) {
            objects.add(CacheTest.createObject());
        }
    }

    public void timeKryoGet(int reps) {
        kryoCache.put(0, objects.get(0));
        for (int i = 0; i < reps; i++) {
            kryoCache.get(0);
        }
    }

    public void timeKryoPut(int reps) {
        for (int i = 0; i < reps; i++) {
            kryoCache.put(0, objects.get(0));
        }
    }

    public void timeSerializationPut(int reps) {
        for (int i = 0; i < reps; i++) {
            defaultCache.put(0, objects.get(0));
        }
    }

    public void timeSerializationGet(int reps) {
        defaultCache.put(0, objects.get(0));
        for (int i = 0; i < reps; i++) {
            defaultCache.get(0);
        }
    }

    public static void main(String[] args) throws Exception {
        Runner.main(CaliperTest.class, new String[]{"-Dsize=1"});
    }

}
