package org.fanye.user.service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.fanye.user.dao.UserMapper;
import org.fanye.user.entity.User;
import org.fanye.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserMapper userMapper;

	/**
	 * 根据用户名查询用户
	 */
	@Override
    public User findUserByUserName(String username) {
        User user=null;
        try {
             user =userMapper.selectByUserName(username);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return user;
    }

	@Override
    public void createUser(User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        user.setPassword(SecurityUtil.encrptyPassword(user.getPassword()));

        userMapper.insertSelective(user);
    }
}
