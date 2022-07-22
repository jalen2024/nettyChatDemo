package com.freddy.im;

import android.text.TextUtils;

import com.freddy.im.interf.IMSClientInterface;
import com.network.message.web.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       MsgTimeoutTimer.java</p>
 * <p>@PackageName:     com.freddy.im</p>
 * <b>
 * <p>@Description:     消息发送超时定时器，每一条消息对应一个定时器</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/09 22:38</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class MsgTimeoutTimer extends Timer {

    private IMSClientInterface imsClient;// ims客户端
    private Message.NetMessage msg;// 发送的消息
    private int currentResendCount;// 当前重发次数
    private MsgTimeoutTask task;// 消息发送超时任务

    public MsgTimeoutTimer(IMSClientInterface imsClient, Message.NetMessage msg) {
        this.imsClient = imsClient;
        this.msg = msg;
        task = new MsgTimeoutTask();
        this.schedule(task, imsClient.getResendInterval(), imsClient.getResendInterval());
    }

    /**
     * 消息发送超时任务
     */
    private class MsgTimeoutTask extends TimerTask {

        @Override
        public void run() {
            if (imsClient.isClosed()) {
                // 从消息发送超时管理器移除该消息
                if (imsClient.getMsgTimeoutTimerManager() != null) {
                    imsClient.getMsgTimeoutTimerManager().remove(msg.getMessageseqid());
                }

                return;
            }

            currentResendCount++;
            if (currentResendCount > imsClient.getResendCount()) {
                // 重发次数大于可重发次数，直接标识为发送失败，并通过消息转发器通知应用层
                try {
                    Message.NetMessage.Builder builder = Message.NetMessage.newBuilder();

                    if (!TextUtils.isEmpty(msg.getMessageid()))
                        builder.setMessageid(msg.getMessageid());
                    if (!TextUtils.isEmpty(msg.getMessageseqid()))
                        builder.setMessageseqid(msg.getMessageseqid());

                    if (!TextUtils.isEmpty(msg.getMessagetype()))
                        builder.setMessagetype(msg.getMessagetype() + "");
                    if (!TextUtils.isEmpty(msg.getGamemoduleid()))
                        builder.setGamemoduleid(msg.getGamemoduleid() + "");
                    if (!TextUtils.isEmpty(msg.getPlayerid()))
                        builder.setPlayerid(msg.getPlayerid());

                    builder.setSendtime(System.currentTimeMillis()+"");
//                    MessageProtobuf.Head.Builder headBuilder = MessageProtobuf.Head.newBuilder();
//                    headBuilder.setMsgId(msg.getHead().getMsgId());
//                    headBuilder.setMsgType(imsClient.getServerSentReportMsgType());
//                    headBuilder.setTimestamp(System.currentTimeMillis());
//                    headBuilder.setStatusReport(IMSConfig.DEFAULT_REPORT_SERVER_SEND_MSG_FAILURE);
//                    builder.setHead(headBuilder.build());

                    // 通知应用层消息发送失败
                    imsClient.getMsgDispatcher().receivedMsg(builder.build());
                } finally {
                    // 从消息发送超时管理器移除该消息
                    imsClient.getMsgTimeoutTimerManager().remove(msg.getMessageid());
                    // 执行到这里，认为连接已断开或不稳定，触发重连
                    imsClient.resetConnect();
                    currentResendCount = 0;
                }
            } else {
                // 发送消息，但不再加入超时管理器，达到最大发送失败次数就算了
                sendMsg();
            }
        }
    }

    public void sendMsg() {
        System.out.println("正在重发消息，message=" + msg);
        imsClient.sendMsg(msg, false);
    }

    public Message.NetMessage getMsg() {
        return msg;
    }

    @Override
    public void cancel() {
        if (task != null) {
            task.cancel();
            task = null;
        }

        super.cancel();
    }
}
