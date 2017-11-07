package dct.com.everyfoody.ui.detail.edit;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

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
import dct.com.everyfoody.model.EditMenu;
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
import static dct.com.everyfoody.ui.detail.edit.EditActivity.MENU_ADD;
import static dct.com.everyfoody.ui.detail.edit.EditActivity.MENU_EDIT;
import static dct.com.everyfoody.ui.login.LoginActivity.AUTH_TOKEN;
import static dct.com.everyfoody.ui.login.LoginActivity.NETWORK_SUCCESS;


public class EditMenuActivity extends WhiteThemeActivity {
    @BindView(R.id.edit_menu_toolbar)Toolbar editToolbar;
    @BindView(R.id.edit_menu_title)TextView titleText;
    @BindView(R.id.menu_item_edit_image)ImageView menuImage;
    @BindView(R.id.menu_item_name)EditText menuName;
    @BindView(R.id.menu_item_price)EditText menuPrice;

    private Uri resultUri;
    private NetworkService networkService;
    private int flag, menuId, imageEditFlag;
    private StoreInfo.MenuInfo menuInfo;

    private final int IMAGE_EDIT = 1301;
    private final int IMAGE_NON_EDIT = 1302;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        ButterKnife.bind(this);

        initData();
        setToolbar();

    }

    private void initData(){
        SharedPreferencesService.getInstance().load(this);
        networkService = ApplicationController.getInstance().getNetworkService();
        Intent getData = getIntent();
        flag = getData.getExtras().getInt("addORedit");
        imageEditFlag = IMAGE_NON_EDIT;
        if(flag == MENU_EDIT){
            Gson gson = new Gson();
            menuInfo = gson.fromJson(getData.getExtras().getString("menuItem"), StoreInfo.MenuInfo.class);
            menuId = menuInfo.getMenuID();
            titleText.setText("메뉴정보 수정");
            setLayout();
        }
    }

    private void setToolbar(){
        editToolbar.setTitle("");
        setSupportActionBar(editToolbar);
        editToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        editToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setLayout(){
        Glide.with(this).load(menuInfo.getMenuImageURL()).into(menuImage);
        menuName.setText(menuInfo.getMenuTitle());
        menuPrice.setText(menuInfo.getMenuPrice()+"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.complete_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_complete) {
           networkMenuModify();
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem deleteIcon = menu.findItem(R.id.menu_delete);
        deleteIcon.setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    private void networkMenuModify(){

        RequestBody name = RequestBody.create(MediaType.parse("multipart/form-data"), menuName.getText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("multipart/form-data"), menuPrice.getText().toString());

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
        if(flag == MENU_ADD) {
            Call<BaseModel> menuAddCall = networkService.registerMenu(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN), body, name, price);

            menuAddCall.enqueue(new Callback<BaseModel>() {
                @Override
                public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                            ToastMaker.makeShortToast(getApplicationContext(), "메뉴가 추가되었습니다.");
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BaseModel> call, Throwable t) {
                    LogUtil.d(getApplicationContext(), t.toString());
                }
            });
        }
        else if(flag == MENU_EDIT){
            if(imageEditFlag == IMAGE_EDIT) {

                Call<BaseModel> menuEditCall = networkService.editMenu(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN), menuId, body, name, price);

                menuEditCall.enqueue(new Callback<BaseModel>() {
                    @Override
                    public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                                ToastMaker.makeShortToast(getApplicationContext(), "메뉴가 수정되었습니다.");
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseModel> call, Throwable t) {
                        LogUtil.d(getApplicationContext(), t.toString());
                    }
                });
            }
            else if(imageEditFlag == IMAGE_NON_EDIT){
                EditMenu editMenu = new EditMenu();
                editMenu.setMenuImage(menuInfo.getMenuImageURL());
                editMenu.setMenuName(menuName.getText().toString());
                editMenu.setMenuPrice(menuPrice.getText().toString());

                Call<BaseModel> menuEditCall = networkService.editMenuNoimage(SharedPreferencesService.getInstance().getPrefStringData(AUTH_TOKEN)
                ,menuInfo.getMenuID(), editMenu);

                menuEditCall.enqueue(new Callback<BaseModel>() {
                    @Override
                    public void onResponse(Call<BaseModel> call, Response<BaseModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus().equals(NETWORK_SUCCESS)) {
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseModel> call, Throwable t) {
                        LogUtil.d(getApplicationContext(), t.toString());
                    }
                });
            }
        }


    }

    private void imagePicker(){
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(EditMenuActivity.this)
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
                .withAspectRatio(3.6f, 1.5f)
                .withMaxResultSize(maxWidth, maxHeight)
                .start(this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri tempUri = UCrop.getOutput(data);
            resultUri = tempUri;
            Glide.with(menuImage.getContext())
                    .load(resultUri)
                    .into(menuImage);
            imageEditFlag = IMAGE_EDIT;
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

    @OnClick(R.id.menu_item_edit_image_picker)
    public void onClickPicker(View view){
        checkPermission();
        imagePicker();
    }
}
