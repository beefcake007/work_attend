package org.fanye.common.security;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.fanye.user.entity.Permission;
import org.fanye.user.entity.Role;
import org.fanye.user.entity.User;
import org.fanye.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;


public class MyRealm extends AuthorizingRealm{

    @Autowired
    private UserService userService;


    /**
     * ÊÚÈ¨
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        String username = (String)principalCollection.getPrimaryPrincipal();
        User user = userService.findUserByUserName(username);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        for(Role role :user.getRoleList()){
            authorizationInfo.addRole(role.getRole());
            for(Permission permission :role.getPermissionList()){
                authorizationInfo.addStringPermission(permission.getPermission());
            }
        }
        return authorizationInfo;
    }


    /**
     * ÈÏÖ¤ µÇÂ¼
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToke = (UsernamePasswordToken)authenticationToken;
        String username =  usernamePasswordToke.getUsername();
        User user = userService.findUserByUserName(username);
        if(user==null){
            return null;
        }else {
            AuthenticationInfo info = new SimpleAuthenticationInfo(user.getUsername(), user.getPassword(), getName());
            SecurityUtils.getSubject().getSession().setAttribute("userInfo",user);
            return info;
        }

    }
}
