package com.example.demo.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author DongChengLong
 * @desc
 * @date 2020/12/14 13:22
 */
@Data
public class BusinessInfoEs implements Serializable {

    /**
     * 商家id
     */
    private String businessId;

    /**
     * 商家原id
     */
    private String businessOriginalId;

    /**
     * 商户地址
     */
    private String businessAddress;

    /**
     * 商户名称
     */
    private String businessName;

    /**
     * 商户邮编
     */
    private String businessCode;

    /**
     * 商户状态
     */
    private String businessStatus;

    /**
     * 商户邮箱
     */
    private String businessEmail;

    /**
     * 商户网址
     */
    private String businessUrl;

    /**
     * 电话是否加密
     */
    private String businessIssafety;

    /**
     * 增值业务标识
     */
    private String businessZzLogo;

    /**
     * 增值类型
     */
    private String businessZzType;

    /**
     * 单位名称编码
     */
    private String businessUnitCode;

    /**
     * 商家头像url
     */
    private String businessHeadUrl;

    /**
     * 商家背景url
     */
    private String businessBgUrl;

    /**
     * 商家经纬度
     */
    private String businessLngLat;

    /**
     * 商家电话
     */
    private String businessPhone;


    /**
     * 商家类型
     */
    private String businessType;

    /**
     * 省节点编码
     */
    private Integer provinceCode;

    /**
     * 省名称
     */
    private String provinceName;

    /**
     * 市节点编码
     */
    private Integer cityCode;

    /**
     * 市名称
     */
    private String cityName;

    /**
     * 区/县编码
     */
    private Integer countyCode;

    /**
     * 区/县名称
     */
    private String countyName;

    /**
     * 商家类别编码
     */
    private Integer businessTypeId;

    private static final long serialVersionUID = 1L;

}
