package com.gridsum.domain;

import lombok.Builder;
import lombok.Data;

/**
 * @author xulei
 * @date 2022/8/31
 * @description: 测试
 */
@Data
@Builder
public class GpsEntity {
    /**
     * 国家
     */
    private String country;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
}
