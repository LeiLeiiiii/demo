package com.gridsum.controller;

import com.gridsum.domain.GpsEntity;
import com.gridsum.iplocation.IpLocation;
import com.gridsum.iplocation.IpLocationParser;
import com.gridsum.utils.FileUtil;
import com.gridsum.utils.IpHelper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * @author xulei
 * @date 2022/8/31
 * @description: 测试
 */
public class GpsUpdate {
    private static final Logger logger = LoggerFactory.getLogger(GpsUpdate.class);
    public static final String TAG_CHARACTER = ",";
    public static final String TABLE_CHARACTER = "\t";
    public static final String COUNTRY = "中国";

    public static void main(String[] args) {
        //1.分别读取ip库的文件和gps库的文件
        //读ip库文件
//        List<String> ipLocation = readFile(new String[]{"ipv6.txt","ipv4.txt"});
        //1.1处理文件
//        Map<String, String> process = process(ipLocation);

        //读取高德文件
        List<String> ipLocation = readFile(new String[]{"gaode.txt"});

        HashMap<String, String> process = new HashMap<>();
        for (String s : ipLocation) {
            process.put(s,s);
        }

        //读gps文件
        List<GpsEntity> gps = readGpsFile("gps.csv");
        //2.比较并输出文件
        compareFiles(process, gps);

    }

    private static void compareFiles(Map<String, String> map, List<GpsEntity> list) {
        try {
            File file = new File("unEqual1.txt");
            String location = "";
            for (GpsEntity gps : list) {
                location = gps.getCountry()
                        + TABLE_CHARACTER
                        + gps.getProvince()
                        + TABLE_CHARACTER
                        + gps.getCity();
                if (StringUtils.isEmpty(map.get(location))) {
                    FileUtils.writeStringToFile(file, location +"\n", true);
                }
            }

//            File file1 = new File("not1.txt");
//            HashMap<String, String> hashMap = new HashMap<>();
//            String location = "";
//            for (GpsEntity gps : list) {
//                location = gps.getCountry()
//                        + TABLE_CHARACTER
//                        + gps.getProvince()
//                        + TABLE_CHARACTER
//                        + gps.getCity();
//                hashMap.put(location,location);
//            }
//            for (Map.Entry<String, String> entry : map.entrySet()) {
//                if (StringUtils.isEmpty(hashMap.get(entry.getKey()))){
//                    FileUtils.writeStringToFile(file1, entry.getKey() +"\n", true);
//                }
//            }

        } catch (IOException e) {
            logger.error("文件写入错误，错误信息为：{}", e.getMessage());
        }
    }

    private static List<GpsEntity> readGpsFile(String path) {
        CSVFormat format = CSVFormat.Builder.create()
                .setSkipHeaderRecord(true)
                .setDelimiter(TAG_CHARACTER)
                .setAllowDuplicateHeaderNames(false)
                .setTrim(true)
                .build();

        Function<CSVRecord, GpsEntity> caaIpDBParser =
                (record) -> GpsEntity.builder()
                        .country(record.get(0))
                        .province(record.get(1))
                        .city(record.get(3))
                        .build();

        return FileUtil.loadCSVFile(new File(path), format, caaIpDBParser);
    }


    public static List<String> readFile(String[] paths) {
        List<String> strings = new ArrayList<>();
        try {
            //判空
            for (String path : paths) {
                if (StringUtils.isEmpty(path)){
                    continue;
                }
                strings.addAll(FileUtils.readLines(new File(path)));
            }
        } catch (IOException e) {
            logger.error("读取文件错误，错误信息为：{}",e.getMessage());
        }
        return strings;
    }

    /**
     * 生成国家省份城市的国双标准文件
     *
     * @param list
     * @throws IOException
     */
    public static Map<String, String> process(List<String> list) {
        HashMap<String, String> map = new HashMap<>();
        try {
            Set<String> strings = new HashSet<>();
            for (String ipString : list) {
                String[] split = ipString.split(TABLE_CHARACTER);
                List<String> ip = IpHelper.getIp(split[0], split[1], 1);
                IpLocation ipLocation = IpLocationParser.getInstance().parseIp(ip.get(0));
                if (ipLocation.getCountry().equals(COUNTRY)
                        && !"-".equals(ipLocation.getProvince())
                        && !"-".equals(ipLocation.getCity())) {
                    strings.add(ipLocation.getCountry()
                            + TABLE_CHARACTER
                            + ipLocation.getProvince()
                            + TABLE_CHARACTER
                            + ipLocation.getCity());
                }
            }
            FileUtils.writeLines(new File("standardFile.txt"), strings,true);
            for (String string : strings) {
                map.put(string, string);
            }
        } catch (IOException e) {
            logger.error("文件写入错误，错误信息为：{}", e.getMessage());
        }
        return map;
    }


}
