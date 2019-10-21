package org.uma.VeRxina;

import io.reactivex.Flowable;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class OnSubscribe {


    public static void main(String[] args) {

        Flowable.range(1, 3).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("START");
                s.request(Long.MAX_VALUE);
                System.out.println("END"); // ちゃんと先に呼ばれるけどね。。
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onComplete() {
                System.out.println("完了");
            }
        });

    }
}
