package ducere.lechal.pod.retrofit;

import ducere.lechal.pod.constants.Constants;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sunde on 16-05-2016.
 */
public class RetroClient {

    private static RetroClient retroClient;

    private LechalService service;

    private RetroClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(LechalService.class);
    }

    public static RetroClient getInstance() {
        if (retroClient == null) {
            retroClient = new RetroClient();
        }
        return retroClient;
    }

    public LechalService getService() {
        return service;
    }
}
