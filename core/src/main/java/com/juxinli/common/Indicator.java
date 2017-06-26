package com.juxinli.common;

import com.juxinli.util.UUIDUtil;
import org.apache.log4j.Logger;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 基本监控指标的基础类
 * Created by maybo on 17/6/20.
 */
public class Indicator {

    public static final String REST_TEMPLATE_TYPE="http_rest_template";

    public static final String HTTP_MVC_PRE="http_input";

    public static final String RABBIT_TEMPLATE="rabbitmq_template";

    public static final int FAIL=500;

    public static  final int SUCCESS=200;

    private long time=new Date().getTime();//时间
    private String contextId;//上下文编号
    private String type="DEFAULT";//类型
    private static volatile String name;//名字
    private String content;//内容
    private int status = 200;//状态码
    private String error;//异常描述
    private int depth = 1;//所处深度,表述服务所在位置
    private Map<String,Object> extend = new HashMap<>();//扩展
    private String index="";
    private static Indicator indicator=null;

    public Indicator(String traceCode) {

         Logger logger = Logger.getLogger(this.getClass());

        if (null == traceCode) {
            throw new NullPointerException("全局监控的编号不可以为空!");
        }

        String decodeId = null;
        try {
            decodeId = new String(Base64Utils.decodeFromString(traceCode), "utf-8");
            String[] decodeIdArray=decodeId.split("_");

            if (decodeIdArray.length<2){
                throw  new Exception("traceCode的编码格式不合法!");

            }else {
                this.contextId=decodeIdArray[0];
                this.depth=Integer.valueOf(decodeIdArray[1]);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }


    }

    public Indicator(String ContextId, int depth){
        this.contextId=content;
        this.depth=depth;
    }

    public Indicator() {

        String id= UUIDUtil.getOrderIdByUUId()+"_"+1;
        try {
            this.contextId = Base64Utils.encodeToString(id.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }



    /**
     * 为服务全局追踪监控编号
     *
     * @return
     */
    public String traceCode() {
        try {
            return Base64Utils.encodeToString((this.contextId+"_"+depth).getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContextId() {
        return contextId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Map<String,Object> getExtend() {
        return extend;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "Indicator{" +
                "time=" + time +
                ", contextId='" + contextId + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", depth=" + depth +
                ", extend=" + extend +
                '}';
    }

}
