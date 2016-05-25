package ducere.lechal.pod.retrofit;

import ducere.lechal.pod.constants.Constants;
import ducere.lechal.pod.server_models.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface LechalService {

    String USER_LOGIN_TYPE = "102";
    String USER_GET_TYPE = "104";
    String USER_UPLOAD_PROFILE_PICTURE_TYPE = "109";
    String USER_REGISTER_TYPE = "101";
    String STATUS_CODE_SUCCESS = "150";
    String USER_CREATE_OR_UPDATE_TYPE = "103";


    @FormUrlEncoded
    @POST(Constants.USER)
    Call<ResponseBody> loginUser(@Field("email") String email, @Field("password") String password, @Field("type") String type);

    @FormUrlEncoded
    @POST(Constants.USER)
    Call<User> getUser(@Field("userId") String email, @Field("type") String type);

    @Multipart
    @Headers("Content-type:multipart/form-data")
    @POST(Constants.USER)
    Call<ResponseBody> uploadProfilePicture(@Part("userId") RequestBody userId,  @Part("type") RequestBody type,@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(Constants.USER)
    Call<ResponseBody> register(@Field("firstName") String firstName, @Field("lastName") String lastName, @Field("email") String email, @Field("password") String password, @Field("type") String type);

    @FormUrlEncoded
    @POST(Constants.USER)
    Call<ResponseBody> updateProfile(@Field("fullName") String fullName, @Field("country") String country, @Field("dob") String dob, @Field("emial") String emial, @Field("phno") String phno, @Field("emergencyNo") String emergencyNo, @Field("type") String type);

}