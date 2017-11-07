package dct.com.everyfoody.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import dct.com.everyfoody.base.BaseModel;

/**
 * Created by jyoung on 2017. 10. 31..
 */

public class UserStatus extends BaseModel{

    @SerializedName("data")
    @Expose
    private int userStatus;

    public int getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }
}
