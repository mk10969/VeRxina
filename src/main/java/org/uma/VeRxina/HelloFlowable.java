package org.uma.VeRxina;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.List;

public class HelloFlowable {

    public static void main(String[] args) {
        System.out.println("start");

        Flowable<String> flowable = Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {

                List<String> datas = Arrays.asList("Hello", "World", "!!!");

                for (String data : datas) {
                    if (emitter.isCancelled()) {
                        return;
                    }
                    emitter.onNext(data);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER);

        flowable.observeOn(Schedulers.computation()).subscribe(new Subscriber<String>() {
            private Subscription subscription;

            @Override
            public void onSubscribe(Subscription s) {
                this.subscription = s;
                this.subscription.request(1L);
            }

            @Override
            public void onNext(String s) {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + ": " + s);
                this.subscription.request(1L);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                String threadName = Thread.currentThread().getName();
                System.out.println(threadName + "完了しました。");
            }
        });

        App.sleep(500);

        System.out.println("end");
    }

}
