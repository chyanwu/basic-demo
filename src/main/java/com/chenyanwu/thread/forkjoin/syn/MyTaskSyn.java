package com.chenyanwu.thread.forkjoin.syn;

import java.util.concurrent.RecursiveTask;

/**
 * @Auther: chenyanwu
 * @Date: 2019/11/4 16:13
 * @Description:
 * @Version 1.0
 */
public class MyTaskSyn extends RecursiveTask<Integer> {
    private int[] src;
    private int left;
    private int right;

    public MyTaskSyn(int[] src, int left, int right) {
        this.src = src;
        this.left = left;
        this.right = right;
    }

    @Override
    protected Integer compute() {
        if(right - left < SynMain.INTERVAL) {
            int sum = 0;
            for(int i=left; i<=right; i++) {
                sum += src[i];
            }
            return sum;
        } else {
            int mid = (right + left)/2;
            MyTaskSyn leftTask = new MyTaskSyn(src, left, mid);
            MyTaskSyn rightTask = new MyTaskSyn(src, mid + 1, right);
            leftTask.fork();
            rightTask.fork();
//            invokeAll(leftTask, rightTask);

            return leftTask.join() + rightTask.join();
        }
    }
}
