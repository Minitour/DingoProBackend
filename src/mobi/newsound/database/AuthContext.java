package mobi.newsound.database;

import com.google.gson.annotations.Expose;

/**
 * Created by Antonio Zaitoun on 09/02/2018.
 */
public class AuthContext {

    @Expose
    public final String id;

    @Expose
    public final String sessionToken;

    private int role = -1;

    public AuthContext(String id, String sessionToken) {
        this.id = id;
        this.sessionToken = sessionToken;
    }

    void setRole(int role) { this.role = role; }

    public boolean isValid() {
        return role != -1;
    }

    public int getRole() { return role; }


}
