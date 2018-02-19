package mobi.newsound.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataAccess;
import mobi.newsound.model.Appeal;
import mobi.newsound.model.Report;
import mobi.newsound.utils.JSONResponse;
import mobi.newsound.utils.RESTRoute;
import spark.Request;
import spark.Response;

public class SubmitAppealController implements RESTRoute {

    private static final Gson gson = new Gson();

    @Override
    public Object handle(Request request, Response response, JsonObject body) throws Exception {
        AuthContext context = extractFromBody(body);

        Report report = gson.fromJson(body.get("report"), Report.class);
        Appeal appeal = gson.fromJson(body.get("appeal"), Appeal.class);

        try(DataAccess db = DataAccess.getInstance()) {

            db.submitAppeal(context, appeal, report);

            return JSONResponse.SUCCESS();
        } catch (Exception e) {
            return JSONResponse.FAILURE().message(e.getMessage());
        }
    }
}
