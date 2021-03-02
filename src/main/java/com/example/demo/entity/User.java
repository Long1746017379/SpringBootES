package com.example.demo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author DongChengLong
 * @desc
 * @date 2021/03/03 1:10
 */
@Data
@NoArgsConstructor
public class User implements Serializable {
    private static final long serialVersionUID = 4605139038211278516L;

    private String respDesc;

    private String TRANS_ID;

    private String TIMESTAMP;

    private String RESP_CODE;

    private String APP_ID;

}
