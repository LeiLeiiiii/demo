package com.gridsum.controller;


import com.gridsum.domain.GpsCountryEntity;
import com.gridsum.utils.FileUtil;
import com.gridsum.utils.HtmlUtils;
import com.gridsum.utils.HttpClientTest;
import com.gridsum.utils.UrlUtils;

import java.util.List;

/**
 * @author xulei
 * @date 2022/9/28
 * @description: 测试
 */
public class GpsController {

    public static void main(String[] args) throws Exception {
        //读取国双的gps库文件
        List<GpsCountryEntity> list = FileUtil.readGpsFile("城市经纬度信息.csv");
        //取出掉首个标题行
        list.remove(0);
        //处理所有的城市
        for (GpsCountryEntity entity : list) {
            String city = entity.getCity();
            if (entity.getPolyline().equals("null")) continue;
            System.out.println("============开始制作" + city + "模板=============");
            //获取高德的数据源
            if (city.equals("那曲地区")){
                city = "那曲市";
            }
            String polyline2 = UrlUtils.getUrl(city);
            HtmlUtils.createHtml(UrlUtils.processLocation(entity.getPolyline()),UrlUtils.processLocation(polyline2), city);
            System.out.println("============结束制作" + city + "模板=============");
        }
    }
}
