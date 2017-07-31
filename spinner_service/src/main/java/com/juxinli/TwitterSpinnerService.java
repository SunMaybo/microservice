package com.juxinli;

import com.juxinli.common.JsonObjectMapper;
import com.juxinli.common.JsonResponse;
import com.juxinli.spinner.CollectTemplate;
import com.juxinli.spinner.SpinnerResponse;
import okhttp3.Headers;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * twitter数据抓取服务启动线程池高效查询
 * Created by maybo on 17/7/18.
 */
@Service
public class TwitterSpinnerService {

    @Autowired
    private CollectTemplate collectTemplate;


    private ExecutorService pool = Executors.newCachedThreadPool();

    private Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private ApiTokenService apiTokenService;

    private String accountUrl = "https://api.twitter.com/1.1/account/settings.json";

    private String userUrl = "https://api.twitter.com/1.1/users/show.json";

    private String followerUrl = "https://api.twitter.com/1.1/followers/ids.json";

    private String friendisUrl = "https://api.twitter.com/1.1/friends/ids.json";

    private String favoritesUrl = "https://api.twitter.com/1.1/favorites/list.json";

    private String homeTimeLineUrl = "https://api.twitter.com/1.1/statuses/home_timeline.json";

    /**
     * 获取关于用户所有twitter数据信息
     * @param uid
     * @return
     */
    public Map<String, Object> twitterInfo(String uid) {
        Map<String, Object> data = new HashMap<>();
        String screenName = getScreenName(uid);
        if (screenName==null){
            return null;
        }
        Future<Map<String, Object>> userFuture = twitterUser(uid, screenName);
        Future<List<Map<String, Object>>> friendsFuture = twitterFriends(uid, screenName);
        Future<List<Map<String, Object>>> followFuture = twitterFollows(uid, screenName);
        Future<List<Map<String, Object>>> favoritesFuture = twitterUserFavorites(uid, screenName);
        Future<List<Map<String, Object>>> HomeTimeLinesFuture = twitterUserHomeTimeLine(uid, 200);
        try {
            data.put("user", userFuture.get());
            data.put("friends", friendsFuture.get());
            data.put("follows", followFuture.get());
            data.put("favorites", favoritesFuture.get());
            data.put("timeLies",HomeTimeLinesFuture.get());
        } catch (InterruptedException e) {
            logger.error("线程中断异常",e);
        } catch (ExecutionException e) {
            logger.error("线程执行异常",e);
        }
        return data;
    }

    /**
     * 通过账号信息获取用户的名字
     * @param uid
     * @return
     */
    private String getScreenName(String uid) {
        Map<String, Object> baseMap = getUserBaseInfo(uid);
        if (null == baseMap) {
            return null;
        } else {
            String screenName = baseMap.get("screen_name").toString();
            return screenName;
        }

    }

    /**
     * 获取用户基本信息
     * @param uid
     * @return
     */
    private Map<String, Object> getUserBaseInfo(String uid) {
        String jsonStr = apiTokenService.apiTwitterToken(uid, accountUrl, "GET");
        JsonResponse jsonResponse = (JsonResponse) JsonObjectMapper.readerValueAsObject(jsonStr, JsonResponse.class);
        if (null==jsonResponse){
            return null;
        }
        if (jsonResponse.getCode() != 200) {
            return null;
        }
        Headers.Builder headBuilder = new Headers.Builder();
        headBuilder.add("Authorization", jsonResponse.getData().toString());
        SpinnerResponse response = collectTemplate.get(accountUrl, headBuilder.build());
        if (response.getCode() == 200) {

            Map dataMap = (Map) JsonObjectMapper.readerValueAsObject(response.getData(), Map.class);
            return dataMap;


        }
        return null;
    }

    /**
     * 获取用户信息通过用户的id
     * @param ids
     * @param uid
     * @return
     */
    private List<Map<String, Object>> getUserByIds(List<Object> ids, String uid) {
        //friendsList获取
        List<Map<String, Object>> friendList = new ArrayList<>();
        List<Future<Map<String, Object>>> futureFriendsList = new ArrayList<>();
        if (ids != null) {
            for (Object id : ids) {
                Future<Map<String, Object>> friendfuture = pool.submit(new Callable<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> call() throws Exception {
                        return getUserInfoById(uid, id.toString());
                    }
                });
                futureFriendsList.add(friendfuture);
            }

        }

        for (Future<Map<String, Object>> future : futureFriendsList) {
            try {
                friendList.add(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return friendList;
    }

    /**
     * 通过名字获取用户详细信息
     * @param uid
     * @param screenName
     * @return
     */
    private Future<Map<String, Object>> twitterUser(String uid, String screenName) {
        Future<Map<String, Object>> future = pool.submit(new Callable<Map<String, Object>>() {
            @Override
            public Map<String, Object> call() throws Exception {
                return getUserInfo(uid, screenName);
            }
        });


        return future;
    }

    /**
     *获取用户时间轴信息
     * @param uid
     * @param count
     * @return
     */
    private Future<List<Map<String, Object>>> twitterUserHomeTimeLine(String uid, int count) {
        Future<List<Map<String, Object>>> future = pool.submit(new Callable<List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> call() throws Exception {
                return getHomeTimeLinesInfo(uid, count);
            }
        });


        return future;
    }

    /**
     * 获取用户喜欢的推文
     * @param uid
     * @param screenName
     * @return
     */
    private Future<List<Map<String, Object>>> twitterUserFavorites(String uid, String screenName) {
        Future<List<Map<String, Object>>> future = pool.submit(new Callable<List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> call() throws Exception {
                return getFavoritesInfo(uid, screenName);
            }
        });


        return future;
    }

    /**
     * 获取好友信息
     * @param uid
     * @param screenName
     * @return
     */
    private Future<List<Map<String, Object>>> twitterFriends(String uid, String screenName) {


        Future<List<Map<String, Object>>> future = pool.submit(new Callable<List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> call() throws Exception {
                Map<String, Object> friendMap = getFriendsIds(uid, screenName);
                List<Object> ids = null;
                if (null != friendMap) {
                    ids = (List<Object>) friendMap.get("ids");
                }

                return getUserByIds(ids, uid);
            }
        });


        return future;
    }

    /**
     * 获取关注信息
     * @param uid
     * @param screenName
     * @return
     */
    private Future<List<Map<String, Object>>> twitterFollows(String uid, String screenName) {


        Future<List<Map<String, Object>>> future = pool.submit(new Callable<List<Map<String, Object>>>() {
            @Override
            public List<Map<String, Object>> call() throws Exception {
                Map<String, Object> friendMap = getFollowersIds(uid, screenName);
                List<Object> ids = null;
                if (null != friendMap) {
                    ids = (List<Object>) friendMap.get("ids");
                }

                return getUserByIds(ids, uid);
            }
        });

        return future;
    }

    private Map<String, Object> getFriendsIds(String uid, String screenName) {
        return getIds(uid, screenName, friendisUrl);
    }

    private Map<String, Object> getFollowersIds(String uid, String screenName) {

        return getIds(uid, screenName, followerUrl);
    }

    private Map<String, Object> getIds(String uid, String screenName, String url) {
        Map<String, String> params = new HashMap<>();
        params.put("screen_name", screenName);
        params.put("count",5000+"");
        String jsonStr = apiTokenService.apiTwitterToken(uid, url, "GET", params);
        JsonResponse jsonResponse = (JsonResponse) JsonObjectMapper.readerValueAsObject(jsonStr, JsonResponse.class);
        if (null==jsonResponse){
            return null;
        }
        if (jsonResponse.getCode() != 200) {
            return null;
        }
        Headers.Builder headBuilder = new Headers.Builder();
        headBuilder.add("Authorization", jsonResponse.getData().toString());
        SpinnerResponse response = collectTemplate.get(url + "?screen_name=" + screenName+"&count=5000", headBuilder.build());
        if (response.getCode() == 200) {
            Map dataMap = null;

            dataMap = (Map) JsonObjectMapper.readerValueAsObject(response.getData(), Map.class);

            return dataMap;
        }
        return null;
    }


    private Map<String, Object> getUserInfoById(String uid, String id) {

        if (id != null) {
            Map<String, String> params = new HashMap<>();
            params.put("user_id", id);
            String jsonStr = apiTokenService.apiTwitterToken(uid, userUrl, "GET", params);
            JsonResponse jsonResponse = (JsonResponse) JsonObjectMapper.readerValueAsObject(jsonStr, JsonResponse.class);
            if (null==jsonResponse){
                return null;
            }
            if (jsonResponse.getCode() != 200) {
                return null;
            }
            Headers.Builder headBuilder = new Headers.Builder();
            headBuilder.add("Authorization", jsonResponse.getData().toString());
            SpinnerResponse response = collectTemplate.get(userUrl + "?user_id=" + id, headBuilder.build());
            if (response.getCode() == 200) {
                Map dataMap = null;

                dataMap = (Map) JsonObjectMapper.readerValueAsObject(response.getData(), Map.class);

                return dataMap;
            }
            return null;
        }
        return null;
    }


    private Map<String, Object> getUserInfo(String uid, String screenName) {

        if (screenName != null) {
            Map<String, String> params = new HashMap<>();
            params.put("screen_name", screenName);
            String jsonStr = apiTokenService.apiTwitterToken(uid, userUrl, "GET", params);
            JsonResponse jsonResponse = (JsonResponse) JsonObjectMapper.readerValueAsObject(jsonStr, JsonResponse.class);
            if (null==jsonResponse){
                return null;
            }
            if (jsonResponse.getCode() != 200) {
                return null;
            }
            Headers.Builder headBuilder = new Headers.Builder();
            headBuilder.add("Authorization", jsonResponse.getData().toString());
            SpinnerResponse response = collectTemplate.get(userUrl + "?screen_name=" + screenName, headBuilder.build());
            if (response.getCode() == 200) {
                Map dataMap = null;

                dataMap = (Map) JsonObjectMapper.readerValueAsObject(response.getData(), Map.class);

                return dataMap;
            }
            return null;
        }
        return null;
    }

    private List<Map<String, Object>> getFavoritesInfo(String uid, String screenName) {

        if (screenName != null) {
            Map<String, String> params = new HashMap<>();
            params.put("screen_name", screenName);
            String jsonStr = apiTokenService.apiTwitterToken(uid, favoritesUrl, "GET", params);
            JsonResponse jsonResponse = (JsonResponse) JsonObjectMapper.readerValueAsObject(jsonStr, JsonResponse.class);
            if (null==jsonResponse){
                return null;
            }
            if (jsonResponse.getCode() != 200) {
                return null;
            }
            Headers.Builder headBuilder = new Headers.Builder();
            headBuilder.add("Authorization", jsonResponse.getData().toString());
            SpinnerResponse response = collectTemplate.get(favoritesUrl + "?screen_name=" + screenName, headBuilder.build());
            if (response.getCode() == 200) {
                List<Map<String, Object>> dataMap = null;

                dataMap = (List<Map<String, Object>>) JsonObjectMapper.readerValueAsObject(response.getData(), List.class);

                return dataMap;
            }
            return null;
        }
        return null;
    }

    private List<Map<String, Object>> getHomeTimeLinesInfo(String uid, int count) {

        Map<String, String> params = new HashMap<>();
        params.put("count", count + "");
        String jsonStr = apiTokenService.apiTwitterToken(uid, homeTimeLineUrl, "GET", params);
        JsonResponse jsonResponse = (JsonResponse) JsonObjectMapper.readerValueAsObject(jsonStr, JsonResponse.class);
        if (null==jsonResponse){
            return null;
        }
        if (jsonResponse.getCode() != 200) {
            return null;
        }
        Headers.Builder headBuilder = new Headers.Builder();
        headBuilder.add("Authorization", jsonResponse.getData().toString());
        SpinnerResponse response = collectTemplate.get(homeTimeLineUrl + "?count=" + count, headBuilder.build());
        if (response.getCode() == 200) {
            List<Map<String, Object>> dataMap = null;

            dataMap = (List<Map<String, Object>>) JsonObjectMapper.readerValueAsObject(response.getData(), List.class);

            return dataMap;
        }
        return null;

    }

}
