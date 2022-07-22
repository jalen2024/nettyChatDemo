package com.freddy.chat.im;

import com.freddy.chat.bean.AppMessage;
import com.freddy.chat.bean.BaseMessage;
import com.freddy.chat.bean.ContentMessage;
import com.freddy.chat.bean.MessageBody;
import com.freddy.chat.utils.StringUtil;
import com.freddy.im.bean.MessageTypes;
import com.network.message.web.Message;

/**
 * <p>@ProjectName:     BoChat</p>
 * <p>@ClassName:       MessageBuilder.java</p>
 * <p>@PackageName:     com.bochat.app.message</p>
 * <b>
 * <p>@Description:     消息转换</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/02/07 17:26</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class MessageBuilder {

    /**
     * 根据聊天消息，生成一条可以能够传输通讯的消息
     *
     * @param msgId
     * @return
     */
    public static AppMessage buildAppMessage(String msgId, String msgQid, String sendTime) {
        AppMessage message = new AppMessage();

        message.msgId = msgId;
        message.msgQid = msgQid;
        message.sendTime = sendTime;
        message.gmId = MessageTypes.BUS_BOX;
        message.msgType = 1;

        return message;
    }

    /**
     * 根据聊天消息，生成一条可以能够传输通讯的消息
     *
     * @param msg
     * @return
     */
    public static AppMessage buildAppMessage(ContentMessage msg) {
        AppMessage message = new AppMessage();
        message.msgId = msg.msgId;
        message.msgQid = msg.msgQid;
        message.sendTime = msg.sendTime;
        message.gmId = msg.gmId;
        message.msgType = msg.msgType;
        return message;
    }

    /**
     * 根据聊天消息，生成一条可以能够传输通讯的消息
     *
     * @param msg
     * @return
     */
    public static AppMessage buildAppMessage(BaseMessage msg) {
        AppMessage message = new AppMessage();

        message.msgId = msg.msgId;
        message.msgQid = msg.msgQid;
        message.sendTime = msg.sendTime;
        message.gmId = msg.gmId;
        message.msgType = msg.msgType;

        return message;
    }

    /**
     * 根据业务消息对象获取protoBuf消息对应的builder
     *
     * @param message
     * @return
     */
    public static Message.NetMessage.Builder getProtoBufMessageBuilderByAppMessage(AppMessage message) {
        Message.NetMessage.Builder builder = Message.NetMessage.newBuilder();
        if (!StringUtil.isEmpty(message.msgId))
            builder.setMessageid(message.msgId);
        if (!StringUtil.isEmpty(message.msgQid))
            builder.setMessageseqid(message.msgQid);
        if (!StringUtil.isEmpty(message.msgType))
            builder.setMessagetype(message.msgType + "");
        if (!StringUtil.isEmpty(message.gmId))
            builder.setGamemoduleid(message.gmId + "");
        if (!StringUtil.isEmpty(message.playId))
            builder.setPlayerid(message.playId);
        return builder;
    }

    /**
     * 通过protobuf消息对象获取业务消息对象
     *
     * @param protobufMessage
     * @return
     */
    public static AppMessage getMessageByProtobuf(
            Message.NetMessage protobufMessage) {
        AppMessage message = new AppMessage();


        message.msgId = protobufMessage.getMessageid();
        message.msgQid = protobufMessage.getMessageseqid();
        message.sendTime = protobufMessage.getSendtime();
        if (StringUtil.isNotEmpty(protobufMessage.getGamemoduleid())) {
            message.gmId = Integer.parseInt(protobufMessage.getGamemoduleid());
        }
        if (StringUtil.isNotEmpty(protobufMessage.getGamemoduleid())) {
            message.msgType = Integer.parseInt(protobufMessage.getMessagetype());
        }
        return message;
    }
}
