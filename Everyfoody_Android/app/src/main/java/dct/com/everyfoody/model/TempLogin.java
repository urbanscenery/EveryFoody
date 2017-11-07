package dct.com.everyfoody.model;

/**
 * Created by jyoung on 2017. 10. 27..
 */

public class TempLogin {

    private String email;
    private String uid;
    private String imageUrl;
    private int category;

    public TempLogin(String email, String uid, String imageUrl, int category) {
        this.email = email;
        this.uid = uid;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
