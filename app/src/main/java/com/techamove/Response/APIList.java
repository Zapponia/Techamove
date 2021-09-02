package com.techamove.Response;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIList {

    @FormUrlEncoded
    @POST("login")
    Call<ResponseBody> login(@Field("mobile_number") String mobile_number,
                             @Field("country_code") String country_code);

    @FormUrlEncoded
    @POST("login-with-mail")
    Call<ResponseBody> loginEmail(@Field("email") String mobile_numbel,
                                  @Field("password") String password,
                                  @Field("device_token") String device_token,
                                  @Field("device_type") String device_type);


    @Multipart
    @POST("register")
    Call<ResponseBody> register
            (@Part("full_name") RequestBody full_name,
             @Part("email") RequestBody email,
             @Part("mobile_number") RequestBody mobile_number,
             @Part("account_type") RequestBody account_type,
             @Part("device_token") RequestBody device_token,
             @Part("in_app_purchase_id") RequestBody in_app_purchase_id,
             @Part MultipartBody.Part User_Profile_Pic,
             @Part("device_type") RequestBody device_type,
             @Part("country_code") RequestBody country_code,
             @Part("password") RequestBody password,
             @Part("parent_id") RequestBody parent_id
            );


    @FormUrlEncoded
    @POST("verifyotp")
    Call<ResponseBody> verifyotp(@Field("verify_otp") String verify_otp,
                                 @Field("device_token") String device_token,
                                 @Field("device_type") String device_type);

    @FormUrlEncoded
    @POST("showcardlist")
    Call<ResponseBody> showcardlist(@Field("users id") String id);

    @FormUrlEncoded
    @POST("addnewcard")
    Call<ResponseBody> addnewcard(@Field("users_id") String users_id,
                                  @Field("full_name") String full_name,
                                  @Field("email") String email,
                                  @Field("mobile_number") String mobile_number,
                                  @Field("business_name") String business_name,
                                  @Field("business_service") String business_service,
                                  @Field("facebook") String facebook,
                                  @Field("google") String google,
                                  @Field("linkedin") String linkedin,
                                  @Field("instagram") String instagram,
                                  @Field("website_link") String website_link,
                                  @Field("twitter") String twitter,
                                  @Field("save_status") String save_status,
                                  @Field("country_code") String country_code);

    @FormUrlEncoded
    @POST("updatecarddetails")
    Call<ResponseBody> updatecarddetails(
            @Field("full_name") String full_name,
            @Field("email") String email,
            @Field("mobile_number") String mobile_number,
            @Field("business_name") String business_name,
            @Field("business_service") String business_service,
            @Field("facebook") String facebook,
            @Field("google") String google,
            @Field("linkedin") String linkedin,
            @Field("instagram") String instagram,
            @Field("website_link") String website_link,
            @Field("twitter") String twitter,
            @Field("id") String id,
            @Field("country_code") String country_code);

    @FormUrlEncoded
    @POST("deletecarddetails")
    Call<ResponseBody> deletecarddetails(@Field("business_card_id") String business_card_id);

    @FormUrlEncoded
    @POST("sharecarddelete")
    Call<ResponseBody> sharecarddelete(@Field("business_card_id") String business_card_id);


    @FormUrlEncoded
    @POST("sharecard")
    Call<ResponseBody> sharecard(@Field("card_id") String card_id,
                                 @Field("users_id") String users_id,
                                 @Field("share_id") String share_id);

    @FormUrlEncoded
    @POST("sharevideos")
    Call<ResponseBody> sharevideos(@Field("video_id") String video_id,
                                   @Field("share_id") String share_id);

    @FormUrlEncoded
    @POST("premium")
    Call<ResponseBody> premium(@Field("card_id") String card_id);

    @FormUrlEncoded
    @POST("videopremium")
    Call<ResponseBody> videopremium(@Field("video_id") String video_id);

    @FormUrlEncoded
    @POST("sharelist")
    Call<ResponseBody> sharelist(@Field("users_id") String card_id);


    @POST("logout")
    Call<ResponseBody> logout();


    @POST("savecardlist")
    Call<ResponseBody> savecardlist();

    @FormUrlEncoded
    @POST("savecard")
    Call<ResponseBody> savecard(@Field("card_id") String card_id);

    @FormUrlEncoded
    @POST("savecarddelete")
    Call<ResponseBody> savecarddelete(@Field("business_card_id") String business_card_id);


    @FormUrlEncoded
    @POST("showcarddetails")
    Call<ResponseBody> showcarddetails(@Field("business_card_id") String business_card_id);

    @Multipart
    @POST("uploadvideo")
    Call<ResponseBody> uploadvideo(@Part MultipartBody.Part uploadvideo,
                                   @Part MultipartBody.Part thumb);

    @POST("ownvideo")
    Call<ResponseBody> ownvideo();

    @POST("sharevideoslist")
    Call<ResponseBody> sharevideoslist();

    @FormUrlEncoded
    @POST("purchase")
    Call<ResponseBody> purchase(@Field("email") String email,
                                @Field("in_app_purchase_id") String in_app_purchase_id,
                                @Field("purchased_product_name") String purchased_product_name);

    @FormUrlEncoded
    @POST("licenseverify")
    Call<ResponseBody> licenseverify(@Field("licence_key") String licence_key);


    @Multipart
    @POST("setting")
    Call<ResponseBody> setting(@Part("full_name") RequestBody full_name,
                               @Part MultipartBody.Part User_Profile_Pic);

    @FormUrlEncoded
    @POST("ownvideodelete")
    Call<ResponseBody> ownvideodelete(@Field("video_id") int video_id);

    @FormUrlEncoded
    @POST("sharevideodelete")
    Call<ResponseBody> sharevideodelete(@Field("video_id") int video_id);

    @POST("notification")
    Call<ResponseBody> shownotificationlist();
    //@Field("notification id") String id

    @POST("check-subscription")
    Call<ResponseBody> checkSubscription();


    @GET("api1.php")
    Call<ResponseBody> checkEmail(@Query("email") String email);


    @FormUrlEncoded
    @POST("newUsers")
    Call<ResponseBody> newUsers(@Field("full_name") String full_name,
                                @Field("email") String email,
                                @Field("country_code") String country_code,
                                @Field("mobile_number") String mobile_number,
                                @Field("user_id") String user_id);

    @GET("shop/index.php?route=feed/rest_api/customers")
    Call<ResponseBody> customers();

    @FormUrlEncoded
    @POST("shareusermobile")
    Call<ResponseBody> sendSms(@Field("card_id") String card_id,
                               @Field("share_id") String share_id);


    @FormUrlEncoded
    @POST("changePassword")
    Call<ResponseBody> changePassword(@Field("oldPassword") String oldPassword,
                                      @Field("newPassword") String newPassword);


    @FormUrlEncoded
    @POST("forgot-password")
    Call<ResponseBody> forgotPassword(@Field("email") String email);


    @FormUrlEncoded
    @POST("emailCheck")
    Call<ResponseBody> checkEmailExit(@Field("email") String email);


}
