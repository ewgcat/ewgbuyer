package com.ewgvip.buyer.android.models;

/**
 * Created by Administrator on 2016/7/18.
 */
public class TeamItem {

    /**
     * addTime : 2016年06月08日
     * child_size : 1
     * gradeName : {"icon":"/resources/style/vip/general.png","name":"普通会员"}
     * photo_url : http://12306.iok.la/resources/style/common/images/member.jpg
     * userName : zhanglibo5
     * user_id : 47
     */

    public String addTime;
    public String child_size;
    public String photo_url;
    public String userName;
    public String user_id;

    /**
     * icon : /resources/style/vip/general.png
     * name : 普通会员
     */

    public GradeNameBean gradeName;




    public TeamItem(String addTime, String child_size, String photo_url, String userName, String user_id, GradeNameBean gradeName) {
        this.addTime = addTime;
        this.child_size = child_size;
        this.photo_url = photo_url;
        this.userName = userName;
        this.user_id = user_id;
        this.gradeName = gradeName;
    }

    @Override
    public String toString() {
        return "TeamItem{" +
                "addTime='" + addTime + '\'' +
                ", child_size='" + child_size + '\'' +
                ", photo_url='" + photo_url + '\'' +
                ", userName='" + userName + '\'' +
                ", user_id='" + user_id + '\'' +
                ", gradeName=" + gradeName +
                '}';
    }
}
