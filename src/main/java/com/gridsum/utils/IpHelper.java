package com.gridsum.utils;

import java.math.BigInteger;
import java.util.*;

/**
 * @author xulei
 * @date 2022/9/1
 * @description: 测试
 */
public class IpHelper {
    /**
     * 将缩写的ipv6地址补全
     *
     * @param ip
     * @return
     */
    public static String fullIp(String ip) {
        if ("::".equals(ip)) return "0000:0000:0000:0000:0000:0000:0000:0000";
        if (ip.endsWith("::")) ip += 0;
        //补全所有:
        String[] split = ip.split(":");
        String symbol = "::";
        int length = split.length;
        while (length < 8) {
            symbol += ":";
            length++;
        }
        ip = ip.replaceAll("::", symbol);
        String fullIp = "";
        for (String singleIp : ip.split(":")) {
            while (singleIp.length() < 4) {
                singleIp = "0" + singleIp;
            }
            fullIp += singleIp + ":";
        }
        return fullIp.substring(0, fullIp.length() - 1);
    }

    /**
     * 随机生成ip
     *
     * @param beginIp
     * @param endIp
     * @param num   需要随机生成ip个数
     * @return
     */
    public static List<String> getIp(String beginIp, String endIp, int num) {
        //将ip补全
        beginIp = fullIp(beginIp);
        endIp = fullIp(endIp);
        BigInteger begin = IpExchangeUtil.stringToBigInt(beginIp);
        BigInteger end = IpExchangeUtil.stringToBigInt(endIp);

        List<String> ips = new ArrayList<>();
//        int length = 0;
//        //判断set集合大小
//        if (end.subtract(begin).compareTo(new BigInteger(String.valueOf(num))) > 0) {
//            length = 10;
//        } else {
//            length = end.subtract(begin).intValue();
//        }

        Random random = new Random();
        while (true) {
            //在开始和结束ip范围内生成一个bigInteger随机数 通过起始ip的bigInteger加上随机数转成随机ip
            BigInteger result = new BigInteger((end.subtract(begin)).bitLength(), random);

            while (result.compareTo(end.subtract(begin)) > 0) {

                result = new BigInteger(end.subtract(begin).bitLength(), random);

            }
            ips.add(IpExchangeUtil.bigIntToString(begin.add(result)));
            break;
//            if (ips.size() >= length) break;
        }

        return ips;
    }
}
