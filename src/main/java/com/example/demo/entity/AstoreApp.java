package com.example.demo.entity;

import java.io.Serializable;

/**
 * @author DongChengLong
 * @desc
 * @date 2021/02/04 17:18
 */
public class AstoreApp implements Serializable {
    private String astoreAppName;

    private int orderNum;

    private String orderNumFinished;

    private String orderNumNotFinished;

    private String avgDeliveryDay;

    private String province;

    private String name;

    public String getAstoreAppName() {
        return astoreAppName;
    }

    public void setAstoreAppName(String astoreAppName) {
        this.astoreAppName = astoreAppName;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderNumFinished() {
        return orderNumFinished;
    }

    public void setOrderNumFinished(String orderNumFinished) {
        this.orderNumFinished = orderNumFinished;
    }

    public String getOrderNumNotFinished() {
        return orderNumNotFinished;
    }

    public void setOrderNumNotFinished(String orderNumNotFinished) {
        this.orderNumNotFinished = orderNumNotFinished;
    }

    public String getAvgDeliveryDay() {
        return avgDeliveryDay;
    }

    public void setAvgDeliveryDay(String avgDeliveryDay) {
        this.avgDeliveryDay = avgDeliveryDay;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}