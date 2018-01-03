package cn.sn.zwcx.test;

import java.util.Arrays;
import java.util.List;



public class MyClass {
    public static void main(String[] args){
        //一共要买200本作业本，每本0.5元，买5本送1本，一共需要多少钱？
        thinkOf();
    }

    private static void thinkOf() {
        double one = 0.5f;
        int total = 200;
        int shop = 5;
        double v = one * shop / (shop + 1);
        double v1 = total * v;
        System.out.print("一共需要：" + v1);


    }
}
