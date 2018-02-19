package mobi.newsound.controllers;

import com.google.gson.JsonObject;
import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataAccess;
import mobi.newsound.utils.JSONResponse;
import mobi.newsound.utils.RESTRoute;
import spark.Request;
import spark.Response;

/**
 * Created By Tony on 10/02/2018
 */
public class UpdatePasswordController implements RESTRoute {
    @Override
    public Object handle(Request request, Response response, JsonObject body) throws Exception {
        try {
            AuthContext context = extractFromBody(body);
            String currentPassword = body.get("currentPassword").getAsString();
            String newPassword = body.get("newPassword").getAsString();

            //check db and create context
            try (DataAccess db = DataAccess.getInstance() ){
                assert db != null;
                boolean didChange = db.updatePassword(context,currentPassword,newPassword);
                if(didChange)
                    return JSONResponse.SUCCESS();
                else
                    return JSONResponse
                            .FAILURE()
                            .message("Error: Incorrect Password!");

            }catch (DataAccess.DSException e){
                return JSONResponse
                        .FAILURE()
                        .message("Error: "+e.getMessage());
            }
        }catch (NullPointerException e){
            return JSONResponse
                    .FAILURE()
                    .message("Error: "+e.getMessage());
        }
    }
}
