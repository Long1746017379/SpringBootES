package com.example.demo.test;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.BusinessTypeConf;
import com.example.demo.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DongChengLong
 * @desc
 * @date 2020/12/04 14:25
 */
@Slf4j
public class Test {
    public static void main(String[] args) {

//        List<BusinessTypeConf> list = new ArrayList<>();
//
//        BusinessTypeConf businessTypeConf = new BusinessTypeConf();
//        businessTypeConf.setBusinessTypeId(1001L);
//        businessTypeConf.setBusinessType("小吃");
//
//        list.add(businessTypeConf);
//        log.info("List.size()：{}", list.size());

        int q = 6000 % 10000;
        System.out.println(q);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mobile", "15830755059");
        System.out.println(jsonObject);

        log.debug("jsonObject:{}", jsonObject);
        log.info("jsonObject:{}", jsonObject);
        log.warn("jsonObject:{}", jsonObject);
        log.error("jsonObject:{}", jsonObject);
        System.out.println(jsonObject.size());

    }

    private static int test() {
        int i = 1;
        try {
            i = 100;
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            i = 5;
            log.info("finally中i的值为：{}", i);
        }
        return i;
    }

}
