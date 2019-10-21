package org.uma.VeRxina;


import io.reactivex.functions.Action;

public class HelloLambda {

    public static void main(String[] args) throws Exception {
        HelloLambda target = new HelloLambda();
        target.execute();

    }

    public void execute() throws Exception {
        //匿名クラス
        //匿名クラスの場合、toString()を呼んでいない。
        // thisは関数型インターフェースのインスタンスを示している。
        Action anonymous = new Action() {
            @Override
            public void run() throws Exception {
                System.out.println("匿名クラスの場合: " + this);
            }
        };
        // ラムダ式
        // ラムダ式の場合、toString()を呼んでいる。
        // thisは、実装されているクラス自身を示している。
        Action lambda = () -> System.out.println("ラムダ式の場合: " + this);

        anonymous.run();
        lambda.run();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

}
