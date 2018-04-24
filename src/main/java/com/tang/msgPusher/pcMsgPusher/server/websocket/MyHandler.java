package com.tang.msgPusher.pcMsgPusher.server.websocket;

import com.tang.constants.MsgPusherConstant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * pc端消息推送-使用websocket实现
 *
 * 轮询：客户端定时向服务器发送Ajax请求，服务器接到请求后马上返回响应信息并关闭连接。
 * 优点：后端程序编写比较容易。
 * 缺点：请求中有大半是无用，浪费带宽和服务器资源。  实例：适于小型应用。
 *
 * 长轮询：客户端向服务器发送Ajax请求，服务器接到请求后hold住连接，直到有新消息才返回响应信息并关闭连接，
 * 客户端处理完响应信息后再向服务器发送新的请求。
 * 优点：在无消息的情况下不会频繁的请求，耗费资小。
 * 缺点：服务器hold连接会消耗资源，返回数据顺序无保证，难于管理维护。 Comet异步的ashx， 实例：WebQQ、Hi网页版、Facebook IM。
 *
 * 长连接：在页面里嵌入一个隐蔵iframe，将这个隐蔵iframe的src属性设为对一个长连接的请求或是采用xhr请求，
 * 服务器端就能源源不断地往客户端输入数据。
 * 优点：消息即时到达，不发无用请求；管理起来也相对便。
 * 缺点：服务器维护一个长连接会增加开销。  实例：Gmail聊天
 *
 * Flash Socket：在页面中内嵌入一个使用了Socket类的 Flash 程序,
 * JavaScript通过调用此Flash程序提供的Socket接口与服务器端的Socket接口进行通信，
 * JavaScript在收到服务器端传送的信息后控制页面的显示。
 * 优点：实现真正的即时通信，而不是伪即时。
 * 缺点：客户端必须安装Flash插件；非HTTP协议，无法自动穿越防火墙。实例：网络互动游戏。
 *
 * Websocket: WebSocket是HTML5开始提供的一种浏览器与服务器间进行全双工通讯的网络技术。
 * 依靠这种技术可以实现客户端和服务器端的长连接，双向实时通信。
 * 特点: 事件驱动  异步  使用ws或者wss协议的客户端socket  能够实现真正意义上的推送功能
 * 缺点：少部分浏览器不支持，浏览器支持的程度与方式有区别。
 *
 * websocket允许通过JavaScript建立与远程服务器的连接，从而实现客户端与服务器间双向的通信。
 * 在websocket中有两个方法：
 * 1、send() 向远程服务器发送数据
 * 2、close() 关闭该websocket链接
 *
 * websocket同时还定义了几个监听函数:
 * 1、onopen 当网络连接建立时触发该事件
 * 2、onerror 当网络发生错误时触发该事件
 * 3、onclose 当websocket被关闭时触发该事件
 * 4、onmessage 当websocket接收到服务器发来的消息的时触发的事件，也是通信中最重要的一个监听事件。msg.data
 *
 * websocket还定义了一个readyState属性，这个属性可以返回websocket所处的状态：
 * 1、CONNECTING(0) websocket正尝试与服务器建立连接
 * 2、OPEN(1) websocket与服务器已经建立连接
 * 3、CLOSING(2) websocket正在关闭与服务器的连接
 * 4、CLOSED(3) websocket已经关闭了与服务器的连接
 *
 * websocket的url开头是ws，如果需要ssl加密可以使用wss，
 * 当我们调用websocket的构造方法构建一个websocket对象（new WebSocket(url)）的之后，就可以进行即时通信了。
 *
 * 实现Websocket建立连接、发送消息、断开连接等时候的处理类
 * a.在afterConnectionEstablished连接建立成功之后，记录用户的连接标识，便于后面发信息，这里我是记录将id记录在Map集合中。
 * b.在handleTextMessage中可以对H5 Websocket的send方法进行处理
 * c.sendMessageToUser向指定用户发送消息，传入用户标识和消息体
 * d.sendMessageToAllUsers向左右用户广播消息，只需要传入消息体
 * e.handleTransportError连接出错处理，主要是关闭出错会话的连接，和删除在Map集合中的记录
 * f.afterConnectionClosed连接已关闭，移除在Map集合中的记录。
 * g.getClientId我自己封装的一个方法，方便获取用户标识
 *
 * @author: tangYiLong
 * @create: 2018-04-17 16:07
 **/
@Component
public class MyHandler extends TextWebSocketHandler {
    private static final Log LOGGER = LogFactory.getLog(MyHandler.class);

    //在线用户列表
    private static final Map<Integer, WebSocketSession> users;

    static {
        users = new HashMap<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        System.out.println("成功建立连接");
        Integer userId = getClientId(session);
        System.out.println(userId);
        if (userId != null) {
            users.put(userId, session);
            session.sendMessage(new TextMessage("成功建立socket连接"));
            System.out.println(userId);
            System.out.println(session);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        TextMessage returnMessage = new TextMessage(message.getPayload()+" received at server");
        session.sendMessage(returnMessage);
    }

    /**
     * 发送信息给指定用户
     * @param clientId
     * @param message
     * @return
     */
    public boolean sendMessageToUser(Integer clientId, TextMessage message) {
        if (users.get(clientId) == null) return false;
        WebSocketSession session = users.get(clientId);
        System.out.println("sendMessage:" + session);
        if (!session.isOpen()) return false;
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    /**
     * 广播信息
     * @param message
     * @return
     */
    public boolean sendMessageToAllUsers(TextMessage message) {
        boolean allSendSuccess = true;
        Set<Integer> clientIds = users.keySet();
        WebSocketSession session = null;
        for (Integer clientId : clientIds) {
            try {
                session = users.get(clientId);
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                allSendSuccess = false;
            }
        }

        return  allSendSuccess;
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        System.out.println("连接出错");
        users.remove(getClientId(session));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("连接已关闭：" + status);
        users.remove(getClientId(session));
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 获取用户标识
     * @param session
     * @return
     */
    private Integer getClientId(WebSocketSession session) {
        try {
            Integer clientId = (Integer) session.getAttributes().get(MsgPusherConstant.WEBSOCKET_CLIENT_ID);
            return clientId;
        } catch (Exception e) {
            return null;
        }
    }
}
