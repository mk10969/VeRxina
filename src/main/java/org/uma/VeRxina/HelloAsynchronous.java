package org.uma.VeRxina;

import io.reactivex.Flowable;
import io.reactivex.subscribers.ResourceSubscriber;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

public class HelloAsynchronous {

    public static void main(String[] args) throws InterruptedException {
        flowableMultiThread();
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

    }

}
