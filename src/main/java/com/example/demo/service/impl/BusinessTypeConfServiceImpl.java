package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.BusinessInfo;
import com.example.demo.entity.BusinessTypeConf;
import com.example.demo.mapper.BusinessInfoDao;
import com.example.demo.mapper.BusinessTypeConfMapper;
import com.example.demo.service.BusinessTypeConfService;
import com.example.demo.utils.PageUtils;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author DongChengLong
 * @desc
 * @date 2021/01/14 11:09
 */
@Slf4j
@Service
public class BusinessTypeConfServiceImpl implements BusinessTypeConfService {

    @Autowired
    BusinessTypeConfMapper businessTypeConfMapper;

    @Autowired
    BusinessInfoDao businessInfoDao;

    @Autowired
    SqlSessionFactory sqlSessionFactory;


    /**
     * 查询商家类别信息
     *
     * @return
     */
    @Override
    public JSONArray queryBusinessTypeConfAll() {
        log.info("-----从MySQL查询商家类别信息-----");
        JSONArray jsonArray = new JSONArray();
        List<BusinessTypeConf> businessTypeConfs = businessTypeConfMapper.queryAll();

        for (BusinessTypeConf businessTypeConf : businessTypeConfs) {
            jsonArray.add(businessTypeConf);
        }

        return jsonArray;
    }

    @Override
    public JSONObject importExcel(MultipartFile file) {
        log.info("-----开始导入Excel数据-----");
        JSONObject jsonObject = new JSONObject();

        try {
            if (!file.isEmpty()) {
                // 文件的真实名称（带有后缀）
                String realFilename = file.getOriginalFilename();

                // 获取文件格式后缀名
                String prefix = file.getOriginalFilename().substring(realFilename.indexOf(".") + 1);

                Workbook workbook = null;

                if ("xls".equals(prefix) || "xlsx".equals(prefix)) {
                    if ("xls".equals(prefix)) {
                        // TODO 走 xls 03 解析
                        log.info("----------走excel 2003的解析----------");
                        workbook = new HSSFWorkbook(file.getInputStream());
                    }

                    if ("xlsx".equals(prefix)) {
                        // TODO 走 xlsx 07 解析
                        log.info("----------走excel 2007的解析----------");
                        workbook = new XSSFWorkbook(file.getInputStream());
                    }

                    // 获取到共有多少个工作表
                    int numberOfSheets = workbook.getNumberOfSheets();

                    /**
                     * 获取excel 工作表的两种方式
                     * 获取到第二个工作表（注意getSheetAt() 从0开始 0代表第一个sheet工作表）
                     * 1.getSheetAt(int i)
                     * 2.getSheet(String name)
                     */

                    Sheet sheet = workbook.getSheetAt(numberOfSheets - 1);
                    Sheet businessType = workbook.getSheet("商家行业大类");

                    // 获取到共多少行
                    int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
                    String sheetName = sheet.getSheetName();
                    log.info("----------该工作表的名字：{}----------", sheetName);

                    Row everyrow = null;

                    List<BusinessTypeConf> allList = new ArrayList<>();

                    for (int i = 1; i < physicalNumberOfRows; i++) {
                        // 取出每一行
                        everyrow = sheet.getRow(i);

                        // 获取一共有多少列
                        int cellNumbers = everyrow.getPhysicalNumberOfCells();
                        /**
                         * 获取当前单元格内容的类型
                         * STRING、NUMERIC、BOOLEAN
                         */
                        CellType cellTypeEnum = everyrow.getCell(0).getCellTypeEnum();
                        CellType cellTypeEnum1 = everyrow.getCell(1).getCellTypeEnum();

                        everyrow.getCell(0).setCellType(CellType.STRING);
                        everyrow.getCell(1).setCellType(CellType.STRING);


                        BusinessTypeConf businessTypeConf = new BusinessTypeConf();
                        businessTypeConf.setBusinessTypeId(Long.valueOf(everyrow.getCell(0).toString()));
                        businessTypeConf.setBusinessType(everyrow.getCell(1).toString());
                        allList.add(businessTypeConf);
                    }

                    // TODO 写入MySQL
                    long startWriteMysql = System.currentTimeMillis();
                    Long insertCount = writeMysql(allList);
                    long endWriteMysql = System.currentTimeMillis();
                    log.info("-----批量写入MySQL程序耗时：{}毫秒，{}秒-----", (endWriteMysql-startWriteMysql), ((endWriteMysql-startWriteMysql)/1000));
                    log.info("批量写入成功：{}条", insertCount);

                    jsonObject.put("insertCount", insertCount);
                    jsonObject.put("message", "批量写入成功"+insertCount+"条");
                    jsonObject.put("consumeTime", (endWriteMysql-startWriteMysql)+"毫秒");

                } else {
                    // TODO 不符合文件上传格式
                    jsonObject.put("code", "1001");
                    jsonObject.put("msg", "不符合文件上传格式");
                    jsonObject.put("data", new ArrayList<>());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * 批量插入1W条MySQL数据（传统xml文件foreach写法）
     * @return
     */
    @Override
    public JSONObject forEachInsertBusinessInfo() {

        JSONObject jsonObject = new JSONObject();

        BusinessInfo businessInfo = null;
        String lngLat = "39.798074085515398,116.51922196247814";
        String phone = "18733839515";
        String email = "475104860@qq.com";
        String address = "郑州市金水区经一路59号 郑州子阳文化传播有限公司";
        String name = "郑州晚报广告热线";

        List<BusinessInfo> businessInfoList = new ArrayList<>();
//        business_info (business_id, business_original_id, business_address, business_name, business_email, business_lng_lat, business_phone)
        for (int i = 1; i <= 10000; i++){
            String uuid = UUID.randomUUID().toString();
            businessInfo = new BusinessInfo(uuid, uuid, address, name+i,
                    "", "", email, "", "", "", "",
                    "", "", "", lngLat, (long)0, (long)404,
                    phone, "", "");

            businessInfoList.add(businessInfo);
        }

        // TODO 批量写入MySQL
        long startWriteMysql = System.currentTimeMillis();
        Long insertCount = businessInfoDao.forEachInsert(businessInfoList);
        long endWriteMysql = System.currentTimeMillis();

        log.info("-----传统xml文件foreach写法，批量写入MySQL程序耗时：{}毫秒，{}秒-----", (endWriteMysql-startWriteMysql), ((endWriteMysql-startWriteMysql)/1000));
        log.info("传统xml文件foreach写法，批量写入成功：{}条", insertCount);

        // TODO 批量写入ES
        pulkWriteEs(businessInfoList);

        jsonObject.put("insertCount", insertCount);
        jsonObject.put("message", "批量写入成功"+insertCount+"条");
        jsonObject.put("consumeTime", (endWriteMysql-startWriteMysql)+"毫秒");

        return jsonObject;
    }

    private void pulkWriteEs(List<BusinessInfo> businessInfoList) {
        if (!CollectionUtils.isEmpty(businessInfoList)) {
            int size = businessInfoList.size();
            BulkRequest bulkRequest = new BulkRequest();
            for (int i = 0; i < size; i++) {
                BusinessInfo businessInfo = businessInfoList.get(i);
                bulkRequest.add(new IndexRequest());
            }

        }
    }


    /**
     * 批量插入1W条MySQL数据（batch批处理，手动提交，不采用自动提交）
     * @return
     */
    @Override
    public JSONObject batchInsertBusinessInfo() {

        JSONObject jsonObject = new JSONObject();
        // business_info (business_id, business_original_id, business_address, business_name, business_email, business_lng_lat, business_phone)
        BusinessInfo businessInfo = null;
        String headUrl = "/688B62F3E73C4A1DA55E7758A69D6642.jpg?Expires=1611800207&OSSAccessKeyId=aCW95UoszCe2MNLb&Signature=efSWmTQMLkSwsdxuMNeqpTzWvzE=";
        String bgUrl = "/887CEE282A32465592C1B1812F0A302B.jpg?Expires=1611800269&OSSAccessKeyId=aCW95UoszCe2MNLb&Signature=dowH2uvyqDgM3xhfpfsIp+8fOS4=";
        String lngLat = "39.798074085515398,116.51922196247814";
        String phone = "18733839515";
        String email = "475104860@qq.com";
        String address = "河北省保定市唐县下三土门村";
        String name = "欢欢";

        List<BusinessInfo> businessInfoList = new ArrayList<>();
        for (int i = 1; i <= 10000; i++){
            String uuid = UUID.randomUUID().toString();
            businessInfo = new BusinessInfo(uuid, uuid, address, name+i,
                    "", "", email, "", "", "", "",
                    "", headUrl, bgUrl, lngLat, (long)0, (long)404,
                    phone, "", "");

            businessInfoList.add(businessInfo);
        }


        //如果自动提交设置为true,将无法控制提交的条数，改为最后统一提交，可能导致内存溢出
        SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            // TODO 批量写入MySQL
            long startWriteMysql = System.currentTimeMillis();
            Long insertCount = 0L;
            for (int i = 1; i <= 10; i++){
                Page<BusinessInfo> pageFromList = PageUtils.getPageFromList(businessInfoList, i, 1000);
                insertCount = insertCount + businessInfoDao.forEachInsert(pageFromList);
                session.commit();
                //清理缓存，防止溢出
                session.clearCache();
            }
            long endWriteMysql = System.currentTimeMillis();

            log.info("batch批处理，手动提交，不采用自动提交，批量写入MySQL程序耗时：{}毫秒，{}秒", (endWriteMysql-startWriteMysql), ((endWriteMysql-startWriteMysql)/1000));
            log.info("batch批处理，手动提交，不采用自动提交，批量写入成功：{}条", insertCount);

            jsonObject.put("insertCount", insertCount);
            jsonObject.put("message", "批量写入成功"+insertCount+"条");
            jsonObject.put("consumeTime", (endWriteMysql-startWriteMysql)+"毫秒");

        } catch (Exception e) {
            e.printStackTrace();
            //没有提交的数据可以回滚
            if (session != null) {
                session.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }

        return jsonObject;
    }

    /**
     * 写入MySQL
     * @param allList
     */
    private Long writeMysql(List<BusinessTypeConf> allList) {
        log.info("-----开始写入MySQL，List大小为{}-----", allList.size());
        return businessTypeConfMapper.writeMysql(allList);
    }


}
