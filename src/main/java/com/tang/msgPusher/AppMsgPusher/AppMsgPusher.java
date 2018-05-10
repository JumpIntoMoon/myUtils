package com.tang.msgPusher.AppMsgPusher;

/**
 * 一、轮询法：
 * 这种方法最简单，Client每过一段时间向Server请求一次数据。
 * 优缺点很明显，优点是实现简单；缺点是间隔时间不好控制，并且消耗大（电量、流量）。
 * <p>
 * 二、长连接法：
 * 还是从socket入手，Client使用socket连接Server，并且保持socket连接，Server随时可以通过这个socket发送数据给Client。
 * 优点：最有效，客户端设备消耗比第一种小（设备应该从系统层对socket的长连接做优化，socket链接维护成本从客户端来讲应该是小于频繁的http请求的）；
 * 缺点：服务端压力大，每一个设备都需要一个socket连接。
 *
 * @author: tangYiLong
 * @create: 2018-04-17 15:53
 **/
public class AppMsgPusher {
}
