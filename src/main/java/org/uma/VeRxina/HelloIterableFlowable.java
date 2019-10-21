package org.uma.VeRxina;

import io.reactivex.Flowable;

import java.util.Arrays;
import java.util.List;

public class HelloIterableFlowable {

    public static void main(String[] args) {

        List<String> list = Arrays.asList("a", "b", "c");

        Flowable.fromIterable(list).subscribe(System.out::println);


    }
}
