package ducere.lechal.pod.beans;

import java.io.Serializable;

/**
 * Created by durga on 10-10-2015.
 */
public class Contacts implements Serializable {
    private String name;
    private String email;
    private String image;



    public Contacts(String n, String e) { name = n; email = e; }
    public Contacts(String n, String e, String img) { name = n; email = e; image = img; }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getImage() {
        return image;
    }

    @Override
    public String toString() { return name; }
}