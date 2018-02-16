package mobi.newsound.database;

import mobi.newsound.models.*;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.Date;
//import net.ucanaccess.jdbc.UcanaccessDriver;
import static mobi.newsound.util.Config.config;

class Database implements DataStore{

    final static int MAX_ALLOWED_SESSIONS = config.get("user").getAsJsonObject().get("max_allowed_sessions").getAsInt();
    final static long MAX_TIME_OUT = config.get("user").getAsJsonObject().get("session_time_out").getAsInt();
    final static String DB_LOCATION = config.get("db").getAsJsonObject().get("access_file_location").getAsString();
    final static String JASPER_BIN = config.get("RF_REPORT_BINARY").getAsString();
    private Connection connection;

    Database() throws SQLException {
        connection = getUcanaccessConnection(new File(DB_LOCATION).getAbsolutePath());
    }

    private Connection getUcanaccessConnection(String pathNewDB) throws SQLException{
  //      String url = UcanaccessDriver.URL_PREFIX + pathNewDB+";Columnorder=Display;Showschema=true";
    //    return DriverManager.getConnection(url, null, null);
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    @Override
    public boolean isValid(AuthContext context) throws DSException {
        return false;
    }

    @Override
    public AuthContext signIn(String email, String password_raw) throws DSException {
        return null;
    }

    @Override
    public boolean updatePassword(AuthContext context, String currentPassword, String newPassword) throws DSException {
        return false;
    }

    @Override
    public String resetPassword(AuthContext context) throws DSException {
        return null;
    }

    @Override
    public boolean addAppealToReport(AuthContext context, Appeal appeal, Report report) throws DSException {
        return false;
    }

    @Override
    public boolean addLandmarksToRoutes(AuthContext context, Landmark landmark, Route route) throws DSException {
        return false;
    }

    @Override
    public boolean assignOfficersToPartnerships(AuthContext context, List<OperationalOfficer> officers, List<Partnership> partnerships) throws DSException {
        return false;
    }

    @Override
    public boolean assignPartnershipToShift(AuthContext context, Partnership partnership, Shift shift) throws DSException {
        return false;
    }

    @Override
    public boolean assignRouteToShift(AuthContext context, Route route, Shift shift) throws DSException {
        return false;
    }

    @Override
    public Defendant createDefendant(AuthContext context, Defendant defendant) throws DSException {
        return null;
    }

    @Override
    public OfficerReport createOfficerReport(AuthContext context, OfficerReport report) throws DSException {
        return null;
    }

    @Override
    public Partnership createPartnership(AuthContext context, Partnership partnership) throws DSException {
        return null;
    }

    @Override
    public VolunteerReport createVolunteerReport(AuthContext context, VolunteerReport report) throws DSException {
        return null;
    }

    @Override
    public Report CreateReportFromExistReport(AuthContext context, VolunteerReport volunteerReport, OfficerReport officerReport) throws DSException {
        return null;
    }

    @Override
    public void getAppealExport(AuthContext context, List<Report> reports) throws DSException {

    }

    @Override
    public void getReportExportByDate(AuthContext context, Date from, Date to, List<Report> reports) throws DSException {

    }

    @Override
    public void getAllReportsExportToDingoReport(AuthContext context, List<OfficerReport> officerReports) throws DSException {

    }

    @Override
    public List<Report> getAllReports(AuthContext context) throws DSException {
        return null;
    }

    @Override
    public List<Appeal> getAllAppeals(AuthContext context) throws DSException {
        return null;
    }

    @Override
    public List<Defendant> getAllDefendants(AuthContext context) throws DSException {
        return null;
    }

    @Override
    public List<Landmark> getLandmarks(AuthContext context) throws DSException {
        return null;
    }

    @Override
    public List<Partnership> getPartnetships(AuthContext context) throws DSException {
        return null;
    }

    @Override
    public List<Report> getReportsFromDingoReport(AuthContext context) throws DSException {
        return null;
    }

    @Override
    public List<Route> getRoutes(AuthContext context) throws DSException {
        return null;
    }

    @Override
    public List<Shift> getShifts(AuthContext context) throws DSException {
        return null;
    }

    @Override
    public List<VehicleModel> getVehicleModels(AuthContext context) throws DSException {
        return null;
    }

    @Override
    public List<Vehicle> getVehicles(AuthContext context) throws DSException {
        return null;
    }

    @Override
    public boolean submitAppeal(AuthContext context, Appeal appeal) throws DSException {
        return false;
    }
}
