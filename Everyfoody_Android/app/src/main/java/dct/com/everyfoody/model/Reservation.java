package dct.com.everyfoody.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import dct.com.everyfoody.base.BaseModel;

/**
 * Created by jyoung on 2017. 10. 18..
 */

public class Reservation extends BaseModel {

    @SerializedName("data")
    @Expose
    private List<Store> store;

    public List<Store> getStore() {
        return store;
    }

    public void setStore(List<Store> store) {
        this.store = store;
    }

    public class Store {

        @SerializedName("storeID")
        @Expose
        private Integer storeID;
        @SerializedName("storeName")
        @Expose
        private String storeName;
        @SerializedName("storeImage")
        @Expose
        private String storeImageURL;
        @SerializedName("reservationCount")
        @Expose
        private Integer reservationCount;
        @SerializedName("reservationTime")
        @Expose
        private String reservationTime;

        public Integer getStoreID() {
            return storeID;
        }

        public void setStoreID(Integer storeID) {
            this.storeID = storeID;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public String getStoreImageURL() {
            return storeImageURL;
        }

        public void setStoreImageURL(String storeImageURL) {
            this.storeImageURL = storeImageURL;
        }

        public Integer getReservationCount() {
            return reservationCount;
        }

        public void setReservationCount(Integer reservationCount) {
            this.reservationCount = reservationCount;
        }

        public String getReservationTime() {
            return reservationTime;
        }

        public void setReservationTime(String reservationTime) {
            this.reservationTime = reservationTime;
        }

    }

}
