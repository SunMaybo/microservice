package com.juxinli.spinner;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by maybo on 17/6/28.
 */
public class ResponseProcessorFactory {
    private static final ExecutorService POOL = Executors.newCachedThreadPool();

    public static void execute(SpinnerResponse spinnerResponse, final ResponseProcessor responseProcessor){
        POOL.submit(new Runnable() {
            @Override
            public void run() {
                responseProcessor.process(spinnerResponse);
            }
        });
    }
}
