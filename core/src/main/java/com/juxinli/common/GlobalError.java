package com.juxinli.common;

/**
 * Created by maybo on 17/7/27.
 */
public enum GlobalError implements Error {


    SYSTEM_RUNTIME_EXCEPTION(5000,"系统运行异常!"),
    SYSTEM_RESPONSE_TIMEOUT_EXCEPTION(5005,"系统响应超时!"),
    SYSTEM_FILE_READ_EXCEPTION(5010,"系统文件读异常!"),
    SYSTEM_FILE_WRITE_EXCEPTION(5015,"系统文件写异常!"),
    SYSTEM_OUT_MEMORY_EXCEPTION(5020,"系统内存泄露!"),


    DB_CONNECTION_TIMEOUT_EXCEPTION(4000,"数据库连接超时异常!"),
    DB_READ_EXCEPTION(4005,"数据库读异常!"),
    DB_WRITE_EXCEPTION(4010,"数据库写异常!"),
    DB_EXCEPTION(4015,"数据库调用异常!"),
    DB_DOWN_EXCEPTION(4020,"数据库异常关闭!"),


    REMOTE_METHOD_EXCEPTION(3000,"接口调用服务器异常!"),
    REMOTE_METHOD_TIMEOUT_EXCEPTION(3005,"接口调用超时!"),
    REMOTE_METHOD_PARAM_PARSED_EXCEPTION(3010,"接口调用参数解析异常!"),

    CALL_REMOTE_FORMAT_EXCEPTION(3015,"调用接口的参数不合法!"),
    CALL_REMOTE_AUTH_EXCEPTION(3020,"接口调用授权失败!"),
    CALL_REMOTE_AUTH_OVERDUE_EXCEPTION(3020,"接口调用权限过期!"),
    CALL_REMOTE_404_EXCEPTION(3025,"接口路径错误404!"),

    MSG_SEND_EXCEPTION(3030,"消息发送异常!"),
    MSG_PARSED_EXCEPTION(3035,"消息解析异常!"),


    GET_PARAM_PARSED_EXCEPTION(3040,"获取的参数解析异常!"),

    GET_PARAM_VALID_EXCEPTION(3045,"获取的参数校验异常!");



    GlobalError(int status, String description) {
        this.status = status;
        this.description = description;
    }

    int status;
    String description;

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getStatus() {
        return status;
    }

}
