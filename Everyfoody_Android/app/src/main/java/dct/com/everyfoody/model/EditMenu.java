package dct.com.everyfoody.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by jyoung on 2017. 10. 29..
 */

public class EditMenu {
    @SerializedName("image")
    @Expose
    private String menuImage;
    @SerializedName("menu_name")
    @Expose
    private String menuName;
    @SerializedName("menu_price")
    @Expose
    private String menuPrice;

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        this.menuPrice = menuPrice;
    }
}
