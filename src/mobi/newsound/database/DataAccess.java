package mobi.newsound.database;

import mobi.newsound.model.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static mobi.newsound.database.Database.JASPER_BIN;

public interface DataAccess extends AutoCloseable,Serializable{

    static DataAccess getInstance(){
        try {
            return new Database();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    default boolean isValid(AuthContext context) throws DSException { throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @param account
     * @throws DSException
     */
    default void createUser(AuthContext context,Account account) throws DSException {throw new DSUnimplementedException();}

    default List<Account> getAccounts(AuthContext context) throws DSException {throw new DSUnimplementedException();}

    /**
     * Sign in with email and password
     *
     * @param email
     * @param password_raw
     * @return Auth Context
     */
    default AuthContext signIn(String email,String password_raw) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @param currentPassword
     * @param newPassword
     * @return true if the password was updated.
     */
    default boolean updatePassword(AuthContext context,String currentPassword,String newPassword) throws DSException {throw new DSUnimplementedException();}

    /**
     * Reset the password and return an auto-generated password
     * @param context
     * @return The generated password.
     */
    default String resetPassword(AuthContext context) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @param appeal
     * @param report
     * @return
     * @throws DSException
     */
    default boolean addAppealToReport(AuthContext context, Appeal appeal, Report report) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @param landmark
     * @param route
     * @return
     * @throws DSException
     */
    default boolean addLandmarksToRoutes(AuthContext context, Landmark landmark) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @param officers
     * @param partnerships
     * @return
     * @throws DSException
     */
    default boolean assignOfficersToPartnerships(AuthContext context, OperationalOfficer officers, Partnership partnerships) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @param partnership
     * @param shift
     * @return
     * @throws DSException
     */
    default boolean assignPartnershipToShift(AuthContext context, Partnership partnership, Shift shift) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @param route
     * @param shift
     * @return
     * @throws DSException
     */
    default boolean assignRouteToShift(AuthContext context, Route route, Shift shift) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @param defendant
     * @return
     * @throws DSException
     */
    default boolean createDefendant(AuthContext context, Defendant defendant) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @param report
     * @return
     * @throws DSException
     */
    default boolean createOfficerReport(AuthContext context, Report report) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @param partnership
     * @return
     * @throws DSException
     */
    default boolean createPartnership(AuthContext context) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @param report
     * @return
     * @throws DSException
     */
    default boolean createVolunteerReport(AuthContext context, Report report) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @param report
     * @return
     * @throws DSException
     */
    default Report createGeneralReport(AuthContext context, Report report) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @param reports
     * @throws DSException
     */
    default void getAppealExport(AuthContext context, List<Report> reports) throws DSException {throw new DSUnimplementedException();}

    /**
     * @param from
     * @param to
     * @param context
     * @param os - The reports to export
     * @throws DSException
     */
    default void getReportsExportByDate(AuthContext context, Date from, Date to, OutputStream os) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @param officerReports
     * @throws DSException
     */
    default List<Report> getAllOfficerReportsExportToDingoReport(AuthContext context) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @return
     * @throws DSException
     */
    default List<OperationalOfficer> getAllOfficers(AuthContext context) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @return
     * @throws DSException
     */
    default List<Report> getAllReports(AuthContext context) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @return
     * @throws DSException
     */
    default List<Appeal> getAllAppeals(AuthContext context) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @return
     * @throws DSException
     */
    default List<Defendant> getAllDefendants(AuthContext context) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @return
     * @throws DSException
     */
    default List<Landmark> getLandmarks(AuthContext context) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @return
     * @throws DSException
     */
    default List<Partnership> getPartnerships(AuthContext context) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @return
     * @throws DSException
     */
    default List<Report> importReportsFromDingoReport(AuthContext context) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @return
     * @throws DSException
     */
    default List<Route> getRoutes(AuthContext context) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @return
     * @throws DSException
     */
    default List<Shift> getShifts(AuthContext context) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @return
     * @throws DSException
     */
    default List<VehicleModel> getVehicleModels(AuthContext context) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @return
     * @throws DSException
     */
    default List<Vehicle> getVehicles(AuthContext context) throws DSException {throw new DSUnimplementedException();}

    /**
     *
     * @param context
     * @param appeal
     * @return
     * @throws DSException
     */
    default boolean submitAppeal(AuthContext context, Appeal appeal, Report report) throws DSException {throw new DSUnimplementedException();}


    default void testJasper (Date from, Date to,OutputStream os) throws DSException {throw new DSUnimplementedException(); }

    default void createRoute(AuthContext context) throws DSException {throw new DSUnimplementedException();}


    abstract class DSException extends RuntimeException {
        DSException(){}
        DSException(String message){
            super(message);
        }
    }

    class DSUnimplementedException extends DSException{
        public DSUnimplementedException() {
            super("Unimplemented");
        }

        DSUnimplementedException(String message) {
            super(message);
        }
    }

    class DSFormatException extends DSException {
        public DSFormatException() {
        }

        DSFormatException(String message) {
            super(message);
        }
    }

    class DSAuthException extends DSException{
        public DSAuthException() {
        }

        DSAuthException(String message) {
            super(message);
        }
    }
}
