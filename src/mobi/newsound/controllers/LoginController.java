package mobi.newsound.controllers;

import com.google.gson.JsonObject;
import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataAccess;
import mobi.newsound.utils.JSONResponse;
import mobi.newsound.utils.RESTRoute;
import spark.Request;
import spark.Response;


/**
 * Created by Antonio Zaitoun on 09/02/2018.
 */
public class LoginController implements RESTRoute {

    @Override
    public Object handle(Request request, Response response, JsonObject body) throws Exception {
        //get login parameters: (email,password)

        try {
            String email = body.get("email").getAsString();
            String raw_password = body.get("password").getAsString();
            //check db and create context
            try (DataAccess db = DataAccess.getInstance() ){
                assert db != null;
                AuthContext context = db.signIn(email,raw_password);

                return JSONResponse
                        .SUCCESS()
                        .data(context);

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
