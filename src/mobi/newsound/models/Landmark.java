package mobi.newsound.models;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

public class Landmark extends DBObject {

    @Expose
    private int route;

    @Expose
    private int orderNum;

    @Expose
    private String plannedArrivalTime;

    @Expose
    private String latitude;

    @Expose
    private String longitude;

    public Landmark(int route, int orderNum, String plannedArrivalTime, String latitude, String longitude) {
        setRoute(route);
        setOrderNum(orderNum);
        setPlannedArrivalTime(plannedArrivalTime);
        setLatitude(latitude);
        setLongitude(longitude);
    }

    public void setRoute(int route) {
        this.route = route;
    }

    public void setOrderNum(int orderNum) {
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

    public int getRoute() {
        return route;
    }

    public int getOrderNum() {
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
                new Column("route",route),
                new Column("orderNum",orderNum),
                new Column("plannedArrivalTime",plannedArrivalTime),
                new Column("latitude",latitude),
                new Column("longitude",longitude)
        };
    }
}
