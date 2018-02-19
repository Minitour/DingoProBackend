package mobi.newsound.model;

import com.google.gson.annotations.Expose;
import mobi.newsound.database.Column;
import mobi.newsound.database.DBObject;

import java.util.Map;

public class Defendant extends DBObject {

    @Expose
    private Integer ID;

    @Expose
    private String drivingLicense;

    @Expose
    private String name;

    @Expose
    private String address;

    public Defendant(Integer id, String drivingLicense, String name, String address) {
        setID(id);
        setDrivingLicense(drivingLicense);
        setName(name);
        setAddress(address);
    }

    public Defendant(Map<String,Object> map) {
        super(map);
    }

    public void setID(Integer id) {
        this.ID = id;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getID() {
        return ID;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public Column[] db_columns() {
        return new Column[]{
                new Column("ID",ID),
                new Column("drivingLicense",drivingLicense),
                new Column("name",name),
                new Column("address",address)
        };
    }

    @Override
    public String db_table() {
        return "TblDefendants";
    }

}
