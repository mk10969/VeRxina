package org.uma.VeRxina;

import io.reactivex.Flowable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

public class SubscribeSubscriptionCancel {

    public static void main(String[] args) {

        Flowable.interval(200L, TimeUnit.MILLISECONDS).subscribe(new Subscriber<Long>() {

            private Subscription subscription;
            private long time;

            @Override
            public void onSubscribe(Subscription s) {
                this.subscription = s;
                this.time = System.currentTimeMillis();
                this.subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Long data) {
                if (System.currentTimeMillis() - time > 500) {
                    subscription.cancel();
                    System.out.println("購読解除");
                    return;
                }
                System.out.println("data : " + data);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("エラー");
            }

            @Override
            public void onComplete() {
                System.out.println("完了");
            }
        });

        App.sleep(2000);

    }
}
