package org.uma.VeRxina;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.ResourceSubscriber;

import java.util.Arrays;
import java.util.List;

public class SubscribeWith {
    public static void main(String[] args) {

        Flowable flowable = Flowable.create(new FlowableOnSubscribe<String>() {
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

        Disposable disposable = (Disposable) flowable.subscribeWith(new ResourceSubscriber() {
            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        });

    }
}
