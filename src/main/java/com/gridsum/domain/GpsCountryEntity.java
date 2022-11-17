package com.gridsum.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @author xulei
 * @date 2022/9/28
 * @description: 测试
 */
@Builder
@Data
public class GpsCountryEntity {
    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 省份key
     */
    private int provinceKey;

    /**
     * 城市
     */
    private String city;

    /**
     * 城市code
     */
    private int cityCode;

    /**
     * adcode
     */
    private int adCode;

    /**
     *  经纬度点位
     */
    private String polyline;

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     *  最大经度
     */
    private String maxLongitude;

    /**
     *  最小经度
     */
    private String minLongitude;

    /**
     *  最大纬度
     */
    private String maxLatitude;

    /**
     *  最小纬度
     */
    private String minLatitude;

}
