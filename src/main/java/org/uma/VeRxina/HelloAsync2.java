package org.uma.VeRxina;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class HelloAsync2 {
    public static void main(String[] args) throws InterruptedException {
        test_２つのFlwoableを結合();

    }


    private static void test_２つのスレッドから同時更新() throws InterruptedException {
        final Counter counter = new Counter();

        Flowable.range(1, 10000)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(
                        data -> counter.increment(),
                        error -> error.printStackTrace(),
                        () -> System.out.println("1番目 counter: " + counter.getCount())
                );

        Flowable.range(1, 10000)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation())
                .subscribe(
                        data -> counter.increment(),
                        error -> error.printStackTrace(),
                        () -> System.out.println("2番目 counter: " + counter.getCount())
                );

        Thread.sleep(2000L);
    }


    public static class Counter {
        private int count;

        void increment() {
            count++;
        }

        int getCount() {
            return this.count;
        }
    }

    private static void test_２つのFlwoableを結合() throws InterruptedException {
        final Counter counter = new Counter();

        Flowable<Integer> flowable1 = Flowable.range(1, 10000)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation());

        Flowable<Integer> flowable2 = Flowable.range(1, 10000)
                .subscribeOn(Schedulers.computation())
                .observeOn(Schedulers.computation());


        Flowable.merge(flowable1, flowable2)
                .subscribe(
                        data -> counter.increment(),
                        error -> System.out.println(error + "エラーです"),
                        () -> System.out.println("2番目 counter: " + counter.getCount())
                );

        Thread.sleep(2000L);
    }


}
