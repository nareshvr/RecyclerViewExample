package ducere.lechal.pod.server_models;

import java.io.Serializable;

/**
 * Created by Siva on 20-05-2016.
 */
public class Friends implements Serializable {
    private String userId;
    private String name;
    private String img;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }


}
