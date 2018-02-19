package mobi.newsound.main;

import mobi.newsound.controllers.*;
import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataStore;
import mobi.newsound.model.Appeal;
import mobi.newsound.model.Defendant;
import mobi.newsound.model.Report;
import mobi.newsound.utils.JSONResponse;
import mobi.newsound.utils.JSONTransformer;
import mobi.newsound.utils.RESTRoute;
import mobi.newsound.utils.Stub;
import org.apache.log4j.BasicConfigurator;

import static spark.Spark.get;
import static spark.Spark.port;
import static mobi.newsound.utils.Config.config;
import static spark.Spark.post;

public class Main {

    public static void main(String[] args) {

        BasicConfigurator.configure();

        port(config.get("port").getAsInt());

        make("/signin",new LoginController());
        make("/updatePassword",new UpdatePasswordController());
        make("/createUser",new CreateUserController());

        //TODO: remove this later
        initTests();
    }

    static void make(String route, RESTRoute controller){
        post(route, "application/json", controller,new JSONTransformer());




    }

    static void initTests() {

        //test the sign in - OK!
        get("/test1", "application/json", (request, response) -> {
            response.header("Content-Type", "application/json");
            try(DataStore db = DataStore.getInstance()) {
                return db.signIn("root@system.net", "password");
            }catch (DataStore.DSException e) {
                return JSONResponse.FAILURE().message(e.getMessage());
            }
        },new JSONTransformer());

        //test2 Update password - Done & WORKING!

        //test  create defendant - OK!
        get("/test3", "application/json", (request, response) -> {
            response.header("Content-Type", "application/json");
            try (DataStore db = DataStore.getInstance()){
                AuthContext context = db.signIn("root@system.net", "password");
                Defendant defendant = Stub.getDefendantStub();
                return db.createDefendant(context, defendant);
            }catch (DataStore.DSException e) {
                return JSONResponse.FAILURE().message(e.getMessage());
            }
        }, new JSONTransformer());

        //test get defendants - OK!
        //test add appeal to report - OK!
        get("/test4", "application/json", (request, response) -> {
            response.header("Content-Type", "application/json");
            try (DataStore db = DataStore.getInstance()) {
                AuthContext context = db.signIn("root@system.net", "password");
                Appeal appeal = new Appeal(1,  null, null);
                Report report = new Report("1", null, null, null, null, null, null, null);
                return db.addAppealToReport(context, appeal, report);
            }catch (DataStore.DSException e) {
                return JSONResponse.FAILURE().message(e.getMessage());
            }
        });


    }
}


//AuthContext context = db.signIn("root@system.net", "password");