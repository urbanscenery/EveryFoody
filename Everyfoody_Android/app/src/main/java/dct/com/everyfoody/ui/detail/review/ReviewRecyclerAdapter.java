package dct.com.everyfoody.ui.detail.review;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dct.com.everyfoody.R;
import dct.com.everyfoody.model.ResReview;


/**
 * Created by jyoung on 2017. 10. 4..
 */

public class ReviewRecyclerAdapter extends RecyclerView.Adapter {
    private List<ResReview.Review> reviewList;

    public void refreshAdapter(List<ResReview.Review> reviewList){
        this.reviewList = reviewList;
        notifyDataSetChanged();
    }

    public ReviewRecyclerAdapter(List<ResReview.Review> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_review_rcv_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ReviewViewHolder)holder).bindView(reviewList.get(position));
    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }


    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.review_writer_name)TextView reviewWriter;
        @BindView(R.id.review_content)TextView reviewContent;
        @BindView(R.id.review_image)ImageView reviewImage;
        @BindView(R.id.review_rating)RatingBar reviewRating;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(ResReview.Review reviewItem){
            reviewWriter.setText(reviewItem.getReviewWriter());
            reviewContent.setText(reviewItem.getReviewContent());
            reviewRating.setRating((float)reviewItem.getReviewScore()/2);

            if(reviewItem.getReviewImageUrl() != null)
                Glide.with(reviewImage.getContext()).load(reviewItem.getReviewImageUrl()).into(reviewImage);
        }


    }
}
