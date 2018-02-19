package mobi.newsound.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataAccess;
import mobi.newsound.model.Landmark;
import mobi.newsound.model.Route;
import mobi.newsound.utils.JSONResponse;
import mobi.newsound.utils.RESTRoute;
import spark.Request;
import spark.Response;

public class AddLandmarksToRoutesController implements RESTRoute{

    private static final Gson gson = new Gson();

    @Override
    public Object handle(Request request, Response response, JsonObject body) throws Exception {

        AuthContext context = extractFromBody(body);
        Landmark landmark = gson.fromJson(body.get("landmark"), Landmark.class);
        Route route = gson.fromJson(body.get("route"), Route.class);

        try(DataAccess db = DataAccess.getInstance()) {
            assert db != null;
            db.addLandmarksToRoutes(context, landmark);

            return JSONResponse.SUCCESS();
        } catch (Exception e) {
            return JSONResponse.FAILURE().message("Error: " + e.getMessage());
        }

    }
}
