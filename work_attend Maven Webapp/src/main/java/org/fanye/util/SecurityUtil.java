package org.fanye.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Encoder;

public class SecurityUtil {

	public static String encrptyPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest md5=MessageDigest.getInstance("MD5");
		BASE64Encoder base64en=new BASE64Encoder();
		return base64en.encode(md5.digest(password.getBytes("utf-8")));
	}
	
	public static boolean checkPassword(String inputPwd,String dbPwd) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		String result=encrptyPassword(inputPwd);
		if(result.equals(dbPwd)){
			return true;
		}else{
			return false;
		}
	}
	
	public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String str=SecurityUtil.encrptyPassword("123456");
		System.out.println(str);
	}
}
