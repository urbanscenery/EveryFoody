package dct.com.everyfoody.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import dct.com.everyfoody.base.BaseModel;

/**
 * Created by jyoung on 2017. 10. 23..
 */

public class CheckId extends BaseModel {
    @SerializedName("data")
    @Expose
    private int idFlag;

    public int getIdFlag() {
        return idFlag;
    }

    public void setIdFlag(int idFlag) {
        this.idFlag = idFlag;
    }
}
