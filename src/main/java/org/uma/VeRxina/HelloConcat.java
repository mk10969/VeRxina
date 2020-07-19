package org.uma.VeRxina;

import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

public class HelloConcat {

    public static void main(String[] args) throws InterruptedException {

//        List<String> list = Arrays.asList("a", "b", "c");
//        Flowable.fromIterable(list);

        test_concatFlowable();


        Thread.sleep(3000L);

    }


    private static void test_concatFlowable() {
        Flowable<Long> flowable1 = Flowable.interval(300L, TimeUnit.MILLISECONDS).take(5);
        Flowable<Long> flowable2 = Flowable.interval(500L, TimeUnit.MILLISECONDS).take(2)
                .map(data -> data + 100L);

        // flowableを直列に実行する。ただし途中でエラーが発生した場合、以降、中断される
        Flowable<Long> result = Flowable.concat(flowable1, flowable2);
        result.subscribe(System.out::println);

    }

    private static void test_concatArrayFlowable() {
        Flowable<Long> flowable1 = Flowable.interval(300L, TimeUnit.MILLISECONDS).take(5);
        Flowable<Long> flowable2 = Flowable.interval(500L, TimeUnit.MILLISECONDS).take(2)
                .map(data -> data + 100L);

        // concatEagerは、concatと違い並列にObservableは処理するけど
        // 返してくる順番は保証されている。

        Flowable<Long> result = Flowable.concatArray(flowable1, flowable2);
        result.subscribe(System.out::println);

    }

}
