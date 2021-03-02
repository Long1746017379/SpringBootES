package com.example.demo.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * business_type_conf
 * @author 
 */
@Data
@NoArgsConstructor
public class BusinessTypeConf implements Serializable {
    /**
     * 商家类型id
     */
    private Long businessTypeId;

    /**
     * 商家类型
     */
    private String businessType;

    private static final long serialVersionUID = 1L;
}