package com.freddy.chat.bean;

import com.freddy.chat.utils.StringUtil;
import com.freddy.im.bean.MessageTypes;

/**
 * <p>@ProjectName:     NettyChat</p>
 * <p>@ClassName:       ContentMessage.java</p>
 * <p>@PackageName:     com.freddy.chat.bean</p>
 * <b>
 * <p>@Description:     内容消息，包含单聊消息及群聊消息</p>
 * </b>
 * <p>@author:          FreddyChen</p>
 * <p>@date:            2019/04/10 00:06</p>
 * <p>@email:           chenshichao@outlook.com</p>
 */
public class ContentMessage extends BaseMessage {

//    protected boolean isRead;
//    protected boolean isPlaying;
//    protected boolean isLoading;

    public ContentMessage() {
    }

    public ContentMessage(String msgId, String msgQid, String sendTime) {
        this.msgId = msgId;
        this.msgQid = msgQid;
        this.sendTime = sendTime;
        this.gmId = MessageTypes.BUS_BOX;
        this.msgType = 1;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ContentMessage)) {
            return false;
        }

        return StringUtil.equals(this.msgQid, ((ContentMessage) obj).msgQid);
    }

    @Override
    public int hashCode() {
        try {
            return this.msgQid.hashCode();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return 1;
    }
}
