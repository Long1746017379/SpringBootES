package com.example.demo.test;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.AstoreApp;
import com.example.demo.entity.BusinessTypeConf;
import com.example.demo.entity.OrderNumAndDuration;
import com.example.demo.entity.User;
import com.example.demo.entity.abilityplatform.bo.AbilityPlatformBO;
import com.example.demo.entity.abilityplatform.bo.UNIBSSATTACHEDBean;
import com.example.demo.entity.abilityplatform.bo.UNIBSSHEADBean;
import com.example.demo.utils.HttpClientUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.demo.utils.oss.AliyunOSSUtil.RSP_CODE_SUCCESS;

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

//        int q = 6000 % 10000;
//        System.out.println(q);
//
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("mobile", "15830755059");
//        System.out.println(jsonObject);
//
//        log.debug("jsonObject:{}", jsonObject);
//        log.info("jsonObject:{}", jsonObject);
//        log.warn("jsonObject:{}", jsonObject);
//        log.error("jsonObject:{}", jsonObject);
//        System.out.println(jsonObject.size());

//        String postJson = "{\"respCode\":\"0000\",\"respDesc\":\"请求成功\",\"list\":[{\"astoreAppName\":null,\"orderNum\":44,\"orderNumFinished\":43,\"orderNumNotFinished\":1,\"avgDeliveryDay\":\"0.05\",\"province\":null,\"name\":\"海南省分公司\"},{\"astoreAppName\":null,\"orderNum\":39,\"orderNumFinished\":39,\"orderNumNotFinished\":0,\"avgDeliveryDay\":\"0.69\",\"province\":null,\"name\":\"江西省分公司\"},{\"astoreAppName\":null,\"orderNum\":37,\"orderNumFinished\":32,\"orderNumNotFinished\":5,\"avgDeliveryDay\":\"0.23\",\"province\":null,\"name\":\"云南省分公司\"},{\"astoreAppName\":null,\"orderNum\":37,\"orderNumFinished\":37,\"orderNumNotFinished\":0,\"avgDeliveryDay\":\"0.92\",\"province\":null,\"name\":\"江苏省分公司\"},{\"astoreAppName\":null,\"orderNum\":34,\"orderNumFinished\":30,\"orderNumNotFinished\":4,\"avgDeliveryDay\":\"0.08\",\"province\":null,\"name\":\"广西壮族自治区分公司\"},{\"astoreAppName\":null,\"orderNum\":26,\"orderNumFinished\":24,\"orderNumNotFinished\":2,\"avgDeliveryDay\":\"1.63\",\"province\":null,\"name\":\"四川省分公司\"},{\"astoreAppName\":null,\"orderNum\":24,\"orderNumFinished\":23,\"orderNumNotFinished\":1,\"avgDeliveryDay\":\"1.16\",\"province\":null,\"name\":\"山东省分公司\"},{\"astoreAppName\":null,\"orderNum\":24,\"orderNumFinished\":22,\"orderNumNotFinished\":2,\"avgDeliveryDay\":\"0.00\",\"province\":null,\"name\":\"安徽省分公司\"},{\"astoreAppName\":null,\"orderNum\":18,\"orderNumFinished\":10,\"orderNumNotFinished\":11,\"avgDeliveryDay\":\"2.11\",\"province\":null,\"name\":\"上海市分公司\"},{\"astoreAppName\":null,\"orderNum\":17,\"orderNumFinished\":16,\"orderNumNotFinished\":1,\"avgDeliveryDay\":\"11.18\",\"province\":null,\"name\":\"河南省分公司\"},{\"astoreAppName\":null,\"orderNum\":16,\"orderNumFinished\":12,\"orderNumNotFinished\":4,\"avgDeliveryDay\":\"0.08\",\"province\":null,\"name\":\"内蒙古自治区分公司\"},{\"astoreAppName\":null,\"orderNum\":15,\"orderNumFinished\":11,\"orderNumNotFinished\":5,\"avgDeliveryDay\":\"1.95\",\"province\":null,\"name\":\"湖南省分公司\"},{\"astoreAppName\":null,\"orderNum\":15,\"orderNumFinished\":14,\"orderNumNotFinished\":1,\"avgDeliveryDay\":\"2.33\",\"province\":null,\"name\":\"河北省分公司\"},{\"astoreAppName\":null,\"orderNum\":15,\"orderNumFinished\":12,\"orderNumNotFinished\":4,\"avgDeliveryDay\":\"1.54\",\"province\":null,\"name\":\"北京市分公司\"},{\"astoreAppName\":null,\"orderNum\":14,\"orderNumFinished\":8,\"orderNumNotFinished\":6,\"avgDeliveryDay\":\"0.00\",\"province\":null,\"name\":\"广东省分公司\"},{\"astoreAppName\":null,\"orderNum\":13,\"orderNumFinished\":11,\"orderNumNotFinished\":2,\"avgDeliveryDay\":\"5.39\",\"province\":null,\"name\":\"吉林省分公司\"},{\"astoreAppName\":null,\"orderNum\":12,\"orderNumFinished\":9,\"orderNumNotFinished\":3,\"avgDeliveryDay\":\"3.84\",\"province\":null,\"name\":\"贵州省分公司\"},{\"astoreAppName\":null,\"orderNum\":11,\"orderNumFinished\":10,\"orderNumNotFinished\":1,\"avgDeliveryDay\":\"0.14\",\"province\":null,\"name\":\"甘肃省分公司\"},{\"astoreAppName\":null,\"orderNum\":11,\"orderNumFinished\":11,\"orderNumNotFinished\":0,\"avgDeliveryDay\":\"0.72\",\"province\":null,\"name\":\"浙江省分公司\"},{\"astoreAppName\":null,\"orderNum\":10,\"orderNumFinished\":7,\"orderNumNotFinished\":3,\"avgDeliveryDay\":\"2.55\",\"province\":null,\"name\":\"重庆市分公司\"},{\"astoreAppName\":null,\"orderNum\":10,\"orderNumFinished\":9,\"orderNumNotFinished\":1,\"avgDeliveryDay\":\"4.72\",\"province\":null,\"name\":\"湖北省分公司\"},{\"astoreAppName\":null,\"orderNum\":9,\"orderNumFinished\":9,\"orderNumNotFinished\":0,\"avgDeliveryDay\":\"1.02\",\"province\":null,\"name\":\"天津市分公司\"},{\"astoreAppName\":null,\"orderNum\":9,\"orderNumFinished\":7,\"orderNumNotFinished\":2,\"avgDeliveryDay\":\"1.46\",\"province\":null,\"name\":\"青海省分公司\"},{\"astoreAppName\":null,\"orderNum\":9,\"orderNumFinished\":9,\"orderNumNotFinished\":0,\"avgDeliveryDay\":\"0.80\",\"province\":null,\"name\":\"陕西省分公司\"},{\"astoreAppName\":null,\"orderNum\":7,\"orderNumFinished\":6,\"orderNumNotFinished\":1,\"avgDeliveryDay\":\"3.77\",\"province\":null,\"name\":\"宁夏回族自治区分公司\"},{\"astoreAppName\":null,\"orderNum\":7,\"orderNumFinished\":5,\"orderNumNotFinished\":2,\"avgDeliveryDay\":\"0.26\",\"province\":null,\"name\":\"辽宁省分公司\"},{\"astoreAppName\":null,\"orderNum\":6,\"orderNumFinished\":4,\"orderNumNotFinished\":2,\"avgDeliveryDay\":\"0.50\",\"province\":null,\"name\":\"福建省分公司\"},{\"astoreAppName\":null,\"orderNum\":4,\"orderNumFinished\":2,\"orderNumNotFinished\":2,\"avgDeliveryDay\":\"4.59\",\"province\":null,\"name\":\"新疆维吾尔自治区分公司\"},{\"astoreAppName\":null,\"orderNum\":3,\"orderNumFinished\":3,\"orderNumNotFinished\":0,\"avgDeliveryDay\":\"0.17\",\"province\":null,\"name\":\"山西省分公司\"},{\"astoreAppName\":null,\"orderNum\":1,\"orderNumFinished\":0,\"orderNumNotFinished\":1,\"avgDeliveryDay\":null,\"province\":null,\"name\":\"黑龙江省分公司\"}],\"listMap\":null}";
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, OrderNumAndDuration> rspMap = new HashMap<>();
//
//        setOrderNumAndDurationRspMap(rspMap, postJson, objectMapper);
//        disposeRspmap(rspMap);


//        Map<String, Object> map = new HashMap<>();
//        if (!map.containsKey("city")){
//            map.put("ctiy", "001");
//        }
//        log.info("{}",map);
//
//        map.put("avgDay", "123");
//        String avgDay = map.get("avgDay").toString();
//        log.info("avgDay：{}", avgDay);

        String str = "12我爱中国哈哈哈EM345";
        double length = getLength(str);
        log.info("{}", length);
        log.info("{}", str.length());
        if (length > 10){
            log.info("姓名超出长度", str);
        }


        BusinessTypeConf businessTypeConf = new BusinessTypeConf();
        businessTypeConf.setBusinessTypeId(1001L);
        businessTypeConf.setBusinessType("小吃");
        ObjectMapper objectMapper = new ObjectMapper();
        String stringBusiness = "{\"businessTypeId\":1001,\"businessType\":\"小吃\"}";

        String ss = "{\"respDesc\":111,\"transId\":\"222\",\"timesTamp\":\"333\",\"respCode\":\"444\",\"appId\":\"555\"}";

        try {
//            String string = objectMapper.writeValueAsString(businessTypeConf);
//            log.info("{}", string);

            BusinessTypeConf b = objectMapper.readValue(stringBusiness, BusinessTypeConf.class);
            log.info("{}", b);

            User user = objectMapper.readValue(ss, User.class);
            log.info("user：{}", user);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取字符串字符（中文两个字符）
     * @param s
     * @return
     */
    public static double getLength(String s) {
        double valueLength = 0;
        String chinese = "^[\u4e00-\u9fa5],{0,}$";
        for (int i = 0; i < s.length(); i++) {
            // 获取一个字符
            String temp = s.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                // 中文字符长度为1
                valueLength += 1;
            } else {
                // 其他字符长度为0.5
                valueLength += 0.5;
            }
        }
        // 进位取整
        return valueLength;
    }

    private static void disposeRspmap(Map<String, OrderNumAndDuration> rspMap) {

    }

    private static void setOrderNumAndDurationRspMap(Map<String, OrderNumAndDuration> rspMap, String postJson, ObjectMapper objectMapper) {
        try {
            Map<String, Object> jsonMap = objectMapper.readValue(postJson, Map.class);
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, AstoreApp.class);
            List<AstoreApp> list = objectMapper.readValue(objectMapper.writeValueAsBytes(jsonMap.get("list")), listType);
            for (AstoreApp v : list) {
                if (v.getName() != null) {
                    OrderNumAndDuration orderNumAndDuration = new OrderNumAndDuration();
//                    if (null == v.getAvgDeliveryDay()) {
//                        orderNumAndDuration.setOrderNum(v.getOrderNum());
//                        orderNumAndDuration.setAvgDeliveryDay("null");
//                    }else {
//                        orderNumAndDuration.setOrderNum(v.getOrderNum());
//                        orderNumAndDuration.setAvgDeliveryDay(v.getAvgDeliveryDay());
//                    }

                    orderNumAndDuration.setOrderNum(v.getOrderNum());
                    orderNumAndDuration.setAvgDeliveryDay(v.getAvgDeliveryDay());
                    rspMap.put(v.getName(), orderNumAndDuration);
                }
            }
            log.info("rspMap:{}", JSONObject.toJSONString(rspMap));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
