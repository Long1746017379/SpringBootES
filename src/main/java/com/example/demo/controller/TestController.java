package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.AstoreApp;
import com.example.demo.entity.OrderNumAndDuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author DongChengLong
 * @desc
 * @date 2021/02/04 17:53
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/navigation")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TestController {

    @PostMapping(value = "/testTwoHeader")
    public Map testTwoHeader(@RequestBody JSONObject jsonObject){

        String postJson = "{\"respCode\":\"0000\",\"respDesc\":\"请求成功\",\"list\":[{\"astoreAppName\":null,\"orderNum\":35,\"orderNumFinished\":35,\"orderNumNotFinished\":0,\"avgDeliveryDay\":\"0.06\",\"province\":null,\"name\":\"客户运营平台\"},{\"astoreAppName\":null,\"orderNum\":5,\"orderNumFinished\":1,\"orderNumNotFinished\":4,\"avgDeliveryDay\":\"2.08\",\"province\":null,\"name\":\"掌沃通\"},{\"astoreAppName\":null,\"orderNum\":1,\"orderNumFinished\":0,\"orderNumNotFinished\":1,\"avgDeliveryDay\":null,\"province\":null,\"name\":\"cBSS营业前台\"}],\"listMap\":null}";

        ObjectMapper objectMapper = new ObjectMapper();
        List<AstoreApp> list = jsonStringTOList(postJson, objectMapper);

        Map<String, Object> moreFlowApp = count(jsonObject.getString("name"), list);

        return moreFlowApp;
    }


    @GetMapping(value = "/testProvinceAndSub")
    public JSONArray query() {
        JSONArray jsonArray = new JSONArray();

        String postJson = "{\"respCode\":\"0000\",\"respDesc\":\"请求成功\",\"list\":[{\"astoreAppName\":null,\"orderNum\":44,\"orderNumFinished\":43,\"orderNumNotFinished\":1,\"avgDeliveryDay\":\"0.05\",\"province\":null,\"name\":\"海南省分公司\"},{\"astoreAppName\":null,\"orderNum\":39,\"orderNumFinished\":39,\"orderNumNotFinished\":0,\"avgDeliveryDay\":\"0.69\",\"province\":null,\"name\":\"江西省分公司\"},{\"astoreAppName\":null,\"orderNum\":37,\"orderNumFinished\":32,\"orderNumNotFinished\":5,\"avgDeliveryDay\":\"0.23\",\"province\":null,\"name\":\"云南省分公司\"},{\"astoreAppName\":null,\"orderNum\":37,\"orderNumFinished\":37,\"orderNumNotFinished\":0,\"avgDeliveryDay\":\"0.92\",\"province\":null,\"name\":\"江苏省分公司\"},{\"astoreAppName\":null,\"orderNum\":34,\"orderNumFinished\":30,\"orderNumNotFinished\":4,\"avgDeliveryDay\":\"0.08\",\"province\":null,\"name\":\"广西壮族自治区分公司\"},{\"astoreAppName\":null,\"orderNum\":26,\"orderNumFinished\":24,\"orderNumNotFinished\":2,\"avgDeliveryDay\":\"1.63\",\"province\":null,\"name\":\"四川省分公司\"},{\"astoreAppName\":null,\"orderNum\":24,\"orderNumFinished\":23,\"orderNumNotFinished\":1,\"avgDeliveryDay\":\"1.16\",\"province\":null,\"name\":\"山东省分公司\"},{\"astoreAppName\":null,\"orderNum\":24,\"orderNumFinished\":22,\"orderNumNotFinished\":2,\"avgDeliveryDay\":\"0.00\",\"province\":null,\"name\":\"安徽省分公司\"},{\"astoreAppName\":null,\"orderNum\":18,\"orderNumFinished\":10,\"orderNumNotFinished\":11,\"avgDeliveryDay\":\"2.11\",\"province\":null,\"name\":\"上海市分公司\"},{\"astoreAppName\":null,\"orderNum\":17,\"orderNumFinished\":16,\"orderNumNotFinished\":1,\"avgDeliveryDay\":\"11.18\",\"province\":null,\"name\":\"河南省分公司\"},{\"astoreAppName\":null,\"orderNum\":16,\"orderNumFinished\":12,\"orderNumNotFinished\":4,\"avgDeliveryDay\":\"0.08\",\"province\":null,\"name\":\"内蒙古自治区分公司\"},{\"astoreAppName\":null,\"orderNum\":15,\"orderNumFinished\":11,\"orderNumNotFinished\":5,\"avgDeliveryDay\":\"1.95\",\"province\":null,\"name\":\"湖南省分公司\"},{\"astoreAppName\":null,\"orderNum\":15,\"orderNumFinished\":14,\"orderNumNotFinished\":1,\"avgDeliveryDay\":\"2.33\",\"province\":null,\"name\":\"河北省分公司\"},{\"astoreAppName\":null,\"orderNum\":15,\"orderNumFinished\":12,\"orderNumNotFinished\":4,\"avgDeliveryDay\":\"1.54\",\"province\":null,\"name\":\"北京市分公司\"},{\"astoreAppName\":null,\"orderNum\":14,\"orderNumFinished\":8,\"orderNumNotFinished\":6,\"avgDeliveryDay\":\"0.00\",\"province\":null,\"name\":\"广东省分公司\"},{\"astoreAppName\":null,\"orderNum\":13,\"orderNumFinished\":11,\"orderNumNotFinished\":2,\"avgDeliveryDay\":\"5.39\",\"province\":null,\"name\":\"吉林省分公司\"},{\"astoreAppName\":null,\"orderNum\":12,\"orderNumFinished\":9,\"orderNumNotFinished\":3,\"avgDeliveryDay\":\"3.84\",\"province\":null,\"name\":\"贵州省分公司\"},{\"astoreAppName\":null,\"orderNum\":11,\"orderNumFinished\":10,\"orderNumNotFinished\":1,\"avgDeliveryDay\":\"0.14\",\"province\":null,\"name\":\"甘肃省分公司\"},{\"astoreAppName\":null,\"orderNum\":11,\"orderNumFinished\":11,\"orderNumNotFinished\":0,\"avgDeliveryDay\":\"0.72\",\"province\":null,\"name\":\"浙江省分公司\"},{\"astoreAppName\":null,\"orderNum\":10,\"orderNumFinished\":7,\"orderNumNotFinished\":3,\"avgDeliveryDay\":\"2.55\",\"province\":null,\"name\":\"重庆市分公司\"},{\"astoreAppName\":null,\"orderNum\":10,\"orderNumFinished\":9,\"orderNumNotFinished\":1,\"avgDeliveryDay\":\"4.72\",\"province\":null,\"name\":\"湖北省分公司\"},{\"astoreAppName\":null,\"orderNum\":9,\"orderNumFinished\":9,\"orderNumNotFinished\":0,\"avgDeliveryDay\":\"1.02\",\"province\":null,\"name\":\"天津市分公司\"},{\"astoreAppName\":null,\"orderNum\":9,\"orderNumFinished\":7,\"orderNumNotFinished\":2,\"avgDeliveryDay\":\"1.46\",\"province\":null,\"name\":\"青海省分公司\"},{\"astoreAppName\":null,\"orderNum\":9,\"orderNumFinished\":9,\"orderNumNotFinished\":0,\"avgDeliveryDay\":\"0.80\",\"province\":null,\"name\":\"陕西省分公司\"},{\"astoreAppName\":null,\"orderNum\":7,\"orderNumFinished\":6,\"orderNumNotFinished\":1,\"avgDeliveryDay\":\"3.77\",\"province\":null,\"name\":\"宁夏回族自治区分公司\"},{\"astoreAppName\":null,\"orderNum\":7,\"orderNumFinished\":5,\"orderNumNotFinished\":2,\"avgDeliveryDay\":\"0.26\",\"province\":null,\"name\":\"辽宁省分公司\"},{\"astoreAppName\":null,\"orderNum\":6,\"orderNumFinished\":4,\"orderNumNotFinished\":2,\"avgDeliveryDay\":\"0.50\",\"province\":null,\"name\":\"福建省分公司\"},{\"astoreAppName\":null,\"orderNum\":4,\"orderNumFinished\":2,\"orderNumNotFinished\":2,\"avgDeliveryDay\":\"4.59\",\"province\":null,\"name\":\"新疆维吾尔自治区分公司\"},{\"astoreAppName\":null,\"orderNum\":3,\"orderNumFinished\":3,\"orderNumNotFinished\":0,\"avgDeliveryDay\":\"0.17\",\"province\":null,\"name\":\"山西省分公司\"},{\"astoreAppName\":null,\"orderNum\":1,\"orderNumFinished\":0,\"orderNumNotFinished\":1,\"avgDeliveryDay\":null,\"province\":null,\"name\":\"黑龙江省分公司\"}],\"listMap\":null}";
        ObjectMapper objectMapper = new ObjectMapper();
        Map rspMap = setOrderNumAndDurationRspMap(postJson, objectMapper);

        disposeRspmap(rspMap, jsonArray);

        return jsonArray;
    }


    private Map<String, Object> count(String name, List<AstoreApp> list) {
        Map<String, Object> dataMap = new HashMap<>();
        int count = 0;
        int orderNum = 0;
        BigDecimal bigDecimal = new BigDecimal(0.0);
        for (AstoreApp app : list) {
            if (name.equals(app.getName())) {
                if (null != app.getAvgDeliveryDay()) {
                    count = count + 1;
                    orderNum += app.getOrderNum();
                    bigDecimal = bigDecimal.add(new BigDecimal(app.getAvgDeliveryDay()));
                } else {
                    count = count + 1;
                    orderNum += app.getOrderNum();
                }
            }
        }

        BigDecimal bigDecimalZero = new BigDecimal(0.0);
        // 初始化 avgDay 的值 divide 默认为0.0(平均交付时长)
        BigDecimal divide = null;
        /**
         * a.compareTo(b)
         * 值为:0---a等于b
         *      1---a大于b
         *     -1---a小于b
         */
        // TODO bigDecimal累加后的结果跟 bigDecimalZero 0.0 做判断
        if (bigDecimal.compareTo(bigDecimalZero) >= 1 && count > 0) {
            divide = bigDecimal.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP);
            dataMap.put("avgDay", String.valueOf(divide));
        } else {
            dataMap.put("avgDay", divide);
        }
        dataMap.put("ordeNum", orderNum);
        return dataMap;
    }


    private void disposeRspmap(Map<String, OrderNumAndDuration> rspMap, JSONArray jsonArray) {
        List<Map<String, Object>> bizCountList = new ArrayList<>();
        List<Map<String, Object>> timeoutCountList = new ArrayList<>();

        // 地图调用，需要返回地图的格式数据
        Map<String, List> map = new HashMap();

        if (!rspMap.isEmpty()) {
            Boolean flag = false;
            for (Map.Entry<String, OrderNumAndDuration> entry : rspMap.entrySet()) {
                flag = true;
                Map<String, Object> bizCountMap = new HashMap<>();
                Map<String, Object> timeoutCountMap = new HashMap<>();
                if (null == entry.getValue().getAvgDeliveryDay()) {
                    bizCountMap.put("avgDeliveryDay", null);
                    bizCountMap.put("orderNum", entry.getValue().getOrderNum());
                    bizCountMap.put("name", entry.getKey());
                    timeoutCountMap.put("avgDeliveryDay", null);
                    timeoutCountMap.put("orderNum", entry.getValue().getOrderNum());
                    timeoutCountMap.put("name", entry.getKey());
                } else {
                    bizCountMap.put("orderNum", entry.getValue().getOrderNum());
                    bizCountMap.put("avgDeliveryDay", entry.getValue().getAvgDeliveryDay());
                    bizCountMap.put("name", entry.getKey());
                    timeoutCountMap.put("orderNum", entry.getValue().getOrderNum());
                    timeoutCountMap.put("avgDeliveryDay", entry.getValue().getAvgDeliveryDay());
                    timeoutCountMap.put("name", entry.getKey());
                }
                bizCountList.add(bizCountMap);
                timeoutCountList.add(timeoutCountMap);
            }
        }

        map.put("bizCountList", bizCountList);
        map.put("timeoutCountList", timeoutCountList);
        jsonArray.add(map);
    }


    private List<AstoreApp> jsonStringTOList(String postJson, ObjectMapper objectMapper) {
        List<AstoreApp> list = new ArrayList<>();
        try {
            Map<String, Object> jsonMap = objectMapper.readValue(postJson, Map.class);
            CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, AstoreApp.class);
            list = objectMapper.readValue(objectMapper.writeValueAsBytes(jsonMap.get("list")), listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    private Map setOrderNumAndDurationRspMap(String postJson, ObjectMapper objectMapper) {
        Map<String, OrderNumAndDuration> rspMap = new HashMap<>();
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
        return rspMap;
    }


}
