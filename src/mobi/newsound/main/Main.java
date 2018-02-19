package mobi.newsound.main;

import mobi.newsound.controllers.*;
import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataAccess;
import mobi.newsound.model.*;
import mobi.newsound.utils.JSONResponse;
import mobi.newsound.utils.JSONTransformer;
import mobi.newsound.utils.RESTRoute;
import mobi.newsound.utils.Stub;
import org.apache.log4j.BasicConfigurator;

import java.util.Date;

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
        make("/getAccounts",new GetAccountsController());
        make("/getAppeals",new GetAppealsController());
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
            try(DataAccess db = DataAccess.getInstance()) {
                return db.signIn("root@system.net", "password");
            }catch (DataAccess.DSException e) {
                return JSONResponse.FAILURE().message(e.getMessage());
            }
        },new JSONTransformer());

        //test2 Update password - Done & WORKING!

        //test  create defendant - OK!
        get("/test3", "application/json", (request, response) -> {
            response.header("Content-Type", "application/json");
            try (DataAccess db = DataAccess.getInstance()){
                AuthContext context = db.signIn("root@system.net", "password");
                Defendant defendant = Stub.getDefendantStub();
                return db.createDefendant(context, defendant);
            }catch (DataAccess.DSException e) {
                return JSONResponse.FAILURE().message(e.getMessage());
            }
        }, new JSONTransformer());

        //test add landmark to route - OK!
        get("/test5", "application/json", (request, response) -> {
           response.header("Content-Type", "application/json");
           try(DataAccess db = DataAccess.getInstance()) {
               AuthContext context = db.signIn("root@system.net", "password");
               Route route = new Route(1);
               Landmark landmark = new Landmark(route, 1, null, "34", "34");
               return db.addLandmarksToRoutes(context, landmark, route);
           } catch (DataAccess.DSException e) {
               return JSONResponse.FAILURE().message(e.getMessage());
           }
        });

        //test assign officer to partnership - OK!
        get("/test6", "application/json", (request, response) -> {
           response.header("Content-Type", "application/json");
            try(DataAccess db = DataAccess.getInstance()) {
                AuthContext context = db.signIn("root@system.net", "password");
                Partnership partnership = new Partnership(1, null, null);
                //String ID, String EMAIL, Integer ROLE_ID, String pin, String name, String phoneExtension, int position, Partnership ptship
                OperationalOfficer operationalOfficer = new OperationalOfficer("12345", "tomer12_3@hotmai.com", 2, "123", "jon snow", "04", 2, null);
                return db.assignOfficersToPartnerships(context, operationalOfficer, partnership);
            }catch (DataAccess.DSException e) {
                return JSONResponse.FAILURE().message(e.getMessage());
            }
        });

        //test assign partnership to shift - OK!
        get("/test7", "application/json", (request, response) -> {
            response.header("Content-Type", "application/json");
            try(DataAccess db = DataAccess.getInstance()) {
                AuthContext context = db.signIn("root@system.net", "password");
                Partnership partnership = new Partnership(2, null, null);
                //int shiftCode, Date shiftDate, String type
                Shift shift = new Shift(1, null, null);
                return db.assignPartnershipToShift(context, partnership, shift);
            }catch (DataAccess.DSException e) {
                return JSONResponse.FAILURE().message(e.getMessage());
            }
        }, new JSONTransformer());

        //assign route to shift - OK!
        get("/test9", "application/json", (request, response) -> {
            response.header("Content-Type", "application/json");
            try(DataAccess db = DataAccess.getInstance()) {
                AuthContext context = db.signIn("root@system.net", "password");
                Route route = new Route(1);
                //int shiftCode, Date shiftDate, String type
                Shift shift = new Shift(1, null, null);
                return db.assignRouteToShift(context, route, shift);
            }catch (DataAccess.DSException e) {
                return JSONResponse.FAILURE().message(e.getMessage());
            }
        }, new JSONTransformer());

        //test create partnership - OK!
        get("/test10", "application/json", (request, response) -> {
            response.header("Content-Type", "application/json");
            try(DataAccess db = DataAccess.getInstance()) {
                AuthContext context = db.signIn("root@system.net", "password");
                Partnership partnership = new Partnership(2, null, null);
                return db.createPartnership(context, partnership);
            }catch (DataAccess.DSException e) {
                return JSONResponse.FAILURE().message(e.getMessage());
            }
        }, new JSONTransformer());



        //test add appeal to report - OK!
        get("/test4", "application/json", (request, response) -> {
            response.header("Content-Type", "application/json");
            try (DataAccess db = DataAccess.getInstance()) {
                AuthContext context = db.signIn("root@system.net", "password");
                Appeal appeal = new Appeal(1,  null, null);
                Report report = new Report("1", null, null, null, null, null, null, null);
                return db.addAppealToReport(context, appeal, report);
            }catch (DataAccess.DSException e) {
                return JSONResponse.FAILURE().message(e.getMessage());
            }
        });

        //test get defendants - OK!
        //test get shifts - OK!
        //test get partnerships - OK!
        //test get all appeals - OK!
        //test get all routes - OK!
        //test get all reports - OK!
        //test get landmarks - OK!
        //test get vehicle models - OK!
        //test get vehicles - OK!

        //test submit appeal - NOT working!
        get("/test8", "application/json", (request, response) -> {
            response.header("Content-Type", "application/json");
            try(DataAccess db = DataAccess.getInstance()) {
                AuthContext context = db.signIn("root@system.net", "password");
                Appeal appeal = new Appeal(124, "reason45", new Date());
                Report report = new Report("42324234234", null, null, null, null, null, null, null);
                return db.submitAppeal(context, appeal, report);
            }catch (DataAccess.DSException e) {
                return JSONResponse.FAILURE().message(e.getMessage());
            }
        },new JSONTransformer());


    }
}


//AuthContext context = db.signIn("root@system.net", "password");