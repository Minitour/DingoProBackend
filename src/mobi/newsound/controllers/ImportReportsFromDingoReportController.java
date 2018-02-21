package mobi.newsound.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataAccess;
import mobi.newsound.model.Defendant;
import mobi.newsound.model.Report;
import mobi.newsound.model.Vehicle;
import mobi.newsound.utils.JSONResponse;
import mobi.newsound.utils.RESTRoute;
import org.json.XML;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImportReportsFromDingoReportController implements RESTRoute {

    private static final Gson gson = new Gson();
    @Override
    public Object handle(Request request, Response response, JsonObject body) throws Exception {
        AuthContext context = extractFromBody(body);

        String xmlData = body.get("xml").getAsString();

        String json = XML.toJSONObject(xmlData).toString();

        JsonObject object = gson.fromJson(json,JsonObject.class);
        JsonArray array = object.getAsJsonArray("array");

        List<Report> reportList = new ArrayList<>();
        for(JsonElement element : array){
            JsonObject o = element.getAsJsonObject();
            //date
            Date date = new Date(o.get("incidentDate").getAsString());
            //description
            String description = o.get("description").getAsString();
            //violation type
            String violation = null;
            String evidanceLink = null;
            try{
                JsonObject violationJson = o.get("violations").getAsJsonArray().get(0)
                        .getAsJsonObject();
                violation = violationJson.get("type")
                        .getAsJsonObject().get("description")
                        .getAsString();
                evidanceLink = violationJson.get("evidenceLink").getAsString();
            }catch (Exception e){
                try{
                    JsonObject violationJson = o.get("violations").getAsJsonObject();
                    violation = violationJson.get("type").getAsJsonObject().get("description").getAsString();
                    evidanceLink = violationJson.get("evidenceLink").getAsString();
                }catch (Exception ignored){
                    violation = "NOT FOUND";
                }
            }

            //defendant (insert if not exist)
            JsonObject carOwner;
            try{
                carOwner = o.get("vehicle").getAsJsonObject().get("owners").getAsJsonArray().get(0).getAsJsonObject();
            }catch (Exception e){
                try {
                    carOwner = o.get("vehicle").getAsJsonObject().get("owners").getAsJsonObject();
                }catch (Exception ignored){
                    carOwner = null;
                }
            }
            Integer defId = carOwner.get("id").getAsInt();
            Integer defDL = carOwner.get("drivingLicense").getAsInt();
            String name = carOwner.get("name").getAsString();
            String address = carOwner.get("address").getAsString();
            Defendant defendant = new Defendant(defId,defDL.toString(),name,address);


            //vehicle (insert if not exists)
            Vehicle v = gson.fromJson(o.get("vehicle"), Vehicle.class);

            //(String alphaNum, Date violationDate, String description, String status, String violationType, Defendant defendant, Vehicle vehicle, Appeal appeal)
            //evidence link
            Report report = new Report(
                    null,
                    date,
                    description,
                    null,
                    violation,
                    defendant,
                    v,
                    null);
            report.setEvidenceLink(evidanceLink);
            report.setReport_type(0);
            reportList.add(report);
        }
        //get XML data

        //convert to JSON

        //convert to model object

        //insert to db

        try(DataAccess db = DataAccess.getInstance()){
            assert db != null;
            reportList.forEach(report -> db.createReport(context,report));
            return JSONResponse.SUCCESS();
        }catch (Exception e){
            return JSONResponse.FAILURE().message(e.getMessage());
        }
    }
}
