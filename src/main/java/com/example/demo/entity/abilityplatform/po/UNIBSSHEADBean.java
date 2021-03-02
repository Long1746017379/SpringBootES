package com.example.demo.entity.abilityplatform.po;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DongChengLong
 * @desc
 * @date 2021/03/02 20:26
 */
@NoArgsConstructor
@Data
public class UNIBSSHEADBean {
    /**
     * RESP_DESC : 成功
     * TRANS_ID : 20210301165955842885732
     * TIMESTAMP : 2021-03-01 16:59:55 842
     * RESP_CODE : 00000
     * APP_ID : k4KYgtD3My
     */

    private String RESPDESC;
    private String TRANSID;
    private String TIMESTAMP;
    private String RESPCODE;
    private String APPID;
}
