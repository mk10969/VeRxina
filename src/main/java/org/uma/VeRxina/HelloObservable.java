package org.uma.VeRxina;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HelloObservable {
    public static void main(String[] args) {

        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                String[] datas = {"hello", "world", "!!!"};
                for (String data : datas) {
                    if (emitter.isDisposed()) {
                        return;
                    }
                    emitter.onNext(data);

                }
                emitter.onComplete();
            }
        });

        observable
                .observeOn(Schedulers.computation())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        //何もしない
                    }

                    @Override
                    public void onNext(String s) {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ": " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        String threadName = Thread.currentThread().getName();
                        System.out.println(threadName + ": 完了");

                    }
                });


        App.sleep(500);

    }

}
