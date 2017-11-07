package dct.com.everyfoody.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import dct.com.everyfoody.base.BaseModel;

/**
 * Created by jyoung on 2017. 10. 20..
 */

public class Turn extends BaseModel{

    @SerializedName("data")
    @Expose
    private List<TurnInfo> turnList = null;

    public List<TurnInfo> getTurnList() {
        return turnList;
    }

    public void setTurnList(List<TurnInfo> turnList) {
        this.turnList = turnList;
    }

    public class TurnInfo {

        @SerializedName("user_nickname")
        @Expose
        private String userNickname;
        @SerializedName("user_phone")
        @Expose
        private String userPhone;
        @SerializedName("reservation_time")
        @Expose
        private String reservationTime;
        @SerializedName("user_id")
        @Expose
        private Integer userId;

        public String getUserNickname() {
            return userNickname;
        }

        public void setUserNickname(String userNickname) {
            this.userNickname = userNickname;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getReservationTime() {
            return reservationTime;
        }

        public void setReservationTime(String reservationTime) {
            this.reservationTime = reservationTime;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

    }
}
