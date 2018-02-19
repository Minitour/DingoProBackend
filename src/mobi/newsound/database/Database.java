package mobi.newsound.database;

import mobi.newsound.model.*;
import net.ucanaccess.jdbc.UcanaccessDriver;
import org.mindrot.jbcrypt.BCrypt;

import java.io.File;
import java.sql.*;
import java.util.*;
import java.util.Date;
//import net.ucanaccess.jdbc.UcanaccessDriver;
import static mobi.newsound.utils.Config.config;

class Database implements DataAccess {

    final static int MAX_ALLOWED_SESSIONS = config.get("user").getAsJsonObject().get("max_allowed_sessions").getAsInt();
    final static long MAX_TIME_OUT = config.get("user").getAsJsonObject().get("session_time_out").getAsInt();
    final static String DB_LOCATION = config.get("db").getAsJsonObject().get("access_file_location").getAsString();
    final static String JASPER_BIN = config.get("RF_REPORT_BINARY").getAsString();
    private Connection connection;

    Database() throws SQLException {
        connection = getUcanaccessConnection(new File(DB_LOCATION).getAbsolutePath());
    }

    private Connection getUcanaccessConnection(String pathNewDB) throws SQLException{
        String url = UcanaccessDriver.URL_PREFIX + pathNewDB+";Columnorder=Display;Showschema=true";
        return DriverManager.getConnection(url, null, null);
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }

    @Override
    public boolean isValid(AuthContext context) throws DSException {
        return isContextValid(context) != -1;
    }

    @Override
    public AuthContext signIn(String email, String password_raw) throws DSException {
        //Context Level: NONE
        try {
            List<Map<String,Object>> user = get("SELECT * FROM ACCOUNTS WHERE EMAIL = ?",email);
            if(user.size() != 1)
                //no such user
                throw new DSAuthException("Account Does Not Exist");
            else{
                //check password
                boolean doesMatch = BCrypt.checkpw(password_raw,(String) user.get(0).get("PASSWORD"));
                if(doesMatch){
                    //password is correct
                    String token = TokenGenerator.generateToken();
                    String id = (String) user.get(0).get("ID");
                    int roleId = (int) user.get(0).get("ROLE_ID");
                    insert("SESSIONS",
                            new Column("ID",id),
                            new Column("SESSION_TOKEN",token),
                            new Column("CREATION_DATE",new Date())
                    );

                    //clean up old sessions
                    long timestamp = System.currentTimeMillis();
                    List<Map<String,Object>> sessions = get("SELECT * FROM SESSIONS WHERE ID = ? ORDER BY CREATION_DATE ASC",id);
                    Set<String> toRemove = new HashSet<>();

                    for(Map<String,Object> map : sessions){
                        String sessionToken = (String) map.get("SESSION_TOKEN");

                        if(MAX_TIME_OUT > 0){
                            //check if timed out
                            Date date = (Date) map.get("CREATION_DATE");
                            if(timestamp - date.getTime() > MAX_TIME_OUT)
                                toRemove.add(sessionToken);
                            continue;
                        }

                        if(sessions.size() - toRemove.size() > MAX_ALLOWED_SESSIONS)
                            toRemove.add(sessionToken);

                    }
                    if(toRemove.size() > 0)
                        deleteMany("SESSIONS",
                                "SESSION_TOKEN in (#)",
                                toRemove.toArray(new String[toRemove.size()]));

                    AuthContext context = new AuthContext(id,token);
                    context.setRole(roleId);
                    return context;

                }else throw new DSAuthException("Invalid Password");
            }
        } catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public boolean updatePassword(AuthContext context, String currentPassword, String newPassword) throws DSException {
        try{
            //Context Level: ANY
            if(isContextValid(context) != -1){
                List<Map<String,Object>> user = get("SELECT (PASSWORD) FROM ACCOUNTS WHERE ID = ?",context.id);
                if(user.size() == 1){
                    String hashedPassword = (String) user.get(0).get("PASSWORD");
                    if(BCrypt.checkpw(currentPassword,hashedPassword))
                        //update password
                        return update("ACCOUNTS",
                                new Where("ID = ?",context.id),
                                new Column("PASSWORD",BCrypt.hashpw(newPassword,BCrypt.gensalt())));

                    else
                        throw new DSAuthException("Incorrect password");

                }else
                    throw new DSAuthException("Account Not Found");

            }else throw new DSAuthException("Invalid Context");
        }catch (SQLException e){
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public String resetPassword(AuthContext context) throws DSException {
        return null;
    }

    @Override
    public boolean addAppealToReport(AuthContext context, Appeal appeal, Report report) throws DSException {
        //context: 1

        isContextValidFor(context, roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); }, 1);

        String reportIdString = report.getAlphaNum();
        String reportQuery = "SELECT alphaNum FROM " + report.db_table()+ " WHERE alphaNum = ?";

        try {
            boolean checkReport = get(reportQuery, reportIdString).size() == 1;

            if (checkReport) {
                update(report.db_table(),
                        new Where("alphaNum = ?", report.getAlphaNum()),
                        new Column("appeal", appeal.getSerialNum()));
                return true;
            }
            else
                return false;

        } catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public boolean addLandmarksToRoutes(AuthContext context, Landmark landmark, Route route) throws DSException {
        //context: 1

        isContextValidFor(context, roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); }, 1);
        try {
            update("TblLandmarks",
                    new Where("latitude = ? AND longitude = ?", landmark.getLatitude(), landmark.getLongitude()),
                    new Column("route", route.getSerialNum()));
            return true;
        } catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public boolean assignOfficersToPartnerships(AuthContext context, OperationalOfficer officer, Partnership partnership) throws DSException {
        //context: 1

        isContextValidFor(context, roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); }, 1);
        try {
            update("TblOperationalOfficers",
                    new Where("pin = ?", officer.getPin()),
                    new Column("ptship", partnership.getPtshipNum()));
            return true;
        }catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public boolean assignPartnershipToShift(AuthContext context, Partnership partnership, Shift shift) throws DSException {
        //context: 1

        isContextValidFor(context, roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); }, 1);
        try {
            update("TblShiftPartnerships",
                    new Where("shift = ?", shift.getShiftCode()),
                    new Column("ptship", partnership.getPtshipNum()));
            return true;
        }catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public boolean assignRouteToShift(AuthContext context, Route route, Shift shift) throws DSException {
        //context: 1

        isContextValidFor(context, roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); }, 1);
        try {
            update("TblShiftsPartnerships",
                    new Where("shift = ?", shift.getShiftCode()),
                    new Column("route", route.getSerialNum()));
            return true;
        }catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public boolean createDefendant(AuthContext context, Defendant defendant) throws DSException {
        //context: operational officer  (assumption : operational officer roleId = 2)

        isContextValidFor(context, roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); }, 2);
        try {
            insert("TblDefendants", defendant);
            return true;
        }catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public boolean createOfficerReport(AuthContext context, Report report) throws DSException {

        //check context

        try {
            Report createGeneralReportValidator = createGeneralReport(context, report);

            if (createGeneralReportValidator != null) {

                //checking the partnership
                String partnershipNumString = report.getForeignKey("part");
                String partnershipQuery = "SELECT * FROM TblPartnerships WHERE ptshipNum = ?";
                boolean partnershipExists = get(partnershipQuery, partnershipNumString).size() == 1;

                //checking the route
                String routeString = report.getForeignKey("route");
                String routeQuery = "SELECT * FROM TblRoutes WHERE serialNum = ?";
                boolean routeExists =  get(routeQuery, routeString).size() == 1;

                //checking the orderNum in landmarks Tbl given route exists
                boolean orderNumExists = false;
                String orderNumString = null;
                if (routeExists) {
                    orderNumString = report.getForeignKey("orderNum");
                    String orderNumQuery = "SELECT * FROM TblLandmarks WHERE (route = ? AND orderNum = ?)";
                    orderNumExists = get(orderNumQuery, routeString, orderNumString).size() == 1;
                }

                if (partnershipExists && routeExists && orderNumExists) {
                    update("TblOfficerReport",
                            new Where("alphaNum = ?", report.getAlphaNum()),
                            new Column("part", partnershipNumString));

                    update("TblOfficerReport",
                            new Where("alphaNum = ?", report.getAlphaNum()),
                            new Column("route", routeString));

                    update("TblOfficerReport",
                            new Where("alphaNum = ?", report.getAlphaNum()),
                            new Column("orderNum", orderNumString));

                    return true;
                }
            }
        } catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean createPartnership(AuthContext context, Partnership partnership) throws DSException {
        //context: HighRankOfficer (assumption: HighRankOfficer roleId = 1)

        isContextValidFor(context, roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); }, 1);
        try {
            insert("TblPartnerships", partnership);
            return true;
        } catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
    }

    private Report createGeneralReport(Report report) throws  DSException {
        Report generalReport = null;

        Defendant defendant = report.getDefendant();
        generalReport.setDefendant(defendant);

        Vehicle vehicle = report.getVehicle();
        generalReport.setVehicle(vehicle);

        Appeal appeal = report.getAppeal();
        generalReport.setAppeal(appeal);

        //checking for existing of the objects above -> not exist - > insert
        boolean vehicleExists = false;
        boolean defendantExists = false;
        boolean appealExist = false;

        try {
            //checking for the vehicle
            String vehicleQuery = "SELECT licensePlate FROM TblVehicles WHERE licensePlate = ?";
            vehicleExists = get(vehicleQuery, vehicle.getLicensePlate()).size() == 1;

            //checking for the defendant
            String defendantQuery = "SELECT ID FROM TblDefendants WHERE ID = ?";
            defendantExists = get(defendantQuery, defendant.getId()).size() == 1;

            //checking for the appeal
            String appealQuery = "SELECT serialNum FROM TblAppeals WHERE serialNum = ?";
            appealExist = get(appealQuery, appeal.getSerialNum()).size() == 1;

            //inserting the object if they don't exists in Tbl
            if (!vehicleExists) {

                //checking if the model exists -> else insert to Tbl
                VehicleModel vehicleModel = vehicle.getForeignKey("model");
                String vehicleModelQuery = "SELECT modelNum FROM TblVehicleModel WHERE modelNum = ?";
                boolean vehicleModelExists = get(vehicleModelQuery, vehicleModel.getModelNum()).size() == 1;
                if (!vehicleModelExists)
                    insert(vehicleModel);

                //insert the vehicle to Tbl
                insert(vehicle);
            }

            if (!defendantExists)
                insert(defendant);

            if (!appealExist)
                insert(appeal);

            //insert to the right Tbl
            insert(report);

            generalReport.setAlphaNum(report.getAlphaNum());
            generalReport.setViolationDate(report.getViolationDate());
            generalReport.setDescription(report.getDescription());
            generalReport.setStatus(report.getStatus());
            generalReport.setViolationType(report.getViolationType());

            return generalReport;

        } catch (SQLException e) {
            //rolling back
            try {
                delete("TblVehicles", "licensePlate = ?", vehicle.getLicensePlate());
                delete("TblDefendants", "ID = ?", defendant.getId());
                delete("TblAppeals", "serialNum = ?", appeal.getSerialNum());
            }catch (SQLException e1) {
                throw new DSFormatException(e.getMessage());
            }
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public boolean createVolunteerReport(AuthContext context, Report report) throws DSException {

        //check context

        try {

            Report createGeneralReportValidator = createGeneralReport(context, report);

            if (createGeneralReportValidator != null) {

                //checking the url -> if the url is valid then insert to TblVolunteerReport.evidenceLink -> else return error message
                String url = report.getEvidenceLink();
                boolean validUrl;

                //split to get the type of url
                String[] args = url.split("\\.");
                String urlType = args[args.length - 1];

                //validate by the type of url
                switch (urlType) {
                    case "mp4": {
                        validUrl = validateFileUrl(url, context.id, false, urlType);
                        break;
                    }
                    default: {
                        validUrl = validateFileUrl(url, context.id, true, urlType);
                        break;
                    }
                }

                if (!validUrl) {
                    System.out.println("Invalid url " + url);
                    return false;
                }
                else {
                    update("TblVolunteerReport",
                            new Where("alphaNum = ?", report.getAlphaNum()),
                            new Column("evidenceLink", url));
                    return true;
                }

            }

        } catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
        return false;
    }

    @Override
    public void getAppealExport(AuthContext context, List<Report> reports) throws DSException {

    }

    @Override
    public void getReportsExportByDate(AuthContext context, Date from, Date to, List<Report> reports) throws DSException {

    }

    @Override
    public void getAllOfficerReportsExportToDingoReport(AuthContext context, List<Report> officerReports) throws DSException {

    }

    @Override
    public List<Report> getAllReports(AuthContext context) throws DSException {


            //get the foreignKeys using String fieldString = officerReport.getForeignKey("field")

            //create lists (if needed) for the foreign keys objects

            //for every list do a loop and create an Object for it

            //assign the objects to the officer report

        //get the volunteer reports

        List<Report> reports = new ArrayList<>();
        try {

            //get officer reports
            List<Map<String,Object>> dataFromOfficers = get("SELECT * FROM TblOfficerReport WHERE report_type = ?",1);
            for (Map<String,Object> map: dataFromOfficers) {
                Report officerReport  = new Report(map);

                String vehicleString = officerReport.getForeignKey("vehicle");
                String appealString = officerReport.getForeignKey("appeal");
                String defendantString = officerReport.getForeignKey("defendant");
                String partString = officerReport.getForeignKey("part");
                String routeString = officerReport.getForeignKey("route");
                officerReport.setRoute(new Route(Integer.valueOf(routeString)));
                String orderNumString = officerReport.getForeignKey("orderNum");

                //get the partnership
                Partnership partnership = new Partnership(get("SELECT * FROM TblPartnerships WHERE ptshipNum = ?", partString).get(0));

                //get the officers in the partnership and add them
                List<Map<String,Object>> officersFromTbl = get("SELECT * FROM TblOperationalOfficers WHERE ptship = ?", partString);
                for (Map<String,Object> mapOfficers: officersFromTbl) {
                    partnership.addOfficerToPartnership(new OperationalOfficer(mapOfficers));
                }
                officerReport.setPart(partnership);

                //get the landmark
                Landmark landmark = new Landmark(get("SELECT * FROM TblLandmarks WHERE (route = ? AND orderNum = ?)", routeString, orderNumString).get(0));
                officerReport.setOrderNum(landmark);

                //get the defendant
                Defendant defendant = new Defendant(get("SELECT * FROM TblDefendants WHERE ID = ?", defendantString).get(0));
                officerReport.setDefendant(defendant);

                //get the appeal
                Appeal appeal = new Appeal(get("SELECT * FROM TblAppeals WHERE serialNum = ?", appealString).get(0));
                officerReport.setAppeal(appeal);

                //get the vehicle
                Vehicle vehicle = new Vehicle(get("SELECT * FROM TblVehicles WHERE licensePlate = ?", vehicleString).get(0));
                    //getting the model for the car
                String modelString = vehicle.getForeignKey("model");
                VehicleModel vehicleModel = new VehicleModel(get("SELECT * FROM TblVehicleModels WHERE modelNum = ?", modelString).get(0));
                vehicle.setModel(vehicleModel);
                officerReport.setVehicle(vehicle);

                reports.add(officerReport);
            }

            //get the volunteer reports
            List<Map<String,Object>> dataFromVolunteers = get("SELECT * FROM TblOfficerReport WHERE report_type = ?",0);
            for (Map<String,Object> map: dataFromVolunteers) {
                Report volunteerReport = new Report(map);

                String defendantString = volunteerReport.getForeignKey("defendant");
                String vehicleString = volunteerReport.getForeignKey("vehicle");
                String appealString = volunteerReport.getForeignKey("appeal");

                //get the defendant
                Defendant defendant = new Defendant(get("SELECT * FROM TblDefendants WHERE ID = ?", defendantString).get(0));
                volunteerReport.setDefendant(defendant);

                //get the vehicle
                Vehicle vehicle = new Vehicle(get("SELECT * FROM TblVehicles WHERE licensePlate = ?", vehicleString).get(0));
                    //getting the model for the car
                String modelString = vehicle.getForeignKey("model");
                VehicleModel vehicleModel = new VehicleModel(get("SELECT * FROM TblVehicleModels WHERE modelNum = ?", modelString).get(0));
                vehicle.setModel(vehicleModel);
                volunteerReport.setVehicle(vehicle);

                //get the appeal
                Appeal appeal = new Appeal(get("SELECT * FROM TblAppeals WHERE serialNum = ?", appealString).get(0));
                volunteerReport.setAppeal(appeal);

                reports.add(volunteerReport);
            }
            return reports;

        } catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public List<Appeal> getAllAppeals(AuthContext context) throws DSException {
        //context: HighRankOfficer (assumption: HighRankOfficer roleId = 1)

        isContextValidFor(context, roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); }, 1);
        List<Appeal> appeals = new ArrayList<>();
        try {
            List<Map<String,Object>> data = get("SELECT TblAppeals.*, TblDefendants.*, TblOfficerReport.* " +
                    "FROM TblDefendants INNER JOIN (TblAppeals INNER JOIN TblOfficerReport ON TblAppeals.serialNum = TblOfficerReport.appeal) ON TblDefendants.ID = TblOfficerReport.defendant");
            for(Map<String,Object> map: data) {
                Appeal appeal = new Appeal(map);
                Defendant defendant = new Defendant(map);
                Report report = new Report(map);

                appeal.setReport(report);
                appeal.setDefendant(defendant);

                appeals.add(appeal);
            }
            return appeals;
        } catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public List<Defendant> getAllDefendants(AuthContext context) throws DSException {
        //context: HighRankOfficer (assumption: HighRankOfficer roleId = 1)

        isContextValidFor(context, roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); }, 1);
        List<Defendant> defendants = new ArrayList<>();
        try {
            List<Map<String,Object>> data = get("SELECT * FROM TblDefendants");
            for(Map<String,Object> map: data)
                defendants.add(new Defendant(map));
            return defendants;
        } catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public List<Landmark> getLandmarks(AuthContext context) throws DSException {
        //context: HighRankOfficer (assumption: HighRankOfficer roleId = 1)

        isContextValidFor(context, roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); }, 1);
        List<Landmark> landmarks = new ArrayList<>();
        try  {
            List<Map<String,Object>> data = get("SELECT * FROM TblAppeals");
            for(Map<String,Object> map: data)
                landmarks.add(new Landmark(map));
            return landmarks;
        } catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public List<Partnership> getPartnerships(AuthContext context) throws DSException {
        //context: HighRankOfficer (assumption: HighRankOfficer roleId = 1)

        isContextValidFor(context, roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); }, 1);
        List<Partnership> partnerships = new ArrayList<>();
        try {
            List<Map<String,Object>> data = get("SELECT * FROM TblPartnerships");
            for(Map<String,Object> map: data)
                partnerships.add(new Partnership(map));
            return partnerships;
        } catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public List<Report> importReportsFromDingoReport(AuthContext context) throws DSException {
        return null;
    }

    @Override
    public List<Route> getRoutes(AuthContext context) throws DSException {
        //context: HighRankOfficer (assumption: HighRankOfficer roleId = 1)

        isContextValidFor(context, roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); }, 1);
        List<Route> routes = new ArrayList<>();
        try {
            List<Map<String,Object>> data = get("SELECT * FROM TblRoutes");
            for(Map<String, Object> map: data)
                routes.add(new Route(map));
            return routes;
        } catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public List<Shift> getShifts(AuthContext context) throws DSException {
        //context: HighRankOfficer (assumption: HighRankOfficer roleId = 1)

        isContextValidFor(context, roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); }, 1);
        List<Shift> shifts = new ArrayList<>();
        try {
            List<Map<String,Object>> data = get("SELECT * FROM TblShifts");
            for(Map<String,Object> map: data)
                shifts.add(new Shift(map));
            return shifts;
        } catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }

    }

    @Override
    public List<VehicleModel> getVehicleModels(AuthContext context) throws DSException {
        //context: HighRankOfficer (assumption: HighRankOfficer roleId = 1)

        isContextValidFor(context, roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); }, 1);
        List<VehicleModel> vehicleModels = new ArrayList<>();
        try {
            List<Map<String, Object>> data = get("SELECT * FROM TblVehicleModel");
            for(Map<String,Object> map: data)
                vehicleModels.add(new VehicleModel(map));
            return vehicleModels;
        } catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public List<Vehicle> getVehicles(AuthContext context) throws DSException {
        //context: HighRankOfficer (assumption: HighRankOfficer roleId = 1)

        isContextValidFor(context, roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); }, 1);

        //get the vehicles - > for each vehicle, with the vehicle model field (String model = vehicle.getForeignKey("model"))
        List<Vehicle> vehicles = new ArrayList<>();
        try {
            List<Map<String,Object>> data = get("SELECT * FROM TblVehicles");
            for(Map<String,Object> map: data) {
                //create a vehicle
                Vehicle vehicle = new Vehicle(map);

                //get the model number
                String model = vehicle.getForeignKey("model");

                //get the model from Tbl
                if (model != null) {
                    VehicleModel vehicleModel = new VehicleModel(get("SELECT * FROM TblVehicleModel WHERE modelNum = ?", model).get(0));
                    vehicle.setModel(vehicleModel);
                }
                //adding
                vehicles.add(vehicle);
            }
            return vehicles;
        }catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public boolean submitAppeal(AuthContext context, Appeal appeal, Report report) throws DSException {
        //check context

        try {
            //checking for existence of appeal
            int appealSerialNumString = appeal.getSerialNum();
            String appealQueryForVolunteersReports = "SELECT * FROM TblAppeals WHERE serialNum = ?";
            boolean appealValidator = get(appealQueryForVolunteersReports, String.valueOf(appealSerialNumString)).size() == 0;

            //if the appeal does not exists -> insert to the right Tbl and update the report
            if (appealValidator) {
                insert(appeal);

                //if we get false here -> means report does not exists -> delete appeal from Tbl (needs to be connected to report)
                if (addAppealToReport(context, appeal, report))
                    return true;
                else {
                    try {
                        delete("TblAppeals", "serialNum = ?", String.valueOf(appealSerialNumString));
                        return false;
                    } catch (SQLException e1) {
                        throw new DSFormatException(e1.getMessage());
                    }
                }
            }

        }catch (SQLException e) {
            throw new DSFormatException(e.getMessage());
        }
        return false;
    }

    @Override
    public void createUser(AuthContext context, Account account) throws DSException {
        int role = isContextValidFor(context,roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); },1);

        if(role == 1 && account.getROLE_ID() != 2)
            throw new DSAuthException("Head Officer can only add an officer.");
        try{
            //create account
            String id = ObjectId.generate();
            account.setID(id);

            //make copy
            Account toInsert = new Account(id,account.getEMAIL(),account.getROLE_ID(),account.getPassword());
            //insert copy
            insert(toInsert);

            if(account.getROLE_ID() == 2)
                insert(account);



        } catch (SQLException e) {
            e.printStackTrace();
            throw new DSFormatException(e.getMessage());
        }
    }

    @Override
    public List<Account> getAccounts(AuthContext context) throws DSException {
        isContextValidFor(context,roleId -> { if(roleId == -1) throw new DSAuthException("Invalid Context"); });
        try{
            List<Account> accounts = new ArrayList<>();
            get("SELECT ID,EMAIL,ROLE_ID FROM Accounts").forEach(
                    stringObjectMap -> accounts.add(new Account(stringObjectMap))
            );
            return accounts;
        }catch (SQLException e){
            throw new DSFormatException(e.getMessage());
        }
    }

    /**
     * This method checks if the given context is valid and returns the role id for that given context.
     * In other words, this method validates the session and then returns the role of the user.
     * @param context The auth context.
     * @return The role id of a given context or -1 if invalid.
     */
    private int isContextValid(AuthContext context){
        if(context.isValid())
            return context.getRole();

        try {
            List<Map<String,Object>> data = get("SELECT Accounts.ROLE_ID " +
                    "FROM Accounts INNER JOIN Sessions ON Accounts.ID = Sessions.ID " +
                    "WHERE (((Sessions.ID)= ? ) AND ((Sessions.SESSION_TOKEN)= ? ))", context.id, context.sessionToken);
            int role =  data.size() == 0 ? -1 : (Integer) data.get(0).get("ROLE_ID");
            context.setRole(role);
            return role;
        } catch (SQLException e) {
            return -1;
        }
    }

    /**
     *
     * @param context The auth context to check.
     * @param validator The closure to activate when we get a validation.
     * @param roles The roles that are allowed for this context.
     * @return The role id if valid, else -1.
     * @throws DSException
     */
    private int isContextValidFor(AuthContext context,ContextValidator validator,int...roles) throws DSException{
        int roleId = isContextValid(context);

        //invalid role id.
        if(roleId == -1) {
            validator.validWithRoleId(-1);
            return -1;
        }

        //if superuser always return true.
        if(roleId == 0){
            validator.validWithRoleId(0);
            return 0;
        }

        //check if the role id found is included in one of the roles given.
        for(int role : roles){
            if (role == roleId){
                validator.validWithRoleId(role);
                return role;
            }
        }

        validator.validWithRoleId(-1);
        return -1;
    }

    /**
     * Use this method to delete entries.
     *
     * Usage Example:
     *      @code {
     *
     *          //Example for deleting a session via token or id
     *          delete("SESSIONS","ID = ? OR TOKEN = ?",id,token);
     *
     *          //Simple Example for Deleting an account with a certain email.
     *          delete("ACCOUNTS","EMAIL = ?",email);
     *
     *          //Deleting multiple accounts with ids 32,542,22
     *          delete("ACCOUNTS","ID in (?, ?, ?)",32,542,22);
     *      }
     *
     * @param table The name of the table.
     * @param where The predicate/condition.
     * @param values The values.
     * @throws SQLException
     */
    protected boolean delete(String table, String where, Object... values) throws SQLException{

        String query = "DELETE FROM "+ table +" WHERE "+ where;

        PreparedStatement statement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);

        int index = 1;
        for(Object obj : values)
            statement.setObject(index++,obj);

        return statement.executeUpdate() != 0;
    }

    /**
     * Use this method to delete multiple entries. Instead of inserting many wildcards use `#` operator.
     *
     * @code {
     *
     *          deleteMany("ACCOUNTS","ID in (#)",32,542,22)
     *
     *          //Is the same as:
     *          delete("ACCOUNTS","ID in (?, ?, ?)",32,542,22);
     *      }
     *
     * @param table
     * @param where
     * @param values
     * @return
     * @throws SQLException
     */
    protected boolean deleteMany(String table,String where,String... values) throws SQLException{
        StringBuilder builder = new StringBuilder();

        for(String ignored : values)
            builder.append("?").append(",");

        builder.deleteCharAt(builder.length()-1);

        return delete(table,where.replace("#",builder.toString()),values);
    }

    /**
     *
     * Use this method to insert data into a certain table.
     *
     * Usage Example:
     *      @code {
     *          insert("SESSIONS",
     *               new Column("ID",id),
     *               new Column("SESSION_TOKEN",token),
     *               new Column("CREATION_DATE",new Date())
     *          );
     *      }
     *
     *      @see Column
     *
     *
     * @param table The name of the table.
     * @param values Var args of Pairs of Type (String:Object). Use Column for ease of use.
     * @param <T> The type of the generated key.
     * @return
     * @throws SQLException
     */
    protected<T> T insert(String table, Column...values) throws SQLException {

        StringBuilder builder1 = new StringBuilder();
        StringBuilder builder2 = new StringBuilder();

        for(Column value : values){
            if(!value.shouldIgnore()) {
                builder1.append(value.getKey()).append(",");
                builder2.append("?").append(",");
            }
        }

        builder1.deleteCharAt(builder1.length() - 1);
        builder2.deleteCharAt(builder2.length() - 1);

        String query  ="INSERT INTO "+table+" ("+builder1.toString()+") VALUES ("+builder2.toString()+");";

        PreparedStatement statement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);

        int index = 1;
        for(Column obj : values)
            if(!obj.shouldIgnore())
                statement.setObject(index++,obj.getValue());

        statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();

        return rs.next() ? (T) rs.getObject(1) : null;
    }

    /**
     *
     * @param table
     * @param dbObject
     * @param <T>
     * @return
     * @throws SQLException
     */
    protected <T> T insert(String table,DBObject dbObject) throws SQLException {
        return insert(table,dbObject.db_columns());
    }

    /**
     *
     * @param object
     * @param <T>
     * @return
     * @throws SQLException
     */
    protected <T> T insert(DBObject object) throws SQLException{
        return insert(object.db_table(),object.db_columns());
    }

    /**
     * Use this method to make Database Queries.
     *
     * Usage Example:
     *      @code { get("SELECT * FROM USERS WHERE EMAIL = ?",email) }
     *
     * @param query The Query String.
     * @return A List Of Hash Maps of Type (String:Object)
     * @throws SQLException
     */
    protected List<Map<String,Object>> get(String query,Object...args) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(query);

        int index = 1;
        for(Object val : args)
            statement.setObject(index++,val);

        ResultSet set = statement.executeQuery();

        String[] columns = new String[set.getMetaData().getColumnCount()];

        for (int i = 1; i <= columns.length; i++ )
            columns[i - 1] = set.getMetaData().getColumnName(i);

        List<Map<String,Object>> data = new ArrayList<>();

        while (set.next()){
            Map<String,Object> map = new HashMap<>();
            for(String name : columns)
                map.put(name,set.getObject(name));

            data.add(map);

        }

        return data;
    }

    /**
     * Use this method to update an existing entry.
     *
     * @param table The table
     * @param where The condition
     * @param values The values to set/update
     * @return
     * @throws SQLException
     */
    private boolean update(String table,Where where, Column...values) throws SQLException {

        StringBuilder builder = new StringBuilder();

        for(Column value : values)
            builder.append(value.getKey()).append(" = ").append("?,");

        builder.deleteCharAt(builder.length()-1);

        String query  ="UPDATE "+table+" SET "+builder.toString()+" WHERE "+where.syntax;
        PreparedStatement statement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);

        int index = 1;
        for(Column obj : values)
            if(!obj.shouldIgnore())
                statement.setObject(index++,obj.getValue());

        for(Object o : where.values)
            statement.setObject(index++,o);

        return statement.executeUpdate() != 0;
    }

    private boolean validateFileUrl(String url,String ownerId,boolean canBeNullOrEmpty,String... types){
        if(canBeNullOrEmpty && (url == null || url.isEmpty()))
            return true;
        else if (url == null || url.isEmpty())
            return false;

        // "/resources/{owner_id}/{file_name}.{type}"
        String[] contents = url.split("/");

        if(contents.length != 3)
            return false;

        String owner = contents[1];

        if(!owner.equals(ownerId))
            return false;

        String file = contents[2];
        String type = file.split("\\.")[1];

        for(String s : types){
            if(s.equalsIgnoreCase(type))
                return true;
        }
        return false;
    }

    @FunctionalInterface
    interface ContextValidator{
        void validWithRoleId(int roleId) throws DSException;
    }

    /**
     * Class used to create Where Predicates
     */
    class Where {
        final String syntax;
        final Object[] values;

        public Where(String syntax, Object...values) {
            this.syntax = syntax;
            this.values = values;
        }
    }

}

/*
Roles Ids:
    1.HighRankOfficer
    2.OperationalOfficer
    3.OnCallOfficer
    4.Defendant
 */