package org.uma.VeRxina;

import io.reactivex.Single;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class HelloSingle {

    public static void main(String[] args) {

        Single<DayOfWeek> single = Single.create(emitter -> emitter.onSuccess(LocalDate.now().getDayOfWeek()));
        single.subscribe(System.out::println);


    }
}
