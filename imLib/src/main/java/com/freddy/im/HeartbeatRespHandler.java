package com.freddy.im;

import com.freddy.im.netty.NettyTcpClient;
import com.network.message.web.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       HeartbeatRespHandler.java</p>
 * <p>@PackageName:     com.freddy.im</p>
 * <b>
 * <p>@Description:     心跳消息响应处理handler</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/08 01:08</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class HeartbeatRespHandler extends ChannelInboundHandlerAdapter {

    private NettyTcpClient imsClient;

    public HeartbeatRespHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message.NetMessage heartbeatRespMsg = (Message.NetMessage) msg;
        if (heartbeatRespMsg == null ) {
            return;
        }

        Message.NetMessage heartbeatMsg = imsClient.getHeartbeatMsg();
        if (heartbeatMsg == null ) {
            return;
        }

        int msgId = Integer.parseInt(heartbeatMsg.getMessageid());
        if (msgId == Integer.parseInt(heartbeatRespMsg.getMessageid())) {
            System.out.println("收到服务端心跳响应消息，message=" + heartbeatRespMsg);
        } else {
            // 消息透传
            ctx.fireChannelRead(msg);
        }
    }
}
