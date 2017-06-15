package com.sby.practice.utils;

import com.baidu.mapapi.map.Marker;

/**
 * 类型转换
 * Created by kowal on 2017/4/20.
 */

public class FormatUtils
{
    public static Integer strToInt(String str)
    {
        if (str == null || "".equals(str))
            return 0;
        return Integer.parseInt(str);
    }

    public static String intToStr(Integer integer)
    {
        if (integer == null || "".equals(integer))
        {
            return null;
        }
        return String.valueOf(integer);
    }

    /**
     * 返回整数+小数点后6位
     */
    public static String latLng(Marker marker, String type)
    {
        String str;
        String result = "";
        if (null == marker || null == type || "".equals(type))
        {
            return "获取失败";
        } else
        {
            switch (type)
            {
                case "longitude":
                    str = marker.getPosition().longitude + "";
                    String[] inte = str.split("\\.");
                    result =  inte[0] + "." + inte[1].substring(0, 6);
                    break;
                case "latitude":
                    str = marker.getPosition().latitude + "";
                    String[] inte2 = str.split("\\.");
                    result =  inte2[0] + "." + inte2[1].substring(0, 6);
                    break;
            }
        }
        return result;
    }

    /**
     * M转换
     * @param size
     * @return
     */
    public static String formatDataSize(int size) {
        String ret = "";
        if (size < (1024 * 1024)) {
            ret = String.format("%dK", size / 1024);
        } else {
            ret = String.format("%.1fM", size / (1024 * 1024.0));
        }
        return ret;
    }
}