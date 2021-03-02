package com.example.demo.entity.abilityplatform.bo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DongChengLong
 * @desc 能力平台接口请求实体
 * @date 2021/03/02 20:20
 */
@NoArgsConstructor
@Data
public class AbilityPlatformBO {

    /**
     * UNI_BSS_ATTACHED : {"MEDIA_INFO":""}
     * UNI_BSS_BODY : {"NUMBER_ADD_ROLE_REQ":{"ROOT":{"REQ_BODY":{"EPARCHY_CODE":"110","ROLE_ID":"2199001","PROVINCE_CODE":"11","DEVELOP_STAFF_ID":"1105195884","MSTAFF_ID":"18618473899"}}}}
     * UNI_BSS_HEAD : {"APP_ID":"k4KYgtD3My","TIMESTAMP":"2021-03-01 16:59:55 842","TRANS_ID":"20210301165955842885732","TOKEN":"7d6be863774d07b60290fd897ea3e19e"}
     */

    private UNIBSSATTACHEDBean UNIBSSATTACHED;
    private UNIBSSBODYBean UNIBSSBODY;
    private UNIBSSHEADBean UNIBSSHEAD;
}
