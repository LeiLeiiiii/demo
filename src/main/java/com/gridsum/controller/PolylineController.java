package com.gridsum.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.gridsum.domain.GpsCountryEntity;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * @author xulei
 * @date 2022/11/7
 * @description: 测试
 */
public class PolylineController {
    private static final Logger logger = LoggerFactory.getLogger(PolylineController.class);


    public static void main(String[] args) throws Exception {
        //读取高德城市信息
        List<GpsCountryEntity> entityList = readFile(new String[]{"gaode.txt"});

        //通过城市信息去获取高德信息，并将属性进行填充
        for (GpsCountryEntity gpsCountryEntity : entityList) {
            Map<String, String> map = getUrl(gpsCountryEntity.getCity());
            //cityCode
            gpsCountryEntity.setCityCode(Integer.parseInt(map.get("citycode")));
            //polyline
            String polyline = map.get("polyline");
            polyline = polyline.replaceAll("||",",");
            gpsCountryEntity.setPolyline(polyline);
            //中心经纬度
            String[] centers = map.get("center").split(",");
            gpsCountryEntity.setLongitude(centers[0]);
            gpsCountryEntity.setLatitude(centers[1]);
            //最大最小经纬度
            findMaxOrMinLongitudeAndLatitude(gpsCountryEntity,polyline);

        }
        //将entityList输出到文件里
        

    }

    private static void findMaxOrMinLongitudeAndLatitude(GpsCountryEntity gpsCountryEntity, String polyline) {
        //先将点集转换成点数组
        String[] split = polyline.split(";");
        ArrayList<Double> longitudeList = new ArrayList<>();
        ArrayList<Double> latitudeList = new ArrayList<>();

        for (String s : split) {
            String[] strings = s.split(",");
            longitudeList.add(Double.valueOf(strings[0]));
            latitudeList.add(Double.valueOf(strings[1]));
        }

        Collections.sort(longitudeList);
        Collections.sort(latitudeList);

        gpsCountryEntity.setMaxLongitude(String.valueOf(longitudeList.get(longitudeList.size()-1)));
        gpsCountryEntity.setMinLongitude(String.valueOf(longitudeList.get(0)));

        gpsCountryEntity.setMaxLatitude(String.valueOf(latitudeList.get(latitudeList.size()-1)));
        gpsCountryEntity.setMinLatitude(String.valueOf(latitudeList.get(0)));

    }


    public static List<GpsCountryEntity> readFile(String[] paths) {
        List<GpsCountryEntity> list = new ArrayList<>();
        try {
            List<String> strings = new ArrayList<>();
            //判空
            for (String path : paths) {
                if (StringUtils.isEmpty(path)){
                    continue;
                }
                strings.addAll(FileUtils.readLines(new File(path)));
            }
            for (String string : strings) {
                String[] split = string.split("\t");
                list.add(GpsCountryEntity.builder().country(split[0]).province(split[1]).city(split[2]).build());
            }

        } catch (IOException e) {
            logger.error("读取文件错误，错误信息为：{}",e.getMessage());
        }
        return list;
    }

    /**
     * 获取高德数据源
     *
     * @param country
     * @return
     * @throws Exception
     */
    public static Map<String, String> getUrl(String country) throws Exception {
        //拼接url
        String url1 = "https://restapi.amap.com/v3/config/district?key=31bc19ab892b7351f9d8d7be9d2bb532&keywords=" + country + "&extensions=all&output=JSON";

        // 1  创建URL对象,接收用户传递访问地址对象链接
        URL url = new URL(url1);

        // 2 打开用户传递URL参数地址
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();

        // 3 设置HTTP请求的一些参数信息
        connect.setRequestMethod("GET"); // 参数必须大写
        connect.connect();


        // 4 获取URL请求到的数据，并创建数据流接收
        InputStream isString = connect.getInputStream();

        // 5 构建一个字符流缓冲对象,承载URL读取到的数据
        BufferedReader isRead = new BufferedReader(new InputStreamReader(isString));

        // 6 输出打印获取到的文件流

        //定义一个接受数据的字符串
        String response = "";
        String str = "";
        while ((str = isRead.readLine()) != null) {
            str = new String(str.getBytes(), "UTF-8"); //解决中文乱码问题
            response = str;
        }

        // 7 关闭流
        isString.close();
        connect.disconnect();

        HashMap<String, Object> map = (HashMap<String, Object>) JSON.parse(response);

        JSONArray array = (JSONArray) map.get("districts");

        Map<String, String> districtsMap = null;

        for (Object o : array) {
            Map<String, String> stringMap = (Map<String, String>) o;
            if (stringMap.get("name").equals(country)){
                districtsMap = stringMap;
                break;
            }
        }

//        Map<String, String> districtsMap = (Map<String, String>) ((JSONArray) map.get("districts")).get(0);

//        if (((JSONArray) map.get("districts")).size() != 1){
//            districtsMap = (Map<String, String>) ((JSONArray) map.get("districts")).get(1);
//        }


        return districtsMap;
    }
}
