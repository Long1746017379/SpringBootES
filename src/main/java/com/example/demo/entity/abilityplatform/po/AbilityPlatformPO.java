package com.example.demo.entity.abilityplatform.po;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DongChengLong
 * @desc 能力平台接口响应实体
 * @date 2021/03/02 20:25
 */
@NoArgsConstructor
@Data
public class AbilityPlatformPO {

    /**
     * UNI_BSS_HEAD : {"RESP_DESC":"成功","TRANS_ID":"20210301165955842885732","TIMESTAMP":"2021-03-01 16:59:55 842","RESP_CODE":"00000","APP_ID":"k4KYgtD3My"}
     * UNI_BSS_ATTACHED : {"MEDIA_INFO":""}
     * UNI_BSS_BODY : {"NUMBER_ADD_ROLE_RSP":{"ROOT":{"RSP_CODE":"1","RSP_DESC":"未查到该条数据！"}}}
     */

    private UNIBSSHEADBean UNIBSSHEAD;
    private UNIBSSATTACHEDBean UNIBSSATTACHED;
    private UNIBSSBODYBean UNIBSSBODY;
}
