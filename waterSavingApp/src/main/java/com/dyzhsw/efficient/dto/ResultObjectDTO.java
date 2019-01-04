package com.dyzhsw.efficient.dto;


/**
 * @Author: pjx
 * @Date: 2018/12/12 14:13
 * @Version 1.0
 */
public class ResultObjectDTO {
    /**状态码**/
    private Integer stateCode;
    /**提示信息**/

    private String message;

    public Integer getStateCode() {
        return stateCode;
    }

    public void setStateCode(Integer stateCode) {
        this.stateCode = stateCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    /** 返回数据 **/
    private Object object;
}
