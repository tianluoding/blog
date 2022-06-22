package com.blog.resp;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回类
 */
@Data
public class CommonResp<T> implements Serializable {
    /**
     * success 1 error 0
     */
    private Integer code;
    /**
     * error message
     */
    private String msg;
    /**
     * 数据
     */
    private T data;
    /**
     * 动态数据
     */
    private Map map = new HashMap();

    public static <T> CommonResp<T> success(T object){
        CommonResp<T> resp = new CommonResp<>();
        resp.data = object;
        resp.code = 1;
        return resp;
    }

    public static <T> CommonResp<T> error(String msg){
        CommonResp<T> resp = new CommonResp<>();
        resp.code = 0;
        resp.msg = msg;
        return resp;
    }
}
