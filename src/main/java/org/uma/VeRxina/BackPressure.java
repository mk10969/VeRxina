package org.uma.VeRxina;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

public class BackPressure {

    public static void main(String[] args) throws InterruptedException {
        test_バックプレッシャーなし();
    }

    private static class MySubscriber<T> implements Subscriber<T> {
        @Override
        public void onSubscribe(Subscription s) {
            System.out.println("  --> onSubscribe");
            s.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(T data) {
            System.out.println("  --> onNext: " + data);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

        @Override
        public void onError(Throwable throwable) {
            System.out.println("onError!!!");
            throwable.printStackTrace();
        }

        @Override
        public void onComplete() {
            System.out.println("  --> onComplete");
        }
    }

    private static void test_バックプレッシャーなし() throws InterruptedException {
        Flowable.interval(100L, TimeUnit.MILLISECONDS)
                .observeOn(Schedulers.computation(), false, 2)
                .onBackpressureBuffer()
                .doOnSubscribe(subscription -> System.out.println("<-- subscribe"))
                .doOnNext(data -> System.out.println("Flowable generated data:" + data))
                .doOnNext(i -> System.out.println("<-- emit: " + i))
                .subscribe(new MySubscriber<>());

        Thread.sleep(10000L);
    }


    private static void test_baskpressuerBuffer() throws InterruptedException {
        /*
         * Flowableはrequestに関係なくデータを生成し、observeOnからのrequestが来ると2件データを通知。
         * Flowableで生成されたデータが全部バッファされて2件ずつ通知される。
         */
        Flowable<Long> flowable = Flowable.interval(100L, TimeUnit.MILLISECONDS)
                .take(10) //一旦、上限設定
                .doOnSubscribe(subscription -> System.out.println("<-- subscribe"))
                .doOnNext(data -> System.out.println("Flowable generated data:" + data))
                .onBackpressureBuffer();

        flowable.doOnRequest(req -> System.out.println("<-- request: " + req))
                .observeOn(Schedulers.computation(), false, 2)
                .doOnRequest(req -> System.out.println("  <-- request: " + req))
                .subscribe(new MySubscriber<>());

        Thread.sleep(11_000L);
    }

    private static void test_baskpressuerBufferCapCapacity3() throws InterruptedException {
        /*
         * Flowableはrequestに関係なくデータを生成し、observeOnからのrequestが来ると2件データを通知。
         * Flowableで生成されたデータが全部バッファされて2件ずつ通知される。
         */
        Flowable<Long> flowable = Flowable.interval(100L, TimeUnit.MILLISECONDS)
                .take(100) //一旦、上限設定
                .doOnSubscribe(subscription -> System.out.println("<-- subscribe"))
                .doOnNext(data -> System.out.println("Flowable generated data:" + data))
                .onBackpressureBuffer(40, () -> System.out.println("overflow"));

        flowable.doOnRequest(req -> System.out.println("<-- request: " + req))
                .observeOn(Schedulers.computation(), false, 20)
                .doOnRequest(req -> System.out.println("  <-- request: " + req))
                .subscribe(new MySubscriber<>());

        Thread.sleep(110_000L);
    }


}
