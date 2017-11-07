package dct.com.everyfoody.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import dct.com.everyfoody.base.BaseModel;

/**
 * Created by jyoung on 2017. 10. 15..
 */

public class StoreInfo extends BaseModel {


    @SerializedName("data")
    @Expose
    private DetailInfo detailInfo;

    public DetailInfo getDetailInfo() {
        return detailInfo;
    }

    public void setDetailInfo(DetailInfo detailInfo) {
        this.detailInfo = detailInfo;
    }

    public class BasicInfo {

        @SerializedName("storeID")
        @Expose
        private Integer storeID;
        @SerializedName("storeName")
        @Expose
        private String storeName;
        @SerializedName("storeImage")
        @Expose
        private String storeImage;
        @SerializedName("storeFacebookURL")
        @Expose
        private String storeFacebookURL;
        @SerializedName("storeTwitterURL")
        @Expose
        private String storeTwitterURL;
        @SerializedName("storeInstagramURL")
        @Expose
        private String storeInstagramURL;
        @SerializedName("storeHashtag")
        @Expose
        private String storeHashtag;
        @SerializedName("storeOpentime")
        @Expose
        private String storeOpentime;
        @SerializedName("storeBreaktime")
        @Expose
        private String storeBreaktime;
        @SerializedName("storePhone")
        @Expose
        private String storePhone;
        @SerializedName("reservationCount")
        @Expose
        private Integer reservationCount;
        @SerializedName("reservationCheck")
        @Expose
        private Integer reservationCheck;
        @SerializedName("bookmarkCheck")
        @Expose
        private Integer bookmarkCheck;

        public Integer getBookmarkCheck() {
            return bookmarkCheck;
        }

        public void setBookmarkCheck(Integer bookmarkCheck) {
            this.bookmarkCheck = bookmarkCheck;
        }

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

        public String getStoreImage() {
            return storeImage;
        }

        public void setStoreImage(String storeImage) {
            this.storeImage = storeImage;
        }

        public String getStoreFacebookURL() {
            return storeFacebookURL;
        }

        public void setStoreFacebookURL(String storeFacebookURL) {
            this.storeFacebookURL = storeFacebookURL;
        }

        public String getStoreTwitterURL() {
            return storeTwitterURL;
        }

        public void setStoreTwitterURL(String storeTwitterURL) {
            this.storeTwitterURL = storeTwitterURL;
        }

        public String getStoreInstagramURL() {
            return storeInstagramURL;
        }

        public void setStoreInstagramURL(String storeInstagramURL) {
            this.storeInstagramURL = storeInstagramURL;
        }

        public String getStoreHashtag() {
            return storeHashtag;
        }

        public void setStoreHashtag(String storeHashtag) {
            this.storeHashtag = storeHashtag;
        }

        public String getStoreOpentime() {
            return storeOpentime;
        }

        public void setStoreOpentime(String storeOpentime) {
            this.storeOpentime = storeOpentime;
        }

        public String getStoreBreaktime() {
            return storeBreaktime;
        }

        public void setStoreBreaktime(String storeBreaktime) {
            this.storeBreaktime = storeBreaktime;
        }

        public String getStorePhone() {
            return storePhone;
        }

        public void setStorePhone(String storePhone) {
            this.storePhone = storePhone;
        }

        public Integer getReservationCount() {
            return reservationCount;
        }

        public void setReservationCount(Integer reservationCount) {
            this.reservationCount = reservationCount;
        }

        public Integer getReservationCheck() {
            return reservationCheck;
        }

        public void setReservationCheck(Integer reservationCheck) {
            this.reservationCheck = reservationCheck;
        }

    }

    public class DetailInfo {

        @SerializedName("basicInfo")
        @Expose
        private BasicInfo basicInfo;
        @SerializedName("menuInfo")
        @Expose
        private List<MenuInfo> menuInfo = null;

        public BasicInfo getBasicInfo() {
            return basicInfo;
        }

        public void setBasicInfo(BasicInfo basicInfo) {
            this.basicInfo = basicInfo;
        }

        public List<MenuInfo> getMenuInfo() {
            return menuInfo;
        }

        public void setMenuInfo(List<MenuInfo> menuInfo) {
            this.menuInfo = menuInfo;
        }

    }

    public class MenuInfo {

        @SerializedName("menuID")
        @Expose
        private Integer menuID;
        @SerializedName("menuTitle")
        @Expose
        private String menuTitle;
        @SerializedName("menuPrice")
        @Expose
        private Integer menuPrice;
        @SerializedName("menuImageURL")
        @Expose
        private String menuImageURL;

        public Integer getMenuID() {
            return menuID;
        }

        public void setMenuID(Integer menuID) {
            this.menuID = menuID;
        }

        public String getMenuTitle() {
            return menuTitle;
        }

        public void setMenuTitle(String menuTitle) {
            this.menuTitle = menuTitle;
        }

        public Integer getMenuPrice() {
            return menuPrice;
        }

        public void setMenuPrice(Integer menuPrice) {
            this.menuPrice = menuPrice;
        }

        public String getMenuImageURL() {
            return menuImageURL;
        }

        public void setMenuImageURL(String menuImageURL) {
            this.menuImageURL = menuImageURL;
        }

    }


}
