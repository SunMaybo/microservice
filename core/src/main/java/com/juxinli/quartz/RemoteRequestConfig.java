package com.juxinli.quartz;


/**
 * Created by maybo on 17/5/14.
 */
public class RemoteRequestConfig {


    private BordService boardService;

    private Class<?> aClass;

    public void setaClass(Class<?> aClass) {
        this.aClass = aClass;
    }

    public Class<?> getaClass() {
        return aClass;
    }

    public void setBoardService(BordService boardService) {
        this.boardService = boardService;
    }

    public BordService getBoardService() {
        return boardService;
    }
}
