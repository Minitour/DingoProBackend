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

    public AuthContext(String id, String sessionToken) {
        this.id = id;
        this.sessionToken = sessionToken;
    }
}
