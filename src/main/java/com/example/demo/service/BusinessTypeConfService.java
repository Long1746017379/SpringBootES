package com.example.demo.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author DongChengLong
 * @desc
 * @date 2021/01/14 11:09
 */
public interface BusinessTypeConfService {

    JSONArray queryBusinessTypeConfAll();

    JSONObject importExcel(MultipartFile file);

    JSONObject forEachInsertBusinessInfo();

    JSONObject batchInsertBusinessInfo();
}
