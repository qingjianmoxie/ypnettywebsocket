package com.lzhpo.ypnettywebsocket.vo;

import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.Serializable;

/**
 * @author lzhpo
 */
public class ResultData<V> implements Serializable {

    private static final long serialVersionUID = 6674999278660577990L;

    private int code = HttpResponseStatus.OK.code();
    private String msg = "success";
    private V data;

    public ResultData() {
    }

    public ResultData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultData(V data) {
        this.data = data;
    }

    public ResultData(int code, String msg, V data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public V getData() {
        return data;
    }

    public void setData(V data) {
        this.data = data;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 建造者模式
     * @param <V>
     */
    public static class Builder<V> {

        private ResultData resultData;

        public Builder() {
            resultData = new ResultData();
        }

        public Builder code(int code) {
            resultData.code = code;
            return this;
        }

        public Builder msg(String msg) {
            resultData.msg = msg;
            return this;
        }

        public Builder data(V data) {
            resultData.data = data;
            return this;
        }

        public ResultData builded() {
            return resultData;
        }
    }

}
