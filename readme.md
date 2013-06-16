### Cached - An off-heap slab cache for Java

Using standard java serialization

    Cache<Integer, AnObject> cache = CacheBuilder.<Integer, AnObject>newBuilder().build();
    cache.put(1, new AnObject());
    AnObject obj = cache.get(1);


Using a Kryo serializer

    KryoCacheValueSerializer<AnObject> serializer = new KryoCacheValueSerializer<>(AnObject.class);
    Cache<Integer, AnObject> cache = CacheBuilder.<Integer, AnObject>newBuilder()
               .serializer(serializer).build();
    cache.put(1, new AnObject());
    AnObject obj = cache.get(1);


Some benchmarks

     0% Scenario{vm=java, trial=0, benchmark=KryoGet, size=1} 3208.17 ns; σ=126.11 ns @ 10 trials
    25% Scenario{vm=java, trial=0, benchmark=KryoPut, size=1} 8607.30 ns; σ=920.77 ns @ 10 trials
    50% Scenario{vm=java, trial=0, benchmark=SerializationPut, size=1} 17424.07 ns; σ=1651.44 ns @ 10 trials
    75% Scenario{vm=java, trial=0, benchmark=SerializationGet, size=1} 48492.15 ns; σ=6658.39 ns @ 10 trials

           benchmark    us linear runtime
             KryoGet  3.21 =
             KryoPut  8.61 =====
    SerializationPut 17.42 ==========
    SerializationGet 48.49 ==============================
