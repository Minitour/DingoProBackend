package mobi.newsound.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataAccess;
import mobi.newsound.model.Defendant;
import mobi.newsound.utils.JSONResponse;
import mobi.newsound.utils.RESTRoute;
import spark.Request;
import spark.Response;

public class CreateDefendantController implements RESTRoute {

    private static final Gson gson = new Gson();

    @Override
    public Object handle(Request request, Response response, JsonObject body) throws Exception {

        AuthContext context = extractFromBody(body);
        Defendant defendant = gson.fromJson(body.get("defendant"), Defendant.class);

        try(DataAccess db = DataAccess.getInstance()) {
            assert db != null;
            db.createDefendant(context, defendant);

            return JSONResponse.SUCCESS();
        } catch (Exception e) {
            return JSONResponse.FAILURE().message("Error: " + e.getMessage());
        }
    }
}
