package dct.com.everyfoody.request;

import dct.com.everyfoody.base.BaseModel;
import dct.com.everyfoody.model.CheckId;
import dct.com.everyfoody.model.EditMenu;
import dct.com.everyfoody.model.Login;
import dct.com.everyfoody.model.MainList;
import dct.com.everyfoody.model.Menu;
import dct.com.everyfoody.model.Notification;
import dct.com.everyfoody.model.OpenLocation;
import dct.com.everyfoody.model.RegisterStore;
import dct.com.everyfoody.model.ResLocation;
import dct.com.everyfoody.model.ResReview;
import dct.com.everyfoody.model.Reservation;
import dct.com.everyfoody.model.SideMenu;
import dct.com.everyfoody.model.StoreInfo;
import dct.com.everyfoody.model.Turn;
import dct.com.everyfoody.model.UserInfo;
import dct.com.everyfoody.model.UserStatus;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Jyoung on 2017-07-18.
 */

public interface NetworkService {

    //로그인
    @POST("/signin")
    Call<Login> userLogin(@Body UserInfo userInfo);

    //회원가입(이용자)
    @POST("/signup/customer")
    Call<BaseModel> userSignUp(@Body UserInfo userInfo);

    //회원가입(사용자)
    @POST("/signup/owner")
    Call<BaseModel> ownerSignUp(@Body UserInfo ownerInfo);

    //메인
    @GET("/main/lists/{location}/{latitude}/{longitude}")
    Call<MainList> getMainLists(@Header("token") String token, @Path("location") int location, @Path("latitude") double lat, @Path("longitude") double lon );

    //디테일
    @GET("/store/info/{storeID}")
    Call<StoreInfo> getStoreInfo(@Header("token") String token, @Path("storeID") int storeId);

    //예약하기
    @GET("/reservation/compilation/{storeID}")
    Call<BaseModel> userReseve(@Header("token") String token, @Path("storeID") int storeId);

    //예약 현황(이용자)
    @GET("/reservation/lists")
    Call<Reservation> getReservationList(@Header("token") String token);

    //즐겨찾기
    @GET("/bookmark/compilation/{storeID}")
    Call<BaseModel> userBookmark(@Header("token") String token, @Path("storeID") int storeId);

    //즐겨찾기 리스트
    @GET("/bookmark/lists/{latitude}/{longitude}")
    Call<MainList> getBookmarkList(@Header("token") String token, @Path("latitude") double lat, @Path("longitude") double log);

    //리뷰 리스트
    @GET("/review/lists/{storeID}")
    Call<ResReview> getReviewList(@Header("token") String token, @Path("storeID") int storeId);

    //리뷰 등록
    @Multipart
    @POST("/review/registration")
    Call<BaseModel> registerReview(@Header("token") String token,
                                   @Part MultipartBody.Part files,
                                   @Part("storeID") RequestBody storeId,
                                   @Part("score") RequestBody score,
                                   @Part("content") RequestBody content);

    //가게 위치
    @GET("/store/location/{storeID}")
    Call<ResLocation> getLocation(@Header("token") String token, @Path("storeID") int storeId);

    //가게 열기
    @PUT("/management/registration/opening")
    Call<BaseModel> openStore(@Header("token") String token, @Body OpenLocation openLocation);

    //가게 닫기
    @PUT("/management/registration/closing")
    Call<BaseModel> closeStore(@Header("token") String token);

    //내 가게 정보
    @GET("/management/ownerinfo/basicinfo")
    Call<StoreInfo> getMyStoreInfo(@Header("token") String token);

    //예약 현황(사업자)
    @GET("/management/customers/lists")
    Call<Turn> getTurnList(@Header("token") String token);

    //메뉴 추가
    @Multipart
    @PUT("/management/menuinfo/addition")
    Call<BaseModel> registerMenu(@Header("token") String token,
                                 @Part MultipartBody.Part file,
                                 @Part("menu_name") RequestBody menuName,
                                 @Part("menu_price") RequestBody menuPrice);

    //메뉴 수정
    @Multipart
    @PUT("/management/menuinfo/modification/{menu_id}")
    Call<BaseModel> editMenu(@Header("token") String token,
                                 @Path("menu_id") int menuId,
                                 @Part MultipartBody.Part file,
                                 @Part("menu_name") RequestBody menuName,
                                 @Part("menu_price") RequestBody menuPrice);

    //메뉴 수정 (텍스트만)
    @POST("/management/menuinfo/modification/{menu_id}")
    Call<BaseModel> editMenuNoimage(@Header("token") String token,@Path("menu_id") int menuId, @Body EditMenu editMenu);

    //메뉴 삭제
    @DELETE("/management/menuinfo/remove/{menu_id}")
    Call<BaseModel> deleteMenu(@Header("token") String token, @Path("menu_id") int menuId);

    //사이드 메뉴
    @GET("/main/sidemenu/{user_status}")
    Call<SideMenu> getSideMenuInfo(@Header("token") String token, @Path("user_status") int userStatus);

    //토글 체크(이용자)
    @PUT("/main/toggle/user/opening/{owner_id}")
    Call<BaseModel> checkedToggle(@Header("token") String token, @Path("owner_id") int owner_id);

    //토글 체크해제(이용자)
    @PUT("/main/toggle/user/closing/{owner_id}")
    Call<BaseModel> unCheckedToggle(@Header("token") String token, @Path("owner_id") int ownerId);

    //토글 체크(사업자)
    @PUT("/main/toggle/owner/opening/{kind}")
    Call<BaseModel> checkedToggleOwner(@Header("token") String token, @Path("kind") int kind);

    //토글 체크해제(사업자)
    @PUT("/main/toggle/owner/closing/{kind}")
    Call<BaseModel> unCheckedToggleOwner(@Header("token") String token, @Path("kind") int kind);

    //아이디 체크
    @GET("/signup/checking/{user_uid}")
    Call<CheckId> checkId(@Path("user_uid") String uid);

    //프로필 수정
    @Multipart
    @PUT("/management/myprofile")
    Call<BaseModel> modifyProfile(@Header("token") String token,
                                  @Part MultipartBody.Part file);

    //순번 제거
    @DELETE("/management/customers/lists/remove")
    Call<BaseModel> nextGuset(@Header("token") String token);

    //가게 대표 이미지 수정
    @Multipart
    @PUT("/management/ownerinfo/imagemodi")
    Call<BaseModel> modifyStoreImage(@Header("token") String token,
                                     @Part MultipartBody.Part[] files);

    //가게 기본정보 수정
    @POST("/management/ownerinfo/basicmodi")
    Call<BaseModel> modifyBasicInfo(@Header("token") String token,
                                    @Body StoreInfo.BasicInfo basicInfo);

    //알림 리스트
    @PUT("/main/notice/lists")
    Call<Notification> getNotiList(@Header("token") String token);

    //가게 등록
    @PUT("/management/registration/store")
    Call<BaseModel> registerStore(@Header("token") String token, @Body RegisterStore registerStore);

    //메뉴 리스트만
    @GET("/management/menuinfo/lists")
    Call<Menu> getMenuList(@Header("token") String token);

    //라이센스 등록
    @Multipart
    @POST("/management/registration/store")
    Call<BaseModel> registerLicense(@Header("token") String token,
                                    @Part("store_name") RequestBody storeName,
                                    @Part MultipartBody.Part file);

    @GET("/management/check")
    Call<UserStatus> getUserStatus(@Header("token") String token);

    @PUT("/signout")
    Call<BaseModel> logout(@Header("token") String token);

}