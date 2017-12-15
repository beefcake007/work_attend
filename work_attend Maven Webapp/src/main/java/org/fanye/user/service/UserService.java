package org.fanye.user.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.fanye.user.entity.User;


public interface UserService {

	User findUserByUserName(String username);

    void createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException;
}
