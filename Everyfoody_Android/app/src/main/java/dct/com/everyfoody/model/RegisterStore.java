package dct.com.everyfoody.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jyoung on 2017. 10. 28..
 */

public class RegisterStore {

    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("image")
    @Expose
    private String license;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
