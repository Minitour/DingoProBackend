package mobi.newsound.main;

import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataStore;
import mobi.newsound.models.Appeal;
import mobi.newsound.models.Defendant;
import mobi.newsound.models.VolunteerReport;
import mobi.newsound.util.JSONResponse;
import mobi.newsound.util.JSONTransformer;
import mobi.newsound.util.RESTRoute;
import mobi.newsound.util.Stub;
import org.apache.log4j.BasicConfigurator;

import java.util.Date;

import static spark.Spark.get;
import static spark.Spark.port;
import static mobi.newsound.util.Config.config;
import static spark.Spark.post;

public class Main {

    public static void main(String[] args) {

        BasicConfigurator.configure();

        port(config.get("port").getAsInt());

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
                VolunteerReport report = new VolunteerReport("1", null, null, null, null, null, null, null, null);
                return db.addAppealToReport(context, appeal, report);
            }catch (DataStore.DSException e) {
                return JSONResponse.FAILURE().message(e.getMessage());
            }
        });


    }
}


//AuthContext context = db.signIn("root@system.net", "password");