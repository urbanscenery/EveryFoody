package dct.com.everyfoody.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import dct.com.everyfoody.base.BaseModel;

/**
 * Created by jyoung on 2017. 10. 18..
 */

public class ResReview extends BaseModel {

    @SerializedName("data")
    @Expose
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("storeID")
        @Expose
        private Integer storeID;
        @SerializedName("reviews")
        @Expose
        private List<Review> reviews = null;

        public Integer getStoreID() {
            return storeID;
        }

        public void setStoreID(Integer storeID) {
            this.storeID = storeID;
        }

        public List<Review> getReviews() {
            return reviews;
        }

        public void setReviews(List<Review> reviews) {
            this.reviews = reviews;
        }

    }


    public class Review {

        @SerializedName("reviewWriter")
        @Expose
        private String reviewWriter;
        @SerializedName("reviewScore")
        @Expose
        private Integer reviewScore;
        @SerializedName("reviewContent")
        @Expose
        private String reviewContent;
        @SerializedName("reviewImageUrl")
        @Expose
        private String reviewImageUrl;

        public String getReviewWriter() {
            return reviewWriter;
        }

        public void setReviewWriter(String reviewWriter) {
            this.reviewWriter = reviewWriter;
        }

        public Integer getReviewScore() {
            return reviewScore;
        }

        public void setReviewScore(Integer reviewScore) {
            this.reviewScore = reviewScore;
        }

        public String getReviewContent() {
            return reviewContent;
        }

        public void setReviewContent(String reviewContent) {
            this.reviewContent = reviewContent;
        }

        public String getReviewImageUrl() {
            return reviewImageUrl;
        }

        public void setReviewImageUrl(String reviewImageUrl) {
            this.reviewImageUrl = reviewImageUrl;
        }

    }
}