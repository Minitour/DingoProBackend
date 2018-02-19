package mobi.newsound.controllers;

import com.google.gson.JsonObject;
import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataStore;
import mobi.newsound.utils.JSONResponse;
import mobi.newsound.utils.RESTRoute;
import spark.Request;
import spark.Response;

public class ValidateContextController implements RESTRoute {
    @Override
    public Object handle(Request request, Response response, JsonObject body) throws Exception {
        AuthContext context = extractFromBody(body);
        try (DataStore db = DataStore.getInstance()){
            assert db != null;
            boolean value = db.isValid(context);
            if (value)
                return JSONResponse.SUCCESS().data(context.getRole());
            else
                return JSONResponse.FAILURE().message("Invalid Context");

        }catch (Exception e) {
            return JSONResponse.FAILURE().message(e.getMessage());
        }
    }
}
