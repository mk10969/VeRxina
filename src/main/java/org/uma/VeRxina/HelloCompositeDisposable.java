package org.uma.VeRxina;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HelloCompositeDisposable {

    public static void main(String[] args) throws InterruptedException {
        CompositeDisposable compositeDisposable = new CompositeDisposable();

        compositeDisposable.add(Flowable.range(1, 3)
                .doOnCancel(() -> System.out.println("No.1 canceled"))
                .observeOn(Schedulers.computation())
                .subscribe(data -> {
                    Thread.sleep(100L);
                    System.out.println("No.1 :" + data);
                })
        );

        compositeDisposable.add(Flowable.range(1, 3)
                .doOnCancel(() -> System.out.println("No.2 canceled"))
                .observeOn(Schedulers.computation())
                .subscribe(data -> {
                    Thread.sleep(100L);
                    System.out.println("No.2 :" + data);
                })
        );

        Thread.sleep(150L);
        compositeDisposable.dispose();

        // https://qiita.com/fukasawah/items/71b8f5931265fd384231
        // 安易なdispose()は、アカン。
    }
}
