package com.juxinli.common;

import java.util.Date;

/**
 * Created by maybo on 17/6/23.
 */
public class Message {

    public int status=200;
    public String type;
    public long time=new Date().getTime();
    public String traceCode;
    public Object object;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTraceCode() {
        return traceCode;
    }

    public void setTraceCode(String traceCode) {
        this.traceCode = traceCode;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Object getObject() {
        return object;
    }

    @Override
    public String toString() {
        return "Message{" +
                "status=" + status +
                ", type='" + type + '\'' +
                ", time=" + time +
                ", traceCode='" + traceCode + '\'' +
                ", t=" + object +
                '}';
    }
}
