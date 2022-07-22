package com.freddy.chat.bean;

import com.freddy.chat.utils.StringUtil;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       BaseMessage.java</p>
 * <p>@PackageName:     com.freddy.chat.bean</p>
 * <b>
 * <p>@Description:     消息基类</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/10 00:02</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class BaseMessage {


    public String msgId;       // 消息类型id  1 2  10000
    public String msgQid;       // 消息id  不同的消息id
    public int gmId;        // 6 盲盒模块 的消息
    public int msgType; // 消息类型 1 从客户端发给后台的 2 从后台发给客户端的
    public String sendTime; //消息的发送时间


    @Override
    public int hashCode() {
        try {
            return this.msgQid.hashCode();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof BaseMessage)) {
            return false;
        }

        return StringUtil.equals(this.msgQid, ((BaseMessage) obj).msgQid);
    }

    @Override
    public String toString() {
        return "BaseMessage{" +
                "msgId='" + msgId + '\'' +
                ", msgQid='" + msgQid + '\'' +
                ", gmId=" + gmId +
                ", msgType=" + msgType +
                ", sendTime='" + sendTime + '\'' +
                '}';
    }
}
