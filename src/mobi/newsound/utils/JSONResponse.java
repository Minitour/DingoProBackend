package mobi.newsound.utils;

import com.google.gson.annotations.Expose;

/**
 * Created by Antonio Zaitoun on 09/02/2018.
 */
public class JSONResponse<T> {

    /**
     * Default Success Object
     * @param <T>
     * @return
     */
    public static <T> JSONResponse<T> SUCCESS() { return  new JSONResponse<T>(200,"Success"); }

    /**
     * Default Failure Object
     * @param <T>
     * @return
     */
    public static <T> JSONResponse<T> FAILURE() { return  new JSONResponse<T>(400,"Error"); }

    @Expose
    private int code;

    @Expose
    private String message;

    @Expose
    private T data;

    public JSONResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public JSONResponse data(T data) {
        this.data = data;
        return this;
    }

    public JSONResponse message(String message) {
        this.message = message;
        return this;
    }

    public JSONResponse code(int code) {
        this.code = code;
        return this;
    }
}
