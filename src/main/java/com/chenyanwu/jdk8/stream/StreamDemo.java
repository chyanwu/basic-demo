package com.chenyanwu.jdk8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Auther: chenyanwu
 * @Date: 2019/9/24 09:41
 * @Description:
 * @Version 1.0
 */
public class StreamDemo {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        // 获取对应的平方数  map 将一个数据转换成另外一个数据
        List<Integer> squaresList = numbers.stream().map(i -> i*i).distinct().collect(Collectors.toList());

        System.out.println(squaresList.toString());
    }
}
