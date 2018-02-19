package mobi.newsound.model;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

import java.util.Map;
import java.util.Optional;

public class Landmark extends DBObject {

    @Expose
    private Route route;

    @Expose
    private Integer orderNum;

    @Expose
    private String plannedArrivalTime;

    @Expose
    private String latitude;

    @Expose
    private String longitude;

    public Landmark(Route route, Integer orderNum, String plannedArrivalTime, String latitude, String longitude) {
        setRoute(route);
        setOrderNum(orderNum);
        setPlannedArrivalTime(plannedArrivalTime);
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public Landmark(Map<String,Object> map) {
        super(map);
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public void setPlannedArrivalTime(String plannedArrivalTime) {
        this.plannedArrivalTime = plannedArrivalTime;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Route getRoute() {
        return route;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public String getPlannedArrivalTime() {
        return plannedArrivalTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public Column[] db_columns() {
        return new Column[]{
                new Column<>("route", Optional.ofNullable(route), Route::getSerialNum), //FK
                new Column("orderNum",orderNum),
                new Column("plannedArrivalTime",plannedArrivalTime),
                new Column("latitude",latitude),
                new Column("longitude",longitude)
        };
    }
}
