package com.example.demo.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * business_info
 * @author 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessInfo implements Serializable {
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
     * 商家类型id
     */
    private Long businessTypeId;

    /**
     * 省份地市id
     */
    private Long provinceCityId;

    /**
     * 商家电话
     */
    private String businessPhone;

    private String operationUser;

    private String operationTime;

    private static final long serialVersionUID = 1L;
}