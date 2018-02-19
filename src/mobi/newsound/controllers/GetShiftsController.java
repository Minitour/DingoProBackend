package mobi.newsound.controllers;

import com.google.gson.JsonObject;
import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataAccess;
import mobi.newsound.model.Route;
import mobi.newsound.model.Shift;
import mobi.newsound.utils.JSONResponse;
import mobi.newsound.utils.RESTRoute;
import spark.Request;
import spark.Response;

import java.util.List;

public class GetShiftsController implements RESTRoute{
    @Override
    public Object handle(Request request, Response response, JsonObject body) throws Exception {
        AuthContext context = extractFromBody(body);

        try(DataAccess db = DataAccess.getInstance()) {
            assert  db != null;
            List<Shift> shiftList = db.getShifts(context);

            return JSONResponse.SUCCESS().data(shiftList);
        }catch (Exception e) {
            return JSONResponse.FAILURE().message(e.getMessage());
        }
    }
}
