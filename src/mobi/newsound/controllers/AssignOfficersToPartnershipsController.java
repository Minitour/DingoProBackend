package mobi.newsound.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataAccess;
import mobi.newsound.model.OperationalOfficer;
import mobi.newsound.model.Partnership;
import mobi.newsound.utils.JSONResponse;
import mobi.newsound.utils.RESTRoute;
import spark.Request;
import spark.Response;

public class AssignOfficersToPartnershipsController implements RESTRoute {

    private static final Gson gson = new Gson();

    @Override
    public Object handle(Request request, Response response, JsonObject body) throws Exception {

        AuthContext context = extractFromBody(body);
        OperationalOfficer operationalOfficer = gson.fromJson(body.get("officer"), OperationalOfficer.class);
        Partnership partnership = gson.fromJson(body.get("partnership"), Partnership.class);

        try(DataAccess db = DataAccess.getInstance()) {
            assert db != null;
            db.assignOfficersToPartnerships(context,operationalOfficer, partnership);

            return JSONResponse.SUCCESS();
        } catch (Exception e) {
            return JSONResponse.FAILURE().message("Error: " + e.getMessage());
        }
    }
}
