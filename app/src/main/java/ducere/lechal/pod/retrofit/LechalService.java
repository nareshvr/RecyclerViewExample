package ducere.lechal.pod.retrofit;

import ducere.lechal.pod.beans.GroupJourney;
import ducere.lechal.pod.constants.Constants;
import ducere.lechal.pod.server_models.ContactsCount;
import ducere.lechal.pod.server_models.Friends;
import ducere.lechal.pod.server_models.GroupCreateResponse;
import ducere.lechal.pod.server_models.User;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface LechalService {

    String USER_LOGIN_TYPE = "102";
    String USER_GET_TYPE = "104";
    String USER_UPLOAD_PROFILE_PICTURE_TYPE = "109";

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
    Call<ContactsCount> sendContacts(@Field("userId") String userId, @Field("contacts") String image, @Field("type") String type);

    @FormUrlEncoded
    @POST(Constants.USER)
    Call<Friends[]> getFriends(@Field("userId") String userId, @Field("type") String type);


    @POST(Constants.NAVIGATE)
    Call<GroupCreateResponse> createGroupJourney(@Body GroupJourney groupJourney);

    @GET
    Call<ResponseBody> fetchProfileImage(@Url String url);
}