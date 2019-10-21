package org.uma.VeRxina;

import io.reactivex.Maybe;
import io.reactivex.MaybeObserver;
import io.reactivex.disposables.Disposable;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class HelloMaybe {

    public static void main(String[] args) {
        Maybe<DayOfWeek> maybe = Maybe.create(emitter -> emitter.onSuccess(LocalDate.now().getDayOfWeek()));
        maybe.subscribe(new MaybeObserver<DayOfWeek>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(DayOfWeek dayOfWeek) {
                System.out.println(dayOfWeek);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                // MaybeObserverの場合、このメソッド呼ばれない。
                System.out.println("完了");
            }
        });

    }
}
