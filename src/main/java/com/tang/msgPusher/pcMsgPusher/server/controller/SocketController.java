package com.tang.msgPusher.pcMsgPusher.server.controller;

import com.tang.constants.MsgPusherConstant;
import com.tang.msgPusher.pcMsgPusher.server.websocket.MyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @description: description
 * @author: tangYiLong
 * @create: 2018-04-19 11:43
 **/
@Controller
public class SocketController {
    @Autowired
    MyHandler handler;


    @RequestMapping("/login/{userId}")
    public String login(HttpSession session, @PathVariable("userId") Integer userId, HttpServletResponse response) {
        System.out.println("登录接口,userId=" + userId);
        session.setAttribute(MsgPusherConstant.WEBSOCKET_CLIENT_ID, userId);
        System.out.println(session.getAttribute("userId"));
        //同源策略规定，浏览器的ajax只能访问跟它的HTML页面同源（相同域名或IP）的资源
        //若采用将页面拷到本地直接打开的方式，则会报跨域错误，因此要加入以下设置（反之，如果是从浏览器发送http请求的方式，则不需要设置）
        response.setHeader("Access-Control-Allow-Origin", "*");
        return "testWebsocket";
    }

    @RequestMapping("/message/{userId}")
    public @ResponseBody
    String sendMessage(@PathVariable Integer userId) {
        boolean hasSend = handler.sendMessageToUser(userId, new TextMessage("发送一条消息"));
        System.out.println(hasSend);
        return "isSent : " + hasSend;
    }
}
