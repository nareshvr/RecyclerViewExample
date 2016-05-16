package ducere.lechal.pod.retrofit;

import ducere.lechal.pod.constants.Constants;
import ducere.lechal.pod.server_models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LechalService {

    @FormUrlEncoded
    @POST(Constants.USER)
    Call<ResponseBody> loginUser(@Field("email") String email, @Field("password") String password, @Field("type") String type);

    @FormUrlEncoded
    @POST(Constants.USER)
    Call<User> getUser(@Field("userId") String email, @Field("type") String type);
}