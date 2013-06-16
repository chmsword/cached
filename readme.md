### Cached - An off-heap slab cache for Java

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
