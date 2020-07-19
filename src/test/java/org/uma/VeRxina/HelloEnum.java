package org.uma.VeRxina;

import org.junit.jupiter.api.Test;

public class HelloEnum {


    @Test
    void test(){
        Code.of(0);

        String str = "000";
        System.out.println(Integer.valueOf(str));
    }


    public static enum Code{

        ZERO(0, "zero"),
        ONE(1, "one"),
        TWO(2, "twe")
        ;

        private Integer code;
        private String name;

        Code(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public Integer getCode() {
            return this.code;
        }

        public String getName() {
            return this.name;
        }

        public static void of(Integer code){

            if(ZERO.getCode().equals(code)){
                System.out.println(code);
                System.out.println("ZERO");
            }
            System.out.println("ooooo");

        }


    }
}
