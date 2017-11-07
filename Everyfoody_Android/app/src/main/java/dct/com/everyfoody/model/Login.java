package dct.com.everyfoody.model;

import com.google.gson.annotations.SerializedName;

import dct.com.everyfoody.base.BaseModel;

/**
 * Created by jyoung on 2017. 10. 16..
 */

public class Login extends BaseModel {

    @SerializedName("data")
    private LoginResult resultData;

    public LoginResult getResultData() {
        return resultData;
    }

    public void setResultData(LoginResult resultData) {
        this.resultData = resultData;
    }

    public class LoginResult{
        private String token;
        private String name;
        private int category;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCategory() {
            return category;
        }

        public void setCategory(int category) {
            this.category = category;
        }
    }
}
