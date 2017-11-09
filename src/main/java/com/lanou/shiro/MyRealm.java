package com.lanou.shiro;

import com.lanou.bean.SysUser;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dllo on 17/11/7.
 */
public class MyRealm extends AuthorizingRealm {

    @Override
    public String getName() {
        return "suibian";
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    //授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SysUser user =(SysUser) principalCollection.getPrimaryPrincipal();
        //获取user的用户id及各种信息

        List<String> preList = Arrays.asList("user:query","user:update");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        for (String per : preList) {
            info.addStringPermission(per);
        }

        return info;
    }

    //认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username = (String) authenticationToken.getPrincipal();

        if (!("zhangsan".equals(username))){
            throw new UnknownAccountException("用户名不正确");
        }

        String password = new String((char[])authenticationToken.getCredentials());
        if (!("123".equals(password))){
            throw new IncorrectCredentialsException("密码不正确!");
        }

        SysUser user = new SysUser();
        user.setUsername(username);
        user.setPassword(password);

        return new SimpleAuthenticationInfo(user,password,getName());
    }
}
