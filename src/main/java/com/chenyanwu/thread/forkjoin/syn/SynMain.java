package com.chenyanwu.thread.forkjoin.syn;

import java.util.concurrent.ForkJoinPool;

/**
 * @Auther: chenyanwu
 * @Date: 2019/11/4 16:15
 * @Description:
 * @Version 1.0
 */
public class SynMain {

    public static final int INTERVAL = 5;


    public static void main(String[] args) {
        int[] arr = new int[12];
        for(int i=0; i<12; i++) {
            arr[i] = i+1;
        }
        int sum = 0;
        for(int i=0; i<arr.length; i++) {
            sum += arr[i];
        }
        System.out.println(sum);

        // 定义一个ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();
        MyTaskSyn myTaskSyn = new MyTaskSyn(arr, 0, arr.length - 1);
        int result = pool.invoke(myTaskSyn);

        System.out.println(result);
    }
}
