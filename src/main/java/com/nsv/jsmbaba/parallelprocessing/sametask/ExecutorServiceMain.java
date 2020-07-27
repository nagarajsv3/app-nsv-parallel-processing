package com.nsv.jsmbaba.parallelprocessing.sametask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class ExecutorServiceMain {
    private static final Random PRNG = new Random();

    public static Result compute(Object obj) throws InterruptedException {
        int wait = PRNG.nextInt(3000);
        Thread.sleep(wait);
        System.out.println("CurrentThread=" + Thread.currentThread().getName());
        return new Result(wait);
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        List<Object> objects = new ArrayList<Object>();
        for (int i = 0; i < 100; i++) {
            objects.add(new Object());
        }
        List<Callable<Result>> tasks = new ArrayList<Callable<Result>>();
        for (final Object object : objects) {
            Callable<Result> c = new Callable<Result>() {
                @Override
                public Result call() throws Exception {
                    return compute(object);
                }
            };
            tasks.add(c);
        }


        int availableProcessors = Runtime.getRuntime().availableProcessors();
        System.out.println("Available Processors=" + availableProcessors);
        //ExecutorService exec = Executors.newCachedThreadPool();
        // some other exectuors you could try to see the different behaviours

        ExecutorService exec = Executors.newFixedThreadPool(availableProcessors);
        //ExecutorService exec = Executors.newSingleThreadExecutor();
        try {
            long start = System.currentTimeMillis();
            List<Future<Result>> results = exec.invokeAll(tasks);
            int sum = 0;
            for (Future<Result> fr : results) {
                sum += fr.get().wait;
                System.out.println(String.format("Task waited %d ms",
                        fr.get().wait));
            }
            long elapsed = System.currentTimeMillis() - start;
            System.out.println(String.format("Elapsed time: %d ms", elapsed));
            System.out.println(String.format("... but compute tasks waited for total of %d ms; speed-up of %.2fx", sum, sum / (elapsed * 1d)));
        } finally {
            exec.shutdown();
        }
    }
}
