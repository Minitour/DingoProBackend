package mobi.newsound.models;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

import java.util.Date;
import java.util.Optional;

public class OfficerReport extends DBObject {

    @Expose
    private String alphaNum;

    @Expose
    private Date violationDate;

    @Expose
    private String description;

    @Expose
    private String status;

    @Expose
    private String violationType;

    @Expose
    private Defendant defendant;

    @Expose
    private Vehicle vehicle;

    @Expose
    private Appeal appeal;

    @Expose
    private Partnership part;

    @Expose
    private Route route;

    @Expose
    private Landmark orderNum;

    public OfficerReport(String alphaNum, Date violationDate, String description, String status, String violationType, Defendant defendant, Vehicle vehicle, Appeal appeal, Partnership part, Route route, Landmark orderNum) {
        setAlphaNum(alphaNum);
        setViolationDate(violationDate);
        setDescription(description);
        setStatus(status);
        setViolationType(violationType);
        setDefendant(defendant);
        setVehicle(vehicle);
        setAppeal(appeal);
        setPart(part);
        setRoute(route);
        setOrderNum(orderNum);
    }

    public void setAlphaNum(String alphaNum) {
        this.alphaNum = alphaNum;
    }

    public void setViolationDate(Date violationDate) {
        this.violationDate = violationDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setViolationType(String violationType) {
        this.violationType = violationType;
    }

    public void setDefendant(Defendant defendant) {
        this.defendant = defendant;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public void setAppeal(Appeal appeal) {
        this.appeal = appeal;
    }

    public void setPart(Partnership part) {
        this.part = part;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public void setOrderNum(Landmark orderNum) {
        this.orderNum = orderNum;
    }

    public String getAlphaNum() {
        return alphaNum;
    }

    public Date getViolationDate() {
        return violationDate;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getViolationType() {
        return violationType;
    }

    public Defendant getDefendant() {
        return defendant;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Appeal getAppeal() {
        return appeal;
    }

    public Partnership getPart() {
        return part;
    }

    public Route getRoute() {
        return route;
    }

    public Landmark getOrderNum() {
        return orderNum;
    }

    @Override
    public Column[] db_columns() {
        return new Column[]{
                new Column("alphaNum",alphaNum),
                new Column("violationDate",violationDate),
                new Column("description",description),
                new Column("status",status),
                new Column("violationType",violationType),
                new Column<>("defendant", Optional.ofNullable(defendant), Defendant::getId), //FK
                new Column<>("vehicle", Optional.ofNullable(vehicle), Vehicle::getLicensePlate), //FK
                new Column<>("appeal", Optional.ofNullable(appeal), Appeal::getSerialNum), //FK
                new Column<>("part", Optional.ofNullable(part), Partnership::getPtshipNum), //FK
                new Column<>("route", Optional.ofNullable(route), Route::getSerialNum), //FK
                new Column<>("orderNum", Optional.ofNullable(orderNum), Landmark::getOrderNum) //FK
        };
    }


}
