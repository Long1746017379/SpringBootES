package com.example.demo.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.gavaghan.geodesy.Ellipsoid;
import org.gavaghan.geodesy.GeodeticCalculator;
import org.gavaghan.geodesy.GeodeticCurve;
import org.gavaghan.geodesy.GlobalCoordinates;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

/**
 * @author 董成龙
 * @desc 根据两个经纬度计算两地之间的距离
 * @date 2020-11-18
 */
@Slf4j
@Component
public class CountDistanceByLngLat {

    /**
     * 根据经纬度计算两地之间距离（可通过Ellipsoid.Sphere 和 Ellipsoid.WGS84）
     * Sphere相对而言比较准确一些几乎无偏差，WGS84相对于Sphere而言有几十米的误差
     *
     * @param currentLng 用户当前位置经度
     * @param currentLat 用户当前位置纬度
     * @param targetLng  用户目标位置经度
     * @param targetLat  用户目标位置纬度
     * @param ellipsoid  使用的哪种算法（Ellipsoid.Sphere 和 Ellipsoid.WGS84）
     * @return
     */
    public static double getDistanceBySphere(double currentLng, double currentLat, double targetLng, double targetLat, Ellipsoid ellipsoid) {
        // 用户当前坐标
        GlobalCoordinates currentLocation = new GlobalCoordinates(currentLng, currentLat);

        // 目标位置坐标
        GlobalCoordinates targetLocation = new GlobalCoordinates(targetLng, targetLat);

        return getDistanceMeter(currentLocation, targetLocation, ellipsoid);
    }


    private static double getDistanceMeter(GlobalCoordinates currentLocation, GlobalCoordinates targetLocation, Ellipsoid ellipsoid) {
        // 创建GeodeticCalculator，调用计算方法，传入坐标系、经纬度用于计算距离
        GeodeticCurve geoCurve = new GeodeticCalculator().calculateGeodeticCurve(ellipsoid, currentLocation, targetLocation);
        return geoCurve.getEllipsoidalDistance();
    }



    /**
     * 地理编码正向解析（根据位置转换为经纬度）
     * @param address
     * @return
     */
    public static Map getLngLat(String address) {
        Map resMap = new HashMap<>();
        String url = "http://api.map.baidu.com/geocoding/v3/?address="+address+"&output=json&ak=uOVlZMORsibggoeSd98ZFU20mA7dQfZm";
        try {
            String json = HttpClientUtil.doGet(url);
            JSONObject jsonObject = JSONObject.parseObject(json);

            Double lat = jsonObject.getJSONObject("result").getJSONObject("location").getDoubleValue("lat");
            Double lng = jsonObject.getJSONObject("result").getJSONObject("location").getDoubleValue("lng");

            resMap.put("lng", lng);
            resMap.put("lat", lat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resMap;
    }

    /**
     * 地理编码正向解析（根据位置转换为经纬度）
     *
     * @param address
     * @return
     */
    public static Map getLngAndLat(String address) {
        Map map = new HashMap();
        String url = "http://api.map.baidu.com/geocoding/v3/?address="+address+"&output=json&ak=uOVlZMORsibggoeSd98ZFU20mA7dQfZm&callback=showLocation";
        try {
            String json = loadJSON(url);
            JSONObject obj = JSONObject.parseObject(json);
            if (obj.get("status").toString().equals("0")) {
                double lng = obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
                double lat = obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
                map.put("lng", lng);
                map.put("lat", lat);
                log.info("根据地址 {}，获取到的经纬度为{}，{}", address, lng, lat);
            } else {
                map.put("error", "未找到相匹配的经纬度！");
                log.info("未找到相匹配的经纬度！");
            }
        } catch (Exception e) {
            map.put("error", "未找到相匹配的经纬度，请检查地址");
            log.info("未找到相匹配的经纬度！请检查地址");
        }
        return map;
    }

    private static double getDecimal(double num) {
        if (Double.isNaN(num)) {
            return 0;
        }
        BigDecimal bd = new BigDecimal(num);
        num = bd.setScale(6, BigDecimal.ROUND_HALF_UP).doubleValue();
        return num;
    }

    private static String loadJSON(String url) {
        StringBuilder json = new StringBuilder();
        try {
            URL oracle = new URL(url);
            URLConnection yc = oracle.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream(), "UTF-8"));
            String inputLine = null;
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            }
            in.close();
        } catch (MalformedURLException e) {

        } catch (IOException e) {

        }
        return json.toString();
    }


    /**
     * 根据经纬度获取到地理位置（省市区/县）
     *
     * @param lat 纬度
     * @param lng 经度
     * @return
     * @throws IOException
     */
    public static JSONObject getCoordinate(String lat, String lng) {

        JSONObject resBody = new JSONObject();
        String url = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=uOVlZMORsibggoeSd98ZFU20mA7dQfZm&output=json&coordtype=wgs84ll&location="+lat+","+lng+"";
        String json = HttpClientUtil.doGet(url);

        JSONObject jsonObject = JSONObject.parseObject(json);
        String county = jsonObject.getJSONObject("result").getJSONObject("addressComponent").getString("country");
        String province = jsonObject.getJSONObject("result").getJSONObject("addressComponent").getString("province");
        String city = jsonObject.getJSONObject("result").getJSONObject("addressComponent").getString("city");
        String district = jsonObject.getJSONObject("result").getJSONObject("addressComponent").getString("district");

        resBody.put("country", county);
        resBody.put("province", province);
        resBody.put("city", city);
        resBody.put("district", district);

        return resBody;
    }

    public static void main(String[] args) {
        log.info("结果为：{}", getLngLat("天安门"));
        log.info("结果为：{}", getCoordinate("39.91552563252131", "116.40384710616807"));
    }



}
