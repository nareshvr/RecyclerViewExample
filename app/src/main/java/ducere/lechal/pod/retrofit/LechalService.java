package ducere.lechal.pod.retrofit;

import ducere.lechal.pod.constants.Constants;
import ducere.lechal.pod.server_models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LechalService {

    String USER_LOGIN_TYPE = "102";
    String USER_GET_TYPE = "104";
    String USER_UPLOAD_PROFILE_PICTURE_TYPE = "109";
    String USER_REGISTER_TYPE = "101";
    String STATUS_CODE_SUCCESS = "150";
    @FormUrlEncoded
    @POST(Constants.USER)
    Call<ResponseBody> loginUser(@Field("email") String email, @Field("password") String password, @Field("type") String type);

    @FormUrlEncoded
    @POST(Constants.USER)
    Call<User> getUser(@Field("userId") String email, @Field("type") String type);

    @FormUrlEncoded
    @POST(Constants.USER)
    Call<User> uploadProfilePicture(@Field("userId") String userId, @Field("image") String image, @Field("type") String type);

    @FormUrlEncoded
    @POST(Constants.USER)
    Call<ResponseBody> register(@Field("firstName") String firstName, @Field("lastName") String lastName, @Field("email") String email, @Field("password") String password,@Field("type") String type);

}