package com.gridsum.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xulei
 * @date 2022/9/27
 * @description: 测试
 */
public class HttpClientTest {

    /**
     * 获取高德数据源
     *
     * @param country
     * @return
     * @throws IOException
     */
    public static String getDataSource(String country) throws IOException {

        CloseableHttpClient httpClient = HttpClients.createDefault();

//        HttpGet httpGet = new HttpGet("https://restapi.amap.com/v3/config/district?keywords=" + country + "&key=31bc19ab892b7351f9d8d7be9d2bb532");
        HttpGet httpGet = new HttpGet("https://restapi.amap.com/v3/config/district?key=31bc19ab892b7351f9d8d7be9d2bb532&keywords=" + country + "&extensions=all&output=JSON");

        //调用HttpClient实例来执行GET请求方法，得到response
        CloseableHttpResponse response = httpClient.execute(httpGet);

        HttpEntity entity = response.getEntity();

        String string = EntityUtils.toString(entity, "utf-8");

        HashMap<String, Object> map = (HashMap<String, Object>) JSON.parse(string);

        Map<String, String> districtsMap = (Map<String, String>) ((JSONArray) map.get("districts")).get(0);

        return districtsMap.get("polyline");
    }

    public static String changeLocations(String location) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("https://restapi.amap.com/v3/assistant/coordinate/convert?key=31bc19ab892b7351f9d8d7be9d2bb532&locations=" + location + "&coordsys=baidu");

        CloseableHttpResponse response = httpClient.execute(httpGet);

        HttpEntity entity = response.getEntity();

        String string = EntityUtils.toString(entity, "utf-8");

        HashMap<String, String> map = (HashMap<String, String>) JSON.parse(string);

        return map.get("locations");
    }


    public static void main(String[] args) throws IOException {
//        String hashMap = getTest("北京市");
//        System.out.println("hashMap = " + hashMap);
    }
}
