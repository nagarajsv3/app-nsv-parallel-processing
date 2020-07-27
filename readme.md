Parallel Processing of same task multiple times
1. Note that using newCachedThreadPool could be bad if objects is a big list. A cached thread pool could create a thread per task! You may want to use newFixedThreadPool(n) where n is something reasonable (like the number of cores you have, assuming compute() is CPU bound).

2. Streams are designed for data parallelism, not task parallelism. See stackoverflow.com/a/23370799/208288