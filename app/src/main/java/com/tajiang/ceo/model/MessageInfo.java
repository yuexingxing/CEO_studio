package com.tajiang.ceo.model;

/**
 * Created by Administrator on 2017-08-08.
 */

public class MessageInfo {

    private String content;//	消息内容	string
    private String createTime;//	消息日期	string
    private int isRead;//	是否已读	number	1：已读 0：未读
    private int msgId;//	消息编码	number

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

}
