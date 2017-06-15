package com.sby.practice;

import java.util.List;

/**
 * Created by kowal on 2017/4/28.
 */

public class ArrayLi
{
    private static List<Integer> list;

    public static List<Integer> getList()
    {
        return list;
    }

    public static void add(Integer integer)
    {
        list.add(integer);
    }

}
