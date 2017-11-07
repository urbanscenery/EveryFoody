package dct.com.everyfoody.ui.detail.edit;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dct.com.everyfoody.R;
import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.base.util.LogUtil;
import dct.com.everyfoody.base.util.SharedPreferencesService;
import dct.com.everyfoody.global.ApplicationController;
import dct.com.everyfoody.model.StoreInfo;
import dct.com.everyfoody.request.NetworkService;
import gun0912.tedbottompicker.TedBottomPicker;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.attr.maxHeight;
import static android.R.attr.maxWidth;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getCacheDir;
import static dct.com.everyfoody.ui.login.LoginActivity.AUTH_TOKEN;
import static dct.com.everyfoody.ui.login.LoginActivity.NETWORK_SUCCESS;

/**
 * Created by jyoung on 2017. 10. 4..
 */

public class NormalEditFragment extends Fragment {
    @BindView(R.id.edit_operation_time)
    EditText operationEdit;
    @BindView(R.id.edit_break_time)
    EditText breakTiemEdit;
    @BindView(R.id.edit_hashtag)
    EditText hashtagEdit;
    @BindView(R.id.edit_phone_number)
    EditText phoneNumEdit;
    @BindView(R.id.edit_facebook_url)
    EditText facebookEdit;
    @BindView(R.id.edit_twitter_url)
    EditText twitterEdit;
    @BindView(R.id.edit_instagram_url)
    EditText instagramEdit;
    @BindView(R.id.edit_main_image)
    ImageView mainImage;

    private StoreInfo.BasicInfo basicInfo;
    private NetworkService networkService;
    private Uri[] resultUri = new Uri[2];
    private Uri tempUri;
    private File[] files;

    public final static int THUMBNAIL_CROP = 1000;

    public NormalEditFragment() {
    }

    public static NormalEditFragment getInstance(Bundle bundle) {
        NormalEditFragment normalEditFragment = new NormalEditFragment();
        normalEditFragment.setArguments(bundle);
        return normalEditFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_normal_info_edit, null);
        ButterKnife.bind(this, view);
        networkService = ApplicationController.getInstance().getNetworkService();
        SharedPreferencesService.getInstance().load(getContext());
        if (getArguments() != null) {
            Gson gson = new Gson();
            basicInfo = gson.fromJson(getArguments().getString("basic"), StoreInfo.BasicInfo.class);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initInfo();
        phoneNumEdit.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    private void initInfo() {
        operationEdit.setText(basicInfo.getStoreOpentime());
        breakTiemEdit.setText(basicInfo.getStoreBreaktime());
        hashtagEdit.setText(basicInfo.getStoreHashtag());
        phoneNumEdit.setText(basicInfo.getStorePhone());
        facebookEdit.setText(basicInfo.getStoreFacebookURL());
        twitterEdit.setText(basicInfo.getStoreTwitterURL());
        instagramEdit.setText(basicInfo.getStoreInstagramURL());
        Glide.with(getContext()).load(basicInfo.getStoreImage()).into(mainImage);
    }

    public StoreInfo.BasicInfo getEditInfo() {
        basicInfo.setStoreBreaktime(breakTiemEdit.getText().toString());
        basicInfo.setStoreOpentime(operationEdit.getText().toString());
        basicInfo.setStoreHashtag(hashtagEdit.getText().toString());
        basicInfo.setStorePhone(phoneNumEdit.getText().toString());
        basicInfo.setStoreFacebookURL(facebookEdit.getText().toString());
        basicInfo.setStoreInstagramURL(instagramEdit.getText().toString());
        basicInfo.setStoreTwitterURL(twitterEdit.getText().toString());

        return basicInfo;
    }

    @OnClick(R.id.edit_main_image_picker)
    public void selectImage(View view) {
        checkPermission();
        getImage();

    }

    private void modifyImage() {
        MultipartBody.Part[] body = new MultipartBody.Part[2];
        files = new File[body.length];


        for (int i = 0; i < body.length; i++) {
            if (resultUri[i].toString() == "") {
                body[i] = null;
            } else {
                BitmapFactory.Options options = new BitmapFactory.Options();

                InputStream in = null;
                try {
                    in = getActivity().getContentResolver().openInputStream(resultUri[i]);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);

                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());

                files[i] = new File(resultUri[i].toString());

                body[i] = MultipartBody.Part.createFormData("image", files[i].getName(), photoBody);
                bitmap.recycle();
            }
        }

        Call<BaseModel> editMainImageCall = networkService.modifyStoreImage(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN), body);

        editMainImageCall.enqueue(new Callback<BaseModel>() {
            @Override
            public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                        Glide.with(mainImage.getContext())
                                .load(resultUri[1])
                                .into(mainImage);
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseModel> call, Throwable t) {
                LogUtil.d(getContext(), t.toString());
            }
        });
    }

    private void getImage() {
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(getActivity())
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {
                        tempUri = uri;
                        cropImage(uri);
                    }
                })
                .create();

        tedBottomPicker.show(getActivity().getSupportFragmentManager());
    }

    private void cropImage(Uri uri) {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getResources().getColor(R.color.colorAccent));
        options.setToolbarTitle("사진 편집");
        options.setStatusBarColor(getResources().getColor(R.color.colorAccent));

        Uri mDestinationUri = Uri.fromFile(new File(getCacheDir(), uri.toString().substring(uri.toString().lastIndexOf('/') + 1)));


        UCrop.of(uri, mDestinationUri)
                .withOptions(options)
                .withAspectRatio(3.6f, 2.8f)
                .withMaxResultSize(maxWidth, maxHeight)
                .start(getActivity());
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

        TedPermission.with(getContext())
                .setPermissionListener(permissionlistener)
                .setRationaleMessage("에브리푸디를 100% 이용하기 위한 권한을 주세요!!")
                .setDeniedMessage("왜 거부하셨어요...\n하지만 [설정] > [권한] 에서 권한을 허용할 수 있어요.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void thumbnailCrop() {
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getResources().getColor(R.color.colorAccent));
        options.setToolbarTitle("사진 편집");
        options.setStatusBarColor(getResources().getColor(R.color.colorAccent));

        Uri mDestinationUri = Uri.fromFile(new File(getCacheDir(), "thumbnail" + tempUri.toString().substring(tempUri.toString().lastIndexOf('/') + 1)));


        UCrop.of(tempUri, mDestinationUri)
                .withOptions(options)
                .withAspectRatio(3.1f, 1.3f)
                .withMaxResultSize(maxWidth, maxHeight)
                .start(getActivity(), THUMBNAIL_CROP);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == UCrop.REQUEST_CROP) {
                final Uri cropUri = UCrop.getOutput(data);
                resultUri[1] = cropUri;
                thumbnailCrop();
            } else if (requestCode == THUMBNAIL_CROP) {
                final Uri cropUri = UCrop.getOutput(data);
                resultUri[0] = cropUri;
                modifyImage();
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
}
