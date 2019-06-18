package com.yangjia.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomRealm extends AuthorizingRealm {
    Map<String, String> userMap = new HashMap<String, String>();

    {
        userMap.put("mark", "73bea81c6c06bacab41a995495239545");
        super.setName("customRealm");
    }

    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        String username = (String) principals.getPrimaryPrincipal();
        //从数据库获取角色数据
        Set<String> roles = getRolesByUserName(username);
        //从数据库获取权限数据
        Set<String> permit = getPermitByUserName(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setStringPermissions(permit);
        simpleAuthorizationInfo.setRoles(roles);

        return simpleAuthorizationInfo;
    }

    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        //从主体传过来的认证信息中获得用户名
        String userName = (String) token.getPrincipal();
        //2.通过用户名到数据库中获取凭证
        String pwd = getPasswordByUserName(userName);
        if (pwd == null) {
            return null;
        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo("mark", pwd, "customRealm");
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("mark"));
        return authenticationInfo;
    }

    private String getPasswordByUserName(String userName) {
        return userMap.get(userName);
    }

    /**
     * 模拟数据库获取角色数据
     *
     * @param userName
     * @return
     */
    private Set<String> getRolesByUserName(String userName) {
        Set<String> sets = new HashSet<String>();
        sets.add("admin");
        sets.add("user");
        return sets;
    }

    /**
     * 模拟数据库获取权限数据
     *
     * @param userName
     * @return
     */
    private Set<String> getPermitByUserName(String userName) {
        Set<String> sets = new HashSet<String>();
        sets.add("user:add");
        sets.add("user:delete");
        return sets;
    }

    public static void main(String[] args) {
        Md5Hash MD5 = new Md5Hash("123456", "mark");
        System.out.println(MD5.toString());
    }
}

