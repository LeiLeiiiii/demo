package com.gridsum.utils;

import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * @author xulei
 * @date 2022/9/28
 * @description: 测试
 */
public class HtmlUtils {
    public static void createHtml(String polyline1,String polyline2,String country) {
        try {
            //输出文件
            PrintStream printStream = new PrintStream(new FileOutputStream("src\\main\\resources\\static/"+country+".html"));
            //导入html模板
            StringBuilder stringBuilder = htmlModel(polyline1,polyline2);

            printStream.println(stringBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        createHtml("116.812384,39.615914;116.812081,39.615689;116.809714,39.614525;116.806213,39.613809;116.805113,39.614337","北京");
    }

    /**
     * html模板
     * 红线是国双的  蓝线是高德的
     * @param polyline1  国双经纬度点位
     * @param polyline2 高德经纬度点位
     * @return
     */
    public static StringBuilder htmlModel(String polyline1,String polyline2){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("<!DOCTYPE html>");
        stringBuilder.append("<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">");
        stringBuilder.append("<head>");
        stringBuilder.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        stringBuilder.append("<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\" />");
        stringBuilder.append("<style type=\"text/css\">");
        stringBuilder.append("body, html,#allmap {width: 100%;height: 100%;overflow: hidden;margin:0;}");
        stringBuilder.append("</style>");
        stringBuilder.append("<script type=\"text/javascript\" src=\"http://api.map.baidu.com/api?v=2.0&ak=BQgIg9BRTszhwxXzDrTxIDN9aBA32MN8\"></script>");
        stringBuilder.append("<title>route</title>");
        stringBuilder.append("<base href=\"${pageContext.request.contextPath}/\"/>");
        stringBuilder.append("</head>");
        stringBuilder.append("<body>");
        stringBuilder.append("<div id=\"allmap\"></div>");
        stringBuilder.append("</body>");
        stringBuilder.append("</html>");
        stringBuilder.append("<script type=\"text/javascript\">");
        stringBuilder.append("var map = new BMap.Map(\"allmap\"); ");
        stringBuilder.append("let str =  \""+polyline1+"\"; ");
        stringBuilder.append("let strArr = str.split(';'); ");
        stringBuilder.append("let result = []; ");
        stringBuilder.append("strArr.forEach(item => {");
        stringBuilder.append("result.push(item.split(','));");
        stringBuilder.append("}); ");
        stringBuilder.append("var point = new BMap.Point(result[0][0],result[0][1]); ");
        stringBuilder.append("map.centerAndZoom(point, 15); ");
        stringBuilder.append("let data = []; ");
        stringBuilder.append("result.forEach(item => {");
        stringBuilder.append("data.push(new BMap.Point(Number(item[0]),Number(item[1])));");
        stringBuilder.append("}); ");
//        stringBuilder.append("console.log(data);");
        stringBuilder.append("var polyline = new BMap.Polyline(data, {strokeColor:\"red\", strokeWeight:10, strokeOpacity:1}); ");
        stringBuilder.append("map.enableScrollWheelZoom(); ");
        stringBuilder.append("map.addOverlay(polyline); ");


        stringBuilder.append(" let str2 = \""+polyline2+"\";");
        stringBuilder.append(" let strArr1 = str2.split(';');");
        stringBuilder.append(" let result1 = [];");
        stringBuilder.append(" strArr1.forEach(item => {");
        stringBuilder.append(" result1.push(item.split(','));");
        stringBuilder.append(" }); ");
        stringBuilder.append("var point1 = new BMap.Point(result1[0][0],result1[0][1]);");
        stringBuilder.append("  map.centerAndZoom(point1, 15);");
        stringBuilder.append(" let data1 = [];");
        stringBuilder.append(" result1.forEach(item => {");
        stringBuilder.append(" data1.push(new BMap.Point(Number(item[0]),Number(item[1])));");
        stringBuilder.append(" }); ");
        stringBuilder.append("var polyline1 = new BMap.Polyline(data1, {strokeColor:\"blue\", strokeWeight:6, strokeOpacity:1});");
        stringBuilder.append(" map.addOverlay(polyline1);");


        stringBuilder.append("</script>");
        stringBuilder.append("</html>");


        return stringBuilder;
    }
}
