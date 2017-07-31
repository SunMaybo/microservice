package com.juxinli.service;

import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

/**
 * Created by maybo on 17/7/7.
 */
public class FaceBookDemo {
    public static void main(String[] args) {

        Facebook facebook = new FacebookTemplate("", "myapp");
        User profile = facebook.userOperations().getUserProfile("4");

    }
}
