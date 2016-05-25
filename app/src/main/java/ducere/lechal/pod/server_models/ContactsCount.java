package ducere.lechal.pod.server_models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Siva on 20-05-2016.
 */
public class ContactsCount implements Serializable {

    @SerializedName("status")
    private String status;
    @SerializedName("error")
    private String error;
    @SerializedName("count")
    private int count;

    public String getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public int getCount() {
        return count;
    }
}
