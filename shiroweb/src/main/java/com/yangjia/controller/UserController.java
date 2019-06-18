package com.yangjia.controller;

import com.yangjia.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/***
 * http://www.flvcd.com/
 *  .--,       .--,
 * ( (  \.---./  ) )
 *  '.__/o   o\__.'
 *     {=  ^  =}
 *      >  -  <
 *     /       \
 *    //       \\
 *   //|   .   |\\
 *   "'\       /'"_.-~^`'-.
 *      \  _  /--'         `
 *    ___)( )(___
 *   (((__) (__)))   
 *
 * 高山仰止,景行行止.虽不能至,心向往之。
 *
 *@author : Darius
 *@Date   :Created in 2019/6/17 22:19
 *
 */
@Controller
public class UserController {
    @RequestMapping(value = "/sublogin", method = RequestMethod.POST,
            produces = "application/json;charset=utf-8")
    @ResponseBody
    public String sublogin(User user) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),
                user.getPwdpass());

        try {
            subject.login(token);
        } catch (AuthenticationException e) {
            return e.getMessage();
        }

        return "登陆成功";
    }
}
