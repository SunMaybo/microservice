package com.juxinli;

import com.juxinli.common.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 数据抓取
 * Created by maybo on 17/7/18.
 */
@RestController
@RequestMapping("/spinner/data")
public class SpinnerController {

    @Autowired
    TwitterSpinnerService twitterSpinnerService;

    @Autowired
    FacebookSpinnerService facebookSpinnerService;

    @RequestMapping("/twitter")
    public JsonResponse getTwitterInfo(String uid) {
        Map<String,Object>dataMap=twitterSpinnerService.twitterInfo(uid);
        if (dataMap==null){
            return JsonResponse.createError("获取授权信息失败",null,401);
        }
        return JsonResponse.createData(twitterSpinnerService.twitterInfo(uid));
    }

    @RequestMapping("/facebook")
    public JsonResponse getFaceBookInfo(String uid) {
        return JsonResponse.createData(facebookSpinnerService.getInfo(uid));
    }
}
