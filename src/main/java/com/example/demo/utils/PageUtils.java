package com.example.demo.utils;

import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DongChengLong
 * @desc 分页工具类
 * @date 2021/01/08 14:42
 */
@Slf4j
public class PageUtils {

    /**
     * 将要分页的List传入进行分页
     * @param responses 要分页的List
     * @param pageNum 当前页（从1开始）
     * @param pageSize 每页显示的数据
     * @param <T>
     * @return
     */
    public static <T> Page<T> getPageFromList(List<T> responses, int pageNum, int pageSize) {
        //分页控制
        Page<T> page = new Page<T>(pageNum, pageSize);
        int size = responses.size();
        if (size == 0 || pageNum < 1 || pageSize < 0) {
            return page;
        }
        //设置总大小
        page.setTotal(size);

        int fromIndex = (pageNum - 1) * pageSize;
        int toIndex = pageNum * pageSize;
        if (fromIndex >= size && toIndex >= size) {
            return page;
        } else if (fromIndex <= size && toIndex >= size){
            toIndex = size;
        }
        responses = responses.subList(fromIndex, toIndex);
        page.addAll(responses);
        return page;
    }

    public static void main(String[] args) {
        List<Object> list = new ArrayList<>();
        for (int i = 1; i <= 50000; i++){
            list.add(i);
        }

        int pageNum = 1000;
        int pageSize = 20;
        long start1 = System.currentTimeMillis();
        log.info("开始时间：{}", start1);
        Page pageFromList = getPageFromList(list, pageNum, pageSize);
        for (Object o : pageFromList) {
            log.info("第 {} 页，数据{}", pageNum, o);
        }
        long end1 = System.currentTimeMillis();
        log.info("结束时间：{}", end1);
        log.info("程序耗时：{} 毫秒", (end1-start1));
        log.info("程序耗时：{} 秒", (end1-start1) / 1000);

    }


}
