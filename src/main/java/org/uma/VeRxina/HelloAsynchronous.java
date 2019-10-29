package org.uma.VeRxina;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class HelloAsynchronous {

    public static void main(String[] args) throws InterruptedException {
        flowableFlatMap();
    }

    private static void heavySubscribe() throws InterruptedException {
        // 1秒ごとにデータ通知しているのに、2秒ごとにデータが通知されてしまう。
        Flowable.interval(1000L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> System.out.println("emit: " + System.currentTimeMillis() + "ms: " + data))
                .subscribe(data -> Thread.sleep(2000L));
        Thread.sleep(5000L);
    }

    private static void heavySubscribe2() throws InterruptedException {
        // ライブラリが、内部で対応してくれるため、500msの遅れが発生しない。
        Flowable.interval(1000L, TimeUnit.MILLISECONDS)
                .doOnNext(data -> System.out.println("emit: " + System.currentTimeMillis() + "ms: " + data))
                .subscribe(data -> Thread.sleep(500L));
        Thread.sleep(5000L);
    }

    private static void flowableOnlyMainThread() {
        System.out.println("start");

        Flowable.just(1, 2, 3).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + ": " + integer);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + ": 完了");
            }
        });

        System.out.println("end");

    }

    private static void flowableMultiThread() {
        System.out.println("start");

        Flowable.interval(300L, TimeUnit.MILLISECONDS).subscribe(new ResourceSubscriber<Long>() {
            @Override
            public void onNext(Long data) {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + ": " + data);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + ": 完了");
            }
        });

        System.out.println("end");

        // スリープしないと、mainスレッドが終わってしまう。。。
        App.sleep(2000);

    }

    private static void flowableSubscribeOn() {
        Flowable.just(1, 2, 3, 4, 5)
                .subscribeOn(Schedulers.computation())
                .subscribeOn(Schedulers.io()) // 2つめ以降の設定は無効になる。
                .subscribe(data -> {
                    String threadName = Thread.currentThread().getName();
                    System.out.println(threadName + ": " + data);
                });

        App.sleep(2000);

    }

    private static void flowableObserveOn() {
        // これ便利だな。
        // 生成側は、一定的にデータを生成し、
        // 消費者側で、全部受け取るのではなく、何個か飛ばしデータを受け取る。
        Flowable.interval(300L, TimeUnit.MILLISECONDS)
                .onBackpressureDrop()
                .observeOn(Schedulers.computation(), false, 2)
                .subscribe(new ResourceSubscriber<Long>() {
                    @Override
                    public void onNext(Long data) {
                        // 重い処理
                        try {
                            Thread.sleep(1000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            System.exit(1);
                        }
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ": " + data);
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ": 完了");
                    }
                });

        App.sleep(10000);
    }

    private static void flowableFlatMap() {
        // 自作Thread
        int threadCt = Runtime.getRuntime().availableProcessors() + 1;
        System.out.println(threadCt);
        ExecutorService executor = Executors.newFixedThreadPool(threadCt);
        Scheduler myScheduler = Schedulers.from(executor);

        Iterator<Integer> iterator = IntStream.range(0, 10000).iterator();

        Flowable.fromIterable(() -> iterator)
                .flatMap(data -> {
                    // 100ms遅れてデータを通知するFlowable生成
                    // delay()メソッドは、デフォルトでSchedularsを指定している。    @SchedulerSupport(SchedulerSupport.COMPUTATION)
                    return Flowable.just(data).delay(1000L, TimeUnit.MICROSECONDS);
                })
                .subscribe(
                        data -> {
                            String threadName = Thread.currentThread().getName();
                            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("ss.SSS"));
                            System.out.println(threadName + ": " + data + ", time: " + time);
                        },
                        Throwable::printStackTrace,
                        () -> System.out.println("完了")
                );
        App.sleep(2000);

    }


    private static void flowableConcatMap() {
        // concatMapは、遅い。flatMapの方が断然速い。
        Iterator<Integer> iterator = IntStream.range(0, 10000).iterator();

        Flowable.fromIterable(() -> iterator)
                .concatMap(data -> {
                    // 100ms遅れてデータを通知するFlowable生成
                    // delay()メソッドは、デフォルトでSchedularsを指定している。    @SchedulerSupport(SchedulerSupport.COMPUTATION)
                    return Flowable.just(data).delay(1000L, TimeUnit.MICROSECONDS);
                })
                .subscribe(
                        data -> {
                            String threadName = Thread.currentThread().getName();
                            String time = LocalTime.now().format(DateTimeFormatter.ofPattern("ss.SSS"));
                            System.out.println(threadName + ": " + data + ", time: " + time);
                        },
                        Throwable::printStackTrace,
                        () -> System.out.println("完了")
                );
        App.sleep(2000);

    }

}
