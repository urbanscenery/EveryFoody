package dct.com.everyfoody.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import dct.com.everyfoody.base.BaseModel;

/**
 * Created by jyoung on 2017. 10. 29..
 */

public class Menu extends BaseModel {

    @SerializedName("data")
    @Expose
    private MenuItem menuItem;

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public class MenuItem{
        @SerializedName("menuinfo")
        private List<StoreInfo.MenuInfo> menuInfoList;

        public List<StoreInfo.MenuInfo> getMenuInfoList() {
            return menuInfoList;
        }

        public void setMenuInfoList(List<StoreInfo.MenuInfo> menuInfoList) {
            this.menuInfoList = menuInfoList;
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
