package com.example.demo.entity.abilityplatform.po;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * t_install_person_sync_abilityplatform_fail
 * 智家工程师同步能力平台失败表
 *
 * @author
 */
@Data
public class TInstallPersonSyncAbilityplatformFail implements Serializable {

    private Long installPersonId;

    /**
     * 智家工程师账号
     */
    private String account;

    /**
     * 智家工程师手机号
     */
    private String phone;

    /**
     * 智家工程师姓名
     */
    private String name;

    /**
     * 省份编码
     */
    private String provinceCode;

    /**
     * 地市编码
     */
    private String eparchyCode;

    /**
     * 区县编码
     */
    private String districtCode;

    /**
     * cbss账号
     */
    private String cbssAccount;

    /**
     * 发展人姓名
     */
    private String developName;

    /**
     * 发展人编码
     */
    private String developId;

    /**
     * 渠道编码
     */
    private String sourceChannelCode;

    /**
     * 渠道名称
     */
    private String sourceChannelName;

    /**
     * 营服中心
     */
    private String salemanServiceCenter;

    /**
     * 0: 失效
     * 1: 生效
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 角色编码（可以传个默认值 2199001-智家工程师角色）
     */
    private String roleId;

    /**
     * 能力平台发起调用时间
     */
    private String timestamp;

    /**
     * 能力平台发起调用时间流水
     */
    private String transid;

    /**
     * 能力平台发起调用token认证
     */
    private String token;

    /**
     * 同步时间
     */
    private Date syncTime;

    /**
     * 同步状态：
     * 0 成功  1失败
     */
    private Integer syncStatus;

    /**
     * 备注
     */
    private String note;

    private static final long serialVersionUID = 1L;
}