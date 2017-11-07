package dct.com.everyfoody.ui.detail.review;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.base.WhiteThemeActivity;
import dct.com.everyfoody.base.util.LogUtil;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.base.util.ToastMaker;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.ResReview;
import dct.com.everyfoody.request.NetworkService;
import dct.com.everyfoody.ui.bookmark.BookmarkActivity;
import dct.com.everyfoody.ui.login.LoginActivity;
import gun0912.tedbottompicker.TedBottomPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.maxHeight;
import static android.R.attr.maxWidth;
import static dct.com.everyfoody.ui.login.LoginActivity.AUTH_TOKEN;
import static dct.com.everyfoody.ui.login.LoginActivity.NETWORK_SUCCESS;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_GUEST;
import static dct.com.everyfoody.ui.login.LoginActivity.RESULT_OWNER;
import static dct.com.everyfoody.ui.login.LoginActivity.USER_STATUS;

public class ReviewActivity extends WhiteThemeActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.review_toolbar)
    Toolbar reviewToolbar;
    @BindView(R.id.review_rcv)
    RecyclerView reviewRecycler;
    @BindView(R.id.comment_edit)
    EditText commentEdit;
    @BindView(R.id.selected_image_preview)
    ImageView previewImage;
    @BindView(R.id.comment_item)
    LinearLayout commentItem;
    @BindView(R.id.warning_layout)
    View warningLayout;
    @BindView(R.id.review_srl)SwipeRefreshLayout reviewRefreshLayout;

    private ReviewRecyclerAdapter reviewRecyclerAdapter;
    private NetworkService networkService;
    private List<ResReview.Review> reviewList;
    private int tempId;
    private Uri resultUri;
    private String rating;
    private ReviewDialog reviewDialog;
    private BookmarkActivity.NonDataWarning nonDataWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        SharedPreferencesService.getInstance().load(this);
        reviewList = new ArrayList<>();
        Intent getId = getIntent();
        tempId = getId.getExtras().getInt("storeId");

        checkUserStatus();
        setToolbar();
        setRecycler();
        getReviewList();
        setRefresh();
        checkTextLength();
    }
    private void checkTextLength(){
        commentEdit.addTextChangedListener(new TextWatcher() {
            String strCur;
            @Override
            public void beforeTextChanged(CharSequence c, int i, int i1, int i2) {
                if(c.length() > 30){
                    commentEdit.setText(strCur);
                    ToastMaker.makeShortToast(getApplicationContext(), "리뷰는 30자 제한입니다.");
                }
            }

            @Override
            public void onTextChanged(CharSequence c, int i, int i1, int i2) {
                strCur = c.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void setRefresh(){
        reviewRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        reviewRefreshLayout.setOnRefreshListener(this);
    }

    private void ifemptyData() {
        nonDataWarning = new BookmarkActivity.NonDataWarning();
        ButterKnife.bind(nonDataWarning, warningLayout);
        reviewRecycler.setVisibility(View.INVISIBLE);
        warningLayout.setVisibility(View.VISIBLE);
        nonDataWarning.warningText.setText("등록된 후기가 없습니다.");
    }

    private void checkUserStatus() {
        if (SharedPreferencesService.getInstance().getPrefIntegerData(USER_STATUS) == RESULT_OWNER)
            commentItem.setVisibility(View.GONE);

    }

    private void getReviewList() {
        String token = SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN);
        if (token.equals(""))
            token = "nonLoginUser";

        Call<ResReview> getReviewList = networkService.getReviewList(token, tempId);

        getReviewList.enqueue(new Callback<ResReview>() {
            @Override
            public void onResponse(Call<ResReview> call, Response<ResReview> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                        reviewList = response.body().getResult().getReviews();
                        reviewRecyclerAdapter.refreshAdapter(reviewList);

                        if (reviewList.size() == 0)
                            ifemptyData();
                        else {
                            reviewRecycler.setVisibility(View.VISIBLE);
                            warningLayout.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResReview> call, Throwable t) {
                LogUtil.d(getApplicationContext(), t.toString());
            }
        });
    }

    private void setRecycler() {
        reviewRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        reviewRecyclerAdapter = new ReviewRecyclerAdapter(reviewList);
        reviewRecycler.setAdapter(reviewRecyclerAdapter);
    }

    private void setToolbar() {
        reviewToolbar.setTitle("");
        setSupportActionBar(reviewToolbar);
        reviewToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        reviewToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void checkPermission() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("에브리푸디를 100% 이용하기 위한 권한을 주세요!!")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void registerReview() {
        RequestBody storeId = RequestBody.create(MediaType.parse("multipart/form-data"), tempId + "");
        RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), commentEdit.getText().toString());
        RequestBody score = RequestBody.create(MediaType.parse("multipart/form-data"), rating);

        MultipartBody.Part body;

        if (resultUri == null) {
            body = null;
        } else {

            BitmapFactory.Options options = new BitmapFactory.Options();

            InputStream in = null;
            try {
                in = getContentResolver().openInputStream(resultUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());

            File photo = new File(resultUri.toString());

            body = MultipartBody.Part.createFormData("image", photo.getName(), photoBody);

        }

        Call<BaseModel> registerReview = networkService.registerReview(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN),
                body, storeId, score, content);

        registerReview.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                        getReviewList();
                        reviewDialog.dismiss();
                        commentEdit.setText("");
                        previewImage.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                LogUtil.d(getApplicationContext(), t.toString());
            }
        });
    }

    @OnClick(R.id.comment_register_btn)
    public void onClickRegisterReview(View view) {
        if (SharedPreferencesService.getInstance().getPrefIntegerData(USER_STATUS) != RESULT_GUEST) {
            ToastMaker.makeShortToast(getApplicationContext(), "로그인이 필요한 기능입니다.");
            Intent needLogin = new Intent(this, LoginActivity.class);
            startActivity(needLogin);
        } else {
            if (commentEdit.length() != 0) {
                reviewDialog = new ReviewDialog(this, yesClickListener);
                reviewDialog.show();
            } else
                ToastMaker.makeShortToast(getApplicationContext(), "후기 내용 또는 사진을 추가해주세요");
        }

    }

    public View.OnClickListener yesClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            rating = String.valueOf(((RatingBar) reviewDialog.findViewById(R.id.regiter_review_rating)).getRating() * 2);
            registerReview();
        }
    };

    @OnClick(R.id.comment_select_image)
    public void onClickSelectImage(View view) {
        checkPermission();

        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(ReviewActivity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        cropImage(uri);
                    }
                })
                .create();

        tedBottomPicker.show(getSupportFragmentManager());
    }

    private void cropImage(Uri tempUri) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getResources().getColor(R.color.colorAccent));
        options.setToolbarTitle("사진 편집");
        options.setStatusBarColor(getResources().getColor(R.color.colorAccent));

        Uri mDestinationUri = Uri.fromFile(new File(getCacheDir(), tempUri.toString().substring(tempUri.toString().lastIndexOf('/') + 1)));


        UCrop.of(tempUri, mDestinationUri)
                .withOptions(options)
                .withAspectRatio(2.8f, 1.1f)
                .withMaxResultSize(maxWidth, maxHeight)
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri tempUri = UCrop.getOutput(data);
            resultUri = tempUri;
            previewImage.setVisibility(View.VISIBLE);
            Glide.with(previewImage.getContext())
                    .load(resultUri)
                    .into(previewImage);
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    @Override
    public void onRefresh() {
        getReviewList();
        reviewRefreshLayout.setRefreshing(false);
    }
}
