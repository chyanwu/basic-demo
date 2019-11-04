package com.chenyanwu.jdk8.asyn;

import cn.hutool.core.util.IdUtil;
import com.chenyanwu.jdk8.asyn.vo.BatchTaskVO;
import com.chenyanwu.jdk8.asyn.vo.ResultVO;
import com.chenyanwu.jdk8.asyn.vo.User;
import sun.reflect.generics.tree.VoidDescriptor;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Auther: chenyanwu
 * @Date: 2019/9/24 10:04
 * @Description:
 * @Version 1.0
 */
public class CompletableFutureDemo {

    private static final Executor executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 1、 初始化数据
        List<User> users = initUserData();
        for(User user: users) {
            System.out.println(user);
        }
        System.out.println("=========================1 end=================");
        // 2、 按年级进行分组
        Map<Integer, List<User>> groupByGrade = users.stream().collect(Collectors.groupingBy(User::getGrade));
        List<BatchTaskVO> allJob = new ArrayList<>();
        for(Map.Entry<Integer, List<User>> entry: groupByGrade.entrySet()) {
            BatchTaskVO taskVO = new BatchTaskVO();
            taskVO.setUserList(entry.getValue());
            taskVO.setGrade(entry.getKey());
            taskVO.setBatchno(IdUtil.simpleUUID());
            allJob.add(taskVO);
        }
        System.out.println("=========================2 end=================");
        // 3、 创建ComletableFuture对象，并进行业务处理
        List<CompletableFuture<ResultVO>> allMission = allJob.stream()
                .map(taskV0 -> subMission(taskV0))  // 此处将当前的数据进行处理，并转换未CompletableFuture来存储
                .collect(Collectors.toList());

        System.out.println("=========================2 end=================");

        /**
         * allOf是等待所有任务完成，构造后CompletableFuture完成
         * anyOf是只要有一个任务完成，构造后CompletableFuture就完成
         */
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                allMission.toArray(new CompletableFuture[allMission.size()])
        );

        // 4、使用thenApplay来完成值得转化
        CompletableFuture<List<ResultVO>> allResult = allFutures.thenApply(v -> {
            System.out.println("============thenApplay=====================");
            return allMission.stream()
                    .map(pageContentFuture -> pageContentFuture.join())  // join获取结果
                    .collect(Collectors.toList());
        });

        List<ResultVO> result = allResult.get();
        for(ResultVO rv: result) {
            System.out.println(rv.getResult());
        }
        allResult.whenComplete((s, e) -> {
            System.out.println("任务"+s+"完成!result="+s+"，异常 e="+e+","+new Date());
        });
    }

    /*
        创建CompletableFuture
        Asynsc表示异步,而supplyAsync与runAsync不同在与前者异步返回一个结果,后者是void
     */
    private static CompletableFuture<ResultVO> subMission(BatchTaskVO taskV0) {
        CompletableFuture<ResultVO> future = CompletableFuture.supplyAsync(new Supplier<ResultVO>() {
            @Override
            public ResultVO get() {
                ResultVO resultVO = new ResultVO();
                List<User> users = taskV0.getUserList();
                for(User user: users) {
                    System.out.println("==============subMission start===========");
                    System.out.println(user);
                    System.out.println("==============subMission end===========");
                }
                resultVO.setResult(taskV0.getGrade() + " success!");
                return resultVO;
            }
        }, executor).exceptionally(ex -> {
            ResultVO resultVO = new ResultVO();
            resultVO.setResult(taskV0.getGrade() + " failed!");
            return resultVO;
        });
        return future;
    }

    public static List<User> initUserData() {
        List<User> users = new ArrayList<>();
        Random random = new Random();
        for(int i=0; i<100; i++) {
            User user = new User();
            user.setId(i);
            user.setName("Jack" + i);
            user.setAge(random.nextInt(12));
            user.setGrade((i % 6) + 1);
            users.add(user);
        }
        return users;
    }
}
