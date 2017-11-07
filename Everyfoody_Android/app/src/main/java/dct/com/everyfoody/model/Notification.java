package dct.com.everyfoody.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import dct.com.everyfoody.base.BaseModel;

/**
 * Created by jyoung on 2017. 10. 27..
 */

public class Notification extends BaseModel {

    @SerializedName("data")
    @Expose
    private List<Noti> notiList = null;

    public List<Noti> getNotiList() {
        return notiList;
    }

    public void setNotiList(List<Noti> notiList) {
        this.notiList = notiList;
    }

    public class Noti {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user_id")
        @Expose
        private Integer userId;
        @SerializedName("notice_content")
        @Expose
        private String noticeContent;
        @SerializedName("notice_time")
        @Expose
        private String noticeTime;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getNoticeContent() {
            return noticeContent;
        }

        public void setNoticeContent(String noticeContent) {
            this.noticeContent = noticeContent;
        }

        public String getNoticeTime() {
            return noticeTime;
        }

        public void setNoticeTime(String noticeTime) {
            this.noticeTime = noticeTime;
        }

    }

}
