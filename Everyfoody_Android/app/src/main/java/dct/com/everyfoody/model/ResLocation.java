package dct.com.everyfoody.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import dct.com.everyfoody.base.BaseModel;

/**
 * Created by jyoung on 2017. 10. 18..
 */

public class ResLocation extends BaseModel {

    @SerializedName("data")
    @Expose
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public class Location {

        @SerializedName("storeID")
        @Expose
        private Integer storeID;
        @SerializedName("storeLatitude")
        @Expose
        private Double storeLatitude;
        @SerializedName("storeLongitude")
        @Expose
        private Double storeLongitude;
        @SerializedName("reservationCount")
        @Expose
        private Integer reservationCount;

        public Integer getStoreID() {
            return storeID;
        }

        public void setStoreID(Integer storeID) {
            this.storeID = storeID;
        }

        public Double getStoreLatitude() {
            return storeLatitude;
        }

        public void setStoreLatitude(Double storeLatitude) {
            this.storeLatitude = storeLatitude;
        }

        public Double getStoreLongitude() {
            return storeLongitude;
        }

        public void setStoreLongitude(Double storeLongitude) {
            this.storeLongitude = storeLongitude;
        }

        public Integer getReservationCount() {
            return reservationCount;
        }

        public void setReservationCount(Integer reservationCount) {
            this.reservationCount = reservationCount;
        }

    }

}
