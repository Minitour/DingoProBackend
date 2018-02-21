package mobi.newsound.main;

import mobi.newsound.controllers.*;
import mobi.newsound.database.AuthContext;
import mobi.newsound.database.DataAccess;
import mobi.newsound.model.*;
import mobi.newsound.utils.JSONResponse;
import mobi.newsound.utils.JSONTransformer;
import mobi.newsound.utils.RESTRoute;
import mobi.newsound.utils.Stub;
import net.sf.jasperreports.engine.JasperCompileManager;
import org.apache.log4j.BasicConfigurator;
import spark.Request;
import spark.Response;
import spark.Route;

import java.io.File;
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
        make("/assignOfficerToPartnership",new AssignOfficersToPartnershipsController());
        make("/assignPartnershipToShiftToRoute",new AssignPartnershipToShiftOnRouteController());
        make("/assignRouteToShift",new AssignRouteToShiftController());
        make("/addLandmarkToRoute",new AddLandmarksToRoutesController());
        make("/createDefendant",new CreateDefendantController());
        make("/createPartnership",new CreatePartnershipController());
        make("/createRoute",new CreateRouteController());
        make("/createShift",new CreateShiftController());
        make("/createReport",new CreateReportController());
        make("/getAllReports",new GetAllReportsController());
        make("/getDefendants",new GetDefendantsController());
        make("/getLandmarks",new GetLandmarksController());
        make("/getOfficers",new GetAllOfficersController());
        make("/getRoutes",new GetRoutesController());
        make("/getShifts",new GetShiftsController());
        make("/getVehicleModels",new GetVehicleModelsController());
        make("/getPartnerships",new GetPartnershipsController());
        make("/getVehicles",new GetVehiclesController());
        make("/submitAppeal",new SubmitAppealController());
        make("/exportReportToDingoReport", new ExportReportsToDingoReportController());
        make("/importReports",new ImportReportsFromDingoReportController());
        get("/exportReports",new ExportReportsByDateController());
        get("/exportAppeals",new ExportAppealsForOfficersReportsController());
    }

    static void make(String route, RESTRoute controller){
        post(route, "application/json", controller,new JSONTransformer());
    }

}