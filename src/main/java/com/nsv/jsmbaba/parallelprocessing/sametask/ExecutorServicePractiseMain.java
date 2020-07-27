package com.nsv.jsmbaba.parallelprocessing.sametask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorServicePractiseMain {

    // 1! = 1
    // 2! = 2 * 1 = n * ((n-1)!)
    // 3! = 3 * 2 * 1 = n * ((n-1)!)

    public static NumberSquare squareOfANumber(Integer number, NumberSquare numFact){
        numFact.setSquare(number*number);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {


        }
        System.out.println("Thread Name="+Thread.currentThread().getName()+" ; number="+number+" ; square"+numFact.getSquare());
        return numFact;
    }

    public static void main(String[] args) {

        List<NumberSquare> numberSquares = new ArrayList<>();

        for (int i = 1; i <= 32; i++) {
            numberSquares.add(new NumberSquare(i));
        }

        //long startTime = System.currentTimeMillis();
        //calculateSquareSequentially(numberSquares);
        //long endTime = System.currentTimeMillis();
        //System.out.println("Time Taken to calculate Factorial of all numbers sequentially="+(endTime-startTime));

        //numberSquares.forEach((num)->{
        //    System.out.println("Number = "+num.getNumber() + "; Factorial="+num.getSquare());
        //});

        //numberSquares.clear();
        System.out.println("**************************************************************");

        //startTime = System.currentTimeMillis();
        calculateFactorialParallelyUsingCacheThreadPool(numberSquares);
        //endTime = System.currentTimeMillis();
        //System.out.println("Time Taken to calculate Factorial of all numbers parallely="+(endTime-startTime));
        //System.out.println("Size="+ numberSquares.size());

        numberSquares.forEach((num)->{
            System.out.println("Number = "+num.getNumber() + "; Factorial="+num.getSquare());
        });




    }

    private static void calculateFactorialParallelyUsingCacheThreadPool(List<NumberSquare> numberSquares) {
        List<Callable<NumberSquare>> tasks = new ArrayList<>();

        for (NumberSquare numberSquare: numberSquares) {

            Callable<NumberSquare> task = new Callable<NumberSquare>() {
                @Override
                public NumberSquare call() throws Exception {
                    return squareOfANumber(numberSquare.getNumber(), numberSquare);
                }
            };

            tasks.add(task);
        }


        System.out.println("Available Processors= "+Runtime.getRuntime().availableProcessors());
        //ExecutorService executorService = Executors.newCachedThreadPool();
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try{
            List<Future<NumberSquare>> futureNumberSquares = executorService.invokeAll(tasks);

            futureNumberSquares.forEach(
                    (numberSquareFuture)-> {
                        try {
                            System.out.println("Number="+numberSquareFuture.get().getNumber()+";Square="+numberSquareFuture.get().getSquare());
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
            );

        }catch (Exception exp){

        }finally {
            executorService.shutdown();
        }


    }

    private static void calculateSquareSequentially(List<NumberSquare> numberSquares) {
        for (NumberSquare numFact: numberSquares) {
            squareOfANumber(numFact.getNumber(), numFact);

        }
    }

}
