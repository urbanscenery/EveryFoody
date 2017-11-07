package dct.com.everyfoody.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import dct.com.everyfoody.base.BaseModel;

/**
 * Created by jyoung on 2017. 10. 22..
 */

public class SideMenu extends BaseModel {

    @SerializedName("data")
    @Expose
    private SideInfo sideInfo;

    public SideInfo getSideInfo() {
        return sideInfo;
    }

    public void setSideInfo(SideInfo sideInfo) {
        this.sideInfo = sideInfo;
    }

    public class SideInfo{
        @SerializedName("imageURL")
        @Expose
        private String imageUrl;
        @SerializedName("reservationCount")
        @Expose
        private int resNum;
        @SerializedName("bookmarkCount")
        @Expose
        private int bmNum;
        @SerializedName("bookmarkInfo")
        @Expose
        private List<BookMark> bookMarkList;
        @SerializedName("toggleStatus")
        @Expose
        private List<Integer> ownerStatus;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public List<Integer> getOwnerStatus() {
            return ownerStatus;
        }

        public void setOwnerStatus(List<Integer> ownerStatus) {
            this.ownerStatus = ownerStatus;
        }

        public int getResNum() {
            return resNum;
        }

        public void setResNum(int resNum) {
            this.resNum = resNum;
        }

        public int getBmNum() {
            return bmNum;
        }

        public void setBmNum(int bmNum) {
            this.bmNum = bmNum;
        }

        public List<BookMark> getBookMarkList() {
            return bookMarkList;
        }

        public void setBookMarkList(List<BookMark> bookMarkList) {
            this.bookMarkList = bookMarkList;
        }
    }

    public class BookMark{

        @SerializedName("store_name")
        @Expose
        private String storeName;
        @SerializedName("toggle")
        @Expose
        private int toggle;
        @SerializedName("id")
        @Expose
        private int storeId;

        public int getStoreId() {
            return storeId;
        }

        public void setStoreId(int storeId) {
            this.storeId = storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public int getToggle() {
            return toggle;
        }

        public void setToggle(int toggle) {
            this.toggle = toggle;
        }
    }



}
