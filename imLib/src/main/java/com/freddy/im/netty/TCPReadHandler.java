package com.freddy.im.netty;

import com.alibaba.fastjson.JSONObject;
import com.freddy.im.IMSConfig;
import com.freddy.im.bean.MessageIdType;
import com.freddy.im.bean.MessageTypes;
import com.network.message.web.Message;

import java.util.UUID;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.internal.StringUtil;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       TCPReadHandler.java</p>
 * <p>@PackageName:     com.freddy.im.netty</p>
 * <b>
 * <p>@Description:     消息接收处理handler</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/07 21:40</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class TCPReadHandler extends ChannelInboundHandlerAdapter {

    private NettyTcpClient imsClient;

    public TCPReadHandler(NettyTcpClient imsClient) {
        this.imsClient = imsClient;
    }

    // 断线的时候重连
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.err.println("TCPReadHandler channelInactive()");
        Channel channel = ctx.channel();
        if (channel != null) {
            channel.close();
            ctx.close();
        }

        // 触发重连
        imsClient.resetConnect(false);
    }


    // 报错的时候重连
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        System.err.println("TCPReadHandler exceptionCaught()");
        Channel channel = ctx.channel();
        if (channel != null) {
            channel.close();
            ctx.close();
        }

        // 触发重连
        imsClient.resetConnect(false);
    }

    //接收到消息的时候 处理
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message.NetMessage message = (Message.NetMessage) msg;
        if (message == null) {
            return;
        }

        int msgType = Integer.parseInt(message.getMessageid());
        if (msgType == imsClient.getServerSentReportMsgType()) {
            // 因为后台没有 返回消息状态 所以不做更多判断
//            int statusReport = message.getHead().getStatusReport();
//            System.out.println(String.format("服务端状态报告：「%d」, 1代表成功，0代表失败", statusReport));
//            if (statusReport == IMSConfig.DEFAULT_REPORT_SERVER_SEND_MSG_SUCCESSFUL) {
            System.out.println("收到服务端消息发送状态报告，message=" + message + "，从超时管理器移除");
            imsClient.getMsgTimeoutTimerManager().remove(message.getMessageseqid());
//            }
        } else {
            // 其它消息
            // 收到消息后，立马给服务端回一条消息接收状态报告
            System.out.println("收到消息，message=" + message);
            Message.NetMessage receivedReportMsg = buildReceivedReportMsg(message.getMessageseqid());
            if (receivedReportMsg != null) {
                imsClient.sendMsg(receivedReportMsg);
            }
        }

        // 接收消息，由消息转发器转发到应用层
        imsClient.getMsgDispatcher().receivedMsg(message);
    }

    /**
     * 构建客户端消息接收状态报告
     * TODO 需要新增用户信息进来
     *
     * @param msgId
     * @return
     */
    private Message.NetMessage buildReceivedReportMsg(String msgId) {
        if (StringUtil.isNullOrEmpty(msgId)) {
            return null;
        }
        Message.NetMessage.Builder builder = Message.NetMessage.newBuilder();
        builder.setMessageid(MessageIdType.HEART_BEAT + "");
        builder.setMessageseqid(UUID.randomUUID().toString());
        builder.setGamemoduleid(MessageTypes.BUS_BOX + "");
        builder.setMessagetype("1");// 客户端发给服务端的 固定为1
        builder.setSendtime(System.currentTimeMillis() + "");
        return builder.build();
    }
}
