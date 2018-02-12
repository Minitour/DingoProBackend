package mobi.newsound.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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

    Object handle(Request request, Response response, JsonObject body) throws Exception;
}
