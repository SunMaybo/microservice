package com.juxinli.service;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import twitter4j.*;
import twitter4j.api.FavoritesResources;
import twitter4j.api.FriendsFollowersResources;
import twitter4j.api.UsersResources;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.util.Iterator;

/**
 * Created by maybo on 17/6/29.
 */
public class TwitterDemo {
    public static void main(String[] args) throws TwitterException {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey("Le1e95r2eaq6wWeCxrRntH1Cf");
        configurationBuilder.setOAuthConsumerSecret("xt1z9AaULbnXA8eLskEQGPthka3xQFm6aZFUmp4js5LxGyDYrK");
        configurationBuilder.setJSONStoreEnabled(true);
      /*  configurationBuilder.setHttpReadTimeout(120000);
        configurationBuilder.setHttpConnectionTimeout(120000);*/

        Configuration configuration = configurationBuilder.build();
        Twitter twitter = new TwitterFactory(configuration).getInstance();
        //requestToken = twitter.getOAuthRequestToken();
        //accessToken = twitter.getOAuthAccessToken(requestToken);
        AccessToken accessToken = new AccessToken("880315414659706881-McxWXeyyOlO7FwTu1GFCGkpaJUkAeHF", "L5dC1I4DvMd2pPjht8yoeW8QZ9B8cqMOer56QQ06quqv7");
        System.out.println("AccessToken:" + accessToken);
        twitter.setOAuthAccessToken(accessToken);
        int page = 1;
        ResponseList<User> users;

        Query query =new Query();
        PagableResponseList<User>pagableResponseList;


       // do {
            UsersResources usersResources = twitter.users();
            User user=  twitter.showUser("Ethan930147677");
        //ResponseList<Status> favorites = twitter.favorites().getFavorites();

            //  users = twitter.searchUsers("Reuters", page);
           // ResponseList<Status> responseList = twitter.getUserTimeline("Reuters");




/*

            for (User user : pagableResponseList) {
                if (user.getStatus() != null) {
                    System.out.println("@" + user.getScreenName() + " - " + user.getStatus().getText());
                } else {

                    // the user is protected
                    System.out.println("@" + user.getScreenName());
                }
            }
            page++;
        } while (pagableResponseList.size() != 0 && page < 50);
        UsersResources usersResources = twitter.users();
        FriendsFollowersResources friendsFollowersResources = twitter.friendsFollowers();
*/

      /*  try {
            PagableResponseList<User> pagableResponseList = usersResources.getBlocksList();
            Iterator<User> userIterator = pagableResponseList.iterator();
            while (userIterator.hasNext()) {
                User user = userIterator.next();
                System.out.println(user.toString());
            }
        } catch (TwitterException e) {
            e.printStackTrace();
        }*/

        }

}
