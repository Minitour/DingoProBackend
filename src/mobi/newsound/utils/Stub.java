package mobi.newsound.utils;

import mobi.newsound.model.*;

import java.util.*;

/**
 * Created by Antonio Zaitoun on 11/02/2018.
 */
public final class Stub {

    //TODO: replace UUID.randomUUID().toString() with real stubs using a stub lib.

    public static Report getReportStub() {
        String alphaNum = UUID.randomUUID().toString();
        Date violationDate = new Date();
        String description = UUID.randomUUID().toString();
        String status = UUID.randomUUID().toString();
        String violationType = UUID.randomUUID().toString();
        Defendant defendant = getDefendantStub();
        Vehicle vehicle = getVehicleStub();
        Appeal appeal = getAppealStub();
        String evidenceLink = UUID.randomUUID().toString();;
        Report report = new Report(alphaNum, violationDate, description, status, violationType, defendant, vehicle, appeal);
        report.setReport_type(0);
        report.setEvidenceLink(evidenceLink);
        return report;
        //Report(alphaNum, violationDate, description, status, violationType, defendant, vehicle, appeal)


        //        int reportNum = new Random().nextInt(1000);
//        String description = UUID.randomUUID().toString();
//        Date date = new Date();
//        Volunteer volunteer = getVolunteerStub();
//        Vehicle vehicle = getVehicleStub();
//        Report report = new Report(reportNum,description,date,volunteer,vehicle);
//
//        int rnd = new Random().nextInt(10) + 1;
//        List<Violation> violationList = new ArrayList<>();
//
//        for (int i = 0; i < rnd; i++)
//            violationList.add(getViolationStub());
//
//        report.setViolations(violationList);
//
//        return report;
    }

    //Defendant(int id, String drivingLicense, String name, String address)
    public static Defendant getDefendantStub() {
        int id = new Random().nextInt(1000);
        String drivingLicense = UUID.randomUUID().toString();
        String name = UUID.randomUUID().toString();
        String address = UUID.randomUUID().toString();
        return new Defendant(id,drivingLicense,name, address);
    }

    //Appeal(int serialNum, String reason, Date appealDate)
    public static Appeal getAppealStub() {
        int serialNum = new Random().nextInt(1000);
        String reason = UUID.randomUUID().toString();
        Date appealDate = new Date();
        return new Appeal(serialNum,reason,appealDate);
    }

    //Vehicle(String licensePlate, String colorHEX, VehicleModel model)
    public static Vehicle getVehicleStub(){
        String licensePlate = UUID.randomUUID().toString();
        String colorHEX = UUID.randomUUID().toString();
        VehicleModel model = getVehicleModelStub();
        return new Vehicle(licensePlate, colorHEX, model);
    }

    //public VehicleModel(String modelNum, String name)
    public static VehicleModel getVehicleModelStub(){
        String modelNum = UUID.randomUUID().toString();
        String name = UUID.randomUUID().toString();
        return new VehicleModel(modelNum, name);
    }

    //TODO: add other data type stubs.
}
