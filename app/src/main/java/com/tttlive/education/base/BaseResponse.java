package com.tttlive.education.base;

/**
 * Created by Iverson on 2016/12/23 下午8:59
 * 此类用于：返回数据的基类
 */

public class BaseResponse<DataType> {
    public static final boolean RESULT_CODE_SUCCESS = true;
    public static final boolean RESULT_CODE_TOKEN_EXPIRED = false;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    /**
     * 通用返回值属性
     */
    private int code;
    /**
     * 通用返回信息。
     */
    private String message;
    /**
     * 具体的内容。
     */
    private DataType data;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataType getData() {
        return data;
    }

    public void setData(DataType data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
