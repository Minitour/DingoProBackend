package mobi.newsound.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import mobi.newsound.database.AuthContext;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by Antonio Zaitoun on 09/02/2018.
 */
@FunctionalInterface
public interface RESTRoute extends Route {

    @Override
    default Object handle(Request request, Response response) throws Exception{
        response.header("Content-Type","application/json");
        return handle(request,response,new Gson().fromJson(request.body(), JsonObject.class));
    }

    Object handle(Request request,Response response,JsonObject body) throws Exception;

    default AuthContext extractFromBody(JsonObject body){
        try {
            String id = body.get("id").getAsString();
            String sessionToken = body.get("sessionToken").getAsString();
            return new AuthContext(id,sessionToken);
        }catch (NullPointerException e){
            return null;
        }
    }
}
