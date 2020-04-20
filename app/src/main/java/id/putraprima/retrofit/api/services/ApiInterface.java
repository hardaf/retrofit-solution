package id.putraprima.retrofit.api.services;


import android.text.style.UpdateAppearance;

import java.util.List;
import java.util.Map;

import id.putraprima.retrofit.api.models.AppVersion;
import id.putraprima.retrofit.api.models.Envelope;
import id.putraprima.retrofit.api.models.LoginRequest;
import id.putraprima.retrofit.api.models.LoginResponse;
import id.putraprima.retrofit.api.models.Recipe;
import id.putraprima.retrofit.api.models.RegisterRequest;
import id.putraprima.retrofit.api.models.RegisterResponse;
import id.putraprima.retrofit.api.models.UpdatePasswordRequest;
import id.putraprima.retrofit.api.models.UpdateRequest;
import id.putraprima.retrofit.api.models.UpdateResponse;
import id.putraprima.retrofit.api.models.UserInfo;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiInterface{
    @GET("/")
    Call<AppVersion> getAppVersion();

    @POST("/api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("/api/auth/me")
    Call<Envelope<UserInfo>> me(@Header("Authorization") String token);

    @POST("/api/auth/register")
    Call<Envelope<RegisterResponse>> register(@Body RegisterRequest registerRequest);

    @PATCH("/api/account/profile")
    Call<Envelope<UpdateResponse>> updateProfile(@Body UpdateRequest updateRequest);

    @PATCH("/api/account/password")
    Call<Envelope<UpdateResponse>> updatePassword(@Body UpdatePasswordRequest updatePasswordRequest);

    @GET("/api/recipe")
    Call<Envelope<List<Recipe>>> LoadRecipe();

    @GET("/api/recipe")
    Call<Envelope<List<Recipe>>> LoadMoreRecipe(@Query("page") int page);

    @Multipart
    @POST("/api/recipe")
    Call<ResponseBody> doUpload(@Part MultipartBody.Part photo, @PartMap Map<String, RequestBody> text);
}
