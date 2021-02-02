package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.BusinessTypeConfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author DongChengLong
 * @desc
 * @date 2021/01/13 20:47
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/navigation")
@CrossOrigin(origins = "*", maxAge = 3600)
public class BusinessTypeConfController {

    @Autowired
    BusinessTypeConfService businessTypeConfService;


    @GetMapping(value = "/queryCateg")
    public JSONArray query() {
        return businessTypeConfService.queryBusinessTypeConfAll();
    }


    @PostMapping(value = "/importExcel")
    public JSONObject importExcel(@RequestParam("file") MultipartFile file) {
        return businessTypeConfService.importExcel(file);
    }


    @GetMapping(value = "/forEachInsertBusinessInfo")
    public JSONObject forEachInsertBusinessInfo() {
        return businessTypeConfService.forEachInsertBusinessInfo();
    }


    @GetMapping(value = "/batchInsertBusinessInfo")
    public JSONObject batchInsertBusinessInfo() {
        return businessTypeConfService.batchInsertBusinessInfo();
    }

}
