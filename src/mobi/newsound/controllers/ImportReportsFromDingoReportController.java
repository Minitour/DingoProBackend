package mobi.newsound.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataAccess;
import mobi.newsound.utils.JSONResponse;
import mobi.newsound.utils.RESTRoute;
import org.json.XML;
import spark.Request;
import spark.Response;

public class ImportReportsFromDingoReportController implements RESTRoute {

    private static final Gson gson = new Gson();
    @Override
    public Object handle(Request request, Response response, JsonObject body) throws Exception {
        AuthContext context = extractFromBody(body);

        String xmlData = body.get("xml").getAsString();

        String json = XML.toJSONObject(xmlData).toString();

        JsonObject object = gson.fromJson(json,JsonObject.class);

        //get XML data

        //convert to JSON

        //convert to model object

        //insert to db

        try(DataAccess db = DataAccess.getInstance()){

            return JSONResponse.SUCCESS();
        }catch (Exception e){
            return JSONResponse.FAILURE();
        }
    }
}
