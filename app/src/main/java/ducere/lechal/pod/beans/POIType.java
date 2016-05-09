package ducere.lechal.pod.beans;

/**
 * Created by Siva on 07-05-2016.
 */
public class POIType {
    String type;
    String id;

    public POIType(String type, String id) {
        this.type = type;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
