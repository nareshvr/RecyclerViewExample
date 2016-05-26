package ducere.lechal.pod.server_models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Siva on 24-05-2016.
 */
public class GroupCreateResponse implements Serializable {
    @SerializedName("status")
    private int status;
    @SerializedName("error")
    private String error;
    @SerializedName("count")
    private String groupId;

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getGroupId() {
        return groupId;
    }
}
