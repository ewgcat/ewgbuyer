package com.ewgvip.buyer.android.models;

/**
 * Created by Administrator on 2016/8/16.
 */
public class EwgMessage {

    /**
     * addTime : 2016-08-16 11:38:27
     * content : 1222222222222222222222222222222222222222222222222222222222
     * fromUser : 系统消息
     * id : 6222
     * status : 0
     */
    public String addTime;
    public String content;
    public String fromUser;
    public String id;
    public String status;


    public EwgMessage(String fromUser, String addTime, String content, String id, String status) {
        this.fromUser = fromUser;
        this.addTime = addTime;
        this.content = content;
        this.id = id;
        this.status = status;
    }

    @Override
    public String toString() {
        return "EwgMessage{" +
                "addTime='" + addTime + '\'' +
                ", content='" + content + '\'' +
                ", fromUser='" + fromUser + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
