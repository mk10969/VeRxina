package org.uma.VeRxina;

import io.reactivex.Flowable;

public class HelloAlternativeData {


    public static void main(String[] args) {
        test_代替データ();

    }


    private static void test_代替データ() {
        // 大事なのは、エラーが発生したら、そのあとのデータは通知されないこと。
        // エラーだけスキップしたいね。。。

        Flowable.just(1, 3, 5, 0, 2, 4)
                .map(data -> 100 / data)
                .onErrorReturnItem(0)
                .subscribe(System.out::println);

    }

}
