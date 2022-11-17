package com.gridsum.utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xulei
 * @date 2022/9/27
 * @description: 测试
 */
public class UrlUtils {
    public static void main(String[] args) throws Exception {
        getCountry("中国");
    }

    /**
     * 获取高德所有城市名称
     */
    public static void getCountry(String country) throws Exception {
        //拼接url
        String url1 = "https://restapi.amap.com/v3/config/district?key=31bc19ab892b7351f9d8d7be9d2bb532&keywords=" + country + "&extensions=base&output=JSON&subdistrict=2";

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

        Map<String, Object> countryMap = (Map<String, Object>) ((JSONArray) map.get("districts")).get(0);

        ArrayList<Map<String, Object>> provinceList = (ArrayList<Map<String, Object>>) countryMap.get("districts");

        File file = new File("gaode.txt");

        for (Map<String, Object> cityMap : provinceList) {
            String province = (String) cityMap.get("name");

            ArrayList<Map<String, Object>> citys = (ArrayList<Map<String, Object>>) (cityMap.get("districts"));
            for (Map<String, Object> city : citys) {
                String cityName = (String) city.get("name");
                FileUtils.writeStringToFile(file, "中国" + "\t" + province + "\t" + cityName + "\n", true);
            }
        }
    }


    /**
     * 获取高德数据源
     *
     * @param country
     * @return
     * @throws Exception
     */
    public static String getUrl(String country) throws Exception {
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

        Map<String, String> districtsMap = (Map<String, String>) ((JSONArray) map.get("districts")).get(0);


        return districtsMap.get("polyline");
    }

    /**
     * 百度转换坐标系接口
     */
    public static String changeBaiDuLocation(String location) throws Exception {
        //拼接url
        String url1 = "https://api.map.baidu.com/geoconv/v1/?coords=" + location + "&from=3&to=5&ak=pm3YxRrjk6lSGFKGGaSDjEwr9Kk8Ojea";

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

        List<Map<String, Double>> result = (List<Map<String, Double>>) map.get("result");

        StringBuffer buffer = new StringBuffer();

        for (Map<String, Double> stringDoubleMap : result) {
            for (Map.Entry<String, Double> entry : stringDoubleMap.entrySet()) {
                buffer.append(entry.getValue() + ",");
            }
            buffer.deleteCharAt(buffer.length() - 1);
            buffer.append(";");
        }

        buffer.deleteCharAt(buffer.length() - 1);

        return buffer.toString();
    }


    /**
     * 转换坐标系  将坐标转换为百度坐标
     *
     * @param location
     * @return
     */
    public static String changeLocation(String location) throws Exception {
        //拼接url
        String url1 = "https://restapi.amap.com/v3/assistant/coordinate/convert?key=826c1d2d94e7004efd835d9f1a57975c&locations=" + location + "&coordsys=baidu";

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

        HashMap<String, String> map = (HashMap<String, String>) JSON.parse(response);

        return map.get("locations");

    }

    /**
     * 处理polyline
     */
    public static String processPolyline(String polyline) throws Exception {

        StringBuffer stringBuffer = new StringBuffer();

        //定义表示位
        int count = 0;

        String str = "";
        String[] split = polyline.split(";");
        for (String s : split) {
            count++;
            str += s + ";";
            if (count == 100) {
                String location = changeBaiDuLocation(str.substring(0, str.length() - 1));
                stringBuffer.append(location + ";");
                count = 0;
                str = "";
            }
        }
        if (str.equals("")) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        } else {
            stringBuffer.append(changeBaiDuLocation(str.substring(0, str.length() - 1)));
        }

        return stringBuffer.toString();
    }

    /**
     * 截取字符串
     *
     * @param data 指定字符串
     * @param str  需要定位的特殊字符或者字符串
     * @param num  第n次出现
     * @return 第n次出现的位置索引
     */
    public static int getIndexOf(String data, String str, int num) {
        Pattern pattern = Pattern.compile(str);
        Matcher findMatcher = pattern.matcher(data);
        //标记遍历字符串的位置
        int indexNum = 0;
        while (findMatcher.find()) {
            indexNum++;
            if (indexNum == num) {
                break;
            }
        }
        System.out.println("字符或者字符串" + str + "第" + num + "次出现的位置为：" + findMatcher.start());
        return findMatcher.start();
    }


    private static double xPi = 3.14159265358979324 * 3000.0 / 180.0;

    public static String marsToBaidu(String location) {
        String[] split = location.split(",");
        double x = Double.parseDouble(split[0]);
        double y = Double.parseDouble(split[1]);
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * xPi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * xPi);

        return String.valueOf(dataDigit(14, z * Math.cos(theta) + 0.0065) + "," + dataDigit(14, z * Math.sin(theta) + 0.006));
    }

    public static double dataDigit(int digit, double input) {
        return BigDecimal.valueOf(input).setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 处理polyline
     */
    public static String processLocation(String polyline) {
        StringBuffer buffer = new StringBuffer();
        polyline = polyline.replaceAll("\\|", ";");
        String[] split = polyline.split(";");
        for (String s : split) {
            buffer.append(marsToBaidu(s) + ";");
        }
        buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }

}
