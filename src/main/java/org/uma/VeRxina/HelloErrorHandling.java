package org.uma.VeRxina;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import java.util.stream.IntStream;

public class HelloErrorHandling {


    public static void main(String[] args) {
        test_処理の再実行();

    }

    private static void test_処理の再実行() {

        Flowable<Integer> flowable = Flowable.<Integer>create(emitter -> {
            System.out.println("処理開始");
            IntStream.range(0, 10).forEach(i -> {
                if (i == 2) {
                    throw new RuntimeException("例外発生");
                }
                emitter.onNext(i);
            });
            emitter.onComplete();
            System.out.println("処理完了");
        }, BackpressureStrategy.BUFFER)
                .doOnSubscribe(subscription -> System.out.println("flowable; doOnSubscribe"))
                // エラーが発生したら、２回まで再実行
                .retry(2);


        flowable.subscribe(data -> {
            System.out.println("data: " + data);
        }, error -> {
            System.out.println("エラー: " + error);
        }, () -> {
            System.out.println("完了");
        });

//                flowable; doOnSubscribe　Strat
//                処理開始
//                dat: 0
//                dat: 1
                  //　リトライ１回目
//                flowable; doOnSubscribe
//                処理開始
//                dat: 0
//                dat: 1
                  //　リトライ２回目
//                flowable; doOnSubscribe
//                処理開始
//                dat: 0
//                dat: 1
//                エラー: java.lang.RuntimeException: 例外発生



    }

}
