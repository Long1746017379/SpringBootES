package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.example.demo.entity.SyncBusinessInfo;
import com.example.demo.mapper.BusinessInfoDao;
import com.example.demo.utils.CountDistanceByLngLat;
import com.example.demo.utils.HttpClientUtil;
import com.example.demo.utils.SFTPUtil;
import com.example.demo.utils.oss.AliyunOSSUtil;
import com.example.demo.utils.oss.AliyunOssConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import static com.example.demo.utils.oss.AliyunOSSUtil.RSP_CODE_SUCCESS;

/**
 * @author DongChengLong
 * @desc
 * @date 2021/01/12 13:15
 */
@Slf4j
@RestController
@RequestMapping(value = "sftp")
@PropertySource(value = "classpath:application.properties", encoding = "UTF-8")
public class SftpController {

    private ChannelSftp sftpClient = null;

    @Value("${sftp.filePath}")
    private String filePath;

    @Value("${sftp.fileName}")
    private String fileName;

    /**
     * oss 工具客户端
     */
    private static OSSClient ossClient = null;

    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    BusinessInfoDao businessInfoDao;

    SqlSession session = null;

    @GetMapping("testValue")
    public JSONObject testValue() {
        JSONObject jsonObject = new JSONObject();
        log.info("filePath：{},fileName：{}", filePath, fileName);
        jsonObject.put("filePath", filePath);
        jsonObject.put("fileName", fileName);
        return jsonObject;
    }

    /**
     * 测试读取sftp乱码问题
     * @param jsonObject
     * @return
     */
    @PostMapping("testSyncMySQLAndES")
    public JSONObject testSyncMySQLAndES(@RequestBody JSONObject jsonObject) {
        String fileName = jsonObject.getString("fileName");
        JSONObject repBody = new JSONObject();
        if (StringUtils.isAnyBlank(fileName)) {
            repBody.put("message", "fileName 为空请仔细核实！！！");
            return repBody;
        }
        long startTime = System.currentTimeMillis();
        // TODO 读取OSS数据批量写入MySQL和ES
        List data = readFileToOSSTest(fileName);
        long endTime = System.currentTimeMillis();
        log.info("共计耗时：{}毫秒，{}秒", (endTime-startTime), ((endTime-startTime)/1000));
        repBody.put("data", data);
        repBody.put("message", "共计耗时：" + (endTime-startTime) + " 毫秒" + ((endTime-startTime)/1000) + " 秒");

        return repBody;
    }

    private List readFileToOSSTest(String fileName1) {
        SFTPUtil.getSftpClient();
        return SFTPUtil.testReadSftp(filePath, fileName);
    }


    @GetMapping(value = "/uploadOSS")
    public JSONObject uploadOSS(){
        // TODO 从ftp服务器中读取文件，然后就写入上传到OSS
        JSONObject jsonObject = new JSONObject();
        try {
            sftpClient = SFTPUtil.getSftpClient();
            if (SFTPUtil.isExistFilePath(filePath) && SFTPUtil.isExist(filePath, fileName)) {
                long startTime = System.currentTimeMillis();
                Vector<?> objects = SFTPUtil.listFiles(filePath);
                for (Object o : objects) {
                    log.info("{}", o);
                }
                SFTPUtil.download(filePath, fileName);
                long endTime = System.currentTimeMillis();
                jsonObject.put("message", "耗时："+(endTime-startTime)+"毫秒");
            } else {
                log.error("该路径：{}不存在，或者该路径下的文件：{}不存在", filePath, fileName);
            }
        } catch (SftpException e) {
            e.printStackTrace();
            jsonObject.put("message", "同步sftp文件服务器数据到oss异常："+e);
        }
        return jsonObject;
    }


    @PostMapping("/readOss")
    public JSONObject readOss(@RequestBody JSONObject jsonObject) {
        JSONObject rspBody = new JSONObject();
        String fileName1 = jsonObject.getString("fileName");
        JSONObject repBody = new JSONObject();
        if (StringUtils.isAnyBlank(fileName1)) {
            repBody.put("message", "fileName 为空请仔细核实！！！");
            return repBody;
        }

        // TODO 读取OSS
//        readFileToOSS(fileName);
        SFTPUtil.getSftpClient();
        List list = SFTPUtil.testReadSftp(filePath, fileName);
        rspBody.put("data", list);
        return rspBody;
    }

    /**
     * 读取OSS
     * @param fileName
     */
    private void readFileToOSS(String fileName) {
        try {
            ossClient = AliyunOssConfig.getOssClient();
            log.info("-----开始OSS文件读取-----");
            // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
            OSSObject ossObject = ossClient.getObject(AliyunOssConfig.JAVA_BUCKET_NAME, fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ossObject.getObjectContent()));

            int nowLine = 1;
            String line = null;
            String id = "";
            String keyword = "";
            String typeId = "";
            String city = "";
            List<SyncBusinessInfo> syncBusinessInfoList = new ArrayList<>();
            List<SyncBusinessInfo> batchSyncBusinessInfoList = new ArrayList<>();
            SyncBusinessInfo nowSyncBusinessInfo;

            while ((line = bufferedReader.readLine()) != null) {
                log.info("第 {} 行数据：{}", nowLine, line);
                String[] split = line.split(",");

                // TODO 取出每一行的数据去调用114提供的查询接口
                id = split[0];
                keyword = split[1];
                typeId = split[2];
                city = split[3];

                if (StringUtils.isAnyBlank(id, keyword, typeId, city)) {

                    String url = "http://133.160.45.100:9100/agent/doSearchunit?city="+city+"&keyword="+keyword+"";
                    String postJson = HttpClientUtil.doGet(url);
                    ObjectMapper objectMapper = new ObjectMapper();

                    syncBusinessInfoList = setOfferInfoToList(postJson, objectMapper);
                    nowSyncBusinessInfo = new SyncBusinessInfo();

                    for (SyncBusinessInfo syncBusinessInfo : syncBusinessInfoList) {
                        if (id.equals(syncBusinessInfo.getUnitid())) {
                            nowSyncBusinessInfo.setUnitid(syncBusinessInfo.getUnitid());
                            nowSyncBusinessInfo.setName(syncBusinessInfo.getName());
                            nowSyncBusinessInfo.setAddress(syncBusinessInfo.getAddress());
                            nowSyncBusinessInfo.setKeyname(syncBusinessInfo.getKeyname());
                            nowSyncBusinessInfo.setPhone(syncBusinessInfo.getPhone());
                            nowSyncBusinessInfo.setAddService(syncBusinessInfo.getAddService());
                            nowSyncBusinessInfo.setPassword(syncBusinessInfo.getPassword());
                            break;
                        }
                    }

                    // TODO 走公网获取商家的经纬度信息
                    String latLng = "";
                    if (nowSyncBusinessInfo != null) {
                        Map lngLat = CountDistanceByLngLat.getLngLat(nowSyncBusinessInfo.getAddress());
                        if (lngLat.containsKey("lng") && lngLat.containsKey("lat")){
                            Double lng = (Double) lngLat.get("lng");
                            Double lat = (Double) lngLat.get("lat");
                            latLng = lat.toString() + "," + lng.toString();
                            nowSyncBusinessInfo.setLnglat(latLng);
                        }
                    }
                    batchSyncBusinessInfoList.add(nowSyncBusinessInfo);

                    // TODO 批量插入MySQL
                    if ((batchSyncBusinessInfoList.size() % 10000 == 0)) {
                        batchIntoMySQL(batchSyncBusinessInfoList);
                        batchSyncBusinessInfoList = new ArrayList<>();
                    }

                    // TODO 批量插入ES
                    if (batchSyncBusinessInfoList.size() % 10000 == 0) {
                        batchIntoES(batchSyncBusinessInfoList);
                        batchSyncBusinessInfoList = new ArrayList<>();
                    }


                }

                nowLine++;
            }

            // 判断该list是否还有数据，有则提交
            if (batchSyncBusinessInfoList.size() > 0) {
                // 执行MySQL批处理


                // 执行ES批处理
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

    }

    /**
     * 批量写入ES
     * @param batchSyncBusinessInfoList
     */
    private void batchIntoES(List<SyncBusinessInfo> batchSyncBusinessInfoList) {


    }


    /**
     * 批量写入MySQL
     * @param batchSyncBusinessInfoList
     */
    private void batchIntoMySQL(List<SyncBusinessInfo> batchSyncBusinessInfoList) {

        // 如果自动提交设置为true,将无法控制提交的条数，改为最后统一提交，可能导致内存溢出
        session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
//        businessInfoDao.forEachInsert(batchSyncBusinessInfoList);
        session.commit();
        //清理缓存，防止溢出
        session.clearCache();

    }

    /**
     * 将调用114返回的data为list的数据做处理
     * @param postJson
     * @param objectMapper
     * @return
     */
    private List<SyncBusinessInfo> setOfferInfoToList(String postJson, ObjectMapper objectMapper) {
        List<SyncBusinessInfo> data = new ArrayList<>();
        try {
            Map<String, Object> jsonMap = objectMapper.readValue(postJson, Map.class);
            String respCode = (String) jsonMap.get("rc");
            if (RSP_CODE_SUCCESS.equals(respCode)) {
                CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, SyncBusinessInfo.class);
                data = objectMapper.readValue(objectMapper.writeValueAsBytes(jsonMap.get("data")), listType);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }



}
