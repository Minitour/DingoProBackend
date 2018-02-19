package mobi.newsound.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataAccess;
import mobi.newsound.model.Route;
import mobi.newsound.model.Shift;
import mobi.newsound.utils.JSONResponse;
import mobi.newsound.utils.RESTRoute;
import spark.Request;
import spark.Response;

public class AssignRouteToShiftController implements RESTRoute {

    private static final Gson gson = new Gson();

    @Override
    public Object handle(Request request, Response response, JsonObject body) throws Exception {

        AuthContext context = extractFromBody(body);
        Route route = gson.fromJson(body.get("route"), Route.class);
        Shift shift = gson.fromJson(body.get("shift"), Shift.class);

        try(DataAccess db = DataAccess.getInstance()) {
            assert db != null;
            db.assignRouteToShift(context, route, shift);

            return JSONResponse.SUCCESS();
        } catch (Exception e) {
            return JSONResponse.FAILURE().message("Error: " + e.getMessage());
        }

    }
}
