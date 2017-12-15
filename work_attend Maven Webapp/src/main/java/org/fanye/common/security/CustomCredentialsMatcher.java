package org.fanye.common.security;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.fanye.util.SecurityUtil;

/**
 * 自定义密码验证
 * @author fanye
 *
 */
public class CustomCredentialsMatcher extends SimpleCredentialsMatcher{

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {

        try {
            UsernamePasswordToken usertoken = (UsernamePasswordToken) token;
            String password = String.valueOf(usertoken.getPassword());
            Object tokenCredentials = SecurityUtil.encrptyPassword(password);
            Object accountCredentials =getCredentials(info);
            return equals(tokenCredentials,accountCredentials);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
